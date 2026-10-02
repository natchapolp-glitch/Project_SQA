from pathlib import Path
root=Path(__file__).resolve().parents[2]
report=root/'scripts/reporting/build_kku_analysis_v6_deadline.py'
deck=root/'scripts/reporting/build_kku_slides_v6_deadline.mjs'
if report.exists() or deck.exists(): raise ValueError('Builder version already exists')
text=(root/'scripts/reporting/build_kku_analysis_v5.py').read_text(encoding='utf-8')
text=text.replace('report-20261002_HAIKU.md','report-20261002_DEADLINE.md')
text=text.replace('## การทำต่อด้วยบัญชีใหม่และ Claude Haiku','## ประวัติ checkpoint 181 รอบ ก่อนการทำต่อช่วงเย็น')
addition='''    haiku = [r for r in done if r['approach']=='kku-claude' and r.get('model')=='claude-haiku-latest']
    sonnet = [r for r in done if r['approach']=='kku-claude' and 'sonnet' in (r.get('model') or '').lower()]
    secondary_audit=json.loads((OUT/'clarified-evidence-audit-20261002.json').read_text())
    latest=[
        '## สถานะล่าสุดสำหรับส่งภายใน 23:00 วันที่ 2 ตุลาคม',
        f'ผลหลักเงื่อนไข prompt เดิม: {len(done)}/204 รอบ; CMA-ES และ FSCS-ART อย่างละ 51/51 รอบ และ Gemini 51/51 รอบ Claude ยังขาด {51-len([r for r in done if r["approach"]=="kku-claude"])} รอบ ไม่อ้างว่างานทดลองครบทั้งหมด',
        f'Claude ในผลหลักรวมหลายรุ่น: Haiku {len(haiku)} รอบ ครอบคลุม {len({r["project"] for r in haiku})}/17 โปรเจกต์ และผล Sonnet เดิม {len(sonnet)} รอบ แยกชื่อรุ่นตาม manifest ไม่เรียกผล Sonnet ว่า Haiku หากใช้เงื่อนไข Haiku เท่านั้น ผลหลักที่ผ่านรวม {len(done)-len(sonnet)}/204 รอบ ตารางรวมตระกูล Claude ด้านล่างยังมี Sonnet จึงใช้เป็นผลประวัติและต้องอ่านการแยกรุ่นนี้ประกอบ',
        'ผลเพิ่ม Time-1/101: คำชี้แจงบริบทตามรายวิชาที่ผู้ใช้อนุญาต เป็น prompt iteration 2 มี 30 เทส ผ่าน fixed ซ้ำ รัน buggy และ coverage ครบ ไม่พบ sampled fault; line 36/305, branch 9/122 เก็บใน kku-clarified-20261002 แยกจากผลหลัก ไม่บวกเป็นรอบอิสระของ prompt เดิม',
        f'ผลเพิ่มเติมผ่าน execution audit {secondary_audit["passed_records"]}/{secondary_audit["completed_records_audited"]} รายการ JxPath-1/103 iteration 2 ได้คำตอบเชิงอธิบายแต่ไม่มี Java source จึง generation_failed และไม่มี coverage ที่อนุมานขึ้น',
        'หน้า KKU ของบัญชีที่ผู้ใช้เปิดให้แสดง Claude Haiku quota 100% หลังคำตอบ JxPath ช่วง 20:17 น. ดูภาพและ request/operator metadata ที่ capture; ไม่คาดเดาเวลาที่โควต้ารีเซ็ต',
        'JacksonXml-1/102 ซ่อมจากคำตอบ Haiku เดิมด้วย policy v84: import ของ Woodstox, cast ของ null overloads, helper catch ที่ compiler ไม่ยอมรับ, ตัดทั้งเมธอดที่ใช้ API ไม่มีในรุ่นนี้ และเลื่อน XML fixture ไป START_ELEMENT ตาม fixed setup failure ก่อนจำกัด 30 และ fixed-only pruning เหลือ 26 เทส Assertions ที่เก็บไว้ไม่เปลี่ยน คำตอบต้นฉบับมี 58 methods แต่ raw count ใน record เป็น 57 หลัง compatibility exclusion',
        'JacksonDatabind-1/101 ใช้การซ่อม fixture/import แบบประกาศเวอร์ชันก่อนรันและเก็บ failed attempts v85-v88 ตาม fixed diagnostics ไม่ส่ง diagnostics ให้ AI ไม่เปลี่ยน assertions; ใช้สถานะใน manifest ล่าสุด ไม่อนุมานว่าทุกการซ่อมสำเร็จ',
        'ภาพ PNG เพิ่ม 7 รายการเป็นการแปลงภาพ JPEG ต้นฉบับที่มีอยู่ให้ format ตรงกับ auditor โดยคง pixels และ JPEG เดิม ไม่ได้สร้างภาพหลักฐานย้อนหลัง ดู image-format-receipt.json',
        'ชุดส่งคืนนี้จำกัด 17 บัค งานทดลอง Chart-1 ใหม่ในชุด bugs850 และแผน 850 บัคไม่รวมในตัวเลขของชุดนี้ และยังไม่ได้ส่ง Classroom',
    ]
    lines[4:4]=latest
'''
text=text.replace("    rendered = ''",addition+"    rendered = ''")
report.write_text(text,encoding='utf-8')
text=(root/'scripts/reporting/build_kku_slides_v5.mjs').read_text(encoding='utf-8')
text=text.replace('tmp/kku-presentation-haiku','tmp/kku-presentation-deadline')
text=text.replace('SQA_Round2_KKU_Only_20261002_HAIKU.pptx','SQA_Round2_KKU_Only_20261002_DEADLINE.pptx')
text=text.replace('explicitTotalSlideCount:16','explicitTotalSlideCount:17')
text=text.replace("'ส่ง exact original prompt ผ่าน gen.ai.kku.ac.th/chat เท่านั้น'", "'ผลหลักใช้ prompt เดิมผ่าน KKU / clarification แยกเงื่อนไข'")
text=text.replace("'ผลเดิมใช้เฉพาะ audited KKU Claude/Gemini และแยก model label'", "'Haiku เป็นรุ่นสำหรับงานใหม่ / Sonnet เดิมแยกตาม model label'")
text=text.replace("'บัญชีใหม่ใช้ Haiku ต่อ / โควตาที่สังเกต 93.5%'", "'Claude Haiku ในบัญชีปัจจุบันถึงโควต้า 100% แล้ว'")
text=text.replace("'Claude: 30 methods / fixed ผ่านซ้ำ / พบ assertion failure บน buggy'", "'Claude Sonnet เดิม: 30 methods / พบ assertion failure บน buggy'")
anchor="page('ผู้จัดทำ',["
extra="""const haikuDone=data.rows.filter(r=>r.approach==='kku-claude'&&r.status==='complete'&&r.model==='claude-haiku-latest');
page('ผลเพิ่มเติมและการแยกรุ่น Claude',[
  `ผลหลัก Haiku ${haikuDone.length}/51 รอบ / Sonnet เดิม 5 รอบ แยกไว้`,
  'Time-1/101 clarification: 30 เทส / fixed ผ่าน / ไม่พบ fault',
  'Time เพิ่มแยกเงื่อนไข ไม่บวกเป็น prompt เดิมหรือบัคใหม่',
  'JxPath clarification ไม่มีโค้ดเทส / งานยังไม่ครบทุกเงื่อนไข'
],source+'\\nUpdated source: docs/HAIKU_CLARIFICATION_RESULT_20261002_TH.md; clarified-evidence-audit-20261002.json; source-processing policies v84-v88. Actual current completion is in summary.json. Five Sonnet results remain historical; no relabelling as Haiku.');
"""
text=text.replace(anchor,extra+anchor)
text=text.replace("const source='Sources:","const source='Latest deadline preparation preserves original-prompt primary results and separate coursework clarification. Current Haiku quota 100%; historical checkpoint statements below refer to the earlier 181-run package. Sources:")
deck.write_text(text,encoding='utf-8')
print(report)
print(deck)
