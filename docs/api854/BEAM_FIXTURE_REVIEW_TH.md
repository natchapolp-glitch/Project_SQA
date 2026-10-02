# Beam: explicit fixture และ eligibility import — 2026-10-03

งานรอบนี้แก้ fixture ของ Closure-176 และ JxPath-1 และรัน evaluator จริงกับ FSCS-ART/CMA-ES รวม 4 suites ใน development condition ใหม่ ไม่ใช่ผล primary pilot และยังไม่ใช่การรับรองครบ 20 bugs/80 jobs

## ผลที่ส่งให้ออมตรวจรับ

| Bug / approach | fixed รอบ 1 / 2 | buggy failures | fixed line coverage | fixed branch coverage |
| --- | --- | --- | --- | --- |
| Closure-176 / FSCS-ART | ผ่าน / ผ่าน | 2, ตรวจพบบัค | 214/739 | 72/489 |
| Closure-176 / CMA-ES | ผ่าน / ผ่าน | 2, ตรวจพบบัค | 242/739 | 82/489 |
| JxPath-1 / FSCS-ART | ผ่าน / ผ่าน | 0, ไม่ตรวจพบบัค | 144/773 | 67/562 |
| JxPath-1 / CMA-ES | ผ่าน / ผ่าน | 0, ไม่ตรวจพบบัค | 220/773 | 106/562 |

ทุก suite มี 30 tests; fixed-1, fixed-2, buggy และ coverage แต่ละ stage บันทึก executed=30, skipped=0, target_checks=30 จริง ไม่มี unexpected fixed exceptions ใน retained tests ทั้ง 120 cases มีหลักฐาน coverage ตรงกับ method descriptor ของ target ที่เลือก ไม่อ้างว่าเรียกครบทุก eligible method

วัด supplementary buggy coverage ด้วย archive เดิมและตรวจ SHA-256 อีกครั้ง: Closure FSCS/CMA-ES lines=213/736, 241/736 และ branches=72/485, 82/485; JxPath FSCS/CMA-ES lines=144/773, 220/773 และ branches=67/556, 106/556 ค่า denominator ของ fixed/buggy อาจต่างกัน จึงเก็บแยกกัน

ผล semantic review แยกจาก evaluator เดิม: reviewer=`Codex local development review`, verdict=valid และ local_development_usable=true สำหรับสี่ suites นี้เท่านั้น ผล evaluator ต้นฉบับยัง usable=false ตามเดิม ไม่แก้ย้อนหลังให้ดูเหมือนผ่าน Gate A; team_or_primary_approval=false และ gate_a_approved=false ยังต้องให้ทีมตรวจรับ recipe/preconditions/oracles

## สิ่งที่แก้และขอบเขต policy

- เพิ่ม opt-in `beam-explicit-fixtures-v3-proposal` โดย default legacy ยังเหมือนเดิม Constructor/setup failure และ unsupported argument type ไม่ถูกแปลงเป็น target exception หรือ silent null เพื่อสร้าง test
- Closure ใช้ production parser/registry/scope/CFG/reverse interpreter และ FlowScope ที่อยู่ใน dependency graph เดียวกัน AST เลือกตามชนิด operation; assertions ตรวจ scalar/structured state รวมผลหลัง mutation
- JxPath ใช้ DOM/JDOM ที่มี namespace, attributes และ children จริง Text/processing-instruction methods ใช้ node ชนิดนั้นจริง ส่วน iterator/sort ใช้ parent และ anchor ที่เป็นลูกของ receiver
- ตัด constructor-only, identity/type-only oracle และ methods/types ที่ยังไม่มี recipe ตาม capability policy ก่อน observe ไม่คัด target จากผล buggy และไม่แก้ assertions หลัง evaluation
- เก็บ development v1/v2 และ suite/observations เดิมไว้ใน `superseded-development/` รอบ v3 เป็น condition ใหม่หลังแก้ preconditions ไม่ใช่การ repair ผล primary

**Strict policy นี้ยังรองรับและ review เฉพาะ TypeInference กับ DOM/JDOMNodePointer ของสอง bugs ข้างต้น** อีก 18 bugs ยังใช้ผล declaration discovery เดิม ไม่เปลี่ยน label ว่าผ่าน explicit fixture review และไม่ควรเปิด policy นี้ทั่วทั้ง 20 bugs จนกว่าจะมี recipe/capability ที่ตรวจรับร่วมกัน

## Eligibility ที่ builder ออมใช้ได้

ส่ง [eligibility import](evidence/beam-aom-eligibility-import-20261003/index.json) เป็น `<Project>-<bug>/eligibility.json` ครบ 20 bugs พร้อม fixed source hashes, source-bound provenance และ excluded declarations ตาม schema ที่ออมขอ มี shared declarations 691 และ fixed-only exclusions 3; fixture policy label คง `common-fixed-buggy-production-types-v1` เป็น historical discovery scope, semantic approval ยัง pending

