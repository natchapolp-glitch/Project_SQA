# งานที่แชมป์เตรียมไว้ก่อนรับหลักฐานครบ

สถานะบีมที่ผู้ใช้แจ้งหลัง checkpoint นี้: [27/40 suites และงานให้ครบ 691 declarations](CHAMP_BEAM_V5_PROGRESS_TH.md)
ตารางด้านล่างแสดงเฉพาะไฟล์ที่แชมป์รับและตรวจแล้ว ไม่ใช่สถานะสดของงานที่บีมกำลังรัน

อ้างอิง `champ 19ef7ae6` และ shared-v5 ของ `aom 76b88e25`
ตรวจ identities/owners, source pins, review hashes และ runner assignment จากไฟล์ที่ทีมส่งมาแล้ว
รอบนี้ไม่มี KKU request, live queue mutation หรือการแก้ quota ledger
ตัวตรวจ API worker ยังปฏิเสธ proposal ที่ไม่ frozen ตามเดิม

## ตารางตรวจรับ 20 pilot bugs

Job owner เป็น ownership เดิม ส่วน CPU prepare runner มาจาก v5 runner proposal ไม่ได้ย้าย ownership
V3 declarations เป็น discovery inventory; V5 targets เป็น subset ตาม reviewed capability recipes
Local development review ไม่ใช่ shared primary/team semantic approval

| Bug | Job owner | CPU prepare ตาม proposal | V3 declarations | V5 targets | Local suite review |
|---|---|---|---:|---:|---|
| Chart-1 | champ | aom-pc1 | 56 | pending | ยังไม่มีใน packet ที่รับ |
| Cli-1 | aom | aom-pc1 | 16 | pending | ยังไม่มีใน packet ที่รับ |
| Closure-1 | aom | aom-pc1 | 14 | pending | ยังไม่มีใน packet ที่รับ |
| Closure-176 | beam | beam-pc1 | 50 | 28 | มี 2 algorithm suites |
| Codec-1 | aom | aom-pc1 | 18 | 12 | มี 2 algorithm suites |
| Collections-1 | aom | aom-pc1 | 22 | 12 | มี 2 algorithm suites |
| Compress-1 | champ | aom-pc1 | 19 | pending | ยังไม่มีใน packet ที่รับ |
| Csv-1 | aom | aom-pc1 | 7 | 5 | มี 2 algorithm suites |
| Gson-1 | champ | aom-pc1 | 5 | pending | ยังไม่มีใน packet ที่รับ |
| JacksonCore-1 | champ | aom-pc1 | 44 | pending | ยังไม่มีใน packet ที่รับ |
| JacksonDatabind-1 | aom | aom-pc1 | 37 | pending | ยังไม่มีใน packet ที่รับ |
| JacksonDatabind-112 | champ | aom-pc1 | 12 | pending | ยังไม่มีใน packet ที่รับ |
| JacksonXml-1 | beam | beam-pc1 | 47 | pending | ยังไม่มีใน packet ที่รับ |
| Jsoup-1 | beam | beam-pc1 | 12 | pending | ยังไม่มีใน packet ที่รับ |
| JxPath-1 | beam | beam-pc1 | 84 | 67 | มี 2 algorithm suites |
| JxPath-22 | beam | beam-pc1 | 44 | pending | ยังไม่มีใน packet ที่รับ |
| Lang-1 | aom | aom-pc1 | 47 | pending | ยังไม่มีใน packet ที่รับ |
| Math-1 | champ | aom-pc1 | 85 | pending | ยังไม่มีใน packet ที่รับ |
| Mockito-1 | beam | beam-pc1 | 15 | pending | ยังไม่มีใน packet ที่รับ |
| Time-1 | champ | aom-pc1 | 57 | pending | ยังไม่มีใน packet ที่รับ |

รวม discovery 691 declarations; v5 เตรียมแล้ว 5 bugs / 124 targets พร้อม received local reviews 10 suites
อีก 15 bugs แบ่งตาม job owner: champ 7, aom 4, beam 4
บีมเป็นผู้ตรวจ adapters/fixtures/evaluator ตามแผนทีม ไม่ได้หมายความว่า 15 bugs เปลี่ยน owner เป็น beam
ข้อมูลที่ตรวจพร้อม path/hash ของแต่ละ review อยู่ใน [planning receipt](../../output/api854-provider-preflight-20261003/champ-v5-waiting-work-v1.json)

## หลักฐานที่ต้องรับในรอบถัดไป

บีมส่งสำหรับแต่ละ bug ที่ยัง pending:

1. Prospective receiver/arguments recipe และข้อจำกัด method/fixture ที่อ่านจาก fixed production declarations/source
2. Meaningful deterministic oracle และทุก capability exclusion พร้อมเหตุผล ไม่เลือกจาก buggy outcomes
3. Suite bytes/hash, fixed สองรอบ, buggy และ target coverage พร้อม executed/skipped/target-check counters
4. Source/recipe/capability policy version ที่จะส่งให้ทั้งสี่ approaches ใช้ และ local semantic review แยกจาก original evaluation result

