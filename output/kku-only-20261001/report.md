# การสร้างชุดทดสอบ Java: ผลรอบที่ 2

กลุ่ม 14 / CP353201 Software Quality Assurance

CMA-ES และ FSCS-ART เปรียบเทียบกับ Claude และ Gemini ผ่าน KKU IntelSphere

ข้อมูล ณ 2026-10-01T12:13:51.773790+00:00 UTC

สถานะ: สำเร็จ 140/204 รอบตามแผนทีม งานทดลองยังไม่ครบ และยังไม่ใช่หลักฐานการส่ง Classroom

## ขอบเขตและการเปลี่ยนแผน

อ้างอิงเกณฑ์ SQA_Project_2026.pdf สำหรับการส่งรอบ 2: ใช้สองอัลกอริทึมและสองเครื่องมือ AI สร้าง unit tests ของ Java projects ใน Defects4J เปรียบเทียบ coverage, fault detection และ efficiency พร้อมโค้ด หลักฐาน รายงาน การนำเสนอและ demo

เจ้าของงานกำหนดให้ส่ง prompt ผ่าน https://gen.ai.kku.ac.th/chat เท่านั้น และเลือกเฉพาะ Claude กับ Gemini จึงใช้กลุ่มเปรียบเทียบใหม่ CMA-ES, FSCS-ART, KKU Claude และ KKU Gemini ผล Claude โดยตรงและโมเดลตระกูลอื่นในชุดเดิมไม่รวมในผลหลัก ไม่อ้างว่าผู้สอนอนุมัติการเปลี่ยนเครื่องมือจากแผนรอบแรก

17 projects × 1 active bug/project × 3 run indices (101/102/103) × 4 approaches = 204 รอบ งบสูงสุด 30 วิธีทดสอบต่อชุด AI ส่วนอัลกอริทึมเสนอ 30 input vectors จำนวนนี้เป็นแผนทีม ไม่ใช่จำนวนที่โจทย์ระบุโดยตรง หนึ่ง bug ต่อ project ไม่ครอบคลุมทุก bug ในฐานข้อมูล

โปรเจกต์: Chart, Cli, Closure, Codec, Collections, Compress, Csv, Gson, JacksonCore, JacksonDatabind, JacksonXml, Jsoup, JxPath, Lang, Math, Mockito, Time

## สถานะและจำนวนชุดทดสอบ

| วิธี | สำเร็จ/51 | โปรเจกต์/17 | Test methods | ใช้เดิม | ทำใหม่ | ขาด | ยังไม่สำเร็จ |
|---|---|---|---|---|---|---|---|
| CMA-ES | 51/51 | 17/17 | 1,529 | 51 | 0 | 0 | 0 |
| FSCS-ART | 51/51 | 17/17 | 1,529 | 51 | 0 | 0 | 0 |
| KKU Claude | 7/51 | 7/17 | 165 | 3 | 4 | 35 | 9 |
| KKU Gemini | 31/51 | 17/17 | 595 | 13 | 18 | 17 | 3 |

จำนวน methods เป็นผลรวม declared methods ใน completed suites อาจมีสถานการณ์ซ้ำข้ามรอบ ไม่ใช่ unique scenarios และไม่คูณจำนวนการรัน fixed/buggy ผล failed/missing มีค่าการวัดที่ไม่มีเป็น null ไม่แทนด้วยศูนย์

มี fresh completed records ที่ซ้ำ identity กับผลเดิม 1 รายการ: Time/Gemini/102 เก็บผลใหม่เป็น secondary validation และคงผลเดิมเป็น primary ตามลำดับที่มีหลักฐานก่อน โดยไม่ใช้ fault/coverage เลือกผล จึงไม่เพิ่มจำนวนรอบหรือ methods ในตาราง primary ตรวจผลที่สำเร็จด้วย audit แยกตาม family รวม secondary โดยอ่านจำนวนล่าสุดจาก claude/gemini-evidence-audit.json

ผลเดิมผ่าน baseline audit 171/171 records การนำมาใช้ในชุดนี้อ้าง record path และ SHA-256 ไม่แก้ชื่อโมเดลหรือ generator เดิม เลือกเฉพาะผลอัลกอริทึมและคำตอบ KKU ที่ระบุ resolved model เป็น Claude/Gemini และ metadata ตรงกัน ผล KKU เดิมบางรายการมาจาก Auto Router ซึ่งแยกจากคำตอบใหม่ที่เลือก agent โดยตรง

## วิธีสร้างอินพุตและ oracle

