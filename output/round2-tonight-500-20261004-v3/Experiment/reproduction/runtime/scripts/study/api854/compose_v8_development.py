"""Bind the incremental Math field composition to current runtime; keep all stages closed."""
import argparse
import copy
from pathlib import Path

from .common import ROOT, read_json, sha256, implementation_hashes
from .preparation import POLICY_V8, encoded, digest
from .build_prepare_v8_development import ACCEPTANCE, ACCEPTANCE_SHA256, receive


def compose(preparation, output):
    preparation,output = Path(preparation).resolve(),Path(output).resolve()
    receive()
    index = read_json(preparation/'index.json')
    if (index['runtime_source_sha256'] != implementation_hashes() or index['policy_sha256'] != digest(encoded(POLICY_V8))
            or len(index['records']) != 20 or index['generation_ready'] is not False
            or (index['target_count'],index['capability_exclusion_count']) != (379,312)):
        raise ValueError('Require current-runtime twenty-bug v8 development inputs')
    relative = preparation.relative_to(ROOT).as_posix()
    base = ROOT/'output/api854-20261003/aom-continuation-v7-development'
    protocol = copy.deepcopy(read_json(base/'protocol.proposal.json'))
    output.mkdir(parents=True,exist_ok=False)
    (output/'runner-plan.json').write_bytes((base/'runner-plan.json').read_bytes())
    protocol.update(condition='api854-20261003-fraction-field-development-v8',
        approval_state='development_proposal_not_frozen',state='development_proposal_pending_team_review',
        status='development_proposal_pending_team_review',enabled_stages=[],preparation_artifacts=relative,
        preparation_import_evidence={'path':relative+'/index.json','sha256':sha256(preparation/'index.json'),'generation_ready':False},
        prepare_policy_sha256=index['policy_sha256'],fixture_policy_id=POLICY_V8['fixture_policy'],
        source_sha256=implementation_hashes(),runner_plan_sha256=sha256(output/'runner-plan.json'),
        context_selection=POLICY_V8['context_policy_id'],
        accepted_candidate_receipt={'path':ACCEPTANCE,'sha256':ACCEPTANCE_SHA256,'scope':'Two Math signatures only; not final condition approval'},
        fixture_development_scope={'bugs':[{'project':r['project'],'bug_id':r['bug_id']} for r in index['records']],
            'primary':False,'team_approved':False,'selected_common_declarations':379,'unsupported_common_declarations':312},
        prepare_status='v7 subset plus two accepted Math fields and shared factory knowledge; final condition pending')
    protocol['generation'].update(prepare_contract=POLICY_V8['contract'],fixture_policy_id=POLICY_V8['fixture_policy'],
        context_policy_id=POLICY_V8['context_policy_id'],prompt_policy_id=POLICY_V8['prompt_policy_id'],prompt_token_reserve=None)
    protocol['processing_policy']['fixture_policy'] = POLICY_V8['fixture_policy']
    protocol['gate_a'].update(reviewed_by={'aom':False,'beam':False,'champ':False},evidence=[],pending=[
        '312 unsupported common declarations; Champ owner review remaining 167 is a separate subset',
        'Four enum targets and condition-bound semantic/host/team acceptance',
        'Champ must remeasure new prompts, framing, actual limits and current quota/bucket/reset/expiry'])
    (output/'protocol.proposal.json').write_bytes(encoded(protocol))
    (output/'protocol.proposal.json.sha256').write_bytes((sha256(output/'protocol.proposal.json')+'\n').encode())
    (output/'prepare-policy.json').write_bytes(encoded(POLICY_V8))
    (output/'checksums.json').write_bytes(encoded({p.name:sha256(p) for p in output.iterdir() if p.is_file()}))
    return {'bugs':20,'selected':379,'unsupported':312,'max_prompt_utf8_bytes':index['max_prompt_utf8_bytes'],'generation_ready':False}


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--preparation',type=Path,required=True)
    parser.add_argument('--output',type=Path,required=True)
    args = parser.parse_args()
    print(compose(args.preparation,args.output))
