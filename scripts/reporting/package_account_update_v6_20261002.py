from pathlib import Path
import hashlib,json,zipfile,shutil,sys
sys.path.insert(0,str(Path(__file__).resolve().parent))
from package_submission import ROOT,included
OUT=ROOT/'output/kku-only-20261001'
verification=json.loads((OUT/'account-update-20261002.json').read_text(encoding='utf-8'))
assert verification['primary_completed']==185
assert verification['primary_expected']==204
assert verification['primary_pending']==19
output=ROOT/'output/SQA_Round2_17Bugs_20261002_ACCOUNT_UPDATE_V6_Evidence.zip'
if output.exists(): raise ValueError('Version the package; never overwrite delivered ZIP')
directories=('algorithms','ai-tests','dataset','docs','experiments','output/submission','output/kku-only-20261001','presentation','prompts','results','scripts')
excluded_results=ROOT/'results/study/bugs850-20261002'
files={p for d in directories for p in (ROOT/d).rglob('*') if p.is_file() and included(p) and not p.is_relative_to(excluded_results)}
files|={ROOT/n for n in ('README.md','.gitignore','.gitattributes','active-bugs.csv','deprecated-bugs.csv') if (ROOT/n).exists()}
manifest=[{'path':p.relative_to(ROOT).as_posix(),'bytes':p.stat().st_size,'sha256':hashlib.sha256(p.read_bytes()).hexdigest()} for p in sorted(files)]
temporary=output.with_suffix('.building.zip')
with zipfile.ZipFile(temporary,'w',compression=zipfile.ZIP_DEFLATED,compresslevel=6) as z:
 for p in sorted(files): z.write(p,'Project_SQA/'+p.relative_to(ROOT).as_posix())
 z.writestr('Project_SQA/SUBMISSION_FILE_MANIFEST.json',json.dumps(manifest,indent=2))
 z.writestr('Project_SQA/START_HERE_17BUGS.txt','Read docs/ACCOUNT_UPDATE_20261002.md first. Latest original-prompt primary results: 185/204 complete; 19 incomplete. Strict Haiku: 180/204. Claude: 32/51 (27 Haiku and 5 historical Sonnet). JacksonCore-1/102 completed with 29 test methods, compile/fixed pass, no fault detected. The later Chart-1/102 Haiku attempt was refused and remains pending; refusal evidence is preserved. Earlier PDF/PPTX files are historical checkpoints. Clarification experiments are separate. This package excludes the draft 850-bug results; the 850-bug experiment has not been run. Classroom not submitted. Private provider screenshots are included in this local evidence ZIP and should not be placed in public Git.')
with zipfile.ZipFile(temporary) as z:
 assert z.testzip() is None
 for item in manifest:
  assert hashlib.sha256(z.read('Project_SQA/'+item['path'])).hexdigest()==item['sha256'],item['path']
temporary.replace(output)
checksum=hashlib.sha256(output.read_bytes()).hexdigest()
output.with_suffix('.zip.sha256').write_text(checksum+'  '+output.name+'\n',encoding='ascii')
receipt={'package':output.relative_to(ROOT).as_posix(),'files':len(files),'bytes':output.stat().st_size,'sha256':checksum,'crc_and_all_manifest_hashes_verified':True,'primary_completed':185,'strict_haiku_primary_completed':180,'primary_expected':204,'primary_pending':19,'experimental_completion':False,'classroom_submitted':False,'includes_private_provider_screenshots':True,'excludes_draft_850_bug_results':True}
(OUT/'package-verification-20261002_ACCOUNT_UPDATE_V6.json').write_text(json.dumps(receipt,indent=2)+'\n',encoding='utf-8')
downloads=Path('C:/Users/ACER/Downloads'); downloads.mkdir(parents=True,exist_ok=True)
shutil.copy2(output,downloads/output.name); shutil.copy2(output.with_suffix('.zip.sha256'),downloads/(output.name+'.sha256'))
print(json.dumps(receipt,indent=2))
