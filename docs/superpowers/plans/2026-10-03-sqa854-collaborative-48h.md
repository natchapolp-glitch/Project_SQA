# SQA 854 Bugs — Collaborative 48-Hour Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:executing-plans. งานอิสระในแต่ละระยะทำ parallel ได้ แต่ต้องผ่านจุดตรวจรับร่วมก่อนเริ่มระยะถัดไป แชมป์ บีม ออมใช้ Codex ใน branch ของตนเอง

**Goal:** ตั้งแต่ 3 ต.ค. 2569 เวลา 05:00 ถึง 5 ต.ค. 2569 เวลา 05:00 (Asia/Bangkok) ให้ active bugs 854 รายการมีผลทดลองด้วย CMA-ES, FSCS-ART, KKU Claude Haiku และ KKU Gemini Flash Lite รวม 3,416 job keys โดยนับความล้มเหลวที่เกิดจากการทดลองจริงพร้อมหลักฐาน และแยกงานที่ยังไม่ทดลอง

**Architecture:** เริ่มจากทีมสามคนร่วมแก้คอขวดเรื่อง target adapters, API/quota และ queue contract จากนั้นทดสอบ pipeline ครบสี่วิธี ก่อนกระจายงานแบบ shard ที่ไม่ซ้ำกัน ใช้คิวกลางที่คงสถานะและเก็บ artifacts อัตโนมัติ ส่วน difficult jobs ถูกส่งกลับให้ทั้งสามคนช่วยกันแทนการปล่อยให้คนเดียวติดงาน

**Tech Stack:** Defects4J 3.0.1, Java/JUnit, Python runners, KKU IntelSphere API, transactional queue, isolated worktrees/containers, Codex และ Git

**Spec:** ข้อ 2.2 ใน `C:/Users/ACER/Downloads/SQA_Project_2026 (2).pdf`; คำขอวันที่ 2026-10-03 ให้ทำ 854 bugs ด้วยสามคน แตกงานยากเป็น parallel ช่วยกันก่อนทำขั้นถัดไป และแจ้งก่อนสลับบัญชี KKU เมื่อ tokens หมด

**Confirmed resources:** ผู้ใช้ยืนยัน 10 บัญชี KKU ที่มี API key พร้อมแล้ว และ 6 เครื่อง (บีม 3, แชมป์ 1, ออม 2) เพื่อนยังไม่ได้เริ่ม implementation ทั้งสาม branch มีฐานเดียวกัน รุ่นโมเดลเลือก Haiku และ Flash Lite; numeric model IDs/version และ CPU/RAM/OS ของแต่ละเครื่องต้องตรวจใน preflight

**Precedence:** ข้อกำหนดที่ยืนยันและ contract ในเอกสารนี้แทนข้อเสนอ 100–130 บัญชี/64 workers, model choices, queue และ export paths ที่ต่างกันในแผน 2026-10-02 ส่วนรายละเอียด KKU endpoints ใช้อ้างอิงจากแผนเดิมได้

## Global Constraints

