# แชมป์รับ Beam 68b81b0e — หลักฐานรอบแรก 15 bugs / 30 suites

รับ replacement packet ภายหลังแล้ว: [ผลตรวจ Beam 3ae6f2fb](CHAMP_BEAM_REPAIR_INTAKE_TH.md)
ผลรอบแรกด้านล่างคงเดิมตามหลักฐาน ไม่ใช่สถานะหลังรอบแก้

รวม `beam 68b81b0ea8dd981bb31fd377e168c63aaade4475` ต่อจาก `champ 315a46fd` แบบ offline
รับโค้ด fixture policy v5, prospective probe recipes, local reviewer/exporter,
CLI framework dependency repair helper และ supplemental target coverage helper ของบีม
นี่เป็นการตรวจรับไฟล์และ integration; semantic/shared contract และ Gate A ยังไม่ผ่าน

## สิ่งที่ตรวจจากไฟล์แล้ว

ตรวจ [evidence packet](evidence/beam-pilot-v5-review-20261003/index.json)
พร้อม checksums ครบ **1,040 ไฟล์**, retained execution source pins 40 ไฟล์
และ producer/reviewer/exporter snapshots 3 ไฟล์
ตรวจ suite archive ตรงกับ generated Java, suite/protocol/result hashes,
observations, stage counts และ bindings ของ original evaluation กับ local review
แชมป์ไม่ได้รัน Defects4J experiments เหล่านี้ซ้ำ

| ผลรอบแรก | Suites | รายละเอียด |
|---|---:|---|
| Measurement ครบ fixed สองรอบ / buggy / coverage | 26 | fixed ผ่านสองรอบ; แต่ไม่ใช่ local semantic verdict ที่ valid ทุก suite |
| Local fixture/oracle review valid | 24 | 12 bugs ทั้งสอง approaches เฉพาะ sampled tests; original results ยัง usable=false |
| Local review invalid | 2 | Chart-1 ทั้งสอง approaches: coverage ยังไม่ยืนยัน `AreaRenderer.getLegendItem(int,int)` |
| Environment failed | 2 | Cli-1 ทั้งสอง approaches: ขาด `org.hamcrest.SelfDescribing` ก่อนเข้า tests |
| Fixed failed | 2 | Mockito-1 ทั้งสอง approaches: helper class identity ต่างระหว่าง probe และ packaged JUnit |

ตัวเลข measurement กับ local review เป็นการสรุปคนละมิติ ไม่บวกเป็นจำนวน suites ใหม่
มี 30 suites สำหรับ 15 bugs ตรงกับรายการที่ยังรอใน checkpoint เดิม
คงผลที่ไม่ผ่านและ original results ทุกไฟล์ไว้ ไม่เปลี่ยน unusable เป็น usable หรือ relabel เป็นผล primary
หลักฐาน local review เดิมอีกห้าบั๊กอยู่ใน packets รุ่นก่อนหน้าและคง runtime/protocol ของแต่ละรุ่น

รายละเอียด audit พร้อม hashes, per-suite outcomes และ source differences อยู่ใน
[Champ evidence audit](../../output/api854-provider-preflight-20261003/champ-beam68-evidence-audit-v1.json)

## Requirement 691 declarations

ตรวจ [capability packet](evidence/beam-pilot-v5-capabilities-20261003/index.json)
ครบ 20 artifact hashes และเทียบ selected/excluded declaration identities กับ immutable shared discovery v3
ตรงครบ **691 common declarations**: recipe proposal เลือก 377 และยัง exclude จาก capability 314
fixed-only exclusions 3 รายการอยู่นอก denominator 691 ตาม discovery เดิม

377 เป็น declared capability subset ไม่ใช่หลักฐานว่าทั้ง 377 ได้ถูกเรียกจริงหรือ semantic approved แล้ว
ข้อเสนอ subset ใน receipt เก่าไม่แทน requirement ที่ผู้ใช้ยืนยันว่าต้องรองรับครบ 691
ต้องเติม recipes/oracles และ execution/review evidence ที่ยังขาด พร้อมตรวจ shared knowledge ของทั้งสี่ approaches
ใช้ [รายการเทียบ 691 ที่แชมป์เตรียมไว้](CHAMP_BEAM_V5_PROGRESS_TH.md) เป็น expected inventory ได้

## รอบแก้ใหม่ที่บีมแจ้ง

ผู้ใช้ส่งต่อว่ารอบแก้ใหม่ CLI ผ่านทั้งสอง approaches และ Mockito ผ่าน FSCS-ART แล้ว
กำลังรันส่วนที่เหลือกับ Chart และเติม fixtures/oracles ให้ครบ 691
**Replacement suite receipts ของรอบแก้ยังไม่อยู่ใน commit 68b81b0e**
จึงบันทึกเป็นรายงานผ่านผู้ใช้ และคงผลรอบแรกข้างต้นไว้จนรับ commit/packet ใหม่

