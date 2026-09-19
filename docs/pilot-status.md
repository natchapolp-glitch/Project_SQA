# Delivery status and scope boundary

The reproducible pilot uses Defects4J `Lang-1` (Apache Commons Lang,
`NumberUtils.createNumber`) because the supplied Round 1 report and active-bug
table identify it as an active candidate.  The setup, metadata exports, baseline
logs, FSCS-ART/CMA-ES implementations, and a b/f differential oracle are part
of this repository.

This is deliberately labelled a **pilot**.  The assignment asks for all Java
projects in Defects4J, while the supplied Round 1 report describes a subset.
No document in this repository should describe the pilot as the complete
all-project study.  The full run requires a declared project/bug sampling rule,
equivalent AI-tool access, and a recorded compute budget before aggregation.

For every selected bug, the minimum evidence checklist is:

1. `defects4j checkout` metadata for both buggy and fixed revisions.
2. A baseline test log for both revisions.
3. Raw generated inputs/tests and deterministic generator configuration.
4. The same test or oracle result on both revisions.
5. A result row that links to the preceding artefacts, including failures.
