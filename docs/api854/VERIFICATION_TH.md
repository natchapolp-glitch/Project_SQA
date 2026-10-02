# ผลตรวจงานออม — 3 ตุลาคม 2569

Foundation checks เท่านั้น ไม่ใช่หลักฐานว่าได้ทำ API854 primary experiments หรือผ่าน Gate A/B

Final regression checks หลังแก้ review findings:

- `python -m unittest discover -s scripts/study/api854/tests -v`: 20 passed
- `python -m unittest discover -s scripts/study/tests -v`: 26 passed (รวม Java probe integration)
- `PYTHONPATH=algorithms/python python -m unittest discover -s algorithms/python/tests -v`: 9 passed
- `python -m unittest discover -s scripts/ai/tests -v`: 10 passed, 1 skipped
- `python -m unittest discover -s scripts/reporting -p test_aggregate.py -v`: 8 passed

รวม 74 tests: 73 passed, 1 skipped, 0 failures
Skipped: test_reject_symlink_packaging เพราะ Windows นี้ไม่อนุญาตสร้าง symlink
ครั้งแรก algorithm discovery ไม่มี PYTHONPATH จึง import ไม่ได้; final run ใช้ setting ตาม README แล้วผ่าน

ตรวจเพิ่มเติม:

- Actual installed Defects4J 3.0.1 pids/bids เทียบ exact IDs: 854 unique pairs / 17 projects
- Ownership counts: champ 285 / beam 285 / aom 284 bugs
- Job counts: 3,416 unique keys; aom 1,136 keys
- Inventory canonical hash ตรง bugs.json; protocol/job manifest hash ตรงกัน
- preparation-evidence-v2.zip: testzip ผ่าน และ verified payload hashes ครบ 9 files
- ZIP รวม snapshot, report/progress และ 6 configs จาก preparation snapshot เดียวกัน
- Frozen ZIP สร้างซ้ำจาก snapshot เดิมได้ bytes เหมือนกันใน test
- Git diff whitespace check ผ่าน; runtime SQLite/.env/.local ถูก ignore
- Generated JSON/report ใช้ LF บน Windows/WSL เพื่อให้ bytes และ checksums คงที่ข้าม platform

Focused tests ครอบคลุม simultaneous claims/restart/idempotent seed, expired fencing lease,
unknown-outcome quarantine, same-attempt recovery, forbidden semantic retries, foreign protocol/model,
evidence hashes, fixedสองครั้ง/same-suite/coverage validity, HTTP auth/upload/download,
generation cutoff, example gate rejection, non-pooled protocol conditions และ secret rejection ก่อน ZIP

ยังต้องตรวจบน integration จริง: KKU quota/auth/reset/visible notification, exact model/settings,
target adapter coverage, 80-job pilot, all-four-method validity, throughput/token measurement,
remote-host TLS/connectivity และ final report/slides/demo ตาม rubric
การทดสอบ HTTP นี้ใช้ localhost synthetic records ไม่ใช้ API tokens
