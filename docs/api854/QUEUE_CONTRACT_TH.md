# Queue contract v1 — สำหรับแชมป์ บีม ออม

**Deployment update:** ทีมใช้ `queue_server.py`/`queue_client.py` และ `/v1/...` จาก
QUEUE_CONNECTION_TH.md กับ protocol.core-frozen.json สำหรับ prepare-only80jobs
เนื้อหา endpoint ด้านล่างเป็น foundation service.py reference ไม่ใช่ deployed API; ห้ามเปิด controllerซ้ำ

ไฟล์เจ้าของออม: `scripts/study/api854/{inventory,queue,service,report}.py`
Runtime: `.local/api854/` ถูก ignore ทั้งฐานข้อมูล artifacts และ credentials
คิวนี้ไม่เรียก KKU หรือ Defects4J เอง แชมป์/บีมต่อ workers ตาม contract นี้

## จุดเชื่อมต่อหลายเครื่อง

ออมเครื่อง 1 รัน HTTP service หนึ่ง instance ฐานข้อมูล SQLite อยู่บนดิสก์ของ server เท่านั้น
ทุกเครื่องใช้ service เดียวกัน ห้ามแชร์ SQLite ผ่าน network drive หรือ copy ให้ workers เขียนแยกกัน
SQLite ใช้ BEGIN IMMEDIATE, unique job key และ foreign keys; workers ต่อผ่าน HTTP เท่านั้น
ใช้ TLS เมื่อ bind ไป address ภายนอก หรือใช้ SSH tunnel ไป localhost
เก็บ `API854_QUEUE_TOKEN` ใน environment เท่านั้น ไม่มี KKU key อยู่ในคิว

HTTP ทุก endpoint ใช้ `Authorization: Bearer <API854_QUEUE_TOKEN>`
Header นี้เป็น transport authentication ห้ามเขียนลง request/evidence metadata
ผลสำเร็จ HTTP 200, unauthorized 401, invalid lease/record 400, endpoint ที่ไม่มี 404
Error ไม่ echo request body หรือ credential กลับมา

## Identity และ lease

Job identity คือ `(project, bug_id, approach, protocol_hash, repeat_index=1)`
job_id เป็น SHA-256 ของ identity แบบ canonical JSON: sort_keys=true, separators=(',', ':'), UTF-8
Protocol hash ใช้ canonical JSON ของ protocol ทั้งไฟล์; file SHA-256 เป็นคนละค่า
Ownership เปลี่ยนผ่าน reassignment event ไม่เปลี่ยน job key และไม่สร้าง duplicate
Worker ID ระบุเครื่อง/process เช่น aom-host2-eval1; lease token สุ่มต่อ claim ห้าม reuse
Artifact/worktree ของแต่ละ attempt แยกกัน Lease ไม่ใช่สิทธิ์เขียน shared checkout

## Worker calls

POST /claim: `{ "stage": "generate" | "evaluate", "worker_id": "...", "lease_seconds": 300, "owner": "aom" }`
owner เป็น optional filter; shard คือเจ้าของตรวจรับ ไม่จำกัด worker เครื่องใด
คืน job พร้อม lease/lease_until หรือ null เมื่อไม่มีงานพร้อม
generate เริ่มจาก not_attempted/queued; evaluate เริ่มจาก generated
generation_record เป็น JSON string ใน job ให้ parse เพื่ออ่าน suite/source/artifact URI

POST /renew: `{job_id, lease, worker_id, lease_seconds}`
POST /start: `{job_id, lease, worker_id, metadata}` คืน `{attempt_id}`
metadata ใช้ aliases/hash/model/environment เท่านั้น ไม่มี secrets/email/header
POST /dispatched: `{job_id, lease, worker_id, attempt_id}`
commit dispatch intent ทันทีก่อน request/algorithm/evaluation จริง ห้ามทำ execution ก่อน start/dispatched
POST /defer: `{job_id, lease, worker_id, reason}` ใช้ก่อน dispatch สำหรับ quota/auth/readiness blocker
defer พัก job และคง not_attempted เมื่อยังไม่เคยรัน ไม่ใช้ fail เพื่อเติมยอด

POST /artifact: `{job_id, lease, worker_id, attempt_id, data_base64}`
ข้อมูลไม่ว่าง ขนาดไม่เกิน 8 MiB ต่อ artifact; แยก logs ใหญ่เป็น chunks พร้อม hashes
คืน `{path, sha256, uri}`; path เป็นตำแหน่งบน server ห้ามตีความเป็น local path ของ remote worker
GET /artifact/<job_id>/<attempt_id>/<sha256> ใช้ uri ที่คืนมาเพื่อดาวน์โหลด
Remote evaluator ตรวจ SHA-256 ของ bytes ก่อนใช้งานและสร้าง worktree ของตนเอง
Server เก็บ immutable bytes ใน directory job/attempt/hash; upload bytes เดิมซ้ำได้อย่าง idempotent
ผล artifact record ต้องเก็บ uri ควบคู่ path; package เก็บ server paths ไว้เป็น provenance

POST /complete: `{job_id, lease, worker_id, attempt_id, record}`
generate → generated (รอ evaluation); evaluate → complete (usable)
POST /fail: `{job_id, lease, worker_id, attempt_id, status, record}`
typed outcomes: generation_failed, refused, truncated, compile_failed, fixed_failed,
environment_failed, invalid_suite, timeout, coverage_failed
fail ต้องเป็นการทดลองจริงที่ dispatch แล้ว มี reason, protocol_hash และ hashed evidence
Generation/evaluation failure ต้องใช้ outcome ให้ตรง stage

