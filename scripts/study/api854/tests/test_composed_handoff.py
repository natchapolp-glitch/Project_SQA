"""Champ CLI -> current Store -> Beam evaluation download, all providers mocked."""
from dataclasses import replace
import json
import unittest

from . import test_api_worker as fixture_module
from .test_aom_store_integration import StoreBridge
from .test_beam_queue import mock_stage_gate
from .test_champ_queue import FAKE_QUEUE_TOKEN
from scripts.study.api854 import queue_server
from scripts.study.api854.api_worker import WorkerBlocked, FrozenSettings, APIWorker
from scripts.study.api854.champ_queue import ChampQueueClient, QueueAccess
from scripts.study.api854.beam_queue import download_generation
from scripts.study.api854.queue_client import QueueClient
from scripts.study.api854.evaluate_worker import validate_lineage
from scripts.study.api854.common import sha256, ROOT, read_json
from scripts.study.api854.team_queue import TeamQueueClient


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
        self.publish_for_owner("champ")

    def test_api_coordinator_sends_aom_work_without_changing_owner(self):
        self.publish_for_owner("aom")

    def test_api_coordinator_sends_beam_work_without_changing_owner(self):
        self.publish_for_owner("beam")

    def publish_for_owner(self, owner):
        f = self.fixture
        store = queue_server.Store(f.root / 'composed-store', {'champ': FAKE_QUEUE_TOKEN})
        self.addCleanup(store.db.close)
        store.seed({'bugs': [{'project': 'Lang', 'bug_id': 1, 'owner': owner}]},
                   'mock-only', f.settings.protocol_hash, False, protocol=mock_stage_gate())
        claim = store.claim({'worker_id': 'mock-prepare', 'stage': 'prepare',
                             'owner': owner, 'approaches': ['kku-claude']}, 'champ')
        envelope = QueueClient.envelope(claim)
        artifacts = [store.upload(claim['job']['job_id'], envelope, artifact['name'], data)
                     for artifact, data in f.queue_transport.artifacts.values()]
        metadata = f.claim['job']['payload']['stage_history'][0]['metadata']
        store.complete(claim['job']['job_id'], {**envelope, 'outcome': 'prepared',
            'artifact_ids': [a['artifact_id'] for a in artifacts], 'metadata': metadata})
        (f.root/'runner-plan.json').write_text(json.dumps(read_json(ROOT/'experiments/configs/api854-20261003/runner-plan.v1.json')),encoding='utf-8')
        (f.root/'access.private.json').write_text(json.dumps({'role':'champ','schema_version':'1.0','base_url':'https://queue.example.test','worker_token':FAKE_QUEUE_TOKEN}),encoding='utf-8')
        f.queue = TeamQueueClient.from_plan(f.root/'access.private.json', f.root/'runner-plan.json', 'champ-pc1',
            owner=owner, stage='generate', approaches=['kku-claude'], protocol_path=f.protocol_path, condition='mock-integration')
        f.queue.transport, f.queue.mock_mode = StoreBridge(queue_server, store), True
        f.kku_response['choices'][0]['message']['content'] = (
            '```java\nimport org.junit.Test; import static org.junit.Assert.*;\n'
            'public class ExampleTest { @Test public void example() { assertEquals(1, 1); } }\n```')
        worker = APIWorker(f.queue, f.kku, f.ledger, f.settings, approach='kku-claude',
            bucket='sonnet', window='window', artifact_root=f.root/'artifacts',private_root=f.root/'private',
            worker_id='champ-pc1',suite_resolver=f.suite_resolver,model=f.worker().model,condition='mock-integration',owner=owner)
        result = worker.once()
        self.assertEqual(result['state'], 'published')
        self.assertEqual(len(f.kku_calls), 1)
        evaluation = QueueClient.claim(f.queue, 'mock-evaluate', 'evaluate', ['kku-claude'], owner=owner)
        self.assertEqual(evaluation['job']['owner'], owner)
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
