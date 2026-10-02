# หลักฐานอ่าน API/คิวจากเครื่องแชมป์ — 3 ตุลาคม 2569

หลักฐาน API/คิว: [champ-readiness-v2.json](../../output/api854-provider-preflight-20261003/champ-readiness-v2.json)
สถานะรับงานบีม/ออมล่าสุด: [CHAMP_V3_ACCEPTANCE_TH.md](CHAMP_V3_ACCEPTANCE_TH.md)
เก็บ timestamps, HTTP status, hashes และรายการ fields ที่ตรวจได้.
รุ่น v1 เก็บ checkpoint ก่อนเพิ่มข้อมูล stage gate; ไม่เขียนทับหลักฐานเดิม.
ไม่มี KKU generation request, claim, upload, complete หรือการใช้โควตาสร้างเทสในการตรวจนี้.

## รับ integration ที่ออม push แล้ว

ดึงและรวม branch `aom` ถึง commit `8fcec539` แล้ว (รวม `6dd61871`).
ตรวจออฟไลน์บนเครื่องแชมป์ **116 tests ผ่าน** รวม gated Store ปัจจุบัน.
ตรวจ prepare artifacts ครบ **20 bugs / 162 checksum entries**; source/prompt bytes
และ metadata hashes ตรงกัน. SHA-256 ของ core protocol bytes ตรงทั้ง `.sha256`
และ hash ที่คิวรายงานใน readiness receipt.
หลักฐาน: [champ-aom-preparation-audit-v1.json](../../output/api854-provider-preflight-20261003/champ-aom-preparation-audit-v1.json).

Prompt ใหญ่สุด Math-1 = **99,335 UTF-8 bytes**; รวม 20 prompts = 905,811 bytes.
ตัวเลขนี้ไม่ใช่ measured tokens. Worker ปัจจุบันต้องมี conservative prompt reserve
ครอบคลุม byte floor และ provider framing; ยังไม่กำหนด reserve/runtime model limits แทนทีม.
Artifacts ยังระบุ adapter eligibility เป็น false; checksum ผ่านไม่อนุมัติ eligibility
หรือ Gate A/B. ไม่เปลี่ยน frozen-core bytes และไม่ claim/publish preparation เข้าคิว.

## ยืนยันจาก endpoint จริงแล้ว

- ใช้ URL ใหม่ `https://angel-keywords-optics-alternatively.trycloudflare.com`
  ตรวจ health พร้อมและ authenticated schema `1.0` จากเครื่องแชมป์ผ่าน.
  URL ใน default private access file ของแชมป์อัปเดตหลัง authentication ผ่าน;
  token อยู่ `.local/` เท่านั้น.
- `GET /v1/status` พบ run `api854-pilot-preflight-20261003-v1` มี 80 jobs
  ทั้งหมด `prepare/queued`. `enabled_stages` เปิดเฉพาะ `prepare`.
- Hash ที่คิวรายงานสำหรับ run นี้:
  `675a480915c40ab19f7be57b56b046fb8c8924e7330e4cc8956b2ed3de6d31ad`.
  Readiness v2 เป็น checkpoint ก่อนออม push; checkpoint รับ integration ด้านบน
  เทียบ core bytes/hash สำเร็จแล้ว และตรวจชุดล่าสุด 116 tests.
- บัญชี alias `a01`: `GET /models` และ `POST /chat/models-list` คืน HTTP 200
  และแสดง exact string IDs `claude-sonnet-5` / `gemini-3.5-flash-lite` ทั้งสอง endpoint.
  Provider ที่รายการโมเดลระบุคือ Claude / Gemini ตามลำดับ.
  นี่เป็น model discovery ไม่ใช่หลักฐาน resolved model ของ generation.
- CLI worker ปฏิเสธ `champ-generation-contract.proposal.json` ด้วย
  `protocol_not_frozen` / exit 1 ก่อน discovery หรือ claim.

## Settings/limits: สิ่งที่ทราบและยังไม่ทราบ

