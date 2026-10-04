"""Package unchanged Java sources for evaluation; never claims test execution."""
from __future__ import annotations

import argparse
import hashlib
import io
from pathlib import Path
import re
import tarfile

from .common import positive_int, sha256, write_json

MAX_ARTIFACT_BYTES = 20 * 1024 * 1024  # Queue schema 1.0 upload limit.
MAX_SOURCE_BYTES = 100 * 1024 * 1024
JAVA_IDENTIFIER = r"[A-Za-z_$][A-Za-z0-9_$]*"


def package_name(source):
    # Ignore comments and literals when reading the package declaration. This
    # is a layout check, not a Java parser or a test-method counter. Compilation
    # and runtime discovery remain the evaluator's responsibility.
    stripped = re.sub(r'/\*.*?\*/|//[^\r\n]*|"(?:\\.|[^"\\])*"|\'(?:\\.|[^\'\\])*\'',
                      " ", source, flags=re.DOTALL)
    declarations = re.findall(r"\bpackage\s+(" + JAVA_IDENTIFIER +
                              r"(?:\s*\.\s*" + JAVA_IDENTIFIER + r")*)\s*;", stripped)
    if len(declarations) > 1:
        raise ValueError("Multiple package declarations")
    return re.sub(r"\s+", "", declarations[0]) if declarations else ""


def pack_suite(sources, output, test_count, test_method_cap=30):
    count = positive_int(test_count, "producer-declared test_count")
    cap = positive_int(test_method_cap, "test_method_cap")
    if count > cap:
        raise ValueError("Suite exceeds the protocol test-method cap")
    sources, output = Path(sources), Path(output)
    if sources.is_symlink() or getattr(sources, "is_junction", lambda: False)() or not sources.is_dir():
        raise ValueError("Sources must be a real directory")
    source_root = sources.resolve()
    if output.resolve().is_relative_to(source_root):
        raise ValueError("Output must be outside the source directory")
    entries, total, seen = [], 0, set()
    for path in sorted(sources.rglob("*")):
        if path.is_symlink() or getattr(path, "is_junction", lambda: False)():
            raise ValueError("Source links/junctions are not allowed")
        if not path.resolve().is_relative_to(source_root):
            raise ValueError("Source path escapes root")
        if path.is_dir():
            continue
        name = path.relative_to(sources).as_posix()
        parts = name.split("/")
        if (not path.is_file() or not name.endswith(".java") or
                not re.fullmatch(JAVA_IDENTIFIER, parts[-1][:-5]) or
                any(not re.fullmatch(JAVA_IDENTIFIER, part) for part in parts[:-1])):
            raise ValueError(f"Only Java sources at package paths are allowed: {name}")
        if name.casefold() in seen:
            raise ValueError("Case-colliding archive paths are not portable")
        seen.add(name.casefold())
        # Bound reads as well as the final compressed artifact.
        with path.open("rb") as stream:
            data = stream.read(MAX_SOURCE_BYTES - total + 1)
        total += len(data)
        if total > MAX_SOURCE_BYTES:
            raise ValueError("Uncompressed sources exceed size limit")
        text = data.decode("utf-8-sig")
        if not text.strip() or "\x00" in text:
            raise ValueError(f"Empty or binary Java source: {name}")
        expected_parent = package_name(text).replace(".", "/")
        if "/".join(parts[:-1]) != expected_parent:
            raise ValueError(f"Package declaration differs from archive path: {name}")
        entries.append((name, data))
    if not entries:
        raise ValueError("Suite contains no Java sources")
    # All inputs validated before creating an immutable output directory.
    output.mkdir(parents=True, exist_ok=False)
    suite = output / "suite.tar.bz2"
    with suite.open("xb") as stream, tarfile.open(fileobj=stream, mode="w:bz2", format=tarfile.USTAR_FORMAT) as archive:
        for name, data in entries:
            member = tarfile.TarInfo(name)
            member.size, member.mode, member.mtime = len(data), 0o644, 0
            member.uid = member.gid = 0
            member.uname = member.gname = ""
            archive.addfile(member, io.BytesIO(data))
    size = suite.stat().st_size
    if size > MAX_ARTIFACT_BYTES:
        raise ValueError("Archive exceeds queue artifact size limit; do not upload")
    manifest = {"schema_version": 1, "packaging_contract": "beam-java-suite-v1",
                "suite": suite.name, "suite_sha256": sha256(suite), "size_bytes": size,
                "source_bytes": total, "test_count": count, "test_method_cap": cap,
                "test_count_origin": "producer_declared", "executed_test_count": None,
                "source_sha256": {name: hashlib.sha256(data).hexdigest() for name, data in entries},
                "semantic_validity": "pending_review", "queue_published": False}
    write_json(output / "suite-manifest.json", manifest)
    return manifest


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--sources", type=Path, required=True)
    parser.add_argument("--output", type=Path, required=True)
    parser.add_argument("--test-count", type=int, required=True,
                        help="Producer-declared method count, not runtime execution count")
    parser.add_argument("--test-method-cap", type=int, default=30, help="Must match frozen protocol")
    args = parser.parse_args()
    manifest = pack_suite(args.sources, args.output, args.test_count, args.test_method_cap)
    print(f"suite.tar.bz2 SHA-256={manifest['suite_sha256']}; execution/validity pending")


if __name__ == "__main__":
    main()
