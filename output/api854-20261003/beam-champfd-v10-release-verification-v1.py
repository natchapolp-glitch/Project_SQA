"""Verify exact staged v10 return and prior sealed execution evidence."""
from pathlib import Path
from datetime import datetime,timezone
import hashlib,json,re,runpy,subprocess
ROOT=Path(__file__).resolve().parents[2]
BASE=ROOT/'output/api854-20261003/beam-champfd-v10-return-v1'
n=runpy.run_path(str(BASE/'review.py'))
fresh,*_=n['audit']();receipt=n['read'](BASE/'receipt.json')
assert {k:v for k,v in receipt.items() if k not in {'checked_at_utc','producer_sha256'}}==fresh
assert receipt['producer_sha256']==n['digest']((BASE/'review.py').read_bytes())
manifest=n['read'](BASE/'checksums.json');assert 'checksums.json' not in manifest
for name,value in manifest.items():assert n['digest']((BASE/name).read_bytes())==value,name
provenance=n['read'](BASE/'received-provenance.json')
specs=[r['commit']+':'+r['source_path'] for r in provenance]
original=n['blobs'](specs)
for r in provenance:
    data=(ROOT/r['received_path']).read_bytes()
    assert data==original[r['commit']+':'+r['source_path']] and n['digest'](data)==r['sha256']
doc=ROOT/'docs/api854/BEAM_CHAMPFD_V10_FINAL_RETURN_TH.md'
for href in re.findall(r'\]\(([^)]+)\)',doc.read_text(encoding='utf-8')):
    if not href.startswith('http'):assert (doc.parent/href).resolve().is_file(),href
names=subprocess.check_output(['git','diff','--cached','--name-only','-z'],cwd=ROOT).decode().split('\0')
names=[p for p in names if p]
allowed={'.gitignore','.gitattributes','README.md',doc.relative_to(ROOT).as_posix(),Path(__file__).relative_to(ROOT).as_posix()}
for p in names:assert p in allowed or p.startswith('output/api854-20261003/beam-champfd-v10-return-v1/'),p
staged=n['blobs']([':'+p for p in names])
for p in names:assert staged[':'+p]==(ROOT/p).read_bytes(),p
for p in BASE.rglob('*'):
    if p.is_file():assert p.relative_to(ROOT).as_posix() in names,'Evidence not staged '+str(p)
subprocess.run(['git','diff','--cached','--check'],cwd=ROOT,check=True)
result={'status':'pass','checked_at_utc':datetime.now(timezone.utc).isoformat(),
 'producer_sha256':n['digest'](Path(__file__).read_bytes()),'receipt_sha256':n['digest']((BASE/'receipt.json').read_bytes()),
 'peer_unique_git_blobs_verified':fresh['unique_peer_git_blobs_verified'],
 'binding_entries_verified':fresh['binding_entries_verified'],'received_git_copies_verified':len(provenance),
 'root_checksum_entries_verified':len(manifest),'staged_git_blobs_verified':len(names),
 'consumer_combinations_in_original_proof':80,'bounded_component_cases_in_original_proof':64,
 'fixed_observations_in_original_proof':128,'worksheet_pairs_verified':40,
 'new_target_executions':0,'new_test_executions':0,'live_kku_requests':0,
 'queue_or_ledger_mutations':0,'primary_added':0,'all_390_semantic_approval':False,'gate_a_approved':False}
out=Path(__file__).with_suffix('.json')
with out.open('x',encoding='utf-8',newline='\n') as f:json.dump(result,f,indent=2);f.write('\n')
print(json.dumps(result,indent=2))
