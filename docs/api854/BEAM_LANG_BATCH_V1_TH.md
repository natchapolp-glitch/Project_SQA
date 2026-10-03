# บีม — Lang helper development รอบถัดไป

วันที่ 3 ตุลาคม 2026; ต่อจาก `beam c125695a`
รวม Aom `e95e979b` ใน merge `2b048895` ก่อนพัฒนา Lang
เก็บ factory/projection และคำตัดสินที่รับ Math `getField()` สอง signatures ไว้ครบ
ไม่ได้แก้ preparation/results ของ v7/v8 หรือ buffer รอบเดิมให้เป็นผลรอบใหม่

## งานที่เพิ่ม

เพิ่มเฉพาะ NumberUtils helpers ของ **Lang-1 หนึ่ง bug**:

| Method | Exact parameters | Recipe / oracle |
| --- | --- | --- |
| isAllZeros | String; static / no constructor | null, empty, zero-only, nonzero text; ตรวจ Boolean ตาม fixed-source contract |
| validateArray | Object; static / no constructor | null, empty int[], nonempty int[]; ตรวจ acceptance/array contents และ rejection class/message/input state |

`isAllZeros(null)` คืน true; empty string คืน false ตาม fixed source
`validateArray` มี documented invalid-input boundaries สำหรับ null และ array ว่าง
recipe นี้ไม่ส่ง non-array Object เป็น legal array และไม่รับ fixture setup error เป็น target result
successful validation มี actual array-content oracle แทน bare void/stateless assertion
expected exceptions ยอมรับเฉพาะสอง class/message/state ที่กำหนดไว้และเฉพาะ policy ใหม่
policy เก่าไม่ถูกขยาย expected-exception scope ย้อนหลัง

## Conditions และจำนวนรายการ

Fixture policy ใหม่คือ `beam-explicit-fixtures-v9-buffer-lang-development`
ชื่อนี้เป็น **fixture condition พัฒนา** ไม่ใช่ shared preparation v9 หรือ primary approval

| Condition | Selected | Excluded |
| --- | --- | --- |
| Historical v5 | 377 | 314 |
| Aom fraction-field policy / historical shared v8 | 379 | 312 |
| Beam buffer policy | 385 | 306 |
| Union ของ buffer กับ accepted Math ก่อนเพิ่ม Lang | 387 | 304 |
| Prospective combined Lang condition | 389 | 302 |

ทุกแถวคง denominator 691 declarations ของ 20 pilot bugs
เพิ่มจาก checkpoint บีมเดิมรวม 4 รายการ: Math ที่ทีมรับ 2 และ Lang ใหม่ 2
ไม่ได้เพิ่ม 4 bugs หรือรับรอง semantic coverage ครบ 389 รายการ
constructors/hashCode และ empty-enum targets สี่รายการยังไม่ได้รับเพิ่ม
ตรวจรายการทั้งหมดและเหตุผล exclusions ใน
[capability ledger](../../output/api854-20261003/beam-lang-capabilities-v1.json)

## หลักฐานทดลองจริง

[Sampled development packet](evidence/beam-lang-development-20261003-v1/index.json)
มี FSCS-ART และ CMA-ES อย่างละหนึ่ง suite ของ Lang-1, suite ละ 30 test methods
รวม 60 methods; fixed-1, fixed-2, buggy และ coverage ทุก stage:
executed=30, skipped=0, target_checks=30
local semantic review ผ่านทั้งสอง suites โดยแยก review supplement จาก original results
original results คง `usable: false`; team/primary approval ยัง false

| Suite | Line coverage | Branch coverage | Fault detected |
| --- | --- | --- | --- |
| FSCS-ART | 117/380 | 48/350 | false |
| CMA-ES | 137/380 | 63/350 | false |

[Checksums ของ sampled packet](evidence/beam-lang-development-20261003-v1/checksums.json)
ผูก 119 files; ไม่มี assertion repair หรือการตัดเคสจาก buggy feedback

