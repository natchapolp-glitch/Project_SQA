# Champ รับ scoped Codec candidates ของ Beam a44f796f

ตรวจวันที่ 4 ตุลาคม 2026 จาก `beam a44f796f6b72281b0bd39d3f26bfc80519e00710`
และต้นฉบับ shared v10 `aom a4880fb2fde574e77705841f62f302273be7dcd9`.
**รับร่วม bounded candidate ทั้ง 5 signatures / 43 cases**;
shared integration ยังไม่เสร็จ และ `candidate_fault_detected=false` ตามจริง.

| Exact declaration | Cases | Preconditions / oracle ที่รับ |
|---|---:|---|
| Metaphone.isNextChar(StringBuffer,int,char) | 8 | Fresh default Metaphone และ real non-null ABCA/empty/A buffer; declared indices/char; adjacent-next Boolean, false guard boundaries, buffer/maxCodeLen state คงเดิม |
| Metaphone.isPreviousChar(StringBuffer,int,char) | 8 | Fixture เดียวกันตาม declared cases; adjacent-previous Boolean และ false guard boundaries; buffer/maxCodeLen state คงเดิม |
| Metaphone.isVowel(StringBuffer,int) | 9 | AEIOUB indices 0–5; True สำหรับ vowel 5 ตัว/False สำหรับ B; empty/negative/at-length ตรวจ exact StringIndexOutOfBoundsException; state คงเดิม |
| Metaphone.regionMatch(StringBuffer,int,String) | 9 | Real non-null ABCA/empty buffer และ bounded non-null needle; match/mismatch/too-long/negative/end/beyond-end; empty needle ที่ end/empty buffer เป็น True; state คงเดิม |
| SoundexUtils.difference(StringEncoder,String,String) | 9 | Static package helper กับ reflective production SoundexUtils identity และ real fresh default Metaphone encoder/maxCodeLen4; inputs เฉพาะ null/empty/A/a/E/B/AB; positional score จาก sealed literal encoded referencesและ encoder state คงเดิม |

สี่ Metaphone declarations เป็น private instance methods; difference เป็น package-local static method.
ใช้ exact production receiver/default constructor, getDeclaredMethod/setAccessible และ exact JVM descriptors.
ไม่รับ arbitrary encoders/maxCodeLen, null buffer/needle/encoder, non-ASCII/overflow indices,
EncoderException propagation หรือ semantic ของ public callers ทั้งหมด.
Legal null strings ของ difference แยกจาก null encoder; invalid vowel indices เป็น declared target boundaries.

[Joint verdict](../../output/api854-20261004/champ-codec-joint-review-v2/joint-verdict.json)
ใส่ Champ verdict/agreed preconditions/agreed oracle ครบตาม template;
`joint_acceptance_complete=true`, `accepted_into_shared_inputs=false`, `shared_integration_approved=false`.
เก็บ sealed policy ที่ยังเป็น pending ก่อน execute ไว้ตามประวัติ ไม่แก้ verdict ย้อนหลัง.

## ผลตรวจและรันเพิ่มบนเครื่องแชมป์

- Root manifest 88 entries / nested native manifest 68 entries ผ่าน;
  received provenance 7 ไฟล์ตรง exact Aom Git blobs และ historical Beam runtime 41 pins ตรง Beam commit.
  Runtime ชุดนี้เป็น baseline ของบีม; standalone probe ไม่ใช่ shared SqaProbe ของ Aom v10.
- ตรวจ raw native command records 21 รายการ: stdout/stderr/retained gzip archive hashes และ exit codes ตรง.
  Beam native host เป็น Linux/WSL Java11, beam-pc1 CPU1 และ held/reused lock exit9/0.
- ตรวจ production Codec-1 revisions จาก local Defects4J commit-db:
  fixed `52d82d1dfff8c2b2ded9d843e0b03017af6d747c`, buggy `9c0cabead7cf075308b11362172ae1a48d41321c`.
  Local production tar bytes ตรง Beam; Java source inventory 25 ไฟล์ต่อ revision ตรง seal.
  Fixed target sourcesตรง retained Aom bytes หลังตรวจ normalized EOL ของต้นฉบับ production.
- Champ ตรวจ oracle โดยอิสระทั้ง43 casesจาก adjacency/vowels/region bounds และ positional score;
  literal table null/empty→empty, single A/a/E/B และ AB→ABตรง bounded production contract.
  สร้าง cases.tsv ใหม่จาก policyและ independent oracle แล้วตรวจทุก argument/hashตรงก่อน execute.
  ไม่เรียก target/differenceEncoded เพื่อสร้าง expected value.
- รัน received evidence guard tests บน isolated pinned Beam snapshotใหม่: **7 ผ่าน / skip0**.
  Guard testsครอบคลุม coherent wrong Boolean/score/state, fixture/skips, receiver/descriptor และ source seals.
- Champ native Corretto Java17, compile production/probeด้วย `--release 8`:
  fixed43 casesสองรอบ = **86 observations**; buggy43 casesสองรอบ = **86 observations**.
  JDI อีก43ต่อ revision = **86 traced observations**; exact first entriesครบ5 declarations/43 casesต่อ revision.
  ค่า Boolean/integer/exception, full buffer contents/length/capacityและ maxCodeLenตรง Beamทุก stage.
  คง bytecodes เดิมระหว่าง tracing; ไม่อ้าง bytecode equality ข้าม compiler หรือ line/branch coverage percentage.
- Champ รัน sensitivity เพิ่ม: isNextChar ==→!= จับ3cases; difference score+1จับ9cases.
  Restore original sourceและ compiled class hashesครบ. ทั้งสองเป็น controlled mutations
  แยกจาก production buggy evidence ไม่เพิ่ม fault ของ Codec-1หรือ experiment cases.
