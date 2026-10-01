#!/usr/bin/env python3
"""Evaluate original-prompt AI responses with disclosed local source processing."""
from __future__ import annotations
import argparse
import fcntl
import hashlib
import json
from pathlib import Path
import re
import shutil
import sys
import time

ROOT = Path(__file__).resolve().parents[2]
sys.path.insert(0, str(ROOT / 'scripts/ai'))
from evidence import ingest
from evaluate import EvaluationConfig, evaluate_run, write_record
from normalize_provider_source import normalize, positions, prune_fixed_failures
from provider_compatibility_v37 import repair

PROCESSING_SOURCES = ('scripts/study/evaluate_provider_normalized_v37.py',
    'scripts/study/provider_prefix_v30.py',
    'scripts/study/provider_compatibility_v31.py',
    'scripts/study/provider_compatibility_v32.py',
    'scripts/study/provider_compatibility_v33.py',
    'scripts/study/provider_compatibility_v34.py',
    'scripts/study/provider_compatibility_v35.py',
    'scripts/study/provider_compatibility_v36.py',
    'scripts/study/provider_compatibility_v37.py',
    'scripts/study/normalize_provider_source.py', 'scripts/study/java/TestMethodPositions.java')


def sha(path):
    return hashlib.sha256(path.read_bytes()).hexdigest()


