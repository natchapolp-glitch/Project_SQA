"""Data integrity checks; fixtures never enter study results."""
import importlib.util
import json
from pathlib import Path
import tempfile
import unittest

spec = importlib.util.spec_from_file_location("aggregate", Path(__file__).with_name("aggregate.py"))
aggregate = importlib.util.module_from_spec(spec)
spec.loader.exec_module(aggregate)


class AggregationIntegrityTest(unittest.TestCase):
    def row(self, **updates):
        return {"run_id": "r1", "project": "Lang", "bug_id": "1", "generator": "cmaes",
                "seed": 1, "budget": 30, "status": "complete", "compile_status": "passed",
                "fault_detected": False, "valid": True, **updates}

    def test_missing_coverage_has_no_observations(self):
        result = aggregate.summarize([self.row()])
        self.assertIsNone(result["line_coverage_macro"])
        self.assertIsNone(result["line_covered_sum"])
        self.assertEqual(result["line_coverage_observations"], 0)
        self.assertEqual(result["fault_evaluated_runs"], 1)
        self.assertEqual(result["fault_detection_rate"], 0.0)

    def test_incomplete_run_excluded_from_fault_and_coverage(self):
        result = aggregate.summarize([self.row(status="partial", fault_detected=True, line_covered=4, line_total=5)])
        self.assertEqual(result["fault_evaluated_runs"], 0)
        self.assertEqual(result["line_coverage_observations"], 0)
        self.assertEqual(result["incomplete_runs"], 1)

    def test_coverage_macro_micro_denominators_differ(self):
        result = aggregate.summarize([self.row(line_covered=1, line_total=2), self.row(line_covered=8, line_total=10), self.row()])
        self.assertAlmostEqual(result["line_coverage_macro"], .65)
        self.assertAlmostEqual(result["line_coverage_micro"], .75)
        self.assertEqual(result["line_coverage_observations"], 2)

    def test_repeated_seed_counts_one_bug(self):
        result = aggregate.summarize([self.row(fault_detected=True), self.row(seed=2, fault_detected=True)])
        self.assertEqual(result["fault_detected_runs"], 2)
        self.assertEqual(result["fault_detected_bugs"], 1)

    def test_generation_and_total_cost_exclude_incomplete_runs(self):
        result = aggregate.summarize([self.row(generation_seconds=5, total_seconds=12),
                                      self.row(status='failed', generation_seconds=100, total_seconds=200)])
        self.assertEqual(result['generation_seconds_observations'], 1)
        self.assertEqual(result['generation_seconds_mean'], 5)
        self.assertEqual(result['total_seconds_mean'], 12)

    def test_bug_rate_and_run_rate_have_different_denominators(self):
        result = aggregate.summarize([self.row(fault_detected=True), self.row(seed=2, fault_detected=False)])
        self.assertEqual(result['fault_detection_rate'], 1.0)
        self.assertEqual(result['fault_detection_run_rate'], 0.5)

    def test_duplicate_invalid_and_missing_manifest_are_explicit(self):
        with tempfile.TemporaryDirectory() as tmp:
            root = Path(tmp)
            for name, row in [("one", self.row()), ("duplicate", self.row()), ("invalid", self.row(run_id="r2", line_covered=6, line_total=5))]:
                p = root / "study" / name / "record.json"
                p.parent.mkdir(parents=True)
                p.write_text(json.dumps(row), encoding="utf-8")
            manifest = root / "manifest.json"
            manifest.write_text(json.dumps([self.row(), self.row(generator="claude")]))
            result = aggregate.aggregate(root, root / "study", root / "out", manifest)
            self.assertEqual(result["overall"]["observed_runs"], 3)
            self.assertEqual(result["overall"]["completed_runs"], 1)
            self.assertEqual(len(result["issues"]), 2)
            self.assertEqual(len(result["missing_runs"]), 1)
            self.assertEqual(result["missing_runs"][0]["generator"], "claude")

    def test_string_boolean_rejected(self):
        with tempfile.TemporaryDirectory() as tmp:
            p = Path(tmp) / "record.json"
            p.write_text("{}")
            row = aggregate.normalize(self.row(fault_detected="false"), p, Path(tmp))
            self.assertFalse(row["valid"])
            self.assertIsNone(row["fault_detected"])


if __name__ == "__main__":
    unittest.main()
