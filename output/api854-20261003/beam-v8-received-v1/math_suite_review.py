"""Scoped Math-v8 sampled-suite review, separate from the v5/buffer/Lang reviewer."""
from pathlib import Path
import xml.etree.ElementTree as ET
from scripts.study.api854.common import read_json,write_json,sha256
from scripts.study.api854.fixture_policy import POLICY_V6
from scripts.study.api854.fixture_semantics import descriptor
from scripts.study.api854.evaluate_worker import review_validity


def review(root):
    root=Path(root);index=read_json(root/'index.json')
    assert index['fixture_policy_id']==POLICY_V6
    reports=[]
    for row in index['records']:
        assert (row['project'],row['bug_id'])==('Math',1)
        generation=Path(row['generation_result']).parent;evaluation=Path(row['evaluation_result']).parent
        before=sha256(evaluation/'result.json')
        generated=read_json(generation/'result.json');evaluated=read_json(evaluation/'result.json')
        coverage=evaluation/'measurement/coverage/coverage.xml'
        covered={(c.get('name'),m.get('name'),m.get('signature','').split(')')[0]+')')
            for c in ET.parse(coverage).getroot().iter('class') for m in c.findall('./methods/method')
            if any(int(l.get('hits','0'))>0 for l in m.findall('./lines/line'))}
        selected=read_json(generation/'setup/targets.fixture-policy.json')['targets']
        cases=[]
        for case in read_json(generation/'suite/observations.json'):
            if not case['retained']:continue
            t=case['target'];outcome=case['fixed_first']['outcome']
            valid=(t in selected and case['fixed_first']==case['fixed_second']
                and case['fixed_first']['status']=='ok' and case['fixed_first']['target_invoked'] is True
                and (t['class'],t['method'],descriptor(t['parameter_types'])) in covered
                and not outcome.startswith('exception:') and 'object-type:' not in outcome
                and outcome not in ('value:null|state=stateless-scalars','void|state=stateless-scalars'))
            if t['method']=='getField':
                valid=valid and t['constructor_types']=='double' and not t['parameter_types']
                valid=valid and ':zero=fraction:0/1:one=fraction:1/1|state=fraction:' in outcome
            cases.append({'case_id':case['case_id'],'target':t,'vector':case['vector'],'outcome':outcome,
                'stable_fixed_target_invocation_and_covered_structural_oracle':valid})
        counts={}
        for stage in ('fixed-1','fixed-2','buggy','coverage'):
            path=evaluation/'measurement'/stage/'sqa-stage-counts.json';c=read_json(path)
            assert c['executed']==c['target_checks']==30 and c['skipped']==0
            counts[stage]={**c,'evidence':{'path':path.relative_to(evaluation).as_posix(),'sha256':sha256(path)}}
        valid=len(cases)==30 and all(c['stable_fixed_target_invocation_and_covered_structural_oracle'] for c in cases)
        folder=evaluation/'math-v8-semantic-evidence';folder.mkdir(exist_ok=False)
        path=folder/'review.json'
        doc={'reviewer':'Beam scoped Math-v8 development review','verdict':'valid' if valid else 'invalid',
            'suite_sha256':generated['suite_sha256'],'fixed_source_sha256':generated['fixed_source_sha256'],
            'target_execution_evidence':[{'path':coverage.relative_to(evaluation).as_posix(),'sha256':sha256(coverage)}],
            'fixture_oracle_review':{'scope':'30 retained sampled cases; not all 53 Math selected declarations',
                'preconditions':'Finite quarter-valued production fraction receivers, bounded scalar operands. getField has exactly double constructor/no arguments; factory class/zero/one and rational receiver state are projected.',
                'cases':cases,'independent_field_reference':'output/api854-20261003/beam-v8-received-v1/math-reference/receipt.json',
                'producer_sha256':sha256(__file__),'team_or_primary_approval':False},
            'weak_oracles':{'invalid_case_ids':[c['case_id'] for c in cases if not c['stable_fixed_target_invocation_and_covered_structural_oracle']],
                'limitations':['Bounded projections do not prove full domain or object equivalence.','Target coverage may include setup; actual invocation is independently checked.','Suite chosen prospectively; no assertions or target selection repaired from buggy outcomes.']},
            'stage_counts':counts}
        write_json(path,doc)
        result=review_validity(path,generated['suite_sha256'],generated['fixed_source_sha256'],evaluated['measurement'],evaluation)
        dest=evaluation.parent/'semantic-review';dest.mkdir(exist_ok=False)
        write_json(dest/'review.json',{'job':evaluated['job'],'evaluation_result_sha256':before,**result})
        assert sha256(evaluation/'result.json')==before
        reports.append({'project':'Math','bug_id':1,'approach':row['approach'],'verdict':doc['verdict'],
            'local_development_usable':result['usable'],'original_result_unchanged':True,'team_or_primary_approval':False})
    write_json(root/'semantic-review-index.json',{'primary':False,'gate_a_approved':False,
        'reviewer_sha256':sha256(__file__),'reviews':reports})
    return reports
