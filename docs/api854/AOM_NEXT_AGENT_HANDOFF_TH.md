# Handoff สำหรับผู้ทำ branch aom ต่อ — 4 ตุลาคม 2026

## อ่านก่อนเริ่ม

ผู้ใช้คือ **ออม** ให้ทำงานฝั่ง Aom ใน repo `natchapolp-glitch/Project_SQA`.
Checkout: `C:/Users/ACER/Documents/ChatGPT/SQAProj/Project_SQA_aom`, branch `aom`.
Shell: PowerShell; Python/WSL ใช้ตามงานเดิม. ตรวจ `git status` ก่อนแก้ไฟล์ทุกครั้ง.
ผู้ใช้เคยอนุญาตทำงานที่พร้อมและ push branch aom แล้ว ให้ส่งมอบผ่าน GitHub พร้อมข้อความที่ผู้ใช้คัดลอกให้บีม/แชมป์ ไม่ใช่ส่งข้อความตรงเอง.
คำขอล่าสุดคือเขียน handoff เนื่องจาก context/token ใกล้หมด: **รอบนี้ยังไม่ได้เริ่ม implement Codec**. ก่อนหน้านี้เป็นการถามว่าแต่ละข้อความมีงานอะไรต้องทำ.

อ่านไฟล์นี้ แล้วอ่าน `START_HERE.md`, `AOM_GRAPHICS_V12_HANDOFF_TH.md`, `AOM_PILOT_GATE_DECISION_TH.md` และคำรับ Codec จาก peer commits ด้านล่าง. เอกสารเก่ามีตัวเลขหลายรุ่น ให้ใช้รุ่นล่าสุดและ exact pins.

## Checkpoint ที่เสร็จและ push แล้ว

Code/artifact baseline: **63ad195623c2ed3f67f3ae232c00c54d3160ce72** บน `aom`.
ชื่อ commit: `Integrate bounded Graphics2D recipes into shared v12 development inputs`.
ตอนเริ่มเขียน handoff working tree สะอาด. Commit handoff ที่ตามมาจะเพิ่มเอกสารเท่านั้น.

- Shared development **v12**: 20 bugs, **403 selected / 288 exclusions / denominator 691**.
- รวม Graphics exact 7 ต่อจาก Chronology v11 โดยรักษา setter/JDOM/Math/Buffer/Lang และ Chart 8 เดิม.
- Prompt ใหญ่สุด **304,787 UTF-8 bytes**; ไม่ใช่ provider tokens หรือ reserve.
- Full regression ผ่าน **82 tests**, ไม่มี skips (WSL, ประมาณ 317 วินาที).
- ตรวจ Git blobs ที่ seal 1,109 hashes รวม runtime 41 pins; historical committed outputs 9,390 ไฟล์ยังตรงเดิม.
- ทุก gate/stage ของ live ยังปิด; reserve ยัง null. ไม่ได้เรียก KKU หรือ mutate live queue/quota ledger หรือเพิ่ม primary results.
- 403 selected ไม่ใช่การรับรอง semantic ครบ 403 และยังไม่ครบ requirement 691.

## Artifact หลักของ v12

ทุก path ด้านล่างอยู่ภายใน repo:

- `output/api854-20261003/prepare-v12-graphics-development-v1/`: index, 20 preparation packets, context/prompt/metadata/targets/exclusions.
- `output/api854-20261003/aom-continuation-v12-development-v1/`: protocol.proposal.json, runner-plan.json, policy/checksums. Runner bytes รักษาจาก v11 แต่ protocol/pins เป็น v12.
- `output/api854-20261003/aom-graphics-v12-intake-v1/`: immutable peer receipts และ Git-byte provenance.
- `output/api854-20261003/aom-graphics-v12-integration-v5/`: **current accepted development proof**; versions 1/2 เป็น failed attempts, 3/4 superseded หลัง hardening. อย่าเลือก proof เก่าแทน v5.
- `output/api854-20261003/aom-graphics-v12-environment-controls-v2/`: current setup/target failure controls; v1 superseded.
- `output/api854-20261003/aom-v12-preserved-runtime-v1/`: retained 64 cases + Chronology 13 cases ภายใต้ runtime v12.
- `output/api854-20261003/aom-v12-readiness-v1/`: Gate checklist, worksheet **40 prompt/model pairs**, full regression logs, completion receipt และ final checksums.

