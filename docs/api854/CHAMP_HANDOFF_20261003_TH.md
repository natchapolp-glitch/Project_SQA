# ส่งต่องานแชมป์ — API854 / 3 ตุลาคม 2569

## สิ่งที่ทีมดึงไปเชื่อมได้

Branch `champ` มี KKU client, exact model selection, quota ledger แบบเครื่องเดียว,
fixed-context exporter, generation evidence และ queue adapter schema 1.0
อ้างอิงคิวออม commit `2e419e421c2fcc75dbfd516e04ed8ce27aafa463`.
ตรวจออฟไลน์ 68 tests ผ่านก่อนส่งต่อชุดแรก; ไม่มีผล mock รวมใน primary study.
คำสั่งตรวจและ interfaces อยู่ใน [API_SETUP_TH.md](API_SETUP_TH.md).

- **ออมใช้:** `GenerationJob`, `GenerationWorker.generate()`,
  `ChampQueueClient`, `QueueGenerationHandoff` และ model-selection manifest.
  ต้องรวม `claude-sonnet-5` / `gemini-3.5-flash-lite` เข้า frozen protocol.
- **บีมใช้:** `export_context()` รับ fixed checkout และรายการไฟล์ที่เลือกชัดเจน;
  generation result มี `source_blocks` ตามลำดับและ hashes.
  `suite_resolver(result) -> Path` ต้องคืน `suite.tar.bz2` ตาม policy ที่ตรึง;
  แชมป์จะอัปโหลด archive นี้และใช้ SHA-256 ของ bytes ที่ส่งจริง.
- `queue_client.py` นำจากออมมาเป็น dependency ไม่ได้เปลี่ยนต้นฉบับ.
  ออมรวม branch นี้แล้วควรตรวจไฟล์นั้นตรงกันก่อน integration.

## สิ่งที่รอจากทีม

| เจ้าของ | สิ่งที่ต้องส่งก่อน live pilot |
| --- | --- |
| ออม | frozen protocol/hash, pilot jobs, target/prompt/token budgets และ job payload contract |
| บีม | target/context selection, adapter readiness, suite resolver และ frozen processing policy |
| แชมป์ | actual remaining quota และหลักฐาน bucket/reset ของโมเดลที่เลือก |
| ทั้งทีม | เลือกผู้ส่ง API ต่อบัญชีเพียงเครื่องเดียว หรือใช้ global limiter กลาง; lease renewal/recovery และ end-to-end pilot |

คิวพร้อมเชื่อมไม่ยืนยันว่า protocol/adapters พร้อม. ณ จุดส่งต่อชุดแรกยังไม่พบ
ผลรัน API854 ใน local `results/study/`; ไม่อ้างว่า 854 bugs เริ่มหรือเสร็จแล้ว.
โควตา 200k/350k เป็น planning input ไม่ใช่ remaining จริงของ Sonnet 5/Flash Lite.
แชมป์มี allocation 285 bugs / 1,140 job keys; รายการแบ่งงานไม่ใช่ผลทดลอง.

## การรับงานโดยไม่ชนกัน

แต่ละคนทำบน branch ของตน: `champ`, `beam`, `aom`; ออม review และรวมเข้า `test`.
ให้ review/cherry-pick หรือ merge commit ของ `champ` ตาม workflow ของทีม;
อย่าทับไฟล์ protocol/queue ด้วย draft เก่า. Credentials และ runtime `.local/`
ไม่อยู่ใน commit. ไม่มีการส่ง key/token ในเอกสารนี้.

คิวหมด lease ระหว่าง generation ต้อง reconcile ห้ามส่ง KKU ซ้ำโดยเดา.
การขาด suite resolver จะคง publication pending ไม่ถือว่า generated/usable.
