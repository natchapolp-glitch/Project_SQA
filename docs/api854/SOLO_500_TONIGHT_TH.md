# เป้าส่งคืนนี้: 500 บั๊ก สี่วิธี

คำสั่งผู้ใช้วันที่ 4 ต.ค. 2026 ลดเป้าจาก inventory 854 เป็น **500 บั๊ก × 4 วิธี = 2,000 planned jobs**
หนึ่งรอบสร้างต่อ bug/method เก็บผลผ่านและไม่ผ่าน ไม่ใช้จำนวน JUnit methods แทนจำนวนบั๊ก
เลือก 500 รายการจาก installed inventory ด้วย project-name sorted round robin ก่อนดูผล
ทุก project ทั้ง 17 อยู่ในขอบเขต รายการและ protocol binding อยู่ใน
`docs/api854/plans/solo-500-20261004/selected-scope.json`
354 บั๊กนอกขอบเขตใหม่นี้ยังอยู่ใน inventory และประวัติเดิม ไม่ใช่ pending ในตาราง 2,000 jobs

## งานที่เดินต่อ

1. รักษา pilot Csv/Lang/Math และ compact batch แรกอีก 14 projects ทุก outcome
2. ระหว่างรัน เตรียมรายงาน PDF, PowerPoint 12 slides, demo และหลักฐานในโครงส่งงานทั้งเจ็ดหมวด
3. หลัง batch แรกปิด receipt ครบ ตรวจชุดส่งและซ้อม replay archives ทั้งสี่วิธีโดยไม่เรียก AI
4. ปล่อยคิวขยายอีก 483 บั๊กตามลำดับ scope ใช้ protocol compact เดิมและหนึ่ง CPU slot
5. ตรวจ quota ที่สังเกตล่าสุดก่อนผูก alias ต่อ case ผูก alias ก่อนเรียกจริง
   ไม่เปลี่ยน key ระหว่าง job ไม่ส่งซ้ำเมื่อ outcome ไม่แน่นอน และไม่สร้างใหม่เพื่อให้ผลผ่าน
6. ตัวเก็บรายงานบน Windows อัปเดตจาก outcomes ที่ปิด receipt แล้วทุก 45 วินาที
   เพื่อไม่ให้การอ่านไฟล์ข้ามไดรฟ์ใน WSL ถ่วงการรัน Java หยุดรับบั๊กใหม่และส่ง AI ใหม่อย่างช้าที่ 21:40 น. กรุงเทพฯ
   (cutoff 22:00 น. เผื่อเวลารันและตรวจชุดส่งก่อนเที่ยงคืน)
7. สร้าง snapshot ใหม่จากผลจริง ตรวจ hashes/archives/ZIP/credentials แล้ว push branch `Team`

ตั้ง `finish_tonight.py` เป็น process เบื้องหลังบนเครื่องนี้: เมื่อคิวหยุดหรือเวลา 22:00 น.
จะสร้าง `output/round2-tonight-500-20261004-final` และ
`output/SQA_Round2_500_Target_20261004_FINAL_SNAPSHOT.zip` จาก sealed results เท่านั้น
ตรวจ protocol bytes/source pins/outcome/measurement/archive/CSV และ ZIP CRC ก่อน push `Team`
หาก Git มีงาน staged ของคนอื่นหรือ branch เปลี่ยน จะเก็บชุดส่งในเครื่องและไม่ commit งานนั้น
ไฟล์ที่สร้างภายหลังต้องตรวจหน้ารายงาน/สไลด์ที่ render ล่าสุดก่อนส่ง
ไม่ได้ยืนยันว่า background process จะทำงานต่อได้หลังปิดเครื่องหรือ Windows sleep

การตรวจรอบนี้: unit tests 33 รายการผ่าน และการจำลองแก้ state/coverage/protocol
ถูก verifier ปฏิเสธทั้งสามกรณี โดยไม่แก้หลักฐานเดิมหรือเรียก AI

