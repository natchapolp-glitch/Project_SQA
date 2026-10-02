from concurrent.futures import ThreadPoolExecutor
from pathlib import Path
import tempfile
import unittest

from scripts.study.api854.quota import QuotaBlocked, QuotaLedger

NOW = "2026-10-03T16:59:00+00:00"
RESET = "2026-10-03T17:00:00+00:00"


class QuotaTests(unittest.TestCase):
    def setUp(self):
        self.temp = tempfile.TemporaryDirectory()
        self.path = Path(self.temp.name) / "quota.sqlite"
        self.ledger = QuotaLedger(self.path)
        self.ledger.observe("a01", "haiku", "day1", remaining=1000, reset_at=RESET,
                            evidence_ref="mock-quota-observation", now=NOW)
        self.ledger.activate_initial("a01", "haiku")

    def tearDown(self):
        self.temp.cleanup()

    def reserve(self, job="job1", tokens=600, **overrides):
        return self.ledger.reserve("a01", "haiku", "day1", tokens=tokens, job_key=job, now=overrides.get("now", NOW))

    def test_initial_quota_must_be_observed(self):
        self.ledger.activate_initial("a01", "gemini")
        with self.assertRaises(QuotaBlocked):
            self.ledger.reserve("a01", "gemini", "day1", tokens=100, job_key="gemini", now=NOW)

    def test_simultaneous_workers_cannot_double_reserve(self):
        def attempt(index):
            try:
                return self.reserve(job=f"job{index}")
            except QuotaBlocked:
                return None
        with ThreadPoolExecutor(max_workers=2) as executor:
            ids = list(executor.map(attempt, [1, 2]))
        self.assertEqual(sum(x is not None for x in ids), 1)
        self.assertEqual(len(self.ledger.snapshot()["reservations"]), 1)

    def test_unknown_reservation_survives_restart(self):
        reserved = self.reserve()
        self.ledger.finish(reserved, outcome="transport_unknown", unknown=True)
        self.ledger = QuotaLedger(self.path)
        with self.assertRaises(QuotaBlocked):
            self.reserve(job="job2")
        self.assertEqual(self.ledger.snapshot()["reservations"][0]["tokens"], 600)
        self.ledger.reconcile_unknown(reserved, remaining=800, evidence_ref="operator-receipt", processed=True)
        self.reserve(job="job2")
        with self.assertRaises(QuotaBlocked):
            self.reserve()

    def test_crash_before_response_retains_reservation(self):
        self.reserve()
        self.ledger = QuotaLedger(self.path)
        with self.assertRaises(QuotaBlocked):
            self.reserve(job="different-job")

    def test_success_reconciles_server_remaining_not_estimate(self):
        reserved = self.reserve()
        self.ledger.finish(reserved, outcome="response_received", used=300, remaining=700)
        self.assertEqual(self.ledger.snapshot()["buckets"][0]["remaining"], 700)
        self.reserve(job="job2", tokens=650)

    def test_missing_quota_blocks_next_request(self):
        self.ledger.finish(self.reserve(), outcome="response_received", used=300)
        with self.assertRaises(QuotaBlocked):
            self.reserve(job="job2")

    def test_midnight_does_not_refill_assumed_quota(self):
        with self.assertRaises(QuotaBlocked):
            self.reserve(now="2026-10-03T17:00:01+00:00")
        with self.assertRaises(QuotaBlocked):
            self.ledger.reserve("a01", "haiku", "day2", tokens=100, job_key="job", now=RESET)
        self.ledger.observe("a01", "haiku", "day2", remaining=500, reset_at="2026-10-04T17:00:00Z",
                            evidence_ref="verified-new-window", now=RESET)
        self.ledger.reserve("a01", "haiku", "day2", tokens=100, job_key="job", now=RESET)

    def test_insufficient_quota_state_persists(self):
        with self.assertRaises(QuotaBlocked):
            self.reserve(tokens=1001)
        self.assertEqual(self.ledger.snapshot()["buckets"][0]["state"], "insufficient_budget")

    def test_switch_requires_user_visible_notification(self):
        self.ledger.observe("a02", "haiku", "day1", remaining=1000, reset_at=RESET,
                            evidence_ref="mock-quota", now=NOW)
        event = self.ledger.request_switch("a01", "a02", "haiku", reason="quota exhausted")
        with self.assertRaises(QuotaBlocked):
            self.ledger.reserve("a02", "haiku", "day1", tokens=100, job_key="switch", now=NOW)
        with self.assertRaises(QuotaBlocked):
            self.ledger.activate_initial("a02", "haiku")
        self.ledger.acknowledge_notification(event["event_id"], receipt="user-visible-message-id")
        self.ledger.reserve("a02", "haiku", "day1", tokens=100, job_key="switch", now=NOW)

    def test_daily_limit_auth_invalid_model_have_distinct_states(self):
        for outcome, expected in [("daily_limit", "quota_exhausted"), ("auth_failed", "auth_failed"),
                                  ("invalid_model", "invalid_model")]:
            with self.subTest(outcome=outcome):
                path = Path(self.temp.name) / (outcome + ".sqlite")
                ledger = QuotaLedger(path)
                ledger.observe("a01", "haiku", "day", remaining=1000, reset_at=RESET,
                               evidence_ref="mock", now=NOW)
                ledger.activate_initial("a01", "haiku")
                rid = ledger.reserve("a01", "haiku", "day", tokens=100, job_key="job", now=NOW)
                ledger.finish(rid, outcome=outcome)
                self.assertEqual(ledger.snapshot()["buckets"][0]["state"], expected)

    def test_proven_rejections_allow_at_most_two_retries(self):
        for _ in range(3):
            self.ledger.finish(self.reserve(), outcome="rate_limited")
        with self.assertRaises(QuotaBlocked):
            self.reserve()

    def test_observe_cannot_erase_pending_usage(self):
        self.reserve()
        with self.assertRaises(QuotaBlocked):
            self.ledger.observe("a01", "haiku", "day1", remaining=1000, reset_at=RESET,
                                evidence_ref="new", now=NOW)

    def test_global_limit_is_not_one_limit_per_model(self):
        for account, model in [("a02", "gemini"), ("a03", "other")]:
            self.ledger.observe(account, model, "day1", remaining=1000, reset_at=RESET,
                                evidence_ref="mock", now=NOW)
            self.ledger.activate_initial(account, model)
        self.reserve(tokens=100)
        self.ledger.reserve("a02", "gemini", "day1", tokens=100, job_key="second", now=NOW)
        with self.assertRaises(QuotaBlocked):
            self.ledger.reserve("a03", "other", "day1", tokens=100, job_key="third", now=NOW)
