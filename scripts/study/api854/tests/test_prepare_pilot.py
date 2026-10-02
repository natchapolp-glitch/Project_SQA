import hashlib
import json
from pathlib import Path
import tempfile
import unittest

from scripts.study.api854.prepare_pilot import prepare, select_files, CONTEXT_POLICY, PROMPT_POLICY
from scripts.study.api854.api_worker import resolve_prepared_job
from types import SimpleNamespace


class PreparePilotTests(unittest.TestCase):
    def test_real_artifact_bytes_are_accepted_by_worker_without_generation(self):
        with tempfile.TemporaryDirectory() as directory:
            root = Path(directory)
            fixed = root / "fixed"
            source = fixed / "src/main/java/p/Example.java"
            source.parent.mkdir(parents=True)
            source.write_text("package p; public class Example { public int value() { return 1; } }", encoding="utf-8")
            (fixed / ".defects4j.config").write_text("pid=Lang\nvid=2f\n", encoding="utf-8")
            (fixed / "coverage.xml").write_text("PRIVATE_EXECUTION_LOG", encoding="utf-8")
            output = root / "artifacts"
            metadata = prepare(fixed, "Lang", 2, ["p.Example"], output)
            prompt = (output / "prompt.md").read_bytes()
            self.assertNotIn(b"PRIVATE_EXECUTION_LOG", prompt)
            artifacts = []
            contents = {}
            for name in ("context-manifest.json", "prompt.md"):
                data = (output / name).read_bytes()
                contents[name] = data
                artifacts.append({"name": name, "size": len(data), "sha256": hashlib.sha256(data).hexdigest()})
            class Client:
                def download(self, artifact):
                    return contents[artifact["name"]]
            job = {"run_id": "mock-prepare", "project": "Lang", "bug_id": 2, "approach": "kku-claude",
                   "owner": "champ", "stage": "generate", "protocol_hash": "a" * 64,
                   "payload": {"stage_history": [{"stage": "prepare", "outcome": "prepared",
                       "artifacts": artifacts, "metadata": metadata}]}}
            settings = SimpleNamespace(models={"kku-claude": {}}, protocol_hash="a" * 64, context_policy_id=CONTEXT_POLICY,
                                       prompt_policy_id=PROMPT_POLICY, prompt_token_reserve=len(prompt))
            resolved = resolve_prepared_job(Client(), job, settings)
            self.assertEqual(resolved.prompt.encode("utf-8"), prompt)
            checksums = json.loads((output / "checksums.json").read_text(encoding="utf-8"))
            for name, expected in checksums.items():
                self.assertEqual(hashlib.sha256((output / name).read_bytes()).hexdigest(), expected)
            with self.assertRaises(FileExistsError):
                prepare(fixed, "Lang", 2, ["p.Example"], output)

    def test_missing_or_ambiguous_target_source_is_not_guessed(self):
        with tempfile.TemporaryDirectory() as directory:
            root = Path(directory)
            with self.assertRaises(ValueError):
                select_files(root, ["p.Example"])
            for prefix in ("src", "other"):
                source = root / prefix / "p/Example.java"
                source.parent.mkdir(parents=True)
                source.write_text("class Example {}", encoding="utf-8")
            with self.assertRaises(ValueError):
                select_files(root, ["p.Example"])
