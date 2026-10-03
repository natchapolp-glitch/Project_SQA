"""Offline acceptance and meaningful failure boundaries for sealed v8 inputs."""
from pathlib import Path
import tempfile
import unittest
from unittest.mock import patch

from scripts.study.api854.audit_v8_shared_limits import (
    ARTIFACTS, PREP, PROTOCOL, SOURCE, consumer_check, inspect, partition)
from scripts.study.api854.api_worker import WorkerBlocked
from scripts.study.api854.common import ROOT, read_json, sha256
from scripts.study.api854.preparation import encoded


class V8SharedLimitsTests(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        with patch('urllib.request.OpenerDirector.open', side_effect=AssertionError('Audit attempted HTTP')):
            cls.audit = inspect()
        cls.protocol = read_json(ROOT/PROTOCOL)
        cls.row = cls.audit['largest_prompt']
        cls.folder = ROOT/PREP/f"{cls.row['project']}-{cls.row['bug_id']}"

    def test_actual_twenty_inputs_and_guards_are_verified_offline(self):
        self.assertEqual(len(self.audit['records']), 20)
        self.assertEqual(self.audit['target_count'], 378)
        self.assertEqual(self.audit['capability_exclusion_count'], 313)
        self.assertEqual(self.audit['proposal_rejections'], {
            'actual_proposal': 'protocol_not_frozen',
            'isolated_approval_flag_changed': 'prepare_contract_not_frozen'})
        self.assertTrue(all(r['cpu_shared_loader_verified'] and
            r['api_approaches_verified'] == ['kku-claude', 'kku-gemini'] for r in self.audit['records']))
        self.assertFalse(self.audit['gate_a_passed'])

    def test_final_reserve_and_limits_remain_unknown_despite_complete_prompt_inventory(self):
        self.assertIsNone(self.audit['final_prompt_reserve'])
        self.assertIsNone(self.audit['framing_bound_H'])
        self.assertIsNone(self.audit['model_context_limit_tokens'])
        self.assertIsNone(self.audit['model_max_output_limit_tokens'])
        self.assertIsNone(self.audit['provider_token_count_for_v8_prompts'])
        self.assertFalse(self.audit['quota_bucket_window_reset_expiry_verified'])
        self.assertEqual(len(self.audit['conditional_reserve_worksheet']), 40)
        shared_floor = self.audit['max_prompt_utf8_bytes']+4096
        for row in self.audit['conditional_reserve_worksheet']:
            self.assertEqual(row['shared_frozen_reservation_numerical_floor_before_unknown_framing'], shared_floor)
            self.assertEqual(row['historical_headroom_after_shared_floor_before_unknown_framing'],
                             row['historical_remaining_tokens']-shared_floor)
            self.assertFalse(row['json_overhead_is_provider_framing_H'])
            self.assertFalse(row['live_reservation_ready'])

    def test_api_rejects_reserve_one_byte_below_largest_actual_prompt(self):
        with self.assertRaisesRegex(WorkerBlocked, 'prompt_exceeds_frozen_conservative_bound'):
            consumer_check(self.folder, self.row, self.protocol, sha256(ROOT/PROTOCOL),
                           reserve=self.row['prompt_utf8_bytes']-1)

    def test_changed_prompt_rejected_by_shared_consumer(self):
        with tempfile.TemporaryDirectory() as directory:
            folder = Path(directory)
            for name in ARTIFACTS:
                (folder/name).write_bytes((self.folder/name).read_bytes())
            (folder/'prompt.md').write_bytes((folder/'prompt.md').read_bytes()+b'\nchanged')
            with self.assertRaisesRegex(ValueError, 'Preparation hashes'):
                consumer_check(folder, self.row, self.protocol, sha256(ROOT/PROTOCOL))

    def test_omitted_exclusion_cannot_close_original_declaration_inventory(self):
        row = next(r for r in self.audit['records'] if r['capability_exclusion_count'])
        actual = ROOT/PREP/f"{row['project']}-{row['bug_id']}"
        with tempfile.TemporaryDirectory() as directory:
            folder = Path(directory)
            (folder/'targets.json').write_bytes((actual/'targets.json').read_bytes())
            exclusions = read_json(actual/'capability-exclusions.json')
            exclusions['excluded'].pop()
            (folder/'capability-exclusions.json').write_bytes(encoded(exclusions))
            with self.assertRaisesRegex(ValueError, 'Capability partition differs'):
                partition(folder, ROOT/SOURCE/actual.name, read_json(actual/'prepare-metadata.json'))


if __name__ == '__main__':
    unittest.main()
