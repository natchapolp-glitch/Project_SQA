# ชุดส่งล่าสุด: Csv-1 บั๊กเดียว / 4 วิธี

ผู้ใช้เลือกส่งกรณีศึกษาหนึ่งบั๊กตามรูปแบบงานเพื่อน เมื่อ 4 ตุลาคม 2026
ชุดปัจจุบันอยู่ที่ `output/round2-single-bug-20261004` และ ZIP
`output/SQA_Round2_Csv1_4Methods_20261004.zip` ไม่รวมผล 17 projects เก่าในตารางหรือภาคผนวกชุดนี้
ประวัติและผลที่ไม่ผ่านยังอยู่ใน repository เดิม

## สิ่งส่งมอบ

- [รายงาน PDF 9 หน้า](../../output/round2-single-bug-20261004/Report/SQA_Round2_Report.pdf)
- [สไลด์ PowerPoint 12 หน้า](../../output/round2-single-bug-20261004/Presentation/SQA_Round2.pptx)
- [คู่มือ demo พร้อมคำสั่ง](../../output/round2-single-bug-20261004/Presentation/demo-guide.md)
- [ตารางผล 4 วิธี](../../output/round2-single-bug-20261004/Experiment/summary.csv)
- [README ของชุดส่ง](../../output/round2-single-bug-20261004/README.md)
- [ZIP สำหรับส่งต่อ/ส่งงาน](../../output/SQA_Round2_Csv1_4Methods_20261004.zip)

มี code, configurations, frozen helper/runtime, inputs, prompts, AI raw responses,
JUnit sources/archives, fixed/buggy logs, JUnit XML, coverage และ source bindings ที่ใช้จริง
AI ทั้งสองใช้ผลที่เก็บผ่าน KKU IntelSphere เท่านั้น ไม่มีคำขอ AI เพิ่มในรอบจัดชุดส่ง

## ผลจริง

| วิธี | JUnit methods | Fixed failures รอบ 1 / 2 | Buggy failures | Lines | Branches |
|---|---:|---:|---:|---:|---:|
| CMA-ES | 30 | 0 / 0 | 0 | 31/37 | 13/26 |
| FSCS-ART | 30 | 0 / 0 | 0 | 31/37 | 13/26 |
| KKU Sonnet 5 | 21 | 0 / 0 | 1 | 36/37 | 22/26 |
| KKU Gemini 3.5 Flash Lite | 18 | 0 / 0 | 1 | 37/37 | 23/26 |

ทั้งสอง AI พบ fault เดียวกันของ Csv-1 ไม่ใช่สองบั๊ก
99 methods รวมจากสี่ suites ไม่ใช่ 99 บั๊กหรือ 99 scenarios ที่ไม่ซ้ำ
Oracle/context ใช้ fixed source และ input domains ต่างกัน จึงไม่อ้างว่าเป็นการเปรียบเทียบที่ควบคุมทุกเงื่อนไข
รายงานบอกข้อจำกัดของ coverage และเวลาแต่ละขั้นตอน

## การตรวจและ demo

ซ้อม archives เดิมผ่าน Defects4J 3.0.1 / Java 11 / CPU 1 slot ใน fresh worktrees ครบทั้งสี่วิธี
ผ่าน fixed สองรอบ, buggy และ coverage ตรง canonical records ของบีม
ดู [receipt ของการซ้อม](../../output/round2-single-bug-20261004/Presentation/rehearsal/receipt.json)
ผลซ้อมไม่เพิ่มจำนวนบั๊กหรือ generation repeats

ทดสอบคำสั่งสร้าง algorithm suites ใหม่ได้วิธีละ 30 tests
FSCS-ART source ตรงเดิม; CMA-ES ต่างเฉพาะ floating point หลักท้ายของ vectors
Assertions/targets ตรงเดิม ผลสร้างใหม่เก็บแยกจากตารางและยังไม่ใช้แทน canonical evaluation

รายงานตรวจภาพทุกหน้า; สไลด์ตรวจภาพทุกหน้า พร้อม package/layout, native tables และ chart workbook
ตรวจ hashes ของ copied evidence, relative links, ZIP CRC และ hashes ทุกไฟล์ก่อนส่ง
ไม่ได้ตรวจเปิดด้วยโปรแกรม PowerPoint โดยตรง

## ข้อความให้ทีม

ถึงแชมป์และบีม:

> ออมทำชุดส่ง Csv-1 บั๊กเดียวครบ 4 วิธีแล้วครับ มี PDF/สไลด์/โค้ด/tests/prompts/configs/results และ demo
> ใช้ผล Csv ที่รับตรวจแล้ว ซ้อม Defects4J ครบ 4 วิธี ผลตรงเดิม ไม่มีการเรียก KKU เพิ่ม
> ขอใช้ชุด `output/round2-single-bug-20261004` และ ZIP ชื่อ Csv1_4Methods เป็นชุดส่งปัจจุบัน
> พัก candidate/854/new runs สำหรับชุดนี้ ไม่ต้องรัน Csv ซ้ำ ประวัติทั้งหมดเก็บไว้ใน repo ครับ

ผู้ใช้ส่ง Classroom และนำเสนอ demo เอง ชุดหนึ่งบั๊กนี้ไม่แสดงว่าทำครบทุก Java project ตามเอกสารวิชา
ไม่มีการเปลี่ยน Gate A, primary cohort, queue หรือหลักฐาน sealed เดิม
