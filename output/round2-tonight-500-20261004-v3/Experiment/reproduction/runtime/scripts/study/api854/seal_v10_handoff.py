"""Check final development bytes and preserve an offline handoff receipt."""
import json
import re
import subprocess

from .common import ROOT, read_json, sha256, contained, implementation_hashes
from .preparation import encoded, digest
from .joint_recipe_v10 import INTAKE, load_intake, CHAMP, V9, git_bytes, git_json
from .gate_a import inspect

PREP=ROOT/'output/api854-20261003/prepare-v10-joint-development'
PAIR=ROOT/'output/api854-20261003/aom-continuation-v10-development'
OUT=ROOT/'output/api854-20261003/aom-v10-readiness-v1'


def seal():
    receipt=load_intake();index=read_json(PREP/'index.json');protocol=read_json(PAIR/'protocol.proposal.json')
    current=implementation_hashes()
    if current!=index['runtime_source_sha256'] or current!=protocol['source_sha256']:
        raise ValueError('Final runtime source pins changed')
    for base in (INTAKE,PAIR,OUT):
        for path,h in read_json(base/'checksums.json').items():
            if sha256(contained(base,path))!=h:raise ValueError('Manifest binding changed')
    cases=read_json(OUT/'fixed-runtime-verification.json')
    if (cases['runtime_helper_sha256']!=current['algorithms/java/SqaProbe.java']
            or cases['verifier_sha256']!=sha256(ROOT/'scripts/study/api854/verify_v10_recipe_runtime.py')
            or cases['fixed_source_integration_cases']!=64 or cases['status']!='pass'
            or not cases['legacy_policy_behavior_preserved'] or not cases['temporary_setter_mutation_detected']):
        raise ValueError('Final fixed runtime integration proof differs')
    counts={}
    for name,expected in [('regression-tests.log',61),('post-review-tests.log',7)]:
        text=(OUT/name).read_text(encoding='utf-8')
        if not re.search(r'Ran '+str(expected)+r' tests in .*?\n\s*\nOK\s*$',text) or '... skipped ' in text:
            raise ValueError('Fresh test results incomplete or skipped')
        counts[name]={'tests':expected,'failures':0,'errors':0,'skipped':0,'sha256':sha256(OUT/name)}
    worksheet=read_json(OUT/'prompt-reserve-worksheet.json')
    if (len(worksheet['records'])!=40 or worksheet['protocol_sha256']!=sha256(PAIR/'protocol.proposal.json')
            or worksheet['runtime_source_sha256']!=current or worksheet['final_reserve'] is not None):
        raise ValueError('Worksheet condition binding differs')
    expected_pairs={(r['project'],r['bug_id'],a) for r in index['records'] for a in ('kku-claude','kku-gemini')}
    if {(r['project'],r['bug_id'],r['approach']) for r in worksheet['records']}!=expected_pairs:
        raise ValueError('Worksheet pair inventory differs')
    for r in worksheet['records']:
        prompt=contained(ROOT,r['prompt_path'])
        if (r['requested_model_id']!=protocol['models'][r['approach']]['id']
                or r['prompt_sha256']!=sha256(prompt) or r['prompt_utf8_bytes']!=prompt.stat().st_size
                or r['final_reserve'] is not None):raise ValueError('Worksheet model/prompt binding differs')
    gate=inspect(protocol_path=PAIR/'protocol.proposal.json',runner_path=PAIR/'runner-plan.json')
    required={'prepare_contract','protocol_runner_binding','preparation_index_binding','runtime_source_binding',
        'shared_policy','fixture_recipe_binding','runner_coverage','eligible_targets_20'}
    if gate['gate_a_passed'] or any(c['status']!='pass' for c in gate['checklist'] if c['id'] in required):
        raise ValueError('Final input binding audit failed or gate was unexpectedly opened')
    worklist=[];source_equality=[]
    for row in index['records']:
        name=f"{row['project']}-{row['bug_id']}";folder=PREP/name
        manifest=read_json(folder/'context-manifest.json');before=git_json(CHAMP,V9+'/'+name+'/context-manifest.json')
        if manifest['source_files']!=before['source_files'] or manifest['source_hash']!=before['source_hash']:
            raise ValueError('v9 fixed source knowledge changed')
        source_equality.append({'bug':name,'source_hash':manifest['source_hash'],'source_files':len(manifest['source_files'])})
        worklist.append({'project':row['project'],'bug_id':row['bug_id'],'owner':row['owner'],
            'selected':row['target_count'],'exclusions':row['capability_exclusion_count'],
            'unsupported_targets':read_json(folder/'capability-exclusions.json')['excluded'],
            'condition_semantic_review':'pending','final_host_binding':'pending'})
    preserved={}
    for base in ('output/api854-20261003/prepare-v7-twenty-bug-development',
                 'output/api854-20261003/prepare-v8-fraction-field-development',
                 'output/api854-20261003/aom-continuation-v8-development'):
        paths=subprocess.check_output(['git','-C',str(ROOT),'ls-tree','-r','--name-only','f753770d',base],text=True).splitlines()
        for path in paths:
            if (ROOT/path).read_bytes()!=git_bytes('f753770d',path):raise ValueError('Historical file was changed: '+path)
        preserved[base]=len(paths)
    (OUT/'remaining-worklist.json').write_bytes(encoded({'condition':protocol['condition'],'denominator':691,
        'selected':390,'exclusions':301,'bugs':worklist,'empty_enum_targets':4,'empty_enum_joint_decision':None,
        'chronology_candidates_not_adopted':6,'gate_a_approved':False}))
    result={'schema_version':1,'status':'aom_v10_composition_and_offline_input_checks_complete',
        'condition':protocol['condition'],'protocol_sha256':sha256(PAIR/'protocol.proposal.json'),
        'runner_plan_sha256':sha256(PAIR/'runner-plan.json'),'preparation_index_sha256':sha256(PREP/'index.json'),
        'joint_component_receipt_sha256':sha256(INTAKE/'receipt.json'),
        'current_validator_sha256':sha256(ROOT/'scripts/study/api854/joint_recipe_v10.py'),
        'sealer_sha256':sha256(__file__),'runtime_source_sha256':current,'runtime_files':len(current),
        'bugs':20,'selected':390,'exclusions':301,'denominator':691,'v9_sources_preserved':source_equality,
        'historical_files_unchanged':preserved,'four_consumer_bug_approach_combinations':80,
        'test_runs':counts,'test_count_runs_not_added_as_unique_cases':True,
        'fixed_runtime_case_count':64,'fixed_runtime_observations':128,'new_defects4j_evaluations':0,
        'fixed_runtime_proof_sha256':sha256(OUT/'fixed-runtime-verification.json'),
        'prompt_model_pairs':40,'max_prompt_utf8_bytes':index['max_prompt_utf8_bytes'],
        'numerical_guard_floor_plus_unknown_framing':index['max_prompt_utf8_bytes']+4096,
        'guard_is_not_token_reserve':True,'final_reserve':None,
        'gate_a_approved':False,'live_kku_requests':0,'queue_mutations':0,'primary_results_added':0,
        'pending':['Beam final condition semantic/consumer/host review',
            'Champ final prompt/reserve/provider/settings/limits/framing/current quota/reset/expiry',
            '301 exclusions and three-owner Gate A requirements']}
    (OUT/'completion-receipt.json').write_bytes(encoded(result))
    hashes={}
    for base in (INTAKE,PREP,PAIR,OUT):
        for p in sorted(base.rglob('*')):
            if p.is_file() and p.name!='final-checksums.json':
                hashes[p.relative_to(ROOT).as_posix()]=sha256(p)
    (OUT/'final-checksums.json').write_bytes(encoded(hashes))
    return result


if __name__=='__main__':
    result=seal()
    print({k:result[k] for k in ('status','bugs','selected','exclusions','max_prompt_utf8_bytes','fixed_runtime_case_count','gate_a_approved')})
