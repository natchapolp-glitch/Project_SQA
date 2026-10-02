from pathlib import Path
import tempfile
import unittest

from scripts.study.api854.prepare_worker import CONTEXT_POLICY, PROMPT_POLICY, export_prompt
from scripts.study.api854.common import sha256


class PreparePromptTests(unittest.TestCase):
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
