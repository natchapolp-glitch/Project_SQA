"""Recreate ignored, immutable received sources from pinned Git blobs only.

Read-only for already present files; never overwrites a differing file or proof.
No API, queue or environment credentials are used.
"""
from pathlib import Path
import hashlib,io,json,subprocess,tarfile
BASE=Path(__file__).resolve().parent;ROOT=BASE.parents[2]
info=json.loads((BASE/'snapshot-provenance.json').read_text(encoding='utf-8'))
SNAP=(ROOT/info['private_snapshot']).resolve()
assert SNAP.is_relative_to((ROOT/'.local/api854').resolve())
rows=info['files']+json.loads((BASE/'snapshot-supplement-provenance.json').read_text(encoding='utf-8'))
required={r['source_path']:r['sha256'] for r in rows}
paths=info['archive_paths']+[r['source_path'] for r in rows if r['source_path'] not in {i['source_path'] for i in info['files']}]
archive=subprocess.check_output(['git','archive',info['source_commit'],*paths],cwd=ROOT)
restored=0;seen=set()
with tarfile.open(fileobj=io.BytesIO(archive)) as t:
    for member in t.getmembers():
        if not member.isfile():continue
        name=member.name;assert name in required
        raw=t.extractfile(member).read();assert hashlib.sha256(raw).hexdigest()==required[name]
        target=(SNAP/name).resolve();assert target.is_relative_to(SNAP)
        if target.exists():assert target.read_bytes()==raw,name
        else:
            target.parent.mkdir(parents=True,exist_ok=True)
            with target.open('xb') as f:f.write(raw)
            restored+=1
        seen.add(name)
assert seen==set(required)
print(json.dumps({'git_snapshot_files_verified':len(seen),'missing_files_restored':restored}))
