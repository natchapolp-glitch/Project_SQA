import fs from 'node:fs/promises';
import path from 'node:path';
import {pathToFileURL} from 'node:url';
import {Presentation, PresentationFile} from '@oai/artifact-tool';
const ROOT='C:/WORK/SQA_PROJECT/Project_SQA';
const BUNDLE=path.join(ROOT,'output/round2-single-bug-20261004');
const TMP=path.join(ROOT,'.local/csv-authoring');
const SKILL='C:/Users/tupto/.codex/plugins/cache/openai-primary-runtime/presentations/26.930.11008/skills/presentations';
const PY='C:/Users/tupto/.cache/codex-runtimes/codex-primary-runtime/dependencies/python/python.exe';
const {finalizePresentation,applyPresentationChartFont}=await import(pathToFileURL(path.join(SKILL,'container_tools/artifact_tool_utils.mjs')).href);
const data=JSON.parse(await fs.readFile(path.join(BUNDLE,'Experiment/results.json'),'utf8'));
const rows=data.methods;
const family='Tahoma';
const presentation=Presentation.create({slideSize:{width:1280,height:720}});
const navy='#17344A',muted='#526371',teal='#087F8C';
function txt(slide,value,x,y,w,h,size=30,bold=false,color=navy){
 const shape=slide.shapes.add({geometry:'textbox',position:{left:x,top:y,width:w,height:h},fill:'none',line:{fill:'none',width:0}});
 shape.text=value;shape.text.style={typeface:family,fontSize:size,bold,color,autoFit:'none'};return shape;
}
function slide(title,notes=''){
 const s=presentation.slides.add();s.background.fill='#FFFFFF';
 txt(s,title,64,48,1152,95,44,true);
 s.speakerNotes.textFrame.setText(notes+'\nSource: Experiment/results.json; Experiment/summary.csv; frozen Csv-1 generation and canonical Beam Defects4J records included in this bundle.');
 return s;
}
function body(s,lines,y=175){txt(s,lines.join('\n\n'),64,y,1152,450,30);}
function table(s,values,widths,y=180,height=320){
 const t=s.tables.add({rows:values.length,columns:values[0].length,left:64,top:y,width:1152,height,columnWidths:widths,values});
 for(let r=0;r<values.length;r++)for(let c=0;c<values[0].length;c++){
   const cell=t.getCell(r,c);cell.text.style={typeface:family,fontSize:25,bold:r===0,color:navy};
   cell.fill=r===0?'#E9F2F5':'#FFFFFF';
 }
 t.borders.assign({style:'solid',fill:'#D5DFE6',width:1});return t;
}

let s=presentation.slides.add();s.background.fill='#FFFFFF';
txt(s,'CP353201 SOFTWARE QUALITY ASSURANCE',64,48,1152,50,23,false,muted);
txt(s,'การสร้าง Unit Tests\nด้วยอัลกอริทึมและ AI',64,155,1152,185,58,true);
txt(s,'Defects4J Csv-1: บั๊กเดียว / 4 วิธี',64,380,1152,60,37,false,teal);
txt(s,'CMA-ES  ·  FSCS-ART  ·  KKU Sonnet 5  ·  Gemini 3.5 Flash Lite',64,470,1152,65,25);
txt(s,'รอบ 2  |  4 ตุลาคม 2026',64,610,1152,45,23,false,muted);
s.speakerNotes.textFrame.setText('กรณีศึกษาบั๊กเดียวตามขอบเขตที่ทีมเลือก ไม่ใช่ผลทุก project/dataset. สมาชิก: '+data.members.map(m=>m.name+' '+m.id).join('; '));

s=slide('ขอบเขตและสภาพแวดล้อม','หนึ่ง generated suite ต่อวิธี. Fixed สองรอบเป็นการตรวจ suite เดิม ไม่ใช่ independent generation repeats.');
table(s,[['รายการ','ขอบเขตที่ทดลอง'],['บั๊ก / class','Csv-1 / ExtendedBufferedReader'],['วิธี','2 algorithms + 2 AI ผ่าน KKU'],['Benchmark','Defects4J 3.0.1 / Java 11'],['การวัดผล','Fixed 2 รอบ → buggy → class coverage']], [330,822],170,360);
txt(s,'หนึ่งบั๊ก  ·  หนึ่ง target class  ·  หนึ่ง suite ต่อวิธี',64,565,1152,70,27,false,teal);

s=slide('กระบวนการสร้างและประเมิน tests','Algorithms ใช้ bounded input vectors; fixed observation สองครั้งสร้าง expected oracle. AI prompt ใช้ fixed source และ embedded contract. Benchmark hashes ต่าง native upstream และถูก bind ตามหลักฐานก่อนรัน.');
body(s,['1  สร้าง suite จาก vectors หรือ prompt/context','2  ใช้ suite เดิมรัน fixed revision สองครั้ง','3  รัน buggy revision และตรวจ assertion ที่ล้มเหลว','4  วัด line / branch coverage แล้วเปรียบเทียบผล'],165);
txt(s,'Oracle และ AI context ของชุดนี้อิง fixed source',64,615,1152,55,27,false,teal);

