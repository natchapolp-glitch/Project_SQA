from pathlib import Path
import json
import shutil
import tempfile
import unittest
from scripts.study.api854.export_eligibility import convert_record, DISCOVERY_POLICY

PACKET = Path(__file__).resolve().parents[4] / 'docs/api854/evidence/beam-aom-review-20261003'


class EligibilityImportTests(unittest.TestCase):
    def setUp(self):
        self.temp = tempfile.TemporaryDirectory()
        self.addCleanup(self.temp.cleanup)
        self.root = Path(self.temp.name)
        (self.root / 'review').mkdir()
        shutil.copyfile(PACKET / 'review/Closure-176.json', self.root / 'review/Closure-176.json')
        shutil.copytree(PACKET / 'declarations/Closure-176', self.root / 'declarations/Closure-176', copy_function=shutil.copyfile)
        self.row = {'project': 'Closure', 'bug_id': 176, 'owner': 'beam'}

    def test_retained_discovery_is_not_relabelled_as_runtime_oracle_approval(self):
        result = convert_record(self.root, self.row)
        self.assertEqual(result['fixture_policy'], DISCOVERY_POLICY)
        self.assertEqual(len(result['targets']), 50)
        self.assertFalse(result['usable'])
        self.assertFalse(result['fixture_oracle_approval'])
        self.assertTrue(all(set(t) == {'class', 'constructor_types', 'method', 'parameter_types'} for t in result['targets']))

    def test_changed_buggy_declarations_are_rejected(self):
        path = self.root / 'declarations/Closure-176/targets.buggy.json'
        data = json.loads(path.read_text())
        data['targets'].pop()
        path.write_text(json.dumps(data))
        with self.assertRaisesRegex(ValueError, 'signature sets'):
            convert_record(self.root, self.row)

    def test_owner_identity_cannot_be_swapped(self):
        with self.assertRaisesRegex(ValueError, 'Pilot identity'):
            convert_record(self.root, {**self.row, 'owner': 'aom'})
