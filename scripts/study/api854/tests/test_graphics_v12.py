import json
import unittest
import tempfile
import shutil
from pathlib import Path
from unittest.mock import patch
from scripts.study.api854.graphics_v12 import contract
from scripts.study.api854.fixture_policy import POLICY_V11, POLICY_V12, select
from scripts.study.api854.common import ROOT


class GraphicsScopeTests(unittest.TestCase):
    def test_only_joint_exact_seven_extend_v11(self):
        c = contract()
        discovery = json.loads((ROOT/'output/api854-20261003/prepare-v3/Chart-1/targets.json').read_bytes())['targets']
        before, _ = select(discovery, POLICY_V11)
        after, _ = select(discovery, POLICY_V12)
        fields = ('class','constructor_types','method','parameter_types')
        keys = lambda rows: {tuple(t[k] for k in fields) for t in rows}
        self.assertEqual(len(before), 8)
        self.assertEqual(len(after), 15)
        self.assertEqual(keys(after)-keys(before), keys(c['targets']))
        self.assertFalse(c['shared_integration_approved'])

    def test_overload_or_receiver_changes_stay_excluded(self):
        c = contract()
        rows = [{**c['targets'][0], 'constructor_types':'boolean'},
                {**c['targets'][0], 'parameter_types':'java.awt.Graphics2D'}]
        selected, excluded = select(rows, POLICY_V12)
        self.assertEqual(selected, [])
        self.assertEqual(len(excluded), 2)

    def test_rehashed_intake_cannot_remove_immutable_trace_binding(self):
        from scripts.study.api854 import graphics_v12 as intake
        from scripts.study.api854.preparation import encoded
        from scripts.study.api854.common import sha256
        with tempfile.TemporaryDirectory() as folder:
            destination=Path(folder)/'intake'
            shutil.copytree(intake.INTAKE,destination)
            receipt=json.loads((destination/'receipt.json').read_bytes())
            del receipt['received']['candidate-trace.java']
            (destination/'receipt.json').write_bytes(encoded(receipt))
            hashes=json.loads((destination/'checksums.json').read_bytes())
            hashes['receipt.json']=sha256(destination/'receipt.json')
            (destination/'checksums.json').write_bytes(encoded(hashes))
            with patch.object(intake,'INTAKE',destination):
                with self.assertRaisesRegex(ValueError,'inventory'):
                    intake.load_intake()

    def test_empty_rehashed_shared_manifest_cannot_authorize_selection(self):
        from scripts.study.api854 import verify_graphics_v12 as proof
        with tempfile.TemporaryDirectory() as folder:
            destination=Path(folder)
            (destination/'receipt.json').write_text('{"status":"pass"}')
            (destination/'checksums.json').write_text('{}')
            with patch.object(proof,'OUTPUT',destination):
                with self.assertRaisesRegex(ValueError,'inventory'):
                    proof.checked()

    def test_all_twenty_four_cases_are_reachable_in_generator_domain(self):
        from scripts.study.api854.verify_graphics_v12 import oracle_module,cases_vectors
        policy,_,_=oracle_module()
        vectors=cases_vectors(policy)
        self.assertEqual(len(vectors),24)
        self.assertTrue(all(-1<=v<=1 for v in vectors.values()))
        for method in set(policy['cases'].values()):
            values=[v for n,v in vectors.items() if policy['cases'][n]==method]
            self.assertEqual(sorted({min(len(values)-1,int((v+1)*len(values)/2)) for v in values}),list(range(len(values))))
