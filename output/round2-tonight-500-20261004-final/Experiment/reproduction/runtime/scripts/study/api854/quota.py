"""Durable, single-host token reservations. Not a multi-host queue or limiter.

No assumed midnight refill. A new window requires a fresh server observation.
Uncertain attempts keep reservations across restart. Switching requires a
notification receipt supplied by the operator after the user was notified.
"""
from __future__ import annotations

from contextlib import contextmanager
from datetime import datetime, timezone
import json
from pathlib import Path
import sqlite3
import uuid


class QuotaBlocked(Exception):
    pass


class QuotaLedger:
    def __init__(self, path: Path, *, global_limit: int = 2):
        if type(global_limit) is not int or global_limit < 1:
            raise ValueError("global_limit must be positive")
        self.path, self.global_limit = Path(path), global_limit
        self.path.parent.mkdir(parents=True, exist_ok=True)
        with self._connection() as db:
            db.executescript("""
                CREATE TABLE IF NOT EXISTS accounts (
                    alias TEXT PRIMARY KEY, state TEXT NOT NULL
                );
                CREATE TABLE IF NOT EXISTS buckets (
                    account TEXT NOT NULL, model TEXT NOT NULL, window TEXT NOT NULL,
                    remaining INTEGER NOT NULL, reset_at TEXT NOT NULL,
                    state TEXT NOT NULL DEFAULT 'active', evidence_ref TEXT NOT NULL,
                    PRIMARY KEY(account,model,window)
                );
                CREATE TABLE IF NOT EXISTS reservations (
                    id TEXT PRIMARY KEY, job_key TEXT NOT NULL,
                    account TEXT NOT NULL, model TEXT NOT NULL, window TEXT NOT NULL,
                    tokens INTEGER NOT NULL, state TEXT NOT NULL, used INTEGER,
                    outcome TEXT, created_at TEXT NOT NULL,
                    FOREIGN KEY(account,model,window) REFERENCES buckets(account,model,window)
                );
                CREATE INDEX IF NOT EXISTS reservation_bucket
                    ON reservations(account,model,window,state);
                CREATE TABLE IF NOT EXISTS switches (
                    id TEXT PRIMARY KEY, from_account TEXT NOT NULL,
                    to_account TEXT NOT NULL, model TEXT NOT NULL, reason TEXT NOT NULL,
                    state TEXT NOT NULL, notification_receipt TEXT
                );
                CREATE TABLE IF NOT EXISTS routes (
                    model TEXT PRIMARY KEY, account TEXT NOT NULL
                );
            """)

    @contextmanager
    def _connection(self):
        db = sqlite3.connect(self.path, timeout=15, isolation_level=None)
        db.row_factory = sqlite3.Row
        db.execute("PRAGMA foreign_keys=ON")
        try:
            yield db
        finally:
            db.close()

    @contextmanager
    def _transaction(self):
        with self._connection() as db:
            db.execute("BEGIN IMMEDIATE")
            try:
                yield db
                db.execute("COMMIT")
            except BaseException:
                db.execute("ROLLBACK")
                raise

    @staticmethod
    def _time(value: str | None = None) -> datetime:
        result = datetime.fromisoformat(value) if value else datetime.now(timezone.utc)
        if result.tzinfo is None:
            raise ValueError("Use timezone-aware ISO timestamps")
        return result.astimezone(timezone.utc)

    def observe(self, account: str, model: str, window: str, *, remaining: int,
                reset_at: str, evidence_ref: str, now: str | None = None):
        """Initialize/update from actual observation, not advertised daily quota.

        Model is a quota bucket identifier, possibly shared between aliases of
        a model family. Keys sharing account quota must use one account alias.
        Do not call this concurrently with an outstanding request; reconciliation
        has its own transaction. reset_at is a conservative observation expiry
        until the server reset timezone is verified.
        """
        if type(remaining) is not int or remaining < 0 or not evidence_ref or not window:
            raise ValueError("Require observed remaining quota, window, and evidence reference")
        reset = self._time(reset_at)
        if reset <= self._time(now):
            raise ValueError("Observation reset/expiry must be in the future")
        with self._transaction() as db:
            account_state = db.execute("SELECT state FROM accounts WHERE alias=?", (account,)).fetchone()
            if account_state and account_state[0] != "active":
                raise QuotaBlocked("Account credentials are disabled; verify replacement credentials first")
            pending = db.execute("SELECT COUNT(*) FROM reservations WHERE account=? AND model=? "
                                 "AND state IN ('reserved','needs_reconciliation')", (account, model)).fetchone()[0]
            if pending:
                raise QuotaBlocked("Reconcile outstanding reservations before refreshing quota")
            old = db.execute("SELECT * FROM buckets WHERE account=? AND model=? AND window=?",
                             (account, model, window)).fetchone()
            if old and old["state"] == "auth_failed":
                raise QuotaBlocked("Credential repair needs a new verified account setup")
            db.execute("INSERT INTO buckets VALUES (?,?,?,?,?,'active',?) "
                       "ON CONFLICT(account,model,window) DO UPDATE SET remaining=excluded.remaining, "
                       "reset_at=excluded.reset_at,state='active',evidence_ref=excluded.evidence_ref",
                       (account, model, window, remaining, reset.isoformat(), evidence_ref))
            db.execute("INSERT OR IGNORE INTO accounts VALUES (?,'active')", (account,))

    def activate_initial(self, account: str, model: str):
        """Set the first account once. A later switch must use the receipt gate."""
        with self._transaction() as db:
            route = db.execute("SELECT account FROM routes WHERE model=?", (model,)).fetchone()
            if route and route[0] != account:
                raise QuotaBlocked("Notify the user through the account-switch gate first")
            db.execute("INSERT OR IGNORE INTO routes VALUES (?,?)", (model, account))

    def reserve(self, account: str, model: str, window: str, *, tokens: int,
                job_key: str, now: str | None = None) -> str:
        if type(tokens) is not int or tokens < 1 or not job_key:
            raise ValueError("Require positive prompt+output reserve and a job key")
        now_dt = self._time(now)
        insufficient = False
        reservation_id = str(uuid.uuid4())
        with self._transaction() as db:
            account_state = db.execute("SELECT state FROM accounts WHERE alias=?", (account,)).fetchone()
            if not account_state or account_state[0] != "active":
                raise QuotaBlocked("Account credentials are not active")
            route = db.execute("SELECT account FROM routes WHERE model=?", (model,)).fetchone()
            if not route or route[0] != account:
                raise QuotaBlocked("Account is not the active, notified route for this bucket")
            bucket = db.execute("SELECT * FROM buckets WHERE account=? AND model=? AND window=?",
                                (account, model, window)).fetchone()
            if not bucket:
                raise QuotaBlocked("Initial remaining quota has not been observed")
            if bucket["state"] != "active":
                raise QuotaBlocked(f"Bucket blocked: {bucket['state']}")
            if now_dt >= self._time(bucket["reset_at"]):
                raise QuotaBlocked("Window expired: verify server reset before starting a new window")
            previous = db.execute("SELECT state,outcome FROM reservations WHERE job_key=?", (job_key,)).fetchall()
            if any(r["state"] != "released" or r["outcome"] != "rate_limited" for r in previous):
                raise QuotaBlocked("This job already has an attempt; do not regenerate or duplicate")
            if len(previous) >= 3:
                raise QuotaBlocked("At most two retries after proven rejection")
            active = db.execute("SELECT account,state FROM reservations "
                                "WHERE state IN ('reserved','needs_reconciliation')").fetchall()
            if any(r["account"] == account for r in active) or len(active) >= self.global_limit:
                raise QuotaBlocked("Local concurrency limit reached")
            held = db.execute("SELECT COALESCE(SUM(tokens),0) FROM reservations "
                              "WHERE account=? AND model=? AND window=? "
                              "AND state IN ('reserved','needs_reconciliation')", (account, model, window)).fetchone()[0]
            if bucket["remaining"] - held < tokens:
                db.execute("UPDATE buckets SET state='insufficient_budget' WHERE account=? AND model=? AND window=?",
                           (account, model, window))
                insufficient = True
            else:
                db.execute("INSERT INTO reservations VALUES (?,?,?,?,?,?,'reserved',NULL,NULL,?)",
                           (reservation_id, job_key, account, model, window, tokens, now_dt.isoformat()))
        if insufficient:
            raise QuotaBlocked("Insufficient observed quota; notify before selecting another account")
        return reservation_id

    def available(self, account: str, model: str, window: str, *, job_key: str | None = None) -> int:
        """Read-only scheduling check; reserve() still rechecks atomically.

        Call before claiming a job so missing/expired quota does not turn an
        unattempted job into a leased generation job needing reconciliation.
        """
        with self._connection() as db:
            account_row = db.execute("SELECT state FROM accounts WHERE alias=?", (account,)).fetchone()
            route = db.execute("SELECT account FROM routes WHERE model=?", (model,)).fetchone()
            bucket = db.execute("SELECT * FROM buckets WHERE account=? AND model=? AND window=?",
                                (account, model, window)).fetchone()
            if not account_row or account_row[0] != "active" or not route or route[0] != account:
                raise QuotaBlocked("Account is not the active verified route")
            if not bucket or bucket["state"] != "active" or self._time() >= self._time(bucket["reset_at"]):
                raise QuotaBlocked("Require a current observed quota window")
            active = db.execute("SELECT account FROM reservations WHERE state IN ('reserved','needs_reconciliation')").fetchall()
            if len(active) >= self.global_limit or any(row[0] == account for row in active):
                raise QuotaBlocked("Outstanding reservation/concurrency requires waiting or reconciliation")
            if job_key is not None:
                previous = db.execute("SELECT state,outcome FROM reservations WHERE job_key=?", (job_key,)).fetchall()
                if len(previous) >= 3 or any(row["state"] != "released" or row["outcome"] != "rate_limited" for row in previous):
                    raise QuotaBlocked("Job already attempted; do not regenerate")
            return bucket["remaining"]

    def finish(self, reservation_id: str, *, outcome: str, remaining: int | None = None,
               used: int | None = None, unknown: bool = False):
        if remaining is not None and (type(remaining) is not int or remaining < 0):
            raise ValueError("Invalid observed remaining")
        if used is not None and (type(used) is not int or used < 0):
            raise ValueError("Invalid observed usage")
        with self._transaction() as db:
            row = db.execute("SELECT * FROM reservations WHERE id=?", (reservation_id,)).fetchone()
            if not row or row["state"] != "reserved":
                raise QuotaBlocked("Unknown or already resolved reservation")
            if unknown:
                state, bucket_state = "needs_reconciliation", "needs_reconciliation"
            elif outcome in {"daily_limit", "auth_failed", "invalid_model", "rate_limited", "request_rejected", "redirect_blocked"}:
                state = "released"
                bucket_state = {"daily_limit": "quota_exhausted", "auth_failed": "auth_failed",
                                "invalid_model": "invalid_model"}.get(outcome, "active")
            else:
                state = "settled"
                bucket_state = "active" if remaining is not None else "quota_unverified"
            db.execute("UPDATE reservations SET state=?,used=?,outcome=? WHERE id=?",
                       (state, used, outcome, reservation_id))
            db.execute("UPDATE buckets SET state=?,remaining=COALESCE(?,remaining) "
                       "WHERE account=? AND model=? AND window=?",
                       (bucket_state, remaining, row["account"], row["model"], row["window"]))
            if outcome == "auth_failed":
                db.execute("UPDATE accounts SET state='auth_failed' WHERE alias=?", (row["account"],))

    def reconcile_unknown(self, reservation_id: str, *, remaining: int, evidence_ref: str,
                          processed: bool):
        """Operator only. Even an unprocessed unknown isn't automatically retried."""
        if type(remaining) is not int or remaining < 0 or not evidence_ref or type(processed) is not bool:
            raise ValueError("Require evidence, observed remaining, and processing conclusion")
        with self._transaction() as db:
            row = db.execute("SELECT * FROM reservations WHERE id=?", (reservation_id,)).fetchone()
            if not row or row["state"] != "needs_reconciliation":
                raise QuotaBlocked("Reservation is not awaiting reconciliation")
            db.execute("UPDATE reservations SET state='settled',outcome=? WHERE id=?",
                       ("reconciled_processed" if processed else "reconciled_not_processed", reservation_id))
            db.execute("UPDATE buckets SET state='active',remaining=?,evidence_ref=? "
                       "WHERE account=? AND model=? AND window=?",
                       (remaining, evidence_ref, row["account"], row["model"], row["window"]))

    def request_switch(self, from_account: str, to_account: str, model: str, *, reason: str) -> dict:
        if from_account == to_account or not reason:
            raise ValueError("Require a different account and switch reason")
        event_id = str(uuid.uuid4())
        with self._transaction() as db:
            route = db.execute("SELECT account FROM routes WHERE model=?", (model,)).fetchone()
            if not route or route[0] != from_account:
                raise QuotaBlocked("Switch source is not the active account")
            db.execute("INSERT INTO switches VALUES (?,?,?,?,?,'waiting_notification',NULL)",
                       (event_id, from_account, to_account, model, reason))
        return {"event_id": event_id, "state": "waiting_notification", "from_account": from_account,
                "to_account": to_account, "model_bucket": model, "reason": reason}

    def acknowledge_notification(self, event_id: str, *, receipt: str):
        """Call AFTER the user-facing notification was delivered. A log isn't delivery."""
        if not receipt.strip():
            raise ValueError("Require a user-visible notification receipt")
        with self._transaction() as db:
            event = db.execute("SELECT * FROM switches WHERE id=?", (event_id,)).fetchone()
            if not event or event["state"] != "waiting_notification":
                raise QuotaBlocked("No pending switch")
            changed = db.execute("UPDATE routes SET account=? WHERE model=? AND account=?",
                                 (event["to_account"], event["model"], event["from_account"])).rowcount
            if changed != 1:
                raise QuotaBlocked("Switch became stale; notify the actual source account again")
            db.execute("UPDATE switches SET state='notified',notification_receipt=? WHERE id=?", (receipt, event_id))

    def snapshot(self) -> dict:
        with self._connection() as db:
            return {table: [dict(row) for row in db.execute(f"SELECT * FROM {table}")]
                    for table in ("accounts", "buckets", "reservations", "routes", "switches")}
