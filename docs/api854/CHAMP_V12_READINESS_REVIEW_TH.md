# Champ รับตรวจ Aom Graphics v12 และ worksheet 40 คู่

รับ `aom 63ad195623c2ed3f67f3ae232c00c54d3160ce72` วันที่ 4 ตุลาคม 2026.
รับ **scoped input bindings / consumers / bounded raw-evidence checks** ของ
`api854-20261003-graphics-v12-development`; ยังไม่รับ semantic ทั้ง403,
final owner-host, provider reserve หรือ Gate A/pilot.

## ชุดส่งคืน

- ผลสำเร็จ: `output/api854-20261004/champ-v12-readiness-review-v2/receipt.json`
- Manifest: `champ-v12-readiness-review-v2/checksums.json`
- Worksheet ที่คำนวณใหม่: `champ-v12-readiness-review-v2/prompt-model-worksheet.json`
- Provider metadata ปัจจุบัน: `champ-v12-readiness-review-v2/current-provider-review.json`
- Original Git bindings: `champ-v12-readiness-review-v2/source-bindings.json`
- จุดรวม paths/hashes: `output/api854-20261004/champ-v12-return-index-v1.json`
- ข้อเสนอ pilot แยก: [CHAMP_V12_PILOT_PROPOSAL_TH.md](CHAMP_V12_PILOT_PROPOSAL_TH.md)

Paths ที่ขึ้นต้น `champ-v12-readiness-review-v2/` อยู่ใต้ `output/api854-20261004/`.
เก็บ v1 ซึ่งหยุดที่การสร้างโฟลเดอร์นำเข้า metadata ก่อน snapshot/tests แล้ว;
ไม่ใช้ v1 เป็นคำรับสำเร็จ. Metadata ใน v2 ใช้ observations ชุดเดิม ไม่ส่ง API ซ้ำ.

## ผลตรวจ

ตรวจ final manifest ของออมครบ1068รายการ และ runtime41ไฟล์กับ actual Git bytes.
Protocol/index/runner SHA-256:

- Protocol `71cd8ecff8d67c2ffbcb5b29e2f13c71c631620fc1cc09a92d6aa33c2dadbb02`
- Index `77c78d56085002ad5653a3662316e3df578db84865419f1e6a4a4c695e01a920`
- Runner `462f6196d81dfc9ed1bda31ce0ae81e59759341b6671cb7fc9d9301fe453e048`

ครบ20bugs/403selected/288exclusions/691declarations. เทียบ immutable
Aom v11 `6c0f6328`: เพิ่มเฉพาะ inherited Graphics7 ของ Chart-1, รักษา
targets/exclusion reasons/fixed-source bytes ของรายการเดิม รวม Chronology6.
Historical output9390ไฟล์คง Git blob IDs เดิม. Codec5 bounded candidate ยังไม่ได้ integrate
ใน v12; ไม่เพิ่มยอดจาก395หรือ397ที่เป็นข้อเสนอรุ่นก่อน.

รัน exact Aom snapshot แยกจาก Champ shared v9: tests15ผ่าน/skip0,
consumer80 bug×approach combinations และ Gate input-binding8checks ผ่าน.
ตรวจ analytic pixel/state checker กับ raw Graphics96observations,
inherited JDI48entryrecords/7declarations, packaged JUnit24cases×4stages,
Chart8 old/new48pairs, shifted-domain-line mutation และ actual setup/fatal cause-chain2controls.
คำรับ component64cases และ Chronology13cases ผูก v12 runtime; ตรวจ buggy
arrays_bad_order negative-control signature ทั้งสองรอบ. การตรวจครั้งนี้ไม่รัน Java ใหม่
และไม่แทน final native host receipt ของบีมหรือออม.

Gate A ยังfalse; selected403ไม่ใช่คำรับ full semantic403หรือ691.
Current Champ shared runtime/checkpoint100pinsยังเป็น v9; ไม่มีการ merge runtime ใหม่
เข้า working tree แล้วอ้าง pins เดิม. ไม่มี queue/ledger/primary-result mutation.

## Worksheet และ provider จริง

