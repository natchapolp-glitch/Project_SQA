# SQA 854 Bugs — Collaborative 48-Hour Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:executing-plans. งานอิสระในแต่ละระยะทำ parallel ได้ แต่ต้องผ่านจุดตรวจรับร่วมก่อนเริ่มระยะถัดไป แชมป์ บีม ออมใช้ Codex ใน branch ของตนเอง

**Goal:** ภายในกรอบ 48 ชั่วโมง ประเมิน active bugs 854 รายการด้วย CMA-ES, FSCS-ART, KKU Claude และ KKU Gemini รวม 3,416 job keys และส่งผลพร้อมหลักฐานโดยแยกความสำเร็จและความล้มเหลวตามจริง

**Architecture:** เริ่มจากทีมสามคนร่วมแก้คอขวดเรื่อง target adapters, API/quota และ queue contract จากนั้นทดสอบ pipeline ครบสี่วิธี ก่อนกระจายงานแบบ shard ที่ไม่ซ้ำกัน ใช้คิวกลางที่คงสถานะและเก็บ artifacts อัตโนมัติ ส่วน difficult jobs ถูกส่งกลับให้ทั้งสามคนช่วยกันแทนการปล่อยให้คนเดียวติดงาน

**Tech Stack:** Defects4J 3.0.1, Java/JUnit, Python runners, KKU IntelSphere API, transactional queue, isolated worktrees/containers, Codex และ Git

**Spec:** ข้อ 2.2 ใน `C:/Users/ACER/Downloads/SQA_Project_2026 (2).pdf`; คำขอวันที่ 2026-10-03 ให้ทำ 854 bugs ด้วยสามคน แตกงานยากเป็น parallel ช่วยกันก่อนทำขั้นถัดไป และแจ้งก่อนสลับบัญชี KKU เมื่อ tokens หมด

## Global Constraints

- แผนนี้แทนการแบ่งงานแบบเจ้าของส่วนเดียวตลอดโครงการ ใช้เจ้าของไฟล์เพื่อลด conflict แต่ทุกคนช่วยแก้คอขวดร่วมกัน
- 854 bugs × 4 วิธี × 1 รอบ = 3,416 jobs; retries เป็น attempts ของ job เดิม ไม่เพิ่มจำนวน bugs
- จำนวน active bugs/17 projects ต้องยืนยันจาก installed Defects4J และตรึง hash ก่อนส่ง API; ไม่รวม deprecated
- attempted ครบ 3,416 กับ usable ครบ 3,416 เป็นคนละเกณฑ์ ไม่มีการรับประกันว่า AI จะสร้างเทสสำเร็จทุก bug ใน 48 ชั่วโมง
- ใช้ Claude/Gemini ผ่าน KKU API เท่านั้นสำหรับผล AI สองวิธี; Codex ช่วย orchestration/runner/review หากแก้เนื้อหา AI tests ต้องเปิดเผย ไม่แอบนับเป็นคำตอบดิบของ KKU
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

T0 คือเวลาที่ทีมเริ่มตามแผนจริง เก็บ timestamp UTC และ Asia/Bangkok ลง run metadata ไม่ถือว่าเริ่มรันแล้วจากการเขียนแผน

เสร็จระดับการทดลอง: มี outcome และหลักฐานในทุก 3,416 job keys รวม generation_failed/refused/compile_failed/environment_failed ที่ต้องระบุแยก

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

### A3. งานยากเฉพาะ Closure และ JxPath

- [ ] **แชมป์:** ตรวจ fixture/context/prompt และ provenance คำตอบ AI
- [ ] **บีม:** ตรวจ build/test framework และ assertion execution พร้อม fixed/buggy/coverage
- [ ] **ออม:** ตรวจ exclusion/repair policy และบันทึก raw/processed differences เป็นเงื่อนไขทดลอง
- [ ] ยืนยันว่า suite ที่มี compiler=null แล้ว return ก่อน assert ไม่ถูกนับ usable แม้ compile ผ่าน

