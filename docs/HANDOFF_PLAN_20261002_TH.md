# แผนส่งต่องาน SQA 2.2 ให้เพื่อน — 2 ตุลาคม 2026

## ข้อความสำหรับส่งให้เพื่อน

ช่วยทำงาน SQA 2.2 ต่อจากชุดไฟล์ล่าสุด โดยใช้ Claude และ Gemini ผ่าน KKU IntelSphere เท่านั้น ตอนนี้ผลที่ตรวจหลักฐานแล้วเสร็จ 174/204 รอบ: CMA-ES 51/51, FSCS-ART 51/51, Gemini 51/51 และ Claude 21/51 เหลือ Claude 30 รอบ รวมทั้งสรุปรายงาน สไลด์ และแพ็กส่งงาน บัญชี natchapol ใช้ Claude Haiku จนโควต้า 100% แล้ว ไม่ทราบเวลารีเซ็ตที่ยืนยันได้

**ต้องรับไฟล์ล่าสุดจากเครื่อง/ชุดส่งต่อด้วย** เพราะผลล่าสุดยังไม่ได้ push ไป GitHub branch `test` หาก clone จาก GitHub อย่างเดียวจะได้ข้อมูลเก่า กรุณาอ่าน checkpoint และตรวจยอดก่อนเริ่ม เพื่อไม่ส่ง prompt ซ้ำรอบที่เสร็จแล้ว

## 1. รับไฟล์และยืนยันจุดเริ่มต้น

- Repository: https://github.com/natchapolp-glitch/Project_SQA/tree/test
- โฟลเดอร์งานล่าสุดบนเครื่องเจ้าของ: `C:\Users\ACER\Documents\ChatGPT\SQAProj\Project_SQA_from_test`
- Commit ล่าสุดที่เผยแพร่: `846fb43c4a3daa8f48febef0da7b559186086acb` ผลหลังจากนี้ยังเป็นงาน local
- รับ source, scripts, results, output, docs, presentation และ `ai-tests/provider-captures/` รวมหลักฐานภาพต้นฉบับแบบส่วนตัว ภาพบางส่วนไม่ได้อยู่ใน public Git
- อ่าน `docs/CLAUDE_HAIKU_NATCHAPOL_CHECKPOINT_20261002.md`, `docs/KKU_ONLY_CONTINUATION.md` และ `results/study/kku-only-20261001/protocol.json`
- รัน `python3 scripts/study/kku_only.py status` จาก root ของ repo แล้วอ่าน `output/kku-only-20261001/pending-runs.csv` ที่สร้างใหม่ ยอดเริ่มต้นควรเป็น 174 complete / 30 pending
- pending CSV ก่อน refresh อาจยังแสดง Closure/102 ว่า missing ทั้งที่ล่าสุดเก็บคำตอบและบันทึก generation_failed แล้ว ต้องใช้สถานะที่ refresh ใหม่

## 2. แบ่งงานที่ทำพร้อมกันได้

- คนทำ AI: เก็บรอบ Claude ที่ค้าง พร้อม prompt, response, screenshot, บัญชี, รุ่นโมเดล และเวลา
- คนประเมิน: รันคำตอบบน fixed/buggy, coverage และ audit เก็บประวัติที่ล้มเหลว
- คนทำรายงาน: เตรียมโครงรายงาน/สไลด์/คำอธิบายวิธีทดลอง แล้วใส่ตัวเลขจากผลที่ audit ล่าสุดเมื่อพร้อม
- กำหนดผู้รวมผลหนึ่งคน เพื่อไม่แก้ primary record หรือส่ง prompt ของ identity เดียวกันพร้อมกัน

## 3. รายการ Claude ที่ยังไม่ complete (30 รอบ)

ทุกรายการใช้ bug_id 1, budget 30 และเลขรอบต่อไปนี้ รายการนี้เป็นคิวที่ยังไม่สำเร็จ ไม่ใช่จำนวน test cases

- Chart: 102, 103
- Closure: 101, 102, 103
- Collections: 102, 103
- Compress: 101, 102
- Gson: 102, 103
- JacksonCore: 102
- JacksonDatabind: 101, 102, 103
- JacksonXml: 101, 102, 103
- JxPath: 101, 102, 103
- Lang: 101, 103
- Math: 101, 103
- Mockito: 101, 102, 103
- Time: 101, 102

เริ่มจากรอบที่ยังไม่เคยได้คำตอบ: Closure/103, JacksonDatabind/102–103, JacksonXml/102–103, JxPath/102–103 และ Mockito/103 แล้วค่อยกลับไปดูคำตอบที่ generation_failed หรือ compile failed ใช้ pending CSV และ record จริงยืนยันก่อนแต่ละรอบ หากโควต้าหรือกำหนดส่งไม่พอ ให้รายงานผลที่ขาดตามจริง

