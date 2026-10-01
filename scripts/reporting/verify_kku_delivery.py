import hashlib, json, shutil, zipfile
from pathlib import Path
from pypdf import PdfReader
ROOT=Path(__file__).resolve().parents[2]
OUT=ROOT/'output/kku-only-20261001'
def sha(p): return hashlib.sha256(p.read_bytes()).hexdigest()
summary=json.loads((OUT/'summary.json').read_text(encoding='utf-8'))
assert summary['expected_runs']==204 and summary['completed_runs']==136
pdf=OUT/'SQA_Round2_KKU_Only.pdf'
ppt=OUT/'SQA_Round2_KKU_Only_v2.pptx'
assert len(PdfReader(pdf).pages)==8
with zipfile.ZipFile(ppt) as z:
    slides=[n for n in z.namelist() if __import__('re').fullmatch(r'ppt/slides/slide\d+\.xml',n)]
    assert len(slides)==16
    assert len([n for n in z.namelist() if __import__('re').fullmatch(r'ppt/(?:slides/)?charts/chart\d+\.xml',n)])==1
for name in ['baseline-evidence-audit.json','claude-evidence-audit.json','gemini-evidence-audit.json','provenance-audit.json']:
    data=json.loads((OUT/name).read_text(encoding='utf-8'))
    assert not data.get('issues'), name
shutil.copy2(ROOT/'tmp/kku-presentation/SQA_Round2_KKU_Only_v2.pptx.validation.json',OUT/'presentation-validation.json')
receipt={'completed_runs':136,'pending_runs':68,'pdf_pages':8,'presentation_slides':16,
 'visual_review':'All 8 final PDF pages and all 16 final imported PPTX slides inspected; no clipping or overlap found.',
 'powerpoint_native_app_opened':False,'audits':'zero issues in baseline, Claude, Gemini, and provenance receipts',
 'files':{p.name:sha(p) for p in [pdf,ppt,OUT/'summary.json',OUT/'analysis.json']}}
(OUT/'delivery-verification.json').write_text(json.dumps(receipt,indent=2)+'\n',encoding='utf-8')
status=json.loads((OUT/'delivery-status.json').read_text(encoding='utf-8'))
status['local_artifacts']={'report':'verified 8-page PDF','presentation':'verified 16-slide editable PPTX; native PowerPoint opening unverified','zip':'prepared for local packaging'}
(OUT/'delivery-status.json').write_text(json.dumps(status,indent=2)+'\n',encoding='utf-8')
print(json.dumps(receipt,indent=2))