Graphics proof: 24 reference cases fixed/buggy อย่างละสองรอบ, exact JDI entry ครบ 7 signatures/24 cases, nested JUnit counters 24 executed / 0 skipped / 24 target checks; Chart8 เทียบ v11/v12 48 pairs ตรงกัน. Sensitivity และ fixture-failure rejection มีหลักฐาน. **Graphics reference ยังไม่พบ fault**; exact method entry ไม่ใช่ line/branch coverage percentage. Time-1 retained negative reference ยังเป็น arrays_bad_order อย่าเปลี่ยนผลย้อนหลัง.

## สารใหม่จากบีม/แชมป์ที่ตรวจแล้ว

Fetch และอ่านเอกสารด้วย `git show` แล้วในรอบก่อนเขียนไฟล์นี้:

1. **Champ e1e1701a** — `docs/api854/CHAMP_BEAM_V10_REAFFIRMATION_TH.md`.
   รับ Beam f708595d; 437 unique Git blobs ตรง. ปิด scoped v10 inputs/bounded-oracle/Beam-host waits. ไม่ใช่คำรับ v12 และไม่ใช่ full semantic/Gate A. สถานะที่พูดถึง Aom v11/Graphics ยังไม่รวมในเอกสารนั้นเก่ากว่า Aom v12 ที่ push แล้ว. ไม่ต้องรัน v10 ซ้ำหรือขอคำรับเดิมซ้ำ.
2. **Beam a44f796f6b72281b0bd39d3f26bfc80519e00710** — separate Codec candidate packet. ข้อความว่า “รอ Champ” ถูกปิดโดยข้อ 3 แล้ว.
3. **Champ af1fe272** — `docs/api854/CHAMP_CODEC_JOINT_REVIEW_TH.md`.
   บีมและแชมป์รับร่วม Codec 5 exact signatures ใน bounded domain แล้ว. **พร้อมให้ Aom implement/test prospective shared integration** แต่ยังไม่ได้ accepted into shared inputs.
4. ข้อความว่า Champ ตรวจ Codec เสร็จและยังไม่มี Aom shared Codec เป็นการแจ้งซ้ำข้อ 3 ไม่ใช่งานอีกชุด.

Remote ณ ตอนตรวจ: origin/beam a44f796f, origin/champ af1fe272. Fetch ใหม่ก่อนเริ่ม อาจมีคำรับเพิ่มเติม; อย่าทำ stale packet ทับงานใหม่.

## งานถัดไปที่พร้อม: Codec shared integration

ใช้ **v12 403/288 เป็น baseline** อย่ากลับไปใช้ v10 390/301.
ตัวเลข **395/296** ใน packet คือข้อเสนอ Codec-only ต่อจาก v10 ไม่ใช่ผลที่จะใช้กับ v12.
ยังไม่ได้ตรวจ exact identity union กับ targets/exclusions v12 ในรอบนี้ จึงอย่าอ้าง 408/283 เป็นผลสำเร็จ. ตรวจว่า exact 5 อยู่ใน exclusions จริงและไม่มี overlap แล้วจึงคำนวณผลหลัง integration ผ่าน.

Accepted targets (class / method / parameter_types; constructor_types เป็น empty string):

- `org.apache.commons.codec.language.Metaphone.isNextChar(java.lang.StringBuffer,int,char)` — 8 cases.
- `org.apache.commons.codec.language.Metaphone.isPreviousChar(java.lang.StringBuffer,int,char)` — 8 cases.
- `org.apache.commons.codec.language.Metaphone.isVowel(java.lang.StringBuffer,int)` — 9 cases.
- `org.apache.commons.codec.language.Metaphone.regionMatch(java.lang.StringBuffer,int,java.lang.String)` — 9 cases.
- `org.apache.commons.codec.language.SoundexUtils.difference(org.apache.commons.codec.StringEncoder,java.lang.String,java.lang.String)` — 9 cases.

