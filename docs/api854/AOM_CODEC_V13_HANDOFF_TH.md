# ออมส่ง Codec5 shared development v13

Condition `api854-20261004-codec-v13-development` บน branch `aom` ต่อจาก immutable v12
`63ad195623c2ed3f67f3ae232c00c54d3160ce72`; รับ Beam `a44f796f`
และ Champ `af1fe272` ด้วย exact Git bytes/checksums ไม่ merge runtime ของ peer.
**20 bugs / 408 selected / 283 exclusions / denominator 691**.
เพิ่มเฉพาะ Codec5 ที่อยู่ใน v12 exclusions จริงและไม่ซ้ำ Codec13 เดิม.
รักษา Graphics/Chronology/setter/JDOM/Math/Buffer/Csv/Lang และ sources/exclusion reasons เดิม.
ตัวเลขนี้เป็น development capability declarations ไม่ใช่จำนวนบั๊กที่ทดสอบเสร็จหรือ full semantic approval.

## ชุดปัจจุบัน

ทุก path อยู่ใต้ `output/api854-20261004/`:

- `aom-codec-v13-intake-v1/`: immutable joint verdict, policy, peer code/trace, source archives/provenance.
- `aom-codec-v13-integration-v3/`: current accepted local shared Codec proof.
- `aom-codec-v13-environment-v1/`: actual pre-target default-state and target LinkageError controls.
- `aom-codec-v13-packaging-v1/`: two independent reference suites ขนาด 30 และ 13; cap30 คงเดิม.
- `aom-v13-preserved-runtime-v3/`: fresh retained components64, Chronology13, Graphics24/Chart48 pairs.
- `prepare-v13-codec-development-v2/`: preparation20 bugs, fixed context, shared recipe, targets, prompt, metadata/checksums.
- `aom-continuation-v13-development-v2/`: protocol.proposal.json, runner-plan.json, policy/checksums.
- `aom-v13-readiness-v2/`: Gate input audit, worksheet40, regression log, completion receipt/final checksums/worklist.

Final pins:

| Artifact | SHA-256 |
|---|---|
| Protocol | `42d64c198710facb9e1e06bff0e89bd87e8cb7820a8175ecaf59e6f35f85e1aa` |
| Preparation index | `89caabf5e652f498205cf18fe7a255dfc94c889f4506bd536fd47ba23cbda91d` |
| Runner | `462f6196d81dfc9ed1bda31ce0ae81e59759341b6671cb7fc9d9301fe453e048` |
| SqaProbe.java | `8e30b54abb2d24a20ed194593badc13b892308bc6eec2353f4f1e90b1fc6fb18` |
| Codec integration receipt | `0f6451ab71bd8fd8ee04c2d1cdcd610334ceeee0076dfe6e6505abaa6e43069a` |
| Codec environment receipt | `0dc0a55647d8305ae01ea621e95b33a5cacd86cf6c80c7e190626af871373d76` |
| Codec packaging receipt | `f43df2129fa680d20d21517722c981fef32c04377926f9201e2deb81fc1568b0` |
| Retained runtime receipt | `018c77e6ac6dc6c6f7e64ce0f23a315142644e90c6e0cea3bb90c57199cdda7f` |

Runtime41 files อ่านครบจาก completion-receipt.json; final-checksums.json ตรวจ exact bytes ของชุดส่งมอบ.
Historical committed output 12,693 files ตรง Git blob IDs เดิม.

## ขอบเขตและผลตรวจ

Exact private instance Metaphone.isNextChar/isPreviousChar/isVowel/regionMatch
และ package-static SoundexUtils.difference(StringEncoder,String,String) เท่านั้น.
Fresh real non-null StringBuffer/default Metaphone/maxCodeLen4; difference ใช้ real
Metaphone StringEncoder กับ sealed literal codes. Vector[0] แบ่ง43 declared buckets ภายใน[-1,1].
ก่อน target ตรวจ initial receiver/encoder state; oracle ตรวจ Boolean/integer/exact exception,
full unchanged buffer contents/length/capacity และ maxCodeLen. Illegal vowel indices เป็น
declared target exceptions; setup/projection/fatal target errors เป็น fixture failures.
ไม่รับ arbitrary encoder/maxCodeLen, null buffer/needle/encoder, non-ASCII/overflow indices,
EncoderException propagation หรือ full public-caller semantics. Legal null strings ของ difference
ไม่ใช่การรับ null encoder.

- Shared fixed43 casesสองรอบ/86 observations และ buggy43สองรอบ/86 observationsผ่าน; fault=false.
- Exact first JDI entries ครบ43 cases/5 signatures ต่อ revision; ไม่ใช่ line/branch coverage percentage.
- Independent peer checker ตรวจ scalar/exception/state จาก raw rows; เพิ่ม guard initial buffer capacity.
- Shared mutations isNextChar และ difference score จับ3/9cases; restore original class hashes.
- Codec13 old-v12/new-v13 เทียบ fixed/buggy ×13targets×3vectors =78 pairs ตรงกัน.
- Nested reference JUnit suites30+13 รัน fixed/buggyสองรอบ; ทุก stage executed=target_checks, skipped=0.
  Reference43 ไม่ได้ส่งเป็น primary suite43 และไม่มีการเพิ่ม suite cap.
