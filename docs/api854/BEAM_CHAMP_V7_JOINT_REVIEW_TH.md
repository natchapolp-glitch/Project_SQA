# บีมรับ worklist แชมป์ 169 declarations และตรวจเทียบ v7

วันที่ 3 ต.ค. 2569 รับเอกสาร CHAMP_V7_INTAKE_TH.md จากผู้ใช้และ worklist
ใน local Champ checkpoint `11a00be09e84aac942955255c9e56ebba98d9126` (เอกสารต้นทางอ้าง intake เดิม `ebacc21b`)
ตอนตรวจ `origin/champ` ยังเป็น `2ac55e31`; ไม่อ้างว่า checkpoint นี้ push แล้ว
สำเนาเอกสารเป็นข้อมูลส่งมอบ ไม่ได้ใช้คำสั่งในเอกสารเปิด API หรือเปลี่ยน protocol

## สิ่งที่ตรวจรับแล้ว

- ตรวจ worklist/audit/index/protocol links ด้วย SHA-256 ก่อนใช้; worklist มี 169 signatures ไม่ซ้ำ
- ตรวจ checksums ของ Champ fresh preparation 243 entries / 20 bugs
- prompt/context/fixture recipe/targets/capability exclusions และ fixed production/build bytes
  ตรงกับ immutable v7 ที่บีมใช้ครบ 20 bugs จึงผูกคำวินิจฉัยเดิมกับ signatures ได้
- Runtime ต่างที่ `scripts/study/api854/common.py`: Champ เพิ่ม Windows extended-path normalization
  ใน containment guard. บันทึกความต่างไว้ ไม่ย้ายผลเดิมไปเป็น experiment ของ runtime หลังรวม
- เก็บ source declaration candidates และ prospective review actions ของทุก signature
  เป็นที่เริ่มอ่าน declaration/body/overload ไม่ใช่การรับรอง preconditions หรือ oracle ครบแล้ว

ผลนี้คือ **intake/consistency/diagnostic review**; ไม่ใช่ semantic acceptance ของ 169 รายการ
diagnostics เดิมเป็นสอง probe attempts ที่ midpoint เดียว ไม่ใช่ coverage ของ input domain

## ผลเทียบกับหลักฐานเดิม

- Chart-1: 48 ค้าง = 14 normal observations + 34 fixture errors
- Compress-1: 11 ค้าง = 6 normal observations + 5 target exceptions
- JacksonCore-1: 26 ค้าง = 16 normal observations + 8 target exceptions + 2 fixture errors
- JacksonDatabind-112: 8 ค้าง = 2 normal observations + 6 fixture errors
- Math-1: 34 ค้าง = 31 normal observations + 1 target exception + 2 fixture errors
- Time-1: 42 ค้าง = 7 normal observations + 17 target exceptions + 18 fixture errors

รวม 169 = **76 normal observations + 31 target exceptions + 62 fixture errors**
normal output ที่ซ้ำได้ยังต้องตรวจว่า assertion ตรวจ state จริง ไม่ใช่ null/void/object identity
target exception ยังต้องตรวจ legal preconditions และแยก valid invalid-input boundary
fixture_error ไม่ยืนยันว่า method ถูกเรียกสำเร็จ แม้บางกรณีล้มเหลวขณะ projection หลัง invocation
Gson-1 ไม่มี exclusions ใน worklist แต่ยังต้อง joint semantic review ของ selected targets ตามเดิม

## ลำดับร่วมตรวจที่เสนอ

1. Buffer slice recipes: NumberInput/TextBuffer/CPIO ต้อง derive offset/length จาก buffer เดียวกัน
   ใช้ digits/text และ coherent archive entry size; แยก invalid slices ออกจาก normal cases
2. Fraction field return: ตรวจ runtime type, zero=0/1 และ one=1/1 ของ production field
   ไม่ใช้ object identity; บีมทำ isolated development proof สอง targets ใน packet ใหม่
