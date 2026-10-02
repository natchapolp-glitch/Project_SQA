import json
from pathlib import Path
import tempfile
import unittest
from unittest.mock import patch

from scripts.study.api854 import algorithm_worker, common, configuration, evaluate_worker, queue_worker
from scripts.study.api854.context_export import export_context
from scripts.study.api854.preparation import POLICY_V3, compose, digest, encoded, validate
from scripts.study.api854.api_worker import FrozenSettings, resolve_prepared_job
from scripts.study.api854.prepared_inputs import load
from scripts.study.api854.beam_queue import download_generation
from scripts.study.api854.queue_client import QueueClient
from scripts.study.api854.queue_worker import local_job
from . import test_champ_bridge as fixture
from .test_beam_queue import mock_stage_gate


def shared_context(root):
    fixed = root / 'fixed-v3'
    (fixed / 'src/example').mkdir(parents=True)
    (fixed / '.defects4j.config').write_bytes(b'pid=Lang\nvid=4f\n')
    (fixed / 'src/example/Target.java').write_bytes(b'package example; public abstract class Target {}\r\n')
    (fixed / 'src/example/Receiver.java').write_bytes(b'package example; public class Receiver extends Target {}\r\n')
    folder = root / 'prepared-v3'
    manifest = export_context(fixed, 'Lang', 4, ['src/example/Receiver.java','src/example/Target.java'],
        folder, policy_id=POLICY_V3['context_policy_id'])
    mapping = {'src/example/Target.java': digest((fixed / 'src/example/Target.java').read_bytes())}
    targets = [{'class':'example.Receiver','constructor_types':'','method':'target','parameter_types':''}]
    metadata = compose(folder, classes=['example.Target'], targets=targets, policy=POLICY_V3,
        modified_sources=mapping, fixture_classes=['example.Target','example.Receiver'])
    return fixed, folder, manifest, metadata, targets


