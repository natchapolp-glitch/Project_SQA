# คิวกลางและหลักฐานของออม — schema 1.0

## URL ที่เปิดใช้งาน

https://defendant-reaches-clinton-guides.trycloudflare.com

Cloudflare รับ HTTPS port 443 แล้วส่งเข้า controller ที่เครื่องออม `127.0.0.1:8765` ไม่ต้องอยู่ Wi-Fi เดียวกันหรือติดตั้ง VPN ที่เครื่องเพื่อน URL นี้เป็นชั่วคราว เปลี่ยนเมื่อเริ่ม tunnel ใหม่ เครื่องออมต้องเปิดอยู่และไม่ sleep ตลอดงาน ไม่รับประกัน uptime 48 ชั่วโมง

ผลตรวจเมื่อ 3 ต.ค. 2569 เวลา 01:12 ประเทศไทย: health พร้อม, authenticated schema/status เข้าถึงได้, status ไม่มี token ได้ HTTP 401 ตรวจจากเครื่องออมผ่าน HTTPS; แชมป์และบีมยังต้องตรวจจากเครื่องตนเอง

## วิธีรับ credentials และตรวจการเชื่อมต่อ

ออมส่งไฟล์ `champ-access.private.json` ให้แชมป์ และ `beam-access.private.json` ให้บีมผ่านช่องทางส่วนตัว ไฟล์อยู่ `.local/api854/` ในเครื่องออม มี base_url และ worker_token แยกคน ห้ามลง Git/แชทสาธารณะ/ภาพหลักฐาน ห้ามส่ง `controller-credentials.json` ซึ่งมี admin token บัญชี KKU และ API key เก็บอยู่ที่ worker เท่านั้น

จาก checkout ที่มี `scripts/study/api854/queue_client.py`:

```powershell
$env:SQA_QUEUE_ACCESS_FILE = 'C:\private\champ-access.private.json'
py scripts/study/api854/queue_client.py check
```

เปลี่ยนชื่อไฟล์เป็นของบีมเมื่อใช้เครื่องบีม ผลต้องเห็น `health.ready=true` และ `authenticated_schema_version=1.0` ตัวอย่างไม่มี secret:

```powershell
Invoke-RestMethod 'https://defendant-reaches-clinton-guides.trycloudflare.com/health'
```

## Contract ที่ทีมต้องตรวจรับก่อนเริ่ม pilot

อ่านรายละเอียดเครื่องอ่านได้ที่ [queue-schema-v1.json](queue-schema-v1.json) หรือ authenticated `GET /v1/schema` เป็น custom JSON contract ไม่ใช่ OpenAPI เอกสารนี้เสนอ contract ที่ติดตั้งแล้ว ยังไม่ใช่หลักฐานว่าทั้งสามคนตรวจรับแล้ว

