"""Audit received repair/combined evidence; never rerun or approve experiments."""
import argparse
from collections import Counter
from datetime import datetime, timezone
import json
from pathlib import Path
import tarfile
import xml.etree.ElementTree as ET

from .common import ROOT, contained, read_json, sha256, write_json, implementation_hashes
from .preparation import clean_targets, digest, encoded, POLICY_V7
from .fixture_semantics import descriptor, declaring_class, expected_exception
from .target_coverage import validate_stage

EVIDENCE = ROOT / 'docs/api854/evidence'


def require(condition, message):
    if not condition:
        raise ValueError(message)


def verify_packet(folder):
    checksums = read_json(folder / 'checksums.json')
    for name, expected in checksums.items():
        require(sha256(contained(folder, name)) == expected, 'Received checksum differs: ' + name)
    return len(checksums)


def verify_suite(packet, row):
    folder = contained(packet, f"{row['project']}-{row['bug_id']}-{row['approach']}")
    generated = read_json(folder / 'original-generation-result.json')
    original = read_json(folder / 'original-evaluation-result.json')
    review = read_json(folder / 'semantic-review-input.json')
    verdict = read_json(folder / 'semantic-review-result.json')
    protocol_hash = read_json(packet / 'index.json')['protocol_sha256']
    require(original['job']['protocol_hash'] == generated['job']['protocol_hash'] == protocol_hash,
            'Suite protocol binding differs')
    for name in ('project', 'bug_id', 'approach'):
        require(original['job'][name] == generated['job'][name] == row[name], 'Suite identity differs')
    require(original['observed_outcome'] == 'complete' and original['usable'] is False,
            'Original result must remain complete and unapproved')
    require(sha256(folder / 'suite.tar.bz2') == generated['suite_sha256'] == original['suite_sha256'] == row['suite_sha256'],
            'Archive binding differs')
    with tarfile.open(folder / 'suite.tar.bz2', 'r:bz2') as archive:
        java = [archive.extractfile(member).read() for member in archive if member.isfile() and member.name.endswith('.java')]
    require(java == [(folder / 'generation/GeneratedStudyTest.java').read_bytes()], 'Archived generated Java differs')
    require(verdict['status'] == review['verdict'] == row['semantic_review']['verdict'] == 'valid', 'Local verdict differs')
    require(verdict['evaluation_result_sha256'] == sha256(folder / 'original-evaluation-result.json')
            and verdict['review_sha256'] == sha256(folder / 'semantic-review-input.json') == row['semantic_review']['review_sha256']
            and verdict['review'] == review, 'Original/review bindings differ')
    require(review['fixture_oracle_review']['team_or_primary_approval'] is False, 'Unexpected primary approval')
    mapped = read_json(folder / 'evidence-path-map.json')['paths']
    refs = review['target_execution_evidence'] + [s['evidence'] for s in review['stage_counts'].values()]
    for ref in refs:
        require(sha256(contained(folder, mapped[ref['path']])) == ref['sha256'], 'Review evidence path/hash differs')
    for stage in ('fixed-1', 'fixed-2', 'buggy', 'coverage'):
        counts = read_json(folder / stage / 'sqa-stage-counts.json')
        require(all(counts[k] == v for k,v in {'executed':30,'skipped':0,'target_checks':30}.items()), 'Stage counters differ')
        command = read_json(folder / stage / 'command.json')
        require(command['timed_out'] is False, 'Unexpected timed-out stage')
        if stage != 'buggy':
            require(command['exit_code'] == 0 and not (folder / stage / 'failing_tests').read_text().strip(),
                    'Fixed/coverage stage did not pass')
    coverage_files = [folder / 'coverage/coverage.xml']
    binding = folder / 'target-coverage-binding.json'
    if binding.is_file():
        bound = read_json(binding)
        extra_folder = contained(folder, bound['directory'])
        extra = read_json(extra_folder / 'result.json')
        previous = folder / 'target-coverage/result.json'
        require(sha256(extra_folder / 'result.json') == bound['result_sha256']
                and sha256(previous) == bound['previous_result_sha256']
                and read_json(previous)['status'] == 'failed', 'Supplemental attempts were not retained/bound')
    else:
        extra_folder = folder / 'target-coverage'
        extra = read_json(extra_folder / 'result.json') if extra_folder.is_dir() else None
    if extra is not None:
        measurement_hash = digest((json.dumps(original['measurement'], indent=2, allow_nan=False) + '\n').encode())
        require(extra['status'] == 'complete' and extra['suite_sha256'] == row['suite_sha256']
                and extra['original_measurement_sha256'] == measurement_hash, 'Supplemental measurement lineage differs')
        counts, xml_hash = validate_stage(extra['stage'], extra_folder / 'command', 30, extra['classes'])
        require(counts == extra['stage_counts'] and xml_hash == extra['coverage_sha256'], 'Supplemental stage evidence differs')
        coverage_files.append(extra_folder / 'command/coverage.xml')
    oracle = read_json(folder / 'fixture-oracle-review.json')
    observations = read_json(folder / 'generation/observations.json')
    retained = [case for case in observations if case['retained']]
    require(oracle['suite_sha256'] == row['suite_sha256']
            and oracle['observations_sha256'] == sha256(folder / 'generation/observations.json')
            and oracle['fixed_source_sha256'] == generated['fixed_source_sha256'] == review['fixed_source_sha256'],
            'Oracle/observation/source binding differs')
    require(len(retained) == len(oracle['cases']) == 30 and len({c['case_id'] for c in retained}) == 30,
            'Retained case count/identity differs')
    selected = read_json(folder / 'setup/targets.fixture-policy.json')['targets']
    covered = {(cls.get('name'), method.get('name'), method.get('signature', '').split(')')[0] + ')')
        for xml in coverage_files for cls in ET.parse(xml).getroot().iter('class') for method in cls.findall('./methods/method')
        if any(int(line.get('hits','0')) > 0 for line in method.findall('./lines/line'))}
    for observation, case in zip(retained, oracle['cases']):
        target, first = observation['target'], observation['fixed_first']
        outcome = first['outcome']
        allowed = expected_exception(target, outcome)
        require(case['case_id'] == observation['case_id'] and case['target'] == target and case['outcome'] == outcome,
                'Reviewed case differs from retained observation')
        derived = {'two_fixed_observations_agree':first == observation['fixed_second'],
            'target_invoked':first.get('target_invoked') is True, 'declared_recipe_selected':target in selected,
            'target_method_covered':(declaring_class(target), target['method'], descriptor(target['parameter_types'])) in covered,
            'no_type_only_or_constructor_only_oracle':'object-type:' not in outcome and not outcome.startswith('constructed:'),
            'no_stateless_null_or_void_oracle':outcome not in {'value:null|state=stateless-scalars','void|state=stateless-scalars'},
            'expected_boundary_exception':allowed, 'unexpected_exception':outcome.startswith('exception:') and not allowed}
        require(all(case[k] == value for k,value in derived.items()), 'Local semantic evidence disagrees with observations/XML')
        require(all(derived[k] for k in derived if k not in {'expected_boundary_exception','unexpected_exception'})
                and not derived['unexpected_exception'], 'Local development case is invalid')
    return {'project':row['project'],'bug_id':row['bug_id'],'approach':row['approach'],
        'suite_sha256':row['suite_sha256'],'protocol_sha256':protocol_hash,'local_development_verdict':'valid',
        'reviewed_cases':30,'original_usable':False,'team_or_primary_approval':False}


