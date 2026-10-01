from pathlib import Path
import json
root=Path(__file__).resolve().parents[2];out=root/'output/kku-only-20261001'
summary=json.loads((out/'summary.json').read_text());provenance=json.loads((out/'provenance-audit-current.json').read_text())
assert summary['completed_runs']==178 and not provenance['issues']
status=json.loads((out/'delivery-status.json').read_text())
status['remaining_work']='26 planned Claude identities remain incomplete; owner Classroom submission. Missing 54 primary screenshots received and imported; provenance audit now zero issues.'
status['provenance_limitations']={'issues':0,'recovered_screenshots':54,'receipt':'results/validation/provider-image-recovery-20261002/import-receipt.json','scope':'File presence, hashes, matching raw response/metadata, PNG decode and visual spot checks; not independent authentication of past browser activity.'}
status['local_artifacts'].update(report='output/kku-only-20261001/SQA_Round2_KKU_Only_20261002_COMPLETE.pdf',presentation='output/kku-only-20261001/SQA_Round2_KKU_Only_20261002_COMPLETE.pptx',zip='output/SQA_Round2_KKU_Only_20261002_COMPLETE_VERIFIED_Evidence.zip',verification='output/kku-only-20261001/delivery-verification-20261002_COMPLETE.json',zip_status='Pending creation of revised verified package; previous packages preserved.')
status['github']['status']='image_recovery_artifact_update_pending_push'
status['github']['public_screenshots']='Original provider screenshots remain private and are ignored by public Git; all 54 missing primary images received from owner-provided ZIP and imported locally.'
(out/'delivery-status.json').write_text(json.dumps(status,indent=2)+'\n')
doc='''# ชุดส่งอัปเดตภาพครบ — 2 ตุลาคม 2026

ได้รับภาพ provider-screen.png ที่ขาดครบ 54 รายการจาก ZIP ที่เจ้าของงานส่งมา ผลตรวจ provenance ล่าสุด 200 records / 0 issues. นำเข้าเฉพาะภาพที่ขาด ไฟล์คำตอบและ metadata ที่อยู่คู่กันตรงกับของเดิม ไม่เขียนทับผลทดลอง

**คำว่า COMPLETE หมายถึงแก้เรื่องภาพที่ขาดแล้ว การทดลองยังไม่ครบ: 178/204 รอบ เหลือ 26 รอบ** CMA-ES, FSCS-ART และ Gemini 51/51; Claude 25/51. ไม่ส่ง prompt AI เพิ่ม

## ใช้ไฟล์เหล่านี้ส่งและส่งต่อ

- รายงาน: output/kku-only-20261001/SQA_Round2_KKU_Only_20261002_COMPLETE.pdf
- สไลด์: output/kku-only-20261001/SQA_Round2_KKU_Only_20261002_COMPLETE.pptx
- ZIP ที่แก้รายงาน/สไลด์และ manifest แล้ว: output/SQA_Round2_KKU_Only_20261002_COMPLETE_VERIFIED_Evidence.zip
- Demo: presentation/demo-guide-kku-only.md
- Verification: output/kku-only-20261001/delivery-verification-20261002_COMPLETE.json

ZIP ที่เพื่อนเพิ่มภาพและส่งผ่าน Downloads มีภาพครบ แต่รายงาน/สไลด์และ provenance receipt ในนั้นยังเป็น checkpoint ที่ระบุว่าขาดภาพ 54 รายการ อีกทั้งภาพที่เพิ่มไม่อยู่ใน manifest เดิม จึงทำชุด VERIFIED ใหม่พร้อม SHA ของทุกไฟล์ ไม่เขียนทับ ZIP จากเพื่อนหรือ ZIP เก่า

Execution audits ของผลเดิมยังตรวจ hash ตรง: baseline 171/171, Claude fresh 22/22, Gemini fresh 39/39 (รวม secondary หนึ่งชุด). ไม่รันเทสเพิ่มและไม่เปลี่ยนจำนวนรอบ

หลักฐานนำเข้า: results/validation/provider-image-recovery-20261002/import-receipt.json เก็บ ZIP SHA-256, SHA ของภาพ 54 ไฟล์ และ provenance-before.json. CRC และ manifest ที่มีใน ZIP ต้นทางตรวจผ่าน ภาพที่เพิ่มถูกตรวจ PNG และวัด SHA ใหม่ ไม่สร้างภาพย้อนหลัง ภาพเต็มอยู่ใน ZIP ส่วนตัวและไม่ push public Git

เจ้าของงานจะส่ง Classroom เองก่อนเที่ยงคืนวันที่ 2 ตุลาคม เวลาไทย ดู delivery-status.json สำหรับสถานะ GitHub/ZIP ที่ทำสำเร็จจริง เอกสารและไฟล์ชื่อเก่าเป็นประวัติ
'''
(root/'docs/SUBMISSION_READY_20261002_COMPLETE.md').write_text(doc,encoding='utf-8')
readme=root/'README.md';text=readme.read_text(encoding='utf-8');start=text.index('# SQA Project 2.2')
readme.write_text('''## Latest checkpoint: recovered screenshots — 178/204 runs

All 54 missing primary provider screenshots have been received from the owner-provided ZIP and imported locally; current provenance audit: 200 records, zero issues. Experimental results unchanged: 178/204 completed, 26 incomplete. COMPLETE refers to screenshot recovery, not experiment completion.

Current report/deck use `_20261002_COMPLETE`. Current private ZIP uses `_20261002_COMPLETE_VERIFIED_Evidence`. Read `docs/SUBMISSION_READY_20261002_COMPLETE.md` and `output/kku-only-20261001/delivery-status.json`. Private images remain outside public Git. Owner will submit Classroom themselves before midnight Bangkok time.

'''+text[start:],encoding='utf-8')
for name in ['docs/HANDOFF_TO_TEAM.md','docs/HANDOFF_PLAN_20261002_TH.md','docs/KKU_ONLY_CONTINUATION.md']:
    path=root/name;text=path.read_text(encoding='utf-8').replace('docs/SUBMISSION_READY_20261002.md','docs/SUBMISSION_READY_20261002_COMPLETE.md')
    if name.endswith('KKU_ONLY_CONTINUATION.md'):
        text=text.replace('Teammate screenshots omitted from Git were not received; the current provenance receipt discloses those missing images. Rechecking all capture files requires the missing teammate originals as well.','All 54 missing primary teammate screenshots have now been received and imported; current provenance receipt has zero issues. Private originals are retained locally and in the new verified evidence package.')
    path.write_text(text,encoding='utf-8')
