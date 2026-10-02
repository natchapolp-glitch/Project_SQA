"""Actual loopback queue, mocked provider, immutable suite handoff to evaluator."""
from datetime import datetime, timedelta, timezone
from dataclasses import replace
import json
from pathlib import Path
import tarfile
import unittest
from unittest.mock import patch

from scripts.study.api854 import common, configuration, champ_bridge, evaluate_worker
from scripts.study.api854.api_worker import APIWorker, FrozenSettings, load_suite_resolver
from scripts.study.api854.beam_queue import FencedPublisher, download_generation
from scripts.study.api854.champ_queue import ChampQueueClient
from scripts.study.api854.kku_client import Account, KKUClient, Model, digest, http_transport
from scripts.study.api854.lease import LeaseHeartbeat
from scripts.study.api854.models import load_selection
from scripts.study.api854.quota import QuotaLedger
from scripts.study.api854.queue_client import QueueClient
from scripts.study.api854.queue_worker import local_job
from . import test_beam_queue
from .test_client import response
from .test_worker import completion


class ChampBridgeTests(unittest.TestCase):
    def setUp(self):
        test_beam_queue.QueueIntegrationTests.setUp(self)
        with self.store.db:
            self.store.db.execute("DELETE FROM jobs")
        self.store.credentials['champ'] = 'offline-champ-fixture-only-123456789'
        self.client = ChampQueueClient.__new__(ChampQueueClient)
        QueueClient.__init__(self.client, f'http://127.0.0.1:{self.server.server_port}', self.store.credentials['champ'])
        self.client.transport, self.client.mock_mode = http_transport, True
        self.protocol = configuration.proposal()
        self.protocol.update(status='frozen', approval_state='frozen', models=load_selection(),
            start_at=(datetime.now(timezone.utc) - timedelta(hours=1)).isoformat(),
            generation_cutoff_at=(datetime.now(timezone.utc) + timedelta(hours=1)).isoformat(),
            generation={'input_contract': 'champ-generation-input-v1', 'temperature': 0,
                'max_tokens': 4000, 'prompt_token_reserve': 2000,
                'context_policy_id': self.protocol['context_selection'],
                'prompt_policy_id': 'beam-fixed-targets-junit4-v1',
                'suite_policy_id': 'beam-java-suite-v1', 'suite_resolver': champ_bridge.RESOLVER_SPEC,
                'owners': ['champ', 'beam', 'aom']})
        self.protocol_path = self.root / 'shared-protocol.json'
        common.write_json(self.protocol_path, self.protocol)
        self.settings = FrozenSettings.load(self.protocol_path)
        self.store.seed({'bugs': [{'project': 'Lang', 'bug_id': 4, 'owner': 'champ'}]},
                        'mock-only', self.settings.protocol_hash, False, protocol=test_beam_queue.mock_stage_gate())
        self.calls = []
        self.content = 'package example; public class ExampleTest { @org.junit.Test public void target() { org.junit.Assert.assertEquals(1, 1); } }\r\n'
        self.kku = KKUClient(Account('a01', 'offline-kku-fixture-only-123456789'), transport=self.transport)
        self.ledger = QuotaLedger(self.root / 'quota.sqlite')
        self.ledger.observe('a01', 'sonnet', 'offline', remaining=200000,
            reset_at=(datetime.now(timezone.utc) + timedelta(hours=1)).isoformat(), evidence_ref='offline-only')
        self.ledger.activate_initial('a01', 'sonnet')
        self.sources = {'src/Target.java': digest(b'class Target {}')}
        files = [{'path': name, 'sha256': value, 'bytes': 15} for name, value in self.sources.items()]
        self.manifest = {'project': 'Lang', 'bug_id': 4, 'revision': '4f',
            'selection_policy_id': self.protocol['context_selection'], 'source_files': files,
            'source_hash': digest(json.dumps(files, sort_keys=True, separators=(',', ':')).encode()),
            'contains_execution_logs': False}
        self.prepare_metadata = {'fixed_source_sha256': self.sources, 'source_sha256': self.manifest['source_hash'],
            'context_source_hash': self.manifest['source_hash'], 'prompt_policy_id': 'beam-fixed-targets-junit4-v1',
            'prompt_sha256': digest(b'fixed-only offline fixture prompt'), 'target_count': 1}
        self.prepare_claim = self.client.claim('champ-fixture', 'prepare', ['kku-claude'])
        self.prepared = self.root / 'prepared'
        self.prepared.mkdir()
        common.write_json(self.prepared / 'context-manifest.json', self.manifest)
        (self.prepared / 'prompt.md').write_bytes(b'fixed-only offline fixture prompt')
        common.write_json(self.prepared / 'targets.json', {'targets': [{'class': 'example.Target', 'method': 'target'}],
                                                        'errors': {'fixed': [], 'buggy': []}})
        self.prepare_metadata['targets_sha256'] = common.sha256(self.prepared / 'targets.json')

    tearDown = test_beam_queue.QueueIntegrationTests.tearDown

    def publish_prepare(self):
        with LeaseHeartbeat(self.client, self.prepare_claim) as guard:
            FencedPublisher(self.client, self.prepare_claim, guard, self.root / 'prepare-publication').publish(
                'prepared', [p for p in self.prepared.iterdir() if p.is_file()], self.prepare_metadata)

    def transport(self, method, url, headers, body, timeout):
        self.calls.append(json.loads(body))
        return response(payload=completion(choices=[{'finish_reason': 'stop', 'message': {'content': self.content}}]))

    def worker(self, owner='champ'):
        return APIWorker(self.client, self.kku, self.ledger, self.settings, approach='kku-claude',
            bucket='sonnet', window='offline', artifact_root=self.root / 'artifacts',
            private_root=self.root / 'private', worker_id='champ-fixture',
            suite_resolver=load_suite_resolver(champ_bridge.RESOLVER_SPEC),
            model=Model('claude-sonnet-5', 'claude-sonnet-5', 'Claude'), condition='mock-integration', owner=owner)

    def test_api_coordinator_claims_beam_shard_only_when_explicitly_frozen(self):
        with self.store.db:
            self.store.db.execute("UPDATE jobs SET owner='beam'")
        self.publish_prepare()
        self.assertEqual(self.worker(owner='beam').once()['state'], 'published')
        self.assertEqual(len(self.calls), 1)
        self.assertTrue(all(j['owner'] == 'beam' for j in self.store.status()['jobs']))

    def test_undeclared_generation_owner_is_blocked_before_claim(self):
        self.settings = replace(self.settings, generation_owners=('champ',))
        with self.assertRaisesRegex(Exception, 'routing_not_frozen'):
            self.worker(owner='beam')
        self.assertFalse(self.calls)

    def test_explicit_receiver_context_preserves_modified_source_evaluator_lineage(self):
        policy = 'beam-modified-and-shared-receiver-java-v2-proposal'
        self.protocol['context_selection'] = policy
        self.protocol['generation']['context_policy_id'] = policy
        path = self.root / 'receiver-protocol.json'
        common.write_json(path, self.protocol)
        self.settings = FrozenSettings.load(path)
        with self.store.db:
            self.store.db.execute('UPDATE jobs SET protocol_hash=?', (self.settings.protocol_hash,))
        self.store.seed({'bugs': [{'project': 'Lang', 'bug_id': 4, 'owner': 'champ'}]},
                        'mock-only', self.settings.protocol_hash, False, protocol=test_beam_queue.mock_stage_gate())
        extra = {'src/example/ConcreteReceiver.java': digest(b'public class ConcreteReceiver {}')}
        self.manifest['selection_policy_id'] = policy
        self.manifest['source_files'].append({'path': next(iter(extra)), 'sha256': next(iter(extra.values())), 'bytes': 32})
        source_hash = digest(json.dumps(self.manifest['source_files'], sort_keys=True, separators=(',', ':')).encode())
        self.manifest['source_hash'] = source_hash
        (self.prepared / 'context-manifest.json').write_text(json.dumps(self.manifest))
        (self.prepared / 'targets.json').write_text(json.dumps({'targets': [{'class': 'example.ConcreteReceiver', 'method': 'target'}]}))
        self.prepare_metadata.update(source_sha256=source_hash, context_source_hash=source_hash,
            target_classes=['Target'], additional_receiver_source_sha256=extra,
            targets_sha256=common.sha256(self.prepared / 'targets.json'))
        self.publish_prepare()
        self.assertEqual(self.worker().once()['state'], 'published')
        claim = QueueClient.claim(self.client, 'champ-fixture', 'evaluate', ['kku-claude'], owner='champ')
        _, lineage = download_generation(self.client, claim, self.root / 'receiver-evaluation-input')
        self.assertEqual(lineage['fixed_source_sha256'], self.sources)
        self.assertEqual(lineage['context_source_hash'], source_hash)

    def test_cli_callable_publishes_unchanged_bare_java_suite_and_downloadable_lineage(self):
        self.publish_prepare()
        self.assertEqual(self.worker().check()['state'], 'ready')
        self.assertFalse(self.calls)
        generated = self.worker().once()
        self.assertEqual(generated['state'], 'published')
        self.assertEqual(len(self.calls), 1)
        claim = QueueClient.claim(self.client, 'champ-fixture', 'evaluate', ['kku-claude'], owner='champ')
        suite, lineage = download_generation(self.client, claim, self.root / 'evaluation-input')
        evaluate_worker.validate_lineage(local_job(claim), lineage, suite, self.protocol)
        with tarfile.open(suite, 'r:bz2') as archive:
            self.assertEqual(archive.getnames(), ['example/ExampleTest.java'])
            self.assertEqual(archive.extractfile(archive.getmembers()[0]).read(), self.content.encode())
        self.assertEqual(lineage['fixed_source_sha256'], self.sources)
        self.assertIsNone(lineage['executed_test_count'])
        self.assertNotEqual(lineage['job']['attempt_id'], claim['attempt_id'])
        self.assertTrue((self.root / 'private' / f"beam-input-{lineage['job']['attempt_id']}" / 'implementation/source-hashes.json').is_file())

    def test_missing_source_mapping_rejects_before_generation_claim_and_provider_call(self):
        del self.prepare_metadata['fixed_source_sha256']
        self.publish_prepare()
        with self.assertRaisesRegex(ValueError, 'mapping'):
            self.worker().once()
        self.assertFalse(self.calls)
        self.assertEqual(len(self.store.status()['attempts']), 1)

    def test_target_count_hash_mismatch_rejects_before_provider_call(self):
        self.prepare_metadata['target_count'] = 2
        self.publish_prepare()
        with self.assertRaisesRegex(ValueError, 'targets'):
            self.worker().once()
        self.assertFalse(self.calls)
        self.assertEqual(len(self.store.status()['attempts']), 1)

    def test_missing_shared_worker_protocol_or_changed_code_rejects_before_claim(self):
        self.publish_prepare()
        with patch('scripts.study.api854.common.implementation_matches', return_value=False):
            with self.assertRaisesRegex(ValueError, 'implementation'):
                self.worker().once()
        self.assertFalse(self.calls)
        self.assertEqual(len(self.store.status()['attempts']), 1)

    def test_no_tests_publishes_failure_without_suite_or_semantic_repair(self):
        self.content = 'public class NoTests {}'
        self.publish_prepare()
        self.assertEqual(self.worker().once()['state'], 'published')
        job = next(j for j in self.store.status()['jobs'] if j['approach'] == 'kku-claude')
        self.assertEqual(job['outcome'], 'generation_failed')
        self.assertNotIn('suite.tar.bz2', [a['name'] for a in job['payload']['stage_history'][-1]['artifacts']])
        self.assertEqual(len(self.calls), 1)

    def test_incomplete_second_fence_rejects_entire_output(self):
        self.content = '```java\n' + self.content + '```\n```java\npublic class Incomplete {'
        self.publish_prepare()
        self.worker().once()
        job = next(j for j in self.store.status()['jobs'] if j['approach'] == 'kku-claude')
        self.assertEqual(job['outcome'], 'generation_failed')
        self.assertEqual(len(self.calls), 1)

    def shared_prepare(self, *, eligible):
        from scripts.study.api854.context_export import export_context
        from scripts.study.api854.preparation import compose, POLICY, encoded
        fixed = self.root / 'shared-fixed'
        (fixed / 'src').mkdir(parents=True)
        (fixed / '.defects4j.config').write_bytes(b'pid=Lang\nvid=4f\n')
        (fixed / 'src/Target.java').write_bytes(b'class Target {}')
        self.prepared = self.root / 'shared-prepared'
        export_context(fixed, 'Lang', 4, ['src/Target.java'], self.prepared,
                       policy_id=POLICY['context_policy_id'])
        targets = [{'class': 'Target', 'constructor_types': '', 'method': 'target', 'parameter_types': ''}]
        self.prepare_metadata = compose(self.prepared, classes=['Target'], targets=targets if eligible else [])
        self.protocol['prepare_policy_sha256'] = digest(encoded(POLICY))
        self.protocol['generation'].update(prepare_contract='aom-beam-prepare-v2', handoff_contract='beam-v1',
            prompt_policy_id=POLICY['prompt_policy_id'], prompt_token_reserve=100000)
        self.protocol_path = self.root / 'shared-prepare-v2-protocol.json'
        common.write_json(self.protocol_path, self.protocol)
        self.settings = FrozenSettings.load(self.protocol_path)
        with self.store.db:
            self.store.db.execute('DELETE FROM attempts')
            self.store.db.execute('DELETE FROM jobs')
        self.store.seed({'bugs': [{'project': 'Lang', 'bug_id': 4, 'owner': 'champ'}]},
            'mock-only', self.settings.protocol_hash, False, protocol=test_beam_queue.mock_stage_gate())
        self.prepare_claim = self.client.claim('champ-fixture', 'prepare', ['kku-claude'])

    def test_shared_prepare_v2_bridge_publishes_hash_bound_evaluator_input(self):
        self.shared_prepare(eligible=True)
        self.publish_prepare()
        generated = self.worker().once()
        self.assertEqual(generated['state'], 'published')
        self.assertEqual(len(self.calls), 1)
        claim = QueueClient.claim(self.client, 'champ-fixture', 'evaluate', ['kku-claude'], owner='champ')
        suite, lineage = download_generation(self.client, claim, self.root / 'shared-evaluation')
        evaluate_worker.validate_lineage(local_job(claim), lineage, suite, self.protocol)
        self.assertEqual(lineage['context_source_hash'], self.prepare_metadata['context_source_hash'])
        self.assertEqual(lineage['fixed_source_sha256'], self.prepare_metadata['fixed_source_sha256'])
        self.assertEqual(lineage['job']['protocol_hash'], self.settings.protocol_hash)

    def test_shared_prepare_v2_pending_targets_block_before_claim_and_provider(self):
        from scripts.study.api854.api_worker import WorkerBlocked
        self.shared_prepare(eligible=False)
        self.publish_prepare()
        before = len(self.store.status()['attempts'])
        with self.assertRaisesRegex(WorkerBlocked, 'shared_preparation_not_ready'):
            self.worker().once()
        self.assertFalse(self.calls)
        self.assertEqual(len(self.store.status()['attempts']), before)
