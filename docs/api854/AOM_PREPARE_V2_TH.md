# Prepare v2, policy ร่วม, runner ทุก owner และ checklist Gate A

ชุดใหม่ supersedes preparation proposal นี้: [AOM_PREPARE_V3_HANDOFF_TH.md](AOM_PREPARE_V3_HANDOFF_TH.md).
v2 เดิมเก็บเป็น immutable evidence; discovery import/receiver/shared fixtures อยู่ใน v3 แล้ว.

ออมทำทั้งสี่ส่วนแล้ว โดยเก็บ v1 และ frozen-core bytes เดิม.
ชุดนี้เป็น proposal พร้อมตรวจรับ ไม่ใช่ frozen primary protocol หรือการเปิด live generation.

## 1. Prepare รุ่นใหม่

Artifacts ครบ 20 bugs อยู่ `output/api854-20261003/prepare-v2/`.
แต่ละ bug มี context-manifest.json, prompt.md, targets.json, prepare-policy.json,
prepare-metadata.json, fixed-source, revision-proof และ checksums.
index.json ระบุ owner, hashes, provenance จาก v1 และขนาด prompt.

ใช้สัญญา `aom-beam-prepare-v2`; metadata มีทั้ง source_sha256/prompt_sha256/prompt_policy_id
ของแชมป์ และ fixed_source_sha256/context_source_hash ของบีม พร้อม policy/targets hashes.
worker prepare ของบีมสร้าง prompt/metadata แบบเดียวกันและ publish เป็น artifacts แยกได้แล้ว.
API worker ตรวจทั้งสัญญาและ inventory ของ eligible targets ก่อน claim/API.
Evaluator รับ suite/lineage และ fixed-source mapping เดียวกัน; generation/evaluation แยก attempt ID.

v2 คง fixed Java/build bytes และ fixed revision proofs ของ v1.
ไฟล์ prompt/manifest ใหม่มี lineage กลับไป hash เดิม ไม่แก้ v1.
prompt ใหญ่สุด 99,439 UTF-8 bytes ณ ชุดที่ยังไม่มี target declarations.
เมื่อเพิ่ม declarations ขนาดจะเปลี่ยน ต้องวัดและตั้ง reserve รวม provider framing ใหม่.

ยังไม่มี full source-bound target inventories ส่งกลับมา จึงมี targets=[]/pending_discovery
ทั้ง 20 bugs และ adapter_eligibility_verified=false. ไม่ใช้ scan receipt hashes 6 bugs
แทนรายการจริง. API worker ปฏิเสธชุดที่ไม่มี eligible declarations ก่อน generation.
เมื่อได้หลักฐาน ให้สร้าง version ใหม่ด้วย --eligibility-root ไม่แก้ v2 ที่ส่งตรวจรับแล้ว.
eligible declarations หมายถึง discovery support เท่านั้น; semantic/oracle review ยังแยกต่างหาก.

อัปเดตหลังตรวจบีม `44dd5cb0`: ได้รับ source-matched discovery ครบ 20 bugs/691 declarations
และ exclusions 3 รายการแล้ว แต่ยังไม่ import ลง inventories ของ v2 เดิม.
ดู [ผลตรวจ context/prompt และสัญญาที่ต้องรวม](AOM_BEAM_HANDOFF_REVIEW_TH.md)
พร้อม checklist `gate-a-beam-review-release.json`. Semantic/oracle และ primary approval ยัง pending.

## 2. Policy ชุดเดียว

ไฟล์ `experiments/configs/api854-20261003/prepare-policy.v2.json`:

- context: modified-java-and-root-build-v1; ใช้ fixed context เดียวกันทั้งสี่วิธี.
- targets: shared-declaration-signatures-v1; เทียบ class/constructor/method/parameters
  ของ fixed และ buggy โดยไม่เอา buggy outcomes, patch หรือ trigger เข้า prompt.
- fixture: common-fixed-buggy-production-types-v1 ตาม SqaProbe; เลือก constructor
  ที่รองรับและง่ายที่สุดจาก common production types.
- prompt: shared-fixed-targets-junit4-v2, deterministic meaningful assertions, JUnit 4.
- suite: beam-java-suite-v1; เก็บ Java bytes เดิมและจัด package/public class filenames.
- suite เกิน 30 methods: ปฏิเสธทั้งชุดเป็น generation_failed เก็บ raw bytes;
  ไม่ตัด methods/แก้ assertions/prune failures หรือส่ง AI ซ้ำเชิง semantics.
- จำนวน annotations เป็น packaging count; executed/skipped ต้องวัดจาก evaluator.

primary draft มี policy SHA และ fields ของ worker ทั้งสองฝั่งแล้ว.
temperature 0/output 4096 ยังเป็น proposal; model_settings_verified=false, reserve=null.
state/status/approval ยังไม่ frozen และ enabled_stages=[] ใน primary draft.
core run เดิมยัง prepare-only ตาม protocol bytes เดิม ไม่ได้รับการแก้ย้อนหลัง.

## 3. Runner ทุก owner

