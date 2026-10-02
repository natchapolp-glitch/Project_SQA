import json
from pathlib import Path
import tempfile
import unittest
from concurrent.futures import ThreadPoolExecutor

from scripts.study.api854 import inventory, queue, report

ROOT = Path(__file__).resolve().parents[4]


class InventoryTests(unittest.TestCase):
    def test_duplicate_or_changed_active_inventory_is_rejected(self):
        rows = [{"project": "Lang", "bug_id": 1, "owner": "aom"}]
        with self.assertRaises(ValueError):
            inventory.validate_inventory(rows + rows, expected_bugs=2, expected_projects=1)
        with self.assertRaises(ValueError):
            inventory.compare_installed(rows, {"Lang": [2]})

    def test_repository_ownership_has_exact_counts_and_four_unique_jobs(self):
        rows = json.loads((ROOT / "experiments/configs/api854-20261003/ownership.json").read_text())["bugs"]
        checked = inventory.validate_inventory(rows)
        jobs = inventory.build_jobs(checked, "a" * 64)
        self.assertEqual(len(jobs), 3416)
        self.assertEqual(len({j["job_id"] for j in jobs}), 3416)
        self.assertEqual(sum(j["owner"] == "aom" for j in jobs), 1136)
        self.assertEqual(inventory.build_jobs(list(reversed(checked)), "a" * 64), jobs)

    def test_pilot_has_20_bugs_and_all_projects_before_outcomes(self):
        rows = json.loads((ROOT / "experiments/configs/api854-20261003/ownership.json").read_text())["bugs"]
        pilot = inventory.select_pilot(rows)
        self.assertEqual(len(pilot), 20)
        self.assertEqual(len({r["project"] for r in pilot}), 17)
        self.assertEqual(sum(r["project"] == "Closure" for r in pilot), 2)
        self.assertEqual(sum(r["project"] == "JxPath" for r in pilot), 2)


