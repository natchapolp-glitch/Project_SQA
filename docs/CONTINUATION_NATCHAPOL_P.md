# Checkpoint: natchapol.p, 1 October 2026

Source: branch `test`, commit `bf6086bd03c3082b4108a69fe1dcf5503b0e13e4`.
Work is in `Project_SQA_from_test`; the older local checkout is preserved.

## Current result

- 140/204 planned primary runs completed and execution-audited; 64 remain incomplete.
- CMA-ES and FSCS-ART: 51/51 each, all 17 projects.
- KKU Claude: 7/51, 7 projects. KKU Gemini: 31/51, all 17 projects.
- Nine new original-prompt requests used account `natchapol.p@kkumail.com`.
- Four new successful runs add 82 runnable methods: Codec/Gemini/102 (5),
  Codec/Claude/102 (28), Gson/Claude/101 (23), JacksonXml/Gemini/101 (26).
- Gson/Claude/101 detected the sampled bug. The other three did not.
- Five requests failed to produce runnable suites. Preserve busy, refusal,
  dependency-request and truncation evidence; do not count them as successful tests.

No evaluation feedback or additional context was sent to providers. Exact displayed
model labels and account are in each operator metadata file. Sonnet, Haiku, Gemini
Pro and Flash must be disclosed separately; no uniform model version is inferred.

## Reproducibility fixes

- Restore only CRLF bytes in three algorithm files to match original frozen hashes.
  Before bytes and receipt: `results/validation/frozen-source-eol-recovery-20261001/`.
- v38: one hash-identified Codec local variable reference typo, diagnosed from fixed
  compiler only; assertion unchanged. Source snapshot hashes precede evaluation,
  but the descriptive v38 policy JSON was written afterwards and says so.
- v39: advance JacksonXml fixture reader with `nextTag()` to satisfy the fixed
  constructor's START_ELEMENT precondition. Policy written before reevaluation.
- Preserve v37/v38 failures and fixed-only pruning attempts. Do not modify old processors.

## Evidence and deliverables

- `output/kku-only-20261001/continuation-natchapol-p.json`: nine captures, outcome,
  source/model metadata references and SHA receipts.
- `output/kku-only-20261001/summary.json`, `study-manifest.csv`, `pending-runs.csv`:
  current authoritative status.
- `SQA_Round2_KKU_Only_natchapol_p.pdf`: current eight-page report, 140/204.
  Older PDF/PPTX/ZIP versions are historical checkpoints, not updated submission files.
- Baseline execution audit: 171/171. New Claude audit: 4/4. New Gemini audit: 19/19,
  including the retained secondary duplicate. All have zero execution audit issues.
- `provenance-audit-current.json`: 26 unresolved image gaps in the public checkout
  (25 historical provider screenshots and one incident screenshot). Public Git omits
  these deliberately. Obtain the original private evidence package to verify them;
  do not recreate screenshots or claim this checkout contains them. All nine new
  captures have their actual screenshots.

## Remaining work

Claude lacks successful coverage of ten projects. The assignment requires all Java
projects, so the task cannot yet be called complete. The 204-run count is the team's
plan, not an instructor-mandated number.

A request to authorize revised wording/chunking of the same original context is
pending. No revised prompt has been sent. Compilation/buggy/coverage logs remain
prohibited for provider transmission. Preserve the original cohort if a new prompt
version is authorized, and disclose comparisons by prompt/model/version.

The updated work is local and has not been pushed. The Classroom assignment URL and
exact deadline time are still unknown; no Classroom submission has been made.
Update slides, rerun the demo and package final evidence after the experiment is settled.
