# Champ Chronology candidate หลังรับช่วง e742095d

รับช่วงบน `champ` จาก `e742095d` หลัง fetch เฉพาะ branch และตรวจ checkpoint:
source/evidence/runtime pins เดิม 48 รายการตรงกับ received bytes, focused tests เดิม 8 ผ่าน
และ shared audit ได้ 20 bugs / selected 380 / unsupported 311 / denominator 691.
แก้ local fetch config โดยเอา refspec ของ `codex/champ-v7-intake` ที่ลบไปแล้วออก;
ไม่ได้เปลี่ยน remote branches. เก็บ PDF untracked ของผู้ใช้ไว้และไม่รวมใน commit.

สร้าง candidate แยกจาก shared runtime สำหรับ `org.joda.time.Chronology` ทั้ง 6 identities
ของ Time-1 ใน [worklist v9](../../output/api854-20261003/aom-champ-v9-readiness-worklist-v1.json).
[Policy](../../scripts/study/api854/development/chronology/policy.json),
[suite](../../scripts/study/api854/development/chronology/ChronologyProbe.java) และ
[verifier](../../scripts/study/api854/verify_chronology_development.py)
ถูก snapshot และ seal ก่อน compilation/execution.

## ขอบเขต fixture/oracle

ใช้ real ISO/Buddhist Chronology factories, UTC และ fixed +07:00 offset.
ตั้ง JVM default timezone เป็น UTC และเลือก production `UTCProvider` ชัดเจน;
ไม่ได้ตรวจ named timezone หรือ timezone database resources.
เมธอดใช้ default Partial แล้ว seed field ที่มีค่าถูกต้องก่อนเริ่ม trace target.
assert exact field types/values, chronology/UTC, array isolation, receiver immutability
และ object identity ตามกรณี same/different normalized chronology.

| Exact target | Cases | สิ่งที่ตรวจ |
|---|---|---|
| `Partial(Chronology)` | 2 | +07:00 → UTC และ typed null → ISO, empty fields |
| `Partial(DateTimeFieldType,int,Chronology)` | 2 | hour 10 และ reject hour 24 |
| `Partial(DateTimeFieldType[],int[],Chronology)` | 3 | leap day 2024-02-29, defensive arrays, reject Feb 30 และ year/dayOfMonth/era ที่เรียงผิด |
| `Partial(Chronology,DateTimeFieldType[],int[])` | 1 | package-private target กับข้อมูล UTC ที่ validate แยกก่อนแล้ว |
| `getField(int,Chronology)` | 2 | protected target คืน field จาก supplied Buddhist chronology (epoch year 2513), receiver ISO คงเดิม; reject bad index |
| `withChronologyRetainFields(Chronology)` | 3 | retain hour 10, normalize UTC, same chronology คืน receiver เดิม, null → ISO, receiver เดิมคงค่า |

Chronology-first constructor เป็น internal constructor ที่ไม่ validate/normalize และเก็บ arrays โดยตรง.
candidate ไม่อ้าง defensive copying, arbitrary-value validation หรือ zone normalization ของ signature นี้.
package-local probe เรียก protected/package-private targets บน real final Partial โดยไม่แก้ production source.
ขอบเขตนี้ยังต้องถูกนำไปออกแบบ recipe/invocation ใน shared runtime ถ้าทีมจะรับเข้า.

## ผลรันและหลักฐาน

[Packet v2 receipt](../../output/api854-20261003/chronology-development-v2/receipt.json),
[checksums](../../output/api854-20261003/chronology-development-v2/checksums.json)
และ [preexecution seal](../../output/api854-20261003/chronology-development-v2/preexecution-seal.json)
เก็บ source archives, compiled-source hashes, dependency hash, suite/verifier snapshots,
raw stdout/stderr และ command receipts.

ใช้ underlying Time-1 revisions จาก local Defects4J commit-db:
fixed `9a62b06be5d0df8e833ff8583398cca386608cac` และ
buggy `8612f9e5b88c1bea933ef9ab1e431f5db3006b48`.
fixed Partial ที่ compile เป็น exact retained v9 bytes หลังเทียบ source content กับ mirror;
dependency source ที่เปลี่ยนระหว่าง revisions เช่น `UnsupportedDurationField` มาจาก revision ของตนเอง.
ใช้ local `joda-convert-1.2.jar`; ไม่มี downloads หรือ provider requests.

