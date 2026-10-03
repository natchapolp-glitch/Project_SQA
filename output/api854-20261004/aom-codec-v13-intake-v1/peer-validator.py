"""Seal and run bounded Codec helpers on Beam's one-slot Java11 host.

No shared runtime/preparation, Defects4J evaluation, provider or live queue changes.
"""
from pathlib import Path
from datetime import datetime,timezone
import base64,csv,gzip,hashlib,io,json,os,platform,subprocess,sys,tarfile,tempfile,time
BASE=Path(__file__).resolve().parent;ROOT=BASE.parents[2]
sys.path[:0]=[str(ROOT),str(ROOT/'scripts/study')]
from scripts.study.api854.common import cpu_slot
POLICY=json.loads((BASE/'policy.json').read_text(encoding='utf-8'))
CASES=POLICY['cases'];TARGETS={r['target']['method']:r for r in POLICY['targets']}
OUT=BASE/'native-v1'
SOURCE_PREFIX='src/java/org/apache/commons/codec/language/'
OWNERS=['org.apache.commons.codec.language.Metaphone','org.apache.commons.codec.language.SoundexUtils']
JVM=['-Duser.timezone=UTC','-Duser.language=en','-Duser.country=US']
def sha(path):return hashlib.sha256(path.read_bytes()).hexdigest()
def read(path):return json.loads(path.read_text(encoding='utf-8'))
def require(ok,message):
    if not ok:raise ValueError(message)
def write(path,data):
    with path.open('x',encoding='utf-8',newline='\n') as f:json.dump(data,f,ensure_ascii=False,indent=2);f.write('\n')
def expected(c):
    text=c['buffer'];i=c['index'];method=c['method'];error=None
    if method=='isNextChar':
        neighbors={j:a[1] for j,a in enumerate(zip(text,text[1:]))};value=neighbors.get(i)==c['char']
    elif method=='isPreviousChar':
        neighbors={j+1:a[0] for j,a in enumerate(zip(text,text[1:]))};value=neighbors.get(i)==c['char']
    elif method=='isVowel':
        if i<0 or i>=len(text):value=None;error='java.lang.StringIndexOutOfBoundsException'
        else:value=text[i] in {'A','E','I','O','U'}
    elif method=='regionMatch':
        value=i>=0 and i+len(c['needle'])<=len(text) and text[i:i+len(c['needle'])]==c['needle']
    else:
        table=POLICY['metaphone_encoded_reference_table']
        codes=[table['<null>' if s is None else s] for s in (c['s1'],c['s2'])]
        value=sum(left==right for left,right in zip(*codes))
    require(type(c['expected_value'])==type(value) and c['expected_value']==value and c['expected_exception_class']==error,
            'Predeclared literal differs from independent oracle '+c['case'])
    return {'value':value,'exception_class':error,'buffer_contents':text,
            'buffer_length':None if text is None else len(text),'buffer_unchanged':True,
            'receiver_max_code_len':None if method=='difference' else 4,
            'encoder_max_code_len':4 if method=='difference' else None,
            'encoder_encoded_inputs':codes if method=='difference' else None}
def records(raw):return [json.loads(line) for line in raw.decode('utf-8').splitlines() if line.strip()]
def validate(rows,allow_failures=False):
    cases=[r for r in rows if 'case' in r and not r.get('method_entry')]
    require([r['case'] for r in cases]==[c['case'] for c in CASES],'Case order/inventory differs')
    for c,row in zip(CASES,cases):
        method=c['method'];target=TARGETS[method]
        require(row.get('setup_succeeded') is True and row.get('target_invoked') is True,'Fixture error is not target evidence')
        require(row['method']==method and row['descriptor']==target['descriptor'] and
                row['declaring_class']==target['declaring_class'] and row['receiver_class']==target['target']['class'],
                'Wrong exact helper/receiver/descriptor')
        oracle=expected(c);require(row['expected_observation']==oracle,'Independent oracle differs '+c['case'])
        require(type(row['expected_observation']['value'])==type(oracle['value']),'Expected value type differs')
        pre=row['pre_state'];post=row['post_state']
        require(pre['buffer_contents']==c['buffer'] and pre['buffer_length']==oracle['buffer_length'],
                'Wrong preexecution buffer state')
        require(pre==post,'Buffer state changed')
        passed=row['observation']==oracle and type(row['observation']['value'])==type(oracle['value'])
        require(type(row['target_check_passed']) is bool and row['target_check_passed'] is passed,'Assertion counters differ')
        require(row['failure_class']==(None if passed else 'java.lang.AssertionError'),'Wrong target failure class')
        if not passed:require(allow_failures,'Production fixed oracle failed '+c['case'])
    passed=sum(r['target_check_passed'] for r in cases)
    require([r for r in rows if r.get('summary')]==[{'summary':True,'executed':43,'target_checks':43,
         'passed':passed,'failed':43-passed,'skipped':0,'fixture_errors':0}],'Counters/skips/fixture errors differ')
    return cases
