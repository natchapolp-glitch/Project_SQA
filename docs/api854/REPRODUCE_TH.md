# วิธีเริ่มงาน branch beam — แผน 2026-10-03

สถานะ: มี local JSON workers และการทดสอบ contract แล้ว ยังไม่มี live Defects4J
pilot, KKU API หรือ queue integration; Gate A/B ยังไม่ผ่าน ผลเดิม 17 bugs เป็น
historical ไม่ปะปนกับผลชุดใหม่ อ่านแผน `docs/superpowers/plans/2026-10-03-sqa854-collaborative-48h.md`
และ proposal interface `docs/api854/JOB_CONTRACT_BEAM.md` คู่กัน

## 1. ติดตั้งบนเครื่องบีม

Windows เครื่องนี้ยังไม่มี WSL ที่ใช้งานได้ การติดตั้งต้องใช้สิทธิ์ Windows
Administrator ซึ่งต่างจากการอนุญาต sandbox ของ Codex เปิด PowerShell ด้วย
**Run as administrator** แล้วรัน:

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File "C:\Users\User456\Documents\SQA\Project_SQA\scripts\setup\install-beam-wsl.ps1"
```

สคริปต์เก็บ transcript ใน `output/api854-beam/setup/` เปิด Windows features แบบ
NoRestart ถ้าขึ้น `RESTART_REQUIRED` ให้บันทึกงานและรีสตาร์ตเอง แล้วรันสคริปต์เดิม
อีกครั้ง จากนั้นเปิด `wsl -d Ubuntu-24.04` ตั้ง Linux username/password
ไม่ต้องส่ง password/API key ในแชท

ใน Ubuntu:

```bash
cd /mnt/c/Users/User456/Documents/SQA/Project_SQA
bash scripts/setup/setup-beam-defects4j.sh
source "$HOME/sqa-beam/environment.sh"
python3 -m scripts.study.api854.environment --output output/api854-beam/environment-linux-1
```

ใช้ Java/Javac 11, Defects4J v3.0.1 และ TZ=America/Los_Angeles สำหรับรันทดสอบ
(กำหนดการส่งงาน/โควตายังเป็น Asia/Bangkok) Checkout อยู่ Linux filesystem
ใต้ `$HOME/sqa-beam` เพื่อลด I/O บน Windows mount ติดตั้ง dependencies และ
Defects4J init ต้องดาวน์โหลด repositories/libraries และใช้พื้นที่มาก ตรวจ disk
free จาก environment evidence ก่อนแจกงาน

รัน preflight บนอีกสองเครื่องด้วย เก็บ CPU/RAM/disk/runtime แยกกัน ทุก worker
บนเครื่องเดียวต้องใช้ worktrees root เดียวเพื่อใช้ CPU slot lock ร่วมกัน เริ่ม
หนึ่งงานหนักต่อเครื่องเท่านั้น ยังไม่เพิ่ม slots จน pilot ตรวจรับ

## 2. Inventory และ adapter readiness

บนเครื่องนี้สร้างบัญชี Linux `beam` สำหรับ worker แล้ว ใช้จาก PowerShell:

```powershell
wsl -d Ubuntu-24.04 -u beam
```

สคริปต์ติดตั้งระบบรันด้วย root ส่วน Defects4J อยู่ `/home/beam/sqa-beam/defects4j`
และ worker รันด้วย `beam` ไม่ต้องเปิดเผย Linux password หรือ KKU keys

```bash
python3 -m scripts.study.api854.adapters --verify-installed \
  --output output/api854-beam/readiness-installed-1
python3 -m scripts.study.api854.adapters --verify-installed \
  --scan Closure:3 JxPath:1 Lang:4 \
  --worktrees "$HOME/sqa-beam/worktrees" \
  --output output/api854-beam/readiness-representatives-1
