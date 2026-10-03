# ออมรับ Champ 8e350c68 และสร้าง shared preparation ร่าง v6

Checkpoint นี้เป็นประวัติ: [งานล่าสุดรับรอบแก้ 7e09a5fe และ preparation ร่าง v7](AOM_CHAMP7E_V7_REPAIR_ACCEPTANCE_TH.md)
ข้อที่ระบุว่า packet รอบแก้ยังไม่รับด้านล่างเป็นสถานะ ณ checkpoint v6; source pins v6 ไม่ใช่ current runtime

รวม `champ 8e350c6853eed252c4c0642319b043da50d64269` ต่อจากออม `4a0e699c`
รับ packet บีมรอบแรกพร้อม failures แล้วสร้าง **20-bug development candidate** จาก fixed inputs v3
และ runtime หลังรวม ไม่ใช่ final shared acceptance หรือ primary results
ไม่มี KKU request, quota ledger/live queue mutation, freeze หรือ pilot dispatch

## หลักฐานรอบแรกที่ตรวจจากไฟล์จริง

ตรวจ 1,040 checksum entries, retained execution runtime 40 files และ suite archive/Java/result bindings
ครบ 15 bugs / 30 algorithm development suites

| มิติ | Suites | ขอบเขต |
|---|---:|---|
| Measurement complete | 26 | fixed สองรอบ / buggy / coverage; ไม่ใช่ semantic approval ทุก suite |
| Local review valid | 24 | sampled suites ของ 12 bugs; original usable=false ยังคงเดิม |
| Local review invalid | 2 | Chart-1 ทั้งสอง approaches |
| Environment failed | 2 | Cli-1 ทั้งสอง approaches |
| Fixed failed | 2 | Mockito-1 ทั้งสอง approaches |

Measurement กับ verdict เป็นคนละมิติ ห้ามบวกเป็นจำนวน suites ใหม่
ออมตรวจ received bytes ไม่ได้ rerun Defects4J experiments ของบีม
ตรวจ capability artifacts 20 bugs เทียบ common discovery v3: 691 declarations แบ่งเป็น selected 377 / excluded 314
fixed-only exclusions 3 รายการอยู่นอก denominator 691 ตาม discovery เดิม

[Received evidence audit](../../output/api854-20261003/aom-champ8e-beam68-audit-v1.json)
บันทึก source differences ระหว่าง execution packet กับ runtime ปัจจุบันไว้ ไม่อ้างว่า packet เดิมทดสอบ runtime ใหม่แล้ว

## Shared preparation ร่างใหม่

- Contract: `aom-beam-prepare-v6-development`
- Fixture policy: `beam-explicit-fixtures-v5-proposal`
- Scope: pilot 20 bugs; selected 377 declarations และรักษา unsupported 314 ไว้ครบ
- Fixed production/build/receiver bytes มาจาก immutable v3 ที่ตรวจ checksums ทุกไฟล์แล้ว
- ทุก bug มี source/targets/recipe/prompt bindings; CPU และ API consumers อ่าน bytes ชุดเดียวกัน
- Runtime inventory ของ proposal ใหม่ 41 files รวม target_coverage.py; runner คง one-host plan
- Enabled stages ว่าง, reserve null, reviewed_by ทุกคน false และ generation_ready=false

เพิ่ม shared validator สำหรับ development contract นี้และการอ่าน lineage/recipes ของ API/CPU consumer
API FrozenSettings ยังปฏิเสธ contract v6-development แม้เปลี่ยน approval flag เป็น frozen
จึงต้องมี final contract/condition ที่ทีมตรวจรับก่อนเปิด primary; candidate นี้ไม่ใช่ทางลัดให้ flip flags
แก้ recipe_document/validate_recipe ให้รับ fixture policy v5 และผูก recipe sources กับ protocol ใน Gate A checker
ไม่แก้ immutable v1–v5 preparation, proposals, frozen core หรือ experiment receipts เก่าย้อนหลัง

จุดตรวจรับ:

- [20-bug preparation index](../../output/api854-20261003/prepare-v6-twenty-bug-development/index.json)
- [Protocol proposal](../../output/api854-20261003/aom-continuation-v6-development/protocol.proposal.json)
- [Runner plan](../../output/api854-20261003/aom-continuation-v6-development/runner-plan.json)
- [Selected Gate A checklist](../../output/api854-20261003/aom-gate-a-selected-v6-development-v1.json)