- แผนนี้แทนการแบ่งงานแบบเจ้าของส่วนเดียวตลอดโครงการ ใช้เจ้าของไฟล์เพื่อลด conflict แต่ทุกคนช่วยแก้คอขวดร่วมกัน
- 854 bugs × 4 วิธี × 1 รอบ = 3,416 jobs; retries เป็น attempts ของ job เดิม ไม่เพิ่มจำนวน bugs
- จำนวน active bugs/17 projects ต้องยืนยันจาก installed Defects4J และตรึง hash ก่อนส่ง API; ไม่รวม deprecated
- เป้าหมายที่ผู้ใช้เลือกคือทุก bug มีผลทั้งสี่วิธี รวม real failures ที่มีหลักฐาน ไม่กำหนดว่าทุก suite ต้องใช้ได้ แต่ queued/quota_deferred/not_attempted ไม่ใช่ real failures และไม่นับว่าทดลองครบ
- ใช้ Haiku/Flash Lite รุ่นที่ตรึงผ่าน KKU API สำหรับผล AI สองวิธี ไม่มี fallback เปลี่ยนรุ่นเงียบ Codex ช่วย orchestration/runner/review; การสร้าง assertions/fixtures/logic ใหม่ด้วย Codex แยกเป็น assisted condition ไม่รวม primary KKU results
- ส่ง fixed source/API/build context ตามขอบเขตงาน ห้ามส่ง compile/test logs กลับ AI ตามข้อจำกัดเดิมของผู้ใช้
- credentials อยู่ใน environment/secrets ที่ถูก ignore; ผลใช้ account alias ไม่เปิดเผย key/email/auth header
- quota ตั้งต้น Claude 200,000 และ Gemini 350,000 tokens/account/day; timezone ตั้งต้น Asia/Bangkok รีเซ็ตเที่ยงคืนตามผู้ใช้ แต่ต้องตรวจ server จริง
- เมื่อ quota หมดหรือเหลือไม่พอ reserve: หยุดส่งคำขอใน bucket นั้นและแจ้งผู้ใช้ก่อนสลับบัญชี ไม่หมุนบัญชีอัตโนมัติโดยเงียบ
- เก็บผลเก่า 17 bugs/204 รอบเป็น historical; ชุดใหม่ต้องมี protocol/version ของตนเอง
- ระยะเวลาในแผนเป็น allocation ไม่ใช่หลักฐานความเร็วจริง หาก gate ไม่ผ่านต้องรายงานความเสี่ยงและช่วยแก้ ไม่นับข้ามเป็นผ่าน

## Review Focus

1. runner ที่รองรับเพียง 17 sampled bugs: ตรวจ adapter coverage ก่อน scale ไม่ตีความว่ารายการ 854 คือรองรับแล้ว
2. tests ว่าง/fixture null/early return: ตรวจ meaningful assertions และ target execution โดยเฉพาะ Closure
3. quota หมดแต่สลับ key เงียบ: บังคับ state/notification gate ก่อน request แรกบนบัญชีใหม่
4. duplicate jobs/shared worktree: ใช้ unique job key, lease และพื้นที่รันแยกก่อนขยายเครื่อง
5. source/model/prompt/record ปะปน: ทุก artifact มี lineage/hash และใช้ protocol ที่ตรึงเดียวกัน

## 1. เวลาเริ่มและนิยามเสร็จ

T0 ที่ผู้ใช้ยืนยันคือ `2026-10-03T05:00:00+07:00` (`2026-10-02T22:00:00Z`); deadline `2026-10-05T05:00:00+07:00` (`2026-10-04T22:00:00Z`) ใช้วันส่ง 5 ต.ค. แทนข้อความ 4 ต.ค. ที่ผู้ใช้แก้แล้ว การเขียนแผนยังไม่ใช่การเริ่มรันหรือการตั้งระบบให้ตื่นตามเวลา

เสร็จระดับการทดลองตามเป้าหมาย: ทุก 3,416 job keys มี actual attempt และ observed outcome รวม refused/generation_failed/compile_failed/fixed_failed/coverage_failed/environment_failed/timeout ที่เกิดจริง ถ้าเตรียม target ไม่ได้ให้บันทึก adapter_unsupported/preflight_failed พร้อมหลักฐานแยกจากการรันทดสอบ งานที่ quota ไม่พอหรือไม่เคยส่ง/รันต้องคง not_attempted/deferred ไม่แต่งเป็น failure

เสร็จระดับผลใช้ได้: ทุก 854 bugs มี usable suite ครบสี่วิธี พร้อม fixed repeat, buggy และ coverage หากยังไม่ถึงให้รายงานเปอร์เซ็นต์ matched-usable ตามจริง

ไม่ต้องให้ทุกเทสผ่าน buggy; fault_detected=false เป็นผลที่ยอมรับได้ หาก suite ถูกต้องและได้วัดจริง

## 2. ระยะ A — ทั้งสามคนช่วยกันปิดงานยาก / ชั่วโมง 0–4

### A1. แตกปัญหา target และ Defects4J adapters

- [ ] **บีม:** inventory active bugs, build frameworks, modified classes และความสามารถ runner เดิมต่อ bug; ส่ง supported/unsupported manifest
- [ ] **แชมป์:** สร้าง fixed-source/API/build-context export ตาม contract ของ target selection ไม่ผูก target กับ bug 1 เท่านั้น
- [ ] **ออม:** ตรวจนโยบาย target/budgets/seed/test caps ของทั้งสี่วิธีและทำเครื่องมือตรวจ inventory/hash/duplicate
- [ ] ทั้งสามคนเลือก unsupported ที่เป็นตัวแทน framework/project ช่วยกันแก้ adapter และทดสอบ integration ก่อนแจก shard ห้ามให้บีมแบกทุก project คนเดียว