FSCS-ART เลือก candidate ที่มีระยะห่างต่ำสุดจากอินพุตก่อนหน้ามากที่สุดใน normalized input space ทำให้ชุดอินพุตกระจายตัว CMA-ES ปรับ mean, covariance และ step size ของการค้นหาตัวแปรต่อเนื่อง ตัว implementation ในงานนี้ใช้ความถี่ของ fixed behavior เป็น objective เพื่อเพิ่มความหลากหลายของ output ไม่ใช้ coverage เป็น fitness

SqaProbe ใช้ signatures ที่มีทั้ง fixed และ buggy revisions รวมสมาชิก non-public ที่ reflection เข้าถึงได้ สร้าง scalar, enum, string, array, collection และ constructor fixture แบบจำกัด ทุก proposed input สังเกต fixed สองครั้ง เก็บเฉพาะผลคงที่และ input ไม่ซ้ำมาสร้าง assertions ผล opaque object ยืนยันได้เพียง runtime type ส่วน serialized output ยาวเกิน 16000 ตัวอักษรใช้ SHA-256 และ byte length ข้อจำกัดนี้ลดการทดสอบ object graphs และ stateful sequences

AI ใช้ prompt ต้นฉบับของแต่ละ project ใน ai-context/prompt.md ให้ fixed source/build/API context ส่งครั้งเดียวต่อ run index ไม่ส่ง compile/error/coverage/buggy logs กลับให้ AI โค้ดจากส่วน thinking ไม่ถือเป็น final source

เก็บ raw DOM-rendered response และ code blocks, model label จริง, เวลาเริ่มและเวลาสังเกตคำตอบ, URL และ screenshot ไม่อ้าง hidden temperature, token count หรือ resolved version ของ latest AI run indices เป็นหมายเลขรอบ ไม่ใช่ RNG seeds ของโมเดล

## ขั้นตอนประเมินและหลักฐาน

![Workflow](workflow.svg)

สร้าง external JUnit suite ตามรุ่นที่ project รองรับ ประเมิน fixed ครั้งที่ 1 และ fixed ครั้งที่ 2 แล้วรัน buggy และเก็บ Cobertura coverage บน fixed นิยาม fault detected คือ assertion ผ่าน fixed ซ้ำแต่ล้มเหลวบน buggy โดยแยก compiler, harness และ timeout errors ออก อ่าน triggering test ประกอบก่อนสรุป semantic defect

Coverage วัดเฉพาะ classes ที่แก้ไขเพื่อซ่อม bug ไม่ใช่ทุก class ใน project เก็บ covered/total counters, coverage.xml, command.json/log, failing_tests, suite archive และ processing snapshots ที่ตรวจ hash ได้

เครื่องใหม่ใช้ Ubuntu WSL, Java 11, Defects4J 3.0.1 commit 6d54320e0db5a357f9ab38a8e4d2e5aead7e1c09 ผล fixed source ของทั้ง 17 projects ตรวจ SHA ตรง ai-context เดิม ดู environment receipts สำหรับ versions/checkouts ผล baseline มาจากอีกเครื่อง จึงมีผลของ hardware, host load, caches และ build overhead ต่อเวลา

## RQ1: Coverage ของชุดที่สำเร็จ

![Coverage](coverage.svg)

Macro = ค่าเฉลี่ย covered/total ต่อ run; micro = ผลรวม covered / ผลรวม total ไม่ใช่ union coverage และนับโค้ดซ้ำข้ามรอบ ตัวหารและ successful subsets ต่างกัน จึงใช้ผลต่อไปนี้เชิงพรรณนา

| วิธี | n | Line macro | Condition macro | Line micro |
|---|---|---|---|---|
| CMA-ES | 51 | 26.6% | 15.1% | 21.3% |
| FSCS-ART | 51 | 26.4% | 15.6% | 21.6% |
| KKU Claude | 7 | 62.2% | 55.6% | 29.4% |
| KKU Gemini | 31 | 73.1% | 63.9% | 60.4% |

CMA-ES และ FSCS-ART มีครบ 51 รอบบน sample เดียวกัน Line macro ต่างกัน 0.22 percentage points ผลใกล้กันในค่าเฉลี่ย แต่ coverage ต่ำในบาง fixtures และแต่ละ project มีขนาด classes ต่างกัน การเทียบ AI ต้องดูตัวหารและกลุ่มตรงกันแยก เพราะ sample ที่สำเร็จมี composition ต่างจากอัลกอริทึม

## RQ2: การตรวจพบ sampled bugs

