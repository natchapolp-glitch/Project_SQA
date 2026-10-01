import fs from 'node:fs/promises';
import path from 'node:path';
import { fileURLToPath, pathToFileURL } from 'node:url';

const root = path.resolve(path.dirname(fileURLToPath(import.meta.url)), '../..');
const runtime = process.env.SQA_RUNTIME_ROOT;
const skill = process.env.SQA_PRESENTATION_SKILL;
if (!runtime || !skill) throw new Error('Set SQA_RUNTIME_ROOT and SQA_PRESENTATION_SKILL to bundled runtime paths.');
process.env.RUNTIME_NODE_MODULES=path.join(runtime,'node/node_modules');
const {Presentation, PresentationFile} = await import(pathToFileURL(path.join(runtime, 'node/node_modules/@oai/artifact-tool/dist/artifact_tool.mjs')).href);
const {finalizePresentation, applyPresentationChartFont} = await import(pathToFileURL(path.join(skill, 'container_tools/artifact_tool_utils.mjs')).href);
const data = JSON.parse(await fs.readFile(path.join(root, 'output/submission/summary.json'), 'utf8'));
const config = JSON.parse(await fs.readFile(path.join(root, 'experiments/configs/round2.json'), 'utf8'));
const targets = JSON.parse(await fs.readFile(path.join(root, 'dataset/study-targets.json'), 'utf8'));
const temporary = path.resolve(root, process.env.SQA_PRESENTATION_TMP_DIR || 'tmp/presentation');
await fs.mkdir(temporary, {recursive: true});
const presentation = Presentation.create({slideSize:{width:1280, height:720}});
const slides = [];
const source = 'Source: output/submission/summary.json; experiments/configs/round2.json; docs/study-protocol.md';
function text(slide, value, x, y, w, h, size=29, bold=false, color='#243746') {
  const box=slide.shapes.add({geometry:'textbox', position:{left:x,top:y,width:w,height:h},fill:'none',line:{fill:'none',width:0}});
  box.text=value; box.text.style={typeface:'Tahoma',fontSize:size,bold,color,autoFit:'none'};
  return box;
}
function page(title, lines, notes=source) {
  const slide=presentation.slides.add(); slides.push(slide); slide.background.fill='#FFFFFF';
  text(slide,title,70,45,1140,104,44,true,'#102A43');
  lines.forEach((line,i)=>text(slide,line,76,176+i*88,1124,78,28));
  slide.speakerNotes.textFrame.setText(notes);
  return slide;
}
let cover=page('การสร้างชุดทดสอบ Java',[
  'CMA-ES และ FSCS-ART เปรียบเทียบกับ Claude และ IntelliSphere',
  'CP353201 Software Quality Assurance',
  `หลักฐาน ณ ${data.metadata.generated_at_utc.slice(0,19)} UTC`,
  'สถานะงานยังไม่ครบจนกว่าผลทดลองและหลักฐาน AI จะครบ'
]);
page('ขอบเขตการทดลอง',[
  `${targets.projects.length} โปรเจกต์จาก Defects4J 3.0.1`,
  'เลือก active bug หมายเลขน้อยที่สุดของแต่ละโปรเจกต์',
  `${config.seeds.length} รอบ; อัลกอริทึม ${config.budgets.join(', ')} vectors / AI ไม่เกิน ${config.budgets.join(', ')} methods`,
  'ขอบเขตปัจจุบันเป็นหนึ่ง bug ต่อโปรเจกต์ ต้องระบุข้อจำกัดนี้ในรายงาน'
]);
page('อัลกอริทึมที่ใช้สร้างอินพุต',[
  'FSCS-ART เลือกเวกเตอร์ที่ห่างจากอินพุตก่อนหน้า',
  'CMA-ES ปรับการกระจายจากความหลากหลายของ fixed behavior',
  'Probe ใช้ reflection และสร้าง fixtures แบบจำกัด ไม่ครอบคลุม object graph ทุกแบบ',
  'วัด coverage หลังสร้างชุดทดสอบ โดยไม่ใช้ buggy outcome เลือก assertion'
]);
page('จำนวน test cases ที่ประเมินสำเร็จ',[
  ...data.test_case_counts.map(x=>`${x.generator}: ${x.retained_methods_completed_suites.toLocaleString('en-US')} test methods / ${x.completed_runs} runs`)
],source+'\nSum across primary completed suites, projects and run indices. Repeated scenarios across runs are counted. Excludes pilot, repair histories and incomplete suites. Not number of unique scenarios.');
page('การตรวจสอบชุดทดสอบ',[
  '1  ตรวจ fixed behavior ซ้ำก่อนสร้าง JUnit assertion',
  '2  รัน JUnit บน fixed สองครั้ง แล้วรันบน buggy',
  '3  แยก harness error และ timeout ออกจาก fault detection',
  '4  เก็บ coverage, logs และประวัติการซ่อมชุดทดสอบจาก fixed'
]);
page('ผลรันที่มีหลักฐาน',[
  `พบ ${data.overall.observed_runs} records และเสร็จครบ ${data.overall.completed_runs} runs`,
  `ยังไม่สมบูรณ์ ${data.overall.incomplete_runs} runs`,
  `manifest ระบุ ${data.metadata.expected_runs ?? 'ไม่ทราบ'} runs และยังไม่มี record ${data.missing_runs.length} runs`,
  'missing หรือ failed ไม่ถูกตีความเป็น coverage เท่ากับศูนย์'
]);
const measured=data.method_summary.filter(r=>r.line_coverage_macro!==null);
if(measured.length) {
  const slide=page('Coverage ของชุดทดสอบที่ประเมินครบ',[]);
  const chart=slide.charts.add('bar',{
    position:{left:90,top:165,width:1100,height:390},
    categories:measured.map(r=>r.generator),
    series:[{name:'Line coverage (%)',values:measured.map(r=>+(100*r.line_coverage_macro).toFixed(2)),fill:'#245B82'},
            {name:'Condition coverage (%)',values:measured.map(r=>+(100*r.branch_coverage_macro).toFixed(2)),fill:'#709A9D'}],
    barOptions:{direction:'column',grouping:'clustered'},hasLegend:true,dataLabels:{showValue:true,position:'outEnd'}
  });
  applyPresentationChartFont(chart,{fontFamily:'Tahoma'});
  for(const style of [chart.legend?.textStyle,chart.dataLabels?.textStyle,chart.xAxis?.textStyle,chart.yAxis?.textStyle]) {
    if(style) style.fontSize=22;
  }
  text(slide,'ค่าเฉลี่ยจาก sample ที่สำเร็จต่างกัน จึงไม่ใช้กราฟนี้จัดอันดับ',76,558,1128,40,20);
  text(slide,`ค่าเฉลี่ยต่อ run เฉพาะคลาสที่แก้ไข; ${measured.map(r=>r.generator+': n='+r.line_coverage_observations).join(', ')}`,76,607,1128,62,23);
}
page('การตรวจพบ bug และตัวหาร',data.method_summary.map(r=>
  r.fault_evaluated_bugs
    ? `${r.generator}: ${r.fault_detected_bugs}/${r.fault_evaluated_bugs} bugs ที่ประเมินได้`
    : `${r.generator}: ยังไม่มีผลประเมิน bug (0/0)`),
  source+'\nDistinct project/bug pairs. Zero denominator means no evidence, not a zero detection rate. Repeated seeds count the same bug once.');
