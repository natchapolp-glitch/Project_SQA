#!/usr/bin/env python3
"""KKU-only buggy-context pilot with one repair and immutable request stages."""
from __future__ import annotations

import argparse
from dataclasses import asdict
import json
from pathlib import Path
import re
import shutil
import sys
import time
import xml.etree.ElementTree as ET

import solo_batch as batch
from evaluate import EvaluationConfig, evaluate_run
from api854.common import cpu_slot
from api854.kku_client import KKUClient, KKUError, Model, load_account, normalize_usage, normalize_quota
from api854.pack_suite import pack_suite
from api854.suite_resolver import source_layout, java_tokens

TEMPLATES = {
    "P01": """You are planning deterministic Java regression tests. Analyze only the supplied bug metadata and buggy API signatures. Use documented API contracts and Java semantics as oracles, not merely the buggy implementation. In at most 10 short lines list useful behaviors, boundaries and valid receiver setup. Do not write tests yet. No external search, fixed source, patch, existing dataset tests, or hidden evaluation results are available.\n\n{context}\n""",
    "P02": """Generate complete Java 7-compatible JUnit 4 test source files for the APIs below (Java 11 runtime; existing project build). Return each file in a fenced java block with package declaration/imports and one public test class ending in Test. Prefer 4 to 6 concise high-value @Test methods (at most 12 in total), each independent with meaningful assertions or expected exceptions. Use documented contracts, not copying buggy behavior. Construct valid receivers; package-local targets can be tested from their own package. Use only listed production APIs and JDK/JUnit 4, no new dependencies. No randomness, sleeps, network, processes, external files, assumptions, ignored tests, production shadow classes or build changes. Keep source concise and omit explanatory comments. Return complete source only, not a diff or partial methods.\n\n{context}\n\nPlanning response:\n{analysis}\n""",
    "P03": """This is the single allowed repair of the generated Java suite. Correct compile/fixed-validation issues using only the supplied buggy context and fixed-validation excerpt. Return COMPLETE replacement Java files in java fences, at most 12 @Test methods, Java 7-compatible JUnit 4 with meaningful independent assertions. Preserve intended documented contracts; do not weaken or delete assertions solely to make tests pass. No production/build changes, added dependencies, assumptions, ignored tests or external access. No buggy evaluation feedback is supplied.\n\n{context}\n\nOriginal generated source:\n{source}\n\nCompile/fixed-validation excerpt:\n{feedback}\n""",
    "P04": """Extend a valid base suite using ONLY new test classes; do not repeat, modify, replace or redefine any base file. Return new COMPLETE Java test files in java fences with at most 4 additional @Test methods total, or exactly NO_CHANGE. Use Java 7-compatible JUnit 4 and meaningful assertions based on documented contracts. Fixed coverage counters/uncovered line numbers are hints; line numbers may differ in buggy source. Use only the supplied buggy context and APIs. No added dependencies, production/build changes, random/time/network/process/file access, assumptions or ignored tests.\n\n{context}\n\nUnchanged base files:\n{source}\n\nFixed coverage:\n{coverage}\n""",
}
METHODS = {"kku-claude": ("AI1_KKU_Claude", "claude-sonnet-5", "Claude"),
           "kku-gemini": ("AI2_KKU_Gemini", "gemini-3.5-flash-lite", "Gemini")}


class OutcomeError(Exception):
    def __init__(self, state, message):
        super().__init__(message)
        self.state = state


def analysis_context_for(info):
    return (f"Case: {info['case']}\nJava 11, JUnit 4\nModified classes: "
            + ", ".join(info["classes_modified"]) + "\nBUGGY API signatures:\n" + info["buggy_signatures"])


def source_pins():
    names = ["scripts/study/solo_ai_nightly.py", "scripts/study/solo_batch.py", "scripts/study/evaluate.py",
             "scripts/study/api854/kku_client.py", "scripts/study/api854/pack_suite.py",
             "scripts/study/api854/suite_resolver.py", "scripts/study/api854/common.py"]
    return {n: batch.sha(batch.ROOT / n) for n in names}


