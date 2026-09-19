# Lang-1 corrected smoke-pilot result

Configuration: `experiments/configs/lang1-smoke.yaml` executed by
`scripts/run/run-lang1-smoke.py` on 2026-09-19.

| Generator | Seed | SUT executions | Unique inputs | Unique b/f differences | JUnit result |
| --- | ---: | ---: | ---: | ---: | --- |
| FSCS-ART | 2026 | 30 | 30 | 21 | fails on `1b`, passes on `1f` |
| CMA-ES | 2026 | 30 | 27 | 15 | fails on `1b`, passes on `1f` |

`difference_rows` is retained as diagnostic data only.  The comparison uses
`unique_differences` for test-count reporting, so repeated CMA-ES inputs do not
inflate the result.  CMA-ES uses a Lang-1 boundary-targeting heuristic; these
figures validate the workflow and do not establish a general performance ranking.

For each generator, `regression-test/GeneratedLang1RegressionTest.java` was
derived from an observed fixed-revision value and compiled independently against
both revisions.  `validation.env` records `buggy_exit=1` and `fixed_exit=0`.
