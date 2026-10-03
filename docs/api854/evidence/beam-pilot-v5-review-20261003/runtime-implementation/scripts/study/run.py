#!/usr/bin/env python3
"""Inventory and run a declared all-project Defects4J study in Linux/WSL."""
from __future__ import annotations

import argparse
import csv
import hashlib
from datetime import datetime, timezone
import json
import os
from pathlib import Path
import re
import shutil
import subprocess
import sys

from evaluate import EvaluationConfig, evaluate_run, run_command, validate_worktree
from generate import generate_suite

ROOT = Path(__file__).resolve().parents[2]


def save(path, data):
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(data, indent=2) + '\n', encoding='utf-8')


def query(d4j, *args):
    result = subprocess.run([str(d4j), *args], text=True, capture_output=True,
                            timeout=120, env={**os.environ, 'TZ': 'America/Los_Angeles'})
    if result.returncode:
        raise RuntimeError(result.stderr[-2000:])
    return result.stdout.strip()


def inventory(d4j, config, destination):
    projects = query(d4j, 'pids').splitlines()
    if not projects or any(not re.fullmatch(r'[A-Za-z]+', p) for p in projects):
        raise ValueError('Unexpected project inventory')
    entries = []
    for project in sorted(projects):
        bugs = sorted(int(n) for n in query(d4j, 'bids', '-p', project).splitlines())
        if not bugs:
            raise ValueError(f'No active bugs for {project}')
        entries.append({'project': project, 'bug_id': bugs[0], 'active_bug_count': len(bugs),
                        'selection': config['selection']})
    save(destination / 'study-targets.json', {'projects': entries, 'configuration': config})
    fields = ['project', 'bug_id', 'generator', 'seed', 'budget', 'status']
    with (destination / 'study-manifest.csv').open('w', newline='', encoding='utf-8') as stream:
        writer = csv.DictWriter(stream, fieldnames=fields)
        writer.writeheader()
        for entry in entries:
            for method in config['generators']:
                for seed in config['seeds']:
                    for budget in config['budgets']:
                        writer.writerow(dict(project=entry['project'], bug_id=entry['bug_id'],
                                             generator=method, seed=seed, budget=budget, status='planned'))
    return entries


def stage(command, cwd, output, timeout):
    result = run_command([str(s) for s in command], cwd, output, timeout)
    if result['exit_code'] != 0 or result['timed_out']:
        raise RuntimeError(f'Command failed; see {output / "command.log"}')
    return (output / 'command.log').read_text(encoding='utf-8', errors='replace')


def shared_targets(fixed_targets, buggy_targets):
    fields = ('class', 'constructor_types', 'method', 'parameter_types')
    identity = lambda target: tuple(target[field] for field in fields)
    buggy_signatures = {identity(target) for target in buggy_targets}
    return ([target for target in fixed_targets if identity(target) in buggy_signatures],
            [target for target in fixed_targets if identity(target) not in buggy_signatures])


