#!/usr/bin/env python3
"""Prepare identical fixed-source AI contexts and archive operator-supplied evidence.

This script never calls an AI provider and never labels locally generated code as
Claude or IntelliSphere output. Compilation and fault detection are separate steps.
"""

from __future__ import annotations

import argparse
import hashlib
import io
import json
import re
import shutil
import sys
import tarfile
from datetime import datetime, timezone
from pathlib import Path

SCHEMA_VERSION = 1
TOOLS = {"claude": "Anthropic Claude", "intellisphere": "KKU AI Sphere / IntelliSphere"}
PROJECT_RE = re.compile(r"^[A-Za-z][A-Za-z0-9]*$")
CLASS_RE = re.compile(r"^[A-Za-z_$][\w$]*(?:\.[A-Za-z_$][\w$]*)*$", re.ASCII)
BUILD_NAMES = {"pom.xml", "build.xml", "build.gradle", "build.gradle.kts", "settings.gradle", "settings.gradle.kts", "gradle.properties", "project.properties"}
EXCLUDED_DIRS = {".git", ".svn", "target", "build", "out", "node_modules"}
REPO_ROOT = Path(__file__).resolve().parents[2]


class EvidenceError(ValueError):
    """Invalid input or evidence that cannot be safely preserved."""


def sha256(data: bytes) -> str:
    return hashlib.sha256(data).hexdigest()


def now() -> str:
    return datetime.now(timezone.utc).isoformat()


def write_json(path: Path, value: dict) -> None:
    path.write_text(json.dumps(value, indent=2, ensure_ascii=False) + "\n", encoding="utf-8")


def read_json(path: Path) -> dict:
    try:
        value = json.loads(path.read_text(encoding="utf-8"))
    except (OSError, ValueError) as exc:
        raise EvidenceError(f"Cannot read JSON object {path}: {exc}") from exc
    if not isinstance(value, dict):
        raise EvidenceError(f"Expected JSON object: {path}")
    return value


def plain_file(path: Path, root: Path | None = None) -> Path:
    """Reject symlink/reparse traversal, including directory ancestors."""
    path = path.absolute()
    if root is not None:
        try:
            path.relative_to(root.absolute())
            path.resolve().relative_to(root.resolve())
        except ValueError as exc:
            raise EvidenceError(f"Path escapes source directory: {path}") from exc
    for item in [path, *path.parents]:
        if item.is_symlink() or (hasattr(item, "is_junction") and item.is_junction()):
            raise EvidenceError(f"Symlinks and junctions are not accepted: {item}")
    if not path.is_file():
        raise EvidenceError(f"Expected regular file: {path}")
    return path


def new_output(path: Path, inputs: list[Path]) -> Path:
    path = path.absolute()
    if path.exists():
        raise EvidenceError(f"Output already exists; choose a new run directory: {path}")
    for source in inputs:
        source = source.resolve()
        try:
            path.resolve().relative_to(source)
        except ValueError:
            pass
        else:
            raise EvidenceError(f"Output must be outside source directory: {source}")
    path.mkdir(parents=True)
    return path


def checked_identity(project: str, bug_id: int) -> None:
    if not PROJECT_RE.fullmatch(project) or bug_id < 1:
        raise EvidenceError("Project must be alphanumeric and bug ID must be positive")


