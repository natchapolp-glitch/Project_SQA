"""Versioned declaration-only prompt/context proposal; never modifies Aom v1."""
from __future__ import annotations

import argparse
import json
from pathlib import Path
import subprocess

from .common import contained, read_json, sha256, write_json
from .review_prepare import composite_hash


def revision_proof(tree, project, bug, files):
    git = ['git', '-c', f'safe.directory={tree}', '-C', str(tree)]
    def query(*args):
        return subprocess.check_output([*git, *args], text=True).strip()
    head = query('rev-parse', 'HEAD')
    fixed = query('rev-parse', f'D4J_{project}_{bug}_FIXED_VERSION')
    changes = query('status', '--porcelain', '--', *files)
    if head != fixed or changes:
        raise ValueError('Supplement sources must be unmodified at the exact fixed tag')
    return {'head': head, 'fixed_tag_commit': fixed, 'selected_source_changes': changes, 'verified': True}


def export(artifacts, review, adapters, output):
    review = read_json(Path(review) / 'index.json')
    if review['artifact_integrity_passed'] != 20 or review['eligibility_counts'] != {'declaration_intersection_verified': 20}:
        raise ValueError('Complete, matching independent review required before supplement export')
    output = Path(output)
    output.mkdir(parents=True, exist_ok=False)
    records = []
    for result in review['bugs']:
        project, bug = result['project'], result['bug_id']
        name = f'{project}-{bug}'
        incoming = Path(artifacts) / name
        destination = output / name
        destination.mkdir()
        choices = [Path(root) / name for root in adapters if (Path(root) / name / 'adapter.json').is_file()]
        if len(choices) != 1:
            raise ValueError('Exactly one independently scanned adapter required')
        folder = choices[0]
        adapter = read_json(folder / 'adapter.json')
        tree = Path(adapter['fixed_worktree'])
        source_dir = (folder / 'dir.src.classes.txt').read_text(encoding='utf-8').strip()
        sources = dict(result['artifacts']['fixed_source_sha256'])
        extras = []
        for receiver in result['eligibility']['extra_receiver_classes_missing_from_v1_modified_source']:
            relative = Path(source_dir) / Path(*receiver.split('$', 1)[0].split('.')).with_suffix('.java')
            source = contained(tree, relative)
            data = source.read_bytes()
            target = destination / 'additional-fixed-source' / relative
            target.parent.mkdir(parents=True, exist_ok=True)
            with target.open('xb') as stream:
                stream.write(data)
            extras.append({'path': relative.as_posix(), 'sha256': sha256(target), 'bytes': len(data), 'receiver': receiver})
            sources[relative.as_posix()] = sha256(target)
        proof = revision_proof(tree, project, bug, sorted(sources))
        for relative, digest in sources.items():
            if sha256(contained(tree, Path(relative))) != digest:
                raise ValueError('Supplement source changed during export')
        targets = result['eligibility']['eligible_targets']
        prompt = (incoming / 'prompt.md').read_bytes()
        suffix = (
            '\n\n## Preregistered shared declaration eligibility supplement\n\n'
            'Generate tests only for the following eligible declarations. This list comes from '
            'fixed/buggy signature intersection, not execution behavior or triggering tests. '
            'The list does not certify fixture construction or semantic validity. '
            'Use meaningful non-null domain fixtures from the supplied fixed APIs. '
            'Do not substitute construction failures, null-only inputs, empty tests or non-null-only assertions '
            'for checks that reach a target. Preserve assertions; no compile/test feedback or repair loop.\n\n'
            + json.dumps(targets, ensure_ascii=False, sort_keys=True, indent=2) + '\n')
        for extra in extras:
            data = (destination / 'additional-fixed-source' / extra['path']).read_bytes()
            suffix += ('\n## Additional fixed concrete receiver: ' + extra['receiver'] + '\n\n```\n'
                       + data.decode('utf-8') + '\n```\n')
        proposed = destination / 'prompt.eligible-v2.proposal.md'
        with proposed.open('xb') as stream:
            stream.write(prompt + suffix.encode('utf-8'))
        manifest = read_json(incoming / 'context-manifest.json')
        source_files = [*manifest['source_files'], *[{k: e[k] for k in ('path', 'sha256', 'bytes')} for e in extras]]
        write_json(destination / 'context-manifest.v2.proposal.json', {
            **manifest, 'selection_policy_id': 'beam-modified-and-shared-receiver-java-v2-proposal',
            'source_files': source_files, 'source_hash': composite_hash(source_files),
            'derived_from_manifest_sha256': sha256(incoming / 'context-manifest.json'),
            'original_source_location': 'Aom commit 8fcec539 output/api854-20261003/prepare-v1/' + name + '/fixed-source',
            'additional_source_location': 'additional-fixed-source', 'approval_state': 'proposal_pending_team_review'})
        write_json(destination / 'targets.json', {'targets': targets,
            'excluded_fixed_only': result['eligibility']['excluded_fixed_only'],
            'eligibility': 'shared declaration signatures; fixtures/oracles pending review'})
        write_json(destination / 'revision-proof.json', proof)
        record = {'project': project, 'bug_id': bug, 'owner': result['owner'], 'approval_state': 'proposal_pending_team_review',
            'v1_prompt_sha256': sha256(incoming / 'prompt.md'), 'prompt_sha256': sha256(proposed),
            'prompt_utf8_bytes': proposed.stat().st_size, 'prompt_policy_id': 'beam-eligibility-supplement-v2-proposal',
            'context_policy_id': 'beam-modified-and-shared-receiver-java-v2-proposal',
            'source_sha256': composite_hash(source_files), 'context_source_hash': composite_hash(source_files),
            'fixed_source_sha256': result['artifacts']['fixed_source_sha256'],
            'target_classes': result['artifacts']['target_classes'],
            'additional_receiver_source_sha256': {e['path']: e['sha256'] for e in extras},
            'targets_sha256': sha256(destination / 'targets.json'), 'target_count': len(targets),
            'processing_scope': 'new proposal bytes; v1 prefix preserved, no generated assertion/source repairs',
            'compatibility': 'team must adopt v2 policies and supplementary receiver mapping before API use',
            'usable': False}
        write_json(destination / 'prepare-metadata.v2.proposal.json', record)
        checksums = {p.relative_to(destination).as_posix(): sha256(p) for p in destination.rglob('*') if p.is_file()}
        write_json(destination / 'checksums.json', checksums)
        records.append(record)
    summary = {'schema_version': 1, 'scope': 'Beam review supplement; NOT approved primary preparation',
        'primary': False, 'live_queue_mutations': 0, 'real_kku_requests': 0, 'records': records,
        'largest_prompt_utf8_bytes': max(r['prompt_utf8_bytes'] for r in records),
        'token_count_or_provider_overhead_verified': False,
        'input_index_sha256': sha256(Path(artifacts) / 'index.json')}
    write_json(output / 'index.json', summary)
    return summary


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    for key in ('artifacts', 'review', 'output'):
        parser.add_argument('--' + key, type=Path, required=True)
    parser.add_argument('--adapters', nargs='+', type=Path, required=True)
    args = parser.parse_args()
    result = export(args.artifacts, args.review, args.adapters, args.output)
    print(json.dumps({'bugs': len(result['records']), 'largest_prompt_utf8_bytes': result['largest_prompt_utf8_bytes'],
                      'real_kku_requests': 0}))


if __name__ == '__main__':
    main()
