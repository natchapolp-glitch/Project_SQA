"""Real Beam v10 host/fixed-helper review, without provider or queue operations."""
from pathlib import Path
from datetime import datetime,timezone
import gzip
import hashlib
import json
import subprocess
import sys

BASE=Path(__file__).resolve().parent;BEAM_ROOT=BASE.parents[2]
SNAPSHOT=BEAM_ROOT/'.local/api854/beam-v10-snapshot-a4880fb2'
sys.path[:0]=[str(SNAPSHOT),str(SNAPSHOT/'scripts/study')]
from scripts.study.api854 import verify_v10_recipe_runtime as verifier
from scripts.study.api854 import joint_recipe_v10 as intake
from scripts.study.api854.common import cpu_slot,implementation_hashes
from scripts.study.api854.environment import inspect_environment

OUT=BASE/'host-review';OUT.mkdir(exist_ok=False)
def sha(path):return hashlib.sha256(path.read_bytes()).hexdigest()
def read(path):return json.loads(path.read_text(encoding='utf-8'))
def write(name,data):
    with (OUT/name).open('x',encoding='utf-8',newline='\n') as stream:
        json.dump(data,stream,indent=2);stream.write('\n')

references=read(BASE/'reference-provenance.json')
def pinned_bytes(commit,path):
    row=next(item for item in references if item['source_commit']==commit and item['source_path']==path)
    location=BEAM_ROOT/row['received_path'];assert sha(location)==row['sha256'];return location.read_bytes()
intake.git_bytes=pinned_bytes
intake.git_json=lambda commit,path:json.loads(pinned_bytes(commit,path))
# Windows managed-worktree .git paths are unavailable to Linux git. Only these
# three read-only peer-reference lookups use byte-exact pre-exported Git blobs.
# Defects4J production mirror git calls remain real and unchanged.

def retained_run(command,**kwargs):
    index=len(verifier.COMMANDS)
    result=subprocess.run(command,capture_output=True,timeout=90,**kwargs)
    prefix=OUT/'commands'/f'{index:03d}';prefix.parent.mkdir(exist_ok=True)
    if command[0]=='git' and 'archive' in command:
        path=prefix.with_suffix('.stdout.tar.gz');path.write_bytes(gzip.compress(result.stdout,mtime=0))
    else:
        path=prefix.with_suffix('.stdout.log');path.write_bytes(result.stdout)
    stderr=prefix.with_suffix('.stderr.log');stderr.write_bytes(result.stderr)
    original={'argv':list(map(str,command)),'exit_code':result.returncode,
              'stdout_sha256':hashlib.sha256(result.stdout).hexdigest(),
              'stderr_sha256':hashlib.sha256(result.stderr).hexdigest()}
    verifier.COMMANDS.append(original)
    record={**original,'retained_stdout':path.relative_to(OUT).as_posix(),
            'retained_stdout_sha256':sha(path),'retained_stderr':stderr.relative_to(OUT).as_posix()}
    prefix.with_suffix('.command.json').write_bytes((json.dumps(record,indent=2)+'\n').encode())
    if result.returncode:raise ValueError(result.stderr.decode(errors='replace'))
    return result.stdout
verifier.run=retained_run

completion=read(SNAPSHOT/'output/api854-20261003/aom-v10-readiness-v1/completion-receipt.json')
before=implementation_hashes();assert before==completion['runtime_source_sha256']
seal={'sealed_at_utc':datetime.now(timezone.utc).isoformat(),'source_commit':read(BASE/'snapshot-provenance.json')['source_commit'],
    'condition':completion['condition'],'protocol_sha256':completion['protocol_sha256'],
    'runner_sha256':completion['runner_plan_sha256'],'index_sha256':completion['preparation_index_sha256'],
    'runtime_source_sha256':before,'verifier_sha256':sha(SNAPSHOT/'scripts/study/api854/verify_v10_recipe_runtime.py'),
    'wrapper_sha256':sha(Path(__file__)),'peer_reference_bindings':references,
    'declared_fixed_case_count':64,'cpu_slots':1,'primary':False,'gate_a_approved':False}
write('preexecution-seal.json',seal)
trees='/home/team/sqa-round2/beam-buffer-worktrees'
child='from scripts.study.api854.common import cpu_slot\nimport sys\ntry:\n with cpu_slot(sys.argv[1]):pass\nexcept RuntimeError:sys.exit(9)\n'
with cpu_slot(trees):
    environment=inspect_environment('/home/team/sqa-round2/defects4j/framework/bin/defects4j',OUT/'environment')
    if not environment['ready']:raise ValueError('Beam environment prerequisites failed')
    challenge=subprocess.run([sys.executable,'-B','-c',child,trees],cwd=SNAPSHOT,capture_output=True)
    assert challenge.returncode==9
    proof=verifier.verify('/home/team/sqa-round2/defects4j')
    assert proof['status']=='passed' and proof['fixed_source_integration_cases']==64
    assert implementation_hashes()==before
reuse=subprocess.run([sys.executable,'-B','-c',child,trees],cwd=SNAPSHOT,capture_output=True)
assert reuse.returncode==0
write('fixed-runtime-verification.json',proof)
write('host-receipt.json',{'status':'v10_scoped_technical_host_review_passed','worker_id':'beam-pc1','cpu_slots':1,
    'condition':completion['condition'],'protocol_sha256':completion['protocol_sha256'],
    'runner_sha256':completion['runner_plan_sha256'],'preparation_index_sha256':completion['preparation_index_sha256'],
    'environment_ready':True,'environment_sha256':sha(OUT/'environment/environment.json'),
    'runtime_source_sha256':before,'shared_worktrees_root':trees,
    'cross_process_cpu_lock':{'held_slot_rejected_exit':challenge.returncode,'released_slot_reusable_exit':reuse.returncode},
    'fixed_cases_verified':64,'repeated_fixed_observations':128,
    'fixed_runtime_proof_sha256':sha(OUT/'fixed-runtime-verification.json'),
    'final_primary_protocol_approval':False,'all_390_semantic_approval':False,'gate_a_approved':False,
    'live_kku_requests':0,'queue_mutations':0,'primary_results_added':0,'new_defects4j_evaluations':0})
hashes={path.relative_to(OUT).as_posix():sha(path) for path in sorted(OUT.rglob('*')) if path.is_file()}
write('checksums.json',hashes)
print(json.dumps({'status':'pass','fixed_cases_verified':64,'repeated_fixed_observations':128,
                  'cross_process_cpu_lock':[challenge.returncode,reuse.returncode],'environment_ready':True}))