```

ตัวอย่างนี้เป็น bugs ของบีม ใช้สำหรับตรวจ adapter ไม่ใช่รายการ 20 pilot bugs
ที่ตรึงแล้ว ต้องร่วมกับออมเลือก pilot ครบ 17 projects ก่อน live pilot
คำสั่งแรกตรวจ installed `pids/bids` กับ allocation 854 bugs จริง ไม่ใช้
`active-bugs.csv` ที่ root เป็นรายการทุก project เพราะไฟล์นั้นมีข้อมูล Lang
คำสั่ง scan ทำ fixed/buggy checkout/build, baseline trigger และ discovery
ใช้ generic probe เดิมตาม modified classes ของ **bug ที่ระบุ** ไม่ล็อก bug 1
`supported` หมายถึง build/discovery ผ่าน ยังต้องตรวจ fixture/oracle ใน pilot
รายการไม่เคย scan คง `needs_adapter` พร้อม `attempted=false` ไม่มีการแต่ง
unsupported/failure ให้ครบยอด ผลขั้นต้นอยู่ `output/api854-beam/readiness-initial/`

## 3. Protocol proposal และ job JSON

ให้ออม review proposal/selection/budget/seed/cap/source hashes ร่วมกับแชมป์
ก่อนใช้ primary workers ต้องใช้ protocol revision เดียวกัน เมื่อแก้ source
ให้สร้าง protocol/run ใหม่ เก็บ attempt เก่าไว้

```bash
python3 -m scripts.study.api854.configuration \
  --output output/api854-beam/protocol-proposal-linux-1.json
python3 -m scripts.study.api854.make_job --project Lang --bug-id 4 \
  --approach cmaes --run-id beam-adapter-check --attempt-id attempt-1 \
  --protocol output/api854-beam/protocol-proposal-linux-1.json \
  --output output/api854-beam/jobs/lang4-cmaes-linux.json
```

## 4. Algorithm generation และ evaluation

หลัง environment/target/protocol ผ่านการตรวจรับ:

```bash
python3 -m scripts.study.api854.algorithm_worker \
  --job output/api854-beam/jobs/lang4-cmaes-linux.json \
  --protocol output/api854-beam/protocol-proposal-linux-1.json \
  --results results/study --worktrees "$HOME/sqa-beam/worktrees"
```

อ่าน generation `result.json` ได้ suite path/hash และ lineage ใช้ hash ใน job
แทน `<PROTOCOL_HASH>` ด้านล่าง:

```bash
python3 -m scripts.study.api854.evaluate_worker \
  --job output/api854-beam/jobs/lang4-cmaes-linux.json \
  --protocol output/api854-beam/protocol-proposal-linux-1.json \
  --suite results/study/beam-adapter-check/<PROTOCOL_HASH>/Lang/4/cmaes/attempt-1/generation/suite/Lang-4f-cmaes.101.tar.bz2 \
  --lineage results/study/beam-adapter-check/<PROTOCOL_HASH>/Lang/4/cmaes/attempt-1/generation/result.json \
  --results results/study --worktrees "$HOME/sqa-beam/worktrees"
```

suite เดิมต้องผ่าน fixed สองครั้ง จากนั้น buggy และ coverage มี logs และ hashes
ครบ ความล้มเหลว coverage เก็บ fault detection ที่วัดได้ไว้ ไม่มีการเติม 0 แทน
missing และไม่แก้ assertions เพื่อให้ผ่าน fixed/ทำ buggy fail เปลี่ยน approach
เป็น `fscs-art` เพื่อทำอีกวิธี ส่วน Claude/Gemini ให้แชมป์ส่ง suite และ lineage
ตาม contract เดียวกัน โดย worker นี้ไม่มีการเรียก AI

ผล complete ยังไม่ถือ usable จนตรวจ target execution/fixtures/oracles และ
executed/skipped counts ตาม `JOB_CONTRACT_BEAM.md` หากยังวัด counts ไม่ได้ให้
pending review เพิ่ม instrumentation ก่อนนับ usable อย่าเดาจากจำนวน methods
หรือ `Failing tests: 0` ตรวจ Closure/JxPath กับทีมก่อนปล่อย family

หลังมี review พร้อมหลักฐานที่ hash ตรวจได้:

```bash
python3 -m scripts.study.api854.validity \
  --evaluation <ATTEMPT_DIRECTORY>/evaluation --review <REVIEW_JSON>
