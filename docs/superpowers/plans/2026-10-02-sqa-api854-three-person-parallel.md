# SQA API 854 Bugs — Three-Person Parallel Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement task by task. แชมป์ บีม ออมทำพร้อมกันใน branch ของตนเอง แผนนี้แทนขอบเขตเดิม 17 bugs

**Goal:** ประเมิน 854 active bugs จาก 17 projects ด้วย CMA-ES, FSCS-ART, KKU Claude และ KKU Gemini อย่างละหนึ่งรอบ รวม 3,416 งาน พร้อมผลและหลักฐานที่ตรวจสอบย้อนกลับได้

**Architecture:** แยกคิวสร้างเทส API ออกจากคิวรัน Defects4J ใช้ฐานข้อมูลคิวกลางและ ledger โควตาเพื่อไม่ให้งานซ้ำ แชมป์ดูแล API/บัญชี บีมดูแลอัลกอริทึม/การรัน ออมดูแล manifest/การวิเคราะห์/รายงาน งานเริ่มเป็น pilot ก่อนแล้วจึงขยายด้วย protocol เดียวกัน

**Tech Stack:** KKU IntelSphere API, Python, SQLite, Java/JUnit, Defects4J 3.0.1, existing study runners, Git

**Spec:** Assignment `C:/Users/ACER/Downloads/SQA_Project_2026 (2).pdf` ข้อ 2.2; ผู้ใช้ขยายขอบเขตเป็น 854 bugs ครบ 4 วิธี และให้ 10 บัญชี โดย Claude 200,000 และ Gemini 350,000 tokens/บัญชี/วัน รีเซ็ตเที่ยงคืน

## Global Constraints

- เริ่มหนึ่งรอบต่อ bug/วิธี: algorithms 1,708 งาน, AI 1,708 งาน; ไม่นับ retries เป็น bugs หรือผลอิสระเพิ่มเติม
- ต้องแยก attempted, generation-valid, compile-valid, fixed-valid, evaluated และ fault-detected การพยายามครบ 3,416 ไม่เท่ากับได้ usable suites ครบ 3,416
- ตรวจ local active-bugs manifest แล้วพบ 854 entries และ 17 projects; ก่อนรันยืนยันกับ installed Defects4J อีกครั้ง ตัด deprecated ออกและตรึง inventory/hash
- คงผลหลักเก่า 185/204 เป็น historical dataset ห้ามรวมเข้าชุด API ใหม่อัตโนมัติ เพราะ model/prompt/protocol อาจต่างกัน
- ส่งเฉพาะ fixed source/API/build context ที่เกี่ยวกับงานและ prompts ห้ามส่ง execution/compile logs กลับไป AI ตามข้อจำกัดของผู้ใช้เดิม
- credentials เก็บเฉพาะ environment/secret file ที่ถูก ignore ใช้ account alias a01–a10 ในผล ไม่เก็บ key/header/email ลง Git หรือ ZIP
- ใช้บัญชีและ quota ที่มีสิทธิ์ใช้งานจริง หากบัญชีแชร์ quota กลางให้รวมเป็น quota bucket เดียว ไม่ถือว่า 10 keys เท่ากับ 10 โควตา
- โควตาและเวลา reset ที่ผู้ใช้ให้เป็นค่าตั้งต้น ต้องตรวจ response จริง; timezone ตั้งต้น Asia/Bangkok ตามบริบท แต่เอกสาร KKU ระบุเพียง reset ทุกวัน ไม่ระบุ timezone
- ผลล้มเหลว/refusal/timeout เป็นผลทดลองที่ต้องเก็บ ไม่เปลี่ยนถ้อยคำหลอก AI และไม่ทำ assertions ว่างเพื่อให้ผ่าน
- เปลี่ยนจากภาพแชททุกคำขอเป็น API request/response/usage provenance; เก็บภาพหน้าการตั้งค่า/usage ที่ปิดข้อมูลส่วนตัวประกอบ ตรวจ rubric ว่าต้องมีภาพเพิ่มเติมหรือไม่
- แผนนี้ยังไม่ใช่ API runner ที่ทำงานแล้ว ไม่ได้ส่งคำขอหรือใช้ token ในการจัดทำแผน

