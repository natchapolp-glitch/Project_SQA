# ออมตรวจ Csv condition ใหม่ครบสี่วิธีด้วย Defects4J แล้ว

รับ Champ `d98fcceee344edb9015854914c659722578d0ffe` ระหว่างทำรอบแรก.
คงแผน ready results first / immutable Aom63ad1956 v12 / CPU1 / พัก candidate.
ผลก่อนหน้าและ failed attempts ทุก condition เก็บครบ ไม่แก้ Java/archives/assertions.

## ผลล่าสุด

Generation condition: `api854-20261004-csv-messages-disabled-thinking-native-development-v2`.
Claude requested `/messages` + thinking disabled; Gemini `/chat/completions` ตามแผนแชมป์.
ออมไม่ได้ส่ง KKU request เพิ่ม. Fixed source/context/targets/recipes/runtime v12 เหมือนเดิม.

Evaluation condition แยกและประกาศก่อนรัน:
`api854-20261004-csv-messages-disabled-thinking-native-development-v2-d4j-benchmark-csv1-java11-los-angeles-v2`.
Java11 / Defects4J3.0.1 / America/Los_Angeles / fresh checkout ต่อ approach / aom-pc1 CPU1.

| วิธี | Tests | Fixed-1 / Fixed-2 | Buggy failures | Lines | Branches | Fault |
|---|---:|---|---:|---:|---:|---|
| CMA-ES | 30 | ผ่าน / ผ่าน | 0 | 31/37 (83.78%) | 13/26 (50%) | false |
| FSCS-ART | 30 | ผ่าน / ผ่าน | 0 | 31/37 (83.78%) | 13/26 (50%) | false |
| Claude Sonnet 5 | 21 | ผ่าน / ผ่าน | 1 | 36/37 (97.30%) | 22/26 (84.62%) | true |
| Gemini 3.5 Flash Lite | 18 | ผ่าน / ผ่าน | 1 | 37/37 (100%) | 23/26 (88.46%) | true |

Coverage stage ผ่านทั้งสี่; coverage คือ ExtendedBufferedReader เพียงคลาสเดียว.
Algorithms มี counters executed30/skipped0/target_checks30 ครบทุก stage.
AI มี actual Defects4J test-start events21/18ทุก stage แต่ไม่มี aggregate skip/target counters;
ตาราง JSON/CSV จึงเก็บสองค่านี้ null ไม่ยืม native skip0 หรืออ้าง exact declaration invocationครบ.

Sonnet fail `lineNumberDoesNotDoubleCountCRLF` expected1/actual0;
Gemini fail `testCarriageReturnLineNumber` expected2/actual1.
สอง failure นี้เกิดบน actual Defects4J Csv-1b และเกี่ยวกับ patch read() นับ CR/ไม่ double-count CRLF.
Fixed ผ่านซ้ำและไม่มี harness/infrastructure failures ที่ evaluator รับเป็นfault.
Input-domain equivalence ยังไม่รับรอง: AI ใช้ legal Reader inputs กว้างกว่า bounded algorithm streams.
จึงไม่ใช้ผลหนึ่ง bug นี้สรุปว่า AI เหนือกว่า algorithms หรือ semanticครบ403/691/854.

รอบใหม่รวม checkout/evaluation **144.21s**. เวลาแต่ละ evaluator อยู่ใน record.json.
รอบแรก **77.10s** แยกไว้; ไม่รวม API/generation และไม่ทำนายเวลาทั้ง854จากCsv.

## ความต่างของ buggy source ที่พบและเก็บไว้

Wrapper แชมป์กำหนดว่าทุก native production source ต้องตรง checkout Defects4J.
รอบ strict ของออมหยุดก่อน test เพราะ native buggy ใช้ upstream commit `0833f45b`,
แต่ Csv-1b ของ Defects4J เป็น reconstructed benchmark `fd396e4f`.
Fixed ทุก source ตรง; buggy ต่างเพียง `ExtendedBufferedReader.java`.

- Native buggy SHA-256: `d725202c7a4989c6ec37289578d7f8497d1f0a8c623c64fe3e1aabb9f00fa4a4`
- Actual Defects4J buggy SHA-256: `4ddf7df1c7c8b9e3d45dc6a3410f802a2e5b641f37089655c3eb2f52cdfd5185`

Diff มี buffer-read counting ที่ Defects4J reconstructed version รับจาก fixed ไว้แล้ว.
ดังนั้น native pair ไม่ใช่ byte-identical Defects4J pair แม้ single-character CR fault ยัง reproduceได้.
เก็บ strict attempt v1, actual/native Java bytes และ diff; ไม่แก้ fixture/test/production sourceให้ตรง.
รอบ v2 bind benchmark buggy SHA ล่วงหน้าอย่างเปิดเผย ตรวจ source ที่เหลือกับ native เดิมทั้งหมด,
และตรวจ `.defects4j.config` ของ Csv-1f/b ก่อนใช้ evaluator v12.
**อย่าลบ guard ใน wrapper เดิมหรือเปลี่ยนชื่อ hash ให้ผ่าน**; บีมใช้ condition/receiptนี้รับตรวจ
หรือประกาศ exact benchmark binding ใหม่ก่อนรันของตัวเอง.

