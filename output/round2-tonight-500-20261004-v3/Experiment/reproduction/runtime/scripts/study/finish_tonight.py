#!/usr/bin/env python3
"""Build a delivery when dispatch ends or at the declared evening cutoff."""
import argparse
from datetime import datetime, timezone
import hashlib
import os
from pathlib import Path
import shutil
import subprocess
import time
import zipfile
import solo_batch as batch
from build_tonight_snapshot import build
from author_tonight_report import author
from verify_tonight_snapshot import verify


def finish(args):
    root = args.output.resolve()
    build(root, args.offline.resolve(), [p.resolve() for p in args.ai], args.scope)
    author(root)
    shutil.copyfile(Path(__file__).with_name('tonight_demo.py'), root / 'Presentation/demo.py')
    guide = Path(__file__).with_name('tonight_demo_guide.md')
    shutil.copyfile(guide, root / 'Presentation/demo-guide.md')
    rehearsal = args.control / 'diagnostics/demo-rehearsal'
    if rehearsal.is_dir():
        shutil.copytree(rehearsal, root / 'Presentation/rehearsal')
    else:
        (root / 'Presentation/REHEARSAL_PENDING.md').write_text('Live archive replay has not yet been rehearsed. Stored-result display remains available without AI.\n', encoding='utf-8')
    env = {**os.environ, 'RUNTIME_NODE_MODULES': str(args.node_modules.resolve()),
           'RUNTIME_PYTHON': str(Path(__import__('sys').executable).resolve())}
    subprocess.run([str(args.node), str(args.deck_builder), str(root)], check=True, env=env)
    verification = verify(root)
    batch.write(root / 'Experiment/package-verification.json', verification)
    sums = root / 'SHA256SUMS.txt'
    files = sorted(p for p in root.rglob('*') if p.is_file() and p != sums)
    sums.write_text(''.join(f'{batch.sha(p)}  {p.relative_to(root).as_posix()}\n' for p in files), encoding='utf-8')
    if args.zip.exists():
        raise ValueError('ZIP already exists; preserve old package')
    with zipfile.ZipFile(args.zip, 'w', compression=zipfile.ZIP_DEFLATED, compresslevel=6) as archive:
        for p in [*files, sums]:
            archive.write(p, p.relative_to(root).as_posix())
    with zipfile.ZipFile(args.zip) as archive:
        if archive.testzip() is not None or len(archive.namelist()) != len(files) + 1:
            raise ValueError('ZIP CRC/member count failure')
    batch.write(args.control / (root.name + '-delivery.json'), {'snapshot': str(root), 'zip': str(args.zip.resolve()),
                'zip_sha256': batch.sha(args.zip), 'verification': verification,
                'not_a_completion_claim': True, 'visually_review_latest_rendered_pages_and_slides_before_submission': True})
    if args.publish_team:
        repo = batch.ROOT.resolve()
        files_to_publish = [root, args.zip.resolve(),
                            (args.control / (root.name + '-delivery.json')).resolve()]
        if not all(p.is_relative_to(repo) for p in files_to_publish):
            raise ValueError('Publication path outside repository')
        branch = subprocess.check_output(['git', 'branch', '--show-current'], cwd=repo, text=True).strip()
        staged = subprocess.check_output(['git', 'diff', '--cached', '--name-only'], cwd=repo, text=True).strip()
        if branch != 'Team' or staged:
            raise ValueError('Automatic publish requires Team and no pre-existing staged work; delivery retained locally')
        subprocess.run(['git', 'add', '--', *[str(p.relative_to(repo)) for p in files_to_publish]], cwd=repo, check=True)
        subprocess.run(['git', 'commit', '-q', '-m', 'Capture evening results for selected 500-bug scope'], cwd=repo, check=True)
        subprocess.run(['git', 'push', 'origin', 'HEAD:Team'], cwd=repo, check=True)
    print({'zip': str(args.zip), 'verification': verification}, flush=True)


def main():
    cli = argparse.ArgumentParser(description=__doc__)
    for name in ('offline', 'scope', 'control', 'output', 'zip', 'node', 'node-modules', 'deck-builder'):
        cli.add_argument('--' + name, type=Path, required=True)
    cli.add_argument('--ai', type=Path, nargs='+', required=True)
    cli.add_argument('--at', default='2026-10-04T15:00:00+00:00')
    cli.add_argument('--now', action='store_true')
    cli.add_argument('--publish-team', action='store_true')
    args = cli.parse_args()
    deadline = datetime.fromisoformat(args.at)
    while not args.now and not (args.control / 'dispatch-ended.json').exists() and datetime.now(timezone.utc) < deadline:
        time.sleep(20)
    finish(args)


if __name__ == '__main__':
    main()
