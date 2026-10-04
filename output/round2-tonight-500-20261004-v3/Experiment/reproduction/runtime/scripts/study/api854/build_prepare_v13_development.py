"""Compose five locally verified Codec identities on exact immutable v12."""
import argparse
import json
from pathlib import Path

from .common import ROOT, sha256, contained, implementation_hashes, read_json
from .preparation import POLICY_V13, encoded, digest, clean_targets, compose
from .fixture_policy import POLICY_V13 as FIXTURE, recipe_document, select
from .codec_v13 import INTAKE, PREVIOUS as V11, BASE, PROOF as INTEGRATION
from .joint_recipe_v10 import git_bytes




def checked_integration():
    from .verify_codec_v13 import checked
    return checked()


def build(destination):
    contract=checked_integration()
    destination=Path(destination).resolve()
    index=json.loads(git_bytes(BASE,V11+'/index.json'))
    if (len(index['records']),index['target_count'],index['capability_exclusion_count'])!=(20,403,288):
        raise ValueError('Immutable v12 inventory differs')
    runtime=implementation_hashes();recipe=recipe_document(runtime,FIXTURE)
    destination.mkdir(parents=True,exist_ok=False)
    records=[];added=[]
    for row in index['records']:
        name=f"{row['project']}-{row['bug_id']}";base=V11+'/'+name
        hashes=json.loads(git_bytes(BASE,base+'/checksums.json'))
        old={p:git_bytes(BASE,base+'/'+p) for p in hashes}
        if any(digest(old[p])!=h for p,h in hashes.items()): raise ValueError('Immutable v12 checksum differs')
        metadata=json.loads(old['prepare-metadata.json']);manifest=json.loads(old['context-manifest.json'])
        if any(row.get(k)!=v for k,v in metadata.items()): raise ValueError('v12 index metadata differs')
        discovery=read_json(ROOT/'output/api854-20261003/prepare-v3'/name/'targets.json')
        targets,_=select(discovery['targets'],FIXTURE)
        before=json.loads(old['targets.json'])['targets']
        if not all(t in clean_targets(targets) for t in before): raise ValueError('Accepted v12 identity removed')
        delta=[t for t in clean_targets(targets) if t not in before]
        if delta and (name!='Codec-1' or clean_targets(delta)!=clean_targets(contract['targets'])):
            raise ValueError('Unaccepted Codec delta')
        added.extend(delta)
        excluded=[r for r in json.loads(old['capability-exclusions.json'])['excluded'] if r['target'] not in delta]
        if len(targets)+len(excluded)!=len(discovery['targets']): raise ValueError('Discovery partition differs')
        folder=destination/name;folder.mkdir()
        for entry in manifest['source_files']:
            raw=old['fixed-source/'+entry['path']]
            if digest(raw)!=entry['sha256'] or len(raw)!=entry['bytes']: raise ValueError('v12 production context changed')
            path=contained(folder/'fixed-source',entry['path']);path.parent.mkdir(parents=True,exist_ok=True);path.write_bytes(raw)
        manifest.update(selection_policy_id=POLICY_V13['context_policy_id'],derived_from_manifest_sha256=digest(old['context-manifest.json']))
        (folder/'context-manifest.json').write_bytes(encoded(manifest))
        (folder/'revision-proof.json').write_bytes(old['revision-proof.json'])
        result=compose(folder,classes=metadata['target_classes'],targets=targets,policy=POLICY_V13,
            modified_sources=metadata['fixed_source_sha256'],fixture_classes=discovery['fixture_classes'],fixture_recipe=recipe,
            previous_hashes={**metadata['previous_hashes'],'v12_index_sha256':digest(git_bytes(BASE,V11+'/index.json')),
                'v12_prompt_sha256':digest(old['prompt.md']),'v12_targets_sha256':digest(old['targets.json']),
                'codec_intake_sha256':sha256(INTAKE/'receipt.json'),'shared_integration_receipt_sha256':sha256(INTEGRATION/'receipt.json')})
        (folder/'capability-exclusions.json').write_bytes(encoded({'fixture_policy_id':FIXTURE,'selected':clean_targets(targets),
            'excluded':excluded,'accepted_additions':delta,'selection_basis':'Exact joint bounded candidate identities after presealed shared integration; inventory not selected by buggy outcomes',
            'codec_intake_sha256':sha256(INTAKE/'receipt.json'),'team_approval':False}))
        (folder/'checksums.json').write_bytes(encoded({p.relative_to(folder).as_posix():sha256(p) for p in sorted(folder.rglob('*')) if p.is_file()}))
        records.append({'project':row['project'],'bug_id':row['bug_id'],'owner':row['owner'],'capability_exclusion_count':len(excluded),**result})
    if clean_targets(added)!=contract['targets']: raise ValueError('Exactly five identities required')
    result={'schema_version':13,'records':records,'policy_sha256':digest(encoded(POLICY_V13)),
        'builder_sha256':sha256(__file__),'source_v12':{'commit':BASE,'path':V11+'/index.json','sha256':digest(git_bytes(BASE,V11+'/index.json'))},
        'accepted_additions':clean_targets(added),'joint_receipt':{'path':(INTAKE/'receipt.json').relative_to(ROOT).as_posix(),'sha256':sha256(INTAKE/'receipt.json')},
        'shared_integration_receipt':{'path':(INTEGRATION/'receipt.json').relative_to(ROOT).as_posix(),'sha256':sha256(INTEGRATION/'receipt.json')},
        'runtime_source_sha256':runtime,'target_count':sum(r['target_count'] for r in records),
        'capability_exclusion_count':sum(r['capability_exclusion_count'] for r in records),'required_common_declarations':691,
        'max_prompt_utf8_bytes':max(r['prompt_utf8_bytes'] for r in records),'generation_ready':False,
        'primary':False,'team_approval':False,'live_requests':0,'queue_mutations':0}
    if (result['target_count'],result['capability_exclusion_count'])!=(408,283): raise ValueError('Expected 408/283 development partition')
    (destination/'index.json').write_bytes(encoded(result));return result


if __name__=='__main__':
    p=argparse.ArgumentParser(description=__doc__);p.add_argument('--output',type=Path,required=True);a=p.parse_args()
    result=build(a.output);print({k:result[k] for k in ('target_count','capability_exclusion_count','max_prompt_utf8_bytes','generation_ready')})
