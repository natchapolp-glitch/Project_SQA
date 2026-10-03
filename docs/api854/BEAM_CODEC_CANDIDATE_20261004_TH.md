# บีมพัฒนา Codec candidate แยก — 4 ตุลาคม 2026

**บีมรับ bounded candidate 5 signatures / 43 cases** จาก Codec-1 exclusions ของ
exact Aom `a4880fb2fde574e77705841f62f302273be7dcd9`.
Fixed และ buggy ผ่านอย่างละสองรอบ พร้อม exact method-entry และ meaningful value/state oracles.
**ยังไม่พบ fault ของ Codec-1**, ยังรอ Champ scoped verdict และ shared integrationของออม.
งานนี้ทำได้เองระหว่างรอ provider/reserve/Gate A และไม่ใช้ KKU quota.

เริ่มที่ [receipt](../../output/api854-20261004/beam-codec-candidate-v1/receipt.json),
[sealed policy/cases](../../output/api854-20261004/beam-codec-candidate-v1/policy.json),
[joint review template](../../output/api854-20261004/beam-codec-candidate-v1/joint-review.template.json),
[native proof](../../output/api854-20261004/beam-codec-candidate-v1/native-v1/receipt.json)
และ [checksums](../../output/api854-20261004/beam-codec-candidate-v1/checksums.json).
Templateมี exact declaring classes/JVM descriptors, per-signature preconditions/oracles และ paths/SHA-256.

## Signatures และ scope

| Exact target | Cases | Domain/oracle ที่รับ |
|---|---:|---|
| Metaphone().isNextChar(StringBuffer,int,char) | 8 | Fresh real non-null buffer ABCA/empty/A, bounded indices/char; adjacent-next Booleanและ false guard boundaries |
| Metaphone().isPreviousChar(StringBuffer,int,char) | 8 | Fresh real non-null buffer ABCA/empty/A, bounded indices/char; adjacent-previous Booleanและ false guard boundaries |
| Metaphone().isVowel(StringBuffer,int) | 9 | AEIOUB: vowelsจริง5/consonant1; empty/negative/at-lengthแยก exact StringIndexOutOfBoundsException boundary |
| Metaphone().regionMatch(StringBuffer,int,String) | 9 | Bounded ABCA/emptyและ non-null needle: exact match/mismatch, too-long/negative, empty needleที่ end/empty bufferและ beyond-end |
| SoundexUtils().difference(StringEncoder,String,String) | 9 | Static package helperบน production class identity; real fresh default Metaphone encoder/maxCodeLen4; inputs null/empty/A/a/E/B/AB เท่านั้น |

สี่ Metaphone methodsเป็น private instance declarations; differenceเป็น package-local static declaration.
ใช้ reflectionเพื่อเข้าถึง exact declarations และ real default production receivers;
ไม่มี fake StringBuffer, fake encoder หรือ receiver subclass.

Oracleยึด predeclared Boolean/integer/exception valuesก่อน execute.
StringBufferตรวจ contents/length/capacity ก่อนและหลังทุก target;
Metaphone receiverและ encoderตรวจ maxCodeLen4คงเดิม.
differenceใช้ literal encoded-reference tableที่ sealไว้ก่อนรัน แล้วนับตำแหน่งตรงกันอย่างอิสระ;
ไม่เรียก difference/differenceEncodedเพื่อคำนวณ expected result.
Real encoder outputsถูกตรวจเทียบ literal referencesใน fixture preflight ก่อน target invocation.
ทุก fixture/target invocationและ observed return/stateมี raw JSON logs.

ไม่รับรอง public callers, arbitrary encoders, null buffer/needle/encoder,
non-ASCII/overflow indices/arbitrary maxCodeLen หรือ EncoderException propagation.
ข้อยกเว้น invalid isVowel indicesที่ประกาศไว้เป็น target boundary assertions ไม่ใช่ fixture failures.

## หลักฐานรันจริง

- ใช้ local Defects4J Codec-1 underlying production Git revisions:
  fixed `52d82d1dfff8c2b2ded9d843e0b03017af6d747c`,
  buggy `9c0cabead7cf075308b11362172ae1a48d41321c`.
  Export src/javaทั้งสอง revisions; fixed Metaphone/SoundexUtils normalized EOLตรง retained Aom v10
  แล้ว compile exact retained bytes. Sources/interfacesจริง ไม่มี dependency downloads.
- Preexecution sealมี suite/policy/cases.tsv/source inventories/archive hashes/runtime/input provenance.
  เลือกทั้ง5 exclusionsและ43 casesก่อนดู buggy outcomes ไม่มี outcome-based selection.
