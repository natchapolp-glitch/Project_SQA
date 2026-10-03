"""Reject identity drift and misleading null-boundary evidence before review."""
import copy
import subprocess
import sys
import unittest

from scripts.study.api854.common import ROOT, read_json, sha256
from scripts.study.api854.build_v9_readiness_worklist import reconcile
from scripts.study.api854.verify_enum_boundary_development import (
    parse_log, validate_observations, validate_trace)

PREP = ROOT/'output/api854-20261003/prepare-v9-twenty-bug-development'
PROOF = ROOT/'output/api854-20261003/enum-boundary-development-v3'


class V9ContinuationEvidenceTests(unittest.TestCase):
    def math_inventory(self):
        selected = read_json(PREP/'Math-1/targets.json')['targets']
        excluded = read_json(PREP/'Math-1/capability-exclusions.json')['excluded']
        diagnostics = read_json(ROOT/'docs/api854/evidence/beam-v7-fixed-sweep-20261003/Math-1/record.json')['cases']
        return selected, excluded, diagnostics

    def trace(self):
        return parse_log((PROOF/'method_entry_trace.stdout.log').read_bytes())

    def test_current_partition_and_recorded_boundary_evidence_are_valid(self):
        self.assertEqual(len(reconcile(*self.math_inventory())), 85)
        trace = self.trace()
        self.assertEqual(len(validate_observations(trace)), 5)
        self.assertEqual(len(validate_trace(trace)), 7)
        work = read_json(ROOT/'output/api854-20261003/aom-champ-v9-readiness-worklist-v1.json')
        self.assertEqual(len(work['unsupported_targets']), 311)
        self.assertEqual(sum(r['unsupported'] for r in work['owners'].values()), 311)
        self.assertEqual(work['pending_empty_enum_targets'], 4)
        self.assertFalse(work['enum_joint_decision_approved'])
        self.assertFalse(work['original_314_closed'])

    def test_an_excluded_target_cannot_also_be_selected(self):
        selected, excluded, diagnostics = self.math_inventory()
        with self.assertRaisesRegex(ValueError, 'overlap'):
            reconcile(selected+[excluded[0]['target']], excluded, diagnostics)

    def test_duplicate_or_changed_diagnostic_identity_is_rejected(self):
        selected, excluded, diagnostics = self.math_inventory()
        with self.assertRaisesRegex(ValueError, 'Duplicate diagnostic'):
            reconcile(selected, excluded, diagnostics+[diagnostics[0]])
        changed = copy.deepcopy(diagnostics)
        changed[0]['target']['parameter_types'] += ',long'
        with self.assertRaisesRegex(ValueError, 'partition differs'):
            reconcile(selected, excluded, changed)

    def test_inherited_jsonparser_feature_is_not_target_coverage(self):
        trace = self.trace()
        entry = next(r for r in trace if r.get('method_entry'))
        entry['descriptor'] = entry['descriptor'].replace('dataformat/xml/deser/FromXmlParser', 'core/JsonParser')
        with self.assertRaisesRegex(ValueError, 'exact method descriptor'):
            validate_trace(trace)

    def test_false_configure_must_enter_disable(self):
        trace = self.trace()
        entry = next(r for r in trace if r.get('method_entry') and r['case'] == 'configure_false' and r['method'] == 'disable')
        entry['method'] = 'enable'
        with self.assertRaisesRegex(ValueError, 'delegation trace differs'):
            validate_trace(trace)

    def test_npe_alone_does_not_accept_changed_parser_state(self):
        trace = self.trace()
        case = next(r for r in trace if 'before' in r)
        case['after']['format_features'] = 1
        with self.assertRaisesRegex(ValueError, 'Parser state changed'):
            validate_observations(trace)

    def test_setup_failures_or_skipped_cases_cannot_be_accepted(self):
        trace = self.trace()
        case = next(r for r in trace if 'before' in r)
        case['setup_succeeded'] = False
        with self.assertRaisesRegex(ValueError, 'Setup/target assertion'):
            validate_observations(trace)
        trace = self.trace()
        summary = next(r for r in trace if r.get('summary'))
        summary['skipped'] = 1
        with self.assertRaisesRegex(ValueError, 'Execution counters'):
            validate_observations(trace)

    def test_existing_sealed_packet_is_rejected_without_any_writes(self):
        before = {p.name:sha256(p) for p in PROOF.iterdir() if p.is_file()}
        result = subprocess.run([sys.executable, '-m',
            'scripts.study.api854.verify_enum_boundary_development',
            '--defects4j','unused','--output',str(PROOF)],cwd=ROOT,capture_output=True)
        self.assertEqual(result.returncode, 2)
        self.assertIn(b'Evidence output already exists', result.stderr)
        self.assertEqual({p.name:sha256(p) for p in PROOF.iterdir() if p.is_file()}, before)
