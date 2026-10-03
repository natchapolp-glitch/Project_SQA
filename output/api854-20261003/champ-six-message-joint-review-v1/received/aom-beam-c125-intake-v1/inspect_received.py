"""Read immutable Git objects; receive development evidence without adopting recipes.

Run from the repository root. No API calls, queue operations or runtime edits.
This is an intake producer, not a semantic/team acceptance authority.
"""
import argparse
import hashlib
import io
import json
from pathlib import Path
import subprocess
import tarfile
import types
import xml.etree.ElementTree as ET

BEAM = 'c125695a13620c7d08ba1f344c6ac287e512ea22'
CHAMP = 'da878b54'
AOM = 'ff02b94f'
PACKET = 'docs/api854/evidence/beam-buffer-development-20261003-v1'
REFERENCE = 'docs/api854/evidence/beam-buffer-reference-20261003-v1'
V9 = 'output/api854-20261003/prepare-v9-twenty-bug-development'
FIELDS = ('project', 'bug_id', 'class', 'constructor_types', 'method', 'parameter_types')


def require(condition, message):
    if not condition:
        raise ValueError(message)


def digest(data):
    return hashlib.sha256(data).hexdigest()


class Objects:
    def __init__(self, revision):
        self.revision = subprocess.check_output(['git', 'rev-parse', revision], text=True).strip()
        self.bindings = {}

    def blob(self, path):
        data = subprocess.check_output(['git', 'show', f'{self.revision}:{path}'])
        self.bindings[path] = digest(data)
        return data

    def json(self, path):
        return json.loads(self.blob(path))

    def checksums(self, directory):
        hashes = self.json(directory + '/checksums.json')
        for name, expected in hashes.items():
            require(not Path(name).is_absolute() and '..' not in Path(name).parts,
                    'Unsafe checksum member')
            require(digest(self.blob(directory + '/' + name)) == expected,
                    'Checksum mismatch: ' + directory + '/' + name)
        return len(hashes)


def identity(target, project=None, bug_id=None):
    scoped = dict(target)
    if project is not None:
        scoped.update(project=project, bug_id=bug_id)
    return tuple(scoped[field] for field in FIELDS)


