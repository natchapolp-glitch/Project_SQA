"""Audit sealed v9 inputs and compute an offline byte-guard reserve worksheet.

No provider connection, credentials, queue, quota ledger or approval mutation.
Historical calibration receipts cannot establish current quota or model limits.
"""
import argparse
from datetime import datetime, timezone
import json
from pathlib import Path
import tempfile
from types import SimpleNamespace

from .api_worker import FrozenSettings, WorkerBlocked, resolve_prepared_job
from .common import ROOT, contained, implementation_hashes, read_json, sha256, write_json
from .gate_a import route_coverage
from .fixture_policy import select, validate_recipe
from .preparation import clean_targets, digest, encoded, policy_for, validate
from .prepared_inputs import load
from .review_champ_preflight import observations

PREP = 'output/api854-20261003/prepare-v9-twenty-bug-development'
PROTOCOL = 'output/api854-20261003/aom-continuation-v9-development/protocol.proposal.json'
RUNNER = 'output/api854-20261003/aom-continuation-v9-development/runner-plan.json'
SOURCE = 'output/api854-20261003/prepare-v3'
RECEIPT = 'output/api854-provider-preflight-20261003/champ-kku-v5-calibration-evidence-v1.json'
ARTIFACTS = ('context-manifest.json', 'prepare-metadata.json', 'prompt.md',
             'targets.json', 'prepare-policy.json', 'fixture-recipes.json')


def require(condition, message):
    if not condition:
        raise ValueError(message)


def identities(targets):
    return {tuple(t[k] for k in ('class', 'constructor_types', 'method', 'parameter_types'))
            for t in clean_targets(targets)}


def partition(folder, old, metadata):
    """Retain the complete original discovery partition; no outcome selection."""
    document = read_json(folder/'targets.json')
    exclusions = read_json(folder/'capability-exclusions.json')
    selected = identities(document['targets'])
    excluded = identities([r['target'] for r in exclusions['excluded']])
    original = identities(read_json(old/'targets.json')['targets'])
    expected_selected, expected_excluded = select(read_json(old/'targets.json')['targets'], metadata['fixture_policy_id'])
    require(selected == identities(expected_selected) and exclusions['excluded'] == expected_excluded,
            'Capability partition differs from the predeclared structural policy')
    require(not selected & excluded and selected | excluded == original,
            'Selected/excluded partition differs from original declarations')
    require(len(excluded) == len(exclusions['excluded']) and
            identities(exclusions['selected']) == selected and
            exclusions['team_approval'] is False and
            exclusions['fixture_policy_id'] == metadata['fixture_policy_id'],
            'Capability exclusions contain duplicate identities or differ from selected inputs')
    return len(selected), len(excluded)


def consumer_check(folder, row, protocol, protocol_hash, *, reserve=None):
    """Call production CPU loader and both API consumers with file transport."""
    data = {name: (folder/name).read_bytes() for name in ARTIFACTS}
    metadata = read_json(folder/'prepare-metadata.json')
    client = SimpleNamespace(download=lambda artifact: data[artifact['name']])
    job = {'project': row['project'], 'bug_id': row['bug_id'], 'owner': row['owner'],
           'run_id': 'offline-v9-shared-input-audit', 'protocol_hash': protocol_hash,
           'stage': 'generate', 'approach': 'kku-claude',
           'payload': {'stage_history': [{'stage': 'prepare', 'outcome': 'prepared',
                       'attempt_id': 'offline-sealed-input', 'metadata': metadata,
                       'artifacts': [{'name': name, 'size': len(raw), 'sha256': digest(raw)}
                                     for name, raw in data.items()]}]}}
    generation = protocol['generation']
    settings = SimpleNamespace(protocol_hash=protocol_hash, models=protocol['models'],
        generation_owners=tuple(generation['owners']),
        context_policy_id=generation['context_policy_id'],
        prompt_policy_id=generation['prompt_policy_id'],
        prompt_token_reserve=len(data['prompt.md']) if reserve is None else reserve,
        handoff_contract='beam-v1', prepare_contract=generation['prepare_contract'],
        protocol=protocol)
    cpu = load(client, job, protocol)
    require(cpu['metadata'] == metadata and
            identities(cpu['targets']) == identities(read_json(folder/'targets.json')['targets']) and
            cpu['fixture_recipe'] == json.loads(data['fixture-recipes.json']),
            'CPU shared input differs from sealed artifacts')
    for approach in ('kku-claude', 'kku-gemini'):
        job['approach'] = approach
        api = resolve_prepared_job(client, job, settings)
        require(api.prompt.encode('utf-8') == data['prompt.md'] and
                api.source_hash == metadata['context_source_hash'],
                'API shared prompt/source differs from sealed artifacts')
    return {'cpu_shared_loader_verified': True, 'api_approaches_verified': ['kku-claude', 'kku-gemini'],
            'actual_cpu_defects4j_generation_rerun': False,
            'contract_check_uses_isolated_settings': True}


