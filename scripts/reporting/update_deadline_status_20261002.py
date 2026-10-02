from pathlib import Path
import json,shutil
from datetime import datetime,timezone
root=Path(__file__).resolve().parents[2]
out=root/'output/kku-only-20261001'
path=out/'delivery-status.json'
status=json.loads(path.read_text())
status['previous_checkpoint']=status.get('previous_checkpoint',{'experiment':status['experiment'],'local_artifacts':status['local_artifacts'],'github':status['github']})
status['experiment']='incomplete: primary 183/204; Claude 30/51 including 25 Haiku and 5 historical Sonnet; both algorithms and Gemini 51/51; one completed Time clarification kept separate'
status['updated_at_utc']=datetime.now(timezone.utc).isoformat()
status['local_continuation_published']=False
status['github']['status']='latest_local_changes_not_yet_published'
status['local_artifacts']={'report':'output/kku-only-20261001/SQA_Round2_KKU_Only_20261002_DEADLINE.pdf',
    'presentation':'output/kku-only-20261001/SQA_Round2_KKU_Only_20261002_DEADLINE.pptx',
    'zip':'output/SQA_Round2_17Bugs_20261002_DEADLINE_Evidence.zip',
    'verification':'output/kku-only-20261001/delivery-verification-20261002_DEADLINE.json',
    'verification_status':'9 PDF pages and 17 slide previews reviewed; execution/provenance audits and artifact structure passed; native PowerPoint not opened',
    'zip_status':'building_next; final external package receipt verifies bytes and SHA'}
status['latest_local_test_update']={'completed':183,'pending':21,'claude_completed_runs':30,'claude_projects':15,
    'claude_test_methods':782,'claude_haiku_completed_runs':25,'claude_haiku_projects':13,
    'historical_sonnet_completed_runs':5,'strict_haiku_primary_total':178,'secondary_clarification_completed':1,
    'secondary_clarification_methods':30,'published':False,'classroom_submitted':False,
    'receipt':'docs/SUBMISSION_READY_20261002_DEADLINE.md'}
status['remaining_work']='21 primary Claude identities incomplete, including no completed Claude results for Closure/JxPath. Strict Haiku also needs replacements for five historical Sonnet identities. Current KKU Haiku quota 100%; another authorized logged-in account is pending user response. Classroom submission not performed.'
status['historical_claude_quota_before_deadline']=status['claude_quota']
status['claude_quota']={'model':'claude-haiku-latest','account':'current-user-authorized-account','used_percent':100,
    'status':'daily_limit_reached','reset_time':None,
    'evidence':'ai-tests/provider-captures/kku-clarified-20261002/claude/JxPath-1/s103-i2-kitathip-haiku-20261002/provider-screen.png'}
status['provenance_limitations']['scope']='Current manifest: 203 records checked, zero issues; original JPEG screenshots converted to PNG with hashes where required. No authentication claim beyond captured UI and local records.'
path.write_text(json.dumps(status,ensure_ascii=False,indent=2)+'\n',encoding='utf-8')
readme=root/'README.md'
text=readme.read_text(encoding='utf-8')
header='''# Latest submission checkpoint — 17 bugs, 2 October 2026

Use **20261002_DEADLINE** PDF/PPTX and `docs/SUBMISSION_READY_20261002_DEADLINE.md`.
Original-prompt primary results: **183/204 completed, 21 incomplete**. Both algorithms and KKU Gemini: 51/51 each. KKU Claude: 30/51, comprising 25 Haiku and 5 historical Sonnet results. Strict Haiku primary total: 178/204. One completed Time clarification result is separate and is not added to original-prompt totals. Current Haiku quota: 100%.

The available evidence is checked and packaged for submission; the experiment is still incomplete. Classroom has not been submitted. Private screenshots remain in the local ZIP. All checkpoint statements below describe older versions; consult the current delivery status and verification receipts.

## Historical checkpoints

'''
if not text.startswith('# Latest submission checkpoint — 17 bugs'):
    readme.write_text(header+text,encoding='utf-8')
validation=root/'results/validation/deadline-20261002'
validation.mkdir(parents=True,exist_ok=True)
shutil.copy2(root/'tmp/kku-presentation-deadline/SQA_Round2_KKU_Only_20261002_DEADLINE.pptx.validation.json',validation/'presentation-validation.json')
print('Updated current delivery status; retained historical checkpoint')