| Stage | Executed / checks | ผ่าน / assertion fail | Skips / fixture errors |
|---|---:|---:|---:|
| fixed first และ second (แต่ละรอบ) | 13 / 13 | 13 / 0 | 0 / 0 |
| buggy first และ second (แต่ละรอบ) | 13 / 13 | 12 / 1 | 0 / 0 |
| fixed JDI trace | 13 / 13 | 13 / 0 | 0 / 0 |
| buggy JDI trace | 13 / 13 | 12 / 1 | 0 / 0 |

observations ซ้ำตรงกันในแต่ละ revision และ tracing ไม่เปลี่ยน observations หรือ production bytecode.
JDI ยืนยัน first exact target descriptor ครบ 13 cases / 6 declarations ในทั้งสอง revisions.
เป็น method-entry/source-entry-line evidence ไม่ใช่ line/branch coverage percentage.
ในทุก buggy runs กรณี `arrays_bad_order` ไม่ reject ลำดับ year/dayOfMonth/era ตาม oracle;
fixed reject ด้วย `IllegalArgumentException`.
นี่เป็น candidate fault detection จากการ compile/run underlying revisions,
ยังไม่มี full Defects4J evaluation หรือ primary experiment.

temporary fixed mutation ที่เปลี่ยน `getField` ให้ใช้ receiver chronology แทน argument
ถูกจับโดย Buddhist field/value oracle เฉพาะ `getfield_buddhist`.
mutation sensitivity แยกจาก production fault detection และไม่เปลี่ยน shared source.
stdout/stderr ทุก stage ของ v2 ถูกเก็บ; stderr ทั้งหมดว่าง.

[Attempt v1](../../output/api854-20261003/chronology-development-v1/failure.json) เก็บไว้ครบ:
fixed assertions ผ่าน 13 แต่ Python verifier ระบุ base exception class แทน
`IllegalFieldValueException` ที่เป็น subclass จริง และ JVM เตือน timezone resources ที่ไม่ได้ compile.
v2 แก้ exact exception expectations และ pin UTCProvider; ไม่เขียนทับ v1.

## การตรวจรับและงานต่อ

ผ่าน **16 focused integrity tests** (เดิม 8 + Chronology ใหม่ 8), ไม่มี skip/failure/error.
ตรวจ wrong descriptor, weakened value oracle, setup failures, skipped counters,
unexpected runtime failure, duplicate cases, packet checksums/repeats และ existing-output guard.
[Test receipt](../../output/api854-20261003/champ-chronology-tests-v1.json),
[raw log](../../output/api854-20261003/champ-chronology-tests-v1.log)
และ [continuation pins/summary](../../output/api854-20261003/champ-chronology-continuation-v1.json).
ไม่ได้รัน full 351 integration tests ซ้ำ; runtime 41 files และ shared v9 inputs คง hashes เดิม.

ทั้ง 6 identities ยัง unsupported. owner oracle/integration approval ยัง false;
denominator 691, selected 380, unsupported 311, empty-enum unsupported 4 คงเดิม.
Gate A=false, final reserve=null, primary results added=0;
ไม่มี KKU requests, live queue mutations หรือ quota ledger imports.

งานต่อคือให้ทีมตรวจ bounded domains/assertions และ protected/internal invocation ของ packet นี้,
หรือพัฒนา Graphics2D ทั้ง 7 identities เป็น packet แยกต่อได้.
หลังรับ recipe เข้าร่วมจริงจึงสร้าง preparation/policy/condition/protocol/runner รุ่นใหม่เป็นคู่เดียวกัน,
ตรวจ shared prompts/hashes/bytes ทุก approach และคำนวณ limits/reserve ใหม่.
อย่าเปลี่ยน approval flags หรือ selected counts จาก candidate proof เพียงอย่างเดียว.

คำสั่งตรวจที่ไม่เขียนทับ sealed output:

```powershell
python -m unittest -v scripts.study.api854.tests.test_v9_continuation_evidence scripts.study.api854.tests.test_chronology_development
```

ถ้าต้อง execute verifier อีก ใช้ output รุ่นใหม่เสมอ:

```powershell
python -m scripts.study.api854.verify_chronology_development --defects4j D:/Projects/Project_SQA/defects4j --output output/api854-20261003/chronology-development-v3
```
