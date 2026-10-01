"""Verify the October 2 final checkpoint; explicitly retain provenance limitations."""
import hashlib,json,re,zipfile,argparse
from pathlib import Path
from collections import Counter
from xml.etree import ElementTree as ET
from pypdf import PdfReader
ROOT=Path(__file__).resolve().parents[2]
OUT=ROOT/'output/kku-only-20261001'
def sha(p):return hashlib.sha256(p.read_bytes()).hexdigest()
def load(name):return json.loads((OUT/name).read_text(encoding='utf-8'))
def main():
    parser=argparse.ArgumentParser()
    parser.add_argument('--visual-reviewed',action='store_true',required=True)
    args=parser.parse_args()
    summary=load('summary.json');analysis=load('analysis.json');rows=summary['rows']
    assert len(rows)==summary['expected_runs']==204
    assert len({(r['project'],r['bug_id'],r['approach'],r['run_index']) for r in rows})==204
    done=[r for r in rows if r['status']=='complete']
    assert len(done)==summary['completed_runs']
    assert analysis['summary_sha256']==sha(OUT/'summary.json')
    for c in analysis['counts']:
        selected=[r for r in done if r['approach']==c['approach']]
        assert c['completed']==len(selected) and c['methods']==sum(r['test_count'] for r in selected)
    audits={}
    for name in ['baseline','claude','gemini']:
        data=load(name+'-evidence-audit.json')
        assert not data['issues'] and data['passed_records']==data['completed_records_audited']
        for record in data['records']:assert sha(ROOT/record['path'])==record['sha256']
        audits[name]=data['passed_records']
    provenance=load('provenance-audit-current.json')
    assert provenance['manifest_sha256']==sha(OUT/'study-manifest.csv')
    for record in provenance['records']:assert sha(ROOT/record['path'])==record['sha256']
    assert all(i['issue']=='missing capture screenshot' for i in provenance['issues']),provenance['issues']
    frozen=json.loads((ROOT/'results/study/kku-only-20261001/claude/config.json').read_text())['source_sha256']
    for name,expected in frozen.items():assert sha(ROOT/name)==expected,name
    pdf=OUT/'SQA_Round2_KKU_Only_20261002.pdf';ppt=OUT/'SQA_Round2_KKU_Only_20261002.pptx'
    pages=PdfReader(pdf).pages
    assert len(pages)>0 and all(p.extract_text().strip() for p in pages)
    ns={'a':'http://schemas.openxmlformats.org/drawingml/2006/main','c':'http://schemas.openxmlformats.org/drawingml/2006/chart'}
    with zipfile.ZipFile(ppt) as z:
        slidepaths=sorted(n for n in z.namelist() if re.fullmatch(r'ppt/slides/slide\d+\.xml',n))
        assert len(slidepaths)==16
        tables={n:ET.fromstring(z.read(n)).findall('.//a:tbl',ns) for n in slidepaths}
        assert sum(map(len,tables.values()))==4
        for n in [6,8,9,10]:assert len(tables[f'ppt/slides/slide{n}.xml'])==1
        table=tables['ppt/slides/slide6.xml'][0]
        tabletext=[''.join(e.itertext()) for e in table.findall('.//a:t',ns)]
        for c in analysis['counts']:assert str(c['methods']) in tabletext
        charts=[n for n in z.namelist() if re.fullmatch(r'ppt/(?:slides/)?charts/chart\d+\.xml',n)]
        assert len(charts)==1 and any(n.endswith('.xlsx') for n in z.namelist())
        series=ET.fromstring(z.read(charts[0])).findall('.//c:ser',ns)
        assert len(series)==2
        for ser,key in zip(series,['line_coverage_macro','branch_coverage_macro']):
            actual=[float(v.text) for v in ser.findall('.//c:val//c:pt/c:v',ns)]
            expected=[round(100*m[key],2) for m in summary['method_summary']]
            assert len(actual)==4 and all(abs(a-b)<0.001 for a,b in zip(actual,expected)),(actual,expected)
    receipt={'completed_runs':len(done),'pending_runs':204-len(done),'pdf_pages':len(pages),'presentation_slides':16,
      'visual_review':'All final PDF pages and all 16 imported final PPTX slides inspected.',
      'powerpoint_native_app_opened':False,'execution_audits':audits,
      'provenance_issue_count':len(provenance['issues']),'provenance_issue_counts':dict(Counter(i['issue'] for i in provenance['issues'])),
      'frozen_source_hashes_verified':len(frozen),'files':{p.name:sha(p) for p in [pdf,ppt,OUT/'summary.json',OUT/'analysis.json',OUT/'claude-quota-proof-20261002.png']}}
    (OUT/'delivery-verification-20261002.json').write_text(json.dumps(receipt,indent=2)+'\n')
    print(json.dumps(receipt,indent=2))
if __name__=='__main__':main()
