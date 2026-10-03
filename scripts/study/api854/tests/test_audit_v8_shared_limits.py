"""Retained v8 inputs must reject the later composed runtime."""
import unittest
from scripts.study.api854.audit_v8_shared_limits import inspect

class RetainedV8Tests(unittest.TestCase):
    def test_retained_v8_does_not_certify_changed_runtime(self):
        with self.assertRaisesRegex(ValueError, 'runtime pins differ'):
            inspect()

if __name__ == '__main__':
    unittest.main()
