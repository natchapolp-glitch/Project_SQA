"""Prospective independent Buffer/Csv reference proof, never an algorithm result.

Seal exact cases, expected oracles and unchanged suite bytes before observation.
Run inside Beam WSL from the repository root, using the shared one-CPU lock.
"""
from pathlib import Path
from datetime import datetime,timezone
import subprocess
import sys
import xml.etree.ElementTree as ET

ROOT=Path.cwd();sys.path[:0]=[str(ROOT),str(ROOT/'scripts/study')]
from scripts.study.api854.common import read_json,write_json,sha256,cpu_slot,implementation_hashes,assert_implementation,snapshot_implementation
from scripts.study.api854.fixture_policy import POLICY_V6_BUFFER,POLICY_V5,BUFFER_SIGNATURES
from scripts.study.api854.buffer_fixture_review import EXAMPLES,expected,scalar
from scripts.study.api854.fixture_semantics import descriptor
from scripts.study.api854.pack_suite import pack_suite
from generate import suite_source,observe
from evaluate import evaluate_run,EvaluationConfig

BASE=ROOT/'output/api854-20261003/beam-buffer-joint-review-v1'
TREES=Path('/home/team/sqa-round2/beam-buffer-worktrees')
D4J='/home/team/sqa-round2/defects4j/framework/bin/defects4j'


def bind(p):return {'path':Path(p).relative_to(ROOT).as_posix(),'sha256':sha256(p)}


