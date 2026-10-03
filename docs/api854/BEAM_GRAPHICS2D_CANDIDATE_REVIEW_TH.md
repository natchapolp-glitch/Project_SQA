# บีมรับ Graphics2D candidate ของ Champ 8d9295e6

**รับ bounded standalone candidate ทั้ง 7 signatures / 24 cases** พร้อม native Java/AWT host proof.
เป็นคำรับร่วมกับ Champ สำหรับ oracle development; shared integration, full legal domain และ Gate A ยัง pending.

เริ่มที่ [receipt.json](../../output/api854-20261003/beam-graphics-review-v1/receipt.json),
[joint-verdict.json](../../output/api854-20261003/beam-graphics-review-v1/joint-verdict.json),
[integration-requirements.json](../../output/api854-20261003/beam-graphics-review-v1/integration-requirements.json)
และ [checksums.json](../../output/api854-20261003/beam-graphics-review-v1/checksums.json).
Joint verdict มี exact signatures/descriptors, agreed preconditions/oracles และ evidence paths/SHA-256.

## ตรวจจริงบนบีม

- รับ immutable Champ `8d9295e6c238ce6e2f9c2014932207ef2f036663` ใน snapshot แยก;
  received blobs และ source/suite/dependency/runtime pins ตรวจตรง Git/sealed manifests.
- Fresh focused tests **8 ผ่าน / skip 0** บน exact test source ของ Champ:
  raw raster/state/repeats, sources/dependencies, inherited receiver/descriptor, fixture/skips,
  coherent wrong paint/background และ sealed-output guard. ไม่บวก 39 tests ที่ Champ รายงานเป็น tests ใหม่ของบีม.
- รัน actual production Java บน **WSL/Linux / Java 11 / beam-pc1 / CPU 1 slot**.
  CPU lock เดียวกับ workers บีม: held slot ปฏิเสธ exit 9; ปล่อยแล้ว reuse exit 0.
  Chart-1 original SVN fixed **r2266**, buggy **r2264**, source inventory654 Java filesต่อ revision.
  Production sources/dependencies ตรง Champ; fixed target/receiver retained bytesตรง source pins.
- Fixed **24 executed / 24 target checks / 24 passed / 0 failed / 0 skipped / 0 fixture errors**
  ทั้งสองรอบ; buggy countersเดียวกันทั้งสองรอบ. เป็น24 unique cases,
  **48 fixed observations / 48 buggy observations** ไม่บวก tracingเป็น casesใหม่.
- JDI exact method-entry24 cases/7 declarationsทั้งสอง revisions;
  traced observationsตรง plain executions และ production class bytesไม่เปลี่ยน.
  เป็น method-entry evidence ไม่ใช่ line/branch coverage percentageหรือ full Defects4J suites.
- Full ARGB bytesและ graphics/receiver/dataset/return/exception stateตรง Champทุก stage.
  รับ raster equivalenceเฉพาะ domainและ native hostsที่วัดจริง.
- Temporary vertical drawDomainLine +4 pixelsถูก oracleจับเฉพาะ domain_line_v.
  **candidate_fault_detected=false**: buggy productionผ่านทุก case;
  mutation sensitivityไม่ใช่การพบบั๊ก Chart-1.

[Native host receipt](../../output/api854-20261003/beam-graphics-review-v1/host-review-v1/host-receipt.json)
และ [native evidence](../../output/api854-20261003/beam-graphics-review-v1/host-review-v1/native-evidence/receipt.json)
มี source archives/jars, preexecution seal, commands/raw logs/pixels และ suite hashes.
ใช้ exact canonical verifier/assertionsของ Champ; adapterเปลี่ยนเฉพาะ Windows WSL launcher/path
เป็น native Linux path/command. Adapter hash sealก่อน run ไม่มีการลด assertions.

## ขอบเขต semantic ที่รับ

Receiverทุก targetเป็น default production AreaRenderer(); exact declaring classเป็น
AbstractCategoryItemRenderer. ไม่แทน receiverด้วย declaring class.
Graphics64×64 TYPE_INT_RGB/white, antialias off/stroke normalize, identity transform/null clip;
real2×2 dataset A/B, real axes/data area(10,10,40,40), range[0,10], zero category margins.
Reference pixelsสร้างจาก primitive AWT/analytic coordinates **ก่อน target invocation**;
Pythonตรวจ full solid-region pixelsและ stateอย่างอิสระ ทุก graphics context dispose.

