# แชมป์รับ Aom b11b379a และวัด shared development v6 ใหม่

รวม `aom b11b379a669e423e597ba66be344d23eed0939ea` ต่อจาก `champ e5d4a66b`
ซึ่งมี `7e09a5fe` และ Beam `3ae6f2fb` รอบแก้แล้ว
ใช้บันทึกนี้ส่งต่อสถานะรวมล่าสุดได้ ไม่ต้องส่งข้อความแต่ละ checkpoint แยกกัน
งานรับ keys พักไว้ตามคำขอผู้ใช้ ไม่มี KKU request, live queue mutation หรือการเปิด Gate A ในรอบนี้

## การรวมและ build ใหม่

แก้ conflicts ใน README, HANDOFF และ START_HERE ให้เก็บ checkpoint เดิมพร้อมชี้งานล่าสุด
รวมการรองรับ `aom-beam-prepare-v6-development` กับ recipe/runtime guard ของแชมป์
ตัดการเรียก recipe validator ซ้ำที่เกิดจาก merge และเปลี่ยน isolated selected-pair fixture ให้ build v6
ยังคง regression ที่ปฏิเสธการ repin runtime โดยใช้ embedded recipes เก่า
Windows path boundary และ unauthorized POST fixes จาก e5d4a66b ยังคงอยู่

สร้าง output ใหม่จาก immutable discovery v3 โดยใช้ runtime หลังรวม:

```powershell
python -m scripts.study.api854.build_prepare_v6_development `
  --input output/api854-20261003/prepare-v3 `
  --output output/api854-20261003/champ-prepare-v6-b11-development-v1
```

Directory นี้สร้างไว้แล้ว หากสร้างซ้ำต้องเลือก output ใหม่ที่ยังไม่มี
ไม่แก้ preparation/proposals/frozen core ของ checkpoint เดิมย้อนหลัง

| รายการ | ผลหลังรวม |
|---|---|
| Development preparation | 20 bugs |
| Selected / excluded common declarations | 377 / 314; requirement 691 คงเดิม |
| Checksums ที่ตรวจจาก fresh preparation | 243 entries |
| CPU/API อ่าน prompt และ recipe ชุดเดียวกัน | ผ่านครบ 20 bugs |
| Max prompt | 250,315 UTF-8 bytes — Math-1 |
| Prompt files เปลี่ยนจาก Aom candidate หรือไม่ | ไม่เปลี่ยนทั้ง 20 ไฟล์; วัด bytes/hash ใหม่แล้ว |
| Runtime pins ใน Aom proposal ที่รับมา | blocked เมื่อเทียบกับโค้ดหลังรวม |
| Runtime/runner/index/recipe bindings ของ fresh proposal | pass |
| ครบ 691 declarations / host / semantic / provider / team | ยังไม่ผ่าน |
| Gate A / generation authorization | false |

แม้ prompt bytes เท่าเดิม runtime pins และ preparation index ต้องผูกกับรุ่นใหม่
Fresh proposal ปรับ processing fixture policy ที่สืบทอดมาจาก v3 ให้ตรง fixture v5 ของ development contract
ยังเป็นข้อเสนอเพื่อ review ไม่ใช่การตรวจรับ semantic หรือ final shared condition
Enabled stages ว่าง, reserve null และ reviewed_by ทั้งสามคน false
FrozenSettings ปฏิเสธ proposal ก่อน claim; หากเปลี่ยน approval flag อย่างเดียวก็ยังปฏิเสธ v6-development contract

- [Fresh preparation index](../../output/api854-20261003/champ-prepare-v6-b11-development-v1/index.json)
- [Fresh development protocol](../../output/api854-provider-preflight-20261003/champ-v6-b11-development.proposal.json)
- [Runner plan ที่เลือก](../../output/api854-20261003/aom-continuation-v6-development/runner-plan.json)
- [Selected-pair checklist](../../output/api854-provider-preflight-20261003/champ-aomb11-v6-selected-current-v1.json)
- [Integration audit](../../output/api854-provider-preflight-20261003/champ-aomb11-v6-integration-audit-v1.json)

## Reserve worksheet ใหม่

วัด bytes/hash ของทุก prompt แล้วคำนวณ [conditional worksheet 40 แถว](../../output/api854-provider-preflight-20261003/champ-aomb11-v6-reserve-worksheet-v1.json)
สำหรับ 20 bugs × 2 models โดยใช้ proposed output cap 4096:

