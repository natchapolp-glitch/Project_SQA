# API854 protocol draft v1

Protocol: `experiments/configs/api854-20261003/protocol.json`
อ่านแผน 2026-10-03 และ START_HERE ก่อนใช้ เอกสารนี้เป็น contract ที่เตรียมให้ออม/ทีมตรวจรับ
ยังไม่ได้ freeze model settings หรือผ่าน Gate A/B และยังไม่ได้ส่ง live request

## Inventory และ jobs

ตรวจ installed Defects4J 3.0.1 ด้วย pids/bids แล้วเทียบ exact active project/bug pairs กับ ownership
ตรง 854 bugs / 17 projects ไม่ใช้ Lang-only active-bugs.csv ที่ root
Installed export มี installation revision, README version hash และคำสั่งที่ใช้
bugs.json มี inventory_hash, ownership file hash และ installed export hash
jobs.json มี 3,416 unique keys; champ=1,140, beam=1,140, aom=1,136
ไม่ถือว่า inventory ถูกต้องแล้ว adapters รองรับทั้งหมด

Pilot ล่วงหน้า 20 bugs: lowest active ID ของทั้ง 17 projects เพิ่ม highest active ID ของ Closure,
JxPath และ JacksonDatabind รวม 80 jobs ต้องตรวจ large-context/framework coverage กับบีมก่อนเปิด gate
รายการอยู่ใน pilot20.json ไม่เลือกจาก outcomes และไม่แทนด้วย bug ที่ง่ายกว่าอย่างเงียบ

## Fairness และ budget

หนึ่งรอบ/วิธี/bug; algorithms seed 101, 30 proposed inputs ไม่ใช่ 30 usable tests ที่รับประกัน
Fixed observations สอง JVM ต่อ proposal; final fixed validation สองครั้ง + buggy + coverage แยก cost
FSCS candidate set=10; CMA-ES sigma=0.3, default population 4+floor(3*log(dimensions))
Target eligibility ใช้ shared declaration signatures ไม่มี buggy observations/fix patch เป็น feedback
Coverage ใช้ modified target classes เดียวกันทั้งสี่วิธี ไม่อ้าง coverage ทั้ง project
KKU Haiku/Flash Lite ต้อง resolve exact API IDs และ response model versions/settings จริง
AI seed=null ไม่อ้างว่า seed 101 ควบคุม provider randomness
Source/API/build context เหมือนกัน; fresh request ไม่มี compile/test log feedback
Timezone execution คง America/Los_Angeles ตาม runner เดิมทั้งสี่วิธี; schedule ใช้ Asia/Bangkok
ทีมต้องยืนยัน evaluator/context configs ใช้ค่าเดียวกันก่อน Gate A

## Raw/processed policy และ exclusions

คง raw response immutable และ cap source order 30 methods พร้อม processing history
Allowlist draft จำกัด extraction/code fences และจัด Java packages เท่านั้น
ห้ามแก้ assertions/prune fixed-failed methods/เพิ่ม null-return guards ใน condition นี้
Closure compiler=null หรือ returnก่อนassert = invalid_suite ไม่ใช่ usable
JxPath oracle/fixture repair ต้องทำ condition ใหม่และเปิดเผย raw/processed delta
Semantic changes จาก Codex ต้องระบุว่า locally edited AI-assisted; ไม่อ้าง raw KKU output

## Gate และ cutoff

Default seed jobs เป็น held/not_attempted ไม่มี workers เริ่มจากการสร้าง files
protocol.state ต้อง frozen, installed_verified=true และ exact models/settings พร้อม ก่อนเปิด pilot
gate-file ระบุ protocol/inventory hashes, evidence hashes และ reviewed_by ทั้ง champ/beam/aom
ตัวอย่าง gates.example.json ใช้เปิดงานไม่ได้แม้เป็นไฟล์ valid JSON
Gate A เปิดเฉพาะ 80 pilot jobs; Gate B เปิด full cohort หลัง token/throughput/validity checks จากจริง
เปลี่ยน protocol หลัง pilot → เก็บ pilot เป็นคนละ condition และใช้ database/run directory ใหม่

Generation cutoff 5 ต.ค. 2569 01:00 +07:00; service ไม่ claim/start/dispatched generation ใหม่หลังเวลา cutoff
Evidence freeze 03:00: หยุด claim/start execution ใหม่; drain outcomes ที่เริ่มแล้วได้ก่อน operator freeze
Deadline 05:00: ทีมตรวจแพ็กและส่ง; cutoff ไม่แต่ง unattempted ให้เป็น failures
Protocol draft ไม่เปลี่ยน implementation_started ใน planning constraints ที่เป็น historical confirmation

## Metrics

แยก bugs, job keys, attempts, suite/test-method counts; retries ไม่เพิ่ม denominator
Terminal keys/3,416 และ matched usable bugs/854 คนละค่า
By-method eligible coverage/FDR เป็น descriptive; comparison หลักใช้ matched_by_approach เท่านั้น
Report เก็บทุก failure/missing job และ historical dataset แยกออก
Macro ratio = เฉลี่ยต่อ usable suite ที่ denominator>0; micro = ผลรวม covered/ผลรวม total
Unknown metrics=null ไม่ใช่ 0; known-zero ratio ต้องมาจาก measurement จริง
Workflow p50/p95 ใช้ nearest rank ของ completed attempt durations ไม่ใช่ isolated CPU benchmark
Token บวกเฉพาะ generation attempts ไม่บวก provenance ที่คัดลอกไป evaluation อีกครั้ง
Quota/ETA คง null เมื่อยังไม่มีข้อมูลที่ยืนยัน ไม่ extrapolate จากจำนวนบัญชี/เครื่อง

## Shared policy v2 ที่ออมจัดทำ

ดู AOM_PREPARE_V2_TH.md และ prepare-policy.v2.json สำหรับ primary draft ล่าสุด.
Suite เกิน 30 methods ปฏิเสธทั้งชุด ไม่ใช้ source-order truncation. Core protocol เดิมคง bytes เดิม.
