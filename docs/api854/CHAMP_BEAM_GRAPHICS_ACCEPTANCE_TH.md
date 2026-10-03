# Champ รับ Graphics2D joint verdict จาก Beam

ตรวจวันที่ 4 ตุลาคม 2026 (Asia/Bangkok) จาก `beam db74f11789719b9ae7bbc7b5b64416ed535d4e36`
เทียบ candidate เดิม `champ 8d9295e6c238ce6e2f9c2014932207ef2f036663`
และ shared v10 `aom a4880fb2fde574e77705841f62f302273be7dcd9`.

**รับร่วมครบ 7 bounded Graphics2D candidates แล้ว; shared integration ยังไม่เสร็จ.**
ใช้ exact default `AreaRenderer()` receiver และ inherited declarations ของ
`AbstractCategoryItemRenderer`: drawAnnotations, drawBackground, drawDomainLine,
drawDomainMarker, drawOutline, drawRangeMarker และ initialise.
Exact signatures/JVM descriptors, preconditions และ oracles ตรงกับ Champ template เดิมทุกช่อง
ใน [received joint verdict](../../output/api854-20261003/champ-beam-graphics-acceptance-v1/received/joint-verdict.json).

## ผลตรวจหลักฐาน

- Packet manifest 977 entries ผ่าน. ตรวจ nested manifests อีก 1,338 entries รวมหลักฐานเก่า v1/v2/v3
  และ native host; เป็นจำนวน checks รวมที่ซ้อนกัน ไม่ใช่ unique files เพิ่มทั้งหมด.
- Received provenance 562 entries และ snapshot source pins 1,637 entries ตรง exact Git blobs.
  สร้าง Git archive เดิมใน memory แล้วตรวจขนาด/hash ตรงด้วย.
- Native source archives มี Java 654 ไฟล์ต่อ revision: fixed SVN r2266 / buggy r2264.
  ตรวจ compiled source maps, retained owner/receiver sources, dependencies, suite และ preexecution seal.
  Candidate รันจาก source/runtime ของ Champ v9 (107 input pins / 41 runtime pins)
  ซึ่งแยกจาก helper/runtime ของ shared Aom v10 อย่างชัดเจน.
- ตรวจ raw native commands 15 รายการและ hashes/status ของ logs.
  Fixed ซ้ำ 48 observations, buggy ซ้ำ 48, JDI traces อีก 48 รวม **144 production observations**.
  Parse state, exceptions, return, dataset/receiver และ full 64×64 ARGB;
  ตรวจ independent state/filled-region oracle และ raw raster **288 ไฟล์** เทียบ Champ ตรงทั้ง 6 stages.
- JDI เข้า exact inherited declarations ครบ 7 และครบ 24 cases ต่อ revision.
  นี่เป็น method-entry evidence; ไม่มี line/branch coverage percentage หรือ full Defects4J run.
- **candidate fault=false** ตามจริง. Temporary shifted-domain-line mutation ถูกจับที่ domain_line_v
  แยกจากผล buggy ไม่เพิ่มจำนวน faults ของ Chart-1.
- รับ native host evidence ของ beam-pc1 / Linux / Java 11 / CPU 1 slot;
  held-lock challenge exit 9 และ release/reuse exit 0 ตรง receipt.
  รอบนี้ Champ ตรวจหลักฐาน ไม่มี Java execution หรือ CPU challenge ใหม่บนเครื่องบีม.
- Received focused tests 8 ผ่าน / skip 0. Champ รัน focused Graphics tests เพิ่มอีก 8 ผ่าน / skip 0.
  Inspector ปฏิเสธ negative controls 8 แบบ รวม pixel เสีย, ไม่เรียก target, descriptor ผิด,
  oracle อ่อนลง, count/integration/reserve ที่อ้างเกินจริง และ CPU lock ที่หายไป.

เก็บ audit attempts ที่ไม่ผ่านสองรอบไว้ใน `champ-beam-graphics-intake-commands-v1/v2`:
รอบแรกเป็น empty-cache preload ของคำสั่ง Champ; รอบสองอ่านคำว่า skipped ในชื่อ test เป็นผล skip.
แก้ inspector แล้วรอบ v3 ผ่าน. ทั้งสองไม่ใช่ failed Java cases ของบีม และไม่มี shared runtime mutation.

## งาน integration ของออม

Shared condition ยังเป็น `api854-20261003-joint-recipes-v10-development`, **390 selected / 301 exclusions / 691**.
ตรวจ v10 targets จริงแล้ว: Chart 8 รายการเดิมยัง selected; Graphics ทั้ง 7 ยัง excluded.
**397 / 294 เป็นเพียงจำนวนที่เสนอหากรวม Graphics 7 แล้วตรวจผ่าน**.
ไม่รวม Chronology 6 หรือ enum 4 โดยอัตโนมัติ.

