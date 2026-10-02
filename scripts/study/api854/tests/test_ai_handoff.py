from pathlib import Path
import unittest

from . import test_beam_queue
from scripts.study.api854.ai_handoff import BeamGenerationHandoff
from scripts.study.api854.common import read_json, sha256, write_json
from scripts.study.api854.beam_queue import FencedPublisher, download_generation
from scripts.study.api854.lease import LeaseHeartbeat
from scripts.study.api854.queue_client import QueueClient
from scripts.study.api854.queue_worker import local_job


class AIHandoffTests(unittest.TestCase):
    setUp = test_beam_queue.QueueIntegrationTests.setUp
    tearDown = test_beam_queue.QueueIntegrationTests.tearDown

    def handoff(self):
        prepare = self.client.claim("beam-fixture", "prepare", ["kku-claude"])
        observed = self.root / "prepared.json"
        observed.write_text('{"condition":"offline-fixture"}')
        sources = {"src/Target.java": "b" * 64}
        with LeaseHeartbeat(self.client, prepare, interval_seconds=30) as heartbeat:
            FencedPublisher(self.client, prepare, heartbeat, self.root / "prepare-publication").publish(
                "prepared", [observed], {"fixed_source_sha256": sources, "context_source_hash": "c" * 64})
        claim = QueueClient.claim(self.client, "beam-fixture", "generate", ["kku-claude"], owner="beam")
        job = local_job(claim)
        heartbeat = LeaseHeartbeat(self.client, claim, interval_seconds=30)
        heartbeat.__enter__()
        self.addCleanup(heartbeat.stop)
        handoff = BeamGenerationHandoff(self.client, claim, heartbeat=heartbeat, job=job,
            protocol=read_json(self.protocol_path), fixed_source_sha256=sources,
            context_source_hash="c" * 64, worker_id="beam-fixture")
        directory = self.root / "generation"
        directory.mkdir()
        source = directory / "source-block-001.java"
        source.write_bytes(b"import org.junit.Test; public class AIExampleTest { @Test public void target() {} }")
        result = {**job, "condition": "mock-integration", "generation_outcome": "response_received",
                  "artifact_path": str(directory), "source_hash": "c" * 64,
                  "model_pin": {"id": "claude-sonnet-5"}, "actual_model": "claude-sonnet-5",
                  "source_blocks": [{"path": source.name, "sha256": sha256(source)}],
                  "usage": None, "model_quota": None}
        return handoff, result, claim

    def test_champ_source_blocks_publish_suite_manifest_lineage_for_evaluation(self):
        handoff, result, claim = self.handoff()
        write_json(Path(result["artifact_path"]) / "generation-result.json", result)
        receipt = handoff.publish_generation(result)
        self.assertTrue(receipt.endswith(":generated"))
        evaluation = self.client.claim("beam-fixture", "evaluate", ["kku-claude"])
        suite, lineage = download_generation(self.client, evaluation, self.root / "download")
        self.assertEqual(sha256(suite), lineage["suite_sha256"])
        self.assertEqual(lineage["job"]["attempt_id"], claim["attempt_id"])
        self.assertNotEqual(lineage["job"]["attempt_id"], evaluation["attempt_id"])
        self.assertIsNone(lineage["executed_test_count"])
        stage = evaluation["job"]["payload"]["stage_history"][-1]
        self.assertFalse(stage["metadata"]["usable"])
        self.assertEqual(stage["metadata"]["requested_model"], "claude-sonnet-5")
        self.assertIsNone(stage["metadata"]["usage"])

    def test_no_tests_is_observed_generation_failure_with_raw_evidence(self):
        handoff, result, claim = self.handoff()
        source = Path(result["artifact_path"]) / "source-block-001.java"
        source.write_bytes(b"public class AIExampleTest {}")
        result["source_blocks"][0]["sha256"] = sha256(source)
        write_json(Path(result["artifact_path"]) / "generation-result.json", result)
        self.assertTrue(handoff.publish_generation(result).endswith(":generation_failed"))
        job = next(j for j in self.store.status()["jobs"] if j["approach"] == "kku-claude")
        self.assertEqual(job["state"], "finished")
        self.assertEqual(job["payload"]["stage_history"][-1]["metadata"]["raw_generation_outcome"], "response_received")

    def test_mock_result_cannot_reach_live_client(self):
        handoff, result, claim = self.handoff()
        self.client.mock_mode = False
        with self.assertRaisesRegex(ValueError, "Mock AI evidence"):
            handoff.publish_generation(result)

    def test_wrong_requested_model_cannot_replace_the_condition(self):
        handoff, result, claim = self.handoff()
        result["model_pin"]["id"] = "claude-sonnet-5.5"
        with self.assertRaisesRegex(ValueError, "no fallback"):
            handoff.publish_generation(result)

    def test_invalid_context_cannot_be_used_to_construct_handoff(self):
        handoff, result, claim = self.handoff()
        with self.assertRaisesRegex(ValueError, "successful queue preparation"):
            BeamGenerationHandoff(self.client, claim, heartbeat=handoff.lease_guard,
                job=local_job(claim), protocol=read_json(self.protocol_path),
                fixed_source_sha256={"different.java": "d" * 64}, context_source_hash="c" * 64,
                worker_id="beam-fixture")


if __name__ == "__main__":
    unittest.main()
