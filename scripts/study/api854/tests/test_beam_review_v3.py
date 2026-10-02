from pathlib import Path
import tempfile
import unittest

from scripts.study.api854.beam_review_v3 import inspect, checked_files
from scripts.study.api854.common import ROOT, write_json, sha256


class BeamSharedV3ReviewTests(unittest.TestCase):
    def test_real_imported_packet_matches_beam_discovery_and_one_host_routing(self):
        result = inspect(ROOT / 'output/api854-20261003/prepare-v3',
            ROOT / 'docs/api854/evidence/beam-aom-review-20261003',
            ROOT / 'experiments/configs/api854-20261003/runner-plan.beam-one-host.v2.json',
            ROOT / 'experiments/configs/api854-20261003/ownership.json')
        self.assertEqual((result['bugs_reviewed'], result['target_count'], result['fixed_only_exclusion_count']), (20,691,3))
        self.assertFalse(result['gate_a_approved'])
        self.assertFalse(any(r['shared_v3_semantic_approved'] for r in result['records']))

    def test_checksum_cannot_escape_review_packet_even_with_valid_hash(self):
        with tempfile.TemporaryDirectory() as directory:
            root = Path(directory)
            folder = root / 'packet'
            folder.mkdir()
            outside = root / 'outside.txt'
            outside.write_text('valid bytes outside the selected packet')
            write_json(folder / 'checksums.json', {'../outside.txt': sha256(outside)})
            with self.assertRaisesRegex(ValueError, 'containment'):
                checked_files(folder)
