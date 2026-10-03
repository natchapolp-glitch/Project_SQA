# Cli-1: oracle ใหม่และผล Defects4J ของออม

ใช้แผน Champ `d98fccee` เก็บผลจาก bugs ที่พร้อม พัก candidate ใหม่ และรักษา immutable v12
ที่ `63ad195623c2ed3f67f3ae232c00c54d3160ce72` เป็นฐาน. รอบนี้แก้เฉพาะ oracle ของ Cli-1
ใน runtime แยก; root helper, v12/v13 และผลเดิมไม่เปลี่ยน. ไม่เรียก KKU ไม่แก้คิว.

## ขอบเขต oracle

condition สำหรับ generation คือ `api854-20261004-cli-unordered-options-v1-development`.
มี CommandLine 16 declarations เดิมและ fixed-source/build context เดิม.
`getOptions()`, `iterator()` และ options ใน receiver state เปรียบเทียบเป็น multiset
ของ identity/value projection จึงยอมรับการสลับลำดับ options และรักษาจำนวนซ้ำ.
ค่าในแต่ละ Option, positional arguments และ arrays/lists อื่นยังรักษาลำดับ.
Option projection เดิมตรวจ short opt และ values; ไม่อ้างว่าตรวจ metadata ทุกชนิดของ Option.
ไม่ขยาย input domain และไม่ใช้ failure vector, buggy source หรือ execution logs ใน prompt ใหม่.

FSCS เดิมที่รายงาน mismatch เพราะ options สลับลำดับยังคง raw suite/assertion/failure และ quarantine เดิม.
เวกเตอร์ดังกล่าวใช้เป็น diagnostic regression เท่านั้น: oracle ใหม่ให้ fixed/buggy เท่ากัน;
ไม่ใช่ผลทดสอบวิธีที่ห้าและไม่นับเป็น scientific fault.

## ผลจริงของ condition ใหม่

| วิธี | Tests | fixed สองรอบ | buggy | Coverage ของ CommandLine บน fixed | Fault |
|---|---:|---|---|---|---|
| CMA-ES | 30 | ผ่าน / ผ่าน | ผ่าน | 43/45 lines, 14/16 branches | ไม่พบ |
| FSCS-ART | 30 | ผ่าน / ผ่าน | ผ่าน | 42/45 lines, 12/16 branches | ไม่พบ |
| KKU Sonnet 5 | — | ยังไม่รัน | — | — | ยังไม่มีผลใหม่ |
| KKU Gemini 3.5 Flash Lite | — | ยังไม่รัน | — | — | ยังไม่มีผลใหม่ |

Algorithms seed101/budget30 สร้าง fresh suites จาก fixed เท่านั้น.
ทุก stage ของสอง algorithms มี executed=30 / skipped=0 / target_checks=30
ตรงกับ Defects4J `all_tests` และ unchanged suite counters. Coverage denominator 45/16
เป็น class ที่ instrument; ไม่ใช่ 16 declarations หรือ 854 bugs.
AI ของ condition เก่าไม่โอนมาเป็นผล condition ใหม่. Cli ยังไม่ครบผลจริงทั้งสี่วิธี.

Java oracle controls ผ่าน 15 ข้อ ครอบคลุม permutation, key/value changes, ordered values,
duplicate multiplicity, ordered args/arrays และ fixture rejection.
Reference 32 cases ครอบคลุม 16 declarations ด้วยสอง vectors; fixed observations 64 ครั้งเสถียร
และ old-policy projection ใน helper ใหม่ตรงกับ helper v12 ทุก 32 cases.
Reference cases เป็น development evidence แยกจาก generated suites 30 tests.

เครื่องออม: WSL Ubuntu / Java11 / Defects4J3.0.1 / TZ America/Los_Angeles / CPU1
ใช้ lock `/home/team/sqa-round2/worktrees`. Actual Cli-1b ต่างจาก native pair ของ Champ
ที่ CommandLine.java และ Option.java; เก็บ hashes ทั้งสองคู่ไว้และประกาศ D4J เป็นคนละ condition.
ไม่มีการแก้ production source ให้ตรงกับ native หรือแก้ assertions ให้ผ่าน.

## รอบที่ไม่ผ่านยังเก็บครบ

- Preparation v1 ถูก reject ก่อน generation/testing เพราะ helper ใหม่ยังไม่อยู่ใน embedded recipe/prompt.
  ใช้ preparation **v2** เท่านั้น; receipt ที่ `aom-cli-preparation-review-v1` อธิบายเหตุผล.
- D4J v1 ทั้งสอง algorithms ล้มเหลวก่อนเริ่ม test methods เพราะ Cli framework ใช้ JUnit jar
  ที่ขาด `org.hamcrest.SelfDescribing`. เป็น environment failure, fault=null ไม่ใช่ target failure.