[Reference receipt รอบที่ตรวจรับ](evidence/beam-lang-reference-20261003-v2/receipt.json)
ประกาศตัวอย่างล่วงหน้า 12 ตัวอย่าง: isAllZeros 8 และ validateArray 4
fixed observations 24 ครั้ง agree, target invoked และตรง independently declared expectations
นำทั้ง 12 cases ไปสร้าง reference JUnit suite แยกจากสองอัลกอริทึม
fixed สองรอบ, buggy และ coverage ทุก stage:
executed=12, skipped=0, target_checks=12; ทั้งสอง helpers มี method coverage
reference suite เป็น development proof ไม่ใช่ primary FSCS-ART/CMA-ES result

[Public audit](../../output/api854-20261003/beam-lang-public-audit-v1.json)
ตรวจ 281 checksums ของ sampled packet และ reference ทั้งสอง attempts พร้อม runtime/test log bindings
helpers ใหม่ทั้งสองถูกเรียกใน sampled suites ด้วย; reference suite SHA-256 ของสอง attempts ตรงกัน

เก็บ [reference attempt แรก](evidence/beam-lang-reference-20261003-v1/receipt.json) ครบ:
actual measurement complete และ fixed_validation=`passed_twice` แต่ summary checker
เปรียบเทียบกับ `passed` จึงรายงาน local_development_valid=false
แก้เฉพาะ checker และรัน destination ใหม่; ไม่แก้ assertions/results/checksums ของรอบแรก
ทั้งสอง reference attempts ยัง fault_detected=false ตามจริง

## Tests และข้อจำกัด

[Offline receipt](../../output/api854-20261003/beam-lang-test-receipt-v1.json)
API854 320 tests: ผ่าน 318, ข้าม 2 (Windows symlink และ Math probe ที่อ้าง retained classes
ของ WSL เครื่องเดิม); Java probe ผ่าน 9 ไม่ข้าม
Lang reference/runtime/evaluator ตรวจจริงบน WSL เครื่องปัจจุบันแยกไว้ข้างต้น

ผลทั้งหมดเป็น bounded development evidence; รอบนี้ยังไม่พบ fault ของ Lang-1
ไม่รับรอง input domain ครบทุกค่า หรือ semantic validity ครบทุก selected declaration
KKU requests=0, live queue mutations=0, primary added=0 และ Gate A=false

## ส่งต่อให้ทีม

**ออม:** รับ scoped Lang signatures/recipes พร้อมหลักฐานชุดนี้ไปตรวจร่วมกับแชมป์
เก็บ shared v8 เดิมเป็น historical condition; runtime เปลี่ยนหลัง merge/เพิ่ม Lang
ต้อง compose preparation และวัด prompts ใน destination รุ่นใหม่จาก fixture condition
ที่ทีมตกลงใช้ตรงกัน ไม่เปลี่ยน fixture_policy_id ของ artifacts เก่าเฉย ๆ

**แชมป์:** ตรวจ exact signatures, preconditions, exception oracle และ immutable evidence
ร่วมกันก่อนรับ Lang เข้าชุด final shared inputs; structural union 389 เป็น proposal
ยังไม่ใช่คำตัดสินรับทั้ง 389 รายการหรือ final reserve
limits/framing/current quota และ reserve ต้องผูกกับ final prompts/preparation รุ่นเดียวกัน

**บีม:** ทำ Codec StringBuffer/encoder helpers ต่อจาก exclusions 302 รายการ
และปิด repeated fixed/semantic/fixture review ให้ครบ requirement 691 ร่วมทีม
four empty-enum targets ยังคง pending ตาม [ข้อเสนอเดิม](BEAM_V7_ENUM_DECISION_TH.md)
ยังไม่เปิด pilot/primary หรือเรียก KKU เพิ่มเพื่อทำงานพัฒนานี้
