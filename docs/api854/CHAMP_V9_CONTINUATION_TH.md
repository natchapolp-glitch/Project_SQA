# งานต่อจาก shared development v9 บน champ

ทำงานใน `D:\Projects\SQA_p\Project_SQA` ต่อจาก checkpoint `7756c46a`.
ส่ง checkpoint ที่รวมและทดสอบแล้วขึ้น remote `champ` และลบ remote
`codex/champ-v7-intake` หลังตรวจว่า commits ทั้งหมดอยู่ใน `champ` แล้ว.
ไม่มี branch `codex/champ`; ไม่เปลี่ยน branches ของออมหรือบีม.
[Receipt ที่เชื่อม current worklist กับหลักฐานทั้งสี่ enum targets](../../output/api854-20261003/aom-champ-v9-continuation-receipt-v2.json).

## Worklist ปัจจุบัน

จัดรายการ unsupported จาก preparation v9 ใหม่ โดยตรวจ received Beam evidence,
shared-input hashes และจับคู่ทุก signature กับ diagnostic v7 ตาม identity จริง.
ไม่มีการนำ observation เก่ามาอ้างว่าเป็นการรันด้วย runtime v9.
[Worklist 311 รายการพร้อม raw-case hashes และงานถัดไป](../../output/api854-20261003/aom-champ-v9-readiness-worklist-v1.json)
และ [สคริปต์ตรวจ/สร้างใหม่](../../scripts/study/api854/build_v9_readiness_worklist.py).

| Owner | Unsupported | Historical fixture errors | Normal observations รอ oracle review | Target exceptions รอ review |
|---|---:|---:|---:|---:|
| ออม | 53 | 31 | 17 | 5 |
| บีม | 91 | 33 | 53 | 5 |
| แชมป์ | 167 | 60 | 76 | 31 |
| รวม | 311 | 124 | 146 | 41 |

Selected ยัง 380 และ denominator ยัง 691. Delta จาก v7 คือ setter หนึ่งและ Math สอง
signatures ที่เป็น prospective structural capability ไม่ถือว่าปิด 314 declarations เดิม.
สี่ empty-enum targets อยู่ในงานบีมและยังเป็น unsupported.

กลุ่ม fixture ที่ควรเริ่มตรวจจากข้อมูลเก่า: `JXPathContext` 13 targets ของบีม,
`Graphics2D` 7 และ `Chronology` 6 ของแชมป์, `StringBuffer` 4 ของออม.
จำนวนนี้เป็น affected signatures ไม่ใช่จำนวนที่รับรองว่าจะปิดได้ด้วย recipe เดียว.
แต่ละ signature ยังต้องมี preconditions, meaningful assertions, repeated execution และ target coverage.

## เติมหลักฐานพัฒนาของ empty enum

สร้างและ seal policy/suite ก่อนรัน โดยไม่แก้ `SqaProbe`, shared fixture policy หรือ preparation.
ใช้ exact retained fixed `FromXmlParser.java` SHA-256
`8c3b885b733a0b42b2ab10e92c2a3ed86e83e888cbf71a058372d638f311a549`,
ตรวจเทียบกับ fixed mirror revision `2d7683ed820116b77cba9b4b290cd7ce7dfa5cf4`.
Dependencies และ generated PackageVersion มี hashes อยู่ใน packet; ไม่มี downloads.

ทดสอบ `configure(null,true)`, `configure(null,false)`, `enable(null)`, `disable(null)`
และ `isEnabled(null)` กับ fresh real parser/valid StAX XML ทุกรายการ.
ชนิด null ผูกเป็น `FromXmlParser.Feature` ชัดเจนเพื่อไม่เรียก inherited `JsonParser.Feature` overload.
Production enum มี constants = 0. Fixed สองรอบและ tracing run ผ่านรอบละ 5 cases;
executed = target_checks = 5, skipped = fixture_errors = 0.