## 4. วิธีเก็บคำตอบจาก KKU

1. เปิด https://gen.ai.kku.ac.th/chat ตรวจบัญชีและโควต้า เลือก Claude รุ่น `claude-haiku-latest` ตามคำสั่งล่าสุดของเจ้าของงาน
2. ใช้ prompt เดิมของโปรเจกต์จาก `results/study/kku-only-20261001/claude/<Project>/ai-context/prompt.md` ส่งในแชทใหม่
3. **ส่งได้เฉพาะ prompt เดิมที่อนุญาตแล้ว** ห้ามส่ง compile/test/coverage logs หรือข้อความขอซ่อมเพิ่มเติมโดยไม่มีอนุญาตใหม่
4. สร้าง capture directory ใหม่ เช่น `ai-tests/provider-captures/kku-only-20261001/claude/<Project>-1/s<index>-i1-<account>-<timestamp>/` ไม่ทับไฟล์เดิม
5. เก็บ `request-start.json`, ภาพบัญชี/โมเดล, เวลาเริ่มและเวลาที่เห็นคำตอบเสร็จ, URL, response export ต้นฉบับ, `provider-screen.png` และ `operator-metadata.json` ตามตัวอย่างที่มีอยู่
6. บันทึก Response Style จริงด้วย รอบ natchapol ล่าสุดใช้ Explanatory; รอบก่อนใช้ Normal ต้องเปิดเผยความต่างในรายงาน ไม่อ้างว่าตั้งค่าเหมือนกันทั้งหมด
7. หากโมเดลปฏิเสธ ไม่มี Java หรือคำตอบขาด ให้เก็บเป็นหลักฐานความล้มเหลว อย่าเขียนคำตอบ AI ขึ้นเอง
8. เมื่อโควต้าเต็ม ให้เก็บภาพและ quota observation แล้วหยุดส่ง prompt

## 5. ประเมินผลและเกณฑ์นับสำเร็จ

สภาพแวดล้อมปัจจุบัน: WSL distro `Ubuntu`, Java 11, timezone `America/Los_Angeles`; worktrees อยู่ `/home/aomsin/sqa-round2/worktrees`; Defects4J อยู่ `/home/aomsin/sqa-round2/defects4j/framework/bin/defects4j` บนเครื่องเพื่อนต้องเตรียมและระบุ path ที่มีอยู่จริง

ตัวอย่างคำสั่งใน WSL จาก root ของ repo (แทน Project/index/capture ให้ถูกต้อง):

```bash
python3 scripts/study/evaluate_provider_normalized_v60.py \
  --tool intellisphere --project Closure --seed 103 \
  --capture ai-tests/provider-captures/kku-only-20261001/claude/Closure-1/s103-i1-ACCOUNT-TIMESTAMP \
  --batch results/study/kku-only-20261001/claude \
  --worktrees /home/aomsin/sqa-round2/worktrees \
  --d4j /home/aomsin/sqa-round2/defects4j/framework/bin/defects4j
```

- ตรวจ source ของ driver และ history ก่อนใช้ โดยเฉพาะ identity ที่เคยใช้ v60 แล้ว ห้ามทับ capture/history/record เดิม หาก namespace ชนต้องสร้าง driver เวอร์ชันใหม่
- ใช้ Java fences ที่สมบูรณ์, เลือกตาม source order ไม่เกิน budget 30, ตรวจ fixed สองครั้ง, buggy หนึ่งครั้ง และ fixed coverage
- รอบ complete ต้องมีผลการประเมินและ audit รองรับ การมีคำตอบ AI อย่างเดียวไม่นับ complete
- เทสควรผ่าน fixed ส่วน buggy อาจผ่านหรือ fail; fail ที่สัมพันธ์กับ bug เป็นผลตรวจพบ ไม่จำเป็นต้องบังคับให้ทุกโปรเจกต์ตรวจพบ bug
- ห้ามแก้ expected values เพื่อให้ผ่าน ห้ามเลือกเทสจากผล buggy ห้ามแต่ง coverage หรือแทนค่าที่ไม่ได้วัดด้วยศูนย์
- วิธีประมวลผล/ตัดเทสที่ทำในเครื่องต้องเปิดเผยและเก็บก่อน–หลัง การแก้ compatibility เพิ่มต้องใช้เวอร์ชันใหม่ ไม่แก้ script ที่ถูกบันทึก hash ไปแล้ว
- เวอร์ชันล่าสุดที่สร้างคือ v66; ก่อนสร้าง v67 ให้ตรวจว่าไฟล์ยังไม่มีจริง
- รอบซ้ำ Lang/102 และ Math/102 ของ natchapol ถูกเก็บไว้แต่ไม่เพิ่มยอด และห้ามทับผลเดิม

