"""Champ CLI -> current Store -> Beam evaluation download, all providers mocked."""
from dataclasses import replace
import json
import unittest

from . import test_api_worker as fixture_module
from .test_aom_store_integration import StoreBridge
from .test_beam_queue import mock_stage_gate
from .test_champ_queue import FAKE_QUEUE_TOKEN
from scripts.study.api854 import queue_server
from scripts.study.api854.api_worker import WorkerBlocked, FrozenSettings
from scripts.study.api854.champ_queue import ChampQueueClient, QueueAccess
from scripts.study.api854.beam_queue import download_generation
from scripts.study.api854.queue_client import QueueClient
from scripts.study.api854.evaluate_worker import validate_lineage
from scripts.study.api854.common import sha256


class ComposedHandoffTests(unittest.TestCase):
    def setUp(self):
        self.fixture = fixture_module.APIWorkerTests()
        self.fixture.setUp()
        self.addCleanup(self.fixture.tearDown)
        self.fixture.settings = replace(self.fixture.settings, handoff_contract="beam-v1", protocol={
            "suite_packaging": "beam-java-suite-v1", "compatibility_policy": "none", "test_method_cap": 30,
            "model_policy": {"kku-claude": {"kku_model_id": "claude-sonnet-5"}}})
        history = self.fixture.claim['job']['payload']['stage_history'][0]['metadata']
        history['fixed_source_sha256'] = {f['path']: f['sha256'] for f in self.fixture.manifest['source_files']}
        history['context_source_hash'] = self.fixture.manifest['source_hash']

    def test_missing_mapping_blocks_before_claim_and_provider_request(self):
        del self.fixture.claim['job']['payload']['stage_history'][0]['metadata']['fixed_source_sha256']
        with self.assertRaisesRegex(WorkerBlocked, 'beam_preparation_lineage_mismatch'):
            self.fixture.worker().once()
        self.assertEqual(self.fixture.queue_transport.claims, 0)
        self.assertEqual(self.fixture.kku_calls, [])

    def test_cli_publishes_suite_and_lineage_accepted_by_evaluator(self):
        f = self.fixture
        store = queue_server.Store(f.root / 'composed-store', {'champ': FAKE_QUEUE_TOKEN})
        self.addCleanup(store.db.close)
        store.seed({'bugs': [{'project': 'Lang', 'bug_id': 1, 'owner': 'champ'}]},
                   'mock-only', f.settings.protocol_hash, False, protocol=mock_stage_gate())
        claim = store.claim({'worker_id': 'mock-prepare', 'stage': 'prepare',
                             'owner': 'champ', 'approaches': ['kku-claude']}, 'champ')
        envelope = QueueClient.envelope(claim)
        artifacts = [store.upload(claim['job']['job_id'], envelope, artifact['name'], data)
                     for artifact, data in f.queue_transport.artifacts.values()]
        metadata = f.claim['job']['payload']['stage_history'][0]['metadata']
        store.complete(claim['job']['job_id'], {**envelope, 'outcome': 'prepared',
            'artifact_ids': [a['artifact_id'] for a in artifacts], 'metadata': metadata})
        f.queue = ChampQueueClient(QueueAccess('https://queue.example.test', FAKE_QUEUE_TOKEN),
                                   transport=StoreBridge(queue_server, store), mock_mode=True)
        f.kku_response['choices'][0]['message']['content'] = (
            '```java\nimport org.junit.Test; import static org.junit.Assert.*;\n'
            'public class ExampleTest { @Test public void example() { assertEquals(1, 1); } }\n```')
        result = f.worker().once()
        self.assertEqual(result['state'], 'published')
        self.assertEqual(len(f.kku_calls), 1)
        evaluation = QueueClient.claim(f.queue, 'mock-evaluate', 'evaluate', ['kku-claude'], owner='champ')
        suite, lineage = download_generation(f.queue, evaluation, f.root / 'download')
        job = {**lineage['job'], 'attempt_id': evaluation['attempt_id']}
        validate_lineage(job, lineage, suite, {'test_method_cap': 30})
        self.assertEqual(sha256(suite), lineage['suite_sha256'])
        self.assertNotEqual(job['attempt_id'], lineage['job']['attempt_id'])
        self.assertIsNone(lineage['executed_test_count'])
        self.assertFalse(evaluation['job']['payload']['stage_history'][-1]['metadata']['usable'])

    def test_frozen_loader_rejects_beam_contract_without_frozen_processing_fields(self):
        path = self.fixture.protocol_path
        protocol = json.loads(path.read_text(encoding='utf-8'))
        protocol['generation']['handoff_contract'] = 'beam-v1'
        path.write_text(json.dumps(protocol), encoding='utf-8')
        with self.assertRaisesRegex(WorkerBlocked, 'beam_processing_contract_not_frozen'):
            FrozenSettings.load(path)
