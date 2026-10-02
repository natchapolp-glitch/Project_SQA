from pathlib import Path
import tempfile
import unittest
from unittest.mock import patch

from scripts.study.api854.common import write_json
from scripts.study.api854.queue_connection import connection_check


class ConnectionTests(unittest.TestCase):
    def setUp(self):
        self.temp = tempfile.TemporaryDirectory()
        self.addCleanup(self.temp.cleanup)
        self.access = Path(self.temp.name) / "access.private.json"
        write_json(self.access, {"role": "beam", "schema_version": "1.0", "worker_token": "fixture-secret",
                                 "base_url": "https://queue.example/"})

    def test_check_is_read_only_and_omits_token_and_lease_details(self):
        responses = [{"service": "sqa-api854-queue", "ready": True}, {"schema_version": "1.0"},
                     {"schema_version": "1.0", "jobs": [], "attempts": [{"lease_token": "fixture-lease"}]}]
        with patch("scripts.study.api854.queue_connection.QueueClient.request", side_effect=responses) as request:
            result = connection_check(self.access)
        self.assertEqual([c.args for c in request.call_args_list], [("GET", "/health"), ("GET", "/v1/schema"), ("GET", "/v1/status")])
        self.assertEqual(result["jobs"], 0)
        self.assertEqual(result["mutations_sent"], 0)
        self.assertNotIn("fixture-secret", str(result))
        self.assertNotIn("fixture-lease", str(result))

    def test_server_error_cannot_echo_worker_secret(self):
        with patch("scripts.study.api854.queue_connection.QueueClient.request", side_effect=RuntimeError("bad fixture-secret")):
            with self.assertRaises(RuntimeError) as error:
                connection_check(self.access)
        self.assertNotIn("fixture-secret", str(error.exception))

    def test_changed_schema_is_rejected_before_use(self):
        with patch("scripts.study.api854.queue_connection.QueueClient.request", side_effect=[
            {"service": "sqa-api854-queue", "ready": True}, {"schema_version": "2.0"}, {"schema_version": "1.0"}]):
            with self.assertRaisesRegex(ValueError, "schema changed"):
                connection_check(self.access)

    def test_http_access_file_cannot_transmit_token(self):
        self.access.write_text('{"role":"beam","schema_version":"1.0","worker_token":"fixture-secret","base_url":"http://queue.example"}')
        with patch("scripts.study.api854.queue_connection.QueueClient.request") as request:
            with self.assertRaises(ValueError):
                connection_check(self.access)
        request.assert_not_called()


if __name__ == "__main__":
    unittest.main()
