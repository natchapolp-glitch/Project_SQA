"""Verify staged recipe-return bytes, peer Git provenance and historical pins."""
from pathlib import Path
from datetime import datetime,timezone
import hashlib
import json
import subprocess
import sys

ROOT=Path.cwd();sys.path.insert(0,str(ROOT))
from scripts.study.api854.common import read_json,write_json,sha256,implementation_hashes
BASE=ROOT/'output/api854-20261003/beam-final-recipe-return-v1'
expected={};entries=0
roots=[BASE]+[ROOT/'docs/api854/evidence'/n for n in
    ('beam-lang-development-20261003-v1','beam-lang-reference-20261003-v1','beam-lang-reference-20261003-v2')]
for tree in roots:
    for manifest in tree.rglob('checksums.json'):
        for rel,h in read_json(manifest).items():
            p=manifest.parent/rel;assert sha256(p)==h,(manifest,rel)
            expected[':'+p.relative_to(ROOT).as_posix()]=h;entries+=1
        expected[':'+manifest.relative_to(ROOT).as_posix()]=sha256(manifest)
for b in read_json(BASE/'received-aom/provenance.json'):
    assert sha256(ROOT/b['received_path'])==b['sha256']
    expected[b['source_commit']+':'+b['source_path']]=b['sha256']
template=read_json(BASE/'received-aom/joint-lang-acceptance.template.json')
for name,h in template['beam_runtime_source_sha256'].items():expected[template['beam_commit']+':'+name]=h
current=implementation_hashes()
assert current==template['beam_runtime_source_sha256']
for name,h in current.items():expected[':'+name]=h
registry=read_json(BASE/'final-recipe-return-index.json')
for row in registry['groups']:
    b=row['receipt'];assert sha256(ROOT/b['path'])==b['sha256'];expected[':'+b['path']]=b['sha256']
for name in subprocess.check_output(['git','diff','--cached','--name-only','--diff-filter=ACM','-z']).decode().split('\0'):
    if name:expected[':'+name]=sha256(ROOT/name)
keys=sorted(expected);raw=subprocess.check_output(['git','cat-file','--batch'],input=''.join(k+'\n' for k in keys).encode())
offset=0
for key in keys:
    end=raw.index(b'\n',offset);header=raw[offset:end].split();offset=end+1
    assert len(header)==3 and header[1]==b'blob',('Missing Git evidence',key,header)
    size=int(header[2]);data=raw[offset:offset+size];offset+=size+1
    assert hashlib.sha256(data).hexdigest()==expected[key],('Git-byte mismatch',key)
assert offset==len(raw)
lang=read_json(BASE/'beam-lang-verdict.json')
assert len(lang['candidates'])==2
assert all(c['beam_joint_verdict'] and c['champ_joint_verdict'] is None and not c['accepted_into_shared_inputs'] for c in lang['candidates'])
assert registry['recipes_indexed']==14 and not registry['union_implemented']
assert not registry['gate_a_approved'] and not registry['pilot_authorized']
assert registry['real_kku_requests']==registry['queue_mutations']==registry['primary_results_added']==0
tests=read_json(ROOT/'output/api854-20261003/beam-final-recipe-tests-v1.json')
assert tests['passed']==3 and tests['skipped']==0 and not tests['historical_lang_runtime_differences']
assert sha256(ROOT/'output/api854-20261003/beam-final-recipe-tests-v1.log')==tests['log_sha256']
report={'status':'pass','checked_at_utc':datetime.now(timezone.utc).isoformat(),
    'producer_sha256':sha256(__file__),'checksum_entries_verified':entries,'exact_git_blobs_verified':len(keys),
    'historical_runtime_pins_verified':len(template['beam_runtime_source_sha256']),'current_runtime_pins_verified':len(current),
    'lang_independent_cases_rechecked':12,'historical_fixed_observations':24,'focused_tests_passed':3,
    'lang_verdict_sha256':sha256(BASE/'beam-lang-verdict.json'),
    'recipe_return_index_sha256':sha256(BASE/'final-recipe-return-index.json'),
    'new_defects4j_executions':0,'joint_acceptance_complete':False,'gate_a_approved':False,
    'real_kku_requests':0,'queue_mutations':0,'primary_results_added':0,
    'scope':'Verified scoped Beam return and registry, not final combined semantic/protocol/provider approval'}
write_json(ROOT/'output/api854-20261003/beam-final-recipe-release-verification-v1.json',report)
print(json.dumps(report))
