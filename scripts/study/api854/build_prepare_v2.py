"""Compose a new immutable preparation version from verified v1 fixed bytes."""
import argparse
import json
from pathlib import Path
import shutil

from .preparation import compose, digest, encoded, CONTEXT_POLICY, POLICY, validate


def build(source, destination, eligibility_root=None):
    source, destination = Path(source), Path(destination)
    destination.mkdir(parents=True, exist_ok=False)
    index = json.loads((source / "index.json").read_text(encoding="utf-8"))
    records = []
    for row in index["records"]:
        name = f"{row['project']}-{row['bug_id']}"
        old = source / name
        checksums = json.loads((old / "checksums.json").read_text(encoding="utf-8"))
        for relative, expected in checksums.items():
            path = (old / relative).resolve(strict=True)
            if not path.is_relative_to(old.resolve()) or digest(path.read_bytes()) != expected:
                raise ValueError(f"v1 checksum differs: {name}/{relative}")
        proof = json.loads((old / "revision-proof.json").read_text(encoding="utf-8"))
        if not proof.get("verified") or proof["head"] != proof["fixed_tag_commit"]:
            raise ValueError("Require verified immutable fixed revision")
        folder = destination / name
        folder.mkdir()
        shutil.copytree(old / "fixed-source", folder / "fixed-source")
        shutil.copyfile(old / "context.md", folder / "context.md")
        shutil.copyfile(old / "revision-proof.json", folder / "revision-proof.json")
        manifest = json.loads((old / "context-manifest.json").read_text(encoding="utf-8"))
        manifest["selection_policy_id"] = CONTEXT_POLICY
        (folder / "context-manifest.json").write_bytes(encoded(manifest))
        targets, evidence = None, None
        if eligibility_root and (Path(eligibility_root) / name / "eligibility.json").is_file():
            evidence_path = Path(eligibility_root) / name / "eligibility.json"
            evidence = json.loads(evidence_path.read_text(encoding="utf-8"))
            sources = {f["path"]: f["sha256"] for f in manifest["source_files"] if f["path"].endswith(".java")}
            if (evidence.get("project") != row["project"] or evidence.get("bug_id") != row["bug_id"]
                    or evidence.get("fixed_source_sha256") != sources
                    or evidence.get("target_selection") != POLICY["target_selection"]
                    or evidence.get("fixture_policy") != POLICY["fixture_policy"]):
                raise ValueError("Eligibility evidence is not bound to this fixed source/policy")
            targets = evidence["targets"]
            shutil.copyfile(evidence_path, folder / "eligibility.json")
        old_metadata = json.loads((old / "prepare-metadata.json").read_text(encoding="utf-8"))
        metadata = compose(folder, classes=old_metadata["target_classes"], targets=targets,
            previous_hashes={"manifest_sha256": digest((old / "context-manifest.json").read_bytes()),
                             "prompt_sha256": old_metadata["prompt_sha256"], "source_sha256": old_metadata["source_sha256"]})
        validate(manifest, metadata, (folder / "prompt.md").read_bytes(),
                 (folder / "targets.json").read_bytes(), (folder / "prepare-policy.json").read_bytes())
        sums = {p.relative_to(folder).as_posix(): digest(p.read_bytes()) for p in sorted(folder.rglob("*")) if p.is_file()}
        (folder / "checksums.json").write_bytes(encoded(sums))
        records.append({"project": row["project"], "bug_id": row["bug_id"], "owner": row["owner"],
            "state": "composed_pending_team_review", "fixed_revision_verified": True, **metadata})
    result = {"schema_version": 2, "records": records, "source_version": "prepare-v1",
              "policy_sha256": digest(encoded(POLICY)), "live_requests": 0, "queue_mutations": 0,
              "generation_ready": False, "max_prompt_utf8_bytes": max(r["prompt_utf8_bytes"] for r in records),
              "provider_framing_overhead": None}
    (destination / "index.json").write_bytes(encoded(result))
    return result


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--input", required=True, type=Path)
    parser.add_argument("--output", required=True, type=Path)
    parser.add_argument("--eligibility-root", type=Path)
    args = parser.parse_args()
    result = build(args.input, args.output, args.eligibility_root)
    print(json.dumps({"bugs": len(result["records"]), "shared_discovery": sum(r["adapter_eligibility_verified"] for r in result["records"]),
                      "generation_ready": False, "max_prompt_utf8_bytes": result["max_prompt_utf8_bytes"]}))


if __name__ == "__main__":
    main()
