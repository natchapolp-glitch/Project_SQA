"""Compact metadata and quota aliases must be determined before dispatch."""
from pathlib import Path
import sys
import unittest

sys.path.insert(0, str(Path(__file__).resolve().parents[1] / "scripts/study"))
import solo_ai_nightly as ai
import solo_nightly_batch as runner


class NightlyTest(unittest.TestCase):
    def test_p01_has_no_production_source(self):
        context = ai.analysis_context_for({"case": "Csv-1", "classes_modified": ["Reader"],
                                          "buggy_signatures": "public int read();", "source_excerpts": "SECRET_SOURCE_SENTINEL"})
        prompt = ai.TEMPLATES["P01"].format(context=context)
        self.assertIn("public int read();", prompt)
        self.assertNotIn("SECRET_SOURCE_SENTINEL", prompt)

    def test_round_robin_covers_projects_before_extra_bugs(self):
        config = {"inventory": {"Csv": [1, 2, 3], "Lang": [1, 2], "Chart": [1, 2], "Math": [1]}}
        jobs = runner.schedule(config, ["a02", "a03"], 4)
        self.assertEqual([r["case"] for r in jobs], ["Chart-1", "Chart-2", "Csv-2", "Lang-2"])
        self.assertEqual([r["account_alias"] for r in jobs], ["a02", "a03", "a02", "a03"])

    def test_existing_pilot_cases_are_not_regenerated(self):
        config = {"inventory": {"Csv": [1], "Lang": [1], "Math": [1]}}
        self.assertEqual(runner.schedule(config, ["a02"], 20), [])


if __name__ == "__main__":
    unittest.main()