class SharedV3Tests(unittest.TestCase):
    def test_receiver_partition_retains_exact_source_bytes_and_fixture_binding(self):
        with tempfile.TemporaryDirectory() as directory:
            _, folder, manifest, metadata, _ = shared_context(Path(directory))
            args = [manifest,metadata,*[(folder / n).read_bytes() for n in ('prompt.md','targets.json','prepare-policy.json')]]
            self.assertTrue(validate(*args, require_eligible=True))
            self.assertEqual(list(metadata['fixed_source_sha256']), ['src/example/Target.java'])
            self.assertEqual(list(metadata['additional_receiver_source_sha256']), ['src/example/Receiver.java'])
            self.assertIn(b'extends Target {}\r\n', (folder / 'prompt.md').read_bytes())
            metadata['additional_receiver_source_sha256'] = {}
            with self.assertRaisesRegex(ValueError,'receiver source mapping'):
                validate(*args)

    def test_fixture_inventory_mutation_is_rejected_even_with_rehashed_targets(self):
        with tempfile.TemporaryDirectory() as directory:
            _, folder, manifest, metadata, _ = shared_context(Path(directory))
            targets = json.loads((folder / 'targets.json').read_bytes())
            targets['fixture_classes'].append('example.Unreviewed')
            data = encoded(targets)
            metadata['targets_sha256'] = digest(data)
            with self.assertRaisesRegex(ValueError,'Fixture inventory'):
                validate(manifest,metadata,(folder / 'prompt.md').read_bytes(),data,(folder / 'prepare-policy.json').read_bytes())

    def test_standalone_algorithm_cannot_bypass_shared_prepare(self):
        protocol = configuration.proposal()
        protocol['generation'] = {'prepare_contract':POLICY_V3['contract']}
        with self.assertRaisesRegex(ValueError,'same bound preparation'):
            algorithm_worker.execute({'approach':'cmaes'},protocol,None,None,'defects4j')

    def bridge_fixture(self):
        f = fixture.ChampBridgeTests()
        f.setUp()
        self.addCleanup(f.tearDown)
        f.fixed_v3, f.prepared, self.manifest, f.prepare_metadata, self.targets = shared_context(f.root)
        f.protocol.update(context_selection=POLICY_V3['context_policy_id'], prepare_policy_sha256=digest(encoded(POLICY_V3)))
        f.protocol['generation'].update(prepare_contract=POLICY_V3['contract'], handoff_contract='beam-v1',
            context_policy_id=POLICY_V3['context_policy_id'], prompt_policy_id=POLICY_V3['prompt_policy_id'], prompt_token_reserve=100000)
        f.protocol_path = f.root / 'shared-v3-protocol.json'
        common.write_json(f.protocol_path,f.protocol)
        f.settings = FrozenSettings.load(f.protocol_path)
        with f.store.db:
            f.store.db.execute('DELETE FROM attempts')
            f.store.db.execute('DELETE FROM jobs')
        f.store.seed({'bugs':[{'project':'Lang','bug_id':4,'owner':'champ'}]},'mock-only',f.settings.protocol_hash,False,protocol=mock_stage_gate())
        f.prepare_claim = f.client.claim('champ-fixture','prepare',['kku-claude'])
        f.publish_prepare()
        return f

    def test_algorithm_queue_uses_shared_receiver_targets_before_probe_execution(self):
        f = self.bridge_fixture()
        claim = f.client.claim('champ-fixture','prepare',['cmaes'])
        from scripts.study.api854.lease import LeaseHeartbeat
        from scripts.study.api854.beam_queue import FencedPublisher
        with LeaseHeartbeat(f.client,claim) as guard:
            FencedPublisher(f.client,claim,guard,f.root/'prepare-algorithm').publish('prepared',
                [p for p in f.prepared.iterdir() if p.is_file()], f.prepare_metadata)
        def adapter(d4j,job,trees,output,timeout):
            output.mkdir()
            (output/'dir.src.classes.txt').write_text('src')
            (output/'classes.modified.txt').write_text('example.Target\n')
            (output/'fixture-classes.txt').write_text('example.Receiver\nexample.Target\n')
            return {'fixed_worktree':str(f.fixed_v3),'classes_file':str(output/'classes.modified.txt'),
                'classpath':'offline-fixture','targets_sha256':'b'*64,'source_sha256':{}}, [{**self.targets[0],'dimensions':0}]
        # Mock only environment/discovery/observations; generation and publication execute.
        # Rebuild the frozen test protocol before claiming the cmaes stage.
        f.protocol['worker_routing'] = {'generate':['champ']}
        path = f.root/'algorithm-shared-v3.json'
        common.write_json(path,f.protocol)
        with f.store.db:
            f.store.db.execute('UPDATE jobs SET protocol_hash=?',(common.sha256(path),))
        f.store.seed({'bugs':[{'project':'Lang','bug_id':4,'owner':'champ'}]},'mock-only',common.sha256(path),False,protocol=mock_stage_gate())
        with patch.object(algorithm_worker,'inspect_environment',return_value={'ready':True}), \
             patch.object(algorithm_worker,'prepare_adapter',side_effect=adapter), \
             patch('generate.observe',return_value={'status':'ok','outcome':'value:fixed'}):
            result = queue_worker.run_one(f.client,protocol_path=path,run_id='mock-only',stage='generate',
                approaches=['cmaes'],worker_id='champ-fixture',owner='champ',output=f.root/'cpu-run',
                results=f.root/'cpu-results',worktrees=f.root/'cpu-worktrees',condition='primary')
        self.assertEqual(result['outcome'],'generated')
        job = next(j for j in f.store.status()['jobs'] if j['approach']=='cmaes')
        self.assertEqual(job['owner'],'champ')
        self.assertEqual(job['payload']['stage_history'][-1]['metadata']['fixed_source_sha256'],f.prepare_metadata['fixed_source_sha256'])
        self.assertFalse(f.calls)

    def test_cpu_and_api_consume_identical_preparation_and_evaluator_mapping(self):
        f = self.bridge_fixture()
        job = next(j for j in f.store.status()['jobs'] if j['stage']=='generate' and j['approach']=='kku-claude')
        cpu = load(f.client,job,f.protocol)
        api = resolve_prepared_job(f.client,job,f.settings)
        self.assertEqual(cpu['metadata']['context_source_hash'],api.source_hash)
        self.assertEqual(cpu['targets'],self.targets)
        self.assertEqual(f.worker().once()['state'],'published')
        claim = QueueClient.claim(f.client,'champ-fixture','evaluate',['kku-claude'],owner='champ')
        suite,lineage = download_generation(f.client,claim,f.root/'evaluation-v3')
        evaluate_worker.validate_lineage(local_job(claim),lineage,suite,f.protocol)
        self.assertEqual(lineage['fixed_source_sha256'],f.prepare_metadata['fixed_source_sha256'])
        self.assertNotIn('src/example/Receiver.java',lineage['fixed_source_sha256'])
        self.assertEqual(lineage['context_source_hash'],f.prepare_metadata['context_source_hash'])

    def test_rebound_fixture_count_rejects_before_claim_or_paid_send(self):
        f = self.bridge_fixture()
        original = f.client.download
        with patch.object(f.client,'download', wraps=f.client.download) as download:
            def tamper(artifact):
                data = original(artifact)
                return data + b'tampered' if artifact['name']=='targets.json' else data
            download.side_effect = tamper
            job = next(j for j in f.store.status()['jobs'] if j['stage']=='generate' and j['approach']=='kku-claude')
            with self.assertRaisesRegex(ValueError,'hash|size'):
                load(f.client,job,f.protocol)
        self.assertFalse(f.calls)
        self.assertEqual(len(f.store.status()['attempts']),1)
