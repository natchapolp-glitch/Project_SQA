"""Standalone generation stage, ready for a future queue adapter.

Does not claim jobs, renew leases, or evaluate tests. The caller must implement
the eventual central queue contract. Local quota DB may not be shared by hosts.
"""
from __future__ import annotations

from dataclasses import asdict, dataclass
import json
from pathlib import Path
import re
from typing import Protocol
import uuid

from .kku_client import KKUClient, KKUError, Model, digest, normalize_quota, normalize_usage, sanitize, utc_now
from .quota import QuotaLedger


def write_json(path: Path, value: dict):
    """Immutable artifact. A crash leaves evidence rather than overwriting it."""
    with path.open("x", encoding="utf-8") as stream:
        json.dump(value, stream, ensure_ascii=False, indent=2, allow_nan=False)
        stream.write("\n")


@dataclass(frozen=True)
class GenerationJob:
    run_id: str
    protocol_hash: str
    project: str
    bug_id: int
    approach: str
    prompt: str
    source_hash: str
    repeat_index: int = 1

    def __post_init__(self):
        for text in (self.run_id, self.project):
            if not re.fullmatch(r"[A-Za-z0-9][A-Za-z0-9_-]{0,119}", text):
                raise ValueError("Unsafe run/project path")
        for value in (self.protocol_hash, self.source_hash):
            if not re.fullmatch(r"[0-9a-f]{64}", value):
                raise ValueError("Require frozen SHA-256 protocol and fixed-context hashes")
        if type(self.bug_id) is not int or self.bug_id < 1 or self.repeat_index != 1:
            raise ValueError("Primary study uses positive bug IDs and one repeat")
        if self.approach not in {"kku-claude", "kku-gemini"} or not self.prompt.strip():
            raise ValueError("Require a primary KKU approach and fixed-context prompt")

    @property
    def key(self) -> str:
        return json.dumps([self.project, self.bug_id, self.approach, self.protocol_hash,
                           self.repeat_index], separators=(",", ":"))


class HandoffAdapter(Protocol):
    """PROPOSAL ONLY. The actual queue schema/lease contract belongs to Aom.

    Publish immutable artifacts to a shared URI first, then advance the stage
    with lease token/version validation. A returned receipt must mean durable
    acceptance. This interface doesn't provide distributed exactly-once I/O.
    """
    def publish_generation(self, result: dict) -> str: ...


