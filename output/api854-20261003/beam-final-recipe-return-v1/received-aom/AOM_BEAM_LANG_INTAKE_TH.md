# ออมตรวจ scoped Lang recipes จาก Beam 22982e8c

รับงาน `beam 22982e8c3b63380830cdbbe8d282c6593314cadf` เทียบกับ
`champ e742095d` และ Aom base `ec26350f` หลัง fetch branches ใหม่.
Champ checkpoint ล่าสุดเพิ่ม handoff document ยังไม่มี Lang/buffer joint verdict.
ออมเก็บ packets ที่รับมาเป็นสำเนา bytes เดิม และทำ scoped source/oracle review แล้ว;
การ compose shared preparation/prompts ใหม่ยังรอ verdict ที่ผูกหลักฐานจากแชมป์ร่วมกับบีม.

## ผลตรวจรับ

- **281 checksum entries** ของ sampled packet และ reference attempts v1/v2 ตรงทั้งหมด.
  Runtime 41 pins ตรงกับ commit บีมและ packaged implementation.
- Fixed `NumberUtils.java` SHA-256
  `0374c7b486626927217ad42677e1a57fc60fc551218660316a0e7f4f8ea79477`
  ตรงทั้ง retained reference source และ shared Champ v9 source.
- Sampled suites FSCS-ART/CMA-ES รวม 60 methods: fixed-1/fixed-2/buggy/coverage
  ทั้ง 8 stages executed=30, skipped=0, target_checks=30, command exit=0,
  raw all_tests=30, failing_tests ว่าง. Exact helper overloads มี entry hits ทั้งสอง approaches.
  Coverage XML ตรงผลเดิม: FSCS-ART 117/380 lines, 48/350 branches;
  CMA-ES 137/380 lines, 63/350 branches. ทั้งคู่ fault_detected=false.
- Reference ปัจจุบันมี 12 cases: isAllZeros 8 และ validateArray 4;
  fixed observations 24 ครั้งตรงกันและ target invoked.
  ออมคำนวณ Boolean/exception-message/array-state expectations จาก declared inputs ซ้ำ
  แล้วเทียบ raw observations. Reference fixed-1/fixed-2/buggy/coverage ทุก stage
  executed=12, skipped=0, target_checks=12; exact helpers มี coverage และ fault_detected=false.
- Reference attempt v1 คง `local_development_valid=false` ตาม checker รุ่นนั้น
  แม้ actual measurement complete / fixed_validation=passed_twice.
  Attempt v2 ผ่านหลังแก้ checker; suite hashes ทั้งสองตรงกัน.
  ไม่รวมสอง attempts เป็น 24 independent cases และไม่เปลี่ยนผลเก่า.
- ออมรัน snapshot ของ Beam commit นี้เพิ่ม **14 tests ผ่าน ไม่มี skip**:
  Lang policy/exception regression 3, buffer policy regression 2 และ real Java probe 9.
  Full API854 318 ผ่าน / 2 skip ของบีมเป็น received evidence ที่ตรวจ log hashes;
  ออมไม่ได้รัน full suite หรือ Defects4J experiments รอบนี้ซ้ำ.
- ตรวจ historical shared v8 **266 ไฟล์** ตรง Aom base ทุกไฟล์.
  Main runtime, shared v8 protocol/runner/preparation และ primary state ของออมคงเดิม.

[Intake receipt และ immutable bindings](../../output/api854-20261003/aom-beam-lang-intake-v1/audit/receipt.json),
[ผล tests ที่ออมรัน](../../output/api854-20261003/aom-beam-lang-intake-v1/test-command.json),
[raw log](../../output/api854-20261003/aom-beam-lang-intake-v1/aom-lang-focused-tests.log),
[snapshot provenance](../../output/api854-20261003/aom-beam-lang-intake-v1/test-snapshot-receipt.json)
และ [inspector สำหรับ destination ใหม่](../../output/api854-20261003/aom-beam-lang-intake-v1/inspect_received.py).
Original measured `usable=false` คงเดิม; local semantic `valid` อยู่ใน supplement แยก.

## Scoped source / precondition / oracle review ของออม

รับพิจารณาเฉพาะ Lang-1 `org.apache.commons.lang3.math.NumberUtils`, constructor_types ว่าง,
ทั้งคู่เป็น **private static helpers** ของ retained fixed revision:

1. `isAllZeros(java.lang.String)` descriptor `(Ljava/lang/String;)Z`, fixed source lines 633–642.
   null คืน true, empty คืน false, zero-only nonempty คืน true, text ที่มี nonzero character คืน false.
   Reference ครอบคลุม null, empty, `0`, `000`, `001`, `12`, `00 0`, `-0`.
   Oracle เป็น actual Boolean ไม่ใช่เพียง type. การเรียก private helper ด้วย reflection
   ยังไม่พิสูจน์ input domain/caller preconditions ของ public numeric methods ทั้งหมด.