- Linux/WSL Java/Javac11, compile production/probeด้วย --release8;
  **beam-pc1 / CPU1 slot** และ shared CPU lockเดิม.
  Held slot challenge exit9, ปล่อยแล้ว reuse exit0.
- Fixed first/second และ buggy first/secondแต่ละ stage:
  **executed43 / target_checks43 / passed43 / failed0 / skipped0 / fixture_errors0**.
  เป็น43 unique cases, **86 fixed observations +86 buggy observations**.
- JDI exact first method-entryครบ43 cases/5 signaturesทั้งสอง revisions.
  Trace observationsตรง plain executions และ original production class hashesไม่เปลี่ยนจาก tracing.
  Method-entryเท่านั้น ไม่ใช่ line/branch percentageหรือ full Defects4J suite coverage.
- Separate mutationควบคุม: isNextCharกลับ == เป็น != ถูกจับ3cases;
  differenceคะแนน+1ถูกจับ9cases. Mutated sources/logsเก็บแยกและ sourceถูก restore.
  ทั้งสอง mutationไม่ใช่ production bug detection และไม่บวกเป็น experiment casesใหม่.
- Fresh evidence guard tests **7ผ่าน / skip0**:
  source/seal integrity, repeats/entries, actual mutations, coherent wrong Boolean/score,
  receiver/descriptor, fixture/skips และ coherent buffer/encoder-state drift.
  [Tests/raw command/log hashes](../../output/api854-20261004/beam-codec-candidate-v1/focused-tests.command.json).

Native proofเป็น standalone compiled production helper development.
ไม่มี full Defects4J evaluation, CMA-ES/FSCS-ART generation หรือ AI experimentใหม่ใน packetนี้.
Original shared runtime41ไฟล์และ historical suites/coverage/fault labelsคง exact bytes.

## Shared integration และจำนวนที่ยังไม่เปลี่ยน

Current v10ยัง **390 selected /301 exclusions /691**, Codec selected13/excluded5.
Policy/sealตอนก่อนexecuteเก็บ verdictเป็น pendingไว้ตามประวัติ;
คำรับบีมหลังตรวจอยู่ receiptและ joint-review.template.json ไม่แก้ sealed policyย้อนหลัง.
Champ verdict=null, joint_acceptance_complete=false, shared_integration_approved=false.

หากทีมรับ5นี้และ integrate **Codecกลุ่มนี้เท่านั้นบน v10** จะเสนอ **395/296/691**.
ยังไม่ใช่ selectedจริง และไม่รวม Graphics/Chronologyโดยอัตโนมัติ.
ต้อง bind bounded StringBuffer/real StringEncoder lifecycleและ meaningful scalar/exception/state projections
ใน shared helper, ตรวจ Codec13เดิมรวม setter/getter, รักษา setter/JDOM/Math/Buffer/Lang,
และให้ทั้งสี่ approachesได้ source/context/fixture/oracle knowledgeเดียวกัน.
แล้วจึงสร้าง condition/preparation/protocol/runner/runtime/promptsใหม่และวัด reserveของรุ่นเดียวกัน.

Gate A=false, final reserve=null, KKU requests=0, queue mutations=0, primary additions=0.
ไม่มี live generation/pilot หรือ account/credential changes.

## ฝากแชมป์

บีมทำ Codec candidate5 signatures/43 casesเสร็จแล้วครับ.
Fixed/buggyอย่างละสองรอบผ่าน, JDI exact5ครบ, Boolean/score mutationถูกจับ,
buffer/maxCodeLen stateคงเดิม และ fresh tests7ผ่าน ไม่มีskip; candidate fault=false.
ขอ scoped review/private StringBuffer helpersกับ real Metaphone StringEncoderตาม template
พร้อม verdict/preconditions/oracle/evidence hashesครับ ยังไม่รวมเข้า v10และไม่กระทบ reserveชุดเดิม.

## ฝากออม

บีมส่ง Codec standalone packetแยก5 signaturesแล้วครับ v10ยัง390/301.
รอ Champ scoped verdictก่อนเลือก integrate bounded StringBuffer/encoder fixturesและ oracle;
หากเลือกต้องตรวจ Codec13เดิม/รักษา accepted recipesและใช้ condition/preparation/promptsใหม่.
Possible Codec-only union395/296เป็น proposalเท่านั้น ไม่เปิด Gate Aหรือ generationครับ.

## ตรวจซ้ำ

รัน test_evidence.pyได้โดยไม่แก้ sealed native proof.
finalize_packet.pyให้เรียก audit()แบบ read-only; producer main/verify_native.pyห้ามเขียนลง packetเดิม.
หาก executeใหม่ ให้ใช้ attempt/outputใหม่ และ seal suite/policyก่อนexecuteทุกครั้ง.
