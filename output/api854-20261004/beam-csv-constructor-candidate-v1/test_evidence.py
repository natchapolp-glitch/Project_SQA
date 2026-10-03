"""Reject truncated observations, changed sealed inputs and incorrect fault attribution."""
import copy,json,unittest
from pathlib import Path
import verify_native as producer
BASE=Path(__file__).resolve().parent
def read(path):return json.loads(Path(path).read_bytes())
class EvidenceGuards(unittest.TestCase):
    def test_case_inventory_rejects_missing_duplicate_and_reorder(self):
        rows=read(BASE/'native-v2/receipt.json')['stages']['fixed_first']['observations']
        for invalid in [rows[:-1],rows+[rows[0]],list(reversed(rows))]:
            with self.assertRaises(ValueError):producer.validate(invalid)
    def test_independent_oracle_rejects_wrong_initial_state_and_type(self):
        rows=read(BASE/'native-v2/receipt.json')['stages']['fixed_first']['observations']
        for key,value in [('initial_last',0),('initial_lines',1),('initial_reads',1),('initial_closes',1),('close_count',0),('initial_reads',False)]:
            altered=copy.deepcopy(rows);altered[1]['observation'][key]=value
            self.assertEqual(producer.validate(altered)[1],['ascii'])
    def test_producers_and_received_sources_match_preexecution_seals(self):
        native=read(BASE/'native-v2/preexecution-seal.json')
        for path,digest in native['suite_sha256'].items():self.assertEqual(producer.sha(BASE/path),digest)
        provenance=read(BASE/'received-provenance.json')
        for path,digest in provenance['sha256'].items():self.assertEqual(producer.sha(BASE/'received-aom'/path),digest)
        source='src/main/java/org/apache/commons/csv/ExtendedBufferedReader.java'
        self.assertEqual(native['source_sha256']['fixed'][source],producer.sha(BASE/'received-aom/fixed-source'/source))
        evaluator=read(BASE/'d4j-v1/preexecution-seal.json')
        self.assertEqual(evaluator['producer_sha256'],producer.sha(BASE/'verify_evaluator.py'))
        self.assertEqual(evaluator['native_preexecution_seal_sha256'],producer.sha(BASE/'native-v2/preexecution-seal.json'))
        self.assertEqual(evaluator['suite_sha256'],producer.sha(BASE/'d4j-v1/packaged/suite.tar.bz2'))
        self.assertEqual(evaluator['policy_sha256'],producer.sha(BASE/'policy.json'))
    def test_exact_original_constructor_entries_and_fault_attribution(self):
        receipt=read(BASE/'native-v2/receipt.json')
        for revision in ['fixed','buggy']:
            entries=receipt['stages'][revision+'_entry_trace']['exact_entries']
            self.assertEqual(len(entries),5)
            self.assertEqual({r['case'] for r in entries},{c['case'] for c in producer.POLICY['cases']})
            for entry in entries:
                self.assertEqual((entry['class'],entry['method'],entry['descriptor']),(producer.OWNER,'<init>',producer.POLICY['descriptor']))
                self.assertGreater(entry['source_line'],0)
            observations=receipt['stages'][revision+'_first']['observations']
            for row in observations[:-1]:
                for key in ['initial_last','initial_lines','initial_reads','initial_closes']:
                    self.assertEqual(row['observation'][key],next(c for c in producer.POLICY['cases'] if c['case']==row['case'])['expected'][key])
        # The defect observation is after construction; no constructor fault assertion.
        self.assertEqual(receipt['buggy_failed_cases'],['crlf'])
        self.assertIn('not proof the constructor is buggy',receipt['fault_attribution'])
        self.assertEqual(receipt['controlled_mutation']['failed_cases'],['empty','ascii','crlf','unicode'])
        self.assertEqual(receipt['cpu_lock_exits'],[9,0])
    def test_real_evaluator_all_stages_and_approval_boundaries(self):
        record=read(BASE/'d4j-v1/measurement/record.json')
        self.assertEqual(record['status'],'complete');self.assertEqual(record['fixed_validation'],'passed_twice')
        self.assertTrue(record['fault_detected'])
        for name in ['fixed-1','fixed-2','buggy','coverage']:
            counts=read(BASE/'d4j-v1/measurement'/name/'sqa-stage-counts.json')
            self.assertEqual(counts,{'schema_version':1,'executed':5,'skipped':0,'target_checks':5})
        self.assertGreater(record['line_total'],0);self.assertGreater(record['line_covered'],0)
        receipt=read(BASE/'d4j-v1/receipt.json')
        self.assertEqual(receipt['primary_added'],0);self.assertFalse(receipt['generation_algorithm_result'])
        self.assertFalse(receipt['full_legal_domain_approved']);self.assertFalse(receipt['shared_integration_approved'])
        self.assertFalse(receipt['gate_a_approved']);self.assertEqual(receipt['kku_requests'],0);self.assertEqual(receipt['queue_mutations'],0)
if __name__=='__main__':unittest.main(verbosity=2)
