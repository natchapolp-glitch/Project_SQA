from copy import deepcopy
from dataclasses import replace
from datetime import datetime, timedelta, timezone
import io
import json
from pathlib import Path
import tarfile
import tempfile
import unittest

from scripts.study.api854.api_worker import APIWorker, FrozenSettings, WorkerBlocked, resolve_prepared_job
from scripts.study.api854.champ_queue import ChampQueueClient, QueueAccess
from scripts.study.api854.kku_client import Account, KKUClient, Model, digest
from scripts.study.api854.models import load_selection
from scripts.study.api854.quota import QuotaBlocked, QuotaLedger
from .test_champ_queue import claim_fixture, FAKE_QUEUE_TOKEN
from .test_client import response
from .test_lease import RenewTransport
from .test_worker import completion


class WorkerQueueTransport(RenewTransport):
    def __init__(self, claim, manifest, prompt, prompt_policy):
        super().__init__()
        self.claim_data = claim
        self.claims = 0
        artifacts = []
        for artifact_id, name, data in [('prepcontext', 'context-manifest.json', json.dumps(manifest).encode()),
                                        ('prepprompt', 'prompt.md', prompt.encode())]:
            artifact = {'artifact_id': artifact_id, 'uri': '/v1/artifacts/' + artifact_id,
                        'name': name, 'sha256': digest(data), 'size': len(data)}
            self.artifacts[artifact_id] = (artifact, data)
            artifacts.append(artifact)
        claim['job']['payload'] = {'stage_history': [{'stage': 'prepare', 'outcome': 'prepared',
            'metadata': {'source_sha256': manifest['source_hash'], 'prompt_sha256': digest(prompt.encode()),
                         'prompt_policy_id': prompt_policy}, 'artifacts': artifacts}]}

    def __call__(self, method, url, headers, body, timeout):
        if url.endswith('/status'):
            job = deepcopy(self.claim_data['job'])
            job['state'] = 'queued'
            return response(payload={'schema_version': '1.0', 'jobs': [job]})
        if url.endswith('/claim'):
            data = json.loads(body)
            assert data['owner'] == 'champ'
            assert data['stage'] == 'generate'
            assert data['approaches'] == ['kku-claude']
            self.claims += 1
            return response(payload=self.claim_data)
        return super().__call__(method, url, headers, body, timeout)


