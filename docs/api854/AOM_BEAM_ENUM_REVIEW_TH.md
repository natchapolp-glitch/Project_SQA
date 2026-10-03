# ตรวจข้อเสนอ enum targets ของ Beam สำหรับการพิจารณาร่วม

ตรวจหลักฐานที่ commit `532baa317cd0c6895a0f1d7a3b1ca9f5544132f3` วันที่ 3 ต.ค. 2569
ผลนี้เป็น independent technical review เพื่อให้ออม บีม และแชมป์พิจารณาร่วมกัน
ไม่ได้บันทึกแทนการลงความเห็นของเจ้าของงานทั้งสาม และยังไม่มี joint semantic approval

## ข้อเท็จจริงที่ยืนยัน

Fixed `FromXmlParser.java` บรรทัด 31–65 ประกาศ `Feature` โดยไม่มี enum constant
พารามิเตอร์ชนิด `Feature` จึงมี legal non-null domain ว่างใน production API รุ่นนี้
SHA-256 source คือ `8c3b885b733a0b42b2ab10e92c2a3ed86e83e888cbf71a058372d638f311a549`
ตรงกับ readiness receipt และ fresh fixed-source comparison ใน retained sweep record

พบ exactly 4 signatures ในข้อเสนอและ capability exclusions ของ v7:

| Signature | Fixed source lines | Raw probe |
| --- | --- | --- |
| `FromXmlParser.configure(Feature,boolean)` | 229–235 | `JacksonXml-1/case-009.json` |
| `FromXmlParser.disable(Feature)` | 220–223 | `JacksonXml-1/case-010.json` |
| `FromXmlParser.enable(Feature)` | 215–218 | `JacksonXml-1/case-011.json` |
| `FromXmlParser.isEnabled(Feature)` | 225–227 | `JacksonXml-1/case-037.json` |

ทั้งสี่รายการยังเป็น `unsupported` และ `oracle_approved=false`
Raw files ผ่านการตรวจ SHA-256 กับ sweep checksums ทั้งสี่ไฟล์
แต่ละรายการมี fixed observations สองครั้งที่ซ้ำกัน รวม 8 attempts:
`exception:java.lang.NullPointerException` และ `target_invoked=true`

## ขอบเขตของหลักฐาน diagnostic

Beam helper SHA-256 `da4f29d28a90619b6dc627177dde4cfa7a237a8ad782816d4bc16183dd154625`
ตรงกับ runtime binding ใน readiness receipt ตอนตรวจแบบ read-only
บรรทัด 811–814 map enum ที่ไม่มี constant เป็น null
บรรทัด 973–974 ตั้ง invocation marker ก่อน `method.invoke`
และบรรทัด 983–987 แปลง `InvocationTargetException` เป็น exception observation
Receiver-constructor failures ของ method cases ถูกแยกเป็น fixture failure ที่บรรทัด 961–965
จึงมีหลักฐานวินิจฉัยของ target exception สำหรับ null boundary

Marker นี้ไม่ใช่ target-entry coverage receipt และ observation เมื่อเกิด exception
ไม่เก็บ parser state ก่อน/หลัง จึงยังรับรอง state oracle ไม่ได้
Midpoint vector ของ `configure` map boolean เป็น true; ยังไม่มี false case ใน raw packet นี้
ผลซ้ำสองรอบจึงไม่อนุมัติ null oracle และไม่ยืนยัน normal-domain meaningful execution

## ข้อเสนอ prospective boundary สำหรับตัดสินร่วม

คง denominator 691 และ signatures ทั้งสี่ ระบุข้อจำกัด `empty_non_null_parameter_domain`
แยกจาก recipe ที่พัฒนาไม่เสร็จ และคงทั้งสี่ใน unsupported ของ preparation รุ่นใหม่
การรับ setter หรือ JDOM recipe ไม่ได้แก้ข้อจำกัด enum นี้

ถ้าทีมตกลงนับ invalid-input boundary coverage ให้ freeze policy และ suite ก่อนรัน
โดยใช้กรณี `configure(null,true)`, `configure(null,false)`, `enable(null)`,
`disable(null)`, `isEnabled(null)` กับ fresh real parser receiver และ valid StAX input
ตรวจว่า setup สำเร็จก่อนเรียก target จับ exception จาก target แยกจาก fixture error
และตรวจ unchanged `getFormatFeatures`, closed/current-token/text ก่อนและหลัง exception
Source สนับสนุน NPE เป็น candidate oracle แต่ยังต้องได้รับ joint approval

เก็บ fixed สองรอบพร้อม source/helper/policy/suite hashes และ execution counts
เก็บ target-entry coverage ของทั้งสี่ methods และสอง configure branches
ผูกกับเงื่อนไข execution ที่อนุมัติ ใช้ policy และ accounting เท่ากันในทุก study method
รายงาน invalid-input boundary แยกจาก normal-domain semantic coverage
หาก requirement บังคับ normal-domain meaningful execution ทุก signature
ให้ทีมและผู้กำหนด requirement ตัดสินข้อจำกัด empty domain ตามจริง
ไม่ลบ targets ลด denominator สร้าง enum ปลอม หรือใช้ setup exception แทน target exception

## สถานะและหลักฐานที่ผูกไว้

Joint decision ของออม บีม และแชมป์ยัง pending; Beam ได้บันทึก proposal แล้ว
Review นี้ไม่ได้เปลี่ยน runtime ไม่ได้รัน suite เพิ่ม ไม่ส่ง live requests หรือแก้ queue
ไม่ใช่ primary results ไม่ปิด 314 unsupported declarations เดิม และ Gate A ยังไม่ผ่าน

[Machine-readable review และ exact SHA-256 ของหลักฐาน](../../output/api854-20261003/aom-beam-enum-review-v1.json)
ผูก source, proposal, readiness/handoff/Gate A, raw sweep index/record/checksums,
fixed-sweep script, compile command และ v7 target/exclusion files รวมถึง raw enum cases ทั้งสี่

- [Beam enum proposal](BEAM_V7_ENUM_DECISION_TH.md)
- [Beam readiness](evidence/beam-v7-readiness-20261003/readiness.json)
- [Raw JacksonXml fixed record](evidence/beam-v7-fixed-sweep-20261003/JacksonXml-1/record.json)
- [Fixed source](../../output/api854-20261003/prepare-v7-twenty-bug-development/JacksonXml-1/fixed-source/src/main/java/com/fasterxml/jackson/dataformat/xml/deser/FromXmlParser.java)