```

เก็บ review แยก immutable ไม่เขียนทับ evaluation เดิม คิวกลางยังต้องมี leases,
renew, reconciliation, upload/shared artifact URIs และ atomic completion จากออม
ก่อนหลายเครื่องรันร่วมกัน ไม่แชร์ SQLite runtime หรือ worktree เดียวกัน

## 5. ตรวจโค้ด

```bash
python3 -m unittest discover -s scripts/study/api854/tests -t . -v
python3 -m unittest discover -s scripts/study/tests -p test_evaluate.py -v
python3 -m unittest discover -s scripts/study/tests -p test_generate.py -v
```

ทั้งหมดเป็น fixtures ใน temporary directories ไม่ใช่ผลทดลองจริง ติดตั้ง Linux runtime
และ scan Lang-4/Closure-3/JxPath-1 แล้ว งานถัดไปคือ semantic execution review,
scan project families ที่เหลือ, เชื่อม worker คิว/AI lineage และผ่าน Gate A ก่อน pilot 20 bugs/80 jobs
หลัง Gate B จึงดูแล shard บีม 285 bugs/1,140 jobs และ reproducibility/demo

## 6. ตรวจคิวของออม

นำ client/contract จาก origin/aom commit `2e419e42` มาในไฟล์ใหม่แล้ว ไม่ merge
ทับงาน beam Token บีมเก็บใน `.local/api854/beam-access.private.json` ซึ่ง Git
ignore รันตรวจแบบ read-only ได้:

```bash
python3 -m scripts.study.api854.queue_connection \
  --output output/api854-beam/queue-connection-next.json
```

คำสั่งนี้ตรวจ health/schema/status ไม่ claim หรือ seed งาน และไม่ส่ง key ไป KKU
Receipt การตรวจจริงอยู่ `output/api854-beam/queue-connection-1.json`: schema 1.0,
health พร้อม และมี 0 jobs ตอนตรวจ ต้องรอออมตรึง protocol/seed pilot หลังทีมตรวจรับ
ก่อนแจกงาน อ่าน QUEUE_CONNECTION_TH.md และ queue-schema-v1.json ของออม
การเชื่อมต่อสำเร็จยังไม่ใช่การทดสอบ worker publish/lease/upload ครบ pipeline

## 7. แพ็ก suite ส่งต่อ evaluation

ใช้ `python3 -m scripts.study.api854.pack_suite --sources <java-only-folder>
--output <new-output-folder> --test-count <producer-declared-count> --test-method-cap 30`
รันเป็นคำสั่งบรรทัดเดียว รายละเอียดรูปแบบ, lineage และ upload/complete อยู่
[SUITE_HANDOFF_TH.md](SUITE_HANDOFF_TH.md) มี development smoke ผ่าน actual Lang-4
ที่ `output/api854-beam/suite-handoff-smoke-1/receipt.json` และ proposal รุ่น 7
การผ่าน smoke ไม่ยืนยัน usable หรือความพร้อม 854 bugs

## 8. Worker ต่อคิว

โค้ดเชื่อมต่ออยู่ใน [BEAM_QUEUE_INTEGRATION_TH.md](BEAM_QUEUE_INTEGRATION_TH.md)
มี CLI prepare/algorithm generation/evaluate พร้อม renewal/upload/completion journal
และ `BeamGenerationHandoff` สำหรับ Java source blocks ของแชมป์ การทดสอบ local
ไม่ใช่การตรวจรับคิวสดหรือ pilot ทั้ง 17 projects
