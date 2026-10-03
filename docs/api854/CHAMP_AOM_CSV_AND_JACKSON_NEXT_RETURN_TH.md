# Champ รับ Csv ของ Aom และส่ง JacksonDatabind-112 ชุดถัดไป

รับ scoped evidence จาก `aom 4334c2ab908f517f99c7046c08f45a25c0b6a64d` แล้ว
และจัด JacksonDatabind-112 ให้ออมรันต่อบน `aom-pc1` CPU1. Compress-1 ยังส่งให้บีมตามเดิม.
ใช้ frozen preparation/runtime จาก `aom 63ad195623c2ed3f67f3ae232c00c54d3160ce72`;
ไม่เปลี่ยน shared v12/v13, queue หรือ Gate A.

ระหว่างปิด publication พบ push ใหม่: Beam `f3484746fc19b5aa642e51fb08de5471a0c17509`
ส่ง Compress full Defects4J2algorithmsแล้ว และ Aom `7f0c6d31a4fee1b8e4b20ca8cf02513dceb990e3`
ส่ง Cli prospective unordered oracle+full Defects4J2algorithmsแล้ว. Champ อ่านเอกสารทั้งสองแล้ว
แต่ยังไม่รวมเป็นคำรับใหม่ในตาราง10suite-host rows ด้านล่าง; ต้องตรวจ pinned raw receipts แยกต่อ.
ไม่ให้เพื่อนรัน Compress/Cliชุดเดิมซ้ำ. Cli raw false-positive conditionเก่ายังคง quarantine;
conditionใหม่ของออมมี AI pending ต้องตรวจ preparation-v2/worksheet ก่อน provider calls.

## Csv ที่ตรวจรับ

ตรวจ hashes ของ Aom 10 packets รวม **881 entries**, canonical records,
raw test-start lists 16 ชุด, coverage XML 4 ไฟล์, unchanged archives,
official isolated-bug patch และ dependency hashes ที่ออมเก็บหลังรัน 22 ไฟล์.
ตรวจ raw logs ของ regression 7+16 = 23 tests ที่ออมรันผ่าน; Champ ไม่รัน regression/Csv/KKU ซ้ำ.

| วิธี | Tests | Fixed 2 รอบ/coverage | Buggy | Lines | Branches |
|---|---:|---|---|---:|---:|
| CMA-ES | 30 | ผ่าน | ไม่พบ fault | 31/37 | 13/26 |
| FSCS-ART | 30 | ผ่าน | ไม่พบ fault | 31/37 | 13/26 |
| Sonnet Messages | 21 | ผ่าน | CR fault 1 test | 36/37 | 22/26 |
| Gemini | 18 | ผ่าน | CR fault 1 test | 37/37 | 23/26 |

Algorithm executed/target checks =30, skipped=0 ทุก stage.
AI รอบออมยืนยัน 21/18 test-start events แต่ไม่มีแยก skip/target counters จึงคง `null`;
ไม่ยืม skip=0 จากเครื่องบีมหรือ native. Sonnet `total_tokens` ยังคง `null` ตาม provider.
CR failures ตรงกับ native/Beam: `lineNumberDoesNotDoubleCountCRLF` expected1/actual0
และ `testCarriageReturnLineNumber` expected2/actual1.

เก็บ strict source attempt v1 ที่หยุดก่อน tests และ native/reconstructed Java+diff ครบ.
ตรวจ independently ว่า fixed + official GNU patch ให้ buggy hash
`4ddf7df1c7c8b9e3d45dc6a3410f802a2e5b641f37089655c3eb2f52cdfd5185`
ตรง binding ที่ออมประกาศก่อน v2. Native parent กับ Defects4J isolated bug เป็นคนละ source condition.
คำรับนี้ผูก sealed producer ที่มี worktree/source guards; Aom packet ไม่ export source inventories
ของทั้ง 8 checkouts แยกรายต้น จึงไม่อ้างว่า Champ อ่าน bytes ของ checkout ที่ยังอยู่บนเครื่องออม.

