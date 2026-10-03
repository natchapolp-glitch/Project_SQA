# แชมป์ตรวจ Aom 76b88e25 / shared v5 และ KKU preflight

รวม `aom 76b88e25` ต่อจาก `champ a7964613` แล้ว ตรวจ API worker, shared preparation v5 และ assignment แบบ offline
พร้อมเก็บ KKU model catalog และส่ง non-test settings calibration สองคำขอให้ตอบ `OK`
ไม่มีคำขอสร้างโค้ด/เทส ไม่มี dispatch หรือ mutation ของ live queue และยังไม่เปิด live pilot

## ผล KKU จริงของบัญชี a01

GET `/api/v1/models` และ POST `/api/v1/chat/models-list` ตอบ 200 และพบ model IDs ทั้งสองตามที่เลือก
จากนั้น POST `/api/v1/chat/completions` หนึ่งครั้งต่อโมเดลด้วยข้อความสั้นที่ไม่ใช่ test-generation prompt
ส่ง `temperature=0`, `max_tokens=4096`, `stream=false` ทั้งสองตอบ `OK`, finish reason `stop` และ HTTP 200

| Model ID ที่ส่งและ KKU ส่งกลับ | เวลาสังเกตไทย 2026-10-03 | Daily quota | Daily usage หลังคำขอ | Daily remaining | Prompt / completion / total tokens ของ preflight |
|---|---|---:|---:|---:|---:|
| claude-sonnet-5 | 07:13:41 | 200,000 | 39 | 199,961 | 35 / 4 / 39 |
| gemini-3.5-flash-lite | 07:13:43 | 350,000 | 21 | 349,979 | 20 / 1 / 21 |

ตัวเลขเป็น observation ณ เวลาข้างต้น ไม่ใช่โควตาปัจจุบันที่รับรองตลอดวัน
รวม usage ที่ KKU รายงานสำหรับ calibration สองครั้ง = 60 tokens ไม่มี retry หรือสลับบัญชี
การตอบ 200 ยืนยันว่า API รับ request settings นี้; response ไม่ได้ echo effective temperature/output cap ของ backend
IDs ที่ส่งกลับยังเป็น alias เดิม ไม่ได้ระบุ underlying provider revision

หลักฐานพร้อม request payload, response metadata, observation time, request ID และ SHA-256 อยู่ใน
[KKU calibration receipt](../../output/api854-provider-preflight-20261003/champ-kku-v5-calibration-evidence-v1.json)
ไม่ใส่ Authorization, API key, email หรือ private access files ใน Git

