import fs from 'node:fs/promises';
import path from 'node:path';
import { fileURLToPath,pathToFileURL } from 'node:url';
const root=path.resolve(path.dirname(fileURLToPath(import.meta.url)),'../..');
const runtime=process.env.SQA_RUNTIME_ROOT;
const skill=process.env.SQA_PRESENTATION_SKILL;
if(!runtime||!skill)throw new Error('Set bundled runtime and presentation skill paths');
process.env.RUNTIME_NODE_MODULES=path.join(runtime,'node/node_modules');
const {Presentation,PresentationFile}=await import(pathToFileURL(path.join(runtime,'node/node_modules/@oai/artifact-tool/dist/artifact_tool.mjs')).href);
const {finalizePresentation,applyPresentationChartFont}=await import(pathToFileURL(path.join(skill,'container_tools/artifact_tool_utils.mjs')).href);
const out=path.join(root,'output/kku-only-20261001');
const data=JSON.parse(await fs.readFile(path.join(out,'summary.json'),'utf8'));
const analysis=JSON.parse(await fs.readFile(path.join(out,'analysis.json'),'utf8'));
const temp=path.join(root,'tmp/kku-presentation-deadline'); await fs.mkdir(temp,{recursive:true});
const p=Presentation.create({slideSize:{width:1280,height:720}});
const slides=[];
const names={'cmaes':'CMA-ES','fscs-art':'FSCS-ART','kku-claude':'KKU Claude','kku-gemini':'KKU Gemini'};
const percent=x=>x===null?'N/A':(100*x).toFixed(1)+'%';
const provenance=JSON.parse(await fs.readFile(path.join(out,'provenance-audit-current.json'),'utf8'));
const source='Latest deadline preparation preserves original-prompt primary results and separate coursework clarification. Current Haiku quota 100%; historical checkpoint statements below refer to the earlier 181-run package. Sources: output/kku-only-20261001/summary.json, analysis.json, study-manifest.csv. Audits and source records linked by SHA-256. New account: results/validation/new-account-haiku-20261002/continuation-receipt.json. Time102, Mockito103 and Compress101 add three completed identities. Compress101 detects a sampled fault. Owner selected Haiku before Time102 submission and later requested packaging without another account.';
function text(s,v,x,y,w,h,size=29,bold=false,color='#243746'){
  const t=s.shapes.add({geometry:'textbox',position:{left:x,top:y,width:w,height:h},fill:'none',line:{fill:'none',width:0}});
  t.text=v; t.text.style={typeface:'Tahoma',fontSize:size,bold,color,autoFit:'none'}; return t;
}
function page(title,lines=[],notes=source){
  const s=p.slides.add(); slides.push(s); s.background.fill='#FFFFFF';
  text(s,title,70,45,1140,106,43,true,'#102A43');
  lines.forEach((v,i)=>text(s,v,76,177+i*88,1124,78,29));
  s.speakerNotes.textFrame.setText(notes); return s;
}
function table(s,values,widths){
  const t=s.tables.add({rows:values.length,columns:values[0].length,left:76,top:177,width:1128,height:360,columnWidths:widths,values});
  for(let r=0;r<values.length;r++)for(let c=0;c<values[0].length;c++){
    const cell=t.getCell(r,c);cell.fill=r===0?'#E7EDF3':r%2?'#FFFFFF':'#F3F6F9';
    cell.text.style={typeface:'Tahoma',fontSize:r===0?24:27,bold:r===0,color:'#243746'};
  }
  return t;
}
page('การสร้างชุดทดสอบ Java รอบที่ 2',[
  'CMA-ES และ FSCS-ART เทียบกับ Claude และ Gemini ผ่าน KKU',
  'CP353201 Software Quality Assurance / กลุ่ม 14',
  `สำเร็จ ${data.completed_runs}/${data.expected_runs} รอบตามแผนทีม`,
  'ผลทดลองยังไม่ครบ และยังไม่ใช่หลักฐานการส่งงาน'
]);
page('ขอบเขตการทดลอง',[
  '17 Java projects ใน Defects4J 3.0.1 / หนึ่ง active bug ต่อ project',
  '3 run indices: 101, 102, 103 / สูงสุด 30 test methods ต่อ AI suite',
  'อัลกอริทึมเสนอ 30 input vectors ต่อ run',
  'วัด coverage เฉพาะ classes ที่แก้ไขใน bug ที่เลือก'
],source+'\n204 runs is the team plan, not a number mandated by the assignment. All sampled bug IDs are 1.');
page('ข้อกำหนด AI ปัจจุบัน',[
  'ผลหลักใช้ prompt เดิมผ่าน KKU / clarification แยกเงื่อนไข',
  'คำตอบใหม่เลือก Claude หรือ Gemini agent โดยตรง',
  'Haiku เป็นรุ่นสำหรับงานใหม่ / Sonnet เดิมแยกตาม model label',
  'Claude-direct และโมเดลตระกูลอื่นไม่รวมในผลหลัก'
],source+'\nOwner changed the round-one AI comparison. Instructor approval is not assumed. Historical Auto Router outputs are reused only when resolved allowed-family labels and provenance agree.');
page('อัลกอริทึมและ oracle',[
  'FSCS-ART เลือก candidate ที่ห่างจาก input ก่อนหน้ามากที่สุด',
  'CMA-ES ปรับ distribution เพื่อเพิ่ม fixed-output diversity',
  'Reflection fixtures มีขอบเขตจำกัด รวมสมาชิก non-public',
  'สังเกต fixed ซ้ำก่อนสร้าง assertions และเก็บ unstable cases'
],'Sources: scripts/study/generate.py; docs/study-protocol.md; Hansen tutorial https://arxiv.org/abs/1604.00772; Chen et al. https://doi.org/10.1007/978-3-540-30502-6_23. Coverage is measured afterward, not used as CMA-ES fitness.');
page('หลักฐานที่ต้องผ่านก่อนนับ completed',[
  'JUnit suite ผ่าน fixed ครั้งที่ 1 และครั้งที่ 2',
  'รัน buggy เพื่อแยก assertion failure จาก harness/timeout errors',
  'เก็บ fixed coverage, logs, suite archives และ processing hashes',
  'Execution audit ผ่าน / record SHA ตรง manifest; provenance gaps เปิดเผยแยก'
]);
let s=page('ผลที่สำเร็จและ test methods');
table(s,[['วิธี','Runs / 51','Projects / 17','Methods'],...analysis.counts.map(c=>[c.name,`${c.completed}/51`,`${c.projects}/17`,String(c.methods)])],[360,230,270,268]);
text(s,'Methods รวมสถานการณ์ที่อาจซ้ำข้ามรอบ ไม่ใช่ unique scenarios',76,574,1120,70,25);
s=page('RQ1: Coverage ของ successful subsets');
const chart=s.charts.add('bar',{position:{left:85,top:165,width:1110,height:390},categories:data.method_summary.map(m=>names[m.approach]),
  series:[{name:'Line macro (%)',values:data.method_summary.map(m=>+(100*m.line_coverage_macro).toFixed(2)),fill:'#245B82'},
          {name:'Condition macro (%)',values:data.method_summary.map(m=>+(100*m.branch_coverage_macro).toFixed(2)),fill:'#709A9D'}],
  barOptions:{direction:'column',grouping:'clustered'},hasLegend:true,
  legend:{position:'bottom',textStyle:{typeface:'Tahoma',fontSize:21}},
  xAxis:{visible:true,textStyle:{typeface:'Tahoma',fontSize:21}},
  yAxis:{visible:true,min:0,max:100,majorUnit:25,textStyle:{typeface:'Tahoma',fontSize:18}},
  dataLabels:{showValue:true,position:'outEnd',textStyle:{typeface:'Tahoma',fontSize:20,bold:true}}});
