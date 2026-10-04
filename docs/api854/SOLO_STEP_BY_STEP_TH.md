# ทำต่อทีละขั้น — ผู้ใช้ทำทุกหน้าที่เอง

ทำบน branch `aom` ไม่รอออม/บีม/แชมป์ และไม่สร้างคำรับแทนเพื่อน
เป้าหมายเดิมคือ 854 active bugs / 17 projects × 4 วิธี = 3,416 planned jobs
เก็บผลไม่ผ่านและข้อจำกัดตามจริง ไม่บังคับให้ทุก job valid หรือพบ fault

## ขั้น 1: เครื่อง รายการบั๊ก runner และ offline pilot

- ตรวจ installed inventory เทียบแผน: 854 active bugs / 17 projects ตรงกัน
- Java 11.0.32.1; Defects4J 3.0.1 commit `6d54320e0db5a357f9ab38a8e4d2e5aead7e1c09`
- เพิ่ม `scripts/study/solo_batch.py`: เลือก case ได้, checkpoint/hash checks,
  เก็บ invalid/unsupported/infra failures และทำตาราง 3,416 jobs พร้อม PENDING
- ทดสอบ runner 10 ข้อผ่าน รวมหลักฐานถูกแก้ไข, เปลี่ยน intent/condition,
  interrupted stages และ target ที่ไม่ได้เรียกจริง
- แยก diagnostic generic/null-fixture pilot ที่ coverage Csv-1 เพียง 1/37 บรรทัด
  ไว้ใน `output/round2-full-study-20261004`; ไม่รวมในชุดปัจจุบัน
- ใช้ receiver/fixtures **v12 เดิม** ใน `output/round2-solo-pilot-20261004`
  ยืนยัน executed/skipped/target_checks ของ fixed-1, fixed-2, buggy และ coverage
  ไม่เพิ่มงานพัฒนา recipes ใหม่เพื่อขวางการทดลอง
- คำสั่ง resume Csv-1 ข้าม outcomes ที่เสร็จแล้วจริง ไม่เกิด generation ซ้ำ

ผลล่าสุดดูจาก
[summary.json](../../output/round2-solo-pilot-20261004/Report/data/summary.json)
และ [ตาราง jobs](../../output/round2-solo-pilot-20261004/Report/data/final_comparison.csv)
เท่านั้น ไม่ใช้ `Experiment/jobs.csv` ซึ่งเป็น planned snapshot PENDING ตอนเริ่ม
Offline pilot รันแล้วทั้ง Csv-1/Lang-1/Math-1 ด้วยสอง algorithms รวม **6 outcomes**
แต่ละ suite มี 30 methods / executed=30 / skipped=0 / target_checks=30 ทุก stage
fixed สองรอบผ่านทั้งหมด; buggy ไม่พบ fault ทั้งหก suites ใน input domain นี้
ผลการตรวจ resume ยืนยัน hashes ของ generation/measurement/outcome ไม่เปลี่ยน
ชุดนี้ยังไม่เรียก KKU และยังไม่ใช่ผลครบสี่วิธี

| Case | CMA-ES lines | FSCS-ART lines | CMA-ES conditions | FSCS-ART conditions |
|---|---:|---:|---:|---:|
| Csv-1 | 31/37 | 31/37 | 13/26 | 13/26 |
| Lang-1 | 137/380 | 117/380 | 63/350 | 48/350 |
| Math-1 | 226/429 | 198/429 | 70/200 | 63/200 |

Generation ใช้ประมาณ 7.9–8.8 วินาทีต่อ suite; evaluation ประมาณ 29.4–36.1 วินาที
ไม่รวม checkout/compile/context ที่ใช้ร่วมกัน และยังไม่รวม AI จึงไม่ใช้รับรองเวลา full run
Planned 3,416 jobs: DONE 6 / PENDING 3,410; AI ยัง pending 1,708 jobs
Receipt การตรวจอยู่ใน
`Experiment/diagnostics/offline-acceptance/receipt.json` ของชุดปัจจุบัน

