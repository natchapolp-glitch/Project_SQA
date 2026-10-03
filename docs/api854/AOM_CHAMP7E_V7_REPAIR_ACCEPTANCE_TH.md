# ออมรับ Champ 7e09a5fe และ preparation ร่าง v7

รวม `champ 7e09a5feb32772a6d4c2921c804decabba0c1ab1` ซึ่งรับ `beam 3ae6f2fb`
ต่อจากออม `b11b379a` ตรวจหลักฐานรอบแก้และสร้าง shared development inputs ใหม่ครบ 20 bugs
โดยคง frozen core, ผลรอบแรก, preparation/protocol v1–v6 และ source/build evidence 284 bugs เดิม
ไม่มี KKU request, quota ledger/live queue mutation, protocol freeze หรือ pilot dispatch

## ผลตรวจรับไฟล์ของออม

- ตรวจ 1,349 checksum entries: packet รอบแรก 1,040, รอบแก้ 301 และ combined handoff 8
- ตรวจ retained execution runtime ของสอง packets ตามรุ่นเดิม: 40 และ 41 files
- รอบแก้ Chart/Cli/Mockito ทั้งสอง algorithms รวม 6 suites complete / local-valid
- Combined handoff มี 15 bugs / 30 local-valid suites: 24 เดิม + 6 รอบแก้
- ตรวจ Java ตรง suite archive, protocol/suite/result/review hashes และ evidence path mappings
- ทุก stage มี executed=30, skipped=0, target_checks=30; fixed สองรอบและ coverage ผ่าน
- ตรวจ retained observations เทียบ local oracle และ coverage XML อีกครั้งครบ 900 sampled cases
- Chart supplemental attempt ที่ failed คงอยู่ และ attempt ใหม่ผูกกับ unchanged suite/measurement
- CLI framework dependency repair มี before/after XML hashes; ไม่แก้ production classes หรือ generated suites
- Original results ยังคง usable=false; local semantic verdict ไม่แทน final joint approval

[Repair audit ของออม](../../output/api854-20261003/aom-champ7e-repair-audit-v1.json)
นี่เป็นการตรวจ received files/observations/XML ไม่ใช่การ rerun Defects4J บนเครื่องออม
900 cases เป็น cases ภายใน 30 development suites ไม่ใช่ 900 บั๊ก หรือ primary completion
ผลรอบแรก 26 complete / 2 environment_failed / 2 fixed_failed และ Chart local-invalid สอง suites เก็บเป็นประวัติครบ
หลักฐานห้าบั๊กก่อนหน้านั้นคง condition/source version เดิม ไม่ relabel เป็นผล v7

## Shared preparation รุ่นใหม่

- Contract: `aom-beam-prepare-v7-development`; fixture policy คง `beam-explicit-fixtures-v5-proposal`
- ครบ pilot 20 bugs จาก common discovery v3: 691 declarations = selected 377 + unsupported 314
- ตรวจ partition ทุก bug กับ capability export ของบีม และ checksums/source lineage ครบ
- ผูก protocol/runner/index/policy และ runtime ปัจจุบัน 41 files ใหม่
- ใช้ one-host-per-owner runner plan เดิม: champ-pc1 / beam-pc1 / aom-pc1, CPU หนึ่ง slot ต่อ host
- CPU/API consumer tests อ่าน prompt/recipe bytes เดียวกันครบ 20 bugs แบบ isolated file transport
- Development contract ยังถูก API FrozenSettings ปฏิเสธ แม้เปลี่ยน approval flag เป็น frozen
- Enabled stages ว่าง, reserve null, reviewed_by ทุกคน false, generation_ready=false

Prompt ทั้ง 20 ไฟล์มี SHA-256 ตรง v6 เพราะ context/recipe inputs ไม่เปลี่ยน แต่ source/condition pins ต้องเป็นรุ่นใหม่
ไม่ใช้การที่ prompt เหมือนกันรับรองว่า runtime หรือ experiments รุ่นใหม่ถูกทดสอบแล้ว

ไฟล์ตรวจรับ:

- [Preparation v7 index](../../output/api854-20261003/prepare-v7-twenty-bug-development/index.json)
- [Protocol v7 proposal](../../output/api854-20261003/aom-continuation-v7-development/protocol.proposal.json)
- [Runner v7 binding](../../output/api854-20261003/aom-continuation-v7-development/runner-plan.json)
- [Gate A selected v7 checklist](../../output/api854-20261003/aom-gate-a-selected-v7-development-v1.json)
- [รายการค้าง พร้อม signatures ครบ 314 รายการ](../../output/api854-20261003/aom-v7-readiness-worklist-v1.json)

Input bindings/completeness 20 bugs ผ่าน; final declaration coverage, host/semantic/provider/team approvals ยังไม่ผ่าน
Gate A=false และ primary completion ของ cohort 854 bugs ยังคง 0

## ตารางสำหรับทีมทำต่อ

Owner เป็นเจ้าของบั๊กใน inventory; งาน support/oracle และการตรวจ shared recipe ต้องประสานบีมและตรวจร่วมทีม
การรองรับครบ declarations ในแถวใดไม่ใช่ joint semantic approval ของแถวนั้น

