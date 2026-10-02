from pathlib import Path
import tempfile
import unittest
from unittest.mock import patch

from scripts.study.api854.prepare_worker import CONTEXT_POLICY, PROMPT_POLICY, EXPLICIT_PROMPT_POLICY, export_prompt, execute
from scripts.study.api854.common import sha256, read_json, implementation_hashes
from scripts.study.api854.fixture_policy import POLICY


class PreparePromptTests(unittest.TestCase):
    def test_explicit_worker_exports_recipe_instead_of_shared_legacy_prompt(self):
        from scripts.study.api854.configuration import proposal
        import json
        with tempfile.TemporaryDirectory() as directory:
            root = Path(directory)
            fixed = root / 'fixed'
            source = 'src/com/google/javascript/jscomp/TypeInference.java'
            (fixed / source).parent.mkdir(parents=True)
            (fixed / source).write_bytes(b'package com.google.javascript.jscomp; public class TypeInference {}')
            (fixed / '.defects4j.config').write_bytes(b'pid=Closure\nvid=176f\n')
            protocol = proposal()
            protocol.update(context_selection=CONTEXT_POLICY, fixture_policy_id=POLICY,
                generation={'context_policy_id':CONTEXT_POLICY, 'prompt_policy_id':EXPLICIT_PROMPT_POLICY,
                            'fixture_policy_id':POLICY})
            target = {'class':'com.google.javascript.jscomp.TypeInference', 'constructor_types':'',
                'method':'getBooleanOutcomes', 'parameter_types':'boolean'}
            job = {'run_id':'beam-development-prepare-merge', 'protocol_hash':'a'*64, 'project':'Closure',
                   'bug_id':176, 'approach':'kku-claude', 'attempt_id':'fixture-prepare'}
            def adapter(d4j, job, trees, output, timeout, **options):
                self.assertEqual(options['fixture_policy'], POLICY)
                output.mkdir()
                (output / 'dir.src.classes.txt').write_text('src')
                (output / 'classes.modified.txt').write_text(target['class'])
                (output / 'targets.fixture-policy.json').write_text(json.dumps({'targets':[target]}))
                return {'fixed_worktree':str(fixed), 'classes_file':str(output/'classes.modified.txt'),
                    'targets_sha256':sha256(output/'targets.fixture-policy.json'),
                    'targets_file':str(output/'targets.fixture-policy.json')}, [target]
            with patch('scripts.study.api854.prepare_worker.inspect_environment', return_value={'ready':True}), \
                    patch('scripts.study.api854.prepare_worker.prepare_adapter', side_effect=adapter):
                result = execute(job, protocol, root/'results', root/'trees', 'offline-only')
            self.assertEqual(result['observed_outcome'], 'prepared', result.get('error'))
            self.assertIsNone(result['preparation'])
            self.assertEqual(result['prompt']['prompt_policy_id'], EXPLICIT_PROMPT_POLICY)
            self.assertEqual(result['prompt']['fixture_policy_id'], POLICY)
            recipe = root/'results'/job['run_id']/job['protocol_hash']/'Closure'/'176'/'kku-claude'/job['attempt_id']/'prepare/context/fixture-recipes.json'
            self.assertEqual(sha256(recipe), result['prompt']['fixture_recipes_sha256'])

    def test_shared_v3_cannot_be_silently_relabelled_as_explicit_fixture_policy(self):
        with tempfile.TemporaryDirectory() as directory:
            root = Path(directory)
            protocol = {'fixture_policy_id': POLICY, 'generation': {'prepare_contract': 'aom-beam-prepare-v3'}}
            with self.assertRaisesRegex(ValueError, 'new shared preparation contract'):
                execute({}, protocol, root / 'results', root / 'trees', 'not-called')
            self.assertFalse((root / 'results').exists())

    def test_execute_explicit_recipe_uses_versioned_prompt_export(self):
        with tempfile.TemporaryDirectory() as directory:
            root = Path(directory)
            (root / 'setup').mkdir()
            (root / 'fixed').mkdir()
            (root / 'setup/dir.src.classes.txt').write_text('src')
            (root / 'setup/classes.txt').write_text('example.Target')
            (root / 'context').mkdir()
            (root / 'context/context.md').write_text('fixed-source unit fixture only')
            protocol = {'context_selection': CONTEXT_POLICY, 'test_method_cap': 30,
                'fixture_policy_id': POLICY, 'source_sha256': implementation_hashes(), 'command_timeout_seconds': 1,
                'generation': {'context_policy_id': CONTEXT_POLICY, 'prompt_policy_id': EXPLICIT_PROMPT_POLICY,
                    'fixture_policy_id': POLICY}}
            targets = [{'class': 'com.google.javascript.jscomp.TypeInference', 'constructor_types': '',
                'method': 'getBooleanOutcomes', 'parameter_types':
                    'com.google.javascript.rhino.jstype.BooleanLiteralSet,com.google.javascript.rhino.jstype.BooleanLiteralSet,boolean'}]
            prepared = {'classes_file': str(root / 'setup/classes.txt'), 'fixed_worktree': str(root / 'fixed'),
                        'targets_sha256': 'a' * 64}
            prefix = 'scripts.study.api854.prepare_worker.'
            with patch(prefix + 'start_attempt', return_value=root), patch(prefix + 'snapshot_implementation'), \
                    patch(prefix + 'inspect_environment', return_value={'ready': True}), \
                    patch(prefix + 'new_worktrees', return_value=root / 'trees'), \
                    patch(prefix + 'prepare_adapter', return_value=(prepared, targets)), \
                    patch(prefix + 'fixed_sources', return_value={'src/Target.java': 'b' * 64}), \
                    patch(prefix + 'export_context', return_value={'source_hash': 'c' * 64}), \
                    patch(prefix + 'compose', side_effect=AssertionError('legacy composition must not receive explicit fixtures')):
                result = execute({'project': 'Closure', 'bug_id': 176}, protocol, root, root / 'trees', 'not-called')
            self.assertEqual(result['observed_outcome'], 'prepared')
            self.assertEqual(result['prompt']['fixture_policy_id'], POLICY)
            self.assertTrue((root / 'context/fixture-recipes.json').exists())
    def test_explicit_recipes_are_hash_bound_and_shared_with_ai_inputs(self):
        with tempfile.TemporaryDirectory() as directory:
            root = Path(directory)
            (root / 'context.md').write_text('fixed source only', encoding='utf-8')
            protocol = {'test_method_cap': 30, 'fixture_policy_id': POLICY, 'source_sha256': implementation_hashes(),
                'generation': {'context_policy_id': CONTEXT_POLICY, 'prompt_policy_id': EXPLICIT_PROMPT_POLICY, 'fixture_policy_id': POLICY}}
            targets = [{'class': 'com.google.javascript.jscomp.TypeInference', 'constructor_types': '',
                'method': 'getBooleanOutcomes', 'parameter_types': 'com.google.javascript.rhino.jstype.BooleanLiteralSet,com.google.javascript.rhino.jstype.BooleanLiteralSet,boolean'}]
            metadata = export_prompt({'project': 'Closure', 'bug_id': 176}, protocol, targets, root)
            recipe = read_json(root / 'fixture-recipes.json')
            self.assertEqual(metadata['fixture_recipes_sha256'], sha256(root / 'fixture-recipes.json'))
            self.assertEqual(recipe['fixture_policy_id'], POLICY)
            self.assertTrue(all(recipe['source_sha256'][name] == protocol['source_sha256'][name] for name in recipe['sources']))
            self.assertIn('Same fixture construction/projection knowledge', (root / 'prompt.md').read_text())

    def test_explicit_fixtures_cannot_reuse_the_old_prompt_policy(self):
        with tempfile.TemporaryDirectory() as directory:
            protocol = {'test_method_cap': 30, 'fixture_policy_id': POLICY,
                'generation': {'context_policy_id': CONTEXT_POLICY, 'prompt_policy_id': PROMPT_POLICY}}
            with self.assertRaisesRegex(ValueError, 'policies differ'):
                export_prompt({'project': 'Closure', 'bug_id': 176}, protocol, [], Path(directory))
    def test_core_preflight_exports_no_unapproved_prompt(self):
        with tempfile.TemporaryDirectory() as directory:
            root = Path(directory)
            self.assertIsNone(export_prompt({'project': 'Lang', 'bug_id': 4}, {'test_method_cap': 30}, [], root))
            self.assertFalse((root / 'prompt.md').exists())

    def test_prompt_pins_policy_and_contains_only_fixed_context_and_eligible_targets(self):
        with tempfile.TemporaryDirectory() as directory:
            root = Path(directory)
            (root / 'context.md').write_text('fixed source bytes', encoding='utf-8')
            protocol = {'test_method_cap': 30, 'generation': {
                'context_policy_id': CONTEXT_POLICY, 'prompt_policy_id': PROMPT_POLICY}}
            targets = [{'class': 'example.Target', 'method': 'target', 'parameter_types': 'int'}]
            result = export_prompt({'project': 'Lang', 'bug_id': 4}, protocol, targets, root)
            text = (root / 'prompt.md').read_text(encoding='utf-8')
            self.assertIn('fixed source bytes', text)
            self.assertIn('example.Target', text)
            self.assertIn('at most 30 @Test', text)
            self.assertEqual(result['prompt_sha256'], sha256(root / 'prompt.md'))
            self.assertEqual(result['prompt_utf8_bytes'], len(text.encode()))
            with self.assertRaises(FileExistsError):
                export_prompt({'project': 'Lang', 'bug_id': 4}, protocol, targets, root)