- Fixed/buggy productionทุก stage executed43/target_checks43/passed43/failed0/skipped0/fixture_errors0.
  **ยังไม่พบ fault ของ Codec-1จาก candidateนี้**. นี่คือ standalone helper development;
  ไม่ใช่ full Defects4J evaluation หรือ CMA-ES/FSCS-ART/AI experiment.

เก็บ failed attempt v1ครบ: archive guardหยุดก่อน tests/Java เพราะ default Windows Git exportแปลง EOL.
แก้โดยส่ง `-c core.autocrlf=false` เฉพาะคำสั่ง git archive แล้ว tar hashesตรง Beam;
ไม่ได้เปลี่ยน Git configถาวรหรือ normalizeหลักฐานเดิม. **v2เป็น successful packetปัจจุบัน**.
Champ shared source/evidence/runtime checkpoint100pinsคง bytesเดิม.

## งานของออมก่อนรวมเข้าชุดใหม่

Actual v10ยัง **390 selected / 301 exclusions / denominator691**;
ตรวจ Codec targetsจริงแล้ว existing13 selected / candidate5 excluded.
**395/296เป็น proposalเฉพาะการเพิ่ม Codec5บน v10หลัง shared integrationผ่าน**.
Chronology v11ที่ Aom6c0f6328 และ Graphics7เป็นงานคนละ condition/packet;
หากเลือก baselineอื่นให้คำนวณ unionจาก actual identitiesที่ตรวจรับแล้ว ไม่บวก proposalอัตโนมัติ.

ออมทำ prospective shared implementation/testingต่อได้จากคำรับร่วมนี้:

1. Bind fresh real StringBuffer/default receiver และ real Metaphone StringEncoder lifecycleบน shared helper.
   Mapเฉพาะ bounded domainที่รับให้ทั้ง CMA-ES/FSCS-ARTเข้าถึงได้;
   separate fixture_errorจาก actual target assertions และทำ scalar/exception/buffer/encoder-state projection.
2. ตรวจ Codec13เดิม รวม Metaphone setter/getter state; รักษา setter/JDOM/Math/Buffer/Csv/Lang ที่รับแล้ว.
3. ให้ source/context/fixture/oracle knowledgeเดียวกันทั้ง4 approaches;
   ตรวจ shared fixedซ้ำ/buggy/exact entries/sensitivityและ consumers/host ของ conditionใหม่นั้น.
4. ส่ง condition/preparation/index/protocol/runner/runtime/prompts/40-pair worksheetของรุ่นเดียวกัน
   ให้แชมป์ตรวจ bindingsและวัด reserveใหม่. ไม่ใช้ reserve v10แทนรุ่นที่เพิ่ม recipes.

GateA=false / final reserve=null / KKU requests0 / live queue mutation0 / primary result additions0.
ยังรอ provider/settings/limits/token framing/current quota/reset/expiryและเกณฑ์เปิดทดลองร่วมกัน.

## Paths / SHA-256 ส่งกลับ

- [Receipt](../../output/api854-20261004/champ-codec-joint-review-v2/receipt.json)
  `787bd56f78ff919450cfa47ac914258c5bedaba0fc58961de85d1f4711e2050e`
- [Joint verdict](../../output/api854-20261004/champ-codec-joint-review-v2/joint-verdict.json)
  `f676f016e6f2132e4967fdb5997af9b74eef7d87b1a796ebccad6c480c970f9b`
- [Checksums](../../output/api854-20261004/champ-codec-joint-review-v2/checksums.json)
  `d81d3a2ac25c62c51a29345a5079ec08fa1932dfb837af3d62c66b934bc6c447`
- [Return index](../../output/api854-20261004/champ-codec-return-index-v1.json)
- [Reviewer/producer](../../scripts/study/api854/review_codec_candidates.py)

ตรวจซ้ำต้องใช้ outputใหม่:

```powershell
python -B -X utf8 -m scripts.study.api854.review_codec_candidates --defects4j D:\Projects\Project_SQA\defects4j --output output/api854-20261004/champ-codec-joint-review-NEW
```

## ฝากออม

แชมป์รับร่วม Codec5ของ beam a44f796fแล้วครับ ใช้ joint-verdict/receipt/path/hashตามเอกสารนี้ได้เลย.
Champ native fixed/buggy43casesซ้ำและ JDI exact5ผ่านตรง Beam, guard tests7ผ่าน, fault=false.
ขอ integrate bounded StringBuffer/real encoder lifecycleกับ meaningful value/exception/state oracle,
ตรวจ Codec13เดิมและรักษา accepted recipes แล้วส่ง condition/preparation/prompts/worksheetใหม่ให้วัด reserveครับ.
v10ยัง390/301; 395/296เป็น Codec-only proposalหลัง integrationผ่าน ไม่รวม Chronology/Graphicsอัตโนมัติ.

## ฝากบีม

แชมป์ตรวจรับ a44f796fครบทั้ง5ตาม scoped templateแล้วครับ preconditions/oracles/exact signaturesตรง,
43cases nativeทุก stageตรงกัน และ mutationsจับ3/9casesตามหลักฐาน. ไม่ต้องรอ Champ bounded verdictเพิ่ม.
เมื่อออมส่ง shared conditionใหม่ ขอร่วมตรวจ Codec13เดิม+candidate5, inputsทั้ง4และ host bindingsรุ่นนั้น;
รักษา fault=false และ method-entry/full coverage/primary resultsแยกตามจริงครับ.
