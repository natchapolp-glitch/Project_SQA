#!/usr/bin/env python3
"""Prepare and track an evidence-linked Claude/Gemini KKU-only study.

Original records remain immutable. The approach field describes the comparison
group; generator/model/provider in source evidence are never relabelled.
"""
from __future__ import annotations

import argparse
import csv
from datetime import datetime, timezone
import hashlib
import json
from pathlib import Path
import shutil
import sys

ROOT = Path(__file__).resolve().parents[2]
BASE = ROOT / 'results/study/round2-v4-20260929'
COHORT = ROOT / 'results/study/kku-only-20261001'
OUT = ROOT / 'output/kku-only-20261001'
CAPTURES = ROOT / 'ai-tests/provider-captures/kku-only-20261001'
APPROACHES = ('cmaes', 'fscs-art', 'kku-claude', 'kku-gemini')


def load(path):
    return json.loads(path.read_text(encoding='utf-8'))


def sha(path):
    return hashlib.sha256(path.read_bytes()).hexdigest()


def relative(path):
    return path.relative_to(ROOT).as_posix()


def save(path, value):
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(value, ensure_ascii=False, indent=2,
                               allow_nan=False) + '\n', encoding='utf-8')


def csv_write(path, rows):
    path.parent.mkdir(parents=True, exist_ok=True)
    if not rows:
        path.write_text('project,bug_id,approach,run_index,budget,status\n', encoding='utf-8')
        return
    fields = list(dict.fromkeys(k for row in rows for k in row))
    with path.open('w', encoding='utf-8-sig', newline='') as stream:
        writer = csv.DictWriter(stream, fieldnames=fields)
        writer.writeheader()
        writer.writerows(rows)


def model_family(model):
    model = str(model).lower()
    if model.startswith('claude-'):
        return 'claude'
    if model.startswith('gemini-'):
        return 'gemini'
    return None


def prepare():
    if (COHORT / 'protocol.json').exists():
        raise ValueError('Prepared cohort already exists; use status to refresh it')
    config = load(BASE / 'config.json')
    for name, expected in config['source_sha256'].items():
        if sha(ROOT / name) != expected:
            raise ValueError('Frozen baseline source changed: ' + name)
    projects = sorted(p.name for p in BASE.iterdir() if (p / 'ai-context').is_dir())
    for family in ('claude', 'gemini'):
        batch = COHORT / family
        save(batch / 'config.json', config)
        for project in projects:
            shutil.copytree(BASE / project / 'ai-context', batch / project / 'ai-context')
    protocol = {
        'schema_version': 1,
        'created_at_utc': datetime.now(timezone.utc).isoformat(),
        'user_instruction': 'Claude and Gemini only, through https://gen.ai.kku.ac.th/chat',
        'baseline': relative(BASE), 'approaches': list(APPROACHES),
        'projects': projects, 'run_indices': [101, 102, 103], 'budget': 30,
        'scope': 'One active bug/project; target modified classes, not all bugs or full-project coverage.',
        'prompt_policy': 'Exact original prepared prompts only. No evaluation logs sent to AI.',
        'new_selection_policy': 'Explicit Claude or Gemini agent; Auto Router prohibited for new captures.',
        'reuse_policy': 'Reference audited completed baseline records with KKU provenance and matching Claude/Gemini capture/generation labels. Preserve original evidence and model labels.',
        'model_comparison_limit': 'Family-level comparison; historical and current model versions may differ. Report by exact label and distinguish reused/new and repair effort.',
        'processing_policy': 'Source-order 30-method cap, KKU UI suffix cleanup, up to two fixed-only pruning passes. Any further compatibility recovery needs a separate disclosed version.',
        'validation': ['fixed-1', 'fixed-2', 'buggy', 'coverage'],
        'missing_metrics': 'null; never replace missing/failed measurements with zero',
        'ui_time_scope': 'Submit-to-observed-completion upper bound; not model compute time.',
        'algorithm_budget_scope': '30 proposed inputs; AI budget 30 test methods, not equal computation.',
        'frozen_baseline_sources': config['source_sha256'],
        'tracking_script_sha256': sha(Path(__file__)),
    }
    save(COHORT / 'protocol.json', protocol)


