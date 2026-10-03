# Handoff ทำคนเดียว — Solo / เวลาที่ผู้ใช้แจ้งเหลือ 20 ชั่วโมง

## คำสั่งล่าสุดของผู้ใช้มีผลเหนือแผนแบ่งงานเดิม

ผู้ใช้แจ้งว่า **บีมและออมไม่ทำต่อแล้ว ผู้ใช้จะทำ solo** และขอ handoff.
Codex คนถัดไปต้องรับงานฝั่ง Champ, การทดลองที่เหลือ, รวมผล และจัดชุดส่งเอง.
ใช้หลักฐานที่เพื่อน push ไว้ได้ แต่ไม่รอข้อความ, verdict, การรัน หรือการเขียนรายงานจากเพื่อน.
เครื่อง Linux ของบีม/ออมเป็นหลักฐานสภาพแวดล้อมในอดีต ไม่ถือว่ายังเข้าถึงหรือใช้งานได้.

เวลาที่ผู้ใช้แจ้งก่อนขอ handoff คือ **20 ชั่วโมง**; deadline แบบวัน/เวลายังไม่ได้ระบุ.
เวลาผ่านไประหว่างทำ handoff แล้ว อย่าเริ่มนับ 20 ชั่วโมงใหม่เมื่อรับช่วง.
เป้าหมายที่ผู้ใช้เลือกไว้คือ **เก็บผลจริงครบ 4 วิธีจากชุดที่พร้อมก่อน แล้วขยาย bugs ตามเวลาที่เหลือ**.
จากยอดและ throughput ที่ตรวจได้ ยังไม่มีหลักฐานรองรับว่าจะครบ 854 × 4 วิธีทันเวลานี้.

## คำตอบเรื่องเวลาทำครบ 854 เมื่อ solo

ผู้ใช้ถามเพิ่มว่าหากใช้ Codex ทำคนเดียวจะใช้เวลานานเท่าใด.
ยังไม่มี ETA ที่รับรองวันจบได้; สำหรับ 854 bugs × 4 วิธีพร้อมตรวจหลักฐาน
ให้สื่อสารเป็น **ระดับหลายสัปดาห์ถึงหลายเดือน** ตามงานที่ยังไม่พร้อมและข้อจำกัด provider.

ตัวอย่างคำนวณเดิมประมาณ 43–85 วันเป็นเพียงสมมติฐานของ Sonnet generation:
ถ้า 10 aliases มี independent quota ที่เติมทุกวัน และแต่ละ alias ทำ generation ได้ 1–2 bugs/วัน
จะได้ 10–20 bugs/วัน; 854 หาร throughput นี้ประมาณ 42.7–85.4 วัน.
ไม่มีหลักฐานยืนยัน independent buckets/reset/expiry และยังไม่รวม setup/oracle/invalid-suite work.
ห้ามนำตัวอย่างนี้ไปสัญญาว่าจะเสร็จภายในช่วงดังกล่าวหรือบอกว่าเป็นเวลารัน CPU อย่างเดียว.
จำนวนผล valid ครบทุก bug ไม่สามารถรับประกันได้; failures ที่เกิดจริงต้องเก็บและรายงาน.
ผู้ใช้ยังไม่ได้ยกเลิกคำขอ handoff หรือเปลี่ยนคำสั่ง solo จากการถามเวลานี้.

## เริ่มจาก checkpoint นี้

Workspace: `D:\Projects\SQA_p\Project_SQA`, PowerShell, branch `champ`.
ก่อนสร้าง handoff: Champ `8389043b687b0712119c6540fb06bbce387e76e7`.
หลังรับช่วงให้ใช้ commit ที่มีเอกสารนี้ แล้วตรวจ `git status` / `git log -1`.

Pins ของ peer ที่อ่านล่าสุด:

