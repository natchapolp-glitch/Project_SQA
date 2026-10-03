Latest Champ integration: [Aom 4a0e699c / selected pair, recipe sources and Windows stage creation](api854/CHAMP_AOM4A_INTEGRATION_TH.md). Shared-v5 is still five bugs / 124 of 691 declarations, with old runtime pins blocked against the merged code. Account intake is paused. No live pilot or KKU requests in this integration.

Received Aom team acceptance: [Champ b6051367 / selected Gate A input checker](api854/AOM_GATE_A_B605_ACCEPTANCE_TH.md). Protocol/runner and fixture recipe bindings are checked explicitly. Final shared inputs, limits/framing/reset/expiry and joint Gate A remain pending. Primary completion is still 0.

Current Aom work (3 October 2026): read [AOM_CONTINUATION_V5_TH.md](api854/AOM_CONTINUATION_V5_TH.md) and [AOM_INDEPENDENT_PREPARATION_TH.md](api854/AOM_INDEPENDENT_PREPARATION_TH.md). The owner now requests branch `aom`, 854 active bugs, four approaches, one repeat per bug, and Claude Sonnet 5 / Gemini 3.5 Flash Lite through KKU only. New primary completion is **0**. Independent source/build preparation and recovery evidence do not approve Gate A.

The 17-bug / 204-run checkpoints below are historical. Preserve their evidence separately from the new 854-bug cohort.

Historical checkpoint: **181/204 completed, 23 pending**. Use artifacts ending `_20261002_HAIKU` and `docs/SUBMISSION_READY_20261002_HAIKU.md`. Earlier COMPLETE checkpoints below are historical (178 runs). Owner stopped further AI requests for that checkpoint and will submit Classroom themselves.

> Current checkpoint: อ่าน docs/SUBMISSION_READY_20261002_COMPLETE.md และ delivery-status.json ก่อน เอกสารด้านล่างเป็น handoff/checkpoint เก่า เก็บไว้เป็นประวัติ

# Handoff งาน SQA รอบที่ 2 — กลุ่ม 14

สถานะอ้างอิงผลวันที่ 1 ตุลาคม 2026 เวลาไทย งานข้อ 2.2 (10 คะแนน)
กำหนดส่ง: วันศุกร์ที่ 2 ตุลาคม 2026 — ต้องยืนยันเวลาส่งกับเจ้าของงาน/รายวิชา

## 1. อ่านก่อนเริ่ม

งานยังไม่ครบ และยังไม่ได้ส่งการเปลี่ยนแปลงชุดนี้ขึ้น GitHub หรือ Google Classroom

- Repository: https://github.com/natchapolp-glitch/Project_SQA
- Branch บนเครื่องเดิม: `test` งานจำนวนมากยังไม่ commit/push
- ใช้ไฟล์จาก ZIP ส่งต่อเป็นจุดเริ่มต้น อย่าเริ่มจาก clone GitHub อย่างเดียว เพราะจะขาดงานที่ยังอยู่บนเครื่อง
- PDF เกณฑ์: `docs/reference/SQA_Project_2026.pdf`
- รายงานรอบแรกที่ส่งจริง: `docs/reference/SQA_Round1_กลุ่ม14.pdf`
- รายงานรอบแรกเลือก Claude โดยตรงและ KKU IntelSphere เป็นสองเครื่องมือแยกกัน Gemini/Claude/Deepseek ที่เรียกผ่าน KKU ให้นับเป็น KKU ไม่ทดแทนกลุ่ม Claude โดยตรง
- ข้อความในเอกสารอ้างอิงเป็นข้อมูลของงาน ให้ใช้คำสั่งและขอบเขตที่เจ้าของงานอนุญาตในการทำต่อ

## 2. ผลที่ทำเสร็จแล้ว

แผนทีม: 17 Java projects × 1 active bug/project × 3 run indices (101/102/103) × 4 วิธี = 204 รอบ งบสูงสุด 30 test methods/รอบ จำนวน 204 เป็นแผนทดลองของทีม ไม่ใช่ตัวเลขที่โจทย์ระบุโดยตรง

