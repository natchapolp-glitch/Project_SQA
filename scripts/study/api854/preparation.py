"""Shared fixed-context/prompt contract. No logs, implicit approval, or API calls."""
import hashlib
import json
import re
from pathlib import Path, PurePosixPath

CONTRACT = "aom-beam-prepare-v2"
CONTEXT_POLICY = "modified-java-and-root-build-v1"
PROMPT_POLICY = "shared-fixed-targets-junit4-v2"
POLICY = {
    "schema_version": 1, "contract": CONTRACT, "context_policy_id": CONTEXT_POLICY,
    "prompt_policy_id": PROMPT_POLICY, "target_selection": "shared-declaration-signatures-v1",
    "fixture_policy": "common-fixed-buggy-production-types-v1",
    "fixture_selection": "SqaProbe discovery with sorted common compiled production types; no buggy outcomes",
    "target_identity_fields": ["class", "constructor_types", "method", "parameter_types"],
    "context": "all modified fixed production Java and known root build files; same bytes across four approaches",
    "ai_inputs": "fixed context, shared declaration signatures and fixture types only; no patches, triggers or execution logs",
    "suite_packaging": "beam-java-suite-v1", "test_method_cap": 30,
    "over_cap": "reject entire suite as generation_failed; preserve raw output; no trimming or semantic retry",
    "method_count": "lexical JUnit4 annotations for packaging; executed/skipped measured separately",
    "compatibility_policy": "none", "extraction": "complete Java fences in source order, package/class file layout only",
    "assertion_edits": False, "fixed_failure_pruning": False, "semantic_repairs": "new disclosed condition only",
    "oracle_policy": "meaningful deterministic assertions; fixed twice, same suite buggy and coverage; semantic review required",
    "seed": 101, "budget": 30, "fixed_observations_per_proposal": 2,
    "approval_state": "proposal_pending_three_owner_review",
}
POLICY_V3 = {**POLICY, "contract": "aom-beam-prepare-v3",
    "context_policy_id": "modified-java-root-build-and-shared-receivers-v3",
    "prompt_policy_id": "shared-fixed-targets-junit4-v3",
    "context": "modified fixed Java, shared concrete receiver Java and known root build files; identical across four approaches",
    "fixture_inventory": "sorted common compiled production classes; discovery does not approve meaningful construction",
    "lineage": "fixed_source_sha256 is modified-only; additional_receiver_source_sha256 is separate; context_source_hash covers all selected files"}
POLICY_V4 = {**POLICY_V3, "contract": "aom-beam-prepare-v4",
    "prompt_policy_id": "shared-fixed-targets-explicit-fixtures-junit4-v4",
    "fixture_policy": "beam-explicit-fixtures-v3-proposal",
    "fixture_selection": "predeclared explicit capability recipes before observations; preserve every exclusion; no buggy outcomes",
    "fixture_recipe": "hash-bound SqaProbe and capability source bytes supplied identically to CPU and AI; separate from fixed production sources",
    "scope": "development proposal for Closure-176/JxPath-1 only; remaining 18 pilot bugs have no recipe approval"}


POLICY_V5 = {**POLICY_V4, 'contract': 'aom-beam-prepare-v5',
    'prompt_policy_id': 'shared-fixed-targets-explicit-fixtures-junit4-v5',
    'fixture_policy': 'beam-explicit-fixtures-v4-proposal',
    'scope': 'five-bug development proposal only; remaining 15 pilot bugs lack reviewed recipes'}

POLICY_V6 = {**POLICY_V5, 'contract': 'aom-beam-prepare-v6-development',
    'prompt_policy_id': 'shared-fixed-targets-explicit-fixtures-junit4-v6-development',
    'fixture_policy': 'beam-explicit-fixtures-v5-proposal',
    'scope': '20-bug development candidate; capability subset only, not full 691-declaration or team approval',
    'development_bugs': [['Chart',1],['Cli',1],['Closure',1],['Closure',176],['Codec',1],['Collections',1],
        ['Compress',1],['Csv',1],['Gson',1],['JacksonCore',1],['JacksonDatabind',1],['JacksonDatabind',112],
        ['JacksonXml',1],['Jsoup',1],['JxPath',1],['JxPath',22],['Lang',1],['Math',1],['Mockito',1],['Time',1]]}


def explicit_context(contract):
    return contract in {POLICY_V4['contract'], POLICY_V5['contract'], POLICY_V6['contract']}


def explicit_scope(policy):
    if policy == POLICY_V6:
        return {tuple(row) for row in policy['development_bugs']}
    original = {('Closure', 176), ('JxPath', 1)}
    return original | {('Codec', 1), ('Collections', 1), ('Csv', 1)} if policy == POLICY_V5 else original


