Next Aom agent: [continuation handoff — v12 baseline and jointly accepted Codec packet](AOM_NEXT_AGENT_HANDOFF_TH.md). Codec shared integration has not started; live/Gate A remain closed.

Latest Aom composition: [shared Graphics2D v12 development inputs](AOM_GRAPHICS_V12_HANDOFF_TH.md). 20 bugs; 403 selected / 288 exclusions of 691. Current proof v5; v11 preserved. Historical v10 scoped acceptance closed; final semantic/host/provider/team gate decision pending, pilot closed.

Latest Aom composition: [shared Chronology v11 development inputs](AOM_CHRONOLOGY_V11_HANDOFF_TH.md). 20 bugs; 396 selected / 295 exclusions of 691. Use preparation/protocol suffix v3; final semantic/host/provider review pending, Gate A/pilot closed.

Latest Aom composition: [jointly accepted v10 shared development inputs](AOM_JOINT_V10_HANDOFF_TH.md). 20 bugs; 390 selected / 301 exclusions of 691. Same inputs for all four approaches, final semantic/host/reserve review pending; Gate A/pilot closed.

Latest Aom scoped review: [Beam 2e11c7d9 Buffer/Csv verdict and 42-case reference received](AOM_BEAM_BUFFER_VERDICT_ACCEPTANCE_TH.md). Independent oracles/checksums verified; 11 fresh tests passed. Champ verdict required before new shared composition; historical v8 unchanged.

Latest Aom receipt: [Beam 24a38184 Math-only v8 preparation/host evidence accepted for development review](AOM_BEAM_V8_RECEIVED_ACCEPTANCE_TH.md). 809 checksums verified; Aom reran 4 tests across 80 consumer combinations. Final composition/joint verdict/Gate A remain pending.

Latest Aom intake: [Beam 22982e8c scoped Lang source/oracle review and joint-verdict tasks](AOM_BEAM_LANG_INTAKE_TH.md). 281 checksums verified; historical shared v8 retained. Lang and buffer decisions remain separate; Gate A/pilot closed.

Latest Aom intake: [Beam c125695a buffer/runtime review and exact tasks for Champ/Beam](AOM_BEAM_BUFFER_INTAKE_TH.md). 193 checksums verified; shared buffer composition awaits joint acceptance. Historical v7 retained; Gate A/pilot closed.

Latest Aom waiting work: [host/input readiness, recovered prepare-only queue and exact tasks for Beam/Champ](AOM_V8_WAITING_WORK_TH.md). Shared v8 unchanged; Gate A/pilot remain closed.

Latest Aom handoff: [shared v8 with the two accepted Math getField signatures](AOM_MATH_FIELD_V8_HANDOFF_TH.md). 20 bugs; 379 selected / 312 unsupported of 691; max prompt 257,515 bytes. Champ remeasures reserve; Beam and Champ continue joint review. Gate A/pilot remain closed. Earlier checkpoints below retain their original counts and evidence.

Latest Champ candidate decision: [accept two Math getField candidates for shared composition](CHAMP_MATH_FIELD_ACCEPTANCE_TH.md). Current v7 counts and Gate A remain unchanged.

Latest Champ joint-review intake: [Beam 0b560f05 / Math field proof and shared-input consistency](CHAMP_BEAM0B560_JOINT_INTAKE_TH.md). Final shared recipes and reserve remain pending.

Latest Champ evidence intake: [Beam 532baa31 diagnostics and oracle development](CHAMP_BEAM532_ACCEPTANCE_TH.md). Final recipes/preparation and Gate A remain pending.

Latest Champ v7 intake: [fresh runtime bindings, offline audit and reserve worksheet](CHAMP_V7_INTAKE_TH.md). Gate A remains closed; account intake remains paused. Previous checkpoints below retain their original evidence.

# SQA API854 — เริ่มจากแผนที่ยืนยันแล้ว

**Integration รุ่นก่อน:** [แชมป์รับ Aom b11b379a / build และวัด prompt v6 ใหม่หลังรวม](CHAMP_AOMB11_V6_INTEGRATION_TH.md)
ร่างครบ 20 bugs แต่ selected 377/691; ยังรออีก 314 declarations และ semantic/shared acceptance
รับงานบีมรอบแก้จาก champ 7e09a5fe และ integration e5d4a66b แล้ว; งานรับ keys ยังพักไว้
Checkpoint ที่รับจากออม: [shared development v6](AOM_CHAMP8E_V6_DEVELOPMENT_TH.md)

**Integration ก่อนหน้า:** [แชมป์รับ Aom 4a0e699c และตรวจ recipe ↔ protocol runtime](CHAMP_AOM4A_INTEGRATION_TH.md)
คู่ shared-v5 เดิมยัง 5/20 bugs และ 124/691 declarations; runtime pins เก่าถูก blocked เมื่อเทียบกับโค้ดที่รวมล่าสุด
งานรับ keys พักไว้ตามคำขอผู้ใช้; limits/framing/bucket/reset/expiry และ final 20-bug reserve ยัง pending

