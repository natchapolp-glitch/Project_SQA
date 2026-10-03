"""Bind Beam's fresh consumer/host evidence to Aom's exact v10 condition."""
from pathlib import Path
from datetime import datetime,timezone
import hashlib
import io
import json
import re
import subprocess

BASE=Path(__file__).resolve().parent;ROOT=BASE.parents[2]
PEER=BASE/'received-aom';HOST=BASE/'host-review-v2'
def sha(path):return hashlib.sha256(path.read_bytes()).hexdigest()
def read(path):return json.loads(path.read_text(encoding='utf-8'))
def require(condition,message):
    if not condition:raise ValueError(message)
def write(name,data):
    with (BASE/name).open('x',encoding='utf-8',newline='\n') as stream:
        json.dump(data,stream,ensure_ascii=False,indent=2);stream.write('\n')


def audit():
    provenance=read(BASE/'received-aom-provenance.json')+read(BASE/'reference-provenance.json')
    specs=list(dict.fromkeys(item['source_commit']+':'+item['source_path'] for item in provenance))
    stream=io.BytesIO(subprocess.check_output(['git','cat-file','--batch'],cwd=ROOT,input=''.join(s+'\n' for s in specs).encode()))
    blobs={}
    for spec in specs:
        header=stream.readline().split();require(len(header)==3 and header[1]==b'blob','Missing source Git blob')
        blobs[spec]=stream.read(int(header[2]));require(stream.read(1)==b'\n','Bad Git batch')
    require(stream.read()==b'','Extra Git response')
    for item in provenance:
        require(sha(ROOT/item['received_path'])==item['sha256']==hashlib.sha256(blobs[item['source_commit']+':'+item['source_path']]).hexdigest(),
                'Received Git bytes differ')
    prefix='output/api854-20261003/'
    completion=read(PEER/(prefix+'aom-v10-readiness-v1/completion-receipt.json'))
    pins={'protocol_sha256':prefix+'aom-continuation-v10-development/protocol.proposal.json',
          'runner_plan_sha256':prefix+'aom-continuation-v10-development/runner-plan.json',
          'preparation_index_sha256':prefix+'prepare-v10-joint-development/index.json',
          'worksheet_sha256':prefix+'aom-v10-readiness-v1/prompt-reserve-worksheet.json'}
    for name,path in pins.items():
        if name in completion:require(sha(PEER/path)==completion[name],'Final input pin differs: '+name)
    index=read(PEER/pins['preparation_index_sha256'])
    require((len(index['records']),index['target_count'],index['capability_exclusion_count'])==(20,390,301),'Unexpected v10 partition')
    require(index['runtime_source_sha256']==completion['runtime_source_sha256'],'Preparation/runtime binding differs')
    for path,value in completion['runtime_source_sha256'].items():require(sha(PEER/path)==value,'Received v10 runtime differs')
    counts={'selected':0,'excluded':0}
    for row in index['records']:
        folder=PEER/(prefix+'prepare-v10-joint-development')/(str(row['project'])+'-'+str(row['bug_id']))
        metadata=read(folder/'prepare-metadata.json')
        require(metadata['fixture_policy_id']=='aom-beam-champ-joint-fixtures-v10-development','Wrong fixture policy')
        require(sha(folder/'targets.json')==metadata['targets_sha256'] and sha(folder/'fixture-recipes.json')==metadata['fixture_recipes_sha256'],
                'Bug target/recipe pin differs')
        counts['selected']+=len(read(folder/'targets.json')['targets']);counts['excluded']+=len(read(folder/'capability-exclusions.json')['excluded'])
    require(counts=={'selected':390,'excluded':301},'Actual identity counts differ')
    tests=read(BASE/'consumer-tests.command.json');require(tests['exit_code']==0,'Fresh consumer tests failed')
    for kind in ['stdout','stderr']:require(sha(BASE/('consumer-tests.'+kind+'.log'))==tests[kind+'_sha256'],'Consumer log changed')
    text=(BASE/'consumer-tests.stderr.log').read_text()
    require(re.search(r'Ran 7 tests in',text) and text.strip().endswith('OK') and 'skipped=' not in text,'Consumer test counters differ')
    host=read(HOST/'host-receipt.json');proof=read(HOST/'fixed-runtime-verification.json')
    require(host['condition']==completion['condition'] and host['protocol_sha256']==completion['protocol_sha256'] and
            host['runner_sha256']==completion['runner_plan_sha256'] and host['preparation_index_sha256']==completion['preparation_index_sha256'],
            'Host and preparation conditions differ')
    require(host['runtime_source_sha256']==completion['runtime_source_sha256'],'Host uses a different runtime')
    require(host['worker_id']=='beam-pc1' and host['cpu_slots']==1 and host['environment_ready'] is True and
            host['cross_process_cpu_lock']=={'held_slot_rejected_exit':9,'released_slot_reusable_exit':0},'Host slot/environment mismatch')
    require(host['fixed_runtime_proof_sha256']==sha(HOST/'fixed-runtime-verification.json') and proof['status']=='pass','Real fixed proof changed')
    require(proof['runtime_helper_sha256']==completion['runtime_source_sha256']['algorithms/java/SqaProbe.java'],'Real helper differs from final pin')
    require(proof['fixed_source_integration_cases']==64 and len(proof['observations'])==10 and len(proof['buffer_csv_lang_fixed_cases'])==54,'Fixed case count differs')
    for row in [*proof['observations'],*proof['buffer_csv_lang_fixed_cases']]:
        require(row['fixed_first']==row['fixed_second'] and row['fixed_first'].get('target_invoked') is True,'Fixed results differ or target not invoked')
        if 'expected' in row:require(row['fixed_first']['outcome']==row['expected'],'Independent bounded outcome differs')
    require(proof['temporary_setter_mutation_detected'] is True and proof['legacy_policy_behavior_preserved'] is True,'Sensitivity/legacy checks missing')
    checksum_entries=0
    for folder in [BASE/'host-review',HOST]:
        for name,value in read(folder/'checksums.json').items():require(sha(folder/name)==value,'Host checksum mismatch');checksum_entries+=1
    for command in (HOST/'commands').glob('*.command.json'):
        record=read(command);require(record['exit_code']==0,'Real host command failed')
        require(sha(HOST/record['retained_stdout'])==record['retained_stdout_sha256'] and
                sha(HOST/record['retained_stderr'])==record['stderr_sha256'],'Raw command output changed')
    for path,value in read(ROOT/'output/api854-20261003/beam-final-recipe-return-v1/beam-lang-verdict.json')['current_runtime_source_sha256'].items():
        require(sha(ROOT/path)==value,'Original Beam runtime changed')
    return {'status':'accepted_for_scoped_v10_consumer_component_semantic_and_technical_host_review',
        'aom_commit':read(BASE/'snapshot-provenance.json')['source_commit'],'condition':completion['condition'],
        'input_bindings':{name:{'commit':read(BASE/'snapshot-provenance.json')['source_commit'],'path':path,'sha256':sha(PEER/path)} for name,path in pins.items()},
        'v10_runtime_source_sha256':completion['runtime_source_sha256'],
        'received_git_blobs_verified':len(provenance),'host_checksum_entries_verified':checksum_entries,
        'bugs':20,'selected':390,'exclusions':301,'denominator':691,'fresh_tests_passed':7,'fresh_tests_skipped':0,
        'four_consumer_bug_approach_combinations_verified':80,'consumer_transport':'offline in-memory artifact client; real CPU loader/API prompt resolver',
        'fresh_fixed_component_cases_verified':64,'fresh_fixed_observations':128,
        'semantic_scope':'Setter/JDOM/Math 10 + Buffer/Csv 42 + Lang 12 bounded cases; not all 390 declarations/full legal domains',
        'all_390_semantic_approval':False,'consumer_bindings_review_passed':True,'beam_host_technical_review_passed':True,
        'worker_id':'beam-pc1','cpu_slots':1,'cpu_lock_rejected_held_slot_exit':9,'cpu_lock_reusable_exit':0,
        'new_defects4j_fixed_buggy_coverage_evaluations':0,'gate_a_approved':False,'primary_protocol_freeze_approved':False,
        'final_reserve':None,'live_kku_requests':0,'queue_mutations':0,'primary_results_added':0,
        'chronology_integrated_into_v10':False,'chronology_v9_diagnostic_packet':'beam-champ7de-integration-review-v1',
        'original_beam_runtime_files_unchanged':41,'producer_sha256':sha(Path(__file__)),
        'next_action':'Champ audit v10 forty prompt/model pairs and current provider/reserve evidence; Aom/owners keep semantic scope and Gate A requirements explicit.'}


if __name__=='__main__':
    result=audit();result['checked_at_utc']=datetime.now(timezone.utc).isoformat()
    write('receipt.json',result)
    print(json.dumps({k:result[k] for k in ['status','bugs','selected','exclusions','fresh_tests_passed','four_consumer_bug_approach_combinations_verified','fresh_fixed_component_cases_verified','fresh_fixed_observations']}))