| ชุด | Exact commit | ใช้ทำอะไร |
|---|---|---|
| Aom ล่าสุด | `e2ce1e2701e5d08a01cef0481e53ea621bc9956e` | report v6 รวม Csv/Jsoup และ Cli ใหม่; ยังไม่รวมผล Compress ล่าสุด/JacksonDatabind112 |
| Aom Cli | `7f0c6d31a4fee1b8e4b20ca8cf02513dceb990e3` | prospective unordered-options oracle, preparation v2, algorithms สองวิธี |
| Beam ล่าสุด | `f3484746fc19b5aa642e51fb08de5471a0c17509` | Compress full Defects4J สอง algorithms พร้อม GNU/EOL binding |
| Frozen generation v12 | `63ad195623c2ed3f67f3ae232c00c54d3160ce72` | 20 bugs / 403 selected declarations / 288 exclusions; ไม่ใช่จำนวน bugs ที่รันสำเร็จ |

Shared working-tree checkpoint ยังเป็น v9 และมี **100 pins**; ไม่ merge runtime ของ peer มาทับ.
ผลที่ใช้ v12 ให้ใช้ frozen runtime 41 files ของ packet นั้น; Cli ใช้ fork/preparation v2 ของ Cli.
Primary ของ cohort 854 ยัง 0 / Gate A=false / final reserve=null.
ผล development ที่รันได้จริงใช้รายงานได้เมื่อเปิดเผย scope; ไม่เปลี่ยนชื่อให้เป็น primary ที่ผ่าน Gate A.

Snapshot สำหรับ handoff อยู่ที่
`output/api854-20261004/champ-solo-handoff-v1/`:

- `status.json`: pins, ยอด, งานค้างและที่มาของตัวเลข.
- `checkpoint-pins.json`: 100 pins ที่ตรวจ ณ handoff.
- `native-results.json`: 28 outcomes เดิม รวม invalid; 6 unique bugs.
- `reviewed-host-results.json`: คำรับ scoped Csv/Jsoup เดิมของ Champ.
- `peer-documents/`: exact Git bytes ของเอกสาร Aom/Beam ล่าสุด; ไม่ใช่การ merge.
- `peer-records/`: canonical Cli/Compress records ที่ตรวจ SHA-256 แล้ว.
- `historical/`: summary/protocol เก่าที่เก็บแยก; ยังไม่ได้ re-audit ทั้ง cohort รอบนี้.
- `previous-documents/docs/api854/CHAMP_CODEX_HANDOFF_TH.md`: main handoff เดิมก่อนเพิ่มลิงก์ solo.
- `checksums.json`: ตรวจ bytes ทั้ง packet; ห้ามแก้ไฟล์ที่ seal แล้ว.

## ยอดจริงของชุดใหม่ ณ handoff

นับ unique bug ต่อวิธีที่มี owner-completed full Defects4J record; ไม่บวก host replay หรือ condition ซ้ำ.
คำรับ scoped ของ Champ ปิดแล้วสำหรับ Csv/Jsoup. Cli/Compress มี canonical record hashes ตรง;
การ audit raw source/oracle/host ของ Champ ยังไม่ครบ จึงต้องแสดง review status ตามจริง.

| วิธี | Unique bugs ที่มี full D4J completed records | เทียบเป้าทีม 854 |
|---|---:|---:|
| CMA-ES | 4: Csv-1, Cli-1, Jsoup-1, Compress-1 | 0.47% |
| FSCS-ART | 4: Csv-1, Cli-1, Jsoup-1, Compress-1 | 0.47% |
| KKU Sonnet | 1: Csv-1 | 0.12% |
| KKU Gemini | 1: Csv-1 | 0.12% |

มี **Csv-1 เพียง bug เดียวที่ valid full D4J ครบ 4 วิธี** ใน generation condition ล่าสุด.
Native ของ Champ มี 6 bugs / 28 condition×method outcomes รวม failures; AI generation requests 14
และ calibration 13 รวม billable requests 27. ไม่ตีความจำนวน outcomes/tests/403 declarations เป็น bugs.

