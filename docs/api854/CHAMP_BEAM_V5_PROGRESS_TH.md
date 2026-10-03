# รับสถานะบีม v5 และเตรียมตรวจครบ 691 declarations

รับ commit/packet รอบแรกภายหลังแล้ว: [ผลตรวจ Beam 68b81b0e และสถานะรอบแก้](CHAMP_BEAM68_ACCEPTANCE_TH.md)
ข้อความและ receipt ด้านล่างเป็น checkpoint ก่อนรับชุดนั้น ไม่ใช่สถานะปัจจุบัน

วันที่ 3 ตุลาคม 2569 ผู้ใช้ส่งต่อสถานะจากบีม: รันบนเครื่องเดียว `beam-pc1` เสร็จ 27/40 suites
บีมแจ้งว่า suites ที่จบมี fixed สองรอบ, buggy และ coverage ครบแล้ว
`Cli-1` ติด environment dependency Hamcrest จึงยังไม่นับผลบั๊ก
รุ่นที่บีมกำลังทำรองรับ 377/691 declarations และกำลังขยาย fixtures/oracles ให้ครบตาม requirement
บีมแจ้งว่ายังไม่เรียก KKU API และยังไม่เปิด live pilot

ข้อความนี้เป็น **สถานะที่บีมแจ้งผ่านผู้ใช้** ยังไม่มี commit/paths ของหลักฐานรอบใหม่ให้แชมป์ตรวจ
หลัง fetch ในรอบนี้ `origin/beam` ยังเป็น `3c7c62b8` และ `origin/aom` ยังเป็น `76b88e25`
หลักฐานที่แชมป์ตรวจจากไฟล์แล้วจึงยังเป็นชุดเก่า 5 bugs / 10 local development suites
และ shared preparation v5 ของออม 5 bugs / 124 targets ตาม [ผลตรวจ v5](CHAMP_V5_KKU_ACCEPTANCE_TH.md)
ห้ามรวมตัวเลขจากคนละรุ่นให้ดูเหมือนตรวจรับชุดใหม่แล้ว

## รายการเทียบที่แชมป์เตรียมแล้ว

[Progress/inventory receipt](../../output/api854-provider-preflight-20261003/champ-beam-v5-progress-inventory-v1.json)
แยก reported progress ออกจาก verified inventory และเก็บ expected declarations ครบ 20 bugs / 691 รายการ
อ่านจาก immutable shared discovery v3 พร้อมตรวจ SHA-256 ของ index และ targets ทั้ง 20 ไฟล์
เทียบด้วย `bug` และ `class`, `constructor_types`, `method`, `parameter_types` ครบชุด เพื่อไม่สับสน overloads
รายการนี้เป็นฐานตรวจความครบถ้วน ไม่รับรองว่า v3 หรือ v5 เรียกได้จริงหรือมี oracle แล้ว

จากสถานะที่บีมแจ้ง ยังมี 13 suites ที่ไม่ได้รายงานว่าจบ และอีก 314 declarations ที่ต้องเพิ่มการรองรับ
ยังไม่มีรายชื่อ 377 declarations หรือ suites ทั้ง 27 ให้เทียบ จึงไม่แจกแจงรายการที่รองรับหรืออนุมานจำนวนบั๊กที่จบ
ตัวเลข 377/691 เป็น fixture capability ที่บีมแจ้ง ไม่ใช่ line/branch coverage หรือผลตรวจรับ semantic
การจบ 40 suites ก็ยังต้องตรวจการรองรับ declarations และ semantic/shared contract แยกกัน

## สิ่งที่ขอให้บีมส่งในรอบถัดไป

1. Commit และ index/paths/hash ของ suites พร้อม identities ของ declarations ที่รองรับและที่ยังขาด
2. Receiver/argument recipes และ meaningful deterministic oracles สำหรับทุก declaration ตาม requirement 691 รายการ
   ต้องเห็นการเรียก target จริง; ไม่ใช้เพียง discovered signature หรือการตั้ง flag supported เป็นหลักฐาน
3. หลักฐาน fixed สองรอบ, buggy, target coverage และ executed/skipped/target-check counters ของแต่ละ suite
   แยก local semantic verdict จาก original evaluation result และการลงตรวจรับร่วมทีม
4. สำหรับ `Cli-1`: error log, Java/JUnit/Hamcrest versions และ compile/runtime classpath ก่อนแก้
   พร้อม source ของ dependency และ hash หลังแก้ แล้วรัน stages ที่เกี่ยวข้องใหม่ให้ครบก่อนนับผลบั๊ก
   คงผล environment failure เดิมไว้ ไม่จัดเป็น bug outcome หรือปรับ oracle เพื่อให้ผ่าน
5. Shared source/recipe/prompt policy และ bytes/hash รุ่นเดียวกันสำหรับทั้งสี่ approaches
   หากขยาย recipes ให้สร้าง input condition รุ่นใหม่และเก็บรุ่นเดิมไว้ ไม่แก้ receipts/protocol เก่าย้อนหลัง

## งานของออมและแชมป์เมื่อรับชุดใหม่

ออมรวม recipes ที่รองรับครบ requirement เข้าชุด preparation ใหม่ของ 20 pilot bugs
ตรวจ target identities ตรงรายการ 691 และผูก protocol/runner/recipe/prompt hashes ของรุ่นเดียวกัน
ปิด semantic/shared contract และ host acceptance ร่วมทีมก่อน Gate A/freeze/seed primary queue ใหม่
ข้อจำกัดของ Gate A checker รุ่นที่รับอยู่ระบุใน [งานรอก่อนหน้า](CHAMP_V5_WAITING_WORK_TH.md)

แชมป์ตรวจ bytes/hash, worker/runner integration และวัด max prompt ใหม่เมื่อ final recipes ครบ
ค่า 177,698 bytes และ reserve worksheet เดิมใช้กับห้าบั๊กของ Aom 76b88e25 เท่านั้น
ห้ามใช้เป็น final maximum ของ 20 bugs หรือของชุดที่ขยายจาก 377 เป็น 691 declarations
ยังต้องได้ provider limits/framing/bucket/reset/expiry evidence เพื่อคำนวณ reserve และตรวจ quota

งานรอบนี้ตรวจ inventory/hash และเตรียม handoff แบบ offline เท่านั้น ไม่มี KKU request ใหม่
ไม่มี live queue mutation หรือ Defects4J experiment run ฝั่งแชมป์ และยังไม่เปิด live pilot

## ข้อความส่งให้ทีม

บีม: รับสถานะ 27/40 suites และ 377/691 declarations แล้วครับ ทำต่อบน beam-pc1 ได้ตามแผน
แก้ Hamcrest ของ Cli-1 พร้อมเก็บ dependency/classpath/error evidence และรันใหม่ก่อนนับผลบั๊ก
เมื่อ push ส่ง commit และ paths/index ของ suite/recipe/oracle/target evidence มาครับ
ฝั่งแชมป์มี expected inventory ครบ 691 พร้อม hashes สำหรับเทียบ และยังไม่ถือว่า Gate A ผ่าน

ออม: บีมกำลังขยาย fixtures/oracles จาก 377 ให้ครบ 691 declarations; สถานะ 27/40 ยังเป็นรายงานผ่านผู้ใช้
รอ commit/evidence แล้วรวม final 20-bug preparation รุ่นใหม่และวัด prompt bytes ใหม่ก่อนส่งแชมป์คำนวณ reserve
ยังไม่ใช้ max prompt ของชุดห้าบั๊กเป็น final และยังไม่ freeze primary/open live pilot ครับ
