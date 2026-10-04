#!/usr/bin/env python3
"""Continue the selected scope after the first-project batch, without retries."""
import argparse
from datetime import datetime, timezone
from pathlib import Path
import shutil
import time
import math
import solo_ai_nightly as ai
import solo_batch as batch
from solo_scope import load_scope


def sealed(root, case, method):
    path = root / 'Experiment/evaluations' / case / method / 'run-final'
    if not (path / 'receipt.json').is_file():
        return False
    batch.verify(path)
    return True


def quota_observations(roots):
    observations = {}
    for root in roots:
        for method, (folder, _, _) in ai.METHODS.items():
            for path in (root / folder / 'Result').glob('*/*/*/metadata.json'):
                row = batch.read(path)
                remaining = row.get('model_quota', {}).get('daily_remaining_tokens')
                intent = path.with_name('request-intent.json')
                alias = row.get('account_alias') or (batch.read(intent).get('account_alias') if intent.exists() else None)
                if row.get('error_state') == 'QUOTA_PAUSED':
                    remaining = 0
                key = alias, method
                if alias and isinstance(remaining, (int, float)):
                    stamp = path.stat().st_mtime_ns
                    if stamp > observations.get(key, {}).get('stamp', -1):
                        observations[key] = {'stamp': stamp, 'remaining': remaining,
                                             'source': str(path), 'sha256': batch.sha(path)}
    return observations


def choose_alias(aliases, methods, observations, minimum=30000):
    def admitted(alias, method):
        value = observations.get((alias, method), {}).get('remaining')
        return isinstance(value, (int, float)) and not isinstance(value, bool) and math.isfinite(value) and value >= minimum
    eligible = [a for a in aliases if all(admitted(a, m) for m in methods)]
    if not eligible:
        return None
    return max(eligible, key=lambda a: min(observations[a, m]['remaining'] for m in methods))


def main():
    cli = argparse.ArgumentParser(description=__doc__)
    cli.add_argument('--offline', type=Path, required=True)
    cli.add_argument('--ai', type=Path, required=True)
    cli.add_argument('--pilot', type=Path, required=True)
    cli.add_argument('--scope', type=Path, required=True)
    cli.add_argument('--secrets', type=Path, required=True)
    cli.add_argument('--control', type=Path, required=True)
    cli.add_argument('--cutoff', default='2026-10-04T15:00:00+00:00')
    cli.add_argument('--dry-run', action='store_true')
    cli.add_argument('--start-signal', type=Path, help='Optional manual release file after initial delivery/rehearsal')
    args = cli.parse_args()
    offline, output, pilot = args.offline.resolve(), args.ai.resolve(), args.pilot.resolve()
    protocol, parent, config = ai.load_protocol(output)
    if parent != offline:
        cli.error('AI parent differs')
    selection = load_scope(args.scope, config, batch.sha(offline / 'Experiment/protocol/offline.json'))
    first_plan = batch.read(output / 'Experiment/batch-plan.json')
    first_cases = {j['case'] for j in first_plan['jobs']} | {'Csv-1', 'Lang-1', 'Math-1'}
    pending = [c for c in selection['cases'] if c not in first_cases]
    manifest = {'schema': 'solo-expansion.v1', 'scope_sha256': batch.sha(args.scope),
                'cases': pending, 'first_batch_sha256': batch.sha(output / 'Experiment/batch-plan.json'),
                'ai_protocol_sha256': batch.sha(output / 'Experiment/protocol/ai.json'),
                'controller_sha256': batch.sha(Path(__file__)), 'cutoff_utc': args.cutoff,
                'stop_dispatch_seconds_before_cutoff': 1200, 'cpu_slots': 1,
                'account_policy': 'latest response quota observation; one alias bound before each case; never switch within a job',
                'admission_minimum_observed_tokens': 30000, 'independent_quota_buckets_verified': False,
                'retry_policy': 'no automatic HTTP retry, no regeneration of terminal failures',
                'unrequested_policy': 'leave PENDING; retain separate admission reason, not a model failure'}
    manifest['start_signal'] = str(args.start_signal.resolve()) if args.start_signal else None
    if args.dry_run:
        print({'expansion_cases': len(pending), 'planned_scope_bugs': len(selection['cases']), 'provider_requests': 0})
        return
    control = args.control.resolve()
    plan = control / 'plan.json'
    if plan.exists():
        if batch.read(plan) != manifest:
            raise ValueError('Expansion manifest changed')
    else:
        batch.write(plan, manifest)
    cutoff = datetime.fromisoformat(args.cutoff)
    print('Waiting for sealed first-project batch; no concurrent Java/provider work', flush=True)
    while not all(sealed(offline, j['case'], m) if not m.startswith('kku-') else sealed(output, j['case'], m)
                  for j in first_plan['jobs'] for m in batch.METHODS):
        if (cutoff - datetime.now(timezone.utc)).total_seconds() < 1200:
            print('Cutoff while waiting', flush=True)
            return
        time.sleep(20)
    # Completion receipts are published just before controller exit; allow its CPU lock to release.
    time.sleep(3)
    batch.write(control / 'first-batch-complete.json', {'first_batch_sha256': manifest['first_batch_sha256'],
                 'observed_at_utc': datetime.now(timezone.utc).isoformat()})
    while args.start_signal and not args.start_signal.exists():
        if (cutoff - datetime.now(timezone.utc)).total_seconds() < 1200:
            return
        time.sleep(10)
    for case in pending:
        if (cutoff - datetime.now(timezone.utc)).total_seconds() < 1200:
            print('Cutoff guard: stopping dispatch', flush=True)
            break
        if shutil.disk_usage(Path(config['worktrees_root'])).free < 10 * 1024**3:
            print('Disk guard: stopping dispatch below 10 GiB free', flush=True)
            break
        assignment = control / 'admissions' / (case + '.json')
        methods = [m for m in ai.METHODS if not sealed(output, case, m)]
        if assignment.exists():
            row = batch.read(assignment)
            if row['case'] != case or row['plan_sha256'] != batch.sha(plan):
                raise ValueError('Admission binding changed')
            alias = row['account_alias']
        else:
            observations = quota_observations([pilot, output])
            alias = choose_alias(protocol['known_accounts'], methods, observations) if methods else None
            batch.write(assignment, {'case': case, 'plan_sha256': batch.sha(plan), 'account_alias': alias,
                        'decision_at_utc': datetime.now(timezone.utc).isoformat(), 'pending_ai_methods': methods,
                        'reason': 'observed quota admission' if alias else 'no admitted alias; AI remains unrequested',
                        'quota_sources': {m: observations.get((alias, m)) for m in methods},
                        'no_http_retry': True})
        print(f'EXPANSION {case} ACCOUNT {alias}', flush=True)
        batch.run_algorithms(offline, [case], config, ['cmaes', 'fscs-art'])
        for method in methods:
            if not alias or (cutoff - datetime.now(timezone.utc)).total_seconds() < 1200:
                continue
            ai.run_case(output, offline, case, method, alias, args.secrets, protocol, config)
    batch.write(control / 'dispatch-ended.json', {'ended_at_utc': datetime.now(timezone.utc).isoformat(),
                'plan_sha256': batch.sha(plan), 'completion_claim': False})
    print('Expansion dispatch ended; keep all recorded outcomes', flush=True)


if __name__ == '__main__':
    main()
