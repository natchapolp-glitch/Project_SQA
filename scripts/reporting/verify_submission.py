"""Verify every packaged byte against its manifest and current workspace."""
from pathlib import Path
import hashlib
import json
import zipfile
from datetime import datetime, timezone
from package_submission import ROOT, DIRECTORIES, included

def main():
    path = ROOT/'output/SQA_Round2_Submission.zip'
    digest = hashlib.sha256(path.read_bytes()).hexdigest()
    if path.with_suffix('.zip.sha256').read_text().split()[0] != digest:
        raise ValueError('ZIP checksum sidecar differs')
    with zipfile.ZipFile(path) as archive:
        if archive.testzip() is not None:
            raise ValueError('ZIP CRC failure')
        manifest = json.loads(archive.read('Project_SQA/SUBMISSION_FILE_MANIFEST.json'))
        names = archive.namelist()
        if len(names) != len(manifest)+1 or len(names) != len(set(names)):
            raise ValueError('Unexpected or duplicate ZIP entries')
        for entry in manifest:
            data = archive.read('Project_SQA/'+entry['path'])
            if len(data) != entry['bytes'] or hashlib.sha256(data).hexdigest() != entry['sha256']:
                raise ValueError('Manifest differs: '+entry['path'])
            source = ROOT/entry['path']
            if not included(source) or source.read_bytes() != data:
                raise ValueError('Workspace/exclusion differs: '+entry['path'])
        expected = {p.relative_to(ROOT).as_posix() for directory in DIRECTORIES
                    for p in (ROOT/directory).rglob('*') if p.is_file() and included(p)}
        expected |= {n for n in ('README.md','.gitignore','active-bugs.csv','deprecated-bugs.csv') if (ROOT/n).is_file()}
        if expected != {e['path'] for e in manifest}:
            raise ValueError('Package file set differs')
    primary = [e for e in manifest if e['path'].startswith('results/study/round2-v4-20260929/')
               and e['path'].endswith('/evaluation/record.json')]
    summary = json.loads((ROOT/'output/submission/summary.json').read_text(encoding='utf-8'))
    if len(primary) != summary['overall']['observed_runs']:
        raise ValueError('Primary record count differs from summary')
    result = {'verified_at_utc':datetime.now(timezone.utc).isoformat(),
              'files':len(manifest),'bytes':path.stat().st_size,'sha256':digest,
              'crc':'passed','manifest_hashes':'all_passed','workspace_equivalence':'passed',
              'private_exclusions':'passed','primary_records':len(primary)}
    (ROOT/'output/SQA_Round2_Submission.verification.json').write_text(json.dumps(result,indent=2),encoding='utf-8')
    print(json.dumps(result))

if __name__ == '__main__':
    main()
