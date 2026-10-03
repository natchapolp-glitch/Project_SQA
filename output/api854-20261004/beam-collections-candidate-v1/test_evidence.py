"""Negative evidence guards for the independent bounded map oracle and JDI proof."""
import gzip, hashlib, io, json, tarfile, unittest
from pathlib import Path
import verify_native as v
BASE = Path(__file__).resolve().parent


def rows(stage='fixed_first'):
    return v.records((v.OUT / (stage + '.stdout.log')).read_bytes())


class CollectionsEvidenceTests(unittest.TestCase):
    def test_original_seals_sources_and_runtime_have_not_changed(self):
        seal = v.read(v.OUT / 'preexecution-seal.json')
        for name, digest in seal['suite_sha256'].items(): self.assertEqual(v.sha(BASE / name), digest, name)
        for name, digest in seal['cases_tsv_sha256'].items(): self.assertEqual(v.sha(v.OUT / name), digest, name)
        for name, digest in seal['runtime_source_sha256'].items(): self.assertEqual(v.sha(v.ROOT / name), digest, name)
        for name, digest in v.read(v.OUT / 'checksums.json').items(): self.assertEqual(v.sha(v.OUT / name), digest, name)
        for version in ['fixed', 'buggy']:
            archive = gzip.decompress((v.OUT / (version + '-production-archive.stdout.tar.gz')).read_bytes())
            command = v.read(v.OUT / (version + '-production-archive.command.json'))
            self.assertEqual(hashlib.sha256(archive).hexdigest(), command['stdout_sha256'])
            with tarfile.open(fileobj=io.BytesIO(archive)) as tar:
                sources = {e.name: tar.extractfile(e).read() for e in tar.getmembers()
                           if e.isfile() and e.name.endswith('.java')}
            if version == 'fixed':
                raw = (BASE / 'received-aom/output/api854-20261003/prepare-v11-chronology-development-v3/Collections-1/fixed-source' / v.SOURCE).read_bytes()
                self.assertEqual(sources[v.SOURCE].replace(b'\r\n', b'\n'), raw.replace(b'\r\n', b'\n'))
                sources[v.SOURCE] = raw
            self.assertEqual({name: hashlib.sha256(raw).hexdigest() for name, raw in sources.items()}, seal['source_sha256'][version])
    def test_repeats_and_exact_entries_match_ten_targets(self):
        proof = v.read(v.OUT / 'receipt.json')
        for version in ['fixed', 'buggy']:
            observed = []
            for suffix in ['first', 'second', 'method_entry']:
                stage = version + '_' + suffix; data = rows(stage)
                observed.append(v.validate(data, allow_failures=version == 'buggy'))
                self.assertEqual(observed[-1], proof['stages'][stage]['cases'])
                if suffix == 'method_entry': self.assertEqual(len(v.validate_trace(data, proof['stages'][stage]['exit_code'])), 40)
            self.assertEqual(observed[0], observed[1]); self.assertEqual(observed[0], observed[2])
        self.assertEqual(proof['buggy_failed_cases'], ['mapIterator_three'])
        self.assertEqual(proof['cpu_lock_exits'], [9, 0])
    def test_actual_hash_and_missing_mapping_mutations_are_detected(self):
        for label, count in [('hash', 3), ('conversion', 2)]:
            cases = [c for c in v.CASES if c['method'] == ('hashCode' if label == 'hash' else 'convertToMap')]
            observations = v.validate(v.records((v.OUT / (label + '-mutation.stdout.log')).read_bytes()), cases, allow_failures=True)
            self.assertEqual(sum(not r['target_check_passed'] for r in observations), count)
    def test_coherent_wrong_hash_or_scalar_type_cannot_pass(self):
        for value in [1, False]:
            data = rows(); row = next(r for r in data if r.get('case') == 'hashCode_empty')
            row['observation']['value'] = value
            with self.assertRaisesRegex(ValueError, 'Independent oracle'): v.validate(data)
    def test_live_views_and_iterator_updates_need_full_map_state(self):
        for case in ['entrySet_three', 'keySet_three', 'values_three', 'mapIterator_three']:
            data = rows(); row = next(r for r in data if r.get('case') == case)
            row['observation']['post_map'] = row['observation']['pre_map']
            row['observation']['source_unchanged'] = True
            with self.assertRaisesRegex(ValueError, 'Independent oracle'): v.validate(data)
    def test_serialization_and_constructor_aliases_cannot_pass(self):
        for case in ['constructor_three', 'readObject_three', 'writeObject_three', 'createDelegateMap_three']:
            data = rows(); row = next(r for r in data if r.get('case') == case)
            row['observation']['independent_result'] = False
            with self.assertRaisesRegex(ValueError, 'Independent oracle'): v.validate(data)
    def test_fixture_errors_and_skips_are_not_target_evidence(self):
        data = rows(); data[0]['setup_succeeded'] = False
        with self.assertRaisesRegex(ValueError, 'Fixture failure'): v.validate(data)
        data = rows(); data[-1]['skipped'] = 1
        with self.assertRaisesRegex(ValueError, 'Counters'): v.validate(data)
    def test_wrong_first_descriptor_and_missing_entries_are_rejected(self):
        data = rows('fixed_method_entry'); next(r for r in data if r.get('method_entry'))['descriptor'] = '()Vwrong'
        with self.assertRaisesRegex(ValueError, 'entry descriptor'): v.validate_trace(data)
        data = [r for r in rows('fixed_method_entry') if not (r.get('method_entry') and r['case'] == 'readObject_empty')]
        with self.assertRaisesRegex(ValueError, 'Missing exact entry'): v.validate_trace(data)
    def test_storage_conversion_must_preserve_every_mapping_and_clear_slots(self):
        data = rows(); row = next(r for r in data if r.get('case') == 'convertToMap_three')
        row['observation']['flat_slots_cleared'] = False
        with self.assertRaisesRegex(ValueError, 'Independent oracle'): v.validate(data)
    def test_real_evaluator_uses_unchanged_nested_suite_and_executes_all_stages(self):
        folder = BASE / 'd4j-v3'; record = v.read(folder / 'measurement/record.json')
        seal = v.read(folder / 'preexecution-seal.json')
        self.assertEqual(record['status'], 'complete'); self.assertEqual(record['fixed_validation'], 'passed_twice')
        self.assertTrue(record['fault_detected'])
        self.assertEqual(record['triggering_tests'], ['sqa.development.CollectionsCandidateTest::bounded_mapIterator'])
        self.assertEqual(v.sha(folder / 'packaged/suite.tar.bz2'), seal['suite_sha256'])
        self.assertEqual(record['suite_sha256'], seal['suite_sha256'])
        self.assertEqual(v.sha(folder / 'measurement' / Path(record['suite_path']).name), seal['suite_sha256'])
        with tarfile.open(folder / 'packaged/suite.tar.bz2') as tar:
            members = [m for m in tar.getmembers() if m.isfile()]
            self.assertEqual([m.name for m in members], ['sqa/development/CollectionsCandidateTest.java'])
            raw = tar.extractfile(members[0]).read()
            self.assertEqual(hashlib.sha256(raw).hexdigest(), seal['source_sha256'][members[0].name])
            self.assertIn(b'public static final class CollectionsCandidateProbe', raw)
            self.assertEqual(raw.count(b'@org.junit.Test'), 10)
        for stage in ['fixed-1', 'fixed-2', 'buggy', 'coverage']:
            self.assertEqual(v.read(folder / 'measurement' / stage / 'sqa-stage-counts.json'),
                             {'schema_version': 1, 'executed': 10, 'skipped': 0, 'target_checks': 10})
        self.assertEqual((record['line_covered'], record['line_total'], record['branch_covered'], record['branch_total']),
                         (203, 495, 103, 376))


if __name__ == '__main__': unittest.main(verbosity=2)
