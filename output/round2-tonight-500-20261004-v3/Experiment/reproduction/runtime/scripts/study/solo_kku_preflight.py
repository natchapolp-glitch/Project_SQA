#!/usr/bin/env python3
"""Read-only KKU model availability; never generates text or logs credentials."""
import argparse
from concurrent.futures import ThreadPoolExecutor
from dataclasses import asdict
import json
from pathlib import Path

from api854.kku_client import KKUClient, KKUError, load_account, pin_model, sanitize

MODELS = {"kku-claude": ("sonnet", "claude-sonnet-5"),
          "kku-gemini": ("flash-lite", "gemini-3.5-flash-lite")}


def inspect_account(alias, secrets, output):
    folder = output / alias
    folder.mkdir()
    try:
        account = load_account(alias, secrets)
        client = KKUClient(account, timeout=30)
        models, evidence = client.list_models()
        selected = {method: asdict(pin_model(models, family, name))
                    for method, (family, name) in MODELS.items()}
        result = {"alias": alias, "status": "MODELS_AVAILABLE", "http_status": evidence["http_status"],
                  "selected_models": selected, "started_at_utc": evidence["started_at_utc"],
                  "ended_at_utc": evidence["ended_at_utc"], "raw_body_sha256": evidence["raw_body_sha256"],
                  "daily_quota": None, "limits": None, "requests_generated": 0}
        # Model-list bodies are sanitized before writing, never credential headers.
        payload = sanitize(evidence["body"], (account.api_key,))
        (folder / "models.json").write_text(json.dumps(payload, indent=2) + "\n", encoding="utf-8")
    except KKUError as error:
        result = {"alias": alias, "status": error.kind, "http_status": error.status,
                  "unknown": error.unknown, "requests_generated": 0}
    except (ValueError, OSError):
        result = {"alias": alias, "status": "LOCAL_CONFIGURATION_ERROR", "requests_generated": 0}
    (folder / "status.json").write_text(json.dumps(result, indent=2) + "\n", encoding="utf-8")
    return result


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--secrets", type=Path, required=True)
    parser.add_argument("--output", type=Path, required=True)
    args = parser.parse_args()
    args.output.mkdir(parents=True, exist_ok=False)
    data = json.loads(args.secrets.read_text(encoding="utf-8"))
    aliases = [row["alias"] for row in data["accounts"]]
    if len(aliases) != len(set(aliases)):
        raise ValueError("Duplicate aliases")
    with ThreadPoolExecutor(max_workers=3) as pool:
        results = list(pool.map(lambda alias: inspect_account(alias, args.secrets, args.output), aliases))
    summary = {"schema": "kku-read-only-preflight.v1", "origin": "https://gen.ai.kku.ac.th/api/v1",
               "generation_requests": 0, "accounts": results,
               "quota_note": "Model availability is not proof of quota, distinct buckets, or effective settings"}
    (args.output / "summary.json").write_text(json.dumps(summary, indent=2) + "\n", encoding="utf-8")
    for row in results:
        print(json.dumps({"alias": row["alias"], "status": row["status"], "http_status": row.get("http_status")}), flush=True)


if __name__ == "__main__":
    main()
