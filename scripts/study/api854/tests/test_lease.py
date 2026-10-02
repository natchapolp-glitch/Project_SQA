from datetime import datetime, timedelta, timezone
from pathlib import Path
import tempfile
import threading
import time
import unittest
from unittest.mock import Mock

from scripts.study.api854.champ_queue import ChampQueueClient, QueueAccess, QueueError, QueueGenerationHandoff
from scripts.study.api854.generate_worker import GenerationJob, GenerationWorker
from scripts.study.api854.kku_client import Account, KKUClient, Model
from scripts.study.api854.lease import LeaseHeartbeat, generate_with_lease
from scripts.study.api854.quota import QuotaLedger
from .test_champ_queue import claim_fixture, FakeQueueTransport, FAKE_QUEUE_TOKEN, FAKE_LEASE_TOKEN
from .test_client import response
from .test_worker import completion


class RenewTransport(FakeQueueTransport):
    def __init__(self, *, failure=None):
        super().__init__()
        self.renewals, self.failure, self.guard = 0, failure, None

    def __call__(self, method, url, headers, body, timeout):
        if url.endswith('/renew'):
            import json
            envelope = json.loads(body)
            assert envelope['lease_token'] == FAKE_LEASE_TOKEN
            assert envelope['attempt_id'] == 'queue-attempt-001'
            assert envelope['lease_version'] == 3
            self.renewals += 1
            if self.failure is not None:
                return self.failure
            return response(payload={'lease_expires_at_unix': time.time() + envelope['lease_seconds']})
        if url.endswith(('/complete', '/fail')) and self.guard is not None:
            assert not self.guard._thread.is_alive(), 'Renew must stop before completion'
        return super().__call__(method, url, headers, body, timeout)


class LeaseTests(unittest.TestCase):
    def test_initial_renew_confirms_fence_and_updates_shared_expiry(self):
        claim = claim_fixture()
        client = Mock(renew=Mock(return_value={'lease_expires_at_unix': time.time() + 1200}))
        with LeaseHeartbeat(client, claim) as guard:
            self.assertGreater(claim['lease_expires_at_unix'], time.time() + 1000)
            guard.check()
        client.renew.assert_called_once_with(claim, lease_seconds=900)
        self.assertFalse(guard._thread.is_alive())

    def test_background_failure_is_sticky_and_stops_publication(self):
        claim, failed = claim_fixture(), threading.Event()
        def renew(*args, **kwargs):
            if client.renew.call_count == 1:
                return {'lease_expires_at_unix': time.time() + 900}
            failed.set()
            raise QueueError('lease_conflict', status=409)
        client = Mock()
        client.renew.side_effect = renew
        with LeaseHeartbeat(client, claim, interval_seconds=0.01) as guard:
            self.assertTrue(failed.wait(1))
            with self.assertRaises(QueueError) as error:
                guard.check()
            self.assertEqual(error.exception.kind, 'lease_conflict')
            with self.assertRaises(QueueError):
                guard.renew_once()
        self.assertEqual(client.renew.call_count, 2)

    def test_expired_claim_and_invalid_receipt_do_not_enable_sending(self):
        claim, client = claim_fixture(), Mock()
        claim['lease_expires_at_unix'] = time.time() - 1
        with self.assertRaises(QueueError):
            LeaseHeartbeat(client, claim).__enter__()
        client.renew.assert_not_called()
        for expiry in (None, True, float('nan'), time.time() - 1):
            client.renew.return_value = {'lease_expires_at_unix': expiry}
            with self.subTest(expiry=expiry), self.assertRaises(QueueError) as error:
                LeaseHeartbeat(client, claim_fixture()).__enter__()
            self.assertEqual(error.exception.kind, 'invalid_renewal_receipt')

    def test_arbitrary_transport_error_does_not_expose_credentials(self):
        client = Mock(renew=Mock(side_effect=RuntimeError('private example token')))
        with self.assertRaises(QueueError) as error:
            LeaseHeartbeat(client, claim_fixture()).__enter__()
        self.assertNotIn('private example token', str(error.exception))
        self.assertTrue(error.exception.unknown)

    def test_before_completion_joins_and_renews_without_changing_fence(self):
        claim = claim_fixture()
        client = Mock(renew=Mock(return_value={'lease_expires_at_unix': time.time() + 900}))
        with LeaseHeartbeat(client, claim) as guard:
            guard.before_complete()
            self.assertFalse(guard._thread.is_alive())
        self.assertEqual(client.renew.call_count, 2)
        self.assertEqual(claim['lease_version'], 3)


