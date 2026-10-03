# ออมรับ b6051367 และแก้ Gate A input checker

รวม `champ b60513672122d7533999e5fbbd369d4ea2eaf936` ต่อจากงานออม `978f68ab`
รับตาราง pilot 20 bugs, missing evidence 15 bugs ใน checkpoint นั้น และ conditional reserve worksheet
พร้อมตรวจไฟล์กับ Git commit จริงและ hashes ที่ worksheet อ้าง
ไม่มีคำขอ KKU เพิ่ม ไม่มี quota ledger/live queue mutation และไม่มีการ freeze/เปิด pilot

## ส่วน checker ที่แก้แล้ว

`scripts/study/api854/gate_a.py` ต้องรับ **--protocol และ --runner** ชัดเจน
ไม่เลือก protocol.json หรือ runner-plan.v1.json ให้เอง หากไม่ระบุคู่จะหยุดก่อนตรวจ
ตรวจ protocol runner hash, preparation index path/hash และ runtime source inventory ของรุ่นที่เลือก

เลือก preparation policy จาก contract ของ protocol แล้วส่ง fixture-recipes.json เข้า validator สำหรับ v4/v5
ตรวจ index/metadata/identity, fixed source bytes/revision, target/recipe/prompt bindings,
selected/excluded declaration partition และ lineage กลับ common discovery v3
วัด prompt bytes จากไฟล์จริงและเทียบกับ index ไม่ใช้ค่า summary ที่ไม่ตรง bytes เพื่อคำนวณ reserve
เพิ่ม checklist ที่แยก completeness 20 bugs กับ requirement 691 common declarations

Runtime worker modules ที่ protocol pin ไม่เปลี่ยน การแก้ครั้งนี้เป็นตัวอ่านหลักฐาน/checklist
ผล checker ใหม่ผูก SHA256 ของ checker เองไว้เพื่อแยกจาก receipts รุ่นเก่า
ไม่แก้ immutable preparations/proposals/frozen core หรือ receipts เก่าย้อนหลัง

## ผลตรวจคู่ v5 ที่มีอยู่จริง

| รายการ | ผล |
|---|---|
| Protocol / runner binding | ผ่านคู่ที่เลือกจริง |
| Preparation index / runtime source / shared policy | ผ่าน |
| Fixture recipe bindings | ผ่าน 5 bugs |
| Routing | ครบ 10,248 stage keys |
| Completeness ของ pilot | ยังมี 5 จาก 20 bugs |
| Common declarations | เลือก 124 จาก requirement 691 |
| Host / semantic / provider / team approval | pending |
| Gate A / generation authorization | false |

ผลและ evidence paths/hashes: [selected v5 checklist](../../output/api854-20261003/aom-gate-a-selected-v5-b605-v2.json)

Checker รุ่นนี้ตรวจ input bindings และรายงานข้อมูลที่ยังขาด
ช่อง host/semantic/provider/three-owner acceptance ยังไม่มีตัวรับรอง condition-bound evidence ครบและจึงคง pending
การเปลี่ยน flags ใน proposal หรือการมี local review ไม่ทำให้ช่องเหล่านี้ผ่าน
ก่อน final freeze ยังต้องตรวจรับหลักฐานจริงร่วมทีม และเพิ่ม validation ของหลักฐานรุ่นสุดท้าย
Policy contract ที่ยังไม่ implement จะถูกปฏิเสธ ต้องเพิ่ม contract/validator พร้อม tests เมื่อสร้าง final 20-bug condition

## Worksheet ที่ออมตรวจแล้ว

ตรวจ 20 unique identities/owners ตรง core pilot, 691 discovery declarations,
shared v5 5 bugs / 124 targets และ runner assignment ของแต่ละ owner
ตรวจ review-input hashes 10 รายการ และ arithmetic ของ budget rows 10 รายการ
floor = prompt UTF-8 bytes + proposed output 4096; headroom ใช้ historical remaining ลบ floor
H, context/output limits และ expiry ยังไม่ทราบ จึงไม่ใช้ตารางนี้แทน final reserve หรืออนุญาต batch

[Worksheet acceptance receipt](../../output/api854-20261003/aom-b605-worksheet-acceptance-v2.json)
ไม่เอาค่า 07:13 น. ไปเรียกเป็นโควตาสด ไม่ import ledger และไม่ส่ง prompts ไป KKU
ตัวเลข missing 15 ของตารางเป็น snapshot ของ b6051367
งานบน champ 8e350c68 ที่มี packet บีมรอบแรกยังต้องรับแยกพร้อม failures/runtime/policy ใหม่
จึงยังไม่เปลี่ยน snapshot นี้เป็น final shared acceptance

## ตรวจซ้ำ

รันจาก repository root และเลือก output filename ใหม่ที่ยังไม่มี:

```powershell
python -m scripts.study.api854.gate_a `
  --protocol experiments/configs/api854-20261003/champ-composed-v5.proposal.json `
  --runner output/api854-20261003/aom-continuation-v5/runner-plan.json `
  --output output/api854-20261003/gate-a-selected-v5-next.json
```

การเขียน checklist ไม่ใช่คำสั่ง freeze/seed หรือ dispatch
คำสั่งใน handoff รุ่นเก่าที่ระบุเพียง --output ต้องเติมคู่ --protocol/--runner ก่อนใช้ checker รุ่นนี้

Local validation ผ่าน **37 tests / 0 skips** รวม 11 selected-pair tests และ guards ของ runner/shared v5/API worker
ครอบคลุม runner เก่าที่ยัง route ครบ, contract ไม่ตรง, recipe เปลี่ยนแม้ refresh checksum manifest,
duplicate identity, stale runtime, index ไม่ผูก hash, path escape, prompt maximum ที่ไม่ตรง bytes
และ discovery เปลี่ยนแม้แก้ metadata/checksums โดยไม่ตรง index รุ่นเดิม
ใช้สำเนาแยกและ mock provider ไม่รัน Defects4J experiments หรือ KKU generation

[Validation receipt](../../output/api854-20261003/aom-gate-a-b605-validation-v2.json)

## ส่งต่อทีม

> ออมรับ b6051367 แล้ว แก้ Gate A checker ให้ระบุ protocol/runner จริง และ validate fixture recipe ของ contract ที่เลือก
> คู่ shared v5 ผ่าน input bindings แต่ยังมี 5/20 bugs และ 124/691 declarations จึง Gate A ยัง false
> Tests ผ่าน 37 ตรวจ worksheet/hash ครบ ไม่เรียก KKU หรือเปลี่ยน live queue
> รับ evidence/runtime/recipes รุ่นถัดไปแล้วต้องสร้าง final shared condition ใหม่ก่อน freeze

ให้ดึง branch aom ล่าสุดแล้วอ่านเอกสารนี้
ZIP source/build เดิมยังเป็น snapshot ของ 31da18ce และไม่รวมการแก้ checker/worksheet acceptance รอบนี้