s=slide('CMA-ES: เลือก inputs ด้วย fixed behavior','พื้นฐาน: Hansen (2016), https://arxiv.org/abs/1604.00772. Code: Algorithm1_CMAES/Code/cmaes.py and Shared/frozen-runtime/scripts/study/generate.py. ไม่กล่าวว่า fitness เป็น coverage.');
body(s,['สุ่ม vector จาก multivariate normal distribution','ปรับ mean, covariance และ step size ตามคะแนน','Objective: ลดความถี่ของ fixed behavior ที่เคยพบ','Seed 101 / budget 30 vectors / bounds [-1, 1]']);
txt(s,'Coverage วัดภายหลัง ไม่ได้ใช้เป็น fitness',64,615,1152,55,27,false,teal);

s=slide('FSCS-ART: กระจาย inputs ในขอบเขตที่กำหนด','พื้นฐาน: Chen, Leung & Mak (2004), https://www.cs.nmsu.edu/~hleung/adaptiveRandomTesting.pdf. Code: Algorithm2_FSCSART/Code/fscs_art.py. Distance ใช้ normalized Euclidean; candidate set10; seed101/budget30.');
body(s,['เริ่มจาก input สุ่มหนึ่งจุด','สุ่ม candidate set 10 จุดสำหรับ input ถัดไป','เลือกจุดที่ห่างจากจุดเดิมใกล้ที่สุดมากที่สุด','ใช้ helper และ target list เดียวกับ CMA-ES']);
txt(s,'max candidate { min distance to selected inputs }',64,615,1152,55,27,false,teal);

s=slide('AI สองตัวผ่าน KKU IntelSphere','Exact same prompt SHA48558986...; requested Sonnet claude-sonnet-5, observed anthropic/claude-sonnet-5. Gemini gemini-3.5-flash-lite. Prompt/settings/raw responses included under both AI folders. Claude received response was reconciled after provider-name guard; no resend.');
table(s,[['ค่า','Sonnet 5','Gemini 3.5 Flash Lite'],['Temperature','0','0'],['Max output','4096 tokens','4096 tokens'],['Input tokens','63,672','41,314'],['Output tokens','3,636','1,655']],[290,400,462],170,355);
txt(s,'Prompt/context เดียวกัน  ·  เก็บ raw output และ settings',64,566,1152,70,27,false,teal);

s=slide('ผล fixed และ buggy ของ Csv-1','Canonical records from Beam counted replay. All fixed1/fixed2 and coverage stages pass. Actual method counts30/30/21/18 with skipped0 from XML. 99 method entries across suites is not99 unique scenarios. Fault is one distinct bug, not two bugs because two AI suites detected it.');
table(s,[['วิธี','Tests','Fixed fail','Buggy fail','พบ fault'],...rows.map(r=>[r.method,String(r.declared_tests),'0 / 0',String(r.buggy_failures),r.buggy_failures?'พบ':'ไม่พบ'])], [335,140,235,220,222],175,365);
txt(s,'AI ทั้งสองพบ CR fault  ·  algorithms ไม่พบในชุดนี้',64,570,1152,65,27,false,teal);

s=slide('Coverage ของ target class','Raw counts: CMA/FSCS31/37 lines13/26 branches; Sonnet36/37 lines22/26 branches; Gemini37/37 lines23/26 branches. Percentages computed directly from the same denominators. One class only; not project coverage.');
const chart=s.charts.add('bar',{position:{left:64,top:155,width:1152,height:410},
 categories:['CMA-ES','FSCS-ART','Sonnet 5','Gemini'],
 series:[{name:'Line coverage',values:rows.map(r=>Number((r.line_pct/100).toFixed(4))),fill:'#087F8C',valuesFormatCode:'0.0%'},
         {name:'Branch coverage',values:rows.map(r=>Number((r.branch_pct/100).toFixed(4))),fill:'#E49B33',valuesFormatCode:'0.0%'}],
 barOptions:{direction:'column',grouping:'clustered'},hasLegend:true,
 yAxis:{minimumScale:0,maximumScale:1,numberFormatCode:'0%',textStyle:{typeface:family,fontSize:23}},
 xAxis:{textStyle:{typeface:family,fontSize:23}},legend:{position:'bottom',textStyle:{typeface:family,fontSize:24}},
 dataLabels:{showValue:true,position:'outEnd',numberFormatCode:'0.0%',textStyle:{typeface:family,fontSize:23}},
 chartFill:'#FFFFFF',plotAreaFill:'#FFFFFF'});
applyPresentationChartFont(chart,{fontFamily:family});
txt(s,'Class coverage: ExtendedBufferedReader เท่านั้น',64,612,1152,55,27,false,muted);

