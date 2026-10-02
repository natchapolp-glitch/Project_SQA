"""Exercise Champ's mocked API worker against the current gated Aom Store."""
from . import test_aom_store_integration as baseline
from .test_beam_queue import mock_stage_gate
from scripts.study.api854 import queue_server
import types


class CurrentStoreIntegrationTests(baseline.AomStoreIntegrationTests):
    @classmethod
    def setUpClass(cls):
        class MockGatedStore(queue_server.Store):
            def seed(self, manifest, run_id, protocol_hash, pilot=False, protocol=None):
                return super().seed(manifest, run_id, protocol_hash, pilot, protocol=mock_stage_gate())
        cls.aom = types.SimpleNamespace(Store=MockGatedStore, schema=queue_server.schema, APIError=queue_server.APIError)
