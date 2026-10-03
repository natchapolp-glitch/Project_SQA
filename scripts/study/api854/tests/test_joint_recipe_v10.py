"""Exact peer verdicts and closed composition boundaries."""
import copy
import importlib.util
import unittest
from scripts.study.api854 import fixture_policy as fixtures
from scripts.study.api854.common import ROOT, read_json

class JointV10Tests(unittest.TestCase):
    def test_new_policy_accepts_only_exact_constructor_and_overload(self):
        policy = getattr(fixtures, 'POLICY_V10', None)
        self.assertIsNotNone(policy, 'Joint v10 policy missing')
        good = {'class':'com.fasterxml.jackson.core.io.NumberInput','constructor_types':'',
                'method':'parseInt','parameter_types':'[C,int,int'}
        wrong = {**good,'constructor_types':'int'}
        unsupported = {**good,'parameter_types':'[C'}
        lang = {'class':'org.apache.commons.lang3.math.NumberUtils','constructor_types':'',
                'method':'validateArray','parameter_types':'java.lang.Object'}
        selected, excluded = fixtures.select([good,wrong,unsupported,lang],policy)
        self.assertEqual(selected,[good,lang])
        self.assertEqual([r['target'] for r in excluded],[wrong,unsupported])

    def test_lang_peer_bindings_and_missing_verdict_are_checked(self):
        self.assertIsNotNone(importlib.util.find_spec('scripts.study.api854.joint_recipe_v10'), 'Joint intake missing')
        from scripts.study.api854.joint_recipe_v10 import validate_lang, INTAKE
        beam=read_json(INTAKE/'received/beam-lang-verdict.json')
        champ=read_json(INTAKE/'received/champ-lang-verdict.json')
        self.assertEqual(len(validate_lang(beam,champ)),2)
        rejected=copy.deepcopy(beam);rejected['candidates'][0]['beam_joint_verdict']=None
        with self.assertRaises(ValueError): validate_lang(rejected,champ)
        changed=copy.deepcopy(champ);changed['reference_suite_sha256']='0'*64
        with self.assertRaises(ValueError): validate_lang(beam,changed)
        scope=copy.deepcopy(beam);scope['candidates'][0]['target'].update(project='Math',bug_id=999)
        with self.assertRaises(ValueError): validate_lang(scope,champ)
        nested=copy.deepcopy(beam);nested['candidates'][0]['evidence'][0]['sha256']='0'*64
        with self.assertRaises(ValueError): validate_lang(nested,champ)
        wrong=copy.deepcopy(champ);wrong['candidates'][0]['target']['constructor_types']='double'
        with self.assertRaises(ValueError): validate_lang(beam,wrong)

if __name__=='__main__': unittest.main()
