# แชมป์ตรวจรับ v10: inputs ผ่าน แต่ final provider reserve ยังรอหลักฐาน

ตรวจ exact Aom `a4880fb2fde574e77705841f62f302273be7dcd9` และ Beam
`8af29c164a2f3b6f009c96825748afc909c56298` วันที่ 3 ตุลาคม 2026.
**รับ offline input bindings ของ condition `api854-20261003-joint-recipes-v10-development`:**
20 bugs / selected 390 / exclusions 301 / denominator 691.
Worksheet ครบ 40 คู่; final token reserve ยังเป็น `null` และ Gate A ยังไม่ผ่าน.

## สิ่งที่ตรวจจริง

- ตรวจ Aom final manifest 294 entries และ nested manifests อีกครั้ง; ตรวจ runtime 41 files
  จาก Git blobs จริง. [Receipt](../../output/api854-20261003/champ-v10-readiness-review-v2/receipt.json)
  บันทึกจำนวนแต่ละ manifest แยกกัน ไม่รวม entries ที่ซ้ำเป็น unique files.
- ตรวจ original/received provenance ของ Beam และ receipts v3; exact Buffer 8/Csv กับ Lang 2
  ตรง scoped decisions ที่ Aom ใช้ประกอบ v10. Aom ใช้ explicit Lang receipt เดิมจาก `bcb63277`;
  คำยืนยัน v3 สอดคล้องกัน ไม่ได้แก้ receipt เก่าที่ระบุ pending ย้อนหลัง.
- เปรียบเทียบทุก bug กับ retained Champ v9: เพิ่มเฉพาะ accepted Buffer 8 + Lang 2;
  selected เดิม, fixed-source bytes/inventories และ exclusion reasons ที่เหลือคงเดิม.
  Setter/JDOM/Math อยู่ใน base แล้ว ไม่บวกซ้ำ; JDOM เป็น recipe repair.
- รัน actual received CPU loaders/API resolver บน immutable Aom snapshot: ครบ 80 bug × approach
  combinations. Sources/context/recipes/targets/prompt bindings ตรงกันทั้งสี่ approaches.
  API resolver ใช้ offline settings; FrozenSettings ยังคงปฏิเสธ development generation
  แม้เปลี่ยน approval flag เป็น frozen.
- รัน received focused tests **7 tests ผ่าน ไม่มี skip**. รัน negative/audit tests ของแชมป์
  **8 tests ผ่าน ไม่มี skip** แยกกัน; ไม่รวมกับ 61/7 ของออม หรือ 39 tests checkpoint ก่อนเป็นยอดใหม่.
- Compile/invoke production fixed Java บนเครื่องแชมป์ใหม่: **64 cases / 128 observations**
  ตรง received proof ของออม รวม setter/JDOM/Math 10, Buffer/Csv 42 และ Lang 12.
  ตรวจ 54 Buffer/Csv/Lang cases ด้วย independent scalar/content/state/exception oracle อีกครั้ง.
  Temporary incorrect setter ถูก oracle จับได้. ไม่ใช่การรัน Defects4J buggy/coverage suites ใหม่;
  ไม่เปลี่ยน historical fault หรือ CMA-ES String append entry hits=0 / FSCS-ART=2.

Csv condition ใช้ stream `A\nBC\nDE` เมื่อ vector[0]<0 และ `12\n345\n` ในกรณีอื่น;
fresh production reader ทุก case. รับผลกระทบต่อ five existing reader methods อย่างเปิดเผย.
Lang ยังเฉพาะ private reflective helpers: bounded null/String สำหรับ isAllZeros และ null/int[]
สำหรับ validateArray พร้อม exact exception messages และ unchanged input contents.
ไม่รับ non-array Object, array types อื่น หรือ public callers เพิ่ม.
Chronology 6 / Graphics 7 / empty-enum 4 ไม่ adopt เข้า v10.

## Worksheet และ reserve