def init(output, offline, preflight):
    config = batch.load_config(offline)
    status = batch.read(preflight / "summary.json")
    ready = [r["alias"] for r in status["accounts"] if r["status"] == "MODELS_AVAILABLE"]
    if not ready:
        raise ValueError("No verified KKU model mapping")
    output.mkdir(parents=True, exist_ok=False)
    protocol = {
        "schema": "solo-ai-nightly.v2", "condition": "buggy-only-signature-analysis-nightly",
        "offline_path": str(offline), "offline_protocol_sha256": batch.sha(offline / "Experiment/protocol/offline.json"),
        "templates": TEMPLATES, "models": {m: name for m, (_, name, _) in METHODS.items()},
        "settings_requested": {"temperature": 0, "max_tokens": 4096, "stream": False},
        "claude_endpoint": "/messages", "claude_thinking_requested": {"type": "disabled"},
        "gemini_endpoint": "/chat/completions", "effective_settings_verified": False,
        "observed_settings_policy": "Request acceptance does not prove effective settings; preserve returned fields",
        "limits_verified": False, "quota_buckets_verified_distinct": False, "reset_time_verified": False,
        "post_analysis_admission": "prompt UTF-8 bytes + 4096 output + 8192 buffer; operator guard, not measured framing",
        "repair_cap": 1, "p02_test_cap": 12, "extension_test_cap": 4, "total_test_cap": 16,
        "analysis_context_cap_characters": 4000,
        "p01_context": "metadata and buggy signatures only; no source",
        "p02_style": "prefer 4-6 concise methods; hard maximum 12",
        "job_account_policy": "manifest assigns one verified alias per job before dispatch; no within-job key rotation",
        "predecessor_condition": "solo-ai-pilot.v1; preserve failures separately",
        "extension_failure_policy": "retain valid unchanged base and rejected extension evidence",
        "repair_feedback": "compile/fixed stages only, bounded 6000 characters; never buggy results",
        "quota_policy": "pause unrequested stages; no account switch or automatic HTTP retry within pilot; sealed paused outcomes require explicit reconciliation/new continuation condition",
        "known_accounts": ready, "generation_repeats": 1, "source_sha256": source_pins(),
        "read_only_preflight_sha256": batch.sha(preflight / "summary.json"),
        "old_results_pooled": False, "old_gate_or_queue_mutated": False,
    }
    batch.write(output / "Experiment/protocol/ai.json", protocol)
    for method, (folder, _, _) in METHODS.items():
        for stage, template in TEMPLATES.items():
            path = output / folder / "Prompt" / f"{stage}.txt"
            path.parent.mkdir(parents=True, exist_ok=True)
            path.write_text(template, encoding="utf-8")
    for name in protocol["source_sha256"]:
        path = output / "Experiment/automation/runtime" / name
        path.parent.mkdir(parents=True, exist_ok=True)
        shutil.copyfile(batch.ROOT / name, path)
    return protocol


def load_protocol(output):
    protocol = batch.read(output / "Experiment/protocol/ai.json")
    if protocol["source_sha256"] != source_pins():
        raise ValueError("AI implementation changed; preserve this condition")
    offline = Path(protocol["offline_path"])
    if batch.sha(offline / "Experiment/protocol/offline.json") != protocol["offline_protocol_sha256"]:
        raise ValueError("Offline condition binding changed")
    for folder, _, _ in METHODS.values():
        for stage, template in protocol["templates"].items():
            if (output / folder / "Prompt" / f"{stage}.txt").read_text(encoding="utf-8") != template:
                raise ValueError("Template snapshot changed")
    return protocol, offline, batch.load_config(offline)


def decode_messages(payload):
    if payload.get("model") not in {"claude-sonnet-5", "anthropic/claude-sonnet-5"}:
        raise OutcomeError("INFRA_ERROR", "KKU returned a model outside the exact Sonnet 5 mapping")
    blocks = payload.get("content")
    if not isinstance(blocks, list) or any(not isinstance(b, dict) or b.get("type") != "text" or not isinstance(b.get("text"), str) for b in blocks):
        raise OutcomeError("OUTPUT_INCOMPLETE", "Response contains unsupported assistant blocks")
    text = "".join(b["text"] for b in blocks)
    stop = payload.get("stop_reason")
    outcome = "response_received" if stop == "end_turn" and text.strip() else "truncated" if stop == "max_tokens" else "generation_failed"
    return text, {"outcome": outcome, "actual_model": payload.get("model"), "actual_provider": payload.get("provider"),
                  "finish_reason": stop, "usage": normalize_usage(payload), "model_quota": normalize_quota(payload)}


