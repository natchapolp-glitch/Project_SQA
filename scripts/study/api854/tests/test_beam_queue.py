"""Real loopback Aom HTTP server; stage execution is explicitly a fixture."""
from http.server import ThreadingHTTPServer
from pathlib import Path
import tempfile
import threading
import unittest
from unittest.mock import patch

from scripts.study.api854 import common, configuration, queue_worker
from scripts.study.api854.beam_queue import BeamAccess, BeamQueueClient, FencedPublisher, download_generation
from scripts.study.api854.champ_queue import QueueError
from scripts.study.api854.lease import LeaseHeartbeat
from scripts.study.api854.pack_suite import pack_suite
from scripts.study.api854.queue_client import QueueClient
from scripts.study.api854.queue_server import Store, Handler


def mock_stage_gate():
    return {"state": "frozen", "enabled_stages": ["prepare", "generate", "evaluate"],
            "kku": {"model_settings_verified": True,
                    "exact_model_ids": {"kku-claude": "mock-claude", "kku-gemini": "mock-gemini"}},
            "gate_a": {"reviewed_by": {"aom": True, "champ": True, "beam": True}, "evidence": ["offline-fixture-only"]}}


class QueueIntegrationTests(unittest.TestCase):
    def setUp(self):
        self.temp = tempfile.TemporaryDirectory()
        self.root = Path(self.temp.name)
        self.protocol_path = self.root / "protocol.json"
        common.write_json(self.protocol_path, configuration.proposal())
        self.run_id = "beam-development-fixture"
        self.token = "offline-loopback-beam-credential-unique-12345678"
        self.store = Store(self.root / "controller", {"beam": self.token, "admin": "offline-admin-only-123456789"})
        self.store.seed({"bugs": [{"project": "Lang", "bug_id": 4, "owner": "beam"}]},
                        self.run_id, common.sha256(self.protocol_path), False, protocol=mock_stage_gate())
        self.server = ThreadingHTTPServer(("127.0.0.1", 0), Handler)
        self.server.daemon_threads = True
        self.server.store = self.store
        self.thread = threading.Thread(target=self.server.serve_forever, daemon=True)
        self.thread.start()
        self.client = BeamQueueClient.for_loopback_test(f"http://127.0.0.1:{self.server.server_port}", self.token)
        self.serial = 0

    def tearDown(self):
        self.server.shutdown()
        self.server.server_close()
        self.thread.join()
        self.store.db.close()
        self.temp.cleanup()

    def execute_fixture(self, stage, job, protocol, results, *args):
        folder = common.start_attempt(results, job, stage)
        (folder / "fixture.log").write_text("Offline test fixture, not an experiment\n")
        result = common.envelope(job, "prepared" if stage == "prepare" else "generated" if stage == "generation" else "complete",
                                 fixed_source_sha256={"src/Target.java": "b" * 64}, usable=False)
        if stage == "generation":
            sources = folder / "sources"
            sources.mkdir()
            (sources / "ExampleTest.java").write_bytes(b"public class ExampleTest { @org.junit.Test public void target() {} }")
            manifest = pack_suite(sources, folder / "package", 1)
            result.update(test_count=1, suite_sha256=manifest["suite_sha256"],
                          generation={"suite": str(folder / "package/suite.tar.bz2")})
        common.write_json(folder / "result.json", result)
        return result

    def run_stage(self, stage, owner="beam"):
        self.serial += 1
        worker = {"prepare": "prepare_worker", "generate": "algorithm_worker", "evaluate": "evaluate_worker"}[stage]
        phase = {"prepare": "prepare", "generate": "generation", "evaluate": "evaluation"}[stage]
        with patch(f"scripts.study.api854.queue_worker.{worker}.execute",
                   side_effect=lambda job, protocol, results, *args: self.execute_fixture(phase, job, protocol, results, *args)):
            return queue_worker.run_one(self.client, protocol_path=self.protocol_path, run_id=self.run_id,
                stage=stage, approaches=["cmaes"], worker_id="beam-fixture",
                output=self.root / f"run-{self.serial}", results=self.root / "results", worktrees=self.root / "trees",
                heartbeat_interval=0.02, owner=owner)

    def test_cross_owner_requires_protocol_route_and_keeps_owner_lineage(self):
        with self.assertRaisesRegex(ValueError, "routing"):
            self.run_stage("prepare", owner="champ")
        self.assertEqual(len(self.store.status()["attempts"]), 0)
        path = self.root / "routed-protocol.json"
        protocol = common.read_json(self.protocol_path)
        protocol["worker_routing"] = {s: ["champ"] for s in ("prepare", "generate", "evaluate")}
        common.write_json(path, protocol)
        self.protocol_path = path
        with self.store.db:
            self.store.db.execute("DELETE FROM jobs")
        self.store.seed({"bugs": [{"project": "Lang", "bug_id": 4, "owner": "champ"}]},
                        self.run_id, common.sha256(path), False, protocol=mock_stage_gate())
        receipts = [self.run_stage(s, owner="champ") for s in ("prepare", "generate", "evaluate")]
        self.assertEqual([r["outcome"] for r in receipts], ["prepared", "generated", "complete"])
        self.assertTrue(all(j["owner"] == "champ" for j in self.store.status()["jobs"]))

    def test_prepare_generate_evaluate_transition_and_cross_attempt_artifacts(self):
        receipts = [self.run_stage(stage) for stage in ("prepare", "generate", "evaluate")]
        self.assertEqual([r["outcome"] for r in receipts], ["prepared", "generated", "complete"])
        self.assertEqual(len({r["attempt_id"] for r in receipts}), 3)
        job = next(j for j in self.store.status()["jobs"] if j["approach"] == "cmaes")
        self.assertEqual(job["state"], "finished")
        self.assertFalse(job["payload"]["stage_history"][-1]["metadata"]["usable"])
        history = job["payload"]["stage_history"]
        self.assertEqual(len(history), 3)
        self.assertEqual(history[-2]["metadata"]["suite_sha256"],
                         next(a["sha256"] for a in history[-2]["artifacts"] if a["name"] == "suite.tar.bz2"))
        for path in self.root.glob("run-*/*.json"):
            self.assertNotIn(self.token, path.read_text())

    def test_mixed_protocol_is_rejected_before_claim(self):
        self.store.seed({"bugs": [{"project": "Lang", "bug_id": 5, "owner": "beam"}]}, "other-run", "d" * 64, False,
                        protocol=mock_stage_gate())
        with self.assertRaisesRegex(ValueError, "mixes run"):
            self.run_stage("prepare")
        self.assertEqual(len(self.store.status()["attempts"]), 0)

    def test_draft_cannot_start_primary_and_ai_is_not_implicitly_claimed(self):
        kwargs = dict(protocol_path=self.protocol_path, run_id=self.run_id, stage="prepare", approaches=["cmaes"],
                      worker_id="beam-fixture", output=self.root / "run", results=self.root / "results", worktrees=self.root / "trees")
        with self.assertRaisesRegex(ValueError, "frozen protocol"):
            queue_worker.run_one(self.client, condition="primary", **kwargs)
        with self.assertRaisesRegex(ValueError, "AI generation"):
            self.client.claim("beam-fixture", "generate", ["kku-claude"])
        self.assertEqual(len(self.store.status()["attempts"]), 0)

    def active_publisher(self):
        claim = self.client.claim("beam-fixture", "prepare", ["cmaes"])
        heartbeat = LeaseHeartbeat(self.client, claim, interval_seconds=30)
        heartbeat.__enter__()
        self.addCleanup(heartbeat.stop)
        evidence = self.root / "evidence.json"
        evidence.write_text('{"fixture":true}')
        return claim, heartbeat, FencedPublisher(self.client, claim, heartbeat, self.root / "publish"), evidence

    def test_expired_lease_stops_publication(self):
        claim, heartbeat, publisher, evidence = self.active_publisher()
        with self.store.lock, self.store.db:
            self.store.db.execute("UPDATE jobs SET lease_until=0 WHERE job_id=?", (claim["job"]["job_id"],))
        with self.assertRaises(QueueError):
            heartbeat.renew_once()
        with self.assertRaises(QueueError):
            publisher.publish("prepared", [evidence], {})
        self.assertEqual(self.store.db.execute("SELECT count(*) FROM artifacts").fetchone()[0], 0)

    def test_unknown_upload_preserves_intent_without_replaying(self):
        claim, heartbeat, publisher, evidence = self.active_publisher()
        original = self.client.upload_checked
        def lose_receipt(*args):
            original(*args)
            raise QueueError("transport_unknown", unknown=True)
        with patch.object(self.client, "upload_checked", side_effect=lose_receipt) as upload:
            with self.assertRaises(QueueError):
                publisher.publish("prepared", [evidence], {})
            with self.assertRaisesRegex(QueueError, "upload_needs_reconciliation"):
                publisher.publish("prepared", [evidence], {})
            self.assertEqual(upload.call_count, 1)
        self.assertEqual(self.store.db.execute("SELECT count(*) FROM artifacts").fetchone()[0], 1)

    def test_unknown_complete_does_not_replay_or_regenerate(self):
        claim, heartbeat, publisher, evidence = self.active_publisher()
        original = self.client.complete
        def lose_receipt(*args, **kwargs):
            original(*args, **kwargs)
            raise QueueError("transport_unknown", unknown=True)
        with patch.object(self.client, "complete", side_effect=lose_receipt) as complete:
            with self.assertRaises(QueueError):
                publisher.publish("prepared", [evidence], {})
            with self.assertRaisesRegex(QueueError, "completion_needs_reconciliation"):
                publisher.publish("prepared", [evidence], {})
            self.assertEqual(complete.call_count, 1)
        job = next(j for j in self.store.status()["jobs"] if j["approach"] == "cmaes")
        self.assertEqual(job["stage"], "generate")

    def test_all_evidence_is_checked_before_any_upload(self):
        claim, heartbeat, publisher, evidence = self.active_publisher()
        unsafe = self.root / "unsafe.json"
        unsafe.write_text(claim["lease_token"])
        with self.assertRaisesRegex(ValueError, "Private credential"):
            publisher.publish("prepared", [evidence, unsafe], {})
        self.assertEqual(self.store.db.execute("SELECT count(*) FROM artifacts").fetchone()[0], 0)

    def test_download_rejects_foreign_origin_before_request(self):
        claim = {"job": {"payload": {"stage_history": [{"stage": "generate", "outcome": "generated",
                 "artifacts": [{"name": "suite.tar.bz2", "uri": "https://foreign.example/steal"}]}]}}}
        with patch.object(self.client, "request") as request:
            with self.assertRaisesRegex(ValueError, "outside the queue"):
                download_generation(self.client, claim, self.root / "download")
            request.assert_not_called()

    def test_live_access_validation_and_redacted_server_errors(self):
        for url in ("http://queue.example", "https://queue.example/other", "https://queue.example/?token=x"):
            with self.assertRaises(ValueError):
                BeamAccess(url, self.token)
        self.assertNotIn(self.token, repr(BeamAccess("https://queue.example", self.token)))
        self.assertEqual(self.client.check()["role"], "beam")


if __name__ == "__main__":
    unittest.main()
