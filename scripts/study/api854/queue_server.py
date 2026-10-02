"""Authenticated single-controller queue and immutable artifact store (stdlib only).

SQLite and evidence stay on the controller's local disk. Workers use HTTP,
never a shared SQLite file. No KKU requests are made by this service.
"""
from __future__ import annotations

import argparse
import hashlib
import hmac
import json
import os
from pathlib import Path
import re
import secrets
import sqlite3
import threading
import time
import uuid
from http.server import BaseHTTPRequestHandler, ThreadingHTTPServer
from urllib.parse import urlsplit

VERSION = "1.0"
APPROACHES = ("cmaes", "fscs-art", "kku-claude", "kku-gemini")
STAGES = ("prepare", "generate", "evaluate")
SAFE_NAME = re.compile(r"[A-Za-z0-9][A-Za-z0-9_.-]{0,119}\Z")
OUTCOMES = {
    "prepare": {"prepared", "preflight_failed", "adapter_unsupported"},
    "generate": {"generated", "refused", "generation_failed", "needs_reconciliation"},
    "evaluate": {"complete", "compile_failed", "fixed_failed", "environment_failed",
                 "coverage_failed", "timeout", "invalid", "partial", "failed"},
}


class APIError(Exception):
    def __init__(self, status, code):
        self.status, self.code = status, code


def digest(data):
    return hashlib.sha256(data).hexdigest()


def safe_name(value):
    if not isinstance(value, str) or not SAFE_NAME.fullmatch(value):
        raise APIError(400, "invalid_name")
    return value


def schema():
    return {
        "schema_version": VERSION,
        "authentication": "Authorization: Bearer <private worker token>",
        "stages": list(STAGES),
        "scheduling_states": ["queued", "leased", "needs_reconciliation", "finished"],
        "observed_outcomes": {k: sorted(v) for k, v in OUTCOMES.items()},
        "endpoints": {
            "GET /health": "Public minimal readiness (no credentials or evidence)",
            "GET /v1/schema": "This contract", "GET /v1/status": "Counts and job outcomes",
            "POST /v1/jobs/claim": {"worker_id": "string", "stage": "prepare|generate|evaluate",
                "owner": "optional champ|beam|aom", "approaches": "optional array",
                "lease_seconds": "60..3600, default 900"},
            "POST /v1/jobs/{job_id}/renew": {"attempt_id": "string", "lease_token": "string",
                "lease_version": "integer", "lease_seconds": "60..3600"},
            "POST /v1/jobs/{job_id}/artifacts": {
                "body": "raw bytes (maximum 20 MiB)",
                "headers": ["X-Artifact-Name", "X-Attempt-ID", "X-Lease-Token", "X-Lease-Version"]},
            "GET /v1/artifacts/{artifact_id}": "Authenticated immutable bytes with SHA256/ETag",
            "POST /v1/jobs/{job_id}/complete": {"attempt_id": "string", "lease_token": "string",
                "lease_version": "integer", "outcome": "stage-specific observed outcome",
                "artifact_ids": "nonempty array uploaded by THIS attempt",
                "metadata": "JSON object; suggested common fields and required AI generation fields below"},
            "POST /v1/jobs/{job_id}/fail": "Same envelope as complete, a failure outcome only",
            "POST /v1/admin/reconcile/{job_id}": {
                "admin_only": True, "resolution": "resume|finish",
                "outcome": "required for finish", "reason": "required evidence/reconciliation reason"},
        },
        "claim_response": "job (or null), attempt_id, lease_token, lease_version, lease_expires_at_unix",
        "job_fields": ["job_id", "run_id", "protocol_hash", "project", "bug_id", "approach",
                       "owner", "stage", "state", "outcome", "payload", "lease_version"],
        "metadata_fields": ["started_at_utc", "ended_at_utc", "worker_id", "source_sha256",
            "suite_sha256", "raw_evaluator_status", "failure_reason", "executed_tests", "skipped_tests",
            "requested_model", "actual_model", "account_alias", "prompt_sha256", "response_id",
            "usage", "model_quota", "checks_reach_target", "line_covered", "line_total",
            "branch_covered", "branch_total", "fault_detected", "stage_results"],
        "generation_metadata": "For AI: requested_model, actual_model, account_alias, prompt_sha256, "
            "response_id, usage and model_quota. Unknown values are null, never invented.",
        "artifact_namespace": "run_id/protocol_hash/project/bug_id/approach/attempt_id/name",
        "rules": ["All mutations are fenced by the active attempt, lease token and version.",
            "prepare/prepared advances to generate; generate/generated advances to evaluate.",
            "Each stage keeps its immutable artifacts in payload.stage_history.",
            "An expired generation lease becomes needs_reconciliation; no automatic resend.",
            "Expired preparation/evaluation requeue with prior artifact references intact.",
            "finished outcomes need observed evidence, not an administrative quota/deadline label.",
            "Never upload keys, authorization headers, emails or unrelated browser screenshots.",
            "Zero skipped/failing reports alone do not establish meaningful test execution.",
            "Queue success does not certify suite validity; worker evidence establishes that."],
    }