def prepare(args: argparse.Namespace) -> dict:
    checked_identity(args.project, args.bug_id)
    worktree = args.fixed_worktree.absolute()
    config_path = plain_file(worktree / ".defects4j.config", worktree)
    config = {}
    for line in config_path.read_text(encoding="utf-8").splitlines():
        if "=" in line and not line.lstrip().startswith("#"):
            key, value = line.split("=", 1)
            config[key.strip()] = value.strip()
    if config.get("pid") != args.project or config.get("vid") != f"{args.bug_id}f":
        raise EvidenceError("The worktree .defects4j.config must match the requested project and fixed revision")
    class_bytes = plain_file(args.classes_file).read_bytes()
    classes = []
    for line in class_bytes.decode("utf-8-sig").splitlines():
        name = line.strip()
        if not name or name.startswith("#"):
            continue
        if not CLASS_RE.fullmatch(name):
            raise EvidenceError(f"Expected one fully qualified Java class per line: {name!r}")
        if name not in classes:
            classes.append(name)
    if not classes:
        raise EvidenceError("The classes file is empty")
    sources = []
    all_java = sorted(worktree.rglob("*.java"))
    for name in classes:
        suffix = name.split("$")[0].replace(".", "/") + ".java"
        matches = [p for p in all_java if not EXCLUDED_DIRS.intersection(p.relative_to(worktree).parts)
                   and p.relative_to(worktree).as_posix().endswith("/" + suffix)]
        root_match = worktree / suffix
        if root_match.is_file() and root_match not in matches:
            matches.append(root_match)
        if len(matches) != 1:
            raise EvidenceError(f"Class {name} must resolve to exactly one source file; found {len(matches)}")
        path = plain_file(matches[0], worktree)
        relative = path.relative_to(worktree)
        if any(part.lower() in {"test", "tests", "testcases"} for part in relative.parts[:-1]):
            raise EvidenceError(f"Target classes must be production source, not tests: {relative}")
        sources.append((path, relative, path.read_bytes(), name))
    builds = []
    for name in sorted(BUILD_NAMES):
        for path in sorted(worktree.rglob(name)):
            relative = path.relative_to(worktree)
            if EXCLUDED_DIRS.intersection(relative.parts) or any(p.lower() in {"test", "tests"} for p in relative.parts[:-1]):
                continue
            plain_file(path, worktree)
            builds.append((path, relative, path.read_bytes()))
    template_path = plain_file(REPO_ROOT / "prompts" / "round2-unit-test.md")
    template_bytes = template_path.read_bytes()
    prompt = template_bytes.decode("utf-8").rstrip() + "\n\n"
    prompt += f"## Experiment context\n\nProject: {args.project}\nFixed reference revision: {args.bug_id}f\n"
    prompt += "Target classes:\n" + "".join(f"- {name}\n" for name in classes)
    manifest = {"schema_version": SCHEMA_VERSION, "kind": "fixed-source-ai-context",
                "project": args.project, "bug_id": args.bug_id, "reference_revision": f"{args.bug_id}f",
                "prepared_at": now(), "classes": classes, "classes_file_sha256": sha256(class_bytes),
                "template_sha256": sha256(template_bytes), "source_files": [], "build_files": [],
                "oracle_scope": "fixed-reference-behavior", "contains_buggy_source_or_patch_or_triggering_tests": False}
    target_bytes = None
    if getattr(args, "targets_file", None):
        target_path = plain_file(args.targets_file)
        target_bytes = target_path.read_bytes()
        target_data = read_json(target_path)
        eligible = target_data.get("targets")
        if not isinstance(eligible, list) or not eligible:
            raise EvidenceError("Eligible target inventory must contain a nonempty targets list")
        prompt += "\n## Eligible shared API declarations\n\n"
        prompt += "Target these declarations, which exist on both evaluation revisions. "
        prompt += "Only declaration signatures were checked; no buggy behavior was supplied. "
        prompt += "Generate at most 30 independent test methods per response.\n\n```json\n"
        prompt += json.dumps(eligible, indent=2) + "\n```\n"
        manifest["eligible_targets"] = eligible
        manifest["target_inventory_sha256"] = sha256(target_bytes)
    for _, relative, data, name in sources:
        data.decode("utf-8")
        prompt += f"\n## Production source {relative.as_posix()}\n\n```java\n{data.decode('utf-8')}\n```\n"
        manifest["source_files"].append({"path": relative.as_posix(), "class": name, "sha256": sha256(data), "bytes": len(data)})
    for _, relative, data in builds:
        data.decode("utf-8")
        prompt += f"\n## Build configuration {relative.as_posix()}\n\n```text\n{data.decode('utf-8')}\n```\n"
        manifest["build_files"].append({"path": relative.as_posix(), "sha256": sha256(data), "bytes": len(data)})
    prompt_bytes = prompt.encode("utf-8")
    manifest["prompt_sha256"] = sha256(prompt_bytes)
    output = new_output(args.output, [worktree])
    (output / "prompt.md").write_bytes(prompt_bytes)
    (output / "classes.txt").write_text("\n".join(classes) + "\n", encoding="utf-8")
    if target_bytes is not None:
        (output / "target-inventory.json").write_bytes(target_bytes)
    for _, relative, data, *_ in sources + builds:
        destination = output / "context" / relative
        destination.parent.mkdir(parents=True, exist_ok=True)
        destination.write_bytes(data)
    write_json(output / "context-manifest.json", manifest)
    return {"output": str(output), "project": args.project, "bug_id": args.bug_id,
            "source_files": len(sources), "build_files": len(builds), "prompt_sha256": manifest["prompt_sha256"]}


def timestamp_or_unknown(value: object, name: str, warnings: list[str]) -> str | None:
    if value is None:
        warnings.append(f"{name} is unknown; recover it from the provider session if available")
        return None
    if not isinstance(value, str):
        raise EvidenceError(f"{name} must be an ISO 8601 timestamp with timezone")
    try:
        parsed = datetime.fromisoformat(value.replace("Z", "+00:00"))
    except ValueError as exc:
        raise EvidenceError(f"Invalid {name}: {value!r}") from exc
    if parsed.tzinfo is None:
        raise EvidenceError(f"{name} must include a timezone")
    return value


