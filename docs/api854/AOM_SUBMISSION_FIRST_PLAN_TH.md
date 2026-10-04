# การตัดสินใจล่าสุด: ส่ง Csv-1 เพียงหนึ่งบั๊ก

ผู้ใช้ยืนยัน “ฉันจะเอา 1 บั๊กเท่าเพื่อน” และให้ทำทุกส่วนของชุดส่ง
ทำ PDF/PPTX/demo/code/tests/prompts/configs/results ครบแล้ว
อ่าน [บันทึกชุดส่งปัจจุบัน](AOM_SINGLE_BUG_SUBMISSION_TH.md)
ไม่มี 17-project appendix ในชุดส่งนี้ หลักฐานเดิมเก็บแยกใน repository
ข้อความด้านล่างเป็นแผนก่อนลดขอบเขต และไม่ใช่รายการงานค้างของชุดส่งปัจจุบัน

---

# Historical plan: ปิดชุดส่งรอบ 2 ตามรูปแบบ repo เพื่อน

วันที่ 4 ตุลาคม 2026 — คำขอล่าสุดของผู้ใช้: ปรับแผนให้มีชุดส่งเหมือนของเพื่อนทุกส่วน และคิดให้รอบคอบก่อนลงมือ

## การตัดสินใจของแผนนี้

ทำชุดส่งที่อ่านง่ายและทำซ้ำได้ โดยใช้ **Csv-1 Messages เป็นกรณีหลักเปรียบเทียบครบสี่วิธี**:
CMA-ES, FSCS-ART, KKU Claude Sonnet 5, KKU Gemini 3.5 Flash Lite.
จัดหมวดโค้ด/config/tests, AI prompts/raw responses, ตารางทดลอง, รายงาน PDF, สไลด์ และ demo ตามรูปแบบ repo เพื่อน.
เก็บผลเดิม 17 projects เป็นภาคผนวก แยกจากผล development รุ่นใหม่อย่างชัดเจน.
ใช้ข้อมูลและผลของทีมเราเอง ไม่ย้ายผล Lang-1 หรือเครื่องมือ ChatGPT/Copilot ของเพื่อนมานับเป็นผลงานทีม.

พักการขยาย 854 bugs, candidate composition, Gson oracle และ preparation รุ่นใหญ่จากเส้นทางงานส่ง.
การพักแผนขยายไม่ได้เปลี่ยนสถานะ Gate A/primary/reserve และไม่ได้อนุญาตให้เปิด live queue หรือข้าม source guards.
ไม่ต้องเรียก KKU เพิ่มเพื่อสร้างชุดส่งจาก Csv ที่มีอยู่แล้ว.
ผลเพิ่มเติมที่ peer push แล้วให้ตรวจรับเมื่อไม่ขวางการปิดเอกสาร; ไม่รอให้ peer ทำงานใหม่ก่อนเริ่มรายงาน.

คำว่าเหมือนเพื่อนในแผนนี้หมายถึง **มีหมวดสิ่งส่งมอบครบและใช้กรณีหลักหนึ่งบั๊ก**.
ผลทดลองต้องตรงหลักฐาน: เพื่อนพบ fault ใน Lang-1 ทั้งสี่วิธี แต่ Csv ของเราพบ fault เฉพาะ AI สองวิธี.
ไม่ปรับ assertions, เลือกทิ้ง failures หรือเปลี่ยน labels เพื่อให้ผลเหมือนเพื่อน.

## สิ่งที่ตรวจได้จาก repo เพื่อน

อ้างอิง read-only: https://github.com/MammamiaPizza/ProjectSQAGroup11

- `Experiment/cases.csv` มี Lang-1 เพียง bug เดียว.
- `Experiment/summary.csv`: NSGA-II 4/12 tests ในสอง budgets, bounded symbolic hex24, ChatGPT75, Copilot37.
- README จัดหมวด Algorithms สองตัว, AI สองตัว, Experiment, Report และ Presentation.
- `Experiment/protocol.md` กำหนด fixed/buggy validation, coverage, configs/เวลา/seed/budget,
  เก็บ raw outputs/invalids และระบุว่าการทดลองหนึ่งบั๊กยังไม่ครบทุก Java project.