### A2. ทำ API/quota และ queue พร้อมกัน

- [ ] **แชมป์:** KKU client/model mapping/raw response/usage/quota typed errors อ่าน API reference ที่ตรวจไว้ในแผน 2026-10-02
- [ ] **บีม:** evaluation worker/worktree/cache/timeouts ต่อ runner เดิม ตรวจ suite เดียวกันบน fixed/buggy
- [ ] **ออม:** queue/leases/attempt IDs/artifact contract และ progress summary; one shared server queue สำหรับหลายเครื่อง ไม่แชร์ SQLite file เขียนบน network drive
- [ ] ทุกคนตกลง schema ก่อน worker integration: job key=(project,bug_id,approach,protocol_hash,repeat_index), source/suite hashes, account alias, requested/actual model, usage, quota, status, paths
- [ ] ออมตรึง queue contract และสร้าง 80 pilot jobs ก่อน pilot: scheduling state queued/leased/deferred_quota/needs_reconciliation/finished แยกจาก observed outcome; completion ของ generation ส่งต่อ evaluation แบบ atomic และ completion ของ evaluation ไม่เปลี่ยน source/suite เดิม
- [ ] ใช้ queue server บนเครื่องออม 1 โดย SQLite อยู่ local หลัง server; workers ติดต่อ claim/renew/publish/fail ผ่าน server API ที่ตกลงกัน ทุก mutation ตรวจ lease token+version และ artifact publish ก่อน advance stage ป้องกัน stale worker เขียนผล
- [ ] ทุก artifact ใช้ `results/study/<run_id>/<protocol_hash>/<project>/<bug_id>/<approach>/<attempt_id>/` attempt_id ไม่ซ้ำข้าม protocol; immutable raw response/source/suite/logs ใช้ artifact URI ที่ทุกเครื่องเข้าถึงผ่านบริการกลางได้ ไม่ใช้ local path ของเครื่องอื่น

### A3. งานยากเฉพาะ Closure และ JxPath

- [ ] **แชมป์:** ตรวจ fixture/context/prompt และ provenance คำตอบ AI
- [ ] **บีม:** ตรวจ build/test framework และ assertion execution พร้อม fixed/buggy/coverage
- [ ] **ออม:** ตรวจ exclusion/repair policy และบันทึก raw/processed differences เป็นเงื่อนไขทดลอง
- [ ] ยืนยันว่า suite ที่มี compiler=null แล้ว return ก่อน assert ไม่ถูกนับ usable แม้ compile ผ่าน

**Gate A:** pipeline ขั้นพื้นฐานทำงานครบ 4 วิธี, queue resume ได้, quota notification ใช้ได้ และ pilot target families มี fixture/oracle/instrumentation ที่ตรวจได้ ทุก bug มี readiness state supported/needs_adapter/unsupported ห้ามปล่อย family ที่ยังไม่ได้ตรวจรับเพียงเพราะมีเจ้าของและแผนแก้ ถ้า gate ติด ให้ทั้งสามคนช่วยแก้ blocker

## 3. ระยะ B — Pilot ร่วมกัน / ชั่วโมง 4–8

