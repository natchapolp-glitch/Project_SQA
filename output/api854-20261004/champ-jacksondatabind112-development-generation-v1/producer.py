"""Preflight exact frozen v12 ready bugs, then collect two unchanged-prompt generations.

Prospective new-bug allocations; Sonnet Messages/thinking disabled, Gemini
Chat Completions. Four calls maximum including fresh quota calibration. No
feedback, automatic retry, model switch, or shared primary queue mutation.
"""
import argparse
from datetime import datetime, timedelta, timezone
import json
import os
from pathlib import Path
import subprocess
import tempfile

from .common import ROOT, write_json, sha256
from .kku_client import KKUClient, Model, load_account, KKUError, utc_now, normalize_usage, normalize_quota
from .review_v10_readiness import BatchedObjects
from .review_joint_recipe_intake import require, digest, extract_archive
from .start_csv_development import AOM, PAIR, MODELS
from .evaluate_csv_development import PROJECTS, BUG_IDS, ISOLATED_PROJECTS, sources, command, production_dependencies, prepare_build_generated_sources
from .verify_chronology_development import checkpoint

PREP='output/api854-20261003/prepare-v12-graphics-development-v1/Jsoup-1'
CONDITION=PROJECTS['Jsoup'][-1]
ACCOUNT='a03'
MESSAGES_MODEL='anthropic/claude-sonnet-5'
MESSAGES_PROVIDER='Claude Platform on AWS'
SCOPE={'Jsoup':(10,'a03'),'Gson':(5,'a04'),'Compress':(8,'a05'),'JacksonDatabind':(4,'a06')}


def messages_completion(payload):
    require(payload.get('model')==MESSAGES_MODEL and payload.get('provider')==MESSAGES_PROVIDER,
            'Observed Messages model/provider differs from the previously calibrated mapping')
    blocks=payload.get('content')
    require(isinstance(blocks,list) and all(b.get('type')=='text' and isinstance(b.get('text'),str) for b in blocks),
            'Nontext Messages content cannot be used as a test suite')
    require(payload.get('usage',{}).get('output_tokens_details',{}).get('thinking_tokens')==0,
            'Disabled thinking was not confirmed by usage evidence')
    content=''.join(b['text'] for b in blocks);reason=payload.get('stop_reason')
    outcome='truncated' if reason=='max_tokens' else 'response_received' if reason=='end_turn' and content.strip() else 'generation_failed'
    return content,outcome,{'requested_model_id':'claude-sonnet-5','actual_model':payload['model'],
        'actual_provider':payload['provider'],'finish_reason':reason,'thinking_tokens':0,
        'usage':normalize_usage(payload),'model_quota':normalize_quota(payload)}


