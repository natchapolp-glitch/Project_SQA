"""Audit a pinned Beam Graphics2D joint verdict without executing peer code or APIs."""
import argparse
import copy
from datetime import datetime, timezone
import io
import json
from pathlib import Path
import re
import subprocess
import tarfile

from .common import ROOT, sha256, write_json
from .review_joint_recipe_intake import digest, require
from .review_v10_readiness import AOM, CONDITION, PREP, PAIR, READY, BatchedObjects, target_key
from .verify_chronology_development import checkpoint
from . import verify_graphics_development as graphics

BEAM = 'db74f11789719b9ae7bbc7b5b64416ed535d4e36'
CHAMP = '8d9295e6c238ce6e2f9c2014932207ef2f036663'
BASE = 'output/api854-20261003/beam-graphics-review-v1'
HOST = BASE + '/host-review-v1'
NATIVE = HOST + '/native-evidence'
PEER = 'output/api854-20261003/graphics-development-v3'
TEMPLATE = 'output/api854-20261003/champ-graphics-joint-review.template.json'
STAGES = ('fixed_first', 'fixed_second', 'buggy_first', 'buggy_second',
          'fixed_method_entry_trace', 'buggy_method_entry_trace')
VERDICT = 'accepted_for_bounded_candidate_oracle_development'


class GitPixels:
    """Expose immutable Git pixel blobs to the canonical independent validator."""
    def __init__(self, objects, path, overrides=None):
        self.objects, self.path, self.overrides = objects, path, overrides or {}

    def __truediv__(self, name):
        return GitPixels(self.objects, self.path + '/' + name, self.overrides)

    def read_bytes(self):
        return self.overrides[self.path] if self.path in self.overrides else self.objects.blob(self.path)


def validate_scope(receipt, host, joint, integration):
    require(receipt['champ_commit'] == joint['champ_candidate_commit'] == CHAMP
            and receipt['policy_id'] == joint['candidate_policy_id'] == graphics.POLICY['policy_id'],
            'Candidate commit/policy differs')
    require(receipt['actual_shared_aom_commit'] == joint['actual_shared_aom_commit']
            == integration['base_aom_commit'] == AOM
            and receipt['actual_shared_condition'] == joint['actual_shared_condition']
            == integration['base_condition'] == CONDITION, 'Actual shared v10 condition differs')
    require((receipt['actual_selected'], receipt['actual_exclusions'], receipt['denominator']) == (390, 301, 691)
            and (joint['actual_selected'], joint['actual_unsupported'], joint['denominator']) == (390, 301, 691)
            and integration['base_counts'] == {'selected':390, 'exclusions':301, 'denominator':691},
            'Actual shared partition differs')
    require(receipt['graphics_only_possible_next_condition_counts']
            == integration['graphics_only_possible_next_counts']
            == {'selected':397, 'exclusions':294, 'implemented':False}
            and joint['graphics_only_proposed_union'] == {'selected':397, 'unsupported':294, 'implemented':False},
            'Possible next condition cannot imply completed integration')
    require(joint['joint_acceptance_complete'] is True and len(joint['candidates']) == 7
            and receipt['candidate_signatures'] == 7 and receipt['unique_bounded_cases'] == 24,
            'Missing bounded joint verdict')
    for row in joint['candidates']:
        require(row['champ_verdict'] == row['beam_verdict'] == VERDICT
                and row['agreed_preconditions'] == row['proposed_preconditions']
                and row['agreed_oracle'] == row['proposed_oracle']
                and row['aom_shared_integration_verdict'] is None and row['accepted_into_shared_inputs'] is False,
                'Joint acceptance scope/oracle differs')
    require(receipt['shared_preparation_approved'] is False and receipt['full_legal_domain_approval'] is False
            and receipt['gate_a_approved'] is False and receipt['final_reserve'] is None
            and receipt['full_defects4j_evaluation'] is False
            and receipt['candidate_fault_detected'] is False, 'Unsubstantiated shared/full-domain/Gate/fault approval')
    require(joint['shared_integration_approved'] is False and joint['new_shared_preparation_authorized_by_receipt'] is False
            and joint['full_legal_domain_approval'] is False and joint['full_defects4j_evaluation'] is False
            and joint['candidate_fault_detected'] is False and joint['gate_a_passed'] is False
            and joint['final_prompt_reserve'] is None, 'Joint verdict exceeds bounded candidate scope')
    require(integration['status'] == 'shared_integration_pending'
            and integration['new_preparation_authorized_by_receipt'] is False
            and integration['gate_a_approved'] is False, 'Integration contract became an approval')
    require(host['status'] == 'bounded_native_graphics_review_passed'
            and host['worker_id'] == receipt['host_worker_id'] == 'beam-pc1'
            and host['cpu_slots'] == receipt['cpu_slots'] == 1
            and host['cpu_lock_exits'] == receipt['cpu_lock_exits'] == [9, 0]
            and '11.' in host['java_version'] and 'Linux' in host['platform'], 'Beam native host/CPU binding differs')
    require(host['shared_integration_approved'] is False and host['gate_a_approved'] is False
            and host['candidate_fault_detected'] is False, 'Host acceptance scope differs')
    for document, primary in ((receipt,'primary_results_added'), (host,'primary_results_added'),
                              (joint,'primary_added'), (integration,'primary_added')):
        require(document['live_requests'] == document['queue_mutations'] == document[primary] == 0,
                'Unexpected live/queue/primary activity')


