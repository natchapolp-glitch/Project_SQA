"""Protect scoped peer verdicts against misleading oracle/coverage evidence."""
import copy
import subprocess
import sys
import unittest

from scripts.study.api854.common import ROOT, implementation_hashes, read_json, sha256
from scripts.study.api854.review_joint_recipe_intake import method_entry, validate_buffer_observations

PROOF = ROOT/'output/api854-20261003/champ-six-message-joint-review-v3'


class JointRecipeIntakeTests(unittest.TestCase):
    def csv_rows(self):
        verdict = read_json(PROOF/'champ-buffer-verdict.json')
        rows = copy.deepcopy(next(c['fixed_repeated_observations'] for c in verdict['candidates']
                                  if c['target']['project'] == 'Csv'))
        for i,row in enumerate(rows):
            row['case_id'] = i
        return rows

    def test_sealed_packet_hashes_and_runtime_remain_valid(self):
        hashes = read_json(PROOF/'checksums.json')
        actual = {p.relative_to(PROOF).as_posix() for p in PROOF.rglob('*') if p.is_file()}
        self.assertEqual(actual, set(hashes)|{'checksums.json'})
        for path,value in hashes.items():
            self.assertEqual(sha256(PROOF/path), value, path)
        receipt = read_json(PROOF/'receipt.json')
        self.assertEqual(implementation_hashes(), receipt['runtime_source_sha256'])
        self.assertEqual((receipt['shared_selected'],receipt['shared_unsupported'],receipt['denominator']), (380,311,691))
        self.assertFalse(receipt['gate_a_approved'])
        self.assertEqual(receipt['primary_results_added'], 0)
        buffer = read_json(PROOF/'champ-buffer-verdict.json')
        self.assertEqual(buffer['current_runtime_source_sha256'], receipt['runtime_source_sha256'])
        self.assertEqual(buffer['reviewer_runtime_commit'], receipt['base_champ_commit'])
        self.assertEqual(buffer['champ_producer_sha256'], receipt['reviewer_source_sha256'])
        self.assertNotEqual(buffer['received_beam_current_runtime_source_sha256'],buffer['current_runtime_source_sha256'])
        self.assertTrue(buffer['new_shared_preparation_authorized_by_receipt'])
        self.assertFalse(buffer['gate_a_approved'])

    def test_reference_cannot_relabel_zero_cmaes_sampled_coverage(self):
        rows = read_json(PROOF/'receipt.json')['buffer_reference_target_coverage']
        row = next(r for r in rows if r['target']['class'].endswith('TextBuffer')
                   and r['target']['parameter_types']=='java.lang.String,int,int')
        self.assertEqual({r['approach']:r['hits'] for r in row['historical']}, {'fscs-art':2,'cmaes':0})
        self.assertGreater(row['reference']['entry_hits'], 0)

    def test_declared_and_observed_wrong_sentinel_cannot_pass_together(self):
        rows = self.csv_rows()
        validate_buffer_observations(rows,4)
        row = rows[0]
        wrong = row['expected'].replace('java.lang.Character:fg==;', 'java.lang.Character:WA==;',1)
        row['expected'] = row['fixed_first']['outcome'] = row['fixed_second']['outcome'] = wrong
        with self.assertRaisesRegex(ValueError, 'Independent Buffer/Csv oracle'):
            validate_buffer_observations(rows,4)

    def test_uninvoked_target_and_unstable_observation_are_rejected(self):
        rows = self.csv_rows()
        rows[0]['fixed_first']['target_invoked'] = rows[0]['fixed_second']['target_invoked'] = False
        with self.assertRaisesRegex(ValueError, 'setup/invocation/repeat'):
            validate_buffer_observations(rows,4)
        rows = self.csv_rows()
        rows[0]['fixed_second']['outcome'] += 'changed'
        with self.assertRaisesRegex(ValueError, 'setup/invocation/repeat'):
            validate_buffer_observations(rows,4)

    def test_wrong_overload_does_not_count_as_exact_coverage(self):
        class Objects:
            def blob(self, unused):
                return b'<coverage><class name="C"><methods><method name="append" signature="([CII)V"><lines><line number="1" hits="3"/></lines></method></methods></class></coverage>'
        with self.assertRaisesRegex(ValueError, 'Exact coverage descriptor'):
            method_entry(Objects(),'unused',{'class':'C','method':'append'},'(Ljava/lang/String;II)V')

    def test_champ_verdict_does_not_forge_beam_lang_or_final_reserve(self):
        lang = read_json(PROOF/'champ-lang-verdict.json')
        self.assertTrue(all(r['champ_joint_verdict'].startswith('accepted_') for r in lang['candidates']))
        self.assertTrue(all(r['beam_joint_verdict'] is None for r in lang['candidates']))
        self.assertFalse(lang['joint_acceptance_complete'])
        reserve = read_json(PROOF/'reserve-readiness.json')
        self.assertIsNone(reserve['final_prompt_reserve'])
        self.assertFalse(reserve['final_40_pair_worksheet_created'])
        self.assertFalse(reserve['live_ai_test_authorized'])

    def test_existing_packet_is_rejected_without_appending_failure_files(self):
        before = {p.relative_to(PROOF).as_posix():sha256(p) for p in PROOF.rglob('*') if p.is_file()}
        result = subprocess.run([sys.executable,'-m','scripts.study.api854.review_joint_recipe_intake',
                                 '--output',str(PROOF)],cwd=ROOT,capture_output=True)
        self.assertEqual(result.returncode,2)
        self.assertIn(b'Evidence output already exists', result.stderr)
        self.assertEqual(before,{p.relative_to(PROOF).as_posix():sha256(p) for p in PROOF.rglob('*') if p.is_file()})


if __name__ == '__main__':
    unittest.main()
