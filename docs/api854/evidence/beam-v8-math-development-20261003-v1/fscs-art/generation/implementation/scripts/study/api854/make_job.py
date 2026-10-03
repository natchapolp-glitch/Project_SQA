"""Build one local job JSON from an explicitly selected allocated project/bug."""
import argparse
from pathlib import Path

from .adapters import allocation_rows
from .common import ROOT, APPROACHES, identifier, positive_int, sha256, write_json, load_job


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    for name in ("project", "run-id", "attempt-id"):
        parser.add_argument("--" + name, required=True)
    parser.add_argument("--bug-id", type=int, required=True)
    parser.add_argument("--approach", choices=sorted(APPROACHES), required=True)
    parser.add_argument("--protocol", type=Path, required=True)
    parser.add_argument("--output", type=Path, required=True)
    args = parser.parse_args()
    for value, name in ((args.project, "project"), (args.run_id, "run_id"), (args.attempt_id, "attempt_id")):
        identifier(value, name)
    positive_int(args.bug_id, "bug_id")
    rows = allocation_rows(ROOT / "experiments/configs/api854-20261003/ownership.json")
    if (args.project, args.bug_id) not in {(r['project'], r['bug_id']) for r in rows}:
        parser.error("Project/bug is not in the 854 active allocation")
    job = {"schema_version": 1, "run_id": args.run_id, "project": args.project, "bug_id": args.bug_id,
           "approach": args.approach, "protocol_hash": sha256(args.protocol), "repeat_index": 1, "attempt_id": args.attempt_id}
    args.output.parent.mkdir(parents=True, exist_ok=True)
    write_json(args.output, job)
    load_job(args.output, args.protocol)
    print(f"Local job only: {args.output}; no claim or API request sent")


if __name__ == "__main__":
    main()
