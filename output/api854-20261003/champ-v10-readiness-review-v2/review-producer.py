"""Review exact peer v10 inputs and request bytes without any authenticated API call.

Peer consumers and fixed Java run on a temporary pinned archive. The Champ v9
runtime and every received packet remain unchanged; token readiness stays pending.
"""
import argparse
from datetime import datetime, timezone
import io
import json
from pathlib import Path
import subprocess
import sys
import tempfile
import urllib.request

from .common import ROOT, implementation_hashes, read_json, sha256, write_json
from .review_joint_recipe_intake import GitObjects, digest, execute, extract_archive, require, buffer_oracle
from .review_final_recipe_returns import lang_oracle
from .verify_chronology_development import checkpoint

AOM = 'a4880fb2fde574e77705841f62f302273be7dcd9'
BEAM = '8af29c164a2f3b6f009c96825748afc909c56298'
PREP = 'output/api854-20261003/prepare-v10-joint-development'
PAIR = 'output/api854-20261003/aom-continuation-v10-development'
READY = 'output/api854-20261003/aom-v10-readiness-v1'
INTAKE = 'output/api854-20261003/aom-joint-v10-intake-v1'
JOINT = 'output/api854-20261003/beam-peer-joint-return-v3'
V9 = 'output/api854-20261003/prepare-v9-twenty-bug-development'
CONDITION = 'api854-20261003-joint-recipes-v10-development'
MODELS = {'kku-claude': 'claude-sonnet-5', 'kku-gemini': 'gemini-3.5-flash-lite'}
FIELDS = ('class', 'constructor_types', 'method', 'parameter_types')


class BatchedObjects(GitObjects):
    def blob(self, path):
        require(not Path(path).is_absolute() and '..' not in Path(path).parts, 'Unsafe Git member')
        if path not in self.cache:
            self.preload([path])
        self.bindings[path] = digest(self.cache[path])
        return self.cache[path]

    def preload(self, paths):
        paths = sorted(set(paths) - self.cache.keys())
        specs = [self.commit + ':' + p for p in paths]
        raw = subprocess.check_output(['git', 'cat-file', '--batch'], cwd=ROOT,
                                      input=('\n'.join(specs) + '\n').encode())
        stream = io.BytesIO(raw)
        for path in paths:
            header = stream.readline().split()
            require(len(header) == 3 and header[1] == b'blob', 'Missing Git blob: ' + path)
            data = stream.read(int(header[2]))
            require(stream.read(1) == b'\n', 'Malformed Git batch')
            self.cache[path] = data
        require(not stream.read(), 'Unexpected Git batch tail')


def target_key(target):
    return tuple(target[k] for k in FIELDS)


def worksheet_row(row, prompt, approach, model_id):
    require(approach in MODELS and model_id == MODELS[approach], 'Wrong requested model binding')
    request = {'model': model_id, 'messages': [{'role': 'user', 'content': prompt.decode('utf-8')}],
               'max_tokens': 4096, 'stream': False, 'temperature': 0}
    wire = json.dumps(request, ensure_ascii=False, allow_nan=False).encode('utf-8')
    return {'project': row['project'], 'bug_id': row['bug_id'], 'owner': row['owner'],
            'approach': approach, 'requested_model_id': model_id,
            'prompt_path': PREP + f"/{row['project']}-{row['bug_id']}/prompt.md",
            'prompt_sha256': digest(prompt), 'prompt_utf8_bytes': len(prompt),
            'requested_temperature': 0, 'requested_output_cap': 4096,
            'serialized_request_utf8_bytes': len(wire), 'serialized_request_sha256': digest(wire),
            'json_serialization_overhead_bytes': len(wire) - len(prompt),
            'json_overhead_is_provider_framing': False,
            'historical_style_byte_guard_before_unknown_framing': len(prompt) + 4096,
            'provider_prompt_tokens': None, 'provider_framing_tokens': None,
            'current_model_id_verified': False, 'effective_settings_verified': False,
            'context_limit_tokens': None, 'output_limit_tokens': None,
            'current_quota_remaining_tokens': None, 'quota_bucket': None,
            'quota_reset_at': None, 'credential_expiry_at': None,
            'final_token_reserve': None, 'live_reservation_ready': False}