500 เป็นเป้าที่เลือก ไม่ใช่คำรับรองว่าจะรันครบก่อนส่ง ข้อจำกัดเวลาหรือ quota
ต้องเหลือ PENDING/QUOTA_PAUSED ตามจริง ค่า coverage/fault ที่ไม่มีให้เป็น null ไม่ใช่ศูนย์
ถ้าไม่มี alias ผ่าน admission บั๊กนั้นยังรัน algorithms ได้ แต่ AI ที่ยังไม่ส่งไม่ใช่ model failure
เหตุผล admission เก็บแยกใน `output/round2-solo-500-control-20261004/admissions/`

## เปิดดูผล

- ตาราง: `output/round2-solo-500-control-20261004/Report/data/final_comparison.csv`
- จำนวนรวม: `output/round2-solo-500-control-20261004/Report/data/summary.json`
- คิวขยาย: `output/round2-solo-500-control-20261004/plan.json`
- หลักฐาน algorithm: `output/round2-solo-pilot-20261004`
- AI pilot: `output/round2-solo-ai-pilot-20261004`
- AI compact: `output/round2-solo-ai-nightly-20261004`

เอกสารนี้แทนเป้าเดิมเท่านั้น ไม่แก้ protocol/source pins/ผลเก่า
ผู้ใช้ทำทุกหน้าที่เอง ไม่ต้องรอคำรับจากออม บีม หรือแชมป์
ไม่ใช้ Gate A เก่าเป็นเหตุขวางการทดลอง subset และไม่กล่าวอ้างว่า Gate A ผ่าน

## Resume ที่ผู้ใช้ยืนยันหลังหารือแผนแบ่งเครื่อง

ผู้ใช้เลือกกลับมารันต่อเครื่องเดียวตามแผนเดิม ไม่เปิด shard หรือสุ่ม scope ใหม่
คิวขยายใช้ manifest เดิม 483 บั๊ก โดยเก็บผลเริ่มต้นทั้ง 17 บั๊ก / 68 outcomes
รวม Claude quota pauses เดิมไว้ครบ ไม่ส่งคำขอเดิมซ้ำเพื่อให้ผ่าน
ตรวจ preflight 33 tests และหลักฐาน snapshot ผ่านแล้ว; ซ้อม replay Csv-1
ทั้งสี่วิธีครบ fixed twice / buggy / coverage โดยไม่มีคำขอ AI เพิ่ม

ชุดสำรองที่ตรวจภาพ PDF 10 หน้า / PowerPoint 12 slides แล้ว:
`output/SQA_Round2_500_Target_20261004_CHECKPOINT.zip`
หลักฐานใน `output/round2-tonight-500-20261004-v3` เป็น snapshot ของ 17 บั๊ก
ไม่ใช่ตัวนับสดของคิวที่กำลังเดินต่อ ดู `resumed-by-user.json` สำหรับ process bindings
ตัวคิวหยุดรับบั๊กใหม่และส่ง AI ใหม่ 21:40 และ finalizer ตั้งสร้างชุด final เวลา 22:00 กรุงเทพฯ
คู่ algorithms ของบั๊กที่รับไว้ก่อน guard รันต่อให้จบได้ รวมถึงเริ่มวิธีที่สองหลัง guard
จึงไม่รับรองว่า worker หยุดทุกขั้นตอนตรง 22:00; snapshot รวมเฉพาะ sealed outcomes
งานที่ยังไม่ปิด receipt ณ ตอนรวบรวมไม่ถูกอ้างว่าเสร็จ และเก็บผลภายหลังในเครื่องไว้
มี heartbeat รายชั่วโมงเพื่ออ่านความคืบหน้า/ข้อผิดพลาดและตรวจชุด final
heartbeat ไม่เปิด worker หรือส่งคำขอ KKU เพิ่ม เครื่องต้องเปิดและไม่ sleep