```text
Largest prompt guard floor = 250,315 + H
Largest request guard floor = 254,411 + H
```

H คือ provider framing bound ที่ยังไม่ทราบ ตัวเลขนี้เป็น floor ของ conservative byte guard ปัจจุบัน
ไม่ใช่ actual prompt token count และไม่ใช้เป็นการจอง batch หรือประมาณ total usage
ตารางด้านล่างอ้างอิง a01 snapshot 3 ต.ค. เวลาไทย 07:13:41–43 เท่านั้น ไม่ใช่โควตาปัจจุบัน

| Model | Historical remaining | Historical daily ceiling | Remaining ลบ largest floor ก่อน H |
|---|---:|---:|---:|
| claude-sonnet-5 | 199,961 | 200,000 | -54,450 |
| gemini-3.5-flash-lite | 349,979 | 350,000 | 95,568 |

Largest floor ของ Sonnet ยังเกิน historical daily ceiling 54,411 ก่อน H
การรอ reset หรือเพิ่มบัญชีที่มี ceiling เท่ากันไม่แก้ guard นี้
ต้องได้ limits และการนับ tokens/framing ที่มีหลักฐานเพื่อกำหนด reserve ที่รับรองได้
หรือทีมต้อง review prospective shared context policy ใหม่ให้เหมือนกันทั้งสี่ approaches แล้ว build/measure ใหม่
รอบนี้ไม่ลด context, แยก prompt, เดา token count หรือเติมข้อมูล quota ที่ไม่ทราบ
Settings/limits/framing/bucket/window/reset/expiry และ **final reserve ยัง pending**
ชุด 20-bug development นี้ยังไม่ใช่ final accepted prompts; เมื่อชุด final เปลี่ยนต้องคำนวณใหม่อีกครั้ง

## หลักฐานบีมและ validation

งานรวมนี้มี repair packet และ combined handoff จาก 7e09a5fe แล้ว
ใช้ [บันทึกรอบแก้](CHAMP_BEAM_REPAIR_INTAKE_TH.md) และ hashes ที่ผูกใน fresh proposal ตรวจร่วมได้
Local suite reviews คง source/protocol versions ของแต่ละ packet ไม่ relabel เป็นผลของ fresh v6 condition
ไม่ได้ rerun Defects4J experiments บนเครื่องแชมป์ และ primary_completed ยัง 0

ผล Aom 287 pass / skip 1 ตรวจ source pins กับ commit b11b379a แล้ว แยกจากผลบนเครื่องแชมป์
ผล full API854/legacy Java รอบนี้และ source/log hashes อยู่ใน
[validation receipt](../../output/api854-provider-preflight-20261003/champ-aomb11-v6-validation-v1.json)
รันรวม **333 tests: ผ่าน 332 / skip 1 / failures 0 / errors 0**
API854 รัน 303 tests (302 pass / 1 skip); legacy Java probes ผ่าน 30 tests
รายการ skip เป็น source-link fixture ที่ Windows ไม่มีสิทธิ์สร้าง symlink

## ข้อความส่งให้ทีมพร้อม champ commit ล่าสุด

ออม: แชมป์รวม b11b379a ต่อจาก e5d4a66b แล้ว รวม 7e09a5fe/บีมรอบแก้ด้วยครับ
build และตรวจ CPU/API bindings ครบ 20 inputs ใหม่; max ยัง 250,315 bytes (Math-1)
worksheet ใหม่ครบ 40 แถว floor สูงสุด 254,411 + H; Sonnet เกิน historical 200,000 ภายใต้ guard เดิม
ยัง pending limits/framing/bucket/reset/expiry/final reserve และยังไม่เปิด Gate A ครับ

บีม: ดึง champ รุ่นที่ส่งพร้อมบันทึกนี้ได้ครับ มี shared v6 draft ครบ 20 bugs หลังรวมงานรอบแก้แล้ว
ตรวจ final shared recipe/policy และทำ support/semantic/fixture/oracle review อีก 314 declarations ต่อ
ส่งข้อสรุปสี่ enum targets กับหลักฐาน target execution ให้ทีม review โดยคง requirement 691 ไว้
ยังไม่เรียก KKU หรือเปิด pilotครับ
