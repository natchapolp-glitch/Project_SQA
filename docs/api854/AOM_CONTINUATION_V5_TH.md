# ออม: ทำต่อครบทุกบั๊ก — checkpoint 3 ตุลาคม 2026

คำสั่งเจ้าของงานในแชทนี้: ทำต่อบน branch `aom`, push เข้า `aom`, เริ่ม **1 รอบต่อบั๊กต่อวิธี** ก่อนเพิ่มรอบซ้ำ และใช้ **Claude Sonnet 5 / Gemini 3.5 Flash Lite ผ่าน KKU** ตามแผนใหม่ ชุดปัจจุบันยังไม่เปิด generation หรือ primary queue

## แผนและสิ่งที่ทำเพิ่ม

1. ตรวจงานล่าสุดของทีมแล้วรวม branch Beam `3c7c62b8` และ Champ `64418ca0` โดยรักษา shared input guards ของออม, one-host guards ของบีม และ recipe/resolver tests ของแชมป์
2. ตรวจ Defects4J ที่ติดตั้งบนเครื่องปัจจุบันจริง: 854 active bugs / 17 projects ตรง ownership ทุก ID ไม่ใช้ root active-bugs.csv ซึ่งมีข้อมูลเพียงบาง project
3. เครื่องนี้ผ่าน Linux/WSL, Java/Javac 11, Defects4J 3.0.1 และ prerequisites; receipt ใหม่อยู่ `output/api854-20261003/aom-current-host-continuation/` ไม่อ้างว่า receipt เครื่องก่อนเป็นเครื่องนี้
4. ตรวจ received development evidence ของ Closure-176, JxPath-1, Codec-1, Collections-1, Csv-1 รวม 10 suites; hashes, suite/source bytes, fixed twice, buggy/coverage และ executed/skipped/target-check counters ตรงหลักฐานเดิม ผลนี้ไม่ใช่ primary runs ใหม่
5. สร้าง shared preparation **v5** สำหรับห้าบั๊กนี้: 124 capability-selected targets; source/receiver/fixture/recipe/prompt bytes ชุดเดียวกันทั้งสี่วิธี ครบ lineage ไปถึง generation/evaluation; เก็บทุก exclusion และไม่เปิด fallback ไป generic null fixtures
6. เก็บ v1–v4, frozen core และ original experiment packets เดิม ไม่เขียนทับ; v5 เป็น five-bug development proposal เท่านั้น อีก 15 pilot bugs ยังไม่มี reviewed recipes ชุดนี้
7. ออก runner proposal ใหม่ใช้ CPU ออมเครื่องนี้ 1 slot และบีม 1 slot พร้อม Champ API coordinator ถอน aom-pc2 ที่ยังไม่มีหลักฐานจาก proposal ใหม่นี้เท่านั้น ไม่เปลี่ยน assignment ในประวัติเดิม ตรวจ routing 10,248 stage keys ไม่มี gaps

## ตัวเลขที่ต้องใช้สื่อสาร

- เป้าหมายทั้ง cohort: **854 × 4 × 1 = 3,416 jobs**
- Primary ใหม่ที่สำเร็จ: **0**; manifests เป็น held planning ไม่ได้ dispatch
- Received development: 5 bugs / 10 algorithm suites; ไม่ใช่ 10 KKU outputs
- Historical cohort 17 bugs แยกจาก primary ใหม่ ไม่เอาจำนวน test methods มานับเป็นจำนวนบั๊ก
- Prompt v5 สูงสุด 177,698 UTF-8 bytes; เป็น **bytes ไม่ใช่ token count**; ต้องให้แชมป์วัด provider framing/limits/reserve ใหม่ ไม่ใช้ตัวเลข v3/v4 แทน
- ไม่มี real KKU request หรือ live queue mutation ใน checkpoint รอบนี้

## ลำดับที่จะไปถึงการรันจริง

1. บีมส่ง/ตรวจ prospective receiver, arguments, oracle และ execution evidence ของอีก 15 pilot bugs พร้อม immutable source/recipe version; รวมเป็น shared preparation รุ่นใหม่ก่อนใช้ทุกวิธี
2. แชมป์เก็บ observed quota remaining/unit/bucket/window/expiry และ exact model settings/context/output limits/framing ของบัญชีจริง ไม่ส่ง keys ในแชทหรือ Git
3. ออมรวมหลักฐานที่ตรง final input/runtime/runner hashes แล้วตรึง condition ใหม่; ไม่กรอก reviewed=true หรือค่าที่ไม่รู้เพื่อให้ gate ผ่าน
4. เริ่ม pilot 20 bugs × 4 วิธี = 80 jobs ด้วย fixed twice → buggy → target coverage และ semantic review วัดเวลาจริง/ข้อจำกัด แล้วจึงขยายครบ 854 bugs
5. ทำ report/slides/demo จาก frozen results เดียวกัน; รายการ failed/not_attempted ยังต้องเปิดเผยตามจริง

การรันทดสอบระบบ offline หรือการตรวจ packet ไม่แทนผลทดลองจริง หากอาจารย์ต้องครบทุกบั๊กยังต้องทำ primary ให้ครบขอบเขตนั้น งานนี้ไม่ได้รับรองวันเสร็จหรือคะแนน

## จุดเริ่มต่อ

- `output/api854-20261003/aom-continuation-v5/checkpoint.json`
- `output/api854-20261003/aom-continuation-v5/protocol.proposal.json` และ `runner-plan.json`
- `output/api854-20261003/aom-continuation-v5/full-cohort-held-jobs.json`
- `output/api854-20261003/prepare-v5-five-bug-development/index.json`
- `scripts/study/api854/build_prepare_v5.py` (สร้างใหม่ใน output ที่ยังไม่มี)
- `scripts/study/api854/checkpoint_aom_continuation.py` (ตัวสร้าง checkpoint แบบครั้งเดียว)

Validation receipt และ ZIP/SHA256 ของชุดส่งต่ออยู่ `output/api854-20261003/` ชื่อ AOM_Continuation_v5_20261003 ไม่รวม credentials/private state และไม่ใช้ ZIP เป็นหลักฐานว่า primary รันแล้ว
