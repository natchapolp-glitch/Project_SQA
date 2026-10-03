# ออมตรวจรับ Beam c125695a — buffer/slice ยังรอ joint acceptance

ตรวจ branch `beam` ที่ `c125695a13620c7d08ba1f344c6ac287e512ea22`
เทียบ `champ` ที่ `da878b54fc6e5ed5b0ec15d6853373346642c86c`
และ Aom base `ff02b94f2d650e0f44b2fe404e39d96fe76c60fc`.
รับสำเนา packets เดิมโดยคง bytes และ original results ไว้ใน branch aom;
ยังไม่รวม runtime ของบีมแทน runtime ปัจจุบัน และยังไม่สร้าง preparation buffer รุ่นใหม่.

## ผลตรวจที่ออมทำแล้ว

- ตรวจ 193 checksum entries ของ development/reference packets ตรงทุกไฟล์;
  runtime 41 pins ตรงทั้ง source ที่ commit บีมและ packaged implementation.
- อ่านหลักฐาน fixed reference 32 ตัวอย่าง / 64 observations ของ 8 declarations:
  fixed สองรอบตรงกัน, target invoked, outcomes ตรง expected ที่บันทึกไว้.
  Fixed Java source hashes ของ JacksonCore-1/Csv-1 ตรงกับ retained sources ใน Champ v9.
- ทั้ง 4 suites มี suite/archive/generated-source hashes ตรง; original `usable=false`
  คงเดิม แยก local semantic review `valid` จาก team approval ซึ่งยัง false.
  fixed-1/fixed-2/buggy/coverage ทั้ง 16 stages มี executed=30, skipped=0,
  target_checks=30, command exit=0, raw all_tests 30 และ failing_tests ว่าง.
  Coverage XML ตรงกับ measured numerators/denominators; fault_detected=false ทั้งสี่.
- ตรวจ exact JVM descriptors และ method entry lines ของ 8 overloads:
  ทุก declaration มี hits เมื่อรวมสอง approaches แต่
  `TextBuffer.append(String,int,int)` มี FSCS-ART hits=2 และ CMA-ES hits=0.
  ห้ามสรุปว่าทุก approach เรียกครบทั้ง 8 หรือครอบคลุม input domain ทั้งหมด.
- รันบน WSL จาก immutable Beam snapshot เพิ่ม **11 tests ผ่าน ไม่มี skip**:
  buffer policy tests 2 และ Java probe integration 9.
  ผลระบบ 308 ผ่าน / 1 skip ของบีมเป็น received evidence ที่ตรวจ log hashes;
  ออมไม่ได้อ้างว่ารัน full suite หรือ Defects4J suites เหล่านั้นซ้ำ.
- ตรวจ historical preparation v7 **264 ไฟล์** ตรงกับ Aom base ทุกไฟล์.
  Shared v8, protocol/runner และ runtime ของออมคงเดิม.

[Receipt และ source bindings](../../output/api854-20261003/aom-beam-c125-intake-v1/audit/receipt.json),
[exact method coverage](../../output/api854-20261003/aom-beam-c125-intake-v1/target-method-coverage.json),
[ผล tests ที่ออมรัน](../../output/api854-20261003/aom-beam-c125-intake-v1/test-command.json)
และ [raw log](../../output/api854-20261003/aom-beam-c125-intake-v1/aom-buffer-focused-tests.log).
[Inspector ที่รันซ้ำได้](../../output/api854-20261003/aom-beam-c125-intake-v1/inspect_received.py)
อ่าน immutable Git objects; ต้อง `git fetch origin` และใช้ output directory ใหม่.

## Runtime / fixture condition ที่ต้องตกลงก่อน composition

บีมใช้ `beam-explicit-fixtures-v6-buffer-proposal` ต่อจาก v5:
377 เดิม + buffer 8 = **385 selected / 306 unsupported** ของ 691 declarations.
Champ มี shared v9 `beam-explicit-fixtures-v7-development` แล้ว:
setter หนึ่ง + Math getField สอง รวม **380 selected / 311 unsupported** พร้อม JDOM projection repair.

บีม v6-buffer ยังไม่มีสาม selected signatures ใหม่นี้ จึงเอา policy/helper มาแทน v9 ตรง ๆ ไม่ได้.
มี runtime 7 ไฟล์ต่างจาก Champ v9 รวม consumer/preparation modules;
การรวมต้องรักษา setter state/getter, JDOM attribute projection, Math field projection/factory sources
และ validation ของ shared inputs ทั้ง CPU/API/evaluator ไว้.
ถ้ารับ buffer ทั้ง 8 โดยไม่มี delta อื่น union จะเป็น **388 / 691**, unsupported **303**.
ตัวเลขนี้เป็นผล set comparison ยังไม่ได้ implement, compose หรือรับรอง semantic completeness.

