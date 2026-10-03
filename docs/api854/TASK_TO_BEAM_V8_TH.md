# คำสั่งงานส่งบีม: ตรวจรับ v8 และส่ง candidate ที่ตกลงแล้ว

ออมรวม Math getField สองรายการที่แชมป์รับแล้ว และ push inputs ที่ commit `e95e979b`
ขอให้บีมตรวจ exact v8 condition และร่วมกับแชมป์ตัดสิน recipe รอบถัดไป
ออมตรวจ input bindings/เครื่องตัวเองและกู้ preparation queue ให้แล้ว; ยังไม่เปิด Gate A/pilot

## เริ่มทำ

1. อ่าน [v8 handoff](AOM_MATH_FIELD_V8_HANDOFF_TH.md) แล้ว fetch/รวม `origin/aom` โดยเก็บงานเดิมไว้
2. ตรวจ protocol/index/runner และ runtime hashes ตาม
   [task manifest](../../output/api854-20261003/aom-v8-waiting-work-v1/team-task-manifest.json)
3. หาก runtime หลังรวมต่าง ให้แจ้ง commit/diff และสร้าง preparation/protocol ใน destination ใหม่
   อย่าแก้ hashes ของ v8 เดิมหรือย้ายหลักฐานเดิมเป็นผลของ condition ใหม่

```powershell
git fetch origin
git merge origin/aom
python -m unittest scripts.study.api854.tests.test_fraction_field_shared scripts.study.api854.tests.test_preparation_v8_development -v
python -m scripts.study.api854.inspect_v8_composition --protocol output/api854-20261003/aom-continuation-v8-development/protocol.proposal.json --runner output/api854-20261003/aom-continuation-v8-development/runner-plan.json --output output/api854-20261003/beam-received-v8-audit-NEW
```

ผล audit ที่ input bindings ผ่านแต่ Gate A=false เป็นสถานะที่คาดไว้ตอนนี้
Math probe ต้องรันใน Linux/WSL ที่มี retained Math classes; ข้ามบน Windows ไม่แทนผลรันจริง
อ่าน [หลักฐาน fixed สองรอบ/buggy/coverage ของ v8](evidence/aom-fraction-field-v8-20261003-attempt2/receipt.json)

## งานที่ขอให้ส่งกลับ

**ก. ตรวจรับ Math composition:** ยืนยัน exact constructor `double` / getField arguments ว่าง
ทั้ง BigFraction/Fraction; factory source/projection และ preconditions ตรงคำตัดสิน
และ CPU/AI ทั้งสี่ approaches ได้ knowledge เดียวกัน
modified-source coverage mapping ต้องไม่รวม factory sources เพิ่ม

**ข. ร่วมกับแชมป์ตัดสิน setter/JDOM เป็นชุดแรก:** ส่ง verdict ที่ชัดเจนจากทั้งสองคน
รวม getter/state oracle ของ setter และ JDOM Attribute projection
การที่ suite/checksums/counters ผ่านเป็น evidence integrity; ยังต้องระบุ preconditions/oracle ที่รับ
JDOM อาจเป็นการซ่อม recipe ของ target ที่ selected อยู่แล้ว ต้องระบุ `repair_existing_selected_recipe`
ไม่ใช่นับทุก recipe repair เป็นการปิด unsupported declaration เพิ่ม

**ค. ทยอย review งานที่เหลือ:** ใช้ [worklist 312 รายการ](../../output/api854-20261003/aom-v8-waiting-work-v1/composition-audit/worklist.json)
ระบุ exact signatures, fixture/source hashes, domain preconditions และ oracle
พร้อม repeated fixed observations, unchanged JUnit fixed สองรอบ/buggy/target coverage
รายงาน executed/skipped/target_checks จริงและ setup failures แยกจาก target results
ไม่เลือก targets จาก buggy outcomes; รายการที่ยังไม่ได้รับต้องคง pending

**ง. ยืนยัน beam-pc1:** ส่ง environment/CPU-lock/shared-worktrees-root evidence ที่ผูกกับ runner SHA นี้
ใช้หนึ่ง CPU slot และระบุ readiness ของ prepare, CMA-ES/FSCS-ART generation, evaluation
แชมป์เป็นผู้ส่ง API ทุก owners ตาม runner plan

**จ. enum สี่ signatures:** คง denominator 691 และเสนอคำตัดสินร่วมพร้อมแชมป์/ออม
null-boundary coverage ต้องแยกจาก normal-domain coverage
หาก requirement ต้อง legal non-null domain ให้ส่งข้อจำกัดนี้ให้ทีม/ผู้กำหนด requirement ตัดสิน
อย่าลบ targets, สร้าง enum ปลอม หรือปิดยอดด้วย NPE จาก null โดยลำพัง

## รูปแบบส่งกลับ

คัดลอก templates ไป path ใหม่ แล้วกรอกผลจริง:

- [beam-v8-review.template.json](../../output/api854-20261003/aom-v8-waiting-work-v1/return-templates/beam-v8-review.template.json)
- [joint-candidate-acceptance.template.json](../../output/api854-20261003/aom-v8-waiting-work-v1/return-templates/joint-candidate-acceptance.template.json)

เติม reviewer commit, observed time, evidence paths และ SHA-256; เปลี่ยน `example_only=false`
เมื่อเป็น receipt ที่ตรวจจริงแล้ว โดย verdict ที่ยังไม่ทราบคง null/pending
ไม่แก้ template ต้นฉบับและไม่ใส่ API keys/worker tokens ใน Git
push branch ที่ทำงานจริง แล้วส่ง **ชื่อ branch + commit + receipt paths** กลับมา
candidate ที่จะให้ออมรวมต้องมี scoped verdict ของทั้งบีมและแชมป์ ไม่รับ helper policy ทั้งก้อนโดยปริยาย

คิวสำหรับ GET health/schema เท่านั้นในรอบนี้:
`https://fax-stake-salvador-experts.trycloudflare.com`
ใช้ role access file ส่วนตัวเดิมและเปลี่ยน `base_url`; tokens เดิมยังใช้ได้
คิวมี 80 prepare/queued, 0 attempts และผูก frozen core เดิม; ไม่ใช่ v8 generation queue
ยังไม่ใช้ worker `--once`, claim/publish งาน, live generation หรือ seed v8
