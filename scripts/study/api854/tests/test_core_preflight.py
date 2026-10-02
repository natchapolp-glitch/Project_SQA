from pathlib import Path
import tempfile
import unittest
from unittest.mock import Mock, patch

from scripts.study.api854 import common, core_preflight, queue_worker
from . import test_beam_queue


class CorePreflightTests(unittest.TestCase):
    def setUp(self):
        self.path = common.ROOT / "experiments/configs/api854-20261003/protocol.core-frozen.json"

    def test_exact_bytes_bind_without_mutation_and_cannot_open_generation(self):
        original = self.path.read_bytes()
        bound = core_preflight.bind(self.path, stage="prepare", condition="preflight", run_id=core_preflight.CORE_RUN)
        self.assertTrue(bound["preparation_only"])
        self.assertEqual(bound["core_protocol_sha256"], common.sha256(self.path))
        self.assertTrue(common.implementation_matches(bound["source_sha256"]))
        self.assertEqual(original, self.path.read_bytes())
        for stage, condition, run_id in [("generate", "preflight", core_preflight.CORE_RUN),
                                         ("evaluate", "preflight", core_preflight.CORE_RUN),
                                         ("prepare", "primary", core_preflight.CORE_RUN),
                                         ("prepare", "preflight", "different-run")]:
            with self.assertRaises(ValueError):
                core_preflight.bind(self.path, stage=stage, condition=condition, run_id=run_id)

    def test_changed_protocol_bytes_rejected(self):
        with tempfile.TemporaryDirectory() as directory:
            changed = Path(directory) / "core.json"
            changed.write_bytes(self.path.read_bytes() + b"\n")
            with self.assertRaises(ValueError):
                core_preflight.bind(changed, stage="prepare", condition="preflight", run_id=core_preflight.CORE_RUN)

    def test_unenabled_stage_rejected_before_claim(self):
        client = Mock()
        client.request.return_value = {"schema_version": "1.0", "enabled_stages": [], "jobs": []}
        with tempfile.TemporaryDirectory() as directory:
            with self.assertRaisesRegex(ValueError, "not enabled"):
                queue_worker.run_one(client, protocol_path=self.path, run_id=core_preflight.CORE_RUN,
                    stage="prepare", condition="preflight", approaches=["cmaes"], worker_id="beam-test",
                    output=Path(directory) / "attempt", results=Path(directory) / "results", worktrees=Path(directory) / "trees")
        client.claim.assert_not_called()


class CoreQueueIntegrationTests(unittest.TestCase):
    setUp = test_beam_queue.QueueIntegrationTests.setUp
    tearDown = test_beam_queue.QueueIntegrationTests.tearDown
    execute_fixture = test_beam_queue.QueueIntegrationTests.execute_fixture

    def test_core_preparation_publishes_but_generate_remains_unclaimable(self):
        self.protocol_path = common.ROOT / "experiments/configs/api854-20261003/protocol.core-frozen.json"
        self.run_id = core_preflight.CORE_RUN
        core = common.read_json(self.protocol_path)
        self.store.seed({"bugs": [{"project": "Jsoup", "bug_id": 1, "owner": "beam"}]},
                        self.run_id, common.sha256(self.protocol_path), False, protocol=core)
        # Remove the unrelated development fixture; real worker refuses mixtures.
        with self.store.db:
            self.store.db.execute("DELETE FROM jobs WHERE run_id != ?", (self.run_id,))
        with patch("scripts.study.api854.queue_worker.prepare_worker.execute",
                   side_effect=lambda job, protocol, results, *args: self.execute_fixture("prepare", job, protocol, results)):
            receipt = queue_worker.run_one(self.client, protocol_path=self.protocol_path, run_id=self.run_id,
                stage="prepare", condition="preflight", approaches=["cmaes"], worker_id="beam-fixture",
                output=self.root / "core-attempt", results=self.root / "results", worktrees=self.root / "trees")
        self.assertEqual(receipt["outcome"], "prepared")
        self.assertEqual(receipt["condition"], "preflight")
        self.assertIsNone(self.client.claim("beam-fixture", "generate", ["cmaes"])["job"])
        self.assertTrue(common.read_json(self.root / "core-attempt/execution-binding.json")["preparation_only"])