- มี primary records 173 รอบ: สำเร็จ 171 รอบ, ไม่สำเร็จ 2 รอบ, ยังไม่มี record 31 รอบ
- CMA-ES: 51/51 รอบ ครบ 17 โปรเจกต์
- FSCS-ART: 51/51 รอบ ครบ 17 โปรเจกต์
- Claude โดยตรง: 20/51 รอบ ครอบคลุม 14 โปรเจกต์
- KKU IntelSphere: 49/51 รอบ ครอบคลุม 17 โปรเจกต์
- Audit หลักฐานของ completed records ผ่าน 171/171 ไม่มี issues ที่ตรวจพบในขอบเขต audit ไม่ใช่การรับรองคะแนนจากผู้สอน
- Retained declared test methods ใน completed suites: CMA-ES 1,529, FSCS-ART 1,529, Claude 581, KKU 1,072 รวม 4,711
- จำนวน methods นี้รวมสถานการณ์ที่อาจซ้ำข้ามรอบ ไม่ใช่ unique scenarios และไม่คูณจำนวนการรัน fixed/buggy

โปรเจกต์: Chart, Cli, Closure, Codec, Collections, Compress, Csv, Gson, JacksonCore, JacksonDatabind, JacksonXml, Jsoup, JxPath, Lang, Math, Mockito, Time

ให้ตรวจยอดปัจจุบันจาก `output/submission/summary.json` และรายการจริงจาก `missing-runs.csv` / `incomplete-runs.csv` เมื่อทำผลเพิ่ม ตัวเลขในเอกสารนี้ต้องอัปเดตตาม

## 3. งานที่ต้องทำต่อ เรียงตามความสำคัญ

### A. เก็บ Claude ของ Lang, Math และ Time ก่อน

สามโปรเจกต์นี้ยังไม่มีผล Claude โดยตรง ให้เริ่มรอบ 101 เพื่อให้เครื่องมือนี้ครอบคลุมทั้ง 17 โปรเจกต์ก่อน จากนั้นเก็บรอบ 102/103

Claude หน้าเว็บที่สังเกตล่าสุดระบุโควต้าเปิดอีกครั้ง 17:50 น. วันที่ 1 ตุลาคม (อนุมาน Asia/Bangkok) เวลานี้เป็นหลักฐานจากการสังเกตเดิม ไม่ใช่การรับประกันว่าสถานะปัจจุบันเหมือนเดิม ต้องเปิดบริการตรวจอีกครั้ง

### B. ทำ Claude รอบที่เหลือให้ครบแผน

ขาด 31 รอบ:

- Lang, Math, Time: 101/102/103 รวม 9 รอบ
- Closure, Collections, Compress, Csv, Gson, JacksonCore, JacksonDatabind, JacksonXml, Jsoup, JxPath, Mockito: 102/103 รวม 22 รอบ
- Chart, Cli และ Codec ของ Claude ครบทั้งสามรอบแล้ว ไม่ต้องเก็บซ้ำ

### C. กู้ KKU Closure รอบ 102 และ 103

สองรอบนี้เป็น primary failed records ที่ยังคอมไพล์ไม่ผ่าน ไม่มีผล coverage/fault ที่ใช้สรุปได้

- Closure 101 สำเร็จแล้วด้วยชุดที่ผ่าน local fixture/reflection processing และ fixed-only pruning เหลือ 16 methods; ไม่ตรวจพบ sampled bug
- Gemini secondary retries ได้ข้อความอธิบายหรือ Java ถูกตัดกลางคัน
- OpenAI secondary retry ไม่มี Java ที่ประเมินได้
- Deepseek secondary retry ของ Closure 102 ล่าสุดจบด้วย server busy
- KKU Claude agent เคยแสดง daily usage 100% ต้องตรวจสถานะใหม่ก่อนใช้
- ประวัติทั้งหมดอยู่ใน `results/validation/kku-original-prompt-retries/round2-v4-20260929/Closure/` และ `results/validation/ai-source-processing-vN/`

