# ชุดส่งมอบออม: prepare v3 และ runtime ร่วม

ออมทำส่วนที่ตรวจได้โดยไม่ใช้ KKU แล้ว: import Beam `44dd5cb0`, shared context/prompt/fixture
policy v3, Chart receiver partition, CPU/API input binding, runner coverage และ Gate A evidence.
เป็น proposal สำหรับทีมตรวจรับ ยังไม่ใช่ frozen primary protocol และยังไม่เปิด live pilot.

## ไฟล์ที่ให้เพื่อนดึง

- [prepare-v3/index.json](../../output/api854-20261003/prepare-v3/index.json): 20 bugs, 691 targets,
  exclusions 3 รายการ; แต่ละ bug มี fixed-source, context-manifest, prompt, targets,
  prepare-policy, metadata, eligibility พร้อม provenance และ revision proofs/checksums.
- [prepare-policy.v3.json](../../experiments/configs/api854-20261003/prepare-policy.v3.json)
  และ `.sha256`: contract `aom-beam-prepare-v3`, context
  `modified-java-root-build-and-shared-receivers-v3`, prompt `shared-fixed-targets-junit4-v3`.
- [protocol.json](../../experiments/configs/api854-20261003/protocol.json): primary draft,
  current runtime hashes และ callable `scripts.study.api854.champ_bridge:champ_suite_resolver`.
- [runner-plan.v1.json](../../experiments/configs/api854-20261003/runner-plan.v1.json):
  Champ API coordinator ทุก owner; Beam CPU owner beam; Aom CPU owners aom/champ.
- [Gate A ล่าสุด](../../output/api854-20261003/team-preparation-v3-gate-a-release.json),
  [validation receipt](../../output/api854-20261003/prepare-v3-validation.json),
  [capacity](../../output/api854-20261003/prepare-v3-capacity.json),
  [prompt sizes](../../output/api854-20261003/prepare-v3-prompts.csv).
- [ZIP ส่งมอบ](../../output/api854-20261003/aom-prepare-pilot-v3.zip) และ `.sha256`:
  artifacts/policy/draft/runner/checklist/docs จากชุดเดียวกัน ไม่ใส่ tokens/keys.

ทุกคน fetch `aom` และ checkout commit ที่ออมแจ้ง ก่อนตรวจ ZIP SHA หรือ hashes ใน index.
เก็บ v1/v2/core เดิม; ไม่แทน URL protocol ของ 80 core-preflight jobs ด้วย primary draft นี้.
หาก artifact/hash เปลี่ยนต้องออก version ใหม่ ไม่เขียนทับ input ที่เคยรับตรวจแล้ว.

## สิ่งที่รวมแล้ว

`fixed_source_sha256` คงเฉพาะ modified Java สำหรับ evaluator target coverage.
`additional_receiver_source_sha256` เก็บ Chart AreaRenderer แยก; `context_source_hash`
รวม modified/receiver/build files. Retained revision proofs ของออมและบีมอยู่แยกกัน.
Prompt ทั้งสี่วิธีใช้ fixed context/declaration inventory/common compiled fixture classes ชุดเดียวกัน.
discovery เป็น eligibility ไม่ใช่การรับรอง meaningful construction หรือ oracle.

Algorithm queue worker ดาวน์โหลด/ตรวจ shared preparation ก่อน claim และตรวจซ้ำหลัง claim.
Fresh algorithm discovery ต้องมี source, signatures, fixture inventory และ context bytes ตรงกัน
ก่อน probe execution. ใช้ canonical declaration identity order ใน v3 ทั้ง CMA-ES/FSCS-ART.
API worker ตรวจสัญญาเดียวกันก่อน paid send. Evaluator download ตรวจ modified mapping
และ receiver/build context hash กับ prepare stage; generation/evaluation attempt IDs แยกกัน.

Suite เกิน 30 methods ถูก reject ทั้งชุดและเก็บ raw evidence; ไม่มี trimming, assertion repair,
pruning หรือส่ง compile/test/coverage feedback เข้า AI. Runtime เดิมของ v2 ยังอ่านได้ตาม contract เดิม.

Prompt ใหญ่สุดปัจจุบัน Math-1 **161,982 UTF-8 bytes**; รวมหนึ่ง copy ต่อ bug **1,338,070 bytes**.
ตัวเลขเพิ่มจาก Beam 115,826 เพราะ v3 ใส่ common fixture class inventory และใช้ shared prompt ใหม่.
ห้ามใช้ตัวเลขเดิม 99,439 หรือ 115,826 ตั้ง reserve ของชุดนี้.
ตัวเลขเป็น bytes ไม่ใช่ provider token count. ตาม worker guard numerical floor ของ prompt reserve
คือ 161,982 ก่อน framing; หากใช้ output 4096 จะมี floor ของ total reservation 166,078
ก่อน framing. ต้องตรวจ provider limits/usage/units จริง ไม่อ้างเป็น actual quota consumption.

