"""Current composition preserves the denominator, source knowledge and closed gate."""
import copy
import json
from pathlib import Path
from types import SimpleNamespace
import tempfile
import unittest

from scripts.study.api854.common import ROOT, read_json, sha256
from scripts.study.api854.preparation import POLICY_V10, encoded, digest, validate
from scripts.study.api854.prepared_inputs import load
from scripts.study.api854.api_worker import FrozenSettings, WorkerBlocked, resolve_prepared_job

PREP = ROOT/'output/api854-20261003/prepare-v10-joint-development'
PROTOCOL = ROOT/'output/api854-20261003/aom-continuation-v10-development/protocol.proposal.json'
OLD = ROOT/'output/api854-20261003/prepare-v7-twenty-bug-development'


class DevelopmentV10Tests(unittest.TestCase):
    def test_v9_is_preserved_and_exactly_ten_accepted_signatures_are_added(self):
        from scripts.study.api854.joint_recipe_v10 import CHAMP,V9,INTAKE,git_json
        index=read_json(PREP/'index.json')
        self.assertEqual((len(index['records']),index['target_count'],index['capability_exclusion_count']),(20,390,301))
        added=[]
        receipt=read_json(INTAKE/'receipt.json')
        for row in index['records']:
            name=f"{row['project']}-{row['bug_id']}"
            before=git_json(CHAMP,V9+'/'+name+'/targets.json')['targets']
            after=read_json(PREP/name/'targets.json')['targets']
            self.assertTrue(all(t in after for t in before))
            delta=[t for t in after if t not in before]
            added.extend(delta)
            old_ex=git_json(CHAMP,V9+'/'+name+'/capability-exclusions.json')['excluded']
            new_ex=read_json(PREP/name/'capability-exclusions.json')['excluded']
            self.assertEqual(new_ex,[r for r in old_ex if r['target'] not in delta])
        self.assertCountEqual(added,receipt['buffer_targets']+receipt['lang_targets'])

    def test_reserve_worksheet_has_forty_actual_model_ids_and_no_token_claim(self):
        worksheet=read_json(ROOT/'output/api854-20261003/aom-v10-readiness-v1/prompt-reserve-worksheet.json')
        self.assertEqual(len(worksheet['records']),40)
        self.assertTrue(worksheet['bytes_are_not_provider_tokens'])
        self.assertIsNone(worksheet['final_reserve'])
        self.assertTrue(all(isinstance(r['requested_model_id'],str) for r in worksheet['records']))
        self.assertEqual({r['requested_model_id'] for r in worksheet['records']},
                         {'claude-sonnet-5','gemini-3.5-flash-lite'})

    def test_changing_approval_flag_does_not_authorize_development_generation(self):
        protocol = read_json(PROTOCOL)
        self.assertEqual(protocol['enabled_stages'],[])
        self.assertIsNone(protocol['generation']['prompt_token_reserve'])
        protocol['approval_state'] = 'frozen'
        with tempfile.TemporaryDirectory() as directory:
            path = Path(directory)/'forged.json'
            path.write_bytes(encoded(protocol))
            with self.assertRaisesRegex(WorkerBlocked,'prepare_contract_not_frozen'):
                FrozenSettings.load(path)

    def test_all_four_consumers_get_identical_context_recipe_and_source_mapping(self):
        protocol = read_json(PROTOCOL)
        for row in read_json(PREP/'index.json')['records']:
            folder = PREP/f"{row['project']}-{row['bug_id']}"
            names = ('context-manifest.json','prepare-metadata.json','prompt.md','targets.json','prepare-policy.json','fixture-recipes.json')
            data = {name:(folder/name).read_bytes() for name in names}
            client = SimpleNamespace(download=lambda artifact:data[artifact['name']])
            metadata = read_json(folder/'prepare-metadata.json')
            for approach in ('cmaes','fscs-art','kku-claude','kku-gemini'):
                with self.subTest(project=row['project'],bug_id=row['bug_id'],approach=approach):
                    job = {'project':row['project'],'bug_id':row['bug_id'],'owner':row['owner'],
                        'run_id':'offline-v10-check','protocol_hash':sha256(PROTOCOL),'stage':'generate','approach':approach,
                        'payload':{'stage_history':[{'stage':'prepare','outcome':'prepared','attempt_id':'offline-prepare',
                            'metadata':metadata,'artifacts':[{'name':name,'size':len(raw),'sha256':sha256(folder/name)} for name,raw in data.items()]}]}}
                    cpu = load(client,job,protocol)
                    self.assertEqual(cpu['fixture_recipe'],json.loads(data['fixture-recipes.json']))
                    self.assertEqual(cpu['metadata']['prompt_sha256'],digest(data['prompt.md']))
                    self.assertEqual(cpu['metadata']['fixed_source_sha256'],read_json(OLD/folder.name/'prepare-metadata.json')['fixed_source_sha256'])
                    if approach.startswith('kku-'):
                        settings = SimpleNamespace(protocol_hash=job['protocol_hash'],models=protocol['models'],
                            generation_owners=('champ','beam','aom'),context_policy_id=POLICY_V10['context_policy_id'],
                            prompt_policy_id=POLICY_V10['prompt_policy_id'],prompt_token_reserve=len(data['prompt.md']),
                            handoff_contract='beam-v1',prepare_contract=POLICY_V10['contract'],protocol=protocol)
                        api = resolve_prepared_job(client,job,settings)
                        self.assertEqual(api.prompt.encode(),data['prompt.md'])

    def test_omitting_factory_knowledge_from_rehashed_prompt_is_rejected(self):
        folder = PREP/'Math-1'
        manifest, metadata = read_json(folder/'context-manifest.json'), copy.deepcopy(read_json(folder/'prepare-metadata.json'))
        prompt = (folder/'prompt.md').read_bytes()
        path = 'src/main/java/org/apache/commons/math3/fraction/BigFractionField.java'
        source = (folder/'fixed-source'/path).read_bytes()
        prompt = prompt.replace(source,b'// omitted production factory\n',1)
        metadata.update(prompt_sha256=digest(prompt),prompt_utf8_bytes=len(prompt))
        with self.assertRaisesRegex(ValueError,'context.*prompt'):
            validate(manifest,metadata,prompt,(folder/'targets.json').read_bytes(),
                     (folder/'prepare-policy.json').read_bytes(),fixture_recipe=read_json(folder/'fixture-recipes.json'))


if __name__ == '__main__':
    unittest.main()
