"""One immutable CMA-ES/FSCS-ART job from JSON, without any API calls."""
from __future__ import annotations

import argparse
from pathlib import Path
import time

from .common import load_job, sha256, write_json, start_attempt, envelope, cpu_slot, snapshot_implementation, assert_implementation
from .environment import inspect_environment
from .adapters import prepare_adapter
from .worker import new_worktrees, fixed_sources, artifact_index
from generate import generate_suite


def execute(job, protocol, results, worktrees, d4j, *, prepared_input=None):
    if job["approach"] not in {"cmaes", "fscs-art"}:
        raise ValueError("algorithm_worker accepts only cmaes/fscs-art")
    from .preparation import shared_context
    shared_v3 = shared_context(protocol.get("generation", {}).get("prepare_contract"))
    if shared_v3 and not prepared_input:
        raise ValueError("Shared-v3 algorithms require the same bound preparation as API workers")
    output = start_attempt(results, job, "generation")
    started = time.monotonic()
    phase = "preflight"
    try:
        snapshot_implementation(output, protocol["source_sha256"])
        environment = inspect_environment(d4j, output / "environment")
        if not environment["ready"]:
            result = envelope(job, "preflight_failed", evaluation_attempted=False, issues=environment["issues"])
        else:
            phase = "adapter"
            trees = new_worktrees(worktrees, job, "generation")
            fixture_options = {'fixture_policy': protocol['fixture_policy_id']} if protocol.get('fixture_policy_id') else {}
            prepared, targets = prepare_adapter(d4j, job, trees, output / "setup", protocol["command_timeout_seconds"], **fixture_options)
            source_dir = (output / "setup/dir.src.classes.txt").read_text(encoding="utf-8").strip()
            classes = Path(prepared["classes_file"]).read_text(encoding="utf-8").strip().splitlines()
            sources = fixed_sources(prepared["fixed_worktree"], classes, source_dir)
            if shared_v3:
                from .preparation import clean_targets
                from .preparation import clean_fixture_classes
                if (sources != prepared_input["metadata"]["fixed_source_sha256"]
                        or clean_targets(targets) != prepared_input["targets"]
                        or clean_fixture_classes((output / "setup/fixture-classes.txt").read_text().splitlines()) != prepared_input["fixture_classes"]):
                    raise ValueError("Algorithm discovery/source/fixture inventory differs from shared preparation")
                fields = ("class", "constructor_types", "method", "parameter_types")
                targets = sorted(targets, key=lambda t: tuple(t[k] for k in fields))
                # Verify receiver/build context bytes, retaining modified-only coverage mapping.
                for entry in prepared_input["manifest"]["source_files"]:
                    from .common import contained
                    if sha256(contained(prepared["fixed_worktree"], entry["path"])) != entry["sha256"]:
                        raise ValueError("Algorithm fixed context differs from AI preparation")
            phase = "generation"
            generation = generate_suite(job["project"], job["bug_id"], job["approach"], protocol["budget"],
                                        protocol["seed"], targets, prepared["classpath"], output / "suite",
                                        protocol["observation_timeout_seconds"], **fixture_options)
            assert_implementation(protocol["source_sha256"])
            result = envelope(job, "generated" if generation["test_count"] else "generation_failed",
                              generation=generation, fixed_source_sha256=sources,
                              targets_sha256=prepared["targets_sha256"], implementation_sha256=prepared["source_sha256"],
                              semantic_validity="pending_review", evaluation_attempted=False)
            if generation["test_count"]:
                result["suite_sha256"] = sha256(generation["suite"])
                result["test_count"] = generation["test_count"]
                result["generation_seconds"] = generation["generation_seconds"]
                if shared_v3:
                    result.update(context_source_hash=prepared_input["metadata"]["context_source_hash"],
                                  shared_targets_sha256=prepared_input["metadata"]["targets_sha256"],
                                  prepare_attempt_id=prepared_input["prepare_attempt_id"])
                    for key in ('fixture_policy_id', 'fixture_recipes_sha256'):
                        if key in prepared_input['metadata']:
                            result[key] = prepared_input['metadata'][key]
    except Exception as error:
        outcome = "generation_failed" if phase == "generation" else "preflight_failed"
        result = envelope(job, outcome, error=f"{type(error).__name__}: {error}", failed_phase=phase,
                          evaluation_attempted=False)
    result["worker_seconds"] = time.monotonic() - started
    result["artifacts"] = artifact_index(output)
    write_json(output / "result.json", result)
    return result


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    for name in ("job", "protocol", "results", "worktrees"):
        parser.add_argument("--" + name, type=Path, required=True)
    parser.add_argument("--d4j", default="defects4j")
    args = parser.parse_args()
    job, protocol = load_job(args.job, args.protocol)
    with cpu_slot(args.worktrees):
        result = execute(job, protocol, args.results, args.worktrees, args.d4j)
    print(f"{job['project']}-{job['bug_id']}: {result['observed_outcome']}; queue_published=False")
    return 0 if result["observed_outcome"] == "generated" else 1


if __name__ == "__main__":
    raise SystemExit(main())
