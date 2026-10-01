# การเก็บหลักฐานจาก Claude และ IntelliSphere

ใช้บัญชีที่เข้าถึงเครื่องมือได้จริงและบันทึกชื่อโมเดลที่หน้าเว็บแสดง ไม่ควรใช้ชื่อ
แพลตฟอร์มแทนชื่อโมเดล หากค่าบางอย่างไม่ปรากฏให้ระบุว่าไม่ทราบ

1. รันตัวเตรียมการทดลองใน `scripts/study/run.py` เพื่อสร้าง `ai-context/prompt.md`
   พร้อม source และ build configuration ของ fixed revision
2. เปิดบทสนทนาใหม่ในแต่ละเครื่องมือ ส่ง prompt และ context เดียวกัน บันทึกเวลา
   ชื่อโมเดล และค่าที่ตั้งจริง ห้ามเติม patch หรือผล buggy ระหว่างการสร้างรอบแรก
3. บันทึกข้อความตอบกลับทั้งหมดเป็น UTF-8 และแยก Java source ที่เครื่องมือสร้าง
   ลงโฟลเดอร์ tests โดยรักษา package directory หากแก้ไขเองต้องบันทึกการแก้ไข
4. นำเข้าด้วยคำสั่งด้านล่าง แล้วประเมิน archive ผ่าน evaluator เดียวกับอัลกอริทึม
5. เก็บรอบที่ compile ไม่ผ่านด้วย หากส่ง prompt ซ่อมให้เก็บ prompt/response ใหม่
   และระบุหมายเลข iteration ไม่เขียนทับหลักฐานรอบก่อน

```bash
python3 scripts/ai/evidence.py ingest \
  --tool claude --model ACTUAL_MODEL_NAME \
  --prompt results/study/RUN/PROJECT/ai-context/prompt.md \
  --response PATH_TO_RAW_RESPONSE --tests-dir PATH_TO_JAVA_SOURCES \
  --metadata-file PATH_TO_OPERATOR_METADATA \
  --output ai-tests/claude/PROJECT-BUG/RUN --seed 101
```

เปลี่ยน `--tool` เป็น `intellisphere` สำหรับอีกเครื่องมือ ค่า `--seed` ในการนำเข้า
คือรหัสรอบสำหรับชื่อไฟล์ Defects4J ไม่ได้ยืนยันว่าโมเดลตั้ง random seed ได้
เรียก `python3 scripts/ai/evidence.py ingest --help` เพื่อดูตัวเลือกทั้งหมด

Operator metadata ควรมี `generated_at` แบบ ISO 8601 พร้อม timezone,
`parameters` ตามค่าจริง, `session_url` เมื่อมี, `manual_edits` และ `notes`
ห้ามใส่ password, cookie หรือ API key ในไฟล์หลักฐาน

การนำเข้าไม่ใช่การยืนยันว่าชุดทดสอบผ่าน ต้องรัน `scripts/study/evaluate.py`
กับ fixed/buggy worktrees และคลาสที่จะวัด coverage ก่อนเพิ่มผลลงรายงาน
ใช้ `--help` เพื่อดูพารามิเตอร์ที่ต้องส่ง และระบุ `--test-count` เฉพาะเมื่อ
นับจำนวน test methods ได้จริง ข้อมูล AI ที่ยังไม่มีต้องแสดงเป็น missing

## การประมวลผล source ที่ใช้กับผลรอบนี้

ใช้เฉพาะ `ai-context/prompt.md` เดิมในแต่ละบทสนทนา ผู้ใช้ไม่อนุญาตให้ส่ง
compile/fixed failure logs เพื่อให้ผู้ให้บริการซ่อมเพิ่มเติม ชุดที่ประเมินจึงเป็น
AI-assisted tests พร้อมการประมวลผลในเครื่อง ไม่ใช่ผลโมเดลที่ไม่ได้แก้ไข

