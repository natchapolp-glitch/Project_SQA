# Shared runtime and inputs

Frozen 41-file runtime from generation v12; inputs and benchmark source bindings are unchanged.
The embedded helper is needed to generate reflection-based tests. Archives already embed their required helper.
Regenerate algorithms: `python3 Shared/regenerate-algorithms.py --d4j /path/to/defects4j --worktrees /path/to/worktrees --output /fresh/output`.
New generated suites are separate replays and are never silently substituted into the reported table.
