"""Immutable bounded Graphics2D intake. Shared verification is a separate gate."""
import json
from .common import ROOT, sha256
from .preparation import encoded, digest, clean_targets
from .joint_recipe_v10 import git_bytes

BASE = '6c0f6328788f56e3ac2dc52f0e3520b820892625'
V11 = 'output/api854-20261003/prepare-v11-chronology-development-v3'
CHAMP = '8d9295e6c238ce6e2f9c2014932207ef2f036663'
BEAM = 'db74f11789719b9ae7bbc7b5b64416ed535d4e36'
CANDIDATE = 'output/api854-20261003/graphics-development-v3'
INTAKE = ROOT/'output/api854-20261003/aom-graphics-v12-intake-v1'
PAIR = 'output/api854-20261003/aom-continuation-v11-development-v3'
FIELDS = ('class','constructor_types','method','parameter_types')
RECEIVER = 'org.jfree.chart.renderer.category.AreaRenderer'
OWNER = 'org.jfree.chart.renderer.category.AbstractCategoryItemRenderer'
from .fixture_policy import GRAPHICS_SIGNATURES
SIGNATURES = {t[2]:t[3] for t in sorted(GRAPHICS_SIGNATURES)}


def contract():
    raw = git_bytes(BEAM,'output/api854-20261003/beam-graphics-review-v1/joint-verdict.json')
    if digest(raw) != 'afd02f15e34195b129f9620ad5f6e91e45e4a804e021274e5508eb4f45716b84':
        raise ValueError('Joint Graphics contract hash differs')
    joint = json.loads(raw)
    if not joint['joint_acceptance_complete'] or joint['shared_integration_approved'] or joint['new_shared_preparation_authorized_by_receipt']:
        raise ValueError('Require bounded joint candidate scope, not implied shared acceptance')
    targets = clean_targets([r['target'] for r in joint['candidates']])
    expected = clean_targets([dict(zip(FIELDS,(RECEIVER,'',m,p))) for m,p in SIGNATURES.items()])
    if targets != expected or any(r['exact_declaring_class'] != OWNER or r['accepted_into_shared_inputs'] for r in joint['candidates']):
        raise ValueError('Exact inherited identities differ')
    return {'targets':targets,'candidates':joint['candidates'],'shared_integration_approved':False,
            'joint_sha256':digest(raw),'gate_a_passed':False}


def received_refs():
    return {
      'joint-verdict.json':(BEAM,'output/api854-20261003/beam-graphics-review-v1/joint-verdict.json'),
      'integration-requirements.json':(BEAM,'output/api854-20261003/beam-graphics-review-v1/integration-requirements.json'),
      'candidate-receipt.json':(CHAMP,CANDIDATE+'/receipt.json'),
      'candidate-checksums.json':(CHAMP,CANDIDATE+'/checksums.json'),
      'candidate-verifier.py':(CHAMP,'scripts/study/api854/verify_graphics_development.py'),
      'candidate-probe.java':(CHAMP,'scripts/study/api854/development/graphics/Graphics2DProbe.java'),
      'candidate-trace.java':(CHAMP,'scripts/study/api854/development/graphics/GraphicsEntryTrace.java'),
      'v10-champ-acceptance.json':('1b0bcbceecbbf898cf908806e0d7820360db0710','output/api854-20261003/champ-beam-v10-acceptance-v1/receipt.json'),
      'v10-beam-return.json':('f708595d9fac74a603f8d5d6cb9bf0485f34c561','output/api854-20261003/beam-champfd-v10-return-v1/receipt.json')}

def receive():
    c = contract()
    INTAKE.mkdir(parents=True,exist_ok=False)
    refs = received_refs()
    bindings = {}
    for name,(commit,path) in refs.items():
        raw = git_bytes(commit,path); (INTAKE/name).write_bytes(raw)
        bindings[name] = {'commit':commit,'path':path,'sha256':digest(raw)}
    candidate = json.loads((INTAKE/'candidate-receipt.json').read_bytes())
    if (bindings['candidate-receipt.json']['sha256'] != 'c8533f8185479b556bb233be1b0d602647279687e2e30e08dbba0a94c8a19232'
        or candidate['cases_per_revision'] != 24 or candidate['exact_declarations_entered_per_revision'] != 7):
        raise ValueError('Candidate evidence differs')
    champ = json.loads((INTAKE/'v10-champ-acceptance.json').read_bytes())
    beam = json.loads((INTAKE/'v10-beam-return.json').read_bytes())
    if (champ['aom_commit'] != beam['aom_commit'] or champ['four_consumer_combinations_received'] != 80
        or beam['consumer_combinations_verified'] != 80 or not champ['beam_scoped_host_acceptance_received']
        or not beam['peer_bounded_observations_match_beam'] or champ['gate_a_passed'] or beam['gate_a_approved']):
        raise ValueError('Historical scoped v10 receipt differs')
    result = {**c,'received':bindings,'historical_v10_scoped_wait_closed':True,
        'v10_acceptance_transfers_to_v11_or_v12':False,'active_preparation_unchanged':True,
        'new_shared_preparation_authorized':False,'primary_added':0,'live_requests':0,'queue_mutations':0}
    (INTAKE/'receipt.json').write_bytes(encoded(result))
    (INTAKE/'producer.py').write_bytes(__import__('pathlib').Path(__file__).read_bytes())
    (INTAKE/'checksums.json').write_bytes(encoded({p.name:sha256(p) for p in INTAKE.iterdir() if p.is_file()}))
    return result


def load_intake():
    c=contract()
    receipt=json.loads((INTAKE/'receipt.json').read_bytes())
    hashes=json.loads((INTAKE/'checksums.json').read_bytes())
    names={p.name for p in INTAKE.iterdir() if p.is_file() and p.name!='checksums.json'}
    expected=received_refs()
    canonical_names=set(expected)|{'producer.py','receipt.json'}
    if set(hashes)!=canonical_names or names!=canonical_names or set(receipt['received'])!=set(expected):
        raise ValueError('Complete bounded intake inventory required')
    for name,h in hashes.items():
        if name not in names or sha256(INTAKE/name)!=h: raise ValueError('Intake checksum differs')
    if receipt['targets']!=c['targets'] or receipt['joint_sha256']!=c['joint_sha256']:
        raise ValueError('Current exact contract differs')
    for name,ref in receipt['received'].items():
        if (ref['commit'],ref['path'])!=expected[name]: raise ValueError('Canonical immutable intake reference differs')
        raw=git_bytes(ref['commit'],ref['path'])
        if digest(raw)!=ref['sha256'] or (INTAKE/name).read_bytes()!=raw:
            raise ValueError('Received immutable Git bytes differ')
    verifier=git_bytes(CHAMP,'scripts/study/api854/verify_graphics_development.py')
    if (INTAKE/'candidate-verifier.py').read_bytes()!=verifier:
        raise ValueError('Accepted analytic checker bytes differ')
    if receipt['shared_integration_approved'] or receipt['new_shared_preparation_authorized'] or receipt['gate_a_passed']:
        raise ValueError('Bounded intake cannot authorize shared/primary/live work')
    return receipt


if __name__ == '__main__':
    print({'received':len(receive()['received']),'v10_scoped_wait_closed':True,'shared_integration_approved':False})
