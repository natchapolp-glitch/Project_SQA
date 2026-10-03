# ออมรวม Champ intake และ recipes เป็น shared development v9

รับ `codex/champ-v7-intake` ถึง `4c9ccf7e` รวมกับงานออมที่ตรวจ Beam
`532baa317cd0c6895a0f1d7a3b1ca9f5544132f3` แล้ว งาน v7 ที่ค้างในโฟลเดอร์เดิม
เก็บเป็น checkpoint `2a5ae90d` ก่อนรวม รวมทั้ง regression ของ processing-policy
โดยไม่ทิ้ง staged work เดิม คู่ที่เลือกใช้ตรวจรอบนี้คือ
`api854-20261003-twenty-bug-development-v9-integrated`.

## ผลตรวจสองข้อความของเพื่อน

Diagnostic/readiness/setter/JDOM packets ผ่าน 982 checksum entries และ identities ตรงกับ
discovery v3 ครบ 691 declarations. Diagnostic เดิมแบ่งเป็น 127 fixture errors,
514 stable normal observations ที่รอ oracle review และ 50 target exceptions.
Setter 4 tests และ JDOM 2 tests ต่อ stage มี counters, suite/result hashes และ target-method
coverage ตรงกัน ทั้งสอง proof มี `fault_detected=false`.
[ตรวจ Beam จาก received bytes ซ้ำ](../../output/api854-20261003/aom-beam532baa31-readiness-audit-v2.json)

ข้อความของ Champ ที่ commit `11a00be0` ตรงกับหลักฐานในรุ่นนั้น ทั้ง 40 worksheet rows,
41 runtime pins และ floor `254,411 + H`. คำอ้างว่ารันผ่าน 6 tests มี structured receipt
และ test definitions รองรับ แต่ raw validation log ไม่อยู่ใน Git จึงไม่ได้รับรอง log hash
หรืออ้างว่ารันชุดเดิมซ้ำแล้ว ข้อความว่า “ยังไม่ push” เป็นสถานะเก่า:
ตอนรับงาน remote branch มีงานต่อถึง `4c9ccf7e` แล้ว.
[การตรวจ peer intake และข้อจำกัด](CHAMP_PEER_INTAKE_REVIEW_TH.md)

## Recipes ที่รับเข้า development composition

รับ `Metaphone.setMaxCodeLen(int)` พร้อม getter/state และ encode observation,
JDOM Attribute projection ของ name/namespace/value และเฉพาะสอง Math signatures
`BigFraction.getField()` / `Fraction.getField()` ที่ `constructor_types=double`, ไม่มี parameters.
Math ใช้ class/zero/one projection และเก็บ factory sources ของ fixed revision เพิ่มสองไฟล์
โดยตรวจ source hashes แยกจาก receiver sources. ไม่เพิ่ม constructor/signature อื่นจาก packet.
[Math intake audit](../../output/api854-20261003/aom-math-field-intake-audit-v2.json)

Runtime policy ใหม่คือ `beam-explicit-fixtures-v7-development`; policy รุ่นเก่ายังคงพฤติกรรมเดิม.
ตรวจ exact retained fixed Java sources แบบ offline: 10 cases ซ้ำสองรอบ ครอบคลุม setter
0/1/4/8, JDOM สองกรณี และ Math สี่กรณี พร้อมตรวจ no-op setter mutation ที่ oracle เห็นความต่าง.
นี่เป็น integration proof ไม่ใช่การรัน Defects4J experiments ใหม่หรือ primary results.
[Runtime proof](../../output/api854-20261003/aom-v9-recipe-runtime-verification-v1.json)

| Development checkpoint | Selected | Unsupported | Max prompt UTF-8 bytes | Request floor เมื่อ output cap = 4,096 |
|---|---:|---:|---:|---|
| Peer v7 | 377 | 314 | 250,315 | 254,411 + H |
| Setter/JDOM v8 | 378 | 313 | 252,219 | 256,315 + H |
| Integrated v9 | 380 | 311 | 258,914 | 263,010 + H |

ทุกแถวคง denominator 691. การเพิ่ม structural capability ใน candidate ไม่ถือว่าปิด
314 declarations เดิมหรืออนุมัติ meaningful oracle ครบทุก declaration.
สี่ enum targets ยังคง unsupported และอยู่ใน denominator; เสนอแยก null-boundary coverage
จาก normal-domain coverage โดย joint decision ยัง pending.
[รายละเอียด enum รวม configure true/false ที่ยังต้องตรวจ](AOM_BEAM_ENUM_REVIEW_TH.md)

