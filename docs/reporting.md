# การรวมผลและสร้างเอกสารรอบที่ 2

ตัวรวมผลอ่านหลักฐานจริงจาก `results/study/**/record.json` แยกการทดลองที่สมบูรณ์ ออกจากงานที่ยังไม่รัน งานที่ล้มเหลว และข้อมูลผิดรูปแบบ ผล pilot เดิมอยู่ในไฟล์แยกและไม่รวมในสถิติการทดลองใหม่

## คำสั่ง

ใช้ Python 3.10 ขึ้นไป คำสั่งรวมผลใช้ standard library ไม่ต้องติดตั้ง package

```bash
python -m pip install -r scripts/reporting/requirements.txt
python scripts/reporting/aggregate.py --results results/study/round2-v4-20260929 --manifest results/study/round2-v4-20260929/study-manifest.csv
python scripts/reporting/build_figures.py
```

หากยังไม่มี manifest ให้ละ `--manifest` โปรแกรมจะระบุว่าไม่สามารถตรวจความครบถ้วนได้ ไม่ถือว่าจำนวนไฟล์ที่พบเท่ากับจำนวนงานทั้งหมด

```bash
python scripts/reporting/build_report.py --source output/submission/report.md --output output/submission/SQA_Round2_Report.pdf
```

คำสั่งสร้าง PDF ใช้ ReportLab และ Tahoma บน Windows (หรือระบุ `--font` กับ `--bold-font`) ข้อความมาจาก report.md ซึ่งตัวรวมผลสร้างจาก record.json ข้อมูลจะยังระบุว่างานไม่ครบจนกว่าผลทดลองและหลักฐานตามโจทย์จะครบจริง

สไลด์สร้างด้วย `scripts/reporting/build_slides.mjs` ต้องใช้ `@oai/artifact-tool` ที่มากับ Codex โดยตั้ง `SQA_RUNTIME_ROOT` และ `SQA_PRESENTATION_SKILL` (โฟลเดอร์ skills/presentations ที่มี container_tools) และตั้ง `SQA_PRESENTATION_TMP_DIR` เป็นโฟลเดอร์ build ใหม่ทุกครั้ง แล้วสั่ง `node scripts/reporting/build_slides.mjs` ทุกครั้งที่ข้อมูลเปลี่ยนให้สร้างรูป รายงาน PDF และสไลด์ใหม่ พร้อม render และตรวจหน้าทุกหน้าก่อนส่ง

## รูปแบบข้อมูล

ตรวจหลักฐานหลังทุก worker จบด้วย `python scripts/reporting/audit_evidence.py`
ตัวตรวจเทียบ hashes, fixed/buggy logs, raw coverage counters, archive integrity,
budget/retained tests และ repair lineage โดยไม่ใช้ข้อมูลจำลองในผลจริง จากนั้น
สร้าง ZIP ด้วย `python scripts/reporting/package_submission.py` จะได้
`output/SQA_Round2_Submission.zip`, checksum และ file manifest ภายใน ZIP

แต่ละ record เป็น JSON object ที่มี `run_id`, `project`, `bug_id`, `generator`, `seed`, `budget`, `status`, `test_count`, `compile_status`, `fault_detected`, `line_covered`, `line_total`, `branch_covered`, `branch_total`, `duration_seconds`, `artifact_path` ฟิลด์เพิ่มเติมคงอยู่ใน summary.json

- `generator`: `cmaes`, `fscs-art`, `claude` หรือ `intellisphere`
- `status`: `complete` เมื่อประเมินเสร็จ ผล fault_detected อาจเป็น false ได้ ส่วน `partial`, `blocked`, `error`, `timeout`, `planned` ไม่ถือว่าเสร็จ โปรแกรมยอมรับ completed/success/ok เพื่ออ่านผลเก่าได้
- `compile_status`: `passed`, `failed` หรือ null โปรแกรมรองรับ pass/success/ok/compiled และ fail/error/compile_failed เป็นชื่อแทน
- `fault_detected`: true, false หรือ null ห้ามใช้ 0/1 หรือ string แทน boolean
- Coverage เป็นจำนวนเต็มไม่ติดลบ ต้อง covered ไม่เกิน total ถ้ายังไม่ได้วัดให้ null ทั้งคู่
- `duration_seconds`: จำนวนวินาทีไม่ติดลบหรือ null
- `artifact_path`: ตำแหน่งหลักฐานชุดทดสอบและ logs ไม่ใช่หลักฐานว่าผลนั้นถูกต้องด้วยตัวมันเอง

manifest เป็น CSV หรือ JSON list มี `project`, `bug_id`, `generator`, `seed`, `budget` เป็นกุญแจจับคู่ รองรับฟิลด์เสริมเช่น target_classes/status ชื่อไฟล์และ run_id ไม่ใช้แทนกุญแจนี้ จึงสามารถพบ record ที่ blocked แล้วระบุว่าเป็นงานไม่สมบูรณ์ โดยไม่แจ้งเป็นงานที่หายไปซ้ำอีก

## ตัวหารและขอบเขตข้อสรุป

