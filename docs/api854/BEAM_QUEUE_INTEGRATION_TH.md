# Beam queue / AI suite / evaluator integration — 2026-10-03

## โค้ดที่เชื่อมแล้ว

- `queue_worker.py`: รับงานที่ owner=beam ครั้งละหนึ่ง stage; prepare, CMA-ES/FSCS-ART
  generation และ evaluation ครบทุก approach ไม่มีการเรียก KKU โดย implicit
- `prepare_worker.py`: checkout/build/discover targets แล้ว export modified fixed Java
  และ known build files ที่ root ด้วย policy `modified-java-and-root-build-v1`
- `BeamQueueClient` ใช้ transport ของแชมป์และ QueueClient ของออม HTTP errors
  ไม่พิมพ์ response body/credentials; production ต้องเป็น explicit HTTPS origin
- `LeaseHeartbeat` ของแชมป์ renew ระหว่างงาน หยุด heartbeat และ renew อีกครั้งก่อน complete
- `FencedPublisher`: ตรวจ artifacts ทุกไฟล์ก่อน upload, ตรวจ receipt hash/size,
  บันทึก upload/complete intents ก่อน mutation และไม่ replay ถ้าผลไม่แน่นอน
- `download_generation`: ใช้ URI จาก generation stage history, ตรวจ hash, generation
  attempt ID และ fixed-source mapping กับ preparation; evaluator ใช้ attempt ID ใหม่
- `BeamSuiteResolver`: รับ `GenerationWorker` ของแชมป์โดยตรง ตั้งชื่อ Java ตาม public
  top-level class/package, คง bytes, ตรวจ source-block hashes และนับ JUnit4 annotations
  เพื่อจำกัด cap ไม่ตัด tests/แก้ oracle จำนวนนี้ไม่ใช่ executed/skipped count
- `BeamGenerationHandoff`: ส่ง suite, manifest, generation lineage, source map และ raw
  generation evidence พร้อม required AI metadata ของคิว ก่อน advance ไป evaluate

รวม dependency จาก `champ` commit `558b3641` และ `aom` commit `2e419e42`
โดยไม่ merge branch หรือแทนที่ Beam files/QueueClient ของออม Import receipt ระบุ full revisions
กับ source hashes ที่ `output/api854-beam/team-import-1.json`
ปรับ `generate_worker.py` ให้ prompt/response/source-blocks เขียน UTF-8 bytes โดยตรง
เพื่อไม่ให้ Windows แปลง newline จน stored bytes ไม่ตรง hash ที่คำนวณจากข้อความ
ไม่มีการแก้ source semantics หรือ regenerate response

## รันหนึ่ง stage หลังออม seed งานด้วย protocol ที่ตรงกัน

```bash
source "$HOME/sqa-beam/environment.sh"
python3 -m scripts.study.api854.queue_worker \
  --protocol /path/to/frozen-protocol.json --run-id TEAM_RUN_ID \
  --stage evaluate --approaches cmaes fscs-art kku-claude kku-gemini \
  --condition primary --worker-id beam-pc1 \
  --worktrees "$HOME/sqa-beam/worktrees" \
  --output /path/to/new-queue-operation
```

`--condition primary` ต้องมี `protocol.status="frozen"`; hash และ implementation dependencies
ต้องตรง ก่อน claim ถ้าคิวมี eligible jobs คนละ run/protocol จะหยุด เพราะ schema 1.0
ไม่มี filter run_id/protocol_hash ที่ endpoint claim ผล primary ใช้ `results/study/`
ส่วน development ใช้ run ID `beam-development-*` และ `results/validation/api854-beam/`
ถือ CPU slot เดียวต่อ host ทั้งงาน; output operation ต้องใหม่เสมอ ห้าม rerun เพื่อ replay mutation

เปลี่ยน `--stage prepare` ได้ทั้งสี่ approach; `--stage generate` อนุญาตเฉพาะ `cmaes fscs-art`
generation ของ KKU ต้องมี preflight/key/quota/prompt ที่ตรวจแล้ว และ caller ของแชมป์
ใช้ `GenerationWorker` กับ `BeamGenerationHandoff` โดยส่ง claim, heartbeat และ job เดียวกัน
`generate_with_lease()` เดิมของแชมป์จำกัด owner=champ หากเป็น owner=beam ให้ caller
เรียก `worker.generate(..., attempt_id=claim["attempt_id"])` ภายใน `with heartbeat:`
ห้ามสลับ owner หรือสร้าง attempt เองเพื่อหลบข้อจำกัด

