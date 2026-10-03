"""Publish Champ's semantic candidate review; no runtime or gate changes."""
import re
from datetime import datetime,timezone
from scripts.study.api854.common import ROOT,read_json,sha256,contained,write_json

P=ROOT/'docs/api854/evidence/beam-champ-math-field-20261003-attempt2'
O=ROOT/'output/api854-provider-preflight-20261003'
for name,digest in read_json(P/'checksums.json').items():
    assert sha256(contained(P,name))==digest
r=read_json(P/'receipt.json')
helper=P/'helper-src/algorithms/java/SqaProbe.java'
assert sha256(helper)==r['candidate_probe_sha256']
assert sha256(P/'evaluation/record.json')==r['result_sha256']
assert sha256(P/'package/suite.tar.bz2')==r['suite_sha256']
obs=read_json(P/'observations.json')
base=ROOT/'output/api854-20261003/champ-prepare-v7-current-v1/Math-1/fixed-source/src/main/java/org/apache/commons/math3/fraction'
accepted=[]
for cls in ['BigFraction','Fraction']:
    source=base/(cls+'.java')
    text=source.read_text(encoding='utf-8')
    assert re.search(r'public\s+'+cls+r'Field\s+getField\(\)\s*\{\s*return\s+'+cls+r'Field\.getInstance\(\);\s*\}',text)
    factory=P/'supplemental-fixed-source'/(cls+'Field.java')
    f=factory.read_text(encoding='utf-8')
    for method,value in [('getZero',cls+'.ZERO'),('getOne',cls+'.ONE'),('getRuntimeClass',cls+'.class')]:
        assert re.search(method+r'\(\)\s*\{\s*return\s+'+re.escape(value)+r';\s*\}',f)
    rows=[row for row in obs if row['target']['class']=='org.apache.commons.math3.fraction.'+cls]
    assert len(rows)==2
    for row in rows:
        a,b=row['fixed_first'],row['fixed_second']
        assert a==b and a['status']=='ok' and a['target_invoked'] is True
        assert 'runtime=type:org.apache.commons.math3.fraction.'+cls in a['outcome']
        assert ':zero=fraction:0/1:one=fraction:1/1' in a['outcome']
        assert row['target']['constructor_types']=='double' and row['target']['parameter_types']==''
    accepted.append({'target':rows[0]['target'],'champ_verdict':'accepted_for_shared_composition',
        'preconditions':'Real production receiver from successful finite double construction; no getField arguments. No receiver setup exception is accepted as a target result.',
        'source_reason':'getField returns the production singleton without reading or mutating receiver state.',
        'oracle':'Runtime type matches element class; structural zero=0/1 and one=1/1; target invoked; retained receiver rational state unchanged.',
        'receiver_samples':['7/4','-3/4'],'target_source_sha256':sha256(source),
        'supplemental_factory_sha256':sha256(factory),'scope':'Exact constructor/target signature and field projection; not other constructors, methods or whole helper policy.',
        'beam_role':'Candidate proposer and development execution evidence; no new Beam approval signature asserted.'})
result={'checked_at_utc':datetime.now(timezone.utc).isoformat(),'reviewer':'champ',
    'beam_source_commit':'0b560f058b811c8d9891b93f7606654106691766','candidate_policy_id':r['candidate_policy_id'],
    'candidate_probe_sha256':r['candidate_probe_sha256'],'suite_sha256':r['suite_sha256'],'result_sha256':r['result_sha256'],
    'accepted_candidates':accepted,'accepted_count':2,'champ_owner_review_remaining':167,
    'composition_requirements':['Add both immutable field factory sources/knowledge to shared context for all four approaches.',
        'Port only reviewed projection under new policy/runtime pins; retain old candidate/runtime evidence.',
        'Rebuild source/context/recipe/targets/prompt/index/protocol/runner bindings and run consumer/execution regressions.',
        'Send composed inputs back to Champ for final byte/token/limits/reserve review.'],
    'current_shared_supported':377,'current_shared_unsupported':314,'denominator':691,
    'current_inventory_changed':False,'final_condition_approved':False,'gate_a_passed':False,'pilot_authorized':False,
    'primary_results_added':0,'provider_requests':0,'queue_mutations':0,'auditor_sha256':sha256(__file__)}
write_json(O/'champ-math-field-candidate-acceptance-v1.json',result)
print({'accepted_for_composition':2,'remaining_champ_candidate_review':167,'current_capability_counts_unchanged':True})
