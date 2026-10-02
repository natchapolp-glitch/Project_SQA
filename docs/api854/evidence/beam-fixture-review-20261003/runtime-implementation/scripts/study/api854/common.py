"""Strict, provisional job-file boundary; the queue owner must review this contract."""
from __future__ import annotations

import hashlib
import json
import math
from pathlib import Path
import re
import sys
from contextlib import contextmanager

STUDY = Path(__file__).resolve().parents[1]
ROOT = STUDY.parents[1]
if str(STUDY) not in sys.path:
    sys.path.insert(0, str(STUDY))

APPROACHES = {"cmaes", "fscs-art", "kku-claude", "kku-gemini"}


def read_json(path):
    return json.loads(Path(path).read_text(encoding="utf-8-sig"))


def sha256(path):
    digest = hashlib.sha256()
    with Path(path).open("rb") as stream:
        for block in iter(lambda: stream.read(1024 * 1024), b""):
            digest.update(block)
    return digest.hexdigest()


def write_json(path, value):
    path = Path(path)
    # Exclusive creation prevents an attempt/result from being silently replaced.
    with path.open("x", encoding="utf-8", newline="\n") as stream:
        stream.write(json.dumps(value, indent=2, ensure_ascii=False, allow_nan=False) + "\n")


def identifier(value, name):
    if not isinstance(value, str) or not re.fullmatch(r"[A-Za-z0-9][A-Za-z0-9_-]{0,63}", value):
        raise ValueError(f"Unsafe {name}")
    if value.upper() in {"CON", "PRN", "AUX", "NUL", *(f"COM{i}" for i in range(1, 10)), *(f"LPT{i}" for i in range(1, 10))}:
        raise ValueError(f"Reserved {name}")
    return value


def positive_int(value, name, minimum=1):
    if type(value) is not int or value < minimum:
        raise ValueError(f"{name} must be an integer >= {minimum}")
    return value


def finite_positive(value, name):
    if isinstance(value, bool) or not isinstance(value, (int, float)) or not math.isfinite(value) or value <= 0:
        raise ValueError(f"{name} must be finite and positive")
    return value


def load_job(job_path, protocol_path, *, allow_core_preflight=False):
    job, protocol = read_json(job_path), read_json(protocol_path)
    required = {"schema_version", "run_id", "project", "bug_id", "approach", "protocol_hash", "repeat_index", "attempt_id"}
    if not isinstance(job, dict) or set(job) != required or type(job["schema_version"]) is not int or job["schema_version"] != 1:
        raise ValueError("Job must match provisional schema v1 exactly (no secrets or paths in job)")
    for field in ("run_id", "project", "attempt_id"):
        identifier(job[field], field)
    positive_int(job["bug_id"], "bug_id")
    if job["approach"] not in APPROACHES:
        raise ValueError("Unknown approach")
    if job["repeat_index"] != 1 or type(job["repeat_index"]) is not int:
        raise ValueError("The current plan has one primary repeat")
    if not isinstance(job["protocol_hash"], str) or not re.fullmatch(r"[a-f0-9]{64}", job["protocol_hash"]):
        raise ValueError("protocol_hash must be SHA-256")
    if sha256(protocol_path) != job["protocol_hash"]:
        raise ValueError("Protocol hash differs from job; do not mix conditions")
    if protocol.get("state") == "frozen_core":
        if not allow_core_preflight:
            raise ValueError("Frozen core is preparation only, not a generation/evaluation protocol")
        from .core_preflight import bind
        protocol = bind(protocol_path, stage="prepare", condition="preflight", run_id=job["run_id"])
    validate_protocol(protocol)
    return job, protocol


def validate_protocol(protocol):
    """Validate the implementation contract without creating a job or claim."""
    if not isinstance(protocol, dict) or type(protocol.get("schema_version")) is not int or protocol.get("schema_version") != 1 or protocol.get("defects4j_version") != "3.0.1" or protocol.get("timezone") != "America/Los_Angeles":
        raise ValueError("Protocol must pin Defects4J 3.0.1 and test timezone")
    for field in ("seed", "budget", "test_method_cap"):
        positive_int(protocol.get(field), field, 0 if field == "seed" else 1)
    if protocol["budget"] > protocol["test_method_cap"]:
        raise ValueError("Algorithm input budget must not exceed the frozen suite cap")
    for field in ("command_timeout_seconds", "observation_timeout_seconds"):
        finite_positive(protocol.get(field), field)
    if protocol.get("target_selection") != "shared-declaration-signatures-v1":
        raise ValueError("This adapter implements only shared-declaration-signatures-v1")
    if protocol.get("compatibility_policy") != "none":
        raise ValueError("This worker makes no compatibility or semantic repairs")
    if protocol.get('fixture_policy_id') not in {None, 'beam-explicit-fixtures-v3-proposal'}:
        raise ValueError('Unknown fixture policy')
    if not implementation_matches(protocol.get("source_sha256")):
        raise ValueError("Frozen implementation hashes differ; obtain a new protocol from Aom")
    return protocol


