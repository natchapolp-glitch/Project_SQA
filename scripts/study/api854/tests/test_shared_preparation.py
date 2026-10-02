from dataclasses import replace
import json
from pathlib import Path
import tempfile
import unittest

from scripts.study.api854.context_export import export_context
from scripts.study.api854.preparation import compose, validate, POLICY, encoded, digest, CONTEXT_POLICY, PROMPT_POLICY
from scripts.study.api854.api_worker import resolve_prepared_job, WorkerBlocked
from . import test_api_worker as fixture


class SharedPreparationTests(unittest.TestCase):
    def setUp(self):
        self.tmp = tempfile.TemporaryDirectory()
        self.addCleanup(self.tmp.cleanup)
        self.root = Path(self.tmp.name)
        fixed = self.root / "fixed"
        fixed.mkdir()
        (fixed / '.defects4j.config').write_text('pid=Lang\nvid=1f\n', encoding='utf-8')
        (fixed / 'Example.java').write_text('public class Example { public int value() { return 1; } }', encoding='utf-8')
        self.output = self.root / 'context'
        self.manifest = export_context(fixed, 'Lang', 1, ['Example.java'], self.output, policy_id=CONTEXT_POLICY)

    def composed(self, targets=None):
        metadata = compose(self.output, classes=['Example'], targets=targets)
        data = {name: (self.output / name).read_bytes() for name in
                ('prompt.md', 'context-manifest.json', 'targets.json', 'prepare-policy.json')}
        return metadata, data

    def test_hash_mapping_policy_and_log_redaction_match_both_consumers(self):
        targets = [{'class':'Example','constructor_types':'','method':'value','parameter_types':'',
                    'dimensions':0, 'error':'DO_NOT_SEND_EXECUTION_LOG'}]
        metadata, data = self.composed(targets)
        self.assertNotIn(b'DO_NOT_SEND_EXECUTION_LOG', data['prompt.md'])
        self.assertNotIn(b'DO_NOT_SEND_EXECUTION_LOG', data['targets.json'])
        self.assertTrue(validate(self.manifest, metadata, data['prompt.md'], data['targets.json'], data['prepare-policy.json'], require_eligible=True))
        f = fixture.APIWorkerTests()
        f.setUp()
        self.addCleanup(f.tearDown)
        settings = replace(f.settings, context_policy_id=CONTEXT_POLICY, prompt_policy_id=PROMPT_POLICY,
            prepare_contract='aom-beam-prepare-v2', handoff_contract='beam-v1', prompt_token_reserve=100000,
            protocol={'prepare_policy_sha256': digest(encoded(POLICY))})
        job = f.claim['job']
        job['payload']['stage_history'][0].update(metadata=metadata,
            artifacts=[{'name':n,'size':len(b),'sha256':digest(b)} for n,b in data.items()])
        class Client:
            def download(self, artifact): return data[artifact['name']]
        self.assertEqual(resolve_prepared_job(Client(), job, settings).prompt.encode(), data['prompt.md'])
        metadata['fixed_source_sha256'] = {'Wrong.java':'a' * 64}
        with self.assertRaisesRegex(WorkerBlocked, 'lineage_mismatch'):
            resolve_prepared_job(Client(), job, settings)

    def test_missing_declarations_are_explicitly_pending_and_cannot_generate(self):
        metadata, data = self.composed()
        self.assertFalse(metadata['adapter_eligibility_verified'])
        self.assertTrue(validate(self.manifest, metadata, data['prompt.md'], data['targets.json'], data['prepare-policy.json']))
        with self.assertRaisesRegex(ValueError, 'required before generation'):
            validate(self.manifest, metadata, data['prompt.md'], data['targets.json'], data['prepare-policy.json'], require_eligible=True)
        with self.assertRaises(FileExistsError):
            compose(self.output, classes=['Example'])

    def test_tampered_prompt_targets_and_policy_are_rejected(self):
        metadata, data = self.composed()
        for name in ('prompt.md','targets.json','prepare-policy.json'):
            changed = dict(data)
            changed[name] += b'changed'
            with self.subTest(name=name), self.assertRaises(ValueError):
                validate(self.manifest, metadata, changed['prompt.md'], changed['targets.json'], changed['prepare-policy.json'])
