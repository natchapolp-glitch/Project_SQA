# Round 2 experimental protocol

## Scope and research questions

This study compares CMA-ES, FSCS-ART, Claude and IntelliSphere on Defects4J
3.0.1. `dataset/study-targets.json` is generated from the installed release's
`pids` and `bids`, rather than treating the Lang-only root CSV as the dataset.
The initial selection is the lowest active bug ID in each of the 17 projects.
This covers project identities, not every active bug. No instructor approval
of this sampling choice is assumed. Unsupported APIs, build failures and absent
AI submissions stay visible and do not count as completed experiments.

RQ1 concerns line and condition coverage of the classes modified by each selected
fix. RQ2 concerns distinct bugs detected. RQ3 concerns generation time, evaluation
time, compilation success and practical limits. These are target-class coverage
measurements, not coverage of every class in each project.

Execution used disjoint project workers concurrently on the same Windows/WSL
host, with up to four workers including follow-up repairs. Checkouts/build caches
are reused, and later suites can benefit from cache warming. Wall-clock times
therefore include JVM/build overhead and shared host load; they are descriptive
workflow costs, not isolated algorithm CPU benchmarks or evidence of a causal
speed advantage. Setup/checkout time is excluded from generation/evaluation totals.

Mockito FSCS-ART runs can use `scripts/study/run_isolated.py`, which creates
separate fixed/buggy checkouts for disjoint seed/method identities. It uses the
original frozen target inventory, re-bases only checkout paths in the classpath,
and preserves the driver source hash/snapshot and checkout/compile logs. It
refuses to replace existing generated suites. This improves execution throughput;
it does not change the input generator or assertion oracle. The existing serial
worker may report an already-existing generation directory for such an identity;
the definitive completion evidence is evaluation/record.json and its raw logs.

## Reproducible generation

The frozen configuration is `experiments/configs/round2.json`: three recorded
seeds and 30 proposed input vectors for each search generator and selected bug.
FSCS-ART chooses the candidate farthest from previous vectors in normalized input
space. CMA-ES minimizes the frequency of the observed fixed-revision behavior.
This is an output-diversity objective, not an instrumented coverage objective.
A final incomplete CMA-ES population never exceeds the requested input budget.

`SqaProbe` discovers declared methods and constructors on modified classes,
including non-public members when Java reflection permits access. It builds
bounded inputs from scalar values, enums, arrays, strings, common collection
interfaces, and a limited recursive-constructor fixture heuristic; it also
tries a concrete project class for an abstract target. This is not exhaustive
object-graph generation. Scalar/array outputs are recorded by value, opaque
objects by runtime type, and exceptions by class only. Each input runs on the
fixed revision twice in separate JVMs. Stable observations become assertions,
including inputs that do not expose a fault. Missing behavior, timeouts and
unstable observations remain in the raw ledger but do not become assertions.
Module access restrictions, fixture construction failures, and type-only
observations can limit meaningful coverage and must be reported. Declaration
signatures are discovered on both revisions and intersected before generation,
using fixture class names present on both. A method added only by the fix is not
an eligible test target. No buggy execution outcome enters this eligibility step.
Exact serialized outcomes longer than 16000 characters are represented by their
SHA-256 digest and UTF-8 byte length, which keeps generated Java literals within
the JVM constant limit.

Buggy execution outcomes, the fix patch and developer triggering test are not
supplied to the generator. Shared declaration signatures are the eligibility
input, and fixed behavior is the assertion oracle. The runner uses the triggering test only to check that the dataset
bug is reproducible. Existing defect-informed Lang-1 pilot evidence is retained
separately and must not be pooled with these prospective results.

The algorithm budget counts proposed inputs. Fixed-reference validation uses two
observations per proposal; final JUnit executions and coverage add separate cost.
Different vectors can decode to the same Java arguments. Report retained vector
count as test count, not as proof of distinct semantic inputs. This limited
adapter does not cover arbitrary object graphs or stateful call sequences, and
an observed opaque-object type does not assert its internal state. Private
members are attempted only where reflection permits access.

## Validation and metrics

An external JUnit source archive is compiled and run twice on fixed, then once
on buggy. The evaluator parses Defects4J's failure count and failure artifact;
the command exit code alone is insufficient. Both fixed runs must pass. A valid
suite may detect zero faults. Harness/linkage errors and timeouts are not fault
detections. Cobertura coverage is collected on fixed using the same class list
for all four approaches. Raw commands, logs, source archive hashes, timestamps,
Java version, Defects4J revision and coverage XML/CSV are preserved per run.

