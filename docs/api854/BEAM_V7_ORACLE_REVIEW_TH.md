# บีม: fixed-source oracle review ที่ทำได้ก่อน API พร้อม

ตรวจ shared preparation v7 และ probe v5 บน branch แยก วันที่ 3 ต.ค. 2569
นี่เป็นข้อค้นพบและ prospective recipe requirements ไม่ใช่การเพิ่ม approved targets

## Metaphone.setMaxCodeLen(int)

Fixed source `Metaphone.java` บรรทัด 397/403 มี getter คืน `maxCodeLen`
และ setter เขียน field โดยตรง แต่ probe `FixtureSession.state()` ของ Codec
ลงท้ายเป็น `stateless-scalars`; การเปรียบเทียบ `void|state=stateless-scalars`
จึงไม่ตรวจว่าการเปลี่ยนค่าเกิดจริง ต้องเพิ่ม getter/state oracle ก่อนรับรอง setter

Prospective receiver: real `Metaphone()`; ใช้ค่า 0, 1, 4 และ 8 พร้อม assert
`getMaxCodeLen()` เท่าค่าที่ส่งหลังเรียก setter และใช้ข้อความ deterministic
ตรวจ encoded length ไม่เกินเพดานที่ตั้ง ไม่ใช้ buggy output เลือกค่า
ค่าลบต้องตรวจ documented/fixed preconditions แยกก่อนนำเป็น expected exception

## Flat3Map: constructor / convertToMap / mapIterator

Fixed source `Flat3Map.java` บรรทัด 367 เปลี่ยน representation เป็น delegate map;
บรรทัด 389 สร้าง delegate และบรรทัด 571 คืน iterator
ผล map projection ช่วยตรวจ entries แต่ไม่รับรอง representation mutation
หรือ iterator key/value traversal จากคำว่าเรียกได้เพียงอย่างเดียว

Prospective fixture: empty map และ map ที่มี 1/2/3 entries; ตรวจ size/get/contains
ก่อนและหลัง convertToMap ให้ข้อมูลเท่ากัน จากนั้นใส่ entry ที่สี่เพื่อตรวจ normal
delegate path; iterator ต้อง traverse key/value จริง, normalize ordering และ restore
receiver ใหม่ทุก case การเรียก constructor ต้องตรวจ empty structural state
แทนเพียงข้อความว่า constructed ห้ามนับ hashCode จนกำหนด equality/hash invariant

## Chart: getters และ removeAnnotations

Diagnostic sweep มี getters ที่เรียกได้แต่คืน null เพราะ receiver ไม่มี generator
state ของ Chart ใน probe v5 บันทึกเพียง row/column counts ของ dataset
จึงไม่ตรวจว่าลบ annotations หรือเปลี่ยน generator สำเร็จจริง

Prospective fixture: concrete annotation/generator จาก production factories
พร้อมค่าที่ต่างกัน ตรวจ getter คืน object/behavior ที่ผูกกับ fixture และ
annotation inventory ก่อน/หลัง removeAnnotations ต้องเปลี่ยนตามที่กำหนด
หลีกเลี่ยงผล object-type อย่างเดียวหรือ getter ที่คืน null ทุก case
drawing methods ต้องมี BufferedImage/Graphics2D และ coherent plot/axes/dataset;
oracle ควรตรวจ state/geometry/pixel invariant ที่กำหนดล่วงหน้า ไม่เดาผลจาก buggy

## Buffer slice methods

NumberInput, TextBuffer, ExtendedBufferedReader และ CPIO write มี overloads
ที่รับ buffer/offset/length. Current generic arguments สร้างแต่ละ argument อิสระ
การเรียกได้หรือเกิด bounds exception จึงไม่รับรอง normal-domain preconditions

Prospective recipe ต้องสร้าง buffer ก่อนแล้ว derive offset/length จากขนาดเดียวกัน:
offset >= 0, length >= 0, offset + length <= buffer.length รวม empty/full/end slice
NumberInput ใช้ numeric character slices; TextBuffer ตรวจ contents/size;
reader ตรวจผล buffer และ reader position; CPIO ต้องกำหนด entry size ให้เท่าข้อมูล
ที่เขียนและตรวจ archive bytes/end-entry โดยแยก invalid bounds cases ต่างหาก

## ขั้นตอนก่อนเพิ่ม approved capability

1. Implement recipes/oracles ใน policy รุ่นใหม่โดยคง v5 และผลเดิม
2. ตรวจ repeated fixed observations ด้วยหลาย vectors และ factory/setup evidence
3. เก็บ JUnit fixed สองรอบ, buggy และ target coverage/counters ของ suites ใหม่
4. ส่ง recipe knowledge เดียวกันให้ทั้งสี่ approaches; compose condition ใหม่
5. ทีมตรวจ shared semantic approval จาก exact version ก่อนเปิด Gate A

หลักฐานราย signature ดู
[fixed sweep](evidence/beam-v7-fixed-sweep-20261003/index.json)
และ [worklist](evidence/beam-v7-readiness-20261003/sweep-summary.json)
