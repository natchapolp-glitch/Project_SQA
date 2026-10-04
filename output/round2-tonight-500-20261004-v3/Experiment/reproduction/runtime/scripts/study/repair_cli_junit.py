#!/usr/bin/env python3
"""Repair Cli's missing Hamcrest runtime and re-evaluate unchanged v4 suites.

Run in the same WSL/Java 11 environment as run.py. This patches a framework
dependency path, never project production sources or generated assertions.
Initial failed evaluations are moved to results/validation before re-evaluation.
"""
from __future__ import annotations

import argparse
import hashlib
import json
from pathlib import Path
import shutil
import zipfile

from evaluate import EvaluationConfig, evaluate_run, utc_now

ROOT = Path(__file__).resolve().parents[2]


def save(path, value):
    path.write_text(json.dumps(value, indent=2) + '\n', encoding='utf-8')


def repair(d4j, batch):
    d4j = d4j.resolve()
    framework = d4j.parents[2]
    build = framework / 'framework/projects/Cli/Cli.build.xml'
    jar = framework / 'framework/projects/lib/junit-4.12-hamcrest-1.3.jar'
    with zipfile.ZipFile(jar) as archive:
        if 'org/hamcrest/SelfDescribing.class' not in archive.namelist():
            raise ValueError('Bundled JUnit/Hamcrest jar lacks the missing class')
    before = build.read_bytes()
    old = b'file://${d4j.home}/framework/projects/lib/junit-4.12.jar'
    new = b'file://${d4j.home}/framework/projects/lib/junit-4.12-hamcrest-1.3.jar'
    if before.count(old) != 1:
        raise ValueError('Cli build differs from the expected unmodified dependency path')
    evidence = batch / 'environment-repair'
    evidence.mkdir(exist_ok=False)
    (evidence / 'Cli.build.original.xml').write_bytes(before)
    after = before.replace(old, new, 1)
    build.write_bytes(after)
    (evidence / 'Cli.build.patched.xml').write_bytes(after)
    metadata = {'changed_at_utc': utc_now(), 'project': 'Cli', 'changed_path': str(build),
                'reason': 'JUnit 4 test runner failed before executing tests: NoClassDefFoundError org/hamcrest/SelfDescribing',
                'before_sha256': hashlib.sha256(before).hexdigest(),
                'after_sha256': hashlib.sha256(after).hexdigest(),
                'jar_path': str(jar), 'jar_sha256': hashlib.sha256(jar.read_bytes()).hexdigest(),
                'scope': 'Framework JUnit dependency path only; same patch for fixed and buggy. Generated suites unchanged.'}
    save(evidence / 'repair.json', metadata)
    return evidence


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--d4j', required=True, type=Path)
    parser.add_argument('--batch', required=True, type=Path)
    args = parser.parse_args()
    batch = args.batch.resolve()
    if batch.parent != (ROOT / 'results/study').resolve():
        parser.error('Batch must be directly inside this repository results/study')
    repair_dir = repair(args.d4j, batch)
    config = json.loads((batch / 'config.json').read_text())
    metadata = json.loads((batch / 'Cli/setup/prepared.json').read_text())
    archive_root = ROOT / 'results/validation' / (batch.name + '-cli-classpath-failure')
    for method in ('cmaes', 'fscs-art'):
        for seed in config['seeds']:
            for budget in config['budgets']:
                name = f'{method}-s{seed}-b{budget}'
                folder = batch / 'Cli' / name
                evaluation = folder / 'evaluation'
                old = json.loads((evaluation / 'record.json').read_text())
                failure = (evaluation / 'fixed-1/failing_tests').read_text()
                if old['status'] != 'failed' or 'org/hamcrest/SelfDescribing' not in failure:
                    raise ValueError(f'Refusing to replace an unrelated evaluation: {name}')
                generated = json.loads((folder / 'generation/generation.json').read_text())
                suite = Path(generated['suite'])
                if hashlib.sha256(suite.read_bytes()).hexdigest() != generated['suite_sha256']:
                    raise ValueError('Generated suite changed')
                archived = archive_root / name / 'evaluation'
                if archived.exists():
                    raise FileExistsError(archived)
                # Resolved paths stay inside the repository; retain all evidence.
                archived.parent.mkdir(parents=True, exist_ok=True)
                shutil.move(str(evaluation), str(archived))
                result = evaluate_run(EvaluationConfig('Cli', metadata['bug_id'], method, seed, budget,
                    suite, Path(metadata['buggy_worktree']), Path(metadata['fixed_worktree']), evaluation,
                    str(args.d4j.resolve()), Path(metadata['classes_file']), generated['generation_seconds'],
                    generated['test_count'], config['command_timeout_seconds']))
                result['run_id'] = f'{batch.name}/Cli/{name}'
                result['source_sha256'] = config['source_sha256']
                result['environment_repair'] = (repair_dir / 'repair.json').relative_to(ROOT).as_posix()
                result['superseded_environment_failure'] = (archived / 'record.json').relative_to(ROOT).as_posix()
                save(evaluation / 'record.json', result)
                print(f'{name}: {result["status"]}, fault={result["fault_detected"]}', flush=True)


if __name__ == '__main__':
    main()
