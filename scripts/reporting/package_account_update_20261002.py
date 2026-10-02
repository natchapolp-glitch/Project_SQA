from pathlib import Path
import hashlib,json,zipfile,shutil
from package_submission import ROOT,included

OUT=ROOT/'output/kku-only-20261001'
verification=json.loads((OUT/'account-update-20261002.json').read_text())
assert verification['primary_completed']==184
output=ROOT/'output/SQA_Round2_17Bugs_20261002_ACCOUNT_UPDATE_Evidence.zip'
if output.exists(): raise ValueError('Version the package; never overwrite delivered ZIP')
directories=('algorithms','ai-tests','dataset','docs','experiments','output/submission',
             'output/kku-only-20261001','presentation','prompts','results','scripts')
files={p for d in directories for p in (ROOT/d).rglob('*') if p.is_file() and included(p)
       and not p.is_relative_to(ROOT/'results/study/bugs850-20261002')}
files|={ROOT/n for n in ('README.md','.gitignore','.gitattributes','active-bugs.csv','deprecated-bugs.csv') if (ROOT/n).exists()}
manifest=[{'path':p.relative_to(ROOT).as_posix(),'bytes':p.stat().st_size,'sha256':hashlib.sha256(p.read_bytes()).hexdigest()} for p in sorted(files)]
temporary=output.with_suffix('.building.zip')
with zipfile.ZipFile(temporary,'w',compression=zipfile.ZIP_DEFLATED,compresslevel=6) as z:
    for p in sorted(files): z.write(p,'Project_SQA/'+p.relative_to(ROOT).as_posix())
    z.writestr('Project_SQA/SUBMISSION_FILE_MANIFEST.json',json.dumps(manifest,indent=2))
    z.writestr('Project_SQA/START_HERE_17BUGS.txt',
               'Read docs/ACCOUNT_UPDATE_20261002.md FIRST. Primary184/204 complete,20remaining. Strict Haiku179/204. Secondary JxPath28 methods and Time30 methods kept separate. Claude quota100percent. PDF/PPTX DEADLINE are historical183-run checkpoints; attach the latest update. Original provider proof remains private. Classroom not submitted. Actual850-bug results excluded.\n')
with zipfile.ZipFile(temporary) as z:
    assert z.testzip() is None
    for item in manifest:
        assert hashlib.sha256(z.read('Project_SQA/'+item['path'])).hexdigest()==item['sha256'],item['path']
temporary.replace(output)
checksum=hashlib.sha256(output.read_bytes()).hexdigest()
output.with_suffix('.zip.sha256').write_text(checksum+'  '+output.name+'\n')
receipt={'package':output.relative_to(ROOT).as_posix(),'files':len(files),'bytes':output.stat().st_size,
         'sha256':checksum,'crc_and_all_manifest_hashes_verified':True,'primary_completed':184,
         'experimental_completion':False,'classroom_submitted':False}
(OUT/'package-verification-20261002_ACCOUNT_UPDATE.json').write_text(json.dumps(receipt,indent=2)+'\n')
downloads=Path('C:/Users/ACER/Downloads')
shutil.copy2(output,downloads/output.name)
shutil.copy2(output.with_suffix('.zip.sha256'),downloads/(output.name+'.sha256'))
print(json.dumps(receipt,indent=2))
