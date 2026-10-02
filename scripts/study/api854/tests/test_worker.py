from dataclasses import replace
from datetime import datetime, timedelta, timezone
import json
from pathlib import Path
import tempfile
import unittest

from scripts.study.api854.generate_worker import GenerationJob, GenerationWorker
from scripts.study.api854.kku_client import Account, KKUClient, Model
from scripts.study.api854.quota import QuotaBlocked, QuotaLedger
from .test_client import ScriptedTransport, completion as generic_completion, response


def completion(**overrides):
    return generic_completion(**{"model": "claude-sonnet-5", "provider": "Claude", **overrides})


class FakeHandoff:
    def __init__(self, fail=False):
        self.fail, self.results = fail, []

    def publish_generation(self, result):
        self.results.append(result)
        if self.fail:
            raise TimeoutError("lost publish receipt")
        return "mock-queue-receipt"


class WorkerTests(unittest.TestCase):
    def setUp(self):
        self.temp = tempfile.TemporaryDirectory()
        self.root = Path(self.temp.name)
        self.ledger = QuotaLedger(self.root / "quota.sqlite")
        self.ledger.observe("a01", "sonnet", "window", remaining=200000,
                            reset_at=(datetime.now(timezone.utc) + timedelta(hours=1)).isoformat(),
                            evidence_ref="mock-only-quota")
        self.ledger.activate_initial("a01", "sonnet")
        self.job = GenerationJob("mock-only", "a" * 64, "Lang", 1, "kku-claude", "fixed source", "b" * 64)

    def tearDown(self):
        self.temp.cleanup()

    def worker(self, transport, handoff=None):
        return GenerationWorker(KKUClient(Account("a01", "sk_fake_credential_not_real_123456"), transport=transport),
                                self.ledger, artifact_root=self.root / "artifacts",
                                model=Model("claude-sonnet-5", "claude-sonnet-5", "Claude"),
                                bucket="sonnet", window="window", handoff=handoff, condition="mock-integration")

    def generate(self, worker, job=None):
        return worker.generate(job or self.job, prompt_token_reserve=1000, max_tokens=4000)

    def test_end_to_end_exports_evidence_handoff_but_not_evaluation(self):
        adapter = FakeHandoff()
        transport = ScriptedTransport(response(payload=completion()))
        result = self.generate(self.worker(transport, adapter))
        path = Path(result["artifact_path"])
        self.assertEqual(result["condition"], "mock-integration")
        self.assertEqual(result["queue_status"], "published")
        self.assertEqual(result["evaluation_status"], "not_attempted")
        self.assertIsNone(result["usable_suite"])
        self.assertIsNone(result["fault_detected"])
        self.assertEqual(len(adapter.results), 1)
        for file in ("request.json", "prompt.md", "attempt-start.json", "send-intent.json",
                     "response-evidence.json", "response.txt", "generation-result.json", "source-block-001.java"):
            self.assertTrue((path / file).is_file(), file)
        self.assertEqual(self.ledger.snapshot()["reservations"][0]["state"], "settled")

    def test_start_and_send_intent_exist_before_transport(self):
        def transport(*args):
            self.assertEqual(len(list(self.root.rglob("attempt-start.json"))), 1)
            self.assertEqual(len(list(self.root.rglob("send-intent.json"))), 1)
            return response(payload=completion())
        self.generate(self.worker(transport))

    def test_no_live_queue_does_not_claim_connected(self):
        result = self.generate(self.worker(ScriptedTransport(response(payload=completion()))))
        self.assertEqual(result["queue_status"], "not_connected")

    def test_publish_failure_does_not_regenerate(self):
        transport = ScriptedTransport(response(payload=completion()))
        result = self.generate(self.worker(transport, FakeHandoff(fail=True)))
        self.assertEqual(result["queue_status"], "publish_pending")
        self.assertEqual(len(transport.calls), 1)
        with self.assertRaises(QuotaBlocked):
            self.generate(self.worker(transport))

    def test_unknown_outcome_has_evidence_and_keeps_quota(self):
        result = self.generate(self.worker(ScriptedTransport(TimeoutError())))
        self.assertEqual(result["generation_outcome"], "needs_reconciliation")
        self.assertEqual(self.ledger.snapshot()["reservations"][0]["state"], "needs_reconciliation")
        self.assertTrue((Path(result["artifact_path"]) / "error.json").exists())
        with self.assertRaises(QuotaBlocked):
            self.generate(self.worker(ScriptedTransport(response(payload=completion()))))

    def test_rejected_requests_remain_evidenced(self):
        result = self.generate(self.worker(ScriptedTransport(response(401, {"error": "This model reached daily limit."}))))
        self.assertEqual(result["generation_outcome"], "daily_limit")
        self.assertEqual(self.ledger.snapshot()["buckets"][0]["state"], "quota_exhausted")

    def test_no_java_and_truncation_do_not_become_usable(self):
        for index, (content, reason, expected) in enumerate([
            ("I cannot provide Java", "stop", "generation_failed_no_java_fence"),
            ("```java\nclass Broken", "length", "truncated"),
        ], 1):
            with self.subTest(expected=expected):
                payload = completion(choices=[{"finish_reason": reason, "message": {"content": content}}])
                result = self.generate(self.worker(ScriptedTransport(response(payload=payload))),
                                       replace(self.job, bug_id=index))
                self.assertEqual(result["generation_outcome"], expected)
                self.assertFalse(result["source_blocks"])

    def test_credentials_never_exported_even_when_echoed(self):
        payload = completion(id="sk_fake_credential_not_real_123456", choices=[{
            "finish_reason": "stop", "message": {"content": "sk_fake_credential_not_real_123456"}}])
        result = self.generate(self.worker(ScriptedTransport(response(payload=payload))))
        self.assertEqual(result["generation_outcome"], "generation_failed_sensitive_response")
        for file in Path(result["artifact_path"]).iterdir():
            self.assertNotIn("sk_fake_credential", file.read_text(encoding="utf-8"))

    def test_prompt_cannot_contain_key(self):
        transport = ScriptedTransport(response(payload=completion()))
        with self.assertRaises(ValueError):
            self.generate(self.worker(transport), replace(self.job, prompt="sk_fake_credential_not_real_123456"))
        self.assertFalse(transport.calls)

    def test_unsafe_paths_and_model_fallback_rejected(self):
        with self.assertRaises(ValueError):
            replace(self.job, project="../Lang")
        worker = self.worker(ScriptedTransport(response(payload=completion())))
        worker.model = Model(9, "claude-haiku-test")
        from scripts.study.api854.kku_client import KKUError
        with self.assertRaises(KKUError):
            self.generate(worker)
