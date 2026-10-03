"""Receive immutable component decisions; they authorize only prospective development."""
import argparse
import copy
import json
import subprocess
from pathlib import Path

from .common import ROOT, contained, read_json, sha256
from .preparation import encoded, digest, clean_targets

BEAM = 'bcb63277a24d4cb3141139ed946de5f3f82f5bd4'
CHAMP = '1a28deea84ffd346a86e79d5a5c1122266fb2237'
INTAKE = ROOT/'output/api854-20261003/aom-joint-v10-intake-v1'
V9 = 'output/api854-20261003/prepare-v9-twenty-bug-development'
V9_PROTOCOL = 'output/api854-20261003/aom-continuation-v9-integrated'
BEAM_PACKET = 'output/api854-20261003/beam-final-recipe-return-v1'
CHAMP_PACKET = 'output/api854-20261003/champ-six-message-joint-review-v3'


def git_bytes(commit, path):
    return subprocess.check_output(['git', '-C', str(ROOT), 'show', commit+':'+path])


def git_json(commit, path):
    return json.loads(git_bytes(commit, path))


def validate_lang(beam, champ):
    keys = ('beam_commit', 'aom_review_receipt_sha256', 'beam_runtime_source_sha256',
            'fixed_source_sha256', 'reference_observations_sha256', 'reference_suite_sha256',
            'reference_coverage_sha256')
    if any(beam.get(k) != champ.get(k) for k in keys):
        raise ValueError('Lang peer source/reference/runtime bindings differ')
    expected = clean_targets([{'class':'org.apache.commons.lang3.math.NumberUtils',
        'constructor_types':'', 'method':method, 'parameter_types':types}
        for method,types in [('isAllZeros','java.lang.String'),('validateArray','java.lang.Object')]])
    for doc, field in [(beam,'beam_joint_verdict'),(champ,'champ_joint_verdict')]:
        if (doc.get('example_only') is not False or len(doc['candidates']) != 2
                or clean_targets([r['target'] for r in doc['candidates']]) != expected
                or any(r['target'].get('project') != 'Lang' or r['target'].get('bug_id') != 1
                       for r in doc['candidates'])
                or any(not str(r.get(field)).startswith('accepted_for_') for r in doc['candidates'])):
            raise ValueError('Require actual Lang acceptance for exactly two bounded private helpers')
        for candidate in doc['candidates']:
            for evidence in candidate['evidence']:
                if digest(git_bytes(evidence.get('commit', BEAM), evidence['path'])) != evidence['sha256']:
                    raise ValueError('Lang per-candidate evidence binding differs')
    return expected


def verify_manifest(commit, base):
    hashes = git_json(commit, base+'/checksums.json')
    for name, expected in hashes.items():
        contained(ROOT, base+'/'+name)
        if digest(git_bytes(commit, base+'/'+name)) != expected:
            raise ValueError('Received checksum differs: '+base+'/'+name)
    return len(hashes)


