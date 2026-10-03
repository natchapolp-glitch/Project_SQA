# งานออมที่ทำได้โดยไม่รอทีม — source preparation / recovery / demo

คำสั่งผู้ใช้: ทำงานที่ออมทำได้เองต่อบน `aom` ใช้หนึ่ง CPU slot; ยังไม่เรียก KKU หรือเปิด primary queue ชุด source preparation ใหม่นี้แยกจาก shared preparation v5 และไม่เปลี่ยน frozen protocol/runtime เดิม

ผล source/build ที่ตรวจแล้ว: **284 unique bugs ของออม เก็บ fixed source และ compile ผ่านครบ 284** ผล v1 เดิมผ่าน 282 และ source selection ติด 2 รายการ แก้ Gson-18 ใน v2 และ Time-25 ใน v3 โดยรักษา raw failures เดิม รวม **287 source preparation attempts** (284 completed original records + 2 recovery attempts + 1 interrupted attempt) ไม่ใช่จำนวนรอบทดลองหลัก ผล primary ใหม่ยังเป็น **0**

เครื่องมือ preparation ผ่าน **15 offline unit tests** ตาม `aom-independent-unit-validation-v2.json` Receipt ก่อนหน้านี้ 11 tests และ source/test snapshots ใน `aom-independent-unit-history-v1/` เก็บเป็นประวัติของ finalizer ก่อนเพิ่ม recovery version ที่สอง ไม่ใช้แทน validation รุ่นล่าสุด

## ไฟล์ที่เปิดอ่าน

- `output/api854-20261003/aom-owner-sources-final/index.json`: สถานะรวมล่าสุดของออม 284 bugs พร้อมลิงก์หลักฐานแต่ละ version; ใช้เมื่อ final audit เสร็จ
- `output/api854-20261003/aom-independent-checkpoint.json`: สรุป independent preparation และ hashes ของไฟล์ชุดส่ง แยก primary completion และ Gate A ชัดเจน
- `output/api854-20261003/aom-owner-sources-v1/index.json`: ผล attempt เดิมทุก ID; เก็บ failures เดิมโดยไม่เขียนทับ
- `output/api854-20261003/aom-owner-progress.html`: เปิดจากไฟล์ใช้ snapshot/โหลด index เอง; เปิดผ่าน localhost จะอ่าน index ในเครื่องทุก 10 วินาที ค้นหา/กรองและดู record ได้
- `output/api854-20261003/aom-recovery-preparation-v3.json`: ผล actual crash/reopen/fencing ใน private isolated store บนเครื่องเดียว
- `output/api854-20261003/aom-recovery-public-evidence-v3/`: sanitized job/attempt records และ offline artifacts สองไฟล์พร้อม SHA256 ไม่รวม leases/credentials/database
- `REPORT_854_DRAFT_TH.md`: โครงรายงาน ไม่มี metrics ที่แต่งขึ้น
- `AOM_DEMO_PREPARATION_TH.md`: บทซ้อมเดโมหลักฐานปัจจุบันและรายการที่จะเติมเมื่อมีผลจริง
- `output/api854-20261003/AOM_SQA_854_Preparation_DRAFT_20261003.pptx`: สไลด์ร่าง 9 หน้า อธิบายแผนและหลักฐานเตรียมงาน เว้นผลเปรียบเทียบไว้รอ primary จริง
- `AOM_CONTINUATION_V5_TH.md`: ขั้นตอนที่ยังรอทีมก่อนเปิด primary

## Source preparation ทำอะไร

ตรวจ ownership กับ installed active inventory แล้วทำเฉพาะ 284 bugs ของออมทีละรายการบน WSL `Ubuntu` user `team` เก็บ dedicated worktrees ใหม่ที่ `/home/team/sqa-round2/aom-source-context-v1/` ไม่ใช้หรือแก้ worktrees ของเพื่อน

