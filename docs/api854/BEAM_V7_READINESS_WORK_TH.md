# บีม: ตรวจ v7 และ fixed-only diagnostic sweep

ตรวจวันที่ 3 ต.ค. 2569 บน WSL ของ aomsin; เก็บงานใน branch `codex/beam-readiness` แยกจาก checkout เดิม

## ผลที่ทำแล้ว

- รวม Aom `c25faa5e` ซึ่งมี Beam `3ae6f2fb` และตรวจ runtime/preparation v7 ที่ได้รับ
- ตรวจ source/recipe/prompt/targets partitions ครบ 20 bugs: selected 377 + unsupported 314 = 691
- ตรวจ CPU/API consumers อ่าน inputs เดียวกัน; Gate A ยังคง false ตามข้อค้างจริง
- Fixed-only sweep ทดลอง probe 691/691 declarations รายการละสองครั้งด้วย midpoint vector; ไม่ใช้ buggy outcomes เลือก scope
- 127 รายการได้ fixture_error จึงไม่ยืนยันว่าทุก declaration เข้า target สำเร็จ
- ตรวจ fixed Java source bytes ของ checkout ใหม่กับ retained v7 ก่อน observations; เก็บ setup failures และ raw observations
- ไม่เปลี่ยน capability policy, ไม่เพิ่ม usable count และไม่เปิด live API/queue

## ผลวินิจฉัยรายกลุ่ม

- `fixture_error`: 127 declarations
- `stable_normal_observation_oracle_review_needed`: 514 declarations
- `target_exception_review_needed`: 50 declarations

## ลำดับงานต่อสำหรับ 314 unsupported targets

- `fixture_error`: 126 รายการใน worklist เดิม
- `stable_normal_observation_oracle_review_needed`: 147 รายการใน worklist เดิม
- `target_exception_review_needed`: 41 รายการใน worklist เดิม

เริ่มจาก stable normal observations เพื่อ review receiver/arguments/state oracle จาก fixed source ก่อนเพิ่ม recipe
target exceptions ต้องแยก valid boundary จาก invalid preconditions; fixture_error ต้องสร้าง concrete object graph
constructor/hashCode ต้องตรวจ structural/identity oracle โดยไม่ถือว่าผลซ้ำครั้งเดียวรับรองแล้ว

## สิ่งที่ยังต้องตรวจร่วม

พบ selected target ของ v7 หนึ่งรายการที่ midpoint fixture ยังล้มเหลว:
`JxPath-1 / JDOMNodePointer.attributeIterator(QName)` ขาด projection ของ `org.jdom.Attribute`
จึงไม่ควรถือว่า selected 377 รายการผ่าน execution/oracle ครบจาก capability list

## หลักฐาน recipe ใหม่ที่ทำเพิ่ม

- Metaphone.setMaxCodeLen: prospective handwritten suite 4 tests ตรวจ getter/state และ encoding bound
  fixed สองรอบ, buggy, coverage ผ่าน; ทุก stage executed=4/skipped=0/target_checks=4
  coverage ยืนยัน setter ถูกเรียก; ไม่ตรวจพบบัคในชุดนี้
- JDOM attribute oracle: เก็บ red regression กับ helper เดิมก่อนเพิ่ม name/namespace/value projection
  ใน helper ทดลอง policy `beam-jdom-attribute-oracle-v6-development` แยกจาก runtime v7
  green ผ่าน และ fixed observations สอง vectors ให้ค่าต่างกันแต่ซ้ำได้
  suite 2 tests ผ่าน fixed สองรอบ, buggy และ target coverage;
  ทุก stage executed=2/skipped=0/target_checks=2; ไม่ตรวจพบบัคในชุดนี้
- ทั้งสองเป็น recipe-development proofs ไม่ใช่ FSCS-ART/CMA-ES/AI primary runs
  ไม่แก้ source hashes/runtime ของ v7 หรือประกาศว่า 314 รายการปิดแล้ว
- [Oracle review/recipes ต่อ](BEAM_V7_ORACLE_REVIEW_TH.md)
- [Setter packet](evidence/beam-v7-setter-recipe-20261003/receipt.json)
- [JDOM candidate packet](evidence/beam-v7-jdom-oracle-20261003/receipt.json)
- [Final verification](evidence/beam-v7-readiness-20261003/verification-v2.json)

## งานค้างหลังหลักฐานพัฒนา

- Requirement ครบ 691 declarations รวม 4 empty-enum targets ยังไม่ผ่าน
- ข้อเสนอ enum ดู [BEAM_V7_ENUM_DECISION_TH.md](BEAM_V7_ENUM_DECISION_TH.md)
- Meaningful oracle, final host acceptance, provider settings/limits/reserve/quota และ three-owner review
- รอบนี้ไม่มี new primary results; diagnostics ไม่แทน JUnit/buggy/coverage evidence

## หลักฐาน

- [Readiness receipt](evidence/beam-v7-readiness-20261003/readiness.json)
- [Gate A receipt](evidence/beam-v7-readiness-20261003/gate-a.json)
- [Validation log](evidence/beam-v7-readiness-20261003/validation.log)
- [Prioritized diagnostic worklist](evidence/beam-v7-readiness-20261003/sweep-summary.json)
- [Raw fixed sweep](evidence/beam-v7-fixed-sweep-20261003/index.json)
- [Sweep checksums](evidence/beam-v7-fixed-sweep-20261003/checksums.json)
