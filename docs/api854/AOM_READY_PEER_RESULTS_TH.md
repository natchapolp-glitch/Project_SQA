# ออมรับผล Jsoup และรวมรายงานล่าสุด

รับ Champ `a4c723ad6cd58b3e837af28036ebefcfdba025d1` ตามข้อความผู้ใช้.
ตรวจ Git พบ Beam `2c0e92fcca561ddd82f7881e8f6a0536ccdfef92` รัน Csv4/Jsoup2 แล้ว
และ Champ `e9c6371892b3521d363b9c9b548ed348a1c15b5a` ตรวจรับ พร้อมเพิ่ม Compress/Gson.
จึงรวมผลล่าสุดใน **report v6** โดยรับหลักฐาน peer และไม่รัน Defects4J ซ้ำบนออม.
การรับตรวจรอบนี้ไม่มี KKU request, CPU evaluation, queue mutation หรือการ merge runtime ของเพื่อน.

## ตารางสำหรับรายงาน

| Bug / condition ล่าสุด | CMA-ES | FSCS-ART | KKU Sonnet 5 | KKU Gemini 3.5 Flash Lite |
|---|---|---|---|---|
| Csv-1 Messages | 30 tests, full D4J ผ่าน | 30 tests, full D4J ผ่าน | 21 tests, full D4J ผ่าน; พบ CR fault | 18 tests, full D4J ผ่าน; พบ CR fault |
| Cli-1 oracle ใหม่ของออม | 30 tests, full D4J ผ่าน | 30 tests, full D4J ผ่าน | ยังไม่ได้เก็บใน condition ใหม่ | ยังไม่ได้เก็บใน condition ใหม่ |
| Jsoup-1 | 30 tests, full D4J ผ่าน | 30 tests, full D4J ผ่าน | native fixed fail1/27 ทั้งสองรอบ; reject ทั้ง suite | native fixed fail2/9 ทั้งสองรอบ; reject ทั้ง suite |
| Compress-1 | native30 ผ่าน fixed2/coverage; buggy fail1; รอ Beam D4J | native30 ผ่าน fixed2/coverage; buggy fail5; รอ Beam D4J | truncated4096 ไม่มี executable suite | 8 tests, compile failed |
| Gson-1 | native30 fixed fail11 ทั้งสองรอบ; Type identity oracle | native30 fixed fail8 ทั้งสองรอบ; Type identity oracle | truncated4096 ไม่มี executable suite | 6 tests, compile failed |

Algorithms ของ Csv/Cli/Jsoup ที่ผ่าน full D4J ยังไม่พบ fault ในชุดที่วัด.
Jsoup AI ไม่ผ่าน fixed จึงไม่รัน buggy/coverage บน Beam และไม่ถือว่าเป็นการพบ bug.
ค่า fault/coverage ที่ไม่วัดคง null; ไม่ตัด tests, ซ่อม assertion หรือส่ง failure feedback ให้ AI.
Compress fault เป็นผล native ที่รอ benchmark replay ยืนยัน ไม่รวมเป็น full D4J fault.
Csv AI inputs กว้างกว่า bounded streams ของ algorithms; ผลไม่ใช่การรับรอง input-domain equivalence.
Coverage เป็นระดับ target class ไม่ใช่ทั้ง project หรือ 854 bugs.

### ยอดรวมที่มีความหมายต่างกัน

- ข้อความ Champ a4c723ad เดิมมี **3 unique bugs / 16 native condition outcomes**;
  เก็บ 16 rows เดิมครบในรายงานใหม่.
- Champ e9c63718 ล่าสุดมี **5 unique bugs / 24 native condition outcomes** รวม invalids.
  Csv สอง conditions ยังเป็น bug เดิม; Cli scoped oracle ใหม่ของออมเป็นอีก condition ซึ่งแยกจาก native24.
- ตาราง execution ของออมรวม 18 outcome/attempt rows: **14 completed host evaluations**,
  2 prior invalid outcomes และ 2 Cli environment-failed attempts เดิม.
  หลังไม่นับ Csv ที่อีก host replayซ้ำ เหลือ **10 unique generation-condition × method pairs**
  ที่ complete ครอบคลุม **3 bugs: Csv, Cli, Jsoup**. ไม่ตีความว่าเป็น independent statistical repeats.
- มี **Csv-1 เพียง bug เดียว** ที่มี valid full D4J results ครบ 4 วิธีใน condition ล่าสุด.
  Jsoup เก็บครบ 4 outcomes แต่ valid full D4J เพียงสอง algorithms; Cli ใหม่ยังรอ AI สองชุด.

## ผล Defects4J ที่รับจากบีม

| วิธี | Tests | Buggy failures | Target class lines | Target class branches |
|---|---:|---:|---:|---:|
| Csv CMA-ES | 30 | 0 | 31/37 | 13/26 |
| Csv FSCS-ART | 30 | 0 | 31/37 | 13/26 |
| Csv Sonnet | 21 | 1 | 36/37 | 22/26 |
| Csv Gemini | 18 | 1 | 37/37 | 23/26 |
| Jsoup CMA-ES | 30 | 0 | 36/46 | 10/18 |
| Jsoup FSCS-ART | 30 | 0 | 36/46 | 10/18 |