- งานหนึ่งรายการ = bug หนึ่งตัว × วิธีหนึ่งวิธี: `job_id`, `run_id`, `project`, `bug_id`, `approach`, `protocol_hash`, `owner` มี unique key ตาม run/protocol/project/bug/approach
- วิธี: `cmaes`, `fscs-art`, `kku-claude`, `kku-gemini` stage แยก `prepare → generate → evaluate`
- รับงาน `POST /v1/jobs/claim`: worker_id, stage, owner/approaches ตัวกรอง, lease_seconds 60–3600 ค่าเริ่มต้น 900 ถ้าไม่มีงานได้ job=null
- claim ส่ง attempt_id, lease_token, lease_version, lease_expires_at_unix กลับ เก็บ lease_token ส่วนตัว ทุก mutation ต้องมีสิทธิ์ attempt ปัจจุบัน; สิทธิ์หมดหรือเวอร์ชันเก่าได้ 409
- ต่ออายุ `POST /v1/jobs/{job_id}/renew` ทุกประมาณ 300 วินาที ใช้ attempt_id/lease_token/lease_version เดิมและ lease_seconds
- ส่งหลักฐาน `POST /v1/jobs/{job_id}/artifacts`: raw bytes สูงสุด 20 MiB พร้อม X-Artifact-Name/X-Attempt-ID/X-Lease-Token/X-Lease-Version ได้ artifact_id, uri, sha256, size หลักฐานชื่อเดิมใน attempt เดิมห้ามเปลี่ยนเนื้อหา
- รับหลักฐาน `GET /v1/artifacts/{artifact_id}` ต้องใช้ Bearer token ตรวจ SHA256 ทุกครั้ง หลักฐานบนเครื่องออมอยู่ `.local/api854/evidence/run_id/protocol_hash/project/bug_id/approach/attempt_id/name`
- ส่งผล `/complete` หรือ `/fail`: attempt envelope, outcome, artifact_ids ที่อัปโหลดโดย attempt นี้, metadata ต้องมีหลักฐานอย่างน้อยหนึ่งไฟล์ `/fail` รับเฉพาะผลล้มเหลว
- `generated` ต้องมี suite_sha256 ตรงกับไฟล์ suite ที่อัปโหลด AI generate ต้องมี requested_model, actual_model, account_alias, prompt_sha256, response_id, usage, model_quota ใช้ null เมื่อไม่ทราบ ห้ามเดา เก็บ token input/output/total และ quota ที่บริการรายงานใน object
- metadata ส่วนอื่นเป็นช่องแนะนำ: timestamps UTC, worker_id, source hash, executed/skipped tests, coverage numerator/denominator, fault_detected, raw_evaluator_status, stage_results, failure_reason Worker ต้องส่งตาม protocol การทดลองที่ตรึงแล้ว; server ตรวจเฉพาะกฎข้างต้น ไม่ตัดสินความถูกต้องทางวิทยาศาสตร์แทนทีม
- สถานะคิว `queued/leased/needs_reconciliation/finished` แยกจาก observed outcome เช่น compile_failed, refused, timeout คำว่า finished ไม่แปลว่าผ่านหรือทดสอบครบทุก stage
- quota หมดก่อนส่งคำขอ: worker แจ้งผู้ใช้ก่อนสลับบัญชี งานยังไม่เคยทดลองคง not_attempted ในรายงาน อย่าแต่งเป็น generation_failed; การแจ้ง/การคุม quota เป็นงานของ runner แชมป์ ไม่ใช่ controller
- generate lease หมด: needs_reconciliation ไม่ส่ง AI ซ้ำอัตโนมัติ prepare/evaluate หมดจะกลับ queued; worker ต้องกำหนดเพดาน retry ตาม protocol
- `GET /v1/status` มี jobs และ attempts พร้อม artifact references รวม attempt ที่หมดสิทธิ์ เพื่อกู้หลักฐาน
- admin เท่านั้นใช้ `/v1/admin/reconcile/{job_id}`: resume ต้อง reason และตรวจว่าไม่มีคำขอ AI ค้างก่อน; finish refused/generation_failed ต้อง artifact_id ที่สังเกตจริง; finish generated ต้อง artifact_ids และ metadata เดิม (suite hash + AI fields) เพื่อเดินไป evaluate โดยไม่เรียก AI ซ้ำ
- client ปฏิเสธ HTTP redirect และไม่ retry mutation อัตโนมัติ หลัง network error ให้ตรวจ status/attempt/evidence ก่อนตัดสินใจส่งใหม่

## ตัวอย่าง worker ไม่มี secret

```python
from scripts.study.api854.queue_client import QueueClient
c = QueueClient.from_environment()
claim = c.claim('champ-pc1', 'prepare', owner='champ')
if claim['job'] is not None:
    # ทำ preflight จริงและเขียน preflight.json ก่อนส่ง
    artifact = c.upload(claim, 'preflight.json')
    c.complete(claim, 'prepared', [artifact], {'worker_id': 'champ-pc1'})
```

ตัวอย่างนี้แสดงการเชื่อมต่อเท่านั้น ต้องใช้ preflight จริงและ metadata ตาม protocol ห้ามสร้างผลสมมติ

## สถานะการเปิดงานและการดูแลเครื่องออม

ตอนเปิดบริการคิวว่าง 0 งาน ยังไม่ได้เริ่ม 854 bugs, ไม่เรียก KKU และไม่ตรึง exact model ID ไฟล์ `queue-protocol.draft.json` เป็น integration draft ห้ามใช้เป็นผลหลัก ต้องให้ทีมยืนยัน protocol/model IDs/adapter แล้ว seed pilot 20 bugs (80 jobs) ก่อนขยาย

ใช้ `start_controller.ps1` เปิดบริการและ tunnel เมื่อบริการหยุดแล้ว สคริปต์คงฐานข้อมูล/หลักฐานเดิมและสร้างไฟล์ URL ปัจจุบันใน `.local/api854/connection.json` แจก URL และไฟล์ private ที่อัปเดตใหม่ ห้ามรัน controller สองตัวกับฐานข้อมูลเดียว

ถ้าจะ seed: หยุด controller เดิมก่อน แล้วเริ่มด้วย `--seed-manifest experiments/configs/api854-20261003/ownership.json --protocol <frozen-protocol.json> --run-id <run-id> --seed-scope pilot` การใช้ all จะสร้าง 3,416 jobs ขั้นนี้ต้องใช้ protocol ที่ตรึงจริง

สำรอง SQLite ด้วย SQLite backup API พร้อมโฟลเดอร์ evidence และ credentials ผ่านช่องทางส่วนตัว ห้ามคัดลอกเฉพาะ state.sqlite ระหว่างมี WAL writer และห้ามลงหลักฐานที่มี key ลง Git

Cloudflare Quick Tunnel เหมาะสำหรับชั่วคราว จำกัด 200 requests พร้อมกัน: [เอกสาร Cloudflare](https://developers.cloudflare.com/tunnel/get-started/quick-tunnels/) เริ่ม worker concurrency ต่ำก่อน การปิดเครื่อง/เน็ตหลุด/หยุด tunnel ทำให้ URL ใช้ไม่ได้