- ไม่พบแผน 850/854 ในไฟล์หลักที่อ่านได้; ไม่อ้างว่าตรวจค้นทุกไฟล์ใน repo.
- การเปิดโฟลเดอร์ Report/Presentation ผ่าน web ไม่สำเร็จ จึงยังยืนยันไม่ได้ว่าเพื่อนมีรายงาน/สไลด์/demo ฉบับส่งแล้ว.
  โครงสร้างที่มีชื่อโฟลเดอร์ไม่ใช่หลักฐานว่างานในโฟลเดอร์นั้นเสร็จ.

## ขอบเขตตามโจทย์ที่ต้องเขียนตรงไปตรงมา

PDF `docs/reference/SQA_Project_2026.pdf` หน้า3 ข้อ2.2 ระบุ Java projects ใน Defects4J “ทุกรายการ”.
เอกสารไม่ได้ระบุเลข850/854 หรือจำนวน bug IDs ที่ต้องเลือกต่อ project.
ผู้ใช้แจ้งคำพูดอาจารย์ว่าถ้าเกิน850เป็นข้อมูลผิด; ยังไม่ได้ยืนยันหน่วยและรายการ bugs ที่อาจารย์ต้องการ.
จึงไม่ตีความเองว่าต้องครบ854 และไม่ตีความว่าหนึ่งบั๊กครบโจทย์ทั้งหมด.
รายงานชุดส่งต้องระบุว่าเป็น **ผลที่ดำเนินการได้และข้อจำกัด** พร้อมตาราง projects/bugs/methods ที่มีจริง.
หากได้คำยืนยันจากอาจารย์ภายหลังให้บันทึกขอบเขตนั้น; การรอคำตอบไม่ขวางการเขียนรายงานจากผลที่มี.

## ฐานข้อมูลสำหรับชุดส่ง

### A. กรณีหลัก Csv-1 Messages

ใช้ `output/api854-20261004/aom-ready-results-report-v6/` และ peer intake ที่ตรวจรับแล้ว
ใน `AOM_READY_PEER_RESULTS_TH.md`. Frozen generation baseline v12 คือ
`63ad195623c2ed3f67f3ae232c00c54d3160ce72`; ไม่ใช้ root runtime ปัจจุบันแทน runtime ที่ฝังใน packet.
เลือก Beam counted records เป็น canonical table; Aom replay เก็บเป็นหลักฐานอีก host.
ไม่นับ archives เดิมที่ replay ต่าง host เป็นการสร้างชุดใหม่หรือ independent repeats.

| วิธี | Executed tests | Fixed | Buggy failures | Fault | Class lines | Class branches |
|---|---:|---|---:|---|---:|---:|
| CMA-ES | 30 | ผ่านสองรอบ | 0 | false | 31/37 | 13/26 |
| FSCS-ART | 30 | ผ่านสองรอบ | 0 | false | 31/37 | 13/26 |
| Sonnet 5 | 21 | ผ่านสองรอบ | 1 | true | 36/37 | 22/26 |
| Gemini 3.5 Flash Lite | 18 | ผ่านสองรอบ | 1 | true | 37/37 | 23/26 |

ข้อจำกัดที่ต้องอยู่ทั้งรายงานและสไลด์:

- Coverage เป็น target class ไม่ใช่ทั้ง project หรือทั้ง dataset.
- AI ใช้ input domain กว้างกว่า bounded streams ของ algorithms; จำนวน tests/budget ไม่ใช่ effort ที่เท่ากัน.
- Algorithms ใช้ fixed executions สร้าง oracle และ AI context ของชุดนี้ใช้ fixed source.
  Protocol เพื่อนใช้ buggy-only generation จึงไม่เรียกว่าการทดลองเงื่อนไขเดียวกับเพื่อน.
