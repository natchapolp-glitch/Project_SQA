"""Single-host SQLite transactions behind a central authenticated HTTP service.

Worker API is intentionally separate from generation/evaluation implementations.
Never put this database on a network share or start independent databases per worker.
"""
from __future__ import annotations

from contextlib import contextmanager
import json
from pathlib import Path
import re
import sqlite3
import time
import uuid

from .inventory import APPROACHES, OWNERS, canonical_hash, file_hash

FAILURES = frozenset({"generation_failed", "refused", "truncated", "compile_failed",
                      "fixed_failed", "environment_failed", "invalid_suite", "timeout", "coverage_failed"})
TERMINAL = FAILURES | {"complete"}


def checked_artifacts(record):
    artifacts = record.get("artifacts")
    if not isinstance(artifacts, list) or not artifacts:
        raise ValueError("an observed outcome requires nonempty hashed artifacts")
    for artifact in artifacts:
        path = Path(artifact["path"])
        if not path.is_file() or path.stat().st_size == 0 or file_hash(path) != artifact["sha256"]:
            raise ValueError("artifact missing, empty, or hash mismatch on queue server")
    return artifacts


def no_credentials(value):
    if isinstance(value, dict):
        for key, item in value.items():
            if str(key).lower() in {"authorization", "api_key", "apikey", "access_token", "password", "email", "credentials"}:
                raise ValueError("credentials/identity are forbidden in records; use account alias")
            no_credentials(item)
    elif isinstance(value, list):
        for item in value:
            no_credentials(item)
    elif isinstance(value, str) and re.search(r"(?i)\bbearer\s+\S+|\bsk-[A-Za-z0-9_-]{16,}\b", value):
        raise ValueError("possible credential in metadata")


def checked_record(record, job, stage, protocol=None):
    checked_artifacts(record)
    if record.get("protocol_hash") != job["protocol_hash"]:
        raise ValueError("record belongs to another protocol")
    for field in ("source_hash", "suite_hash"):
        if not re.fullmatch(r"[a-f0-9]{64}", str(record.get(field, ""))):
            raise ValueError(f"{field} must be SHA-256")
    suite = record.get("suite_path")
    if not any(a["path"] == suite and a["sha256"] == record["suite_hash"] for a in record["artifacts"]):
        raise ValueError("suite must be a hashed artifact")
    if not any(a["sha256"] == record["source_hash"] for a in record["artifacts"]):
        raise ValueError("fixed source/context must be a hashed artifact")
    methods = record.get("test_methods")
    if type(methods) is not int or not 1 <= methods <= 30:
        raise ValueError("suite must contain 1..30 test methods")
    if job["approach"].startswith("kku-"):
        if not re.fullmatch(r"a\d{2}", str(record.get("account_alias", ""))):
            raise ValueError("KKU account alias required")
        settings = (protocol or {}).get("kku", {})
        expected = settings.get("exact_model_ids", {}).get(job["approach"])
        expected_actual = settings.get("exact_model_versions", {}).get(job["approach"], expected)
        if not expected or record.get("requested_model") != expected or record.get("actual_model") != expected_actual:
            raise ValueError("models differ from registered frozen protocol")
        for field in ("prompt_hash", "request_hash", "raw_response_hash"):
            if not re.fullmatch(r"[a-f0-9]{64}", str(record.get(field, ""))):
                raise ValueError(f"KKU {field} required")
            if not any(a["sha256"] == record[field] for a in record["artifacts"]):
                raise ValueError(f"KKU {field} must identify a hashed artifact")
        if not any(a["sha256"] == record["raw_response_hash"] for a in record["artifacts"]):
            raise ValueError("raw response must be a hashed artifact")
        if not record.get("response_id") or "usage" not in record or "model_quota" not in record:
            raise ValueError("KKU response provenance required (unknown usage may be null)")
    if stage == "evaluate":
        generated = json.loads(job["generation_record"])
        if record["suite_hash"] != generated["suite_hash"] or record["source_hash"] != generated["source_hash"]:
            raise ValueError("evaluate the identical generated suite and source")
        if record.get("fixed_passes") != [True, True]:
            raise ValueError("two successful fixed runs required")
        if record.get("compile_valid") is not True:
            raise ValueError("successful compilation required")
        hashes = {a["sha256"] for a in record["artifacts"]}
        roles = record.get("evidence_roles", {})
        if not all(roles.get(role) in hashes for role in ("fixed1", "fixed2", "buggy", "coverage", "validity")):
            raise ValueError("hashed fixed/buggy/coverage/semantic-validity evidence required")
        for field in ("meaningful_assertions", "target_executed", "buggy_measured", "coverage_measured"):
            if record.get(field) is not True:
                raise ValueError(f"{field} must be verified")
        if type(record.get("fault_detected")) is not bool:
            raise ValueError("fault detection must be observed; false is valid")
        for covered, total in (("line_covered", "line_total"), ("condition_covered", "condition_total")):
            c, t = record.get(covered), record.get(total)
            if type(c) is not int or type(t) is not int or not 0 <= c <= t:
                raise ValueError("coverage counts must be measured nonnegative integers")


