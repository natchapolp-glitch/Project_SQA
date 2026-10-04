# Demo สำหรับนำเสนอรอบ 2

1. เปิด README และ Report/data/summary.json อธิบายเป้า 500 bugs / 2,000 jobs กับจำนวนที่ทำจริง
2. เปิด test source, actual prompt และ raw answer ของ Csv-1 ในโฟลเดอร์ทั้งสี่วิธี
3. แสดงผลที่วัดไว้โดยไม่เรียก AI:

```bash
python3 -B Presentation/demo.py --case Csv-1
```

4. บน Ubuntu WSL เครื่องเดิม สาธิตการรัน archives เดิม:

```bash
python3 -B Presentation/demo.py --case Csv-1 --replay \
  --output /home/team/sqa-round2/demo-presenter-fresh-20261004
```

ใช้ Java 11 และ Defects4J 3.0.1; output ต้องไม่เคยมีอยู่ และยังมี worktrees เดิม
CPU lock ใช้หนึ่ง slot ถ้า batch ใช้อยู่ คำสั่งจะบอก busy และหยุด ไม่ได้รออัตโนมัติ
ระหว่าง batch ให้แสดงหลักฐานใน Presentation/rehearsal แทน ถ้ามี REHEARSAL_PENDING.md
แปลว่ายังไม่ได้ซ้อม live replay และไม่ควรกล่าวว่าซ้อมผ่านแล้ว

บนเครื่องใหม่ เตรียม Csv 1b/1f และ compile ก่อน แล้วระบุ --worktrees/--d4j ให้ตรงเครื่อง:

```bash
mkdir -p /tmp/sqa-demo/Csv-1
defects4j checkout -p Csv -v 1b -w /tmp/sqa-demo/Csv-1/b
defects4j checkout -p Csv -v 1f -w /tmp/sqa-demo/Csv-1/f
defects4j compile -w /tmp/sqa-demo/Csv-1/b
defects4j compile -w /tmp/sqa-demo/Csv-1/f
python3 -B Presentation/demo.py --case Csv-1 --replay \
  --worktrees /tmp/sqa-demo --d4j /absolute/path/to/defects4j \
  --output /tmp/sqa-demo-output-new
```

5. แสดงตัวอย่างไม่ผ่าน: Lang-1/Sonnet OUTPUT_INCOMPLETE และ Cli-1 Hamcrest harness failure
6. อธิบายว่า DONE คือ fixed twice / buggy / coverage รันครบ; fault_detected คือผลอีกช่องหนึ่ง
7. แสดงตารางขอบเขต 500 ที่ยัง PENDING/QUOTA_PAUSED และ full-inventory CSV 854 ที่เก็บแยกกัน

Replay ใช้ suite เดิม ไม่เพิ่มจำนวน bugs หรือ generation rounds และไม่ต้องใช้ API key
ถ้าคืนนี้ยังไม่ครบ ให้ระบุจำนวนที่ทำจริงจาก snapshot ไม่ใช้ 500 เป็นจำนวนที่เสร็จ

## บท demo ประมาณ 5 นาที

- นาที 0–1: เปิด `Report/data/summary.json` ของชุดที่กำลังนำเสนอ อ่านจำนวนจริง
  แยก `planned_bugs`, `recorded_bugs`, `bugs_four_methods_recorded` และ
  `bugs_four_methods_evaluated` เป้า 500 ไม่ใช่จำนวนที่ทำเสร็จ
- นาที 1–2: แสดง Csv-1 ทั้งสี่วิธีด้วยคำสั่งขั้น 3 และเปิด
  `Presentation/rehearsal/receipt.json` ถ้ามี ทุกวิธีประเมินครบแต่ไม่พบ fault
  อธิบายว่า fixed ผ่านสองครั้งเพิ่มความเชื่อมั่นในการตรวจ suite เดิม ไม่ใช่ generation สองรอบ
- นาที 2–3: แสดงตัวอย่างตรวจพบ fault จริงด้วยสองคำสั่งด้านล่าง
  แล้วเปิด `measurement/record.json` และ log ของ fixed-1/fixed-2/buggy
  ภายใต้ `Experiment/evaluations/<case>/<method>/offline/run-final`
- นาที 3–4: เปิด Lang-1/Sonnet ที่ output ไม่ครบ และ Cli-1 ที่ classpath ขาด Hamcrest
  ผลสร้างโค้ดไม่ผ่านกับ infrastructure failure ต้องแยกกัน ทั้งสองอย่างไม่ใช่ product fault
- นาที 4–5: เปิดตาราง/กราฟ coverage ในรายงาน อ่าน denominator ของแต่ละวิธีจากชุดนี้
  แล้วแสดง code/configurations/prompts/raw answers/archives และ `SHA256SUMS.txt`
  ระบุข้อจำกัดและงานที่ยังค้างจาก snapshot ที่ส่งจริง

คำสั่งแสดงผลที่ตรวจพบ fault ไม่รัน Java และไม่เรียก AI จึงใช้ระหว่าง batch ได้:

```bash
python3 -B Presentation/demo.py --case Compress-1 --method cmaes
python3 -B Presentation/demo.py --case Time-1 --method fscs-art
```

หลักฐานเดิมของ Compress-1/CMA-ES: fixed failures 0 และ 0; buggy failures 3
คือ `generated4`, `generated9`, `generated10` จัดเป็นหนึ่งบั๊กที่ตรวจพบ ไม่ใช่สามบั๊ก
Time-1/CMA-ES และ Time-1/FSCS-ART มี fixed failures 0 ทั้งสองรอบ
และ buggy failures วิธีละ 1 ทั้งสองวิธีตรวจบั๊กเดียวกัน ไม่เพิ่มจำนวน unique bugs
ผลตัวอย่างนี้เป็นหลักฐานที่มีอยู่แล้ว ไม่ใช่ผลทั้งหมดของเป้า 500

ถ้าใช้ snapshot สำรอง 17 บั๊ก ต้องพูดว่าเป็น checkpoint 17 บั๊ก;
ถ้าใช้ชุด final ให้อ่านตัวเลขใหม่จากไฟล์ใน ZIP final ห้ามนำตัวนับสดที่ใหม่กว่าไปแทน
ผลใน ZIP เก่า ขณะ batch ใช้ CPU ให้แสดง stored results และหลักฐาน rehearsal
อย่าเปิด `--replay` ชนกับ worker ที่กำลังรัน
