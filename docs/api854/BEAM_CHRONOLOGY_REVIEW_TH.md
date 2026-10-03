# บีมตรวจ Chronology candidate จากแชมป์ 07e68e52

**คำตัดสินบีม:** รับทั้ง 6 exact signatures เฉพาะ bounded candidate oracle development
ตาม policy ที่ seal ไว้ใน `chronology-development-v2`.
ยังไม่ใช่ joint acceptance, shared recipe integration หรือ Gate A.

หลักฐานคำรับและ hashes อยู่ใน
[beam-chronology-verdict.json](../../output/api854-20261003/beam-chronology-review-v1/beam-chronology-verdict.json).
ตัวตรวจอิสระที่รันบนเครื่องบีมอยู่ใน
[review_received.py](../../output/api854-20261003/beam-chronology-review-v1/review_received.py).
เก็บ exact Git blobs จาก `champ 07e68e520db37edffa5ef9fa842202150ec28e04`
พร้อม [provenance](../../output/api854-20261003/beam-chronology-review-v1/received-champ/provenance.json).

## Exact signatures และขอบเขตที่รับ

ทุก target อยู่ใน Time-1 `org.joda.time.Partial`.
JVM descriptors และ worklist identities แบบเต็มอยู่ใน receipt ข้างบน.
Method targets ใช้ default constructor identity แล้ว seed valid fields ก่อนเริ่ม trace;
ไม่ได้เปลี่ยน receiver identity เป็น constructor ที่รับ Chronology.

| Signature | Preconditions / oracle ที่รับ |
|---|---|
| `Partial(Chronology)` | Real ISO offset +07:00 หรือ typed null; ผลเป็น ISO UTC และไม่มี fields |
| `Partial(DateTimeFieldType,int,Chronology)` | hourOfDay=10 ได้ field/value เดิมและ UTC; hour=24 ต้องปฏิเสธด้วย IllegalFieldValueException |
| `Partial(DateTimeFieldType[],int[],Chronology)` | sorted year/month/day และ 2024-02-29; ตรวจ values, types, UTC และ defensive input/getter arrays; Feb 30 ปฏิเสธด้วย IllegalFieldValueException; year/dayOfMonth/era ปฏิเสธด้วย IllegalArgumentException |
| `Partial(Chronology,DateTimeFieldType[],int[])` | package-private; ใช้ UTC และ inputs ที่ public constructor validate แล้วเท่านั้น; รับ field/value projection ไม่อ้างการ validate, normalize หรือ clone ของ internal constructor |
| `getField(int,Chronology)` | protected; default Partial แล้ว seed year=2024; index=0 และ supplied Buddhist +07:00 ให้ field ของ supplied chronology และ epoch year=2513; receiver เดิมไม่เปลี่ยน; index=1 ต้อง ArrayIndexOutOfBoundsException |
| `withChronologyRetainFields(Chronology)` | default Partial แล้ว seed hour=10; real ISO/Buddhist UTC/+07:00 และ typed null; ตรวจ chronology, fields, values และ original state; normalized same ISO คืน object เดิม ส่วนเปลี่ยน chronology คืน object ใหม่ |

Timezone provider ตรึงเป็น UTCProvider และ JVM default เป็น UTC.
รับเฉพาะ UTC/fixed +07:00; ไม่ขยายคำรับไป named zones หรือ timezone-resource coverage.
Protected/internal targets ใช้ package-local Java probe; shared runner ต้องรองรับการเรียก exact target
และแยก setup failure เป็น fixture_error ก่อนนำคำรับนี้ไป integrate.

## หลักฐานที่ตรวจบนเครื่องบีม

- Exact received Git blobs 69 ไฟล์ และ shared input Git pins ของแชมป์ตรวจตรง source commit.
- Checksums ของ v1/v2 รวม 57 entries; compiled production source hashes รวม 314 entries.
  Fixed Partial ผูก exact retained source bytes; mirror EOL comparison ตรงกัน.
- Raw logs, command receipts, preexecution seal, suite hashes และ exact JDI descriptors ตรงกัน.
- Fixed สองรอบและ trace: executed=13, target_checks=13, passed=13, failed=0,
  skipped=0, fixture_errors=0. Buggy สองรอบและ trace: 13/13, passed=12, failed=1,
  skipped=0, fixture_errors=0; failure คือ `arrays_bad_order` และเป็น AssertionError.
- JDI มี first exact target entry ครบ 13 cases / 6 declarations ต่อ revision;
  เป็น entry evidence ไม่ใช่ line/branch coverage percentage.
- Expected field/value projections ตรวจจาก declared inputs และ fixed source contracts;
  leap date ใช้ calendar derivation และ Buddhist epoch year=1970+543.
  Temporary ignored-chronology mutation ล้มเฉพาะ `getfield_buddhist` ตามหลักฐานแชมป์.
- Negative controls 6 แบบถูกปฏิเสธ: skipped, fixture error, weakened Buddhist oracle,
  runtime exception, missing case และ wrong JVM descriptor.
- Runtime ปัจจุบันของบีม 41 ไฟล์ไม่เปลี่ยน; บีมไม่ได้รัน Java/Defects4J เพิ่มในรอบตรวจนี้.
  ผล execution ข้างต้นเป็นหลักฐานที่แชมป์รันและบีมตรวจรับ ไม่ใช่ rerun บนเครื่องบีม.

เก็บ v1 ที่มี checker failure ไว้ครบ: `Independent value/state oracle differs`.
v2 ปรับ exception expectation และ timezone setup หลัง development attempt นั้นแล้ว seal ใหม่.
จึงไม่อ้างว่าชุดนี้เป็น blind primary experiment หรือ full Defects4J evaluation.

## ส่งต่อออมและแชมป์

**แชมป์:** ขอคำรับฝั่งแชมป์ต่อ exact 6 signatures พร้อมยืนยัน package-local/internal invocation,
UTCProvider/offset domain และ exception/identity/state assertions ตาม policy hash ใน Beam receipt.
คำรับต้องระบุว่าเป็น bounded candidate ไม่ใช่ approval ของ full legal domain.

**ออม:** ใช้ Beam receipt นี้ประกอบ joint decision ได้ แล้วจึง implement/bind shared recipe
ของรุ่นที่จะใช้จริง รัน integration และสร้าง preparation ใหม่ครบ 20 bugs.
ต้องเก็บ descriptor/receiver identity/domain/oracle เดียวกันในทุก approach
และให้แชมป์วัด prompt/reserve ของ preparation ใหม่นั้น.

Shared v9 ยังคง selected=380, exclusions=311, denominator=691.
ถ้าเพิ่ม Chronology ทั้ง 6 จาก base นี้เพียงกลุ่มเดียว จะเป็น 386/305 **ในข้อเสนอเท่านั้น**;
อย่าบวกซ้ำกับ recipe groups อื่นหรือเปลี่ยน counts จนตรวจ final union จริง.
ไม่เปลี่ยน runtime/preparation/protocol, ไม่เรียก KKU, ไม่เปลี่ยน live queue,
ไม่เพิ่ม primary results และไม่เปิด Gate A/live pilot.