Receipt: `output/api854-20261004/champ-aom-csv-results-review-v1/receipt.json`
SHA-256: `4fa2ed019b1fe2865aa30e265acf11228522e6b8dc87491a1facd37d9902bff0`.
การรัน archives เดิมอีก host เพิ่มหลักฐาน host ไม่เพิ่ม unique bugs หรือ independent generations.
Cli option-order false positive ยังคง quarantine และไม่ถูกนับเป็น scientific fault.
AI Reader domain กว้างกว่า bounded algorithm fixtures; ยังไม่รับรองความเท่าเทียมของ input domains.

## JacksonDatabind-112 ที่ส่งให้ออม

4 exact signatures ของ `com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer`:

- `deserialize(JsonParser,DeserializationContext)`
- `deserialize(JsonParser,DeserializationContext,Collection)`
- `deserializeUsingCustom(JsonParser,DeserializationContext,Collection,JsonDeserializer)`
- `handleNonArray(JsonParser,DeserializationContext,Collection)`

ใช้ constructor/recipes/context/prompt ทั้ง 6 ไฟล์จาก v12 เดิม. Discovery fixed/isolated buggy ตรงกัน;
ตรวจ zero-vector fixed observations ใน fresh JVM 2 รอบต่อ target ก่อนเรียก provider,
และ whole generated suites ต้องผ่าน fixed สองรอบอีกครั้ง.
Fixed revision `8bb7c9abca9a2e298a2436cb995ba1721e49de5c`;
parent `f36222e5c0b318e2b739e2b77b93c5d2c919413f` เป็น metadata ไม่ใช่ buggy source ที่ใช้รัน.
Buggy derive จาก fixed + official `112.src.patch` ก่อน AI requests.
Production dependencies ตรง installed benchmark build ของ revision นี้:
Jackson core2.9.9 / annotations2.9.0 และ `generated_sources/2.9.9/PackageVersion.java`
ที่ framework ใส่ตอน checkout. ไม่แก้ production target.

| วิธี | ผลที่เก็บจริง | Native fixed/buggy/coverage |
|---|---|---|
| CMA-ES | 30 tests | ผ่าน fixed2, buggy, coverage; skip0/target checks30; fault=false |
| FSCS-ART | 30 tests | ผ่าน fixed2, buggy, coverage; skip0/target checks30; fault=false |
| Sonnet Messages | output4096 truncated | invalid; ไม่มี valid suite/buggy/coverage |
| Gemini | 9 tests compileผ่าน | fixed fail4 ทั้ง2รอบ; reject ทั้งsuite; ไม่รัน buggy/coverage |

Algorithms ได้ target-class lines49/91 =53.85%, branches36.67% ทั้งสองวิธี.
ค่าที่ไม่ได้วัดของ AI เป็น `null` ไม่ใช่ coverage0 หรือไม่พบ fault.
เก็บ AI responses/source/assertions เดิม ไม่ repair/prune/resend.
Native Java17/UTC เป็น development measurement; full Defects4J ยังรอเครื่องออม.

Packets:

- `output/api854-20261004/champ-jacksondatabind112-development-generation-v1`
- `output/api854-20261004/champ-jacksondatabind112-native-measurement-v1`
- `output/api854-20261004/champ-jacksondatabind112-benchmark-binding-review-v2`
- `output/api854-20261004/champ-aom-next-replay-guard-check-v2`
- `output/api854-20261004/champ-aom-next-batch-audit-v1`

Native receipt SHA-256: `622af4c66ba145e8f4aaa5626a0efa8cc4c6c569a8fd8ed6e7ef7c1b847a92bd`.
Native checksums SHA-256: `076ef9d1a0935e06e52e5221201735e05bc136bf7a081e939a0464171e126647`.
Ready archives: CMA `f6c42065aac99100da135819cccd9f4c3ca3deb48611a0750528959a49befc73`,
FSCS `279b376fc16c720ff792fb0c1356df4736ef482df6a5306a91e3b87d8f803258`.

