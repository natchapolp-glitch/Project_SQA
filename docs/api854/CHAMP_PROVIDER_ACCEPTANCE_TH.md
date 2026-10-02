# หลักฐานอ่าน API/คิวจากเครื่องแชมป์ — 3 ตุลาคม 2569

หลักฐานปัจจุบัน: [champ-readiness-v2.json](../../output/api854-provider-preflight-20261003/champ-readiness-v2.json)
เก็บ timestamps, HTTP status, hashes และรายการ fields ที่ตรวจได้.
รุ่น v1 เก็บ checkpoint ก่อนเพิ่มข้อมูล stage gate; ไม่เขียนทับหลักฐานเดิม.
ไม่มี KKU generation request, claim, upload, complete หรือการใช้โควตาสร้างเทสในการตรวจนี้.

## ยืนยันจาก endpoint จริงแล้ว

- ใช้ URL ใหม่ `https://angel-keywords-optics-alternatively.trycloudflare.com`
  ตรวจ health พร้อมและ authenticated schema `1.0` จากเครื่องแชมป์ผ่าน.
  URL ใน default private access file ของแชมป์อัปเดตหลัง authentication ผ่าน;
  token อยู่ `.local/` เท่านั้น.
- `GET /v1/status` พบ run `api854-pilot-preflight-20261003-v1` มี 80 jobs
  ทั้งหมด `prepare/queued`. `enabled_stages` เปิดเฉพาะ `prepare`.
- Hash ที่คิวรายงานสำหรับ run นี้:
  `675a480915c40ab19f7be57b56b046fb8c8924e7330e4cc8956b2ed3de6d31ad`.
  ยังไม่ได้อ่าน bytes ของ `protocol.core-frozen.json` จาก Git branch ออม;
  จึงยังไม่อ้างว่าเครื่องแชมป์เทียบ file hash หรือทำซ้ำ 114 tests แล้ว.
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

1. ออม **push `6dd61871` หรือ commit integration ล่าสุดขึ้น branch `aom`**.
   ตอนตรวจ remote ยังอยู่ที่ `2e419e421c2fcc75dbfd516e04ed8ce27aafa463`.
   ไฟล์บันทึกตรวจรับที่ `C:/Users/ACER/.../Project_SQA_aom/` เป็น path เครื่องออม;
   เครื่องแชมป์ต้องรับไฟล์ผ่าน Git/private channel จึงจะตรวจได้.
2. ให้ทีมใช้ receipt นี้เป็นหลักฐาน model discovery/queue readiness;
   ไม่นับว่า settings/quota หรือ Gate B ผ่านแล้ว.
3. บีมส่ง context/prompt/suite policies, resolver/adapters และ fixed/buggy/coverage evidence.
4. หลังทีมตรวจครบ ให้ตรึง primary protocol bytes และ seed **run ใหม่**;
   คง core-frozen/preflight run เดิมไว้ ไม่เปลี่ยนย้อนหลัง.

แชมป์ตรวจออฟไลน์ชุดที่มีใน branch ตนผ่าน 90 tests ก่อนหน้านี้.
ผล 114 tests เป็นข้อมูลที่ออมแจ้ง ยังไม่ได้ทำซ้ำบนเครื่องนี้เพราะ commit ยังไม่อยู่บน remote.
