# Current project status — 2 October 2026

The KKU-only primary study has **185/204 completed runs** and 19 pending. CMA-ES, FSCS-ART and KKU Gemini are each 51/51; KKU Claude is 32/51. Strict Haiku-only primary total: 180/204. The latest JacksonCore run contributed 29 evaluated test methods; a subsequent Chart request was refused and remains pending.

The completed primary runs contain 4,862 retained test methods (repeated scenarios may count more than once). Clarification runs for Time, JxPath and Collections are separate and are not included in the primary total. See `docs/ACCOUNT_UPDATE_20261002.md`, `summary.json`, `pending-runs.csv` and the audit receipts under `output/kku-only-20261001/` for current evidence.

The local evidence package is `output/SQA_Round2_17Bugs_20261002_ACCOUNT_UPDATE_V5_Evidence.zip`. Existing report markdown, PDF/PPTX, analysis.json and delivery-status.json files describe earlier checkpoints. The 850-bug experiment has not been run, the primary study is incomplete, and Classroom has not been submitted. Private provider screenshots are included in the local ZIP and intentionally excluded from public Git.

# SQA Project 2.2 — CP353201

## Current continuation: Claude and Gemini through KKU only

The owner changed the AI constraint on 1 October 2026. New prompts go only to
https://gen.ai.kku.ac.th/chat with the explicitly selected Claude or Gemini agent.
The primary comparison is now CMA-ES, FSCS-ART, KKU Claude and KKU Gemini.
The original study below remains historical evidence and uses a different AI scope.

Current evidence and deliverables are in `output/kku-only-20261001/`. Read
`summary.json`, `analysis.json`, `pending-runs.csv` and the evidence audits for
exact completion counts. The experiment remains incomplete; failed and missing
measurements stay null. See `docs/KKU_ONLY_CONTINUATION.md` for the protocol,
local processing versions, commands and runtime paths. The new report/deck
do not replace or reinterpret the historical report/deck as KKU-only results.

Run `python3 scripts/study/kku_only.py status` and
`python3 scripts/reporting/audit_kku_provenance_v2.py` to refresh/check the linked
manifest. Completed fresh suites also require `audit_kku_only_v4.py --results
results/study/kku-only-20261001/<family> --output
output/kku-only-20261001/<family>-evidence-audit.json`. Reused original records
retain their actual provider/model labels and hashes. AI results are assisted
by disclosed local processing, with failed attempts preserved.

`delivery-status.json` records local packaging and remote submission status.
Private provider screenshots can contain unrelated chat titles and are omitted
from public Git publication. The local evidence package retains originals.
Auditing the complete local package requires those private evidence files.

This repository contains the experiment code and evidence for comparing
CMA-ES, FSCS-ART, Claude, and KKU IntelliSphere on Defects4J.

## Submission status

The package is **incomplete**. The manifest plans 204 runs across 17 Defects4J
projects (one lowest-numbered active bug per project, four methods, three run
indices, and budget 30). The current repaired batch is
`results/study/round2-v4-20260929/`; exact completion counts are generated in
`output/submission/summary.json`. Ubuntu, Java 11 and Defects4J are available.
The installed WSL distribution is named `Ubuntu`; an earlier lookup using
`Ubuntu-24.04` was incorrect. Actual Claude and IntelliSphere responses are now
being collected; `summary.json` and `ai-provider-attempts.csv` show current counts.

The two algorithm cohorts are now complete: **102/102 final runs**, 51 per
method, across all 17 selected project/bug pairs. The final evidence audit
checks completed records against archives, raw logs, counters and source hashes.
This includes the documented Cli dependency repair and one JxPath fixed-validation
follow-up. AI results include both successful evaluations and failures; consult
the generated summary and checklist for exact totals rather than assuming every
captured response produced a valid test suite.

AI suites use disclosed local processing: a source-order cap at 30 test methods,
limited IntelSphere UI suffix cleanup, exact filename hint removal when needed,
at most two fixed-only method pruning follow-ups, and disclosed fixed-API/JUnit
compatibility repairs in v4/v5. The v6/v7 local processors salvage only complete
methods before a truncated UI response and apply recorded parser/compiler fixes
in v6-v29, including disclosed fixed-version fixture and reflection repairs.
Raw responses and all failed attempts are preserved. These are
**AI-assisted tests with local processing**, not unedited model output. Only the
original prepared prompts are sent to providers; evaluation logs stay local.
IntelSphere uses Auto Router and explicitly selected Gemini/OpenAI agents; resolved models vary and are recorded per attempt. AI generation timing
is an observation upper bound through the UI and cannot rank provider compute
speed against algorithm timing. See `docs/ai-workflow.md` for the complete policy.

