"""Bind post-repair development inputs to current runtime; never enable stages."""
import argparse
import copy
from pathlib import Path

from .common import ROOT, read_json, sha256, implementation_hashes
from .preparation import POLICY_V7, encoded, digest


def compose(preparation, output):
    preparation, output = Path(preparation).resolve(), Path(output).resolve()
    index = read_json(preparation / 'index.json')
    if (index['runtime_source_sha256'] != implementation_hashes()
            or index['policy_sha256'] != digest(encoded(POLICY_V7))
            or len(index['records']) != 20 or index['generation_ready'] is not False):
        raise ValueError('Require current-runtime twenty-bug v7 development inputs')
    relative = preparation.relative_to(ROOT).as_posix()
    protocol = copy.deepcopy(read_json(ROOT / 'output/api854-20261003/aom-continuation-v6-development/protocol.proposal.json'))
    runner = (ROOT / 'output/api854-20261003/aom-continuation-v6-development/runner-plan.json').read_bytes()
    output.mkdir(parents=True, exist_ok=False)
    (output / 'runner-plan.json').write_bytes(runner)
    protocol.update(condition='api854-20261003-twenty-bug-development-v7',
        approval_state='development_proposal_not_frozen', state='development_proposal_pending_team_review',
        status='development_proposal_pending_team_review', enabled_stages=[],
        preparation_artifacts=relative,
        preparation_import_evidence={'path':relative + '/index.json', 'sha256':sha256(preparation / 'index.json'),
            'generation_ready':False},
        prepare_policy_sha256=index['policy_sha256'], fixture_policy_id=POLICY_V7['fixture_policy'],
        source_sha256=implementation_hashes(), runner_plan_sha256=sha256(output / 'runner-plan.json'),
        fixture_development_scope={'bugs':[{'project':r['project'],'bug_id':r['bug_id']} for r in index['records']],
            'primary':False, 'team_approved':False, 'selected_common_declarations':index['target_count'],
            'unsupported_common_declarations':index['capability_exclusion_count']},
        prepare_status='Post-repair development inputs; capability subset only, not final shared acceptance.')
    for name in ('beam_fixture_review', 'beam_handoff_review', 'fixture_development_proposal'):
        protocol.pop(name, None)
    protocol['received_development_evidence'] = [
        {'path':path, 'sha256':sha256(ROOT / path), 'team_or_primary_approval':False}
        for path in ('docs/api854/evidence/beam-pilot-v5-repair-review-20261003/index.json',
                     'docs/api854/evidence/beam-pilot-v5-repair-handoff-20261003/index.json')]
    protocol['generation'].update(prepare_contract=POLICY_V7['contract'], fixture_policy_id=POLICY_V7['fixture_policy'],
        prompt_policy_id=POLICY_V7['prompt_policy_id'], prompt_token_reserve=None)
    protocol['processing_policy']['fixture_policy'] = POLICY_V7['fixture_policy']
    protocol['gate_a'].update(reviewed_by={'aom':False,'beam':False,'champ':False}, evidence=[],
        pending=['314 unsupported common declarations including four enum targets requiring joint decision',
                 'Condition-bound semantic/host/team acceptance; received local reviews retain their original versions',
                 'Provider limits/framing/bucket/reset/expiry and final reserve'])
    (output / 'protocol.proposal.json').write_bytes(encoded(protocol))
    (output / 'protocol.proposal.json.sha256').write_bytes((sha256(output / 'protocol.proposal.json') + '\n').encode())
    (output / 'prepare-policy.json').write_bytes(encoded(POLICY_V7))
    (output / 'checksums.json').write_bytes(encoded({p.name:sha256(p) for p in output.iterdir() if p.is_file()}))
    return {'bugs':len(index['records']), 'selected':index['target_count'],
        'unsupported':index['capability_exclusion_count'], 'max_prompt_utf8_bytes':index['max_prompt_utf8_bytes'],
        'runtime_files':len(protocol['source_sha256']), 'generation_ready':False}


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--preparation', type=Path, required=True)
    parser.add_argument('--output', type=Path, required=True)
    args = parser.parse_args()
    print(compose(args.preparation, args.output))
