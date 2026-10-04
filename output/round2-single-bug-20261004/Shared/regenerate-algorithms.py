"""Regenerate algorithm suites with the packaged frozen runtime and fixed Csv source."""
import argparse
from datetime import datetime,timezone
import hashlib
import json
import os
from pathlib import Path
import subprocess
import sys

def digest(p):return hashlib.sha256(Path(p).read_bytes()).hexdigest()

def main():
    p=argparse.ArgumentParser();p.add_argument('--d4j',required=True);p.add_argument('--worktrees',required=True);p.add_argument('--output',required=True)
    a=p.parse_args()
    if sys.platform!='linux':raise RuntimeError('Use Linux/WSL and Java11')
    bundle=Path(__file__).resolve().parents[1];data=json.loads((bundle/'Experiment/results.json').read_text())
    runtime=bundle/'Shared/frozen-runtime'
    for name,h in data['runtime_source_hashes'].items():
        if digest(runtime/name)!=h:raise RuntimeError('Frozen runtime differs')
    sys.path.insert(0,str(runtime/'scripts/study'))
    from generate import generate_suite
    from evaluate import run_command,validate_worktree
    from api854.common import cpu_slot
    out=Path(a.output).resolve();out.mkdir(parents=True,exist_ok=False)
    roots=Path(a.worktrees).resolve();tree=roots/('csv-regenerate-'+datetime.now(timezone.utc).strftime('%Y%m%dT%H%M%S%f'))/'f'
    os.environ['TZ']='America/Los_Angeles'
    version=subprocess.run(['java','-version'],capture_output=True,text=True,check=True).stderr
    if 'version "11.' not in version:raise RuntimeError('Java11 required')
    with cpu_slot(roots):
        tree.parent.mkdir(parents=True,exist_ok=False)
        r=run_command([a.d4j,'checkout','-p','Csv','-v','1f','-w',str(tree)],bundle,out/'checkout',300)
        if r['exit_code']!=0:raise RuntimeError('Checkout failed')
        validate_worktree(tree,'Csv','1f')
        for name,h in data['fixed_source_hashes'].items():
            if digest(tree/name)!=h:raise RuntimeError('Fixed source differs')
        r=run_command([a.d4j,'compile','-w',str(tree)],bundle,out/'compile',300)
        if r['exit_code']!=0:raise RuntimeError('Compile failed')
        binclasses=subprocess.run([a.d4j,'export','-p','dir.bin.classes','-w',str(tree)],capture_output=True,text=True,check=True).stdout.strip()
        helper=out/'helper';helper.mkdir()
        r=run_command(['javac','--release','8','-d',str(helper),str(runtime/'algorithms/java/SqaProbe.java')],bundle,out/'compile-helper',60)
        if r['exit_code']!=0:raise RuntimeError('Helper compile failed')
        targets=json.loads((bundle/'Shared/inputs/algorithm-targets.json').read_text())['targets']
        results=[]
        for method in ['cmaes','fscs-art']:
            result=generate_suite('Csv',1,method,30,101,targets,os.pathsep.join([str(tree/binclasses),str(helper)]),out/method,10,
                fixture_policy='aom-beam-champ-graphics-fixtures-v12-development')
            results.append(result);print(json.dumps({'approach':method,'test_count':result['test_count']}),flush=True)
        (out/'receipt.json').write_text(json.dumps({'scope':'New regeneration replay; does not replace reported suites','results':results,'api_requests':0},indent=2)+'\n')

if __name__=='__main__':main()
