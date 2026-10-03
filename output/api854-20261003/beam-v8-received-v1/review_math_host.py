"""Beam's bounded real-host review; no provider or queue operations.

Run from the repository root inside WSL after run_v8_math.py completes.
Historical evidence and preparation are immutable; output directories are new.
"""
from pathlib import Path
import subprocess
import sys
import xml.etree.ElementTree as ET

ROOT = Path.cwd()
sys.path[:0] = [str(ROOT), str(ROOT/'scripts/study')]
from scripts.study.api854.common import read_json, write_json, sha256, cpu_slot, implementation_hashes
from scripts.study.api854.pack_suite import pack_suite
from scripts.study.api854.fixture_policy import POLICY_V5, POLICY_V6
from generate import observe, suite_source
from evaluate import EvaluationConfig, evaluate_run

BASE = ROOT/'output/api854-20261003/beam-v8-received-v1'
TREES = Path('/home/team/sqa-round2/beam-buffer-worktrees')
D4J = '/home/team/sqa-round2/defects4j/framework/bin/defects4j'


def main():
    out = BASE/'math-reference'
    out.mkdir(exist_ok=False)
    child = '''from scripts.study.api854.common import cpu_slot
import sys
try:
 with cpu_slot(sys.argv[1]): pass
except RuntimeError: sys.exit(9)
'''
    with cpu_slot(TREES):
        blocked = subprocess.run([sys.executable,'-c',child,str(TREES)],cwd=ROOT,capture_output=True,text=True)
    released = subprocess.run([sys.executable,'-c',child,str(TREES)],cwd=ROOT,capture_output=True,text=True)
    assert (blocked.returncode,released.returncode)==(9,0)
    runner = BASE/'composition/runner-plan.json'
    host = {'worker_id':'beam-pc1','cpu_slots':1,'shared_worktrees_root':str(TREES),
        'runner_plan_sha256':sha256(runner),'environment_sha256':sha256(BASE/'environment/environment.json'),
        'environment_ready':read_json(BASE/'environment/environment.json')['ready'],
        'cross_process_cpu_lock':{'held_slot_rejected_exit':blocked.returncode,'released_slot_reusable_exit':released.returncode},
        'runtime_source_sha256':implementation_hashes(),'producer_sha256':sha256(__file__),
        'host_team_approval':False,'gate_a_passed':False,'real_kku_requests':0,'queue_mutations':0}
    write_json(BASE/'host-receipt.json',host)
    dev = read_json(ROOT/'.local/api854/beam-v8-math-development-v1/index.json')
    row = next(r for r in dev['records'] if r['approach']=='fscs-art')
    generation = Path(row['generation_result']).parent
    adapter = read_json(generation/'setup/adapter.json')
    fixed, buggy = Path(adapter['fixed_worktree']),Path(adapter['buggy_worktree'])
    metadata = read_json(BASE/'preparation/Math-1/prepare-metadata.json')
    for rel,digest in {**metadata['fixed_source_sha256'],**metadata['additional_fixture_source_sha256']}.items():
        assert sha256(fixed/rel)==digest,rel
    with cpu_slot(TREES):
        classes = out/'probe-classes'
        classes.mkdir()
        compiled = subprocess.run(['javac','-source','7','-target','7','-d',str(classes),str(ROOT/'algorithms/java/SqaProbe.java')],capture_output=True,text=True)
        (out/'compile.log').write_text(compiled.stdout+compiled.stderr)
        assert compiled.returncode==0
        cp = str(classes)+':'+adapter['classpath']
        cases=[]
        for short in ('BigFraction','Fraction'):
            target={'class':'org.apache.commons.math3.fraction.'+short,'constructor_types':'double','method':'getField','parameter_types':''}
            old=observe(cp,target,[0.5]*3,20,POLICY_V5)
            assert old['status']=='fixture_error'
            for coordinate,rational in ((0.5,'7/4'),(-0.5,'-3/4')):
                expected='value:fraction-field:runtime=type:'+target['class']+':zero=fraction:0/1:one=fraction:1/1|state=fraction:'+rational
                first=observe(cp,target,[coordinate]*3,20,POLICY_V6)
                second=observe(cp,target,[coordinate]*3,20,POLICY_V6)
                assert first==second and first['status']=='ok' and first['target_invoked'] is True and first['outcome']==expected,(target,first)
                cases.append({'case_id':len(cases),'target':target,'vector':[coordinate]*3,'expected':expected,
                    'fixed_first':first,'fixed_second':second,'retained':True,'legacy_v5':old})
        write_json(out/'observations.json',cases)
        sources=out/'sources';sources.mkdir()
        (sources/'GeneratedStudyTest.java').write_text(suite_source(cases,POLICY_V6))
        package=pack_suite(sources,out/'package',4)
        targets=out/'target-classes.txt'
        targets.write_text('\n'.join('org.apache.commons.math3.fraction.'+c for c in ('BigFraction','Fraction'))+'\n')
        measured=evaluate_run(EvaluationConfig(project='Math',bug_id=1,generator='beam-v8-math-reference',seed=101,budget=4,
            test_count=4,suite=out/'package/suite.tar.bz2',fixed_worktree=fixed,buggy_worktree=buggy,
            output=out/'evaluation',classes_file=targets,d4j=D4J,timeout_seconds=900))
        counts={s:read_json(out/'evaluation'/s/'sqa-stage-counts.json') for s in ('fixed-1','fixed-2','buggy','coverage')}
        coverage=ET.parse(out/'evaluation/coverage/coverage.xml').getroot()
        hits={c.get('name'):any(int(l.get('hits','0'))>0 for m in c.findall('./methods/method') if m.get('name')=='getField'
            and m.get('signature','').startswith('()') for l in m.findall('./lines/line'))
            for c in coverage.iter('class') if c.get('name') in {t['target']['class'] for t in cases}}
        valid=(measured['status']=='complete' and measured['fixed_validation']=='passed_twice'
            and all(v['executed']==v['target_checks']==4 and v['skipped']==0 for v in counts.values())
            and len(hits)==2 and all(hits.values()))
        receipt={'primary':False,'gate_a_passed':False,'team_semantic_approval':False,'real_kku_requests':0,'queue_mutations':0,
            'scope':'Four predeclared independent reference examples; not an algorithm result or full domain review',
            'producer_sha256':sha256(__file__),'fixture_policy_id':POLICY_V6,'runtime_source_sha256':implementation_hashes(),
            'shared_protocol_sha256':sha256(BASE/'composition/protocol.proposal.json'),
            'shared_preparation_index_sha256':sha256(BASE/'preparation/index.json'),
            'modified_source_sha256':metadata['fixed_source_sha256'],'factory_source_sha256':metadata['additional_fixture_source_sha256'],
            'suite_sha256':package['suite_sha256'],'fixed_example_count':4,'fixed_observations':8,
            'stage_counts':counts,'target_getField_coverage':hits,'measurement_status':measured['status'],
            'fixed_validation':measured.get('fixed_validation'),'fault_detected':measured.get('fault_detected'),
            'local_development_valid':valid}
        write_json(out/'receipt.json',receipt)
        write_json(out/'checksums.json',{p.relative_to(out).as_posix():sha256(p) for p in sorted(out.rglob('*'))
            if p.is_file() and p.suffix!='.class'})
        assert valid,receipt
    print('Math host/reference passed: 4 examples, 8 fixed observations, all four stages 4/0/4',flush=True)


if __name__=='__main__':
    main()
