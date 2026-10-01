#!/usr/bin/env python3
"""KKU-only audit fork: original audit checks plus explicit capture path support.
Baseline audit_evidence.py remains unchanged; CLI requires a family batch.
"""
from __future__ import annotations

import argparse
from collections import Counter
import hashlib
import json
from pathlib import Path
import re
import sys
import tarfile
import xml.etree.ElementTree as ET

ROOT = Path(__file__).resolve().parents[2]
sys.path.insert(0, str(ROOT / 'scripts/study'))
from evaluate import parse_test_evidence, parse_coverage_csv, validate_archive, utc_now


def digest(path):
    return hashlib.sha256(path.read_bytes()).hexdigest()


def check_record(record_path, frozen):
    record = json.loads(record_path.read_text())
    evaluation = record_path.parent
    errors = []
    if record['source_sha256'] != frozen:
        errors.append('record_source_hashes_differ_from_batch')
    for stage in ('fixed-1', 'fixed-2', 'buggy'):
        folder = evaluation / stage
        command = json.loads((folder / 'command.json').read_text())
        if command['exit_code'] != 0 or command['timed_out']:
            errors.append(stage + '_command_not_successful')
        result = parse_test_evidence((folder / 'command.log').read_text(),
                                     (folder / 'failing_tests').read_text())
        if result['failure_count'] != record['stages'][stage]['failure_count']:
            errors.append(stage + '_failure_count_mismatch')
        if stage.startswith('fixed') and result['failure_count']:
            errors.append(stage + '_did_not_pass')
        if stage == 'buggy':
            if bool(result['failure_count']) != record['fault_detected']:
                errors.append('fault_detection_mismatch')
            if result['failing_tests'] != record['triggering_tests']:
                errors.append('triggering_tests_mismatch')
    coverage = evaluation / 'coverage'
    command = json.loads((coverage / 'command.json').read_text())
    if command['exit_code'] != 0 or command['timed_out'] or (coverage / 'failing_tests').read_text().strip():
        errors.append('coverage_not_successful')
    counters = parse_coverage_csv((coverage / 'summary.csv').read_text())
    if any(record[k] != value for k, value in counters.items()):
        errors.append('coverage_counters_mismatch')
    ET.parse(coverage / 'coverage.xml')
    suite = evaluation / Path(record['suite_path']).name
    validate_archive(suite)
    if digest(suite) != record['suite_sha256']:
        errors.append('evaluated_archive_hash_mismatch')
    generation = evaluation.parent / 'generation'
    if record['generator'] in ('cmaes', 'fscs-art'):
        generated = json.loads((generation / 'generation.json').read_text())
        original = generation / Path(generated['suite']).name
        observations = json.loads((generation / 'observations.json').read_text())
        if digest(original) != record['suite_sha256'] or generated['suite_sha256'] != record['suite_sha256']:
            errors.append('generated_archive_differs_from_evaluated_archive')
        retained = [row for row in observations if row['retained']]
        if len(retained) != record['test_count'] or generated['test_count'] != record['test_count']:
            errors.append('retained_test_count_mismatch')
        if len(observations) != record['budget'] or generated['proposed_inputs'] != record['budget']:
            errors.append('input_budget_mismatch')
        if any(row['fixed_first'].get('status') != 'ok' or
               row['fixed_first'] != row['fixed_second'] for row in retained):
            errors.append('retained_fixed_observation_not_stable')
        if abs(generated['generation_seconds'] - record['generation_seconds']) > 1e-6:
            errors.append('generation_time_mismatch')
    elif record['generator'] in ('claude', 'intellisphere'):
        metadata_file = ROOT / record['ai_evidence_metadata']
        metadata = json.loads(metadata_file.read_text())
        if metadata['tool'] != record['generator'] or metadata['archive_run_seed'] != record['seed']:
            errors.append('ai_provider_identity_mismatch')
        if metadata['project'] != record['project'] or metadata['bug_id'] != record['bug_id']:
            errors.append('ai_project_identity_mismatch')
        if metadata['provenance_status'] != 'recorded' or not metadata['model'].strip():
            errors.append('ai_provider_provenance_incomplete')
        for filename, key in (('prompt.md', 'prompt_sha256'), ('response.txt', 'response_sha256'),
                              ('context-manifest.json', 'context_manifest_sha256')):
            if digest(metadata_file.parent / filename) != metadata[key]:
                errors.append('ai_' + key + '_mismatch')
        if metadata['external_suite_sha256'] != record['suite_sha256']:
            errors.append('ai_archive_differs_from_evaluated_archive')
        context = json.loads((metadata_file.parent / 'context-manifest.json').read_text())
        if metadata['prompt_iteration'] == 1 and metadata['prompt_sha256'] != context['prompt_sha256']:
            errors.append('ai_initial_prompt_differs_from_prepared_context')
        if metadata['prompt_iteration'] > 1 and not (ROOT / metadata['parent_run']).is_dir():
            errors.append('ai_parent_attempt_missing')
        operator = json.loads((metadata_file.parent / 'operator-metadata.json').read_text())
        expected_time = operator['generation_seconds'] + sum(
            a['generation_seconds'] for a in record.get('ai_prior_attempts', []))
        if abs(expected_time - record['generation_seconds']) > 1e-6:
            errors.append('ai_generation_time_mismatch')
        if record['test_count'] > record['budget']:
            errors.append('ai_test_method_budget_exceeded')
        driver_hash = record['ai_execution_driver_sha256']
        if digest(ROOT / record['ai_execution_driver']) != driver_hash or digest(generation / 'execution-driver.py') != driver_hash:
            errors.append('ai_execution_driver_hash_mismatch')
        for name, expected in record.get('ai_processing_source_sha256', {}).items():
            if digest(ROOT / name) != expected or digest(generation / 'processing-sources' / name) != expected:
                errors.append('ai_processing_source_hash_mismatch:' + name)
        if record.get('ai_local_processing_edits') != metadata.get('manual_edits', []):
            errors.append('ai_processing_edits_differ_from_provider_metadata')
        capture = ROOT / record["ai_capture_path"] if record.get("ai_capture_path") else ROOT / f"ai-tests/provider-captures/{record['generator']}/{record['project']}-{record['bug_id']}/s{record['seed']}-i1"
        response_file = capture / 'response.md'
        if digest(response_file) != metadata['response_sha256']:
            errors.append('ai_original_capture_response_changed')
        source_hashes = {}
        skipped_duplicate_names = []
        response_text = response_file.read_text(encoding='utf-8')
        if record.get('ai_source_processing_directory') == 'source-processing-v40':
            if (record['project'], record['generator'], record['seed']) != ('Lang', 'intellisphere', 102):
                raise ValueError('Unexpected v40 identity')
            expected = 'e688f9d452ed54772e271294effe58def0680cefb3cf0cf008d0164249f03e22'
            marker = '\n    @Test\n    public void testMaxDoubleArrayWithNaN() {'
            if hashlib.sha256(response_text.encode()).hexdigest() != expected or response_text.count(marker) != 1:
                raise ValueError('v40 recovery raw lineage differs')
            recovery = record['ai_local_processing_edits'][0]
            if recovery.get('raw_response_sha256') != expected or 'local_syntax_recovery' not in recovery:
                raise ValueError('v40 recovery evidence missing')
            response_text = response_text[:response_text.index(marker)].rstrip() + '\n}\n```\n'
        for code in re.findall(r'```java[^\n]*\n(.*?)\n```', response_text, re.S | re.I):
            cls = re.search(r'\bpublic\s+(?:final\s+)?class\s+([A-Za-z_$][\w$]*)', code)
            if not cls:
                continue
            package = re.search(r'^\s*package\s+([\w.]+)\s*;', code, re.M)
            name = (package.group(1).replace('.','/')+'/' if package else '')+cls.group(1)+'.java'
            if name in source_hashes and record.get('ai_source_processing_directory') == 'source-processing-v6':
                skipped_duplicate_names.append(name)
                continue
            source_hashes[name] = hashlib.sha256((code.strip()+'\n').encode('utf-8')).hexdigest()
        if skipped_duplicate_names != record.get('ai_duplicate_source_classes_skipped', []):
            errors.append('ai_duplicate_source_class_lineage_mismatch')
        originals = capture / record.get('ai_source_processing_directory','source-processing-v1') / 'input-tests'
        original_hashes = {p.relative_to(originals).as_posix():digest(p) for p in originals.rglob('*.java')}
        if original_hashes != source_hashes:
            errors.append('ai_input_source_differs_from_captured_response')
        for edit in record.get('ai_local_processing_edits', []):
            if record.get('ai_source_processing_directory') == 'source-processing-v40' and edit == record['ai_local_processing_edits'][0]:
                continue
            name = edit['path']
            if source_hashes.get(name) != edit['before_sha256']:
                errors.append('ai_processing_hash_chain_broken:' + name)
            if edit['after_sha256'] is None:
                source_hashes.pop(name, None)
            else:
                source_hashes[name] = edit['after_sha256']
        with tarfile.open(suite, 'r:bz2') as archive:
            archive_hashes = {member.name:hashlib.sha256(archive.extractfile(member).read()).hexdigest()
                for member in archive.getmembers() if member.isfile() and member.name.endswith('.java')}
        if archive_hashes != source_hashes:
            errors.append('ai_evaluated_source_differs_from_processing_hash_chain')
        for previous in record.get('ai_fixed_pruning_history', []):
            previous_path = ROOT / previous['previous_record']
            previous_record = json.loads(previous_path.read_text())
            stage = previous['fixed_stage_used']
            if previous_record['status'] != 'invalid' or not stage.startswith('fixed'):
                errors.append('ai_pruning_did_not_follow_invalid_fixed_validation')
            failures = parse_test_evidence((previous_path.parent / stage / 'command.log').read_text(),
                                           (previous_path.parent / stage / 'failing_tests').read_text())
            if not set(previous['removed_methods']).issubset(set(failures['failing_tests'])):
                errors.append('ai_pruned_methods_not_in_fixed_failures')
    if abs(record['duration_seconds'] + record['generation_seconds'] - record['total_seconds']) > 1e-6:
        errors.append('total_time_mismatch')
    if record.get('environment_repair'):
        repair_file = ROOT / record['environment_repair']
        repair = json.loads(repair_file.read_text())
        if digest(repair_file.parent / 'Cli.build.original.xml') != repair['before_sha256']:
            errors.append('environment_before_hash_mismatch')
        if digest(repair_file.parent / 'Cli.build.patched.xml') != repair['after_sha256']:
            errors.append('environment_after_hash_mismatch')
        if not (ROOT / record['superseded_environment_failure']).is_file():
            errors.append('original_environment_failure_missing')
    for repair in record.get('fixed_oracle_repair_history', []):
        previous_file = ROOT / repair['previous_record']
        previous = json.loads(previous_file.read_text())
        stage = repair['fixed_stage_used']
        if previous['status'] != 'invalid' or not stage.startswith('fixed'):
            errors.append('pruning_did_not_follow_invalid_fixed_validation')
        failures = parse_test_evidence((previous_file.parent / stage / 'command.log').read_text(),
                                      (previous_file.parent / stage / 'failing_tests').read_text())
        expected = {f'GeneratedStudyTest::generated{i}' for i in repair['removed_case_ids']}
        if set(failures['failing_tests']) != expected:
            errors.append('pruned_cases_differ_from_fixed_failures')
        previous_generation = json.loads((ROOT / repair['previous_generation']).read_text())
        previous_suite = (ROOT / repair['previous_generation']).parent / Path(previous_generation['suite']).name
        if digest(previous_suite) != repair['previous_suite_sha256']:
            errors.append('original_pruned_suite_hash_mismatch')
    if record.get('repair_source_sha256') and digest(ROOT / 'scripts/study/repair_fixed_oracles.py') != record['repair_source_sha256']:
        errors.append('repair_source_hash_mismatch')
    if record.get('execution_driver'):
        expected = record['execution_driver_sha256']
        if digest(ROOT / record['execution_driver']) != expected:
            errors.append('isolated_execution_driver_hash_mismatch')
        if digest(ROOT / record['setup_evidence'] / 'execution-driver.py') != expected:
            errors.append('isolated_execution_driver_snapshot_mismatch')
        target_file = evaluation.parent.parent / 'setup/targets.json'
        if digest(target_file) != record['targets_sha256']:
            errors.append('isolated_execution_target_inventory_mismatch')
    return errors


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--results', type=Path, default=ROOT / 'results/study/round2-v4-20260929')
    parser.add_argument('--output', type=Path, default=ROOT / 'output/submission/evidence-audit.json')
    args = parser.parse_args()
    batch = args.results.resolve()
    frozen = json.loads((batch / 'config.json').read_text())['source_sha256']
    issues, audited, other = [], [], Counter()
    for name, expected in frozen.items():
        if digest(ROOT / name) != expected:
            issues.append({'path': name, 'issue': 'current_source_differs_from_frozen_source'})
    for path in sorted((ROOT/'results/validation/ai-provider-service-retry').glob('*/*/*/retry-lineage.json')):
        lineage = json.loads(path.read_text())
        for target, field in (('original-capture/response.md','response_sha256'),('original-run/evaluation/record.json','record_sha256')):
            if digest(path.parent/target) != lineage[field]:
                issues.append({'path':path.relative_to(ROOT).as_posix(),'issue':'provider_service_retry_history_hash_mismatch:'+field})
    for path in sorted(batch.rglob('record.json')):
        record = json.loads(path.read_text())
        if record['status'] != 'complete':
            other[record['status']] += 1
            continue
        try:
            errors = check_record(path, frozen)
        except (KeyError, ValueError, OSError, ET.ParseError) as error:
            errors = [f'{type(error).__name__}: {error}']
        relative = path.relative_to(ROOT).as_posix()
        issues += [{'path': relative, 'issue': error} for error in errors]
        audited.append({'path': relative, 'sha256': digest(path), 'passed': not errors})
    output = {'audited_at_utc': utc_now(), 'results_path': batch.relative_to(ROOT).as_posix(),
              'completed_records_audited': len(audited), 'passed_records': sum(r['passed'] for r in audited),
              'other_status_counts': dict(other), 'issues': issues, 'records': audited,
              'scope': 'Artifact hashes, fixed/buggy logs, counters, archive integrity, generation budget and source freeze. This is not an instructor grading decision.'}
    args.output.parent.mkdir(parents=True, exist_ok=True)
    args.output.write_text(json.dumps(output, indent=2) + '\n')
    print(json.dumps({k: output[k] for k in ('completed_records_audited', 'passed_records', 'other_status_counts')}))
    print(f'issues: {len(issues)}')
    return 1 if issues else 0


if __name__ == '__main__':
    raise SystemExit(main())
