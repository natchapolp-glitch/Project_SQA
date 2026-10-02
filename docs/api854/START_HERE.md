# SQA API854 — เริ่มจากแผนที่ยืนยันแล้ว

## ข้อมูลปัจจุบัน

- เริ่ม: 3 ตุลาคม 2569 เวลา 05:00 ประเทศไทย
- ส่ง: 5 ตุลาคม 2569 เวลา 05:00 ประเทศไทย (48 ชั่วโมง)
- เป้าหมาย: 854 active bugs × CMA-ES/FSCS-ART/KKU Claude Sonnet/KKU Gemini Flash Lite = 3,416 งาน รวมความล้มเหลวจากการทดลองจริงพร้อมหลักฐาน
- โมเดลที่ผู้ใช้เลือกล่าสุด: `claude-sonnet-5` และ `gemini-3.5-flash-lite` ตาม [model-selection.json](../../experiments/configs/api854-20261003/model-selection.json) แทน Haiku ที่ระบุในแผนก่อนหน้า ต้องรวมคู่นี้เข้า frozen protocol ก่อน seed pilot; ห้าม fallback เป็น `claude-sonnet-5.5`
- ทรัพยากรที่ผู้ใช้ยืนยัน: 10 บัญชี API-ready; บีม 3 เครื่อง แชมป์ 1 เครื่อง ออม 2 เครื่อง
- ต้องแจ้งผู้ใช้ก่อนสลับบัญชีเมื่อ quota หมด; ห้ามส่ง compile/test logs กลับ AI
- งานไม่เคยส่ง/รันเพราะ quota/deadline ต้องคง not_attempted ไม่แต่งเป็น failure

## อ่านตามลำดับ

1. [แผนล่าสุดและเงื่อนไขที่ยืนยัน](../superpowers/plans/2026-10-03-sqa854-collaborative-48h.md)
2. [รายการ bugs ของแชมป์ บีม ออม](BUG_OWNERSHIP_20261003_TH.md)
3. [Ownership สำหรับระบบ](../../experiments/configs/api854-20261003/ownership.json)
4. [ข้อมูลเงื่อนไขที่ผู้ใช้ยืนยัน](../../experiments/configs/api854-20261003/confirmed-plan-constraints.json)
5. [API reference และ file map เดิม](../superpowers/plans/2026-10-02-sqa-api854-three-person-parallel.md) ใช้เฉพาะส่วนที่ไม่ขัดแผนล่าสุด
6. [API worker ของแชมป์และสิ่งที่ออม/บีมต้องส่งก่อน pilot](API_WORKER_HANDOFF_TH.md)
7. [หลักฐาน model IDs/คิวใหม่ และ settings/quota ที่ยังรอยืนยัน](CHAMP_PROVIDER_ACCEPTANCE_TH.md)

## เริ่มทำพร้อมกัน

- แชมป์/branch champ: API/model mapping/quota/notification และ fixed context
- บีม/branch beam: adapter readiness/environment/algorithm/evaluation
- ออม/branch aom: protocol/queue/schema/inventory และร่างรายงานตั้งแต่ต้น

ตกลง schema → ตรวจ pipeline/adapters → pilot20bugs → ตรวจ token/throughput/validity → ขยายตามความสามารถจริง เมื่อมี blocker ให้ช่วยกันก่อนปล่อย family ที่ติดกลับคิว

กำหนด cutoff: ส่ง AI ใหม่ถึง 5 ต.ค. 01:00, drain และ freeze 03:00, ตรวจแพ็กและส่งถึง 05:00

เอกสารนี้เป็น handoff และ plan ไม่ใช่หลักฐานว่า API runner ถูกสร้างหรือเริ่มรันแล้ว ไม่มีการตั้งเวลารันอัตโนมัติจากการเขียนไฟล์นี้