- [ ] ตรึง 20 pilot bugs ล่วงหน้าให้ครอบคลุมทั้ง 17 projects รวม Closure/JxPath/context ใหญ่ รวม 80 jobs
- [ ] แชมป์ดู API token/latency/error; บีมดู generation/evaluation/runtime; ออมดู counts/hash/restart/model fairness
- [ ] ใช้ mock checks สำหรับ duplicate claims, interrupted requests, quota exhaustion, notification before switch, auth failure และ midnight boundary ก่อน live pilot
- [ ] ตรวจ actual quota scope และ remaining ของบัญชี; ตรวจรุ่นโมเดลตรงทุกบัญชี เก็บ refusal/truncation ตามจริง
- [ ] วัด p50/p95 ของ token และเวลาทั้ง pipeline กำหนด concurrency จาก CPU/RAM/disk และ throughput KKU จริง ไม่เพิ่มตามจำนวน keys โดยอัตโนมัติ
- [ ] ตรวจ source/tests ไม่ว่าง fixed ผ่านสองครั้ง buggy/coverage วัดได้ และ artifacts ครบ ไม่ตรวจเฉพาะ summary
- [ ] เก็บ executed/skipped counts และหลักฐานจาก source/control flow หรือ runtime ว่า checks เรียก intended target จริง; ระบุ weak oracles เช่นตรวจ type/void/constructor อย่างเดียว ไม่บังคับ buggy fail หรือบังคับ coverage ผ่านเปอร์เซ็นต์ที่ตั้งเอง
- [ ] Primary AI หนึ่ง generation request/job; ไม่มี semantic regeneration หรือเลือกคำตอบที่ดีที่สุด ข้อผิดพลาดชั่วคราวที่ยืนยัน reject ก่อนประมวลผล retry ได้สูงสุด 2 ครั้ง; timeout/5xx ที่ outcome ไม่ทราบเก็บ needs_reconciliation ห้ามส่งซ้ำอัตโนมัติ แชมป์ตรวจ body/request ID/usage แล้วออมบันทึกข้อสรุป
- [ ] Evaluation infrastructure retry ได้หนึ่งครั้งด้วย suite hash เดิมเท่านั้น ไม่ retry เพื่อเปลี่ยน observed buggy failures/fixed-invalid outcome; เก็บ raw evaluator status invalid/partial/failed/complete กับ normalized stage outcomes แยก และรักษา metrics ที่วัดได้แม้ coverage ล้มเหลว

**Gate B:** ทั้งสามคนตรวจรับ protocol/model IDs/pilot validity พร้อม resource readiness และ deadline feasibility จาก actual tokens/remaining quotas/throughput ที่ concurrency จริง ต้องคำนวณว่างานค้างและ tail เหลือเวลาพอถึง evaluation cutoff 5 ต.ค. 03:00 หรือไม่ หากไม่พอแจ้งผู้ใช้ทันทีและส่งแผน partial ตามจริง ไม่ผ่าน gate จากจำนวนบัญชี/เครื่องเพียงอย่างเดียว หากแก้ protocol เก็บ pilot เก่าแยก condition; ถ้า protocol คงเดิม pilot รวมใน 854 ได้

## 4. ระยะ C — กระจายรัน / ชั่วโมง 8–42 (3 ต.ค. 13:00 ถึง 4 ต.ค. 23:00)

หลัง Gate B ออม upsert full manifest ให้มี 3,416 unique jobs ถ้า protocol คงเดิมให้เก็บ80pilot keys/results และเพิ่ม3,336งานที่เหลือ ไม่ recreate pilot work แล้วแยก bugs เป็นสาม shard แบบ deterministic กระจายตาม project/estimated runtime เพื่อไม่ให้คนหนึ่งได้แต่ project หนัก:

- **แชมป์:** shard A 285 bugs ดูผลทั้งสี่วิธี + ดูแล API coordinator
- **บีม:** shard B 285 bugs ดูผลทั้งสี่วิธี + ดูแล evaluation coordinator
- **ออม:** shard C 284 bugs ดูผลทั้งสี่วิธี + ดูแล queue/analysis coordinator

รายการเจ้าของแต่ละ project/bug ที่ระบุจริง: `docs/api854/BUG_OWNERSHIP_20261003_TH.md` และ machine-readable `experiments/configs/api854-20261003/ownership.json` ใช้รายการนี้ก่อนแจกงาน ไม่มี bug ซ้ำระหว่างสามคน และทุกคนมี bugs จากทั้ง 17 projects จำนวนสมดุลแต่เวลาอาจต่างกัน ให้ปรับหลัง pilot ผ่าน reassignment event

shard คือเจ้าของตรวจรับ ไม่ใช่การแบ่ง quota ล็อกกับคน จำนวนเครื่อง/บัญชีกระจายจากคิวกลาง และ reassignment ต้องมี event/lease เพื่อไม่ทำงานซ้ำ

