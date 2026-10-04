#!/usr/bin/env python3
"""Verify deliverable counts, receipts, archive binding and native artifacts."""
import argparse
from collections import Counter
import csv
import json
from pathlib import Path
import re
import zipfile
import solo_batch as batch
import nightly_report as report


def verify(root, require_artifacts=True):
    snapshot = batch.read(root / 'Experiment/snapshot.json')
    summary = batch.read(root / 'Report/data/summary.json')
    if summary != snapshot['summary']:
        raise ValueError('Snapshot and report summaries differ')
    for condition, digest in summary['protocols'].items():
        evidence = root / 'Experiment/evidence' / condition / 'Experiment'
        protocol_path = evidence / 'protocol' / ('offline.json' if condition == 'offline' else 'ai.json')
        if batch.sha(protocol_path) != digest:
            raise ValueError('Packaged protocol bytes differ')
        protocol = batch.read(protocol_path)
        if condition != 'offline' and protocol['offline_protocol_sha256'] != summary['protocols']['offline']:
            raise ValueError('Packaged AI parent differs')
        for name, expected in protocol['source_sha256'].items():
            source = (evidence / 'automation/runtime' / name).resolve()
            if not source.is_relative_to(evidence.resolve()) or batch.sha(source) != expected:
                raise ValueError('Packaged source differs from protocol pins')
    with (root / 'Report/data/final_comparison.csv').open(encoding='utf-8', newline='') as stream:
        rows = list(csv.DictReader(stream))
    if (len(rows) != summary['planned_jobs'] or len({r['case'] for r in rows}) != summary['planned_bugs']
            or len({(r['case'], r['method']) for r in rows}) != len(rows)
            or dict(Counter(r['state'] for r in rows)) != summary['states']):
        raise ValueError('Comparison counts or states differ')
    recorded = [r for r in rows if r['attempted'] == 'True']
    if len(recorded) != summary['attempted_jobs'] or len(recorded) != len(snapshot['results']):
        raise ValueError('Recorded results/count differ')
    index = {(r['case'], r['method']): r for r in rows}
    for entry in snapshot['results']:
        path = (root / entry['outcome']).resolve()
        if not path.is_relative_to(root.resolve()):
            raise ValueError('Evidence escapes snapshot')
        batch.verify(path.parent)
        row = batch.read(path)
        declared = index[entry['case'], entry['method']]
        if (row['case'] != entry['case'] or row['method'] != entry['method'] or row['state'] != declared['state']
                or row['state'] != entry['state'] or row['protocol_hash'] != entry['protocol_sha256']
                or summary['protocols'][entry['condition']] != entry['protocol_sha256']
                or declared['condition'] != entry['condition'] or declared['result_path'] != entry['outcome']):
            raise ValueError('Outcome binding differs')
        for field in batch.FIELDS + report.EXTRA:
            if field not in ('condition', 'result_path'):
                expected = '' if row.get(field) is None else str(row[field])
                if declared[field] != expected:
                    raise ValueError('CSV metric differs from raw outcome: ' + field)
        if entry['state'] == 'DONE':
            archive = (root / entry['archive']).resolve()
            measurement = path.parent / (Path(row['selected_measurement']).name
                                          if row['method'].startswith('kku-') else 'measurement')
            record = batch.read(measurement / 'record.json')
            if (archive.parent != measurement.resolve() or archive.name != Path(record['suite_path']).name
                    or not archive.is_relative_to(root.resolve()) or batch.sha(archive) != entry['archive_sha256']
                    or entry['archive_sha256'] != row['suite_sha256']
                    or entry['archive_sha256'] != record['suite_sha256']):
                raise ValueError('Measured archive differs')
            if (record['status'] != 'complete' or record['fixed_validation'] != 'passed_twice'
                    or row['valid_on_fixed'] is not True or record['generator'] != entry['method']
                    or f"{record['project']}-{record['bug_id']}" != entry['case']
                    or record['test_count'] != entry['generated_test_methods']):
                raise ValueError('DONE measurement is incomplete or bound to a different suite')
            for field in ('fault_detected', 'line_covered', 'line_total', 'branch_covered', 'branch_total'):
                if row[field] != record[field]:
                    raise ValueError('Outcome metric differs from selected measurement')
            for stage in ('fixed-1', 'fixed-2', 'buggy', 'coverage'):
                observed = record['stages'][stage]
                if observed.get('timed_out') or (stage.startswith('fixed-') and observed['failure_count'] != 0):
                    raise ValueError('DONE stage is not valid')
    for prefix in ('Experiment/contexts', 'Experiment/algorithm-targets'):
        for receipt in (root / prefix).glob('*/receipt.json'):
            batch.verify(receipt.parent)
    runtime = root / 'Experiment/reproduction/runtime'
    for file in ('experiments/configs/api854-20261003/installed-active-bugs.json',
                 'docs/api854/plans/friend-layout-full-v1/cases.csv',
                 'docs/api854/plans/friend-layout-full-v1/jobs.csv'):
        if not (runtime / file).is_file():
            raise ValueError('Missing fresh-generation dependency: ' + file)
    for method, values in summary['per_method'].items():
        matching = [r for r in rows if r['method'] == method]
        if values['attempted_cases'] != sum(r['attempted'] == 'True' for r in matching):
            raise ValueError('Per-method attempted count differs')
        if values['successfully_evaluated'] != sum(r['state'] == 'DONE' for r in matching):
            raise ValueError('Per-method DONE count differs')
    if require_artifacts:
        if not (root / 'Report/SQA_Round2_Report.pdf').read_bytes().startswith(b'%PDF'):
            raise ValueError('Report PDF missing')
        with zipfile.ZipFile(root / 'Presentation/SQA_Round2.pptx') as deck:
            if deck.testzip() is not None:
                raise ValueError('PowerPoint CRC failure')
            slides = [n for n in deck.namelist() if re.fullmatch(r'ppt/slides/slide\d+\.xml', n)]
            if len(slides) != 12:
                raise ValueError('Unexpected PowerPoint slide count')
        for file in ('README.md', 'Presentation/demo.py', 'Presentation/demo-guide.md'):
            if not (root / file).is_file():
                raise ValueError('Missing deliverable ' + file)
    # Check files rather than credentials: no private registry or recognizable key is permitted.
    for path in root.rglob('*'):
        if not path.is_file():
            continue
        if '.local' in path.parts or path.name.endswith('.private.json'):
            raise ValueError('Private file packaged')
        if re.search(rb'\bsk_[A-Za-z0-9_-]{20,}\b', path.read_bytes()):
            raise ValueError('Recognizable credential in deliverable')
    return {'planned_bugs': summary['planned_bugs'], 'planned_jobs': len(rows),
            'recorded_bugs': summary['recorded_bugs'], 'recorded_outcomes': len(recorded),
            'states': summary['states'], 'receipt_integrity': 'PASS'}


if __name__ == '__main__':
    cli = argparse.ArgumentParser(description=__doc__)
    cli.add_argument('--snapshot', type=Path, required=True)
    cli.add_argument('--evidence-only', action='store_true')
    args = cli.parse_args()
    print(json.dumps(verify(args.snapshot.resolve(), not args.evidence_only), ensure_ascii=False))
