#!/usr/bin/env python3
"""Check KKU-only selection, raw captures, failure evidence and manifest references."""
import hashlib
import json
from pathlib import Path
from datetime import datetime, timezone

ROOT = Path(__file__).resolve().parents[2]
OUT = ROOT / 'output/kku-only-20261001'

def sha(path):
    return hashlib.sha256(path.read_bytes()).hexdigest() if path.is_file() else None

def main():
    summary = json.loads((OUT / 'summary.json').read_text(encoding='utf-8'))
    issues, checked = [], []
    recorder_hashes = {sha(p) for p in (ROOT / 'scripts/study').glob('record_kku_capture_failure*.py')}
    for secondary in summary.get('secondary_new_records',[]):
        if sha(ROOT / secondary['record']) != secondary['record_sha256']:
            issues.append({'issue':'secondary record hash mismatch','path':secondary['record']})
        if not any(row.get('record_path')==secondary['primary_record'] for row in summary['rows']):
            issues.append({'issue':'secondary identity lacks retained original primary'})
        if any(row.get('record_path')==secondary['record'] for row in summary['rows']):
            issues.append({'issue':'secondary duplicate counted in primary manifest'})
    incident_path = ROOT / 'results/validation/operator-capture-routing-20261001/incident.json'
    if incident_path.exists():
        incident = json.loads(incident_path.read_text())
        cap = ROOT / 'ai-tests/provider-captures/kku-only-20261001/gemini/Lang-1/s102-i1'
        if sha(cap / 'response.md') != incident['lang_response_sha256_verified'] or sha(cap / 'browser-recovery-20261001/response.md') != incident['lang_response_sha256_verified']:
            issues.append({'issue':'recovered Lang raw response hash mismatch'})
        for item in incident['preserved_misrouted_files']:
            if sha(incident_path.parent / item['name']) != item['sha256']:
                issues.append({'issue':'operator incident history hash mismatch', 'file':item['name']})
    for row in summary['rows']:
        errors = []
        path = ROOT / row['record_path'] if row.get('record_path') else None
        if path:
            if sha(path) != row['record_sha256']:
                errors.append('manifest record hash mismatch')
            record = json.loads(path.read_text(encoding='utf-8'))
            if row['origin'] == 'new-kku-capture':
                capture = ROOT / row['capture_path']
                operator = json.loads((capture / 'operator-metadata.json').read_text(encoding='utf-8'))
                family = row['approach'].removeprefix('kku-')
                if operator['selected_agent'].lower() != family or not operator['model'].lower().startswith(family + '-'):
                    errors.append('explicit agent/model family mismatch')
                if not operator['session_url'].startswith('https://gen.ai.kku.ac.th/chat'):
                    errors.append('provider destination mismatch')
                if operator.get('manual_edits'):
                    errors.append('raw provider edits declared')
                if not (capture / 'provider-screen.png').is_file():
                    errors.append('missing capture screenshot')
                if record.get('ai_evidence_metadata'):
                    metadata_path = ROOT / record['ai_evidence_metadata']
                    generation = json.loads(metadata_path.read_text())
                    if sha(capture / 'response.md') != generation['response_sha256'] or sha(metadata_path.parent / 'response.txt') != generation['response_sha256']:
                        errors.append('original/ingested response hash mismatch')
                    for name, expected in record.get('ai_processing_source_sha256',{}).items():
                        if sha(ROOT / name) != expected:
                            errors.append('processing source changed: ' + name)
                if record.get('failure_recorder_sha256'):
                    if record['failure_recorder_sha256'] not in recorder_hashes:
                        errors.append('failure recorder version unavailable')
                    for name, field in [('response.md', 'raw_response_sha256'), ('operator-metadata.json', 'capture_metadata_sha256')]:
                        if sha(capture / name) != record[field]:
                            errors.append(field + ' mismatch')
                    for field in ['fault_detected', 'line_covered', 'line_total', 'branch_covered', 'branch_total', 'duration_seconds']:
                        if record[field] is not None:
                            errors.append('failed provider output has inferred metric: ' + field)
                    if record['test_count'] != 0 or record['compile_status'] != 'not_run':
                        errors.append('failed provider output has invented test execution')
        if errors:
            issues.extend({'project':row['project'], 'approach':row['approach'], 'index':row['run_index'], 'issue':e} for e in errors)
        if path:
            checked.append({'path':row['record_path'], 'sha256':sha(path), 'passed':not errors})
    result = {'audited_at_utc':datetime.now(timezone.utc).isoformat(), 'manifest_sha256':sha(OUT / 'study-manifest.csv'),
              'records_checked':len(checked), 'issues':issues, 'records':checked,
              'scope':'Manifest hashes and new provider selection/failure evidence. Completed execution requires separate full evidence audits.'}
    (OUT / 'provenance-audit-current.json').write_text(json.dumps(result, indent=2) + '\n', encoding='utf-8')
    print(json.dumps({'checked':len(checked), 'issues':len(issues)}))
    return bool(issues)

if __name__ == '__main__':
    raise SystemExit(main())
