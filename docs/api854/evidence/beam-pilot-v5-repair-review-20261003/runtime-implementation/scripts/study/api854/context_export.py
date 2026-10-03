"""Export explicitly selected fixed source/build files; no log feedback to AI.

Selection policy and target adapter readiness are still owned by the shared
protocol/Beam. This exporter doesn't infer targets or claim support for 854 bugs.
"""
from __future__ import annotations

import argparse
import json
from pathlib import Path, PurePosixPath
import re

from .generate_worker import write_json
from .kku_client import digest

BUILD_FILES = {"pom.xml", "build.xml", "build.gradle", "build.gradle.kts", "settings.gradle",
               "settings.gradle.kts", "gradle.properties", "project.properties"}


def export_context(worktree: Path, project: str, bug_id: int, paths: list[str], destination: Path,
                   *, policy_id: str) -> dict:
    if not re.fullmatch(r"[A-Za-z][A-Za-z0-9]*", project) or type(bug_id) is not int or bug_id < 1:
        raise ValueError("Invalid project/bug identity")
    if not paths or len(set(paths)) != len(paths) or not policy_id.strip():
        raise ValueError("Require explicit unique file selection and a protocol selection-policy ID")
    root = worktree.resolve(strict=True)
    config = {}
    for line in (root / ".defects4j.config").read_text(encoding="utf-8").splitlines():
        if "=" in line and not line.lstrip().startswith("#"):
            key, value = line.split("=", 1)
            key = key.strip()
            if key in config:
                raise ValueError("Duplicate Defects4J config field")
            config[key] = value.strip()
    if (config.get("pid"), config.get("vid")) != (project, f"{bug_id}f"):
        raise ValueError("Only the exact fixed Defects4J revision may be exported")
    files, sections, content = [], [], []
    for relative in paths:
        posix = PurePosixPath(relative)
        if posix.is_absolute() or ".." in posix.parts or "\\" in relative or ":" in relative:
            raise ValueError("Unsafe context file path")
        if posix.suffix != ".java" and posix.name not in BUILD_FILES:
            raise ValueError("Only selected Java source and known build files are allowed; never evaluation logs")
        if any(part.startswith(".") or part.lower() in {"target", "build", "results", "logs", "output"}
               for part in posix.parts[:-1]):
            raise ValueError("Generated/log/hidden directories are not fixed-source context")
        source = (root / relative).resolve(strict=True)
        if not source.is_relative_to(root) or not source.is_file():
            raise ValueError("Context path escaped fixed worktree")
        data = source.read_bytes()
        text = data.decode("utf-8")
        files.append({"path": posix.as_posix(), "sha256": digest(data), "bytes": len(data)})
        content.append((relative, data))
        fence = "`" * max(3, max((len(x) for x in re.findall(r"`+", text)), default=0) + 1)
        sections.append(f"## {posix.as_posix()}\n\n{fence}\n{text}\n{fence}\n")
    if not any(PurePosixPath(f["path"]).suffix == ".java" for f in files):
        raise ValueError("Fixed context must include Java source")
    source_hash = digest(json.dumps(files, sort_keys=True, separators=(",", ":")).encode())
    manifest = {"schema": "champ-fixed-context-v1-proposal", "project": project, "bug_id": bug_id,
                "revision": f"{bug_id}f", "selection_policy_id": policy_id,
                "source_files": files, "source_hash": source_hash, "contains_execution_logs": False}
    destination.mkdir(parents=True, exist_ok=False)
    for relative, data in content:
        target = destination / "fixed-source" / relative
        target.parent.mkdir(parents=True, exist_ok=True)
        with target.open("xb") as stream:
            stream.write(data)
    (destination / "context.md").write_text("\n".join(sections), encoding="utf-8")
    write_json(destination / "context-manifest.json", manifest)
    return manifest


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--worktree", type=Path, required=True)
    parser.add_argument("--project", required=True)
    parser.add_argument("--bug-id", type=int, required=True)
    parser.add_argument("--files", nargs="+", required=True)
    parser.add_argument("--policy-id", required=True)
    parser.add_argument("--output", type=Path, required=True)
    args = parser.parse_args()
    print(json.dumps(export_context(args.worktree, args.project, args.bug_id, args.files, args.output,
                                    policy_id=args.policy_id), ensure_ascii=True))


if __name__ == "__main__":
    main()
