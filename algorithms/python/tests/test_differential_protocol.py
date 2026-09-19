import base64
import json
import unittest
from pathlib import Path
from types import SimpleNamespace
from unittest.mock import patch

import differential_lang1


class DifferentialProtocolTests(unittest.TestCase):
    def test_value_protocol_preserves_type_and_value(self):
        payload = {"status": "value", "type": "java.lang.Integer", "value_base64": base64.b64encode(b"42").decode()}
        completed = SimpleNamespace(returncode=0, stdout=json.dumps(payload), stderr="")
        with patch("differential_lang1.subprocess.run", return_value=completed):
            outcome = differential_lang1.execute("unused", Path("unused"), "0x2a")
        self.assertEqual(outcome, {"status": "value", "type": "java.lang.Integer", "value": "42"})

    def test_exception_protocol_preserves_type_and_message(self):
        payload = {"status": "exception", "type": "java.lang.NumberFormatException", "message_base64": base64.b64encode(b"bad input").decode()}
        completed = SimpleNamespace(returncode=0, stdout=json.dumps(payload), stderr="")
        with patch("differential_lang1.subprocess.run", return_value=completed):
            outcome = differential_lang1.execute("unused", Path("unused"), "bad")
        self.assertEqual(outcome, {"status": "exception", "type": "java.lang.NumberFormatException", "message": "bad input"})

    def test_infrastructure_outcomes_are_not_comparable(self):
        self.assertFalse(
            differential_lang1.is_comparable(
                {"status": "execution_error"},
                {"status": "value", "type": "java.lang.Integer", "value": "1"},
            )
        )

    def test_harness_errors_make_the_run_unacceptable(self):
        rows = [{
            "input": "0x1",
            "comparable": False,
            "different": False,
        }]
        summary = differential_lang1.summarize(rows)
        self.assertEqual(summary["harness_errors"], 1)
        self.assertEqual(summary["unique_differences"], 0)
        with self.assertRaisesRegex(SystemExit, "1 harness errors"):
            differential_lang1.ensure_no_harness_errors(summary)
