"""Evaluate an unchanged hash-bound suite; semantic review gates usable counts."""
from __future__ import annotations

import argparse
from pathlib import Path
import time

from .common import load_job, read_json, sha256, write_json, start_attempt, envelope, positive_int, contained, cpu_slot, snapshot_implementation, assert_implementation
from .environment import inspect_environment
from .worker import new_worktrees, prepare_evaluation, normalize_evaluation, artifact_index
from evaluate import EvaluationConfig, evaluate_run, validate_archive


def validate_lineage(job, lineage, suite, protocol):
    original = lineage.get("job")
    # Queue schema 1.0 allocates a new attempt for each stage. Generation and
    # evaluation share a job key while keeping their distinct attempt IDs.
    identity = ("schema_version", "run_id", "project", "bug_id", "approach", "protocol_hash", "repeat_index")
    if not isinstance(original, dict) or any(original.get(k) != job.get(k) for k in identity) or lineage.get("observed_outcome") != "generated":
        raise ValueError("Generation lineage must belong to this exact job key")
    from .common import identifier
    identifier(original.get("attempt_id"), "generation attempt_id")
    if lineage.get("suite_sha256") != sha256(suite):
        raise ValueError("Suite hash differs from generation; no implicit repairs")
    count = positive_int(lineage.get("test_count"), "test_count")
    if count > protocol["test_method_cap"]:
        raise ValueError("Suite exceeds protocol test-method cap")
    if not lineage.get("fixed_source_sha256"):
        raise ValueError("Generation must retain fixed source hashes")
    validate_archive(Path(suite))
    return count


def review_validity(path, suite_hash, source_hashes, record, evidence_root=None):
    if path is None:
        return {"status": "pending_review", "usable": False, "reason": "No target-execution/semantic review supplied"}
    review = read_json(path)
    if review.get("suite_sha256") != suite_hash or review.get("fixed_source_sha256") != source_hashes:
        raise ValueError("Validity review hashes differ from evaluated source/suite")
    for field in ("reviewer", "target_execution_evidence", "fixture_oracle_review", "weak_oracles"):
        if not review.get(field):
            raise ValueError(f"Validity review lacks {field}")
    if evidence_root is None:
        raise ValueError("Validity review requires retained evidence root")
    def verify_reference(ref):
        if not isinstance(ref, dict) or set(ref) != {"path", "sha256"}:
            raise ValueError("Evidence reference requires relative path and sha256")
        retained = contained(evidence_root, Path(ref["path"]))
        if not retained.is_file() or sha256(retained) != ref["sha256"]:
            raise ValueError("Validity evidence absent or hash differs")
    references = review["target_execution_evidence"]
    if not isinstance(references, list) or not references:
        raise ValueError("Target execution review needs evidence references")
    for ref in references:
        verify_reference(ref)
    counts = review.get("stage_counts", {})
    for stage in ("fixed-1", "fixed-2", "buggy", "coverage"):
        entry = counts.get(stage, {})
        positive_int(entry.get("executed"), f"{stage}.executed")
        positive_int(entry.get("skipped"), f"{stage}.skipped", 0)
        positive_int(entry.get("target_checks"), f"{stage}.target_checks")
        verify_reference(entry.get("evidence"))
    if review.get("verdict") not in {"valid", "invalid"}:
        raise ValueError("Review verdict must be valid or invalid")
    valid = review["verdict"] == "valid"
    return {"status": review["verdict"], "usable": valid and record["status"] == "complete",
            "review_sha256": sha256(path), "review": review}


def execute(job, protocol, results, worktrees, d4j, suite, lineage, review=None):
    count = validate_lineage(job, lineage, suite, protocol)
    output = start_attempt(results, job, "evaluation")
    frozen_suite = output / "input-suite.tar.bz2"
    # Copy once, then evaluate the retained bytes. Future changes to input cannot affect execution.
    frozen_suite.write_bytes(Path(suite).read_bytes())
    if sha256(frozen_suite) != lineage["suite_sha256"]:
        raise ValueError("Suite changed while being retained")
    write_json(output / "lineage.json", lineage)
    started = time.monotonic()
    phase = "preflight"
    try:
        snapshot_implementation(output, protocol["source_sha256"])
        environment = inspect_environment(d4j, output / "environment")
        if not environment["ready"]:
            result = envelope(job, "preflight_failed", evaluation_attempted=False, issues=environment["issues"], usable=False)
        else:
            trees = new_worktrees(worktrees, job, "evaluation")
            paths, classes, sources = prepare_evaluation(d4j, job, trees, output / "setup", protocol["command_timeout_seconds"])
            if sources != lineage["fixed_source_sha256"]:
                raise ValueError("Checked-out fixed source differs from generation source")
            phase = "evaluation"
            record = evaluate_run(EvaluationConfig(
                project=job["project"], bug_id=job["bug_id"], generator=job["approach"],
                seed=protocol["seed"], budget=protocol["budget"], suite=frozen_suite,
                buggy_worktree=paths["b"], fixed_worktree=paths["f"], output=output / "measurement",
                d4j=d4j, classes_file=classes, test_count=count,
                generation_seconds=lineage.get("generation_seconds"), timeout_seconds=protocol["command_timeout_seconds"]))
            assert_implementation(protocol["source_sha256"])
            if (record.get("suite_sha256") is not None and record["suite_sha256"] != lineage["suite_sha256"]) or sha256(frozen_suite) != lineage["suite_sha256"]:
                raise ValueError("Evaluator suite hash differs from retained generation suite")
            result = envelope(job, normalize_evaluation(record), evaluator_status=record["status"],
                              evaluation_attempted=True, measurement=record, fixed_source_sha256=sources,
                              suite_sha256=lineage["suite_sha256"], usable=False)
            # Bad or missing review never discards measured failures/coverage.
            try:
                validity = review_validity(review, lineage["suite_sha256"], sources, record, output)
                result.update(validity=validity, usable=validity["usable"])
                if review is not None:
                    write_json(output / "validity-review.json", read_json(review))
            except (ValueError, OSError) as error:
                result["validity"] = {"status": "review_rejected", "reason": str(error)}
    except Exception as error:
        result = envelope(job, "preflight_failed" if phase == "preflight" else "environment_failed",
                          failed_phase=phase, error=f"{type(error).__name__}: {error}",
                          evaluation_attempted=phase == "evaluation", usable=False)
    result["worker_seconds"] = time.monotonic() - started
    result["artifacts"] = artifact_index(output)
    write_json(output / "result.json", result)
    return result


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    for name in ("job", "protocol", "results", "worktrees", "suite", "lineage"):
        parser.add_argument("--" + name, type=Path, required=True)
    parser.add_argument("--validity-review", type=Path)
    parser.add_argument("--d4j", default="defects4j")
    args = parser.parse_args()
    job, protocol = load_job(args.job, args.protocol)
    with cpu_slot(args.worktrees):
        result = execute(job, protocol, args.results, args.worktrees, args.d4j, args.suite,
                         read_json(args.lineage), args.validity_review)
    print(f"{job['project']}-{job['bug_id']}: {result['observed_outcome']}; usable={result['usable']}; queue_published=False")
    return 0 if result["observed_outcome"] == "complete" else 1


if __name__ == "__main__":
    raise SystemExit(main())
