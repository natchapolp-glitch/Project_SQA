#!/usr/bin/env python3
"""Package KKU-only continuation with original historical evidence, locally only."""
import json
import hashlib
import zipfile
from pathlib import Path
from package_submission import included, ROOT

def main():
    output=ROOT/'output/SQA_Round2_KKU_Only_20261002_HAIKU_VERIFIED_Evidence.zip'
    if output.exists():
        raise ValueError('Do not overwrite an existing delivered package; version the new package')
    directories=('algorithms','ai-tests','dataset','docs','experiments','output/submission',
                 'output/kku-only-20261001','presentation','prompts','results','scripts')
    files={p for directory in directories for p in (ROOT/directory).rglob('*') if p.is_file() and included(p)}
    files|={ROOT/n for n in ['README.md','.gitignore','.gitattributes','active-bugs.csv','deprecated-bugs.csv'] if (ROOT/n).exists()}
    manifest=[{'path':p.relative_to(ROOT).as_posix(),'bytes':p.stat().st_size,'sha256':hashlib.sha256(p.read_bytes()).hexdigest()} for p in sorted(files)]
    temporary=output.with_suffix('.building.zip')
    with zipfile.ZipFile(temporary,'w',compression=zipfile.ZIP_DEFLATED,compresslevel=6) as z:
        for p in sorted(files):z.write(p,'Project_SQA/'+p.relative_to(ROOT).as_posix())
        z.writestr('Project_SQA/SUBMISSION_FILE_MANIFEST.json',json.dumps(manifest,indent=2))
        z.writestr('Project_SQA/PRIVATE_EVIDENCE_NOTICE.txt','Full provider screenshots may contain unrelated chat titles. This package retains only original evidence available on this machine. All 54 missing primary teammate screenshots have now been received from the owner-provided ZIP; see current provenance audit and import receipt. Latest Haiku checkpoint: 181/204 completed, 23 incomplete. Historical COMPLETE artifacts refer to recovered screenshots, not experiment completion. No missing images were recreated. Public Git excludes those screenshots. Keep this package for the team/course submission channel, not public reposting. Experimental results remain incomplete; see output/kku-only-20261001/summary.json and delivery-status.json.\n')
    with zipfile.ZipFile(temporary) as z:
        bad=z.testzip()
        if bad:raise ValueError('ZIP CRC failed: '+bad)
        for item in manifest:
            if hashlib.sha256(z.read('Project_SQA/'+item['path'])).hexdigest()!=item['sha256']:
                raise ValueError('Packaged bytes differ: '+item['path'])
    temporary.replace(output)
    checksum=hashlib.sha256(output.read_bytes()).hexdigest()
    output.with_suffix('.zip.sha256').write_text(checksum+'  '+output.name+'\n')
    receipt={'package':output.relative_to(ROOT).as_posix(),'files':len(files),'bytes':output.stat().st_size,'sha256':checksum,
             'crc_and_all_manifest_hashes_verified':True,'scope':'Local private evidence package only, not proof of remote submission.'}
    (ROOT/'output/kku-only-20261001/package-verification-20261002_HAIKU.json').write_text(json.dumps(receipt,indent=2)+'\n')
    print(json.dumps(receipt))

if __name__=='__main__':main()
