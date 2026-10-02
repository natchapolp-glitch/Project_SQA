"""Coordinator CLI and HTTP worker interface; no credentials in persisted records."""
from __future__ import annotations

import argparse
import base64
from datetime import datetime, timezone
import hashlib
import hmac
from http.server import BaseHTTPRequestHandler, ThreadingHTTPServer
import json
import os
from pathlib import Path
import re
import ssl

from .inventory import canonical_hash, file_hash
from .queue import Queue, checked_artifacts

CUTOFF = datetime.fromisoformat("2026-10-05T01:00:00+07:00").timestamp()
FREEZE = datetime.fromisoformat("2026-10-05T03:00:00+07:00").timestamp()
MAX_BODY = 12 * 1024 * 1024


def validate_gate(gate, protocol, bugs, expected_gate):
    if gate.get("example_only") is not False or gate.get("gate") != expected_gate:
        raise ValueError("a real signed gate record is required")
    if protocol.get("state") != "frozen" or not bugs.get("installed_verified"):
        raise ValueError("freeze protocol and verify installed inventory first")
    if gate.get("inventory_hash") != bugs["inventory_hash"] or gate.get("protocol_hash") != canonical_hash(protocol):
        raise ValueError("gate hashes differ from inventory/protocol")
    if not all(gate.get("reviewed_by", {}).get(owner) is True for owner in ("champ", "beam", "aom")):
        raise ValueError("all three owners must review gate")
    checks = ["installed_inventory_verified", "four_approach_pipeline_verified", "adapters_verified",
              "quota_notification_verified", "queue_restart_verified", "pilot_context_breadth_verified", "model_settings_verified"]
    if expected_gate == "B":
        checks += ["pilot_validity_verified", "pilot_token_throughput_verified"]
    if not all(gate.get("checks", {}).get(check) is True for check in checks):
        raise ValueError("gate checks incomplete")
    kku = protocol["kku"]
    if (not kku.get("model_settings_verified") or not all(kku.get("exact_model_ids", {}).get(a)
            for a in ("kku-claude", "kku-gemini")) or kku.get("max_output_tokens") is None
            or kku.get("temperature") is None):
        raise ValueError("actual KKU models and supported settings must be frozen")
    checked_artifacts({"artifacts": gate.get("evidence")})
    return {"gate": expected_gate, "sha256": canonical_hash(gate)}


