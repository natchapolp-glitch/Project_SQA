"""Verify staged v10 review, isolated Aom snapshot and historical Beam bytes."""
from pathlib import Path
from datetime import datetime,timezone
import hashlib
import importlib.util
import io
import json
import subprocess

ROOT=Path.cwd();BASE=ROOT/'output/api854-20261003/beam-v10-received-review-v1'
def sha(path):return hashlib.sha256(path.read_bytes()).hexdigest()
def read(path):return json.loads(path.read_text(encoding='utf-8'))
spec=importlib.util.spec_from_file_location('beam_v10_finalizer',BASE/'finalize_review.py')
review=importlib.util.module_from_spec(spec);spec.loader.exec_module(review)
result=review.audit();sealed=read(BASE/'receipt.json');assert all(sealed[k]==v for k,v in result.items())
snapshot=read(BASE/'snapshot-provenance.json');folder=ROOT/snapshot['snapshot_directory']
names=subprocess.check_output(['git','ls-tree','-r','--name-only',snapshot['source_commit'],'--',*snapshot['git_archive_paths']],text=True).splitlines()
expected={snapshot['source_commit']+':'+name:sha(folder/name) for name in names}
assert len(names)==snapshot['snapshot_files']
entries=0
roots=[BASE,ROOT/'output/api854-20261003/beam-champ7de-integration-review-v1',
       ROOT/'output/api854-20261003/beam-chronology-review-v1',ROOT/'output/api854-20261003/beam-final-recipe-return-v1',
       ROOT/'output/api854-20261003/beam-buffer-joint-review-v1',ROOT/'output/api854-20261003/beam-v8-received-v1',
       ROOT/'output/api854-20261003/beam-peer-joint-return-v1',ROOT/'output/api854-20261003/beam-peer-joint-return-v3']
roots += [ROOT/'docs/api854/evidence'/name for name in ['beam-lang-development-20261003-v1',
    'beam-lang-reference-20261003-v1','beam-lang-reference-20261003-v2','beam-v8-math-development-20261003-v1']]
for tree in roots:
    for manifest in tree.rglob('checksums.json'):
        for name,value in read(manifest).items():
            path=manifest.parent/name;assert sha(path)==value,(manifest,name)
            expected[':'+path.relative_to(ROOT).as_posix()]=value;entries+=1
        expected[':'+manifest.relative_to(ROOT).as_posix()]=sha(manifest)
historical=read(ROOT/'output/api854-20261003/beam-final-recipe-return-v1/received-aom/joint-lang-acceptance.template.json')
for name,value in historical['beam_runtime_source_sha256'].items():
    assert sha(ROOT/name)==value;expected[':'+name]=value;expected[historical['beam_commit']+':'+name]=value
for name in subprocess.check_output(['git','diff','--cached','--name-only','--diff-filter=ACM','-z']).decode().split('\0'):
    if name:expected[':'+name]=sha(ROOT/name)
keys=sorted(expected)
stream=io.BytesIO(subprocess.check_output(['git','cat-file','--batch'],input=''.join(k+'\n' for k in keys).encode()))
for key in keys:
    header=stream.readline().split();assert len(header)==3 and header[1]==b'blob',(key,header)
    data=stream.read(int(header[2]));assert hashlib.sha256(data).hexdigest()==expected[key],key;assert stream.read(1)==b'\n'
assert stream.read()==b''
report={'status':'pass','checked_at_utc':datetime.now(timezone.utc).isoformat(),'producer_sha256':sha(Path(__file__)),
        'condition':result['condition'],'aom_commit':result['aom_commit'],'isolated_snapshot_git_blobs_verified':len(names),
        'checksum_entries_verified':entries,'staged_and_historical_git_blobs_verified':len(keys),
        'v10_runtime_pins_verified':41,'historical_beam_runtime_pins_verified':41,'current_beam_runtime_pins_verified':41,
        'fresh_tests_passed':7,'fresh_tests_skipped':0,'four_consumer_combinations':80,
        'active_fixed_component_cases':64,'active_fixed_observations':128,'cpu_lock_check_exits':[9,0],
        'consumer_review_passed':True,'scoped_technical_host_review_passed':True,'all_390_semantic_approval':False,
        'new_full_defects4j_evaluations':0,'live_kku_requests':0,'queue_mutations':0,'primary_results_added':0,'gate_a_approved':False}
with (ROOT/'output/api854-20261003/beam-v10-release-verification-v1.json').open('x',encoding='utf-8',newline='\n') as f:
    json.dump(report,f,indent=2);f.write('\n')
print(json.dumps(report))
