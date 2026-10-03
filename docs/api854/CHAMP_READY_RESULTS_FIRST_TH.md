# เก็บผลจริงก่อนขยาย — Csv-1 ครบสี่วิธีแล้ว

เอกสารนี้เก็บbaselineแรกไว้. อ่าน [ผลและงานส่งต่อใหม่](CHAMP_READY_RESULTS_UPDATE_TH.md)
สำหรับCsvMessagesconditionที่ทั้ง4วิธีผ่านfixed, ClioraclequarantineและfullD4Jreplaywrapper.
ห้ามลบbaselineinvalidหรือpoolผลข้ามcondition.

ผู้ใช้ยืนยันวันที่ 4 ตุลาคม 2026 ว่า **“เก็บผลจริงครบ 4 วิธีจากชุดที่พร้อมก่อน
แล้วขยายจำนวน bugs ตามเวลาที่เหลือ”** และแจ้งเวลาเหลือ 24 ชั่วโมง.
คำตอบนี้อนุญาตงานเก็บผลจริงและใช้ KKU credentials ที่ให้ไว้ในขอบเขตงาน;
แผน deadline v1 เป็นบันทึกก่อนคำตอบนี้. เป้าหมาย full 854 ยังอยู่ใน backlog.

## ผลที่รันแล้ว

เริ่มด้วย Csv-1 / exact 6 declarations ของ `ExtendedBufferedReader` จาก Aom
`63ad195623c2ed3f67f3ae232c00c54d3160ce72`. ใช้ prompt/recipes/runtime v12 เดิม,
seed 101, cap 30 tests, temperature 0, max output 4096, non-streaming.
Condition แยก: `api854-20261004-csv-six-target-native-development-v1`.
เป็น native development บน Java17, production release7, UTC;
ยังไม่ใช่ primary หรือการรัน Defects4J เต็มขั้นตอนบน host ที่ทีมรับร่วม.

| วิธี | ผลจริง | Coverage / fault |
|---|---|---|
| CMA-ES | 30 tests; fixed สองรอบ, buggy, instrumented fixed ผ่านทุก stage; skip 0 | Target class 31/37 lines (83.78%), branches 50%; fault=false |
| FSCS-ART | 30 tests; fixed สองรอบ, buggy, instrumented fixed ผ่านทุก stage; skip 0 | Target class 31/37 lines (83.78%), branches 50%; fault=false |
| Claude Sonnet | finish_reason=length; ใช้ completion 4096 tokens; assistant content ว่าง | Invalid/truncated; fault/coverage unavailable |
| Gemini Flash Lite | Compile ผ่าน, 15 tests; fixed ทั้งสองรอบ failed 1 / skip 0 | ปฏิเสธทั้ง suite; ยังไม่รัน buggy/coverage |

Gemini ผิดที่ `testReadBufferWithNewlines`: หลังอ่าน `a\nb\r\nc` ครบ 6 ตัว
คาด `readAgain()` เป็น CR (13) แต่ผลจริงเป็น `c` (99). เก็บ raw response
และ Java ที่ extract ไว้เหมือนเดิม ไม่แก้ assertion ไม่ตัด test และไม่ส่ง feedback/retry.
ความผิดของ test บน fixed ไม่ใช่ fault ของ Csv.

คำว่า “ครบสี่วิธี” หมายถึง **มี outcome จริงของทั้งสี่**; มี valid measured suites 2,
invalid AI suites 2. ไม่มี full Defects4J evaluation หรือ primary results เพิ่ม.
83.78% เป็น coverage ของคลาสนี้เท่านั้น ไม่ใช่ทั้ง Csv, 403 declarations หรือ 854 bugs.
Counts เป็น JUnit ที่รันจริง; target checks ราย stage ยังไม่ได้เก็บแยกใน native v4.
AI input-domain equivalence/full semantic ยังไม่รับรอง.

## Provider และเวลา

KKU a01 ใช้ calibration สั้นสอง requests แล้ว generation จริงสอง requests ตามลำดับ.
Calibration พบ daily cap Sonnet 200000 / Gemini 350000 tokens จริง ณ เวลานั้น.
Generation Sonnet input/completion/total = 63672/4096/67768; คงเหลือ 132213.
Gemini = 41314/1287/42601; คงเหลือ 307393. ไม่ถือว่าอีกเก้า keys มี quota แยก
และไม่รวม quota ทั้งสิบโดยไม่มีหลักฐาน. Snapshot นี้ไม่ใช่ current quota ตลอดวัน.

Prompt 154511 bytes ไม่ใช่ tokens. Admission ใช้ byte bound + operator framing buffer
4096 + output cap4096 =162703; buffer นี้ **ไม่ใช่ measured framing/final v12 reserve**.
Standard token-count endpoint ที่ลองตอบ 404. Context/output limits, effective settings,
exact reset timezone/expiry และ final reserve ของ worksheet40pairs ยังไม่ยืนยันครบ.

Generation latency Sonnet 43.69s / Gemini 5.40s. Algorithm generation CMA-ES9.45s /
FSCS-ART8.60s; native command durations รวม44.77s. ตัวเลขนี้เป็น Csv native run นี้;
ไม่ใช้ประมาณเวลาทั้ง854หรือแทน measured full Defects4J throughput.

