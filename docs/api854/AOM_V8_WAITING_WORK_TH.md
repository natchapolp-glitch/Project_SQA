# ออมทำงานระหว่างรอทีม: host/input readiness และชุดคำสั่งส่งต่อ

วันที่ 3 ต.ค. 2569 ทำงานที่ตรวจได้เองต่อจาก v8 `e95e979b`
ยังไม่มี candidate acceptance ใหม่บน remote branches จึงคง shared inputs/runtime/protocol v8 เดิม

## งานที่ออมทำเสร็จในรอบนี้

1. ตรวจ input bindings ใหม่ครบ 20 bugs: selected 379, unsupported 312, denominator 691
   runtime, recipes, index, protocol และ runner ตรงกัน; routes ครบ 10,248 stage keys ของ allocation
2. ตรวจ WSL environment ใหม่: Java/javac 11, Defects4J 3.0.1 และ prerequisites ผ่าน
   ทดสอบ cross-process CPU lock จริง: worker ที่สองถูกกัน exit 9 และใช้ slot ได้หลัง release exit 0
   เครื่องออมใช้หนึ่ง CPU slot และ shared root เดียวตาม receipt
3. ตรวจพบ controller/tunnel เดิมหยุด: สำรอง SQLite แบบ consistent ไว้ส่วนตัว
   กู้ controller ด้วย persisted state เดิมโดยไม่ seed และเปิด quick tunnel ใหม่
4. ตรวจ authenticated GET health/schema/status ผ่านทั้ง local/public รวม 6 requests
   งานคง **80 prepare/queued, 0 attempts**; enabled stage มีเพียง prepare ของ frozen core
   v8 proposal ยัง `enabled_stages=[]` และไม่มีการผูก queue ใหม่เข้ากับ v8
5. เตรียมคำสั่งงานแยกให้เพื่อนและ return templates ที่ผูก exact inputs
   รวม 40 คู่ bug × model สำหรับแชมป์ตรวจ accounting/limits โดยไม่สั่ง generation

หลักฐานอยู่ใน [waiting-work bundle](../../output/api854-20261003/aom-v8-waiting-work-v1/checksums.json):

- [input audit / Gate A checklist](../../output/api854-20261003/aom-v8-waiting-work-v1/composition-audit/gate-a-checklist.json)
- [WSL environment](../../output/api854-20261003/aom-v8-waiting-work-v1/environment/environment.json)
- [aom-pc1 host receipt](../../output/api854-20261003/aom-v8-waiting-work-v1/aom-host-receipt.json)
- [queue ก่อนกู้](../../output/api854-20261003/aom-v8-waiting-work-v1/queue-before-recovery.json)
- [queue recovery receipt](../../output/api854-20261003/aom-v8-waiting-work-v1/queue-recovery-receipt.json)
- [task manifest](../../output/api854-20261003/aom-v8-waiting-work-v1/team-task-manifest.json)

host receipt เป็น preflight ของเครื่องออม ไม่ใช่การเซ็นรับทั้งทีม
ยังรอ host acceptance ของบีม/แชมป์และ review ของ shared condition
การกู้ transport ไม่ใช่การเปิด Gate A/pilot; รอบนี้ไม่มี KKU requests, seed หรือ claims

Regression รอบนี้รัน 12 tests: ผ่าน 11, ข้าม Math production probe บน Windows 1
หลักฐาน WSL/JUnit ของ v8 เดิมยังตรง runtime เดิม; ไม่เปลี่ยน labels เป็นผลใหม่ของ primary
ตรวจ role Beam ด้วย connection_check จริงผ่านด้วย
[verification receipt](../../output/api854-20261003/aom-v8-waiting-work-v1/verification-receipt.json)
บันทึกการตรวจ templates, document links, bindings และการกัน credentials ออกจากไฟล์สาธารณะ

## คิวที่ใช้อยู่หลังการกู้

**URL ใหม่:** `https://fax-stake-salvador-experts.trycloudflare.com`

health/schema/status ต้องใช้ Authorization จาก role access file ส่วนตัวที่เคยได้รับ
เปลี่ยนเฉพาะ `base_url` เป็น URL ใหม่; tokens เดิมยังใช้ได้
ออมอัปเดต access files ในเครื่องตัวเองแล้ว ไม่ใส่ credentials/SQLite backup ใน Git
URL นี้เป็น quick tunnel ชั่วคราว ต้องมี controller/cloudflared ทำงานบนเครื่องออม
ถ้าปิดเครื่องหรือ restart tunnel ให้ตรวจและแจ้ง URL ใหม่ก่อนเพื่อนเชื่อมต่อ
การเปิด URL เปล่าโดยไม่มี token อาจได้ invalid_worker_token ซึ่งไม่แทนการตรวจ authenticated health

## ส่งคำสั่งให้เพื่อน

- **บีม:** ส่ง [TASK_TO_BEAM_V8_TH.md](TASK_TO_BEAM_V8_TH.md)
  ให้ตรวจ Math composition/semantic/host และร่วมกับแชมป์ส่ง scoped acceptance ของ setter/JDOM และ candidate ถัดไป
- **แชมป์:** ส่ง [TASK_TO_CHAMP_V8_TH.md](TASK_TO_CHAMP_V8_TH.md)
  ให้ตรวจ 40 คู่ prompt × model, actual limits/settings/quota/framing/reserve และ co-review candidate

ทั้งสองส่ง branch + pushed commit + receipt paths/hashes กลับมา
ออมจะรวมเฉพาะ candidate ที่มี verdict ทั้งสองคนเป็น condition ใหม่ แล้วให้แชมป์วัด reserve ใหม่อีกครั้ง
templates ตั้ง `example_only=true`, verdicts null/pending; ไม่ใช่ receipts ที่เซ็นรับแล้ว

Gate A ที่ยังรอ: 312 unsupported declarations, condition-bound semantic review,
provider limits/reserve/current quota, runner host acceptance และ three-owner review
enum สี่ signatures คง denominator และรอคำตัดสินร่วม; primary results added=0
