# AI-generated-test evidence layout

The current all-project workflow is `docs/ai-workflow.md`, implemented by
`scripts/ai/evidence.py`. Prepared prompt/source/target bundles are in each
project's `results/study/round2-v4-20260929/<project>/ai-context/`. Use that
workflow for Round 2; the Lang-only example below is historical.

A valid evaluated suite must pass fixed validation twice. It may detect zero
faults; that is a completed negative outcome. A detected regression additionally
fails on buggy without a harness/linkage/timeout error. Do not discard valid
non-detecting suites or label missing provider responses as completed runs.

Actual authorised browser responses are now stored in
`provider-captures/<tool>/<project>-1/s<run-index>-i1/`, with model metadata,
screenshots, and original/processed Java source. Final evaluation evidence is
in the corresponding `results/study/round2-v4-20260929/<project>/<tool>-s<index>-b30/`.
Failed and superseded attempts remain under `results/validation`. No credentials
are stored. See `docs/ai-workflow.md` for original-prompt-only transmission,
local processing, Auto Router model variation and timing limitations.

The historical Lang evidence layout was:

```text
ai-tests/<tool>/Lang-1/<run-id>/
  prompt.md
  metadata.json
  response.txt
  generated-test.java
  buggy-test.log
  fixed-test.log
```

`metadata.json` must include `tool`, `model_or_version`, `run_id`, `timestamp`,
`seed_or_temperature`, `source_revision`, and `fixed_revision`.  A generated
test counts as a valid regression test only when it fails on `1b`, passes on
`1f`, and its logs are preserved.  Keep non-compiling or non-discriminating
outputs as negative evidence rather than silently omitting them.
