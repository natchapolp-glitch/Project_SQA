import json
import unittest

from scripts.study.api854.kku_client import (
    Account, HTTPResponse, KKUClient, KKUError, Model, normalize_usage, parse_models, pin_model,
)


def response(status=200, payload=None, headers=None):
    return HTTPResponse(status, headers or {}, json.dumps(payload).encode())


def completion(**overrides):
    payload = {"id": "mock-response", "model": 7, "provider": "claude-haiku-test",
               "choices": [{"index": 0, "finish_reason": "stop",
                            "message": {"role": "assistant", "content": "```java\nclass Example {}\n```"}}],
               "usage": {"prompt_tokens": 100, "completion_tokens": 200, "total_tokens": 300},
               "model_quota": {"daily_quota_tokens": 200000, "daily_usage_tokens": 300,
                               "daily_remaining_tokens": 199700}}
    payload.update(overrides)
    return payload


class ScriptedTransport:
    def __init__(self, *responses):
        self.responses = list(responses)
        self.calls = []

    def __call__(self, method, url, headers, body, timeout):
        self.calls.append({"method": method, "url": url, "body": None if body is None else json.loads(body)})
        if not self.responses:
            raise AssertionError("Unexpected retry/request")
        item = self.responses.pop(0)
        if isinstance(item, Exception):
            raise item
        return item


