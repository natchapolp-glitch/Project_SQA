# Concrete problems and lessons from Round 2

## Shared APIs are required for a valid oracle

The early JacksonCore CMA-ES suites passed fixed but failed on buggy because
`_badBigDecimal` did not exist in the buggy class. The failure was
`SQA_HARNESS reflection failure`, not a behavioral assertion. The evaluator
correctly refused to count it as bug detection. Evidence is retained at
`results/validation/round2-v3-20260928/JacksonCore/cmaes-s101-b30/evaluation/buggy/failing_tests`.

The repaired runner inventories declaration signatures on fixed and buggy,
uses fixture class names present on both, and selects their intersection.
This uses API eligibility only; expected values and search fitness still come
from fixed execution. Fixed-only exclusions and both inventories are saved in
each project setup folder. AI prompts receive the same eligible inventory.

## Observation encoding must respect Java limits

A JacksonCore FSCS-ART suite could not compile because the serialized char-array
oracle exceeded Java's constant-string limit. Its complete compiler log is in
`results/validation/round2-v3-20260928/JacksonCore/fscs-art-s101-b30/evaluation/fixed-1/command.log`.
Long exact observations now use SHA-256 plus UTF-8 byte length. An integration
test checks repeatability, distinguishes two long arrays, and verifies the
result stays bounded. Small outputs keep exact scalar/array assertions.

## Test framework dependencies can fail before a test starts

All six initial Cli v4 suites failed in JUnit initialization with
`NoClassDefFoundError: org/hamcrest/SelfDescribing`. The Cli framework build
selected a plain JUnit 4.12 jar while the bundled combined JUnit/Hamcrest jar
was available. The repair changes this dependency path for both revisions and
re-evaluates the identical generated archives, with their hashes verified.
No production source, input, assertion or oracle changes are made.

Before/after framework files and jar SHA-256 are in
`results/study/round2-v4-20260929/environment-repair/`. Initial failures are in
`results/validation/round2-v4-20260929-cli-classpath-failure/`.
Reproduction uses `scripts/study/repair_cli_junit.py`; a clean setup should
apply the same one-line dependency correction before starting Cli evaluation.

## The adapter controls what search can explore

JxPath's CMA-ES seed 101 selected an identity-dependent `hashCode` value.
Separate observation JVMs happened to produce the same value, but fixed JUnit
validation failed. Follow-up fixed-only pruning removes that single assertion,
keeps the other proposed inputs, and re-runs the evaluator. The original
generation and failing suite remain in
`results/validation/round2-v4-20260929-fixed-oracle-repair/`. The pruning rule,
cost, source hash and excluded case IDs are explicit in the new record. Two
repeat observations are useful but cannot prove cross-context determinism.

Both algorithms use a normalized vector representation. Different vectors can
decode to the same argument values. Null/default fixtures are useful boundary
inputs, but can spend many evaluations on immediate exceptions. Object outputs
are observed by runtime type/nullness, and constructor assertions observe
construction or exception only. Coverage and zero-detection outcomes must be
interpreted with these limitations. A project-specific stateful fixture could
improve coverage, but constitutes a new study version and needs new evidence.

The Cli regression example observes an iterator's runtime implementation type.
It establishes a revision-sensitive assertion under the evaluator's operational
definition, but does not assert the iterator's element contents. Therefore
`fault_detected` counts should be read with the generated assertions: they are
not independent proof that every case exposes a semantic public-API failure.
Future work should add value/state oracles and stateful fixture construction.

## Keep failed results and versions

Earlier batches are retained under `results/validation/`; they are excluded from
the repaired study's aggregate. Each batch freezes configuration and source
SHA-256 values, including the algorithm implementations. Each evaluation keeps
the Java/Defects4J version, test archive, commands, fixed/buggy logs and coverage.
Missing, failed and interrupted records stay distinct from valid zero outcomes.

## Runtime names need verification

The existing WSL distribution is named `Ubuntu`. An earlier command used
`Ubuntu-24.04` and returned DISTRO_NOT_FOUND, which led to an incorrect diagnosis
that the environment was missing. Listing installed distributions recovered the
working Java 11 and Defects4J environment. The subsequent v4 run uses `Ubuntu`.

## AI provenance remains an independent requirement

Actual provider responses are now retained under `ai-tests/provider-captures`.
Some responses exceed the requested method budget, some rendered Java is
truncated or contains UI artifacts, and some provider calls return server busy.
The local AST cap, limited renderer cleanup and fixed-only pruning are disclosed
in `docs/ai-workflow.md`, with original source, edits and failed attempts retained.
Do not attribute a UI export/parser failure solely to the model. Auto Router
model selection varies per response. UI observation timing can include operator
delay and must not be treated as model compute time. No additional compile/fixed
logs are sent to providers under the user's original-prompt-only instruction.

A prompt template and locally authored JUnit source do not establish a Claude
or IntelliSphere result. The workflow stores actual provider responses, model,
settings, timestamp and repair iterations before applying the same evaluator.
Without those responses, algorithm results can be reported but a complete
four-method ranking is unsupported.