def request_once(client, method, stage, prompt, output, case, protocol, prior_quota=None):
    folder = output / METHODS[method][0] / "Result" / case / "run-final" / stage
    model = METHODS[method][1]
    prompt_hash = __import__("hashlib").sha256(prompt.encode()).hexdigest()
    if folder.exists():
        if not (folder / "receipt.json").exists():
            raise OutcomeError("INFRA_ERROR", "Uncertain prior HTTP outcome preserved; do not resend")
        batch.verify(folder)
        intent = batch.read(folder / "request-intent.json")
        if (intent["prompt_sha256"] != prompt_hash or intent["model"] != model or intent["account_alias"] != client.account.alias
                or intent["protocol_sha256"] != batch.sha(output / "Experiment/protocol/ai.json")):
            raise OutcomeError("INFRA_ERROR", "Request binding changed on resume")
        meta = batch.read(folder / "metadata.json")
        if "error_state" in meta:
            raise OutcomeError(meta["error_state"], "Retained provider failure; no retry")
        return (folder / "raw-response.txt").read_text(encoding="utf-8"), meta
    guard = len(prompt.encode("utf-8")) + 4096 + 8192
    if prior_quota is not None and prior_quota < guard:
        raise OutcomeError("QUOTA_PAUSED", f"Known remaining quota {prior_quota} below operator guard {guard}; stage not requested")
    folder.mkdir(parents=True)
    settings = protocol["settings_requested"]
    request = {"model": model, "messages": [{"role": "user", "content": prompt}], **settings}
    if method == "kku-claude":
        request["thinking"] = protocol["claude_thinking_requested"]
    endpoint = protocol["claude_endpoint"] if method == "kku-claude" else protocol["gemini_endpoint"]
    batch.write(folder / "request-intent.json", {"account_alias": client.account.alias, "model": model,
                "stage": stage, "prompt_sha256": prompt_hash, "endpoint": endpoint, "settings": settings,
                "thinking_requested": request.get("thinking"), "operator_guard": guard,
                "quota_before_from_prior_response": prior_quota,
                "protocol_sha256": batch.sha(output / "Experiment/protocol/ai.json"),
                "declared_before_request": True, "automatic_retry": False})
    prompt_path = output / METHODS[method][0] / "Prompt" / case / "run-final" / f"{stage}.txt"
    prompt_path.parent.mkdir(parents=True, exist_ok=True)
    prompt_path.write_text(prompt, encoding="utf-8")
    started = time.monotonic()
    try:
        if method == "kku-claude":
            payload, evidence = client._request("POST", endpoint, request)
            # Retain successful response before interpreting its model/content.
            batch.write(folder / "response.json", evidence)
            text, meta = decode_messages(payload)
        else:
            completion = client.complete(Model(model, model, METHODS[method][2]), prompt, max_tokens=4096, temperature=0)
            batch.write(folder / "response.json", completion.evidence)
            text, meta = completion.content, {**completion.metadata, "outcome": completion.outcome}
        (folder / "raw-response.txt").write_text(text, encoding="utf-8")
        meta.update(account_alias=client.account.alias, requested_model=model, request_seconds=time.monotonic() - started)
        batch.write(folder / "metadata.json", meta)
        batch.seal(folder)
        print(f"{case} {method} {stage}: {meta['outcome']}; usage={meta['usage']}", flush=True)
        return text, meta
    except (KKUError, OutcomeError) as error:
        state = "QUOTA_PAUSED" if isinstance(error, KKUError) and error.kind == "daily_limit" else getattr(error, "state", "INFRA_ERROR")
        detail = error.record() if isinstance(error, KKUError) else {"message": str(error), "state": state}
        batch.write(folder / "metadata.json", {"error_state": state, "error": detail,
                                              "request_seconds": time.monotonic() - started})
        batch.seal(folder)
        raise OutcomeError(state, "Provider error retained; no automatic retry") from None


