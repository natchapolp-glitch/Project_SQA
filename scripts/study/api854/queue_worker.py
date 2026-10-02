"""Run one allocated prepare/algorithm/evaluation stage under Aom queue leases."""
from __future__ import annotations

import argparse
from pathlib import Path

from . import algorithm_worker, evaluate_worker, prepare_worker
from .common import (ROOT, APPROACHES, read_json, write_json, load_job, sha256, identifier,
                     cpu_slot, job_relative, implementation_matches)
from .beam_queue import BeamAccess, BeamQueueClient, FencedPublisher, bundle_evidence, download_generation
from .champ_queue import QueueError
from .lease import LeaseHeartbeat


def local_job(claim):
    job = claim["job"]
    return {"schema_version": 1, **{key: job[key] for key in
            ("run_id", "project", "bug_id", "approach", "protocol_hash")},
            "repeat_index": job.get("payload", {}).get("repeat_index", 1), "attempt_id": claim["attempt_id"]}


def stage_metadata(result, protocol, worker_id):
    measurement = result.get("measurement", {})
    review = result.get("validity", {}).get("review", {})
    counts = review.get("stage_counts")
    metadata = {"worker_id": worker_id, "raw_worker_outcome": result["observed_outcome"],
                "source_sha256": result.get("fixed_source_sha256"),
                "implementation_sha256": protocol["source_sha256"],
                "fixed_source_sha256": result.get("fixed_source_sha256"),
                "targets_sha256": result.get("targets_sha256"),
                "suite_sha256": result.get("suite_sha256"), "test_count": result.get("test_count"),
                "raw_evaluator_status": result.get("evaluator_status"),
                "failure_reason": result.get("error"), "usable": result.get("usable", False),
                "executed_tests": {s: c["executed"] for s, c in counts.items()} if counts else None,
                "skipped_tests": {s: c["skipped"] for s, c in counts.items()} if counts else None,
                "checks_reach_target": None, "semantic_validity": result.get("validity", {}).get("status", "pending_review"),
                "stage_results": measurement.get("stages")}
    for field in ("line_covered", "line_total", "branch_covered", "branch_total", "fault_detected"):
        metadata[field] = measurement.get(field)
    if result.get("context"):
        metadata["source_sha256"] = result["context"]["source_hash"]
        metadata["context_source_hash"] = result["context"]["source_hash"]
        metadata["context_selection"] = result["context"]["selection_policy_id"]
    if protocol.get("preparation_only"):
        metadata["execution_binding_scope"] = "preparation_only"
        metadata["approval_state"] = protocol["approval_state"]
        metadata["primary"] = False
    return metadata