## Review Focus

1. quota หมดหรือ 401 คนละสาเหตุ: แชมป์จำแนก auth, invalid model และ daily limit จาก body ไม่ retry วนจาก status อย่างเดียว
2. process หยุดหลังส่ง API: แชมป์และออมบันทึก attempt ก่อนส่งและหลังรับ แยก unknown outcome ป้องกันส่งซ้ำโดยไม่จำเป็น
3. tests ผ่านแต่ไม่ได้ทดสอบจริง: บีมตรวจ fixture, skipped/empty methods, assertions และ coverage โดยเฉพาะ Closure
4. หลาย worker เขียนข้อมูลหรือใช้ quota ซ้ำ: ออมทำ atomic claims/leases แชมป์ reserve tokens ก่อนส่ง บีมใช้ Defects4J worktree แยก
5. ผลต่างเพราะ model/budget/target เปลี่ยน: ออมตรึง protocol และวิเคราะห์ fallback/compatibility repairs แยก ไม่เลือกผลที่ดีที่สุดย้อนหลัง

## 1. API ที่ตรวจจากเอกสาร KKU

แหล่งข้อมูล: https://gen.ai.kku.ac.th/docs/api อ่านผ่าน browser เมื่อ 2026-10-02

- Base URL: `https://gen.ai.kku.ac.th/api/v1`
- `GET /models` ดึง model mapping; `POST /chat/models-list` มี id/name
- ใช้ `POST /chat/completions` เป็น interface กลางทั้ง Claude/Gemini, Authorization Bearer จาก secret environment, `stream=false`
- response มี `id`, `model`, `provider`, `choices`, `usage.prompt_tokens`, `usage.completion_tokens`, `usage.total_tokens` และ `model_quota.daily_quota_tokens/daily_usage_tokens/daily_remaining_tokens`
- เอกสาร request table ระบุ model เป็น numeric ID แต่ตัวอย่างใช้ string name: ให้ resolve ID จากรายการจริงและตรวจ pilot ก่อน ไม่ hardcode ID จากตัวอย่าง
- ตัวอย่าง quota เท่ากับ input+output แต่หากมี thinking/cache ต้องตรวจ usage กับ quota delta จริงก่อนใช้สูตรประมาณ; ไม่สมมติว่าตัวเลขทุก field บวกกันได้โดยไม่ซ้ำ
- เอกสารระบุ daily limit เป็น HTTP 401 พร้อมข้อความ `This model reached daily limit.` ต่างจาก Invalid API key/Invalid model ซึ่งก็เป็น 401
- ไม่พบ quota-only endpoint ในหน้านี้: ใช้ response quota + ledger และค่าคงเหลือจากเจ้าของบัญชี ไม่แต่ง endpoint เพิ่ม

## 2. โควตาและประมาณเวลาที่ใช้

ค่าจากผู้ใช้: Claude 10×200,000 = 2,000,000 tokens/วัน; Gemini 10×350,000 = 3,500,000 tokens/วัน เป็นคนละ pool ห้ามโอนรวมเป็น quota Claude

ตั้ง reserve 20% สำหรับ retry/ความคลาดเคลื่อน ได้ budget generation ปกติ Claude 1,600,000 และ Gemini 2,800,000 tokens/วัน เมื่อทุกบัญชีเริ่มเต็ม วันนี้ถ้าใช้ไปแล้วให้ใช้ remaining จริง

ให้ T_C/T_G เป็น token เฉลี่ยต่อ suite ตามการนับ quota จริง รวม system, fixed source, prompt, output; คำนวณทุก attempt เพิ่มใน ledger

