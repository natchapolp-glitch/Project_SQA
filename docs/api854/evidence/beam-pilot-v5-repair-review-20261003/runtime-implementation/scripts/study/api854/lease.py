"""Renew one existing claim while KKU runs; never claim or resend a request."""
from __future__ import annotations

import math
import threading
import time

from .champ_queue import QueueError


class LeaseHeartbeat:
    """An uncertain renew stops publication, but preserves in-flight evidence.

    Owns the existing claim dict, so handoff sees the latest confirmed expiry.
    Schema 1.0 renewal does not change the lease token/version. No automatic
    recovery/retry: the operator reconciles with Aom before continuing.
    """
    def __init__(self, client, claim: dict, *, lease_seconds: int = 900,
                 interval_seconds: float = 60):
        if type(lease_seconds) is not int or not 60 <= lease_seconds <= 3600:
            raise ValueError("Lease seconds must be 60..3600")
        if not math.isfinite(interval_seconds) or not 0 < interval_seconds < lease_seconds / 2:
            raise ValueError("Heartbeat interval must be less than half the lease")
        if not claim.get("job") or not claim.get("attempt_id") or not claim.get("lease_token"):
            raise ValueError("Require an existing queue claim")
        self.client, self.claim = client, claim
        self.lease_seconds, self.interval_seconds = lease_seconds, interval_seconds
        self._stop = threading.Event()
        self._lock = threading.RLock()
        self._thread = None
        self._error = None
        self._entered = False

    def check(self):
        with self._lock:
            if self._error is not None:
                raise self._error
            expiry = self.claim.get("lease_expires_at_unix")
            if type(expiry) not in (int, float) or not math.isfinite(expiry) or expiry <= time.time():
                raise QueueError("lease_expired")

    def renew_once(self):
        with self._lock:
            self.check()
            try:
                receipt = self.client.renew(self.claim, lease_seconds=self.lease_seconds)
                expiry = receipt.get("lease_expires_at_unix")
                if type(expiry) not in (int, float) or not math.isfinite(expiry) or expiry <= time.time():
                    raise QueueError("invalid_renewal_receipt", unknown=True)
                self.claim["lease_expires_at_unix"] = expiry
            except Exception as error:
                # Do not retain arbitrary exception messages (may include secrets).
                self._error = error if isinstance(error, QueueError) else QueueError("renewal_unknown", unknown=True)
                self._stop.set()
                raise self._error from None

    def _run(self):
        while not self._stop.wait(self.interval_seconds):
            try:
                self.renew_once()
            except QueueError:
                return

    def __enter__(self):
        if self._entered:
            raise ValueError("A heartbeat cannot be reused")
        self._entered = True
        # Confirm ownership before any model request or quota reservation.
        self.renew_once()
        self._thread = threading.Thread(target=self._run, name="api854-lease", daemon=True)
        self._thread.start()
        return self

    def stop(self):
        self._stop.set()
        if self._thread is not None:
            self._thread.join()

    def before_complete(self):
        # No renew may race with the final stage transition. Obtain a fresh
        # full lease after joining, then let the handoff persist/send its intent.
        self.stop()
        self.renew_once()

    def __exit__(self, exc_type, exc_value, traceback):
        self.stop()


def generate_with_lease(worker, job, heartbeat: LeaseHeartbeat, *, owner=None, **generation_options):
    """Generate once under an existing claim; caller supplies frozen job/policy.

    No payload interpretation, scheduler, account switch or semantic retry.
    Without a handoff, the observed result stays local for later publication.
    A failed lease during HTTP preserves generation artifacts as publish_pending.
    """
    if worker.handoff is not None and (
        getattr(worker.handoff, "lease_guard", None) is not heartbeat
        or getattr(worker.handoff, "claim", None) is not heartbeat.claim
    ):
        raise ValueError("Handoff must share this heartbeat and claim")
    options = dict(generation_options)
    expected_owner = options.pop("expected_owner", owner or "champ")
    if owner is not None and expected_owner != owner:
        raise ValueError("Conflicting owner arguments")
    attempt_id = options.pop("attempt_id", heartbeat.claim["attempt_id"])
    if attempt_id != heartbeat.claim["attempt_id"]:
        raise ValueError("Use the queue claim's attempt ID")
    queue_job = heartbeat.claim["job"]
    if expected_owner not in {"aom", "beam", "champ"} or queue_job.get("owner") != expected_owner or queue_job.get("stage") != "generate":
        raise ValueError("Require the assigned owner's active generation claim")
    for key in ("run_id", "protocol_hash", "project", "bug_id", "approach"):
        if queue_job.get(key) != getattr(job, key):
            raise ValueError(f"Generation job does not match claim {key}")
    with heartbeat:
        return worker.generate(job, attempt_id=attempt_id, **options)
