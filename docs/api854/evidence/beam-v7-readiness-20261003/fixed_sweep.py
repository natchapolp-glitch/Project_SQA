"""Diagnostic fixed-only sweep. Does not alter capability selection or approvals."""
import json
import os
from pathlib import Path
import subprocess
import sys
from collections import Counter

ROOT = Path(__file__).resolve().parents[4]
sys.path.insert(0, str(ROOT))
sys.path.insert(0, str(ROOT / 'scripts/study'))
from scripts.study.api854.common import cpu_slot, sha256, write_json
from scripts.study.api854.fixture_policy import POLICY_V5
from generate import observe
from run import stage
from evaluate import validate_worktree


def run():
    output = Path(sys.argv[1]).resolve()
    output.mkdir(parents=True, exist_ok=False)
    d4j = '/home/aomsin/sqa-round2/defects4j/framework/bin/defects4j'
    lock_root = Path('/home/aomsin/sqa-round2/worktrees-isolated')
    trees = lock_root / output.name
    preparation = ROOT / 'output/api854-20261003/prepare-v7-twenty-bug-development'
    discovery = ROOT / 'output/api854-20261003/prepare-v3'
    load = lambda path: json.loads(path.read_text(encoding='utf-8'))
    index = load(preparation / 'index.json')
    records = []
    helper = output / 'probe-classes'
    helper.mkdir()
    with cpu_slot(lock_root):
        stage(['javac', '-source', '7', '-target', '7', '-d', helper,
               ROOT / 'algorithms/java/SqaProbe.java'], ROOT, output / 'compile-probe', 120)
        for row in index['records']:
            name = f"{row['project']}-{row['bug_id']}"
            folder = output / name
            folder.mkdir()
            record = {'project': row['project'], 'bug_id': row['bug_id'], 'owner': row['owner'],
                      'primary': False, 'semantic_approved': False, 'cases': []}
            print('START', name, flush=True)
            try:
                tree = trees / name
                stage([d4j, 'checkout', '-p', row['project'], '-v', f"{row['bug_id']}f", '-w', tree],
                      ROOT, folder / 'checkout', 900)
                validate_worktree(tree, row['project'], f"{row['bug_id']}f")
                manifest = load(preparation / name / 'context-manifest.json')
                matched, mismatches = [], []
                for source in manifest['source_files']:
                    if source['path'].endswith('.java'):
                        actual = sha256(tree / source['path'])
                        entry = {'path': source['path'], 'expected': source['sha256'], 'actual': actual}
                        (matched if actual == source['sha256'] else mismatches).append(entry)
                record['fixed_source_comparison'] = {'matched': matched, 'mismatches': mismatches}
                if mismatches:
                    raise ValueError('Fixed source bytes differ from v7; no observations taken')
                stage([d4j, 'compile', '-w', tree], ROOT, folder / 'compile', 900)
                cp_file = folder / 'cp.test.txt'
                stage([d4j, 'export', '-w', tree, '-p', 'cp.test', '-o', cp_file],
                      ROOT, folder / 'export-cp', 120)
                classpath = cp_file.read_text().strip() + os.pathsep + str(helper)
                targets = load(discovery / name / 'targets.json')['targets']
                partition = load(preparation / name / 'capability-exclusions.json')
                key = lambda t: tuple(t[k] for k in ('class','constructor_types','method','parameter_types'))
                reasons = {key(r['target']): r['reason'] for r in partition['excluded']}
                for n, target in enumerate(targets):
                    count = sum(bool(p) for p in (target['constructor_types'] + ',' + target['parameter_types']).split(','))
                    vector = [0.5] * max(3, count * 3)
                    first = observe(classpath, target, vector, 20, POLICY_V5)
                    second = observe(classpath, target, vector, 20, POLICY_V5)
                    case = {'target': target, 'vector': vector,
                            'v7_capability': 'unsupported' if key(target) in reasons else 'selected',
                            'exclusion_reason': reasons.get(key(target)), 'first': first, 'second': second,
                            'repeat_equal': first == second, 'oracle_approved': False}
                    record['cases'].append(case)
                    write_json(folder / f'case-{n:03d}.json', case)
                record['status'] = 'fixed_sweep_complete'
            except (ValueError, RuntimeError, OSError) as error:
                record['status'] = 'setup_failed'
                record['error'] = str(error)
            record['status_counts'] = dict(Counter(c['first']['status'] for c in record['cases']))
            record['repeated_stable_invocations'] = sum(c['repeat_equal'] and c['first'].get('target_invoked') is True
                                                       and c['first']['status'] == 'ok' for c in record['cases'])
            write_json(folder / 'record.json', record)
            records.append(record)
            print('DONE', name, record['status'], record['status_counts'], flush=True)
    result = {'scope': '691 common declarations, two identical-vector fixed-only diagnostic observations',
              'primary': False, 'semantic_approved': False, 'capability_changes': 0,
              'real_kku_requests': 0, 'queue_mutations': 0, 'fixture_policy': POLICY_V5,
              'inspector_sha256': sha256(__file__), 'probe_sha256': sha256(ROOT / 'algorithms/java/SqaProbe.java'),
              'preparation_index_sha256': sha256(preparation / 'index.json'),
              'host': 'current aomsin WSL host; not beam-pc1 acceptance',
              'observation_timezone': 'America/Los_Angeles', 'records': records}
    write_json(output / 'index.json', result)
    checksums = {p.relative_to(output).as_posix(): sha256(p) for p in output.rglob('*') if p.is_file()}
    write_json(output / 'checksums.json', checksums)
    print('COMPLETE', len(records), sum(len(r['cases']) for r in records), flush=True)


if __name__ == '__main__':
    run()
