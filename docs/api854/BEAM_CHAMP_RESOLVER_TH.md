# ส่ง resolver/adapters/evaluator ให้แชมป์และออมตรวจรับ

Repository callable สำหรับ API CLI ล่าสุด:

```text
scripts.study.api854.champ_bridge:champ_suite_resolver
```

`load_suite_resolver` โหลด callable นี้ได้. APIWorker เรียก `validate_prepared` ก่อน claim
และ `create_handoff` หลังได้ claim เพื่อใช้ `BeamGenerationHandoff` กับ heartbeat เดียวกัน.
Default resolver เก่าของแชมป์ยังใช้ QueueGenerationHandoff ตามเดิม.

## Contract ที่ต้องใช้ร่วมกัน

ข้อเสนอรวมอยู่ `experiments/configs/api854-20261003/beam-champ-contract.proposal.json`.
ยังไม่ frozen; temperature/max_tokens/prompt_token_reserve/time window เป็น null จนทีม
ได้หลักฐานจริง. CLI ปฏิเสธไฟล์นี้ก่อน claim หรือส่ง KKU. อย่าเติมค่าเพื่อข้าม gate.

Primary protocol ใหม่ต้องรวม Beam implementation hashes/seed/budget/timeouts/target policy
และ Champ generation contract ใน bytes เดียวกันที่ใช้ seed queue:

- `suite_packaging` และ generation.suite_policy_id: `beam-java-suite-v1`
- `context_selection` และ generation.context_policy_id: `modified-java-and-root-build-v1`
- generation.prompt_policy_id: `beam-fixed-targets-junit4-v1`
- generation.suite_resolver: callable ข้างต้น
- generation.owners: owner shards ที่อนุญาตให้ API coordinator รับ เช่น champ/beam/aom
- worker_routing.prepare/generate/evaluate: owner shards ที่ Beam CPU worker รับข้าม owner ได้
- `status=frozen` และ `approval_state=frozen` หลังตรวจรับ; Aom ยังต้องตรึง model/settings,
  three-owner Gate A และ enabled_stages ตามกติกาคิว. Core-frozen เดิมไม่ใช้ generation/evaluate.

ไม่มีการเปลี่ยน owner ของ job. `api_worker --owner beam` จะรับ AI ของ Beam ได้เฉพาะ
เมื่อ generation.owners ใน protocol อนุญาต. `queue_worker --owner champ --stage evaluate`
จะรับ evaluation ของ Champ ได้เฉพาะเมื่อ worker_routing.evaluate อนุญาต.
ค่าปริยายยังเป็น API owner=champ และ CPU owner=beam; route ที่ไม่อนุญาตถูกปฏิเสธก่อน claim.

## Preparation และผลส่งต่อ

prepare ต้อง publish individual context-manifest.json, prompt.md และ targets.json.
metadata มี composite `source_sha256`, `context_source_hash`, `fixed_source_sha256` mapping,
prompt_sha256/prompt_policy_id, targets_sha256 และ target_count. Prompt ใช้เฉพาะ fixed source,
root build files และ eligible declaration list; ไม่แนบ compile/test logs, patch หรือ triggering tests.
core preflight ยังไม่ export prompt ที่ถือว่าผ่านการตรวจรับ.

Adapter ตรวจ source/context/targets/prompt hashes ก่อนส่ง AI และบันทึก inputs/code ก่อน send.
ผลส่งออกเป็น individual suite.tar.bz2, suite-manifest.json, generation-lineage.json,
source-map.json, generation-result.json พร้อม bundle ของหลักฐาน. evaluator ดาวน์โหลด
suite/lineage ผ่าน artifact IDs ตรวจ generation attempt, prepare mapping และ suite hash
แล้วใช้ fresh fixed/buggy worktrees. No-test/over-cap/partial source เป็น failure พร้อม raw
evidence ไม่มี pruning/repair/resend. รองรับ complete Java fences หรือหนึ่ง Java file แบบ bare;
source bytes รวม CRLF ไม่ถูกแปลง.

## หลักฐานที่ทำจริง

- 160 API854 tests ผ่าน ไม่มี skip รวม callable loader, preclaim source/target checks,
  cross-owner coordination, empty/partial output, leases และ immutable artifacts.
- APIWorker จริง + Aom HTTP server จริงบน loopback + resolver นี้ + Defects4J 3.0.1:
  Lang-4 สอง AI approaches ผ่าน 6 stage attempts. Fixed สองรอบผ่าน, buggy detected,
  coverage 12/25 lines และ 3/14 branches ทั้งคู่.
- KKU transport ของ smoke เป็น mock; Java fixture มาจาก development CMA-ES เดิม
  ไม่ใช่ model output. Quota/settings/Gate A values ใน smoke เป็น fixtures เท่านั้น.
- ยังไม่มี real KKU requests หรือ live queue mutations. usable=false รอ semantic review.
- Source snapshot ของ runtime smoke เก็บ implementation ก่อนเพิ่ม routing;
  routing extension ตรวจด้วย actual local HTTP tests ในชุดล่าสุดแยกต่างหาก.

หลักฐาน public: [beam-champ-apiworker-smoke-20261003.json](evidence/beam-champ-apiworker-smoke-20261003.json).
Adapters ของ pilot Beam 6 bugs: [beam-pilot-adapters-20261003.json](evidence/beam-pilot-adapters-20261003.json).

ถัดไปให้ทีม review interface/policies/fixtures นี้ร่วมกับ observed settings/quota จากแชมป์,
ตรึง primary protocol/run ใหม่ และทดสอบ real generation/validity ก่อนนับ Gate B.
โค้ดพร้อมให้ตรวจรับไม่ได้หมายความว่า live pilot ผ่านแล้ว.
