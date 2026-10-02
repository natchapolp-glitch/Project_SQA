import json
from pathlib import Path
import tempfile
import unittest
from unittest.mock import patch

from scripts.study.api854.common import ROOT, read_json, write_json
from scripts.study.api854.team_queue import assignment, TeamQueueClient
from scripts.study.api854.gate_a import route_coverage
from scripts.study.api854 import queue_worker
from . import test_beam_queue as fixture


class TeamRoutingTests(unittest.TestCase):
    def setUp(self):
        self.plan = read_json(ROOT / 'experiments/configs/api854-20261003/runner-plan.v1.json')

    def test_all_854_bugs_four_methods_three_stages_have_assigned_runners(self):
        coverage = route_coverage(self.plan, read_json(ROOT / 'experiments/configs/api854-20261003/ownership.json'))
        self.assertEqual(coverage['covered_stage_keys'], 10248)
        self.assertFalse(coverage['gaps'])
        for owner in ('aom','beam','champ'):
            self.assertEqual(assignment(self.plan,'champ-pc1','champ',owner,'generate',['kku-claude'])['worker_id'], 'champ-pc1')

    def test_roles_cpu_api_stage_and_owner_boundaries_cannot_be_bypassed(self):
        for args in [('aom-pc1','aom','beam','prepare',['cmaes']),
                     ('aom-pc1','aom','aom','generate',['kku-claude']),
                     ('champ-pc1','champ','champ','evaluate',['kku-gemini']),
                     ('aom-pc1','champ','aom','prepare',['cmaes'])]:
            with self.subTest(args=args), self.assertRaises(ValueError): assignment(self.plan,*args)

    def test_primary_plan_proposal_cannot_start_even_with_a_frozen_protocol(self):
        with tempfile.TemporaryDirectory() as directory:
            root = Path(directory)
            write_json(root/'plan.json',self.plan)
            write_json(root/'protocol.json',{'state':'frozen','approval_state':'frozen'})
            write_json(root/'access.private.json',{'role':'aom','schema_version':'1.0','base_url':'https://queue.example.test','worker_token':'mock-private-only'})
            with self.assertRaisesRegex(ValueError,'reviewed plan'):
                TeamQueueClient.from_plan(root/'access.private.json',root/'plan.json','aom-pc1',owner='aom',stage='evaluate',approaches=['kku-claude'],protocol_path=root/'protocol.json',condition='primary')

    def test_aom_runner_handles_prepare_algorithm_evaluation_for_aom_and_champ(self):
        f = fixture.QueueIntegrationTests()
        f.setUp()
        self.addCleanup(f.tearDown)
        token='isolated-aom-role-private-token-987654321'
        f.store.credentials['aom']=token
        write_json(f.root/'runner-plan.json',self.plan)
        write_json(f.root/'aom-access.private.json',{'role':'aom','schema_version':'1.0','base_url':'https://queue.example.test','worker_token':token})
        for bug,owner in [(5,'aom'),(6,'champ')]:
            f.store.seed({'bugs':[{'project':'Lang','bug_id':bug,'owner':owner}]},f.run_id,
                         fixture.common.sha256(f.protocol_path),False,protocol=fixture.mock_stage_gate())
            for stage,phase,module in [('prepare','prepare','prepare_worker'),('generate','generation','algorithm_worker'),('evaluate','evaluation','evaluate_worker')]:
                client=TeamQueueClient.from_plan(f.root/'aom-access.private.json',f.root/'runner-plan.json','aom-pc1',owner=owner,stage=stage,approaches=['cmaes'],protocol_path=f.protocol_path,condition='development')
                # Test-only transport origin; production constructors permit HTTPS exclusively.
                client.base_url=f.client.base_url
                client.mock_mode=True
                with patch(f'scripts.study.api854.queue_worker.{module}.execute',side_effect=lambda job,protocol,results,*args:f.execute_fixture(phase,job,protocol,results,*args)):
                    result=queue_worker.run_one(client,protocol_path=f.protocol_path,run_id=f.run_id,stage=stage,approaches=['cmaes'],worker_id='aom-pc1',owner=owner,output=f.root/f'run-{owner}-{stage}',results=f.root/'results',worktrees=f.root/'trees')
                self.assertEqual(result['state'],'published')
                job=next(j for j in f.store.status()['jobs'] if j['job_id']==result['job_id'])
                self.assertEqual(job['owner'],owner)
            self.assertEqual(job['state'],'finished')