def check_received_worksheet(received, rows, index_hash, protocol_hash, runtime):
    require(received['condition'] == CONDITION and received['preparation_index_sha256'] == index_hash
            and received['protocol_sha256'] == protocol_hash
            and received['runtime_source_sha256'] == runtime, 'Worksheet condition/pins differ')
    require(received['prompt_model_pairs'] == len(received['records']) == len(rows) == 40,
            'Require exactly forty prompt/model pairs')
    keys = lambda r: (r['project'], r['bug_id'], r['approach'])
    expected = {keys(r): r for r in rows}
    require(len(expected) == len({keys(r) for r in received['records']}) == 40,
            'Duplicate/missing worksheet pair')
    for r in received['records']:
        require(keys(r) in expected, 'Unexpected worksheet pair')
        e = expected[keys(r)]
        require(all(r[k] == e[k] for k in ('owner', 'prompt_path', 'prompt_sha256',
                                          'prompt_utf8_bytes', 'requested_model_id')),
                'Worksheet prompt/model/owner differs')
        require(r['temperature'] == e['requested_temperature'] == 0
                and r['output_cap'] == e['requested_output_cap'] == 4096
                and r['historical_style_numerical_guard_floor_plus_unknown_framing'] ==
                e['historical_style_byte_guard_before_unknown_framing'], 'Worksheet settings/guard differs')
        require(r['final_reserve'] is None and r['provider_input_tokens'] is None
                and r['framing_overhead'] is None, 'Unsubstantiated token/framing measurement')
    require(received['final_reserve'] is None and received['bytes_are_not_provider_tokens'] is True,
            'Bytes cannot establish final token reserve')


def verify_partition(objects, index, intake, confirmations):
    accepted = intake['buffer_targets'] + intake['lang_targets']
    require(len(accepted) == len({target_key(t) for t in accepted}) == 10, 'Accepted delta differs')
    grouped = {}
    for doc in confirmations:
        for row in doc['candidates']:
            target = row['target']
            grouped.setdefault((target['project'], target['bug_id']), set()).add(target_key(target))
    records, delta_count = [], 0
    for row in index['records']:
        name = f"{row['project']}-{row['bug_id']}"
        old = read_json(ROOT / V9 / name / 'targets.json')['targets']
        new = objects.document(PREP + '/' + name + '/targets.json')['targets']
        old_keys, new_keys = {target_key(t) for t in old}, {target_key(t) for t in new}
        expected_delta = grouped.get((row['project'], row['bug_id']), set())
        require(old_keys <= new_keys and new_keys - old_keys == expected_delta
                and len(new_keys) == len(new) == row['target_count'], 'Selected delta differs: ' + name)
        old_ex = read_json(ROOT / V9 / name / 'capability-exclusions.json')['excluded']
        new_ex = objects.document(PREP + '/' + name + '/capability-exclusions.json')['excluded']
        require(new_ex == [r for r in old_ex if target_key(r['target']) not in expected_delta]
                and len(new_ex) == row['capability_exclusion_count'], 'Exclusions/reasons changed: ' + name)
        ex_keys = {target_key(r['target']) for r in new_ex}
        require(not new_keys & ex_keys and len(new) + len(new_ex) == len(old) + len(old_ex),
                'Common declaration partition differs: ' + name)
        before = read_json(ROOT / V9 / name / 'context-manifest.json')
        after = objects.document(PREP + '/' + name + '/context-manifest.json')
        require(before['source_files'] == after['source_files']
                and before['source_hash'] == after['source_hash'], 'Fixed source knowledge changed: ' + name)
        for path in (ROOT / V9 / name / 'fixed-source').rglob('*'):
            if path.is_file():
                relative = path.relative_to(ROOT / V9 / name / 'fixed-source').as_posix()
                require(objects.blob(PREP + '/' + name + '/fixed-source/' + relative) == path.read_bytes(),
                        'Retained fixed source bytes changed: ' + relative)
        delta_count += len(expected_delta)
        records.append({'project': row['project'], 'bug_id': row['bug_id'], 'owner': row['owner'],
                        'selected': len(new), 'exclusions': len(new_ex),
                        'accepted_additions': [t for t in new if target_key(t) in expected_delta],
                        'fixed_source_hash': after['source_hash']})
    require(len(records) == 20 and delta_count == 10
            and sum(r['selected'] for r in records) == index['target_count'] == 390
            and sum(r['exclusions'] for r in records) == index['capability_exclusion_count'] == 301
            and index['required_common_declarations'] == 691, 'v10 aggregate partition differs')
    require({target_key(t) for t in index['accepted_additions']} == {target_key(t) for t in accepted},
            'Indexed additions differ from accepted receipts')
    return records