[KKU API reference](https://gen.ai.kku.ac.th/docs/api) ที่ดาวน์โหลดจริงระบุ
parameter `temperature` ช่วง 0–2 และ `max_tokens` สำหรับจำกัดความยาวคำตอบ.
มี hash ของ HTML ใน receipt; สำเนา HTML/text อยู่ `.local/` ไม่ commit.

ดังนั้น temperature 0 อยู่ในช่วงที่เอกสาร API ทั่วไประบุ.
**ยังไม่ได้ยืนยันว่าทั้งสองโมเดลรับ/ใช้ temperature 0 และ max_tokens 4096 จริง**
เพราะยังไม่อนุญาตให้เริ่ม generation. ไม่มีการยิง token probe เพื่อทดสอบ settings.
เอกสาร parameter ทั่วไปไม่รับรอง context/output limits รายโมเดล.

รายการจาก `/models` มีเพียง fields `id`, `object`, `display_name`, `created`,
`owned_by`; ไม่คืน supported settings, context/output caps หรือ remaining/bucket/expiry.
`prompt_token_reserve` ยังไม่ตรึง ต้องคำนวณจาก context/prompt ของบีมและ provider framing.
Temperature 0 / max_tokens 4096 จึงยังคงเป็นข้อเสนอร่วมก่อน freeze primary protocol.

## โควตา: ยังเปิด generation ไม่ได้

Remaining, shared bucket และ expiry/reset ของ Sonnet 5/Flash Lite **ยังไม่ทราบ**.
ไม่ใช้ 200k/350k จากแผนแทน observed remaining.
เอกสารแสดง quota fields ใน completion response แต่ model-list endpoints ไม่คืนค่าเหล่านี้.
ยังไม่พบ quota-only endpoint ใน API reference ที่ตรวจ และไม่มีการเดา endpoint เพิ่ม.
Browser runtime ในเซสชันแชมป์เปิดไม่ได้ จึงยังอ่านหน้า quota ของบัญชีจาก UI ไม่ได้.

เจ้าของบัญชีต้องส่งตัวเลข actual remaining ของทั้งสองโมเดลและหลักฐานว่า bucket
แชร์กันอย่างไร พร้อมเวลาสังเกต/expiry ที่ตรวจได้ผ่านช่องทางส่วนตัวตามเดิม.
ลง ledger ผ่าน `quota_control observe` หลังมีข้อมูลจริงเท่านั้น.
Keys ที่เครื่องนี้พบมีเฉพาะ `a01`; เริ่ม pilot บัญชีเดียวที่ตรวจแล้วได้.

## สิ่งที่ต้องส่งกลับให้ออม/บีม

1. รับงานออมผ่าน Git แล้ว; ไม่ติดเรื่อง commit ยังไม่ push.
   Readiness receipts v1/v2 เก็บสถานะ remote เดิมไว้เป็นประวัติ ไม่แก้ย้อนหลัง.
2. ให้ทีมใช้ receipt นี้เป็นหลักฐาน model discovery/queue readiness;
   ไม่นับว่า settings/quota หรือ Gate B ผ่านแล้ว.
3. รับ context/prompt/suite policies, resolver/adapters และ Lang-4 mock-AI fixed/buggy/coverage evidence จากบีมแล้ว.
   ล่าสุดรับ source-bound discovery 20 bugs / 691 declarations เข้า shared v3 แล้ว และรับ Beam fixture review สี่ suites
   ของ Closure-176/JxPath-1. ยังรอ fixtures/oracles อีก 18 bugs และ semantic review ร่วมทีมตามเอกสาร v3.
4. หลังทีมตรวจครบ ให้ตรึง primary protocol bytes และ seed **run ใหม่**;
   คง core-frozen/preflight run เดิมไว้ ไม่เปลี่ยนย้อนหลัง.

แชมป์ผ่าน 90 tests ก่อนรับงานออม; ปัจจุบันรวม integration และ prepare tests แล้วผ่าน 116 tests.
