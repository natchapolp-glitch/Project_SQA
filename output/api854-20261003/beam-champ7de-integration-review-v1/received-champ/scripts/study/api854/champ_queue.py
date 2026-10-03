"""Champ integration with Aom queue schema 1.0.

Reuses queue_client.py from aom commit 2e419e421c2fcc75dbfd516e04ed8ce27aafa463.
Live CLI is read-only. No scheduler, admin operations, or implicit API requests.
"""
from __future__ import annotations

import argparse
from dataclasses import dataclass, field
import json
from pathlib import Path
import re
import tarfile
import time
from typing import Callable
import urllib.parse

from .generate_worker import write_json
from .kku_client import digest, http_transport, sanitize, utc_now
from .queue_client import QueueClient

SCHEMA_VERSION = "1.0"
MAX_ARTIFACT_BYTES = 20 * 1024 * 1024


class QueueError(Exception):
    def __init__(self, kind: str, *, status: int | None = None, unknown: bool = False):
        super().__init__(f"Queue {kind}" + (f" (HTTP {status})" if status else ""))
        self.kind, self.status, self.unknown = kind, status, unknown


@dataclass(frozen=True)
class QueueAccess:
    base_url: str
    worker_token: str = field(repr=False)
    role: str = "champ"
    schema_version: str = SCHEMA_VERSION

    def __post_init__(self):
        url = urllib.parse.urlparse(self.base_url)
        if url.scheme != "https" or not url.hostname or url.username or url.password or \
                url.path not in {"", "/"} or url.query or url.fragment or url.port not in {None, 443}:
            raise ValueError("Require the explicitly configured HTTPS queue origin")
        if self.role != "champ" or self.schema_version != SCHEMA_VERSION or not self.worker_token:
            raise ValueError("Require Champ access with schema version 1.0")

    @classmethod
    def load(cls, path: Path) -> QueueAccess:
        data = json.loads(path.read_text(encoding="utf-8"))
        return cls(**{key: data[key] for key in ("base_url", "worker_token", "role", "schema_version")})


class ChampQueueClient(QueueClient):
    def __init__(self, access: QueueAccess, *, transport: Callable = http_transport,
                 timeout: float = 20, mock_mode: bool = False):
        if mock_mode and transport is http_transport:
            raise ValueError("Mock mode requires an offline transport")
        super().__init__(access.base_url, access.worker_token, timeout=timeout)
        self.transport, self.mock_mode = transport, mock_mode

    def request(self, method, path, body=None, headers=None, raw=False):
        # Don't accept absolute artifact URLs or redirect to a different origin.
        if not re.fullmatch(r"/(?:health|v1/(?:schema|status|jobs/claim|"
                            r"jobs/[A-Za-z0-9_-]+/(?:renew|artifacts|complete|fail)|"
                            r"artifacts/[A-Za-z0-9_-]+))", path):
            raise ValueError("Unsupported queue path")
        extra = headers or {}
        if any(key.lower() == "authorization" for key in extra):
            raise ValueError("Cannot override queue authorization")
        data = body if isinstance(body, bytes) else json.dumps(body, allow_nan=False).encode() if body is not None else None
        outgoing = {"Authorization": "Bearer " + self.token,
                    "Content-Type": "application/octet-stream" if isinstance(body, bytes) else "application/json",
                    **extra}
        try:
            response = self.transport(method, self.base_url + path, outgoing, data, self.timeout)
        except OSError:
            raise QueueError("transport_unknown", unknown=method != "GET") from None
        if not 200 <= response.status < 300:
            kind = "lease_conflict" if response.status == 409 else "auth_failed" if response.status in (401, 403) \
                else "redirect_blocked" if 300 <= response.status < 400 else "request_failed"
            raise QueueError(kind, status=response.status,
                             unknown=method != "GET" and response.status >= 500)
        if raw:
            return response.body
        try:
            result = json.loads(response.body)
        except (ValueError, UnicodeDecodeError):
            raise QueueError("invalid_response", unknown=method != "GET") from None
        if not isinstance(result, dict):
            raise QueueError("invalid_response", unknown=method != "GET")
        return result

    def check(self) -> dict:
        health = self.request("GET", "/health")
        schema = self.request("GET", "/v1/schema")
        if not health.get("ready") or health.get("schema_version") != SCHEMA_VERSION or \
                schema.get("schema_version") != SCHEMA_VERSION:
            raise QueueError("schema_or_readiness_mismatch")
        expected = {"generate": {"generated", "generation_failed", "refused", "needs_reconciliation"}}
        if not expected["generate"].issubset(set(schema.get("observed_outcomes", {}).get("generate", []))):
            raise QueueError("generation_contract_mismatch")
        return {"observed_at_utc": utc_now(), "health": health,
                "authenticated_schema_version": schema["schema_version"], "role": "champ",
                "queue_mutations": 0, "kku_generation_requests": 0}

    def claim(self, worker_id, stage, approaches=None, owner="champ", lease_seconds=900):
        if owner != "champ" or stage not in {"prepare", "generate"}:
            raise ValueError("Champ claims only its own preparation/generation work")
        if stage == "generate" and (not approaches or any(x not in {"kku-claude", "kku-gemini"} for x in approaches)):
            raise ValueError("Restrict Champ generation claims to primary KKU approaches")
        if not 60 <= lease_seconds <= 3600:
            raise ValueError("Lease seconds must be 60..3600")
        return super().claim(worker_id, stage, approaches, owner, lease_seconds)

    def upload_checked(self, claim: dict, path: Path) -> dict:
        data = path.read_bytes()
        if len(data) > MAX_ARTIFACT_BYTES:
            raise ValueError("Artifact exceeds the queue's 20 MiB limit")
        if self.token.encode() in data or claim["lease_token"].encode() in data:
            raise ValueError("Artifact contains private queue credentials")
        artifact = super().upload(claim, path)
        if artifact.get("sha256") != digest(data) or artifact.get("size") != len(data):
            raise QueueError("upload_hash_mismatch", unknown=True)
        if not isinstance(artifact.get("artifact_id"), str):
            raise QueueError("invalid_artifact_receipt", unknown=True)
        return artifact