สูตรขั้นต่ำจาก token ของแต่ละ pool: ceil(854×T_C/1,600,000), ceil(854×T_G/2,800,000); ระยะรวมขึ้นกับ pool ที่ช้ากว่า รวม latency/rate limit/evaluation ด้วย

ตัวอย่างสมมติ T_C=T_G (ยังไม่ใช่ค่าที่วัดจริง):
- 5,000 tokens/suite: Claude 3 quota windows, Gemini 2
- 10,000 tokens/suite: Claude 6 quota windows, Gemini 4
- 20,000 tokens/suite: Claude 11 quota windows, Gemini 7
- 30,000 tokens/suite: Claude 17 quota windows, Gemini 10

นี่เป็นขั้นต่ำแบบ aggregate; prompt ใหญ่, account packing, shared quotas, refusals/retries และ throughput อาจเพิ่มเวลา quota windows ไม่เท่ากับจำนวนวันเต็ม 24 ชั่วโมง เพราะเริ่มก่อน reset ได้ และต้องรู้ remaining จริง

งาน evaluation ตัวอย่าง: ถ้าเฉลี่ย 80 วินาที/งานทั้ง fixed repeats+buggy+coverage จะใช้ 3,416×80/3,600 = 75.9 worker-hours; หาก 180 วินาทีใช้ 170.8 worker-hours ก่อนเผื่อ timeout/setup ด้วย 8 workers ที่เครื่องรองรับจริงประมาณ 9.5–21.4 ชั่วโมง ไม่ใช่หลักฐานว่าเครื่องปัจจุบันทำได้ 8 workers

แผนเวลา: พัฒนาและ pilot 1–2 วันเป็นค่าประมาณ จากนั้นกำหนด ETA จาก usage/latency/evaluation p50,p95 ที่วัดจริง หากใช้เฉลี่ย 10k–20k tokens ต่อ suite และ quota แยกเต็มจริง คาด 6–11 quota windows สำหรับ generation และเผื่อ QA/รายงานอีก 0.5–1 วัน โดยรัน evaluation ซ้อนระหว่าง generation ห้ามรับประกันเสร็จในคืนนี้

## 3. Protocol และคิวกลาง — interface ที่ทั้งสามคนใช้

สร้าง `experiments/configs/api854-20261002/{bugs.json,protocol.json,accounts.example.json}` และ runtime ที่ถูก ignore `.local/api854/state.sqlite`; ชื่อไฟล์เหล่านี้ยังเป็นงานตามแผน

ออมตรึง job key = `(project, bug_id, approach, protocol_hash, repeat_index=1)` ให้มี UNIQUE constraint; retries เก็บ attempt index แยก ห้ามเพิ่ม job keys

API requests ต้องมี model version ที่เลือกจากรายการจริง แนะนำ Haiku คงที่สำหรับ primary Claude เพื่อลดปัญหา busy; การเปลี่ยนจาก Sonnet-first เดิมเป็นข้อเสนอใน protocol ที่ต้องตกลงก่อน pilot Gemini ต้องกำหนดรุ่นเดียวกันทุกบัญชี ห้าม fallback เงียบ

กำหนดก่อน pilot: fixed-class/API selection policy เดียวกันทุกวิธี, generator seed 101, per-suite test-method cap 30 ตามรูปแบบเดิม, algorithm search budget/config เดิมที่ตรวจแล้ว, temperature และ max output จากข้อจำกัดโมเดลจริง ไม่มีการตั้งค่าที่ model ไม่รองรับ

AI ใช้ fresh request ต่อ job ไม่แนบ chat history ทั้งหมด การลด prompt ต้องมีนโยบายคงที่และ context เพียงพอ ไม่เลือก target จากผล fault detection ที่เห็นแล้ว

สถานะ: queued → claimed → generating/generated → evaluating → complete; terminal failures เก็บ generation_failed/compile_failed/fixed_failed/environment_failed/refused พร้อม attempts หาก HTTP outcome ไม่ทราบใช้ needs_reconciliation ห้ามเทียบเป็น generation_failed อัตโนมัติ