แต่ละบั๊ก checkout รุ่น fixed, ตรวจ HEAD กับ installed fixed tag, ยืนยัน selected source/build ไม่มี diff, export bytes เดิมของ modified production Java classes กับ build files ที่มีใน root แล้ว compile fixed หนึ่งครั้ง เก็บ source context ก่อน compile และตรวจ selected bytes ซ้ำหลัง compile ไม่อ่าน buggy patch/trigger tests และไม่ส่ง execution logs เข้า AI

Compile timeout 120 วินาทีและ checkout timeout 300 วินาทีเป็นขอบเขตของ prerequisite probe นี้ ไม่ใช่ timeout ของ primary protocol ความล้มเหลว/timeout เก็บเดิมพร้อม logs และ reason; source ที่ตรวจได้ก่อน compile failure ยังเก็บไว้ ห้ามแปลว่าพร้อมรับรอง Gate A

ไฟล์ต่อบั๊ก: `record.json`, `revision-proof.json`, `context/context-manifest.json`, `context/fixed-source/`, stage `command.json`/`command.log`, `checksums.json` แฟ้ม config ผูก ownership/installed/exporter/selector/driver hashes ไว้ `primary_usable=false` เสมอ

## ทำต่อและตรวจ bytes

รันจาก repository root ใน WSL; ใช้คำสั่งนี้กับ installation และ paths ของเครื่องนี้เท่านั้น:

```powershell
wsl -d Ubuntu -u team -- bash -lc 'cd /mnt/c/WORK/SQA_PROJECT/Project_SQA && python3 -m scripts.study.api854.prepare_owner_sources --d4j /home/team/sqa-round2/defects4j/framework/bin/defects4j --worktrees /home/team/sqa-round2/aom-source-context-v1 --output output/api854-20261003/aom-owner-sources-v1 --resume'
```

`--resume` ตรวจ hashes แล้วข้าม completed records ไม่ checkout/compile ซ้ำ completed failure ก็ถือว่าได้บันทึกแล้ว ไม่ retry เงียบ ๆ ถ้ามี unfinished folder จากการหยุดฉับพลัน จะหยุดให้ reconcile โดยรักษา folder/worktree เดิม ห้ามลบหรือแก้ checksums เพื่อให้ผ่าน ให้บันทึกสาเหตุและใช้ attempt/output ใหม่ที่ทีมตกลง ถ้า source/config/policy hashes เปลี่ยนให้สร้าง version ใหม่ ไม่เขียนทับ version นี้

บนเครื่องอื่น clone ได้เฉพาะ public evidence ไม่มี worktrees/Defects4J installation/SQLite/keys ต้องติดตั้งและตรวจ environment ของเครื่องนั้นก่อนสร้าง preparation ใหม่ และใช้ paths/output ใหม่ ไม่อ้างว่า receipt เครื่องนี้เป็นการรันบนเครื่องใหม่

```powershell
python -X utf8 -m scripts.study.api854.audit_owner_sources --folder output/api854-20261003/aom-owner-sources-v1 --receipt output/api854-20261003/aom-source-audit-new.json
python -X utf8 -m scripts.study.api854.owner_progress_page --index output/api854-20261003/aom-owner-sources-v1/index.json --output output/api854-20261003/aom-owner-progress.html
```

Audit final ปฏิเสธ records ที่ยัง pending; `--allow-pending` ใช้ตรวจ checkpoint ระหว่างเตรียมเท่านั้น Receipt ต้องเลือก path ที่ยังไม่มีเพื่อเก็บ audit เดิม ขณะ batch ทำงาน index เป็น snapshot ของ completed records ล่าสุด อย่า package folder ที่กำลังเขียน

## การรักษา attempt เดิมและ source recovery

