# SQA Review and Three-Person Parallel Implementation Plan

> **แผนเก่า — เปลี่ยนขอบเขตแล้ว:** ใช้ `2026-10-02-sqa-api854-three-person-parallel.md` ในโฟลเดอร์นี้เป็นแผนปัจจุบัน: 854 active bugs × 4 วิธี ผ่าน KKU API 10 บัญชี แผนนี้เก็บเป็นประวัติของชุด 17 bugs

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:executing-plans when implementing this plan. แผนนี้แบ่งงานให้คน 3 คนทำพร้อมกัน โดยแต่ละคนมีเจ้าของไฟล์ชัดเจน

**Goal:** ทำชุดทดลอง 17 โปรเจกต์ให้ตรวจสอบย้อนกลับได้ พร้อมรายงานเปรียบเทียบ 2 อัลกอริทึมและ 2 AI และชุดส่งงานที่ตัวเลขตรงกับหลักฐาน

**Architecture:** คงผลทดลองเดิมเป็นฐาน แยกงานตรวจเทส งานวิเคราะห์ และงานเอกสารออกจากกัน ส่งต่อผลผ่าน manifest ที่ระบุแหล่งข้อมูลและ hash แล้วรวมงานหลังตรวจรับ ไม่เขียนทับหลักฐานเดิม

**Tech Stack:** Defects4J, Java/JUnit, Python study/reporting scripts, KKU IntelSphere Claude/Gemini, Git branch test

**Spec:** `C:/Users/ACER/Downloads/SQA_Project_2026 (2).pdf` ข้อ 2.2 และคำขอผู้ใช้ให้ตรวจ Closure/JxPath พิจารณาหนึ่งรอบ และตรวจความครบ 17 โปรเจกต์

## Global Constraints

- งานข้อ 2.2 ต้องมี 2 อัลกอริทึม + 2 AI พร้อมผล Coverage/Performance การเปรียบเทียบ และหลักฐานทำซ้ำได้
- เอกสารระบุให้ทดสอบโปรเจกต์ใน Defects4J ทุกรายการ แต่ไม่ได้กำหนด 850 bugs หรือจำนวนรอบซ้ำ ขอบเขตปัจจุบันคือ 1 bug ต่อโปรเจกต์ จึงยังจำกัดการสรุปภาพรวมของ benchmark
- AI ใช้ Claude และ Gemini บน KKU IntelSphere เก็บชื่อโมเดลจริงทุกครั้ง หาก Sonnet server busy จึงใช้ Haiku ตามคำสั่งล่าสุดของผู้ใช้
- ส่ง AI เฉพาะ prompt/ข้อมูลต้นฉบับที่ได้รับอนุญาต ห้ามส่ง compile/test logs เพิ่มตามข้อจำกัดที่ผู้ใช้เลือกไว้
- คงคำตอบดิบ prompt ภาพ และผลล้มเหลว ห้ามทำเทสผ่านด้วยการปิด assertions หรือเขียนเทสใหม่เองแล้วอ้างว่า AI สร้าง
- fixed revision ใช้ตรวจความใช้ได้ของเทส; buggy revision ใช้วัดการตรวจพบบัค ไม่ต้องบังคับให้ทุกเทสผ่านบน buggy
- ห้ามปรับ assertions จากผลบน buggy เพื่อทำให้คะแนนดีขึ้น การตัดเทส/แก้ compatibility ต้องมีรายการก่อนและหลัง
- ภาพหลักฐานที่มีบัญชี/ข้อมูลส่วนตัวเก็บในชุดส่งงานที่ตรวจสิทธิ์แล้ว ตรวจภาพก่อนนำขึ้น GitHub
- งานรอบนี้เป็น review และ plan ยังไม่ได้รันผลใหม่หรือเปลี่ยนขอบเขตการทดลองจริง

## Review Focus

1. เทสผ่านโดยไม่ได้เรียกโค้ดจริง: แชมป์ ตรวจ early return, null fixture, ignored tests, assertions และ coverage; บีม ตรวจรับ
2. ตัวเลขจากคนละ checkpoint: บีม กำหนด manifest เดียว; ออม ตรวจให้ report/slides/README ใช้ชุดเดียวกัน
3. เลือกเฉพาะรอบสำเร็จจนเกิด bias: บีม เก็บทุก attempt และรายงานนโยบายคัดเลือกกับอัตราล้มเหลว
4. สลับ prompt/model ระหว่างชุดทดลอง: แชมป์ บันทึกจริง; บีม แยก original/clarification และ Haiku/Sonnet
5. ทำซ้ำไม่ได้หรือภาพไม่ครบ: ออม ไล่ source → capture → processing → suite → logs → result และทำรายการที่ยังขาด

