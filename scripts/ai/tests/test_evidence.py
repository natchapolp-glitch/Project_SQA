import argparse
import importlib.util
import json
from pathlib import Path
import tarfile
import tempfile
import unittest


SPEC = importlib.util.spec_from_file_location("ai_evidence", Path(__file__).resolve().parents[1] / "evidence.py")
evidence = importlib.util.module_from_spec(SPEC)
SPEC.loader.exec_module(evidence)


class EvidenceTests(unittest.TestCase):
    def setUp(self):
        self.temp = tempfile.TemporaryDirectory()
        self.root = Path(self.temp.name)
        self.worktree = self.root / "fixed"
        self.worktree.mkdir()
        (self.worktree / ".defects4j.config").write_text("pid=Lang\nvid=1f\n", encoding="utf-8")
        source = self.worktree / "src/main/java/org/example/Example.java"
        source.parent.mkdir(parents=True)
        source.write_text("package org.example; public class Example {}\n", encoding="utf-8")
        (self.worktree / "pom.xml").write_text("<project/>\n", encoding="utf-8")
        self.classes = self.root / "classes.txt"
        self.classes.write_text("org.example.Example\n", encoding="utf-8")
        self.context = self.root / "context"
        self.prepare = argparse.Namespace(project="Lang", bug_id=1, fixed_worktree=self.worktree,
                                          classes_file=self.classes, output=self.context)

    def tearDown(self):
        self.temp.cleanup()

    def ingest_args(self, java=True):
        evidence.prepare(self.prepare)
        tests = self.root / "tests"
        tests.mkdir()
        source = "package org.example; public class ExampleTest {}\n"
        if java:
            target = tests / "org/example/ExampleTest.java"
            target.parent.mkdir(parents=True)
            target.write_text(source, encoding="utf-8")
        response = self.root / "response.txt"
        response.write_text("```java\n" + source + "```\n" if java else "Insufficient context", encoding="utf-8")
        return argparse.Namespace(tool="claude", model="operator-recorded-test-model", prompt=self.context / "prompt.md",
                                  response=response, tests_dir=tests, output=self.root / "evidence", metadata_file=None,
                                  seed=7, iteration=1)

    def test_reject_buggy_context(self):
        (self.worktree / ".defects4j.config").write_text("pid=Lang\nvid=1b\n", encoding="utf-8")
        with self.assertRaisesRegex(evidence.EvidenceError, "fixed revision"):
            evidence.prepare(self.prepare)

    def test_context_contains_only_selected_source_and_build(self):
        (self.worktree / "bug.patch").write_text("SECRET BUG PATCH", encoding="utf-8")
        (self.worktree / "triggering_tests").write_text("SECRET TRIGGER", encoding="utf-8")
        evidence.prepare(self.prepare)
        prompt = (self.context / "prompt.md").read_text(encoding="utf-8")
        self.assertIn("public class Example", prompt)
        self.assertNotIn("SECRET", prompt)
        self.assertFalse((self.context / "context/bug.patch").exists())

    def test_package_paths_and_missing_provenance(self):
        args = self.ingest_args()
        result = evidence.ingest(args)
        self.assertEqual(result["external_suite"], "Lang-1f-claude.7.tar.bz2")
        with tarfile.open(args.output / result["external_suite"]) as archive:
            self.assertEqual(archive.getnames(), ["org/example/ExampleTest.java"])
            self.assertTrue(all(member.isfile() for member in archive.getmembers()))
        metadata = json.loads((args.output / "metadata.json").read_text(encoding="utf-8"))
        self.assertIsNone(metadata["generated_at"])
        self.assertEqual(metadata["provenance_status"], "incomplete")
        self.assertEqual(metadata["compile_status"], "not_run")
        self.assertIsNone(metadata["fault_detected"])

    def test_reject_modified_context(self):
        args = self.ingest_args()
        (self.context / "context/pom.xml").write_text("changed", encoding="utf-8")
        with self.assertRaisesRegex(evidence.EvidenceError, "Context file has changed"):
            evidence.ingest(args)

    def test_eligible_api_inventory_is_in_prompt_and_tampering_is_rejected(self):
        targets = self.root / 'targets.json'
        targets.write_text(json.dumps({'targets': [{'class': 'org.example.Example',
            'constructor_types': '', 'method': 'value', 'parameter_types': '', 'dimensions': 3}]}))
        self.prepare.targets_file = targets
        args = self.ingest_args()
        self.assertIn('Eligible shared API declarations', args.prompt.read_text())
        (self.context / 'target-inventory.json').write_text('{}')
        with self.assertRaisesRegex(evidence.EvidenceError, 'target inventory has changed'):
            evidence.ingest(args)

    def test_inner_class_context_resolves_top_level_source(self):
        self.classes.write_text('org.example.Example$Nested\n')
        evidence.prepare(self.prepare)
        self.assertIn('public class Example', (self.context / 'prompt.md').read_text())

    def test_reject_source_not_present_in_response(self):
        args = self.ingest_args()
        args.response.write_text("No Java returned", encoding="utf-8")
        with self.assertRaisesRegex(evidence.EvidenceError, "manual_edits"):
            evidence.ingest(args)

    def test_preserve_no_source_failure_without_archive(self):
        args = self.ingest_args(java=False)
        result = evidence.ingest(args)
        self.assertIsNone(result["external_suite"])
        metadata = json.loads((args.output / "metadata.json").read_text(encoding="utf-8"))
        self.assertEqual(metadata["generation_status"], "no_java_sources")

    def test_validate_provider_timestamp(self):
        args = self.ingest_args()
        metadata = self.root / "operator.json"
        metadata.write_text(json.dumps({"generated_at": "2026-09-27T18:00:00", "parameters": {}}), encoding="utf-8")
        args.metadata_file = metadata
        with self.assertRaisesRegex(evidence.EvidenceError, "timezone"):
            evidence.ingest(args)

    def test_read_only_immutable_output(self):
        evidence.prepare(self.prepare)
        with self.assertRaisesRegex(evidence.EvidenceError, "already exists"):
            evidence.prepare(self.prepare)

    def test_reject_symlink_packaging(self):
        args = self.ingest_args()
        target = args.tests_dir / "LinkedTest.java"
        try:
            target.symlink_to(self.worktree / "pom.xml")
        except OSError:
            self.skipTest("Creating symlinks is unavailable in this environment")
        with self.assertRaisesRegex(evidence.EvidenceError, "contains a link"):
            evidence.ingest(args)


if __name__ == "__main__":
    unittest.main()
