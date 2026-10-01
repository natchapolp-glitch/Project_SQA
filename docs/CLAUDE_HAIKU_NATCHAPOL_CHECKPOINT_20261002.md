# Claude Haiku natchapol checkpoint — 2 October 2026

Account: natchapol.p@kkumail.com. KKU IntelSphere, claude-haiku-latest. Original prepared prompts only; no evaluation logs or clarification sent.

- Overall audited completion: **174/204**. Claude **21/51**; Gemini, CMA-ES and FSCS-ART **51/51** each. **30 Claude identities remain incomplete**.
- Five new completed evaluations add **127 retained test methods**.
- Claude reached 100% daily usage on Closure/102. No further prompt submitted.
- Fresh Claude evidence audit: 18/18 complete records passed, zero issues. Three reused baseline records bring the Claude total to 21.

- Compress/103: 11 methods; fault detected False; lines 134/165; branches 34/59.
- Jsoup/103: 27 methods; fault detected False; lines 39/46; branches 14/18.
- Time/103: 30 methods; fault detected False; lines 39/305; branches 9/122.
- Codec/101: 29 methods; fault detected False; lines 69/256; branches 6/184.
- Cli/102: 30 methods; fault detected True; lines 44/45; branches 13/16.

## Processing and limitations

- Compress: unsupported constructor methods excluded using fixed compile diagnostics; surviving assertions unchanged. Version 63 driver with version 61 compatibility processing.
- Jsoup: filename header removed; unsupported selectFirst/hasParent methods excluded using fixed compile diagnostics. Version 65.
- Cli: unsupported setValue methods excluded using fixed compile diagnostics. Version 66. Detects the selected bug.
- Time and Codec: closed Java fences only, source-order cap 30. Codec removes one fixed assertion failure; no assertion expectations changed.
- Operator repeats: Lang/102 and Math/102 were already complete. Native exports retained as additional captures; evaluator refused overwrite. Neither counted as a new primary run.
- Closure/102 asked for additional context and produced no Java tests. Preserved as generation_failed; missing measurements remain null.
- Response Style was Explanatory on this account, whereas earlier sessions used Normal. This UI setting difference is disclosed and cannot be treated as an identical configuration.
- Generation timing includes UI preparation and waiting, an observed upper bound rather than pure model latency.
- Historical provenance gaps remain; the entire provenance audit does not pass.
- Report PDF remains checkpoint 140; presentation checkpoint 136. Final report, slides, package and Classroom submission remain unfinished.
- Continuation is local and unpushed. Last published commit: 846fb43c4a3daa8f48febef0da7b559186086acb.
- Quota evidence: ai-tests/provider-captures/kku-only-20261001/claude/Closure-1/s102-i1-natchapol-haiku-20261002/provider-screen.png and quota-limit-observation.json.
