"""User-directed bounded Csv development generation, separate from primary Gate A.

Preserves the exact received v12 prompt. Two sequential requests maximum, no
retry, feedback or account switch. The byte guard is an operator admission
buffer, not a claim of measured provider framing or final v12 reserve.
"""
import argparse
from datetime import datetime, timedelta, timezone
from pathlib import Path
import json

from .common import ROOT, read_json, write_json, sha256
from .kku_client import KKUClient, Model, load_account, KKUError, utc_now
from .review_v10_readiness import BatchedObjects
from .review_joint_recipe_intake import require, digest

AOM='63ad195623c2ed3f67f3ae232c00c54d3160ce72'
PREP='output/api854-20261003/prepare-v12-graphics-development-v1/Csv-1'
PAIR='output/api854-20261003/aom-continuation-v12-development-v1'
CALIBRATION=ROOT/'output/api854-20261004/champ-provider-calibration-v1'
CONDITION='api854-20261004-csv-six-target-native-development-v1'
MODELS={'kku-claude':('claude-sonnet-5','Claude'),'kku-gemini':('gemini-3.5-flash-lite','Gemini')}


def run(output):
    output=Path(output).resolve();require(output.is_relative_to(ROOT/'output') and not output.exists(),'New contained output required')
    aom=BatchedObjects(AOM); prompt=aom.blob(PREP+'/prompt.md')
    targets=aom.document(PREP+'/targets.json');require(len(targets['targets'])==6 and all(t['class']=='org.apache.commons.csv.ExtendedBufferedReader' for t in targets['targets']),'Csv scope differs')
    runtime=aom.document(PAIR+'/protocol.proposal.json')['source_sha256']
    reserve=len(prompt)+4096+4096
    observations={}
    for name,provider in MODELS.values():
        obs=read_json(CALIBRATION/(name+'.json'));require(obs['status']=='observed','Missing actual quota observation')
        ended=datetime.fromisoformat(obs['evidence']['ended_at_utc'])
        require(datetime.now(timezone.utc)<ended+timedelta(minutes=15),'Quota observation older than conservative validity window')
        remaining=obs['metadata']['model_quota']['daily_remaining_tokens']
        require(type(remaining) is int and remaining>=reserve,'Insufficient observed quota for byte-buffer admission')
        observations[name]={'remaining_observed':remaining,'observation_at_utc':ended.isoformat(),
                            'evidence_path':str((CALIBRATION/(name+'.json')).relative_to(ROOT)),'evidence_sha256':sha256(CALIBRATION/(name+'.json'))}
    output.mkdir(parents=True,exist_ok=False)
    received=output/'received';received.mkdir()
    names=('prompt.md','targets.json','fixture-recipes.json','prepare-metadata.json','context-manifest.json','prepare-policy.json')
    for name in names:(received/name).write_bytes(aom.blob(PREP+'/'+name))
    (output/'producer.py').write_bytes(Path(__file__).read_bytes())
    plan={'schema_version':1,'condition':CONDITION,'purpose':'User-authorized real four-approach ready-subset work with 24-hour deadline; first bounded native Csv experiment',
        'user_priority':'Collect actual results for all four approaches on ready bugs, then expand within time remaining',
        'primary_gate_a_passed':False,'full_defects4j_evaluation':False,'three_owner_final_condition_approval':False,
        'standalone_native_development_only':True,'aom_commit':AOM,'project':'Csv','bug_id':1,'targets':targets['targets'],
        'source_v12_runtime_sha256':runtime,'received_inputs_sha256':{n:sha256(received/n) for n in names},
        'account_alias':'a01','models':{k:v[0] for k,v in MODELS.items()},'temperature':0,'max_tokens':4096,'stream':False,
        'request_cap':2,'total_completion_token_cap':8192,'prompt_utf8_bytes':len(prompt),'prompt_sha256':digest(prompt),
        'admission_prompt_byte_bound':len(prompt),'operator_framing_buffer':4096,'reservation_guard':reserve,
        'operator_buffer_is_measured_framing':False,'final_v12_reserve':None,'quota_observations':observations,
        'observation_validity_minutes':15,'credential_expiry_or_reset_claimed':False,'automatic_retry':False,'feedback':False,'account_switch':False,
        'queue_mutations':0,'shared_quota_ledger_mutations':0,'primary_results_allowed':False,'producer_sha256':sha256(output/'producer.py'),'started_at_utc':utc_now()}
    write_json(output/'preexecution-plan.json',plan)
    client=KKUClient(load_account('a01',ROOT/'.local/api854/accounts.json'),timeout=180)
    rows=[]
    for approach,(name,provider) in MODELS.items():
        path=output/approach;path.mkdir()
        request={'model':name,'messages':[{'role':'user','content':prompt.decode('utf-8')}],'max_tokens':4096,'temperature':0,'stream':False}
        wire=json.dumps(request,ensure_ascii=False,allow_nan=False).encode()
        write_json(path/'request-intent.json',{'declared_before_request':True,'condition':CONDITION,'approach':approach,'account_alias':'a01',
            'model':name,'max_tokens':4096,'temperature':0,'prompt_sha256':digest(prompt),'serialized_request_sha256':digest(wire),
            'serialized_request_utf8_bytes':len(wire),'guard':reserve,'source_quota_observation':observations[name],'started_at_utc':utc_now()})
        try:
            result=client.complete(Model(name,name,provider),prompt.decode('utf-8'),max_tokens=4096,temperature=0)
            (path/'raw-response.txt').write_text(result.content,encoding='utf-8',newline='\n')
            write_json(path/'response.json',{'metadata':result.metadata,'evidence':result.evidence,'outcome':result.outcome})
            usage=result.metadata['usage'];quota=result.metadata['model_quota']
            in_guard=type(usage['prompt_tokens']) is int and usage['prompt_tokens']<=len(prompt)+4096
            trustworthy=all(type(usage[k]) is int for k in ('prompt_tokens','completion_tokens','total_tokens')) and type(quota['daily_remaining_tokens']) is int
            row={'approach':approach,'requested_model_id':name,'outcome':result.outcome,'usage':usage,'model_quota':quota,
                 'input_within_provisional_guard':in_guard,'trustworthy_usage_quota':trustworthy,'suite_semantic_validity':'pending_source_review',
                 'raw_response_sha256':sha256(path/'raw-response.txt'),'primary_result':False}
            write_json(path/'generation-receipt.json',row);rows.append(row)
            if not in_guard or not trustworthy:break
        except KKUError as error:
            row={'approach':approach,'outcome':'provider_error','error':error.record(),'primary_result':False}
            write_json(path/'generation-receipt.json',row);rows.append(row);break
    result={'condition':CONDITION,'status':'generation_observations_collected_evaluation_pending','requests_attempted':len(rows),'records':rows,
            'native_evaluation_complete':False,'primary_results_added':0,'queue_mutations':0,'shared_quota_ledger_mutations':0,
            'primary_gate_a_passed':False,'final_v12_reserve':None,'finished_at_utc':utc_now()}
    write_json(output/'receipt.json',result)
    write_json(output/'checksums.json',{p.relative_to(output).as_posix():sha256(p) for p in sorted(output.rglob('*')) if p.is_file()})
    return result


if __name__=='__main__':
    p=argparse.ArgumentParser(description=__doc__);p.add_argument('--output',type=Path,required=True);a=p.parse_args()
    result=run(a.output)
    print(json.dumps({'status':result['status'],'requests':result['requests_attempted'],'records':result['records']}))