class QueueTests(unittest.TestCase):
    def setUp(self):
        self.tmp = tempfile.TemporaryDirectory()
        self.addCleanup(self.tmp.cleanup)
        self.now = 1000.0
        self.q = queue.Queue(Path(self.tmp.name) / "state.sqlite", clock=lambda: self.now)
        self.jobs = inventory.build_jobs([{"project": "Lang", "bug_id": 1, "owner": "aom"}], "a" * 64)
        self.q.seed(self.jobs, active=True)  # Test-only explicit activation; CLI verifies gates.

    def claim(self, worker="w1", stage="generate"):
        return self.q.claim(stage, worker, lease_seconds=10)

    def test_simultaneous_claims_and_restart_do_not_duplicate(self):
        with ThreadPoolExecutor(max_workers=8) as pool:
            claims = list(pool.map(lambda n: self.claim(str(n)), range(8)))
        actual = [c for c in claims if c]
        self.assertEqual(len(actual), 4)
        self.assertEqual(len({c["job_id"] for c in actual}), 4)
        restarted = queue.Queue(self.q.path, clock=lambda: self.now)
        self.assertIsNone(restarted.claim("generate", "new"))
        self.assertEqual(restarted.seed(self.jobs, active=True), 0)

    def test_stale_lease_cannot_renew_or_finish_reclaimed_job(self):
        claim = self.claim()
        self.now += 11
        self.q.release_expired()
        replacement = self.claim("w2")
        self.assertEqual(replacement["job_id"], claim["job_id"])
        with self.assertRaises(ValueError):
            self.q.renew(claim["job_id"], claim["lease"], "w1")

    def test_uncertain_generation_is_quarantined_after_crash(self):
        claim = self.claim()
        attempt = self.q.start(claim["job_id"], claim["lease"], "w1")
        self.q.dispatched(claim["job_id"], claim["lease"], "w1", attempt)
        self.now += 11
        self.q.release_expired()
        row = next(r for r in self.q.snapshot()["jobs"] if r["job_id"] == claim["job_id"])
        self.assertEqual(row["status"], "needs_reconciliation")
        self.assertTrue(row["attempted"])
        self.assertEqual(self.q.snapshot()["attempts"][0]["status"], "unknown")
        self.assertEqual(len([self.claim(str(n)) for n in range(3)]), 3)
        self.assertIsNone(self.claim("last"))

    def test_unstarted_work_is_never_counted_as_failure(self):
        summary = report.summarize(self.q.snapshot())
        self.assertEqual(summary["attempted_keys"], 0)
        self.assertEqual(summary["not_attempted_keys"], 4)
        self.assertEqual(summary["terminal_outcomes"], 0)
        self.assertEqual(summary["matched_usable_bugs"], 0)

    def test_failure_requires_dispatch_and_evidence(self):
        claim = self.claim()
        attempt = self.q.start(claim["job_id"], claim["lease"], "w1")
        with self.assertRaises(ValueError):
            self.q.fail(claim["job_id"], claim["lease"], "w1", attempt,
                        "generation_failed", {"reason": "no key"})
        self.q.dispatched(claim["job_id"], claim["lease"], "w1", attempt)
        evidence = self.artifact("failure.txt", b"observed failure")
        with self.assertRaises(ValueError):
            self.q.fail(claim["job_id"], claim["lease"], "w1", attempt,
                        "generation_failed", {"reason": "wrong condition", "protocol_hash": "b" * 64, "artifacts": [evidence]})
        self.q.fail(claim["job_id"], claim["lease"], "w1", attempt,
                    "generation_failed", {"reason": "observed failure", "protocol_hash": "a" * 64, "artifacts": [evidence]})
        self.assertEqual(report.summarize(self.q.snapshot())["terminal_outcomes"], 1)

    def artifact(self, name, data):
        path = Path(self.tmp.name) / name
        path.write_bytes(data)
        return {"path": str(path), "sha256": inventory.file_hash(path)}

    def test_usable_requires_same_suite_two_fixed_runs_and_measurements(self):
        claim = self.claim()
        attempt = self.q.start(claim["job_id"], claim["lease"], "w1")
        self.q.dispatched(claim["job_id"], claim["lease"], "w1", attempt)
        suite = self.artifact("suite.java", b"assertEquals(1, value());")
        source = self.artifact("fixed-source.java", b"fixed source")
        generated = {"artifacts": [suite, source], "source_hash": source["sha256"],
                     "suite_hash": suite["sha256"], "suite_path": suite["path"],
                     "protocol_hash": "a" * 64, "test_methods": 1}
        self.q.complete(claim["job_id"], claim["lease"], "w1", attempt, generated)
        evaluation = self.claim(stage="evaluate")
        ev_attempt = self.q.start(evaluation["job_id"], evaluation["lease"], "w1")
        self.q.dispatched(evaluation["job_id"], evaluation["lease"], "w1", ev_attempt)
        bad = {**generated, "fixed_passes": [True], "meaningful_assertions": True,
               "target_executed": True, "buggy_measured": True, "coverage_measured": True,
               "fault_detected": False, "line_covered": 0, "line_total": 10,
               "condition_covered": 0, "condition_total": 4}
        with self.assertRaises(ValueError):
            self.q.complete(evaluation["job_id"], evaluation["lease"], "w1", ev_attempt, bad)
        logs = [self.artifact(name + ".txt", name.encode()) for name in ("fixed1", "fixed2", "buggy", "coverage", "validity")]
        roles = {name: a["sha256"] for name, a in zip(("fixed1", "fixed2", "buggy", "coverage", "validity"), logs)}
        with self.assertRaises(ValueError):
            self.q.complete(evaluation["job_id"], evaluation["lease"], "w1", ev_attempt,
                            {**bad, "fixed_passes": [True, True]})
        self.q.complete(evaluation["job_id"], evaluation["lease"], "w1", ev_attempt,
                        {**bad, "fixed_passes": [True, True], "compile_valid": True,
                         "artifacts": [suite, source, *logs], "evidence_roles": roles})
        summary = report.summarize(self.q.snapshot())
        self.assertEqual(summary["usable_keys"], 1)
        self.assertEqual(summary["attempted_keys"], 1)
        self.assertEqual(summary["matched_usable_bugs"], 0)
        self.assertEqual(summary["by_approach"]["cmaes"]["coverage"]["line_ratio_micro"], 0.0)
        self.assertIsNone(summary["by_approach"]["kku-claude"]["coverage"]["line_ratio_micro"])

    def test_claimed_only_seed_is_held_until_activation(self):
        held = queue.Queue(Path(self.tmp.name) / "held.sqlite", clock=lambda: self.now)
        held.seed(self.jobs)
        self.assertIsNone(held.claim("generate", "w"))

    def test_model_cannot_change_silently_even_when_response_agrees(self):
        protocol = {"kku": {"exact_model_ids": {"kku-claude": "haiku-frozen", "kku-gemini": "flash-lite-frozen"}}}
        ph = inventory.canonical_hash(protocol)
        q = queue.Queue(Path(self.tmp.name) / "model.sqlite", clock=lambda: self.now)
        q.register_protocol(protocol)
        jobs = inventory.build_jobs([{"project": "Lang", "bug_id": 1, "owner": "aom"}], ph)
        q.seed([j for j in jobs if j["approach"] == "kku-claude"], active=True)
        c = q.claim("generate", "w")
        a = q.start(c["job_id"], c["lease"], "w")
        q.dispatched(c["job_id"], c["lease"], "w", a)
        suite = self.artifact("model-suite.java", b"assertTrue(result);")
        source = self.artifact("model-source.java", b"fixed source")
        raw = self.artifact("model-response.json", b'{"choices": ["assertTrue(result);"]}')
        prompt = self.artifact("model-prompt.txt", b"fixed-only prompt")
        request = self.artifact("model-request.json", b'{"model": "haiku-frozen"}')
        record = {"artifacts": [suite, source, raw, prompt, request], "suite_path": suite["path"],
                  "suite_hash": suite["sha256"], "source_hash": source["sha256"],
                  "protocol_hash": ph, "test_methods": 1, "account_alias": "a01",
                  "requested_model": "other-haiku", "actual_model": "other-haiku",
                  "prompt_hash": prompt["sha256"], "request_hash": request["sha256"],
                  "raw_response_hash": raw["sha256"], "response_id": "test-id",
                  "usage": {"total_tokens": 9}, "model_quota": None}
        with self.assertRaises(ValueError):
            q.complete(c["job_id"], c["lease"], "w", a, record)
        with self.assertRaises(ValueError):
            q.complete(c["job_id"], c["lease"], "w", a,
                       {**record, "requested_model": "haiku-frozen", "actual_model": "haiku-frozen",
                        "artifacts": [suite, source, raw]})
        q.complete(c["job_id"], c["lease"], "w", a,
                   {**record, "requested_model": "haiku-frozen", "actual_model": "haiku-frozen"})

    def test_metadata_cannot_persist_credentials(self):
        c = self.claim()
        with self.assertRaises(ValueError):
            self.q.start(c["job_id"], c["lease"], "w1", {"headers": {"Authorization": "Bearer private"}})

    def test_semantic_generation_failures_cannot_be_retried_in_primary_condition(self):
        c = self.claim()
        a = self.q.start(c["job_id"], c["lease"], "w1")
        self.q.dispatched(c["job_id"], c["lease"], "w1", a)
        evidence = self.artifact("semantic-failure.txt", b"observed invalid suite")
        self.q.fail(c["job_id"], c["lease"], "w1", a, "invalid_suite",
                    {"reason": "empty suite", "protocol_hash": "a" * 64, "artifacts": [evidence]})
        with self.assertRaises(ValueError):
            self.q.requeue(c["job_id"], "try again", [evidence])

    def test_crash_response_can_be_recovered_without_new_generation_attempt(self):
        c = self.claim()
        a = self.q.start(c["job_id"], c["lease"], "w1")
        self.q.dispatched(c["job_id"], c["lease"], "w1", a)
        self.now += 11
        self.q.release_expired()
        suite = self.artifact("recovered.java", b"assertTrue(result);")
        source = self.artifact("recovered-source.java", b"fixed source")
        proof = self.artifact("stopped-worker.txt", b"old worker stopped; response recovered")
        record = {"protocol_hash": "a" * 64, "suite_hash": suite["sha256"],
                  "source_hash": source["sha256"], "suite_path": suite["path"],
                  "test_methods": 1, "artifacts": [suite, source]}
        self.q.recover(c["job_id"], a, "generated", record, "recovered existing output", [proof])
        self.assertEqual(len(self.q.snapshot()["attempts"]), 1)
        self.assertEqual(self.q.claim("evaluate", "w2")["job_id"], c["job_id"])

    def test_worker_stopped_does_not_prove_provider_did_not_process_request(self):
        c = self.claim()
        a = self.q.start(c["job_id"], c["lease"], "w1")
        self.q.dispatched(c["job_id"], c["lease"], "w1", a)
        self.now += 11
        self.q.release_expired()
        proof = self.artifact("stopped.txt", b"client stopped but server outcome unknown")
        with self.assertRaises(ValueError):
            self.q.requeue(c["job_id"], "client stopped", [proof], "confirmed_execution_stopped")


if __name__ == "__main__":
    unittest.main()