## Interface สำหรับ caller ฝั่งแชมป์

```python
heartbeat = LeaseHeartbeat(queue_client, claim)
job = local_job(claim)
prepared = next(h for h in reversed(claim["job"]["payload"]["stage_history"])
                if h["stage"] == "prepare" and h["outcome"] == "prepared")["metadata"]
handoff = BeamGenerationHandoff(
    queue_client, claim, heartbeat=heartbeat, job=job, protocol=frozen_protocol,
    fixed_source_sha256=prepared["fixed_source_sha256"],
    context_source_hash=prepared["context_source_hash"],
    worker_id="ACCOUNT_OWNER_HOST", credential_secrets=(kku_client.account.api_key,),
)
# GenerationJob.source_hash ต้องเท่ากับ prepared context_source_hash
# prompt ต้องใช้ source bytes จาก prepare evidence/context manifest ไม่ใช้ evaluation logs
worker.handoff = handoff
with heartbeat:
    result = worker.generate(generation_job, attempt_id=claim["attempt_id"],
        prompt_token_reserve=frozen_prompt_reserve, max_tokens=frozen_output_budget,
        temperature=frozen_temperature)
```

Artifact `evidence.tar.bz2` ของ prepare มี `context/context-manifest.json`, `context/context.md`,
`context/fixed-source/` และ `setup/targets.json` พร้อม environment/build logs แชมป์ต้องเลือก
เฉพาะ fixed-source/context ที่ manifest ระบุไปทำ prompt ห้ามส่ง evidence bundle ทั้งก้อนไป AI
Root paths ใน metadata เป็นตำแหน่งบนเครื่องผู้ผลิต ใช้ artifact download จึงอ่านได้ข้ามเครื่อง

## ตรวจรับก่อน pilot 80 งาน

- Tests ใช้ `python3 -m unittest discover -s scripts/study/api854/tests -t . -v`
  ต้องมี `-t .` เพราะ tests ของแชมป์ import fixtures แบบ package
- Local HTTP server ของออม + Defects4J จริงใช้เป็น development evidence เท่านั้น
  AI transport ที่ mock ไม่ใช่หลักฐาน model/quota/fairness และส่งไป live queue ไม่ได้
- URL คิวเดิมเชื่อมต่อไม่ได้จากทั้ง Windows และ WSL ต่อมาผู้ใช้ให้ URL ใหม่
  authenticated health/schema/status พร้อมแล้ว: prepare 80 งาน, 0 attempts,
  owner beam 24 งาน ยังไม่ claim/upload ไปคิวจริง หลักฐาน `queue-current-2.json`
- ออมยังต้องส่ง frozen protocol/pilot selection และตรวจ controller/tunnel; แชมป์ตรวจ
  live models/remaining quota/account routing/prompt budgets แล้วจึงทำ actual AI smoke
- จำนวน executed/skipped/target checks และ fixture/oracle review ยังต้องมีหลักฐานจริง
  ก่อนตั้ง usable=true การ complete stage ไม่ใช่การรับรอง semantic validity
- หาก upload/complete intent มีแต่ไม่รู้ receipt ต้อง reconcile กับ status/artifacts ของออม
  ก่อนดำเนินการต่อ ห้ามยิง KKU หรือ completion ซ้ำอัตโนมัติ

## ผลรันจริงของ local integration

ผลที่ส่ง review อยู่ใน [evidence/beam-local-queue-smoke-20261003.json](evidence/beam-local-queue-smoke-20261003.json)
Tests 151 ผ่าน (api854 131, evaluator 16, generator 4) Local server ของออมมี
Lang-4 สี่ job keys ผ่าน 12 stage attempts prepare/generate/evaluate;
fixed passed twice, buggy พบ failures, coverage lines 12/25 และ branches 3/14
ทั้งสี่กรณี AI สองกรณีใช้ mock response จาก development suite จึงไม่ใช่ผลจากโมเดล
จริงและไม่ยืนยัน fairness/token/quota ไม่มี real KKU requests หรือ live queue mutations
ทุกกรณียัง usable=false และไม่ใช่ pilot/primary results
