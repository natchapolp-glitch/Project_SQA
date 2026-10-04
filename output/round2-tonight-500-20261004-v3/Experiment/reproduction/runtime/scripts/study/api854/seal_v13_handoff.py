"""Seal current V13 development inputs, raw evidence and immutable history."""
import hashlib
import json
import re
import subprocess
from pathlib import Path
from .common import ROOT, read_json, sha256, contained, implementation_hashes
from .preparation import encoded
from .codec_v13 import BASE, PREP, NEW_PAIR as PAIR, READY as OUT, INTAKE, PROOF, load_intake
from .verify_codec_v13 import checked
from .gate_a import inspect

RETAINED=ROOT/'output/api854-20261004/aom-v13-preserved-runtime-v3'
PACKAGING=ROOT/'output/api854-20261004/aom-codec-v13-packaging-v1'
ENV=ROOT/'output/api854-20261004/aom-codec-v13-environment-v1'
HISTORY=ROOT/'output/api854-20261004/aom-v13-peer-history-v1'


def packet(base):
    h=read_json(base/'checksums.json')
    if set(h)!={p.relative_to(base).as_posix() for p in base.rglob('*') if p.is_file() and p!=base/'checksums.json'}:raise ValueError('Complete manifest required: '+str(base))
    for n,v in h.items():
        if sha256(contained(base,n))!=v:raise ValueError('Packet bytes differ: '+str(base/n))


