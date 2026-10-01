from pathlib import Path
root=Path(__file__).resolve().parents[2]
target=root/'scripts/reporting/build_kku_analysis_v4.py'
assert not target.exists()
text=(root/'scripts/reporting/build_kku_analysis_v3.py').read_text(encoding='utf-8')
old='ภาพส่วนตัวของรอบที่เพื่อนทำเพิ่มถูกยกเว้นจาก public Git ผู้ส่งต่อยืนยันว่าได้รับเฉพาะ branch จึงยังไม่มีไฟล์ภาพต้นฉบับเหล่านั้นในเครื่องนี้ raw response, metadata, source archives และ logs ที่ได้รับยังตรวจได้ตามขอบเขต audit ไม่สร้างภาพหรือหลักฐานย้อนหลัง ภาพที่เครื่องนี้เก็บเองยังคงอยู่ใน ZIP ส่วนตัว'
new='วันที่ 2 ตุลาคมได้รับ ZIP เพิ่มภาพจากเพื่อน และนำเข้าภาพ provider-screen.png ที่ขาด 54 รายการ โดยไฟล์คำตอบและ metadata ที่อยู่คู่กันตรงกับของเดิม ไม่เขียนทับโค้ดหรือผลทดลองและไม่สร้างภาพย้อนหลัง บันทึก ZIP SHA, image SHA และการนำเข้าไว้ใน results/validation/provider-image-recovery-20261002/import-receipt.json ภาพที่เพิ่มไม่ได้อยู่ใน manifest เดิมของ ZIP ที่ได้รับ จึงวัด SHA แยกและจัดทำ manifest ใหม่ในชุดส่งที่แก้แล้ว ภาพส่วนตัวคงอยู่ใน ZIP ส่วน public Git เก็บเฉพาะ receipts และผลตรวจ'
assert old in text
text=text.replace(old,new).replace('report-20261002.md','report-20261002_COMPLETE.md')
target.write_text(text,encoding='utf-8')
