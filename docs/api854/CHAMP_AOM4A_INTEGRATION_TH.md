# แชมป์ตรวจ integration Aom 4a0e699c

รวม `aom 4a0e699cb0ff44b90368cd8d27a0bcac134736dc` ต่อจาก `champ 7e09a5fe`
ซึ่งมีงานบีม `3ae6f2fb` แล้ว รับ selected-pair Gate A checker และตรวจ integration แบบ offline
งานรับ keys พักไว้ตามคำขอผู้ใช้ รอบนี้ไม่มี KKU request, live queue mutation หรือการเปิด pilot

## การแก้ที่เพิ่มในการตรวจรับ

Checker ของออมบังคับ `--protocol` / `--runner` และตรวจ runner, preparation index,
runtime source inventory, fixture recipes, discovery lineage และ prompt bytes แล้ว
พบกรณีเปลี่ยน runtime pins ใน proposal ให้ตรงโค้ดใหม่ แต่ยังใช้ embedded recipe sources เก่า:
checker ที่รับมารายงาน runtime และ recipe binding ว่าผ่านพร้อมกันได้

เพิ่มการเทียบ recipe source hashes กับ `source_sha256` ของ protocol ที่เลือกโดยตรง
ผล [regression ก่อน/หลังของ runtime สุดท้าย](../../output/api854-provider-preflight-20261003/champ-aom4a-recipe-runtime-regression-v2.json)
ยืนยันว่า runtime binding ผ่าน แต่ recipe binding ต้อง blocked เมื่อยังเป็น recipe เก่า
Gate A และ generation authorization เป็น false ทั้งก่อนและหลัง ไม่มีการส่ง API ในการตรวจนี้
Regression test ใช้ proposal/recipes ที่ compose ใหม่เฉพาะ temporary directory;
ไม่เขียนทับ historical preparation, frozen core หรือ proposal ที่ทีมเคยส่งมา

ชุดทดสอบเต็มรอบแรกพบ Windows race ในการสร้าง stage พร้อมกัน: `Path.resolve()` ส่ง path เดียวกัน
กลับมาเป็น drive path และ extended-path prefix ต่างกัน ทำให้ containment check ปฏิเสธโดยผิดพลาด
แก้ `common.contained` ให้ normalize drive/UNC spelling หลัง resolve links ก่อนเทียบ boundary
เพิ่ม regression สอง tests สำหรับ equivalent paths และการปฏิเสธ outside/root paths
ตรวจสร้างงานพร้อมกัน 400 รอบหลังแก้ไม่มี unexpected containment error; ยังมีผู้สร้าง stage สำเร็จเพียงหนึ่งรายต่อรอบ
เก็บผล full run ที่ error ไว้ใน validation receipt และรันชุดเต็มใหม่หลังแก้

รอบถัดมาพบ legacy HTTP service ปิด connection หลังตอบ 401 ทั้งที่ยังมี POST body ไม่ได้อ่าน
แก้ให้ discard body ที่มี Content-Length ภายใน MAX_BODY ด้วย timeout โดยไม่ parse JSON หรือเรียกคิว
เพิ่ม test ส่ง unauthorized invalid JSON ห้าครั้ง ต้องได้ 401 และ queue snapshot เดิม
ชุด server หลังแก้ผ่าน 6 tests; เก็บ failed runs ไว้และตรวจ full validation อีกครั้งกับโค้ดสุดท้าย

## คู่ v5 เดิมเมื่อใช้กับ runtime ที่รวมล่าสุด

ใช้คู่เดิมอย่างชัดเจน:

```powershell
python -m scripts.study.api854.gate_a `
  --protocol experiments/configs/api854-20261003/champ-composed-v5.proposal.json `
  --runner output/api854-20261003/aom-continuation-v5/runner-plan.json `
  --output .local/api854/gate-a-selected-review-new.json
