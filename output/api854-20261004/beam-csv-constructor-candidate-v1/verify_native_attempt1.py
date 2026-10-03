"""Real bounded constructor proof on Beam Java11; independent sealed observations."""
import csv,gzip,hashlib,io,json,subprocess,sys,tarfile,tempfile
from datetime import datetime,timezone
from pathlib import Path
BASE=Path(__file__).resolve().parent; ROOT=BASE.parents[2]
sys.path[:0]=[str(ROOT),str(ROOT/'scripts/study')]
from scripts.study.api854.common import cpu_slot,implementation_hashes
OUT=BASE/'native-v1'
POLICY=json.loads((BASE/'policy.json').read_bytes())
OWNER='org.apache.commons.csv.ExtendedBufferedReader'
SOURCE='src/main/java/org/apache/commons/csv/ExtendedBufferedReader.java'
def sha(path):return hashlib.sha256(Path(path).read_bytes()).hexdigest()
def write(path,value):
    with Path(path).open('x',encoding='utf-8',newline='\n') as stream:json.dump(value,stream,ensure_ascii=False,indent=2);stream.write('\n')
def require(ok,message):
    if not ok:raise ValueError(message)
def execute(argv,label,check=True,binary=False):
    args=[str(a) for a in argv]
    result=subprocess.run(args,cwd=ROOT,capture_output=True,timeout=300)
    if binary:
        with gzip.open(OUT/(label+'.stdout.tar.gz'),'xb') as stream:stream.write(result.stdout)
    else:(OUT/(label+'.stdout.log')).write_bytes(result.stdout)
    (OUT/(label+'.stderr.log')).write_bytes(result.stderr)
    record={'argv':args,'cwd':str(ROOT),'exit_code':result.returncode,'stdout_sha256':hashlib.sha256(result.stdout).hexdigest(),'stderr_sha256':hashlib.sha256(result.stderr).hexdigest()}
    write(OUT/(label+'.command.json'),record)
    require(not check or result.returncode==0,'Command failed: '+label)
    return result,record
def rows(raw):return [json.loads(line) for line in raw.decode().splitlines() if line]
def validate(observations):
    observations=[r for r in observations if 'observation' in r]
    require([r['case'] for r in observations]==[c['case'] for c in POLICY['cases']],'Wrong case order/inventory')
    failed=[]
    for case,row in zip(POLICY['cases'],observations):
        if json.dumps(row['observation'],sort_keys=True)!=json.dumps(case['expected'],sort_keys=True):failed.append(case['case'])
    return observations,failed
