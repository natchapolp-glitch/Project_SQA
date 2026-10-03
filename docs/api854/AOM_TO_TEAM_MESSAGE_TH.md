# ข้อความพร้อมส่งต่อจากออม — v8

## ส่งให้บีม

ออม push shared inputs v8 แล้ว (input commit e95e979b): 20 bugs, 379 selected / 312 unsupported / 691 declarations
และตรวจ Math getField สองรายการพร้อม factory knowledge, fixed สองรอบ/buggy/coverage แล้ว
ขอให้ดึง origin/aom และทำตาม [TASK_TO_BEAM_V8_TH.md](TASK_TO_BEAM_V8_TH.md): ตรวจ exact composition/semantic/beam-pc1
แล้วร่วมกับแชมป์ส่ง scoped acceptance ของ setter/JDOM และ candidate ถัดไป โดยแยก target addition จาก recipe repair
ส่ง branch + pushed commit + receipt paths/hashes ตาม templates กลับให้ออม
enum สี่ targets คง denominator และรอคำตัดสินร่วม ยังไม่เปิด Gate A/pilot

## ส่งให้แชมป์

ออมส่ง preparation/protocol/runner v8 แล้ว และทำ input/WSL/CPU-lock/queue preflight ฝั่งออมเพิ่มครบ
ขอให้ดึง origin/aom และทำตาม [TASK_TO_CHAMP_V8_TH.md](TASK_TO_CHAMP_V8_TH.md): ตรวจ 40 คู่ prompt × model
ด้วย model IDs ที่เลือก temperature 0/output 4096 พร้อม actual context/output limits, framing และ observed quota/bucket/reset/expiry
Prompt สูงสุด 257,515 bytes; conservative request floor ใหม่ 261,611 + framing ไม่ใช้ floor v7 เดิม
ร่วมกับบีมตัดสิน setter/JDOM และส่ง final reserve ready/blocked พร้อม branch + pushed commit + receipt paths/hashes
ยังไม่ส่ง generation requests หรือเริ่ม --once; templates เป็น pending แบบฟอร์ม ไม่ใช่ใบอนุมัติ

## คิวใหม่และหลักฐาน

URL: https://fax-stake-salvador-experts.trycloudflare.com
health/schema/status ผ่านแบบ authenticated; คิวคง 80 prepare/queued, 0 attempts ของ frozen core เดิม
ใช้ role access file ส่วนตัวเดิม เปลี่ยน base_url เป็น URL นี้; tokens เดิมยังใช้ได้และไม่ส่ง tokens ผ่าน Git
ยังไม่มี v8 generation queue และไม่เปิด Gate A/pilot
รายละเอียดสิ่งที่ออมทำเพิ่มและ return templates: [AOM_V8_WAITING_WORK_TH.md](AOM_V8_WAITING_WORK_TH.md)