ออมทำ prospective shared implementation/testing ต่อได้ตามคำสั่งงาน; receipt นี้รับ bounded candidate
และ host proof แต่ไม่ได้รับรอง shared implementation ที่ยังไม่ถูกสร้าง.
ใช้ [integration requirements](../../output/api854-20261003/champ-beam-graphics-acceptance-v1/received/integration-requirements.json)
และ [receipt](../../output/api854-20261003/champ-beam-graphics-acceptance-v1/receipt.json) เป็น contract:

1. Bind lifecycle, pixel/state projection และ exact target บน shared helper จริง.
   Setup ของ **initialise ห้ามเรียก initialise ล่วงหน้า**; ใช้ distinct real non-null plot,
   info=null และ real 2×2 dataset หรือ legal null dataset ตาม scoped oracle.
   แยก fixture_error ออกจาก target failure; legal null dataset ไม่ใช่ null-Paint/Stroke exception boundary.
2. รักษาและ regression Chart 8 รายการเดิม: findRangeBounds สอง overloads, getColumnCount,
   getItemMiddle, getLegendItem, getLegendItems, getPassCount และ getRowCount.
   Generic fixture เดิม 16×16/one category เรียก initialise ระหว่าง setup;
   isolate per-target fixture หรือระบุการเปลี่ยน Chart condition ให้ตรวจร่วมอย่างชัดเจน.
3. รักษา full image/state/return/exception oracle และ analytic reference ก่อน target invocation.
   Dispose graphics contexts; ให้ production source, fixtures, oracle knowledge/context เท่ากันทั้ง 4 approaches.
4. ตรวจ shared helper fixed สองรอบ, buggy, exact method entry, sensitivity และ consumers/host ใหม่.
   เก็บ condition ใหม่และหลักฐานเดิมแยก; ส่ง preparation/index/protocol/runner/runtime/prompts/worksheet
   ของรุ่นเดียวกันให้ Champ ตรวจ bindings และวัด reserve ใหม่.

Gate A ยังไม่เปิด; final reserve=null. Provider model/settings/limits/token framing/current quota/reset/expiry
ยัง pending. ไม่มี KKU request, live queue mutation, ledger import หรือ primary result ในรอบนี้.
หลักฐาน Beam v10 consumers/host เดิมยังรับแล้ว; ไม่ต้องรอคำรับชุดเดิมซ้ำ.

## Paths และ hashes ส่งกลับ

- [Receipt](../../output/api854-20261003/champ-beam-graphics-acceptance-v1/receipt.json)
  SHA-256 `d9c3c613819f3cad9bafb23bd88b630953f68f0de4e51cf4e7870a1b9a064deb`
- [Packet checksums](../../output/api854-20261003/champ-beam-graphics-acceptance-v1/checksums.json)
  SHA-256 `15276b1f3623a1e2a21cdbe7ed91a6e9e0add1169b9d95ada0531b80e0833203`
- [Return index](../../output/api854-20261003/champ-beam-graphics-return-index-v1.json)
- [Inspector](../../scripts/study/api854/inspect_beam_graphics_acceptance.py)
- [Champ focused test command/logs](../../output/api854-20261003/champ-beam-graphics-focused-tests-v1/tests.command.json)

ตรวจซ้ำต้องเลือก output ใหม่เสมอ:

```powershell
python -B -m scripts.study.api854.inspect_beam_graphics_acceptance --output output/api854-20261003/champ-beam-graphics-acceptance-NEW
```

## ข้อความให้ออม

แชมป์รับ beam db74f117 แล้วครับ Graphics 7 มี bounded joint verdict ครบ ภาพ/state ตรงทั้ง 6 stages,
native host proof ผ่าน และ fault=false. ใช้ CHAMP_BEAM_GRAPHICS_ACCEPTANCE_TH.md / return index
พร้อม receipt/path/SHA-256 ได้เลย ขอ integrate lifecycle/oracle บน shared helper และ regression Chart 8 เดิม;
initialise setup ห้ามเรียก target ล่วงหน้า. v10 ยัง 390/301; หลัง integration ผ่านจึงเสนอ 397/294
แล้วส่ง final pins/preparation/prompts/worksheet รุ่นใหม่ให้ตรวจ bindings และ reserve ต่อครับ.

## ข้อความให้บีม

แชมป์ตรวจรับ db74f117 ผ่านแล้วครับ exact signatures/preconditions/oracles ตรง candidate เดิม,
raw pixels/state/JDI/source bindings และ beam-pc1 CPU1slot evidence ครบ.
ไม่ต้องรอ joint Graphics verdict จากแชมป์เพิ่ม. เมื่อออมส่ง shared integration condition ใหม่
ขอรับตรวจ Chart 8 เดิม + Graphics 7 บน shared helper/inputs ทั้ง 4 approaches และ native host bindings;
แยก method-entry จาก line/branch coverage และรักษา historical fault/results ไว้ครับ.
