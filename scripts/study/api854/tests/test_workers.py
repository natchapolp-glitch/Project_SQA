"""Contract/lifecycle fixtures in temporary directories, never primary study evidence."""
from concurrent.futures import ThreadPoolExecutor
import io
import json
from pathlib import Path
import tarfile
import tempfile
import sys
import unittest
from unittest.mock import patch

from scripts.study.api854 import common, configuration, adapters, algorithm_worker, evaluate_worker, worker


class JobBoundaryTests(unittest.TestCase):
    def setUp(self):
        self.temp = tempfile.TemporaryDirectory()
        self.addCleanup(self.temp.cleanup)
        self.root = Path(self.temp.name)
        self.protocol = self.root / "protocol.json"
        common.write_json(self.protocol, configuration.proposal())
        self.job = {"schema_version": 1, "run_id": "fixture", "project": "Lang", "bug_id": 4,
                    "approach": "cmaes", "protocol_hash": common.sha256(self.protocol), "repeat_index": 1, "attempt_id": "attempt-1"}
        self.job_file = self.root / "job.json"

    def load(self):
        self.job_file.write_text(json.dumps(self.job), encoding="utf-8")
        return common.load_job(self.job_file, self.protocol)

    def test_job_and_exact_protocol_bytes_are_bound(self):
        job, _ = self.load()
        self.assertEqual(job, self.job)
        with self.protocol.open("a") as stream:
            stream.write("\n")
        with self.assertRaisesRegex(ValueError, "hash differs"):
            self.load()

    def test_paths_secrets_boolean_ids_and_second_primary_repeat_are_rejected(self):
        for change in ({"attempt_id": "../outside"}, {"run_id": "CON"}, {"bug_id": True},
                       {"repeat_index": 2}, {"schema_version": True}, {"api_key": "fixture-secret"}, {"approach": "shell"}):
            with self.subTest(change=change):
                original = self.job.copy()
                self.job.update(change)
                with self.assertRaises(ValueError):
                    self.load()
                self.job = original

    def test_source_snapshot_retains_exact_frozen_bytes(self):
        expected = common.implementation_hashes()
        common.snapshot_implementation(self.root, expected)
        for name, digest in expected.items():
            self.assertEqual(common.sha256(self.root / "implementation" / name), digest)
        self.assertEqual(common.read_json(self.root / "implementation/source-hashes.json"), expected)
        with self.assertRaises(FileExistsError):
            common.snapshot_implementation(self.root, expected)

    def test_changed_sources_cannot_be_claimed_as_frozen_snapshot(self):
        expected = common.implementation_hashes()
        with patch.object(common, "implementation_hashes", return_value={"other.py": "0" * 64}):
            with self.assertRaisesRegex(ValueError, "Implementation changed"):
                common.snapshot_implementation(self.root, expected)
        self.assertFalse((self.root / "implementation").exists())

    def test_changed_implementation_cannot_reuse_frozen_protocol(self):
        with patch.object(common, "implementation_hashes", return_value={"changed.py": "0" * 64}):
            with self.assertRaisesRegex(ValueError, "implementation hashes"):
                self.load()

    def test_team_protocol_can_pin_additional_owner_modules(self):
        protocol = configuration.proposal()
        protocol["source_sha256"]["scripts/study/api854/queue_server.py"] = "a" * 64
        self.protocol.write_text(json.dumps(protocol), encoding="utf-8")
        self.job["protocol_hash"] = common.sha256(self.protocol)
        self.load()

    def test_only_one_concurrent_stage_creation_succeeds(self):
        def attempt(_):
            try:
                common.start_attempt(self.root / "results", self.job, "generation")
                return True
            except FileExistsError:
                return False
        with ThreadPoolExecutor(max_workers=4) as pool:
            self.assertEqual(sum(pool.map(attempt, range(4))), 1)

    @unittest.skipUnless(sys.platform == "win32", "Windows extended-path spellings")
    def test_resolved_extended_paths_keep_the_same_containment_boundary(self):
        root = self.root.resolve()
        target = root / "child"
        extended_root = Path("\\\\?\\" + str(root))
        extended_target = Path("\\\\?\\" + str(target))
        unc_root = Path("\\\\fixture-server\\share\\root")
        unc_target = Path("\\\\?\\UNC\\fixture-server\\share\\root\\child")
        for first, second, expected in ((root, extended_target, target),
                                        (extended_root, target, target),
                                        (unc_root, unc_target, unc_root / "child")):
            with self.subTest(root=str(first)), patch.object(Path, "resolve", side_effect=[first, second]):
                self.assertEqual(common.contained(root, "child"), expected)

    @unittest.skipUnless(sys.platform == "win32", "Windows extended-path spellings")
    def test_extended_path_normalization_still_rejects_outside_and_root_itself(self):
        root = self.root.resolve()
        for target in (root, root.parent / "outside"):
            extended = Path("\\\\?\\" + str(target))
            with self.subTest(target=str(target)), patch.object(Path, "resolve", side_effect=[root, extended]):
                with self.assertRaisesRegex(ValueError, "Path escapes root"):
                    common.contained(root, "child")

    def test_worktrees_are_isolated_and_interrupted_trees_cannot_be_reused(self):
        first = worker.new_worktrees(self.root / "trees", self.job, "generation")
        with self.assertRaises(FileExistsError):
            worker.new_worktrees(self.root / "trees", self.job, "generation")
        other = worker.new_worktrees(self.root / "trees", {**self.job, "attempt_id": "attempt-2"}, "generation")
        self.assertNotEqual(first, other)

    def test_missing_environment_never_counts_as_an_evaluation(self):
        with patch.object(algorithm_worker, "inspect_environment", return_value={"ready": False, "issues": ["Linux absent"]}):
            result = algorithm_worker.execute(self.job, configuration.proposal(), self.root / "results", self.root / "trees", "d4j")
        self.assertEqual(result["observed_outcome"], "preflight_failed")
        self.assertFalse(result["evaluation_attempted"])
        self.assertFalse(result["queue_published"])

    def test_allocation_is_complete_without_claiming_runtime_support(self):
        rows = adapters.allocation_rows(common.ROOT / "experiments/configs/api854-20261003/ownership.json")
        self.assertEqual(sum(row["owner"] == "beam" for row in rows), 285)
        self.assertEqual(len({(row["project"], row["bug_id"]) for row in rows}), 854)

    def test_algorithm_produces_real_archive_from_fixed_fixture_observations(self):
        def adapter(d4j, job, trees, output, timeout):
            output.mkdir()
            fixed = trees / "fixed"
            (fixed / "src/example").mkdir(parents=True)
            (fixed / "src/example/Focal.java").write_text("package example; public class Focal {}")
            (output / "dir.src.classes.txt").write_text("src")
            classes = output / "classes.modified.txt"
            classes.write_text("example.Focal\n")
            targets = [{"class": "example.Focal", "constructor_types": "", "method": "value", "parameter_types": "int", "dimensions": 3}]
            return {"fixed_worktree": str(fixed), "classes_file": str(classes), "classpath": "fixture",
                    "targets_sha256": "c" * 64, "source_sha256": {"fixture": "d" * 64}}, targets
        protocol = {**configuration.proposal(), "budget": 3}
        with patch.object(algorithm_worker, "inspect_environment", return_value={"ready": True}), \
             patch.object(algorithm_worker, "prepare_adapter", side_effect=adapter), \
             patch("generate.observe", return_value={"status": "ok", "outcome": "value:fixed"}):
            result = algorithm_worker.execute(self.job, protocol, self.root / "results", self.root / "trees", "d4j")
        self.assertEqual(result["observed_outcome"], "generated")
        self.assertEqual(result["test_count"], 3)
        archive = Path(result["generation"]["suite"])
        self.assertEqual(common.sha256(archive), result["suite_sha256"])
        with tarfile.open(archive, "r:bz2") as source:
            self.assertEqual(source.getnames(), ["GeneratedStudyTest.java"])
        self.assertEqual(result["semantic_validity"], "pending_review")

    def test_no_shared_targets_cannot_be_reported_as_supported(self):
        output = self.root / "setup"
        output.mkdir()
        with patch.object(adapters, "query", return_value="4\n"), \
             patch.object(adapters, "prepare", side_effect=RuntimeError("No shared supported declaration signatures")):
            with self.assertRaises(RuntimeError):
                adapters.prepare_adapter("d4j", self.job, self.root / "trees", output, 30)

    def test_installed_inventory_must_match_not_just_count(self):
        rows = [{"project": "Lang", "bug_id": 4}]
        with patch.object(adapters, "query", side_effect=["Lang", "5"]):
            with self.assertRaisesRegex(ValueError, "mismatch"):
                adapters.verify_installed_inventory("d4j", rows)