def proposal_rejections(path, protocol):
    try:
        FrozenSettings.load(path)
    except WorkerBlocked as error:
        actual = str(error)
    else:
        raise ValueError('Development proposal unexpectedly accepted')
    require(actual == 'protocol_not_frozen', 'Development rejection reason differs')
    # This changes an isolated copy only; the received proposal stays immutable.
    forged = {**protocol, 'approval_state': 'frozen'}
    with tempfile.TemporaryDirectory() as directory:
        copy = Path(directory)/'isolated-forged-frozen.json'
        copy.write_bytes(encoded(forged))
        try:
            FrozenSettings.load(copy)
        except WorkerBlocked as error:
            forced = str(error)
        else:
            raise ValueError('Development contract opened by an approval flag')
    require(forced == 'prepare_contract_not_frozen', 'Development contract guard differs')
    return {'actual_proposal': actual, 'isolated_approval_flag_changed': forced}


def inspect(root=ROOT, *, preparation=PREP, protocol_path=PROTOCOL, runner_path=RUNNER,
            source=SOURCE, receipt_path=RECEIPT):
    root = Path(root).resolve()
    prep, protocol_file, runner, original, receipt_file = (
        contained(root, p) for p in (preparation, protocol_path, runner_path, source, receipt_path))
    index, protocol = read_json(prep/'index.json'), read_json(protocol_file)
    generation = protocol['generation']
    policy = policy_for(generation['prepare_contract'])
    require(policy['contract'] == 'aom-beam-prepare-v9-development', 'Require v9 development inputs')
    require(contained(root, protocol['preparation_artifacts']) == prep,
            'Protocol preparation path differs')
    require(generation['prompt_token_reserve'] is None and generation['max_tokens'] == 4096 and
            generation['temperature'] == 0 and protocol['enabled_stages'] == [] and
            index['generation_ready'] is False and index['team_approval'] is False and
            index['primary'] is False and index['live_requests'] == index['queue_mutations'] == 0 and
            all(value is False for value in protocol['gate_a']['reviewed_by'].values()),
            'Unexpected approval, enabled stage or final reserve')
    runtime = implementation_hashes()
    require(protocol['source_sha256'] == runtime and index['runtime_source_sha256'] == runtime,
            'New v9 runtime pins differ from actual implementation')
    require(protocol['runner_plan_sha256'] == sha256(runner), 'Protocol/runner hash differs')
    require((protocol_file.with_name(protocol_file.name+'.sha256')).read_text().split()[0]
            == sha256(protocol_file), 'Protocol sidecar differs')
    require(index['source_v3_index_sha256'] == sha256(original/'index.json'), 'Original v3 lineage differs')
    core_path = root/'experiments/configs/api854-20261003/protocol.core-frozen.json'
    ownership_path = root/'experiments/configs/api854-20261003/ownership.json'
    expected = {(r['project'], r['bug_id'], r['owner']) for r in read_json(core_path)['pilot_bugs']}
    require(len(index['records']) == 20 and
            {(r['project'], r['bug_id'], r['owner']) for r in index['records']} == expected,
            'Require exact 20-bug pilot identities/ownership')
    old_index = read_json(original/'index.json')
    require({(r['project'], r['bug_id'], r['owner']) for r in old_index['records']} == expected and
            sum(r['target_count'] for r in old_index['records']) == 691,
            'Original 691-declaration inventory differs')
    routes = route_coverage(read_json(runner), read_json(ownership_path))
    require(routes['covered_stage_keys'] == routes['expected_stage_keys'] == 10248 and
            not routes['gaps'], 'Runner routing differs')
    historical = observations(read_json(receipt_file))
    records, worksheet, enum_excluded, checksum_entries = [], [], [], 0
    for row in index['records']:
        folder = contained(prep, f"{row['project']}-{row['bug_id']}")
        old = contained(original, folder.name)
        for base in (old, folder):
            checksums = read_json(base/'checksums.json')
            required = ((set(ARTIFACTS) | {'revision-proof.json', 'capability-exclusions.json'})
                        if base == folder else
                        ((set(ARTIFACTS)-{'fixture-recipes.json'}) | {'revision-proof.json'}))
            require(required <= checksums.keys(), 'Required artifact missing from checksums')
            for name, expected_hash in checksums.items():
                require(sha256(contained(base, name)) == expected_hash, 'Artifact checksum differs: '+name)
                checksum_entries += 1
        manifest, metadata = read_json(folder/'context-manifest.json'), read_json(folder/'prepare-metadata.json')
        prompt = (folder/'prompt.md').read_bytes()
        require(manifest['project'] == row['project'] and manifest['bug_id'] == row['bug_id'] and
                manifest['revision'] == str(row['bug_id'])+'f' and
                all(row.get(k) == v for k, v in metadata.items()) and
                metadata['prompt_utf8_bytes'] == len(prompt) and
                metadata['semantic_validity'] == 'pending_review' and
                metadata['approval_state'] == 'proposal_pending_three_owner_review',
                'Index/prepared identity, metadata, pending approval or bytes differ')
        validate(manifest, metadata, prompt, (folder/'targets.json').read_bytes(),
                 (folder/'prepare-policy.json').read_bytes(), require_eligible=True,
                 fixture_recipe=read_json(folder/'fixture-recipes.json'))
        validate_recipe(read_json(folder/'fixture-recipes.json'), runtime, policy=policy['fixture_policy'])
        old_metadata = read_json(old/'prepare-metadata.json')
        require(metadata['previous_hashes'] == {'v3_prompt_sha256': old_metadata['prompt_sha256'],
                'v3_targets_sha256': old_metadata['targets_sha256'],
                'v3_index_sha256': sha256(original/'index.json')}, 'Per-bug v3 lineage differs')
        proof = read_json(folder/'revision-proof.json')
        require(proof['verified'] is True and proof['head'] == proof['fixed_tag_commit'] and
                (folder/'revision-proof.json').read_bytes() == (old/'revision-proof.json').read_bytes(),
                'Fixed revision proof differs')
        original_paths = {entry['path'] for entry in read_json(old/'context-manifest.json')['source_files']}
        factory_paths = policy['reviewed_factory_sources'] if (row['project'],row['bug_id'])==('Math',1) else {}
        require({entry['path'] for entry in manifest['source_files']} == original_paths | set(factory_paths),
            'Unreviewed supplemental context source')
        for entry in manifest['source_files']:
            file = contained(folder/'fixed-source', entry['path'])
            original_file = (root/'docs/api854/evidence/beam-champ-math-field-20261003-attempt2/supplemental-fixed-source'/Path(entry['path']).name
                if entry['path'] in factory_paths else contained(old/'fixed-source', entry['path']))
            require(sha256(file) == entry['sha256'] and file.stat().st_size == entry['bytes'] and
                    file.read_bytes() == original_file.read_bytes(),
                    'Fixed context bytes/lineage differ')
            if entry['path'] in factory_paths:
                require(entry['sha256'] == factory_paths[entry['path']], 'Reviewed factory hash differs')
        selected, excluded = partition(folder, old, metadata)
        require(selected == row['target_count'] and excluded == row['capability_exclusion_count'],
                'Declaration partition count differs')
        if row['project'] == 'JacksonXml' and row['bug_id'] == 1:
            enum_excluded = [r['target'] for r in read_json(folder/'capability-exclusions.json')['excluded']
                if r['target']['class'] == 'com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser'
                and r['target']['method'] in {'enable', 'disable', 'configure', 'isEnabled'}
                and 'FromXmlParser$Feature' in r['target']['parameter_types']]
        consumers = consumer_check(folder, row, protocol, sha256(protocol_file))
        records.append({'project': row['project'], 'bug_id': row['bug_id'], 'owner': row['owner'],
            'target_count': selected, 'capability_exclusion_count': excluded,
            'prompt_utf8_bytes': len(prompt), 'prompt_sha256': digest(prompt),
            'targets_sha256': metadata['targets_sha256'], 'fixture_recipes_sha256': metadata['fixture_recipes_sha256'],
            'context_source_hash': metadata['context_source_hash'], **consumers})
        for model in historical:
            # Identical to KKUClient.complete/_request serialization, without transport.
            request = {'model': model['model_id'], 'messages': [{'role': 'user', 'content': prompt.decode('utf-8')}],
                       'max_tokens': 4096, 'stream': False, 'temperature': 0}
            wire = json.dumps(request, ensure_ascii=False, allow_nan=False).encode('utf-8')
            floor = len(prompt)+4096
            worksheet.append({'project': row['project'], 'bug_id': row['bug_id'], 'model_id': model['model_id'],
                'prompt_utf8_bytes': len(prompt), 'proposed_output_cap': 4096,
                'per_prompt_numerical_floor_before_unknown_framing': floor,
                'historical_remaining_observed_at_utc': model['observed_at_utc'],
                'historical_remaining_tokens': model['daily_remaining_tokens'],
                'historical_headroom_after_per_prompt_floor_before_unknown_framing': model['daily_remaining_tokens']-floor,
                'serialized_request_utf8_bytes': len(wire), 'serialized_request_sha256': digest(wire),
                'json_serialization_overhead_bytes': len(wire)-len(prompt),
                'json_overhead_is_provider_framing_H': False, 'provider_prompt_token_count': None,
                'framing_bound_H': None, 'live_reservation_ready': False})
    largest = max(records, key=lambda r: r['prompt_utf8_bytes'])
    maximum = largest['prompt_utf8_bytes']
    target_count = sum(r['target_count'] for r in records)
    exclusion_count = sum(r['capability_exclusion_count'] for r in records)
    require(target_count == index['target_count'] == 380 and exclusion_count == index['capability_exclusion_count'] == 311
            and index['required_common_declarations'] == target_count+exclusion_count == 691,
            'v9 selected/unsupported aggregate differs')
    require(maximum == index['max_prompt_utf8_bytes'], 'Actual maximum differs from index')
    require(len(enum_excluded) == 4, 'Four pending empty-enum targets must remain explicit exclusions')
    shared_floor = maximum+4096
    for item in worksheet:
        item['shared_frozen_reservation_numerical_floor_before_unknown_framing'] = shared_floor
        item['historical_headroom_after_shared_floor_before_unknown_framing'] = item['historical_remaining_tokens']-shared_floor
    bound_paths = (protocol_file, runner, prep/'index.json', original/'index.json', receipt_file, core_path, ownership_path)
    return {'schema_version': 1, 'checked_at_utc': datetime.now(timezone.utc).isoformat(),
        'scope': 'Offline exact v9 shared-input audit and conditional reserve worksheet; development only',
        'review_implementation_sha256': sha256(__file__),
        'input_sha256': {p.relative_to(root).as_posix(): sha256(p) for p in bound_paths},
        'runtime_source_sha256': runtime, 'runtime_source_pins_verified': len(runtime),
        'checksum_entries_verified_v3_and_v9': checksum_entries, 'routing': routes,
        'records': records, 'shared_prepared_bugs': 20, 'target_count': target_count,
        'capability_exclusion_count': exclusion_count, 'required_common_declarations': 691,
        'original_314_unsupported_closed': False, 'full_691_meaningful_oracle_acceptance': False,
        'pending_empty_enum_targets': enum_excluded, 'empty_enum_joint_decision_approved': False,
        'prompt_utf8_bytes_sum_once_per_bug': sum(r['prompt_utf8_bytes'] for r in records),
        'largest_prompt': largest, 'max_prompt_utf8_bytes': maximum,
        'prompt_bytes_are_provider_token_counts': False, 'provider_token_count_for_v9_prompts': None,
        'shared_prompt_reserve_numerical_floor_before_unknown_framing': maximum,
        'prompt_reserve_formula': f'{maximum} + H', 'proposed_output_cap': 4096,
        'request_reservation_formula': f'{shared_floor} + H',
        'request_reservation_numerical_floor_before_unknown_framing': shared_floor,
        'sum_request_reservations_per_model_numerical_floor': 20*shared_floor,
        'sum_request_reservations_both_models_numerical_floor': 40*shared_floor,
        'sum_reservations_is_actual_usage_or_required_simultaneous_quota': False,
        'framing_bound_H': None, 'final_prompt_reserve': None,
        'provider_context_output_limits_verified': False, 'model_context_limit_tokens': None,
        'model_max_output_limit_tokens': None, 'effective_backend_settings_verified': False,
        'quota_bucket_window_reset_expiry_verified': False, 'historical_models': historical,
        'historical_response_raw_bytes_reverified': False,
        'historical_receipt_caveat': 'Public packet has response fields and hashes, not original raw response bytes; observed remaining is historical, not current quota.',
        'conditional_reserve_worksheet': worksheet,
        'proposal_rejections': proposal_rejections(protocol_file, protocol),
        'generation_authorized': False, 'gate_a_passed': False, 'primary_results': False,
        'real_kku_requests_this_review': 0, 'live_queue_mutations_this_review': 0,
        'quota_ledger_modified': False, 'team_or_human_approval_inferred': False,
        'recompute_after_any_recipe_policy_prompt_or_runtime_change': True}


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--preparation', default=PREP)
    parser.add_argument('--protocol', default=PROTOCOL)
    parser.add_argument('--runner', default=RUNNER)
    parser.add_argument('--output', type=Path, required=True)
    args = parser.parse_args()
    result = inspect(preparation=args.preparation, protocol_path=args.protocol, runner_path=args.runner)
    write_json(args.output, result)
    print(json.dumps({k: result[k] for k in ('shared_prepared_bugs', 'target_count',
        'capability_exclusion_count', 'max_prompt_utf8_bytes', 'final_prompt_reserve', 'gate_a_passed')}))
