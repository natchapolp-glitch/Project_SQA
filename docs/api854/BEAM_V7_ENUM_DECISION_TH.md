# ข้อเสนอของบีม: empty enum domain ของ JacksonXml-1

ตรวจ fixed production source ของ shared preparation v7 วันที่ 3 ต.ค. 2569
ข้อเสนอนี้ยังไม่ใช่คำตัดสินร่วม ไม่ลด requirement 691 และไม่เปลี่ยน capability policy

## ข้อเท็จจริงที่ตรวจได้

`FromXmlParser.java` บรรทัด 35–38 ประกาศ `Feature implements FormatFeature`
แล้วตามด้วย `;` โดยไม่มี enum constant และ source comment ระบุว่าเป็น placeholder
`enable`, `disable`, `isEnabled` บรรทัด 215–226 เรียก `f.getMask()` โดยตรง
`configure` บรรทัด 229–235 ส่ง `f` ต่อให้ enable/disable
ดังนั้น source ไม่มี legal non-null enum instance สำหรับพารามิเตอร์ของสี่ methods นี้
การสร้าง instance ปลอมจะเปลี่ยน input domain จาก production API

Source/signatures/SHA-256 ที่ตรวจอยู่ใน
[readiness receipt](evidence/beam-v7-readiness-20261003/readiness.json)
และ immutable source อยู่ใน
[FromXmlParser.java](../../output/api854-20261003/prepare-v7-twenty-bug-development/JacksonXml-1/fixed-source/src/main/java/com/fasterxml/jackson/dataformat/xml/deser/FromXmlParser.java)

## ข้อเสนอให้ทีมตรวจร่วม

คง signatures ทั้งสี่ไว้ใน denominator 691 และระบุสถานะ
`empty_non_null_parameter_domain` แยกจาก recipe ที่ยังพัฒนาไม่เสร็จ
ถ้าทีมตกลงทดสอบ null boundary ให้ใช้ real parser receiver ที่พร้อมใช้งาน
แล้วทดสอบ configure(null,true/false), enable(null), disable(null), isEnabled(null)
ตรวจการเข้า method จริง, exception class และ parser state ก่อน/หลัง
แสดงเป็น invalid-input boundary coverage แยกจาก normal-domain semantic coverage
ไม่ใช้ exception จาก receiver setup แทน target exception

การที่ source dereference null ไม่ใช่หลักฐานว่าทีมอนุมัติ NPE เป็น oracle
ต้องเก็บ prospective policy ที่ทั้งสี่วิธีได้รับเท่ากัน และ fixed repeated execution
ก่อนรับรอง boundary cases หาก rubric ต้อง normal-domain meaningful execution
ของทุก signature ให้บันทึกข้อจำกัดนี้ตามจริงเพื่อให้ทีม/ผู้กำหนด requirement ตัดสิน
การไม่สามารถสร้าง non-null enum ไม่ควรถูกแก้ด้วยการลบ targets หรือสร้าง enum ปลอม

## ส่วนที่ยัง pending

Fixed-only diagnostic sweep เรียกทั้งสี่ signatures แล้วสองครั้งด้วย vector เดียวกัน
ผลเป็น `exception:java.lang.NullPointerException` และ `target_invoked=true` ทุกกรณี
เป็นผลจาก boundary ที่ probe ส่ง null ไม่ใช่การสร้าง legal non-null Feature
ไม่อนุมัติ oracle หรือเปลี่ยน denominator จาก observation นี้
Raw observations อยู่ใต้ `evidence/beam-v7-fixed-sweep-20261003/JacksonXml-1/case-*.json`

- Joint decision ของแชมป์/ออม/บีมเรื่องความหมายของ coverage สำหรับ empty domain
- Approved boundary oracle และ condition-bound execution evidence
- Shared recipe/protocol รุ่นใหม่หลังคำตัดสิน; v7 ยังเป็น development proposal

เอกสารนี้ไม่ได้อนุมัติ live generation, Gate A หรือ primary results
