# แชมป์ตรวจรับ shared v3 และ Beam fixture runtime

รวม `aom d147e216` และ `beam 3ff1a6a4` เข้า branch champ แล้ว ตรวจช่วง offline
ไม่มี KKU API request, paid generation หรือ mutation ของ live queue ในการรับงานนี้
ข้อมูลเดิมที่รอ inventory ครบ 20 bugs ถูกแทนด้วยสถานะ v3 ด้านล่าง

## สิ่งที่ตรวจได้แล้ว

- v3 ครบ 20 bugs / 691 declarations / exclusions 3 รายการ ตรวจ 243 checksum entries
  และเทียบ ZIP ทั้ง 282 file entries กับ checkout bytes ตรงกัน
- Policy, source/receiver partition, index และ runtime pins ของ draft ออมตรงกันก่อนรวม fixture runtime ใหม่
  Frozen-core hash เดิมยังเป็น `675a480915c40ab19f7be57b56b046fb8c8924e7330e4cc8956b2ed3de6d31ad`
- Runner plan ครอบคลุม 10,248 stage keys; แชมป์เป็น API coordinator ของ owners champ/beam/aom
  ตรวจ config/assignment ของ champ-pc1 และการบล็อก off-assignment/primary proposal โดยไม่ส่ง HTTP mutation
- Beam evidence inventories และ execution source snapshots ผ่าน hash audit สี่ suites ของ
  Closure-176/JxPath-1 มี fixed สองรอบ, buggy, coverage และ local review แยกจาก original results
  Closure ตรวจพบบัคทั้งสองวิธี; JxPath ไม่ตรวจพบบัคทั้งสองวิธีตามผลจริง
- รวม opt-in fixture module/recipe guards, canonical target publication และ shared-v3 runner/resolver
  แก้ auto-merge ให้ explicit prompt สร้าง recipe artifact ได้ และ shared targets ใช้ไฟล์ของ composition
  เพิ่ม regression ตรวจทั้งเส้นทาง recipe และการบล็อกเปิด two-bug fixture policy ทั่ว shared v3 ก่อน claim

**ผลตรวจชุดที่รวมแล้ว:** API854 รัน 225 tests ผ่าน 224 / skip 1 เรื่อง Windows symlink privilege;
legacy evaluator/generator และ Java probe integration ผ่าน 30 tests รวมผ่าน 254 ไม่มี failures/errors
Tests ใช้ isolated Store และ mocked provider; Java probe tests ใช้ JDK จริง

คิวจริงตรวจเฉพาะ GET เมื่อ 2026-10-03 06:12:17 เวลาไทย: health/schema ผ่าน,
80 jobs ยัง prepare/queued, attempts=0, เปิดเฉพาะ prepare และ core hash ตรงเดิม
ไม่มี claim/upload/complete และไม่ได้เรียก KKU endpoint ใดในรอบนี้

หลักฐาน:

- [v3 input audit](../../output/api854-provider-preflight-20261003/champ-aom-v3-input-audit-v1.json)
- [Beam packet audit](../../output/api854-provider-preflight-20261003/champ-beam-v3-evidence-audit-v1.json)
- [champ-pc1 runner acceptance](../../output/api854-provider-preflight-20261003/champ-pc1-v3-runner-acceptance-v1.json)
- [Composition validation](../../output/api854-provider-preflight-20261003/champ-composed-v3-validation-v1.json)
- [Gate A checkpoint](../../output/api854-provider-preflight-20261003/champ-composed-v3-gate-a-v1.json)

## ขอบเขต fixture policy และ draft

`beam-explicit-fixtures-v3-proposal` รองรับเฉพาะ TypeInference และ DOM/JDOMNodePointer
ที่บีม review ใน development ของสอง bugs นี้ อีก 18 bugs ยังไม่มี explicit recipe review
Shared prepare v3 เดิมยังไม่ได้ adopt policy นี้; ไม่เปิด flag ให้ทั้งชุดหรือเปลี่ยน original prompts/targets
การ adopt ต้องประกอบ policy/context/prompt/recipe version ใหม่และตรวจรับร่วมกันก่อน freeze

เก็บ draft ออม `protocol.json` และ packets เดิมไว้ ไม่เขียน source hashes ของมันย้อนหลัง
สร้าง [champ-composed-v3.proposal.json](../../experiments/configs/api854-20261003/champ-composed-v3.proposal.json)
ที่ pin runtime หลัง merge ไว้แยกสำหรับออมตรวจต่อ; ไม่มี fixture policy flag ใหม่ใน draft นี้
ยังไม่ frozen, reserve=null, model_settings_verified=false และไม่เปิด stages
เมื่อออมรวม code ชุดนี้ต้องตรวจ source map ใหม่ก่อนตรึง primary bytes/run

## Reserve และ API evidence ที่ยังปิดไม่ได้

Prompt v3 สูงสุด Math-1 = **161,982 UTF-8 bytes**; รวมหนึ่งชุดต่อ bug = 1,338,070 bytes
ตาม guard ของ worker ให้ `H` เป็น provider framing bound ที่ยังต้องมีหลักฐาน:

```
prompt_token_reserve >= 161982 + H
request reservation >= 166078 + H  (ถ้ายังเสนอ output 4096)
```

ตัวเลขนี้เป็น numerical floor ตาม byte guard ไม่ใช่ provider token count หรือ actual quota consumption
ยังคำนวณค่าจองสุดท้ายรวม overhead เป็นตัวเลขเดียวไม่ได้เพราะ H/context/output limits ยังไม่ทราบ
ค่า `final_reserve` จึงคง null; หากสร้าง prompt/recipe version ใหม่ต้องวัดใหม่อีกครั้ง

หลักฐาน model IDs จริงที่มีคือ discovery checkpoint เดิมใน `champ-readiness-v2.json`
ซึ่งพบ `claude-sonnet-5` และ `gemini-3.5-flash-lite` เมื่อสังเกตครั้งนั้น
รอบนี้ไม่เรียก KKU API ใหม่ตามคำสั่งทีม จึงยังไม่ยืนยัน runtime temperature 0/output 4096,
resolved version, model-specific limits หรือ observed quota จากข้อมูลที่ยังไม่มี
ต้องรับ actual remaining/unit/bucket/window/expiry และ provider settings/limits/framing evidence
ผ่าน `.local/api854/provider-evidence.private.json`; ไม่ใส่ keys/tokens ในเอกสารหรือ Git

## งานที่รอเพื่อไปต่อ

1. บีม: fixtures/oracles อีก 18 bugs, shared-v3/explicit-policy adoption และ semantic review ร่วมทีม
   พร้อมหลักฐาน target execution; ยืนยัน environment/assignment ของ beam-pc1/2/3
2. แชมป์/เจ้าของบัญชี: actual API settings/limits/framing และ quota evidence เพื่อปิด reserve
3. ออม/ทั้งทีม: host readiness ที่ยังขาด, runner/shared policy review และ Gate A ร่วมกัน
   จากนั้นออมจึงตรึง primary bytes/hash และ seed run ใหม่ โดยคง core-preflight 80 งานเดิม