old=root/'docs/SUBMISSION_READY_20261002.md';text=old.read_text(encoding='utf-8')
old.write_text('> ภาพที่ขาดได้รับแล้ว: ใช้ docs/SUBMISSION_READY_20261002_COMPLETE.md เป็นสถานะล่าสุด เอกสารด้านล่างเป็น checkpoint ก่อนรับภาพ\n\n'+text,encoding='utf-8')
demo=root/'presentation/demo-guide-kku-only.md';text=demo.read_text(encoding='utf-8').replace('`_20261002`','`_20261002_COMPLETE`').replace('provenance audit ยังรายงาน missing capture screenshot จากเพื่อนตามจริง','provenance audit ปัจจุบัน 0 issues หลังรับภาพที่ขาด 54 รายการ')
demo.write_text(text,encoding='utf-8')
verify=(root/'scripts/reporting/verify_kku_delivery_v2.py').read_text().replace('SQA_Round2_KKU_Only_20261002.pdf','SQA_Round2_KKU_Only_20261002_COMPLETE.pdf').replace('SQA_Round2_KKU_Only_20261002.pptx','SQA_Round2_KKU_Only_20261002_COMPLETE.pptx').replace('delivery-verification-20261002.json','delivery-verification-20261002_COMPLETE.json')
(root/'scripts/reporting/verify_kku_delivery_v3.py').write_text(verify,encoding='utf-8')
preview=(root/'scripts/reporting/render_checkpoint_previews_20261002.py').read_text().replace('SQA_Round2_KKU_Only_20261002.pdf','SQA_Round2_KKU_Only_20261002_COMPLETE.pdf').replace('kku-pdf-preview-20261002-v3','kku-pdf-preview-20261002_COMPLETE').replace('final-previews-20261002','final-previews-20261002_COMPLETE')
(root/'scripts/reporting/render_complete_previews_20261002.py').write_text(preview,encoding='utf-8')
package=(root/'scripts/reporting/package_kku_submission_v2.py').read_text().replace('SQA_Round2_KKU_Only_20261002_Evidence.zip','SQA_Round2_KKU_Only_20261002_COMPLETE_VERIFIED_Evidence.zip').replace('package-verification-20261002.json','package-verification-20261002_COMPLETE.json').replace('Some screenshots from teammates were omitted from public Git and have not been received; see provenance-audit-current.json.','All 54 missing primary teammate screenshots have now been received from the owner-provided ZIP; see current provenance audit and import receipt. COMPLETE refers to recovered screenshots; the 204-run experiment is still incomplete.')
(root/'scripts/reporting/package_kku_submission_v3.py').write_text(package,encoding='utf-8')
print('Prepared recovered-image checkpoint and reproducible verifier/package scripts.')
