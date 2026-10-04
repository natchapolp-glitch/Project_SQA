#!/usr/bin/env python3
"""Freeze a reduced bug scope independently of experimental outcomes."""
import argparse
from collections import Counter
from pathlib import Path
import solo_batch as batch


def select_cases(inventory, count):
    if count < 1 or count > sum(map(len, inventory.values())):
        raise ValueError('Scope size outside installed inventory')
    projects = sorted(inventory)
    ordered = [f'{project}-{inventory[project][index]}'
               for index in range(max(map(len, inventory.values())))
               for project in projects if index < len(inventory[project])]
    if len(ordered) != len(set(ordered)):
        raise ValueError('Duplicate inventory cases')
    return ordered[:count]


def load_scope(path, config, protocol_hash):
    manifest = batch.read(path)
    expected = select_cases(config['inventory'], manifest['selected_bugs'])
    if (manifest['schema'] != 'solo-selected-scope.v1'
            or manifest['offline_protocol_sha256'] != protocol_hash
            or manifest['cases'] != expected or manifest['planned_jobs'] != len(expected) * len(batch.METHODS)):
        raise ValueError('Selected scope differs from its deterministic inventory/protocol binding')
    return manifest


def main():
    cli = argparse.ArgumentParser(description=__doc__)
    cli.add_argument('--offline', type=Path, required=True)
    cli.add_argument('--output', type=Path, required=True)
    cli.add_argument('--count', type=int, default=500)
    args = cli.parse_args()
    source = args.offline / 'Experiment/protocol/offline.json'
    config = batch.read(source)
    cases = select_cases(config['inventory'], args.count)
    manifest = {'schema': 'solo-selected-scope.v1', 'selected_bugs': len(cases),
                'planned_jobs': len(cases) * len(batch.METHODS), 'cases': cases,
                'full_inventory_bugs': sum(map(len, config['inventory'].values())),
                'excluded_from_selected_scope': sum(map(len, config['inventory'].values())) - len(cases),
                'per_project': dict(Counter(c.rsplit('-', 1)[0] for c in cases)),
                'offline_protocol_sha256': batch.sha(source),
                'selection_policy': 'project-name sorted round robin; installed active bug order; no outcome filtering',
                'human_request': '2026-10-04: approximately 500 bugs, all four methods; keep pass and fail',
                'generation_repeats': 1, 'completion_claim': False}
    if args.output.exists():
        if batch.read(args.output) != manifest:
            raise ValueError('Existing scope is immutable')
    else:
        batch.write(args.output, manifest)
    print({k: manifest[k] for k in ('selected_bugs', 'planned_jobs', 'per_project')})


if __name__ == '__main__':
    main()
