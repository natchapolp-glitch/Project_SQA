#!/usr/bin/env python3
"""Generate prospective reflection-based JUnit tests using fixed behavior only."""
from __future__ import annotations

import argparse
import base64
from collections import Counter
import hashlib
import io
import json
import os
from pathlib import Path
import subprocess
import sys
import tarfile
import time

ROOT = Path(__file__).resolve().parents[2]
sys.path.insert(0, str(ROOT / 'algorithms' / 'python'))
from atcg import CMAES, CMAESConfig, FSCSART, FSCSARTConfig


def observe(classpath, target, vector, timeout):
    command = ['java', '-Xmx256m', '-cp', classpath, 'SqaProbe', 'observe', target['class'],
               target['constructor_types'], target['method'], target['parameter_types'],
               ','.join(format(x, '.17g') for x in vector)]
    try:
        result = subprocess.run(command, capture_output=True, text=True, timeout=timeout,
                                env={**os.environ, 'TZ': 'America/Los_Angeles'})
    except subprocess.TimeoutExpired:
        return {'status': 'timeout', 'command': command}
    lines = [line[len('SQA_RESULT:'):] for line in result.stdout.splitlines()
             if line.startswith('SQA_RESULT:')]
    if result.returncode != 0 or len(lines) != 1:
        return {'status': 'harness_error', 'exit_code': result.returncode,
                'stdout': result.stdout, 'stderr': result.stderr, 'command': command}
    try:
        return {'status': 'ok', 'outcome': base64.b64decode(lines[0], validate=True).decode('utf-8')}
    except (ValueError, UnicodeError) as error:
        return {'status': 'protocol_error', 'error': str(error)}


def generate(targets, algorithm, budget, seed, observer):
    """Budget counts proposed inputs; every proposal gets two fixed observations.

    CMA-ES minimizes fixed-behavior frequency (an output-diversity proxy, not
    coverage). No partial population is evaluated beyond the input budget.
    Unsupported/unstable observations remain in the raw ledger.
    """
    if not targets or budget < 1:
        raise ValueError('Need supported targets and a positive budget')
    dimensions = max(t['dimensions'] for t in targets) + 1
    bounds = ((-1.0,) * dimensions, (1.0,) * dimensions)
    art = FSCSART(FSCSARTConfig(*bounds, seed=seed)) if algorithm == 'fscs-art' else None
    strategy = CMAES(CMAESConfig(*bounds, seed=seed)) if algorithm == 'cmaes' else None
    if art is None and strategy is None:
        raise ValueError('Unknown generator')
    rows, seen, frequencies = [], set(), Counter()
    while len(rows) < budget:
        population = strategy.ask() if strategy else [(art.next_case(), ())]
        evaluated = []
        for point, noise in population[:budget - len(rows)]:
            target_index = min(len(targets) - 1, int((max(-1, min(1, point[0])) + 1) / 2 * len(targets)))
            target = targets[target_index]
            vector = list(point[1:1 + target['dimensions']])
            first, second = observer(target, vector), observer(target, vector)
            stable = first.get('status') == second.get('status') == 'ok' and first['outcome'] == second['outcome']
            identity = json.dumps([target, vector], sort_keys=True)
            accepted = stable and identity not in seen
            row = {'case_id': len(rows) + 1, 'target': target, 'vector': vector,
                   'fixed_first': first, 'fixed_second': second, 'stable': stable,
                   'retained': accepted}
            rows.append(row)
            if stable:
                behavior = (target_index, first['outcome'])
                score = float(frequencies[behavior])
                frequencies[behavior] += 1
                seen.add(identity)
            else:
                score = 1e6
            evaluated.append((point, noise, score))
        if strategy and len(evaluated) == strategy.lambda_:
            strategy.tell(evaluated)
    return rows


