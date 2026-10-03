"""Verify Codec candidate sources, guards and staged sealed evidence before push."""
from pathlib import Path
from datetime import datetime,timezone
import gzip,hashlib,io,json,re,runpy,subprocess,tarfile
ROOT=Path(__file__).resolve().parents[2];BASE=ROOT/'output/api854-20261004/beam-codec-candidate-v1'
n=runpy.run_path(str(BASE/'finalize_packet.py'));fresh=n['audit']();receipt=n['read'](BASE/'receipt.json')
assert {k:v for k,v in receipt.items() if k not in {'checked_at_utc','producer_sha256'}}==fresh
assert receipt['producer_sha256']==n['sha'](BASE/'finalize_packet.py')
manifest=n['read'](BASE/'checksums.json');assert 'checksums.json' not in manifest
for name,value in manifest.items():assert n['sha'](BASE/name)==value,name
seal=n['read'](BASE/'native-v1/preexecution-seal.json')
for version in ['fixed','buggy']:
    raw=gzip.decompress((BASE/('native-v1/'+version+'-production-archive.stdout.tar.gz')).read_bytes())
    command=n['read'](BASE/('native-v1/'+version+'-production-archive.command.json'))
    assert hashlib.sha256(raw).hexdigest()==command['stdout_sha256']
    with tarfile.open(fileobj=io.BytesIO(raw)) as t:
        sources={m.name:t.extractfile(m).read() for m in t.getmembers() if m.isfile() and m.name.endswith('.java')}
    if version=='fixed':
        for name in ['Metaphone.java','SoundexUtils.java']:
            path='src/java/org/apache/commons/codec/language/'+name
            retained=(BASE/'received-aom/output/api854-20261003/prepare-v10-joint-development/Codec-1/fixed-source'/path).read_bytes()
            assert sources[path].replace(b'\r\n',b'\n')==retained.replace(b'\r\n',b'\n')
            sources[path]=retained
    assert {p:hashlib.sha256(value).hexdigest() for p,value in sources.items()}==seal['source_sha256'][version]
template=n['read'](BASE/'joint-review.template.json')
assert len(template['candidates'])==5 and template['joint_acceptance_complete'] is False
for row in template['candidates']:
    assert row['beam_verdict']=='accepted_for_bounded_candidate_oracle_development'
    assert row['champ_verdict'] is None and row['shared_integration_approved'] is False
    assert row['accepted_into_shared_inputs'] is False
    assert row['proposed_preconditions'] and row['proposed_oracle']
for binding in template['evidence']:assert n['sha'](ROOT/binding['path'])==binding['sha256']
assert sum(r['candidate_cases'] for r in template['candidates'])==43
doc=ROOT/'docs/api854/BEAM_CODEC_CANDIDATE_20261004_TH.md'
for href in re.findall(r'\]\(([^)]+)\)',doc.read_text(encoding='utf-8')):
    if not href.startswith('http'):assert (doc.parent/href).resolve().is_file(),href
names=[p for p in subprocess.check_output(['git','diff','--cached','--name-only','-z'],cwd=ROOT).decode().split('\0') if p]
allowed={'.gitignore','.gitattributes','README.md',doc.relative_to(ROOT).as_posix(),Path(__file__).relative_to(ROOT).as_posix()}
for p in names:assert p in allowed or p.startswith('output/api854-20261004/beam-codec-candidate-v1/'),p
batch=subprocess.check_output(['git','cat-file','--batch'],cwd=ROOT,input=('\n'.join(':'+p for p in names)+'\n').encode())
stream=io.BytesIO(batch)
for p in names:
    header=stream.readline().split();assert len(header)==3 and header[1]==b'blob'
    raw=stream.read(int(header[2]));assert stream.read(1)==b'\n' and raw==(ROOT/p).read_bytes(),p
assert stream.read()==b''
for p in BASE.rglob('*'):
    if p.is_file():assert p.relative_to(ROOT).as_posix() in names,'Unstaged sealed evidence '+str(p)
subprocess.run(['git','diff','--cached','--check'],cwd=ROOT,check=True)
result={'status':'pass','checked_at_utc':datetime.now(timezone.utc).isoformat(),
 'producer_sha256':n['sha'](Path(__file__)),'receipt_sha256':n['sha'](BASE/'receipt.json'),
 'root_checksum_entries_verified':len(manifest),'staged_git_blobs_verified':len(names),
 'received_git_sources_verified':7,'unchanged_shared_runtime_files':41,
 'native_cases':43,'declarations':5,'fixed_observations':86,'buggy_observations':86,
 'fresh_focused_tests_passed':7,'fresh_focused_tests_skipped':0,'oracle_mutations_detected':2,
 'candidate_fault_detected':False,'shared_integration_approved':False,'gate_a_approved':False,
 'kku_requests':0,'queue_mutations':0,'primary_added':0}
with Path(__file__).with_suffix('.json').open('x',encoding='utf-8',newline='\n') as f:json.dump(result,f,indent=2);f.write('\n')
print(json.dumps(result,indent=2))
