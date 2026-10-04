"""Finite local compatibility repair cohort, with per-request command logs."""
from concurrent.futures import ThreadPoolExecutor
from pathlib import Path
import subprocess
import sys
import json

ROOT = Path(__file__).resolve().parents[2]


def execute(item):
    project, tool, seed = item
    capture = ROOT / f'ai-tests/provider-captures/{tool}/{project}-1/s{seed}-i1'
    command = [sys.executable,str(ROOT/'scripts/study/evaluate_provider_normalized_v8.py'),
        '--project',project,'--tool',tool,'--seed',str(seed),'--capture',str(capture)]
    if (capture/'source-processing-v8').exists():
        return {'project':project,'tool':tool,'seed':seed,'skipped':'Already processed v8'}
    result = subprocess.run(command,cwd=ROOT,text=True,capture_output=True)
    (capture/'compatibility-v8-driver.log').write_text(result.stdout+result.stderr)
    (capture/'compatibility-v8-command.json').write_text(json.dumps({'command':command,'returncode':result.returncode},indent=2))
    return {'project':project,'tool':tool,'seed':seed,'returncode':result.returncode,'output':result.stdout[-1200:]}


if __name__ == '__main__':
    policy = json.loads((ROOT/'results/study/round2-v4-20260929/ai-processing-policy-v8.json').read_text())
    items = [(p,t,int(s)) for p,t,s in (x.split('/') for x in policy['planned_runs'])]
    with ThreadPoolExecutor(max_workers=3) as pool:
        for outcome in pool.map(execute, items):
            print(json.dumps(outcome),flush=True)