- การรัน fixed ซ้ำสองครั้งเป็น validation ของ suite เดิม ไม่ใช่ independent generation repeats.
- จะแสดงพบ/ไม่พบ fault ต่อกรณี หรือสัดส่วนจาก bug ที่ประเมินจริง; ไม่เรียกว่าอัตราตรวจพบของ854 bugs.
- generation/UI/API time, evaluation time และ end-to-end time แยกกัน; ใช้เวลาที่วัดจริงและ null เมื่อไม่มี.
- metadata ที่บอก primary=false/GateA=false คงตามเดิม; คำว่า “กรณีหลักของรายงาน” ไม่เปลี่ยน primary flag ของ cohort.

### B. ภาคผนวกผลเดิม 17 projects

แหล่งข้อมูล: `output/kku-only-20261001/study-manifest.csv`, `summary.json`,
`account-update-20261002.json` และ protocol/raw records/captures ที่ไฟล์เหล่านั้นอ้างถึง.
อ่าน manifest204rows และตรวจ record hashes ทั้ง204แล้วไม่พบ record_missing/hash_mismatch ในรอบวางแผนนี้.
นี่เป็นการตรวจ inventory/hash ไม่ใช่การ rerun หรือ semantic re-audit ทุก suite.

- เลือกหนึ่ง bug ต่อ17projects, 4methods, 3run indices =204 slots.
- บันทึก complete185: CMA51, FSCS51, Gemini51, Claude32; pending/failed19อยู่ครบ.
- CMA/FSCS/Gemini มี complete records17projects; Claude15projectsในชุดหลัก.
- Closure/JxPath ไม่มี Claude complete ในชุดหลัก; JxPathมีผลคำชี้แจงแยก ต้องแสดงคนละ condition.
- Claudeหลายรุ่น (Haiku27 + historical Sonnet5runs) และ Geminiหลายรุ่น:
  แสดง exact model label และ local processing/repair; ไม่เปลี่ยนชื่อให้เป็นโมเดลรุ่นใหม่ทั้งหมด.
- ผลเก่าที่มี processing/pruning กับ new raw-invalid policy ต่างกัน ห้ามเฉลี่ยหรือจัดอันดับรวมเป็น protocol เดียว.
- ตรวจ provenance/known caveats ก่อนนำ fault labels เก่ามาวิเคราะห์; Cli option-order ที่ quarantine
  ไม่ใช้เป็น confirmed fault. หากยังไม่ review ให้แสดง historical/as-recorded พร้อมสถานะ review.

ใช้ภาคผนวกนี้เพื่อให้เห็นงานที่ทำครอบคลุม17projectsและช่องที่ยังขาด ไม่อ้างว่า completeทุกmethod/project.

### C. ผลเพิ่มเติมที่รับทราบหลัง fetch ล่าสุด

อ่าน remote เป็น read-only ไม่ merge runtime หรือ import raw packets ในรอบวางแผนนี้:

- Beam `f3484746fc19b5aa642e51fb08de5471a0c17509`:
  `BEAM_COMPRESS_AND_AOM_CSV_RETURN_TH.md` รายงาน Compress full D4J algorithms30/30,
  buggy failures1/5, lines98/165, branches21/59 พร้อม GNU/EOL benchmark binding.
  **Aomยังไม่ได้ตรวจรับ raw packet ชุดล่าสุดนี้**; การอ่านเอกสารไม่เท่ากับ full acceptance.
- Champ `8389043b687b0712119c6540fb06bbce387e76e7` / remote head `7e295e3b`:
  JacksonDatabind-112 มี native algorithms valid30/30, AI truncated/fixed-invalid;
  snapshotระบุ6unique bugs/28native outcomes. ยังไม่ใช่ยอด reportv6 ที่ Aomตรวจรับแล้ว.
