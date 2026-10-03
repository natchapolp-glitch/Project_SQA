"""Reporting guards: retained invalids, independent XML counts and replay deduplication."""
import csv
from pathlib import Path
import sys
import tempfile
import unittest

sys.path.insert(0,str(Path(__file__).resolve().parents[1]))
from aom_ready_peer_results import junit_counts,generation_key,write_union_csv,REPORT,OLD
from aom_ready_csv import load


class PeerReportTests(unittest.TestCase):
    def setUp(self):
        self.temp=tempfile.TemporaryDirectory()
        self.addCleanup(self.temp.cleanup)
        self.root=Path(self.temp.name)

    def xml(self,duplicate=False):
        folder=self.root/"stage"
        (folder/"junit-reports").mkdir(parents=True)
        names=["one","one" if duplicate else "two"]
        body=''.join('<testcase classname="ExampleTest" name="'+n+'"/>' for n in names)
        (folder/"junit-reports/TEST-ExampleTest.xml").write_text('<testsuite tests="2" failures="0" errors="0" skipped="0">'+body+'</testsuite>')
        (folder/"all_tests").write_text('one(ExampleTest)\ntwo(ExampleTest)\n')
        return folder

    def test_duplicate_xml_identity_rejected(self):
        with self.assertRaisesRegex(ValueError,"Duplicate"):
            junit_counts(self.xml(True),2,{"executed":2,"skipped":0,"failed":0,"errors":0,"target_checks":None},False)

    def test_algorithm_cannot_infer_missing_target_counter(self):
        expected={"executed":2,"skipped":0,"failed":0,"errors":0,"target_checks":None}
        with self.assertRaisesRegex(ValueError,"did not check"):
            junit_counts(self.xml(),2,expected,True)

    def test_csv_keeps_native_only_columns_and_null_metrics(self):
        path=self.root/"results.csv"
        write_union_csv(path,[{"status":"complete","fault":False},{"status":"pending","fault":None,"next":"wait"}])
        with path.open(newline="") as f: rows=list(csv.DictReader(f))
        self.assertEqual(rows[1]["next"],"wait")
        self.assertEqual(rows[1]["fault"],"")

    def test_replay_host_does_not_create_a_generation_repeat(self):
        one={"project":"Csv","bug_id":1,"approach":"kku-claude","condition":"aom-execution","generation_condition":"generation-v2"}
        two={**one,"condition":"beam-execution","evaluation_host":"beam-pc1"}
        self.assertEqual(generation_key(one),generation_key(two))
        self.assertNotEqual(generation_key(one),generation_key({**two,"generation_condition":"generation-v1"}))

    def test_historical_rows_unchanged(self):
        original=load(OLD/"full-d4j-results.json")["records"]
        latest=load(REPORT/"full-d4j-results.json")["records"]
        self.assertEqual(latest[:len(original)],original)
        completed=[r for r in latest if r["status"]=="complete"]
        self.assertEqual(len(completed),14)
        self.assertEqual(len({generation_key(r) for r in completed}),10)

    def test_native_invalid_jsoup_ai_is_not_full_d4j_measurement(self):
        native=load(REPORT/"native-results.json")["records"]
        self.assertEqual(len(native),24)
        self.assertEqual(len({r["project"] for r in native}),5)
        ai=[r for r in native if r["project"]=="Jsoup" and r["approach"].startswith("kku-")]
        self.assertEqual(sorted(r["fixed_first_failed"] for r in ai),[1,2])
        for row in ai:
            self.assertIsNone(row["raw_native_fault_flag"])
            self.assertIsNone(row["target_class_covered_lines"])
        full=load(REPORT/"full-d4j-results.json")["records"]
        self.assertFalse(any(r["project"]=="Jsoup" and r["approach"].startswith("kku-") for r in full))

    def test_v13_and_pending_cli_ai_not_transferred(self):
        catalog=load(REPORT/"condition-catalog.json")
        self.assertFalse(catalog["v13"]["included_in_v12_results"])
        latest=load(REPORT/"latest-four-methods.json")["records"]
        self.assertEqual(len(latest),20)
        pending=[r for r in latest if r["project"]=="Cli" and r["approach"].startswith("kku-")]
        for row in pending:
            self.assertEqual(row["status"],"pending_not_generated_in_new_condition")
            self.assertIsNone(row["fault_detected"])
            self.assertFalse(row["full_defects4j_completed"])


if __name__=="__main__": unittest.main()