คำสั่ง (Ubuntu WSL user `team`, จากราก repo):

```bash
python3 -B scripts/study/solo_batch.py run \
  --output output/round2-solo-pilot-20261004 --cases Csv-1 Lang-1 Math-1
python3 -B scripts/study/solo_batch.py report \
  --output output/round2-solo-pilot-20261004
```

ใช้ CPU lock เดิมหนึ่ง slot. Resume ในเครื่องเดิมและรักษา checkout/compiled artifacts
ไว้; stage ที่ interrupted/uncertain ไม่ถูกส่งหรือรันใหม่อัตโนมัติ ต้องตรวจหลักฐานก่อน recovery
การเปลี่ยน implementation/condition ต้องแยก output ไม่เขียนทับหลักฐานเก่า

## ขั้น 2: AI pilot ผ่าน KKU

ทำ workflow P01/P02/P03/P04 และตรึง templates/settings ก่อนส่งจริง
ใช้ buggy-only context ที่ขั้น 1 เตรียมไว้: source รวมไม่เกิน 12,000 characters
กับ buggy signatures ไม่เกิน 6,000 characters ไม่ส่ง fixed source/patch/trigger tests
ใช้ Sonnet 5 และ Gemini 3.5 Flash Lite ตามคำสั่งเดิมเท่านั้น

ตรวจบัญชีที่ใช้จริง/model IDs/current quota/limits ก่อนคำขอใหม่และแจ้งผู้ใช้ก่อนเริ่ม
P03 ซ่อมได้หนึ่งคำขอตาม compile/fixed feedback ที่จำกัด; ไม่ใช้ buggy failures ไปซ่อม tests
P04 เก็บทั้งผลเพิ่มและ rejection; ใช้ base suite ที่ valid ตามกติกาเดียวกันทั้งสองโมเดล
เก็บ prompts, raw responses, requested/returned settings, usage, Java ทุกขั้นและผลไม่ผ่าน
อย่านับ context ที่เตรียมไว้หรือคำขอ “OK” เป็น AI test generation ที่เสร็จ

ทำ AI ของ Csv-1/Lang-1/Math-1 แล้วตรวจทั้งสี่วิธีร่วมในตาราง
ผล invalid เป็นผลจริง ไม่เพิ่ม retries เพื่อเอาผลดี และไม่เปลี่ยนโมเดลเอง
ขั้นนี้ยังไม่เสร็จใน offline pilot

## ขั้น 3: ขยายและวัดเวลา/โควตา

เมื่อ pipeline ทั้งสี่วิธีทำงานจริง เริ่ม 10–20 bugs แล้วใช้เวลาจริง/tokens จริง
วางขนาด batch และการเดินครบรายการ; ยังไม่รับรองวันเสร็จจากเวลา algorithms อย่างเดียว
Quota หมดให้เก็บ jobs ที่ยังไม่เริ่ม/QUOTA_PAUSED แยกจาก terminal failures
รายงาน planned/attempted/provider_requested/evaluated/valid/pending แยกกัน
ไม่รวม diagnostic/ผลเดิมหรือ host replays เป็น independent generation repeats

## ขั้น 4: ปิดทุกหมวดส่งงาน

สร้างรายงาน PDF/กราฟ/PowerPoint/demo จากตารางจริง พร้อมข้อจำกัดและ denominators
ซ้อม demo, ตรวจ ZIP/CRC/checksums และ credentials แล้ว push เฉพาะ branch `aom`
ผู้ใช้ส่ง Classroom และนำเสนอเอง
หากยัง pending ต้องรายงานจำนวนค้าง ไม่ใช้คำว่าเสร็จครบ 854

อ้างอิงรายละเอียดโครงไฟล์ใน
[แผนเต็ม](AOM_FRIEND_LAYOUT_FULL_SUBMISSION_PLAN_TH.md)
และ [README ชุดปัจจุบัน](../../output/round2-solo-pilot-20261004/README.md)