## 6. ตรวจหลักฐานและอัปเดตยอด

```bash
python3 scripts/reporting/audit_kku_only_v4.py \
  --results results/study/kku-only-20261001/claude \
  --output output/kku-only-20261001/claude-evidence-audit.json
python3 scripts/study/kku_only.py status
python3 scripts/reporting/audit_kku_provenance_v2.py
```

- รอบ Claude ใหม่ที่ complete มี audit ผ่าน 18/18; รวม baseline ที่ reuse อีก 3 เป็น 21
- Provenance ของข้อมูลเก่ายังมีช่องว่าง จึงยังอ้างไม่ได้ว่า audit ทั้งชุดผ่าน ต้องดู issue จริงและแนบคำอธิบาย ไม่สร้างหลักฐานย้อนหลัง
- อัปเดต README, delivery-status และ checkpoint จากยอดที่ตรวจแล้ว ห้ามรัน checkpoint script เก่าที่ hardcode ยอด 169/174 หลังยอดเปลี่ยน

## 7. รายงาน สไลด์ และชุดส่งงาน

- PDF ปัจจุบันเป็น checkpoint 140 รอบ สไลด์เป็น checkpoint 136 รอบ ยังไม่ใช่ยอด 174 ต้องสร้างใหม่
- อัปเดตการเปรียบเทียบ CMA-ES, FSCS-ART, KKU Claude และ KKU Gemini: จำนวนรอบสำเร็จ/ล้มเหลว, จำนวน methods, line/branch coverage, เวลา และ fault detection พร้อม denominator และ missing values
- เปิดเผยรุ่นโมเดล/บัญชี/Response Style, การ reuse ข้อมูล, การตัดเทสและซ่อม compatibility, เวลา AI ที่รวมการเตรียม UI และข้อจำกัด ไม่สรุปผู้ชนะจากข้อมูลไม่ครบโดยไม่มีเงื่อนไข
- ใช้ `scripts/reporting/build_kku_analysis.py` และ `scripts/reporting/render_kku_report.py` หลังตรวจ dependencies/paths ใน source แล้วตรวจ PDF ที่ได้จริง
- ใส่ source code, results/logs, diagram, prompt/config และ README วิธีทำซ้ำตามโจทย์ เตรียม demo ตาม `presentation/demo-guide-kku-only.md`
- ตรวจรายชื่อสมาชิก รหัสนักศึกษา ไฟล์ข้อกำหนด `SQA_Project_2026 (2).pdf` และ checklist ให้ตรงฉบับส่ง
- แพ็ก ZIP ล่าสุดพร้อม checksum และตรวจว่าไฟล์เปิดได้ แยกภาพที่มีข้อมูลบัญชี/แชทส่วนตัวสำหรับส่งกลุ่มออกจากไฟล์ที่จะเผยแพร่สาธารณะ
- Commit/push branch `test` เมื่อรับผิดชอบชุดไฟล์ร่วมกันแล้ว และตรวจผลบน GitHub; ห้ามระบุว่าเผยแพร่แล้วก่อน push สำเร็จ
- เจ้าของงานต้องยืนยัน Classroom assignment และเวลาปิดรับ แล้วส่งจริงพร้อมเก็บหลักฐานรับส่ง ขณะนี้สถานะคือยังไม่ส่ง

## 8. นิยามงานพร้อมส่ง

- จำนวน 204 เป็นแผนทดลองของกลุ่ม: 17 โปรเจกต์ × 4 วิธี × 3 รอบ ไม่ใช่ขั้นต่ำที่โจทย์บังคับ และไม่จำเป็นต้องเพิ่มเป็น 800 test cases
- ถ้าทำครบแผน: 204 identity ต้องมีการประเมินสำเร็จและหลักฐานตรวจสอบได้ ไม่ใช่แค่ส่ง prompt ครบ 204 ครั้ง
- ถ้าทำไม่ครบก่อนปิดรับ: ส่งผลที่ตรวจแล้ว พร้อมจำนวน failed/missing และข้อจำกัดตามจริง ไม่เขียนว่าครบเงื่อนไขทั้งหมด
- ความครบของงานยังต้องดูรายงาน/โค้ด/ผล/diagram/prompt/config/demo/README และการส่งปลายทางร่วมกัน จำนวนเทสอย่างเดียวรับรองคะแนนไม่ได้

## รูปแบบแจ้งผลกลับเจ้าของงาน

แจ้ง project/run ที่ทำเพิ่ม, บัญชีและโมเดล, capture path, จำนวน retained methods, fixed/buggy/coverage, audit result, ยอด complete/pending ล่าสุด, โควต้า, commit ที่ push และสิ่งที่ยังค้าง พร้อมลิงก์หรือ path หลักฐาน
