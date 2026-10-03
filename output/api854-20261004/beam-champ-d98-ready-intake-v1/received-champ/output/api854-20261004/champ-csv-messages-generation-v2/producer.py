"""Prospective Csv transport condition: Sonnet Messages with thinking disabled.

Exact original prompt/targets, fresh Gemini control, no feedback/test repairs.
Admission uses prior actual same-prompt token observations plus explicit buffer;
this is a bounded development budget, not proof of final primary framing reserve.
"""
import argparse
import json
from pathlib import Path

from .common import ROOT,read_json,write_json,sha256
from .kku_client import KKUClient,Model,load_account,KKUError,utc_now,normalize_usage,normalize_quota
from .review_joint_recipe_intake import require,digest
from .start_csv_development import AOM,MODELS

CONDITION='api854-20261004-csv-messages-disabled-thinking-native-development-v2'
BASE=ROOT/'output/api854-20261004'
ORIGINAL=BASE/'champ-csv-development-generation-v1'
CALIBRATION=BASE/'champ-provider-messages-calibration-v1'


def run(output,received_attempt=None):
    output=Path(output).resolve();require(output.is_relative_to(ROOT/'output') and not output.exists(),'New contained output required')
    for packet in (ORIGINAL,CALIBRATION):
        for relative,h in read_json(packet/'checksums.json').items():require(sha256(packet/relative)==h,'Prerequisite packet changed')
    calibration=read_json(CALIBRATION/'receipt.json')
    require(calibration['actual_model']=='anthropic/claude-sonnet-5' and calibration['stop_reason']=='end_turn','Messages mapping not observed')
    calibrated_provider=read_json(CALIBRATION/'response.json')['evidence']['body']['provider']
    if received_attempt is not None:
        received_attempt=Path(received_attempt).resolve()
        require(received_attempt.is_relative_to(ROOT/'output'),'Unsafe received attempt path')
        for relative,h in read_json(received_attempt/'checksums.json').items():require(sha256(received_attempt/relative)==h,'Received attempt changed')
        require(read_json(received_attempt/'preexecution-plan.json')['condition']==CONDITION
                and not (received_attempt/'kku-gemini/request-intent.json').exists(), 'Cannot resend or reconcile an already attempted Gemini request')
    original=read_json(ORIGINAL/'preexecution-plan.json');prompt=(ORIGINAL/'received/prompt.md').read_bytes()
    require(original['aom_commit']==AOM and digest(prompt)==original['prompt_sha256'],'Original prompt pin differs')
    quota={'kku-claude':calibration['model_quota']['daily_remaining_tokens'],
           'kku-gemini':read_json(ORIGINAL/'kku-gemini/generation-receipt.json')['model_quota']['daily_remaining_tokens']}
    observed={approach:read_json(ORIGINAL/approach/'generation-receipt.json')['usage']['prompt_tokens'] for approach in MODELS}
    budgets={approach:tokens+8192+4096 for approach,tokens in observed.items()}
    require(all(type(quota[a]) is int and quota[a]>=budgets[a] for a in MODELS),'Known remaining quota insufficient for development budget')
    output.mkdir(parents=True);received=output/'received';received.mkdir()
    for path in (ORIGINAL/'received').iterdir():(received/path.name).write_bytes(path.read_bytes())
    (output/'producer.py').write_bytes(Path(__file__).read_bytes())
    plan={**original,'condition':CONDITION,'purpose':'Prospective transport/settings experiment after baseline Sonnet truncation; no test feedback',
          'producer_sha256':sha256(output/'producer.py'),'account_alias':'a01','request_cap':2,'started_at_utc':utc_now(),
          'quota_observations':None,'quota_observation_max_age_enforced':False,
          'quota_at_prior_known_responses':quota,'prior_same_prompt_input_tokens':observed,'development_reservation_budget':budgets,
          'operator_framing_buffer':8192,'operator_buffer_is_measured_framing':False,
          'budgets_are_admission_buffers_not_proven_maximums':True,'final_v12_reserve':None,
          'request_transport':{'kku-claude':'/messages','kku-gemini':'/chat/completions'},
          'requested_thinking':{'kku-claude':{'type':'disabled'},'kku-gemini':None},
          'requested_to_observed_model_mapping':{'claude-sonnet-5':'anthropic/claude-sonnet-5'},
          'observed_messages_provider':calibrated_provider,
          'reconciled_received_attempt_manifest_sha256':sha256(received_attempt/'checksums.json') if received_attempt else None,
          'transport_calibration_receipt_sha256':sha256(CALIBRATION/'receipt.json'),
          'baseline_generation_manifest_sha256':sha256(ORIGINAL/'checksums.json'),
          'automatic_retry':False,'feedback':False,'account_switch':False,'primary_results_allowed':False}
    # Supersede byte-bound fields inherited from the baseline with explicit new admission accounting.
    for key in ('admission_prompt_byte_bound','reservation_guard','observation_validity_minutes'):
        plan.pop(key,None)
    write_json(output/'preexecution-plan.json',plan)
    client=KKUClient(load_account('a01',ROOT/'.local/api854/accounts.json'),timeout=180);rows=[];new_requests=0
    for approach,(name,provider) in MODELS.items():
        folder=output/approach;folder.mkdir()
        request={'model':name,'messages':[{'role':'user','content':prompt.decode('utf-8')}],'max_tokens':4096,'temperature':0,'stream':False}
        if approach=='kku-claude':request['thinking']={'type':'disabled'}
        wire=json.dumps(request,ensure_ascii=False,allow_nan=False).encode()
        if approach=='kku-claude' and received_attempt:
            original_intent=received_attempt/approach/'request-intent.json'
            require(read_json(original_intent)['serialized_request_sha256']==digest(wire),'Received request differs')
            (folder/'request-intent.json').write_bytes(original_intent.read_bytes())
            write_json(folder/'request-not-resent.json',{'received_response_reconciled':True,'new_request_sent':False,
                'source_response_path':(received_attempt/approach/'response.json').relative_to(ROOT).as_posix(),
                'source_response_sha256':sha256(received_attempt/approach/'response.json'),
                'cause':'Guard expected chat vendor label Claude instead of previously observed Messages label Claude Platform on AWS'})
        else:
            write_json(folder/'request-intent.json',{'condition':CONDITION,'declared_before_request':True,'account_alias':'a01',
                'approach':approach,'endpoint_path':plan['request_transport'][approach],'serialized_request_sha256':digest(wire),
                'prompt_sha256':digest(prompt),'max_tokens':4096,'temperature':0,'thinking':request.get('thinking'),
                'development_budget':budgets[approach],'started_at_utc':utc_now()})
        try:
            if approach=='kku-claude':
                if received_attempt:
                    evidence=read_json(received_attempt/approach/'response.json')['evidence'];payload=evidence['body']
                else:
                    new_requests+=1;payload,evidence=client._request('POST','/messages',request)
                write_json(folder/'response.json',{'evidence':evidence})
                require(payload.get('model')=='anthropic/claude-sonnet-5' and payload.get('provider')==calibrated_provider,'Observed Messages model/provider differs')
                blocks=payload.get('content');require(isinstance(blocks,list),'Messages content not an array')
                require(all(b.get('type')=='text' and isinstance(b.get('text'),str) for b in blocks),'Nontext assistant content; cannot use as a test suite')
                content=''.join(b['text'] for b in blocks);reason=payload.get('stop_reason')
                outcome='truncated' if reason=='max_tokens' else 'response_received' if reason=='end_turn' and content.strip() else 'generation_failed'
                metadata={'requested_model_id':name,'actual_model':payload['model'],'actual_provider':payload['provider'],
                          'finish_reason':reason,'usage':normalize_usage(payload),'model_quota':normalize_quota(payload)}
            else:
                new_requests+=1
                completion=client.complete(Model(name,name,provider),prompt.decode('utf-8'),max_tokens=4096,temperature=0)
                content=completion.content;outcome=completion.outcome;metadata=completion.metadata
                write_json(folder/'response.json',{'metadata':metadata,'evidence':completion.evidence,'outcome':outcome})
            (folder/'raw-response.txt').write_text(content,encoding='utf-8',newline='\n')
            usage=metadata['usage'];remaining=metadata['model_quota']['daily_remaining_tokens']
            within=type(usage['prompt_tokens']) is int and usage['prompt_tokens']<=observed[approach]+8192
            trustworthy=all(type(usage[k]) is int for k in ('prompt_tokens','completion_tokens')) and type(remaining) is int
            row={'approach':approach,'requested_model_id':name,'actual_model':metadata['actual_model'],'outcome':outcome,
                 'usage':usage,'model_quota':metadata['model_quota'],'input_within_provisional_guard':within,
                 'trustworthy_usage_quota':trustworthy,'raw_response_sha256':sha256(folder/'raw-response.txt'),
                 'suite_semantic_validity':'pending_source_review','primary_result':False}
            write_json(folder/'generation-receipt.json',row);rows.append(row)
            if not within or not trustworthy:break
        except KKUError as error:
            row={'approach':approach,'outcome':'provider_error','error':error.record(),'primary_result':False}
            write_json(folder/'generation-receipt.json',row);rows.append(row);break
    receipt={'condition':CONDITION,'status':'prospective_transport_generations_collected','requests_attempted':len(rows),'records':rows,
             'new_requests_attempted':new_requests,'reconciled_received_responses':1 if received_attempt else 0,
             'primary_results_added':0,'primary_gate_a_passed':False,'queue_mutations':0,'finished_at_utc':utc_now()}
    write_json(output/'receipt.json',receipt)
    write_json(output/'checksums.json',{p.relative_to(output).as_posix():sha256(p) for p in sorted(output.rglob('*')) if p.is_file()})
    return receipt


if __name__=='__main__':
    parser=argparse.ArgumentParser(description=__doc__);parser.add_argument('--output',required=True,type=Path)
    parser.add_argument('--received-attempt',type=Path);args=parser.parse_args()
    try:r=run(args.output,args.received_attempt)
    except Exception as error:
        out=args.output.resolve()
        if out.is_relative_to(ROOT/'output') and out.exists() and not (out/'checksums.json').exists():
            write_json(out/'failed-attempt.json',{'status':'attempt_failed','error_type':type(error).__name__,'error':str(error),'primary_results_added':0})
            write_json(out/'checksums.json',{p.relative_to(out).as_posix():sha256(p) for p in sorted(out.rglob('*')) if p.is_file()})
        raise
    print(json.dumps({'status':r['status'],'records':r['records']}))
