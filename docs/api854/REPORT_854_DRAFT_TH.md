# ร่างรายงาน: เปรียบเทียบการสร้างเทสบน Defects4J 854 บั๊ก

สถานะ: โครงรายงานก่อนเปิด primary experiment ไม่ใช่รายงานผลสำเร็จ ส่งต่อพร้อม `AOM_CONTINUATION_V5_TH.md` และหลักฐาน source preparation ของออม ห้ามนำช่องว่างด้านล่างไปตีความเป็นผลเท่ากับศูนย์

## วัตถุประสงค์และขอบเขต

เปรียบเทียบ CMA-ES, FSCS-ART, Claude Sonnet 5 และ Gemini 3.5 Flash Lite โดย AI ใช้ผ่าน KKU เท่านั้น บัญชีจริงต้องยืนยัน exact model ID/settings/limits ก่อนตรึง condition

ใช้ installed Defects4J 3.0.1 active inventory ที่ตรวจตรง ownership: 854 bugs ใน 17 projects เริ่ม 1 รอบต่อ bug ต่อวิธี รวม 3,416 job keys ออมรับผิดชอบ 284 bugs / 1,136 job keys เป้าหมายเหล่านี้เป็นจำนวนงานที่วางแผน ไม่ใช่จำนวนที่ทำสำเร็จ

คำว่า bug, job, attempt และ test method มีความหมายต่างกัน: bug คือรุ่นบั๊กใน dataset; job คือ bug × วิธี × condition × repeat; attempt คือการเริ่มทำ stage ซึ่งอาจมี retry; test method คือเมธอดใน suite จำนวนเมธอดมากไม่เพิ่มจำนวนบั๊กที่ครอบคลุม

ผลชุดเดิมที่เลือก 17 bugs เก็บเป็น historical cohort แยกต่างหาก ผล development ของทีมและการทดสอบ runner ไม่รวมเป็น primary ใหม่

## วิธีทดลอง

1. ตรวจ installed inventory, environment, ownership และ source/build revision
2. ตรวจ shared target declarations, exclusions, receiver/arguments/oracle recipes และ prompt policy ให้ทั้งสี่วิธีใช้เงื่อนไขเดียวกัน
3. ตรึง final protocol, runtime hashes, model/settings และ measured quota/framing/reserve แล้วตรวจ Gate A ร่วมทีม
4. ทดลอง pilot 20 bugs × 4 วิธี วัดเวลาจริง และตรวจความถูกต้องก่อนขยาย cohort
5. ทุก suite ที่สร้างได้ต้องคง bytes เดิม: fixed ครั้งที่ 1 → fixed ครั้งที่ 2 → buggy → target coverage และ semantic/target-execution review เก็บ stage logs/counters และ hashes ทุกจุด
6. แยก failed, timeout, rejected, excluded, not_attempted และ pending_review; ไม่แก้ assertions หรือคัดเทสเพื่อทำให้ผ่านใน condition เดิม

Source preparation ของออมมีเพียง fixed Java/build context และ compile evidence ยังไม่มี reviewed declaration/fixture/oracle และไม่ถือว่าเป็น shared generation packet ที่พร้อมใช้ AI

## ผลที่จะเติมเมื่อมี frozen primary snapshot

Snapshot SHA256: **รอหลักฐาน**; final protocol SHA256: **รอ freeze**; ช่วงเวลารันจริง: **รอหลักฐาน**

| วิธี | งานตามแผน | เริ่มจริง | ผล terminal | Usable suites | ตรวจพบบั๊ก | Line coverage | Condition coverage |
|---|---:|---:|---:|---:|---:|---:|---:|
| CMA-ES | 854 | รอหลักฐาน | รอหลักฐาน | รอหลักฐาน | รอหลักฐาน | — | — |
| FSCS-ART | 854 | รอหลักฐาน | รอหลักฐาน | รอหลักฐาน | รอหลักฐาน | — | — |
| KKU Claude Sonnet 5 | 854 | รอหลักฐาน | รอหลักฐาน | รอหลักฐาน | รอหลักฐาน | — | — |
| KKU Gemini 3.5 Flash Lite | 854 | รอหลักฐาน | รอหลักฐาน | รอหลักฐาน | รอหลักฐาน | — | — |

รายงาน matched comparison เฉพาะ bugs ที่มี usable results ครบทั้งสี่วิธี พร้อมจำนวน/สัดส่วนจาก 854 แยกจาก all available results แต่ยังแสดง failures และ missing work ของทุกวิธี ห้ามลบทิ้งจาก denominator ของงานตามแผน

Coverage แสดง covered/total ของ target classes และระบุว่า macro (เฉลี่ยสัดส่วนต่อ usable bug) หรือ micro (รวม covered หารรวม total) denominator เป็นศูนย์หรือไม่มี measurement ให้เก็บ null ไม่ใช่ 0% Fault detection rate ใช้ detected/eligible usable suites พร้อมแสดงจำนวนจริง การผ่าน fixed แล้วไม่พบบั๊กเป็นผลที่ถูกต้องได้

เวลา generation/evaluation/end-to-end, retries และ actual API usage แยกตามหลักฐานที่วัดได้ Missing usage, quota และ ETA คง null จนมีข้อมูลจริง ห้ามใช้ UTF-8 bytes แทน token counts หรืออ้าง cost efficiency โดยไม่มีหน่วยและสูตรที่ตรึงไว้

Fault efficiency ตาม assignment ให้ทีมยืนยันนิยามและ denominator ใน final protocol ก่อนคำนวณ และรายงานควบคู่ fault detection counts/rate ไม่เปลี่ยนชื่อ metric เดิมเพื่อให้ดูครบ

## ข้อจำกัดและความเป็นธรรม

- หนึ่งรอบต่อ bug ยังไม่เพียงพอวัดความแปรปรวนของ stochastic methods; รอบซ้ำภายหลังต้องมี repeat index และ provenance ของตัวเอง
- Target exclusions, unsupported receivers และ weak oracles ต้องเปิดเผยพร้อม reasons ไม่อ้างว่าครอบคลุม API ทั้งหมด
- Pilot/development/source build evidence ไม่รับรองผล primary 854 bugs
- Model availability/quota/runtime ต่างเครื่องต้องเปิดเผยตาม observed conditions ไม่สร้างข้อมูลเพื่อปิด gate

## ชุดส่งและการทำซ้ำ

เมื่อมีผลจริง ให้ freeze inventory/protocol/records/evidence หนึ่ง snapshot แล้วสร้างรายงาน สไลด์ และ ZIP จากชุดเดียวกัน ตรวจ hashes/ZIP integrity และเก็บ private credentials/provider screens แยกจาก Git ก่อนทีมส่ง Classroom ด้วยตนเอง

เครื่องมือรายงานเดิม `scripts/study/api854/report.py` ยังมีข้อความชื่อ Haiku แบบคงที่ใน template จึงต้องตรวจ/ปรับสำหรับ final condition ก่อนใช้กับ Sonnet 5; ไม่แก้ frozen implementation เดิมระหว่างงานที่ผูก hashes อยู่