- [ ] รัน algorithms ล่วงหน้าและให้ API generation/evaluation ซ้อนกัน แจกงานตาม readiness ไม่รอ batch ทั้งหมดเสร็จก่อนทดสอบ
- [ ] เริ่มหนึ่ง CPU-heavy slot ต่อเครื่องที่พร้อม ไม่ถือว่า checkout/algorithm/evaluation มีโควตา CPU แยกกัน เพิ่มเป็นสอง slot เฉพาะเครื่องที่ pilot ยืนยัน CPU/RAM/disk และ runtime คงที่ ไม่มีการอ้างว่า 6 เครื่องเท่ากับ 64 workers
- [ ] API ใช้ global limiter ตามบริการ KKU และ per-bucket reservations เก็บ retry-after/backoff ไม่มีหลักฐานว่าเพิ่มบัญชีแล้ว throughput เพิ่มเท่าตัวเสมอ
- [ ] ทุก 2 ชั่วโมง export matched-usable bugs/854 และ terminal outcomes/3,416 แยกกัน พร้อม backlog/quota/ETA
- [ ] งานติดเกิน 30 นาทีหรือพบหลาย bugs ใช้สาเหตุเดียวกัน ส่งเข้า shared blocker queue: แชมป์ดู source/context, บีมดู environment/runner, ออมดู metadata/protocol
- [ ] worker อื่นรัน jobs อิสระต่อได้ แต่ shard/adapter ที่มี systematic failure ต้องหยุดก่อนเสีย token/เวลาซ้ำ
- [ ] หลังแก้ blocker ตรวจตัวแทนซ้ำก่อนปล่อย family นั้นกลับคิว ไม่ปล่อย batch ทั้ง family จากการแก้ที่ยังไม่ได้ยืนยัน

## 5. ระยะ D — ปิดงานค้างและ reset รอบสุดท้าย / ชั่วโมง 42–44 (4 ต.ค. 23:00 ถึง 5 ต.ค. 01:00)

- [ ] หยุดเพิ่ม condition/model ใหม่ เน้น bugs ที่ขาดเพียงหนึ่งหรือสองวิธีให้ matched set สมบูรณ์
- [ ] แชมป์ตรวจ API refusal/truncation/source provenance; บีมตรวจ compile/environment/flaky/coverage; ออมตรวจ duplicate/missing metadata/ภาพประกอบและ counts
- [ ] สำหรับ difficult jobs ใช้สามส่วนตรวจพร้อมกันก่อนตัดสิน retry โดยไม่ส่ง logs เพิ่มให้ KKU
- [ ] หากยังไม่ใช้งานได้เก็บ failure reason กับ attempts จริง ไม่ตัด assertions ให้ suite ผ่านหรือเพิ่มผลที่ไม่ได้รัน
- [ ] 5 ต.ค. 00:00 ตรวจ reset ครั้งที่สองจริงก่อนใช้ window วันที่สาม แจ้งก่อนสลับบัญชีทุกครั้ง; เริ่มงานใหม่หลัง reset เฉพาะ job ที่ตาม runtime pilot มีเวลาพอ drain ถึง 03:00 มิฉะนั้น deferred ไม่เรียกว่าส่งทดลองแล้ว
- [ ] หยุดส่ง generation requests ใหม่และหยุด claim target preparation/checkout/algorithm generation ใหม่ 5 ต.ค. 01:00 จากนั้นรับ evaluation ของ suite ที่พร้อมและ reconciliation เท่านั้น มีรายการงานที่ยังไม่ส่ง/ไม่ทราบผลแยกต่างหาก

**Gate D:** มี artifact/status ของทุก job ที่ประมวลผล ผลค้างระบุชัด actual models/conditions แยกครบ และมีรายการ unresolved สำหรับรายงาน

## 6. ระยะ E — Drain, freeze และส่ง / ชั่วโมง 44–48 (5 ต.ค. 01:00–05:00)

