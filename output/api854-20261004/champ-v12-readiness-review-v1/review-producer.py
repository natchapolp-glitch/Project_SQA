"""Audit immutable v12 inputs; import read-only provider metadata, never generate.

Run received consumers/oracle validators on a temporary exact Aom snapshot.
The local Champ runtime, private ledger and received evidence remain unchanged.
"""
import argparse
from datetime import datetime, timezone
import json
from pathlib import Path
import re
import subprocess
import sys
import tempfile

from .common import ROOT, implementation_hashes, read_json, sha256, write_json
from .review_joint_recipe_intake import digest, execute, extract_archive, require
from .review_v10_readiness import BatchedObjects, target_key, worksheet_row
from .verify_chronology_development import checkpoint

AOM = '63ad195623c2ed3f67f3ae232c00c54d3160ce72'
BASE = '6c0f6328788f56e3ac2dc52f0e3520b820892625'
PREFIX = 'output/api854-20261003/'
PREP = PREFIX+'prepare-v12-graphics-development-v1'
OLD = PREFIX+'prepare-v11-chronology-development-v3'
PAIR = PREFIX+'aom-continuation-v12-development-v1'
READY = PREFIX+'aom-v12-readiness-v1'
INTAKE = PREFIX+'aom-graphics-v12-intake-v1'
GRAPHICS = PREFIX+'aom-graphics-v12-integration-v5'
ENV = PREFIX+'aom-graphics-v12-environment-controls-v2'
RETAINED = PREFIX+'aom-v12-preserved-runtime-v1'
CONDITION = 'api854-20261003-graphics-v12-development'
MODELS = {'kku-claude':'claude-sonnet-5', 'kku-gemini':'gemini-3.5-flash-lite'}

ADAPTER = r'''
import json, pathlib, subprocess, sys, unittest
snapshot, original, output = map(pathlib.Path, sys.argv[1:])
sys.path.insert(0,str(snapshot))
original_check_output = subprocess.check_output
def git_read(command,*args,**kwargs):
    command=list(command)
    if len(command)>2 and command[:2]==['git','-C'] and pathlib.Path(command[2])==snapshot:
        command[2]=str(original)
    return original_check_output(command,*args,**kwargs)
subprocess.check_output=git_read
from scripts.study.api854.common import read_json,write_json,implementation_hashes,sha256
from scripts.study.api854.verify_graphics_v12 import checked
from scripts.study.api854.graphics_v12 import load_intake
from scripts.study.api854.gate_a import inspect
from scripts.study.api854.verify_chronology_v11 import validate_buggy_signature
require=lambda ok,msg: None if ok else (_ for _ in ()).throw(ValueError(msg))
load_intake(); checked()
pair='output/api854-20261003/aom-continuation-v12-development-v1'
gate=inspect(protocol_path=pair+'/protocol.proposal.json',runner_path=pair+'/runner-plan.json')
required={'prepare_contract','protocol_runner_binding','preparation_index_binding','runtime_source_binding',
          'shared_policy','fixture_recipe_binding','runner_coverage','eligible_targets_20'}
require(not gate['gate_a_passed'] and all(c['status']=='pass' for c in gate['checklist'] if c['id'] in required),
        'Current binding checks failed or Gate A opened')
require(sum(c['id'] in required for c in gate['checklist'])==8,'Missing binding check')
write_json(output/'received-runtime-gate-recheck.json',gate)
suite=unittest.defaultTestLoader.loadTestsFromNames([
    'scripts.study.api854.tests.test_graphics_v12',
    'scripts.study.api854.tests.test_preparation_v12_development',
    'scripts.study.api854.tests.test_chronology_v11'])
result=unittest.TextTestRunner(verbosity=2).run(suite)
require(result.wasSuccessful() and result.testsRun==15 and not result.skipped,'Received focused tests failed')
retained=snapshot/'output/api854-20261003/aom-v12-preserved-runtime-v1'
c=read_json(retained/'component-receipt.json'); t=read_json(retained/'chronology/receipt.json')
require(c['status']=='pass' and c['fixed_source_integration_cases']==64 and c['temporary_setter_mutation_detected']
        and c['legacy_policy_behavior_preserved'] and c['runtime_helper_sha256']==implementation_hashes()['algorithms/java/SqaProbe.java'],
        'Retained component/runtime binding differs')
require(t['status']=='pass' and t['cases']==13 and t['runtime_source_sha256']==implementation_hashes(),
        'Retained Chronology/runtime binding differs')
for repeat in ('first','second'):validate_buggy_signature(t['stages']['buggy_'+repeat]['cases'])
env=snapshot/'output/api854-20261003/aom-graphics-v12-environment-controls-v2'
e=read_json(env/'receipt.json')
require(e['status']=='pass' and set(e['controls'])=={'binding_assertion','target_linkage'},'Missing actual environment controls')
require(e['shared_integration_sha256']==sha256(snapshot/'output/api854-20261003/aom-graphics-v12-integration-v5/receipt.json'),
        'Environment control bound to another integration')
for name,record in e['controls'].items():
    path=env/(name+'-cause-chain.stdout.log');raw=path.read_bytes()
    require(record['status']=='fixture_error' and not record['ordinary_target_observation']
            and record['cause_chain_proof']['exit_code']==0 and b'CONTROL_VALID=true' in raw
            and record['cause_chain_proof']['stdout_sha256']==sha256(path),'Actual fatal/setup cause chain differs')
write_json(output/'received-scoped-recheck.json',{'tests':result.testsRun,'skipped':len(result.skipped),
    'four_consumer_combinations':80,'graphics_raw_observations_revalidated':96,'graphics_exact_declarations':7,
    'graphics_entry_records_revalidated':48,'chart_old_new_pairs_revalidated':48,'junit_graphics_cases_per_stage':24,
    'junit_stages_revalidated':4,'graphics_candidate_fault_detected':False,'retained_component_receipt_cases':64,
    'retained_chronology_receipt_cases':13,'environment_controls_revalidated':2,'fresh_java_executions':0,
    'scope':'Fresh Python consumer/analytic/raw-evidence checks; received Java execution evidence, no native host acceptance'})
print(json.dumps({'status':'pass','tests':result.testsRun,'consumers':80,'gate_a_passed':False}))
'''