def run(output):
    output = Path(output)
    require(not output.exists(), 'Refuse to overwrite a sealed intake')
    beam, champ, aom = Objects(BEAM), Objects(CHAMP), Objects(AOM)
    counts = [beam.checksums(directory) for directory in (PACKET, REFERENCE)]
    require(counts == [191, 2], 'Unexpected packet sizes')
    protocol = beam.json(PACKET + '/protocol-proposal.json')
    pins = protocol['source_sha256']
    require(len(pins) == 41, 'Expected 41 runtime pins')
    for path, expected in pins.items():
        require(digest(beam.blob(path)) == expected, 'Beam runtime drift: ' + path)
        require(digest(beam.blob(PACKET + '/runtime-implementation/' + path)) == expected,
                'Packaged runtime drift: ' + path)
    index = beam.json(PACKET + '/index.json')
    require(index['protocol_sha256'] == digest(beam.blob(PACKET + '/protocol-proposal.json')),
            'Development protocol binding')
    require(index['team_or_primary_approval'] is False and len(index['records']) == 4,
            'Development scope')
    policy = types.ModuleType('received_fixture_policy')
    exec(compile(beam.blob('scripts/study/api854/fixture_policy.py'),
                 'received fixture_policy.py', 'exec'), policy.__dict__)
    reference = beam.json(REFERENCE + '/review.json')
    require(reference['source_sha256'] == pins and reference['team_or_primary_approval'] is False,
            'Reference runtime/scope')
    require(reference['reviewer_sha256'] == digest(beam.blob(REFERENCE + '/buffer_fixture_review.py')),
            'Reference producer binding')
    require(len(reference['cases']) == 32, 'Reference examples')
    examples = {}
    for row in reference['cases']:
        target = identity(row['target'], row['project'], row['bug_id'])
        examples.setdefault(target, []).append(row['example_id'])
        require(row['fixed_first'] == row['fixed_second'], 'Unstable reference')
        observation = row['fixed_first']
        require(observation['status'] == 'ok' and observation['target_invoked'] is True
                and observation['outcome'] == row['expected'] and row['reference_check_passed'] is True,
                'Reference failure')
    require(len(examples) == 8 and all(sorted(v) == [1, 2, 3, 4] for v in examples.values()),
            'Not four examples per exact declaration')
    inventory_index = beam.json('docs/api854/evidence/beam-aom-review-20261003/index.json')
    inventory = []
    for row in inventory_index['bugs']:
        inventory.extend(dict(t, project=row['project'], bug_id=row['bug_id']) for t in
                         beam.json('docs/api854/evidence/beam-aom-review-20261003/declarations/'
                         + f"{row['project']}-{row['bug_id']}/targets.json")['targets'])
    require(len(inventory) == 691 and len({identity(t) for t in inventory}) == 691,
            'Common inventory')
    old, _ = policy.select(inventory, policy.POLICY_V5)
    new, excluded = policy.select(inventory, policy.POLICY_V6_BUFFER)
    old_ids, new_ids = {identity(t) for t in old}, {identity(t) for t in new}
    added = new_ids - old_ids
    require((len(old), len(new), len(excluded)) == (377, 385, 306) and old_ids <= new_ids,
            'Beam selection totals/regression')
    require(added == set(examples), 'Reference does not cover exactly the added declarations')
    v9_index = champ.json(V9 + '/index.json')
    v9_ids = set()
    v9_sources = {}
    for row in v9_index['records']:
        bug = f"{row['project']}-{row['bug_id']}"
        targets = champ.json(V9 + '/' + bug + '/targets.json')['targets']
        v9_ids.update(identity(t, row['project'], row['bug_id']) for t in targets)
        for path, expected in row['fixed_source_sha256'].items():
            if row['project'] in reference['fixed_source_sha256']:
                require(digest(champ.blob(V9 + '/' + bug + '/fixed-source/' + path)) == expected,
                        'Retained fixed source binding')
        v9_sources[row['project']] = row['fixed_source_sha256']
    require(len(v9_ids) == 380 and old_ids <= v9_ids and not (added & v9_ids),
            'Champ v9 composition delta')
    for project, hashes in reference['fixed_source_sha256'].items():
        require(hashes == v9_sources[project], 'Buffer source differs from shared fixed source')
    suites, invoked = [], set()
    for record in index['records']:
        directory = PACKET + f"/{record['project']}-1-{record['approach']}"
        result = beam.json(directory + '/original-evaluation-result.json')
        review = beam.json(directory + '/semantic-review-result.json')
        measurement = result['measurement']
        require(result['usable'] is False and measurement['fault_detected'] is False,
                'Preserve original measured result')
        require(review['evaluation_result_sha256'] == digest(beam.blob(directory + '/original-evaluation-result.json'))
                and review['status'] == 'valid'
                and review['review']['fixture_oracle_review']['team_or_primary_approval'] is False,
                'Separate local semantic supplement')
        suite = beam.blob(directory + '/suite.tar.bz2')
        require(digest(suite) == result['suite_sha256'] == record['suite_sha256'], 'Suite binding')
        with tarfile.open(fileobj=io.BytesIO(suite)) as archive:
            java = [archive.extractfile(m).read() for m in archive.getmembers()
                    if m.isfile() and m.name.endswith('GeneratedStudyTest.java')]
        require(java == [beam.blob(directory + '/generation/GeneratedStudyTest.java')],
                'Packaged generated source differs')
        for stage in ('fixed-1', 'fixed-2', 'buggy', 'coverage'):
            counters = beam.json(directory + '/' + stage + '/sqa-stage-counts.json')
            require((counters['executed'], counters['skipped'], counters['target_checks']) == (30, 0, 30),
                    'Stage counters')
            command = beam.json(directory + '/' + stage + '/command.json')
            require(command['exit_code'] == 0 and command['timed_out'] is False, 'Stage command failed')
            require(not beam.blob(directory + '/' + stage + '/failing_tests').strip(), 'Failed tests')
            require(len(beam.blob(directory + '/' + stage + '/all_tests').splitlines()) == 30,
                    'Raw test enumeration')
        xml = ET.fromstring(beam.blob(directory + '/coverage/coverage.xml'))
        coverage = {key: int(xml.attrib[attr]) for key, attr in
                    [('line_covered', 'lines-covered'), ('line_total', 'lines-valid'),
                     ('branch_covered', 'branches-covered'), ('branch_total', 'branches-valid')]}
        require(all(measurement[k] == v == record[k] for k, v in coverage.items()),
                'Coverage XML disagrees with result')
        observations = beam.json(directory + '/generation/observations.json')
        require(len(observations) == 30, 'Generation observations')
        for row in observations:
            require(row['retained'] is True and row['stable'] is True
                    and row['fixed_first'] == row['fixed_second']
                    and row['fixed_first']['status'] == 'ok'
                    and row['fixed_first']['target_invoked'] is True, 'Retained observation failure')
            invoked.add(identity(row['target'], record['project'], record['bug_id']))
        suites.append({'bug': f"{record['project']}-1", 'approach': record['approach'],
                       'suite_sha256': digest(suite), 'original_usable': False,
                       'local_semantic_review': 'valid', 'team_approval': False,
                       'fault_detected': False, 'stage_executed': 30,
                       'stage_skipped': 0, 'stage_target_checks': 30, **coverage})
    require(added <= invoked, 'New declaration absent from sampled suites')
    tests = beam.json('output/api854-20261003/beam-buffer-test-receipt-v1.json')
    for command in tests['commands']:
        require(digest(beam.blob('output/api854-20261003/' + command['log'])) == command['log_sha256'],
                'Received test log binding')
    historical = 'output/api854-20261003/prepare-v7-twenty-bug-development'
    # The v7 index may use a different directory; derive it from the existing tree.
    candidates = subprocess.check_output(['git', 'ls-tree', '-r', '--name-only', aom.revision], text=True).splitlines()
    v7_indexes = [p for p in candidates if '/prepare-v7' in p and p.endswith('/index.json')]
    require(len(v7_indexes) == 1, 'Historical v7 index identity')
    historical = str(Path(v7_indexes[0]).parent).replace('\\', '/')
    v7_files = [p for p in candidates if p.startswith(historical + '/')]
    require(all(digest(aom.blob(p)) == digest((Path.cwd() / p).read_bytes()) for p in v7_files),
            'Local historical v7 changed')
    changed_vs_champ = [path for path, value in pins.items() if digest(champ.blob(path)) != value]
    changed_vs_aom = [path for path, value in pins.items() if digest(aom.blob(path)) != value]
    receipt = {'schema_version': 1, 'beam_commit': beam.revision, 'champ_commit': champ.revision,
        'aom_base_commit': aom.revision, 'checksum_entries_verified': sum(counts),
        'runtime_pins_verified': 41, 'fixed_reference_declarations': 8,
        'fixed_reference_examples': 32, 'received_fixed_observations': 64,
        'aom_defects4j_reruns': 0, 'suites': suites,
        'beam_condition': {'policy': policy.POLICY_V6_BUFFER, 'selected': 385, 'unsupported': 306},
        'champ_v9_condition': {'selected': 380, 'unsupported': 311, 'max_prompt_bytes': v9_index['max_prompt_utf8_bytes']},
        'candidate_union_if_accepted': {'selected': len(v9_ids | added), 'unsupported': 691-len(v9_ids | added),
                                       'implemented': False, 'measured': False},
        'added_targets': [dict(zip(FIELDS, target)) for target in sorted(added)],
        'v9_targets_missing_from_beam_buffer': [dict(zip(FIELDS, target)) for target in sorted(v9_ids-new_ids)],
        'runtime_files_different_from_champ_v9': changed_vs_champ,
        'runtime_files_different_from_aom_v8': changed_vs_aom,
        'historical_v7_files_unchanged': len(v7_files),
        'status': 'received_integrity_verified_joint_buffer_acceptance_pending',
        'new_shared_preparation_created': False, 'final_prompt_reserve': None,
        'gate_a_approved': False, 'primary': False, 'real_kku_requests': 0, 'live_queue_mutations': 0,
        'limitations': ['Received evidence checked; no new Defects4J experiments claimed.',
          'Bounded examples and four suites do not validate every declaration or full input domain.',
          'Beam v6 buffer is not a drop-in replacement for the accepted v9 setter/JDOM/Math recipes.',
          'Await Champ verdict on exact buffer signatures and changed Csv stream condition before composition.'],
        'inspector_sha256': digest(Path(__file__).read_bytes())}
    output.mkdir(parents=True, exist_ok=False)
    for name, data in [('receipt.json', receipt), ('received-object-sha256.json',
                       {obj.revision: obj.bindings for obj in (beam, champ, aom)})]:
        (output / name).write_text(json.dumps(data, indent=2, ensure_ascii=False)+'\n', encoding='utf-8')
    print(json.dumps({k: receipt[k] for k in ('status', 'checksum_entries_verified',
                      'fixed_reference_examples', 'candidate_union_if_accepted', 'historical_v7_files_unchanged')}, indent=2))
    return receipt


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--output', required=True)
    run(parser.parse_args().output)
