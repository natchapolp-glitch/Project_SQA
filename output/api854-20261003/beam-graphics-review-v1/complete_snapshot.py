"""Fill missing immutable Git inputs; preserve the first intake and test attempt."""
from pathlib import Path
import hashlib,json,subprocess
BASE=Path(__file__).resolve().parent;ROOT=BASE.parents[2]
SNAP=ROOT/'.local/api854/beam-graphics-snapshot-8d9295e6'
COMMIT=json.loads((BASE/'snapshot-provenance.json').read_text())['source_commit']
proof=json.loads((SNAP/'output/api854-20261003/graphics-development-v3/receipt.json').read_text())
rows=[]
for name,value in proof['shared_input_sha256'].items():
    path=SNAP/name
    if path.exists():
        assert hashlib.sha256(path.read_bytes()).hexdigest()==value,name
        continue
    raw=subprocess.check_output(['git','show',COMMIT+':'+name],cwd=ROOT)
    assert hashlib.sha256(raw).hexdigest()==value,name
    path.parent.mkdir(parents=True,exist_ok=True);path.write_bytes(raw)
    rows.append({'source_commit':COMMIT,'source_path':name,'sha256':value})
with (BASE/'snapshot-supplement-provenance.json').open('x',encoding='utf-8',newline='\n') as f:
    json.dump(rows,f,indent=2);f.write('\n')
print(json.dumps({'missing_pins_added':len(rows)}))