| Bug / condition ที่ใช้ | CMA-ES | FSCS-ART | Sonnet | Gemini |
|---|---|---|---|---|
| Csv-1 Messages | 30 tests; full D4J; fault=false | 30 tests; full D4J; fault=false | 21 tests; full D4J; CR fault | 18 tests; full D4J; CR fault |
| Cli-1 unordered ใหม่ | 30 tests; full D4J; fault=false | 30 tests; full D4J; fault=false | ยังไม่ generate ใน condition ใหม่ | ยังไม่ generate ใน condition ใหม่ |
| Jsoup-1 | 30 tests; full D4J; fault=false | 30 tests; full D4J; fault=false | 27 tests; fixed fail1 ทั้งสองรอบ; reject ทั้ง suite | 9 tests; fixed fail2 ทั้งสองรอบ; reject ทั้ง suite |
| Compress-1 | 30 tests; owner full D4J; buggy fail1 | 30 tests; owner full D4J; buggy fail5 | truncated4096 | 8 tests; compile failed |
| Gson-1 | native fixed fail11; oracle identity problem | native fixed fail8; oracle identity problem | truncated4096 | 6 tests; compile failed |
| JacksonDatabind-112 | native30 fixed2/buggy/coverage ผ่าน; full D4J ยังไม่มี receipt | native30 fixed2/buggy/coverage ผ่าน; full D4J ยังไม่มี receipt | truncated4096 | 9 tests; fixed fail4 ทั้งสองรอบ |

Csv class lines/branches: algorithms31/37,13/26; Sonnet36/37,22/26; Gemini37/37,23/26.
Cli: CMA43/45,14/16; FSCS42/45,12/16. Jsoupทั้งคู่36/46,10/18.
Compress owner records ทั้งคู่98/165,21/59; fault ยังต้องอ่าน binding/oracle receipt ก่อนรวมคำรับของ Champ.
Coverage ทั้งหมดนี้เป็น target class ไม่ใช่ coverage ทั้ง project/dataset.
Csv AI ใช้ input domain กว้างกว่า bounded algorithm streams; ห้ามสรุปความเหนือกว่าทั่วไป.
Cli FSCS condition เก่ามี option-order false positive ซึ่ง quarantine แล้ว ไม่ใช่ confirmed fault.

## ผลเก่าที่อาจใช้ประกอบรายงานได้ — แยกเงื่อนไขให้ชัด

มี `output/kku-only-20261001/summary.json` และ `results/study/kku-only-20261001/protocol.json`.
Summary รายงาน 185/204 completed runs: CMA51/51, FSCS51/51, Gemini51/51, Claude32/51.
Protocol เก่าเลือก **1 bug ต่อ 17 projects × 3 repeats × 4 วิธี**; มี exact model labels ต่างรุ่น
และอนุญาต local processing/fixed-only pruning ตาม policy เก่า.

นี่เป็นหลักฐานจริงที่มีอยู่ก่อนเป้า 854; **ไม่ได้ re-audit canonical records ทั้งหมดใน handoff นี้**.
185 คือจำนวน runs ไม่ใช่ 185 bugs. ห้ามรวมกับ no-repair Sonnet condition ใหม่เป็น cohort เดียวกัน.
ถ้าใช้เป็นผลประกอบ ให้เปิดเผย model labels, reused/new, local processing, missing Claude runs
และตรวจ existing provenance audits ก่อนใช้ตาราง. ไม่อ้างว่า historical คือผลใหม่ครบ 854.

โจทย์ PDF หน้า 3 ข้อ 2.2 ระบุ Java projects ใน Defects4J “ทุกรายการ” แต่ไม่เขียนเลข 854
หรือจำนวน bugs ต่อ project. ยังไม่ได้รับคำยืนยันจากอาจารย์ว่าเลือก bugs ตัวแทนได้หรือเลือก project เดียวได้.
การจัดชุดส่งจากผลที่มีไม่ใช่หลักฐานว่าครบข้อกำหนดทั้งหมด.

## ลำดับงาน solo ที่ลดความเสี่ยงส่งไม่ทัน

1. **ทำรายงานจากหลักฐานที่มีทันที**: ใช้ Aom report v6 เป็นฐานอ่าน, เติม Compress/JacksonDatabind112
   ตาม review status จริง และคง pending/invalid/null. ทำ draft รายงาน+ตาราง+demo ให้มีภายในช่วงแรก
   ไม่รอให้รันครบ 854 หรือปิด candidate ทั้งหมด. แยก historical กับ new development เป็นคนละส่วน.
