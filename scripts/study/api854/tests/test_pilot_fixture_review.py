import unittest
from scripts.study.api854.common import ROOT, read_json
from scripts.study.api854.fixture_policy import POLICY_V4, POLICY_V5, select
from scripts.study.api854.fixture_semantics import descriptor, expected_exception


class PilotFixtureReviewTests(unittest.TestCase):
    def test_v5_has_a_predeclared_recipe_for_every_remaining_pilot_bug(self):
        remaining = [('Chart',1),('Cli',1),('Closure',1),('Compress',1),('Gson',1),('JacksonCore',1),
            ('JacksonDatabind',1),('JacksonDatabind',112),('JacksonXml',1),('Jsoup',1),('JxPath',22),
            ('Lang',1),('Math',1),('Mockito',1),('Time',1)]
        for project, bug in remaining:
            with self.subTest(project=project,bug=bug):
                targets = read_json(ROOT / f'docs/api854/evidence/beam-aom-review-20261003/declarations/{project}-{bug}/targets.json')['targets']
                kept, excluded = select(targets,POLICY_V5)
                self.assertTrue(kept)
                self.assertEqual(len(targets),len(kept)+len(excluded))
                self.assertTrue(all(t['method'] not in {'<init>','hashCode'} for t in kept))
                self.assertTrue(all(row['reason'] for row in excluded))

    def test_new_policy_does_not_relabel_v4_or_accept_correlated_slice_arguments(self):
        base = {'class':'com.fasterxml.jackson.core.util.TextBuffer',
            'constructor_types':'com.fasterxml.jackson.core.util.BufferRecycler','method':'append','parameter_types':'char'}
        slice_target = {**base,'parameter_types':'[C,int,int'}
        self.assertEqual(select([base],POLICY_V4)[0],[])
        kept, excluded = select([base,slice_target],POLICY_V5)
        self.assertEqual(kept,[base])
        self.assertEqual(excluded[0]['reason'],'text_buffer_slice_recipe_not_reviewed')

    def test_only_documented_numeric_boundary_exception_is_accepted(self):
        target = {'class':'org.apache.commons.lang3.math.NumberUtils','method':'createNumber','parameter_types':'java.lang.String'}
        self.assertTrue(expected_exception(target,'exception:java.lang.NumberFormatException'))
        self.assertFalse(expected_exception(target,'exception:java.lang.NullPointerException'))
        self.assertFalse(expected_exception({**target,'method':'max'},'exception:java.lang.NumberFormatException'))
        self.assertFalse(expected_exception({**target,'class':'unknown.Class'},'exception:java.lang.NumberFormatException'))

    def test_coverage_identity_keeps_arrays_and_overloads_distinct(self):
        self.assertEqual(descriptor('[I,int,java.lang.String'),'([IILjava/lang/String;)')
        self.assertEqual(descriptor('[Ljava.lang.Object;'),'([Ljava/lang/Object;)')
        self.assertNotEqual(descriptor('char'),descriptor('[C,int,int'))
