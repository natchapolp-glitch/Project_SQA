"""Create the current deck builder from the existing editable design."""
from pathlib import Path
root=Path(__file__).resolve().parents[2]
target=root/'scripts/reporting/build_kku_slides_v3.mjs'
if target.exists(): raise ValueError('Version 3 already exists')
text=(root/'scripts/reporting/build_kku_slides.mjs').read_text(encoding='utf-8')
text=text.replace("const source='Sources:","const provenance=JSON.parse(await fs.readFile(path.join(out,'provenance-audit-current.json'),'utf8'));\nconst source='Sources:")
text=text.replace("text(s,analysis.matched_groups.map(g=>`${g.project}/s${g.index}`).join(', '),76,558,1120,72,24);", "text(s,`${analysis.matched_groups.length} กลุ่มจาก ${new Set(analysis.matched_groups.map(g=>g.project)).size} projects / รายการเต็มอยู่ใน analysis.json`,76,558,1120,72,24);")
text=text.replace("'v30–v37 กู้ prefixes และแก้ fixed API compatibility'", "'เก็บ driver/source snapshots และเวอร์ชัน processing ของแต่ละ run'")
text=text.replace("Policies: results/study/kku-only-20261001/processing-policy-v30..v37.json.", "Policies and snapshots: results/study/kku-only-20261001/processing-policy-v*.json and generation/processing-sources. Missing historical policy files are not reconstructed as prior evidence.")
text=text.replace("'Claude ใน KKU แสดง daily usage 100% ระหว่าง Compress/101'", "'โควตาและ server busy จำกัดการเก็บคำตอบผ่าน KKU'")
text=text.replace("'Server busy, final output ว่าง และ Java ถูกตัดท้าย เก็บตามจริง'", "`Provenance ยังมี ${provenance.issues.length} issues / ดู audit receipt ล่าสุด`")
text=text.replace("'Missing/failed metrics เป็น null / ไม่อ้างว่าครบแผน'", "'ภาพบางรอบไม่อยู่ในชุดที่ได้รับ / missing metrics เป็น null'")
text=text.replace("'SQA_Round2_KKU_Only_v2.pptx'", "'SQA_Round2_KKU_Only_20261002.pptx'")
target.write_text(text,encoding='utf-8')
render=(root/'scripts/reporting/render_kku_slides.mjs').read_text(encoding='utf-8').replace('SQA_Round2_KKU_Only_v2.pptx','SQA_Round2_KKU_Only_20261002.pptx').replace('final-previews','final-previews-20261002')
(root/'scripts/reporting/render_kku_slides_v3.mjs').write_text(render,encoding='utf-8')
print(target)
