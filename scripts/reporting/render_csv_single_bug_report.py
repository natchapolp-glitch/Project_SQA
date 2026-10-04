"""Author a report from the four canonical Csv measurements (no new test generation)."""
from pathlib import Path
import html
import json
from reportlab.lib import colors
from reportlab.lib.pagesizes import A4
from reportlab.lib.styles import ParagraphStyle
from reportlab.pdfbase import pdfmetrics
from reportlab.pdfbase.ttfonts import TTFont
from reportlab.platypus import SimpleDocTemplate,Paragraph,Spacer,PageBreak,Table,TableStyle,Preformatted

ROOT=Path(__file__).resolve().parents[2]
BUNDLE=ROOT/'output/round2-single-bug-20261004'
DATA=json.loads((BUNDLE/'Experiment/results.json').read_text(encoding='utf-8'))
ROWS=DATA['methods']
OUT=BUNDLE/'Report';OUT.mkdir(exist_ok=True)
pdfmetrics.registerFont(TTFont('Tahoma','C:/Windows/Fonts/tahoma.ttf'))
pdfmetrics.registerFont(TTFont('TahomaBold','C:/Windows/Fonts/tahomabd.ttf'))
pdfmetrics.registerFontFamily('Tahoma',normal='Tahoma',bold='TahomaBold',italic='Tahoma',boldItalic='TahomaBold')
NAVY=colors.HexColor('#17344A');TEAL=colors.HexColor('#087F8C');GRAY=colors.HexColor('#526371')
BODY=ParagraphStyle('Body',fontName='Tahoma',fontSize=10.4,leading=15.7,spaceAfter=11,textColor=NAVY)
TITLE=ParagraphStyle('Title',parent=BODY,fontName='TahomaBold',fontSize=26,leading=33,spaceAfter=18)
H1=ParagraphStyle('H1',parent=BODY,fontName='TahomaBold',fontSize=19,leading=25,spaceAfter=17)
H2=ParagraphStyle('H2',parent=BODY,fontName='TahomaBold',fontSize=12,leading=18,spaceBefore=9,spaceAfter=9)
SMALL=ParagraphStyle('Small',parent=BODY,fontSize=8.6,leading=12.5,spaceAfter=8)
CELL=ParagraphStyle('Cell',parent=BODY,fontSize=9.0,leading=13,spaceAfter=0)
THAI=ParagraphStyle('Thai',parent=BODY,wordWrap='CJK',fontSize=10.2,leading=16)
source=[];story=[]

def p(text,style=BODY):
    story.append(Paragraph(html.escape(text),style));source.append(text+'\n')
def heading(text):p(text,H1);source.append('')
def sub(text):p(text,H2)
def table(headers,values,widths):
    matrix=[[Paragraph(html.escape(str(x)),CELL) for x in row] for row in [headers]+values]
    t=Table(matrix,colWidths=widths,hAlign='LEFT',repeatRows=1)
    t.setStyle(TableStyle([('BACKGROUND',(0,0),(-1,0),colors.HexColor('#E9F2F5')),
                          ('LINEBELOW',(0,0),(-1,0),1,TEAL),('VALIGN',(0,0),(-1,-1),'TOP'),
                          ('LEFTPADDING',(0,0),(-1,-1),7),('RIGHTPADDING',(0,0),(-1,-1),7),
                          ('TOPPADDING',(0,0),(-1,-1),9),('BOTTOMPADDING',(0,0),(-1,-1),9),
                          ('LINEBELOW',(0,1),(-1,-1),0.4,colors.HexColor('#D5DFE6'))]))
    story.append(t);story.append(Spacer(1,14))
    source.append('| '+' | '.join(map(str,headers))+' |\n| '+' | '.join(['---']*len(headers))+' |\n'+
                  '\n'.join('| '+' | '.join(map(str,r))+' |' for r in values)+'\n')
def page():story.append(PageBreak());source.append('\n---\n')
def code(text):
    story.append(Preformatted(text,ParagraphStyle('Code',fontName='Courier',fontSize=9,leading=13,spaceAfter=15)))
    source.append('```text\n'+text+'\n```\n')