## 1. Review สถานะเริ่มงาน

ฐาน local branch `test`: commit `fb3fe69c63db983f3c4a6e6f53bffa0de49d14e0` ตรวจเมื่อ 2026-10-02

แหล่งผลหลัก: `output/kku-only-20261001/summary.json` generated_at_utc `2026-10-02T15:18:58.704609+00:00`

- แผนเดิม 204 รอบ = 17 โปรเจกต์ × 4 วิธี × 3 รอบ; complete 185 เหลือ 19
- CMA-ES 51/51, FSCS-ART 51/51, Gemini 51/51, Claude 32/51
- ทั้ง 17 โปรเจกต์อยู่ในแผนและถูกพยายามทดสอบ แต่ Claude original prompt มีผลที่ใช้รันสำเร็จ 15/17 โปรเจกต์ ขาด Closure และ JxPath
- Claude ที่สำเร็จเดิมมี Haiku 27 รอบ และ Sonnet 5 รอบ จึงห้ามเรียกชุดนี้ว่า Haiku-only
- ชุดหนึ่งผลต่อโปรเจกต์/วิธีมี original prompt ที่เลือกได้ 66/68 ช่อง ไม่ใช่ผลหลักครบ 204 รอบ
- JxPath clarification มี record complete 28 test methods, line 108/773, branch 56/562, fault_detected=false หากผ่านการตรวจเนื้อหา สามารถเพิ่มเป็นผลเสริมช่องที่ 67 ได้ ต้องระบุ prompt เปลี่ยน
- Closure clarification compile ไม่ผ่าน และพบปัญหาคุณภาพเพิ่มเติม: `createMockCompiler()` คืน null; เทสมี `if (compiler == null) return;` ก่อน assertions; หลายส่วนมีเพียงคอมเมนต์ การตัด private API ที่ compile ไม่ผ่านอย่างเดียวจึงไม่ทำให้เป็นเทสที่มีความหมาย
- report/slides/เอกสารเริ่มงานหลายเวอร์ชันต้องตรวจวันที่และตัวเลขใหม่ ภาพที่มีอยู่ไม่ได้แปลว่าครบทุก record ต้องนับจากไฟล์จริง

ความครบทั้ง 17 โปรเจกต์ต้องแยก 3 ความหมาย: อยู่ในขอบเขต, มี attempt, มี suite ที่ใช้ได้ครบ 4 วิธี รายงานทั้งสามอย่าง ห้ามเรียกทั้งสามอย่างว่า complete เหมือนกัน

## 2. ขอบเขตที่แนะนำสำหรับส่งงาน

คงผลหลัก 204 attempts เป็นประวัติการทดลอง เปรียบเทียบแบบหนึ่ง suite ต่อโปรเจกต์/วิธีเป็นการวิเคราะห์เสริมเป้าหมาย 68 ช่อง เริ่มจาก 66 ช่อง original prompt และตรวจ JxPath/Closure เพิ่ม ไม่ยกเลิก 19 รอบที่ยังไม่สำเร็จด้วยการเปลี่ยน denominator

ให้บีม ระบุการเลือก original suite ตาม run index 101 → 102 → 103 โดยเลือก complete ตัวแรก พร้อมเก็บ failures ก่อนหน้าและเปิดเผยว่าเป็นการเลือกภายหลัง ห้ามเลือกจาก coverage สูงสุดหรือบัคที่ตรวจพบ หากรวม clarification ให้แสดงผลแยกและวิเคราะห์ความเทียบเคียง

การมี 68 ช่องยังไม่พิสูจน์ว่าได้คะแนนเต็ม: ต้องตรวจคุณภาพเทส ความยุติธรรมของ budgets/target APIs เวลา ผลล้มเหลว รายงาน และ demo ด้วย ยังไม่เพิ่มขอบเขต 850 bugs ในรอบเร่งส่งนี้

## 3. แชมป์ — Test & Evidence Owner

**เริ่มได้ทันที:** ตรวจ Closure/JxPath และจัดหลักฐานผลทดลองใหม่

