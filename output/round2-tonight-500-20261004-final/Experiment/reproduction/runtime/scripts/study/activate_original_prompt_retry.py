"""Activate a completed original-prompt retry while preserving a failed run."""
from pathlib import Path
import argparse
import hashlib
import json
import shutil

ROOT=Path(__file__).resolve().parents[2]

def sha(path):
    return hashlib.sha256(path.read_bytes()).hexdigest()

def main():
    p=argparse.ArgumentParser(description=__doc__)
    p.add_argument('--project',required=True)
    p.add_argument('--seed',type=int,required=True)
    a=p.parse_args()
    batch=ROOT/'results/study/round2-v4-20260929'
    history=ROOT/f'results/validation/kku-original-prompt-retries/{batch.name}/{a.project}/s{a.seed}'
    fresh=history/'new-capture'
    capture=ROOT/f'ai-tests/provider-captures/intellisphere/{a.project}-1/s{a.seed}-i1'
    run=batch/a.project/f'intellisphere-s{a.seed}-b30'
    for path in (fresh,capture,run,history):
        if not path.resolve().is_relative_to(ROOT):raise ValueError('Path escapes repository')
    record=run/'evaluation/record.json'
    old=json.loads(record.read_text(encoding='utf-8'))
    if old['status'] not in ('failed','generation_failed','invalid'):
        raise ValueError('Only a preserved incomplete primary run may be replaced')
    meta=json.loads((fresh/'operator-metadata.json').read_text(encoding='utf-8'))
    if meta.get('prompt_iteration',1)!=1 or not (fresh/'response.md').read_text(encoding='utf-8').strip():
        raise ValueError('Complete original-prompt evidence required')
    if (history/'original-capture').exists() or (history/'original-run').exists():
        raise ValueError('Prior retry history must not be overwritten')
    ledger={'reason':'Fresh response to unchanged original prompt after failed local evaluation; no logs or repair prompt sent.',
        'prompt_sha256':sha(batch/a.project/'ai-context/prompt.md'),
        'original_response_sha256':sha(capture/'response.md'),
        'original_record_sha256':sha(record),'new_response_sha256':sha(fresh/'response.md'),
        'original_capture_file_hashes':{x.relative_to(capture).as_posix():sha(x) for x in capture.rglob('*') if x.is_file()}}
    shutil.move(str(capture),str(history/'original-capture'))
    shutil.move(str(run),str(history/'original-run'))
    shutil.move(str(fresh),str(capture))
    (history/'retry-lineage.json').write_text(json.dumps(ledger,indent=2),encoding='utf-8')
    print(history)

if __name__=='__main__':main()
