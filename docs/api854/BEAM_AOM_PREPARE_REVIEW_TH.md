# บันทึกส่งมอบบีม: ตรวจ prepare ของออม 2026-10-03

ตรวจเฉพาะส่วนบีมตาม [AOM_PREPARE_HANDOFF_TH.md](AOM_PREPARE_HANDOFF_TH.md) จาก branch `aom` commit `8fcec539` แล้ว ส่งชุดนี้ให้ออมตรวจรับได้ แต่ยังไม่รับรอง Gate A หรือเปิด live pilot: fixture/oracle validity ยัง pending และยังไม่ได้ประเมิน prospective suites ครบ 20 bugs/80 jobs

## Artifacts และ eligibility

- ตรวจ Git artifact bytes, checksum, manifest, prompt, metadata และ fixed revision proof ครบ 20 bugs / 17 projects ผ่านทั้งหมด โดยไม่แก้ prepare-v1
- checkout/build/discovery บน Defects4J 3.0.1 จริงครบ 20 bugs เปรียบเทียบ fixed/buggy declaration signatures และ compiled fixture classes รวมทั้งตรวจ fixed Java bytes ตรงกับ artifacts ของออม
- มี eligible declarations ร่วมกัน 691 รายการ ตัด fixed-only 3 รายการใน Cli-1, Gson-1 และ JacksonCore-1 รายละเอียด signature อยู่ใน [review/](evidence/beam-aom-review-20261003/review/) และ [declarations/](evidence/beam-aom-review-20261003/declarations/)
- Eligible หมายถึง declarations ใช้ร่วมกันได้ ยังไม่ยืนยันว่า reflection สร้าง meaningful receiver/arguments ได้ทุก target การใช้ null/empty collection หรือ exception ต้องตรวจรายกรณี

สรุปที่ตรวจด้วยเครื่อง: [index.json](evidence/beam-aom-review-20261003/index.json) และ [checksums.json](evidence/beam-aom-review-20261003/checksums.json) ครอบคลุมหลักฐาน 254 ไฟล์

## suite_resolver และ adapters/evaluator

Callable สำหรับ APIWorker คือ `scripts.study.api854.champ_bridge:champ_suite_resolver` ตาม [BEAM_CHAMP_RESOLVER_TH.md](BEAM_CHAMP_RESOLVER_TH.md) ส่ง `suite.tar.bz2` และ generation-lineage เป็น artifacts แยกกัน ตรวจ protocol/source/attempt/hash ก่อน evaluation รายละเอียดแพ็ก suite อยู่ใน [SUITE_HANDOFF_TH.md](SUITE_HANDOFF_TH.md)

`adapters.py` ตรวจ fixed/buggy build และ baseline ของ dataset; `evaluate_worker.py` เรียก evaluator จริงเพื่อรัน generated suite บน fixed สองรอบและ buggy แล้ววัด fixed target-class coverage ผล dataset baseline แยกจาก prospective suites ด้านล่าง

ชุด FSCS-ART ใหม่ seed 101, budget 30 ต่อ bug มีหลักฐานรันจริง:

| Bug | Fixed รอบ 1/2 | Buggy | Fixed coverage lines / branches | Buggy coverage lines / branches |
| --- | --- | --- | --- | --- |
| Closure-176 | ผ่านทั้งสองรอบ | ผ่าน; ไม่พบ fault | 10/739 · 3/489 | 10/736 · 3/485 |
| JxPath-1 | ผ่านทั้งสองรอบ | ผ่าน; ไม่พบ fault | 75/773 · 22/562 | 75/773 · 22/556 |

[evaluator/index.json](evidence/beam-aom-review-20261003/evaluator/index.json) แนบ suite bytes, observations, fixed/buggy logs และ coverage XML พร้อม hashes Buggy coverage เป็น supplementary audit ที่ใช้ suite hash เดียวกัน แยกจาก measurement ของ evaluator เดิม จำนวน 30 คือ declared test methods; ไม่อ้างเป็น executed test count เพราะ log ไม่รายงานค่านั้น

