"""Repeat unchanged archive after an OS permission failure, preserving evidence."""
from pathlib import Path
import fcntl
import hashlib
import json
import shutil
from evaluate import EvaluationConfig, evaluate_run, write_record

ROOT = Path(__file__).resolve().parents[2]
BATCH = ROOT / 'results/study/round2-v4-20260929'
RUN = BATCH / 'Chart/claude-s102-b30'
HISTORY = ROOT / 'results/validation/os-permission-retries' / BATCH.name / 'Chart/claude-s102-b30'

def main():
    path = RUN / 'evaluation/record.json'
    old = json.loads(path.read_text())
    if old['status'] != 'failed' or 'PermissionError' not in old.get('error', ''):
        raise ValueError('Expected the recorded OS permission failure')
    if HISTORY.exists():
        raise ValueError('Retry history exists; do not overwrite')
    metadata = json.loads((RUN / 'generation/metadata.json').read_text())
    suite = RUN / 'generation' / metadata['external_suite']
    suite_hash = hashlib.sha256(suite.read_bytes()).hexdigest()
    if suite_hash != old['suite_sha256']:
        raise ValueError('Archive changed')
    HISTORY.parent.mkdir(parents=True, exist_ok=True)
    shutil.copytree(RUN, HISTORY)
    shutil.rmtree(RUN / 'evaluation')
    with (BATCH / 'Chart/ai-evaluator.lock').open('a') as lock:
        fcntl.flock(lock, fcntl.LOCK_EX)
        new = evaluate_run(EvaluationConfig(project='Chart', bug_id=1, generator='claude',
            seed=102, budget=30, suite=suite, fixed_worktree=Path('/home/aomsin/sqa-round2/worktrees/Chart/1/f'),
            buggy_worktree=Path('/home/aomsin/sqa-round2/worktrees/Chart/1/b'), output=RUN/'evaluation',
            classes_file=BATCH/'Chart/ai-context/classes.txt',
            d4j='/home/aomsin/sqa-round2/defects4j/framework/bin/defects4j',
            generation_seconds=old['generation_seconds'], test_count=old['test_count'], timeout_seconds=900))
    for key, value in old.items():
        if key.startswith('ai_') or key in ('source_sha256','generation_time_scope','test_count_source'):
            new[key] = value
    new['run_id'] = old['run_id']
    new['workflow_seconds'] = new.get('total_seconds',0) + old['workflow_seconds'] - old['generation_seconds']
    new['os_failure_retry_history'] = str((HISTORY/'evaluation/record.json').relative_to(ROOT))
    new['os_failure_retry_scope'] = 'Same archive, tests, assertions and frozen evaluator; fresh fixed twice, buggy and coverage.'
    write_record(path,new)
    print(json.dumps({'status':new['status'],'test_count':new['test_count'],'fault_detected':new['fault_detected']}))

if __name__ == '__main__':
    main()
