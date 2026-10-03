from pathlib import Path
import sys,unittest
sys.path.insert(0,str(Path.cwd()))
from scripts.study.api854.common import cpu_slot,write_json,sha256,implementation_hashes
p=Path('output/api854-20261003/beam-buffer-joint-review-v1')
with cpu_slot('/home/team/sqa-round2/beam-buffer-worktrees'):
 suite=unittest.defaultTestLoader.loadTestsFromNames(['scripts.study.api854.tests.test_buffer_fixture_policy','scripts.study.tests.test_java_probe'])
 with (p/'focused-tests.log').open('w') as f: result=unittest.TextTestRunner(stream=f,verbosity=2).run(suite)
 assert result.wasSuccessful() and result.testsRun==11 and not result.skipped
 write_json(p/'focused-tests.json',{'tests_run':result.testsRun,'passed':result.testsRun,'skipped':len(result.skipped),'log_sha256':sha256(p/'focused-tests.log'),'runtime_source_sha256':implementation_hashes(),'real_kku_requests':0,'queue_mutations':0,'scope':'Local buffer policy regression and real Java integration; not joint shared approval'})
 print('11 focused tests passed, skipped=0')
