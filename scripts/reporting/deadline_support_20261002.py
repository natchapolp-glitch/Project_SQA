from pathlib import Path
import hashlib, json, shutil
from PIL import Image

ROOT=Path(__file__).resolve().parents[2]
converted=[]
for jpg in (ROOT/'ai-tests/provider-captures').rglob('provider-screen.jpg'):
    png=jpg.with_suffix('.png')
    if png.exists(): continue
    with Image.open(jpg) as image:
        image.convert('RGB').save(png)
    converted.append({'source':jpg.relative_to(ROOT).as_posix(),'source_sha256':hashlib.sha256(jpg.read_bytes()).hexdigest(),'png':png.relative_to(ROOT).as_posix(),'png_sha256':hashlib.sha256(png.read_bytes()).hexdigest(),'operation':'Lossless PNG encoding of existing JPEG pixels; original retained; no reconstruction'})
receipt=ROOT/'results/validation/deadline-20261002/image-format-receipt.json'
receipt.parent.mkdir(parents=True,exist_ok=True)
if converted:
    previous=json.loads(receipt.read_text()) if receipt.exists() else []
    receipt.write_text(json.dumps(previous+converted,indent=2)+'\n')
secondary=ROOT/'results/study/kku-clarified-20261002/claude/JxPath/ai-context'
if not secondary.exists():
    shutil.copytree(ROOT/'results/study/kku-only-20261001/claude/JxPath/ai-context',secondary)
    clarification=ROOT/'ai-tests/provider-captures/kku-clarified-20261002/claude/JxPath-1/s103-i2-kitathip-haiku-20261002/clarification-prompt.md'
    shutil.copy2(clarification,secondary/'prompt.md')
target=ROOT/'scripts/study/evaluate_provider_normalized_v86.py'
if not target.exists():
    text=(ROOT/'scripts/study/evaluate_provider_normalized_v85.py').read_text().replace('v85','v86')
    anchor="    source = path.read_text()\n"
    addition="    source = source.replace('SimpleAnnotations extends com.fasterxml.jackson.databind.util.Annotations', 'SimpleAnnotations implements com.fasterxml.jackson.databind.util.Annotations')\n"
    text=text.replace(anchor,anchor+addition,1).replace('Add missing Annotations import based on fixed compile error. Assertions unchanged.','Add missing Annotations import and implement the Annotations interface based on fixed compiler errors. Assertions unchanged.')
    target.write_text(text)
    (ROOT/'results/study/kku-only-20261001/processing-policy-v86.json').write_text(json.dumps({'basis':'fixed compiler identifies Annotations as interface; add import and change extends to implements; assertions unchanged; original history retained','driver_sha256':hashlib.sha256(target.read_bytes()).hexdigest()},indent=2)+'\n')
print(json.dumps({'png_conversions':len(converted),'new_driver':str(target)}))
