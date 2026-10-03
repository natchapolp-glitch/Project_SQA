"""Independent Beam receipt review. Reads sealed peer evidence; never executes Java/API."""
from pathlib import Path
import calendar
import copy
import hashlib
import io
import json
import subprocess
import tarfile
from datetime import datetime, timezone

BASE = Path(__file__).resolve().parent
ROOT = BASE.parents[2]
PEER = BASE / 'received-champ'
COMMIT = '07e68e520db37edffa5ef9fa842202150ec28e04'
V2 = PEER / 'output/api854-20261003/chronology-development-v2'
SOURCE = 'src/main/java/org/joda/time/Partial.java'


def require(condition, message):
    if not condition:
        raise ValueError(message)


def digest(data):
    return hashlib.sha256(data).hexdigest()


def read(path):
    return json.loads(path.read_text(encoding='utf-8'))


def git_blobs(paths, revision=COMMIT):
    paths = list(dict.fromkeys(paths))
    response = subprocess.run(['git', 'cat-file', '--batch'], cwd=ROOT,
                              input=''.join(f'{revision}:{p}\n' for p in paths).encode(),
                              capture_output=True, check=True).stdout
    stream = io.BytesIO(response)
    result = {}
    for path in paths:
        header = stream.readline().decode().strip().split()
        require(len(header) == 3 and header[1] == 'blob', 'Missing Git blob: ' + path)
        result[path] = stream.read(int(header[2]))
        require(stream.read(1) == b'\n', 'Malformed Git response')
    require(stream.read() == b'', 'Unexpected Git response tail')
    return result


def snapshot(types, values, buddhist=False):
    return {'chronology': 'org.joda.time.chrono.' + ('BuddhistChronology' if buddhist else 'ISOChronology'),
            'zone': 'UTC', 'types': types, 'values': values}


# Derived from declared inputs and fixed source contracts, not measured output.
EXPECTED = {
    'empty_iso_offset': snapshot([], []), 'empty_null': snapshot([], []),
    'single_hour_iso': snapshot(['hourOfDay'], [10]),
    'single_invalid_hour': {'rejected': True, 'exception': 'org.joda.time.IllegalFieldValueException'},
    'arrays_leap_iso': snapshot(['year', 'monthOfYear', 'dayOfMonth'], [2024, 2, 29]),
    'arrays_invalid_date': {'rejected': True, 'exception': 'org.joda.time.IllegalFieldValueException'},
    'arrays_bad_order': {'rejected': True, 'exception': 'java.lang.IllegalArgumentException'},
    'internal_iso': snapshot(['year', 'monthOfYear', 'dayOfMonth'], [2024, 2, 29]),
    'getfield_buddhist': {'field': 'year', 'epoch_year': 1970 + 543,
                         'receiver': snapshot(['year'], [2024])},
    'getfield_bad_index': {'rejected': True, 'exception': 'java.lang.ArrayIndexOutOfBoundsException'},
    'withchrono_buddhist': snapshot(['hourOfDay'], [10], True),
    'withchrono_same': snapshot(['hourOfDay'], [10]),
    'withchrono_null': snapshot(['hourOfDay'], [10]),
}


def validate_records(records, revision, policy, trace=False):
    cases = [r for r in records if 'case' in r and 'method_entry' not in r]
    require([r['case'] for r in cases] == list(EXPECTED), 'Case inventory/order changed')
    failed = []
    for row in cases:
        require(row['setup_succeeded'] is True, 'Fixture failure cannot count as algorithm fault')
        require(type(row['target_check_passed']) is bool, 'Invalid assertion state')
        if row['target_check_passed']:
            require(row['failure_class'] is None and row['failure_reason'] is None, 'Passed case has failure')
            require(row['observation'] == EXPECTED[row['case']], 'Independent oracle differs')
        else:
            require(row['failure_class'] == 'java.lang.AssertionError', 'Unexpected runtime failure')
            failed.append(row['case'])
    require(failed == ([] if revision == 'fixed' else ['arrays_bad_order']), 'Unexpected failure inventory')
    summaries = [r for r in records if r.get('summary')]
    require(summaries == [{'summary': True, 'executed': 13, 'target_checks': 13,
                          'passed': 13-len(failed), 'failed': len(failed),
                          'skipped': 0, 'fixture_errors': 0}], 'Counters do not reconcile')
    first = {}
    if trace:
        for entry in [r for r in records if r.get('method_entry')]:
            require(entry['case'] in EXPECTED and entry['class'] == 'org.joda.time.Partial', 'Unexpected target entry')
            require(type(entry['source_line']) is int and entry['source_line'] > 0, 'Missing source location')
            first.setdefault(entry['case'], entry)
        require(set(first) == set(EXPECTED), 'Missing case target entry')
        for case, entry in first.items():
            require([entry['method'], entry['descriptor']] == policy['exact_targets'][policy['cases'][case]],
                    'Wrong exact descriptor or first target entry')
        require(len({(e['method'], e['descriptor']) for e in first.values()}) == 6, 'Wrong declaration count')
        count = sum(bool(r.get('method_entry')) for r in records)
        require([r for r in records if r.get('trace_summary')] ==
                [{'trace_summary': True, 'method_entries': count, 'debuggee_exit_code': bool(failed)*1}],
                'Trace counters differ')
    return cases, list(first.values())


