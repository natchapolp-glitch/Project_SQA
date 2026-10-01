# Additional test rounds — 1 October 2026

Current completed runs: **142/204**; remaining **62**. KKU Claude successful projects: **9/17**. Both new requests used the exact original prepared prompt via KKU IntelSphere, account natchapol.p, model claude-haiku-latest. No evaluation logs were sent to AI.

- Lang/102: 30 runnable methods; fixed tests passed twice; buggy tests passed; coverage 42/380 lines and 8/350 branches. Raw complete prefix contains 166 methods; retain first 30 in source order under the original budget. Raw answer was truncated. v40 discards the unfinished final method and closes the class without creating/changing assertions. Policy was recorded before evaluation. An independent v2 audit reconstructs the exact hash-identified prefix and checks its source lineage.
- Math/102: 29 runnable methods; fixed tests passed twice; buggy tests passed; coverage 77/429 lines and 29/200 branches. First complete class contains 114 methods, capped at 30; one method failed on fixed and was removed under the existing fixed-only pruning policy. Failed attempt and original response are preserved. Incomplete second class was excluded.

These runs add **59 evaluated methods**. Neither detects the selected bug. Coverage remains limited; these results cannot prove bug absence, establish a winner, or validate all project bugs.

Claude family completed-record audit: 6/6 pass, zero issues. Baseline and Gemini audits retain their prior results. Current provenance audit checks 154 records and still reports 26 historical image gaps from the public clone. Both new captures have real screenshots. Claude quota observed: 99.1% used.

The previous PDF and full ZIP are the **140-run checkpoint**. This document, extra-runs-142.json, summary.json and this supplemental ZIP are the latest 142-run update. Final report/slides still need integration. No GitHub push or Classroom submission was performed.
