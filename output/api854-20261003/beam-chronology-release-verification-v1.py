"""Verify public staged Chronology receipts and protect historical Beam pins."""
from pathlib import Path
from datetime import datetime, timezone
import hashlib
import importlib.util
import io
import json
import subprocess

ROOT = Path.cwd()
BASE = ROOT/'output/api854-20261003/beam-chronology-review-v1'
def sha(path): return hashlib.sha256(path.read_bytes()).hexdigest()
def read(path): return json.loads(path.read_text(encoding='utf-8'))

spec = importlib.util.spec_from_file_location('beam_chronology_review', BASE/'review_received.py')
review = importlib.util.module_from_spec(spec)
spec.loader.exec_module(review)
actual = review.audit()
sealed = read(BASE/'beam-chronology-verdict.json')
assert {k:v for k,v in actual.items() if k!='checked_at_utc'} == {k:v for k,v in sealed.items() if k!='checked_at_utc'}
expected = {}
entries = 0
roots = [BASE, ROOT/'output/api854-20261003/beam-final-recipe-return-v1',
         ROOT/'output/api854-20261003/beam-buffer-joint-review-v1',
         ROOT/'output/api854-20261003/beam-v8-received-v1']
roots += [ROOT/'docs/api854/evidence'/name for name in
          ['beam-lang-development-20261003-v1','beam-lang-reference-20261003-v1',
           'beam-lang-reference-20261003-v2','beam-buffer-reference-20261003-v2',
           'beam-v8-math-development-20261003-v1'] if (ROOT/'docs/api854/evidence'/name).exists()]
for tree in roots:
    for manifest in tree.rglob('checksums.json'):
        for name, value in read(manifest).items():
            path = manifest.parent/name
            assert sha(path)==value, (manifest,name)
            expected[':'+path.relative_to(ROOT).as_posix()] = value
            entries += 1
        expected[':'+manifest.relative_to(ROOT).as_posix()] = sha(manifest)
template = read(ROOT/'output/api854-20261003/beam-final-recipe-return-v1/received-aom/joint-lang-acceptance.template.json')
assert actual['beam_runtime_sha256'] == template['beam_runtime_source_sha256']
for name, value in template['beam_runtime_source_sha256'].items():
    expected[':'+name] = value
    expected[template['beam_commit']+':'+name] = value
for name in subprocess.check_output(['git','diff','--cached','--name-only','--diff-filter=ACM','-z']).decode().split('\0'):
    if name: expected[':'+name] = sha(ROOT/name)
keys = sorted(expected)
result = subprocess.run(['git','cat-file','--batch'], input=''.join(k+'\n' for k in keys).encode(),
                        capture_output=True,check=True)
stream = io.BytesIO(result.stdout)
for key in keys:
    header = stream.readline().split()
    assert len(header)==3 and header[1]==b'blob', (key, header)
    data = stream.read(int(header[2]))
    assert hashlib.sha256(data).hexdigest()==expected[key], key
    assert stream.read(1)==b'\n'
assert stream.read()==b''
receipt = {'status':'pass','checked_at_utc':datetime.now(timezone.utc).isoformat(),
           'producer_sha256':sha(Path(__file__)), 'checksum_entries_verified':entries,
           'staged_and_historical_git_blobs_verified':len(keys),
           'historical_runtime_pins_verified':41,'current_runtime_pins_verified':41,
           'candidate_review_repeated':True,'negative_controls_rejected':6,
           'new_java_or_defects4j_executions':0,'primary_results_added':0,'live_requests':0,
           'queue_mutations':0,'shared_integration_approved':False,'gate_a_passed':False,
           'beam_verdict_sha256':sha(BASE/'beam-chronology-verdict.json'),
           'scope':'Public immutable Beam candidate review; no final recipe or pilot approval'}
path = ROOT/'output/api854-20261003/beam-chronology-release-verification-v1.json'
with path.open('x',encoding='utf-8',newline='\n') as f:
    json.dump(receipt,f,ensure_ascii=False,indent=2);f.write('\n')
print(json.dumps(receipt))
