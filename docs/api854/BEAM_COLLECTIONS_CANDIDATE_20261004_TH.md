# บีมส่ง Collections-1 อีก 10 exact declarations สำหรับตรวจร่วม

ชุดนี้มี **40 bounded cases / 10 declarations** ที่ยังเป็น exclusions ใน Aom v11/v12.
บีมรับ scoped fixtures/oracles ของชุดนี้แล้ว พร้อม native JDI และ full Defects4J evaluator
บน `beam-pc1` CPU1. ยังไม่รวมใน shared condition และยังรอคำรับออม/แชมป์.

## ขอบเขตและ oracle

ใช้ real `Flat3Map` ไม่ subclass, String/null keys และ values ใน map ขนาด 0/1/3/4;
แต่ละ case สร้าง fixture ใหม่. `convertToMap` รับเฉพาะ flat mode ขนาด 0/1/3.
`readObject` / `writeObject` เข้า private hooks ผ่าน object graph และ
`ObjectInputStream` / `ObjectOutputStream` จริง ไม่สะท้อนเรียก hook กับ stream ที่ยังไม่อยู่ใน serialization context.

| Exact method | Cases | Assertions |
|---|---:|---|
| `Flat3Map(Map)` | 5 | Full copied map, source unchanged, independent result; null input → exact NPE |
| `convertToMap()` | 3 | Full mappings retained, delegate mode, original flat slots cleared |
| `createDelegateMap()` | 4 | Empty fresh independent map; returned-map mutation leaves receiver unchanged |
| `entrySet()` | 4 | Full key/value pairs and backed-view clear changes receiver |
| `keySet()` | 4 | Full keys and backed-view clear changes receiver |
| `values()` | 4 | Full value multiset including null; backed-view clear changes receiver |
| `mapIterator()` | 4 | Full original pairs, exact getKey/getValue, setValue previous return, only selected key changes |
| `hashCode()` | 4 | Independent Map hash contract: sum of key/value String hashes XOR, null hash zero |
| `readObject(ObjectInputStream)` | 4 | Full valid native round-trip content, independent map/source and return storage |
| `writeObject(ObjectOutputStream)` | 4 | Full valid native round-trip content, unchanged source and independent deserialized copy |

Expected observations คำนวณจาก sealed fixtures ด้วย independent Python map/multiset/String hash model
ก่อนรัน Java. เก็บ full observations และ raw bytes ไม่ใช้ object string หรือ identity hash เป็น oracle.
Native JDI ตรวจ first entry exact owner/method/descriptor/positive source line ครบ 40 cases / 10 declarations
ทั้ง fixed และ buggy. Observation ที่เกิดจาก fixture/projection failure ไม่ผ่าน guard.

## ผลสด

- Java11 native: fixed 40 cases × 2 ผ่าน, buggy 40 cases × 2 มี `mapIterator_three` ล้มเหลวตรงกัน;
  trace run คง observations และ production bytecode เดิม.
- Controlled hash mutation จับ 3 flat-mode cases และ delegated case ยังผ่านตาม scope;
  omit-first-mapping conversion mutation จับ 2 nonempty cases. Mutations แยกจาก original production sources.
- Guard tests **10 passed / 0 skipped**, รวม wrong hash/type, changed map/alias/state,
  missing/wrong exact entry, fixture errors/skips, source/archive/seal consistency และ actual evaluator evidence.
- Full Defects4J development evaluation: JUnit **10 grouped methods / 40 nested cases**,
  fixed สองรอบผ่าน, buggy มี failure `bounded_mapIterator`; counters ทุก stage
  fixed-1/fixed-2/buggy/coverage = executed10 / skipped0 / target_checks10.
- Cobertura: **203/495 lines, 103/376 branches** ของ explicit modified class `Flat3Map`.
  ผล `fault_detected=true` เป็น manual bounded component evidence ไม่ใช่ผล CMA-ES/FSCS-ART/KKU pilot.