- [ ] ออม freeze inventory/protocol/records hashes และสร้าง report/slides/ZIP จาก snapshot เดียว
- [ ] 01:00–03:00 drain evaluation/reconciliation ที่ยังค้าง; freeze queue/report snapshot 03:00 พร้อม not_attempted/deferred/unknown items ตามจริง ปิด claims ใหม่และแยกlate artifactsจากsnapshotนั้น 03:00–05:00ตรวจและแพ็กส่ง ห้ามแก้ผลในsnapshotหลังfreezeโดยไม่เพิ่มversion
- [ ] ออมร่าง report/slides/evidence index และสร้าง package skeleton ตั้งแต่ระยะ B/C ไม่รอทำเอกสารทั้งหมดหลัง 03:00
- [ ] แชมป์ตรวจ evidence chain และ secrets; บีมตรวจ reproducibility/demo และผล fixed/buggy/coverage
- [ ] ทั้งสามคนตรวจยอด bugs, suites, attempts, test methods และ failures ไม่ปะปนกัน
- [ ] รายงานความครบของ 17 projects, matched usable bugs/854, attempted keys/3,416 และข้อจำกัด models/prompts/repairs
- [ ] ตรวจ ZIP แตกได้/ไฟล์อ่านได้ README ชี้ START_HERE รุ่นสุดท้าย และช่องทางส่งตามที่ผู้ใช้มอบหมาย

## 7. กติกา token หมดและการสลับบัญชี

สถานะบัญชี/โมเดล: active → quota_exhausted/insufficient_budget → waiting_notification → switch_ready → active_on_next_account

1. ก่อนส่ง reserve ตาม prompt estimate + max output หาก remaining ไม่พอ ไม่เริ่ม request ใหม่ใน bucket นั้น
2. เมื่อพบ daily limit ต้องจำแนกจาก body/quota ไม่ใช่ HTTP 401 อย่างเดียว เพราะ invalid key/model ก็อาจเป็น 401
3. บันทึก account alias/model/remaining/reset time/jobs affected แล้วแจ้งผู้ใช้ในช่องงานที่เห็นได้ ก่อนสลับ credentials
4. ข้อความแจ้ง: “บัญชี aXX สำหรับ [model] โควตาหมด/ไม่พอ เหลือ [N] tokens งานค้าง [M] รายการ จะสลับไป aYY รุ่นเดิม ส่วนงานรันทดสอบดำเนินต่อ” ห้ามแสดง keys/email
5. ห้ามส่ง request จาก aYY ก่อน notification ถูกส่งสำเร็จ หากแจ้งไม่ได้ให้ pause bucket นั้น ระบบอื่นทำต่อได้
6. ผู้ใช้ขอให้แจ้งก่อน ไม่ได้ขอให้รออนุมัติทุกครั้ง จึงสลับได้หลังแจ้งสำเร็จ แต่ถ้าผู้ใช้สั่งหยุด/เลือกบัญชีใหม่ต้องทำตาม
7. สลับบัญชีไม่เปลี่ยน model/protocol งานเดิมเก็บ attempts/usage เดิมไม่ reset ประวัติ เมื่อ server reset ให้เปิด window ใหม่และยืนยัน remaining จริง
8. หากใช้ unattended runner ให้ทำ event queue ที่ Codex/ผู้ดูแลอ่านแล้วแจ้งผู้ใช้ก่อน operator เปิดใช้บัญชีถัดไป ไม่พึ่ง console log ที่ผู้ใช้ไม่ได้เห็น และไม่เพิ่มการส่งไป Slack/email โดยไม่ได้รับมอบหมาย

## 8. ทรัพยากรจริงและการประเมินเป้าหมาย

มี 10 บัญชี ไม่ใช่ 100 และ 6 เครื่อง ไม่ใช่ 64 workers:
- เครื่องแชมป์ 1: API/quota coordinator และ generation workers (I/O); เพิ่ม CPU-heavy slot ได้หาก pilot รองรับ
- เครื่องบีม 1–3: target preparation/algorithm generation/evaluation โดยแชร์ CPU-heavy slots
- เครื่องออม 1: queue/artifact server/progress/report/package; อย่าให้ build หนักแย่งทรัพยากรจน controller ล่ม
- เครื่องออม 2: algorithm/evaluation worker

เริ่ม 4 CPU-heavy slots บนบีมสามเครื่องและออม 2 เพิ่มบนแชมป์/ออม 1 หรือเพิ่ม slots ต่อเครื่องหลังวัดเท่านั้น บันทึก OS/CPU/RAM/Java11/Defects4J/network/disk ก่อน live pilot

05:00 วันที่ 3 ถึง 05:00 วันที่ 5 ครอบคลุม 3 calendar quota windows โดย windowsแรกอาจมี usage เดิม และ windowสุดท้ายมีเวลาส่งเพียง 00:00–01:00 จึงห้ามถือว่าใช้โควตาวันที่สามได้หมดแน่นอน

