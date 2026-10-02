import unittest

from scripts.study.api854.kku_client import KKUError, Model, parse_models
from scripts.study.api854.models import load_selection, resolve_selected
from .test_client import ScriptedTransport, response
from . import test_worker as worker_fixtures
from .test_worker import completion


class SelectionTests(unittest.TestCase):
    def test_user_selected_sonnet_5_is_exact_among_two_sonnets(self):
        models = parse_models({"data": [
            {"id": "claude-sonnet-5.5", "owned_by": "Claude"},
            {"id": "claude-sonnet-5", "owned_by": "Claude"},
            {"id": "gemini-3.5-flash-lite", "owned_by": "Gemini"},
        ]})
        self.assertEqual(resolve_selected(models, "kku-claude").id, "claude-sonnet-5")
        self.assertEqual(resolve_selected(models, "kku-gemini").id, "gemini-3.5-flash-lite")

    def test_missing_selected_version_cannot_fallback(self):
        for model in (Model("claude-sonnet-5.5", "claude-sonnet-5.5", "Claude"),
                      Model("claude-haiku-latest", "claude-haiku-latest", "Claude"),
                      Model(7, "claude-sonnet-5", "Claude")):
            with self.subTest(model=model), self.assertRaises(KKUError):
                resolve_selected([model], "kku-claude")

    def test_manifest_separates_model_selection_from_protocol_readiness(self):
        selection = load_selection()
        self.assertEqual(selection["kku-claude"]["family"], "sonnet")
        self.assertEqual(selection["kku-gemini"]["family"], "flash-lite")


class PrimaryPinTests(unittest.TestCase):
    def setUp(self):
        # Reuse fixture setup explicitly; do not import its test class into discovery.
        self.fixture = worker_fixtures.WorkerTests("test_no_live_queue_does_not_claim_connected")
        self.fixture.setUp()

    def tearDown(self):
        self.fixture.tearDown()

    def test_primary_worker_sends_selected_id(self):
        transport = ScriptedTransport(response(payload=completion()))
        worker = self.fixture.worker(transport)
        worker.condition = "primary-kku-api"
        result = self.fixture.generate(worker)
        self.assertEqual(transport.calls[0]["body"]["model"], "claude-sonnet-5")
        self.assertEqual(result["requested_model_id"], "claude-sonnet-5")

    def test_primary_worker_rejects_other_sonnet_before_reservation_or_request(self):
        transport = ScriptedTransport(response(payload=completion()))
        worker = self.fixture.worker(transport)
        worker.condition = "primary-kku-api"
        worker.model = Model("claude-sonnet-5.5", "claude-sonnet-5.5", "Claude")
        with self.assertRaises(KKUError):
            self.fixture.generate(worker)
        self.assertFalse(transport.calls)
        self.assertFalse(worker.ledger.snapshot()["reservations"])