- เก็บ response, rendered HTML/text ของ IntelSphere, metadata และภาพหน้าจอก่อนแก้
- ใช้ Java compiler AST นับ methods และเก็บ 30 methods แรกตามลำดับไฟล์/source
  หาก provider สร้างเกิน budget ไม่เลือกจาก coverage หรือ buggy failures
- IntelSphere UI บางคำตอบมี `[integer](undefined)` ใน Java source จึงลบเฉพาะ
  suffix นี้นอก string/comment ไม่อ้างว่าสามารถตรวจสอบ raw API response ได้
- ถ้า tests ไม่ผ่าน fixed ตัดเฉพาะ failing methods และลอง fixed ใหม่ได้ไม่เกิน
  สองรอบ ไม่เปลี่ยน expected values หรือใช้ผล buggy เลือกว่าจะตัด method ใด
- เก็บ initial attempts และคำตอบก่อนแก้ทุกครั้ง พร้อม SHA-256 ของ source และ
  รายการแก้ไขใน `source-processing-v1` และ `results/validation/ai-source-processing`
- AST parse/compile failures ที่ยังแก้ไม่ได้เป็นผลไม่สมบูรณ์ ไม่มี coverage หรือ
  fault outcome ที่อนุมานขึ้นแทน การล้มเหลวอาจมาจาก UI export/local processor

กติกานี้เพิ่มระหว่างการรวมผล AI หลังพบ responses ที่เกิน budget และ export
artifact จึงไม่ใช่ preregistered protocol ดู `ai-processing-policy.json` ใน batch
และ source snapshots ของ processor ทั้งสามไฟล์ ห้ามแก้ source ที่ audit hash
แล้วระหว่างชุดทดลอง หากเปลี่ยนกติกาต้องสร้าง cohort ใหม่

```bash
python3 scripts/study/run_provider_captures.py --workers 3
```

คำสั่งประเมินเฉพาะ captures ที่มี metadata พร้อมแล้ว ครั้งละไม่เกิน 3 workers
และล็อก worktree ของ project ระหว่าง evaluation ห้ามเปิด wrapper ซ้ำพร้อมกัน
เวลาประเมินได้รับผลจาก shared host load และ caches

Claude แสดง Sonnet 5.5 / Medium ใน session ที่ใช้ IntelSphere ใช้ Auto Router
และเก็บชื่อโมเดลที่ resolve จริงต่อคำตอบ ไม่ตีความเป็นโมเดลเดียวตลอด cohort
อุณหภูมิ, token usage และ model random seed ที่เว็บไม่แสดงเก็บเป็นไม่ทราบ
`generation_seconds` ของ AI เป็นเวลาถึงการสังเกตผ่าน UI บวก processing ในเครื่อง
เป็นขอบเขตบนที่อาจรวมเวลาผู้ปฏิบัติงาน จึงไม่ใช้จัดอันดับความเร็วกับอัลกอริทึม

Budget 30 ของ AI นับ test methods ส่วนอัลกอริทึมนับ proposed input vectors
หนึ่ง method อาจมีหลาย calls/assertions หรือ stateful fixture จึงไม่ใช่จำนวน
program calls หรือ compute budget ที่เท่ากัน และไม่ใช่ input space เดียวกันทั้งหมด

ผลที่ไม่สร้างโค้ด เช่น server busy หรือ quota อยู่ในหลักฐานด้วย การ retry ต้อง
ใช้ prompt เดิมและเก็บ response/record ก่อนหน้าไว้ ไม่เขียนทับผลสำเร็จ

ก่อนเริ่ม cohort ที่ใช้ original prompts เท่านั้น มี Claude Csv budget-correction
response `s101-i2` ซึ่งยังไม่ผ่าน fixed validation เก็บไว้ใน provider captures และ
`results/validation/ai-source-processing` แต่ไม่รวมเป็นผล primary cohort หรือ
นับเป็นคำตอบ original-prompt รอบใหม่ ไม่ได้ส่ง fixed/compile logs ให้โมเดล

### Version 2: filename hints ในกรอบ Java

