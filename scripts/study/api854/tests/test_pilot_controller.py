import tempfile
import unittest
from pathlib import Path
from scripts.study.api854.queue_server import Store


class PilotControllerTests(unittest.TestCase):
    def setUp(self):
        self.tmp = tempfile.TemporaryDirectory()
        self.addCleanup(self.tmp.cleanup)
        self.store = Store(Path(self.tmp.name), {})
        self.addCleanup(self.store.db.close)
        self.rows = {"bugs": [{"project": "Lang", "bug_id": 1, "owner": "aom"}]}

    def test_prepare_only_pilot_never_leaks_into_generation(self):
        self.store.seed(self.rows, "pilot", "a" * 64, False,
                        protocol={"enabled_stages": ["prepare"], "state": "frozen_core"})
        first = self.store.claim({"worker_id": "beam1", "stage": "prepare"}, "beam")
        self.assertIsNotNone(first["job"])
        with self.store.db:
            self.store.db.execute("UPDATE jobs SET stage='generate',state='queued' WHERE job_id=?", (first["job"]["job_id"],))
        self.assertIsNone(self.store.claim({"worker_id": "champ1", "stage": "generate"}, "champ")["job"])

    def test_missing_protocol_is_held_instead_of_assumed_ready(self):
        self.store.seed(self.rows, "pilot", "a" * 64, False)
        self.assertIsNone(self.store.claim({"worker_id": "beam1", "stage": "prepare"}, "beam")["job"])

    def test_unverified_models_cannot_open_generation(self):
        with self.assertRaises(ValueError):
            self.store.seed(self.rows, "pilot", "a" * 64, False,
                            protocol={"state": "frozen", "enabled_stages": ["prepare", "generate"]})


if __name__ == "__main__":
    unittest.main()