- Line coverage: LinesCovered / LinesTotal.
- Condition coverage: ConditionsCovered / ConditionsTotal from Cobertura.
- Fault detection rate: distinct detected project/bug pairs / distinct eligible
  evaluated pairs, within each method and declared budget. Repeated seeds do not
  multiply the bug count.
- Unknown or failed metrics remain null, not zero. Full comparison requires all
  four approaches evaluated on the same target set. AI run indices are not
  stochastic model seeds unless the provider actually supports and records seeds.

### Follow-up fixed validation

The initial v4 generation checks repeatability in separate observation JVMs.
JxPath exposed an object identity dependent `hashCode` oracle that coincidentally
matched in those JVMs but differed in JUnit. The reported follow-up rule,
`fixed-validation-pruning-v1`, removes only generated methods listed as failing
in a fixed validation artifact and re-evaluates the unchanged remaining inputs.
It applies equally to CMA-ES and FSCS-ART. No buggy outcome is consulted to choose
exclusions, and no new inputs are proposed. Up to three fixed repair attempts
are allowed; a suite with no remaining tests stays invalid.

`scripts/study/repair_fixed_oracles.py` preserves each prior generation and
evaluation under results/validation. New records contain removed case IDs,
fixed stage, original archive hashes, attempt cost and the repair script hash.
Report raw first-attempt failures alongside the final retained suites. Final
test count can be below the input budget. `workflow_seconds` includes generation,
final evaluation, failed evaluation attempts and repackaging; `total_seconds`
includes only generation and final evaluation. Neither includes checkout/setup,
idle user time or development/debugging effort.

## AI provenance and fairness

The actual AI cohort uses the disclosed local processing in `docs/ai-workflow.md`:
source-order budget caps, limited IntelSphere renderer cleanup, and at most two
fixed-only method pruning follow-ups. Raw responses and failed attempts remain
available. This policy was introduced during AI integration, not preregistered.
Only original prepared prompts are sent to providers. No fixed/compile failure
logs are sent for additional model repair. The cohort is AI-assisted with local
processing, not unedited model output. IntelSphere Auto Router resolves different
models; provider UI timing is an observation upper bound, not model compute time.

Both AI tools receive the exact prepared fixed-source context and prompt.
Record the actual provider, model, date, settings, response and each repair
iteration. Preserve uncompilable responses as outcomes. Use `scripts/ai/evidence.py`
to import actual responses, then `scripts/study/evaluate.py` for the same external
suite evaluation. Do not attribute locally authored tests to either AI provider.
Prompt/token cost and algorithm input budgets are different units and must be
reported separately. AI prompts include the same eligible declaration inventory
and request at most 30 independent test methods per response. AI fixture design
can differ from the bounded reflection adapter; discuss this capability difference
in the final comparison.

## Sources

- Assignment: `docs/reference/SQA_Project_2026.pdf`, section 2.2.
- Defects4J release: https://github.com/rjust/defects4j/tree/v3.0.1
- External-suite evaluation: https://defects4j.org/html_doc/run_bug_detection.html
- Coverage: https://defects4j.org/html_doc/run_coverage.html
- Hansen (2016), The CMA Evolution Strategy: A Tutorial: https://arxiv.org/abs/1604.00772
- Chen, Leung and Mak (2004), Adaptive Random Testing: https://link.springer.com/chapter/10.1007/978-3-540-30502-6_23

These references establish the algorithm concepts, not performance claims for
this implementation. The class selection uses known `classes.modified` from
the benchmark fix as localization information, so this is not an unknown-defect
black-box test-generation study on the full project. Assertions, adapter,
representation, Cholesky-based implementation and fixed-behavior-frequency
fitness must be evaluated as the documented project implementation.

## Continued fixed-version fixture recovery (1 October)

Local source-processing cohorts v14–v26 preserve the raw provider outputs and prior attempts with versioned hashes. They include explicit compiler-pass fixture initialization/normalization, reflective access to the same original private methods, and legacy constructor/metadata/type/codec API repairs. This is post-hoc AI-assisted testing with local intervention. Do not present those suites as unmodified AI output or compare their completion rates as if every provider received the same repair effort. Assertions and expected values are not changed using buggy results; methods failing fixed validation may be excluded under the declared policy. Secondary original-prompt retries are retained outside primary metrics.
