# ออมรับผลจริงก่อนขยาย — ใช้ฐาน v12

**สถานะล่าสุดมี condition ใหม่จาก Champ d98fccee และ Csv full Defects4Jครบ4validแล้ว:**
อ่าน [AOM_READY_RESULTS_UPDATE_TH.md](AOM_READY_RESULTS_UPDATE_TH.md) ก่อน.
เอกสารนี้เก็บ baseline invalidและแผนเวลาครั้งแรกครบ ไม่ใช้ยอดเก่าทับผลconditionใหม่.

ผู้ใช้ให้ปรับตามแชมป์ `a4a38a5e` วันที่ 4 ตุลาคม 2026: เก็บ outcome จริงครบสี่วิธี
จาก bugs ที่พร้อมก่อน แล้วขยายตามเวลาที่เหลือ พักการรวม candidate ใหม่.
ฐานการทดลองคือ Aom `63ad195623c2ed3f67f3ae232c00c54d3160ce72` (v12).
Codec shared v13 ที่ทำและ push แล้ว `ff319ecd` เก็บไว้ครบเป็นประวัติ ไม่ลบหรือย้อน branch.
โค้ดใน branch ปัจจุบันจึงเป็น v13 แต่ **รอบ Csv นี้โหลด evaluator/runtime จาก snapshot v12**.
อย่าเรียก worker ใน root แล้วอ้างว่าเป็น v12 หรือใช้คำรับ v12 รับรอง v13.

## ผลที่ออมทำแล้ว

รับสาม packets ของแชมป์จาก Git blobs โดยไม่ merge runtime ฝั่งแชมป์:
generation, native measurement v4 และ four-approach summary.
ตรวจ peer manifest 135 entries, runtime v12 41 pins และ Java ใน archives ตรง manifest.
Native attempts v1/v2/v3 ที่ไม่ผ่านยังอยู่ใน commit แชมป์; ไม่เขียนทับหรือแทนผลเหล่านั้น.

รัน Defects4J 3.0.1 / Java 11 บน WSL เครื่อง `Javis3`, worker `aom-pc1`, CPU 1 slot.
checkout/compile Csv-1f และ Csv-1b ในพื้นที่ใหม่ ล็อก CPU ด้วย canonical worktrees root.
ตรวจ source ของ fixed ตรงทั้ง received metadata และ v12 index แบบ SHA-256 ของ bytes จริง.
ไม่แก้ Java/archives, ไม่ regenerate, ไม่ส่ง logs ให้ AI และไม่มี API/queue mutation ในรอบออม.

| วิธี | ผล Defects4J รอบนี้ | Coverage ของ ExtendedBufferedReader | Fault |
|---|---|---|---|
| CMA-ES | 30 tests; fixed 2 รอบ / buggy / coverage ผ่าน; ทุก stage executed=30, skipped=0, target_checks=30 | 31/37 lines (83.78%); 13/26 branches (50%) | false |
| FSCS-ART | 30 tests; fixed 2 รอบ / buggy / coverage ผ่าน; ทุก stage executed=30, skipped=0, target_checks=30 | 31/37 lines (83.78%); 13/26 branches (50%) | false |
| KKU Claude Sonnet 5 | คง invalid generation: truncated, ไม่มี executable suite จึงไม่เรียก Defects4J | unavailable | unavailable |
| KKU Gemini 3.5 Flash Lite | compile ผ่าน; fixed 2 รอบเริ่ม 15 tests และ fail 1 ข้อเดิมทั้งสองรอบ; reject ทั้ง suite | unavailable; ไม่รัน coverage | unavailable; ไม่รัน buggy |

Gemini ผิด `testReadBufferWithNewlines`: คาด readAgain() = CR (13) แต่ได้ `c` (99).
ทดสอบบน fixed แล้วผิด จึงไม่ใช่ fault ของ Csv. Sealed evaluator หยุดที่ fixed-1 ตาม policy;
fixed-2 เป็น confirmation แยกของ archive เดิม ไม่แก้ evaluator และไม่ให้สิทธิ์รัน buggy/coverage.
Gemini `record.json` เดิมเขียน compile_status=unknown; ตาราง derive compile ผ่านจาก compile.gen.tests log.
15 เป็น Defects4J Formatter.startTest events; skip และ target-check counters ของ AI ชุดนี้ยัง unavailable.
ไม่ยืม skip=0 จาก native มาใส่ Defects4J และไม่แปลง null เป็น 0.

เวลาวัดจริง: preparation+environment+evaluations รวม **77.10 วินาที**.
CMA evaluation 27.37s, FSCS 26.54s, Gemini fixed-1 5.25s และ confirmation 4.43s.
ไม่รวม API/generation ของแชมป์และไม่ใช้ตัวเลข Csv นี้ทำนายเวลา 854 bugs.
มี **4 outcomes / 2 valid full-D4J suites / 2 invalid outcomes** ในหนึ่ง bug เท่านั้น.

## แยกเงื่อนไขและข้อจำกัด

- Generation/native ของแชมป์: `api854-20261004-csv-six-target-native-development-v1`, Java17, UTC.
- Evaluation ของออม: `api854-20261004-csv-v12-d4j-development-v1`, Java11, America/Los_Angeles,
  unchanged suites, seed101, cap30, Cobertura/classes.modified.
