"""Operator CLI for observed quota and user-notified account switches.

Never calls KKU or rotates credentials. A console event is not delivery;
acknowledge only AFTER a user-facing message was delivered, with its receipt.
"""
from __future__ import annotations

import argparse
import json
from pathlib import Path

from .generate_worker import write_json
from .kku_client import digest, sanitize
from .quota import QuotaBlocked, QuotaLedger


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--ledger", type=Path, default=Path(".local/api854/quota.sqlite"))
    commands = parser.add_subparsers(dest="command", required=True)
    observe = commands.add_parser("observe", help="Record actual remaining from identified evidence")
    observe.add_argument("--account", required=True)
    observe.add_argument("--bucket", required=True)
    observe.add_argument("--window", required=True)
    observe.add_argument("--remaining", type=int, required=True)
    observe.add_argument("--expires-at", required=True, help="Conservative aware expiry; not an assumed reset")
    observe.add_argument("--evidence", type=Path, required=True)
    activate = commands.add_parser("activate-initial")
    activate.add_argument("--account", required=True)
    activate.add_argument("--bucket", required=True)
    switch = commands.add_parser("request-switch")
    switch.add_argument("--from-account", required=True)
    switch.add_argument("--to-account", required=True)
    switch.add_argument("--bucket", required=True)
    switch.add_argument("--reason", choices=("quota_exhausted", "insufficient_budget"), required=True)
    acknowledge = commands.add_parser("acknowledge-notification")
    acknowledge.add_argument("--event-id", required=True)
    acknowledge.add_argument("--receipt", required=True, help="Reference to delivered user-facing notification")
    commands.add_parser("status")
    args = parser.parse_args()
    try:
        ledger = QuotaLedger(args.ledger)
        if args.command == "observe":
            evidence = args.evidence.resolve(strict=True)
            if not evidence.is_file():
                raise ValueError("Evidence must be a file")
            ledger.observe(args.account, args.bucket, args.window, remaining=args.remaining,
                           reset_at=args.expires_at, evidence_ref=f"sha256:{digest(evidence.read_bytes())}:{evidence.name}")
            result = {"state": "observed", "account_alias": args.account, "bucket": args.bucket,
                      "remaining": args.remaining, "window": args.window}
        elif args.command == "activate-initial":
            ledger.activate_initial(args.account, args.bucket)
            result = {"state": "initial_route", "account_alias": args.account, "bucket": args.bucket}
        elif args.command == "request-switch":
            result = ledger.request_switch(args.from_account, args.to_account, args.bucket, reason=args.reason)
            directory = args.ledger.parent / "notifications"
            directory.mkdir(parents=True, exist_ok=True)
            write_json(directory / f"{result['event_id']}.json", result)
            result = {**result, "instruction": "Deliver a user-visible message BEFORE acknowledge-notification; this output is not a receipt"}
        elif args.command == "acknowledge-notification":
            ledger.acknowledge_notification(args.event_id, receipt=args.receipt)
            result = {"state": "notified", "event_id": args.event_id, "kku_requests": 0}
        else:
            result = ledger.snapshot()
    except (QuotaBlocked, ValueError, OSError) as error:
        reason = str(error) if isinstance(error, QuotaBlocked) else "invalid_configuration_or_evidence"
        print(json.dumps({"state": "blocked", "reason": reason, "kku_requests": 0}))
        return 1
    print(json.dumps(sanitize(result), ensure_ascii=True))
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
