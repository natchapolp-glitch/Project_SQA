"""Audit sealed Csv observations and export an honest four-approach table.

No API calls, suite repairs, queue mutations, or primary-result promotion.
"""
import argparse
import csv
from datetime import datetime, timezone
import io
from pathlib import Path
import re
import tarfile

from .common import ROOT, read_json, write_json, sha256
from .evaluate_csv_development import counts, coverage_xml
from .review_joint_recipe_intake import require
from .verify_chronology_development import checkpoint

BASE = ROOT / 'output/api854-20261004'
PACKETS = (
    'champ-24h-capacity-plan-v1', 'champ-provider-calibration-v1',
    'champ-provider-token-count-probe-v1', 'champ-csv-development-generation-v1',
    'champ-csv-native-measurement-v1', 'champ-csv-native-measurement-v2',
    'champ-csv-native-measurement-v3', 'champ-csv-native-measurement-v4',
)


def run(output):
    output = Path(output).resolve()
    require(output.is_relative_to(ROOT / 'output') and not output.exists(), 'New contained output required')
    pins, pin_audit = checkpoint()
    manifests = {}
    checked = 0
    for name in PACKETS:
        packet = BASE / name
        manifest = read_json(packet / 'checksums.json')
        actual = {p.relative_to(packet).as_posix() for p in packet.rglob('*') if p.is_file()}
        require(actual == set(manifest) | {'checksums.json'}, 'Unsealed packet inventory: ' + name)
        for relative, expected in manifest.items():
            path = (packet / relative).resolve()
            require(path.is_relative_to(packet) and sha256(path) == expected, 'Packet bytes changed: ' + name + '/' + relative)
            checked += 1
        manifests[name] = {'path': (packet / 'checksums.json').relative_to(ROOT).as_posix(),
                           'sha256': sha256(packet / 'checksums.json'), 'entries': len(manifest)}
    generation = BASE / 'champ-csv-development-generation-v1'
    native = BASE / 'champ-csv-native-measurement-v4'
    result = read_json(native / 'receipt.json')
    require(len(result['records']) == 4 and result['full_defects4j_evaluations'] == 0
            and result['primary_results_added'] == 0, 'Wrong scope or missing approaches')
    require({r['approach'] for r in result['records']} == {'cmaes', 'fscs-art', 'kku-claude', 'kku-gemini'}, 'Approaches differ')
    table = []
    for row in result['records']:
        approach = row['approach']
        folder = native / approach
        declared = row.get('declared_test_count')
        for stage, observation in row.get('stages', {}).items():
            raw = (folder / (stage + '.stdout.log')).read_bytes()
            require(counts(raw) == observation['counts'], 'Actual JUnit counts differ')
            require(sha256(folder / (stage + '.stdout.log')) == observation['command']['stdout_sha256'], 'Stage log differs')
        for relative, expected in row.get('source_sha256', {}).items():
            require(sha256(folder / 'sources' / relative) == expected, 'Generated source changed')
        if declared:
            archive = folder / 'packaged-suite/suite.tar.bz2'
            with tarfile.open(archive) as bundle:
                members = [m for m in bundle if m.isfile()]
                source_bytes = {p.relative_to(folder / 'sources').as_posix(): p.read_bytes()
                                for p in (folder / 'sources').rglob('*.java')}
                require({m.name for m in members} == set(source_bytes), 'Packaged suite inventory differs')
                for member in members:
                    require(bundle.extractfile(member).read() == source_bytes[member.name], 'Packaged Java changed')
        if approach in {'cmaes', 'fscs-art'}:
            require(row['status'] == 'native_fixed_twice_buggy_coverage_measured' and row['fault_detected'] is False, 'Unexpected algorithm outcome')
            require(all(s['counts'] == {'executed': 30, 'skipped': 0, 'failed': 0}
                        for s in row['stages'].values()), 'Algorithm stage counts differ')
            require(coverage_xml(folder / 'coverage-report/coverage.xml') == row['coverage'], 'Actual coverage differs')
            require(row['coverage']['covered_lines'] == 31 and row['coverage']['instrumented_lines'] == 37, 'Target coverage differs')
            require((folder / 'algorithm-generation/GeneratedStudyTest.java').read_bytes()
                    == (folder / 'sources/GeneratedStudyTest.java').read_bytes(), 'Algorithm source repaired')
            require(not any(s in (folder / 'buggy.stdout.log').read_bytes() for s in
                            (b'SQA_HARNESS', b'SQA_FIXTURE_FAILURE', b'NoClassDefFoundError', b'LinkageError')), 'Harness failure mislabeled')
        if approach == 'kku-gemini':
            raw = (generation / approach / 'raw-response.txt').read_text(encoding='utf-8')
            blocks = re.findall(r'^```(?:java)?\s*\n(.*?)^```\s*$', raw, re.MULTILINE | re.DOTALL)
            require(len(blocks) == 1 and blocks[0].encode('utf-8') == next((folder / 'sources').rglob('*.java')).read_bytes(), 'AI source repaired')
            require(row['status'] == 'fixed_validation_failed_entire_suite_rejected', 'Gemini outcome differs')
            require(set(row['stages']) == {'fixed_first', 'fixed_second'} and all(s['counts'] == {'executed': 15, 'skipped': 0, 'failed': 1}
                         for s in row['stages'].values()), 'Gemini invalid status differs')
            require((folder / 'fixed_first.stdout.log').read_bytes() == (folder / 'fixed_second.stdout.log').read_bytes(), 'Fixed failure is not repeatable')
        if approach == 'kku-claude':
            require(row['status'] == 'invalid_generation_truncated' and not (generation / approach / 'raw-response.txt').read_bytes(), 'Sonnet truncation differs')
        usage = row.get('generation', {}).get('usage', {})
        quota = row.get('generation', {}).get('model_quota', {})
        if usage:
            require(usage['prompt_tokens'] + usage['completion_tokens'] == usage['total_tokens'], 'Observed usage inconsistent')
            require(quota['daily_usage_tokens'] + quota['daily_remaining_tokens'] == quota['daily_quota_tokens'], 'Observed quota inconsistent')
        coverage = row.get('coverage', {})
        table.append({'project': 'Csv', 'bug_id': 1, 'approach': approach, 'condition': result['condition'],
                      'status': row['status'], 'declared_tests': declared,
                      'fixed_first_executed': row.get('stages', {}).get('fixed_first', {}).get('counts', {}).get('executed'),
                      'fixed_first_failed': row.get('stages', {}).get('fixed_first', {}).get('counts', {}).get('failed'),
                      'fixed_second_failed': row.get('stages', {}).get('fixed_second', {}).get('counts', {}).get('failed'),
                      'buggy_executed': row.get('stages', {}).get('buggy', {}).get('counts', {}).get('executed'),
                      'fault_detected': row.get('fault_detected'),
                      'target_class_covered_lines': coverage.get('covered_lines'),
                      'target_class_instrumented_lines': coverage.get('instrumented_lines'),
                      'target_class_line_rate': coverage.get('line_rate'), 'target_class_branch_rate': coverage.get('branch_rate'),
                      'prompt_tokens': usage.get('prompt_tokens'), 'completion_tokens': usage.get('completion_tokens'),
                      'total_tokens': usage.get('total_tokens'), 'primary_result': False,
                      'full_defects4j_evaluation': False, 'receipt_path': (folder / 'receipt.json').relative_to(ROOT).as_posix(),
                      'receipt_sha256': sha256(folder / 'receipt.json')})
    output.mkdir(parents=True)
    (output / 'producer.py').write_bytes(Path(__file__).read_bytes())
    buffer = io.StringIO(newline='')
    writer = csv.DictWriter(buffer, fieldnames=list(table[0]), lineterminator='\n')
    writer.writeheader(); writer.writerows(table)
    (output / 'results.csv').write_text(buffer.getvalue(), encoding='utf-8', newline='\n')
    write_json(output / 'results.json', {'condition': result['condition'], 'records': table,
                                        'missing_values_are_unavailable_not_zero': True})
    receipt = {'schema_version': 1, 'status': 'four_approach_native_outcomes_audited',
               'condition': result['condition'], 'user_ready_subset_priority_explicitly_accepted': True,
               'user_authorization': 'Collect actual results for all four approaches on ready bugs, then expand within time remaining',
               'deadline_plan_v1_superseded_fields': ['scope_reduction_authorized', 'status'],
               'full854_goal_retained': True, 'bugs_with_all_four_outcomes': 1, 'valid_measured_suites': 2,
               'invalid_ai_suites': 2, 'full_defects4j_evaluations': 0, 'primary_results_added': 0,
               'primary_gate_a_passed': False, 'packet_manifests': manifests, 'checksum_entries_verified': checked,
               'shared_checkpoint_pin_count': len(pins), 'shared_checkpoint_audit': pin_audit,
               'attempt_classification': {'v1': 'native harness setup failure: missing snapshot config',
                   'v2': 'algorithm CLI harness failures; not algorithm scientific failures',
                   'v3': 'coverage harness absolute-path instrumentation failure; not detected bug',
                   'v4': 'successful native four-approach outcome collection; original AI responses reused'},
               'queue_mutations': 0, 'shared_quota_ledger_mutations': 0,
               'limitations': ['Native Java17/release7/UTC, not frozen Defects4J host/environment',
                   'Coverage percentage applies only to ExtendedBufferedReader',
                   'AI input-domain equivalence and full 403 declaration semantics not approved',
                   'Target checks per stage unavailable in retained native-v4 evidence',
                   'Do not substitute native elapsed time for full 854-bug throughput',
                   'Model context/output limits, exact framing, expiry/reset and other accounts quota remain unverified'],
               'completed_at_utc': datetime.now(timezone.utc).isoformat()}
    write_json(output / 'receipt.json', receipt)
    write_json(output / 'checksums.json', {p.relative_to(output).as_posix(): sha256(p) for p in sorted(output.rglob('*')) if p.is_file()})
    return receipt


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--output', type=Path, required=True)
    value = run(parser.parse_args().output)
    print({'status': value['status'], 'checksums': value['checksum_entries_verified'], 'four_approach_bugs': value['bugs_with_all_four_outcomes']})
