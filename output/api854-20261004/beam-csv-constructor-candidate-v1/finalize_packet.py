"""One-shot receipt/checksum finalization only after full independent guards pass."""
import json,subprocess,sys
from pathlib import Path
import verify_native as native
BASE=Path(__file__).resolve().parent
def main():
    native.require(not (BASE/'receipt.json').exists() and not (BASE/'checksums.json').exists(),'Already finalized')
    tests=subprocess.run([sys.executable,'-B','test_evidence.py'],cwd=BASE,capture_output=True)
    (BASE/'focused-tests.stdout.log').write_bytes(tests.stdout);(BASE/'focused-tests.stderr.log').write_bytes(tests.stderr)
    native.write(BASE/'focused-tests.command.json',{'argv':[sys.executable,'-B','test_evidence.py'],'cwd':str(BASE),'exit_code':tests.returncode,'source_sha256':native.sha(BASE/'test_evidence.py')})
    native.require(tests.returncode==0 and b'Ran 5 tests' in tests.stderr and b'OK' in tests.stderr,'Guards failed')
    receipt={'status':'pass','scope':'Bounded real Reader constructor fixture plus first-character/close binding integration, manual component development only','declarations':1,'unique_cases':5,'fixed_observations':10,'buggy_observations':10,'native_receipt_sha256':native.sha(BASE/'native-v2/receipt.json'),'evaluator_receipt_sha256':native.sha(BASE/'d4j-v1/receipt.json'),'measurement_record_sha256':native.sha(BASE/'d4j-v1/measurement/record.json'),'suite_sha256':native.sha(BASE/'d4j-v1/packaged/suite.tar.bz2'),'policy_sha256':native.sha(BASE/'policy.json'),'producer_sha256':{p.name:native.sha(p) for p in [BASE/'verify_native.py',BASE/'verify_evaluator.py',BASE/'test_evidence.py',Path(__file__)]},'focused_tests':{'passed':5,'skipped':0},'source_condition':'shared-v12-development','source_commit':native.POLICY['source_commit'],'shared_selected':403,'shared_excluded':288,'denominator':691,'new_full_defects4j_evaluations':1,'primary_results_added':0,'manual_component_fault_detected':True,'fault_attribution':'CRLF follow-up read/line-count behavior, not constructor defect','semantic_validity':'pending_joint_review','full_legal_domain_approved':False,'shared_integration_approved':False,'gate_a_approved':False,'all_691_complete':False,'enum_4_decision':'pending_team_decision','kku_requests':0,'queue_mutations':0,'final_reserve':None}
    native.write(BASE/'receipt.json',receipt)
    native.write(BASE/'checksums.json',{p.relative_to(BASE).as_posix():native.sha(p) for p in sorted(BASE.rglob('*')) if p.is_file() and '__pycache__' not in p.parts})
    print(json.dumps({'status':'pass','receipt_sha256':native.sha(BASE/'receipt.json'),'checksums_sha256':native.sha(BASE/'checksums.json'),'suite_sha256':receipt['suite_sha256']}))
if __name__=='__main__':main()
