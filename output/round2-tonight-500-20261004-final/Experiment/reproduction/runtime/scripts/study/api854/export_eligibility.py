"""Convert retained declaration evidence to Aom's source-bound builder input.

This exports the historical common-production-types discovery policy. It does
not relabel it as the new explicit recipe policy or certify semantic validity.
"""
import argparse
from pathlib import Path

from .common import read_json, sha256, write_json, contained

FIELDS = ('class', 'constructor_types', 'method', 'parameter_types')
DISCOVERY_POLICY = 'common-fixed-buggy-production-types-v1'


def identities(targets):
    keys = [tuple(target[field] for field in FIELDS) for target in targets]
    if len(set(keys)) != len(keys) or any(not all(isinstance(value, str) for value in key) for key in keys):
        raise ValueError('Malformed or duplicated declaration signature')
    return set(keys)


def convert_record(packet, row):
    name = f"{row['project']}-{row['bug_id']}"
    review_path = packet / 'review' / f'{name}.json'
    review = read_json(review_path)
    if any(review.get(key) != row[key] for key in ('project', 'bug_id', 'owner')):
        raise ValueError('Pilot identity differs from retained review')
    artifacts, eligibility = review['artifacts'], review['eligibility']
    if (artifacts['integrity'] != 'passed' or eligibility['status'] != 'declaration_intersection_verified'
            or eligibility['fixed_java_matches_aom'] is not True
            or eligibility['common_fixture_class_intersection_verified'] is not True):
        raise ValueError('Require independently verified declaration/source/class intersection')
    declaration = packet / 'declarations' / name
    fixed = read_json(declaration / 'targets.fixed.json')['targets']
    buggy = read_json(declaration / 'targets.buggy.json')['targets']
    common = read_json(declaration / 'targets.json')
    targets, excluded = common['targets'], common['excluded_fixed_only']
    if (identities(targets) != identities(fixed) & identities(buggy)
            or identities(excluded) != identities(fixed) - identities(buggy)
            or identities(eligibility['eligible_targets']) != identities(targets)
            or identities(eligibility['excluded_fixed_only']) != identities(excluded)):
        raise ValueError('Retained declarations differ from fixed/buggy signature sets')
    clean = lambda items: sorted(({field: target[field] for field in FIELDS} for target in items),
                                 key=lambda target: tuple(target[field] for field in FIELDS))
    return {'schema_version': 1, **row, 'fixed_source_sha256': artifacts['fixed_source_sha256'],
        'target_selection': 'shared-declaration-signatures-v1', 'fixture_policy': DISCOVERY_POLICY,
        'targets': clean(targets), 'excluded_fixed_only': clean(excluded),
        'scope': 'Declaration-only import for Aom builder; not runtime fixture/oracle approval',
        'semantic_validity': 'pending_review', 'fixture_oracle_approval': False, 'usable': False,
        'provenance': {'beam_handoff_commit': '44dd5cb0', 'beam_review_sha256': sha256(review_path),
            'retained_evidence_sha256': {filename: sha256(declaration / filename) for filename in
                ('targets.fixed.json', 'targets.buggy.json', 'targets.json', 'fixture-classes.txt')},
            'recorded_fixed_commit': artifacts['recorded_fixed_commit'],
            'cross_host_git_commit_identity_claimed': False,
            'new_explicit_fixture_policy_adopted': False}}


def export(packet, output):
    packet, output = Path(packet), Path(output)
    checksums = read_json(packet / 'checksums.json')
    actual = {p.relative_to(packet).as_posix() for p in packet.rglob('*') if p.is_file() and p != packet / 'checksums.json'}
    if actual != set(checksums):
        raise ValueError('Input packet file coverage differs')
    for relative, expected in checksums.items():
        if sha256(contained(packet, Path(relative))) != expected:
            raise ValueError('Input packet checksum differs: ' + relative)
    index = read_json(packet / 'index.json')
    records = [convert_record(packet, row) for row in index['bugs']]
    if len(records) != 20 or len({(r['project'], r['bug_id']) for r in records}) != 20:
        raise ValueError('Require all 20 pilot identities')
    output.mkdir(parents=True, exist_ok=False)
    for record in records:
        folder = output / f"{record['project']}-{record['bug_id']}"
        folder.mkdir()
        write_json(folder / 'eligibility.json', record)
    summary = {'schema_version': 1, 'input_packet_checksums_sha256': sha256(packet / 'checksums.json'),
        'converter_sha256': sha256(__file__), 'fixture_policy': DISCOVERY_POLICY,
        'bug_count': len(records), 'target_count': sum(len(r['targets']) for r in records),
        'fixed_only_exclusion_count': sum(len(r['excluded_fixed_only']) for r in records),
        'fixture_oracle_approval': False, 'primary': False,
        'real_kku_requests': 0, 'live_queue_mutations': 0}
    write_json(output / 'index.json', summary)
    write_json(output / 'checksums.json', {p.relative_to(output).as_posix(): sha256(p) for p in sorted(output.rglob('*')) if p.is_file()})
    return summary


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--packet', required=True, type=Path)
    parser.add_argument('--output', required=True, type=Path)
    args = parser.parse_args()
    result = export(args.packet, args.output)
    print(f"Exported {result['bug_count']} source-bound inventories / {result['target_count']} declarations; semantic approval pending")


if __name__ == '__main__':
    main()