- **Fault detection rate ต่อ bug** ใช้จำนวนคู่ project/bug ที่ตรวจพบอย่างน้อยหนึ่งครั้ง หารด้วยจำนวนคู่ที่ประเมินได้อย่างน้อยหนึ่ง run โดยนับ bug ซ้ำหลาย seed เพียงครั้งเดียว รายงาน **fault rate ต่อ run** แยกต่างหาก โดยใช้เฉพาะ record ที่ผ่าน validation, status complete, compile passed และ fault_detected เป็น boolean
- **จำนวน bug** นับคู่ project/bug_id ไม่ซ้ำ แยกจากจำนวน run และอินพุตที่เผยพฤติกรรมแตกต่าง
- **Compile rate** ใช้เฉพาะ run ที่ทราบผล compile passed/failed การไม่รันไม่ใช่ compile failed
- **Coverage macro** เฉลี่ย covered/total ต่อ run ที่เสร็จและคอมไพล์ผ่าน **Coverage micro** ใช้ผลรวม covered/ผลรวม total ของ run เหล่านั้น ทั้งคู่ตัดค่าที่หายไปและ total=0 ออก แสดงจำนวน observations เสมอ
- การรันหลาย seed ทำให้ micro นับโค้ดเดียวกันซ้ำ จึงไม่ใช่ union coverage ทั้ง project และไม่ใช้แทนสถิติต่อ project
- Summary รวมทั้งวิธีเป็นข้อมูลเชิงพรรณนา เมื่อ budgets/bugs/seeds ไม่เท่ากันห้ามใช้จัดอันดับความสามารถ ตรวจ `project-method-summary.csv` ซึ่งแยก budget และ `matched-groups.csv` ก่อนเปรียบเทียบแบบจับคู่
- ผลซ้ำที่มี run_id และกุญแจเหมือนกันถูกแยกเป็น invalid record ไม่รวมในตัวชี้วัด หากกุญแจเดียวกันแต่คนละ run_id จะเป็นการรันซ้ำจริง แสดง `ambiguous_repeats` ใน matched groups และต้องเลือก replication protocol ก่อนอนุมานเชิงสถิติ
- ความผิดพลาดของข้อมูลบันทึกไว้ใน issues.csv ไม่เติมค่าทดแทน ค่า null แสดงเป็นช่องว่างใน CSV

## ไฟล์ผลลัพธ์

- `summary.json` ข้อมูลทั้งหมดพร้อมเวลา UTC และ SHA-256 ของ record ที่อ่าน
- `normalized-runs.csv` ข้อมูลมาตรฐานทุก run
- `method-summary.csv` สถิติตามวิธีและตัวหาร
- `project-method-summary.csv` สถิติตาม project วิธี และ budget
- `matched-groups.csv` ความครบถ้วนของสี่วิธีที่ project/bug/seed/budget เดียวกัน
- `missing-runs.csv` งานตาม manifest ที่ยังไม่พบ record
- `incomplete-runs.csv` งานที่พบแต่ยังไม่เสร็จหรือข้อมูลไม่ผ่าน validation รวมสถานะ failed/interrupted และเหตุผลจาก runner เมื่อมี
- `issues.csv` ไฟล์เสีย ค่าไม่ถูกชนิด จำนวน coverage ขัดกัน หรือผลซ้ำ
- `pilot-summary.csv` หลักฐาน Lang-1 differential-v3 ที่แยกจากการทดลองใหม่
- `report.md` รายงานภาษาไทยที่สร้างจากข้อมูลชุดเดียวกัน

## สถานะปัจจุบันและข้อจำกัดของหลักฐาน

manifest ปัจจุบันวางแผน 204 runs: 17 โปรเจกต์ × 4 วิธี × 3 run indices ที่ budget 30 โดยเลือก active bug หมายเลขต่ำสุดหนึ่ง bug ต่อโปรเจกต์ ขอบเขตนี้ไม่ใช่ทุก bug ใน Defects4J และไม่มีการอ้างว่าอาจารย์อนุมัติวิธีสุ่มตัวอย่างแล้ว หลักฐานล่าสุดต้องอ้างจาก `summary.json`; ห้ามใช้จำนวนตามแผนเป็นจำนวนที่เสร็จจริง

distribution ที่ใช้งานได้มีชื่อ `Ubuntu` การตรวจครั้งก่อนด้วยชื่อ `Ubuntu-24.04` ผิดพลาด สภาพแวดล้อม Java 11 และ Defects4J เดิมยังใช้งานได้ ชุด v4 แก้ target ที่มีเฉพาะ fixed และ oracle ที่ยาวเกิน Java string limit แล้ว ผลที่ยังไม่มีต้องคงเป็น missing และ failure จาก harness ต้องไม่ถูกนับเป็นการตรวจพบ bug บันทึก AI ใช้คำตอบจริงจาก Claude และ IntelSphere พร้อมกติกา local processing ที่เปิดเผยใน docs/ai-workflow.md

