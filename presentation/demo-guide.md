# แนวทางสาธิตงานรอบ 2

1. เปิด `output/submission/report.md` และชี้ขอบเขตข้อมูลจริง รายการที่ยังขาด
   และความต่างระหว่าง pilot เดิมกับการทดลองแบบ prospective
2. เปิด config, manifest และ target inventory เพื่อแสดง bug, seed และ budget
3. เปิด generation/observations.json เทียบกับ GeneratedStudyTest.java
   อธิบายว่า expected value มาจาก fixed revision และตรวจซ้ำก่อนสร้าง assertion
4. เปิด evaluation/record.json และ logs ของ fixed-1, fixed-2 และ buggy
   อธิบายความต่างระหว่างทดสอบไม่ผ่านกับ infrastructure error
5. เปิด coverage/summary.csv และ coverage.xml พร้อมสูตรที่ใช้คำนวณ
6. แสดง prompt, raw response และ metadata จริงของ AI ทั้งสองเครื่องมือ
   พร้อม source-processing ledger อธิบายว่าเป็น AI-assisted ที่มี local processing
7. สร้างรายงานใหม่ด้วยคำสั่งด้านล่าง แล้วชี้ว่า missing ไม่ถูกเปลี่ยนเป็นศูนย์

```bash
python3 scripts/reporting/aggregate.py --results results/study/round2-v4-20260929 \
  --manifest results/study/round2-v4-20260929/study-manifest.csv
python3 scripts/reporting/audit_evidence.py
```

## ตัวอย่างที่เปิดสาธิตได้จากผลจริง

- Cli / CMA-ES seed 101: เปิด `results/study/round2-v4-20260929/Cli/cmaes-s101-b30/`
  แสดง fixed ผ่านสองครั้ง และ `buggy/failing_tests` เทียบกับ generated methods
  อธิบายการแก้ JUnit/Hamcrest dependency พร้อม log เดิมใน results/validation
- JacksonCore / FSCS-ART seed 101: เปิด source และ coverage เพื่อแสดงผลหลังแก้
  API eligibility กับ oracle ที่ยาว โดยแยกผล v3 ที่ล้มเหลวไว้ตรวจย้อนหลัง
- JxPath / CMA-ES seed 101: แสดง history ที่ตัด generated21 ซึ่งไม่ผ่าน fixed
  เพราะ object identity hashCode อธิบายว่าทำไมไม่ควรนับเป็นการตรวจพบบั๊ก
- Csv / Claude และ IntelSphere run-index 101: เปิด
  `ai-tests/provider-captures/claude/Csv-1/s101-i1/` และ
  `ai-tests/provider-captures/intellisphere/Csv-1/s101-i1/` แสดง response จริง
  เทียบกับ tests หลัง processing และ records ใน `results/study/round2-v4-20260929/Csv/`
  ทั้งสองมี fixed ผ่านสองครั้งและ buggy assertion failure พร้อม coverage จริง
- เปิด `output/submission/ai-provider-attempts.csv` และ `incomplete-runs.csv`
  แสดง parser/compile failures และ UI server busy ไม่ใช้ผลที่ไม่ครบจัดอันดับ
- แสดง `ai-tests/provider-status.json` เพื่ออธิบายข้อจำกัดโควตา และชี้ว่า
  AI generation time เป็น UI observation upper bound ไม่ใช่ model compute time

เวลานำเสนอโดยประมาณ: ขอบเขต/วิธี 2 นาที ผลและตัวหาร 3 นาที เปิดหลักฐาน 3 นาที
บทเรียน/ข้อจำกัด/AI 2 นาที ให้ทีมปรับตามเวลาที่ผู้สอนกำหนดจริง

สำหรับ live run ใช้ run-id ใหม่เสมอ การรันใช้เวลาตามขนาดโปรเจกต์ จึงควรมี
ผลรันที่ตรวจสอบไว้ล่วงหน้าพร้อม log ให้เปิดอธิบายโดยไม่กล่าวว่าเป็นการรันสด

```bash
python3 scripts/study/run.py run --d4j "$D4J_HOME/framework/bin/defects4j" \
  --worktrees "$HOME/sqa-round2/worktrees" --projects Lang --run-id DEMO_UNIQUE_ID
```
