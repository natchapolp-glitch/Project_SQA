import hashlib
from pathlib import Path
import tarfile
import tempfile
import unittest

from scripts.study.api854.common import write_json, read_json
from scripts.study.api854.suite_resolver import BeamSuiteResolver


class ResolverTests(unittest.TestCase):
    def setUp(self):
        self.temp = tempfile.TemporaryDirectory()
        self.addCleanup(self.temp.cleanup)
        self.root = Path(self.temp.name)
        self.job = {"schema_version": 1, "run_id": "fixture", "protocol_hash": "a" * 64,
                    "project": "Lang", "bug_id": 4, "approach": "kku-claude", "repeat_index": 1,
                    "attempt_id": "generate-fixture"}
        self.resolver = BeamSuiteResolver(self.job, {"suite_packaging": "beam-java-suite-v1",
            "compatibility_policy": "none", "test_method_cap": 30}, {"src/Target.java": "b" * 64}, "c" * 64)

    def result(self, *sources):
        blocks = []
        for index, data in enumerate(sources, 1):
            name = f"source-block-{index:03d}.java"
            (self.root / name).write_bytes(data)
            blocks.append({"path": name, "sha256": hashlib.sha256(data).hexdigest()})
        result = {**self.job, "source_hash": "c" * 64, "generation_outcome": "response_received",
                  "artifact_path": str(self.root), "source_blocks": blocks}
        write_json(self.root / "generation-result.json", result)
        return result

    def test_package_filename_fences_and_bytes_bind_to_lineage(self):
        data = b'package example;\r\nimport org.junit.Test;\r\npublic class NamedTest {\r\n@Test public void testTarget() {}\r\n}\r\n'
        result = self.result(data)
        suite = self.resolver(result)
        with tarfile.open(suite, "r:bz2") as archive:
            self.assertEqual(archive.getnames(), ["example/NamedTest.java"])
            self.assertEqual(archive.extractfile(archive.getmembers()[0]).read(), data)
        lineage = read_json(self.root / "beam-suite/generation-lineage.json")
        self.assertEqual(lineage["test_count"], 1)
        self.assertIsNone(lineage["executed_test_count"])
        self.assertEqual(lineage["job"], self.job)
        self.assertEqual(self.resolver(result), suite)

    def test_comments_strings_and_nested_helpers_do_not_invent_tests_or_classes(self):
        data = b'''// public class Wrong {} @Test
public class NamedTest {
  String fake = "public class Fake { @Test }";
  @org.junit.Test(timeout=1000) public void target() {}
  public static class Helper { char brace = '}'; }
}'''
        self.resolver(self.result(data))
        self.assertEqual(read_json(self.root / "beam-suite/generation-lineage.json")["test_count"], 1)

    def test_over_cap_is_rejected_without_trimming(self):
        methods = b"".join(f"@Test public void test{i}() {{}}".encode() for i in range(31))
        with self.assertRaisesRegex(ValueError, "no truncation"):
            self.resolver(self.result(b"public class TooMany {" + methods + b"}"))
        self.assertFalse((self.root / "beam-suite").exists())

    def test_modified_block_hash_and_wrong_context_are_rejected(self):
        result = self.result(b"public class OneTest { @Test public void target() {} }")
        (self.root / "source-block-001.java").write_bytes(b"changed")
        with self.assertRaisesRegex(ValueError, "hash differs"):
            self.resolver(result)
        result["source_hash"] = "d" * 64
        with self.assertRaisesRegex(ValueError, "exact fixed context"):
            self.resolver(result)

    def test_duplicate_class_filenames_are_rejected(self):
        data = b"public class OneTest { @Test public void target() {} }"
        with self.assertRaisesRegex(ValueError, "collide"):
            self.resolver(self.result(data, data + b"\n"))

    def test_refusal_never_becomes_generated(self):
        result = self.result(b"public class OneTest { @Test public void target() {} }")
        result["generation_outcome"] = "refused"
        with self.assertRaises(ValueError):
            self.resolver(result)

    def test_republication_cannot_change_retained_archive(self):
        result = self.result(b"public class OneTest { @Test public void target() {} }")
        suite = self.resolver(result)
        suite.write_bytes(suite.read_bytes() + b"changed")
        with self.assertRaisesRegex(ValueError, "reconciliation"):
            self.resolver(result)

    def test_champ_worker_stores_crlf_source_bytes_matching_its_digest(self):
        from datetime import datetime, timedelta, timezone
        import json
        from scripts.study.api854.generate_worker import GenerationJob, GenerationWorker
        from scripts.study.api854.kku_client import Account, HTTPResponse, KKUClient, Model
        from scripts.study.api854.quota import QuotaLedger
        source = "public class CRLFTest {\r\n@org.junit.Test public void target() {}\r\n}\r\n"
        def transport(*args):
            return HTTPResponse(200, {}, json.dumps({"id": "fixture", "model": "claude-sonnet-5", "provider": "Claude",
                "choices": [{"finish_reason": "stop", "message": {"content": "```java\n" + source + "```"}}],
                "usage": {"prompt_tokens": 1, "completion_tokens": 2, "total_tokens": 3},
                "model_quota": {"daily_remaining_tokens": 9997}}).encode())
        ledger = QuotaLedger(self.root / "quota.sqlite")
        ledger.observe("a01", "sonnet", "fixture", remaining=10000,
            reset_at=(datetime.now(timezone.utc) + timedelta(hours=1)).isoformat(), evidence_ref="mock-only")
        ledger.activate_initial("a01", "sonnet")
        generator = GenerationWorker(KKUClient(Account("a01", "offline-credential-only"), transport=transport),
            ledger, artifact_root=self.root / "generated", model=Model("claude-sonnet-5", "claude-sonnet-5", "Claude"),
            bucket="sonnet", window="fixture", condition="mock-integration")
        job = GenerationJob(self.job["run_id"], self.job["protocol_hash"], "Lang", 4, "kku-claude", "fixture", "c" * 64)
        result = generator.generate(job, prompt_token_reserve=10, max_tokens=100, attempt_id=self.job["attempt_id"])
        block = result["source_blocks"][0]
        retained = Path(result["artifact_path"]) / block["path"]
        self.assertEqual(retained.read_bytes(), source.encode())
        self.assertEqual(hashlib.sha256(retained.read_bytes()).hexdigest(), block["sha256"])
        self.resolver(result)