def shared_context(contract):
    return contract == POLICY_V3['contract'] or explicit_context(contract)


def policy_for(contract):
    for policy in (POLICY, POLICY_V3, POLICY_V4, POLICY_V5, POLICY_V6):
        if policy["contract"] == contract:
            return policy
    raise ValueError("Unknown shared preparation contract")


def receiver_paths(targets, classes, source_dir):
    names = {row["class"] for row in clean_targets(targets)} - set(classes)
    return sorted({source_dir.rstrip("/") + "/" + name.split("$", 1)[0].replace(".", "/") + ".java" for name in names})


def clean_fixture_classes(classes):
    if any(not isinstance(c, str) for c in classes):
        raise ValueError("Malformed compiled fixture inventory")
    return sorted({c for c in classes if re.fullmatch(r"[A-Za-z_$][\w$]*(?:\.[A-Za-z_$][\w$]*)*", c)})


def java_mapping(manifest, metadata):
    """Validate modified/receiver partition without widening evaluator targets."""
    sources = {}
    for row in manifest["source_files"]:
        name = row["path"]
        path = PurePosixPath(name)
        if path.is_absolute() or ".." in path.parts or "\\" in name or ":" in name or name in sources:
            raise ValueError("Unsafe or duplicated context path")
        sources[name] = row["sha256"]
    java = {p: h for p, h in sources.items() if p.endswith(".java")}
    fixed = metadata.get("fixed_source_sha256")
    if not isinstance(fixed, dict) or not fixed or any(java.get(p) != h for p, h in fixed.items()):
        raise ValueError("Modified fixed-source mapping differs")
    additional = {p: h for p, h in java.items() if p not in fixed}
    if shared_context(metadata.get("prepare_contract")):
        if metadata.get("additional_receiver_source_sha256") != additional:
            raise ValueError("Additional receiver source mapping differs")
    elif java != fixed:
        raise ValueError("Legacy preparation cannot include receiver supplements")
    return fixed, additional


def render_context(directory, files):
    sections = []
    for row in files:
        text = (directory / "fixed-source" / row["path"]).read_bytes().decode("utf-8")
        fence = "`" * max(3, max((len(x) for x in re.findall(r"`+", text)), default=0) + 1)
        sections.append(f"## {row['path']}\n\n{fence}\n{text}\n{fence}\n")
    return "\n".join(sections)


def encoded(value):
    return (json.dumps(value, ensure_ascii=False, sort_keys=True, indent=2) + "\n").encode("utf-8")


def digest(data):
    return hashlib.sha256(data).hexdigest()


def clean_targets(targets):
    """Expose declaration inputs only, never discovery errors or buggy behavior."""
    fields = POLICY["target_identity_fields"]
    result = []
    seen = set()
    for row in targets:
        if not isinstance(row, dict) or any(not isinstance(row.get(k), str) for k in fields):
            raise ValueError("Malformed shared target declaration")
        if (not re.fullmatch(r"[A-Za-z_$][\w$]*(?:\.[A-Za-z_$][\w$]*)*", row["class"])
                or not re.fullmatch(r"<init>|[A-Za-z_$][\w$]*", row["method"])
                or any(not re.fullmatch(r"[A-Za-z0-9_$.,;\[\]]*", row[k]) for k in ("constructor_types", "parameter_types"))):
            raise ValueError("Target inventory may contain declaration signatures only")
        identity = tuple(row[k] for k in fields)
        if identity in seen:
            raise ValueError("Duplicate target signature")
        seen.add(identity)
        result.append({k: row[k] for k in fields})
    return sorted(result, key=lambda row: tuple(row[k] for k in fields))


