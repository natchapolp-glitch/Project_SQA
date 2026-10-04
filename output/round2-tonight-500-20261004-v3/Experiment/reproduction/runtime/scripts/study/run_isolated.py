#!/usr/bin/env python3
"""Evaluate one planned algorithm run in separate Defects4J checkouts.

Uses the batch's frozen inventory/source configuration. Intended for disjoint
run identities while a serial worker evaluates another seed/method. Refuses to
replace an existing generation or evaluation. Setup cost is retained separately.
"""
from __future__ import annotations
import argparse
import hashlib
import json
from pathlib import Path
import shutil
import time

from evaluate import EvaluationConfig, evaluate_run, validate_worktree
from generate import generate_suite
from run import ROOT, stage, save


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--batch', required=True, type=Path)
    parser.add_argument('--d4j', required=True, type=Path)
    parser.add_argument('--worktrees', required=True, type=Path)
    parser.add_argument('--project', required=True, choices=['Mockito'])
    parser.add_argument('--generator', required=True, choices=['cmaes', 'fscs-art'])
    parser.add_argument('--seed', required=True, type=int)
    args = parser.parse_args()
    batch = args.batch.resolve()
    if batch.parent != (ROOT / 'results/study').resolve():
        parser.error('Batch must stay inside repository results/study')
    config = json.loads((batch / 'config.json').read_text())
    if args.seed not in config['seeds']:
        parser.error('Seed is not in the frozen batch')
    for name, expected in config['source_sha256'].items():
        if hashlib.sha256((ROOT / name).read_bytes()).hexdigest() != expected:
            raise ValueError('Frozen source changed')
    metadata_file = batch / args.project / 'setup/prepared.json'
    started = time.monotonic()
    while not metadata_file.is_file():
        if time.monotonic() - started > 600:
            raise TimeoutError('Main worker did not prepare the project inventory')
        time.sleep(1)
    prepared = json.loads(metadata_file.read_text())
    for budget in config['budgets']:
        name = f'{args.generator}-s{args.seed}-b{budget}'
        folder = batch / args.project / name
        setup = batch / args.project / 'parallel-setup' / name
        if (folder / 'generation').exists() or (folder / 'evaluation').exists():
            print(f'{name}: existing run; skipped', flush=True)
            continue
        setup.mkdir(parents=True, exist_ok=False)
        shutil.copy2(Path(__file__), setup / 'execution-driver.py')
        trees = {}
        for revision in ('f', 'b'):
            tree = (args.worktrees / args.project / str(prepared['bug_id']) / name / revision).resolve()
            tree.parent.mkdir(parents=True, exist_ok=True)
            if tree.exists():
                validate_worktree(tree, args.project, str(prepared['bug_id']) + revision)
            else:
                stage([args.d4j.resolve(), 'checkout', '-p', args.project, '-v', str(prepared['bug_id']) + revision,
                       '-w', tree], ROOT, setup / ('checkout-' + revision), config['command_timeout_seconds'])
            stage([args.d4j.resolve(), 'compile', '-w', tree], ROOT, setup / ('compile-' + revision), config['command_timeout_seconds'])
            trees[revision] = tree
        if (folder / 'generation').exists() or (folder / 'evaluation').exists():
            print(f'{name}: main worker started it; skipped', flush=True)
            continue
        targets_path = Path(prepared['targets_file'])
        targets = json.loads(targets_path.read_text())['targets']
        classpath = prepared['classpath'].replace(prepared['fixed_worktree'], str(trees['f']))
        generated = generate_suite(args.project, prepared['bug_id'], args.generator, budget, args.seed,
            targets, classpath, folder / 'generation', config['observation_timeout_seconds'])
        if not generated['test_count']:
            raise ValueError('No stable generated tests in isolated checkout')
        result = evaluate_run(EvaluationConfig(args.project, prepared['bug_id'], args.generator, args.seed,
            budget, Path(generated['suite']), trees['b'], trees['f'], folder / 'evaluation',
            str(args.d4j.resolve()), Path(prepared['classes_file']), generated['generation_seconds'],
            generated['test_count'], config['command_timeout_seconds']))
        result.update(run_id=f'{batch.name}/{args.project}/{name}', source_sha256=config['source_sha256'],
                      execution_driver='scripts/study/run_isolated.py',
                      execution_driver_sha256=hashlib.sha256(Path(__file__).read_bytes()).hexdigest(),
                      setup_evidence=setup.relative_to(ROOT).as_posix(),
                      targets_sha256=hashlib.sha256(targets_path.read_bytes()).hexdigest(),
                      execution_note='Disjoint run identity in isolated checkouts on the same host; wall time includes shared host load.')
        save(folder / 'evaluation/record.json', result)
        print(f'{name}: {result["status"]}, fault={result["fault_detected"]}', flush=True)


if __name__ == '__main__':
    main()