ทางเลือกคือใช้ prompt เดิมขอคำตอบใหม่ผ่าน KKU เมื่อบริการพร้อม หรือซ่อม fixture ในเครื่องโดยเปิดเผยทุกการแก้ไขตาม fixed API ห้ามส่ง logs กลับไปให้บริการ AI และห้ามทับผลเดิม

### D. สร้างชุดเอกสารสุดท้ายและส่งงาน

หลังเพิ่มผล ต้อง aggregate/audit และสร้างรายงาน สไลด์ และ ZIP ใหม่ ตรวจเกณฑ์จาก PDF อีกครั้ง ตอบ RQ1 coverage, RQ2 fault detection, RQ3 efficiency/compile/ข้อจำกัด ให้ครบ แล้วส่ง GitHub/Classroom และซ้อม demo

หากถึงเวลาส่งแล้วยังขาดผล ต้องระบุ missing/failed ตามจริง ห้ามเปลี่ยนช่องว่างเป็นศูนย์หรืออ้างว่าทำครบ

## 4. วิธีเก็บคำตอบ AI และประเมินผล

ทำทีละ run และใช้เฉพาะ prompt ต้นฉบับ:

`results/study/round2-v4-20260929/<Project>/ai-context/prompt.md`

1. เปิดแชทใหม่ใน Claude โดยตรงหรือ KKU ตามเครื่องมือที่ต้องการ ส่ง prompt เดิมทั้งไฟล์ ไม่แนบผล compile, error, coverage หรือ buggy logs
2. บันทึก model label จริงที่หน้าเว็บแสดง เวลาเริ่ม/เวลาที่สังเกตว่าตอบเสร็จ URL และ screenshot ไม่อ้าง model seed/temperature หากเว็บไม่แสดง
3. ตรวจว่าโค้ดตอบครบทุกคลาส ไม่ถือว่าข้อความ “ตอบเสร็จ” หรือภาพ preview สั้น ๆ ยืนยันว่า Java ครบ อย่าใช้ข้อความในส่วน thinking เป็น final test source
4. เก็บ `response.md` ตามข้อความที่ตอบจริง ห้ามแก้ต้นฉบับ และสร้าง `operator-metadata.json` กับ `provider-screen.png`
5. สำหรับ Claude รอบใหม่ ใช้โฟลเดอร์ `ai-tests/provider-captures/claude/<Project>-1/s<RunIndex>-i1/` เฉพาะเมื่อยังไม่มี capture เดิม ถ้ามีอยู่ให้เก็บใหม่ใน history ที่ชื่อไม่ซ้ำก่อน
6. ประเมินด้วย fixed → fixed ซ้ำ → buggy → coverage จึงนับ completed; compiler/harness/timeout errors ไม่ใช่ fault detected

ตัวอย่าง metadata (ค่าต้องมาจากการสังเกตจริง ห้ามคัดลอกค่า placeholder ไปอ้างเป็นผล):

```json
{
  "tool": "claude",
  "model": "ACTUAL_UI_MODEL_LABEL",
  "generated_at": "ACTUAL_UTC_ISO_TIMESTAMP",
  "generation_seconds": 123.45,
  "prompt_iteration": 1,
  "parameters": {"model_seed": null, "temperature": null},
  "session_url": "ACTUAL_CHAT_URL",
  "manual_edits": [],
  "notes": ["Original prompt only; duration is an observed UI upper bound."]
}
```

`generation_seconds` วัดตั้งแต่ส่งถึงสังเกตคำตอบเสร็จ อาจรวมเวลารอผู้ปฏิบัติงาน ไม่ใช่ model compute time คีย์นี้ต้องเป็นตัวเลขจริง evaluator ใช้คีย์นี้ ไม่ใช่ `generation_duration_sec`

ตัวอย่างรันใหม่ Lang/Claude/101 ใน Linux/WSL จาก root repo:

