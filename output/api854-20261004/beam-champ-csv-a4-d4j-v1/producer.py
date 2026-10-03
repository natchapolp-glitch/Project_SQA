"""Beam replays pinned Csv suites with real Defects4J, preserving invalid outcomes.

Run directly in a fresh process. Frozen v12 imports come from the previously
audited isolated snapshot, not the current Beam source condition. No API calls.
"""
import argparse,hashlib,json,shutil,sys,tarfile,time
from datetime import datetime,timezone
from pathlib import Path
ROOT=Path(__file__).resolve().parents[3]
sys.path.insert(0,str(Path(__file__).parent))
import ready_d4j_counts as observer
AOM='63ad195623c2ed3f67f3ae232c00c54d3160ce72'
SNAPSHOT=ROOT/'.local/api854/beam-v12-snapshot-63ad1956-v2'
sys.path[:0]=[str(SNAPSHOT),str(SNAPSHOT/'scripts/study')]
from scripts.study.api854.common import cpu_slot
from scripts.study.api854.worker import prepare_evaluation
from scripts.study import evaluate as evaluator
FIELDS=('class','constructor_types','method','parameter_types')
APPROACHES=('cmaes','fscs-art','kku-claude','kku-gemini')
PREFIX='output/api854-20261004/'
PREP='output/api854-20261003/prepare-v12-graphics-development-v1/Csv-1'
def read(path):return json.loads(Path(path).read_bytes())
def sha(path):return hashlib.sha256(Path(path).read_bytes()).hexdigest()
def require(ok,message):
    if not ok:raise ValueError(message)
def write(path,value):
    with Path(path).open('x',encoding='utf-8',newline='\n') as stream:json.dump(value,stream,ensure_ascii=False,indent=2);stream.write('\n')
def check_suite(folder):
    manifest=read(folder/'suite-manifest.json');suite=folder/'suite.tar.bz2'
    require(sha(suite)==manifest['suite_sha256'],'Suite hash changed')
    require(manifest['packaging_contract']=='beam-java-suite-v1' and 0<manifest['test_count']<=30,'Wrong packaging/count contract')
    inventory={}
    with tarfile.open(suite,'r:bz2') as archive:
        for member in archive:
            require(member.isfile() and member.name.endswith('.java') and not member.issym() and not member.islnk(),'Unexpected suite entry')
            require(member.name not in inventory and not Path(member.name).is_absolute() and '..' not in Path(member.name).parts,'Unsafe duplicate suite source')
            inventory[member.name]=hashlib.sha256(archive.extractfile(member).read()).hexdigest()
    require(inventory==manifest['source_sha256'],'Suite sources differ from sealed manifest')
    return manifest