โค้ดใหม่ normalize ชื่อ helper ของ Mockito ก่อนสร้าง prospective oracle ใหม่
CLI helper เปลี่ยนเฉพาะ framework JUnit dependency ไป bundled JUnit/Hamcrest jar พร้อมเก็บ before/after/hash
Chart helper วัด supplemental target classes กับ suite เดิมและแยกจาก modified-class metrics
การมี helper ในโค้ดยังไม่ใช่หลักฐานว่ารอบแก้รันสำเร็จบนเครื่องบีมแล้ว
เมื่อรับรอบแก้ต้องตรวจ suite/source hashes, dependency provenance, stage counters และ review ใหม่แยกจากรอบแรก

## Shared preparation และงานของแชมป์

Runtime หลังรวมไม่ตรงกับ source pins ของ `champ-composed-v5.proposal.json` ห้าบั๊กเดิม 4 ไฟล์:
`SqaProbe.java`, `common.py`, `fixture_policy.py`, `prepare_worker.py`
มี `target_coverage.py` เพิ่มใน implementation hash inventory ด้วย
Retained execution packet มี source version ของตนเองก่อนการรวม Champ และก่อน prospective fixes
ทุก version/hash ที่ต่างกันเก็บใน audit; ไม่อ้างว่าผลรอบแรกได้ทดสอบ runtime ล่าสุดครบแล้ว

Shared preparation v5 ของออมใช้ fixture policy v4; Beam development fixture policy v5 เป็นคนละ field/condition
Shared validator ปัจจุบันยังไม่รับ Beam fixture policy v5 เป็น final shared recipe contract
ออมกับบีมต้องรวม final recipes ให้ครบ requirement, กำหนด input condition ใหม่ร่วมกัน และสร้าง preparation ครบ 20 bugs
ผูก protocol/runner/source/recipe/prompt hashes ของรุ่นเดียวกันก่อน Gate A/freeze/seed primary queue ใหม่
ไม่แก้ immutable proposal/preparation/receipts เก่าย้อนหลัง

แชมป์จะตรวจ API worker/runner กับ input condition ใหม่ และวัด max prompt/reserve อีกครั้ง
ค่า 177,698 bytes และ worksheet เดิมใช้กับชุดห้าบั๊กของ Aom 76b88e25 เท่านั้น
Provider limits/framing/quota bucket/reset/expiry evidence ยังไม่ครบตาม [KKU acceptance](CHAMP_V5_KKU_ACCEPTANCE_TH.md)
worker ยังปฏิเสธ protocol ที่ไม่ frozen ก่อน claim/send
ข้อจำกัดของ Gate A checker รุ่นที่รับอยู่ยังต้องแก้ตาม [waiting-work checklist](CHAMP_V5_WAITING_WORK_TH.md)

## Validation และขอบเขต

ชุดตรวจเฉพาะ fixture review/shared v4-v5/API worker/prompt ผ่าน 34 tests
Full API854 รัน 254 tests ผ่าน 253 / skip 1 เรื่อง Windows source symlink privilege
Legacy/real Java ผ่าน 30 tests รวมชุดหลักผ่าน **283 tests / skip 1**; ไม่บวกชุดเฉพาะที่ซ้ำใน full count
ตรวจ protocol development/proposal ทั้งสามไฟล์ถูกปฏิเสธ `protocol_not_frozen` ก่อน claim/HTTP
หลักฐานพร้อม log hashes/source pins อยู่ใน [validation receipt](../../output/api854-provider-preflight-20261003/champ-beam68-validation-v1.json)
ใช้ mock provider/isolated Store และ Java probe กับ JDK จริง แยกจาก Beam Defects4J experiment evidence
ยังไม่เปิด live pilot, ไม่เรียก KKU API เพิ่ม และไม่มี live queue mutation ในงานแชมป์รอบนี้

## ข้อความส่งให้ทีม

บีม: แชมป์รวม 68b81b0e และตรวจ packet/hash รอบแรกครบแล้วครับ คงทั้งผลผ่านและไม่ผ่านไว้
ส่ง commit/packet ของรอบแก้ CLI, Mockito และ Chart เมื่อพร้อม พร้อมปิด recipes/oracles/execution review ครบ 691
ยังไม่ถือว่ารอบแก้ผ่านจากข้อความสถานะเพียงอย่างเดียว และยังไม่ปิด semantic/shared contract หรือ Gate A

ออม: รับหลักฐานบีมรอบแรก 15 bugs / 30 suites แล้ว มี 24 local-valid, Chart invalid 2, CLI environment-failed 2,
Mockito fixed-failed 2; รอบแก้กำลังรอ packet ใหม่ Runtime ใหม่ทำให้ proposal ห้าบั๊กเดิมมี stale source pins
ต้องรวม final shared recipe condition ให้ครบ 691/20 bugs แล้วสร้าง preparation, วัด max prompt และส่งแชมป์คำนวณ reserveใหม่ก่อน Gate A
