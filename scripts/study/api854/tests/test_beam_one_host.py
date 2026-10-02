import unittest
from unittest.mock import patch, Mock
from pathlib import Path
import tempfile

from scripts.study.api854.common import ROOT, read_json, write_json
from scripts.study.api854.gate_a import route_coverage
from scripts.study.api854.team_queue import assignment, TeamQueueClient
from scripts.study.api854 import queue_worker, configuration
from . import test_beam_queue as fixture


class BeamOneHostTests(unittest.TestCase):
    def test_explicit_recipe_cannot_claim_a_legacy_shared_v3_job(self):
        with tempfile.TemporaryDirectory() as directory:
            root = Path(directory)
            protocol = configuration.proposal()
            protocol.update(fixture_policy_id='beam-explicit-fixtures-v4-proposal',
                            generation={'prepare_contract': 'aom-beam-prepare-v3'})
            write_json(root / 'protocol.json', protocol)
            client = Mock()
            with self.assertRaisesRegex(ValueError, 'new shared preparation contract before claim'):
                queue_worker.run_one(client, protocol_path=root / 'protocol.json',
                    run_id='beam-development-no-stale-recipe', stage='generate', approaches=['fscs-art'],
                    worker_id='beam-pc1', output=root / 'output', results=root / 'results', worktrees=root / 'trees')
            client.request.assert_not_called()
            client.claim.assert_not_called()
            self.assertFalse((root / 'output').exists())
    def setUp(self):
        self.plan = read_json(ROOT / 'experiments/configs/api854-20261003/runner-plan.beam-one-host.v2.json')

    def test_one_host_keeps_all_854_owner_and_stage_routes(self):
        result = route_coverage(self.plan, read_json(ROOT / 'experiments/configs/api854-20261003/ownership.json'))
        self.assertEqual(result['covered_stage_keys'], 10248)
        self.assertFalse(result['gaps'])
        self.assertEqual([r['worker_id'] for r in self.plan['runners'] if r['role'] == 'beam'], ['beam-pc1'])

    def test_retired_hosts_and_paid_generation_cannot_be_assigned(self):
        for worker, stage, approaches in [('beam-pc2', 'prepare', ['cmaes']),
                ('beam-pc3', 'evaluate', ['kku-claude']), ('beam-pc1', 'generate', ['kku-gemini'])]:
            with self.subTest(worker=worker, stage=stage), self.assertRaises(ValueError):
                assignment(self.plan, worker, 'beam', 'beam', stage, approaches)
        actor = assignment(self.plan, 'beam-pc1', 'beam', 'beam', 'evaluate', ['kku-gemini'])
        self.assertEqual(actor['max_cpu_slots'], 1)

    def test_other_owner_assignments_preserved_and_plan_still_proposed(self):
        original = read_json(ROOT / 'experiments/configs/api854-20261003/runner-plan.v1.json')
        self.assertEqual([r for r in original['runners'] if r['role'] != 'beam'],
                         [r for r in self.plan['runners'] if r['role'] != 'beam'])
        self.assertNotEqual(self.plan['status'], 'frozen')
        self.assertFalse(self.plan['live_workers_started'])

    def test_same_beam_host_routes_all_three_local_stages_without_changing_owner(self):
        f = fixture.QueueIntegrationTests()
        f.setUp()
        self.addCleanup(f.tearDown)
        write_json(f.root / 'one-host-plan.json', self.plan)
        write_json(f.root / 'beam-access.private.json', {'role': 'beam', 'schema_version': '1.0',
            'base_url': 'https://queue.example.test', 'worker_token': f.client.token})
        for stage, phase, module in [('prepare', 'prepare', 'prepare_worker'),
                ('generate', 'generation', 'algorithm_worker'), ('evaluate', 'evaluation', 'evaluate_worker')]:
            client = TeamQueueClient.from_plan(f.root / 'beam-access.private.json', f.root / 'one-host-plan.json',
                'beam-pc1', owner='beam', stage=stage, approaches=['fscs-art'],
                protocol_path=f.protocol_path, condition='development')
            client.base_url, client.mock_mode = f.client.base_url, True
            with patch(f'scripts.study.api854.queue_worker.{module}.execute',
                    side_effect=lambda job, protocol, results, *args: f.execute_fixture(phase, job, protocol, results, *args)):
                result = queue_worker.run_one(client, protocol_path=f.protocol_path, run_id=f.run_id,
                    stage=stage, approaches=['fscs-art'], worker_id='beam-pc1', owner='beam',
                    output=f.root / f'one-host-{stage}', results=f.root / 'results', worktrees=f.root / 'trees')
            self.assertEqual(result['state'], 'published')
            job = next(j for j in f.store.status()['jobs'] if j['job_id'] == result['job_id'])
            self.assertEqual(job['owner'], 'beam')
        self.assertEqual(job['state'], 'finished')
