# แชมป์รับ v7 ของออมและเตรียมงานที่ไม่ต้องรอทีม

วันที่ 3 ตุลาคม 2569: ฐาน champ `2ac55e31` รวมออม `c25faa5e` ใน branch แยก
`codex/champ-v7-intake` ที่ Project_SQA_champ_v7 ไม่แตะไฟล์งานค้างบน test หรือ checkout ของออม
Native worktree tool ไม่ resolve repository reference จึงใช้ git worktree ของ repository ที่ตรวจแล้ว
ไม่มี KKU request, live queue mutation, quota ledger import หรือ account intake ในรอบนี้

## ผลตรวจรับ inputs

คู่ v7 ของออมที่รับมา runtime binding ถูก blocked เมื่อเทียบกับโค้ดหลังรวมของแชมป์
จึง build preparation และ compose proposal ใหม่โดยไม่แก้ artifacts เดิม
ตรวจ checksum 243 entries ครบ 20 bugs; selected 377 / unsupported 314 คง denominator 691
prompt ทั้ง 20 ไฟล์มี bytes/hash ตรงชุดออม; max Math-1 = 250,315 UTF-8 bytes
คู่ใหม่ผ่าน runtime, protocol/runner, preparation และ recipe bindings แต่ไม่ผ่าน Gate A
Enabled stages ว่าง, reserve null และ reviewed_by ทั้งสามคน false

- [Input audit และ signatures ที่ยังค้าง](../../output/api854-provider-preflight-20261003/champ-v7-input-audit-v1.json)
- [Fresh preparation](../../output/api854-20261003/champ-prepare-v7-current-v1/index.json)
- [Fresh proposal](../../output/api854-provider-preflight-20261003/champ-v7-current-proposal-v1/protocol.proposal.json)
- [Gate A ของคู่ใหม่](../../output/api854-provider-preflight-20261003/champ-v7-current-gate-a-v1.json)
- [สคริปต์ audit ที่ใช้](../../output/api854-provider-preflight-20261003/audit-champ-v7.py)

## Reserve และหลักฐาน provider ที่ยังขาด

[Worksheet ใหม่](../../output/api854-provider-preflight-20261003/champ-v7-reserve-worksheet-v1.json)
มี 40 แถวสำหรับ 20 bugs × 2 models พร้อม hashes ของ inputs/protocol ใหม่
เมื่อเสนอ output cap 4096: largest request guard floor = 254,411 + H
H คือ provider framing bound ที่ยังไม่มีหลักฐาน; bytes นี้ไม่ใช่ token count จริง
ตัวเลข quota ใน worksheet เป็น historical a01 observation เวลาไทย 07:13 เท่านั้น
Sonnet historical ceiling 200,000 ต่ำกว่า floor 54,411 ก่อน H
จึงไม่แก้โดยรอ reset หรือเพิ่มบัญชี ceiling เท่าเดิม และไม่ใช้ worksheet เปิดคำขอ

สิ่งที่ต้องได้จากหน้าบัญชีหรือผู้ดูแล KKU: context/output caps ของโมเดลที่ใช้จริง,
วิธีนับ source prompt tokens และ framing, bucket sharing, window/reset/timezone,
expiry ของ observation และ remaining ล่าสุด หาก final context/recipe เปลี่ยนต้องวัดใหม่
ไม่คัดลอกยอดเดิมเป็น current quota และไม่เติม unknown fields ด้วยการเดา
เตรียม [รายการขอหลักฐานรายโมเดล](../../output/api854-provider-preflight-20261003/champ-v7-provider-evidence-request-v1.json)
โดยไม่อ่านหรือขอ keys เพิ่ม เว็บ docs KKU เปิดผ่าน web tool ไม่สำเร็จในรอบนี้
จึงไม่มีข้อมูล provider ใหม่มาแทน historical receipt

## งานฝั่งแชมป์ที่แบ่งไว้แล้ว

ค้าง support/fixture/oracle review 169 declarations ใน bugs เจ้าของแชมป์:
Chart-1 48, Compress-1 11, JacksonCore-1 26, JacksonDatabind-112 8,
Math-1 34, Time-1 42; Gson-1 ไม่มี capability exclusions แต่ยังต้อง joint semantic review
รายละเอียด signatures/reasons อยู่ใน input audit เพื่อร่วมตรวจกับบีมโดยไม่ลด requirement
และแยกเป็น [worklist เฉพาะแชมป์](../../output/api854-provider-preflight-20261003/champ-v7-owner-worklist-v1.json) แล้ว
เริ่มตรวจตามกลุ่มที่มี existing recipe ใกล้เคียงและเสนอ fixture แบบ prospective;
ห้ามใช้ buggy outcome เลือก target หรือถือ null exception เป็น meaningful oracle โดยไม่มี review

## วิธีตรวจซ้ำ

รันจาก root ของ Project_SQA_champ_v7:

```powershell
python -m unittest discover -s scripts/study/api854/tests -t . -v
python -m scripts.study.api854.gate_a --protocol output/api854-provider-preflight-20261003/champ-v7-current-proposal-v1/protocol.proposal.json --runner output/api854-provider-preflight-20261003/champ-v7-current-proposal-v1/runner-plan.json --output output/api854-provider-preflight-20261003/champ-v7-gate-recheck.json
```

เลือก output ใหม่ที่ยังไม่มีสำหรับ checker/build/composer/audit ทุกครั้ง
ผล validation รอบแรกเก็บไว้เป็นประวัติ: discover ขาด -t . ทำ relative imports ล้มเหลว
และ merge ทำ selected-pair test ใช้ v6 builder กับ v7 protocol; แก้เฉพาะ import เป็น v7
คง regression ทั้ง stale-v6 และ repinned-runtime/old-recipe ไว้ ไม่แก้ assertions ให้ผ่าน
ผลตรวจรอบแก้จะบันทึกใน validation receipt พร้อม log hash

## งานที่รอข้อมูลจริง

Gate A ยังรอ host readiness, รองรับอีก 314 declarations, condition-bound semantic/oracle review,
provider limits/quota/reserve และการตรวจรับทั้งสามคน ก่อนออม freeze และ seed run ใหม่
Local development suites เดิมคง source/condition versions ของตน ไม่ relabel เป็นผล v7
primary completion ยังคง 0; account intake พักไว้ตาม checkpoint เดิม

## ผล verification ล่าสุด

API854 รัน 307 tests: ผ่าน 306 / skip 1 (Windows symlink privilege); legacy/Java ผ่าน 30 tests
รวมไม่ซ้ำ 337 tests: ผ่าน 336 / skip 1 / failures 0 / errors 0
Selected Gate A 13 tests ผ่านและรวมอยู่ใน API854 total แล้ว
[Validation receipt พร้อม log hashes](../../output/api854-provider-preflight-20261003/champ-v7-validation-v1.json)
Self-review ตรวจ merge diff, runtime guards, regression assertions และ input/recipe hashes แล้ว
หลักฐาน runtime เป็น offline/mock consumer tests ไม่ใช่ live pilot หรือ primary outcomes