def extract_sources(text, cap, modified_classes):
    blocks = re.findall(r"```(?:java)?\s*\n(.*?)```", text, re.DOTALL)
    if not blocks and "```" not in text:
        blocks = [text]
    if not blocks:
        raise ValueError("No complete Java blocks")
    sources, count = {}, 0
    for block in blocks:
        data = block.encode("utf-8")
        name, tests = source_layout(data)
        tokens = java_tokens(block)
        if name[:-5].replace("/", ".") in modified_classes or any(x in tokens for x in ("Ignore", "Assume")):
            raise ValueError("Production shadowing/ignored/assumed tests rejected")
        if name.casefold() in {n.casefold() for n in sources} or not name.endswith("Test.java"):
            raise ValueError("Duplicate source or non-test public class")
        sources[name] = data
        count += tests
    if not 1 <= count <= cap:
        raise ValueError("JUnit method count missing or over cap; no trimming")
    return sources, count


def package(sources, count, folder, cap):
    if folder.exists():
        batch.verify(folder)
        manifest = batch.read(folder / "package/suite-manifest.json")
        if manifest["source_sha256"] != {n: __import__("hashlib").sha256(b).hexdigest() for n, b in sources.items()}:
            raise OutcomeError("INFRA_ERROR", "Source package binding changed")
        return folder / "package/suite.tar.bz2"
    src = folder / "sources"
    src.mkdir(parents=True)
    for name, data in sources.items():
        path = src / name
        path.parent.mkdir(parents=True, exist_ok=True)
        path.write_bytes(data)
    pack_suite(src, folder / "package", count, cap)
    batch.seal(folder)
    return folder / "package/suite.tar.bz2"


def measure(suite, count, meta, method, folder, config):
    if folder.exists():
        if not (folder / "receipt.json").exists():
            raise OutcomeError("INFRA_ERROR", "Interrupted evaluation retained")
        batch.verify(folder)
        return batch.read(folder / "record.json")
    with cpu_slot(config["worktrees_root"]):
        result = evaluate_run(EvaluationConfig(meta["project"], meta["bug_id"], method, 101, 12, suite,
            Path(meta["trees"]["b"]), Path(meta["trees"]["f"]), folder, config["d4j"],
            Path(meta["classes_file"]), test_count=count, timeout_seconds=config["timeout_seconds"]))
    if result["status"] == "complete":
        try:
            result["observed_test_starts"] = observed_starts(folder, count)
        except ValueError as error:
            result.update(status="failed", error=str(error), fault_detected=None)
        batch.write(folder / "record.json", result)
    batch.seal(folder)
    return result


def observed_starts(folder, expected):
    """D4J Formatter.startTest events; not inferred from source annotations."""
    counts = {}
    for stage in ("fixed-1", "fixed-2", "buggy", "coverage"):
        path = folder / stage / "all_tests"
        if not path.is_file():
            raise ValueError(f"Missing started-test evidence: {stage}")
        names = [n.strip() for n in path.read_text(encoding="utf-8").splitlines() if n.strip()]
        if len(names) != expected or len(set(names)) != expected:
            raise ValueError(f"Started-test count/duplicates differ from declared suite: {stage}")
        counts[stage] = len(names)
    return counts


def usage_totals(metadatas, attempts):
    result = {"request_attempts": attempts, "completed_provider_responses": sum("usage" in m for m in metadatas)}
    for field in ("prompt_tokens", "completion_tokens"):
        values = [m.get("usage", {}).get(field) for m in metadatas]
        result[field] = sum(values) if attempts and len(values) == attempts and all(isinstance(v, int) for v in values) else None
    values = [m.get("request_seconds") for m in metadatas]
    result["generation_seconds"] = sum(values) if len(values) == attempts and attempts and all(isinstance(v, (int, float)) for v in values) else None
    return result


def fixed_feedback(record, measurement):
    chunks = []
    for name in ("fixed-1", "fixed-2"):
        if name not in record.get("stages", {}):
            continue
        for file in ("command.log", "failing_tests"):
            path = measurement / name / file
            if path.exists():
                chunks.append(f"{name}/{file}:\n" + path.read_text(encoding="utf-8", errors="replace")[-2400:])
    return "\n".join(chunks)[-6000:]


def source_text(sources):
    return "\n".join(f"FILE {n}\n```java\n{b.decode('utf-8')}\n```" for n, b in sorted(sources.items()))


def coverage_feedback(record, folder):
    result = {k: record.get(k) for k in ("line_covered", "line_total", "branch_covered", "branch_total")}
    path = folder / "coverage/coverage.xml"
    if path.exists():
        result["uncovered_fixed_lines"] = sorted({int(line.get("number")) for line in ET.parse(path).iter("line")
            if line.get("hits") == "0" and line.get("number", "").isdigit()})[:40]
    return json.dumps(result)


