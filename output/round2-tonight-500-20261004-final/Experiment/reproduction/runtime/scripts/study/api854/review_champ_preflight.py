"""Read-only acceptance of Champ's exact v5 preflight checkpoint.

Validates received bytes and request/response arithmetic. Does not contact KKU,
update a quota ledger, approve semantic review, freeze a protocol or seed a queue.
"""
import argparse
from datetime import datetime, timezone
import hashlib
import json
from pathlib import Path
import subprocess

from .common import ROOT, contained, read_json, sha256, write_json
from .gate_a import route_coverage
from .preparation import validate
from .api_worker import FrozenSettings, WorkerBlocked

CHAMP = '19ef7ae61472ad0e1c6a853dd3f1d081804c03a4'
MODELS = {'claude-sonnet-5', 'gemini-3.5-flash-lite'}
RECEIPT = 'output/api854-provider-preflight-20261003/champ-kku-v5-calibration-evidence-v1.json'
PROPOSAL = 'experiments/configs/api854-20261003/champ-composed-v5.proposal.json'
PREP = 'output/api854-20261003/prepare-v5-five-bug-development'
RUNNER = 'output/api854-20261003/aom-continuation-v5/runner-plan.json'


def require(condition, message):
    if not condition:
        raise ValueError(message)


def observations(receipt):
    rows = receipt['calibration_observations']
    require(len(rows) == 2 and {r['model_id'] for r in rows} == MODELS, 'Require exactly two selected models')
    result = []
    for row in rows:
        request, response = row['request'], row['response']
        payload = json.dumps(request, ensure_ascii=False, allow_nan=False).encode()
        require(hashlib.sha256(payload).hexdigest() == row['request_json_sha256']
                and len(payload) == row['request_json_utf8_bytes'], 'Serialized request differs')
        require(request['model'] == response['model'] == row['model_id'], 'Model identity differs')
        require(request['temperature'] == 0 and request['max_tokens'] == 4096
                and request['stream'] is False and row['http_status'] == 200, 'Requested settings not accepted')
        require(row['temperature0_request_accepted'] is True
                and row['max_tokens4096_request_accepted'] is True, 'Acceptance flag differs')
        require(len(response['choices']) == 1 and response['choices'][0]['message']['content'] == 'OK'
                and response['choices'][0]['finish_reason'] == 'stop', 'Preflight response differs')
        usage, quota = response['usage'], response['model_quota']
        require(all(type(usage[k]) is int and usage[k] >= 0 for k in ('prompt_tokens', 'completion_tokens', 'total_tokens')),
                'Invalid usage counters')
        require(usage['total_tokens'] == usage['prompt_tokens'] + usage['completion_tokens'], 'Usage arithmetic differs')
        require(all(type(quota[k]) is int and quota[k] >= 0 for k in
                    ('daily_quota_tokens', 'daily_usage_tokens', 'daily_remaining_tokens')), 'Invalid quota counters')
        require(quota['daily_quota_tokens'] - quota['daily_usage_tokens'] == quota['daily_remaining_tokens'],
                'Quota arithmetic differs')
        require(datetime.fromisoformat(row['observed_at_utc']).utcoffset() is not None, 'Observation time needs timezone')
        unknowns = ('backend_model_revision', 'model_context_limit_tokens', 'model_max_output_limit_tokens',
                    'provider_framing_bound_tokens', 'quota_bucket_id_returned', 'quota_window_id_returned',
                    'quota_reset_at_utc_returned', 'observation_expiry_at_utc')
        require(all(row[k] is None for k in unknowns), 'This acceptance supports the incomplete original checkpoint only')
        require(row['effective_backend_temperature_verified'] is False and row['effective_output_cap_verified'] is False
                and row['usable_for_live_quota_ledger_import'] is False, 'Request acceptance cannot imply effective settings or ledger readiness')
        result.append({'model_id': row['model_id'], 'account_alias': row['account_alias'],
                       'observed_at_utc': row['observed_at_utc'], 'request_settings_accepted': True,
                       'effective_backend_settings_verified': False, 'remaining_is_current_verified': False,
                       'daily_quota_tokens': quota['daily_quota_tokens'], 'daily_remaining_tokens': quota['daily_remaining_tokens'],
                       'reported_usage': usage, 'ledger_import_authorized': False, 'missing_fields': list(unknowns)})
    require(sum(r['reported_usage']['total_tokens'] for r in result) == receipt['calibration_total_reported_tokens'] == 60,
            'Total calibration usage differs')
    for field in ('test_generation_requests', 'pilot_generation_requests', 'live_queue_mutations', 'automatic_account_switches', 'probe_retries'):
        require(receipt[field] == 0, 'Unexpected preflight action: ' + field)
    require(receipt['final_prompt_reserve'] is None and receipt['framing_bound_H'] is None
            and receipt['gate_a_passed'] is False and receipt['live_pilot_authorized'] is False, 'Unexpected gate/reserve claim')
    return result


