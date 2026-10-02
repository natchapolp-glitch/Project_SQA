"""One-job CLI worker for Aom queue 1.0. Draft protocols cannot claim/send.

Explicit --once is required for mutation. --check reads readiness only.
Settings/input contract below are a proposal until Aom freezes them in the
same protocol bytes used to seed the queue. No account rotation or retry.
"""
from __future__ import annotations

import argparse
from dataclasses import dataclass
from datetime import datetime, timezone
import importlib
import json
import math
from pathlib import Path
import re
import sys

from .champ_queue import ChampQueueClient, QueueAccess, QueueError, QueueGenerationHandoff
from .generate_worker import GenerationJob, GenerationWorker, write_json
from .kku_client import KKUClient, KKUError, digest, load_account, sanitize, utc_now
from .lease import LeaseHeartbeat, generate_with_lease
from .models import load_selection, resolve_selected
from .quota import QuotaBlocked, QuotaLedger

CONTRACT = "champ-generation-input-v1"


class WorkerBlocked(Exception):
    """Stable public reason, never includes credentials or raw exceptions."""


@dataclass(frozen=True)
class FrozenSettings:
    protocol_hash: str
    models: dict
    temperature: float
    max_tokens: int
    prompt_token_reserve: int
    context_policy_id: str
    prompt_policy_id: str
    suite_policy_id: str
    suite_resolver: str
    start_at: datetime
    generation_cutoff_at: datetime
    handoff_contract: str = "champ-v1"
    protocol: dict | None = None
    prepare_contract: str = "champ-v1"

    @classmethod
    def load(cls, path: Path):
        data = path.read_bytes()
        protocol = json.loads(data)
        if protocol.get("approval_state") != "frozen":
            raise WorkerBlocked("protocol_not_frozen")
        models = protocol.get("models")
        if models != load_selection():
            raise WorkerBlocked("model_selection_not_frozen")
        generation = protocol.get("generation") or {}
        prepare_contract = generation.get("prepare_contract", "champ-v1")
        if prepare_contract not in {"champ-v1", "aom-beam-prepare-v2"}:
            raise WorkerBlocked("prepare_contract_not_frozen")
        if prepare_contract == "aom-beam-prepare-v2":
            from .preparation import POLICY, encoded, digest as prepare_digest
            if (generation.get("context_policy_id") != POLICY["context_policy_id"]
                    or generation.get("prompt_policy_id") != POLICY["prompt_policy_id"]
                    or generation.get("handoff_contract") != "beam-v1"
                    or protocol.get("prepare_policy_sha256") != prepare_digest(encoded(POLICY))):
                raise WorkerBlocked("shared_prepare_policy_not_frozen")
        handoff_contract = generation.get("handoff_contract", "champ-v1")
        if handoff_contract not in {"champ-v1", "beam-v1"}:
            raise WorkerBlocked("handoff_contract_not_frozen")
        if handoff_contract == "beam-v1":
            if (protocol.get("suite_packaging") != "beam-java-suite-v1"
                    or protocol.get("compatibility_policy") != "none"
                    or type(protocol.get("test_method_cap")) is not int
                    or protocol["test_method_cap"] != 30
                    or generation.get("suite_policy_id") != "beam-java-suite-v1"
                    or generation.get("suite_resolver") != "scripts.study.api854.suite_resolver:BeamSuiteResolver"
                    or any(protocol.get("model_policy", {}).get(a, {}).get("kku_model_id") != models[a]["id"] for a in models)):
                raise WorkerBlocked("beam_processing_contract_not_frozen")
        if generation.get("input_contract") != CONTRACT:
            raise WorkerBlocked("generation_contract_not_frozen")
        for key in ("max_tokens", "prompt_token_reserve"):
            if type(generation.get(key)) is not int or generation[key] < 1:
                raise WorkerBlocked("token_settings_not_frozen")
        temperature = generation.get("temperature")
        if type(temperature) not in (int, float) or not math.isfinite(temperature) or not 0 <= temperature <= 2:
            raise WorkerBlocked("temperature_not_frozen")
        for key in ("context_policy_id", "prompt_policy_id", "suite_policy_id", "suite_resolver"):
            if not isinstance(generation.get(key), str) or not generation[key].strip():
                raise WorkerBlocked("processing_policies_not_frozen")
        try:
            start = datetime.fromisoformat(protocol["start_at"])
            cutoff = datetime.fromisoformat(protocol["generation_cutoff_at"])
            if start.tzinfo is None or cutoff.tzinfo is None or start >= cutoff:
                raise ValueError("Invalid time window")
        except (KeyError, TypeError, ValueError):
            raise WorkerBlocked("generation_window_not_frozen") from None
        return cls(digest(data), models, temperature, **{key: generation[key] for key in (
            "max_tokens", "prompt_token_reserve", "context_policy_id", "prompt_policy_id", "suite_policy_id", "suite_resolver")},
            start_at=start, generation_cutoff_at=cutoff, handoff_contract=handoff_contract, protocol=protocol,
            prepare_contract=generation.get("prepare_contract", "champ-v1"))

    @property
    def reserve_tokens(self):
        return self.prompt_token_reserve + self.max_tokens


