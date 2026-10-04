"""Immutable jointly accepted Codec5 intake; no live/team approval transfer."""
import argparse
import json
from pathlib import Path
from .common import ROOT, sha256, contained
from .preparation import encoded, digest, clean_targets
from .joint_recipe_v10 import git_bytes

BASE = '63ad195623c2ed3f67f3ae232c00c54d3160ce72'
PREVIOUS = 'output/api854-20261003/prepare-v12-graphics-development-v1'
PAIR = 'output/api854-20261003/aom-continuation-v12-development-v1'
BEAM = 'a44f796f6b72281b0bd39d3f26bfc80519e00710'
CHAMP = 'af1fe272'
CANDIDATE = 'output/api854-20261004/beam-codec-candidate-v1'
JOINT = 'output/api854-20261004/champ-codec-joint-review-v2'
INTAKE = ROOT/'output/api854-20261004/aom-codec-v13-intake-v1'
PROOF = ROOT/'output/api854-20261004/aom-codec-v13-integration-v3'
PREP = ROOT/'output/api854-20261004/prepare-v13-codec-development-v2'
NEW_PAIR = ROOT/'output/api854-20261004/aom-continuation-v13-development-v2'
READY = ROOT/'output/api854-20261004/aom-v13-readiness-v2'


def contract():
    raw = git_bytes(CHAMP, JOINT+'/joint-verdict.json')
    if digest(raw) != 'f676f016e6f2132e4967fdb5997af9b74eef7d87b1a796ebccad6c480c970f9b':
        raise ValueError('Joint Codec verdict differs')
    verdict = json.loads(raw)
    if not verdict['joint_acceptance_complete'] or verdict['shared_integration_approved']:
        raise ValueError('Require bounded joint acceptance without shared approval')
    policy = json.loads(git_bytes(BEAM, CANDIDATE+'/policy.json'))
    targets = clean_targets([r['target'] for r in policy['targets']])
    if clean_targets([r['target'] for r in verdict['candidates']])!=targets:
        raise ValueError('Joint verdict identities differ from predeclared policy')
    for row in verdict['candidates']:
        proposed=next(r for r in policy['targets'] if r['target']==row['target'])
        if (row['beam_verdict']!='accepted_for_bounded_candidate_oracle_development'
            or row['champ_verdict']!='accepted_for_bounded_candidate_oracle_development'
            or row['descriptor']!=proposed['descriptor'] or row['declaring_class']!=proposed['declaring_class']
            or row['candidate_cases']!=sum(c['method']==row['target']['method'] for c in policy['cases'])
            or row['accepted_into_shared_inputs'] or not row['agreed_preconditions'] or not row['agreed_oracle']):
            raise ValueError('Exact joint verdict/preconditions/oracle differs')
    from .fixture_policy import CODEC_SIGNATURES
    if targets != clean_targets([dict(zip(('class','constructor_types','method','parameter_types'), t)) for t in CODEC_SIGNATURES]):
        raise ValueError('Exact Codec registry differs')
    old = json.loads(git_bytes(BASE, PREVIOUS+'/Codec-1/targets.json'))['targets']
    ex = json.loads(git_bytes(BASE, PREVIOUS+'/Codec-1/capability-exclusions.json'))['excluded']
    if len(old)!=13 or any(t in old or not any(r['target']==t for r in ex) for t in targets):
        raise ValueError('Five candidates must be disjoint v12 exclusions')
    return {'targets':targets, 'joint_sha256':digest(raw), 'policy':policy,
            'shared_integration_approved':False, 'gate_a_passed':False}