def validate_trace(rows,exit_code=0):
    entries=[r for r in rows if r.get('method_entry')];first={}
    for row in entries:
        require(row['case'] in {c['case'] for c in CASES} and row['class'] in OWNERS and row['source_line']>0,
                'Wrong entry case/owner/line')
        first.setdefault(row['case'],row)
    require(set(first)=={c['case'] for c in CASES},'Missing exact entry')
    for c in CASES:
        row=first[c['case']];t=TARGETS[c['method']]
        require((row['class'],row['method'],row['descriptor'])==
                (t['declaring_class'],c['method'],t['descriptor']),'Wrong first target entry descriptor')
    require(len({(r['class'],r['method'],r['descriptor']) for r in first.values()})==5,'Five declarations required')
    require([r for r in rows if r.get('trace_summary')]==[{'trace_summary':True,'method_entries':len(entries),
            'debuggee_exit_code':exit_code}],'Trace counters differ')
    return list(first.values())
def execute(argv,name,check=True,binary=False):
    begin=time.monotonic();run=subprocess.run(list(map(str,argv)),capture_output=True,timeout=180)
    stdout=OUT/(name+('.stdout.tar.gz' if binary else '.stdout.log'))
    with stdout.open('xb') as f:f.write(gzip.compress(run.stdout,mtime=0) if binary else run.stdout)
    stderr=OUT/(name+'.stderr.log');stderr.write_bytes(run.stderr)
    record={'argv':list(map(str,argv)),'exit_code':run.returncode,'duration_seconds':time.monotonic()-begin,
            'stdout_sha256':hashlib.sha256(run.stdout).hexdigest(),'stderr_sha256':sha(stderr),
            'retained_stdout':stdout.name,'retained_stdout_sha256':sha(stdout)}
    write(OUT/(name+'.command.json'),record)
    if check:require(run.returncode==0,name+' failed: '+run.stderr.decode(errors='replace')[-1800:])
    return run,record
