from pathlib import Path
import hashlib,json,re,zipfile
from pypdf import PdfReader
from xml.etree import ElementTree as ET

ROOT=Path(__file__).resolve().parents[2]
OUT=ROOT/'output/kku-only-20261001'
sha=lambda p: hashlib.sha256(p.read_bytes()).hexdigest()
summary=json.loads((OUT/'summary.json').read_text())
analysis=json.loads((OUT/'analysis.json').read_text())
rows=summary['rows'];done=[r for r in rows if r['status']=='complete']
assert len(rows)==204 and len({(r['project'],r['bug_id'],r['approach'],r['run_index']) for r in rows})==204
assert len(done)==summary['completed_runs']==183
assert len({r['project'] for r in rows})==17
assert analysis['summary_sha256']==sha(OUT/'summary.json')
for count in analysis['counts']:
    selected=[r for r in done if r['approach']==count['approach']]
    assert count['completed']==len(selected) and count['methods']==sum(r['test_count'] for r in selected)
audits={}
for name in ('baseline-evidence-audit.json','claude-evidence-audit.json','gemini-evidence-audit.json','clarified-evidence-audit-20261002.json','provenance-audit-current.json'):
    data=json.loads((OUT/name).read_text())
    assert not data['issues'],(name,data['issues'])
    for record in data['records']:
        assert record['passed'] and sha(ROOT/record['path'])==record['sha256'],record['path']
    audits[name]={'records':len(data['records']),'sha256':sha(OUT/name)}
assert json.loads((OUT/'provenance-audit-current.json').read_text())['manifest_sha256']==sha(OUT/'study-manifest.csv')
pdf=OUT/'SQA_Round2_KKU_Only_20261002_DEADLINE.pdf'
ppt=OUT/'SQA_Round2_KKU_Only_20261002_DEADLINE.pptx'
pages=PdfReader(pdf).pages
assert len(pages)==9 and all(p.extract_text().strip() for p in pages)
text='\n'.join(p.extract_text() for p in pages)
assert '183/204' in text and '178/204' in text and 'Haiku' in text
with zipfile.ZipFile(ppt) as z:
    assert z.testzip() is None
    slides=[n for n in z.namelist() if re.fullmatch(r'ppt/slides/slide\d+\.xml',n)]
    assert len(slides)==17
    ns={'a':'http://schemas.openxmlformats.org/drawingml/2006/main'}
    assert sum(len(ET.fromstring(z.read(n)).findall('.//a:tbl',ns)) for n in slides)==4
haiku=[r for r in done if r['approach']=='kku-claude' and r.get('model')=='claude-haiku-latest']
receipt={'primary_completed':183,'primary_pending':21,'primary_methods':sum(r['test_count'] for r in done),
         'claude_haiku_primary_completed':len(haiku),'claude_haiku_primary_projects':len({r['project'] for r in haiku}),
         'historical_completed_sonnet':5,'strict_haiku_primary_completed_total':178,
         'secondary_clarification_completed':1,'secondary_clarification_methods':30,
         'claude_quota_observed_percent':100,'pdf_pages':9,'pptx_slides':17,'audits':audits,
         'artifacts':{p.name:sha(p) for p in (pdf,ppt,OUT/'summary.json',OUT/'analysis.json')},
         'visual_review':'All nine PDF pages and all seventeen authored slide previews reviewed. Native PowerPoint was not opened.',
         'classroom_submitted':False,'experimental_completion':False,'scope':'Validated available evidence, not a grading decision; source/prompt conditions remain separate.'}
(OUT/'delivery-verification-20261002_DEADLINE.json').write_text(json.dumps(receipt,indent=2)+'\n')
print(json.dumps(receipt,indent=2))
