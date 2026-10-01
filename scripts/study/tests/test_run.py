import sys
import unittest
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parents[1]))
import run


class SharedApiEligibilityTests(unittest.TestCase):
    def test_fixed_only_method_is_excluded_without_behavior_observation(self):
        common = {'class': 'example.Target', 'constructor_types': '', 'method': 'common',
                  'parameter_types': 'int', 'dimensions': 3}
        added = {**common, 'method': 'addedByFix'}
        different_ctor = {**common, 'constructor_types': 'java.lang.String'}
        eligible, excluded = run.shared_targets([common, added, different_ctor], [common])
        self.assertEqual(eligible, [common])
        self.assertEqual(excluded, [added, different_ctor])


if __name__ == '__main__':
    unittest.main()
