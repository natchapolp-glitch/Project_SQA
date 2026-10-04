"""Verify the fresh shared condition, all required controls and immutable output history."""
import hashlib
import re
import subprocess
from .common import ROOT,sha256,read_json,implementation_hashes,contained
from .preparation import encoded
from .graphics_v12 import INTAKE,BASE,V11,load_intake
from .verify_graphics_v12 import OUTPUT as INTEGRATION,checked
from .gate_a import inspect

PREP=ROOT/'output/api854-20261003/prepare-v12-graphics-development-v1'
PAIR=ROOT/'output/api854-20261003/aom-continuation-v12-development-v1'
OUT=ROOT/'output/api854-20261003/aom-v12-readiness-v1'
ENV=ROOT/'output/api854-20261003/aom-graphics-v12-environment-controls-v2'
RETAINED=ROOT/'output/api854-20261003/aom-v12-preserved-runtime-v1'


def packet(base,complete=True):
    hashes=read_json(base/'checksums.json')
    if complete and set(hashes)!={p.relative_to(base).as_posix() for p in base.rglob('*') if p.is_file() and p!=base/'checksums.json'}:
        raise ValueError('Complete packet inventory required')
    for n,h in hashes.items():
        if sha256(contained(base,n))!=h:raise ValueError('Packet checksum differs: '+n)


