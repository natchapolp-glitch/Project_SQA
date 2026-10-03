# ออมส่ง shared preparation v10 — รวม scoped recipes ที่ทั้งสองฝ่ายรับแล้ว

ออมรับ Beam `bcb63277a24d4cb3141139ed946de5f3f82f5bd4` และ Champ
`1a28deea84ffd346a86e79d5a5c1122266fb2237` แล้วรวม Buffer 8 + Lang 2
บน immutable Champ v9 โดยรักษา setter/getter/state, JDOM Attribute projection,
Math getField สอง signatures และ production factory knowledge.
**ชุดใหม่ครบ 20 bugs: selected 390 / exclusions 301 / denominator 691.**
เป็น prospective development condition สำหรับตรวจรุ่นเดียวกัน ไม่ใช่ primary frozen protocol.

## ชุดไฟล์ที่ทีมใช้ร่วมกัน

- [Preparation/index](../../output/api854-20261003/prepare-v10-joint-development/index.json)
- [Protocol proposal](../../output/api854-20261003/aom-continuation-v10-development/protocol.proposal.json)
- [Runner plan](../../output/api854-20261003/aom-continuation-v10-development/runner-plan.json)
- [Joint component intake](../../output/api854-20261003/aom-joint-v10-intake-v1/receipt.json)
- [Prompt reserve worksheet 40 คู่](../../output/api854-20261003/aom-v10-readiness-v1/prompt-reserve-worksheet.json)
- [Completion receipt](../../output/api854-20261003/aom-v10-readiness-v1/completion-receipt.json)
- [Final checksums](../../output/api854-20261003/aom-v10-readiness-v1/final-checksums.json)
- [Remaining worklist](../../output/api854-20261003/aom-v10-readiness-v1/remaining-worklist.json)
- [Gate A input checklist](../../output/api854-20261003/aom-v10-readiness-v1/gate-a-input-checklist.json)

SHA-256 ที่ส่งให้เพื่อนต้องตรวจจาก bytes จริงหลัง checkout:

- `output/api854-20261003/prepare-v10-joint-development/index.json` — SHA-256 `4f8e942f8c1b0bf9eb5d8404b49ca3de2c4eb46c2f4b4955dc46347b760509b6`
- `output/api854-20261003/aom-continuation-v10-development/protocol.proposal.json` — SHA-256 `eb047d1b52ae3950e476948b4fb0de9c0ba2afbb3dad9efdfc5fef5b26b81ffe`
- `output/api854-20261003/aom-continuation-v10-development/runner-plan.json` — SHA-256 `462f6196d81dfc9ed1bda31ce0ae81e59759341b6671cb7fc9d9301fe453e048`
- `output/api854-20261003/aom-v10-readiness-v1/prompt-reserve-worksheet.json` — SHA-256 `f6d232d4d1e9d815434928ac357a0fffa6dad60be2923fcc981440983e1b017d`
- `output/api854-20261003/aom-joint-v10-intake-v1/receipt.json` — SHA-256 `0ba6413f1a0a0f84dc824aaa21f2da67f835f5a0dff391b5aab870d28eb87034`

## Condition และการรักษาประวัติ

- Condition: `api854-20261003-joint-recipes-v10-development`
- Prepare contract: `aom-beam-prepare-v10-development`
- Fixture: `aom-beam-champ-joint-fixtures-v10-development`
- Current runtime 41 pins ตรง protocol/index/worksheet; historical experiment runtime ยังแยกตาม receipt เดิม.
- Fixed source inventories ทั้ง 20 bugs ตรง v9 ทุกไฟล์; factory supplements ไม่เพิ่ม evaluator modified targets.
- เพิ่มจาก v9 เฉพาะ Buffer 8 และ Lang 2. Setter/JDOM/Math ไม่บวกซ้ำ; JDOM เป็น recipe repair.
- Exclusions และ reasons เดิมคงไว้ ตัดเฉพาะ 10 additions ที่รับ. Enum 4 และ Chronology 6 ไม่ adopt.
- Csv stream เปลี่ยนอย่างเปิดเผย: vector[0]<0 ใช้ `A\nBC\nDE`, นอกนั้น `12\n345\n`, fresh reader ต่อ case;
  กระทบ five existing reader methods ด้วย. All approaches ใช้ knowledge ใหม่เท่ากัน.
- Lang เฉพาะ private static helpers: bounded null/String/int[]; Boolean จริง,
  IllegalArgumentException class/message และ input state; ไม่ครอบคลุม public callers/all array types.
- ไม่เปลี่ยน bytes ของ historical v7/v8/protocol/suites/results. เก็บ receive producer เดิม
  พร้อม hash และเพิ่ม current validator ที่ตรวจ project/bug กับ nested evidence ชัดขึ้น.

## ผลตรวจของออม

