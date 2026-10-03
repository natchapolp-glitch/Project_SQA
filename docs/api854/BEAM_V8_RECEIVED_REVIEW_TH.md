# บีมตรวจรับงานออม v8 บนเครื่องบีม

รับงาน `origin/aom ff02b94f2d650e0f44b2fe404e39d96fe76c60fc` ตาม
[TASK_TO_BEAM_V8_TH.md](TASK_TO_BEAM_V8_TH.md) โดยรวมใน isolated worktree ของบีม
ที่ merge commit `cc2c7f2563e2d11db26a4c9fd34a05bf3e701f5f`.
เก็บงาน Buffer/Lang และหลักฐานเก่าทั้งหมดไว้ ไม่มี KKU request, queue mutation
หรือ primary result เพิ่ม และยังไม่อนุมัติ Gate A/pilot.

ผลรับงานจริงอยู่ใน [beam-v8-review.json](../../output/api854-20261003/beam-v8-received-v1/beam-v8-review.json).
`reviewer_commit` คือ commit ของ runtime/inputs ที่ตรวจ ไม่ใช่ self-referential hash ของ commit
ที่นำ receipt ไปส่ง; hash ของไฟล์หลักฐานอยู่ใน checksums และ Git commit ที่ส่งกลับทีม.

## Runtime และ preparation

Historical task manifest มี runtime 41 pins; runtime บีมปัจจุบันต่าง 5 ไฟล์
คือ helper/fixture policy และ shared-input support ที่มีงาน Buffer/Lang เพิ่ม.
ตรวจ hash ของ protocol/index/runner/worksheet ที่ออมส่งตรงทุกไฟล์ก่อนสร้าง preparation ใหม่.
รายละเอียด before/after อยู่ใน [runtime-diff.json](../../output/api854-20261003/beam-v8-received-v1/runtime-diff.json).
ไม่แก้ source pins หรือผลตรวจของ v8 เดิม.

สร้าง preparation ใหม่ครบ 20 bugs ใน
[preparation/index.json](../../output/api854-20261003/beam-v8-received-v1/preparation/index.json)
พร้อม [protocol](../../output/api854-20261003/beam-v8-received-v1/composition/protocol.proposal.json),
[runner](../../output/api854-20261003/beam-v8-received-v1/composition/runner-plan.json)
และ [audit](../../output/api854-20261003/beam-v8-received-v1/composition-audit/gate-a-checklist.json).
ชุดนี้ใช้ Math-only fixture profile เดิมของ v8: **379 selected / 312 exclusions / denominator 691**.
Buffer/Lang cumulative profile ของบีมมี 389 selected; Champ/Aom integrated v9 มี 380 selected.
ตัวเลขเหล่านี้เป็นคนละ condition และห้ามนำผล/hash/reserve มาใช้แทนกัน.

ทดสอบ preparation ใหม่จริงผ่าน 4 tests ไม่มี skip ครอบคลุม 20 bugs × 4 consumers,
source/recipe/context mapping ที่เท่ากัน และ rejection เมื่อขาด factory knowledge.
ดู [four-consumer-validation.json](../../output/api854-20261003/beam-v8-received-v1/four-consumer-validation.json).
Focused tests ต้นฉบับผ่าน 7 และ skip 1 เพราะอ้าง Math classes ใน home ของเครื่องเดิม;
ผลจริงบนเครื่องบีมที่ระบุด้านล่างใช้แทนการอ้างว่า skipped test ผ่าน.

## Math และเครื่องบีม

รับเฉพาะ BigFraction/Fraction, `constructor_types=double`, `method=getField`, parameters ว่าง.
receiver เป็น production fraction จาก finite quarter-valued input;
ตัวอย่างอ้างอิงล่วงหน้าคือ 7/4 และ -3/4 ของทั้งสอง classes.
Oracle เปรียบเทียบ runtime field class, zero=0/1, one=1/1 และ receiver numerator/denominator.
Production field factories สองไฟล์มี source hashes แยกจาก modified-source coverage mapping.

รัน Linux/WSL Ubuntu user `team`, Java/Javac 11, Defects4J 3.0.1 จริงบน `beam-pc1`:

- FSCS-ART และ CMA-ES อย่างละ 30 tests: unchanged suites วัด fixed สองรอบ/buggy/coverage
  ทุก stage `executed=30/skipped=0/target_checks=30` และ `fault_detected=false`.
- Independent reference 4 tests / 8 fixed observations: unchanged suite วัดทั้ง 4 stages
  `executed=4/skipped=0/target_checks=4` พร้อม target getField coverage ทั้งสอง classes.
- Source และ selected signatures ของ actual checkouts ตรง preparation v8 ใหม่;
  local development runs ไม่ใช่ live queue/shared primary experiments.
- CPU lock ตรวจสอง processes บน root เดียวกับที่ใช้รันจริง
  `/home/team/sqa-round2/beam-buffer-worktrees`: process ที่สองถูกปฏิเสธ exit 9
  ขณะถือ lock และใช้ slot ได้ exit 0 หลัง release. หนึ่ง CPU slot เท่านั้น.

