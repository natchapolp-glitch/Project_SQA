"""Only the two jointly accepted signatures may enter the next shared inputs."""
import unittest
import tempfile
from pathlib import Path
import subprocess
import sys

from scripts.study.api854.common import ROOT, read_json
from scripts.study.api854.preparation import java_mapping

from scripts.study.api854.fixture_policy import POLICY_V5, select

FIELD_POLICY = 'aom-beam-fraction-field-v6-development'


def field_target(name, constructor='double', parameters=''):
    return {'class': 'org.apache.commons.math3.fraction.' + name,
            'constructor_types': constructor, 'method': 'getField', 'parameter_types': parameters}


class FractionFieldSelectionTests(unittest.TestCase):
    def test_prepare_worker_exports_reviewed_factories_and_rejects_changed_bytes(self):
        from scripts.study.api854 import prepare_worker
        if not hasattr(prepare_worker,'selected_context_sources'):
            self.fail('Prepare worker does not yet export the reviewed factory knowledge')
        with tempfile.TemporaryDirectory() as directory:
            tree = Path(directory)
            for short in ('BigFraction','Fraction'):
                target = tree/f'src/main/java/org/apache/commons/math3/fraction/{short}Field.java'
                target.parent.mkdir(parents=True,exist_ok=True)
                target.write_bytes((ROOT/'docs/api854/evidence/beam-champ-math-field-20261003-attempt2/supplemental-fixed-source'/target.name).read_bytes())
            selected = prepare_worker.selected_context_sources({'project':'Math','bug_id':1},
                'aom-beam-prepare-v8-development',{'src/Target.java':'0'*64},[],tree)
            self.assertEqual(selected,['src/Target.java',
                'src/main/java/org/apache/commons/math3/fraction/BigFractionField.java',
                'src/main/java/org/apache/commons/math3/fraction/FractionField.java'])
            target.write_text('// corrupt factory',encoding='utf-8')
            with self.assertRaisesRegex(ValueError,'factory'):
                prepare_worker.selected_context_sources({'project':'Math','bug_id':1},
                    'aom-beam-prepare-v8-development',{'src/Target.java':'0'*64},[],tree)

    def test_only_accepted_constructor_and_argument_signatures_are_added(self):
        accepted = [field_target('BigFraction'), field_target('Fraction')]
        unreviewed = [field_target('BigFraction', 'int,int'),
                      field_target('Fraction', 'double,double,int'),
                      field_target('Fraction', parameters='int'), field_target('OtherFraction')]
        self.assertEqual(select(accepted, POLICY_V5)[0], [])
        try:
            selected, excluded = select(accepted + unreviewed, FIELD_POLICY)
        except ValueError as error:
            self.fail('Accepted field policy is not implemented: ' + str(error))
        self.assertEqual(selected, accepted)
        self.assertEqual([row['target'] for row in excluded], unreviewed)

    def test_factory_knowledge_does_not_widen_modified_or_receiver_sources(self):
        receipt = read_json(ROOT/'output/api854-provider-preflight-20261003/champ-math-field-candidate-acceptance-v1.json')
        fixed, factories = {}, {}
        for candidate in receipt['accepted_candidates']:
            short = candidate['target']['class'].rsplit('.', 1)[1]
            path = 'src/main/java/org/apache/commons/math3/fraction/' + short
            fixed[path+'.java'] = candidate['target_source_sha256']
            factories[path+'Field.java'] = candidate['supplemental_factory_sha256']
        manifest = {'project':'Math', 'bug_id':1,
                    'source_files':[{'path':p, 'sha256':h} for p,h in {**fixed,**factories}.items()]}
        metadata = {'prepare_contract':'aom-beam-prepare-v8-development', 'fixed_source_sha256':fixed,
                    'additional_receiver_source_sha256':{}, 'additional_fixture_source_sha256':factories}
        try:
            self.assertEqual(java_mapping(manifest,metadata), (fixed,{}))
        except ValueError as error:
            self.fail('Reviewed factory supplements are not supported: ' + str(error))
        metadata['additional_fixture_source_sha256'] = {p:'0'*64 for p in factories}
        with self.assertRaises(ValueError):
            java_mapping(manifest,metadata)


class FractionFieldProbeTests(unittest.TestCase):
    @unittest.skipUnless(sys.platform != 'win32' and Path('/home/aomsin/sqa-round2/worktrees-isolated/beam-v7-fixed-sweep-20261003/Math-1/target/classes').is_dir(),
                         'Requires retained Math fixed classes in the WSL development environment')
    def test_real_fraction_receivers_have_structural_field_observations(self):
        sys.path.insert(0,str(ROOT/'scripts/study'))
        from generate import observe
        cp = (ROOT/'docs/api854/evidence/beam-v7-fixed-sweep-20261003/Math-1/cp.test.txt').read_text().strip()
        with tempfile.TemporaryDirectory() as directory:
            result = subprocess.run(['javac','-source','7','-target','7','-d',directory,
                                     str(ROOT/'algorithms/java/SqaProbe.java')],capture_output=True,text=True)
            self.assertEqual(result.returncode,0,result.stderr)
            for short in ('BigFraction','Fraction'):
                target = field_target(short)
                old = observe(cp+':'+directory,target,[0.5]*3,20,POLICY_V5)
                self.assertEqual(old['status'],'fixture_error')
                for value,rational in ((0.5,'7/4'),(-0.5,'-3/4')):
                    first = observe(cp+':'+directory,target,[value]*3,20,FIELD_POLICY)
                    second = observe(cp+':'+directory,target,[value]*3,20,FIELD_POLICY)
                    self.assertEqual(first['status'],'ok',first)
                    self.assertTrue(first['target_invoked'])
                    self.assertEqual(first,second)
                    self.assertEqual(first['outcome'], 'value:fraction-field:runtime=type:'+target['class']+
                                     ':zero=fraction:0/1:one=fraction:1/1|state=fraction:'+rational)


if __name__ == '__main__':
    unittest.main()
