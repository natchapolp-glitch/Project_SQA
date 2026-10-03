from pathlib import Path
import sys
from scripts.study.api854.common import cpu_slot, sha256, write_json

output=Path(sys.argv[1]); expected=sys.argv[2]
root=Path('/home/beam/sqa-beam/worktrees')
try:
 with cpu_slot(root): outcome='acquired'
except RuntimeError as error:
 if not str(error).startswith('CPU slot busy;'): raise
 outcome='blocked_busy'
if output.exists(): raise ValueError('Receipt already exists')
output.parent.mkdir(parents=True,exist_ok=True)
write_json(output,{'worker_id':'beam-pc1','physical_hosts':1,'cpu_slots':1,
 'scope':'Real shared worktrees root; no generation/evaluation job attempted by this checking process',
 'root':str(root),'outcome':outcome,'expected_outcome':expected,'passed':outcome==expected,
 'common_sha256':sha256('scripts/study/api854/common.py'),'checker_sha256':sha256(__file__),
 'development_protocol_sha256':sha256('output/api854-beam/pilot-fixtures-v5-1/protocol-proposal.json'),
 'queue_mutations':0,'kku_requests':0})
print(outcome,'passed=',outcome==expected)
raise SystemExit(0 if outcome==expected else 4)