| วิธี | พบ/รอบที่วัดได้ | พบ/bugs ที่วัดได้ | อัตราระดับ bug |
|---|---|---|---|
| CMA-ES | 9/51 | 5/17 | 29.4% |
| FSCS-ART | 10/51 | 4/17 | 23.5% |
| KKU Claude | 4/7 | 4/7 | 57.1% |
| KKU Gemini | 14/31 | 9/17 | 52.9% |

CMA-ES พบ 5/17 sampled bugs และ FSCS-ART พบ 4/17 แม้ FSCS-ART มีรอบที่พบ fault มากกว่า (10 เทียบกับ 9) การนับระดับ run จึงตอบต่างจากระดับ distinct bug และไม่ควรเลือกวิธีจากตัวเลขใดตัวเลขหนึ่งโดยไม่ระบุหน่วย

ตรวจพบซ้ำหลาย methods/runs ของ project-bug เดียวกันนับเป็นหนึ่ง bug ในอัตราระดับ bug การไม่ตรวจพบ sampled bug ไม่ยืนยันว่า suite ไม่มีประโยชน์ หรือ project ไม่มีข้อผิดพลาด

## เปรียบเทียบสี่วิธีในกลุ่มตรงกัน

มี 6 project-bug-index groups ที่ครบทั้งสี่วิธี: Chart-1/s101, Codec-1/s102, Collections-1/s101, Csv-1/s101, Gson-1/s101, Jsoup-1/s101

| วิธี | n กลุ่มตรงกัน | Line macro | Condition macro | พบ/รอบ |
|---|---|---|---|
| CMA-ES | 6 | 29.2% | 17.2% | 0/6 |
| FSCS-ART | 6 | 30.6% | 19.1% | 0/6 |
| KKU Claude | 6 | 55.9% | 48.2% | 3/6 |
| KKU Gemini | 6 | 67.5% | 61.0% | 3/6 |

กลุ่มตรงกันลดความต่างของ project/index แต่จำนวนกลุ่มน้อยและเลือกจากชุดที่ผ่านทั้งหมด ยังมี selection bias, model/version และ processing effort ที่ต่างกัน ไม่ใช้เป็น causal ranking และไม่ทดสอบนัยสำคัญกับ sample นี้

## RQ3: Efficiency และความสำเร็จของ workflow

| วิธี | Compile ผ่าน/วัด | median evaluation (s) | median generation (s) |
|---|---|---|---|
| CMA-ES | 51/51 | 55.64 | 13.50 |
| FSCS-ART | 51/51 | 54.42 | 13.61 |
| KKU Claude | 7/7 | 33.44 | 133.18 |
| KKU Gemini | 31/31 | 36.25 | 109.89 |

Compile rate ใช้เฉพาะ run ที่มีผล compile ชัดเจน ไม่รวม provider failures ที่ไม่ได้คอมไพล์ และไม่รวม attempt ที่ถูกเก็บเป็น history ก่อนซ่อม ค่า generation ของ AI เป็น submit-to-observed-completion upper bound รวมการรอผู้ปฏิบัติงาน ค่าอัลกอริทึมเป็นเวลาเครื่องวัด จึงเปรียบเทียบความเร็วโดยตรงไม่ได้

evaluation รวม build/JVM/test/coverage overhead และได้ผลของ cache warming งบ 30 proposed inputs กับ 30 AI methods ไม่ใช่ effort ที่เท่ากัน token/cost/model compute ไม่ปรากฏใน UI จึงไม่สร้างตัวเลขขึ้นเอง local repair/pruning ต้องดู history เพิ่ม ไม่ใช่เพียง final-run เวลา

## การซ่อมและต้นทุนที่เปิดเผย

ใช้ source-order cap 30 methods, UI suffix cleanup และ fixed-only pruning สูงสุดสองรอบตาม processor ที่เก็บ hash ถ้าต้องเพิ่ม compatibility ใช้เวอร์ชันใหม่ เก็บ raw source และ failed attempts เดิม ห้ามแก้ expected values จาก buggy outcomes

เวอร์ชันใหม่ v30 กู้ complete prefix ของ Gemini Lang; v31 แก้ overload ambiguity ของ Time ด้วย (String)null โดยคง assertion; v32 กู้ complete prefix ของ Claude Jsoup; v33 กู้ Claude Cli; v34 กู้ Gemini Codec; v35 cast Collections iterator เป็น ResettableIterator; v36/v37 unbox Integer sibling index สองตำแหน่งของ Jsoup ตาม fixed compiler/API ใช้ source hashes ที่ระบุ ไม่อ้างว่าต้นฉบับ AI คอมไพล์ได้โดยไม่มี processing

