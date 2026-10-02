"""Review immutable preparation bytes and independently retained adapter evidence.

This is offline review only. No queue/API calls or experiment approval occurs.
Declaration eligibility is deliberately separate from fixture/oracle validity.
"""
from __future__ import annotations

import argparse
from collections import Counter
import hashlib
import json
from pathlib import Path, PurePosixPath

from .common import contained, read_json, sha256, write_json
from .context_export import BUILD_FILES


def safe_relative(name):
    if not isinstance(name, str):
        raise ValueError("Artifact path must be a string")
    path = PurePosixPath(name)
    if not path.parts or path.is_absolute() or ".." in path.parts or "\\" in name or ":" in name:
        raise ValueError("Unsafe artifact path")
    return Path(*path.parts)


def composite_hash(files):
    return hashlib.sha256(json.dumps(files, sort_keys=True, separators=(",", ":")).encode()).hexdigest()


def validate_artifact(root, row):
    root = Path(root)
    checksums = read_json(root / "checksums.json")
    actual = {p.relative_to(root).as_posix() for p in root.rglob("*") if p.is_file()
              and p.name != "checksums.json"}
    if not isinstance(checksums, dict) or set(checksums) != actual:
        raise ValueError("Checksums must cover exactly the retained artifact files")
    for name, digest in checksums.items():
        path = contained(root, safe_relative(name))
        if path.is_symlink() or sha256(path) != digest:
            raise ValueError(f"Artifact checksum differs: {name}")
    manifest = read_json(root / "context-manifest.json")
    metadata = read_json(root / "prepare-metadata.json")
    identity = (row["project"], row["bug_id"])
    if ((manifest.get("project"), manifest.get("bug_id")) != identity
            or manifest.get("revision") != f"{row['bug_id']}f"
            or manifest.get("contains_execution_logs") is not False):
        raise ValueError("Fixed context identity differs or contains execution feedback")
    files = manifest.get("source_files")
    if not isinstance(files, list) or not files or len({f["path"] for f in files}) != len(files):
        raise ValueError("Require unique source-file inventory")
    sources = {}
    for entry in files:
        relative = safe_relative(entry["path"])
        if relative.suffix != ".java" and relative.as_posix() not in BUILD_FILES:
            raise ValueError("Context file is neither production Java nor a root build file")
        path = contained(root / "fixed-source", relative)
        if path.is_symlink() or sha256(path) != entry["sha256"] or path.stat().st_size != entry["bytes"]:
            raise ValueError("Fixed-source bytes/hash/size differ from manifest")
        if relative.suffix == ".java":
            sources[relative.as_posix()] = entry["sha256"]
    if not sources:
        raise ValueError("Missing fixed Java sources")
    expected_files = {"context-manifest.json", "context.md", "prepare-metadata.json", "prompt.md", "revision-proof.json"}
    expected_files.update("fixed-source/" + entry["path"] for entry in files)
    if set(checksums) != expected_files:
        raise ValueError("Unexpected file in fixed preparation artifact contract")
    context_hash = composite_hash(files)
    prompt = root / "prompt.md"
    if (manifest.get("source_hash") != context_hash or metadata.get("source_sha256") != context_hash
            or row.get("source_sha256") != context_hash or metadata.get("prompt_sha256") != sha256(prompt)
            or row.get("prompt_sha256") != sha256(prompt)
            or metadata.get("prompt_utf8_bytes") != prompt.stat().st_size
            or row.get("prompt_utf8_bytes") != prompt.stat().st_size
            or manifest.get("selection_policy_id") != metadata.get("context_policy_id")
            or row.get("context_policy_id") != metadata.get("context_policy_id")
            or row.get("prompt_policy_id") != metadata.get("prompt_policy_id")
            or row.get("target_classes") != metadata.get("target_classes")):
        raise ValueError("Context/prompt/index metadata hashes or policy IDs differ")
    proof = read_json(root / "revision-proof.json")
    if (proof.get("verified") is not True or proof.get("matches_fixed_tag") is not True
            or proof.get("head") != proof.get("fixed_tag_commit") or proof.get("selected_source_changes") != ""):
        raise ValueError("Recorded fixed revision proof is incomplete or reports source edits")
    return {"integrity": "passed", "file_count": len(checksums), "checksums_sha256": sha256(root / "checksums.json"),
            "context_manifest_sha256": sha256(root / "context-manifest.json"),
            "context_source_hash": context_hash, "prompt_sha256": sha256(prompt),
            "prompt_utf8_bytes": prompt.stat().st_size, "fixed_source_sha256": sources,
            "target_classes": metadata["target_classes"], "recorded_fixed_commit": proof["head"],
            "revision_proof_scope": "Aom retained Git proof; independent local source comparison recorded separately",
            "context_policy_id": metadata["context_policy_id"], "prompt_policy_id": metadata["prompt_policy_id"]}


