from contextlib import redirect_stdout
from datetime import datetime, timedelta, timezone
import io
import json
from pathlib import Path
import tempfile
import unittest
from unittest.mock import patch

from scripts.study.api854.quota import QuotaLedger
from scripts.study.api854.quota_control import main


class QuotaControlTests(unittest.TestCase):
    def setUp(self):
        self.temp = tempfile.TemporaryDirectory()
        self.root = Path(self.temp.name)
        self.ledger_path = self.root / 'quota.sqlite'
        self.evidence = self.root / 'mock-observation.json'
        self.evidence.write_text('{"condition":"mock-only"}')

    def tearDown(self):
        self.temp.cleanup()

    def invoke(self, *args):
        stream = io.StringIO()
        with patch('sys.argv', ['quota_control', '--ledger', str(self.ledger_path), *args]), redirect_stdout(stream):
            code = main()
        return code, json.loads(stream.getvalue())

    def observe(self, account='a01'):
        return self.invoke('observe', '--account', account, '--bucket', 'sonnet', '--window', 'mock-window',
                           '--remaining', '10000', '--expires-at',
                           (datetime.now(timezone.utc) + timedelta(hours=1)).isoformat(),
                           '--evidence', str(self.evidence))

    def test_actual_observation_links_evidence_and_initial_route(self):
        code, _ = self.observe()
        self.assertEqual(code, 0)
        self.assertEqual(self.invoke('activate-initial', '--account', 'a01', '--bucket', 'sonnet')[0], 0)
        ledger = QuotaLedger(self.ledger_path)
        self.assertEqual(ledger.available('a01', 'sonnet', 'mock-window'), 10000)
        self.assertTrue(ledger.snapshot()['buckets'][0]['evidence_ref'].startswith('sha256:'))

    def test_request_event_does_not_enable_route_until_user_notification_ack(self):
        self.observe()
        self.observe('a02')
        self.invoke('activate-initial', '--account', 'a01', '--bucket', 'sonnet')
        code, event = self.invoke('request-switch', '--from-account', 'a01', '--to-account', 'a02',
                                  '--bucket', 'sonnet', '--reason', 'insufficient_budget')
        self.assertEqual(code, 0)
        ledger = QuotaLedger(self.ledger_path)
        self.assertEqual(ledger.snapshot()['routes'][0]['account'], 'a01')
        self.assertTrue((self.root / 'notifications' / f"{event['event_id']}.json").is_file())
        code, _ = self.invoke('acknowledge-notification', '--event-id', event['event_id'], '--receipt', '')
        self.assertEqual(code, 1)
        code, _ = self.invoke('acknowledge-notification', '--event-id', event['event_id'], '--receipt', 'mock-user-message-001')
        self.assertEqual(code, 0)
        self.assertEqual(ledger.snapshot()['routes'][0]['account'], 'a02')

    def test_missing_evidence_or_expired_observation_is_blocked(self):
        code, output = self.invoke('observe', '--account', 'a01', '--bucket', 'sonnet', '--window', 'mock',
                                  '--remaining', '100', '--expires-at', '2020-01-01T00:00:00Z',
                                  '--evidence', str(self.evidence))
        self.assertEqual(code, 1)
        self.assertEqual(output['kku_requests'], 0)
        self.assertFalse(QuotaLedger(self.ledger_path).snapshot()['buckets'])
