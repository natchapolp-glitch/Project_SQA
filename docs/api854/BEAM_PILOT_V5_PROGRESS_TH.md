# บีม: หลักฐานพัฒนา 15 bugs และ requirement ครบ 691 declarations

ตรวจสถานะวันที่ 2026-10-03 จาก immutable receipts ของ
`beam-development-pilot-fixtures-v5-1` บน `beam-pc1` / CPU 1 slot
ไม่มี KKU request หรือ live queue mutation ในชุดนี้

## ชุด 15 bugs ที่แชมป์รอ

รัน FSCS-ART และ CMA-ES ครบ 30 suites แล้ว: 26 suites จบ fixed สองรอบ,
buggy และ coverage; 4 suites ยังไม่ผ่านดังรายละเอียดด้านล่าง
การ review เป็น local development review แยกจาก original measured result
ไม่ใช่ primary approval, shared-contract approval หรือ Gate A

- `Cli-1` สอง suites: JUnit runtime ขาด `org.hamcrest.SelfDescribing` ก่อนเข้า tests
  จัดเป็น environment failure ไม่ใช่ detected fault ต้องเพิ่ม dependency ในสภาพแวดล้อมแล้วรันใหม่
- `Mockito-1` สอง suites: method oracle มีชื่อคลาส `SqaProbe$FixtureMock` แต่ helper
  ที่แพ็กใน JUnit เป็น `GeneratedStudyTest$SqaProbe$FixtureMock` ต้องให้ oracle
  ใช้ identity ของ fixture ที่คงที่ แล้วสร้าง prospective suites ใหม่ ไม่แก้ assertions ใน suite เดิม
- `Chart-1` สอง suites: measurement จบครบ แต่ local review ยัง invalid เพราะ
  `AreaRenderer.getLegendItem(int,int)` ไม่อยู่ใน coverage ของ modified superclass
  ต้องเก็บ target coverage เพิ่ม และคง modified-class metric แยกไว้
- อีก 12 bugs / 24 suites ผ่าน local fixture/oracle review ของ sampled tests
  การผ่าน suite ยังไม่รับรอง declarations ที่ยังไม่ถูกสุ่มเรียก

หลักฐานพร้อม suites, observations, stage logs/counters, coverage, failed attempts,
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

## ห้าบัค / 124 targets ที่แชมป์รับแล้ว

`Closure-176`, `Codec-1`, `Collections-1`, `Csv-1`, `JxPath-1`
มี 10 local development suites ใน packets เดิมแล้ว ไม่ใช่ห้าบัคที่ไม่มีหลักฐาน
แชมป์จัด shared preparation v5 ของห้าบัคนี้โดยใช้ fixture policy v4
ชื่อ shared preparation version และ fixture policy version เป็นคนละ field
ต้องตรวจ source/recipe/targets/prompt bindings ของ version ที่ทีมจะใช้จริงร่วมกัน
หลักฐานเดิมต้องคง runtime/protocol hashes ของตัวเอง ไม่ relabel เป็นผลรอบใหม่

## ขั้นต่อไปของบีม

1. ปิด CLI dependency, Mockito packaging identity และ Chart target coverage ด้วยหลักฐานใหม่
2. ปิด semantic/fixture/oracle review ของ 15 bugs และรองรับครบ 691 declarations
3. ตรวจ shared preparation/recipe contract รุ่นล่าสุดของทีม และสร้าง packet/checksums
4. Commit/push ให้แชมป์กับออมดึงตรวจร่วมก่อนรวม preparation รุ่นเดียวกันและ Gate A

Gate A และ team semantic approval ยัง false; KKU quota/settings/limits/reserve เป็นงานฝั่งแชมป์
