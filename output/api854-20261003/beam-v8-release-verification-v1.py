"""Verify every retained checksum and exact staged Git bytes before release."""
from pathlib import Path
import hashlib
import json
import subprocess
import sys
from datetime import datetime,timezone

ROOT=Path.cwd();sys.path.insert(0,str(ROOT))
from scripts.study.api854.common import read_json,write_json,sha256,implementation_hashes

base=ROOT/'output/api854-20261003/beam-v8-received-v1'
packet=ROOT/'docs/api854/evidence/beam-v8-math-development-20261003-v1'
expected={}
entries=0
for tree in (base,packet):
    for manifest in tree.rglob('checksums.json'):
        for rel,h in read_json(manifest).items():
            path=manifest.parent/rel
            assert path.is_file() and sha256(path)==h,(manifest,rel)
            name=path.relative_to(ROOT).as_posix()
            assert expected.get(name,h)==h
            expected[name]=h;entries+=1
        expected[manifest.relative_to(ROOT).as_posix()]=sha256(manifest)
for name in subprocess.check_output(['git','diff','--cached','--name-only','--diff-filter=ACM','-z']).decode().split('\0'):
    if name:expected[name]=sha256(ROOT/name)
current=implementation_hashes()
assert current==read_json(base/'composition/protocol.proposal.json')['source_sha256']
for name,h in current.items():expected[name]=h
names=sorted(expected)
raw=subprocess.check_output(['git','cat-file','--batch'],input=''.join(':'+n+'\n' for n in names).encode())
offset=0
for name in names:
    end=raw.index(b'\n',offset);header=raw[offset:end].split();offset=end+1
    assert len(header)==3 and header[1]==b'blob',('Missing staged evidence',name,header)
    size=int(header[2]);content=raw[offset:offset+size];offset+=size+1
    assert hashlib.sha256(content).hexdigest()==expected[name],('Git byte mismatch',name)
assert offset==len(raw)
for key in ('protocol','preparation_index','runner_plan','prompt_worksheet'):
    b=read_json(base/'beam-v8-review.json')['input_binding'][key]
    assert sha256(ROOT/b['path'])==b['sha256']
review=read_json(base/'beam-v8-review.json')
assert not review['gate_a_passed'] and not review['pilot_authorized']
assert review['real_kku_requests']==review['queue_mutations']==review['primary_results_added']==0
report={'status':'pass','checked_at_utc':datetime.now(timezone.utc).isoformat(),
    'producer_sha256':sha256(__file__),'checksum_entries_verified':entries,'exact_staged_git_blobs_verified':len(names),
    'runtime_source_files_verified':len(current),'historical_task_bindings_unchanged':True,
    'beam_review_sha256':sha256(base/'beam-v8-review.json'),
    'math_reference_sha256':sha256(base/'math-reference/receipt.json'),
    'math_development_checksums_sha256':sha256(packet/'checksums.json'),
    'primary_results_added':0,'gate_a_passed':False,'real_kku_requests':0,'queue_mutations':0,
    'scope':'Exact public file/hash/Git-byte integrity, not new experiment execution or joint semantic approval'}
write_json(ROOT/'output/api854-20261003/beam-v8-release-verification-v1.json',report)
print(json.dumps(report))
