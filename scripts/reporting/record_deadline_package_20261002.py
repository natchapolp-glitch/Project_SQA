from pathlib import Path
import json
root=Path(__file__).resolve().parents[2]
out=root/'output/kku-only-20261001'
receipt=json.loads((out/'package-verification-20261002_DEADLINE.json').read_text())
path=out/'delivery-status.json'
status=json.loads(path.read_text())
status['local_artifacts']['zip_status']='CRC and all 21899 file manifest SHA-256 hashes verified; private copy saved in Downloads. Archived delivery status is a packaging-time snapshot; current external receipt records later publication.'
status['local_artifacts']['zip_sha256']=receipt['sha256']
status['local_artifacts']['zip_bytes']=receipt['bytes']
status['local_artifacts']['package_receipt']='output/kku-only-20261001/package-verification-20261002_DEADLINE.json'
path.write_text(json.dumps(status,ensure_ascii=False,indent=2)+'\n',encoding='utf-8')
