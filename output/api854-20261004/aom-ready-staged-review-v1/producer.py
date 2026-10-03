"""Verify the staged evidence bytes match every local seal before commit."""
import hashlib
import json
from pathlib import Path
import subprocess
import sys

root = Path.cwd().resolve()
sys.path.insert(0, str(root / "scripts/study"))
from aom_ready_csv import save, seal, verify_manifest, digest

changed = subprocess.check_output(["git", "diff", "--cached", "--name-only", "-z"]).split(b"\0")
names = [v.decode() for v in changed if v]
allowed = ("output/api854-20261004/aom-ready-", "scripts/study/aom_ready", "docs/api854/")
exact = {".gitignore", "scripts/study/tests/test_aom_ready_report.py"}
if any(name not in exact and not name.startswith(allowed) for name in names):
    raise ValueError("Unrelated staged change")
index = {}
for entry in subprocess.check_output(["git", "ls-files", "-s", "-z"]).split(b"\0"):
    if entry:
        mode, obj, remainder = entry.split(b" ", 2)
        stage, name = remainder.split(b"\t", 1)
        if stage == b"0":
            index[name.decode()] = obj.decode()
for name in names:
    data = (root / name).read_bytes()
    expected = hashlib.sha1(b"blob " + str(len(data)).encode() + b"\0" + data).hexdigest()
    if index.get(name) != expected:
        raise ValueError(f"Staged and measured bytes differ: {name}")
sealed = 0
entries = 0
for folder in (root / "output/api854-20261004").glob("aom-ready-*"):
    for manifest in folder.rglob("checksums.json"):
        values = json.loads(manifest.read_text(encoding="utf-8-sig"))
        entries += verify_manifest(manifest.parent)
        for name in values:
            file = manifest.parent / name
            relative = file.relative_to(root).as_posix()
            if relative not in index:
                raise ValueError(f"Sealed file omitted from index (ignore rule?): {relative}")
        if manifest.relative_to(root).as_posix() not in index:
            raise ValueError("Seal itself missing from index")
        sealed += 1
packet = root / "output/api854-20261004/aom-ready-staged-review-v1"
packet.mkdir(exist_ok=False)
save(packet / "receipt.json", {"staged_files_verified": len(names), "nested_manifests_verified": sealed,
     "nested_manifest_entries_verified": entries, "sealed_files_omitted": 0,
     "staged_and_worktree_bytes_equal": True, "unrelated_changes": 0,
     "private_runtime_files_staged": 0, "api_requests": 0, "queue_mutations": 0,
     "primary_results": 0, "gate_a_approved": False})
(packet / "producer.py").write_bytes(Path(__file__).read_bytes())
seal(packet)
print(f"staged={len(names)}, seals={sealed}, entries={entries}, omissions=0")
