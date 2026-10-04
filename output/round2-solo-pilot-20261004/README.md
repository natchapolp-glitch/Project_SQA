# Current solo offline pilot

854 active bugs / 17 projects / 3,416 planned jobs. This is an **offline pilot**,
not a completed full benchmark. Only actual outcomes in the current table count;
AI workflow, requests, report PDF, slides, demo and full-scope ZIP remain pending.

Completed offline pilot: Csv-1, Lang-1 and Math-1, both algorithms, **6 outcomes**.
Each has 30 executed tests, 0 skipped, 30 target checks in all four stages;
fixed runs passed twice and no buggy fault was detected in this bounded domain.
DONE 6 / PENDING 3,410; AI jobs remain PENDING. Resume retained every generation
and measurement byte. See the [review receipt](Experiment/diagnostics/offline-acceptance/receipt.json).

- [Latest job table](Report/data/final_comparison.csv)
- [Counters](Report/data/summary.json) / [status](Report/benchmark_status.md)
- [Protocol and code pins](Experiment/protocol/offline.json)
- [Installed inventory evidence](Experiment/inventory)
- [Runtime versions](Experiment/environment/versions.json)
- [Csv-1 buggy-only AI context](Experiment/contexts/Csv-1/ai-context.json)
- [Retained runner/core source](Experiment/automation/runtime)

No AI requests are sent by `solo_batch.py`. KKU Sonnet 5 and Gemini 3.5 Flash Lite
are the planned models; current access, quota and AI stage protocol will be checked
before starting new requests. No old shared queue or Gate A flag is modified.

## Run/resume in the original workspace

```bash
cd /mnt/c/WORK/SQA_PROJECT/Project_SQA
python3 -B scripts/study/solo_batch.py run \
  --output output/round2-solo-pilot-20261004 --cases Csv-1

# Subsequent pilot cases; count only after their outcomes have been produced.
python3 -B scripts/study/solo_batch.py run \
  --output output/round2-solo-pilot-20261004 --cases Lang-1 Math-1

python3 -B scripts/study/solo_batch.py report \
  --output output/round2-solo-pilot-20261004
```

Use Ubuntu WSL user `team`, Java 11 and Defects4J 3.0.1. Completed stages are
hash-checked rather than regenerated. Interrupted/uncertain stages are retained
and require explicit reconciliation before recovery. `init` creates a fresh output
directory and must not be run over this one. The existing CPU lock under
`/home/team/sqa-round2/worktrees` permits one CPU-heavy job at a time.

Checkouts and compiled probe classes are local runtime artifacts. Git preserves
their source, compiler commands and hashes rather than all compiled binaries.
Resume requires the original checkouts/compiled artifacts; a fresh clone must
initialize a fresh cohort before generation. Planned `Experiment/jobs.csv` is an
immutable PENDING snapshot; current outcomes are in Report/data, not that snapshot.

## Condition and limitations

The current algorithm condition uses the **existing v12 bounded fixtures**.
Each algorithm proposes 30 vectors, seed 101, with two fixed observations per
vector. Retained observations must show actual target invocation. Successful
full evaluations must also retain executed, skipped and target-check counters
for fixed-1, fixed-2, buggy and coverage. This verifies the selected cases, not
coverage of every declaration or every API in a project. No new fixtures are
being developed to delay the experiment.

CMA-ES fitness remains frequency of fixed behavior; FSCS-ART uses normalized
input distance. Both use fixed observations as oracles; AI receives buggy-only
context. The existing bounded domains and opaque-object comparisons limit what
can be detected. These differences must be disclosed in the report.

AI source context has a combined 12,000-character cap, equally allocated among
sorted modified classes, plus up to 6,000 characters of buggy `javap` signatures.
No fixed source, patch, trigger test or buggy-failure feedback is included.
These character budgets do not represent measured token counts.

The existing evaluator's `branch_covered/branch_total` fields are Cobertura
`ConditionsCovered/ConditionsTotal`. Missing measurements remain null/blank.
Generated methods and verified execution counters are distinct. Two fixed
replays of one suite are not independent generation repeats. All new results are
single-operator checks/self-review, not new peer approval.

## Diagnostic results kept separately

`../round2-full-study-20261004` retains the initial generic/null-fixture pilot.
Its two Csv-1 suites covered only 1/37 lines and 0/26 conditions, exposing receiver
setup as the dominant observation. That condition was stopped and is **excluded
from this cohort**. Historical one-bug delivery and all old evidence remain
unchanged. Invalid, unsupported and infrastructure outcomes are retained rather
than converted to passing scientific results.
