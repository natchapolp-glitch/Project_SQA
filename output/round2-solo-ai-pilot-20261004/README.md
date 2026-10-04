# KKU pilot checkpoint — 2026-10-04

Branch `Team`; operator performs all roles. Parent offline evidence remains unchanged at
`../round2-solo-pilot-20261004`. This is a pilot checkpoint, not a completed 854-bug submission.

Canonical combined counts: [combined_summary.json](Report/data/combined_summary.json),
[combined_comparison.csv](Report/data/combined_comparison.csv). The parent's `summary.json`
continues to describe its six offline-only outcomes; do not add the two summaries together.

| Case | CMA-ES | FSCS-ART | Sonnet 5 | Gemini 3.5 Flash Lite |
|---|---|---|---|---|
| Csv-1 | DONE | DONE | DONE | DONE |
| Lang-1 | DONE | DONE | OUTPUT_INCOMPLETE | DONE |
| Math-1 | DONE | DONE | QUOTA_PAUSED | DONE |

Combined: 12 attempted jobs, 10 evaluated/DONE, 1 incomplete output, 1 quota guard pause,
3,404 not started. Only Csv-1 has four completed evaluations. All 10 completed suites
passed fixed twice and did not detect a buggy-revision fault in their tested domains.

Four completed AI suites each executed 12 test methods in each of fixed-1/fixed-2/buggy/coverage.
The six algorithm suites each executed 30. Different suite sizes and different oracle
construction prevent interpreting these small pilot results as a controlled superiority claim.
AI skipped/target-check counters are unavailable; they remain null, not fabricated zeros.
Actual AI started-test events are verified using Defects4J's `Formatter.startTest` ledger.

The protocol is `Experiment/protocol/ai.json`, SHA-256
`f4e01a246a10a9dff206df55a3e02c4a4d5596ccd4f54d8a734de00f71841b00`.
Both models receive the same buggy-only context and templates/settings. P01 plans,
P02 generates at most 12 methods, P03 permits one compile/fixed repair, and P04 adds
at most four methods without changing valid base files. No buggy results enter a prompt.
Fixed coverage determines extension; rejected additions and original files remain available.

Specific observations:

- Sonnet Lang P02 stopped at `max_tokens` (4,096); incomplete source was not evaluated or retried.
- Sonnet Math P01 completed, then P02 was not sent: remaining 30,455 below the declared
  conservative operator guard 32,723. This is not a provider report of zero quota.
- Gemini Csv P03 repaired fixed validation; its accepted P04 added one method (12 final).
- Gemini Lang P04 failed fixed validation; the unchanged 12-method base was retained.
- Gemini Math P03 repaired fixed validation; P04 failed compile/evaluation, so the unchanged
  repaired 12-method base was retained.
- No unbounded repair, account switch within a job, model substitution, peer verdict,
  old queue mutation, or Gate A claim occurred.

16 generation requests confirmed: Sonnet 5 requests, Gemini 11. Sum of reported input/output
components was Sonnet 34,363 and Gemini 58,300 tokens. Sonnet does not return `total_tokens`;
these are explicit sums of known components, not a provider-reported total.
Last observed `a01` remaining: Sonnet 30,455/200,000; Gemini 206,124/350,000.
These are observations at response time, not current guarantees or combined quota across keys.
Precise reset time, distinct buckets, effective settings and request limits remain unverified.

The ten supplied keys are kept only in ignored local private storage. Public preflight
`../round2-solo-provider-20261004` shows exact model availability for aliases a01–a10,
not proof of ten accounts or ten independent quota buckets. Do not package credentials.

Commands on the original Ubuntu WSL host, from repository root:

```bash
python3 -B scripts/study/solo_ai.py run \
  --output output/round2-solo-ai-pilot-20261004 \
  --secrets .local/kku/accounts.private.json --account a01 \
  --cases Csv-1 Lang-1 Math-1
python3 -B scripts/study/solo_results.py \
  --offline output/round2-solo-pilot-20261004 \
  --ai output/round2-solo-ai-pilot-20261004 \
  --destination output/round2-solo-ai-pilot-20261004/Report/data
```

Completed and paused outcomes are sealed. The resume command reuses all six outcomes,
including failures/pauses, and sends no requests. It does **not** continue Math Sonnet after
a reset or automatically switch keys. A continuation must explicitly record its new
condition, selected account and predecessor, preserving this checkpoint and counting
one case/method job with separate stages/attempts rather than inventing extra completed bugs.
Do not repeat Lang until an explicitly separate prospective condition is selected.

Binding/archive/source/count verification plus actual resume proof is recorded in
`Experiment/diagnostics/ai-acceptance/receipt.json` (192 started-test events in four final
AI suites × four stages; zero files changed on resume). These are automated acceptance
checks and operator source review, not an independent peer review. Fresh machines need
their own worktrees/runtime preparation; the private keys and local compiled artifacts
are not included in Git.

Next step: prospectively define a 10–20-bug batch using remaining authorized keys, fix
response-length/account-admission handling based on these observations, and measure its
time/usage before scaling. Preserve these pilot failures and report all pending jobs.
