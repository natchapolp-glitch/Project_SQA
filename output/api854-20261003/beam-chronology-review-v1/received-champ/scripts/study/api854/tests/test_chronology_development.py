"""Reject misleading execution/oracle/coverage evidence and protect sealed packets."""
import copy
import subprocess
import sys
import unittest

from scripts.study.api854.common import ROOT, read_json, sha256
from scripts.study.api854.verify_chronology_development import (
    parse_log, validate_observations, validate_trace,
)

PROOF = ROOT/'output/api854-20261003/chronology-development-v2'


class ChronologyDevelopmentTests(unittest.TestCase):
    def records(self, name='fixed_method_entry_trace'):
        return parse_log((PROOF/(name+'.stdout.log')).read_bytes())

    def test_received_packet_checksums_and_both_revision_evidence(self):
        checks = read_json(PROOF/'checksums.json')
        self.assertEqual({p.name for p in PROOF.iterdir() if p.is_file()}, set(checks)|{'checksums.json'})
        for name, digest in checks.items():
            self.assertEqual(sha256(PROOF/name), digest, name)
        receipt = read_json(PROOF/'receipt.json')
        self.assertEqual(receipt['status'], 'pass')
        for version in ('fixed','buggy'):
            first = validate_observations(self.records(version+'_first'), fixed=(version == 'fixed'))
            self.assertEqual(first, validate_observations(self.records(version+'_second'), fixed=(version == 'fixed')))
            trace = self.records(version+'_method_entry_trace')
            self.assertEqual(first, validate_observations(trace, fixed=(version == 'fixed')))
            self.assertEqual(len(validate_trace(trace, receipt['stages'][version+'_first']['exit_code'])), 13)
        self.assertFalse(receipt['oracle_approved'])
        self.assertFalse(receipt['shared_preparation_modified'])
        self.assertEqual((receipt['selected'],receipt['unsupported'],receipt['denominator']), (380,311,691))

    def test_another_constructor_descriptor_is_not_target_coverage(self):
        records = self.records()
        entry = next(r for r in records if r.get('method_entry') and r['case'] == 'internal_iso')
        entry['descriptor'] = '([Lorg/joda/time/DateTimeFieldType;[ILorg/joda/time/Chronology;)V'
        with self.assertRaisesRegex(ValueError, 'Exact target descriptor'):
            validate_trace(records)

    def test_weakened_assertion_cannot_accept_wrong_chronology_value(self):
        records = self.records()
        row = next(r for r in records if r.get('case') == 'getfield_buddhist' and 'observation' in r)
        row['observation']['epoch_year'] = 1970
        with self.assertRaisesRegex(ValueError, 'Independent value/state oracle'):
            validate_observations(records)

    def test_setup_failure_cannot_be_counted_as_execution(self):
        records = self.records()
        next(r for r in records if 'observation' in r)['setup_succeeded'] = False
        with self.assertRaisesRegex(ValueError, 'Setup failure'):
            validate_observations(records)

    def test_skipped_case_cannot_be_accepted(self):
        records = self.records()
        next(r for r in records if r.get('summary'))['skipped'] = 1
        with self.assertRaisesRegex(ValueError, 'Execution counters'):
            validate_observations(records)

    def test_unexpected_buggy_runtime_error_is_not_an_assertion_failure(self):
        records = self.records('buggy_first')
        row = next(r for r in records if 'observation' in r and not r['target_check_passed'])
        row['failure_class'] = 'java.lang.NullPointerException'
        with self.assertRaisesRegex(ValueError, 'Unexpected runtime/fixture failure'):
            validate_observations(records, fixed=False)

    def test_duplicate_case_is_rejected(self):
        records = self.records()
        records.append(copy.deepcopy(next(r for r in records if 'observation' in r)))
        with self.assertRaisesRegex(ValueError, 'Case inventory/order'):
            validate_observations(records)

    def test_existing_sealed_output_is_rejected_without_any_writes(self):
        before = {p.name:sha256(p) for p in PROOF.iterdir() if p.is_file()}
        result = subprocess.run([sys.executable,'-m','scripts.study.api854.verify_chronology_development',
                                 '--defects4j','unused','--output',str(PROOF)],cwd=ROOT,capture_output=True)
        self.assertEqual(result.returncode, 2)
        self.assertIn(b'Evidence output already exists', result.stderr)
        self.assertEqual({p.name:sha256(p) for p in PROOF.iterdir() if p.is_file()}, before)


if __name__ == '__main__':
    unittest.main()
