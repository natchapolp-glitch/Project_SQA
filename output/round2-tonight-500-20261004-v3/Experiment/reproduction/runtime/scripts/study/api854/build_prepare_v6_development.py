"""Compose 20 current-runtime development inputs; retain all unsupported targets.

Not final shared acceptance, not generation authorization, and no provider call.
"""
import argparse
from pathlib import Path
import json

from .common import ROOT, contained, read_json, sha256, implementation_hashes
from .preparation import POLICY_V6, compose, encoded, digest, validate, clean_targets, explicit_scope
from .fixture_policy import POLICY_V5, select, recipe_document


def build(source, destination):
    source, destination = Path(source).resolve(), Path(destination).resolve()
    index = read_json(source/'index.json')
    core = read_json(ROOT/'experiments/configs/api854-20261003/protocol.core-frozen.json')
    identities = {(r['project'],r['bug_id'],r['owner']) for r in index['records']}
    expected = {(r['project'],r['bug_id'],r['owner']) for r in core['pilot_bugs']}
    if len(index['records']) != 20 or identities != expected or {(p,b) for p,b,o in identities} != explicit_scope(POLICY_V6):
        raise ValueError('Require exact 20-bug pilot inventory')
    runtime = implementation_hashes()
    recipe = recipe_document(runtime, POLICY_V5)
    destination.mkdir(parents=True, exist_ok=False)
    records, excluded_total = [], 0
    for row in index['records']:
        old = contained(source, f"{row['project']}-{row['bug_id']}")
        for name, expected_hash in read_json(old/'checksums.json').items():
            if sha256(contained(old,name)) != expected_hash:
                raise ValueError('Original v3 artifact changed: '+name)
        manifest, metadata = read_json(old/'context-manifest.json'), read_json(old/'prepare-metadata.json')
        validate(manifest, metadata, (old/'prompt.md').read_bytes(), (old/'targets.json').read_bytes(),
                 (old/'prepare-policy.json').read_bytes(), require_eligible=True)
        document = read_json(old/'targets.json')
        targets, excluded = select(document['targets'], POLICY_V5)
        folder = destination/old.name
        folder.mkdir()
        for entry in manifest['source_files']:
            path = contained(folder/'fixed-source', entry['path'])
            path.parent.mkdir(parents=True, exist_ok=True)
            path.write_bytes(contained(old/'fixed-source',entry['path']).read_bytes())
        for name in ('context-manifest.json','revision-proof.json'):
            (folder/name).write_bytes((old/name).read_bytes())
        prepared = compose(folder, classes=metadata['target_classes'], targets=targets, policy=POLICY_V6,
            modified_sources=metadata['fixed_source_sha256'], fixture_classes=document['fixture_classes'],
            fixture_recipe=recipe, previous_hashes={'v3_prompt_sha256':metadata['prompt_sha256'],
                'v3_targets_sha256':metadata['targets_sha256'],'v3_index_sha256':sha256(source/'index.json')})
        (folder/'capability-exclusions.json').write_bytes(encoded({'fixture_policy_id':POLICY_V5,
            'selection_basis':'Predeclared current structural recipes; no buggy outcomes; not final semantic acceptance',
            'selected':clean_targets(targets),'excluded':excluded,'team_approval':False}))
        (folder/'checksums.json').write_bytes(encoded({p.relative_to(folder).as_posix():sha256(p)
            for p in sorted(folder.rglob('*')) if p.is_file()}))
        records.append({'project':row['project'],'bug_id':row['bug_id'],'owner':row['owner'],
                        'capability_exclusion_count':len(excluded),**prepared})
        excluded_total += len(excluded)
    result = {'schema_version':6,'scope':'20-bug current-runtime development candidate only',
        'records':records,'policy_sha256':digest(encoded(POLICY_V6)),
        'source_v3_index_sha256':sha256(source/'index.json'), 'builder_sha256':sha256(__file__),
        'runtime_source_sha256':runtime,'target_count':sum(r['target_count'] for r in records),
        'capability_exclusion_count':excluded_total,'required_common_declarations':691,
        'max_prompt_utf8_bytes':max(r['prompt_utf8_bytes'] for r in records),
        'generation_ready':False,'primary':False,'team_approval':False,'live_requests':0,'queue_mutations':0}
    if result['target_count']+excluded_total != 691:
        raise ValueError('Common declaration partition differs')
    (destination/'index.json').write_bytes(encoded(result))
    return result


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--input',type=Path,required=True)
    parser.add_argument('--output',type=Path,required=True)
    args = parser.parse_args()
    result = build(args.input,args.output)
    print(json.dumps({k:result[k] for k in ('target_count','capability_exclusion_count','max_prompt_utf8_bytes','generation_ready')}))


if __name__=='__main__':
    main()
