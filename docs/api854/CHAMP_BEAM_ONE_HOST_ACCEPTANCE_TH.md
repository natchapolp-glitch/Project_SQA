# แชมป์รับงาน Beam 3c7c62b8: เครื่องเดียวและ local review 5/20

รวม `beam 3c7c62b8` ต่อจาก `champ 64418ca0` ในช่วง offline
รับหลักฐานใหม่ Codec-1, Collections-1 และ Csv-1 พร้อมแผน `beam-pc1` เครื่องเดียว CPU 1 slot
ยังไม่มีการตรวจรับ semantic/shared contract ร่วมทีม ไม่เปิด live pilot และไม่มี KKU API request

## หลักฐานที่ตรวจแล้ว

- ตรวจ SHA-256 ของ packet เดิม 221 files, scalar packet ใหม่ 242 files และ host packet 16 files
  รวมทั้ง runtime snapshots ที่ใช้สร้างผลจริง, suite archives และความสัมพันธ์ semantic review กับ original result
- มี local development suites รวม 10 suites จาก 5 bugs: Closure-176, JxPath-1, Codec-1, Collections-1 และ Csv-1
  ทั้ง 6 suites ใหม่มี 30 tests ต่อ suite; fixed สองรอบ, buggy และ coverage รัน stage ละ 30, skip 0, target checks 30
  Fixed ผ่านสองรอบ; buggy ของสาม bugs ใหม่มี failure 0 และ `fault_detected=false` ตามผลจริง
- Local semantic review ของ 10 suites มี verdict valid แต่ `team_or_primary_approval=false`
  Original evaluation results ยังคง `usable=false` โดยไม่เขียนผลตรวจย้อนหลัง
  หลักฐานเหล่านี้เป็น development conditions ของ FSCS-ART/CMA-ES ยังไม่อนุมัติ shared-v3 หรือ KKU suites
- ตรวจ shared prepare v3 ครบ 20 bugs / 691 declarations / 3 fixed-only exclusions / 243 checksums
  แผนใหม่ครอบคลุม 10,248 stage keys ไม่มีช่องว่าง และ assignments ของแชมป์/ออมเหมือนแผนเดิม
- รับ host evidence ของ `beam-pc1`: WSL/Linux, Java/Javac 11, Defects4J 3.0.1 และ CPU 1 slot
  หลักฐาน flock ระหว่าง processes ระบุ worker ที่สองถูกปฏิเสธ exit 9 และ slot ใช้ได้อีกหลัง release exit 0
  CPU workers บนเครื่องบีมต้องใช้ root `/home/beam/sqa-beam/worktrees` ร่วมกัน
  หลักฐานนี้ส่งจากเครื่องบีม แชมป์ไม่ได้รัน Defects4J experiments ซ้ำบนเครื่องแชมป์

| Bug | Approach | Lines covered/total | Branches covered/total | Fault detected |
|---|---|---:|---:|---|
| Codec-1 | FSCS-ART | 154/256 | 64/184 | false |
| Codec-1 | CMA-ES | 149/256 | 57/184 | false |
| Collections-1 | FSCS-ART | 117/495 | 68/376 | false |
| Collections-1 | CMA-ES | 103/495 | 61/376 | false |
| Csv-1 | FSCS-ART | 20/37 | 4/26 | false |
| Csv-1 | CMA-ES | 20/37 | 4/26 | false |

## Plan และ runtime ที่ส่งให้ออมตรวจต่อ

ใช้คู่ proposal ต่อไปนี้สำหรับการตรวจรับ:

- [runner-plan.beam-one-host.v2.json](../../experiments/configs/api854-20261003/runner-plan.beam-one-host.v2.json)
  SHA-256 `7bc100a2baa9c3db69e0c2cd5146360b099fd5c17a78921b52c0fd6bd9c96c56`
- [champ-beam-one-host.v2.proposal.json](../../experiments/configs/api854-20261003/champ-beam-one-host.v2.proposal.json)
  Pin source hashes ของ runtime ที่รวมแล้ว พร้อม `.sha256` แยกจาก proposal ต้นฉบับของบีม
- [confirmed-plan-constraints.beam-one-host.v2.json](../../experiments/configs/api854-20261003/confirmed-plan-constraints.beam-one-host.v2.json)
  บีมยืนยัน 1 host / 1 CPU slot; CPU รวม 3 เป็น planning target ที่ยังต้องตรวจ host ของออม

แผนใหม่นี้ยังเป็น proposal และต้องให้ออม/ทีมรับก่อนใช้แทน runner v1
`beam-pc2` และ `beam-pc3` ไม่อยู่ในแผนใหม่; assignment lookup ของทั้งสองถูกปฏิเสธ
ตรวจ champ-pc1 เป็น API coordinator ของ owners champ/beam/aom กับคู่ proposal นี้แล้ว
off-assignment claim และ primary routing ถูกบล็อกก่อน HTTP; API worker ปฏิเสธ protocol ที่ยังไม่ frozen
ไม่มี claim/upload/complete ของ live queue ในการตรวจครั้งนี้

