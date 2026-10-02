import json
from pathlib import Path
import tempfile
import unittest

from scripts.study.api854.context_export import export_context


class ContextTests(unittest.TestCase):
    def setUp(self):
        self.temp = tempfile.TemporaryDirectory()
        self.root = Path(self.temp.name)
        self.worktree = self.root / "fixed"
        self.worktree.mkdir()
        (self.worktree / ".defects4j.config").write_text("pid=Lang\nvid=2f\n", encoding="utf-8")
        (self.worktree / "Example.java").write_text("class Example {}\n", encoding="utf-8")
        (self.worktree / "pom.xml").write_text("<project/>\n", encoding="utf-8")

    def tearDown(self):
        self.temp.cleanup()

    def export(self, paths=None):
        return export_context(self.worktree, "Lang", 2, paths or ["Example.java", "pom.xml"],
                              self.root / "export", policy_id="mock-selection-v1")

    def test_non_bug_one_context_hash_and_immutability(self):
        result = self.export()
        self.assertEqual(result["revision"], "2f")
        self.assertEqual(len(result["source_hash"]), 64)
        self.assertFalse(result["contains_execution_logs"])
        self.assertEqual(len(result["source_files"]), 2)
        self.assertEqual((self.root / "export/fixed-source/Example.java").read_bytes(),
                         (self.worktree / "Example.java").read_bytes())
        with self.assertRaises(FileExistsError):
            self.export()

    def test_buggy_revision_and_identity_mismatch_rejected(self):
        for vid in ("2b", "1f"):
            (self.worktree / ".defects4j.config").write_text(f"pid=Lang\nvid={vid}\n", encoding="utf-8")
            with self.assertRaises(ValueError):
                self.export()

    def test_logs_hidden_paths_and_traversal_rejected(self):
        for path in ("compile.log", "../Example.java", ".local/Example.java", "results/Example.java"):
            with self.subTest(path=path), self.assertRaises(ValueError):
                self.export([path])

    def test_requires_java_source_and_rejects_duplicate_selection(self):
        for paths in (["pom.xml"], ["Example.java", "Example.java"]):
            with self.assertRaises(ValueError):
                self.export(paths)
