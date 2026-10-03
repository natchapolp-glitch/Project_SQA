"""Audit the new JacksonDatabind-112 batch and consolidate scoped host receipts."""
import argparse
import csv
import io
import json
from pathlib import Path
import re
import tarfile

from .common import ROOT, read_json, sha256, write_json
from .audit_ready_development_results import audit_packet
from .evaluate_csv_development import counts, coverage_xml, PROJECTS
from .review_v10_readiness import BatchedObjects
from .review_joint_recipe_intake import require, digest
from .start_csv_development import AOM
from .verify_chronology_development import checkpoint
from .kku_client import utc_now, normalize_usage, normalize_quota

BASE = ROOT / 'output/api854-20261004'
GEN = BASE / 'champ-jacksondatabind112-development-generation-v1'
NATIVE = BASE / 'champ-jacksondatabind112-native-measurement-v1'


def write_table(path, rows):
    buf = io.StringIO(newline='')
    writer = csv.DictWriter(buf, fieldnames=list(rows[0]), lineterminator='\n')
    writer.writeheader()
    writer.writerows(rows)
    path.write_text(buf.getvalue(), encoding='utf-8', newline='\n')


def run(output):
    output = Path(output).resolve()
    require(output.is_relative_to(ROOT / 'output') and not output.exists(), 'Fresh contained audit required')
    pins, _ = checkpoint()
    packets = {}
    names = ['champ-ready-progress-audit-v1', 'champ-aom-csv-results-review-v1',
             'champ-mockito-ready-preflight-v1', 'champ-mockito-ready-preflight-v2',
             'champ-mockito-ready-preflight-v3', 'champ-mockito-ready-preflight-review-v1',
             'champ-jacksondatabind112-ready-preflight-v1', 'champ-jacksondatabind112-ready-preflight-v2',
             GEN.name, NATIVE.name]
    for name in names:
        packets[name] = audit_packet(BASE / name)
    plan = read_json(GEN / 'preexecution-plan.json')
    seal = read_json(NATIVE / 'preexecution-seal.json')
    require(plan['account_alias'] == 'a06' and plan['project'] == 'JacksonDatabind' and plan['bug_id'] == 112 and
            plan['condition'] == PROJECTS['JacksonDatabind'][-1] and plan['shared_checkpoint_pins'] == pins,
            'Prospective project/account/condition differs')
    require(read_json(GEN / 'receipt.json')['requests_attempted'] == 4 and
            read_json(GEN / 'receipt.json')['generation_requests_attempted'] == 2, 'Actual request count differs')
    aom = BatchedObjects(AOM)
    prep = 'output/api854-20261003/prepare-v12-graphics-development-v1/JacksonDatabind-112'
    aom.preload([prep + '/' + n for n in plan['received_inputs_sha256']])
    for name, h in plan['received_inputs_sha256'].items():
        require((GEN / 'received' / name).read_bytes() == aom.blob(prep + '/' + name) and
                sha256(GEN / 'received' / name) == h, 'Received frozen input changed')
    require(seal['aom_commit'] == AOM and seal['runtime_source_sha256'] == plan['source_v12_runtime_sha256'],
            'Frozen runtime differs')
    derived = read_json(NATIVE / 'isolated-bug-reference/derivation.json')
    preflight = read_json(GEN / 'native-preflight/isolated-bug-reference/derivation.json')
    expected = dict(derived['expected_isolated_buggy_source_sha256'])
    generated = seal['production_source_sha256']['buggy']['build_generated_sources']
    require(len(generated) == 1, 'Benchmark generated source count differs')
    expected.update({p: d['sha256'] for p, d in generated.items()})
    require(expected == seal['production_source_sha256']['buggy']['sources'] and
            derived['expected_isolated_buggy_source_sha256'] == preflight['expected_isolated_buggy_source_sha256'],
            'Isolated bug/build-generated source binding differs')
    stability = read_json(GEN / 'native-preflight/fixed-stability.json')['zero_vector_two_fresh_JVM_observations_per_target']
    require(len(stability) == 4, 'Fixed preflight stability scope differs')
    for i, entry in enumerate(stability):
        first = (GEN / ('native-preflight/fixed-stability-' + str(i) + '-1.stdout.log')).read_bytes()
        second = (GEN / ('native-preflight/fixed-stability-' + str(i) + '-2.stdout.log')).read_bytes()
        require(first == second and digest(first) == entry['observation_sha256'] and
                b'SQA_TRACE:{"target_invoked":true}' in first and b'SQA_FIXTURE_FAILURE' not in first,
                'Preexecution fixed target observation was not stable or invoked')
    fresh = []
    received = read_json(NATIVE / 'receipt.json')
    require(len(received['records']) == 4 and received['primary_results_added'] == 0, 'Whole outcome scope differs')
    for row in received['records']:
        approach = row['approach']
        folder = NATIVE / approach
        declared = row.get('declared_test_count')
        for stage, record in row.get('stages', {}).items():
            require(counts((folder / (stage + '.stdout.log')).read_bytes()) == record['counts'], 'Raw JUnit counts differ')
            if record.get('target_counts'):
                require(read_json(folder / (stage + '.target-counts.json')) == record['target_counts'], 'Target counters differ')
        if declared:
            sources = {p.relative_to(folder / 'sources').as_posix(): p.read_bytes() for p in (folder / 'sources').rglob('*.java')}
            require({p: digest(raw) for p, raw in sources.items()} == row['source_sha256'] and 0 < declared <= 30,
                    'Whole suite source/cap differs')
            with tarfile.open(folder / 'packaged-suite/suite.tar.bz2') as archive:
                files = [m for m in archive if m.isfile()]
                require({m.name for m in files} == set(sources) and
                        all(archive.extractfile(m).read() == sources[m.name] for m in files), 'Archive bytes changed')
            if approach.startswith('kku-'):
                blocks = re.findall(r'^```(?:java)?\s*\n(.*?)^```\s*$', (GEN / approach / 'raw-response.txt').read_text(encoding='utf-8'),
                                    re.MULTILINE | re.DOTALL)
                require({b.encode() for b in blocks} == set(sources.values()), 'AI assertions repaired or tests pruned')
            else:
                require((folder / 'algorithm-generation/GeneratedStudyTest.java').read_bytes() == sources['GeneratedStudyTest.java'],
                        'Algorithm Java changed')
        cov = row.get('coverage', {})
        usage = row.get('generation', {}).get('usage', {})
        if cov:
            require(coverage_xml(folder / 'coverage-report/coverage.xml', PROJECTS['JacksonDatabind'][2]) == cov,
                    'Target-class coverage differs')
            require(all(row['stages'][stage]['counts'] == {'executed': declared, 'skipped': 0, 'failed': 0}
                        for stage in ('fixed_first', 'fixed_second', 'coverage')), 'Fixed suite did not pass wholly')
            require(row['fault_detected'] == (row['stages']['buggy']['counts']['failed'] > 0), 'Raw native fault differs')
        if usage:
            body = read_json(GEN / approach / 'response.json')['evidence']['body']
            require(normalize_usage(body) == usage and normalize_quota(body) == row['generation']['model_quota'],
                    'Provider usage/quota differs')
        interpretation = ('native_failure_pending_full_D4J_semantic_review' if row.get('fault_detected') else
                          'native_no_fault_in_this_scope' if cov else 'unavailable_invalid_suite')
        fresh.append({'project': 'JacksonDatabind', 'bug_id': 112, 'approach': approach, 'condition': received['condition'],
                      'raw_status': row['status'], 'test_count': declared,
                      'fixed_first_failed': row.get('stages', {}).get('fixed_first', {}).get('counts', {}).get('failed'),
                      'fixed_second_failed': row.get('stages', {}).get('fixed_second', {}).get('counts', {}).get('failed'),
                      'buggy_failed': row.get('stages', {}).get('buggy', {}).get('counts', {}).get('failed'),
                      'raw_native_fault_flag': row.get('fault_detected'), 'fault_interpretation': interpretation,
                      'target_class_covered_lines': cov.get('covered_lines'), 'target_class_instrumented_lines': cov.get('instrumented_lines'),
                      'target_class_line_rate': cov.get('line_rate'), 'target_class_branch_rate': cov.get('branch_rate'),
                      'prompt_tokens': usage.get('prompt_tokens'), 'completion_tokens': usage.get('completion_tokens'),
                      'provider_total_tokens': usage.get('total_tokens'), 'target_checks_available': row.get('target_checks_per_stage') is not None,
                      'primary_result': False, 'full_defects4j_evaluation': False,
                      'receipt_path': (folder / 'receipt.json').relative_to(ROOT).as_posix(), 'receipt_sha256': sha256(folder / 'receipt.json')})
    table = list(read_json(BASE / 'champ-ready-progress-audit-v1/native-results.json')['records']) + fresh
    require(len(table) == 28 and len({(r['project'], r['bug_id']) for r in table}) == 6, 'Unique-bug/outcome accounting differs')
    beam = read_json(BASE / 'champ-beam-ready-results-review-v5/receipt.json')
    aom_receipt = read_json(BASE / 'champ-aom-csv-results-review-v1/receipt.json')
    host_rows = []
    for peer, rows in (('beam', beam['records']), ('aom', aom_receipt['records'])):
        for row in rows:
            c = row.get('coverage', {})
            host_rows.append({'project': row['project'], 'bug_id': row['bug_id'], 'approach': row['approach'], 'peer': peer,
                              'host': row.get('worker_id', row.get('host')), 'generation_condition': row['generation_condition'],
                              'execution_condition': row.get('execution_condition', row.get('condition')),
                              'test_count': row.get('test_count', row.get('declared_tests')), 'fault_detected': row['fault_detected'],
                              'line_covered': c.get('line_covered', row.get('line_covered')),
                              'line_total': c.get('line_total', row.get('line_total')),
                              'branch_covered': c.get('branch_covered', row.get('branch_covered')),
                              'branch_total': c.get('branch_total', row.get('branch_total')),
                              'suite_sha256': row['suite_sha256'], 'primary_result': False,
                              'record_path': row.get('peer_record_path', row.get('record_path')),
                              'record_sha256': row.get('canonical_record_sha256', row.get('record_sha256'))})
    require(len(host_rows) == 10 and len({(r['project'], r['bug_id']) for r in host_rows}) == 2, 'Reviewed host evidence scope differs')
    output.mkdir(parents=True)
    (output / 'producer.py').write_bytes(Path(__file__).read_bytes())
    write_table(output / 'native-results.csv', table)
    write_json(output / 'native-results.json', {'records': table, 'missing_values_are_not_zero': True,
                                              'different_conditions_are_not_independent_repetitions': True})
    write_table(output / 'reviewed-host-results.csv', host_rows)
    write_json(output / 'reviewed-host-results.json', {'records': host_rows, 'scope': 'Latest independently reviewed Csv/Jsoup host receipts only',
                                                    'same_suite_host_replays_are_not_new_bugs_or_independent_generations': True})
    ready = [r['approach'] for r in received['records'] if r['status'] == 'native_fixed_twice_buggy_coverage_measured']
    receipt = {'status': 'aom_csv_accepted_and_jacksondatabind112_next_batch_audited', 'native_unique_bugs': 6,
               'native_condition_bug_approach_outcomes': 28, 'new_native_records': fresh, 'reviewed_latest_host_suite_rows': 10,
               'reviewed_latest_host_unique_bugs': 2, 'unique_bugs_with_four_valid_full_D4J_methods': 1,
               'new_billable_requests': 4, 'new_generation_requests': 2, 'cumulative_billable_requests': 27,
               'cumulative_generation_requests': 14, 'next_ready_packet': NATIVE.relative_to(ROOT).as_posix(),
               'next_ready_approaches': ready, 'next_host': 'aom-pc1', 'compress_host_assignment_retained': 'beam-pc1',
               'mockito_preflight_held_without_API': True, 'packet_manifests': packets,
               'manifest_entries_verified': sum(p['entries'] for p in packets.values()),
               'shared_checkpoint_pins_unchanged': len(pins), 'provider_total_null_retained': True,
               'primary_results_added': 0, 'gate_a_approved': False, 'completed_at_utc': utc_now()}
    write_json(output / 'receipt.json', receipt)
    write_json(output / 'checksums.json', {p.relative_to(output).as_posix(): sha256(p) for p in sorted(output.rglob('*')) if p.is_file()})
    return receipt


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--output', type=Path, required=True)
    out = parser.parse_args().output.resolve()
    new = not out.exists()
    try:
        value = run(out)
    except Exception as error:
        if new and out.is_relative_to(ROOT / 'output') and out.exists() and not (out / 'checksums.json').exists():
            write_json(out / 'failed-attempt.json', {'error': str(error), 'error_type': type(error).__name__, 'primary_results_added': 0})
            write_json(out / 'checksums.json', {p.relative_to(out).as_posix(): sha256(p) for p in sorted(out.rglob('*')) if p.is_file()})
        raise
    print(json.dumps({'status': value['status'], 'ready': value['next_ready_approaches'],
                      'outcomes': value['native_condition_bug_approach_outcomes'], 'entries': value['manifest_entries_verified']}))
