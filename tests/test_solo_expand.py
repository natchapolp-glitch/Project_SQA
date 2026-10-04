from pathlib import Path
import sys
import unittest
sys.path.insert(0, str(Path(__file__).resolve().parents[1] / 'scripts/study'))
from solo_expand_500 import choose_alias


class AdmissionTest(unittest.TestCase):
    def test_unknown_or_nonfinite_quota_is_not_an_observation(self):
        for observations in ({}, {('a01', 'kku-claude'): {'remaining': float('nan')}},
                             {('a01', 'kku-claude'): {'remaining': float('inf')}}):
            self.assertIsNone(choose_alias(['a01'], ['kku-claude', 'kku-gemini'], observations))
    def test_low_quota_alias_is_not_rotated_into_a_job(self):
        observations = {('a01', 'kku-claude'): {'remaining': 10000},
                        ('a02', 'kku-claude'): {'remaining': 100000},
                        ('a02', 'kku-gemini'): {'remaining': 150000}}
        self.assertEqual(choose_alias(['a01', 'a02'], ['kku-claude', 'kku-gemini'], observations), 'a02')
        observations['a02', 'kku-gemini']['remaining'] = 1000
        self.assertIsNone(choose_alias(['a01', 'a02'], ['kku-claude', 'kku-gemini'], observations))


if __name__ == '__main__':
    unittest.main()