class QueueGenerationHandoff:
    """Uploads a completed local attempt, then sends the fenced stage outcome.

    The suite resolver belongs to the frozen suite-processing/evaluator policy.
    It must return an immutable ready-to-evaluate archive, not new test logic.
    Without that resolver a raw Java response cannot advance to 'generated'.
    Local checkpoint records receipts so resuming publication never calls KKU.
    If the final mutation's response is lost, inspect status before resending.
    """
    def __init__(self, client: ChampQueueClient, claim: dict, *, worker_id: str,
                 suite_resolver: Callable[[dict], Path] | None = None,
                 suite_policy_id: str | None = None, credential_secrets: tuple[str, ...] = (),
                 lease_guard=None):
        self.client, self.claim, self.worker_id = client, claim, worker_id
        self.suite_resolver, self.suite_policy_id = suite_resolver, suite_policy_id
        if lease_guard is not None and lease_guard.claim is not claim:
            raise ValueError("Lease guard and handoff must share the same claim")
        self.lease_guard = lease_guard
        self.secrets = (client.token, claim["lease_token"], *credential_secrets)

    def _validate(self, result: dict):
        if self.lease_guard is not None:
            self.lease_guard.check()
        job = self.claim.get("job") or {}
        if job.get("owner") != "champ" or job.get("stage") != "generate":
            raise ValueError("Require an active Champ generation claim")
        for key in ("run_id", "project", "bug_id", "approach", "protocol_hash"):
            if result.get(key) != job.get(key):
                raise ValueError(f"Generation result doesn't match queue {key}")
        if result.get("attempt_id") != self.claim["attempt_id"]:
            raise ValueError("Pass the queue attempt_id into GenerationWorker.generate")
        if result.get("condition") != "primary-kku-api" and not self.client.mock_mode:
            raise ValueError("Mock evidence must never be uploaded to a live primary queue")
        if time.time() >= self.claim["lease_expires_at_unix"]:
            raise QueueError("lease_expired")

    def _validate_suite(self, path: Path):
        from scripts.study.evaluate import validate_archive
        validate_archive(path)
        # Check compressed contents as well as uploaded bytes for credentials.
        total = 0
        with tarfile.open(path, "r:bz2") as archive:
            for member in archive:
                if member.isfile():
                    total += member.size
                    if total > MAX_ARTIFACT_BYTES:
                        raise ValueError("Unpacked suite exceeds 20 MiB")
                    stream = archive.extractfile(member)
                    data = stream.read() if stream is not None else b""
                    if any(s.encode() in data for s in self.secrets if s) or \
                            re.search(rb"\bsk_[A-Za-z0-9_-]{12,}\b", data):
                        raise ValueError("Private credentials in suite archive")

    def publish_generation(self, result: dict) -> str:
        self._validate(result)
        raw_outcome = result["generation_outcome"]
        if raw_outcome == "response_received":
            if self.suite_resolver is None or not self.suite_policy_id:
                raise ValueError("Need Beam's frozen suite-processing adapter before generated handoff")
            suite = Path(self.suite_resolver(result))
            if suite.name != "suite.tar.bz2" or not suite.is_file():
                raise ValueError("Suite resolver must return suite.tar.bz2")
            self._validate_suite(suite)
            outcome = "generated"
        elif raw_outcome == "refused":
            outcome, suite = "refused", None
        elif raw_outcome in {"needs_reconciliation", "daily_limit", "auth_failed", "invalid_model",
                              "rate_limited", "redirect_blocked", "request_rejected"}:
            # Rejected before model processing isn't a successful experiment.
            outcome, suite = "needs_reconciliation", None
        else:
            outcome, suite = "generation_failed", None
        directory = Path(result["artifact_path"])
        allowed = {"request.json", "prompt.md", "attempt-start.json", "send-intent.json",
                   "response-evidence.json", "response.txt", "generation-result.json", "error.json"}
        files = sorted(p for p in directory.iterdir() if p.is_file() and
                       (p.name in allowed or re.fullmatch(r"source-block-[0-9]{3,}\.java", p.name)))
        if suite is not None:
            if any(p.name == suite.name for p in files):
                raise ValueError("Duplicate suite artifact name")
            files.append(suite)
        if not files:
            raise ValueError("Cannot publish a result without observed evidence")
        names = [p.name for p in files]
        if len(names) != len(set(names)):
            raise ValueError("Artifact filenames must be unique")
        # Validate every file before the first mutation; no partial secret upload.
        for path in files:
            data = path.read_bytes()
            if len(data) > MAX_ARTIFACT_BYTES or any(s.encode() in data for s in self.secrets if s):
                raise ValueError("Oversized artifact or private credential in evidence")
            if re.search(rb"\bsk_[A-Za-z0-9_-]{12,}\b", data):
                raise ValueError("KKU credential in evidence")
        error_body = result.get("error", {}).get("evidence", {}).get("body", {})
        if not isinstance(error_body, dict):
            error_body = {}
        metadata = {"started_at_utc": result.get("created_at_utc"), "ended_at_utc": result.get("ended_at_utc"),
                    "worker_id": self.worker_id, "source_sha256": result.get("source_hash"),
                    "requested_model": result.get("model_pin", {}).get("id"),
                    "requested_model_name": result.get("model_pin", {}).get("name"),
                    "actual_model": result.get("actual_model", error_body.get("model")),
                    "actual_provider": result.get("actual_provider", error_body.get("provider")),
                    "account_alias": result.get("account_alias"), "prompt_sha256": result.get("prompt_sha256"),
                    "response_id": result.get("response_id", error_body.get("id")),
                    "usage": result.get("usage"), "model_quota": result.get("model_quota"),
                    "raw_generation_outcome": raw_outcome, "condition": result.get("condition"),
                    "evaluation_status": "not_attempted", "usable_suite": None, "fault_detected": None,
                    "suite_processing_policy": self.suite_policy_id if suite is not None else None}
        if suite is not None:
            metadata["suite_sha256"] = digest(suite.read_bytes())
        if sanitize(metadata, self.secrets) != metadata:
            raise ValueError("Private credential in metadata")
        artifacts = []
        for index, path in enumerate(files):
            self._validate(result)
            receipt_path = directory / f"queue-upload-{index:03d}.json"
            if receipt_path.exists():
                receipt = json.loads(receipt_path.read_text(encoding="utf-8"))
                if receipt["name"] != path.name or receipt["sha256"] != digest(path.read_bytes()):
                    raise ValueError("Evidence changed after a prior queue upload")
            else:
                receipt = self.client.upload_checked(self.claim, path)
                write_json(receipt_path, receipt)
            artifacts.append(receipt)
        self._validate(result)
        intent = directory / "queue-complete-intent.json"
        if intent.exists():
            raise QueueError("completion_needs_reconciliation", unknown=True)
        if self.lease_guard is not None:
            self.lease_guard.before_complete()
            self._validate(result)
        write_json(intent, {"job_id": self.claim["job"]["job_id"], "attempt_id": self.claim["attempt_id"],
                            "outcome": outcome, "artifact_ids": [a["artifact_id"] for a in artifacts],
                            "metadata": metadata, "created_at_utc": utc_now()})
        completed = self.client.complete(self.claim, outcome, artifacts, metadata,
                                         failed=outcome in {"generation_failed", "refused"})
        if completed.get("job_id") != self.claim["job"]["job_id"] or completed.get("outcome") != outcome:
            raise QueueError("invalid_completion_receipt", unknown=True)
        write_json(directory / "queue-complete-receipt.json", completed)
        return f"queue:{completed['job_id']}:{self.claim['attempt_id']}:{outcome}"


def main() -> int:
    parser = argparse.ArgumentParser(description="Read-only Champ queue connectivity check")
    parser.add_argument("--access", type=Path, default=Path(".local/api854/champ-access.private.json"))
    parser.add_argument("--output", type=Path)
    args = parser.parse_args()
    client = ChampQueueClient(QueueAccess.load(args.access))
    try:
        result = client.check()
    except QueueError as error:
        print(json.dumps({"error_kind": error.kind, "http_status": error.status,
                          "queue_mutations": 0, "kku_generation_requests": 0}))
        return 1
    if args.output is not None:
        args.output.parent.mkdir(parents=True, exist_ok=True)
        write_json(args.output, result)
    print(json.dumps(result, ensure_ascii=True))
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