const paired=data.paired_comparison;
const difference=(metric, unit, scale=1)=>paired.metrics[metric]
  ? `${(paired.metrics[metric].mean_difference*scale).toFixed(2)} ${unit} (n=${paired.metrics[metric].n})`
  : 'ยังไม่มีข้อมูล';
page('เปรียบเทียบบนคู่ทดลองเดียวกัน',[
  `จับคู่ได้ ${paired.matched_pairs} คู่ จาก ${paired.distinct_bugs} project/bug`,
  `Line: CMA-ES ลบ FSCS-ART = ${difference('line','percentage points',100)}`,
  `Condition: CMA-ES ลบ FSCS-ART = ${difference('branch','percentage points',100)}`,
  'เป็นสถิติเชิงพรรณนา ไม่อ้างนัยสำคัญจาก seeds ของ bug เดียวกัน'
]);
const time=v=>v===null||v===undefined?'ยังไม่มีข้อมูล':`${v.toFixed(1)} วินาที`;
page('เวลาและต้นทุนการทดลอง',[
  ...data.method_summary.filter(r=>['cmaes','fscs-art'].includes(r.generator)).map(r=>
    `${r.generator}: สร้าง ${time(r.generation_seconds_mean)} / ประเมิน ${time(r.duration_mean_seconds)}`),
  `Workflow CMA-ES ลบ FSCS-ART = ${difference('workflow_seconds','วินาที')}`,
  'เฉลี่ยเฉพาะ runs ที่เสร็จ; AI tokens และ prompt iterations รายงานแยก'
]);
page('หลักฐานจากเครื่องมือ AI',[
  'แนวทางคือใช้ fixed source และ prompt เดียวกันกับทั้งสองบริการ',
  `Claude: มี ${data.method_summary.find(r=>r.generator==='claude').completed_runs} runs ที่ประเมินครบ`,
  `IntelliSphere: มี ${data.method_summary.find(r=>r.generator==='intellisphere').completed_runs} runs ที่ประเมินครบ`,
  'เก็บ response จริงและรุ่นโมเดล; Auto Router อาจเลือกโมเดลต่างกัน'
]);
page('การประมวลผล tests ของ AI',[
  'เก็บคำตอบดิบ แล้วคง 30 methods แรกตามลำดับ source',
  'แก้ filename/UI suffix/API compatibility และตัด fixed failures',
  'เก็บทุกการแก้ไข; ไม่ส่ง evaluation logs กลับให้โมเดล',
  'AI-assisted พร้อม local processing; เวลา UI ไม่ใช่ model compute time'
],source+'\nPolicy: docs/ai-workflow.md; source-processing-v1 through v29 ledgers. Added during AI integration, not preregistered. Retain raw responses, original failures, source hashes, complete-prefix salvage and exact compatibility edits.');
const four=data.four_method_comparison;
page(`สี่วิธีบน ${four.matched_groups} กลุ่มที่มีผลครบ`,four.methods.map(r=>
  `${r.generator}: Line ${r.line_coverage_macro===null?'n/a':(100*r.line_coverage_macro).toFixed(2)+'%'} / Condition ${r.branch_coverage_macro===null?'n/a':(100*r.branch_coverage_macro).toFixed(2)+'%'} (n=${r.completed_runs})`
),source+`\n${four.distinct_bugs} distinct bugs. Four-method matched subset is conditional on all methods completing; not an overall ranking.`);
page('ข้อจำกัดและผลที่ยังสรุปไม่ได้',[
  'ชุดข้อมูลนี้ไม่ใช่ทุก active bug ใน Defects4J',
  'API ที่ต้องใช้ object fixtures อาจต้องเพิ่ม adapter เฉพาะ',
  'ผล pilot เดิม Lang-1 แยกจากการทดลอง prospective',
  'ยังจัดอันดับทั่วไปไม่ได้จาก sample ที่ผ่านและผลบางส่วน'
]);
page('การสาธิตและการทำซ้ำ',[
  'เปิด config และ manifest แล้วตรวจ source tests ที่สร้าง',
  'เทียบ fixed/buggy logs กับ evaluation/record.json',
  'เปิด coverage/summary.csv และแสดงสูตรที่ใช้คำนวณ',
  'สร้างรายงานใหม่ด้วย scripts/reporting/aggregate.py'
],source+'\nDemo walkthrough: presentation/demo-guide.md');
page('อุปสรรคและบทเรียนที่แก้แล้ว',[
  'ใช้ API signatures ที่มีทั้ง fixed และ buggy เพื่อกัน reflection error',
  'oracle ที่ยาวใช้ SHA-256 และ byte length เพื่อให้ Java คอมไพล์ได้',
  'เก็บผลล้มเหลวและเวอร์ชันเดิมไว้ตรวจย้อนหลัง',
  'การออกแบบ fixtures และ oracle มีผลต่อ coverage และ fault detection'
],source+'\nConcrete evidence and discussion: docs/lessons-learned.md');
page('ผู้จัดทำ',[
  'นายธนินธร อันทรบุตร  673380043-6',
  'นายศุภกร กรมรินทร์  673380061-4',
  'นายณัชพล เพ็งพล  673380267-4',
  'นายณัฐกรณ์ อินธิสาร  673380268-2'
],'Source: docs/reference/SQA_Round1_กลุ่ม14.pdf');

