"""Audit Beam's immutable fixture/eligibility packet without rerunning experiments."""
import argparse
import datetime
import json
import tarfile
from pathlib import Path

from .common import ROOT, contained, read_json, sha256
from .preparation import clean_targets, encoded


def packet_checksums(folder):
    checksums = read_json(folder / 'checksums.json')
    actual = {p.relative_to(folder).as_posix() for p in folder.rglob('*')
              if p.is_file() and p != folder / 'checksums.json'}
    if actual != set(checksums):
        raise ValueError('Checksum file coverage differs')
    for name, expected in checksums.items():
        if sha256(contained(folder, name)) != expected:
            raise ValueError('Packet checksum differs: ' + name)
    return len(checksums)


def inspect(root=ROOT):
    root = Path(root)
    packet = root / 'docs/api854/evidence/beam-fixture-review-20261003'
    imports = root / 'docs/api854/evidence/beam-aom-eligibility-import-20261003'
    counts = {'fixture_packet':packet_checksums(packet), 'eligibility_packet':packet_checksums(imports)}
    protocol = read_json(packet / 'protocol-proposal.json')
    index = read_json(packet / 'index.json')
    if sha256(packet / 'protocol-proposal.json') != index['protocol_sha256']:
        raise ValueError('Development protocol differs')
    for name, expected in protocol['source_sha256'].items():
        if sha256(contained(packet / 'runtime-implementation', name)) != expected:
            raise ValueError('Retained execution source differs: ' + name)
    v3 = root / 'output/api854-20261003/prepare-v3'
    matched, targets, excluded = 0, 0, 0
    for row in read_json(v3 / 'index.json')['records']:
        name = f"{row['project']}-{row['bug_id']}"
        receipt = read_json(imports / name / 'eligibility.json')
        metadata = read_json(v3 / name / 'prepare-metadata.json')
        inventory = read_json(v3 / name / 'targets.json')
        old_receipt = read_json(v3 / name / 'eligibility.json')
        if (any(receipt[k] != row[k] for k in ('project','bug_id','owner'))
                or receipt['fixed_source_sha256'] != metadata['fixed_source_sha256']
                or clean_targets(receipt['targets']) != inventory['targets']
                or clean_targets(receipt['excluded_fixed_only']) != clean_targets(old_receipt['excluded_fixed_only'])
                or receipt['fixture_oracle_approval'] is not False):
            raise ValueError('Eligibility differs from shared v3 or claims semantic approval')
        matched += 1
        targets += len(receipt['targets'])
        excluded += len(receipt['excluded_fixed_only'])
    records = []
    expected_identities = {('Closure',176,a) for a in ('cmaes','fscs-art')} | {('JxPath',1,a) for a in ('cmaes','fscs-art')}
    if {(r['project'],r['bug_id'],r['approach']) for r in index['records']} != expected_identities:
        raise ValueError('Require four retained development suites')
    for row in index['records']:
        folder = packet / f"{row['project']}-{row['bug_id']}-{row['approach']}"
        result = read_json(folder / 'original-evaluation-result.json')
        review = read_json(folder / 'semantic-review-result.json')
        measurement = result['measurement']
        if (sha256(folder / 'suite.tar.bz2') != row['suite_sha256']
                or result['suite_sha256'] != row['suite_sha256']
                or result['usable'] is not False or row['semantic_review']['team_or_primary_approval'] is not False
                or review['evaluation_result_sha256'] != sha256(folder / 'original-evaluation-result.json')
                or review['status'] != 'valid' or measurement['fixed_validation'] != 'passed_twice'):
            raise ValueError('Suite/result/review scope differs')
        with tarfile.open(folder / 'suite.tar.bz2','r:bz2') as archive:
            sources = [archive.extractfile(member).read() for member in archive.getmembers() if member.isfile() and member.name.endswith('.java')]
        if sources != [(folder / 'generation/GeneratedStudyTest.java').read_bytes()]:
            raise ValueError('Retained generated suite bytes differ')
        for stage in ('fixed-1','fixed-2','buggy','coverage'):
            counts_stage = read_json(folder / stage / 'sqa-stage-counts.json')
            if (counts_stage != row['stage_counts'][stage] or
                    any(counts_stage[k] != v for k,v in {'executed':30,'skipped':0,'target_checks':30}.items())):
                raise ValueError('Actual stage counters differ')
        for stage in ('fixed-1','fixed-2'):
            if measurement['stages'][stage]['failure_count'] != 0 or (folder / stage / 'failing_tests').stat().st_size:
                raise ValueError('Fixed validation did not pass')
        if measurement['stages']['buggy']['failure_count'] != row['buggy_failure_count']:
            raise ValueError('Buggy failures differ')
        for key in ('fault_detected','line_covered','line_total','branch_covered','branch_total'):
            if measurement[key] != row[key]:
                raise ValueError('Measurement differs from summary: ' + key)
        records.append({k:row[k] for k in ('project','bug_id','approach','buggy_failure_count','fault_detected',
            'line_covered','line_total','branch_covered','branch_total','suite_sha256')})
    return {'schema_version':1, 'checked_at_utc':datetime.datetime.now(datetime.timezone.utc).isoformat(),
        'beam_commit':'3ff1a6a415189d8f3944bf0723b2628b3b1f8d71', 'checksum_files_verified':counts,
        'execution_protocol_sha256':index['protocol_sha256'], 'retained_runtime_files_verified':len(protocol['source_sha256']),
        'source_matched_bugs':matched, 'shared_target_count':targets, 'fixed_only_exclusions':excluded,
        'eligibility_matches_v3':True, 'development_suites':records,
        'semantic_review_scope':'Beam local development only; received and audited, not team/primary approval',
        'shared_runtime_execution_repeated':False, 'recipe_reviewed_bugs':2, 'remaining_pilot_bugs_without_recipe_review':18,
        'gate_a_approved':False, 'live_requests':0, 'queue_mutations':0,
        'packet_checksums_sha256':sha256(packet / 'checksums.json'),
        'eligibility_checksums_sha256':sha256(imports / 'checksums.json')}


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--output',type=Path,required=True)
    args = parser.parse_args()
    result = inspect()
    args.output.parent.mkdir(parents=True,exist_ok=True)
    with args.output.open('xb') as stream:
        stream.write(encoded(result))
    print(json.dumps({k:result[k] for k in ('checksum_files_verified','source_matched_bugs','shared_target_count','recipe_reviewed_bugs','gate_a_approved')}))


if __name__ == '__main__':
    main()
