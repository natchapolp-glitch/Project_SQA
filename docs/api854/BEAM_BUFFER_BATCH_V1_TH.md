# บีม — งานกลุ่ม buffer/slice รุ่นแรก

วันที่ 3 ตุลาคม 2026; รวม Champ `2ac55e31` และ Aom `c25faa5e`
ใน integration commit `014dcd62` ก่อนทำ prospective development ของบีม
ใช้ checkout แยกจากเครื่องมือ/ไฟล์ที่ฝั่งออมกำลังทำอยู่

## ขอบเขตงานนี้

เพิ่ม fixture policy `beam-explicit-fixtures-v6-buffer-proposal`
เฉพาะ 8 declarations ที่กำหนดไว้ก่อนวัดผล:

| Bug | Class / method | Parameter types |
| --- | --- | --- |
| JacksonCore-1 | NumberInput.inLongRange | char[], int, int, boolean |
| JacksonCore-1 | NumberInput.parseBigDecimal | char[] |
| JacksonCore-1 | NumberInput.parseBigDecimal | char[], int, int |
| JacksonCore-1 | NumberInput.parseInt | char[], int, int |
| JacksonCore-1 | NumberInput.parseLong | char[], int, int |
| JacksonCore-1 | TextBuffer.append | char[], int, int |
| JacksonCore-1 | TextBuffer.append | String, int, int |
| Csv-1 | ExtendedBufferedReader.read | char[], int, int |

ตัวเลขนี้คือ method declarations ของ **2 bugs** ไม่ใช่การทดสอบเพิ่ม 8 bugs
รายการรวมยังเป็น 691 declarations ของ pilot 20 bugs ตาม requirement เดิม

NumberInput ใช้ digit/decimal buffers และ offset/length ที่สัมพันธ์กัน;
parseInt ใช้ 1–9 digits และ parseLong ใช้ 10–18 digits ตาม fixed-source preconditions
TextBuffer เริ่มด้วยข้อความจริง แล้วต่อ legal nonempty slice พร้อม oracle ตรวจ contents/size
Csv ใช้ stream ที่มี newline และ buffer 8 ตัวอักษรที่เติม sentinel `~`:
oracle ตรวจ return count, line number, last character และ buffer รวมตำแหน่งที่ไม่ได้เขียน

นิยามและ selection ของ fixture policies v3/v4/v5 คงเดิม
v6-buffer เป็น input condition ใหม่ รวม stream recipe ใหม่ของ Csv; ไม่ relabel suites เก่า
ทุก unsupported declaration คงอยู่ใน capability ledger ไม่กรองออกจาก denominator

## หลักฐานและสถานะ

[Capability ledger](../../output/api854-20261003/beam-buffer-capabilities-v1.json)
ตรวจ inventory/source bindings ครบ 20 bugs / 691 declarations:
structural selection ของ v5 = 377, prospective v6-buffer = 385, exclusions = 306
ตัวเลข selected ไม่ใช่การรับรอง semantic validity ครบทุก declaration

[Offline test receipt](../../output/api854-20261003/beam-buffer-test-receipt-v1.json)
ตรวจ API854 309 tests: ผ่าน 308, ข้าม Windows symlink 1;
Java probe integration ผ่าน 9 tests และไม่ข้าม
รวม regression ที่รับเข้าจากทั้ง Champ/Aom และตรวจ v5 selection ไม่เปลี่ยน

[Development packet](evidence/beam-buffer-development-20261003-v1/index.json)
มี 4 suites: JacksonCore-1 และ Csv-1 อย่างละ FSCS-ART/CMA-ES
suite ละ 30 tests รวม 120 test methods; fixed-1, fixed-2, buggy และ coverage
ทุก stage มี executed=30, skipped=0, target_checks=30
local semantic review ผ่านทั้ง 4 suites แต่ original measured result คง `usable: false`
และแยก review supplement ต่างหาก; ไม่มีการแก้ suites หรือผลย้อนหลัง
มี [checksums](evidence/beam-buffer-development-20261003-v1/checksums.json) ผูก 191 files