p('CP353201 SOFTWARE QUALITY ASSURANCE',SMALL)
story.append(Spacer(1,42))
p('AI-Assisted Testing vs.\nAutomatic Test Generation',TITLE)
p('Defects4J Csv-1: a four-method case study',H1)
p('CMA-ES | FSCS-ART | KKU Sonnet 5 | KKU Gemini 3.5 Flash Lite',BODY)
p('Round 2 submission | 4 October 2026',SMALL)
story.append(Spacer(1,25))
p('Abstract',H2)
p('This case study compares four test-generation approaches on one real Defects4J bug. The selected suites contain 30, 30, 21 and 18 JUnit methods. All suites pass the fixed revision twice. Sonnet and Gemini expose a carriage-return line-counting fault on the buggy revision; the two bounded algorithm suites do not. Target-class coverage and recorded timing are reported with their original scopes. Fixed-assisted oracle construction, unequal input domains and a single bug limit the conclusions. This is a measured one-bug study, not a result for the entire Defects4J dataset.')
sub('Team members')
for member in DATA['members']:p(member['name']+'  |  '+member['id'],SMALL)
p('Repository: https://github.com/natchapolp-glitch/Project_SQA (branch aom)',SMALL)
page()

heading('1. Study design and measurement')
p('The objective is to build executable unit tests using two implemented algorithms and two university-hosted AI tools, then compare validation, code coverage, fault detection and recorded performance. Csv-1 and org.apache.commons.csv.ExtendedBufferedReader are the only experimental case in this submission.')
table(['Item','Declared design'],[
    ['Case','Csv-1; one bug, one target class'],
    ['Methods','CMA-ES, FSCS-ART, KKU Sonnet 5, KKU Gemini 3.5 Flash Lite'],
    ['Algorithm budget','30 proposed input vectors; seed 101; six method declarations'],
    ['AI settings','Same prompt/context; temperature 0; maximum output 4096 tokens'],
    ['Validation','Same suite: fixed1, fixed2, buggy, fixed coverage'],
    ['Benchmark environment','Defects4J 3.0.1 / Java 11 / America/Los_Angeles'],
    ['Measurement unit','JUnit method entries, not unique scenarios or number of bugs']],[130,375])
sub('Process')
code('Source / bounded inputs / prompt\n    -> four generated JUnit suites\n    -> fixed revision twice\n    -> buggy revision with unchanged suites\n    -> target-class coverage and result table')
p('A fault is counted only when a valid suite passes the fixed revision and an assertion on the buggy revision relates to the real patch. Compilation problems, missing dependencies and fixture errors are not counted as faults. Coverage is covered executable lines/branches divided by the measured target-class totals. The two fixed passes validate stability of one suite; they are not independent generation repeats.')
page()

heading('2. Algorithm implementations')
sub('CMA-ES')
p('The implemented optimizer samples a bounded continuous vector from a multivariate normal distribution. It updates the mean, covariance matrix and step size from ranked candidates, using rank-one/rank-mu covariance updates and cumulative step-size adaptation. Box clamping keeps vectors within [-1,1]. The implementation uses Python standard-library arithmetic and a Cholesky factor, with diagonal jitter for robustness. The core strategy follows the CMA-ES principles described by Hansen [1].')
p('For this Java adapter, one vector coordinate selects a method and the remaining coordinates decode arguments/fixtures. The objective minimizes the observed frequency of stable fixed-revision behavior, a proxy for output diversity. It does not optimize line or branch coverage. The budget is 30 proposed vectors, not 30 complete populations; a final partial population is observed without a full update. Initial sigma is 0.30 of mean domain width, and seed101 is recorded.')
sub('FSCS-ART')
p('The first point is random. Each later step samples 10 candidates and chooses the candidate whose distance to its nearest selected point is greatest. Euclidean distances are normalized by coordinate-domain width. This implements the max-min spreading principle of adaptive random testing [2]. The Java adapter decodes the selected vectors using the same frozen helper and six-target list as CMA-ES.')
sub('JUnit oracle construction')
p('Every proposed vector is executed twice on the fixed revision. Stable, nonduplicate vector/target observations become expected assertions. Fixture failures and unstable outputs remain in the observation ledger instead of being emitted as valid tests. GeneratedStudyTest embeds its required helper. The bounded Reader fixtures use LF-containing strings; they do not include the CR/CRLF sequences that expose the selected fault. Vector diversity therefore does not guarantee semantic input coverage.')
p('Implementation: Algorithm1_CMAES/Code, Algorithm2_FSCSART/Code, Shared/frozen-runtime/scripts/study/generate.py and Shared/inputs/algorithm-targets.json. The bundle also provides a fixed-source regeneration command; newly regenerated results must remain separate from the original reported suites.',SMALL)
page()

