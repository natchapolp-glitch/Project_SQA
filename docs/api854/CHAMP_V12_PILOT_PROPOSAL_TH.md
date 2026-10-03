# ข้อเสนอ Champ: ทดลอง development แยกจาก primary Gate A

เสนอเพื่อให้ทีมตัดสิน ไม่ใช่คำอนุมัติเปิด. Primary requirement691และ v12
403selected/288exclusionsคงเดิม. ข้อเสนอนี้ทดสอบ pipeline/fixture/oracle ของ
สอง bugs ด้วยทั้งสี่วิธี ก่อนขยายการทดลอง; ผลไม่เพียงพอสำหรับสรุปเปรียบเทียบเชิงสถิติ.

## ขอบเขตที่เสนอ

ใช้ **Chart-1 เฉพาะ inherited Graphics7** และ **Time-1 เฉพาะ Chronology6**.
Exact identities13รายการอยู่ใน
`output/api854-20261004/champ-v12-pilot-proposal-v1/proposal.json` พร้อม immutable source refs.
Chartใช้ bounded24cases/canvas64×64/lifecycle/oracleที่รับใน shared integrationv5.
Timeใช้ bounded13cases, real ISO/Buddhist chronology เฉพาะUTC/+07:00 และ
protected/internal invocation contractที่รับใน shared Chronology. ไม่ขยาย legal domainsเอง.
Chart8เดิมและ Timeรายการอื่นคงเป็น regression evidence แต่ไม่ใช่ selected pilot targets.

สร้าง condition/policy/checker/preparation/prompts ใหม่สำหรับ subset13ก่อน;
ทั้งสี่วิธีต้องใช้ exact context/targets/recipesชุดเดียวกัน. Worksheetใหม่มี
2bugs×2models=4คู่; ห้ามใช้ token reserveของ20bugs/v12แทน conditionใหม่.
รายงาน subset13แยกจาก primary691โดยไม่ลบ declarationsออกจาก primary denominator.

หนึ่ง development runต่อbug×approach: **8suites** = CMA-ES/FSCS-ART/Sonnet/Flash Lite.
ใช้ generator seed101/repeat1, algorithm input budget30, normalized bounds[-1,1],
fixed observations2ต่อproposal, CMA sigma0.3/populationสูตรเดิม, FSCS candidate-set10.
Suite cap30 methods; เกินให้ rejectทั้งsuite. AI temperature0/output cap4096,
one requestต่อbug/model, **รวม4generationrequests**, output capรวม16384tokens;
input/framing budgetยังnullจนมีการวัดและ quota admission. ไม่มี semantic retry,
account/model fallback หรือแก้ assertion/prune test เพื่อให้ผ่าน fixed.

ทุกsuiteที่ถูกต้องรัน fixedสองรอบ, buggy, coverage; สูงสุด32evaluationstagesสำหรับ8suites.
Observation timeout10s/command timeout900sตามฐาน. Timeout/invalid suiteเป็นผลจริงที่เก็บไว้;
ห้ามสร้างsuiteใหม่แทนเพื่อเลือกผลที่ดี. เก็บ executed/skipped/target_checksและทุก failure.
JDI method entryเรียกตามชนิดหลักฐาน; ห้ามแปลงเป็น line/branch percentage.
ต้องมี actual coverage instrumentation/metric contractร่วมกันก่อนเปิด หากจะรายงาน line/branch.
Chart reference fault=false; ไม่บังคับ AI/algorithmต้องพบfault. Time arrays_bad_order
เป็น reference negative control; ผลexperimentรายงานตามtestที่สร้างจริง.

## เงื่อนไขก่อนเปิดและหยุด

ทั้งสามคนต้องรับ exact targets/domains/oracles และลงคำรับ policyนี้. ออม compose
condition/prepare/checkerใหม่โดยคง primary gateblocked; บีมกับแชมป์ตรวจ consumers/pins.
มี final host/dependency/lease/lock/routing receipts: Champ API coordinator,
Aom CPU1สำหรับ Champ-ownedสองbugs; Beam hostต้องรับเฉพาะบทบาทที่ใช้จริง.
ต้องตรวจ dependenciesและ evaluator actual Defects4J execution ของ conditionใหม่,
ไม่ใช้เพียง standalone/native reference proofแทน.

Provider contractต้องมี exact IDs, effective settings, context/output limits,
actual input/framing4คู่, quota bucket/remaining/reset/expiry และ reserveผูก frozen bytes.
แยก byte guardกับtoken admissionอย่างชัดเจน. ทำ manual assignmentเฉพาะบัญชีที่รับหลักฐาน;
ห้าม switchบัญชีเงียบ ๆ. Metadataที่ตรวจแล้วไม่ใช่คำรับquotaหรือgeneration.

หยุดเมื่อ pinsเปลี่ยน, input/host/oracleไม่ตรง, credential/expiry/quotaไม่ผ่าน,
responseไม่มี trustworthy usage/quota, 401/429/5xx/transport outcomeunknown,
fixture/setup/fatal error, counters/coverage contractผิด หรือเกิน token/request budget.
Unknown outcomeต้อง reconcileก่อนส่งซ้ำ. Failed/rejected/timeoutsเก็บ artifactsและสถานะจริง;
ไม่รวม setup failureเป็นfaultและไม่แก้ raw response. เจ้าของทีมอนุมัติ recoveryต่อconditionนี้.

ก่อนรันต้อง freeze raw prompts/runtime/protocol/index/runner/test packaging hashesและงบ;
หลังรันต้องมี immutable raw/processedresponse, usage/quota/requestevidence, command/stdout/stderr,
fixed/buggy/coverage/countersต่อsuite และสรุปทั้ง8ผลรวมinvalid/timeout.
ผลอยู่ development namespace แยกจาก primary result table. ขยายจำนวนbugs/repeatsต้องออก
conditionและงบที่รับใหม่ก่อน; ไม่ใช้ runเดียวนี้อ้างผลวิชาครบหรือผ่าน primary Gate A.

ถ้าทีมเลือกคง primary691ก่อนทุกexperiment ให้บันทึกคำตัดสินและตรวจ288exclusionsต่อ.
ข้อเสนอนี้เพียงทำทางเลือก developmentให้ reviewได้ ยังไม่เปลี่ยน flags/runner/queue/checker.