- D4J v2 replay **archives เดิม byte-for-byte** โดยเปลี่ยนเฉพาะ framework JUnit URL
  เป็น bundled JUnit+Hamcrest เดียวกันสำหรับ fixed/buggy แล้ว restore ค่าเดิมใน finally ภายใต้ CPU lock.
  before/restored SHA-256 `a04de90cfb72816d929923b831b588e83d3dccfc959c373b61cd4c71f94c1a66`.
  Production, generated Java และ suite assertions ไม่เปลี่ยน.

## Paths และ pins สำหรับตรวจ/ทำต่อ

ทุก path ด้านล่างอยู่ใต้ `output/api854-20261004/` และมี `checksums.json`:

- `aom-cli-unordered-preparation-v2/`: frozen runtime 41 files, prepared/Cli-1,
  protocol.proposal.json, runner.proposal.json, oracle-contract.json, worksheet สองโมเดล.
- `aom-cli-unordered-d4j-v1/`: generation/archives ใหม่, controls, reference/diagnostic,
  production-source-binding และ environment failure เดิม.
- `aom-cli-unordered-d4j-v2/`: authoritative completed Defects4J replay สอง algorithms;
  evaluation condition `api854-20261004-cli-unordered-options-v1-development-d4j-java11-la-v2-hamcrest`.
- `aom-ready-results-report-v3/`: Csv เดิมสอง conditions คงครบ + Cli สอง attempts/two completed replay;
  มี latest four-method table ซึ่ง AI ยัง pending, worklist80 และ native table/quarantine เดิม.
  รวม 12 D4J condition-outcome rows: 8 complete / 2 prior invalid / 2 Cli environment failed,
  **2 unique bugs**. จำนวน rows ไม่ใช่จำนวน bugs หรือผลครบ 854.

Prompt SHA-256 `7dbe11213e2c3608ee5f9ade657691438e52c1541575c293ee98cc1dbc587439`;
ขนาด **170,764 UTF-8 bytes** ไม่ใช่จำนวน tokens.
Helper SHA-256 `d40b49631d9d5318027a1249c7cfeaa16fc208c203c53bfd19fd3bf8cd7372db`.
Protocol SHA-256 `966396b49376a6495174503b248c13c8076aea534cbbdcd9fcece0acf80c1494`.
CMA suite SHA-256 `43d1605e1742ea7f25a274025107fb0599abe9e5d065be58ad0d28808afc2080`;
FSCS suite SHA-256 `b10d618e72aa7d28cc81beaa96890ef1bee1febfb5bea3cb3b636b0624ea851d`.
Generation และ AI ใช้ Cli16/fixed context/recipe ใหม่ชุดเดียวกัน; AI ยังไม่ได้รับ prompt ผ่าน KKU ในรอบออม.
Inherited policy descriptions ของ project อื่นเป็น lineage ของ v12; ขอบเขตที่ใช้จริงคือ
Cli-only contract/oracle-contract/fixture_selection และ whitelist class guard ของ condition นี้.

## ขั้นถัดไป

บีมรับตรวจ oracle controls, reference 16 signatures, actual benchmark sources, helper/source/prompt
pins และ host/counters แล้วส่ง scoped verdict. แชมป์ตรวจ preparation v2/worksheet สองคู่,
วัด tokens/framing/current settings/limits/quota แล้วเก็บ raw AI outcomes ผ่าน KKU เท่านั้น
ตามขั้นตอน development ที่ใช้จริง. ใช้ Sonnet 5 `/messages` thinking disabled และ
Gemini 3.5 Flash Lite `/chat/completions` ตาม contract ที่ทีมตรวจแล้ว; max output4096 ต้องมี receipt จริง.
ไม่ส่ง failure feedback, ไม่ตัด/ซ่อม Java, ไม่อ้าง input-domain equivalence ของ AI จน review.
หาก suite invalid ให้เก็บ outcome ทั้งชุดและรายงาน ไม่วนแก้เพื่อให้ผ่าน.

ออมรับ AI suites ใหม่เมื่อมี exact hashes แล้วทำ full Defects4J ของ unchanged archives.
Generic live queue/worker ยังไม่รองรับ scoped preparation contract นี้โดยอัตโนมัติ;
อย่าเปลี่ยน generation_ready/enabled_stages เพื่อข้าม guard. Protocol เป็น proposal, primary0,
Gate A=false และ reserve=null. การเก็บ development results ตามแผนผู้ใช้ไม่ใช่การอนุมัติ primary/Gate A.
รับ ready bugs ถัดไปจากทีมและทำตาราง/รายงานควบคู่ ไม่กลับไป compose candidate.