Export contract: `results/study/api854-20261002/<project>/<bug_id>/<approach>/attempt-<n>/` มี prompt/request ที่ปิด credentials, raw response, source, suite, generation metadata, evaluation record/logs ตาม adapter ของ runner เดิม

Metadata ต้องมี run/job/attempt IDs, timestamps UTC, account alias, requested/actual model, prompt/protocol/source/suite hashes, usage และ model_quota, response ID/status/finish reason, repair/exclusion history และ paths ไป artifacts

## 4. แชมป์ — KKU API & Quota Owner / codex/champ

**Files:** `scripts/study/api854/kku_client.py`, `quota.py`, `generate_worker.py`, `docs/api854/API_SETUP_TH.md` และ generation/capture artifacts ของตน

**Consumes:** ออมกำหนด queue contract/protocol; secrets จาก environment ที่ owners ตั้งในเครื่อง; fixed-source context จากบีม
**Produces:** raw response/source/usage/attempt metadata และ enqueue evaluation ผ่าน transaction ของ queue

- [ ] ตรวจ keys/permissions ของ a01–a10 โดยไม่พิมพ์ keys; ตรวจ model IDs และเลือกจริงทุกบัญชี ไม่สมมติ quota/model เท่ากัน
- [ ] client คืน response + normalized usage/quota + typed error; ปิด auth headers ใน logs และกำหนด timeout
- [ ] quota ledger ผูก account+model/quota bucket+reset window เก็บ actual used/reserved/remaining; reserve prompt estimate+max output ก่อนส่งแล้ว reconcile กับ response
- [ ] เริ่ม concurrency 1 request/account และ global 2 ค่อยปรับจาก pilot ตาม limits จริง จำกัดการส่งส่วนกลางด้วย ไม่ถือว่า 10 บัญชีอนุญาต 10 requests พร้อมกันเสมอ
- [ ] daily limit พัก bucket นั้นถึง reset; auth error ปิดบัญชีรอเจ้าของแก้; transient 429/5xx ใช้ retry-after ถ้ามีหรือ exponential backoff+jitter สูงสุด 3 transport attempts เก็บ usage ที่อาจถูกคิด
- [ ] เมื่อ output truncated/ไม่ใช่ Java/refusal บันทึก generation outcome ตามจริง ไม่ retry ไม่จำกัด ไม่เปลี่ยนโมเดลโดยไม่ระบุ condition
- [ ] หลังเที่ยงคืนสร้าง window ใหม่โดยคง ledger เก่าและตรวจ quota จาก request ปกติครั้งถัดไป; อย่า reset ยอดใน response เองหาก server ยังไม่ reset
- [ ] ตรวจ client/ledger ด้วย mocked responses: usage mapping, model mapping, 401 ทั้งสามชนิด, restart, quota reservation สอง worker, unknown response และ midnight boundary ก่อน live pilot

## 5. บีม — Algorithms & Defects4J Evaluation Owner / codex/beam

**Files:** `scripts/study/api854/context.py`, `algorithm_worker.py`, `evaluate_worker.py`, `docs/api854/REPRODUCE_TH.md` และ evaluation artifacts ของตน

**Consumes:** bug inventory/protocol และ generated suite ที่ queue ระบุพร้อม hash
**Produces:** fixed repeats/buggy/coverage/timing records, failure reason และ semantic test validity review