def suite_source(rows):
    helper = (ROOT / 'algorithms/java/SqaProbe.java').read_text(encoding='utf-8')
    imports = [line for line in helper.splitlines() if line.startswith('import ')]
    nested = '\n'.join(line for line in helper.splitlines() if not line.startswith('import '))
    nested = nested.replace('public final class SqaProbe', 'public static final class SqaProbe', 1)
    lines = ['import org.junit.Test;', 'import static org.junit.Assert.assertEquals;',
             *imports, 'public class GeneratedStudyTest {']
    for row in rows:
        if not row['retained']:
            continue
        target = row['target']
        strings = [target[k] for k in ('class', 'constructor_types', 'method', 'parameter_types')]
        args = ', '.join(json.dumps(s, ensure_ascii=True) for s in strings)
        vector = ', '.join(format(x, '.17g') for x in row['vector'])
        expected = json.dumps(row['fixed_first']['outcome'], ensure_ascii=True)
        lines.extend(['  @Test(timeout=10000)', f'  public void generated{row["case_id"]}() {{',
                      f'    assertEquals({expected}, SqaProbe.observe({args}, new double[]{{{vector}}}));', '  }'])
    return '\n'.join(lines + [nested, '}', ''])


def generate_suite(project, bug_id, algorithm, budget, seed, targets, classpath, output, timeout=10):
    output = Path(output).resolve()
    output.mkdir(parents=True, exist_ok=False)
    started = time.monotonic()
    rows = generate(targets, algorithm, budget, seed,
                    lambda target, vector: observe(classpath, target, vector, timeout))
    (output / 'observations.json').write_text(json.dumps(rows, indent=2) + '\n', encoding='utf-8')
    count = sum(row['retained'] for row in rows)
    manifest = {'project': project, 'bug_id': bug_id, 'generator': algorithm, 'seed': seed,
                'budget': budget, 'proposed_inputs': len(rows), 'fixed_observation_executions': 2 * len(rows),
                'test_count': count, 'status': 'generated' if count else 'no_stable_cases',
                'generation_seconds': time.monotonic() - started,
                'objective': 'fixed behavior frequency minimization' if algorithm == 'cmaes' else 'normalized input distance',
                'oracle_scope': 'fixed revision only; observed exceptions compare class, not message',
                'limitations': ['Declared methods with bounded recursive fixtures; unconstructible reference arguments become null.',
                                'Observations exceeding 16000 characters compare SHA-256 and byte length.',
                                'Opaque object returns compare runtime type and nullness only, not full state.',
                                'Constructors observe successful construction or target exception only.',
                                'Vector uniqueness does not imply unique decoded Java argument values.',
                                'Runtime coverage is measured afterward, not used as CMA-ES fitness.']}
    if count:
        source = suite_source(rows).encode('utf-8')
        helper = (ROOT / 'algorithms/java/SqaProbe.java').read_bytes()
        (output / 'GeneratedStudyTest.java').write_bytes(source)
        (output / 'SqaProbe.java').write_bytes(helper)
        archive = output / f'{project}-{bug_id}f-{algorithm}.{seed}.tar.bz2'
        with tarfile.open(archive, 'w:bz2') as tar:
            # Defects4J treats every source file in the archive as a test class.
            # The helper is nested inside the single actual JUnit class.
            for name, data in [('GeneratedStudyTest.java', source)]:
                info = tarfile.TarInfo(name)
                info.size, info.mode, info.mtime = len(data), 0o644, 0
                tar.addfile(info, io.BytesIO(data))
        manifest.update(suite=str(archive), suite_sha256=hashlib.sha256(archive.read_bytes()).hexdigest())
    (output / 'generation.json').write_text(json.dumps(manifest, indent=2) + '\n', encoding='utf-8')
    return manifest


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    for name in ('project', 'classpath'):
        parser.add_argument('--' + name, required=True)
    parser.add_argument('--bug-id', required=True, type=int)
    parser.add_argument('--generator', choices=['cmaes', 'fscs-art'], required=True)
    parser.add_argument('--budget', type=int, default=30)
    parser.add_argument('--seed', type=int, default=101)
    parser.add_argument('--targets', type=Path, required=True)
    parser.add_argument('--output', type=Path, required=True)
    parser.add_argument('--timeout', type=float, default=10)
    args = parser.parse_args()
    targets = json.loads(args.targets.read_text(encoding='utf-8'))['targets']
    result = generate_suite(args.project, args.bug_id, args.generator, args.budget,
                            args.seed, targets, args.classpath, args.output, args.timeout)
    print(json.dumps(result, indent=2))
    return 0 if result['test_count'] else 1


if __name__ == '__main__':
    raise SystemExit(main())
