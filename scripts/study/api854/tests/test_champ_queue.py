from datetime import datetime, timedelta, timezone
import io
import json
from pathlib import Path
import tempfile
import time
import tarfile
import unittest
import urllib.request

from scripts.study.api854.champ_queue import (
    ChampQueueClient, QueueAccess, QueueError, QueueGenerationHandoff,
)
from scripts.study.api854.generate_worker import GenerationWorker
from scripts.study.api854.kku_client import Account, HTTPResponse, KKUClient, Model, digest
from scripts.study.api854.quota import QuotaLedger
from .test_client import ScriptedTransport, response
from .test_worker import completion

FAKE_QUEUE_TOKEN = "fake_queue_token_only_for_offline_tests"
FAKE_LEASE_TOKEN = "fake_lease_token_only_for_offline_tests"


def claim_fixture():
    return {"job": {"job_id": "job001", "run_id": "mock-only", "protocol_hash": "a" * 64,
                    "project": "Lang", "bug_id": 1, "approach": "kku-claude", "owner": "champ",
                    "stage": "generate", "state": "leased"},
            "attempt_id": "queue-attempt-001", "lease_token": FAKE_LEASE_TOKEN,
            "lease_version": 3, "lease_expires_at_unix": time.time() + 900}


class FakeQueueTransport:
    """Schema 1.0 HTTP contract checks, not a real controller or network call."""
    def __init__(self, *, fail_complete=False, bad_upload=False):
        self.calls, self.artifacts = [], {}
        self.fail_complete, self.bad_upload = fail_complete, bad_upload

    def __call__(self, method, url, headers, body, timeout):
        path = url.split("https://queue.example.test", 1)[1]
        self.calls.append({"method": method, "path": path,
                           "body": body if "artifacts" in path else json.loads(body) if body else None})
        if headers["Authorization"] != "Bearer " + FAKE_QUEUE_TOKEN:
            return response(401, {"error": "unauthorized"})
        if path == "/health":
            return response(payload={"ready": True, "schema_version": "1.0"})
        if path == "/v1/schema":
            return response(payload={"schema_version": "1.0", "observed_outcomes": {
                "generate": ["generated", "generation_failed", "refused", "needs_reconciliation"]}})
        if path.endswith("/artifacts"):
            if headers["X-Attempt-ID"] != "queue-attempt-001" or headers["X-Lease-Token"] != FAKE_LEASE_TOKEN \
                    or headers["X-Lease-Version"] != "3":
                raise AssertionError("Missing/faulty lease fence")
            artifact_id = f"artifact{len(self.artifacts) + 1}"
            artifact = {"artifact_id": artifact_id, "uri": "/v1/artifacts/" + artifact_id,
                        "name": headers["X-Artifact-Name"], "sha256": digest(body), "size": len(body)}
            self.artifacts[artifact_id] = (artifact, body)
            if self.bad_upload:
                artifact = {**artifact, "sha256": "0" * 64}
            return response(payload=artifact)
        if path.startswith("/v1/artifacts/"):
            return HTTPResponse(200, {}, self.artifacts[path.rsplit("/", 1)[1]][1])
        if path.endswith(("/complete", "/fail")):
            data = json.loads(body)
            if self.fail_complete:
                raise TimeoutError("possible success without response")
            if data["attempt_id"] != "queue-attempt-001" or data["lease_token"] != FAKE_LEASE_TOKEN \
                    or data["lease_version"] != 3:
                raise AssertionError("Missing completion fence")
            if not data["artifact_ids"] or any(a not in self.artifacts for a in data["artifact_ids"]):
                raise AssertionError("Artifacts must belong to the current attempt")
            required = {"requested_model", "actual_model", "account_alias", "prompt_sha256",
                        "response_id", "usage", "model_quota"}
            if not required.issubset(data["metadata"]):
                raise AssertionError("Missing required AI metadata")
            if data["outcome"] == "generated":
                hashes = {self.artifacts[a][0]["sha256"] for a in data["artifact_ids"]}
                if data["metadata"]["suite_sha256"] not in hashes:
                    raise AssertionError("Suite hash must match an uploaded artifact")
                if not path.endswith("/complete"):
                    raise AssertionError("Success may not use /fail")
            return response(payload={"job_id": "job001", "outcome": data["outcome"],
                                     "state": "queued" if data["outcome"] == "generated" else
                                     "needs_reconciliation" if data["outcome"] == "needs_reconciliation" else "finished"})
        raise AssertionError(f"Unexpected queue mutation: {path}")


