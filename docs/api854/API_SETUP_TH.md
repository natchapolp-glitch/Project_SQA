# API client และ queue adapter ฝั่งแชมป์

ขอบเขตที่ทำ: KKU client, model discovery, quota ledger แบบเครื่องเดียว, fixed-context exporter,
generation evidence และ adapter สำหรับคิวออม schema 1.0 ยังไม่มี scheduler ที่รันงานจริงอัตโนมัติ

## ผลตรวจจากเครื่องแชมป์

- อ่าน KKU API reference จริงที่ <https://gen.ai.kku.ac.th/docs/api> วันที่ 3 ต.ค. 2569
- รายการโมเดลของบัญชี `a01` คืน **string ID** ทั้ง `GET /models` และ `POST /chat/models-list`
  ต่างจากตัวอย่าง numeric ID ในเอกสาร Client รองรับทั้งสองรูปแบบและตรึง exact ID/name
- ผู้ใช้เลือก **`claude-sonnet-5`** แทน Haiku แล้ว; Gemini คง **`gemini-3.5-flash-lite`**
  ใช้ `experiments/configs/api854-20261003/model-selection.json` เป็น model selection กลาง
  Primary worker ปฏิเสธรุ่นอื่นรวมถึง `claude-sonnet-5.5` แม้เป็น Sonnet เหมือนกัน
  ต้องรวม model selection นี้เข้า protocol ที่ออมตรึงก่อน seed pilot
- คิวออมตอบ `health.ready=true` และ authenticated schema `1.0` จากเครื่องแชมป์
  หลักฐาน local: `.local/api854/queue-check-champ-v1.json`
- การตรวจการเชื่อมต่อใช้ GET เท่านั้น ไม่ claim, upload, complete หรือส่ง prompt สร้างเทส
- สถานะ readiness ไม่ยืนยันว่า primary protocol ถูกตรึง หรือ 854 bugs มี adapters พร้อมแล้ว