2. **ปิดงานที่พร้อมและทำให้ได้ผล 4 วิธีเพิ่ม**: Cli AI ใหม่เป็นตัวเลือกแรก เพราะ algorithms/oracle/preparation
   พร้อมแล้ว. ตรวจ exact preparation v2, embedded helper/prompt, source/runtime/settings และ current quota
   ก่อนทำ prospective condition; เก็บ raw outcomes ทั้งชุด ไม่ retry/prune/repair failures เพื่อเพิ่มยอด.
   Generic live worker ไม่รองรับ Cli contract นี้อัตโนมัติ; อย่า flip queue/generation flags เพื่อข้าม guard.
3. **รันเฉพาะบนเครื่องที่ solo ใช้ได้จริง**: Champ ปัจจุบัน Windows/Java17 สำหรับ native;
   ยังไม่มี proof ของ Champ Linux/Java11 full D4J host. หากไม่มี Linux host พร้อม ห้ามติดตั้ง/ย้ายระบบจนกิน
   เวลารายงานทั้งหมด; ใช้ผล full D4J ของเพื่อนที่มีแล้วและรายงาน new native แยก condition ตามจริง.
4. JacksonDatabind112 มี valid algorithm archives สองชุดและ explicit GNU/EOL binding พร้อม;
   replay เมื่อ solo มี Linux/Java11 host ที่ผ่าน source/library/CPU guards เท่านั้น. ห้ามใช้ชื่อ host ของเพื่อน
   หรือสรุปว่า peer รันแล้วเพราะเคยส่งคำสั่งไป.
5. กัน **อย่างน้อย 6–8 ชั่วโมงสุดท้าย** สำหรับ freeze ตาราง, รายงาน, สไลด์, demo และตรวจชุดส่ง.
   ณ จุดตัดนั้นหยุดเพิ่ม bugs/conditions; งานที่ยังไม่ผ่านคง failure/pending พร้อมเหตุผล.

พัก Graphics/Codec/Chronology/enum candidate ใหม่, compose preparation รุ่นใหญ่ และ full-semantic audit
ทุก declaration ที่ยังไม่มีผลทดลอง. งานเหล่านี้เก็บเป็น limitations/future work; ไม่ควรขวางการทำรายงาน.
คำรับ pending ที่จำเป็นต่อการใช้ผลให้ Codex solo ตรวจจาก pinned evidence เอง ไม่รอเพื่อน.
ยังไม่เปิด global live queue หรือ primary gate โดยอัตโนมัติ.

## Paths สำคัญและวิธีอ่าน peer โดยไม่ทับ frozen inputs

```powershell
git status --short
git log -1 --format="%H %s"
git fetch origin +refs/heads/aom:refs/remotes/origin/aom +refs/heads/beam:refs/remotes/origin/beam +refs/heads/champ:refs/remotes/origin/champ
git show e2ce1e2701e5d08a01cef0481e53ea621bc9956e:docs/api854/AOM_READY_PEER_RESULTS_TH.md
git show 7f0c6d31a4fee1b8e4b20ca8cf02513dceb990e3:docs/api854/AOM_CLI_UNORDERED_RESULTS_TH.md
git show f3484746fc19b5aa642e51fb08de5471a0c17509:docs/api854/BEAM_COMPRESS_AND_AOM_CSV_RETURN_TH.md
python -B -X utf8 -c "from scripts.study.api854.verify_chronology_development import checkpoint; p,_=checkpoint(); print('checkpoint pins:',len(p))"
```

Peer paths อยู่ใน pinned branch; ไม่จำเป็นต้องมีใน root checkout ของ Champ.
อ่าน Git blobs ด้วย `BatchedObjects` หรือ `git show`; archive ใช้ `git -c core.autocrlf=false archive`.
`BatchedObjects.preload([])` มีข้อจำกัด: อย่าเรียกเมื่อชุด paths ว่าง.

- Cli: `output/api854-20261004/aom-cli-unordered-preparation-v2/`, `aom-cli-unordered-d4j-v2/`.
  Prompt170764 bytes SHA `7dbe11213e2c3608ee5f9ade657691438e52c1541575c293ee98cc1dbc587439`.
  Helper SHA `d40b49631d9d5318027a1249c7cfeaa16fc208c203c53bfd19fd3bf8cd7372db`.
