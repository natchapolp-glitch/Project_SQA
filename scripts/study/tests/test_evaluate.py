"""Evaluator contract tests; fake Defects4J evidence is never study data."""

import importlib.util
import io
import json
from pathlib import Path
import sys
import tarfile
import tempfile
import unittest
from unittest.mock import patch


MODULE_PATH = Path(__file__).resolve().parents[1] / "evaluate.py"
SPEC = importlib.util.spec_from_file_location("study_evaluate", MODULE_PATH)
evaluate = importlib.util.module_from_spec(SPEC)
sys.modules[SPEC.name] = evaluate
SPEC.loader.exec_module(evaluate)


class EvidenceParsingTests(unittest.TestCase):
    def test_assertion_failures_are_valid_results_despite_zero_cli_exit(self):
        result = evaluate.parse_test_evidence(
            "Running ant...\nFailing tests: 1\n  - ExampleTest::edge\n",
            "--- ExampleTest::edge\njunit.framework.AssertionFailedError\n",
        )
        self.assertEqual(result, {"failure_count": 1, "failing_tests": ["ExampleTest::edge"]})

    def test_missing_conflicting_and_duplicate_test_evidence_is_rejected(self):
        for log, failures in (
            ("BUILD SUCCESSFUL", ""),
            ("Failing tests: 0\n", None),
            ("Failing tests: 1\n", ""),
            ("Failing tests: 0\n", "error output"),
            ("Failing tests: 0\nFailing tests: 0\n", ""),
            ("Failing tests: 2\n", "--- Test::method\n--- Test::method\n"),
        ):
            with self.subTest(log=log, failures=failures), self.assertRaises(evaluate.EvidenceError):
                evaluate.parse_test_evidence(log, failures)

    def test_coverage_preserves_true_zero_counters(self):
        result = evaluate.parse_coverage_csv("LinesTotal,LinesCovered,ConditionsTotal,ConditionsCovered\n10,0,0,0\n")
        self.assertEqual(result, {"line_total": 10, "line_covered": 0, "branch_total": 0, "branch_covered": 0})

    def test_invalid_coverage_counters_are_rejected(self):
        header = "LinesTotal,LinesCovered,ConditionsTotal,ConditionsCovered\n"
        for row in ("10,11,2,1\n", "10,3,2,3\n", "-1,0,0,0\n", "10,,2,0\n", "10,1,2,1\n10,1,2,1\n"):
            with self.subTest(row=row), self.assertRaises(evaluate.EvidenceError):
                evaluate.parse_coverage_csv(header + row)


