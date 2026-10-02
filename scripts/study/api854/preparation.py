"""Shared fixed-context/prompt contract. No logs, implicit approval, or API calls."""
import hashlib
import json
import re
from pathlib import Path

CONTRACT = "aom-beam-prepare-v2"
CONTEXT_POLICY = "modified-java-and-root-build-v1"
PROMPT_POLICY = "shared-fixed-targets-junit4-v2"
POLICY = {
    "schema_version": 1, "contract": CONTRACT, "context_policy_id": CONTEXT_POLICY,
    "prompt_policy_id": PROMPT_POLICY, "target_selection": "shared-declaration-signatures-v1",
    "fixture_policy": "common-fixed-buggy-production-types-v1",
    "fixture_selection": "SqaProbe discovery with sorted common compiled production types; no buggy outcomes",
    "target_identity_fields": ["class", "constructor_types", "method", "parameter_types"],
    "context": "all modified fixed production Java and known root build files; same bytes across four approaches",
    "ai_inputs": "fixed context, shared declaration signatures and fixture types only; no patches, triggers or execution logs",
    "suite_packaging": "beam-java-suite-v1", "test_method_cap": 30,
    "over_cap": "reject entire suite as generation_failed; preserve raw output; no trimming or semantic retry",
    "method_count": "lexical JUnit4 annotations for packaging; executed/skipped measured separately",
    "compatibility_policy": "none", "extraction": "complete Java fences in source order, package/class file layout only",
    "assertion_edits": False, "fixed_failure_pruning": False, "semantic_repairs": "new disclosed condition only",
    "oracle_policy": "meaningful deterministic assertions; fixed twice, same suite buggy and coverage; semantic review required",
    "seed": 101, "budget": 30, "fixed_observations_per_proposal": 2,
    "approval_state": "proposal_pending_three_owner_review",
}


def encoded(value):
    return (json.dumps(value, ensure_ascii=False, sort_keys=True, indent=2) + "\n").encode("utf-8")


def digest(data):
    return hashlib.sha256(data).hexdigest()


def clean_targets(targets):
    """Expose declaration inputs only, never discovery errors or buggy behavior."""
    fields = POLICY["target_identity_fields"]
    result = []
    seen = set()
    for row in targets:
        if not isinstance(row, dict) or any(not isinstance(row.get(k), str) for k in fields):
            raise ValueError("Malformed shared target declaration")
        if (not re.fullmatch(r"[A-Za-z_$][\w$]*(?:\.[A-Za-z_$][\w$]*)*", row["class"])
                or not re.fullmatch(r"<init>|[A-Za-z_$][\w$]*", row["method"])
                or any(not re.fullmatch(r"[A-Za-z0-9_$.,;\[\]]*", row[k]) for k in ("constructor_types", "parameter_types"))):
            raise ValueError("Target inventory may contain declaration signatures only")
        identity = tuple(row[k] for k in fields)
        if identity in seen:
            raise ValueError("Duplicate target signature")
        seen.add(identity)
        result.append({k: row[k] for k in fields})
    return sorted(result, key=lambda row: tuple(row[k] for k in fields))