GET /snapshot คืน jobs, attempts, events, reservations ใน transaction เดียว
Workers ไม่มี HTTP endpoint เปิด gate/requeue/reassign; coordinator ทำผ่าน local CLI/API

## Generation record

Required: protocol_hash, source_hash, suite_hash, suite_path, test_methods (1..30), artifacts
source_hash ต้องชี้ fixed source/context ที่มีใน artifacts; suite_path/hash ต้องตรง artifact
KKU เพิ่ม account_alias, requested_model, actual_model, prompt_hash, request_hash,
raw_response_hash, response_id, usage, model_quota; raw response ต้องมี hashed artifact
Models ต้องตรง protocol ที่ register; ใช้ response model version จากจริง ไม่ fallback เงียบ
Usage/model_quota ไม่ทราบได้เป็น null โดยไม่แต่งค่าศูนย์
เก็บ raw/processed hashes, processing history/exclusions/operator ตาม protocol แม้ไม่แก้ source

## Evaluation record

คง generation lineage และ KKU provenance เดิม แต่ไม่บวก usage ซ้ำใน evaluation
Required เพิ่ม compile_valid=true, fixed_passes=[true,true], meaningful_assertions=true,
target_executed=true, buggy_measured=true, coverage_measured=true, fault_detected เป็น boolean
line_covered/line_total และ condition_covered/condition_total เป็น nonnegative integers
Zero denominator ให้ ratio=null; measured zero numerator ให้ ratio=0 เมื่อ denominator >0
evidence_roles ต้องชี้ hashes ใน artifacts ของ fixed1, fixed2, buggy, coverage, validity
Beam เป็นผู้ตรวจ log/coverage/semantic validity จริง Boolean ใน record ไม่แทนการตรวจ artifact
suite/source hashes ต้องตรง generated ห้าม prune/assertion repair ใน condition นี้

Failure evaluation ยังต้องคง suite/source hashes เดิมและเก็บ partial measurements ที่ได้จริง
KKU failures ต้องระบุ requested model, account alias, prompt/request hashes และ redacted request artifact
ระบุ response_received; ถ้าได้รับ response เก็บ raw_response_hash/artifact ถ้าไม่ได้ให้ false/null
actual_model, usage, model_quota และ raw_response_hash ที่ไม่ทราบใช้ null ชัดเจน
ไม่ใส่ execution logs ลง KKU request แม้เก็บ logs ใน local evidence

## Recovery และ retries

Claim หมด lease ก่อน start: กลับสถานะพร้อมโดยยังไม่เพิ่ม attempt
หมด leaseหลัง start: needs_reconciliation ทั้ง generation/evaluation; ไม่ dispatch ซ้ำอัตโนมัติ
Lease เก่า renew/complete/fail ไม่ได้ แม้เป็น worker เดิม
Coordinator ต้องยืนยัน worker เก่าหยุดและตรวจ request/response/artifacts ก่อนเลือก:

- recover: publish output ที่ได้แล้วใน attempt เดิม พร้อม evidence ว่าเป็น output เดิม
- requeue: เฉพาะ infrastructure/unknown ที่ตรวจแล้ว ใช้ typed decision confirmed_not_executed,
  confirmed_transport_failed หรือ confirmed_execution_stopped พร้อม hashed proof
- confirmed_not_executed คง incomplete และปรับ attempted ให้ตรงประวัติ ไม่เป็น failure

Primary draft policy: generation infrastructure ไม่เกิน 3 attempts ต่อ job;
generation requeue ต้อง confirmed_not_executed และพิสูจน์ว่า provider/generator ไม่ประมวลผล attempt เดิม
หยุด client/timeout/transport error อย่างเดียวไม่พิสูจน์เรื่องนี้; คง needs_reconciliation จนมีหลักฐาน
evaluation infrastructure ไม่เกิน 2 attempts ต่อ job (suite/source เดิม)
refused/truncated/generation_failed/compile_failed/fixed_failed/invalid_suite ไม่ retry เพื่อหา semantic output ใหม่
การเปลี่ยน prompt/assertions/model/repair ต้องใช้ protocol condition ใหม่ พร้อม history และผลเดิม
นี่เป็นนโยบาย draft ให้ออม/ทีมรับรองก่อน Gate A ไม่อ้างว่าได้ตกลงผ่านแล้ว

## Quota integration ของแชมป์

Queue.reservation เชื่อม reservation_id → attempt_id ด้วย foreign key พร้อม bucket/reset_window
แชมป์ดูแล reservation/remaining/reset/limiter และ notification ใน quota.py ไม่ใช่ Queue.claim
ก่อน /dispatched ต้อง reserve prompt estimate + max output สำเร็จ
quota/auth blockerก่อน dispatchใช้ /defer ไม่เป็น experimental failure
เมื่อเปลี่ยนบัญชีต้องแจ้งในช่องที่ผู้ใช้เห็นสำเร็จก่อนส่ง request บัญชีถัดไป
ห้ามใส่ API key/email ใน metadata; aliases a01..a10 เท่านั้น
Quota reset/notification/auth mocked tests และ live evidence เป็น Gate A ของแชมป์; queue tests ไม่อ้างแทน
