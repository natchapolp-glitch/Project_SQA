# ออมรับงานบีม 3ff1a6a4 และ compose fixture proposal

รับ branch beam commit `3ff1a6a415189d8f3944bf0723b2628b3b1f8d71` แล้ว
ตรวจ immutable execution packet และ eligibility import ก่อนรวม runtime กับ all-owner guards
ของ `aom d147e216`. ยังไม่ตรึง primary และไม่เปิด live pilot.

## สิ่งที่ตรวจรับแล้ว

- Checksum coverage ครบ fixture packet 221 files และ eligibility packet 21 files;
  retained runtime source bytes ตรงกับ development execution protocol ของบีม.
- Eligibility ครบ 20 pilot bugs/691 declarations/exclusions 3 ตรงกับ prepare v3 ของออม;
  ไม่เขียนทับ v1/v2/v3 หรือเปลี่ยน label discovery เป็น semantic approval.
- Four suites ของ Closure-176/JxPath-1 × CMA-ES/FSCS-ART: suite Java bytes, archive hashes,
  fixed twice, actual counters, buggy failures และ coverage summary ตรงกับ original evidence.
  Fixed/buggy/coverage stages มี executed=30/skipped=0/target_checks=30 ต่อ suite.
- Closure มี buggy failures 2 ต่อ suite; JxPath ไม่มี fault ตามผลจริง.
  บีมเก็บ separate local semantic verdict=valid; original evaluator usable=false ยังอยู่เดิม.
  ผลนี้เป็น development evidence ของสอง bugs ไม่ใช่ team approval หรือ primary pilot results.

Receipt ที่ตรวจซ้ำได้:
[aom-beam-fixture-review-3ff1a6a4.json](../../output/api854-20261003/aom-beam-fixture-review-3ff1a6a4.json).
คำสั่ง `python -m scripts.study.api854.review_beam_fixture --output /path/to/new-receipt.json`
ไม่รัน experiment หรือใช้ API.

## Shared fixture development v4

[prepare-policy.v4.json](../../experiments/configs/api854-20261003/prepare-policy.v4.json)
เป็น contract `aom-beam-prepare-v4`, prompt `shared-fixed-targets-explicit-fixtures-junit4-v4`.
ใช้ fixed context/modified-only coverage mapping/receiver partition ของ v3 และเพิ่ม recipe sources
ของ `SqaProbe.java` กับ `fixture_policy.py` พร้อม hash binding ทั้ง CPU/API.
Recipe แยกจาก production source; prompt ไม่มี observation/evaluation feedback.

[prepare-v4-fixture-development](../../output/api854-20261003/prepare-v4-fixture-development/index.json)
มี **สอง bugs เท่านั้น**, 95 capability-selected targets และเก็บทุก capability exclusion ก่อน observe.
ไม่ใช้ strict policy ครอบ 18 bugs ที่ยังไม่มี recipe review และไม่มี legacy fallback ใน v4.
Prepared recipe/prompt/targets/metadata ต้องตรง policy และ implementation ของ protocol;
prepare เก่าหรือ recipe ที่แก้ source แม้ rehash artifacts แล้วก็ถูกปฏิเสธก่อน generation claim/API send.
Prepare publication มีชื่อ `targets.json` เพียงหนึ่ง artifact และแนบ recipe ของ prepare attempt เดียวกัน.
CPU generation เก็บ full context lineage ต่อถึง evaluator และ modified-only source map ตามเดิม.

Prompt สูงสุดของ **สอง-bug v4 development proposal** = **174,475 UTF-8 bytes**.
ไม่ใช่ token count หรือ final primary reserve; v3 เดิมสูงสุด 161,982 bytes.
เมื่อเพิ่ม recipes ของ 18 bugs ที่เหลือต้องออก policy/artifacts รุ่นใหม่และวัดขนาดสุดท้ายอีกครั้ง.
แชมป์ต้องรวม provider framing และตรวจ token/context/output units/limits จริงก่อน freeze.

[protocol.fixture-development-v4.json](../../experiments/configs/api854-20261003/protocol.fixture-development-v4.json)
พร้อม `.sha256` เป็น draft, enabled stages ว่าง และ provider/quota ยังไม่ยืนยัน.
Primary [protocol.json](../../experiments/configs/api854-20261003/protocol.json) ยังใช้ v3 draft
และ re-pin runtime ที่ compose แล้ว; `.proposal.sha256` อัปเดตตาม exact bytes.
ห้ามใช้ source hashes ของ development protocol บีมแทน hashes ของ runtime ออม.
Execution บน Defects4J ของบีมยัง bind กับ original development protocol;
tests ของ shared composition ใช้ loopback/mock provider/discovery จึงไม่อ้างว่า rerun four Java experiments.

Rebuild แบบ offline:

```bash
python -m scripts.study.api854.build_prepare_v4 \
  --input output/api854-20261003/prepare-v3 --output /path/to/new-development-preparation
```

## งานที่ส่งกลับให้ทีม

- **บีม:** ดึง runtime ออมชุดนี้ ตรวจ shared v4 recipe/targets/exclusions และ rerun evidence
  ตาม condition ใหม่หากใช้ shared protocol. เพิ่ม/ตรวจ receiver/arguments/oracles สำหรับ 18 bugs ที่เหลือ
  และส่ง recipe/capability version พร้อม source hashes; ไม่แก้ assertion ของ suites ที่ส่งแล้ว.
  ปิด host acceptance ของ beam-pc1/2/3. Development four suites ไม่แทน whole-pilot acceptance.
- **แชมป์:** ดึง shared runtime ออม ตรวจ v3/v4 input guards และขนาด prompt ใหม่;
  ยังไม่เริ่ม generation. ส่ง exact models/settings/context/output caps/framing/observed quota/expiry
  และ host acceptance. Reserve ต้องยึด prompt รุ่นสุดท้าย ไม่ใช้ตัวเลข v2 หรือ receipt builder เก่า.
- **ออม/ทั้งทีม:** รวม all-pilot recipes กับ context/fixture/prompt policy รุ่นที่ตกลงกัน,
  ตรวจ runtime/host/runner และบันทึกสาม owner reviews. Gate A ผ่านแล้วจึง freeze primary/runner bytes/hash
  และ seed run ใหม่; เก็บ existing core 80 prepare jobs แยกไว้.

## Validation และสถานะ gate

ผล tests Windows/WSL และ checksums ของ immutable inputs อยู่ใน
[fixture-composition-validation.json](../../output/api854-20261003/fixture-composition-validation.json).
[Cross-platform receipt](../../output/api854-20261003/prepare-v4-cross-platform.json)
ยืนยัน rebuild Windows/WSL 27 files bytes ตรงกัน รวม recipe/prompt/manifest/metadata.

[Gate A ปัจจุบัน](../../output/api854-20261003/team-beam-fixture-gate-a-3ff1a6a4.json)
บันทึก received four development suites/two reviewed bugs และ 18 bugs pending.
ยัง pending meaningful_oracles, model settings/limits/reserve, quota, host acceptance และ three-owner review.
ไม่มี KKU requests หรือ live queue mutation ในการตรวจรับ/compose รอบนี้.

หมายเหตุการ fetch: checkout เดิมติดตาม remote branch เฉพาะ aom จึงต้องใช้ explicit refspec
เพื่ออัปเดต `origin/beam`/`origin/champ`. รอบนี้ตรวจ `git ls-remote` เทียบ full commit จริงแล้ว
และเพิ่ม fetch refs ใน local Git config เพื่อไม่ให้การตรวจครั้งถัดไปอ่าน remote-tracking ref เก่า.
