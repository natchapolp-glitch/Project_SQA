# ประเด็นที่ทีมต้องตัดสินก่อน pilot — ไม่ใช่คำอนุมัติ

คำรับ scoped v10 ของ Beam `0ca73ee6` และ Champ `1b0bcbce` ปิดแล้ว;
Beam `f708595d` ยืนยันว่าผลและ pins ของ Champ `fd2e16ab` ตรงกัน.
ไม่ต้องขอคำรับส่วนเดิมซ้ำ แต่คำรับนั้นผูก Aom `a4880fb2` / v10 เท่านั้น.
ไม่โอนเป็นการรับ runtime/preparation/เครื่องผู้รันของ v11 หรือ v12.

## สิ่งที่ยังตกลงไม่ได้จากข้อความส่งมอบ

Gate A ปัจจุบันต้องครบ denominator **691 declarations**. ชุด development v12
มี 403 selected / 288 exclusions และยังไม่ได้รับรอง meaningful semantic coverage
ครบ 403 หรือ full legal domains. จำนวน selected ไม่ใช่จำนวน bugs ที่ตรวจพบ.
Enum ทั้ง 4 ยังคงอยู่ใน denominator; null-boundary/method-entry ไม่ใช่คำรับ full enum domain.
ทีมต้องตกลงร่วมกันก่อนเปลี่ยนเกณฑ์ ไม่ให้การปิด scoped receipts เปลี่ยน Gate A อัตโนมัติ.

ให้บีมและแชมป์ส่งข้อเสนอ/คำตัดสินพร้อมออมสำหรับทางเลือกต่อไปนี้:

1. **คง requirement เดิมสำหรับ primary pilot:** ตรวจ exclusions/semantic requirement
   ให้ครบตาม protocol เดิม และแนบหลักฐานของแต่ละ declaration/fixture/oracle.
2. **เสนอการทดลอง development ที่มีขอบเขตแยก:** ระบุวัตถุประสงค์ รายชื่อ bugs/
   exact targets/domains ที่รับได้ จำนวน runs/budget/stop rules และหลักฐานขั้นต่ำ
   ให้ครบทั้งสามคนรับก่อน. เก็บ exclusions/denominator และผลเดิมตามจริง;
   อย่าเรียกว่าผ่าน primary Gate A หรือครบ 691. หากเลือกทางนี้ต้องออก condition/
   policy/checker ใหม่ที่ตรวจสอบได้ก่อนเปิด ไม่ใช่แก้ flag ใน proposal ปัจจุบัน.

เอกสารนี้ยังไม่เลือกทางใด และยังไม่แก้ Gate A checker.

## หลักฐานที่ต้องผูกกับ condition เดียวกัน

- exact protocol/preparation index/runner/runtime/prompts hashes; consumers ทั้ง 4
  ใช้ context/targets/recipes เดียวกัน. รักษา test cap 30 และ reject ทั้ง suite เมื่อเกิน.
- bounded semantic receipt ของบีมและแชมป์ตามขอบเขตที่ตกลง พร้อม fixed สองรอบ/
  buggy/coverage โดยแยก reference suites, algorithm results และ method entry ออกจากกัน.
- owner-host bindings: Champ API coordinator สำหรับ generation ของทุก owner;
  Beam CPU1 สำหรับงานบีม; Aom CPU1 สำหรับงานออมและแชมป์ ตาม runner-plan.
  ต้องตรวจ host/dependencies/lock/lease/stage routing ของรุ่นใหม่. Local Java proofs
  หรือ scoped Beam-host v10 เดิมไม่ใช่คำรับ final host ของ v12.
- provider evidence ปัจจุบัน: exact model IDs, effective temperature/output cap,
  context/output limits, token counting และ framing overhead, quota remaining/
  bucket/reset/expiry พร้อม timestamp/evidence. ใช้ worksheet 40 คู่ของ condition สุดท้าย.
  UTF-8 bytes และ historical floor ไม่ใช่ final token reserve.
- ทั้งสามคนตรวจหลักฐานและลงคำรับ Gate/pilot ตาม policy ที่ตกลง; จึง freeze bytes,
  ให้แชมป์ตรวจ reserve ที่ผูก bytes สุดท้าย แล้วจึงพิจารณาเปิดคิว generation.

Credentials อยู่ใน private ignored files ตามเดิม; งานรับ keys ที่พักไว้ยังไม่ถูกเปิดเอง.
ไม่มีคำขอ KKU, live queue mutation, quota ledger mutation หรือ primary result เพิ่มจากชุดนี้.