บางคำตอบใส่ชื่อไฟล์ เช่น `com/example/FooTest.java` ไว้ก่อนบรรทัด package
ภายในกรอบ Java ซึ่งไม่ใช่ Java syntax Version 2 ตัดเฉพาะบรรทัดแรกเมื่อชื่อ
ตรงกับ package/class path หรือชื่อไฟล์ของ class ที่ประกาศไว้ ไม่เปลี่ยน assertions
กติกา cap, renderer suffix และ fixed-only pruning เหมือน v1 ทุกอย่าง

ใช้ `evaluate_provider_normalized_v2.py` และ `source-processing-v2` เฉพาะ
captures ที่มี filename hint ตรงตามกติกา ไม่แก้ source ของ processor v1 ที่ freeze
แล้ว เก็บ v1 parse failures ใน `results/validation/ai-source-processing-v2`
และบันทึก before/after hash chain เชื่อมคำตอบต้นฉบับจนถึง archive ที่ประเมิน
policy/source hashes อยู่ใน `ai-processing-policy-v2.json` ของ batch กติกานี้
เพิ่มระหว่างแก้ ingestion หลังเห็น parse error จึงไม่ใช่ preregistered protocol

Version 3 ขยายการจับ filename hint เดียวกันให้รองรับคำนำหน้า `src/test/java/`,
`src/test/`, `test/` หรือ `tests/` ที่ตามด้วย package/class path ตรงกัน ใช้ driver
และ policy v3 แยกต่างหาก เก็บผลก่อนหน้าใน `ai-source-processing-v3` และ
source-processing-v3 ไม่เปลี่ยน assertions และไม่แก้ processor รุ่นเดิม

### Local compatibility v4/v5 (29 September evening)

The frozen v1/v2/v3 processors remain unchanged. New v4/v5 drivers and
policies preserve failed attempts before applying fixed-API compatibility
repairs: Cli `addValue`, Closure language setter / nested enum qualification,
Time null overload disambiguation, Jackson token accessor, and equivalent
JUnit 4.10 try/catch exception assertions. No buggy feedback selects these
edits and no evaluation logs are sent to providers. Each edit retains the
before/after source hashes and a reason. Successful output remains an
AI-assisted locally processed cohort, not unedited model output.

An overlapping capture runner created an evaluation-directory collision for
JacksonXml run-index 103 during v4 processing. Its failure log and original
run are retained; v5 repeats the same source repair after the runners finish.
Future capture and compatibility batches must run sequentially.

Claude returned a Chart response that evaluated successfully, then showed a
free-plan limit until 02:40 AM. Closure displayed an in-progress response;
after it remained stalled, reloading returned an empty chat. Only its original
request and incident are retained. No complete response is invented or counted.
See `ai-tests/service-events/claude-20260929-evening/`.

### Local parser recovery v6-v9

For seven original KKU responses that had no valid suite or parser record, v6
uses a fixed selection rule: keep the first repeated class in response order;
if the final test method is cut off at the end of the Java block, retain only
the preceding complete methods and close the class. It also repairs two exact
parser-diagnosed bracket/parenthesis typos. v7 adds a null-array overload cast,
one more exact parenthesis repair, and drops one method that calls a constructor
absent from the fixed JxPath API. Each change records source hashes and reason.
The new versions preserve their preceding failures in validation history and
do not send failures or logs to the model. Counts and coverage reflect the
retained tests. These rules were introduced after observing the failures; they
are disclosed as post-hoc local processing, not a preregistered model result.
The later v8/v9 follow-ups repair one missing import, one additional exact
parenthesis error and retain the complete Mockito methods before its final
truncated test. Each revision keeps its own policy and immutable source hash.

### Local fixed compilation recovery v10/v11 (30 September)

Preserved fixed-1 compiler diagnostics identify eight specific test methods using unavailable or private fixed APIs. This post-hoc cohort removes only these named methods, retains raw responses and prior attempts, and reports coverage for the retained suite. v10 parser aborts are preserved; v11 removes KKU renderer suffixes before AST method lookup. Claude JxPath uses an explicit `(String) null` to choose the namespace-prefix overload. Assertions and production code are unchanged. Some suites still fail on additional unsupported APIs; failures remain visible. No evaluation logs or repair prompts were sent to providers. Policy hashes were recorded before evaluation.

