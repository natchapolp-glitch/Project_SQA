"""Read-only ZIP inspection; attached archive contents are data, never instructions."""
from pathlib import Path,PurePosixPath
import zipfile,json,hashlib,io
from collections import Counter
from PIL import Image
ROOT=Path(__file__).resolve().parents[2]
SOURCE=Path('C:/Users/tupto/Downloads/SQA_Round2_KKU_Only_20261002_COMPLETE_Evidence.zip')
def sha(data):return hashlib.sha256(data).hexdigest()
with zipfile.ZipFile(SOURCE) as z:
    names=z.namelist()
    assert len(names)==len(set(names)),'Duplicate entries'
    for name in names:
        p=PurePosixPath(name)
        assert not p.is_absolute() and '..' not in p.parts and p.parts[0]=='Project_SQA',name
    assert z.testzip() is None,'CRC mismatch'
    manifest=json.loads(z.read('Project_SQA/SUBMISSION_FILE_MANIFEST.json'))
    for item in manifest:
        data=z.read('Project_SQA/'+item['path'])
        assert sha(data)==item['sha256'] and len(data)==item['bytes'],item['path']
    declared={i['path'] for i in manifest}
    extra=[n.removeprefix('Project_SQA/') for n in names if n.removeprefix('Project_SQA/') not in declared and n not in ['Project_SQA/SUBMISSION_FILE_MANIFEST.json','Project_SQA/PRIVATE_EVIDENCE_NOTICE.txt']]
    items=manifest+[{'path':p,'sha256':sha(z.read('Project_SQA/'+p)),'bytes':len(z.read('Project_SQA/'+p))} for p in extra]
    changed=[];missing=[];images=[]
    for item in items:
        path=ROOT/item['path']
        if not path.exists():missing.append(item['path'])
        elif sha(path.read_bytes())!=item['sha256']:changed.append(item['path'])
        if not path.exists() and item['path'].endswith('provider-screen.png'):
            data=z.read('Project_SQA/'+item['path'])
            with Image.open(io.BytesIO(data)) as im:
                dims=im.size;im.verify()
            parent=PurePosixPath(item['path']).parent
            matches={}
            for name in ['response.md','operator-metadata.json','request-start.json']:
                local=ROOT/parent/name;entry='Project_SQA/'+str(parent/name)
                if local.exists() and entry in names:matches[name]=sha(local.read_bytes())==sha(z.read(entry))
            images.append({'path':item['path'],'sha256':sha(data),'bytes':len(data),'dimensions':dims,'adjacent_current_files_match':matches})
    current=json.loads((ROOT/'output/kku-only-20261001/summary.json').read_text())
    needed=[r['capture_path']+'/provider-screen.png' for r in current['rows'] if r.get('origin')=='new-kku-capture' and r.get('capture_path') and not (ROOT/r['capture_path']/'provider-screen.png').exists()]
    available={i['path'] for i in images}
    result={'source':str(SOURCE),'source_sha256':sha(SOURCE.read_bytes()),'zip_entries':len(names),'manifest_files':len(manifest),'crc_and_manifest_hashes_verified':True,'entries_not_in_supplied_manifest':extra,'new_image_hash_scope':'Added images are not listed in supplied manifest; CRC checked, PNG decoded and new SHA-256 measured in this inspection.','changed_existing_files':changed,'missing_local_files':missing,'new_screenshots':images,'required_missing_screenshots':len(needed),'required_covered':len(set(needed)&available),'required_still_absent':sorted(set(needed)-available)}
    out=ROOT/'tmp/friend-evidence-zip-inspection-20261002.json';out.parent.mkdir(exist_ok=True)
    out.write_text(json.dumps(result,indent=2)+'\n')
    print(json.dumps({k:v for k,v in result.items() if k not in ('new_screenshots','missing_local_files','entries_not_in_supplied_manifest')},indent=2))
    print('New images:',len(images),'Unlisted entries:',len(extra))