- ตรวจ immutable return packets และ per-signature source/reference/runtime bindings.
- Tests ใหม่ตรวจ 80 bug × approach combinations: CPU loaders และ API prompt resolver ได้ sources,
  recipes, context, targets, prompt hash เดียวกัน. ใช้ offline settings เพื่อทดสอบ resolver;
  FrozenSettings ยัง reject development contract แม้ปลอม approval เป็น frozen.
- Regression **61 tests ผ่าน ไม่มี skip**: API worker, shared preparation, legacy v8, fixture/prompt,
  Gate A mutation guards และ Java probe. Post-review targeted **7 tests ผ่าน** รวม negative scopes/evidence
  และ actual model IDs. สองรอบมี tests ซ้ำ จึงไม่อ้างว่ารวมเป็น 68 unique tests.
- [Fixed-runtime integration proof](../../output/api854-20261003/aom-v10-readiness-v1/fixed-runtime-verification.json):
  **64 cases / 128 repeated fixed observations** — setter/JDOM/Math 10, Buffer/Csv 42, Lang 12.
  Compile และ invoke production fixed Java จริงจาก local Defects4J mirrors;
  old policy behavior คงเดิม และ temporary incorrect setter ถูก oracle ตรวจพบ.
  นี่ไม่ใช่การรัน Defects4J fixed/buggy/coverage suites ใหม่ และไม่ใช่ผล primary algorithms.
- Independent code review ตรวจแล้ว ไม่มี critical/important findings ค้าง.
- Historical CMA-ES String append entry hits=0 / FSCS-ART=2 คงเดิม;
  reference/integration proof ไม่แก้ coverage หรือ fault ของ algorithm results เดิม.

## Prompt/reserve และ runners

Max prompt UTF-8 **265,937 bytes**; worksheet ครบ 20 bugs × requested models 2 = 40 คู่:
`claude-sonnet-5`, `gemini-3.5-flash-lite`. Requested temperature=0 / output cap=4096.
Numerical guard floor แบบเดิม **270,033 + unknown framing**; bytes ไม่ใช่ provider tokens,
และเลขนี้ไม่ใช่ final token reserve. `final_reserve`/`prompt_token_reserve` คง null.

Runner proposal รับ bytes จาก v9: Champ API coordinator ของทุก owner,
Beam CPU one slot ของ Beam, Aom CPU one slot ของ Aom/Champ พร้อม evaluation ทั้งสี่ approaches.
Route coverage **10,248 stage keys** ครบทุก 854 bugs; ไม่ใช่หลักฐานว่า hosts/quotas ปัจจุบันพร้อม.
ยังต้องผูก host/environment/CPU-lock acceptance กับ condition v10 นี้.

## ส่งต่อให้บีม

ดึง aom แล้วตรวจ condition v10 โดยใช้ exact index/protocol/runner/worksheet hashes ด้านบน.
ตรวจ consumers ทั้งสี่, signatures/domains/oracles และ actual helper/dependency/host bindings
โดยรักษา setter/JDOM/Math/factories. ส่ง verdict/receipt ที่ระบุ condition/hash/commit/path
และสิ่งที่ตรวจจริง; ไม่ใช้ historical local review แทน final condition acceptance.
หากสร้าง development suites ใหม่ ให้ seal ก่อน execute และรายงาน fixed สองรอบ, buggy,
coverage, executed/skipped/target_checks, semantic review กับ fault ตามจริง.
เก็บ old suites/results และ CMA-ES String append gap ไว้. Codec/Chronology/enum ทำ packet แยก;
ไม่เพิ่มรายการใหม่เข้า v10 โดยไม่มี joint verdict และ condition ใหม่.

## ส่งต่อให้แชมป์

ดึง aom และตรวจ preparation/prompts/40-pair worksheet ของ v10 รุ่นเดียวกัน.
วัด final reserve รวม system/messages/framing overhead และ output 4096; ยืนยัน exact model IDs,
รองรับ temperature 0, context/output limits พร้อม evidence ของ effective settings.
ยืนยัน current quota remaining/bucket/reset/expiry ของ coordinator และ ledger readiness
ก่อนคำขอ generation ที่ทีมอนุมัติ. เคารพงานรับ accounts/keys ที่พักไว้และ authorization ของผู้ใช้.
ส่ง receipt ผูก condition/protocol/index/prompt/runner/runtime hashes; ไม่มี fallback ไป condition อื่น.
ร่วมตรวจ runner/host และ Gate A; บัญชีเดียวต้องไม่ถูกหลาย host ส่งซ้ำ และแจ้งก่อนสลับ account.

## สิ่งที่ยังไม่ผ่าน

Gate A=false, enabled_stages=[], generation_ready=false, team/primary approval=false.
ยังค้าง final condition semantic/host acceptance, provider/settings/limits/framing/current quota/reserve,
301 exclusions และ three-owner review. Checker ยังคง all_common_declarations blocked ที่ 390/691;
ไม่รับรอง semantic coverage ครบ 390 เพียงเพราะ structural selection/receipts ผ่าน.
**ไม่มี KKU request, live queue mutation หรือ primary result เพิ่มในงานออมนี้.**
