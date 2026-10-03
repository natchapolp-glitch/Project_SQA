"""Return scoped joint recipe decisions from pinned peer commits, entirely offline.

This authorizes bounded prospective composition, not a final shared condition.
Received inspectors run on temporary archives; old sealed packets stay unchanged.
"""
import argparse
import copy
from datetime import datetime, timezone
import json
from pathlib import Path
import re
import subprocess
import sys
import tempfile

from .common import ROOT, implementation_hashes, read_json, sha256, write_json
from .review_joint_recipe_intake import (
    GitObjects, BUFFER, LANG, V9, FIELDS, check_stages, digest, execute,
    extract_archive, method_entry, require, scalar,
)
from .verify_chronology_development import checkpoint

BEAM = '477b4f8a'
AOM = 'f753770d'
CHAMP = '1a28deea'
PREVIOUS = 'output/api854-20261003/champ-six-message-joint-review-v3'
RETURNS = 'output/api854-20261003/beam-final-recipe-return-v1'
CHRONOLOGY = 'output/api854-20261003/beam-chronology-review-v1'
AOM_BUFFER = 'output/api854-20261003/aom-beam-buffer-verdict-intake-v1'
DESCRIPTORS = {'isAllZeros': '(Ljava/lang/String;)Z', 'validateArray': '(Ljava/lang/Object;)V'}
INPUTS = {'isAllZeros': [None, '', '0', '000', '001', '12', '00 0', '-0'],
          'validateArray': [None, [], [0], [-1, 0, 7]]}


def identity(target):
    return tuple(target[field] for field in FIELDS)


def binding(objects, path):
    return {'commit': objects.commit, 'path': path, 'sha256': digest(objects.blob(path))}


def lang_oracle(row):
    target, value = row['target'], row['declared_input']['input']
    require(target['class'] == 'org.apache.commons.lang3.math.NumberUtils'
            and target['constructor_types'] == '', 'Wrong private helper receiver/class')
    name = target['method']
    if name == 'isAllZeros':
        require(target['parameter_types'] == 'java.lang.String'
                and (value is None or isinstance(value, str)), 'Wrong String overload/domain')
        answer = value is None or (bool(value) and all(c == '0' for c in value))
        require(row['declared_input']['expected_boolean'] is answer, 'Declared Boolean oracle differs')
        return 'value:' + scalar('java.lang.Boolean', str(answer).lower()) + '|state=stateless-scalars'
    require(name == 'validateArray' and target['parameter_types'] == 'java.lang.Object'
            and (value is None or isinstance(value, list)
                 and all(type(v) is int for v in value)), 'Wrong Object overload/int[] domain')
    state = 'null' if value is None else '[I[' + ''.join(scalar('java.lang.Integer', v) + ';' for v in value) + ']'
    message = 'The Array must not be null' if value is None else 'Array cannot be empty.' if not value else None
    require(row['declared_input']['expected_message'] == message, 'Declared exception message differs')
    result = ('exception:java.lang.IllegalArgumentException|message=' + scalar('java.lang.String', message)
              if message else 'void')
    return result + '|state=validation-input:' + state


def validate_lang(beam, champ, observations):
    require(len(observations) == 12 and [r['case_id'] for r in observations] == list(range(1, 13)),
            'Lang case inventory differs')
    for name, values in INPUTS.items():
        cases = [r for r in observations if r['target']['method'] == name]
        require([r['declared_input']['input'] for r in cases] == values, 'Bounded helper inputs differ')
        for row in cases:
            expected = lang_oracle(row)
            require(row['fixed_first'] == row['fixed_second']
                    and row['fixed_first'] == {'status': 'ok', 'outcome': expected, 'target_invoked': True}
                    and row['expected'] == expected and row['stable'] is True
                    and row['retained'] is True and row['reference_check_passed'] is True,
                    'Lang independent value/state oracle or invocation differs')
    require(len(beam['candidates']) == len(champ['candidates']) == 2, 'Lang signature count differs')
    previous = {identity(r['target']): r for r in champ['candidates']}
    require({identity(r['target']) for r in beam['candidates']} == set(previous), 'Lang exact signatures differ')
    for row in beam['candidates']:
        name = row['target']['method']
        require(row['target']['project'] == 'Lang' and row['target']['bug_id'] == 1
                and row['exact_jvm_descriptor'] == DESCRIPTORS[name], 'Lang exact descriptor differs')
        require(row['beam_joint_verdict'] == 'accepted_for_bounded_prospective_development_composition'
                and row['champ_joint_verdict'] is None and row['accepted_into_shared_inputs'] is False,
                'Need an explicit scoped Beam verdict, without inferred shared approval')
        require(previous[identity(row['target'])]['champ_joint_verdict'] ==
                'accepted_for_prospective_bounded_development_composition', 'Missing prior Champ verdict')
        require(row['fixed_repeated_observations'] ==
                [r for r in observations if r['target']['method'] == name], 'Beam evidence differs from raw observations')


