"""KKU non-streaming client. Transport errors never trigger automatic retries.

Reference: https://gen.ai.kku.ac.th/docs/api (checked 2026-10-03).
Uses only Python's standard library and an injectable transport for offline QA.
"""
from __future__ import annotations

from dataclasses import dataclass, field
from datetime import datetime, timezone
import hashlib
import json
import math
import os
from pathlib import Path
import re
import socket
from typing import Any, Callable
import urllib.error
import urllib.parse
import urllib.request

BASE_URL = "https://gen.ai.kku.ac.th/api/v1"
_SENSITIVE = {"authorization", "x-api-key", "api_key", "apikey", "email", "access_token"}


def sanitize(value: Any, secrets: tuple[str, ...] = ()) -> Any:
    """Redact credentials even if a provider echoes them in an error body."""
    if isinstance(value, dict):
        return {k: "[REDACTED]" if str(k).lower() in _SENSITIVE else sanitize(v, secrets)
                for k, v in value.items()}
    if isinstance(value, list):
        return [sanitize(v, secrets) for v in value]
    if isinstance(value, str):
        for secret in secrets:
            if secret:
                value = value.replace(secret, "[REDACTED]")
        value = re.sub(r"\bsk_[A-Za-z0-9_-]{12,}\b", "[REDACTED]", value)
        value = re.sub(r"(?i)Bearer\s+\S+", "Bearer [REDACTED]", value)
        return value
    return value


def digest(value: bytes) -> str:
    return hashlib.sha256(value).hexdigest()


def utc_now() -> str:
    return datetime.now(timezone.utc).isoformat()


@dataclass(frozen=True)
class Account:
    alias: str
    api_key: str = field(repr=False)

    def __post_init__(self):
        if not re.fullmatch(r"a[0-9]{2,}", self.alias) or not self.api_key.strip():
            raise ValueError("Require account alias aXX and a non-empty credential")


def load_account(alias: str, secret_file: Path | None = None) -> Account:
    """Environment has precedence; never log the returned object as a dict."""
    if not re.fullmatch(r"a[0-9]{2,}", alias):
        raise ValueError("Invalid account alias")
    key = os.environ.get(f"KKU_API_KEY_{alias.upper()}")
    if key:
        return Account(alias, key.strip())
    if secret_file is not None:
        data = json.loads(secret_file.read_text(encoding="utf-8"))
        matches = [x for x in data.get("accounts", []) if x.get("alias") == alias]
        if len(matches) == 1:
            return Account(alias, str(matches[0]["api_key"]).strip())
    raise ValueError(f"No credential configured for {alias}")


@dataclass(frozen=True)
class HTTPResponse:
    status: int
    headers: dict[str, str]
    body: bytes


class _NoRedirect(urllib.request.HTTPRedirectHandler):
    def redirect_request(self, req, fp, code, msg, headers, newurl):
        # Do not forward Authorization to any redirect destination.
        return None


def http_transport(method: str, url: str, headers: dict, body: bytes | None,
                   timeout: float) -> HTTPResponse:
    opener = urllib.request.build_opener(_NoRedirect())
    request = urllib.request.Request(url, data=body, headers=headers, method=method)
    try:
        with opener.open(request, timeout=timeout) as response:
            return HTTPResponse(response.status, dict(response.headers), response.read())
    except urllib.error.HTTPError as error:
        return HTTPResponse(error.code, dict(error.headers), error.read())


class KKUError(Exception):
    """Safe structured exception. Unknown outcomes must be reconciled manually."""
    def __init__(self, kind: str, message: str, *, status: int | None = None,
                 evidence: dict | None = None, unknown: bool = False,
                 retry_after: str | None = None):
        super().__init__(message)
        self.kind, self.status, self.unknown = kind, status, unknown
        self.evidence, self.retry_after = evidence or {}, retry_after

    def record(self) -> dict:
        return {"kind": self.kind, "message": str(self), "http_status": self.status,
                "outcome_unknown": self.unknown, "retry_after": self.retry_after,
                "evidence": self.evidence}


@dataclass(frozen=True)
class Model:
    id: int | str
    name: str
    provider: str | None = None