def main():
    OUT.mkdir(exist_ok=False); runtime=implementation_hashes()
    worktrees='/home/beam/sqa-beam/worktrees';d4j=Path('/home/beam/sqa-beam/defects4j')
    child='from scripts.study.api854.common import cpu_slot\nimport sys\ntry:\n with cpu_slot(sys.argv[1]):pass\nexcept RuntimeError:sys.exit(9)\n'
    with cpu_slot(worktrees):
        held,_=execute([sys.executable,'-B','-c',child,worktrees],'held-cpu-lock',False);require(held.returncode==9,'CPU lock failed')
        java,_=execute(['java','-version'],'java-version');require('11.' in java.stderr.decode(),'Actual Java11 required')
        metadata=d4j/'framework/projects/Csv/active-bugs.csv'
        with metadata.open() as stream:commit=next(r for r in csv.DictReader(stream) if r['bug.id']=='1')
        revisions={'fixed':commit['revision.id.fixed'],'buggy':commit['revision.id.buggy']}
        write(OUT/'revision-metadata.json',{'record':commit,'metadata_sha256':sha(metadata),'framework_sha':subprocess.check_output(['git','-C',str(d4j),'rev-parse','HEAD']).decode().strip()})
        metadata_copy=OUT/'active-bugs.csv';metadata_copy.write_bytes(metadata.read_bytes())
        with tempfile.TemporaryDirectory(dir=ROOT/'output',prefix='.beam-csv-') as temporary:
            temp=Path(temporary).resolve();require(temp.is_relative_to((ROOT/'output').resolve()),'Unsafe temporary root')
            folders={};classes={};inventories={};archives={}
            for version,revision in revisions.items():
                folder=temp/version;folder.mkdir();folders[version]=folder;cp=folder/'classes';cp.mkdir();classes[version]=cp
                archive,record=execute(['git','-C',d4j/'project_repos/commons-csv.git','archive',revision,'src/main/java'],version+'-production-archive',binary=True);archives[version]=record
                with tarfile.open(fileobj=io.BytesIO(archive.stdout)) as tar:
                    for entry in tar.getmembers():
                        if not entry.isfile():continue
                        dst=(folder/entry.name).resolve();require(dst.is_relative_to(folder),'Unsafe archive path');dst.parent.mkdir(parents=True,exist_ok=True);dst.write_bytes(tar.extractfile(entry).read())
                if version=='fixed':
                    raw=(BASE/'received-aom/fixed-source'/SOURCE).read_bytes()
                    require((folder/SOURCE).read_bytes().replace(b'\r\n',b'\n')==raw.replace(b'\r\n',b'\n'),'Fixed source differs');(folder/SOURCE).write_bytes(raw)
                inventories[version]={p.relative_to(folder).as_posix():sha(p) for p in sorted((folder/'src/main/java').rglob('*.java'))}
            sealed={n:sha(BASE/n) for n in ['policy.json','received-provenance.json','CsvConstructorProbe.java','CsvConstructorEntryTrace.java','verify_native.py']}
            write(OUT/'preexecution-seal.json',{'sealed_at_utc':datetime.now(timezone.utc).isoformat(),'suite_sha256':sealed,'source_sha256':inventories,'revisions':revisions,'production_archives':archives,'runtime_source_sha256':runtime,'worker_id':'beam-pc1','cpu_slots':1,'selection_used_buggy_outcomes':False,'primary':False})
            stages={};class_hashes={}
            for version in revisions:
                execute(['javac','--release','7','-g','-encoding','UTF-8','-d',classes[version],folders[version]/SOURCE,BASE/'CsvConstructorProbe.java'],version+'-compile')
                class_hashes[version]=sha(classes[version]/(OWNER.replace('.','/')+'.class'))
                for repeat in ['first','second']:
                    label=version+'_'+repeat;result,record=execute(['java','-Duser.timezone=UTC','-cp',classes[version],'org.apache.commons.csv.CsvConstructorProbe'],label)
                    record['observations'],record['failed_cases']=validate(rows(result.stdout));require(not record['failed_cases'],'Constructor independent oracle failed '+label);stages[label]=record
                require(stages[version+'_first']['observations']==stages[version+'_second']['observations'],'Unstable repeat')
            execute(['javac','--add-modules','jdk.jdi','-d',classes['fixed'],BASE/'CsvConstructorEntryTrace.java'],'compile-trace')
            for version in revisions:
                result,record=execute(['java','--add-modules','jdk.jdi','-cp',classes['fixed'],'CsvConstructorEntryTrace',classes[version]],version+'-entry-trace')
                traced=rows(result.stdout);record['observations'],failed=validate(traced);require(not failed and record['observations']==stages[version+'_first']['observations'],'Tracing changed observations')
                entries=[r for r in traced if r.get('method_entry')];require(len(entries)==5 and {r['case'] for r in entries}=={c['case'] for c in POLICY['cases']},'Missing exact constructor entries')
                require(all((r['class'],r['method'],r['descriptor'])==(OWNER,'<init>',POLICY['descriptor']) and r['source_line']>0 for r in entries),'Wrong constructor identity')
                require(sha(classes[version]/(OWNER.replace('.','/')+'.class'))==class_hashes[version],'Production bytecode changed');record['exact_entries']=entries;stages[version+'_entry_trace']=record
            source=folders['fixed']/SOURCE;original=source.read_bytes();text=original.decode();old='private int lastChar = UNDEFINED;';require(text.count(old)==1,'Ambiguous mutation')
            source.write_text(text.replace(old,'private int lastChar = 0;'),encoding='utf-8');(OUT/'last-char-mutated.java').write_bytes(source.read_bytes())
            execute(['javac','--release','7','-g','-encoding','UTF-8','-cp',classes['fixed'],'-d',classes['fixed'],source],'mutation-compile')
            result,mutation=execute(['java','-cp',classes['fixed'],'org.apache.commons.csv.CsvConstructorProbe'],'mutation')
            mutation['observations'],mutation['failed_cases']=validate(rows(result.stdout));require(mutation['failed_cases']==[c['case'] for c in POLICY['cases'] if c['input'] is not None],'Constructor-state mutation escaped')
            source.write_bytes(original);execute(['javac','--release','7','-g','-encoding','UTF-8','-cp',classes['fixed'],'-d',classes['fixed'],source],'restore-compile')
            require(sha(classes['fixed']/(OWNER.replace('.','/')+'.class'))==class_hashes['fixed'],'Bytecode restore differs')
            require(sealed=={n:sha(BASE/n) for n in sealed} and runtime==implementation_hashes(),'Producer/shared runtime changed')
    reuse,_=execute([sys.executable,'-B','-c',child,worktrees],'released-cpu-lock');require(reuse.returncode==0,'Slot not reusable')
    receipt={'status':'pass','declarations':1,'unique_cases':5,'fixed_observations':10,'buggy_observations':10,'stages':stages,'controlled_mutation':mutation,'suite_sha256':sealed,'runtime_source_sha256':runtime,'source_sha256':inventories,'production_class_sha256':class_hashes,'preexecution_seal_sha256':sha(OUT/'preexecution-seal.json'),'worker_id':'beam-pc1','cpu_slots':1,'cpu_lock_exits':[9,0],'manual_component_fault_detected':False,'full_legal_domain_approved':False,'shared_integration_approved':False,'primary_added':0,'gate_a_approved':False,'kku_requests':0,'queue_mutations':0}
    write(OUT/'receipt.json',receipt);print(json.dumps({'status':'pass','declarations':1,'unique_cases':5,'exact_entries_per_revision':5}))
if __name__=='__main__':
    try:main()
    except BaseException as error:
        if OUT.exists():write(OUT/'failure.json',{'status':'failed_attempt_retained','reason':type(error).__name__+': '+str(error)})
        raise
    finally:
        if OUT.exists():write(OUT/'checksums.json',{p.relative_to(OUT).as_posix():sha(p) for p in sorted(OUT.rglob('*')) if p.is_file()})
