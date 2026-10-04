"""Compose explicit fixture development inputs for the five reviewed bugs only.

Copies fixed bytes from v3, preserves capability exclusions, makes no approval,
queue mutation or provider call. Future all-pilot adoption requires a new policy.
"""
import argparse
import json
from pathlib import Path

from .common import implementation_hashes, read_json, sha256, contained
from .fixture_policy import POLICY_V4 as FIXTURE_POLICY, select, recipe_document
from .preparation import POLICY_V5, compose, encoded, digest, validate


def build(source, destination):
    source, destination = Path(source), Path(destination)
    index = read_json(source / 'index.json')
    scope = {('Closure', 176), ('JxPath', 1), ('Codec', 1), ('Collections', 1), ('Csv', 1)}
    selected = [r for r in index['records'] if (r['project'], r['bug_id']) in scope]
    if len(selected) != 5:
        raise ValueError('Require all five reviewed development identities')
    recipe = recipe_document(implementation_hashes(), FIXTURE_POLICY)
    records = []
    destination.mkdir(parents=True, exist_ok=False)
    for row in selected:
        old = source / f"{row['project']}-{row['bug_id']}"
        for name, expected in read_json(old / 'checksums.json').items():
            if sha256(contained(old, name)) != expected:
                raise ValueError('V3 input checksum differs: ' + name)
        manifest, metadata = read_json(old / 'context-manifest.json'), read_json(old / 'prepare-metadata.json')
        validate(manifest, metadata, (old / 'prompt.md').read_bytes(), (old / 'targets.json').read_bytes(),
                 (old / 'prepare-policy.json').read_bytes(), require_eligible=True)
        document = read_json(old / 'targets.json')
        targets, excluded = select(document['targets'], FIXTURE_POLICY)
        folder = destination / old.name
        folder.mkdir()
        for entry in manifest['source_files']:
            path = folder / 'fixed-source' / entry['path']
            path.parent.mkdir(parents=True, exist_ok=True)
            path.write_bytes(contained(old / 'fixed-source', entry['path']).read_bytes())
        for name in ('context-manifest.json', 'revision-proof.json'):
            (folder / name).write_bytes((old / name).read_bytes())
        prepared = compose(folder, classes=metadata['target_classes'], targets=targets, policy=POLICY_V5,
            modified_sources=metadata['fixed_source_sha256'], fixture_classes=document['fixture_classes'],
            fixture_recipe=recipe, previous_hashes={'v3_prompt_sha256':metadata['prompt_sha256'],
                'v3_targets_sha256':metadata['targets_sha256'], 'v3_index_sha256':sha256(source / 'index.json')})
        (folder / 'capability-exclusions.json').write_bytes(encoded({'fixture_policy_id':FIXTURE_POLICY,
            'selection_basis':'predeclared recipes before any observation; no buggy outcomes',
            'selected': targets, 'excluded': excluded, 'team_approval':False}))
        (folder / 'checksums.json').write_bytes(encoded({p.relative_to(folder).as_posix():sha256(p)
            for p in sorted(folder.rglob('*')) if p.is_file()}))
        records.append({'project':row['project'], 'bug_id':row['bug_id'], 'owner':row['owner'],
            'capability_exclusion_count':len(excluded), **prepared})
    result = {'schema_version':5, 'records':records, 'policy_sha256':digest(encoded(POLICY_V5)),
        'source_v3_index_sha256':sha256(source / 'index.json'), 'scope':'five-bug offline development proposal',
        'target_count':sum(r['target_count'] for r in records),
        'max_prompt_utf8_bytes':max(r['prompt_utf8_bytes'] for r in records),
        'generation_ready':False, 'primary':False, 'team_approval':False,
        'remaining_pilot_bugs_without_recipe_review':15, 'live_requests':0, 'queue_mutations':0}
    (destination / 'index.json').write_bytes(encoded(result))
    return result


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--input', type=Path, required=True)
    parser.add_argument('--output', type=Path, required=True)
    args = parser.parse_args()
    result = build(args.input, args.output)
    print(json.dumps({k:result[k] for k in ('target_count','max_prompt_utf8_bytes','generation_ready')}))


if __name__ == '__main__':
    main()