class QueueClientTests(unittest.TestCase):
    def client(self, transport=None, mock=True):
        return ChampQueueClient(QueueAccess("https://queue.example.test/", FAKE_QUEUE_TOKEN),
                                transport=transport or FakeQueueTransport(), mock_mode=mock)

    def test_authenticated_connectivity_uses_get_only(self):
        transport = FakeQueueTransport()
        result = self.client(transport).check()
        self.assertEqual(result["authenticated_schema_version"], "1.0")
        self.assertEqual(result["queue_mutations"], 0)
        self.assertEqual([c["method"] for c in transport.calls], ["GET", "GET"])

    def test_schema_mismatch_blocks_worker(self):
        transport = ScriptedTransport(response(payload={"ready": True, "schema_version": "1.0"}),
                                      response(payload={"schema_version": "2.0"}))
        with self.assertRaises(QueueError):
            self.client(transport).check()

    def test_role_origin_and_mock_transport_guard(self):
        with self.assertRaises(ValueError):
            QueueAccess("https://queue.example.test", "fake", role="beam")
        with self.assertRaises(ValueError):
            QueueAccess("http://queue.example.test", "fake")
        with self.assertRaises(ValueError):
            ChampQueueClient(QueueAccess("https://queue.example.test", "fake"), mock_mode=True)

    def test_private_access_not_in_repr(self):
        self.assertNotIn(FAKE_QUEUE_TOKEN, repr(QueueAccess("https://queue.example.test", FAKE_QUEUE_TOKEN)))

    def test_conflict_and_redirect_do_not_retry_or_expose_body(self):
        for status, kind in [(409, "lease_conflict"), (302, "redirect_blocked"), (401, "auth_failed")]:
            transport = ScriptedTransport(response(status, {"secret": FAKE_QUEUE_TOKEN}))
            with self.assertRaises(QueueError) as error:
                self.client(transport).request("POST", "/v1/jobs/claim", {})
            self.assertEqual(error.exception.kind, kind)
            self.assertNotIn(FAKE_QUEUE_TOKEN, str(error.exception))
            self.assertEqual(len(transport.calls), 1)

    def test_redirect_handler_never_forwards_auth(self):
        from scripts.study.api854.queue_client import NoRedirect
        req = urllib.request.Request("https://queue.example.test/health", headers={"Authorization": "Bearer fake"})
        self.assertIsNone(NoRedirect().redirect_request(req, None, 302, "", {}, "https://other.example.test"))

    def test_absolute_uri_and_admin_mutation_rejected(self):
        for path in ("https://other.example.test/v1/artifacts/a", "/v1/admin/reconcile/job001"):
            with self.assertRaises(ValueError):
                self.client().request("GET", path)

    def test_claim_only_champ_primary_generation(self):
        for kwargs in ({"owner": "beam"}, {"approaches": ["cmaes"]}, {"approaches": None}):
            with self.assertRaises(ValueError):
                self.client().claim("champ-pc1", "generate", **kwargs)

    def test_lost_mutation_response_requires_reconciliation(self):
        transport = ScriptedTransport(TimeoutError())
        with self.assertRaises(QueueError) as error:
            self.client(transport).request("POST", "/v1/jobs/claim", {})
        self.assertTrue(error.exception.unknown)
        self.assertEqual(len(transport.calls), 1)