[Worksheet ใหม่](../../output/api854-20261003/champ-v10-readiness-review-v2/prompt-model-worksheet.json)
ตรวจ actual prompts ทั้ง 20 และ requested models สองตัว:
`claude-sonnet-5`, `gemini-3.5-flash-lite`; temperature=0, max_tokens=4096, stream=false.
Requested IDs/settings ตรง protocol; ยังไม่ใช่ current provider discovery หรือ effective backend receipt.
สร้าง request ด้วย serialization เดียวกับ pinned KKU client โดยไม่มี transport call.

| รายการ | ผลตรวจ |
|---|---|
| Prompt ใหญ่สุด | Math-1, 265,937 UTF-8 bytes |
| Prompt SHA-256 | `d11fdfdac2ab39cce2e8872caec465f4a3424c0da4f302e91cb9fecc53cf6b6f` |
| Serialized request ของ Math-1 / Sonnet | 278,062 bytes; JSON overhead 12,125 bytes |
| Guard ตามสูตรเดิม | 270,033 + H ที่ยังไม่ทราบ |
| Provider prompt tokens / framing | null / null |
| Current model availability/settings/limits | pending |
| Current bucket/quota/reset/credential expiry | pending |
| Final token reserve / live ready | null / false |

**JSON overhead ไม่ใช่ provider framing และ bytes ไม่ใช่ tokens.**
สูตร token reservation ต้องรวม measured prompt tokens กับ framing ที่ยังไม่ได้รวมใน measurement
แล้วบวก output 4096; ห้ามนับ framing ซ้ำ. Worksheet แสดงสูตรทาง token แยกจาก byte guard.
Current worker ยังตรวจ `len(prompt_bytes) <= prompt_token_reserve` เป็น conservative admission guard
และจอง `prompt_token_reserve + max_tokens`. หาก measurement เป็น tokens น้อยกว่า byte guard
จะใส่ measurement นั้นใน frozen field โดยตรงไม่ได้; ต้องกำหนด bound ที่ผ่านทั้ง guard และ
provider budget ด้วยหลักฐาน/condition เดียวกัน หรือ review runtime เปลี่ยนในรุ่นใหม่.
ยังไม่ตั้งค่าหรือแก้ runtime ให้ข้าม guard ในงานนี้.

