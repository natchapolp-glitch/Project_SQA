# ออมทำ prepare artifacts ของ pilot 20 bugs

Artifacts อยู่ `output/api854-20261003/prepare-v1/` แยกโฟลเดอร์ `Project-bugId`.
`index.json` รวมทั้ง 20 bugs, owner, source/prompt hashes, ขนาด prompt และสถานะตรวจรับ.
แต่ละโฟลเดอร์มี `context-manifest.json`, `prompt.md`, `prepare-metadata.json`,
`fixed-source/`, `context.md`, `revision-proof.json` และ `checksums.json`.

เลือกทุก production class จาก Defects4J `modified_classes/<bug>.src` และ build files
ที่รู้จักใน root ของ fixed worktree เท่านั้น ไม่แนบ patch, buggy source, triggering tests,
compile/test/coverage logs หรือ credentials. ทุก selected Java source ไม่มี local edits
และ HEAD ตรงกับ `D4J_Project_bug_FIXED_VERSION` ทั้ง 20 bugs.
Closure-176, JacksonDatabind-112 และ JxPath-22 checkout เพิ่มแบบแยกจาก worktrees เดิม.

## สถานะและสัญญา

เป็น `prepared_proposal_pending_adapter_review` ยังไม่ publish outcome `prepared` เข้าคิว.
ไม่เปลี่ยน frozen-core bytes/hash เดิม และไม่เรียก KKU หรือ claim งาน.
Primary draft เพิ่ม context/prompt policy IDs เพื่อส่งให้ทีมตรวจรับแล้ว:

- context: `aom-fixed-modified-classes-build-v1-proposal`
- prompt: `aom-fixed-junit4-cap30-v1-proposal`

Worker resolve_prepared_job อ่าน artifact bytes/metadata ตามสัญญาได้ใน offline test.
Prompt เป็น JUnit 4, deterministic, meaningful assertions, source-order cap 30,
ไม่แก้ production code และไม่มี feedback/repair loop.
Candidate classes ครบตาม policy แต่ยังไม่มีการรับรอง method/fixture eligibility.

## แชมป์ตรวจต่อ

ใช้ `index.json` ดูขนาด prompt ของแต่ละ bug. ขนาด UTF-8 ไม่ใช่ measured tokens.
prompt_token_reserve ต้องไม่น้อยกว่าขนาด prompt ใหญ่สุด รวม provider framing overhead
ที่ตรวจจริง และต้องอยู่ใน context limit ของทั้งสองโมเดล.
อย่าตั้ง reserve อัตโนมัติจาก quota advertised; ยังรอ observed remaining/settings evidence.

## บีมตรวจต่อ

ตรวจ target declaration signatures ที่ร่วมกันใน fixed/buggy และ common fixture types;
บันทึก exclusions/eligible targets แบบ reproducible ก่อนใช้ prompt นี้.
หากต้องเพิ่ม fixed dependencies, API context, fixture descriptions หรือปรับ prompt
ให้สร้าง artifact version ใหม่พร้อม hashes/lineage ไม่แก้ v1 หลังตรวจรับ.
ส่ง suite resolver, package/file/processing policy และ adapters/evaluator evidence ต่อ.

หลังตรวจรับให้ตรึง primary protocol bytes/hash และ seed run ใหม่ก่อน publish preparation.
Preflight run เดิมยังเปิดเฉพาะ prepare; generation/evaluate ถูกล็อก.
ห้ามนำข้อเสนอเหล่านี้นับเป็น live pilot result หรือ Gate B.

## สร้างซ้ำ

ใช้ output directory ใหม่ที่ยังไม่เคยมี artifacts:

```powershell
python -m scripts.study.api854.prepare_pilot --workspace //wsl.localhost/Ubuntu/home/aomsin/sqa-round2 --pilot experiments/configs/api854-20261003/protocol.core-frozen.json --output output/api854-20261003/prepare-v2
```

Exporter จะปฏิเสธ revision ที่ไม่ใช่ fixed และ path ของ logs/hidden/generated directories;
selector ปฏิเสธ source ที่หาไม่พบหรือพบหลายตำแหน่งแทนการเดา.
`revision-proof.json` เป็นหลักฐาน Git ของ v1 ณ เวลาตรวจ ต้องตรวจใหม่เมื่อสร้าง version ใหม่.

Validation: ชุด api854 ผ่าน 116 tests; checksums และ fixed revision proofs ผ่านครบ 20 bugs.
prompt ใหญ่สุด Math-1 = 99,335 UTF-8 bytes (ยังไม่รวม provider framing overhead).
