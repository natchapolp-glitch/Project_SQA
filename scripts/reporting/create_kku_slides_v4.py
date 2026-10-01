from pathlib import Path
root=Path(__file__).resolve().parents[2]
target=root/'scripts/reporting/build_kku_slides_v4.mjs';assert not target.exists()
text=(root/'scripts/reporting/build_kku_slides_v3.mjs').read_text(encoding='utf-8')
text=text.replace('SQA_Round2_KKU_Only_20261002.pptx','SQA_Round2_KKU_Only_20261002_COMPLETE.pptx')
text=text.replace('ภาพบางรอบไม่อยู่ในชุดที่ได้รับ / missing metrics เป็น null','ได้รับภาพที่ขาด 54 รายการแล้ว / failed-missing metrics ยังเป็น null')
text=text.replace('ข้อจำกัดบริการและผลที่ยังขาด','สถานะหลักฐานและผลที่ยังขาด')
text=text.replace("'Provenance ยังมี ${provenance.issues.length}","'Provenance ยังมี ${provenance.issues.length}")
text=text.replace('Provenance ยังมี ${provenance.issues.length} issues / ดู audit receipt ล่าสุด','Provenance audit: ${provenance.issues.length} issues / ภาพและ hashes ตรวจแล้ว')
target.write_text(text,encoding='utf-8')
render=(root/'scripts/reporting/render_kku_slides_v3.mjs').read_text().replace('SQA_Round2_KKU_Only_20261002.pptx','SQA_Round2_KKU_Only_20261002_COMPLETE.pptx').replace('final-previews-20261002','final-previews-20261002_COMPLETE')
(root/'scripts/reporting/render_kku_slides_v4.mjs').write_text(render,encoding='utf-8')