รายงานมี `ai-provider-attempts.csv` แสดงรุ่นโมเดลและสถานะทุก original-prompt
response และ `ai-provider-service-retries.csv` สำหรับ server-busy attempts เดิม
ก่อนลองซ้ำ `four_method_comparison` ใช้เฉพาะ project/bug/run-index/budget
ที่ทั้งสี่วิธีประเมินครบ ส่วน macro coverage รวมของวิธีที่มี sample ต่างกัน
ใช้เป็นข้อมูลเชิงพรรณนาเท่านั้น เวลา AI เป็น UI observation upper bound
ไม่ใช่ model compute time ดูค่าที่ไม่ทราบใน metadata แทนการเติมศูนย์

## การแก้ dependency ของ Cli

การรัน Mockito ใน checkout แยกใช้ `scripts/study/run_isolated.py` หลัง setup
ของ worker หลักเสร็จ ตัวอย่างสำหรับ seed 101 (ใช้ seed 102/103 ใน checkout
แยกตามชื่อ run ได้):

```bash
python3 scripts/study/run_isolated.py --batch results/study/round2-v4-20260929 --d4j /home/aomsin/sqa-round2/defects4j/framework/bin/defects4j --worktrees /home/aomsin/sqa-round2/worktrees-isolated --project Mockito --generator fscs-art --seed 101
```

ทุก run ใช้ frozen source/target inventory เดิม เก็บ execution driver hash และ
setup logs ไว้ใน Mockito/parallel-setup ไม่ใช้ checkout เดียวกับ worker หลัก

Cli ใน Defects4J รุ่นที่ใช้ตั้ง `junit.jar` ไปยัง JUnit 4.12 ที่ไม่มี Hamcrest ทำให้ JUnit 4 test runner ล้มก่อนเริ่ม test ตัวแก้ `scripts/study/repair_cli_junit.py` เปลี่ยนเฉพาะ dependency path ไปยัง `junit-4.12-hamcrest-1.3.jar` ที่ framework มีอยู่แล้ว แล้วประเมิน archive เดิมซ้ำ ก่อนรันให้ตรวจว่า Cli ทั้ง 6 รอบจบแล้วและไม่มี worker ใช้ checkout ของ Cli อยู่ คำสั่งนี้ใช้ได้ครั้งเดียวกับ batch เดิมและจะปฏิเสธ configuration ที่ไม่ตรง

```bash
python3 scripts/study/repair_cli_junit.py --d4j /home/aomsin/sqa-round2/defects4j/framework/bin/defects4j --batch results/study/round2-v4-20260929
```

สำเนา before/after, jar SHA-256 และเหตุผลอยู่ใน batch/environment-repair ส่วนผลล้มเหลวเดิมอยู่ใน results/validation/round2-v4-20260929-cli-classpath-failure ไม่รวมเป็นผลสำเร็จในสถิติ

## ข้อจำกัดของหลักฐาน pilot

Legacy pilot ใช้ Lang-1 เพียง bug เดียว seed 2026 budget 30 โดยสร้าง representation เพื่อเผยความผิดพลาด NumberUtils ที่ทราบแล้ว จึงเป็นการทดลองที่ใช้ความรู้เกี่ยวกับ defect ประกอบ และไม่เทียบตรงกับการสร้าง test โดยไม่เห็น defect ทั้งชุด

15 และ 21 ใน summary เป็นจำนวนอินพุตที่ให้ผลต่างไม่ซ้ำ ไม่ใช่จำนวนบั๊ก ไม่ใช่ coverage และไม่ใช่ผลจาก 15 หรือ 21 projects หลักฐาน fixed/buggy JUnit logs ยืนยันการตรวจพบ Lang-1 ในขอบเขตนี้เท่านั้น

ข้อ 2.2 กำหนด Java projects ทุกรายการ การเลือก bug อย่างน้อยหนึ่งตัวต่อ project เป็นขอบเขตการทดลองที่โครงการกำหนดเอง ไม่ใช่การอนุมัติจากผู้สอน และไม่หมายความว่าทดสอบทุก active bug

## ตรวจความถูกต้องของตัวรวมผล

เมื่อต้องประเมิน fixed oracle ที่ถูกปฏิเสธ ให้จบ worker ของ project ก่อน แล้วใช้:

```bash
python3 scripts/study/repair_fixed_oracles.py --batch results/study/round2-v4-20260929 --d4j /home/aomsin/sqa-round2/defects4j/framework/bin/defects4j --projects JxPath
```

คำสั่งนี้ตัดเฉพาะ generated methods ที่ระบุว่าล้มเหลวบน fixed ไม่อ่าน buggy outcomes
เพื่อเลือก test ผลก่อนแก้คงอยู่ใน results/validation และต้องอธิบาย follow-up rule
ในการเปรียบเทียบ ส่วนการทดลองครั้งแรกยังมีความผิดพลาด ไม่ถือว่าผ่านแต่แรก

```bash
python -m unittest discover -s scripts/reporting -p "test_*.py"
```

การทดสอบใช้ข้อมูลสังเคราะห์ใน temporary directory แยกจาก results/study เพื่อตรวจการไม่นำ null ไปแทนศูนย์ การตัด run ที่ไม่สมบูรณ์ออกจากตัวหาร และการตรวจ duplicate/invalid records ข้อมูลสังเคราะห์ไม่รวมในรายงานผลจริง