def load_suite_resolver(spec: str):
    # A repository module supplied by Beam and named by the frozen protocol.
    if not re.fullmatch(r"scripts\.study\.[A-Za-z0-9_.]+:[A-Za-z_][A-Za-z0-9_]*", spec):
        raise WorkerBlocked("suite_resolver_must_be_repository_callable")
    module, name = spec.split(":")
    try:
        resolver = getattr(importlib.import_module(module), name)
    except (ImportError, AttributeError):
        raise WorkerBlocked("beam_suite_resolver_unavailable") from None
    if not callable(resolver):
        raise WorkerBlocked("beam_suite_resolver_unavailable")
    return resolver


def resolve_prepared_job(client, job: dict, settings: FrozenSettings, *, owner="champ") -> GenerationJob:
    """Consume two hashed artifacts from the most recent prepared stage.

    Preparation owns the approved prompt/selection; this worker never appends
    errors, invents target selection, or alters source/assertions.
    """
    if job.get("protocol_hash") != settings.protocol_hash or job.get("owner") != owner or \
            job.get("stage") != "generate" or job.get("approach") not in settings.models:
        raise WorkerBlocked("job_identity_or_protocol_mismatch")
    history = job.get("payload", {}).get("stage_history", [])
    prepared = next((entry for entry in reversed(history) if entry.get("stage") == "prepare"
                     and entry.get("outcome") == "prepared"), None)
    if prepared is None:
        raise WorkerBlocked("prepared_context_missing")
    artifacts = prepared.get("artifacts", [])
    def download(name):
        matches = [artifact for artifact in artifacts if artifact.get("name") == name]
        if len(matches) != 1:
            raise WorkerBlocked("prepared_artifact_missing_or_ambiguous")
        artifact = matches[0]
        if type(artifact.get("size")) is not int or not 0 < artifact["size"] <= 20 * 1024 * 1024:
            raise WorkerBlocked("prepared_artifact_size_invalid")
        data = client.download(artifact)
        if len(data) != artifact["size"]:
            raise WorkerBlocked("prepared_artifact_size_mismatch")
        return data
    manifest = json.loads(download("context-manifest.json"))
    metadata = prepared.get("metadata") or {}
    prompt_bytes = download("prompt.md")
    prompt = prompt_bytes.decode("utf-8")
    files = manifest.get("source_files")
    if not isinstance(files, list) or not files or any(
        not isinstance(row, dict) or not re.fullmatch(r"[0-9a-f]{64}", str(row.get("sha256", "")))
        for row in files
    ):
        raise WorkerBlocked("invalid_fixed_source_manifest")
    source_hash = digest(json.dumps(files, sort_keys=True, separators=(",", ":")).encode())
    if manifest.get("project") != job["project"] or manifest.get("bug_id") != job["bug_id"] or \
            manifest.get("revision") != f"{job['bug_id']}f" or manifest.get("contains_execution_logs") is not False or \
            manifest.get("selection_policy_id") != settings.context_policy_id or manifest.get("source_hash") != source_hash or \
            metadata.get("source_sha256") != source_hash or metadata.get("prompt_sha256") != digest(prompt_bytes) or \
            metadata.get("prompt_policy_id") != settings.prompt_policy_id:
        raise WorkerBlocked("prepared_context_or_prompt_policy_mismatch")
    # Conservative byte-level floor. The frozen bound still covers provider
    # message wrappers/system overhead; this is not a claimed tokenizer count.
    if len(prompt_bytes) > settings.prompt_token_reserve:
        raise WorkerBlocked("prompt_exceeds_frozen_conservative_bound")
    if getattr(settings, "handoff_contract", "champ-v1") == "beam-v1":
        fixed_sources = {row["path"]: row["sha256"] for row in files if row.get("path", "").endswith(".java")}
        if (not fixed_sources or metadata.get("fixed_source_sha256") != fixed_sources
                or metadata.get("context_source_hash") != source_hash):
            raise WorkerBlocked("beam_preparation_lineage_mismatch")
    if getattr(settings, "prepare_contract", "champ-v1") == "aom-beam-prepare-v2":
        from .preparation import validate
        try:
            validate(manifest, metadata, prompt_bytes, download("targets.json"),
                     download("prepare-policy.json"), require_eligible=True)
        except ValueError:
            raise WorkerBlocked("shared_preparation_not_ready") from None
        if metadata["policy_sha256"] != settings.protocol.get("prepare_policy_sha256"):
            raise WorkerBlocked("shared_preparation_policy_not_frozen")
    return GenerationJob(job["run_id"], settings.protocol_hash, job["project"], job["bug_id"],
                         job["approach"], prompt, source_hash)