## Shared inputs และ limits/reserve รุ่นที่รวมแล้ว

[Preparation index](../../output/api854-20261003/prepare-v9-twenty-bug-development/index.json),
[protocol](../../output/api854-20261003/aom-continuation-v9-integrated/protocol.proposal.json)
และ [runner](../../output/api854-20261003/aom-continuation-v9-integrated/runner-plan.json)
ผูก runtime 41 files. ตรวจ 488 checksum entries ของต้นทาง v3 และ v9,
declaration partitions, exact fixed-source bytes และ source/context/recipe/prompt hashes.
CPU shared loader และ API consumers ทั้งสอง approaches อ่านชุดเดียวกันครบ 20 bugs
ด้วย isolated settings/file transport; runner routing ครบ 10,248 stage keys.
[Shared-input audit และ worksheet 40 แถว](../../output/api854-provider-preflight-20261003/champ-v9-shared-limits-audit-v2.json)

Prompt สูงสุดคือ Math-1 **258,914 bytes**; รวมหนึ่ง prompt ต่อ bug = **3,210,781 bytes**.
ตัวเลข bytes เป็น numerical floor ของ conservative worker guard ไม่ใช่ provider token count.
เมื่อใช้ output cap ที่เสนอ 4,096: prompt reserve ต้องมีอย่างน้อย `258914 + H`
และ request reservation `263010 + H`. H ยัง unknown จึงคง `final_prompt_reserve=null`.
ผลรวม reservation สำหรับ 20 requests ต่อ model ก่อน H = 5,260,200;
สอง models = 10,520,400 เป็น planning envelope ไม่ใช่ actual usage หรือ simultaneous quota requirement.

Historical a01 observations วันที่ 3 ต.ค. 2026 เวลา 00:13 UTC มี remaining
Claude 199,961 และ Gemini 349,979; เทียบ floor ใหม่ก่อน H เหลือ −63,049 / +86,969.
ข้อมูลนี้ไม่รับรอง current quota. Provider token count ของ prompts รุ่นนี้, effective backend settings,
context/output limits, framing, bucket/window/reset/expiry ยัง unknown;
ไม่ประมาณ token count จาก calibration ข้อความสั้น.

Gate A ยัง false, `enabled_stages=[]`, approvals ทั้งสาม false, primary completion = 0.
API worker ปฏิเสธ proposal และสำเนาที่เปลี่ยน approval flag อย่างเดียวก็ยังถูกปฏิเสธ.
ไม่มี KKU request, live queue mutation หรือ quota ledger import ใน integration รอบนี้.
[Gate A ของคู่ integrated](../../output/api854-20261003/aom-gate-a-selected-v9-integrated-v1.json)

## Verification และประวัติรุ่น

ตรวจครบ **351 distinct test cases**: API854 320 และ study 31 ไม่มี failures/errors.
Full regression บน WSL snapshot ผ่าน 348 cases; สอง Windows-only cases และ Aom Store integration
ที่ข้ามใน snapshot ตรวจบน Windows ผ่านครบอีกสาม cases. Runtime hashes ก่อน/หลังรันตรงกันทั้งสองรอบ.
[ผลรวมและ hashes](../../output/api854-20261003/aom-v9-consolidated-offline-validation-v1.json),
[full-run raw log](../../output/api854-20261003/aom-v9-offline-integration-validation-v3.log),
[Windows supplement raw log](../../output/api854-20261003/aom-v9-windows-supplement-validation-v1.log)
และ [snapshot/environment provenance](../../output/api854-20261003/aom-v9-offline-validation-environment-v1.json).

Receipt v3 มี field `passed` ที่นับต่ำไปหนึ่งเพราะหัก class-level skip จาก `testsRun` ซ้ำ;
ผลรวมแก้การนับไว้โดยไม่เปลี่ยน raw log หรือผล success และแก้ runner สำหรับการรันครั้งถัดไป.
Validation v1/v2 ที่หยุดกลางทางเก็บไว้เป็น `interrupted` และไม่อ้างว่าผ่านเต็มชุด.
Preparation v7/v8, v9 draft (`aom-continuation-v9-development`) และ audit v1
เป็นหลักฐานตามรุ่นเดิม; สำหรับคู่ integrated ให้ใช้ audit v2 ข้างต้น.
Audit Beam/Math v1 ที่ protocol อ้างเก็บ hashes เดิมไว้; การตรวจซ้ำ v2 ผูกกับ inspector source ปัจจุบัน.
