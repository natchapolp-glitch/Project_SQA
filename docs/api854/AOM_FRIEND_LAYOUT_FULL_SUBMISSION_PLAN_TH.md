# แผนชุดส่งครบทุกส่วนตามโครง repo เพื่อน

วันที่ 4 ตุลาคม 2026 — เป็นแผนและรายการงาน ยังไม่ใช่ผลทดลอง 854 บั๊กที่รันแล้ว

## เป้าหมาย

ใช้ Defects4J 3.0.1 จำนวน **854 active bugs / 17 projects** ทดลองด้วย
CMA-ES, FSCS-ART, KKU Claude Sonnet 5 และ KKU Gemini 3.5 Flash Lite
เริ่มหนึ่ง generated suite ต่อบั๊กต่อวิธี: 3,416 planned jobs
เก็บทุกสถานะ ไม่บังคับให้ทุกบั๊กสร้าง tests สำเร็จ และไม่เติมผลที่ยังไม่ได้ทดลอง

อ้างอิงโครงล่าสุดของเพื่อนที่ branch `jiratchaya_673380510-1`:
[Report](https://github.com/MammamiaPizza/ProjectSQAGroup11/tree/jiratchaya_673380510-1/Report),
[AI protocol](https://github.com/MammamiaPizza/ProjectSQAGroup11/blob/jiratchaya_673380510-1/Experiment/protocol/ai_final_protocol.md)
และ [AI summary](https://github.com/MammamiaPizza/ProjectSQAGroup11/blob/jiratchaya_673380510-1/Report/summaries/ai_summary.md)
อ่านเป็น reference เท่านั้น ไม่แก้ repo เพื่อนและไม่ใช้ผลของเพื่อนเป็นผลของเรา

เพื่อนมีสรุป AI 854 attempted cases ต่อวิธี แต่ summaries ของสอง algorithms ยังเป็น template
และ Presentation ใน branch ที่ตรวจมีเพียง `.gitkeep`
ดังนั้นเป้าหมายของเราคือทำหมวดส่งงานครบจริง ไม่ถือว่า placeholder ของเพื่อนเป็นงานที่เสร็จ

## โครงไฟล์ของชุดส่งใหม่

รักษาการจัดหมวดตามเพื่อน เปลี่ยนชื่อโฟลเดอร์วิธีให้ตรงกับเครื่องมือที่ทีมเราเลือก
ไม่ใช้ชื่อ NSGA-II/SymbolicExecution/ChatGPT/Copilot ครอบเนื้อหา CMA-ES/FSCS-ART/KKU
เครื่องมือทั้งสี่นี้ไม่เปลี่ยนตามเพื่อน
ชุดส่งหลัก/ZIP ใช้เจ็ดโฟลเดอร์ข้างล่างกับ README; เอกสารรุ่นเก่าและไฟล์ภายในเครื่องไม่ปนในชุดนี้
เก็บหลักฐานเดิมใน Git และระบุตำแหน่งผ่าน `Experiment/archive` เพื่อไม่ทำลายประวัติ

```text
Project_SQA/                          รากของชุดส่งใหม่
├── Algorithm1_CMAES/
│   ├── Code/                         โค้ด generator และ Java adapter
│   ├── Configuration/                seed, budget, bounds, objective, environment
│   ├── Result_Round1/<case>/run-final/ ผล generation/validation/evaluation จริง
│   ├── Result_Round2/                 รอบสร้างซ้ำเฉพาะที่ทำจริง; ยังไม่ทำให้ระบุชัด
│   └── Test/<case>/run-final/         Java tests และ helper ที่ใช้จริง
├── Algorithm2_FSCSART/
│   ├── Code/
│   ├── Configuration/
│   ├── Result_Round1/<case>/run-final/
│   ├── Result_Round2/
│   └── Test/<case>/run-final/
├── AI1_KKU_Claude/
│   ├── Prompt/
│   │   ├── 01_analyze_context.txt
│   │   ├── 02_generate_suite.txt
│   │   ├── 03_repair_suite.txt
│   │   ├── 04_extend_coverage.txt
│   │   └── <case>/run-final/          prompt ที่เติม context และส่งจริง
│   ├── Result/<case>/run-final/       raw answers, request settings, usage, status
│   ├── TestCode/<case>/run-final/     generated/repaired/final Java แยกกัน
│   └── docs/setup/                   วิธีใช้ KKU และ prerequisites
├── AI2_KKU_Gemini/
│   ├── Prompt/                       master templates เหมือน Claude ทุก byte
│   ├── Result/<case>/run-final/
│   ├── TestCode/<case>/run-final/
│   └── docs/setup/
├── Experiment/
│   ├── README.md
│   ├── cases.csv                     854 project/bug pairs ที่ไม่ซ้ำ
│   ├── context-template.md
│   ├── protocol/
│   │   ├── final_protocol.md
│   │   ├── ai_final_protocol.md
│   │   └── algorithm_final_protocol.md
│   ├── contexts/<case>/              metadata, signatures, buggy context, hashes
│   ├── automation/                   inventory/context/batch/evaluation/report CLIs
│   ├── evaluations/<case>/<method>/run-final/
│   ├── ai-results/                   ตาราง outcome ของ AI พร้อมที่มาผล
│   ├── diagnostics/                  infrastructure failures และ recovery
│   └── archive/                      บันทึกตำแหน่งหลักฐานรุ่นเก่า แยกจาก cohort ใหม่
├── Report/
│   ├── README.md
│   ├── SQA_Round2_Report.pdf
│   ├── report-source.md
│   ├── benchmark_status.md
│   ├── figures/                      กราฟ/diagram จากข้อมูลจริง
│   ├── data/
│   │   ├── ai/{case_results.csv,summary.json}
│   │   ├── cmaes/{case_results.csv,summary.json}
│   │   ├── fscs_art/{case_results.csv,summary.json}
│   │   └── final_comparison.csv
│   └── summaries/
│       ├── ai_summary.md
│       ├── cmaes_summary.md
│       ├── fscs_art_summary.md
│       └── final_comparison.md
├── Presentation/
│   ├── README.md
│   ├── SQA_Round2.pptx
│   ├── demo-guide.md
│   ├── demo.sh
│   └── demo-results/                 หลักฐานซ้อมของ suites ที่นำเสนอ
└── README.md                         สมาชิก/รหัส/scope/วิธีรัน/ลิงก์ทุกสิ่งส่งมอบ
```

`<case>` เช่น `Csv-1` ส่วน `run-final` เป็นชื่อ namespace ที่เสนอและต้องผูก protocol/config ก่อนเริ่ม
ไฟล์ใน `docs/api854/plans/friend-layout-full-v1/` เป็น blueprint ไม่ใช่ final results
Git history และชุด Csv-1 ที่ส่งแล้วเก็บครบ ผลเดิมไม่ถูกย้ายมานับเป็นผลใหม่โดยอัตโนมัติ

## ลำดับงานที่ทำจริง

1. **ยืนยันรายการบั๊กและโครงส่งงาน** — อ่าน installed active-bug index เทียบ Defects4J ที่ติดตั้ง
   สร้าง cases 854 รายการและ jobs 3,416 รายการ สถานะเริ่มต้น `PENDING`
   map ชื่อวิธี/ผลเก่าที่มีอยู่ และให้ README แสดงว่าอะไรเป็นผลเก่า อะไรเป็นงานใหม่
2. **ทำ runner ที่ทำงานเป็น batch และ resume ได้** — ใช้โค้ด CMA-ES/FSCS-ART และ evaluator เดิม
   ปรับ orchestration/context สำหรับทุก case; ไม่ clone/เขียน core algorithms ใหม่ถ้าไม่จำเป็น
   ใช้ CPU lock เดิมบนเครื่องออมหนึ่ง slot ห้ามชน jobs ของคนอื่น
   แต่ละ case มี output ใหม่ ไม่เขียนทับ sealed evidence; จบ/ล้มเหลวแล้วเดินต่อ case ถัดไป
   checkpoint หลังจบแต่ละขั้นตอน; resume เฉพาะขั้นที่ยังค้าง ไม่ retry ผลโมเดลเพื่อเอาผลดีขึ้น
3. **กำหนด condition ใหม่ที่สั้นและใช้ซ้ำได้** — AI context จาก buggy source/metadata/signatures เท่านั้น
   เลือก context แบบ deterministic มี budget แทนการแนบ helper/source ยาวทั้งหมด
   Algorithms ใช้ fixed observations สร้าง oracle ตามวิธีเดิม จึงต้องเปิดเผยความต่างกับ AI
   เลือก target classes/inputs ก่อนดูผล buggy และตรึง seed/budget/settings/code/context hashes
   ถ้า adapter ไม่รองรับหรือ build ไม่สำเร็จ ให้เก็บเหตุผลจริง ไม่รอพัฒนา fixture ทุก declaration ก่อนทดลองต่อ
4. **ทำ AI workflow ผ่าน KKU** — ใช้ Sonnet 5 + Gemini 3.5 Flash Lite เท่านั้น
   P01 วิเคราะห์สั้น → P02 สร้าง suite → P03 ซ่อมตามเงื่อนไขได้หนึ่งคำขอ → วัด coverage → P04 เพิ่มตามเงื่อนไข
   master templates/context selection/repair rules ต้องเหมือนกันสำหรับ AI ทั้งสอง
   Proposed starting limits: context source ไม่เกิน 12,000 characters; P02 ไม่เกิน 12 methods;
   P04 เพิ่มไม่เกิน 4 methods; temperature 0; request max_tokens 4096 ตาม setting ที่เคยรับ preflight
   ค่าเหล่านี้ยังต้องทดสอบกับ models/limits ปัจจุบันก่อน freeze ห้ามเดาว่า effective setting ตรง request
   เก็บ prompt/raw output/Java ทุกขั้น/model IDs/usage/time/status ห้ามส่ง fixed source หรือเฉลย patch ให้ AI
   P03 ใช้ excerpt การ compile/fixed-validation แบบจำกัด ไม่ใช้ buggy failure feedback เพื่อไล่จับบั๊ก
   P04 ที่ใช้ไม่ได้ให้เก็บ rejection และใช้ base suite ที่ผ่าน fixed อยู่แล้ว โดยใช้กติกาเดียวกันทั้งสอง models
5. **รัน pilot ก่อน** — Csv-1, Lang-1 และ Math-1 ภายใต้ condition ใหม่ ผ่าน pipeline ทั้งสี่วิธี
   เป้าหมายคือพิสูจน์ runner/status/counts/การเก็บหลักฐาน/เวลาจริง/การใช้ tokens ไม่ใช่ให้ทุกวิธีพบ fault
   ปิด runtime/credentials/current quota/model IDs และการคำนวณจำนวนคำขอที่ใช้จริงก่อนขยาย
   ถ้าระบบถูกต้องแต่โมเดลสร้าง invalid tests ให้บันทึกเป็นผลและไปต่อ ไม่เพิ่มรอบซ่อมตามอำเภอใจ
6. **ขยาย batch ทั่ว 17 projects** — เริ่ม batch 10–20 bugs เพื่อวัดเวลา/throughput/quota อีกครั้ง
   แล้วเดินครบรายการด้วยเงื่อนไขเดียวกัน มีคำสั่ง resume และรายงาน jobs ที่ยังไม่ได้ทำ
   Quota หมดให้ pause jobs ที่ยังค้าง ไม่แปลงเป็น model failure หรือผล completed
   infrastructure recovery แยกจาก outcome ของวิธี และมีจำนวน retries/timeouts ที่ประกาศไว้ก่อน
7. **สรุปข้อมูลควบคู่กับการรัน** — อ่าน records มาสร้าง Report/data และ summaries อัตโนมัติ
   แสดง planned/attempted/valid/invalid/infra/quota/pending แยกกัน
   Fault detection หารด้วยจำนวน cases ที่ valid และมีผล buggy จริง
   Coverage เฉลี่ยเฉพาะ cases ที่วัดได้ แยก line/branch/condition ตามชนิด metric ที่บันทึกจริง
   แสดง tests, generation/evaluation time และ tokens พร้อมจำนวน cases ที่มีข้อมูล ไม่แทนค่าหายด้วย 0
8. **ปิดสิ่งส่งมอบทุกส่วน** — รายงาน PDF ฉบับใหม่, กราฟ/diagram, PowerPoint, demo ที่ซ้อมจริง,
   README สมาชิก/รหัส/คำสั่ง/prerequisites, code/config/prompts/test sources/ผลล้มเหลว และ ZIP/checksums
   ถ้าหยุดก่อนครบ ให้ทุกเอกสารรายงานตัวเลขและ remaining jobs ตรงกัน ไม่มีคำว่า complete ทั้งชุด
   ผู้ใช้ส่ง Classroom และนำเสนอเอง; push งานของเราเฉพาะ branch `aom`

## แยกตัวเลขสำคัญ

- **854** = planned bugs ไม่ใช่ tests และไม่ใช่จำนวนที่ผ่าน
- **3,416** = planned bug/method pairs สำหรับหนึ่ง generation ต่อวิธี
- AI มี **1,708 bug/model pairs**; P01/P02 ต้องมากกว่าหนึ่งคำขอต่อคู่
  หากทุกคู่พร้อมส่ง Base P01+P02 รวม 3,416 requests; เมื่อใช้ P03/P04 ครบจะสูงสุด 6,832 requests
  เป็นขอบเขตการวางแผน ไม่ใช่จำนวนคำขอที่ส่งแล้ว และ quota/infra อาจทำให้ส่งไม่ครบ
- Fixed validation สองครั้งของ suite เดิมไม่ใช่สอง independent generation repeats
- `Result_Round2` มีโครงไว้ได้ แต่ต้องระบุยังไม่ทำจนมี generation ใหม่จริง ห้าม copy Round1 ไปเป็น Round2
- จำนวน commits ไม่ใช่ตัววัดจำนวนการทดลอง เก็บงานตาม batch ที่ตรวจแล้ว ไม่สร้าง version ใหม่ทุก case

## สถานะและเกณฑ์เสร็จ

Outcome ต้องแยก `DONE`, `INVALID_AFTER_REPAIR`, `OUTPUT_INCOMPLETE`, `UNSUPPORTED`,
`INFRA_ERROR`/`TIMEOUT` ออกจาก `PENDING`/`QUOTA_PAUSED`
ให้เหตุผลและ stage จริงเสมอ; unsupported ไม่แปลว่าส่ง AI request แล้ว
`attempted` นับเมื่อมี attempt ที่บันทึกจริง; `provider_requested` และ `evaluated` เป็นคนละตัวเลข
ไม่มี fault/coverage/test-count ให้ใส่ null ไม่แต่งผล ไม่คัดทิ้ง invalid เพื่อตกแต่ง evaluation rate

Full run เสร็จเมื่อทุก planned job มี terminal outcome ที่มีหลักฐานตามขั้นตอน
ไม่ต้องให้ valid ครบ 3,416 แต่ต้องแสดงข้อจำกัดของ unsupported/infra failures อย่างชัดเจน
ถ้า job หยุดเพราะ quota หรือยังไม่เริ่ม ยังไม่ใช่ full run ที่เสร็จ
ชุดส่งต้องมีทุกหมวดข้างต้น, ตาราง/รายงาน/สไลด์ตัวเลขตรงกัน, demo ผ่านการซ้อม,
ZIP อ่านได้/CRC/hash ผ่าน และไม่มี credentials ใน Git/ZIP

## ใช้อะไรจากงานเดิม

ใช้ core CMA-ES/FSCS-ART, D4J installation/Java environment, evaluation และ packaging mechanics,
KKU client ที่ตรวจ credentials privately และหลักฐานเก่าเป็น reference ของ environment
ต้องเปลี่ยน orchestrator/compact buggy context/prompt stages/reporter สำหรับ condition ใหม่
ไม่ flip Gate A หรือเปิดคิวเดิมที่ยังไม่ผ่านเงื่อนไข เพื่อหลอกว่ารุ่นเก่าพร้อม
condition ใหม่ต้องมี protocol/settings/runtime checks ของตัวเอง; ไม่จำเป็นต้องสร้าง shared recipe รุ่นใหม่ครบ 691 declarations
Csv-1 รายงานเดิมยังเก็บไว้ แต่เป็น fixed-assisted cohort คนละชุดกับ AI buggy-only ใหม่
จะ reuse ผลใดต้องพิสูจน์ว่า configuration/context/protocol ตรงกันก่อน มิฉะนั้นแยกเป็น historical

## สิ่งที่ทำในรอบวางแผนนี้

ตรวจ repo เพื่อนแบบ read-only, ตรวจ source index ของเราได้ 854 บั๊ก/17 projects,
ทำ layout contract และรายการ planned jobs ทั้งหมดเป็น PENDING
ยังไม่ได้สร้าง batch runner ใหม่, ยังไม่ได้รัน pilot ใหม่, ไม่เรียก KKU และไม่เปลี่ยน live queues
ยังไม่รับรองวันเสร็จ; ต้องวัด pilot/batch และ current quota ก่อนกำหนดเวลา
