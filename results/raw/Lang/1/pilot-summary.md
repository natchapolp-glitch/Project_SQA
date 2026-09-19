# Lang-1 smoke-pilot result

Run date: 2026-09-19  
Project/bug: Defects4J `Lang-1` / LANG-747  
Target: `NumberUtils.createNumber(String)`  
Configuration: `experiments/configs/lang1-smoke.yaml`

## Baseline

| Revision | Result |
| --- | --- |
| `1b` | 1 failing test: `NumberUtilsTest::TestLang747` |
| `1f` | 0 failing tests |

Raw compile/test logs are in `baseline/1b/` and `baseline/1f/`.  Metadata for
both revisions is in `metadata/`.

## Differential input generation

Inputs encode a hexadecimal string with a variable leading-zero count and
suffix digit count.  The same oracle invokes `NumberUtils.createNumber` in both
revisions and compares the returned number class/value or exception outcome.

| Generator | Seed | Budget | Observed b/f differences |
| --- | ---: | ---: | ---: |
| FSCS-ART | 2026 | 30 | 21 |
| CMA-ES | 2026 | 30 | 16 |

For example, input `0x0000000000000000001` produces `BigInteger:1` on `1b`
and `Integer:1` on `1f`.  The raw inputs, comparisons, and summary JSON/CSV
files are retained in `differential/`.

This is a smoke-pilot observation, not a statistical conclusion or a completed
all-project comparison.  AI-tool results are intentionally absent until they
are generated with authorised tool access and recorded under `ai-tests/`.
