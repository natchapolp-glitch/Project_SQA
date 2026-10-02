import json,hashlib
from pathlib import Path
R=Path(__file__).resolve().parents[2]
O=R/'output/kku-only-20261001'
def sha(p):return hashlib.sha256(p.read_bytes()).hexdigest()
v=(R/'scripts/reporting/verify_kku_delivery_v3.py').read_text()
v=v.replace('_20261002_COMPLETE','_20261002_HAIKU').replace('claude-quota-proof-20261002.png','claude-quota-proof-20261002_HAIKU.png')
v=v.replace("assert len(done)==summary['completed_runs']","assert len(done)==summary['completed_runs']==181\n    assert sum(r['test_count'] for r in done if r['approach']=='kku-claude')==726\n    continuation=json.loads((ROOT/'results/validation/new-account-haiku-20261002/continuation-receipt.json').read_text())\n    assert len(continuation['requests'])==8\n    for item in continuation['requests']:\n        for name,key in [('response.md','response_sha256'),('operator-metadata.json','operator_sha256'),('provider-screen.png','screenshot_sha256')]:\n            assert sha(ROOT/item['capture']/name)==item[key]")
v=v.replace("assert all(i['issue']=='missing capture screenshot' for i in provenance['issues']),provenance['issues']","assert not provenance['issues'],provenance['issues']")
v=v.replace('All final PDF pages and all 16 imported final PPTX slides inspected.','All 8 final PDF pages inspected; 7 changed final PPTX slides inspected individually, 9 unchanged previews byte-identical to previously reviewed slides; whole deck scanned.')
(R/'scripts/reporting/verify_kku_delivery_haiku_20261002.py').write_text(v,encoding='utf-8')
p=(R/'scripts/reporting/package_kku_submission_v3.py').read_text().replace('_20261002_COMPLETE','_20261002_HAIKU')
p=p.replace('COMPLETE refers to recovered screenshots; the 204-run experiment is still incomplete.','Latest Haiku checkpoint: 181/204 completed, 23 incomplete. Historical COMPLETE artifacts refer to recovered screenshots, not experiment completion.')
(R/'scripts/reporting/package_kku_submission_haiku_20261002.py').write_text(p,encoding='utf-8')
notice='''# ชุดส่งล่าสุด: Claude Haiku — 2 ตุลาคม 2026

ผลสำเร็จ 181/204 รอบ เหลือ 23 รอบของ Claude ตามแผนทีม (204 เป็นแผนทีม ไม่ใช่จำนวนขั้นต่ำที่อาจารย์กำหนด) CMAES, FSCS และ Gemini อย่างละ 51 รอบ; Claude 28 รอบ ครอบคลุม 13 projects รวม 726 test methods.

บัญชีใหม่ส่ง 8 คำขอ: Sonnet 1 ก่อนเปลี่ยนตามคำสั่งเจ้าของ และ Haiku 7 ผ่าน KKU เท่านั้น เพิ่มผลสำเร็จ Time 102 (30 methods), Mockito 103 (21), Compress 101 (29; ตรวจพบ fault) รวม 80 methods. คำขออื่นล้มเหลว/ถูกปฏิเสธ/compile ไม่ผ่านและไม่ได้นับเป็นผลสำเร็จ โควตาล่าสุดใช้ 93.5%; เจ้าของไม่มีบัญชีเพิ่มและสั่งหยุดทดลองเพื่อเตรียมชุดส่ง.

Time ใช้เฉพาะคลาสแรกที่สมบูรณ์และจำกัด 30 methods; ส่วนคำตอบที่ขาดเก็บตามจริง. Mockito ตัด 7 methods ที่ไม่ผ่าน fixed validation. Compress ตัดทั้ง method ที่เรียก constructor ไม่รองรับตาม policy v75 ก่อนจำกัดจำนวนและตัดอีก 1 method ที่ไม่ผ่าน fixed validation; ไม่เปลี่ยน assertions. ดู continuation-receipt.json และ processing policies v73–v75.

รายงาน/สไลด์ปัจจุบันอยู่ใน output/kku-only-20261001/ ชื่อ SQA_Round2_KKU_Only_20261002_HAIKU.pdf และ .pptx. ZIP ส่วนตัวอยู่ที่ output/SQA_Round2_KKU_Only_20261002_HAIKU_VERIFIED_Evidence.zip รวมภาพหลักฐานจริงที่มีอยู่ และ manifest ตรวจ SHA256 ทุกไฟล์. ชุด COMPLETE เก่าเป็น checkpoint 178 รอบ.

ตรวจ execution audits: baseline 171/171, Gemini 39/39, Claude ใหม่ 25/25 (อีก 3 เป็น reused); ไม่มี issues. Provenance 200 records ไม่มี issues; ภาพจากเพื่อน 54 ภาพได้รับแล้ว. รายงาน 8 หน้าและสไลด์ 16 หน้าได้รับการตรวจรูปแบบ; ตาราง 4 และกราฟ 1 เป็น native editable objects. ไม่ได้เปิดด้วยแอป PowerPoint จริง.

ใช้ delivery-verification-20261002_HAIKU.json, package-verification-20261002_HAIKU.json และ delivery-status.json ตรวจสถานะ. ZIP เก็บสถานะ ณ เวลาสร้าง; GitHub publication ภายหลังดู delivery-status.json ใน checkout ปัจจุบัน. ภาพ provider ส่วนตัวและ ZIP ไม่อยู่ใน public Git. ผู้ส่งงานนำ PDF/PPTX และหลักฐานตามช่องทางที่อาจารย์กำหนดส่ง Classroom เองก่อนเที่ยงคืน; ยังไม่ได้ส่ง Classroom.
'''
(R/'docs/SUBMISSION_READY_20261002_HAIKU.md').write_text(notice,encoding='utf-8')
header='Current checkpoint: **181/204 completed, 23 pending**. Use artifacts ending `_20261002_HAIKU` and `docs/SUBMISSION_READY_20261002_HAIKU.md`. Earlier COMPLETE checkpoints below are historical (178 runs). Owner stopped further AI requests and will submit Classroom themselves.\n\n'
for name in ['README.md','docs/HANDOFF_TO_TEAM.md','docs/HANDOFF_PLAN_20261002_TH.md','docs/KKU_ONLY_CONTINUATION.md','docs/CONTINUATION_NEW_ACCOUNT_20261002_TH.md','docs/SUBMISSION_READY_20261002_COMPLETE.md','presentation/demo-guide-kku-only.md']:
    f=R/name
    f.write_text(header+f.read_text(encoding='utf-8'),encoding='utf-8')