def audit_production(objects, base, proof):
    seal = objects.document(base + '/preexecution-seal.json')
    require(digest(objects.blob(base + '/preexecution-seal.json')) == proof['preexecution_seal_sha256'],
            'Native preexecution seal differs')
    for key in ('policy_id', 'suite_sha256', 'shared_input_sha256', 'runtime_source_sha256',
                'source_archives', 'compiled_source_sha256', 'dependencies_sha256', 'worklist_identities'):
        require(seal[key] == proof[key], 'Native seal/receipt differs: ' + key)
    require(seal['svn_revisions'] == {'fixed':'2266', 'buggy':'2264'}
            and seal['primary'] is False and seal['owner_approval'] is False
            and seal['selection_used_buggy_outcomes'] is False, 'Source revision/selection/approval differs')
    for name, value in proof['suite_sha256'].items():
        require(digest(objects.blob(base + '/' + name)) == value, 'Executed suite changed')
    for version, metadata in proof['source_archives'].items():
        raw = objects.blob(base + '/' + version + '-production-source.tar.gz')
        require(digest(raw) == metadata['archive_sha256'], 'Production archive hash differs')
        with tarfile.open(fileobj=io.BytesIO(raw)) as archive:
            sources = {m.name:archive.extractfile(m).read() for m in archive.getmembers()
                       if m.isfile() and m.name.endswith('.java')}
        require(len(sources) == 654 and set(sources) == set(proof['compiled_source_sha256'][version]),
                'Production Java source inventory differs')
        if version == 'fixed':
            for name in (graphics.SOURCE, graphics.RECEIVER_SOURCE):
                retained = (graphics.PREP / 'fixed-source' / name).read_bytes()
                require(sources[name].replace(b'\r\n', b'\n') == retained.replace(b'\r\n', b'\n')
                        and digest(retained) == seal['fixed_target_source_sha256'][name],
                        'Retained production receiver/declaring source differs')
                sources[name] = retained
        require({name:digest(raw) for name, raw in sources.items()} == proof['compiled_source_sha256'][version],
                'Compiled production source pins differ')
        for name, value in proof['dependencies_sha256'][version].items():
            require(digest(objects.blob(base + '/dependencies/' + name)) == value, 'Dependency bytes differ')
    commands = [p for p in objects.document(base + '/checksums.json') if p.endswith('.command.json')]
    require(len(commands) == 15, 'Production command inventory differs')
    for name in commands:
        command = objects.document(base + '/' + name)
        stem = name.removesuffix('.command.json')
        expected_exit = 1 if stem == 'temporary-shifted-line' else 0
        require(command['exit_code'] == expected_exit, 'Unexpected native command status: ' + stem)
        for stream in ('stdout', 'stderr'):
            require(digest(objects.blob(base + '/' + stem + '.' + stream + '.log')) == command[stream + '_sha256'],
                    'Native command/log hash differs')
        if stem in STAGES or stem == 'temporary-shifted-line':
            described = proof['stages'][stem] if stem in STAGES else proof['temporary_shifted_line_mutation']
            require(all(described[key] == value for key, value in command.items()), 'Receipt/raw command differs')
    rows, records_by_stage = {}, {}
    for stage in STAGES:
        records = graphics.parse_log(objects.blob(base + '/' + stage + '.stdout.log'))
        rows[stage] = graphics.validate_records(records, GitPixels(objects, base + '/' + stage + '-pixels'))
        require(rows[stage] == proof['stages'][stage]['cases'], 'Receipt/raw pixel-state observations differ')
        if stage.endswith('method_entry_trace'):
            entries = graphics.validate_trace(records, 0)
            require(len(entries) == 24 and entries == proof['stages'][stage]['exact_target_entries'],
                    'Receipt/raw inherited method entries differ')
        records_by_stage[stage] = records
    for revision in ('fixed', 'buggy'):
        require(rows[revision + '_first'] == rows[revision + '_second'] == rows[revision + '_method_entry_trace'],
                'Repeated/traced pixel-state observations differ')
    mutation = graphics.validate_records(graphics.parse_log(objects.blob(base + '/temporary-shifted-line.stdout.log')),
                                        GitPixels(objects, base + '/temporary-shifted-line-pixels'),
                                        allow_assertion_failures=True)
    require([r['case'] for r in mutation if not r['target_check_passed']]
            == proof['temporary_shifted_line_mutation']['failed_cases'] == ['domain_line_v']
            and proof['oracle_sensitivity_verified'] is True, 'Shifted-line oracle sensitivity differs')
    require(proof['status'] == 'pass' and proof['cases_per_revision'] == 24
            and proof['exact_declarations_entered_per_revision'] == 7
            and proof['declaring_class'] == graphics.OWNER and proof['receiver_class'] == graphics.RECEIVER
            and proof['worklist_identities'] == graphics.POLICY['worklist_identities']
            and proof['candidate_fault_detected'] is False and proof['buggy_failed_cases'] == []
            and proof['full_defects4j_evaluation'] is False and proof['shared_integration_approved'] is False
            and proof['gate_a_passed'] is False and proof['final_prompt_reserve'] is None
            and proof['line_or_branch_coverage_percentage'] is None
            and proof['live_requests'] == proof['queue_mutations'] == proof['primary_results_added'] == 0,
            'Native standalone proof scope differs')
    return rows, records_by_stage


