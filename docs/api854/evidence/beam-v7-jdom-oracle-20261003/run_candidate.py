"""New condition-local JDOM oracle candidate; does not repin or edit shared v7."""
import json
from pathlib import Path
import subprocess
import sys

ROOT = Path(__file__).resolve().parents[4]
sys.path.insert(0, str(ROOT))
sys.path.insert(0, str(ROOT / 'scripts/study'))
import generate
from scripts.study.api854.common import cpu_slot, write_json, sha256
from scripts.study.api854.pack_suite import pack_suite
from run import stage
from evaluate import EvaluationConfig, evaluate_run

base = Path(__file__).parent
sweep = ROOT / 'docs/api854/evidence/beam-v7-fixed-sweep-20261003'
d4j = '/home/aomsin/sqa-round2/defects4j/framework/bin/defects4j'
lock_root = Path('/home/aomsin/sqa-round2/worktrees-isolated')
policy = 'beam-jdom-attribute-oracle-v6-development'
load = lambda p: json.loads(p.read_text(encoding='utf-8'))
assert load(base / 'red.json')['passed'] is False
assert load(base / 'red.json')['first']['reason'] == 'No structural oracle: org.jdom.Attribute'

with cpu_slot(lock_root):
    original = (ROOT / 'algorithms/java/SqaProbe.java').read_bytes().decode('utf-8')
    anchor = '            if (name.equals("org.jdom.Element") || name.equals("org.jdom.ProcessingInstruction")'
    assert original.count(anchor) == 1
    addition = ('            if (name.equals("org.jdom.Attribute"))\n'
        '                return "jdom-attribute:name=" + projection(call(result, "getName", new Class<?>[]{}), depth + 1)\n'
        '                    + ":namespace=" + projection(call(result, "getNamespaceURI", new Class<?>[]{}), depth + 1)\n'
        '                    + ":value=" + projection(call(result, "getValue", new Class<?>[]{}), depth + 1);\n')
    candidate = original.replace(anchor, addition + anchor).replace('beam-explicit-fixtures-v5-proposal', policy)
    helper = base / 'helper-src/algorithms/java/SqaProbe.java'
    helper.parent.mkdir(parents=True)
    helper.write_bytes(candidate.encode('utf-8'))
    classes = base / 'candidate-classes'
    classes.mkdir()
    stage(['javac','-source','7','-target','7','-d',classes,helper], ROOT, base / 'compile-candidate',120)
    stage(['python3',base / 'test_oracle.py','--candidate'], ROOT, base / 'green-test',60)
    target = load(base / 'red.json')['target']
    cp = (sweep / 'JxPath-1/cp.test.txt').read_text().strip() + ':' + str(classes)
    rows = []
    for n, value in enumerate((0.5,-0.5)):
        vector = [value] * 9
        first = generate.observe(cp,target,vector,20,policy)
        second = generate.observe(cp,target,vector,20,policy)
        assert first == second and first['status'] == 'ok' and first.get('target_invoked') is True
        assert 'jdom-attribute:' in first['outcome'] and not first['outcome'].startswith('exception:')
        rows.append({'case_id':n,'retained':True,'target':target,'vector':vector,'fixed_first':first,'fixed_second':second})
    assert rows[0]['fixed_first']['outcome'] != rows[1]['fixed_first']['outcome']
    write_json(base / 'observations.json', rows)
    # suite_source reads the retained candidate helper rather than the runtime helper.
    generate.ROOT = base / 'helper-src'
    sources = base / 'sources'
    sources.mkdir()
    (sources / 'GeneratedStudyTest.java').write_bytes(generate.suite_source(rows,policy).encode('utf-8'))
    package = pack_suite(sources,base / 'package',2,30)
    trees = lock_root / 'beam-v7-jdom-oracle-20261003'
    trees.mkdir()
    for revision in ('f','b'):
        stage([d4j,'checkout','-p','JxPath','-v','1'+revision,'-w',trees / revision],ROOT,base / ('checkout-'+revision),900)
    prep = ROOT / 'output/api854-20261003/prepare-v7-twenty-bug-development/JxPath-1'
    manifest = load(prep / 'context-manifest.json')
    for source in manifest['source_files']:
        if source['path'].endswith('.java'):
            assert sha256(trees / 'f' / source['path']) == source['sha256']
    instrument = base / 'target-classes.txt'
    instrument.write_text('org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer\n',encoding='utf-8')
    result = evaluate_run(EvaluationConfig(project='JxPath',bug_id=1,generator='jdom-oracle-development',seed=101,budget=2,
        suite=base / 'package/suite.tar.bz2',buggy_worktree=trees / 'b',fixed_worktree=trees / 'f',
        output=base / 'evaluation',classes_file=instrument,d4j=d4j,test_count=2,timeout_seconds=300))
    write_json(base / 'receipt.json', {'primary':False,'shared_policy_changed':False,'team_semantic_approval':False,
        'purpose':'Condition-local candidate for one selected declaration whose v7 oracle fails; not a study approach.',
        'original_probe_sha256':sha256(ROOT / 'algorithms/java/SqaProbe.java'),'candidate_probe_sha256':sha256(helper),
        'candidate_policy_id':policy,'suite_sha256':package['suite_sha256'],'result_sha256':sha256(base / 'evaluation/record.json'),
        'status':result['status'],'fault_detected':result['fault_detected'],'real_kku_requests':0,'queue_mutations':0,
        'runner_sha256':sha256(__file__)})
    checksums = {p.relative_to(base).as_posix():sha256(p) for p in base.rglob('*') if p.is_file() and '__pycache__' not in p.parts}
    write_json(base / 'checksums.json',checksums)
    print(json.dumps({'status':result['status'],'fault_detected':result['fault_detected']}))