หลักฐาน: [Math development checksums](evidence/beam-v8-math-development-20261003-v1/checksums.json),
[reference receipt](../../output/api854-20261003/beam-v8-received-v1/math-reference/receipt.json),
[environment](../../output/api854-20261003/beam-v8-received-v1/environment/environment.json),
[host readiness](../../output/api854-20261003/beam-v8-received-v1/host-readiness.json).
แชมป์ยังเป็นผู้ส่ง API ของทุก owners; live queue transport ไม่ได้ทดลองในรอบนี้.

## คำตัดสิน setter/JDOM ของบีม

ตรวจ 103 checksum entries ของ packets เดิม (setter 41, JDOM 62) พร้อม unchanged suite/result hashes,
fixed สองรอบ/buggy/target coverage และ counters จริง.
สำเนา peer receipts จาก exact Champ commit `e742095dcddf795b4d575a611b5f844da9977388`
มี [provenance](../../output/api854-20261003/beam-v8-received-v1/peer-evidence/provenance.json).
ไม่รัน Defects4J ของ setter/JDOM ใหม่และไม่ execute peer scripts.

| Candidate | Exact signature / change | Preconditions / oracle ที่บีมรับในขอบเขต development |
|---|---|---|
| Codec-1 | `Metaphone()` → `setMaxCodeLen(int)`; `add_unsupported_signature` | receiver ใหม่, limit ไม่ติดลบ (proof 0/1/4/8), string `architecture`; getter ต้องเท่ากับ limit, encode length ไม่เกิน limit และ getter state คงเดิม; shared recipe เปรียบเทียบ encoded output จริงด้วย |
| JxPath-1 | `JDOMNodePointer(Object, Locale)` → `attributeIterator(QName)`; `repair_existing_selected_recipe` | production JDOM attached root/item และ coherent QName; projected Attribute name/namespace/value, iterator order และ XML state; `id` namespace ว่างกับ left/right values ต้องต่างกัน |

Setup/constructor/dependency/projection failure เป็น fixture_error ไม่ถือเป็น target exception
หรือ skipped-success. Oracle ข้างต้นเป็น bounded projection; ไม่ใช่ full domain/object equivalence.
ไม่มีการเลือก target จาก buggy outcomes. JDOM เป็น recipe repair จึงไม่ปิด unsupported เพิ่ม.

บีมรับ **bounded development candidate recipe review** ของสองรายการนี้.
Receipt ของ Champ ที่อ่านรับ integrity ของ historical development evidence และระบุชัดว่า
ยังไม่ final shared approval; Aom ใน newer v9 รับเข้า composition แล้ว แต่ไม่แทน verdict ส่วนตัวของ Champ.
จึงกรอก `champ_verdict=null` และ `accepted_candidates=[]` ไว้ตามจริง:
[joint-candidate-review.json](../../output/api854-20261003/beam-v8-received-v1/joint-candidate-review.json).
ขอ Champ ส่ง scoped verdict ของ exact signatures/preconditions/oracles นี้
ก่อนให้ออมประกอบและตรวจ final combined condition.

Math-only v8 นี้ยังมี known fixture failure ของ JDOM `attributeIterator` เพราะไม่ได้รับ Attribute repair
เข้าชุดนี้; final condition semantic review จึง pending. งาน selected targets และ exclusions อื่น
ยังต้อง review ไม่ถือว่า preparation ผ่านแล้วเท่ากับ oracle ถูกทั้งหมด.

## Empty enum และงานที่ยังต้องร่วมทีม

ตรวจ Champ enum-boundary v3 packet 29 checksum entries และ production source binding.
Mirror archive เป็น CRLF ต่างจาก retained source แบบ LF; verifier เปรียบเทียบเนื้อหา normalized
แล้ว compile exact retained bytes ที่ pin ไว้. ตรวจทั้ง archive raw hash และ retained raw hashแยกกัน.
หลักฐาน peer นี้ใช้ Windows/Java17; ไม่ใช่ Defects4J readiness ของเครื่องบีมและไม่ได้ rerun.

ข้อเสนอของบีม: **คง 4 signatures เป็น exclusions และคง denominator 691**;
รายงาน null-input target-entry/state diagnostics แยกจาก normal-domain coverage.
ใน fixed revision นี้ไม่มี legal non-null enum member. ต้องให้ออม/บีม/แชมป์ตกลง requirement
และ boundary oracle ร่วมกันก่อนใช้ผล ไม่ปิดยอดด้วย NPE หรือสร้าง enum ปลอม.
[enum-review.json](../../output/api854-20261003/beam-v8-received-v1/enum-review.json)
คง joint/normal-domain/null-boundary verdict เป็น null.

Max prompt ของ preparation นี้ **264,899 UTF-8 bytes**; เมื่อ output cap 4,096
conservative request floor คือ **268,995 + framing overhead**.
Overhead/limits/quota/reset/expiry และ final reserve ของ condition นี้ยัง pending.
ห้ามนำ floor ของ newer v9 หรือโควตาที่สังเกตในอดีตมาอนุมัติคำขอของชุดนี้.

ส่งออมและแชมป์ให้อ่าน receipt นี้ พร้อมเลือกรุ่น final ที่จะรวมแล้วเตรียมครบ 20 bugs,
วัด prompts/ตรึง runtime และตรวจ semantic/hosts/reserve ร่วมกันอีกครั้ง.
งานครั้งนี้ไม่เปิด live pilot และไม่รับรองว่าทดสอบครบ 854 bugs หรือ 691 declarations.