def tree_files(commit, prefixes):
    raw=subprocess.check_output(['git','ls-tree','-r','--name-only','-z',commit,'--',*prefixes],cwd=ROOT)
    return {p.decode('utf-8') for p in raw.split(b'\0') if p}


def audit_partition(aom, old, index, intake):
    wanted={target_key(t) for t in intake['targets']}
    require(len(wanted)==7,'Graphics exact seven differ')
    records=[]; additions=[]
    for row in index['records']:
        name=f"{row['project']}-{row['bug_id']}"; before=OLD+'/'+name; after=PREP+'/'+name
        prior=old.document(before+'/targets.json')['targets']; current=aom.document(after+'/targets.json')['targets']
        pkeys={target_key(t) for t in prior}; ckeys={target_key(t) for t in current}
        expected=wanted if name=='Chart-1' else set()
        require(len(ckeys)==len(current)==row['target_count'] and ckeys-pkeys==expected and pkeys<=ckeys,
                'Selected partition differs: '+name)
        exclusions=aom.document(after+'/capability-exclusions.json')['excluded']
        old_ex=old.document(before+'/capability-exclusions.json')['excluded']
        require(exclusions==[r for r in old_ex if target_key(r['target']) not in expected]
                and len(exclusions)==row['capability_exclusion_count'],'Exclusions/reasons differ: '+name)
        ekeys={target_key(r['target']) for r in exclusions}
        require(not ckeys & ekeys and len(ekeys)==len(exclusions)
                and len(current)+len(exclusions)==len(prior)+len(old_ex),'Denominator differs: '+name)
        cm=aom.document(after+'/context-manifest.json'); om=old.document(before+'/context-manifest.json')
        require(cm['source_files']==om['source_files'] and cm['source_hash']==om['source_hash'],
                'Fixed knowledge selection differs: '+name)
        paths=tree_files(BASE,[before+'/fixed-source'])
        missing=paths-old.cache.keys()
        if missing:old.preload(missing)
        received=[after+p[len(before):] for p in paths]
        missing=set(received)-aom.cache.keys()
        if missing:aom.preload(missing)
        for path in paths:require(old.blob(path)==aom.blob(after+path[len(before):]),'Retained fixed bytes differ')
        delta=[t for t in current if target_key(t) in expected]; additions.extend(delta)
        records.append({'project':row['project'],'bug_id':row['bug_id'],'owner':row['owner'],
                        'selected':len(current),'exclusions':len(exclusions),'accepted_graphics_additions':delta,
                        'fixed_context_source_hash':cm['source_hash'],'full_semantic_acceptance':False})
    require(len(records)==20 and sum(r['selected'] for r in records)==403 and sum(r['exclusions'] for r in records)==288
            and index['required_common_declarations']==691 and len(additions)==7,'Aggregate partition differs')
    require({target_key(t) for t in index['accepted_additions']}==wanted,'Indexed Graphics additions differ')
    return records


