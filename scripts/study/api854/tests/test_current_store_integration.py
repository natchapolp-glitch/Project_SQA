"""Mocked generation against the current gated Store; no production access."""
from . import test_aom_store_integration as baseline
from scripts.study.api854 import queue_server
import types

class CurrentStoreIntegrationTests(baseline.AomStoreIntegrationTests):
    @classmethod
    def setUpClass(cls):
        class MockGatedStore(queue_server.Store):
            def seed(self, manifest, run_id, protocol_hash, pilot=False, protocol=None):
                declaration = {"state": "frozen", "enabled_stages": ["prepare", "generate", "evaluate"],
                    "kku": {"model_settings_verified": True, "exact_model_ids": {"kku-claude": "mock-claude", "kku-gemini": "mock-gemini"}},
                    "gate_a": {"reviewed_by": {"aom": True, "champ": True, "beam": True}, "evidence": ["mock-only"]}}
                return super().seed(manifest, run_id, protocol_hash, pilot, protocol=declaration)
        cls.aom = types.SimpleNamespace(Store=MockGatedStore, schema=queue_server.schema, APIError=queue_server.APIError)