| Bug / approach | Line coverage | Branch coverage | Fault detected |
| --- | --- | --- | --- |
| JacksonCore-1 / FSCS-ART | 160/403 | 66/242 | false |
| JacksonCore-1 / CMA-ES | 157/403 | 76/242 | false |
| Csv-1 / FSCS-ART | 31/37 | 13/26 | false |
| Csv-1 / CMA-ES | 31/37 | 13/26 | false |

ผลชุดนี้ยังไม่พบ fault ของ 2 bugs ดังกล่าว; คงผล false ตามจริง
การผ่าน fixture/oracle validation ไม่แปลว่าจับบั๊กสำเร็จหรือทดสอบ input domain ครบ

[Fixed reference sweep](evidence/beam-buffer-reference-20261003-v1/review.json)
ตรวจครบ 8 declarations ด้วย 4 ตัวอย่างต่อรายการ รวม 32 ตัวอย่าง / 64 fixed observations
ทุกคู่ agree, target invoked และตรงค่าคาดหวังจาก decimal arithmetic,
string concatenation และ bounded stream reads ที่กำหนดก่อนอ่านผล
รวม buffer sentinels; ไม่มี case ที่ไม่ผ่านถูกตัดออก
นี่เป็น bounded fixed-source review เพิ่มเติม ไม่ใช่ primary algorithm suite

[Public packet audit](../../output/api854-20261003/beam-buffer-public-audit-v1.json)
ตรวจ 193 checksums ของทั้งสอง packets พร้อม runtime/test log bindings
และยืนยันว่า declarations ใหม่ทั้ง 8 ถูกเรียกจริงใน sampled suites ด้วย

ไม่มี KKU requests, live queue mutations หรือ primary jobs ในงานนี้
Gate A และ team/primary approval ยัง false

## งานต่อของบีม

1. ทำ fixture/oracle กลุ่มถัดไปจาก exclusions 306 รายการ โดยเริ่ม Lang-1
   (`isAllZeros`, `validateArray`) แล้ว Codec StringBuffer/encoder methods
2. ตรวจ repeated fixed observations, preconditions, meaningful oracles และ execution evidence
   ให้ครบ requirement 691 รายการ; sampled suites อย่างเดียวไม่ยืนยันครบทุก declaration
3. คง 4 empty-enum targets ของ JacksonXml-1 ในรายการ pending:
   fixed enum `FromXmlParser.Feature` ไม่มี legal non-null constants
   ต้องมีคำตัดสินร่วมก่อนรับรอง; ไม่สร้าง enum ปลอมและไม่ลด denominator
4. เมื่อ fixture/runtime รุ่นที่จะใช้จริงพร้อม ให้ตรวจร่วมกับออม/แชมป์
   ก่อนรวม shared preparation/protocol รุ่นเดียวกันและ freeze

## ข้อความส่งต่อให้ทีม

บีมรวม Champ/Aom ล่าสุดและปิด bounded buffer/slice development 8 declarations
ใน 2 bugs โดยคง requirement 691 รายการ ไม่ใช้ subset แทน scope จริง
โปรดคง historical v7 preparation เป็นหลักฐานของ source pins เดิม
runtime เปลี่ยนในรอบนี้จึงต้องสร้าง prospective preparation/prompt measurements
จาก runtime/fixture condition ที่ทีมยืนยันตรงกันก่อน Gate A
ยังไม่เปิด live pilot และไม่ต้องเรียก KKU เพื่อตรวจชุดนี้

สำหรับแชมป์: ตรวจ implementation/recipe bindings และผลพัฒนาใน packet ใหม่
โดยแยก local semantic review จาก team approval; reserve ต้องคำนวณจาก prompt
ของ final condition หลัง preparation พร้อมครบ ไม่ใช้ค่าประวัติแทนโดยตรง
