"""Activate a captured unchanged-prompt retry without overwriting any history."""
from pathlib import Path
import argparse
import hashlib
import json
import shutil

ROOT = Path(__file__).resolve().parents[2]

def sha(path):
    return hashlib.sha256(path.read_bytes()).hexdigest()

def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--project',required=True)
    parser.add_argument('--seed',type=int,required=True)
    parser.add_argument('--tool',choices=('claude','intellisphere'),required=True)
    parser.add_argument('--fresh',type=Path,required=True)
    parser.add_argument('--history-tag',required=True)
    args = parser.parse_args()
    batch = ROOT/'results/study/round2-v4-20260929'
    fresh = args.fresh.resolve()
    capture = ROOT/f'ai-tests/provider-captures/{args.tool}/{args.project}-1/s{args.seed}-i1'
    run = batch/args.project/f'{args.tool}-s{args.seed}-b30'
    history = ROOT/f'results/validation/original-prompt-retries/{batch.name}/{args.project}/{args.tool}-s{args.seed}/{args.history_tag}'
    for path in (fresh,capture,run,history):
        if not path.resolve().is_relative_to(ROOT):
            raise ValueError('Path escapes repository')
    if history.exists() or fresh == capture or fresh.is_relative_to(capture):
        raise ValueError('History/capture overlap')
    old = json.loads((run/'evaluation/record.json').read_text())
    if old['status'] not in ('failed','generation_failed','invalid','partial'):
        raise ValueError('Only incomplete primary runs may be replaced')
    metadata = json.loads((fresh/'operator-metadata.json').read_text())
    if metadata.get('prompt_iteration',1)!=1 or not metadata.get('model'):
        raise ValueError('Original prompt and named model required')
    if not (fresh/'response.md').read_text().strip():
        raise ValueError('No response')
    ledger = {'prompt_sha256':sha(batch/args.project/'ai-context/prompt.md'),
        'old_response_sha256':sha(capture/'response.md'),
        'old_record_sha256':sha(run/'evaluation/record.json'),
        'new_response_sha256':sha(fresh/'response.md'),
        'activation_driver_sha256':sha(Path(__file__)),
        'old_capture_hashes':{p.relative_to(capture).as_posix():sha(p) for p in capture.rglob('*') if p.is_file()},
        'scope':'Same original prompt; previous primary and capture preserved; retry is not an independent extra run.'}
    history.mkdir(parents=True)
    shutil.move(str(capture),str(history/'capture'))
    shutil.move(str(run),str(history/'run'))
    shutil.copytree(fresh,capture)
    (history/'lineage.json').write_text(json.dumps(ledger,indent=2))
    print(history.relative_to(ROOT))

if __name__ == '__main__':
    main()