def preserve_run(run, destination):
    if not run.resolve().is_relative_to(ROOT / 'results/study') or not destination.resolve().is_relative_to(ROOT / 'results/validation'):
        raise ValueError('Attempt paths escape the result directories')
    if destination.exists():
        raise ValueError('Attempt history already exists: ' + str(destination))
    destination.parent.mkdir(parents=True, exist_ok=True)
    shutil.move(str(run), str(destination))


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
    batch, capture = args.batch.resolve(), args.capture.resolve()
    context = batch / args.project / 'ai-context'
    identity = json.loads((context / 'context-manifest.json').read_text())
    bug = identity['bug_id']
    operator = json.loads((capture / 'operator-metadata.json').read_text())
    if operator.get('prompt_iteration', 1) != 1:
        parser.error('This cohort uses only the original prepared prompt')
    frozen = json.loads((batch / 'config.json').read_text())['source_sha256']
    for name, expected in frozen.items():
        if sha(ROOT / name) != expected:
            parser.error('Frozen source changed: ' + name)
    processing_hashes = {name: sha(ROOT / name) for name in PROCESSING_SOURCES}
    run = batch / args.project / f'{args.tool}-s{args.seed}-b30'
    history = ROOT / 'results/validation/ai-source-processing-v37' / batch.name / args.project / run.name
    if run.exists():
        old = json.loads((run / 'evaluation/record.json').read_text())
        if old['status'] == 'complete':
            parser.error('A completed run must not be overwritten')
        preserve_run(run, history / 'original-provider-attempt')
    started = time.monotonic()
    working = capture / 'source-processing-v37'
    tests = working / 'tests'
    originals = working / 'input-tests'
    originals.mkdir(parents=True, exist_ok=False)
    response = (capture / 'response.md').read_text(encoding='utf-8')
    for code in re.findall(r'```java[^\n]*\n(.*?)\n```', response, re.S | re.I):
        cls = re.search(r'\bpublic\s+(?:final\s+)?class\s+([A-Za-z_$][\w$]*)', code)
        if not cls:
            continue
        package = re.search(r'^\s*package\s+([\w.]+)\s*;', code, re.M)
        path = originals.joinpath(*(package.group(1).split('.') if package else []), cls.group(1) + '.java')
        if path.exists():
            parser.error('Duplicate source class: ' + cls.group(1))
        path.parent.mkdir(parents=True, exist_ok=True)
        path.write_text(code.strip() + '\n', encoding='utf-8')
    shutil.copytree(originals, tests)
    prefix_edits = repair(tests, args.project, args.tool, args.seed)
    count, raw_count, edits = normalize(tests, args.tool)
    edits = prefix_edits + edits
    processing_seconds = time.monotonic() - started
    prior_evaluation_seconds, pruning_history = 0.0, []
    project_lock = batch / args.project / 'ai-evaluator.lock'
    for attempt in range(1, 4):
        run.mkdir(parents=True, exist_ok=True)
        metadata = dict(operator)
        metadata.update(context_manifest=str(context / 'context-manifest.json'),
            manual_edits=edits, local_processing_policy='provider-source-processing-v37',
            raw_test_method_count=raw_count, retained_test_method_count=count,
            generation_seconds=operator['generation_seconds'] + processing_seconds)
        metadata['notes'] = operator.get('notes', []) + [
            'Local processing v37: hash-identified Claude Cli complete-prefix recovery plus hash-identified Claude Jsoup complete-prefix recovery plus fixed-diagnosed Time String-null overload disambiguation plus exact-tail, hash-identified Gemini Lang complete-prefix recovery before evaluation; source-order cap at 30 tests, KKU renderer suffix cleanup, and up to two fixed-only method-pruning follow-ups.',
            'Raw provider response unchanged. No evaluation results were sent to either provider.',
            'The evaluated cohort is AI-assisted with local processing, not unedited model output.']
        operator_file = run / 'processing-operator-metadata.json'
        operator_file.write_text(json.dumps(metadata, ensure_ascii=False, indent=2), encoding='utf-8')
        generation = run / 'generation'
        generated = ingest(argparse.Namespace(tool=args.tool, model=metadata['model'],
            prompt=context / 'prompt.md', response=capture / 'response.md', tests_dir=tests,
            output=generation, metadata_file=operator_file, seed=args.seed, iteration=1))
        for name in PROCESSING_SOURCES:
            target = generation / 'processing-sources' / name
            target.parent.mkdir(parents=True, exist_ok=True)
            shutil.copy2(ROOT / name, target)
        shutil.copy2(Path(__file__), generation / 'execution-driver.py')
        if not generated['external_suite'] or count == 0:
            record = {'schema_version':1,'project':args.project,'bug_id':bug,'generator':args.tool,
                'seed':args.seed,'budget':30,'status':'generation_failed','test_count':count,
                'compile_status':'not_run','fault_detected':None,'line_covered':None,'line_total':None,
                'branch_covered':None,'branch_total':None,'duration_seconds':None,
                'generation_seconds':metadata['generation_seconds'],'total_seconds':None,
                'workflow_seconds':metadata['generation_seconds'] + prior_evaluation_seconds,
                'failed_stage':'generation','error':'No runnable test methods remained.'}
        else:
            with project_lock.open('a') as lock:
                fcntl.flock(lock, fcntl.LOCK_EX)
                record = evaluate_run(EvaluationConfig(project=args.project, bug_id=bug,
                    generator=args.tool, seed=args.seed, budget=30,
                    suite=generation / generated['external_suite'],
                    fixed_worktree=args.worktrees / args.project / str(bug) / 'f',
                    buggy_worktree=args.worktrees / args.project / str(bug) / 'b',
                    output=run / 'evaluation', classes_file=context / 'classes.txt', d4j=args.d4j,
                    generation_seconds=metadata['generation_seconds'],test_count=count,timeout_seconds=900))
            record['workflow_seconds'] = record.get('total_seconds', 0) + prior_evaluation_seconds
        record.update(source_sha256=frozen, run_id=f'{batch.name}/{args.project}/{run.name}',
            ai_evidence_metadata=(generation / 'metadata.json').relative_to(ROOT).as_posix(),
            ai_execution_driver=PROCESSING_SOURCES[0],
            ai_execution_driver_sha256=processing_hashes[PROCESSING_SOURCES[0]],
            ai_processing_source_sha256=processing_hashes,
            ai_source_processing_directory='source-processing-v37',
            ai_raw_test_method_count=raw_count,ai_local_processing_edits=edits,
            ai_fixed_pruning_history=pruning_history,
            test_count_source='java_ast_declared_test_methods',
            generation_time_scope='Provider UI observation upper bound plus measured local source processing.',
            ai_output_scope='AI-assisted with disclosed local source processing; not unedited model output.')
        record['ai_capture_path'] = capture.relative_to(ROOT).as_posix()
        (run / 'evaluation').mkdir(exist_ok=True)
        write_record(run / 'evaluation/record.json', record)
        if record['status'] != 'invalid' or attempt == 3:
            break
        stage = next((s for s in ('fixed-1','fixed-2') if record['stages'].get(s,{}).get('failure_count')), None)
        if stage is None:
            break
        failures = set(record['stages'][stage]['failing_tests'])
        prune_started = time.monotonic()
        removed = prune_fixed_failures(tests, failures)
        if not removed:
            break
        previous = history / f'fixed-validation-attempt-{attempt}'
        preserve_run(run, previous)
        edits += removed
        prior_evaluation_seconds += record['duration_seconds']
        pruning_history.append({'previous_record':(previous / 'evaluation/record.json').relative_to(ROOT).as_posix(),
            'fixed_stage_used':stage,'removed_methods':[m for e in removed for m in e['fixed_failed_methods_removed']]})
        count = sum(len(positions(p)) for p in tests.rglob('*.java'))
        processing_seconds += time.monotonic() - prune_started
    print(json.dumps({'project':args.project,'tool':args.tool,'seed':args.seed,
        'raw_tests':raw_count,'final_tests':count,'status':record['status'],
        'fault_detected':record['fault_detected']}))
    return 0 if record['status'] == 'complete' else 1


if __name__ == '__main__':
    raise SystemExit(main())
