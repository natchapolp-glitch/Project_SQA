# Checklist งานข้อ 2.2 จาก PDF โจทย์

สถานะจากหลักฐานที่ aggregate เมื่อ 2026-10-01T07:12:39.718047+00:00 (UTC)
ขอบเขต: เลือก active bug หมายเลขต่ำสุดหนึ่งรายการต่อ Java project รวม 17 projects; ไม่ใช่ทุก bug ใน Defects4J

| เงื่อนไข | สถานะ | หลักฐาน/งานที่เหลือ |
|---|---|---|
| พัฒนาอัลกอริทึมและทดลองทุกรายการ Java projects | เสร็จตามแผน algorithm | CMA-ES/FSCS-ART source และ 102/102 final algorithm runs; เลือก 1 bug/project, 3 seeds, 30 proposed inputs |
| ใช้ AI-Assisting Tools ที่เลือกกับทุก project | ยังไม่ครบ | claude: 14/17 projects, 20 responses, 20 complete; intellisphere: 17/17 projects, 51 responses, 49 complete; responses รวมข้อความผิดพลาด ไม่เท่ากับ valid suites |
| บันทึก coverage, efficiency และ fault detection | มีผลจริงและผลล้มเหลว | มี raw logs, test archives, coverage XML/CSV และเวลา; AI มี 69/102 complete และบันทึกคำตอบ 71/102 รอบ; AI เวลาเป็น UI observation upper bound |
| เปรียบเทียบทุกวิธีและสรุปผล/ปัญหา | ยังไม่ครบสี่วิธี | มี descriptive paired comparison ของสองอัลกอริทึม; กลุ่มที่มีผลครบสี่วิธี 20 กลุ่ม |
| รายงาน/source/test/results/ภาพหรือ diagram/prompt/config ทำซ้ำได้ | มีฉบับตามหลักฐานปัจจุบัน | PDF, Markdown, SVG/PNG, source, frozen hashes, actual JUnit, archives, logs, AI prompts และคำสั่งทำซ้ำ; เพิ่มผล AI แล้วสร้างฉบับสุดท้าย |
| Presentation และ Demo | เตรียมไฟล์แล้ว | PPTX และ demo-guide พร้อมตัวอย่าง algorithm และ AI จริง; ทีมต้องซ้อมนำเสนอ |
| README อธิบายงานและชื่อ/รหัสสมาชิก | มีแล้ว | ชื่อและรหัส 4 คนอ้างจากรายงานรอบแรก |
| ส่ง GitHub และ Google Classroom | ยังไม่ได้ส่งการเปลี่ยนแปลงรอบนี้ | งานอยู่บนเครื่องใน branch test; การ push ล่าสุดถูก GitHub ปฏิเสธ 403 เพราะบัญชี aarktik ไม่มีสิทธิ์เขียน; ชุด ZIP คือ output/SQA_Round2_Submission.zip พร้อม manifest และ SHA-256 (สร้างด้วย package_submission.py) |
| นำเสนอและสาธิตต่อผู้สอน | ทีมต้องดำเนินการ | ใช้ PPTX/demo-guide และแสดง logs ที่ตรวจสอบแล้ว |

## งานที่เหลือ

1. เก็บ AI รอบที่ยังขาดหลังบริการเปิดให้ใช้งาน โดยใช้เฉพาะ prompt เดิม ดู provider-status.json และ missing-runs.csv
2. ตรวจผล compile/parse/fixed failures จาก incomplete-runs.csv; หากเปลี่ยนการประมวลผลให้แยก cohort ใหม่และเก็บผลเดิม
3. ตรวจรายงาน สไลด์ และ ZIP ฉบับปัจจุบันร่วมกันก่อนส่ง ไม่อ้างว่าข้อมูลที่ยังขาดประเมินสำเร็จแล้ว
4. ส่งงานผ่าน GitHub/Classroom และนำเสนอ demo ตามช่องทางและเวลาของรายวิชา

## หลักฐานการตรวจและการแก้ไข

- `output/submission/evidence-audit.json` ตรวจ hashes, fixed/buggy logs, counters, archives, budget และ repair lineage
- Cli: แก้ JUnit/Hamcrest dependency แล้วประเมิน archive เดิม 6 รอบ; เก็บก่อน/หลังและผลล้มเหลวเดิม
- JxPath: ตัด generated21 ที่ไม่ผ่าน fixed validation แล้วเหลือ 29 tests; ไม่ใช้ buggy outcome เลือก test
- Mockito: ใช้ checkout แยกสำหรับ FSCS-ART พร้อม driver hash/snapshot; เวลาได้รับผลจาก shared host load
- AI: source-order cap ไม่เกิน 30, IntelSphere UI suffix cleanup, filename hint removal ใน v2/v3, fixed-only pruning ไม่เกินสองรอบ, v4/v5 fixed-API/JUnit compatibility, v6-v29 parser/compile/fixture recovery ตาม fixed API ที่เปิดเผย; เก็บคำตอบและทุกการแก้ไข ไม่ส่ง evaluation logs ให้บริการ
- AI: Auto Router เลือกโมเดลต่างกันได้; null ไม่แทนด้วยศูนย์และ matched comparison ใช้เฉพาะกลุ่มที่ครบ
- `docs/lessons-learned.md` และ `docs/study-protocol.md` อธิบาย oracle/type-only/fixture และ sampling limitations

WSL distribution ที่ใช้งานได้ชื่อ `Ubuntu`; Java 11 และ Defects4J 3.0.1 ใช้งานได้แล้ว
PDF โจทย์กำหนด Java projects ทุกรายการ แต่ไม่ได้ระบุจำนวน bugs ต่อ project ข้อสรุปของชุดทดลองนี้จำกัดอยู่ใน sample ที่ระบุ