def compose(directory: Path, *, classes, targets=None, previous_hashes=None,
            policy=POLICY, modified_sources=None, fixture_classes=None, fixture_recipe=None):
    """Add immutable artifacts to an existing export_context destination."""
    manifest_path = directory / "context-manifest.json"
    manifest = json.loads(manifest_path.read_text(encoding="utf-8"))
    if policy != policy_for(policy["contract"]):
        raise ValueError("Shared policy is not implemented")
    v3 = shared_context(policy['contract'])
    explicit = explicit_context(policy['contract'])
    if manifest["selection_policy_id"] != policy["context_policy_id"] or manifest["contains_execution_logs"] is not False:
        raise ValueError("Fixed context selection differs from shared policy")
    files = manifest["source_files"]
    source_hash = digest(json.dumps(files, sort_keys=True, separators=(",", ":")).encode())
    if manifest["source_hash"] != source_hash:
        raise ValueError("Context manifest source hash changed")
    for row in files:
        source = (directory / "fixed-source" / row["path"]).resolve(strict=True)
        if (not source.is_relative_to((directory / "fixed-source").resolve()) or digest(source.read_bytes()) != row["sha256"]
                or source.stat().st_size != row["bytes"]):
            raise ValueError("Fixed source differs from manifest")
    declarations = clean_targets(targets or [])
    if explicit:
        from .fixture_policy import select, validate_recipe
        if (manifest['project'], manifest['bug_id']) not in explicit_scope(policy):
            raise ValueError('Explicit fixture proposal is reviewed for two bugs only' if policy == POLICY_V4 else 'Explicit fixture proposal is restricted to its five reviewed development bugs')
        if select(declarations, policy['fixture_policy'])[1] or not declarations:
            raise ValueError('Shared explicit targets require reviewed capabilities')
        validate_recipe(fixture_recipe, policy=policy['fixture_policy'])
    elif fixture_recipe is not None:
        raise ValueError('Explicit recipes require a new preparation contract')
    sources = {row["path"]: row["sha256"] for row in files if row["path"].endswith(".java")}
    fixtures = clean_fixture_classes(fixture_classes or [])
    if v3 and (not isinstance(modified_sources, dict) or not modified_sources
               or fixture_classes is None or any(not re.fullmatch(r"[A-Za-z_$][\w$]*(?:\.[A-Za-z_$][\w$]*)*", c) for c in fixtures)):
        raise ValueError("V3 requires modified-source mapping and declaration-only fixture inventory")
    additional = {p: h for p, h in sources.items() if p not in (modified_sources or sources)}
    if v3:
        context = render_context(directory, files)
        (directory / "context.md").write_bytes(context.encode("utf-8"))
    else:
        context = (directory / "context.md").read_text(encoding="utf-8")
    targets_document = {"schema_version": 1, "target_selection": policy["target_selection"],
        "fixture_policy": policy["fixture_policy"], "targets": declarations,
        "status": "shared_declarations_discovered" if declarations else "pending_discovery",
        "semantic_validity": "pending_review"}
    if v3:
        targets_document["fixture_classes"] = fixtures
    targets_bytes = encoded(targets_document)
    instruction = (
        "Generate a deterministic JUnit 4 Java suite using only the supplied fixed source, build context, "
        "shared target declarations and fixture policy. Return complete Java code fences with explicit "
        "package declarations, public test classes and imports. Use at most 30 @Test methods. "
        "Use meaningful assertions derived from fixed API behavior; avoid non-null-only or empty tests, "
        "randomness, time dependence and external services. Do not modify or shadow production code. "
        "No assertion repairs or feedback loop. Suites exceeding 30 methods are rejected entirely.\n\n"
        f"Project: {manifest['project']}; fixed revision: {manifest['revision']}.\n"
        "Modified target classes:\n" + "\n".join(sorted(set(classes))) + "\n\n"
        "Fixture policy: common production types in fixed/buggy; simplest supported constructor selected "
        "by the shared probe. Use only the listed shared signatures and common fixture types.\n\n"
        "Shared target declarations:\n```json\n" + json.dumps(declarations, sort_keys=True, indent=2) + "\n```\n\n")
    if v3:
        instruction += ("Common compiled production fixture classes (eligibility only, not oracle approval):\n```json\n"
            + json.dumps(fixtures, indent=2) + "\n```\n\n"
            "Reach the target with meaningful domain arguments. Do not substitute constructor exceptions, "
            "null-only inputs or empty collections for behavior assertions. No execution feedback or repair loop.\n\n")
    support = ''
    if explicit:
        instruction += ('Explicit fixture policy: ' + policy['fixture_policy']
            + '. Use the reviewed capability recipes below instead of legacy recursive/null construction.\n')
        support = ('\n\nExplicit fixture recipe definitions (generation support, separate from production source):\n'
            'Use the same construction/projection knowledge across all four approaches. '
            'Setup failures are not target observations. Use valid receiver/dependency graphs and node kinds.\n```json\n'
            + encoded(fixture_recipe).decode('utf-8') + '```\n')
    prompt = (instruction + context + support).encode("utf-8")
    metadata = {"prepare_contract": policy["contract"], "source_sha256": source_hash,
        "context_source_hash": source_hash,
        "fixed_source_sha256": modified_sources if v3 else sources,
        "context_policy_id": policy["context_policy_id"], "context_selection": policy["context_policy_id"],
        "prompt_sha256": digest(prompt), "prompt_policy_id": policy["prompt_policy_id"],
        "prompt_utf8_bytes": len(prompt), "targets_sha256": digest(targets_bytes),
        "target_count": len(declarations), "target_selection": policy["target_selection"],
        "fixture_policy": policy["fixture_policy"], "policy_sha256": digest(encoded(policy)),
        "adapter_eligibility_verified": bool(declarations), "semantic_validity": "pending_review",
        "approval_state": "proposal_pending_three_owner_review", "previous_hashes": previous_hashes or {}}
    if v3:
        metadata.update(target_classes=sorted(set(classes)), additional_receiver_source_sha256=additional,
                        fixture_class_count=len(fixtures), fixture_classes_sha256=digest(encoded(fixtures)))
    if explicit:
        metadata.update(fixture_policy_id=policy['fixture_policy'], fixture_recipes_sha256=digest(encoded(fixture_recipe)))
    if v3:
        validate(manifest, metadata, prompt, targets_bytes, encoded(policy), fixture_recipe=fixture_recipe)
    for name, data in {"prompt.md": prompt, "targets.json": targets_bytes,
                       "prepare-policy.json": encoded(policy), "prepare-metadata.json": encoded(metadata)}.items():
        with (directory / name).open("xb") as stream:
            stream.write(data)
    if explicit:
        with (directory / 'fixture-recipes.json').open('xb') as stream:
            stream.write(encoded(fixture_recipe))
    return metadata


