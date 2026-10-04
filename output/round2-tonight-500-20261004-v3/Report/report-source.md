# AI-Assisted Testing vs. Automatic Test Generation

CP353201 Software Quality Assurance | Round 2 | 4 October 2026

CMA-ES / FSCS-ART / KKU Sonnet 5 / KKU Gemini 3.5 Flash Lite

Measured deadline snapshot: 17 bugs across 17 projects. 68 recorded bug/method jobs out of 2,000 planned. This is a partial benchmark; the chosen target is 500 bugs, not a completion claim.

นายธนินธร อันทรบุตร | 673380043-6

นายศุภกร กรมรินทร์ | 673380061-4

นายณัชพล เพ็งพล | 673380267-4

นายณัฐกรณ์ อินธิสาร | 673380268-2

Repository: https://github.com/natchapolp-glitch/Project_SQA | branch Team

Results snapshot (UTC): 2026-10-04T04:41:03.810754+00:00


---

# 1. Objective and declared scope

The study implements two algorithms and uses two university-hosted AI models to generate unit tests for real Java bugs in Defects4J. We compare fixed validation, buggy-revision fault detection, target-class code coverage, generation/evaluation time and reported token usage.

Item | Experimental design

Dataset | Defects4J 3.0.1; 854 active bugs in 17 projects

Selected target | 500 bugs / 2000 jobs; project-sorted round robin, without outcome filtering

Executed scope | 17 bugs / 17 projects recorded at this snapshot

Algorithms | CMA-ES and FSCS-ART; seed 101, budget 30 vectors

AI models | claude-sonnet-5 and gemini-3.5-flash-lite via KKU only

Validation | One unchanged suite: fixed-1, fixed-2, buggy, fixed coverage

Environment | Java 11.0.32.1; WSL Ubuntu; America/Los_Angeles test timezone

Host | Ryzen 3 3100, 4 cores/8 threads, 16 GB RAM; one CPU slot

A bug/method pair is the counting unit. Multiple JUnit methods, P01-P04 calls, fixed validation repeats and rejected extensions are not additional bugs. Missing measurements are left unavailable rather than replaced by zero.

The course's round-two specification asks for all Java projects, measurements, comparisons, reproducible source/test/prompt/configuration evidence, a complete report, presentation and demo. The snapshot discloses which work remains; folder structure or a planned inventory does not prove it was executed.


---

# 2. Algorithms and test oracles

CMA-ES proposes vectors from a multivariate normal distribution and adapts the mean, covariance and step size from objective scores. The implemented objective minimizes observed fixed-behavior frequency as an output-diversity proxy. Runtime coverage is measured afterwards; it is not the optimization fitness.

FSCS-ART draws a candidate set and selects the point whose minimum normalized distance to previously selected points is greatest. The candidate-set size is 10. Both methods use the same target inventory, vector bounds, seed and bounded Java adapter/receiver policy.

The shared adapter discovers declarations in modified classes available in both revisions. Existing v12 fixtures construct supported receivers and arguments. Unsupported signatures or generation/build failures are retained as outcomes; the study does not claim arbitrary support for every Java declaration.

Algorithm tests use stable fixed-revision reference observations as their expected values. Each retained observation is checked twice and must invoke its intended target. This supports regression comparison but differs from contract-derived AI assertions. Opaque objects, unsupported constructors and limited vector domains restrict fault detection.

Method | Budget | Oracle / input policy

CMA-ES | 30 vectors | Stable fixed observations; behavior-frequency objective

FSCS-ART | 30 vectors | Stable fixed observations; normalized input distance

AI | Up to 12 + 4 methods | Buggy context, API contracts, one bounded fixed-feedback repair


---

# 3. AI generation protocol

P01 analyzes context. P02 creates complete Java 7-compatible JUnit 4 source for the Java 11 runtime. Optional P03 repairs compile or fixed-validation issues once. After measuring fixed coverage, P04 may add new test classes; invalid additions are rejected while preserving the valid base suite. No buggy failure feedback is used in repair or extension prompts.

Both models receive identical master wording, context selection and repair/extension policies within a condition. Temperature 0, max_tokens 4,096 and stream false are requested. Sonnet uses /messages with thinking disabled; Gemini uses /chat/completions. Accepted requests do not prove effective settings, and output truncation remains a measured outcome.

