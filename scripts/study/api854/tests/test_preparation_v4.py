"""Shared explicit recipes: real loopback queue, mocked provider/discovery only."""
import json
from pathlib import Path
import tempfile
import unittest
from unittest.mock import patch

from scripts.study.api854 import common, algorithm_worker, queue_worker, prepare_worker
from scripts.study.api854.api_worker import FrozenSettings, WorkerBlocked, resolve_prepared_job
from scripts.study.api854.beam_queue import download_generation
from scripts.study.api854.context_export import export_context
from scripts.study.api854.fixture_policy import POLICY, recipe_document
from scripts.study.api854.preparation import POLICY as POLICY_V2, POLICY_V3, POLICY_V4, compose, digest, encoded, validate
from scripts.study.api854.prepared_inputs import load
from scripts.study.api854.queue_client import QueueClient
from scripts.study.api854 import evaluate_worker
from . import test_champ_bridge as fixture
from .test_beam_queue import mock_stage_gate

CLASS = 'com.google.javascript.jscomp.TypeInference'
TARGET = {'class':CLASS,'constructor_types':'','method':'getBooleanOutcomes',
    'parameter_types':'com.google.javascript.rhino.jstype.BooleanLiteralSet,com.google.javascript.rhino.jstype.BooleanLiteralSet,boolean'}
SOURCE = 'src/com/google/javascript/jscomp/TypeInference.java'


