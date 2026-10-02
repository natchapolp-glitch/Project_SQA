# ชุดส่งล่าสุดก่อน 23:00 — 17 บัค

ใช้ไฟล์ลงท้าย **20261002_DEADLINE** สำหรับการส่งล่าสุด ไฟล์ HAIKU/COMPLETE รุ่นก่อนเป็น checkpoint เก่า

## ผลที่มีจริง

- ผลหลัก prompt เดิม 183/204 รอบ เหลือ 21 รอบที่ยังไม่ completed
- CMA-ES 51/51, FSCS-ART 51/51 และ KKU Gemini 51/51 ครอบคลุม 17 บัคจาก 17 โปรเจกต์
- KKU Claude 30/51 รอบ ครอบคลุม 15/17 โปรเจกต์ ผลนี้รวม Haiku 25 รอบ และ Sonnet เดิม 5 รอบ ไม่มีการเปลี่ยนชื่อรุ่น
- หากกำหนด Haiku เท่านั้น: ผลหลักรวม 178/204 รอบ, Haiku ครอบคลุม 13/17 โปรเจกต์ ยังไม่มี Haiku ที่รันครบใน Chart, Collections, Closure และ JxPath
- Time-1/101 clarification มี 30 เทส ผ่าน fixed ซ้ำและมีผล buggy/coverage เป็นผลเพิ่มเติมแยกเงื่อนไข ไม่บวกเข้าผลหลัก prompt เดิม
- ผลหลักรวม 4,828 เมธอดเทส ผลเพิ่มเติม Time 30 เมธอด ไม่ใช่จำนวนบัคหรือ unique test scenarios

## สิ่งที่ทำเพิ่มจาก checkpoint 181

1. ซ่อมและประเมินคำตอบ Haiku เดิม JacksonXml-1/102: 26 เทสครบ fixed/buggy/coverage ไม่พบ fault
2. ซ่อมและประเมินคำตอบ Haiku เดิม JacksonDatabind-1/101: 30 เทสครบ ไม่พบ fault
3. เก็บทุก failed attempt และประกาศ processing policy v84-v88 ก่อน execution ใช้ fixed diagnostics เท่านั้น ไม่ส่ง logs ให้ AI และไม่เปลี่ยน assertions ของเทสที่เก็บไว้
4. ตรวจ execution audits: baseline 171/171, Claude fresh 27/27, Gemini fresh 39/39, clarification 1/1 ไม่มี issues
5. ตรวจ provenance 203 records ไม่มี issues ภาพ JPEG เดิม 7 รายการแปลงเป็น PNG พร้อม hash receipt และเก็บ JPEG ต้นฉบับ
6. ลอง JxPath clarification ได้คำอธิบายไม่มี Java source จึงบันทึก generation_failed; หน้า KKU แสดง Claude quota 100% เก็บภาพจริงไว้
7. ปรับรายงาน 9 หน้าและสไลด์ 17 หน้า ตรวจภาพทุกหน้าและตรวจโครงสร้างสไลด์ ตาราง/กราฟแก้ไขได้ ยังไม่ได้เปิดด้วย PowerPoint จริง

## ไฟล์สำหรับส่ง

- `output/kku-only-20261001/SQA_Round2_KKU_Only_20261002_DEADLINE.pdf`
- `output/kku-only-20261001/SQA_Round2_KKU_Only_20261002_DEADLINE.pptx`
- `output/SQA_Round2_17Bugs_20261002_DEADLINE_Evidence.zip`
- `output/kku-only-20261001/delivery-verification-20261002_DEADLINE.json`
- `output/kku-only-20261001/package-verification-20261002_DEADLINE.json`

เริ่มอ่านรายงานหน้าแรกและเอกสารนี้ก่อน ไฟล์รุ่นก่อนคงไว้เพื่อประวัติ ส่วนแผน 850 บัคไม่ใช่ผลของชุดส่ง 17 บัค

## ยังไม่ครบอะไร

ผลหลัก Claude ยังไม่ completed 21 รอบ ดู `pending-runs.csv` หากต้องใช้ Haiku ทั้งหมด ต้องแทน Sonnet เดิมอีก 5 รอบด้วย ผล prompt clarification ต้องรายงานแยกจาก prompt เดิม โควต้าบัญชีปัจจุบันเต็มและไม่ทราบเวลาที่รีเซ็ต จึงยังรับรองว่าครบงานทดลองไม่ได้

ไฟล์พร้อมส่งตามผลที่มีจริง ไม่ใช่หลักฐานว่าได้ส่ง Classroom แล้ว ผู้ใช้ต้องนำไฟล์ส่งในช่องทางของอาจารย์ GitHub ของชุดล่าสุดให้ตรวจ `delivery-status.json`; อย่านำสถานะ published ของ checkpoint เก่ามาอ้างว่าไฟล์ล่าสุดเผยแพร่แล้ว
