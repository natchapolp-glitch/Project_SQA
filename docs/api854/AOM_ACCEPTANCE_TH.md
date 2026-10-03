# ตรวจรับ API worker ของแชมป์

**Checker รุ่นใหม่:** [ออมรับ b6051367 และแก้ selected Gate A inputs](AOM_GATE_A_B605_ACCEPTANCE_TH.md)

**บันทึกล่าสุด:** [ออมรับ Champ 19ef7ae6 และ KKU preflight](AOM_CHAMP19_PREFLIGHT_ACCEPTANCE_TH.md)
มี request-settings acceptance และ quota snapshot จริงแล้ว ส่วน effective limits/framing/reset/expiry
และ final shared condition ยัง pending เอกสารด้านล่างเป็นประวัติการตรวจรับรุ่นแรก

นำ worker commit 833b97fd พร้อม dependencies มาเชื่อมใน branch aom แล้ว
ตรวจ offline ทั้ง Store ที่แชมป์ pin และ Store ปัจจุบันซึ่งมี stage gate
ไม่ใช้ live API หรือ credentials ใน tests; mock suite resolver ยังไม่ใช่ evaluator ของบีม

คิว local health/schema ผ่าน; tunnel process เดิมหยุดและ DNS URL เดิมล้มเหลว
สร้าง tunnel ใหม่ ตรวจ authenticated HTTPS health/schema ผ่าน URL:
https://angel-keywords-optics-alternatively.trycloudflare.com
private access files ของสามคนอัปเดต URL แล้ว ต้องรับไฟล์ส่วนตัวผ่านช่องทางเดิม
URL ชั่วคราวอาจเปลี่ยนเมื่อ restart; หลักฐาน tunnel-recovery.json ใน output pilot-preflight

Primary protocol.json ยังเป็น proposal และ worker ต้องปฏิเสธก่อน claim
รุ่นที่เลือก claude-sonnet-5 / gemini-3.5-flash-lite ตามข้อมูลแชมป์
temperature 0 / max_tokens 4096 ยังเป็นข้อเสนอ ไม่มี provider/settings verification
รอ observed quota พร้อม expiry/evidence, provider model limits, prompt reserve
และบีมส่ง context/prompt/suite policies, resolver/adapters และ fixed/buggy/coverage evidence
จึงตรึง primary protocol bytes และ seed run ใหม่เมื่อ Gate A ตรวจครบ
core-frozen/preflight run เดิมไม่ถูกแก้ย้อนหลัง เปิดเฉพาะ prepare 80 jobs
ยังไม่เริ่ม live pilot และไม่ถือว่า offline tests ผ่าน Gate B

Validation: python -m unittest discover -s scripts/study/api854/tests -t . ผ่าน 114 tests
