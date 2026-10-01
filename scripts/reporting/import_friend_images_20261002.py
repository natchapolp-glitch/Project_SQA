"""Import only inspected missing screenshot bytes; never execute archive contents."""
from pathlib import Path
import json,zipfile,hashlib
from datetime import datetime,timezone
ROOT=Path(__file__).resolve().parents[2];OUT=ROOT/'output/kku-only-20261001'
check=json.loads((ROOT/'tmp/friend-evidence-zip-inspection-20261002.json').read_text())
source=Path(check['source'])
def sha(data):return hashlib.sha256(data).hexdigest()
assert sha(source.read_bytes())==check['source_sha256']
assert check['required_covered']==54 and not check['required_still_absent']
assert len(check['new_screenshots'])==54
summary_before=sha((OUT/'summary.json').read_bytes())
with zipfile.ZipFile(source) as z:
    for item in check['new_screenshots']:
        dest=(ROOT/item['path']).resolve()
        assert dest.is_relative_to(ROOT/'ai-tests/provider-captures') and dest.name=='provider-screen.png'
        assert not dest.exists(),dest
        assert all(item['adjacent_current_files_match'].values()),item
        assert sha(z.read('Project_SQA/'+item['path']))==item['sha256']
    history=ROOT/'results/validation/provider-image-recovery-20261002';history.mkdir(parents=True,exist_ok=False)
    (history/'provenance-before.json').write_bytes((OUT/'provenance-audit-current.json').read_bytes())
    for item in check['new_screenshots']:
        dest=ROOT/item['path'];dest.parent.mkdir(parents=True,exist_ok=True)
        dest.write_bytes(z.read('Project_SQA/'+item['path']))
        assert sha(dest.read_bytes())==item['sha256']
assert summary_before==sha((OUT/'summary.json').read_bytes())
receipt={**check,'imported_at_utc':datetime.now(timezone.utc).isoformat(),'imported_screenshots':54,'imported_only':'Missing provider-screen.png files. No code, response, metadata, record, or experimental results overwritten. Bundled docs and changed delivery-status.json not imported.','summary_sha256_unchanged':summary_before,'visual_spot_check':'Chart Claude102 refusal and Cli Gemini101 screenshots show KKU and matching model-family labels. All images decoded; image existence/hash checks do not independently authenticate past browser activity.'}
(history/'import-receipt.json').write_text(json.dumps(receipt,indent=2)+'\n')
print(json.dumps({'imported':54,'summary_unchanged':True}))