4 Metaphone methods เป็น private instance; difference เป็น package-local static. ใช้ exact getDeclaredMethod/JVM descriptors ไม่ใช่เลือก overload ตามชื่อ.
Fresh real non-null StringBuffer และ fresh default Metaphone/maxCodeLen=4. difference ใช้ **real Metaphone StringEncoder** และ literal encoded references ที่ seal ก่อน invoke.
อ่าน exact case domains จาก policy/verdict: uppercase bounded buffers/indices/needles; isVowel invalid indices คาด exact StringIndexOutOfBoundsException; regionMatch empty needle มี boundary semantics; difference strings จำกัด null/empty/A/a/E/B/AB.
Oracles ต้องตรวจ scalar/exception และ unchanged full buffer contents/length/capacity/maxCodeLen state ตาม scoped verdict.
**ไม่ได้รับ arbitrary encoder/maxCodeLen/null buffer/null needle/null encoder/non-ASCII/overflow/EncoderException propagation/full public-caller semantics.** null strings ที่ legal ใน difference ไม่ใช่การรับ null encoder.

Current joint packet:
`output/api854-20261004/champ-codec-joint-review-v2/` จาก Champ commit af1fe272.

- joint-verdict.json SHA-256 `f676f016e6f2132e4967fdb5997af9b74eef7d87b1a796ebccad6c480c970f9b`
- receipt.json SHA-256 `787bd56f78ff919450cfa47ac914258c5bedaba0fc58961de85d1f4711e2050e`
- checksums.json SHA-256 `d81d3a2ac25c62c51a29345a5079ec08fa1932dfb837af3d62c66b934bc6c447`
- return index: `output/api854-20261004/champ-codec-return-index-v1.json`
- Champ reviewer: `scripts/study/api854/review_codec_candidates.py`

Champ evidence: 43 cases fixed/buggy อย่างละสองรอบ, JDI exact 5 targets, guard tests 7 passed; production counters 43/0/43, fault=false. Temporary mutation sensitivity ไม่ใช่ discovered production bug.
Original Codec fixed revision `52d82d1dfff8c2b2ded9d843e0b03017af6d747c`; buggy `9c0cabead7cf075308b11362172ae1a48d41321c`.
Champ v1 failed เพราะ Windows Git EOL archive; v2 แก้ด้วย **operation-local** `git -c core.autocrlf=false archive`. อย่าแก้ global Git settings หรือเขียน receipts เดิมย้อนหลัง.

## ลำดับทำงาน

1. Fetch/status แล้ว validate peer receipt/hashes/exact original revisions และ candidate identity delta จาก v12. Import แบบ immutable provenance เฉพาะจำเป็น ไม่ blind merge ทั้ง branch.
2. เพิ่ม runtime/fixture/oracle scope สำหรับ exact Codec5 ตาม verdict ให้ CPU algorithms และ AI prompts มี source/context/fixture/oracle knowledge เหมือนกันทั้ง 4 approaches.
3. Regress **Codec13 เดิม** โดยเฉพาะ setter/getter state และรักษา Graphics/Chronology/setter/JDOM/Math/Buffer/Lang ทั้งหมด. แยก harness/fixture failure จาก target observation.
4. สร้าง fresh proof fixed twice/buggy/exact entry/counters/sensitivity/consumer checks ของ shared implementation จริง ไม่เอา peer standalone proof มาแทน local shared proof.
5. สร้าง development condition รุ่นใหม่ (เลือกชื่อ/version หลังตรวจ remote), preparation20bugs, context-manifest/prompt/metadata/checksums ให้ตรงกัน พร้อม index/protocol/runner/runtime pins และ worksheet40 คู่. เก็บ v12/เก่าเป็นหลักฐานย้อนหลัง ไม่ overwrite.
6. ตรวจ artifacts/test/seal พร้อม raw logs และ staged Git bytes ก่อน commit/push aom. อัปเดต handoff + AOM_TO_TEAM_MESSAGE_TH.md โดยผลใหม่ยังปิด live/Gate A และ reserve null จนมี final approvals/evidence.
7. ส่งต่อให้ Beam ตรวจ bounded semantic + all4 consumers + host binding และ Champ ตรวจ runtime/runner + actual provider/final reserve ของ **รุ่นเดียวกัน**.

## จุดเริ่มต้นในโค้ดและคำสั่งตรวจเดิม

Shared runtime: `algorithms/java/SqaProbe.java`.
Policy/runtime binding: `scripts/study/api854/fixture_policy.py`, `preparation.py`, `common.py`, `prepare_worker.py`, `api_worker.py`, `generate.py` (ตรวจ path จริงด้วย rg ก่อนแก้).
Graphics registry ถูกเก็บใน hash-bound fixture_policy.py; อย่าให้ policy ดึง candidate registry จาก unbound operational module.
Current live FrozenSettings whitelist ยังไม่รับ v12; ไม่ควรขยาย live approval โดยอัตโนมัติเพราะ development integration ผ่าน.