const candidate=path.join(temporary,'candidate.pptx');
await(await PresentationFile.exportPptx(presentation)).save(candidate);
const finalPath=path.join(root,'presentation/SQA_Round2.pptx');
// Finalizer requires a new output. Move an older generated deck to the private
// build folder instead of overwriting a user-supplied source presentation.
try { await fs.rename(finalPath,path.join(temporary,`previous-${Date.now()}.pptx`)); } catch(e) {if(e.code!=='ENOENT')throw e;}
const result=await finalizePresentation({workspaceDir:root,candidatePath:candidate,finalPath,
  pythonExecutable:path.join(runtime,'python/python.exe'),
  integrityValidatorPath:path.join(skill,'container_tools/inspect_presentation_package_integrity.py'),
  layoutValidatorPath:path.join(skill,'container_tools/inspect_presentation_layout_geometry.py'),
  layoutArgs:['--expected-slide-size-emu','12192000,6858000','--validate-heading-fit'],
  fontPolicy:{basis:'design',families:['Tahoma']},verifyArtifactToolImport:true,
  receiptPath:path.join(temporary,'validation.json'),materializeLiteralChartWorkbooks:true});
for(let i=0;i<slides.length;i++) {
  const preview=await presentation.export({slide:slides[i],format:'png',scale:1});
  await fs.writeFile(path.join(temporary,`slide-${i+1}.png`),new Uint8Array(await preview.arrayBuffer()));
}
console.log(JSON.stringify({finalPath,slides:slides.length,result}));