def encoded(value):return '-' if value is None else base64.b64encode(value.encode('utf-8')).decode()
def main():
    require(not OUT.exists(),'Evidence output exists; choose new output/attempt')
    OUT.mkdir();before={p:sha(ROOT/p) for p in read(ROOT/'output/api854-20261003/beam-final-recipe-return-v1/received-aom/joint-lang-acceptance.template.json')['beam_runtime_source_sha256']}
    for c in CASES:expected(c)
    with (OUT/'cases.tsv').open('x',encoding='utf-8',newline='\n') as f:
        for c in CASES:
            ref=expected(c)['encoder_encoded_inputs'] or [None,None]
            fields=[c['case'],c['method'],encoded(c['buffer']),str(c['index']),str(ord(c['char'])),encoded(c['needle']),
                    encoded(c['s1']),encoded(c['s2']),json.dumps(c['expected_value']),c['expected_exception_class'] or '-',
                    encoded(ref[0]),encoded(ref[1])]
            f.write('\t'.join(fields)+'\n')
    roots='/home/team/sqa-round2/beam-buffer-worktrees';d4j=Path('/home/team/sqa-round2/defects4j')
    child='from scripts.study.api854.common import cpu_slot\nimport sys\ntry:\n with cpu_slot(sys.argv[1]):pass\nexcept RuntimeError:sys.exit(9)\n'
    with cpu_slot(roots):
        held,_=execute([sys.executable,'-B','-c',child,roots],'held-cpu-lock',check=False);require(held.returncode==9,'CPU lock not held')
        java,_=execute(['java','-version'],'java-version');execute(['javac','-version'],'javac-version')
        require('11.' in java.stderr.decode(),'Native Java11 required')
        with (d4j/'framework/projects/Codec/commit-db').open() as f:
            commit=next(r for r in csv.reader(f) if r[0]=='1')
        revisions={'fixed':commit[2],'buggy':commit[1]}
        with tempfile.TemporaryDirectory(dir=ROOT/'output',prefix='.beam-codec-candidate-') as temporary:
            temp=Path(temporary).resolve();require(temp.is_relative_to((ROOT/'output').resolve()),'Build escaped workspace')
            folders={};inventories={};archives={};originals={};classes={};stages={};class_hashes={}
            for version,rev in revisions.items():
                folder=temp/version;folder.mkdir();folders[version]=folder
                exported,record=execute(['git','-C',d4j/'project_repos/commons-codec.git','archive',rev,'src/java'],
                                        version+'-production-archive',binary=True)
                archives[version]=record
                with tarfile.open(fileobj=io.BytesIO(exported.stdout)) as tar:
                    for m in tar.getmembers():
                        if not m.isfile():continue
                        path=(folder/m.name).resolve();require(path.is_relative_to(folder),'Unsafe source archive')
                        path.parent.mkdir(parents=True,exist_ok=True);path.write_bytes(tar.extractfile(m).read())
                if version=='fixed':
                    for name in ['Metaphone.java','SoundexUtils.java']:
                        retained=BASE/'received-aom/output/api854-20261003/prepare-v10-joint-development/Codec-1/fixed-source'/ (SOURCE_PREFIX+name)
                        path=folder/(SOURCE_PREFIX+name)
                        require(path.read_bytes().replace(b'\r\n',b'\n')==retained.read_bytes().replace(b'\r\n',b'\n'),
                                'Fixed source differs from retained v10 '+name)
                        path.write_bytes(retained.read_bytes())
                inventories[version]={p.relative_to(folder).as_posix():sha(p) for p in sorted((folder/'src/java').rglob('*.java'))}
                classes[version]=folder/'classes';classes[version].mkdir()
            suite={p.name:sha(p) for p in [BASE/'policy.json',BASE/'CodecCandidateProbe.java',BASE/'CodecEntryTrace.java',Path(__file__)]}
            write(OUT/'preexecution-seal.json',{'sealed_at_utc':datetime.now(timezone.utc).isoformat(),
               'policy_id':POLICY['policy_id'],'suite_sha256':suite,'cases_tsv_sha256':sha(OUT/'cases.tsv'),
               'runtime_source_sha256':before,'input_provenance':read(BASE/'received-provenance.json'),
               'revisions':revisions,'source_sha256':inventories,'production_archives':archives,
               'worker_id':'beam-pc1','cpu_slots':1,'selection_used_buggy_outcomes':False,'primary':False})
            for version in revisions:
                folder=folders[version];cp=classes[version]
                execute(['javac','--release','8','-g','-encoding','UTF-8','-sourcepath',folder/'src/java','-d',cp,
                         folder/(SOURCE_PREFIX+'Metaphone.java'),folder/(SOURCE_PREFIX+'SoundexUtils.java'),
                         BASE/'CodecCandidateProbe.java'],version+'-compile')
                if version=='fixed':execute(['javac','--add-modules','jdk.jdi','-d',cp,BASE/'CodecEntryTrace.java'],'compile-trace')
                class_hashes[version]={owner:sha(cp/(owner.replace('.','/')+'.class')) for owner in OWNERS}
                for repeat in ('first','second'):
                    stage=version+'_'+repeat;run,record=execute(['java',*JVM,'-cp',cp,'sqa.development.CodecCandidateProbe',OUT/'cases.tsv'],stage,check=False)
                    rows=validate(records(run.stdout),allow_failures=version=='buggy')
                    require(run.returncode==int(any(not r['target_check_passed'] for r in rows)),'Stage return code differs')
                    record['cases']=rows;stages[stage]=record
                require(stages[version+'_first']['cases']==stages[version+'_second']['cases'],'Repeats differ')
            for version in revisions:
                stage=version+'_method_entry';run,record=execute(['java','--add-modules','jdk.jdi','-cp',classes['fixed'],
                      'CodecEntryTrace',classes[version],OUT/'cases.tsv'],stage,check=False)
                rows=validate(records(run.stdout),allow_failures=version=='buggy')
                require(rows==stages[version+'_first']['cases'],'Trace changed observations')
                require(run.returncode==stages[version+'_first']['exit_code'],'Trace status changed')
                record['cases']=rows;record['exact_entries']=validate_trace(records(run.stdout),run.returncode);stages[stage]=record
                require(class_hashes[version]=={owner:sha(classes[version]/(owner.replace('.','/')+'.class')) for owner in OWNERS},
                        'Tracing changed original bytecode')
            mutations={}
            for label,name,old,new in [
                ('next_char','Metaphone.java','matches = string.charAt(index + 1) == c;','matches = string.charAt(index + 1) != c;'),
                ('difference_score','SoundexUtils.java','return differenceEncoded(encoder.encode(s1), encoder.encode(s2));',
                 'return differenceEncoded(encoder.encode(s1), encoder.encode(s2)) + 1;')]:
                path=folders['fixed']/(SOURCE_PREFIX+name);original=path.read_bytes();text=original.decode('utf-8')
                require(text.count(old)==1,'Unexpected mutation source');path.write_text(text.replace(old,new),encoding='utf-8')
                (OUT/(label+'-mutated.java')).write_bytes(path.read_bytes())
                execute(['javac','--release','8','-g','-encoding','UTF-8','-cp',classes['fixed'],'-d',classes['fixed'],path],label+'-compile')
                run,record=execute(['java',*JVM,'-cp',classes['fixed'],'sqa.development.CodecCandidateProbe',OUT/'cases.tsv'],label+'-mutation',check=False)
                rows=validate(records(run.stdout),allow_failures=True)
                failed=[r['case'] for r in rows if not r['target_check_passed']]
                require(run.returncode==1 and failed,'Oracle missed mutation')
                expected_failed=([c['case'] for c in CASES if c['method']=='isNextChar' and c['index'] in {0,1} and c['buffer']=='ABCA']
                                 if label=='next_char' else [c['case'] for c in CASES if c['method']=='difference'])
                require(failed==expected_failed,'Unexpected mutation/fixture contamination')
                record['failed_cases']=failed;mutations[label]=record
                path.write_bytes(original)
                execute(['javac','--release','8','-g','-encoding','UTF-8','-cp',classes['fixed'],'-d',classes['fixed'],path],label+'-restore-compile')
            require(before=={p:sha(ROOT/p) for p in before},'Shared runtime changed')
            require(suite=={p.name:sha(p) for p in [BASE/'policy.json',BASE/'CodecCandidateProbe.java',BASE/'CodecEntryTrace.java',Path(__file__)]},
                    'Sealed suite changed')
    reused,_=execute([sys.executable,'-B','-c',child,roots],'released-cpu-lock');require(reused.returncode==0,'CPU lock not reusable')
    failed=[r['case'] for r in stages['buggy_first']['cases'] if not r['target_check_passed']]
    result={'status':'pass','scope':'Standalone bounded Codec-1 reflective helper development; not full Defects4J or shared integration',
      'policy_id':POLICY['policy_id'],'revisions':revisions,'stages':stages,'mutations':mutations,
      'source_sha256':inventories,'production_class_sha256':class_hashes,'suite_sha256':suite,
      'cases_tsv_sha256':sha(OUT/'cases.tsv'),'preexecution_seal_sha256':sha(OUT/'preexecution-seal.json'),
      'unique_cases':43,'declarations':5,'fixed_observations':86,'buggy_observations':86,
      'candidate_fault_detected':bool(failed),'buggy_failed_cases':failed,'method_entry_only':True,
      'line_or_branch_coverage_percentage':None,'oracle_mutations_detected':True,
      'worker_id':'beam-pc1','cpu_slots':1,'cpu_lock_exits':[held.returncode,reused.returncode],
      'java_version':java.stderr.decode().strip(),'platform':platform.platform(),
      'runtime_source_sha256':before,'shared_integration_approved':False,'full_legal_domain_approval':False,
      'actual_shared_selected':390,'actual_shared_exclusions':301,'primary_added':0,'new_defects4j_evaluations':0,
      'kku_requests':0,'queue_mutations':0,'gate_a_approved':False,'final_reserve':None}
    write(OUT/'receipt.json',result);return result
if __name__=='__main__':
    try:
        result=main();print(json.dumps({k:result[k] for k in ['status','unique_cases','declarations','candidate_fault_detected','cpu_lock_exits']}))
    except BaseException as error:
        if OUT.exists() and not (OUT/'checksums.json').exists():write(OUT/'failure.json',{'status':'fail','reason':str(error),'primary':False})
        raise
    finally:
        if OUT.exists() and not (OUT/'checksums.json').exists():
            hashes={p.relative_to(OUT).as_posix():sha(p) for p in sorted(OUT.rglob('*')) if p.is_file()}
            write(OUT/'checksums.json',hashes)
