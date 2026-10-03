
import json, pathlib, subprocess, sys, unittest
snapshot, original, output, defects4j = map(pathlib.Path, sys.argv[1:])
sys.path.insert(0, str(snapshot))
old_check_output = subprocess.check_output
def read_git(command, *args, **kwargs):
    command = list(command)
    if len(command) > 2 and command[:2] == ['git', '-C'] and pathlib.Path(command[2]) == snapshot:
        command[2] = str(original)
    return old_check_output(command, *args, **kwargs)
subprocess.check_output = read_git
from scripts.study.api854.common import implementation_hashes, read_json, write_json
from scripts.study.api854.joint_recipe_v10 import load_intake
from scripts.study.api854.gate_a import inspect
from scripts.study.api854.verify_v10_recipe_runtime import verify
require = lambda ok, msg: None if ok else (_ for _ in ()).throw(ValueError(msg))
load_intake()
pair = 'output/api854-20261003/aom-continuation-v10-development'
gate = inspect(protocol_path=pair+'/protocol.proposal.json', runner_path=pair+'/runner-plan.json')
required = {'prepare_contract','protocol_runner_binding','preparation_index_binding','runtime_source_binding',
            'shared_policy','fixture_recipe_binding','runner_coverage','eligible_targets_20'}
require(not gate['gate_a_passed'] and all(c['status']=='pass' for c in gate['checklist'] if c['id'] in required),
        'v10 structural binding audit failed')
write_json(output/'received-runtime-gate-recheck.json', gate)
suite = unittest.defaultTestLoader.loadTestsFromNames([
    'scripts.study.api854.tests.test_joint_recipe_v10',
    'scripts.study.api854.tests.test_preparation_v10_development'])
result = unittest.TextTestRunner(verbosity=2).run(suite)
require(result.wasSuccessful() and result.testsRun == 8 and not result.skipped, 'Focused received tests failed')
write_json(output/'received-focused-tests.json', {'tests':result.testsRun,'failures':len(result.failures),
    'errors':len(result.errors),'skipped':len(result.skipped),'four_consumer_bug_approach_combinations':80})
proof = verify(defects4j)
write_json(output/'native-fixed-runtime-recheck.json', proof)
print(json.dumps({'status':'pass','tests':result.testsRun,'fixed_cases':proof['fixed_source_integration_cases']}))
