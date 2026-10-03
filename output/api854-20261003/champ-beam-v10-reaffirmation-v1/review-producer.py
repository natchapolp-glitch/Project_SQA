"""Receive Beam's immutable v10 reaffirmation; preserve the earlier scoped closure."""
import argparse
from datetime import datetime, timezone
from pathlib import Path
import json

from .common import ROOT, read_json, sha256, write_json
from .review_joint_recipe_intake import digest, require
from .review_v10_readiness import AOM, CONDITION, BatchedObjects
from .verify_chronology_development import checkpoint

BEAM = 'f708595d9fac74a603f8d5d6cb9bf0485f34c561'
EXECUTION = '0ca73ee6e30f719d23b6b6b89da22fdf953d5160'
CHAMP = 'fd2e16abef02740bdf9a0f6cd0556a038a0912f7'
CLOSURE = '1b0bcbceecbbf898cf908806e0d7820360db0710'
BASE = 'output/api854-20261003/beam-champfd-v10-return-v1'
LOCAL = 'output/api854-20261003/champ-beam-v10-acceptance-v1'


def preload(objects, paths):
    missing = set(paths) - objects.cache.keys()
    if missing:
        objects.preload(missing)


def run(output):
    output = Path(output).resolve()
    require(output.is_relative_to(ROOT / 'output') and not output.exists(), 'Require a new output directory')
    pins, _ = checkpoint()
    objects = {}
    def source(commit):
        if commit not in objects:
            objects[commit] = BatchedObjects(commit)
        return objects[commit]
    beam = source(BEAM)
    manifest = beam.document(BASE + '/checksums.json')
    preload(beam, [BASE + '/' + p for p in manifest])
    require(beam.checksums(BASE) == 13, 'Reaffirmation packet inventory differs')
    receipt = beam.document(BASE + '/receipt.json')
    refs = beam.document(BASE + '/verified-git-binding-entries.json')
    unique = {(r['commit'], r['path']) for r in refs}
    require(len(refs) == receipt['binding_entries_verified'] == 441
            and len(unique) == receipt['unique_peer_git_blobs_verified'] == 437, 'Binding/unique counts differ')
    for commit in {r['commit'] for r in refs}:
        preload(source(commit), [r['path'] for r in refs if r['commit'] == commit])
    for ref in refs:
        require(digest(source(ref['commit']).blob(ref['path'])) == ref['sha256'], 'Peer Git binding differs')
    copies = beam.document(BASE + '/received-provenance.json')
    require(len(copies) == 9, 'Received copy count differs')
    for row in copies:
        require(row['commit'] == CHAMP and digest(source(CHAMP).blob(row['source_path']))
                == row['sha256'] == digest(beam.blob(row['received_path'])), 'Received original bytes differ')
    prior_objects = source(CLOSURE)
    prior_manifest = prior_objects.document(LOCAL + '/checksums.json')
    preload(prior_objects, [LOCAL + '/' + p for p in prior_manifest])
    require(prior_objects.checksums(LOCAL) == 10, 'Earlier Champ closure packet differs')
    for path, value in prior_manifest.items():
        require(sha256(ROOT / LOCAL / path) == value, 'Local earlier closure artifact changed')
    prior = prior_objects.document(LOCAL + '/receipt.json')
    for ref in receipt['beam_final_condition_verdict_bindings']:
        require(ref['commit'] == EXECUTION and digest(source(EXECUTION).blob(ref['path'])) == ref['sha256'],
                'Executed Beam evidence binding differs')
        if ref['path'] in prior['original_evidence_bindings']['sha256']:
            require(prior['original_evidence_bindings']['sha256'][ref['path']] == ref['sha256'],
                    'Earlier Champ reviewed evidence differs')
    require(receipt['champ_commit'] == CHAMP and receipt['aom_commit'] == AOM
            and receipt['beam_execution_evidence_commit'] == prior['beam_commit'] == EXECUTION
            and receipt['condition'] == prior['condition'] == CONDITION
            and receipt['final_input_bindings'] == prior['input_bindings'], 'Reaffirmation condition differs')
    for ref in receipt['final_input_bindings'].values():
        require(ref['commit'] == AOM and digest(source(AOM).blob(ref['path'])) == ref['sha256'], 'Actual v10 pin differs')
    reviewed = read_json(ROOT / 'output/api854-20261003/champ-v10-readiness-review-v2/receipt.json')
    require(receipt['runtime_source_sha256'] == reviewed['runtime_source_sha256']
            and len(receipt['runtime_source_sha256']) == 41, 'Reviewed v10 runtime differs')
    for path, value in receipt['runtime_source_sha256'].items():
        require(digest(source(AOM).blob(path)) == value, 'Exact v10 runtime Git pin differs')
    require((receipt['bugs'], receipt['selected'], receipt['exclusions'], receipt['denominator']) == (20,390,301,691)
            and (receipt['consumer_combinations_verified'], receipt['bounded_component_cases_verified'],
                 receipt['bounded_fixed_observations_verified'], receipt['worksheet_pairs_verified']) == (80,64,128,40)
            and receipt['consumer_review_passed'] is True and receipt['bounded_component_semantic_review_passed'] is True
            and receipt['native_technical_host_receipt_verified'] is True
            and receipt['peer_bounded_observations_match_beam'] is True, 'Missing scoped reaffirmation')
    require(receipt['all_390_semantic_approval'] is False and receipt['provider_current_evidence_verified'] is False
            and receipt['gate_a_approved'] is False and receipt['final_reserve'] is None
            and receipt['chronology_graphics_enum_adopted_into_v10'] is False
            and receipt['new_target_executions'] == receipt['new_test_executions'] == receipt['live_kku_requests']
            == receipt['live_queue_mutations'] == receipt['quota_ledger_mutations'] == receipt['primary_added'] == 0,
            'Reaffirmation exceeded scoped closure')
    require(receipt['existing_beam_host'] == prior['beam_worker'] == 'beam-pc1'
            and receipt['cpu_slots'] == prior['beam_cpu_slots'] == 1 and receipt['cpu_lock_exits'] == [9,0],
            'Previously accepted host/lock differs')
    worksheet_path = 'output/api854-20261003/champ-v10-readiness-review-v2/prompt-model-worksheet.json'
    require(source(CHAMP).blob(worksheet_path) == (ROOT / worksheet_path).read_bytes()
            and digest(source(CHAMP).blob(worksheet_path)) == prior['existing_champ_worksheet_sha256'],
            'Previously reviewed 40-pair worksheet changed')
    release_path = 'output/api854-20261003/beam-champfd-v10-release-verification-v1.json'
    release = beam.document(release_path)
    require(release['status'] == 'pass' and release['receipt_sha256'] == digest(beam.blob(BASE + '/receipt.json'))
            and release['root_checksum_entries_verified'] == len(manifest)
            and release['peer_unique_git_blobs_verified'] == len(unique)
            and release['binding_entries_verified'] == len(refs) and release['received_git_copies_verified'] == len(copies),
            'Release verification binding differs')
    require(checkpoint()[0] == pins, 'Champ shared checkpoint changed')
    output.mkdir(parents=True, exist_ok=False)
    for name in ('receipt.json','checksums.json','received-provenance.json'):
        (output / ('beam-' + name)).write_bytes(beam.blob(BASE + '/' + name))
    (output / 'beam-release-verification.json').write_bytes(beam.blob(release_path))
    (output / 'review-producer.py').write_bytes(Path(__file__).read_bytes())
    result = {'schema_version':1,'status':'v10_scoped_closure_reaffirmed_no_pending_beam_receipt',
              'checked_at_utc':datetime.now(timezone.utc).isoformat(),'received_beam_commit':BEAM,
              'original_beam_execution_commit':EXECUTION,'earlier_champ_closure_commit':CLOSURE,
              'reviewed_champ_readiness_commit':CHAMP,'aom_commit':AOM,'condition':CONDITION,
              'received_manifest_entries_verified':13,'binding_entries_verified':441,'unique_peer_git_blobs_verified':437,
              'received_copies_verified':9,'runtime_pins_verified':41,'unchanged_prior_champ_manifest_entries':10,
              'closed_items':['four_approach_input_bindings','bounded_component_oracles','beam_pc1_cpu1_technical_host'],
              'consumer_combinations_received':80,'bounded_cases_received':64,'fixed_observations_received':128,
              'unchanged_reviewed_prompt_model_pairs':40,'selected':390,'exclusions':301,'denominator':691,
              'remaining_owner_host_bindings_approved':False,'all_390_semantic_approval':False,
              'provider_current_evidence_verified':False,'final_token_reserve':None,'gate_a_approved':False,
              'new_conditions_inherit_this_acceptance':False,'new_target_executions':0,'new_test_executions':0,
              'live_kku_requests':0,'live_queue_mutations':0,'quota_ledger_mutations':0,'primary_added':0,
              'unchanged_champ_checkpoint_pins':len(pins)}
    write_json(output / 'receipt.json', result)
    write_json(output / 'source-bindings.json', {obj.commit:obj.bindings for obj in objects.values()})
    write_json(output / 'checksums.json', {p.name:sha256(p) for p in sorted(output.iterdir()) if p.is_file()})
    return result


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--output', type=Path, required=True)
    result = run(parser.parse_args().output)
    print(json.dumps({k:result[k] for k in ('status','binding_entries_verified','unique_peer_git_blobs_verified',
                                         'selected','exclusions','gate_a_approved')}))
