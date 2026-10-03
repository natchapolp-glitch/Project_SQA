"""Condition-local fraction field oracle candidate for Champ's reviewed worklist."""
import json
from pathlib import Path
import subprocess
import sys
ROOT = Path(__file__).resolve().parents[4]
sys.path.insert(0,str(ROOT)); sys.path.insert(0,str(ROOT / 'scripts/study'))
import generate
from scripts.study.api854.common import cpu_slot, sha256, write_json
from scripts.study.api854.pack_suite import pack_suite
from run import stage
from evaluate import EvaluationConfig, evaluate_run

base = Path(__file__).parent
sweep = ROOT / 'docs/api854/evidence/beam-v7-fixed-sweep-20261003'
d4j = '/home/aomsin/sqa-round2/defects4j/framework/bin/defects4j'
lock_root = Path('/home/aomsin/sqa-round2/worktrees-isolated')
policy = 'beam-champ-fraction-field-v6-development'
load = lambda path: json.loads(path.read_text(encoding='utf-8'))
red = load(base / 'red.json')
assert red['passed'] is False and len(red['records'])==2
assert all(r['first']['status']=='fixture_error' and 'No structural oracle:' in r['first']['reason'] for r in red['records'])

with cpu_slot(lock_root):
    original = (ROOT / 'algorithms/java/SqaProbe.java').read_bytes().decode('utf-8')
    anchor = '            if (pilot && result instanceof java.lang.reflect.Type)'
    assert original.count(anchor)==1
    addition = ('            if (pilot && (name.equals("org.apache.commons.math3.fraction.BigFractionField")\n'
        '                    || name.equals("org.apache.commons.math3.fraction.FractionField")))\n'
        '                return "fraction-field:runtime=" + projection(call(result, "getRuntimeClass", new Class<?>[]{}), depth + 1)\n'
        '                    + ":zero=" + projection(call(result, "getZero", new Class<?>[]{}), depth + 1)\n'
        '                    + ":one=" + projection(call(result, "getOne", new Class<?>[]{}), depth + 1);\n')
    candidate = original.replace(anchor,addition+anchor).replace('beam-explicit-fixtures-v5-proposal',policy)
    helper = base / 'helper-src/algorithms/java/SqaProbe.java'
    helper.parent.mkdir(parents=True)
    helper.write_bytes(candidate.encode('utf-8'))
    classes = base / 'candidate-classes'; classes.mkdir()
    stage(['javac','-source','7','-target','7','-d',classes,helper],ROOT,base / 'compile-candidate',120)
    stage(['python3',base / 'test_field_oracle.py','--candidate'],ROOT,base / 'green-test',60)
    cp = (sweep / 'Math-1/cp.test.txt').read_text().strip()+':'+str(classes)
    previous_cases = load(sweep / 'Math-1/record.json')['cases']
    targets = [c for c in previous_cases if c['target']['method']=='getField']
    assert len(targets)==2
    rows = []
    for case in targets:
        for value in (0.5,-0.5):
            vector = [value]*len(case['vector'])
            first = generate.observe(cp,case['target'],vector,20,policy)
            second = generate.observe(cp,case['target'],vector,20,policy)
            assert first==second and first['status']=='ok' and first.get('target_invoked') is True
            assert ':zero=fraction:0/1:one=fraction:1/1' in first['outcome']
            assert 'runtime=class:'+case['target']['class'] in first['outcome']
            rows.append({'case_id':len(rows),'retained':True,'target':case['target'],'vector':vector,
                         'fixed_first':first,'fixed_second':second})
    write_json(base / 'observations.json',rows)
    generate.ROOT = base / 'helper-src'
    sources = base / 'sources'; sources.mkdir()
    (sources / 'GeneratedStudyTest.java').write_bytes(generate.suite_source(rows,policy).encode('utf-8'))
    package = pack_suite(sources,base / 'package',4,30)
    trees = lock_root / 'beam-champ-math-field-20261003'; trees.mkdir()
    for revision in ('f','b'):
        stage([d4j,'checkout','-p','Math','-v','1'+revision,'-w',trees / revision],ROOT,base / ('checkout-'+revision),900)
    prep = ROOT / 'output/api854-20261003/prepare-v7-twenty-bug-development/Math-1'
    for source in load(prep / 'context-manifest.json')['source_files']:
        if source['path'].endswith('.java'):
            assert sha256(trees / 'f' / source['path'])==source['sha256']
    supplemental = {}
    fixed_sources = base / 'supplemental-fixed-source'; fixed_sources.mkdir()
    for name in ('BigFractionField','FractionField'):
        relative = f'src/main/java/org/apache/commons/math3/fraction/{name}.java'
        data = (trees / 'f' / relative).read_bytes()
        retained = subprocess.check_output(['git','-c','safe.directory=*','-C',str(trees / 'f'),'show','HEAD:'+relative])
        assert data==retained
        (fixed_sources / (name+'.java')).write_bytes(data)
        supplemental[relative] = sha256(fixed_sources / (name+'.java'))
    instrument = base / 'target-classes.txt'
    instrument.write_text('org.apache.commons.math3.fraction.BigFraction\norg.apache.commons.math3.fraction.Fraction\n',encoding='utf-8')
    result = evaluate_run(EvaluationConfig(project='Math',bug_id=1,generator='fraction-field-oracle-development',seed=101,budget=4,
        suite=base / 'package/suite.tar.bz2',buggy_worktree=trees / 'b',fixed_worktree=trees / 'f',output=base / 'evaluation',
        classes_file=instrument,d4j=d4j,test_count=4,timeout_seconds=300))
    write_json(base / 'receipt.json', {'scope':'Two Champ-owned getField declarations, isolated structural oracle candidate.',
        'primary':False,'shared_policy_changed':False,'joint_semantic_approval':False,
        'original_probe_sha256':sha256(ROOT / 'algorithms/java/SqaProbe.java'),'candidate_probe_sha256':sha256(helper),
        'candidate_policy_id':policy,'supplemental_fixed_source_sha256':supplemental,
        'context_action_required':'Review adding both production field factory sources/knowledge to identical shared inputs before primary adoption.',
        'suite_sha256':package['suite_sha256'],'result_sha256':sha256(base / 'evaluation/record.json'),
        'status':result['status'],'fault_detected':result['fault_detected'],'real_kku_requests':0,'queue_mutations':0,
        'runner_sha256':sha256(__file__)})
    files = {p.relative_to(base).as_posix():sha256(p) for p in base.rglob('*') if p.is_file() and '__pycache__' not in p.parts}
    write_json(base / 'checksums.json',files)
    print(json.dumps({'status':result['status'],'fault_detected':result['fault_detected'],'target_declarations':2}))