Lang-53 รอบแรกหยุดระหว่าง checkout ไม่มี exit receipt จึงบันทึกเป็น `interrupted_exit_unknown` ไม่เรียกว่าผ่านหรือล้มเหลว เก็บ raw record/log พร้อม SHA256 ใน `aom-source-interrupted-attempts/Lang-53-session1/` และย้าย worktree ที่ยังไม่เสร็จไป `aom-source-context-v1/interrupted/Lang-53-session1` ก่อนเริ่มใหม่ ตรวจแล้วไม่มีกระบวนการเดิมทำงานอยู่

Gson-18 พบปัญหา selector เดิมตัดชื่อ `com.google.gson.internal.$Gson$Types` ที่เครื่องหมาย `$` ทั้งที่เป็นชื่อ top-level class จริง เก็บ error เดิมใน v1 แล้วใช้ `owner_source_selection_v2.py` ที่ค้น literal filename ก่อน fallback enclosing class สำหรับ inner classes ทำเฉพาะบั๊กนี้ใน output/worktree v2 ใหม่ ไม่แก้ Java bytes หรือ frozen selector เดิม

Selector v2 ใช้เฉพาะ independent source preparation ในชุดนี้ การนำไปใช้กับ shared generation packet ต้องเลือก policy/version ใหม่และตรวจรับร่วมทีมก่อน ไม่ถือว่าแก้ shared frozen selector เดิมแล้ว

Time-25 พบ `DateTimeZone.java` สองแห่ง เพราะ suffix ของไฟล์ใน `JodaTimeContrib/gwt/.../emul/org/joda/time/` ตรงกับชื่อ class ด้วย แต่ Defects4J export `dir.src.classes` ระบุ `src/main/java` จึงใช้ selector/driver v3 เฉพาะบั๊กนี้ จำกัดการค้นใน production root ที่ export จริง พร้อมเก็บ raw export log ไม่เลือกลำดับแรกจากไฟล์ซ้ำ และไม่แก้ source Java เก็บ failure เดิมไว้ใน v1

```powershell
wsl -d Ubuntu -u team --cd /mnt/c/WORK/SQA_PROJECT/Project_SQA -- python3 -m scripts.study.api854.prepare_owner_sources_v2 --d4j /home/team/sqa-round2/defects4j/framework/bin/defects4j --worktrees /home/team/sqa-round2/aom-source-context-v2 --output output/api854-20261003/aom-owner-sources-v2 --only Gson-18
wsl -d Ubuntu -u team --cd /mnt/c/WORK/SQA_PROJECT/Project_SQA -- python3 -m scripts.study.api854.prepare_owner_sources_v3 --d4j /home/team/sqa-round2/defects4j/framework/bin/defects4j --worktrees /home/team/sqa-round2/aom-source-context-v3 --output output/api854-20261003/aom-owner-sources-v3 --only Time-25
python -X utf8 -m scripts.study.api854.finalize_owner_sources --original output/api854-20261003/aom-owner-sources-v1 --recovery output/api854-20261003/aom-owner-sources-v2 output/api854-20261003/aom-owner-sources-v3 --interruptions output/api854-20261003/aom-source-interrupted-attempts --output output/api854-20261003/aom-owner-sources-final
wsl -d Ubuntu -u team --cd /mnt/c/WORK/SQA_PROJECT/Project_SQA -- python3 -m scripts.study.api854.prove_gson18_source_recovery --receipt output/api854-20261003/gson18-selection-recovery-proof.json
wsl -d Ubuntu -u team --cd /mnt/c/WORK/SQA_PROJECT/Project_SQA -- python3 -m scripts.study.api854.prove_time25_source_recovery --receipt output/api854-20261003/time25-selection-recovery-proof.json
wsl -d Ubuntu -u team --cd /mnt/c/WORK/SQA_PROJECT/Project_SQA -- python3 -m scripts.study.api854.audit_fixed_git_bytes --receipt output/api854-20261003/aom-fixed-git-bytes-audit.json
python -X utf8 -m scripts.study.api854.owner_progress_page --index output/api854-20261003/aom-owner-sources-final/index.json --output output/api854-20261003/aom-owner-progress.html
```

