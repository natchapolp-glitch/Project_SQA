"""Reporting integrity checks; temporary fixtures are not experiment outcomes."""
import json
from pathlib import Path
import sys
import tempfile
import unittest

sys.path.insert(0, str(Path(__file__).resolve().parents[1]))
from aom_ready_csv import safe_child, digest, verify_manifest
from aom_ready_report import stage_counts, flatten, read_started


class ReadyEvidenceTests(unittest.TestCase):
    def setUp(self):
        self.temp = tempfile.TemporaryDirectory()
        self.addCleanup(self.temp.cleanup)
        self.root = Path(self.temp.name)

    def test_archive_and_manifest_paths_cannot_escape(self):
        for name in ("../secret", "/absolute", "C:/secret", "a\\..\\secret"):
            with self.subTest(name=name), self.assertRaises(ValueError):
                safe_child(self.root, name)

    def test_checksum_tampering_rejected(self):
        file = self.root / "raw.txt"
        file.write_bytes(b"original")
        (self.root / "checksums.json").write_text(json.dumps({"raw.txt": digest(b"original")}), encoding="utf-8")
        self.assertEqual(verify_manifest(self.root), 1)
        file.write_bytes(b"changed")
        with self.assertRaises(ValueError):
            verify_manifest(self.root)

    def test_unmeasured_values_do_not_become_zero(self):
        row = flatten({"status": "invalid_generation_truncated", "fault_detected": None})
        for name in ("fault_detected", "line_covered", "buggy_executed", "coverage_target_checks"):
            self.assertIsNone(row[name])
        missing = stage_counts(self.root / "not-attempted", 30)
        self.assertIsNone(missing["executed"])

    def test_formatter_starts_do_not_certify_missing_target_counter(self):
        (self.root / "all_tests").write_text("one(ExampleTest)\ntwo(ExampleTest)\n", encoding="utf-8")
        count = stage_counts(self.root, 2)
        self.assertEqual(count["executed"], 2)
        self.assertIsNone(count["target_checks"])
        self.assertIsNone(count["skipped"])
        with self.assertRaises(ValueError):
            stage_counts(self.root, 2, require_target=True)

    def test_independent_start_evidence_rejects_counter_disagreement(self):
        (self.root / "all_tests").write_text("one(ExampleTest)\ntwo(ExampleTest)\n", encoding="utf-8")
        path = self.root / "sqa-stage-counts.json"
        path.write_text(json.dumps({"executed": 1, "skipped": 0, "target_checks": 1}), encoding="utf-8")
        with self.assertRaises(ValueError):
            stage_counts(self.root, 2, require_target=True)
        path.write_text(json.dumps({"executed": 2, "skipped": 0, "target_checks": 2}), encoding="utf-8")
        self.assertEqual(stage_counts(self.root, 2, True)["target_checks"], 2)

    def test_skipped_or_unchecked_cases_cannot_be_full_algorithm_measurement(self):
        (self.root / "all_tests").write_text("one(ExampleTest)\ntwo(ExampleTest)\n", encoding="utf-8")
        for counts in ({"executed": 2, "skipped": 1, "target_checks": 2},
                       {"executed": 2, "skipped": 0, "target_checks": 1},
                       {"executed": True, "skipped": 0, "target_checks": 2}):
            (self.root / "sqa-stage-counts.json").write_text(json.dumps(counts), encoding="utf-8")
            with self.subTest(counts=counts), self.assertRaises(ValueError):
                stage_counts(self.root, 2, require_target=True)

    def test_duplicate_test_starts_are_not_distinct_executed_methods(self):
        (self.root / "all_tests").write_text("one(ExampleTest)\none(ExampleTest)\n", encoding="utf-8")
        with self.assertRaises(ValueError):
            read_started(self.root)


if __name__ == "__main__":
    unittest.main()
