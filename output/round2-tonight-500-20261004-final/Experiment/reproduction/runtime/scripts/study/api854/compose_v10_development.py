"""Bind the accepted recipe development condition; final reserve and Gate A stay pending."""
import argparse
import copy
from pathlib import Path

from .common import ROOT, read_json, sha256, contained, implementation_hashes
from .preparation import POLICY_V10, encoded, digest, validate
from .joint_recipe_v10 import INTAKE, load_intake
from .gate_a import inspect


def compose(preparation, output):
    preparation,output=Path(preparation).resolve(),Path(output).resolve()
    load_intake()
    index=read_json(preparation/'index.json')
    if (index['runtime_source_sha256']!=implementation_hashes()
            or index['policy_sha256']!=digest(encoded(POLICY_V10))
            or len(index['records'])!=20 or (index['target_count'],index['capability_exclusion_count'])!=(390,301)
            or index['generation_ready'] is not False):
        raise ValueError('Require current v10 development preparation')
    for row in index['records']:
        folder=contained(preparation,f"{row['project']}-{row['bug_id']}")
        for name,h in read_json(folder/'checksums.json').items():
            if sha256(contained(folder,name))!=h: raise ValueError('Prepared artifact changed')
        validate(read_json(folder/'context-manifest.json'),read_json(folder/'prepare-metadata.json'),
            (folder/'prompt.md').read_bytes(),(folder/'targets.json').read_bytes(),
            (folder/'prepare-policy.json').read_bytes(),require_eligible=True,
            fixture_recipe=read_json(folder/'fixture-recipes.json'))
    protocol=copy.deepcopy(read_json(INTAKE/'received/v9-protocol.json'))
    output.mkdir(parents=True,exist_ok=False)
    (output/'runner-plan.json').write_bytes((INTAKE/'received/v9-runner-plan.json').read_bytes())
    relative=preparation.relative_to(ROOT).as_posix()
    protocol.update(condition='api854-20261003-joint-recipes-v10-development',
        approval_state='development_proposal_not_frozen',state='development_proposal_pending_team_review',
        status='development_proposal_pending_team_review',enabled_stages=[],preparation_artifacts=relative,
        preparation_import_evidence={'path':relative+'/index.json','sha256':sha256(preparation/'index.json'),'generation_ready':False},
        prepare_policy_sha256=index['policy_sha256'],fixture_policy_id=POLICY_V10['fixture_policy'],
        source_sha256=implementation_hashes(),runner_plan_sha256=sha256(output/'runner-plan.json'),
        context_selection=POLICY_V10['context_policy_id'],joint_component_receipt=index['joint_receipt'],
        fixture_development_scope={'bugs':[{'project':r['project'],'bug_id':r['bug_id']} for r in index['records']],
            'primary':False,'team_approved':False,'selected_common_declarations':390,'unsupported_common_declarations':301},
        prepare_status='Joint component acceptance composed; final semantic/consumer/host/provider acceptance pending')
    protocol['generation'].update(prepare_contract=POLICY_V10['contract'],fixture_policy_id=POLICY_V10['fixture_policy'],
        context_policy_id=POLICY_V10['context_policy_id'],prompt_policy_id=POLICY_V10['prompt_policy_id'],prompt_token_reserve=None)
    protocol['processing_policy']['fixture_policy']=POLICY_V10['fixture_policy']
    protocol['gate_a'].update(reviewed_by={'aom':False,'beam':False,'champ':False},evidence=[],pending=[
        '301 unsupported common declarations including four enum and unaccepted Chronology candidates',
        'Final condition meaningful oracle/four-consumer/host review; old algorithm evidence is not relabeled',
        'Final same-condition 40 prompt/model pairs, actual settings/limits/framing/current quota/reset/expiry/reserve'])
    for name in ('recipe_intake_evidence','received_development_evidence'):
        protocol.pop(name,None)
    (output/'protocol.proposal.json').write_bytes(encoded(protocol))
    (output/'protocol.proposal.json.sha256').write_bytes((sha256(output/'protocol.proposal.json')+'\n').encode())
    (output/'prepare-policy.json').write_bytes(encoded(POLICY_V10))
    (output/'checksums.json').write_bytes(encoded({p.name:sha256(p) for p in output.iterdir() if p.is_file()}))
    return protocol


def audit(protocol_path, output):
    protocol_path,output=Path(protocol_path).resolve(),Path(output).resolve()
    protocol=read_json(protocol_path)
    runner=protocol_path.parent/'runner-plan.json'
    gate=inspect(protocol_path=protocol_path,runner_path=runner)
    required={'prepare_contract','protocol_runner_binding','preparation_index_binding','runtime_source_binding',
              'shared_policy','fixture_recipe_binding','runner_coverage','eligible_targets_20'}
    if gate['gate_a_passed'] or any(c['status']!='pass' for c in gate['checklist'] if c['id'] in required):
        raise ValueError('Input contract audit must pass with Gate A still closed')
    preparation=contained(ROOT,protocol['preparation_artifacts']);index=read_json(preparation/'index.json')
    worksheet=[]
    for row in index['records']:
        folder=preparation/f"{row['project']}-{row['bug_id']}"
        for approach in ('kku-claude','kku-gemini'):
            worksheet.append({'project':row['project'],'bug_id':row['bug_id'],'owner':row['owner'],
                'approach':approach,'requested_model_id':protocol['models'][approach]['id'],
                'prompt_path':(folder/'prompt.md').relative_to(ROOT).as_posix(),
                'prompt_sha256':sha256(folder/'prompt.md'),'prompt_utf8_bytes':(folder/'prompt.md').stat().st_size,
                'output_cap':4096,'temperature':0,
                'historical_style_numerical_guard_floor_plus_unknown_framing':row['prompt_utf8_bytes']+4096,
                'provider_input_tokens':None,'framing_overhead':None,'final_reserve':None})
    output.mkdir(parents=True,exist_ok=False)
    (output/'gate-a-input-checklist.json').write_bytes(encoded(gate))
    (output/'prompt-reserve-worksheet.json').write_bytes(encoded({'condition':protocol['condition'],
        'protocol_sha256':sha256(protocol_path),'preparation_index_sha256':sha256(preparation/'index.json'),
        'runner_plan_sha256':sha256(runner),'runtime_source_sha256':implementation_hashes(),
        'records':worksheet,'prompt_model_pairs':40,'max_prompt_utf8_bytes':index['max_prompt_utf8_bytes'],
        'bytes_are_not_provider_tokens':True,'guard_is_not_token_reserve':True,'final_reserve':None,
        'provider_limits_settings_framing_current_quota_reset_expiry':'pending','live_kku_requests':0}))
    (output/'checksums.json').write_bytes(encoded({p.name:sha256(p) for p in output.iterdir() if p.is_file()}))
    return {'selected':390,'exclusions':301,'max_prompt_utf8_bytes':index['max_prompt_utf8_bytes'],
            'prompt_model_pairs':40,'gate_a_passed':gate['gate_a_passed']}


if __name__=='__main__':
    p=argparse.ArgumentParser(description=__doc__)
    p.add_argument('--preparation',type=Path,required=True);p.add_argument('--output',type=Path,required=True)
    p.add_argument('--audit-output',type=Path,required=True);a=p.parse_args()
    compose(a.preparation,a.output)
    print(audit(a.output/'protocol.proposal.json',a.audit_output))
