"""Bind setter/JDOM recipe intake development inputs to current runtime; never enable stages."""
import argparse
import copy
from pathlib import Path

from .common import ROOT, read_json, sha256, implementation_hashes, contained
from .preparation import POLICY_V9, encoded, digest, validate, explicit_scope


def compose(preparation, output):
    preparation, output = Path(preparation).resolve(), Path(output).resolve()
    index = read_json(preparation / 'index.json')
    if (index['runtime_source_sha256'] != implementation_hashes()
            or index['policy_sha256'] != digest(encoded(POLICY_V9))
            or len(index['records']) != 20 or index['generation_ready'] is not False):
        raise ValueError('Require current-runtime twenty-bug v9 development inputs')
    expected = {(r['project'],r['bug_id'],r['owner']) for r in
        read_json(ROOT/'experiments/configs/api854-20261003/protocol.core-frozen.json')['pilot_bugs']}
    if ({(r['project'],r['bug_id'],r['owner']) for r in index['records']} != expected
            or {(r['project'],r['bug_id']) for r in index['records']} != explicit_scope(POLICY_V9)
            or index['target_count'] != 380 or index['capability_exclusion_count'] != 311):
        raise ValueError('Require exact pilot identities and prospective 380/311 partition')
    for row in index['records']:
        folder = contained(preparation, f"{row['project']}-{row['bug_id']}")
        for name, expected_hash in read_json(folder/'checksums.json').items():
            if sha256(contained(folder,name)) != expected_hash:
                raise ValueError('Prepared input changed: '+name)
        manifest, metadata = read_json(folder/'context-manifest.json'), read_json(folder/'prepare-metadata.json')
        if any(row.get(key) != value for key,value in metadata.items()):
            raise ValueError('Index metadata differs from prepared input')
        validate(manifest, metadata, (folder/'prompt.md').read_bytes(), (folder/'targets.json').read_bytes(),
            (folder/'prepare-policy.json').read_bytes(), require_eligible=True,
            fixture_recipe=read_json(folder/'fixture-recipes.json'))
    if index['max_prompt_utf8_bytes'] != max(r['prompt_utf8_bytes'] for r in index['records']):
        raise ValueError('Prompt maximum differs')
    relative = preparation.relative_to(ROOT).as_posix()
    protocol = copy.deepcopy(read_json(ROOT / 'output/api854-20261003/aom-continuation-v6-development/protocol.proposal.json'))
    runner = (ROOT / 'output/api854-20261003/aom-continuation-v6-development/runner-plan.json').read_bytes()
    output.mkdir(parents=True, exist_ok=False)
    (output / 'runner-plan.json').write_bytes(runner)
    protocol.update(condition='api854-20261003-twenty-bug-development-v9-integrated',
        approval_state='development_proposal_not_frozen', state='development_proposal_pending_team_review',
        status='development_proposal_pending_team_review', enabled_stages=[],
        preparation_artifacts=relative,
        preparation_import_evidence={'path':relative + '/index.json', 'sha256':sha256(preparation / 'index.json'),
            'generation_ready':False},
        prepare_policy_sha256=index['policy_sha256'], fixture_policy_id=POLICY_V9['fixture_policy'],
        source_sha256=implementation_hashes(), runner_plan_sha256=sha256(output / 'runner-plan.json'),
        fixture_development_scope={'bugs':[{'project':r['project'],'bug_id':r['bug_id']} for r in index['records']],
            'primary':False, 'team_approved':False, 'selected_common_declarations':index['target_count'],
            'unsupported_common_declarations':index['capability_exclusion_count']},
        prepare_status='Setter/JDOM and two Math getField intake development inputs; 311 still unsupported, not final shared acceptance.',
        recipe_intake_evidence=index['recipe_intake_evidence'])
    for name in ('beam_fixture_review', 'beam_handoff_review', 'fixture_development_proposal'):
        protocol.pop(name, None)
    protocol['received_development_evidence'] = [
        {'path':path, 'sha256':sha256(ROOT / path), 'team_or_primary_approval':False}
        for path in ('docs/api854/evidence/beam-pilot-v5-repair-review-20261003/index.json',
                     'docs/api854/evidence/beam-pilot-v5-repair-handoff-20261003/index.json',
                     'docs/api854/evidence/beam-v7-readiness-20261003/handoff.json',
                     'output/api854-20261003/aom-beam532baa31-readiness-audit-v1.json',
                     'output/api854-20261003/aom-math-field-intake-audit-v1.json',
                     'output/api854-20261003/aom-beam-enum-review-v1.json')]
    protocol['generation'].update(prepare_contract=POLICY_V9['contract'], fixture_policy_id=POLICY_V9['fixture_policy'],
        context_policy_id=POLICY_V9['context_policy_id'],
        prompt_policy_id=POLICY_V9['prompt_policy_id'], prompt_token_reserve=None)
    protocol['processing_policy']['fixture_policy'] = POLICY_V9['fixture_policy']
    protocol['gate_a'].update(reviewed_by={'aom':False,'beam':False,'champ':False}, evidence=[],
        pending=['311 unsupported common declarations including four enum targets requiring joint decision',
                 'Condition-bound semantic/host/team acceptance; received local reviews retain their original versions',
                 'Provider limits/framing/bucket/reset/expiry and final reserve'])
    (output / 'protocol.proposal.json').write_bytes(encoded(protocol))
    (output / 'protocol.proposal.json.sha256').write_bytes((sha256(output / 'protocol.proposal.json') + '\n').encode())
    (output / 'prepare-policy.json').write_bytes(encoded(POLICY_V9))
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