**Checker ที่รับจากออม:** [ออมรับ b6051367 และแก้ selected Gate A inputs](AOM_GATE_A_B605_ACCEPTANCE_TH.md)
**Integration ล่าสุดของออม:** [รับ Champ 7e09a5fe / repair audit และ shared development v7 ครบ 20 inputs](AOM_CHAMP7E_V7_REPAIR_ACCEPTANCE_TH.md)
ตรวจ combined 15 bugs / 30 local-valid suites; candidate ยังรองรับ subset 377/691 declarations
มี worklist อีก 314 รายการ; max prompt 250,315 bytes; offline tests ผ่าน 302 ข้าม 1; ยังไม่เปิด Gate A/pilot

**ประวัติ checker:** [ออมรับ b6051367 และแก้ selected Gate A inputs](AOM_GATE_A_B605_ACCEPTANCE_TH.md)
การใช้ gate_a ต้องระบุ --protocol และ --runner; worksheet/preflight ไม่ใช่การอนุมัติเปิด pilot

**Historical provider receipt:** [Champ 19ef7ae6 / KKU preflight](AOM_CHAMP19_PREFLIGHT_ACCEPTANCE_TH.md)
runner proposal ปัจจุบันใช้ champ-pc1, beam-pc1 และ aom-pc1; ยังไม่เปิด primary/Gate A
รายการทรัพยากรและข้อมูลไม่ยืนยันในแผนแรกด้านล่างให้เทียบกับ checkpoint ที่ตรวจรับรุ่นล่าสุดก่อนใช้งาน

## ข้อมูลปัจจุบัน

- เริ่ม: 3 ตุลาคม 2569 เวลา 05:00 ประเทศไทย
- ส่ง: 5 ตุลาคม 2569 เวลา 05:00 ประเทศไทย (48 ชั่วโมง)
- เป้าหมาย: 854 active bugs × CMA-ES/FSCS-ART/KKU Claude Sonnet/KKU Gemini Flash Lite = 3,416 งาน รวมความล้มเหลวจากการทดลองจริงพร้อมหลักฐาน
- โมเดลที่ผู้ใช้เลือกล่าสุด: `claude-sonnet-5` และ `gemini-3.5-flash-lite` ตาม [model-selection.json](../../experiments/configs/api854-20261003/model-selection.json) แทน Haiku ที่ระบุในแผนก่อนหน้า ต้องรวมคู่นี้เข้า frozen protocol ก่อน seed pilot; ห้าม fallback เป็น `claude-sonnet-5.5`
- Runner proposal ล่าสุด: champ-pc1 เป็น API coordinator, beam-pc1 CPU 1 slot และ aom-pc1 CPU 1 slot ตาม [v5 runner plan](../../output/api854-20261003/aom-continuation-v5/runner-plan.json); ยังรอตรวจรับร่วมทีม
- บัญชีที่มี actual KKU preflight ในหลักฐานปัจจุบัน: a01; อีกบัญชีจากแผน 10 บัญชียังต้องตรวจ credentials/assignment/observed quota ก่อนใช้ ไม่ถือว่าโควตารวมพร้อมแล้ว
- ต้องแจ้งผู้ใช้ก่อนสลับบัญชีเมื่อ quota หมด; ห้ามส่ง compile/test logs กลับ AI
- งานไม่เคยส่ง/รันเพราะ quota/deadline ต้องคง not_attempted ไม่แต่งเป็น failure

## อ่านตามลำดับ

เริ่มจาก [แชมป์รับ Aom b11b379a](CHAMP_AOMB11_V6_INTEGRATION_TH.md)
Checkpoint ก่อนหน้า: [แชมป์ตรวจ integration Aom 4a0e699c](CHAMP_AOM4A_INTEGRATION_TH.md)
สถานะบีมที่รับก่อนหน้า: [แชมป์รับ Beam 3ae6f2fb: รอบแก้และขั้นตอนรับบัญชีที่พักไว้](CHAMP_BEAM_REPAIR_INTAKE_TH.md)
และ [ขั้นตอน/template รับบัญชีแบบส่วนตัว](CHAMP_ACCOUNT_INTAKE_TH.md)
ผลรอบแรก: [แชมป์ตรวจ Beam 68b81b0e](CHAMP_BEAM68_ACCEPTANCE_TH.md)
และ [checkpoint สถานะบีมเดิม / รายการเทียบ 691 declarations](CHAMP_BEAM_V5_PROGRESS_TH.md)
และ [ผลตรวจ shared-v5 และ KKU preflight ที่รับจากไฟล์แล้ว](CHAMP_V5_KKU_ACCEPTANCE_TH.md)
และ [ตาราง 20 bugs / งานที่ยังรอ / reserve worksheet](CHAMP_V5_WAITING_WORK_TH.md)
เอกสารแผน v1/v3 ด้านล่างใช้เป็นประวัติ; protocol/runner pair ที่จะใช้ต้องตรงกับ final shared input condition

