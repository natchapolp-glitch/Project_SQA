"""Prospective, Cli-only unordered option oracle derived from immutable v12.

Does not edit the root helper, historical packets, old generated assertions or live gates.
"""
from __future__ import annotations

import argparse
import copy
import difflib
import json
from pathlib import Path
import shutil

from aom_ready_csv import ROOT, DAY, INTAKE, BASE, PAIR, PREP, digest, load, save, seal, verify_manifest
from aom_ready_messages import RECEIVED

POLICY = "aom-cli-unordered-options-v1-development"
CONDITION = "api854-20261004-cli-unordered-options-v1-development"
OUTPUT = ROOT / DAY / "aom-cli-unordered-preparation-v2"
RUNTIME = OUTPUT / "runtime"

JAVA_METHOD = '''
        // Option membership has no ordering contract. Values inside each option do.
        String unorderedOptions(java.util.List<Object> options, int depth) throws ReflectiveOperationException {
            if (options.size() > 256) throw new FixtureFailure("Oracle options limit exceeded", null);
            java.util.List<String> items = new java.util.ArrayList<String>();
            for (Object option : options) {
                if (option == null || !option.getClass().getName().equals("org.apache.commons.cli.Option"))
                    throw new FixtureFailure("Unexpected non-Option in unordered option collection", null);
                items.add(projection(option, depth + 1));
            }
            java.util.Collections.sort(items);
            StringBuilder out = new StringBuilder("options-multiset[");
            for (String item : items) out.append(item).append(';');
            return out.append(']').toString();
        }

'''

JAVA_ARRAY = '''            if (cliUnordered && targetClass.equals("org.apache.commons.cli.CommandLine")
                    && result.getClass().isArray()
                    && result.getClass().getComponentType().getName().equals("org.apache.commons.cli.Option")) {
                int length = Array.getLength(result);
                if (length > 256) throw new FixtureFailure("Oracle options limit exceeded", null);
                java.util.List<Object> options = new java.util.ArrayList<Object>();
                for (int i = 0; i < length; i++) options.add(Array.get(result, i));
                return unorderedOptions(options, depth);
            }
            if (cliUnordered && targetClass.equals("org.apache.commons.cli.CommandLine")
                    && method.equals("iterator") && result instanceof java.util.Iterator) {
                java.util.List<Object> options = new java.util.ArrayList<Object>();
                java.util.Iterator<?> iterator = (java.util.Iterator<?>)result;
                while (iterator.hasNext()) {
                    if (options.size() >= 256) throw new FixtureFailure("Oracle options limit exceeded", null);
                    options.add(iterator.next());
                }
                return unorderedOptions(options, depth);
            }
'''


def replace_one(text, old, new):
    if text.count(old) != 1:
        raise ValueError("Pinned helper patch anchor differs; refuse adaptive patching")
    return text.replace(old, new, 1)


def helper_source(data):
    # Normalize only this new derived helper; immutable base bytes remain untouched.
    text = data.decode("utf-8").replace("\r\n", "\n")
    text = replace_one(text, '    private static final ThreadLocal<FixtureSession> FIXTURES',
        f'    public static final String CLI_UNORDERED_FIXTURES = "{POLICY}";\n'
        '    private static final ThreadLocal<FixtureSession> FIXTURES')
    text = replace_one(text, '        final boolean reviewed;', '        final boolean reviewed;\n        final boolean cliUnordered;')
    text = replace_one(text, '            this.reviewed = JOINT_FIXTURES.equals(policy)',
        '            this.cliUnordered = CLI_UNORDERED_FIXTURES.equals(policy);\n'
        '            this.reviewed = cliUnordered || JOINT_FIXTURES.equals(policy)')
    text = replace_one(text, '        String projection(Object result, int depth)', JAVA_METHOD + '        String projection(Object result, int depth)')
    text = replace_one(text, '            if (pilot && result.getClass().isArray()) {', JAVA_ARRAY + '            if (pilot && result.getClass().isArray()) {')
    text = replace_one(text, '&& !CHRONOLOGY_FIXTURES.equals(policy) && !GRAPHICS_FIXTURES.equals(policy))',
        '&& !CHRONOLOGY_FIXTURES.equals(policy) && !GRAPHICS_FIXTURES.equals(policy)\n'
        '                && !CLI_UNORDERED_FIXTURES.equals(policy))')
    text = replace_one(text, '        FIXTURES.set(new FixtureSession(className, methodName, policy));',
        '        if (CLI_UNORDERED_FIXTURES.equals(policy) && !className.equals("org.apache.commons.cli.CommandLine"))\n'
        '            throw new IllegalArgumentException("Cli-only unordered option condition");\n'
        '        FIXTURES.set(new FixtureSession(className, methodName, policy));')
    return text.encode("utf-8")


