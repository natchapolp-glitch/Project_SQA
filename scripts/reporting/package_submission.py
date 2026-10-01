#!/usr/bin/env python3
"""Package source, reports and raw evidence; no remote upload is performed."""
from __future__ import annotations
import argparse
import hashlib
import json
from pathlib import Path
import zipfile

ROOT = Path(__file__).resolve().parents[2]
DIRECTORIES = ('algorithms', 'ai-tests', 'dataset', 'docs', 'experiments', 'output/submission',
               'presentation', 'prompts', 'results', 'scripts')


def included(path):
    relative = path.relative_to(ROOT)
    return not any(part in ('__pycache__', 'node_modules', '.venv', '.git', 'tmp') or
                   part.startswith('.chart-data-') for part in relative.parts) and not (
                       path.name.startswith('.env') or path.suffix in ('.pyc', '.zip'))


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--output', type=Path, default=ROOT / 'output/SQA_Round2_Submission.zip')
    args = parser.parse_args()
    output = args.output.resolve()
    if not output.is_relative_to(ROOT / 'output'):
        parser.error('Output must stay inside this repository output directory')
    files = {p for directory in DIRECTORIES for p in (ROOT / directory).rglob('*') if p.is_file() and included(p)}
    files |= {ROOT / name for name in ('README.md', '.gitignore', 'active-bugs.csv', 'deprecated-bugs.csv') if (ROOT / name).is_file()}
    manifest = [{'path': path.relative_to(ROOT).as_posix(), 'bytes': path.stat().st_size,
                 'sha256': hashlib.sha256(path.read_bytes()).hexdigest()} for path in sorted(files)]
    output.parent.mkdir(exist_ok=True)
    temporary = output.with_suffix('.building.zip')
    with zipfile.ZipFile(temporary, 'w', compression=zipfile.ZIP_DEFLATED, compresslevel=6) as archive:
        for path in sorted(files):
            archive.write(path, 'Project_SQA/' + path.relative_to(ROOT).as_posix())
        archive.writestr('Project_SQA/SUBMISSION_FILE_MANIFEST.json', json.dumps(manifest, indent=2))
    with zipfile.ZipFile(temporary) as archive:
        bad = archive.testzip()
        if bad:
            raise ValueError('ZIP CRC check failed: ' + bad)
    temporary.replace(output)
    checksum = hashlib.sha256(output.read_bytes()).hexdigest()
    output.with_suffix('.zip.sha256').write_text(checksum + '  ' + output.name + '\n')
    print(json.dumps({'path': str(output), 'files': len(files), 'bytes': output.stat().st_size, 'sha256': checksum}))


if __name__ == '__main__':
    main()