def compose(directory: Path, *, classes, targets=None, previous_hashes=None):
    """Add immutable artifacts to an existing export_context destination."""
    manifest_path = directory / "context-manifest.json"
    manifest = json.loads(manifest_path.read_text(encoding="utf-8"))
    if manifest["selection_policy_id"] != CONTEXT_POLICY or manifest["contains_execution_logs"] is not False:
        raise ValueError("Fixed context selection differs from shared policy")
    files = manifest["source_files"]
    source_hash = digest(json.dumps(files, sort_keys=True, separators=(",", ":")).encode())
    if manifest["source_hash"] != source_hash:
        raise ValueError("Context manifest source hash changed")
    for row in files:
        source = (directory / "fixed-source" / row["path"]).resolve(strict=True)
        if not source.is_relative_to((directory / "fixed-source").resolve()) or digest(source.read_bytes()) != row["sha256"]:
            raise ValueError("Fixed source differs from manifest")
    declarations = clean_targets(targets or [])
    targets_document = {"schema_version": 1, "target_selection": POLICY["target_selection"],
        "fixture_policy": POLICY["fixture_policy"], "targets": declarations,
        "status": "shared_declarations_discovered" if declarations else "pending_discovery",
        "semantic_validity": "pending_review"}
    targets_bytes = encoded(targets_document)
    context = (directory / "context.md").read_text(encoding="utf-8")
    instruction = (
        "Generate a deterministic JUnit 4 Java suite using only the supplied fixed source, build context, "
        "shared target declarations and fixture policy. Return complete Java code fences with explicit "
        "package declarations, public test classes and imports. Use at most 30 @Test methods. "
        "Use meaningful assertions derived from fixed API behavior; avoid non-null-only or empty tests, "
        "randomness, time dependence and external services. Do not modify or shadow production code. "
        "No assertion repairs or feedback loop. Suites exceeding 30 methods are rejected entirely.\n\n"
        f"Project: {manifest['project']}; fixed revision: {manifest['revision']}.\n"
        "Modified target classes:\n" + "\n".join(sorted(set(classes))) + "\n\n"
        "Fixture policy: common production types in fixed/buggy; simplest supported constructor selected "
        "by the shared probe. Use only the listed shared signatures and common fixture types.\n\n"
        "Shared target declarations:\n```json\n" + json.dumps(declarations, sort_keys=True, indent=2) + "\n```\n\n")
    prompt = (instruction + context).encode("utf-8")
    metadata = {"prepare_contract": CONTRACT, "source_sha256": source_hash,
        "context_source_hash": source_hash,
        "fixed_source_sha256": {row["path"]: row["sha256"] for row in files if row["path"].endswith(".java")},
        "context_policy_id": CONTEXT_POLICY, "context_selection": CONTEXT_POLICY,
        "prompt_sha256": digest(prompt), "prompt_policy_id": PROMPT_POLICY,
        "prompt_utf8_bytes": len(prompt), "targets_sha256": digest(targets_bytes),
        "target_count": len(declarations), "target_selection": POLICY["target_selection"],
        "fixture_policy": POLICY["fixture_policy"], "policy_sha256": digest(encoded(POLICY)),
        "adapter_eligibility_verified": bool(declarations), "semantic_validity": "pending_review",
        "approval_state": "proposal_pending_three_owner_review", "previous_hashes": previous_hashes or {}}
    for name, data in {"prompt.md": prompt, "targets.json": targets_bytes,
                       "prepare-policy.json": encoded(POLICY), "prepare-metadata.json": encoded(metadata)}.items():
        with (directory / name).open("xb") as stream:
            stream.write(data)
    return metadata


def validate(manifest, metadata, prompt, targets, policy, *, require_eligible=False):
    files = manifest["source_files"]
    source_hash = digest(json.dumps(files, sort_keys=True, separators=(",", ":")).encode())
    expected = {"source_sha256": source_hash, "context_source_hash": source_hash,
                "prompt_sha256": digest(prompt), "targets_sha256": digest(targets), "policy_sha256": digest(policy),
                "fixed_source_sha256": {f["path"]: f["sha256"] for f in files if f["path"].endswith(".java")}}
    if any(metadata.get(k) != v for k, v in expected.items()) or manifest.get("source_hash") != source_hash:
        raise ValueError("Preparation hashes or fixed-source mapping differ")
    if (metadata.get("prepare_contract") != CONTRACT or manifest.get("selection_policy_id") != CONTEXT_POLICY
            or metadata.get("context_policy_id") != CONTEXT_POLICY or metadata.get("context_selection") != CONTEXT_POLICY
            or metadata.get("prompt_policy_id") != PROMPT_POLICY or json.loads(policy) != POLICY):
        raise ValueError("Preparation policy differs")
    document = json.loads(targets)
    if (document.get("target_selection") != POLICY["target_selection"]
            or document.get("fixture_policy") != POLICY["fixture_policy"]
            or metadata.get("target_selection") != POLICY["target_selection"]
            or metadata.get("fixture_policy") != POLICY["fixture_policy"]):
        raise ValueError("Target/fixture policy differs")
    declarations = clean_targets(document["targets"])
    if metadata.get("target_count") != len(declarations) or metadata.get("adapter_eligibility_verified") is not bool(declarations):
        raise ValueError("Eligibility state differs from declarations")
    if require_eligible and not declarations:
        raise ValueError("Shared declarations are required before generation")
    return True