**อ่านก่อน:**
- `results/study/kku-confirmed-20261002/claude/Closure/intellisphere-s101-b30/generation/tests/com/google/javascript/jscomp/RemoveUnusedVarsTest.java`
- record และ fixed-1/command.log ภายใต้ evaluation ของ Closure เดียวกัน
- `results/study/kku-confirmed-20261002/claude/JxPath/intellisphere-s101-b30/generation/tests/org/apache/commons/jxpath/ri/model/dom/DOMNodePointerTest.java`
- record/logs ของ JxPath และ `output/kku-only-20261001/confirmed-evidence-audit-20261002.json`
- `scripts/study/run_isolated.py`, `scripts/study/evaluate.py`, `scripts/study/run_provider_captures.py` ก่อนเลือกวิธีรัน ห้ามรัน batch ที่เขียนทับผลเดิม

**เจ้าของไฟล์ใหม่:** `docs/review/TEST_VALIDITY_20261002_TH.md`, `results/study/parallel-20261002/`, captures ใหม่ใน `ai-tests/provider-captures/parallel-20261002/`

- [ ] ตรวจทุก method ของ Closure และ JxPath ว่า fixture ใช้ได้ เรียก target จริง และมี oracle ที่ตรวจผลได้ เก็บรายการ method ที่ว่าง/ข้าม/รับ exception กว้างเกินไป
- [ ] บันทึกข้อสรุป Closure ว่า suite ปัจจุบันยังใช้ไม่ได้ พร้อมตำแหน่ง source และ log ไม่แก้ผลเดิมเป็น complete
- [ ] ตรวจ JxPath 28 methods กับ suite ที่รันจริง ยืนยัน fixed รันสองครั้งผลคงเดิม, buggy outcome, coverage และ hash ตรง artifact; บันทึกข้อจำกัดของ assertions ที่พบ
- [ ] หากต้องสร้าง Closure ใหม่ ให้ KKU สร้าง runnable tests จาก fixed source และ API/build context ที่อนุญาต ระบุว่าต้องมี compiler fixture จริงและตรวจผล observable ไม่ส่ง logs และไม่บิดเบือนวัตถุประสงค์เพื่อหลบ refusal
- [ ] เก็บ prompt/raw response/model/screenshot/time และ processing history ในโฟลเดอร์ใหม่ แม้ AI ปฏิเสธหรือ server busy
- [ ] ใช้ worktree ของ Defects4J แยกจากผู้ร่วมงาน ตรวจ fixed สองครั้ง แล้ว buggy และ coverage ด้วย suite เดียวกัน เก็บ source/suite hash และ logs
- [ ] ส่งผลให้บีม ตรวจรับ พร้อมสถานะ complete/failed/blocked และเหตุผล ถ้ายังสร้างไม่ได้ ส่งหลักฐาน failure เพื่อรายงานตามจริง

**ข้อมูลส่งต่อ:** สร้าง `results/study/parallel-20261002/handoff-runs.json` เป็น array ของ object ที่มี `project`, `bug_id`, `approach`, `run_id`, `status`, `model`, `prompt_condition`, `record_path`, `capture_path`, `suite_sha256`, `validity_review_path` ให้ path เป็น relative จาก repo และค่าไม่มีข้อมูลเป็น null

**เกณฑ์ตรวจรับ:** runnable มีการตรวจผลจริง, fixed ผ่านอย่างคงที่, buggy ถูกวัด, coverage target มีข้อมูล, provenance ตรง source; fault_detected=false ยอมรับได้ การมี @Test จำนวนมากหรือ compile ผ่านอย่างเดียวไม่เพียงพอ

## 4. บีม — Results & Fair Comparison Owner

**เริ่มได้ทันที:** ใช้ฐานเดิมวิเคราะห์และตรวจความครบ 17 โปรเจกต์ ระหว่างรอผลใหม่ของแชมป์

**เจ้าของไฟล์ใหม่:** `output/parallel-20261002/selected-runs.json`, `output/parallel-20261002/analysis.json`, `docs/review/COMPARISON_REVIEW_20261002_TH.md` หากต้องเพิ่มสคริปต์ใช้ `scripts/reporting/parallel_scope_20261002.py`