The assignment requests development and comparison on all listed Java projects.
This plan selects one active bug in each of the 17 project identities. Its
findings describe this selected sample and do not cover every Defects4J bug.
The PDF does not specify a required number of bugs per project; any additional
scope assigned by the instructor should be applied separately.

## Evidence and deliverables

- `output/submission/SQA_Round2_Report.pdf` — formatted report from the current
  evidence; still a draft because the experiment is incomplete.
- `output/submission/report.md` — source report; `summary.json`, CSV tables,
  and the run records retain the underlying counts and provenance.
- `presentation/SQA_Round2.pptx` — editable 17-slide presentation with current
  status and limitations.
- `presentation/demo-guide.md` — demo steps and commands.
- `docs/study-protocol.md` — scope, methods, validation rules, and metrics.
- `docs/submission-checklist.md` — rubric crosswalk and outstanding items.
- `docs/ai-workflow.md` and `prompts/round2-unit-test.md` — AI prompt and the
  workflow for preserving actual provider responses.
- `results/study/round2-v4-20260929/` — current shared-API prospective run.
- `results/validation/` — development checks kept outside the study results.
- `docs/lessons-learned.md` — concrete problems, fixes, and evidence paths.
- `output/SQA_Round2_Submission.zip` — local package of source and evidence;
  includes a file manifest with SHA-256 checksums.

## Reproduction

The study runner requires Defects4J 3.0.1 and Java 11 in Linux/WSL. The prepared
environment is accessible with `wsl.exe -d Ubuntu`. Inventory projects and bugs:

```bash
python3 scripts/study/run.py inventory --d4j "$D4J_HOME/framework/bin/defects4j"
```

Start a new run ID whenever the source or configuration changes:

```bash
python3 scripts/study/run.py run --d4j "$D4J_HOME/framework/bin/defects4j" \
  --worktrees "$HOME/sqa-round2/worktrees" --run-id NEW_RUN_ID
```

After runs finish, regenerate the aggregate report and PDF:

```bash
python3 -m pip install -r scripts/reporting/requirements.txt
python3 scripts/reporting/aggregate.py --results results/study/round2-v4-20260929 \
  --manifest results/study/round2-v4-20260929/study-manifest.csv
python3 scripts/reporting/audit_evidence.py
python3 scripts/reporting/build_figures.py
python3 scripts/reporting/build_report.py
```

The presentation also needs the bundled `@oai/artifact-tool` runtime; see
`docs/reporting.md` for the exact environment variables and command. Preserve
all actual AI responses, model names, prompts, generated tests, and evaluation
logs. Never replace missing provider evidence with locally generated tests.

The v4 batch explicitly records a Cli framework JUnit/Hamcrest dependency repair
and a JxPath fixed-only assertion pruning follow-up. Original failures remain
under results/validation. See `docs/reporting.md` and `docs/study-protocol.md`;
these are reported repairs, not claims that every first attempt passed.

After refreshing and reviewing all deliverables, run
`python3 scripts/reporting/package_submission.py` to build the local ZIP and its
SHA-256 checksum. The ZIP includes an inventory with a checksum for every file.
Packaging does not submit work to GitHub or Classroom.

## Team

- นายธนินธร อันทรบุตร — 673380043-6
- นายศุภกร กรมรินทร์ — 673380061-4
- นายณัชพล เพ็งพล — 673380267-4
- นายณัฐกรณ์ อินธิสาร — 673380268-2

Advisor: ผศ. ดร.ชิตสุธา สุ่มเล็ก, Khon Kaen University.

### Counting test cases

See `output/submission/test-case-counts.csv` for retained declared methods in completed primary suites, separate from run counts and incomplete suite methods. Counts sum across projects and run indices; repeated scenarios are counted. Pilot and validation histories are excluded.
# Local continuation checkpoint — natchapol.p

The updated local experiment has 140/204 completed primary runs. See
[`docs/CONTINUATION_NATCHAPOL_P.md`](docs/CONTINUATION_NATCHAPOL_P.md) and
[`output/kku-only-20261001/continuation-natchapol-p.json`](output/kku-only-20261001/continuation-natchapol-p.json).
The current local PDF is `SQA_Round2_KKU_Only_natchapol_p.pdf`; earlier reports,
slides and ZIPs are historical checkpoints. Claude still has successful results
for only 7/17 projects, and no Classroom submission has been made.