applyPresentationChartFont(chart,{fontFamily:'Tahoma'});
text(s,'ค่าเฉลี่ยต่อ run / ตัวหารต่างกัน / ใช้จัดอันดับทั่วไปไม่ได้',76,584,1120,60,25);
s=page('RQ2: Fault detection พร้อมตัวหาร');
table(s,[['วิธี','พบ / runs','พบ / bugs','Bug rate'],...data.method_summary.map(m=>[names[m.approach],`${m.fault_detected_runs}/${m.fault_evaluated_runs}`,`${m.fault_detected_bugs}/${m.fault_evaluated_bugs}`,percent(m.fault_detection_rate)])],[350,260,260,258]);
text(s,'นับ bug เดียวครั้งเดียว แม้หลาย methods/runs ตรวจพบซ้ำ',76,575,1120,60,25);
s=page(`กลุ่มที่ครบสี่วิธี: ${analysis.matched_groups.length} กลุ่ม`);
table(s,[['วิธี','n','Line macro','พบ / runs'],...analysis.paired_summary.map(m=>[names[m.approach],String(m.n),percent(m.line_macro),`${m.fault_runs}/${m.n}`])],[380,130,330,288]);
text(s,`${analysis.matched_groups.length} กลุ่มจาก ${new Set(analysis.matched_groups.map(g=>g.project)).size} projects / รายการเต็มอยู่ใน analysis.json`,76,558,1120,72,24);
text(s,'กลุ่มน้อยและเลือกจากชุดที่ผ่านทั้งหมด ยังมี selection bias',76,643,1120,50,23);
s=page('RQ3: เวลาและ compilation');
table(s,[['วิธี','Compile ผ่าน/วัด','Eval median (s)','Gen median (s)'],...data.method_summary.map(m=>[names[m.approach],`${m.compile_passed}/${m.compile_evaluated}`,m.duration_median_seconds.toFixed(2),m.generation_seconds_median.toFixed(2)])],[310,290,264,264]);
text(s,'AI generation = เวลา UI ที่สังเกต / ไม่ใช่ model compute time',76,568,1120,65,25);
text(s,'ต่างเครื่อง, caches และ build overhead มีผลต่อเวลา',76,637,1120,45,24);
page('ผล AI ผ่าน local processing',[
  'เก็บ raw response โดยไม่แก้ไข และเก็บ failed attempts ทุกครั้ง',
  'Cap ตาม source order และ fixed-only pruning สูงสุดสองรอบ',
  'เก็บ driver/source snapshots และเวอร์ชัน processing ของแต่ละ run',
  'ใช้คำว่า AI-assisted with local processing ในรายงาน'
],source+'\nNew v73/v74 use the generic policy and preserve separate attempt history. v75 excludes one entire Compress101 method with unsupported constructor using only fixed compiler diagnostics; retained assertions unchanged. Time102 retains complete UnsupportedDurationField class and preserves incomplete Partial output separately. Policies and snapshots: results/study/kku-only-20261001/processing-policy-v*.json and generation/processing-sources. Missing historical policy files are not reconstructed as prior evidence. No assertion values selected from buggy outcomes. Compile rate describes final primary records, not all attempts.');
page('ตัวอย่างจริง: Jsoup รอบ 101',[
  'Claude Sonnet เดิม: 30 methods / พบ assertion failure บน buggy',
  'Gemini: 16 methods / fixed ผ่านซ้ำ / ไม่ตรวจพบ sampled bug',
  'เปิด source ของ normalise และ failing_tests เพื่ออธิบายพฤติกรรม',
  'Coverage สูงต้องพิจารณาร่วมกับ assertions และ fault outcomes'
],source+'\nExample records: results/study/kku-only-20261001/{claude,gemini}/Jsoup/intellisphere-s101-b30/evaluation/record.json. Demo guide: presentation/demo-guide-kku-only.md.');
page('สถานะหลักฐานและผลที่ยังขาด',[
  'Claude Haiku ในบัญชีปัจจุบันถึงโควต้า 100% แล้ว',
  `Provenance audit: ${provenance.issues.length} issues / ภาพและ hashes ตรวจแล้ว`,
  `ยังไม่ completed ${data.expected_runs-data.completed_runs} รอบ / ดู pending-runs.csv`,
  'ได้รับภาพที่ขาด 54 รายการแล้ว / failed-missing metrics ยังเป็น null'
]);
page('ข้อจำกัดการเปรียบเทียบ',[
  'หนึ่ง bug ต่อ project / successful subsets และ model versions ต่างกัน',
  'Fixture/reflection/oracle จำกัด object graphs และ stateful sequences',
  '30 algorithm inputs และ 30 AI methods ใช้ effort ต่างกัน',
  'ยังสรุปผู้ชนะทั่วไปหรือ causal speed advantage ไม่ได้'
]);
page('Demo และการทำซ้ำ',[
  'เปิด manifest แล้วตรวจ path และ SHA ของ record ที่เลือก',
  'ดู generated source เทียบ fixed/buggy logs และ coverage.xml',
  'รัน status และ audits ก่อนสร้าง analysis/report ใหม่',
  'ตรวจ delivery-status.json ก่อนอ้างว่า GitHub/Classroom ส่งแล้ว'
],source+'\nCommands and walkthrough: docs/KKU_ONLY_CONTINUATION.md and presentation/demo-guide-kku-only.md. Old aggregate.py commands describe the original study, not this new comparison.');
const haikuDone=data.rows.filter(r=>r.approach==='kku-claude'&&r.status==='complete'&&r.model==='claude-haiku-latest');
page('ผลเพิ่มเติมและการแยกรุ่น Claude',[
  `ผลหลัก Haiku ${haikuDone.length}/51 รอบ / Sonnet เดิม 5 รอบ แยกไว้`,
  'Time-1/101 clarification: 30 เทส / fixed ผ่าน / ไม่พบ fault',
  'Time เพิ่มแยกเงื่อนไข ไม่บวกเป็น prompt เดิมหรือบัคใหม่',
  'JxPath clarification ไม่มีโค้ดเทส / งานยังไม่ครบทุกเงื่อนไข'
],source+'\nUpdated source: docs/HAIKU_CLARIFICATION_RESULT_20261002_TH.md; clarified-evidence-audit-20261002.json; source-processing policies v84-v88. Actual current completion is in summary.json. Five Sonnet results remain historical; no relabelling as Haiku.');
page('ผู้จัดทำ',[
  'นายธนินธร อันทรบุตร  673380043-6',
  'นายศุภกร กรมรินทร์  673380061-4',
  'นายณัชพล เพ็งพล  673380267-4',
  'นายณัฐกรณ์ อินธิสาร  673380268-2'
],'Source: docs/reference/SQA_Round1_กลุ่ม14.pdf');
const candidate=path.join(temp,'candidate.pptx');
await(await PresentationFile.exportPptx(p)).save(candidate);
const finalPath=path.join(out,process.env.SQA_FINAL_PPTX_NAME||'SQA_Round2_KKU_Only_20261002_DEADLINE.pptx');
const result=await finalizePresentation({workspaceDir:root,candidatePath:candidate,finalPath,
  pythonExecutable:path.join(runtime,'python/python.exe'),integrityValidatorPath:path.join(skill,'container_tools/inspect_presentation_package_integrity.py'),
  layoutValidatorPath:path.join(skill,'container_tools/inspect_presentation_layout_geometry.py'),
  layoutArgs:['--expected-slide-size-emu','12192000,6858000','--validate-heading-fit',...[6,8,9,10].flatMap(n=>['--require-native-table-slide',String(n)])],
  requiredNativeTableOwnerSlides:[6,8,9,10],requiredNativeChartOwnerSlides:[7],explicitTotalSlideCount:17,
  fontPolicy:{basis:'design',families:['Tahoma']},verifyArtifactToolImport:true,
  receiptPath:path.join(temp,path.basename(finalPath)+'.validation.json'),materializeLiteralChartWorkbooks:true});
for(let i=0;i<slides.length;i++){
  const preview=await p.export({slide:slides[i],format:'png',scale:1});
  await fs.writeFile(path.join(temp,`slide-${i+1}.png`),new Uint8Array(await preview.arrayBuffer()));
}
console.log(JSON.stringify({finalPath,slides:slides.length,result}));
