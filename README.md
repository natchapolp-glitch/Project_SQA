# งานล่าสุด — solo offline pilot สำหรับแผนเต็ม

**Branch ทำงานปัจจุบัน: `Team`** — ย้าย checkpoint จาก `aom 026e83ed` ตามคำสั่งผู้ใช้
เก็บ `aom` และประวัติเดิมไว้ครบ เมื่อโปรเจคเสร็จค่อยจัดชุดไฟล์สำหรับ repo ใหม่
ตาม [แผนย้าย repo](docs/api854/REPOSITORY_MIGRATION_PLAN_TH.md)

เริ่ม implementation แล้ว อ่าน [ลำดับงานทีละขั้น](docs/api854/SOLO_STEP_BY_STEP_TH.md)
และ [ชุด pilot ปัจจุบัน](output/round2-solo-pilot-20261004/README.md)
มี runner เลือกบั๊ก/resume/checkpoint และตารางผลกับงานค้าง; ยังไม่เรียก KKU ในชุดใหม่
ดู [ตัวนับล่าสุด](output/round2-solo-pilot-20261004/Report/data/summary.json)
สำหรับจำนวนที่รันจริง ไม่อ้างว่า planned jobs ทั้งหมดเสร็จแล้ว

## แผนขยาย — โครงชุดส่งตาม repo เพื่อน

ผู้ดำเนินงานล่าสุดทำทุกหน้าที่เองคนเดียวบน branch `Team` รวมการรัน ตรวจผล และสิ่งส่งมอบ
ไว้ใน workflow เดียว ไม่ต้องรอคำรับจากเพื่อน; ประวัติแบ่งงานเดิมเก็บไว้เป็น provenance

ผู้ใช้ขอแผนทดลอง 854 บั๊กด้วย 4 วิธีและจัดหมวดสิ่งส่งมอบตาม repo เพื่อน
อ่าน [แผนเต็ม](docs/api854/AOM_FRIEND_LAYOUT_FULL_SUBMISSION_PLAN_TH.md)
และ [blueprint โครงไฟล์/รายการงาน](docs/api854/plans/friend-layout-full-v1/README.md)
Blueprint ตอนวางแผนมี 854 บั๊ก / 3,416 planned jobs ทั้งหมด `PENDING`; ผลปัจจุบันดูชุด pilot ข้างต้น
ผลและไฟล์ด้านล่างเป็นชุด Csv-1 ที่ทำเสร็จแล้ว ไม่ใช่ผลครบ 854 บั๊ก

---

# ชุดส่งที่ทำแล้ว — Csv-1 บั๊กเดียว / 4 วิธี

กรณีศึกษา Defects4J Csv-1 เปรียบเทียบ **CMA-ES, FSCS-ART, KKU Claude Sonnet 5 และ Gemini 3.5 Flash Lite**
ชุดส่งปัจจุบันอยู่บน branch `aom` ใช้ผลจริงหนึ่ง generated suite ต่อวิธี

## ไฟล์ส่งงาน

- **[ดาวน์โหลด ZIP](output/SQA_Round2_Csv1_4Methods_20261004.zip)** — รวมเอกสารและหลักฐานของชุดนี้
- [รายงาน PDF 9 หน้า](output/round2-single-bug-20261004/Report/SQA_Round2_Report.pdf)
- [PowerPoint 12 หน้า](output/round2-single-bug-20261004/Presentation/SQA_Round2.pptx)
- [Demo: คำสั่งและลำดับนำเสนอ](output/round2-single-bug-20261004/Presentation/demo-guide.md)
- [README ของชุดส่ง](output/round2-single-bug-20261004/README.md)
- [ตารางผล](output/round2-single-bug-20261004/Experiment/summary.csv) / [วิธีทดลอง](output/round2-single-bug-20261004/Experiment/protocol.md)

ใน ZIP มี code, configurations, frozen helper/inputs, prompt และ raw AI output, test sources/archives,
ผล fixed/buggy, JUnit XML, coverage และ source hashes สำหรับทำซ้ำ
AI ใช้หลักฐานเดิมจาก **KKU IntelSphere** ไม่มีคำขอ AI เพิ่มระหว่างจัดชุดส่ง

## ผลที่วัดได้

| วิธี | JUnit methods | Fixed failures รอบ 1 / 2 | Buggy failures | Line coverage | Branch coverage |
|---|---:|---:|---:|---:|---:|
| CMA-ES | 30 | 0 / 0 | 0 | 31/37 | 13/26 |
| FSCS-ART | 30 | 0 / 0 | 0 | 31/37 | 13/26 |
| Sonnet 5 | 21 | 0 / 0 | 1 | 36/37 | 22/26 |
| Gemini 3.5 Flash Lite | 18 | 0 / 0 | 1 | 37/37 | 23/26 |

AI ทั้งสองพบ fault เดียวกันเรื่องนับบรรทัดเมื่ออ่าน carriage return
Algorithms ไม่พบในขอบเขต inputs ของชุดนี้
99 methods รวมจากสี่ suites **ไม่ใช่ 99 บั๊ก**; coverage เป็นของ target class `ExtendedBufferedReader`
รายงานระบุข้อจำกัดของ fixed-assisted oracle/context, input domains และเวลาที่วัดจากคนละขั้นตอน
ขอบเขตหนึ่งบั๊กนี้ไม่แสดงว่าทำครบทุก Java project ในเอกสารวิชา

## เริ่ม demo

ใช้ Linux/WSL, Java 11 และ Defects4J 3.0.1 ตาม [สภาพแวดล้อม](output/round2-single-bug-20261004/Experiment/environment.md)
จาก root ของชุดส่ง:

```bash
python3 Presentation/replay-demo.py \
  --d4j /path/to/defects4j/framework/bin/defects4j \
  --worktrees /path/to/worktrees \
  --output /fresh/path/csv-demo
```

Output ต้องเป็น path ใหม่ คำสั่งตรวจ hashes และใช้ CPU หนึ่ง slot
ซ้อม archives เดิมครบทั้งสี่วิธีแล้ว ผลตรง canonical records;
[receipt การซ้อม](output/round2-single-bug-20261004/Presentation/rehearsal/receipt.json)
เป็น host replay ไม่เพิ่มจำนวนบั๊กหรือ generation repeats

## สมาชิก

| ชื่อ-นามสกุล | รหัสนักศึกษา |
|---|---|
| นายธนินธร อันทรบุตร | 673380043-6 |
| นายศุภกร กรมรินทร์ | 673380061-4 |
| นายณัชพล เพ็งพล | 673380267-4 |
| นายณัฐกรณ์ อินธิสาร | 673380268-2 |

[บันทึกชุดส่งและข้อความให้ทีม](docs/api854/AOM_SINGLE_BUG_SUBMISSION_TH.md)
ผลเก่าและงานพัฒนาหลายบั๊กเก็บไว้ครบ ดู [README เดิม](README_HISTORY.md)
และ Git history; ไม่รวมยอดผลเก่าเข้าตารางชุดส่งนี้
