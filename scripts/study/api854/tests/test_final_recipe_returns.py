import copy
from pathlib import Path
import subprocess
import sys
import unittest

from scripts.study.api854.common import ROOT, implementation_hashes, read_json, sha256
from scripts.study.api854.review_final_recipe_returns import validate_lang

PROOF = ROOT / 'output/api854-20261003/champ-final-recipe-joint-return-v1'
PREVIOUS = ROOT / 'output/api854-20261003/champ-six-message-joint-review-v3'


class FinalRecipeReturnTests(unittest.TestCase):
    def inputs(self):
        beam = read_json(PROOF / 'received/beam-lang-verdict.json')
        previous = read_json(PREVIOUS / 'champ-lang-verdict.json')
        rows = [r for candidate in beam['candidates'] for r in candidate['fixed_repeated_observations']]
        return beam, previous, rows

    def test_sealed_hashes_and_runtime_bindings(self):
        for path, expected in read_json(PROOF / 'checksums.json').items():
            self.assertEqual(sha256(PROOF / path), expected, path)
        receipt = read_json(PROOF / 'receipt.json')
        self.assertEqual(receipt['champ_runtime_source_sha256'], implementation_hashes())
        self.assertEqual(receipt['checkpoint_pins_verified'], 100)
        self.assertEqual(receipt['aom_buffer_review']['independently_checked_cases'], 42)
        self.assertEqual(receipt['aom_buffer_review']['fixed_observations'], 84)
        self.assertEqual(receipt['real_kku_requests'], 0)
        self.assertFalse(receipt['runtime_modified'])

    def test_joint_components_do_not_grant_final_integration_or_live_authority(self):
        lang = read_json(PROOF / 'champ-lang-joint-verdict.json')
        self.assertTrue(lang['joint_acceptance_complete'])
        self.assertTrue(all(r['beam_joint_verdict'] and r['champ_joint_verdict'] for r in lang['candidates']))
        self.assertTrue(all(not r['accepted_into_shared_inputs'] for r in lang['candidates']))
        buffer = read_json(PROOF / 'champ-buffer-csv-joint-verdict.json')
        self.assertTrue(buffer['joint_acceptance'])
        self.assertIsNotNone(buffer['csv_stream_condition_change']['agreed_condition'])
        self.assertEqual(buffer['historical_cmaes_string_append_entry_hits'], 0)
        chronology = read_json(PROOF / 'champ-chronology-joint-verdict.json')
        self.assertTrue(chronology['joint_bounded_candidate_acceptance_complete'])
        self.assertFalse(chronology['shared_integration_approved'])
        self.assertEqual(len(chronology['exact_targets']), 6)
        self.assertIn('UTCProvider', chronology['agreed_preconditions']['chronology'])
        self.assertIn('skips validation', chronology['agreed_preconditions']['internal_constructor'])
        for packet in (lang, buffer, chronology):
            self.assertFalse(packet['gate_a_approved'])
            self.assertFalse(packet['pilot_authorized'])
            self.assertIsNone(packet['final_prompt_reserve'])
        union = read_json(PROOF / 'proposed-union.json')
        self.assertEqual(union['actual_selected'], 380)
        self.assertEqual(union['buffer_and_lang_only'], {'selected': 390, 'unsupported': 301})
        self.assertEqual(union['including_chronology_after_shared_invocation_and_oracle_integration'],
                         {'selected': 396, 'unsupported': 295})
        self.assertFalse(union['implemented'])

    def test_wrong_private_helper_overload_rejected(self):
        beam, previous, rows = self.inputs()
        beam['candidates'][1]['exact_jvm_descriptor'] = '([I)V'
        with self.assertRaisesRegex(ValueError, 'descriptor'):
            validate_lang(beam, previous, rows)

    def test_missing_explicit_beam_verdict_rejected(self):
        beam, previous, rows = self.inputs()
        beam['candidates'][0]['beam_joint_verdict'] = None
        with self.assertRaisesRegex(ValueError, 'explicit scoped Beam verdict'):
            validate_lang(beam, previous, rows)

    def test_poisoned_exception_message_rejected_even_when_both_runs_agree(self):
        beam, previous, rows = self.inputs()
        row = rows[8]
        row['declared_input']['expected_message'] = 'Array is missing'
        row['expected'] = 'exception:java.lang.IllegalArgumentException'
        row['fixed_first']['outcome'] = row['fixed_second']['outcome'] = row['expected']
        with self.assertRaisesRegex(ValueError, 'exception message'):
            validate_lang(beam, previous, rows)

    def test_bare_void_state_oracle_rejected(self):
        beam, previous, rows = self.inputs()
        row = rows[10]
        row['expected'] = 'void|state=stateless-scalars'
        row['fixed_first']['outcome'] = row['fixed_second']['outcome'] = row['expected']
        with self.assertRaisesRegex(ValueError, 'value/state oracle'):
            validate_lang(beam, previous, rows)

    def test_target_not_invoked_cannot_count_as_success(self):
        beam, previous, rows = self.inputs()
        rows[0]['fixed_first']['target_invoked'] = rows[0]['fixed_second']['target_invoked'] = False
        with self.assertRaisesRegex(ValueError, 'invocation'):
            validate_lang(beam, previous, rows)

    def test_existing_output_rejected_without_altering_evidence(self):
        before = {p.relative_to(PROOF).as_posix(): sha256(p) for p in PROOF.rglob('*') if p.is_file()}
        result = subprocess.run([sys.executable, '-B', '-m', 'scripts.study.api854.review_final_recipe_returns',
                                 '--output', str(PROOF)], cwd=ROOT, capture_output=True)
        self.assertEqual(result.returncode, 2)
        self.assertIn(b'Evidence output already exists', result.stderr)
        self.assertEqual(before, {p.relative_to(PROOF).as_posix(): sha256(p) for p in PROOF.rglob('*') if p.is_file()})


if __name__ == '__main__':
    unittest.main()