0. [คิวกลางที่เปิดและวิธีเชื่อมต่อของแชมป์/บีม](QUEUE_CONNECTION_TH.md) — worker token รับผ่านช่องทางส่วนตัว
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

## งานออมที่เตรียมแล้ว — 3 ตุลาคม 2569

**อัปเดต:** [ตรึง core protocol และเปิดคิว preparation pilot80](PILOT_OPENING_TH.md)
ใช้ controller `/v1/...` ของ QUEUE_CONNECTION_TH.md; generation/evaluation ยังถูก gate ไว้
นี่ไม่ใช่ frozen primary model protocol หรือหลักฐานว่า Gate A/B ผ่าน

ตรวจ installed Defects4J 3.0.1 ตรง 854 bugs/17 projects และสร้าง 3,416 job keys ไม่ซ้ำแล้ว
งานออม 284 bugs/1,136 keys; คิวทั้งชุดยัง held/not_attempted และไม่มี live requests
Protocol เป็น draft; Gate A/B ยังไม่ผ่าน ต้องใช้หลักฐานจากแชมป์/บีมและตรวจรับทั้งสามคน

- [Protocol และ fairness](PROTOCOL_TH.md)
- [Queue/schema contract สำหรับ workers](QUEUE_CONTRACT_TH.md)
- [คำสั่งใช้งานสำหรับออม](AOM_RUNBOOK_TH.md)
- [ความคืบหน้าและงานที่รอทีม](AOM_PROGRESS_TH.md)
- [ร่างรายงาน](REPORT_TH.md)
- [รายการหลักฐาน](EVIDENCE_INDEX.md)

ตรวจรับ worker/tunnel ล่าสุด: [AOM_ACCEPTANCE_TH.md](AOM_ACCEPTANCE_TH.md)

Prepare pilot 20 bugs ของออม: [AOM_PREPARE_HANDOFF_TH.md](AOM_PREPARE_HANDOFF_TH.md) — artifacts พร้อมตรวจรับ ยังไม่เปิด generation

ฝั่งแชมป์รับ integration `8fcec539` แล้ว ตรวจ 116 tests และ hashes ของ artifacts
20 bugs / 162 entries ผ่าน; [หลักฐานตรวจรับปัจจุบัน](CHAMP_PROVIDER_ACCEPTANCE_TH.md).
Model/settings/quota และ adapters/evaluator ยังต้องตรวจให้ครบก่อน live generation.

ตรวจรับ branch และ composition ล่าสุด: [AOM_TEAM_INTEGRATION_TH.md](AOM_TEAM_INTEGRATION_TH.md) — ยังรอ Gate A/settings/quota

งานออมสี่ส่วนล่าสุด: [AOM_PREPARE_V2_TH.md](AOM_PREPARE_V2_TH.md) — prepare v2/policy ร่วม/runner ทุก owner/checklist Gate A

ตรวจส่งมอบบีม `44dd5cb0`: [AOM_BEAM_HANDOFF_REVIEW_TH.md](AOM_BEAM_HANDOFF_REVIEW_TH.md) —
รับหลักฐาน discovery ครบ 20 bugs/691 declarations และ exclusions 3 รายการแล้ว;
context/prompt supplement ยังต้องรวมกับ runtime ออม และ fixture/oracle/Gate A ยัง pending.

ตรวจรับ integration แชมป์ `d134cde6`: [AOM_CHAMP_INTEGRATION_REVIEW_TH.md](AOM_CHAMP_INTEGRATION_REVIEW_TH.md) —
shared prepare v2 + callable resolver + runner guards รวมแล้ว; receiver/context รุ่นใหม่และ Gate A ยัง pending.

ชุดส่งมอบใหม่ล่าสุด: [AOM_PREPARE_V3_HANDOFF_TH.md](AOM_PREPARE_V3_HANDOFF_TH.md) —
import discovery ครบ 20 bugs/691 targets แล้ว พร้อม receiver partition/shared fixtures,
CPU/API binding และงานต่อสำหรับแชมป์/บีม. Primary/provider/semantic/host Gate A ยัง pending.

รับ fixture รอบใหม่บีม `3ff1a6a4`: [AOM_BEAM_FIXTURE_REVIEW_TH.md](AOM_BEAM_FIXTURE_REVIEW_TH.md) —
four development suites ของ Closure-176/JxPath-1 และ eligibility import ตรง v3 ครบ 20 bugs;
shared recipe development v4 compose แล้วเฉพาะสอง bugs. อีก 18 recipes/team acceptance/API evidence ยัง pending.
