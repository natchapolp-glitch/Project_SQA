"""Predeclared Lang helper examples, independent reference oracles and JUnit proof.

Development only: no queue/provider, no primary algorithm attribution. Retains
all observations and failures; never repairs assertions from buggy feedback.
"""
import argparse
import base64
from pathlib import Path
import xml.etree.ElementTree as ET

from .common import ROOT, read_json, write_json, sha256, cpu_slot, assert_implementation, snapshot_implementation
from .fixture_policy import LANG_SIGNATURES, POLICY_V9_LANG
from .pack_suite import pack_suite
from .fixture_semantics import descriptor
from generate import observe, suite_source
from evaluate import EvaluationConfig, evaluate_run


def scalar(kind, text):
    return kind + ':' + base64.b64encode(text.encode()).decode()


# Bucket centres, input cases and expected outcomes declared before execution.
ZERO_EXAMPLES = [(-0.875, None, True), (-0.625, '', False), (-0.375, '0', True),
                 (-0.125, '000', True), (0.125, '001', False), (0.375, '12', False),
                 (0.625, '00 0', False), (0.875, '-0', False)]
ARRAY_EXAMPLES = [(-0.75, None, 'The Array must not be null'),
                  (-0.25, [], 'Array cannot be empty.'), (0.25, [0], None),
                  (0.75, [-1, 0, 7], None)]


def examples(method):
    if method == 'isAllZeros':
        return [(a, {'input': text, 'expected_boolean': boolean},
                 'value:' + scalar('java.lang.Boolean', str(boolean).lower()) + '|state=stateless-scalars')
                for a, text, boolean in ZERO_EXAMPLES]
    rows = []
    for a, values, message in ARRAY_EXAMPLES:
        array = 'null' if values is None else '[I[' + ''.join(scalar('java.lang.Integer', str(n))+';' for n in values) + ']'
        outcome = ('void' if message is None else 'exception:java.lang.IllegalArgumentException|message='
                   + scalar('java.lang.String', message)) + '|state=validation-input:' + array
        rows.append((a, {'input': values, 'expected_message': message}, outcome))
    return rows


