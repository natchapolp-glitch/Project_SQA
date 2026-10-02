# Beam checkpoint — 3 ต.ค. 2569

อ้างอิงแผน 2026-10-03 ไม่ใช่ผลทดลองครบ 854 bugs

สถานะล่าสุดหลังผู้ใช้รีสตาร์ต: WSL2/Ubuntu 24.04, Java/Javac 11 และ Defects4J
v3.0.1 ติดตั้งและ environment ready แล้ว ยืนยัน installed inventory 854 bugs
ตรงกับ allocation ทั้ง 17 projects; Lang-4, Closure-3 และ JxPath-1 adapter preflight ผ่าน ตรวจคิวของ
ออมด้วย Beam token สำเร็จ schema 1.0 มี 0 jobs/0 attempts ตอนตรวจ ไม่มี queue
mutation หรือ KKU API request จากการตรวจนี้

## ทำแล้ว

- กู้ Git index ที่หายด้วย read-tree HEAD ไม่เขียนทับ working files; branch beam
  ยังอ้างอิง e85c4587 และคงการเปลี่ยนแปลงเดิมใน Gradle caches/paths ไว้
- สร้าง local algorithm/evaluation workers รับ job JSON และ protocol/source/suite
  hashes แยก worktrees/attempts ไม่เขียนทับ artifacts ไม่เรียก KKU API
- สร้าง inventory readiness 854 unique bugs/17 projects; บีม 285 bugs/1,140 keys
  และตรวจ installed Defects4J ตรงกันแล้ว ส่วน runtime support ต้อง scan ต่อ bug
  Lang-4, Closure-3 และ JxPath-1 ผ่าน checkout/build/shared discovery และยัง
  pending semantic review ส่วนอีก 851 bugs ยังไม่ทำ per-bug preflight
- ตรวจ environment เครื่องนี้: Windows 11, Intel Core i5-1334U (10 cores/12
  logical processors), RAM ประมาณ 16 GiB, disk free ประมาณ 303 GiB ตอนตรวจ
  เครื่องบีมอีกสองเครื่องยังไม่ได้ตรวจ
- เพิ่ม protocol/interface proposal, lineage และ semantic-review gate แยก
  evaluator complete จาก usable counts; เก็บ partial metrics ไม่แทน null ด้วย 0
- Tests ผ่าน 46 บน Linux: worker/connection/contract 26, evaluator เดิม 16, generation เดิม 4;
  เป็น fixtures ไม่ใช่ actual experiment outcomes; Bash/PowerShell syntax ผ่าน
- ทดลอง CLI algorithm worker บน Windows จริงและเก็บ preflight_failed เพราะ
  Linux/Java11/Defects4J ยังไม่พร้อมไว้ใต้ output/api854-beam/preflight-attempts/
  evaluation_attempted=false ไม่ใช่ผล primary และไม่เพิ่มยอด 3,416 jobs
- รันตัวติดตั้งแบบ Administrator สำเร็จถึงการเปิด Windows components สำหรับ
  WSL; transcript วันที่ 3 ต.ค. 01:14 ระบุ RESTART_REQUIRED ปิดตัวติดตั้งแล้ว
  ไม่รีสตาร์ตอัตโนมัติ ผู้ใช้รีสตาร์ตให้แล้ว จึงติดตั้ง Ubuntu/Java11/Defects4J ต่อ
- รอบติดตาม: ผู้ใช้ยืนยันว่ายังไม่ได้รีสตาร์ต ตรวจพบ boot เดิมวันที่ 25 ก.ย.
  เพิ่ม frozen implementation snapshots ต่อ attempt และตรวจ hash หลังรัน
  ปฏิเสธ schema_version ที่เป็น boolean; ตัวติดตั้งตรวจ EnablePending/
  DisablePending เพื่อไม่เดินหน้าติดตั้ง distro ก่อน Windows restart
- นำ QueueClient/contract ต้นฉบับจาก aom commit 2e419e42 มาในไฟล์ใหม่ที่ไม่ชน
  เก็บ worker token ใน .local/api854/beam-access.private.json ซึ่ง Git ignore
  ตรวจ token ไม่ปะปนใน code/docs/evidence และเก็บ receipt queue-connection-1.json
