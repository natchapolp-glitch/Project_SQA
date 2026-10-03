"""Bind Champ fd2e16ab to Beam's already executed exact-v10 scoped verdict.

Read-only Git/evidence audit. No repeated target execution, KKU transport,
credential access, quota ledger, queue or primary approval mutation.
"""
from pathlib import Path
from datetime import datetime,timezone
import hashlib,io,json,runpy,subprocess
BASE=Path(__file__).resolve().parent;ROOT=BASE.parents[2]
CHAMP=subprocess.check_output(['git','rev-parse','fd2e16ab'],cwd=ROOT,text=True).strip()
BEAM='0ca73ee6e30f719d23b6b6b89da22fdf953d5160'
AOM='a4880fb2fde574e77705841f62f302273be7dcd9'
PREFIX='output/api854-20261003/';PACK=PREFIX+'champ-v10-readiness-review-v2/'
OLD=ROOT/(PREFIX+'beam-v10-received-review-v1')
def digest(raw):return hashlib.sha256(raw).hexdigest()
def read(path):return json.loads(path.read_text(encoding='utf-8'))
def require(ok,message):
    if not ok:raise ValueError(message)
def write(name,data):
    path=BASE/name;path.parent.mkdir(parents=True,exist_ok=True)
    with path.open('x',encoding='utf-8',newline='\n') as f:json.dump(data,f,ensure_ascii=False,indent=2);f.write('\n')
def blobs(specs):
    specs=list(dict.fromkeys(specs));data=subprocess.check_output(['git','cat-file','--batch'],cwd=ROOT,input=('\n'.join(specs)+'\n').encode())
    stream=io.BytesIO(data);result={}
    for spec in specs:
        head=stream.readline().split();require(len(head)==3 and head[1]==b'blob','Missing Git blob '+spec)
        result[spec]=stream.read(int(head[2]));require(stream.read(1)==b'\n','Invalid Git framing')
    require(stream.read()==b'','Unexpected Git bytes');return result
