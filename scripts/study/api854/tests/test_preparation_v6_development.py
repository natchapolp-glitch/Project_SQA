"""CPU/API consumers read identical current candidate bytes without dispatch."""
import json
from pathlib import Path
from types import SimpleNamespace
import tempfile
import unittest

from scripts.study.api854.common import ROOT, read_json, sha256
from scripts.study.api854.preparation import POLICY_V6, encoded, explicit_scope
from scripts.study.api854.prepared_inputs import load
from scripts.study.api854.api_worker import FrozenSettings, WorkerBlocked, resolve_prepared_job

PREP = ROOT/'output/api854-20261003/prepare-v6-twenty-bug-development'
PROTOCOL = ROOT/'output/api854-20261003/aom-continuation-v6-development/protocol.proposal.json'


class DevelopmentV6Tests(unittest.TestCase):
    def test_scope_is_exact_pilot_and_retains_unsupported_declarations(self):
        index = read_json(PREP/'index.json')
        self.assertEqual(explicit_scope(POLICY_V6), {(r['project'],r['bug_id']) for r in index['records']})
        self.assertEqual(index['target_count'],377)
        self.assertEqual(index['capability_exclusion_count'],314)
        self.assertFalse(index['generation_ready'])

    def test_development_contract_cannot_be_opened_by_changing_approval_flag(self):
        protocol = read_json(PROTOCOL)
        protocol['approval_state'] = 'frozen'
        with tempfile.TemporaryDirectory() as directory:
            path = Path(directory)/'forged-frozen.json'
            path.write_bytes(encoded(protocol))
            with self.assertRaisesRegex(WorkerBlocked,'prepare_contract_not_frozen'):
                FrozenSettings.load(path)

    def test_cpu_and_api_read_the_same_twenty_prepared_prompts_and_recipes(self):
        protocol = read_json(PROTOCOL)
        for row in read_json(PREP/'index.json')['records']:
            with self.subTest(project=row['project'],bug_id=row['bug_id']):
                folder = PREP/f"{row['project']}-{row['bug_id']}"
                names = ['context-manifest.json','prepare-metadata.json','prompt.md','targets.json','prepare-policy.json','fixture-recipes.json']
                data = {name:(folder/name).read_bytes() for name in names}
                # File-backed isolated transport; no HTTP/provider/client construction.
                client = SimpleNamespace(download=lambda artifact:data[artifact['name']])
                metadata = read_json(folder/'prepare-metadata.json')
                job = {'project':row['project'],'bug_id':row['bug_id'],'owner':row['owner'],
                       'run_id':'offline-v6-consumer-check','protocol_hash':sha256(PROTOCOL),
                       'stage':'generate','approach':'kku-claude',
                       'payload':{'stage_history':[{'stage':'prepare','outcome':'prepared','attempt_id':'isolated-prepare',
                            'metadata':metadata,'artifacts':[{'name':name,'size':len(raw),'sha256':sha256(folder/name)} for name,raw in data.items()]}]}}
                settings = SimpleNamespace(protocol_hash=job['protocol_hash'],models=protocol['models'],generation_owners=('champ','beam','aom'),
                    context_policy_id=POLICY_V6['context_policy_id'],prompt_policy_id=POLICY_V6['prompt_policy_id'],
                    prompt_token_reserve=len(data['prompt.md']),handoff_contract='beam-v1',prepare_contract=POLICY_V6['contract'],protocol=protocol)
                cpu = load(client,job,protocol)
                api = resolve_prepared_job(client,job,settings)
                self.assertEqual(api.prompt.encode('utf-8'),data['prompt.md'])
                self.assertEqual(cpu['metadata']['prompt_sha256'],metadata['prompt_sha256'])
                self.assertEqual(cpu['fixture_recipe'],json.loads(data['fixture-recipes.json']))


if __name__=='__main__':
    unittest.main()
