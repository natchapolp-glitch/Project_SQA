"""Run six pinned shared-v9 fixture diagnostics on Beam's one-slot WSL host."""
from pathlib import Path
from datetime import datetime, timezone
import hashlib
import json
import subprocess
import sys
import tarfile
import tempfile

ROOT=Path.cwd();sys.path.insert(0,str(ROOT))
from scripts.study.api854.common import cpu_slot
BASE=ROOT/'output/api854-20261003/beam-champ7de-integration-review-v1'
PEER=BASE/'received-champ'
OUT=BASE/'shared-v9-diagnostic-v2'
SOURCE='src/main/java/org/joda/time/Partial.java'
def sha(path):return hashlib.sha256(path.read_bytes()).hexdigest()
def read(path):return json.loads(path.read_text(encoding='utf-8'))
def write(name,data):
    with (OUT/name).open('x',encoding='utf-8',newline='\n') as stream:
        json.dump(data,stream,indent=2);stream.write('\n')
def execute(name,command):
    result=subprocess.run(command,capture_output=True,timeout=120)
    (OUT/(name+'.stdout.log')).write_bytes(result.stdout)
    (OUT/(name+'.stderr.log')).write_bytes(result.stderr)
    record={'command':command,'exit_code':result.returncode,'stdout_sha256':sha(OUT/(name+'.stdout.log')),
            'stderr_sha256':sha(OUT/(name+'.stderr.log'))}
    write(name+'.command.json',record)
    if result.returncode:raise ValueError('Diagnostic command failed: '+name)
    return result

OUT.mkdir(exist_ok=False)
candidate=read(PEER/'output/api854-20261003/chronology-development-v2/receipt.json')
metadata=read(PEER/'output/api854-20261003/prepare-v9-twenty-bug-development/Time-1/prepare-metadata.json')
helper=PEER/'algorithms/java/SqaProbe.java'
jar=Path('/home/team/sqa-round2/defects4j/framework/projects/Time/lib/joda-convert-1.2.jar')
dependency=candidate['dependencies_sha256']['framework/projects/Time/lib/joda-convert-1.2.jar']
assert sha(jar)==dependency
archive=PEER/'output/api854-20261003/chronology-development-v2/fixed-production-source.tar.gz'
assert sha(archive)==candidate['source_archives']['fixed']['archive_sha256']
retained=PEER/'output/api854-20261003/prepare-v9-twenty-bug-development/Time-1/fixed-source'/SOURCE
assert sha(retained)==candidate['fixed_target_source_sha256']
declared={'sealed_at_utc':datetime.now(timezone.utc).isoformat(),'case_count':6,
    'policy_id':metadata['fixture_policy_id'],'worklist_identities':candidate['worklist_identities'],
    'vector':[0.5,0.5,0.5],'helper_sha256':sha(helper),'driver_sha256':sha(BASE/'SharedChronologyDiagnosticV2.java'),
    'producer_sha256':sha(Path(__file__)),'production_archive_sha256':sha(archive),'fixed_target_sha256':sha(retained),
    'dependency_sha256':dependency,'purpose':'Diagnose unchanged shared-v9 fixture support; never count fixture failures as bugs',
    'primary':False,'expected_current_support':False}
write('preexecution-seal.json',declared)
execute('java-version',['java','-version'])
with cpu_slot('/home/team/sqa-round2/beam-buffer-worktrees'), tempfile.TemporaryDirectory(prefix='beam-shared-chronology-') as temporary:
    working=Path(temporary)
    with tarfile.open(archive) as tar:
        for member in tar.getmembers():
            if member.isfile() and member.name.endswith('.java'):
                target=(working/member.name).resolve();assert target.is_relative_to(working.resolve())
                target.parent.mkdir(parents=True,exist_ok=True);target.write_bytes(tar.extractfile(member).read())
    assert (working/SOURCE).read_bytes().replace(b'\r\n',b'\n')==retained.read_bytes().replace(b'\r\n',b'\n')
    (working/SOURCE).write_bytes(retained.read_bytes())
    classes=working/'classes';classes.mkdir()
    execute('compile',['javac','--release','8','-g','-cp',str(jar),'-sourcepath',str(working/'src/main/java'),
                       '-d',str(classes),str(working/SOURCE),str(helper),str(BASE/'SharedChronologyDiagnosticV2.java')])
    command=['java','-Duser.timezone=UTC','-Dorg.joda.time.DateTimeZone.Provider=org.joda.time.tz.UTCProvider',
             '-cp',str(classes)+':'+str(jar),'SharedChronologyDiagnosticV2',metadata['fixture_policy_id']]
    first=execute('first',command);second=execute('second',command)
    assert first.stdout==second.stdout
    rows=[json.loads(line) for line in first.stdout.decode().splitlines()]
    assert len(rows)==6
    for index,row in enumerate(rows):
        identity=candidate['worklist_identities'][index]
        assert all(row[k]==identity[k] for k in ['constructor_types','method','parameter_types'])
        assert row['target_invoked'] is False and row['outcome'] is None
        assert row['error_class']=='SqaProbe$FixtureFailure'
        assert 'No explicit recipe:' in row['error_message']
    receipt={'status':'diagnostic_complete_shared_integration_blocked','fixed_diagnostic_attempts':12,
        'unique_declarations':6,'repeat_observations_equal':True,'target_invocations':0,'fixture_failures':12,
        'production_class_sha256':sha(classes/'org/joda/time/Partial.class'),'shared_helper_class_sha256':sha(classes/'SqaProbe.class'),
        'observations':rows,'preexecution_seal_sha256':sha(OUT/'preexecution-seal.json'),
        'jvm':'Java11 on Beam WSL Ubuntu; compiled --release 8','cpu_slots':1,
        'shared_worktrees_root':'/home/team/sqa-round2/beam-buffer-worktrees',
        'helper_sha256':sha(helper),'production_archive_sha256':sha(archive),'candidate_fault_counted':False,
        'full_defects4j_evaluation':False,'new_defects4j_runs':0,'primary_results_added':0,'real_kku_requests':0,
        'queue_mutations':0,'gate_a_approved':False,'final_condition_host_approval':False}
write('receipt.json',receipt)
hashes={p.name:sha(p) for p in sorted(OUT.iterdir()) if p.is_file()}
write('checksums.json',hashes)
print(json.dumps({k:receipt[k] for k in ['status','fixed_diagnostic_attempts','target_invocations','fixture_failures','candidate_fault_counted']}))
