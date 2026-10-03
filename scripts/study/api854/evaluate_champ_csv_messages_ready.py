"""Beam replays the new Messages-condition Csv suites on real Defects4J.

Run directly in a fresh process. Frozen v12 imports come from the previously
audited isolated snapshot, not the current Beam source condition. No API calls.
"""
import argparse,hashlib,io,json,shutil,subprocess,sys,tarfile,time,difflib,tempfile
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
def isolated_bug_reference(d4j,native_seal,output):
    """Derive expected D4J buggy sources from fixed revision plus official bug patch."""
    repo=d4j/'project_repos/commons-csv.git';patch=d4j/'framework/projects/Csv/patches/1.src.patch'
    metadata=d4j/'framework/projects/Csv/active-bugs.csv'
    source='src/main/java/org/apache/commons/csv/ExtendedBufferedReader.java'
    raw_patch=patch.read_bytes();framework_commit=subprocess.check_output(['git','-C',str(d4j),'rev-parse','HEAD']).decode().strip()
    canonical=subprocess.check_output(['git','-C',str(d4j),'show','HEAD:framework/projects/Csv/patches/1.src.patch'])
    require(raw_patch==canonical,'Installed official patch differs from framework Git blob')
    require(raw_patch.count(b'diff --git ')==1 and raw_patch.startswith(('diff --git a/'+source+' b/'+source+'\n').encode()),'Unexpected isolated Csv patch scope')
    import csv
    with metadata.open() as stream:record=next(r for r in csv.DictReader(stream) if r['bug.id']=='1')
    require(record['revision.id.fixed']==native_seal['exact_production_revisions']['fixed'] and record['revision.id.buggy']==native_seal['exact_production_revisions']['buggy'],'Installed active bug metadata differs')
    folder=output/'isolated-bug-reference';folder.mkdir(exist_ok=False)
    (folder/'1.src.patch').write_bytes(raw_patch);(folder/'active-bugs.csv').write_bytes(metadata.read_bytes())
    fixed_revision=native_seal['exact_production_revisions']['fixed']
    archive=subprocess.check_output(['git','-C',str(repo),'archive',fixed_revision,'src/main/java'])
    reference=folder/'expected-buggy';reference.mkdir()
    with tarfile.open(fileobj=io.BytesIO(archive)) as tar:
        for member in tar:
            if not member.isfile():continue
            path=(reference/member.name).resolve();require(path.is_relative_to(reference),'Unsafe reference archive path');path.parent.mkdir(parents=True,exist_ok=True);path.write_bytes(tar.extractfile(member).read())
    expected_fixed=native_seal['production_source_sha256']['fixed']['sources']
    for name,digest in expected_fixed.items():require(sha(reference/name)==digest,'Fixed reference differs from native fixed sources')
    fixed_bytes=(reference/source).read_bytes();(folder/'fixed-ExtendedBufferedReader.java').write_bytes(fixed_bytes)
    command=['patch','--batch','--fuzz=0','-p1','-i',str(folder/'1.src.patch')]
    # GNU patch needs chmod/rename support; use native Linux for its reference
    # scratch tree, then preserve resulting bytes in the public Windows packet.
    scratch_root=Path('/home/beam/sqa-beam/worktrees').resolve()
    with tempfile.TemporaryDirectory(dir=scratch_root,prefix='.beam-csv-reference-') as temporary:
        scratch=Path(temporary).resolve();require(scratch.is_relative_to(scratch_root),'Unsafe reference scratch tree')
        for name in expected_fixed:
            dst=scratch/name;dst.parent.mkdir(parents=True,exist_ok=True);shutil.copyfile(reference/name,dst)
        result=subprocess.run(command,cwd=scratch,capture_output=True,timeout=60)
        if result.returncode==0:
            for name in expected_fixed:shutil.copyfile(scratch/name,reference/name)
    (folder/'patch.stdout.log').write_bytes(result.stdout);(folder/'patch.stderr.log').write_bytes(result.stderr)
    require(result.returncode==0,'Official isolated-bug patch failed')
    expected={p.relative_to(reference).as_posix():sha(p) for p in sorted(reference.rglob('*.java'))}
    require(set(expected)==set(expected_fixed),'Official patch changed source inventory')
    native_bug=subprocess.check_output(['git','-C',str(repo),'show',native_seal['exact_production_revisions']['buggy']+':'+source])
    require(hashlib.sha256(native_bug).hexdigest()==native_seal['production_source_sha256']['buggy']['sources'][source],'Native buggy source differs from sealed revision')
    (folder/'native-ExtendedBufferedReader.java').write_bytes(native_bug)
    (folder/'native-vs-isolated-bug.diff').write_text(''.join(difflib.unified_diff(native_bug.decode().splitlines(keepends=True),(reference/source).read_text().splitlines(keepends=True),fromfile='native-parent-git-revision',tofile='official-isolated-d4j-buggy')),encoding='utf-8',newline='\n')
    write(folder/'derivation.json',{'framework_commit':framework_commit,'official_patch_sha256':sha(folder/'1.src.patch'),'patch_git_blob_verified':True,'active_bug_metadata_sha256':sha(folder/'active-bugs.csv'),'active_bug_record':record,'fixed_archive_sha256':hashlib.sha256(archive).hexdigest(),'patch_command':command,'patch_exit_code':result.returncode,'expected_isolated_buggy_sources_sha256':expected,'native_and_isolated_buggy_sources_identical':expected==native_seal['production_source_sha256']['buggy']['sources'],'production_source_replacement':False,'note':'Expected reference only; actual Defects4J production sources are validated byte-for-byte and never overwritten.'})
    return expected
