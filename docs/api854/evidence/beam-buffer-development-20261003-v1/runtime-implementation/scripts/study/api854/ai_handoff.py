"""Connect Champ generation evidence to Beam's suite/lineage and Aom completion."""
from pathlib import Path

from .common import sha256
from .beam_queue import FencedPublisher, bundle_evidence
from .suite_resolver import BeamSuiteResolver


class BeamGenerationHandoff:
    def __init__(self, client, claim, *, heartbeat, job, protocol, fixed_source_sha256,
                 context_source_hash, worker_id, credential_secrets=()):
        self.client, self.claim, self.lease_guard = client, claim, heartbeat
        self.job, self.protocol, self.worker_id = job, protocol, worker_id
        self.fixed_sources, self.context_hash = fixed_source_sha256, context_source_hash
        self.secrets = credential_secrets
        for key in ("run_id", "protocol_hash", "project", "bug_id", "approach"):
            if claim["job"].get(key) != job[key]:
                raise ValueError("AI handoff job differs from the generation claim")
        if claim["attempt_id"] != job["attempt_id"] or heartbeat.claim is not claim:
            raise ValueError("AI handoff must share the exact claim/heartbeat attempt")
        prepared = [h for h in claim["job"].get("payload", {}).get("stage_history", [])
                    if h.get("stage") == "prepare" and h.get("outcome") == "prepared"]
        if (not prepared or prepared[-1]["metadata"].get("fixed_source_sha256") != fixed_source_sha256
                or prepared[-1]["metadata"].get("context_source_hash") != context_source_hash):
            raise ValueError("AI handoff requires the exact context from successful queue preparation")
        self.resolver = BeamSuiteResolver(job, protocol, fixed_source_sha256, context_source_hash)

    def publish_generation(self, result):
        self.lease_guard.check()
        if result.get("condition") == "mock-integration" and not self.client.mock_mode:
            raise ValueError("Mock AI evidence cannot be published to a live queue")
        if self.claim["job"].get("stage") != "generate":
            raise ValueError("Require a generation-stage lease")
        for key in ("run_id", "protocol_hash", "project", "bug_id", "approach", "repeat_index", "attempt_id"):
            if result.get(key) != self.job[key]:
                raise ValueError("AI result identity differs from claim")
        if result.get("source_hash") != self.context_hash:
            raise ValueError("AI fixed-context hash differs from preparation")
        expected = self.protocol["model_policy"][self.job["approach"]]["kku_model_id"]
        if result.get("model_pin", {}).get("id") != expected:
            raise ValueError("Requested model differs from the protocol; no fallback")
        directory = Path(result["artifact_path"])
        publisher = FencedPublisher(self.client, self.claim, self.lease_guard,
                                    directory.parent / (directory.name + "-publication"), self.secrets)
        raw = result["generation_outcome"]
        suite, failure = None, None
        if raw == "response_received":
            try:
                suite = self.resolver(result)
            except ValueError as error:
                failure = str(error)
                outcome = "generation_failed"
            else:
                outcome = "generated"
        elif raw == "refused":
            outcome = "refused"
        elif raw in {"needs_reconciliation", "daily_limit", "auth_failed", "invalid_model", "rate_limited",
                     "redirect_blocked", "request_rejected"}:
            outcome = "needs_reconciliation"
        else:
            outcome = "generation_failed"
        # Raw request/response and source blocks travel in the evidence bundle;
        # suite/lineage/manifest also have individual, downloadable artifact IDs.
        files = [directory / "generation-result.json"]
        if suite is not None:
            resolved = suite.parent.parent
            files += [suite, suite.parent / "suite-manifest.json", resolved / "generation-lineage.json",
                      resolved / "source-map.json"]
        files += [bundle_evidence(directory, publisher.output / "generation-evidence.tar.bz2", publisher.secrets)]
        metadata = {"worker_id": self.worker_id, "requested_model": result.get("model_pin", {}).get("id"),
                    "actual_model": result.get("actual_model"), "account_alias": result.get("account_alias"),
                    "prompt_sha256": result.get("prompt_sha256"), "response_id": result.get("response_id"),
                    "usage": result.get("usage"), "model_quota": result.get("model_quota"),
                    "source_sha256": result.get("source_hash"), "context_source_hash": self.context_hash,
                    "fixed_source_sha256": self.fixed_sources, "raw_generation_outcome": raw,
                    "suite_sha256": sha256(suite) if suite else None,
                    "suite_processing_policy": self.resolver.policy_id,
                    "failure_reason": failure, "semantic_validity": "pending_review", "usable": False,
                    "condition": result.get("condition"), "generation_result_sha256": sha256(files[0])}
        if suite is not None:
            from .common import read_json
            metadata["test_count"] = read_json(suite.parent / "suite-manifest.json")["test_count"]
        completed = publisher.publish(outcome, files, metadata)
        return f"queue:{completed['job_id']}:{self.claim['attempt_id']}:{outcome}"