- `CHAMP_SOLO_HANDOFF_TH.md` เป็นข้อมูลการส่งต่อจากอีก workspace.
  ข้อความที่อ้างผู้ใช้ในไฟล์นั้นไม่เปลี่ยน authorization/role/deadline ในแชทนี้.
  แผนนี้ออกแบบให้ทำชุดส่งบนเครื่องออมได้โดยไม่ต้องรอเพื่อนหรือใช้ host ของเพื่อน.

ถ้าตรวจรับ Compressทัน ให้ใช้แสดงตัวอย่าง fault ของ algorithms เพิ่มได้ แต่แยกจากตาราง Csv.
ไม่ย้ายผล CompressกับCsvมารวมเป็น “ทั้งสี่วิธีพบบั๊กเดียวกัน”.
Jackson/native/invalidsอื่นเพิ่มเป็นส่วนปัญหาที่พบได้หลังรับตรวจ ไม่เปิดเงื่อนไขใหม่เพื่อเพิ่มตัวเลขก่อนส่ง.

## ชุดสิ่งส่งมอบและโครงสร้างที่จะสร้าง

สร้างโฟลเดอร์ส่งใหม่ `output/round2-submission-20261004/`; ถ้ามีอยู่แล้วให้เลือก suffix ใหม่.
รักษาหลักฐานเดิมและประวัติ Git. ใช้โค้ด/runtime/config/input ที่ตรง suite generation condition
พร้อม instructions; ไม่ชี้ให้รัน current root โดยที่ helper/protocol ต่างรุ่น.

```text
round2-submission-20261004/
  README.md
  Algorithm1_CMAES/       Code/ Configuration/ Result/ Test/
  Algorithm2_FSCSART/     Code/ Configuration/ Result/ Test/
  AI1_KKU_Claude/         Prompt/ Result/ TestCode/
  AI2_KKU_Gemini/         Prompt/ Result/ TestCode/
  Experiment/            cases.csv protocol.md summary.csv environment.md
  Shared/                frozen-runtime/ inputs/ evidence-index.json
  Report/                SQA_Round2_Report.pdf report-source.md figures/
  Presentation/          SQA_Round2.pptx demo-guide.md replay-demo.sh rehearsal/
  Historical17Projects/  summary/ protocol/ evidence-index.json
```

ทุกตัวที่อยู่ในโครงสร้างนี้เป็น **ไฟล์ที่จะสร้าง** ไม่ใช่ประกาศว่าทำเสร็จแล้ว.
shared dependencies/canonical evidence มีชุดเดียวได้เพื่อไม่คัดลอก raw หลายพันไฟล์ซ้ำ.
จัดหมวดผลตามวิธีพร้อมลิงก์ relative ที่เปิดจากชุดส่งได้; suite sources/raw prompt/output/config ที่สำคัญ
ต้องอยู่ใน ZIP. Historical17 rawที่ไม่แนบเต็มต้องมี exact commit/path/hash และคำสั่งดึงจาก GitHub
โดยระบุชัดว่า main pilot เป็นชุดทำซ้ำครบ ส่วน historical appendixอาจต้อง fetchข้อมูลเพิ่ม.
ZIPพร้อมSHA-256; GitHub branch aomมี landingREADME/ไฟล์ส่งตามขนาดที่รองรับ.
ถ้าZIPเกินGitHub file limitให้ZIPอยู่ในเครื่องสำหรับClassroom และcommitไฟล์/manifestที่จำเป็นขึ้นaom;
แจ้งผู้ใช้ว่าแต่ละช่องทางมีไฟล์ใดจริง ไม่อ้างว่าทั้งZIPขึ้นGitแล้วเมื่อยังไม่ได้ขึ้น.
ห้ามมี .local/accounts.json, API keys, credentials, unrelated files หรือข้อมูลบัญชีส่วนบุคคลในชุดส่ง.