def inspect():
    combined_folder = EVIDENCE / 'beam-pilot-v5-repair-handoff-20261003'
    combined = read_json(combined_folder / 'index.json')
    total_checksums = verify_packet(combined_folder)
    packets, packet_counts, runtime_counts = {}, {}, {}
    for ref in combined['packet_refs']:
        folder = contained(EVIDENCE, ref['packet'])
        for name, field in [('index.json','index_sha256'),('semantic-review-index.json','review_index_sha256'),('checksums.json','checksums_sha256')]:
            require(sha256(folder / name) == ref[field], 'Combined packet reference differs')
        packet_counts[ref['packet']] = verify_packet(folder)
        total_checksums += packet_counts[ref['packet']]
        index = read_json(folder / 'index.json')
        protocol = read_json(folder / 'protocol-proposal.json')
        require(sha256(folder / 'protocol-proposal.json') == index['protocol_sha256'], 'Received protocol differs')
        for name, expected in protocol['source_sha256'].items():
            require(sha256(contained(folder / 'runtime-implementation', name)) == expected, 'Received execution runtime differs')
        runtime_counts[ref['packet']] = len(protocol['source_sha256'])
        require(index['exporter_sha256'] == sha256(folder / 'review-implementation/export_fixture_evidence.py')
                and read_json(folder / 'semantic-review-index.json')['reviewer_sha256'] == sha256(folder / 'review-implementation/fixture_semantics.py'),
                'Retained exporter/reviewer pins differ')
        packets[ref['packet']] = (folder, {(r['project'],r['bug_id'],r['approach']):r for r in index['records']})
    rows, identities = [], set()
    for row in combined['records']:
        identity = (row['project'],row['bug_id'],row['approach'])
        require(identity not in identities, 'Duplicate combined identity')
        identities.add(identity)
        packet, records = packets[row['packet']]
        verified = verify_suite(packet, records[identity])
        require(all(row[k] == verified[k] for k in ('suite_sha256','protocol_sha256','local_development_verdict')),
                'Combined suite binding differs')
        rows.append({'packet':row['packet'],**verified})
    require(len(rows) == 30 and len({(r['project'],r['bug_id']) for r in rows}) == 15, 'Combined cohort differs')
    first_folder = EVIDENCE / 'beam-pilot-v5-review-20261003'
    first = read_json(first_folder / 'index.json')
    first_outcomes = Counter(r['evaluation_outcome'] for r in first['records'])
    require(first_outcomes == {'complete':26,'environment_failed':2,'fixed_failed':2}, 'First-round outcomes changed')
    repair_name = 'beam-pilot-v5-repair-review-20261003'
    repair = read_json(EVIDENCE / repair_name / 'index.json')
    require(len(repair['records']) == 6 and all(r['evaluation_outcome'] == 'complete' for r in repair['records']), 'Repair cohort differs')
    cli = read_json(combined_folder / 'environment-repair-cli-v5/repair.json')
    require(cli['before_sha256'] == sha256(combined_folder / 'environment-repair-cli-v5/Cli.build.original.xml')
            and cli['after_sha256'] == sha256(combined_folder / 'environment-repair-cli-v5/Cli.build.patched.xml'), 'CLI framework repair differs')
    candidate_path = ROOT / 'output/api854-20261003/prepare-v7-twenty-bug-development/index.json'
    candidate = read_json(candidate_path)
    require(candidate['runtime_source_sha256'] == implementation_hashes()
            and candidate['policy_sha256'] == digest(encoded(POLICY_V7)), 'Current candidate source/policy binding differs')
    old_pins = read_json(EVIDENCE / repair_name / 'protocol-proposal.json')['source_sha256']
    current = implementation_hashes()
    return {'checked_at_utc':datetime.now(timezone.utc).isoformat(), 'received_champ_commit':'7e09a5feb32772a6d4c2921c804decabba0c1ab1',
        'received_beam_commit':'3ae6f2fbffe007c37c248520f60b534e09bcd6c0', 'checksum_entries_verified':total_checksums,
        'packet_checksum_entries':packet_counts, 'retained_execution_runtime_counts':runtime_counts,
        'combined_index_sha256':sha256(combined_folder / 'index.json'), 'combined_suites':rows,
        'combined_bugs':15, 'combined_local_valid_suites':30, 'local_cases_rechecked_from_observations_and_xml':900,
        'repair_suites':6, 'first_round_outcomes_retained':dict(first_outcomes),
        'candidate_preparation_index_sha256':sha256(candidate_path), 'candidate_bugs':20,
        'candidate_selected_declarations':candidate['target_count'], 'candidate_unsupported_declarations':candidate['capability_exclusion_count'],
        'candidate_max_prompt_utf8_bytes':candidate['max_prompt_utf8_bytes'],
        'repair_execution_runtime_differences':{name:{'received':value,'current':current.get(name)} for name,value in old_pins.items() if current.get(name)!=value},
        'review_implementation_sha256':sha256(__file__), 'local_semantic_helper_sha256':sha256(ROOT / 'scripts/study/api854/fixture_semantics.py'),
        'scope':'Received file/observation/XML audit; no Defects4J rerun, final joint semantic approval or primary experiment.',
        'gate_a_passed':False,'generation_authorized':False,'primary_completed':0,'live_requests':0,'queue_mutations':0}


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--output', type=Path, required=True)
    args = parser.parse_args()
    result = inspect()
    write_json(args.output, result)
    print(json.dumps({k:result[k] for k in ('checksum_entries_verified','combined_bugs','combined_local_valid_suites','repair_suites')}))