ทุกกรณีเกิด NPE จาก target/delegation ที่ตรวจ stack แล้ว ไม่ใช่จาก receiver setup.
`getFormatFeatures`, closed state, current token และ text คงเดิมหลัง exception;
parser อ่าน field/value ถัดไป (`other`/`beta`) ได้.
Temporary mutation ที่เปลี่ยน features ก่อนโยน NPE ถูก state oracle ตรวจจับได้.
นี่เป็นการตรวจความไวของ candidate oracle ไม่ใช่ production bug detection.

JVM JDI MethodEntry events จาก bytecode ที่ไม่ถูกแก้ยืนยัน exact descriptors:

| Active case | Methods ที่เข้า | Source entry lines |
|---|---|---|
| configure true | configure → enable | 230 → 216 |
| configure false | configure → disable | 230 → 221 |
| enable | enable | 216 |
| disable | disable | 221 |
| isEnabled | isEnabled | 226 |

เป็น method-entry/delegation evidence; ไม่อ้าง line/branch coverage percentage.
ไม่มี buggy evaluation หรือ Defects4J experiment rerun ใน packet นี้.
[Receipt](../../output/api854-20261003/enum-boundary-development-v3/receipt.json),
[checksums](../../output/api854-20261003/enum-boundary-development-v3/checksums.json),
[policy ที่ seal ก่อนรัน](../../output/api854-20261003/enum-boundary-development-v3/policy.json),
[trace/raw observations](../../output/api854-20261003/enum-boundary-development-v3/method_entry_trace.stdout.log)
และ [verifier](../../scripts/study/api854/verify_enum_boundary_development.py).

เก็บ attempts v1/v2 ที่ไม่ผ่านไว้พร้อม source/logs/checksums:
v1 หยุดที่การเทียบ canonical temporary path บน Windows;
v2 compile ไม่ผ่านเพราะ untyped null กำกวมระหว่าง overloads.
ไม่ย้ายผล failures เหล่านี้ไปนับเป็น target execution.

## สถานะการรับรองและ verification

ผ่าน focused tests ใหม่ 8 รายการ ไม่มี skip/failure/error:
ตรวจ partition overlap/identity drift, exact descriptor, configure false delegation,
NPE พร้อม state change, setup/skipped counters ที่ห้ามรับผิด และการ rerun ที่ต้องปฏิเสธ
existing sealed output โดยไม่เขียนไฟล์เพิ่ม. Verifier ที่รัน proof เก็บ source snapshot ใน packet;
verifier ปัจจุบันเพิ่ม guard นี้ภายหลังและผูก hashes แยกไว้ใน receipt v2.
[ผลและ hashes](../../output/api854-20261003/aom-champ-v9-continuation-tests-v2.json)
และ [raw log](../../output/api854-20261003/aom-champ-v9-continuation-tests-v2.log).
Full 351-test integration ของ checkpoint ก่อนหน้าเป็นหลักฐานรุ่นเดิม ไม่อ้างว่ารัน full suite ใหม่รอบนี้.

หลักฐานใหม่นี้เติม false case/state/actual-entry ที่ขาดจาก diagnostics เดิมได้
แต่ไม่อนุมัติ NPE เป็น boundary oracle แทนทีม และไม่รับรอง normal non-null domain coverage.
Joint decision ของทั้งสาม owners, condition-bound accounting ที่ใช้เท่ากันทั้งสี่ approaches
และ semantic acceptance ยัง pending. สี่ targets ยังอยู่ใน unsupported และ denominator เดิม.

Runtime 41 files, shared preparation และ protocol/runner v9 คง hashes เดิม.
Gate A ยัง false, primary results added = 0; ไม่มี API request, live queue mutation หรือ quota ledger import.
Request floor ยัง `263010 + H`; final reserve, provider limits/framing และ current quota ยัง unknown.
ใช้ [คู่ integrated v9 และ reserve audit](AOM_CHAMP_V9_INTEGRATION_TH.md) ต่อไป.