s=slide('ตัวอย่าง fault: carriage return และ CRLF','Sonnet actual source lineNumberDoesNotDoubleCountCRLF: A\\r\\nB, after readingA andCR expected1 observed0 on1b. Gemini testCarriageReturnLineNumber a\\rb\\nc, after readinga CR b LF expected2 observed1. Both pass fixed. Source/patch binding under Experiment/source-bindings.');
table(s,[['AI / input','Fixed','Buggy'],['Sonnet: "A\\r\\nB"','line number = 1','คาด 1 แต่ได้ 0'],['Gemini: "a\\rb\\nc"','line number = 2','คาด 2 แต่ได้ 1']],[500,300,352],180,270);
txt(s,'assertEquals(expected, reader.getLineNumber())',64,485,1152,60,32,true,teal);
txt(s,'Bounded algorithm streams ไม่มี CR จึงไม่ครอบคลุมเงื่อนไขนี้',64,585,1152,70,27);

s=slide('เวลาที่บันทึก: แยก generation กับ evaluation','Generation algorithm time is native Windows original producer; AI is observed KKU HTTP wall time, not model compute. Evaluation time from canonical D4J record; excludes original generation and does not represent end-to-end effort. No speed superiority claim.');
table(s,[['วิธี','Generation / HTTP (s)','D4J evaluation (s)'],...rows.map(r=>[r.method,r.generation_seconds.toFixed(2),r.evaluation_seconds.toFixed(2)])],[360,412,380],175,365);
txt(s,'คนละขั้นตอนและ host  ·  ไม่ใช้จัดอันดับความเร็วทั่วไป',64,570,1152,65,27,false,teal);

s=slide('ข้อจำกัดและสิ่งที่ได้เรียนรู้','Fixed-assisted regression study; no blind-buggy-only claim. Unequal bounded algorithm domain vs AI broader sequences. One bug/oneclass/onegeneratedsuite. Prior17projects and v13 history are retained elsewhere and not pooled into this one-bug result.');
body(s,['หนึ่งบั๊กและหนึ่ง class ยังสรุปทั้ง dataset ไม่ได้','Fixed context / oracle และ input domains ต่างกัน','จำนวน tests ไม่ใช่งบคำนวณหรือ scenarios ที่เท่ากัน','Tests ผ่านและ coverage สูง ไม่รับรองว่าจะพบ fault']);

s=slide('Demo และการทำซ้ำ','Open Presentation/demo-guide.md and replay-demo.py. Replay all unchanged archives in fresh worktrees with Java11/D4J3.0.1/source guards/CPU1 lock. Saved rehearsal is not another generation repeat. Members and IDs are in README and report.');
body(s,['เปิด code / prompt / raw output และ suite ที่สร้างจริง','รัน fixed → buggy → coverage ด้วย archives เดิม','แสดง assertion failure และตารางเปรียบเทียบ','ใช้ README + demo-guide เริ่มทำซ้ำได้'],165);
txt(s,'ชุดส่ง: Report / Presentation / Experiment / 4 methods',64,615,1152,55,27,false,teal);

await fs.mkdir(TMP,{recursive:true});
const candidate=path.join(TMP,'csv-candidate.pptx');
await (await PresentationFile.exportPptx(presentation)).save(candidate);
const finalPath=path.join(BUNDLE,'Presentation/SQA_Round2.pptx');
await finalizePresentation({workspaceDir:ROOT,candidatePath:candidate,finalPath,pythonExecutable:PY,
 integrityValidatorPath:path.join(SKILL,'container_tools/inspect_presentation_package_integrity.py'),
 layoutValidatorPath:path.join(SKILL,'container_tools/inspect_presentation_layout_geometry.py'),
 layoutArgs:['--expected-slide-size-emu','12192000,6858000','--validate-bullet-geometry','--validate-heading-fit',
  ...[2,6,7,9,10].flatMap(n=>['--require-native-table-slide',String(n)])],
 explicitTotalSlideCount:12,requiredNativeTableOwnerSlides:[2,6,7,9,10],requiredNativeChartOwnerSlides:[8],
 materializeLiteralChartWorkbooks:true,fontPolicy:{basis:'design',families:[family]},verifyArtifactToolImport:true,
 receiptPath:path.join(TMP,'csv-slides.validation.json')});
const finalDeck=await PresentationFile.importPptx(await (await import('@oai/artifact-tool')).FileBlob.load(finalPath));
await fs.mkdir(path.join(TMP,'slides'),{recursive:true});
for(let i=0;i<finalDeck.slides.items.length;i++){
 const im=await finalDeck.export({slide:finalDeck.slides.items[i],format:'png',scale:1});
 await fs.writeFile(path.join(TMP,'slides',`slide-${i+1}.png`),new Uint8Array(await im.arrayBuffer()));
}
console.log(JSON.stringify({pptx:finalPath,slides:finalDeck.slides.items.length}));