**Gate A:** pipeline ขั้นพื้นฐานทำงานครบ 4 วิธี, queue resume ได้, quota notification ใช้ได้, adapter gaps มีเจ้าของและทางแก้ที่ตรวจได้ ถ้า gate ติด ให้ทั้งสามคนหยุดเริ่ม feature ใหม่และช่วยแก้ blocker

## 3. ระยะ B — Pilot ร่วมกัน / ชั่วโมง 4–8

- [ ] ตรึง 20 pilot bugs ล่วงหน้าให้ครอบคลุมทั้ง 17 projects รวม Closure/JxPath/context ใหญ่ รวม 80 jobs
- [ ] แชมป์ดู API token/latency/error; บีมดู generation/evaluation/runtime; ออมดู counts/hash/restart/model fairness
- [ ] ใช้ mock checks สำหรับ duplicate claims, interrupted requests, quota exhaustion, notification before switch, auth failure และ midnight boundary ก่อน live pilot
- [ ] ตรวจ actual quota scope และ remaining ของบัญชี; ตรวจรุ่นโมเดลตรงทุกบัญชี เก็บ refusal/truncation ตามจริง
- [ ] วัด p50/p95 ของ token และเวลาทั้ง pipeline กำหนด concurrency จาก CPU/RAM/disk และ throughput KKU จริง ไม่เพิ่มตามจำนวน keys โดยอัตโนมัติ
- [ ] ตรวจ source/tests ไม่ว่าง fixed ผ่านสองครั้ง buggy/coverage วัดได้ และ artifacts ครบ ไม่ตรวจเฉพาะ summary

**Gate B:** ทั้งสามคนตรวจรับ protocol และ pilot จากหลักฐาน หากต้องแก้ protocol เก็บ pilot เก่าแยก condition; ถ้า protocol คงเดิม pilot รวมใน 854 ได้

## 4. ระยะ C — กระจายรัน / ชั่วโมง 8–36

หลัง Gate B ออมสร้าง 3,416 jobs และแยก bugs เป็นสาม shard แบบ deterministic กระจายตาม project/estimated runtime เพื่อไม่ให้คนหนึ่งได้แต่ project หนัก:

- **แชมป์:** shard A 285 bugs ดูผลทั้งสี่วิธี + ดูแล API coordinator
- **บีม:** shard B 285 bugs ดูผลทั้งสี่วิธี + ดูแล evaluation coordinator
- **ออม:** shard C 284 bugs ดูผลทั้งสี่วิธี + ดูแล queue/analysis coordinator

รายการเจ้าของแต่ละ project/bug ที่ระบุจริง: `docs/api854/BUG_OWNERSHIP_20261003_TH.md` และ machine-readable `experiments/configs/api854-20261003/ownership.json` ใช้รายการนี้ก่อนแจกงาน ไม่มี bug ซ้ำระหว่างสามคน และทุกคนมี bugs จากทั้ง 17 projects จำนวนสมดุลแต่เวลาอาจต่างกัน ให้ปรับหลัง pilot ผ่าน reassignment event

shard คือเจ้าของตรวจรับ ไม่ใช่การแบ่ง quota ล็อกกับคน จำนวนเครื่อง/บัญชีกระจายจากคิวกลาง และ reassignment ต้องมี event/lease เพื่อไม่ทำงานซ้ำ

