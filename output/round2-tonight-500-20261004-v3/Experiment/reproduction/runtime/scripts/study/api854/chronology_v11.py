"""Exact immutable joint contract for shared Chronology development, not final approval."""
from pathlib import Path
import json

from .common import ROOT, sha256
from .preparation import encoded, digest, clean_targets
from .fixture_policy import CHRONOLOGY_SIGNATURES
from .joint_recipe_v10 import git_bytes

CHAMP = '7de14726198886e43d8eb6b8a917f58500acab93'
BEAM = '8bde1b80b16f57e1cbae5d86dc20e52715621746'
JOINT = 'output/api854-20261003/champ-final-recipe-joint-return-v1/champ-chronology-joint-verdict.json'
REQUIREMENTS = 'output/api854-20261003/beam-champ7de-integration-review-v1/integration-requirements.json'
INTAKE = ROOT/'output/api854-20261003/aom-chronology-v11-intake-v1'
V10_COMMIT = 'a4880fb2fde574e77705841f62f302273be7dcd9'
V10 = 'output/api854-20261003/prepare-v10-joint-development'


def receive_contract(write=True):
    blobs = {'joint-verdict.json':(CHAMP,JOINT), 'integration-requirements.json':(BEAM,REQUIREMENTS)}
    raw = {name:git_bytes(*ref) for name,ref in blobs.items()}
    joint, requirements = json.loads(raw['joint-verdict.json']),json.loads(raw['integration-requirements.json'])
    if (digest(raw['joint-verdict.json'])!='14a6ae7a62d61b428be07f766a3b5075ae1712afe6a8bc2b249280f3609e54d7'
            or joint['prospective_shared_recipe_implementation_authorized'] is not True
            or joint['joint_bounded_candidate_acceptance_complete'] is not True
            or joint['shared_integration_approved'] is not False or joint['accepted_into_shared_inputs'] is not False):
        raise ValueError('Require bounded implementation authorization, not inferred shared approval')
    fields = ('class','constructor_types','method','parameter_types')
    targets = clean_targets(joint['worklist_identities'])
    if (set(tuple(t[k] for k in fields) for t in targets)!=CHRONOLOGY_SIGNATURES
            or targets!=clean_targets(requirements['worklist_identities'])
            or joint['exact_targets']!=requirements['exact_targets']):
        raise ValueError('Exact declaration/receiver/descriptor binding differs')
    for name,key in [('beam-verdict.json','received_beam_verdict'),('candidate-receipt.json','candidate_receipt')]:
        ref = joint[key]
        blobs[name]=(ref['commit'],ref['path']); raw[name]=git_bytes(*blobs[name])
        if digest(raw[name])!=ref['sha256']: raise ValueError('Received evidence Git bytes differ')
    receipt = {'targets':targets,'descriptors':joint['exact_targets'],
        'independent_expected_observations':requirements['independent_expected_observations'],
        'received':{n:{'commit':c,'path':p,'sha256':digest(raw[n])} for n,(c,p) in blobs.items()},
        'joint_scoped_candidate_authorized':True,'final_shared_team_approval':False,
        'gate_a_passed':False,'live_requests':0,'queue_mutations':0}
    if write:
        INTAKE.mkdir(parents=True,exist_ok=False)
        for name,data in raw.items(): (INTAKE/name).write_bytes(data)
        (INTAKE/'receipt.json').write_bytes(encoded(receipt))
        (INTAKE/'producer.py').write_bytes(Path(__file__).read_bytes())
        (INTAKE/'checksums.json').write_bytes(encoded({p.name:sha256(p) for p in INTAKE.iterdir() if p.is_file()}))
    return receipt


def load_contract():
    expected = receive_contract(write=False)
    for name,h in json.loads((INTAKE/'checksums.json').read_bytes()).items():
        if sha256(INTAKE/name)!=h: raise ValueError('Chronology received packet changed')
    if json.loads((INTAKE/'receipt.json').read_bytes())!=expected:
        raise ValueError('Chronology contract differs from immutable joint verdict')
    return expected


if __name__=='__main__':
    result=receive_contract()
    print({'received':len(result['received']),'targets':len(result['targets']),'gate_a_passed':False})