ทั้งห้าบั๊กที่มี local review แล้วก็ยังต้อง review semantic/shared contract ร่วมทีมกับ input version ที่จะใช้จริง
ออมรวม recipes ที่รับแล้วเป็น shared preparation รุ่นใหม่ให้ครบ 20 bugs ก่อนตรึง primary condition
ต้องใช้ protocol/runner/source/recipe/prompt hashes ของรุ่นเดียวกัน และคงหลักฐานเก่าไว้

ออมต้องผูก Gate A checklist กับคู่ protocol/runner ที่เลือกจริงด้วย
ตัว `gate_a.py` ปัจจุบันยังอ่าน `protocol.json` / `runner-plan.v1.json` โดยตรง และการ validate ไม่ส่ง fixture recipe
จึงยังไม่ใช่ตัวตรวจ v5 pair ที่ครบถ้วน ให้ตรวจแก้ส่วนนี้ก่อนใช้ checklist เป็นหลักฐาน Gate A
ในช่วงนี้ใช้ [v5 audit](../../output/api854-provider-preflight-20261003/champ-aom-v5-input-runner-audit-v1.json)
และ [validation](../../output/api854-provider-preflight-20261003/champ-v5-validation-v1.json) ประกอบ review โดยไม่ถือว่า gate ผ่าน

## Worksheet ของ reserve สำหรับห้าบั๊กที่มี inputs

ใช้ remaining จาก KKU a01 เมื่อ 2026-10-03 07:13:41–43 เวลาไทยเท่านั้น
Sonnet = 199,961 และ Gemini = 349,979 tokens ไม่ใช่ยอดสดในขณะเปิดเอกสาร
ตัวเลขต่อไปนี้ใช้ numerical floor ตาม conservative byte guard และ proposed output 4096

| Bug | Reservation floor ก่อน H | Headroom Sonnet ก่อน H | Headroom Gemini ก่อน H |
|---|---:|---:|---:|
| Closure-176 | 181,794 + H | 18,167 - H | 168,185 - H |
| Codec-1 | 111,927 + H | 88,034 - H | 238,052 - H |
| Collections-1 | 145,298 + H | 54,663 - H | 204,681 - H |
| Csv-1 | 70,643 + H | 129,318 - H | 279,336 - H |
| JxPath-1 | 145,290 + H | 54,671 - H | 204,689 - H |

H เป็น provider framing bound ที่ยังไม่ทราบ ตัวเลข headroom คือ snapshot remaining ลบ reservation floor ก่อน H
ไม่ใช่การจองจริง ไม่ใช่ actual token consumption และไม่อนุมัติให้รัน batch
การสะสม floor ของหลายคำขอไม่ใช่ยอด quota ที่ใช้จริง เพราะ ledger ต้อง reconcile actual usage หลังแต่ละคำขอ
ยังไม่ import observation เพราะ bucket/window/reset/expiry และ limits ไม่ครบ
อีก 15 bugs ยังไม่มี final recipe prompts จึงไม่ประมาณ max prompt หรือ final reserve ของ 20 bugs จากตารางนี้

## ข้อมูลที่เจ้าของบัญชีต้องขอจาก KKU

เตรียมข้อความนี้ไว้ส่งผ่านช่องทางของเจ้าของบัญชี ไม่มีการส่งข้อความถึงผู้ดูแลจาก agent:

> ขอ metadata ของ `claude-sonnet-5` และ `gemini-3.5-flash-lite` สำหรับ KKU API ครับ:
> underlying provider/model revision, context/input/output ceilings, effective support ของ temperature 0 และ max_tokens 4096,
> วิธีนับ input tokens รวม provider framing หรือ endpoint count-tokens ที่รองรับ,
> quota bucket IDs และ models ที่ใช้ bucket ร่วมกัน, unit/window, เวลา reset พร้อม timezone และอายุ observation ที่ควรใช้
> มีหลักฐาน preflight HTTP 200 แล้ว แต่ responses ให้เพียง model alias, usage และ daily quota counters ครับ

ไม่ต้องส่ง API key ให้เพื่อนเพื่อเติมข้อมูลเหล่านี้ ใช้ account alias และ evidence/hash ได้
เมื่อได้ข้อมูลให้แชมป์ตรวจความสัมพันธ์ของ bucket และกำหนด observation expiry ก่อนนำเข้า ledger
เมื่อครบ 20 final prompts จึงคำนวณ reserve ใหม่และตรวจ context/output limits ของแต่ละโมเดล
ยังต้องลง semantic/host/shared contract review และ Gate A ร่วมกันก่อนออมสร้าง primary queue ใหม่

## ผลตรวจงานเตรียมนี้

ตรวจครบ 20 identities/owners, 5 prepared bugs, 15 missing recipes, 10 conditional budget rows และทุก review hash ที่อ้าง
Source pins ตรงกับ runtime ที่ผ่าน full validation 279 tests / skip 1 ใน commit 19ef7ae6
งานรอบนี้เพิ่มเอกสารและ planning receipt จึงตรวจข้อมูล/links/hash โดยไม่รันทดสอบระบบทั้งหมดซ้ำ
ยังไม่เปิด live pilot