heading('3. AI prompting and retained evidence')
p('Both AI tools were accessed through KKU IntelSphere at https://gen.ai.kku.ac.th. The requested Sonnet ID is claude-sonnet-5 and the observed response model is anthropic/claude-sonnet-5. Gemini requested/observed gemini-3.5-flash-lite. The source prompt is identical for both methods; its SHA-256 is 485589862422ea6cd0f756145d2e8fef4781232751fffab9ab3c40b6c73c019b.')
table(['Setting','Sonnet 5','Gemini 3.5 Flash Lite'],[
 ['Transport','/messages','/chat/completions'],['Temperature','0','0'],['Maximum output','4096 tokens','4096 tokens'],
 ['Thinking','Disabled','Not applicable in retained request'],
 ['Observed input tokens','63,672','41,314'],['Observed output tokens','3,636','1,655'],
 ['Generated/executed methods','21','18']],[135,175,195])
p('The prompt supplies fixed source and an embedded helper/fixture contract, asks for executable JUnit source, limits the method count and specifies the project/version context. The full exact prompt and settings are included, rather than a reconstructed sample. This makes the study fixed-assisted regression test generation: the AI has access to fixed behavior/source and the result must not be described as blind discovery from buggy-only context.')
p('The selected Java was extracted from the fenced response without editing assertions or pruning failing tests. Raw response text, request intent, model/usage receipts, original extracted sources and packaged suites are included. A prior provider-label check rejected the Messages provider name. Its received Sonnet response was reconciled without resending the request; the failure and no-resend receipt are retained separately.')
p('AI generation is not repeated during the demo. Reproducing an API request may yield different text even with temperature0 and requires university access/quota; replaying the saved suites requires neither API keys nor fresh model tokens.')
page()

heading('4. Results on Defects4J')
table(['Method','Tests','Fixed failures','Buggy failures','Fault'],[
 [r['method'],r['declared_tests'],'0 / 0',r['buggy_failures'],'Yes' if r['buggy_failures'] else 'No'] for r in ROWS],[155,60,105,100,85])
p('The four suites contain 99 method entries in total per stage, not 99 distinct semantic scenarios. Canonical records are the counted Beam Defects4J replay; the Aom replay is supporting evidence and is not an extra generation repeat. All fixed and coverage stages pass. Two valid suites expose the same Csv-1 fault, so the number of distinct bugs in this study remains one.')
table(['Method','Lines covered','Line %','Branches covered','Branch %'],[
 [r['method'],f"{r['line_covered']}/{r['line_total']}",f"{r['line_pct']:.2f}",f"{r['branch_covered']}/{r['branch_total']}",f"{r['branch_pct']:.2f}"] for r in ROWS],[155,90,70,110,80])
p('Sonnet and Gemini cover more measured target-class lines/branches in this case. Their handwritten-style sequences exercise CR and CRLF behavior outside the bounded algorithm stream domain. These observations support a case-specific explanation; unequal domains and generation effort prevent a controlled superiority claim.')
table(['Method','Generation / HTTP seconds','D4J evaluation seconds'],[
 [r['method'],f"{r['generation_seconds']:.2f}",f"{r['evaluation_seconds']:.2f}"] for r in ROWS],[170,180,155])
p('Algorithm generation time is the original native Windows producer duration, including fixed observations and suite construction. AI time is observed HTTP submit-to-response wall time, not model compute time. D4J time is the canonical evaluation duration; it is not generation time or an end-to-end benchmark. Values are not averaged across hosts or summed into a fair speed ranking.',SMALL)
page()

