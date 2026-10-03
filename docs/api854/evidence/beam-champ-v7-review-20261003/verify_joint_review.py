import json,hashlib,xml.etree.ElementTree as ET
from pathlib import Path
ROOT=Path(__file__).resolve().parents[4]
HERE=Path(__file__).resolve().parent
E=HERE.parent
read=lambda p:json.loads(p.read_text(encoding='utf-8'))
sha=lambda p:hashlib.sha256(p.read_bytes()).hexdigest()
checked=0
for name in ('beam-champ-v7-review-20261003','beam-champ-math-field-20261003','beam-champ-math-field-20261003-attempt2'):
    folder=E/name
    for rel,digest in read(folder/'checksums.json').items():
        p=(folder/rel).resolve()
        assert p.is_relative_to(folder.resolve())
        assert sha(p)==digest,(name,rel)
        checked+=1
p=E/'beam-champ-math-field-20261003-attempt2'
r=read(p/'receipt.json')
assert r['status']=='complete' and r['fault_detected'] is False
assert all(r[k] is False for k in ('primary','shared_policy_changed','joint_semantic_approval'))
assert sha(ROOT/'algorithms/java/SqaProbe.java')==r['original_probe_sha256']
assert read(p/'red.json')['passed'] is False
assert read(p/'green.json')['passed'] is True
obs=read(p/'observations.json');assert len(obs)==4
for row in obs:
    a,b=row['fixed_first'],row['fixed_second']
    assert a==b and a['status']=='ok' and a['target_invoked'] is True
    assert 'runtime=type:'+row['target']['class'] in a['outcome']
    assert ':zero=fraction:0/1:one=fraction:1/1' in a['outcome']
for stage in ('fixed-1','fixed-2','buggy','coverage'):
    assert read(p/'evaluation'/stage/'sqa-stage-counts.json')==dict(schema_version=1,executed=4,skipped=0,target_checks=4)
xml=ET.parse(p/'evaluation/coverage/coverage.xml')
hits={}
for cls in xml.findall('.//class'):
    if cls.attrib.get('name') in {o['target']['class'] for o in obs}:
        lines=cls.findall('./methods/method[@name="getField"]/lines/line')
        hits[cls.attrib['name']]=sum(int(line.attrib['hits']) for line in lines)
assert len(hits)==2 and all(v>0 for v in hits.values()),hits
review=read(HERE/'review.json')
assert review['required_common_declarations']==691 and review['shared_supported']==377 and review['shared_unsupported']==314
assert review['gate_a_passed'] is False and review['primary_completed']==0
out=dict(status='passed',manifest_files_verified=checked,observations=4,stage_counts_verified=4,getField_hits=hits,required_common_declarations=691,shared_policy_changed=False,pilot_opened=False)
(HERE/'verification.json').write_text(json.dumps(out,ensure_ascii=False,indent=2)+'\n',encoding='utf-8')
print(json.dumps(out))