KKU ไม่ส่ง context/output ceilings, quota bucket/window IDs หรือ reset timestamp ใน responses เหล่านี้
[API documentation](https://gen.ai.kku.ac.th/docs/api) ระบุ temperature 0–2, max_tokens และ daily quota/reset โดยทั่วไป
แต่ไม่กำหนด limits แยกสองโมเดลหรือเวลา/timezone ของ reset จึงไม่ใช้ตัวเลขตัวอย่างใน docs แทน observation
เครื่องมือ browser ล้มเหลวด้วย `helper_sandbox_lock_failed / SetNamedSecurityInfoW error 5` ทั้งก่อนและหลัง reset session
จึงยังอ่าน quota dashboard ที่ล็อกอินไม่ได้ และไม่แก้ Windows security เพื่อข้ามปัญหานี้

## สิ่งที่ตรวจรับได้จาก v5

- ตรวจ 5 bugs / 124 capability-selected targets / 63 checksum entries; fixed source, revision proof,
  target/fixture/recipe/prompt bindings และทุก capability exclusion ตรงกับข้อมูล v3 ต้นทางและ policy v5
- Prompt สูงสุด Closure-176 = **177,698 UTF-8 bytes**; รวม prompts ห้าบั๊กหนึ่งชุด = 634,472 bytes
  เป็น bytes ไม่ใช่ provider token count และยังไม่มี preparation ที่อนุมัติ recipes ของอีก 15 pilot bugs
- Source hashes ของ runtime ที่รวมแล้วตรงกับ Aom v5 proposal ทุกไฟล์
  เก็บ [champ-composed-v5.proposal.json](../../experiments/configs/api854-20261003/champ-composed-v5.proposal.json)
  แยกพร้อม `.sha256` สำหรับส่งตรวจรับ ไม่เขียนทับ Aom draft/immutable inputs
- คู่ runner plan ปัจจุบันของ proposal คือ
  [aom-continuation-v5/runner-plan.json](../../output/api854-20261003/aom-continuation-v5/runner-plan.json)
  SHA-256 `462f6196d81dfc9ed1bda31ce0ae81e59759341b6671cb7fc9d9301fe453e048`
  มี champ-pc1 API coordinator, beam-pc1 CPU 1 slot และ aom-pc1 CPU 1 slot
  ไม่มี aom-pc2/beam-pc2/beam-pc3 ในแผนใหม่นี้; routing ครบ 10,248 stage keys
- ตรวจ ownership กับ installed Aom inventory ครบ 854 bugs; held manifest มี 3,416 jobs ยังไม่ dispatch
  Champ assignment ใช้ได้สำหรับ owners champ/beam/aom ใน development review
  Off-assignment claim และ primary proposal ถูกปฏิเสธก่อน HTTP; API worker ปฏิเสธ protocol ที่ไม่ frozen
- หลักฐาน Defects4J 10 suites เป็น received development results เดิมของบีม
  แชมป์ไม่ได้รัน Defects4J experiments ซ้ำ และ offline tests ไม่ใช่ผล pilot ใหม่
- ZIP ของออมถูก ignore โดย Git จึงไม่มีไฟล์ ZIP หลัง fetch; มีเพียง package receipt/SHA ที่รับมา
  รอบนี้ตรวจ versioned input files โดยตรง ไม่อ้างว่า verify ZIP bytes แล้ว

หลักฐาน: [v5 input/runner audit](../../output/api854-provider-preflight-20261003/champ-aom-v5-input-runner-audit-v1.json)
และ [validation receipt](../../output/api854-provider-preflight-20261003/champ-v5-validation-v1.json)
API854 รัน 250 tests ผ่าน 249 / skip 1 เรื่อง Windows symlink privilege; legacy/Java ผ่าน 30 tests
รวมผ่าน 279 tests ไม่มี failures/errors และชุดตรวจ shared-v4/v5/API guards เฉพาะผ่าน 35 tests
การทดสอบใช้ isolated Store/mock provider และ Java probe ใช้ JDK จริง แยกจาก real KKU calibration ข้างต้น

## Reserve และสิ่งที่ยังขาด

ตาม conservative byte guard ของ worker สำหรับ **ห้าบั๊กที่เตรียมแล้ว**:

```
prompt_token_reserve >= 177698 + H
request reservation >= 181794 + H  (เมื่อเสนอ max_tokens=4096)
```

H ต้องเป็น provider framing bound ที่มีหลักฐาน ปัจจุบันยังเป็น null
Usage 35/20 prompt tokens ของข้อความ calibration ไม่ใช่ token count ของ v5 source prompts
Receipt คำนวณ JSON serialization overhead เป็น bytes ให้ครบ 5 bugs × 2 models โดยไม่ได้ส่ง prompts เหล่านั้นให้ KKU
JSON overhead นี้ไม่ใช่ provider framing tokens และไม่ใช้แทน H
จึงยังไม่กรอก final reserve, context ceiling, output ceiling, quota window/bucket/expiry หรือ provider reset time
และยังไม่ import observations เหล่านี้เข้า live quota ledger เพราะขาด bucket/window/expiry ที่ตรวจรับแล้ว

ต้องรับ limits/settings/framing และ bucket/reset/expiry evidence ของ KKU เพิ่มจากผู้ดูแล/หน้าบัญชีที่เข้าถึงได้
worksheet แยกใน `.local/api854/provider-evidence-v5-calibration.private.json` เก็บค่าที่พบและคง unknown fields เป็น null
เมื่อเตรียมครบ 20 pilot bugs ด้วย shared policy รุ่นสุดท้าย ต้องวัด max prompt และคำนวณ reserve ใหม่อีกครั้ง
Gate A และ shared semantic review ยังไม่ผ่าน; proposal ไม่อนุญาต live pilot

## ข้อความส่งให้ทีม

ออม: รับ 76b88e25 และตรวจ shared v5/API worker แล้ว มี actual settings-request acceptance และ remaining ของ a01
พร้อม receipt/hash แต่ limits/framing/reset/expiry และ final 20-bug preparation ยัง pending
ดึง champ commit ที่แจ้งพร้อมเอกสารนี้และตรวจ protocol/runner pair ก่อน freeze condition ใหม่

บีม: ดึง champ รุ่นที่แจ้งเพื่อตรวจ shared-v5 capability/recipe/prompt contract ของห้าบั๊กกับหลักฐานเดิม
ทำ prospective fixtures/oracles และ target execution evidence อีก 15 pilot bugs ต่อได้
ยังต้อง semantic/shared contract review ร่วมทีม และยังไม่เปิด live pilot
