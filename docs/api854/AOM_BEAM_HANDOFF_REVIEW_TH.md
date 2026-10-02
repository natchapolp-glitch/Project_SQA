# ออมตรวจส่งมอบบีม 44dd5cb0 ก่อนตรึง primary protocol

ตรวจ branch beam commit `44dd5cb0b44e8f075ed283d0ff4f4ae51c97f4ed`
เทียบกับ runtime/prepare v2 ของออม `c6982003` แล้ว รับผล declaration discovery
เป็นหลักฐานประกอบการสร้าง prepare รุ่นถัดไป แต่ยังไม่รับรอง primary protocol หรือ Gate A/B.
รอบนี้ไม่ merge runtime, ไม่แก้ prepare v1/v2/core และไม่ claim/upload/เรียก KKU.

ผลตรวจด้วยเครื่องอยู่ [beam-handoff-review-44dd5cb0.json](../../output/api854-20261003/team-preparation-v2/beam-handoff-review-44dd5cb0.json).
Checklist ล่าสุด: [gate-a-beam-review-release.json](../../output/api854-20261003/team-preparation-v2/gate-a-beam-review-release.json)
แยก received 20 bugs/691 declarations จาก inventories ที่ผูกกับ v2 แล้ว 0 bugs.
ตรวจ Git blob bytes และ retained signatures; ไม่ได้รัน checkout/build/Java evaluation ใหม่.
165 tests เป็นผลที่บีมส่งมอบ ไม่ใช่ผล regression ของ composition กับ runtime ออม.

## รับได้จากหลักฐานนี้

- checksum coverage ครบตามไฟล์จริง 254 ไฟล์ และ SHA-256 ตรงทุกไฟล์.
- identity ตรง core pilot ครบ 20 bugs; modified Java hashes ตรง prepare v2 ของออมทุก bug.
- ตรวจ fixed/buggy signature sets ใหม่: eligible = intersection ทั้ง 691 declarations;
  exclusions = fixed minus buggy ทั้ง 3 รายการ ไม่ตัดตามผล test/coverage.
- prompt supplement คง v1 prefix bytes เดิมครบ 20 bugs; prompt/manifest/metadata/targets
  hashes และขนาดตรงกัน. ขนาดใหญ่สุด Math-1 115,826 UTF-8 bytes.

Exclusions ที่ยืนยัน:

1. Cli-1: `org.apache.commons.cli.CommandLine.resolveOption(java.lang.String)`
2. Gson-1: `com.google.gson.TypeInfoFactory.extractTypeForHierarchy(java.lang.reflect.Type,java.lang.reflect.TypeVariable)`
3. JacksonCore-1: `com.fasterxml.jackson.core.io.NumberInput._badBigDecimal(java.lang.String)`

ต้องคงทั้ง 3 รายการเป็น evidence แยกจาก eligible prompt inventory.
fixed-only source อาจยังอยู่ใน fixed context ได้ แต่ห้ามเลือก declaration เหล่านี้เป็น target.

## Context/prompt v2 ที่ต้องรวมก่อนใช้จริง

Chart-1 ต้องเพิ่ม fixed `source/org/jfree/chart/renderer/category/AreaRenderer.java`
เพราะ declaration discovery ใช้ concrete receiver นี้แทน abstract modified class.
ตรวจ source จาก `D4J_Chart_1_FIXED_VERSION` ใน checkout ของออมด้วย `git show ... | sha256sum`
ได้ `fb540d6b7c8faf9f9b83e26d51d9756ed2978b02e29f8f8100ce84661cd227c6`
ตรง supplement ของบีม; HEAD และ fixed tag ของออมตรง `db7dd1c2fc5626e634dc8ed88fed452d4f3fa19a`.

ต้องเก็บ `fixed_source_sha256` เฉพาะ modified Java สำหรับ evaluator target coverage
และให้ `context_source_hash` รวม receiver/build/context ทั้งหมด.
เก็บ `additional_receiver_source_sha256` และ proof ของ receiver เพิ่มแยกกัน.
Recorded Git commits ของ checkout บีม/ออมต่างกันทั้ง 20 bugs; ห้ามเขียนทับ proof เดิม
หรืออ้าง commit identity ข้ามเครื่องจาก source hashes. ใน review เก็บ proof แต่ละแหล่ง
และตรวจ exact modified bytes; Chart receiver มีการตรวจเทียบกับ fixed tag ออมเพิ่มโดยตรง.

