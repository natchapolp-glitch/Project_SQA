import base64
from datetime import datetime, timezone
import json
from pathlib import Path
import tempfile
import threading
import unittest
import zipfile
from unittest.mock import patch
from urllib import request, error

from scripts.study.api854 import inventory, queue, report, service


class ServiceTests(unittest.TestCase):
    def setUp(self):
        self.tmp = tempfile.TemporaryDirectory()
        self.addCleanup(self.tmp.cleanup)
        self.root = Path(self.tmp.name)
        self.q = queue.Queue(self.root / "state.sqlite")
        self.q.seed(inventory.build_jobs([{"project": "Lang", "bug_id": 1, "owner": "aom"}], "a" * 64), active=True)
        self.server = service.make_server(self.q, "127.0.0.1", 0, "test-secret", self.root / "artifacts")
        self.thread = threading.Thread(target=self.server.serve_forever, daemon=True)
        self.thread.start()
        self.addCleanup(self.close_server)
        self.url = f"http://127.0.0.1:{self.server.server_port}"

    def close_server(self):
        self.server.shutdown()
        self.server.server_close()
        self.thread.join(2)

    def post(self, action, payload, token="test-secret"):
        req = request.Request(self.url + "/" + action, json.dumps(payload).encode(),
                              {"Authorization": "Bearer " + token, "Content-Type": "application/json"})
        with request.urlopen(req, timeout=5) as response:
            return json.load(response)

    def test_worker_auth_and_artifact_upload_with_real_server(self):
        with self.assertRaises(error.HTTPError) as unauthorized:
            self.post("claim", {"stage": "generate", "worker_id": "w"}, token="wrong")
        self.assertEqual(unauthorized.exception.code, 401)
        unauthorized.exception.close()
        claim = self.post("claim", {"stage": "generate", "worker_id": "w"})
        lease = {"job_id": claim["job_id"], "lease": claim["lease"], "worker_id": "w"}
        attempt = self.post("start", lease)["attempt_id"]
        data = b"observed evidence"
        artifact = self.post("artifact", {**lease, "attempt_id": attempt,
                                          "data_base64": base64.b64encode(data).decode()})
        self.assertEqual(Path(artifact["path"]).read_bytes(), data)
        self.assertEqual(artifact["sha256"], "7b3a46c30e4b7f2692c718389281c74aa0115fb42fb9bded18ab8a1b21811916")
        download = request.Request(self.url + artifact["uri"], headers={"Authorization": "Bearer test-secret"})
        with request.urlopen(download, timeout=5) as response:
            self.assertEqual(response.read(), data)
        with self.assertRaises(error.HTTPError) as missing:
            self.post("activate", {})  # Workers cannot open gates or administer queue.
        missing.exception.close()

    def test_generation_cutoff_holds_jobs_without_fabricated_failure(self):
        late = datetime(2026, 10, 4, 18, 0, tzinfo=timezone.utc).timestamp()
        self.q.clock = lambda: late  # 5 Oct 01:00 Asia/Bangkok.
        self.assertIsNone(self.post("claim", {"stage": "generate", "worker_id": "w"}))
        self.assertEqual(report.summarize(self.q.snapshot())["terminal_outcomes"], 0)

    def test_unauthorized_post_body_returns_401_without_parsing_or_claiming(self):
        self.q.clock = lambda: 1000
        before = self.q.snapshot()
        with patch.object(self.q, "claim", side_effect=AssertionError("unauthorized queue access")):
            for _ in range(5):
                req = request.Request(self.url + "/claim", b"invalid JSON body\n" * 1024,
                                      {"Authorization": "Bearer wrong", "Content-Type": "application/json"})
                with self.assertRaises(error.HTTPError) as unauthorized:
                    request.urlopen(req, timeout=5)
                self.assertEqual(unauthorized.exception.code, 401)
                unauthorized.exception.close()
        self.assertEqual(self.q.snapshot(), before)

    def test_example_gate_never_activates_pilot(self):
        with self.assertRaises(ValueError):
            service.validate_gate({"example_only": True}, {"state": "draft"}, {}, "A")


class FreezeTests(unittest.TestCase):
    def test_frozen_package_is_repeatable_and_detects_secret_artifacts(self):
        with tempfile.TemporaryDirectory() as tmp:
            root = Path(tmp)
            q = queue.Queue(root / "state.sqlite", clock=lambda: 1000)
            q.seed(inventory.build_jobs([{"project": "Lang", "bug_id": 1, "owner": "aom"}], "a" * 64))
            snapshot = q.snapshot()
            report.write_report(snapshot, root / "report")
            self.assertNotIn(b"\r\n", (root / "report" / "progress.json").read_bytes())
            first, second = root / "first.zip", root / "second.zip"
            report.freeze(snapshot, [], first)
            report.freeze(snapshot, [], second)
            with zipfile.ZipFile(first) as archive:
                self.assertIn("report/REPORT_TH.md", archive.namelist())
                self.assertEqual(json.loads(archive.read("report/progress.json"))["not_attempted_keys"], 4)
            self.assertEqual(first.read_bytes(), second.read_bytes())
            self.assertEqual(report.verify_package(first)["snapshot_hash"], inventory.canonical_hash(snapshot))
            secret = root / "record.json"
            secret.write_text('{"api_key": "do-not-publish-this"}')
            with self.assertRaises(ValueError):
                report.freeze(snapshot, [secret], root / "secret.zip")
            self.assertFalse((root / "secret.zip").exists())

    def test_protocols_are_not_pooled_into_matched_results(self):
        with tempfile.TemporaryDirectory() as tmp:
            q = queue.Queue(Path(tmp) / "state.sqlite")
            rows = [{"project": "Lang", "bug_id": 1, "owner": "aom"}]
            q.seed(inventory.build_jobs(rows, "a" * 64) + inventory.build_jobs(rows, "b" * 64))
            with self.assertRaises(ValueError):
                report.summarize(q.snapshot())


if __name__ == "__main__":
    unittest.main()
