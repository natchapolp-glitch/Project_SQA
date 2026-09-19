# AI-generated-test evidence layout

The repository does not contain credentials for Claude or IntelliSphere, so no
AI-generated test is fabricated here.  When an authorised operator runs either
tool, retain the exact prompt, tool/model version, date, raw response, generated
source, and b/f test logs under:

```text
ai-tests/<tool>/Lang-1/<run-id>/
  prompt.md
  metadata.json
  response.txt
  generated-test.java
  buggy-test.log
  fixed-test.log
```

`metadata.json` must include `tool`, `model_or_version`, `run_id`, `timestamp`,
`seed_or_temperature`, `source_revision`, and `fixed_revision`.  A generated
test counts as a valid regression test only when it fails on `1b`, passes on
`1f`, and its logs are preserved.  Keep non-compiling or non-discriminating
outputs as negative evidence rather than silently omitting them.