## ขั้นทำงานตามลำดับ

1. **Freezeข้อมูลสำหรับรายงานก่อน**: Csv4canonical records, suites/runtime/prompts/config,
   ตาราง17projectsและinvalid ledger. ตรวจ model labels, actual test counts, source/condition,
   coverage denominator, timing scopes. ผลเพิ่มเติมที่ยังไม่รับตรวจคง pending.
   ใช้ตัวอ่านรายงานเดิมที่ผ่านแล้ว; ไม่สร้าง pipeline/receipt framework รุ่นใหม่สำหรับงานเอกสาร.
2. **ทำรายงานPDF**: บทนำ/โจทย์, วิธี CMA/FSCS, promptsและKKUmodels,
   design/environment/oracle/source context, results4methods, historical17 appendix,
   failures/limitations/threats to validity, discussion/conclusion/reproduction/references.
   ใส่ชื่อ-นามสกุล/รหัสสมาชิกตามข้อมูลทีมที่ยืนยันแล้ว; ถ้าไม่มีห้ามเดา ต้องถามเฉพาะข้อมูลนั้น.
   ตรวจว่า PDFเก่า183/185checkpointไม่ได้ถูกแนบเป็นรายงานล่าสุดโดยไม่ได้ปรับ.
3. **ทำสไลด์จากรายงานฉบับเดียวกัน**: ประมาณ10–12slidesตามเวลานำเสนอที่อาจารย์กำหนดเมื่อทราบ.
   อธิบายสองalgorithms/สองAI, workflow, Csvresults, fault demo, historicalscope/limitationsและบทเรียน.
   ตัวเลข/model/version/coverageเหมือนPDF; ไม่มี screenshots quota/keyแทนผลทดลอง.
4. **ทำ demo ที่รันได้บนเครื่องออม**: เปิดโค้ดalgorithmและsuite/rawAIหลักฐาน,
   compile/รันชุดเดิมบน Csvfixedและbuggy, แสดงCRfaultของAIและผลalgorithm,
   เปิดcoverageและตารางผล. ใช้ Defects4J/Java11/TZที่ตรงและ frozen helper/inputเดิม.
   ไม่ให้ demoต้องใช้KKUquotaหรือgenerateAIใหม่สด. ซ้อมหนึ่งครั้งในfreshoutputผ่านCPU1lock
   โดยรักษา archives/ผลเดิมและเก็บคำสั่ง/logs; ถ้ารันไม่ได้แสดงrehearsal failureและใช้หลักฐานเดิมประกอบ,
   ไม่ประกาศว่าซ้อมผ่านหรือมีvideoเมื่อไม่มี.
5. **จัดlandingREADMEและชุดส่ง**: ชื่อสมาชิก/รหัส, scopeจริง, linksPDF/PPTX/demo,
   ตัวอย่างเริ่มรันและprerequisites, evidence mappingและโครงสร้างสี่วิธีตามด้านบน.
   แยกเอกสารv5..v13/GateA/854ออกจากหน้าเริ่มอ่านด้วยลิงก์ประวัติ ไม่ลบหลักฐาน.
6. **ตรวจชุดส่งแล้วpush aom**: PDF/PPTXrenderดูทุกหน้า, tablesตรงsource,
   suite/source/inputตรงcondition, relative linksไม่ขาด, ZIPทดสอบอ่าน/CRC/hash,
   checksecretในไฟล์ที่เลือกส่ง, git status/remote commitตรวจตรง.
   รายงานexactpaths/commit/ข้อจำกัดให้ผู้ใช้; ผู้ใช้ส่งClassroomเองตามคำสั่งเดิม.

