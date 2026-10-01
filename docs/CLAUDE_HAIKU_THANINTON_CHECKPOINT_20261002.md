# Claude Haiku — thaninton.u checkpoint, 2 October 2026

Account: `thaninton.u@kkumail.com`; KKU IntelSphere, selected `claude-haiku-latest`.
Original prepared prompts only; no execution logs or extra academic clarification sent.

## Result

- Daily limit visibly reached at 100% on Math/101. No subsequent prompt submitted.
- Audited overall completion: **169/204**; Claude **16/51**, Gemini and both algorithms **51/51** each. **35 planned Claude identities remain incomplete**.
- Nine captured requests on this account including Lang/103 from the prior interrupted turn; eight requests in the retry continuation. Two completed evaluations add **58 retained methods**.

- JacksonCore/103: 28 methods; fault detected False; lines 63/403; branches 50/242.
- Csv/103: 30 methods; fault detected True; lines 36/37; branches 22/26.

## Processing and unsuccessful requests

- JacksonCore/103: 74 methods in closed Java fences; incomplete trailing TextBuffer block omitted. First 30 source-order methods selected, two fixed assertion failures removed. Final 28; surviving assertions unchanged.
- Csv/103: 37 original methods; source-order cap 30; no fixed failures pruned. Detects the selected bug.
- Lang/103, Compress/102, Time/102, Chart/103, Collections/103, Gson/103, Math/101: captured refusal or missing dependency response; no Java suite. Evaluator records generation_failed, with no fabricated coverage.
- Successful suites passed twice on fixed, ran on buggy, and produced coverage. Fresh Claude evidence audit: 13/13 complete records pass, zero issues; three reused baseline Claude records bring primary completion to 16.
- Generation duration includes UI preparation and waiting, so it is an observation upper bound rather than pure model latency.

## Evidence and remaining work

- Quota evidence: `ai-tests/provider-captures/kku-only-20261001/claude/Math-1/s101-i1-thaninton-haiku-20261002/provider-screen.png` and `quota-limit-observation.json`.
- Native exports, provider/model/account metadata, processing snapshots, failed fixed attempts, fixed/buggy logs and coverage are preserved locally.
- Historical provenance gaps remain. The entire provenance audit does not pass.
- Report PDF remains checkpoint 140 and slides checkpoint 136; final report/slides/package and Classroom submission remain unfinished.
- Current continuation is local and unpushed; last published commit 846fb43c4a3daa8f48febef0da7b559186086acb.
