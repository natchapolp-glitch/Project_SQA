"""Read-only receipt/source integrity review of Champ's enum boundary packet."""
from pathlib import Path
import io
import json
import subprocess
import sys
import tarfile

ROOT=Path.cwd();sys.path.insert(0,str(ROOT))
from scripts.study.api854.common import read_json,write_json,sha256
BASE=ROOT/'output/api854-20261003/beam-v8-received-v1'
COMMIT='e742095dcddf795b4d575a611b5f844da9977388'
PREFIX='output/api854-20261003/enum-boundary-development-v3/'
out=BASE/'peer-evidence/enum-boundary-v3';out.mkdir(exist_ok=True)
files=subprocess.check_output(['git','ls-tree','-r','--name-only',COMMIT,'--',PREFIX],text=True).splitlines()
for name in files:
    target=out/name.removeprefix(PREFIX);target.parent.mkdir(parents=True,exist_ok=True)
    raw=subprocess.check_output(['git','show',COMMIT+':'+name])
    if target.exists(): assert target.read_bytes()==raw,name
    else: target.write_bytes(raw)
checks=read_json(out/'checksums.json')
for rel,digest in checks.items():assert sha256(out/rel)==digest,rel
receipt=read_json(out/'receipt.json');assert receipt['status']=='pass'
with tarfile.open(out/'fixed-production-source.tar') as archive:
    members=[m for m in archive.getmembers() if m.name.endswith('/FromXmlParser.java')]
    assert len(members)==1
    source=archive.extractfile(members[0]).read()
    import hashlib
    archived_digest=hashlib.sha256(source).hexdigest()
    # Peer verifier compares normalized mirror bytes, then actually compiles the
    # exact pinned retained bytes. Archive CRLF is not that retained-source hash.
    retained=BASE/'preparation/JacksonXml-1/fixed-source/src/main/java/com/fasterxml/jackson/dataformat/xml/deser/FromXmlParser.java'
    assert sha256(retained)==receipt['fixed_source_sha256']
    assert source.replace(b'\r\n',b'\n')==retained.read_bytes().replace(b'\r\n',b'\n')
# Do not run peer code, fabricate enum members, or promote null to normal domain.
assert b'enum Feature' in source
intake=read_json(BASE/'peer-evidence/champ-beam532-intake-v1.json')
report={'reviewer':'beam','source_commit':COMMIT,'source_prefix':PREFIX,
    'packet_files_checked':len(checks),'packet_checksums_sha256':sha256(out/'checksums.json'),
    'receipt_sha256':sha256(out/'receipt.json'),'fixed_source_sha256':receipt['fixed_source_sha256'],
    'archive_source_raw_sha256':archived_digest,'mirror_vs_retained_comparison':'Normalized line endings equal; peer verifier compiles exact pinned retained source bytes',
    'exact_signatures':intake['enum_targets'],'producer_sha256':sha256(__file__),
    'beam_proposal':'Retain four exclusions and denominator 691; report null-input target-entry/state boundary diagnostics separately. No legal non-null enum members are available in this fixed revision.',
    'beam_boundary_evidence_verdict':'Received packet/source integrity verified; peer offline execution evidence, not rerun on Beam',
    'normal_domain_requirement_decision':None,'null_boundary_policy_verdict':None,
    'joint_decision':None,'unsupported_closed':0,'gate_a_passed':False,'primary':False,
    'real_kku_requests':0,'queue_mutations':0,
    'limitations':['No new Defects4J or Java execution for this peer packet.',
        'Peer uses Windows/Java17; this is separate from Beam Linux/Java11 Defects4J readiness.',
        'Repeated fixed null observations and temporary mutation evidence do not establish meaningful normal-domain coverage or buggy fault detection.',
        'Aom/Beam/Champ must explicitly decide the requirement and boundary oracle before any adoption.']}
write_json(BASE/'enum-review.json',report)
print(json.dumps({'enum_files_checked':len(checks),'joint_decision':None,'unsupported_closed':0}))