ไฟล์ `experiments/configs/api854-20261003/runner-plan.v1.json` เป็น host assignment proposal:

- champ-pc1 ใช้ role=champ ส่ง KKU สำหรับ owner champ/beam/aom.
  เป็น account coordinator เดียว; keys/ledger อยู่เครื่องนี้, หนึ่ง outstanding ต่อบัญชี,
  global API limit 2 และแจ้งผู้ใช้ก่อนสลับบัญชี.
- beam-pc1/2/3 ใช้ role=beam ทำ prepare, algorithm generation และ evaluation ของ owner=beam.
- aom-pc1/2 ใช้ role=aom ทำ prepare, algorithm generation และ evaluation ของ owner=aom/champ.
- ทุก CPU host ถือ slot เดียวจาก canonical worktrees root; host pool claim ผ่าน lease.

ตรวจ coverage ของ 854 bugs × 4 approaches × 3 stages = 10,248 stage keys ไม่มีช่องว่าง.
TeamQueueClient บังคับ worker/role/owner/stage/approach ตรง plan ก่อน claim.
owner ของ job ไม่เปลี่ยน; ไม่สลับ owner เพื่อหลบ quota/lease.
CPU runner รับ KKU generation ไม่ได้. API CLI รองรับ --owner โดย coordinator ไม่เปลี่ยน job keys.
Primary routing ต้องมี reviewed/frozen runner plan และ SHA ตรง frozen protocol.
ตอนนี้ยังไม่เริ่ม process/ตั้ง scheduler หรือแจก credentials เพิ่ม.

หลังตรวจรับ host และได้รับ private access file ของตัวเองแล้ว ตัวอย่าง prepare-only บน WSL:

```bash
python3 -m scripts.study.api854.queue_worker \
  --access .local/api854/aom-access.private.json \
  --runner-plan experiments/configs/api854-20261003/runner-plan.v1.json \
  --worker-id aom-pc1 --owner aom --stage prepare \
  --approaches cmaes fscs-art kku-claude kku-gemini \
  --protocol experiments/configs/api854-20261003/protocol.core-frozen.json \
  --run-id api854-pilot-preflight-20261003-v1 --condition preflight \
  --worktrees "$HOME/sqa-round2/team-worktrees" \
  --output .local/api854/new-prepare-operation
```

ให้ output เป็น directory ใหม่เสมอ. หาก mutation ไม่ทราบผลให้ reconcile ไม่ rerun.
ถ้ารับงาน champ ให้ --owner champ; งานบีมใช้ worker/access ของบีม.
ตอน primary ต้องใช้ protocol/run ใหม่ที่ทีมรับรอง ไม่ใช้ core กับ generate/evaluate.

## 4. Checklist และหลักฐาน Gate A

JSON ล่าสุด: `output/api854-20261003/team-preparation-v2/gate-a-checklist-release.json`.
มี path/SHA ของ policy, plan, core/primary draft, index และ receipts ของแชมป์/บีม.
ผ่านด้าน artifact contract 20 bugs, policy consistency, route coverage และ model discovery.
ยัง pending host acceptance, source-bound eligibility 20 bugs, meaningful oracle/pipeline review,
settings/limits/reserve, observed quota และ review ทั้งสามคน.
เครื่องมือ checklist ไม่ freeze protocol, seed jobs หรือเปิด stages ให้เอง.

อัปเดตเป็น checkpoint ใหม่หลังได้หลักฐาน:

```bash
python3 -m scripts.study.api854.gate_a --output output/api854-20261003/team-preparation-v2/gate-a-next.json
```

แชมป์ส่ง observed remaining/bucket/expiry และ model runtime/settings/limit/framing evidence.
บีมส่ง actual target inventories พร้อม project/bug/fixed-source hashes และ fixture policy,
รวม semantic/fixed-twice/buggy/coverage evidence; receipt digest อย่างเดียวไม่อนุมัติ inventory.
ทีมตรวจ policy/hosts แล้วจึงสร้าง frozen primary bytes/hash และ seed run ใหม่.

## สร้าง prepare version ถัดไป

```bash
python3 -m scripts.study.api854.build_prepare_v2 \
  --input output/api854-20261003/prepare-v1 \
  --eligibility-root /path/to/reviewed-eligibility \
  --output output/api854-20261003/prepare-v3
```

eligibility root ต้องมี Project-bug/eligibility.json ที่ project, bug_id,
fixed_source_sha256, target_selection, fixture_policy และ targets ตรงสัญญา.
เก็บผล actual discovery และ review ของผู้ผลิตประกอบด้วย; builder ไม่ตรวจ semantic validity แทนบีม.
ไม่แนบ execution logs เข้า prompt; discovery errors/exclusions เก็บใน evidence แยก.

Validation: WSL api854 186 tests ผ่าน; Windows 185 pass/1 skip (symlink fixture);
legacy evaluator/generator 20 tests ผ่าน. Queue HTTPS/local พร้อม, 80 prepare/queued/0 attempts,
ไม่มี queue mutation หรือ live KKU ในรอบทำงานนี้.
