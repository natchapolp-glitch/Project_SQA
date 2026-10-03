"""Run in WSL after source-only recovery; retain observed byte equality proof."""
import hashlib
import json
from pathlib import Path
import subprocess
import argparse

root=Path(__file__).resolve().parents[3]
parser=argparse.ArgumentParser(description=__doc__)
parser.add_argument('--receipt',type=Path,required=True)
args=parser.parse_args()
original=root/'output/api854-20261003/aom-owner-sources-v1'
recovery=root/'output/api854-20261003/aom-owner-sources-v2'
load=lambda p:json.loads(p.read_text())
digest=lambda p:hashlib.sha256(p.read_bytes()).hexdigest()
old_tree=Path(load(original/'config.json')['worktrees'])/'Gson/18/f'
new_tree=Path(load(recovery/'config.json')['worktrees'])/'Gson/18/f'
old_head=(original/'Gson-18/revision-head/command.log').read_text().strip()
new_head=(recovery/'Gson-18/revision-head/command.log').read_text().strip()
assert subprocess.check_output(['git','rev-parse','HEAD'],cwd=old_tree,text=True).strip()==old_head
assert subprocess.check_output(['git','rev-parse','HEAD'],cwd=new_tree,text=True).strip()==new_head
manifest=load(recovery/'Gson-18/context/context-manifest.json')
paths=[item['path'] for item in manifest['source_files']]
for tree,head in [(old_tree,old_head),(new_tree,new_head)]:
    assert not subprocess.check_output(['git','diff',head,'--',*paths],cwd=tree)
files=[]
for item in manifest['source_files']:
    assert digest(old_tree/item['path'])==digest(new_tree/item['path'])==item['sha256']
    files.append({'path':item['path'],'original_fixed_bytes_sha256':digest(old_tree/item['path']),
                  'recovered_fixed_bytes_sha256':item['sha256'],'equal':True})
proof={'scope':'Observed fixed-source byte equality for versioned Gson-18 prerequisite recovery.',
    'implementation_sha256':digest(Path(__file__)),
    'original_error':load(original/'Gson-18/record.json')['error'],
    'original_record_sha256':digest(original/'Gson-18/record.json'),
    'recovery_record_sha256':digest(recovery/'Gson-18/record.json'),
    'original_current_head_matches_saved_head':True,'recovery_current_head_matches_saved_head':True,
    'selected_files_unchanged_from_each_fixed_head':True,'files':files,
    'behavioral_source_edits':False,'primary_completed':0,'real_kku_requests':0}
out=args.receipt.resolve()
with out.open('x') as stream:stream.write(json.dumps(proof,indent=2)+'\n')
print(json.dumps(proof))
