"""Beam repeats the received Cli option-order diagnostic without changing its oracle."""
import argparse,base64,hashlib,io,json,os,re,subprocess,sys,tarfile,tempfile,time
from datetime import datetime,timezone
from pathlib import Path
ROOT=Path(__file__).resolve().parents[3]
SNAPSHOT=ROOT/'.local/api854/beam-v12-snapshot-63ad1956-v2'
sys.path[:0]=[str(SNAPSHOT),str(SNAPSHOT/'scripts/study')]
from scripts.study.api854.common import cpu_slot
def sha(path):return hashlib.sha256(Path(path).read_bytes()).hexdigest()
def read(path):return json.loads(Path(path).read_bytes())
def require(ok,message):
    if not ok:raise ValueError(message)
def write(path,value):
    with Path(path).open('x',encoding='utf-8',newline='\n') as stream:json.dump(value,stream,ensure_ascii=False,indent=2);stream.write('\n')
def decode(raw):
    match=re.fullmatch(r'void\|state=cli:array\[(.*)\]:array\[java\.lang\.String:([A-Za-z0-9+/=]+);\]',raw)
    require(match is not None,'Unexpected bounded observation grammar')
    options=re.findall(r'option:([^:;]+):array\[java\.lang\.String:([A-Za-z0-9+/=]+);\];',match[1])
    require(len(options)==2 and ''.join('option:'+k+':array[java.lang.String:'+v+';];' for k,v in options)==match[1],'Extra/missing option state')
    pairs=[(k,base64.b64decode(v,validate=True).decode('utf-8')) for k,v in options]
    require(len({k for k,v in pairs})==2,'Duplicate option key')
    return {'ordered_options':pairs,'option_multiset':sorted(pairs),'positional_arguments':[base64.b64decode(match[2],validate=True).decode('utf-8')]}
def command(argv,cwd,output,label,binary=False):
    started=time.monotonic();args=[str(a) for a in argv];result=subprocess.run(args,cwd=cwd,capture_output=True,timeout=300)
    (output/(label+('.stdout.tar' if binary else '.stdout.log'))).write_bytes(result.stdout);(output/(label+'.stderr.log')).write_bytes(result.stderr)
    record={'argv':args,'cwd':str(cwd),'exit_code':result.returncode,'duration_seconds':time.monotonic()-started,'stdout_sha256':hashlib.sha256(result.stdout).hexdigest(),'stderr_sha256':hashlib.sha256(result.stderr).hexdigest()}
    write(output/(label+'.command.json'),record);require(result.returncode==0,'Command failed: '+label);return result.stdout