## ยอดจริงและหลักฐาน

ตาราง native: **2 unique bugs / 12 condition×bug×approach outcomes**.
ตาราง Defects4J ของออม: **1 unique bug / 8 condition×bug×approach outcomes**
(Csv สอง conditions, 6 complete suite rows + 2 invalid outcomes).
Csv conditionล่าสุดครบ4 valid measured suites; Csvยังนับเป็น bugเดียว.
Baseline Sonnet truncated/Gemini fixedfail ยังคงอยู่ ไม่กลายเป็นpassedจากผล conditionใหม่.

หลักฐานทั้งหมดใต้ `output/api854-20261004/`:

- [รับ Champ d98 และ verify384 peer manifest entries](../../output/api854-20261004/aom-ready-messages-intake-v1/receipt.json)
- [strict source attempt v1 ที่ไม่ผ่าน](../../output/api854-20261004/aom-ready-messages-d4j-v1/failed-attempt.json)
- [diff/native/benchmark bytes](../../output/api854-20261004/aom-ready-messages-d4j-v1/native-vs-benchmark.diff)
- [v2 plan — pins/benchmark override/host/condition ก่อนรัน](../../output/api854-20261004/aom-ready-messages-d4j-v2/preexecution-plan.json)
- [v2 measurements/receipt](../../output/api854-20261004/aom-ready-messages-d4j-v2/receipt.json)
- [Defects4J CSV แยก condition](../../output/api854-20261004/aom-ready-results-report-v2/full-d4j-results.csv)
- [JSON/ทุก row ผูก record SHA-256](../../output/api854-20261004/aom-ready-results-report-v2/full-d4j-results.json)
- [Native CSV เก็บ baseline + newCsv + Cli แยก condition](../../output/api854-20261004/aom-ready-results-report-v2/native-results.csv)
- [Cli quarantine และ receipt hashes](../../output/api854-20261004/aom-ready-results-report-v2/cli-quarantine.json)
- [cohort20 ×4 worklistล่าสุด](../../output/api854-20261004/aom-ready-results-report-v2/cohort20-worklist.csv)
- [ตรวจ publication: 871 sealed entries, 17,277 historical Git blobs, 23 tests และ row bindings](../../output/api854-20261004/aom-ready-results-publication-v3/receipt.json)

Cli-1 มี native outcomesครบ4รับเข้าแล้ว แต่ **full Defects4J ยังไม่รัน**.
Cli FSCS raw flag=true จากลำดับ options เป็น false positive ตาม receipt แชมป์;
เก็บrawflagไว้และกักออกจาก scientific fault counts ไม่แต่งเป็น production fault.
Worklistมี Csv4 complete ใน conditionใหม่, Cli4 native received/D4J pending-oracle,
และอีก72 pending_not_received_by_aom. สถานะออมไม่ใช่ยอดงานสดทั้งหมดของเพื่อน.

## งานต่อที่ส่งทีม

1. บีมรับตรวจ Csv replay/benchmark source mapping, byte hashes, counters และ exact Aom host.
   Wrapper strict เดิมจะติด buggy source mismatchเดียวกัน; ไม่ rerunโดยไม่ประกาศ condition.
2. ออมแก้ oracle ของ Cli **prospectively**: optionsเป็นunordered mapping/value multisetตามcontract,
   แต่ positional arguments/buffers/orderedarrays ต้องเก็บลำดับ. ตรวจ regression exact16 targets
   และ state/value-sensitive controls ก่อนออก runtime/prompt/worksheet conditionใหม่.
   ไม่แก้ raw Java/suites/protocol v12/v13 ที่ sealแล้ว. ตอนนี้ยังไม่ได้ implement oracleใหม่.
3. แชมป์รับตาราง Defects4J แล้วส่ง ready batch ถัดไปพร้อม condition/source/suite/provider receipts;
   ก่อนขยาย Messages contractเข้าทั้ง20ให้ bind exact domains/protocol/worksheetใหม่ร่วมออม.
   ไม่ transfer reserve/approvalของ Chat conditionเดิม.
4. คงbuffer4ชั่วโมงสุดท้ายสำหรับreport/slides/demo/ZIPตาม [แผนเวลา](AOM_READY_RESULTS_FIRST_TH.md).
   จำนวน854ยังเป็นbacklog; primary results=0, GateAfalse, final reserve=null.

Current user-visible plan/next agent/team messages ผูกเอกสารนี้; [รอบแรก](AOM_READY_RESULTS_FIRST_TH.md)
เป็นหลักฐาน baseline ที่คงไว้. Audit ที่หยุดเพราะ WSL Git filesystem scan เก็บ v1 แล้ว;
v2สำเร็จด้วยการเปรียบเทียบGit blobตรงและตรวจ23testsโดยไม่rerun experiment;
final review v3ตรวจเพิ่มเติมด้วย native Windows filesystem/Git และยืนยันตาราง conditionใหม่.
