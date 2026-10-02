# Beam preparation สำหรับ core-frozen ของออม

Run `api854-pilot-preflight-20261003-v1` มีงาน Beam 24 keys จาก 6 bugs.
ไฟล์ `experiments/configs/api854-20261003/protocol.core-frozen.json` ต้องมี SHA-256
`675a480915c40ab19f7be57b56b046fb8c8924e7330e4cc8956b2ed3de6d31ad`.
เก็บไฟล์นี้ตาม bytes เดิม; `core_preflight.bind` สร้าง local execution binding แยก
และบันทึก hashes ของ implementation ที่ใช้ในแต่ละ attempt.

binding นี้เปิดเฉพาะ prepare ของ run/hash ดังกล่าว ไม่ใช่ frozen primary protocol.
seed/budget/cap/timeouts/timezone อ่านจาก core; target discovery ใช้
`shared-declaration-signatures-v1` และ context export ใช้ `modified-java-and-root-build-v1`.
ทั้งสอง policy เป็น implementation ที่บันทึกให้ทีมตรวจรับเพิ่มเติม ไม่อ้างว่า Gate A ผ่าน.

รันใน WSL โดยใช้ CPU slot เดียวกับ workers อื่น:

```bash
source /home/beam/sqa-beam/environment.sh
python3 -m scripts.study.api854.queue_worker \
  --protocol experiments/configs/api854-20261003/protocol.core-frozen.json \
  --run-id api854-pilot-preflight-20261003-v1 --condition preflight \
  --stage prepare --approaches cmaes fscs-art kku-claude kku-gemini \
  --worktrees /home/beam/sqa-beam/worktrees \
  --output output/api854-beam/live-prepare/attempt-01
```

หนึ่ง invocation รับหนึ่ง stage; ใช้ output ใหม่ทุกครั้ง. ไม่ replay claim/upload/complete
ที่ผลไม่ชัดเจน และไม่ claim ต่อจนตรวจ pending journal. worker ตรวจ enabled_stages,
run/hash/owner, snapshot implementation, checkout/build fixed+buggy, common targets,
fixed-source hashes และ context. อัปโหลด result, evidence bundle, context-manifest,
targets พร้อม receipt; metadata เก็บ implementation/fixed source/targets/context hashes
และ `approval_state=local_execution_binding_pending_three_owner_review`.

Preflight ไม่สร้าง prompt ที่ถือว่าผ่านการตรวจรับ และไม่ส่ง KKU request.
Primary API CLI ของแชมป์ต้องได้ context/prompt policies ที่ตรึงและ individual prompt.md,
model/settings/quota evidence พร้อม suite resolver/handoff ที่ส่ง lineage ให้ evaluator.
เมื่อทีมยอมรับแล้วต้องสร้าง protocol และ run ใหม่ตาม PILOT_OPENING_TH.md.

หลักฐาน adapters หกบัค: [beam-pilot-adapters-20261003.json](evidence/beam-pilot-adapters-20261003.json).
ผล local four-method evaluator: [beam-local-queue-smoke-20261003.json](evidence/beam-local-queue-smoke-20261003.json).
ผลเหล่านี้ยังมี semantic validity pending และไม่รวมเป็นผล pilot จริง.