อ้างอิง contract จาก `aom` commit `2e419e421c2fcc75dbfd516e04ed8ce27aafa463`:
[QUEUE_CONNECTION_TH.md](https://github.com/natchapolp-glitch/Project_SQA/blob/2e419e421c2fcc75dbfd516e04ed8ce27aafa463/docs/api854/QUEUE_CONNECTION_TH.md),
[queue-schema-v1.json](https://github.com/natchapolp-glitch/Project_SQA/blob/2e419e421c2fcc75dbfd516e04ed8ce27aafa463/docs/api854/queue-schema-v1.json)
และ `queue_client.py` จาก commit เดียวกัน นำ client ของออมมาใช้เป็น dependency โดยไม่แก้ไฟล์ต้นฉบับ

## Credentials และคำสั่งตรวจ

ใช้ Python 3.10+ และ standard library เท่านั้นสำหรับ client/tests
จาก root ของ repository:

```powershell
python -m scripts.study.api854.champ_queue
python -m scripts.study.api854.preflight --account a01 --secrets .local/api854/accounts.json --output .local/api854/model-discovery-a01-new.json
python -m unittest discover -s scripts/study/api854/tests -t . -v
```

Queue access อยู่ `.local/api854/champ-access.private.json` มี `role`, `worker_token`,
`schema_version`, `base_url`; `--access` เลือกไฟล์อื่นได้ URL tunnel อาจเปลี่ยนเมื่อออมเปิดใหม่
CLI `champ_queue` ตรวจ health/schema อย่างเดียว ไม่รัน worker หรือ claim งาน

KKU key อ่านจาก `KKU_API_KEY_A01` หรือไฟล์ `.local/api854/accounts.json`
รูปแบบไฟล์: `{"accounts":[{"alias":"a01","api_key":"<private>"}]}`
`.local/` ถูก ignore ห้ามใส่ secrets, quota SQLite และ access/lease files ลง Git หรือ ZIP
เครื่องแชมป์เก็บเฉพาะ access ของแชมป์ ไม่ใช้ token ของบีม

## ส่วนที่พร้อมให้ทีมเชื่อม

`kku_client.py`: `KKUClient.list_models()`, `pin_model()`, `KKUClient.complete()`
ใช้ request ใหม่ที่มี fixed-context prompt เดียว `stream=false` ไม่แนบ execution logs
เก็บ requested/actual model, response ID, finish reason, usage, quota และ hash ของ raw HTTP body
หลักฐาน body ที่เขียนลงไฟล์ปิด credentials; hash อ้างถึง bytes ก่อน redact
ไม่สร้างค่าการวัดที่ขาดขึ้นเอง และไม่เลือกโมเดลอื่นเป็น fallback

`context_export.py`: รับ exact fixed Defects4J checkout, project/bug ID, รายชื่อ Java/build files
และ selection policy ID ตรวจ `.defects4j.config` ว่าเป็น `<bug_id>f`, เก็บสำเนา bytes และ hashes
ไม่อนุมาน target selection แทนออม/บีม และไม่รับ compile/test/coverage logs

`quota.py`: `QuotaLedger.observe()` ต้องรับ **remaining ที่สังเกตจริง** พร้อม evidence reference
และ reset/expiry ที่ยังไม่ถึง ใช้ `activate_initial()` แล้ว `reserve()` ก่อนส่ง
reserve เท่ากับ prompt token bound + max output; protocol ต้องกำหนด bound/ส่วนสำรอง
ห้าม initialize ด้วย 200k/350k โดยถือว่าบัญชีเต็ม และไม่ refill ที่เที่ยงคืนเอง
`finish()` reconcile ด้วย quota ที่ server คืน หากไม่ทราบ outcome จะคง reservation ข้าม restart
ถ้าขาด quota response จะพัก bucket รอการตรวจ ไม่เดาว่า usage เท่ากับ billed quota

Concurrency เป็น **local**: หนึ่ง outstanding request/account และรวมเริ่มต้นสอง requests
ห้ามแชร์ ledger SQLite นี้ข้ามเครื่อง; global limiter/บัญชีร่วมต้องตกลงกับทีมก่อน live pilot
401 แยก daily limit / invalid key / invalid model; timeout/5xx เป็น unknown ไม่มี automatic retry
อนุญาต manual transport retry ได้เฉพาะ proven rate-limit rejection สูงสุดสองครั้ง
ต้องปฏิบัติตาม Retry-After; ไม่มี semantic regeneration หรือ retry ผลที่ล้มเหลวเพื่อเลือกคำตอบใหม่

การเปลี่ยนบัญชีใช้ `request_switch()` → แจ้งผู้ใช้ให้เห็นจริง → `acknowledge_notification(receipt=...)`
จึงเปิด route บัญชีใหม่ได้ Console log อย่างเดียวไม่เป็น notification receipt

## Generation → queue schema 1.0

`GenerationWorker.generate()` รับ `GenerationJob`, prompt reserve/max output/temperature ที่ตรึงแล้ว
และ **`attempt_id` จาก claim ของคิว** เก็บหลักฐาน immutable ก่อนส่งและหลังรับคำตอบ
ไม่ใส่ lease token ในหลักฐานเผยแพร่ ผล `response_received` หมายถึงพบ Java fence ที่สมบูรณ์
ยังไม่รับรอง compile, cap 30, fixed tests, coverage หรือ usable suite

`QueueGenerationHandoff` รับ client, claim, worker_id, suite resolver และ suite policy ID
ตรวจ identity/attempt/lease expiry ก่อนทุก mutation อัปโหลดหลักฐานด้วย lease envelope
ตรวจ server hash/size ก่อนส่ง `complete`/`fail` พร้อม artifact IDs ของ attempt นั้น
มี upload receipts และ completion intent แยกไว้ใน attempt directory

สำหรับ `generated` ต้องมี **Beam's suite resolver** คืน `suite.tar.bz2` พร้อมนโยบาย processing ที่ตรึงแล้ว
adapter ตรวจ archive/path/Java files/credentials และใช้ SHA-256 ของ archive ที่อัปโหลดจริง
ไม่สร้าง suite หรือแก้ assertions ให้อัตโนมัติ ไม่มี resolver จะคง handoff pending
Beam ต้องกำหนด filenames/packages, source-order cap 30 และ repair/exclusion policy พร้อม lineage

Outcome mapping:

| ผลที่สังเกตจาก generation | คิว schema 1.0 |
| --- | --- |
| response_received + suite archive จากนโยบายที่ตรึง | generated → evaluate |
| explicit refusal | refused ผ่าน /fail |
| truncated / ไม่มี Java / model mismatch | generation_failed พร้อม raw reason |
| transport unknown / quota / auth / invalid model / rejected-before-processing | needs_reconciliation |

กรณี quota/auth ถูกปฏิเสธไม่อ้างว่า model สร้างเทสแล้ว; metadata เก็บ raw reason แยก
และให้ออม reconcile/requeue หลังตรวจหลักฐาน ไม่มีการสรุปเป็น failure ที่เติมยอดครบแผน

เครือข่ายหลุดตอนส่ง completion: ห้ามเรียก KKU ใหม่หรือ resend completion โดยเดา
ตรวจ `GET /v1/status`/attempt/artifact references กับออมก่อน; ถ้าสิทธิ์หมดใช้ออม/admin reconcile
เมื่อ process ตายหลัง send intent ให้ถือว่า outcome ยังไม่ทราบ อย่าลบ reservation เพื่อยิงซ้ำ
ใช้ `LeaseHeartbeat` และ `generate_with_lease()` ใน `lease.py` เพื่อ renew claim เดิม
ก่อนส่ง KKU และระหว่างรอคำตอบ. ส่ง heartbeat เดียวกันเป็น `lease_guard` ของ
`QueueGenerationHandoff`; หยุด heartbeat และ renew ครั้งสุดท้ายก่อน complete/fail.
เมื่อ renew ไม่สำเร็จจะหยุด publication และเก็บ generation artifacts ไว้
ไม่ cancel/ยิงคำขอ KKU ซ้ำ. ถ้าคำตอบยังไม่ทราบต้อง reconcile usage และคิวกับออม.
ส่วนนี้ยังไม่ใช่ scheduler และไม่ claim งานเอง; caller ต้องใช้ frozen payload contract.

ตัวอย่างการเชื่อมหลังมี frozen job และ suite policy (ไม่รันทันทีจากตัวอย่าง):

```python
from scripts.study.api854.lease import LeaseHeartbeat, generate_with_lease

heartbeat = LeaseHeartbeat(queue_client, claim)
handoff = QueueGenerationHandoff(
    queue_client, claim, worker_id="champ-pc1",
    suite_resolver=beam_suite_resolver, suite_policy_id=frozen_suite_policy_id,
    credential_secrets=(kku_client.account.api_key,), lease_guard=heartbeat,
)
worker = GenerationWorker(
    kku_client, ledger, artifact_root=artifact_root, model=pinned_model,
    bucket=verified_bucket, window=observed_window, handoff=handoff,
)
result = generate_with_lease(
    worker, frozen_job, heartbeat,
    prompt_token_reserve=frozen_prompt_token_bound,
    max_tokens=frozen_output_budget, temperature=frozen_temperature,
)
```

## รายการที่ต้องให้ทีมช่วยก่อน live pilot

- **ผู้ใช้/แชมป์:** ตรวจ actual remaining quota ของ Sonnet 5/Flash Lite และ bucket sharing/reset timezone
  ค่า 200k/350k เดิมเป็น planning input ไม่รับรอง remaining หรือ quota ของรุ่นที่เปลี่ยนใหม่
- **ออม:** frozen protocol hash, pilot jobs, target/prompt/budget contract และการ reconcile
  `queue-protocol.draft.json` ยังไม่ใช่ primary protocol ห้าม seed จาก draft เพื่อเริ่มผลหลัก
- **บีม:** fixed-context selection/target readiness และ suite resolver ตาม interface ข้างต้น
  evaluator ต้องตรวจ meaningful assertions, fixed repeat, buggy และ coverage ด้วย suite เดียวกัน
- **ทั้งทีม:** ตรวจ global API limiter/บัญชีร่วม, lease renew/recovery และรับ contract ก่อนใช้ quota จริง

เครื่องแชมป์พบ credentials เฉพาะ alias `a01` ในจุดส่งต่อวันที่ 3 ต.ค.
หากให้แชมป์เป็น API coordinator ทั้งหมด ต้องจัดสรร keys a01–a10 ใน ignored local file
และยืนยัน observed remaining ของแต่ละบัญชี. หากส่งจากหลายเครื่อง ให้แยกบัญชี
ไม่ใช้ alias/bucket เดียวพร้อมกันข้ามเครื่องด้วย ledger แบบ local นี้.
เริ่ม pilot ด้วยบัญชีเดียวที่ตรวจโควตาแล้วได้ ไม่จำเป็นต้องรอ keys ครบสิบ.

Offline tests ใช้ fixture แยก condition `mock-integration`; adapter ปฏิเสธ mock evidence บน live client
ไม่มี mock results รวมเข้า primary dataset และการเชื่อมคิวสำเร็จไม่เท่ากับเริ่มทดลองสำเร็จ