ถ้าทุกบัญชีเริ่มเต็มและใช้ได้ทั้งสาม windows: Claude สูงสุด 6,000,000 tokens, Gemini 10,500,000; สำรอง20% เหลือ4,800,000/8,400,000 ค่าเฉลี่ย billed tokens รวมทุก attempts ต่อ bug ต้องไม่เกินประมาณ Claude5,621 และ Gemini9,836 เพื่อรองรับ854 requests/บริการ ถ้าใช้ได้เพียงสอง windows เงื่อนไขเหลือ Claude3,747/Gemini6,557 tokens/bug ตัวเลขนี้เป็น aggregate ceiling ต้องเผื่อ per-account packing, context limits, latency และ usage จริง

Gate B ใช้ cumulative predicted prompt+output ของทุก job กับ actual remaining ในแต่ละ window ตรวจว่าจำเป็นต้องฝากงานไว้ reset สุดท้ายกี่งานและทำได้ในหนึ่งชั่วโมงจริงหรือไม่ หากไม่พอให้แจ้งจำนวนบัญชีเพิ่ม/โควตาเพิ่มที่คำนวณจากpilot หรือขอบเขตที่ทำได้ตามจริง ไม่ลด context จนเทสไม่มีความหมายและไม่ทำ refusal จงใจเพื่อเติมยอด

ตัวอย่าง throughput: ถ้า80pilot jobsมีoutcomesแล้ว เหลือ3,336keys และ T8ถึงT46=38ชั่วโมง ต้องทำเฉลี่ยประมาณ88 outcomes/ชั่วโมง พร้อมเผื่อ tail และ failures; วัดทั้ง generation/setup/algorithm observations/evaluation เพราะ budget30 ของ algorithms มีfixed observationsสองครั้งต่อproposal เวลา20นาที/keyที่6slotsใช้ประมาณ190ชั่วโมงสำหรับ3,416keysในอุดมคติ จึงต้องวัดก่อนเชื่อว่า6เครื่องทำทัน

ทีมพร้อมช่วยตามที่ผู้ใช้ยืนยัน ให้สลับผู้เฝ้าคิว/notification ทุก4ชั่วโมงและใช้ alerts/checkpoints แทนการเฝ้าหน้าจอพร้อมกันตลอด48ชั่วโมง หากGate Bไม่ผ่านใน8ชั่วโมงต้องแจ้งETA/ข้อจำกัดใหม่

## 9. Branch และเจ้าของไฟล์

- แชมป์ `champ`: API client/quota/generation/context extraction
- บีม `beam`: target adapters/algorithms/evaluator/environment
- ออม `aom`: inventory/protocol/queue/analysis/report/package
- ใช้ directories ในแผน API วันที่ 2026-10-02 เป็น file map ก่อนเพิ่มชื่อใหม่ ต้องตกลง interface ใน schema กลาง ห้ามให้สองคนแก้ queue/protocol เดียวกันพร้อมกัน
- ถ้าแบ่ง Codex subtasks ให้ใช้ worktree ของ branch เจ้าของแล้วแตก sub-branch แยกตาม blocker ไม่ใช้ worktree Defects4J ร่วมกัน; owner review/merge ก่อน integration
- ออมเป็นผู้รวมเข้า test หลังตรวจรับ ทั้งสาม branch ต้องใช้ plan revision เดียวกัน เก็บ raw/results ตาม immutable attempts ไม่ merge SQLite runtime/credentials

## สิ่งที่ส่งต่อให้แต่ละคนเริ่มทันที

แชมป์เริ่ม A1 context + A2 API; บีมเริ่ม A1 adapter inventory + A2 evaluator; ออมเริ่ม A1 policy + A2 queue ทั้งสามรวม A3 Closure/JxPath แล้วผ่าน Gate A ก่อน pilot เมื่อเจองานยากใช้ shared blocker queue ตามระยะ C ไม่รอให้เจ้าของแก้ลำพัง

แผนนี้เป็นเอกสารเตรียมงาน ยังไม่ได้สร้าง runner แจกบัญชี หรือเริ่ม 48-hour run
