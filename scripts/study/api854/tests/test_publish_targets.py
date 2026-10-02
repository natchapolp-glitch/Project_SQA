from pathlib import Path
import tempfile
import unittest
from scripts.study.api854.common import sha256
from scripts.study.api854.queue_worker import canonical_targets_artifact


class TargetArtifactTests(unittest.TestCase):
    def test_selected_inventory_uses_contract_name_without_editing_source_bytes(self):
        with tempfile.TemporaryDirectory() as directory:
            root = Path(directory)
            source = root / 'targets.fixture-policy.json'
            data = b'{"targets":[{"class":"example.Target"}]}\r\n'
            source.write_bytes(data)
            destination = canonical_targets_artifact(source, root, sha256(source))
            self.assertEqual(destination.name, 'targets.json')
            self.assertEqual(destination.read_bytes(), data)
            self.assertEqual(source.read_bytes(), data)
            with self.assertRaises(FileExistsError):
                canonical_targets_artifact(source, root, sha256(source))

    def test_changed_inventory_is_rejected_before_canonical_artifact_is_created(self):
        with tempfile.TemporaryDirectory() as directory:
            root = Path(directory)
            source = root / 'targets.fixture-policy.json'
            source.write_bytes(b'changed')
            with self.assertRaisesRegex(ValueError, 'changed before upload'):
                canonical_targets_artifact(source, root, '0' * 64)
            self.assertFalse((root / 'targets.json').exists())
