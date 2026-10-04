"""Reject mixing stale protocol bindings and duplicate experiment jobs."""
from pathlib import Path
import sys
import tempfile
import unittest

sys.path.insert(0, str(Path(__file__).resolve().parents[1] / "scripts/study"))
import solo_results as report


class SoloResultsTest(unittest.TestCase):
    def fixture(self, root):
        offline, ai = root / "offline", root / "ai"
        report.batch.write(offline / "Experiment/protocol/offline.json", {"inventory": {"Csv": [1]}, "scope_bugs": 1})
        digest = report.batch.sha(offline / "Experiment/protocol/offline.json")
        report.batch.write(ai / "Experiment/protocol/ai.json", {"offline_protocol_sha256": digest})
        return offline, ai

    def outcome(self, root, method, protocol):
        folder = root / "Experiment/evaluations/Csv-1" / method / "run-final"
        report.batch.write(folder / "outcome.json", {"case": "Csv-1", "method": method, "protocol_hash": protocol,
                                                   "state": "INVALID_AFTER_REPAIR", "attempted": True,
                                                   "evaluated": True, "provider_requested": True,
                                                   "request_attempts": 3, "completed_provider_responses": 3})
        report.batch.seal(folder)
        return folder

    def test_invalid_job_and_requests_are_not_repeats_or_done(self):
        with tempfile.TemporaryDirectory(dir=report.batch.ROOT) as temp:
            offline, ai = self.fixture(Path(temp))
            self.outcome(ai, "kku-claude", report.batch.sha(ai / "Experiment/protocol/ai.json"))
            rows, stats = report.collect(offline, ai)
            self.assertEqual(len(rows), 4)
            self.assertEqual(stats["attempted_jobs"], 1)
            self.assertEqual(stats["provider_request_attempts"], 3)
            self.assertEqual(stats["bugs_four_methods_done"], 0)
            self.assertIsNone(rows[0]["fault_detected"])

    def test_wrong_protocol_is_rejected_even_with_valid_receipt(self):
        with tempfile.TemporaryDirectory(dir=report.batch.ROOT) as temp:
            offline, ai = self.fixture(Path(temp))
            self.outcome(ai, "kku-claude", "other-condition")
            with self.assertRaises(ValueError):
                report.collect(offline, ai)

    def test_mutated_raw_evidence_is_rejected(self):
        with tempfile.TemporaryDirectory(dir=report.batch.ROOT) as temp:
            offline, ai = self.fixture(Path(temp))
            folder = self.outcome(ai, "kku-claude", report.batch.sha(ai / "Experiment/protocol/ai.json"))
            (folder / "outcome.json").write_text("{}")
            with self.assertRaises(report.batch.StageError):
                report.collect(offline, ai)


if __name__ == "__main__":
    unittest.main()