Condition | Cases / context | Important distinction

Initial pilot | Csv-1, Lang-1, Math-1; source up to 12,000 characters + signatures up to 6,000 | P01 also included buggy source; Sonnet Lang output incomplete

Compact nightly | Remaining cases; P01 metadata/signatures only; P02 same source budget | Prefer 4-6 concise methods, hard maximum 12; predetermined key alias per case

The protocols are labelled separately and preserved with source hashes. Aggregate descriptive values do not establish a controlled comparison between the two prompt conditions. Ten supplied key aliases are available, but key count alone does not demonstrate independent accounts or quota buckets.

Raw assistant text, actual sent prompts, model/provider IDs, usage/quota observations, generated Java and all validation stages are retained. Credentials are stored only in ignored local private files and are excluded from the delivery.


---

# 4. Validation and measurement

For executable generated suites, the evaluator runs fixed twice, then buggy, then Cobertura coverage on modified classes. A detected fault requires a valid suite and a genuine failing buggy-revision test. Linkage errors, harness failures, timeouts and fixed-invalid assertions are not counted as detected faults.

Algorithm method entries and target invocations are checked with stage counters. AI entries are verified through Defects4J Formatter.startTest ledgers across all four stages. Missing AI skipped/target-check counters remain unavailable. Passing fixed twice improves confidence in repeatability but does not prove absence of flakiness.

Metric | Definition / denominator

Evaluation rate | DONE / recorded attempted jobs for that method

Fault detection | Fault-detecting cases / cases with valid measured buggy results

Line coverage | Covered lines / total lines in modified target classes; average only measured ratios

Condition coverage | Cobertura branch/condition counters; average only measured nonzero denominators

Time | Generation API/algorithm time and selected evaluation time reported separately

Tokens | Reported input/output components; unknown usage is not estimated as zero

Selected-suite evaluation time excludes failed earlier base/repair/extension evaluations and shared checkout/setup. Per-job elapsed time, stage timings and logs remain in the raw records. No total full-dataset completion time is extrapolated from this small snapshot.


---

# 5. Recorded outcomes

Method | Attempted | DONE | Fault result | Fault cases

CMA-ES | 17 | 15 | 15 | 2

FSCS-ART | 17 | 15 | 15 | 1

KKU Sonnet 5 | 17 | 7 | 7 | 0

KKU Gemini 3.5 Flash Lite | 17 | 7 | 7 | 0

All four method outcomes are recorded for 17 bugs. All four evaluations completed successfully for 3 bugs. These are different counts; fixed-invalid or output-incomplete results are still experiment outcomes.

CMA-ES: DONE=15, PENDING=483, INFRA_ERROR=1, INVALID_GENERATED_SUITE=1

FSCS-ART: DONE=15, PENDING=483, INFRA_ERROR=1, INVALID_GENERATED_SUITE=1

KKU Sonnet 5: DONE=7, PENDING=483, INFRA_ERROR=1, INVALID_AFTER_REPAIR=6, QUOTA_PAUSED=2, OUTPUT_INCOMPLETE=1

KKU Gemini 3.5 Flash Lite: INVALID_AFTER_REPAIR=8, PENDING=483, INFRA_ERROR=2, DONE=7

PENDING describes selected work absent from the sealed snapshot, which may include a running job. QUOTA_PAUSED describes a blocked stage, not a generated-suite failure. This report does not claim 500 attempted cases for either AI. The original 854-bug inventory is retained separately; bugs outside the selected scope are not PENDING jobs in this table.


---

# Recorded coverage by Java project

Project | Recorded method outcomes

Chart | 4

Cli | 4

Closure | 4

Codec | 4

Collections | 4

Compress | 4

Csv | 4

Gson | 4

JacksonCore | 4

JacksonDatabind | 4

JacksonXml | 4

Jsoup | 4

JxPath | 4

Lang | 4

Math | 4

Mockito | 4

Time | 4


---

# 6. Coverage and token observations

Method | Line cases | Mean line % | Condition cases | Mean condition %

CMA-ES | 15 | 45.6 | 15 | 28.6

