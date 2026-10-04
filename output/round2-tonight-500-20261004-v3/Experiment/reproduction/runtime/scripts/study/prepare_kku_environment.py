#!/usr/bin/env python3
"""Restore local Defects4J worktrees and verify original fixed-source context."""
import argparse
from datetime import datetime, timezone
import hashlib
import json
import os
from pathlib import Path
import subprocess
import sys

ROOT = Path(__file__).resolve().parents[2]
BASE = ROOT / 'results/study/round2-v4-20260929'
OUTPUT = ROOT / 'output/kku-only-20261001/environment'
sys.path.insert(0, str(ROOT / 'scripts/study'))
from evaluate import validate_worktree


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--projects', nargs='+', default=['Lang', 'Math', 'Time', 'Closure'])
    parser.add_argument('--worktrees', type=Path, default=Path('/home/team/sqa-round2/worktrees'))
    parser.add_argument('--d4j', default='/home/team/sqa-round2/defects4j/framework/bin/defects4j')
    args = parser.parse_args()
    env = {**os.environ, 'JAVA_HOME': '/usr/lib/jvm/java-11-openjdk-amd64', 'TZ': 'America/Los_Angeles'}
    OUTPUT.mkdir(parents=True, exist_ok=True)
    results = []
    for project in args.projects:
        context = BASE / project / 'ai-context'
        identity = json.loads((context / 'context-manifest.json').read_text())
        bug = identity['bug_id']
        item = {'project': project, 'bug_id': bug, 'context_matches': [], 'prepared_at_utc': datetime.now(timezone.utc).isoformat()}
        for revision in ('f', 'b'):
            worktree = args.worktrees / project / str(bug) / revision
            worktree.parent.mkdir(parents=True, exist_ok=True)
            if not worktree.exists():
                logfile = OUTPUT / f'{project}-{bug}{revision}-checkout.log'
                with logfile.open('x') as stream:
                    subprocess.run([args.d4j, 'checkout', '-p', project, '-v', f'{bug}{revision}', '-w', str(worktree)],
                                   stdout=stream, stderr=subprocess.STDOUT, env=env, check=True, timeout=900)
            validate_worktree(worktree, project, f'{bug}{revision}')
            if revision == 'f':
                for source in identity['source_files']:
                    actual = hashlib.sha256((worktree / source['path']).read_bytes()).hexdigest()
                    if actual != source['sha256']:
                        raise ValueError(f'Fixed context source changed: {project}/{source["path"]}')
                    item['context_matches'].append({'path': source['path'], 'sha256': actual})
        results.append(item)
        print(json.dumps({'project': project, 'worktrees': 'verified', 'fixed_sources': 'match-original-context'}), flush=True)
        (OUTPUT / f'{project}-verification.json').write_text(json.dumps(item, indent=2) + '\n')
    return 0


if __name__ == '__main__':
    raise SystemExit(main())