class HandoffTests(unittest.TestCase):
    def setUp(self):
        self.temp = tempfile.TemporaryDirectory()
        self.root = Path(self.temp.name)
        self.claim = claim_fixture()
        self.ledger = QuotaLedger(self.root / "quota.sqlite")
        self.ledger.observe("a01", "sonnet", "window", remaining=200000,
                            reset_at=(datetime.now(timezone.utc) + timedelta(hours=1)).isoformat(), evidence_ref="mock")
        self.ledger.activate_initial("a01", "sonnet")
        from scripts.study.api854.generate_worker import GenerationJob
        self.job = GenerationJob("mock-only", "a" * 64, "Lang", 1, "kku-claude", "fixed context", "b" * 64)

    def tearDown(self):
        self.temp.cleanup()

    def result(self, payload=None, failure=None):
        transport = ScriptedTransport(failure if failure is not None else response(payload=payload or completion()))
        worker = GenerationWorker(KKUClient(Account("a01", "mock-kku-key"), transport=transport), self.ledger,
                                  artifact_root=self.root / "artifacts",
                                  model=Model("claude-sonnet-5", "claude-sonnet-5", "Claude"),
                                  bucket="sonnet", window="window", condition="mock-integration")
        return worker.generate(self.job, prompt_token_reserve=1000, max_tokens=4000,
                               attempt_id=self.claim["attempt_id"])

    def adapter(self, transport=None, **kwargs):
        client = ChampQueueClient(QueueAccess("https://queue.example.test", FAKE_QUEUE_TOKEN),
                                  transport=transport or FakeQueueTransport(), mock_mode=True)
        return QueueGenerationHandoff(client, self.claim, worker_id="champ-pc1", **kwargs)

    def test_generated_requires_external_suite_processing_policy(self):
        result = self.result()
        transport = FakeQueueTransport()
        with self.assertRaises(ValueError):
            self.adapter(transport).publish_generation(result)
        self.assertFalse(transport.calls)

    def test_artifacts_uploaded_before_fenced_completion(self):
        result = self.result()
        suite = self.root / "suite.tar.bz2"
        # Archive checks are real; Java execution remains outside this mock test.
        source = b"class Example {}\n"
        with tarfile.open(suite, "w:bz2") as archive:
            info = tarfile.TarInfo("Example.java")
            info.size = len(source)
            archive.addfile(info, io.BytesIO(source))
        transport = FakeQueueTransport()
        receipt = self.adapter(transport, suite_resolver=lambda _: suite,
                               suite_policy_id="mock-only-v1").publish_generation(result)
        self.assertIn(":generated", receipt)
        self.assertTrue(all(c["path"].endswith("/artifacts") for c in transport.calls[:-1]))
        data = transport.calls[-1]["body"]
        self.assertEqual(data["metadata"]["suite_sha256"], digest(suite.read_bytes()))
        self.assertEqual(data["metadata"]["requested_model"], "claude-sonnet-5")
        self.assertIsNone(data["metadata"]["usable_suite"])

    def test_suite_archive_credentials_and_traversal_rejected_before_upload(self):
        result = self.result()
        suite = self.root / "suite.tar.bz2"
        for filename, content in [("../Example.java", b"class Example {}"),
                                  ("Example.java", FAKE_QUEUE_TOKEN.encode())]:
            with self.subTest(filename=filename, content=content):
                with tarfile.open(suite, "w:bz2") as archive:
                    info = tarfile.TarInfo(filename)
                    info.size = len(content)
                    archive.addfile(info, io.BytesIO(content))
                transport = FakeQueueTransport()
                with self.assertRaises(ValueError):
                    self.adapter(transport, suite_resolver=lambda _: suite,
                                 suite_policy_id="mock-only-v1").publish_generation(result)
                self.assertFalse(transport.calls)

    def test_refusal_uses_fail_with_observed_artifacts(self):
        payload = completion(choices=[{"finish_reason": "content_filter", "message": {"content": "refused"}}])
        result = self.result(payload=payload)
        transport = FakeQueueTransport()
        self.adapter(transport).publish_generation(result)
        self.assertTrue(transport.calls[-1]["path"].endswith("/fail"))
        self.assertEqual(transport.calls[-1]["body"]["outcome"], "refused")

    def test_quota_rejection_and_timeout_are_not_finished_generation_failures(self):
        result = self.result(failure=response(401, {"error": "This model reached daily limit."}))
        transport = FakeQueueTransport()
        self.adapter(transport).publish_generation(result)
        self.assertEqual(transport.calls[-1]["body"]["outcome"], "needs_reconciliation")
        self.assertEqual(transport.calls[-1]["body"]["metadata"]["raw_generation_outcome"], "daily_limit")

    def test_unknown_provider_outcome_remains_needs_reconciliation(self):
        result = self.result(failure=TimeoutError())
        transport = FakeQueueTransport()
        self.adapter(transport).publish_generation(result)
        self.assertEqual(transport.calls[-1]["body"]["outcome"], "needs_reconciliation")

    def test_local_evidence_checked_before_any_upload(self):
        result = self.result(failure=TimeoutError())
        (Path(result["artifact_path"]) / "response.txt").write_text(FAKE_QUEUE_TOKEN, encoding="utf-8")
        transport = FakeQueueTransport()
        with self.assertRaises(ValueError):
            self.adapter(transport).publish_generation(result)
        self.assertFalse(transport.calls)

    def test_mismatched_job_attempt_and_expired_lease_rejected(self):
        result = self.result(failure=TimeoutError())
        transport = FakeQueueTransport()
        for change in ({"project": "Math"}, {"attempt_id": "other-attempt"}):
            with self.assertRaises(ValueError):
                self.adapter(transport).publish_generation({**result, **change})
        self.claim["lease_expires_at_unix"] = time.time() - 1
        with self.assertRaises(QueueError):
            self.adapter(transport).publish_generation(result)
        self.assertFalse(transport.calls)

    def test_mock_evidence_cannot_reach_primary_live_queue(self):
        result = self.result(failure=TimeoutError())
        transport = FakeQueueTransport()
        client = ChampQueueClient(QueueAccess("https://queue.example.test", FAKE_QUEUE_TOKEN), transport=transport)
        with self.assertRaises(ValueError):
            QueueGenerationHandoff(client, self.claim, worker_id="champ-pc1").publish_generation(result)
        self.assertFalse(transport.calls)

    def test_bad_upload_hash_stops_before_complete(self):
        result = self.result(failure=TimeoutError())
        transport = FakeQueueTransport(bad_upload=True)
        with self.assertRaises(QueueError):
            self.adapter(transport).publish_generation(result)
        self.assertEqual(len(transport.calls), 1)

    def test_lost_completion_is_not_automatically_resent(self):
        result = self.result(failure=TimeoutError())
        transport = FakeQueueTransport(fail_complete=True)
        adapter = self.adapter(transport)
        with self.assertRaises(QueueError):
            adapter.publish_generation(result)
        count = len(transport.calls)
        with self.assertRaises(QueueError) as error:
            adapter.publish_generation(result)
        self.assertEqual(error.exception.kind, "completion_needs_reconciliation")
        self.assertEqual(len(transport.calls), count)

    def test_download_hash_mismatch_is_rejected(self):
        transport = FakeQueueTransport()
        client = self.adapter(transport).client
        path = self.root / "evidence.txt"
        path.write_bytes(b"observed evidence")
        artifact = client.upload_checked(self.claim, path)
        self.assertEqual(client.download(artifact), path.read_bytes())
        with self.assertRaises(ValueError):
            client.download({**artifact, "sha256": "0" * 64})
