"""Run explicit local development proofs sequentially; never contacts a queue/API."""
import argparse
from collections import Counter
from pathlib import Path

from . import algorithm_worker, configuration, evaluate_worker
from .common import ROOT, cpu_slot, sha256, write_json, read_json, job_relative, identifier
from .fixture_policy import POLICY_V4, select


def run(bugs, output, worktrees, d4j):
    known = {(r['project'], r['bug_id']) for r in read_json(ROOT / 'docs/api854/evidence/beam-aom-review-20261003/index.json')['bugs']}
    if not bugs or len(set(bugs)) != len(bugs) or not set(bugs) <= known:
        raise ValueError('Require unique reviewed pilot bugs')
    for project, bug in bugs:
        inventory = read_json(ROOT / f'docs/api854/evidence/beam-aom-review-20261003/declarations/{project}-{bug}/targets.json')
        if not select(inventory['targets'], POLICY_V4)[0]:
            raise ValueError(f'{project}-{bug} has no explicit recipe in this version')
    output = Path(output)
    output.mkdir(parents=True, exist_ok=False)
    run_id = identifier('beam-development-' + output.name, 'run ID')
    protocol = configuration.proposal()
    protocol.update(fixture_policy_id=POLICY_V4, status='development_fixture_review_not_primary',
                    beam_worker_id='beam-pc1', max_cpu_slots=1)
    protocol_path = output / 'protocol-proposal.json'
    write_json(protocol_path, protocol)
    protocol_hash = sha256(protocol_path)
    results = ROOT / 'results/validation/api854-beam'
    records = []
    with cpu_slot(worktrees):
        for project, bug in bugs:
            for approach in ['fscs-art', 'cmaes']:
                name = f'{project}-{bug}-{approach}'
                print('START generation', name, flush=True)
                job = {'schema_version': 1, 'run_id': run_id, 'project': project, 'bug_id': bug,
                    'approach': approach, 'protocol_hash': protocol_hash, 'repeat_index': 1,
                    'attempt_id': f'fixture-gen-{name}-1'}
                write_json(output / f'{name}-generation-job.json', job)
                generated = algorithm_worker.execute(job, protocol, results, worktrees, d4j)
                folder = results / job_relative(job) / 'generation'
                record = {'project': project, 'bug_id': bug, 'approach': approach,
                    'generation_outcome': generated['observed_outcome'], 'generation_result': str(folder / 'result.json'),
                    'declared_test_count': generated.get('test_count'), 'error': generated.get('error')}
                if generated['observed_outcome'] == 'generated':
                    observations = read_json(folder / 'suite/observations.json')
                    record.update(observation_status_counts=dict(Counter(o['fixed_first']['status'] for o in observations)),
                        retained_target_invocations=sum(o['retained'] and o['fixed_first'].get('target_invoked') is True for o in observations),
                        retained_exception_count=sum(o['retained'] and o['fixed_first'].get('outcome', '').startswith('exception:') for o in observations),
                        suite_sha256=generated['suite_sha256'])
                    evaluation_job = {**job, 'attempt_id': f'fixture-eval-{name}-1'}
                    write_json(output / f'{name}-evaluation-job.json', evaluation_job)
                    print('START evaluation', name, generated['test_count'], flush=True)
                    evaluated = evaluate_worker.execute(evaluation_job, protocol, results, worktrees, d4j,
                                                        generated['generation']['suite'], generated)
                    evaluation_folder = results / job_relative(evaluation_job) / 'evaluation'
                    measurement = evaluated.get('measurement', {})
                    record.update(evaluation_outcome=evaluated['observed_outcome'], usable=evaluated.get('usable', False),
                        evaluation_result=str(evaluation_folder / 'result.json'),
                        **{key: measurement.get(key) for key in ['fault_detected', 'line_covered', 'line_total', 'branch_covered', 'branch_total']},
                        stage_counts={stage: read_json(evaluation_folder / 'measurement' / stage / 'sqa-stage-counts.json')
                            if (evaluation_folder / 'measurement' / stage / 'sqa-stage-counts.json').is_file() else None
                            for stage in ['fixed-1', 'fixed-2', 'buggy', 'coverage']}, error=evaluated.get('error'))
                write_json(output / f'{name}-receipt.json', record)
                records.append(record)
                print('DONE', name, record.get('evaluation_outcome', record['generation_outcome']), flush=True)
    write_json(output / 'index.json', {'primary': False, 'fixture_policy_id': POLICY_V4,
        'protocol_sha256': protocol_hash, 'worker_id': 'beam-pc1', 'cpu_slots': 1,
        'development_runner_sha256': sha256(__file__), 'real_kku_requests': 0, 'live_queue_mutations': 0,
        'semantic_validity': 'pending_review', 'records': records})
    return records


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--bugs', required=True, nargs='+', metavar='PROJECT:BUG')
    parser.add_argument('--output', required=True, type=Path)
    parser.add_argument('--worktrees', required=True, type=Path)
    parser.add_argument('--d4j', default='defects4j')
    args = parser.parse_args()
    bugs = [(p, int(b)) for p, b in (name.split(':') for name in args.bugs)]
    run(bugs, args.output, args.worktrees, args.d4j)


if __name__ == '__main__':
    main()