- แพ็ก helper เป็น nested class ใน source เดียว เพื่อไม่ให้ Defects4J discover helper เป็น empty test class.
  Package layout และ suite cap30 ตรวจด้วย `pack_suite`; suite เดิมไม่เปลี่ยนระหว่าง evaluator stages.
- Actual Defects4J checkouts เปรียบเทียบ fixed modified-source SHA กับ Aom preparation แล้วตรงกัน.
  Native archive ใช้ public `active-bugs.csv` production revisions และเก็บ provenance;
  ไม่อ้าง synthetic checkout commit ว่าตรงกันข้ามเครื่อง.

## Paths และ pins

Root: `output/api854-20261004/beam-collections-candidate-v1/`.

- [receipt](../../output/api854-20261004/beam-collections-candidate-v1/receipt.json)
- [policy และ exact descriptors](../../output/api854-20261004/beam-collections-candidate-v1/policy.json)
- [native receipt](../../output/api854-20261004/beam-collections-candidate-v1/native-v4/receipt.json)
- [actual evaluator record](../../output/api854-20261004/beam-collections-candidate-v1/d4j-v3/measurement/record.json)
- [suite manifest](../../output/api854-20261004/beam-collections-candidate-v1/d4j-v3/packaged/suite-manifest.json)
- [checksums](../../output/api854-20261004/beam-collections-candidate-v1/checksums.json)

| Artifact | SHA-256 |
|---|---|
| Root receipt | `e60e75ff4c30d9a07213de3d46f93b35679e5b40cb89e3509ebce0f230894eda` |
| Root checksums | `1b88a182152dd9bae669a51940b498b63e86e1beaf5b88c379fc186b107faf07` |
| `suite.tar.bz2` | `15f0b34f4cd88fab3362db18c042ac4542a98d39dcf4c6d7593a3b7888cf5b65` |

Native attempts1–3 และ evaluator attempts1–2 ที่ไม่ผ่านยังอยู่ครบพร้อม logs,
checksums และ original producer sources. Corrections แก้ metadata lookup, probe measurement timing,
mutation-path count และ JUnit packaging/name; ไม่ลด targets หรือเปลี่ยน expected map semantics เพื่อให้ buggy ผ่าน.

## งานที่ต้องตรวจร่วม

ขอออม/แชมป์ review exact 10 declarations, bounded input/valid stream preconditions,
oracle, method-entry และ full evaluator evidence. หากรับแล้วจึง implement prospective
shared recipe/policy/context สำหรับทั้ง 4 approaches พร้อม regress Collections12 เดิม,
ออก condition/preparation รุ่นใหม่และให้บีมตรวจ host/consumers ตาม pins ใหม่.

ยังไม่รับ arbitrary key/value/Map subclass หรือ malformed serialization streams.
ชุดนี้ไม่เพิ่ม selected ใน shared v11/v12 อัตโนมัติ; denominator ยังคง691.
Enum4 pending ร่วมทีมตามคำตอบผู้ใช้. KKU calls0, live queue mutations0, primary results0,
Gate A false, final reserve null.

## ข้อความคัดลอกส่งเพื่อน

> บีมส่ง Collections-1 อีก10 exact declarations /40 bounded cases ให้ตรวจร่วมครับ.
> Native fixed/buggy อย่างละสองรอบและ JDI เข้า exact targetsครบ10; guard tests10ผ่าน.
> Full Defects4J evaluator fixedสองรอบผ่าน, buggyพบ `bounded_mapIterator`,
> coverage203/495lines และ103/376branches. อ่าน `BEAM_COLLECTIONS_CANDIDATE_20261004_TH.md`
> พร้อม policy/receipt/checksums ได้ครับ. ยังเป็น manual component development,
> ไม่ใช่ shared integration หรือผล pilot; ไม่เรียก KKU/คิว และยังไม่ปิด requirement691/GateA.
