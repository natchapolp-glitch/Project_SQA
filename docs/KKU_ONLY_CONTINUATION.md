Current checkpoint: **181/204 completed, 23 pending**. Use artifacts ending `_20261002_HAIKU` and `docs/SUBMISSION_READY_20261002_HAIKU.md`. Earlier COMPLETE checkpoints below are historical (178 runs). Owner stopped further AI requests and will submit Classroom themselves.

# SQA round 2: KKU-only continuation, 1 October 2026

Current checkpoint: `docs/SUBMISSION_READY_20261002_COMPLETE.md`. Collection stopped at owner request after Claude quota reached 100%; prepare from the captured results. Current deliverables use suffix `_20261002`. Historical checkpoints remain separate.

The owner changed the AI constraint: use Claude and Gemini only through
https://gen.ai.kku.ac.th/chat. The original handoff remains as historical context.
New AI requests explicitly select the Claude or Gemini agent. Never select Auto
Router or another family for new requests.

The four comparison approaches are CMA-ES, FSCS-ART, KKU Claude and KKU Gemini.
Keep 17 projects, one active bug/project, run indices 101/102/103 and budget 30.
This is a team experimental plan, not a numeric requirement from the assignment.

Preserve the original study and all failed attempts. Reuse audited algorithm
records and eligible audited KKU Claude/Gemini records by reference and SHA-256.
Do not rewrite generator/provider/model fields in historical records. Exclude
Claude-direct and other model families from the primary KKU-only comparison.
Historical Auto Router responses are eligible only where the displayed resolved
model is explicitly Claude/Gemini and capture/generation provenance agrees.
Disclose routing, exact model labels, dates, local repairs and reused/new subsets.

Use exact original project prompts. Do not send fixed/compile/buggy/coverage
logs to providers. Keep raw rendered responses, screenshots, request time,
completion observation, URL and displayed model label. Do not invent hidden
parameters, token counts or a resolved version behind a `latest` UI label.

Validate fixed twice, buggy once, then fixed coverage. Failed or missing
measurements stay null. Initial local processing uses the existing immutable
source-order cap/UI cleanup/fixed-only pruning implementation. Any additional
processing requires a disclosed new version and preserved before/after evidence.

Layout:

- `results/study/kku-only-20261001/protocol.json`: scope and rules.
- `results/study/kku-only-20261001/claude/` and `gemini/`: independent evaluation batches.
- `ai-tests/provider-captures/kku-only-20261001/<family>/<Project>-1/s<index>-i1/`: fresh captures.
- `output/kku-only-20261001/`: audit, immutable-record references, current totals and pending queue.

Prepare once after the baseline audit:

```bash
python3 scripts/study/kku_only.py prepare
python3 scripts/study/kku_only.py status
```

Evaluate a fresh capture using the disclosed versioned processor, with the correct
family-specific batch and the new local runtime paths:

```bash
python3 scripts/study/evaluate_provider_normalized_v37.py \
  --tool intellisphere --project Lang --seed 101 \
  --capture ai-tests/provider-captures/kku-only-20261001/claude/Lang-1/s101-i1 \
  --batch results/study/kku-only-20261001/claude \
  --worktrees /home/team/sqa-round2/worktrees \
  --d4j /home/team/sqa-round2/defects4j/framework/bin/defects4j
```

The underlying generator stays `intellisphere`; the separate `approach` field
links it to the chosen Claude/Gemini family. The baseline reporting commands
target the old study and must not be used to claim KKU-only completion.

Versions v30–v37 preserve the older immutable processors. Policies and source
hashes are in `processing-policy-vN.json`. v30 salvages a hash-identified Lang
prefix, v31 casts Time null to String for an overloaded call, v32 salvages Claude
Jsoup, v33 Claude Cli, v34 Gemini Codec, and v35 casts the Collections iterator
to its declared ResettableIterator interface. Assertions are unchanged by these
compatibility steps. v36/v37 explicitly unbox Jsoup Integer sibling indices to
resolve JUnit overload ambiguity, preserving expected values and failed attempts.
Source-order caps and at most two fixed-only pruning passes
remain disclosed. Before any additional processing, preserve old attempts and
create a new version. Never reuse a capture/evaluation directory by overwriting.

Fresh Gemini Time/102 was collected despite an eligible reused Time/102 record.
Keep the audited fresh result as secondary validation. The original remains the
primary regardless of outcome, and the fresh duplicate adds no run/methods to
the 204-row manifest. `summary.json/secondary_new_records` links both immutable
records. Always inspect the pending manifest before selecting another identity.

New service/empty-output failures use immutable failure-recorder versions.
Mockito had no test methods before truncation; Gemini Cli/101 ends before its
first test body. Failure records have zero tests and null execution metrics.
Claude's daily quota reached 100% during Compress/101; Gemini reached 100% after
Jsoup/103. Remaining AI work needs the providers to become available. The UI
did not provide a verified reset time. Check the live UI, not historical times.

An operator routed Math/102 exports to Lang/102 after Lang evaluation. Original
Lang response was recovered byte-for-byte from the evaluator snapshot, then
the persisted Lang chat was re-observed with matching response/source hashes.
Auxiliary screenshot/DOM exports are a later observation. All misrouted files
and recovery details are preserved in
`results/validation/operator-capture-routing-20261001/incident.json`.

After full family audits, refresh status and build descriptive deliverables:

```bash
python3 scripts/study/kku_only.py status
python3 scripts/reporting/audit_kku_provenance_v2.py
python3 scripts/reporting/build_kku_analysis_v3.py
python3 scripts/reporting/render_kku_report.py --source output/kku-only-20261001/report-20261002.md --output output/kku-only-20261001/SQA_Round2_KKU_Only_20261002.pdf
```

The PDF/slide builders use bundled Windows artifact runtimes. See their sources
for paths and environment arguments. The old report/PPTX stay historical.
`analysis.json` describes both successful subsets and groups matched across all
four approaches, with explicit denominators. No general winner is inferred.

Demo receipts in `output/kku-only-20261001/demo/rehearsal.json` document a real
separate-checkout run of the preserved Claude Jsoup suite: fixed has zero failing
tests and buggy has one. It does not create another primary experiment.
Use `presentation/demo-guide-kku-only.md` for the walkthrough.

Public Git omits full provider screenshots because their sidebar can show
unrelated chat history. Available originals remain in the private local evidence
ZIP. All 54 missing primary teammate screenshots have now been received and imported; current provenance receipt has zero issues. Private originals are retained locally and in the new verified evidence package. `.gitattributes`
disables Git EOL conversion so frozen hashes retain their exact bytes.

The owner requested notice before important actions. Announce tool installation,
study/protocol changes, batches of AI submissions and publication before acting.
Final delivery status is in `delivery-status.json`; local artifacts do not prove Classroom submission. The owner confirmed submission before midnight on 2 October 2026 Bangkok time and will submit Classroom themselves. No Classroom destination is needed by this agent. This deadline comes from the owner, not a new claim about the assignment PDF.