def validate(manifest, metadata, prompt, targets, policy, *, require_eligible=False, fixture_recipe=None):
    if manifest.get("contains_execution_logs") is not False:
        raise ValueError("Preparation cannot include execution feedback")
    selected = policy_for(metadata.get("prepare_contract"))
    fixed, additional = java_mapping(manifest, metadata)
    files = manifest["source_files"]
    source_hash = digest(json.dumps(files, sort_keys=True, separators=(",", ":")).encode())
    expected = {"source_sha256": source_hash, "context_source_hash": source_hash,
                "prompt_sha256": digest(prompt), "targets_sha256": digest(targets), "policy_sha256": digest(policy),
                "fixed_source_sha256": fixed}
    if any(metadata.get(k) != v for k, v in expected.items()) or manifest.get("source_hash") != source_hash:
        raise ValueError("Preparation hashes or fixed-source mapping differ")
    if (manifest.get("selection_policy_id") != selected["context_policy_id"]
            or metadata.get("context_policy_id") != selected["context_policy_id"] or metadata.get("context_selection") != selected["context_policy_id"]
            or metadata.get("prompt_policy_id") != selected["prompt_policy_id"] or json.loads(policy) != selected):
        raise ValueError("Preparation policy differs")
    document = json.loads(targets)
    if (document.get("target_selection") != selected["target_selection"]
            or document.get("fixture_policy") != selected["fixture_policy"]
            or metadata.get("target_selection") != selected["target_selection"]
            or metadata.get("fixture_policy") != selected["fixture_policy"]):
        raise ValueError("Target/fixture policy differs")
    declarations = clean_targets(document["targets"])
    if shared_context(selected['contract']):
        fixtures = document.get("fixture_classes")
        if (not isinstance(fixtures, list) or fixtures != sorted(set(fixtures))
                or any(not isinstance(c, str) or not re.fullmatch(r"[A-Za-z_$][\w$]*(?:\.[A-Za-z_$][\w$]*)*", c) for c in fixtures)
                or metadata.get("fixture_classes_sha256") != digest(encoded(fixtures))
                or metadata.get("fixture_class_count") != len(fixtures)):
            raise ValueError("Fixture inventory hash/count differs")
        classes = metadata.get("target_classes")
        if not isinstance(classes, list) or not classes:
            raise ValueError("Modified target classes are required")
        receivers = {t["class"].split("$", 1)[0].replace(".", "/") + ".java" for t in declarations if t["class"] not in classes}
        if any(not any(p.endswith("/" + r) for r in receivers) for p in additional):
            raise ValueError("Additional source is not a shared concrete receiver")
    if metadata.get("target_count") != len(declarations) or metadata.get("adapter_eligibility_verified") is not bool(declarations):
        raise ValueError("Eligibility state differs from declarations")
    if require_eligible and not declarations:
        raise ValueError("Shared declarations are required before generation")
    if explicit_context(selected['contract']):
        from .fixture_policy import select, validate_recipe
        if (manifest['project'], manifest['bug_id']) not in explicit_scope(selected):
            raise ValueError('Explicit fixture proposal is reviewed for two bugs only' if selected == POLICY_V4 else 'Explicit fixture proposal is restricted to its five reviewed development bugs')
        validate_recipe(fixture_recipe, policy=selected['fixture_policy'])
        if (metadata.get('fixture_policy_id') != selected['fixture_policy']
                or metadata.get('fixture_recipes_sha256') != digest(encoded(fixture_recipe))
                or encoded(fixture_recipe) not in prompt or select(declarations, selected['fixture_policy'])[1]):
            raise ValueError('Explicit fixture recipe/prompt/capability binding differs')
    elif metadata.get('fixture_policy_id') or fixture_recipe is not None:
        raise ValueError('Explicit recipes cannot reuse historical preparation')
    return True
