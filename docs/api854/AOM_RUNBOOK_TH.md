# ออม — เตรียมคิวและตรวจ snapshot

ทำจาก repository root ของ branch aom; Python standard library ไม่ต้องติดตั้ง service dependencies
เครื่อง Windows ใช้ Python; installed inventory export ใช้ WSL ที่มี Defects4J
โค้ดนี้เป็นเครื่องมือ orchestration ไม่ใช่ generator/evaluator ของแชมป์/บีม

## ตรวจและสร้าง manifests

```powershell
python -m unittest discover -s scripts/study/api854/tests -v
```

บน WSL:

```bash
python3 -m scripts.study.api854.inventory export-installed \
  --defects4j /home/aomsin/sqa-round2/defects4j/framework/bin/defects4j \
  --output experiments/configs/api854-20261003/installed-active-bugs.json
```

ปรับ --defects4j ให้ตรง installation จริงของเครื่อง export ตรวจ release จาก README และ revision
ไม่ใช้ `defects4j version` เพราะ Defects4J 3.0.1 ไม่มี subcommand นี้

```powershell
python -m scripts.study.api854.inventory prepare --ownership experiments/configs/api854-20261003/ownership.json --protocol experiments/configs/api854-20261003/protocol.json --installed experiments/configs/api854-20261003/installed-active-bugs.json --output experiments/configs/api854-20261003
python -m scripts.study.api854.service seed --jobs experiments/configs/api854-20261003/jobs.json --protocol experiments/configs/api854-20261003/protocol.json
```

สร้าง 3,416 keys เป็น held/not_attempted; seed ซ้ำไม่สร้าง jobs ซ้ำ
ถ้า protocol hash เปลี่ยนให้ใช้ --db ของ condition ใหม่ (global flag วางก่อน subcommand)

## Server และ gates

กำหนด API854_QUEUE_TOKEN ผ่าน secrets/environment ของเครื่อง อย่าใส่ค่าจริงลง Git/แชท

```powershell
python -m scripts.study.api854.service serve --protocol experiments/configs/api854-20261003/protocol.json
```

Default localhost:8540 ใช้ SSH tunnel จาก workers หรือ bind remote ด้วย --tls-cert/--tls-key จริง
ยังไม่เปิด service unattended อัตโนมัติจากการเขียนเอกสารนี้
ทีมเติม exact models/settings, freeze protocol, regenerate manifests และใช้ condition database ใหม่
สร้าง gate-a.json/gate-b.json จากหลักฐานจริง ห้ามแก้ example_only เป็น false โดยไม่มีการตรวจรับ

```powershell
python -m scripts.study.api854.service activate --gate A --gate-file .local/api854/gate-a.json --protocol experiments/configs/api854-20261003/protocol.json --bugs experiments/configs/api854-20261003/bugs.json --jobs experiments/configs/api854-20261003/jobs.json --pilot experiments/configs/api854-20261003/pilot20.json
```

Gate B ใช้คำสั่งเดียวกันเปลี่ยน --gate B และ --gate-file เป็นหลักฐาน Gate B
ออมไม่รับรอง quota/model/adapters แทนเจ้าของ; queue mock tests ไม่ใช่ Gate A/B live evidence

## Recovery

```powershell
python -m scripts.study.api854.service release-expired
python -m scripts.study.api854.service recover --resolution .local/api854/recovery.json
```

recovery.json เป็น arguments ของ Queue.recover: job_id, attempt_id, status, record, reason, evidence
ใช้ output ที่มีอยู่แล้วจาก interrupted attempt หลังยืนยัน workerเก่าหยุด ไม่มี request ใหม่
ถ้าต้อง retry infrastructure ใช้ requeue พร้อม --job-id --reason --evidence --decision
ดู retry caps และ exclusions ใน QUEUE_CONTRACT_TH.md; ห้าม requeue semantic failures ใน condition เดิม

## Snapshot/report/package

```powershell
python -m scripts.study.api854.service snapshot --output .local/api854/snapshot.json
python -m scripts.study.api854.report --snapshot .local/api854/snapshot.json --output output/api854-20261003/checkpoint
python -m scripts.study.api854.report --snapshot .local/api854/snapshot.json --output output/api854-20261003/frozen --freeze-zip output/api854-20261003/frozen-evidence.zip --config experiments/configs/api854-20261003/protocol.json --config experiments/configs/api854-20261003/bugs.json --config experiments/configs/api854-20261003/ownership.json --config experiments/configs/api854-20261003/installed-active-bugs.json
```

Freeze ZIP มี snapshot/config/evidence/SHA256SUMS.json ตรวจแตกได้และ hash ทุกไฟล์ พร้อม checksum ZIP
ไม่รวม SQLite/.env/secrets; scanner ตรวจ credential patterns และ emails แต่ทีมยังต้องตรวจ raw artifacts จริง
เก็บแค่ hash ที่มี file bytes ตรง ไม่เอา summary แทน evidence
รายงาน/สไลด์สุดท้ายต้องใช้ frozen snapshot_hash เดียวกัน
ปัจจุบัน output/api854-20261003/preparation เป็น checkpoint เตรียมงาน ไม่มี observed experiments

## Project regression checks

```powershell
python -m unittest discover -s scripts/study/tests -v
$env:PYTHONPATH='algorithms/python'
python -m unittest discover -s algorithms/python/tests -v
python -m unittest discover -s scripts/ai/tests -v
python -m unittest discover -s scripts/reporting -p test_aggregate.py -v
```

การทดสอบ algorithm ต้องใช้ PYTHONPATH ตาม README เดิม; ไม่ใช่ Defects4J full-cohort run
