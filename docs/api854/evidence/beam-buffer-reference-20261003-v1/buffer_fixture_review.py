"""Bounded fixed-source reference checks for the eight prospective buffer recipes.

These are development checks, not primary jobs or team approval. The four input
examples and expected results are declared independently of observed outcomes;
every failed observation is retained. No buggy observation selects fixtures.
"""
import argparse
import base64
from pathlib import Path

from .common import ROOT, read_json, sha256, write_json, cpu_slot, assert_implementation
from .fixture_policy import BUFFER_SIGNATURES, POLICY_V6_BUFFER
from generate import observe


# Boundary examples from the fixture's documented input domains. Expected values
# come from decimal arithmetic, string concatenation and bounded stream reads.
EXAMPLES = [
    {'a': -1.0, 'b': -1.0, 'int': '0', 'long': '1000000000', 'decimal': '0',
     'range': 'true', 'text': '123A', 'buffer': '~A~~~~~~', 'read': '1', 'line': 0, 'last': 65},
    {'a': -0.25, 'b': 1.0, 'int': '7', 'long': '1234567890123', 'decimal': '12.50',
     'range': 'true', 'text': '123ABCD', 'buffer': '~A\nBC\nD~', 'read': '6', 'line': 2, 'last': 68},
    {'a': 0.0, 'b': -1.0, 'int': '12345', 'long': '1234567890123', 'decimal': '12.50',
     'range': 'true', 'text': '45.52', 'buffer': '~~1~~~~~', 'read': '1', 'line': 0, 'last': 49},
    {'a': 1.0, 'b': 1.0, 'int': '999999999', 'long': '123456789012345678', 'decimal': '-0.125',
     'range': 'false', 'text': '45.52345', 'buffer': '~~12\n34~', 'read': '5', 'line': 1, 'last': 52},
]


def scalar(class_name, text):
    return class_name + ':' + base64.b64encode(text.encode('utf-8')).decode('ascii')


def expected(target, example):
    if target['class'].endswith('TextBuffer'):
        return f"void|state=text:{example['text']}:size={len(example['text'])}"
    if target['class'].endswith('ExtendedBufferedReader'):
        buffer = '[C[' + ''.join(scalar('java.lang.Character', c) + ';' for c in example['buffer']) + ']'
        return ('value:' + scalar('java.lang.Integer', example['read'])
                + f"|state=reader:line={example['line']}:last={example['last']}:buffer={buffer}")
    field, cls = {
        'parseInt': ('int', 'java.lang.Integer'), 'parseLong': ('long', 'java.lang.Long'),
        'parseBigDecimal': ('decimal', 'java.math.BigDecimal'),
        'inLongRange': ('range', 'java.lang.Boolean'),
    }[target['method']]
    return 'value:' + scalar(cls, example[field]) + '|state=stateless-scalars'


def run(development, output, worktrees):
    index = read_json(Path(development) / 'index.json')
    protocol = read_json(Path(development) / 'protocol-proposal.json')
    if index['fixture_policy_id'] != POLICY_V6_BUFFER:
        raise ValueError('Require the predeclared buffer development policy')
    output = Path(output)
    output.mkdir(parents=True, exist_ok=False)
    rows, source_hashes, identities = [], {}, set()
    with cpu_slot(worktrees):
        assert_implementation(protocol['source_sha256'])
        for project in ['JacksonCore', 'Csv']:
            record = next(r for r in index['records'] if r['project'] == project and r['approach'] == 'fscs-art')
            generation = Path(record['generation_result']).parent
            result = read_json(generation / 'result.json')
            setup = read_json(generation / 'setup/adapter.json')
            tree = Path(setup['fixed_worktree'])
            for name, digest in result['fixed_source_sha256'].items():
                if sha256(tree / name) != digest:
                    raise ValueError('Fixed source changed after generation')
            source_hashes[project] = result['fixed_source_sha256']
            targets = read_json(generation / 'setup/targets.fixture-policy.json')['targets']
            for target in targets:
                identity = (target['class'], target['method'], target['parameter_types'])
                if identity not in BUFFER_SIGNATURES:
                    continue
                identities.add(identity)
                for example_id, example in enumerate(EXAMPLES, 1):
                    vector = [example['a'], example['b']] + [0.0] * (target['dimensions'] - 2)
                    first = observe(setup['classpath'], target, vector, 10, POLICY_V6_BUFFER)
                    second = observe(setup['classpath'], target, vector, 10, POLICY_V6_BUFFER)
                    reference = expected(target, example)
                    valid = (first == second and first.get('status') == 'ok'
                             and first.get('target_invoked') is True and first.get('outcome') == reference)
                    rows.append({'project': project, 'bug_id': 1, 'target': target, 'example_id': example_id,
                                 'vector': vector, 'expected': reference, 'fixed_first': first,
                                 'fixed_second': second, 'reference_check_passed': valid})
        assert_implementation(protocol['source_sha256'])
    summary = {'primary': False, 'gate_a_approved': False, 'team_or_primary_approval': False,
        'fixture_policy_id': POLICY_V6_BUFFER, 'reviewer_sha256': sha256(__file__),
        'protocol_sha256': sha256(Path(development) / 'protocol-proposal.json'),
        'source_sha256': protocol['source_sha256'], 'fixed_source_sha256': source_hashes,
        'expected_declarations': 8, 'observed_declarations': len(identities), 'example_count': len(rows),
        'fixed_observation_count': len(rows) * 2,
        'passed': identities == BUFFER_SIGNATURES and len(rows) == 32 and all(r['reference_check_passed'] for r in rows),
        'real_kku_requests': 0, 'live_queue_mutations': 0,
        'limitations': ['Four bounded reference examples per declaration; not exhaustive input coverage.',
                       'This fixed-source sweep supplements sampled suites; it is not a primary algorithm result.'],
        'cases': rows}
    write_json(output / 'review.json', summary)
    with (output / 'buffer_fixture_review.py').open('xb') as stream:
        stream.write(Path(__file__).read_bytes())
    write_json(output / 'checksums.json', {p.name: sha256(p) for p in output.iterdir() if p.is_file()})
    print(f"REFERENCE CHECK: declarations={len(identities)}, examples={len(rows)}, passed={summary['passed']}")
    return summary['passed']


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--development', required=True, type=Path)
    parser.add_argument('--output', required=True, type=Path)
    parser.add_argument('--worktrees', required=True, type=Path)
    args = parser.parse_args()
    raise SystemExit(0 if run(args.development, args.output, args.worktrees) else 1)
