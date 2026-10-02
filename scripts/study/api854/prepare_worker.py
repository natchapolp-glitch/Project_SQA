"""One preparation attempt with an explicit fixed-source/context export."""
from pathlib import Path
import time

from .common import start_attempt, snapshot_implementation, assert_implementation, envelope, write_json
from .environment import inspect_environment
from .adapters import prepare_adapter
from .worker import new_worktrees, fixed_sources, artifact_index
from .context_export import BUILD_FILES, export_context
from .preparation import compose

CONTEXT_POLICY = "modified-java-and-root-build-v1"


def execute(job, protocol, results, worktrees, d4j):
    if protocol.get("context_selection") != CONTEXT_POLICY:
        raise ValueError("Frozen protocol must declare this explicit fixed-context selection")
    output = start_attempt(results, job, "prepare")
    started = time.monotonic()
    try:
        snapshot_implementation(output, protocol["source_sha256"])
        environment = inspect_environment(d4j, output / "environment")
        if not environment["ready"]:
            result = envelope(job, "preflight_failed", issues=environment["issues"])
        else:
            trees = new_worktrees(worktrees, job, "prepare")
            prepared, targets = prepare_adapter(d4j, job, trees, output / "setup", protocol["command_timeout_seconds"])
            source_dir = (output / "setup/dir.src.classes.txt").read_text(encoding="utf-8").strip()
            classes = Path(prepared["classes_file"]).read_text(encoding="utf-8").splitlines()
            fixed_tree = Path(prepared["fixed_worktree"])
            sources = fixed_sources(fixed_tree, classes, source_dir)
            selected = sorted(sources) + sorted(name for name in BUILD_FILES if (fixed_tree / name).is_file())
            context = export_context(fixed_tree, job["project"], job["bug_id"], selected,
                                     output / "context", policy_id=CONTEXT_POLICY)
            preparation = compose(output / "context", classes=classes, targets=targets)
            assert_implementation(protocol["source_sha256"])
            result = envelope(job, "prepared" if targets else "adapter_unsupported",
                              fixed_source_sha256=sources, context=context,
                              targets_sha256=prepared["targets_sha256"], target_count=len(targets),
                              adapter=prepared, preparation=preparation, semantic_validity="pending_review")
    except Exception as error:
        result = envelope(job, "preflight_failed", error=f"{type(error).__name__}: {error}")
    result["worker_seconds"] = time.monotonic() - started
    result["artifacts"] = artifact_index(output)
    write_json(output / "result.json", result)
    return result