- Actual default maxCodeLen5 control ถูกปฏิเสธก่อน invoke; LinkageError control ถูกปฏิเสธหลัง invoke.
  Cause chains/markers ตรวจจริงและ evaluator ไม่รวมเป็น discovered fault.
- Retained components64 + Chronology13 + Graphics24 ผ่านภายใต้ runtimeรุ่นนี้;
  Chart8 old/new48pairsตรงกัน, Graphics packaged JUnit/control proofsครบ.
  Time-1 arrays_bad_order negative reference คงเดิม; ไม่เปลี่ยนย้อนหลังเป็น algorithm result.
- Regression **95 tests / skip0**; all4 consumersครบ80 combinations,
  Gate input binding checks8ผ่าน. เปลี่ยนเฉพาะ offline v13 lineage/recipe handling ของ API worker;
  FrozenSettings live whitelistยังไม่รับ v13 แม้เปลี่ยนapproval flag.

Current proofs เป็น local development/reference ไม่ใช่ full Defects4J evaluation,
CMA-ES/FSCS-ART/AI primary results หรือ owner-host certification.

## ประวัติที่รักษาไว้

Integration-v1/preserved-runtime-v1 เป็นก่อนเพิ่ม default-state pre-target guard;
integration-v2/preserved-runtime-v2 เป็นก่อนเพิ่ม API offline v13 lineage handling.
Prepare/protocol/readiness-v1 เป็น superseded inputs: focused consumer check13testsพบ4errors
สำหรับ Chart/Math × Claude/Gemini (`beam_preparation_lineage_mismatch`). เก็บ artifactsเดิมไว้,
ไม่ใช้เป็นคำรับfinalและไม่แก้hashesย้อนหลัง. ใช้ current v3/v2 paths ข้างบนเท่านั้น.

## ส่งตรวจและ reserve

Worksheet40 =20bugs×2 requested models `claude-sonnet-5` / `gemini-3.5-flash-lite`;
temperature0/output4096. Promptใหญ่สุด **321,750 UTF-8 bytes** ไม่ใช่ tokens.
final_reserve=null. Champ `c1daf015` เป็น scoped v12 review/metadata history ไม่โอนเป็น v13 approval.
ล่าสุด Beam `92a3ee1b` รับ scoped v12 oracles/consumers/technical host แล้ว;
รับ original receipts/documents ที่ `aom-v13-peer-history-v1/` เป็น history เท่านั้น ไม่ต้องขอ v12 ซ้ำ.
Collections10 ที่ Beam ส่งใน commit นี้ยังรอ joint candidate review และไม่ได้รวมใน v13/408 targets.

บีมตรวจ Codec5 bounded semantic/oracles + Codec13 เดิม, consumersทั้ง4 และ beam-pc1 CPU1/lock/lease/dependencies
โดยผูก protocol/index/runner/runtime hashes รุ่นนี้ ส่ง explicit verdict/receipt paths/SHA-256.
แชมป์ตรวจ shared runtime/runner/worksheet40และ actual provider IDs/effective settings/limits/
input token+framing/current quota/bucket/reset/expiry พร้อมtimestampsของ conditionนี้.
อย่าใช้ byte guard/JSON bytes/historical reserve แทน measured provider reserve; ยังต้องแยก byte/token admission
หาก measured boundต่ำกว่าprompt bytesตามข้อค้นพบใน v12.

Aom owner-host receipt (CPU1สำหรับ Aom+Champ-owned jobs) และคำตัดสินทีมตาม
[pilot decision](AOM_PILOT_GATE_DECISION_TH.md) ยังpending. ข้อเสนอ bounded pilotของ Champยังเป็นproposal,
ไม่สร้าง/เปิด subset conditionจากคำรับ Codecโดยอัตโนมัติ. Primary691 denominator/exclusionsคงเดิม.
**Gate A=false / team approval=false / live requests0 / queue mutations0 / primary results0**.

## ตรวจซ้ำโดยไม่เขียนทับหลักฐาน

Python `-B -m scripts.study.api854.run_v13_regression` เป็น producerเขียน log fixed path;
อย่า rerunในชุด sealed. ใช้ unittest commandจาก readiness/regression-command.json ใน snapshot
และบันทึก outputใหม่. Java verifierทุกตัวต้อง `--output` ใหม่และ initialized local Defects4J.
Fresh reproductionต้องbind runtime/source pinsตาม receipt ก่อน ไม่ถือว่าเปลี่ยนflagsแล้วอนุมัติ live.
