"""Beam's read-only connection check using Aom's published QueueClient."""
from __future__ import annotations

import argparse
import json
import os
from pathlib import Path
from urllib.parse import urlsplit

from .common import ROOT, read_json, write_json
from .queue_client import QueueClient


def connection_check(access_file):
    access = read_json(access_file)
    if access.get("role") != "beam" or access.get("schema_version") != "1.0":
        raise ValueError("Expected Beam access file for queue schema 1.0")
    url = urlsplit(access["base_url"])
    if url.scheme != "https" or not url.hostname or url.username or url.password or url.query or url.fragment:
        raise ValueError("Worker token must use the explicit HTTPS queue base URL")
    client = QueueClient(access["base_url"], access["worker_token"])
    try:
        health = client.request("GET", "/health")
        schema = client.request("GET", "/v1/schema")
        status = client.request("GET", "/v1/status")
    except Exception as error:
        # A server/network error must not echo the credential in CLI output.
        raise RuntimeError(str(error).replace(access["worker_token"], "[redacted]")) from None
    if health.get("service") != "sqa-api854-queue" or health.get("ready") is not True:
        raise ValueError("Queue health does not establish readiness")
    if schema.get("schema_version") != "1.0" or status.get("schema_version") != "1.0":
        raise ValueError("Queue schema changed; review contract before starting workers")
    return {"base_url": access["base_url"], "role": "beam", "health_ready": True,
            "authenticated_schema_version": "1.0", "jobs": len(status.get("jobs", [])),
            "attempts": len(status.get("attempts", [])), "mutations_sent": 0,
            "note": "Connectivity only; no claim, queue seed, KKU request or experiment was started."}


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--access-file", type=Path, default=Path(os.environ.get("SQA_QUEUE_ACCESS_FILE", ROOT / ".local/api854/beam-access.private.json")))
    parser.add_argument("--output", type=Path)
    args = parser.parse_args()
    result = connection_check(args.access_file)
    if args.output:
        args.output.parent.mkdir(parents=True, exist_ok=True)
        write_json(args.output, result)
    print(json.dumps(result, ensure_ascii=False))


if __name__ == "__main__":
    main()