## ให้แชมป์ทำต่อ

1. ดึง commit/ZIP ของออม ตรวจ index/policy/implementation hashes และรัน API854 composition tests.
2. คำนวณ reserve จาก prompt v3 สุดท้ายรวม provider framing แล้วตรวจ exact IDs
   `claude-sonnet-5` / `gemini-3.5-flash-lite`, temperature 0/output 4096, context/output caps
   และ resolved version เมื่อมีหลักฐาน. คงค่าที่ไม่รู้เป็น null; ไม่เติมเพื่อข้าม gate.
3. ส่ง observed remaining/unit/bucket/window/expiry พร้อม observed time และ evidence/hash
   ของบัญชีที่จะใช้จริง. โควตาที่สองโมเดลแชร์กันต้องเป็น bucket เดียว ไม่คูณยอด.
   ใช้ provider-evidence template ใน branch champ; ไม่แนบ API keys.
4. ยืนยัน readiness/assignment ของ champ-pc1 และ review shared policy/runner/protocol.
   primary `--check` ต้องรอ frozen protocol + observed ledger; ไม่เปิด `--once` ตอนนี้.
   ต้องแจ้งก่อนสลับบัญชีและไม่ส่งซ้ำอัตโนมัติ.

## ให้บีมทำต่อ

1. ตรวจ v3 declarations/fixture inventory/exclusions และ Chart receiver hashes เทียบหลักฐานของตน.
   Discovery 20 bugs ส่งและ import แล้ว ไม่ต้องส่งชุดเดิมซ้ำ.
2. ปิด meaningful receiver/arguments/oracle review และ executed/skipped/target execution evidence.
   เริ่ม Closure-176/JxPath-1 ที่มี exception/null สูง. Fixed twice/buggy/coverage เดิมเก็บไว้;
   `fault_detected=false` และ usable=false ตามจริง ไม่ถือเป็น live pilot.
3. ตรวจ algorithm canonical order กับ fixture policy และ bridge/evaluator รุ่นนี้.
   หากแก้ fixture/generator ให้ส่ง version/diff/hash และ rerun evidence ของ condition ใหม่;
   ห้ามซ่อม assertion ของ suite เดิม. ห้ามนับ mock AI suite เป็น KKU output.
4. ยืนยัน environment และ runner assignment ของ beam-pc1/2/3 พร้อม team review.

## ส่วนที่ยังเป็นงานออมเมื่อหลักฐานมาครบ

รวบรวม provider/semantic/host evidence และ review สามคน จากนั้นตรึง primary protocol/runner
bytes/hash และ seed run ใหม่. ไม่ทำได้จากข้อมูลปัจจุบันเพราะ settings/quota/semantic ยังไม่ยืนยัน.
Environment aom-pc1 ตรวจจริงแล้ว ready (WSL Linux, Java/Javac 11, Defects4J 3.0.1);
aom-pc2 ยังไม่มีหลักฐานของเครื่องนั้น ไม่คัดลอก receipt ของ pc1 ไปใช้แทน.

คำสั่งตรวจแบบ offline:

```bash
python3 -m unittest discover -s scripts/study/api854/tests -t .
python3 -m scripts.study.api854.gate_a --output /path/to/new-gate-a-checkpoint.json
```

สร้าง preparation ซ้ำได้ใน output ใหม่ด้วย `build_prepare_v3 --input output/api854-20261003/prepare-v1
--review docs/api854/evidence/beam-aom-review-20261003 --output /path/to/new-preparation`.
ใช้ D4J executable บนเครื่องออม `/home/aomsin/sqa-round2/defects4j/framework/bin/defects4j`
เป็น `--d4j` แทนสมมติว่าอยู่ใน PATH. สำหรับเครื่องอื่นให้ใช้ path ที่ตรวจของเครื่องนั้น.

Validation: API854 WSL 210 ผ่านไม่มี skip; Windows 209 ผ่าน/skip symlink 1;
legacy evaluator 16/generator 4 ผ่าน. Tests ใช้ isolated Store/mock observations/provider
และตรวจ shared-v3 source/fixture tampering, CPU/API preparation parity, receiver/evaluator lineage.
ไม่มี Java experimental run ใหม่หรือ KKU/live queue mutation จากรอบทำ v3 นี้.
