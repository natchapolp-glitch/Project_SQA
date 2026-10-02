# Beam local JSON boundary — proposal v1

For the 2026-10-03 plan. This is a worker-side proposal for Aom/Champ to review,
not an implemented queue contract. No server, lease mutation, remote artifact
upload, quota account switching or KKU requests are implemented here.

Job JSON contains exactly:

```json
{
  "schema_version": 1,
  "run_id": "api854-pilot",
  "project": "Lang",
  "bug_id": 4,
  "approach": "cmaes",
  "protocol_hash": "<SHA-256 of exact protocol file bytes>",
  "repeat_index": 1,
  "attempt_id": "attempt-1"
}
```

Approaches: `cmaes`, `fscs-art`, `kku-claude`, `kku-gemini`. IDs use ASCII letters,
digits, underscores and hyphens; no paths, credentials or auth headers in jobs.
Retries retain the same job key and need a new immutable attempt ID. Across
machines, the queue owner must allocate attempts and enforce leases. Local
directory exclusivity is not a distributed lease mechanism.

Protocol inputs implemented by this worker: `schema_version=1`,
`defects4j_version=3.0.1`, `timezone=America/Los_Angeles`, nonnegative `seed`,
positive `budget`/`test_method_cap`, positive command/observation timeouts,
`target_selection=shared-declaration-signatures-v1`, `compatibility_policy=none`,
and the worker dependency hashes from `common.implementation_hashes()` in
`source_sha256`. A frozen team protocol may also pin Champ/Aom's modules; every
Beam dependency must match, while unrelated owner modules need not be installed
on Beam's machine.
The proposal command pins current local implementation bytes. Aom must integrate
AI model IDs/prompt/generation policy and coordinate Gate A/B acceptance before
primary pilot or batch use. Do not treat the example protocol as team-approved.

Artifact layout:

```text
<results-root>/<run_id>/<protocol_hash>/<project>/<bug_id>/<approach>/<attempt_id>/
  generation/job.json, environment/, setup/, suite/, result.json
  evaluation/job.json, lineage.json, input-suite.tar.bz2, environment/, setup/,
             measurement/{record.json,fixed-1/,fixed-2/,buggy/,coverage/}, result.json
  semantic-review/review.json
```

Both worker stages also retain `implementation/` containing the frozen runner,
probe and algorithm source bytes with a hash manifest. The worker verifies those
hashes before retaining sources and after generation/evaluation; source changes
during an attempt cannot be published as the old frozen implementation.

`result.json` retains the job, observed outcome, source/suite lineage and file
hashes. It always sets `queue_published=false`. Paths in the underlying legacy
record are local paths. Aom's uploader must retain the manifest and translate
references into shared artifact URIs before atomic stage advance; workers must
never send another machine a local pathname as if it were an accessible URI.

Generation result required for evaluation: identical job key (run/project/bug/
approach/protocol/repeat), with its original generation `attempt_id` preserved,
`observed_outcome=generated`, `suite_sha256`, `test_count`, and
`fixed_source_sha256` mapping checkout-relative modified fixed source filenames
to SHA-256. The evaluator copies the suite once and compares freshly checked-out
fixed sources to this mapping before execution. Champ's suite extraction must
produce this lineage without changing assertions/fixtures or inventing provenance.
This worker applies no compatibility edits or suite pruning.
Queue schema 1.0 allocates a distinct attempt ID at each stage. This is accepted
without rewriting the generation lineage; it does not authorize regenerating AI
or retrying evaluations beyond the protocol limit.

Normalized outcomes include `generated`, `generation_failed`, `preflight_failed`,
`complete`, `compile_failed`, `fixed_failed`, `coverage_failed`,
`environment_failed`, `timeout`. They are stage outcomes, separate from queue
states. `preflight_failed` with `evaluation_attempted=false` is an actual setup
failure, not an evaluated suite. Missing runtime or untouched bugs must not be
aggregated as evaluated failures. `measurement.status` retains the legacy raw
`invalid/partial/failed/complete`; partial measured detection is retained when
coverage fails. A CPU slot rejection creates no attempt: defer the job.

`complete` does not imply `usable`. No semantic review means `usable=false`.
Post-evaluation review is an additional immutable artifact bound to the evaluation
result hash. It records reviewer, source/suite hashes, fixture/oracle reasoning,
weak oracles, and evidence-backed executed/skipped/target-check counts for fixed-1,
fixed-2, buggy and coverage. Each evidence reference is `{ "path": "relative path
inside evaluation", "sha256": "..." }`. Do not invent counts when the runtime
does not expose them; retain pending review and add instrumentation approved by
the team. A Closure compiler-null early-return suite fails semantic review even
if the legacy test command reports zero failures. Source/control-flow inspection
is allowed as target execution evidence, but it must substantiate those counts.

Queue integration checklist for Aom: claim → check/renew lease → stage worker →
retain/upload artifacts → check lease token/version → atomic publish/advance.
Interrupted worker directories are retained; reconciliation decides whether to
retry with the same suite and a new attempt. There is no automatic retry or API
regeneration in these workers. Evaluation infrastructure retries require the
original suite/source hashes and the protocol's retry limit (one); the future
queue adapter must enforce that across attempts.