heading('5. Fault analysis and demo evidence')
p('Csv-1 concerns line counting in ExtendedBufferedReader when reading carriage returns and CRLF. The official isolated patch and both benchmark/source representations are included under Experiment/source-bindings. AI assertions fail on the buggy revision and pass on the fixed revision, with these concrete examples:')
sub('Sonnet: lineNumberDoesNotDoubleCountCRLF')
code('ExtendedBufferedReader br =\n  new ExtendedBufferedReader(new StringReader("A\\r\\nB"));\nbr.read();  // A\nbr.read();  // carriage return\nassertEquals(1, br.getLineNumber());\n// Csv-1b: expected 1, observed 0\n// Csv-1f: passes')
sub('Gemini: testCarriageReturnLineNumber')
code('ExtendedBufferedReader br =\n  new ExtendedBufferedReader(new StringReader("a\\rb\\nc"));\nbr.read(); br.read(); br.read(); br.read();\nassertEquals(2, br.getLineNumber());\n// Csv-1b: expected 2, observed 1\n// Csv-1f: passes')
p('CMA-ES and FSCS-ART execute their 30 retained tests successfully on both revisions. That means the sampled suites did not expose this fault, not that the buggy revision is correct. The shared bounded fixture streams omit carriage returns, while the AI suites explicitly test those sequences. Improving algorithm input recipes is future work and is not silently applied to the reported suites.')
p('The demo replays these same saved archives in fresh worktrees, shows fixed/buggy logs, and opens coverage/summary.csv. The source names and values above are traceable to the included Java and failing_tests files. A fresh rehearsal is kept separately from canonical results, and does not change the number of bugs or generation repeats.')
page()

heading('6. Source binding and reproducibility')
p('Generation used frozen v12 code at commit 63ad195623c2ed3f67f3ae232c00c54d3160ce72. The retained native upstream buggy file differs from the isolated bug version reconstructed by Defects4J. The original strict mismatch is retained in the repository; the accepted replay binds the actual benchmark source explicitly before execution. Neither test assertions nor production sources were edited to make a mismatch disappear.')
table(['Artifact','SHA-256 / identity'],[
 ['Fixed ExtendedBufferedReader','b09b4e6ae43f1914f8e53e0bad7ba5f9483dc9b3e7ae8889c1d9d40c0de19508'],
 ['Benchmark buggy reader','4ddf7df1c7c8b9e3d45dc6a3410f802a2e5b641f37089655c3eb2f52cdfd5185'],
 ['Framework commit','6d54320e0db5a357f9ab38a8e4d2e5aead7e1c09'],
 ['Target','org.apache.commons.csv.ExtendedBufferedReader']],[150,355])
p('The bundle includes frozen code, the six-target list, original observation ledgers, test archives, raw prompts/responses, settings, source bindings, canonical logs, JUnit XML reports and coverage XML/CSV. dependencies such as Java and the initialized Defects4J distribution must be installed using the official instructions [3]. The included checksums and evidence index identify copied source artifacts.')
code('python3 Presentation/replay-demo.py \\\n  --d4j /path/to/defects4j/framework/bin/defects4j \\\n  --worktrees /path/to/worktrees \\\n  --output /fresh/path/csv-demo')
p('Use Linux/WSL, Java11 and America/Los_Angeles. Output paths must be new. The script verifies frozen code, suites, project revisions and production-source hashes before using a single CPU slot. It then runs fixed twice, buggy and target coverage. If an environment/source guard fails, keep the failure and report it instead of bypassing the guard.')
p('Algorithm regeneration: run Shared/regenerate-algorithms.py with the same three arguments. It uses only fixed source, the frozen helper, seed101 and budget30; its fresh outputs are separate from reported suites. AI prompts can be reused in KKU by an authorized team member, but a newly generated response requires a separate evaluation record.')
page()