def audit():
    beam=runpy.run_path(str(OLD/'finalize_review.py'))['audit']()
    sealed=read(OLD/'receipt.json')
    require({k:v for k,v in sealed.items() if k!='checked_at_utc'}==beam,'Prior Beam receipt no longer matches audit')
    for name,value in read(OLD/'checksums.json').items():
        require(digest((OLD/name).read_bytes())==value,'Prior Beam root seal differs '+name)
    index_path=PREFIX+'champ-v10-readiness-return-index-v1.json'
    index_raw=blobs([CHAMP+':'+index_path])[CHAMP+':'+index_path];index=json.loads(index_raw)
    require(index['received_aom_commit']==AOM and index['condition']==beam['condition'],'Peer uses other condition')
    names=[index_path,'docs/api854/CHAMP_V10_READINESS_REVIEW_TH.md',PACK+'checksums.json',PACK+'receipt.json',
           PACK+'prompt-model-worksheet.json',PACK+'native-fixed-runtime-recheck.json',PACK+'received-focused-tests.json',
           PREFIX+'champ-v10-readiness-focused-tests-v1.json',PREFIX+'champ-v10-readiness-focused-tests-v1.log']
    own_names=[PREFIX+'beam-v10-received-review-v1/receipt.json',PREFIX+'beam-v10-received-review-v1/checksums.json',
               PREFIX+'beam-v10-received-review-v1/host-review-v2/host-receipt.json',
               PREFIX+'beam-v10-received-review-v1/host-review-v2/fixed-runtime-verification.json',
               'docs/api854/BEAM_V10_CONSUMER_HOST_ACCEPTANCE_TH.md']
    first=blobs([CHAMP+':'+p for p in names]+[BEAM+':'+p for p in own_names])
    peer=json.loads(first[CHAMP+':'+PACK+'receipt.json'])
    manifest=json.loads(first[CHAMP+':'+PACK+'checksums.json'])
    specs=[CHAMP+':'+p for p in index['artifacts_sha256']]
    specs += [CHAMP+':'+PACK+p for p in manifest]
    for binding in peer['source_bindings'].values():specs += [binding['commit']+':'+p for p in binding['paths']]
    source=blobs(specs)
    verified=[]
    for p,value in index['artifacts_sha256'].items():
        require(digest(source[CHAMP+':'+p])==value,'Champ return-index pin differs '+p)
        verified.append({'commit':CHAMP,'path':p,'sha256':value,'scope':'peer_return_index'})
    for p,value in manifest.items():
        require(digest(source[CHAMP+':'+PACK+p])==value,'Champ sealed packet differs '+p)
        verified.append({'commit':CHAMP,'path':PACK+p,'sha256':value,'scope':'peer_packet_manifest'})
    for binding in peer['source_bindings'].values():
        for p,value in binding['paths'].items():
            require(digest(source[binding['commit']+':'+p])==value,'Peer original source binding differs '+p)
            verified.append({'commit':binding['commit'],'path':p,'sha256':value,'scope':'peer_original_source_binding'})
    for p in own_names:require(first[BEAM+':'+p]==(ROOT/p).read_bytes(),'Original Beam published proof changed')
    require(peer['condition']==beam['condition'] and peer['received_commits']['aom']==AOM,'Condition not identical')
    require((peer['reviewed_bugs'],peer['selected'],peer['exclusions'],peer['denominator'])==(20,390,301,691),
            'Partition differs')
    require(peer['runtime_source_sha256']==beam['v10_runtime_source_sha256'],'Runtime41 differs')
    aom_pins=peer['source_bindings']['aom']['paths']
    for binding in beam['input_bindings'].values():
        require(binding['commit']==AOM and aom_pins[binding['path']]==binding['sha256'],'Final input binding differs')
    require(peer['four_consumer_combinations_verified']==80 and peer['native_fixed_cases_rerun']==64
            and peer['native_fixed_observations']==128,'Peer counters differ')
    native=json.loads(first[CHAMP+':'+PACK+'native-fixed-runtime-recheck.json'])
    beam_native=read(OLD/'host-review-v2/fixed-runtime-verification.json')
    for key in ['runtime_helper_sha256','verifier_sha256','dependency_fixed_revisions','math_source_and_factory_sha256',
                'production_source_sha256','dependency_sha256','policy','observations','buffer_csv_lang_fixed_cases',
                'legacy_policy_behavior_preserved','temporary_setter_mutation_detected','fixed_source_integration_cases']:
        require(native[key]==beam_native[key],'Peer/Beam bounded runtime proof differs '+key)
    worksheet=json.loads(first[CHAMP+':'+PACK+'prompt-model-worksheet.json'])
    require(worksheet['condition']==beam['condition'] and worksheet['pairs']==len(worksheet['records'])==40,
            'Worksheet does not bind exact40 pairs')
    require(worksheet['final_token_reserve'] is None and worksheet['byte_guard_is_provider_token_reserve'] is False,
            'Byte guard cannot authorize provider reserve')
    observed=set()
    for row in worksheet['records']:
        key=(row['project'],row['bug_id'],row['approach']);require(key not in observed,'Duplicate model/prompt pair');observed.add(key)
        model={'kku-claude':'claude-sonnet-5','kku-gemini':'gemini-3.5-flash-lite'}[row['approach']]
        prompt=source[AOM+':'+row['prompt_path']]
        require(row['requested_model_id']==model and row['prompt_sha256']==digest(prompt) and
                row['prompt_utf8_bytes']==len(prompt) and row['requested_temperature']==0 and row['requested_output_cap']==4096,
                'Wrong requested model/settings/prompt')
        request={'model':model,'messages':[{'role':'user','content':prompt.decode('utf-8')}],
                 'max_tokens':4096,'stream':False,'temperature':0}
        raw=json.dumps(request,ensure_ascii=False,allow_nan=False).encode('utf-8')
        require(row['serialized_request_sha256']==digest(raw) and row['serialized_request_utf8_bytes']==len(raw),
                'Serialized request differs')
        require(row['final_token_reserve'] is None and row['live_reservation_ready'] is False and
                row['provider_prompt_tokens'] is None and row['provider_framing_tokens'] is None,
                'Current provider evidence cannot be inferred')
    require(worksheet['max_prompt_utf8_bytes']==265937 and worksheet['conditional_byte_guard_floor']==270033,
            'Worksheet guard differs')
    focused=json.loads(first[CHAMP+':'+PREFIX+'champ-v10-readiness-focused-tests-v1.json'])
    require(focused['tests']==8 and focused['skipped']==focused['errors']==focused['failures']==0 and focused['passed'] is True,
            'Champ reported tests incomplete')
    require(digest(first[CHAMP+':'+PREFIX+'champ-v10-readiness-focused-tests-v1.log'])==focused['log_sha256'],'Peer log differs')
    for key in ['current_model_ids_verified','effective_settings_verified','provider_framing_limits_verified',
                'current_quota_reset_expiry_verified','full_condition_semantic_host_accepted','gate_a_passed','pilot_authorized']:
        require(peer[key] is False,'Peer still has no final approval/provider evidence')
    require(peer['final_token_reserve'] is None and peer['authenticated_kku_requests']==peer['live_queue_mutations']==0,
            'Unexpected live activity/reserve')
    bindings=[{'commit':BEAM,'path':p,'sha256':digest(first[BEAM+':'+p])} for p in own_names]
    result={'status':'beam_exact_v10_scoped_verdict_reaffirmed_for_champ_fd2e16ab',
       'champ_commit':CHAMP,'aom_commit':AOM,'beam_execution_evidence_commit':BEAM,'condition':beam['condition'],
       'prior_beam_commit_seen_by_champ':peer['received_commits']['beam'],
       'prior_peer_pending_request_resolved_scope':'Exact v10 consumers, bounded component oracles and technical Beam host receipt are already available at0ca73ee6.',
       'beam_final_condition_verdict_bindings':bindings,'final_input_bindings':beam['input_bindings'],
       'runtime_source_sha256':beam['v10_runtime_source_sha256'],'selected':390,'exclusions':301,'denominator':691,
       'bugs':20,'consumer_combinations_verified':80,'bounded_component_cases_verified':64,
       'bounded_fixed_observations_verified':128,'existing_beam_focused_tests_passed':7,
       'new_target_executions':0,'new_test_executions':0,'peer_bounded_observations_match_beam':True,
       'worksheet_pairs_verified':40,'unique_peer_git_blobs_verified':len(source),
       'binding_entries_verified':len(verified),'existing_beam_host':'beam-pc1','cpu_slots':1,'cpu_lock_exits':[9,0],
       'semantic_scope':beam['semantic_scope'],'bounded_component_semantic_review_passed':True,
       'all_390_semantic_approval':False,'native_technical_host_receipt_verified':True,'consumer_review_passed':True,
       'provider_current_evidence_verified':False,'final_reserve':None,'gate_a_approved':False,
       'live_kku_requests':0,'live_queue_mutations':0,'quota_ledger_mutations':0,'primary_added':0,
       'chronology_graphics_enum_adopted_into_v10':False,
       'next_action':'Champ/Aom use Beam0ca73ee6 receipt for exact v10 scoped checks; full semantic/exclusions/provider reserve and team Gate A remain pending.'}
    return result,first,verified,names
if __name__=='__main__':
    result,raws,verified,names=audit()
    provenance=[]
    for p in names:
        dest=BASE/'received-champ'/p;dest.parent.mkdir(parents=True,exist_ok=True)
        with dest.open('xb') as f:f.write(raws[CHAMP+':'+p])
        provenance.append({'commit':CHAMP,'source_path':p,'sha256':digest(raws[CHAMP+':'+p]),
                           'received_path':dest.relative_to(ROOT).as_posix()})
    write('received-provenance.json',provenance);write('verified-git-binding-entries.json',verified)
    result['checked_at_utc']=datetime.now(timezone.utc).isoformat();result['producer_sha256']=digest(Path(__file__).read_bytes())
    write('receipt.json',result)
    rows={p.relative_to(BASE).as_posix():digest(p.read_bytes()) for p in sorted(BASE.rglob('*')) if p.is_file()}
    write('checksums.json',rows)
    print(json.dumps({k:result[k] for k in ['status','consumer_combinations_verified','bounded_component_cases_verified',
          'worksheet_pairs_verified','unique_peer_git_blobs_verified','all_390_semantic_approval','gate_a_approved']},indent=2))
