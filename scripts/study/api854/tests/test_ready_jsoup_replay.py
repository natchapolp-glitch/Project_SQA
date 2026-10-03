"""Checks for selective replay, real counts and official benchmark source binding."""
import hashlib,json,sys,unittest
from pathlib import Path
ROOT=Path(__file__).resolve().parents[4]
sys.path.insert(0,str(ROOT/'scripts/study/api854'))
import ready_d4j_counts as counts
import ready_d4j_reference as reference
BASE=ROOT/'output/api854-20261004/beam-champ-jsoup-d4j-v1'
INTAKE=ROOT/'output/api854-20261004/beam-champ-jsoup-intake-v1'
PEER=INTAKE/'received-champ/output/api854-20261004/champ-jsoup-native-measurement-v2'
def read(path):return json.loads(Path(path).read_bytes())
def sha(path):return hashlib.sha256(Path(path).read_bytes()).hexdigest()
class JsoupReplayGuards(unittest.TestCase):
    def test_both_algorithm_measurements_have_actual_four_stage_counts(self):
        rows={r['approach']:r for r in read(BASE/'results.json')}
        for approach in ['cmaes','fscs-art']:
            row=rows[approach];self.assertEqual(row['status'],'complete');self.assertEqual(row['fixed_validation'],'passed_twice');self.assertFalse(row['fault_detected'])
            for stage in ['fixed-1','fixed-2','buggy','coverage']:
                actual=read(BASE/approach/'evaluation'/stage/'actual-junit-counts.json')
                parsed=counts.parse_reports((BASE/approach/'evaluation'/stage/'junit-reports').glob('TEST-*.xml'))
                for field in ['executed','skipped','failed','errors']:self.assertEqual(parsed[field],actual[field])
                self.assertEqual((actual['executed'],actual['skipped'],actual['target_checks']),(30,0,30))
            record=read(BASE/approach/'evaluation/record.json')
            self.assertEqual(row['record_sha256'],sha(BASE/approach/'evaluation/record.json'))
            self.assertEqual((record['line_covered'],record['line_total'],record['branch_covered'],record['branch_total']),(36,46,10,18))
    def test_invalid_ai_is_retained_without_replay_or_fake_zero_metrics(self):
        rows={r['approach']:r for r in read(BASE/'results.json')}
        for approach,number,failures in [('kku-claude',27,1),('kku-gemini',9,2)]:
            row=rows[approach];self.assertEqual(row['status'],'invalid_native_fixed_entire_suite_rejected');self.assertEqual(row['full_defects4j_status'],'not_replayed_by_design');self.assertEqual(row['test_count'],number)
            self.assertIsNone(row['fault_detected']);self.assertIsNone(row['coverage']);self.assertIsNone(row['worker_seconds_including_setup'])
            self.assertFalse((BASE/approach/'evaluation').exists())
            for stage in row['native_fixed_stage_counts'].values():self.assertEqual(stage['executed'],number);self.assertEqual(stage['failed'],failures);self.assertEqual(stage['skipped'],0)
            self.assertEqual(row['native_outcome_sha256'],sha(PEER/approach/'receipt.json'))
    def test_archives_and_sealed_producers_are_byte_identical(self):
        seal=read(BASE/'preexecution-seal.json')
        for approach,data in seal['suites'].items():
            self.assertEqual(data['suite_sha256'],sha(BASE/approach/'suite.tar.bz2'))
            self.assertEqual((BASE/approach/'suite.tar.bz2').read_bytes(),(PEER/approach/'packaged-suite/suite.tar.bz2').read_bytes())
        for name,digest in seal['producers_sha256'].items():self.assertEqual(sha(BASE/name),digest)
        self.assertEqual(seal['producers_sha256']['producer.py'],sha(ROOT/'scripts/study/api854/evaluate_champ_jsoup_ready.py'))
        self.assertEqual(seal['producers_sha256']['ready_d4j_reference.py'],sha(ROOT/'scripts/study/api854/ready_d4j_reference.py'))
        self.assertEqual(seal['producers_sha256']['ready_d4j_counts.py'],sha(ROOT/'scripts/study/api854/ready_d4j_counts.py'))
    def test_actual_benchmark_sources_equal_official_patch_reference(self):
        seal=read(BASE/'preexecution-seal.json');derived=read(BASE/'isolated-bug-reference/derivation.json')
        self.assertTrue(derived['official_patch_git_blob_verified']);self.assertEqual(derived['patch_exit_code'],0);self.assertFalse(derived['actual_production_source_replacement'])
        self.assertFalse(derived['native_and_isolated_buggy_sources_identical'])
        for approach in ['cmaes','fscs-art']:
            tree=Path('/home/beam/sqa-beam/worktrees/beam-jsoup-ready-evaluator-v1')/approach/'Jsoup/1/b'
            for path,digest in seal['expected_isolated_buggy_source_sha256'].items():self.assertEqual(sha(tree/path),digest)
        for path,digest in seal['current_beam_runtime_before_sha256'].items():self.assertEqual(sha(ROOT/path),digest)
    def test_framework_is_restored_and_results_not_promoted(self):
        receipt=read(BASE/'receipt.json');self.assertEqual(receipt['full_defects4j_completed'],2);self.assertEqual(receipt['native_invalid_retained'],2);self.assertFalse(receipt['ai_replayed'])
        self.assertTrue(receipt['framework_restored']['restored_exact_bytes']);self.assertTrue(receipt['framework_restored']['observed_bytes_unchanged'])
        original=BASE/'count-observer-framework/defects4j.build.original.xml'
        self.assertEqual(sha(original),sha('/home/beam/sqa-beam/defects4j/framework/projects/defects4j.build.xml'))
        self.assertEqual(receipt['kku_requests'],0);self.assertEqual(receipt['queue_mutations'],0);self.assertEqual(receipt['primary_results_added'],0);self.assertFalse(receipt['gate_a_approved'])
    def test_unknown_mapping_is_rejected_before_writing_or_running(self):
        with self.assertRaises(ValueError):reference.derive('Csv',1,'jsoup.git','src/main/java',{},'/not-used','/not-used','/not-used')
if __name__=='__main__':unittest.main(verbosity=2)