class APIWorker:
    def __init__(self, queue, kku, ledger, settings, *, approach, bucket, window,
                 artifact_root, private_root, worker_id, suite_resolver, model, condition="primary-kku-api", owner="champ"):
        if owner not in {"aom", "beam", "champ"} or (owner != "champ" and not getattr(queue, "team_routing", False)):
            raise WorkerBlocked("owner_requires_explicit_runner_plan")
        if owner != "champ" and settings.handoff_contract != "beam-v1":
            raise WorkerBlocked("all_owner_routing_requires_beam_handoff")
        if not callable(suite_resolver):
            raise WorkerBlocked("beam_suite_resolver_unavailable")
        if condition not in {"primary-kku-api", "mock-integration"} or (
            condition == "mock-integration" and not queue.mock_mode
        ):
            raise WorkerBlocked("mock_evidence_cannot_use_live_queue")
        resolve_selected([model], approach, settings.models)
        self.queue, self.kku, self.ledger, self.settings = queue, kku, ledger, settings
        self.approach, self.bucket, self.window = approach, bucket, window
        self.owner = owner
        self.artifact_root, self.private_root = Path(artifact_root), Path(private_root)
        self.worker_id, self.suite_resolver, self.model = worker_id, suite_resolver, model
        self.condition = condition

    def check(self) -> dict:
        self.queue.check()
        remaining = self.ledger.available(self.kku.account.alias, self.bucket, self.window)
        if remaining < self.settings.reserve_tokens:
            raise QuotaBlocked("Insufficient observed quota; notify user before switching")
        status = self.queue.request("GET", "/v1/status")
        eligible = [job for job in status.get("jobs", []) if job.get("state") == "queued" and
                    job.get("stage") == "generate" and job.get("owner") == self.owner and
                    job.get("approach") == self.approach]
        # Controller 1.0 cannot filter claims by run/protocol; reject mixed
        # protocols before claim, and revalidate the actual returned claim.
        for job in eligible:
            if job.get("protocol_hash") != self.settings.protocol_hash:
                raise WorkerBlocked("mixed_generation_protocols_in_queue")
        if eligible:
            prepared = resolve_prepared_job(self.queue, eligible[0], self.settings, owner=self.owner)
            if sanitize(prepared.prompt, (self.kku.account.api_key, self.queue.token)) != prepared.prompt:
                raise WorkerBlocked("credentials_in_prepared_prompt")
            self.ledger.available(self.kku.account.alias, self.bucket, self.window, job_key=prepared.key)
        return {"state": "ready" if eligible else "idle", "eligible_jobs": len(eligible),
                "protocol_hash": self.settings.protocol_hash, "account_alias": self.kku.account.alias,
                "model": self.model.id, "reserve_tokens": self.settings.reserve_tokens,
                "remaining": remaining, "queue_mutations": 0, "kku_generation_requests": 0}

    def once(self) -> dict:
        now = datetime.now(timezone.utc)
        if not self.settings.start_at <= now < self.settings.generation_cutoff_at:
            raise WorkerBlocked("outside_frozen_generation_window")
        ready = self.check()
        if ready["state"] == "idle":
            return ready
        claim = self.queue.claim(self.worker_id, "generate", approaches=[self.approach], owner=self.owner)
        if claim.get("job") is None:
            return {**ready, "state": "idle", "queue_mutations": 1}
        if not isinstance(claim.get("attempt_id"), str) or not re.fullmatch(
            r"[A-Za-z0-9][A-Za-z0-9_-]{0,119}", claim["attempt_id"]
        ):
            raise WorkerBlocked("unsafe_queue_attempt_id")
        # Save private claim before doing anything else. Tokens never enter
        # published evidence, and a crash cannot silently erase lease ownership.
        self.private_root.mkdir(parents=True, exist_ok=True)
        write_json(self.private_root / f"claim-{claim['attempt_id']}.json", claim)
        try:
            job = resolve_prepared_job(self.queue, claim["job"], self.settings, owner=self.owner)
            if sanitize(job.prompt, (self.kku.account.api_key, self.queue.token, claim["lease_token"])) != job.prompt:
                raise WorkerBlocked("credentials_in_prepared_prompt")
            guard = LeaseHeartbeat(self.queue, claim)
            if self.settings.handoff_contract == "beam-v1":
                from .ai_handoff import BeamGenerationHandoff
                from .queue_worker import local_job
                prepared = next(h for h in reversed(claim["job"]["payload"]["stage_history"])
                                if h.get("stage") == "prepare" and h.get("outcome") == "prepared")["metadata"]
                handoff = BeamGenerationHandoff(self.queue, claim, heartbeat=guard, job=local_job(claim),
                    protocol=self.settings.protocol, fixed_source_sha256=prepared["fixed_source_sha256"],
                    context_source_hash=prepared["context_source_hash"], worker_id=self.worker_id,
                    credential_secrets=(self.kku.account.api_key,))
            else:
                handoff = QueueGenerationHandoff(self.queue, claim, worker_id=self.worker_id,
                    suite_resolver=self.suite_resolver, suite_policy_id=self.settings.suite_policy_id,
                    credential_secrets=(self.kku.account.api_key,), lease_guard=guard)
            worker = GenerationWorker(self.kku, self.ledger, artifact_root=self.artifact_root,
                                      model=self.model, bucket=self.bucket, window=self.window, handoff=handoff,
                                      condition=self.condition)
            result = generate_with_lease(worker, job, guard, prompt_token_reserve=self.settings.prompt_token_reserve,
                                        max_tokens=self.settings.max_tokens, temperature=self.settings.temperature, expected_owner=self.owner)
        except Exception:
            write_json(self.private_root / f"reconcile-{claim['attempt_id']}.json", {
                "state": "needs_operator_reconciliation", "job_id": claim["job"]["job_id"],
                "attempt_id": claim["attempt_id"], "created_at_utc": utc_now(),
                "instruction": "Inspect saved claim and send-intent/result before requeue; never resend automatically"})
            raise
        return {"state": result["queue_status"], "job_id": claim["job"]["job_id"],
                "attempt_id": claim["attempt_id"], "generation_outcome": result["generation_outcome"],
                "account_alias": result["account_alias"], "model": self.model.id,
                "artifact_path": result["artifact_path"], "kku_generation_requests": 1,
                "evaluation_status": "not_attempted"}


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    action = parser.add_mutually_exclusive_group(required=True)
    action.add_argument("--check", action="store_true")
    action.add_argument("--once", action="store_true")
    parser.add_argument("--protocol", type=Path, required=True)
    parser.add_argument("--approach", choices=("kku-claude", "kku-gemini"), required=True)
    parser.add_argument("--bucket", required=True, help="Verified quota bucket ID, not inferred from model name")
    parser.add_argument("--window", required=True)
    parser.add_argument("--account", default="a01")
    parser.add_argument("--secrets", type=Path, default=Path(".local/api854/accounts.json"))
    parser.add_argument("--access", type=Path, default=Path(".local/api854/champ-access.private.json"))
    parser.add_argument("--ledger", type=Path, default=Path(".local/api854/quota.sqlite"))
    parser.add_argument("--worker-id", default="champ-pc1")
    parser.add_argument("--runner-plan", type=Path)
    parser.add_argument("--owner", choices=("aom", "beam", "champ"), default="champ")
    parser.add_argument("--artifact-root", type=Path, default=Path("results/study"))
    args = parser.parse_args()
    try:
        settings = FrozenSettings.load(args.protocol)
        resolver = load_suite_resolver(settings.suite_resolver)
        if args.runner_plan:
            from .team_queue import TeamQueueClient
            queue = TeamQueueClient.from_plan(args.access, args.runner_plan, args.worker_id,
                owner=args.owner, stage="generate", approaches=[args.approach],
                protocol_path=args.protocol, condition="primary")
        else:
            queue = ChampQueueClient(QueueAccess.load(args.access))
        kku = KKUClient(load_account(args.account, args.secrets))
        models, _ = kku.list_models()
        model = resolve_selected(models, args.approach, settings.models)
        ledger = QuotaLedger(args.ledger)
        worker = APIWorker(queue, kku, ledger, settings, approach=args.approach, bucket=args.bucket,
                           window=args.window, artifact_root=args.artifact_root,
                           private_root=args.access.parent / "worker-state", worker_id=args.worker_id,
                           suite_resolver=resolver, model=model, owner=args.owner)
        result = worker.once() if args.once else worker.check()
    except Exception as error:
        # No arbitrary traceback/raw error/header can leak into CLI output.
        reason = str(error) if isinstance(error, (WorkerBlocked, QuotaBlocked)) else getattr(error, "kind", "configuration_or_input_error")
        print(json.dumps({"state": "blocked", "reason": reason,
                          "action": "inspect local claim/evidence if any; notify user before switching accounts",
                          "automatic_retry": False, "automatic_account_switch": False}))
        return 1
    print(json.dumps(result, ensure_ascii=True))
    return 0 if result["state"] in {"ready", "idle", "published"} else 2


if __name__ == "__main__":
    sys.exit(main())