def seal():
    checked();load_intake()
    index=read_json(PREP/'index.json');protocol=read_json(PAIR/'protocol.proposal.json');runtime=implementation_hashes()
    if (index['runtime_source_sha256']!=runtime or protocol['source_sha256']!=runtime
        or (len(index['records']),index['target_count'],index['capability_exclusion_count'])!=(20,403,288)
        or index['generation_ready'] or protocol['enabled_stages'] or protocol['generation']['prompt_token_reserve'] is not None):
        raise ValueError('Final closed development condition differs')
    for base in (PAIR,ENV,RETAINED):packet(base)
    # OUT's first manifest seals the input audit only; final-checksums below covers later tests/completion.
    packet(OUT,complete=False)
    retained=read_json(RETAINED/'receipt.json');components=read_json(RETAINED/'component-receipt.json')
    chronology=read_json(RETAINED/'chronology/receipt.json')
    if (retained['status']!='pass' or retained['component_cases']!=64 or retained['chronology_cases']!=13
        or components['status']!='pass' or components['runtime_helper_sha256']!=runtime['algorithms/java/SqaProbe.java']
        or not components['legacy_policy_behavior_preserved'] or not components['temporary_setter_mutation_detected']
        or components['fixed_source_integration_cases']!=64 or chronology['runtime_source_sha256']!=runtime
        or chronology['status']!='pass' or chronology['cases']!=13):
        raise ValueError('Preserved component/Chronology proof differs')
    from .verify_chronology_v11 import validate_buggy_signature
    for repeat in ('first','second'):
        validate_buggy_signature(chronology['stages']['buggy_'+repeat]['cases'])
    environment=read_json(ENV/'receipt.json')
    if (environment['status']!='pass' or environment['shared_integration_sha256']!=sha256(INTEGRATION/'receipt.json')
        or set(environment['controls'])!={'binding_assertion','target_linkage'}):
        raise ValueError('Actual pre-target/fatal error separation proof missing')
    for name,record in environment['controls'].items():
        raw=(ENV/(name+'-cause-chain.stdout.log')).read_bytes()
        if (record['status']!='fixture_error' or record['ordinary_target_observation']
            or record['cause_chain_proof']['exit_code'] or b'CONTROL_VALID=true' not in raw
            or record['cause_chain_proof']['stdout_sha256']!=hashlib.sha256(raw).hexdigest()):
            raise ValueError('Actual error cause/invocation control differs')
    gate=inspect(protocol_path=PAIR/'protocol.proposal.json',runner_path=PAIR/'runner-plan.json')
    required={'prepare_contract','protocol_runner_binding','preparation_index_binding','runtime_source_binding',
              'shared_policy','fixture_recipe_binding','runner_coverage','eligible_targets_20'}
    if gate['gate_a_passed'] or any(c['status']!='pass' for c in gate['checklist'] if c['id'] in required):
        raise ValueError('Input audit failed or Gate A opened')
    worksheet=read_json(OUT/'prompt-reserve-worksheet.json')
    if (len(worksheet['records'])!=40 or worksheet['runtime_source_sha256']!=runtime
        or worksheet['protocol_sha256']!=sha256(PAIR/'protocol.proposal.json') or worksheet['final_reserve'] is not None):
        raise ValueError('Same-condition worksheet differs')
    for r in worksheet['records']:
        p=contained(ROOT,r['prompt_path'])
        if r['prompt_sha256']!=sha256(p) or r['prompt_utf8_bytes']!=p.stat().st_size or r['requested_model_id']!=protocol['models'][r['approach']]['id']:
            raise ValueError('Current prompt/model pair differs')
    for r in index['records']:packet(PREP/f"{r['project']}-{r['bug_id']}")
    # Git blob IDs verify every previously committed output byte without thousands of git show processes.
    entries=subprocess.check_output(['git','-C',str(ROOT),'ls-tree','-r','-z',BASE,'--','output/api854-20261003/']).split(b'\0')
    historical=0
    for entry in entries:
        if not entry:continue
        meta,name=entry.split(b'\t',1);kind,blob=meta.split()[1:]
        if kind!=b'blob':continue
        raw=contained(ROOT,name.decode()).read_bytes()
        actual=hashlib.sha1(b'blob '+str(len(raw)).encode()+b'\0'+raw).hexdigest().encode()
        if actual!=blob:raise ValueError('Historical committed output bytes changed: '+name.decode())
        historical+=1
    log=(OUT/'regression-tests.log').read_text()
    match=re.search(r'Ran (\d+) tests in .*?\n\s*\nOK\s*$',log)
    if not match or int(match[1])<82 or '... skipped ' in log:raise ValueError('Full regression failed/incomplete/skipped')
    work=[{'project':r['project'],'bug_id':r['bug_id'],'owner':r['owner'],'selected':r['target_count'],
        'exclusions':r['capability_exclusion_count'],'unsupported_targets':read_json(PREP/f"{r['project']}-{r['bug_id']}"/'capability-exclusions.json')['excluded'],
        'condition_semantic_review':'pending','final_host_acceptance':'pending'} for r in index['records']]
    (OUT/'remaining-worklist.json').write_bytes(encoded({'condition':protocol['condition'],'selected':403,'exclusions':288,
        'denominator':691,'bugs':work,'enum_targets_still_in_denominator':4,'gate_a_passed':False,
        'team_gate_decision':'docs/api854/AOM_PILOT_GATE_DECISION_TH.md'}))
    result={'status':'aom_v12_shared_composition_offline_checks_complete','condition':protocol['condition'],
        'protocol_sha256':sha256(PAIR/'protocol.proposal.json'),'preparation_index_sha256':sha256(PREP/'index.json'),
        'runner_plan_sha256':sha256(PAIR/'runner-plan.json'),'runtime_source_sha256':runtime,'runtime_files':len(runtime),
        'joint_intake_sha256':sha256(INTAKE/'receipt.json'),'shared_integration_receipt_sha256':sha256(INTEGRATION/'receipt.json'),
        'retained_runtime_receipt_sha256':sha256(RETAINED/'receipt.json'),'environment_control_receipt_sha256':sha256(ENV/'receipt.json'),
        'bugs':20,'selected':403,'exclusions':288,'denominator':691,'historical_committed_output_files_unchanged':historical,
        'retained_fixed_cases':64,'chronology_cases':13,'graphics_cases':24,'graphics_exact_inherited_declarations':7,
        'graphics_buggy_fault_detected':False,'chart_old_new_observation_pairs':48,'junit_graphics_cases':24,
        'coverage_kind':'Exact inherited JDI method entry; no line/branch percentage','regression_tests':int(match[1]),
        'regression_log_sha256':sha256(OUT/'regression-tests.log'),'four_consumer_combinations':80,'prompt_model_pairs':40,
        'max_prompt_utf8_bytes':index['max_prompt_utf8_bytes'],'final_reserve':None,'gate_a_input_checks_passed':8,
        'gate_a_passed':False,'shared_team_approval':False,'live_requests':0,'queue_mutations':0,'primary_results_added':0,
        'scope':'Local development/reference integration, not algorithm fault results or full Defects4J execution',
        'historical_v10_scoped_wait_closed':True,'v10_acceptance_transfers_to_v12':False,'sealer_sha256':sha256(__file__),
        'pending':['Beam/Champ final-condition scoped semantic and owner-host receipts','Champ current provider/settings/limits/tokens/framing/quota/reserve','Team691-primary versus separate bounded development-pilot policy decision']}
    (OUT/'completion-receipt.json').write_bytes(encoded(result))
    hashes={p.relative_to(ROOT).as_posix():sha256(p) for base in (PREP,PAIR,OUT,INTAKE,INTEGRATION,ENV,RETAINED)
            for p in sorted(base.rglob('*')) if p.is_file() and p.name!='final-checksums.json'}
    (OUT/'final-checksums.json').write_bytes(encoded(hashes))
    return result


if __name__=='__main__':
    r=seal();print({k:r[k] for k in ('status','selected','exclusions','regression_tests','historical_committed_output_files_unchanged','gate_a_passed')})
