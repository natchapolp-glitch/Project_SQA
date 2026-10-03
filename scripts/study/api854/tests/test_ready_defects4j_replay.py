"""Regression guards for real ready-results replay and quarantine evidence."""
import hashlib,json,sys,tempfile,unittest
from pathlib import Path
ROOT=Path(__file__).resolve().parents[4]
sys.path.insert(0,str(ROOT/'scripts/study/api854'))
import ready_d4j_counts as counts
import review_champ_cli_ready_order as cli
LATEST=ROOT/'output/api854-20261004/beam-champ-csv-messages-d4j-v3'
OLD=ROOT/'output/api854-20261004/beam-champ-csv-a4-d4j-v1'
CLI=ROOT/'output/api854-20261004/beam-champ-cli-order-review-v1'
INTAKE=ROOT/'output/api854-20261004/beam-champ-d98-ready-intake-v1/received-champ/output/api854-20261004'
def read(path):return json.loads(Path(path).read_bytes())
def sha(path):return hashlib.sha256(Path(path).read_bytes()).hexdigest()
class CountGuards(unittest.TestCase):
    def xml(self,folder,text,name='TEST-Fixture.xml'):
        path=Path(folder)/name;path.write_text(text,encoding='utf-8');return path
    def test_missing_duplicate_or_inconsistent_xml_is_rejected(self):
        with self.assertRaises(ValueError):counts.parse_reports([])
        invalid=['<testsuite name="Fixture" tests="2" failures="0" errors="0"><testcase name="a"/></testsuite>',
                 '<testsuite name="Fixture" tests="2" failures="0" errors="0"><testcase name="a"/><testcase name="a"/></testsuite>',
                 '<testsuite name="Fixture" tests="1" failures="0" errors="0"><testcase name="a"><failure/></testcase></testsuite>',
                 '<testsuite name="Fixture" tests="1" failures="0" errors="0" skipped="0"><testcase name="a"><skipped/></testcase></testsuite>']
        with tempfile.TemporaryDirectory() as folder:
            for text in invalid:
                with self.assertRaises(ValueError):counts.parse_reports([self.xml(folder,text)])
    def test_ignored_test_is_not_counted_as_executed_or_target_check(self):
        with tempfile.TemporaryDirectory() as folder:
            path=self.xml(folder,'<testsuite name="Fixture" tests="3" failures="1" errors="0" skipped="1"><testcase name="a"/><testcase name="b"><skipped/></testcase><testcase name="c"><failure/></testcase></testsuite>')
            observed=counts.parse_reports([path])
            self.assertEqual((observed['discovered'],observed['executed'],observed['skipped'],observed['failed']),(3,2,1,1))
            self.assertIsNone(observed['target_checks'])
    def test_formatter_is_restored_after_controlled_exception(self):
        original=(LATEST/'count-observer-framework/defects4j.build.original.xml').read_bytes()
        with tempfile.TemporaryDirectory() as folder:
            framework=Path(folder);path=framework/'framework/projects/defects4j.build.xml';path.parent.mkdir(parents=True);path.write_bytes(original)
            with self.assertRaisesRegex(RuntimeError,'controlled stop'):
                with counts.observe_framework(framework,framework/'evidence'):
                    self.assertNotEqual(path.read_bytes(),original)
                    raise RuntimeError('controlled stop')
            self.assertEqual(path.read_bytes(),original)
            self.assertTrue(read(framework/'evidence/restoration.json')['restored_exact_bytes'])
    def test_already_augmented_framework_is_rejected(self):
        original=(LATEST/'count-observer-framework/defects4j.build.original.xml').read_bytes()
        modified=counts.augment_formatter(original)
        with self.assertRaises(ValueError):counts.augment_formatter(modified)
