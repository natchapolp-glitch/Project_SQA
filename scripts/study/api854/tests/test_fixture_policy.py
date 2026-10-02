import unittest
from scripts.study.api854.fixture_policy import POLICY, select


def target(method='traverseName', params='com.google.javascript.rhino.Node,com.google.javascript.jscomp.type.FlowScope'):
    return {'class': 'com.google.javascript.jscomp.TypeInference', 'constructor_types':
            'com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,'
            'com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map',
            'method': method, 'parameter_types': params, 'dimensions': 21}


class FixturePolicyTests(unittest.TestCase):
    def test_selection_is_declaration_based_and_preserves_every_exclusion(self):
        inputs = [target(), target('updateBind'), target('getBooleanOutcomePair', 'unknown.Type'), target('<init>', '')]
        kept, excluded = select(inputs, POLICY)
        self.assertEqual(kept, [inputs[0]])
        self.assertEqual([row['target'] for row in excluded], inputs[1:])
        self.assertTrue(all(row['reason'] for row in excluded))

    def test_no_unreviewed_project_can_silently_use_generic_null_fixtures(self):
        candidate = {**target(), 'class': 'unknown.Project'}
        kept, excluded = select([candidate], POLICY)
        self.assertFalse(kept)
        self.assertEqual(excluded[0]['reason'], 'explicit_project_recipe_not_reviewed')

    def test_legacy_targets_unchanged_and_unknown_policy_rejected(self):
        inputs = [target()]
        self.assertEqual(select(inputs, None), (inputs, []))
        with self.assertRaises(ValueError):
            select(inputs, 'wrong-policy')
