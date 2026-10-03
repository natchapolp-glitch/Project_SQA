# Champ — bounded Graphics2D candidate สำหรับ Chart-1

ทำงานแยกระหว่างรอ Aom final preparation จาก Champ checkpoint `7de14726`.
Candidate มี **7 exact inherited declarations / 24 cases**; current shared v9 คง **380/311/691**.
ยังไม่เพิ่ม signatures เข้า selected, ไม่เปลี่ยน shared runtime/preparation และไม่เรียก KKU.

เริ่มที่ [sealed receipt v3](../../output/api854-20261003/graphics-development-v3/receipt.json),
[policy](../../output/api854-20261003/graphics-development-v3/policy.json),
[checksums](../../output/api854-20261003/graphics-development-v3/checksums.json),
[continuation/pins](../../output/api854-20261003/champ-graphics-continuation-v1.json) และ
[joint review template](../../output/api854-20261003/champ-graphics-joint-review.template.json).

| Artifact | SHA-256 |
|---|---|
| v3 receipt | `c8533f8185479b556bb233be1b0d602647279687e2e30e08dbba0a94c8a19232` |
| v3 policy | `cd9220e00eebc8cdef1b3865f4eb9cc7173152d5d07712e12866d34843179f3a` |
| v3 checksums | `6a58dca16961433b6de67cf6aad7a1e3a86fc15ed7493793a424065e1a8f15c3` |
| joint review template | `cb3a9d26711d73fe38a759efe31422132636e9be4869827256dbc2486cd9933e` |

## Exact targets และ oracle

Worklist receiver identity ทุก target คือ default production
`org.jfree.chart.renderer.category.AreaRenderer`, constructor types ว่าง.
Declaring class จริงทั้งเจ็ดคือ `org.jfree.chart.renderer.category.AbstractCategoryItemRenderer`.
Method lookup/JDI ต้องตรง owner/descriptor นี้; receiver เป็น AreaRenderer จริง.
Full parameter signatures/JVM descriptors อยู่ใน policy/template ที่ลิงก์ไว้.

| Method | Cases | Preconditions/oracle |
|---|---:|---|
| drawAnnotations | 4 | Real CategoryLineAnnotation foreground/background, real Layer, both orientations; full image for analytic category/range endpoints and red/green layer; null rendering info allowed |
| drawBackground | 2 | Solid yellow alpha1, no background image; exact yellow pixels in [10,50)×[10,50), outside white; paint/composite state |
| drawDomainLine | 4 | Coordinate24/red/BasicStroke2, both orientations; full raster/state. Null Paint/Stroke separately reject with IllegalArgumentException and exact `Null 'paint' argument.` / `Null 'stroke' argument.`; no image/state change |
| drawDomainMarker | 5 | Real category A, line/band, both orientations, alpha1/no label; A middle20 or band[10,30). Missing key is explicit no-op; full pixels/paint/stroke/composite |
| drawOutline | 2 | Real blue BasicStroke2 outline on (10,10,40,40); visible draws exact outline, disabled keeps image/state unchanged |
| drawRangeMarker | 5 | Real ValueMarker2/IntervalMarker2..8, both orientations; exact line/band pixels/state, alpha1/no labels/no interval outlines; outside20 is explicit no-op |
| initialise | 2 | Start bound to distinct real non-null plot, supplied dataset real2×2 or legal null, info null. Check rebind to supplied plot, counts2/2 or0/0, concrete return state/info/bar-width/selection identity, unchanged graphics/dataset |

Graphics fixture: fresh production BufferedImage64×64 TYPE_INT_RGB, white pixels,
antialias off, stroke normalize, identity transform/null clip; initial black/BasicStroke1/SRC_OVER alpha0.5.
Real production plot/dataset/axes: categories A/B, zero margins, rows containing 2/8/4/6,
non-inverted NumberAxis range[0,10], data area(10,10,40,40), both orientations.
Vertical range maps `y=50-4v`, horizontal `x=10+4v`, category middles20/40.
Every actual/reference graphics context is disposed.

Reference image is constructed **before target invocation** from primitive AWT drawing
and analytic coordinates; no production axis conversion or target-pixel-derived expectation.
Assertions compare full ARGB bytes/hash plus concrete graphics/receiver/dataset/return/exception state.
Python rederives state and full solid-region pixels independently.
Raw `.actual.argb` / `.reference.argb` files store4096 big-endian signed ARGB ints per case/stage.
Scope is the measured native Java/AWT host; receiving host must rerun before claiming raster equivalence.
Fonts, gradients, background images, arbitrary Paint/Stroke, transforms/clips and general UI quality are outside scope.

## Execution และ verification

- Local original Defects4J Chart SVN: fixed **r2266**, buggy **r2264**. JFreeChart Git mirrorไม่ใช่ revisionคู่นี้.
- Fixed AbstractCategoryItemRenderer/AreaRenderer normalized mirror EOL comparisonตรง retained v9;
  compile exact retained bytesทั้งสองไฟล์. Archive original sources/dependency jarsไว้.
- Source pins **654 Java filesต่อ revision**; iText/junit/servlet dependenciesจาก SVN.
  Source/suite/dependency/shared-input hashes sealก่อน compilation/observations.