- Compress: `beam-champ-compress-d4j-v2/`, `beam-compress-benchmark-binding-v2/`.
  Execution condition `beam-compress-d4j-gnu-patch-benchmark-counted-development-v2`.
  Native target เป็น CRLF, benchmark เป็น LF; strict failure เดิมอยู่ครบ ห้ามลด exact source guard.
- JacksonDatabind112: `champ-jacksondatabind112-native-measurement-v1/`,
  `champ-jacksondatabind112-benchmark-binding-review-v2/` และ
  [คำสั่ง replay/receipt เดิม](CHAMP_AOM_CSV_AND_JACKSON_NEXT_RETURN_TH.md).
- ตาราง Champ ล่าสุด: `champ-aom-next-batch-audit-v1/{native-results,reviewed-host-results}.json`.
- Aom report ล่าสุด: `aom-ready-results-report-v6/`; เป็น checkpoint ก่อนรับ Compress ล่าสุด/Champ838.

## API / ความปลอดภัย / ข้อจำกัดที่ต้องจำ

Credentials อยู่ใน ignored `.local/api854/accounts.json`, aliases a01–a10.
ห้ามแสดง key, email หรือคัดลอก secret ลง docs/logs/Git. ไม่ต้องขอ key ซ้ำถ้าไฟล์เดิมยังอยู่.
KKU origin `https://gen.ai.kku.ac.th/api/v1`; ไม่มี Authorization redirects.
Sonnet `/messages`, requested `claude-sonnet-5`, actual `anthropic/claude-sonnet-5`, thinking disabled.
Gemini `/chat/completions`, `gemini-3.5-flash-lite`; temperature0/max output4096/stream=false/cap30.
A01–A06 เคยใช้แล้ว; A07–A10 ยังไม่มี current quota proof. ยืนยัน account allocation ก่อน new condition
ไม่ย้าย failed condition ไป key อื่นเพื่อวนลอง. ไม่มีหลักฐานว่า 10 keys เป็น quota pools อิสระ.
A06 ล่าสุดที่ response เคยรายงาน Sonnet115252/Gemini297281 เป็นอดีต ไม่ใช่ current quota.
Prompt bytes ไม่ใช่ tokens; final40pairs reserve/limits/framing/reset/expiry ยังไม่ครบ.

ห้ามแตะ untracked `SQA_Project_2026 (2).pdf`. เก็บ sealed packets/failed attempts/raw AI assertions เดิม.
Windows ไม่มี `sandbox_permissions`; ใช้ `python -B -X utf8`, อ่าน Thai ด้วย UTF-8.
อย่าล้าง/ย้าย recursive paths ที่ยังไม่ได้ตรวจ absolute containment และอย่า repurpose `$HOME`.
ช่วงทำ handoff นี้ไม่มี KKU call, evaluation, queue mutation หรือการสร้าง report ผลใหม่.
ร่างแผน 20h ใน turn ที่ถูกขัดจังหวะยังไม่ได้สร้างไฟล์; อย่าเข้าใจว่า report ชุดส่งเสร็จแล้ว.

## Prompt สำหรับ Codex คนถัดไป

```text
อ่าน docs/api854/CHAMP_SOLO_HANDOFF_TH.md และ snapshot champ-solo-handoff-v1 แล้วรับช่วงบน branch champ แบบ solo: บีมกับออมไม่ทำต่อ ไม่รอ peer งานใหม่ ตรวจ checkpoint100pins/commit/status ก่อนทำต่อ เวลาที่ผู้ใช้แจ้งเหลือ20ชม.เป็นเวลาจากบทสนทนาเดิมห้ามเริ่มนับใหม่ ทำรายงาน/ตาราง/demo จากผลจริงที่มีทันที แยก historical17projects กับ development cohort854 และ invalid/pending ตามจริง จากนั้นปิด Cli AI condition ใหม่และเฉพาะ ready batches บน host ที่ใช้ได้จริงก่อนจุดตัดเวลารายงาน ไม่ compose candidates ใหม่ ไม่อ้างครบ854/GateA ไม่รันผลเดิมซ้ำ ไม่แก้/prune AI tests เพื่อให้ผ่าน ใช้ keys ใน ignored local fileโดยไม่แสดง secrets เก็บหลักฐานแล้ว commit/push champ เมื่อได้ milestone
```