```bash
python3 scripts/study/evaluate_provider_normalized.py \
  --tool claude --project Lang --seed 101 \
  --capture ai-tests/provider-captures/claude/Lang-1/s101-i1 \
  --worktrees /home/aomsin/sqa-round2/worktrees \
  --d4j /home/aomsin/sqa-round2/defects4j/framework/bin/defects4j
```

บนเครื่องเพื่อนต้องเปลี่ยน `--worktrees` และ `--d4j` ให้ตรงเครื่อง ดู `--help` ก่อนใช้ เวอร์ชัน v14–v29 แก้เฉพาะบาง project/run ที่เคยวินิจฉัยไว้ ไม่ใช่ตัวซ่อมทั่วไปสำหรับคำตอบใหม่

สำหรับ KKU Closure ที่มี failed primary อยู่แล้ว ให้เก็บ fresh capture แยกก่อน แล้วใช้ activation script ที่เก็บ old capture/run และ lineage อัตโนมัติ ตัวอย่างชื่อโฟลเดอร์ด้านล่างต้องเปลี่ยนเป็นชื่อที่ยังไม่มี:

```bash
python3 scripts/study/activate_original_prompt_retry_v2.py \
  --tool intellisphere --project Closure --seed 102 \
  --fresh results/validation/kku-original-prompt-retries/round2-v4-20260929/Closure/s102/NEW_CAPTURE \
  --history-tag before-NEW_CAPTURE
```

จากนั้นประเมิน canonical capture `ai-tests/provider-captures/intellisphere/Closure-1/s102-i1` ด้วย driver ที่เหมาะกับคำตอบนั้น activation ไม่นับเป็น completed และ retry ไม่ใช่ independent run เพิ่มจากแผน 204

ถ้า driver abort ก่อนเขียน record ให้เก็บ directory/logs ของ attempt นั้นไว้ก่อนแก้ ไม่ลบหรือรันทับ ถ้าต้องเปลี่ยน processing ให้สร้างเวอร์ชันใหม่พร้อม policy/hash และรักษาหลักฐานก่อน/หลัง อ่านตัวอย่างใน `docs/ai-workflow.md`

## 5. ข้อจำกัดที่ต้องรักษา

- เจ้าของงานอนุญาตให้ส่งเฉพาะ prompt เดิมไป Claude และ KKU IntelSphere ยังไม่อนุญาตส่ง compile/error/coverage/buggy logs ให้ AI ซ่อม
- ห้ามเปลี่ยน production code หรือปรับ build เพื่อทำให้ผลผ่านโดยไม่เปิดเผยการเปลี่ยน protocol
- ห้ามใช้ buggy outcome หรือ hidden detecting tests เลือก/แก้ค่า expected assertions
- ห้ามแก้ frozen source/config หรือ processing versions ที่ถูกใช้และเก็บ hash แล้ว ให้สร้างเวอร์ชันใหม่เมื่อจำเป็น
- ห้ามทับ completed runs, raw responses หรือประวัติเดิม
- ผลที่ซ่อม locally ต้องเรียก AI-assisted with local processing ไม่ใช่ unedited AI output
- ใช้ JUnit ตาม build ของแต่ละโปรเจกต์ ไม่บังคับ JUnit 5 ตามแผนรอบแรก
- ขอบเขตคือ 1 active bug ต่อ project ไม่ใช่ทุก bug ใน Defects4J; 101/102/103 ของ AI เป็น run indices ไม่ใช่ model random seeds
- อธิบายความต่างของโมเดลบน KKU, effort ในการซ่อม, successful subsets และ UI timing ก่อนเปรียบเทียบผล

## 6. ไฟล์ที่ต้องเปิดดู

