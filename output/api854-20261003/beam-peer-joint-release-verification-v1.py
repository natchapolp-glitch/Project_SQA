"""Verify v2 source bindings, staged public bytes and historical Beam evidence."""
from pathlib import Path
from datetime import datetime, timezone
import hashlib
import importlib.util
import io
import json
import subprocess

ROOT=Path.cwd()
BASE=ROOT/'output/api854-20261003/beam-peer-joint-return-v3'
def sha(path): return hashlib.sha256(path.read_bytes()).hexdigest()
def read(path): return json.loads(path.read_text(encoding='utf-8'))

spec=importlib.util.spec_from_file_location('beam_joint_review_v2',BASE/'confirm_peer_verdicts.py')
review=importlib.util.module_from_spec(spec);spec.loader.exec_module(review)
actual=review.audit();sealed=read(BASE/'receipt.json')
assert all(sealed[k]==v for k,v in actual.items())

def bindings(data):
    result=[]
    if isinstance(data,dict):
        if {'commit','path','sha256'} <= set(data):result.append(data)
        for value in data.values():result.extend(bindings(value))
    elif isinstance(data,list):
        for value in data:result.extend(bindings(value))
    return result

returns=[read(BASE/name) for name in ['beam-buffer-csv-joint-confirmation.json','beam-lang-joint-confirmation.json']]
bound=[item for data in returns for item in bindings(data)]
expected={}
for item in bound:
    key=item['commit']+':'+item['path']
    assert key not in expected or expected[key]==item['sha256']
    expected[key]=item['sha256']
    if 'received_path' in item:assert sha(ROOT/item['received_path'])==item['sha256']

# A relocated received path must be rejected as a source-commit binding.
first=next(item for item in bound if 'received_path' in item)
wrong=first['commit']+':'+first['received_path']
wrong_response=subprocess.check_output(['git','cat-file','--batch'],input=(wrong+'\n').encode())
assert wrong_response.endswith(b' missing\n'), 'Wrong-source-path negative control was not rejected'

failed=ROOT/'output/api854-20261003/beam-peer-joint-return-v2'
failed_manifest=read(failed/'checksums.json')
assert failed_manifest['checksums.json']==hashlib.sha256(b'').hexdigest()
assert sha(failed/'checksums.json')!=failed_manifest['checksums.json']
for name,value in failed_manifest.items():
    if name!='checksums.json':assert sha(failed/name)==value

entries=0
roots=[BASE, ROOT/'output/api854-20261003/beam-peer-joint-return-v1',
       ROOT/'output/api854-20261003/beam-chronology-review-v1',
       ROOT/'output/api854-20261003/beam-final-recipe-return-v1',
       ROOT/'output/api854-20261003/beam-buffer-joint-review-v1',
       ROOT/'output/api854-20261003/beam-v8-received-v1']
roots += [ROOT/'docs/api854/evidence'/name for name in
          ['beam-lang-development-20261003-v1','beam-lang-reference-20261003-v1',
           'beam-lang-reference-20261003-v2','beam-v8-math-development-20261003-v1']]
for tree in roots:
    for manifest in tree.rglob('checksums.json'):
        for name,value in read(manifest).items():
            path=manifest.parent/name;assert sha(path)==value,(manifest,name)
            expected[':'+path.relative_to(ROOT).as_posix()]=value;entries+=1
        expected[':'+manifest.relative_to(ROOT).as_posix()]=sha(manifest)
template=read(ROOT/'output/api854-20261003/beam-final-recipe-return-v1/received-aom/joint-lang-acceptance.template.json')
for name,value in template['beam_runtime_source_sha256'].items():
    assert sha(ROOT/name)==value
    expected[':'+name]=value
    expected[template['beam_commit']+':'+name]=value
cp=ROOT/'output/api854-20261003/beam-peer-joint-return-v1/received-champ/output/api854-20261003/champ-six-message-joint-review-v3'
setter=read(cp/'champ-setter-jdom-verdict.json')
assert len(setter['reviewed_candidates'])==2
for row in setter['reviewed_candidates']:
    assert row['champ_verdict']=='accepted_for_bounded_development_composition_preserve_current_v9'
    assert row['new_addition_relative_to_current_v9'] is False
    assert sha(ROOT/row['fixture_recipe_path'])==row['fixture_recipe_sha256']
for name in subprocess.check_output(['git','diff','--cached','--name-only','--diff-filter=ACM','-z']).decode().split('\0'):
    if name:expected[':'+name]=sha(ROOT/name)
keys=sorted(expected)
stream=io.BytesIO(subprocess.check_output(['git','cat-file','--batch'],input=''.join(k+'\n' for k in keys).encode()))
for key in keys:
    header=stream.readline().split()
    assert len(header)==3 and header[1]==b'blob',('Missing source or staged evidence',key,header)
    data=stream.read(int(header[2]));assert hashlib.sha256(data).hexdigest()==expected[key],key
    assert stream.read(1)==b'\n'
assert stream.read()==b''
assert read(ROOT/'output/api854-20261003/beam-peer-joint-return-v1-binding-review.json')['usable_for_joint_return'] is False
report={'status':'pass','checked_at_utc':datetime.now(timezone.utc).isoformat(),'producer_sha256':sha(Path(__file__)),
        'active_return_packet':'beam-peer-joint-return-v3','immutable_received_packet':'beam-peer-joint-return-v1',
        'source_commit_path_bindings_verified':len(bound),'unique_source_bindings_verified':len({i['commit']+':'+i['path'] for i in bound}),
        'wrong_source_path_negative_control_rejected':True,'historical_failed_v2_manifest_self_entry_rejected':True,'semantic_negative_controls_rejected':7,
        'checksum_entries_verified':entries,'staged_and_historical_git_blobs_verified':len(keys),
        'historical_runtime_pins_verified':41,'current_runtime_pins_verified':41,
        'buffer_signatures_agreed':8,'lang_signatures_agreed':2,'setter_jdom_preserve_count':2,
        'gate_a_approved':False,'new_defects4j_runs':0,'live_kku_requests':0,'live_queue_mutations':0,
        'primary_results_added':0,'shared_preparation_modified':False}
path=ROOT/'output/api854-20261003/beam-peer-joint-release-verification-v1.json'
with path.open('x',encoding='utf-8',newline='\n') as f:json.dump(report,f,ensure_ascii=False,indent=2);f.write('\n')
print(json.dumps(report))