ดู patterns ใน `scripts/study/api854/`:
`graphics_v12.py`, `verify_graphics_v12.py`, `build_prepare_v12_development.py`, `compose_v12_development.py`, `verify_graphics_environment_failure.py`, `verify_v12_preserved_runtime.py`, `run_v12_regression.py`, `seal_v12_handoff.py`.
Tests: `scripts/study/api854/tests/test_graphics_v12.py`, `test_preparation_v12_development.py`.
อ่าน argparse/README/log command จริงก่อน execute ไม่เดา flags. `run_v12_regression.py` รวม 82 tests; logs ใน readiness. อย่ารัน verifier/sealer ที่เขียน output directory เดิมจนเปลี่ยน historical evidence.

Git EOL: operation-local archive ใช้ core.autocrlf=false ถ้าต้องตรวจ original Git bytes. Private credentials/worker token ไม่ควรเข้า commit หรือ tool output.
หลีกเลี่ยง broad git add; stage เฉพาะ output/code/docs ของงานใหม่แล้วตรวจ inventory/secret/size/hashes. ไม่ทำ recursive delete/move ข้าม checkout.

## งานที่ยังต้องรอและเงื่อนไข live

- Final owner-host acceptance ผูก exact new protocol/index/runner/runtime และ all4 consumers. Fresh Java local proof ไม่เท่ากับ owner-host receipt.
- Actual provider model IDs/effective temperature0/output4096, context/output limits, tokenization+framing, reserve, observed quota/bucket/reset/expiry พร้อม timestamp/evidence. Requested IDs ปัจจุบัน claude-sonnet-5 / gemini-3.5-flash-lite ยังไม่ใช่การยืนยัน provider.
- Runner proposal: Champ API coordinator ทุก owner; Beam CPU1 สำหรับ Beam-owned CPU jobs; Aom CPU1 สำหรับ Aom+Champ-owned CPU jobs. API max outstanding1/account/global2. ต้องตรวจผูกกับรุ่นใหม่จริง.
- ทีมต้องเลือกเกณฑ์ตาม AOM_PILOT_GATE_DECISION_TH.md: original full691 primary requirement หรือ separately disclosed bounded development experiment พร้อม policy/checker และคำรับทั้ง3. ยังไม่มีคำตัดสินให้เปิด.
- อย่าโอนคำรับ v10 ไป v12/รุ่นถัดไป, อย่าใช้ historical byte floor เป็น reserve, อย่าถือ worksheet/preflight เป็น Gate A approval.
- ไม่เปิด Cloudflare/queue/generation/API เพียงเพราะ Codec integration ผ่าน. งานไม่ attempted คง not_attempted; ไม่แต่ง fault/coverage/failure.

## ข้อความส่งเพื่อนหลังชุดใหม่เสร็จ (เติมผลจริงก่อนส่ง)

**ให้บีม:** ออมรวม Codec5 ตาม Beam a44f796f / Champ af1fe272 ต่อจาก shared v12 แล้วที่ commit <NEW> condition <ID> ผล <selected>/<excluded>/691. ฝากตรวจ scoped recipes/oracles รวม Codec13 เดิม, inputs/consumers ทั้ง4และ beam-pc1 host/lock/lease ผูก exact pins รุ่นนี้ ส่ง receipt paths/hashes. ยังไม่เปิด Gate A/KKU.

**ให้แชมป์:** ออมส่ง condition/preparation/prompts/protocol/runner/runtime และ worksheet40 คู่รุ่น <ID> ที่ commit <NEW>. ฝากตรวจ shared integration และ final reserve โดยใช้ actual provider model/settings/limits/token+framing/quota/reset/expiry ของ prompts รุ่นนี้ พร้อม evidence/timestamps. ตัวเลข 395/296 จาก v10 ไม่ใช่ชุด final รุ่นนี้. ขอคำตัดสิน pilot criteria/remaining host bindings ร่วมทีม; Gate A/live ยังปิด.

อย่าส่ง placeholders เป็นผลเสร็จจริง. ตอนเขียน handoff นี้ **ยังไม่มี Aom shared Codec implementation/preparation ใหม่**.
