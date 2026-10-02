# คิว pilot — เปิด preparation เท่านั้น

ตรวจจริงวันที่ 3 ต.ค. 2569 เวลา 01:45 ประเทศไทย ผ่านทั้ง localhost และ HTTPS tunnel เดิม
Run ID: `api854-pilot-preflight-20261003-v1`

## ส่วนที่ตรึงแล้ว

ไฟล์ immutable: `experiments/configs/api854-20261003/protocol.core-frozen.json`
SHA-256 ของ bytes ไฟล์: `675a480915c40ab19f7be57b56b046fb8c8924e7330e4cc8956b2ed3de6d31ad`
ตรึง inventory/target policy, seed101, budget30, test cap30, validation/processing policies
และรายการ pilot20 ที่เลือกก่อนเห็นผล ครอบคลุม17projects + Closure/JxPath/JacksonDatabind extras
ยังไม่มี exact model IDs/settings จึงไม่เรียกว่า frozen primary experiment protocol หรือ Gate A ผ่าน

## คิวที่ใช้งานจริง

ใช้ `queue_server.py` + `queue_client.py` และ `/v1/...` ตาม QUEUE_CONNECTION_TH.md
เป็น controller เดียวที่ port8765 กับ tunnel เดิม; เก็บฐานข้อมูล/credentialsเดิมและสำรองก่อน seed
Runtime เดิมอยู่ `Project_SQA_from_test/.local/api854/` ซึ่งถูก ignore
ไม่มีการสร้าง/แจก credentials ใหม่ ไม่มีการส่งข้อความหรือ secrets ให้เพื่อนจากขั้นตอนนี้

คิวมี 20 bugs × 4 approaches = 80 unique jobs ที่ stage=prepare/state=queued
enabled_stages=[prepare]; generate/evaluate รับ claim ไม่ได้ แม้ prepare ส่งผลแล้ว
ตรวจผ่าน authenticated HTTPS และ local; actual attempts=0 และยังไม่ใช้ KKU tokens
หลักฐาน: `output/api854-20261003/pilot-preflight/queue-readiness.json`

Inventory/jobs3416 ใน foundation เป็น planning manifest/held snapshot แยกจาก live preflight80นี้
`service.py` เป็น foundation reference implementation ไม่ใช่ deployed controller ของทีม
ห้ามเริ่ม server อีกตัวบน state.sqlite เดิมหรือรวม SQLite สอง schemas

## ขั้นต่อไปเพื่อเปิด generation

แชมป์ส่ง exact requested API model IDs, actual response model versions, supported temperature/max output,
quota/reset/notification evidence และ fixed-context lineage
บีมส่ง adapter/environment/four-method pipeline และ meaningful assertion/fixed/buggy/coverage evidence
ทีมตรวจรับ Gate A ทั้งสามคน แล้วสร้าง frozen primary protocol ใหม่พร้อม hashes/evidence
Primary protocol ใหม่ต้องมี enabled_stages พร้อม model_settings_verified และ gate_a reviewed_by/evidence
การเปลี่ยน protocol ใช้ condition/runใหม่; preflightไม่ถูกรวมเป็น observed experimental pilot80อย่างเงียบ
เก็บ preparation artifacts และ reuseได้เฉพาะเมื่อ source/target/environment hashesยังตรงและเปิดเผย lineage

คำขอของแชมป์ให้ตรึงและเปิด pilot เป็น authorization ให้ทำขั้นนี้ แต่ไม่ใช่หลักฐานของ exactmodelsหรือผล pipeline
ยังไม่เปิด full3416 และไม่ตั้งค่าที่ทีมยังไม่ยืนยันเพื่อให้ผ่าน gate