def ingest(args: argparse.Namespace) -> dict:
    if not args.model.strip() or args.seed < 0 or args.iteration < 1:
        raise EvidenceError("Provide a nonempty model name, a nonnegative run seed, and a positive iteration")
    prompt_path, response_path = plain_file(args.prompt), plain_file(args.response)
    supplied = read_json(plain_file(args.metadata_file)) if args.metadata_file else {}
    for field, expected in [("tool", args.tool), ("model", args.model)]:
        if field in supplied and supplied[field] != expected:
            raise EvidenceError(f"Metadata {field} disagrees with command-line value")
    context_path = Path(supplied.get("context_manifest", prompt_path.parent / "context-manifest.json"))
    if not context_path.is_absolute() and args.metadata_file:
        context_path = args.metadata_file.absolute().parent / context_path
    context_path = plain_file(context_path)
    context = read_json(context_path)
    if context.get("kind") != "fixed-source-ai-context" or context.get("schema_version") != SCHEMA_VERSION:
        raise EvidenceError("A prepare-generated context-manifest.json is required")
    try:
        project, bug_id = context["project"], int(context["bug_id"])
        checked_identity(project, bug_id)
    except (KeyError, TypeError, ValueError) as exc:
        raise EvidenceError("Invalid context project or bug ID") from exc
    if context.get("reference_revision") != f"{bug_id}f":
        raise EvidenceError("Context must identify the fixed revision")
    if context.get("target_inventory_sha256"):
        target_inventory = plain_file(context_path.parent / "target-inventory.json")
        if sha256(target_inventory.read_bytes()) != context["target_inventory_sha256"]:
            raise EvidenceError("Eligible target inventory has changed")
    for source in context.get("source_files", []) + context.get("build_files", []):
        relative = Path(source["path"])
        if relative.is_absolute() or ".." in relative.parts:
            raise EvidenceError("Unsafe context file path")
        source_path = plain_file(context_path.parent / "context" / relative, context_path.parent / "context")
        if sha256(source_path.read_bytes()) != source["sha256"]:
            raise EvidenceError(f"Context file has changed: {relative}")
    prompt_data, response_data = prompt_path.read_bytes(), response_path.read_bytes()
    if not prompt_data.strip() or not response_data.strip():
        raise EvidenceError("The exact prompt and raw response must both be nonempty")
    if args.iteration == 1 and sha256(prompt_data) != context.get("prompt_sha256"):
        raise EvidenceError("First iteration prompt differs from prepared context; prepare matching contexts for both tools")
    warnings = []
    generated_at = timestamp_or_unknown(supplied.get("generated_at"), "generated_at", warnings)
    parameters = supplied.get("parameters")
    if parameters is None:
        warnings.append("Generation parameters are unknown; do not substitute the archive run seed for a model seed")
    elif not isinstance(parameters, dict):
        raise EvidenceError("parameters must be an object or null")
    session_url = supplied.get("session_url")
    if session_url is not None and (not isinstance(session_url, str) or not session_url.startswith("https://")):
        raise EvidenceError("session_url must be an HTTPS session link or null")
    if args.iteration > 1 and not supplied.get("parent_run"):
        raise EvidenceError("Repair iterations require metadata parent_run identifying the preceding evidence directory")
    tests_root = args.tests_dir.absolute()
    if not tests_root.is_dir():
        raise EvidenceError(f"Tests directory does not exist: {tests_root}")
    java = []
    archive_paths = set()
    for path in sorted(tests_root.rglob("*")):
        if path.is_symlink() or (hasattr(path, "is_junction") and path.is_junction()):
            raise EvidenceError(f"Tests directory contains a link: {path}")
        if not path.is_file() or path.suffix != ".java":
            continue
        plain_file(path, tests_root)
        relative = path.relative_to(tests_root)
        if any(part in {"..", "."} or ":" in part or "\\" in part for part in relative.parts):
            raise EvidenceError(f"Unsafe Java archive path: {relative}")
        folded = relative.as_posix().casefold()
        if folded in archive_paths:
            raise EvidenceError(f"Case-colliding Java archive path: {relative}")
        archive_paths.add(folded)
        data = path.read_bytes()
        if not data.strip():
            raise EvidenceError(f"Empty Java file: {relative}")
        data.decode("utf-8-sig")
        java.append((relative, data))
    if not java:
        warnings.append("No Java sources were extracted; retain this provider response as a generation failure")
    missing_from_response = []
    response_normalized = response_data.decode("utf-8-sig").replace("\r\n", "\n").replace("\r", "\n")
    for relative, data in java:
        source_normalized = data.decode("utf-8-sig").replace("\r\n", "\n").replace("\r", "\n").strip()
        if source_normalized not in response_normalized:
            missing_from_response.append(relative.as_posix())
    if missing_from_response and not supplied.get("manual_edits"):
        raise EvidenceError("Java source differs from raw response; document manual_edits in metadata: " + ", ".join(missing_from_response))
    if missing_from_response:
        warnings.append("Manual source edits are declared; disclose them when comparing raw provider output")
    output = new_output(args.output, [tests_root, context_path.parent])
    (output / "prompt.md").write_bytes(prompt_data)
    (output / "response.txt").write_bytes(response_data)
    shutil.copyfile(context_path, output / "context-manifest.json")
    if context.get("target_inventory_sha256"):
        shutil.copyfile(context_path.parent / "target-inventory.json", output / "target-inventory.json")
    if args.metadata_file:
        shutil.copyfile(args.metadata_file, output / "operator-metadata.json")
    files = []
    for relative, data in java:
        destination = output / "tests" / relative
        destination.parent.mkdir(parents=True, exist_ok=True)
        destination.write_bytes(data)
        files.append({"path": relative.as_posix(), "sha256": sha256(data), "bytes": len(data)})
    archive_name = f"{project}-{bug_id}f-{args.tool}.{args.seed}.tar.bz2" if java else None
    if archive_name:
        with tarfile.open(output / archive_name, "w:bz2", format=tarfile.PAX_FORMAT) as archive:
            for relative, data in java:
                member = tarfile.TarInfo(relative.as_posix())
                member.size, member.mode, member.mtime = len(data), 0o644, 0
                archive.addfile(member, io.BytesIO(data))
    metadata = {"schema_version": SCHEMA_VERSION, "kind": "operator-supplied-ai-evidence",
                "tool": args.tool, "provider": TOOLS[args.tool], "model": args.model,
                "project": project, "bug_id": bug_id, "reference_revision": f"{bug_id}f",
                "run_id": output.name, "archive_run_seed": args.seed, "prompt_iteration": args.iteration,
                "generated_at": generated_at, "ingested_at": now(), "parameters": parameters,
                "session_url": session_url, "parent_run": supplied.get("parent_run"),
                "manual_edits": supplied.get("manual_edits"), "notes": supplied.get("notes"),
                "prompt_sha256": sha256(prompt_data), "response_sha256": sha256(response_data),
                "context_manifest_sha256": sha256(context_path.read_bytes()),
                "source_files": context.get("source_files", []), "build_files": context.get("build_files", []),
                "java_files": files, "external_suite": archive_name,
                "external_suite_sha256": sha256((output / archive_name).read_bytes()) if archive_name else None,
                "generation_status": "sources_received" if java else "no_java_sources",
                "provenance_status": "recorded" if generated_at and parameters is not None else "incomplete",
                "compile_status": "not_run", "fixed_test_status": "not_run", "buggy_test_status": "not_run",
                "line_coverage": None, "branch_coverage": None, "fault_detected": None,
                "warnings": warnings}
    write_json(output / "metadata.json", metadata)
    return {"output": str(output), "external_suite": archive_name, "java_files": len(java),
            "provenance_status": metadata["provenance_status"], "warnings": warnings}


