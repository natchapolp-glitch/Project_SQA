"""Build a fail-closed evidence checklist; never freezes a protocol or opens stages."""
import argparse
import datetime
import json
from pathlib import Path

from .common import ROOT, read_json, sha256, contained, implementation_hashes
from .preparation import validate, policy_for, encoded, digest, clean_targets, explicit_context
from .fixture_policy import validate_recipe
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


def inspect(root=ROOT, *, protocol_path=None, runner_path=None):
    """Check an explicitly selected pair, without approval or queue side effects.

    Missing human/provider evidence remains pending. This checker currently
    validates input bindings; it does not certify host, semantic or quota review.
    """
    if protocol_path is None or runner_path is None:
        raise ValueError("Explicit protocol and runner paths are required; no legacy fallback")
    root = Path(root).resolve()
    base = root / "experiments/configs/api854-20261003"
    protocol_path, runner_path = contained(root, protocol_path), contained(root, runner_path)
    protocol, plan = read_json(protocol_path), read_json(runner_path)
    generation = protocol.get("generation", {})
    policy = policy_for(generation.get("prepare_contract"))
    preparation = contained(root, protocol["preparation_artifacts"])
    index = read_json(preparation / "index.json")
    core = read_json(base / "protocol.core-frozen.json")
    ownership = read_json(base / "ownership.json")
    discovery_path = root / "output/api854-20261003/prepare-v3/index.json"
    discovery = read_json(discovery_path)
    discovery_rows = {(r['project'], r['bug_id']): r for r in discovery['records']}
    expected = {(r['project'], r['bug_id'], r['owner']) for r in core['pilot_bugs']}
    discovery_scope = {(r['project'], r['bug_id'], r['owner']) for r in discovery['records']}
    if (len(discovery['records']) != len(discovery_rows) or discovery_scope != expected
            or sum(r['target_count'] for r in discovery['records']) != 691):
        raise ValueError('Common discovery must retain the 20-bug / 691-declaration inventory')
    actual = [(r['project'], r['bug_id'], r['owner']) for r in index['records']]
    owners = {(r['project'], r['bug_id']): r['owner'] for r in ownership['bugs']}
    issues, eligible, selected_count, recipe_count, prompt_sizes = [], 0, 0, 0, []
    evidence_paths = {protocol_path, runner_path, preparation/'index.json', discovery_path,
                      base/'protocol.core-frozen.json', base/'ownership.json'}
    if len(actual) != len(set(actual)) or any(owners.get((p,b)) != o for p,b,o in actual):
        issues.append({'reason': 'Duplicated identity or ownership differs'})
    for row in index["records"]:
        try:
            folder = contained(preparation, f"{row['project']}-{row['bug_id']}")
            checksums = read_json(folder/'checksums.json')
            required = {'context-manifest.json', 'prepare-metadata.json', 'prompt.md', 'targets.json',
                        'prepare-policy.json', 'revision-proof.json'}
            if explicit_context(policy['contract']):
                required |= {'fixture-recipes.json', 'capability-exclusions.json'}
            if not required.issubset(checksums):
                raise ValueError('Required artifact missing from checksums')
            for name, expected_hash in checksums.items():
                file = contained(folder, name)
                if sha256(file) != expected_hash:
                    raise ValueError("Artifact checksum changed")
            manifest, metadata = read_json(folder/'context-manifest.json'), read_json(folder/'prepare-metadata.json')
            if (manifest['project'] != row['project'] or manifest['bug_id'] != row['bug_id']
                    or metadata['prepare_contract'] != policy['contract']
                    or any(row.get(key) != value for key,value in metadata.items())):
                raise ValueError('Index identity/metadata or selected protocol contract differs')
            recipe = read_json(folder/'fixture-recipes.json') if explicit_context(policy['contract']) else None
            prompt = (folder/'prompt.md').read_bytes()
            prompt_sizes.append(len(prompt))
            if metadata['prompt_utf8_bytes'] != len(prompt) or manifest['revision'] != str(row['bug_id'])+'f':
                raise ValueError('Prompt byte size or fixed revision identity differs')
            validate(manifest, metadata,
                     prompt, (folder / "targets.json").read_bytes(),
                     (folder / "prepare-policy.json").read_bytes(), require_eligible=True, fixture_recipe=recipe)
            if recipe is not None:
                validate_recipe(recipe, source_hashes=protocol.get('source_sha256', {}),
                                policy=policy['fixture_policy'])
            for source in manifest['source_files']:
                name = 'fixed-source/' + source['path']
                file = contained(folder, name)
                if name not in checksums or sha256(file) != source['sha256'] or file.stat().st_size != source['bytes']:
                    raise ValueError('Fixed context source bytes differ')
            proof = read_json(folder / "revision-proof.json")
            if proof.get("verified") is not True or proof["head"] != proof["fixed_tag_commit"]:
                raise ValueError("Fixed revision proof differs")
            original = discovery_rows[(row['project'],row['bug_id'])]
            original_folder = discovery_path.parent/f"{row['project']}-{row['bug_id']}"
            original_targets_path = original_folder/'targets.json'
            original_meta_path = original_folder/'prepare-metadata.json'
            original_proof_path = original_folder/'revision-proof.json'
            for file in (original_targets_path, original_meta_path, original_proof_path):
                if sha256(file) != read_json(original_folder/'checksums.json')[file.name]:
                    raise ValueError('Discovery artifact checksum differs')
            if original['fixed_source_sha256'] != metadata['fixed_source_sha256']:
                raise ValueError('Selected fixed-source discovery binding differs')
            original_meta = read_json(original_meta_path)
            original_proof = read_json(original_proof_path)
            if (original_meta['fixed_source_sha256'] != original['fixed_source_sha256']
                    or original_meta['targets_sha256'] != sha256(original_targets_path)
                    or original['targets_sha256'] != sha256(original_targets_path)
                    or original_meta['target_count'] != original['target_count']
                    or original['target_count'] != len(clean_targets(read_json(original_targets_path)['targets']))
                    or original_proof.get('verified') is not True
                    or original_proof['head'] != original_proof['fixed_tag_commit']
                    or proof['head'] != original_proof['head']
                    or (policy['contract'] in {'aom-beam-prepare-v4', 'aom-beam-prepare-v5'}
                        and metadata.get('previous_hashes', {}).get('v3_index_sha256') != sha256(discovery_path))):
                raise ValueError('Discovery index/metadata lineage differs')
            keys = lambda rows: {tuple(t[k] for k in policy['target_identity_fields']) for t in clean_targets(rows)}
            all_targets = keys(read_json(original_targets_path)['targets'])
            chosen = keys(read_json(folder/'targets.json')['targets'])
            if not chosen.issubset(all_targets):
                raise ValueError('Selected target not in common discovery')
            if recipe is not None:
                capability = read_json(folder/'capability-exclusions.json')
                excluded = keys([r['target'] for r in capability['excluded']])
                if (keys(capability['selected']) != chosen or chosen & excluded or chosen | excluded != all_targets
                        or capability['fixture_policy_id'] != policy['fixture_policy']):
                    raise ValueError('Capability selected/excluded partition differs')
                recipe_count += 1
            eligible += 1
            selected_count += len(chosen)
            evidence_paths.update({folder/'checksums.json', original_targets_path, original_meta_path,
                                   original_proof_path, original_folder/'checksums.json'})
        except (OSError, ValueError, KeyError, TypeError) as error:
            issues.append({"project": row["project"], "bug_id": row["bug_id"], "reason": str(error)})
    routes = route_coverage(plan, ownership)
    if (index.get('target_count') != selected_count or index.get('max_prompt_utf8_bytes') != max(prompt_sizes, default=0)):
        issues.append({'reason': 'Preparation index counts or prompt maximum differ from validated bytes'})
    complete_pilot = len(actual) == len(expected) == 20 and set(actual) == expected and not issues
    import_ref = protocol.get('preparation_import_evidence', {})
    index_bound = (import_ref.get('path') == (preparation/'index.json').relative_to(root).as_posix()
                   and import_ref.get('sha256') == sha256(preparation/'index.json'))
    source_pins = protocol.get('source_sha256', {})
    current = implementation_hashes()
    source_issues = []
    if not source_pins or set(source_pins) != set(current):
        source_issues.append('Incomplete runtime source inventory')
    for name, expected_hash in source_pins.items():
        try:
            if sha256(contained(root, name)) != expected_hash:
                source_issues.append(name)
        except (OSError, ValueError):
            source_issues.append(name)
    shared_policy_ok = (protocol.get('prepare_policy_sha256') == index.get('policy_sha256') == digest(encoded(policy))
                        and generation.get('context_policy_id') == policy['context_policy_id']
                        and generation.get('prompt_policy_id') == policy['prompt_policy_id']
                        and (not explicit_context(policy['contract']) or
                             protocol.get('fixture_policy_id') == generation.get('fixture_policy_id') == policy['fixture_policy'])
                        and not issues)
    checklist = [
        {"id": "prepare_contract", "owner": "aom", "status": "pass" if complete_pilot and not issues else "blocked", "bugs": len(index["records"]), "issues": issues},
        {"id": "protocol_runner_binding", "owner": "aom", "status": "pass" if protocol.get('runner_plan_sha256') == sha256(runner_path) else "blocked"},
        {"id": "preparation_index_binding", "owner": "aom", "status": "pass" if index_bound else "blocked"},
        {"id": "runtime_source_binding", "owner": "aom", "status": "pass" if not source_issues else "blocked", "issues": source_issues},
        {"id": "shared_policy", "owner": "aom", "status": "pass" if shared_policy_ok else "blocked", "contract":policy['contract']},
        {"id": "fixture_recipe_binding", "owner": "aom", "status": "pass" if not issues and (not explicit_context(policy['contract']) or recipe_count == len(actual)) else "blocked", "validated_recipe_bugs":recipe_count},
        {"id": "runner_coverage", "owner": "aom", "status": "pass" if not routes["gaps"] else "blocked", **routes},
        {"id": "runner_host_acceptance", "owner": "team", "status": "pending", "reason": "Host assignment prepared; host readiness and team review not received"},
        {"id": "eligible_targets_20", "owner": "beam", "status": "pass" if eligible == 20 and not issues else "pending", "bound_preparation_eligibility": eligible, "preparation_contract": policy["contract"],
         "reason": "Source-bound eligibility is separate from meaningful fixture/oracle review"},
        {"id": "all_common_declarations", "owner": "team", "status": "pass" if complete_pilot and selected_count == 691 else "blocked", "selected_declarations":selected_count, "required_declarations":691},
        {"id": "meaningful_oracles", "owner": "beam", "status": "pending",
         "reason": "No condition-bound joint semantic acceptance certified by this checker; local reviews do not authorize primary"},
        {"id": "model_settings_limits_reserve", "owner": "champ", "status": "pending", "max_prompt_utf8_bytes": index["max_prompt_utf8_bytes"], "reason": "Preflight request acceptance exists separately; effective limits/framing and final 20-bug reserve remain pending"},
        {"id": "observed_quota", "owner": "champ", "status": "pending", "reason": "Historical remaining snapshot does not certify current bucket/reset/expiry or ledger readiness"},
        {"id": "three_owner_review", "owner": "team", "status": "pending", "reviewed_by": {"aom": False, "champ": False, "beam": False}},
    ]
    return {"schema_version": 2, "checked_at_utc": datetime.datetime.now(datetime.timezone.utc).isoformat(),
            "scope":"Selected-pair input binding checklist; not final host/semantic/provider approval",
            "selected_protocol":protocol_path.relative_to(root).as_posix(), "selected_runner":runner_path.relative_to(root).as_posix(),
            "checker_sha256":sha256(__file__),
            "gate_a_passed": all(c["status"] == "pass" for c in checklist), "generation_authorized": False,
            "checklist": checklist, "evidence": [{"path": p.relative_to(root).as_posix(), "sha256": sha256(p)} for p in sorted(evidence_paths)],
            "queue_mutations": 0, "live_requests": 0}


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--protocol", type=Path, required=True)
    parser.add_argument("--runner", type=Path, required=True)
    parser.add_argument("--output", type=Path, required=True)
    args = parser.parse_args()
    result = inspect(protocol_path=args.protocol, runner_path=args.runner)
    args.output.parent.mkdir(parents=True, exist_ok=True)
    with args.output.open("xb") as stream:
        stream.write(encoded(result))
    print(json.dumps({"gate_a_passed": result["gate_a_passed"], "pending": [c["id"] for c in result["checklist"] if c["status"] != "pass"]}))


if __name__ == "__main__":
    main()