def run(intake,output):
    intake=intake.resolve();output=output.resolve();started=time.monotonic()
    require(intake.is_relative_to(ROOT/'output') and output.is_relative_to(ROOT/'output') and not output.exists(),'New contained evaluation output required')
    intake_receipt=read(intake/'receipt.json');require(intake_receipt['status']=='received_and_sha_verified','Verified intake required')
    for name,digest in read(intake/'checksums.json').items():require(sha(intake/name)==digest,'Intake changed: '+name)
    received=intake/'received-champ'
    generation=received/(PREFIX+'champ-csv-development-generation-v1')
    native=received/(PREFIX+'champ-csv-native-measurement-v4')
    plan=read(generation/'preexecution-plan.json')
    require(plan['aom_commit']==AOM,'Different generation source condition')
    require(plan['condition']=='api854-20261004-csv-six-target-native-development-v1','Different generation condition')
    runtime=plan['source_v12_runtime_sha256']
    require(len(runtime)==41,'Expected sealed v12 runtime41')
    for name,digest in runtime.items():require(sha(SNAPSHOT/name)==digest,'Frozen evaluator/runtime changed: '+name)
    beam_before={name:sha(ROOT/name) for name in runtime}
    expected_targets=read(SNAPSHOT/PREP/'targets.json')
    require(read(generation/'received/targets.json')==expected_targets,'Generation targets differ from v12')
    require(len(expected_targets['targets'])==6,'Csv exact six-target scope required')
    frozen_inputs={n:sha(SNAPSHOT/PREP/n) for n in ['targets.json','fixture-recipes.json','prepare-metadata.json','prepare-policy.json','prompt.md','context-manifest.json']}
    require(frozen_inputs==plan['received_inputs_sha256'],'Frozen inputs/prompt binding differs')
    output.mkdir(parents=True,exist_ok=False)
    (output/'producer.py').write_bytes(Path(__file__).read_bytes());(output/'ready_d4j_counts.py').write_bytes(Path(observer.__file__).read_bytes())
    suites={};rows=[]
    for approach in APPROACHES:
        folder=output/approach;folder.mkdir()
        peer=read(native/approach/'receipt.json')
        if approach=='kku-claude':
            gen=read(generation/approach/'generation-receipt.json')
            require(gen['outcome']=='truncated' and not (generation/approach/'raw-response.txt').read_bytes(),'Old Sonnet truncated outcome differs; receive new packet separately')
            row={'project':'Csv','bug_id':1,'approach':approach,'status':'invalid_generation_truncated','test_count':None,'fault_detected':None,'coverage':None,'generation':gen,'evaluation_seconds':None,'stages':{},'suite_sha256':None,'primary':False}
            write(folder/'outcome.json',row);rows.append(row);continue
        manifest=check_suite(native/approach/'packaged-suite')
        archive=folder/'suite.tar.bz2';shutil.copyfile(native/approach/'packaged-suite/suite.tar.bz2',archive)
        (folder/'suite-manifest.json').write_bytes((native/approach/'packaged-suite/suite-manifest.json').read_bytes())
        suites[approach]={'suite_sha256':sha(archive),'manifest_sha256':sha(folder/'suite-manifest.json'),'test_count':manifest['test_count'],'source_sha256':manifest['source_sha256'],'peer_status':peer['status'],'generation_seconds':peer.get('generation_seconds')}
    sealed={'sealed_at_utc':datetime.now(timezone.utc).isoformat(),'generation_condition':plan['condition'],'execution_condition':'beam-csv-a4-d4j-counted-development-v1','source_commit':AOM,'champ_commit':intake_receipt['source_commit'],'intake_receipt_sha256':sha(intake/'receipt.json'),'intake_checksums_sha256':sha(intake/'checksums.json'),'producer_sha256':sha(output/'producer.py'),'counter_observer_sha256':sha(output/'ready_d4j_counts.py'),'frozen_runtime_source_sha256':runtime,'current_beam_runtime_before_sha256':beam_before,'frozen_inputs_sha256':frozen_inputs,'suites':suites,'worker_id':'beam-pc1','cpu_slots':1,'execution_java':11,'execution_timezone':'America/Los_Angeles','peer_native_java':17,'peer_native_timezone':'UTC','test_source_changes':False,'assertion_changes':False,'generation_requests':0,'queue_mutations':0,'primary_results_allowed':False,'pending_latest_csv_packet':True,'cli_fault_approval':'pending_joint_oracle_order_review'}
    write(output/'preexecution-seal.json',sealed)
    d4j_root=Path('/home/beam/sqa-beam/defects4j');d4j=d4j_root/'framework/bin/defects4j';worktrees=Path('/home/beam/sqa-beam/worktrees')
    trees=worktrees/'beam-csv-a4-ready-evaluator-v1';trees.mkdir(exist_ok=False)
    with cpu_slot(worktrees):
        setup_start=time.monotonic()
        paths,classes,sources=prepare_evaluation(str(d4j),{'project':'Csv','bug_id':1},trees,output/'setup',900)
        setup_seconds=time.monotonic()-setup_start
        for name,digest in sources.items():require(sha(SNAPSHOT/PREP/'fixed-source'/name)==digest,'Actual D4J source differs: '+name)
        original_command=observer.install_collector(evaluator,worktrees)
        try:
            with observer.observe_framework(d4j_root,output/'count-observer-framework'):
                for approach in APPROACHES:
                    if approach not in suites:continue
                    folder=output/approach;manifest=suites[approach];begin=time.monotonic()
                    record=evaluator.evaluate_run(evaluator.EvaluationConfig(project='Csv',bug_id=1,generator=approach,seed=101,budget=30,suite=folder/'suite.tar.bz2',buggy_worktree=paths['b'],fixed_worktree=paths['f'],output=folder/'evaluation',d4j=str(d4j),classes_file=classes,test_count=manifest['test_count'],generation_seconds=manifest['generation_seconds'],timeout_seconds=900))
                    # A rejected suite stays invalid. A second fixed run is diagnostic only.
                    if record['status']=='invalid' and record.get('fixed_validation')=='failed':
                        evaluator.clear_generated_evidence(paths['f'],('failing_tests','all_tests','sqa-stage-counts.json'))
                        diagnostic=folder/'fixed-2-diagnostic'
                        command=evaluator.run_command([str(d4j),'test','-w',str(paths['f']),'-s',str(folder/'evaluation'/f'Csv-1f-{approach}.101.tar.bz2')],output,diagnostic,900)
                        evaluator.copy_evidence(paths['f'],diagnostic,('failing_tests','all_tests','sqa-stage-counts.json'))
                        try:evaluator.inspect_test_stage(command,diagnostic)
                        except evaluator.EvidenceError as error:command['evidence_error']=str(error)
                        write(diagnostic/'diagnostic.json',{'role':'Repeat fixed only; suite remains rejected, no buggy/coverage admitted','command':command})
                    stages={}
                    for stage_name in ['fixed-1','fixed-2','buggy','coverage']:
                        path=folder/'evaluation'/stage_name/'actual-junit-counts.json'
                        if path.is_file():stages[stage_name]=read(path)
                    diagnostic=folder/'fixed-2-diagnostic/actual-junit-counts.json'
                    if diagnostic.is_file():stages['fixed-2-diagnostic']=read(diagnostic)
                    if record['status']=='complete':
                        require(set(stages)=={'fixed-1','fixed-2','buggy','coverage'},'Missing actual stage counters')
                        for stage_name,counts in stages.items():
                            require(counts.get('executed')==manifest['test_count'] and counts['skipped']==0 and counts['errors']==0,'Execution/skipped/environment counters invalid: '+approach+'/'+stage_name)
                            if approach in {'cmaes','fscs-art'}:require(counts['target_checks']==manifest['test_count'],'Generated target checks differ')
                    row={'project':'Csv','bug_id':1,'approach':approach,'status':record['status'],'fixed_validation':record.get('fixed_validation'),'test_count':manifest['test_count'],'fault_detected':record.get('fault_detected'),'coverage':{k:record.get(k) for k in ['line_covered','line_total','branch_covered','branch_total']},'generation_seconds':manifest['generation_seconds'],'evaluation_seconds':time.monotonic()-begin,'canonical_evaluator_seconds':record['duration_seconds'],'suite_sha256':manifest['suite_sha256'],'record_sha256':sha(folder/'evaluation/record.json'),'stages':stages,'primary':False}
                    write(folder/'outcome.json',row);rows.append(row)
        finally:evaluator.run_command=original_command
        for name,digest in runtime.items():require(sha(SNAPSHOT/name)==digest,'Frozen runtime changed after execution')
        require(beam_before=={name:sha(ROOT/name) for name in runtime},'Current Beam runtime changed')
    rows.sort(key=lambda r:APPROACHES.index(r['approach']))
    write(output/'results.json',rows)
    import csv
    with (output/'results.csv').open('x',encoding='utf-8',newline='') as stream:
        writer=csv.DictWriter(stream,fieldnames=['project','bug_id','approach','status','test_count','fault_detected','generation_seconds','evaluation_seconds','suite_sha256']);writer.writeheader()
        for row in rows:writer.writerow({k:row.get(k) for k in writer.fieldnames})
    write(output/'receipt.json',{'status':'old_packet_four_outcomes_recorded','champ_commit':intake_receipt['source_commit'],'generation_condition':plan['condition'],'execution_condition':sealed['execution_condition'],'source_commit':AOM,'preexecution_seal_sha256':sha(output/'preexecution-seal.json'),'results_sha256':sha(output/'results.json'),'four_outcomes':len(rows),'full_defects4j_completed':sum(r['status']=='complete' for r in rows),'invalid_outcomes':sum(r['status'] in {'invalid','invalid_generation_truncated'} for r in rows),'setup_seconds':setup_seconds,'total_wall_seconds':time.monotonic()-started,'worker_id':'beam-pc1','cpu_slots':1,'latest_csv_packet_pending':True,'framework_restored':read(output/'count-observer-framework/restoration.json'),'current_beam_runtime_unchanged':True,'generation_requests':0,'queue_mutations':0,'primary_results_added':0,'gate_a_approved':False,'all_691_complete':False,'cli_fault_approval':'pending_joint_oracle_order_review','limitations':['This is the old Champ a4a38a5e packet; do not label it the promised new all-fixed-passing Csv packet.','Native Java17 UTC and this Defects4J Java11 America/Los_Angeles measurement remain separate execution profiles.','AI target checks are null when not instrumented in the unchanged suite; JUnit counts are real XML counts.','Invalid suites are not admitted to buggy/coverage.','No full semantic/domain equivalence, Gate A or full854 completion claimed.']})
    print(json.dumps({'status':'old_packet_four_outcomes_recorded','records':[{'approach':r['approach'],'status':r['status'],'fault_detected':r['fault_detected']} for r in rows],'total_wall_seconds':time.monotonic()-started}))
if __name__=='__main__':
    parser=argparse.ArgumentParser(description=__doc__);parser.add_argument('--intake',type=Path,required=True);parser.add_argument('--output',type=Path,required=True);args=parser.parse_args()
    try:run(args.intake,args.output)
    except BaseException as error:
        if args.output.exists():write(args.output/'failed-attempt.json',{'status':'failed_attempt_retained','reason':type(error).__name__+': '+str(error)})
        raise
    finally:
        if args.output.exists():write(args.output/'checksums.json',{p.relative_to(args.output).as_posix():sha(p) for p in sorted(args.output.rglob('*')) if p.is_file()})
