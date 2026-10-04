# Diagnostic pilot — generic/null fixtures excluded from production

**Do not expand this adapter condition.** Csv-1 generated valid fixed suites but
both algorithms covered only 1/37 target lines and 0/26 conditions. Receiver
construction exceptions obscured method invocation. These are retained diagnostic
results, not production outcomes. Continue in `../round2-solo-pilot-20261004` using
the existing v12 fixtures and explicit executed/target-check evidence. Do not pool
these two generation attempts with the replacement pilot. The implementation
snapshot in this folder is the original diagnostic version; current source will
reject a resume because its hashes differ.

This is the new cohort, separate from `round2-single-bug-20261004` and all old
shared queues. It has 854 verified active bugs across 17 projects and 3,416 planned
bug/method pairs. It is **not a completed full benchmark**.

- [Current outcomes and pending jobs](Report/data/final_comparison.csv)
- [Current counters](Report/data/summary.json)
- [Benchmark status](Report/benchmark_status.md)
- [Offline protocol and implementation hashes](Experiment/protocol/offline.json)
- [Runtime versions](Experiment/environment/versions.json)
- [Installed inventory command receipts](Experiment/inventory)
- [Csv-1 context](Experiment/contexts/Csv-1/ai-context.json)

`Experiment/cases.csv` and `Experiment/jobs.csv` are immutable planned inventory
snapshots. The jobs snapshot starts entirely PENDING; use Report/data for current
outcomes. Old results are not copied into the new counters.

## Running the next offline step

Run from the repository root in Ubuntu WSL as user `team`, using Java 11 and
Defects4J 3.0.1. No KKU API calls are implemented or enabled in this runner.

```bash
python3 -B scripts/study/solo_batch.py run \
  --output output/round2-full-study-20261004 --cases Csv-1

# The next two pilot cases; not claimed as run until outcomes exist.
python3 -B scripts/study/solo_batch.py run \
  --output output/round2-full-study-20261004 --cases Lang-1 Math-1

python3 -B scripts/study/solo_batch.py report \
  --output output/round2-full-study-20261004
```

Re-running a case checks hashes and retains finished outcomes rather than
generating new suites. A process interrupted within a stage keeps its partial
evidence; recovery must be reconciled explicitly. Completed stages can resume,
but uncertain stages are not automatically repeated. A terminal infrastructure
failure is not a valid suite and is not silently retried to improve results.

The CPU lock is the existing
`/home/team/sqa-round2/worktrees/.beam-cpu-slot.lock`, one slot for this host.
Fresh checkouts are under `worktrees/<cohort>/<case>/{b,f}`. Checkouts, compiled
classes and private runtime data stay on the machine, outside the published
source evidence. Resume requires the original workspace and these checkouts;
a fresh clone should create a fresh output cohort with `init` before running.
Receipts include hashes of local compiled probe classes for provenance; Git
retains their source and compiler commands rather than all compiled binaries.

## Interpretation

The generic algorithm adapter discovers common declaration signatures and uses
the existing bounded recursive fixtures. CMA-ES minimizes frequency of fixed
behaviors; FSCS-ART maximizes normalized input distance. Each proposes 30 vectors
with seed 101, observing each twice on the fixed version. Only stable observations
become assertions. This does not prove invocation/coverage of every declaration.
The limitations of the adapter and opaque object oracles remain in generation
manifests; unsupported cases must not be called successful full-project tests.

The AI context is a deterministic compact excerpt from buggy production source
and buggy API signatures only, with no fixed source, patch, dataset trigger tests
or buggy-failure feedback. Source excerpts have a combined 12,000-character cap,
allocated equally among sorted modified classes. Signatures have a separate
6,000-character cap. This source limit is not a token count. **AI requests are
still pending**, and their stage protocol/settings/quota need a separate check
before the AI pilot. These are self-reviewed results from a single operator;
no new peer verdict or old Gate A approval is claimed.

Coverage counters come from Cobertura `LinesCovered/LinesTotal` and
`ConditionsCovered/ConditionsTotal`; the existing evaluator names the latter
`branch_covered/branch_total`. Missing metrics remain null/blank. Generated JUnit
method counts are not assumed to equal verified executed tests. Two fixed runs
are repeatability checks for the same suite, not independent generation repeats.

Results, failed stages, protocol, retained implementation and command logs are
kept together. Final report PDF, slides, demo and ZIP for the full-scope cohort
are subsequent steps; the historical Csv-1 package is preserved separately.