## Source-byte binding ก่อนรัน Defects4J

Independent GNU review รอบ v1 พบ source SHA ไม่ตรง native Git-apply และเก็บ failure ไว้.
รอบ v2 derive Git/GNU จาก fixed+official patch ใหม่ทั้งคู่แล้วตรวจทุก source:
ต่างเพียง `StringCollectionDeserializer.java` จาก CRLF287 เป็น LF0;
เมื่อ normalize CRLF→LF bytes เหมือนกันทั้งหมด. Generated PackageVersion ตรงกันด้วย.

Native buggy target SHA `5de8985e5ebcd3f428d5de5edbf2eb8551897c19b87403a365b84cd1f475eb02`;
GNU benchmark SHA `cf946c7fad79f432fb197391905a866e81a9147b742bd95dbd4ebd73fc5ca06f`.
ประกาศ expected full source inventory และ execution condition ใหม่ใน binding-v2 ก่อน host tests.
ไม่ลบ default native source guard และไม่ replace checkout production source.
Wrapper รับ binding เฉพาะ project112 นี้ เมื่อ manifest/native packet/official host patch ตรง,
fixed inventoryเดิม, buggyต่างเพียง targetเดียว และตรวจ source evidence ว่าเป็น EOL-only.
จากนั้นยังตรวจ SHA ของ actual fresh checkout ทุก declared source ก่อนแต่ละ suite.

Wrapper export actual source inventories/config hashes ต่อ checkout และตรวจ declared host jars ก่อน tests;
`--counted` ใช้ Beam XML observer ที่ pin เดิม ภายใต้ CPU1 lock และ restore framework ใน `finally`.
Guard checks5ผ่าน: valid binding, reject non-EOL Java content, wrong bug, wrong native packet,
และ reject Windows ก่อน output/checkout mutation; AST5modulesผ่าน.
ตรวจจริง native compile/instrument ผ่าน Java launcher argument files เพื่อเลี่ยง Windows argv limit.
ยังไม่ claim ว่า wrapper รุ่นนี้ execute บน Linux แล้ว.

Binding checksums SHA-256: `b7bc336dd018c27ff50e3cbf0235109f4473d506ba857eb7378cd1fb7130d37a`.
Wrapper SHA-256: `cd3f475401140245c8734545969564fa05d0ddee36c5e7c4374185ddbb37bd0d`.

ให้ออมดึง `origin/champ` แล้วรันจาก repository root บน Java11 host เดิม:

```bash
python3 -B -m scripts.study.api854.replay_csv_development_d4j \
  --packet output/api854-20261004/champ-jacksondatabind112-native-measurement-v1 \
  --benchmark-binding output/api854-20261004/champ-jacksondatabind112-benchmark-binding-review-v2 \
  --approaches cmaes fscs-art --counted \
  --d4j /home/team/sqa-round2/defects4j/framework/bin/defects4j \
  --worktrees /home/team/sqa-round2/worktrees --worker-id aom-pc1 \
  --output output/api854-20261004/aom-jacksondatabind112-valid-algorithm-d4j-replay-v1
```

ส่ง receipts/actual XML counts/coverage/failing tests/time/source inventories/host jars/hashes
และ scoped semantic verdict กลับมา. ถ้า actual source/library/config ไม่ตรง ให้เก็บ attempt แล้วหยุด;
ห้ามแก้ target/ลด guard/repair assertions เพื่อให้ผ่าน. AI invalid ทั้งสองให้เก็บเป็น outcomes เดิม.
ไม่ต้องเรียก KKU ใหม่. ใช้ dirname ใหม่เมื่อมี attempt เกิดแล้ว.

## โควตาและยอดจริง