- [ ] อ่าน protocol, summary และ records เดิม ตรวจ expected/observed/complete/failed จาก record ไม่ใช้ตัวเลขใน PDF เก่าเป็นฐาน
- [ ] ทำ inventory 17 โปรเจกต์: Chart, Cli, Closure, Codec, Collections, Compress, Csv, Gson, JacksonCore, JacksonDatabind, JacksonXml, Jsoup, JxPath, Lang, Math, Mockito, Time
- [ ] สร้าง manifest ครบ 68 ช่องจาก 17×4 แม้ช่องไม่มี complete: ระบุ project, bug_id, approach, selected_record_path หรือ null, selection_reason, prompt_condition, model, suite_sha256, validity_status และ attempt_record_paths
- [ ] ตรวจ budgets, target classes/APIs, raw/retained test counts และวิธีตัด fixed-failing tests ของ 4 วิธี หากไม่เท่ากันระบุข้อจำกัด ไม่สรุปว่าแตกต่างเพราะตัว generator เพียงอย่างเดียว
- [ ] วิเคราะห์ line/branch coverage, generation/execution time และ fault detection พร้อม denominator ระบุ observed/eligible/missing; ห้ามแทน missing ด้วย 0 โดยไม่อธิบาย
- [ ] แยกตาราง/กราฟ original prompt, clarification และ model ที่ต่างกัน เปรียบเทียบแบบจับคู่เฉพาะโปรเจกต์ที่ทั้ง 4 วิธีมีผลใช้ได้ พร้อมภาพรวม failures ทุกโปรเจกต์
- [ ] ตรวจรับ source review และ handoff-runs.json ของแชมป์ ก่อนนำเข้า manifest ถ้าไม่ผ่านให้คง missing/invalid ไม่เพิ่มยอด complete
- [ ] ตรึงผลสุดท้ายใน `output/parallel-20261002/freeze.json`: `baseline_commit`, `selection_policy`, `manifest_sha256`, `analysis_sha256`, `frozen_at_utc`, `unresolved_items` ส่งให้ออม

**เกณฑ์ตรวจรับ:** 68 ช่องไม่มี project/method ซ้ำ, เลือก deterministic, source/hash ย้อนกลับได้, ยอด 204 และยอด selected แยกกัน, missing ไม่ปะปนกับ test failure หรือ fault not detected และไม่มีผล clarification แอบแทน original

## 5. ออม — Report, Demo & Submission Owner

**เริ่มได้ทันที:** ร่างรายงาน/สไลด์ตาม rubric และเตรียม demo ใช้ตัวเลข baseline ที่ติดป้ายชั่วคราว จนบีม freeze

**เจ้าของไฟล์:** `docs/parallel-20261002/`, `output/parallel-20261002/submission/`; แก้ `README.md` หลังรวมผลครั้งสุดท้ายโดยออม คนเดียว

- [ ] ทำ checklist ข้อ 2.2: อธิบาย 2 algorithms, 2 AI/prompts, Defects4J scope, วิธีรัน, coverage/performance, เปรียบเทียบ, ปัญหา/ข้อจำกัด/บทเรียน, source/tests/results/images/configs และ presentation/demo
- [ ] เขียน `docs/parallel-20261002/REPORT_20261002_TH.md` และ outline สไลด์; อธิบายความต่างของ bug, test method, suite และ run ห้ามเรียก 204 รอบว่า 204 bugs
- [ ] ตรวจสมาชิก/รหัสนักศึกษาใน README กับรายงาน ไม่สร้างข้อมูลบุคคลที่ยังไม่ได้รับ
- [ ] ทำ `docs/parallel-20261002/EVIDENCE_INDEX.md` ไล่ capture/ภาพ/model → raw source → processing → suite → execution record ทุก selected run; บันทึกภาพขาดตามจำนวนจริง
- [ ] หลังได้ freeze.json เติมผล/กราฟ/ข้อจำกัดจาก manifest เดียวกัน แล้วสร้าง PDF/PPTX หากใช้งาน ต้องอ่านและใช้ PDF/Presentations skills พร้อมตรวจการแสดงผล
- [ ] ทำ `docs/parallel-20261002/DEMO.md` เลือกตัวอย่างที่มี fixed/buggy/coverage ตรวจสอบได้ ระบุ prerequisites และคำสั่งจาก runner ที่มีอยู่ ทดสอบบน worktree แยก
- [ ] สร้าง `docs/parallel-20261002/START_HERE.md` เป็นจุดเริ่มต้นเดียว ระบุเวอร์ชันล่าสุดและไฟล์เก่าที่เป็น historical; ปรับ README ให้ชี้ไฟล์นี้
- [ ] รวม ZIP ใหม่ใน submission/ พร้อม manifest hashes ตรวจแตกไฟล์ได้ source/logs/ภาพอ่านได้ ไม่มีผล draft 850 ปะปนกับผลที่รันแล้ว
- [ ] ตรวจทั้งสามคนกับ rubric และแพ็กเกจ ให้ผู้ส่งงานอัปโหลด GitHub/Classroom และตรวจไฟล์หลังอัปโหลดตามช่องทางที่ได้รับมอบหมาย

