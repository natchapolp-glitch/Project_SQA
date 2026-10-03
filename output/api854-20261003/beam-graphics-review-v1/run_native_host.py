"""Run exact Champ assertions on Beam Linux/Java 11 using a path/launch adapter.

Only Windows-specific WSL/path launching is adapted; checkpoint, pixels, state,
source seals, method-entry tracing and mutation assertions stay unchanged.
"""
from pathlib import Path
from datetime import datetime,timezone
import hashlib,json,shutil,subprocess,sys
BASE=Path(__file__).resolve().parent;ROOT=BASE.parents[2]
SNAP=ROOT/'.local/api854/beam-graphics-snapshot-8d9295e6'
sys.path[:0]=[str(SNAP),str(SNAP/'scripts/study')]
from scripts.study.api854 import verify_graphics_development as verifier
from scripts.study.api854.common import cpu_slot,implementation_hashes
OUT=BASE/'host-review-v1';OUT.mkdir(exist_ok=False)
NATIVE=SNAP/'output/api854-20261003/beam-graphics-native-v1'
assert not NATIVE.exists()
def sha(path):return hashlib.sha256(path.read_bytes()).hexdigest()
def read(path):return json.loads(path.read_text(encoding='utf-8'))
def write(name,data):
    with (OUT/name).open('x',encoding='utf-8',newline='\n') as f:json.dump(data,f,indent=2);f.write('\n')
before=implementation_hashes()
write('adapter-preexecution-seal.json',{'sealed_at_utc':datetime.now(timezone.utc).isoformat(),
 'champ_commit':read(BASE/'snapshot-provenance.json')['source_commit'],
 'worker_id':'beam-pc1','cpu_slots':1,'wrapper_sha256':sha(Path(__file__)),
 'canonical_verifier_sha256':sha(Path(verifier.__file__)),
 'policy_sha256':sha(verifier.TOOLS/'policy.json'),'runtime_source_sha256':before,
 'adapter':'linux_path returns resolved native path; execute strips only wsl --exec launcher',
 'primary':False,'gate_a':False})
original_execute=verifier.execute
def native_execute(command,packet,name,check=True):
    command=list(map(str,command))
    if command[:2]==['wsl','--exec']:command=command[2:]
    return original_execute(command,packet,name,check=check)
verifier.linux_path=lambda path:str(Path(path).resolve())
verifier.execute=native_execute
trees='/home/team/sqa-round2/beam-buffer-worktrees'
child='from scripts.study.api854.common import cpu_slot\nimport sys\ntry:\n with cpu_slot(sys.argv[1]):pass\nexcept RuntimeError:sys.exit(9)\n'
try:
    with cpu_slot(trees):
        held=subprocess.run([sys.executable,'-B','-c',child,trees],cwd=SNAP,capture_output=True)
        assert held.returncode==9
        proof=verifier.verify('/home/team/sqa-round2/defects4j',NATIVE)
        assert proof['status']=='pass' and proof['cases_per_revision']==24
        assert proof['exact_declarations_entered_per_revision']==7
        assert not proof['candidate_fault_detected']
        assert before==implementation_hashes()
    reused=subprocess.run([sys.executable,'-B','-c',child,trees],cwd=SNAP,capture_output=True)
    assert reused.returncode==0
    shutil.copytree(NATIVE,OUT/'native-evidence')
    peer=read(SNAP/'output/api854-20261003/graphics-development-v3/receipt.json')
    same={}
    for stage in ['fixed_first','fixed_second','buggy_first','buggy_second',
                  'fixed_method_entry_trace','buggy_method_entry_trace']:
        same[stage]=proof['stages'][stage]['cases']==peer['stages'][stage]['cases']
    write('host-receipt.json',{'status':'bounded_native_graphics_review_passed','worker_id':'beam-pc1',
     'cpu_slots':1,'cpu_lock_exits':[held.returncode,reused.returncode],
     'shared_worktrees_root':trees,'canonical_native_receipt_sha256':sha(NATIVE/'receipt.json'),
     'java_version':proof['java_version'],'python':proof['python'],'platform':proof['platform'],
     'peer_case_observations_equal_by_stage':same,'peer_raster_equivalence_claimed':all(same.values()),
     'cases_per_revision':24,'exact_declarations_entered_per_revision':7,
     'candidate_fault_detected':False,'oracle_shifted_line_detected':True,
     'shared_integration_approved':False,'primary_results_added':0,'live_requests':0,
     'queue_mutations':0,'gate_a_approved':False})
except BaseException as error:
    if NATIVE.exists():
        if not (NATIVE/'checksums.json').exists():
            verifier.write_json(NATIVE/'failure.json',{'status':'fail','reason':str(error),'primary':False})
            hashes={p.relative_to(NATIVE).as_posix():sha(p) for p in sorted(NATIVE.rglob('*')) if p.is_file()}
            verifier.write_json(NATIVE/'checksums.json',hashes)
        shutil.copytree(NATIVE,OUT/'native-evidence')
    write('failure.json',{'status':'fail','reason':str(error),'primary':False})
    raise
finally:
    hashes={p.relative_to(OUT).as_posix():sha(p) for p in sorted(OUT.rglob('*')) if p.is_file()}
    write('checksums.json',hashes)
print(json.dumps({'status':'pass','cases_per_revision':24,'exact_declarations':7,
                  'cpu_lock_exits':[held.returncode,reused.returncode],'peer_equal_by_stage':same}))
