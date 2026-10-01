# Demo รอบ 2: Claude/Gemini ผ่าน KKU เท่านั้น

ใช้รายงาน/สไลด์ชื่อที่ลงท้าย `_20261002_COMPLETE` ใน `output/kku-only-20261001/` เป็นชุดปัจจุบัน
เปิด `summary.json` และ `analysis.json` ก่อนพูดจำนวนรอบจริง งานยังไม่ครบแผน
ดู `delivery-status.json` ก่อนอ้างว่าได้ส่ง GitHub หรือ Classroom

## 1. อธิบายขอบเขตและ provenance (1 นาที)

เปิด `results/study/kku-only-20261001/protocol.json` และ
`output/kku-only-20261001/study-manifest.csv` แสดงหนึ่ง active bug ต่อ 17 projects,
run indices 101/102/103 และ budget 30 ตามแผนทีม
AI ใหม่เลือก Claude/Gemini ผ่าน KKU โดยตรง ใช้ exact original prompts
ผลเดิมใช้เฉพาะ allowed-family records ที่ผ่าน audit และอ้าง SHA ไม่แก้ model label
ผล Claude-direct และโมเดลอื่นไม่รวมใน comparison หลัก

## 2. ตัวอย่าง AI ที่ตรวจพบ bug (2 นาที)

เปิดผล Claude/Jsoup/101:

```text
results/study/kku-only-20261001/claude/Jsoup/intellisphere-s101-b30/
```

1. ดู `generation/tests/org/jsoup/nodes/DocumentGeneratedTest.java`
   ที่ method `normaliseMovesHtmlAndHeadTextIntoBodyKeepingOrder`
2. แสดง `evaluation/fixed-1/command.log` และ `fixed-2/command.log`
   ว่าไม่มี failing tests
3. แสดง `evaluation/buggy/failing_tests` ว่า assertion ใน method นี้ล้มเหลว
4. เปิด `evaluation/record.json` และ coverage counters: line 46/46,
   condition 17/18, 30 retained methods, fault_detected=true
5. เปิด `ai-tests/provider-captures/kku-only-20261001/claude/Jsoup-1/s101-i1/`
   เพื่อแสดง raw response และ local processing: คำตอบถูกตัด method ท้าย,
   กู้ complete prefix และ source-order cap 30 โดยเก็บก่อน/หลัง hashes

อธิบายว่า final suite เป็น AI-assisted with local processing
ไม่เรียกว่า raw output ที่ใช้ได้โดยไม่แก้ไข และไม่ถือ harness errors เป็น bug

## 3. เปรียบเทียบกับ Gemini ใน identity เดียวกัน (1 นาที)

เปิด `results/study/kku-only-20261001/gemini/Jsoup/intellisphere-s101-b30/`
Gemini มี 16 methods ผ่าน fixed ซ้ำ แต่ไม่ตรวจพบ sampled bug
อ่าน generated assertions ประกอบ Coverage และ fault detection ตอบคนละคำถาม
อ่านกลุ่มที่ครบสี่วิธีใน `analysis.json` เพื่อแสดงตัวหารตรงกัน
ผล successful subsets ทั้งหมดใช้จัดอันดับทั่วไปไม่ได้

## 4. ตัวอย่างอัลกอริทึมและ oracle (1 นาที)

เปิด `scripts/study/generate.py`:
FSCS-ART กระจาย vectors, CMA-ES ใช้ความถี่ fixed behavior เป็น objective
สังเกต fixed สองครั้งเพื่อสร้าง oracle ไม่ใช้ buggy outcome เลือก expected values
เปิด original `results/study/round2-v4-20260929/Jsoup/` และเลือก
`cmaes-s101-b30` / `fscs-art-s101-b30` จาก manifest
แสดง generated source, raw input ledger และ record/coverage จริง
งบ 30 vectors กับ 30 AI methods ไม่เท่ากับ effort/CPU budget เดียวกัน

## 5. ตรวจซ้ำแบบไม่เขียนทับผลทดลอง (1 นาที)

จาก repo root ใน WSL:

```bash
python3 scripts/study/kku_only.py status
python3 scripts/reporting/audit_kku_provenance_v2.py
python3 scripts/reporting/audit_kku_only_v4.py \
  --results results/study/kku-only-20261001/claude \
  --output output/kku-only-20261001/claude-evidence-audit.json
python3 scripts/reporting/audit_kku_only_v4.py \
  --results results/study/kku-only-20261001/gemini \
  --output output/kku-only-20261001/gemini-evidence-audit.json
```

Execution audit ตรวจ completed records; provenance audit ปัจจุบัน 0 issues หลังรับภาพที่ขาด 54 รายการ ไม่ใช้ receipt เก่าที่ผ่านแทนชุดปัจจุบัน การรัน status ใหม่จะเปลี่ยน timestamp/SHA ของ summary จึงต้อง rebuild analysis/report/deck ก่อนส่งหาก refresh หลังล็อกชุดส่ง Audit ไม่ใช่การประเมินคะแนนจากผู้สอน
อย่ารัน evaluator ทับ completed run ระหว่าง demo
ถ้าต้องสาธิตรัน JUnit สด ใช้ archive เดิมกับ checkout แยกและเก็บ log แยก:

```bash
mkdir -p /home/team/sqa-round2/demo
/home/team/sqa-round2/defects4j/framework/bin/defects4j checkout \
  -p Jsoup -v 1f -w /home/team/sqa-round2/demo/Jsoup-1f
/home/team/sqa-round2/defects4j/framework/bin/defects4j test \
  -w /home/team/sqa-round2/demo/Jsoup-1f \
  -s "$PWD/results/study/kku-only-20261001/claude/Jsoup/intellisphere-s101-b30/evaluation/Jsoup-1f-intellisphere.101.tar.bz2"
```

มีการซ้อมจริงด้วย archive เดิมใน checkout แยกแล้ว ดู
`output/kku-only-20261001/demo/rehearsal.json`: fixed มี 0 failing tests,
buggy มี 1 failing test คำสั่งด้านบนใช้ path ใหม่สำหรับทีม ไม่เขียนทับ
checkout ที่ซ้อมไว้ และไม่เพิ่มผลซ้อมเข้า primary cohort

## จุดที่ต้องพูดตามจริง

- AI labels และ versions ต่างกันระหว่าง reused/new records
- UI generation timing เป็น observation upper bound ไม่ใช่ model compute
- Sampling มีหนึ่ง bug ต่อ project และ modified-class coverage
- Provider quota/service failures และ incomplete responses ทำให้ยังขาดผล
- Full provider screenshots อยู่ในแพ็กเกจหลักฐานในเครื่อง อาจมีชื่อแชทอื่น
  จึงไม่เผยแพร่ screenshots ต้นฉบับไป public Git
- ทีมต้องนำเสนอและกดส่งใน Classroom ด้วยช่องทาง/เวลาของรายวิชาจริง