def provider_evidence(private, output):
    """Import existing responses without sending requests or exposing private fields."""
    from .kku_client import sanitize
    doc=ROOT/'.local/api854/provider-doc-v12.html'; raw=doc.read_bytes()
    text=raw.decode('utf-8')
    review={'source_url':'https://gen.ai.kku.ac.th/docs/api','raw_document_sha256':digest(raw),
            'document_bytes':len(raw),'generic_temperature_and_max_tokens_documented':all(n in text for n in ('temperature','max_tokens')),
            'generation_response_quota_fields_documented':'daily_remaining_tokens' in text,
            'daily_reset_documented':'โควต้าจะรีเซ็ตทุกวัน' in text,
            'count_token_or_quota_metadata_endpoint_found':False,'exact_reset_timezone_or_expiry_documented':False,
            'scope':'Published API lists models/models-list/completions/messages; no token-count/expiry/context limits endpoint documented'}
    write_json(output/'public-provider-document-review.json',review)
    paths=sorted(private.glob('*.json')); require(len(paths)==11,'Require the eleven current metadata observations')
    rows=[]
    for path in paths:
        record=read_json(path); require(record['status']=='pass','Metadata request unsuccessful')
        require((record['method'],record['path']) in {('GET','/models'),('POST','/chat/models-list')},'Only read-only model metadata accepted')
        e=record['evidence']; body=e['body']; data=body['data'] if isinstance(body,dict) else body
        require(e['http_status']==200 and isinstance(data,list),'Model evidence malformed')
        require(set(MODELS.values())<=set(record['model_ids']),'Requested model currently unavailable')
        fields={'id','object','display_name','created','owned_by','name'}
        safe={'alias':record['alias'],'method':record['method'],'path':record['path'],'model_ids':record['model_ids'],
              'http_status':e['http_status'],'started_at_utc':e['started_at_utc'],'ended_at_utc':e['ended_at_utc'],
              'raw_body_sha256':e['raw_body_sha256'],'headers':e['headers'],
              'model_rows':[{k:v for k,v in r.items() if k in fields} for r in data],
              'response_quota_fields_present':bool(isinstance(body,dict) and body.get('model_quota')),
              'source_private_response_retained':True,'keys_or_key_hashes_retained':False}
        require(safe==sanitize(safe),'Sensitive provider metadata')
        write_json(output/'provider-metadata'/path.name,safe); rows.append(safe)
    result={'checked_at_utc':datetime.now(timezone.utc).isoformat(),'origin':'https://gen.ai.kku.ac.th/api/v1',
        'authenticated_readonly_metadata_requests':11,'generation_requests':0,
        'current_model_ids_verified':True,'requested_models':MODELS,'aliases_authenticated_for_model_metadata':[f'a{i:02d}' for i in range(1,11)],
        'assigned_generation_account':'a01','other_accounts_assigned_for_generation':False,
        'effective_temperature_output_cap_verified':False,'context_output_limits_verified':False,
        'provider_input_tokens':None,'provider_framing_tokens':None,'current_quota_remaining_tokens':None,
        'quota_bucket':None,'quota_reset_at':None,'credential_expiry_at':None,'final_token_reserve':None,
        'quota_ledger_modified':False,'evidence_expiry_policy':'Recheck availability/quota immediately before any authorized calibration or generation',
        'reason':'Model endpoints expose model identities only. Public documentation has no token-count/quota/reset-time/expiry metadata endpoint; generation usage is not measured during this closed-condition review.',
        'observations':[{k:r[k] for k in ('alias','method','path','http_status','started_at_utc','ended_at_utc','raw_body_sha256','response_quota_fields_present')} for r in rows]}
    write_json(output/'current-provider-review.json',result)
    return result


