# ออมส่ง shared inputs v8: Math getField สองรายการที่รับแล้ว

วันที่ 3 ต.ค. 2569 ตรวจทุก remote branch แล้วพบข้อเสนอที่
`codex/beam-readiness` commit `0b560f05` และคำตัดสินแชมป์ที่
`codex/champ-v7-intake` commit `4c9ccf7e` จึงรวมเข้า branch `aom` แล้ว
branch `beam`/`champ` ปกติยังไม่ได้มีคำตัดสินล่าสุดนี้ตอนตรวจ

## ขอบเขตที่รวม

รับเฉพาะ `org.apache.commons.math3.fraction.BigFraction.getField()` และ
`org.apache.commons.math3.fraction.Fraction.getField()` ของ Math-1:
`constructor_types=double`, `parameter_types` ว่าง ตาม
[คำตัดสินแชมป์](CHAMP_MATH_FIELD_ACCEPTANCE_TH.md) และ
[receipt ที่ตรึง bytes แล้ว](../../output/api854-provider-preflight-20261003/champ-math-field-candidate-acceptance-v1.json)

- คง selected v7 เดิมทั้งหมด เพิ่มเพียงสอง signatures: **379 selected / 312 unsupported / 691 declarations**
- Math-1 เปลี่ยนจาก 51/34 เป็น 53/32; 19 bugs อื่นมี selected/exclusions และเหตุผลเดิม
- Champ-owner worklist เหลือ **167 จาก 169** เป็นคนละขอบเขตกับ 312 รายการของทั้ง pilot
- คง 20 pilot bugs เดิม; ไม่ใช่เพิ่ม bugs ที่ตรวจพบ fault อีกสองตัว
- เก็บ artifacts v7 และหลักฐาน candidate เดิม ไม่เปลี่ยน label เป็นผลของ v8

receiver เป็น production fraction ที่สร้างสำเร็จจาก finite double ไม่รับ setup exception เป็น target result
projection ตรวจ runtime element class, zero=0/1, one=1/1 และ rational state ของ receiver
ไม่ใช้ object identity/hash และไม่รับ constructors/methods อื่นตาม helper candidate ทั้งก้อน

## ชุดที่ส่งให้ทั้งสี่ approaches

- [preparation index ครบ 20 bugs](../../output/api854-20261003/prepare-v8-fraction-field-development/index.json)
- [protocol proposal v8](../../output/api854-20261003/aom-continuation-v8-development/protocol.proposal.json)
- [protocol SHA-256 sidecar](../../output/api854-20261003/aom-continuation-v8-development/protocol.proposal.json.sha256)
- [runner plan](../../output/api854-20261003/aom-continuation-v8-development/runner-plan.json)
- [composition receipt](../../output/api854-20261003/aom-v8-composition-audit-v1/receipt.json)
- [worklist ที่เหลือ 312 รายการ](../../output/api854-20261003/aom-v8-composition-audit-v1/worklist.json)
- [prompt/reserve worksheet สำหรับแชมป์](../../output/api854-20261003/aom-v8-composition-audit-v1/prompt-reserve-worksheet.json)
- [Gate A checklist ของคู่ใหม่](../../output/api854-20261003/aom-v8-composition-audit-v1/gate-a-checklist.json)

Protocol SHA-256:
`bce120772c3d30c1d4ac1083ba67b15ba9e2c5df0f357e216f03a39bb8e9d6f1`

Index SHA-256:
`1504c82a734cd342e12199b694d71abbfe79b3bcbf66643bbf21f825fd83e950`

Contract ใหม่ `aom-beam-prepare-v8-development` / fixture policy
`aom-beam-fraction-field-v6-development` ผูก runtime ปัจจุบัน 41 files
แต่ละ bug มี context, prompt, target inventory, metadata, recipe และ checksums ตรงกัน
CPU/API consumers อ่านชุดเดียวกัน ไม่อาศัย execution feedback

เพิ่ม fixed production `BigFractionField.java` และ `FractionField.java` ใน Math context/prompt
และให้ fresh prepare worker export bytes ที่ตรึงไว้ด้วย
metadata แยก `additional_fixture_source_sha256` จาก receiver/modified-source mapping
จึงไม่ขยาย classes ที่ evaluator ใช้วัด modified coverage
v8 ตรวจ source bytes ใน prompt เพิ่มด้วย: ตัด factory ออกแล้ว rehash prompt ใหม่ก็ยังถูกปฏิเสธ

Prompt ใหญ่สุด **257,515 UTF-8 bytes** (Math-1) เพิ่มจาก v7 7,200 bytes
ตาม conservative byte guard เดิม request floor รวม output 4096 เป็น **261,611 ก่อน overhead**
ตัวเลขนี้ไม่ใช่ actual provider token count และยังไม่มี final reserve; แชมป์ต้องวัด framing/context/quota ใหม่

