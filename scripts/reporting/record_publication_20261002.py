"""Record a successfully pushed evidence commit after verifying the remote branch."""
from pathlib import Path
import json,subprocess
from datetime import datetime,timezone
root=Path(__file__).resolve().parents[2];out=root/'output/kku-only-20261001'
head=subprocess.check_output(['git','rev-parse','HEAD'],cwd=root,text=True).strip()
remote=subprocess.check_output(['git','ls-remote','origin','refs/heads/test'],cwd=root,text=True).split()[0]
if remote!=head:raise ValueError('Evidence commit not present at remote test HEAD')
status=json.loads((out/'delivery-status.json').read_text())
status['github'].update(status='published',evidence_commit=head,published_at_utc=datetime.now(timezone.utc).isoformat())
status['local_continuation_published']=True;status['latest_local_test_update']['published']=True
status['local_artifacts']['verification_status']='8 final PDF pages and 16 final imported slides visually reviewed; values/hash/audits verified. Native PowerPoint not opened.'
status['local_artifacts']['zip_status']='Ready for packaging after this publication receipt; see package-verification-20261002.json for actual ZIP result.'
(out/'delivery-status.json').write_text(json.dumps(status,indent=2)+'\n')
receipt={'verified_at_utc':datetime.now(timezone.utc).isoformat(),'repository':'https://github.com/natchapolp-glitch/Project_SQA','branch':'test','evidence_commit':head,'remote_test_head_observed':remote,'completed_runs':178,'classroom':'Owner will submit; not submitted by agent','private_evidence':'ZIP remains local; missing teammate screenshots disclosed.'}
(out/'github-publication-20261002.json').write_text(json.dumps(receipt,indent=2)+'\n')
print(json.dumps(receipt))
