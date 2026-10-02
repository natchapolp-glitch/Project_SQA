"""Repository callable for Champ CLI with Beam suite/lineage publication.

No KKU transport, claim, fallback or protocol approval happens in this module.
The frozen shared protocol must explicitly select this resolver and its policies.
"""
from __future__ import annotations

import json
from pathlib import Path, PurePosixPath

from .ai_handoff import BeamGenerationHandoff
from .common import (assert_implementation, read_json, sha256, snapshot_implementation,
                     validate_protocol, write_json)
from .kku_client import digest
from .suite_resolver import BeamSuiteResolver

RESOLVER_SPEC = "scripts.study.api854.champ_bridge:champ_suite_resolver"


def protocol_document(settings):
    if not settings.protocol_bytes or digest(settings.protocol_bytes) != settings.protocol_hash:
        raise ValueError("Resolver needs the original, hash-bound shared protocol bytes")
    protocol = json.loads(settings.protocol_bytes)
    validate_protocol(protocol)
    generation = protocol.get("generation", {})
    if protocol.get('fixture_policy_id'):
        from .prepare_worker import EXPLICIT_PROMPT_POLICY
        if (generation.get('fixture_policy_id') != protocol['fixture_policy_id']
                or generation.get('prompt_policy_id') != EXPLICIT_PROMPT_POLICY):
            raise ValueError('Explicit fixture policy/prompt has not been frozen consistently')
    if (protocol.get("status") != "frozen" or protocol.get("approval_state") != "frozen"
            or protocol.get("suite_packaging") != BeamSuiteResolver.policy_id
            or protocol.get("test_method_cap") != 30
            or settings.suite_resolver != RESOLVER_SPEC
            or generation.get("suite_resolver") != RESOLVER_SPEC
            or settings.suite_policy_id != BeamSuiteResolver.policy_id
            or protocol.get("context_selection") != settings.context_policy_id
            or generation.get("context_policy_id") != settings.context_policy_id
            or generation.get("prompt_policy_id") != settings.prompt_policy_id):
        raise ValueError("Shared protocol has not frozen the Beam/Champ processing contract")
    for approach, model in settings.models.items():
        if protocol.get("model_policy", {}).get(approach, {}).get("kku_model_id") != model["id"]:
            raise ValueError("Beam/Champ requested model policies differ")
    return protocol


def prepared_binding(client, job, settings):
    # Reuse Champ's exact prompt/context checks; no source or prompt rewriting.
    from .api_worker import resolve_prepared_job
    generation_job = resolve_prepared_job(client, job, settings)
    protocol = protocol_document(settings)
    prepared = next(h for h in reversed(job["payload"]["stage_history"])
                    if h.get("stage") == "prepare" and h.get("outcome") == "prepared")
    metadata = prepared.get("metadata", {})
    def download(name):
        found = [a for a in prepared.get("artifacts", []) if a.get("name") == name]
        if len(found) != 1 or type(found[0].get("size")) is not int or not 0 < found[0]["size"] <= 20 * 1024 * 1024:
            raise ValueError("Beam preparation artifact missing, ambiguous or oversized")
        data = client.download(found[0])
        if len(data) != found[0]["size"] or digest(data) != found[0]["sha256"]:
            raise ValueError("Beam preparation artifact bytes/hash differ")
        return data, found[0]
    raw_manifest, _ = download("context-manifest.json")
    manifest = json.loads(raw_manifest)
    sources = {}
    for entry in manifest["source_files"]:
        name = entry["path"]
        path = PurePosixPath(name)
        if (path.is_absolute() or ".." in path.parts or "\\" in name or ":" in name
                or name in sources):
            raise ValueError("Unsafe or duplicated fixed-context source")
        if path.suffix == ".java":
            sources[name] = entry["sha256"]
    fixed_sources = metadata.get("fixed_source_sha256")
    if (not isinstance(fixed_sources, dict) or not fixed_sources
            or any(sources.get(name) != digest for name, digest in fixed_sources.items())
            or metadata.get("context_source_hash") != generation_job.source_hash):
        raise ValueError("Prepared fixed-source mapping/context hash is absent or differs")
    additional = {name: digest for name, digest in sources.items() if name not in fixed_sources}
    if additional and (protocol.get("context_selection") != "beam-modified-and-shared-receiver-java-v2-proposal"
            or metadata.get("additional_receiver_source_sha256") != additional):
        raise ValueError("Additional receiver sources require the explicit shared v2 policy and mapping")
    raw_targets, target_artifact = download("targets.json")
    document = json.loads(raw_targets)
    targets = document.get("targets") if isinstance(document, dict) else None
    if (not isinstance(targets, list) or not targets or metadata.get("targets_sha256") != digest(raw_targets)
            or metadata.get("target_count") != len(targets)):
        raise ValueError("Prepared common targets are empty or differ from metadata")
    if protocol.get('fixture_policy_id'):
        from .fixture_policy import select
        policy = protocol['fixture_policy_id']
        if metadata.get('fixture_policy_id') != policy:
            raise ValueError('Prepared fixture policy differs from frozen generation')
        raw_recipe, _ = download('fixture-recipes.json')
        recipe = json.loads(raw_recipe)
        names = ['algorithms/java/SqaProbe.java', 'scripts/study/api854/fixture_policy.py']
        expected = {name: protocol['source_sha256'][name] for name in names}
        if (metadata.get('fixture_recipes_sha256') != digest(raw_recipe)
                or recipe.get('fixture_policy_id') != policy or recipe.get('source_sha256') != expected
                or not isinstance(recipe.get('sources'), dict) or set(recipe['sources']) != set(names)
                or any(not isinstance(recipe['sources'][name], str) or digest(recipe['sources'][name].encode('utf-8')) != expected[name] for name in names)):
            raise ValueError('Prepared fixture recipe bytes/source hashes differ')
        if select(targets, policy)[1]:
            raise ValueError('Prepared targets do not match explicit fixture capabilities')
    if additional:
        modified = metadata.get("target_classes")
        if not isinstance(modified, list) or not modified:
            raise ValueError("Supplementary context requires retained modified target classes")
        receiver_names = {target["class"] for target in targets} - set(modified)
        for name in additional:
            if not any(name.endswith("/" + receiver.split("$", 1)[0].replace(".", "/") + ".java")
                       for receiver in receiver_names):
                raise ValueError("Additional source is not an eligible shared concrete receiver")
    return {"schema_version": 1, "protocol": protocol, "protocol_hash": settings.protocol_hash,
            "fixed_source_sha256": fixed_sources, "context_source_hash": generation_job.source_hash,
            "prepare_attempt_id": prepared["attempt_id"],
            "targets_sha256": digest(raw_targets), "target_artifact_id": target_artifact["artifact_id"]}


