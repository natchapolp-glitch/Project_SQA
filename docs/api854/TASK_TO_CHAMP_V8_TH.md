# คำสั่งงานส่งแชมป์: ตรวจรับ v8 และปิด limits/reserve evidence

ออมส่ง shared inputs ที่ commit `e95e979b` แล้ว และทำ host/input/queue preflight เพิ่ม
ขอให้แชมป์ดึงชุดนี้ไปวัด final reserve ของ condition นี้ และร่วมตัดสิน recipes รอบถัดไปกับบีม

## เริ่มทำ

```powershell
git fetch origin
git merge origin/aom
python -m unittest scripts.study.api854.tests.test_fraction_field_shared scripts.study.api854.tests.test_preparation_v8_development -v
python -m scripts.study.api854.inspect_v8_composition --protocol output/api854-20261003/aom-continuation-v8-development/protocol.proposal.json --runner output/api854-20261003/aom-continuation-v8-development/runner-plan.json --output output/api854-20261003/champ-received-v8-audit-NEW
```

อ่าน [v8 handoff](AOM_MATH_FIELD_V8_HANDOFF_TH.md) และ
[task manifest พร้อม input hashes](../../output/api854-20261003/aom-v8-waiting-work-v1/team-task-manifest.json)
หาก runtime หลังรวมต่าง ให้สร้าง preparation/protocol/runner bindings ใน destination ใหม่ก่อนตรวจ reserve
ไม่ refresh hashes ในหลักฐานเดิม; audit นี้ไม่อนุมัติ Gate A หรือ seed/claim งาน

## งานที่ขอให้ส่งกลับ

**ก. ตรวจ exact shared inputs:** ตรวจ 20 bugs, 379 selected / 312 unsupported / denominator 691
และ Math สอง signatures ที่รับแล้ว พร้อม factory knowledge ที่ทุก approach ได้เท่ากัน
ใช้ protocol/index/runner SHA จาก manifest ชุดนี้

**ข. ตรวจ prompt/accounting ทั้ง 40 คู่ bug × model:**
ใช้ prompt bytes/hashes จริงจาก worksheet และกรอก 40 rows ใน
[champ-v8-reserve.template.json](../../output/api854-20261003/aom-v8-waiting-work-v1/return-templates/champ-v8-reserve.template.json)
การตรวจ 40 คู่เป็น accounting/limit review ไม่ใช่คำสั่งส่ง generation requests 40 ครั้ง
หากต้องมี provider preflight ใหม่ ให้แยกหลักฐาน/condition ที่ได้รับอนุญาตไว้ชัดเจน

- Models ที่เลือก: `claude-sonnet-5` และ `gemini-3.5-flash-lite`; temperature 0 / requested output 4096
- Prompt สูงสุด v8: **257,515 UTF-8 bytes**; conservative request floor **261,611 + framing**
- ยืนยัน actual model IDs, settings support, effective context/output limits และ provider framing
- แยก actual provider tokens ออกจาก bytes และส่งวิธีวัด/เวอร์ชันเครื่องมือ/evidence/hash
- ส่ง final prompt reserve และเงื่อนไข fits-limits ที่ตรวจได้สำหรับทั้งสองโมเดล
- quota: observed remaining, unit, bucket/window, reset/expiry และ observed time พร้อมหลักฐาน
- snapshot เก่าและ floor v7 254,411 + H ใช้อนุมัติ v8 ไม่ได้

ถ้า policy byte guard ปัจจุบันเกิน model limit ให้บันทึก blocker และเสนอ prospective policy ร่วมทีม
ไม่ลด guard เฉพาะบัญชี/โมเดลหรือ truncate context ของวิธีเดียวเพื่อให้ผ่าน
credentials/ledger และ raw private provider data เก็บเฉพาะเครื่องแชมป์; ส่งกลับเฉพาะหลักฐานที่ลบ secrets แล้ว

**ค. ร่วมกับบีมรับ candidate รอบต่อไป:** เริ่ม setter/JDOM ได้โดยส่ง scoped acceptance
exact signature, legal preconditions, state/structural oracle และ evidence hashes
แยก target addition จาก repair ของ recipe ที่ selected อยู่แล้ว
ใช้ [joint-candidate-acceptance.template.json](../../output/api854-20261003/aom-v8-waiting-work-v1/return-templates/joint-candidate-acceptance.template.json)
งาน owner แชมป์ยังเหลือ 167 รายการ ซึ่งเป็น subset ของ 312 รายการทั้ง pilot

**ง. ยืนยัน champ-pc1/API coordinator readiness:** รับผิดชอบ API ของ champ/beam/aom
keys/ledger อยู่ host เดียว, global API limit 2 และ outstanding ต่อ account 1 ตาม proposal
ยืนยัน assignment ของ a01 และใช้ observed quota จริงก่อนรับบัญชีอื่น
ยังต้องแจ้งก่อนสลับบัญชีเมื่อ quota หมดตามแผน ไม่สลับอัตโนมัติ

**จ. ตัดสิน enum ร่วมทีม:** คงสี่ signatures/denominator และแยก null-boundary coverage
จาก normal-domain requirement; ไม่ถือข้อเสนอเดิมเป็น joint approval แล้ว

## รูปแบบส่งกลับ

คัดลอก template ไป path ใหม่ กรอก reviewer commit/time/evidence paths/SHA-256 และผลจริง
เปลี่ยน `example_only=false` เมื่อเป็น receipt ที่ตรวจแล้ว; fields ที่ยัง unknown คง null
push branch ที่ทำงานจริง แล้วส่ง **branch + commit + receipt paths** กลับให้ออม
พร้อมบอก `reserve ready` หรือ `blocked` และเหตุผลที่ตรวจยืนยันได้

คิว GET health/schema/status ใหม่:
`https://fax-stake-salvador-experts.trycloudflare.com`
ใช้ role access file ส่วนตัวเดิมและเปลี่ยน `base_url`; tokens เดิมยังใช้ได้
80 งานในคิวนี้ยังเป็น prepare/queued ของ frozen core, 0 attempts
ยังไม่มี v8 generation queue และยังไม่ใช้ `--once`, seed/claim/publish หรือเปิด live pilot
