"""Exercise the five-bug contract through the same CPU/API/evaluator boundary."""
import json
from unittest.mock import patch

from . import test_preparation_v4 as base
from scripts.study.api854 import preparation, fixture_policy


class SharedV5Tests(base.SharedV4Tests):
    def setUp(self):
        for name, value in [('POLICY_V4', preparation.POLICY_V5),
                            ('POLICY', fixture_policy.POLICY_V4),
                            ('recipe_document', lambda hashes: fixture_policy.recipe_document(hashes, fixture_policy.POLICY_V4))]:
            item = patch.object(base, name, value)
            item.start()
            self.addCleanup(item.stop)

    def test_five_bug_scope_does_not_admit_unreviewed_bugs(self):
        self.assertEqual(preparation.explicit_scope(preparation.POLICY_V5),
            {('Closure',176),('JxPath',1),('Codec',1),('Collections',1),('Csv',1)})
        self.assertNotIn(('Codec',1), preparation.explicit_scope(preparation.POLICY_V4))
        f = self.setup_fixture()
        manifest = dict(f.manifest, project='Closure', bug_id=1, revision='1f')
        with self.assertRaisesRegex(ValueError, 'restricted'):
            preparation.validate(manifest, f.prepare_metadata,
                (f.prepared/'prompt.md').read_bytes(), (f.prepared/'targets.json').read_bytes(),
                (f.prepared/'prepare-policy.json').read_bytes(),
                fixture_recipe=json.loads((f.prepared/'fixture-recipes.json').read_bytes()))

    def test_recipe_label_must_match_contract_even_when_bytes_hashes_match(self):
        f = self.setup_fixture()
        recipe = json.loads((f.prepared/'fixture-recipes.json').read_bytes())
        recipe['fixture_policy_id'] = fixture_policy.POLICY
        with self.assertRaisesRegex(ValueError, 'recipe source bytes/hash'):
            fixture_policy.validate_recipe(recipe, policy=fixture_policy.POLICY_V4)

    def test_unreviewed_bug_cannot_reuse_two_bug_proposal(self):
        self.test_five_bug_scope_does_not_admit_unreviewed_bugs()
