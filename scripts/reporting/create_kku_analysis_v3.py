"""Version the report builder; keep earlier checkpoint builders unchanged."""
from pathlib import Path
root=Path(__file__).resolve().parents[2]
target=root/'scripts/reporting/build_kku_analysis_v3.py'
if target.exists(): raise ValueError('Version 3 already exists')
text=(root/'scripts/reporting/build_kku_analysis_v2.py').read_text(encoding='utf-8')
start=text.index("    lines += ['## การทำต่อด้วยบัญชี")
end=text.index("              'ผลหลัก:", start)
replacement='''    provenance=json.loads((OUT/'provenance-audit-current.json').read_text(encoding='utf-8'))
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
              'เวอร์ชัน v38-v66 เก็บการซ่อม API/fixture การกู้ prefix และ pruning ตาม source snapshots และ policies ของแต่ละ run ตัวอย่าง v38 แก้ชื่อตัวแปร Codec โดยไม่เปลี่ยน expected; v39 จัด XML reader ให้เป็น START_ELEMENT; เวอร์ชันหลังตัด methods ที่ fixed compiler ไม่รองรับ รุ่น v67 สำหรับคำตอบใหม่ใช้ closed Java fences และการประมวลผลทั่วไป ไม่เรียก compatibility repairs ของคำตอบเก่า หากต้องเพิ่ม repair ให้ใช้เวอร์ชันใหม่',
              'Processing drivers ใน completed primary records: '+', '.join(f'{k}: {v}' for k,v in sorted(processing.items())),
              'Response Style ที่บันทึกใน completed primary AI captures: '+', '.join(f'{k}: {v}' for k,v in sorted(styles.items()))+' ค่า not recorded หมายถึงหลักฐานเดิมไม่ได้ระบุ ไม่อนุมานว่า Normal',
              f'Execution evidence audit ล่าสุด: Claude fresh {ca["passed_records"]}/{ca["completed_records_audited"]}, Gemini fresh {ga["passed_records"]}/{ga["completed_records_audited"]} รวม secondary ที่มีอยู่ใน batch ส่วนผล baseline ที่ reuse ตรวจแยก ไม่รวม failures เป็น completed',
              f'Provenance audit ตรวจ primary records {provenance["records_checked"]} รายการ พบ {len(provenance["issues"])} issues: '+', '.join(f'{k}: {v}' for k,v in Counter(i['issue'] for i in provenance['issues']).items())+'. ขอบเขตนี้ต่างจาก execution audit; ดู receipt จริง ไม่ใช้ receipt รุ่นเก่าที่ผ่านเป็นผลของชุดปัจจุบัน',
              'ภาพส่วนตัวของรอบที่เพื่อนทำเพิ่มถูกยกเว้นจาก public Git ผู้ส่งต่อยืนยันว่าได้รับเฉพาะ branch จึงยังไม่มีไฟล์ภาพต้นฉบับเหล่านั้นในเครื่องนี้ raw response, metadata, source archives และ logs ที่ได้รับยังตรวจได้ตามขอบเขต audit ไม่สร้างภาพหรือหลักฐานย้อนหลัง ภาพที่เครื่องนี้เก็บเองยังคงอยู่ใน ZIP ส่วนตัว',
              'เหตุการณ์คืน CRLF ของ algorithm sources, การซ่อม driver metadata และ capture routing เก็บ receipts/bytes ก่อนแก้ใน results/validation ไม่เปลี่ยน experimental outcomes และต้องเปิดเผยข้อจำกัดของประวัติ',
              'งานล่าสุดยังไม่ใช่การส่ง Classroom เจ้าของงานจะส่งเองก่อนเที่ยงคืนวันที่ 2 ตุลาคม 2026 เวลาไทย หากเก็บไม่ครบต้องส่งผลที่ตรวจแล้วพร้อม failed/missing ตามจริง',
              '## แหล่งข้อมูลและการทำซ้ำ',
'''
text=text[:start]+replacement+text[end:]
text=text.replace('audit_kku_provenance.py สำหรับผลใหม่','audit_kku_provenance_v2.py สำหรับผลใหม่')
text=text.replace("(OUT/'report.md').write_text", "(OUT/'report-20261002.md').write_text")
text=text.replace("str(OUT/'report.md')", "str(OUT/'report-20261002.md')")
target.write_text(text,encoding='utf-8')
print(target)
