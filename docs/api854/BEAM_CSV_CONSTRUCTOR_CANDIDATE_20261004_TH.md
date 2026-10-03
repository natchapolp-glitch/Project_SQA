# บีมส่ง Csv-1 constructor สำหรับตรวจร่วม — 4 ตุลาคม 2026

บีมทำ bounded fixture/oracle พร้อมหลักฐาน **1 exact declaration / 5 cases** ที่ยังเป็น
exclusion ใน shared v12: `org.apache.commons.csv.ExtendedBufferedReader(Reader)`.
ใช้เครื่อง `beam-pc1` CPU1 และ real Defects4J evaluator; ยังรอออม/แชมป์ตรวจร่วมและ integrate.
**ยอด shared คง 403 selected / 288 excluded / 691**. ไม่ใช่ primary generation result.

## Fixture และ oracle

ใช้ `StringReader` จริงผ่าน forwarding Reader ที่นับ read/close โดยไม่เปลี่ยนเนื้อหา:
empty, ASCII, CRLF, Thai/supplementary-character String และ null Reader boundary.
Expected observations เป็น constants จาก documented initial state และ sealed input Strings
ก่อนรัน fixed/buggy: lastChar `UNDEFINED=-2`, lineCounter0, constructor ไม่อ่าน/ปิด Reader.
ตรวจ fields ที่ constructor initialize ก่อนใช้ follow-up methods;
จากนั้นตรวจ first UTF-16 character/EOF, readAgain/line count และ close ส่งถึง supplied Reader ครั้งเดียว.

การอ่านหลัง constructor ตรวจการผูก argument กับ receiver จึงไม่ได้เพิ่ม target count.
ไม่ได้ตรวจการอ่าน String ทั้งหมด, arbitrary Reader implementations, I/O failures, concurrency,
หรือ full legal input domain. Null boundary เป็นหนึ่ง case; มี non-null fixtures อีก4casesจริง.

JDIเข้า owner/method/descriptor **`<init>(Ljava/io/Reader;)V`** พร้อม positive production source line
ครบ5casesทั้ง fixed และ buggy รวม null boundary. Tracing คง full observations/production bytecode เดิม.
Mutation initial lastCharเป็น0ถูก oracleตรวจพบทั้ง4non-null cases; restore bytecodeตรงเดิม.

## ผลการรัน

- Native fixed5cases×2ผ่าน; buggy5cases×2ต่างที่ `crlf` เหมือนกัน.
- **constructor initial stateผ่านทั้งสอง revisions**. ความต่างคือ follow-up `read()` นับ CR
  เป็น1ในfixedและ0ในbuggy: เป็น component/integration fault observation ไม่ใช่ constructor defect claim.
- Pack `suite.tar.bz2` เป็น sourceเดียวมี nested helperและ5JUnitmethods; method cap30.
- Actual Defects4J: fixedสองรอบผ่าน, buggyมี1failure
  `org.apache.commons.csv.CsvConstructorTest::constructor_crlf`.
- fixed-1/fixed-2/buggy/coverage countersครบ **5 executed / 0 skipped / 5 checks** ทุก stage.
- Coberturaสำหรับ modified `ExtendedBufferedReader`: **11/37 lines, 3/26 branches**.
- Evidence guards5ผ่าน/skip0: reject missing/duplicate/reordered cases, wrong state/type,
  changed sealed inputs, wrong exact identity/attribution, invalid evaluator stage/approval boundary.
- Actual D4J fixed modified source SHAตรง received Aom v12. CPUlockheld/released exits9/0;
  shared runtime source pinsเดิมก่อน/หลัง execution.

Native-v1หยุดเพราะ producerสมมติว่า buggyทุกcaseต้องผ่าน. เก็บ raw logs, preexecution seal,
checksumsและ `verify_native_attempt1.py` ไว้; native-v2ยอมบันทึก genuine buggy differences
โดยไม่เปลี่ยน fixture/expected oracleหรือเลือก casesจาก buggy outcomes.

## ตรวจหลักฐาน

Root: [candidate packet](../../output/api854-20261004/beam-csv-constructor-candidate-v1/receipt.json),
[native receipt](../../output/api854-20261004/beam-csv-constructor-candidate-v1/native-v2/receipt.json),
[evaluation record](../../output/api854-20261004/beam-csv-constructor-candidate-v1/d4j-v1/measurement/record.json),
[checksums](../../output/api854-20261004/beam-csv-constructor-candidate-v1/checksums.json).

| Artifact | SHA-256 |
|---|---|
| Root receipt | `f8ea40db62ef0485afa073212c9fbecde7461e1329d94c535f72b80e4e565b33` |
| Root checksums | `0e40b9d4fd260a210a7ded73943fdc3f75b72f09661bfdd45b6eaff66ad7b296` |
| Suite | `17608e7269a7563e34a1765ea5b5213704745b4be74ee8d652a6a5cec5f94ade` |

ครบ1new full manual Defects4J evaluationของ Csv; รวมกับCollectionsรอบนี้เป็น2development evaluations.
**KKU requests0, queue mutations0, primary results0, Gate A=false**.

## ส่งต่อ

> บีมส่ง Collections10และCsv constructor1 bounded candidatesพร้อม native exact-entry,
> fixedสองรอบ/buggy/coverageจาก evaluatorจริงแล้วครับ ขอออม/แชมป์ตรวจ scoped preconditions/oracles
> และ shared integration. Csvมีfault observationจากfollow-up CR line counting ไม่ใช่constructorfault.
> Shared v12ยัง403/288/691; Codec5รอshared integration, enum4คงpendingร่วมทีม;
> recipe/evidenceอื่น268และselected semantic completionยังไม่ปิด. ไม่เรียก KKUหรือเปิดpilotครับ.