Claude Gson and JxPath original prompts were captured on 30 September. The next free-plan reset displayed was 03:40 AM, inferred as 1 October Asia/Bangkok. Screenshot and event are in `ai-tests/service-events/claude-20260930-evening/`.

### Local recovery v12/v13 (1 October, Asia/Bangkok)

Math s103 retained two fixed assertion failures in an old failing_tests file while the command reported one new failure. The frozen evaluator correctly rejected this inconsistency. The local suite excludes these two fixed-diagnosed methods, then undergoes fresh fixed/fixed/buggy/coverage evaluation. No expected values were rewritten. Mockito s103 uses Hamcrest BaseMatcher defaults and removes its forbidden final marker override while preserving custom matching and capture behavior.

v13 removes test methods calling specific unsupported APIs diagnosed on fixed: Cli integer Option constructor, Gson WildcardTypeImpl/getComponentType, and private JxPath getRelativePositionByName. Outcomes report only the retained AI-assisted suite. These policies are post-hoc, not preregistered.

JacksonDatabind s103 originally lost its final failure record after fixed validation excluded all 30 retained methods: the driver attempted to write into an evaluation directory that had not been created for an empty suite. This was reproducible without concurrent jobs. v13 creates that directory and records generation_failed with zero runnable tests. Existing attempts are preserved; the frozen evaluator is unchanged.

`output/submission/test-case-counts.csv` sums retained declared methods across primary completed runs. It counts repeated scenarios across run indices and does not multiply by fixed/buggy executions. Candidate methods from incomplete suites are separate.

### KKU unchanged-prompt retry blocked (1 October)

Chart s102 was retried using only its original prepared prompt. Auto Router resolved to claude-sonnet-5 and returned server busy after visible reasoning. The failure is separate in `results/validation/kku-original-prompt-retries/round2-v4-20260929/Chart/s102/new-capture`; the existing primary failed record and original capture remain unchanged. `ai-tests/service-events/kku-20261001-retry/` holds the event and screenshot. Copy Text did not update the browser clipboard; the exact visible server response was captured from accessibility text instead, and the stale prompt clipboard is preserved with a disclosure. This retry is excluded from the 51 primary KKU requests and successful-suite metrics.

Chart s102 attempt-2: exact original prompt retried in a new KKU chat. Auto Router selected deepseek-v4-pro. The accessibility preview was shortened, but rendered pre/code text contained the complete closing class brace. Verbatim rendered code is retained as rendered-code.json and serialized in response.md. Fixed compilation rejects an unsupported TextAnnotation constructor; no new completed result inferred. Original failed primary capture and run were moved to original-capture/original-run with retry-lineage hashes, while the prior server-busy secondary attempt remains preserved.


### Continued original-prompt runs before Friday delivery (1 October)

Chart KKU s102/s103 original-prompt Gemini retries are complete after disclosed fixed-only pruning and detect the sampled bug. Old raw captures and runs remain in kku-original-prompt-retries. Claude direct s102/s103 are new original-prompt captures using the displayed Sonnet 5.5 Medium label, with real UI screenshots. Claude s102 encountered a Windows/WSL PermissionError while atomically writing a progress record after passing both fixed runs; retry_chart_claude102_evaluation.py preserves the failed run and reevaluates the identical archive through fixed/fixed/buggy/coverage. No test assertions changed for this OS retry.

Cli KKU s103 first Gemini response ended mid-expression and is retained as a secondary truncated response. A second unchanged-prompt chat returned a complete class and was evaluated successfully. Claude direct s102 uses local compatibility v14/v15 to replace all Option.addValueForProcessing calls with the package-visible fixed Option.addValue API; arguments and assertions are unchanged, prior attempts and policies retained. The final suite passes fixed twice and detects the bug.