def make_server(queue, host, port, token, artifact_root):
    if not token:
        raise ValueError("queue authentication token is required")
    root = Path(artifact_root).resolve()
    root.mkdir(parents=True, exist_ok=True)

    class Handler(BaseHTTPRequestHandler):
        def log_message(self, *_):
            pass  # Do not log bearer tokens, request bodies, or artifact data.

        def respond(self, status, value):
            data = json.dumps(value, allow_nan=False).encode()
            self.send_response(status)
            self.send_header("Content-Type", "application/json")
            self.send_header("Content-Length", str(len(data)))
            self.end_headers()
            self.wfile.write(data)

        def authorized(self):
            supplied = self.headers.get("Authorization", "")
            if not hmac.compare_digest(supplied.encode(), ("Bearer " + token).encode()):
                self.respond(401, {"error": "unauthorized"})
                return False
            return True

        def do_GET(self):
            if not self.authorized():
                return
            if self.path == "/snapshot":
                self.respond(200, queue.snapshot())
            elif re.fullmatch(r"/artifact/[a-f0-9]{64}/[a-f0-9]{32}/[a-f0-9]{64}", self.path):
                path = root.joinpath(*self.path.split("/")[2:])
                if not path.is_file() or file_hash(path) != path.name:
                    self.respond(404, {"error": "artifact not found or changed"})
                    return
                data = path.read_bytes()
                self.send_response(200)
                self.send_header("Content-Type", "application/octet-stream")
                self.send_header("Content-Length", str(len(data)))
                self.end_headers()
                self.wfile.write(data)
            else:
                self.respond(404, {"error": "unknown endpoint"})

        def do_POST(self):
            if not self.authorized():
                return
            try:
                size = int(self.headers.get("Content-Length", "0"))
                if not 0 < size <= MAX_BODY:
                    raise ValueError("invalid body size")
                payload = json.loads(self.rfile.read(size))
                action = self.path.lstrip("/")
                allowed = {"claim", "renew", "start", "dispatched", "complete", "fail", "defer", "artifact"}
                if action not in allowed:
                    self.respond(404, {"error": "unknown worker endpoint"})
                    return
                if action == "claim":
                    queue.release_expired()
                    if queue.clock() >= FREEZE or (payload.get("stage") == "generate" and queue.clock() >= CUTOFF):
                        self.respond(200, None)
                        return
                if action in {"start", "dispatched"}:
                    with queue.transaction() as db:
                        row = queue.lease_row(db, payload["job_id"], payload["lease"], payload["worker_id"])
                        if queue.clock() >= FREEZE or (row["stage"] == "generate" and queue.clock() >= CUTOFF):
                            raise ValueError("execution cutoff reached; defer unstarted work")
                if action in {"complete", "fail"}:
                    for item in payload.get("record", {}).get("artifacts", []):
                        if not Path(item["path"]).resolve().is_relative_to(root):
                            raise ValueError("upload evidence to queue artifact storage first")
                if action == "artifact":
                    value = self.upload(payload)
                else:
                    value = getattr(queue, action)(**payload)
                    if action == "start":
                        value = {"attempt_id": value}
                self.respond(200, value)
            except (ValueError, TypeError, KeyError, OSError, json.JSONDecodeError) as exc:
                self.respond(400, {"error": type(exc).__name__, "message": "invalid request, lease, or evidence; check contract"})

        def upload(self, payload):
            with queue.transaction() as db:
                job = queue.lease_row(db, payload["job_id"], payload["lease"], payload["worker_id"])
                queue.attempt_row(db, job, payload["attempt_id"])
                data = base64.b64decode(payload["data_base64"], validate=True)
                if not data or len(data) > 8 * 1024 * 1024:
                    raise ValueError("empty/oversized artifact; split large logs into hashed chunks")
                digest = hashlib.sha256(data).hexdigest()
                directory = root / job["job_id"] / payload["attempt_id"]
                directory.mkdir(parents=True, exist_ok=True)
                path = directory / digest
                try:
                    with path.open("xb") as handle:
                        handle.write(data)
                except FileExistsError:
                    if file_hash(path) != digest:
                        raise ValueError("immutable artifact was changed")
                return {"path": str(path), "sha256": digest,
                        "uri": f"/artifact/{job['job_id']}/{payload['attempt_id']}/{digest}"}

    server = ThreadingHTTPServer((host, port), Handler)
    server.daemon_threads = True
    return server


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--db", default=".local/api854/state.sqlite")
    sub = parser.add_subparsers(dest="command", required=True)
    seed = sub.add_parser("seed")
    seed.add_argument("--jobs", required=True)
    seed.add_argument("--protocol", required=True)
    activate = sub.add_parser("activate")
    for argument in ("jobs", "protocol", "bugs", "gate-file"):
        activate.add_argument("--" + argument, required=True)
    activate.add_argument("--gate", choices=("A", "B"), required=True)
    activate.add_argument("--pilot", required=True)
    snapshot = sub.add_parser("snapshot")
    snapshot.add_argument("--output", required=True)
    sub.add_parser("release-expired")
    retry = sub.add_parser("requeue")
    retry.add_argument("--job-id", required=True)
    retry.add_argument("--reason", required=True)
    retry.add_argument("--evidence", required=True, help="JSON array of server paths/SHA-256")
    retry.add_argument("--decision", required=True, choices=("confirmed_not_executed", "confirmed_transport_failed", "confirmed_execution_stopped"))
    recovery = sub.add_parser("recover")
    recovery.add_argument("--resolution", required=True, help="JSON object matching Queue.recover arguments")
    serve = sub.add_parser("serve")
    serve.add_argument("--host", default="127.0.0.1")
    serve.add_argument("--port", type=int, default=8540)
    serve.add_argument("--artifact-root", default=".local/api854/artifacts")
    serve.add_argument("--tls-cert")
    serve.add_argument("--tls-key")
    serve.add_argument("--protocol", required=True)
    args = parser.parse_args()
    queue = Queue(args.db)
    if args.command == "seed":
        document = json.loads(Path(args.jobs).read_text(encoding="utf-8"))
        protocol = json.loads(Path(args.protocol).read_text(encoding="utf-8"))
        if document["protocol_hash"] != queue.register_protocol(protocol):
            raise ValueError("jobs differ from registered protocol")
        print(json.dumps({"added_held_jobs": queue.seed(document["jobs"])}))
    elif args.command == "activate":
        load = lambda path: json.loads(Path(path).read_text(encoding="utf-8"))
        gate, protocol, bugs, jobs, pilot = [load(path) for path in
            (args.gate_file, args.protocol, args.bugs, args.jobs, args.pilot)]
        evidence = validate_gate(gate, protocol, bugs, args.gate)
        queue.register_protocol(protocol)
        if jobs["protocol_hash"] != canonical_hash(protocol) or jobs["inventory_hash"] != bugs["inventory_hash"]:
            raise ValueError("regenerate manifest after freezing protocol")
        if pilot["protocol_hash"] != jobs["protocol_hash"] or pilot["inventory_hash"] != bugs["inventory_hash"]:
            raise ValueError("pilot condition differs")
        pairs = {(r["project"], r["bug_id"]) for r in pilot["bugs"]}
        if len(pairs) != 20 or len({p for p, _ in pairs}) != 17:
            raise ValueError("pilot must cover 20 distinct bugs and 17 projects")
        ids = [j["job_id"] for j in jobs["jobs"] if args.gate == "B" or (j["project"], j["bug_id"]) in pairs]
        queue.activate(ids, evidence)
        print(json.dumps({"activated": len(ids), "gate": args.gate}))
    elif args.command == "snapshot":
        destination = Path(args.output)
        destination.parent.mkdir(parents=True, exist_ok=True)
        destination.write_text(json.dumps(queue.snapshot(), indent=2) + "\n", encoding="utf-8", newline="\n")
    elif args.command == "release-expired":
        print(json.dumps({"released": queue.release_expired()}))
    elif args.command == "requeue":
        queue.requeue(args.job_id, args.reason, json.loads(Path(args.evidence).read_text(encoding="utf-8")), args.decision)
    elif args.command == "recover":
        queue.recover(**json.loads(Path(args.resolution).read_text(encoding="utf-8")))
    else:
        protocol = json.loads(Path(args.protocol).read_text(encoding="utf-8"))
        ph = queue.register_protocol(protocol)
        if any(j["protocol_hash"] != ph for j in queue.snapshot()["jobs"]):
            raise ValueError("serve exactly one protocol per runtime database")
        if args.host not in ("localhost", "127.0.0.1", "::1") and not (args.tls_cert and args.tls_key):
            raise ValueError("remote binding requires TLS; alternatively use an SSH tunnel to localhost")
        server = make_server(queue, args.host, args.port, os.environ.get("API854_QUEUE_TOKEN"), args.artifact_root)
        if args.tls_cert and args.tls_key:
            context = ssl.SSLContext(ssl.PROTOCOL_TLS_SERVER)
            context.load_cert_chain(args.tls_cert, args.tls_key)
            server.socket = context.wrap_socket(server.socket, server_side=True)
        print("Queue service ready; jobs remain held until signed gates. No API generation is started by this service.")
        try:
            server.serve_forever()
        except KeyboardInterrupt:
            pass
        finally:
            server.server_close()


if __name__ == "__main__":
    main()
