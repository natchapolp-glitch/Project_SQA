import copy
import hashlib
from pathlib import Path
import shutil
import struct
import subprocess
import sys
import tarfile
import tempfile
import unittest

from scripts.study.api854.common import ROOT, implementation_hashes, read_json, sha256
from scripts.study.api854.verify_graphics_development import (
    CASES, OWNER, PREP, SOURCE, RECEIVER_SOURCE, parse_log, validate_records, validate_trace,
)

PROOF = ROOT / 'output/api854-20261003/graphics-development-v3'


class GraphicsDevelopmentTests(unittest.TestCase):
    def records(self, stage='fixed_first'):
        return parse_log((PROOF / (stage + '.stdout.log')).read_bytes())

    def test_sealed_sources_dependencies_and_runtime(self):
        receipt = read_json(PROOF / 'receipt.json')
        for path, expected in read_json(PROOF / 'checksums.json').items():
            self.assertEqual(sha256(PROOF / path), expected, path)
        self.assertEqual(receipt['runtime_source_sha256'], implementation_hashes())
        for path, value in receipt['shared_input_sha256'].items():
            self.assertEqual(sha256(ROOT / path), value)
        for name, value in receipt['suite_sha256'].items():
            self.assertEqual(sha256(PROOF / name), value)
        self.assertEqual(sha256(PROOF / 'preexecution-seal.json'), receipt['preexecution_seal_sha256'])
        for version, metadata in receipt['source_archives'].items():
            archive = PROOF / (version + '-production-source.tar.gz')
            self.assertEqual(sha256(archive), metadata['archive_sha256'])
            with tarfile.open(archive) as stream:
                sources = {m.name: stream.extractfile(m).read() for m in stream.getmembers() if m.name.endswith('.java')}
            if version == 'fixed':
                for name in [SOURCE, RECEIVER_SOURCE]:
                    retained = (PREP / 'fixed-source' / name).read_bytes()
                    self.assertEqual(sources[name].replace(b'\r\n', b'\n'), retained.replace(b'\r\n', b'\n'))
                    sources[name] = retained
            self.assertEqual(set(sources), set(receipt['compiled_source_sha256'][version]))
            for name, raw in sources.items():
                self.assertEqual(hashlib.sha256(raw).hexdigest(), receipt['compiled_source_sha256'][version][name])
            for name, value in receipt['dependencies_sha256'][version].items():
                self.assertEqual(sha256(PROOF / 'dependencies' / name), value)
        self.assertFalse(receipt['gate_a_passed'])
        self.assertFalse(receipt['shared_integration_approved'])
        self.assertEqual(receipt['primary_results_added'], 0)
        self.assertEqual(receipt['live_requests'], 0)

    def test_actual_raw_pixels_repeat_and_seven_inherited_targets_entered(self):
        receipt = read_json(PROOF / 'receipt.json')
        for version in ('fixed', 'buggy'):
            rows = []
            for suffix in ('first', 'second', 'method_entry_trace'):
                stage = version + '_' + suffix
                records = self.records(stage)
                rows.append(validate_records(records, PROOF / (stage + '-pixels'), allow_assertion_failures=version == 'buggy'))
                if suffix == 'method_entry_trace':
                    entries = validate_trace(records, receipt['stages'][stage]['exit_code'])
                    self.assertEqual(len(entries), 24)
                    self.assertEqual({r['class'] for r in entries}, {OWNER})
            self.assertEqual(rows[0], rows[1])
            self.assertEqual(rows[0], rows[2])
        self.assertEqual(receipt['cases_per_revision'], 24)
        self.assertEqual(receipt['exact_declarations_entered_per_revision'], 7)
        self.assertEqual(receipt['candidate_fault_detected'], bool(receipt['buggy_failed_cases']))
        self.assertEqual(receipt['temporary_shifted_line_mutation']['failed_cases'], ['domain_line_v'])

    def test_default_area_renderer_identity_cannot_be_replaced(self):
        records = self.records()
        records[0]['receiver_class'] = OWNER
        with self.assertRaisesRegex(ValueError, 'receiver identity'):
            validate_records(records, PROOF / 'fixed_first-pixels')

    def test_wrong_declaring_class_or_descriptor_is_not_exact_entry(self):
        records = self.records('fixed_method_entry_trace')
        entry = next(r for r in records if r.get('method_entry'))
        entry['descriptor'] = '()V'
        with self.assertRaisesRegex(ValueError, 'JVM descriptor'):
            validate_trace(records, 0)

    def test_fixture_failure_and_skipped_cases_rejected(self):
        records = self.records()
        records[0]['setup_succeeded'] = False
        with self.assertRaisesRegex(ValueError, 'fixture/invocation'):
            validate_records(records, PROOF / 'fixed_first-pixels')
        records = self.records()
        records[-1]['skipped'] = 1
        with self.assertRaisesRegex(ValueError, 'counters/skips'):
            validate_records(records, PROOF / 'fixed_first-pixels')

    def test_wrong_paint_cannot_pass_if_expected_and_actual_agree(self):
        records = self.records()
        records[0]['observation']['graphics']['paint_rgb'] = -16777216
        records[0]['expected_observation']['graphics']['paint_rgb'] = -16777216
        with self.assertRaisesRegex(ValueError, 'Independent graphics/state'):
            validate_records(records, PROOF / 'fixed_first-pixels')

    def test_coherent_wrong_background_pixels_rejected_by_analytic_oracle(self):
        records = self.records()
        with tempfile.TemporaryDirectory(dir=ROOT / 'output', prefix='.champ-graphics-negative-') as temporary:
            images = Path(temporary).resolve()
            self.assertTrue(images.is_relative_to(ROOT / 'output'))
            for path in (PROOF / 'fixed_first-pixels').iterdir():
                shutil.copyfile(path, images / path.name)
            name = 'background_v'
            pixels = tuple(-65536 if 10 <= x < 50 and 10 <= y < 50 else -1 for y in range(64) for x in range(64))
            raw = struct.pack('>4096i', *pixels)
            row = next(r for r in records if r.get('case') == name)
            for suffix, key in [('actual', 'observation'), ('reference', 'expected_observation')]:
                (images / (name + '.' + suffix + '.argb')).write_bytes(raw)
                row[key]['pixel_sha256'] = hashlib.sha256(raw).hexdigest()
            with self.assertRaisesRegex(ValueError, 'Analytic filled-region'):
                validate_records(records, images)

    def test_existing_sealed_output_rejected_without_writes(self):
        before = {p.relative_to(PROOF).as_posix(): sha256(p) for p in PROOF.rglob('*') if p.is_file()}
        result = subprocess.run([sys.executable, '-B', '-m', 'scripts.study.api854.verify_graphics_development',
                                 '--defects4j', '.', '--output', str(PROOF)], cwd=ROOT, capture_output=True)
        self.assertEqual(result.returncode, 2)
        self.assertIn(b'Evidence output already exists', result.stderr)
        self.assertEqual(before, {p.relative_to(PROOF).as_posix(): sha256(p) for p in PROOF.rglob('*') if p.is_file()})


if __name__ == '__main__':
    unittest.main()
