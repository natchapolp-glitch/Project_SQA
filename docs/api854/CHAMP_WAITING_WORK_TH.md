# งานแชมป์ที่เตรียมไว้ระหว่างรอ Gate A

ตรวจ prepare v2 ครบ 20 bugs โดยเทียบ prompt bytes, metadata, policy และ checksums
กับ frozen-core pilot list แล้ว สร้าง [รายงาน capacity](../../output/api854-provider-preflight-20261003/champ-pilot-capacity-v1.json)
และ [รายการ prompt เรียงตามขนาด](../../output/api854-provider-preflight-20261003/champ-pilot-prompts-v1.csv)
ไม่มีการอ่าน credentials, ส่ง HTTP, claim หรือเปลี่ยนสถานะคิวในการเตรียมชุดนี้

## ขนาดและงบที่ต้องพิจารณา

- Pilot มี 80 งานจากสี่วิธี โดย 40 งานเป็น AI: 20 งานต่อโมเดล
- Prompt ปัจจุบันรวมหนึ่งชุดต่อ bug = 907,891 UTF-8 bytes
  ใหญ่สุด Math-1 = 99,439 bytes; ทั้ง 20 bugs ยังมี target_count=0 และยังไม่ eligible
- Worker ปัจจุบันตรวจ `prompt_utf8_bytes <= prompt_token_reserve` ดังนั้น shared reserve
  มี numerical floor 99,439 ตาม guard นี้ **ก่อนเผื่อ provider framing และก่อนเติม targets**
- ถ้ายังใช้ output 4096 ซึ่งเป็นข้อเสนอ: จองต่อ request อย่างน้อย 103,535 หน่วยตาม ledger
  ผลบวกจอง 20 requests ต่อโมเดล = 2,070,700 และสองโมเดล = 4,141,400

ตัวเลขข้างต้นเป็นการคำนวณจาก guard/การจองของ worker ไม่ใช่จำนวน tokens ที่ provider วัดจริง
และไม่ใช่ actual quota consumption ผลบวกของการจองแต่ละ request ไม่ใช่ข้อกำหนดว่าต้องมี quota
ทั้งหมดพร้อมกัน เพราะ worker reconcile การจองกับ usage เมื่อจบแต่ละคำขอ
ยังสรุปจำนวนบัญชีหรือจำนวนวันไม่ได้จนกว่าจะทราบ remaining, bucket sharing, หน่วยและ expiry จริง
ต้องคำนวณใหม่หลังได้รับ target inventory และ prompt สุดท้าย ห้ามลด context โดยไม่ review policy ร่วมกัน

## แบบฟอร์มที่เตรียมให้เจ้าของบัญชี

[provider-evidence.template.json](../../experiments/configs/api854-20261003/provider-evidence.template.json)
เป็นแบบฟอร์มเก็บหลักฐาน ไม่ใช่ protocol หรือไฟล์ import ledger
ให้คัดลอกไป `.local/api854/provider-evidence.private.json` ก่อนกรอก และส่งผ่านช่องทางส่วนตัว
ใช้ alias `a01`; ทำสำเนาเพิ่มเฉพาะบัญชีที่จะใช้จริง ไม่ต้องส่ง keys ทั้งสิบเพื่อเตรียมงานนี้

ต้องเก็บ observed time, actual remaining, unit, bucket ที่แต่ละโมเดลใช้ร่วมกัน, window และ expiry
พร้อมหลักฐานไฟล์/hash แยก quota bucket จริงหนึ่งรายการ ไม่คัดลอกยอดเดียวกันเป็นสองโควตา
ส่วน models เก็บ context/output caps, settings ที่รองรับ, resolved version เมื่อมีหลักฐาน
และ provider framing bound ช่องที่ยังไม่มีหลักฐานคง `null`; ไม่ใส่ API keys หรือ access tokens
แบบฟอร์มนี้ไม่อนุญาต generation probe

## ลำดับงานเมื่อข้อมูลมาถึง

1. บีมส่ง source-bound targets/fixtures ครบชุดและ semantic review; ออม compose preparation รุ่นใหม่
   เก็บ prepare v2 และ core-frozen เดิมไว้ แล้วตรวจ hashes/policies ของรุ่นใหม่
2. แชมป์คำนวณ reserve จาก prompt สุดท้ายพร้อม framing และเทียบ context/output limits จริง
   ลง observed quota ผ่าน `quota_control observe` เมื่อมีหลักฐานจริง และจัดบัญชีตาม bucket/expiry
3. ทีมตรวจ host assignments, shared policy และ Gate A; ออมตรึง protocol/runner hashes และ seed run ใหม่
4. แชมป์ใช้ worker `--check` กับ frozen protocol และ observed ledger ก่อนเริ่มงานตาม stage ที่ทีมเปิด
   ไม่มีการเปลี่ยนบัญชีหรือส่งซ้ำอัตโนมัติ ต้องแจ้งก่อนสลับบัญชีตามระบบเดิม

ตรวจเครื่องแชมป์แบบ offline เพิ่มแล้ว: Python/import resolver พร้อม, `champ-pc1` ได้รับ assignment
สำหรับ AI generation ทั้งสาม owners และ primary proposal ยังถูกปฏิเสธก่อน claim
นี่เป็นการตรวจ imports/routing/protocol guard; live connectivity และการตรวจรับ host ร่วมกันยังค้าง