รอบใหม่นี้ใช้ a06 ที่ประกาศก่อนเริ่ม new-bug allocation: calibration2+generation2 =4 calls.
Sonnet requested `claude-sonnet-5`, observed `anthropic/claude-sonnet-5` / `Claude Platform on AWS`,
Messages thinkingdisabled/observed0; Gemini `gemini-3.5-flash-lite` / Chat Completions.
temp0/output4096/nonstream/cap30/seed101 ไม่มี retry/account switch/feedback.
Prompt190,537 UTF8 bytes SHA `6b30a549a38af1883d1a6036fb3502b7dd380ffc99a90ba9a5058747f6a4f381`;
observed input tokens Sonnet80,633/Gemini51,129.
Byte+operator buffer+output admission198,729 ไม่ใช่ measured framing/proven maximum/final40pair reserve.
Latest observed a06 remaining Sonnet115,252/Gemini297,281;
ไม่อ้างว่าเป็น quota ปัจจุบันตลอดไป/independent pool/reset/expiry.
Sonnet output4096/totalnull; Gemini output1585/total52714.
รวม billable calls27 = generation14+calibration13; metadata/count404 แยกเดิม.

ตาราง `champ-aom-next-batch-audit-v1/native-results.csv` รวม **6 unique bugs/28 condition×bug×approach outcomes**
โดยรวม invalid และ Csv สอง conditions. `reviewed-host-results.csv` รวม latest reviewed receipts
Beam6+Aom4 =10 suite-host rows /2bugs; archives เดิมบนอีก host ไม่เพิ่ม unique bugs/repetitions.
ยังมีเพียง Csv Messages ที่ครบ4valid full Defects4J methods; ไม่ครบ854/ไม่เปิด Gate A/primary0.
Checkpoint100pinsไม่เปลี่ยน. Mockito compileได้แต่6fixed probesเป็น fixture failureก่อนtarget invocation:
กักไว้พร้อม preflight-v1..v3/review ไม่ใช้ API. Gson ยังต้อง prospective structural Type oracle.

## ข้อความพร้อมส่งต่อ

ถึงออม:

> แชมป์ตรวจรับ Csv aom4334c2ab แล้วครับ 881 hashes/counters/archives/CR faultsตรงหลักฐาน
> และคง AI skip/target null กับ Cli quarantineไว้ ดึง origin/champ อ่าน
> docs/api854/CHAMP_AOM_CSV_AND_JACKSON_NEXT_RETURN_TH.md ได้เลยครับ
> ชุดถัดไป JacksonDatabind-112 CMA/FSCS30ผ่าน native fixed2/coverage/buggy, fault=false
> พร้อม sealed GNU EOL-only benchmark binding ให้รัน Defects4Jบน aom-pc1 CPU1ตามคำสั่ง
> ขอ actual counts/coverage/time/source/host receipts+hashesกลับ ไม่เรียก KKUใหม่
> Sonnettruncated/Geminifixedfailเก็บinvalidครบ รับรู้ Cli oracle ใหม่7f0c6d31แล้ว
> แชมป์จะตรวจ preparation-v2/worksheet ก่อนเก็บ AI ใน conditionใหม่นั้น ส่วน Gson Type oracleเป็นงานแยกครับ

ถึงบีม:

> แชมป์ตรวจรับ Csv ของออม4334c2abแล้วครับ เป็น archivesเดิมอีกhost จึงไม่เพิ่มuniquebugs
> AI countersรอบออมที่ไม่ได้วัดยังnull ใช้ receipt/reviewed-host-resultsประกอบรายงานได้
> รอบใหม่นี้ส่ง JacksonDatabind-112ให้ออม รับรู้ Compress f3484746 ที่บีมรันเสร็จแล้ว
> แชมป์จะตรวจรับ benchmark binding/receipts ต่อ ไม่ต้องรัน Compressซ้ำครับ
> บีมช่วยตรวจ Cli oracle/preparation-v2/source/host/countersจาก aom7f0c6d31ได้ตามคำสั่งออม
> คง Cli quarantine, primary0/GateAfalse และไม่ต้องเรียก KKUซ้ำครับ

Publication/ready pins ล่าสุดอยู่ใน `output/api854-20261004/champ-ready-results-return-index-v6.json`;
เก็บ index-v5 และ pre-steering publication-v1/docs snapshotsไว้เป็นประวัติ.
