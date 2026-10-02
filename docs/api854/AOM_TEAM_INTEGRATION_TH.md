# ออมตรวจ branch beam/champ และเชื่อม worker ต่อ

ตรวจ GitHub วันที่ 3 ต.ค. 2569 และรับ Champ `6ff837c1` กับ Beam `75eda88d`.
Champ ได้ model discovery จริง: `claude-sonnet-5` / `gemini-3.5-flash-lite`,
ตรวจ context artifacts 20 bugs และ core protocol hash แล้ว.
Remaining/bucket/expiry, model-specific settings/context limits และ framing reserve ยังไม่ยืนยัน.

Beam ส่ง resolver, prepare/algorithm/evaluate workers และ fenced artifact publication.
มี checkout/build/shared declaration scans ของ 6 bugs ฝั่ง Beam และ local Lang-4 smoke
fixed/buggy/coverage ครบสี่วิธี. AI สองกรณีใช้ fixtures, usable=false, semantic review pending.
นี่เป็น development evidence ไม่ใช่ live pilot/Gate B และยังไม่ครบ adapter review 20 bugs.

## สิ่งที่ออมทำต่อแล้ว

รวมสอง branch โดยคง queue controller/core protocol bytes เดิม.
รับ UTF-8 byte writes ของ Beam เพื่อให้ prompt/response/source-block hashes ตรงบน Windows.
เชื่อม APIWorker กับ BeamGenerationHandoff ผ่าน explicit `generation.handoff_contract="beam-v1"`.
โหมดนี้ bind resolver ด้วย job/attempt/protocol และส่ง suite, manifest, lineage, source map
พร้อม raw evidence. Evaluator ตรวจ suite hash และ generation attempt แยกจาก evaluation attempt ได้.

ตรวจ prepare mapping ก่อน claim/API: manifest Java files ต้องตรง `fixed_source_sha256`
และ `context_source_hash` ต้องตรง source_hash. ขาด/ผิด mapping จะหยุดก่อนส่ง KKU.
Loader ต้องเห็น frozen processing fields และ requested models ตรงกันก่อนเลือกโหมดนี้.
Draft protocol เพิ่ม fields สำหรับ composition แล้ว; approval/state ยังเป็น proposal.
legacy `champ-v1` ยังรองรับเดิมและไม่ถูกเปลี่ยนการทำงานโดย implicit.

## รายการที่ยังต้องตรวจรับก่อนเปิด generation

- Prepare composition: บีมส่ง evidence bundle/context แต่ยังไม่มี prompt.md แยกตาม CLI.
  ต้องสร้าง version ใหม่ที่มี context-manifest.json และ prompt.md พร้อม metadata ทั้ง
  source_sha256/prompt_sha256/prompt_policy_id และ fixed_source_sha256/context_source_hash.
  ต้องเลือก context policy เดียวกันและตรวจ hashes กับ artifacts จริง; ไม่เปลี่ยน v1 ย้อนหลัง.
- Eligibility/fixture และ meaningful oracle review ครบ 20 bugs; 6 adapter scans ยังไม่ใช่ครบทั้งชุด.
- การจัดผู้รัน: API CLI แชมป์รับ owner=champ และ Beam queue CLI รับ owner=beam.
  ก่อนเปิดงานจริงต้องจัด runner สำหรับ owner=aom และ evaluator งานทุก owner;
  test composition ใช้ raw QueueClient กับ Store mock ไม่ใช่การอนุมัติเปลี่ยน owner ของงาน.
- โควตาจริง/settings/context limits/framing overhead จากเจ้าของบัญชี.
- ตรึง processing policy ร่วมกัน: resolver Beam ปฏิเสธ suite เกิน cap โดยไม่ตัด tests.
  Primary draft ต้องตกลงข้อแตกต่างจาก processing_policy เดิมก่อน freeze.
- Review Gate A สามคนพร้อมหลักฐาน, frozen primary bytes/hash และ run ใหม่.

## คิวที่ตรวจจริง

Receipt: `output/api854-20261003/team-integration-v1/acceptance.json`.
80 jobs ยัง prepare/queued, enabled stage มีเฉพาะ prepare และ core SHA ไม่เปลี่ยน.
การตรวจรอบนี้ไม่มี live KKU, claim/upload/complete หรือ queue mutation.
ยังไม่ถือว่า primary frozen หรือ live pilot เปิดแล้ว.

## Validation

Windows api854: 177 tests, ผ่าน 176 และ skip 1 fixture ที่ต้องใช้ symlink.
WSL api854: 177 tests ผ่านทั้งหมด ไม่มี skip.
Legacy evaluator/generator: 20 tests ผ่าน.
Composition tests ใช้ provider mock และ isolated Store: suite/lineage ดาวน์โหลดได้,
generation/evaluation attempt IDs ต่างกัน, missing source mapping หยุดก่อน claim/API,
และ frozen loader ปฏิเสธ processing contract ที่ไม่ครบ.
ไม่มีผล mock รวมเป็น primary observations.