def implementation_hashes():
    names = ["scripts/study/run.py", "scripts/study/generate.py", "scripts/study/evaluate.py", "algorithms/java/SqaProbe.java"]
    names += [p.relative_to(ROOT).as_posix() for p in sorted((ROOT / "algorithms/python/atcg").glob("*.py"))]
    # Other branches add their own client/server modules to the same package.
    # Pin this worker's actual dependencies without requiring unrelated files.
    modules = ("__init__", "adapters", "algorithm_worker", "common", "configuration", "environment",
               "evaluate_worker", "fixture_policy", "make_job", "pack_suite", "queue_client", "queue_connection", "validity", "worker",
               "ai_handoff", "api_worker", "beam_queue", "champ_bridge", "core_preflight", "prepare_worker", "queue_worker", "suite_resolver",
               "champ_queue", "context_export", "generate_worker", "kku_client", "lease", "models", "quota")
    names += [f"scripts/study/api854/{name}.py" for name in modules]
    names += ["experiments/configs/api854-20261003/model-selection.json"]
    return {name: sha256(ROOT / name) for name in names}


def implementation_matches(expected):
    return isinstance(expected, dict) and all(expected.get(name) == digest for name, digest in implementation_hashes().items())


def assert_implementation(expected):
    if not implementation_matches(expected):
        raise ValueError("Implementation changed during attempt; preserve evidence and use a new protocol")


def snapshot_implementation(output, expected):
    """Retain source bytes, not only hash labels, for each real attempt."""
    assert_implementation(expected)
    folder = Path(output) / "implementation"
    folder.mkdir(exist_ok=False)
    retained = implementation_hashes()
    for name, digest in retained.items():
        original = contained(ROOT, Path(name))
        destination = contained(folder, Path(name))
        data = original.read_bytes()
        if hashlib.sha256(data).hexdigest() != digest:
            raise ValueError(f"Implementation changed while snapshotting: {name}")
        destination.parent.mkdir(parents=True, exist_ok=True)
        with destination.open("xb") as stream:
            stream.write(data)
    write_json(folder / "source-hashes.json", retained)
    assert_implementation(expected)


def job_relative(job):
    return Path(job["run_id"], job["protocol_hash"], job["project"], str(job["bug_id"]), job["approach"], job["attempt_id"])


def contained(root, relative):
    root = Path(root).resolve()
    result = (root / relative).resolve()
    if not result.is_relative_to(root) or result == root:
        raise ValueError("Path escapes root")
    return result


def start_attempt(root, job, stage):
    parent = contained(root, job_relative(job))
    parent.mkdir(parents=True, exist_ok=True)
    output = parent / stage
    output.mkdir(exist_ok=False)
    write_json(output / "job.json", job)
    return output


def envelope(job, outcome, **values):
    return {"schema_version": 1, "job": job, "observed_outcome": outcome, "queue_published": False, **values}


@contextmanager
def cpu_slot(worktrees):
    """One CPU-heavy CLI worker per machine/worktree root until pilot approves scaling."""
    if sys.platform != "linux":
        # Windows is allowed only to record an unavailable environment.
        yield
        return
    import fcntl
    root = Path(worktrees).resolve()
    root.mkdir(parents=True, exist_ok=True)
    with (root / ".beam-cpu-slot.lock").open("a") as stream:
        try:
            fcntl.flock(stream, fcntl.LOCK_EX | fcntl.LOCK_NB)
        except BlockingIOError as error:
            raise RuntimeError("CPU slot busy; job remains unattempted. All workers on this host must use the same worktrees root.") from error
        try:
            yield
        finally:
            fcntl.flock(stream, fcntl.LOCK_UN)
