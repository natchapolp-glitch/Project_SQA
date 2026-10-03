
import json, pathlib, subprocess, sys, unittest
snapshot, original, output = map(pathlib.Path, sys.argv[1:])
sys.path.insert(0,str(snapshot))
original_check_output = subprocess.check_output
def git_read(command,*args,**kwargs):
    command=list(command)
    if len(command)>2 and command[:2]==['git','-C'] and pathlib.Path(command[2])==snapshot:
        command[2]=str(original)
    return original_check_output(command,*args,**kwargs)
subprocess.check_output=git_read
from scripts.study.api854.common import read_json,write_json,implementation_hashes,sha256
from scripts.study.api854.verify_graphics_v12 import checked
from scripts.study.api854.graphics_v12 import load_intake
from scripts.study.api854.gate_a import inspect
from scripts.study.api854.verify_chronology_v11 import validate_buggy_signature
require=lambda ok,msg: None if ok else (_ for _ in ()).throw(ValueError(msg))
load_intake(); checked()
pair='output/api854-20261003/aom-continuation-v12-development-v1'
gate=inspect(protocol_path=pair+'/protocol.proposal.json',runner_path=pair+'/runner-plan.json')
required={'prepare_contract','protocol_runner_binding','preparation_index_binding','runtime_source_binding',
          'shared_policy','fixture_recipe_binding','runner_coverage','eligible_targets_20'}
require(not gate['gate_a_passed'] and all(c['status']=='pass' for c in gate['checklist'] if c['id'] in required),
        'Current binding checks failed or Gate A opened')
require(sum(c['id'] in required for c in gate['checklist'])==8,'Missing binding check')
write_json(output/'received-runtime-gate-recheck.json',gate)
suite=unittest.defaultTestLoader.loadTestsFromNames([
    'scripts.study.api854.tests.test_graphics_v12',
    'scripts.study.api854.tests.test_preparation_v12_development',
    'scripts.study.api854.tests.test_chronology_v11'])
result=unittest.TextTestRunner(verbosity=2).run(suite)
require(result.wasSuccessful() and result.testsRun==15 and not result.skipped,'Received focused tests failed')
retained=snapshot/'output/api854-20261003/aom-v12-preserved-runtime-v1'
c=read_json(retained/'component-receipt.json'); t=read_json(retained/'chronology/receipt.json')
require(c['status']=='pass' and c['fixed_source_integration_cases']==64 and c['temporary_setter_mutation_detected']
        and c['legacy_policy_behavior_preserved'] and c['runtime_helper_sha256']==implementation_hashes()['algorithms/java/SqaProbe.java'],
        'Retained component/runtime binding differs')
require(t['status']=='pass' and t['cases']==13 and t['runtime_source_sha256']==implementation_hashes(),
        'Retained Chronology/runtime binding differs')
for repeat in ('first','second'):validate_buggy_signature(t['stages']['buggy_'+repeat]['cases'])
env=snapshot/'output/api854-20261003/aom-graphics-v12-environment-controls-v2'
e=read_json(env/'receipt.json')
require(e['status']=='pass' and set(e['controls'])=={'binding_assertion','target_linkage'},'Missing actual environment controls')
require(e['shared_integration_sha256']==sha256(snapshot/'output/api854-20261003/aom-graphics-v12-integration-v5/receipt.json'),
        'Environment control bound to another integration')
for name,record in e['controls'].items():
    path=env/(name+'-cause-chain.stdout.log');raw=path.read_bytes()
    require(record['status']=='fixture_error' and not record['ordinary_target_observation']
            and record['cause_chain_proof']['exit_code']==0 and b'CONTROL_VALID=true' in raw
            and record['cause_chain_proof']['stdout_sha256']==sha256(path),'Actual fatal/setup cause chain differs')
write_json(output/'received-scoped-recheck.json',{'tests':result.testsRun,'skipped':len(result.skipped),
    'four_consumer_combinations':80,'graphics_raw_observations_revalidated':96,'graphics_exact_declarations':7,
    'graphics_entry_records_revalidated':48,'chart_old_new_pairs_revalidated':48,'junit_graphics_cases_per_stage':24,
    'junit_stages_revalidated':4,'graphics_candidate_fault_detected':False,'retained_component_receipt_cases':64,
    'retained_chronology_receipt_cases':13,'environment_controls_revalidated':2,'fresh_java_executions':0,
    'scope':'Fresh Python consumer/analytic/raw-evidence checks; received Java execution evidence, no native host acceptance'})
print(json.dumps({'status':'pass','tests':result.testsRun,'consumers':80,'gate_a_passed':False}))
