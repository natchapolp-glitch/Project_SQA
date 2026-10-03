"""Checks for selective replay, real counts and official benchmark source binding."""
import hashlib,json,sys,unittest
from pathlib import Path
ROOT=Path(__file__).resolve().parents[4]
sys.path.insert(0,str(ROOT/'scripts/study/api854'))
import ready_d4j_counts as counts
import ready_compress_d4j_reference as reference
BASE=ROOT/'output/api854-20261004/beam-champ-compress-d4j-v2'
INTAKE=ROOT/'output/api854-20261004/beam-champ-compress-intake-v1'
PEER=INTAKE/'received-champ/output/api854-20261004/champ-compress-native-measurement-v2'
def read(path):return json.loads(Path(path).read_bytes())
def sha(path):return hashlib.sha256(Path(path).read_bytes()).hexdigest()
class CompressReplayGuards(unittest.TestCase):
    def test_both_algorithm_measurements_have_actual_four_stage_counts(self):
        rows={r['approach']:r for r in read(BASE/'results.json')}
        for approach in ['cmaes','fscs-art']:
            row=rows[approach];self.assertEqual(row['status'],'complete');self.assertEqual(row['fixed_validation'],'passed_twice');self.assertTrue(row['fault_detected'])
            for stage in ['fixed-1','fixed-2','buggy','coverage']:
                actual=read(BASE/approach/'evaluation'/stage/'actual-junit-counts.json')
                parsed=counts.parse_reports((BASE/approach/'evaluation'/stage/'junit-reports').glob('TEST-*.xml'))
                for field in ['executed','skipped','failed','errors']:self.assertEqual(parsed[field],actual[field])
                self.assertEqual((actual['executed'],actual['skipped'],actual['target_checks']),(30,0,30))
            record=read(BASE/approach/'evaluation/record.json')
            self.assertEqual(row['record_sha256'],sha(BASE/approach/'evaluation/record.json'))
            self.assertEqual((record['line_covered'],record['line_total'],record['branch_covered'],record['branch_total']),(98,165,21,59))
    def test_invalid_ai_is_retained_without_replay_or_fake_zero_metrics(self):
        rows={r['approach']:r for r in read(BASE/'results.json')}
        for approach,status,number in [('kku-claude','invalid_generation_truncated',None),('kku-gemini','compile_failed',8)]:
            row=rows[approach];self.assertEqual(row['status'],status);self.assertEqual(row['full_defects4j_status'],'not_replayed_by_design');self.assertEqual(row['test_count'],number)
            self.assertIsNone(row['fault_detected']);self.assertIsNone(row['coverage']);self.assertIsNone(row['worker_seconds_including_setup']);self.assertIsNone(row['suite_sha256'])
            self.assertFalse((BASE/approach/'evaluation').exists());self.assertEqual(row['native_outcome_sha256'],sha(PEER/approach/'receipt.json'))
    def test_archives_and_sealed_producers_are_byte_identical(self):
        seal=read(BASE/'preexecution-seal.json')
        for approach,data in seal['suites'].items():
            self.assertEqual(data['suite_sha256'],sha(BASE/approach/'suite.tar.bz2'))
            self.assertEqual((BASE/approach/'suite.tar.bz2').read_bytes(),(PEER/approach/'packaged-suite/suite.tar.bz2').read_bytes())
        for name,digest in seal['producers_sha256'].items():self.assertEqual(sha(BASE/name),digest)
        self.assertEqual(seal['producers_sha256']['producer.py'],sha(ROOT/'scripts/study/api854/evaluate_champ_compress_benchmark_ready.py'))
        self.assertEqual(seal['producers_sha256']['ready_compress_d4j_reference.py'],sha(ROOT/'scripts/study/api854/ready_compress_d4j_reference.py'))
        self.assertEqual(seal['producers_sha256']['ready_d4j_counts.py'],sha(ROOT/'scripts/study/api854/ready_d4j_counts.py'))
    def test_actual_benchmark_sources_equal_official_patch_reference(self):
        seal=read(BASE/'preexecution-seal.json');derived=read(BASE/'isolated-bug-reference/derivation.json')
        self.assertTrue(derived['official_patch_git_blob_verified']);self.assertEqual(derived['patch_exit_code'],0);self.assertFalse(derived['actual_production_source_replacement'])
        self.assertFalse(derived['native_and_isolated_buggy_sources_identical'])
        for approach in ['cmaes','fscs-art']:
            tree=Path('/home/beam/sqa-beam/worktrees/beam-compress-ready-evaluator-v2')/approach/'Compress/1/b'
            for path,digest in seal['expected_isolated_buggy_source_sha256'].items():self.assertEqual(sha(tree/path),digest)
        for path,digest in seal['current_beam_runtime_before_sha256'].items():self.assertEqual(sha(ROOT/path),digest)
    def test_framework_is_restored_and_results_not_promoted(self):
        receipt=read(BASE/'receipt.json');self.assertEqual(receipt['full_defects4j_completed'],2);self.assertEqual(receipt['native_invalid_retained'],2);self.assertFalse(receipt['ai_replayed'])
        self.assertTrue(receipt['framework_restored']['restored_exact_bytes']);self.assertTrue(receipt['framework_restored']['observed_bytes_unchanged'])
        original=BASE/'count-observer-framework/defects4j.build.original.xml'
        self.assertEqual(sha(original),sha('/home/beam/sqa-beam/defects4j/framework/projects/defects4j.build.xml'))
        self.assertEqual(receipt['kku_requests'],0);self.assertEqual(receipt['queue_mutations'],0);self.assertEqual(receipt['primary_results_added'],0);self.assertFalse(receipt['gate_a_approved'])
    def test_unknown_mapping_is_rejected_before_writing_or_running(self):
        with self.assertRaises(ValueError):reference.derive('Csv',1,'compress.git','src/main/java',{},'/not-used','/not-used','/not-used')
    def test_strict_source_guard_remains_and_benchmark_is_predeclared(self):
        old=ROOT/'output/api854-20261004/beam-champ-compress-d4j-v1'
        self.assertIn('Native benchmark buggy differs',read(old/'failed-attempt.json')['reason'])
        self.assertFalse((old/'cmaes/evaluation').exists())
        self.assertEqual((old/'producer.py').read_bytes(),(ROOT/'scripts/study/api854/evaluate_champ_compress_ready.py').read_bytes())
        binding=ROOT/'output/api854-20261004/beam-compress-benchmark-binding-v2';plan=read(binding/'binding.json');seal=read(BASE/'preexecution-seal.json')
        self.assertEqual(seal['benchmark_binding_sha256'],sha(binding/'binding.json'));self.assertEqual(seal['expected_isolated_buggy_source_sha256'],plan['expected_benchmark_sources_sha256'])
        native=(binding/'sealed-native-crlf.java').read_bytes();benchmark=(binding/'benchmark-gnu-patch.java').read_bytes()
        self.assertNotEqual(native,benchmark);self.assertEqual(native.replace(b'\r\n',b'\n'),benchmark)
        self.assertEqual(plan['native_buggy_sha256'],sha(binding/'sealed-native-crlf.java'));self.assertEqual(plan['benchmark_buggy_sha256'],sha(binding/'benchmark-gnu-patch.java'))
        self.assertLess(plan['sealed_at_utc'],seal['sealed_at_utc'])
    def test_buggy_failures_are_close_state_assertions_with_zero_errors(self):
        import re
        for approach,number in [('cmaes',1),('fscs-art',5)]:
            c=read(BASE/approach/'evaluation/buggy/actual-junit-counts.json');self.assertEqual(c['failed'],number);self.assertEqual(c['errors'],0)
            text=(BASE/approach/'evaluation/buggy/failing_tests').read_text();identities=re.findall(r'^--- GeneratedStudyTest::(generated\d+)',text,re.M);self.assertEqual(len(identities),number)
            self.assertEqual(text.count('junit.framework.AssertionFailedError:'),number)
            java=(PEER/approach/'algorithm-generation/GeneratedStudyTest.java').read_text()
            for name in identities:
                method=re.search(r'public void '+name+r'\(\) \{(.*?)\n  \}',java,re.S).group(1)
                self.assertIn('"close", ""',method);self.assertIn('void|state=archive:',method);self.assertIn('SqaProbe.targetInvoked()',method)
    def test_aom_review_keeps_absent_ai_counters_null_and_starts_no_rerun(self):
        review=ROOT/'output/api854-20261004/beam-aom-csv-receipts-review-v1'
        result=read(review/'receipt.json');self.assertEqual(result['cpu_test_runs_started'],0);self.assertEqual(result['host'],'aom-pc1');self.assertTrue(result['strict_attempt_retained']);self.assertTrue(result['benchmark_matches_beam_official_patch_reference']);self.assertFalse(result['v13_results_mixed'])
        for row in read(review/'observations.json'):
            for counts in row['actual_stage_evidence'].values():
                if row['approach'].startswith('kku-'):self.assertIsNone(counts['skipped']);self.assertIsNone(counts['target_checks'])
                else:self.assertEqual((counts['test_start_events'],counts['skipped'],counts['target_checks']),(30,0,30))
if __name__=='__main__':unittest.main(verbosity=2)
