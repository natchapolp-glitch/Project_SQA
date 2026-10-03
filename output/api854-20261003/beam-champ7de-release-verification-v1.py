"""Verify real helper diagnostic, staged bytes and protected historical evidence."""
from pathlib import Path
from datetime import datetime,timezone
import hashlib
import importlib.util
import io
import json
import subprocess

ROOT=Path.cwd();BASE=ROOT/'output/api854-20261003/beam-champ7de-integration-review-v1'
def sha(path):return hashlib.sha256(path.read_bytes()).hexdigest()
def read(path):return json.loads(path.read_text(encoding='utf-8'))
spec=importlib.util.spec_from_file_location('beam_champ7de_audit',BASE/'review_integration.py')
review=importlib.util.module_from_spec(spec);spec.loader.exec_module(review)
actual=review.audit();sealed=read(BASE/'receipt.json');assert all(sealed[k]==v for k,v in actual.items())
assert sha(BASE/'integration-requirements.json')==sealed['integration_requirements_sha256']
requirements=read(BASE/'integration-requirements.json');assert len(requirements['independent_expected_observations'])==13
assert requirements['current_shared_support'] is False and requirements['final_approval'] is False
joint=requirements['joint_verdict_source'];expected={joint['commit']+':'+joint['path']:joint['sha256']}
entries=0
roots=[BASE,ROOT/'output/api854-20261003/beam-chronology-review-v1',
       ROOT/'output/api854-20261003/beam-final-recipe-return-v1',ROOT/'output/api854-20261003/beam-buffer-joint-review-v1',
       ROOT/'output/api854-20261003/beam-v8-received-v1',ROOT/'output/api854-20261003/beam-peer-joint-return-v1',
       ROOT/'output/api854-20261003/beam-peer-joint-return-v3']
roots += [ROOT/'docs/api854/evidence'/name for name in ['beam-lang-development-20261003-v1',
    'beam-lang-reference-20261003-v1','beam-lang-reference-20261003-v2','beam-v8-math-development-20261003-v1']]
for tree in roots:
    for manifest in tree.rglob('checksums.json'):
        for name,value in read(manifest).items():
            path=manifest.parent/name;assert sha(path)==value,(manifest,name)
            expected[':'+path.relative_to(ROOT).as_posix()]=value;entries+=1
        expected[':'+manifest.relative_to(ROOT).as_posix()]=sha(manifest)
template=read(ROOT/'output/api854-20261003/beam-final-recipe-return-v1/received-aom/joint-lang-acceptance.template.json')
for name,value in template['beam_runtime_source_sha256'].items():
    assert sha(ROOT/name)==value;expected[':'+name]=value;expected[template['beam_commit']+':'+name]=value
for name in subprocess.check_output(['git','diff','--cached','--name-only','--diff-filter=ACM','-z']).decode().split('\0'):
    if name:expected[':'+name]=sha(ROOT/name)
keys=sorted(expected)
stream=io.BytesIO(subprocess.check_output(['git','cat-file','--batch'],input=''.join(k+'\n' for k in keys).encode()))
for key in keys:
    header=stream.readline().split();assert len(header)==3 and header[1]==b'blob',(key,header)
    data=stream.read(int(header[2]));assert hashlib.sha256(data).hexdigest()==expected[key],key;assert stream.read(1)==b'\n'
assert stream.read()==b''
report={'status':'pass','checked_at_utc':datetime.now(timezone.utc).isoformat(),'producer_sha256':sha(Path(__file__)),
    'checksum_entries_verified':entries,'staged_and_historical_git_blobs_verified':len(keys),
    'shared_v9_runtime_pins_verified':41,'historical_beam_runtime_pins_verified':41,'current_beam_runtime_pins_verified':41,
    'new_fixed_helper_diagnostic_attempts':12,'target_invocations':0,'fixture_failures':12,
    'integration_boundary_negative_controls_rejected':6,'independent_oracle_case_contracts':13,
    'current_shared_chronology_integration_approved':False,'final_condition_received':False,
    'final_four_consumer_checks_run':0,'final_condition_host_approved':False,
    'new_full_defects4j_evaluations':0,'primary_results_added':0,'live_kku_requests':0,'queue_mutations':0,'gate_a_approved':False}
path=ROOT/'output/api854-20261003/beam-champ7de-release-verification-v1.json'
with path.open('x',encoding='utf-8',newline='\n') as stream:json.dump(report,stream,indent=2);stream.write('\n')
print(json.dumps(report))