def run_case(output, offline, case, method, alias, secrets, protocol, config):
    evaluation = output / "Experiment/evaluations" / case / method / "run-final"
    if (evaluation / "outcome.json").exists():
        batch.verify(evaluation)
        print(f"{case} {method}: retained {batch.read(evaluation / 'outcome.json')['state']}", flush=True)
        return
    evaluation.mkdir(parents=True, exist_ok=True)
    started = time.monotonic()
    project, bug = case.rsplit("-", 1)
    if project not in config["inventory"] or int(bug) not in config["inventory"][project]:
        raise ValueError("Case outside installed active inventory")
    outcome = {"case": case, "project": project, "bug_id": int(bug), "method": method,
               "attempted": True, "provider_requested": False, "evaluated": False,
               "protocol_hash": batch.sha(output / "Experiment/protocol/ai.json"), "account_alias": alias}
    def ask(stage, **values):
        nonlocal remaining
        prompt = protocol["templates"][stage].format(context=analysis_context if stage == "P01" else context, **values)
        text, response = request_once(client, method, stage, prompt, output, case, protocol, remaining)
        remaining = response["model_quota"].get("daily_remaining_tokens")
        if response["outcome"] != "response_received":
            raise OutcomeError("OUTPUT_INCOMPLETE", f"{stage} response: {response['outcome']}")
        return text
    try:
        if alias not in protocol["known_accounts"]:
            raise ValueError("Account not in read-only verified registry")
        meta = batch.prepare_case(offline, case, config)
        context = (offline / "Experiment/contexts" / case / "ai-context.txt").read_text(encoding="utf-8")
        destination = output / "Experiment/contexts" / case
        if not destination.exists():
            destination.mkdir(parents=True)
            for n in ("ai-context.txt", "ai-context.json"):
                shutil.copyfile(offline / "Experiment/contexts" / case / n, destination / n)
            batch.seal(destination)
        batch.verify(destination)
        if batch.sha(destination / "ai-context.txt") != meta["ai_context_sha256"]:
            raise OutcomeError("INFRA_ERROR", "Context differs from bound buggy source")
        outcome["context_sha256"] = meta["ai_context_sha256"]
        info = batch.read(destination / "ai-context.json")
        analysis_context = analysis_context_for(info)
        classes = meta["properties"]["b"]["classes.modified"].splitlines()
        client = KKUClient(load_account(alias, secrets), timeout=180)
        remaining = None
        analysis = ask("P01")
        raw = ask("P02", analysis=analysis[:protocol["analysis_context_cap_characters"]])
        repair_reason, result, sources, count = None, None, None, None
        try:
            sources, count = extract_sources(raw, 12, classes)
            suite = package(sources, count, evaluation / "base", 12)
            result = measure(suite, count, meta, method, evaluation / "base-evaluation", config)
            outcome["evaluated"] = "fixed-1" in result["stages"]
            if result["status"] == "invalid" or result.get("compile_status") == "failed":
                repair_reason = fixed_feedback(result, evaluation / "base-evaluation")
        except ValueError as error:
            repair_reason = f"Source extraction/packaging error: {error}"
        selected = "base"
        if repair_reason:
            raw = ask("P03", source=raw, feedback=repair_reason)
            selected = "repaired"
            try:
                sources, count = extract_sources(raw, 12, classes)
                suite = package(sources, count, evaluation / "repaired", 12)
                result = measure(suite, count, meta, method, evaluation / "repaired-evaluation", config)
                outcome["evaluated"] = "fixed-1" in result["stages"]
            except ValueError as error:
                raise OutcomeError("INVALID_AFTER_REPAIR", str(error)) from None
        if result is None or result["status"] != "complete":
            if result:
                outcome.update(batch.normalize(result))
                outcome["selected_measurement"] = (evaluation / f"{selected}-evaluation").relative_to(output).as_posix()
            if result and (result["status"] == "invalid" or result.get("compile_status") == "failed"):
                raise OutcomeError("INVALID_AFTER_REPAIR", "Suite failed fixed validation after single repair")
            raise OutcomeError("INFRA_ERROR", "Evaluation failed/partial; no repair using buggy feedback")
        selected_measurement = evaluation / f"{selected}-evaluation"
        outcome.update(batch.normalize(result), base_usable=True, base_suite=selected)
        # Fixed coverage alone determines extension; buggy failures never enter prompts.
        if result["line_covered"] < result["line_total"]:
            try:
                extension = ask("P04", source=source_text(sources), coverage=coverage_feedback(result, selected_measurement))
                if extension.strip() != "NO_CHANGE":
                    additions, added = extract_sources(extension, 4, classes)
                    if {n.casefold() for n in additions} & {n.casefold() for n in sources}:
                        raise ValueError("Extension attempts to replace an unchanged base source")
                    combined = {**sources, **additions}
                    extended_suite = package(combined, count + added, evaluation / "extended", 16)
                    extended = measure(extended_suite, count + added, meta, method, evaluation / "extended-evaluation", config)
                    if extended["status"] == "complete":
                        result, suite, sources, count = extended, extended_suite, combined, count + added
                        selected_measurement = evaluation / "extended-evaluation"
                        selected = "extended"
                    else:
                        batch.write(evaluation / "extension-rejected.json", {"status": extended["status"], "base_retained": True})
            except OutcomeError as error:
                outcome.update(base_usable=True, base_suite=selected, extension_state=error.state)
                raise
            except ValueError as error:
                batch.write(evaluation / "extension-rejected.json", {"reason": str(error), "base_retained": True})
        outcome.update(batch.normalize(result))
        outcome.update(state="DONE", selected_suite=selected, generated_test_methods=count,
                       selected_measurement=selected_measurement.relative_to(output).as_posix(),
                       suite_sha256=batch.sha(suite), model_id=METHODS[method][1])
        outcome.update(executed_tests=result["observed_test_starts"]["fixed-1"],
                       observed_test_starts=result["observed_test_starts"], skipped_tests=None,
                       test_count_source="Defects4J Formatter.startTest; skipped counter unavailable")
        test_dest = output / METHODS[method][0] / "TestCode" / case / "run-final" / "final"
        if not test_dest.exists():
            test_dest.mkdir(parents=True)
            for n, data in sources.items():
                path = test_dest / n
                path.parent.mkdir(parents=True, exist_ok=True)
                path.write_bytes(data)
    except OutcomeError as error:
        outcome.update(state=error.state, error=str(error))
    except (ValueError, OSError, batch.StageError) as error:
        outcome.update(state="INFRA_ERROR", error=f"{type(error).__name__}: {error}")
    requests = output / METHODS[method][0] / "Result" / case / "run-final"
    metadatas = [batch.read(p) for p in requests.glob("*/metadata.json")]
    attempts = len(list(requests.glob("*/request-intent.json")))
    outcome.update(provider_requested=bool(attempts), elapsed_seconds=time.monotonic() - started,
                   **usage_totals(metadatas, attempts))
    batch.write(evaluation / "outcome.json", outcome)
    batch.seal(evaluation)
    print(f"{case} {method}: {outcome['state']}; fault={outcome.get('fault_detected')}", flush=True)
    return outcome["state"]