- `output/submission/summary.json`: ยอดผลและตัวหาร
- `output/submission/missing-runs.csv`: Claude 31 รอบที่ยังขาด
- `output/submission/incomplete-runs.csv`: KKU Closure 102/103
- `output/submission/evidence-audit.json`: audit 171 completed records
- `output/submission/kku-failure-review.json`: 18 กรณี KKU ที่เคยไม่สำเร็จ กู้คืนแล้ว 16 กรณี
- `output/submission/delivery-verification.json`: การตรวจรายงาน/สไลด์และข้อจำกัด
- `ai-tests/provider-status.json`: สถานะบริการที่สังเกตไว้ ไม่ใช่สถานะสด
- `ai-tests/provider-captures/`: primary raw responses/metadata/screenshots
- `results/study/round2-v4-20260929/`: config, manifest, primary generation/evaluation/coverage
- `results/validation/`: ผลก่อนซ่อมและ secondary retries ไม่นับรวมเป็น primary runs
- `docs/ai-workflow.md`, `docs/study-protocol.md`, `docs/submission-checklist.md`: protocol, disclosures และเกณฑ์งาน
- `output/submission/SQA_Round2_Report.pdf`: รายงานล่าสุด 16 หน้า สะท้อนผล 171 รอบแล้ว
- `presentation/SQA_Round2.pptx`: สไลด์ล่าสุด 17 หน้า สะท้อนผล 171 รอบแล้ว
- `presentation/demo-guide.md`: ตัวอย่างผลจริงและขั้นตอน demo
- `output/SQA_Team_Handoff.zip`: ไฟล์รวมสำหรับส่งต่อ

รายงานและสไลด์ผ่านการตรวจภาพ render แล้ว แต่ยังเป็นฉบับระหว่างทำเพราะมีผลขาด ไม่ได้ยืนยันการรันใน native PowerPoint และไม่ใช่หลักฐานว่ากดส่งงานแล้ว

## 7. สภาพแวดล้อมและการย้ายเครื่อง

เครื่องเดิม:

- Repo: `C:\Users\ACER\Documents\ChatGPT\SQAProj\Project_SQA`
- Local branch: `test`
- WSL distribution: `Ubuntu` (ไม่ใช่ `Ubuntu-24.04`)
- Java 11, Python 3.12, Defects4J 3.0.1
- Defects4J: `/home/aomsin/sqa-round2/defects4j/framework/bin/defects4j`
- Worktrees: `/home/aomsin/sqa-round2/worktrees/<Project>/1/f` และ `/b`

ZIP มีโค้ด ผล logs archives prompts รายงานและเอกสาร แต่ไม่มี `.git`, Linux worktrees/runtime, dependencies ใน `tmp/` หรือ environment ที่ติดตั้งครบ เพื่อนต้องติดตั้งเครื่องมือและ checkout revisions ตาม context/config ก่อนประเมินเพิ่ม ตั้ง paths เอง และอ่าน README/`--help`

อย่ารัน generator ทั้ง cohort ใหม่เพื่อเริ่มต่อ เพราะอัลกอริทึมครบแล้ว งานต่อคือ AI ที่ขาด/failed และเอกสารส่ง

## 8. สร้างผลรวม รายงาน และ ZIP ใหม่

หลังทุกชุดผลใหม่ จาก root repo:

```bash
python3 scripts/reporting/aggregate.py --results results/study/round2-v4-20260929 \
  --manifest results/study/round2-v4-20260929/study-manifest.csv
python3 scripts/reporting/audit_evidence.py
python3 scripts/reporting/build_failure_review.py
python3 scripts/reporting/update_checklist.py
python3 scripts/reporting/build_figures.py
python3 scripts/reporting/build_report.py
```

Reporting dependencies อยู่ใน `scripts/reporting/requirements.txt` เครื่องเดิมใช้ Python runtime ที่มี reporting libraries ใน `tmp/reporting-packages` และตั้ง `PYTHONPATH` ไปโฟลเดอร์นั้น เครื่องเพื่อนติดตั้ง dependencies เอง PDF ใช้ฟอนต์ Tahoma ใน Windows ต้องระบุ `--font`/`--bold-font` ใหม่หากไม่มี