คำนวณ prompt×model40คู่ใหม่จาก bytes ของ v12 พร้อม serialization hash/size.
Requested IDs: `claude-sonnet-5` / `gemini-3.5-flash-lite`; temperature0,
max_tokens4096, streamfalse. Prompt ใหญ่สุด Math-1 **304787 UTF-8 bytes**;
conditional historical byte guard **308883 + H (unknown)**. ทั้งสองค่าไม่ใช่ tokens/reserve.
JSON escaping overhead เป็น bytes ไม่ใช่ provider message framing.

ใช้ credentials ที่ผู้ใช้ให้ไว้เฉพาะ KKU HTTPS origin เพื่อขอ **read-only metadata11ครั้ง**:
GET `/models` ของ a01–a10 และ POST `/chat/models-list` ของ a01.
HTTP200ทุกครั้ง; exact IDs ทั้งสองมีจริงในทุก observation. เก็บ timestamp/raw-body SHA-256
และ model rows ที่ไม่มี credentials/ข้อมูลส่วนบุคคล. ไม่มีการส่ง prompt เพื่อสร้างคำตอบ
และไม่มี automatic account switch; runner ยัง assign generation เฉพาะa01.
Authentication200ยืนยันการใช้ key ณเวลานั้น ไม่ใช่ expiry หรือ quotaสำหรับ generation.

Model endpoints ที่ตรวจส่ง identities เท่านั้น ไม่มี usage/quota/limits/settings/framing/expiry.
[คู่มือ KKU API](https://gen.ai.kku.ac.th/docs/api) ระบุ generic temperature/max_tokens,
quota fields ใน generation response และ daily reset แต่ไม่มี token-count/expiry/context-limit
metadata endpoint หรือ exact reset timezone ในคู่มือที่รับมา.
จึงยังไม่วัด actual input tokens40คู่, uncounted framing, effective settings,
context/output limits, quota bucket/remaining/reset/expiry และ **final reserve=null** ตามจริง.
ต้องมี provider metadata/counting contract หรือการ calibration ที่ตกลงขอบเขต/งบก่อน;
อย่าใช้ quotaตัวอย่างในคู่มือหรือ vendor limits แทนบริการ KKU.

เมื่อได้จำนวน tokens ที่ตรวจสอบได้ ให้คำนวณ max ของ40คำขอ
`provider input tokens (รวม framing ที่นับแล้ว) + framing ที่ยังไม่ถูกนับ + 4096`.
ห้ามนับ framingซ้ำ. Runtime ปัจจุบันเปรียบเทียบ prompt UTF-8 bytes กับ
`prompt_token_reserve`; หาก measured-token bound ต่ำกว่า byte size ต้องแยก byte/token
admission check ใน condition/implementation ที่ทีมตรวจใหม่ก่อนใช้จริง.

## งานต่อ

ออมใช้ receipt นี้ปิด Champ scoped input/worksheet review ของv12ได้;
ยังต้องส่ง host bindings `aom-pc1` CPU1สำหรับงานออม+แชมป์ และ providerข้อมูลที่ยังขาด.
บีมส่ง v12 semantic/oracles/consumers/`beam-pc1` CPU1 receipt ที่ผูก pins ชุดนี้;
ไม่ต้องขอ scoped v10 closure ซ้ำ.
ทั้งสามคนพิจารณาข้อเสนอ bounded development experiment แยกจาก primary691 gate.
เมื่อ condition สุดท้าย freeze แล้วจึงวัด reserveบน prompts รุ่นนั้นใหม่ก่อนเปิดการสร้างคำตอบ.

## ตรวจซ้ำแบบไม่ส่ง API

```powershell
python -B -X utf8 -m scripts.study.api854.review_v12_readiness --output output/api854-20261004/champ-v12-readiness-review-v3 --provider-evidence .local/api854/v12-provider-readonly-v1
```

คำสั่งใช้ current private metadata observations ที่มีอยู่แล้วและ temporary Aom snapshot;
outputต้องเป็น pathใหม่. เครื่องเพื่อนไม่มี private observations ให้ตรวจ manifest/Git bindings
และส่ง provider packetของตนตามขอบเขตที่ตกลง โดยไม่ขอ credentialsมาลง repository.