s=json.loads((O/'delivery-status.json').read_text())
s['previous_checkpoint_claude_quota']=s['claude_quota']
s['experiment']='incomplete: 181/204; Claude 28/51; Gemini and both algorithms 51/51'
s['github']['previous_evidence_commit']=s['github'].pop('evidence_commit')
s['github']['status']='latest checkpoint awaiting push'
s['local_continuation_published']=False
s['remaining_work']='23 planned Claude identities incomplete; owner Classroom submission. Further AI requests stopped at owner request.'
s['latest_local_test_update']={'completed':181,'pending':23,'claude_completed_runs':28,'claude_projects':13,'claude_test_methods':726,'completed_added_since_friend_checkpoint':7,'new_account_requests':8,'published':False,'classroom_submitted':False,'receipt':'docs/SUBMISSION_READY_20261002_HAIKU.md'}
s['claude_quota']={'model':'claude-haiku-latest','account':'new-account-20261002','used_percent':93.5,'reset_time':None,'status':'owner_stopped_to_prepare_submission','evidence':'output/kku-only-20261001/claude-quota-proof-20261002_HAIKU.png','evidence_sha256':sha(O/'claude-quota-proof-20261002_HAIKU.png')}
s['local_artifacts']={k:v.replace('_20261002_COMPLETE','_20261002_HAIKU') if isinstance(v,str) else v for k,v in s['local_artifacts'].items()}
s['local_artifacts']['verification_status']='8 final PDF pages reviewed; 7 changed slides individually reviewed and 9 byte-identical to previously reviewed previews; whole deck scanned. Structural verification receipt records checks.'
s['local_artifacts']['zip_status']='Awaiting packaging. ZIP retains status snapshot at packaging time; consult external receipt for final SHA256.'
(O/'delivery-status.json').write_text(json.dumps(s,indent=2)+'\n',encoding='utf-8')