class EvaluationBoundaryTests(unittest.TestCase):
    def setUp(self):
        self.temp = tempfile.TemporaryDirectory()
        self.addCleanup(self.temp.cleanup)
        self.root = Path(self.temp.name)
        self.suite = self.root / "suite.tar.bz2"
        with tarfile.open(self.suite, "w:bz2") as archive:
            source = b"public class ExampleTest { @org.junit.Test public void value() { org.junit.Assert.assertEquals(1, 1); } }"
            member = tarfile.TarInfo("ExampleTest.java")
            member.size = len(source)
            archive.addfile(member, io.BytesIO(source))
        self.job = {"schema_version": 1, "run_id": "fixture", "project": "Lang", "bug_id": 4,
                    "approach": "kku-claude", "protocol_hash": "a" * 64, "repeat_index": 1, "attempt_id": "attempt-1"}
        self.lineage = {"job": self.job.copy(), "observed_outcome": "generated", "suite_sha256": common.sha256(self.suite),
                        "test_count": 1, "fixed_source_sha256": {"src/Focal.java": "b" * 64}}
        self.protocol = configuration.proposal()

    def test_changed_suite_and_wrong_job_are_rejected_before_execution(self):
        self.lineage["suite_sha256"] = "0" * 64
        with self.assertRaisesRegex(ValueError, "Suite hash"):
            evaluate_worker.validate_lineage(self.job, self.lineage, self.suite, self.protocol)
        self.lineage["job"]["bug_id"] = 5
        with self.assertRaisesRegex(ValueError, "exact job"):
            evaluate_worker.validate_lineage(self.job, self.lineage, self.suite, self.protocol)

    def test_suite_cap_and_missing_source_provenance_are_rejected(self):
        self.lineage["test_count"] = 31
        with self.assertRaisesRegex(ValueError, "cap"):
            evaluate_worker.validate_lineage(self.job, self.lineage, self.suite, self.protocol)

    def test_queue_stage_attempt_changes_keep_original_generation_lineage(self):
        evaluation_job = {**self.job, "attempt_id": "evaluation-attempt-2"}
        self.assertEqual(evaluate_worker.validate_lineage(evaluation_job, self.lineage, self.suite, self.protocol), 1)
        self.assertEqual(self.lineage["job"]["attempt_id"], "attempt-1")
        self.lineage["test_count"] = 1
        self.lineage["fixed_source_sha256"] = {}
        with self.assertRaisesRegex(ValueError, "fixed source"):
            evaluate_worker.validate_lineage(self.job, self.lineage, self.suite, self.protocol)

    def test_evaluation_not_usable_without_semantic_review(self):
        validity = evaluate_worker.review_validity(None, "a" * 64, {}, {"status": "complete"})
        self.assertFalse(validity["usable"])
        self.assertEqual(validity["status"], "pending_review")

    def test_review_needs_retained_evidence_and_actual_target_checks(self):
        evidence = self.root / "runtime-proof.txt"
        evidence.write_text("fixture proof, not experiment evidence", encoding="utf-8")
        ref = {"path": evidence.name, "sha256": common.sha256(evidence)}
        review = {"suite_sha256": "a" * 64, "fixed_source_sha256": {}, "reviewer": "fixture",
                  "target_execution_evidence": [ref], "fixture_oracle_review": "fixture",
                  "weak_oracles": "none in fixture", "verdict": "valid",
                  "stage_counts": {s: {"executed": 1, "skipped": 0, "target_checks": 1, "evidence": ref}
                                   for s in ("fixed-1", "fixed-2", "buggy", "coverage")}}
        path = self.root / "review.json"
        common.write_json(path, review)
        self.assertTrue(evaluate_worker.review_validity(path, "a" * 64, {}, {"status": "complete"}, self.root)["usable"])
        review["stage_counts"]["fixed-1"]["target_checks"] = 0
        path.write_text(json.dumps(review))
        with self.assertRaises(ValueError):
            evaluate_worker.review_validity(path, "a" * 64, {}, {"status": "complete"}, self.root)
        review["stage_counts"]["fixed-1"]["target_checks"] = 1
        ref["path"] = "../outside.txt"
        path.write_text(json.dumps(review))
        with self.assertRaises(ValueError):
            evaluate_worker.review_validity(path, "a" * 64, {}, {"status": "complete"}, self.root)

    def test_coverage_failure_preserves_measured_detection_and_null_coverage(self):
        measurement = {"status": "partial", "suite_sha256": self.lineage["suite_sha256"], "failed_stage": "coverage",
                       "fault_detected": True, "line_covered": None, "stages": {"coverage": {"timed_out": False}}}
        def setup(d4j, job, trees, output, timeout):
            output.mkdir()
            return {"f": trees / "f", "b": trees / "b"}, output / "classes.txt", self.lineage["fixed_source_sha256"]
        with patch.object(evaluate_worker, "inspect_environment", return_value={"ready": True}), \
             patch.object(evaluate_worker, "prepare_evaluation", side_effect=setup), \
             patch.object(evaluate_worker, "evaluate_run", return_value=measurement):
            result = evaluate_worker.execute(self.job, self.protocol, self.root / "results", self.root / "trees", "d4j", self.suite, self.lineage)
        self.assertEqual(result["observed_outcome"], "coverage_failed")
        self.assertTrue(result["measurement"]["fault_detected"])
        self.assertIsNone(result["measurement"]["line_covered"])
        self.assertFalse(result["usable"])

    def test_fixed_failure_timeout_and_compile_failure_are_distinct(self):
        cases = [({"status": "invalid", "fixed_validation": "unstable"}, "fixed_failed"),
                 ({"status": "failed", "compile_status": "failed"}, "compile_failed"),
                 ({"status": "failed", "failed_stage": "buggy", "stages": {"buggy": {"timed_out": True}}}, "timeout")]
        for record, expected in cases:
            with self.subTest(expected=expected):
                self.assertEqual(worker.normalize_evaluation(record), expected)

    def test_evaluator_preflight_failure_is_retained_without_missing_hash_masking(self):
        measurement = {"status": "failed", "failed_stage": "preflight", "error": "PermissionError: fixture",
                       "fault_detected": None, "stages": {}}
        def setup(d4j, job, trees, output, timeout):
            output.mkdir()
            return {"f": trees / "f", "b": trees / "b"}, output / "classes.txt", self.lineage["fixed_source_sha256"]
        with patch.object(evaluate_worker, "inspect_environment", return_value={"ready": True}), \
             patch.object(evaluate_worker, "prepare_evaluation", side_effect=setup), \
             patch.object(evaluate_worker, "evaluate_run", return_value=measurement):
            result = evaluate_worker.execute(self.job, self.protocol, self.root / "results", self.root / "trees", "d4j", self.suite, self.lineage)
        self.assertEqual(result["measurement"]["error"], "PermissionError: fixture")
        self.assertEqual(result["observed_outcome"], "environment_failed")
        self.assertFalse(result["usable"])

    def test_fixed_sources_reject_traversal_and_missing_classes(self):
        with self.assertRaises(ValueError):
            worker.fixed_sources(self.root, ["example.Focal"], "../other")
        with self.assertRaises(ValueError):
            worker.fixed_sources(self.root, ["example.Focal"], "src")


if __name__ == "__main__":
    unittest.main()
