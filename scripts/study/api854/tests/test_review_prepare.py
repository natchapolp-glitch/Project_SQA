import hashlib
import json
from pathlib import Path
import tempfile
import unittest

from scripts.study.api854 import review_prepare
from scripts.study.api854.common import write_json


class ArtifactReviewTests(unittest.TestCase):
    def setUp(self):
        self.temp = tempfile.TemporaryDirectory()
        self.root = Path(self.temp.name)
        source = self.root / 'fixed-source/src/example/Target.java'
        source.parent.mkdir(parents=True)
        source.write_bytes(b'package example; class Target {}\r\n')
        files = [{'path': 'src/example/Target.java', 'sha256': hashlib.sha256(source.read_bytes()).hexdigest(),
                  'bytes': source.stat().st_size}]
        context_hash = review_prepare.composite_hash(files)
        (self.root / 'prompt.md').write_bytes(b'fixed-only prompt\r\n')
        (self.root / 'context.md').write_bytes(b'fixed context')
        metadata = {'source_sha256': context_hash, 'prompt_sha256': hashlib.sha256(b'fixed-only prompt\r\n').hexdigest(),
            'prompt_utf8_bytes': len(b'fixed-only prompt\r\n'), 'context_policy_id': 'fixture-context',
            'prompt_policy_id': 'fixture-prompt', 'target_classes': ['example.Target']}
        self.row = {'project': 'Lang', 'bug_id': 4, **metadata}
        write_json(self.root / 'context-manifest.json', {'project': 'Lang', 'bug_id': 4, 'revision': '4f',
            'contains_execution_logs': False, 'source_files': files, 'source_hash': context_hash,
            'selection_policy_id': 'fixture-context'})
        write_json(self.root / 'prepare-metadata.json', metadata)
        write_json(self.root / 'revision-proof.json', {'head': 'a' * 40, 'fixed_tag_commit': 'a' * 40,
            'verified': True, 'matches_fixed_tag': True, 'selected_source_changes': ''})
        self.checksums()

    def tearDown(self):
        self.temp.cleanup()

    def checksums(self):
        data = {p.relative_to(self.root).as_posix(): hashlib.sha256(p.read_bytes()).hexdigest()
                for p in self.root.rglob('*') if p.is_file() and p.name != 'checksums.json'}
        (self.root / 'checksums.json').write_text(json.dumps(data))

    def test_exact_bytes_and_fixed_metadata_pass_without_approving_semantics(self):
        result = review_prepare.validate_artifact(self.root, self.row)
        self.assertEqual(result['integrity'], 'passed')
        self.assertNotIn('usable', result)

    def test_changed_source_or_prompt_is_rejected(self):
        (self.root / 'prompt.md').write_bytes(b'changed prompt')
        with self.assertRaisesRegex(ValueError, 'checksum'):
            review_prepare.validate_artifact(self.root, self.row)

    def test_extra_checksumming_cannot_hide_outside_contract_files(self):
        (self.root / 'unexpected.json').write_bytes(b'{}')
        self.checksums()
        with self.assertRaisesRegex(ValueError, 'Unexpected file'):
            review_prepare.validate_artifact(self.root, self.row)

    def test_traversal_rejected_without_reading_outside_artifact(self):
        (self.root / 'checksums.json').write_text(json.dumps({'../outside': 'a' * 64}))
        with self.assertRaises(ValueError):
            review_prepare.validate_artifact(self.root, self.row)
        with self.assertRaises(ValueError):
            review_prepare.safe_relative('../outside')