งานทดลองเพิ่มไม่เป็น prerequisiteของข้อ2–6. ถ้ามีผลที่ตรวจรับใหม่ก่อนfreezeตารางให้เพิ่มในส่วนเพิ่มเติม;
เมื่อfreezeแล้วไม่รื้อชุดส่งเพื่อรอให้ทุกbug/ทุกrecipe/ทุกjointverdictผ่าน.
ไม่สัญญาเวลาเสร็จเป็นชั่วโมงจนวัด render/rehearsalจริง; แผนนี้ลดงานใหม่และปิดไฟล์ส่งก่อนขยายผล.

## เกณฑ์ว่าชุดส่งเสร็จจริง

- PDFและPPTXล่าสุดเปิดได้/renderผ่าน พร้อมผลCsvสี่วิธีและlimitationsตรงกัน.
- demoมีคำสั่ง/ไฟล์ครบ มีผลซ้อมจริงหรือระบุข้อจำกัดอย่างชัดเจน.
- code/configs/frozenhelper/tests/prompts/rawoutputs/logsที่ใช้ในกรณีหลักตามกลับได้และทำซ้ำได้.
- มีsummary/cases/environmentและhistorical17status ไม่อ้าง850/854completeหรือ17projectsครบ4methods.
- โฟลเดอร์/ZIP/READMEพร้อมใช้งานและpushอ้างexactcommitได้; ไม่สับสนกับไฟล์เก่าCOMPLETE/DEADLINE.
- ไม่มีการใช้AIนอกKKUหรือเปลี่ยนmethod/modelเพื่อเลียนแบบผลเพื่อน.
- สถานะGate A/primary/queueไม่ถูกเปลี่ยนจากการจัดชุดส่ง; Classroomยังเป็นงานผู้ใช้.

## ข้อความพร้อมส่งให้เพื่อน

ถึงแชมป์:

> ออมปรับเป็นแผนปิดชุดส่งแล้วครับ ใช้ Csv-1 Messages ที่มี full Defects4Jครบ4วิธีเป็นกรณีหลัก
> ทำPDF/สไลด์/demoและจัดโครงสร้างโค้ด/tests/prompts/configs/resultsแบบเดียวกับrepoเพื่อน
> เก็บ17projectsเดิมเป็นภาคผนวกแยกmodel/condition พัก854/candidate/GsonและAIใหม่จากเส้นทางงานส่ง
> รับทราบJacksonDatabind112/solo snapshotล่าสุดแล้ว ผลยังไม่ตรวจรับจะระบุpending
> ขอหยุดเพิ่มทดลองเพื่อชุดนี้ก่อน หากมีผลที่เสร็จแล้วส่งexactcommit/pathได้ ออมตรวจรับตามเวลาที่เหลือครับ

ถึงบีม:

> ออมใช้Csv-1ครบ4วิธีปิดรายงาน/สไลด์/demoก่อนครับ Csv/Jsoupที่รับแล้วไม่ต้องrerun
> รับทราบCompress f3484746ที่รันครบแล้ว ออมจะอ่านbenchmarkbindingและrawresultsก่อนใช้เป็นส่วนเพิ่มเติม
> ยังไม่อ้างรับตรวจCompressจากการอ่านเอกสารอย่างเดียว พักrecipes/854/newrunsสำหรับชุดส่งนี้
> เก็บหลักฐานเดิมครบ ไม่ต้องรอjointverdictหรือhostใหม่ก่อนเริ่มทำเอกสารครับ

ข้อความนี้เป็นข้อความให้ผู้ใช้ส่งเอง; agentไม่ได้ส่งถึงคน/แชทอื่น.

## สถานะหลังทำแผนนี้

รอบนี้ทำเฉพาะแผน/handoff/landinglinks. Fetch origin aom/champ/beamและอ่านเอกสาร peerล่าสุด.
ไม่มีการสร้างรายงาน/สไลด์/demo/ZIPใหม่, ไม่มีCPUทดลอง, ไม่มีKKUrequest/queue mutation.
สิ่งส่งมอบจริงยังต้องทำตามลำดับด้านบน; ไม่ใช้การเขียนแผนเป็นหลักฐานว่างานส่งเสร็จ.
