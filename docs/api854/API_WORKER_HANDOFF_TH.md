# API worker ของแชมป์ — ส่งให้ออม/บีมตรวจรับ

## สิ่งที่ทำได้แล้ว

`python -m scripts.study.api854.api_worker --help`
แสดง CLI ที่รับงาน **ครั้งละหนึ่ง job** จากคิวออม schema 1.0.
`--check` ตรวจ readiness/หลักฐานเตรียมงานด้วย GET; `--once` จึง claim และสร้างเทส.
ไม่มี loop รัน 80 jobs อัตโนมัติ ไม่มีการตั้งเวลารัน และไม่เปิดใช้ draft protocol.

ขั้นตอน: ตรวจ frozen settings/model/remaining → ตรวจ prepare artifacts/hashes →
claim งานของ champ → เก็บ claim ส่วนตัว → renew lease → reserve quota →
ส่ง KKU ครั้งเดียว → เก็บคำตอบดิบ → เรียก suite resolver ของบีม →
upload artifacts → complete generated ให้คิวไป evaluate.
คำว่า generated ไม่รับรอง fixed/buggy/coverage; บีมประเมิน suite เดียวกันต่อ.

ตรวจ integration แบบออฟไลน์กับ **Store จริงของออม commit
`2e419e421c2fcc75dbfd516e04ed8ce27aafa463`** แล้ว: prepare → generate → evaluate,
stage history, artifact hashes, lease renew และ suite hash ผ่าน.
KKU ในการตรวจนี้เป็น mock condition `mock-integration`; ไม่มี live generation
หรือการรัน Java และไม่ใช้คิว/credentials ของจริง.
ชุดตรวจปัจจุบัน 90 tests ผ่าน รวม readiness, quota ก่อน claim, unknown request,
duplicate generation และ notification acknowledgement.

## สิ่งที่ต้องการจากออม

รวม fields ที่ตกลงจาก
`experiments/configs/api854-20261003/champ-generation-contract.proposal.json`
เข้า **protocol เต็มของทีม** แล้วตรึง `approval_state="frozen"`.
ห้ามเปลี่ยนสถานะไฟล์ proposal เป็น frozen แล้วถือว่า experiment protocol ครบ.
hash ที่ worker ใช้คือ SHA-256 ของ bytes ไฟล์ protocol ตรงกับวิธี seed ของคิวออม.

- คู่โมเดลที่ผู้ใช้เลือกแน่นอน: `claude-sonnet-5` และ `gemini-3.5-flash-lite`.
- `temperature=0`, `max_tokens=4096` เป็น settings ที่แชมป์เสนอสำหรับทั้งคู่;
  ต้องตรวจ context/model limits และตกลงก่อน freeze ไม่มีค่า default แทนค่าที่ขาด.
- `prompt_token_reserve` ต้องกำหนดจากขนาด context ที่เตรียมจริง พร้อม framing overhead.
  worker ใช้จำนวน UTF-8 bytes ของ prompt เป็น conservative floor ไม่อ้างว่าเป็น token ที่วัดจริง.
- ตรึง `context_policy_id`, `prompt_policy_id`, `suite_policy_id` และ
  `suite_resolver="scripts.study.<module>:<function>"` ที่บีมส่งให้.
- `start_at` และ `generation_cutoff_at` มี timezone; worker ไม่ส่งก่อนเริ่ม/หลัง cutoff.
- seed pilot ด้วย bytes/hash เดียวกันหลัง integration ตรวจรับ.

คิว 1.0 ไม่มีตัวกรอง protocol ใน claim. Worker จึงปฏิเสธคิวที่ปะปน protocol
สำหรับ champ/approach นั้น ก่อน claim และตรวจ hash ของ claim ที่ได้อีกครั้ง.

## สิ่งที่ต้องการจากบีมใน prepare

ใน `/complete` ของ stage prepare ที่ outcome `prepared` ส่ง artifacts:

| Artifact | ข้อกำหนด |
| --- | --- |
| `context-manifest.json` | output ของ `export_context()`: exact fixed revision, source files/hash, context policy, ไม่มี execution logs |
| `prompt.md` | prompt สุดท้ายตาม frozen policy; ไม่แนบ compile/test/coverage logs |

