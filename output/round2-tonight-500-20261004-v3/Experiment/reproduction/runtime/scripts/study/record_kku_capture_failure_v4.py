#!/usr/bin/env python3
"""Record an observed KKU service/empty-output failure without inventing source."""
import argparse
from datetime import datetime, timezone
import hashlib
import json
from pathlib import Path
import re

ROOT = Path(__file__).resolve().parents[2]


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--family', choices=('claude', 'gemini'), required=True)
    parser.add_argument('--project', required=True)
    parser.add_argument('--index', type=int, required=True)
    parser.add_argument('--reason', choices=('truncated_no_complete_tests', 'truncated_first_test_body', 'scaffold_no_target_tests'))
    parser.add_argument('--capture', type=Path, required=True)
    args = parser.parse_args()
    base = ROOT / 'results/study/kku-only-20261001' / args.family
    context = json.loads((base / args.project / 'ai-context/context-manifest.json').read_text())
    capture = args.capture.resolve()
    if not capture.is_relative_to(ROOT/'ai-tests/provider-captures'):
        raise ValueError('Capture path outside evidence tree')
    operator_path = capture / 'operator-metadata.json'
    operator = json.loads(operator_path.read_text())
    reason = args.reason or operator.get('capture_status')
    if reason == 'scaffold_no_target_tests':
        blocks=json.loads((capture/'rendered-code.json').read_text(encoding='utf-8'))
        source=blocks[0]['text'] if len(blocks)==1 else ''
        expected='44653e13ad4ea493f8d609a53f38e8188c2b5db114f8b80d312e095820de3c6f'
        if (args.family,args.project,args.index)!=('claude','JacksonDatabind',103) or hashlib.sha256(source.encode()).hexdigest()!=expected:
            raise ValueError('Scaffold differs from the reviewed capture')
        executable=re.sub(r'/\*.*?\*/|//[^\n]*','',source,flags=re.S)
        if len(re.findall(r'@Test\b',executable))!=24 or re.search(r'new\s+BeanPropertyWriter\b|\bwriter\s*\.',executable):
            raise ValueError('Scaffold target-use review no longer applies')
        review={'reviewed_before_execution':True,'source_sha256':expected,'raw_declared_tests':24,
                'comment_only_test_methods':23,'remaining_test':'testGetSerializedName checks only SerializedString; target BeanPropertyWriter is never initialized or called.',
                'reason':'Illustrative scaffold lacks executable tests of the requested target. No compile, fixed, buggy, or coverage measurements inferred.'}
        (capture/'quality-review.json').write_text(json.dumps(review,indent=2)+'\n',encoding='utf-8')
    elif reason == 'truncated_no_complete_tests':
        response = (capture / 'response.md').read_text(encoding='utf-8')
        blocks = re.findall(r'```java[^\n]*\n(.*?)\n```', response, re.S | re.I)
        completeness = json.loads((capture / 'capture-completeness.json').read_text())
        if not blocks or completeness.get('complete_test_methods_observed') != 0 or any(re.search(r'@Test\b|public\s+void\s+test\w*\s*\(', b) for b in blocks):
            raise ValueError('No-test truncation evidence does not match this capture')
    elif reason == 'truncated_first_test_body':
        blocks = json.loads((capture / 'rendered-code.json').read_text())
        source = blocks[0]['text'] if len(blocks) == 1 else ''
        expected = '1ffa664133c68adbc2dbb1fc3f230fa5dfa9d94283de17d6f8babe7edc3cbf5c'
        if hashlib.sha256(source.encode()).hexdigest() != expected or source.rstrip().split('public void testEmptyCommandLineState() {')[-1] != '':
            raise ValueError('First-method truncation differs from observed capture')
        if len(re.findall(r'public\s+void\s+test\w*\s*\(',source)) != 1 or source.rstrip() not in (capture / 'response.md').read_text(encoding='utf-8'):
            raise ValueError('Cannot establish absence of complete test methods')
    elif reason not in ('empty-final-observed', 'service_failed', 'quota_failed'):
        raise ValueError('Capture does not describe an observed failure')
    if operator['selected_agent'].lower() != args.family:
        raise ValueError('Family mismatch')
    run = base / args.project / f'intellisphere-s{args.index}-b30'
    output = run / 'evaluation/record.json'
    if run.exists():
        raise ValueError('Existing attempts must be preserved, never overwritten')
    output.parent.mkdir(parents=True)
    record = {'schema_version':1, 'run_id':run.relative_to(ROOT).as_posix(),
              'project':args.project, 'bug_id':context['bug_id'], 'generator':'intellisphere',
              'seed':args.index, 'budget':30, 'status':'generation_failed', 'test_count':0,
              'compile_status':'not_run', 'fault_detected':None,
              'line_covered':None, 'line_total':None, 'branch_covered':None, 'branch_total':None,
              'duration_seconds':None, 'generation_seconds':operator['generation_seconds'],
              'total_seconds':None, 'workflow_seconds':operator['generation_seconds'],
              'generation_time_scope':'Time to observed missing/service output, not successful model compute time.',
              'failed_stage':'provider-output', 'error':reason,
              'ai_capture_path':capture.relative_to(ROOT).as_posix(),
              'capture_metadata_sha256':hashlib.sha256(operator_path.read_bytes()).hexdigest(),
              'raw_response_sha256':hashlib.sha256((capture / 'response.md').read_bytes()).hexdigest(),
              'source_sha256':json.loads((base / 'config.json').read_text())['source_sha256'],
              'recorded_at_utc':datetime.now(timezone.utc).isoformat(),
              'failure_recorder_sha256':hashlib.sha256(Path(__file__).read_bytes()).hexdigest(),
              'note':'Raw response retained. No target test execution or measured outcome inferred; visible thinking excluded. See quality-review.json for scaffold failures.'}
    output.write_text(json.dumps(record, indent=2) + '\n')
    print(json.dumps({'project':args.project, 'family':args.family, 'status':record['status'], 'reason':record['error']}))


if __name__ == '__main__':
    main()