def run(intake,output):
    intake=intake.resolve();output=output.resolve()
    require(intake.is_relative_to(ROOT/'output') and output.is_relative_to(ROOT/'output') and not output.exists(),'New contained evidence required')
    for path,digest in read(intake/'checksums.json').items():require(sha(intake/path)==digest,'Changed received intake')
    peer=intake/'received-champ/output/api854-20261004'
    audit=peer/'champ-cli-order-oracle-audit-v2';native=peer/'champ-cli-native-measurement-v1'
    plan=read(audit/'preexecution-plan.json');native_seal=read(native/'preexecution-seal.json');peer_verdict=read(audit/'receipt.json')
    require(plan['aom_commit']=='63ad195623c2ed3f67f3ae232c00c54d3160ce72' and peer_verdict['confirmed_semantic_fault'] is False,'Unexpected pinned review')
    original=read(native/'fscs-art/receipt.json');require(original['fault_detected'] is True and sha(native/'fscs-art/receipt.json')==plan['original_receipt_sha256'],'Original raw flag changed')
    runtime=native_seal['runtime_source_sha256']
    for path,digest in runtime.items():require(sha(SNAPSHOT/path)==digest,'Frozen helper/runtime changed')
    current_before={path:sha(ROOT/path) for path in runtime}
    output.mkdir(parents=True,exist_ok=False)
    (output/'producer.py').write_bytes(Path(__file__).read_bytes());(output/'CliOrderDiagnostic.java').write_bytes((audit/'CliOrderDiagnostic.java').read_bytes())
    require(sha(output/'CliOrderDiagnostic.java')==plan['diagnostic_sha256'],'Received diagnostic source differs')
    # This parser compares exact mappings as a multiset; positional arguments remain ordered.
    require(decode(plan['original_fixed_assertion'])['option_multiset']==[('extra','left'),('x','alpha')],'Wrong sealed diagnostic expected state')
    d4j=Path('/home/beam/sqa-beam/defects4j');dependencies=[]
    for path,digest in plan['production_dependencies_sha256'].items():
        dependency=d4j/path;require(sha(dependency)==digest,'Production dependency differs');dependencies.append(dependency)
    write(output/'preexecution-seal.json',{'sealed_at_utc':datetime.now(timezone.utc).isoformat(),'condition':plan['condition'],'execution_condition':'beam-cli-order-java11-utc-joint-review-v1','aom_commit':plan['aom_commit'],'champ_commit':read(intake/'receipt.json')['source_commit'],'producer_sha256':sha(output/'producer.py'),'diagnostic_sha256':sha(output/'CliOrderDiagnostic.java'),'received_original_receipt_sha256':sha(native/'fscs-art/receipt.json'),'received_native_manifest_sha256':sha(native/'checksums.json'),'received_diagnostic_manifest_sha256':sha(audit/'checksums.json'),'input_vector':plan['input_vector'],'original_fixed_assertion':plan['original_fixed_assertion'],'runtime_source_sha256':runtime,'production_dependencies_sha256':plan['production_dependencies_sha256'],'exact_production_revisions':native_seal['exact_production_revisions'],'worker_id':'beam-pc1','cpu_slots':1,'java':11,'timezone':'UTC','original_suites_changed':False,'primary':False,'kku_requests':0})
    observations={};worktrees=Path('/home/beam/sqa-beam/worktrees')
    with cpu_slot(worktrees),tempfile.TemporaryDirectory(dir=worktrees,prefix='.beam-cli-review-') as temporary:
        temp=Path(temporary).resolve();require(temp.is_relative_to(worktrees.resolve()),'Unsafe native scratch')
        helperclasses=temp/'helperclasses';helperclasses.mkdir()
        command(['java','-version'],temp,output,'java-version')
        command(['javac','--release','8','-g','-d',helperclasses,SNAPSHOT/'algorithms/java/SqaProbe.java',output/'CliOrderDiagnostic.java'],temp,output,'compile-diagnostic')
        for version,revision in native_seal['exact_production_revisions'].items():
            folder=temp/version;folder.mkdir()
            archive=command(['git','-C',d4j/'project_repos/commons-cli.git','archive',revision,'src/java'],temp,output,version+'-production-archive',True)
            require(hashlib.sha256(archive).hexdigest()==native_seal['production_source_sha256'][version]['archive_sha256'],'Native production archive differs')
            with tarfile.open(fileobj=io.BytesIO(archive)) as tar:
                for member in tar:
                    if not member.isfile():continue
                    path=(folder/member.name).resolve();require(path.is_relative_to(folder),'Unsafe source path');path.parent.mkdir(parents=True,exist_ok=True);path.write_bytes(tar.extractfile(member).read())
            for path,digest in native_seal['production_source_sha256'][version]['sources'].items():require(sha(folder/path)==digest,'Native source differs')
            contract=folder/'src/java/org/apache/commons/cli/CommandLine.java';(output/(version+'-CommandLine.java')).write_bytes(contract.read_bytes())
            classes=folder/'classes';classes.mkdir()
            command(['javac','--release','7','-g','-encoding','UTF-8','-cp',os.pathsep.join(map(str,dependencies)),'-d',classes,*sorted((folder/'src/java').rglob('*.java'))],temp,output,'compile-'+version)
            for repeat in [1,2]:
                label=version+'-'+str(repeat)
                raw=command(['java','-Duser.timezone=UTC','-cp',os.pathsep.join(map(str,[classes,helperclasses,*dependencies])),'CliOrderDiagnostic'],temp,output,label)
                lines=[line.decode().split(':',1)[1] for line in raw.splitlines() if line.startswith(b'CLI_DIAGNOSTIC:')]
                require(len(lines)==1,'Missing/duplicate raw observation');observations[label]={'raw':lines[0],'decoded':decode(lines[0])}
        require(observations['fixed-1']==observations['fixed-2'] and observations['buggy-1']==observations['buggy-2'],'Unstable diagnostic')
        fixed=observations['fixed-1'];buggy=observations['buggy-1']
        require(fixed['raw']==plan['original_fixed_assertion'] and buggy['raw']!=fixed['raw'],'Original mismatch not reproduced')
        for row in [fixed,buggy]:require(row['decoded']['option_multiset']==[('extra','left'),('x','alpha')] and row['decoded']['positional_arguments']==['positional'],'Content/ordered args differ')
        require(fixed['decoded']['ordered_options']!=buggy['decoded']['ordered_options'],'Order mismatch missing')
        require(current_before=={path:sha(ROOT/path) for path in runtime},'Current Beam runtime changed')
    write(output/'receipt.json',{'status':'beam_scoped_cli_order_false_positive_confirmed_and_quarantined','observations':observations,'raw_native_fault_flag_retained':True,'raw_buggy_failed_tests':1,'confirmed_semantic_fault':False,'scientific_fault_count_added':0,'shared_oracle_changed':False,'original_suites_changed':False,'worker_id':'beam-pc1','cpu_slots':1,'preexecution_seal_sha256':sha(output/'preexecution-seal.json'),'primary_results_added':0,'kku_requests':0,'queue_mutations':0,'gate_a_approved':False,'next_action':'Aom/Champ joint review and prospective CommandLine unordered-options oracle with Cli16 regression; do not sort ordered args/buffers/all arrays.','limitations':'Only this sealed two-option/one-argument diagnostic on native production revisions. Not full Cli-1 evaluation/domain approval or proof no Cli bug exists.'})
    print(json.dumps({'status':'beam_scoped_cli_order_false_positive_confirmed_and_quarantined','confirmed_semantic_fault':False}))
if __name__=='__main__':
    parser=argparse.ArgumentParser(description=__doc__);parser.add_argument('--intake',type=Path,required=True);parser.add_argument('--output',type=Path,required=True);args=parser.parse_args()
    try:run(args.intake,args.output)
    except BaseException as error:
        if args.output.exists():write(args.output/'failed-attempt.json',{'status':'failed_attempt_retained','reason':type(error).__name__+': '+str(error)})
        raise
    finally:
        if args.output.exists():write(args.output/'checksums.json',{p.relative_to(args.output).as_posix():sha(p) for p in sorted(args.output.rglob('*')) if p.is_file()})