- ปรับ generation/evaluation lineage ให้รองรับ attempt ID คนละ stage ของคิว
  1.0 โดยไม่เปลี่ยน original generation lineage; frozen team protocol สามารถ
  มี source hashes ของเจ้าของส่วนอื่นเพิ่มเติม แต่ Beam dependencies ต้องตรง
- Development smoke CMA-ES และ FSCS-ART/Lang-4 รุ่น protocol-proposal-6: วิธีละ 30 methods, fixed
  passed_twice, buggy มี assertion failure, coverage lines 12/25 และ branches
  3/14; evaluator complete แต่ usable=false จน semantic review ผ่าน หลักฐาน
  อยู่ results/validation/api854-beam/beam-development-lang4-v2/ ไม่ใช่ primary
- เก็บ smoke รุ่นแรกที่ล้มเหลวจาก copy2 metadata บน Windows mount ไว้ แก้
  evaluator ใช้ copyfile เพื่อรักษา bytes และ hashes และเก็บ preflight error
  เดิมไว้ใน wrapper โดยไม่กลบด้วยข้อความ missing suite hash

## จุดที่ต้องทำต่อ

1. ตรวจ development smoke measurements/semantic execution ของ Lang-4 และ
   meaningful fixtures ของ Closure-3/JxPath-1 ก่อนเรียก usable
2. Scan adapter project families ที่เหลือ ร่วมกับ
   แชมป์/ออมแก้ fixtures/framework/unsupported ไม่ประกาศรองรับจาก allocation
3. ออม review JSON contract และ protocol; แชมป์เชื่อม fixed-source lineage ของ
   generated AI suites; คิวจริงต้องมี leases, artifacts upload และ atomic advance
4. ตรวจ instrumentation/หลักฐาน executed/skipped/target checks ก่อนนับ usable;
   รับรอง Gate A แล้วตรึง pilot 20 bugs/80 jobs ก่อน Gate B และ batch shard

สถานะ checkpoint แรก: ยังไม่ commit/push โค้ดใหม่ ยังไม่ส่ง Classroom และไม่ได้ตั้งเวลาเริ่มรัน 05:00
ไม่มี live KKU request หรือผล primary ใหม่จากการเตรียมงานนี้ Development
validation เก็บใต้ results/validation/api854-beam/ ไม่รวม primary study