**เกณฑ์ตรวจรับ:** report/slides/README/ZIP ใช้ freeze เดียวกัน, ภาพขาดระบุจริง, demo ทำซ้ำได้, แสดง failures/limitations และไม่มีข้ออ้างว่าครบทุกบัคของ Defects4J

## 6. การทำพร้อมกันและรวมงาน

- T0: ทั้งสามคนใช้ commit ฐานเดียวกันและสำเนางานแยก ตรวจ untracked files ก่อนทำงาน ไม่ใช้ git add . กับไฟล์เก่าที่ไม่ได้ตรวจ
- แยก branch จากฐานเดียวกัน พร้อมแผนฉบับนี้: แชมป์ `codex/champ`, บีม `codex/beam`, ออม `codex/aom` แต่ละคนทำงานและ commit เฉพาะ branch ของตนเอง ออมรวมงานเข้า `test` หลังตรวจรับ
- แต่ละคนเริ่มงานของตัวเองทันที บีม ใช้ baseline ก่อน ออม เขียน methods/limitations/demo ก่อน จึงไม่ต้องรอ Closure จึงจะเริ่มเอกสารได้
- ทุก 30 นาทีส่งสรุป 3 บรรทัดในช่องกลุ่ม: ทำอะไรเสร็จ / ติดอะไร / ส่งไฟล์ใดให้ใคร ไม่ส่งบัญชีส่วนตัวหรือ credentials
- แชมป์ → บีม: handoff-runs.json + validity review; บีม ตรวจและ freeze → ออม: selected manifest + analysis + freeze.json
- ออม เป็นผู้รวมงานคนเดียว ตรวจ commit ทีละชุดก่อน merge/cherry-pick ห้ามให้หลายคนเขียน summary หลักหรือแพ็ก ZIP เดียวกันพร้อมกัน
- เผื่อก่อน deadline อย่างน้อย 60 นาทีเพื่อ freeze ตรวจรายงานและ ZIP และอัปโหลด หาก Closure ยังไม่ผ่านให้ freeze ตามจริง ไม่แต่งยอดครบ ไม่มีเวลารับประกันการตอบของ KKU
- หลัง freeze ถ้าจะเพิ่มผล ต้องเพิ่มเวอร์ชัน freeze และอัปเดตเอกสาร/ZIP ทั้งชุด ไม่แก้เฉพาะตัวเลขหน้า README

## 7. ตรวจรับร่วมก่อนส่ง

- [ ] บีม ตรวจเทสและ provenance ของแชมป์ โดยเฉพาะ Closure/JxPath
- [ ] ออม ตรวจจำนวนจาก manifest เทียบรายงานและสไลด์
- [ ] แชมป์ ตรวจคำอธิบายวิธีทดลองและ demo ของออม
- [ ] ทั้งสามคนยืนยันว่า 17 โปรเจกต์มีรายการครบ 4 วิธี แต่แยกช่อง usable/failed/missing อย่างถูกต้อง
- [ ] ยืนยัน 185/204 เป็น checkpoint เดิมเท่านั้น หากมีผลใหม่ต้องคำนวณใหม่โดยอ้าง records ที่เพิ่มจริง
- [ ] ระบุขอบเขต 17 bugs ความต่าง prompt/models และการคัดเลือกผลภายหลังเป็นข้อจำกัด
- [ ] ตรวจข้อกำหนดและเนื้อหาไฟล์ส่งจริงก่อนสรุปว่าเสร็จ ไม่รับรองคะแนนเต็มจากจำนวนรอบเพียงอย่างเดียว

## ผลที่แผนนี้ตั้งใจส่งมอบ

1. ชุดเทส/หลักฐานใหม่ที่มีคุณภาพ หรือบันทึกล้มเหลวที่ตรวจสอบได้
2. ผลวิเคราะห์ 4 วิธีจาก manifest เดียว พร้อมความครบ 17 โปรเจกต์และข้อจำกัด
3. รายงาน สไลด์ demo README และ ZIP ที่ใช้ข้อมูลตรงกัน พร้อมรายการสิ่งที่ยังไม่สำเร็จ

ไม่ต้องรอให้ Claude สำเร็จทุก repetition จึงเริ่มรายงานได้ และไม่ควรเพิ่ม bugs จนฐานหลักและชุดส่งงานตรวจรับแล้ว