def run_one(client, *, protocol_path, run_id, stage, approaches, worker_id, output, results,
            worktrees, d4j="defects4j", condition="development", lease_seconds=900, heartbeat_interval=60):
    protocol = read_json(protocol_path)
    if condition == "preflight" or protocol.get("state") == "frozen_core":
        from .core_preflight import bind
        protocol = bind(protocol_path, stage=stage, condition=condition, run_id=run_id)
    if stage not in {"prepare", "generate", "evaluate"} or not approaches or any(a not in APPROACHES for a in approaches):
        raise ValueError("Invalid stage/approaches before claim")
    if not implementation_matches(protocol.get("source_sha256")):
        raise ValueError("Protocol implementation hashes differ before claiming any work")
    if condition == "primary" and protocol.get("status") != "frozen":
        raise ValueError("Primary work requires Aom's frozen protocol")
    if condition == "development" and not run_id.startswith("beam-development-"):
        raise ValueError("Development work requires a separate beam-development-* run ID")
    if stage == "generate" and any(a not in {"cmaes", "fscs-art"} for a in approaches):
        raise ValueError("KKU generation is separate and requires API/account/prompt preflight")
    identifier(worker_id, "worker_id")
    protocol_hash = sha256(protocol_path)
    # Queue contract cannot filter claims by run/protocol. Refuse mixed eligible
    # queues before claiming; check identity again after claim to close the race.
    status = client.request("GET", "/v1/status")
    if status.get("schema_version") != "1.0":
        raise ValueError("Queue schema changed")
    if "enabled_stages" in status and not any(
            row.get("run_id") == run_id and row.get("protocol_hash") == protocol_hash and row.get("stage") == stage
            for row in status["enabled_stages"]):
        raise ValueError("Queue has not enabled this run/protocol/stage")
    eligible = [j for j in status.get("jobs", []) if j.get("state") == "queued" and j.get("stage") == stage
                and j.get("owner") == "beam" and j.get("approach") in approaches]
    if any(j.get("run_id") != run_id or j.get("protocol_hash") != protocol_hash for j in eligible):
        raise ValueError("Eligible queue mixes run IDs/protocols; controller must isolate the intended run")
    output = Path(output)
    output.mkdir(parents=True, exist_ok=False)
    if condition == "preflight":
        write_json(output / "execution-binding.json", protocol)
    write_json(output / "claim-intent.json", {"worker_id": worker_id, "stage": stage, "run_id": run_id,
                                             "protocol_hash": protocol_hash, "approaches": approaches})
    claim = client.claim(worker_id, stage, approaches, owner="beam", lease_seconds=lease_seconds)
    if not claim.get("job"):
        write_json(output / "receipt.json", {"state": "empty", "kku_requests": 0})
        return {"state": "empty", "kku_requests": 0}
    job = local_job(claim)
    if (job["run_id"] != run_id or job["protocol_hash"] != protocol_hash or claim["job"].get("stage") != stage
            or claim["job"].get("owner") != "beam" or job["approach"] not in approaches):
        raise ValueError("Claim identity changed; preserve claim intent and reconcile without executing")
    write_json(output / "job.json", job)  # No lease token is persisted in public evidence.
    job, protocol = load_job(output / "job.json", protocol_path, allow_core_preflight=condition == "preflight")
    heartbeat = LeaseHeartbeat(client, claim, lease_seconds=lease_seconds, interval_seconds=heartbeat_interval)
    try:
        with heartbeat:
            if stage == "prepare":
                result = prepare_worker.execute(job, protocol, results, worktrees, d4j)
            elif stage == "generate":
                result = algorithm_worker.execute(job, protocol, results, worktrees, d4j)
            else:
                suite, lineage = download_generation(client, claim, output / "generation-input")
                result = evaluate_worker.execute(job, protocol, results, worktrees, d4j, suite, lineage)
            heartbeat.check()
            source = Path(results) / job_relative(job) / ("generation" if stage == "generate" else "evaluation" if stage == "evaluate" else "prepare")
            publisher = FencedPublisher(client, claim, heartbeat, output / "publication")
            files = [source / "result.json", bundle_evidence(source, output / "evidence.tar.bz2", publisher.secrets)]
            if stage == "prepare" and result.get("context"):
                files += [source / "context/context-manifest.json", Path(result["adapter"]["targets_file"])]
            outcome = result["observed_outcome"]
            if stage == "generate" and outcome == "generated":
                prepared = [h for h in claim["job"].get("payload", {}).get("stage_history", [])
                            if h.get("stage") == "prepare" and h.get("outcome") == "prepared"]
                if not prepared or result.get("fixed_source_sha256") != prepared[-1]["metadata"].get("fixed_source_sha256"):
                    raise ValueError("Generation fixed-source hashes differ from successful preparation")
                suite = output / "suite.tar.bz2"
                suite.write_bytes(Path(result["generation"]["suite"]).read_bytes())
                if sha256(suite) != result["suite_sha256"]:
                    raise ValueError("Generation suite changed before upload")
                lineage_path = output / "generation-lineage.json"
                write_json(lineage_path, result)
                files += [suite, lineage_path]
            if stage == "generate" and outcome not in {"generated", "refused", "needs_reconciliation"}:
                outcome = "generation_failed"
            if stage == "evaluate" and outcome == "preflight_failed":
                outcome = "environment_failed"
            metadata = stage_metadata(result, protocol, worker_id)
            completed = publisher.publish(outcome, files, metadata)
        receipt = {"state": "published", "stage": stage, "job_id": completed["job_id"],
                   "attempt_id": claim["attempt_id"], "outcome": outcome, "usable": result.get("usable", False),
                   "kku_requests": 0, "condition": condition}
        write_json(output / "receipt.json", receipt)
        return receipt
    except Exception as error:
        # Stage evidence stays local; do not replay uncertain queue mutations.
        kind = error.kind if isinstance(error, QueueError) else type(error).__name__
        write_json(output / "publication-pending.json", {"state": "needs_reconciliation", "error_kind": kind,
                                                        "attempt_id": claim["attempt_id"], "kku_requests": 0})
        raise


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--access", type=Path, default=ROOT / ".local/api854/beam-access.private.json")
    parser.add_argument("--protocol", type=Path, required=True)
    parser.add_argument("--run-id", required=True)
    parser.add_argument("--stage", choices=("prepare", "generate", "evaluate"), required=True)
    parser.add_argument("--approaches", nargs="+", choices=sorted(APPROACHES), required=True)
    parser.add_argument("--worker-id", default="beam-pc1")
    parser.add_argument("--output", type=Path, required=True)
    parser.add_argument("--results", type=Path)
    parser.add_argument("--worktrees", type=Path, required=True)
    parser.add_argument("--d4j", default="defects4j")
    parser.add_argument("--condition", choices=("development", "preflight", "primary"), default="development")
    args = parser.parse_args()
    if args.results is None:
        args.results = ROOT / ("results/study" if args.condition == "primary" else "results/validation/api854-beam")
    client = BeamQueueClient(BeamAccess.load(args.access))
    try:
        client.check()
        with cpu_slot(args.worktrees):
            receipt = run_one(client, protocol_path=args.protocol, run_id=args.run_id, stage=args.stage,
                              approaches=args.approaches, worker_id=args.worker_id, output=args.output,
                              results=args.results, worktrees=args.worktrees, d4j=args.d4j, condition=args.condition)
    except QueueError as error:
        print(f"Queue {error.kind}; HTTP={error.status}; preserve evidence and reconcile")
        return 1
    print(f"{receipt['state']}; outcome={receipt.get('outcome')}; KKU requests=0")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
