import json
from pathlib import Path
import tempfile
import unittest

from scripts.study.api854.target_coverage import validate_stage
from evaluate import EvidenceError


class SupplementalCoverageTests(unittest.TestCase):
    def setUp(self):
        tmp = tempfile.TemporaryDirectory()
        self.addCleanup(tmp.cleanup)
        self.root = Path(tmp.name)
        # Actual Defects4J coverage logs have metrics, not the test command's
        # "Failing tests: 0" line. Empty retained failing_tests plus counters,
        # successful command and XML establish the coverage pass.
        (self.root / 'command.log').write_text('Running ant (run.gen.tests)... OK\nLines covered: 1\n')
        (self.root / 'failing_tests').write_text('')
        (self.root / 'coverage.xml').write_text('<coverage><class name="fixture.Target"/></coverage>')
        (self.root / 'summary.csv').write_text('LinesTotal,LinesCovered,ConditionsTotal,ConditionsCovered\n2,1,2,1\n')
        self.counts = {'schema_version':1,'executed':30,'skipped':0,'target_checks':30}
        self.write_counts()
        self.stage = {'exit_code':0,'timed_out':False}

    def write_counts(self):
        (self.root / 'sqa-stage-counts.json').write_text(json.dumps(self.counts))

    def test_real_coverage_log_format_is_accepted_with_retained_passing_evidence(self):
        counts, digest = validate_stage(self.stage,self.root,30,['fixture.Target'])
        self.assertEqual(counts,self.counts)
        self.assertEqual(len(digest),64)

    def test_unexecuted_or_skipped_tests_cannot_establish_target_coverage(self):
        self.counts['skipped'] = 1
        self.write_counts()
        with self.assertRaises(EvidenceError): validate_stage(self.stage,self.root,30,['fixture.Target'])
        self.counts.update(skipped=0,target_checks=29)
        self.write_counts()
        with self.assertRaises(EvidenceError): validate_stage(self.stage,self.root,30,['fixture.Target'])

    def test_missing_override_class_or_nonempty_failures_are_rejected(self):
        with self.assertRaises(EvidenceError): validate_stage(self.stage,self.root,30,['fixture.Override'])
        (self.root / 'failing_tests').write_text('--- fixture.Target::test1\n')
        with self.assertRaises(EvidenceError): validate_stage(self.stage,self.root,30,['fixture.Target'])
