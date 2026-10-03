"""Review pinned Aom Csv benchmark evidence without rerunning suites or providers."""
import argparse
import csv
import io
import json
from pathlib import Path
import re
import tempfile
import xml.etree.ElementTree as ET

from .common import ROOT, read_json, sha256, write_json
from .review_v10_readiness import BatchedObjects
from .review_joint_recipe_intake import digest, require
from .start_csv_development import AOM
from .evaluate_csv_development import PROJECTS
from .benchmark_sources import derive
from .verify_chronology_development import checkpoint
from .kku_client import utc_now

PEER = '4334c2ab908f517f99c7046c08f45a25c0b6a64d'
BASE = 'output/api854-20261004/'
PACKETS = ('aom-ready-csv-intake-v1', 'aom-ready-csv-d4j-v1',
           'aom-ready-csv-report-v1', 'aom-ready-messages-intake-v1',
           'aom-ready-messages-d4j-v2', 'aom-ready-results-report-v2',
           'aom-ready-messages-d4j-v1', 'aom-ready-csv-publication-v1',
           'aom-ready-csv-publication-v2', 'aom-ready-results-publication-v3')
RUN = BASE + 'aom-ready-messages-d4j-v2'
REPORT = BASE + 'aom-ready-results-report-v2'
NATIVE = ROOT / BASE / 'champ-csv-messages-native-measurement-v1'


