"""Resume must preserve completed evidence and never rerun uncertain stages."""
import json
from pathlib import Path
import sys
import tempfile
import unittest
from unittest.mock import patch

sys.path.insert(0, str(Path(__file__).resolve().parents[1] / "scripts/study"))
import solo_batch as batch


class SoloBatchTest(unittest.TestCase):
    def test_completed_command_is_not_repeated(self):
        with tempfile.TemporaryDirectory() as temp:
            folder = Path(temp) / "command"
            command = [sys.executable, "-c", "print('retained')"]
            first = batch.stage(command, folder, 10)
            original = batch.file_pins(folder)
            with patch.object(batch, "run_command", side_effect=AssertionError("must not run")):
                self.assertEqual(first, batch.stage(command, folder, 10))
            self.assertEqual(original, batch.file_pins(folder))

    def test_tampered_command_evidence_is_rejected(self):
        with tempfile.TemporaryDirectory() as temp:
            folder = Path(temp) / "command"
            command = [sys.executable, "-c", "print('retained')"]
            batch.stage(command, folder, 10)
            (folder / "command.log").write_text("altered", encoding="utf-8")
            with self.assertRaises(batch.StageError):
                batch.stage(command, folder, 10)

    def test_intent_change_is_rejected(self):
        with tempfile.TemporaryDirectory() as temp:
            folder = Path(temp) / "command"
            command = [sys.executable, "-c", "print('retained')"]
            batch.stage(command, folder, 10)
            with self.assertRaises(batch.StageError):
                batch.stage(command, folder, 11)

    def test_interrupted_stage_never_automatically_repeats(self):
        with tempfile.TemporaryDirectory() as temp:
            folder = Path(temp) / "command"
            folder.mkdir()
            (folder / "command.log").write_text("partial evidence", encoding="utf-8")
            with patch.object(batch, "run_command", side_effect=AssertionError("must not run")):
                with self.assertRaises(batch.StageError):
                    batch.stage(["unknown-command"], folder, 10)
            self.assertEqual("partial evidence", (folder / "command.log").read_text())

    def test_empty_failure_metrics_are_not_fabricated(self):
        failed = batch.normalize({"status": "failed", "stages": {}, "error": "bad infrastructure"})
        self.assertEqual("INFRA_ERROR", failed["state"])
        self.assertIsNone(failed["fault_detected"])
        self.assertIsNone(failed["valid_on_fixed"])
        self.assertIsNone(failed["executed_tests"])
        self.assertFalse(failed["evaluated"])

    def test_fixed_invalid_and_timeout_are_different(self):
        invalid = batch.normalize({"status": "invalid", "fixed_validation": "failed", "stages": {"fixed-1": {}}})
        timeout = batch.normalize({"status": "failed", "stages": {"fixed-1": {"timed_out": True}}})
        self.assertEqual("INVALID_GENERATED_SUITE", invalid["state"])
        self.assertFalse(invalid["valid_on_fixed"])
        self.assertEqual("TIMEOUT", timeout["state"])
        self.assertIsNone(timeout["valid_on_fixed"])

    def test_context_allocation_independent_of_insertion_order(self):
        sources = {"z.Class": "z" * 100, "a.Class": "a" * 100}
        result = batch.compact_sources(sources, 11)
        self.assertEqual(result, batch.compact_sources(dict(reversed(list(sources.items()))), 11))
        self.assertEqual(11, sum(len(row["excerpt"]) for row in result))
        self.assertEqual("a.Class", result[0]["class"])
        self.assertTrue(all(row["truncated"] for row in result))

    def test_report_keeps_pending_and_missing_denominators(self):
        with tempfile.TemporaryDirectory() as temp:
            output = Path(temp)
            protocol = output / "Experiment/protocol/offline.json"
            batch.write(protocol, {"test": "protocol"})
            config = {"inventory": {"Csv": [1]}, "scope_bugs": 1, "schema": "test"}
            stats = batch.report(output, config)
            self.assertEqual({"PENDING": 4}, stats["states"])
            self.assertEqual(0, stats["attempted_jobs"])
            folder = batch.result_path(output, "Csv-1", "cmaes")
            batch.write(folder / "outcome.json", {
                "case": "Csv-1", "method": "cmaes",
                "state": "INFRA_ERROR", "attempted": True, "evaluated": False,
                "provider_requested": False, "protocol_hash": batch.sha(protocol)})
            batch.seal(folder)
            stats = batch.report(output, config)
            self.assertEqual({"INFRA_ERROR": 1, "PENDING": 3}, stats["states"])
            self.assertEqual(1, stats["attempted_jobs"])
            self.assertEqual(0, stats["evaluated_jobs"])
            self.assertEqual(0, stats["provider_requested_jobs"])

    def test_uncalled_target_is_not_accepted_as_valid_execution(self):
        with tempfile.TemporaryDirectory() as temp:
            folder = Path(temp)
            for name in ("fixed-1", "fixed-2", "buggy", "coverage"):
                batch.write(folder / name / "sqa-stage-counts.json",
                            {"executed": 30, "skipped": 0, "target_checks": 30})
            self.assertEqual(30, batch.invocation_counts(folder, 30)["buggy"]["target_checks"])
            batch.write(folder / "fixed-1/sqa-stage-counts.json",
                        {"executed": 30, "skipped": 0, "target_checks": 0})
            with self.assertRaises(batch.StageError):
                batch.invocation_counts(folder, 30)

    def test_report_rejects_condition_mismatch(self):
        with tempfile.TemporaryDirectory() as temp:
            output = Path(temp)
            batch.write(output / "Experiment/protocol/offline.json", {"test": "protocol"})
            folder = batch.result_path(output, "Csv-1", "cmaes")
            batch.write(folder / "outcome.json", {"case": "Csv-1", "method": "cmaes", "protocol_hash": "wrong"})
            batch.seal(folder)
            with self.assertRaises(batch.StageError):
                batch.report(output, {"inventory": {"Csv": [1]}, "scope_bugs": 1, "schema": "test"})


if __name__ == "__main__":
    unittest.main()
