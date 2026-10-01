# Gemini continuation until KKU daily limit — 1 October 2026

Used KKU IntelSphere, account natchapol.p@kkumail.com, explicitly selected
Gemini / gemini-flash. Only original prepared prompts were submitted. No
evaluation logs were sent. Claude remained paused.

The final JxPath/103 response completed while the native UI displayed
**Gemini reached daily usage limit** and removed the message input. No further
prompts were submitted. Evidence is in
`ai-tests/provider-captures/kku-only-20261001/gemini/JxPath-1/s103-i1/`:
`response.md`, `operator-metadata.json`, `quota-limit-observation.json` and the
local private `provider-screen.jpg` / `provider-screen.png`.

## Completed results added in this continuation

- Mockito/101: 17 retained methods; fault not detected. The request began in an
  earlier turn; its generation time includes the intervening conversation and
  is an observation upper bound, unsuitable for compute-speed comparison.
- Math/103: 27 methods; fault not detected. Missing FastMath import repaired
  from fixed compilation, v44; failed v39 preserved.
- Compress/103: 16 methods; fault not detected. Eleven unavailable constructors
  adapted to the fixed API, v45; failed v39 preserved.
- JacksonDatabind/102: 25 methods; fault detected. Original response had 27
  methods. Two compiler-invalid tests excluded (v46/v48), provider accessor
  adapted (v47), checked exceptions declared (v49). Retained assertions stayed
  unchanged. Failed v39/v46/v47/v48 preserved. The final raw-count field counts
  methods after these compiler exclusions; it is not the original response count.
- JacksonCore/102: 19 methods; fault detected.
- JacksonCore/103: 23 methods; fault detected.
- Gson/103: 19 methods; fault detected.
- Lang/103: 29 methods; fault detected.
- Time/103: 23 methods; fault not detected. Ambiguous null constructor argument
  cast to DateTimeFieldType[], v50; failed v39 preserved.
- JxPath/102: 23 methods; fault not detected.
- JxPath/103: 18 methods; fault not detected.

These are 11 added successful identities and 239 retained test methods, each
with two passing fixed executions, a buggy execution and coverage evidence.
Count each method once, not once per execution. Source-order budget capping and
fixed-only pruning are disclosed by each record and processing hash chain.

Compress/102 was captured but truncated mid-method and yielded no closed Java
class. Its status remains generation_failed with null measurement fields;
original response and screenshot retained. It is not counted as successful.

## Remaining work

The planned experiment remains incomplete. After the final execution audit and
status refresh, expected totals are 156/204: CMA-ES 51/51, FSCS-ART 51/51,
KKU Claude 9/51 and KKU Gemini 45/51. Use `summary.json` and matching audit
record hashes as the authoritative checkpoint.

Gemini identities still incomplete: Cli/101, Cli/102, Closure/102, Closure/103,
Gson/102 and Compress/102. Claude has 42 planned identities incomplete and
successful results for 9 of 17 projects. The 204 identities are the team study
plan, not a stated minimum in the assignment PDF.

The report PDF and slide deck remain earlier checkpoints and require updating.
Classroom submission has not been performed. This receipt and local files do
not establish submission or complete rubric compliance.

Public Git excludes private provider screenshots. The historical 26 missing
screenshots from the public checkout remain disclosed; new captures have local
screenshots. Execution and provenance audits have different scopes. Do not
describe the full provenance audit as passed when these historical files are
absent.

## Reproduction and inspection

Run `python3 scripts/study/kku_only.py status` after the family execution audits.
Inspect `output/kku-only-20261001/gemini-evidence-audit.json` and
`provenance-audit-current.json`, then follow record paths in `study-manifest.csv`.
Each successful record includes the exact evaluator version, processing source
hashes, input archive hashes, edit history and original capture path.
Processing policy receipts v44–v50 describe the compatibility changes above;
their prose receipts were written after evaluation, while source hashes were
recorded before execution. All failure logs remained local.