def inspect(root=ROOT):
    root = Path(root).resolve()
    def path(name):
        return contained(root, name)
    def bound(name):
        actual = path(name).read_bytes()
        received = subprocess.check_output(['git', 'show', CHAMP + ':' + name], cwd=root)
        require(actual == received, 'Received Champ bytes changed: ' + name)
        return sha256(path(name))
    received = {name: bound(name) for name in (RECEIPT, PROPOSAL, PROPOSAL + '.sha256',
        'docs/api854/CHAMP_V5_KKU_ACCEPTANCE_TH.md',
        'output/api854-provider-preflight-20261003/champ-aom-v5-input-runner-audit-v1.json',
        'output/api854-provider-preflight-20261003/champ-v5-validation-v1.json')}
    require(path(PROPOSAL + '.sha256').read_text().split()[0] == sha256(path(PROPOSAL)), 'Proposal sidecar differs')
    receipt, protocol = read_json(path(RECEIPT)), read_json(path(PROPOSAL))
    models = observations(receipt)
    for name, expected in protocol['source_sha256'].items():
        require(sha256(path(name)) == expected, 'Runtime pin differs: ' + name)
    require(protocol['runner_plan_sha256'] == sha256(path(RUNNER)), 'Protocol/runner binding differs')
    plan = read_json(path(RUNNER))
    routes = route_coverage(plan, read_json(path('experiments/configs/api854-20261003/ownership.json')))
    require(routes['expected_stage_keys'] == routes['covered_stage_keys'] == 10248 and not routes['gaps'], 'Runner route gap')
    require({r['worker_id'] for r in plan['runners']} == {'aom-pc1', 'beam-pc1', 'champ-pc1'}, 'Host plan differs')
    index, verified = read_json(path(PREP + '/index.json')), 0
    require(len(index['records']) == 5 and index['target_count'] == 124, 'Five-bug scope differs')
    for row in index['records']:
        folder = path(PREP + f"/{row['project']}-{row['bug_id']}")
        for name, expected in read_json(folder / 'checksums.json').items():
            require(sha256(contained(folder, name)) == expected, 'Prepared artifact checksum differs')
            verified += 1
        validate(read_json(folder/'context-manifest.json'), read_json(folder/'prepare-metadata.json'),
                 (folder/'prompt.md').read_bytes(), (folder/'targets.json').read_bytes(), (folder/'prepare-policy.json').read_bytes(),
                 require_eligible=True, fixture_recipe=read_json(folder/'fixture-recipes.json'))
    require(verified == 63 and index['generation_ready'] is False and index['team_approval'] is False, 'Preparation scope differs')
    blocked = {}
    for name in (PROPOSAL, 'output/api854-20261003/aom-continuation-v5/protocol.proposal.json'):
        try:
            FrozenSettings.load(path(name))
        except WorkerBlocked as error:
            blocked[name] = str(error)
        else:
            raise ValueError('Development protocol unexpectedly accepted')
    require(all(reason == 'protocol_not_frozen' for reason in blocked.values()), 'Proposal rejection reason differs')
    checkpoint = read_json(path('output/api854-20261003/aom-independent-checkpoint.json'))
    require(checkpoint['source_prepared'] == checkpoint['fixed_compile_passed'] == 284 and checkpoint['primary_completed'] == 0,
            'Independent preparation scope differs')
    return {'schema_version': 1, 'checked_at_utc': datetime.now(timezone.utc).isoformat(),
        'received_champ_commit': CHAMP, 'scope': 'Exact received v5 checkpoint acceptance; not final Gate A',
        'received_file_sha256': received, 'review_implementation_sha256': sha256(__file__),
        'runtime_source_pins_verified': len(protocol['source_sha256']), 'runner_plan_sha256': sha256(path(RUNNER)),
        'runner_stage_keys_verified': routes['covered_stage_keys'], 'prepare_checksum_entries_verified': verified,
        'shared_prepared_bugs': 5, 'shared_selected_targets': 124, 'models': models,
        'historical_calibration_requests': 2, 'historical_catalog_requests': 2, 'historical_calibration_usage_tokens': 60,
        'raw_response_body_hashes_reverified': False, 'raw_response_note': 'Public packet contains response fields and raw hashes, not original raw body bytes.',
        'max_prompt_utf8_bytes_five_bug_only': index['max_prompt_utf8_bytes'], 'final_prompt_reserve': None,
        'source_and_fixed_compile_passed_aom': 284, 'proposal_rejections': blocked,
        'gate_a_passed': False, 'generation_authorized': False, 'ledger_import_authorized': False,
        'real_kku_requests_this_review': 0, 'live_queue_mutations_this_review': 0, 'primary_completed': 0,
        'remaining': ['Final shared preparation/semantic review for all 20 pilot bugs and 691 declarations',
                      'Effective model limits/settings, provider framing, quota bucket/reset/expiry',
                      'Final protocol/runtime/runner hashes and joint team Gate A acceptance']}


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--output', type=Path, required=True)
    args = parser.parse_args()
    result = inspect()
    write_json(args.output, result)
    print(json.dumps({'shared_bugs': 5, 'targets': 124, 'calibration_usage_tokens': 60, 'gate_a_passed': False}))