3. Time: อ่าน actual field inventory ก่อนเลือก index; ใช้ chronology UTC และ coherent
   ReadableInstant/ReadablePartial/ReadablePeriod; เปรียบเทียบ values/types ไม่ใช้ bounds exception ปิดยอด
4. Chart: สร้าง annotation/generator/dataset จริงและตรวจ mutation state;
   Graphics2D methods ต้องมี BufferedImage/axes/plot/info ที่สัมพันธ์กันพร้อม geometry/render invariant
5. JacksonDatabind-112: production mapper/parser/context/property/NullValueProvider/TypeDeserializer
   ต้องเป็น graph เดียวกัน; projection ต้องตรวจ content/value-instantiator semantics
6. Constructor/clone/equals/hashCode: ตรวจ structural state และ invariants เฉพาะ API
   ไม่รับรองจากข้อความ constructed หรือ hash ที่ซ้ำเพียงกรณีเดียว

รายละเอียดว่าราย signature อยู่กลุ่มไหน พร้อม source candidates/reason/raw diagnostic SHA
อยู่ใน [joint worklist receipt](evidence/beam-champ-v7-review-20261003/review.json)

## Math development candidate

Fixed source ของ BigFraction/Fraction คืน field singleton; production field factories
มี getRuntimeClass/getZero/getOne. Probe v5 ไม่มี projection ของ field จึงได้ fixture_error
บีมเก็บ red regression ทั้งสอง targets แล้วเพิ่ม projection เฉพาะ helper copy ใน policy
`beam-champ-fraction-field-v6-development` แยกจาก runtime/shared v7

เก็บ factory source bytes จาก fresh fixed checkout และเทียบ Git HEAD ก่อนใช้
หากทีมจะรับ recipe นี้ ต้องส่ง factory sources/knowledge ให้ทั้งสี่วิธีเท่ากันใน shared inputs ใหม่
ไม่ใช้ local candidate เปลี่ยน approved capability counts ของ 169/314 รายการ

รอบแรกของ development runner ล้มเหลวก่อน evaluation เพราะตรวจ `runtime=class:`
แต่ helper เดิมจัด Class เป็น Type และคืน `runtime=type:`. เก็บ round 1 และ hashes ไว้
รอบใหม่แก้เฉพาะ expected prefix ไม่ลดการตรวจ runtime type, zero/one หรือ target invocation
ผลรอบใหม่: fixed สองรอบ, buggy และ coverage มี executed=4, skipped=0, target_checks=4 ทุก stage
getField มี coverage hits=2 ทั้งสอง classes; fault_detected=false
ผล JUnit/coverage รอบใหม่และ verification อยู่ใน packet attempt2

- [Failed development runner attempt](evidence/beam-champ-math-field-20261003/failure.json)
- [Candidate receipt](evidence/beam-champ-math-field-20261003-attempt2/receipt.json)
- [Final joint review verification](evidence/beam-champ-v7-review-20261003/verification.json)

## ส่งต่อทีม

**แชมป์:** ตรวจ method bodies/overloads และ prospective fixtures/oracles ตาม worklist ร่วมกับบีม
เน้น normal-domain preconditions ของ slices/time และ concrete dependencies ของ Chart/deserializer
ตรวจ supplemental field factory knowledge ก่อนส่งเป็น final shared inputs; model/limits/reserve ยังเป็น gate ฝั่งแชมป์

**ออม:** ตรวจรับ packet/hashes; รับ candidate ที่ทีมตกลงแล้ว compose preparation/runtime/source pins ใหม่
ห้าม relabel diagnostics หรือ development suites เป็น primary ของ protocol ใหม่

**บีม:** ตรวจ candidate observations/execution/coverage และช่วย implement recipes ต่อเป็น version แยก
ตามกลุ่มที่ตกลง ไม่เลือก target จาก buggy outcome และไม่ลด requirement 691

Requirement ยัง 691; shared capability counts ยัง 377/314; Gate A=false และ primary completion=0
รอบรับงานนี้ไม่มี KKU request/live queue mutation และไม่ได้เปิด pilot
