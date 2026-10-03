# บีม: หลักฐานพัฒนา 15 bugs และ requirement ครบ 691 declarations

ตรวจสถานะวันที่ 2026-10-03 จาก immutable receipts ของรอบแรกและรอบซ่อม
บน `beam-pc1` / CPU 1 slot
ไม่มี KKU request หรือ live queue mutation ในชุดนี้

## ชุด 15 bugs ที่แชมป์รอ

มี **30 development suites ของ 15 bugs** ผ่าน local fixture/oracle review แล้ว
แต่ละ bug มี FSCS-ART และ CMA-ES; แต่ละ suite มี 30 tests และหลักฐาน
fixed สองรอบ, buggy และ coverage ที่ executed/target_checks = 30, skipped = 0 ทุก stage

[บันทึกรวม 30 suites](evidence/beam-pilot-v5-repair-handoff-20261003/index.json)
อ้างอิงชุดเดิมของ 12 bugs / 24 suites และชุดซ่อมของ 3 bugs / 6 suites โดยคง
protocol/runtime/source hashes ของแต่ละรอบ ไม่มีการเปลี่ยนผลเก่าให้เป็นผลรอบใหม่
การ review เป็น local development review แยกจาก original measured result
ไม่ใช่ primary approval, shared-contract approval หรือ Gate A

- `Cli-1`: เพิ่ม JUnit/Hamcrest dependency ใน Defects4J framework สำหรับ fixed และ buggy
  แล้วสร้าง prospective suites ใหม่ทั้งสอง approach ผ่าน fixed สองรอบ, buggy และ coverage
  เก็บ original/patched build XML และ hashes ใน
  [หลักฐาน environment](evidence/beam-pilot-v5-repair-handoff-20261003/environment-repair-cli-v5/repair.json)
- `Mockito-1`: oracle ใช้ identity ของ fixture method ที่คงที่เมื่อนำ helper ไปแพ็กใน JUnit
  สร้าง prospective suites ใหม่ทั้งสอง approach ผ่านครบ ไม่แก้ assertions ใน suites เดิม
- `Chart-1`: เก็บ coverage ของ target `AreaRenderer.getLegendItem(int,int)` เพิ่มต่างหาก
  โดยคง modified-class metric เดิม; validator ใช้ command exit, failing_tests, execution counters
  และ coverage XML เพราะ log ของคำสั่ง coverage ไม่มีบรรทัด `Failing tests: 0`
  เก็บ supplemental attempt แรกที่ validator ล้มเหลวไว้ และผูก attempt ที่ผ่านด้วย hashes
- อีก 12 bugs / 24 suites ผ่าน local fixture/oracle review ของ sampled tests
  การผ่าน suite ยังไม่รับรอง declarations ที่ยังไม่ถูกสุ่มเรียก

[ชุดซ่อมและ reviews](evidence/beam-pilot-v5-repair-review-20261003/index.json)
มี 301 files ใน [checksums](evidence/beam-pilot-v5-repair-review-20261003/checksums.json)
original measured results ยัง `usable: false` และแยก review supplement ต่างหาก
ผล sampled suites นี้ไม่ใช่การรับรอง declarations ครบ 691 รายการ

หลักฐานรอบแรกพร้อม suites, observations, stage logs/counters, coverage, failed attempts,
source snapshots และแยก review อยู่ใน
[evidence packet](evidence/beam-pilot-v5-review-20261003/index.json)
และ [checksums](evidence/beam-pilot-v5-review-20261003/checksums.json): 1,040 files

## ขอบเขต 691 declarations

ผู้ใช้ยืนยันให้รองรับ common declarations ครบทั้ง **691 รายการ** ของ 20 bugs
ห้ามใช้ capability subset 377 รายการเป็น primary scope แทน requirement นี้
มี fixed-only exclusions 3 รายการนอก common scope ตาม historical discovery เดิม

[capability proposal v5](evidence/beam-pilot-v5-capabilities-20261003/index.json)
เป็นบันทึกพัฒนาที่รองรับ 377 รายการ และยังขาด recipes/oracles อีก 314 รายการ
ข้อเสนอให้ทีมยอมรับ subset ในบันทึกนี้ถูกแทนที่ด้วย requirement ครบ 691
ยังไม่ใช้ proposal นี้ตรึง primary protocol หรือเปิด live generation

กำลังตรวจ fixed-only declaration sweep ที่เรียกทุก declaration โดยเก็บทุก setup failure,
target exception และข้อจำกัดของ oracle ไม่ปรับ target inventory จาก buggy outcomes
ต้องปิดการรองรับและหลักฐาน execution/review ครบก่อนประกาศว่าผ่าน requirement

พบ `FromXmlParser.Feature` ใน fixed source เป็น enum ที่ไม่มีค่าคงที่
targets `configure`, `disable`, `enable`, `isEnabled` จำนวน 4 รายการจึงไม่มี legal non-null enum value
ยังไม่ลด inventory ไม่สร้าง enum ปลอม และไม่รับรอง null-rejection เป็น valid oracleก่อนทีมตัดสิน
ส่วนอื่นดำเนินการตรวจต่อได้

[หลักฐาน CPU slot จริง](evidence/beam-pilot-v5-repair-handoff-20261003/pilot-fixtures-v5-1-cpu-slot/held-slot-receipt.json)
ใช้ root `/home/beam/sqa-beam/worktrees` เดียวกับงานจริง:
เมื่อ slot ถูกถือไว้ process ที่สองถูกปฏิเสธ และหลังปล่อย slot จึงรับ lock ได้
เป็นหลักฐานเครื่องบีมเครื่องเดียว ไม่แทนการตรวจรับ host ของออมหรือแชมป์

## ห้าบัค / 124 targets ที่แชมป์รับแล้ว

`Closure-176`, `Codec-1`, `Collections-1`, `Csv-1`, `JxPath-1`
มี 10 local development suites ใน packets เดิมแล้ว ไม่ใช่ห้าบัคที่ไม่มีหลักฐาน
แชมป์จัด shared preparation v5 ของห้าบัคนี้โดยใช้ fixture policy v4
ชื่อ shared preparation version และ fixture policy version เป็นคนละ field
ต้องตรวจ source/recipe/targets/prompt bindings ของ version ที่ทีมจะใช้จริงร่วมกัน
หลักฐานเดิมต้องคง runtime/protocol hashes ของตัวเอง ไม่ relabel เป็นผลรอบใหม่

## ขั้นต่อไปของบีม

1. ส่งหลักฐานซ่อม CLI, Mockito และ Chart พร้อม reviews ให้เพื่อนตรวจร่วม
2. ปิด repeated fixed observations และ semantic/fixture/oracle review ให้ครบ 691 declarations
   รวมข้อจำกัด 4 enum targets ที่ต้องมีคำตัดสินร่วม
3. ตรวจ shared preparation/recipe contract รุ่นล่าสุดของทีม และสร้าง packet/checksums
4. Commit/push ให้แชมป์กับออมดึงตรวจร่วมก่อนรวม preparation รุ่นเดียวกันและ Gate A

Gate A และ team semantic approval ยัง false; KKU quota/settings/limits/reserve เป็นงานฝั่งแชมป์
