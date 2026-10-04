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
