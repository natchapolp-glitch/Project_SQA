# ข้อความส่งต่อจากออม

ออม push ชุด shared prepare v3 บน branch aom แล้ว ให้ดึง commit ล่าสุดพร้อม
[บันทึกส่งมอบ](AOM_PREPARE_V3_HANDOFF_TH.md). มี artifacts 20 bugs, targets 691 รายการ,
exclusions 3 รายการ และ Chart receiver partition พร้อม CPU/API/bridge/evaluator binding.
Prompt ใหญ่สุดใหม่ 161,982 UTF-8 bytes. ZIP พร้อม SHA-256 อยู่ output/api854-20261003/.
ยังไม่ frozen primary และไม่เปิด live pilot; core queue 80 งานยัง prepare/queued/0 attempts.

## ส่งให้แชมป์

ดึง aom ไปตรวจ shared-v3 policy/runner/runtime hashes และ composition tests.
ใช้ prepare-v3/index.json, prepare-v3-prompts.csv และ prepare-v3-capacity.json
คำนวณ prompt reserve ใหม่รวม provider framing; ไม่ใช้ 99,439 หรือ 115,826 ของรุ่นก่อน.
ส่ง actual model/settings/context/output limits พร้อม remaining/unit/bucket/window/expiry,
observed time และ evidence/hash ตาม provider-evidence.template.json. ยืนยัน champ-pc1 readiness
และ team review. ไม่ส่ง API keys และยังไม่เริ่ม --once.

## ส่งให้บีม

Discovery ชุด 44dd5cb0 import ครบแล้ว ไม่ต้องส่ง inventory เดิมซ้ำ.
ขอตรวจ shared-v3 targets/fixtures/exclusions และ Chart receiver mapping แล้วปิด meaningful
fixture/oracle/target execution review โดยเฉพาะ Closure-176/JxPath-1.
แจ้ง executed/skipped counts ที่มีหลักฐาน; หากยังไม่ทราบคง null. ผลเดิม usable=false
และทั้งคู่ fault_detected=false ตามจริง. ถ้าเปลี่ยน fixture/generator policy ให้ส่ง version/diff/hash
และหลักฐาน fixed twice/buggy/coverage ใหม่ ไม่ซ่อม assertion เดิม.
ยืนยัน readiness ของ beam-pc1/2/3 และตรวจ runner/policy ร่วมกัน.

## ดึงงานโดยเก็บงานเดิมไว้

```bash
git fetch origin refs/heads/aom:refs/remotes/origin/aom
git merge origin/aom
python3 -m unittest discover -s scripts/study/api854/tests -t .
```

ตรวจ SHA-256 ของ ZIP/index/policy ตาม commit ที่ออมแจ้ง ไม่เติม proposal fields เพื่อข้าม gate.
เมื่อ semantic/provider/host evidence และ review สามคนครบ ออมจึงตรึง primary protocol/runner
bytes/hash และ seed run ใหม่. preparation-only core เดิมยังคงไว้เป็น evidence แยก.
