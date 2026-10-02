"""Build a fail-closed evidence checklist; never freezes a protocol or opens stages."""
import argparse
import datetime
import json
from pathlib import Path

from .common import ROOT, read_json, sha256
from .preparation import validate, POLICY, encoded, digest
from .team_queue import assignment


def route_coverage(plan, ownership):
    covered, gaps = 0, []
    for row in ownership["bugs"]:
        for approach in ("cmaes", "fscs-art", "kku-claude", "kku-gemini"):
            for stage in ("prepare", "generate", "evaluate"):
                hosts = []
                for actor in plan["runners"]:
                    try:
                        assignment(plan, actor["worker_id"], actor["role"], row["owner"], stage, [approach])
                    except ValueError:
                        continue
                    hosts.append(actor["worker_id"])
                if hosts:
                    covered += 1
                else:
                    gaps.append({**row, "stage": stage, "approach": approach})
    return {"covered_stage_keys": covered, "expected_stage_keys": len(ownership["bugs"]) * 12, "gaps": gaps}


def inspect(root=ROOT):
    root = Path(root)
    base = root / "experiments/configs/api854-20261003"
    preparation = root / "output/api854-20261003/prepare-v2"
    index = read_json(preparation / "index.json")
    core = read_json(base / "protocol.core-frozen.json")
    issues, eligible = [], 0
    for row in index["records"]:
        folder = preparation / f"{row['project']}-{row['bug_id']}"
        try:
            for name, expected in read_json(folder / "checksums.json").items():
                file = (folder / name).resolve(strict=True)
                if not file.is_relative_to(folder.resolve()) or sha256(file) != expected:
                    raise ValueError("Artifact checksum changed")
            validate(read_json(folder / "context-manifest.json"), read_json(folder / "prepare-metadata.json"),
                     (folder / "prompt.md").read_bytes(), (folder / "targets.json").read_bytes(),
                     (folder / "prepare-policy.json").read_bytes())
            proof = read_json(folder / "revision-proof.json")
            if not proof.get("verified") or proof["head"] != proof["fixed_tag_commit"]:
                raise ValueError("Fixed revision proof differs")
            eligible += int(read_json(folder / "prepare-metadata.json")["adapter_eligibility_verified"])
        except (OSError, ValueError, KeyError) as error:
            issues.append({"project": row["project"], "bug_id": row["bug_id"], "reason": str(error)})
    protocol, plan = read_json(base / "protocol.json"), read_json(base / "runner-plan.v1.json")
    discovery = read_json(root / "output/api854-provider-preflight-20261003/champ-readiness-v2.json")
    scan = read_json(root / "docs/api854/evidence/beam-pilot-adapters-20261003.json")
    routes = route_coverage(plan, read_json(base / "ownership.json"))
    identities = lambda rows: {(r["project"], r["bug_id"], r["owner"]) for r in rows}
    complete_pilot = len(index["records"]) == 20 and identities(index["records"]) == identities(core["pilot_bugs"])
    checklist = [
        {"id": "prepare_contract", "owner": "aom", "status": "pass" if complete_pilot and not issues else "blocked", "bugs": len(index["records"]), "issues": issues},
        {"id": "shared_policy", "owner": "aom", "status": "pass" if protocol.get("prepare_policy_sha256") == digest(encoded(POLICY)) == sha256(base / "prepare-policy.v2.json") else "blocked"},
        {"id": "runner_coverage", "owner": "aom", "status": "pass" if not routes["gaps"] else "blocked", **routes},
        {"id": "runner_host_acceptance", "owner": "team", "status": "pending", "reason": "Host assignment prepared; host readiness and team review not received"},
        {"id": "model_discovery", "owner": "champ", "status": "pass" if {r['id'] for r in discovery['models']['selected_rows']} == {'claude-sonnet-5', 'gemini-3.5-flash-lite'} else "blocked", "scope": "catalog only, not resolved generation versions"},
        {"id": "eligible_targets_20", "owner": "beam", "status": "pass" if eligible == 20 else "pending", "bound_v2_eligibility": eligible, "beam_scan_receipts": len(scan["bugs"]), "reason": "Receipt hashes do not substitute for source-bound target/fixture artifacts"},
        {"id": "meaningful_oracles", "owner": "beam", "status": "pending", "reason": "fixed twice/buggy/coverage and semantic review for agreed pipeline; fixture smoke alone is insufficient"},
        {"id": "model_settings_limits_reserve", "owner": "champ", "status": "pending", "max_prompt_utf8_bytes": index["max_prompt_utf8_bytes"], "reason": "temperature 0/output 4096 proposed; provider limits/framing and runtime acceptance missing"},
        {"id": "observed_quota", "owner": "champ", "status": "pending", "reason": "remaining/bucket/expiry observation and ledger evidence missing"},
        {"id": "three_owner_review", "owner": "team", "status": "pending", "reviewed_by": {"aom": False, "champ": False, "beam": False}},
    ]
    evidence_paths = [base / name for name in ("protocol.core-frozen.json", "protocol.json", "prepare-policy.v2.json", "runner-plan.v1.json")]
    evidence_paths += [preparation / "index.json", root / "output/api854-provider-preflight-20261003/champ-readiness-v2.json",
                      root / "output/api854-provider-preflight-20261003/champ-aom-preparation-audit-v1.json",
                      root / "docs/api854/evidence/beam-pilot-adapters-20261003.json",
                      root / "docs/api854/evidence/beam-local-queue-smoke-20261003.json"]
    return {"schema_version": 1, "checked_at_utc": datetime.datetime.now(datetime.timezone.utc).isoformat(),
            "gate_a_passed": all(c["status"] == "pass" for c in checklist), "generation_authorized": False,
            "checklist": checklist, "evidence": [{"path": p.relative_to(root).as_posix(), "sha256": sha256(p)} for p in evidence_paths],
            "queue_mutations": 0, "live_requests": 0}


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--output", type=Path, required=True)
    args = parser.parse_args()
    result = inspect()
    args.output.parent.mkdir(parents=True, exist_ok=True)
    with args.output.open("xb") as stream:
        stream.write(encoded(result))
    print(json.dumps({"gate_a_passed": result["gate_a_passed"], "pending": [c["id"] for c in result["checklist"] if c["status"] != "pass"]}))


if __name__ == "__main__":
    main()
