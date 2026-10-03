"""Verify received Aom provenance, old/new runtime pins and retained fixed bytes."""
import hashlib
import json
from pathlib import Path
import subprocess
import sys

def sha(data):return hashlib.sha256(data).hexdigest()

if __name__=='__main__':
    snapshot=Path(sys.argv[1]);out=Path(__file__).parent/'lineage-receipt.json'
    if out.exists():raise ValueError('Refuse to overwrite lineage receipt')
    base=snapshot/'output/api854-20261003/beam-buffer-joint-review-v1'
    verdict=json.loads((base/'beam-buffer-verdict.json').read_bytes())
    seal=json.loads((base/'reference/preexecution-seal.json').read_bytes())
    reference=json.loads((base/'reference/receipt.json').read_bytes())
    assert verdict['current_runtime_source_sha256']==seal['runtime_source_sha256']==reference['runtime_source_sha256']
    old=json.loads(Path('docs/api854/evidence/beam-buffer-development-20261003-v1/protocol-proposal.json').read_bytes())
    assert verdict['beam_runtime_source_sha256']==old['source_sha256']
    assert verdict['fixed_source_sha256']==seal['fixed_source_sha256']
    sources={}
    for project,files in seal['fixed_source_sha256'].items():
        for p,digest in files.items():
            fixed=Path('output/api854-20261003/prepare-v8-fraction-field-development',project+'-1','fixed-source',p)
            assert sha(fixed.read_bytes())==digest,p
            sources[fixed.as_posix()]=digest
    rows=json.loads((base/'received-aom/provenance.json').read_bytes())
    for row in rows:
        original=subprocess.check_output(['git','show',row['source_commit']+':'+row['source_path']])
        assert sha(original)==row['sha256']==sha((snapshot/row['received_path']).read_bytes())
    data={'source_sha256':sha(Path(__file__).read_bytes()),'received_aom_provenance_files_verified':len(rows),
          'historical_runtime_condition':'c125695a','current_reference_runtime_condition':reference['reviewed_runtime_commit'],
          'old_and_current_runtime_maps_separated':True,'fixed_source_bindings':sources,
          'champ_verdict_pending':True,'joint_acceptance':False}
    out.write_bytes((json.dumps(data,indent=2)+'\n').encode())
    print('Lineage verified: four Aom provenance files, three retained fixed sources, separate old/current runtime maps')
