# แผนส่งงานภายใน 24 ชั่วโมง — ข้อเสนอเพื่อรับร่วม

ผู้ใช้แจ้งเวลาเหลือ24ชั่วโมง วันที่4ตุลาคม2026. เอกสารนี้เป็นแผนจัดลำดับงาน;
ไม่อนุมัติ live pilot ไม่เปลี่ยน requirement854/primary Gate A และไม่อ้างผลที่ยังไม่ได้รัน.
ตัวเลข12–24ชั่วโมงที่เคยประเมินสำหรับ20bugsยังไม่ใช่ measured end-to-end runtime.

## สถานะและกำลังที่ตรวจได้

- Ownership/installed active-ID exportตรงกัน:854bugs/17projects,
  Champ285/Beam285/Aom284. Allocationไม่ใช่ adapter/readinessหรือผลทดลอง.
- Full854×4=3416suitesต่อrepeat; AI854×2=1708generationrequestsต่อrepeat.
- Current preparationของ Aom `63ad1956` / documentation-only continuation `31324545`
  มี20bugsครอบคลุม17projectsทั้งหมด,403selected/288exclusions/691declarations.
  Peer Champ review `c1daf015`/Thai handoff fix `bd0ac894` ยืนยัน40prompt-model pairs
  และ current model IDs; final reserve/limits/quota/framing/expiryยังไม่ยืนยัน.
- Current runner: Beam CPU1 และ Aom CPU1 (รับ CPUงาน Champด้วย); Champเป็น APIcoordinator.
  Account generation assignเฉพาะa01/one outstanding. APIglobal ceiling2ไม่ใช่ current concurrency2
  เมื่อมี active accountเพียงบัญชีเดียว. Keys10ผ่าน model metadataแต่ยังไม่รับquota/expiry.
- Conditionใหม่ยังไม่มี primary/live experiment results. Historical Lang pilotและ
  standalone reference evidenceต้องเก็บ labelsเดิม ไม่รวมเป็นผล4วิธีของv12.

## Throughput ที่ต้องได้หากจะครบ854

เผื่อ4ชั่วโมงท้ายไว้สรุปผล/รายงาน/ZIP/demo/ส่ง เหลือ compute window20ชั่วโมง.
3416suitesบน2CPUslotsต้องใช้ CPUเฉลี่ยไม่เกิน **42.15วินาทีต่อsuite**;
3CPUslotsเพิ่มได้เป็น63.23วินาที. รวม checkout/compile/generationที่ใช้CPU/fixedสองรอบ/
buggy/coverage/setupที่ต้องใช้จริงด้วย. ไม่มี benchmarkยืนยันว่าทำได้.

| สมมติ CPU time เฉลี่ยต่อsuite | CPU slots ขั้นต่ำสำหรับ3416suites/20h |
|---|---:|
| 2 นาที | 6 |
| 5 นาที | 15 |
| 10 นาที | 29 |

ตารางเป็น scenario arithmetic ไม่ใช่เวลาวัด ไม่รวม capacityที่ยังไม่มีหรือเวลาทำadaptersเพิ่ม.
API1708requestsบน1slotต้องเฉลี่ยไม่เกิน42.15วินาที/request; ถ้า2slotsที่รับบัญชี/โควตาจริงแล้ว
จึงได้84.31วินาที. Full output cap1708×4096=6995968tokens **เป็นcap ไม่ใช่จำนวนใช้จริง**;
input/framing/quotaรวมยังไม่ทราบ. การมี10keysอย่างเดียวไม่พิสูจน์ว่ารันปริมาณนี้ได้.

## ลำดับงานที่เสนอ

**H0–H2:** หยุดเพิ่ม candidate/features/conditionรุ่นใหม่ที่ไม่จำเป็นต่อการเริ่มทดลอง.
ใช้ออมv12เป็นฐานรับตรวจ; Codec5เก็บ standalone packetต่อได้แต่ยังไม่ composeรุ่นใหม่ระหว่าง freeze.
ทั้งสามคนตกลง primary691กับ separate bounded development policyตามเอกสารออม;
ห้ามเพียง flip flag. ปิด exact semantic scope/host/dependencies/lease/coverage contract,
provider limits/counting/framing/quota/reset/expiry และ byte/token admissionที่เหลือ.
ออม compose protocol/checker/preparationเฉพาะ conditionที่รับแล้ว; แชมป์วัด reserveจาก frozenbytes.