class BoundChampHandoff(BeamGenerationHandoff):
    def __init__(self, client, claim, *, binding, heartbeat, worker_id, credential_secrets, private_root):
        from .queue_worker import local_job
        self.binding = {**binding, "job": local_job(claim)}
        super().__init__(client, claim, heartbeat=heartbeat, job=self.binding["job"],
            protocol=binding["protocol"], fixed_source_sha256=binding["fixed_source_sha256"],
            context_source_hash=binding["context_source_hash"], worker_id=worker_id,
            credential_secrets=credential_secrets)
        self.resolver = champ_suite_resolver
        # Record the approved inputs/code BEFORE any paid provider send.
        folder = Path(private_root) / f"beam-input-{claim['attempt_id']}"
        folder.mkdir(parents=True, exist_ok=False)
        write_json(folder / "suite-input-binding.json", self.binding)
        snapshot_implementation(folder, binding["protocol"]["source_sha256"])

    def publish_generation(self, result):
        assert_implementation(self.binding["protocol"]["source_sha256"])
        folder = Path(result["artifact_path"])
        retained = folder / "suite-input-binding.json"
        if retained.exists():
            if read_json(retained) != self.binding:
                raise ValueError("Retained generation input binding changed")
        else:
            write_json(retained, self.binding)
            snapshot_implementation(folder, self.binding["protocol"]["source_sha256"])
        return super().publish_generation(result)


class ChampSuiteResolver:
    """One-argument callable plus explicit preclaim/claimed-handoff hooks."""
    policy_id = BeamSuiteResolver.policy_id

    def __call__(self, result):
        binding = read_json(Path(result["artifact_path"]) / "suite-input-binding.json")
        if (binding.get("protocol_hash") != result.get("protocol_hash")
                or binding.get("job", {}).get("protocol_hash") != result.get("protocol_hash")):
            raise ValueError("Suite binding differs from the immutable generation protocol")
        validate_protocol(binding["protocol"])
        return BeamSuiteResolver(binding["job"], binding["protocol"], binding["fixed_source_sha256"],
                                 binding["context_source_hash"])(result)

    def validate_prepared(self, client, job, settings):
        return prepared_binding(client, job, settings)

    def create_handoff(self, client, claim, *, heartbeat, settings, worker_id, credential_secrets, private_root):
        binding = self.validate_prepared(client, claim["job"], settings)
        return BoundChampHandoff(client, claim, binding=binding, heartbeat=heartbeat,
            worker_id=worker_id, credential_secrets=credential_secrets, private_root=private_root)


champ_suite_resolver = ChampSuiteResolver()
