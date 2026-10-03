"""Run immutable Champ Graphics tests with no provider or queue transport."""
from pathlib import Path
import hashlib,json,subprocess,sys
BASE=Path(__file__).resolve().parent
ROOT=BASE.parents[2]
SNAP=ROOT/'.local/api854/beam-graphics-snapshot-8d9295e6'
command=[sys.executable,'-B','-X','utf8','-m','unittest','-v',
         'scripts.study.api854.tests.test_graphics_development']
result=subprocess.run(command,cwd=SNAP,capture_output=True)
for suffix,raw in [('stdout.log',result.stdout),('stderr.log',result.stderr)]:
    with (BASE/('received-tests.'+suffix)).open('xb') as f:f.write(raw)
with (BASE/'received-tests.command.json').open('x',encoding='utf-8') as f:
    json.dump({'argv':command,'exit_code':result.returncode,'cwd':str(SNAP),
               'stdout_sha256':hashlib.sha256(result.stdout).hexdigest(),
               'stderr_sha256':hashlib.sha256(result.stderr).hexdigest()},f,indent=2)
print(result.stderr.decode('utf-8',errors='replace'))
sys.exit(result.returncode)