class SharedV4Tests(unittest.TestCase):
    def setup_fixture(self, claim=True):
        f = fixture.ChampBridgeTests()
        f.setUp()
        self.addCleanup(f.tearDown)
        self.f = f
        fixed = f.root / 'fixed-explicit'
        (fixed / SOURCE).parent.mkdir(parents=True)
        (fixed / SOURCE).write_bytes(b'package com.google.javascript.jscomp; class TypeInference {}\r\n')
        (fixed / '.defects4j.config').write_bytes(b'pid=Closure\nvid=176f\n')
        f.fixed_explicit = fixed
        f.prepared = f.root / 'shared-explicit'
        f.manifest = export_context(fixed,'Closure',176,[SOURCE],f.prepared,policy_id=POLICY_V4['context_policy_id'])
        f.prepare_metadata = compose(f.prepared, classes=[CLASS], targets=[TARGET], policy=POLICY_V4,
            modified_sources={SOURCE:common.sha256(fixed / SOURCE)}, fixture_classes=[CLASS],
            fixture_recipe=recipe_document(common.implementation_hashes()))
        f.protocol.update(context_selection=POLICY_V4['context_policy_id'], fixture_policy_id=POLICY,
            prepare_policy_sha256=digest(encoded(POLICY_V4)), worker_routing={'prepare':['champ'],'generate':['champ']})
        f.protocol['generation'].update(prepare_contract=POLICY_V4['contract'],fixture_policy_id=POLICY,
            handoff_contract='beam-v1',context_policy_id=POLICY_V4['context_policy_id'],
            prompt_policy_id=POLICY_V4['prompt_policy_id'],prompt_token_reserve=150000)
        f.protocol_path = f.root / 'shared-v4-protocol.json'
        common.write_json(f.protocol_path,f.protocol)
        f.settings = FrozenSettings.load(f.protocol_path)
        with f.store.db:
            f.store.db.execute('DELETE FROM attempts')
            f.store.db.execute('DELETE FROM jobs')
        f.store.seed({'bugs':[{'project':'Closure','bug_id':176,'owner':'champ'}]},'mock-only',f.settings.protocol_hash,False,protocol=mock_stage_gate())
        f.prepare_claim = f.client.claim('champ-fixture','prepare',['kku-claude']) if claim else None
        return f

    def test_cpu_api_and_evaluator_bind_same_recipe_context_and_sources(self):
        f = self.setup_fixture()
        f.publish_prepare()
        job = next(j for j in f.store.status()['jobs'] if j['approach']=='kku-claude')
        prepared = load(f.client,job,f.protocol)
        request = resolve_prepared_job(f.client,job,f.settings)
        self.assertEqual(prepared['metadata']['context_source_hash'],request.source_hash)
        self.assertIn(encoded(prepared['fixture_recipe']).decode(),request.prompt)
        self.assertEqual(f.worker().once()['state'],'published')
        claim = QueueClient.claim(f.client,'champ-fixture','evaluate',['kku-claude'],owner='champ')
        suite,lineage = download_generation(f.client,claim,f.root/'evaluate-explicit')
        evaluate_worker.validate_lineage(queue_worker.local_job(claim),lineage,suite,f.protocol)
        self.assertEqual(lineage['fixed_source_sha256'],f.prepare_metadata['fixed_source_sha256'])

    def test_rehashed_recipe_cannot_replace_frozen_source_before_claim(self):
        f = self.setup_fixture()
        recipe = common.read_json(f.prepared / 'fixture-recipes.json')
        recipe['sources']['algorithms/java/SqaProbe.java'] += '\n// changed support\n'
        recipe['source_sha256']['algorithms/java/SqaProbe.java'] = digest(recipe['sources']['algorithms/java/SqaProbe.java'].encode())
        path = f.root/'recomposed'
        export_context(f.fixed_explicit,'Closure',176,[SOURCE],path,policy_id=POLICY_V4['context_policy_id'])
        f.prepare_metadata = compose(path,classes=[CLASS],targets=[TARGET],policy=POLICY_V4,
            modified_sources=f.prepare_metadata['fixed_source_sha256'],fixture_classes=[CLASS],fixture_recipe=recipe)
        f.prepared = path
        f.publish_prepare()
        job = next(j for j in f.store.status()['jobs'] if j['approach']=='kku-claude')
        with self.assertRaisesRegex(ValueError,'frozen protocol'):
            load(f.client,job,f.protocol)
        with self.assertRaises(WorkerBlocked):
            f.worker().once()
        self.assertFalse(f.calls)
        self.assertEqual(len(f.store.status()['attempts']),1)

    def test_old_shared_contract_cannot_activate_explicit_policy(self):
        f = self.setup_fixture()
        for policy in (POLICY_V2, POLICY_V3):
            with self.subTest(contract=policy['contract']):
                f.protocol['generation'].update(prepare_contract=policy['contract'],
                    context_policy_id=policy['context_policy_id'], prompt_policy_id=policy['prompt_policy_id'])
                f.protocol['prepare_policy_sha256'] = digest(encoded(policy))
                stale_path = f.root/(policy['contract']+'-stale-protocol.json')
                common.write_json(stale_path,f.protocol)
                with self.assertRaisesRegex(WorkerBlocked,'fixture_policy'):
                    FrozenSettings.load(stale_path)
                with self.assertRaisesRegex(ValueError,'shared-v4'):
                    common.validate_protocol(f.protocol)
                with self.assertRaisesRegex(ValueError,'shared-v4'):
                    prepare_worker.execute({'project':'Closure','bug_id':176},f.protocol,None,None,'defects4j')

    def test_queue_prepare_publishes_exact_shared_recipe_and_one_targets_artifact(self):
        f = self.setup_fixture(claim=False)
        def adapter(d4j,job,trees,output,timeout,fixture_policy=None):
            self.assertEqual(fixture_policy,POLICY)
            output.mkdir()
            (output/'dir.src.classes.txt').write_text('src')
            (output/'classes.modified.txt').write_text(CLASS+'\n')
            (output/'fixture-classes.txt').write_text(CLASS+'\n')
            (output/'targets.fixture-policy.json').write_bytes(encoded({'targets':[TARGET]}))
            return {'fixed_worktree':str(f.fixed_explicit),'classes_file':str(output/'classes.modified.txt'),
                'targets_file':str(output/'targets.fixture-policy.json'),'targets_sha256':common.sha256(output/'targets.fixture-policy.json'),
                'classpath':'offline-only'},[TARGET]
        with patch.object(prepare_worker,'inspect_environment',return_value={'ready':True}), \
             patch.object(prepare_worker,'prepare_adapter',side_effect=adapter):
            result = queue_worker.run_one(f.client,protocol_path=f.protocol_path,run_id='mock-only',stage='prepare',
                approaches=['kku-claude'],worker_id='champ-fixture',owner='champ',output=f.root/'prepare-live',
                results=f.root/'prepare-results',worktrees=f.root/'prepare-worktrees',condition='primary')
        self.assertEqual(result['outcome'],'prepared')
        job = next(j for j in f.store.status()['jobs'] if j['approach']=='kku-claude')
        prepared = load(f.client,job,f.protocol)
        self.assertEqual(prepared['metadata']['prompt_sha256'],f.prepare_metadata['prompt_sha256'])
        artifacts = job['payload']['stage_history'][-1]['artifacts']
        self.assertEqual(sum(a['name']=='targets.json' for a in artifacts),1)
        self.assertEqual(sum(a['name']=='fixture-recipes.json' for a in artifacts),1)
        self.assertFalse(f.calls)

    def test_cpu_generates_with_same_bound_explicit_recipe(self):
        f = self.setup_fixture()
        from scripts.study.api854.beam_queue import FencedPublisher
        from scripts.study.api854.lease import LeaseHeartbeat
        claim = f.client.claim('champ-fixture','prepare',['cmaes'])
        with LeaseHeartbeat(f.client,claim) as heartbeat:
            FencedPublisher(f.client,claim,heartbeat,f.root/'cpu-prepared').publish('prepared',
                [p for p in f.prepared.iterdir() if p.is_file()],f.prepare_metadata)
        def adapter(d4j,job,trees,output,timeout,fixture_policy=None):
            self.assertEqual(fixture_policy,POLICY)
            output.mkdir()
            (output/'dir.src.classes.txt').write_text('src')
            (output/'classes.modified.txt').write_text(CLASS+'\n')
            (output/'fixture-classes.txt').write_text(CLASS+'\n')
            return {'fixed_worktree':str(f.fixed_explicit),'classes_file':str(output/'classes.modified.txt'),
                'classpath':'mock-observation-only','targets_sha256':'b'*64,'source_sha256':{}},[{**TARGET,'dimensions':3}]
        with patch.object(algorithm_worker,'inspect_environment',return_value={'ready':True}), \
             patch.object(algorithm_worker,'prepare_adapter',side_effect=adapter), \
             patch('generate.observe',return_value={'status':'ok','outcome':'value:fixed','target_invoked':True}) as observer:
            result = queue_worker.run_one(f.client,protocol_path=f.protocol_path,run_id='mock-only',stage='generate',
                approaches=['cmaes'],worker_id='champ-fixture',owner='champ',output=f.root/'cpu-generation',
                results=f.root/'cpu-results',worktrees=f.root/'cpu-worktrees',condition='primary')
        self.assertEqual(result['outcome'],'generated')
        self.assertTrue(all(call.args[-1]==POLICY for call in observer.call_args_list))
        job = next(j for j in f.store.status()['jobs'] if j['approach']=='cmaes')
        self.assertEqual(job['payload']['stage_history'][-1]['metadata']['context_source_hash'],f.prepare_metadata['context_source_hash'])
        claim = QueueClient.claim(f.client,'champ-fixture','evaluate',['cmaes'],owner='champ')
        suite,lineage = download_generation(f.client,claim,f.root/'evaluate-cpu')
        evaluate_worker.validate_lineage(queue_worker.local_job(claim),lineage,suite,f.protocol)
        self.assertFalse(f.calls)

    def test_unreviewed_bug_cannot_reuse_two_bug_proposal(self):
        f = self.setup_fixture()
        manifest = dict(f.manifest,project='Chart',bug_id=1,revision='1f')
        with self.assertRaisesRegex(ValueError,'two bugs only'):
            validate(manifest,f.prepare_metadata,*[(f.prepared/n).read_bytes() for n in ('prompt.md','targets.json','prepare-policy.json')],
                fixture_recipe=common.read_json(f.prepared/'fixture-recipes.json'))


if __name__ == '__main__':
    unittest.main()
