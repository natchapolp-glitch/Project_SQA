"""Adversarial checks for exact candidate scope and meaningful raw oracle evidence."""
import copy
import json
import unittest
from scripts.study.api854.codec_v13 import contract, validators, PROOF
from scripts.study.api854.fixture_policy import POLICY_V12, POLICY_V13, select
from scripts.study.api854.common import ROOT


class CodecScopeTests(unittest.TestCase):
    def rows(self):
        return [json.loads(x) for x in (PROOF/'fixed_first.stdout.log').read_text().splitlines()]

    def test_exact_five_disjoint_exclusions_extend_thirteen(self):
        rows=json.loads((ROOT/'output/api854-20261003/prepare-v3/Codec-1/targets.json').read_bytes())['targets']
        a,_=select(rows,POLICY_V12);b,e=select(rows,POLICY_V13)
        self.assertEqual((len(a),len(b)),(13,18))
        self.assertCountEqual([t for t in b if t not in a],contract()['targets'])
        self.assertFalse(contract()['shared_integration_approved'])

    def test_wrong_constructor_overload_and_receiver_stay_excluded(self):
        t=contract()['targets'][0]
        for wrong in [{**t,'constructor_types':'int'},{**t,'parameter_types':'java.lang.String,int,char'},
                      {**t,'class':'org.apache.commons.codec.language.Caverphone'}]:
            self.assertEqual(select([wrong],POLICY_V13)[0],[])

    def test_all_forty_three_buckets_reachable(self):
        from scripts.study.api854.verify_codec_v13 import vectors
        policy=validators()['POLICY'];v=vectors(policy)
        for c in policy['cases']:
            group=[r for r in policy['cases'] if r['method']==c['method']]
            slot=min(len(group)-1,int((v[c['case']]+1)*len(group)/2))
            self.assertEqual(group[slot],c)
            self.assertTrue(-1<=v[c['case']]<=1)

    def test_coherently_wrong_boolean_and_expected_are_rejected(self):
        rows=self.rows();row=rows[0]
        row['observation']['value']=False;row['expected_observation']['value']=False
        with self.assertRaisesRegex(ValueError,'Independent oracle'):validators()['validate'](rows)

    def test_wrong_score_with_matching_counter_is_rejected(self):
        rows=self.rows();row=next(r for r in rows if r.get('method')=='difference')
        row['observation']['value']=99;row['expected_observation']['value']=99
        with self.assertRaisesRegex(ValueError,'Independent oracle'):validators()['validate'](rows)

    def test_changed_buffer_capacity_is_rejected(self):
        rows=self.rows();rows[0]['post_state']['buffer_capacity']+=1
        with self.assertRaisesRegex(ValueError,'Buffer state'):validators()['validate'](rows)
        rows=self.rows()
        rows[0]['pre_state']['buffer_capacity']=999
        rows[0]['post_state']['buffer_capacity']=999
        with self.assertRaisesRegex(ValueError,'default buffer capacity'):validators()['validate'](rows)

    def test_fixture_error_is_not_target_failure(self):
        rows=self.rows();rows[0]['setup_succeeded']=False
        with self.assertRaisesRegex(ValueError,'Fixture error'):validators()['validate'](rows)

    def test_skipped_case_and_wrong_descriptor_are_rejected(self):
        rows=self.rows();rows[-1]['skipped']=1
        with self.assertRaisesRegex(ValueError,'Counters'):validators()['validate'](rows)
        rows=self.rows();rows[0]['descriptor']='(I)Z'
        with self.assertRaisesRegex(ValueError,'descriptor'):validators()['validate'](rows)


if __name__=='__main__':unittest.main()
