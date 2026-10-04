"""Read-only tag-backed byte audit after all source preparations, in WSL."""
import hashlib
import json
from pathlib import Path
import subprocess
import argparse

root=Path(__file__).resolve().parents[3]
parser=argparse.ArgumentParser(description=__doc__)
parser.add_argument('--index',type=Path,default=root/'output/api854-20261003/aom-owner-sources-final/index.json')
parser.add_argument('--receipt',type=Path,required=True)
args=parser.parse_args()
index_path=args.index.resolve()
base=index_path.parent.parent
index=json.loads(index_path.read_text())
assert index['owner']=='aom' and index['expected_bugs']==284 and index['primary_completed']==0
entries=[]
problems=[]
for record in index['records']:
    if not record['source_prepared']:continue
    folder=base/record['evidence_folder']
    config=json.loads((folder.parent/'config.json').read_text())
    tree=Path(config['worktrees'])/record['project']/str(record['bug_id'])/'f'
    proof=json.loads((folder/'revision-proof.json').read_text())
    head=subprocess.check_output(['git','rev-parse','HEAD'],cwd=tree,text=True).strip()
    assert head==proof['head']
    manifest=json.loads((folder/'context/context-manifest.json').read_text())
    files=[]
    for item in manifest['source_files']:
        listed=subprocess.check_output(['git','ls-files','--',item['path']],cwd=tree,text=True).splitlines()
        tracked=item['path'] in listed
        result=subprocess.run(['git','show',head+':'+item['path']],cwd=tree,capture_output=True) if tracked else None
        if tracked:assert result.returncode==0,result.stderr.decode(errors='replace')
        observed=hashlib.sha256(result.stdout).hexdigest() if tracked else None
        matched=tracked and observed==item['sha256']
        if item['path'].endswith('.java') and not matched:
            problems.append({'project':record['project'],'bug_id':record['bug_id'],'path':item['path'],'reason':'Java bytes not proven against fixed HEAD'})
        if tracked and not matched:
            problems.append({'project':record['project'],'bug_id':record['bug_id'],'path':item['path'],'reason':'Tracked selected bytes differ from fixed HEAD'})
        files.append({'path':item['path'],'retained_sha256':item['sha256'],
            'fixed_git_object_sha256':observed,'git_tracked':tracked,'fixed_git_bytes_match':matched,
            'untracked_build_snapshot_only':not tracked and not item['path'].endswith('.java')})
    entries.append({'project':record['project'],'bug_id':record['bug_id'],'head':head,'files':files})
report={'scope':'Read-only current worktree HEAD and exact git object bytes for selected prerequisite source/build files.',
    'source_index_sha256':hashlib.sha256(index_path.read_bytes()).hexdigest(),
    'implementation_sha256':hashlib.sha256(Path(__file__).read_bytes()).hexdigest(),
    'bugs_checked':len(entries),'selected_files_checked':sum(len(e['files']) for e in entries),
    'untracked_build_snapshots':sum(f['untracked_build_snapshot_only'] for e in entries for f in e['files']),
    'problems':problems,'all_java_fixed_tag_bytes_verified':not problems,
    'entries':entries,'primary_completed':0,'real_kku_requests':0,'live_queue_mutations':0}
out=args.receipt.resolve()
with out.open('x') as stream:stream.write(json.dumps(report,indent=2)+'\n')
print(json.dumps({k:report[k] for k in ('bugs_checked','selected_files_checked','untracked_build_snapshots','problems','all_java_fixed_tag_bytes_verified')}))
if problems:raise SystemExit(1)
