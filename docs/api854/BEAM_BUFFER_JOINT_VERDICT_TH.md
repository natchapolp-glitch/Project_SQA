# บีมส่ง verdict ต่อคำตรวจรับออม ec26350f

รับ exact Git blobs ของ `aom ec26350fc919d1be2b4a559c3f91b0072ba04f75`
ตาม [บันทึกตรวจรับออม](../../output/api854-20261003/beam-buffer-joint-review-v1/received-aom/AOM_BEAM_BUFFER_INTAKE_TH.md).
สำเนา template/intake/coverage มี
[commit/path/hash provenance](../../output/api854-20261003/beam-buffer-joint-review-v1/received-aom/provenance.json).
งานนี้อยู่ใน isolated branch/worktree ของบีม ไม่รวม runtime แทนของออม/แชมป์.

**บีมรับ Buffer ทั้ง 8 รายการในขอบเขต bounded prospective recipe composition**
และรับ Csv stream condition เป็นการเปลี่ยน condition ที่ต้องแจ้งใน shared inputs ใหม่.
คำตัดสินจริงพร้อม exact constructors/parameters, preconditions, oracles และ evidence hashes อยู่ใน
[beam-buffer-verdict.json](../../output/api854-20261003/beam-buffer-joint-review-v1/beam-buffer-verdict.json).
`champ_verdict` และ agreed/joint acceptance ยังคง null/pending; ไม่มีการรับรองแทนแชมป์.

## รายการที่รับในส่วนบีม

NumberInput ใช้ constructor types ว่างตาม static discovery;
TextBuffer ใช้ `BufferRecycler` ที่ไม่ null; ExtendedBufferedReader ใช้ production `Reader`.

| Bug / target | Legal preconditions ที่รับ | Meaningful oracle |
|---|---|---|
| JacksonCore-1 / `NumberInput.inLongRange(char[],int,int,boolean)` | decimal digit slice, offset 2/length คู่กัน, sign flag และ long limits | Boolean จาก magnitude เทียบ positive/negative long limit |
| JacksonCore-1 / `NumberInput.parseBigDecimal(char[])` | decimal array ที่ถูกต้อง | BigDecimal value/string/scale จริง |
| JacksonCore-1 / `NumberInput.parseBigDecimal(char[],int,int)` | complete decimal text ใน legal slice | BigDecimal value/string/scale จริงโดยไม่อ่าน padding |
| JacksonCore-1 / `NumberInput.parseInt(char[],int,int)` | ASCII decimal 1–9 digits, legal slice, ไม่ overflow | Integer ตรงค่าทศนิยมที่ประกาศไว้ |
| JacksonCore-1 / `NumberInput.parseLong(char[],int,int)` | ASCII decimal 10–18 digits, legal slice, ไม่ overflow | Long ตรงค่าทศนิยมที่ประกาศไว้ |
| JacksonCore-1 / `TextBuffer.append(char[],int,int)` | initialized `123`/`45.5`, nonempty source slice ใน bounds | initial text + actual slice และ size |
| JacksonCore-1 / `TextBuffer.append(String,int,int)` | preconditions เดียวกันของ String slice | actual contents และ size |
| Csv-1 / `ExtendedBufferedReader.read(char[],int,int)` | fresh unread production stream; char[8] เติม `~`, offset 1/2, positive bounded length | count, written characters, untouched sentinels, line counter และ last character |

Setup/constructor/dependency/projection failure เป็น fixture_error และไม่ถือเป็น target exception
หรือ skipped-success. ไม่เลือก target หรือแก้ assertions ตาม buggy outcomes.
เงื่อนไขข้างต้นเป็น bounded legal domain; ไม่รับ invalid index/null/overflow หรือ exhaustive coverage
โดยอาศัยผลชุดนี้.

## หลักฐานใหม่ที่ seal ก่อนรัน

เก็บ original packets/suites/results ทั้งหมด unchanged และตรวจ 193 checksums ซ้ำ.
ยืนยัน exact JVM descriptors/entry hits จาก XML ที่ออมส่ง.
**CMA-ES ชุดเดิมมี String append entry hits=0 และ FSCS-ART=2**.
การเพิ่ม independent reference proof ไม่เปลี่ยนค่า coverage หรือ label ของอัลกอริทึมเดิม.