- [ ] ตรวจ environment/JDK/Defects4J และ export active bug IDs; ทำ inventory 854 ไม่อ้างว่าจำนวน test methods เท่ากับ bugs
- [ ] ตรวจว่าสคริปต์เดิมรองรับ target ของทุก bug หรือจำกัด probe ของ 17 bugs ห้ามให้ manifest 854 ทำให้เข้าใจว่ารองรับทุก bug แล้ว; จัด unsupported adapters เป็นงานและรายงานตามจริง
- [ ] เตรียม context จาก fixed revision สำหรับ API พร้อม build/test framework/API info โดยไม่เปิดเผย buggy failure logs ให้ AI
- [ ] ต่อ CMA-ES และ FSCS-ART ผ่าน runner เดิมและ configs ที่ตรึง; สร้าง 1 suite/bug/วิธี เก็บ raw/retained counts และ source provenance
- [ ] แยก worktree project/bug/revision ต่อ lease ห้ามสอง workers ใช้ worktree เดียวกัน; cache checkout/dependencies พร้อม disk limits
- [ ] ตรวจเทสไม่ว่าง ไม่ skip ทั้งหมด fixture ใช้งานได้ และมี observable checks; Closure null-return suite เดิมต้องเป็น invalid แม้แก้ compile ได้
- [ ] ใช้ suite เดียวกันทดสอบ fixed สองครั้ง buggy และ line/branch coverage บันทึก environment failures/timeout แยกจาก test failures
- [ ] compatibility edits ได้เฉพาะนโยบายที่ตรึง เก็บ delta/exclusions ห้ามแก้ assertions เพื่อให้ buggy fail; เก็บคะแนนก่อน/หลัง processing และ fixed-survival counts
- [ ] pilot evaluation concurrency 2 ปรับตาม CPU/RAM/disk/timeouts; ตรวจ fixtures/hashes/coverage denominator และ repeatability ก่อน batch

## 6. ออม — Protocol, Queue, Analysis & Delivery Owner / codex/aom

**Files:** `scripts/study/api854/queue.py`, `inventory.py`, `report.py`, protocol/configs, `docs/api854/{PROTOCOL_TH.md,START_HERE.md,REPORT_TH.md,EVIDENCE_INDEX.md}`, `output/api854-20261002/`

**Consumes:** API/evaluation records ที่แชมป์/บีมส่งผ่านคิว; rubric และผลเก่าเพื่อใช้ประวัติ
**Produces:** frozen inventory/protocol, queue contract, progress/analysis exports, final report/slides/ZIP

- [ ] ยืนยัน inventory มี 854 unique project/bug pairs และ 17 projects สร้าง 3,416 unique jobs พร้อม hash; ใช้ local draft active-bugs เป็น input หลังตรวจ ไม่ stage draft อื่นทั้งหมด
- [ ] ทำ SQLite transactional claims/leases และ recovery; queue runtime ไม่ขึ้น Git; ถ้าใช้หลายเครื่อง ใช้ server queue หรือแบ่ง shard ไม่ copy SQLite ให้หลายเครื่องเขียนร่วมผ่าน network share
- [ ] กำหนด queue interface ก่อนลงมือ worker: claim(stage,worker_id), renew(job_id,lease), complete(job_id,attempt_id,artifacts), fail(...), release_expired(); ทุก mutation ต้องตรวจ lease owner และ atomic
- [ ] jobs/attempts/quota reservations เชื่อม foreign keys แยก credential references ออกจากข้อมูลเผยแพร่; unknown API outcomes ไม่ถูก release ไปยิงซ้ำเอง
- [ ] ทดสอบ duplicate keys, simultaneous claims, expired lease, resume-after-crash และ aggregation of failed/missing jobs ก่อน live pilot
- [ ] ทำ progress ต่อวิธีและต่อ project พร้อม tokens/remaining/rate/error/ETA แยก successful generation จาก evaluated/usable
- [ ] วิเคราะห์ matched bugs ที่ทั้ง 4 วิธี eligible พร้อม all-attempt completion/failure rates, time/coverage/fault detection และ model conditions ห้ามลบ failures จากภาพรวม
- [ ] ตรึง dataset/manifest hashes แล้วทำ report/slides/demo/ZIP จาก snapshot เดียว เก็บ API evidence แทนการแต่งภาพแชทที่ไม่มี
- [ ] ตรวจ README สมาชิก/IDs rubric หลักฐาน ความลับ และ checksum ของ ZIP; ติดป้าย partial หากยังไม่ครบ 854 ทุกวิธี

