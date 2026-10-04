"""Request replay, missing measurements and extension safety controls."""
from pathlib import Path
import sys
import tempfile
import unittest
from types import SimpleNamespace
from unittest.mock import Mock

sys.path.insert(0, str(Path(__file__).resolve().parents[1] / "scripts/study"))
import solo_ai as ai


class SoloAiTest(unittest.TestCase):
    def test_missing_usage_is_unknown_not_partial_total(self):
        rows = [{"usage": {"prompt_tokens": 12, "completion_tokens": 3}, "request_seconds": 1},
                {"error_state": "INFRA_ERROR", "request_seconds": 2}]
        result = ai.usage_totals(rows, 2)
        self.assertIsNone(result["prompt_tokens"])
        self.assertEqual(result["completed_provider_responses"], 1)
        self.assertEqual(result["generation_seconds"], 3)

    def test_complete_usage_can_be_summed(self):
        result = ai.usage_totals([{"usage": {"prompt_tokens": 12, "completion_tokens": 3}, "request_seconds": 1}], 1)
        self.assertEqual(result["prompt_tokens"], 12)
        self.assertEqual(result["completion_tokens"], 3)

    def test_wrong_model_and_truncation_are_not_success(self):
        with self.assertRaises(ai.OutcomeError):
            ai.decode_messages({"model": "other"})
        _, record = ai.decode_messages({"model": "anthropic/claude-sonnet-5", "content": [{"type": "text", "text": "partial"}], "stop_reason": "max_tokens"})
        self.assertEqual(record["outcome"], "truncated")

    def test_ignored_suite_rejected_without_trimming(self):
        text = 'import org.junit.*; public class ExampleTest { @Ignore @Test public void a() {} }'
        with self.assertRaises(ValueError):
            ai.extract_sources(text, 12, [])

    def test_production_shadow_rejected(self):
        text = 'package p; import org.junit.*; public class ExampleTest { @Test public void a() {} }'
        with self.assertRaises(ValueError):
            ai.extract_sources(text, 12, ["p.ExampleTest"])

    def test_zero_or_duplicate_started_tests_rejected(self):
        with tempfile.TemporaryDirectory() as temp:
            root = Path(temp)
            for stage in ("fixed-1", "fixed-2", "buggy", "coverage"):
                path = root / stage / "all_tests"
                path.parent.mkdir()
                path.write_text("a(ExampleTest)\nb(ExampleTest)\n")
            self.assertEqual(ai.observed_starts(root, 2)["coverage"], 2)
            (root / "buggy/all_tests").write_text("a(ExampleTest)\na(ExampleTest)\n")
            with self.assertRaises(ValueError):
                ai.observed_starts(root, 2)

    def test_uncertain_request_never_resent(self):
        with tempfile.TemporaryDirectory() as temp:
            root = Path(temp)
            (root / "AI1_KKU_Claude/Result/Csv-1/run-final/P01").mkdir(parents=True)
            client = SimpleNamespace(account=SimpleNamespace(alias="a01"), _request=Mock())
            with self.assertRaises(ai.OutcomeError):
                ai.request_once(client, "kku-claude", "P01", "prompt", root, "Csv-1", {})
            client._request.assert_not_called()

    def test_quota_guard_does_not_send_or_create_intent(self):
        with tempfile.TemporaryDirectory() as temp:
            root = Path(temp)
            client = SimpleNamespace(account=SimpleNamespace(alias="a01"), _request=Mock())
            with self.assertRaises(ai.OutcomeError) as raised:
                ai.request_once(client, "kku-claude", "P02", "prompt", root, "Csv-1", {}, 1)
            self.assertEqual(raised.exception.state, "QUOTA_PAUSED")
            client._request.assert_not_called()
            self.assertFalse(list(root.rglob("request-intent.json")))

    def test_completed_request_replay_uses_identical_bindings(self):
        with tempfile.TemporaryDirectory() as temp:
            root = Path(temp)
            protocol = {"settings_requested": {"max_tokens": 4096, "temperature": 0, "stream": False},
                        "claude_thinking_requested": {"type": "disabled"}, "claude_endpoint": "/messages"}
            ai.batch.write(root / "Experiment/protocol/ai.json", protocol)
            payload = {"model": "anthropic/claude-sonnet-5", "content": [{"type": "text", "text": "answer"}], "stop_reason": "end_turn"}
            client = SimpleNamespace(account=SimpleNamespace(alias="a01"), _request=Mock(return_value=(payload, {"body": payload})))
            ai.request_once(client, "kku-claude", "P01", "prompt", root, "Csv-1", protocol)
            text, _ = ai.request_once(client, "kku-claude", "P01", "prompt", root, "Csv-1", protocol)
            self.assertEqual(text, "answer")
            self.assertEqual(client._request.call_count, 1)
            ai.batch.write(root / "Experiment/protocol/ai.json", {**protocol, "changed": True})
            with self.assertRaises(ai.OutcomeError):
                ai.request_once(client, "kku-claude", "P01", "prompt", root, "Csv-1", protocol)
            self.assertEqual(client._request.call_count, 1)


if __name__ == "__main__":
    unittest.main()
