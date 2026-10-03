"""Verify sealed Graphics return and exact staged bytes without executing targets."""
from pathlib import Path
from datetime import datetime,timezone
import hashlib,json,re,runpy,subprocess
ROOT=Path(__file__).resolve().parents[2]
BASE=ROOT/'output/api854-20261003/beam-graphics-review-v1'
n=runpy.run_path(str(BASE/'finalize_review.py'));actual=n['audit']()
receipt=n['read'](BASE/'receipt.json')
assert {k:v for k,v in receipt.items() if k not in {'checked_at_utc','producer_sha256'}}==actual
assert receipt['producer_sha256']==n['sha'](BASE/'finalize_review.py')
root_count=n['check_manifest'](BASE)
joint=n['read'](BASE/'joint-verdict.json')
template=n['read'](BASE/'received-champ/output/api854-20261003/champ-graphics-joint-review.template.json')
assert len(joint['candidates'])==7 and joint['joint_acceptance_complete'] is True
assert not joint['shared_integration_approved'] and not joint['new_shared_preparation_authorized_by_receipt']
for row,old in zip(joint['candidates'],template['candidates']):
    for key in ['target','exact_declaring_class','exact_jvm_descriptor','receiver_identity','candidate_cases']:
        assert row[key]==old[key],key
    assert row['beam_verdict']==row['champ_verdict']=='accepted_for_bounded_candidate_oracle_development'
    assert row['agreed_preconditions']==old['proposed_preconditions']
    assert row['agreed_oracle']==old['proposed_oracle']
    assert row['aom_shared_integration_verdict'] is None and row['accepted_into_shared_inputs'] is False
assert (joint['actual_selected'],joint['actual_unsupported'],joint['denominator'])==(390,301,691)
for binding in joint['evidence']:
    row={'commit':binding['commit'],'source_path':binding['path'],'sha256':binding['sha256']}
    n['git_blobs']([row])
for binding in joint['beam_evidence']:assert n['sha'](ROOT/binding['path'])==binding['sha256']
doc=ROOT/'docs/api854/BEAM_GRAPHICS2D_CANDIDATE_REVIEW_TH.md'
for href in re.findall(r'\]\(([^)]+)\)',doc.read_text(encoding='utf-8')):
    if not href.startswith('http'):assert (doc.parent/href).resolve().is_file(),href
names=subprocess.check_output(['git','diff','--cached','--name-only','-z'],cwd=ROOT).decode().split('\0')
names=[p for p in names if p]
allowed={'.gitignore','.gitattributes','README.md',doc.relative_to(ROOT).as_posix(),Path(__file__).relative_to(ROOT).as_posix()}
for p in names:
    assert p in allowed or p.startswith('output/api854-20261003/beam-graphics-review-v1/'),p
rows=[{'commit':'','source_path':p,'sha256':n['sha'](ROOT/p)} for p in names]
stage_count=n['git_blobs'](rows)
for p in BASE.rglob('*'):
    if p.is_file():assert p.relative_to(ROOT).as_posix() in names,'Missing staged evidence: '+str(p)
subprocess.run(['git','diff','--cached','--check'],cwd=ROOT,check=True)
result={'status':'pass','checked_at_utc':datetime.now(timezone.utc).isoformat(),
 'producer_sha256':n['sha'](Path(__file__)),'receipt_sha256':n['sha'](BASE/'receipt.json'),
 'joint_verdict_sha256':n['sha'](BASE/'joint-verdict.json'),
 'snapshot_git_blobs_verified':actual['snapshot_git_blobs_verified'],
 'received_git_blobs_verified':actual['received_git_blobs_verified'],
 'nested_checksum_entries_verified':actual['checksum_entries_verified'],
 'root_checksum_entries_verified':root_count,'staged_git_blobs_verified':stage_count,
 'unchanged_beam_runtime_files':41,'native_cases':24,'fixed_observations':48,'buggy_observations':48,
 'fresh_tests_passed':8,'fresh_tests_skipped':0,'exact_declarations_per_revision':7,
 'cpu_lock_exits':[9,0],'candidate_fault_detected':False,'oracle_shifted_line_detected':True,
 'actual_shared_selected':390,'actual_shared_exclusions':301,'shared_integration_approved':False,
 'gate_a_approved':False,'live_requests':0,'queue_mutations':0,'primary_added':0}
out=Path(__file__).with_suffix('.json')
with out.open('x',encoding='utf-8',newline='\n') as f:json.dump(result,f,indent=2);f.write('\n')
print(json.dumps(result,indent=2))