def fixture_type(name):
    """Conservative classification of the current probe's declared input support."""
    primitives = {"boolean", "byte", "short", "int", "long", "float", "double", "char"}
    if name in primitives or name in {"java.lang.String", "java.lang.Boolean", "java.lang.Byte", "java.lang.Short",
            "java.lang.Integer", "java.lang.Long", "java.lang.Float", "java.lang.Double", "java.lang.Character"}:
        return "scalar_builtin"
    if name.startswith("["):
        return "recursive_array_requires_component_and_depth_review"
    if name in {"java.lang.Number", "java.lang.Object", "java.util.Date", "java.util.List", "java.util.Collection",
                "java.lang.Iterable", "java.util.Set", "java.util.Map"}:
        return "builtin_object_or_empty_collection_requires_oracle_review"
    if name.startswith("java.") or name.startswith("javax.") or name.startswith("org.w3c."):
        return "not_explicit_builtin_fixture_null_fallback_possible"
    return "enum_or_reflective_custom_fixture_requires_review"


def review_adapter(folder, artifact, evidence_label):
    from .worker import fixed_sources
    folder = Path(folder)
    adapter = read_json(folder / "adapter.json")
    fixed = read_json(folder / "targets.fixed.json")
    buggy = read_json(folder / "targets.buggy.json")
    targets = read_json(folder / "targets.json")
    fields = ("class", "constructor_types", "method", "parameter_types")
    signature = lambda target: tuple(target[key] for key in fields)
    shared = {signature(t) for t in fixed["targets"]} & {signature(t) for t in buggy["targets"]}
    declared = {signature(t) for t in targets["targets"]}
    if (shared != declared or len(declared) != len(targets["targets"])
            or {signature(t) for t in targets["excluded_fixed_only"]} != {signature(t) for t in fixed["targets"]} - shared):
        raise ValueError("Retained eligible/excluded declarations differ from fixed/buggy intersection")
    if adapter["targets_sha256"] != sha256(folder / "targets.json"):
        raise ValueError("Adapter target inventory hash differs")
    classes = (folder / "classes.modified.txt").read_text(encoding="utf-8").splitlines()
    source_dir = (folder / "dir.src.classes.txt").read_text(encoding="utf-8").strip()
    local_sources = fixed_sources(adapter["fixed_worktree"], classes, source_dir)
    if sorted(classes) != sorted(artifact["target_classes"]) or local_sources != artifact["fixed_source_sha256"]:
        raise ValueError("Independent installed fixed Java source/class mapping differs from Aom artifacts")
    common_fixtures = (folder / "fixture-classes.txt").read_text(encoding="utf-8").splitlines()
    binaries = []
    for key, filename in (("fixed_worktree", "dir.bin.classes.txt"), ("buggy_worktree", "buggy.dir.bin.classes.txt")):
        binary_dir = (folder / filename).read_text(encoding="utf-8").strip()
        binary = contained(adapter[key], Path(binary_dir))
        binaries.append({p.relative_to(binary).with_suffix("").as_posix().replace("/", ".")
                         for p in binary.rglob("*.class") if "$" not in p.name})
    if common_fixtures != sorted(binaries[0] & binaries[1]):
        raise ValueError("Retained fixture classes differ from independent fixed/buggy compiled intersection")
    used_types = sorted({t for target in targets["targets"] for key in ("constructor_types", "parameter_types")
                         for t in target[key].split(",") if t})
    extra_receivers = sorted({t["class"] for t in targets["targets"]} - set(classes))
    classifications = {name: fixture_type(name) for name in used_types}
    return {"status": "declaration_intersection_verified", "fixed_java_matches_aom": True,
            "evidence_label": evidence_label, "adapter_sha256": sha256(folder / "adapter.json"),
            "targets_sha256": sha256(folder / "targets.json"),
            "common_fixture_classes_sha256": sha256(folder / "fixture-classes.txt"),
            "common_fixture_class_count": len(common_fixtures),
            "common_fixture_class_intersection_verified": True,
            "eligible_targets": targets["targets"], "excluded_fixed_only": targets["excluded_fixed_only"],
            "discovery_errors": targets["errors"], "used_fixture_types": classifications,
            "extra_receiver_classes_missing_from_v1_modified_source": extra_receivers,
            "fixture_validity": "pending_runtime_and_oracle_review", "usable": False,
            "v1_prompt_has_explicit_eligible_target_inventory": False,
            "required_followup": ["Create versioned eligible-target/context supplement; do not modify v1",
                                  "Review recursive/null/empty fixtures and meaningful target execution"]}


