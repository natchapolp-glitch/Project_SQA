#!/usr/bin/env python3
"""Preserve a server-busy attempt before retrying the same original prompt."""
import argparse
import hashlib
import json
from pathlib import Path
import shutil

ROOT = Path(__file__).resolve().parents[2]
BATCH = ROOT / 'results/study/round2-v4-20260929'


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--project', required=True)
    parser.add_argument('--seed', type=int, required=True)
    args = parser.parse_args()
    capture = ROOT / f'ai-tests/provider-captures/intellisphere/{args.project}-1/s{args.seed}-i1'
    run = BATCH / args.project / f'intellisphere-s{args.seed}-b30'
    destination = ROOT / f'results/validation/ai-provider-service-retry/{BATCH.name}/{args.project}/s{args.seed}'
    for path in (capture,run,destination):
        if not path.resolve().is_relative_to(ROOT):
            parser.error('Path escapes repository')
    text = (capture / 'rendered-response.txt').read_text(encoding='utf-8')
    record = json.loads((run / 'evaluation/record.json').read_text())
    if 'The server is busy' not in text or record['status'] != 'generation_failed':
        parser.error('Only a documented server-busy generation failure may be archived here')
    if destination.exists():
        parser.error('A retry history already exists')
    destination.mkdir(parents=True)
    ledger = {'reason':'Provider server busy; retry sends the unchanged original prompt.',
        'original_capture':capture.relative_to(ROOT).as_posix(),
        'original_record':(run/'evaluation/record.json').relative_to(ROOT).as_posix(),
        'response_sha256':hashlib.sha256((capture/'response.md').read_bytes()).hexdigest(),
        'record_sha256':hashlib.sha256((run/'evaluation/record.json').read_bytes()).hexdigest()}
    shutil.move(str(capture),str(destination / 'original-capture'))
    shutil.move(str(run),str(destination / 'original-run'))
    (destination/'retry-lineage.json').write_text(json.dumps(ledger,indent=2),encoding='utf-8')
    print(destination)


if __name__ == '__main__':
    main()
