import fs from 'node:fs/promises';
import path from 'node:path';
import {pathToFileURL} from 'node:url';
import {createRequire} from 'node:module';
if (!process.env.RUNTIME_NODE_MODULES) throw new Error('Set RUNTIME_NODE_MODULES to the Codex bundled Node modules');
const require=createRequire(path.resolve(process.env.RUNTIME_NODE_MODULES,'../artifact-authoring.js'));
const {Presentation, PresentationFile, FileBlob}=await import(pathToFileURL(require.resolve('@oai/artifact-tool')).href);
const ROOT=process.cwd();
const bundle=path.resolve(process.argv[2]);
const TMP=path.join(ROOT,'.local/tonight-authoring',path.basename(bundle));
const SKILL=process.env.CODEX_PRESENTATIONS_SKILL || 'C:/Users/tupto/.codex/plugins/cache/openai-primary-runtime/presentations/26.930.11008/skills/presentations';
const PY=process.env.RUNTIME_PYTHON || 'C:/Users/tupto/.cache/codex-runtimes/codex-primary-runtime/dependencies/python/python.exe';
const {finalizePresentation,applyPresentationChartFont}=await import(pathToFileURL(path.join(SKILL,'container_tools/artifact_tool_utils.mjs')).href);
const snapshot=JSON.parse(await fs.readFile(path.join(bundle,'Experiment/snapshot.json'),'utf8'));
const summary=snapshot.summary;
const methods=Object.values(summary.per_method);
const family='Tahoma',navy='#17344A',muted='#526371',teal='#087F8C';
const presentation=Presentation.create({slideSize:{width:1280,height:720}});
function text(slide,value,x,y,w,h,size=31,bold=false,color=navy){
 const s=slide.shapes.add({geometry:'textbox',position:{left:x,top:y,width:w,height:h},fill:'none',line:{fill:'none',width:0}});
 s.text=value;s.text.style={typeface:family,fontSize:size,bold,color,autoFit:'none'};return s;
}
function slide(title,notes=''){
 const s=presentation.slides.add();s.background.fill='#FFFFFF';text(s,title,64,48,1152,100,44,true);
 s.speakerNotes.textFrame.setText(notes+'\nSource: Experiment/snapshot.json and Report/data/final_comparison.csv; raw outcomes/protocols included. Snapshot: '+summary.snapshot_at_utc);
 return s;
}
function body(s,values){text(s,values.join('\n\n'),64,175,1152,400,31);}
function table(s,values,widths,y=175,h=355){
 const t=s.tables.add({rows:values.length,columns:values[0].length,left:64,top:y,width:1152,height:h,columnWidths:widths,values});
 for(let r=0;r<values.length;r++)for(let c=0;c<values[0].length;c++){
  const cell=t.getCell(r,c);cell.text.style={typeface:family,fontSize:25,bold:r===0,color:navy};cell.fill=r===0?'#E9F2F5':'#FFFFFF';
 }
 t.borders.assign({style:'solid',fill:'#D5DFE6',width:1});return t;
}
const n=v=>v===null?'N/A':typeof v==='number'&& !Number.isInteger(v)?v.toFixed(1):String(v);
let s=presentation.slides.add();s.background.fill='#FFFFFF';
text(s,'CP353201 SOFTWARE QUALITY ASSURANCE',64,48,1152,50,23,false,muted);
text(s,'การสร้าง Unit Tests\nด้วยอัลกอริทึมและ AI',64,160,1152,185,58,true);
text(s,'Defects4J: '+summary.recorded_bugs+' bugs / '+summary.recorded_projects+' projects',64,380,1152,65,38,false,teal);
text(s,'CMA-ES · FSCS-ART · KKU Sonnet 5 · Gemini 3.5 Flash Lite',64,480,1152,60,26);
text(s,'รอบ 2 | ผลที่เก็บได้ ณ 4 ตุลาคม 2026',64,610,1152,50,24,false,muted);
s.speakerNotes.textFrame.setText('ผลบางส่วน เป้าที่เลือก '+summary.planned_bugs+' bugs. สมาชิก: '+snapshot.members.map(m=>m.name+' '+m.id).join('; '));
s=slide('ขอบเขตของผลที่นำเสนอ','Selected '+summary.planned_bugs+' bugs from '+summary.full_inventory_bugs+' installed; '+summary.planned_jobs+' jobs. Counts come from sealed outcomes.');
table(s,[['รายการ','จำนวน / เงื่อนไข'],['เป้าที่เลือก',summary.planned_bugs+' bugs × 4 วิธี = '+summary.planned_jobs.toLocaleString()+' jobs'],['มีผลบันทึก',summary.recorded_bugs+' bugs / '+summary.recorded_projects+' projects'],['Jobs ที่บันทึก',summary.attempted_jobs+' / '+summary.planned_jobs.toLocaleString()],['ครบ 4 outcomes',summary.bugs_four_methods_recorded+' bugs (รวมผลไม่ผ่าน)'],['ครบ 4 evaluations',summary.bugs_four_methods_evaluated+' bugs']], [380,772],175,385);
text(s,'ชุดนี้เป็นผลบางส่วน พร้อมรายการที่ยังค้าง',64,595,1152,55,29,false,teal);
s=slide('กระบวนการทดลอง','All measurement uses Defects4J. Two fixed passes validate the same suite, not independent generation repeats.');
body(s,['1  สร้าง JUnit suite ด้วย 2 algorithms และ 2 AI','2  ตรวจ fixed revision สองครั้ง','3  รัน buggy revision และวัด fixed coverage','4  เก็บทุก outcome แล้วเปรียบเทียบเฉพาะค่าที่วัดได้']);
s=slide('CMA-ES และ FSCS-ART','CMA-ES minimizes fixed-behavior frequency, not runtime coverage. FSCS-ART maximizes minimum normalized input distance; candidate set10. Source: included generate.py, atcg code, v12 adapter.');
table(s,[['วิธี','การเลือก input','Oracle'],['CMA-ES','ปรับ mean / covariance / step size','Fixed observations'],['FSCS-ART','เลือก candidate ที่ห่างจากชุดเดิม','Fixed observations'],['ใช้ร่วมกัน','Seed 101 / budget 30 vectors','ตรวจ target invocation']], [250,560,342],180,330);
text(s,'Coverage วัดภายหลัง ไม่ใช่ CMA-ES fitness',64,570,1152,60,30,false,teal);
s=slide('AI ผ่าน KKU IntelSphere','P01/P02 then optional single P03; actual fixed coverage determines P04. Both models share wording/context/policies within condition. No buggy failure enters repair.');
body(s,['P01  วิเคราะห์ → P02  สร้าง Java/JUnit','P03  ซ่อม compile/fixed ได้หนึ่งครั้ง','P04  เพิ่ม tests; ถ้าใช้ไม่ได้ เก็บ base ที่ valid','Temperature 0 / max output 4,096 tokens ตาม request']);
text(s,'เก็บ raw prompt / response / usage / ผลไม่ผ่าน',64,608,1152,60,28,false,teal);
s=slide('Pilot และรอบย่อ context','These conditions are disclosed separately, not asserted identical. Account aliases fixed per job; no within-job key switch. No independent buckets inferred from key count.');
table(s,[['เงื่อนไข','P01','P02'],['Pilot: Csv/Lang/Math','Buggy source + signatures','ไม่เกิน 12 methods'],['Compact nightly','Metadata + signatures','แนะนำ 4–6; เพดาน 12'],['ทั้งสองรุ่น','ซ่อม 1 ครั้ง / เพิ่มไม่เกิน 4','ไม่ส่ง fixed source หรือ patch']], [380,380,392],180,330);
text(s,'ผลต่างรุ่นมีป้ายกำกับ; ไม่วนรันเพื่อให้ผ่าน',64,575,1152,60,29,false,teal);
s=slide('ผลการทดลองทั้ง 4 วิธี','DONE means fixed twice + buggy + coverage complete. Fault counts exclude missing/invalid/harness cases.');
table(s,[['วิธี','Attempted','DONE','พบ fault / วัดได้'],...methods.map(m=>[m.name,n(m.attempted_cases),n(m.successfully_evaluated),m.fault_detecting_cases+' / '+m.fault_detection_denominator])],[380,220,180,372],180,355);
text(s,'ไม่ผ่านก็เป็นผลทดลอง; quota/pending แยกต่างหาก',64,582,1152,65,28,false,teal);
s=slide('Coverage เฉพาะ cases ที่วัดได้','Descriptive means of per-case measured ratios. Missing ratios excluded, not zero. Unequal domains and suite sizes prevent superiority inference. Cobertura condition counters.');
const valid=methods.filter(m=>m.mean_fixed_line_coverage_percent!==null && m.mean_fixed_condition_coverage_percent!==null);
const chart=s.charts.add('bar',{position:{left:64,top:165,width:1152,height:390},categories:valid.map(m=>m.name),
 series:[{name:'Line coverage (%)',values:valid.map(m=>Number(m.mean_fixed_line_coverage_percent.toFixed(1))),fill:teal},
 {name:'Condition coverage (%)',values:valid.map(m=>Number(m.mean_fixed_condition_coverage_percent.toFixed(1))),fill:'#526B83'}],
 barOptions:{direction:'column',grouping:'clustered'},hasLegend:true,legend:{position:'bottom'},
 dataLabels:{showValue:true,position:'outEnd',numberFormatCode:'0.0'},yAxis:{minimumScale:0,maximumScale:100,numberFormatCode:'0'},
 xAxis:{textStyle:{fontSize:18}},chartTitle:'Measured fixed coverage (%)'});
