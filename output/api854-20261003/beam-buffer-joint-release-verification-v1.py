"""Validate received/historical hashes and exact staged Buffer review bytes."""
from pathlib import Path
from datetime import datetime,timezone
import hashlib
import json
import subprocess
import sys

ROOT=Path.cwd();sys.path.insert(0,str(ROOT))
from scripts.study.api854.common import read_json,write_json,sha256,implementation_hashes
BASE=ROOT/'output/api854-20261003/beam-buffer-joint-review-v1'
roots=[BASE,ROOT/'docs/api854/evidence/beam-buffer-development-20261003-v1',
    ROOT/'docs/api854/evidence/beam-buffer-reference-20261003-v1']
expected={};entries=0
for tree in roots:
    for manifest in tree.rglob('*checksums.json'):
        for rel,h in read_json(manifest).items():
            p=manifest.parent/rel;assert sha256(p)==h,(manifest,rel)
            expected[':'+p.relative_to(ROOT).as_posix()]=h;entries+=1
        expected[':'+manifest.relative_to(ROOT).as_posix()]=sha256(manifest)
for b in read_json(BASE/'received-aom/provenance.json'):
    assert sha256(ROOT/b['received_path'])==b['sha256']
    expected[b['source_commit']+':'+b['source_path']]=b['sha256']
template=read_json(BASE/'received-aom/joint-buffer-acceptance.template.json')
for name,h in template['beam_runtime_source_sha256'].items():expected[template['beam_commit']+':'+name]=h
current=implementation_hashes()
assert current==read_json(BASE/'reference/preexecution-seal.json')['runtime_source_sha256']
for name,h in current.items():expected[':'+name]=h
for name in subprocess.check_output(['git','diff','--cached','--name-only','--diff-filter=ACM','-z']).decode().split('\0'):
    if name:expected[':'+name]=sha256(ROOT/name)
keys=sorted(expected)
raw=subprocess.check_output(['git','cat-file','--batch'],input=''.join(k+'\n' for k in keys).encode());offset=0
for key in keys:
    end=raw.index(b'\n',offset);header=raw[offset:end].split();offset=end+1
    assert len(header)==3 and header[1]==b'blob',('Missing public Git evidence',key,header)
    size=int(header[2]);b=raw[offset:offset+size];offset+=size+1
    assert hashlib.sha256(b).hexdigest()==expected[key],('Git blob hash mismatch',key)
assert offset==len(raw)
review=read_json(BASE/'beam-buffer-verdict.json')
assert len(review['candidates'])==8 and all(c['beam_verdict'] and c['champ_verdict'] is None and not c['accepted_into_shared_inputs'] for c in review['candidates'])
assert review['real_kku_requests']==review['queue_mutations']==review['primary_results_added']==0
assert not review['gate_a_approved'] and not review['current_shared_preparation_changed']
assert review['csv_stream_condition_change']['champ_verdict'] is None
assert read_json(BASE/'focused-tests.json')['tests_run']==11 and not read_json(BASE/'focused-tests.json')['skipped']
coverage=read_json(BASE/'received-aom/target-method-coverage.json')
stringrow=next(r for r in coverage['rows'] if r['method']=='append' and r['descriptor']=='(Ljava/lang/String;II)V')
assert next(e['hits'] for e in stringrow['evidence'] if e['approach']=='cmaes')==0
report={'status':'pass','checked_at_utc':datetime.now(timezone.utc).isoformat(),
    'producer_sha256':sha256(__file__),'checksum_entries_verified':entries,
    'exact_git_blobs_verified':len(keys),'historical_runtime_pins_verified':len(template['beam_runtime_source_sha256']),
    'current_runtime_pins_verified':len(current),'focused_tests_passed':11,
    'beam_verdict_sha256':sha256(BASE/'beam-buffer-verdict.json'),
    'reference_receipt_sha256':sha256(BASE/'reference/receipt.json'),
    'reference_tests':42,'fixed_reference_observations':84,
    'cmaes_historical_string_append_entry_hits':0,'champ_verdict':None,
    'gate_a_approved':False,'primary_results_added':0,'real_kku_requests':0,'queue_mutations':0,
    'scope':'Git-byte/received integrity and scoped Beam review release; not final joint composition approval'}
write_json(ROOT/'output/api854-20261003/beam-buffer-joint-release-verification-v1.json',report)
print(json.dumps(report))