ออมตรวจ raw **JUnit XML24 reports** เทียบ testcase identities, Formatter starts,
suite counters และ evaluator failures. Fixedสองรอบ/coverageผ่าน, skipped0/errors0ทุก stage.
Algorithms target_checks30; AI aggregate target_checks ไม่มี จึงคง null แม้ XML ยืนยัน executed21/18.
ตรวจ suite/archive Java hashes ตรงชุดแชมป์, class coverageตรง raw summary.csv,
fixed context6ไฟล์ต่อbug และ frozen v12 runtime41 pins ตรงกัน.
Official isolated-bug patch/hash/metadata และ framework restoration ตรงหลักฐานที่เก็บ.
Native parent กับ actual D4J buggy sources ต่างกัน จึงรักษา execution conditions แยกตาม receipts.
การตรวจรับหลักฐานจาก peer ไม่ใช่การ rerun บนเครื่องออม.

Focused reporting tests ผ่าน **14 tests**. รับ peer Git blobs แบบ exact bytes **1,364 files**
จากสาม pinned commits; raw responses, invalid Java/archives และ failed coverage attempts อยู่ครบ.
Report v4/v5 เป็นการอ่าน schema ของรายงานผิดและ CSV column mismatch ในฝั่งออม;
เก็บ producer/partial files และ rejection receipts ก่อน publication. ไม่ใช่ peer test failures
และไม่เพิ่มยอด scientific outcomes. **ใช้ report v6** เท่านั้น.

## ไฟล์ส่งมอบ

ใต้ `output/api854-20261004/`:

- `aom-ready-peer-intake-v1/`: exact Champ a4/Beam2c/Champ e9 evidence และ receipts/checksums.
- `aom-ready-peer-review-v1/`: input/source acceptance, raw XML/reporting guards และ test logs.
- `aom-ready-results-report-v6/native-results.{json,csv}`: native24 outcomes พร้อม original receipt/hash.
- `aom-ready-results-report-v6/full-d4j-results.{json,csv}`: Aom/Beam execution rows แยก host/condition.
- `aom-ready-results-report-v6/latest-four-methods.{json,csv}`: ตารางล่าสุด20 method slots ของ5bugs,
  รวม pending/invalid และบอกว่าเป็น native หรือ full D4J.
- `aom-ready-results-report-v6/cohort20-worklist.csv`:80 slots; pending คือยังไม่รับเข้ารายงานออม.
- `aom-ready-results-report-v6/condition-catalog.json`: v12 และ v13 แยกชัดเจน.
- `aom-ready-results-report-v6/peer-acceptance.json` และ `receipt.json`: hashes/counts/summary.

v12 baseline `63ad195623c2ed3f67f3ae232c00c54d3160ce72`; v13 Codec composition ยังเป็น
parked candidate history ตาม [handoff v13](AOM_CODEC_V13_HANDOFF_TH.md).
ไม่ได้ใช้ manual reference suites ของ v13 เป็นผล CMA/FSCS/AI และไม่ได้รวม denominator ต่างรุ่น.
Cli oracle ของออมดู [ผลและ preparation ใหม่](AOM_CLI_UNORDERED_RESULTS_TH.md), commit `7f0c6d31`.
Old Cli FSCS order mismatch ยัง quarantine, raw flagเดิมไม่หายและไม่นับเป็น confirmed fault.

## งานถัดไปของทีม

1. แชมป์รับ Cli preparation v2/worksheet สองคู่จาก aom7f0c6d31 แล้วเก็บ AI ใหม่ผ่าน KKU
   ตาม condition/provider settings ที่ผูก receipt จริง; old AI outcomes ไม่โอนมารุ่นใหม่.
2. บีมไม่ต้อง rerun Csv/Jsoup ที่ส่งและตรวจรับแล้ว; ทำ Compress สอง valid archives จาก Champ e9
   บน CPU1/Java11 พร้อม source guards และส่ง counts/coverage/timing/hashes ตามคำสั่งในเอกสารแชมป์.
3. ออมรับ AI Cli/ผล Compress เมื่อพร้อม, อัปเดตรายงาน และทำ prospective Gson structural Type oracle
   เป็นงานถัดไป: raw type/actual arguments/owner/type variables/cycle guards; type arguments ยัง ordered.
   ต้องมี controls/new preparation/new condition ก่อน generation ห้ามแก้ tests เดิมให้ผ่าน.
4. เตรียมรายงาน สไลด์ และ demo จากผลที่วัดจริงพร้อมข้อจำกัดระหว่างรับ ready batches.

ยังไม่อ้างครบ854, ไม่รวมเป็น primary results, GateA=false และ final reserve=null.
Quota ที่อยู่ใน peer receipts เป็นค่าที่สังเกต ณ เวลานั้น ไม่ใช่ current quota หรือหลักฐาน reset/expiry.
