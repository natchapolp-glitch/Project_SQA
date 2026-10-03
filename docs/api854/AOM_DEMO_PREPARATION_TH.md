# บทเดโมของออม — การเตรียมงานและหลักฐาน

ใช้บทนี้ซ้อมส่วนที่ทำได้โดยไม่รอบีม/แชมป์ เป็น Markdown runbook ยังไม่ได้อัดวิดีโอหรือเปิด primary experiment

สไลด์ประกอบ: `output/api854-20261003/AOM_SQA_854_Preparation_DRAFT_20261003.pptx` มี 9 หน้า เปิดแผน/ขอบเขต หลักฐาน source/dashboard การซ้อมกู้คิว และรายการที่ยังรอทีม ผล coverage/fault efficiency ยังรอ primary snapshot จึงใช้เป็นสไลด์ซ้อมก่อนเปิดผลจริง

## เดโมที่ทำได้ตอนนี้ ประมาณ 4–5 นาที

**0:00–0:40 — ขอบเขต:** “เราวางแผนใช้ 854 bugs, สี่วิธี, หนึ่งรอบ รวม 3,416 jobs ออมรับผิดชอบ 284 bugs ส่วนที่แสดงวันนี้คือ source/build preparation กับความทนทานของคิว ยังไม่ใช่ผลเปรียบเทียบ AI หรือ algorithm”

**0:40–1:40 — หน้าสถานะ:** เปิด `output/api854-20261003/aom-owner-progress.html` จากไฟล์ในเครื่อง ดูจำนวน source/compile และ primary แยกกัน ค้นหา Chart และเลือกสถานะ ใช้ปุ่มโหลด `aom-owner-sources-final/index.json` สำหรับสถานะรวมล่าสุด หากเปิดผ่าน localhost หน้าจะอ่าน index ในเครื่องทุก 10 วินาที หน้านี้ไม่ใช้ KKU และไม่เชื่อม live queue

**1:40–2:50 — ตัวอย่าง source proof:** เปิด `aom-owner-sources-v1/Chart-3/record.json`, `revision-proof.json`, `context/context-manifest.json` และ `compile/command.json` แสดง HEAD ตรง fixed tag, selected source ไม่มี diff, hashes ของ files และ exit code/timeouts ของ compile Source นี้ยังต้องผ่าน shared target/fixture review ก่อนใช้ generation

**2:50–3:50 — ซ้อมกู้คืน:** เปิด `aom-recovery-preparation-v3.json` กับ `aom-recovery-public-evidence-v3/evidence.json` อธิบายว่า worker subprocess หยุดฉับพลันจริง แล้ว reopen private store, จำลอง lease expiry, reclaim job เดิมด้วย attempt ใหม่, fence worker เก่า และตรวจ artifact เดิมอยู่ครบ แสดง attempts สองครั้งของ job เดิมและ hashes ของ offline artifacts เป็นการทดสอบ same-host บนคิวแยก ยังไม่ได้ตรวจ cross-machine และไม่ได้ dispatch AI

**3:50–4:30 — สิ่งที่จะทำต่อ:** เปิด `AOM_CONTINUATION_V5_TH.md` อธิบายว่ารอ reviewed recipes ของ pilot อีก 15 bugs และ actual model settings/limits/quota/framing/reserve จึงจะ freeze condition และเปิด pilot ร่วมทีม ห้ามพูดว่าทดลองครบเพราะ checkout/compile ผ่าน

## ส่วนเดโมผลจริงที่จะเติมภายหลัง

เลือก job จริงจาก frozen snapshot หลัง Gate A แล้วแสดง shared input hashes → actual generator/model/settings → suite hash → fixed สองรอบ → buggy → coverage → stage executed/skipped/target-check counts → semantic review → matched comparison หากตรวจไม่พบบั๊กให้แสดงตามจริง และใช้ result/logs ชุดเดียวกันกับรายงาน

ส่วนนี้ยังไม่มีผล primary ใหม่ ห้ามสวม development suite เป็นผล KKU ใช้แถบ “development evidence” หากจำเป็นต้องสาธิต pipeline ด้วยผลเดิมของทีม

## คำสั่งซ้อม recovery ใหม่อย่างปลอดภัย

รันจาก repository root ด้วย Python ที่มีอยู่ เลือกชื่อ private directory/receipt ที่ยังไม่มี เพื่อเก็บการซ้อมครั้งก่อนโดยไม่เขียนทับ:

```powershell
python -X utf8 -m scripts.study.api854.rehearse_owner_recovery --root .local/api854/aom-recovery-demo-new --receipt output/api854-20261003/aom-recovery-demo-new.json
```

ห้ามเปลี่ยน `--root` ไปเป็น live controller store การซ้อมสร้างเพียง offline dummy artifact, สี่ prepare jobs และสอง attempts ไม่ใช่ measured suites หรือผลทดลองหลัก

ก่อนอัดวิดีโอ ปิด private claims, database, keys และหน้าบัญชี ใช้เฉพาะ public receipt/source proof/dashboard ในบทนี้
