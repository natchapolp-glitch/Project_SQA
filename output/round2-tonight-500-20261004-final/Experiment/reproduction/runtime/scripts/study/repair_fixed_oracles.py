#!/usr/bin/env python3
"""Prune assertions rejected by fixed-only validation, preserving all attempts.

Run only after workers finish using the affected project checkouts. The same
rule applies to both algorithms: remove only generated methods reported failing
on a fixed revision. No buggy evidence is read to select or change assertions.
The 30 proposed inputs and search ledger remain unchanged. This is an explicitly
reported follow-up validation step, not an unmodified first-attempt result.
"""
from __future__ import annotations
import argparse
import hashlib
import io
import json
from pathlib import Path
import re
import shutil
import tarfile
import time

from evaluate import EvaluationConfig, evaluate_run, parse_test_evidence, utc_now
from generate import suite_source

ROOT = Path(__file__).resolve().parents[2]


def save(path, data):
    path.write_text(json.dumps(data, indent=2) + '\n', encoding='utf-8')


def repair(folder, batch, d4j, attempt):
    evaluation = folder / 'evaluation'
    record = json.loads((evaluation / 'record.json').read_text())
    if record['status'] != 'invalid' or not record.get('failed_stage', '').startswith('fixed'):
        return False
    stage = record['failed_stage']
    parsed = parse_test_evidence((evaluation / stage / 'command.log').read_text(),
                                (evaluation / stage / 'failing_tests').read_text())
    ids = []
    for name in parsed['failing_tests']:
        match = re.fullmatch(r'GeneratedStudyTest::generated(\d+)', name)
        if not match:
            raise ValueError('Refusing to prune a non-generated-method failure')
        ids.append(int(match[1]))
    generated_dir = folder / 'generation'
    generated = json.loads((generated_dir / 'generation.json').read_text())
    rows = json.loads((generated_dir / 'observations.json').read_text())
    if not ids or any(not any(row['case_id'] == case and row['retained'] for row in rows) for case in ids):
        raise ValueError('Fixed failure does not map to a retained generated case')
    count = sum(row['retained'] and row['case_id'] not in ids for row in rows)
    if count == 0:
        raise ValueError('Pruning would leave no tests; preserve the invalid result')
    archive = ROOT / 'results/validation' / (batch.name + '-fixed-oracle-repair') / record['project'] / folder.name / f'attempt-{attempt}'
    if archive.exists():
        raise FileExistsError(archive)
    archive.mkdir(parents=True)
    started = time.monotonic()
    shutil.move(str(generated_dir), str(archive / 'generation'))
    shutil.move(str(evaluation), str(archive / 'evaluation'))
    generated_dir.mkdir()
    for row in rows:
        if row['case_id'] in ids:
            row['retained'] = False
            row['exclusion_reason'] = 'assertion failed fixed JUnit validation'
    source = suite_source(rows).encode()
    save(generated_dir / 'observations.json', rows)
    (generated_dir / 'GeneratedStudyTest.java').write_bytes(source)
    shutil.copy2(archive / 'generation/SqaProbe.java', generated_dir / 'SqaProbe.java')
    suite = generated_dir / Path(generated['suite']).name
    with tarfile.open(suite, 'w:bz2') as tar:
        info = tarfile.TarInfo('GeneratedStudyTest.java')
        info.size, info.mode, info.mtime = len(source), 0o644, 0
        tar.addfile(info, io.BytesIO(source))
    repaired_at = utc_now()
    packaging_seconds = time.monotonic() - started
    history = record.get('fixed_oracle_repair_history', []) + [{
        'attempt': attempt, 'repaired_at_utc': repaired_at, 'removed_case_ids': ids,
        'fixed_stage_used': stage, 'previous_test_count': generated['test_count'],
        'remaining_test_count': count, 'initial_evaluation_seconds': record['duration_seconds'],
        'packaging_seconds': packaging_seconds,
        'previous_record': (archive / 'evaluation/record.json').relative_to(ROOT).as_posix(),
        'previous_generation': (archive / 'generation/generation.json').relative_to(ROOT).as_posix(),
        'previous_suite_sha256': generated['suite_sha256']}]
    generated.update(test_count=count, suite=str(suite), suite_sha256=hashlib.sha256(suite.read_bytes()).hexdigest(),
                     fixed_oracle_repair_history=history, postprocessing='fixed-validation-pruning-v1')
    save(generated_dir / 'generation.json', generated)
    config = json.loads((batch / 'config.json').read_text())
    prepared = json.loads((batch / record['project'] / 'setup/prepared.json').read_text())
    result = evaluate_run(EvaluationConfig(record['project'], int(record['bug_id']), record['generator'],
        record['seed'], record['budget'], suite, Path(prepared['buggy_worktree']), Path(prepared['fixed_worktree']),
        evaluation, str(d4j.resolve()), Path(prepared['classes_file']), generated['generation_seconds'],
        count, config['command_timeout_seconds']))
    result.update(run_id=record['run_id'], source_sha256=config['source_sha256'],
                  postprocessing='fixed-validation-pruning-v1', fixed_oracle_repair_history=history,
                  repair_source_sha256=hashlib.sha256(Path(__file__).read_bytes()).hexdigest())
    result['workflow_seconds'] = result['total_seconds'] + sum(
        h['initial_evaluation_seconds'] + h['packaging_seconds'] for h in history)
    save(evaluation / 'record.json', result)
    print(f'{record["project"]}/{folder.name}: {result["status"]}; removed {ids}; tests={count}', flush=True)
    return True


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--batch', required=True, type=Path)
    parser.add_argument('--d4j', required=True, type=Path)
    parser.add_argument('--projects', nargs='+', required=True)
    args = parser.parse_args()
    batch = args.batch.resolve()
    if batch.parent != (ROOT / 'results/study').resolve():
        parser.error('Batch must be directly inside this repository results/study')
    for project in args.projects:
        if not re.fullmatch(r'[A-Za-z]+', project):
            parser.error('Invalid project name')
        for path in sorted((batch / project).glob('*/evaluation/record.json')):
            for attempt in range(1, 4):
                if not repair(path.parent.parent, batch, args.d4j, attempt):
                    break


if __name__ == '__main__':
    main()