class LeasedGenerationTests(unittest.TestCase):
    def setUp(self):
        self.temp = tempfile.TemporaryDirectory()
        self.root, self.claim = Path(self.temp.name), claim_fixture()
        self.ledger = QuotaLedger(self.root / 'quota.sqlite')
        self.ledger.observe('a01', 'sonnet', 'window', remaining=200000,
                            reset_at=(datetime.now(timezone.utc) + timedelta(hours=1)).isoformat(),
                            evidence_ref='mock-only')
        self.ledger.activate_initial('a01', 'sonnet')
        self.job = GenerationJob('mock-only', 'a' * 64, 'Lang', 1, 'kku-claude', 'fixed context', 'b' * 64)
        self.queue = RenewTransport()
        self.client = ChampQueueClient(QueueAccess('https://queue.example.test', FAKE_QUEUE_TOKEN),
                                       transport=self.queue, mock_mode=True)
        self.guard = LeaseHeartbeat(self.client, self.claim)
        self.queue.guard = self.guard
        self.handoff = QueueGenerationHandoff(self.client, self.claim, worker_id='champ-pc1',
                                               lease_guard=self.guard)
        self.kku_calls = 0

    def tearDown(self):
        self.temp.cleanup()

    def worker(self, transport):
        return GenerationWorker(KKUClient(Account('a01', 'mock-only-key'), transport=transport), self.ledger,
                                artifact_root=self.root / 'artifacts',
                                model=Model('claude-sonnet-5', 'claude-sonnet-5', 'Claude'),
                                bucket='sonnet', window='window', handoff=self.handoff, condition='mock-integration')

    def transport(self, *args):
        self.kku_calls += 1
        return response(payload=completion(choices=[{'finish_reason': 'refusal',
                                                      'message': {'content': 'refused'}}]))

    def run_worker(self, transport=None, **options):
        return generate_with_lease(self.worker(transport or self.transport), self.job, self.guard,
                                   prompt_token_reserve=1000, max_tokens=4000, **options)

    def test_single_generation_uploads_evidence_then_stops_heartbeat_before_completion(self):
        result = self.run_worker()
        self.assertEqual(self.kku_calls, 1)
        self.assertEqual(result['queue_status'], 'published')
        self.assertEqual(result['attempt_id'], self.claim['attempt_id'])
        self.assertEqual(self.queue.renewals, 2)
        self.assertTrue(self.queue.calls[-1]['path'].endswith('/fail'))

    def test_failed_initial_renew_never_reserves_tokens_or_calls_kku(self):
        self.queue.failure = response(409, {'error': 'stale_or_invalid_lease'})
        with self.assertRaises(QueueError):
            self.run_worker()
        self.assertEqual(self.kku_calls, 0)
        self.assertEqual(self.ledger.snapshot()['reservations'], [])
        self.assertFalse((self.root / 'artifacts').exists())

    def test_lease_lost_during_kku_keeps_response_without_any_publication_or_retry(self):
        def lose_lease(*args):
            self.queue.failure = response(409, {'error': 'stale_or_invalid_lease'})
            with self.assertRaises(QueueError):
                self.guard.renew_once()
            return self.transport(*args)
        result = self.run_worker(lose_lease)
        self.assertEqual(result['queue_status'], 'publish_pending')
        self.assertEqual(self.kku_calls, 1)
        self.assertFalse(self.queue.calls)
        self.assertTrue((Path(result['artifact_path']) / 'response-evidence.json').is_file())
        self.assertEqual(self.ledger.snapshot()['reservations'][0]['state'], 'settled')

    def test_wrong_job_or_attempt_is_rejected_before_mutation(self):
        with self.assertRaises(ValueError):
            self.run_worker(attempt_id='different-attempt')
        self.claim['job']['bug_id'] = 2
        with self.assertRaises(ValueError):
            self.run_worker()
        self.assertEqual(self.queue.renewals, 0)
        self.assertEqual(self.kku_calls, 0)
