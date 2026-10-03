"""Prevent partial calibration evidence from becoming live-run approval."""
import copy
import unittest

from scripts.study.api854.common import ROOT, read_json
from scripts.study.api854.review_champ_preflight import RECEIPT, observations


class PreflightEvidenceTests(unittest.TestCase):
    def setUp(self):
        self.receipt = copy.deepcopy(read_json(ROOT / RECEIPT))

    def test_received_partial_evidence_remains_historical_and_not_ledger_ready(self):
        rows = observations(self.receipt)
        self.assertEqual(sum(r['reported_usage']['total_tokens'] for r in rows), 60)
        self.assertTrue(all(not r['ledger_import_authorized'] and not r['remaining_is_current_verified'] for r in rows))

    def test_duplicate_model_cannot_replace_second_model(self):
        self.receipt['calibration_observations'][1] = copy.deepcopy(self.receipt['calibration_observations'][0])
        with self.assertRaisesRegex(ValueError, 'two selected models'):
            observations(self.receipt)

    def test_quota_remaining_cannot_be_inflated(self):
        self.receipt['calibration_observations'][0]['response']['model_quota']['daily_remaining_tokens'] += 1
        with self.assertRaisesRegex(ValueError, 'Quota arithmetic'):
            observations(self.receipt)

    def test_requested_settings_cannot_be_relabelled_as_effective_verified(self):
        self.receipt['calibration_observations'][0]['effective_backend_temperature_verified'] = True
        with self.assertRaisesRegex(ValueError, 'effective settings'):
            observations(self.receipt)

    def test_receipt_cannot_enable_live_pilot(self):
        self.receipt['live_pilot_authorized'] = True
        with self.assertRaisesRegex(ValueError, 'gate/reserve claim'):
            observations(self.receipt)


if __name__ == '__main__':
    unittest.main()
