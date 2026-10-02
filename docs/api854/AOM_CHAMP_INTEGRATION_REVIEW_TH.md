# ออมตรวจรับ integration ของแชมป์ d134cde6

รวม `champ d134cde6` เข้าออมต่อจาก `32b8378f` โดยไม่มี Git conflict.
Runtime รวม Beam callable resolver `ce17ce49` กับ shared prepare v2 และ all-owner runner guards.
รอบนี้ยังไม่รวม context/receiver supplement ของ `beam 44dd5cb0` เข้า primary preparation.

## จุดที่ตรวจรับ

- implementation SHA-256 ตรง Champ receipt ทั้ง 38 ไฟล์.
- preclaim ตรวจ shared prompt/manifest/policy/target hashes และปฏิเสธ targets ว่างก่อน claim/provider.
- `champ_suite_resolver` สร้าง hash-bound handoff จาก original protocol bytes และ prepare attempt;
  suite/lineage ส่งถึง evaluator ได้และเก็บ generation/evaluation attempt แยกกัน.
- TeamQueueClient ยังใช้ host/role/owner/stage/approach guards; ไม่มีการเรียก base claim ข้าม plan guards.
- การรับ owner อื่นโดยไม่ใช้ plan ต้องมี owners/worker routing ใน frozen protocol;
  ยังไม่ถือว่ารายชื่อใน draft เป็นการอนุมัติ live runner.
- source/prompt/suite policy และ implementation hashes ใน primary draft อัปเดตให้เลือก bridge นี้;
  primary approval, quota reserve, host acceptance และ three-owner reviews ยังค้าง.

หลักฐานออม: [champ-integration-acceptance-d134cde6.json](../../output/api854-20261003/team-preparation-v2/champ-integration-acceptance-d134cde6.json).
Tests ใช้ isolated local Store และ mock provider ไม่ใช่ผลจาก KKU.
ออมตรวจซ้ำ API854: WSL 199 ผ่านไม่มี skip; Windows 198 ผ่าน/skip symlink 1 จาก 199.
Legacy evaluator 16 และ generator 4 ผ่านบน Windows รวม Windows ผ่าน 218 tests.
Core SHA เดิม, implementation pins ตรง receipt และ draft loader ยังคง `protocol_not_frozen`.
Checklist ใหม่: [gate-a-after-champ-d134cde6.json](../../output/api854-20261003/team-preparation-v2/gate-a-after-champ-d134cde6.json).
ไม่ claim/upload/complete บน live queue และไม่ใช้ generation API ในรอบนี้.

## แก้ความเข้าใจเรื่อง targets ที่ยังรอ

แชมป์รวมบีมถึง `ce17ce49` เท่านั้น. บีมส่ง source-matched discovery เพิ่มใน `44dd5cb0` แล้ว:
20 bugs / 691 eligible declarations / 3 fixed-only exclusions.
ออมตรวจรับหลักฐานนี้ใน [AOM_BEAM_HANDOFF_REVIEW_TH.md](AOM_BEAM_HANDOFF_REVIEW_TH.md).
สิ่งที่ค้างฝั่งออมคือ import inventory และสร้าง preparation version ใหม่ ไม่ใช่ขอ discovery ชุดเดิมซ้ำ.
Discovery ยังไม่รับรอง meaningful fixture/oracle validity.

Bridge ของ `d134cde6` ต้องการ fixed Java mapping เท่ากับ Java mapping ทั้ง manifest.
จึงยังไม่รองรับ Chart-1 ที่เพิ่ม AreaRenderer source ใน context แต่เก็บ evaluator mapping
เฉพาะ modified AbstractCategoryItemRenderer. ต้องรวม receiver policy + split mapping
จาก proposal ใหม่ให้ prepare validation/API bridge/evaluator สอดคล้องกันก่อน freeze.

ขนาด 99,439 bytes ในเอกสารแชมป์เป็น prepare v2 ที่ยังไม่มี declarations.
proposal ของบีมมีขนาดใหญ่สุด 115,826 bytes; หลังสร้าง prepare รุ่นถัดไปต้องวัดขนาดจริงอีกครั้ง
แล้วให้แชมป์ยืนยัน context/output limits และ reserve รวม provider framing.
ไม่ใช้ตัวเลข bytes แทน tokenizer/provider acceptance evidence.

## งานที่เหลือ

1. ออม: import discovery, รวม target/fixture/context/prompt policy และ receiver mapping
   ใน prepare version ใหม่; ตรวจ composition และส่ง artifacts/hashes ใหม่ให้แชมป์/บีม.
2. บีม: ปิด meaningful fixtures/oracles และ target execution โดยเฉพาะ Closure-176/JxPath-1
   ที่มี exception/null สูง; ผลปัจจุบันยัง usable=false.
3. แชมป์: observed remaining/bucket/expiry, settings ที่รับจริง, context/output limits และ framing/reserve.
4. ทีม: ตรวจ host assignments และ Gate A ทั้งสามคน ก่อน frozen primary bytes/hash และ run ใหม่.

Frozen-core bytes เดิมและ prepare v1/v2 ไม่ถูกแก้. การรวม runtime ไม่เปิด live pilot
และไม่เปลี่ยนผล offline/mock ให้เป็น primary observations.