def receive(output=INTAKE):
    output = Path(output).resolve()
    counts = {BEAM_PACKET:verify_manifest(BEAM,BEAM_PACKET), CHAMP_PACKET:verify_manifest(CHAMP,CHAMP_PACKET)}
    index = git_json(BEAM, BEAM_PACKET+'/final-recipe-return-index.json')
    refs = {}
    for group in index['groups']:
        ref = group['receipt']
        if digest(git_bytes(BEAM,ref['path'])) != ref['sha256']:
            raise ValueError('Indexed Beam component hash differs')
        refs[group['group']] = ref
    beam_lang = git_json(BEAM,refs['Lang']['path'])
    champ_lang = git_json(CHAMP,CHAMP_PACKET+'/champ-lang-verdict.json')
    lang = validate_lang(beam_lang,champ_lang)
    beam_buffer = git_json(BEAM,refs['Buffer/Csv']['path'])
    champ_buffer = git_json(CHAMP,CHAMP_PACKET+'/champ-buffer-verdict.json')
    received = champ_buffer['received_beam_verdict']
    if (received['sha256'] != refs['Buffer/Csv']['sha256']
            or clean_targets([r['target'] for r in beam_buffer['candidates']]) !=
               clean_targets([r['target'] for r in champ_buffer['candidates']])
            or len(beam_buffer['candidates']) != 8 or not champ_buffer['joint_acceptance_complete']):
        raise ValueError('Buffer scope or received Beam decision differs')
    for a,b in zip(beam_buffer['candidates'],champ_buffer['candidates']):
        if (not str(a['beam_verdict']).startswith('accepted_for_')
                or not str(b['champ_verdict']).startswith('accepted_for_')
                or any(a[k] != b[k] for k in ('target','preconditions','meaningful_oracle',
                                              'fixed_source_sha256','reference_suite_sha256'))):
            raise ValueError('Buffer bounded domain/oracle acceptance differs')
    proposed = beam_buffer['csv_stream_condition_change']['beam_proposed_condition']
    csv = champ_buffer['csv_stream_condition_change']
    if (csv['champ_verdict'] != 'accepted_as_explicit_prospective_condition_change'
            or any(csv['agreed_condition'].get(k) != v for k,v in proposed.items())):
        raise ValueError('Csv stream condition lacks matching explicit acceptance')
    setter = git_json(CHAMP,CHAMP_PACKET+'/champ-setter-jdom-verdict.json')
    if (len(setter['accepted_candidates']) != 2
            or any(not str(r['beam_verdict']).startswith('accepted_for_')
                   or not str(r['champ_verdict']).startswith('accepted_for_') for r in setter['accepted_candidates'])):
        raise ValueError('Setter/JDOM component verdict incomplete')
    math = git_json(BEAM,refs['Math']['path'])
    if math['accepted_count'] != 2 or any(r['champ_verdict'] != 'accepted_for_shared_composition' for r in math['accepted_candidates']):
        raise ValueError('Math exact acceptance missing')
    decisions = {'beam-lang-verdict.json':(BEAM,refs['Lang']['path']),
        'champ-lang-verdict.json':(CHAMP,CHAMP_PACKET+'/champ-lang-verdict.json'),
        'beam-buffer-verdict.json':(BEAM,refs['Buffer/Csv']['path']),
        'champ-buffer-verdict.json':(CHAMP,CHAMP_PACKET+'/champ-buffer-verdict.json'),
        'beam-setter-jdom-verdict.json':(BEAM,refs['setter/JDOM']['path']),
        'champ-setter-jdom-verdict.json':(CHAMP,CHAMP_PACKET+'/champ-setter-jdom-verdict.json'),
        'math-acceptance.json':(BEAM,refs['Math']['path']),
        'beam-return-index.json':(BEAM,BEAM_PACKET+'/final-recipe-return-index.json'),
        'v9-index.json':(CHAMP,V9+'/index.json'),
        'v9-protocol.json':(CHAMP,V9_PROTOCOL+'/protocol.proposal.json'),
        'v9-runner-plan.json':(CHAMP,V9_PROTOCOL+'/runner-plan.json')}
    output.mkdir(parents=True,exist_ok=False)
    (output/'receive-producer.py').write_bytes(Path(__file__).read_bytes())
    bindings = {}
    for name,(commit,path) in decisions.items():
        raw = git_bytes(commit,path)
        target = output/'received'/name; target.parent.mkdir(exist_ok=True)
        target.write_bytes(raw)
        bindings[name] = {'commit':commit,'path':path,'sha256':digest(raw)}
    combined_lang = []
    for a in beam_lang['candidates']:
        b = next(r for r in champ_lang['candidates'] if r['target']==a['target'])
        combined_lang.append({'target':a['target'], 'beam_verdict':a['beam_joint_verdict'],
            'champ_verdict':b['champ_joint_verdict'], 'beam_preconditions':a['beam_proposed_preconditions'],
            'champ_preconditions':b['agreed_preconditions'], 'beam_oracle':a['beam_proposed_oracle'],
            'champ_oracle':b['agreed_exception_oracle'], 'scope':'private reflective helper; bounded null/String/int[] only'})
    receipt = {'schema_version':1,'status':'component_verdicts_received_for_prospective_v10_composition',
        'source_bindings':bindings,'manifest_entries_verified':counts,
        'buffer_targets':clean_targets([r['target'] for r in beam_buffer['candidates']]),
        'lang_targets':lang,'lang_combined_component_verdicts':combined_lang,
        'csv_agreed_condition':csv['agreed_condition'],'preserve_v9_selected':380,
        'accepted_delta_count':10,'expected_selected':390,'expected_exclusions':301,'denominator':691,
        'setter_jdom_new_counts':0,'recipe_component_acceptance_complete':True,
        'final_condition_semantic_host_approved':False,'gate_a_approved':False,'final_reserve':None,
        'live_kku_requests':0,'queue_mutations':0,'primary_results_added':0,
        'receiver_source_runtime':'Historical experiment bytes are not relabeled as the new composed runtime',
        'producer_sha256':sha256(__file__)}
    (output/'receipt.json').write_bytes(encoded(receipt))
    (output/'checksums.json').write_bytes(encoded({p.relative_to(output).as_posix():sha256(p)
        for p in sorted(output.rglob('*')) if p.is_file()}))
    return receipt


def load_intake():
    for name,h in read_json(INTAKE/'checksums.json').items():
        if sha256(contained(INTAKE,name)) != h:
            raise ValueError('Intake byte binding changed')
    receipt = read_json(INTAKE/'receipt.json')
    if sha256(INTAKE/'receive-producer.py') != receipt['producer_sha256']:
        raise ValueError('Original receive producer snapshot differs')
    for name,ref in receipt['source_bindings'].items():
        if (sha256(INTAKE/'received'/name) != ref['sha256']
                or digest(git_bytes(ref['commit'],ref['path'])) != ref['sha256']):
            raise ValueError('Intake immutable Git binding differs')
    validate_lang(read_json(INTAKE/'received/beam-lang-verdict.json'),
                  read_json(INTAKE/'received/champ-lang-verdict.json'))
    return receipt


if __name__ == '__main__':
    parser=argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--output',type=Path,default=INTAKE)
    args=parser.parse_args()
    result=receive(args.output)
    print({k:result[k] for k in ('status','accepted_delta_count','expected_selected','expected_exclusions')})