def prepare(d4j, entry, worktrees, output, timeout):
    project, bug = entry['project'], entry['bug_id']
    output.mkdir(parents=True, exist_ok=False)
    trees = {}
    for revision in ('f', 'b'):
        tree = (worktrees / project / str(bug) / revision).resolve()
        tree.parent.mkdir(parents=True, exist_ok=True)
        if tree.exists():
            validate_worktree(tree, project, f'{bug}{revision}')
        else:
            stage([d4j, 'checkout', '-p', project, '-v', f'{bug}{revision}', '-w', tree],
                  ROOT, output / f'checkout-{revision}', timeout)
        stage([d4j, 'compile', '-w', tree], ROOT, output / f'compile-{revision}', timeout)
        trees[revision] = tree
    fixed = trees['f']
    properties = {}
    for prop in ('classes.modified', 'cp.test', 'dir.src.classes', 'dir.bin.classes', 'tests.trigger'):
        path = output / (prop + '.txt')
        stage([d4j, 'export', '-w', fixed, '-p', prop, '-o', path], ROOT,
              output / ('export-' + prop), timeout)
        properties[prop] = path.read_text(encoding='utf-8').strip()
    # Trigger names validate dataset eligibility only; they never enter input generation.
    trigger = properties['tests.trigger'].splitlines()[0]
    for revision in ('f', 'b'):
        log = stage([d4j, 'test', '-w', trees[revision], '-t', trigger], ROOT,
                    output / f'baseline-{revision}', timeout)
        match = re.search(r'^Failing tests:\s*(\d+)\s*$', log, re.MULTILINE)
        if not match or (revision == 'f' and int(match[1]) != 0) or (revision == 'b' and int(match[1]) == 0):
            raise RuntimeError('Dataset triggering test is not reproducible; see baseline logs')
    helper = output / 'probe-classes'
    helper.mkdir()
    stage(['javac', '-source', '7', '-target', '7', '-d', helper, ROOT / 'algorithms/java/SqaProbe.java'],
          ROOT, output / 'compile-probe', timeout)
    classpath = properties['cp.test'] + os.pathsep + str(helper)
    buggy_properties = {}
    for prop in ('cp.test', 'dir.bin.classes'):
        path = output / ('buggy.' + prop + '.txt')
        stage([d4j, 'export', '-w', trees['b'], '-p', prop, '-o', path], ROOT,
              output / ('export-buggy-' + prop), timeout)
        buggy_properties[prop] = path.read_text(encoding='utf-8').strip()
    fixture_sets = []
    for tree, binary_dir in ((fixed, properties['dir.bin.classes']),
                             (trees['b'], buggy_properties['dir.bin.classes'])):
        binary = tree / binary_dir
        fixture_sets.append({str(p.relative_to(binary).with_suffix('')).replace(os.sep, '.')
                             for p in binary.rglob('*.class') if '$' not in p.name})
    fixture_classes = sorted(fixture_sets[0] & fixture_sets[1])
    fixtures = output / 'fixture-classes.txt'
    fixtures.write_text('\n'.join(fixture_classes) + '\n', encoding='utf-8')
    discoveries = {}
    for revision, cp in (('fixed', classpath),
                         ('buggy', buggy_properties['cp.test'] + os.pathsep + str(helper))):
        log = stage(['java', '-Xmx256m', '-cp', cp, 'SqaProbe', 'discover', '--fixtures', fixtures,
                     *properties['classes.modified'].splitlines()],
                    ROOT, output / ('discover-' + revision), timeout)
        candidates = [line for line in log.splitlines() if line.startswith('{"targets":')]
        if len(candidates) != 1:
            raise RuntimeError('Probe discovery did not produce exactly one target inventory')
        discoveries[revision] = json.loads(candidates[0])
        save(output / ('targets.' + revision + '.json'), discoveries[revision])
    eligible, excluded = shared_targets(discoveries['fixed']['targets'], discoveries['buggy']['targets'])
    discovery = {'targets': eligible, 'excluded_fixed_only': excluded,
                 'errors': {name: item['errors'] for name, item in discoveries.items()},
                 'eligibility': 'shared declaration signatures only; no buggy outcomes inspected'}
    save(output / 'targets.json', discovery)
    if not discovery['targets']:
        raise RuntimeError('No shared supported declaration signatures; a project-specific adapter is required')
    metadata = {**entry, 'fixed_worktree': str(fixed), 'buggy_worktree': str(trees['b']),
                'classpath': classpath, 'targets_file': str(output / 'targets.json'),
                'classes_file': str(output / 'classes.modified.txt'), 'status': 'prepared'}
    save(output / 'prepared.json', metadata)
    return metadata, discovery['targets']


def blocked(entry, method, seed, budget, output, error):
    output.mkdir(parents=True, exist_ok=False)
    record = {**entry, 'run_id': output.name, 'generator': method, 'seed': seed,
              'budget': budget, 'status': 'blocked', 'compile_status': 'unknown',
              'fault_detected': None, 'line_covered': None, 'line_total': None,
              'branch_covered': None, 'branch_total': None, 'test_count': None,
              'duration_seconds': None, 'artifact_path': str(output), 'error': str(error)}
    save(output / 'record.json', record)


