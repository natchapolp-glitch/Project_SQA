from pathlib import Path
import sys
import unittest

sys.path.insert(0, str(Path(__file__).resolve().parents[1] / 'scripts/study'))
import solo_scope as scope


class ScopeTest(unittest.TestCase):
    def test_round_robin_covers_projects_and_has_no_duplicate(self):
        inventory = {'Z': [1, 3], 'A': [2, 4, 5], 'B': [7]}
        self.assertEqual(scope.select_cases(inventory, 5), ['A-2', 'B-7', 'Z-1', 'A-4', 'Z-3'])
        self.assertEqual(len(set(scope.select_cases(inventory, 6))), 6)

    def test_invalid_size_rejected(self):
        for size in (0, -1, 3):
            with self.assertRaises(ValueError):
                scope.select_cases({'A': [1, 2]}, size)


if __name__ == '__main__':
    unittest.main()
