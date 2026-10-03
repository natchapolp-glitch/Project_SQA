"""Beam queue transport and fenced evidence publication; no mutation retries."""
from __future__ import annotations

from dataclasses import dataclass, field
import hashlib
import io
from pathlib import Path
import re
import tarfile
from urllib.parse import urlsplit

from .common import read_json, sha256, write_json
from .champ_queue import ChampQueueClient, QueueError, MAX_ARTIFACT_BYTES
from .kku_client import http_transport
from .queue_client import QueueClient


@dataclass(frozen=True)
class BeamAccess:
    base_url: str
    worker_token: str = field(repr=False)
    role: str = "beam"
    schema_version: str = "1.0"

    def __post_init__(self):
        url = urlsplit(self.base_url)
        if (url.scheme != "https" or not url.hostname or url.username or url.password
                or url.path not in {"", "/"} or url.query or url.fragment or url.port not in {None, 443}):
            raise ValueError("Require the explicitly configured HTTPS queue origin")
        if self.role != "beam" or self.schema_version != "1.0" or not self.worker_token:
            raise ValueError("Require Beam access for queue schema 1.0")

    @classmethod
    def load(cls, path):
        data = read_json(path)
        return cls(**{key: data[key] for key in ("base_url", "worker_token", "role", "schema_version")})


class BeamQueueClient(ChampQueueClient):
    def check(self):
        result = super().check()
        result["role"] = "beam"
        return result

    def claim(self, worker_id, stage, approaches=None, owner="beam", lease_seconds=900):
        if owner != "beam" or stage not in {"prepare", "generate", "evaluate"}:
            raise ValueError("Beam CLI claims only its allocated work")
        if stage == "generate" and (not approaches or any(a not in {"cmaes", "fscs-art"} for a in approaches)):
            raise ValueError("AI generation needs the separate KKU/account/prompt preflight")
        return QueueClient.claim(self, worker_id, stage, approaches, owner, lease_seconds)

    @classmethod
    def for_loopback_test(cls, url, token):
        parsed = urlsplit(url)
        if (parsed.scheme != "http" or parsed.hostname != "127.0.0.1" or parsed.username
                or parsed.password or parsed.path or parsed.query or parsed.fragment):
            raise ValueError("Test client is restricted to an explicit loopback server")
        client = cls.__new__(cls)
        QueueClient.__init__(client, url, token)
        client.transport, client.mock_mode = http_transport, True
        return client


def assert_public(data, secrets):
    if any(secret.encode() in data for secret in secrets if secret):
        raise ValueError("Private credential in outgoing evidence")
    if re.search(rb"\bsk_[A-Za-z0-9_-]{12,}\b", data):
        raise ValueError("Possible KKU credential in outgoing evidence")


def bundle_evidence(source, destination, secrets):
    """Retain logs/context/code without relying on peer-local filesystem paths."""
    source, destination = Path(source), Path(destination)
    entries = []
    for path in sorted(source.rglob("*")):
        if path.is_symlink():
            raise ValueError("Evidence links are not allowed")
        if path.is_file():
            data = path.read_bytes()
            assert_public(data, secrets)
            entries.append((path.relative_to(source).as_posix(), data))
    destination.parent.mkdir(parents=True, exist_ok=True)
    with destination.open("xb") as stream, tarfile.open(fileobj=stream, mode="w:bz2") as archive:
        for name, data in entries:
            member = tarfile.TarInfo(name)
            member.size, member.mode, member.mtime = len(data), 0o644, 0
            archive.addfile(member, io.BytesIO(data))
    if destination.stat().st_size > MAX_ARTIFACT_BYTES:
        raise ValueError("Evidence bundle exceeds the queue artifact limit; preserve locally")
    return destination