class APIWorkerTests(unittest.TestCase):
    def setUp(self):
        self.temp = tempfile.TemporaryDirectory()
        self.root = Path(self.temp.name)
        protocol = {'approval_state': 'frozen', 'models': load_selection(),
                    'start_at': (datetime.now(timezone.utc) - timedelta(hours=1)).isoformat(),
                    'generation_cutoff_at': (datetime.now(timezone.utc) + timedelta(hours=1)).isoformat(),
                    'generation': {'input_contract': 'champ-generation-input-v1', 'temperature': 0,
                        'max_tokens': 4000, 'prompt_token_reserve': 1000, 'context_policy_id': 'mock-context-v1',
                        'prompt_policy_id': 'mock-prompt-v1', 'suite_policy_id': 'mock-suite-v1',
                        'suite_resolver': 'scripts.study.api854.tests.test_api_worker:mock_suite_resolver'}}
        self.protocol_path = self.root / 'protocol.json'
        self.protocol_path.write_text(json.dumps(protocol), encoding='utf-8')
        self.settings = FrozenSettings.load(self.protocol_path)
        self.claim = claim_fixture()
        self.claim['job']['protocol_hash'] = self.settings.protocol_hash
        files = [{'path': 'Example.java', 'bytes': 16, 'sha256': digest(b'class Example {}')}]
        self.manifest = {'schema': 'champ-fixed-context-v1-proposal', 'project': 'Lang', 'bug_id': 1,
            'revision': '1f', 'selection_policy_id': 'mock-context-v1', 'source_files': files,
            'source_hash': digest(json.dumps(files, sort_keys=True, separators=(',', ':')).encode()),
            'contains_execution_logs': False}
        self.queue_transport = WorkerQueueTransport(self.claim, self.manifest, 'fixed source prompt', 'mock-prompt-v1')
        self.queue = ChampQueueClient(QueueAccess('https://queue.example.test', FAKE_QUEUE_TOKEN),
                                     transport=self.queue_transport, mock_mode=True)
        self.ledger = QuotaLedger(self.root / 'quota.sqlite')
        self.ledger.observe('a01', 'sonnet', 'window', remaining=200000,
            reset_at=(datetime.now(timezone.utc) + timedelta(hours=1)).isoformat(), evidence_ref='mock-only')
        self.ledger.activate_initial('a01', 'sonnet')
        self.kku_calls = []
        self.kku_response = completion()
        self.kku = KKUClient(Account('a01', 'mock-only-key'), transport=self.kku_transport)

    def tearDown(self):
        self.temp.cleanup()

    def kku_transport(self, method, url, headers, body, timeout):
        self.kku_calls.append(json.loads(body))
        return response(payload=self.kku_response)

    def suite_resolver(self, result):
        path = Path(result['artifact_path']) / 'suite.tar.bz2'
        with tarfile.open(path, 'w:bz2') as archive:
            source = b'class Example {}'
            member = tarfile.TarInfo('Example.java')
            member.size = len(source)
            archive.addfile(member, io.BytesIO(source))
        return path

    def worker(self):
        return APIWorker(self.queue, self.kku, self.ledger, self.settings, approach='kku-claude',
            bucket='sonnet', window='window', artifact_root=self.root / 'artifacts',
            private_root=self.root / 'private', worker_id='champ-pc1', suite_resolver=self.suite_resolver,
            model=Model('claude-sonnet-5', 'claude-sonnet-5', 'Claude'), condition='mock-integration')

    def test_check_downloads_hashed_preparation_without_claim_or_kku_request(self):
        before = self.ledger.snapshot()
        result = self.worker().check()
        self.assertEqual(result['state'], 'ready')
        self.assertEqual(self.queue_transport.claims, 0)
        self.assertEqual(self.kku_calls, [])
        self.assertEqual(self.ledger.snapshot(), before)
        self.assertTrue(all(call['method'] == 'GET' for call in self.queue_transport.calls))

    def test_once_claims_generates_and_publishes_exact_settings_and_hashed_suite(self):
        result = self.worker().once()
        self.assertEqual(result['state'], 'published')
        self.assertEqual(len(self.kku_calls), 1)
        self.assertEqual(self.kku_calls[0]['temperature'], 0)
        self.assertEqual(self.kku_calls[0]['max_tokens'], 4000)
        self.assertEqual(self.kku_calls[0]['model'], 'claude-sonnet-5')
        self.assertEqual(result['evaluation_status'], 'not_attempted')
        self.assertTrue((self.root / 'private' / 'claim-queue-attempt-001.json').is_file())
        directory = Path(result['artifact_path'])
        self.assertTrue((directory / 'queue-complete-receipt.json').is_file())
        self.assertEqual(json.loads((directory / 'generation-result.json').read_text())['condition'], 'mock-integration')
        self.assertEqual(self.queue_transport.calls[-1]['body']['outcome'], 'generated')

    def test_missing_or_insufficient_quota_never_claims_or_calls_kku(self):
        self.ledger.observe('a01', 'sonnet', 'window', remaining=100,
            reset_at=(datetime.now(timezone.utc) + timedelta(hours=1)).isoformat(), evidence_ref='mock-only-low')
        with self.assertRaises(QuotaBlocked):
            self.worker().once()
        self.assertEqual(self.queue_transport.claims, 0)
        self.assertFalse(self.kku_calls)

    def test_draft_wrong_models_and_missing_settings_cannot_freeze(self):
        for change in ({'approval_state': 'draft'}, {'models': {}}, {'generation': {}}, {'start_at': '2026-01-01'}):
            protocol = json.loads(self.protocol_path.read_text())
            protocol.update(change)
            self.protocol_path.write_text(json.dumps(protocol))
            with self.subTest(change=change), self.assertRaises(WorkerBlocked):
                FrozenSettings.load(self.protocol_path)
            # Restore complete fixture for the next independent case.
            self.protocol_path.write_text(json.dumps({**protocol, 'approval_state': 'frozen',
                'models': self.settings.models,
                'start_at': self.settings.start_at.isoformat(),
                'generation': {'input_contract': 'champ-generation-input-v1', 'temperature': 0,
                    'max_tokens': 4000, 'prompt_token_reserve': 1000, 'context_policy_id': 'mock-context-v1',
                    'prompt_policy_id': 'mock-prompt-v1', 'suite_policy_id': 'mock-suite-v1',
                    'suite_resolver': self.settings.suite_resolver}}))

    def test_mixed_protocol_and_unapproved_prompt_block_before_claim(self):
        self.claim['job']['protocol_hash'] = 'f' * 64
        with self.assertRaises(WorkerBlocked):
            self.worker().once()
        self.claim['job']['protocol_hash'] = self.settings.protocol_hash
        self.claim['job']['payload']['stage_history'][0]['metadata']['prompt_policy_id'] = 'different'
        with self.assertRaises(WorkerBlocked):
            self.worker().once()
        self.assertEqual(self.queue_transport.claims, 0)
        self.assertFalse(self.kku_calls)

    def test_preparation_hash_corruption_is_detected(self):
        artifact, data = self.queue_transport.artifacts['prepprompt']
        self.queue_transport.artifacts['prepprompt'] = (artifact, b'tampered prompt')
        with self.assertRaises(ValueError):
            self.worker().once()
        self.assertEqual(self.queue_transport.claims, 0)
        self.assertFalse(self.kku_calls)

    def test_outside_window_keeps_jobs_unclaimed(self):
        self.settings = replace(self.settings, generation_cutoff_at=datetime.now(timezone.utc) - timedelta(seconds=1))
        with self.assertRaises(WorkerBlocked):
            self.worker().once()
        self.assertEqual(self.queue_transport.claims, 0)
        self.assertFalse(self.kku_calls)

    def test_processed_job_does_not_send_second_generation(self):
        self.worker().once()
        with self.assertRaises(QuotaBlocked):
            self.worker().once()
        self.assertEqual(self.queue_transport.claims, 1)
        self.assertEqual(len(self.kku_calls), 1)

    def test_provider_timeout_is_published_as_unknown_and_keeps_quota_held(self):
        def timeout(*args):
            self.kku_calls.append('unknown')
            raise TimeoutError()
        self.kku.transport = timeout
        result = self.worker().once()
        self.assertEqual(result['generation_outcome'], 'needs_reconciliation')
        self.assertEqual(self.queue_transport.calls[-1]['body']['outcome'], 'needs_reconciliation')
        self.assertEqual(self.ledger.snapshot()['reservations'][0]['state'], 'needs_reconciliation')
        self.assertEqual(len(self.kku_calls), 1)


def mock_suite_resolver(result):
    raise AssertionError('Offline fixture must explicitly inject its own suite resolver')