ADAPTER = r'''
import json, pathlib, subprocess, sys, unittest
snapshot, original, output, defects4j = map(pathlib.Path, sys.argv[1:])
sys.path.insert(0, str(snapshot))
old_check_output = subprocess.check_output
def read_git(command, *args, **kwargs):
    command = list(command)
    if len(command) > 2 and command[:2] == ['git', '-C'] and pathlib.Path(command[2]) == snapshot:
        command[2] = str(original)
    return old_check_output(command, *args, **kwargs)
subprocess.check_output = read_git
from scripts.study.api854.common import implementation_hashes, read_json, write_json
from scripts.study.api854.joint_recipe_v10 import load_intake
from scripts.study.api854.gate_a import inspect
from scripts.study.api854.verify_v10_recipe_runtime import verify
require = lambda ok, msg: None if ok else (_ for _ in ()).throw(ValueError(msg))
load_intake()
pair = 'output/api854-20261003/aom-continuation-v10-development'
gate = inspect(protocol_path=pair+'/protocol.proposal.json', runner_path=pair+'/runner-plan.json')
required = {'prepare_contract','protocol_runner_binding','preparation_index_binding','runtime_source_binding',
            'shared_policy','fixture_recipe_binding','runner_coverage','eligible_targets_20'}
require(not gate['gate_a_passed'] and all(c['status']=='pass' for c in gate['checklist'] if c['id'] in required),
        'v10 structural binding audit failed')
write_json(output/'received-runtime-gate-recheck.json', gate)
suite = unittest.defaultTestLoader.loadTestsFromNames([
    'scripts.study.api854.tests.test_joint_recipe_v10',
    'scripts.study.api854.tests.test_preparation_v10_development'])
result = unittest.TextTestRunner(verbosity=2).run(suite)
require(result.wasSuccessful() and result.testsRun == 7 and not result.skipped, 'Focused received tests failed')
write_json(output/'received-focused-tests.json', {'tests':result.testsRun,'failures':len(result.failures),
    'errors':len(result.errors),'skipped':len(result.skipped),'four_consumer_bug_approach_combinations':80})
proof = verify(defects4j)
write_json(output/'native-fixed-runtime-recheck.json', proof)
print(json.dumps({'status':'pass','tests':result.testsRun,'fixed_cases':proof['fixed_source_integration_cases']}))
'''


def provider_document_check(output):
    result = {'source_url': 'https://gen.ai.kku.ac.th/docs/api',
              'checked_at_utc': datetime.now(timezone.utc).isoformat(),
              'scope': 'Public documentation only; no Authorization header or authenticated API request',
              'raw_document_retained': False, 'current_model_ids_verified': False,
              'effective_model_settings_verified': False, 'context_output_limits_verified': False,
              'provider_framing_verified': False, 'current_quota_reset_expiry_verified': False}
    try:
        with urllib.request.urlopen(result['source_url'], timeout=20) as response:
            raw = response.read()
            result.update(http_status=response.status, document_bytes=len(raw), raw_document_sha256=digest(raw))
        text = raw.decode('utf-8')
        result['generic_temperature_parameter_documented'] = 'temperature' in text
        result['generic_max_tokens_parameter_documented'] = 'max_tokens' in text
        result['model_quota_response_field_documented'] = 'daily_remaining_tokens' in text
        result['conclusion'] = 'Generic API parameters and quota response fields do not prove either requested model availability, effective settings, limits, token framing, or any account current quota/expiry.'
    except (OSError, ValueError):
        result.update(status='public_document_unavailable', conclusion='No current provider evidence established')
    write_json(output / 'public-provider-document-review.json', result)
    return result


