# Demo: Csv-1 ครบสี่วิธี

## ก่อนนำเสนอ

เปิด Report/SQA_Round2_Report.pdf และ Presentation/SQA_Round2.pptx.
ใช้Linux/WSL+Java11+Defects4J3.0.1. ไม่ต้องlogin KKUหรือใช้โควตาสดเพื่อreplaytests.
ตัวอย่างบนเครื่องออม จากrootของชุดส่ง:

```bash
python3 Presentation/replay-demo.py \
  --d4j /home/team/sqa-round2/defects4j/framework/bin/defects4j \
  --worktrees /home/team/sqa-round2/worktrees \
  --output /fresh/path/csv-demo
```

Outputต้องยังไม่มี; ถ้ามีattemptแล้วใช้dirnameใหม่. ตัว script ตรวจsuite/runtime/source hashesก่อนรัน
และใช้ CPU1lockเดียวกับworkerอื่น. ไม่เขียนทับผลทดลองหรือสั่งAPI.
ผลแต่ละstageอยู่ในapproach/evaluation; summary/receiptของrehearsalแยกจากcanonicalrecords.

## ลำดับการพูด

1. บอกขอบเขต: Csv-1บั๊กเดียว/หนึ่งtarget class/4methods. Methods count30/30/21/18.
2. เปิดโค้ด CMAES/FSCSART และalgorithm-targets.json: strategyเลือกinput, helperสร้างfixture,
   fixedobservationsสร้างoracleแล้วemitJUnit. ไม่เรียกCMAfitnessว่าcoverage.
3. เปิด AI1/AI2 Prompt/prompt.md, settings.json และraw-response.txt แสดงว่าใช้KKUจริง
   ไม่เปิดAPIkeysหรือไฟล์accounts. ชุดนี้ใช้fixedcontext ต้องอธิบายตรงไปตรงมา.
4. เปิด AI1 TestCode/sources/.../ExtendedBufferedReaderTest.java method lineNumberDoesNotDoubleCountCRLF:
   input A\r\nB; fixedผ่านแต่buggyคาด1ได้0.
   Gemini method testCarriageReturnLineNumber: input a\rb\nc; fixedผ่านแต่buggyคาด2ได้1.
5. แสดงfixedสองรอบและbuggylogs. AIพบfaultแต่algorithmsไม่พบในbounded domainนี้.
6. เปิดcoverage/summary.csv และExperiment/summary.csv. Lines31/37,31/37,36/37,37/37;
   branches13/26,13/26,22/26,23/26. แสดงinput-domain/timing limitationsก่อนสรุป.

ถ้าlive replayใช้เวลานานให้ใช้ผลซ้อมที่เก็บไว้ประกอบ แต่บอกว่าเป็นผลซ้อม/ผลบันทึก
ไม่เรียกภาพlogว่าการรันสด. ไม่มีการรับรองผลทั้งdatasetหรือการทำครบทุกproject.

## ผลซ้อมที่มีแล้ว

`rehearsal/receipt.json` ยืนยันทั้งสี่วิธีตรงผล canonical:
fixed ผ่านสองรอบทุกวิธี, buggy failures 0/0/1/1 และ coverage ตรงตาราง
แต่ละวิธีมี `rehearsal/<approach>/evaluation/record.json` พร้อม logs/coverage
ผลนี้เป็นการ replay suites เดิมบนเครื่องออม ไม่ใช่ generation repeat ใหม่
อ่าน source ของ assertion และ log พร้อมกันระหว่างนำเสนอ
