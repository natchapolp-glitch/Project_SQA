# Experiment design

## Research questions

- RQ1: How do CMA-ES, FSCS-ART, Claude, and IntelliSphere compare in line and
  branch coverage under comparable execution budgets?
- RQ2: How do they compare in real fault detection on active Defects4J bugs?
- RQ3: What are their practical costs: runtime, evaluation count, test compilation
  rate, and (for AI) prompt iterations?

## Pilot scope

Start with three to five active Apache Commons Lang bugs. A candidate becomes a
selected bug only after its buggy and fixed revisions compile, its triggering test
is reproducible, and its focal method has an input representation supported by the
pilot generator. The validated choices belong in `dataset/selected-bugs.csv`.

## Shared test-oracle rule

The algorithms generate inputs, not a trustworthy expected value. Therefore the
pilot uses differential testing:

```text
generated input --> fixed revision --> reference behaviour
                \-> buggy revision --> compare observable behaviour
```

An input detects a fault only when the generated test passes on the fixed revision
and fails on the buggy revision. Compare return values, thrown exceptions, and
observable state; exclude nondeterministic inputs.

## Fairness protocol

- Run search algorithms with the same evaluation budget per bug and configuration.
- Repeat each stochastic configuration with recorded independent seeds.
- AI tools receive the same focal-class context and goal. Limit repair/prompt
  refinement to the configured maximum.
- Report AI token usage and prompt iterations separately; they are not equivalent
  to program evaluations.
- Record failures rather than silently discarding uncompilable tests.

## Per-run record

Each raw result must identify `generator`, `project`, `bug_id`, `revision`,
`focal_class`, `focal_method`, `run`, `seed`, `budget`, `started_at`, `ended_at`,
`compile_status`, `line_coverage`, `branch_coverage`, `fault_detected`, and
`artifact_path`.