เรียกผลที่ผ่านว่า AI-assisted with local processing ทุก attempt เก็บก่อน/หลัง policy SHA และ driver/source snapshots มี audit แบบเต็มเฉพาะ completed records และ audit provenance แยกตรวจ provider/failure/manifest ไม่มีการรับรองคะแนนจากผู้สอน

## โมเดลและที่มาของแต่ละกลุ่ม

| วิธี | label จริง | ที่มา | records |
|---|---|---|---|
| KKU Claude | claude-haiku-latest | new-kku-capture | 4 |
| KKU Claude | claude-sonnet-5 | reused-audited-baseline | 3 |
| KKU Claude | claude-sonnet-latest | new-kku-capture | 9 |
| KKU Gemini | gemini-3.6-flash | reused-audited-baseline | 6 |
| KKU Gemini | gemini-flash | new-kku-capture | 20 |
| KKU Gemini | gemini-pro | new-kku-capture | 1 |
| KKU Gemini | gemini-pro | reused-audited-baseline | 7 |

ตารางนับ records ที่มี label รวม failed records ส่วน metric tables นับเฉพาะ completed การรวมตระกูลโมเดลต่าง versions ใช้ตามข้อจำกัดบริการและแยกเปิดเผย ไม่อ้างว่าเป็นโมเดลเดียวกันหรือ independent deterministic repetitions

## อุปสรรคและบทเรียน

บัญชีเดิม: บริการ Claude ใน KKU แสดง daily usage 100% ระหว่างเก็บ Compress รอบ 101 และ Gemini แสดง 100% หลังเก็บ Jsoup รอบ 103 ไม่ปรากฏ reset time ที่ยืนยันได้ เก็บ quota status และ screenshots จริง ไม่สร้างผลทดแทนจาก AI อื่น Claude Lang/Time มี server busy และ Math ไม่มี final code ที่สังเกตได้ Mockito และ Gemini Cli ตัดก่อนมี test method สมบูรณ์ จึงบันทึก generation_failed

คำตอบถูกตัดกลาง Java, overloaded APIs และ fixture differences ทำให้ raw output ใช้ไม่ได้ทันที การตรวจ parser/fixed ซ้ำและการเก็บ version history ช่วยกู้ส่วนที่ใช้ได้โดยไม่ปิดบังปัญหา Coverage สูงยังไม่รับประกัน fault detection: อ่านตัวอย่าง Jsoup ใน demo-guide-kku-only.md และเปรียบเทียบ assertions กับ failing_tests

Operator incident: Math/102 exports ถูกบันทึกไป Lang/102 ชั่วคราวหลัง Lang ประเมินเสร็จ กู้ raw Lang response จาก evaluator snapshot และเรียกแชท Lang เดิมกลับมาได้ SHA ตรงทั้ง response และ Java source ภาพ/DOM exports เป็นการสังเกตใหม่ ไม่อ้างว่าเป็นภาพเดิม เก็บไฟล์ที่บันทึกผิดและ recovery receipt ใน results/validation/operator-capture-routing-20261001 Math ประเมินจาก request-start และ capture ที่แก้ routing ก่อนประเมิน

## ภาคผนวก: ผลต่อโปรเจกต์

ตารางแสดงจำนวน completed runs / 3 ต่อวิธี ให้ดู pending-runs.csv สำหรับ index และเหตุผลที่ยังขาด

| Project | CMA-ES | FSCS-ART | KKU Claude | KKU Gemini |
|---|---|---|---|---|
| Chart | 3/3 | 3/3 | 1/3 | 3/3 |
| Cli | 3/3 | 3/3 | 1/3 | 1/3 |
| Closure | 3/3 | 3/3 | 0/3 | 1/3 |
| Codec | 3/3 | 3/3 | 1/3 | 2/3 |
| Collections | 3/3 | 3/3 | 1/3 | 2/3 |
| Compress | 3/3 | 3/3 | 0/3 | 1/3 |
| Csv | 3/3 | 3/3 | 1/3 | 3/3 |
| Gson | 3/3 | 3/3 | 1/3 | 1/3 |
| JacksonCore | 3/3 | 3/3 | 0/3 | 1/3 |
| JacksonDatabind | 3/3 | 3/3 | 0/3 | 2/3 |
| JacksonXml | 3/3 | 3/3 | 0/3 | 2/3 |
| Jsoup | 3/3 | 3/3 | 1/3 | 3/3 |
| JxPath | 3/3 | 3/3 | 0/3 | 1/3 |
| Lang | 3/3 | 3/3 | 0/3 | 2/3 |
| Math | 3/3 | 3/3 | 0/3 | 2/3 |
| Mockito | 3/3 | 3/3 | 0/3 | 2/3 |
| Time | 3/3 | 3/3 | 0/3 | 2/3 |