def status():
    protocol = load(COHORT / 'protocol.json')
    audit = load(OUT / 'baseline-evidence-audit.json')
    passed = {r['path']: r['sha256'] for r in audit['records'] if r['passed']}
    sources = {}
    excluded = []
    secondary_new = []
    for path in sorted(BASE.glob('*/*/evaluation/record.json')):
        record = load(path)
        approach = record['generator']
        model = None
        capture = None
        if approach == 'intellisphere':
            capture = ROOT / f"ai-tests/provider-captures/intellisphere/{record['project']}-{record['bug_id']}/s{record['seed']}-i1"
            operator = load(capture / 'operator-metadata.json')
            model = operator['model']
            family = model_family(model)
            generation = load(ROOT / record['ai_evidence_metadata'])
            if not family or generation['model'] != model:
                excluded.append({'record': relative(path), 'reason': 'outside allowed family or mismatched model provenance', 'model': model})
                continue
            approach = 'kku-' + family
        elif approach == 'claude':
            excluded.append({'record': relative(path), 'reason': 'Claude direct, outside KKU-only scope'})
            continue
        if record['status'] != 'complete' or passed.get(relative(path)) != sha(path):
            continue
        key = (record['project'], str(record['bug_id']), approach, record['seed'])
        sources[key] = (path, record, capture, model, 'reused-audited-baseline')
    for family in ('claude', 'gemini'):
        new_audit_path = OUT / f'{family}-evidence-audit.json'
        audited_new = ({r['path']: r['sha256'] for r in load(new_audit_path)['records'] if r['passed']}
                       if new_audit_path.exists() else {})
        for path in sorted((COHORT / family).glob('*/*/evaluation/record.json')):
            record = load(path)
            if record['status'] == 'complete' and audited_new.get(relative(path)) != sha(path):
                record = {**record, 'status': 'complete-awaiting-audit'}
            capture = CAPTURES / family / f"{record['project']}-{record['bug_id']}" / f"s{record['seed']}-i1"
            operator = load(capture / 'operator-metadata.json')
            if model_family(operator['model']) != family or operator.get('selected_agent', '').lower() != family:
                raise ValueError('New capture family/selection mismatch: ' + relative(capture))
            key = (record['project'], str(record['bug_id']), 'kku-' + family, record['seed'])
            if key in sources:
                secondary_new.append({'record':relative(path), 'record_sha256':sha(path),
                                      'reason':'Fresh capture duplicates an already eligible reused project/bug/family/index identity. Retain original primary; this is secondary validation, never an added independent run.',
                                      'primary_record':relative(sources[key][0]), 'project':record['project'],
                                      'approach':'kku-'+family, 'run_index':record['seed']})
                continue
            sources[key] = (path, record, capture, operator['model'], 'new-kku-capture')
    rows, measured = [], []
    for project in protocol['projects']:
        bug = str(load(BASE / project / 'ai-context/context-manifest.json')['bug_id'])
        for approach in APPROACHES:
            for index in protocol['run_indices']:
                key = (project, bug, approach, index)
                row = {'project': project, 'bug_id': bug, 'approach': approach, 'run_index': index, 'budget': 30}
                item = sources.get(key)
                if item:
                    path, record, capture, model, origin = item
                    row.update(status=record['status'], origin=origin, model=model,
                               record_path=relative(path), record_sha256=sha(path),
                               capture_path=relative(capture) if capture else None)
                    for metric in ('test_count', 'compile_status', 'fault_detected', 'line_covered', 'line_total',
                                   'branch_covered', 'branch_total', 'generation_seconds', 'duration_seconds', 'workflow_seconds'):
                        row[metric] = record.get(metric)
                    measured.append({**record, 'generator': approach, 'valid': record['status'] != 'complete-awaiting-audit'})
                else:
                    capture = CAPTURES / approach.removeprefix('kku-') / f'{project}-{bug}' / f's{index}-i1'
                    operator_path = capture / 'operator-metadata.json'
                    capture_status = (load(operator_path).get('capture_status', 'captured-awaiting-evaluation')
                                      if operator_path.exists() else 'requested-awaiting-response'
                                      if (capture / 'request-start.json').exists() else 'missing')
                    row.update(status=capture_status, capture_path=relative(capture) if capture_status != 'missing' else None)
                rows.append(row)
    sys.path.insert(0, str(ROOT / 'scripts/reporting'))
    from aggregate import summarize
    methods = [{'approach': a, 'expected_runs': 51, **summarize([r for r in measured if r['generator'] == a])} for a in APPROACHES]
    result = {'generated_at_utc': datetime.now(timezone.utc).isoformat(), 'protocol': relative(COHORT / 'protocol.json'),
              'expected_runs': len(rows), 'completed_runs': sum(r['status'] == 'complete' for r in rows),
              'method_summary': methods, 'rows': rows, 'excluded_baseline': excluded,
              'secondary_new_records':secondary_new,
              'baseline_audit': relative(OUT / 'baseline-evidence-audit.json'),
              'tracking_script_sha256': sha(Path(__file__)),
              'completion_claim': 'incomplete' if any(r['status'] != 'complete' for r in rows) else 'requires-final-audit'}
    save(OUT / 'summary.json', result)
    csv_write(OUT / 'study-manifest.csv', rows)
    missing = [r for r in rows if r['status'] != 'complete']
    csv_write(OUT / 'pending-runs.csv', missing)
    first = [r for r in missing if not any(x['project'] == r['project'] and x['approach'] == r['approach'] and x['status'] == 'complete' for x in rows)]
    ordered = sorted(first, key=lambda r: (r['run_index'], r['project'] not in ('Lang', 'Math', 'Time', 'Closure'), r['project'], r['approach']))
    remaining = [r for r in missing if r not in ordered]
    csv_write(OUT / 'capture-queue.csv', ordered + sorted(remaining, key=lambda r: (r['run_index'], r['project'], r['approach'])))
    print(json.dumps({'expected': len(rows), 'completed': result['completed_runs'], 'pending': len(missing),
                      'methods': [{'approach':m['approach'], 'complete':m['completed_runs']} for m in methods]}))


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('action', choices=('prepare', 'status'))
    args = parser.parse_args()
    if args.action == 'prepare':
        prepare()
    status()
