# Aom implementation ledger — plan: docs/superpowers/plans/2026-10-03-sqa854-collaborative-48h.md

Base revision: e85c4587 (aom). Workspace: separate clone Project_SQA_aom; existing champ checkout untouched.
Spec read: SQA_Project_2026 (2).pdf, page 3 section 2.2. Implementation date: 2026-10-03 Asia/Bangkok.

## Completed foundation deliverables

- A1: compare installed Defects4J pids/bids with 854/17 ownership; hash/duplicate checker and deterministic pilot/job manifests.
- A1/A3: draft target/budget/seed/cap/extraction/exclusion/repair policies; semantic changes require separate disclosed condition.
- A2: transactional central HTTP queue, unique keys, FK attempts/reservations, fencing leases, recovery and progress.
- A2: worker artifact upload/download; immutable hashed bytes; typed stage outcomes and source/suite/model provenance gates.
- B preparation: preregistered 20 bugs / 80 jobs; synthetic claim/crash/auth/cutoff/failure/model/freeze checks.
- C preparation: 3,416 held not_attempted jobs, including Aom's 1,136 keys; no live dispatch.
- E preparation: report/contract/evidence/runbook drafts and deterministic verified snapshot packaging tool.

## Interface pre-flight and rulings

- A2 generation/evaluation consume schema; preserve raw artifacts and carry identical suite/source hashes through worker stages.
- Latest plan requires one server queue across machines, replacing the older proposal of network-share SQLite.
- Ruling: independent branch clone because existing checkouts are on other owners' branches with untracked evidence; no user changes overwritten.
- Ruling: .local/api854 state/artifacts ignored; never merge SQLite/credentials.
- Ruling: protocol remains draft, jobs held; no invented exact KKU model/settings or claimed Gate A/B.
- Ruling: no assertion pruning/semantic retry in primary draft; retry infrastructure ≤3 generation / ≤2 evaluation attempts; team must review policy at Gate A.
- Ruling: execution TZ retained from existing runner; schedule Asia/Bangkok. Beam must verify all four workers agree before pilot.
- Ruling: installed version is checked from README/revision because 3.0.1 has no `defects4j version` subcommand.
- Ruling: pilot highest-ID extras are prospective; actual large-context/framework breadth remains a Beam gate requirement.

## Review and verification

Fresh-context review identified portable downloads, retry caps, failure provenance and coverage_failed outcome gaps.
These were implemented with failing-then-passing regression checks, plus same-attempt recovery of existing crash outputs.
Focused queue/HTTP/package tests and existing generator/evaluator/AI/aggregation regression checks were run.
Algorithm tests need PYTHONPATH=algorithms/python; initial run without it failed imports and was corrected.
AI symlink packaging test is skipped on this Windows environment; remaining checks pass.
Exact final counts/commands are recorded in VERIFICATION_TH.md after final verification.

## Outstanding team gates and later work

Gate A: Champ exact request/response model mapping/settings, KKU quota/auth/reset/visible notification and context evidence;
Beam actual adapters/environment/four-method integration and semantic validity checks;
three-owner approval with matching hashes and actual evidence.
Gate B: observed 80-job pilot, token/throughput/validity review; no mock checks substituted.
Then run full cohort/shards, checkpoint real backlog/quota/ETA, resolve blockers, freeze real evidence and final report/slides/demo/ZIP.
No primary API token used. No background automation or live experiment implied by this ledger.
