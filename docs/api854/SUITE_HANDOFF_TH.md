# suite.tar.bz2 สำหรับ Champ → Beam evaluation

สัญญา `beam-java-suite-v1` วันที่ 2026-10-03 เป็นข้อเสนอส่งต่อที่ใช้กับ evaluator
ปัจจุบันได้ ต้องตรึงพร้อม protocol ของออมก่อนเริ่ม primary/pilot ที่นับผล

## รูปแบบ archive

- ชื่อ artifact `suite.tar.bz2`; tar บีบอัด bzip2, ภายในมีเฉพาะไฟล์ `.java`
- ไฟล์อยู่ตาม package โดยตรง เช่น `org/example/GeneratedTest.java` มี
  `package org.example;` ส่วน default package อยู่ root ห้ามครอบด้วย `src/` หรือ `tests/`
- ใช้ JUnit 4 และ Java 11 ตาม environment; ห้ามใส่ `.class`, jar, JSON, Markdown,
  log, absolute path, `..`, symlink, hardlink หรือชื่อที่ชนกันเมื่อละตัวพิมพ์ใหญ่เล็ก
- Defects4J 3.0.1 เลือกทุก `.java` เป็น test class ยกเว้น `*_scaffolding.java`
  ดังนั้น helper ควรเป็น nested class ใน test; ถ้าใช้ scaffolding ต้องมีชื่อ/อ้างอิง
  ถูกต้องอยู่แล้ว ตัวแพ็กไม่เปลี่ยนชื่อหรือแก้โค้ดให้
- เก็บ bytes ของ Java ตาม producer ส่งมา ไม่ตัด methods ไม่แก้ fixture/oracle
  ไม่ normalize line endings; response ที่ truncated/refused/ไม่มี test ต้องเป็น
  failure ของ generation และห้ามส่ง outcome `generated`
- เรียงชื่อไฟล์, tar USTAR, mode `0644`, mtime/uid/gid `0`, uname/gname ว่าง
  ทำให้ input bytes/path เดิมได้ suite SHA-256 เดิม ไม่ขึ้นกับเวลา/เครื่อง
- กำหนดไม่เกิน 30 test methods ตาม proposal ปัจจุบัน ใช้ cap จาก frozen protocol
  ตัวแพ็กบันทึกจำนวนที่ producer แจ้งเท่านั้น จำนวน executed/skipped ต้องวัดจริงภายหลัง
- จำกัด artifact บีบอัด 20 MiB ตามคิว และ source รวม 100 MiB ตามตัวแพ็ก

## คำสั่งแพ็ก

เตรียมโฟลเดอร์ sources ให้มีเฉพาะ Java ที่ต้องอยู่ใน archive ไม่ชี้ทั้ง working directory
รันจาก root repository ด้วย Python 3 (Windows หรือ Linux; evaluation ใช้ Linux):

```bash
python3 -m scripts.study.api854.pack_suite \
  --sources /path/to/java-sources \
  --output /path/to/new-generation-attempt/package \
  --test-count 30 --test-method-cap 30
```

ได้ `suite.tar.bz2` และ `suite-manifest.json` แยกกัน โฟลเดอร์ output ต้องใหม่และอยู่นอก
sources ถ้า validation ไม่ผ่านห้าม upload/publish ตัวแพ็กไม่ compile หรือยืนยัน semantic validity

## หลักฐานส่งต่อ

Producer เก็บ request/response ต้นฉบับกับ hashes และสร้าง `generation-lineage.json`
โดยใช้ข้อมูลจริงจาก attempt ไม่เติมค่าประมาณ:

```json
{
  "schema_version": 1,
  "job": {
    "schema_version": 1,
    "run_id": "RUN_ID",
    "project": "Lang",
    "bug_id": 4,
    "approach": "kku-claude",
    "protocol_hash": "SHA256_OF_FROZEN_PROTOCOL",
    "repeat_index": 1,
    "attempt_id": "GENERATION_ATTEMPT_ID"
  },
  "observed_outcome": "generated",
  "suite_sha256": "SHA256_FROM_SUITE_MANIFEST",
  "test_count": 30,
  "fixed_source_sha256": {"CHECKOUT_RELATIVE_SOURCE_PATH": "SOURCE_SHA256"},
  "semantic_validity": "pending_review"
}
```