ทั้งสองผล `usable=false`: Closure มี fixed observations เป็น NullPointerException 26 กรณี; JxPath มี NullPointerException 9 กรณี, ClassCastException 2 กรณีและ value:null 4 กรณี ต้องตรวจ receiver/arguments และ assertion ว่าเข้าถึง target อย่างมีความหมายก่อนรับรอง ใน index มี covered methods ให้ตรวจด้วย การที่ buggy ผ่านรายงาน `fault_detected=false` ตามจริง

หลักฐานเดิม Lang-4 ครบสี่ approaches อยู่ใน [beam-local-queue-smoke-20261003.json](evidence/beam-local-queue-smoke-20261003.json) และ APIWorker สอง AI approaches ใน [beam-champ-apiworker-smoke-20261003.json](evidence/beam-champ-apiworker-smoke-20261003.json) การตอบ AI เป็น mock; execution ของ Defects4J เป็นของจริง

## Supplement ใหม่ให้ออมตรวจรับ

prepare-v1 prompt ยังไม่มี explicit eligible method inventory และ Chart-1 ใช้ concrete receiver `org.jfree.chart.renderer.category.AreaRenderer` ซึ่ง source ไม่อยู่ใน context ที่มีเฉพาะ modified Java จึงส่ง [supplement-v2-proposal/](evidence/beam-aom-review-20261003/supplement-v2-proposal/) เป็น bytes ชุดใหม่ รักษา v1 prompt prefix เพิ่ม eligible declarations/ข้อกำหนด meaningful fixture และ fixed AreaRenderer source ที่ตรวจ revision แล้ว

- Context policy: `beam-modified-and-shared-receiver-java-v2-proposal`
- Prompt policy: `beam-eligibility-supplement-v2-proposal`
- Metadata คง `fixed_source_sha256` เฉพาะ modified Java สำหรับ lineage; `context_source_hash` รวม supplementary receiver และ `additional_receiver_source_sha256` ระบุ hashes แยก
- bridge รองรับ receiver supplement เฉพาะ policy และ hash mapping นี้ มี unit test ตรวจ handoff/evaluator binding
- Prompt ใหญ่สุดใหม่ Math-1 **115,826 UTF-8 bytes** (เดิมประมาณ 99 KB) เป็นขนาด bytes ยังไม่ใช่ token count หรือ provider overhead ที่ยืนยันแล้ว ส่งให้ออมประกอบ freeze; การยืนยัน settings/โควตาอยู่นอกการตรวจส่วนบีมครั้งนี้

ออมต้องตรวจและรับ adoption/import contract ของ supplement ก่อนตรึง primary protocol และสร้าง run ใหม่ `prepare_worker` ยังใช้ exporter เดิม ไม่ได้อ้างว่า prepare-v1 หรือ protocol.core-frozen รองรับ v2 แล้ว ห้ามใช้ source hashes ของ proposal เก่ามารับรอง bridge รุ่นใหม่โดยไม่ตรึงใหม่

## งานบีมที่ต้องปิดก่อน live pilot

1. ตรวจ meaningful receiver/arguments และ oracle ราย bug โดยเริ่มจาก Closure/JxPath ที่มี exception/null สูง ปรับ fixture policy เป็น version ใหม่ก่อน freeze หากจำเป็น
2. ให้ออมรับ eligibility/exclusions, Chart receiver context และ input import ของ v2 แล้วบันทึก primary protocol/source hashes ที่ทีมตกลงกัน
3. ตรวจ composition ของคิว + resolver + evaluator ตาม protocol เดียวกัน และรัน prospective suites/semantic review ครบ pilot ก่อนนับเป็น Gate A/B ผ่าน

API854 tests ล่าสุดผ่าน 165 tests ไม่มี skip; legacy evaluator 16 และ generator 4 ผ่านในรอบก่อนหน้า ชุดส่งมอบนี้ **0 real KKU requests และ 0 live queue mutations** ยังไม่ต้องใช้ API key เพื่อทำการตรวจข้างต้น

ตัวตรวจซ้ำ: `python3 -m scripts.study.api854.review_prepare --help` และ `python3 -m scripts.study.api854.prepare_review_supplement --help` ต้องใช้ checkout/build evidence ของ Defects4J และ output directory ใหม่ เก็บ artifact ต้นฉบับของออมตาม Git revision ที่ระบุ
