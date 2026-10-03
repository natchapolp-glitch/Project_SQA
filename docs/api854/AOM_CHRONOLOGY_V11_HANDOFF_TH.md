# ออมส่ง shared Chronology v11 สำหรับตรวจรับรุ่นใหม่

รับ bounded candidate ของบีม `477b4f8a`, คำรับร่วมแชมป์ `7de14726` และ requirements บีม `8bde1b80`.
เพิ่มเฉพาะ 6 identities ของ Time-1 Partial หลังตรวจ shared invocation/oracle.
Preparation ครบ **20 bugs / 396 selected / 295 exclusions / denominator 691**;
รักษา setter/JDOM/Math/factories, Buffer 8/Csv และ Lang 2 จาก immutable Aom `a4880fb2`.
จำนวน selected เป็น capability subset สำหรับ development; ไม่รับรอง semantic coverage ครบหรือ Gate A.

## ชุดที่ต้องตรวจรับ

- Preparation: `output/api854-20261003/prepare-v11-chronology-development-v3/index.json`
- Protocol/runner: `output/api854-20261003/aom-continuation-v11-development-v3/`
- Joint intake: `output/api854-20261003/aom-chronology-v11-intake-v1/receipt.json`
- Shared invocation/JDI proof: `output/api854-20261003/aom-chronology-v11-integration-v3/receipt.json`
- Retained 64-case proof: `output/api854-20261003/aom-v11-preserved-runtime-v2.json`
- JUnit packaging/fixture separation: `output/api854-20261003/aom-v11-suite-packaging-v2/receipt.json`
- Checklist/40 prompt pairs/completion/checksums: `output/api854-20261003/aom-v11-readiness-v1/`

Exact protocol/index/runner/runtime hashes อ่านใน completion-receipt.json และ final-checksums.json.
ตรวจ actual bytes ก่อน import. Runtime pins ครบ 41 files.
Context-manifest/metadata/prompt/targets/fixture-recipes เป็นชุดเดียวสำหรับทั้ง4 approaches.

## หลักฐานและขอบเขต

เรียก protected getField/package-private constructor ผ่าน exact reflection บน production final Partial.
Method receiver เริ่ม default constructorแล้ว seed year=2024/hour=10 ก่อน tracing.
ใช้ production ISO/Buddhist, default UTC/UTCProvider และ UTC/fixed +07:00 เท่านั้น.
ตรวจ chronology class/zone, named/indexed values, public input/getter-array copies,
Buddhist epoch-year2513/supplied field identity, unchanged receivers และ retain same/new identity.
Internal constructor ใช้ validated UTC arrays; ไม่อ้าง clone/validation/zone normalization.
Invalid hour/date/order/index ตรวจ exact exception classes. Setup/projection errors เป็น fixture_error;
JUnit มี SQA_HARNESS marker ให้ evaluator ปฏิเสธการนับเป็น fault.
Case buckets อยู่ใน vector domain [-1,1] ให้ CMA-ES/FSCS-ART เข้าถึงครบ.

ซีล helper/driver/independent oracle ก่อนรัน: fixed13casesผ่านสองรอบ; buggy12ผ่าน/1ล้มเหลวที่ arrays_bad_orderสองรอบ.
JDI first-entryครบ13cases/6declarationsทั้งสองrevisionบน bytecodeเดิม; ไม่มี line/branch percentage claim.
Temporary ignored-chronology mutation ถูกจับที่ getfield_buddhist.
JUnit nested helper fixedสองรอบ/buggyสองรอบตรงกัน พร้อม executed13/skipped0/target_checks13.
Controlled setup failure ถูก evaluator ปฏิเสธการนับเป็น fault.
Recipes เดิม64cases ผ่าน fixedซ้ำ พร้อม legacy-policy checks และ setter sensitivity.
Tests/80consumer combinations อ่าน completion receipt และ raw regression log.
ทั้งหมดเป็น local shared integration/reference development evidence;
ไม่ได้เพิ่มผลพบบัคของ algorithms/primary หรือรัน full Defects4J evaluation ใหม่.

v7/v8/v10 artifactsคงbytesเดิม. Chronology proof attempt1/v1/v2 เก็บก่อนแก้ review; v3 เป็นปัจจุบัน.
Preparation v11 ที่ไม่มีsuffixถูก audit block เพราะขาด v3 discovery lineage (aom-v11-composition-attempt1.json).
Preparation suffixv2ใช้helperก่อนเพิ่ม harness marker; ใช้ suffixv3และ protocol pairข้างบนเท่านั้น.

## งานส่งต่อ

**บีม:** ตรวจ six identities/receiver/preconditions/oracles/raw proof, รับ consumersทั้ง4และ host beam-pc1 CPU1slotใหม่.
ส่ง scoped receipt paths/hashesผูก v11 protocol/index/runner/runtime พร้อมแยก295exclusions/enum4ที่คงdenominator.
คำรับ v10เดิมไม่โอนเป็นการรับ v11โดยอัตโนมัติ.

**แชมป์:** ตรวจ shared implementation/runner และ40prompt×model pairsจาก worksheetรุ่นนี้.
Prompt ใหญ่สุด **278,491 UTF-8 bytes**; requested `claude-sonnet-5`/`gemini-3.5-flash-lite`, temperature0/output4096.
ยืนยัน effective settings และวัด actualtokens/context limits/framing/reserveรวมoutput/overheadจาก promptsเหล่านี้.
ส่ง current quota remaining/bucket/reset/expiry/evidence; historicalfloorsใช้แทนfinalreserveไม่ได้.
Credentialsคงอยู่ใน ignored privatefileเท่านั้น.

**ทั้งทีม:** 295exclusions/fullrequirement/meaningfuloracles/GateAยังpending.
Development proposal, enabled_stages=[], generation_ready=false, final_reserve=null;
KKUrequests=0/livequeuemutations=0/primaryadded=0. ยังไม่freezeหรือเปิดlivepilot.

```powershell
python -B -m unittest -v scripts.study.api854.tests.test_chronology_v11 scripts.study.api854.tests.test_preparation_v11_development
python -B -m scripts.study.api854.seal_v11_handoff
```

Java rerunใช้destinationใหม่และbindingใหม่อย่างเปิดเผย; ไม่เขียนทับsealedreceiptsหรือแก้runtimeอ้างอิงย้อนหลัง.

## Exact final condition bindings

- protocol_sha256: `6319e2ccc9938dd8cc811745ed3ddde96f6d4ff80bbda592f8808453cfdf2028`
- preparation_index_sha256: `a3af148d8ef1232ac3e46163598d41315bfc38bafe0985da855675ffff95e5f4`
- runner_plan_sha256: `462f6196d81dfc9ed1bda31ce0ae81e59759341b6671cb7fc9d9301fe453e048`
- shared_integration_receipt_sha256: `2e366f819591d82d4a49156ca10d49731f18cd91d56d46d22473a15965e6b806`
- packaging_receipt_sha256: `ab99173e87089b1e7195f47dfefabeac65106ae1ee309ab3525c4c6548d973b5`

Fresh regression: 72 tests passed, no failures/errors/skips; four-consumer combinations=80.