heading('7. Limitations and conclusions')
sub('Limits of this evidence')
p('Only one bug and one target class are evaluated, with one generated suite per method. No dataset-wide fault detection rate, statistical significance or general model ranking is established. The course document requests Java projects in Defects4J generally; a one-bug submission does not demonstrate all-project completion.')
p('Fixed-assisted source/oracle access makes this a regression-oriented design. Input domains differ: algorithm vectors decode bounded streams, whereas AI generates broader sequences. Budget30 vectors and maximum30 AI methods are not equal CPU/token effort. Coverage measures the class, not the complete project. API generation occurred on a different host/context from benchmark replay; their elapsed times cannot establish a fair performance winner.')
p('The prior 17-project experiment and newer multi-bug development outputs remain in Git history. They are deliberately outside this selected one-bug table, because their models, contexts, processing rules and generation conditions differ. Historical outputs are not relabeled as Sonnet5/Gemini3.5FlashLite or counted as new runs in this submission.')
sub('What was learned')
p('Executable tests and high coverage address different questions from real-fault detection. Fixed/buggy replay plus source/patch evidence is needed to interpret a failed assertion. A bounded algorithm can generate valid tests yet miss a fault outside its input recipes. In this Csv case, both AI suites included carriage-return scenarios and exposed the fault; that outcome motivates input-domain improvements and broader controlled evaluation, rather than a universal AI advantage.')
sub('สรุปสำหรับทีม')
p('ชุดส่งนี้ทดลอง Csv-1 เพียงหนึ่งบั๊กด้วยสี่วิธี ชุดทดสอบผ่าน fixed ทั้งหมด AI สองตัวพบปัญหานับบรรทัดเมื่ออ่าน carriage return ส่วนอัลกอริทึมไม่พบในขอบเขตอินพุตที่ใช้ ผลและ coverage เป็นของกรณีนี้เท่านั้น ไม่ใช่ผลทั้ง Defects4J ข้อมูลเดิมยังเก็บอยู่ใน Git โดยไม่รวมยอดเข้าชุดนี้',THAI)
page()

heading('References and evidence map')
p('[1] Hansen, N. (2016). The CMA Evolution Strategy: A Tutorial. arXiv:1604.00772. https://arxiv.org/abs/1604.00772',SMALL)
p('[2] Chen, T. Y., Leung, H., and Mak, I. K. (2004). Adaptive Random Testing. ASIAN2004, LNCS3321, pp.320-329. DOI10.1007/978-3-540-30502-6_23. Author-hosted paper: https://www.cs.nmsu.edu/~hleung/adaptiveRandomTesting.pdf',SMALL)
p('[3] Defects4J3.0.1. Official framework and reproduction instructions: https://github.com/rjust/defects4j/tree/v3.0.1',SMALL)
p('[4] Team repository: https://github.com/natchapolp-glitch/Project_SQA. Aom accepted report checkpoint e2ce1e2701e5d08a01cef0481e53ea621bc9956e; frozen generation baseline63ad195623c2ed3f67f3ae232c00c54d3160ce72.',SMALL)
p('[5] Course assignment: SQA_Project_2026.pdf, CP353201 Software Quality Assurance, academic year1/2569, requirements1.3-1.10 and deliverables2.2. Repository reference SHA-256: 8bf8f13e6db76f579d69e19f86454aac35f2ba4f231cb0bf3c6b4284aeb7bc91.',SMALL)
table(['What to inspect','Bundle location'],[
 ['Four-method table','Experiment/summary.csv and results.json'],
 ['CMA-ES / FSCS-ART','Algorithm1_CMAES / Algorithm2_FSCSART'],
 ['Original AI evidence','AI1_KKU_Claude / AI2_KKU_Gemini'],
 ['Exact generation code/input','Shared/frozen-runtime and Shared/inputs'],
 ['Raw benchmark logs/XML','Each method: Result/Defects4J'],
 ['Source/patch derivation','Experiment/source-bindings'],
 ['Demo command/rehearsal','Presentation/demo-guide.md and rehearsal'],
 ['Integrity/provenance','checksums.json; Experiment/evidence-index.json']],[175,330])
p('This document was built from the included canonical records, not from screenshots or invented measurements. The course report, slides, CSV table and demo share this single selected case and method set.',SMALL)

def footer(canvas,doc):
    canvas.saveState();canvas.setFont('Tahoma',8);canvas.setFillColor(GRAY)
    canvas.drawString(45,27,'CP353201 | Csv-1 | Four-method case study')
    canvas.drawRightString(A4[0]-45,27,str(doc.page));canvas.restoreState()

target=OUT/'SQA_Round2_Report.pdf'
if target.exists():raise FileExistsError(target)
SimpleDocTemplate(str(target),pagesize=A4,leftMargin=45,rightMargin=45,topMargin=44,bottomMargin=45,
    title='SQA Round 2 - Csv-1 Four-Method Case Study',author='Project SQA team').build(story,onFirstPage=footer,onLaterPages=footer)
(OUT/'report-source.md').write_text('\n'.join(source),encoding='utf-8')
print(json.dumps({'pdf':str(target),'bytes':target.stat().st_size}))