สร้าง [preexecution seal](../../output/api854-20261003/beam-buffer-joint-review-v1/reference/preexecution-seal.json)
ที่ผูก expected cases, unchanged JUnit suite archives, current runtime 41 hashes และ fixed sources
ก่อน fixed observations/Defects4J execution. ใช้ policy `beam-explicit-fixtures-v6-buffer-proposal`
บน runtime บีม `24a38184`; นี่เป็น reference condition ใหม่ที่แยกจาก historical `c125695a`.

| Reference suite | Tests | Fixed observations | ทุก stage executed/skipped/target_checks | Exact methods covered | Fault detected |
|---|---:|---:|---|---:|---|
| JacksonCore-1 | 28 | 56 | 28/0/28 | Buffer 7 signatures | false |
| Csv-1 | 14 | 28 | 14/0/14 | overload ใหม่ + 5 selected methods เดิม | false |

รวม **42 reference tests / 84 fixed observations**:
Buffer 8 × 4 examples = 32; Csv existing methods 5 × 2 streams = 10.
Fixed pairs ตรง independent expectations ทุกกรณี; วัด unchanged suites fixed สองรอบ/buggy/coverage
พร้อม exact target-method coverage. สรุปใน
[reference receipt](../../output/api854-20261003/beam-buffer-joint-review-v1/reference/receipt.json)
และ [checksums](../../output/api854-20261003/beam-buffer-joint-review-v1/reference/checksums.json).

รันจริงบน Beam WSL, Java 11/Defects4J 3.0.1 โดยถือ one-CPU lock root เดิม
`/home/team/sqa-round2/beam-buffer-worktrees`.
Focused buffer policy 2 tests + real Java probe integration 9 tests ผ่าน 11 ไม่มี skip
ที่ current runtime พร้อม [log/source hashes](../../output/api854-20261003/beam-buffer-joint-review-v1/focused-checksums.json).
การรันเหล่านี้ไม่ใช่ primary results หรือการพิสูจน์ full input domain.

## Csv stream condition ที่บีมเสนอรับ

สร้าง StringReader ใหม่ต่อ target/case:

- `vector[0] < 0`: `A\nBC\nDE`
- `vector[0] >= 0`: `12\n345\n`

condition นี้กระทบ `getLineNumber()`, `lookAhead()`, `read()`, `readAgain()`, `readLine()`
ที่ selected อยู่แล้ว พร้อม overload `read(char[],int,int)` ใหม่.
สิบ reference examples ของห้า methods เดิมทดสอบสอง streams และเก็บ v5 observations แยกไว้
ใน [Csv observations](../../output/api854-20261003/beam-buffer-joint-review-v1/reference/Csv/observations.json).
Fresh reader มี line=0/last=-2; lookAhead ไม่ consume, read เปลี่ยน last,
readLine คืน first line พร้อม line=1/last เป็นตัวท้ายของข้อความจริง.
Buffer read เพิ่ม oracle ของ output buffer และพื้นที่ sentinel ที่ไม่ถูกเขียน.

**บีมรับการเปลี่ยนนี้เฉพาะ condition ใหม่ที่ทั้ง 4 approaches ใช้เหมือนกัน**.
คง v5 และผลย้อนหลัง unchanged; ออมต้องสร้าง recipes/context/prompt/metadata ใหม่ร่วมกับ
setter/JDOM/Math ที่รับไว้ แล้วส่ง prompts ใหม่ให้แชมป์วัด reserve.
ยังไม่มี agreed_condition เพราะแชมป์ต้องยืนยัน verdict ของ Csv stream change โดยตรง.

## ส่งต่อออมและแชมป์

แชมป์ตรวจ `beam-buffer-verdict.json` แล้วส่ง scoped verdict ต่อ 8 exact signatures
และ `csv_stream_condition_change` พร้อม receipt/hash. หากมี precondition/oracle ที่ไม่รับ
ให้ระบุเป็นรายรายการโดยไม่แก้ historical evidence.

ออมรอ verdict ร่วมก่อนประกอบ: ถ้ารับครบ 8 และไม่มี delta อื่น
union ของ shared v9 380 + Buffer 8 จะเป็น **388/691 และ 303 exclusions**.
ตัวเลขนี้ยังเป็น proposed union; ไม่รวม Lang helpers อีก 2 รายการของ Beam-only profile.
ยังไม่สร้าง combined preparation/protocol หรือปิด final reserve ในงานบีมนี้.
คง empty-enum 4 exclusions, Gate A/pilot ปิด, primary results 0,
KKU requests 0 และ queue mutations 0.