| Bug | Owner | ทั้งหมด | รองรับ | ยังไม่รองรับ | Prompt bytes |
|---|---|---:|---:|---:|---:|
| Chart-1 | champ | 56 | 8 | 48 | 215,713 |
| Cli-1 | aom | 16 | 16 | 0 | 116,811 |
| Closure-1 | aom | 14 | 3 | 11 | 171,229 |
| Closure-176 | beam | 50 | 28 | 22 | 216,498 |
| Codec-1 | aom | 18 | 12 | 6 | 146,631 |
| Collections-1 | aom | 22 | 12 | 10 | 180,002 |
| Compress-1 | champ | 19 | 8 | 11 | 121,893 |
| Csv-1 | aom | 7 | 5 | 2 | 105,347 |
| Gson-1 | champ | 5 | 5 | 0 | 107,324 |
| JacksonCore-1 | champ | 44 | 18 | 26 | 142,442 |
| JacksonDatabind-1 | aom | 37 | 14 | 23 | 150,627 |
| JacksonDatabind-112 | champ | 12 | 4 | 8 | 141,545 |
| JacksonXml-1 | beam | 47 | 14 | 33 | 140,497 |
| Jsoup-1 | beam | 12 | 10 | 2 | 107,259 |
| JxPath-1 | beam | 84 | 67 | 17 | 179,994 |
| JxPath-22 | beam | 44 | 36 | 8 | 156,957 |
| Lang-1 | aom | 47 | 45 | 2 | 188,818 |
| Math-1 | champ | 85 | 51 | 34 | 250,315 |
| Mockito-1 | beam | 15 | 6 | 9 | 125,592 |
| Time-1 | champ | 57 | 15 | 42 | 177,269 |
| รวม | | 691 | 377 | 314 | max 250,315 |

ตรวจ source ของ JacksonXml-1 แล้ว `FromXmlParser.Feature` เป็น enum ที่ไม่มี constants
ดู [fixed source บรรทัด 35](../../output/api854-20261003/prepare-v7-twenty-bug-development/JacksonXml-1/fixed-source/src/main/java/com/fasterxml/jackson/dataformat/xml/deser/FromXmlParser.java#L35)
กระทบ configure/disable/enable/isEnabled รวมสี่ signatures ซึ่งยังอยู่ใน 314 และ denominator 691
ต้องตกลงวิธีจัดการ empty non-null parameter domain แบบ prospective ร่วมทีม
ออมไม่สร้าง enum ปลอม ไม่เปลี่ยน inventory และยังไม่รับรอง null-rejection เป็น meaningful oracle เพื่อปิดยอด

## Reserve และ validation

Max prompt วัดจริงยังเป็น **250,315 UTF-8 bytes**; proposed output 4096 ทำให้ guard floor **254,411 + H**
H/framing, actual provider tokens และ final reserve ยัง unknown; bytes นี้ไม่ใช่ token usage จริง
เทียบ historical a01 snapshot 07:13 น. เท่านั้น: Sonnet remaining 199,961 ต่ำกว่า floor 54,450 ก่อน H
daily ceiling 200,000 ใน receipt เดิมก็ต่ำกว่า floor; Gemini remaining 349,979 มี headroom 95,568 ก่อน H
จึงยังใช้ snapshot/worksheet เก่ารับรอง quota ปัจจุบันหรือเปิดคำขอไม่ได้
การเตรียมหลายบัญชีต้องมี distinct bucket/limits metadata และยังไม่แก้ปัญหา per-request floor เกิน ceiling เดิม

Full offline API854 integration รัน **303 tests** ใน 154.973 วินาที: **ผ่าน 302 / ข้าม symlink 1**, exit code 0
ครอบคลุม v7 consumers/flag rejection, stale v6 runtime rejection, selected-pair tampering และ helpers/intake ที่รับมา
[Integration receipt](../../output/api854-20261003/aom-champ7e-integration-validation-v1.json)
Repair audit และ readiness partition inspector รันกับ actual public artifacts แยกจาก integration tests

สร้างซ้ำใช้ directories ใหม่ที่ยังไม่มี แล้วตรวจ Gate A ด้วยคู่ใหม่:

```powershell
python -m scripts.study.api854.build_prepare_v7_development --input output/api854-20261003/prepare-v3 --output output/api854-20261003/prepare-v7-next
python -m scripts.study.api854.compose_v7_development --preparation output/api854-20261003/prepare-v7-next --output output/api854-20261003/proposal-v7-next
python -m scripts.study.api854.gate_a --protocol output/api854-20261003/proposal-v7-next/protocol.proposal.json --runner output/api854-20261003/proposal-v7-next/runner-plan.json --output output/api854-20261003/gate-v7-next.json
```

## ส่งต่อทีม

บีม: ออมรับ repair/combined packet แล้ว ใช้ตารางและ worklist 314 รายการทำ support/oracle ต่อ
ตรวจ shared v7 recipe/prompt ทั้งสี่วิธี และเสนอคำตัดสินสี่ enum targets พร้อมคง source/signatures/outcomes เดิม

แชมป์: ออมรับ 7e09a5fe แล้ว ใช้ v7 selected pair และ max prompt 250,315 bytes คำนวณ reserve/limits ต่อ
received local reviews ไม่อนุมัติ final condition; ยังไม่เปิด KKU/pilot จน Gate A ร่วมทีมผ่าน

ออม: งาน integration/preparation ที่ไม่ต้องรอทำเสร็จใน checkpoint นี้แล้ว
เมื่อ support/recipe หรือ runtime เปลี่ยน ให้ตรวจรับและสร้าง condition/pins ใหม่ก่อน freeze
Report/demo/slides/ZIP เดิมยังเป็น source/build หรือ historical snapshots ไม่ได้กลายเป็น primary results จากงานนี้
