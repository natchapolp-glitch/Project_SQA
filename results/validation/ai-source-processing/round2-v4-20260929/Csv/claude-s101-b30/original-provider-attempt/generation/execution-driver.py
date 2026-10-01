#!/usr/bin/env python3
"""Extract actual provider code, preserve provenance, and run the frozen evaluator."""
from __future__ import annotations

import argparse
import hashlib
import json
from pathlib import Path
import re
import shutil
import sys

ROOT = Path(__file__).resolve().parents[2]
sys.path.insert(0, str(ROOT / 'scripts/ai'))
from evidence import ingest
from evaluate import EvaluationConfig, evaluate_run, write_record


def digest(path):
    return hashlib.sha256(path.read_bytes()).hexdigest()


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--tool', choices=('claude', 'intellisphere'), required=True)
    parser.add_argument('--project', required=True)
    parser.add_argument('--seed', type=int, required=True)
    parser.add_argument('--capture', type=Path, required=True)
    parser.add_argument('--batch', type=Path, default=ROOT / 'results/study/round2-v4-20260929')
    parser.add_argument('--worktrees', type=Path, default=Path('/home/aomsin/sqa-round2/worktrees'))
    parser.add_argument('--d4j', default='/home/aomsin/sqa-round2/defects4j/framework/bin/defects4j')
    args = parser.parse_args()
    batch = args.batch.resolve()
    context = batch / args.project / 'ai-context'
    identity = json.loads((context / 'context-manifest.json').read_text())
    bug = identity['bug_id']
    if identity['project'] != args.project:
        parser.error('Project identity does not match prepared context')
    frozen = json.loads((batch / 'config.json').read_text())['source_sha256']
    for name, expected in frozen.items():
        if digest(ROOT / name) != expected:
            parser.error('Frozen source has changed: ' + name)
    capture = args.capture.resolve()
    operator = json.loads((capture / 'operator-metadata.json').read_text())
    response = (capture / 'response.md').read_text(encoding='utf-8')
    tests = capture / 'extracted-tests'
    tests.mkdir(exist_ok=False)
    sources = []
    for code in re.findall(r'```java[^\n]*\n(.*?)\n```', response, re.S | re.I):
        declaration = re.search(r'\bpublic\s+(?:final\s+)?class\s+([A-Za-z_$][\w$]*)', code)
        if not declaration:
            continue
        package = re.search(r'^\s*package\s+([\w.]+)\s*;', code, re.M)
        relative = Path(*(package.group(1).split('.') if package else [])) / (declaration.group(1) + '.java')
        destination = tests / relative
        if destination.exists():
            parser.error('Duplicate source declaration: ' + str(relative))
        destination.parent.mkdir(parents=True, exist_ok=True)
        destination.write_text(code.strip() + '\n', encoding='utf-8')
        sources.append(code)
    counts = []
    for code in sources:
        annotated = len(re.findall(r'@(?:org\.junit\.)?Test\b', code))
        counts.append(annotated or len(re.findall(r'\bpublic\s+void\s+test\w*\s*\(', code)))
    test_count = sum(counts)
    run = batch / args.project / f'{args.tool}-s{args.seed}-b30'
    iteration = operator.get('prompt_iteration', 1)
    admissible = 1 <= test_count <= 30
    generation = (run / 'generation' if admissible else
        ROOT / 'results/validation/ai-generation-failures' / batch.name / args.project /
        run.name / f'iteration-{iteration}' / 'generation')
    prompt = context / 'prompt.md' if iteration == 1 else capture / 'prompt.md'
    result = ingest(argparse.Namespace(tool=args.tool, model=operator['model'],
        prompt=prompt, response=capture / 'response.md', tests_dir=tests,
        output=generation, metadata_file=capture / 'operator-metadata.json', seed=args.seed, iteration=iteration))
    if not result['external_suite']:
        print(json.dumps({'status': 'no_java_sources', 'evidence': str(generation)}))
        return 1
    if not admissible:
        (generation / 'attempt-status.json').write_text(json.dumps({'status':'budget_violation',
            'observed_test_count':test_count,'maximum_test_count':30,
            'generation_seconds':operator['generation_seconds']}, indent=2))
        print(json.dumps({'status': 'unsupported_test_count', 'test_count': test_count,
                          'evidence': str(generation)}))
        return 1
    driver_hash = digest(Path(__file__))
    shutil.copy2(Path(__file__), generation / 'execution-driver.py')
    record = evaluate_run(EvaluationConfig(project=args.project, bug_id=bug,
        generator=args.tool, seed=args.seed, budget=30,
        suite=generation / result['external_suite'],
        fixed_worktree=args.worktrees / args.project / str(bug) / 'f',
        buggy_worktree=args.worktrees / args.project / str(bug) / 'b',
        output=run / 'evaluation', classes_file=context / 'classes.txt', d4j=args.d4j,
        generation_seconds=operator['generation_seconds'] +
            sum(a['generation_seconds'] for a in operator.get('prior_attempts', [])), test_count=test_count,
        timeout_seconds=900))
    record.update(source_sha256=frozen, run_id=f'{batch.name}/{args.project}/{run.name}',
        ai_evidence_metadata=(generation / 'metadata.json').relative_to(ROOT).as_posix(),
        ai_execution_driver='scripts/study/evaluate_provider.py',
        ai_execution_driver_sha256=driver_hash,
        ai_prior_attempts=operator.get('prior_attempts', []),
        generation_time_scope='Submit in provider UI until completion observed; includes UI/network and observation latency.')
    write_record(run / 'evaluation/record.json', record)
    print(json.dumps({'project': args.project, 'tool': args.tool, 'seed': args.seed,
                      'test_count': test_count, 'status': record['status'],
                      'fault_detected': record['fault_detected']}))
    return 0 if record['status'] == 'complete' else 1


if __name__ == '__main__':
    raise SystemExit(main())
