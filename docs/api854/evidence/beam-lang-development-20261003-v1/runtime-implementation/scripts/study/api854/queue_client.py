"""Small stdlib client for Champ/Beam workers; never prints private credentials."""
from __future__ import annotations

import argparse
import json
import os
from pathlib import Path
import urllib.error
import urllib.request


class NoRedirect(urllib.request.HTTPRedirectHandler):
    def redirect_request(self, req, fp, code, msg, headers, newurl):
        return None


class QueueClient:
    def __init__(self, base_url, token, timeout=30):
        self.base_url = base_url.rstrip("/")
        self.token = token
        self.timeout = timeout
        self.opener = urllib.request.build_opener(NoRedirect())

    @classmethod
    def from_environment(cls):
        access = {}
        token = os.environ.get("SQA_QUEUE_TOKEN")
        if not token and os.environ.get("SQA_QUEUE_ACCESS_FILE"):
            access = json.loads(Path(os.environ["SQA_QUEUE_ACCESS_FILE"]).read_text(encoding="utf-8"))
            token = access["worker_token"]
        if not token:
            raise ValueError("Set SQA_QUEUE_ACCESS_FILE (private file) or SQA_QUEUE_TOKEN")
        url = os.environ.get("SQA_QUEUE_URL") or access.get("base_url")
        if not url:
            raise ValueError("Set SQA_QUEUE_URL or base_url in the private access file")
        return cls(url, token)

    def request(self, method, path, body=None, headers=None, raw=False):
        data = body if isinstance(body, bytes) else json.dumps(body).encode() if body is not None else None
        request = urllib.request.Request(self.base_url + path, data=data, method=method,
            headers={"Authorization": "Bearer " + self.token,
                     "Content-Type": "application/octet-stream" if isinstance(body, bytes) else "application/json",
                     **(headers or {})})
        try:
            with self.opener.open(request, timeout=self.timeout) as response:
                content = response.read()
                return content if raw else json.loads(content)
        except urllib.error.HTTPError as error:
            # Error messages have no token or request body; don't automatically retry mutations.
            detail = error.read().decode("utf-8", errors="replace")[:1024]
            raise RuntimeError(f"Queue HTTP {error.code}: {detail}") from None

    def claim(self, worker_id, stage, approaches=None, owner=None, lease_seconds=900):
        body = {"worker_id": worker_id, "stage": stage, "lease_seconds": lease_seconds}
        if approaches is not None:
            body["approaches"] = approaches
        if owner is not None:
            body["owner"] = owner
        return self.request("POST", "/v1/jobs/claim", body)

    @staticmethod
    def envelope(claim):
        return {key: claim[key] for key in ("attempt_id", "lease_token", "lease_version")}

    def renew(self, claim, lease_seconds=900):
        return self.request("POST", f"/v1/jobs/{claim['job']['job_id']}/renew",
                            {**self.envelope(claim), "lease_seconds": lease_seconds})

    def upload(self, claim, path):
        path = Path(path)
        return self.request("POST", f"/v1/jobs/{claim['job']['job_id']}/artifacts", path.read_bytes(),
            {"X-Artifact-Name": path.name, "X-Attempt-ID": claim["attempt_id"],
             "X-Lease-Token": claim["lease_token"], "X-Lease-Version": str(claim["lease_version"])})

    def complete(self, claim, outcome, artifacts, metadata, failed=False):
        action = "fail" if failed else "complete"
        return self.request("POST", f"/v1/jobs/{claim['job']['job_id']}/{action}",
            {**self.envelope(claim), "outcome": outcome,
             "artifact_ids": [artifact["artifact_id"] for artifact in artifacts], "metadata": metadata})

    def download(self, artifact):
        import hashlib
        content = self.request("GET", artifact["uri"], raw=True)
        if hashlib.sha256(content).hexdigest() != artifact["sha256"]:
            raise ValueError("Downloaded artifact SHA256 mismatch")
        return content


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("action", choices=("check", "schema", "status"))
    args = parser.parse_args()
    client = QueueClient.from_environment()
    if args.action == "check":
        health = client.request("GET", "/health")
        contract = client.request("GET", "/v1/schema")
        print(json.dumps({"health": health, "authenticated_schema_version": contract["schema_version"]}))
    else:
        print(json.dumps(client.request("GET", "/v1/" + args.action), ensure_ascii=False, indent=2))


if __name__ == "__main__":
    main()