def run(development, output, worktrees, d4j):
    development, output = Path(development), Path(output)
    index = read_json(development/'index.json')
    protocol = read_json(development/'protocol-proposal.json')
    if index['fixture_policy_id'] != POLICY_V9_LANG:
        raise ValueError('Require the predeclared combined Lang condition')
    output.mkdir(parents=True, exist_ok=False)
    summary = {'primary': False, 'gate_a_approved': False, 'team_or_primary_approval': False,
               'generator': 'lang-helper-reference-development', 'fixture_policy_id': POLICY_V9_LANG,
               'reviewer_sha256': sha256(__file__), 'source_sha256': protocol['source_sha256'],
               'protocol_sha256': sha256(development/'protocol-proposal.json'),
               'real_kku_requests': 0, 'live_queue_mutations': 0,
               'limitations': ['Bounded predeclared examples, not exhaustive domain coverage.',
                              'A reference suite is not an FSCS-ART/CMA-ES primary result.']}
    with cpu_slot(worktrees):
        assert_implementation(protocol['source_sha256'])
        snapshot_implementation(output, protocol['source_sha256'])
        (output/'lang_fixture_review.py').write_bytes(Path(__file__).read_bytes())
        row = next(r for r in index['records'] if r['project']=='Lang' and r['approach']=='fscs-art')
        generation = Path(row['generation_result']).parent
        generated = read_json(generation/'result.json')
        adapter = read_json(generation/'setup/adapter.json')
        fixed, buggy = Path(adapter['fixed_worktree']), Path(adapter['buggy_worktree'])
        sources = generated['fixed_source_sha256']
        for relative, digest in sources.items():
            if sha256(fixed/relative) != digest:
                raise ValueError('Fixed source changed after generation')
        summary['fixed_source_sha256'] = sources
        for relative in sources:
            destination = output/'fixed-source'/relative
            destination.parent.mkdir(parents=True,exist_ok=True)
            destination.write_bytes((fixed/relative).read_bytes())
        targets = read_json(generation/'setup/targets.fixture-policy.json')['targets']
        cases, selected = [], set()
        for target in targets:
            identity = tuple(target[k] for k in ('class','constructor_types','method','parameter_types'))
            if identity not in LANG_SIGNATURES:
                continue
            selected.add(identity)
            for coordinate, declared, reference in examples(target['method']):
                vector = [coordinate,0.0,0.0]
                first = observe(adapter['classpath'],target,vector,10,POLICY_V9_LANG)
                second = observe(adapter['classpath'],target,vector,10,POLICY_V9_LANG)
                passed = (first==second and first.get('status')=='ok' and first.get('target_invoked') is True
                          and first.get('outcome')==reference)
                cases.append({'case_id':len(cases)+1,'target':target,'vector':vector,'declared_input':declared,
                              'expected':reference,'fixed_first':first,'fixed_second':second,
                              'stable':first==second,'retained':passed,'reference_check_passed':passed})
        write_json(output/'reference-observations.json',cases)
        summary.update(observed_declarations=len(selected),example_count=len(cases),
                       fixed_observation_count=2*len(cases),
                       reference_passed=selected==LANG_SIGNATURES and len(cases)==12
                       and all(c['reference_check_passed'] for c in cases))
        if summary['reference_passed']:
            source_dir=output/'sources'; source_dir.mkdir()
            (source_dir/'GeneratedStudyTest.java').write_text(suite_source(cases,POLICY_V9_LANG),encoding='utf-8')
            package=pack_suite(source_dir,output/'package',len(cases))
            summary['suite_sha256']=package['suite_sha256']
            classes=output/'target-classes.txt'
            classes.write_text('org.apache.commons.lang3.math.NumberUtils\n',encoding='utf-8')
            measured=evaluate_run(EvaluationConfig(project='Lang',bug_id=1,
                generator=summary['generator'],seed=101,budget=len(cases),test_count=len(cases),
                suite=output/'package/suite.tar.bz2',fixed_worktree=fixed,buggy_worktree=buggy,
                output=output/'evaluation',classes_file=classes,d4j=d4j,timeout_seconds=900))
            summary.update(measurement_status=measured['status'],fixed_validation=measured.get('fixed_validation'),
                           fault_detected=measured.get('fault_detected'))
            counts={}
            for stage in ['fixed-1','fixed-2','buggy','coverage']:
                path=output/'evaluation'/stage/'sqa-stage-counts.json'
                counts[stage]=read_json(path) if path.is_file() else None
            summary['stage_counts']=counts
            coverage=output/'evaluation/coverage/coverage.xml'
            covered={(cls.get('name'),m.get('name'),m.get('signature','').split(')')[0]+')')
                for cls in ET.parse(coverage).getroot().iter('class') for m in cls.findall('./methods/method')
                if any(int(line.get('hits','0'))>0 for line in m.findall('./lines/line'))} if coverage.is_file() else set()
            summary['target_coverage']=[{'target':c['target'],'method_covered':
                (c['target']['class'],c['target']['method'],descriptor(c['target']['parameter_types'])) in covered}
                for c in cases if c['case_id'] in {1,9}]
            summary['local_development_valid']=(measured['status']=='complete' and measured.get('fixed_validation')=='passed'
                and all(c and c['executed']==c['target_checks']==12 and c['skipped']==0 for c in counts.values())
                and len(summary['target_coverage'])==2 and all(c['method_covered'] for c in summary['target_coverage']))
        else:
            summary['local_development_valid']=False
        assert_implementation(protocol['source_sha256'])
        for relative,digest in sources.items():
            if sha256(fixed/relative)!=digest:
                raise ValueError('Fixed source changed during reference evaluation')
    write_json(output/'receipt.json',summary)
    write_json(output/'checksums.json',{p.relative_to(output).as_posix():sha256(p)
               for p in sorted(output.rglob('*')) if p.is_file()})
    print('LANG REFERENCE:',summary['example_count'],'examples; fixed_reference=',summary['reference_passed'],
          'local_development_valid=',summary['local_development_valid'],flush=True)
    return summary['local_development_valid']


if __name__=='__main__':
    parser=argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--development',required=True,type=Path)
    parser.add_argument('--output',required=True,type=Path)
    parser.add_argument('--worktrees',required=True,type=Path)
    parser.add_argument('--d4j',default='defects4j')
    args=parser.parse_args()
    raise SystemExit(0 if run(args.development,args.output,args.worktrees,args.d4j) else 1)