def parse_models(payload: Any) -> list[Model]:
    rows = payload.get("data") if isinstance(payload, dict) else payload
    if not isinstance(rows, list):
        raise KKUError("invalid_response", "Model list is not an array")
    models = []
    for row in rows:
        if not isinstance(row, dict):
            raise KKUError("invalid_response", "Invalid model-list entry")
        model_id = row.get("id")
        if isinstance(model_id, str) and model_id.isdigit():
            model_id = int(model_id)
        if isinstance(model_id, str):
            if not re.fullmatch(r"[A-Za-z0-9][A-Za-z0-9._:-]{0,199}", model_id):
                raise KKUError("invalid_response", "Invalid string model ID")
            # Actual /models and /chat/models-list currently use string IDs and
            # vendor names, unlike the numeric IDs in the published docs.
            name = model_id
            provider = row.get("owned_by") or row.get("name")
        else:
            name = row.get("name") or row.get("owned_by")
            provider = row.get("owned_by")
            if type(model_id) is not int or model_id < 1 or not isinstance(name, str) or not name:
                raise KKUError("invalid_response", "Model needs an exact name and a numeric or string ID")
        if provider is not None and not isinstance(provider, str):
            raise KKUError("invalid_response", "Model provider is not text")
        models.append(Model(model_id, name, provider))
    if len({m.id for m in models}) != len(models):
        raise KKUError("invalid_response", "Duplicate numeric model IDs")
    return models


def pin_model(models: list[Model], family: str, exact_name: str | None = None) -> Model:
    if family not in {"haiku", "sonnet", "flash-lite"}:
        raise ValueError("Model family must be haiku, sonnet or flash-lite")
    if family in {"haiku", "sonnet"}:
        candidates = [m for m in models if "claude" in m.name.lower() and family in m.name.lower()]
    else:
        candidates = [m for m in models if "gemini" in m.name.lower()
                      and re.search(r"flash[-_ ]?lite", m.name.lower())]
    if exact_name is not None:
        candidates = [m for m in candidates if m.name == exact_name]
    if len(candidates) != 1:
        raise KKUError("model_mapping", f"Expected one exact {family} model; found {len(candidates)}")
    return candidates[0]


def nonnegative_int(value: Any) -> int | None:
    return value if type(value) is int and value >= 0 else None


def normalize_usage(payload: dict) -> dict:
    usage = payload.get("usage") or {}
    if not isinstance(usage, dict):
        return {"prompt_tokens": None, "completion_tokens": None, "total_tokens": None}
    # Missing measurements remain null; don't fabricate sums (thinking/cache may differ).
    return {"prompt_tokens": nonnegative_int(usage.get("prompt_tokens", usage.get("input_tokens"))),
            "completion_tokens": nonnegative_int(usage.get("completion_tokens", usage.get("output_tokens"))),
            "total_tokens": nonnegative_int(usage.get("total_tokens"))}


def normalize_quota(payload: dict) -> dict:
    quota = payload.get("model_quota") or {}
    if not isinstance(quota, dict):
        quota = {}
    return {k: nonnegative_int(quota.get(k)) for k in
            ("daily_quota_tokens", "daily_usage_tokens", "daily_remaining_tokens")}


@dataclass(frozen=True)
class Completion:
    content: str
    outcome: str
    metadata: dict
    evidence: dict


