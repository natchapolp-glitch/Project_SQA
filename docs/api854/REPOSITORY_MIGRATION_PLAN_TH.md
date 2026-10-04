# Branch Team และแผนจัด repo ใหม่ภายหลัง

คำสั่งผู้ใช้: นำงานล่าสุดจาก aom มาไว้ที่ Team ทำต่อคนเดียว และเมื่อโปรเจคเสร็จ
ค่อยสร้าง repo ใหม่จัดไฟล์ให้เรียบร้อย ยังไม่ได้ขอให้สร้างหรือลบ repo ในรอบนี้

## การย้าย branch ครั้งนี้

- ต้นทาง `aom 026e83edf1e33f1d3f2c8adaf6f8c6b499df250f`
- Team เดิม `170db6d1272d029ea76ccd1e91f12043e35b2474` เป็น ancestor ของต้นทาง
- อัปเดต Team ด้วยประวัติเดียวกัน ไม่ force push ไม่ reset aom และไม่เขียนทับ sealed receipts
- ทำงานใหม่บน Team; ส่วน aom คง checkpoint เดิมไว้สำหรับอ้างอิง
- Offline pilot ปัจจุบัน: `output/round2-solo-pilot-20261004`, 6 algorithm outcomes
  ของ Csv-1/Lang-1/Math-1; AI ยัง pending และไม่มีคำขอใหม่

## เมื่อปิดโปรเจคแล้ว

จัด working snapshot สำหรับ repo ใหม่ตามโครงชุดส่งที่ตกลง: Algorithm1_CMAES,
Algorithm2_FSCSART, AI1_KKU_Claude, AI2_KKU_Gemini, Experiment, Report,
Presentation และ README โดยรวม dependencies/configs ที่จำเป็นต่อคำสั่งรันด้วย
ไม่คัดลอกราก repo ปัจจุบันทั้งหมดโดยอัตโนมัติ

เก็บ code, templates และ prompts ที่ส่งจริง, raw answers, tests, raw evaluation
และ failure records ที่ใช้ในผลรายงาน พร้อม protocol/source hashes และ manifest
แยก diagnostic/ประวัติ/ผลคนละ condition พร้อมคำอธิบาย ไม่ลบผลไม่ผ่านที่ต้องนับ
ไฟล์ที่ยัง pending ต้องแสดงตรงกับรายงาน ไม่เติมผลหรืออ้าง complete จากการจัดโฟลเดอร์

ไม่ใส่ .git เดิม, credentials, private .local, checkout ขนาดใหญ่ หรือ cache/runtime
ที่สร้างใหม่ได้ลงใน working snapshot; repo เดิมยังเป็นคลังประวัติและ provenance
บันทึก source repo/commit และ hashes ของไฟล์ที่ย้ายไว้ด้วย
ก่อนเผยแพร่ ตรวจคำสั่งจากสำเนาใหม่ ลิงก์เอกสาร ตัวเลขรายงาน/สไลด์/ตาราง และไฟล์ส่งครบ

ชื่อ/เจ้าของ/visibility ของ repo ใหม่ยังไม่ได้ระบุ ให้ใช้คำสั่งผู้ใช้เมื่อถึงขั้นสร้างจริง
รอบนี้ทำเฉพาะ branch Team และบันทึกแผน ไม่มี repo ใหม่ที่ถูกสร้างหรือเผยแพร่
