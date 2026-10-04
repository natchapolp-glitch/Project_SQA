"""A deadline report must not turn failures or pending work into success."""
from pathlib import Path
import sys
import tempfile
import unittest

sys.path.insert(0, str(Path(__file__).resolve().parents[1] / "scripts/study"))
import solo_batch as batch
import nightly_report as report
import solo_scope as scope


class ReportTest(unittest.TestCase):
    def roots(self, base):
        offline, pilot, nightly = [base / n for n in ("offline", "pilot", "nightly")]
        batch.write(offline / "Experiment/protocol/offline.json", {"inventory": {"Csv": [1, 2]}, "scope_bugs": 2})
        digest = batch.sha(offline / "Experiment/protocol/offline.json")
        for root in (pilot, nightly):
            batch.write(root / "Experiment/protocol/ai.json", {"offline_protocol_sha256": digest})
        return offline, pilot, nightly

    def outcome(self, root, case, method, state, **values):
        protocol = "ai.json" if method.startswith("kku-") else "offline.json"
        path = root / "Experiment/evaluations" / case / method / "run-final"
        batch.write(path / "outcome.json", {"case": case, "method": method, "state": state,
                    "protocol_hash": batch.sha(root / "Experiment/protocol" / protocol),
                    "attempted": True, "evaluated": state == "DONE", **values})
        batch.seal(path)
        return path

    def test_failure_pause_and_missing_values_are_preserved(self):
        with tempfile.TemporaryDirectory() as tmp:
            offline, pilot, nightly = self.roots(Path(tmp))
            self.outcome(pilot, "Csv-1", "kku-claude", "OUTPUT_INCOMPLETE", prompt_tokens=100)
            self.outcome(nightly, "Csv-2", "kku-claude", "QUOTA_PAUSED")
            self.outcome(nightly, "Csv-2", "kku-gemini", "DONE", valid_on_fixed=True, fault_detected=False,
                         line_covered=1, line_total=2, branch_covered=0, branch_total=0)
            rows, stats = report.collect(offline, [pilot, nightly])
            self.assertEqual(len(rows), 8)
            self.assertEqual(stats["states"]["PENDING"], 5)
            gemini = stats["per_method"]["kku-gemini"]
            self.assertEqual(gemini["fault_detection_denominator"], 1)
            self.assertEqual(gemini["fault_detecting_cases"], 0)
            self.assertEqual(gemini["mean_fixed_line_coverage_percent"], 50)
            self.assertIsNone(gemini["mean_fixed_condition_coverage_percent"])
            self.assertEqual(stats["bugs_four_methods_evaluated"], 0)

    def test_duplicate_bug_method_cannot_be_pooled(self):
        with tempfile.TemporaryDirectory() as tmp:
            offline, pilot, nightly = self.roots(Path(tmp))
            for root in (pilot, nightly):
                self.outcome(root, "Csv-1", "kku-claude", "DONE")
            with self.assertRaises(ValueError):
                report.collect(offline, [pilot, nightly])

    def test_tampered_evidence_cannot_enter_submission(self):
        with tempfile.TemporaryDirectory() as tmp:
            offline, pilot, nightly = self.roots(Path(tmp))
            path = self.outcome(pilot, "Csv-1", "kku-claude", "DONE")
            (path / "outcome.json").write_text("{}")
            with self.assertRaises(batch.StageError):
                report.collect(offline, [pilot, nightly])

    def test_selected_scope_is_not_full_inventory_or_success_filter(self):
        with tempfile.TemporaryDirectory() as tmp:
            offline, pilot, nightly = self.roots(Path(tmp))
            path = Path(tmp) / 'scope.json'
            batch.write(path, {'schema': 'solo-selected-scope.v1', 'selected_bugs': 1,
                              'planned_jobs': 4, 'cases': ['Csv-1'],
                              'offline_protocol_sha256': batch.sha(offline / 'Experiment/protocol/offline.json')})
            self.outcome(pilot, 'Csv-1', 'kku-claude', 'INVALID_AFTER_REPAIR')
            rows, stats = report.collect(offline, [pilot, nightly], path)
            self.assertEqual(len(rows), 4)
            self.assertEqual(stats['planned_bugs'], 1)
            self.assertEqual(stats['full_inventory_bugs'], 2)
            self.assertEqual(stats['states']['INVALID_AFTER_REPAIR'], 1)
            self.outcome(nightly, 'Csv-2', 'kku-gemini', 'DONE')
            with self.assertRaises(ValueError):
                report.collect(offline, [pilot, nightly], path)


if __name__ == "__main__":
    unittest.main()