การอ้างอิง runtime: [Defects4J v3.0.1](https://github.com/rjust/defects4j/blob/v3.0.1/README.md)
และ [Microsoft WSL commands](https://learn.microsoft.com/en-us/windows/wsl/basic-commands)
รายละเอียดคำสั่งใน REPRODUCE_TH.md

## เพิ่มข้อกำหนด suite/model จากแชมป์ผ่านผู้ใช้

- เพิ่ม `pack_suite.py` และสัญญา `beam-java-suite-v1` ใน SUITE_HANDOFF_TH.md:
  suite.tar.bz2 Java-only ตาม package paths, source bytes เดิม, normalized tar metadata,
  SHA-256 และ manifest แยก โดย test_count เป็น producer-declared ไม่อ้าง executed count
- Tests ล่าสุดผ่าน 52 บน Linux: worker/connection/packaging 32, evaluator 16, generator 4
- Actual packaging smoke ใช้ Java จาก CMA-ES/Lang-4 development เดิม แพ็กซ้ำ hash ตรง
  แล้วใช้ fresh fixed/buggy checkouts ทดสอบผ่าน evaluator; receipt อยู่
  output/api854-beam/suite-handoff-smoke-1/ ไม่ใช่ primary และ usable=false รอ semantic review
- proposal รุ่น 7 เพิ่ม packaging contract และชื่อโมเดลที่ผู้ใช้แจ้ง ส่วนรุ่น 8 ล่าสุด
  ระบุ requested KKU IDs `claude-sonnet-5`, `gemini-3.5-flash-lite` ตามผู้ใช้แก้ไข
  Sonnet 5.5 ที่แจ้งผิดแล้ว ไม่ใช้ Haiku เดิมหรือ fallback อัตโนมัติ
- สถานะ model policy เป็น user_supplied_ids_pending_api_preflight; docs API มีเพียง
  ตัวอย่าง catalog ไม่มี KKU API key ในเครื่องและไม่ได้เรียก generation API
  ยังต้องตรวจ actual model, prompt/quota และ freeze กับทีมก่อน primary

## เริ่ม worker integration ตามคำสั่งล่าสุด

- เพิ่ม prepare/algorithm/evaluation queue workers และ fenced publication ใช้ QueueClient
  ของออมกับ transport/heartbeat ของแชมป์; เก็บ intents/receipts, ไม่ retry mutation ที่ไม่ทราบผล
- เพิ่ม BeamSuiteResolver และ BeamGenerationHandoff สำหรับ source blocks ของแชมป์
  พร้อม fixed-source/attempt/hash binding; แก้ UTF-8 byte writes ใน generation worker
  เพื่อไม่ให้ Windows newline translation ทำให้ hash กับ stored source ไม่ตรงกัน
- Tests ล่าสุด 151 ผ่าน: api854 131, evaluator 16, generator 4; รวม local HTTP tests
  ที่ตรวจ expiry, mixed protocol, unknown upload/complete และ mock/live separation
- Actual Lang-4 local queue มี 4 job keys/12 stage attempts ผ่านครบ fixed/buggy/coverage
  AI response สองกรณีเป็น fixture เท่านั้น ไม่มี real KKU requests; usable=false
- URL คิวใหม่ที่ผู้ใช้ให้ authenticated ready: prepare 80 งาน/0 attempts บีม 24 งาน
  ยังไม่ claim เพราะยังไม่มี bytes ของ protocol.core-frozen.json ที่ตรง SHA ในคิว
  ตอน fetch origin/aom ยังอยู่ 2e419e42 และยังไม่พบ 6dd61871 ที่ออมแจ้ง
- รายละเอียด [BEAM_QUEUE_INTEGRATION_TH.md](BEAM_QUEUE_INTEGRATION_TH.md) และ public
  checkpoint ใน docs/api854/evidence/ ไม่แนบ URL/token ส่วนตัว

## ตรวจงานออมที่ push แล้ว

- ดึง `origin/aom` ถึง `8fcec539` และ `origin/champ` ถึง `ca58d751` แล้ว
  เก็บ import receipt พร้อม source hashes ใน output/api854-beam/team-import-2.json
- ได้ bytes ของ `protocol.core-frozen.json` และตรวจ SHA-256 ตรงกับคิว:
  `675a480915c40ab19f7be57b56b046fb8c8924e7330e4cc8956b2ed3de6d31ad`
- เพิ่ม execution binding สำหรับ condition `preflight` โดยเก็บ protocol เดิมไม่แก้ไข
  worker เปิดเฉพาะ prepare และตรวจ enabled_stages ก่อน claim; ไม่ใช้ core นี้เรียก AI/evaluate
  source hashes ของ implementation เป็นหลักฐาน local execution ไม่ใช่การอ้างว่าทีม freeze แล้ว
- ชุด integration ล่าสุด 149 tests ผ่าน ไม่มี skip; รวม legacy evaluator 16 และ generator 4
  ที่ตรวจผ่านก่อนหน้าเป็น 169 tests. Local four-method smoke เดิมมี source snapshot ของรุ่นเดิม
- Pilot ของบีม 6 bugs/24 jobs: Closure-176, JacksonXml-1, Jsoup-1, JxPath-1/22, Mockito-1
  ทั้งหกผ่าน checkout/build/shared declaration discovery แต่ semantic validity ยัง pending
  รายละเอียดใน evidence/beam-pilot-adapters-20261003.json
- กำลังเตรียม commit/push บน beam ด้วยผู้เขียนที่ผู้ใช้กำหนด
  การเริ่ม live preparation ถูก automatic approval review ระงับก่อนรัน เนื่องจากต้องยืนยัน
  สิทธิ์ส่ง fixed-source/context/targets/logs ไปคิวออม; ยังไม่มี live claim/upload หรือ KKU request
- Champ API CLI ล่าสุดใช้ individual context-manifest.json/prompt.md และ callable resolver
  ส่วน Beam handoff ส่ง suite + generation-lineage แยกเพื่อ evaluator. ต้องตรวจรับ composition
  กับ primary protocol ใหม่ก่อนใช้ CLI นี้ทำ real generation; preflight prepare ไม่ข้าม Gate A
