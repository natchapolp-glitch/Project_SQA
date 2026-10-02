"""Import checksum-bound Beam discovery into a new shared, gated preparation."""
import argparse
import json
from pathlib import Path
import re
import shutil

from .preparation import POLICY_V3, clean_targets, compose, digest, encoded, validate


def checked_files(root):
    sums = json.loads((root / "checksums.json").read_bytes())
    actual = {p.relative_to(root).as_posix() for p in root.rglob("*") if p.is_file()
              and p != root / "checksums.json"}
    if set(sums) != actual:
        raise ValueError("Checksum inventory differs from retained files")
    for relative, expected in sums.items():
        path = (root / relative).resolve(strict=True)
        if not path.is_relative_to(root.resolve()) or digest(path.read_bytes()) != expected:
            raise ValueError("Imported evidence checksum differs")


def build(source, review, destination):
    source, review, destination = map(Path, (source, review, destination))
    checked_files(review)
    incoming = json.loads((review / "index.json").read_bytes())
    rows = json.loads((source / "index.json").read_bytes())["records"]
    identity = lambda records: {(r["project"], r["bug_id"], r["owner"]) for r in records}
    if len(rows) != 20 or identity(rows) != identity(incoming["bugs"]):
        raise ValueError("Beam review must match the exact pilot identities")
    destination.mkdir(parents=True, exist_ok=False)
    records = []
    for row in rows:
        name = f"{row['project']}-{row['bug_id']}"
        old, folder = source / name, destination / name
        checked_files(old)
        old_meta = json.loads((old / "prepare-metadata.json").read_bytes())
        old_manifest = json.loads((old / "context-manifest.json").read_bytes())
        old_sources = {f["path"]: f["sha256"] for f in old_manifest["source_files"] if f["path"].endswith(".java")}
        record = json.loads((review / "review" / (name + ".json")).read_bytes())
        if record["artifacts"]["fixed_source_sha256"] != old_sources:
            raise ValueError("Discovery is not bound to the input modified source")
        declarations = json.loads((review / "declarations" / name / "targets.json").read_bytes())
        targets = clean_targets(declarations["targets"])
        signature = lambda t: tuple(t[k] for k in POLICY_V3["target_identity_fields"])
        fixed = json.loads((review / "declarations" / name / "targets.fixed.json").read_bytes())["targets"]
        buggy = json.loads((review / "declarations" / name / "targets.buggy.json").read_bytes())["targets"]
        fs, bs = set(map(signature, fixed)), set(map(signature, buggy))
        if (set(map(signature, targets)) != fs & bs
                or set(map(signature, declarations["excluded_fixed_only"])) != fs - bs):
            raise ValueError("Shared target intersection or exclusions differ")
        supplement = review / "supplement-v2-proposal" / name
        extra_meta = json.loads((supplement / "prepare-metadata.v2.proposal.json").read_bytes())
        if extra_meta["fixed_source_sha256"] != old_sources:
            raise ValueError("Receiver supplement changed modified-source lineage")
        folder.mkdir()
        shutil.copytree(old / "fixed-source", folder / "fixed-source")
        shutil.copyfile(old / "revision-proof.json", folder / "revision-proof.json")
        shutil.copyfile(supplement / "revision-proof.json", folder / "receiver-revision-proof.json")
        proof = json.loads((folder / "revision-proof.json").read_bytes())
        receiver_proof = json.loads((folder / "receiver-revision-proof.json").read_bytes())
        if any(not p.get("verified") or p["head"] != p["fixed_tag_commit"] for p in (proof, receiver_proof)):
            raise ValueError("Require retained immutable fixed revision proofs")
        manifest = json.loads((supplement / "context-manifest.v2.proposal.json").read_bytes())
        files_by_path = {f["path"]: f for f in manifest["source_files"]}
        if (manifest.get("project") != row["project"] or manifest.get("bug_id") != row["bug_id"]
                or manifest.get("revision") != f"{row['bug_id']}f"
                or any(files_by_path.get(f["path"]) != f for f in old_manifest["source_files"])):
            raise ValueError("Supplement changed input identity/source/build inventory")
        for path, expected in extra_meta["additional_receiver_source_sha256"].items():
            original = (supplement / "additional-fixed-source" / path).resolve(strict=True)
            target = (folder / "fixed-source" / path).resolve()
            if (not original.is_relative_to(supplement.resolve()) or not target.is_relative_to((folder / "fixed-source").resolve())
                    or target.exists() or digest(original.read_bytes()) != expected):
                raise ValueError("Receiver supplement path/hash differs")
            target.parent.mkdir(parents=True, exist_ok=True)
            shutil.copyfile(original, target)
        files = sorted(manifest["source_files"], key=lambda f: f["path"])
        manifest.update(source_files=files, source_hash=digest(json.dumps(files, sort_keys=True, separators=(",", ":")).encode()),
                        selection_policy_id=POLICY_V3["context_policy_id"])
        for key in ("original_source_location", "additional_source_location"):
            manifest.pop(key, None)
        (folder / "context-manifest.json").write_bytes(encoded(manifest))
        fixtures_raw = (review / "declarations" / name / "fixture-classes.txt").read_bytes()
        fixtures = sorted({c for c in fixtures_raw.decode().splitlines()
            if re.fullmatch(r"[A-Za-z_$][\w$]*(?:\.[A-Za-z_$][\w$]*)*", c)})
        eligibility = {**row, "target_selection": POLICY_V3["target_selection"], "fixture_policy": POLICY_V3["fixture_policy"],
            "fixed_source_sha256": old_sources, "targets": targets,
            "fixture_classes": fixtures, "excluded_fixed_only": clean_targets(declarations["excluded_fixed_only"]),
            "evidence": {"beam_commit": "44dd5cb0b44e8f075ed283d0ff4f4ae51c97f4ed",
                "review_sha256": digest((review / "review" / (name + ".json")).read_bytes()),
                "fixture_inventory_sha256": digest(fixtures_raw)}, "semantic_validity": "pending_review"}
        # Avoid carrying v1 prompt/index fields into the discovery receipt.
        eligibility = {k: eligibility[k] for k in ("project", "bug_id", "owner", "target_selection", "fixture_policy",
            "fixed_source_sha256", "targets", "fixture_classes", "excluded_fixed_only", "evidence", "semantic_validity")}
        (folder / "eligibility.json").write_bytes(encoded(eligibility))
        metadata = compose(folder, classes=old_meta["target_classes"], targets=targets, policy=POLICY_V3,
            modified_sources=old_sources, fixture_classes=fixtures,
            previous_hashes={"v1_manifest_sha256": digest((old / "context-manifest.json").read_bytes()),
                "v1_prompt_sha256": old_meta["prompt_sha256"], "beam_supplement_prompt_sha256": extra_meta["prompt_sha256"]})
        validate(manifest, metadata, (folder / "prompt.md").read_bytes(), (folder / "targets.json").read_bytes(),
                 (folder / "prepare-policy.json").read_bytes(), require_eligible=True)
        (folder / "checksums.json").write_bytes(encoded({p.relative_to(folder).as_posix(): digest(p.read_bytes())
            for p in sorted(folder.rglob("*")) if p.is_file()}))
        records.append({"project": row["project"], "bug_id": row["bug_id"], "owner": row["owner"],
            "fixed_revision_verified": True, "excluded_fixed_only_count": len(declarations["excluded_fixed_only"]), **metadata})
    index = {"schema_version": 3, "records": records, "source_version": "prepare-v1 + Beam 44dd5cb0 discovery",
        "policy_sha256": digest(encoded(POLICY_V3)), "target_count": sum(r["target_count"] for r in records),
        "excluded_fixed_only_count": sum(r["excluded_fixed_only_count"] for r in records),
        "max_prompt_utf8_bytes": max(r["prompt_utf8_bytes"] for r in records), "provider_framing_overhead": None,
        "generation_ready": False, "semantic_validity": "pending_review", "live_requests": 0, "queue_mutations": 0}
    (destination / "index.json").write_bytes(encoded(index))
    return index


def main():
    p = argparse.ArgumentParser(description=__doc__)
    p.add_argument("--input", required=True, type=Path)
    p.add_argument("--review", required=True, type=Path)
    p.add_argument("--output", required=True, type=Path)
    a = p.parse_args()
    r = build(a.input, a.review, a.output)
    print(json.dumps({k: r[k] for k in ("target_count", "excluded_fixed_only_count", "max_prompt_utf8_bytes", "generation_ready")}))


if __name__ == "__main__":
    main()