**ทันทีที่เงื่อนไขผ่าน:** รัน development probeที่มีขอบเขตชัดและจับเวลาจริง.
ข้อเสนอ2bugs/13targetsอยู่ใน `CHAMP_V12_PILOT_PROPOSAL_TH.md`;
เป็นคนละconditionกับv12และไม่ถือว่า covered17projectsหรือครบ854.
ถ้าขยายเข้าชุด20bugs ให้ใช้ prospective deterministic cohortเดิมซึ่งครอบคลุม17projects,
รับ exact domains/condition/prompts/worksheetใหม่ร่วมกันก่อน ไม่สืบทอดคำรับ2bugsอัตโนมัติ.
ทุกbugใช้4วิธีภายใต้ conditionเดียวกัน; อย่าปะปนผลจากหลายtarget/fixtureconditionsในตารางเปรียบเทียบเดียว.

**H2–H20:** หลัง freezeให้ใช้เวลากับผลจริง. บีมรัน ownerงานบีม;
ออมรัน ownerงานออม+แชมป์ด้วยCPU1; แชมป์ส่ง AIของทุกownerผ่าน coordinatorที่รับ quotaแล้ว.
เก็บ rejected/compile failure/fixed failure/timeout/coverage failureตามจริง;
ไม่แก้assertions/prune tests/เปลี่ยนmodelเงียบ ๆเพื่อเพิ่มยอดpassed.
ใช้ measured CPU/queue/API timingsและ quotaคงเหลือตัดสินการขยายจำนวนbugs;
อนุญาตขยายเฉพาะ inputs/condition/hostsที่พร้อมและงบที่รับไว้ ไม่ถือว่า834bugsที่เหลือพร้อมเพราะมีinventory.

**H20–H24:** freeze evidenceสำหรับsubmission, ทำตาราง results/invalid/pendingจริง,
coverage/fault/execution time/token usageและlimitations; ผูกแต่ละแถวกับ hashes/logs,
เก็บ code/prompts/config/test suites/raw responses, ทำreport/slides/demoและตรวจส่งไฟล์.
งานที่รันไม่ทันใช้สถานะpending ไม่สร้างผลแทน. ถ้า854เป็นข้อบังคับที่ลดไม่ได้ ต้องรายงาน
capacitygapและข้อจำกัดให้ผู้ใช้ตัดสินเพิ่มเครื่อง/โควตาหรือขอขยายเวลาตั้งแต่ต้น.

หาก H2ยังเปิดไม่ได้ ให้ส่ง blockerที่ระบุ owner/action/evidenceอย่างเจาะจงทันที.
ไม่ใช้เวลาเงียบ ๆรับรอง candidatesเพิ่มโดยยังไม่มีการเริ่มทดลอง และไม่เรียก development subsetว่าfull854.

## ข้อความพร้อมส่ง

**ออม:** เหลือ24ชม. ขอใช้v12เป็นฐานปิด condition/policyสำหรับเริ่มทดลองก่อนเพิ่มCodecรุ่นใหม่.
ส่ง current Aom CPU1 receiptสำหรับ Aom+Champ routing และตกลง exact experiment scope/Gateกับทั้งทีม.
เตรียม results manifest/reportควบคู่; deadlineไม่ใช่คำอนุมัติให้ข้ามchecks.

**บีม:** เหลือ24ชม. ขอหยุดcandidateใหม่แล้วปิด v12 scoped semantic/consumers/Beam CPU1 receipt
กับ protocol/index/runner/runtime hashesปัจจุบัน. หลัง conditionรับแล้วให้รัน4วิธีและเก็บ timings/raw evidence;
ส่ง readinessและcapacitygapกลับทันที ไม่ต้องตรวจv10 closureซ้ำ.

**แชมป์:** ติดตาม provider/counting/quota/reserveและ same-condition4approach bindings,
จัด APIงานทุกownerตามบัญชีที่รับแล้ว, ไม่มี secret/keyhashในGit และแจ้งก่อนswitchบัญชีตามpolicy.
Proposalนี้ยังไม่เปิดคิว ไม่แก้flags และไม่เปลี่ยนownershipหรือprimaryresulttable.
