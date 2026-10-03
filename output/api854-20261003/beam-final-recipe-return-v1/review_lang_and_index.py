"""Beam-only scoped Lang acceptance and a hash-bound final-recipe return index.

Rechecks retained measurements/oracles without rerunning Defects4J or changing
historical results. Joint/final/primary approval is never inferred.
"""
from pathlib import Path
from datetime import datetime,timezone
import base64
import json
import re
import subprocess
import sys
import xml.etree.ElementTree as ET

ROOT=Path.cwd();sys.path.insert(0,str(ROOT))
from scripts.study.api854.common import read_json,write_json,sha256,implementation_hashes
BASE=ROOT/'output/api854-20261003/beam-final-recipe-return-v1'


def binding(p):return {'path':Path(p).relative_to(ROOT).as_posix(),'sha256':sha256(p)}
def scalar(cls,text):return cls+':'+base64.b64encode(text.encode()).decode()


def main():
    names=['beam-lang-development-20261003-v1','beam-lang-reference-20261003-v1','beam-lang-reference-20261003-v2']
    packets=[ROOT/'docs/api854/evidence'/n for n in names];integrity=[]
    for packet in packets:
        checks=read_json(packet/'checksums.json')
        for rel,h in checks.items():assert sha256(packet/rel)==h,(packet,rel)
        integrity.append({'checksums':binding(packet/'checksums.json'),'files_checked':len(checks)})
    development,attempt1,reference=packets
    first,second=read_json(attempt1/'receipt.json'),read_json(reference/'receipt.json')
    assert first['local_development_valid'] is False and second['local_development_valid'] is True
    assert first['suite_sha256']==second['suite_sha256']
    assert second['example_count']==12 and second['fixed_observation_count']==24
    source=ROOT/'output/api854-20261003/prepare-v3/Lang-1/fixed-source/src/main/java/org/apache/commons/lang3/math/NumberUtils.java'
    pin='0374c7b486626927217ad42677e1a57fc60fc551218660316a0e7f4f8ea79477'
    assert sha256(source)==pin
    text=source.read_text()
    assert re.search(r'private\s+static\s+boolean\s+isAllZeros\s*\(',text)
    assert re.search(r'private\s+static\s+void\s+validateArray\s*\(',text)
    assert 'The Array must not be null' in text and 'Array cannot be empty.' in text
    cases=read_json(reference/'reference-observations.json')
    recomputed=[]
    for c in cases:
        target=c['target'];value=c['declared_input']['input']
        assert target['class']=='org.apache.commons.lang3.math.NumberUtils' and not target['constructor_types']
        if target['method']=='isAllZeros':
            expected_bool=value is None or (len(value)>0 and all(ch=='0' for ch in value))
            expected='value:'+scalar('java.lang.Boolean',str(expected_bool).lower())+'|state=stateless-scalars'
        else:
            assert target['method']=='validateArray' and (value is None or isinstance(value,list))
            array='null' if value is None else '[I['+''.join(scalar('java.lang.Integer',str(v))+';' for v in value)+']'
            message='The Array must not be null' if value is None else 'Array cannot be empty.' if not value else None
            expected=('void' if message is None else 'exception:java.lang.IllegalArgumentException|message='+scalar('java.lang.String',message))+'|state=validation-input:'+array
        assert c['expected']==expected and c['fixed_first']==c['fixed_second']
        assert c['fixed_first']['status']=='ok' and c['fixed_first']['target_invoked'] is True
        assert c['fixed_first']['outcome']==expected
        recomputed.append({'case_id':c['case_id'],'method':target['method'],'input':value,'expected':expected,'verified':True})
    assert len(recomputed)==12
    write_json(BASE/'independent-oracle-recheck.json',{'fixed_source':binding(source),'cases':recomputed,
        'observations':binding(reference/'reference-observations.json'),'scope':'Independent recomputation from declared inputs; no new Java/Defects4J execution'})
    for stage in ('fixed-1','fixed-2','buggy','coverage'):
        c=read_json(reference/'evaluation'/stage/'sqa-stage-counts.json')
        assert c['executed']==c['target_checks']==12 and c['skipped']==0
        command=read_json(reference/'evaluation'/stage/'command.json');assert command['exit_code']==0 and not command['timed_out']
    descriptors={'isAllZeros':'(Ljava/lang/String;)Z','validateArray':'(Ljava/lang/Object;)V'}
    cov=ET.parse(reference/'evaluation/coverage/coverage.xml').getroot()
    for name,desc in descriptors.items():
        assert any(int(l.get('hits','0'))>0 for cl in cov.iter('class') if cl.get('name')=='org.apache.commons.lang3.math.NumberUtils'
            for m in cl.findall('./methods/method') if m.get('name')==name and m.get('signature')==desc for l in m.findall('./lines/line'))
    index=read_json(development/'index.json');assert len(index['records'])==2
    for row in index['records']:
        assert row['fault_detected'] is False and row['usable'] is False and row['fixed_validation']=='passed_twice'
        for stage in ('fixed-1','fixed-2','buggy','coverage'):
            c=read_json(development/f'Lang-1-{row["approach"]}'/stage/'sqa-stage-counts.json')
            assert c['executed']==c['target_checks']==30 and c['skipped']==0
    report=read_json(BASE/'received-aom/joint-lang-acceptance.template.json')
    assert sha256(BASE/'received-aom/lang-intake.json')==report['aom_review_receipt_sha256']
    assert sha256(reference/'reference-observations.json')==report['reference_observations_sha256']
    assert sha256(reference/'package/suite.tar.bz2')==report['reference_suite_sha256']
    assert sha256(reference/'evaluation/coverage/coverage.xml')==report['reference_coverage_sha256']
    for c in report['candidates']:
        t=c['target'];name=t['method'];assert t['constructor_types']=='' and t['bug_id']==1
        pre=('Private static helper receives null/empty/zero-only/nonzero text. Bounded examples: null, empty, 0, 000, 001, 12, 00 0, -0. No receiver construction.' if name=='isAllZeros' else
            'Private static helper receives null, empty int[], int[]{0}, int[]{-1,0,7}. No non-array Object accepted as a legal fixture; other primitive/reference array types are outside this bounded recipe.')
        oracle=('Actual Boolean: null true, empty false, nonempty zero-only true, any other character false.' if name=='isAllZeros' else
            'Null/empty rejection compares IllegalArgumentException class, exact message The Array must not be null / Array cannot be empty., and unchanged input state. Success compares void plus actual int[] contents; bare void/stateless is not accepted.')
        c.update(beam_joint_verdict='accepted_for_bounded_prospective_development_composition',champ_joint_verdict=None,
            agreed_preconditions=None,agreed_exception_oracle=None,beam_proposed_preconditions=pre,
            beam_proposed_oracle=oracle,private_helper_caller_domain_limitation='Reflective entry proof does not validate all public callers or full legal domain.',
            exact_jvm_descriptor=descriptors[name],change_kind='add_unsupported_signature',fixed_source_sha256=pin,
            fixed_repeated_observations=[r for r in cases if r['target']['method']==name],
            evidence=[binding(reference/'receipt.json'),binding(reference/'reference-observations.json'),
                binding(reference/'evaluation/coverage/coverage.xml'),binding(BASE/'independent-oracle-recheck.json')],
            reference_stage_counts=second['stage_counts'],accepted_into_shared_inputs=False)
    report.update(example_only=False,review_status='beam_scoped_lang_verdict_complete_champ_pending',
        checked_at_utc=datetime.now(timezone.utc).isoformat(),reviewer='beam',
        reviewer_commit=subprocess.check_output(['git','rev-parse','HEAD'],text=True).strip(),
        aom_review_commit='60cc1a6efb54fcaa6dfacb426b0c3b2f8b648be7',producer_sha256=sha256(__file__),
        historical_packet_integrity=integrity,current_runtime_source_sha256=implementation_hashes(),
        reference_history={'attempt1_local_valid':False,'attempt2_local_valid':True,'same_suite_sha256':second['suite_sha256'],
            'independent_cases':12,'fixed_observations':24,'attempts_not_added_to_independent_case_count':True},
        joint_acceptance_complete=False,new_defects4j_executions=0,real_kku_requests=0,queue_mutations=0,primary_results_added=0)
    write_json(BASE/'beam-lang-verdict.json',report)
    # Link scoped verdicts; do not copy verdicts onto a different helper/condition.
    setter=ROOT/'output/api854-20261003/beam-v8-received-v1/joint-candidate-review.json'
    buffer=ROOT/'output/api854-20261003/beam-buffer-joint-review-v1/beam-buffer-verdict.json'
    math=ROOT/'output/api854-provider-preflight-20261003/champ-math-field-candidate-acceptance-v1.json'
    receipts=[{'group':'setter/JDOM','count':2,'beam':'scoped_receipt_sent','champ':'pending','receipt':binding(setter)},
        {'group':'Buffer/Csv','count':8,'beam':'scoped_receipt_sent','champ':'pending','receipt':binding(buffer)},
        {'group':'Lang','count':2,'beam':'scoped_receipt_sent','champ':'pending','receipt':binding(BASE/'beam-lang-verdict.json')},
        {'group':'Math','count':2,'beam':'scoped_receipt_sent','champ':'prior_exact_math_acceptance_received','receipt':binding(math),
            'beam_condition_review':binding(ROOT/'output/api854-20261003/beam-v8-received-v1/beam-v8-review.json')}]
    math_receipt=read_json(math);assert len(math_receipt['accepted_candidates'])==2
    registry={'schema_version':1,'checked_at_utc':datetime.now(timezone.utc).isoformat(),'reviewer':'beam',
        'reviewer_commit':report['reviewer_commit'],'aom_review_commit':report['aom_review_commit'],
        'scope':'Index of separately scoped Beam recipe returns; not final merged helper acceptance or team joint verdict',
        'groups':receipts,'recipes_indexed':14,'champ_pending_groups':['setter/JDOM','Buffer/Csv','Lang'],
        'required_shared_base':'api854-20261003-twenty-bug-development-v9-integrated',
        'preserve_recipes':['setter/getter/state','JDOM Attribute name/namespace/value','Math field projection and fixed factories'],
        'optional_proposed_unions':[{'accepted_delta':'Buffer eight only','selected':388,'exclusions':303},
            {'accepted_delta':'Lang two only','selected':382,'exclusions':309},
            {'accepted_delta':'Buffer eight + Lang two','selected':390,'exclusions':301}],
        'denominator':691,'union_implemented':False,'new_shared_preparation_created':False,
        'csv_stream_change_requires_explicit_champ_verdict':True,'empty_enum_joint_decision':None,
        'historical_cmaes_string_append_hits':0,'gate_a_approved':False,'pilot_authorized':False,
        'final_reserve':None,'provider_settings_limits_framing_quota_expiry':'pending for final condition',
        'real_kku_requests':0,'queue_mutations':0,'primary_results_added':0,
        'next_actions':['Champ returns scoped verdicts with source/recipe/oracle hashes; do not infer receipt integrity is semantic approval.',
            'Aom selects only jointly accepted deltas, preserves accepted recipes and creates a new 20-bug preparation/runner/protocol.',
            'Beam checks final condition consumer equality, semantic evidence and host binding; old results stay unchanged.',
            'Champ measures final prompts/reserve/settings/limits/current quota before any agreed bounded KKU experiment.']}
    write_json(BASE/'final-recipe-return-index.json',registry)
    write_json(BASE/'checksums.json',{p.relative_to(BASE).as_posix():sha256(p) for p in sorted(BASE.rglob('*'))
        if p.is_file() and p!=BASE/'checksums.json' and '__pycache__' not in p.parts and p.suffix not in ('.class','.pyc')})
    print(json.dumps({'lang_signatures_beam_accepted':2,'historical_checksums':sum(r['files_checked'] for r in integrity),
        'recipe_groups_indexed':len(receipts),'recipes_indexed':14,'champ_pending':3,'gate_a':False}))


if __name__=='__main__':main()