class KKUClient:
    def __init__(self, account: Account, *, transport: Callable = http_transport,
                 timeout: float = 120, base_url: str = BASE_URL):
        parsed = urllib.parse.urlparse(base_url)
        if parsed.scheme != "https" or parsed.hostname != "gen.ai.kku.ac.th" or parsed.port not in (None, 443) or \
                parsed.path.rstrip("/") != "/api/v1" or parsed.username or parsed.query or parsed.fragment:
            raise ValueError("Credentials may only be sent to the documented KKU HTTPS origin")
        if timeout <= 0 or not math.isfinite(timeout):
            raise ValueError("Timeout must be finite and positive")
        self.account, self.transport, self.timeout = account, transport, timeout
        self.base_url = base_url.rstrip("/")

    def _request(self, method: str, path: str, payload: dict | None = None) -> tuple[Any, dict]:
        body = None if payload is None else json.dumps(payload, ensure_ascii=False, allow_nan=False).encode()
        headers = {"Authorization": f"Bearer {self.account.api_key}",
                   "Content-Type": "application/json", "Accept": "application/json"}
        started = utc_now()
        try:
            response = self.transport(method, self.base_url + path, headers, body, self.timeout)
        except (urllib.error.URLError, TimeoutError, socket.timeout, OSError):
            raise KKUError("transport_unknown", "No trustworthy HTTP outcome; reconcile before resending",
                           evidence={"started_at_utc": started, "ended_at_utc": utc_now()},
                           unknown=True) from None
        lower_headers = {k.lower(): v for k, v in response.headers.items()}
        try:
            parsed = json.loads(response.body)
        except (ValueError, UnicodeDecodeError):
            parsed = {"non_json_body": response.body.decode("utf-8", errors="replace")}
        evidence = {"http_status": response.status, "started_at_utc": started,
                    "ended_at_utc": utc_now(), "raw_body_sha256": digest(response.body),
                    "body": sanitize(parsed, (self.account.api_key,)),
                    "headers": sanitize({k: lower_headers[k] for k in
                                ("x-request-id", "request-id", "retry-after", "date", "content-type")
                                if k in lower_headers}, (self.account.api_key,))}
        if not 200 <= response.status < 300:
            text = json.dumps(parsed, ensure_ascii=False).lower()
            if response.status == 401 and "daily limit" in text:
                kind = "daily_limit"
            elif response.status in (401, 403) and "invalid model" in text:
                kind = "invalid_model"
            elif response.status in (401, 403) and "invalid api key" in text:
                kind = "auth_failed"
            elif response.status == 429:
                kind = "rate_limited"
            elif response.status >= 500:
                kind = "server_unknown"
            elif response.status in (401, 403):
                kind = "auth_unclassified"
            elif 300 <= response.status < 400:
                kind = "redirect_blocked"
            elif response.status == 408:
                kind = "server_unknown"
            else:
                kind = "request_rejected"
            raise KKUError(kind, f"KKU request returned {kind}", status=response.status,
                           evidence=evidence, unknown=kind in {"server_unknown", "auth_unclassified"},
                           retry_after=sanitize(lower_headers.get("retry-after"), (self.account.api_key,)))
        if isinstance(parsed, dict) and ("non_json_body" in parsed or parsed.get("error")):
            raise KKUError("invalid_response", "Success HTTP status with an invalid response body",
                           status=response.status, evidence=evidence, unknown=True)
        return parsed, evidence

    def list_models(self, *, simple: bool = False) -> tuple[list[Model], dict]:
        payload, evidence = self._request("POST" if simple else "GET",
                                          "/chat/models-list" if simple else "/models",
                                          {} if simple else None)
        try:
            return parse_models(payload), evidence
        except KKUError as error:
            error.evidence = evidence
            raise

    def complete(self, model: Model, prompt: str, *, max_tokens: int,
                 temperature: float | None = None) -> Completion:
        if not prompt.strip() or type(max_tokens) is not int or max_tokens < 1:
            raise ValueError("Require a non-empty fixed-context prompt and positive max_tokens")
        request = {"model": model.id, "messages": [{"role": "user", "content": prompt}],
                   "max_tokens": max_tokens, "stream": False}
        if temperature is not None:
            if not math.isfinite(temperature) or not 0 <= temperature <= 2:
                raise ValueError("temperature must be between 0 and 2")
            request["temperature"] = temperature
        payload, evidence = self._request("POST", "/chat/completions", request)
        if not isinstance(payload, dict):
            raise KKUError("invalid_response", "Completion is not an object", evidence=evidence, unknown=True)
        actual_id, provider = payload.get("model"), payload.get("provider")
        # Preserve the exact returned ID/version; a vendor label isn't a version.
        model_matches = str(actual_id) in {str(model.id), model.name}
        if not model_matches or (provider is not None and provider not in {model.name, model.provider}):
            raise KKUError("model_mismatch", "Actual model differs from the pinned mapping", evidence=evidence)
        choices = payload.get("choices")
        if not isinstance(choices, list) or len(choices) != 1 or not isinstance(choices[0], dict):
            raise KKUError("invalid_response", "Expected exactly one completion choice", evidence=evidence, unknown=True)
        choice = choices[0]
        message = choice.get("message")
        if not isinstance(message, dict):
            raise KKUError("invalid_response", "Missing assistant message", evidence=evidence, unknown=True)
        content = message.get("content") or ""
        if not isinstance(content, str):
            raise KKUError("invalid_response", "Assistant content is not text", evidence=evidence, unknown=True)
        reason = choice.get("finish_reason")
        if message.get("refusal") or reason in {"content_filter", "refusal"}:
            outcome = "refused"
        elif reason in {"length", "max_tokens"}:
            outcome = "truncated"
        elif reason not in {"stop", "end_turn"}:
            outcome = "generation_failed"
        elif not content.strip():
            outcome = "generation_failed"
        else:
            outcome = "response_received"  # NOT proof of Java validity or passing tests.
        metadata = {"response_id": payload.get("id"), "requested_model_id": model.id,
                    "requested_model_name": model.name, "actual_model": actual_id,
                    "actual_provider": provider, "finish_reason": reason,
                    "usage": normalize_usage(payload), "model_quota": normalize_quota(payload)}
        return Completion(content, outcome, sanitize(metadata, (self.account.api_key,)), evidence)