ตัวอย่างมี placeholders ห้ามใช้เป็น job จริง `fixed_source_sha256` ต้องเป็นชุด modified
source files จาก fixed checkout ที่ใช้ทำ context ให้ตรงกับ Beam `worker.fixed_sources`
ไม่ใช่ hash ของ generated tests (ชุดนั้นอยู่ `suite-manifest.source_sha256`)
evaluation ใช้ attempt ID ใหม่ แต่ generation lineage ต้องคง ID เดิมและ job key/protocol เดิม

Upload suite, manifest, lineage และ provenance เป็น artifacts ของ generation attempt เดียวกัน
ก่อน `QueueClient.complete(claim, "generated", artifacts, metadata)`:

```python
artifacts = [client.upload(claim, folder / name) for name in
             ("suite.tar.bz2", "suite-manifest.json", "generation-lineage.json", "provenance.json")]
assert artifacts[0]["sha256"] == manifest["suite_sha256"]
metadata = {
    "suite_sha256": manifest["suite_sha256"],
    "test_count": manifest["test_count"],
    "fixed_source_sha256": lineage["fixed_source_sha256"],
    **{key: provenance[key] for key in
       ("requested_model", "actual_model", "account_alias", "prompt_sha256",
        "response_id", "usage", "model_quota")}
}
client.complete(claim, "generated", artifacts, metadata)
```

`provenance` ต้องมี `requested_model`, `actual_model`, `account_alias`, `prompt_sha256`,
`response_id`, `usage`, `model_quota` ตาม schema ออม ค่าไม่ทราบให้เป็น null ไม่อ้างว่าได้รุ่นตรงกัน
ก่อนเรียก AI ต้องตรึง exact KKU ID; ไม่สลับโมเดลอัตโนมัติ หาก actual model ไม่ตรงให้เก็บ
หลักฐานและแยก failure/review ห้ามปะปนใน primary condition ห้ามแนบ API key หรือ worker token
ใน artifacts; upload หรือ complete ที่ผลตอบกลับไม่แน่นอนต้อง reconcile ก่อน retry

Evaluator download ผ่าน `QueueClient.download` ตรวจ hash จาก receipt และ `validate_lineage`
ตรวจ hash เทียบ generation จากนั้นคัดลอก bytes เข้าพื้นที่ attempt ทดสอบ fixed สองครั้ง,
buggy, coverage และ semantic review การแพ็กผ่านหรือ queue complete ไม่ทำให้ `usable=true`

## โมเดลที่ผู้ใช้แจ้งจากแชมป์

เปลี่ยนข้อกำหนดเดิมของวันที่ 2026-10-03 เป็น Claude Sonnet 5.0 สำหรับ `kku-claude`
และ Gemini 3.5 Flash Lite สำหรับ `kku-gemini` ห้ามนำ Haiku smoke เดิมมานับรวมเงื่อนไขใหม่
บันทึกใน `configuration.proposal().model_policy` ผู้ใช้แก้ไข ID ที่แจ้งผิดและระบุ
`claude-sonnet-5` กับ `gemini-3.5-flash-lite` แล้ว ใช้สองค่านี้เป็น requested model IDs
สถานะ `user_supplied_ids_pending_api_preflight` ต้องตรวจการเข้าถึง/actual model กับ KKU
ก่อนรัน primary ไม่มี silent fallback ไป Haiku, Sonnet 5.5 หรือโมเดลอื่น

ชื่อ API ของผู้ผลิตคือ `claude-sonnet-5`
([Anthropic](https://www.anthropic.com/news/claude-sonnet-5)) และ `gemini-3.5-flash-lite`
([Google](https://ai.google.dev/gemini-api/docs/models/gemini-3.5-flash-lite))
สองชื่อดังกล่าวถูกกำหนดโดยผู้ใช้สำหรับ KKU แล้ว แต่ยังไม่ได้ตรวจ live catalog:
[KKU API docs](https://gen.ai.kku.ac.th/docs/api) ระบุ
base URL `https://gen.ai.kku.ac.th/api/v1` และ `GET /models` ใช้ Bearer KKU API key
ตัวอย่างรายการใน docs เป็นตัวอย่าง ไม่ใช่รายการที่ยืนยันสองรุ่นนี้
