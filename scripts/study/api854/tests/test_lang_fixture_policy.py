import base64
import unittest

from scripts.study.api854.common import ROOT, read_json
from scripts.study.api854.fixture_policy import LANG_SIGNATURES, POLICY_V5, POLICY_V6, POLICY_V6_BUFFER, POLICY_V9_LANG, select
from scripts.study.api854.fixture_semantics import expected_exception


class LangFixturePolicyTests(unittest.TestCase):
    def inventory(self):
        root = ROOT / 'docs/api854/evidence/beam-aom-review-20261003'
        return [t for bug in read_json(root/'index.json')['bugs']
                for t in read_json(root/'declarations'/f"{bug['project']}-{bug['bug_id']}"/'targets.json')['targets']]

    def test_new_condition_preserves_both_parents_and_adds_only_two_lang_helpers(self):
        inventory = self.inventory()
        key = lambda t: tuple(t[k] for k in ('class', 'constructor_types', 'method', 'parameter_types'))
        before = set()
        for policy, count in [(POLICY_V5, 377), (POLICY_V6, 379), (POLICY_V6_BUFFER, 385)]:
            selected, excluded = select(inventory, policy)
            self.assertEqual(len(selected), count)
            self.assertEqual(len(selected)+len(excluded), 691)
            before |= {key(t) for t in selected}
        selected, excluded = select(inventory, POLICY_V9_LANG)
        self.assertEqual((len(selected), len(excluded)), (389, 302))
        self.assertEqual({key(t) for t in selected} - before, LANG_SIGNATURES)
        self.assertTrue(before <= {key(t) for t in selected})
        self.assertTrue(all(row['reason'] for row in excluded))

    def test_no_constructor_or_empty_enum_target_is_admitted(self):
        selected, excluded = select(self.inventory(), POLICY_V9_LANG)
        self.assertFalse(any(t['method'] in {'<init>', 'hashCode'} for t in selected))
        empty = [r for r in excluded if 'FromXmlParser$Feature' in r['target']['parameter_types']]
        self.assertEqual(len(empty), 4)

    def test_only_the_documented_validation_boundaries_are_expected(self):
        target = {'class': 'org.apache.commons.lang3.math.NumberUtils',
                  'method': 'validateArray', 'parameter_types': 'java.lang.Object'}
        for message, state in [('The Array must not be null', 'null'), ('Array cannot be empty.', '[I[]')]:
            outcome = ('exception:java.lang.IllegalArgumentException|message=java.lang.String:'
                       + base64.b64encode(message.encode()).decode() + '|state=validation-input:' + state)
            self.assertTrue(expected_exception(target, outcome, POLICY_V9_LANG))
            self.assertFalse(expected_exception(target, outcome, POLICY_V5))
            self.assertFalse(expected_exception({**target, 'method': 'max'}, outcome, POLICY_V9_LANG))
            self.assertFalse(expected_exception(target, outcome+'changed', POLICY_V9_LANG))
        self.assertFalse(expected_exception(target, 'exception:java.lang.IllegalArgumentException', POLICY_V9_LANG))
        self.assertFalse(expected_exception(target, 'exception:java.lang.NullPointerException', POLICY_V9_LANG))
