from pathlib import Path
import hashlib,json,zipfile,shutil
from package_submission import ROOT,included

OUT=ROOT/'output/kku-only-20261001'
verification=json.loads((OUT/'delivery-verification-20261002_DEADLINE.json').read_text())
assert verification['primary_completed']==183 and not verification['experimental_completion']
output=ROOT/'output/SQA_Round2_17Bugs_20261002_DEADLINE_Evidence.zip'
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
               'Read docs/SUBMISSION_READY_20261002_DEADLINE.md first. Use PDF and PPTX ending 20261002_DEADLINE. Primary original-prompt study: 183/204 completed, 21 incomplete. Historical Claude Sonnet: 5 completed records; Haiku: 25. Strict Haiku primary total: 178/204. Time clarification: one completed secondary result, kept separate. Failed outputs and missing measurements are disclosed. Provider images are original private evidence; do not publicly repost this full package. This ZIP is not proof of Classroom submission. Older deliverables are historical checkpoints. Any GitHub/package status in archived files describes its own checkpoint; use the external current receipt for publication and final ZIP SHA. The two fresh bugs850 algorithm records are excluded from this 17-bug package.\n')
with zipfile.ZipFile(temporary) as z:
    assert z.testzip() is None
    for item in manifest:
        assert hashlib.sha256(z.read('Project_SQA/'+item['path'])).hexdigest()==item['sha256'],item['path']
temporary.replace(output)
checksum=hashlib.sha256(output.read_bytes()).hexdigest()
output.with_suffix('.zip.sha256').write_text(checksum+'  '+output.name+'\n')
receipt={'package':output.relative_to(ROOT).as_posix(),'files':len(files),'bytes':output.stat().st_size,
         'sha256':checksum,'crc_and_all_manifest_hashes_verified':True,'primary_completed':183,
         'experimental_completion':False,'classroom_submitted':False}
(OUT/'package-verification-20261002_DEADLINE.json').write_text(json.dumps(receipt,indent=2)+'\n')
downloads=Path('C:/Users/ACER/Downloads')
shutil.copy2(output,downloads/output.name)
shutil.copy2(output.with_suffix('.zip.sha256'),downloads/(output.name+'.sha256'))
print(json.dumps(receipt,indent=2))