2. `validateArray(java.lang.Object)` descriptor `(Ljava/lang/Object;)V`, source lines 1078–1083.
   ใช้ null, empty int[], `[0]`, `[-1,0,7]`; ไม่ส่ง non-array Object เป็น legal fixture.
   null/empty ต้องเป็น IllegalArgumentException พร้อมข้อความ exact
   `The Array must not be null` / `Array cannot be empty.` และ input state เดิม.
   Success ต้องเป็น void พร้อม actual array contents; bare void/stateless ไม่พอ.
   สูตรนี้เป็น bounded **int[]** recipe ยังไม่รับรองทุก primitive/reference array type.

New runtime จำกัด exception message/state projection และ expected boundary allowance
ไว้ที่ NumberUtils.validateArray ภายใต้ `beam-explicit-fixtures-v9-buffer-lang-development`.
Regression ยืนยันว่า policy เก่า, method อื่น, message/state เปลี่ยน และ exception ผิดชนิด
ไม่ได้รับ boundary allowance เพิ่มย้อนหลัง.
ออมเห็นว่าสอง recipes เหมาะสำหรับ scoped bounded development composition ตามหลักฐานนี้;
ยังไม่ถือเป็น joint acceptance หรือ semantic certification ครบทุก selected declaration.

## Condition ที่ต้องรักษาเมื่อ compose

ชื่อ `beam-explicit-fixtures-v9-buffer-lang-development` เป็น fixture policy ของบีม
ไม่ใช่ shared preparation v9 ของ Champ. ชุดบีม = v5 + accepted Math 2 + buffer 8 + Lang 2
จึงเป็น **389 selected / 302 unsupported** จาก denominator 691.
Shared Champ v9 มี **380 / 311** พร้อม setter หนึ่ง, Math สอง และ JDOM projection repair.

- รับ **Lang สองรายการอย่างเดียว** เข้ากับ shared v9: prospective union **382 / 309**.
- รับ **buffer แปดและ Lang สอง** เข้ากับ shared v9: prospective union **390 / 301**.

ทั้งสองตัวเลขเป็น exact set comparison ยังไม่ได้ compose/measure หรือรับรอง completeness.
การนำ Beam policy มาใช้ทั้งชุดจะเปิด buffer และเปลี่ยน Csv stream ด้วยโดยอัตโนมัติ;
ห้ามนำไปแทน shared v9 หาก verdict รับเฉพาะ Lang.
ต้องรักษา setter/getter/state, JDOM Attribute projection และ Math field/factory sources ที่รับแล้ว.
ไม่เปลี่ยน policy ID ของ artifacts เก่าเพื่ออ้างว่าเป็น condition ใหม่.

## ส่งให้แชมป์

ดึง origin/aom แล้วใช้ [scoped joint-verdict template](../../output/api854-20261003/aom-beam-lang-intake-v1/joint-lang-acceptance.template.json)
ตรวจสอง exact signatures ด้านบนร่วมกับบีม: fixed-source contract, private-helper scope,
null/empty preconditions, array contents และ rejection class/message/state.
บันทึก verdict ต่อรายการและ hashes ของ receipt/source/helper/observations/suite/coverage;
push receipt จริงพร้อม branch/commit/path/SHA-256 ให้ตรวจ bytes ได้.
ตัดสิน Lang แยกจาก [buffer intake เดิม](AOM_BEAM_BUFFER_INTAKE_TH.md) ได้ ไม่ต้องรอ Codec หรือ exclusions ทั้งหมด.
ถ้ายังไม่มีคำตัดสิน buffer ต้องไม่รับทั้ง Beam combined policy โดยปริยาย.

หลังออมส่ง final accepted condition และ prompts รุ่นเดียวกัน ค่อยวัด reserve ใหม่พร้อม
actual settings/limits/framing/current quota/expiry. ค่าขนาด/floor ของ v8/v9 เดิมเป็น historical;
ยังไม่มี prompt measurement หรือ final reserve ของ Lang condition.

## ส่งให้บีม / งานถัดไปของออม

บีมช่วยยืนยัน scoped verdict และข้อจำกัด int[]/private helpers กับแชมป์โดยผูก packet `22982e8c`;
คง original `usable=false`, fault=false และ reference checker history ตามจริง.
งาน Codec ทำ prospective packet ใหม่ได้ แต่ไม่ต้องผูกการรับ Lang ให้รอ Codec.
สี่ empty-enum targets คง denominator และ pending joint decision.

เมื่อมี joint receipt ที่ตรวจ bytes ได้ ออมจึงรวมเฉพาะ accepted signatures เป็น condition ใหม่,
ตรวจ context-manifest/prompt/metadata/recipes/source pins และ consumers ทั้ง 4 approaches,
สร้าง runner/protocol pair และ worksheet ให้แชมป์จาก destination รุ่นใหม่.
ตอนนี้ Gate A/pilot ปิด; primary added=0, KKU requests=0, live queue mutations=0 ในงานนี้.