def preflight(aom,defects4j,output,targets,runtime,jars,project='Jsoup'):
    folder=output/'native-preflight';folder.mkdir()
    repo,source_root,target_name,fixed,buggy,_=PROJECTS[project]
    bug=BUG_IDS[project]
    prep='output/api854-20261003/prepare-v12-graphics-development-v1/'+project+'-'+str(bug)
    # Confirm the supplied production dependency against the exact revision's descriptor.
    descriptor=subprocess.check_output(['git','--git-dir='+str(defects4j/('project_repos/'+repo)),
                                        'show',fixed+(':'+('gson/pom.xml' if project=='Gson' else 'pom.xml'))])
    (folder/'fixed-pom.xml').write_bytes(descriptor)
    if project=='Jsoup':
        require(b'<artifactId>commons-lang</artifactId>' in descriptor and b'<version>2.4</version>' in descriptor,
                'Exact production dependency descriptor differs')
    elif project=='JacksonDatabind':
        require(b'<version>2.9.9-SNAPSHOT</version>' in descriptor and b'<artifactId>jackson-core</artifactId>' in descriptor and len(jars)==2,
                'Exact Jackson build descriptor differs')
        build=defects4j/'framework/projects/JacksonDatabind/build_files'/fixed/'maven-build.xml'
        (folder/'benchmark-maven-build.xml').write_bytes(build.read_bytes())
    else:require(b'<artifactId>junit</artifactId>' in descriptor and b'<scope>test</scope>' in descriptor and not jars,
                 'Gson production dependency declaration differs')
    with tempfile.TemporaryDirectory(prefix='.champ-jsoup-preflight-',dir=ROOT/'output') as temporary:
        temp=Path(temporary).resolve();require(temp.is_relative_to(ROOT/'output'),'Unsafe temporary cleanup path')
        helper=temp/'helper';helper.mkdir();extract_archive(AOM,['algorithms/java/SqaProbe.java'],helper)
        require(sha256(helper/'algorithms/java/SqaProbe.java')==runtime['algorithms/java/SqaProbe.java'],'Helper pin differs')
        helperclasses=temp/'helperclasses';helperclasses.mkdir()
        _,r=command(['javac','--release','8','-d',helperclasses,helper/'algorithms/java/SqaProbe.java'],temp,folder,'compile-helper')
        require(r['exit_code']==0,'Native helper unavailable')
        fixtures=folder/'fixture-classes.txt';fixtures.write_text('\n'.join(targets['fixture_classes'])+'\n',encoding='utf-8',newline='\n')
        fields=('class','constructor_types','method','parameter_types');identity=lambda t:tuple(t[f] for f in fields)
        wanted={identity(t) for t in targets['targets']};observed={}
        for version,revision in (('fixed',fixed),('buggy',buggy)):
            dest=temp/version;dest.mkdir()
            if project in ISOLATED_PROJECTS and version=='buggy':
                from .benchmark_sources import derive
                binding=derive(defects4j,project,bug,repo,fixed,buggy,dest,source_root,folder/'isolated-bug-reference')
                archive=binding['fixed_archive_sha256']
            else:
                archive=sources(defects4j/('project_repos/'+repo),revision,dest,source_root)
                binding={'source_mode':'exact_git_revision','revision':revision,'archive_sha256':archive}
            if version=='fixed':
                for relative,h in aom.document(prep+'/prepare-metadata.json')['fixed_source_sha256'].items():
                    require(sha256(dest/relative)==h,'Prepared fixed source differs')
            generated=prepare_build_generated_sources(defects4j,project,dest)
            classes=dest/'classes';classes.mkdir()
            _,r=command(['javac','--release','7','-g','-cp',os.pathsep.join(map(str,jars)),
                         '-d',classes,*sorted((dest/source_root).rglob('*.java'))],temp,folder,'compile-'+version)
            require(r['exit_code']==0,'Native production compile failed before provider requests')
            raw,r=command(['java','-cp',os.pathsep.join(map(str,[classes,helperclasses,*jars])),
                           'SqaProbe','discover','--fixtures',fixtures,target_name],temp,folder,'discover-'+version)
            require(r['exit_code']==0,'Native exact declaration discovery failed')
            data=json.loads(next(line for line in raw.splitlines() if line.startswith(b'{"targets":')))
            selected={identity(t):t['dimensions'] for t in data['targets'] if identity(t) in wanted}
            require(set(selected)==wanted and not data['errors'],'Exact native target inventory differs')
            write_json(folder/('discovery-'+version+'.json'),data)
            observed[version]={'source_archive_sha256':archive,'source_binding':binding,'selected':selected,'build_generated_sources':generated}
        require(observed['fixed']['selected']==observed['buggy']['selected'],'Shared native dimensions differ')
        if project in {'Compress','JacksonDatabind'}:
            # Prospectively reject volatile fixed projections before any provider call.
            stable=[]
            cp=os.pathsep.join(map(str,[temp/'fixed/classes',helperclasses,*jars]))
            for i,(key,dimensions) in enumerate(sorted(observed['fixed']['selected'].items())):
                outputs=[]
                for repetition in (1,2):
                    raw,r=command(['java','-Duser.timezone=UTC','-cp',cp,'SqaProbe','observe',*key,
                        ','.join(['0']*dimensions),'aom-beam-champ-graphics-fixtures-v12-development'],temp,folder,
                        'fixed-stability-'+str(i)+'-'+str(repetition))
                    require(r['exit_code']==0,'Fixed probe stability preflight failed')
                    require(b'SQA_TRACE:{"target_invoked":true}' in raw and b'SQA_FIXTURE_FAILURE' not in raw,
                            'Fixed preflight did not actually invoke the target')
                    outputs.append(raw)
                require(outputs[0]==outputs[1],'Fixed probe projection is not repeatable before provider calls')
                stable.append({'target':dict(zip(fields,key)),'dimensions':dimensions,'observation_sha256':digest(outputs[0])})
            write_json(folder/'fixed-stability.json',{'zero_vector_two_fresh_JVM_observations_per_target':stable,
                'scope':'Bounded preflight only; all generated suites still require whole-suite fixed validation'})
        write_json(folder/'receipt.json',{'status':'native_compile_and_exact_declarations_ready','project':project,'bug_id':bug,
            'exact_targets':SCOPE[project][0],'fixed_revision':fixed,'buggy_revision':buggy,
            'source_archive_sha256':{v:r['source_archive_sha256'] for v,r in observed.items()},
            'source_bindings':{v:r['source_binding'] for v,r in observed.items()},
            'build_generated_sources':{v:r['build_generated_sources'] for v,r in observed.items()},
            'full_semantic_approval':False,'full_defects4j_evaluation':False,'provider_requests_so_far':0})


