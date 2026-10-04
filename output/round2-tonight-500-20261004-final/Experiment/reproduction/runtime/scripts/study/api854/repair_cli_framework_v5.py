#!/usr/bin/env python3
"""Patch the Defects4J Cli framework JUnit path and record v5 evidence.

Framework-only repair for the v5 pilot: Cli evaluations fail before any test
runs with NoClassDefFoundError org/hamcrest/SelfDescribing because
Cli.build.xml points at junit-4.12.jar instead of the bundled
junit-4.12-hamcrest-1.3.jar. This never touches production sources or
generated assertions. Superseded v5-1 failures stay immutable under
results/validation; only paths are referenced here, never moved or edited.
"""
from __future__ import annotations

import argparse
import datetime
import hashlib
import json
import zipfile
from pathlib import Path

OLD = b'file://${d4j.home}/framework/projects/lib/junit-4.12.jar'
NEW = b'file://${d4j.home}/framework/projects/lib/junit-4.12-hamcrest-1.3.jar'


def sha(path: Path) -> str:
    return hashlib.sha256(path.read_bytes()).hexdigest()


def main() -> None:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--d4j-home', required=True, type=Path)
    parser.add_argument('--evidence-dir', required=True, type=Path)
    parser.add_argument('--superseded', nargs='*', default=[])
    args = parser.parse_args()

    build = args.d4j_home / 'framework/projects/Cli/Cli.build.xml'
    jar = args.d4j_home / 'framework/projects/lib/junit-4.12-hamcrest-1.3.jar'
    with zipfile.ZipFile(jar) as archive:
        if 'org/hamcrest/SelfDescribing.class' not in archive.namelist():
            raise ValueError('Bundled JUnit/Hamcrest jar lacks the missing class')
    before = build.read_bytes()
    out = args.evidence_dir
    out.mkdir(parents=True, exist_ok=False)
    if before.count(OLD) == 1:
        (out / 'Cli.build.original.xml').write_bytes(before)
        after = before.replace(OLD, NEW, 1)
        build.write_bytes(after)
        (out / 'Cli.build.patched.xml').write_bytes(after)
        repaired = True
    elif before.count(NEW) == 1 and before.count(OLD) == 0:
        after = before
        (out / 'Cli.build.patched.xml').write_bytes(after)
        repaired = False
    else:
        raise ValueError('Cli build differs from the expected original or patched path')
    for name in args.superseded:
        if not Path(name).is_file():
            raise ValueError(f'Superseded evidence missing: {name}')
    (out / 'repair.json').write_text(json.dumps({
        'repaired_at_utc': datetime.datetime.now(datetime.timezone.utc).isoformat(),
        'project': 'Cli',
        'changed_path': str(build),
        'reason': 'JUnit 4 runner failed before executing tests: NoClassDefFoundError org/hamcrest/SelfDescribing',
        'before_sha256': hashlib.sha256((out / 'Cli.build.original.xml').read_bytes()).hexdigest()
        if repaired else None,
        'after_sha256': hashlib.sha256(after).hexdigest(),
        'already_patched': not repaired,
        'jar_path': str(jar),
        'jar_sha256': hashlib.sha256(jar.read_bytes()).hexdigest(),
        'scope': 'Framework JUnit dependency path only; same patch for fixed and buggy. Generated suites unchanged.',
        'superseded_failures_preserved': list(args.superseded),
    }, indent=2) + '\n', encoding='utf-8')
    print(f'patched={repaired} after={hashlib.sha256(after).hexdigest()}', flush=True)


if __name__ == '__main__':
    main()
