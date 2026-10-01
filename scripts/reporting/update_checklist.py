#!/usr/bin/env python3
"""Refresh the existing submission checklist from actual aggregated evidence."""
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[2]
data = json.loads((ROOT / 'output/submission/summary.json').read_text(encoding='utf-8'))
methods = {row['generator']: row for row in data['method_summary']}
algorithms = sum(methods[m]['completed_runs'] for m in ('cmaes', 'fscs-art'))
ai = sum(methods[m]['completed_runs'] for m in ('claude', 'intellisphere'))
planned_algorithms = sum(row['generator'] in ('cmaes', 'fscs-art') for row in data['planned_runs'])
planned_ai = len(data['planned_runs']) - planned_algorithms
projects = len({row['project'] for row in data['planned_runs']})
all_pairs = sum(row['all_four_present'] for row in data['matched_groups'])
providers = {r['tool']:r for r in data['ai_provider_evidence']['summary']}
attempted_ai = sum(r['captured_runs'] for r in providers.values())
ai_scope = '; '.join(f"{m}: {providers[m]['captured_projects']}/{projects} projects, {providers[m]['captured_runs']} responses, {methods[m]['completed_runs']} complete" for m in ('claude','intellisphere'))
status = 'เสร็จตามแผน algorithm' if algorithms == planned_algorithms else 'กำลังดำเนินการ'
lines = [
    '# Checklist งานข้อ 2.2 จาก PDF โจทย์', '',
    f"สถานะจากหลักฐานที่ aggregate เมื่อ {data['metadata']['generated_at_utc']} (UTC)",
    'ขอบเขต: เลือก active bug หมายเลขต่ำสุดหนึ่งรายการต่อ Java project รวม 17 projects; ไม่ใช่ทุก bug ใน Defects4J', '',
    '| เงื่อนไข | สถานะ | หลักฐาน/งานที่เหลือ |', '|---|---|---|',
    f'| พัฒนาอัลกอริทึมและทดลองทุกรายการ Java projects | {status} | CMA-ES/FSCS-ART source และ {algorithms}/{planned_algorithms} final algorithm runs; เลือก 1 bug/project, 3 seeds, 30 proposed inputs |',
    f"| ใช้ AI-Assisting Tools ที่เลือกกับทุก project | {'มีคำตอบทุก project' if all(r['captured_projects']==projects for r in providers.values()) else 'ยังไม่ครบ'} | {ai_scope}; responses รวมข้อความผิดพลาด ไม่เท่ากับ valid suites |",
    f'| บันทึก coverage, efficiency และ fault detection | มีผลจริงและผลล้มเหลว | มี raw logs, test archives, coverage XML/CSV และเวลา; AI มี {ai}/{planned_ai} complete และบันทึกคำตอบ {attempted_ai}/{planned_ai} รอบ; AI เวลาเป็น UI observation upper bound |',
    f'| เปรียบเทียบทุกวิธีและสรุปผล/ปัญหา | ยังไม่ครบสี่วิธี | มี descriptive paired comparison ของสองอัลกอริทึม; กลุ่มที่มีผลครบสี่วิธี {all_pairs} กลุ่ม |',
    '| รายงาน/source/test/results/ภาพหรือ diagram/prompt/config ทำซ้ำได้ | มีฉบับตามหลักฐานปัจจุบัน | PDF, Markdown, SVG/PNG, source, frozen hashes, actual JUnit, archives, logs, AI prompts และคำสั่งทำซ้ำ; เพิ่มผล AI แล้วสร้างฉบับสุดท้าย |',
    '| Presentation และ Demo | เตรียมไฟล์แล้ว | PPTX และ demo-guide พร้อมตัวอย่าง algorithm และ AI จริง; ทีมต้องซ้อมนำเสนอ |',
    '| README อธิบายงานและชื่อ/รหัสสมาชิก | มีแล้ว | ชื่อและรหัส 4 คนอ้างจากรายงานรอบแรก |',
    '| ส่ง GitHub และ Google Classroom | ยังไม่ได้ส่งการเปลี่ยนแปลงรอบนี้ | งานอยู่บนเครื่องใน branch test; การ push ล่าสุดถูก GitHub ปฏิเสธ 403 เพราะบัญชี aarktik ไม่มีสิทธิ์เขียน; ชุด ZIP คือ output/SQA_Round2_Submission.zip พร้อม manifest และ SHA-256 (สร้างด้วย package_submission.py) |',
    '| นำเสนอและสาธิตต่อผู้สอน | ทีมต้องดำเนินการ | ใช้ PPTX/demo-guide และแสดง logs ที่ตรวจสอบแล้ว |', '',
    '## งานที่เหลือ', '',
    '1. เก็บ AI รอบที่ยังขาดหลังบริการเปิดให้ใช้งาน โดยใช้เฉพาะ prompt เดิม ดู provider-status.json และ missing-runs.csv',
    '2. ตรวจผล compile/parse/fixed failures จาก incomplete-runs.csv; หากเปลี่ยนการประมวลผลให้แยก cohort ใหม่และเก็บผลเดิม',
    '3. ตรวจรายงาน สไลด์ และ ZIP ฉบับปัจจุบันร่วมกันก่อนส่ง ไม่อ้างว่าข้อมูลที่ยังขาดประเมินสำเร็จแล้ว',
    '4. ส่งงานผ่าน GitHub/Classroom และนำเสนอ demo ตามช่องทางและเวลาของรายวิชา', '',
    '## หลักฐานการตรวจและการแก้ไข', '',
    '- `output/submission/evidence-audit.json` ตรวจ hashes, fixed/buggy logs, counters, archives, budget และ repair lineage',
    '- Cli: แก้ JUnit/Hamcrest dependency แล้วประเมิน archive เดิม 6 รอบ; เก็บก่อน/หลังและผลล้มเหลวเดิม',
    '- JxPath: ตัด generated21 ที่ไม่ผ่าน fixed validation แล้วเหลือ 29 tests; ไม่ใช้ buggy outcome เลือก test',
    '- Mockito: ใช้ checkout แยกสำหรับ FSCS-ART พร้อม driver hash/snapshot; เวลาได้รับผลจาก shared host load',
    '- AI: source-order cap ไม่เกิน 30, IntelSphere UI suffix cleanup, filename hint removal ใน v2/v3, fixed-only pruning ไม่เกินสองรอบ, v4/v5 fixed-API/JUnit compatibility, v6-v29 parser/compile/fixture recovery ตาม fixed API ที่เปิดเผย; เก็บคำตอบและทุกการแก้ไข ไม่ส่ง evaluation logs ให้บริการ',
    '- AI: Auto Router เลือกโมเดลต่างกันได้; null ไม่แทนด้วยศูนย์และ matched comparison ใช้เฉพาะกลุ่มที่ครบ',
    '- `docs/lessons-learned.md` และ `docs/study-protocol.md` อธิบาย oracle/type-only/fixture และ sampling limitations', '',
    'WSL distribution ที่ใช้งานได้ชื่อ `Ubuntu`; Java 11 และ Defects4J 3.0.1 ใช้งานได้แล้ว',
    'PDF โจทย์กำหนด Java projects ทุกรายการ แต่ไม่ได้ระบุจำนวน bugs ต่อ project ข้อสรุปของชุดทดลองนี้จำกัดอยู่ใน sample ที่ระบุ', '',
]
if algorithms < planned_algorithms:
    lines.insert(lines.index('## งานที่เหลือ') + 2, f'- รอ algorithm runs ที่กำลังทำงานให้ครบ ({algorithms}/{planned_algorithms}) และ audit ผลสุดท้าย')
(ROOT / 'docs/submission-checklist.md').write_text('\n'.join(lines), encoding='utf-8')
print(f'algorithm: {algorithms}/{planned_algorithms}; AI: {ai}/{planned_ai}; projects: {projects}')