class Store:
    def __init__(self, root, credentials):
        self.root = Path(root).resolve()
        self.root.mkdir(parents=True, exist_ok=True)
        self.credentials = credentials
        self.lock = threading.RLock()
        self.db = sqlite3.connect(self.root / "state.sqlite", check_same_thread=False)
        self.db.row_factory = sqlite3.Row
        self.db.executescript("""
          PRAGMA journal_mode=WAL;
          PRAGMA foreign_keys=ON;
          CREATE TABLE IF NOT EXISTS jobs (
            job_id TEXT PRIMARY KEY, run_id TEXT NOT NULL, protocol_hash TEXT NOT NULL,
            project TEXT NOT NULL, bug_id INTEGER NOT NULL, approach TEXT NOT NULL, owner TEXT NOT NULL,
            stage TEXT NOT NULL DEFAULT 'prepare', state TEXT NOT NULL DEFAULT 'queued', outcome TEXT,
            payload TEXT NOT NULL DEFAULT '{}', lease_version INTEGER NOT NULL DEFAULT 0,
            lease_hash TEXT, lease_until REAL, attempt_id TEXT,
            UNIQUE(run_id,protocol_hash,project,bug_id,approach));
          CREATE TABLE IF NOT EXISTS attempts (
            attempt_id TEXT PRIMARY KEY, job_id TEXT NOT NULL REFERENCES jobs(job_id),
            stage TEXT NOT NULL, worker_id TEXT NOT NULL, started REAL NOT NULL,
            ended REAL, outcome TEXT, metadata TEXT);
          CREATE TABLE IF NOT EXISTS artifacts (
            artifact_id TEXT PRIMARY KEY, job_id TEXT NOT NULL REFERENCES jobs(job_id),
            attempt_id TEXT NOT NULL REFERENCES attempts(attempt_id), name TEXT NOT NULL,
            path TEXT NOT NULL, sha256 TEXT NOT NULL, size INTEGER NOT NULL,
            UNIQUE(attempt_id,name));
          CREATE TABLE IF NOT EXISTS events (
            event_id INTEGER PRIMARY KEY, job_id TEXT, kind TEXT NOT NULL,
            created REAL NOT NULL, detail TEXT NOT NULL);
        """)

    def event(self, job_id, kind, detail):
        self.db.execute("INSERT INTO events(job_id,kind,created,detail) VALUES(?,?,?,?)",
                        (job_id, kind, time.time(), json.dumps(detail)))

    def seed(self, manifest, run_id, protocol_hash, pilot):
        safe_name(run_id)
        if not re.fullmatch(r"[a-f0-9]{64}", protocol_hash):
            raise APIError(400, "protocol_hash_must_be_sha256")
        bugs = manifest["bugs"]
        identities = [(row["project"], row["bug_id"]) for row in bugs]
        if len(identities) != len(set(identities)):
            raise APIError(400, "duplicate_inventory")
        if pilot:
            chosen = {}
            for row in bugs:
                chosen.setdefault(row["project"], row)
            extras = [next(row for row in bugs if row["project"] == project and
                           row["bug_id"] != chosen[project]["bug_id"])
                      for project in ("Closure", "JxPath", "JacksonDatabind")]
            bugs = list(chosen.values()) + extras
        with self.lock, self.db:
            for row in bugs:
                project = safe_name(row["project"])
                if row["owner"] not in ("champ", "beam", "aom") or type(row["bug_id"]) is not int or row["bug_id"] < 1:
                    raise APIError(400, "invalid_inventory_row")
                for approach in APPROACHES:
                    identity = f"{run_id}:{protocol_hash}:{project}:{row['bug_id']}:{approach}"
                    job_id = digest(identity.encode())[:32]
                    self.db.execute("INSERT OR IGNORE INTO jobs(job_id,run_id,protocol_hash,project,bug_id,approach,owner) VALUES(?,?,?,?,?,?,?)",
                        (job_id, run_id, protocol_hash, project, row["bug_id"], approach, row["owner"]))
            self.event(None, "seed", {"run_id": run_id, "protocol_hash": protocol_hash, "pilot": pilot})
        return len(bugs) * 4

    def public_job(self, row):
        return {key: json.loads(row[key]) if key == "payload" else row[key] for key in
                ("job_id", "run_id", "protocol_hash", "project", "bug_id", "approach", "owner",
                 "stage", "state", "outcome", "payload", "lease_version")}

    def expire(self):
        for row in self.db.execute("SELECT * FROM jobs WHERE state='leased' AND lease_until<?", (time.time(),)).fetchall():
            state = "needs_reconciliation" if row["stage"] == "generate" else "queued"
            self.db.execute("UPDATE jobs SET state=?,lease_hash=NULL,lease_until=NULL WHERE job_id=?", (state, row["job_id"]))
            self.db.execute("UPDATE attempts SET ended=?,outcome=? WHERE attempt_id=?",
                            (time.time(), "lease_expired", row["attempt_id"]))
            self.event(row["job_id"], "lease_expired", {"stage": row["stage"], "state": state})

    def lease_seconds(self, body):
        value = body.get("lease_seconds", 900)
        if type(value) is not int or not 60 <= value <= 3600:
            raise APIError(400, "lease_seconds_out_of_range")
        return value

    def claim(self, body, role):
        worker = safe_name(body.get("worker_id"))
        stage = body.get("stage")
        if stage not in STAGES:
            raise APIError(400, "invalid_stage")
        seconds = self.lease_seconds(body)
        clauses, args = ["state='queued'", "stage=?"], [stage]
        if body.get("owner"):
            if body["owner"] not in ("champ", "beam", "aom"):
                raise APIError(400, "invalid_owner")
            clauses.append("owner=?")
            args.append(body["owner"])
        approaches = body.get("approaches", list(APPROACHES))
        if not isinstance(approaches, list) or not approaches or any(a not in APPROACHES for a in approaches):
            raise APIError(400, "invalid_approaches")
        clauses.append("approach IN (" + ",".join("?" for _ in approaches) + ")")
        args.extend(approaches)
        with self.lock, self.db:
            self.expire()
            row = self.db.execute("SELECT * FROM jobs WHERE " + " AND ".join(clauses) + " ORDER BY project,bug_id,approach LIMIT 1", args).fetchone()
            if row is None:
                return {"job": None}
            attempt, token = uuid.uuid4().hex, secrets.token_urlsafe(32)
            until, version = time.time() + seconds, row["lease_version"] + 1
            self.db.execute("UPDATE jobs SET state='leased',attempt_id=?,lease_hash=?,lease_until=?,lease_version=? WHERE job_id=?",
                            (attempt, digest(token.encode()), until, version, row["job_id"]))
            self.db.execute("INSERT INTO attempts(attempt_id,job_id,stage,worker_id,started) VALUES(?,?,?,?,?)",
                            (attempt, row["job_id"], stage, worker, time.time()))
            self.event(row["job_id"], "claim", {"role": role, "worker_id": worker, "stage": stage, "attempt_id": attempt})
            row = self.db.execute("SELECT * FROM jobs WHERE job_id=?", (row["job_id"],)).fetchone()
            return {"job": self.public_job(row), "attempt_id": attempt, "lease_token": token,
                    "lease_version": version, "lease_expires_at_unix": until}

    def fenced(self, job_id, body):
        row = self.db.execute("SELECT * FROM jobs WHERE job_id=?", (job_id,)).fetchone()
        if row is None:
            raise APIError(404, "job_not_found")
        token = body.get("lease_token", "")
        if not isinstance(token, str) or row["state"] != "leased" or not row["lease_until"] or row["lease_until"] <= time.time() or row["attempt_id"] != body.get("attempt_id") or type(body.get("lease_version")) is not int or row["lease_version"] != body["lease_version"] or not hmac.compare_digest(row["lease_hash"] or "", digest(token.encode())):
            raise APIError(409, "stale_or_invalid_lease")
        return row

    def renew(self, job_id, body):
        seconds = self.lease_seconds(body)
        with self.lock, self.db:
            self.fenced(job_id, body)
            until = time.time() + seconds
            self.db.execute("UPDATE jobs SET lease_until=? WHERE job_id=?", (until, job_id))
            return {"lease_expires_at_unix": until}

    def upload(self, job_id, body, name, data):
        safe_name(name)
        # Reject accidental leakage of this controller's credentials.
        if any(token.encode() in data for token in self.credentials.values()):
            raise APIError(400, "controller_credential_in_artifact")
        sha = digest(data)
        with self.lock, self.db:
            row = self.fenced(job_id, body)
            existing = self.db.execute("SELECT * FROM artifacts WHERE attempt_id=? AND name=?", (body["attempt_id"], name)).fetchone()
            if existing:
                if existing["sha256"] != sha:
                    raise APIError(409, "immutable_artifact_conflict")
                return self.artifact_info(existing)
            relative = Path("evidence") / row["run_id"] / row["protocol_hash"] / row["project"] / str(row["bug_id"]) / row["approach"] / row["attempt_id"] / name
            destination = self.root / relative
            destination.parent.mkdir(parents=True, exist_ok=True)
            try:
                with destination.open("xb") as stream:
                    stream.write(data)
            except FileExistsError:
                if digest(destination.read_bytes()) != sha:
                    raise APIError(409, "immutable_artifact_conflict")
            artifact_id = uuid.uuid4().hex
            self.db.execute("INSERT INTO artifacts VALUES(?,?,?,?,?,?,?)",
                            (artifact_id, job_id, row["attempt_id"], name, relative.as_posix(), sha, len(data)))
            return self.artifact_info(self.db.execute("SELECT * FROM artifacts WHERE artifact_id=?", (artifact_id,)).fetchone())

    def artifact_info(self, row):
        return {"artifact_id": row["artifact_id"], "uri": "/v1/artifacts/" + row["artifact_id"],
                "name": row["name"], "sha256": row["sha256"], "size": row["size"]}

    def complete(self, job_id, body, failure_only=False):
        with self.lock, self.db:
            row = self.fenced(job_id, body)
            outcome = body.get("outcome")
            if outcome not in OUTCOMES[row["stage"]] or (failure_only and outcome in ("prepared", "generated", "complete")):
                raise APIError(400, "invalid_stage_outcome")
            ids, metadata = body.get("artifact_ids"), body.get("metadata")
            if not isinstance(ids, list) or not ids or not all(isinstance(x, str) for x in ids) or not isinstance(metadata, dict):
                raise APIError(400, "evidence_and_metadata_required")
            artifacts = []
            for artifact_id in ids:
                artifact = self.db.execute("SELECT * FROM artifacts WHERE artifact_id=? AND job_id=? AND attempt_id=?", (artifact_id, job_id, row["attempt_id"])).fetchone()
                if artifact is None:
                    raise APIError(400, "artifact_not_owned_by_attempt")
                artifacts.append(self.artifact_info(artifact))
            if outcome == "generated" and not re.fullmatch(r"[a-f0-9]{64}", metadata.get("suite_sha256", "")):
                raise APIError(400, "suite_sha256_required")
            if outcome == "generated" and metadata["suite_sha256"] not in {a["sha256"] for a in artifacts}:
                raise APIError(400, "suite_hash_must_match_uploaded_artifact")
            if any(token in json.dumps(metadata) for token in self.credentials.values()):
                raise APIError(400, "controller_credential_in_metadata")
            if row["approach"].startswith("kku-") and row["stage"] == "generate":
                fields = ("requested_model", "actual_model", "account_alias", "prompt_sha256", "response_id", "usage", "model_quota")
                if any(field not in metadata for field in fields):
                    raise APIError(400, "ai_metadata_fields_required")
            payload = json.loads(row["payload"])
            payload.setdefault("stage_history", []).append({"stage": row["stage"], "attempt_id": row["attempt_id"], "outcome": outcome, "metadata": metadata, "artifacts": artifacts})
            stage, state = row["stage"], "finished"
            if outcome in ("prepared", "generated"):
                stage, state = ("generate" if outcome == "prepared" else "evaluate"), "queued"
            elif outcome == "needs_reconciliation":
                state = "needs_reconciliation"
            self.db.execute("UPDATE attempts SET ended=?,outcome=?,metadata=? WHERE attempt_id=?", (time.time(), outcome, json.dumps(metadata), row["attempt_id"]))
            self.db.execute("UPDATE jobs SET stage=?,state=?,outcome=?,payload=?,lease_hash=NULL,lease_until=NULL WHERE job_id=?",
                            (stage, state, outcome, json.dumps(payload), job_id))
            self.event(job_id, "stage_completed", {"outcome": outcome, "stage": row["stage"]})
            return self.public_job(self.db.execute("SELECT * FROM jobs WHERE job_id=?", (job_id,)).fetchone())

    def reconcile(self, job_id, body):
        if any(token in json.dumps(body) for token in self.credentials.values()):
            raise APIError(400, "controller_credential_in_metadata")
        with self.lock, self.db:
            row = self.db.execute("SELECT * FROM jobs WHERE job_id=?", (job_id,)).fetchone()
            if row is None or row["state"] != "needs_reconciliation" or not isinstance(body.get("reason"), str) or not body["reason"].strip():
                raise APIError(409, "reconciliation_state_and_reason_required")
            resolution = body.get("resolution")
            if resolution == "resume":
                self.db.execute("UPDATE jobs SET state='queued',outcome=NULL WHERE job_id=?", (job_id,))
            elif resolution == "finish" and body.get("outcome") == "generated":
                # Recover an observed suite without making another paid AI request.
                token = secrets.token_urlsafe(32)
                self.db.execute("UPDATE jobs SET state='leased',lease_hash=?,lease_until=? WHERE job_id=?",
                                (digest(token.encode()), time.time() + 60, job_id))
                result = self.complete(job_id, {**body, "attempt_id": row["attempt_id"],
                    "lease_token": token, "lease_version": row["lease_version"]})
                self.event(job_id, "reconciliation", {"resolution": resolution, "reason": body["reason"]})
                return result
            elif resolution == "finish" and body.get("outcome") in ("generation_failed", "refused"):
                # Only allow known failures with an explicit referenced observed artifact.
                artifact_id = body.get("artifact_id")
                if self.db.execute("SELECT 1 FROM artifacts WHERE artifact_id=? AND job_id=? AND attempt_id=?", (artifact_id, job_id, row["attempt_id"])).fetchone() is None:
                    raise APIError(400, "reconciliation_evidence_required")
                self.db.execute("UPDATE jobs SET state='finished',outcome=? WHERE job_id=?", (body["outcome"], job_id))
            else:
                raise APIError(400, "invalid_reconciliation_resolution")
            self.event(job_id, "reconciliation", body)
            return self.public_job(self.db.execute("SELECT * FROM jobs WHERE job_id=?", (job_id,)).fetchone())

    def status(self):
        with self.lock, self.db:
            self.expire()
            return {"schema_version": VERSION,
                    "counts": [dict(row) for row in self.db.execute("SELECT stage,state,outcome,count(*) AS count FROM jobs GROUP BY stage,state,outcome")],
                    "attempts": [{**dict(row), "artifacts": [self.artifact_info(a) for a in self.db.execute(
                        "SELECT * FROM artifacts WHERE attempt_id=?", (row["attempt_id"],))]}
                        for row in self.db.execute("SELECT * FROM attempts")],
                    "jobs": [self.public_job(row) for row in self.db.execute("SELECT * FROM jobs ORDER BY project,bug_id,approach")]}