- Shared checkpoint100 + Graphics raw cases7 = **107 pins**คงเดิม; runtime **41 files**ไม่เปลี่ยน.
- Fixedสองรอบ: **24 executed /24 target checks /24 passed /0 failed /0 skipped /0 fixture errors**ต่อรอบ.
- Buggyสองรอบ: **24/24 passed**เช่นกัน; **candidate_fault_detected=false**ตามจริง.
- JDIบน original production bytecodesทั้งสอง revisions: exact first entry **24 cases /7 declarations**;
  observationsตรง plain executions และ production class hashesไม่เปลี่ยนจาก tracing.
  เป็น method-entry evidence ไม่ใช่ line/branch percentage หรือ full Defects4J evaluation.
- Temporary fixed mutationเลื่อนเฉพาะ vertical drawDomainLine +4 pixels;
  AssertionErrorเฉพาะ **domain_line_v**พิสูจน์ oracle sensitivity แยกจาก production fault detection.
- Focused tests **39ผ่าน ไม่มี skip** (เดิม31 + Graphics8): raw pixels/repeats/source/dependencies/runtime,
  inherited identity/descriptor, fixture/skips, coherent wrong expected+actual state/pixels และ sealed-output guard.

[Test receipt](../../output/api854-20261003/champ-graphics-focused-tests-v1.json),
[raw test log](../../output/api854-20261003/champ-graphics-focused-tests-v1.log).
ไม่มี Defects4J evaluation command, KKU request, live queue mutation, primary addition หรือ final reserveในรอบนี้.

## Failed attempts ที่เก็บไว้

- [v1](../../output/api854-20261003/graphics-development-v1/failure.json): 22 cases passed;
  initialise setup2กรณีเรียก setPlot(null) ซึ่ง productionห้าม จึงเป็น fixture_error.
- [v2](../../output/api854-20261003/graphics-development-v2/failure.json): Java fixedผ่านครบ24;
  Python checkerจัด legal null datasetเป็น null-paint/stroke boundaryผิดพลาด.
- v3ใช้ legal distinct non-null plot setupจาก v2 และจัด boundaryเฉพาะ drawDomainLine.
  เก็บ suite/source/raw logs/checksumsเดิมครบ; ไม่บวก attemptsเป็น independent casesเพิ่ม.

## คำรับและ shared integration

Champรับ bounded candidate oracle development; Beam verdictยัง null/pending.
ทั้ง7ยังunsupported. ถ้ารับและ integrate Graphicsเพียงกลุ่มนี้บน base380 จะเสนอ387/304/691;
ยังไม่รวม delta groupsของ final conditionที่ออมกำลังทำและไม่เปลี่ยนจำนวนจริง.

Generic Chart fixtureเดิมใน SqaProbeใช้16×16 setup graphics, หนึ่ง category/สอง rows values(-2หรือ2)/5
และเรียก initialiseระหว่าง setup. Candidateใหม่ใช้2×2 dataset/axes/graphics domainต่างกัน;
**initialise targetต้องไม่มี initialise setup call**.
ทีมต้องเลือก per-target recipesหรือรับ explicit new Chart condition change,
ตรวจ existing selected Chart8 recipes และให้ทั้งสี่ approachesได้ source/context/knowledgeเดียวกัน.
Review fixture_error separation, full value/state oracle และ host bindingก่อนย้าย targetsเข้า selected.
งาน finalชุดเดิมของออมเดินต่อได้; Graphicsพร้อม reviewสำหรับ conditionที่ทีมเลือก.
Gate A/pilotปิด, final reserve=null; new conditionต้องมี preparation/prompts/reserveของรุ่นเดียวกัน.

## ข้อความส่งต่อ

**บีม:** Champมี graphics-development-v3: 7 inherited signatures/24 cases,
fixed/buggyซ้ำผ่าน/JDI owner-descriptorครบ, shifted-line mutationถูกจับ, fault=false, focused39testsผ่าน.
อ่านเอกสารนี้และ joint template แล้วขอ scoped verdictต่อ default AreaRenderer,
real headless graphics/analytic pixel-state oracle, null-dataset vs null-paint-stroke domains
และ new Chart fixture knowledge. รัน native hostบีมก่อนอ้าง raster equivalence.

**ออม:** มี standalone Graphics2D packetพร้อม peer review; ยังไม่มี joint/shared approval.
เก็บ candidateสำหรับ conditionที่ทีมเลือก และเดิน finalชุดเดิมต่อได้.
หลังคำรับร่วมให้ bind/test per-target lifecycle/oracleและตรวจ Chart8เดิมก่อนเลือก7เพิ่ม;
รักษา accepted recipes และสร้าง preparation/prompts/reserveของ conditionใหม่.

## ตรวจซ้ำ

```powershell
python -B -m unittest -v scripts.study.api854.tests.test_graphics_development
python -B -m scripts.study.api854.verify_graphics_development --defects4j D:/Projects/Project_SQA/defects4j --output output/api854-20261003/graphics-development-NEW
```

Windows hostใช้ Java17/WSL Ubuntuพร้อม svn และ local original SVN mirror.
เลือก outputใหม่เสมอ; preserve failed packets/raw bytes และ credentials/PDFของผู้ใช้.
