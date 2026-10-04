# เตรียมนำเสนอคืนนี้ระหว่างคิวรัน

ทำต่อบนเครื่องเดียวตามคำสั่งล่าสุด เป้า 500 บั๊ก × 4 วิธี
ใช้ข้อมูลจาก ZIP ที่นำเสนอจริง ไม่ใช้จำนวนเป้าหมายแทนผลที่เก็บได้

ชุดสำรองที่ตรวจแล้วอยู่ใน
`output/round2-tonight-500-20261004-v3` และ
`output/SQA_Round2_500_Target_20261004_CHECKPOINT.zip`
มีผล 17 บั๊ก / 68 outcomes; เอกสารและ ZIP นี้คงเดิมขณะคิวรันต่อ
ตัวนับสดใน `output/round2-solo-500-control-20261004/Report/data/summary.json`
อาจมากกว่าชุดสำรอง จึงไม่ใช้ตัวนับสดกล่าวอ้างเนื้อหาของ ZIP สำรอง

## ลำดับที่ใช้พูด

1. บอกโจทย์เปรียบเทียบ CMA-ES, FSCS-ART, Sonnet 5 และ Gemini 3.5 Flash Lite
   บน Defects4J ใช้ seed 101 / algorithm budget 30 และหนึ่ง generation ต่อบั๊กต่อวิธี
2. เปิด Csv-1 เพื่อแสดงทั้งสี่วิธีที่ fixed ผ่านและไม่พบ fault
   แสดง receipt ของการซ้อม suite เดิมครบทั้งสี่วิธี โดยไม่เพิ่ม AI requests
3. เปิด Compress-1/CMA-ES: fixed failures 0/0, buggy failures 3
   จัดเป็นหนึ่งบั๊กที่ตรวจพบ แล้วเปิด Time-1/FSCS-ART: fixed 0/0, buggy 1
4. แสดง Lang-1/Sonnet โค้ดไม่ครบ และ Cli-1 classpath ขาด Hamcrest
   เก็บผลไม่ผ่านตามจริง แยกจาก quota pause และผลยังไม่เริ่ม
5. เปิด coverage/time/token summaries โดยอ่าน denominator จริง
   suites และ oracles ต่างกัน ผลบางส่วนยังไม่พิสูจน์ว่าวิธีหนึ่งเหนือกว่าทุกกรณี
6. เปิดหลักฐาน code, configuration, prompts, raw responses, suites,
   fixed/buggy/coverage logs และ hashes แล้วบอกจำนวนที่ยังค้าง

คำสั่งจากรากชุดสำรอง ใช้ Python ในเครื่องได้ ไม่ต้องใช้ CPU slot ของ Java:

```bash
python Presentation/demo.py --case Csv-1
python Presentation/demo.py --case Compress-1 --method cmaes
python Presentation/demo.py --case Time-1 --method fscs-art
python Presentation/demo.py --case Lang-1 --method kku-claude
```

คู่มือ demo ฉบับปรับปรุงอยู่ที่ `scripts/study/tonight_demo_guide.md`
finalizer จะนำคู่มือนี้ไปใส่ชุด final เมื่อรวบรวมผล ห้ามแก้ ZIP สำรองหรือหลักฐานเก่า
การทดลองหลักยังใช้คิวเดิม ไม่มีการเพิ่ม workers หรือเปลี่ยน model/protocol จากงานเอกสารนี้