- [ ] รัน algorithms ล่วงหน้าและให้ API generation/evaluation ซ้อนกัน แจกงานตาม readiness ไม่รอ batch ทั้งหมดเสร็จก่อนทดสอบ
- [ ] เริ่ม evaluation 2 workers/เครื่อง ปรับเพิ่มหลังวัด ตั้งเป้า 64 workers รวมเมื่อเครื่องและ runner รองรับจริง
- [ ] API ใช้ global limiter ตามบริการ KKU และ per-bucket reservations เก็บ retry-after/backoff ไม่มีหลักฐานว่าเพิ่มบัญชีแล้ว throughput เพิ่มเท่าตัวเสมอ
- [ ] ทุก 2 ชั่วโมง export matched-usable bugs/854 และ terminal outcomes/3,416 แยกกัน พร้อม backlog/quota/ETA
- [ ] งานติดเกิน 30 นาทีหรือพบหลาย bugs ใช้สาเหตุเดียวกัน ส่งเข้า shared blocker queue: แชมป์ดู source/context, บีมดู environment/runner, ออมดู metadata/protocol
- [ ] worker อื่นรัน jobs อิสระต่อได้ แต่ shard/adapter ที่มี systematic failure ต้องหยุดก่อนเสีย token/เวลาซ้ำ
- [ ] หลังแก้ blocker ตรวจตัวแทนซ้ำก่อนปล่อย family นั้นกลับคิว ไม่ปล่อย batch ทั้ง family จากการแก้ที่ยังไม่ได้ยืนยัน

## 5. ระยะ D — ทั้งสามคนปิดงานค้าง / ชั่วโมง 36–42

- [ ] หยุดเพิ่ม condition/model ใหม่ เน้น bugs ที่ขาดเพียงหนึ่งหรือสองวิธีให้ matched set สมบูรณ์
- [ ] แชมป์ตรวจ API refusal/truncation/source provenance; บีมตรวจ compile/environment/flaky/coverage; ออมตรวจ duplicate/missing metadata/ภาพประกอบและ counts
- [ ] สำหรับ difficult jobs ใช้สามส่วนตรวจพร้อมกันก่อนตัดสิน retry โดยไม่ส่ง logs เพิ่มให้ KKU
- [ ] หากยังไม่ใช้งานได้เก็บ failure reason กับ attempts จริง ไม่ตัด assertions ให้ suite ผ่านหรือเพิ่มผลที่ไม่ได้รัน

**Gate D:** มี artifact/status ของทุก job ที่ประมวลผล ผลค้างระบุชัด actual models/conditions แยกครบ และมีรายการ unresolved สำหรับรายงาน

## 6. ระยะ E — ตรวจรับและส่ง / ชั่วโมง 42–48

- [ ] ออม freeze inventory/protocol/records hashes และสร้าง report/slides/ZIP จาก snapshot เดียว
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

## 8. ทรัพยากรและการประเมินเป้าหมาย

10 บัญชีเดิมไม่พอให้รับประกัน 854 bugs ใน 48 ชั่วโมง จัดจำนวนบัญชีจาก pilot ไม่เลือกตัวเลขโดยเดา:

accounts_C = ceil(854 × mean_tokens_C × retry_factor / (200,000 × 0.8 × usable_quota_windows))
accounts_G = ceil(854 × mean_tokens_G × retry_factor / (350,000 × 0.8 × usable_quota_windows))

สมมติ 20,000 tokens/ชุด retry_factor 1.5 และสอง quota windows ที่เริ่มเต็ม: Claude 81 บัญชี Gemini 46; pool 100 บัญชีที่ใช้ทั้งสองโมเดลได้รองรับ token ตามสมมติฐาน ถ้า 30,000 tokens/ชุด Claude 121 บัญชี ใช้ประมาณ 130 เป็น resource envelope ไม่ใช่การรับประกัน throughput

64 evaluation workers ทำ 3,416 jobs เฉลี่ย 20 นาที/งานประมาณ 17.8 ชั่วโมงในอุดมคติ ต้องบวก checkout/cache/timeout/retries/stragglers ที่วัดจริง ไม่ถือว่าระบบเดิมรองรับ 64 workers แล้ว

เวลาพัฒนาไม่ถูกนับเป็นศูนย์เพราะใช้ Codex: ลดได้ด้วยงานอิสระ/worktrees แต่ integration/adapters/validity gates ยังต้องผ่าน หาก Gate B ไม่ผ่านใน 8 ชั่วโมงต้องแจ้ง ETA ใหม่และผลที่คาดได้ ไม่รับรองเสร็จ 100% ล่วงหน้า

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