```

เลือก output ใหม่ที่ยังไม่มีไฟล์อยู่ Checker สร้างรายงานและไม่แก้ protocol/queue
ดู [ผล selected pair ที่ตรวจจริง](../../output/api854-provider-preflight-20261003/champ-aom4a-selected-v5-integrated-v2.json)

| สิ่งที่ตรวจ | ผลใน checkpoint นี้ |
|---|---|
| Protocol ↔ runner และ preparation index | pass |
| Recipe ↔ protocol sources ของคู่เก่า | pass เฉพาะ 5 bugs ที่มี |
| Protocol sources ↔ runtime ที่รวมปัจจุบัน | blocked: pins เก่า 40 ไฟล์ ขณะที่ runtime ปัจจุบันมี 41 |
| Shared preparation | 5/20 bugs, 124/691 declarations; blocked |
| Routing ใน runner proposal | ครบ 10,248 stage keys; pass |
| Host / semantic / provider / quota / three-owner acceptance | pending |
| Gate A / generation authorization | false |

Recipe ของคู่เก่าตรงกับ protocol เก่าไม่ได้ยืนยันว่าใช้งานกับ runtime ใหม่ได้
ต้อง compose และตรวจ condition ใหม่จาก runtime/recipes/policy ที่ตกลงร่วมกัน
ไม่เพียงเปลี่ยน source pins ของ proposal แล้วนำหลักฐานเก่ามารับรอง condition ใหม่

Prompt ใหญ่สุดของคู่ห้าบั๊กเดิมยังเป็น **177,698 UTF-8 bytes** ไม่ใช่จำนวน tokens
บีมมี sampled local suite evidence ครบ 20 bugs ใน packets ที่รับก่อนหน้านี้ แต่ development recipe
ยังรองรับ 377/691 declarations ตาม [บันทึกรอบแก้บีม](CHAMP_BEAM_REPAIR_INTAKE_TH.md)
จึงยังต้องเติม support/review อีก 314 declarations และตรวจ semantic/shared contract ร่วมทีม
ข้อจำกัด FromXmlParser.Feature สี่ targets ยังต้องตัดสินร่วมโดยคง requirement/inventory ไว้

## หลักฐานออมที่รับพร้อม merge นี้

ตรวจ received worksheet/hashes และ provenance ของ Aom 37-test receipt กับ source bytes ใน commit `4a0e699c`
ตรวจ source/build prerequisite packet v1 และ recovery v2/v3 รวม 5,438 artifact hashes
final index ผูก ownership และ raw records ครบ **284 unique bugs** พร้อม fixed-source/compile receipts
ตรวจ public checkpoint hashes อีก 12 รายการ

การตรวจนี้อ่านหลักฐานจากเครื่องออม ไม่ได้ checkout/compile Defects4J 284 bugs ซ้ำบนเครื่องแชมป์
Source preparation/build prerequisite ยังมี primary_completed=0 และ primary_usable=false
ดู [integration audit ของ runtime สุดท้าย](../../output/api854-provider-preflight-20261003/champ-aom4a-integration-audit-v2.json)
และ [Aom selected checker handoff](AOM_GATE_A_B605_ACCEPTANCE_TH.md)

## Validation

ผลชุดทดสอบรอบ integration นี้และ source/log hashes อยู่ใน
[validation receipt](../../output/api854-provider-preflight-20261003/champ-aom4a-integration-validation-v1.json)
ชุดเต็มสุดท้ายรัน **330 tests: ผ่าน 329 / skip 1 / failures 0 / errors 0**
API854 รัน 300 tests (299 pass / 1 skip) และ legacy Java probes รัน 30 tests ผ่านทั้งหมด
รายการ skip เป็น source-link fixture ที่ Windows ไม่มีสิทธิ์สร้าง symlink
ชุดเฉพาะ selected-pair/runner/shared-v5/API worker/preflight review ผ่าน **43 tests** ก่อนพบ Windows path race
ไม่รวม 43 focused tests ซ้ำเป็นจำนวน unique tests และไม่ relabel Aom 37 tests เป็นผลบนเครื่องแชมป์

## สิ่งที่ยังต้องได้ก่อน Gate A

- บีม: final recipes/fixtures/oracles และหลักฐาน target execution/coverage ให้ครบ 691 declarations,
  review semantic/fixture/oracle/shared contract ร่วมทีม พร้อมข้อสรุป targets ที่มีข้อจำกัด
- ออม: รวม final 20-bug prompts/recipes ให้ตรง runtime ที่ตกลง เก็บ index/source/prompt/recipe/policy hashes
  และตรวจ protocol/runner pair กับ checker รุ่นที่รวมแล้ว พร้อมรวบรวม host acceptance ทั้งสามเครื่อง
- แชมป์: limits, provider framing, bucket/window/reset/expiry และหลักฐาน settings ที่ใช้จริงยัง pending;
  final reserve ต้องคำนวณจาก final 20-bug prompts ใหม่เมื่อได้ชุดครบ

Worksheet ของห้าบั๊กใช้ historical remaining snapshot ของ a01 เท่านั้น
ค่ารับ HTTP 200 ใน preflight เดิมไม่ยืนยัน effective backend settings หรือ limits และยังไม่พร้อม import quota ledger
ยังไม่มี final reserve, ไม่มี live generation และยังไม่ตรึง primary protocol หรือ seed primary queue ใหม่

## ข้อความให้ทีมหลังดึง champ

ออม: แชมป์รวม `4a0e699c` และตรวจ integration แล้วครับ เพิ่ม guard ให้ recipe sources ตรง protocol pins
เมื่อเปลี่ยน runtime ต้อง compose recipe/prompt condition ใหม่ด้วย คู่เดิมยัง 5/20 bugs และ 124/691
runtime pins เก่าถูก blocked ตามคาด รอ final 20-bug prompts เพื่อคำนวณ reserve ใหม่; Gate A ยังปิดครับ

บีม: ดึง champ รุ่นที่ส่งพร้อมบันทึกนี้ได้ครับ Checker ใช้ `--protocol` / `--runner` และตรวจ recipe/runtime binding แล้ว
ทำ support/review อีก 314 declarations ต่อ พร้อมส่ง final shared recipes/policy และข้อสรุปสี่ enum targets
เพื่อให้ออมรวมครบ 20 bugs/691 declarations ยังไม่เรียก KKU หรือเปิด pilotครับ