def receive():
    c = contract()
    # Validate every immutable peer packet file, including nested raw command evidence.
    counts = {}
    for commit, folder, expected_hash in [(BEAM,CANDIDATE,None),(CHAMP,JOINT,'d81d3a2ac25c62c51a29345a5079ec08fa1932dfb837af3d62c66b934bc6c447')]:
        raw = git_bytes(commit,folder+'/checksums.json')
        if expected_hash and digest(raw)!=expected_hash: raise ValueError('Joint manifest differs')
        hashes = json.loads(raw)
        for n,h in hashes.items():
            if digest(git_bytes(commit,folder+'/'+n))!=h: raise ValueError('Peer checksum differs: '+n)
        counts[folder] = len(hashes)
    r = git_bytes(CHAMP,JOINT+'/receipt.json')
    if digest(r)!='787bd56f78ff919450cfa47ac914258c5bedaba0fc58961de85d1f4711e2050e': raise ValueError('Joint receipt differs')
    refs = {'joint-verdict.json':(CHAMP,JOINT+'/joint-verdict.json'), 'joint-receipt.json':(CHAMP,JOINT+'/receipt.json'),
            'joint-checksums.json':(CHAMP,JOINT+'/checksums.json'), 'policy.json':(BEAM,CANDIDATE+'/policy.json'),
            'peer-validator.py':(BEAM,CANDIDATE+'/verify_native.py'), 'peer-probe.java':(BEAM,CANDIDATE+'/CodecCandidateProbe.java'),
            'peer-trace.java':(BEAM,CANDIDATE+'/CodecEntryTrace.java'), 'peer-cases.tsv':(BEAM,CANDIDATE+'/native-v1/cases.tsv'),
            'fixed-production-source.tar.gz':(BEAM,CANDIDATE+'/native-v1/fixed-production-archive.stdout.tar.gz'),
            'buggy-production-source.tar.gz':(BEAM,CANDIDATE+'/native-v1/buggy-production-archive.stdout.tar.gz')}
    INTAKE.mkdir(parents=True,exist_ok=False)
    bindings = {}
    for n,(commit,path) in refs.items():
        raw=git_bytes(commit,path);(INTAKE/n).write_bytes(raw)
        bindings[n]={'commit':commit,'path':path,'sha256':digest(raw)}
    result={'status':'bounded_codec_joint_candidate_received','base':BASE,'targets':c['targets'],
            'received':bindings,'peer_manifest_entries_verified':counts,'selected_before':403,'exclusions_before':288,
            'candidate_case_count':43,'shared_integration_approved':False,'gate_a_passed':False,'live_requests':0,'queue_mutations':0}
    (INTAKE/'producer.py').write_bytes(Path(__file__).read_bytes())
    (INTAKE/'receipt.json').write_bytes(encoded(result))
    (INTAKE/'checksums.json').write_bytes(encoded({p.name:sha256(p) for p in INTAKE.iterdir() if p.is_file()}))
    return result


def load_intake():
    c=contract();r=json.loads((INTAKE/'receipt.json').read_bytes());h=json.loads((INTAKE/'checksums.json').read_bytes())
    if set(h)!={p.name for p in INTAKE.iterdir() if p.is_file() and p.name!='checksums.json'}: raise ValueError('Intake inventory differs')
    if r['targets']!=c['targets'] or r['shared_integration_approved'] or r['gate_a_passed']: raise ValueError('Intake scope differs')
    for n,v in h.items():
        if sha256(contained(INTAKE,n))!=v: raise ValueError('Intake bytes differ')
    for n,ref in r['received'].items():
        raw=git_bytes(ref['commit'],ref['path'])
        if digest(raw)!=ref['sha256'] or raw!=(INTAKE/n).read_bytes():raise ValueError('Immutable provenance differs')
    return r


def validators():
    load_intake()
    source=(INTAKE/'peer-validator.py').read_text()
    code=source[source.index('def expected('):source.index('def execute(')]
    policy=json.loads((INTAKE/'policy.json').read_bytes())
    def require(ok, reason):
        if not ok: raise ValueError(reason)
    ns={'POLICY':policy,'CASES':policy['cases'],'TARGETS':{r['target']['method']:r for r in policy['targets']},
        'OWNERS':['org.apache.commons.codec.language.Metaphone','org.apache.commons.codec.language.SoundexUtils'],
        'require':require,'json':json}
    exec(code,ns)
    received_validate=ns['validate']
    def validate(rows,allow_failures=False):
        cases=received_validate(rows,allow_failures=allow_failures)
        for c,row in zip(policy['cases'],cases):
            capacity=None if c['buffer'] is None else 16+len(c['buffer'])
            if row['pre_state']['buffer_capacity']!=capacity:
                raise ValueError('Fresh default buffer capacity differs')
        return cases
    ns['validate']=validate
    return ns


if __name__=='__main__':
    print(receive()['status'])