class ClientTests(unittest.TestCase):
    def client(self, transport):
        return KKUClient(Account("a01", "sk_fake_credential_not_real_123456"), transport=transport)

    def test_numeric_models_and_exact_family_pin(self):
        models = parse_models({"data": [{"id": 7, "owned_by": "claude-haiku-test"},
                                         {"id": 8, "owned_by": "gemini-3.5-flash-lite"}]})
        self.assertEqual(pin_model(models, "haiku").id, 7)
        self.assertEqual(pin_model(models, "flash-lite").id, 8)

    def test_actual_string_models_vendor_is_not_version(self):
        models = parse_models({"data": [{"id": "gemini-3.5-flash-lite", "owned_by": "Gemini"},
                                         {"id": "claude-sonnet-5", "owned_by": "Claude"}]})
        model = pin_model(models, "flash-lite")
        self.assertEqual(model.name, "gemini-3.5-flash-lite")
        with self.assertRaises(KKUError):
            pin_model(models, "haiku")
        simple = parse_models([{"id": "gemini-3.5-flash-lite", "name": "Gemini"}])
        self.assertEqual(simple[0], model)

    def test_ambiguous_versions_are_not_silently_selected(self):
        models = [Model(7, "claude-haiku-one"), Model(8, "claude-haiku-two")]
        with self.assertRaises(KKUError):
            pin_model(models, "haiku")
        self.assertEqual(pin_model(models, "haiku", "claude-haiku-two").id, 8)

    def test_simple_list_endpoint_does_not_generate(self):
        transport = ScriptedTransport(response(payload=[{"id": 7, "name": "claude-haiku-test"}]))
        self.client(transport).list_models(simple=True)
        self.assertTrue(transport.calls[0]["url"].endswith("/chat/models-list"))

    def test_success_usage_quota_and_request(self):
        transport = ScriptedTransport(response(payload=completion()))
        result = self.client(transport).complete(Model(7, "claude-haiku-test"), "fixed source", max_tokens=500)
        self.assertEqual(result.metadata["usage"]["total_tokens"], 300)
        self.assertEqual(result.metadata["model_quota"]["daily_remaining_tokens"], 199700)
        self.assertEqual(result.outcome, "response_received")
        body = transport.calls[0]["body"]
        self.assertFalse(body["stream"])
        self.assertEqual(len(body["messages"]), 1)
        self.assertNotIn("Authorization", json.dumps(result.evidence))

    def test_string_id_completion_preserves_actual_provider(self):
        transport = ScriptedTransport(response(payload=completion(model="gemini-3.5-flash-lite", provider="Gemini")))
        result = self.client(transport).complete(Model("gemini-3.5-flash-lite", "gemini-3.5-flash-lite", "Gemini"),
                                                 "fixed", max_tokens=500)
        self.assertEqual(result.metadata["actual_provider"], "Gemini")

    def test_401_reasons_and_redaction(self):
        for message, kind in [("Invalid API key", "auth_failed"), ("Invalid model", "invalid_model"),
                              ("This model reached daily limit.", "daily_limit")]:
            with self.subTest(kind=kind):
                payload = {"error": message, "echo": "sk_fake_credential_not_real_123456",
                           "authorization": "Bearer something", "email": "private@example.test"}
                client = self.client(ScriptedTransport(response(401, payload)))
                with self.assertRaises(KKUError) as raised:
                    client.complete(Model(7, "claude-haiku-test"), "fixed", max_tokens=500)
                error = raised.exception
                self.assertEqual(error.kind, kind)
                self.assertFalse(error.unknown)
                evidence = json.dumps(error.record())
                self.assertNotIn("sk_fake", evidence)
                self.assertNotIn("private@example", evidence)

    def test_429_records_retry_after_without_retry(self):
        transport = ScriptedTransport(response(429, {"error": "rate limit"}, {"Retry-After": "12"}))
        with self.assertRaises(KKUError) as raised:
            self.client(transport).complete(Model(7, "claude-haiku-test"), "fixed", max_tokens=500)
        self.assertEqual(raised.exception.retry_after, "12")
        self.assertEqual(len(transport.calls), 1)

    def test_timeout_and_5xx_are_unknown_and_never_retried(self):
        for item in (TimeoutError("may contain secret"), response(503, {"error": "busy"})):
            with self.subTest(item=type(item).__name__):
                transport = ScriptedTransport(item)
                with self.assertRaises(KKUError) as raised:
                    self.client(transport).complete(Model(7, "claude-haiku-test"), "fixed", max_tokens=500)
                self.assertTrue(raised.exception.unknown)
                self.assertEqual(len(transport.calls), 1)

    def test_malformed_success_is_unknown_with_evidence(self):
        for payload in ([], {"model": 7, "provider": "claude-haiku-test", "choices": []}):
            with self.subTest(payload=payload):
                with self.assertRaises(KKUError) as raised:
                    self.client(ScriptedTransport(response(payload=payload))).complete(
                        Model(7, "claude-haiku-test"), "fixed", max_tokens=500)
                self.assertTrue(raised.exception.unknown)
                self.assertIn("raw_body_sha256", raised.exception.evidence)

    def test_truncation_and_explicit_refusal(self):
        for reason, expected in [("length", "truncated"), ("content_filter", "refused")]:
            payload = completion(choices=[{"finish_reason": reason, "message": {"content": "partial"}}])
            result = self.client(ScriptedTransport(response(payload=payload))).complete(
                Model(7, "claude-haiku-test"), "fixed", max_tokens=500)
            self.assertEqual(result.outcome, expected)

    def test_model_drift_is_not_primary(self):
        with self.assertRaises(KKUError) as raised:
            self.client(ScriptedTransport(response(payload=completion(model=9)))).complete(
                Model(7, "claude-haiku-test"), "fixed", max_tokens=500)
        self.assertEqual(raised.exception.kind, "model_mismatch")

    def test_missing_usage_stays_null(self):
        self.assertIsNone(normalize_usage({})["total_tokens"])
        self.assertIsNone(normalize_usage({"usage": {"input_tokens": 1, "output_tokens": 2}})["total_tokens"])

    def test_external_origin_rejected(self):
        with self.assertRaises(ValueError):
            KKUClient(Account("a01", "credential"), base_url="https://example.test/api/v1")

    def test_duplicate_model_ids_rejected(self):
        with self.assertRaises(KKUError):
            parse_models([{"id": 1, "name": "one"}, {"id": 1, "name": "two"}])
