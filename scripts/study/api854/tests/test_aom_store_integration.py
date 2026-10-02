"""Offline integration against pinned Aom Store, with mocked KKU only.

Skips when the team's pinned Git object is absent. Does not contact any server,
write a production queue, use private credentials, or measure Java validity.
"""
import json
from pathlib import Path
import subprocess
import types
import unittest
from urllib.parse import urlsplit

from scripts.study.api854.champ_queue import ChampQueueClient, QueueAccess
from scripts.study.api854.kku_client import HTTPResponse
from . import test_api_worker
from .test_champ_queue import FAKE_QUEUE_TOKEN
from .test_client import response

AOM_COMMIT = '2e419e421c2fcc75dbfd516e04ed8ce27aafa463'


class StoreBridge:
    def __init__(self, module, store):
        self.module, self.store = module, store

    def __call__(self, method, url, headers, body, timeout):
        assert headers['Authorization'] == 'Bearer ' + FAKE_QUEUE_TOKEN
        path = urlsplit(url).path
        try:
            if path == '/health':
                return response(payload={'ready': True, 'schema_version': '1.0'})
            if path == '/v1/schema':
                return response(payload=self.module.schema())
            if path == '/v1/status':
                return response(payload=self.store.status())
            if path.startswith('/v1/artifacts/'):
                artifact_id = path.rsplit('/', 1)[1]
                row = self.store.db.execute('SELECT path FROM artifacts WHERE artifact_id=?', (artifact_id,)).fetchone()
                return HTTPResponse(200, {}, (self.store.root / row['path']).read_bytes())
            if path == '/v1/jobs/claim':
                return response(payload=self.store.claim(json.loads(body), 'champ'))
            job_id, action = path.split('/')[-2:]
            if action == 'artifacts':
                envelope = {'attempt_id': headers['X-Attempt-ID'], 'lease_token': headers['X-Lease-Token'],
                            'lease_version': int(headers['X-Lease-Version'])}
                result = self.store.upload(job_id, envelope, headers['X-Artifact-Name'], body)
            elif action == 'renew':
                result = self.store.renew(job_id, json.loads(body))
            else:
                result = self.store.complete(job_id, json.loads(body), failure_only=action == 'fail')
            return response(payload=result)
        except self.module.APIError as error:
            return response(error.status, {'error': error.code})


class AomStoreIntegrationTests(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        repository = Path(__file__).resolve().parents[4]
        try:
            result = subprocess.run(['git', '-c', f'safe.directory={repository}', 'show', AOM_COMMIT + ':scripts/study/api854/queue_server.py'],
                                    cwd=repository, capture_output=True, check=True)
        except (OSError, subprocess.CalledProcessError):
            raise unittest.SkipTest('Pinned Aom queue object not present; fetch the team commit for integration')
        cls.aom = types.ModuleType('offline_aom_queue')
        exec(compile(result.stdout, '<Aom Store ' + AOM_COMMIT + '>', 'exec'), cls.aom.__dict__)

    def test_prepared_artifacts_to_generation_to_evaluation_with_actual_aom_transactions(self):
        fixture = test_api_worker.APIWorkerTests()
        fixture.setUp()
        store = self.aom.Store(fixture.root / 'isolated-aom', {'champ': FAKE_QUEUE_TOKEN,
            'beam': 'fake_beam_access_only', 'admin': 'fake_admin_access_only'})
        try:
            count = store.seed({'bugs': [{'project': 'Lang', 'bug_id': 1, 'owner': 'champ'}]},
                               'mock-only', fixture.settings.protocol_hash, pilot=False)
            self.assertEqual(count, 4)
            prepared = store.claim({'worker_id': 'mock-prepare', 'stage': 'prepare', 'owner': 'champ',
                                    'approaches': ['kku-claude']}, 'champ')
            envelope = {key: prepared[key] for key in ('attempt_id', 'lease_token', 'lease_version')}
            artifacts = [store.upload(prepared['job']['job_id'], envelope, artifact['name'], data)
                         for artifact, data in fixture.queue_transport.artifacts.values()]
            metadata = fixture.claim['job']['payload']['stage_history'][0]['metadata']
            store.complete(prepared['job']['job_id'], {**envelope, 'outcome': 'prepared',
                           'artifact_ids': [artifact['artifact_id'] for artifact in artifacts], 'metadata': metadata})
            fixture.queue = ChampQueueClient(QueueAccess('https://queue.example.test', FAKE_QUEUE_TOKEN),
                                             transport=StoreBridge(self.aom, store), mock_mode=True)
            result = fixture.worker().once()
            self.assertEqual(result['state'], 'published')
            self.assertEqual(len(fixture.kku_calls), 1)
            job = next(job for job in store.status()['jobs'] if job['approach'] == 'kku-claude')
            self.assertEqual(job['stage'], 'evaluate')
            self.assertEqual(job['state'], 'queued')
            self.assertEqual(job['payload']['stage_history'][-1]['outcome'], 'generated')
            self.assertEqual(job['payload']['stage_history'][-1]['metadata']['condition'], 'mock-integration')
            suite = next(artifact for artifact in job['payload']['stage_history'][-1]['artifacts']
                         if artifact['name'] == 'suite.tar.bz2')
            self.assertEqual(suite['sha256'], job['payload']['stage_history'][-1]['metadata']['suite_sha256'])
            self.assertEqual(fixture.worker().check()['state'], 'idle')
            self.assertEqual(len(fixture.kku_calls), 1)
        finally:
            store.db.close()
            fixture.tearDown()