def audit():
    provenance = read(PEER / 'provenance.json')
    receipt = read(V2 / 'receipt.json')
    policy = read(V2 / 'policy.json')
    seal = read(V2 / 'preexecution-seal.json')
    retained_path = 'output/api854-20261003/prepare-v9-twenty-bug-development/Time-1/fixed-source/' + SOURCE
    paths = [p['source_path'] for p in provenance] + list(receipt['shared_input_sha256']) + [retained_path]
    blobs = git_blobs(paths)
    for item in provenance:
        require(item['source_commit'] == COMMIT, 'Mixed source commits')
        require(digest(blobs[item['source_path']]) == item['sha256'], 'Provenance Git hash mismatch')
        require(digest((ROOT/item['received_path']).read_bytes()) == item['sha256'], 'Received byte mismatch')
    for path, value in receipt['shared_input_sha256'].items():
        require(digest(blobs[path]) == value, 'Peer input pin differs from source commit: ' + path)
    checksums = 0
    for version in ['v1', 'v2']:
        packet = V2.parent / ('chronology-development-' + version)
        for name, expected in read(packet/'checksums.json').items():
            require(digest((packet/name).read_bytes()) == expected, 'Packet checksum mismatch: ' + name)
            checksums += 1
    for name, value in receipt['suite_sha256'].items():
        require(digest((V2/name).read_bytes()) == value, 'Suite hash differs')
    require(digest((V2/'preexecution-seal.json').read_bytes()) == receipt['preexecution_seal_sha256'], 'Seal hash mismatch')
    for sk, rk in [('suite_sha256','suite_sha256'), ('shared_input_sha256','shared_input_sha256'),
                   ('runtime_source_sha256','runtime_source_sha256'), ('worklist_identities','worklist_identities'),
                   ('dependency_sha256','dependencies_sha256'), ('fixed_target_source_sha256','fixed_target_source_sha256')]:
        require(seal[sk] == receipt[rk], 'Seal/receipt mismatch: ' + sk)
    compiled_count = 0
    sources = {}
    for revision in ['fixed', 'buggy']:
        archive = V2/(revision+'-production-source.tar.gz')
        require(digest(archive.read_bytes()) == receipt['source_archives'][revision]['archive_sha256'], 'Archive hash mismatch')
        require(seal['mirror_revisions'][revision] == receipt['source_archives'][revision]['revision'], 'Revision mismatch')
        with tarfile.open(archive) as tar:
            members = {m.name: tar.extractfile(m).read() for m in tar.getmembers() if m.isfile() and m.name.endswith('.java')}
        require(set(members) == set(receipt['compiled_source_sha256'][revision]), 'Compiled source inventory differs')
        if revision == 'fixed':
            retained = blobs[retained_path]
            require(members[SOURCE].replace(b'\r\n', b'\n') == retained.replace(b'\r\n', b'\n'), 'Fixed source semantic bytes differ')
            require(digest(retained) == receipt['fixed_target_source_sha256'], 'Retained fixed source pin differs')
            members[SOURCE] = retained
        for name, data in members.items():
            require(digest(data) == receipt['compiled_source_sha256'][revision][name], 'Compiled source hash mismatch')
            compiled_count += 1
        sources[revision] = members[SOURCE].decode('utf-8').replace('\r\n', '\n')
    fixed = sources['fixed']
    require('return iTypes[index].getField(chrono);' in fixed, 'Source no longer binds supplied Chronology')
    require('Partial(Chronology chronology, DateTimeFieldType[] types, int[] values)' in fixed, 'Internal ctor changed')
    require('iChronology = chronology;' in fixed and 'iTypes = types;' in fixed and 'iValues = values;' in fixed, 'Internal storage changed')
    require(calendar.monthrange(2024, 2)[1] == 29, 'Independent leap-day derivation differs')
    stages, raw = {}, {}
    for stage, metadata in receipt['stages'].items():
        raw[stage] = [json.loads(line) for line in (V2/(stage+'.stdout.log')).read_text().splitlines() if line.strip()]
        for suffix in ['stdout','stderr']:
            require(digest((V2/(stage+'.'+suffix+'.log')).read_bytes()) == metadata[suffix+'_sha256'], 'Stage log pin differs')
        require(read(V2/(stage+'.command.json')) == {k:v for k,v in metadata.items() if k not in ['cases','exact_target_entries','all_method_entry_count']}, 'Command record differs')
        revision = stage.split('_')[0]
        cases, entries = validate_records(raw[stage], revision, policy, trace='trace' in stage)
        require(cases == metadata['cases'], 'Receipt/raw observations differ')
        require(metadata['exit_code'] == (0 if revision=='fixed' else 1), 'Wrong process exit')
        if entries:
            require(entries == metadata['exact_target_entries'], 'Receipt/raw entries differ')
        stages[stage] = {'executed':13, 'skipped':0, 'fixture_errors':0, 'target_checks':13,
                         'passed':13 if revision=='fixed' else 12, 'failed':0 if revision=='fixed' else 1,
                         'exact_target_entries':entries}
    for revision in ['fixed','buggy']:
        require(receipt['stages'][revision+'_first']['cases'] == receipt['stages'][revision+'_second']['cases'] ==
                receipt['stages'][revision+'_method_entry_trace']['cases'], 'Repeated/trace observations differ')
    mutation = [json.loads(line) for line in (V2/'temporary-ignored-chronology.stdout.log').read_text().splitlines()]
    require([r['case'] for r in mutation if 'case' in r and r['target_check_passed'] is False] == ['getfield_buddhist'], 'Sensitivity failures differ')
    require(digest((V2/'temporary-ignored-chronology.stdout.log').read_bytes()) == receipt['temporary_ignored_chronology_mutation']['stdout_sha256'], 'Mutation hash differs')
    negatives = []
    mutations = [ ('skipped', lambda rs: rs[-1].update(skipped=1)),
                  ('fixture_error', lambda rs: rs[0].update(setup_succeeded=False)),
                  ('weakened_buddhist_oracle', lambda rs: next(r for r in rs if r.get('case')=='getfield_buddhist')['observation'].update(epoch_year=1970)),
                  ('runtime_failure', lambda rs: rs[0].update(target_check_passed=False, failure_class='java.lang.NullPointerException')),
                  ('missing_case', lambda rs: rs.pop(0)) ]
    for label, mutate in mutations:
        records = copy.deepcopy(raw['fixed_first']); mutate(records)
        try: validate_records(records,'fixed',policy)
        except ValueError: negatives.append(label)
        else: raise ValueError('Negative control wrongly accepted: '+label)
    records = copy.deepcopy(raw['fixed_method_entry_trace'])
    next(r for r in records if r.get('method_entry'))['descriptor'] = '()V'
    try: validate_records(records,'fixed',policy,trace=True)
    except ValueError: negatives.append('wrong_exact_descriptor')
    else: raise ValueError('Wrong descriptor wrongly accepted')
    current = git_blobs(receipt['runtime_source_sha256'], revision='HEAD')
    for path, data in current.items():
        require((ROOT/path).read_bytes() == data, 'Beam runtime modified locally: '+path)
    require(receipt['oracle_approved'] is False and receipt['shared_integration_approved'] is False and
            receipt['gate_a_passed'] is False and receipt['live_requests']==0 and receipt['queue_mutations']==0,
            'Peer candidate unexpectedly claims approval/live changes')
    return {'status':'pass', 'checked_at_utc':datetime.now(timezone.utc).isoformat(),
            'reviewer':'beam', 'source_commit':COMMIT, 'received_git_blobs_verified':len(provenance),
            'peer_shared_input_git_pins_verified':len(receipt['shared_input_sha256']),
            'packet_checksum_entries_verified':checksums, 'compiled_source_hashes_verified':compiled_count,
            'beam_runtime_files_unchanged':len(current), 'beam_runtime_sha256':{p:digest(d) for p,d in current.items()},
            'negative_controls_rejected':negatives, 'stages':stages,
            'worklist_identities':receipt['worklist_identities'], 'exact_targets':policy['exact_targets'],
            'policy_sha256':digest((V2/'policy.json').read_bytes()),
            'peer_receipt_sha256':digest((V2/'receipt.json').read_bytes()),
            'review_script_sha256':digest(Path(__file__).read_bytes()),
            'candidate_fault_detected':True, 'candidate_failed_case':'arrays_bad_order',
            'oracle_sensitivity_case':'getfield_buddhist',
            'beam_verdict':'accepted_for_bounded_candidate_oracle_development',
            'joint_final_recipe_acceptance':False, 'shared_integration_approved':False,
            'new_java_or_defects4j_executions':0, 'full_defects4j_evaluation':False,
            'coverage_kind':'received JDI exact method-entry; no line/branch percentage',
            'preparation_modified':False, 'runtime_modified':False, 'gate_a_passed':False,
            'primary_results_added':0, 'live_requests':0, 'queue_mutations':0,
            'shared_v9_selected_unchanged':380, 'shared_v9_exclusions_unchanged':311, 'denominator':691}


if __name__ == '__main__':
    result = audit()
    output = BASE/'beam-chronology-verdict.json'
    with output.open('x', encoding='utf-8', newline='\n') as stream:
        json.dump(result, stream, ensure_ascii=False, indent=2); stream.write('\n')
    print(json.dumps({k:result[k] for k in ['status','received_git_blobs_verified','packet_checksum_entries_verified',
                                          'compiled_source_hashes_verified','beam_runtime_files_unchanged','negative_controls_rejected','beam_verdict']}))