Collections Claude s101 was retried with its original prompt after the previous quota failure and now completes evaluation. Closure Gemini s101 returned only partial test-plan prose and no Java block; it is secondary history and not counted as successful. Claude agent on KKU currently reports daily usage limit 100%; proof is in ai-tests/service-events/kku-claude-20261001-afternoon. This is a separate quota from Claude.ai direct.

### Fixed-version compatibility cohorts v16–v23 (1 October)

Compress KKU s101 uses the fixed two-step CpioArchiveEntry(format)/setName API instead of an unavailable combined constructor. JacksonXml s102 advances the StAX fixture to START_ELEMENT before constructing FromXmlParser; s103 uses the existing concrete XmlMapper instead of an empty abstract ObjectCodec. JacksonDatabind s101 uses a disclosed generic serializer bridge and configured DefaultSerializerProvider.createInstance. Later cohorts repair fixed property metadata/type APIs in s102/s103 and missing reflection helper overloads in Mockito s101. Each version has a policy with hashes recorded before evaluation; unsuccessful attempts remain in results/validation/ai-source-processing-vN.

Closure KKU s101 requires a compiler-pass fixture rather than Compiler.compile(..., pass), which is not a supported overload. v20 explicitly initializes, parses, normalizes, and executes the original RemoveUnusedVars pass with the original inputs and constructor flags. v21 invokes the same original private static getFunctionArgList via reflection. Original assertions and expected values remain unchanged. Fixed-only validation excluded failing methods; the final retained suite contains 16 of 29 original declared methods and does not detect the sampled bug. These substantial fixture repairs are post-hoc AI-assisted processing, not unedited model output or preregistered policies.

Claude direct Collections and Closure s101, Cli s102/s103, Chart s102/s103, and Codec s102/s103 completed original-prompt generation during this continuation. Codec s103 completed before a free-plan quota notice; the page displays the next reset as 17:50 on 1 October (Asia/Bangkok inferred). Capture-metadata recovery preserves the earlier incomplete staging and metadata, and recovers the observed upper bound from the recorded request-start and capture timestamps. This duration includes delayed observation and is not model compute time.

KKU Closure Gemini secondary retries returned prose or truncated code; its OpenAI secondary retry returned a refusal/missing-context response without Java. None is counted as a completed suite or extra independent run. KKU Claude agent selection displayed 100% daily usage; no new prompt was submitted to that exhausted agent. Only original approved prompts were sent; evaluation/compile logs were never sent back to providers.

v24–v26 additionally select the same named AnnotatedField through fixed fields() iteration, exclude the Mockito method whose original fixture declares capturer1 twice, remove KKU renderer suffixes before the AST lookup, correct the imported verify API spelling, and invoke the same private DOM relative-position method via reflection. The v24 Mockito processing abort (renderer suffix present before AST parsing) is preserved; the preceding failed record was copied back only so the next driver could archive it again. These recovery histories are excluded from primary-run counts. KKU JacksonDatabind s102 completed locally while a duplicate original-prompt UI request was in flight; that secondary truncated response is preserved and never replaces its completed primary record.

v27 inventories the remaining private DOM positioning methods on the fixed source and invokes the original PI/text-node methods through reflection, retaining instances, arguments, and expected positions. Mockito's existing reflection-based vararg test declares its checked exception. Original compile failures and all policy hashes remain preserved. Counters named raw_test_method_count in processing records are measured by the driver after compatibility processing; original response blocks and pre-processing source snapshots must be consulted to count methods removed before that measurement.

v28/v29 extend the same-method reflection access to private DOM string helpers and the retained JDOM positioning methods. The fixed source visibility inventory, input arguments, expected strings/positions and previous failed compilations are preserved. Calls are not substituted with a different production result or rewritten using buggy feedback.

Latest secondary Closure s102 original-prompt Deepseek attempt ended with server busy after visible thinking. The exact final service message and screenshot are preserved; thinking snippets are not counted as final Java sources and the failed primary is unchanged. The primary cohort now has 171 complete/173 observed records, with 31 missing Claude runs and two incomplete KKU Closure runs. These are 1 October observation counts, not a final grading certification.
