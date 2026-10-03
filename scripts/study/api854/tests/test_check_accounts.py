import contextlib
import io
import json
from pathlib import Path
import tempfile
import unittest
from unittest.mock import patch

from scripts.study.api854.check_accounts import inspect_accounts, main


class AccountIntakeTests(unittest.TestCase):
    def test_complete_configuration_does_not_certify_authentication_or_quota(self):
        keys = [f'private-fixture-{number}' for number in range(10)]
        report = inspect_accounts({'accounts': [{'alias': f'a{number+1:02d}', 'api_key': key}
                                  for number, key in enumerate(keys)]}, environ={})
        self.assertEqual(report['state'], 'ready_for_authenticated_preflight')
        self.assertEqual(report['configured_accounts'], 10)
        self.assertFalse(report['authenticated'])
        self.assertFalse(report['quota_verified'])
        self.assertFalse(report['distinct_quota_buckets_verified'])
        self.assertFalse(report['pilot_enabled'])
        self.assertTrue(all(key not in json.dumps(report) for key in keys))

    def test_one_key_is_incomplete_and_file_is_not_mutated(self):
        document = {'accounts': [{'alias': 'a01', 'api_key': 'private-fixture-one'}]}
        original = json.dumps(document)
        report = inspect_accounts(document, environ={})
        self.assertEqual(report['configured_accounts'], 1)
        self.assertEqual(report['missing_or_invalid_accounts'], 9)
        self.assertEqual(report['state'], 'incomplete')
        self.assertEqual(json.dumps(document), original)

    def test_duplicate_aliases_and_keys_cannot_count_as_distinct_accounts(self):
        same = 'private-fixture-same'
        report = inspect_accounts({'accounts': [{'alias': 'a01', 'api_key': same},
                                  {'alias': 'a02', 'api_key': same}]}, aliases=['a01', 'a02'], environ={})
        self.assertEqual(report['state'], 'blocked')
        self.assertEqual(report['shared_credential_alias_groups'], [['a01', 'a02']])
        duplicate = inspect_accounts({'accounts': [{'alias': 'a01', 'api_key': same},
                                     {'alias': 'a01', 'api_key': 'private-fixture-other'}]}, aliases=['a01'], environ={})
        self.assertEqual(duplicate['accounts'][0]['status'], 'ambiguous_alias')
        self.assertEqual(duplicate['configured_accounts'], 0)
        self.assertNotIn(same, json.dumps(report) + json.dumps(duplicate))

    def test_effective_environment_override_matches_worker_precedence(self):
        report = inspect_accounts({'accounts': [{'alias': 'a01', 'api_key': 'private-fixture-same'},
                                  {'alias': 'a02', 'api_key': 'private-fixture-same'}]}, aliases=['a01', 'a02'],
                                  environ={'KKU_API_KEY_A02': 'private-fixture-override'})
        self.assertEqual(report['state'], 'ready_for_authenticated_preflight')
        self.assertEqual(report['accounts'][1]['effective_source'], 'environment')

    def test_non_string_placeholders_and_newlines_are_not_credentials(self):
        for key in [None, 123, True, '', '   ', 'null', 'YOUR_API_KEY', 'private\nfixture']:
            with self.subTest(value_type=type(key).__name__):
                report = inspect_accounts({'accounts': [{'alias': 'a01', 'api_key': key}]}, aliases=['a01'], environ={})
                self.assertNotEqual(report['state'], 'ready_for_authenticated_preflight')
                self.assertEqual(report['configured_accounts'], 0)

    def test_malformed_private_file_does_not_echo_contents_in_cli_output(self):
        secret = 'private-fixture-never-echo'
        with tempfile.TemporaryDirectory() as directory:
            path = Path(directory) / 'accounts.private.json'
            path.write_text('{"accounts": ["' + secret, encoding='utf-8')
            out = io.StringIO()
            with contextlib.redirect_stdout(out):
                result = main(['--secrets', str(path)])
            self.assertEqual(result, 2)
            self.assertNotIn(secret, out.getvalue())
            self.assertEqual(json.loads(out.getvalue())['kku_requests'], 0)

    def test_invalid_alias_never_echoes_secret_in_an_alias_field(self):
        secret = 'private-fixture-never-echo'
        report = inspect_accounts({'accounts': [{'alias': secret, 'api_key': secret}]}, environ={})
        self.assertEqual(report['state'], 'blocked')
        self.assertNotIn(secret, json.dumps(report))
