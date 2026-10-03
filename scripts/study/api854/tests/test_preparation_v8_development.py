"""Current composition preserves the denominator, source knowledge and closed gate."""
import copy
import json
from pathlib import Path
from types import SimpleNamespace
import tempfile
import unittest

from scripts.study.api854.common import ROOT, read_json, sha256
from scripts.study.api854.preparation import POLICY_V8, encoded, digest, validate
from scripts.study.api854.prepared_inputs import load
from scripts.study.api854.api_worker import FrozenSettings, WorkerBlocked, resolve_prepared_job

PREP = ROOT/'output/api854-20261003/prepare-v8-fraction-field-development'
PROTOCOL = ROOT/'output/api854-20261003/aom-continuation-v8-development/protocol.proposal.json'
OLD = ROOT/'output/api854-20261003/prepare-v7-twenty-bug-development'


class DevelopmentV8Tests(unittest.TestCase):
    def test_exactly_two_reviewed_declarations_are_added_and_all_others_retained(self):
        self.assertTrue((PREP/'index.json').is_file(), 'New shared composition has not been built')
        index = read_json(PREP/'index.json')
        self.assertEqual((len(index['records']),index['target_count'],index['capability_exclusion_count']), (20,379,312))
        self.assertFalse(index['generation_ready'])
        keys = lambda rows: {tuple(t[k] for k in ('class','constructor_types','method','parameter_types')) for t in rows}
        added = set()
        for row in index['records']:
            name = f"{row['project']}-{row['bug_id']}"
            before, after = read_json(OLD/name/'targets.json'), read_json(PREP/name/'targets.json')
            delta = keys(after['targets']) - keys(before['targets'])
            self.assertFalse(keys(before['targets']) - keys(after['targets']))
            if name != 'Math-1':
                self.assertFalse(delta)
            added |= delta
        self.assertEqual(added, {('org.apache.commons.math3.fraction.BigFraction','double','getField',''),
                                ('org.apache.commons.math3.fraction.Fraction','double','getField','')})

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
                        'run_id':'offline-v8-check','protocol_hash':sha256(PROTOCOL),'stage':'generate','approach':approach,
                        'payload':{'stage_history':[{'stage':'prepare','outcome':'prepared','attempt_id':'offline-prepare',
                            'metadata':metadata,'artifacts':[{'name':name,'size':len(raw),'sha256':sha256(folder/name)} for name,raw in data.items()]}]}}
                    cpu = load(client,job,protocol)
                    self.assertEqual(cpu['fixture_recipe'],json.loads(data['fixture-recipes.json']))
                    self.assertEqual(cpu['metadata']['prompt_sha256'],digest(data['prompt.md']))
                    self.assertEqual(cpu['metadata']['fixed_source_sha256'],read_json(OLD/folder.name/'prepare-metadata.json')['fixed_source_sha256'])
                    if approach.startswith('kku-'):
                        settings = SimpleNamespace(protocol_hash=job['protocol_hash'],models=protocol['models'],
                            generation_owners=('champ','beam','aom'),context_policy_id=POLICY_V8['context_policy_id'],
                            prompt_policy_id=POLICY_V8['prompt_policy_id'],prompt_token_reserve=len(data['prompt.md']),
                            handoff_contract='beam-v1',prepare_contract=POLICY_V8['contract'],protocol=protocol)
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
