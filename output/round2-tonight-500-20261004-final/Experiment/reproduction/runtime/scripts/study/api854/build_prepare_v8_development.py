"""Compose only the two accepted Math field additions into new shared development inputs."""
import argparse
import json
from pathlib import Path

from .common import ROOT, contained, read_json, sha256, implementation_hashes
from .preparation import POLICY_V8, FRACTION_FACTORY_SOURCES, compose, encoded, digest, validate, clean_targets, explicit_scope
from .fixture_policy import POLICY_V5, POLICY_V6, select, recipe_document

ACCEPTANCE = 'output/api854-provider-preflight-20261003/champ-math-field-candidate-acceptance-v1.json'
ACCEPTANCE_SHA256 = '840c7e38050e5dbb3d696378eb71127271e6b794915ebe6898af3ae6708e3a08'
PACKET = 'docs/api854/evidence/beam-champ-math-field-20261003-attempt2'


def receive():
    """Validate the immutable Champ decision and every retained Beam packet byte."""
    if sha256(ROOT/ACCEPTANCE) != ACCEPTANCE_SHA256:
        raise ValueError('Champ acceptance receipt differs from 4c9ccf7e')
    receipt = read_json(ROOT/ACCEPTANCE)
    packet = ROOT/PACKET
    for name,expected in read_json(packet/'checksums.json').items():
        if sha256(contained(packet,name)) != expected:
            raise ValueError('Beam candidate evidence changed: '+name)
    pairs = {'candidate_probe_sha256':'helper-src/algorithms/java/SqaProbe.java',
             'suite_sha256':'package/suite.tar.bz2','result_sha256':'evaluation/record.json'}
    if any(sha256(packet/name) != receipt[key] for key,name in pairs.items()):
        raise ValueError('Accepted helper/suite/result binding differs')
    expected_targets = clean_targets([{'class':'org.apache.commons.math3.fraction.'+short,
        'constructor_types':'double','method':'getField','parameter_types':''} for short in ('BigFraction','Fraction')])
    if (receipt['accepted_count'] != 2 or clean_targets([r['target'] for r in receipt['accepted_candidates']]) != expected_targets
            or any(r['champ_verdict'] != 'accepted_for_shared_composition' for r in receipt['accepted_candidates'])):
        raise ValueError('Require exactly two accepted Math signatures')
    for path,expected in FRACTION_FACTORY_SOURCES.items():
        if sha256(packet/'supplemental-fixed-source'/Path(path).name) != expected:
            raise ValueError('Reviewed production factory bytes differ')
    return receipt