def run(output, defects4j):
    output = Path(output).resolve()
    require(output.is_relative_to(ROOT / 'output') and not output.exists(), 'Require a new repository output path')
    pins, _ = checkpoint()
    before = implementation_hashes()
    aom, beam = BatchedObjects(AOM), BatchedObjects(BEAM)
    final = aom.document(READY + '/final-checksums.json')
    aom.preload(final)
    for path, expected in final.items():
        require(digest(aom.blob(path)) == expected, 'Final manifest differs: ' + path)
    counts = {'aom_final': len(final)}
    for base in (INTAKE, PAIR, READY):
        counts[base] = aom.checksums(base)
    counts[JOINT] = beam.checksums(JOINT)
    for entry in beam.document(JOINT + '/provenance.json'):
        original = BatchedObjects(entry['source_commit'])
        require(digest(original.blob(entry['source_path'])) == entry['sha256']
                == digest(beam.blob(entry['received_path'])), 'Beam original/received provenance differs')
    docs = [beam.document(JOINT + '/' + name) for name in
            ('beam-buffer-csv-joint-confirmation.json', 'beam-lang-joint-confirmation.json')]
    require(docs[0]['joint_acceptance'] is True and docs[1]['joint_acceptance_complete'] is True,
            'Explicit joint confirmation incomplete')
    intake = aom.document(INTAKE + '/receipt.json')
    old_buffer = aom.document(INTAKE + '/received/champ-buffer-verdict.json')
    old_lang = aom.document(INTAKE + '/received/champ-lang-verdict.json')
    for doc, old, precondition, oracle in [(docs[0], old_buffer, 'accepted_preconditions', 'accepted_oracle'),
                                         (docs[1], old_lang, 'agreed_preconditions', 'agreed_exception_oracle')]:
        previous = {target_key(r['target']): r for r in old['candidates']}
        require(len(doc['candidates']) == len(previous), 'Joint target inventory differs')
        for row in doc['candidates']:
            old_row = previous[target_key(row['target'])]
            if precondition == 'accepted_preconditions':
                require(row[precondition] == old_row['preconditions']
                        and row[oracle] == old_row['meaningful_oracle'], 'Buffer scope/oracle changed')
            else:
                require(row['received_champ_preconditions'] == old_row[precondition]
                        and row['received_champ_oracle'] == old_row[oracle], 'Lang scope/oracle changed')
            for ref in row['evidence']:
                source = BatchedObjects(ref.get('commit', BEAM))
                require(digest(source.blob(ref['path'])) == ref['sha256'], 'Joint evidence binding changed')
    index = aom.document(PREP + '/index.json')
    protocol = aom.document(PAIR + '/protocol.proposal.json')
    runtime = index['runtime_source_sha256']
    require(len(runtime) == 41 and protocol['source_sha256'] == runtime
            and protocol['condition'] == CONDITION
            and protocol['enabled_stages'] == [] and protocol['generation']['prompt_token_reserve'] is None
            and not any(protocol['gate_a']['reviewed_by'].values()), 'Runtime/condition/gate binding differs')
    aom.preload(runtime)
    for path, expected in runtime.items():
        require(digest(aom.blob(path)) == expected, 'Peer runtime pin differs: ' + path)
    require(protocol['runner_plan_sha256'] == digest(aom.blob(PAIR + '/runner-plan.json')),
            'Runner binding differs')
    for row in index['records']:
        counts[PREP + f"/{row['project']}-{row['bug_id']}"] = aom.checksums(PREP + f"/{row['project']}-{row['bug_id']}")
    records = verify_partition(aom, index, intake, docs)
    rows = [worksheet_row(r, aom.blob(PREP + f"/{r['project']}-{r['bug_id']}/prompt.md"), approach,
                          protocol['models'][approach]['id']) for r in index['records'] for approach in MODELS]
    check_received_worksheet(aom.document(READY + '/prompt-reserve-worksheet.json'), rows,
                            digest(aom.blob(PREP + '/index.json')),
                            digest(aom.blob(PAIR + '/protocol.proposal.json')), runtime)
    largest = max(rows, key=lambda r: r['prompt_utf8_bytes'])
    require(largest['prompt_utf8_bytes'] == index['max_prompt_utf8_bytes'] == 265937,
            'Actual maximum prompt differs')
    output.mkdir(parents=True, exist_ok=False)
    received = output / 'received'
    for objects, paths in [(aom, [PREP+'/index.json', PAIR+'/protocol.proposal.json', PAIR+'/runner-plan.json',
                                   READY+'/prompt-reserve-worksheet.json', READY+'/completion-receipt.json',
                                   READY+'/fixed-runtime-verification.json', READY+'/final-checksums.json',
                                   INTAKE+'/receipt.json', 'docs/api854/AOM_JOINT_V10_HANDOFF_TH.md']),
                           (beam, [JOINT+'/'+n for n in ('receipt.json','checksums.json',
                                    'beam-buffer-csv-joint-confirmation.json','beam-lang-joint-confirmation.json')]
                                  + ['docs/api854/BEAM_AOM_BUFFER_CHAMP_JOINT_RETURN_TH.md'])]:
        for path in paths:
            destination = received / ('aom' if objects is aom else 'beam') / path
            destination.parent.mkdir(parents=True, exist_ok=True)
            destination.write_bytes(objects.blob(path))
    (output / 'review-producer.py').write_bytes(Path(__file__).read_bytes())
    (output / 'received-runtime-adapter.py').write_text(ADAPTER, encoding='utf-8', newline='\n')
    write_json(output / 'partition-and-source-review.json', records)
    write_json(output / 'prompt-model-worksheet.json', {'condition': CONDITION, 'pairs': len(rows),
        'records': rows, 'largest_prompt': largest, 'max_prompt_utf8_bytes': 265937,
        'conditional_byte_guard_floor': 270033, 'conditional_formula': '270033 + H (unknown)',
        'byte_guard_is_provider_token_reserve': False, 'final_token_reserve': None,
        'reserve_formula_when_evidence_exists': 'max_model(prompt_tokens + uncounted_framing_tokens) + 4096',
        'framing_double_counting_forbidden': True})
    with tempfile.TemporaryDirectory(prefix='.champ-v10-review-', dir=ROOT/'output') as temporary:
        snapshot = Path(temporary)
        paths = ['scripts/study', 'algorithms', 'experiments/configs/api854-20261003', PREP, PAIR, READY, INTAKE,
                 'output/api854-20261003/prepare-v3', 'output/api854-20261003/prepare-v7-twenty-bug-development',
                 'output/api854-20261003/prepare-v8-fraction-field-development',
                 'docs/api854/evidence/beam-champ-math-field-20261003-attempt2/supplemental-fixed-source']
        archive_hash = extract_archive(AOM, paths, snapshot)
        for path, expected in runtime.items():
            require(sha256(snapshot / path) == expected, 'Extracted archive runtime bytes differ')
        write_json(output / 'preexecution-seal.json', {'declared_before_execution': True,
            'aom_commit': AOM, 'beam_commit': BEAM, 'snapshot_archive_sha256': archive_hash,
            'runtime_source_sha256': runtime, 'manifest_entries_verified': counts,
            'received_inputs_sha256': {p.relative_to(output).as_posix():sha256(p) for p in received.rglob('*') if p.is_file()},
            'review_producer_sha256':sha256(output/'review-producer.py'),
            'adapter_sha256':sha256(output/'received-runtime-adapter.py'), 'champ_v9_pins':pins})
        execute([sys.executable, '-B', output/'received-runtime-adapter.py', snapshot, ROOT, output, defects4j],
                snapshot, output, 'received-runtime-recheck')
    proof = read_json(output / 'native-fixed-runtime-recheck.json')
    old_proof = aom.document(READY + '/fixed-runtime-verification.json')
    for field in ('status', 'policy', 'runtime_helper_sha256', 'verifier_sha256', 'dependency_fixed_revisions',
                  'math_source_and_factory_sha256', 'production_source_sha256', 'dependency_sha256',
                  'observations', 'buffer_csv_lang_fixed_cases', 'legacy_policy_behavior_preserved',
                  'temporary_setter_mutation_detected', 'mutated_observation', 'fixed_source_integration_cases'):
        require(proof[field] == old_proof[field], 'Native fixed proof differs from exact Aom receipt: ' + field)
    for row in proof['buffer_csv_lang_fixed_cases']:
        reference = next(r for r in beam.document(row['reference_path']) if r['case_id'] == row['case_id'])
        expected = lang_oracle(reference) if row['target']['class'].endswith('NumberUtils') else buffer_oracle(reference)
        require(row['expected'] == expected == row['fixed_first']['outcome'] == row['fixed_second']['outcome'],
                'Independent scalar/state oracle differs')
    document = provider_document_check(output)
    require(implementation_hashes() == before and checkpoint()[0] == pins, 'Champ runtime/pins changed')
    result = {'schema_version': 1, 'status': 'offline_v10_inputs_accepted_provider_readiness_pending',
        'checked_at_utc': datetime.now(timezone.utc).isoformat(), 'condition': CONDITION,
        'received_commits': {'aom': AOM, 'beam': BEAM},
        'source_bindings': {'aom': {'commit':AOM,'paths':aom.bindings}, 'beam': {'commit':BEAM,'paths':beam.bindings}},
        'checksum_entry_checks': counts, 'runtime_files': len(runtime), 'runtime_source_sha256': runtime,
        'received_v10_runtime_differs_from_champ_v9': [p for p in runtime if runtime[p] != before[p]],
        'reviewed_bugs': 20, 'selected': 390, 'exclusions': 301, 'denominator': 691,
        'accepted_additions': 10, 'four_consumer_combinations_verified': 80, 'received_tests_rerun': 7,
        'native_fixed_cases_rerun': 64, 'native_fixed_observations': 128,
        'independent_buffer_csv_lang_oracle_cases': 54, 'new_defects4j_evaluations': 0,
        'prompt_model_pairs': 40, 'max_prompt_utf8_bytes': 265937, 'conditional_byte_guard_floor': 270033,
        'final_token_reserve': None, 'current_model_ids_verified': False, 'effective_settings_verified': False,
        'provider_framing_limits_verified': False, 'current_quota_reset_expiry_verified': False,
        'public_document_review': document,
        'champ_shared_runtime_remains_v9': True, 'full_condition_semantic_host_accepted': False,
        'gate_a_passed': False, 'pilot_authorized': False, 'authenticated_kku_requests': 0,
        'live_queue_mutations': 0, 'quota_ledger_modified': False, 'primary_results_added': 0,
        'pending': ['Beam final v10 semantic/four-consumer/native-host verdict',
                    'Current provider model IDs, effective settings and context/output limits',
                    'Exact-model v10 prompt tokens and framing; current account bucket/quota/reset/expiry',
                    '301 exclusions and three-owner Gate A approval']}
    write_json(output / 'receipt.json', result)
    write_json(output / 'checksums.json', {p.relative_to(output).as_posix():sha256(p)
        for p in sorted(output.rglob('*')) if p.is_file()})
    return result


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--output', type=Path, required=True)
    parser.add_argument('--defects4j', type=Path, required=True)
    args = parser.parse_args()
    r = run(args.output, args.defects4j)
    print(json.dumps({k:r[k] for k in ('status','selected','exclusions','prompt_model_pairs',
                     'max_prompt_utf8_bytes','conditional_byte_guard_floor','final_token_reserve')}))