รวม scalar fixtures v4, versioned prompt export และ runner/resolver แล้ว พร้อมรักษา guard ก่อน claim:
การใส่ explicit fixture flag ให้ shared-v3 เดิมถูกปฏิเสธ ต้องมี shared prepare/prompt/recipe contract รุ่นใหม่ที่ทีมตรวจร่วมกัน
v4 ไม่ได้เปิดใน primary/shared-v3; `protocol.json`, frozen core, runner v1, proposal เก่า และ prepare-v3 bytes คงเดิม
Proposal นี้ยังไม่ได้อนุมัติ stages ของ primary; core-preflight เดิมเปิดเฉพาะ prepare ตามขอบเขตเดิม

## Validation และขอบเขต

ผลชุดทดสอบรวมและ log hashes อยู่ใน [validation receipt](../../output/api854-provider-preflight-20261003/champ-beam-one-host-validation-v1.json)
API854 รัน 236 tests ผ่าน 235 / skip 1 เรื่อง Windows symlink privilege; legacy/Java ผ่าน 30 tests
รวมผ่าน 265 tests ไม่มี failures/errors ในผลตรวจซ้ำ ชุดตรวจเฉพาะ guard/plan ผ่าน 19 tests
รอบแรกพบ WinError 10053 ใน loopback wrong-token request ของ service test เดิม
ทดสอบ service แยก 5 tests และรัน API854 ทั้งชุดซ้ำผ่าน โดยไม่ได้แก้ source และยังไม่ยืนยันสาเหตุของ socket abort
ชุด API854 ใช้ isolated Store/mock provider; legacy evaluator/generator/Java probe ใช้ JDK จริง
ชุดทดสอบนี้ไม่ใช่การรัน Defects4J pilot ซ้ำบนเครื่องแชมป์

หลักฐานตรวจรับใหม่:

- [Shared-v3/one-host audit](../../output/api854-provider-preflight-20261003/champ-beam-one-host-shared-v3-audit-v1.json)
- [Suite/host evidence audit](../../output/api854-provider-preflight-20261003/champ-beam-one-host-evidence-audit-v1.json)
- [champ-pc1 assignment acceptance](../../output/api854-provider-preflight-20261003/champ-pc1-one-host-runner-acceptance-v1.json)
- [Beam one-host handoff](BEAM_ONE_HOST_V3_REVIEW_TH.md)

รอบนี้ไม่ได้อ่านหรือแก้คิวจริง สถานะคิวล่าสุดที่มีหลักฐานคือ GET เมื่อ 2026-10-03 06:12:17 เวลาไทย
80 prepare/queued, attempts=0 และ core hash `675a480915c40ab19f7be57b56b046fb8c8924e7330e4cc8956b2ed3de6d31ad`
การตรวจครั้งนี้มี `real_kku_requests=0` และ `live_queue_mutations=0`

## งานที่ยังต้องทำก่อน Gate A

1. บีม: fixture/oracle evidence อีก 15 bugs ได้แก่ Chart-1, Cli-1, Closure-1, Compress-1, Gson-1,
   JacksonCore-1, JacksonDatabind-1, JacksonDatabind-112, JacksonXml-1, Jsoup-1, JxPath-22,
   Lang-1, Math-1, Mockito-1 และ Time-1; ร่วมตรวจ semantic และ shared contract ของทั้งสี่ approaches
2. แชมป์/เจ้าของบัญชี: actual model settings/limits/framing และ quota remaining/unit/bucket/window/expiry พร้อมหลักฐาน
   Temperature 0/output 4096 ยังเป็นข้อเสนอ; final reserve ยังคง null
   Prompt สูงสุด 161,982 bytes เป็นของ shared-v3 เดิม หากรับ recipes/contract ใหม่ต้องวัด prompt ใหม่
   สูตร byte guard เดิมคือ reserve >= 161982 + H และ request reservation >= 166078 + H เมื่อเสนอ output 4096
   ค่า H ยังไม่ทราบ และตัวเลขนี้ไม่ใช่ provider token count หรือ observed quota consumption
3. ออม/ทีม: ตรวจคู่ one-host proposal กับ source hashes หลังดึงงาน, รับหลักฐาน host ที่เหลือ,
   ตกลง shared contract และลง Gate A ร่วมกัน จากนั้นออมจึงตรึง primary bytes/hash และสร้างคิวรอบใหม่
   ไม่แก้ core-preflight 80 งานย้อนหลัง และยังไม่เริ่ม live pilot
