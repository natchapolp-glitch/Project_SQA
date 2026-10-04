"""Read-only model discovery. Never calls chat/completions or creates a job."""
from __future__ import annotations

import argparse
from dataclasses import asdict
import json
from pathlib import Path

from .kku_client import KKUClient, KKUError, load_account, utc_now
from .models import load_selection, resolve_selected


def discover(alias: str, secret_file: Path | None) -> dict:
    client = KKUClient(load_account(alias, secret_file), timeout=30)
    selection = load_selection()
    records = {"kind": "read_only_model_discovery", "account_alias": alias,
               "observed_at_utc": utc_now(), "generation_requests": 0,
               "quota_verified": False, "server_reset_timezone_verified": False,
               "requested_model_selection": selection}
    try:
        models, evidence = client.list_models()
        records.update(models=[asdict(m) for m in models], evidence=evidence)
        records["primary_candidates"] = {}
        for approach in ("kku-claude", "kku-gemini"):
            try:
                records["primary_candidates"][approach] = asdict(resolve_selected(models, approach, selection))
            except KKUError as error:
                records["primary_candidates"][approach] = {"unresolved": error.record()}
    except KKUError as error:
        records["error"] = error.record()
    return records


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--account", default="a01")
    parser.add_argument("--secrets", type=Path)
    parser.add_argument("--output", type=Path, required=True,
                        help="New ignored local discovery artifact; never an executable protocol")
    args = parser.parse_args()
    result = discover(args.account, args.secrets)
    args.output.parent.mkdir(parents=True, exist_ok=True)
    with args.output.open("x", encoding="utf-8") as stream:
        json.dump(result, stream, ensure_ascii=False, indent=2, allow_nan=False)
        stream.write("\n")
    # Only publish model names/IDs and sanitized error, never credentials or raw headers.
    print(json.dumps({"account_alias": args.account, "models": result.get("models", []),
                      "primary_candidates": result.get("primary_candidates"),
                      "error_kind": result.get("error", {}).get("kind"),
                      "generation_requests": 0}, ensure_ascii=True))
    unresolved = any("unresolved" in candidate for candidate in result.get("primary_candidates", {}).values())
    return 1 if "error" in result or unresolved else 0


if __name__ == "__main__":
    raise SystemExit(main())