def run(output,defects4j,project='Jsoup',preflight_only=False):
    output=Path(output).resolve();defects4j=Path(defects4j).resolve()
    require(output.is_relative_to(ROOT/'output') and not output.exists(),'New contained output required')
    require(project in SCOPE,'Unsupported frozen ready bug')
    expected,account=SCOPE[project];condition=PROJECTS[project][-1]
    bug=BUG_IDS[project]
    prep='output/api854-20261003/prepare-v12-graphics-development-v1/'+project+'-'+str(bug)
    pins,excluded=checkpoint()
    aom=BatchedObjects(AOM)
    names=('prompt.md','targets.json','fixture-recipes.json','prepare-metadata.json','context-manifest.json','prepare-policy.json')
    aom.preload([prep+'/'+n for n in names]+[PAIR+'/protocol.proposal.json'])
    prompt=aom.blob(prep+'/prompt.md');targets=aom.document(prep+'/targets.json')
    require(len(targets['targets'])==expected and all(t['class']==PROJECTS[project][2] for t in targets['targets']),'Exact target scope differs')
    runtime=aom.document(PAIR+'/protocol.proposal.json')['source_sha256'];guard=len(prompt)+4096+4096
    jars=production_dependencies(defects4j,project)
    output.mkdir();received=output/'received';received.mkdir()
    for name in names:(received/name).write_bytes(aom.blob(prep+'/'+name))
    (output/'producer.py').write_bytes(Path(__file__).read_bytes())
    plan={'schema_version':1,'condition':condition,'project':project,'bug_id':bug,'aom_commit':AOM,
        'purpose':'User-directed actual four-approach ready-bug expansion within 24 hours',
        'primary_gate_a_passed':False,'full_defects4j_evaluation':False,'primary_results_allowed':False,
        'three_owner_final_condition_approval':False,'source_v12_runtime_sha256':runtime,
        'shared_checkpoint_pins':pins,'shared_checkpoint_excluded_identities':excluded,
        'received_inputs_sha256':{n:sha256(received/n) for n in names},'producer_sha256':sha256(output/'producer.py'),
        'auxiliary_producers_sha256':{n:sha256(ROOT/'scripts/study/api854'/n) for n in ('evaluate_csv_development.py','benchmark_sources.py')},
        'production_dependencies_sha256':{p.relative_to(defects4j).as_posix():sha256(p) for p in jars},
        'account_alias':account,'allocation_declared_before_requests':True,
        'account_purpose':'New '+project+'-1 allocation; no retry or account rotation after previous bug outcomes',
        'native_buggy_source_mode':'fixed_git_plus_official_isolated_bug_patch' if project in ISOLATED_PROJECTS else 'exact_git_parent_revision',
        'models':{a:m[0] for a,m in MODELS.items()},'temperature':0,'max_tokens':4096,'stream':False,
        'request_transport':{'kku-claude':'/messages','kku-gemini':'/chat/completions'},
        'requested_thinking':{'kku-claude':{'type':'disabled'},'kku-gemini':None},
        'requested_to_observed_model_mapping':{'claude-sonnet-5':MESSAGES_MODEL},'observed_messages_provider':MESSAGES_PROVIDER,
        'calibration_prompt':'Reply exactly OK.','calibration_max_tokens':16,'calibration_requests_cap':2,
        'generation_requests_cap':2,'total_requests_cap':4,'total_completion_token_cap':8224,
        'prompt_utf8_bytes':len(prompt),'prompt_sha256':digest(prompt),'reservation_guard':guard,
        'operator_framing_buffer':4096,'operator_buffer_is_measured_framing':False,'final_v12_reserve':None,
        'admission_guard_is_not_proven_token_maximum':True,'observation_validity_minutes':15,
        'automatic_retry':False,'feedback':False,'account_switch_during_attempt':False,
        'shared_accounts_quota_independence_claimed':False,'credential_reset_or_expiry_claimed':False,
        'queue_mutations':0,'shared_quota_ledger_mutations':0,'started_at_utc':utc_now()}
    write_json(output/'preexecution-plan.json',plan)
    preflight(aom,defects4j,output,targets,runtime,jars,project)
    if preflight_only:
        receipt={'status':'native_preflight_ready_no_provider_calls','project':project,'bug_id':bug,'condition':condition,
                 'requests_attempted':0,'records':[],'primary_results_added':0,'primary_gate_a_passed':False}
        write_json(output/'receipt.json',receipt)
        write_json(output/'checksums.json',{p.relative_to(output).as_posix():sha256(p) for p in sorted(output.rglob('*')) if p.is_file()})
        return receipt
    client=KKUClient(load_account(account,ROOT/'.local/api854/accounts.json'),timeout=180)
    calibration=output/'calibration';calibration.mkdir();observations={};rows=[];requests=0
    for approach,(name,provider) in MODELS.items():
        try:
            requests+=1
            if approach=='kku-claude':
                payload,evidence=client._request('POST','/messages',{'model':name,'messages':[{'role':'user','content':plan['calibration_prompt']}],
                    'max_tokens':16,'temperature':0,'stream':False,'thinking':{'type':'disabled'}})
                write_json(calibration/(approach+'-response.json'),{'evidence':evidence})
                content,outcome,metadata=messages_completion(payload)
            else:
                result=client.complete(Model(name,name,provider),plan['calibration_prompt'],max_tokens=16,temperature=0)
                content,outcome,metadata,evidence=result.content,result.outcome,result.metadata,result.evidence
            observation={'status':'observed','account_alias':account,'metadata':metadata,'evidence':evidence,'content':content,'outcome':outcome}
            write_json(calibration/(approach+'.json'),observation)
            require(outcome=='response_received','Quota calibration not usable')
            remaining=metadata['model_quota']['daily_remaining_tokens']
            require(type(remaining) is int and remaining>=guard,'Fresh quota insufficient for provisional byte-buffer admission')
            observations[approach]=observation
        except KKUError as error:
            write_json(calibration/(approach+'-error.json'),{'status':'provider_error','error':error.record()});raise
    for approach,(name,provider) in MODELS.items():
        observation=observations[approach]
        ended=datetime.fromisoformat(observation['evidence']['ended_at_utc'])
        require(datetime.now(timezone.utc)<ended+timedelta(minutes=15),'Quota observation expired before full request')
        folder=output/approach;folder.mkdir()
        request={'model':name,'messages':[{'role':'user','content':prompt.decode('utf-8')}],'max_tokens':4096,'temperature':0,'stream':False}
        if approach=='kku-claude':request['thinking']={'type':'disabled'}
        wire=json.dumps(request,ensure_ascii=False,allow_nan=False).encode()
        write_json(folder/'request-intent.json',{'declared_before_request':True,'condition':condition,'approach':approach,
            'account_alias':account,'endpoint_path':plan['request_transport'][approach],'serialized_request_intent_sha256':digest(wire),
            'serialized_request_utf8_bytes':len(wire),'prompt_sha256':digest(prompt),'max_tokens':4096,'temperature':0,
            'thinking':request.get('thinking'),'provisional_guard':guard,'quota_observation_sha256':sha256(calibration/(approach+'.json')),
            'started_at_utc':utc_now()})
        try:
            requests+=1
            if approach=='kku-claude':
                payload,evidence=client._request('POST','/messages',request)
                write_json(folder/'response.json',{'evidence':evidence})
                content,outcome,metadata=messages_completion(payload)
            else:
                result=client.complete(Model(name,name,provider),prompt.decode('utf-8'),max_tokens=4096,temperature=0)
                content,outcome,metadata=result.content,result.outcome,result.metadata
                write_json(folder/'response.json',{'metadata':metadata,'evidence':result.evidence,'outcome':outcome})
            (folder/'raw-response.txt').write_text(content,encoding='utf-8',newline='\n')
            usage=metadata['usage'];quota=metadata['model_quota']
            within=type(usage['prompt_tokens']) is int and usage['prompt_tokens']<=len(prompt)+4096
            trustworthy=all(type(usage[k]) is int for k in ('prompt_tokens','completion_tokens')) and type(quota['daily_remaining_tokens']) is int
            row={'approach':approach,'requested_model_id':name,'actual_model':metadata['actual_model'],'outcome':outcome,
                'usage':usage,'model_quota':quota,'input_within_provisional_guard':within,'trustworthy_usage_quota':trustworthy,
                'thinking_tokens':metadata.get('thinking_tokens'),'raw_response_sha256':sha256(folder/'raw-response.txt'),
                'suite_semantic_validity':'pending_source_review','primary_result':False}
            write_json(folder/'generation-receipt.json',row);rows.append(row)
            if not within or not trustworthy:break
        except KKUError as error:
            row={'approach':approach,'outcome':'provider_error','error':error.record(),'primary_result':False}
            write_json(folder/'generation-receipt.json',row);rows.append(row);break
    receipt={'condition':condition,'status':'native_ready_generations_collected_evaluation_pending','requests_attempted':requests,
        'generation_requests_attempted':len(rows),'records':rows,'primary_results_added':0,'full_defects4j_evaluations':0,
        'primary_gate_a_passed':False,'queue_mutations':0,'finished_at_utc':utc_now()}
    write_json(output/'receipt.json',receipt)
    write_json(output/'checksums.json',{p.relative_to(output).as_posix():sha256(p) for p in sorted(output.rglob('*')) if p.is_file()})
    return receipt


if __name__=='__main__':
    parser=argparse.ArgumentParser(description=__doc__);parser.add_argument('--output',type=Path,required=True)
    parser.add_argument('--defects4j',type=Path,required=True);parser.add_argument('--project',choices=list(SCOPE),default='Jsoup')
    parser.add_argument('--preflight-only',action='store_true');args=parser.parse_args()
    # Only this invocation's newly created directory may be sealed on failure.
    out=args.output.resolve();new=not out.exists()
    try:value=run(out,args.defects4j,args.project,args.preflight_only)
    except Exception as error:
        if new and out.is_relative_to(ROOT/'output') and out.exists() and not (out/'checksums.json').exists():
            write_json(out/'failed-attempt.json',{'status':'attempt_failed','error_type':type(error).__name__,
                'error':str(error),'primary_results_added':0,'finished_at_utc':utc_now()})
            write_json(out/'checksums.json',{p.relative_to(out).as_posix():sha256(p) for p in sorted(out.rglob('*')) if p.is_file()})
        raise
    print(json.dumps({'status':value['status'],'requests':value['requests_attempted'],'records':value['records']}))
