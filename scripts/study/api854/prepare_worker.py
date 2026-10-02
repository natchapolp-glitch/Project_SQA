"""One preparation attempt with an explicit fixed-source/context export."""
from pathlib import Path
import json
import time

from .common import start_attempt, snapshot_implementation, assert_implementation, envelope, write_json, sha256
from .environment import inspect_environment
from .adapters import prepare_adapter
from .worker import new_worktrees, fixed_sources, artifact_index
from .context_export import BUILD_FILES, export_context
from .preparation import compose, POLICY_V3, receiver_paths

CONTEXT_POLICY = "modified-java-and-root-build-v1"
PROMPT_POLICY = "beam-fixed-targets-junit4-v1"


def export_prompt(job, protocol, targets, context_dir):
    generation = protocol.get("generation")
    if generation is None:
        return None  # Core preflight records context; it cannot approve an AI prompt.
    if (generation.get("context_policy_id") != CONTEXT_POLICY
            or generation.get("prompt_policy_id") != PROMPT_POLICY
            or protocol["test_method_cap"] != 30):
        raise ValueError("Preparation prompt/context policies differ from this implementation")
    # Only common declaration eligibility reaches the prompt, never discovery
    # errors, fixed/buggy differences, patch contents or execution feedback.
    prompt = (
        "Generate a deterministic JUnit 4 Java test suite from the supplied fixed source/build context.\n"
        f"Project: {job['project']}; fixed revision: {job['bug_id']}f.\n"
        "Use only the eligible target declarations listed below. Create at most 30 @Test methods "
        "with meaningful assertions derived from fixed API behavior and checks that reach the target. "
        "Avoid empty tests, assertions that only check non-null, external services, random inputs, "
        "and clock dependence. Do not modify or shadow production classes. "
        "Return complete Java files with explicit packages and public class names; separate multiple "
        "files into complete ```java fences. One complete Java file may be returned without fences. "
        "No explanation, partial files, compile/test feedback, pruning or repair loop.\n\n"
        "Eligible targets (shared declaration signatures and supported fixture types):\n"
        + json.dumps(targets, ensure_ascii=False, sort_keys=True, indent=2) + "\n\n"
        + (context_dir / "context.md").read_text(encoding="utf-8"))
    path = context_dir / "prompt.md"
    with path.open("xb") as stream:
        stream.write(prompt.encode("utf-8"))
    return {"prompt_sha256": sha256(path), "prompt_policy_id": PROMPT_POLICY,
            "context_policy_id": CONTEXT_POLICY, "prompt_utf8_bytes": path.stat().st_size}


def execute(job, protocol, results, worktrees, d4j):
    v3 = protocol.get("generation", {}).get("prepare_contract") == POLICY_V3["contract"]
    context_policy = POLICY_V3["context_policy_id"] if v3 else CONTEXT_POLICY
    if protocol.get("context_selection") != context_policy:
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
            extras = receiver_paths(targets, classes, source_dir) if v3 else []
            selected = sorted(set(sources) | set(extras) | {name for name in BUILD_FILES if (fixed_tree / name).is_file()}) if v3 else sorted(sources) + sorted(name for name in BUILD_FILES if (fixed_tree / name).is_file())
            context = export_context(fixed_tree, job["project"], job["bug_id"], selected,
                                     output / "context", policy_id=context_policy)
            preparation = None
            prompt = None
            if v3:
                fixture_classes = (output / "setup/fixture-classes.txt").read_text(encoding="utf-8").splitlines()
                preparation = compose(output / "context", classes=classes, targets=targets, policy=POLICY_V3,
                    modified_sources=sources, fixture_classes=fixture_classes)
            elif protocol.get("generation", {}).get("prompt_policy_id") == PROMPT_POLICY:
                prompt = export_prompt(job, protocol, targets, output / "context")
            else:
                preparation = compose(output / "context", classes=classes, targets=targets)
            assert_implementation(protocol["source_sha256"])
            result = envelope(job, "prepared" if targets else "adapter_unsupported",
                              fixed_source_sha256=sources, context=context, prompt=prompt,
                              targets_sha256=prepared["targets_sha256"], target_count=len(targets),
                              adapter=prepared, preparation=preparation, semantic_validity="pending_review")
    except Exception as error:
        result = envelope(job, "preflight_failed", error=f"{type(error).__name__}: {error}")
    result["worker_seconds"] = time.monotonic() - started
    result["artifacts"] = artifact_index(output)
    write_json(output / "result.json", result)
    return result