| Method | Cases | คำรับที่จำกัด |
|---|---:|---|
| drawAnnotations | 4 | Foreground/background CategoryLineAnnotation, both orientations, real Layer, analytic endpoints/full pixels/state |
| drawBackground | 2 | Solid yellow alpha1/no image, exact [10,50)×[10,50) pixels/composite restoration |
| drawDomainLine | 4 | Coordinate24/red/BasicStroke2/both orientations; null Paint/Strokeแยก boundary พร้อม exact IllegalArgumentException messagesและ unchanged image/state |
| drawDomainMarker | 5 | Category A line/band/both orientations, alpha1/no label; missing keyเป็น no-op |
| drawOutline | 2 | Blue BasicStroke2 rectangleและ disabled no-op พร้อม state |
| drawRangeMarker | 5 | Value2/Interval2..8/both orientations, alpha1/no labels/no interval outlines; outside20เป็น no-op |
| initialise | 2 | Distinct real non-null prior plot, target rebindจริง; real2×2หรือ legal null method dataset, info null; row/column counts, concrete return/selection identity, unchanged pixels/state |

initialise setupไม่เรียก initialiseล่วงหน้า. Legal null datasetไม่ใช่ null Paint/Stroke boundary.
ไม่รับรอง arbitrary fonts/gradients/background images/Paint/Stroke/transforms/clipsหรือ general UI quality.
Setup/projection failureต้องคงเป็น fixture_error ไม่ใช่ target fault.

## Shared condition ยังต้อง integrate

Actual shared conditionที่บีมรับก่อนหน้านี้คือ Aom `a4880fb2` v10:
**390 selected / 301 exclusions / 691**, Chartเดิม selected8;
Graphics7นี้ยังอยู่ใน exclusionsครบ. Native candidate verifierใช้ pinned Champ v9
380/311เป็นต้นทาง candidate ไม่ใช่ preparation v10หรือ shared-helper integrationใหม่.

ทั้งสองฝ่ายรับ bounded candidateแล้ว แต่ยังต้องให้ออมเลือก per-target lifecycle
หรือ explicit new Chart condition change, integrate shared invocation/full pixel-state projection,
regress Chart8เดิม และให้ทั้งสี่ approachesได้ sources/context/fixtures/oracle knowledgeเดียวกัน.
initialiseต้องแยกจาก generic Chart setupที่เคยเรียก initialise.
หาก integrate **Graphics7กลุ่มนี้เท่านั้นบน v10** จะเสนอ **397/294/691**;
ยังไม่ใช่ selectedจริง และไม่รวม Chronology/enumหรือกลุ่มอื่นโดยอัตโนมัติ.
จากนั้นสร้าง preparation/index/protocol/runner/runtime/prompts และวัด reserveของ conditionใหม่.

Original Beam runtime41 filesและหลักฐาน/coverageเดิมคง exact bytes.
ไม่เปลี่ยน fault labelsหรือ CMA-ES String append entry gapเดิม.
Gate A=false, reserve=null, KKU requests=0, live queue mutations=0, primary additions=0.

## Attempts ที่เก็บไว้

Champ graphics-development-v1/v2ที่ล้มเหลวและ v3 activeเก็บ exact bytesทั้งหมด.
บีม received-testsครั้งแรก7ผ่าน/1error เพราะ intakeขาด raw-case pins7ไฟล์;
เติมจาก exact Champ Git blobsพร้อม supplement provenance แล้ว received-tests-v2ผ่าน8.
ไม่เปลี่ยน suite/oracle/policy และไม่เขียนทับ failed logs. Native host-review-v1ผ่านเป็น active proof.

## ส่งต่อแชมป์

บีมรับ bounded Graphics2D7 signatures/24 casesของ Champ8d9295e6แล้วครับ.
Fresh tests8ผ่าน ไม่มีskip; Java11 native fixed/buggyอย่างละสองรอบผ่าน,
JDI exact7ครบ และ full pixels/stateตรง Champทุกstage; shifted-line mutationถูกจับ, fault=false.
Joint verdict/receiptพร้อม hashesอยู่ beam-graphics-review-v1.
ยังเป็น standalone candidate ไม่มี shared integration/Gate A; งาน reserveของ v10เดิมเดินต่อได้ครับ.

## ส่งต่อออม

บีมกับแชมป์รับ bounded Graphics2D candidate7แล้วครับ อ่าน joint-verdict.jsonและ integration-requirements.json.
ต้อง integrate per-target lifecycle/oracleและ regress Chart8เดิมก่อนเพิ่ม selected;
initialise setupห้าม invoke targetล่วงหน้า และทั้งสี่ approachesต้องได้ knowledgeเดียวกัน.
v10 a4880fb2ยัง390/301; หากเพิ่มGraphicsกลุ่มนี้หลังintegrationผ่านจึงเสนอ397/294
พร้อม condition/preparation/prompts/reserveใหม่ครับ ไม่กระทบงาน final v10ที่กำลังตรวจ.

## ตรวจซ้ำโดยไม่เขียนหลักฐานเดิม

ใช้ restore_snapshot.pyเติม ignored source snapshotจาก pinned Git blobs แล้วเรียก
finalize_review.pyผ่าน runpy.run_path(...)["audit"]()สำหรับ read-only verification.
อย่ารัน producer mainหรือ native/test callersลง outputเดิม;
ถ้าจะ executeซ้ำ ให้เลือก attempt/outputใหม่และคง failed/active proofเดิมไว้.
