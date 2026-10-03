# แชมป์ตรวจ shared inputs และ limits/reserve รุ่น v8

รับตรวจ preparation หลังรวม recipe จาก Beam `532baa31` แล้วแบบ offline: ครบ 20 pilot bugs,
เลือก 378 declarations และคง 313 exclusions รวม denominator 691 เท่าเดิม
การเพิ่ม Metaphone setter หนึ่ง signature และแก้ JDOM oracle ไม่ถือว่าปิดงาน 314 declarations เดิม
หรือรับรอง meaningful oracle ครบ 691; ชุดนี้เป็น development proposal และ Gate A ยังไม่ผ่าน

หลักฐาน: [shared-input/reserve audit](../../output/api854-provider-preflight-20261003/champ-v8-shared-limits-audit-v1.json)
และ [ผลทดสอบ 5 tests](../../output/api854-provider-preflight-20261003/champ-v8-validation-v1.json)

ตรวจ checksum 486 entries ของ v3 ต้นทางและ v8, fixed source/revision lineage,
partition ของทุก declaration และ predeclared capability policy ตรงกัน
Recipe source bytes ผูกกับ runtime SHA-256 ปัจจุบันครบ 41 files
Production CPU shared-input loader และ API consumers ของทั้งสอง approaches อ่าน artifact ชุดเดียวกันครบ 20 bugs
โดยใช้ file transport และ isolated settings; แชมป์ไม่ได้รัน Defects4J generation/evaluation ใหม่
และไม่มี KKU request, live queue mutation หรือการแก้ quota ledger ในรอบตรวจนี้

คู่ protocol/runner รุ่นที่ตรวจ:

| Artifact | SHA-256 |
|---|---|
| v8 preparation index | `1ed61e8ea727ceb8aa9746a96a6567077f6e70580f4b4792f1035b149b079508` |
| v8 protocol proposal | `877628a97d088cec61a8522ee120bf6dc9ca69276459523172c7fe2d76f18561` |
| v8 runner plan | `462f6196d81dfc9ed1bda31ce0ae81e59759341b6671cb7fc9d9301fe453e048` |

Runner routing ครบ 10,248 stage keys. V7 runtime pins เป็นหลักฐานรุ่นเดิม;
รอบนี้ใช้ exact pins ของ v8 พร้อมคง immutable lineage เดิม
Protocol มี `enabled_stages=[]`, approvals ทุกคนเป็น false และ `prompt_token_reserve=null`
API worker ปฏิเสธ proposal ด้วย `protocol_not_frozen`; เปลี่ยน approval flag ของสำเนาทดสอบเพียงอย่างเดียว
ยังถูกปฏิเสธด้วย `prepare_contract_not_frozen`

## Reserve ตาม prompt bytes ที่ตรวจจริง

Prompt สูงสุดคือ **Math-1: 252,219 UTF-8 bytes**; รวม prompts 20 bugs หนึ่งชุด = **3,181,001 bytes**
ตัวเลขนี้เป็น bytes และเป็น numerical floor ของ conservative guard ใน worker ไม่ใช่ provider token counts
เมื่อคงข้อเสนอ output cap 4,096:

```text
shared prompt_token_reserve >= 252219 + H
request reservation         >= 256315 + H
20 requests ต่อ model        >= 5126300 + 20H
40 requests สอง models       >= 10252600 + 40H
```

H คือ provider framing bound ที่ยังไม่มีหลักฐาน จึงคง `framing_bound_H=null`
และ `final_prompt_reserve=null` แม้วัด prompt inventory ครบแล้ว
ผลรวม reservations เป็น planning envelope; ไม่ใช่ actual usage หรือข้อกำหนดว่าต้องมี quota พร้อมกันทั้งชุด
Worksheet มี 40 rows พร้อม per-prompt floor และ shared floor แยกชัดเจน;
per-prompt rows ไม่ได้เปลี่ยน single frozen reserve ของ worker

## หลักฐาน provider ที่มีอยู่

ใช้ [historical KKU calibration receipt](../../output/api854-provider-preflight-20261003/champ-kku-v5-calibration-evidence-v1.json)
ของ a01 วันที่ 3 ต.ค. 2026 เท่านั้น ไม่มีการอ่าน quota ปัจจุบัน

| Model | เวลา observation UTC | Historical remaining | Remaining − shared floor 256,315 ก่อน H |
|---|---|---:|---:|
| claude-sonnet-5 | 00:13:41.830849 | 199,961 | −56,354 |
| gemini-3.5-flash-lite | 00:13:43.301803 | 349,979 | 93,664 |

ตาม snapshot นั้น Claude มี remaining ต่ำกว่า shared numerical floor ก่อนคิด H;
Gemini มีส่วนต่างบวกก่อน H แต่ทั้งสองแถวไม่รับรอง current quota หรืออนุญาต live reservation
ไม่ใช้ prompt tokens 35/20 ของข้อความสั้นที่ตอบ OK ประมาณ token count ของ source prompts

Calibration เดิมยืนยันว่า request `temperature=0`, `max_tokens=4096`, `stream=false` ได้ HTTP 200
แต่ไม่ได้ยืนยัน effective backend settings, underlying model revision, context/output ceilings,
framing H, quota bucket/window/reset หรือ observation expiry; unknown fields ยังคง null
Public packet มี response fields และ raw-body hashes แต่ไม่มี original raw response bytes
จึงไม่อ้างว่าได้ reverify response body hashes แล้ว

Worksheet เก็บ SHA-256 และขนาด serialized request แบบเดียวกับ KKUClient โดยไม่ส่ง request
Math-1 มี wire JSON 263,750 bytes สำหรับ Claude และ 263,756 bytes สำหรับ Gemini
JSON overhead 11,531/11,537 bytes เป็น serialization bytes และไม่ใช่ provider framing tokens หรือค่า H

## Validation และงานที่ยังรอ

Focused tests ผ่าน **5/5**, ไม่มี skips/failures/errors: ตรวจ consumer equality โดยปิด HTTP,
pending limits/reserve, byte guard ต่ำกว่า prompt จริงหนึ่ง byte, prompt ถูกเปลี่ยน,
และ exclusion ถูกลบหนึ่งรายการ

สี่ enum targets ของ JacksonXml-1 (`configure`, `disable`, `enable`, `isEnabled`) ยังอยู่ใน exclusions
และ denominator 691; `empty_enum_joint_decision_approved=false`
ข้อเสนอเรื่อง empty non-null parameter domain และ null boundary ยังต้องคำตัดสินร่วมตาม
[ข้อเสนอของบีม](BEAM_V7_ENUM_DECISION_TH.md)
ไม่ตีความ diagnostic NPE ว่าเป็น oracle ที่ทีมอนุมัติ

ก่อนใช้ live ต้องมี effective limits/settings, framing, bucket/reset/expiry และ condition-bound
semantic/host/team acceptance พร้อม joint Gate A review
ต้องคำนวณ reserve ใหม่เมื่อ recipe, policy, prompt หรือ pinned runtime เปลี่ยน
รายงานนี้ไม่อนุมัติ primary results, live pilot หรือ import quota ledger
