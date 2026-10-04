"""Build immutable fixed-source preparation proposals, without claiming jobs or calling AI."""
from __future__ import annotations

import argparse
import hashlib
import json
from pathlib import Path

from .context_export import BUILD_FILES, export_context

CONTEXT_POLICY = "aom-fixed-modified-classes-build-v1-proposal"
PROMPT_POLICY = "aom-fixed-junit4-cap30-v1-proposal"


def select_files(worktree: Path, classes: list[str]) -> list[str]:
    selected = []
    for name in classes:
        suffix = name.split("$", 1)[0].replace(".", "/") + ".java"
        matches = [p for p in worktree.rglob(Path(suffix).name)
                   if p.as_posix().endswith("/" + suffix)
                   and not any(x.lower() in {"target", "build", "tests", "test", "generated"}
                               for x in p.relative_to(worktree).parts[:-1])]
        if len(matches) != 1:
            raise ValueError(f"Expected one fixed production source for {name}, got {len(matches)}")
        selected.append(matches[0].relative_to(worktree).as_posix())
    selected.extend(name for name in sorted(BUILD_FILES) if (worktree / name).is_file())
    return sorted(set(selected))


def prepare(worktree: Path, project: str, bug_id: int, classes: list[str], output: Path) -> dict:
    paths = select_files(worktree, classes)
    manifest = export_context(worktree, project, bug_id, paths, output, policy_id=CONTEXT_POLICY)
    context = (output / "context.md").read_text(encoding="utf-8")
    prompt = (
        "Generate a JUnit 4 Java test suite using only the supplied fixed source and build context.\n"
        f"Project: {project}; fixed revision: {bug_id}f.\n"
        "Candidate target classes:\n" + "\n".join(sorted(set(classes))) + "\n\n"
        "Return Java source only, with explicit package declarations and JUnit 4 imports. "
        "Create at most 30 @Test methods in source order, with deterministic inputs and meaningful "
        "assertions derived from the fixed API behavior. Avoid external services, clock dependence, "
        "randomness, empty tests, and assertions that merely check non-null. Do not modify production "
        "code or create shadow production classes. Preserve all assertions; no feedback or repair loop.\n\n"
        "Use the provided candidate classes; target eligibility and fixtures are specified by the "
        "shared adapter contract before generation.\n\n" + context)
    data = prompt.encode("utf-8")
    (output / "prompt.md").write_bytes(data)
    metadata = {"source_sha256": manifest["source_hash"],
                "prompt_sha256": hashlib.sha256(data).hexdigest(), "prompt_policy_id": PROMPT_POLICY,
                "context_policy_id": CONTEXT_POLICY, "prompt_utf8_bytes": len(data),
                "approval_state": "prepared_proposal_pending_adapter_review",
                "target_classes": sorted(set(classes)), "adapter_eligibility_verified": False}
    (output / "prepare-metadata.json").write_text(json.dumps(metadata, indent=2) + "\n", encoding="utf-8")
    checksums = {p.relative_to(output).as_posix(): hashlib.sha256(p.read_bytes()).hexdigest()
                 for p in sorted(output.rglob("*")) if p.is_file()}
    (output / "checksums.json").write_text(json.dumps(checksums, indent=2) + "\n", encoding="utf-8")
    return metadata


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--workspace", type=Path, required=True)
    parser.add_argument("--pilot", type=Path, required=True)
    parser.add_argument("--output", type=Path, required=True)
    args = parser.parse_args()
    rows = json.loads(args.pilot.read_text(encoding="utf-8"))["pilot_bugs"]
    records = []
    for row in rows:
        project, bug = row["project"], row["bug_id"]
        fixed = args.workspace / "worktrees" / project / str(bug) / "f"
        if not fixed.exists():
            fixed = args.workspace / "api854-prepare" / f"{project}-{bug}f"
        classes_path = args.workspace / "defects4j/framework/projects" / project / "modified_classes" / f"{bug}.src"
        try:
            classes = [s.strip() for s in classes_path.read_text(encoding="utf-8").splitlines() if s.strip()]
            metadata = prepare(fixed, project, bug, classes, args.output / f"{project}-{bug}")
            records.append({**row, "state": "prepared_proposal", **metadata})
        except (OSError, ValueError) as error:
            records.append({**row, "state": "blocked", "reason": str(error)})
    args.output.mkdir(parents=True, exist_ok=True)
    (args.output / "index.json").write_text(json.dumps({"records": records,
        "live_requests": 0, "queue_mutations": 0}, indent=2) + "\n", encoding="utf-8")
    print(json.dumps({"prepared": sum(r["state"] == "prepared_proposal" for r in records),
                      "blocked": [r for r in records if r["state"] == "blocked"]}))


if __name__ == "__main__":
    main()
