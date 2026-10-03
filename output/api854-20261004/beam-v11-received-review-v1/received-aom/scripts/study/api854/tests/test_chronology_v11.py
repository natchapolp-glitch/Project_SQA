import unittest
from scripts.study.api854.fixture_policy import select


class ChronologyPolicyTests(unittest.TestCase):
    def test_only_six_exact_identities_and_old_policy_unchanged(self):
        from scripts.study.api854.fixture_policy import POLICY_V10, POLICY_V11, CHRONOLOGY_SIGNATURES
        fields = ('class', 'constructor_types', 'method', 'parameter_types')
        targets = [dict(zip(fields,t)) for t in sorted(CHRONOLOGY_SIGNATURES)]
        wrong = dict(targets[0], constructor_types='org.joda.time.Chronology', method='getField', parameter_types='int,org.joda.time.Chronology')
        chosen, excluded = select(targets+[wrong], POLICY_V11)
        self.assertCountEqual(chosen, targets)
        self.assertEqual([r['target'] for r in excluded],[wrong])
        self.assertEqual(select(targets,POLICY_V10)[0],[])

    def test_worklist_inventory_is_six_and_constructor_identity_matters(self):
        from scripts.study.api854.chronology_v11 import receive_contract
        contract = receive_contract(write=False)
        self.assertEqual(len(contract['targets']),6)
        self.assertEqual(sum(t['method']=='<init>' for t in contract['targets']),4)
        self.assertTrue(all(t['constructor_types']=='' for t in contract['targets'] if t['method']!='<init>'))

    def test_all_bounded_driver_cases_use_generator_domain(self):
        from scripts.study.api854.verify_chronology_v11 import CASES
        vectors={(bucket-1)*0.75 for group,bucket in CASES.values()}
        self.assertEqual(vectors,{-0.75,0.0,0.75})
        self.assertTrue(all(-1<=v<=1 for v in vectors))

    def test_buggy_all_pass_cannot_replace_pinned_negative_control(self):
        from scripts.study.api854.verify_chronology_v11 import CASES,validate_buggy_signature
        rows=[{'case':case,'passed':True} for case in CASES]
        with self.assertRaisesRegex(ValueError,'negative-control'):
            validate_buggy_signature(rows)

    def test_chronology_fixture_failure_is_rejected_by_real_evaluator(self):
        from scripts.study.evaluate import parse_test_evidence,EvidenceError
        with self.assertRaisesRegex(EvidenceError,'Harness'):
            parse_test_evidence('Failing tests: 1\n','--- GeneratedStudyTest::generatedcase\n'
                +'GeneratedStudyTest$SqaProbe$FixtureFailure: SQA_HARNESS Chronology setup/projection failed\n')


if __name__=='__main__':
    unittest.main()