Input bindings และ completeness 20 bugs ผ่าน แต่ declaration completeness 691, host/semantic/provider/team approval
ยังไม่ผ่าน Gate A; primary completion ยังคง 0
การเตรียม inputs 20 bugs ไม่ได้แปลว่าสร้าง/รันเทสครบ 20 bugs × 4 approaches แล้ว

## Prompt และ reserve ที่ต้องส่งแชมป์

วัดจาก actual prompt files: **max 250,315 UTF-8 bytes**
ค่าของ v5 ห้าบั๊กเดิม 177,698 bytes และ worksheet เดิมใช้กับ candidate นี้ไม่ได้
ภายใต้ conservative byte guard และ proposed output 4096:

```
prompt reservation floor >= 250315 + H
request reservation floor >= 254411 + H
```

นี่เป็น numerical byte floor ของ guard ไม่ใช่ actual provider token count; H ยัง unknown
เทียบ historical a01 snapshot 07:13 น. เท่านั้น: Sonnet remaining 199,961 ต่ำกว่า floor อยู่ 54,450 ก่อน H
และ daily quota 200,000 ใน receipt เดิมยังต่ำกว่า floor; Gemini remaining 349,979 มี headroom 95,568 ก่อน H
ไม่ใช้ snapshot นี้ยืนยันโควตาปัจจุบันหรือความพร้อมส่งคำขอ
การรอ reset/เพิ่มบัญชีที่มี ceiling เดียวกันไม่แก้ floor ที่ใหญ่กว่า ceiling ของบัญชี

แชมป์ต้องตรวจ actual context/output limits, วิธีนับ tokens/framing, bucket/reset/expiry
และวิธีปิด reserve ที่รับรองได้ หรือทีมต้องกำหนด prospective shared context policy ใหม่พร้อมตรวจทั้งสี่วิธี
ออมยังไม่ลด context, แยก prompt หรือเดา token count เพื่อให้ guard ผ่าน

## Validation และการสร้างซ้ำ

[Integration validation receipt](../../output/api854-20261003/aom-champ8e-integration-validation-v1.json)
Full offline API854 integration รัน 288 tests ใน 159.459 วินาที: ผ่าน 287 / ข้าม symlink 1, exit code 0
แยกจาก received Defects4J experiments; ไม่ใช่ผลทดลอง primary หรือการ rerun ของบีม
Selected-pair/v6 consumer tests ตรวจครบ 20 prompts/recipes และปฏิเสธการเปิด development contract ด้วย approval flag

สร้างซ้ำต้องใช้ output directory ใหม่ที่ยังไม่มี:

```powershell
python -m scripts.study.api854.build_prepare_v6_development `
  --input output/api854-20261003/prepare-v3 `
  --output output/api854-20261003/prepare-v6-development-next
```

ต้องสร้าง protocol/source/runner bindings ใหม่จาก runtime ณ เวลาที่ build ไม่ใช้ proposal snapshot เก่ากับ runtime ใหม่
Build script ไม่เรียก KKU หรือ Defects4J และไม่แก้ live queue

## งานรับต่อและข้อความทีม

ขณะทำงานพบ `champ 7e09a5fe` ซึ่งรับ Beam `3ae6f2fb` และ packet รอบแก้ 6 suites แล้ว
จากเอกสารรอบนั้น combined handoff มี 15 bugs / 30 local-valid suites แต่ capability ยัง 377/691
ออมยังไม่ audit/merge packet รอบแก้นี้ใน checkpoint 8e350c68; ต้องรับเป็นขั้นถัดไปพร้อม runtime/hash ใหม่
ไม่เรียก 6 failures ของรอบแรกว่าเป็นสถานะล่าสุดของบีม และไม่ถือข้อความในรอบแก้เป็น evidence ที่ออมตรวจแล้ว

บีม: ตรวจ shared candidate source/recipe/prompt เดียวกันทั้งสี่วิธี เติม support/oracle/review 314 declarations
และส่ง packet รอบแก้พร้อม lineage โดยคง failures เดิม

แชมป์: ใช้ max prompt 250,315 bytes ของ candidate นี้ตรวจ reserve/limits ต่อ
floor 254,411 + H เกิน historical Sonnet quota ภายใต้ guard เดิม ยังไม่ใช่ actual token usage
เมื่อรับ runtime/recipes รุ่นถัดไปต้อง build/measure ใหม่อีกครั้งก่อน final Gate A

ให้ทีมดึง branch aom ล่าสุดแล้วเริ่มอ่านเอกสารนี้
ZIP/checkpoints เก่าคงเป็น historical snapshots ไม่ได้รวมงานรอบนี้
