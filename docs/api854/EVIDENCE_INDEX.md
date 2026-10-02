# API854 evidence index — preparation checkpoint

This index describes evidence actually created during Aom's foundation work.
No live KKU request or primary Defects4J experiment is represented by the queue preparation.

- `experiments/configs/api854-20261003/installed-active-bugs.json`: actual installed 3.0.1 pids/bids export, installation revision/version hash.
- `experiments/configs/api854-20261003/bugs.json`: exact 854 active-pair comparison, canonical inventory hash and input file hashes.
- `experiments/configs/api854-20261003/ownership.json`: confirmed allocation, unchanged; Aom 284 bugs from 17 projects.
- `experiments/configs/api854-20261003/jobs.json`: 3,416 deterministic unique identities; prospective allocation, not results.
- `experiments/configs/api854-20261003/pilot20.json`: preregistered pilot IDs; context breadth still needs Beam's review.
- `experiments/configs/api854-20261003/protocol.json`: draft policies, unknown KKU IDs/settings explicit as null; not a gate certificate.
- `experiments/configs/api854-20261003/record.schema.json`: exchange format; runtime checks live artifacts, leases and lineage separately.
- `scripts/study/api854/tests/`: synthetic concurrency/crash/failure/gate/model/HTTP/freeze checks, not live API evidence.
- `output/api854-20261003/preparation/`: snapshot-derived preparation report; 0 attempted, 0 terminal, all 3,416 keys not_attempted.
- `.local/api854/`: ignored runtime and preparation snapshot. Not included in Git or delivery ZIP as a database.

Final live artifact layout is job/attempt/hash on the queue server, with authenticated portable download URI.
The final ZIP maps each attempted record's artifact to `evidence/<attempt_id>/<index>-<name>`.
`SHA256SUMS.json` stores exact payload hashes and canonical snapshot_hash; ZIP checksum is a separate sidecar.
Raw responses, processed suites, fixed/buggy logs, coverage and semantic-validity evidence remain distinct artifacts.
Do not include KKU keys/auth headers/emails, browser account screenshots or unrelated historical captures.

Outstanding evidence: exact model/settings mapping, quota/reset/notification tests and live pipeline from Champ;
adapter/environment/semantic-validity evidence from Beam; Gate A/B three-owner signatures and observed pilot metrics.