## การตรวจที่ทำแล้ว

- Windows API854 suite: **315 tests, 313 ผ่าน, ข้าม 2** (symlink บน Windows และ Math probe ที่ต้องรันใน WSL)
- WSL study suite: **31 tests ผ่าน** รวม real Java probe integrations
- WSL Math probe regression ใหม่: ผ่าน; ทั้งสอง targets × สอง finite values ให้ fixed observations ซ้ำกัน
- [JUnit/evaluator v8 receipt](evidence/aom-fraction-field-v8-20261003-attempt2/receipt.json):
  fixed สองรอบ, buggy และ coverage ต่างรัน executed=4 / skipped=0 / target_checks=4
  `getField` coverage hits=2 ทั้งสอง classes; **fault_detected=false**
- ตรวจ fresh Math-1f identity/tag และทุก manifest source กับ fresh Git HEAD รวม factory sources
- [verification receipt](evidence/aom-v8-verification-20261003/receipt.json) ผูกผลตรวจกับ runtime/inputs/protocol นี้
- ผู้ตรวจ diff ไม่พบ Critical/Important; แก้ descriptive policy label ที่ค้างแล้ว

[attempt แรกที่หยุด](evidence/aom-fraction-field-v8-20261003/failure.json) เก็บแยกไว้:
verifier เทียบ synthesized commit IDs ข้าม checkouts จึงหยุดก่อน observations/evaluation
รอบสองตรวจ fixed tag ภายใน checkout นั้นและ source bytes แทน ไม่ลดการตรวจ source lineage
รอบ discovery ที่ใช้ options ไม่ตรงโครงสร้าง tests ถูกแก้คำสั่งแล้ว; ไม่ใช้ผลรอบนั้นเป็น passing evidence

## ส่งต่อให้ใครทำอะไร

**แชมป์:** fetch และรวม `origin/aom` แล้วตรวจ index/protocol/runner/checksums ชุด v8
วัด prompts ทุก bug ตาม worksheet ด้วย model IDs/settings ที่เลือกไว้
ยืนยัน actual context/output limits, framing/reserve และ current remaining/bucket/reset/expiry พร้อมหลักฐาน
อย่านำ reserve ของ v7 หรือ quota snapshot เดิมมาอนุมัติชุดใหม่โดยตรง
ถ้ามีการเปลี่ยน runtime หลังรวม ให้ build/compose/audit ใหม่ใน destination ใหม่ก่อนตรวจรับ

**บีม:** ตรวจว่ารายการรับสองรายการและ factory/projection อยู่ในชุด v8 ตรงคำตัดสิน
review semantic/fixture/oracle ของ final shared condition ต่อร่วมกับแชมป์
ทยอยส่ง candidate ที่ทั้งสองรับแล้วพร้อม exact signatures, preconditions, fixed source/projection hashes
และหลักฐาน fixed สองรอบ/buggy/coverage; งานอื่นใน 312 รายการยังไม่ได้รับอัตโนมัติ
สี่ targets ของ empty `FromXmlParser.Feature` ยังคงอยู่ใน denominator และรอคำตัดสินร่วม

**ออม:** รอบนี้รวมและตรวจชุดแรกเสร็จแล้ว รอ scoped acceptance รอบถัดไปเพื่อเพิ่มเป็นชุดใหม่
และรอ reserve/provider/semantic/host/team receipts ก่อนทำ final frozen protocol

**ทั้งทีม:** ตรวจ runner ของ champ-pc1 / beam-pc1 / aom-pc1 และ Gate A ของ condition ใหม่ร่วมกัน
ตอนนี้ `enabled_stages=[]`, `prompt_token_reserve=null`, Gate A=false, primary added=0
ไม่มี KKU generation request หรือ live queue mutation ในงานรอบนี้; ยังไม่เปิด pilot

## ตรวจซ้ำแบบออฟไลน์

```powershell
git fetch origin
git merge origin/aom
python -m unittest discover -s scripts/study/api854/tests -t . -q
python -m scripts.study.api854.inspect_v8_composition --protocol output/api854-20261003/aom-continuation-v8-development/protocol.proposal.json --runner output/api854-20261003/aom-continuation-v8-development/runner-plan.json --output output/api854-20261003/received-v8-audit-NEW
```

ปลายทาง audit ต้องยังไม่มีอยู่; checker ไม่ freeze protocol หรือ seed/claim งาน
ถ้า runtime ต่างหลังรวม ให้ใช้ `build_prepare_v8_development` จาก prepare-v3 และ
`compose_v8_development` สร้างชุดใหม่ก่อน audit ห้าม refresh hashes ในหลักฐานเดิม
