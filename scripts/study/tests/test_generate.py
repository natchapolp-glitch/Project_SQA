import sys
import unittest
from types import SimpleNamespace
from unittest.mock import patch
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parents[1]))
import generate

TARGETS = [{'class': 'example.Target', 'constructor_types': '', 'method': 'value',
            'parameter_types': 'int', 'dimensions': 3}]


class ProspectiveGeneratorTests(unittest.TestCase):
    def test_malformed_fixture_failure_remains_a_raw_protocol_error(self):
        result = SimpleNamespace(returncode=0, stdout='SQA_FIXTURE_FAILURE:???\n', stderr='')
        with patch.object(generate.subprocess, 'run', return_value=result):
            observed = generate.observe('fixture', TARGETS[0], [0, 0, 0], 10, 'explicit-policy')
        self.assertEqual(observed['status'], 'protocol_error')
        self.assertEqual(observed['stdout'], result.stdout)

    def test_cmaes_partial_population_never_exceeds_budget(self):
        calls = []
        def observer(target, vector):
            calls.append(vector)
            return {'status': 'ok', 'outcome': 'value:0'}
        rows = generate.generate(TARGETS, 'cmaes', 13, 101, observer)
        self.assertEqual(len(rows), 13)
        self.assertEqual(len(calls), 26)
        self.assertTrue(all(row['retained'] for row in rows))

    def test_nondiffering_fixed_cases_are_retained_without_buggy_access(self):
        observer = lambda target, vector: {'status': 'ok', 'outcome': 'value:constant'}
        rows = generate.generate(TARGETS, 'fscs-art', 5, 101, observer)
        self.assertEqual(sum(row['retained'] for row in rows), 5)
        self.assertEqual(generate.suite_source(rows).count('@Test'), 5)

    def test_unstable_and_harness_outcomes_remain_raw_but_not_tests(self):
        calls = [0]
        def observer(target, vector):
            calls[0] += 1
            return {'status': 'ok', 'outcome': str(calls[0])}
        rows = generate.generate(TARGETS, 'cmaes', 4, 101, observer)
        self.assertEqual(len(rows), 4)
        self.assertFalse(any(row['retained'] for row in rows))
        rows = generate.generate(TARGETS, 'fscs-art', 2, 101,
                                 lambda target, vector: {'status': 'harness_error'})
        self.assertFalse(any(row['retained'] for row in rows))

    def test_seed_reproduces_vectors(self):
        observer = lambda target, vector: {'status': 'ok', 'outcome': 'value:constant'}
        for method in ('cmaes', 'fscs-art'):
            self.assertEqual(generate.generate(TARGETS, method, 15, 7, observer),
                             generate.generate(TARGETS, method, 15, 7, observer))


if __name__ == '__main__':
    unittest.main()