อ่าน [เอกสารสาธารณะ KKU](https://gen.ai.kku.ac.th/docs/api) ได้ HTTP 200;
มีพารามิเตอร์ทั่วไปและ quota response fields แต่ไม่ยืนยันสอง exact requested models,
effective settings, context/output limits, hidden framing หรือยอดบัญชีปัจจุบัน.
[Public documentation receipt](../../output/api854-20261003/champ-v10-readiness-review-v2/public-provider-document-review.json)
ระบุเวลา/HTML hash และไม่ได้เก็บ HTML ทั้งหน้า; ไม่ใช้ตัวอย่างใน docs แทน current model discovery.

ตรวจ local credentials แบบ offline แล้ว: aliases a01–a10 โหลดได้, distinct non-empty keys ครบ 10,
secret file ignored/untracked. ไม่เก็บ key หรือ key hash ในหลักฐานและยังไม่ได้พิสูจน์ authentication/expiry.
ไม่เปิด/initialize/import quota ledger และไม่เปลี่ยน account route.
ไม่มี authenticated KKU request, generation, live queue mutation หรือ primary result เพิ่ม.

## Pins และสถานะ runtime

ใช้ exact Aom condition pair นี้ร่วมกัน:

- Preparation index: `4f8e942f8c1b0bf9eb5d8404b49ca3de2c4eb46c2f4b4955dc46347b760509b6`
- Protocol: `eb047d1b52ae3950e476948b4fb0de9c0ba2afbb3dad9efdfc5fef5b26b81ffe`
- Runner: `462f6196d81dfc9ed1bda31ce0ae81e59759341b6671cb7fc9d9301fe453e048`
- Received worksheet: `f6d232d4d1e9d815434928ac357a0fffa6dad60be2923fcc981440983e1b017d`

แชมป์ execute จาก temporary Aom snapshot แล้วลบเฉพาะ temporary directory ที่สร้างเอง.
Champ working tree ยังเป็น shared v9 380/311; received Aom v10 เป็น 390/301.
Runtime v10 ต่างจาก Champ v9 แปดไฟล์ตาม receipt; ไม่ใช้ runtime v9 แทน v10
และไม่เปลี่ยน historical protocol/inputs/results. Source/evidence pins ของ Champ เดิม 100 pins ผ่านก่อน/หลัง.

Sealed packet ปัจจุบันคือ **champ-v10-readiness-review-v2**:

- `receipt.json`: `a9990bc7c048830dc198a9771570cbaa14da19d098e679afcf740570e0da06e9`
- `prompt-model-worksheet.json`: `4ca3ad766346d2723d96b8f016cfcf44138bd659b458c90099dba72907b4ff46`
- `native-fixed-runtime-recheck.json`: `96f69277d5a11245af690dac95fd1079ea42b8de4d5b63b150186dcc4238811d`
- `checksums.json`: `a4e4be05630f8d6852c1a2d91c265e2417460224c9715fef873951965406fdd5`

v1 เก็บเป็น failed counting-guard attempt: received tests ทั้งเจ็ดผ่าน แต่ adapter ของแชมป์
นับ expected เป็นแปดจึงหยุดก่อน Java. v2 แก้ expected count และใช้ destination ใหม่;
ไม่แก้/ลบ v1 หรือ relabel เป็นผลผ่าน. ก่อนสร้าง packet ใช้ Git batch object reader
เพื่ออ่าน nested provenance paths ยาวบน Windows โดยไม่เปลี่ยน received bytes.

## งานที่ต้องส่งให้เพื่อน

**ออม:** แชมป์ตรวจ a4880fb2 + Beam 8af29c16 แล้ว รับ offline v10 input bindings 20 bugs / 390/301/691;
consumers 80 combinations, fixed Java 64 cases ซ้ำสองรอบ และ independent bounded oracles ผ่าน.
ใช้ Champ packet v2 และ return index/hashes จาก commit ที่ส่งกลับ.
Worksheet ครบ 40 คู่, max 265,937 bytes, guard 270,033 + unknown H; final reserve ยัง null.
ขอหลักฐาน current model IDs/effective settings/context-output limits/token-framing และ
coordinator bucket/quota/reset/expiry ที่ทีมได้รับอนุญาตให้เก็บ โดยผูกกับ condition/prompt hashes v10 นี้.
อย่าใช้ quota เก่าหรือ JSON bytes แทน token reserve; รักษา v10 แยกจาก candidate ใหม่.

**บีม:** รับ explicit receipts v3 แล้วและตรวจตรง v10. ขอ final-condition scoped verdict
ของ a4880fb2 สำหรับ semantic/oracles, four-approach inputs และ native host/helper/dependency/CPU-lock bindings
ผูก exact protocol/index/runtime/runner hashes ข้างต้น.
แชมป์ตรวจ offline inputs และ fixed runtime ผ่านแล้ว แต่ยังไม่ถือแทน final host/full semantic approval.
เก็บ historical coverage gap/old suites/results; Chronology/Graphics/Codec ทำ packet แยก.

Gate A checklist ยัง all_common_declarations blocked ที่ 390/691;
semantic/host/provider/quota/three-owner approval ยัง pending. ห้ามเปิด generation/live pilot
หรืออ้างว่า Gate A ผ่านจาก input audit นี้.

## ตรวจซ้ำ

```powershell
python -B -m unittest -v scripts.study.api854.tests.test_v10_readiness_review
python -B -m scripts.study.api854.review_v10_readiness --output output/api854-20261003/champ-v10-readiness-review-NEW --defects4j D:/Projects/Project_SQA/defects4j
```

คำสั่งหลัง execute fixed Java และอ่าน public docs; ไม่มี authenticated API call.
ต้องใช้ output ใหม่เสมอ. Packet v1/v2 ที่ seal แล้วห้ามเขียนทับ.
