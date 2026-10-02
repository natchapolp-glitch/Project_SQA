"""Champ Java fences -> unchanged, named sources and a hash-bound Beam lineage."""
from __future__ import annotations

import re
from pathlib import Path

from .common import contained, read_json, sha256, write_json
from .pack_suite import pack_suite, package_name


def java_tokens(source):
    """Lexical inventory only. javac/JUnit and semantic review decide validity."""
    # Keep comments/literals from contributing class names or @Test counts.
    stripped = re.sub(r'/\*.*?\*/|//[^\r\n]*|"(?:\\.|[^"\\])*"|\'(?:\\.|[^\'\\])*\'',
                      " ", source, flags=re.DOTALL)
    return re.findall(r"[A-Za-z_$][A-Za-z0-9_$]*|\S", stripped)


def source_layout(data):
    source = data.decode("utf-8-sig")
    tokens = java_tokens(source)
    depth, names, tests = 0, [], 0
    for i, token in enumerate(tokens):
        if token == "{":
            depth += 1
        elif token == "}":
            depth -= 1
        elif token == "public" and depth == 0:
            j = i + 1
            while j < len(tokens) and tokens[j] in {"abstract", "final", "strictfp"}:
                j += 1
            if tokens[j:j + 1] == ["class"] and j + 1 < len(tokens):
                names.append(tokens[j + 1])
        elif token == "@":
            following = tokens[i + 1:i + 7]
            if following[:1] == ["Test"] or following[:5] == ["org", ".", "junit", ".", "Test"]:
                tests += 1
    if depth != 0 or len(names) != 1 or not re.fullmatch(r"[A-Za-z_$][A-Za-z0-9_$]*", names[0]):
        raise ValueError("Each Java fence must declare one public top-level class with balanced braces")
    package = package_name(source).replace(".", "/")
    return (package + "/" if package else "") + names[0] + ".java", tests


class BeamSuiteResolver:
    """Reject over-cap/missing/truncated output; never repair or trim assertions."""
    policy_id = "beam-java-suite-v1"

    def __init__(self, job, protocol, fixed_source_sha256, context_source_hash):
        if protocol.get("suite_packaging") != self.policy_id or protocol.get("compatibility_policy") != "none":
            raise ValueError("Suite resolver must match the frozen processing/repair policy")
        if not fixed_source_sha256 or not re.fullmatch(r"[a-f0-9]{64}", context_source_hash):
            raise ValueError("Require fixed-source mapping and context hash from preparation")
        self.job, self.protocol = job, protocol
        self.fixed_sources, self.context_hash = fixed_source_sha256, context_source_hash

    def __call__(self, result):
        for key in ("run_id", "protocol_hash", "project", "bug_id", "approach", "repeat_index", "attempt_id"):
            if result.get(key) != self.job[key]:
                raise ValueError("Generation result differs from the claimed job/attempt")
        if result.get("generation_outcome") != "response_received" or result.get("source_hash") != self.context_hash:
            raise ValueError("Require a complete response using this exact fixed context")
        root = Path(result["artifact_path"])
        original = root / "generation-result.json"
        if read_json(original) != result:
            raise ValueError("Resolver requires the immutable original generation result")
        blocks = result.get("source_blocks")
        if not isinstance(blocks, list) or not blocks:
            raise ValueError("No complete Java source blocks")
        entries, seen, count = [], set(), 0
        for block in blocks:
            if not isinstance(block, dict) or not re.fullmatch(r"source-block-[0-9]{3,}\.java", block.get("path", "")):
                raise ValueError("Unsafe generation source-block reference")
            path = contained(root, Path(block["path"]))
            if path.is_symlink() or sha256(path) != block.get("sha256"):
                raise ValueError("Generated source-block hash differs")
            data = path.read_bytes()
            name, methods = source_layout(data)
            if name.casefold() in seen:
                raise ValueError("Generated class filenames collide")
            seen.add(name.casefold())
            count += methods
            entries.append((block, name, data, methods))
        if not 1 <= count <= self.protocol["test_method_cap"]:
            raise ValueError("Missing JUnit4 tests or suite exceeds the method cap; no truncation")
        output = root / "beam-suite"
        if output.exists():
            # Publication may resume from identical local evidence, never a new AI request.
            lineage = read_json(output / "generation-lineage.json")
            suite = output / "package/suite.tar.bz2"
            if (lineage.get("generation_result_sha256") != sha256(original) or lineage.get("job") != self.job
                    or lineage.get("fixed_source_sha256") != self.fixed_sources
                    or lineage.get("suite_sha256") != sha256(suite)):
                raise ValueError("Existing suite/lineage changed; reconciliation required")
            return suite
        sources = output / "sources"
        sources.mkdir(parents=True, exist_ok=False)
        mapping = []
        for block, name, data, methods in entries:
            target = contained(sources, Path(name))
            target.parent.mkdir(parents=True, exist_ok=True)
            with target.open("xb") as stream:
                stream.write(data)
            mapping.append({"source_block": block["path"], "archive_path": name,
                            "sha256": block["sha256"], "junit4_annotation_count": methods})
        manifest = pack_suite(sources, output / "package", count, self.protocol["test_method_cap"])
        lineage = {"schema_version": 1, "job": self.job, "observed_outcome": "generated",
                   "suite_sha256": manifest["suite_sha256"], "test_count": count,
                   "test_count_origin": "lexical_junit4_annotations", "executed_test_count": None,
                   "fixed_source_sha256": self.fixed_sources, "context_source_hash": self.context_hash,
                   "generation_result_sha256": sha256(original), "source_blocks": mapping,
                   "semantic_validity": "pending_review", "queue_published": False}
        write_json(output / "generation-lineage.json", lineage)
        write_json(output / "source-map.json", {"processing_policy": self.policy_id, "files": mapping})
        return output / "package/suite.tar.bz2"