def checked_failure(record, job, stage, failure, protocol=None):
    if record.get("protocol_hash") != job["protocol_hash"]:
        raise ValueError("failure belongs to another/missing protocol")
    checked_artifacts(record)
    if stage == "evaluate":
        generated = json.loads(job["generation_record"])
        if any(record.get(k) != generated[k] for k in ("suite_hash", "source_hash")):
            raise ValueError("evaluation failure must preserve generated lineage")
        if failure in {"generation_failed", "refused", "truncated"}:
            raise ValueError("generation outcome cannot close evaluation")
    elif failure in {"compile_failed", "fixed_failed", "coverage_failed"}:
        raise ValueError("evaluation outcome cannot close generation")
    if job["approach"].startswith("kku-"):
        expected = (protocol or {}).get("kku", {}).get("exact_model_ids", {}).get(job["approach"])
        expected_actual = (protocol or {}).get("kku", {}).get("exact_model_versions", {}).get(job["approach"], expected)
        if not expected or record.get("requested_model") != expected:
            raise ValueError("failure must identify frozen requested model")
        if record.get("actual_model") not in (None, expected_actual):
            raise ValueError("failure model is another condition")
        if not re.fullmatch(r"a\d{2}", str(record.get("account_alias", ""))):
            raise ValueError("failure account alias required")
        for field in ("actual_model", "usage", "model_quota", "raw_response_hash", "response_received"):
            if field not in record:
                raise ValueError("failure must explicitly record unknown provenance as null")
        for field in ("request_hash", "prompt_hash"):
            if not re.fullmatch(r"[a-f0-9]{64}", str(record.get(field, ""))):
                raise ValueError("request/prompt provenance required")
        hashes = {a["sha256"] for a in record["artifacts"]}
        if record["request_hash"] not in hashes:
            raise ValueError("redacted request must be evidence")
        if record["response_received"] is True:
            if record["raw_response_hash"] not in hashes:
                raise ValueError("received raw response must be evidence")
        elif record["response_received"] is not False or record["raw_response_hash"] is not None:
            raise ValueError("explicit response_received false/null hash required")