def run(output, private):
    output=Path(output).resolve(); require(output.is_relative_to(ROOT/'output') and not output.exists(),'Require new contained output path')
    pins,_=checkpoint(); local_runtime=implementation_hashes()
    aom=BatchedObjects(AOM); old=BatchedObjects(BASE)
    final=aom.document(READY+'/final-checksums.json'); aom.preload(final)
    require(len(final)==1068,'Unexpected final manifest inventory')
    actual=tree_files(AOM,[PREP,PAIR,READY,INTAKE,GRAPHICS,ENV,RETAINED])-{READY+'/final-checksums.json'}
    require(actual==set(final),'Incomplete final manifest inventory')
    for path,h in final.items():require(digest(aom.blob(path))==h,'Final manifest differs: '+path)
    index=aom.document(PREP+'/index.json'); protocol=aom.document(PAIR+'/protocol.proposal.json')
    completion=aom.document(READY+'/completion-receipt.json'); intake=aom.document(INTAKE+'/receipt.json')
    runtime=index['runtime_source_sha256']; aom.preload(runtime)
    require(len(runtime)==41 and protocol['source_sha256']==completion['runtime_source_sha256']==runtime,'Runtime map differs')
    for p,h in runtime.items():require(digest(aom.blob(p))==h,'Runtime bytes differ: '+p)
    bindings={'protocol_sha256':digest(aom.blob(PAIR+'/protocol.proposal.json')),
              'preparation_index_sha256':digest(aom.blob(PREP+'/index.json')),
              'runner_plan_sha256':digest(aom.blob(PAIR+'/runner-plan.json'))}
    require(all(completion[k]==h for k,h in bindings.items()) and protocol['runner_plan_sha256']==bindings['runner_plan_sha256'],
            'Top level condition bindings differ')
    require(protocol['condition']==CONDITION and protocol['enabled_stages']==[] and protocol['generation']['prompt_token_reserve'] is None
            and protocol['generation']['temperature']==0 and protocol['generation']['max_tokens']==4096
            and not any(protocol['gate_a']['reviewed_by'].values()) and not index['generation_ready'] and not completion['gate_a_passed'],
            'Closed condition changed')
    require(completion['selected']==403 and completion['exclusions']==288 and completion['denominator']==691
            and completion['prompt_model_pairs']==40 and completion['regression_tests']==82,'Completion counts differ')
    log=aom.blob(READY+'/regression-tests.log')
    require(digest(log)==completion['regression_log_sha256'] and re.search(rb'Ran 82 tests in .*?\r?\n\s*\r?\nOK\s*$',log)
            and b'... skipped ' not in log,'Received regression evidence differs')
    counts={'final_manifest':len(final)}
    for base in (INTAKE,PAIR,GRAPHICS,ENV,RETAINED):counts[base]=aom.checksums(base)
    for row in index['records']:counts[PREP+f"/{row['project']}-{row['bug_id']}"]=aom.checksums(PREP+f"/{row['project']}-{row['bug_id']}")
    records=audit_partition(aom,old,index,intake)
    received=aom.document(READY+'/prompt-reserve-worksheet.json')
    require(received['condition']==CONDITION and received['runtime_source_sha256']==runtime and all(received[k]==h for k,h in bindings.items()),'Worksheet pins differ')
    rows=[]
    for r in index['records']:
        for approach,model in MODELS.items():
            require(protocol['models'][approach]['id']==model,'Requested model changed')
            prompt=aom.blob(PREP+f"/{r['project']}-{r['bug_id']}/prompt.md")
            row=worksheet_row(r,prompt,approach,model);row['prompt_path']=PREP+f"/{r['project']}-{r['bug_id']}/prompt.md";rows.append(row)
    key=lambda r:(r['project'],r['bug_id'],r['approach'])
    expected={key(r):r for r in rows}
    require(len(expected)==len(received['records'])==received['prompt_model_pairs']==40 and len({key(r) for r in received['records']})==40,'Duplicate/missing pair')
    for r in received['records']:
        e=expected[key(r)]
        require(all(r[k]==e[k] for k in ('owner','prompt_path','prompt_sha256','prompt_utf8_bytes','requested_model_id'))
                and r['temperature']==0 and r['output_cap']==4096
                and r['historical_style_numerical_guard_floor_plus_unknown_framing']==e['historical_style_byte_guard_before_unknown_framing']
                and all(r[k] is None for k in ('provider_input_tokens','framing_overhead','final_reserve')),'Received worksheet row differs')
    largest=max(rows,key=lambda r:r['prompt_utf8_bytes']); require(largest['prompt_utf8_bytes']==index['max_prompt_utf8_bytes']==304787,'Max prompt differs')
    # Preserve all previously committed v11 output bytes, not only the current baseline preparation.
    historical=tree_files(BASE,[PREFIX]); current=tree_files(AOM,[PREFIX]);require(historical<=current,'Historical evidence removed')
    old_tree=subprocess.check_output(['git','ls-tree','-r','-z',BASE,'--',PREFIX],cwd=ROOT).split(b'\0')
    new_tree=subprocess.check_output(['git','ls-tree','-r','-z',AOM,'--',PREFIX],cwd=ROOT).split(b'\0')
    parse=lambda entries:{e.split(b'\t',1)[1]:e.split(b'\t',1)[0].split()[2] for e in entries if e}
    ot,nt=parse(old_tree),parse(new_tree)
    require(all(nt.get(p)==h for p,h in ot.items()),'Historical Git bytes changed')
    require(len(ot)==completion['historical_committed_output_files_unchanged']==9390,'Historical inventory differs')
    output.mkdir(parents=True,exist_ok=False)
    for p in (PREP+'/index.json',PAIR+'/protocol.proposal.json',PAIR+'/runner-plan.json',READY+'/completion-receipt.json',
              READY+'/prompt-reserve-worksheet.json',READY+'/final-checksums.json',INTAKE+'/receipt.json',GRAPHICS+'/receipt.json',
              GRAPHICS+'/preexecution-seal.json',ENV+'/receipt.json',RETAINED+'/receipt.json',RETAINED+'/component-receipt.json',
              RETAINED+'/chronology/receipt.json','docs/api854/AOM_GRAPHICS_V12_HANDOFF_TH.md','docs/api854/AOM_PILOT_GATE_DECISION_TH.md'):
        dest=output/'received'/p;dest.parent.mkdir(parents=True,exist_ok=True);dest.write_bytes(aom.blob(p))
    (output/'review-producer.py').write_bytes(Path(__file__).read_bytes())
    (output/'received-runtime-adapter.py').write_text(ADAPTER,encoding='utf-8',newline='\n')
    write_json(output/'partition-and-source-review.json',records)
    provider=provider_evidence(private,output)
    for r in rows:r['current_model_id_verified']=True
    write_json(output/'prompt-model-worksheet.json',{'condition':CONDITION,**bindings,'runtime_source_sha256':runtime,
        'pairs':40,'records':rows,'largest_prompt':largest,'max_prompt_utf8_bytes':304787,'conditional_byte_guard_floor':308883,
        'conditional_formula':'308883 + H (unknown)','byte_guard_is_provider_token_reserve':False,'final_token_reserve':None,
        'reserve_formula_when_evidence_exists':'max across forty requests(provider_input_tokens including counted framing + uncounted framing_tokens) + 4096',
        'framing_double_counting_forbidden':True,'worker_compatibility':'Current worker compares UTF-8 bytes to prompt_token_reserve; a lower measured-token bound needs a separately reviewed byte/token admission implementation.'})
    with tempfile.TemporaryDirectory(prefix='.champ-v12-review-',dir=ROOT/'output') as folder:
        snapshot=Path(folder).resolve();require(snapshot.is_relative_to(ROOT/'output'),'Unsafe snapshot cleanup path')
        paths=['scripts/study','algorithms','experiments/configs/api854-20261003',PREP,PAIR,READY,INTAKE,GRAPHICS,ENV,RETAINED,
               PREFIX+'prepare-v3',PREFIX+'prepare-v7-twenty-bug-development',PREFIX+'prepare-v8-fraction-field-development']
        archive=extract_archive(AOM,paths,snapshot)
        for p,h in runtime.items():require(sha256(snapshot/p)==h,'Extracted runtime differs')
        write_json(output/'preexecution-seal.json',{'declared_before_execution':True,'aom_commit':AOM,'base_v11_commit':BASE,
            'snapshot_archive_sha256':archive,'runtime_source_sha256':runtime,'input_pins':bindings,'manifest_checks':counts,
            'adapter_sha256':sha256(output/'received-runtime-adapter.py'),'producer_sha256':sha256(output/'review-producer.py'),
            'received_files_sha256':{p.relative_to(output).as_posix():sha256(p) for p in (output/'received').rglob('*') if p.is_file()},
            'champ_v9_checkpoint_pins':pins})
        execute([sys.executable,'-B','-X','utf8',output/'received-runtime-adapter.py',snapshot,ROOT,output],snapshot,output,'received-runtime-recheck')
    proof=read_json(output/'received-scoped-recheck.json')
    require(implementation_hashes()==local_runtime and checkpoint()[0]==pins,'Champ runtime/checkpoint changed')
    write_json(output/'source-bindings.json',{'aom':{'commit':AOM,'sha256':aom.bindings},'v11':{'commit':BASE,'sha256':old.bindings}})
    result={'schema_version':1,'status':'v12_scoped_inputs_accepted_provider_measurements_pending','checked_at_utc':datetime.now(timezone.utc).isoformat(),
        'condition':CONDITION,'aom_commit':AOM,'base_v11_commit':BASE,**bindings,'runtime_files':41,'runtime_source_sha256':runtime,
        'selected':403,'exclusions':288,'denominator':691,'bugs':20,'graphics_additions':7,'full_semantic_acceptance':False,
        'historical_output_files_unchanged':len(ot),'checksum_entry_checks':counts,'received_regression_tests':82,'focused_tests_rerun':15,
        'scoped_recheck':proof,'prompt_model_pairs':40,'max_prompt_utf8_bytes':304787,'conditional_byte_guard_floor':308883,
        'final_token_reserve':None,'current_model_ids_verified':True,'effective_settings_limits_framing_quota_expiry_verified':False,
        'authenticated_readonly_metadata_requests':provider['authenticated_readonly_metadata_requests'],'generation_requests':0,
        'new_defects4j_evaluations':0,'fresh_java_executions':0,'live_queue_mutations':0,'quota_ledger_modified':False,'primary_results_added':0,
        'champ_shared_runtime_remains_v9':True,'full_condition_host_acceptance':False,'gate_a_passed':False,'pilot_authorized':False,
        'pending':['Beam current v12 scoped semantic/consumer/CPU1 host receipt','Aom current v12 host for Aom+Champ CPU routing',
                   'Current provider effective settings/context-output limits/token counting/framing/quota bucket/reset/expiry',
                   'Final reserve on the team-frozen condition; byte/token worker admission compatibility',
                   'Three-owner decision: 691 primary gate or a separately specified bounded development experiment']}
    write_json(output/'receipt.json',result)
    write_json(output/'checksums.json',{p.relative_to(output).as_posix():sha256(p) for p in sorted(output.rglob('*')) if p.is_file()})
    return result


if __name__=='__main__':
    p=argparse.ArgumentParser(description=__doc__);p.add_argument('--output',type=Path,required=True);p.add_argument('--provider-evidence',type=Path,required=True)
    a=p.parse_args();r=run(a.output,a.provider_evidence)
    print(json.dumps({k:r[k] for k in ('status','selected','exclusions','prompt_model_pairs','focused_tests_rerun','current_model_ids_verified','final_token_reserve')}))