def seal():
    checked();load_intake();runtime=implementation_hashes()
    index=read_json(PREP/'index.json');protocol=read_json(PAIR/'protocol.proposal.json')
    if (len(index['records']),index['target_count'],index['capability_exclusion_count'])!=(20,408,283):raise ValueError('Exact 408/283 partition required')
    if index['runtime_source_sha256']!=runtime or protocol['source_sha256']!=runtime or index['generation_ready'] or protocol['enabled_stages'] or protocol['generation']['prompt_token_reserve'] is not None:raise ValueError('Closed condition/runtime required')
    for base in (PAIR,PROOF,INTAKE,RETAINED,PACKAGING,ENV,HISTORY):packet(base)
    history=read_json(HISTORY/'receipt.json')
    from .joint_recipe_v10 import git_bytes
    for n,ref in history['received'].items():
        if (HISTORY/n).read_bytes()!=git_bytes(ref['commit'],ref['path']):raise ValueError('Historical peer provenance differs')
    if history['v12_receipt_transfers_to_v13'] or history['gate_a_passed']:raise ValueError('History cannot approve current condition')
    for row in index['records']:packet(PREP/f"{row['project']}-{row['bug_id']}")
    retained=read_json(RETAINED/'receipt.json');pack=read_json(PACKAGING/'receipt.json');env=read_json(ENV/'receipt.json')
    for r in (retained,pack,env):
        if r['status']!='pass' or r['runtime_source_sha256']!=runtime:raise ValueError('Fresh runtime evidence required')
    if (retained['component_cases'],retained['chronology_cases'],retained['graphics_cases'],retained['chart_old_new_pairs'])!=(64,13,24,48):raise ValueError('Preservation scope differs')
    # Revalidate fresh preserved Graphics raw pixels/JDI/JUnit/environment controls.
    from . import verify_graphics_v12 as graphics
    graphics.OUTPUT=RETAINED/'graphics';graphics.checked()
    from .verify_chronology_v11 import validate_buggy_signature, validate_rows, parse
    from .chronology_v11 import load_contract
    c=load_contract();chrono=read_json(RETAINED/'chronology/receipt.json')
    for version in ('fixed','buggy'):
        for repeat in ('first','second'):
            name=version+'_'+repeat
            rows=validate_rows(parse((RETAINED/'chronology'/(name+'.stdout.log')).read_bytes()),c,version=='fixed')
            if rows!=chrono['stages'][name]['cases']:raise ValueError('Chronology raw observation differs')
            if version=='buggy':validate_buggy_signature(rows)
    component=read_json(RETAINED/'component-receipt.json')
    if (component['runtime_helper_sha256']!=runtime['algorithms/java/SqaProbe.java'] or not component['legacy_policy_behavior_preserved'] or not component['temporary_setter_mutation_detected'] or component['fixed_source_integration_cases']!=64):raise ValueError('Retained components differ')
    if pack['suite_sizes']!=[30,13] or pack['integration_sha256']!=sha256(PROOF/'receipt.json'):raise ValueError('Packaging differs')
    for name,r in pack['stages'].items():
        count=30 if '-0-' in name else 13
        raw=(PACKAGING/(name+'.counts.json')).read_bytes()
        counts=json.loads(raw.decode().replace('\\"','"').replace('\\n','\n'))
        if (r['exit_code'] or counts!={'schema_version':1,'executed':count,'skipped':0,'target_checks':count}
            or counts!=r['counts'] or sha256(PACKAGING/(name+'.stdout.log'))!=r['stdout_sha256']
            or ('OK ('+str(count)+' tests)').encode() not in (PACKAGING/(name+'.stdout.log')).read_bytes()):raise ValueError('Raw JUnit counts differ')
    if set(env['controls'])!={'default_state','target_linkage'}:raise ValueError('Codec actual environment controls required')
    for name,r in env['controls'].items():
        raw=(ENV/(name+'-cause.stdout.log')).read_bytes()
        if r['status']!='fixture_error' or r['ordinary_target_observation'] or r['cause_exit_code'] or b'CONTROL_VALID=true' not in raw:raise ValueError('Codec control escaped harness')
    gate=inspect(protocol_path=PAIR/'protocol.proposal.json',runner_path=PAIR/'runner-plan.json')
    required={'prepare_contract','protocol_runner_binding','preparation_index_binding','runtime_source_binding','shared_policy','fixture_recipe_binding','runner_coverage','eligible_targets_20'}
    if gate['gate_a_passed'] or any(c['status']!='pass' for c in gate['checklist'] if c['id'] in required):raise ValueError('Gate input bindings failed/opened')
    worksheet=read_json(OUT/'prompt-reserve-worksheet.json')
    if len(worksheet['records'])!=40 or worksheet['runtime_source_sha256']!=runtime or worksheet['final_reserve'] is not None or worksheet['protocol_sha256']!=sha256(PAIR/'protocol.proposal.json'):raise ValueError('Worksheet bound to another condition')
    for row in worksheet['records']:
        p=contained(ROOT,row['prompt_path'])
        if row['prompt_sha256']!=sha256(p) or row['prompt_utf8_bytes']!=p.stat().st_size:raise ValueError('Worksheet prompt differs')
    historical=0
    for entry in subprocess.check_output(['git','ls-tree','-r','-z',BASE,'--','output/'],cwd=ROOT).split(b'\0'):
        if not entry:continue
        meta,name=entry.split(b'\t',1);kind,blob=meta.split()[1:]
        if kind!=b'blob':continue
        raw=contained(ROOT,name.decode()).read_bytes()
        if hashlib.sha1(b'blob '+str(len(raw)).encode()+b'\0'+raw).hexdigest().encode()!=blob:raise ValueError('Historical evidence changed: '+name.decode())
        historical+=1
    log=(OUT/'regression-tests.log').read_text();m=re.search(r'Ran (\d+) tests in .*?\n\s*\nOK\s*$',log)
    if not m or int(m[1])<95 or '... skipped ' in log:raise ValueError('Regression incomplete/failed/skipped')
    remaining=[{'project':r['project'],'bug_id':r['bug_id'],'owner':r['owner'],'selected':r['target_count'],'exclusions':r['capability_exclusion_count'],
                'unsupported_targets':read_json(PREP/f"{r['project']}-{r['bug_id']}"/'capability-exclusions.json')['excluded'],
                'final_condition_semantic_review':'pending','owner_host_acceptance':'pending'} for r in index['records']]
    (OUT/'remaining-worklist.json').write_bytes(encoded({'condition':protocol['condition'],'selected':408,'exclusions':283,'denominator':691,'bugs':remaining,'enum_still_excluded':4,'gate_a_passed':False}))
    result={'status':'aom_v13_shared_codec_composition_offline_checks_complete','condition':protocol['condition'],
        'base_aom_commit':BASE,'bugs':20,'selected':408,'exclusions':283,'denominator':691,
        'protocol_sha256':sha256(PAIR/'protocol.proposal.json'),'preparation_index_sha256':sha256(PREP/'index.json'),
        'runner_plan_sha256':sha256(PAIR/'runner-plan.json'),'runtime_source_sha256':runtime,'runtime_files':len(runtime),
        'joint_intake_sha256':sha256(INTAKE/'receipt.json'),'codec_integration_sha256':sha256(PROOF/'receipt.json'),
        'codec_packaging_sha256':sha256(PACKAGING/'receipt.json'),'codec_environment_sha256':sha256(ENV/'receipt.json'),
        'retained_runtime_sha256':sha256(RETAINED/'receipt.json'),'codec_cases':43,'codec_exact_signatures':5,
        'codec_fixed_observations':86,'codec_buggy_observations':86,'codec13_old_new_pairs':78,
        'codec_reference_suite_sizes':[30,13],'codec_fault_detected':False,'retained_component_cases':64,'chronology_cases':13,
        'graphics_cases':24,'chart_old_new_pairs':48,'regression_tests':int(m[1]),'four_consumer_combinations':80,
        'gate_input_checks_passed':8,'prompt_model_pairs':40,'max_prompt_utf8_bytes':index['max_prompt_utf8_bytes'],
        'historical_output_files_unchanged':historical,'gate_a_passed':False,'shared_team_approval':False,
        'final_reserve':None,'live_requests':0,'queue_mutations':0,'primary_results_added':0,
        'owner_host_acceptance':False,'full_semantic_408_approval':False,'coverage_kind':'Exact method entry, no line/branch percentage',
        'historical_v12_beam_scoped_wait_closed':True,'historical_v12_acceptance_transfers_to_v13':False,
        'historical_peer_receipts_sha256':sha256(HISTORY/'receipt.json'),
        'superseded_pre_hardening_proofs':['output/api854-20261004/aom-codec-v13-integration-v1','output/api854-20261004/aom-v13-preserved-runtime-v1','output/api854-20261004/aom-codec-v13-integration-v2','output/api854-20261004/aom-v13-preserved-runtime-v2','output/api854-20261004/prepare-v13-codec-development-v1','output/api854-20261004/aom-continuation-v13-development-v1','output/api854-20261004/aom-v13-readiness-v1'],
        'pending':['Beam final condition bounded semantic/all4 consumer/beam host receipt','Champ final bindings/current provider/token framing/limits/quota/reset/expiry/reserve','Aom owner-host receipt and three-owner pilot policy decision']}
    (OUT/'completion-receipt.json').write_bytes(encoded(result))
    roots=(PREP,PAIR,OUT,INTAKE,PROOF,RETAINED,PACKAGING,ENV,HISTORY)
    hashes={p.relative_to(ROOT).as_posix():sha256(p) for base in roots for p in sorted(base.rglob('*')) if p.is_file() and p.name!='final-checksums.json'}
    (OUT/'final-checksums.json').write_bytes(encoded(hashes));return result


if __name__=='__main__':
    r=seal();print({k:r[k] for k in ('status','selected','exclusions','regression_tests','historical_output_files_unchanged','gate_a_passed')})