class EvaluatorLifecycleTests(unittest.TestCase):
    def setUp(self):
        self.temporary = tempfile.TemporaryDirectory()
        self.addCleanup(self.temporary.cleanup)
        self.root = Path(self.temporary.name)
        self.fixed = self.root / "fixed"
        self.buggy = self.root / "buggy"
        for directory, revision in ((self.fixed, "1f"), (self.buggy, "1b")):
            directory.mkdir()
            (directory / ".defects4j.config").write_text(f"pid=Lang\nvid={revision}\n", encoding="utf-8")
        self.suite = self.root / "provided.tar.bz2"
        with tarfile.open(self.suite, "w:bz2") as archive:
            data = b"public class GeneratedTest { @org.junit.Test public void example() {} }"
            member = tarfile.TarInfo("GeneratedTest.java")
            member.size = len(data)
            archive.addfile(member, io.BytesIO(data))
        self.config = evaluate.EvaluationConfig(
            project="Lang", bug_id=1, generator="fscs-art", seed=2026, budget=30,
            suite=self.suite, buggy_worktree=self.buggy, fixed_worktree=self.fixed,
            output=self.root / "run", d4j="fixture-defects4j", test_count=1,
        )
        self.test_failures = {"fixed-1": 0, "fixed-2": 0, "buggy": 1}
        self.failed_command = None
        self.missing_result = None
        self.coverage_row = "12,7,6,2"
        self.calls = []

    def fake_command(self, command, cwd, directory, timeout):
        """Simulate command outputs at the real expected artifact locations."""
        self.calls.append(command)
        directory.mkdir()
        stage = {"command": command, "cwd": str(cwd), "exit_code": 0,
                 "timed_out": False, "duration_seconds": 0.001}
        output = ""
        if directory.name == "java-version":
            output = 'openjdk version "11.0.28"\n'
        elif "export" in command:
            Path(command[command.index("-o") + 1]).write_text("example.Focal\n", encoding="utf-8")
        elif "test" in command:
            checkout = Path(command[command.index("-w") + 1])
            count = self.test_failures[directory.name]
            output = f"Failing tests: {count}\n"
            if self.missing_result != directory.name:
                (checkout / "failing_tests").write_text(
                    "--- GeneratedTest::example\njava.lang.AssertionError\n" if count else "", encoding="utf-8",
                )
            (checkout / "all_tests").write_text("GeneratedTest\n", encoding="utf-8")
        elif "coverage" in command:
            checkout = Path(command[command.index("-w") + 1])
            (checkout / "failing_tests").write_text("", encoding="utf-8")
            (checkout / "summary.csv").write_text(
                "LinesTotal,LinesCovered,ConditionsTotal,ConditionsCovered\n" + self.coverage_row + "\n", encoding="utf-8",
            )
            (checkout / "coverage.xml").write_text("<coverage/>\n", encoding="utf-8")
        if directory.name == self.failed_command:
            stage["exit_code"] = 1
            output += "Cannot compile extracted test suite!\n" if "test" in command else "Coverage failed\n"
        (directory / "command.log").write_text(output, encoding="utf-8")
        (directory / "command.json").write_text(json.dumps(stage), encoding="utf-8")
        return stage

    def run_fixture(self):
        with patch.object(evaluate, "run_command", side_effect=self.fake_command):
            return evaluate.evaluate_run(self.config)

    def test_complete_fault_detection_has_raw_evidence_and_version(self):
        result = self.run_fixture()
        self.assertEqual(result["status"], "complete")
        self.assertTrue(result["fault_detected"])
        self.assertEqual(result["compile_status"], "passed")
        self.assertEqual(result["line_covered"], 7)
        self.assertEqual(result["branch_covered"], 2)
        self.assertIn('"11.0.28"', result["versions"]["java"])
        self.assertEqual(result["fixed_validation"], "passed_twice")
        self.assertTrue((self.config.output / "buggy" / "failing_tests").is_file())
        self.assertTrue((self.config.output / "coverage" / "coverage.xml").is_file())
        test_commands = [command for command in self.calls if "test" in command]
        self.assertEqual([command[command.index("-w") + 1] for command in test_commands],
                         [str(self.fixed), str(self.fixed), str(self.buggy)])

    def test_zero_detection_is_complete_and_false(self):
        self.test_failures["buggy"] = 0
        result = self.run_fixture()
        self.assertEqual(result["status"], "complete")
        self.assertIs(result["fault_detected"], False)

    def test_no_run_directory_may_be_overwritten(self):
        self.run_fixture()
        original = (self.config.output / "record.json").read_bytes()
        with self.assertRaises(FileExistsError):
            self.run_fixture()
        self.assertEqual(original, (self.config.output / "record.json").read_bytes())

    def test_second_fixed_failure_is_unstable_and_not_a_detection(self):
        self.test_failures["fixed-2"] = 1
        result = self.run_fixture()
        self.assertEqual(result["status"], "invalid")
        self.assertEqual(result["fixed_validation"], "unstable")
        self.assertIsNone(result["fault_detected"])
        self.assertNotIn("buggy", result["stages"])

    def test_buggy_infrastructure_error_cannot_be_counted_as_a_detection(self):
        self.failed_command = "buggy"
        result = self.run_fixture()
        self.assertEqual(result["status"], "failed")
        self.assertEqual(result["compile_status"], "failed")
        self.assertIsNone(result["fault_detected"])

    def test_stale_result_cannot_make_missing_evidence_pass(self):
        (self.fixed / "failing_tests").write_text("", encoding="utf-8")
        self.missing_result = "fixed-1"
        result = self.run_fixture()
        self.assertEqual(result["status"], "failed")
        self.assertIn("did not produce failing_tests", result["error"])
        self.assertIsNone(result["fault_detected"])

    def test_coverage_error_preserves_detection_but_not_fabricated_coverage(self):
        self.failed_command = "coverage"
        result = self.run_fixture()
        self.assertEqual(result["status"], "partial")
        self.assertTrue(result["fault_detected"])
        for field in ("line_covered", "line_total", "branch_covered", "branch_total"):
            self.assertIsNone(result[field])

    def test_inconsistent_coverage_is_partial(self):
        self.coverage_row = "3,7,6,2"
        result = self.run_fixture()
        self.assertEqual(result["status"], "partial")
        self.assertIsNone(result["line_covered"])

    def test_wrong_revision_is_rejected_before_any_command(self):
        (self.fixed / ".defects4j.config").write_text("pid=Lang\nvid=2f\n", encoding="utf-8")
        result = self.run_fixture()
        self.assertEqual(result["status"], "failed")
        self.assertEqual(self.calls, [])

    def test_unsafe_archive_is_rejected_before_any_command(self):
        with tarfile.open(self.suite, "w:bz2") as archive:
            archive.addfile(tarfile.TarInfo("../Escaped.java"), io.BytesIO(b""))
        result = self.run_fixture()
        self.assertEqual(result["status"], "failed")
        self.assertIn("Unsafe source archive", result["error"])
        self.assertEqual(self.calls, [])


class CommandExecutionTests(unittest.TestCase):
    def test_real_subprocess_preserves_nonzero_exit_and_output(self):
        with tempfile.TemporaryDirectory() as temporary:
            root = Path(temporary)
            result = evaluate.run_command(
                [sys.executable, "-c", "print('fixture output'); raise SystemExit(7)"],
                root, root / "command", 10,
            )
            self.assertEqual(result["exit_code"], 7)
            self.assertFalse(result["timed_out"])
            self.assertIn("fixture output", (root / "command" / "command.log").read_text())

    def test_timeout_is_recorded_and_process_terminated(self):
        with tempfile.TemporaryDirectory() as temporary:
            root = Path(temporary)
            result = evaluate.run_command(
                [sys.executable, "-c", "import time; time.sleep(10)"],
                root, root / "timeout", 0.05,
            )
            self.assertTrue(result["timed_out"])
            self.assertIsNotNone(result["exit_code"])


if __name__ == "__main__":
    unittest.main()
