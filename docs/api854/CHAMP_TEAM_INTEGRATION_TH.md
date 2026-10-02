# แชมป์รับงานบีมและออม — 3 ตุลาคม 2569

สถานะล่าสุดเป็น shared v3 และ Beam `3ff1a6a4`: [CHAMP_V3_ACCEPTANCE_TH.md](CHAMP_V3_ACCEPTANCE_TH.md)
บันทึกด้านล่างเก็บการตรวจ integration รุ่นก่อนหน้าไว้

รับ `beam ce17ce49` และ `aom c6982003` เข้า branch `champ` แล้ว เพื่อให้ทีมตรวจรับโค้ดร่วมกัน
ยังไม่เปิด live pilot และไม่มี KKU generation หรือการเปลี่ยนสถานะคิวจริงในการตรวจครั้งนี้

## จุดเชื่อมที่ตรวจ

- API worker เรียก `scripts.study.api854.champ_bridge:champ_suite_resolver` ตรวจ preparation ก่อน claim
  แล้วผูก original protocol bytes, fixed-source/context hashes, targets และ attempt IDs เข้ากับ suite/lineage
- รองรับ shared preparation `aom-beam-prepare-v2` พร้อม prompt/policy/targets hashes ของออม
  และรักษา runner plan guards ของ `TeamQueueClient`; เส้นทางข้าม owner ต้องได้รับการจัดไว้ใน plan
  หรือระบุใน frozen protocol ตามสัญญาบีม โดยไม่เปลี่ยน owner ของงาน
- เก็บ heartbeat และตรวจ owner ของ claim เดียวกันทั้งการส่ง API และการเผยแพร่ artifact
- สร้าง output root ก่อน resolve path เพื่อให้ concurrent stage creation บน Windows
  ใช้ root ที่มีอยู่แล้ว และยังคงตรวจ containment กับการสร้าง stage เพียงครั้งเดียว
- Regression เพิ่มสองกรณี: eligible shared prepare ส่งถึง evaluator ได้ และ targets ที่ยังว่าง
  ถูกบล็อกก่อน generation claim/provider call ทั้งสองกรณีใช้ isolated local Store และ mock provider

ผลตรวจจากเครื่องแชมป์และ hashes อยู่ใน
[champ-team-integration-v1.json](../../output/api854-provider-preflight-20261003/champ-team-integration-v1.json)
และ [Gate A checklist](../../output/api854-provider-preflight-20261003/champ-team-gate-a-v1.json)

API854: รัน 199 tests ผ่าน 198 และ skip 1 (Windows ไม่มีสิทธิ์สร้าง symlink)
Legacy evaluator/generator: ผ่านอีก 20 tests รวมผ่าน 218 tests ไม่มี failures/errors
ใช้ temp path ใน workspace และ isolated mock queue/provider; ไม่ใช่ผลตรวจรับ live pilot

## หลักฐานที่รับจากบีม

[เอกสาร resolver](BEAM_CHAMP_RESOLVER_TH.md) และ
[Lang-4 smoke](evidence/beam-champ-apiworker-smoke-20261003.json)
ระบุ fixed สองรอบ, buggy และ coverage จริงสำหรับ mock AI ทั้งสองโมเดล
พบ fault และ coverage 12/25 lines, 3/14 branches; `usable=false`, semantic review ยัง pending
นี่เป็นหลักฐานที่บีมส่งมา ไม่ใช่การรัน Defects4J ซ้ำบนเครื่องแชมป์ และไม่ใช่ผลจาก KKU model จริง

## สิ่งที่ยังต้องตรวจรับก่อน live

1. **บีม:** source-bound target/fixture inventories ครบ 20 bugs สำหรับ shared prepare v2 และ semantic/oracle review
   ตอนนี้ prepare v2 ทั้ง 20 bugs ยังระบุ eligibility เป็น false; Lang-4 fixture ไม่แทนการตรวจครบชุด
2. **แชมป์/เจ้าของบัญชี:** observed remaining/bucket/expiry, provider context/output limits,
   settings ที่รับจริง และ prompt reserve พร้อม framing หลักฐาน model discovery มีแล้วแต่ไม่รับรอง settings/quota
3. **ทั้งสามคน:** host readiness, shared policy/runner review และ Gate A จากนั้นให้ออมตรึง primary protocol
   ด้วย implementation hashes ของชุดที่รวมแล้วและ seed run ใหม่; คง frozen-core/preflight เดิมไว้

prepare v2 prompt สูงสุด 99,439 UTF-8 bytes ก่อนเติม target declarations; ไม่ใช่จำนวน tokens
Temperature 0 / output 4096 ยังเป็นข้อเสนอ ต้องตรวจ limits และ reserve ก่อน freeze
ไม่ต้องส่ง API keys ทั้ง 10 บัญชีเพื่อให้การตรวจ integration นี้ผ่าน; เก็บ keys/access ผ่านช่องทางส่วนตัวเท่านั้น

งานเตรียมเพิ่มระหว่างรอ: [ขนาด prompt, งบจองและแบบฟอร์ม quota/settings](CHAMP_WAITING_WORK_TH.md)