## การทำต่อด้วยบัญชี natchapol.p

สร้างและเก็บ 9 คำตอบใหม่ ผ่าน KKU เท่านั้น ใช้ prompt เดิม ไม่ส่ง evaluation logs โมเดลตาม UI คือ claude-sonnet-latest, claude-haiku-latest, gemini-pro และ gemini-flash ไม่อ้าง resolved version ที่มองไม่เห็น รอบใหม่สำเร็จ 4 รอบ เพิ่ม 82 methods ที่รันได้จริง อีก 5 คำตอบเป็น service busy, ปฏิเสธหรือขอข้อมูลเพิ่ม และโค้ดถูกตัด ไม่คูณ test methods กับจำนวน fixed/buggy executions

v38 ซ่อมชื่อ local variable result เป็น result1 ใน Codec/Claude/102 โดยคง assertion expected length 10; v39 เรียก nextTag ใน JacksonXml/Gemini/101 fixture ให้ตรง START_ELEMENT precondition ของ fixed constructor เก็บ failed attempts และ snapshots ทั้งหมดไว้ รายละเอียด hashes อยู่ processing-policy-v38/v39.json และ results/validation ในการทดลอง v38 source ถูก hash ก่อน evaluate แต่ descriptive policy file เขียนภายหลัง ไม่อ้างว่า policy file ถูกเขียนล่วงหน้า

คืน CRLF ใน 3 algorithm files ให้ตรง frozen hash เนื้อหาโค้ดไม่เปลี่ยน เก็บ bytes ก่อนซ่อมใน results/validation/frozen-source-eol-recovery-20261001

การตรวจจาก public branch ไม่พบภาพ provider เดิม 25 ภาพและ incident อีก 1 ภาพตาม gitignore จึง provenance audit ปัจจุบันยังไม่ผ่านเต็มชุด แต่ execution audit ของ baseline 171 records, Claude fresh 4 records และ Gemini fresh 19 records ผ่านทุก record ส่วน fresh 9 captures ในบัญชีนี้มีภาพครบ ไฟล์ PDF/PPTX/ZIP รุ่นเก่าเป็น checkpoint ไม่ใช่ผลล่าสุดและยังไม่ใช่การส่ง Classroom

## แหล่งข้อมูลและการทำซ้ำ

ผลหลัก: output/kku-only-20261001/summary.json, analysis.json, study-manifest.csv และ record.json ตาม paths ใน manifest ตรวจ provenance-audit-current.json และ baseline/claude/gemini-evidence-audit.json ก่อนใช้ ทุก completed result ต้องผ่าน audit ที่มี record hash ตรงกัน

การทำซ้ำ: python3 scripts/study/kku_only.py status แล้วรัน audit_kku_provenance.py สำหรับผลใหม่ใช้ evaluate_provider_normalized_vN.py ที่ตรง processing-policy-vN.json และ runtime paths ของเครื่องนี้ เก็บคำตอบผ่านหน้า KKU ด้วย exact original prompt ตาม docs/KKU_ONLY_CONTINUATION.md

เกณฑ์งาน: docs/reference/SQA_Project_2026.pdf แผน/วิธีเดิม: docs/study-protocol.md และ scripts/study/generate.py ข้อจำกัด AI ปัจจุบัน: results/study/kku-only-20261001/protocol.json

Hansen, N. (2016, revised 2023). The CMA Evolution Strategy: A Tutorial. https://arxiv.org/abs/1604.00772

Chen, T. Y., Leung, H., & Mak, I. K. (2004). Adaptive Random Testing. ASIAN 2004, 320–329. https://doi.org/10.1007/978-3-540-30502-6_23

Defects4J 3.0.1 official release documentation: docs/reference/defects4j-readme-v3.0.1.md และ https://github.com/rjust/defects4j/tree/v3.0.1

## ผู้จัดทำ

นายธนินธร อันทรบุตร 673380043-6 / นายศุภกร กรมรินทร์ 673380061-4 / นายณัชพล เพ็งพล 673380267-4 / นายณัฐกรณ์ อินธิสาร 673380268-2

รายชื่อจากรายงานรอบแรกของกลุ่ม 14 สถานะ GitHub/Classroom ให้ตรวจ delivery-status.json ไม่ตีความว่าไฟล์ที่สร้างในเครื่องคือการส่งงานแล้ว

