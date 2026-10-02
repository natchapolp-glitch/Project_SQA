# Beam: ใช้ 1 เครื่องและตรวจ shared prepare v3 — 2026-10-03

บีมยืนยันว่ามี **1 เครื่อง** จึงสร้าง runner proposal รุ่นใหม่ใช้ `beam-pc1` และ CPU 1 slot แทน pool `beam-pc1/2/3` รวม runtime ออม `d147e216` กับ Beam explicit fixture changes โดยรักษา shared-v3 input/receiver/owner/runner guards เดิมไว้ ยังไม่ frozen primary หรือเปิด live pilot

## แผน 1 เครื่องที่ให้ออมรับไปใช้

- [runner-plan.beam-one-host.v2.json](../../experiments/configs/api854-20261003/runner-plan.beam-one-host.v2.json) พร้อม `.sha256`: ถอน `beam-pc2/3`, คง `beam-pc1`, max_cpu_slots=1; assignment ของแชมป์/ออมและ job ownership เดิมคงไว้
- [planning inputs v2](../../experiments/configs/api854-20261003/confirmed-plan-constraints.beam-one-host.v2.json): reported Beam hosts=1 และ verified Beam CPU slot=1; global CPU target ปรับจาก 4 เป็น 3 ตาม reported CPU hosts ของบีม 1 + ออม 2 ไม่ใช่หลักฐาน provisioning ของเครื่องออม
- [protocol.beam-one-host.v2.proposal.json](../../experiments/configs/api854-20261003/protocol.beam-one-host.v2.proposal.json) พร้อม `.sha256`: ผูก plan/host/discovery/semantic evidence และ current implementation hashes; enabled_stages=[] และ approval ยัง pending

เก็บ runner-plan.v1, constraints เดิม, shared prepare v3, primary draft ออมและ frozen core เดิมไว้เป็นหลักฐาน ไม่ใช้ draft ใหม่แทน protocol ของ core-preflight queue เดิม ต้องให้ออมรวม proposal นี้ใน primary version ที่ทีมรับรองภายหลัง ก่อนรันให้ระบุ `--runner-plan` และ `--protocol` เป็นคู่ที่ตรวจรับและ hash-bound ไม่ใช้ชื่อ beam-pc2/3 จากแผนเก่า

หนึ่งเครื่องยังครอบคลุม routing ทั้ง 854 bugs × 4 approaches × 3 stages = **10,248 stage keys** ไม่มี gaps จำนวนงาน/เจ้าของงานไม่ลดลง แต่ CPU ฝั่งบีมรันตามลำดับและต้องวัดเวลาจริงก่อนรับรองกำหนดส่ง

## หลักฐาน host และ runner

[Host receipt](evidence/beam-one-host-readiness-20261003/host-receipt.json) และ [checksums](evidence/beam-one-host-readiness-20261003/checksums.json): ตรวจ environment จริงของเครื่องนี้ผ่าน Linux/WSL, Java/Javac 11, Defects4J 3.0.1 และ prerequisites; ไม่คัดลอก receipt เครื่องเดียวไปอ้างว่าอีกสองเครื่องพร้อม

ทดสอบ `cpu_slot` ด้วยสอง process จริงบน isolated lock root: process ที่สองถูกปฏิเสธขณะ slot ถูกใช้ และเข้าได้หลัง release; development batch ใช้ shared root `/home/beam/sqa-beam/worktrees` และรัน generation/evaluation ตามลำดับจริง ทุก CPU worker บนเครื่องนี้ต้องใช้ root เดียวกัน จึงไม่ควรสตาร์ตหลาย worker ด้วยคนละ worktrees root

Integration test ใช้ runner ใหม่กับ isolated local queue ผ่าน prepare → FSCS generation → evaluation บน worker_id เดิมและ owner=beam ตลอดเส้นทาง ส่วน stage bodies/provider เป็น mock สำหรับการทดสอบ routing การรัน Defects4J จริงของชุด fixture แยกไว้ข้างล่าง ไม่อ้างว่าทดสอบ live queue หรือ KKU แล้ว

## ตรวจ shared-v3 artifacts ครบ 20 bugs

[Beam v3 review receipt](evidence/beam-shared-v3-one-host-review-2-20261003.json) ตรวจ hashes ของ preparation files **243 ไฟล์**, identities/owners, exact shared declarations, common compiled fixture inventory, fixed-only exclusions, production/build bytes, fixed revision proofs และ Chart receiver partition เทียบ Beam retained fixed/buggy discovery กับ eligibility export ของตน

ผลตรงครบ **20 bugs / 691 declarations / 3 exclusions**; `fixed_source_sha256` ยัง modified-only และ Chart `AreaRenderer.java` SHA `fb540d6b7c8faf9f9b83e26d51d9756ed2978b02e29f8f8100ce84661cd227c6` อยู่ใน additional receiver mapping แยกจาก evaluator coverage targets ไม่รับรอง meaningful construction จากรายชื่อ compiled classes เพียงอย่างเดียว

## Fixture review ที่ทำเพิ่มในเครื่องเดียว

เพิ่ม development policy **`beam-explicit-fixtures-v4-proposal`** ต่อจาก v3 โดยเก็บเงื่อนไข/หลักฐานเดิม:

- Codec: production Caverphone/Metaphone encoders และ SoundexUtils ใช้ scalar/String/Object boundary inputs; assertions ตรวจ encoded/scalar return มี EncoderException ที่คาดหมายจาก non-String `encode(Object)` ซึ่ง fixed source ระบุไว้ ไม่ใช่ constructor/setup failure
- Collections: `Flat3Map` เริ่มจากสอง key/value ที่ทราบ ตรวจ return และ sorted concrete map contents หลัง mutation ไม่ใช้ object identity/void-only oracle
- Csv: `ExtendedBufferedReader` รับ non-null in-memory StringReader ตรวจผล read/lookAhead/readLine กับ line/cached-character state; buffer offset/length overload ยังไม่เลือกเพราะยังไม่มี bounds recipe ที่ตรวจรับ

Capability selection เป็น declaration/recipe restriction ก่อนดู buggy outcome และเก็บ exclusions ไว้ ไม่มี assertion repair, fixed-failure pruning หรือส่ง execution feedback ให้ AI

| Bug / approach | fixed รอบ 1 / 2 | buggy failures | fixed lines | fixed branches |
| --- | --- | --- | --- | --- |
| Codec-1 / FSCS-ART | ผ่าน / ผ่าน | 0 | 154/256 | 64/184 |
| Codec-1 / CMA-ES | ผ่าน / ผ่าน | 0 | 149/256 | 57/184 |
| Collections-1 / FSCS-ART | ผ่าน / ผ่าน | 0 | 117/495 | 68/376 |
| Collections-1 / CMA-ES | ผ่าน / ผ่าน | 0 | 103/495 | 61/376 |
| Csv-1 / FSCS-ART | ผ่าน / ผ่าน | 0 | 20/37 | 4/26 |
| Csv-1 / CMA-ES | ผ่าน / ผ่าน | 0 | 20/37 | 4/26 |

ทั้งหมด 6 suites × 30 retained tests = **180 cases** และทั้ง fixed-1/fixed-2/buggy/coverage มี executed=30, skipped=0, target_checks=30 ต่อ stage รวม 24 stage counter files; ตรวจ target method descriptor กับ coverage XML จริง ทุก buggy ผ่านจึง fault_detected=false ไม่ตีความว่าใช้ทดสอบแล้วต้องพบบัคเสมอ

[Evidence index](evidence/beam-scalar-fixture-review-20261003/index.json), [checksums](evidence/beam-scalar-fixture-review-20261003/checksums.json): 242 hash-bound files พร้อม suite archives, untouched generation/evaluation results, observations, selected/excluded targets, logs/counters/coverage, semantic case tables, separate review results และ implementation snapshot

Local semantic verdict=valid สำหรับ retained suites ทั้งหก แยกจาก original evaluator results ที่ยัง usable=false; reviewer=`Codex local development review`, team_or_primary_approval=false ยังต้องตรวจรับร่วมกัน ไม่รับรอง unsampled declarations, AI suites หรือ Gate A จาก review นี้

รวมกับ [Closure-176/JxPath-1 evidence รุ่นก่อน](BEAM_FIXTURE_REVIEW_TH.md) ตอนนี้มี local development review ของ suites ใน **5 จาก 20 bugs** แต่ไม่ใช่ blanket approval ของทุก target ภายในห้าบัค หรือ approval ของ shared-v3 pipeline ทั้งชุด

## จุดเชื่อมที่ยังต้อง version ใหม่ก่อน live

Shared prepare v3 ของออมยังใช้ `common-fixed-buggy-production-types-v1` และ prompt `shared-fixed-targets-junit4-v3`; explicit v4 เป็น development recipe policy แยกกัน ต้องตกลง shared recipe/capability/prompt version ใหม่ที่ให้ความรู้และ targets ชุดเดียวกันทั้งสี่วิธี แล้วสร้าง prepare ใหม่และวัด prompt/reserve ใหม่ ไม่เติม explicit fields ลง v3 เดิมให้ดูเหมือนรองรับ

เพิ่ม guard ปฏิเสธ explicit recipe protocol ที่ยังอ้าง `aom-beam-prepare-v3` ก่อน queue claim; direct prepare ก็ปฏิเสธการ relabel ส่วน local explicit prepare ใช้ recipe/prompt exporter ที่มี hash binding จริง รักษา canonical targets.json และ all-owner runner guards ระหว่าง merge

CPU proofs bind กับ retained development protocol/snapshot; preclaim guard ที่เพิ่มภายหลังตรวจด้วย regression tests แยกกัน current draft pin source hashes ใหม่ ไม่อ้างว่า proof เก่าถูก rerun ด้วย primary composition ปัจจุบัน

Validation หลังรวม: API854 รัน 233 tests ผ่าน 232/skip 1 (source symlink fixture), legacy/Java ผ่าน 31 รวม **263 ผ่าน/1 skip/0 failures/errors**; รวม single-host routing, retired-host rejection, exact real v3 inventory review, fixture capability boundaries, hashed recipe checks และ explicit/shared-v3 preclaim rejection

## งานบีมที่เหลือ

Meaningful construction/oracles และ prospective execution/review ยังเหลือ 15 bugs: Chart-1, Cli-1, Closure-1, Compress-1, Gson-1, JacksonCore-1, JacksonDatabind-1, JacksonDatabind-112, JacksonXml-1, Jsoup-1, JxPath-22, Lang-1, Math-1, Mockito-1, Time-1 รวมการตรวจ unsampled targets และ shared capability exclusions ที่ทีมจะใช้จริง

ออมรับ one-host plan/host receipt และ exact v3 discovery review ไปประกอบได้ ส่วน explicit recipe policy และ local semantic reviews ต้องร่วมตรวจรับก่อนเปลี่ยน shared prepare/protocol แล้วจึงตรวจ runner/Gate A กับทีม รอบนี้ **0 real KKU requests และ 0 live queue mutations** ยังไม่ต้องส่ง API key ให้บีม
