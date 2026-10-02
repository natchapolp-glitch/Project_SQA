from pathlib import Path
import json,subprocess
from datetime import datetime,timezone
root=Path(__file__).resolve().parents[2]
out=root/'output/kku-only-20261001'
head=subprocess.check_output(['git','rev-parse','HEAD'],cwd=root,text=True).strip()
remote=subprocess.check_output(['git','ls-remote','origin','refs/heads/test'],cwd=root,text=True).split()[0]
if head!=remote: raise ValueError('Remote branch does not match published HEAD')
status_path=out/'delivery-status.json'
status=json.loads(status_path.read_text())
status['github'].update(status='published',evidence_commit=head,published_at_utc=datetime.now(timezone.utc).isoformat())
status['local_continuation_published']=True
status['latest_local_test_update']['published']=True
status_path.write_text(json.dumps(status,ensure_ascii=False,indent=2)+'\n',encoding='utf-8')
receipt={'repository':'https://github.com/natchapolp-glitch/Project_SQA','branch':'test',
         'verified_remote_commit':head,'verified_at_utc':datetime.now(timezone.utc).isoformat(),
         'package':json.loads((out/'package-verification-20261002_DEADLINE.json').read_text()),
         'private_images_published':False,'classroom_submitted':False,
         'note':'ZIP is the evidence snapshot before publication; this external receipt records final package SHA and verified GitHub publication. Original-prompt experiment remains 183/204; strict Haiku primary 178/204; secondary clarification separate.'}
(out/'publication-receipt-20261002_DEADLINE.json').write_text(json.dumps(receipt,indent=2)+'\n')
print(head)
