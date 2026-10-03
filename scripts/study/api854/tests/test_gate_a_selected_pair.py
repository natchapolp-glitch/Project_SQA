"""Selected Gate A inputs must not silently reuse earlier runner/recipe bytes."""
import json
from pathlib import Path
import shutil
import tempfile
import unittest

from scripts.study.api854.common import ROOT, read_json, sha256, implementation_hashes
from scripts.study.api854.build_prepare_v6_development import build
from scripts.study.api854.gate_a import inspect
from scripts.study.api854.preparation import encoded

PROTOCOL = 'output/api854-20261003/aom-continuation-v6-development/protocol.proposal.json'
RUNNER = 'output/api854-20261003/aom-continuation-v6-development/runner-plan.json'
PREP = 'output/api854-20261003/prepare-v6-twenty-bug-development'
DISCOVERY = 'output/api854-20261003/prepare-v3'


class SelectedGateTests(unittest.TestCase):
    def setUp(self):
        # Only isolated copies are mutated; upstream evidence remains byte-exact.
        self.temp = tempfile.TemporaryDirectory()
        self.addCleanup(self.temp.cleanup)
        self.root = Path(self.temp.name).resolve()
        self.protocol = read_json(ROOT/PROTOCOL)
        self.protocol['source_sha256'] = implementation_hashes()
        names = [PROTOCOL, RUNNER, 'experiments/configs/api854-20261003/protocol.core-frozen.json',
                 'experiments/configs/api854-20261003/ownership.json',
                 'experiments/configs/api854-20261003/runner-plan.v1.json', DISCOVERY+'/index.json']
        names += list(self.protocol['source_sha256'])
        for name in names:
            destination = self.root/name
            destination.parent.mkdir(parents=True, exist_ok=True)
            destination.write_bytes((ROOT/name).read_bytes())
        # Compose current recipes only in this isolated fixture. Copying an old
        # immutable proposal beside a newer runtime would deliberately be stale.
        for row in read_json(ROOT/(PREP+'/index.json'))['records']:
            name = f"{row['project']}-{row['bug_id']}"
            shutil.copytree(ROOT/DISCOVERY/name, self.root/DISCOVERY/name)
        build(self.root/DISCOVERY, self.root/PREP)
        self.protocol['preparation_import_evidence']['sha256'] = sha256(self.root/PREP/'index.json')
        self.write_protocol()

    def run_gate(self, runner=RUNNER):
        result = inspect(self.root, protocol_path=PROTOCOL, runner_path=runner)
        return result, {row['id']:row for row in result['checklist']}

    def write_protocol(self):
        (self.root/PROTOCOL).write_bytes(encoded(self.protocol))

    def test_paths_are_required_instead_of_legacy_fallback(self):
        with self.assertRaisesRegex(ValueError, 'Explicit protocol and runner'):
            inspect(self.root)

    def test_twenty_bug_bindings_do_not_approve_incomplete_declaration_coverage(self):
        result, checks = self.run_gate()
        for name in ('protocol_runner_binding', 'runtime_source_binding', 'shared_policy', 'fixture_recipe_binding'):
            self.assertEqual(checks[name]['status'], 'pass')
        self.assertEqual(checks['fixture_recipe_binding']['validated_recipe_bugs'], 20)
        self.assertEqual(checks['all_common_declarations']['selected_declarations'], 377)
        self.assertEqual(checks['prepare_contract']['bugs'], 20)
        self.assertEqual(checks['prepare_contract']['issues'], [])
        self.assertEqual(checks['prepare_contract']['status'], 'pass')
        self.assertEqual(checks['all_common_declarations']['status'], 'blocked')
        self.assertFalse(result['gate_a_passed'])
        self.assertFalse(result['generation_authorized'])
        self.assertEqual(result['live_requests'], 0)
        self.assertEqual(result['queue_mutations'], 0)

    def test_old_runner_with_complete_routes_still_fails_pair_binding(self):
        result, checks = self.run_gate('experiments/configs/api854-20261003/runner-plan.v1.json')
        self.assertEqual(checks['runner_coverage']['status'], 'pass')
        self.assertEqual(checks['protocol_runner_binding']['status'], 'blocked')
        self.assertTrue(result['selected_runner'].endswith('runner-plan.v1.json'))

    def test_protocol_cannot_claim_another_contract_for_valid_recipe_files(self):
        self.protocol['generation']['prepare_contract'] = 'aom-beam-prepare-v3'
        self.write_protocol()
        _, checks = self.run_gate()
        self.assertEqual(checks['fixture_recipe_binding']['status'], 'blocked')
        self.assertGreaterEqual(len(checks['prepare_contract']['issues']), 5)

    def test_changed_recipe_cannot_be_hidden_by_refreshing_checksum_manifest(self):
        folder = self.root/PREP/'Codec-1'
        recipe = read_json(folder/'fixture-recipes.json')
        recipe['fixture_policy_id'] = 'beam-explicit-fixtures-v3-proposal'
        (folder/'fixture-recipes.json').write_bytes(encoded(recipe))
        hashes = read_json(folder/'checksums.json')
        hashes['fixture-recipes.json'] = sha256(folder/'fixture-recipes.json')
        (folder/'checksums.json').write_bytes(encoded(hashes))
        _, checks = self.run_gate()
        self.assertEqual(checks['fixture_recipe_binding']['status'], 'blocked')
        self.assertTrue(any(r.get('project') == 'Codec' for r in checks['prepare_contract']['issues']))

    def test_duplicate_index_identity_cannot_inflate_completeness(self):
        path = self.root/PREP/'index.json'
        index = read_json(path)
        index['records'].append(index['records'][0])
        path.write_bytes(encoded(index))
        self.protocol['preparation_import_evidence']['sha256'] = sha256(path)
        self.write_protocol()
        _, checks = self.run_gate()
        self.assertEqual(checks['preparation_index_binding']['status'], 'pass')
        self.assertEqual(checks['prepare_contract']['status'], 'blocked')
        self.assertTrue(checks['prepare_contract']['issues'])

    def test_modified_runtime_fails_even_if_preparation_checksums_still_match(self):
        path = self.root/'algorithms/java/SqaProbe.java'
        path.write_bytes(path.read_bytes()+b'\n// isolated stale-runtime test\n')
        _, checks = self.run_gate()
        self.assertEqual(checks['runtime_source_binding']['status'], 'blocked')

    def test_repinning_runtime_without_regenerating_embedded_recipe_is_blocked(self):
        name = 'algorithms/java/SqaProbe.java'
        path = self.root/name
        path.write_bytes(path.read_bytes()+b'\n// new isolated runtime version\n')
        self.protocol['source_sha256'][name] = sha256(path)
        self.write_protocol()
        result, checks = self.run_gate()
        self.assertEqual(checks['runtime_source_binding']['status'], 'pass')
        self.assertEqual(checks['fixture_recipe_binding']['status'], 'blocked')
        self.assertTrue(any('recipe source differs' in row['reason'].lower()
                            for row in checks['prepare_contract']['issues']))
        self.assertFalse(result['gate_a_passed'])

    def test_unbound_index_fails_without_modifying_artifact_bytes(self):
        self.protocol['preparation_import_evidence']['sha256'] = '0'*64
        self.write_protocol()
        _, checks = self.run_gate()
        self.assertEqual(checks['preparation_index_binding']['status'], 'blocked')

    def test_selected_path_cannot_escape_root(self):
        with self.assertRaises(ValueError):
            inspect(self.root, protocol_path=ROOT/PROTOCOL, runner_path=RUNNER)

    def test_declared_prompt_maximum_cannot_replace_actual_byte_measurement(self):
        path = self.root/PREP/'index.json'
        index = read_json(path)
        index['max_prompt_utf8_bytes'] = 1
        path.write_bytes(encoded(index))
        self.protocol['preparation_import_evidence']['sha256'] = sha256(path)
        self.write_protocol()
        _, checks = self.run_gate()
        self.assertEqual(checks['preparation_index_binding']['status'], 'pass')
        self.assertTrue(any('prompt maximum' in r['reason'] for r in checks['prepare_contract']['issues']))

    def test_changed_common_discovery_cannot_hide_behind_updated_metadata_and_checksums(self):
        folder = self.root/DISCOVERY/'Codec-1'
        document = read_json(folder/'targets.json')
        document['targets'] = document['targets'][:-1]
        (folder/'targets.json').write_bytes(encoded(document))
        metadata = read_json(folder/'prepare-metadata.json')
        metadata['targets_sha256'] = sha256(folder/'targets.json')
        metadata['target_count'] = len(document['targets'])
        (folder/'prepare-metadata.json').write_bytes(encoded(metadata))
        checksums = read_json(folder/'checksums.json')
        for name in ('targets.json','prepare-metadata.json'):
            checksums[name] = sha256(folder/name)
        (folder/'checksums.json').write_bytes(encoded(checksums))
        _, checks = self.run_gate()
        self.assertTrue(any('Discovery index/metadata lineage' in r['reason'] for r in checks['prepare_contract']['issues']))


if __name__ == '__main__':
    unittest.main()
