"""Reject mixed conditions, repeated pairs and unsupported token-readiness claims."""
import copy
import hashlib
import json
import unittest

from scripts.study.api854.common import ROOT, read_json, sha256
from scripts.study.api854.review_v10_readiness import (
    CONDITION, MODELS, PREP, PAIR, READY, check_received_worksheet, worksheet_row,
)

PACKET = ROOT / 'output/api854-20261003/champ-v10-readiness-review-v2'


class V10ReadinessReviewTests(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.review = read_json(PACKET / 'receipt.json')
        cls.worksheet = read_json(PACKET / 'prompt-model-worksheet.json')
        cls.received = read_json(PACKET / 'received/aom' / READY / 'prompt-reserve-worksheet.json')
        cls.index_hash = sha256(PACKET / 'received/aom' / PREP / 'index.json')
        cls.protocol_hash = sha256(PACKET / 'received/aom' / PAIR / 'protocol.proposal.json')

    def validate(self, worksheet):
        check_received_worksheet(worksheet, self.worksheet['records'], self.index_hash,
                                 self.protocol_hash, self.review['runtime_source_sha256'])

    def test_current_packet_and_failed_count_guard_keep_sealed_bytes(self):
        for folder in (PACKET, PACKET.parent / 'champ-v10-readiness-review-v1'):
            for path, expected in read_json(folder / 'checksums.json').items():
                self.assertEqual(sha256(folder / path), expected, path)
        failed = read_json(PACKET.parent / 'champ-v10-readiness-review-v1/failed-review-receipt.json')
        self.assertTrue(failed['received_tests_passed'])
        self.assertFalse(failed['native_java_executed'])
        self.assertEqual(self.review['condition'], CONDITION)
        self.assertEqual((self.review['selected'], self.review['exclusions'], self.review['denominator']), (390,301,691))

    def test_received_forty_pairs_bind_actual_prompts_and_both_requested_models(self):
        self.validate(self.received)
        rows = self.worksheet['records']
        self.assertEqual(len({(r['project'],r['bug_id'],r['approach']) for r in rows}),40)
        self.assertEqual({r['requested_model_id'] for r in rows},set(MODELS.values()))
        self.assertEqual(max(r['prompt_utf8_bytes'] for r in rows),265937)
        self.assertEqual(self.worksheet['conditional_byte_guard_floor'],270033)
        self.assertIsNone(self.worksheet['final_token_reserve'])
        self.assertFalse(self.worksheet['byte_guard_is_provider_token_reserve'])

    def test_replacing_a_pair_with_duplicate_does_not_cover_twenty_bugs_twice(self):
        changed = copy.deepcopy(self.received)
        changed['records'][0] = copy.deepcopy(changed['records'][1])
        with self.assertRaisesRegex(ValueError,'Duplicate/missing'):
            self.validate(changed)

    def test_historical_model_alias_cannot_replace_v10_requested_id(self):
        changed = copy.deepcopy(self.received)
        changed['records'][0]['requested_model_id'] = 'claude-sonnet-4'
        with self.assertRaisesRegex(ValueError,'prompt/model/owner'):
            self.validate(changed)

    def test_output_change_and_fabricated_framing_cannot_reuse_received_reserve(self):
        for key, value in (('output_cap',4095),('temperature',0.7),('framing_overhead',0)):
            changed = copy.deepcopy(self.received)
            changed['records'][0][key] = value
            with self.subTest(key=key), self.assertRaises(ValueError):
                self.validate(changed)
        self.assertTrue(all(r['provider_framing_tokens'] is None and not r['json_overhead_is_provider_framing']
                            for r in self.worksheet['records']))

    def test_byte_guard_does_not_establish_tokens_or_current_quota(self):
        changed = copy.deepcopy(self.received)
        changed['final_reserve'] = 270033
        changed['records'][0]['final_reserve'] = changed['records'][0]['prompt_utf8_bytes'] + 4096
        with self.assertRaisesRegex(ValueError,'Unsubstantiated'):
            self.validate(changed)
        for row in self.worksheet['records']:
            self.assertIsNone(row['provider_prompt_tokens'])
            self.assertIsNone(row['current_quota_remaining_tokens'])
            self.assertFalse(row['effective_settings_verified'])
            self.assertFalse(row['live_reservation_ready'])
        self.assertFalse(self.review['gate_a_passed'])
        self.assertEqual(self.review['authenticated_kku_requests'],0)

    def test_mixed_protocol_or_runtime_rejects_same_prompt_sizes(self):
        for key in ('condition','protocol_sha256','preparation_index_sha256','runtime_source_sha256'):
            changed = copy.deepcopy(self.received)
            changed[key] = {} if key=='runtime_source_sha256' else 'wrong-condition-pin'
            with self.subTest(key=key), self.assertRaisesRegex(ValueError,'condition/pins'):
                self.validate(changed)
        # Serializing Unicode/newlines is a transport measurement, independent
        # of a model tokenizer or hidden backend system-message framing.
        row = {'project':'Csv','bug_id':1,'owner':'beam'}
        prompt = 'read "a"\nทดสอบ'.encode()
        result = worksheet_row(row,prompt,'kku-claude',MODELS['kku-claude'])
        wire = json.dumps({'model':MODELS['kku-claude'],'messages':[{'role':'user','content':prompt.decode()}],
                           'max_tokens':4096,'stream':False,'temperature':0},ensure_ascii=False,allow_nan=False).encode()
        self.assertEqual(result['serialized_request_sha256'],hashlib.sha256(wire).hexdigest())
        self.assertIsNone(result['provider_framing_tokens'])

    def test_native_fixed_integration_matches_received_bounded_outputs_twice(self):
        proof = read_json(PACKET / 'native-fixed-runtime-recheck.json')
        received = read_json(PACKET / 'received/aom' / READY / 'fixed-runtime-verification.json')
        self.assertEqual(proof['observations'], received['observations'])
        self.assertEqual(proof['buffer_csv_lang_fixed_cases'], received['buffer_csv_lang_fixed_cases'])
        rows = proof['observations'] + proof['buffer_csv_lang_fixed_cases']
        self.assertEqual(len(rows),64)
        for row in rows:
            self.assertEqual(row['fixed_first'],row['fixed_second'])
            self.assertTrue(row['fixed_first']['target_invoked'])
            self.assertNotIn('fixture_error',row['fixed_first'])
        self.assertTrue(proof['temporary_setter_mutation_detected'])
        self.assertFalse(self.review['full_condition_semantic_host_accepted'])
        self.assertEqual(read_json(PACKET/'received-focused-tests.json')['four_consumer_bug_approach_combinations'],80)


if __name__ == '__main__':
    unittest.main()