Prepare metadata ต้องมี `source_sha256`, `prompt_sha256` (UTF-8 bytes),
`prompt_policy_id`. Worker ดึงจาก `payload.stage_history` ของ prepare ล่าสุด,
ตรวจ SHA-256/size ของ artifact และตรวจ fixed identity/policies ก่อนส่ง.
Selection/prompts เป็นงานที่ทีมตรึง; worker ไม่เลือก targets/เปลี่ยน prompt ให้เอง.

บีมส่ง callable `suite_resolver(result) -> Path` ที่คืน immutable `suite.tar.bz2`
พร้อม policy เรื่อง filenames/packages, source-order cap 30, repair/exclusion
และ raw/processed lineage. Resolver ต้องไม่สร้าง assertions/fixtures ใหม่.
ให้ resolver เก็บ lineage ใน archive หรือ artifacts ของ evaluator ตาม protocol.

## โควตาและการแจ้งก่อนสลับบัญชี

`python -m scripts.study.api854.quota_control --help`
มีคำสั่ง `observe`, `activate-initial`, `request-switch`,
`acknowledge-notification`, `status`. Ledger/notification files อยู่ `.local/`.

`observe` รับ account/bucket/window, **remaining ที่สังเกตจริง**, expiry ที่มี
timezone และไฟล์หลักฐาน. เก็บ hash ของหลักฐาน ไม่ตั้งยอดจาก advertised quota.
Expiry เป็นเวลาที่ observation หมดอายุแบบ conservative จนตรวจ reset ได้;
ไม่ถือว่าเที่ยงคืนเติมโควตาเอง. จากนั้น `activate-initial` สำหรับบัญชีแรก.

ตัวอย่างหลังผู้ดูแลกำหนดค่าที่สังเกตจริงในตัวแปรแล้ว:

```powershell
python -m scripts.study.api854.quota_control observe --account a01 --bucket $verifiedBucket --window $observedWindow --remaining $observedRemaining --expires-at $observationExpiry --evidence $quotaEvidence
python -m scripts.study.api854.quota_control activate-initial --account a01 --bucket $verifiedBucket
python -m scripts.study.api854.api_worker --check --protocol $frozenProtocol --approach kku-claude --account a01 --bucket $verifiedBucket --window $observedWindow
# เมื่อทุกฝ่ายตรวจรับและถึงเวลาเริ่มแล้ว จึงเปลี่ยน --check เป็น --once
```

เมื่อโควตาไม่พอ worker หยุด **ก่อน claim** ไม่แต่งเป็น generation_failed.
`request-switch` สร้าง event แบบ waiting_notification และไม่เปลี่ยน route.
ผู้ดูแล/Codex อ่าน event แล้วแจ้งผู้ใช้ในช่องที่เห็นได้จริง;
หลังส่งข้อความสำเร็จจึง `acknowledge-notification --event-id ... --receipt ...`.
Console output/event file ไม่ใช่หลักฐานว่าส่งข้อความแล้ว. Worker ไม่มี automatic
account switch และ `activate-initial` ใช้ข้าม gate ไปบัญชีใหม่ไม่ได้.

หลัง acknowledge ต้องมี observation/key/model ของบัญชีใหม่ก่อนรัน.
Ledger นี้ใช้เครื่องเดียว; ถ้าแชมป์เป็น coordinator ให้ keys อยู่เฉพาะเครื่องนี้.
หากแบ่ง API หลายเครื่อง ต้องแบ่งบัญชีไม่ซ้ำหรือตกลง global limiter ก่อน.

## งานที่ยังต้องตรวจสด

1. คิว/tunnel ของออมตอบ health/schema จากเครื่องแชมป์; รอบก่อนพบ HTTP 530.
2. actual remaining/bucket/reset ของบัญชีที่จัดสรร; ไม่จำเป็นต้องรอ keys ครบสิบเพื่อ pilot.
3. target adapters และ meaningful fixed repeat/buggy/coverage ของบีม.
4. settings/prompt/processing contract ข้างต้นได้รับการตรึงร่วมกัน.
5. live end-to-end pilot; การผ่าน tests/integration offline ยังไม่ใช่ gate นี้.

หากหลุดหลัง claim ให้ดู `.local/api854/worker-state/claim-<attempt>.json`
และ reconciliation record พร้อม local send-intent/generation-result.
Unknown request/completion ไม่มีการยิง KKU ซ้ำหรือ resend completion อัตโนมัติ.
เมื่อเสีย lease คำตอบยังอยู่ local; ให้ออม reconcile/รับ artifacts ตามหลักฐาน.
