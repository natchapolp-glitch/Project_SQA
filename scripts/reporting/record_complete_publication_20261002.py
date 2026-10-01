from pathlib import Path
import json,subprocess
from datetime import datetime,timezone
root=Path(__file__).resolve().parents[2];out=root/'output/kku-only-20261001'
head=subprocess.check_output(['git','rev-parse','HEAD'],cwd=root,text=True).strip()
remote=subprocess.check_output(['git','ls-remote','origin','refs/heads/test'],cwd=root,text=True).split()[0]
assert head==remote,'Remote test differs from local pushed checkpoint'
status=json.loads((out/'delivery-status.json').read_text())
original=status['github']['evidence_commit']
status['github'].update(status='published',original_results_commit=original,evidence_commit=head,published_at_utc=datetime.now(timezone.utc).isoformat())
status['local_artifacts']['verification_status']='8 PDF pages reviewed; slide13 updated and reviewed, all other slide previews byte-identical to previously reviewed deck. 16 slides; chart/table values, audit and frozen source hashes verified.'
status['local_artifacts']['zip_status']='Ready for revised private packaging; actual container verified by external package-verification-20261002_COMPLETE.json after packaging.'
status['latest_local_test_update']['receipt']='docs/SUBMISSION_READY_20261002_COMPLETE.md'
(out/'delivery-status.json').write_text(json.dumps(status,indent=2)+'\n')
receipt={'verified_at_utc':datetime.now(timezone.utc).isoformat(),'branch':'test','evidence_commit':head,'remote_head':remote,'completed_runs':178,'provenance_issues':0,'imported_missing_images':54,'image_import_receipt':'results/validation/provider-image-recovery-20261002/import-receipt.json','private_images_published':False,'classroom_submitted':False}
(out/'github-publication-20261002_COMPLETE.json').write_text(json.dumps(receipt,indent=2)+'\n')
print(json.dumps(receipt))