class ActualEvidenceGuards(unittest.TestCase):
    def test_all_four_real_stages_have_matching_actual_counts(self):
        rows=read(LATEST/'results.json');self.assertEqual(len(rows),4)
        for row in rows:
            self.assertEqual(row['status'],'complete');self.assertEqual(row['fixed_validation'],'passed_twice')
            self.assertEqual(set(row['stages']),{'fixed-1','fixed-2','buggy','coverage'})
            for stage in row['stages'].values():
                self.assertEqual(stage['executed'],row['test_count']);self.assertEqual(stage['skipped'],0);self.assertEqual(stage['errors'],0)
                self.assertEqual(stage['target_checks'],row['test_count'] if row['approach'] in {'cmaes','fscs-art'} else None)
            record=read(LATEST/row['approach']/'evaluation/record.json')
            self.assertEqual(row['record_sha256'],sha(LATEST/row['approach']/'evaluation/record.json'))
            self.assertEqual(row['fault_detected'],record['fault_detected'])
            self.assertEqual(row['stages']['buggy']['failed'],record['stages']['buggy']['failure_count'])
            self.assertEqual(record['stages']['fixed-1']['failure_count'],0);self.assertEqual(record['stages']['fixed-2']['failure_count'],0)
    def test_suite_bytes_and_producer_inputs_are_unchanged(self):
        seal=read(LATEST/'preexecution-seal.json')
        self.assertEqual(seal['producer_sha256'],sha(ROOT/'scripts/study/api854/evaluate_champ_csv_messages_ready.py'))
        self.assertEqual(seal['counter_observer_sha256'],sha(ROOT/'scripts/study/api854/ready_d4j_counts.py'))
        for approach,manifest in seal['suites'].items():
            self.assertEqual(manifest['suite_sha256'],sha(LATEST/approach/'suite.tar.bz2'))
            self.assertEqual((LATEST/approach/'suite.tar.bz2').read_bytes(),(INTAKE/'champ-csv-messages-native-measurement-v1'/approach/'packaged-suite/suite.tar.bz2').read_bytes())
        snapshot=ROOT/'.local/api854/beam-v12-snapshot-63ad1956-v2'
        for path,digest in seal['frozen_runtime_source_sha256'].items():self.assertEqual(sha(snapshot/path),digest)
        for path,digest in seal['current_beam_runtime_before_sha256'].items():self.assertEqual(sha(ROOT/path),digest)
    def test_official_isolated_bug_is_not_assumed_equal_to_native_parent(self):
        derived=read(LATEST/'isolated-bug-reference/derivation.json')
        self.assertTrue(derived['patch_git_blob_verified']);self.assertFalse(derived['native_and_isolated_buggy_sources_identical'])
        self.assertFalse(derived['production_source_replacement']);self.assertEqual(derived['patch_exit_code'],0)
        seal=read(LATEST/'preexecution-seal.json')
        for approach in ['cmaes','fscs-art','kku-claude','kku-gemini']:
            tree=Path('/home/beam/sqa-beam/worktrees/beam-csv-messages-ready-evaluator-v3')/approach/'Csv/1/b'
            for path,digest in seal['expected_isolated_buggy_source_sha256'].items():self.assertEqual(sha(tree/path),digest)
    def test_new_valid_condition_does_not_erase_baseline_invalids_or_promote_primary(self):
        baseline={r['approach']:r for r in read(OLD/'results.json')}
        self.assertEqual(baseline['kku-claude']['status'],'invalid_generation_truncated');self.assertEqual(baseline['kku-gemini']['status'],'invalid')
        self.assertNotEqual(read(OLD/'receipt.json')['generation_condition'],read(LATEST/'receipt.json')['generation_condition'])
        for packet in [OLD,LATEST]:
            receipt=read(packet/'receipt.json');self.assertEqual(receipt['primary_results_added'],0);self.assertEqual(receipt['generation_requests'],0);self.assertEqual(receipt['queue_mutations'],0);self.assertFalse(receipt['gate_a_approved'])
            restoration=read(packet/'count-observer-framework/restoration.json');self.assertTrue(restoration['restored_exact_bytes']);self.assertTrue(restoration['observed_bytes_unchanged'])
    def test_cli_is_quarantined_and_parser_preserves_arguments(self):
        receipt=read(CLI/'receipt.json');self.assertTrue(receipt['raw_native_fault_flag_retained']);self.assertFalse(receipt['confirmed_semantic_fault']);self.assertEqual(receipt['scientific_fault_count_added'],0);self.assertFalse(receipt['shared_oracle_changed'])
        fixed=receipt['observations']['fixed-1'];buggy=receipt['observations']['buggy-1']
        self.assertNotEqual(fixed['raw'],buggy['raw']);self.assertEqual(fixed['decoded']['option_multiset'],buggy['decoded']['option_multiset']);self.assertEqual(fixed['decoded']['positional_arguments'],buggy['decoded']['positional_arguments'])
        altered=fixed['raw'].replace('cG9zaXRpb25hbA==','b3RoZXI=')
        self.assertNotEqual(cli.decode(altered)['positional_arguments'],cli.decode(fixed['raw'])['positional_arguments'])
        with self.assertRaises(ValueError):cli.decode(fixed['raw'].replace('option:extra:','option:x:'))
if __name__=='__main__':unittest.main(verbosity=2)