- ตาราง native และ Defects4J แยกไฟล์ ไม่รวมเป็นคะแนนเฉลี่ยเดียว. Generation lineage ยังเป็นของแชมป์.
- Scope คือ 6 Csv declarations; coverage คือคลาส ExtendedBufferedReader เท่านั้น.
  ไม่ใช่ทั้ง Csv/403 declarations/854 bugs และยังไม่รับ full input-domain equivalence.
- Primary=false, Gate A ไม่ผ่าน, final reserve=null; ไม่เปิด shared live queue หรือเปลี่ยน gate flags.
- Historical tokens ของแชมป์คงเดิม: Claude 63672+4096=67768; Gemini 41314+1287=42601.
  ไม่ถือว่า quota snapshot เก่าเป็น current quota และไม่รวมบัญชีที่ยังไม่มีหลักฐาน.

## ตารางและหลักฐาน

ทุก path อยู่ใน `output/api854-20261004/`:

- [ชุดรับและ runtime v12](../../output/api854-20261004/aom-ready-csv-intake-v1/receipt.json)
- [แผนก่อนรัน / exact host, protocol, runner, index, evaluator hashes](../../output/api854-20261004/aom-ready-csv-d4j-v1/preexecution-plan.json)
- [Host/source binding](../../output/api854-20261004/aom-ready-csv-d4j-v1/host-binding.json)
- [ผลและเวลา Defects4J](../../output/api854-20261004/aom-ready-csv-d4j-v1/receipt.json)
- [ตาราง Defects4J CSV](../../output/api854-20261004/aom-ready-csv-report-v1/full-d4j-results.csv)
- [รายละเอียด/receipt hashes ทุกแถว](../../output/api854-20261004/aom-ready-csv-report-v1/full-d4j-results.json)
- [ตาราง native เดิม แยก condition](../../output/api854-20261004/aom-ready-csv-report-v1/native-results.csv)
- [Worklist cohort 20 bugs × 4 วิธี](../../output/api854-20261004/aom-ready-csv-report-v1/cohort20-worklist.csv)

Worklist มี 80 แถว: Csv 4 outcomes; อีก 76 แถว **pending_not_received_by_aom**.
สถานะนี้หมายถึงออมยังไม่มีชุดรับเข้าที่ตรวจแล้ว ไม่ได้อ้างว่าบีม/แชมป์ยังไม่ได้ทำ.
Prepared20 ไม่ได้แปลว่าทุก suite/host พร้อมรัน และไม่ใส่ bugs ที่ยังไม่รันเป็น failure.

## งานออมต่อจากนี้

1. รับ suite/response ของ bug ถัดไปจากแชมป์พร้อม commit, checksums, generation condition,
   source/prompt/targets/recipes pins, account allocation ก่อน request และ exact model/settings.
   ตรวจ unsafe archives/count cap/provenance ก่อนรัน; ห้ามเลือกเฉพาะผลผ่าน.
2. ฝั่งออมรับ owners ออม+แชมป์บน aom-pc1 CPU1; บีมรับ owner บีมบน beam-pc1 CPU1.
   ยืนยันว่าไม่มีสองงานใช้ CPU slot/checkout เดียวกันก่อน dispatch.
   Csv รอบนี้ออมทำแล้ว ขอให้บีมตรวจ receipt ก่อนเพื่อหลีกเลี่ยง rerun ที่ซ้ำโดยไม่จำเป็น.
3. ใช้ immutable v12 runtime และพื้นที่ใหม่ต่อ attempt. Fixed ต้องผ่านสองรอบก่อน buggy/coverage;
   เก็บ compile/fixed/timeout/coverage failure, executed/skipped/target_checks และ raw commands/hashes.
   ถ้าเปลี่ยน helper/recipe/prompt/transport/thinking/output settings ให้ประกาศ condition ใหม่ก่อน.
4. เพิ่มตารางราย bug และให้ครบ outcome สี่วิธีก่อนนับเป็น four-method completed bug.
   แสดง valid, invalid, pending แยกกัน; native ไม่แทน full Defects4J.
5. กัน 4 ชั่วโมงสุดท้ายสำหรับ freeze evidence, report/slides/demo/ZIP และตรวจชุดส่ง.
   แผนแชมป์วัดเวลาคงเหลือจาก 2026-10-03T19:05:05Z โดยประมาณ:
   compute cutoff 4 ต.ค. 22:05:05 ไทย / working deadline 5 ต.ค. 02:05:05 ไทย.
   เป็นเวลาใช้งานภายในจากคำว่าเหลือ24ชั่วโมง **ไม่ใช่เวลาส่งทางการที่ยืนยันแล้ว**;
   ไม่เริ่มนับ24ชั่วโมงใหม่เมื่อคนต่อไปเข้ามา. เก็บเอกสารเวลาเก่าเป็นประวัติ.

คำสั่งรอบนี้: `python3 -B scripts/study/aom_ready_csv.py measure` (WSL).
**ห้ามรันซ้ำใส่ directory เดิม**: intake/measure/report ทุกตัวสร้าง output แบบ exclusive.
ตรวจหลักฐานที่มีด้วย `python3 -B scripts/study/aom_ready_csv.py verify`.
Producer นี้รับ Csv packet เท่านั้น; bug ใหม่ต้องมี intake/plan ที่ pin ของตัวเอง ไม่แก้ plan ที่ seal แล้ว.

อ่านข้อความคัดลอกส่งทีมใน [AOM_TO_TEAM_MESSAGE_TH.md](AOM_TO_TEAM_MESSAGE_TH.md).