## หลักฐานและ attempts

- [ผลรวม CSV](../../output/api854-20261004/champ-csv-four-approach-summary-v1/results.csv)
- [Audit receipt](../../output/api854-20261004/champ-csv-four-approach-summary-v1/receipt.json)
- [Generation plan/receipt](../../output/api854-20261004/champ-csv-development-generation-v1/receipt.json)
- [Native v4 receipt](../../output/api854-20261004/champ-csv-native-measurement-v4/receipt.json)
- Algorithm archives: `champ-csv-native-measurement-v4/{cmaes,fscs-art}/packaged-suite/suite.tar.bz2`.
- Gemini rejected archive: `champ-csv-native-measurement-v4/kku-gemini/packaged-suite/suite.tar.bz2`.

ทุก packet มี checksums; auditตรวจ230 entries และ unchanged shared checkpoint100pins.
Retain native v1 missing-config failure, v2 algorithm CLI harness failures,
v3 coverage path/instrumentation failure และ successful v4. v2 ไม่ใช่ algorithm scientific
failure; v3 ไม่ใช่ detected fault. ไม่มี AI request เพิ่มระหว่างแก้ harness/rerun.
Champ shared workingtreeยัง v9; v12 execute จาก immutable Aom snapshot.

ดึง Beam ล่าสุด `92a3ee1b` แล้ว: มี v12 scoped consumers80/component/beam-pc1 CPU1 receipt
ตาม `BEAM_V12_CONSUMER_HOST_ACCEPTANCE_TH.md`. ใช้ตาม exact pins/scope ของ receipt;
ไม่ต้องรอ Beam v10 อีก และไม่ transfer เป็น full403 semantic/GateA.

## งานต่อทันที

1. Freeze Aom63ad1956 v12 สำหรับ ready-subset development; หยุด compose candidate ใหม่
   ระหว่างเก็บผล. Codec5/Collections10 เก็บ packet แยกไว้ก่อน.
2. ออม/บีมตรวจ Csv archives และรันผ่าน full Defects4J development evaluator บน host
   ที่รับแล้ว โดยไม่เปลี่ยน Java. เก็บ fixed สองรอบ/buggy/coverage/counters/commands/hashes
   และระบุ timezone/dependencies/runtime/host exact bindings. Invalid AI outcomeต้องคงอยู่.
3. แชมป์ขยาย bugs ที่ native compile/fixtures พร้อมและงบรับได้; ใช้ allocation บัญชี
   ที่ประกาศก่อน request. ไม่วนบัญชีเพื่อ retryผลไม่ผ่าน และไม่รวม quotaที่ยังไม่ตรวจ.
   หากเปลี่ยน transport/thinking/output/prompt ต้องเป็น condition ใหม่ เก็บ conditionนี้ครบ.
4. ใช้ cohort20เดิมที่ครอบคลุม17projectsเป็นลำดับถัดไป; แต่ละbugเก็บครบ4outcomes
   ก่อนนับเสร็จ. Full Defects4J pipeline และ native development แยกตาราง.
5. เผื่อ4ชั่วโมงสุดท้ายสำหรับ report/slides/demo/ZIP. แสดง invalid/pending และข้อจำกัด
   ตามจริง; ห้ามใช้ reference candidates เป็นผล algorithm/model หรืออ้างครบ854.

## ข้อความพร้อมส่งเพื่อน

**ออม:** ผู้ใช้เลือกเก็บผลจริงครบ4วิธีจากชุดพร้อมก่อน แล้วขยายตามเวลา24ชม.แล้วครับ.
Champมี Csv-1 native outcomesครบ4: CMA/FSCS30testsต่อวิธีผ่านทุกstage,
target coverage31/37lines/fault=false; Sonnettruncated, Geminifixedfail1ทั้งสองรอบ.
อ่าน CHAMP_READY_RESULTS_FIRST_TH.md แล้วขอ freeze Aom63ad1956 v12ก่อนเพิ่มCodec/Collections;
รับ Csv archives ไปรัน full Defects4J developmentบน AomCPU1หรือประสานBeam,
ส่ง currenthost/runner/condition receipt และ measuredtimingsกลับ. คงinvalidAI/rawtests,
ยังไม่เป็นprimaryหรือfull854. จากนั้นเก็บ4วิธีของready20bugsตามpinsเดียวกันครับ.

**บีม:** รับทราบ v12 receiptที่92a3ee1bแล้วครับ. ผู้ใช้เลือกผลจริงก่อนขยาย;
ขอพักcandidateใหม่แล้วตรวจ Csv-1 packet/archivesของChampร่วมออมและรัน fullDefects4J
developmentบนbeam-pc1ตามdeclaredcondition. มีCMA/FSCS30testsผ่านnative,
Sonnettruncated/Geminifixedfail1ต้องคงผลinvalid. ส่งcounts/coverage/timings/hosthashesกลับ
และเริ่มreadybugsของBeamในcohort20ให้ครบ4outcomes; ไม่ใช้Collectionsreferenceแทนผลmodelครับ.

แชมป์จะประสาน API และเก็บผลต่อ; **ยังไม่ได้ส่งข้อความหาเพื่อนผ่านแอปใด**.