def rejected(name, operation, results):
    try:
        operation()
    except ValueError:
        results.append(name)
    else:
        raise ValueError('Negative control accepted: ' + name)


def run(output):
    output = Path(output).resolve()
    require(output.is_relative_to(ROOT / 'output') and not output.exists(), 'Require a new output directory')
    pins, _ = checkpoint()
    beam, champ, aom = BatchedObjects(BEAM), BatchedObjects(CHAMP), BatchedObjects(AOM)
    manifest = beam.document(BASE + '/checksums.json')
    beam.preload([BASE + '/' + p for p in manifest])
    require(beam.checksums(BASE) == 977, 'Received manifest inventory differs')
    nested = {base:beam.checksums(base) for base in (HOST, NATIVE, *[
        BASE + '/received-champ/output/api854-20261003/graphics-development-' + v for v in ('v1','v2','v3')])}
    receipt, host, joint, integration = [beam.document(path) for path in (
        BASE + '/receipt.json', HOST + '/host-receipt.json', BASE + '/joint-verdict.json', BASE + '/integration-requirements.json')]
    validate_scope(receipt, host, joint, integration)
    require(sum(nested.values()) == receipt['checksum_entries_verified'] == 1338, 'Nested checksum count differs')
    provenance = beam.document(BASE + '/received-provenance.json')
    snapshot = beam.document(BASE + '/snapshot-provenance.json')
    supplement = beam.document(BASE + '/snapshot-supplement-provenance.json')
    source_rows = snapshot['files'] + supplement
    champ.preload([r['source_path'] for r in provenance + source_rows])
    require(snapshot['source_commit'] == CHAMP and len(source_rows) == receipt['snapshot_git_blobs_verified'] == 1637,
            'Candidate snapshot provenance differs')
    for row in provenance:
        require(row['source_commit'] == CHAMP and digest(champ.blob(row['source_path']))
                == row['sha256'] == digest(beam.blob(row['received_path'])), 'Original/received bytes differ')
    require(len(provenance) == receipt['received_git_blobs_verified'] == 562, 'Received provenance count differs')
    for row in source_rows:
        require(row.get('source_commit', CHAMP) == CHAMP
                and digest(champ.blob(row['source_path'])) == row['sha256'], 'Snapshot source pin differs')
    archive = subprocess.check_output(['git', 'archive', CHAMP, *snapshot['archive_paths']], cwd=ROOT)
    require(len(archive) == snapshot['archive_bytes'] and digest(archive) == snapshot['archive_sha256'],
            'Original snapshot archive differs')
    native, peer = beam.document(NATIVE + '/receipt.json'), champ.document(PEER + '/receipt.json')
    peer_manifest = champ.document(PEER + '/checksums.json')
    missing_peer_paths = [PEER + '/' + p for p in peer_manifest if PEER + '/' + p not in champ.cache]
    if missing_peer_paths:
        champ.preload(missing_peer_paths)
    require(champ.checksums(PEER) == 393, 'Original Champ manifest differs')
    for key in ('suite_sha256', 'shared_input_sha256', 'runtime_source_sha256', 'compiled_source_sha256',
                'dependencies_sha256', 'worklist_identities'):
        require(native[key] == peer[key], 'Native/original candidate binding differs: ' + key)
    for path, value in native['shared_input_sha256'].items():
        require(digest(champ.blob(path)) == value, 'Candidate v9 input pin differs')
    for path, value in native['runtime_source_sha256'].items():
        require(digest(champ.blob(path)) == value, 'Candidate v9 runtime pin differs')
    require(receipt['runtime_source_sha256'] == native['runtime_source_sha256'], 'Top/native runtime differs')
    native_rows, native_records = audit_production(beam, NATIVE, native)
    peer_rows, _ = audit_production(champ, PEER, peer)
    equal = {stage:native_rows[stage] == peer_rows[stage] for stage in STAGES}
    require(all(equal.values()) and receipt['peer_raster_equivalence_by_stage']
            == host['peer_case_observations_equal_by_stage'] == equal
            and host['peer_raster_equivalence_claimed'] is True, 'Cross-host pixel/state equivalence differs')
    for stage in STAGES:
        for name in graphics.CASES:
            for kind in ('actual','reference'):
                tail = '/' + stage + '-pixels/' + name + '.' + kind + '.argb'
                require(beam.blob(NATIVE + tail) == champ.blob(PEER + tail), 'Cross-host raw raster differs')
    adapter = beam.document(HOST + '/adapter-preexecution-seal.json')
    require(adapter['champ_commit'] == CHAMP and adapter['wrapper_sha256'] == digest(beam.blob(BASE + '/run_native_host.py'))
            and adapter['canonical_verifier_sha256'] == native['suite_sha256']['verifier.py']
            == digest(champ.blob('scripts/study/api854/verify_graphics_development.py'))
            == digest(Path(graphics.__file__).read_bytes())
            and adapter['policy_sha256'] == native['suite_sha256']['policy.json']
            and adapter['runtime_source_sha256'] == native['runtime_source_sha256']
            and adapter['primary'] is False and adapter['gate_a'] is False
            and digest(beam.blob(NATIVE + '/receipt.json')) == host['canonical_native_receipt_sha256'],
            'Native adapter/verifier/receipt binding differs')
    tests = beam.document(BASE + '/received-tests-v2.command.json')
    for stream in ('stdout','stderr'):
        require(tests[stream + '_sha256'] == digest(beam.blob(BASE + '/received-tests-v2.' + stream + '.log')),
                'Focused test command/log differs')
    log = beam.blob(BASE + '/received-tests-v2.stderr.log').decode('utf-8')
    require(tests['exit_code'] == 0 and re.search(r'Ran 8 tests in', log) and log.strip().endswith('OK')
            and len(re.findall(r'\.\.\. ok\s*$', log, re.MULTILINE)) == 8
            and not re.search(r'\.\.\. skipped\b', log)
            and receipt['fresh_focused_tests_passed'] == 8 and receipt['fresh_focused_tests_skipped'] == 0,
            'Received focused test count/status differs')
    template = champ.document(TEMPLATE)
    immutable_fields = ('target','exact_declaring_class','exact_jvm_descriptor','receiver_identity',
                        'candidate_cases','proposed_preconditions','proposed_oracle','champ_verdict')
    require([{k:r[k] for k in immutable_fields} for r in joint['candidates']]
            == [{k:r[k] for k in immutable_fields} for r in template['candidates']]
            and integration['bounded_candidates'] == joint['candidates']
            and joint['prospective_integration_requirements'] == template['prospective_integration_requirements'],
            'Exact candidate signatures/preconditions/oracles/lifecycle contract changed')
    for ref in joint['evidence']:
        require(ref['commit'] == CHAMP and digest(champ.blob(ref['path'])) == ref['sha256'], 'Joint original evidence differs')
    for ref in joint['beam_evidence']:
        require(digest(beam.blob(ref['path'])) == ref['sha256'], 'Joint host evidence differs')
    expected_paths = {'protocol_sha256':PAIR + '/protocol.proposal.json', 'runner_plan_sha256':PAIR + '/runner-plan.json',
                      'preparation_index_sha256':PREP + '/index.json', 'worksheet_sha256':READY + '/prompt-reserve-worksheet.json'}
    for key, path in expected_paths.items():
        require(receipt['actual_shared_input_bindings'][key] == {'commit':AOM,'path':path,'sha256':digest(aom.blob(path))},
                'Actual v10 input binding differs')
    helper = joint['current_shared_chart_fixture']['helper']
    require(helper == {'commit':AOM,'path':'algorithms/java/SqaProbe.java',
                       'sha256':digest(aom.blob('algorithms/java/SqaProbe.java'))}, 'Actual v10 helper binding differs')
    selected = aom.document(PREP + '/Chart-1/targets.json')['targets']
    excluded = aom.document(PREP + '/Chart-1/capability-exclusions.json')['excluded']
    candidate_keys = {target_key(r['target']) for r in joint['candidates']}
    require(len(selected) == joint['current_shared_chart_fixture']['existing_chart_targets']
            == receipt['existing_chart_selected_targets'] == 8
            and integration['existing_selected_chart_targets_to_preserve_and_regress'] == selected
            and len(candidate_keys) == 7 and candidate_keys <= {target_key(r['target']) for r in excluded}
            and not candidate_keys & {target_key(r) for r in selected}, 'Actual Chart8/Graphics7 partition differs')
    negatives = []
    for name in ('forged_shared_integration','forged_count_397','weakened_oracle','invented_token_reserve','lost_cpu_lock'):
        bad, bad_host, bad_joint = copy.deepcopy(receipt), copy.deepcopy(host), copy.deepcopy(joint)
        if name == 'forged_shared_integration':bad_joint['shared_integration_approved'] = True
        if name == 'forged_count_397':bad['actual_selected'] = 397
        if name == 'weakened_oracle':bad_joint['candidates'][0]['agreed_oracle'] = 'non-null'
        if name == 'invented_token_reserve':bad['final_reserve'] = 270033
        if name == 'lost_cpu_lock':bad_host['cpu_lock_exits'] = [0,0]
        rejected(name, lambda:validate_scope(bad, bad_host, bad_joint, integration), negatives)
    pixel_path = NATIVE + '/fixed_first-pixels/background_v.actual.argb'
    raw = beam.blob(pixel_path)
    corrupt = GitPixels(beam, NATIVE + '/fixed_first-pixels', {pixel_path:bytes([raw[0] ^ 1]) + raw[1:]})
    rejected('corrupt_raw_pixel', lambda:graphics.validate_records(native_records['fixed_first'], corrupt), negatives)
    bad_records = copy.deepcopy(native_records['fixed_first'])
    next(r for r in bad_records if r.get('case') == 'initialise_dataset')['target_invoked'] = False
    rejected('fixture_without_target_invocation', lambda:graphics.validate_records(
        bad_records, GitPixels(beam, NATIVE + '/fixed_first-pixels')), negatives)
    bad_trace = copy.deepcopy(native_records['fixed_method_entry_trace'])
    next(r for r in bad_trace if r.get('method_entry'))['descriptor'] = '()V'
    rejected('wrong_exact_descriptor', lambda:graphics.validate_trace(bad_trace, 0), negatives)
    require(checkpoint()[0] == pins, 'Champ shared pins changed')
    output.mkdir(parents=True, exist_ok=False)
    retained = ('receipt.json','joint-verdict.json','integration-requirements.json','received-tests-v2.command.json',
                'received-tests-v2.stdout.log','received-tests-v2.stderr.log','received-tests.command.json',
                'received-tests.stderr.log','host-review-v1/host-receipt.json',
                'host-review-v1/adapter-preexecution-seal.json')
    for name in retained:
        target = output / 'received' / name
        target.parent.mkdir(parents=True, exist_ok=True)
        target.write_bytes(beam.blob(BASE + '/' + name))
    (output / 'review-producer.py').write_bytes(Path(__file__).read_bytes())
    result = {'schema_version':1,'status':'received_bounded_graphics_joint_verdict_native_pixels_and_state',
              'checked_at_utc':datetime.now(timezone.utc).isoformat(),'beam_commit':BEAM,'champ_candidate_commit':CHAMP,
              'candidate_policy_id':graphics.POLICY['policy_id'],'received_manifest_entries_verified':len(manifest),
              'nested_manifest_entries_verified':nested,'original_received_provenance_entries_verified':len(provenance),
              'snapshot_git_pins_verified':len(source_rows),'snapshot_archive_reconstructed_and_verified':True,
              'production_source_files_per_revision_verified':654,'native_raw_commands_verified':15,
              'candidate_source_runtime_scope':'Champ v9 standalone candidate, separate from actual Aom v10 shared runtime',
              'candidate_source_shared_input_pins':len(native['shared_input_sha256']),
              'candidate_source_runtime_pins':len(native['runtime_source_sha256']),
              'candidate_signatures':7,'unique_bounded_cases':24,'plain_fixed_observations_received':48,
              'plain_buggy_observations_received':48,'traced_observations_received':48,
              'native_production_observations_audited':144,'cross_host_raw_raster_files_compared':288,
              'cross_host_pixel_state_equality_by_stage':equal,'exact_entries_per_revision':24,
              'candidate_fault_detected':False,'shifted_line_sensitivity_detected':True,
              'beam_focused_tests_received':8,'beam_focused_tests_skipped':0,
              'beam_worker':'beam-pc1','beam_cpu_slots':1,'beam_cpu_lock_exit_codes_received':[9,0],
              'native_java_version':native['java_version'],'negative_controls_rejected':negatives,
              'candidate_joint_acceptance_complete':True,'shared_integration_approved':False,
              'all_legal_domain_semantic_approval':False,'full_defects4j_evaluation':False,
              'line_or_branch_coverage_percentage':None,'actual_shared_aom_commit':AOM,'actual_shared_condition':CONDITION,
              'actual_shared_input_bindings':receipt['actual_shared_input_bindings'],'actual_shared_helper':helper,
              'actual_selected':390,'actual_exclusions':301,'denominator':691,
              'existing_chart_selected_targets_to_preserve':selected,
              'graphics_only_possible_next_counts':{'selected':397,'exclusions':294,'implemented':False},
              'prospective_shared_implementation_and_testing_can_proceed':True,
              'condition_creation_owner':'aom','new_condition_acceptance_pending':True,
              'gate_a_approved':False,'final_prompt_reserve':None,'primary_results_added':0,
              'live_requests':0,'queue_mutations':0,'java_runs_performed_by_this_inspector':0,
              'peer_code_executed_by_this_inspector':False,'champ_shared_checkpoint_pins_unchanged':len(pins)}
    write_json(output / 'receipt.json', result)
    write_json(output / 'source-bindings.json', {obj.commit:obj.bindings for obj in (beam, champ, aom)})
    write_json(output / 'champ-checkpoint-pins.json', pins)
    write_json(output / 'checksums.json', {p.relative_to(output).as_posix():sha256(p)
                                          for p in sorted(output.rglob('*')) if p.is_file()})
    return result


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--output', type=Path, required=True)
    result = run(parser.parse_args().output)
    print(json.dumps({key:result[key] for key in ('status','candidate_signatures','native_production_observations_audited',
                                                'actual_selected','actual_exclusions','shared_integration_approved')}))


if __name__ == '__main__':
    main()