FSCS-ART | 15 | 43.8 | 15 | 27.6

KKU Sonnet 5 | 7 | 61.0 | 7 | 51.5

KKU Gemini 3.5 Flash Lite | 7 | 59.6 | 7 | 46.8

Averages exclude missing measurements and zero denominators. Different target classes, supported domains, suite sizes and failed-case selection mean these descriptive averages do not prove one method is superior.

AI | Input recorded | Output recorded | API calls

KKU Sonnet 5 | 229201 | 61152 | 45

KKU Gemini 3.5 Flash Lite | 206274 | 40977 | 54

Sonnet responses may omit total_tokens. When input/output components are present, they are reported explicitly without claiming a provider-reported total. Quota records reflect response time, not guaranteed current availability. No precise daily reset time or pooled quota is asserted.

Method | Mean generation s | Mean selected evaluation s | Measured timing cases

CMA-ES | 15.6 | 39.6 | 17 / 17

FSCS-ART | 14.5 | 39.7 | 17 / 17

KKU Sonnet 5 | 27.4 | 16.4 | 17 / 13

KKU Gemini 3.5 Flash Lite | 9.7 | 15.1 | 17 / 17

Means use recorded numeric timings only, including unsuccessful jobs when a timing was captured. The last column gives generation/evaluation timing counts. These are not total pipeline completion times.


---

# 7. Problems, limitations and learning

Initial Sonnet Lang-1 generation stopped at the 4,096-token output limit; the incomplete Java is preserved without repeated generation to obtain a better result. Sonnet Math-1 stopped before P02 because remaining quota was below the conservative byte-based operator guard; this is not a provider report of zero quota.

Cli-1 exposed a missing Hamcrest class on the test classpath (NoClassDefFoundError: org/hamcrest/SelfDescribing). This is recorded as an infrastructure failure rather than a product fault or a model-quality result. Other build/generation/repair failures are retained with their original stages and logs.

The pilot and nightly cohorts use different prompt compaction. Algorithms use fixed-reference observations while AI uses contract reasoning with bounded fixed feedback. Unequal suite sizes, only one generation per bug/method, bounded adapters and partial dataset execution limit causal or statistical conclusions.

Useful learning: source compilation is not proof that tests execute; fixed validity is not fault detection; high target coverage does not guarantee the dataset bug is exposed; quota pauses are not failed tests; and planned case counts must remain separate from measured outcomes.

Further work is to complete missing cases and projects, recover infrastructure under a declared new condition without regenerating model outcomes, collect more independent runs/configurations when time permits, and compare matched domains with transparent resource budgets.


---

# 8. Reproduction, demo and evidence

Start from the root README and Presentation/demo-guide.md. demo.py shows actual snapshot outcomes and can replay a selected DONE archive on the original Java 11/Defects4J host. Replay writes a fresh output directory, keeps old evidence unchanged, uses the existing CPU lock and sends no AI requests.

Folder | Evidence

Algorithm1_CMAES / Algorithm2_FSCSART | Code, Configuration, Result_Round1 and Test; Round2 explicitly not performed

AI1_KKU_Claude / AI2_KKU_Gemini | Condition-labelled master prompts, actual prompts/raw answers, generated/final source

Experiment | Full planned inventory, snapshot manifest, protocols/runtime, measured evaluations and contexts

Report | This report, source, data tables and four method summaries

Presentation | Editable deck, demo guide, replay script and rehearsal receipt

Reference: course project specification supplied by the user, clauses 1.5-1.10 and 2.2. Dataset: https://github.com/rjust/defects4j. Layout/workflow reference only: MammamiaPizza/ProjectSQAGroup11 branch jiratchaya_673380510-1; none of the friend's results are imported as our own.

CMA-ES background: Hansen, The CMA Evolution Strategy: A Tutorial, https://arxiv.org/abs/1604.00772. FSCS-ART background: Chen, Leung and Mak, Adaptive Random Testing (2004). Actual implementation choices are disclosed in the included generator and configuration rather than inferred from algorithm names.

The complete per-case table is Report/data/final_comparison.csv. Each attempted job links to a retained outcome. Checksums and package verification provide byte-level traceability; they do not substitute for the experiment or independent scientific review.
