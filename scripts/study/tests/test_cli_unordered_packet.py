"""Protect prospective prompt/source binding and immutable condition boundaries."""
import json
from pathlib import Path
import sys
import unittest

sys.path.insert(0, str(Path(__file__).resolve().parents[1]))
from cli_unordered_oracle import OUTPUT, RUNTIME, POLICY, helper_source
from aom_ready_csv import INTAKE, load, digest, verify_manifest
from aom_cli_unordered_measure import MEASURED
from aom_cli_unordered_replay import REPLAY
from aom_cli_unordered_report import OLD, REPORT
from aom_ready_report import stage_counts


class CliPacketTests(unittest.TestCase):
    def test_embedded_recipe_has_exact_runtime_bytes(self):
        recipe = load(OUTPUT / "prepared/Cli-1/fixture-recipes.json")
        self.assertEqual(recipe["fixture_policy_id"], POLICY)
        for name, source in recipe["sources"].items():
            with self.subTest(name=name):
                self.assertEqual(source.encode("utf-8"), (RUNTIME / name).read_bytes())
                self.assertEqual(digest(source.encode("utf-8")), recipe["source_sha256"][name])

    def test_prompt_embeds_entire_new_recipe(self):
        packet = OUTPUT / "prepared/Cli-1"
        prompt = (packet / "prompt.md").read_text(encoding="utf-8")
        recipe = load(packet / "fixture-recipes.json")
        self.assertEqual(prompt.count(json.dumps(recipe, indent=2, ensure_ascii=False)), 1)
        self.assertNotIn("-0.8935972282600377", prompt)
        self.assertNotIn("GeneratedStudyTest.generated16", prompt)
        self.assertEqual(digest((packet / "prompt.md").read_bytes()), load(packet / "prepare-metadata.json")["prompt_sha256"])

    def test_derived_helper_reproducible_from_immutable_v12(self):
        original = (INTAKE / "baseline/algorithms/java/SqaProbe.java").read_bytes()
        self.assertEqual(helper_source(original), (RUNTIME / "algorithms/java/SqaProbe.java").read_bytes())
        self.assertNotIn(POLICY.encode(), original)

    def test_scope_and_gate_boundary(self):
        packet = OUTPUT / "prepared/Cli-1"
        targets = load(packet / "targets.json")["targets"]
        self.assertEqual(len(targets),16)
        self.assertEqual({t["class"] for t in targets},{"org.apache.commons.cli.CommandLine"})
        protocol = load(OUTPUT / "protocol.proposal.json")
        self.assertEqual(protocol["enabled_stages"], [])
        self.assertFalse(protocol["primary"])
        self.assertIsNone(protocol["generation"]["prompt_token_reserve"])
        self.assertEqual(len(load(OUTPUT / "prompt-reserve-worksheet.json")["records"]),2)

    def test_packet_seal_and_narrow_runtime_changes(self):
        self.assertGreater(verify_manifest(OUTPUT),41)
        self.assertEqual(set(load(OUTPUT / "receipt.json")["changed_runtime_files"]),
            {"algorithms/java/SqaProbe.java","scripts/study/generate.py","scripts/study/api854/fixture_policy.py"})

    def test_new_ai_pending_and_historical_rows_not_transferred(self):
        report=load(REPORT / "full-d4j-results.json")
        previous=load(OLD / "full-d4j-results.json")["records"]
        self.assertEqual(report["records"][:len(previous)],previous)
        self.assertEqual(len(report["pending_new_condition"]),2)
        for row in report["pending_new_condition"]:
            self.assertEqual(row["status"],"pending_not_generated_in_new_condition")
            self.assertIsNone(row["fault_detected"])
            self.assertIsNone(row["declared_tests"])

    def test_completed_replay_preserves_suites_and_real_counters(self):
        for algorithm in ("cmaes","fscs-art"):
            generation=load(MEASURED / algorithm / "generation/generation.json")
            record=load(REPLAY / algorithm / "record.json")
            self.assertEqual(record["status"],"complete")
            self.assertEqual(generation["suite_sha256"],record["suite_sha256"])
            for name in ("fixed-1","fixed-2","buggy","coverage"):
                counts=stage_counts(REPLAY / algorithm / name,30,True)
                self.assertEqual((counts["executed"],counts["skipped"],counts["target_checks"]),(30,0,30))

    def test_framework_restored_and_failed_attempts_retained(self):
        restoration=load(REPLAY / "framework-restoration.json")
        self.assertTrue(restoration["restored_exactly"])
        self.assertEqual(restoration["before_sha256"],restoration["restored_sha256"])
        for algorithm in ("cmaes","fscs-art"):
            record=load(MEASURED / algorithm / "measurement/record.json")
            self.assertEqual(record["status"],"failed")
            self.assertIsNone(record["fault_detected"])
            self.assertIn("SelfDescribing",(MEASURED / algorithm / "measurement/fixed-1/failing_tests").read_text())


if __name__ == "__main__":
    unittest.main()
