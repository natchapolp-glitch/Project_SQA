from pathlib import Path
import subprocess,sys,tempfile,json
from scripts.study.api854.common import ROOT,cpu_slot,sha256,write_json,implementation_hashes
out=ROOT/'output/api854-20261003/aom-v8-waiting-work-v1'
protocol=ROOT/'output/api854-20261003/aom-continuation-v8-development/protocol.proposal.json'
runner=protocol.parent/'runner-plan.json'
child='''from pathlib import Path
from scripts.study.api854.common import cpu_slot
import sys
try:
    with cpu_slot(Path(sys.argv[1])):
        pass
except RuntimeError:
    sys.exit(9)
'''
with tempfile.TemporaryDirectory(prefix='aom-v8-cpu-lock-') as folder:
    with cpu_slot(folder):
        blocked=subprocess.run([sys.executable,'-c',child,folder],cwd=ROOT,capture_output=True,text=True)
    released=subprocess.run([sys.executable,'-c',child,folder],cwd=ROOT,capture_output=True,text=True)
    assert blocked.returncode==9 and released.returncode==0,(blocked.stderr,released.stderr)
shared=Path('/home/aomsin/sqa-round2/worktrees-isolated')
available=False
try:
    with cpu_slot(shared): available=True
except RuntimeError: pass
from scripts.study.api854.common import read_json
report={'worker_id':'aom-pc1','environment_ready':read_json(out/'environment/environment.json')['ready'],
'environment_sha256':sha256(out/'environment/environment.json'),'runtime_source_sha256':implementation_hashes(),
'protocol_sha256':sha256(protocol),'runner_plan_sha256':sha256(runner),'cpu_slots':1,
'cross_process_cpu_lock':{'second_worker_rejected_exit':blocked.returncode,'slot_reusable_after_release_exit':released.returncode},
'configured_shared_worktrees_root':str(shared),'shared_slot_available_at_check':available,
'host_preflight_passed':True,'host_team_approval':False,'gate_a_passed':False,'generation_authorized':False,
'provider_requests':0,'queue_mutations':0,'producer_sha256':sha256(__file__)}
write_json(out/'aom-host-receipt.json',report)
print(json.dumps({'environment_ready':report['environment_ready'],'lock':report['cross_process_cpu_lock'],'shared_slot_available':available}))
