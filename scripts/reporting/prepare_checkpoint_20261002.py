from pathlib import Path
import json,hashlib
from datetime import datetime,timezone
root=Path(__file__).resolve().parents[2];out=root/'output/kku-only-20261001'
s=json.loads((out/'summary.json').read_text());a=json.loads((out/'analysis.json').read_text());p=json.loads((out/'provenance-audit-current.json').read_text())
n=s['completed_runs'];pending=204-n;claude=next(c for c in a['counts'] if c['approach']=='kku-claude')
proof=out/'claude-quota-proof-20261002.png'
availability={'recorded_at_utc':datetime.now(timezone.utc).isoformat(),'claude':{'model':'Claude Haiku (latest)','account':None,'used_percent':100,'reset_time':None,'evidence':proof.relative_to(root).as_posix(),'evidence_sha256':hashlib.sha256(proof.read_bytes()).hexdigest()},'collection':'Stopped at owner request; prepare submission using latest results. No further model requests.','gemini':'51 completed runs; current quota not re-observed.'}
(out/'provider-availability-20261002.json').write_text(json.dumps(availability,indent=2)+'\n')
status=json.loads((out/'delivery-status.json').read_text())
status.update(experiment=f'incomplete: {n}/204; KKU Claude {claude["completed"]}/51; Gemini and both algorithms 51/51',remaining_work=f'{pending} planned Claude identities incomplete; {len(p["issues"])} missing capture screenshots from teammate; owner Classroom submission.',local_continuation_published=False)
status['github'].update(status='local_checkpoint_prepared_pending_push',public_screenshots='Public Git excludes original provider screenshots. Local ZIP retains available originals; teammate screenshots are still missing.')
status['classroom']={'status':'owner_will_submit','assignment_url':None,'deadline_local':'2026-10-02T23:59:59+07:00','reason':'Owner explicitly said they will submit themselves before midnight; no destination link required.'}
status['local_artifacts']={'report':'output/kku-only-20261001/SQA_Round2_KKU_Only_20261002.pdf','presentation':'output/kku-only-20261001/SQA_Round2_KKU_Only_20261002.pptx','zip':'output/SQA_Round2_KKU_Only_20261002_Evidence.zip','verification':'output/kku-only-20261001/delivery-verification-20261002.json'}
status['latest_local_test_update']={'completed':n,'pending':pending,'claude_completed_runs':claude['completed'],'claude_projects':claude['projects'],'completed_added_since_friend_checkpoint':n-174,'new_captures':11,'published':False,'classroom_submitted':False,'receipt':'docs/SUBMISSION_READY_20261002.md'}
status['claude_quota']=availability['claude'];status['provenance_limitations']={'issues':len(p['issues']),'issue':'missing capture screenshot','recreated':False}
(out/'delivery-status.json').write_text(json.dumps(status,indent=2)+'\n')
doc=f'''# ชุดส่งล่าสุด 2 ตุลาคม 2026 — กลุ่ม 14

สำเร็จ {n}/204 รอบตามแผนทีม: CMA-ES 51, FSCS-ART 51, KKU Gemini 51, KKU Claude {claude['completed']}. เหลือ {pending} identities ที่ไม่ completed; ดู pending-runs.csv. 204 เป็นแผนทีม ไม่ใช่จำนวนขั้นต่ำที่อาจารย์กำหนด งานทดลองยังไม่ครบ

เจ้าของงานสั่งเตรียมชุดส่งจากผลล่าสุดหลังโควตา Claude 100% และจะส่ง Classroom เองก่อนเที่ยงคืนวันที่ 2 ตุลาคม เวลาไทย ไม่มีการส่ง Classroom โดย agent

## ไฟล์ส่ง

- รายงาน: output/kku-only-20261001/SQA_Round2_KKU_Only_20261002.pdf
- สไลด์ 16 หน้า: output/kku-only-20261001/SQA_Round2_KKU_Only_20261002.pptx
- ZIP ส่วนตัว: output/SQA_Round2_KKU_Only_20261002_Evidence.zip (เก็บไฟล์ภาพที่มีจริงและหลักฐานทั้งหมดที่ได้รับ)
- Demo: presentation/demo-guide-kku-only.md
- ผลหลักและ verification: output/kku-only-20261001/summary.json, analysis.json, delivery-verification-20261002.json

## ผลและข้อจำกัดที่ต้องบอกตามจริง

เก็บ Claude ใหม่ 11 attempts ผ่าน KKU เท่านั้น ใช้ Haiku/Sonnet ตาม metadata, original prompt, response style Normal; บัญชีที่ไม่ได้สังเกตเป็น null. ไม่มีการขอ prompt เพิ่มหลังเจ้าของงานเลือกเตรียมชุดส่ง

Compress/102 และ Lang/101 เพิ่ม completed. Mockito/101 และ /102 ประเมินจากคำตอบที่เก็บแล้วและใช้ผลจริงใน summary. Server busy, refusal, truncated และ JacksonDatabind scaffold เก็บเป็น failed ไม่สร้าง coverage. v67–v72 policies/source snapshots เปิดเผยการประมวลผล, whole-method exclusions จาก fixed compiler และ fixed-only pruning สูงสุดสองครั้ง; retained assertions ไม่ปรับตาม buggy outcomes. v69 guard ผิดพลาดก่อน evaluation มี receipt แยก

Execution audit ของ completed records ผ่านตาม receipts ล่าสุด แต่ provenance ยังพบ {len(p['issues'])} missing capture screenshot issues. เพื่อน push เฉพาะ Git และเจ้าของงานไม่มี private ZIP จากเพื่อน จึงยังไม่มีภาพต้นฉบับเหล่านั้น ไม่สร้างภาพย้อนหลังและไม่อ้างว่า ZIP มีครบทุกภาพ

## การส่งต่อ

ผู้ทำต่อใช้ branch test พร้อม ZIP ล่าสุดเพื่อรับภาพส่วนตัวที่มีในเครื่องนี้ อย่าใช้ตัวเลขในเอกสาร checkpoint เก่าเป็นสถานะล่าสุด. PDF/PPTX ชื่อเก่าและ ZIP เก่าเป็นประวัติ. ตำแหน่ง runtime/worktrees และวิธี audit อยู่ใน docs/KKU_ONLY_CONTINUATION.md. ตรวจ delivery-status.json สำหรับ publication ที่สำเร็จจริง
'''
(root/'docs/SUBMISSION_READY_20261002.md').write_text(doc,encoding='utf-8')
readme=root/'README.md';old=readme.read_text(encoding='utf-8');start=old.index('# SQA Project 2.2')
readme.write_text(f'''## Latest submission checkpoint: {n}/204 — 2 October 2026

CMA-ES, FSCS-ART and KKU Gemini: 51/51 each; KKU Claude: {claude['completed']}/51. Experiment incomplete: {pending} identities remain. Final current PDF/deck use suffix `_20261002`; local private evidence ZIP uses `_20261002_Evidence`. See `docs/SUBMISSION_READY_20261002.md` and `output/kku-only-20261001/delivery-status.json`.

Completed execution evidence is audited. Provenance still has {len(p['issues'])} missing teammate capture screenshots; only originals available on this machine are packaged. No missing evidence recreated. Owner will submit Classroom themselves before midnight Bangkok time. Historical checkpoint filenames and documents below do not describe the latest state.

'''+old[start:],encoding='utf-8')
for name in ['docs/HANDOFF_TO_TEAM.md','docs/HANDOFF_PLAN_20261002_TH.md']:
    path=root/name;text=path.read_text(encoding='utf-8')
    if not text.startswith('> Current checkpoint:'):
        path.write_text('> Current checkpoint: อ่าน docs/SUBMISSION_READY_20261002.md และ delivery-status.json ก่อน เอกสารด้านล่างเป็น handoff/checkpoint เก่า เก็บไว้เป็นประวัติ\n\n'+text,encoding='utf-8')
print(json.dumps({'completed':n,'pending':pending,'provenance_issues':len(p['issues'])}))
