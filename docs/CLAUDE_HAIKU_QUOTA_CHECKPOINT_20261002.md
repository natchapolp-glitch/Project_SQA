# Claude Haiku daily quota checkpoint — 2 October 2026

Requested scope: continue Claude Haiku in KKU IntelSphere until its daily limit.
Account: `supakron.k@kkumail.com`; selected model: `claude-haiku-latest`.
Original prepared prompts only. No test logs, buggy outcomes or coverage feedback sent to the AI.

## Result

- KKU visibly reports **Claude reached daily usage limit**, 100%. No further prompt submitted.
- Overall audited primary completion: **167/204**. Claude **14/51**; Gemini **51/51**; both algorithms **51/51**.
- Four newly completed identities in this continuation; **112 retained methods**. These are team protocol counts, not instructor minimums.

## Completed evaluations

- Cli/103: 27 retained methods; fault detected False; lines 42/45; branches 12/16.
- Codec/103: 29 retained methods; fault detected False; lines 69/256; branches 6/184.
- Jsoup/102: 26 retained methods; fault detected False; lines 39/46; branches 14/18.
- Csv/102: 30 retained methods; fault detected True; lines 37/37; branches 24/26.

Each completed suite passed twice on fixed, ran on buggy and produced coverage. Audit v4 checks logs, counters, archive bytes, processing lineage and source freeze.
Fresh Claude audit: 11/11 completed records pass; zero issues.

## Processing and unsuccessful attempts

- Cli/103: original 48 methods. v54 fails compilation on unavailable Option.setValue. v57 excludes 15 whole methods containing that API, caps remaining 33 at 30 and removes three fixed assertion failures. Final 27. Failed attempts preserved; surviving assertions unchanged.
- Codec/103: 97 methods in closed Java fences. Cap 30, remove one fixed failure; final 29. Incomplete trailing SoundexUtils code block omitted rather than fabricated.
- Jsoup/102: original 31 methods. v54 fails on unavailable getChildNodes; v58 fails on private normalise(Element). v59 excludes the two entire methods, then removes three fixed failures; final 26. Failed attempts preserved.
- Csv/102: 31 methods, source-order cap 30. Detects selected bug. No local assertion edits.
- Closure/101, Chart/102, Collections/102, Gson/102, Math/103 and JacksonCore/102: native responses captured, but no runnable Java suite. Reported as generation failures; missing coverage stays null.
- Closure prompt was prepared before a long idle interval. Its reported generation duration is a UI observation upper bound containing that delay, not model latency.
- Model quota changed from previous 85.1% to 14.6% after Closure, then reached 100% on Csv. Exact reset time is unknown.
- Academic context clarification remains unsubmitted; original-prompt constraint retained.

## Evidence and remaining work

- Quota evidence: `ai-tests/provider-captures/kku-only-20261001/claude/Csv-1/s102-i1-supakron-haiku-20261002/provider-screen.png` and `quota-limit-observation.json`.
- Per-run raw exports, model/account metadata, source-processing snapshots, failed attempts, fixed/buggy logs and coverage retained locally.
- **37 Claude planned identities incomplete**. Historical provenance gaps remain; the provenance audit is not fully passing.
- Report PDF remains checkpoint 140 and slides checkpoint 136. Final report/slides/ZIP and Classroom submission remain unfinished.
- Latest continuation is local and unpushed. Last published commit: 846fb43c4a3daa8f48febef0da7b559186086acb.