def parser() -> argparse.ArgumentParser:
    cli = argparse.ArgumentParser(description=__doc__)
    commands = cli.add_subparsers(dest="command", required=True)
    make = commands.add_parser("prepare", help="Build a fixed-source prompt shared by both AI tools")
    make.add_argument("--project", required=True)
    make.add_argument("--bug-id", type=int, required=True)
    make.add_argument("--fixed-worktree", type=Path, required=True)
    make.add_argument("--classes-file", type=Path, required=True)
    make.add_argument("--targets-file", type=Path)
    make.add_argument("--output", type=Path, required=True)
    make.set_defaults(action=prepare)
    take = commands.add_parser("ingest", help="Preserve real provider evidence and package external Java tests")
    take.add_argument("--tool", choices=sorted(TOOLS), required=True)
    take.add_argument("--model", required=True)
    take.add_argument("--prompt", type=Path, required=True)
    take.add_argument("--response", type=Path, required=True)
    take.add_argument("--tests-dir", type=Path, required=True)
    take.add_argument("--output", type=Path, required=True)
    take.add_argument("--metadata-file", type=Path)
    take.add_argument("--seed", type=int, default=1, help="Run index in the Defects4J archive filename; not an AI model seed")
    take.add_argument("--iteration", type=int, default=1)
    take.set_defaults(action=ingest)
    return cli


def main() -> int:
    args = parser().parse_args()
    try:
        result = args.action(args)
    except (EvidenceError, OSError, UnicodeError) as exc:
        print(json.dumps({"status": "error", "error": str(exc)}, ensure_ascii=False), file=sys.stderr)
        return 2
    print(json.dumps(result, indent=2, ensure_ascii=False))
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