class GenerationWorker:
    def __init__(self, client: KKUClient, ledger: QuotaLedger, *, artifact_root: Path,
                 model: Model, bucket: str, window: str,
                 handoff: HandoffAdapter | None = None, condition: str = "primary-kku-api"):
        if condition not in {"primary-kku-api", "mock-integration"}:
            raise ValueError("Unknown evidence condition")
        self.client, self.ledger = client, ledger
        self.artifact_root, self.model = Path(artifact_root), model
        self.bucket, self.window, self.handoff, self.condition = bucket, window, handoff, condition

    def generate(self, job: GenerationJob, *, prompt_token_reserve: int,
                 max_tokens: int, temperature: float | None = None,
                 attempt_id: str | None = None) -> dict:
        if type(prompt_token_reserve) is not int or prompt_token_reserve < 1:
            raise ValueError("Reserve a conservative, protocol-defined prompt token bound")
        if type(max_tokens) is not int or max_tokens < 1:
            raise ValueError("Require positive output token budget")
        if temperature is not None and not 0 <= temperature <= 2:
            raise ValueError("Invalid temperature")
        from .kku_client import pin_model
        from .models import resolve_selected
        if self.condition == "primary-kku-api":
            resolve_selected([self.model], job.approach)
        else:
            family = "sonnet" if job.approach == "kku-claude" else "flash-lite"
            pin_model([self.model], family, self.model.name)
        safe_prompt = sanitize(job.prompt, (self.client.account.api_key,))
        if safe_prompt != job.prompt:
            raise ValueError("Prompt contains credentials; do not send or export it")
        attempt_id = attempt_id or "attempt-" + str(uuid.uuid4())
        if not re.fullmatch(r"[A-Za-z0-9][A-Za-z0-9_-]{0,119}", attempt_id):
            raise ValueError("Unsafe queue attempt ID")
        path = self.artifact_root / job.run_id / job.protocol_hash / job.project / \
            str(job.bug_id) / job.approach / attempt_id
        path.mkdir(parents=True, exist_ok=False)
        request = {"model": self.model.id, "messages": [{"role": "user", "content": job.prompt}],
                   "stream": False, "max_tokens": max_tokens}
        if temperature is not None:
            request["temperature"] = temperature
        write_json(path / "request.json", request)
        (path / "prompt.md").write_bytes(job.prompt.encode("utf-8"))
        reservation_id = self.ledger.reserve(self.client.account.alias, self.bucket, self.window,
                                            tokens=prompt_token_reserve + max_tokens, job_key=job.key)
        started = {"artifact_schema": "champ-generation-v1-proposal", "condition": self.condition,
                   "run_id": job.run_id, "job_key": job.key, "attempt_id": attempt_id,
                   "project": job.project, "bug_id": job.bug_id, "approach": job.approach,
                   "repeat_index": job.repeat_index, "account_alias": self.client.account.alias,
                   "protocol_hash": job.protocol_hash, "source_hash": job.source_hash,
                   "prompt_sha256": digest(job.prompt.encode()), "reservation_id": reservation_id,
                   "model_pin": asdict(self.model), "quota_bucket": self.bucket,
                   "quota_window": self.window, "created_at_utc": utc_now(),
                   "state": "prepared", "generation_sent": False}
        write_json(path / "attempt-start.json", started)
        # Persist the intent BEFORE send; a crash after this needs reconciliation.
        write_json(path / "send-intent.json", {"state": "needs_reconciliation_if_no_result",
                                              "sent_after_utc": utc_now(), "attempt_id": attempt_id})
        result = {**started, "generation_sent": True, "artifact_path": str(path),
                  "evaluation_status": "not_attempted", "usable_suite": None,
                  "fault_detected": None, "queue_status": "not_connected"}
        try:
            completion = self.client.complete(self.model, job.prompt, max_tokens=max_tokens,
                                              temperature=temperature)
        except KKUError as error:
            write_json(path / "response-evidence.json", error.evidence)
            write_json(path / "error.json", error.record())
            body = error.evidence.get("body", {})
            body = body if isinstance(body, dict) else {}
            usage, quota = normalize_usage(body), normalize_quota(body)
            outcome = "needs_reconciliation" if error.unknown else error.kind
            result.update(state=outcome, generation_outcome=outcome, error=error.record(),
                          usage=usage, model_quota=quota)
            self.ledger.finish(reservation_id, outcome=error.kind, unknown=error.unknown,
                               used=usage["total_tokens"], remaining=quota["daily_remaining_tokens"])
        else:
            write_json(path / "response-evidence.json", completion.evidence)
            raw_content = completion.content
            content = sanitize(raw_content, (self.client.account.api_key,))
            (path / "response.txt").write_bytes(content.encode("utf-8"))
            # Export complete fences in source order; evaluator owns filename,
            # method-cap and compile-validity policy. Do not invent Java tests.
            blocks = re.findall(r"^```java[^\S\n]*\r?\n(.*?)^```[^\S\n]*\r?$", content,
                                flags=re.MULTILINE | re.DOTALL)
            fence_lines = re.findall(r"^```.*$", content, flags=re.MULTILINE)
            if fence_lines and len(fence_lines) != 2 * len(blocks):
                blocks = []  # Never silently drop a partial or non-Java block.
            if not fence_lines and re.search(r"\bpublic\s+(?:(?:abstract|final|strictfp)\s+)*class\s+", content):
                blocks = [content]  # Preserve one bare Java file byte-for-byte.
            sources = []
            if completion.outcome == "response_received":
                for index, block in enumerate(blocks, 1):
                    filename = f"source-block-{index:03d}.java"
                    (path / filename).write_bytes(block.encode("utf-8"))
                    sources.append({"path": filename, "sha256": digest(block.encode())})
            outcome = completion.outcome
            if outcome == "response_received" and not sources:
                outcome = "generation_failed_no_java_fence"
            if content != raw_content:
                outcome = "generation_failed_sensitive_response"
                sources = []
            result.update(state=outcome, generation_outcome=outcome, **completion.metadata,
                          source_blocks=sources, response_content_sha256=digest(raw_content.encode()),
                          stored_content_sha256=digest(content.encode()),
                          sensitive_response_redacted=content != raw_content)
            self.ledger.finish(reservation_id, outcome=outcome,
                               used=completion.metadata["usage"]["total_tokens"],
                               remaining=completion.metadata["model_quota"]["daily_remaining_tokens"])
        result["ended_at_utc"] = utc_now()
        write_json(path / "generation-result.json", result)
        if self.handoff is not None:
            try:
                receipt = self.handoff.publish_generation(result)
                if not isinstance(receipt, str) or not receipt.strip():
                    raise ValueError("Queue adapter returned no durable receipt")
            except Exception:
                # Retry the publication, never regenerate. Preserve the result.
                write_json(path / "handoff.json", {"state": "publish_pending", "attempt_id": attempt_id})
                return {**result, "queue_status": "publish_pending"}
            write_json(path / "handoff.json", {"state": "published", "receipt": receipt})
            return {**result, "queue_status": "published", "queue_receipt": receipt}
        return result