def prepare():
    verify_manifest(INTAKE)
    verify_manifest(RECEIVED)
    OUTPUT.mkdir(parents=True, exist_ok=False)
    baseline = INTAKE / "baseline"
    old_protocol = load(baseline / PAIR / "protocol.proposal.json")
    for name, value in old_protocol["source_sha256"].items():
        source = baseline / name
        if digest(source.read_bytes()) != value:
            raise ValueError("Immutable v12 baseline differs")
        destination = RUNTIME / name
        destination.parent.mkdir(parents=True, exist_ok=True)
        shutil.copyfile(source, destination)
    helper = RUNTIME / "algorithms/java/SqaProbe.java"
    original = helper.read_bytes()
    derived = helper_source(original)
    helper.write_bytes(derived)
    patch = "".join(difflib.unified_diff(original.decode().replace("\r\n", "\n").splitlines(keepends=True),
        derived.decode().splitlines(keepends=True), fromfile="v12/SqaProbe.java", tofile="cli-unordered/SqaProbe.java"))
    (OUTPUT / "helper-change.diff").write_text(patch, encoding="utf-8", newline="\n")
    # Update only the prospective generator's explanatory manifest field.
    generator = RUNTIME / "scripts/study/generate.py"
    text = generator.read_text(encoding="utf-8")
    anchor = "    if count:\n        source = suite_source(rows, fixture_policy).encode('utf-8')"
    text = replace_one(text, anchor, f"    if fixture_policy == '{POLICY}':\n"
        "        manifest['oracle_scope'] = 'Cli options are a sorted projected identity/value multiset; per-option values and positional arguments stay ordered. No old assertions repaired.'\n" + anchor)
    generator.write_text(text, encoding="utf-8", newline="\n")
    selector = RUNTIME / "scripts/study/api854/fixture_policy.py"
    text = selector.read_text(encoding="utf-8")
    text = replace_one(text, "POLICY_V12 = 'aom-beam-champ-graphics-fixtures-v12-development'",
        "POLICY_V12 = 'aom-beam-champ-graphics-fixtures-v12-development'\n" + f"POLICY_CLI_ORACLE = '{POLICY}'")
    text = text.replace("POLICY_V11, POLICY_V12}", "POLICY_V11, POLICY_V12, POLICY_CLI_ORACLE}")
    text = replace_one(text, "    if policy is None:\n        return targets, []", "    if policy is None:\n        return targets, []\n"
        "    if policy == POLICY_CLI_ORACLE:\n"
        "        selected, excluded = select(targets, POLICY_V12)\n"
        "        inside = [t for t in selected if t['class'] == 'org.apache.commons.cli.CommandLine']\n"
        "        outside = [{'target': t, 'reason': 'outside_cli_unordered_oracle_scope'} for t in selected if t not in inside]\n"
        "        return inside, excluded + outside")
    selector.write_text(text, encoding="utf-8", newline="\n")
    received = RECEIVED / "peer" / DAY / "champ-cli-development-generation-v2/received"
    prepare = OUTPUT / "prepared/Cli-1"
    prepare.mkdir(parents=True)
    for name in ("context-manifest.json", "targets.json"):
        shutil.copyfile(received / name, prepare / name)
    metadata = load(received / "prepare-metadata.json")
    recipe = load(received / "fixture-recipes.json")
    original_recipe_text = json.dumps(recipe, indent=2, ensure_ascii=False)
    domain = {"id": POLICY, "target_class": "org.apache.commons.cli.CommandLine",
        "unordered": ["getOptions() return membership", "iterator() option membership", "state.getOptions()"],
        "ordered": ["each Option.getValues() sequence", "positional arguments", "other arrays/lists/buffers"],
        "canonicalization": "sort complete existing projected option identity/value strings, retain duplicate multiplicity",
        "unchanged_input_domain": "v12 bounded fixtures: x(alpha/beta), extra(left/right), positional; same vectors/targets",
        "limits": "Option projection preserves existing short opt/value fields only, not full arbitrary Option metadata equivalence",
        "fixture_policy_id": POLICY, "base_fixture_policy_id": metadata["fixture_policy_id"]}
    save(OUTPUT / "oracle-contract.json", domain)
    recipe["oracle_contract"] = domain
    recipe["fixture_policy_id"] = POLICY
    recipe["sources"] = {name: (RUNTIME / name).read_text(encoding="utf-8") for name in recipe["sources"]}
    # Hash exactly the embedded source strings; they are the same new runtime bytes.
    recipe["source_sha256"] = {name: digest(text.encode("utf-8")) for name, text in recipe["sources"].items()}
    save(prepare / "fixture-recipes.json", recipe)
    policy = load(received / "prepare-policy.json")
    policy.update(condition=CONDITION, oracle_contract=domain, fixture_policy=POLICY,
                  primary=False, generation_ready=False, queue_mutations=0, live_requests=0)
    policy.update(contract="aom-cli-unordered-prepare-v1-development",
        context_policy_id="v12-fixed-cli-source-scoped-unordered-oracle-v1-development",
        prompt_policy_id="v12-cli16-unordered-options-junit4-v1-development",
        development_bugs=[["Cli", 1]],
        fixture_selection="v12 Cli16 only, identical argument/receiver inputs; prospective unordered-options oracle",
        oracle_policy="unordered option identity/value multiset only; per-option values/args/other results ordered")
    save(prepare / "prepare-policy.json", policy)
    instruction = ("# Prospective Cli unordered-options condition\n\n"
        "Condition: " + CONDITION + ". This is a new trial, not a repaired old suite.\n"
        "Same fixed-source context and 16 target declarations follow. No runtime feedback is supplied.\n"
        "CommandLine option membership from getOptions()/iterator() has no iteration-order oracle;\n"
        "compare projected option identities and their values as a multiset, preserving multiplicity.\n"
        "The value sequence inside each Option, positional arguments, and all other ordered results\n"
        "must retain order. Do not sort all arrays or drop values. Use legal inputs for the exact declarations.\n"
        "Generated test-method cap30; JUnit4; independent unchanged Java is retained even if invalid.\n\n"
        "# Original fixed-source/API/build context (immutable v12)\n\n")
    old_prompt = (received / "prompt.md").read_bytes().decode("utf-8")
    old_prompt = replace_one(old_prompt, original_recipe_text, json.dumps(recipe, indent=2, ensure_ascii=False))
    prompt = (instruction + old_prompt).encode("utf-8")
    (prepare / "prompt.md").write_bytes(prompt)
    metadata.update(condition=CONDITION, oracle_contract_id=POLICY,
        fixture_policy=POLICY, fixture_policy_id=POLICY, prepare_contract="aom-cli-unordered-prepare-v1-development",
        context_policy_id="v12-fixed-cli-source-scoped-unordered-oracle-v1-development",
        prompt_policy_id="v12-cli16-unordered-options-junit4-v1-development",
        prompt_sha256=digest(prompt), prompt_utf8_bytes=len(prompt),
        fixture_recipes_sha256=digest((prepare / "fixture-recipes.json").read_bytes()),
        policy_sha256=digest((prepare / "prepare-policy.json").read_bytes()),
        runtime_source_sha256={name: digest((RUNTIME / name).read_bytes()) for name in old_protocol["source_sha256"]},
        previous_condition_sha256={"metadata": digest((received / "prepare-metadata.json").read_bytes()),
            "prompt": digest((received / "prompt.md").read_bytes())})
    save(prepare / "prepare-metadata.json", metadata)
    protocol = copy.deepcopy(old_protocol)
    protocol.update(condition=CONDITION, state="scoped_development_proposal", approval_state="pending_peer_review",
        source_sha256=metadata["runtime_source_sha256"], fixture_policy_id=POLICY, enabled_stages=[],
        scoped_cohort=[{"project": "Cli", "bug_id": 1}], oracle_contract=domain,
        core_shared_v12_not_replaced=True, primary=False)
    protocol["generation"].update(prepare_contract=metadata["prepare_contract"], fixture_policy_id=POLICY,
        context_policy_id=metadata["context_policy_id"], prompt_policy_id=metadata["prompt_policy_id"],
        prompt_token_reserve=None,
        per_model_transport={"claude-sonnet-5": {"path": "/messages", "thinking": {"type": "disabled"}},
                             "gemini-3.5-flash-lite": {"path": "/chat/completions"}})
    save(OUTPUT / "protocol.proposal.json", protocol)
    save(OUTPUT / "runner.proposal.json", {"condition": CONDITION, "cpu_host": "aom-pc1", "cpu_slots": 1,
        "api_coordinator": "champ-pc1", "cpu_lock_root": "/home/team/sqa-round2/worktrees",
        "live_workers_started": False, "queue_mutations": 0, "status": "scoped_development_pending_peer_review"})
    targets = load(prepare / "targets.json")["targets"]
    if len(targets) != 16 or any(t["class"] != "org.apache.commons.cli.CommandLine" for t in targets):
        raise ValueError("Scoped target set changed")
    models = ("claude-sonnet-5", "gemini-3.5-flash-lite")
    save(OUTPUT / "prompt-reserve-worksheet.json", {"condition": CONDITION, "records": [
        {"project": "Cli", "bug_id": 1, "model": model, "prompt_sha256": digest(prompt),
         "prompt_utf8_bytes": len(prompt), "output_cap": 4096, "actual_input_tokens": None,
         "measured_framing": None, "final_reserve": None} for model in models],
        "bytes_are_not_tokens": True, "final_reserve": None})
    save(OUTPUT / "receipt.json", {"base_commit": BASE, "condition": CONDITION, "selected_declarations": 16,
        "runtime_source_sha256": metadata["runtime_source_sha256"], "base_runtime_source_sha256": old_protocol["source_sha256"],
        "changed_runtime_files": [name for name in old_protocol["source_sha256"]
            if old_protocol["source_sha256"][name] != metadata["runtime_source_sha256"][name]],
        "all_four_approaches_receive_same_prepared_packet": True, "old_outputs_untouched": True,
        "protocol_sha256": digest((OUTPUT / "protocol.proposal.json").read_bytes()),
        "prompt_sha256": digest(prompt), "helper_sha256": digest(derived),
        "primary_results": 0, "gate_a_approved": False, "api_requests": 0, "queue_mutations": 0})
    seal(OUTPUT)
    print(json.dumps({"condition": CONDITION, "targets": 16, "prompt_bytes": len(prompt), "helper": digest(derived)}))


if __name__ == "__main__":
    cli = argparse.ArgumentParser(description=__doc__)
    cli.add_argument("stage", choices=("prepare", "verify"))
    args = cli.parse_args()
    if args.stage == "prepare":
        prepare()
    else:
        print(verify_manifest(OUTPUT))
