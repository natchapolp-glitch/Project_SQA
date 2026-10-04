"""Compose accepted exact deltas on immutable v9; never modify historical artifacts."""
import argparse
import json
from pathlib import Path

from .common import ROOT, contained, read_json, sha256, implementation_hashes
from .preparation import POLICY_V10, compose, encoded, digest, clean_targets, explicit_scope
from .fixture_policy import POLICY_V10 as FIXTURE, select, recipe_document
from .joint_recipe_v10 import CHAMP, V9, INTAKE, git_bytes, git_json, load_intake


def build(destination):
    destination = Path(destination).resolve()
    receipt = load_intake()
    index = git_json(CHAMP,V9+'/index.json')
    expected = {(r['project'],r['bug_id'],r['owner']) for r in
        read_json(ROOT/'experiments/configs/api854-20261003/protocol.core-frozen.json')['pilot_bugs']}
    identities = {(r['project'],r['bug_id'],r['owner']) for r in index['records']}
    if (len(index['records'])!=20 or identities!=expected
            or {(p,b) for p,b,o in identities}!=explicit_scope(POLICY_V10)
            or (index['target_count'],index['capability_exclusion_count'])!=(380,311)):
        raise ValueError('Require immutable v9 exact inventory and 380/311 partition')
    runtime = implementation_hashes()
    recipe = recipe_document(runtime,FIXTURE)
    destination.mkdir(parents=True,exist_ok=False)
    records, additions = [], []
    for row in index['records']:
        name = f"{row['project']}-{row['bug_id']}"
        base = V9+'/'+name
        old = {p:git_bytes(CHAMP,base+'/'+p) for p in git_json(CHAMP,base+'/checksums.json')}
        if any(digest(old[p])!=h for p,h in git_json(CHAMP,base+'/checksums.json').items()):
            raise ValueError('Immutable v9 checksum differs')
        manifest, metadata = json.loads(old['context-manifest.json']),json.loads(old['prepare-metadata.json'])
        if any(row.get(k)!=v for k,v in metadata.items()):
            raise ValueError('Historical v9 index metadata differs')
        discovery = ROOT/'output/api854-20261003/prepare-v3'/name
        discovered = read_json(discovery/'targets.json')
        original = read_json(discovery/'prepare-metadata.json')
        if original['fixed_source_sha256']!=metadata['fixed_source_sha256']:
            raise ValueError('v9 modified-source denominator binding differs')
        targets, _ = select(discovered['targets'],FIXTURE)
        before = json.loads(old['targets.json'])['targets']
        if any(t not in clean_targets(targets) for t in clean_targets(before)):
            raise ValueError('An accepted v9 declaration was removed')
        delta = [t for t in clean_targets(targets) if t not in clean_targets(before)]
        allowed = receipt['buffer_targets'] if name in ('JacksonCore-1','Csv-1') else receipt['lang_targets'] if name=='Lang-1' else []
        if any(t not in allowed for t in delta):
            raise ValueError('Unaccepted delta outside reviewed project/signature')
        additions.extend(delta)
        previous_exclusions = json.loads(old['capability-exclusions.json'])['excluded']
        exclusions = [r for r in previous_exclusions if r['target'] not in delta]
        if len(targets)+len(exclusions)!=len(discovered['targets']):
            raise ValueError('Selected/excluded partition differs from discovery')
        folder = destination/name; folder.mkdir()
        for entry in manifest['source_files']:
            raw = old['fixed-source/'+entry['path']]
            if digest(raw)!=entry['sha256'] or len(raw)!=entry['bytes']:
                raise ValueError('v9 context source binding differs')
            path = contained(folder/'fixed-source',entry['path']);path.parent.mkdir(parents=True,exist_ok=True)
            path.write_bytes(raw)
        manifest.update(selection_policy_id=POLICY_V10['context_policy_id'],
            derived_from_manifest_sha256=digest(old['context-manifest.json']))
        (folder/'context-manifest.json').write_bytes(encoded(manifest))
        (folder/'revision-proof.json').write_bytes(old['revision-proof.json'])
        result = compose(folder,classes=metadata['target_classes'],targets=targets,policy=POLICY_V10,
            modified_sources=metadata['fixed_source_sha256'],fixture_classes=discovered['fixture_classes'],fixture_recipe=recipe,
            previous_hashes={'v3_index_sha256':sha256(discovery.parent/'index.json'),
                'v9_index_sha256':digest(git_bytes(CHAMP,V9+'/index.json')),
                'v9_prompt_sha256':digest(old['prompt.md']), 'v9_targets_sha256':digest(old['targets.json']),
                'joint_receipt_sha256':sha256(INTAKE/'receipt.json')})
        (folder/'capability-exclusions.json').write_bytes(encoded({'fixture_policy_id':FIXTURE,
            'selection_basis':'Exact jointly accepted component deltas on immutable v9; no buggy outcome selection',
            'selected':clean_targets(targets),'excluded':exclusions,'accepted_additions':delta,
            'joint_receipt_sha256':sha256(INTAKE/'receipt.json'),'team_approval':False}))
        (folder/'checksums.json').write_bytes(encoded({p.relative_to(folder).as_posix():sha256(p)
            for p in sorted(folder.rglob('*')) if p.is_file()}))
        records.append({'project':row['project'],'bug_id':row['bug_id'],'owner':row['owner'],
            'capability_exclusion_count':len(exclusions),**result})
    if clean_targets(additions)!=clean_targets(receipt['buffer_targets']+receipt['lang_targets']):
        raise ValueError('Composition delta differs from exact jointly accepted identities')
    result = {'schema_version':10,'scope':'Prospective jointly accepted recipes; final condition semantic/host/provider review pending',
        'records':records,'policy_sha256':digest(encoded(POLICY_V10)), 'builder_sha256':sha256(__file__),
        'source_v9':{'commit':CHAMP,'path':V9+'/index.json','sha256':digest(git_bytes(CHAMP,V9+'/index.json'))},
        'accepted_additions':clean_targets(additions),'joint_receipt':{'path':(INTAKE/'receipt.json').relative_to(ROOT).as_posix(),
            'sha256':sha256(INTAKE/'receipt.json')},'runtime_source_sha256':runtime,
        'target_count':sum(r['target_count'] for r in records),
        'capability_exclusion_count':sum(r['capability_exclusion_count'] for r in records),
        'required_common_declarations':691,'max_prompt_utf8_bytes':max(r['prompt_utf8_bytes'] for r in records),
        'generation_ready':False,'primary':False,'team_approval':False,'live_requests':0,'queue_mutations':0}
    if (result['target_count'],result['capability_exclusion_count'])!=(390,301):
        raise ValueError('Expected exact 390/301 partition')
    (destination/'index.json').write_bytes(encoded(result))
    return result


if __name__=='__main__':
    parser=argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--output',type=Path,required=True)
    args=parser.parse_args()
    result=build(args.output)
    print({k:result[k] for k in ('target_count','capability_exclusion_count','max_prompt_utf8_bytes','generation_ready')})
