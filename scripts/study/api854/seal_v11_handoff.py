"""Verify current condition, fresh offline evidence and unchanged history before handoff."""
import re
import subprocess
import io
import tarfile
from .common import ROOT,sha256,read_json,implementation_hashes,contained
from .preparation import encoded
from .chronology_v11 import INTAKE,V10,V10_COMMIT,load_contract
from .joint_recipe_v10 import git_bytes,git_json
from .build_prepare_v11_development import INTEGRATION,checked_integration
from .gate_a import inspect

PREP=ROOT/'output/api854-20261003/prepare-v11-chronology-development-v3'
PAIR=ROOT/'output/api854-20261003/aom-continuation-v11-development-v3'
OUT=ROOT/'output/api854-20261003/aom-v11-readiness-v1'
PACKAGE=ROOT/'output/api854-20261003/aom-v11-suite-packaging-v2'
RETAINED=ROOT/'output/api854-20261003/aom-v11-preserved-runtime-v2.json'


def seal():
    contract=checked_integration();index=read_json(PREP/'index.json');protocol=read_json(PAIR/'protocol.proposal.json')
    current=implementation_hashes()
    if current!=index['runtime_source_sha256'] or current!=protocol['source_sha256']:
        raise ValueError('Current final runtime/condition pins differ')
    for base in (INTAKE,PAIR,OUT,PACKAGE):
        for name,h in read_json(base/'checksums.json').items():
            if sha256(contained(base,name))!=h: raise ValueError('Sealed packet changed')
    retained=read_json(RETAINED);pack=read_json(PACKAGE/'receipt.json')
    if (retained['status']!='pass' or retained['runtime_helper_sha256']!=current['algorithms/java/SqaProbe.java']
            or retained['fixed_source_integration_cases']!=64 or not retained['temporary_setter_mutation_detected']
            or not retained['legacy_policy_behavior_preserved']
            or retained['policy_adapter_sha256']!=sha256(ROOT/'scripts/study/api854/verify_v11_preserved_runtime.py')
            or pack['status']!='pass' or pack['cases']!=13
            or not pack['stages']['controlled_fixture_failure']['evaluator_rejected_as_fault']):
        raise ValueError('Retained/packaged/fixture separation proof incomplete')
    packaging_seal=read_json(PACKAGE/'preexecution-seal.json')
    if (packaging_seal['helper_sha256']!=current['algorithms/java/SqaProbe.java']
            or packaging_seal['generator_sha256']!=current['scripts/study/generate.py']
            or pack['verifier_sha256']!=sha256(ROOT/'scripts/study/api854/verify_v11_suite_packaging.py')):
        raise ValueError('Packaging proof runtime differs')
    gate=inspect(protocol_path=PAIR/'protocol.proposal.json',runner_path=PAIR/'runner-plan.json')
    required={'prepare_contract','protocol_runner_binding','preparation_index_binding','runtime_source_binding',
              'shared_policy','fixture_recipe_binding','runner_coverage','eligible_targets_20'}
    if gate['gate_a_passed'] or any(c['status']!='pass' for c in gate['checklist'] if c['id'] in required):
        raise ValueError('Input audit failed or Gate A was opened')
    worksheet=read_json(OUT/'prompt-reserve-worksheet.json')
    if (len(worksheet['records'])!=40 or worksheet['runtime_source_sha256']!=current
            or worksheet['protocol_sha256']!=sha256(PAIR/'protocol.proposal.json') or worksheet['final_reserve'] is not None):
        raise ValueError('Reserve worksheet condition differs')
    for row in worksheet['records']:
        prompt=contained(ROOT,row['prompt_path'])
        if (row['prompt_sha256']!=sha256(prompt) or row['prompt_utf8_bytes']!=prompt.stat().st_size
                or row['requested_model_id']!=protocol['models'][row['approach']]['id']):
            raise ValueError('Prompt/model pair differs')
    work=[];source_count=0
    for row in index['records']:
        name=f"{row['project']}-{row['bug_id']}";folder=PREP/name
        before=git_json(V10_COMMIT,V10+'/'+name+'/context-manifest.json')
        after=read_json(folder/'context-manifest.json')
        if after['source_files']!=before['source_files']: raise ValueError('Historical production context changed')
        source_count+=len(after['source_files'])
        work.append({'project':row['project'],'bug_id':row['bug_id'],'owner':row['owner'],
            'selected':row['target_count'],'exclusions':row['capability_exclusion_count'],
            'unsupported_targets':read_json(folder/'capability-exclusions.json')['excluded'],
            'condition_semantic_review':'pending','final_host_acceptance':'pending'})
    prefixes=(V10,'output/api854-20261003/aom-continuation-v10-development',
        'output/api854-20261003/prepare-v7-twenty-bug-development',
        'output/api854-20261003/prepare-v8-fraction-field-development')
    preserved={p:0 for p in prefixes}
    raw=subprocess.check_output(['git','-C',str(ROOT),'archive',V10_COMMIT,*prefixes])
    with tarfile.open(fileobj=io.BytesIO(raw)) as archive:
        for member in archive.getmembers():
            if member.isfile():
                if (ROOT/member.name).read_bytes()!=archive.extractfile(member).read():
                    raise ValueError('Historical artifact modified: '+member.name)
                prefix=next(p for p in prefixes if member.name.startswith(p+'/'))
                preserved[prefix]+=1
    log=(OUT/'regression-tests.log').read_text()
    match=re.search(r'Ran (\d+) tests in .*?\n\s*\nOK\s*$',log)
    if not match or '... skipped ' in log: raise ValueError('Fresh regression incomplete/failing/skipped')
    tests=int(match.group(1))
    if tests<71: raise ValueError('Expected full regression including new consumer/evaluator checks')
    (OUT/'remaining-worklist.json').write_bytes(encoded({'condition':protocol['condition'],'selected':396,'exclusions':295,
        'denominator':691,'bugs':work,'empty_enum_targets':4,'full_requirement_approved':False,'gate_a_passed':False}))
    result={'status':'aom_v11_shared_composition_and_offline_checks_complete','condition':protocol['condition'],
        'protocol_sha256':sha256(PAIR/'protocol.proposal.json'),'preparation_index_sha256':sha256(PREP/'index.json'),
        'runner_plan_sha256':sha256(PAIR/'runner-plan.json'),'runtime_source_sha256':current,'runtime_files':len(current),
        'chronology_intake_sha256':sha256(INTAKE/'receipt.json'),'shared_integration_receipt_sha256':sha256(INTEGRATION/'receipt.json'),
        'packaging_receipt_sha256':sha256(PACKAGE/'receipt.json'),'retained_runtime_receipt_sha256':sha256(RETAINED),
        'sealer_sha256':sha256(__file__),'bugs':20,'selected':396,'exclusions':295,'denominator':691,
        'v10_context_source_files_preserved':source_count,'historical_files_unchanged':preserved,
        'retained_fixed_cases':64,'chronology_fixed_cases':13,'shared_chronology_exact_declarations':6,
        'shared_chronology_buggy_failed_case':'arrays_bad_order','coverage_kind':'Exact JDI entry; no line/branch percentage',
        'packaged_suite_cases':13,'packaged_fixture_failure_rejected_as_fault':True,
        'results_scope':'Local development integration/reference cases only; no new primary or algorithm fault result',
        'regression_tests':tests,'regression_log_sha256':sha256(OUT/'regression-tests.log'),'four_consumer_combinations':80,
        'prompt_model_pairs':40,'max_prompt_utf8_bytes':index['max_prompt_utf8_bytes'],
        'numerical_guard_floor_plus_unknown_framing':index['max_prompt_utf8_bytes']+4096,'guard_is_not_provider_token_reserve':True,
        'final_reserve':None,'new_full_defects4j_evaluations':0,'gate_a_passed':False,'live_requests':0,
        'queue_mutations':0,'primary_results_added':0,'new_condition_team_approval':False,
        'pending':['Beam v11 shared semantic/four-consumer/host receipt','Champ v11 prompt/token/settings/limits/framing/quota/reserve',
            '295 exclusions and three-owner Gate A requirements']}
    (OUT/'completion-receipt.json').write_bytes(encoded(result))
    hashes={}
    for base in (INTAKE,INTEGRATION,PACKAGE,PREP,PAIR,OUT):
        for p in sorted(base.rglob('*')):
            if p.is_file() and p.name!='final-checksums.json': hashes[p.relative_to(ROOT).as_posix()]=sha256(p)
    hashes[RETAINED.relative_to(ROOT).as_posix()]=sha256(RETAINED)
    (OUT/'final-checksums.json').write_bytes(encoded(hashes))
    return result


if __name__=='__main__':
    result=seal();print({k:result[k] for k in ('status','selected','exclusions','regression_tests','gate_a_passed')})