สไลด์สร้างด้วย `node scripts/reporting/build_slides.mjs` ต้องตั้ง `SQA_RUNTIME_ROOT`, `SQA_PRESENTATION_SKILL`, `SQA_PRESENTATION_TMP_DIR` ตาม runtime/skill paths บนเครื่องที่ใช้ ตัว runtime ไม่รวมใน ZIP ถ้าไม่มีให้แก้ PPTX ที่แนบด้วย PowerPoint และตรวจทุกสไลด์ พร้อมระบุวิธีตรวจตามจริง

หลังรายงาน/PPTX เสร็จ ให้ render ตรวจทุกหน้า อัปเดต `delivery-verification.json` และสถานะ provider/checklist อย่าเก็บ receipt เก่ามาอ้างกับไฟล์ใหม่

```bash
python3 scripts/reporting/package_submission.py
python3 scripts/reporting/verify_submission.py
```

รอ package เสร็จก่อน verify และอย่าแก้ไฟล์ระหว่างสร้าง ZIP จากนั้นคัดลอก `output/SQA_Round2_Submission.zip` เป็น `output/SQA_Team_Handoff.zip` พร้อม checksum/verification ที่ตรงกัน

## 9. GitHub, Classroom และ Demo

Push ครั้งก่อนถูก GitHub ปฏิเสธ 403 เพราะบัญชี `aarktik` ไม่มีสิทธิ์เขียน repo ยังไม่เคย push branch `test` สำเร็จจากงานนี้

- ใช้บัญชีที่เจ้าของ repo ให้สิทธิ์แล้ว หรือให้เจ้าของ repo นำไฟล์จาก ZIP เข้า repo
- เมื่อสิทธิ์พร้อมและ review ไฟล์แล้ว commit งานบน branch `test` แล้ว push อย่า force-push ทับงานเพื่อน
- อย่า commit ZIP ขนาดใหญ่หรือ environment/credentials โดยไม่ตรวจ `.gitignore`
- ลิงก์ Classroom/เวลาส่งที่แน่นอนยังไม่ได้ให้ไว้ เจ้าของงานต้องยืนยันช่องทางส่ง
- ซ้อม demo จาก logs ที่ audit แล้ว ตัวอย่าง Csv/101 ของ Claude และ KKU มี fixed ผ่านสองครั้งและ buggy assertion failure
- ถ้าทำ live run ใช้ run-id ใหม่ ไม่ทับ cohort ที่รายงาน ใช้ `presentation/demo-guide.md` แบ่งคนอธิบายวิธี ผล ตัวหาร และข้อจำกัด

## 10. ข้อความสั้นสำหรับส่งพร้อมไฟล์

ฝากทำ SQA รอบ 2 ข้อ 2.2 ต่อจาก ZIP ล่าสุดนี้นะ ต้องส่งศุกร์ 2 ตุลาคม งานยังไม่ครบ ตอนนี้สำเร็จ 171/204 รอบ: CMA-ES/FSCS-ART ครบ 102 รอบ, Claude 20 รอบ, KKU IntelSphere 49 รอบ เหลือ Claude 31 รอบและ KKU Closure 102/103 งานแรกคือเก็บ Claude ของ Lang/Math/Time ให้ครบโปรเจกต์ก่อน แล้วตาม missing-runs.csv ต่อ ใช้ prompt เดิมเท่านั้น ห้ามส่ง logs/errors กลับไปให้ AI และต้องเก็บ raw/ผลเดิมทุกครั้ง

รายงาน 16 หน้า สไลด์ 17 หน้า และ audit 171/171 อัปเดตแล้ว หลังเพิ่มผลต้องสร้างรายงาน/สไลด์/ZIP และตรวจใหม่ รายละเอียดคำสั่งและข้อจำกัดอยู่ใน docs/HANDOFF_TO_TEAM.md งานยังไม่ขึ้น GitHub/Classroom; branch test อยู่ในเครื่อง และบัญชีที่ push ก่อนหน้านี้ติด 403 ต้องใช้บัญชีที่มีสิทธิ์ ช่วยยืนยันเวลาส่งกับเจ้าของงานและซ้อม demo ก่อนส่งด้วย
