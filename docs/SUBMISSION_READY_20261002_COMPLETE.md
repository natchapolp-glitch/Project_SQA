# ชุดส่งอัปเดตภาพครบ — 2 ตุลาคม 2026

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