คำสั่งสร้าง receipts/final directory ใช้ครั้งเดียวกับ path ที่ยังไม่มีเท่านั้น หลักฐานที่มีแล้วให้เปิดตรวจ ไม่รันเขียนทับ Final index นับ unique bugs 284 แยกจาก completed original records, recovery attempts และ interrupted attempts ไม่เพิ่มจำนวน bugs หรือ primary เมื่อ retry

Git-object audit ตรวจ Java bytes กับ fixed HEAD จริง ไม่ใช่เพียงตรวจ hashes ใน manifest ซ้ำ Build files ที่ Defects4J สร้างเพิ่มและไม่มีใน Git แสดงเป็น untracked build snapshots แยกจาก tracked fixed source

## Recovery ที่ตรวจจริง

การซ้อม v3 ใช้ worker subprocess ที่ `os._exit(17)` หลัง claim/upload จริง เปิด private store กลับมาสองครั้ง ตรวจ file hashes และจำนวน jobs/attempts จำลอง lease expiry ใน private DB เพื่อไม่ต้องรอ แล้วยืนยัน worker เก่าส่งไฟล์ไม่ได้, job เดิมได้ attempt ใหม่, artifact เดิมอยู่ครบ และ disabled generation ไม่มี claim

ผลนี้ตรวจ same-host persistence/reclaim/fencing เท่านั้น ไม่อ้างว่าตรวจ distributed network partitions, cross-machine concurrency, power failure หรือ real KKU dispatch recovery แล้ว `CREDENTIALS` ใน rehearsal เป็นข้อความ dummy ที่ไม่มีสิทธิ์กับ provider เก็บ private claims/SQLite ไว้ `.local/` ไม่ push

## สิ่งที่ยังเหลือหลังงานนี้

1. บีม: reviewed fixtures/receiver/arguments/oracles และ evidence ของ pilot อีก 15 bugs ตาม shared policy รุ่นใหม่
2. แชมป์: actual exact model settings/limits/quota unit/window/framing/overhead เพื่อปิด measured reserve
3. ออมและทีม: รวม final hashes, ตรวจ protocol/runner และ Gate A ร่วมกัน ก่อน dispatch 80-job pilot
4. เตรียม shared targets/fixtures สำหรับบั๊กที่เหลือหลัง pilot แล้วขยาย generation/evaluation/semantic review ครบ 854 × 4 = 3,416 jobs จากนั้น freeze results เติมผลจริงในรายงาน/สไลด์ และอัดเดโม

Source compile ผ่านและการซ้อม recovery ไม่เพิ่ม primary completion ไม่ใช้โควตา KKU และไม่เปลี่ยนสถานะ live queue

## ส่งให้คนถัดไป

ให้ดึง branch `aom` ล่าสุด แล้วเริ่มจากเอกสารนี้กับ `AOM_CONTINUATION_V5_TH.md` Git เก็บ code, public receipts, fixed source context และเอกสาร/สไลด์ร่างของ checkpoint นี้ ชุด `AOM_Independent_Preparation_20261003_Public.zip` เป็น public snapshot ของ commit ที่ระบุใน `PACKAGE_SCOPE.json` พร้อม SHA256 รายไฟล์ ใช้ส่งต่อไฟล์ในเครื่องได้ ส่วน `.package.json` บน Git ระบุ SHA256 ของ ZIP

ทั้ง Git และ ZIP ไม่รวม keys, private SQLite, Defects4J installation หรือ worktrees ของเครื่องออม ผู้รับต้องเตรียมและตรวจ environment ของตนเองก่อนรันใหม่ ใช้ public evidence นี้ตรวจรับย้อนหลังได้ แต่ต้องไม่อ้างว่าเป็นการรันบนเครื่องผู้รับ และยังต้องปิด dependencies/Gate A ก่อนเปิด primary