จากการอ่าน runtime: NumberInput จับคู่ digit/decimal buffer กับ offset=2 และ length ของข้อความ;
parseInt ใช้ 1–9 digits, parseLong ใช้ 10–18 digits; TextBuffer ใช้ initialized receiver
และ nonempty slice ที่อยู่ใน bounds; Csv ใช้ buffer 8 ตัวเติม sentinel และ bounded write.
Csv v6-buffer เปลี่ยน stream เป็นข้อความมี newline สำหรับ selected reader methods เดิมด้วย
ไม่ใช่เฉพาะ overload ใหม่: ต้องระบุเป็น condition change ใน joint acceptance และ prompt ใหม่.
Oracle ต้องตรวจค่าผลลัพธ์/state รวม buffer ตำแหน่งที่ไม่ได้เขียน ไม่รับเพียง type/exception.
การตรวจ source และ bounded examples นี้ยังไม่แทน joint semantic approval หรือ full-domain review.

## ข้อความส่งต่อให้แชมป์

ดึง `aom` แล้วตรวจชุด Beam `c125695a` ที่ออมรับไว้ในเอกสารนี้.
ใช้ [แบบตัดสินรับ 8 รายการ](../../output/api854-20261003/aom-beam-c125-intake-v1/joint-buffer-acceptance.template.json)
ซึ่งผูก exact signatures, source/runtime/protocol hashes และหลักฐาน coverage แล้ว.
บันทึก verdict ของแชมป์ต่อแต่ละรายการ พร้อม preconditions/oracle และหลักฐานที่ใช้;
ตกลงกับบีมเรื่อง Csv stream condition ที่กระทบ selected reader methods เดิมด้วย.
อย่าเปลี่ยนสถานะ template `pending` เป็น approved โดยไม่มี actual review.
Push receipt ลง branch champ พร้อม commit/path/SHA-256 ให้ตรวจรับได้.

หลังออมส่ง shared condition ที่รวมเฉพาะรายการรับแล้ว จึงวัด prompt ทั้ง 20 bugs × 2 models
และ final reserve ใหม่จาก bytes/runtime ของรุ่นนั้น พร้อม settings/limits/framing/current quota/expiry.
`263,010 + H` เป็น floor ประวัติของ Champ v9; ไม่ใช่ final reserve ของ buffer condition.
ยังไม่ส่ง generation API, seed/claim live jobs หรือเปลี่ยน quota ledger เพื่อทำงานรับชุดนี้.

## ข้อความส่งต่อให้บีม

ดึง `aom` และร่วมตัดสิน 8 buffer declarations กับแชมป์จาก packet `c125695a` เดิม.
ผูก verdict กับ exact constructor/method parameter signatures, fixed sources และ helper hash;
ยืนยัน meaningful oracle/stream state ที่จะใช้เหมือนกันทั้ง 4 approaches.
คง original suites/results และ CMA-ES String append coverage=0 ตามจริง;
32 reference examples และ four sampled suites ยังไม่รับรองครบ requirement 691 declarations.
หากเติมหลักฐาน ให้เป็น prospective packet ที่ seal ก่อนรัน พร้อม fixed สองรอบ,
buggy/coverage/counters และ exclusions ไม่เขียนทับ packet เดิม.
ส่ง receipt ร่วมที่ push แล้วพร้อม commit/path/SHA-256.
สี่ empty-enum targets คง denominator และ unsupported ระหว่างรอ joint decision;
Champ enum-boundary diagnostics ไม่ใช่คำอนุมัติ normal-domain oracle.

## งานถัดไปของออม

เมื่อมี joint acceptance ที่ตรวจ bytes ได้ จึงรวมเฉพาะ accepted delta บน shared v9 ที่ตกลงร่วมกัน
เป็น condition ใหม่ ตรวจไม่ลบ recipes ที่รับแล้ว/ไม่เปลี่ยน historical v7/v8/v9,
สร้าง preparation ของ 20 bugs ใหม่ให้ context-manifest/prompt/metadata/recipes ตรงกัน,
ผูก runtime pins/protocol/runner และทดสอบ consumers ทั้ง 4 approaches ก่อนส่ง worksheet ให้แชมป์.
ขณะนี้ยังไม่มี final buffer prompt measurement/reserve; Gate A/pilot ปิด,
primary results เพิ่ม 0, KKU requests 0 และ live queue mutations 0 ในงานนี้.