class Handler(BaseHTTPRequestHandler):
    server_version = "SQAQueue/1.0"
    protocol_version = "HTTP/1.1"

    def log_message(self, format, *args):
        # Never log request bodies, headers, worker tokens or lease tokens.
        pass

    def reply(self, status, value, raw=False, headers=None):
        data = value if raw else json.dumps(value, ensure_ascii=False).encode()
        self.send_response(status)
        self.send_header("Content-Type", "application/octet-stream" if raw else "application/json; charset=utf-8")
        self.send_header("Content-Length", str(len(data)))
        self.send_header("Cache-Control", "no-store")
        self.send_header("Connection", "close")
        for key, content in (headers or {}).items():
            self.send_header(key, content)
        self.end_headers()
        self.wfile.write(data)
        self.close_connection = True

    def role(self):
        supplied = self.headers.get("Authorization", "")
        for role, token in self.server.store.credentials.items():
            if hmac.compare_digest(supplied, "Bearer " + token):
                return role
        raise APIError(401, "invalid_worker_token")

    def read_body(self, limit):
        if self.headers.get("Transfer-Encoding"):
            raise APIError(400, "content_length_required")
        try:
            length = int(self.headers.get("Content-Length", "0"))
        except ValueError:
            raise APIError(400, "invalid_content_length")
        if not 0 <= length <= limit:
            raise APIError(413, "request_too_large")
        data = self.rfile.read(length)
        if len(data) != length:
            raise APIError(400, "incomplete_body")
        return data

    def do_GET(self):
        self.dispatch()

    def do_POST(self):
        self.dispatch()

    def dispatch(self):
        self.connection.settimeout(30)
        try:
            path = urlsplit(self.path).path
            if self.command == "GET" and path == "/health":
                return self.reply(200, {"service": "sqa-api854-queue", "schema_version": VERSION, "ready": True})
            role = self.role()
            store = self.server.store
            if self.command == "GET":
                if path == "/v1/schema":
                    return self.reply(200, schema())
                if path == "/v1/status":
                    return self.reply(200, store.status())
                match = re.fullmatch(r"/v1/artifacts/([a-f0-9]{32})", path)
                if match:
                    with store.lock:
                        row = store.db.execute("SELECT * FROM artifacts WHERE artifact_id=?", (match[1],)).fetchone()
                        if row is None:
                            raise APIError(404, "artifact_not_found")
                        data = (store.root / row["path"]).read_bytes()
                        if digest(data) != row["sha256"]:
                            raise APIError(500, "artifact_integrity_error")
                    return self.reply(200, data, True, {"X-Content-SHA256": row["sha256"], "ETag": '"' + row["sha256"] + '"'})
                raise APIError(404, "endpoint_not_found")
            upload = re.fullmatch(r"/v1/jobs/([a-f0-9]{32})/artifacts", path)
            if upload:
                try:
                    version = int(self.headers.get("X-Lease-Version", "-1"))
                except ValueError:
                    raise APIError(400, "invalid_lease_version")
                envelope = {"attempt_id": self.headers.get("X-Attempt-ID"), "lease_token": self.headers.get("X-Lease-Token"), "lease_version": version}
                return self.reply(200, store.upload(upload[1], envelope, self.headers.get("X-Artifact-Name"), self.read_body(20 * 1024 * 1024)))
            body = json.loads(self.read_body(1024 * 1024))
            if not isinstance(body, dict):
                raise APIError(400, "json_object_required")
            if path == "/v1/jobs/claim":
                return self.reply(200, store.claim(body, role))
            match = re.fullmatch(r"/v1/jobs/([a-f0-9]{32})/(renew|complete|fail)", path)
            if match:
                result = store.renew(match[1], body) if match[2] == "renew" else store.complete(match[1], body, match[2] == "fail")
                return self.reply(200, result)
            match = re.fullmatch(r"/v1/admin/reconcile/([a-f0-9]{32})", path)
            if match:
                if role != "admin":
                    raise APIError(403, "admin_required")
                return self.reply(200, store.reconcile(match[1], body))
            raise APIError(404, "endpoint_not_found")
        except APIError as error:
            self.reply(error.status, {"error": error.code, "schema_version": VERSION})
        except (json.JSONDecodeError, UnicodeDecodeError, TypeError, ValueError):
            self.reply(400, {"error": "invalid_request"})
        except (BrokenPipeError, ConnectionResetError, TimeoutError):
            self.close_connection = True
        except Exception:
            # Private controller errors may be diagnosed locally, never exposed to peers.
            self.reply(500, {"error": "controller_error"})


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--root", type=Path, required=True)
    parser.add_argument("--host", default="127.0.0.1")
    parser.add_argument("--port", type=int, default=8765)
    parser.add_argument("--seed-manifest", type=Path)
    parser.add_argument("--protocol", type=Path)
    parser.add_argument("--run-id", default="api854-integration-v1")
    parser.add_argument("--seed-scope", choices=("pilot", "all"), default="pilot")
    parser.add_argument("--export-schema", type=Path)
    args = parser.parse_args()
    if args.export_schema:
        args.export_schema.parent.mkdir(parents=True, exist_ok=True)
        args.export_schema.write_text(json.dumps(schema(), indent=2, ensure_ascii=False) + "\n", encoding="utf-8")
        return
    args.root.mkdir(parents=True, exist_ok=True)
    credentials_path = args.root / "controller-credentials.json"
    if not credentials_path.exists():
        credentials_path.write_text(json.dumps({name: secrets.token_urlsafe(40) for name in ("admin", "champ", "beam", "aom")}, indent=2), encoding="utf-8")
    credentials = json.loads(credentials_path.read_text(encoding="utf-8"))
    if set(credentials) != {"admin", "champ", "beam", "aom"} or any(not isinstance(x, str) or len(x) < 32 for x in credentials.values()):
        raise SystemExit("Invalid local credentials file")
    for role in ("champ", "beam", "aom"):
        access_path = args.root / f"{role}-access.private.json"
        previous = json.loads(access_path.read_text(encoding="utf-8-sig")) if access_path.exists() else {}
        access_path.write_text(json.dumps({**previous, "role": role, "worker_token": credentials[role], "schema_version": VERSION}, indent=2), encoding="utf-8")
    store = Store(args.root, credentials)
    if args.seed_manifest:
        if not args.protocol:
            parser.error("--protocol required with --seed-manifest")
        protocol_hash = digest(args.protocol.read_bytes())
        count = store.seed(json.loads(args.seed_manifest.read_text(encoding="utf-8")), args.run_id, protocol_hash, args.seed_scope == "pilot")
        print(f"Seed scope contains {count} unique job keys; existing jobs retained", flush=True)
    server = ThreadingHTTPServer((args.host, args.port), Handler)
    server.daemon_threads = True
    server.store = store
    print(f"SQA queue listening on {args.host}:{args.port}; credentials remain in private local files", flush=True)
    try:
        server.serve_forever()
    except KeyboardInterrupt:
        pass
    finally:
        server.server_close()
        store.db.close()


if __name__ == "__main__":
    main()