applyPresentationChartFont(chart,{fontFamily:family});
text(s,'Measured cases: '+methods.map(m=>m.name+' '+m.line_coverage_cases+'/'+m.condition_coverage_cases).join(' · '),64,580,1152,90,22,false,muted);
s=slide('ผลไม่ผ่านและข้อจำกัด','Original failed outputs preserved. Infrastructure problems do not prove product fault. No extra repair/generation to obtain better results.');
body(s,['Sonnet Lang: output ชนเพดาน 4,096 tokens','Sonnet Math pilot: พักก่อน P02 ตาม quota guard','Cli: classpath ขาด Hamcrest เป็น harness error','Suites และ oracle ต่างกัน; ผลยังไม่ครบ dataset']);
s=slide('Token usage ที่มีหลักฐาน','Input/output components are reported separately. Missing total_tokens in Sonnet is not replaced by an invented provider total. Ten keys do not prove ten independent quota buckets.');
table(s,[['AI','Input tokens','Output tokens','API calls'],...['kku-claude','kku-gemini'].map(k=>{
 const m=summary.per_method[k];return [m.name,n(m.recorded_prompt_tokens),n(m.recorded_completion_tokens),n(m.request_attempts)];})],[410,250,250,242],180,270);
text(s,'โควตาเป็นค่าที่สังเกตตอนตอบ ไม่ใช่ยอดรับรองปัจจุบัน',64,515,1152,100,30,false,teal);
s=slide('Demo: ดูหลักฐานและรัน suite เดิม','Presentation/demo.py displays measurements; --replay uses actual packaged archives in fresh output directories, no KKU calls. Fresh Java11/D4J setup or original host required.');
body(s,['เปิด test code / prompt / raw answer ของ case','แสดง fixed → buggy → coverage จากผลจริง','Replay archive เดิมด้วย Defects4J ใน output ใหม่','อธิบายผลผ่าน ไม่ผ่าน และ denominator ของ metrics']);
s=slide('สิ่งส่งมอบและงานที่ยังค้าง','Partial results are disclosed. Full-scope inventory is planned, not executed proof. Source: course round2 specification, snapshot manifest.');
body(s,['Code / Configuration / Test / Prompt / Raw results','Report PDF + CSV summaries + editable PowerPoint','Demo guide + replay + checksums','ขยาย cases ที่เหลือโดยเก็บทุก outcome ตามจริง']);
text(s,'ไม่อ้างว่าครบ '+summary.planned_bugs+' bugs เมื่อยังมี pending',64,608,1152,60,29,false,teal);
await fs.mkdir(TMP,{recursive:true});
const candidate=path.join(TMP,'candidate.pptx');
await (await PresentationFile.exportPptx(presentation)).save(candidate);
const finalPath=path.join(bundle,'Presentation/SQA_Round2.pptx');
await finalizePresentation({workspaceDir:ROOT,candidatePath:candidate,finalPath,pythonExecutable:PY,
 integrityValidatorPath:path.join(SKILL,'container_tools/inspect_presentation_package_integrity.py'),
 layoutValidatorPath:path.join(SKILL,'container_tools/inspect_presentation_layout_geometry.py'),
 layoutArgs:['--expected-slide-size-emu','12192000,6858000','--validate-bullet-geometry','--validate-heading-fit',...[2,4,6,7,10].flatMap(n=>['--require-native-table-slide',String(n)])],
 explicitTotalSlideCount:12,requiredNativeTableOwnerSlides:[2,4,6,7,10],requiredNativeChartOwnerSlides:[8],
 materializeLiteralChartWorkbooks:true,fontPolicy:{basis:'design',families:[family]},verifyArtifactToolImport:true,
 receiptPath:path.join(TMP,'validation.json')});
const deck=await PresentationFile.importPptx(await FileBlob.load(finalPath));
const previews=path.join(TMP,'slides');await fs.mkdir(previews,{recursive:true});
for(let i=0;i<deck.slides.items.length;i++){
 const im=await deck.export({slide:deck.slides.items[i],format:'png',scale:1});
 await fs.writeFile(path.join(previews,`slide-${i+1}.png`),new Uint8Array(await im.arrayBuffer()));
}
console.log(JSON.stringify({pptx:finalPath,slides:deck.slides.items.length}));
