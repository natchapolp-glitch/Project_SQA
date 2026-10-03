"""Replay only the two valid Jsoup algorithm archives; retain invalid AI outcomes."""
import argparse,csv,hashlib,json,shutil,sys,time
from datetime import datetime,timezone
from pathlib import Path
ROOT=Path(__file__).resolve().parents[3]
sys.path.insert(0,str(Path(__file__).parent))
import ready_d4j_counts as observer
import ready_d4j_reference as reference
from evaluate_champ_csv_ready import check_suite
SNAPSHOT=ROOT/'.local/api854/beam-v12-snapshot-63ad1956-v2'
sys.path[:0]=[str(SNAPSHOT),str(SNAPSHOT/'scripts/study')]
from scripts.study.api854.common import cpu_slot
from scripts.study.api854.worker import prepare_evaluation
from scripts.study import evaluate as evaluator
CONDITION='api854-20261004-jsoup-ten-target-messages-disabled-thinking-native-development-v1'
PREP='output/api854-20261003/prepare-v12-graphics-development-v1/Jsoup-1'
APPROACHES=['cmaes','fscs-art','kku-claude','kku-gemini']
def read(path):return json.loads(Path(path).read_bytes())
def sha(path):return hashlib.sha256(Path(path).read_bytes()).hexdigest()
write=reference.write;require=reference.require
def run(intake,output):
    started=time.monotonic();intake=intake.resolve();output=output.resolve()
    require(intake.is_relative_to(ROOT/'output') and output.is_relative_to(ROOT/'output') and not output.exists(),'New contained output required')
    for path,digest in read(intake/'checksums.json').items():require(sha(intake/path)==digest,'Changed received packet')
    peer=intake/'received-champ/output/api854-20261004';native=peer/'champ-jsoup-native-measurement-v2';generation=peer/'champ-jsoup-development-generation-v1'
    native_receipt=read(native/'receipt.json');native_seal=read(native/'preexecution-seal.json');plan=read(generation/'preexecution-plan.json')
    require(native_receipt['condition']==plan['condition']==CONDITION,'Wrong condition')
    require(plan['aom_commit']=='63ad195623c2ed3f67f3ae232c00c54d3160ce72','Wrong frozen source')
    runtime=plan['source_v12_runtime_sha256'];require(runtime==native_seal['runtime_source_sha256'] and len(runtime)==41,'Runtime41 bindings differ')
    for path,digest in runtime.items():require(sha(SNAPSHOT/path)==digest,'Frozen runtime changed')
    beam_before={path:sha(ROOT/path) for path in runtime}
    prepared=SNAPSHOT/PREP;targets=read(prepared/'targets.json')
    require(len(targets['targets'])==10 and targets==read(generation/'received/targets.json'),'Exact ten-target input differs')
    inputs={name:sha(prepared/name) for name in plan['received_inputs_sha256']};require(inputs==plan['received_inputs_sha256'],'Frozen inputs differ')
    output.mkdir(parents=True,exist_ok=False)
    (output/'producer.py').write_bytes(Path(__file__).read_bytes())
    (output/'ready_d4j_counts.py').write_bytes(Path(observer.__file__).read_bytes());(output/'ready_d4j_reference.py').write_bytes(Path(reference.__file__).read_bytes())
    rows=[];suites={}
    for approach in APPROACHES:
        folder=output/approach;folder.mkdir();receipt=read(native/approach/'receipt.json')
        if approach.startswith('kku-'):
            require(receipt['status']=='fixed_validation_failed_entire_suite_rejected','Expected retained native fixed rejection')
            row={'project':'Jsoup','bug_id':1,'approach':approach,'status':'invalid_native_fixed_entire_suite_rejected','full_defects4j_status':'not_replayed_by_design','test_count':receipt['declared_test_count'],'native_fixed_stage_counts':{name:data['counts'] for name,data in receipt['stages'].items()},'native_outcome_sha256':sha(native/approach/'receipt.json'),'suite_sha256':sha(native/approach/'packaged-suite/suite.tar.bz2'),'fault_detected':None,'coverage':None,'worker_seconds_including_setup':None,'primary':False}
            write(folder/'outcome.json',row);rows.append(row);continue
        require(receipt['status']=='native_fixed_twice_buggy_coverage_measured','Only valid native algorithm suites can be replayed')
        manifest=check_suite(native/approach/'packaged-suite')
        require(manifest['test_count']==30,'Algorithm suite count differs')
        shutil.copyfile(native/approach/'packaged-suite/suite.tar.bz2',folder/'suite.tar.bz2');shutil.copyfile(native/approach/'packaged-suite/suite-manifest.json',folder/'suite-manifest.json')
        suites[approach]={'suite_sha256':sha(folder/'suite.tar.bz2'),'manifest_sha256':sha(folder/'suite-manifest.json'),'test_count':30,'source_sha256':manifest['source_sha256'],'upstream_generation_seconds':receipt['generation_seconds']}
    d4j_root=Path('/home/beam/sqa-beam/defects4j');d4j=d4j_root/'framework/bin/defects4j';worktrees=Path('/home/beam/sqa-beam/worktrees')
    expected_buggy=reference.derive('Jsoup',1,'jsoup.git','src/main/java',native_seal,d4j_root,output/'isolated-bug-reference',worktrees)
    sealed={'sealed_at_utc':datetime.now(timezone.utc).isoformat(),'generation_condition':CONDITION,'execution_condition':'beam-jsoup-d4j-isolated-bug-counted-development-v1','champ_commit':read(intake/'receipt.json')['source_commit'],'aom_commit':plan['aom_commit'],'intake_manifest_sha256':sha(intake/'checksums.json'),'native_manifest_sha256':sha(native/'checksums.json'),'frozen_runtime_source_sha256':runtime,'current_beam_runtime_before_sha256':beam_before,'frozen_inputs_sha256':inputs,'suites':suites,'expected_isolated_buggy_source_sha256':expected_buggy,'source_derivation_sha256':sha(output/'isolated-bug-reference/derivation.json'),'producers_sha256':{p.name:sha(p) for p in [output/'producer.py',output/'ready_d4j_counts.py',output/'ready_d4j_reference.py']},'suite_validator_sha256':sha(ROOT/'scripts/study/api854/evaluate_champ_csv_ready.py'),'worker_id':'beam-pc1','cpu_slots':1,'java':11,'timezone':'America/Los_Angeles','test_source_changes':False,'assertion_changes':False,'ai_invalid_not_replayed':True,'kku_requests':0,'queue_mutations':0,'primary_results_allowed':False}
    write(output/'preexecution-seal.json',sealed)
    trees=worktrees/'beam-jsoup-ready-evaluator-v1';trees.mkdir(exist_ok=False)
    with cpu_slot(worktrees):
        original=observer.install_collector(evaluator,worktrees)
        try:
            with observer.observe_framework(d4j_root,output/'count-observer-framework'):
                for approach,manifest in suites.items():
                    begin=time.monotonic();folder=output/approach;approach_trees=trees/approach;approach_trees.mkdir()
                    paths,classes,sources=prepare_evaluation(str(d4j),{'project':'Jsoup','bug_id':1},approach_trees,folder/'setup',900)
                    setup_seconds=time.monotonic()-begin
                    for path,digest in sources.items():require(sha(prepared/'fixed-source'/path)==digest,'Actual modified fixed source differs')
                    for revision,tree in paths.items():
                        expected=native_seal['production_source_sha256']['fixed']['sources'] if revision=='f' else expected_buggy
                        for path,digest in expected.items():require(sha(tree/path)==digest,'Actual fixed/benchmark source differs')
                    record=evaluator.evaluate_run(evaluator.EvaluationConfig(project='Jsoup',bug_id=1,generator=approach,seed=101,budget=30,suite=folder/'suite.tar.bz2',fixed_worktree=paths['f'],buggy_worktree=paths['b'],output=folder/'evaluation',d4j=str(d4j),classes_file=classes,test_count=30,generation_seconds=manifest['upstream_generation_seconds'],timeout_seconds=900))
                    counts={}
                    for stage in ['fixed-1','fixed-2','buggy','coverage']:
                        path=folder/'evaluation'/stage/'actual-junit-counts.json'
                        if path.is_file():counts[stage]=read(path)
                    if record['status']=='complete':
                        require(set(counts)=={'fixed-1','fixed-2','buggy','coverage'},'Missing actual JUnit stage evidence')
                        for stage,c in counts.items():require(c.get('executed')==30 and c['skipped']==0 and c['errors']==0 and c['target_checks']==30,'Wrong stage counts: '+stage)
                    row={'project':'Jsoup','bug_id':1,'approach':approach,'status':record['status'],'full_defects4j_status':record['status'],'fixed_validation':record.get('fixed_validation'),'test_count':30,'suite_sha256':manifest['suite_sha256'],'record_sha256':sha(folder/'evaluation/record.json'),'fault_detected':record.get('fault_detected'),'coverage':{k:record.get(k) for k in ['line_covered','line_total','branch_covered','branch_total']},'counts':counts,'setup_seconds':setup_seconds,'upstream_generation_seconds':manifest['upstream_generation_seconds'],'worker_seconds_including_setup':time.monotonic()-begin,'canonical_evaluator_seconds':record['duration_seconds'],'primary':False}
                    write(folder/'outcome.json',row);rows.append(row)
        finally:evaluator.run_command=original
        for path,digest in runtime.items():require(sha(SNAPSHOT/path)==digest,'Frozen runtime changed after execution')
        require(beam_before=={path:sha(ROOT/path) for path in runtime},'Beam runtime changed')
    rows.sort(key=lambda r:APPROACHES.index(r['approach']));write(output/'results.json',rows)
    with (output/'results.csv').open('x',encoding='utf-8',newline='') as stream:
        writer=csv.DictWriter(stream,fieldnames=['project','bug_id','approach','status','full_defects4j_status','test_count','fault_detected','worker_seconds_including_setup','suite_sha256']);writer.writeheader()
        for row in rows:writer.writerow({k:row.get(k) for k in writer.fieldnames})
    write(output/'receipt.json',{'status':'jsoup_two_full_replays_and_two_retained_invalid_outcomes','generation_condition':CONDITION,'execution_condition':sealed['execution_condition'],'preexecution_seal_sha256':sha(output/'preexecution-seal.json'),'results_sha256':sha(output/'results.json'),'full_defects4j_completed':sum(r.get('full_defects4j_status')=='complete' for r in rows),'native_invalid_retained':2,'ai_replayed':False,'worker_id':'beam-pc1','cpu_slots':1,'total_wall_seconds':time.monotonic()-started,'current_beam_runtime_unchanged':True,'framework_restored':read(output/'count-observer-framework/restoration.json'),'kku_requests':0,'queue_mutations':0,'primary_results_added':0,'gate_a_approved':False,'full854_completed':False,'limitations':['Two valid algorithm archives replayed only; AI native fixed-invalid outcomes are not D4J measurements.','Class coverage applies to Document only; no full input-domain equivalence claim.','Java17/native UTC and Java11/D4J LosAngeles executions remain separate.','No reference candidates substituted for method results.']})
    print(json.dumps({'status':'jsoup_two_full_replays_and_two_retained_invalid_outcomes','records':[{'approach':r['approach'],'status':r['status'],'fault_detected':r['fault_detected']} for r in rows],'total_wall_seconds':time.monotonic()-started}))
if __name__=='__main__':
    parser=argparse.ArgumentParser(description=__doc__);parser.add_argument('--intake',type=Path,required=True);parser.add_argument('--output',type=Path,required=True);args=parser.parse_args()
    try:run(args.intake,args.output)
    except BaseException as error:
        if args.output.exists():write(args.output/'failed-attempt.json',{'status':'failed_attempt_retained','reason':type(error).__name__+': '+str(error)})
        raise
    finally:
        if args.output.exists():write(args.output/'checksums.json',{p.relative_to(args.output).as_posix():sha(p) for p in sorted(args.output.rglob('*')) if p.is_file()})