def main():
    cli = argparse.ArgumentParser(description=__doc__)
    cli.add_argument("action", choices=("init", "run"))
    cli.add_argument("--output", type=Path, required=True)
    cli.add_argument("--offline", type=Path)
    cli.add_argument("--preflight", type=Path)
    cli.add_argument("--secrets", type=Path)
    cli.add_argument("--account", default="a01")
    cli.add_argument("--cases", nargs="+", default=["Csv-1", "Lang-1", "Math-1"])
    cli.add_argument("--methods", nargs="+", choices=tuple(METHODS), default=list(METHODS))
    args = cli.parse_args()
    if sys.platform != "linux":
        cli.error("Use WSL/Linux with the original Java 11 worktrees")
    output = args.output.resolve()
    if args.action == "init":
        if args.offline is None or args.preflight is None:
            cli.error("init requires offline and preflight paths")
        init(output, args.offline.resolve(), args.preflight.resolve())
    else:
        if args.secrets is None:
            cli.error("run requires a private secrets file")
        protocol, offline, config = load_protocol(output)
        for case in args.cases:
            for method in args.methods:
                state = run_case(output, offline, case, method, args.account, args.secrets, protocol, config)
                if state == "QUOTA_PAUSED":
                    return


if __name__ == "__main__":
    main()
