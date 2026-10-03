"""One preparation attempt with an explicit fixed-source/context export."""
from pathlib import Path
import json
import time

from .common import ROOT, start_attempt, snapshot_implementation, assert_implementation, envelope, write_json, sha256
from .environment import inspect_environment
from .adapters import prepare_adapter
from .worker import new_worktrees, fixed_sources, artifact_index
from .context_export import BUILD_FILES, export_context
from .preparation import compose, POLICY_V3, policy_for, shared_context, explicit_context, receiver_paths

CONTEXT_POLICY = "modified-java-and-root-build-v1"
PROMPT_POLICY = "beam-fixed-targets-junit4-v1"
EXPLICIT_PROMPT_POLICY = 'beam-fixed-targets-explicit-fixtures-v3-junit4-proposal'
SCALAR_PROMPT_POLICY = 'beam-fixed-targets-explicit-fixtures-v4-junit4-proposal'
PILOT_PROMPT_POLICY = 'beam-fixed-targets-explicit-fixtures-v5-junit4-proposal'
BUFFER_PROMPT_POLICY = 'beam-fixed-targets-explicit-fixtures-v6-buffer-junit4-proposal'


def explicit_prompt_policy(fixture_policy):
    from .fixture_policy import POLICY, POLICY_V4, POLICY_V5, POLICY_V6_BUFFER
    return {POLICY: EXPLICIT_PROMPT_POLICY, POLICY_V4: SCALAR_PROMPT_POLICY, POLICY_V5: PILOT_PROMPT_POLICY,
            POLICY_V6_BUFFER: BUFFER_PROMPT_POLICY}[fixture_policy]


def export_prompt(job, protocol, targets, context_dir):
    generation = protocol.get("generation")
    if generation is None:
        return None  # Core preflight records context; it cannot approve an AI prompt.
    fixture_policy = protocol.get('fixture_policy_id')
    prompt_policy = explicit_prompt_policy(fixture_policy) if fixture_policy else PROMPT_POLICY
    if (generation.get("context_policy_id") != CONTEXT_POLICY
            or generation.get("prompt_policy_id") != prompt_policy
            or protocol["test_method_cap"] != 30):
        raise ValueError("Preparation prompt/context policies differ from this implementation")
    fixture_metadata, support = {}, ''
    if fixture_policy:
        from .fixture_policy import select
        if generation.get('fixture_policy_id') != fixture_policy or select(targets, fixture_policy)[1]:
            raise ValueError('Explicit fixture targets/policy must be frozen consistently')
        names = ['algorithms/java/SqaProbe.java', 'scripts/study/api854/fixture_policy.py']
        sources = {name: (ROOT / name).read_bytes().decode('utf-8') for name in names}
        hashes = {name: sha256(ROOT / name) for name in names}
        if any(protocol.get('source_sha256', {}).get(name) != value for name, value in hashes.items()):
            raise ValueError('Explicit fixture recipe source hashes differ from protocol')
        recipe = {'schema_version': 1, 'fixture_policy_id': fixture_policy, 'source_sha256': hashes,
            'sources': sources, 'scope': 'Same fixture construction/projection knowledge for all four approaches; no execution feedback'}
        recipe_path = context_dir / 'fixture-recipes.json'
        write_json(recipe_path, recipe)
        fixture_metadata = {'fixture_policy_id': fixture_policy, 'fixture_recipes_sha256': sha256(recipe_path)}
        support = ('\n\nExplicit fixture policy and recipe definitions (generation support, separate from production source):\n'
            'Use these definitions to construct valid non-null receiver/dependency graphs and meaningful state assertions. '
            'Text/processing-instruction targets need those node kinds. Iterator/sort anchors belong to the receiver. '
            'Setup failures are not target-method observations. Do not change production code.\n```json\n'
            + recipe_path.read_text(encoding='utf-8') + '```\n')
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
        + (context_dir / "context.md").read_text(encoding="utf-8") + support)
    path = context_dir / "prompt.md"
    with path.open("xb") as stream:
        stream.write(prompt.encode("utf-8"))
    return {"prompt_sha256": sha256(path), "prompt_policy_id": prompt_policy,
            "context_policy_id": CONTEXT_POLICY, "prompt_utf8_bytes": path.stat().st_size, **fixture_metadata}


def execute(job, protocol, results, worktrees, d4j):
    contract = protocol.get('generation', {}).get('prepare_contract')
    v3 = shared_context(contract)
    explicit = explicit_context(contract)
    if protocol.get('fixture_policy_id') and (v3 or contract == 'aom-beam-prepare-v2') and not explicit:
        raise ValueError('Explicit fixtures require a new shared preparation contract (shared-v4 or shared-v5) with matching recipe and prompt')
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
            fixture_options = {'fixture_policy': protocol['fixture_policy_id']} if protocol.get('fixture_policy_id') else {}
            prepared, targets = prepare_adapter(d4j, job, trees, output / "setup", protocol["command_timeout_seconds"], **fixture_options)
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
                from .fixture_policy import recipe_document
                recipe = recipe_document(protocol['source_sha256'], protocol['fixture_policy_id']) if explicit else None
                preparation = compose(output / "context", classes=classes, targets=targets, policy=policy_for(contract),
                    modified_sources=sources, fixture_classes=fixture_classes, fixture_recipe=recipe)
            elif protocol.get("generation", {}).get("prompt_policy_id") in {PROMPT_POLICY, EXPLICIT_PROMPT_POLICY, SCALAR_PROMPT_POLICY, PILOT_PROMPT_POLICY, BUFFER_PROMPT_POLICY}:
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
