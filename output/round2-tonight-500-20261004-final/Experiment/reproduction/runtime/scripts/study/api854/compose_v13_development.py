"""Bind the new shared development condition; final owner review/reserve remain pending."""
import argparse
import json
from pathlib import Path

from .common import ROOT,read_json,sha256,implementation_hashes,contained
from .preparation import POLICY_V13,encoded,digest,validate
from .codec_v13 import BASE as V10_COMMIT, PAIR
from .joint_recipe_v10 import git_bytes
from .build_prepare_v13_development import checked_integration
from .compose_v10_development import audit


def compose(preparation,output):
    checked_integration()
    preparation,output=Path(preparation).resolve(),Path(output).resolve()
    index=read_json(preparation/'index.json')
    if (len(index['records']),index['target_count'],index['capability_exclusion_count'])!=(20,408,283):
        raise ValueError('Require exact new 20-bug preparation')
    if index['runtime_source_sha256']!=implementation_hashes() or index['policy_sha256']!=digest(encoded(POLICY_V13)):
        raise ValueError('Require current v13 policy/runtime')
    for row in index['records']:
        folder=contained(preparation,f"{row['project']}-{row['bug_id']}")
        for name,h in read_json(folder/'checksums.json').items():
            if sha256(contained(folder,name))!=h: raise ValueError('Artifact changed')
        validate(read_json(folder/'context-manifest.json'),read_json(folder/'prepare-metadata.json'),
            (folder/'prompt.md').read_bytes(),(folder/'targets.json').read_bytes(),(folder/'prepare-policy.json').read_bytes(),
            require_eligible=True,fixture_recipe=read_json(folder/'fixture-recipes.json'))
    pair=PAIR
    protocol=json.loads(git_bytes(V10_COMMIT,pair+'/protocol.proposal.json'))
    output.mkdir(parents=True,exist_ok=False)
    (output/'runner-plan.json').write_bytes(git_bytes(V10_COMMIT,pair+'/runner-plan.json'))
    relative=preparation.relative_to(ROOT).as_posix()
    protocol.update(condition='api854-20261004-codec-v13-development',enabled_stages=[],
        preparation_artifacts=relative,preparation_import_evidence={'path':relative+'/index.json','sha256':sha256(preparation/'index.json'),'generation_ready':False},
        prepare_policy_sha256=index['policy_sha256'],fixture_policy_id=POLICY_V13['fixture_policy'],source_sha256=implementation_hashes(),
        runner_plan_sha256=sha256(output/'runner-plan.json'),context_selection=POLICY_V13['context_policy_id'],
        joint_codec_receipt=index['joint_receipt'],shared_codec_integration=index['shared_integration_receipt'],
        prepare_status='Local shared Codec5 integration composed; new final-condition Beam/Champ semantic/host/provider review pending')
    protocol['fixture_development_scope'].update(selected_common_declarations=408,unsupported_common_declarations=283)
    protocol['generation'].update(prepare_contract=POLICY_V13['contract'],fixture_policy_id=POLICY_V13['fixture_policy'],
        context_policy_id=POLICY_V13['context_policy_id'],prompt_policy_id=POLICY_V13['prompt_policy_id'],prompt_token_reserve=None)
    protocol['processing_policy']['fixture_policy']=POLICY_V13['fixture_policy']
    protocol['gate_a'].update(reviewed_by={'aom':False,'beam':False,'champ':False},evidence=[],pending=[
        '283 unsupported declarations including four enum; full meaningful requirement remains pending',
        'New condition all-four-consumer semantic/host review; previous v12 approval does not transfer',
        'Same-condition 40 prompts/model pairs and actual token/settings/limits/framing/current quota/reset/expiry/reserve'])
    (output/'protocol.proposal.json').write_bytes(encoded(protocol))
    (output/'protocol.proposal.json.sha256').write_text(sha256(output/'protocol.proposal.json')+'\n')
    (output/'prepare-policy.json').write_bytes(encoded(POLICY_V13))
    (output/'checksums.json').write_bytes(encoded({p.name:sha256(p) for p in output.iterdir() if p.is_file()}))
    return protocol


if __name__=='__main__':
    p=argparse.ArgumentParser(description=__doc__);p.add_argument('--preparation',type=Path,required=True)
    p.add_argument('--output',type=Path,required=True);p.add_argument('--audit-output',type=Path,required=True);a=p.parse_args()
    compose(a.preparation,a.output)
    audit(a.output/'protocol.proposal.json',a.audit_output)
    print({'selected':408,'exclusions':283,'prompt_model_pairs':40,'gate_a_passed':False})
