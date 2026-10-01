#!/usr/bin/env python3
"""Record an observed KKU service/empty-output failure without inventing source."""
import argparse
from datetime import datetime, timezone
import hashlib
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[2]


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--family', choices=('claude', 'gemini'), required=True)
    parser.add_argument('--project', required=True)
    parser.add_argument('--index', type=int, required=True)
    args = parser.parse_args()
    base = ROOT / 'results/study/kku-only-20261001' / args.family
    context = json.loads((base / args.project / 'ai-context/context-manifest.json').read_text())
    capture = ROOT / f'ai-tests/provider-captures/kku-only-20261001/{args.family}/{args.project}-{context["bug_id"]}/s{args.index}-i1'
    operator_path = capture / 'operator-metadata.json'
    operator = json.loads(operator_path.read_text())
    if operator.get('capture_status') not in ('empty-final-observed', 'service_failed', 'quota_failed'):
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
              'failed_stage':'provider-output', 'error':operator['capture_status'],
              'ai_capture_path':capture.relative_to(ROOT).as_posix(),
              'capture_metadata_sha256':hashlib.sha256(operator_path.read_bytes()).hexdigest(),
              'raw_response_sha256':hashlib.sha256((capture / 'response.md').read_bytes()).hexdigest(),
              'source_sha256':json.loads((base / 'config.json').read_text())['source_sha256'],
              'recorded_at_utc':datetime.now(timezone.utc).isoformat(),
              'failure_recorder_sha256':hashlib.sha256(Path(__file__).read_bytes()).hexdigest(),
              'note':'No Java or evaluation outcome inferred; visible thinking is excluded.'}
    output.write_text(json.dumps(record, indent=2) + '\n')
    print(json.dumps({'project':args.project, 'family':args.family, 'status':record['status'], 'reason':record['error']}))


if __name__ == '__main__':
    main()