def proposed_union(buffer, lang, chronology):
    index = read_json(ROOT / V9 / 'index.json')
    selected, excluded = set(), set()
    for record in index['records']:
        folder = ROOT / V9 / f"{record['project']}-{record['bug_id']}"
        def key(target):
            return identity(dict(target, project=record['project'], bug_id=record['bug_id']))
        selected.update(key(t) for t in read_json(folder / 'targets.json')['targets'])
        excluded.update(key(r['target']) for r in read_json(folder / 'capability-exclusions.json')['excluded'])
    require(len(selected) == 380 and len(excluded) == 311 and not selected & excluded, 'Shared partition differs')
    groups = {'Buffer/Csv': {identity(r['target']) for r in buffer['candidates']},
              'Lang': {identity(r['target']) for r in lang['candidates']},
              'Chronology': {identity(dict(t, project='Time', bug_id=1)) for t in chronology}}
    require([len(groups[g]) for g in groups] == [8, 2, 6], 'Delta inventory differs')
    additions = set().union(*groups.values())
    require(len(additions) == 16 and additions <= excluded, 'Delta duplicated, selected already, or outside denominator')
    return {'shared_base': 'api854-20261003-twenty-bug-development-v9-integrated',
            'actual_selected': 380, 'actual_unsupported': 311, 'denominator': 691,
            'buffer_and_lang_only': {'selected': 390, 'unsupported': 301},
            'including_chronology_after_shared_invocation_and_oracle_integration':
                {'selected': len(selected | additions), 'unsupported': len(excluded - additions)},
            'identity_deltas': {g: [dict(zip(FIELDS, t)) for t in sorted(targets)] for g, targets in groups.items()},
            'implemented': False, 'full_semantic_coverage_approved': False}