ตรวจ bytes ของหลักฐานรอบก่อนครบก่อนแปลง และใช้ builder ของ `aom` commit `32b8378fbf15648771c81034a3f8931087f382b6` แบบแยก directory รับ 20 inventories/691 declarations ได้จริงบน Linux filesystem โดยไม่ merge หรือแทนที่ runtime ออม ดู [builder receipt](evidence/beam-fixture-review-20261003/aom-builder-import-receipt.json) ตัว builder ใช้ copytree/copystat จึงทดสอบบน Linux filesystem เพื่อหลีกเลี่ยงข้อจำกัด metadata ของ Windows mount

ตัวเลข prompt 113,583 UTF-8 bytes ใน receipt เป็นเฉพาะ builder เดิมและ historical declarations ไม่ใช่ขนาด prompt ใหม่หลังเพิ่ม Chart receiver/explicit recipes และไม่ใช่ token count หรือ provider overhead

## การเชื่อมกับฝั่ง AI และ runtime ออม

Callable resolver: `scripts.study.api854.champ_bridge:champ_suite_resolver`; suite policy `beam-java-suite-v1` ใช้ archive `suite.tar.bz2` ตาม contract เดิมและตรวจ suite hash ก่อนส่ง evaluator

เพิ่ม proposed prompt policy `beam-fixed-targets-explicit-fixtures-v3-junit4-proposal` สำหรับ strict fixtures ต้องให้ `fixture_policy_id` ใน protocol และ generation ตรงกัน Prepare ส่ง `fixture-recipes.json` ซึ่งเก็บ source bytes ของ `algorithms/java/SqaProbe.java` กับ `scripts/study/api854/fixture_policy.py` พร้อม hashes ที่ตรง frozen source map และแนบ knowledge เดียวกันใน prompt ทั้งสี่ approaches แยก recipe support จาก production fixed source ไม่มี execution feedback

Metadata เพิ่ม `fixture_policy_id`, `fixture_recipes_sha256` และ targets inventory hash; recipe ต้องเป็น artifact ของ prepared attempt เดียวกัน ฝั่ง bridge ตรวจ exact UTF-8 bytes/source hashes และ capability inventory ก่อน provider send การนำ prepare เก่ามาใช้กับ policy ใหม่ถูกปฏิเสธก่อน claim generation/API call

แก้ชื่อ artifact ของ selected targets ให้ publish เป็น `targets.json` ตาม consumer contract โดยคัดลอก bytes เดิมและตรวจ hash ไม่แก้เนื้อหา `targets.fixture-policy.json`

ยังต้อง compose การเปลี่ยนเหล่านี้กับ runtime/all-owner guards ล่าสุดของออม รวม Chart additional receiver และ re-pin source hashes ใน shared proposal ไม่ควรแทน worker ออมทั้งไฟล์หรือใช้ source map เดิมเปิด generation CPU execution proof รอบนี้ bind กับ protocol ที่เก็บไว้; prompt/recipe/publication guards ที่เพิ่มภายหลังตรวจด้วย regression tests แยกกันและยังไม่ได้ตรวจรับ shared primary composition

## หลักฐานและ validation

- [Execution/review index](evidence/beam-fixture-review-20261003/index.json), [checksums](evidence/beam-fixture-review-20261003/checksums.json): 221 files ใน checksum inventory พร้อม original suites/results, stage logs/counters, coverage, separate semantic reviews และ runtime implementation snapshot ตรวจ source hashes ตรง development protocol
- [Eligibility checksums](evidence/beam-aom-eligibility-import-20261003/checksums.json): 21 files ใน inventory (รวม index และ 20 eligibility files)
- `evidence-path-map.json` ในแต่ละ suite map paths ใน immutable original reviews ไปยัง public packet; `command.log` เก็บเป็น `command.txt` โดย bytes เดิม
- Unit/integration tests ผ่าน **178 API854 + 31 legacy/Java = 209**, ไม่มี skip รวม actual loopback queue, mocked provider, stale-prepare rejection, recipe tamper rejection และ canonical targets byte preservation
- **0 real KKU requests; 0 live queue mutations** ในงานรอบนี้

## งานที่ยังต้องปิดก่อน live pilot

1. ออมตรวจรับ eligibility import และร่วมกำหนด shared fixture/context/prompt version รวม Chart receiver และ runtime guards; re-pin implementation ตาม source bytes จริง
2. เพิ่ม/ตรวจ recipe และ meaningful oracles สำหรับส่วนที่เหลือของ 20 bugs แล้วตรวจ prospective suites และ Gate A ตามแผน ไม่ใช้ผลสี่ suites นี้แทนการตรวจครบทั้ง pilot
3. วัด prompt/token reserve/overhead ใหม่จาก prompt สุดท้าย ให้แชมป์ยืนยัน settings/quota/expiry ของ `claude-sonnet-5` และ `gemini-3.5-flash-lite`
4. ตรวจรับ shared resolver → adapters → evaluator กับ queue ร่วมกันก่อนให้ออม freeze primary และเปิด live stages

ยังไม่ต้องส่ง KKU API key เพื่อทำ offline fixture/integration work นี้ เมื่อถึงขั้นต้องเรียก KKU จริงจะแจ้งบีมก่อนตามที่ตกลง