def review(artifacts, pilot, adapters, output):
    index = read_json(Path(artifacts) / "index.json")
    expected = {(r["project"], r["bug_id"], r["owner"]) for r in read_json(pilot)["pilot_bugs"]}
    rows = index.get("records", [])
    if len(rows) != 20 or {(r["project"], r["bug_id"], r["owner"]) for r in rows} != expected:
        raise ValueError("Preparation inventory must match the frozen 20-bug pilot")
    output = Path(output)
    output.mkdir(parents=True, exist_ok=False)
    results = []
    for row in rows:
        name = f"{row['project']}-{row['bug_id']}"
        result = {key: row[key] for key in ("project", "bug_id", "owner")}
        try:
            result["artifacts"] = validate_artifact(Path(artifacts) / name, row)
        except (ValueError, OSError, KeyError) as error:
            result["artifacts"] = {"integrity": "failed", "reason": str(error)}
        candidates = [Path(root) / name for root in adapters if (Path(root) / name / "adapter.json").is_file()]
        if result["artifacts"]["integrity"] == "passed" and len(candidates) == 1:
            try:
                result["eligibility"] = review_adapter(candidates[0], result["artifacts"], f"{candidates[0].parent.name}/{name}")
            except (ValueError, OSError, KeyError) as error:
                result["eligibility"] = {"status": "review_failed", "reason": str(error)}
        else:
            result["eligibility"] = {"status": "adapter_evidence_missing_or_ambiguous", "candidate_count": len(candidates)}
        write_json(output / f"{name}.json", result)
        results.append(result)
    counts = Counter(r["eligibility"]["status"] for r in results)
    summary = {"schema_version": 1, "scope": "Beam offline Aom prepare-v1 review", "primary": False,
        "pilot_sha256": sha256(pilot), "input_index_sha256": sha256(Path(artifacts) / "index.json"),
        "input_git_receipt_sha256": sha256(Path(artifacts) / "git-import-receipt.json"),
        "artifact_integrity_passed": sum(r["artifacts"]["integrity"] == "passed" for r in results),
        "eligibility_counts": dict(counts), "fixture_oracle_approval": False,
        "live_queue_mutations": 0, "real_kku_requests": 0, "bugs": results}
    write_json(output / "index.json", summary)
    return summary


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    for key in ("artifacts", "pilot", "output"):
        parser.add_argument("--" + key, type=Path, required=True)
    parser.add_argument("--adapters", nargs="+", type=Path, required=True)
    args = parser.parse_args()
    summary = review(args.artifacts, args.pilot, args.adapters, args.output)
    print(json.dumps({key: summary[key] for key in ("artifact_integrity_passed", "eligibility_counts", "real_kku_requests")}))
    return 0 if summary["artifact_integrity_passed"] == 20 else 1


if __name__ == "__main__":
    raise SystemExit(main())
