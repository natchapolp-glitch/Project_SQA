"""Meaningful evidence guards: independent values, exact entries and fixture/state separation."""
from pathlib import Path
import copy,gzip,hashlib,importlib.util,json,tarfile,io,unittest
BASE=Path(__file__).resolve().parent
spec=importlib.util.spec_from_file_location('beam_codec_native_verifier',BASE/'verify_native.py')
v=importlib.util.module_from_spec(spec);spec.loader.exec_module(v)
OUT=BASE/'native-v1'
def rows(name='fixed_first'):return v.records((OUT/(name+'.stdout.log')).read_bytes())
class CodecEvidenceTests(unittest.TestCase):
    def test_sealed_production_sources_and_shared_runtime_unchanged(self):
        proof=v.read(OUT/'receipt.json');seal=v.read(OUT/'preexecution-seal.json')
        for name,value in v.read(OUT/'checksums.json').items():self.assertEqual(v.sha(OUT/name),value,name)
        self.assertEqual(v.sha(OUT/'preexecution-seal.json'),proof['preexecution_seal_sha256'])
        for name,value in seal['suite_sha256'].items():self.assertEqual(v.sha(BASE/name),value,name)
        self.assertEqual(v.sha(OUT/'cases.tsv'),seal['cases_tsv_sha256'])
        for name,value in seal['runtime_source_sha256'].items():self.assertEqual(v.sha(v.ROOT/name),value,name)
        for version in ['fixed','buggy']:
            archived=gzip.decompress((OUT/(version+'-production-archive.stdout.tar.gz')).read_bytes())
            command=v.read(OUT/(version+'-production-archive.command.json'))
            self.assertEqual(hashlib.sha256(archived).hexdigest(),command['stdout_sha256'])
            with tarfile.open(fileobj=io.BytesIO(archived)) as t:
                sources={m.name:t.extractfile(m).read() for m in t.getmembers() if m.isfile() and m.name.endswith('.java')}
            if version=='fixed':
                for name in ['Metaphone.java','SoundexUtils.java']:
                    path=v.SOURCE_PREFIX+name
                    retained=(BASE/'received-aom/output/api854-20261003/prepare-v10-joint-development/Codec-1/fixed-source'/path).read_bytes()
                    self.assertEqual(sources[path].replace(b'\r\n',b'\n'),retained.replace(b'\r\n',b'\n'))
                    sources[path]=retained
            self.assertEqual({p:hashlib.sha256(raw).hexdigest() for p,raw in sources.items()},seal['source_sha256'][version])
        self.assertFalse(proof['candidate_fault_detected']);self.assertEqual(proof['buggy_failed_cases'],[])
        self.assertEqual(proof['cpu_lock_exits'],[9,0]);self.assertFalse(proof['shared_integration_approved'])
    def test_repeated_production_and_trace_cases_match_exact_five_targets(self):
        proof=v.read(OUT/'receipt.json')
        for version in ['fixed','buggy']:
            checked=[]
            for suffix in ['first','second','method_entry']:
                stage=version+'_'+suffix;records=rows(stage)
                checked.append(v.validate(records,allow_failures=version=='buggy'))
                self.assertEqual(checked[-1],proof['stages'][stage]['cases'])
                if suffix=='method_entry':self.assertEqual(len(v.validate_trace(records)),43)
            self.assertEqual(checked[0],checked[1]);self.assertEqual(checked[0],checked[2])
    def test_actual_boolean_and_encoder_score_mutations_are_detected(self):
        for name,expected_count in [('next_char',3),('difference_score',9)]:
            checked=v.validate(rows(name+'-mutation'),allow_failures=True)
            failed=[r for r in checked if not r['target_check_passed']]
            self.assertEqual(len(failed),expected_count)
            self.assertTrue(all(r['failure_class']=='java.lang.AssertionError' for r in failed))
    def test_coherent_wrong_boolean_or_score_cannot_redefine_oracle(self):
        for case in ['isNextChar_match_first','difference_equal']:
            records=rows();row=next(r for r in records if r.get('case')==case)
            value=False if case.startswith('is') else 2
            row['observation']['value']=row['expected_observation']['value']=value
            with self.assertRaisesRegex(ValueError,'Independent oracle'):v.validate(records)
    def test_wrong_receiver_or_first_entry_descriptor_rejected(self):
        records=rows();records[0]['receiver_class']='java.lang.StringBuffer'
        with self.assertRaisesRegex(ValueError,'receiver'):v.validate(records)
        records=rows('fixed_method_entry');next(r for r in records if r.get('method_entry'))['descriptor']='()V'
        with self.assertRaisesRegex(ValueError,'entry descriptor'):v.validate_trace(records)
    def test_fixture_or_skipped_cases_cannot_be_target_evidence(self):
        for field in ['setup_succeeded','target_invoked']:
            records=rows();records[0][field]=False
            with self.assertRaisesRegex(ValueError,'Fixture error'):v.validate(records)
        records=rows();records[-1]['skipped']=1
        with self.assertRaisesRegex(ValueError,'Counters'):v.validate(records)
    def test_changed_buffer_or_encoder_state_cannot_pass_coherent_expectations(self):
        records=rows();records[0]['observation']['buffer_contents']='XYZ';records[0]['expected_observation']['buffer_contents']='XYZ'
        with self.assertRaisesRegex(ValueError,'Independent oracle'):v.validate(records)
        records=rows();records[0]['post_state']['buffer_capacity']+=1
        with self.assertRaisesRegex(ValueError,'Buffer state changed'):v.validate(records)
        records=rows();row=next(r for r in records if r.get('case')=='difference_equal')
        row['observation']['encoder_max_code_len']=row['expected_observation']['encoder_max_code_len']=5
        with self.assertRaisesRegex(ValueError,'Independent oracle'):v.validate(records)
if __name__=='__main__':unittest.main(verbosity=2)