def run(output):
    output = Path(output).resolve()
    require(output.is_relative_to(ROOT / 'output') and not output.exists(), 'Require a new repository output path')
    pins, chronology_identities = checkpoint()
    before = implementation_hashes()
    output.mkdir(parents=True, exist_ok=False)
    beam, aom, champ = GitObjects(BEAM), GitObjects(AOM), GitObjects(CHAMP)
    counts = {'previous_champ': champ.checksums(PREVIOUS),
              'beam_final_returns': beam.checksums(RETURNS),
              'beam_chronology': beam.checksums(CHRONOLOGY),
              'aom_buffer': aom.checksums(AOM_BUFFER)}
    for path in champ.bindings:
        require(sha256(ROOT / path) == champ.bindings[path], 'Prior sealed Champ bytes changed: ' + path)
    received = output / 'received'
    received.mkdir()
    received_paths = [(beam, RETURNS + '/final-recipe-return-index.json'),
                      (beam, RETURNS + '/beam-lang-verdict.json'),
                      (beam, CHRONOLOGY + '/beam-chronology-verdict.json'),
                      (aom, AOM_BUFFER + '/champ-buffer-return.template.json'),
                      (aom, AOM_BUFFER + '/inspect_received.py')]
    for objects, path in received_paths:
        (received / Path(path).name).write_bytes(objects.blob(path))
    (output / 'reviewer.py').write_bytes(Path(__file__).read_bytes())
    index = beam.document(RETURNS + '/final-recipe-return-index.json')
    require([r['group'] for r in index['groups']] == ['setter/JDOM', 'Buffer/Csv', 'Lang', 'Math'], 'Return groups differ')
    for row in index['groups']:
        require(binding(beam, row['receipt']['path'])['sha256'] == row['receipt']['sha256'], 'Indexed receipt hash differs')
    # Setter/JDOM and Buffer receipts must still be the exact ones previously reviewed.
    for path in [index['groups'][0]['receipt']['path'], index['groups'][1]['receipt']['path']]:
        require(beam.blob(path) == GitObjects('2e11c7d9').blob(path), 'Previously accepted recipe changed')
    require(index['groups'][3]['receipt']['sha256'] == digest(champ.blob(index['groups'][3]['receipt']['path'])),
            'Prior Math exact acceptance changed')
    adapter = output / 'received-inspector-runner.py'
    adapter.write_text('''import json, runpy, subprocess, sys
script, repository, mode, beam_commit, destination = sys.argv[1:6]
original_output, original_run = subprocess.check_output, subprocess.run
def at_repository(command, *args, **kwargs):
    if isinstance(command, list) and command[0] == 'git':
        command = ['git', '-C', repository, *command[1:]]
    return original_output(command, *args, **kwargs)
subprocess.check_output = at_repository
if mode == 'aom-buffer':
    sys.argv = [script, '--snapshot', '.', '--output', destination]
    runpy.run_path(script, run_name='__main__')
else:
    def pinned_run(command, *args, **kwargs):
        if isinstance(command, list) and command[0] == 'git':
            kwargs['cwd'] = repository
            if 'input' in kwargs:
                kwargs['input'] = kwargs['input'].replace(b'HEAD:', (beam_commit + ':').encode())
        return original_run(command, *args, **kwargs)
    subprocess.run = pinned_run
    module = runpy.run_path(script)
    result = module['audit']()
    with open(destination, 'x', encoding='utf-8', newline='\\n') as stream:
        json.dump(result, stream, indent=2); stream.write('\\n')
    print(json.dumps({k: result[k] for k in ['status', 'received_git_blobs_verified', 'packet_checksum_entries_verified', 'negative_controls_rejected']}))
''', encoding='utf-8', newline='\n')
    with tempfile.TemporaryDirectory(dir=ROOT / 'output', prefix='.champ-final-review-') as temporary:
        snapshot = Path(temporary).resolve()
        archives = {'beam': extract_archive(beam.commit, [CHRONOLOGY, BUFFER, *before,
                    'docs/api854/evidence/beam-buffer-development-20261003-v1',
                    'docs/api854/evidence/beam-buffer-reference-20261003-v1'], snapshot),
                    'aom': extract_archive(aom.commit, ['output/api854-20261003/aom-beam-c125-intake-v1/audit',
                    'output/api854-20261003/prepare-v8-fraction-field-development'], snapshot)}
        aom_command = execute([sys.executable, '-B', adapter, received / 'inspect_received.py', ROOT,
                               'aom-buffer', beam.commit, output / 'aom-buffer-audit'], snapshot, output, 'aom-buffer-review')
        chronology_command = execute([sys.executable, '-B', adapter, snapshot / CHRONOLOGY / 'review_received.py', ROOT,
                                      'chronology', beam.commit, output / 'chronology-audit.json'],
                                     snapshot, output, 'chronology-review')
    chronology_audit = read_json(output / 'chronology-audit.json')
    original_chronology = beam.document(CHRONOLOGY + '/beam-chronology-verdict.json')
    require({k: v for k, v in chronology_audit.items() if k != 'checked_at_utc'} ==
            {k: v for k, v in original_chronology.items() if k != 'checked_at_utc'}, 'Rerun Chronology review differs')
    require(chronology_audit['worklist_identities'] == chronology_identities,
            'Chronology original worklist identities differ')
    lang = beam.document(RETURNS + '/beam-lang-verdict.json')
    previous_lang = champ.document(PREVIOUS + '/champ-lang-verdict.json')
    observations = beam.document(LANG + '/reference-observations.json')
    validate_lang(lang, previous_lang, observations)
    source_path = V9 + '/Lang-1/fixed-source/src/main/java/org/apache/commons/lang3/math/NumberUtils.java'
    fixed_hash = digest(champ.blob(source_path))
    require(lang['fixed_source_sha256'] == previous_lang['fixed_source_sha256']
            and fixed_hash in lang['fixed_source_sha256'].values(), 'Lang fixed source differs')
    source = champ.blob(source_path).decode()
    require(re.search(r'private\s+static\s+boolean\s+isAllZeros\s*\(', source)
            and re.search(r'private\s+static\s+void\s+validateArray\s*\(', source), 'Private helper access changed')
    for manifest in lang['historical_packet_integrity']:
        path = manifest['checksums']['path']
        require(binding(beam, path)['sha256'] == manifest['checksums']['sha256'], 'Lang historical manifest differs')
        require(beam.checksums(str(Path(path).parent).replace('\\', '/')) == manifest['files_checked'], 'Lang checksum count differs')
    check_stages(beam, LANG + '/evaluation', 12)
    coverage = {name: method_entry(beam, LANG + '/evaluation/coverage/coverage.xml',
                                  {'class': 'org.apache.commons.lang3.math.NumberUtils', 'method': name}, descriptor)
                for name, descriptor in DESCRIPTORS.items()}
    require(all(r['entry_hits'] > 0 for r in coverage.values()), 'Lang exact target has no entry evidence')
    common = {'checked_at_utc': datetime.now(timezone.utc).isoformat(), 'champ_review_commit': champ.commit,
              'champ_runtime_source_sha256': before, 'champ_producer_sha256': sha256(__file__),
              'team_or_primary_approval': False, 'gate_a_approved': False, 'pilot_authorized': False,
              'new_shared_preparation_created': False, 'final_prompt_reserve': None,
              'real_kku_requests': 0, 'queue_mutations': 0, 'primary_results_added': 0}
    lang_return = copy.deepcopy(lang)
    lang_return.update(common, reviewer='champ', review_status='beam_champ_scoped_lang_verdict_complete',
                       received_beam_verdict=binding(beam, RETURNS + '/beam-lang-verdict.json'),
                       received_beam_reviewer_commit=lang['reviewer_commit'],
                       received_beam_producer_sha256=lang['producer_sha256'],
                       reviewer_commit=champ.commit, producer_sha256=sha256(__file__),
                       current_runtime_source_sha256=before, joint_acceptance_complete=True,
                       new_shared_preparation_authorized_by_receipt=True,
                       authorization_scope='Bounded private-helper/int[] prospective recipe composition; final shared integration must be tested',
                       exact_target_entry_evidence=coverage)
    prior_by_identity = {identity(r['target']): r for r in previous_lang['candidates']}
    for row in lang_return['candidates']:
        prior = prior_by_identity[identity(row['target'])]
        row.update(champ_joint_verdict=prior['champ_joint_verdict'],
                   agreed_preconditions=row['beam_proposed_preconditions'],
                   agreed_exception_oracle=row['beam_proposed_oracle'])
    write_json(output / 'champ-lang-joint-verdict.json', lang_return)
    template = aom.document(AOM_BUFFER + '/champ-buffer-return.template.json')
    buffer = champ.document(PREVIOUS + '/champ-buffer-verdict.json')
    require(template['beam_verdict_sha256'] == index['groups'][1]['receipt']['sha256'], 'Buffer template binding differs')
    require(template['aom_receipt_sha256'] == digest(aom.blob(AOM_BUFFER + '/audit/receipt.json')), 'Aom audit binding differs')
    buffer_by_identity = {identity(r['target']): r for r in buffer['candidates']}
    require({identity(r['target']) for r in template['candidates']} == set(buffer_by_identity), 'Buffer template signatures differ')
    for row in template['candidates']:
        prior = buffer_by_identity[identity(row['target'])]
        row.update(champ_verdict=prior['champ_verdict'], accepted_preconditions=prior['preconditions'],
                   accepted_oracle=prior['meaningful_oracle'], evidence=prior['evidence'])
    template['csv_stream_condition_change'].update(copy.deepcopy(buffer['csv_stream_condition_change']))
    template.update(common, example_only=False, reviewer='champ', review_status='beam_champ_scoped_buffer_csv_verdict_complete',
                    joint_acceptance=True, new_shared_preparation_authorized_by_receipt=True,
                    authorization_scope='Buffer eight and explicit Csv stream change in a new bounded development condition',
                    prior_champ_verdict=binding(champ, PREVIOUS + '/champ-buffer-verdict.json'),
                    received_aom_template=binding(aom, AOM_BUFFER + '/champ-buffer-return.template.json'),
                    historical_cmaes_string_append_entry_hits=0,
                    received_reference_runtime_source_sha256=template['runtime_source_sha256'])
    template['runtime_source_sha256'] = before
    write_json(output / 'champ-buffer-csv-joint-verdict.json', template)
    setter = champ.document(PREVIOUS + '/champ-setter-jdom-verdict.json')
    setter.update(common, review_status='beam_champ_scoped_setter_jdom_preservation_confirmed',
                  prior_champ_verdict=binding(champ, PREVIOUS + '/champ-setter-jdom-verdict.json'),
                  received_beam_verdict=index['groups'][0]['receipt'],
                  joint_bounded_component_acceptance_complete=True, additions_relative_to_v9=0)
    write_json(output / 'champ-setter-jdom-preservation.json', setter)
    policy = read_json(ROOT / 'output/api854-20261003/chronology-development-v2/policy.json')
    chronology_return = dict(common, schema_version=1, reviewer='champ', review_status='beam_champ_bounded_chronology_candidate_accepted',
                             received_beam_verdict=binding(beam, CHRONOLOGY + '/beam-chronology-verdict.json'),
                             candidate_receipt=binding(champ, 'output/api854-20261003/chronology-development-v2/receipt.json'),
                             policy_sha256=sha256(ROOT / 'output/api854-20261003/chronology-development-v2/policy.json'),
                             worklist_identities=chronology_identities, exact_targets=policy['exact_targets'],
                             agreed_preconditions=policy['preconditions'], agreed_assertions=policy['assertions'],
                             beam_verdict=original_chronology['beam_verdict'],
                             champ_verdict='accepted_for_bounded_candidate_oracle_development',
                             joint_bounded_candidate_acceptance_complete=True,
                             prospective_shared_recipe_implementation_authorized=True,
                             shared_integration_approved=False, accepted_into_shared_inputs=False,
                             full_legal_domain_approval=False,
                             required_integration_checks=['Same exact declaration/receiver identities in all four approaches',
                                 'Package-local or equally exact protected/internal invocation on production Partial',
                                 'UTC default/UTCProvider and only UTC/fixed +07:00 chronology factories',
                                 'Internal constructor uses already-validated UTC arrays; no clone/normalization/validation claim',
                                 'Default method receivers seeded before target tracing; setup/projection failures are fixture_error',
                                 'Preserve exact exception, Buddhist field/epoch-year, receiver state, array mutation and identity assertions',
                                 'Seal and test shared runtime/recipes, fixed repeatability, buggy and exact target-entry evidence before selection'],
                             candidate_fault_detected=True, candidate_failed_case='arrays_bad_order',
                             oracle_sensitivity_case='getfield_buddhist', new_java_or_defects4j_executions=0,
                             evidence_audit={'path': 'chronology-audit.json', 'sha256': sha256(output / 'chronology-audit.json')})
    write_json(output / 'champ-chronology-joint-verdict.json', chronology_return)
    union = proposed_union(buffer, lang, chronology_identities)
    write_json(output / 'proposed-union.json', union)
    after_pins, _ = checkpoint()
    require(pins == after_pins and before == implementation_hashes(), 'Shared checkpoint mutated during review')
    receipt = dict(common, schema_version=1, status='pass', beam_commit=beam.commit, aom_commit=aom.commit,
                   previous_champ_commit=champ.commit, checkpoint_pins_verified=len(pins), checksum_entries_verified=counts,
                   received_reviewers_rerun={'aom_buffer': aom_command, 'beam_chronology': chronology_command},
                   snapshot_archive_sha256=archives,
                   aom_buffer_review=read_json(output / 'aom-buffer-audit/receipt.json'),
                   lang_reference_cases_rederived=12, lang_fixed_observations_checked=24,
                   chronology_received_cases=13, chronology_exact_declarations=6,
                   shared_selected_unchanged=380, shared_unsupported_unchanged=311, denominator=691,
                   runtime_modified=False, shared_preparation_modified=False,
                   final_provider_readiness='Pending final condition, model IDs, limits/settings/framing, current quota/reset/expiry and owner gates',
                   credential_values_or_hashes_included=False)
    write_json(output / 'receipt.json', receipt)
    write_json(output / 'received-object-sha256.json', {o.commit: o.bindings for o in (beam, aom, champ)})
    write_json(output / 'return-index.json', {
        'schema_version': 1, 'review_status': receipt['status'], 'reviewer_commit': champ.commit,
        'beam_commit': beam.commit, 'aom_commit': aom.commit,
        'verdicts': [{'path': p.relative_to(ROOT).as_posix(), 'sha256': sha256(p)}
                     for p in sorted(output.glob('champ-*.json'))],
        'proposed_union': union, 'final_preparation_created': False,
        'final_reserve': None, 'gate_a_approved': False, 'pilot_authorized': False})
    write_json(output / 'checksums.json', {p.relative_to(output).as_posix(): sha256(p)
               for p in sorted(output.rglob('*')) if p.is_file()})
    print(json.dumps({'status': 'pass', 'joint_lang': 2, 'joint_buffer': 8, 'joint_chronology_candidate': 6,
                      'actual_shared_selected': 380, 'proposed_with_chronology': 396, 'provider_requests': 0}))


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--output', type=Path, required=True)
    args = parser.parse_args()
    if args.output.exists():
        parser.error('Evidence output already exists; choose a new packet path')
    try:
        run(args.output)
    except (ValueError, OSError, subprocess.SubprocessError) as error:
        output = args.output.resolve()
        if output.is_relative_to(ROOT / 'output') and output.is_dir() and not (output / 'checksums.json').exists():
            write_json(output / 'failure.json', {'status': 'fail', 'reason': str(error), 'approval': False, 'primary': False})
            write_json(output / 'checksums.json', {p.relative_to(output).as_posix(): sha256(p)
                       for p in sorted(output.rglob('*')) if p.is_file()})
        raise


if __name__ == '__main__':
    main()