class FencedPublisher:
    def __init__(self, client, claim, heartbeat, output, credential_secrets=()):
        self.client, self.claim, self.heartbeat = client, claim, heartbeat
        self.output = Path(output)
        self.output.mkdir(parents=True, exist_ok=True)
        self.secrets = (client.token, claim["lease_token"], *credential_secrets)

    def publish(self, outcome, files, metadata):
        names = [Path(path).name for path in files]
        if not files or len(names) != len(set(names)):
            raise ValueError("Require nonempty, uniquely named evidence artifacts")
        # Validate EVERY artifact before the first upload, including tar contents.
        for path in map(Path, files):
            if path.is_symlink() or path.stat().st_size > MAX_ARTIFACT_BYTES:
                raise ValueError("Unsafe or oversized publication artifact")
            assert_public(path.read_bytes(), self.secrets)
            if path.name.endswith(".tar.bz2"):
                with tarfile.open(path, "r:bz2") as archive:
                    for member in archive:
                        if not member.isfile():
                            raise ValueError("Publication archive must contain regular files only")
                        assert_public(archive.extractfile(member).read(), self.secrets)
        import json
        assert_public(json.dumps(metadata, allow_nan=False).encode(), self.secrets)
        if (self.output / "complete-intent.json").exists():
            raise QueueError("completion_needs_reconciliation", unknown=True)
        artifacts = []
        for index, path in enumerate(map(Path, files)):
            self.heartbeat.check()
            receipt_path = self.output / f"upload-{index:03d}.json"
            intent_path = self.output / f"upload-intent-{index:03d}.json"
            if receipt_path.exists():
                receipt = read_json(receipt_path)
            else:
                if intent_path.exists():
                    raise QueueError("upload_needs_reconciliation", unknown=True)
                write_json(intent_path, {"name": path.name, "sha256": sha256(path),
                                        "attempt_id": self.claim["attempt_id"]})
                receipt = self.client.upload_checked(self.claim, path)
                if (receipt.get("name") != path.name or receipt.get("sha256") != sha256(path)
                        or receipt.get("size") != path.stat().st_size):
                    raise QueueError("invalid_upload_receipt", unknown=True)
                write_json(receipt_path, receipt)
            if receipt.get("name") != path.name or receipt.get("sha256") != sha256(path):
                raise ValueError("Evidence changed after upload")
            artifacts.append(receipt)
        self.heartbeat.before_complete()
        self.heartbeat.check()
        write_json(self.output / "complete-intent.json",
                   {"job_id": self.claim["job"]["job_id"], "attempt_id": self.claim["attempt_id"],
                    "outcome": outcome, "artifact_ids": [a["artifact_id"] for a in artifacts], "metadata": metadata})
        failed = outcome in {"preflight_failed", "adapter_unsupported", "generation_failed", "refused",
                             "compile_failed", "fixed_failed", "environment_failed", "coverage_failed",
                             "timeout", "invalid", "partial", "failed"}
        receipt = self.client.complete(self.claim, outcome, artifacts, metadata, failed=failed)
        if (receipt.get("job_id") != self.claim["job"]["job_id"] or receipt.get("outcome") != outcome
                or receipt.get("state") not in {"queued", "finished", "needs_reconciliation"}):
            raise QueueError("invalid_completion_receipt", unknown=True)
        write_json(self.output / "complete-receipt.json", receipt)
        return receipt


def download_generation(client, claim, output):
    histories = claim["job"].get("payload", {}).get("stage_history", [])
    generations = [h for h in histories if h.get("stage") == "generate" and h.get("outcome") == "generated"]
    if not generations:
        raise ValueError("Evaluation needs a successful generation stage with immutable artifacts")
    generation = generations[-1]
    output = Path(output)
    output.mkdir(parents=True, exist_ok=False)
    found = {}
    for name in ("suite.tar.bz2", "generation-lineage.json"):
        artifacts = [a for a in generation.get("artifacts", []) if a.get("name") == name]
        if len(artifacts) != 1:
            raise ValueError("Generation must publish exactly one suite and one lineage artifact")
        artifact = artifacts[0]
        if not re.fullmatch(r"/v1/artifacts/[a-f0-9]{32}", artifact.get("uri", "")):
            raise ValueError("Artifact URI is outside the queue artifact endpoint")
        data = client.download(artifact)
        if hashlib.sha256(data).hexdigest() != artifact.get("sha256"):
            raise ValueError("Downloaded artifact hash differs")
        path = output / name
        with path.open("xb") as stream:
            stream.write(data)
        found[name] = path
    lineage = read_json(found["generation-lineage.json"])
    if (lineage.get("job", {}).get("attempt_id") != generation.get("attempt_id")
            or lineage.get("suite_sha256") != generation.get("metadata", {}).get("suite_sha256")):
        raise ValueError("Generation stage metadata and lineage disagree")
    prepared = [h for h in histories if h.get("stage") == "prepare" and h.get("outcome") == "prepared"]
    if not prepared or lineage.get("fixed_source_sha256") != prepared[-1]["metadata"].get("fixed_source_sha256"):
        raise ValueError("Generation lineage fixed sources differ from successful preparation")
    if (prepared[-1]["metadata"].get("prepare_contract") in {"aom-beam-prepare-v3", "aom-beam-prepare-v4", "aom-beam-prepare-v5", "aom-beam-prepare-v6-development", "aom-beam-prepare-v7-development"}
            and lineage.get("context_source_hash") != prepared[-1]["metadata"].get("context_source_hash")):
        raise ValueError("Generation receiver/build context differs from successful preparation")
    return found["suite.tar.bz2"], lineage