class Queue:
    def __init__(self, path, clock=time.time):
        self.path = Path(path)
        self.clock = clock
        self.path.parent.mkdir(parents=True, exist_ok=True)
        with self.transaction() as db:
            db.executescript("""
                CREATE TABLE IF NOT EXISTS jobs (
                    job_id TEXT PRIMARY KEY, project TEXT NOT NULL, bug_id INTEGER NOT NULL,
                    approach TEXT NOT NULL, protocol_hash TEXT NOT NULL, repeat_index INTEGER NOT NULL,
                    owner TEXT NOT NULL, active INTEGER NOT NULL DEFAULT 0,
                    stage TEXT NOT NULL DEFAULT 'generate', status TEXT NOT NULL DEFAULT 'not_attempted',
                    attempted INTEGER NOT NULL DEFAULT 0, worker_id TEXT, lease TEXT, lease_until REAL,
                    generation_record TEXT, evaluation_record TEXT, failure_record TEXT,
                    UNIQUE(project,bug_id,approach,protocol_hash,repeat_index)
                );
                CREATE TABLE IF NOT EXISTS protocols (
                    protocol_hash TEXT PRIMARY KEY, document TEXT NOT NULL
                );
                CREATE TABLE IF NOT EXISTS attempts (
                    attempt_id TEXT PRIMARY KEY, job_id TEXT NOT NULL REFERENCES jobs(job_id),
                    attempt_index INTEGER NOT NULL, stage TEXT NOT NULL,
                    status TEXT NOT NULL, started_at REAL NOT NULL, dispatched_at REAL, ended_at REAL,
                    metadata TEXT NOT NULL DEFAULT '{}', record TEXT,
                    UNIQUE(job_id,attempt_index)
                );
                CREATE TABLE IF NOT EXISTS events (
                    event_id INTEGER PRIMARY KEY, at REAL NOT NULL, job_id TEXT REFERENCES jobs(job_id),
                    kind TEXT NOT NULL, payload TEXT NOT NULL
                );
                CREATE TABLE IF NOT EXISTS reservations (
                    reservation_id TEXT PRIMARY KEY,
                    attempt_id TEXT NOT NULL REFERENCES attempts(attempt_id),
                    bucket TEXT NOT NULL, reset_window TEXT NOT NULL,
                    reserved_tokens INTEGER NOT NULL CHECK(reserved_tokens >= 0),
                    used_tokens INTEGER, status TEXT NOT NULL
                );
            """)

    @contextmanager
    def transaction(self):
        db = sqlite3.connect(self.path, timeout=30)
        db.row_factory = sqlite3.Row
        db.execute("PRAGMA foreign_keys=ON")
        try:
            db.execute("BEGIN IMMEDIATE")
            yield db
            db.commit()
        except BaseException:
            db.rollback()
            raise
        finally:
            db.close()

    def event(self, db, job_id, kind, payload):
        db.execute("INSERT INTO events(at,job_id,kind,payload) VALUES(?,?,?,?)",
                   (self.clock(), job_id, kind, json.dumps(payload, allow_nan=False)))

    def register_protocol(self, protocol):
        no_credentials(protocol)
        digest = canonical_hash(protocol)
        with self.transaction() as db:
            db.execute("INSERT OR IGNORE INTO protocols VALUES(?,?)", (digest, json.dumps(protocol, allow_nan=False)))
        return digest

    def seed(self, jobs, active=False):
        added = 0
        with self.transaction() as db:
            for job in jobs:
                identity = {k: job[k] for k in ("project", "bug_id", "approach", "protocol_hash", "repeat_index")}
                if (job["job_id"] != canonical_hash(identity) or job["approach"] not in APPROACHES
                        or job["owner"] not in OWNERS or job["repeat_index"] != 1):
                    raise ValueError("invalid job identity")
                existing = db.execute("SELECT * FROM jobs WHERE job_id=?", (job["job_id"],)).fetchone()
                if existing:
                    if any(existing[k] != job[k] for k in (*identity, "owner")):
                        raise ValueError("seed cannot silently reassign an existing job")
                    continue
                fields = ("job_id", *identity, "owner")
                db.execute(f"INSERT INTO jobs({','.join(fields)},active) VALUES({','.join('?' for _ in fields)},?)",
                           (*[job[k] for k in fields], int(active)))
                added += 1
            self.event(db, None, "seed", {"added": added, "active": active})
        return added

    def activate(self, job_ids, gate_evidence):
        # Only the coordinator invokes this after gate validation in service.py.
        with self.transaction() as db:
            for job_id in job_ids:
                if db.execute("UPDATE jobs SET active=1 WHERE job_id=?", (job_id,)).rowcount != 1:
                    raise ValueError("unknown job in activation")
            self.event(db, None, "activate", {"job_ids": job_ids, "gate_evidence": gate_evidence})

    def lease_row(self, db, job_id, lease, worker_id):
        row = db.execute("SELECT * FROM jobs WHERE job_id=?", (job_id,)).fetchone()
        if (not row or row["lease"] != lease or row["worker_id"] != worker_id
                or row["lease_until"] <= self.clock()):
            raise ValueError("lease expired, missing, or owned by another worker")
        return row

    def claim(self, stage, worker_id, lease_seconds=300, owner=None):
        if stage not in ("generate", "evaluate") or not worker_id or not 1 <= lease_seconds <= 3600:
            raise ValueError("invalid stage/worker/lease duration")
        if owner is not None and owner not in OWNERS:
            raise ValueError("invalid owner")
        with self.transaction() as db:
            row = db.execute("""SELECT * FROM jobs WHERE active=1 AND stage=?
                AND lease IS NULL AND status IN ('not_attempted','generated','queued')
                AND (? IS NULL OR owner=?) ORDER BY project,bug_id,approach LIMIT 1""",
                             (stage, owner, owner)).fetchone()
            if row is None:
                return None
            token = uuid.uuid4().hex
            until = self.clock() + lease_seconds
            db.execute("UPDATE jobs SET status='claimed',worker_id=?,lease=?,lease_until=? WHERE job_id=?",
                       (worker_id, token, until, row["job_id"]))
            self.event(db, row["job_id"], "claim", {"worker_id": worker_id, "stage": stage})
            return {**dict(row), "status": "claimed", "worker_id": worker_id, "lease": token, "lease_until": until}

    def renew(self, job_id, lease, worker_id, lease_seconds=300):
        if not 1 <= lease_seconds <= 3600:
            raise ValueError("invalid lease duration")
        with self.transaction() as db:
            self.lease_row(db, job_id, lease, worker_id)
            until = self.clock() + lease_seconds
            db.execute("UPDATE jobs SET lease_until=? WHERE job_id=?", (until, job_id))
            return until

    def start(self, job_id, lease, worker_id, metadata=None):
        no_credentials(metadata or {})
        with self.transaction() as db:
            row = self.lease_row(db, job_id, lease, worker_id)
            if row["status"] != "claimed":
                raise ValueError("start only once per claim")
            index = db.execute("SELECT COALESCE(MAX(attempt_index),0)+1 FROM attempts WHERE job_id=?", (job_id,)).fetchone()[0]
            attempt = uuid.uuid4().hex
            db.execute("INSERT INTO attempts(attempt_id,job_id,attempt_index,stage,status,started_at,metadata) VALUES(?,?,?,?,?,?,?)",
                       (attempt, job_id, index, row["stage"], "prepared", self.clock(), json.dumps(metadata or {}, allow_nan=False)))
            db.execute("UPDATE jobs SET status=? WHERE job_id=?",
                       ("generating" if row["stage"] == "generate" else "evaluating", job_id))
            self.event(db, job_id, "attempt_started", {"attempt_id": attempt, "index": index})
            return attempt

    def attempt_row(self, db, job, attempt_id):
        row = db.execute("SELECT * FROM attempts WHERE attempt_id=? AND job_id=?", (attempt_id, job["job_id"])).fetchone()
        if not row or row["stage"] != job["stage"] or row["status"] not in ("prepared", "dispatched"):
            raise ValueError("attempt does not belong to current stage or is already closed")
        return row

    def dispatched(self, job_id, lease, worker_id, attempt_id):
        # Commit immediately before external execution. Crash after this means uncertain outcome.
        with self.transaction() as db:
            job = self.lease_row(db, job_id, lease, worker_id)
            attempt = self.attempt_row(db, job, attempt_id)
            if attempt["status"] != "prepared":
                raise ValueError("already dispatched")
            db.execute("UPDATE attempts SET status='dispatched',dispatched_at=? WHERE attempt_id=?", (self.clock(), attempt_id))
            db.execute("UPDATE jobs SET attempted=1 WHERE job_id=?", (job_id,))
            self.event(db, job_id, "dispatch_intent", {"attempt_id": attempt_id})

    def finish(self, job_id, lease, worker_id, attempt_id, record, failure=None):
        no_credentials(record)
        with self.transaction() as db:
            job = self.lease_row(db, job_id, lease, worker_id)
            attempt = self.attempt_row(db, job, attempt_id)
            if attempt["status"] != "dispatched":
                raise ValueError("unexecuted work cannot be an observed outcome")
            if failure:
                if failure not in FAILURES or not record.get("reason"):
                    raise ValueError("typed failure with reason required")
                registered = db.execute("SELECT document FROM protocols WHERE protocol_hash=?", (job["protocol_hash"],)).fetchone()
                checked_failure(record, job, job["stage"], failure, json.loads(registered[0]) if registered else None)
                status, stage, field = failure, job["stage"], "failure_record"
            else:
                registered = db.execute("SELECT document FROM protocols WHERE protocol_hash=?", (job["protocol_hash"],)).fetchone()
                checked_record(record, job, job["stage"], json.loads(registered[0]) if registered else None)
                status = "generated" if job["stage"] == "generate" else "complete"
                stage = "evaluate"
                field = "generation_record" if job["stage"] == "generate" else "evaluation_record"
            encoded = json.dumps(record, allow_nan=False)
            db.execute(f"UPDATE jobs SET status=?,stage=?,{field}=?,lease=NULL,worker_id=NULL,lease_until=NULL WHERE job_id=?",
                       (status, stage, encoded, job_id))
            db.execute("UPDATE attempts SET status=?,ended_at=?,record=? WHERE attempt_id=?",
                       (status, self.clock(), encoded, attempt_id))
            self.event(db, job_id, status, {"attempt_id": attempt_id})

    def complete(self, job_id, lease, worker_id, attempt_id, record):
        self.finish(job_id, lease, worker_id, attempt_id, record)

    def fail(self, job_id, lease, worker_id, attempt_id, status, record):
        self.finish(job_id, lease, worker_id, attempt_id, record, status)

    def defer(self, job_id, lease, worker_id, reason):
        """Quota/auth/readiness blocker before dispatch, preserving not_attempted."""
        with self.transaction() as db:
            job = self.lease_row(db, job_id, lease, worker_id)
            attempts = db.execute("SELECT * FROM attempts WHERE job_id=? AND status IN ('prepared','dispatched')", (job_id,)).fetchall()
            if any(a["status"] == "dispatched" for a in attempts):
                raise ValueError("dispatched work must be reconciled, not deferred")
            db.execute("UPDATE attempts SET status='deferred',ended_at=? WHERE job_id=? AND status='prepared'", (self.clock(), job_id))
            status = "generated" if job["generation_record"] else ("queued" if job["attempted"] else "not_attempted")
            db.execute("UPDATE jobs SET active=0,status=?,lease=NULL,worker_id=NULL,lease_until=NULL WHERE job_id=?", (status, job_id))
            self.event(db, job_id, "deferred", {"reason": reason})

    def release_expired(self):
        with self.transaction() as db:
            rows = db.execute("SELECT * FROM jobs WHERE lease IS NOT NULL AND lease_until<=?", (self.clock(),)).fetchall()
            for row in rows:
                running = row["status"] in ("generating", "evaluating")
                if running:
                    # Both stages quarantined: old workers may still write artifacts. Reconcile first.
                    status = "needs_reconciliation"
                    db.execute("UPDATE attempts SET status='unknown',ended_at=? WHERE job_id=? AND status IN ('prepared','dispatched')",
                               (self.clock(), row["job_id"]))
                else:
                    status = "generated" if row["generation_record"] else ("queued" if row["attempted"] else "not_attempted")
                db.execute("UPDATE jobs SET status=?,lease=NULL,worker_id=NULL,lease_until=NULL WHERE job_id=?", (status, row["job_id"]))
                self.event(db, row["job_id"], "lease_expired", {"status": status})
            return len(rows)

    def requeue(self, job_id, reason, evidence, decision=None):
        """Operator confirms old worker stopped/outcome reconciled before a new attempt."""
        checked_artifacts({"artifacts": evidence})
        if not reason:
            raise ValueError("requeue reason required")
        with self.transaction() as db:
            row = db.execute("SELECT * FROM jobs WHERE job_id=?", (job_id,)).fetchone()
            if not row or row["lease"] is not None or row["status"] == "complete":
                raise ValueError("job cannot be requeued")
            if row["status"] not in {"needs_reconciliation", "environment_failed", "timeout", "coverage_failed"}:
                raise ValueError("semantic failures stay terminal; a revised condition requires a new protocol")
            if decision not in {"confirmed_not_executed", "confirmed_transport_failed", "confirmed_execution_stopped"}:
                raise ValueError("typed operator reconciliation required before retry")
            if row["stage"] == "generate" and decision != "confirmed_not_executed":
                raise ValueError("generation retry requires proof provider/generator never processed the attempt")
            attempt_count = db.execute("SELECT COUNT(*) FROM attempts WHERE job_id=? AND stage=?", (job_id, row["stage"])).fetchone()[0]
            limit = 3 if row["stage"] == "generate" else 2
            if attempt_count >= limit:
                raise ValueError("primary infrastructure retry limit reached")
            if row["status"] == "needs_reconciliation":
                resolution = "not_executed" if decision == "confirmed_not_executed" else "reconciled_failed"
                db.execute("UPDATE attempts SET status=? WHERE job_id=? AND status='unknown'", (resolution, job_id))
                if resolution == "not_executed":
                    executed = db.execute("SELECT COUNT(*) FROM attempts WHERE job_id=? AND dispatched_at IS NOT NULL AND status!='not_executed'", (job_id,)).fetchone()[0]
                    db.execute("UPDATE jobs SET attempted=? WHERE job_id=?", (int(executed > 0), job_id))
                    row = db.execute("SELECT * FROM jobs WHERE job_id=?", (job_id,)).fetchone()
            status = "generated" if row["generation_record"] else ("queued" if row["attempted"] else "not_attempted")
            db.execute("UPDATE jobs SET status=?,failure_record=NULL WHERE job_id=?", (status, job_id))
            self.event(db, job_id, "operator_requeue", {"reason": reason, "decision": decision, "evidence": evidence})

    def recover(self, job_id, attempt_id, status, record, reason, evidence):
        """Recover an existing observed output, never generate a substitute response."""
        no_credentials(record)
        checked_artifacts({"artifacts": evidence})
        if not reason:
            raise ValueError("operator recovery reason required")
        with self.transaction() as db:
            job = db.execute("SELECT * FROM jobs WHERE job_id=?", (job_id,)).fetchone()
            attempt = db.execute("SELECT * FROM attempts WHERE attempt_id=? AND job_id=?", (attempt_id, job_id)).fetchone()
            if (not job or not attempt or job["lease"] is not None or job["status"] != "needs_reconciliation"
                    or attempt["status"] != "unknown" or attempt["stage"] != job["stage"]):
                raise ValueError("recover only the interrupted attempt after old worker stopped")
            registered = db.execute("SELECT document FROM protocols WHERE protocol_hash=?", (job["protocol_hash"],)).fetchone()
            protocol = json.loads(registered[0]) if registered else None
            stage = job["stage"]
            if status in FAILURES:
                if not record.get("reason"):
                    raise ValueError("failure reason required")
                checked_failure(record, job, stage, status, protocol)
                field = "failure_record"
            else:
                wanted = "generated" if stage == "generate" else "complete"
                if status != wanted:
                    raise ValueError("recovered outcome does not match stage")
                checked_record(record, job, stage, protocol)
                field = "generation_record" if stage == "generate" else "evaluation_record"
                stage = "evaluate"
            encoded = json.dumps(record, allow_nan=False)
            db.execute(f"UPDATE jobs SET status=?,stage=?,{field}=?,attempted=1 WHERE job_id=?", (status, stage, encoded, job_id))
            db.execute("UPDATE attempts SET status=?,record=?,ended_at=? WHERE attempt_id=?", (status, encoded, self.clock(), attempt_id))
            self.event(db, job_id, "operator_recovered", {"attempt_id": attempt_id, "reason": reason, "evidence": evidence})

    def reassign(self, job_id, owner, reason):
        if owner not in OWNERS or not reason:
            raise ValueError("owner and reason required")
        with self.transaction() as db:
            row = db.execute("SELECT * FROM jobs WHERE job_id=?", (job_id,)).fetchone()
            if not row or row["lease"] is not None or row["status"] == "needs_reconciliation":
                raise ValueError("resolve active/uncertain lease before reassignment")
            db.execute("UPDATE jobs SET owner=? WHERE job_id=?", (owner, job_id))
            self.event(db, job_id, "reassignment", {"old_owner": row["owner"], "owner": owner, "reason": reason})

    def reservation(self, reservation_id, attempt_id, bucket, reset_window, reserved_tokens):
        # Champ owns limits and notifications; this provides FK-linked persistence only.
        if type(reserved_tokens) is not int or reserved_tokens < 0:
            raise ValueError("invalid reservation")
        with self.transaction() as db:
            db.execute("INSERT INTO reservations VALUES(?,?,?,?,?,?,?)",
                       (reservation_id, attempt_id, bucket, reset_window, reserved_tokens, None, "reserved"))

    def snapshot(self):
        with self.transaction() as db:
            return {"schema_version": 1, "snapshot_at": self.clock(),
                    **{table: [dict(r) for r in db.execute(f"SELECT * FROM {table}")]
                       for table in ("jobs", "attempts", "events", "reservations")}}