def run(output, defects4j):
    output = Path(output).resolve()
    require(output.is_relative_to(ROOT / 'output') and not output.exists(), 'New contained output required')
    pins, _ = checkpoint()
    peer = BatchedObjects(PEER)
    peer.preload([BASE + name + '/checksums.json' for name in PACKETS])
    manifests = {BASE + name: peer.document(BASE + name + '/checksums.json') for name in PACKETS}
    paths = [root + '/' + p for root, manifest in manifests.items() for p in manifest]
    peer.preload([p for p in paths if p not in peer.cache])
    for root, manifest in manifests.items():
        for p, h in manifest.items():
            require(digest(peer.blob(root + '/' + p)) == h, 'Aom sealed manifest mismatch')
    require(sum(len(m) for m in list(manifests.values())[:-1]) == 871, 'Expected Aom intake packet count differs')
    plan = peer.document(RUN + '/preexecution-plan.json')
    native = read_json(NATIVE / 'preexecution-seal.json')
    require(plan['baseline_commit'] == AOM and plan['host'] == 'aom-pc1' and plan['cpu_slots'] == 1,
            'Host or frozen preparation differs')
    require(plan['runtime_source_sha256'] == native['runtime_source_sha256'] and len(plan['runtime_source_sha256']) == 41,
            'Frozen runtime differs')
    require(plan['received_native_manifest_sha256'] == sha256(NATIVE / 'checksums.json'), 'Native packet differs')
    require(not plan['primary_result'] and not plan['gate_a_approved'] and plan['api_requests'] == plan['queue_mutations'] == 0,
            'Unexpected promotion or provider/queue mutation')
    source = 'src/main/java/org/apache/commons/csv/ExtendedBufferedReader.java'
    strict_root = BASE + 'aom-ready-messages-d4j-v1'
    strict = peer.document(strict_root + '/failed-attempt.json')
    require(strict['status'] == 'strict_source_preflight_failed_before_tests' and strict['no_test_outcomes_from_this_attempt'],
            'First strict source failure was not preserved')
    require(strict['native_buggy_sha256'] == native['production_source_sha256']['buggy']['sources'][source],
            'Native parent binding differs')
    require(digest(peer.blob(strict_root + '/native-upstream-buggy.java')) == strict['native_buggy_sha256'],
            'Preserved native Java differs')
    require(digest(peer.blob(strict_root + '/d4j-reconstructed-buggy.java')) == strict['benchmark_buggy_sha256'],
            'Preserved benchmark Java differs')
    require(plan['buggy_source_override']['path'] == source and
            plan['buggy_source_override']['sha256'] == strict['benchmark_buggy_sha256'], 'Prospective benchmark override differs')
    output.mkdir(parents=True)
    (output / 'producer.py').write_bytes(Path(__file__).read_bytes())
    repo, source_root, _, fixed, parent, _ = PROJECTS['Csv']
    with tempfile.TemporaryDirectory(prefix='.champ-aom-csv-review-', dir=ROOT / 'output') as folder:
        scratch = Path(folder).resolve()
        require(scratch.is_relative_to(ROOT / 'output'), 'Unsafe temporary cleanup target')
        derived = derive(Path(defects4j), 'Csv', 1, repo, fixed, parent, scratch, source_root,
                         output / 'csv-source-derivation', patch_tool=Path('C:/Program Files/Git/usr/bin/patch.exe'))
        require(derived['fixed_source_sha256'] == native['production_source_sha256']['fixed']['sources'],
                'Exact fixed inventory differs')
        expected = dict(native['production_source_sha256']['buggy']['sources'])
        expected[source] = strict['benchmark_buggy_sha256']
        require(derived['expected_isolated_buggy_source_sha256'] == expected, 'Official isolated bug inventory differs')
    producer = peer.blob('scripts/study/aom_ready_messages_d4j_v2.py')
    require(digest(producer) == plan['producer_sha256'], 'Pinned execution producer differs')
    require(b'validate_worktree(tree, "Csv", f"1{revision}")' in producer and
            b'Actual production bytes do not match native revisions' in producer, 'Actual worktree/source guards missing')
    rows = peer.document(REPORT + '/full-d4j-results.json')['records']
    require(len(rows) == 8 and sum(r['status'] == 'complete' for r in rows) == 6, 'Aom outcome accounting differs')
    require(sum(r['status'].startswith('invalid') for r in rows) == 2, 'Baseline invalids missing')
    csv_rows = list(csv.DictReader(io.StringIO(peer.blob(REPORT + '/full-d4j-results.csv').decode('utf-8'))))
    require(len(csv_rows) == len(rows), 'CSV/JSON row counts differ')
    for row, flat in zip(rows, csv_rows):
        require(digest(peer.blob(row['record_path'])) == row['record_sha256'], 'Report canonical record differs')
        for k, v in flat.items():
            if k in row:
                expected = '' if row[k] is None else str(row[k])
                require(v == expected, 'CSV/JSON scalar differs: ' + k)
    accepted = []
    for row in rows[-4:]:
        approach = row['approach']
        require(row['condition'] == plan['condition'] and row['generation_condition'] == plan['generation_condition'] and
                row['status'] == 'complete' and not row['primary_result'], 'Execution condition/promotion differs')
        record_root = row['record_path'].rsplit('/', 1)[0]
        record = peer.document(row['record_path'])
        require(record['fixed_validation'] == 'passed_twice' and record['timezone'] == 'America/Los_Angeles' and
                record['versions']['defects4j'] == '3.0.1' and '"11.' in record['versions']['java'], 'Host validation differs')
        archive = record_root + '/' + Path(record['suite_path']).name
        suite_hash = sha256(NATIVE / approach / 'packaged-suite/suite.tar.bz2')
        require(digest(peer.blob(archive)) == suite_hash == row['suite_sha256'] == record['suite_sha256'],
                'Original Java archive changed')
        stages = {}
        identities = None
        for stage in ('fixed-1', 'fixed-2', 'buggy', 'coverage'):
            prefix = record_root + '/' + stage
            events = peer.blob(prefix + '/all_tests').decode('utf-8').splitlines()
            require(len(events) == len(set(events)) == row['declared_tests'] and
                    all(re.fullmatch(r'[\w$]+\([\w.$]+\)', e) for e in events), 'Actual test-start identities differ')
            if identities is None:
                identities = set(events)
            require(set(events) == identities, 'Test identities changed between stages')
            c = row['stage_counts'][stage]
            require(c['started'] == c['executed'] == len(events), 'Test-start count differs')
            counter_path = prefix + '/sqa-stage-counts.json'
            if approach in ('cmaes', 'fscs-art'):
                observed = peer.document(counter_path)
                require(observed['executed'] == observed['target_checks'] == 30 and observed['skipped'] == 0 and
                        c['executed'] == c['target_checks'] == 30 and c['skipped'] == 0, 'Embedded algorithm counts differ')
            else:
                require(counter_path not in peer.cache and c['skipped'] is None and c['target_checks'] is None,
                        'Missing AI counters were inferred')
            failures = peer.blob(prefix + '/failing_tests')
            failed = len(re.findall(rb'^--- ', failures, re.MULTILINE))
            require(failed == c['failed'], 'Actual failure count differs')
            command = peer.document(prefix + '/command.json')
            require(command['exit_code'] == 0 and not command['timed_out'], 'Defects4J stage command failed')
            if stage != 'buggy':
                require(failed == 0, 'Fixed or coverage suite failed')
            stages[stage] = {'executed': len(events), 'skipped': c['skipped'],
                             'target_checks': c['target_checks'], 'failed': failed,
                             'test_start_events_sha256': digest(peer.blob(prefix + '/all_tests'))}
        require(row['fault_detected'] == (stages['buggy']['failed'] > 0), 'Raw fault flag differs')
        coverage = ET.fromstring(peer.blob(record_root + '/coverage/coverage.xml'))
        cls = [c for c in coverage.findall('.//class') if c.get('name') == PROJECTS['Csv'][2]]
        require(len(cls) == 1, 'Exact target-class coverage missing')
        lines = cls[0].findall('./lines/line')
        require(row['line_total'] == len(lines) and row['line_covered'] == sum(int(l.get('hits')) > 0 for l in lines),
                'Raw coverage line counts differ')
        require(abs(float(cls[0].get('branch-rate')) - row['branch_covered'] / row['branch_total']) < 1e-10,
                'Raw branch coverage differs')
        if approach.startswith('kku-'):
            failure = peer.blob(record_root + '/buggy/failing_tests')
            method, assertion = (('lineNumberDoesNotDoubleCountCRLF', b'expected:<1> but was:<0>') if approach == 'kku-claude'
                                 else ('testCarriageReturnLineNumber', b'expected:<2> but was:<1>'))
            require(method.encode() in failure and assertion in failure and b'junit.framework.AssertionFailedError' in failure,
                    'CR bug evidence differs')
            usage = read_json(NATIVE / approach / 'receipt.json')['generation']['usage']
            require(all(row[k] == usage[k] for k in ('prompt_tokens', 'completion_tokens', 'total_tokens')),
                    'Provider usage changed')
            if approach == 'kku-claude':
                require(row['total_tokens'] is None, 'Missing provider total was inferred')
        accepted.append({**row, 'verified_actual_stage_counts': stages, 'host': 'aom-pc1',
                         'original_archive_sha256': suite_hash})
    quarantine = peer.document(REPORT + '/cli-quarantine.json')
    require(not quarantine['included_in_scientific_fault_count'] and quarantine['raw_fault_retained'], 'Cli quarantine missing')
    publication = peer.document(BASE + 'aom-ready-results-publication-v3/receipt.json')
    for name, count in (('test_aom_ready_report', 7), ('test_evaluate', 16)):
        prefix = BASE + 'aom-ready-csv-publication-v1/' + name
        cmd = peer.document(prefix + '/command.json')
        log = peer.blob(prefix + '/command.log')
        require(cmd['exit_code'] == 0 and not cmd['timed_out'] and ('Ran ' + str(count) + ' tests').encode() in log and
                log.rstrip().endswith(b'OK'), 'Recorded Aom regression did not pass')
        require(publication['regression'][name]['command_sha256'] == digest(peer.blob(prefix + '/command.json')) and
                publication['regression'][name]['log_sha256'] == digest(log), 'Regression evidence hashes differ')
    deps = peer.document(BASE + 'aom-ready-csv-publication-v2/dependency-hashes.json')
    for p, h in deps['files'].items():
        relative = p.split('/defects4j/', 1)[1]
        require(sha256(Path(defects4j) / relative) == h, 'Post-run peer dependency differs from local verified installation')
    (output / 'received-full-results.json').write_bytes(peer.blob(REPORT + '/full-d4j-results.json'))
    receipt = {'status': 'aom_csv_four_approach_scoped_benchmark_evidence_accepted', 'aom_commit': PEER,
               'accepted_latest_full_evaluations': 4, 'aom_total_including_baseline_full_evaluations': 6,
               'unique_bugs': 1, 'new_unique_bugs_added_by_this_host_replay': 0,
               'verified_manifest_entries': sum(map(len, manifests.values())),
               'manifest_bindings': {r: {'entries': len(m), 'sha256': digest(peer.blob(r + '/checksums.json'))}
                                     for r, m in manifests.items()},
               'records': accepted, 'recorded_regression_tests_reviewed': 23, 'regression_tests_rerun_here': 0,
               'actual_test_start_lists_checked': 16, 'raw_coverage_XMLs_checked': 4,
               'official_isolated_bug_source_inventory_verified': True,
               'source_derivation_sha256': sha256(output / 'csv-source-derivation/derivation.json'),
               'strict_first_attempt_preserved': True, 'wrapper_source_guard_retained': True,
               'post_run_dependency_hashes_checked': len(deps['files']), 'cli_quarantine_retained': True,
               'shared_checkpoint_pins_unchanged': len(pins), 'kku_requests': 0, 'queue_mutations': 0,
               'primary_results_added': 0, 'gate_a_approved': False, 'completed_at_utc': utc_now(),
               'limitations': ['Aom AI skip/target counters remain null; test-start events do not prove skip absence',
                               'Actual worktree source guards are bound to the sealed producer; per-worktree source inventories were not exported',
                               'Post-run library hashes are not a complete preexecution operating-system binding',
                               'Same suites on another host add host evidence, not new bugs or independent generations',
                               'AI/algorithm input-domain equivalence and full854/GateA remain unapproved']}
    write_json(output / 'receipt.json', receipt)
    write_json(output / 'checksums.json', {p.relative_to(output).as_posix(): sha256(p) for p in sorted(output.rglob('*')) if p.is_file()})
    return receipt


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--output', type=Path, required=True)
    parser.add_argument('--defects4j', type=Path, required=True)
    args = parser.parse_args()
    out = args.output.resolve()
    new = not out.exists()
    try:
        value = run(out, args.defects4j)
    except Exception as error:
        if new and out.is_relative_to(ROOT / 'output') and out.exists() and not (out / 'checksums.json').exists():
            write_json(out / 'failed-attempt.json', {'error': str(error), 'error_type': type(error).__name__, 'primary_results_added': 0})
            write_json(out / 'checksums.json', {p.relative_to(out).as_posix(): sha256(p) for p in sorted(out.rglob('*')) if p.is_file()})
        raise
    print(json.dumps({'status': value['status'], 'manifest_entries': value['verified_manifest_entries'],
                      'accepted_latest_full_evaluations': value['accepted_latest_full_evaluations']}))