def build(source, destination):
    source, destination = Path(source).resolve(), Path(destination).resolve()
    receipt = receive()
    index = read_json(source/'index.json')
    core = read_json(ROOT/'experiments/configs/api854-20261003/protocol.core-frozen.json')
    identities = {(r['project'],r['bug_id'],r['owner']) for r in index['records']}
    expected = {(r['project'],r['bug_id'],r['owner']) for r in core['pilot_bugs']}
    if len(index['records']) != 20 or identities != expected or {(p,b) for p,b,o in identities} != explicit_scope(POLICY_V8):
        raise ValueError('Require exact 20-bug pilot inventory')
    runtime = implementation_hashes()
    recipe = recipe_document(runtime,POLICY_V6)
    destination.mkdir(parents=True,exist_ok=False)
    records,excluded_total,additions = [],0,[]
    for row in index['records']:
        old = contained(source,f"{row['project']}-{row['bug_id']}")
        for name,expected_hash in read_json(old/'checksums.json').items():
            if sha256(contained(old,name)) != expected_hash:
                raise ValueError('Original v3 artifact changed: '+name)
        manifest,metadata = read_json(old/'context-manifest.json'),read_json(old/'prepare-metadata.json')
        validate(manifest,metadata,(old/'prompt.md').read_bytes(),(old/'targets.json').read_bytes(),
                 (old/'prepare-policy.json').read_bytes(),require_eligible=True)
        document = read_json(old/'targets.json')
        baseline,_ = select(document['targets'],POLICY_V5)
        targets,excluded = select(document['targets'],POLICY_V6)
        delta = [t for t in clean_targets(targets) if t not in clean_targets(baseline)]
        if delta and (row['project'],row['bug_id']) != ('Math',1):
            raise ValueError('Unaccepted declaration outside Math-1')
        additions.extend(delta)
        folder = destination/old.name
        folder.mkdir()
        manifest['selection_policy_id'] = POLICY_V8['context_policy_id']
        for entry in manifest['source_files']:
            path = contained(folder/'fixed-source',entry['path'])
            path.parent.mkdir(parents=True,exist_ok=True)
            path.write_bytes(contained(old/'fixed-source',entry['path']).read_bytes())
        if (row['project'],row['bug_id']) == ('Math',1):
            for candidate in receipt['accepted_candidates']:
                short = candidate['target']['class'].rsplit('.',1)[1]
                path = f'src/main/java/org/apache/commons/math3/fraction/{short}.java'
                if metadata['fixed_source_sha256'].get(path) != candidate['target_source_sha256']:
                    raise ValueError('Accepted target source differs from Math fixed revision')
            for path,h in FRACTION_FACTORY_SOURCES.items():
                data = (ROOT/PACKET/'supplemental-fixed-source'/Path(path).name).read_bytes()
                target = contained(folder/'fixed-source',path)
                target.parent.mkdir(parents=True,exist_ok=True)
                with target.open('xb') as stream:
                    stream.write(data)
                manifest['source_files'].append({'path':path,'sha256':h,'bytes':len(data)})
        manifest['source_files'] = sorted(manifest['source_files'],key=lambda r:r['path'])
        manifest['source_hash'] = digest(json.dumps(manifest['source_files'],sort_keys=True,separators=(',',':')).encode())
        (folder/'context-manifest.json').write_bytes(encoded(manifest))
        (folder/'revision-proof.json').write_bytes((old/'revision-proof.json').read_bytes())
        prepared = compose(folder,classes=metadata['target_classes'],targets=targets,policy=POLICY_V8,
            modified_sources=metadata['fixed_source_sha256'],fixture_classes=document['fixture_classes'],fixture_recipe=recipe,
            previous_hashes={'v3_prompt_sha256':metadata['prompt_sha256'],'v3_targets_sha256':metadata['targets_sha256'],
                             'v3_index_sha256':sha256(source/'index.json'),'champ_acceptance_sha256':ACCEPTANCE_SHA256})
        (folder/'capability-exclusions.json').write_bytes(encoded({'fixture_policy_id':POLICY_V6,
            'selection_basis':'v5 capability subset plus exactly two accepted getField signatures; no buggy outcomes',
            'selected':clean_targets(targets),'excluded':excluded,'accepted_additions':delta,
            'acceptance_sha256':ACCEPTANCE_SHA256,'team_approval':False}))
        (folder/'checksums.json').write_bytes(encoded({p.relative_to(folder).as_posix():sha256(p)
            for p in sorted(folder.rglob('*')) if p.is_file()}))
        records.append({'project':row['project'],'bug_id':row['bug_id'],'owner':row['owner'],
                        'capability_exclusion_count':len(excluded),**prepared})
        excluded_total += len(excluded)
    if clean_targets(additions) != clean_targets([r['target'] for r in receipt['accepted_candidates']]):
        raise ValueError('Shared additions differ from the exact accepted set')
    result = {'schema_version':8,'scope':'20-bug development composition, two accepted Math additions only',
        'records':records,'policy_sha256':digest(encoded(POLICY_V8)),
        'source_v3_index_sha256':sha256(source/'index.json'),'builder_sha256':sha256(__file__),
        'accepted_additions':clean_targets(additions),'acceptance':{'path':ACCEPTANCE,'sha256':ACCEPTANCE_SHA256},
        'runtime_source_sha256':runtime,'target_count':sum(r['target_count'] for r in records),
        'capability_exclusion_count':excluded_total,'required_common_declarations':691,
        'max_prompt_utf8_bytes':max(r['prompt_utf8_bytes'] for r in records),
        'generation_ready':False,'primary':False,'team_approval':False,'live_requests':0,'queue_mutations':0}
    if (result['target_count'],excluded_total) != (379,312):
        raise ValueError('Common declaration partition differs from accepted increment')
    (destination/'index.json').write_bytes(encoded(result))
    return result


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--input',type=Path,required=True)
    parser.add_argument('--output',type=Path,required=True)
    args = parser.parse_args()
    result = build(args.input,args.output)
    print(json.dumps({k:result[k] for k in ('target_count','capability_exclusion_count','max_prompt_utf8_bytes','generation_ready')}))
