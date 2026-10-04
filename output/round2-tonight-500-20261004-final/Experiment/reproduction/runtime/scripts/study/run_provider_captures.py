#!/usr/bin/env python3
"""Evaluate ready original-prompt captures and preserve local processing failures."""
import argparse
from concurrent.futures import ThreadPoolExecutor
import hashlib
import json
from pathlib import Path
import re
import subprocess
import sys
import time

ROOT = Path(__file__).resolve().parents[2]
BATCH = ROOT / 'results/study/round2-v4-20260929'


def filename_hint_version(capture):
    response = (capture/'response.md').read_text(encoding='utf-8')
    version = 'v1'
    for code in re.findall(r'```java[^\n]*\n(.*?)\n```',response,re.S|re.I):
        cls = re.search(r'\bpublic\s+(?:final\s+)?class\s+([A-Za-z_$][\w$]*)',code)
        if not cls:
            continue
        package = re.search(r'^\s*package\s+([\w.]+)\s*;',code,re.M)
        relative = (package.group(1).replace('.','/')+'/' if package else '')+cls.group(1)+'.java'
        first = code.strip().splitlines()[0].strip()
        if first in ('src/test/java/'+relative,'src/test/'+relative,'test/'+relative,'tests/'+relative):
            return 'v3'
        if first in (relative,cls.group(1)+'.java'):
            version = 'v2'
    return version


def parser_diagnostics(capture, version='v1'):
    log = capture / f'normalization-diagnostics-{version}.log'
    if log.exists():
        return
    tests = capture / f'source-processing-{version}/tests'
    with log.open('w',encoding='utf-8') as output:
        for source in sorted(tests.rglob('*.java')):
            process = subprocess.run(['java','-cp',str(ROOT/'tmp/java-provider-normalizer'),
                'TestMethodPositions',str(source)],text=True,capture_output=True)
            output.write(str(source.relative_to(ROOT))+'\n'+process.stdout+process.stderr+'\n')


def preserve_failure(capture, operator, project, bug, tool, seed, error):
    run = BATCH / project / f'{tool}-s{seed}-b30'
    evaluation = run / 'evaluation'
    evaluation.mkdir(parents=True, exist_ok=True)
    record_path = evaluation / 'record.json'
    if record_path.exists():
        return
    frozen = json.loads((BATCH / 'config.json').read_text())['source_sha256']
    record = {'schema_version':1,'run_id':f'{BATCH.name}/{project}/{run.name}',
        'project':project,'bug_id':bug,'generator':tool,'seed':seed,'budget':30,
        'status':'generation_failed','test_count':None,'compile_status':'not_run',
        'fault_detected':None,'line_covered':None,'line_total':None,'branch_covered':None,
        'branch_total':None,'generation_seconds':operator['generation_seconds'],
        'duration_seconds':None,'total_seconds':None,'workflow_seconds':None,
        'failed_stage':'local_source_processing','error':error,
        'source_sha256':frozen,'ai_capture':capture.relative_to(ROOT).as_posix(),
        'response_sha256':hashlib.sha256((capture / 'response.md').read_bytes()).hexdigest(),
        'failure_scope':'Local source processor aborted; no coverage or fault outcome is inferred.',
        'artifact_path':str(evaluation)}
    record_path.write_text(json.dumps(record,indent=2),encoding='utf-8')


def evaluate_capture(metadata):
    capture = metadata.parent
    operator = json.loads(metadata.read_text(encoding='utf-8'))
    if operator.get('prompt_iteration', 1) != 1:
        return
    tool = capture.parent.parent.name
    project, bug_text = capture.parent.name.rsplit('-', 1)
    bug, seed = int(bug_text), int(capture.name.split('-')[0][1:])
    version = filename_hint_version(capture)
    record_path = BATCH / project / f'{tool}-s{seed}-b30/evaluation/record.json'
    if record_path.exists():
        record = json.loads(record_path.read_text())
        if record['status'] in ('running', 'complete'):
            return
        if record.get('ai_execution_driver', '').startswith('scripts/study/evaluate_provider_normalized') or record['status'] == 'generation_failed':
            if record['status'] == 'generation_failed':
                parser_diagnostics(capture)
            if not (version != 'v1' and record['status'] == 'generation_failed' and not (capture/f'source-processing-{version}').exists()):
                return
    if (capture / f'source-processing-{version}').exists():
        failure = 'Previous local processing aborted; preserved working source and parser diagnostics.'
        log = capture / 'normalization-recovery.log'
        tests = capture / f'source-processing-{version}/tests'
        with log.open('w',encoding='utf-8') as output:
            for source in tests.rglob('*.java'):
                process = subprocess.run(['java','-cp',str(ROOT/'tmp/java-provider-normalizer'),
                    'TestMethodPositions',str(source)],text=True,capture_output=True)
                output.write(process.stdout + process.stderr)
        preserve_failure(capture,operator,project,bug,tool,seed,failure)
        print(f'{tool} {project} s{seed}: generation_failed (local source processor)',flush=True)
        return
    started = time.monotonic()
    driver = 'evaluate_provider_normalized.py' if version == 'v1' else f'evaluate_provider_normalized_{version}.py'
    command = [sys.executable,str(ROOT/'scripts/study'/driver),
        '--tool',tool,'--project',project,'--seed',str(seed),'--capture',str(capture)]
    process = subprocess.run(command,cwd=ROOT,text=True,capture_output=True)
    log = capture / 'evaluation-driver.log'
    log.write_text(process.stdout + process.stderr,encoding='utf-8')
    (capture/'driver-command.json').write_text(json.dumps({'command':command,
        'exit_code':process.returncode,'driver_wall_seconds':time.monotonic()-started,
        'log':log.relative_to(ROOT).as_posix()},indent=2),encoding='utf-8')
    if not record_path.exists():
        parser_diagnostics(capture,version)
        preserve_failure(capture,operator,project,bug,tool,seed,
            'Local processor exited without final evidence record; see evaluation-driver.log.')
    final = json.loads(record_path.read_text())
    print(f"{tool} {project} s{seed}: {final['status']}; fault={final.get('fault_detected')}",flush=True)


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--workers", type=int, default=3)
    args = parser.parse_args()
    if not 1 <= args.workers <= 4:
        parser.error("workers must be between 1 and 4")
    captures = sorted((ROOT / "ai-tests/provider-captures").glob("*/*/*/operator-metadata.json"))
    with ThreadPoolExecutor(max_workers=args.workers) as executor:
        for result in executor.map(evaluate_capture, captures):
            pass


if __name__ == '__main__':
    main()
