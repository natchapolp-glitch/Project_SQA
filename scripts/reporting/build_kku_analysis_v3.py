#!/usr/bin/env python3
"""Create descriptive, denominator-explicit KKU-only analysis and report source."""
import json
import csv
import hashlib
from pathlib import Path
from collections import Counter, defaultdict
from statistics import mean

ROOT = Path(__file__).resolve().parents[2]
OUT = ROOT / 'output/kku-only-20261001'
NAMES = {'cmaes':'CMA-ES', 'fscs-art':'FSCS-ART', 'kku-claude':'KKU Claude', 'kku-gemini':'KKU Gemini'}

def pct(v):
    return 'N/A' if v is None else f'{100*v:.1f}%'

def main():
    data = json.loads((OUT/'summary.json').read_text(encoding='utf-8'))
    rows, methods = data['rows'], data['method_summary']
    done = [r for r in rows if r['status']=='complete']
    groups = defaultdict(dict)
    for r in done:
        groups[(r['project'],r['bug_id'],r['run_index'])][r['approach']]=r
    matched = {k:v for k,v in groups.items() if set(v)==set(NAMES)}
    paired = []
    for a in NAMES:
        selected = [v[a] for v in matched.values()]
        paired.append({'approach':a, 'n':len(selected),
                       'line_macro':mean(r['line_covered']/r['line_total'] for r in selected) if selected else None,
                       'branch_macro':mean(r['branch_covered']/r['branch_total'] for r in selected if r['branch_total']>0) if selected else None,
                       'fault_runs':sum(r['fault_detected'] for r in selected)})
    model_counts = Counter((r['approach'],r['model'],r['origin']) for r in rows if r.get('model'))
    counts = []
    for m in methods:
        a=m['approach']; selected=[r for r in done if r['approach']==a]
        counts.append({'approach':a, 'name':NAMES[a], 'completed':len(selected),
                       'projects':len({r['project'] for r in selected}),
                       'methods':sum(r['test_count'] for r in selected),
                       'reused':sum(r['origin']=='reused-audited-baseline' for r in selected),
                       'new':sum(r['origin']=='new-kku-capture' for r in selected),
                       'missing':sum(r['status']=='missing' and r['approach']==a for r in rows),
                       'failed':sum(r['status'] not in ('missing','complete') and r['approach']==a for r in rows)})
    result={'summary_sha256':hashlib.sha256((OUT/'summary.json').read_bytes()).hexdigest(),
            'counts':counts,'matched_groups':[{'project':k[0],'bug_id':k[1],'index':k[2]} for k in sorted(matched)],
            'paired_summary':paired,'model_distribution':[{'approach':k[0],'model':k[1],'origin':k[2],'records':n} for k,n in sorted(model_counts.items())],
            'status_counts':dict(Counter(r['status'] for r in rows)),
            'scope':'Descriptive results only. Successful-subset comparison and one bug/project. No causal ranking.'}
    (OUT/'analysis.json').write_text(json.dumps(result,ensure_ascii=False,indent=2)+'\n',encoding='utf-8')
    lines=['# การสร้างชุดทดสอบ Java: ผลรอบที่ 2',
           'กลุ่ม 14 / CP353201 Software Quality Assurance',
           'CMA-ES และ FSCS-ART เปรียบเทียบกับ Claude และ Gemini ผ่าน KKU IntelSphere',
           f'ข้อมูล ณ {data["generated_at_utc"]} UTC',
           f'สถานะ: สำเร็จ {len(done)}/{len(rows)} รอบตามแผนทีม งานทดลองยังไม่ครบ และยังไม่ใช่หลักฐานการส่ง Classroom',
           '## ขอบเขตและการเปลี่ยนแผน',
           'อ้างอิงเกณฑ์ SQA_Project_2026.pdf สำหรับการส่งรอบ 2: ใช้สองอัลกอริทึมและสองเครื่องมือ AI สร้าง unit tests ของ Java projects ใน Defects4J เปรียบเทียบ coverage, fault detection และ efficiency พร้อมโค้ด หลักฐาน รายงาน การนำเสนอและ demo',
           'เจ้าของงานกำหนดให้ส่ง prompt ผ่าน https://gen.ai.kku.ac.th/chat เท่านั้น และเลือกเฉพาะ Claude กับ Gemini จึงใช้กลุ่มเปรียบเทียบใหม่ CMA-ES, FSCS-ART, KKU Claude และ KKU Gemini ผล Claude โดยตรงและโมเดลตระกูลอื่นในชุดเดิมไม่รวมในผลหลัก ไม่อ้างว่าผู้สอนอนุมัติการเปลี่ยนเครื่องมือจากแผนรอบแรก',
           '17 projects × 1 active bug/project × 3 run indices (101/102/103) × 4 approaches = 204 รอบ งบสูงสุด 30 วิธีทดสอบต่อชุด AI ส่วนอัลกอริทึมเสนอ 30 input vectors จำนวนนี้เป็นแผนทีม ไม่ใช่จำนวนที่โจทย์ระบุโดยตรง หนึ่ง bug ต่อ project ไม่ครอบคลุมทุก bug ในฐานข้อมูล',
           'โปรเจกต์: '+', '.join(sorted({r['project'] for r in rows})),
           '## สถานะและจำนวนชุดทดสอบ',
           '| วิธี | สำเร็จ/51 | โปรเจกต์/17 | Test methods | ใช้เดิม | ทำใหม่ | ขาด | ยังไม่สำเร็จ |',
           '|---|---|---|---|---|---|---|---|']
    for c in counts:
        lines.append(f'| {c["name"]} | {c["completed"]}/51 | {c["projects"]}/17 | {c["methods"]:,} | {c["reused"]} | {c["new"]} | {c["missing"]} | {c["failed"]} |')
    lines += ['จำนวน methods เป็นผลรวม declared methods ใน completed suites อาจมีสถานการณ์ซ้ำข้ามรอบ ไม่ใช่ unique scenarios และไม่คูณจำนวนการรัน fixed/buggy ผล failed/missing มีค่าการวัดที่ไม่มีเป็น null ไม่แทนด้วยศูนย์',
              f'มี fresh completed records ที่ซ้ำ identity กับผลเดิม {len(data.get("secondary_new_records",[]))} รายการ: Time/Gemini/102 เก็บผลใหม่เป็น secondary validation และคงผลเดิมเป็น primary ตามลำดับที่มีหลักฐานก่อน โดยไม่ใช้ fault/coverage เลือกผล จึงไม่เพิ่มจำนวนรอบหรือ methods ในตาราง primary ตรวจผลที่สำเร็จด้วย audit แยกตาม family รวม secondary โดยอ่านจำนวนล่าสุดจาก claude/gemini-evidence-audit.json',
              'ผลเดิมผ่าน baseline audit 171/171 records การนำมาใช้ในชุดนี้อ้าง record path และ SHA-256 ไม่แก้ชื่อโมเดลหรือ generator เดิม เลือกเฉพาะผลอัลกอริทึมและคำตอบ KKU ที่ระบุ resolved model เป็น Claude/Gemini และ metadata ตรงกัน ผล KKU เดิมบางรายการมาจาก Auto Router ซึ่งแยกจากคำตอบใหม่ที่เลือก agent โดยตรง',
              '## วิธีสร้างอินพุตและ oracle',
              'FSCS-ART เลือก candidate ที่มีระยะห่างต่ำสุดจากอินพุตก่อนหน้ามากที่สุดใน normalized input space ทำให้ชุดอินพุตกระจายตัว CMA-ES ปรับ mean, covariance และ step size ของการค้นหาตัวแปรต่อเนื่อง ตัว implementation ในงานนี้ใช้ความถี่ของ fixed behavior เป็น objective เพื่อเพิ่มความหลากหลายของ output ไม่ใช้ coverage เป็น fitness',
              'SqaProbe ใช้ signatures ที่มีทั้ง fixed และ buggy revisions รวมสมาชิก non-public ที่ reflection เข้าถึงได้ สร้าง scalar, enum, string, array, collection และ constructor fixture แบบจำกัด ทุก proposed input สังเกต fixed สองครั้ง เก็บเฉพาะผลคงที่และ input ไม่ซ้ำมาสร้าง assertions ผล opaque object ยืนยันได้เพียง runtime type ส่วน serialized output ยาวเกิน 16000 ตัวอักษรใช้ SHA-256 และ byte length ข้อจำกัดนี้ลดการทดสอบ object graphs และ stateful sequences',
              'AI ใช้ prompt ต้นฉบับของแต่ละ project ใน ai-context/prompt.md ให้ fixed source/build/API context ใช้ prompt เดิมในแชทใหม่ต่อ attempt โดยคง run index ไม่ส่ง compile/error/coverage/buggy logs กลับให้ AI โค้ดจากส่วน thinking ไม่ถือเป็น final source',
              'เก็บ raw DOM-rendered response และ code blocks, model label จริง, เวลาเริ่มและเวลาสังเกตคำตอบ, URL และ screenshot ไม่อ้าง hidden temperature, token count หรือ resolved version ของ latest AI run indices เป็นหมายเลขรอบ ไม่ใช่ RNG seeds ของโมเดล',
              '## ขั้นตอนประเมินและหลักฐาน',
              'สร้าง external JUnit suite ตามรุ่นที่ project รองรับ ประเมิน fixed ครั้งที่ 1 และ fixed ครั้งที่ 2 แล้วรัน buggy และเก็บ Cobertura coverage บน fixed นิยาม fault detected คือ assertion ผ่าน fixed ซ้ำแต่ล้มเหลวบน buggy โดยแยก compiler, harness และ timeout errors ออก อ่าน triggering test ประกอบก่อนสรุป semantic defect',
              'Coverage วัดเฉพาะ classes ที่แก้ไขเพื่อซ่อม bug ไม่ใช่ทุก class ใน project เก็บ covered/total counters, coverage.xml, command.json/log, failing_tests, suite archive และ processing snapshots ที่ตรวจ hash ได้',
              'เครื่องใหม่ใช้ Ubuntu WSL, Java 11, Defects4J 3.0.1 commit 6d54320e0db5a357f9ab38a8e4d2e5aead7e1c09 ผล fixed source ของทั้ง 17 projects ตรวจ SHA ตรง ai-context เดิม ดู environment receipts สำหรับ versions/checkouts ผล baseline มาจากอีกเครื่อง จึงมีผลของ hardware, host load, caches และ build overhead ต่อเวลา',
              '## RQ1: Coverage ของชุดที่สำเร็จ',
              'Macro = ค่าเฉลี่ย covered/total ต่อ run; micro = ผลรวม covered / ผลรวม total ไม่ใช่ union coverage และนับโค้ดซ้ำข้ามรอบ ตัวหารและ successful subsets ต่างกัน จึงใช้ผลต่อไปนี้เชิงพรรณนา',
              '| วิธี | n | Line macro | Condition macro | Line micro |',
              '|---|---|---|---|---|']
    for m in methods:
        lines.append(f'| {NAMES[m["approach"]]} | {m["line_coverage_observations"]} | {pct(m["line_coverage_macro"])} | {pct(m["branch_coverage_macro"])} | {pct(m["line_coverage_micro"])} |')
    lines.append(f'CMA-ES และ FSCS-ART มีครบ 51 รอบบน sample เดียวกัน Line macro ต่างกัน {100*(methods[0]["line_coverage_macro"]-methods[1]["line_coverage_macro"]):.2f} percentage points ผลใกล้กันในค่าเฉลี่ย แต่ coverage ต่ำในบาง fixtures และแต่ละ project มีขนาด classes ต่างกัน การเทียบ AI ต้องดูตัวหารและกลุ่มตรงกันแยก เพราะ sample ที่สำเร็จมี composition ต่างจากอัลกอริทึม')
    lines += ['## RQ2: การตรวจพบ sampled bugs',
              '| วิธี | พบ/รอบที่วัดได้ | พบ/bugs ที่วัดได้ | อัตราระดับ bug |', '|---|---|---|---|']
    for m in methods:
        lines.append(f'| {NAMES[m["approach"]]} | {m["fault_detected_runs"]}/{m["fault_evaluated_runs"]} | {m["fault_detected_bugs"]}/{m["fault_evaluated_bugs"]} | {pct(m["fault_detection_rate"])} |')
    lines.append('CMA-ES พบ 5/17 sampled bugs และ FSCS-ART พบ 4/17 แม้ FSCS-ART มีรอบที่พบ fault มากกว่า (10 เทียบกับ 9) การนับระดับ run จึงตอบต่างจากระดับ distinct bug และไม่ควรเลือกวิธีจากตัวเลขใดตัวเลขหนึ่งโดยไม่ระบุหน่วย')
    lines += ['ตรวจพบซ้ำหลาย methods/runs ของ project-bug เดียวกันนับเป็นหนึ่ง bug ในอัตราระดับ bug การไม่ตรวจพบ sampled bug ไม่ยืนยันว่า suite ไม่มีประโยชน์ หรือ project ไม่มีข้อผิดพลาด',
              '## เปรียบเทียบสี่วิธีในกลุ่มตรงกัน',
              f'มี {len(matched)} project-bug-index groups ที่ครบทั้งสี่วิธี: '+(', '.join(f'{k[0]}-{k[1]}/s{k[2]}' for k in sorted(matched)) or 'ยังไม่มี'),
              '| วิธี | n กลุ่มตรงกัน | Line macro | Condition macro | พบ/รอบ |','|---|---|---|---|']
    for p in paired:
        lines.append(f'| {NAMES[p["approach"]]} | {p["n"]} | {pct(p["line_macro"])} | {pct(p["branch_macro"])} | {p["fault_runs"]}/{p["n"]} |')
    lines += ['กลุ่มตรงกันลดความต่างของ project/index แต่จำนวนกลุ่มน้อยและเลือกจากชุดที่ผ่านทั้งหมด ยังมี selection bias, model/version และ processing effort ที่ต่างกัน ไม่ใช้เป็น causal ranking และไม่ทดสอบนัยสำคัญกับ sample นี้',
              '## RQ3: Efficiency และความสำเร็จของ workflow',
              '| วิธี | Compile ผ่าน/วัด | median evaluation (s) | median generation (s) |', '|---|---|---|---|']
    for m in methods:
        lines.append(f'| {NAMES[m["approach"]]} | {m["compile_passed"]}/{m["compile_evaluated"]} | {m["duration_median_seconds"]:.2f} | {m["generation_seconds_median"]:.2f} |')
    lines += ['Compile rate ใช้เฉพาะ run ที่มีผล compile ชัดเจน ไม่รวม provider failures ที่ไม่ได้คอมไพล์ และไม่รวม attempt ที่ถูกเก็บเป็น history ก่อนซ่อม ค่า generation ของ AI เป็น submit-to-observed-completion upper bound รวมการรอผู้ปฏิบัติงาน ค่าอัลกอริทึมเป็นเวลาเครื่องวัด จึงเปรียบเทียบความเร็วโดยตรงไม่ได้',
              'evaluation รวม build/JVM/test/coverage overhead และได้ผลของ cache warming งบ 30 proposed inputs กับ 30 AI methods ไม่ใช่ effort ที่เท่ากัน token/cost/model compute ไม่ปรากฏใน UI จึงไม่สร้างตัวเลขขึ้นเอง local repair/pruning ต้องดู history เพิ่ม ไม่ใช่เพียง final-run เวลา',
              '## การซ่อมและต้นทุนที่เปิดเผย',
              'ใช้ source-order cap 30 methods, UI suffix cleanup และ fixed-only pruning สูงสุดสองรอบตาม processor ที่เก็บ hash ถ้าต้องเพิ่ม compatibility ใช้เวอร์ชันใหม่ เก็บ raw source และ failed attempts เดิม ห้ามแก้ expected values จาก buggy outcomes',
              'เวอร์ชันใหม่ v30 กู้ complete prefix ของ Gemini Lang; v31 แก้ overload ambiguity ของ Time ด้วย (String)null โดยคง assertion; v32 กู้ complete prefix ของ Claude Jsoup; v33 กู้ Claude Cli; v34 กู้ Gemini Codec; v35 cast Collections iterator เป็น ResettableIterator; v36/v37 unbox Integer sibling index สองตำแหน่งของ Jsoup ตาม fixed compiler/API ใช้ source hashes ที่ระบุ ไม่อ้างว่าต้นฉบับ AI คอมไพล์ได้โดยไม่มี processing',
              'เรียกผลที่ผ่านว่า AI-assisted with local processing เก็บ driver/source snapshots และ policies ที่มีจริง ไม่สร้าง policy ในอดีตย้อนหลัง v67 source ถูกตรึงก่อนใช้งาน แต่ไฟล์คำอธิบาย policy เขียนหลัง Closure/103 หยุดเพราะไม่มี test ก่อน execution และก่อนประเมิน Mockito; v68 และ v69-v72 เก็บ policy ก่อน invocation ของเวอร์ชันนั้น มี audit แบบเต็มเฉพาะ completed records และ audit provenance แยกตรวจ provider/failure/manifest ไม่มีการรับรองคะแนนจากผู้สอน',
              '## โมเดลและที่มาของแต่ละกลุ่ม',
              '| วิธี | label จริง | ที่มา | records |','|---|---|---|---|']
    for item in result['model_distribution']:
        lines.append(f'| {NAMES[item["approach"]]} | {item["model"]} | {item["origin"]} | {item["records"]} |')
    lines += ['ตารางนับ records ที่มี label รวม failed records ส่วน metric tables นับเฉพาะ completed การรวมตระกูลโมเดลต่าง versions ใช้ตามข้อจำกัดบริการและแยกเปิดเผย ไม่อ้างว่าเป็นโมเดลเดียวกันหรือ independent deterministic repetitions',
              '## อุปสรรคและบทเรียน',
              'ประวัติ attempts ก่อน checkpoint ล่าสุด: บัญชีเดิมแสดง Claude daily usage 100% ระหว่าง Compress/101 และ Gemini 100% หลัง Jsoup/103 ไม่ปรากฏ reset time ที่ยืนยันได้ Lang/Time เคย server busy, Math เคยไม่มี final code, Mockito และ Gemini Cli เคยถูกตัดก่อนมี method สมบูรณ์ จึงบันทึก failures แยกไว้ หาก primary ล่าสุดสำเร็จให้ใช้ summary ปัจจุบัน ไม่ตีความ failures ในอดีตเป็นสถานะสุดท้าย รอบวันที่ 2 ตุลาคม Claude Haiku แสดง 100% หลังคำตอบ Mockito/102; เจ้าของงานเลือกหยุดเก็บ AI และเตรียมชุดส่งจากผลล่าสุด ดู claude-quota-proof-20261002.png',
              'คำตอบถูกตัดกลาง Java, overloaded APIs และ fixture differences ทำให้ raw output ใช้ไม่ได้ทันที การตรวจ parser/fixed ซ้ำและการเก็บ version history ช่วยกู้ส่วนที่ใช้ได้โดยไม่ปิดบังปัญหา Coverage สูงยังไม่รับประกัน fault detection: อ่านตัวอย่าง Jsoup ใน demo-guide-kku-only.md และเปรียบเทียบ assertions กับ failing_tests',
              'Operator incident: Math/102 exports ถูกบันทึกไป Lang/102 ชั่วคราวหลัง Lang ประเมินเสร็จ กู้ raw Lang response จาก evaluator snapshot และเรียกแชท Lang เดิมกลับมาได้ SHA ตรงทั้ง response และ Java source ภาพ/DOM exports เป็นการสังเกตใหม่ ไม่อ้างว่าเป็นภาพเดิม เก็บไฟล์ที่บันทึกผิดและ recovery receipt ใน results/validation/operator-capture-routing-20261001 Math ประเมินจาก request-start และ capture ที่แก้ routing ก่อนประเมิน',
              '## ภาคผนวก: ผลต่อโปรเจกต์',
              'ตารางแสดงจำนวน completed runs / 3 ต่อวิธี ให้ดู pending-runs.csv สำหรับ index และเหตุผลที่ยังขาด',
              '| Project | CMA-ES | FSCS-ART | KKU Claude | KKU Gemini |','|---|---|---|---|---|']
    for project in sorted({r['project'] for r in rows}):
        lines.append('| '+project+' | '+' | '.join(f'{sum(r["project"]==project and r["approach"]==a for r in done)}/3' for a in NAMES)+' |')
    provenance=json.loads((OUT/'provenance-audit-current.json').read_text(encoding='utf-8'))
    ca=json.loads((OUT/'claude-evidence-audit.json').read_text(encoding='utf-8'))
    ga=json.loads((OUT/'gemini-evidence-audit.json').read_text(encoding='utf-8'))
    processing=Counter()
    styles=Counter()
    for row in done:
        record=json.loads((ROOT/row['record_path']).read_text(encoding='utf-8'))
        if record.get('ai_execution_driver'): processing[Path(record['ai_execution_driver']).name]+=1
        if row.get('capture_path'):
            operator=json.loads((ROOT/row['capture_path']/'operator-metadata.json').read_text(encoding='utf-8'))
            styles[operator.get('response_style','not recorded')]+=1
    lines += ['## การทำต่อและความครบของหลักฐาน',
              'เพื่อนเพิ่มผลผ่าน KKU หลายบัญชีและรุ่น ได้แก่ Sonnet, Haiku, Gemini Pro และ Flash ใช้ Normal หรือ Explanatory ตามที่บันทึกไว้ รุ่นและบัญชีต่างกันจึงเป็นการเปรียบเทียบระดับตระกูล ไม่ถือว่าควบคุม configuration เหมือนกันทุกครั้ง ค่า account ที่ไม่ได้สังเกตในรอบใหม่เป็น null ไม่เดาจากชื่อผู้ใช้เครื่อง',
              'เวอร์ชัน v38-v66 เก็บการซ่อม API/fixture การกู้ prefix และ pruning ตาม source snapshots และ policies ของแต่ละ run ตัวอย่าง v38 แก้ชื่อตัวแปร Codec โดยไม่เปลี่ยน expected; v39 จัด XML reader ให้เป็น START_ELEMENT; เวอร์ชันหลังตัด methods ที่ fixed compiler ไม่รองรับ รุ่น v67 สำหรับคำตอบใหม่ใช้ Java fences ที่บันทึกจาก DOM และการประมวลผลทั่วไป ไม่เรียก compatibility repairs ของคำตอบเก่า; v68 กู้ Lang/101 เฉพาะ complete prefix ก่อน method header ที่ถูกตัด โดยตรวจ SHA ต้นฉบับก่อนรัน หากต้องเพิ่ม repair ให้ใช้เวอร์ชันใหม่',
              'Mockito/101 และ /102 ใช้ v71/v72 ต่อจาก v70: ตัดห้า methods ที่เรียก Invocation.isVarArgs() ซึ่งไม่มีใน fixed API สำหรับ /101; เติม Object.class ให้ Hamcrest Matchers.any สำหรับ /102; ตัดอีกหนึ่ง method ต่อชุดที่ใช้ MatcherDecorator/VarargMatcher ผิดชนิดตาม fixed compiler ไม่แก้ assertions ของ methods ที่เก็บไว้ v69 หยุดที่ guard ก่อน execution เพราะสมมติผิดว่ามี isVarArgs เพียงครั้งเดียว เก็บ source/policy และ input ที่ล้มเหลวไว้แยก ไม่ใช้ buggy outcomes เพื่อซ่อม',
              'Mockito raw provider declarations มี 29 และ 36 methods ตามลำดับ ส่วน ai_raw_test_method_count ใน record นับหลัง compatibility repair ก่อน source-order cap ไม่ใช่จำนวนต้นฉบับ ดู raw-count-disclosure.json และ evaluated test_count สำหรับจำนวนที่รันจริง',
              'JacksonDatabind/Claude/103 ในคำตอบใหม่เป็น scaffold: 24 annotations แต่ 23 methods มีเพียงคอมเมนต์ อีก method ทดสอบ SerializedString โดยไม่เรียก BeanPropertyWriter จึงบันทึก generation_failed ก่อน execution พร้อม exact-source-SHA และ quality-review.json ไม่เพิ่มยอด completed หรือสร้าง coverage ให้ชุดที่ไม่ได้รัน',
              'Processing driver versions ใน completed primary records: '+', '.join(f'{k.replace("evaluate_provider_normalized", "base").replace(".py", "")}: {v}' for k,v in sorted(processing.items()))+' ชื่อเต็มและ source SHA อยู่ใน record ของแต่ละ run',
              'Response Style ที่บันทึกใน completed primary AI captures: '+', '.join(f'{k}: {v}' for k,v in sorted(styles.items()))+' ค่า not recorded หมายถึงหลักฐานเดิมไม่ได้ระบุ ไม่อนุมานว่า Normal',
              f'Execution evidence audit ล่าสุด: Claude fresh {ca["passed_records"]}/{ca["completed_records_audited"]}, Gemini fresh {ga["passed_records"]}/{ga["completed_records_audited"]} รวม secondary ที่มีอยู่ใน batch ส่วนผล baseline ที่ reuse ตรวจแยก ไม่รวม failures เป็น completed',
              f'Provenance audit ตรวจ primary records {provenance["records_checked"]} รายการ พบ {len(provenance["issues"])} issues: '+', '.join(f'{k}: {v}' for k,v in Counter(i['issue'] for i in provenance['issues']).items())+'. ขอบเขตนี้ต่างจาก execution audit; ดู receipt จริง ไม่ใช้ receipt รุ่นเก่าที่ผ่านเป็นผลของชุดปัจจุบัน',
              'ภาพส่วนตัวของรอบที่เพื่อนทำเพิ่มถูกยกเว้นจาก public Git ผู้ส่งต่อยืนยันว่าได้รับเฉพาะ branch จึงยังไม่มีไฟล์ภาพต้นฉบับเหล่านั้นในเครื่องนี้ raw response, metadata, source archives และ logs ที่ได้รับยังตรวจได้ตามขอบเขต audit ไม่สร้างภาพหรือหลักฐานย้อนหลัง ภาพที่เครื่องนี้เก็บเองยังคงอยู่ใน ZIP ส่วนตัว',
              'เหตุการณ์คืน CRLF ของ algorithm sources, การซ่อม driver metadata และ capture routing เก็บ receipts/bytes ก่อนแก้ใน results/validation ไม่เปลี่ยน experimental outcomes และต้องเปิดเผยข้อจำกัดของประวัติ',
              'งานล่าสุดยังไม่ใช่การส่ง Classroom เจ้าของงานจะส่งเองก่อนเที่ยงคืนวันที่ 2 ตุลาคม 2026 เวลาไทย หากเก็บไม่ครบต้องส่งผลที่ตรวจแล้วพร้อม failed/missing ตามจริง',
              '## แหล่งข้อมูลและการทำซ้ำ',
              'ผลหลัก: output/kku-only-20261001/summary.json, analysis.json, study-manifest.csv และ record.json ตาม paths ใน manifest ตรวจ provenance-audit-current.json และ baseline/claude/gemini-evidence-audit.json ก่อนใช้ ทุก completed result ต้องผ่าน audit ที่มี record hash ตรงกัน',
              'การทำซ้ำ: python3 scripts/study/kku_only.py status แล้วรัน audit_kku_provenance_v2.py สำหรับผลใหม่ใช้ evaluate_provider_normalized_vN.py ที่ตรง processing-policy-vN.json และ runtime paths ของเครื่องนี้ เก็บคำตอบผ่านหน้า KKU ด้วย exact original prompt ตาม docs/KKU_ONLY_CONTINUATION.md',
              'เกณฑ์งาน: docs/reference/SQA_Project_2026.pdf แผน/วิธีเดิม: docs/study-protocol.md และ scripts/study/generate.py ข้อจำกัด AI ปัจจุบัน: results/study/kku-only-20261001/protocol.json',
              'Hansen, N. (2016, revised 2023). The CMA Evolution Strategy: A Tutorial. https://arxiv.org/abs/1604.00772',
              'Chen, T. Y., Leung, H., & Mak, I. K. (2004). Adaptive Random Testing. ASIAN 2004, 320–329. https://doi.org/10.1007/978-3-540-30502-6_23',
              'Defects4J 3.0.1 official release documentation: docs/reference/defects4j-readme-v3.0.1.md และ https://github.com/rjust/defects4j/tree/v3.0.1',
              '## ผู้จัดทำ',
              'นายธนินธร อันทรบุตร 673380043-6 / นายศุภกร กรมรินทร์ 673380061-4 / นายณัชพล เพ็งพล 673380267-4 / นายณัฐกรณ์ อินธิสาร 673380268-2',
              'รายชื่อจากรายงานรอบแรกของกลุ่ม 14 สถานะ GitHub/Classroom ให้ตรวจ delivery-status.json ไม่ตีความว่าไฟล์ที่สร้างในเครื่องคือการส่งงานแล้ว']
    from reportlab.graphics import renderSVG
    from kku_figures import workflow, coverage
    renderSVG.drawToFile(workflow(),str(OUT/'workflow.svg'))
    renderSVG.drawToFile(coverage(methods),str(OUT/'coverage.svg'))
    lines.insert(lines.index('## ขั้นตอนประเมินและหลักฐาน')+1,'![Workflow](workflow.svg)')
    lines.insert(lines.index('## RQ1: Coverage ของชุดที่สำเร็จ')+1,'![Coverage](coverage.svg)')
    rendered = ''
    for i, line in enumerate(lines):
        rendered += line + ('\n' if line.startswith('|') and i+1<len(lines) and lines[i+1].startswith('|') else '\n\n')
    (OUT/'report-20261002.md').write_text(rendered,encoding='utf-8')
    print(json.dumps({'completed':len(done),'matched_groups':len(matched),'report_source':str(OUT/'report-20261002.md')}))

if __name__=='__main__':
    main()