Beam proposal ใช้ `beam-modified-and-shared-receiver-java-v2-proposal` /
`beam-eligibility-supplement-v2-proposal`. ออมยังใช้ `modified-java-and-root-build-v1` /
`shared-fixed-targets-junit4-v2`. จึงต้องสร้าง policy/prepare version ใหม่ที่ใช้ร่วมทั้งสี่วิธี
และไม่คัดลอก proposal มาประกาศว่า prepare v2 เดิมรองรับแล้ว.

ตัว builder ออมต้องการ `Project-bug/eligibility.json` พร้อม project/bug/fixed hashes,
target/fixture policy และ targets. ต้องแปลง Beam review + targets โดยรักษา provenance/exclusions
และส่งเฉพาะ declaration fields เข้า prompt ไม่แนบ discovery errors หรือ execution logs.

Callable ใหม่ `scripts.study.api854.champ_bridge:champ_suite_resolver` ต้องประกอบกับ
FrozenSettings/prepare validation และ runner plan ของออม. ปัจจุบันออมเลือก
`scripts.study.api854.suite_resolver:BeamSuiteResolver` และมี all-owner checks ของตน.
ห้ามแทน worker ทั้งไฟล์แล้วเสีย checks เหล่านั้น; ต้องทดสอบ preclaim/handoff/evaluator
ด้วย policy/source/attempt binding ชุดเดียวกัน แล้วตรึง implementation hashes ใหม่.

115,826 bytes เป็นขนาดของ proposal บีม ยังไม่ใช่ขนาดสุดท้ายของ prepare ใหม่หรือ token count.
หลังรวม policy/fixture/context ต้องวัดใหม่ และให้แชมป์ยืนยัน reserve รวม provider framing,
output 4096 และ context limit ของทั้งสองโมเดลจากหลักฐานจริง.

## หลักฐาน evaluator และสิ่งที่ยังรอ

ตรวจ hashes ของ suites, fixed-1/fixed-2/buggy/coverage logs และ fixed coverage XML
ของ Closure-176/JxPath-1 ตรง receipts. ทั้งสอง fixed ผ่านสองรอบและ buggy ผ่าน
จึง `fault_detected=false` ตามหลักฐาน. เป็น FSCS-ART offline development evidence;
ไม่ใช่ผล live pilot ครบ 20 bugs/80 jobs.

ทั้งสอง `usable=false` และ executed test count ยัง null. Closure-176 มี NPE 26 observations;
JxPath-1 มี NPE 9, ClassCastException 2 และ value:null 4. บีมต้องตรวจ receiver/arguments
และ meaningful assertion/target execution ก่อนปิด fixture/oracle review.
หากเปลี่ยน fixture/generator policy ต้องออก version และเก็บหลักฐานเดิม ไม่ซ่อม assertion
ของ suite เดิมเพื่อให้ผ่าน.

ขั้นถัดไปของออม: import eligibility → prepare รุ่นใหม่พร้อม Chart receiver → รวม policy/bridge
และทดสอบ all-owner composition → วัด prompt ใหม่และปรับ Gate A evidence → ทีมรับรองร่วมกัน.
ยังรอบีมปิด fixture/oracle review, แชมป์ส่ง settings/limits/reserve/remaining/bucket/expiry
และทีมตรวจ host assignments/primary protocol. Core queue ปัจจุบันยังเป็น preparation-only;
ไม่ใช้ core hash เดิมเปิด generation/evaluation.

Validation รอบออม: exact evidence/signature/context checks ผ่าน; gate checklist รับ receipt
ที่ hash-bound และปฏิเสธ receipt เมื่อ hash เปลี่ยน; core SHA เดิมและ draft FrozenSettings
ยัง blocked. Team runner tests 4 ผ่าน. ไม่มี Java/KKU/live queue run ในรอบตรวจนี้.