def main():
    cli = argparse.ArgumentParser(description=__doc__)
    cli.add_argument('action', choices=['inventory', 'run'])
    cli.add_argument('--d4j', type=Path, required=True)
    cli.add_argument('--config', type=Path, default=ROOT / 'experiments/configs/round2.json')
    cli.add_argument('--worktrees', type=Path, default=ROOT / 'worktrees/study')
    cli.add_argument('--projects', nargs='*', help='Optional explicitly limited run; manifest still lists all projects')
    cli.add_argument('--run-id', default=datetime.now(timezone.utc).strftime('%Y%m%dT%H%M%SZ'))
    cli.add_argument('--resume', action='store_true')
    cli.add_argument('--inventory-file', type=Path, help='Reuse a pre-generated inventory for disjoint project workers')
    args = cli.parse_args()
    if not re.fullmatch(r'[A-Za-z0-9][A-Za-z0-9._-]{0,63}', args.run_id):
        cli.error('Unsafe run ID')
    d4j = args.d4j.resolve()
    config = json.loads(args.config.read_text(encoding='utf-8'))
    if args.inventory_file:
        saved_inventory = json.loads(args.inventory_file.read_text(encoding='utf-8'))
        if saved_inventory['configuration'] != config:
            raise ValueError('Inventory configuration differs from the requested study')
        entries = saved_inventory['projects']
    else:
        entries = inventory(d4j, config, ROOT / 'dataset')
    if args.action == 'inventory':
        print(json.dumps(entries, indent=2))
        return 0
    if args.projects and not set(args.projects) <= {e['project'] for e in entries}:
        cli.error('Unknown requested project')
    batch = ROOT / 'results/study' / args.run_id
    batch.mkdir(parents=True, exist_ok=args.resume)
    source_files = ['scripts/study/run.py', 'scripts/study/generate.py', 'scripts/study/evaluate.py',
                    'algorithms/java/SqaProbe.java', 'scripts/ai/evidence.py', 'prompts/round2-unit-test.md']
    source_files += [path.relative_to(ROOT).as_posix() for path in sorted((ROOT / 'algorithms/python/atcg').glob('*.py'))]
    source_hashes = {name: hashlib.sha256((ROOT / name).read_bytes()).hexdigest() for name in source_files}
    snapshot = {**config, 'source_sha256': source_hashes}
    if args.resume and (batch / 'config.json').exists():
        previous = json.loads((batch / 'config.json').read_text())
        if previous != snapshot:
            raise ValueError('Source/config changed; start a new run ID instead of mixing implementations')
    else:
        save(batch / 'config.json', snapshot)
    batch_manifest = batch / 'study-manifest.csv'
    if args.resume and batch_manifest.exists():
        if batch_manifest.read_bytes() != (ROOT / 'dataset/study-manifest.csv').read_bytes():
            raise ValueError('Manifest changed; use a new run ID')
    else:
        shutil.copy2(ROOT / 'dataset/study-manifest.csv', batch_manifest)
    for entry in entries:
        project = entry['project']
        if args.projects and project not in args.projects:
            continue
        project_out = batch / project
        setup = project_out / 'setup'
        print(f'Preparing {project}-{entry["bug_id"]}', flush=True)
        setup_error = None
        try:
            if args.resume and (setup / 'prepared.json').is_file():
                metadata = json.loads((setup / 'prepared.json').read_text())
                targets = json.loads((setup / 'targets.json').read_text())['targets']
            else:
                metadata, targets = prepare(d4j, entry, args.worktrees.resolve(), setup, config['command_timeout_seconds'])
        except Exception as error:
            setup_error = error
            print(f'  blocked: {error}', flush=True)
        if setup_error is None:
            prompt_out = project_out / 'ai-context'
            if not prompt_out.exists():
                try:
                    stage([sys.executable, ROOT / 'scripts/ai/evidence.py', 'prepare', '--project', project,
                           '--bug-id', entry['bug_id'], '--fixed-worktree', metadata['fixed_worktree'],
                           '--classes-file', metadata['classes_file'], '--targets-file', metadata['targets_file'],
                           '--output', prompt_out],
                          ROOT, project_out / 'prepare-ai-context', 120)
                except Exception as error:
                    print(f'  AI context error (algorithm runs continue): {error}', flush=True)
        for method in ('cmaes', 'fscs-art'):
            for seed in config['seeds']:
                for budget in config['budgets']:
                    name = f'{method}-s{seed}-b{budget}'
                    output = project_out / name / 'evaluation'
                    if args.resume and (output / 'record.json').exists():
                        continue
                    if setup_error:
                        blocked(entry, method, seed, budget, output, setup_error)
                        continue
                    try:
                        generated = generate_suite(project, entry['bug_id'], method, budget, seed,
                                                   targets, metadata['classpath'], project_out / name / 'generation',
                                                   config['observation_timeout_seconds'])
                        if not generated['test_count']:
                            raise RuntimeError('No stable generated tests; see observations.json')
                        result = evaluate_run(EvaluationConfig(project, entry['bug_id'], method, seed, budget,
                                              Path(generated['suite']), Path(metadata['buggy_worktree']),
                                              Path(metadata['fixed_worktree']), output, str(d4j),
                                              Path(metadata['classes_file']), generated['generation_seconds'],
                                              generated['test_count'], config['command_timeout_seconds']))
                        result['source_sha256'] = source_hashes
                        result['run_id'] = f'{args.run_id}/{project}/{name}'
                        save(output / 'record.json', result)
                        print(f'  {name}: {result["status"]}, fault={result["fault_detected"]}', flush=True)
                    except Exception as error:
                        if not output.exists():
                            blocked(entry, method, seed, budget, output, error)
                        print(f'  {name}: failed {error}', flush=True)
    return 0


if __name__ == '__main__':
    raise SystemExit(main())