## 7. ลำดับเริ่มงานพร้อมกัน

1. 0–2 ชั่วโมงแรกโดยประมาณ: ออมสรุป contract/protocol/configs แชมป์ทำ client/model/usage บีมตรวจ environment/target adapter ทุกโปรเจกต์ ทุกคนทำ branch ตัวเอง
2. Pilot แบบ stratified: เลือก 20 bugs โดยมีครบ 17 projects เพิ่ม Closure/JxPath และ context ใหญ่ รวม 80 jobs ไม่เลือกจากความง่ายหรือผลดีที่สุด บันทึกรายการก่อนรัน
3. Gate: credentials/API mapping ใช้ได้, quota delta เข้าใจ, resume ไม่ duplicate, tests meaningful, fixed/buggy/coverage ทำงาน และโมเดล/budgets ถูกตรึง
4. ถ้า protocol ไม่เปลี่ยน pilot เป็นส่วนหนึ่งของ 854 ได้; หากเปลี่ยนเก็บ pilot เป็นคนละ condition และรัน primary ใหม่
5. รัน generation/evaluation ซ้อนกัน เติมบัคเป็นกลุ่มที่มีครบ 4 วิธี อัลกอริทึมทำล่วงหน้าได้แต่รายงาน comparisons จาก matched set
6. เมื่อ quota AI หมด แชมป์ดู queue/recovery บีมประมวลผลค้าง ออมทำ QA/analysis/report ไม่หยุดทั้งทีมรอ token
7. ทุกวันหลัง reset ประเมิน backlog/ETA จาก actual remaining รวม token ที่ใช้ในหน้าเว็บด้วยหากเป็น pool เดียวกัน
8. ออมรวม commits หลัง cross-review: บีมตรวจ API/source validity, แชมป์ตรวจ evaluator/model provenance, ออมตรวจ counts และทั้งสามตรวจรายงานก่อนส่ง

## 8. Branches และจุดตรวจรับ

- แชมป์ `codex/champ`, บีม `codex/beam`, ออม `codex/aom` คงชื่อเดิม แต่หน้าที่ปรับตามแผนนี้
- แชร์ plan commit เดียวกันก่อนเริ่ม implementation; ออมเป็น integrator เข้า test ไม่ให้หลายคนแก้ไฟล์ queue/protocol/report เดียวกัน
- เกณฑ์ attempted-complete: มี outcome ทุก 3,416 keys โดย terminal failures ยังคงถูกนับเป็น failures
- เกณฑ์ usable-complete: ทุก 854 bugs มี 4 suites ที่ผ่าน validity/fixed gates และมี buggy/coverage outcomes; หากไม่ได้ต้องรายงานยอดจริง ห้ามรับประกันว่า AI จะสร้างสำเร็จทุก bug
- ส่งงานจาก frozen dataset พร้อม reproducibility instructions คงผล 17-bug เดิมใน historical appendix แยกจาก primary API cohort

## สิ่งที่ยังต้องรู้ก่อนเริ่มใช้ token

- API keys อยู่ในเครื่องผ่าน secret/environment ของทั้ง 10 บัญชี และ initial remaining quota ที่ใช้งานจริง
- model ID/version ที่บัญชีเข้าถึงได้ พร้อมตกลง primary Claude/Gemini ก่อน pilot
- quota scope ว่ารวม input/output/thinking/cache และหลายโมเดลแชร์ bucket หรือไม่ จาก response/account platform
- timezone reset และ request/concurrency limits จริง เอกสารสาธารณะไม่ได้แจกแจงครบ
- จำนวนเครื่อง CPU/RAM/disk และเวลาที่แต่ละคนทำได้ เพื่อปรับ evaluation concurrency และ ETA

ข้อมูลเหล่านี้ไม่ต้องส่ง credentials ลงแชท แผนสามารถเตรียมโค้ด/mock checks/contexts ได้ก่อน live requests
