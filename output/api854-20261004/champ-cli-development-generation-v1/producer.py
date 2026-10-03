"""Preflight Cli-1 then collect two unchanged-prompt development generations.

Account a02 is prospectively allocated to this new bug, never used to retry Csv.
Four requests maximum including quota calibration; no model/settings changes.
"""
import argparse
from pathlib import Path
import json
import os
import tempfile

from .common import ROOT, read_json, write_json, sha256
from .kku_client import KKUClient, Model, load_account, KKUError, utc_now
from .review_v10_readiness import BatchedObjects
from .review_joint_recipe_intake import require, digest, extract_archive
from .start_csv_development import AOM, PAIR, MODELS
from .evaluate_csv_development import PROJECTS, RUNNER, sources, command

PREP='output/api854-20261003/prepare-v12-graphics-development-v1/Cli-1'
CONDITION=PROJECTS['Cli'][-1]


def run(output,defects4j):
    output=Path(output).resolve();defects4j=Path(defects4j).resolve()
    require(output.is_relative_to(ROOT/'output') and not output.exists(),'New contained output required')
    aom=BatchedObjects(AOM);prompt=aom.blob(PREP+'/prompt.md');targets=aom.document(PREP+'/targets.json')
    require(len(targets['targets'])==16 and all(t['class']=='org.apache.commons.cli.CommandLine' for t in targets['targets']),'Cli scope differs')
    runtime=aom.document(PAIR+'/protocol.proposal.json')['source_sha256'];guard=len(prompt)+4096+4096
    output.mkdir(parents=True);received=output/'received';received.mkdir()
    names=('prompt.md','targets.json','fixture-recipes.json','prepare-metadata.json','context-manifest.json','prepare-policy.json')
    for name in names:(received/name).write_bytes(aom.blob(PREP+'/'+name))
    (output/'producer.py').write_bytes(Path(__file__).read_bytes())
    plan={'schema_version':1,'condition':CONDITION,'project':'Cli','bug_id':1,'aom_commit':AOM,
        'purpose':'User-directed ready-bug expansion after first actual four-approach Csv outcomes',
        'user_priority':'Collect actual results for all four approaches on ready bugs, then expand within time remaining',
        'primary_gate_a_passed':False,'full_defects4j_evaluation':False,'primary_results_allowed':False,
        'three_owner_final_condition_approval':False,'source_v12_runtime_sha256':runtime,
        'received_inputs_sha256':{n:sha256(received/n) for n in names},'account_alias':'a02',
        'allocation_declared_before_requests':True,'account_purpose':'New Cli bug allocation; no Csv retry or account rotation on failure',
        'models':{k:v[0] for k,v in MODELS.items()},'temperature':0,'max_tokens':4096,'stream':False,
        'calibration_prompt':'Reply exactly OK.','calibration_max_tokens':16,'calibration_requests_cap':2,
        'generation_requests_cap':2,'total_requests_cap':4,'total_completion_token_cap':8224,
        'prompt_utf8_bytes':len(prompt),'prompt_sha256':digest(prompt),'reservation_guard':guard,
        'operator_framing_buffer':4096,'operator_buffer_is_measured_framing':False,'final_v12_reserve':None,
        'automatic_retry':False,'feedback':False,'account_switch_during_attempt':False,
        'shared_accounts_quota_independence_claimed':False,'queue_mutations':0,'shared_quota_ledger_mutations':0,
        'started_at_utc':utc_now()}
    write_json(output/'preexecution-plan.json',plan)
    preflight=output/'native-preflight';preflight.mkdir()
    repo,source_root,target_name,fixed,buggy,_=PROJECTS['Cli']
    with tempfile.TemporaryDirectory(prefix='.champ-cli-preflight-',dir=ROOT/'output') as folder:
        temp=Path(folder).resolve();require(temp.is_relative_to(ROOT/'output'),'Unsafe temporary cleanup path')
        helper=temp/'helper';helper.mkdir();extract_archive(AOM,['algorithms/java/SqaProbe.java'],helper)
        require(sha256(helper/'algorithms/java/SqaProbe.java')==runtime['algorithms/java/SqaProbe.java'],'Helper pin differs')
        helperclasses=temp/'helperclasses';helperclasses.mkdir()
        _,r=command(['javac','--release','8','-d',helperclasses,helper/'algorithms/java/SqaProbe.java'],temp,preflight,'compile-helper')
        require(r['exit_code']==0,'Native helper unavailable')
        fixture=preflight/'fixture-classes.txt';fixture.write_text('\n'.join(targets['fixture_classes'])+'\n',encoding='utf-8',newline='\n')
        fields=('class','constructor_types','method','parameter_types');identity=lambda t:tuple(t[f] for f in fields)
        wanted={identity(t) for t in targets['targets']};observed={}
        for version,revision in (('fixed',fixed),('buggy',buggy)):
            dest=temp/version;dest.mkdir();archive_hash=sources(defects4j/('project_repos/'+repo),revision,dest,source_root)
            if version=='fixed':
                for relative,h in aom.document(PREP+'/prepare-metadata.json')['fixed_source_sha256'].items():
                    require(sha256(dest/relative)==h,'Prepared source differs')
            classes=dest/'classes';classes.mkdir()
            _,r=command(['javac','--release','7','-g','-d',classes,*sorted((dest/source_root).rglob('*.java'))],temp,preflight,'compile-'+version)
            require(r['exit_code']==0,'Native production compile failed')
            raw,r=command(['java','-cp',os.pathsep.join(map(str,[classes,helperclasses])),'SqaProbe','discover','--fixtures',fixture,target_name],temp,preflight,'discover-'+version)
            require(r['exit_code']==0,'Native declaration discovery failed')
            data=json.loads(next(l for l in raw.splitlines() if l.startswith(b'{"targets":')))
            selected={identity(t):t['dimensions'] for t in data['targets'] if identity(t) in wanted}
            require(set(selected)==wanted and not data['errors'],'Exact native target inventory differs')
            write_json(preflight/('discovery-'+version+'.json'),data)
            observed[version]={'source_archive_sha256':archive_hash,'selected':selected}
        require(observed['fixed']['selected']==observed['buggy']['selected'],'Shared native dimensions differ')
        write_json(preflight/'receipt.json',{'status':'native_compile_and_exact_declarations_ready','project':'Cli','bug_id':1,
            'exact_targets':16,'fixed_revision':fixed,'buggy_revision':buggy,
            'source_archive_sha256':{v:r['source_archive_sha256'] for v,r in observed.items()},
            'full_semantic_approval':False,'full_defects4j_evaluation':False,'provider_requests_so_far':0})
    client=KKUClient(load_account('a02',ROOT/'.local/api854/accounts.json'),timeout=180)
    observations={};rows=[];requests=0
    calibration=output/'calibration';calibration.mkdir()
    for name,provider in MODELS.values():
        try:
            requests+=1
            completion=client.complete(Model(name,name,provider),'Reply exactly OK.',max_tokens=16,temperature=0)
            observation={'status':'observed','account_alias':'a02','metadata':completion.metadata,'evidence':completion.evidence,
                         'content':completion.content,'completion_outcome':completion.outcome}
            write_json(calibration/(name+'.json'),observation)
            require(completion.outcome=='response_received','Calibration completion not usable')
            remaining=completion.metadata['model_quota']['daily_remaining_tokens']
            require(type(remaining) is int and remaining>=guard,'Observed quota insufficient for conservative byte-buffer admission')
            observations[name]=observation
        except KKUError as error:
            write_json(calibration/(name+'.json'),{'status':'provider_error','error':error.record()})
            raise
    for approach,(name,provider) in MODELS.items():
        folder=output/approach;folder.mkdir()
        request={'model':name,'messages':[{'role':'user','content':prompt.decode('utf-8')}],
                 'max_tokens':4096,'temperature':0,'stream':False}
        wire=json.dumps(request,ensure_ascii=False,allow_nan=False).encode()
        write_json(folder/'request-intent.json',{'declared_before_request':True,'condition':CONDITION,
            'approach':approach,'account_alias':'a02','model':name,'max_tokens':4096,'temperature':0,
            'prompt_sha256':digest(prompt),'serialized_request_sha256':digest(wire),'serialized_request_utf8_bytes':len(wire),
            'guard':guard,'quota_observation_sha256':sha256(calibration/(name+'.json')),'started_at_utc':utc_now()})
        try:
            requests+=1;completion=client.complete(Model(name,name,provider),prompt.decode('utf-8'),max_tokens=4096,temperature=0)
            (folder/'raw-response.txt').write_text(completion.content,encoding='utf-8',newline='\n')
            write_json(folder/'response.json',{'metadata':completion.metadata,'evidence':completion.evidence,'outcome':completion.outcome})
            usage=completion.metadata['usage'];quota=completion.metadata['model_quota']
            within=type(usage['prompt_tokens']) is int and usage['prompt_tokens']<=len(prompt)+4096
            trustworthy=all(type(usage[k]) is int for k in ('prompt_tokens','completion_tokens','total_tokens')) and type(quota['daily_remaining_tokens']) is int
            row={'approach':approach,'requested_model_id':name,'outcome':completion.outcome,'usage':usage,'model_quota':quota,
                 'input_within_provisional_guard':within,'trustworthy_usage_quota':trustworthy,
                 'raw_response_sha256':sha256(folder/'raw-response.txt'),'suite_semantic_validity':'pending_source_review','primary_result':False}
            write_json(folder/'generation-receipt.json',row);rows.append(row)
            if not within or not trustworthy:break
        except KKUError as error:
            row={'approach':approach,'outcome':'provider_error','error':error.record(),'primary_result':False}
            write_json(folder/'generation-receipt.json',row);rows.append(row);break
    receipt={'condition':CONDITION,'status':'native_ready_generations_collected_evaluation_pending','requests_attempted':requests,
        'generation_requests_attempted':len(rows),'records':rows,'primary_results_added':0,'full_defects4j_evaluations':0,
        'primary_gate_a_passed':False,'queue_mutations':0,'finished_at_utc':utc_now()}
    write_json(output/'receipt.json',receipt)
    write_json(output/'checksums.json',{p.relative_to(output).as_posix():sha256(p) for p in sorted(output.rglob('*')) if p.is_file()})
    return receipt


if __name__=='__main__':
    parser=argparse.ArgumentParser(description=__doc__);parser.add_argument('--output',type=Path,required=True)
    parser.add_argument('--defects4j',type=Path,required=True);args=parser.parse_args()
    try:value=run(args.output,args.defects4j)
    except Exception as error:
        out=args.output.resolve()
        if out.is_relative_to(ROOT/'output') and out.exists() and not (out/'checksums.json').exists():
            write_json(out/'failed-attempt.json',{'status':'attempt_failed','error_type':type(error).__name__,
                'error':str(error),'primary_results_added':0,'finished_at_utc':utc_now()})
            write_json(out/'checksums.json',{p.relative_to(out).as_posix():sha256(p) for p in sorted(out.rglob('*')) if p.is_file()})
        raise
    print(json.dumps({'status':value['status'],'requests':value['requests_attempted'],'records':value['records']}))
