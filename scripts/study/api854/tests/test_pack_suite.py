"""Archive handoff tests: source fidelity, reproducibility and extraction boundary."""
import io
import os
from pathlib import Path
import tarfile
import tempfile
import unittest

from scripts.study.api854.common import sha256
from scripts.study.api854.pack_suite import pack_suite
from scripts.study.api854.evaluate_worker import validate_lineage


class PackagingTests(unittest.TestCase):
    def setUp(self):
        self.temp = tempfile.TemporaryDirectory()
        self.addCleanup(self.temp.cleanup)
        self.root = Path(self.temp.name)
        self.sources = self.root / "sources"
        self.sources.mkdir()

    def source(self, name="ExampleTest.java", data=b"public class ExampleTest {}\r\n"):
        path = self.sources / name
        path.parent.mkdir(parents=True, exist_ok=True)
        path.write_bytes(data)
        return path

    def test_handoff_preserves_bytes_and_evaluator_accepts_hash_bound_suite(self):
        data = b'// package wrong;\npackage sample;\npublic class ExampleTest { String s="package fake;"; }\r\n'
        self.source("sample/ExampleTest.java", data)
        manifest = pack_suite(self.sources, self.root / "out", 1)
        suite = self.root / "out/suite.tar.bz2"
        with tarfile.open(suite, "r:bz2") as tar:
            self.assertEqual(tar.getnames(), ["sample/ExampleTest.java"])
            self.assertEqual(tar.extractfile(tar.getmembers()[0]).read(), data)
        job = {"schema_version": 1, "run_id": "fixture", "project": "Lang", "bug_id": 4,
               "approach": "kku-claude", "protocol_hash": "a" * 64, "repeat_index": 1,
               "attempt_id": "evaluate-attempt"}
        lineage = {"job": {**job, "attempt_id": "generate-attempt"}, "observed_outcome": "generated",
                   "suite_sha256": manifest["suite_sha256"], "test_count": 1,
                   "fixed_source_sha256": {"Example.java": "b" * 64}}
        self.assertEqual(validate_lineage(job, lineage, suite, {"test_method_cap": 30}), 1)
        self.assertIsNone(manifest["executed_test_count"])
        suite.write_bytes(suite.read_bytes() + b"changed")
        with self.assertRaisesRegex(ValueError, "hash differs"):
            validate_lineage(job, lineage, suite, {"test_method_cap": 30})

    def test_reproducible_across_mtime_permissions_and_source_location(self):
        first = self.source()
        one = pack_suite(self.sources, self.root / "one", 1)
        os.utime(first, (123456789, 123456789))
        first.chmod(0o600)
        other = self.root / "another"
        other.mkdir()
        (other / first.name).write_bytes(first.read_bytes())
        two = pack_suite(other, self.root / "two", 1)
        self.assertEqual(one["suite_sha256"], two["suite_sha256"])

    def test_archive_contains_only_sorted_files_with_normalized_metadata(self):
        self.source("ZTest.java")
        self.source("ATest.java", b"public class ATest {}")
        pack_suite(self.sources, self.root / "out", 2)
        with tarfile.open(self.root / "out/suite.tar.bz2", "r:bz2") as tar:
            self.assertEqual(tar.getnames(), ["ATest.java", "ZTest.java"])
            for member in tar:
                self.assertTrue(member.isfile())
                self.assertEqual((member.uid, member.gid, member.mtime, member.mode), (0, 0, 0, 0o644))

    def test_non_java_empty_bad_utf8_and_wrong_package_are_rejected(self):
        for name, data in [("notes.txt", b"notes"), ("Test.java", b" "),
                           ("Test.java", b"\xff"), ("Test.java", b"package other; class Test {}")]:
            with self.subTest(name=name, data=data):
                path = self.source(name, data)
                with self.assertRaises((ValueError, UnicodeError)):
                    pack_suite(self.sources, self.root / "out", 1)
                self.assertFalse((self.root / "out").exists())
                path.unlink()

    def test_links_cannot_import_sources_outside_tree(self):
        outside = self.root / "Outside.java"
        outside.write_bytes(b"class Outside {}")
        try:
            (self.sources / "Outside.java").symlink_to(outside)
        except OSError:
            self.skipTest("Source-link fixture requires symlink support")
        with self.assertRaisesRegex(ValueError, "links"):
            pack_suite(self.sources, self.root / "out", 1)

    def test_cap_and_exclusive_output_prevent_truncation_or_overwrite(self):
        self.source()
        for count in (0, True, 31):
            with self.assertRaises(ValueError):
                pack_suite(self.sources, self.root / "out", count)
        pack_suite(self.sources, self.root / "out", 1)
        before = sha256(self.root / "out/suite.tar.bz2")
        with self.assertRaises(FileExistsError):
            pack_suite(self.sources, self.root / "out", 1)
        self.assertEqual(sha256(self.root / "out/suite.tar.bz2"), before)


if __name__ == "__main__":
    unittest.main()
