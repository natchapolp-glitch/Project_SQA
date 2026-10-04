#!/usr/bin/env python3
"""Create a new immutable submission snapshot from already sealed outcomes."""
import argparse
from pathlib import Path
import shutil

import nightly_report as report
import solo_batch as batch


MEMBERS = [{"name": "นายธนินธร อันทรบุตร", "id": "673380043-6"},
           {"name": "นายศุภกร กรมรินทร์", "id": "673380061-4"},
           {"name": "นายณัชพล เพ็งพล", "id": "673380267-4"},
           {"name": "นายณัฐกรณ์ อินธิสาร", "id": "673380268-2"}]


def copy_tree(source, destination, runtime=False):
    omitted = ['*.pyc', '__pycache__', '*.tmp', '.local', '*.private.json']
    if runtime:
        omitted += ['tests']  # Developer mocks contain synthetic credential strings; no generation dependency.
    shutil.copytree(source, destination, ignore=shutil.ignore_patterns(*omitted))


def build(output, offline, ais, scope=None):
    output.mkdir(parents=True, exist_ok=False)
    rows, stats = report.write_report(offline, ais, output / "Report", scope)
    sources = {"offline": offline, **{p.name: p for p in ais}}
    entries = []
    cases = sorted({r["case"] for r in rows if r["attempted"] is True})
    for condition, source in sources.items():
        destination = output / "Experiment/evidence" / condition
        for name in ("Experiment/protocol", "Experiment/automation/runtime"):
            if (source / name).is_dir():
                copy_tree(source / name, destination / name)
        plan = source / "Experiment/batch-plan.json"
        if plan.is_file():
            shutil.copyfile(plan, destination / "Experiment/batch-plan.json")
    for row in rows:
        if row["attempted"] is not True:
            continue
        condition, case, method = row["condition"], row["case"], row["method"]
        source = sources[condition]
        job = Path(row["result_path"]).parent
        saved = output / "Experiment/evaluations" / case / method / condition / "run-final"
        copy_tree(job, saved)
        row["result_path"] = (saved / "outcome.json").relative_to(output).as_posix()
        entry = {"case": case, "method": method, "condition": condition, "state": row["state"],
                 "protocol_sha256": row["protocol_hash"], "outcome": row["result_path"], "archive": None}
        record = batch.read(job / "outcome.json")
        if row["state"] == "DONE":
            measurement = source / record["selected_measurement"] if method.startswith("kku-") else job / "measurement"
            archive_record = batch.read(measurement / "record.json")
            archive = measurement / Path(archive_record["suite_path"]).name
            copied_measurement = saved / measurement.relative_to(job)
            entry.update(archive=(copied_measurement / archive.name).relative_to(output).as_posix(),
                         archive_sha256=batch.sha(archive), generated_test_methods=row["generated_test_methods"],
                         instrument_classes=archive_record["instrument_classes"])
        entries.append(entry)
        folder = batch.METHODS[method]
        if method.startswith("kku-"):
            for name in ("Prompt", "Result", "TestCode"):
                candidate = source / folder / name / case
                if candidate.is_dir():
                    copy_tree(candidate, output / folder / name / case)
            templates = output / folder / "Prompt/templates" / condition
            templates.mkdir(parents=True, exist_ok=True)
            for file in (source / folder / "Prompt").glob("P*.txt"):
                shutil.copyfile(file, templates / file.name)
        else:
            for name in ("Result_Round1", "Test"):
                candidate = source / folder / name / case
                if candidate.is_dir():
                    copy_tree(candidate, output / folder / name / case)
    # Shared input artifacts: copy only complete, sealed preparations for reported cases.
    for case in cases:
        for prefix in ("Experiment/contexts", "Experiment/algorithm-targets"):
            source = offline / prefix / case
            if (source / "receipt.json").exists():
                batch.verify(source)
                copy_tree(source, output / prefix / case)
    for method in ("cmaes", "fscs-art"):
        folder = output / batch.METHODS[method]
        for name in ("Code", "Configuration", "Result_Round2"):
            (folder / name).mkdir(parents=True, exist_ok=True)
        for file in (batch.ROOT / "algorithms/python/atcg").glob("*.py"):
            shutil.copyfile(file, folder / "Code" / file.name)
        shutil.copyfile(batch.ROOT / "algorithms/java/SqaProbe.java", folder / "Code/SqaProbe.java")
        shutil.copyfile(offline / "Experiment/protocol/offline.json", folder / "Configuration/protocol.json")
        (folder / "Result_Round2/README.md").write_text("No second independent generation round was performed. Fixed repeats validate the same suite.\n", encoding="utf-8")
    # Keep executable repository-relative runtime layout for fresh experiments/replays.
    for prefix in ("scripts/study", "algorithms/python/atcg", "algorithms/java"):
        copy_tree(batch.ROOT / prefix, output / "Experiment/reproduction/runtime" / prefix, runtime=True)
    for prefix in ('experiments/configs/api854-20261003', 'docs/api854/plans/friend-layout-full-v1'):
        copy_tree(batch.ROOT / prefix, output / 'Experiment/reproduction/runtime' / prefix)
    shutil.copyfile(batch.ROOT / "experiments/configs/api854-20261003/installed-active-bugs.json",
                    output / "Experiment/reproduction/installed-active-bugs.json")
    import csv
    selected_cases = {r['case'] for r in rows}
    for file in ("cases.csv", "jobs.csv"):
        source = offline / 'Experiment' / file
        shutil.copyfile(source, output / 'Experiment' / ('full-inventory-' + file))
        with source.open(encoding='utf-8', newline='') as stream:
            reader = csv.DictReader(stream)
            fieldnames = reader.fieldnames
            records = [r for r in reader if r['case'] in selected_cases]
        with (output / 'Experiment' / file).open('w', encoding='utf-8', newline='') as stream:
            writer = csv.DictWriter(stream, fieldnames=fieldnames)
            writer.writeheader()
            writer.writerows(records)
    if scope:
        shutil.copyfile(scope, output / 'Experiment/selected-scope.json')
    batch.write(output / "Experiment/snapshot.json", {"schema": "tonight-delivery.v1", "members": MEMBERS,
                "summary": stats, "results": entries, "source_roots": {n: str(p) for n, p in sources.items()},
                "source_code_sha256": batch.sha(Path(__file__)), "repository_branch": "Team",
                "old_fixed_assisted_csv_pooled": False, "no_keys_packaged": True})
    for row in rows:
        if row["result_path"] and Path(row["result_path"]).is_absolute():
            raise ValueError("Untranslated result path")
    with (output / "Report/data/final_comparison.csv").open("w", encoding="utf-8", newline="") as stream:
        writer = csv.DictWriter(stream, fieldnames=batch.FIELDS + report.EXTRA)
        writer.writeheader()
        writer.writerows(rows)
    (output / "Presentation").mkdir(exist_ok=True)
    (output / "Experiment/archive").mkdir(exist_ok=True)
    (output / "Experiment/archive/README.md").write_text(
        "Historical source evidence remains in the original repository/Team history. Fixed-assisted single-bug results and old shared queue evidence were not pooled into this snapshot.\n", encoding="utf-8")
    print({"snapshot": str(output), "recorded_bugs": stats["recorded_bugs"], "states": stats["states"]})
    return stats


if __name__ == "__main__":
    cli = argparse.ArgumentParser(description=__doc__)
    cli.add_argument("--output", type=Path, required=True)
    cli.add_argument("--offline", type=Path, required=True)
    cli.add_argument("--ai", nargs="+", type=Path, required=True)
    cli.add_argument('--scope', type=Path)
    args = cli.parse_args()
    build(args.output.resolve(), args.offline.resolve(), [p.resolve() for p in args.ai], args.scope)