def main():
    out=BASE/'reference';out.mkdir(exist_ok=False)
    runtime=implementation_hashes()
    old=read_json(ROOT/'.local/api854/beam-buffer-development-v1/index.json')
    summary={'primary':False,'gate_a_approved':False,'joint_semantic_approval':False,
        'real_kku_requests':0,'queue_mutations':0,'fixture_policy_id':POLICY_V6_BUFFER,
        'runtime_source_sha256':runtime,'producer_sha256':sha256(__file__),
        'reviewed_runtime_commit':'24a38184a8ffbe2e08d50fbab7020559746fe432','records':[],
        'limitations':['Predeclared handwritten reference suites, not CMA-ES/FSCS-ART results or complete input domain.',
            'Historical CMA-ES String append entry hits remain zero.',
            'Csv stream condition changes five previously selected reader methods; shared adoption needs explicit Champ verdict and new combined condition.']}
    with cpu_slot(TREES):
        assert_implementation(runtime)
        snapshot_implementation(out,runtime)
        setups={};declared={};packages={};fixed_sources={}
        for project in ('JacksonCore','Csv'):
            row=next(r for r in old['records'] if r['project']==project and r['approach']=='fscs-art')
            folder=Path(row['generation_result']).parent
            setup=read_json(folder/'setup/adapter.json');setups[project]=setup
            pins=read_json(folder/'result.json')['fixed_source_sha256'];fixed_sources[project]=pins
            for rel,h in pins.items():assert sha256(Path(setup['fixed_worktree'])/rel)==h
            selected=read_json(folder/'setup/targets.fixture-policy.json')['targets']
            cases=[]
            for target in selected:
                identity=(target['class'],target['method'],target['parameter_types'])
                if identity in BUFFER_SIGNATURES:
                    for e in EXAMPLES:
                        cases.append({'case_id':len(cases),'target':target,
                            'vector':[e['a'],e['b']]+[0.0]*(target['dimensions']-2),
                            'expected':expected(target,e),'case_kind':'buffer_signature_reference'})
                elif project=='Csv' and target['method'] in ('getLineNumber','lookAhead','read','readAgain','readLine') and not target['parameter_types']:
                    for a,first,text,last in ((-0.5,65,'A',65),(0.5,49,'12',50)):
                        value={'getLineNumber':0,'lookAhead':first,'read':first,'readAgain':-2,'readLine':text}[target['method']]
                        line=1 if target['method']=='readLine' else 0
                        state_last=last if target['method']=='readLine' else first if target['method']=='read' else -2
                        outcome='value:'+scalar('java.lang.String' if target['method']=='readLine' else 'java.lang.Integer',str(value))
                        outcome+=f'|state=reader:line={line}:last={state_last}'
                        cases.append({'case_id':len(cases),'target':target,'vector':[a]*target['dimensions'],
                            'expected':outcome,'declared_stream':'A\nBC\nDE' if a<0 else '12\n345\n',
                            'case_kind':'existing_selected_csv_condition_reference'})
            assert len(cases)==(28 if project=='JacksonCore' else 14)
            declared[project]=cases
            root=out/project;root.mkdir();source=root/'sources';source.mkdir()
            # suite_source's internal expected-observation shape is used only to
            # emit assertions from declared independent values, not as evidence.
            internal=[{**c,'retained':True,'fixed_first':{'status':'ok','target_invoked':True,'outcome':c['expected']}}
                for c in cases]
            (source/'GeneratedStudyTest.java').write_text(suite_source(internal,POLICY_V6_BUFFER))
            packages[project]=pack_suite(source,root/'package',len(cases))
            write_json(root/'declared-cases.json',cases)
        plan={'created_at_utc':datetime.now(timezone.utc).isoformat(),'producer_sha256':sha256(__file__),
            'runtime_source_sha256':runtime,'fixed_source_sha256':fixed_sources,'fixture_policy_id':POLICY_V6_BUFFER,
            'cases':{p:bind(out/p/'declared-cases.json') for p in declared},
            'suites':{p:bind(out/p/'package/suite.tar.bz2') for p in declared},
            'declared_before_observation':True,'primary':False,'approach':'independent-reference',
            'alter_historical_suites':False,'use_buggy_outcomes_for_selection':False}
        write_json(out/'preexecution-seal.json',plan)
        summary['preexecution_seal']=bind(out/'preexecution-seal.json')
        probe=out/'probe-classes';probe.mkdir()
        compiled=subprocess.run(['javac','-source','7','-target','7','-d',str(probe),str(ROOT/'algorithms/java/SqaProbe.java')],capture_output=True,text=True)
        (out/'compile.log').write_text(compiled.stdout+compiled.stderr)
        assert compiled.returncode==0
        for project,cases in declared.items():
            print('START reference',project,len(cases),flush=True)
            root=out/project;setup=setups[project];observations=[]
            cp=str(probe)+':'+setup['classpath']
            for c in cases:
                first=observe(cp,c['target'],c['vector'],20,POLICY_V6_BUFFER)
                second=observe(cp,c['target'],c['vector'],20,POLICY_V6_BUFFER)
                valid=first==second and first.get('status')=='ok' and first.get('target_invoked') is True and first.get('outcome')==c['expected']
                row={**c,'fixed_first':first,'fixed_second':second,'independent_reference_passed':valid}
                if c['case_kind']=='existing_selected_csv_condition_reference':
                    row['historical_v5_stream_observation']=observe(cp,c['target'],c['vector'],20,POLICY_V5)
                observations.append(row)
            write_json(root/'observations.json',observations)
            assert all(r['independent_reference_passed'] for r in observations),[r for r in observations if not r['independent_reference_passed']]
            classes=root/'target-classes.txt';classes.write_text('\n'.join(sorted({c['target']['class'] for c in cases}))+'\n')
            measured=evaluate_run(EvaluationConfig(project=project,bug_id=1,generator='buffer-independent-reference',seed=101,
                budget=len(cases),test_count=len(cases),suite=root/'package/suite.tar.bz2',
                fixed_worktree=Path(setup['fixed_worktree']),buggy_worktree=Path(setup['buggy_worktree']),
                output=root/'evaluation',classes_file=classes,d4j=D4J,timeout_seconds=900))
            counters={s:read_json(root/'evaluation'/s/'sqa-stage-counts.json') for s in ('fixed-1','fixed-2','buggy','coverage')}
            covered={(cl.get('name'),m.get('name'),m.get('signature','').split(')')[0]+')')
                for cl in ET.parse(root/'evaluation/coverage/coverage.xml').getroot().iter('class')
                for m in cl.findall('./methods/method') if any(int(l.get('hits','0'))>0 for l in m.findall('./lines/line'))}
            identities={(c['target']['class'],c['target']['method'],descriptor(c['target']['parameter_types'])) for c in cases}
            coverage_rows=[{'class':cl,'method':method,'parameter_descriptor':desc,'method_covered':(cl,method,desc) in covered}
                for cl,method,desc in sorted(identities)]
            valid=(measured['status']=='complete' and measured.get('fixed_validation')=='passed_twice'
                and all(c['executed']==c['target_checks']==len(cases) and c['skipped']==0 for c in counters.values())
                and all(r['method_covered'] for r in coverage_rows))
            record={'project':project,'bug_id':1,'case_count':len(cases),'fixed_observations':2*len(cases),
                'suite_sha256':packages[project]['suite_sha256'],'fixed_validation':measured.get('fixed_validation'),
                'stage_counts':counters,'exact_target_coverage':coverage_rows,'local_development_valid':valid,
                'measurement_status':measured['status'],'fault_detected':measured.get('fault_detected')}
            write_json(root/'receipt.json',record);summary['records'].append(record)
            assert sha256(root/'package/suite.tar.bz2')==plan['suites'][project]['sha256']
            for rel,h in fixed_sources[project].items():assert sha256(Path(setup['fixed_worktree'])/rel)==h
            assert valid,record
            print('DONE reference',project,valid,flush=True)
        assert_implementation(runtime)
    summary['passed']=all(r['local_development_valid'] for r in summary['records'])
    write_json(out/'receipt.json',summary)
    write_json(out/'checksums.json',{p.relative_to(out).as_posix():sha256(p) for p in sorted(out.rglob('*'))
        if p.is_file() and '__pycache__' not in p.parts and p.suffix not in ('.class','.pyc')})
    print('SEALED Buffer reference 32 cases + Csv stream 10 cases',flush=True)


if __name__=='__main__':main()