def run(intake,output):
    intake=intake.resolve();output=output.resolve();started=time.monotonic()
    require(intake.is_relative_to(ROOT/'output') and output.is_relative_to(ROOT/'output') and not output.exists(),'New contained evaluation output required')
    intake_receipt=read(intake/'receipt.json');require(intake_receipt['status']=='received_and_sha_verified','Verified intake required')
    for name,digest in read(intake/'checksums.json').items():require(sha(intake/name)==digest,'Intake changed: '+name)
    received=intake/'received-champ'
    generation=received/(PREFIX+'champ-csv-messages-generation-v2')
    native=received/(PREFIX+'champ-csv-messages-native-measurement-v1')
    native_receipt=read(native/'receipt.json');native_seal=read(native/'preexecution-seal.json')
    require(len(native_receipt['records'])==4 and all(r['status']=='native_fixed_twice_buggy_coverage_measured' for r in native_receipt['records']),'Four unchanged valid native suites required')
    plan=read(generation/'preexecution-plan.json')
    require(plan['aom_commit']==AOM,'Different generation source condition')
    require(plan['condition']=='api854-20261004-csv-messages-disabled-thinking-native-development-v2' and native_receipt['condition']==plan['condition'],'Different generation condition')
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
        generation_seconds=peer.get('generation_seconds')
        generation_evidence=None
        if approach.startswith('kku-'):
            gen=read(generation/approach/'generation-receipt.json')
            require(gen['outcome']=='response_received' and sha(generation/approach/'raw-response.txt')==gen['raw_response_sha256'],'AI source response differs')
            generation_evidence=read(generation/approach/'response.json')['evidence']
            generation_seconds=(datetime.fromisoformat(generation_evidence['ended_at_utc'])-datetime.fromisoformat(generation_evidence['started_at_utc'])).total_seconds()
        manifest=check_suite(native/approach/'packaged-suite')
        archive=folder/'suite.tar.bz2';shutil.copyfile(native/approach/'packaged-suite/suite.tar.bz2',archive)
        (folder/'suite-manifest.json').write_bytes((native/approach/'packaged-suite/suite-manifest.json').read_bytes())
        suites[approach]={'suite_sha256':sha(archive),'manifest_sha256':sha(folder/'suite-manifest.json'),'test_count':manifest['test_count'],'source_sha256':manifest['source_sha256'],'peer_status':peer['status'],'generation_seconds':generation_seconds,'generation_time_source':'Actual request start/end timestamps' if generation_evidence else 'Actual native algorithm generation command duration'}
    d4j_root=Path('/home/beam/sqa-beam/defects4j')
    expected_isolated_buggy=isolated_bug_reference(d4j_root,native_seal,output)
    sealed={'sealed_at_utc':datetime.now(timezone.utc).isoformat(),'generation_condition':plan['condition'],'execution_condition':'beam-csv-messages-d4j-isolated-bug-counted-development-v3','source_commit':AOM,'champ_commit':intake_receipt['source_commit'],'intake_receipt_sha256':sha(intake/'receipt.json'),'intake_checksums_sha256':sha(intake/'checksums.json'),'producer_sha256':sha(output/'producer.py'),'counter_observer_sha256':sha(output/'ready_d4j_counts.py'),'frozen_runtime_source_sha256':runtime,'current_beam_runtime_before_sha256':beam_before,'frozen_inputs_sha256':frozen_inputs,'suites':suites,'native_production_source_sha256':native_seal['production_source_sha256'],'expected_isolated_buggy_source_sha256':expected_isolated_buggy,'source_derivation_sha256':sha(output/'isolated-bug-reference/derivation.json'),'worker_id':'beam-pc1','cpu_slots':1,'execution_java':11,'execution_timezone':'America/Los_Angeles','peer_native_java':17,'peer_native_timezone':'UTC','test_source_changes':False,'assertion_changes':False,'generation_requests':0,'queue_mutations':0,'primary_results_allowed':False,'cli_fault_approval':'pending_joint_oracle_order_review'}
    write(output/'preexecution-seal.json',sealed)
    d4j_root=Path('/home/beam/sqa-beam/defects4j');d4j=d4j_root/'framework/bin/defects4j';worktrees=Path('/home/beam/sqa-beam/worktrees')
    trees=worktrees/'beam-csv-messages-ready-evaluator-v3';trees.mkdir(exist_ok=False)
    setup_seconds={}
    with cpu_slot(worktrees):
        original_command=observer.install_collector(evaluator,worktrees)
        try:
            with observer.observe_framework(d4j_root,output/'count-observer-framework'):
                for approach in APPROACHES:
                    if approach not in suites:continue
                    folder=output/approach;manifest=suites[approach];begin=time.monotonic()
                    approach_trees=trees/approach;approach_trees.mkdir(exist_ok=False)
                    setup_start=time.monotonic()
                    paths,classes,sources=prepare_evaluation(str(d4j),{'project':'Csv','bug_id':1},approach_trees,folder/'setup',900)
                    setup_seconds[approach]=time.monotonic()-setup_start
                    for name,digest in sources.items():require(sha(SNAPSHOT/PREP/'fixed-source'/name)==digest,'Actual fixed D4J source differs: '+name)
                    for revision,tree in paths.items():
                        expected_sources=native_seal['production_source_sha256']['fixed']['sources'] if revision=='f' else expected_isolated_buggy
                        for name,digest in expected_sources.items():require(sha(tree/name)==digest,'Actual native/D4J full production source differs: '+approach+'/'+revision+'/'+name)
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
                    row={'project':'Csv','bug_id':1,'approach':approach,'status':record['status'],'fixed_validation':record.get('fixed_validation'),'test_count':manifest['test_count'],'fault_detected':record.get('fault_detected'),'coverage':{k:record.get(k) for k in ['line_covered','line_total','branch_covered','branch_total']},'generation_seconds':manifest['generation_seconds'],'generation_time_source':manifest['generation_time_source'],'evaluation_seconds':time.monotonic()-begin,'setup_seconds':setup_seconds[approach],'canonical_evaluator_seconds':record['duration_seconds'],'suite_sha256':manifest['suite_sha256'],'record_sha256':sha(folder/'evaluation/record.json'),'stages':stages,'primary':False}
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
    write(output/'receipt.json',{'status':'messages_packet_four_outcomes_recorded','champ_commit':intake_receipt['source_commit'],'generation_condition':plan['condition'],'execution_condition':sealed['execution_condition'],'source_commit':AOM,'preexecution_seal_sha256':sha(output/'preexecution-seal.json'),'results_sha256':sha(output/'results.json'),'four_outcomes':len(rows),'full_defects4j_completed':sum(r['status']=='complete' for r in rows),'invalid_outcomes':sum(r['status'] in {'invalid','invalid_generation_truncated'} for r in rows),'setup_seconds':setup_seconds,'total_wall_seconds':time.monotonic()-started,'worker_id':'beam-pc1','cpu_slots':1,'framework_restored':read(output/'count-observer-framework/restoration.json'),'current_beam_runtime_unchanged':True,'generation_requests':0,'queue_mutations':0,'primary_results_added':0,'gate_a_approved':False,'all_691_complete':False,'cli_fault_approval':'pending_joint_oracle_order_review','limitations':['Retain old baseline invalid AI outcomes separately; never pool the new Messages transport condition with the baseline.','Native parent buggy revision differs from official D4J isolated-bug source; verified fixed plus official patch bytes, no production replacement.','Native Java17 UTC and this Defects4J Java11 America/Los_Angeles measurement remain separate execution profiles.','AI target checks are null when not instrumented in the unchanged suite; JUnit counts are real XML counts.','Invalid suites are not admitted to buggy/coverage.','AI legal Reader inputs are broader than algorithm bounded streams; full domain equivalence not approved.','No full semantic/domain equivalence, Gate A or full854 completion claimed.']})
    print(json.dumps({'status':'messages_packet_four_outcomes_recorded','records':[{'approach':r['approach'],'status':r['status'],'fault_detected':r['fault_detected']} for r in rows],'total_wall_seconds':time.monotonic()-started}))
if __name__=='__main__':
    parser=argparse.ArgumentParser(description=__doc__);parser.add_argument('--intake',type=Path,required=True);parser.add_argument('--output',type=Path,required=True);args=parser.parse_args()
    try:run(args.intake,args.output)
    except BaseException as error:
        if args.output.exists():write(args.output/'failed-attempt.json',{'status':'failed_attempt_retained','reason':type(error).__name__+': '+str(error)})
        raise
    finally:
        if args.output.exists():write(args.output/'checksums.json',{p.relative_to(args.output).as_posix():sha(p) for p in sorted(args.output.rglob('*')) if p.is_file()})
