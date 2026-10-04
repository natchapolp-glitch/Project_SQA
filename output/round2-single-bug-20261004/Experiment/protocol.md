# วิธีทดลอง Csv-1 บั๊กเดียว

เลือกบั๊ก Csv-1 และ target class `org.apache.commons.csv.ExtendedBufferedReader`.
เปรียบเทียบ CMA-ES, FSCS-ART, KKU Sonnet5 และ Gemini3.5FlashLite ด้วยหนึ่งชุดที่สร้างจริงต่อวิธี.
ตัวเลข tests คือ JUnit method entries ในแต่ละ suite ไม่ใช่จำนวนบั๊กหรือ unique input scenarios.

1. Algorithms ใช้ seed101/budget30 proposed vectors และ frozen fixture policy v12.
   เลือก6 exact method declarations, ใช้ fixed observationsสองครั้งสร้าง expected oracle.
   CMA-ES objective เป็นความถี่ fixed behavior; coverageไม่ได้ใช้เป็น fitness.
   FSCS-ART เลือก candidate ที่มีระยะห่างจากจุดเดิมใกล้ที่สุดมากสุด, candidate set10.
2. AIผ่าน KKU IntelSphere เท่านั้น. ส่ง prompt/contextเดียวกัน, temperature0/output4096;
   Sonnet Messages thinkingdisabled, Gemini Chat Completions. เก็บ raw responses/settings/model IDs.
   Contextของชุดนี้ใช้ fixed source/embedded helper; จึงเป็น fixed-assisted regression study.
3. Extract Javaจากresponseโดยไม่แก้ assertions ของชุดที่เลือก. เก็บ original files/hashes.
4. รันบน Defects4J3.0.1/Java11/TZ America/Los_Angeles: fixed1, fixed2, buggy และ coverage.
   Suiteเดิมทุกstage; fixed failures0เป็นเงื่อนไขของการนับfault.
   Buggy assertion failuresต้องสัมพันธ์กับofficialpatch; compile/environment failuresไม่ใช่fault.
5. Coverageวัด target classด้วย Cobertura. ตารางใช้ canonical Beam counted records,
   XML actual starts30/30/21/18 และ skipped0. Host replayอีกเครื่องไม่ใช่independentgenerationrepeat.
6. Native buggy sourceต่างจากactual reconstructedCsv-1b. ใช้ exact benchmarkhash/sourcebinding
   ที่ประกาศไว้ใน Experiment/source-bindings, ไม่แก้productionหรือassertionsให้ผ่าน.
7. แสดงgeneration/API wall timeแยกจากD4Jevaluation time เพราะใช้คนละhost/ขั้นตอน.
   Missing metricเป็นnull. ไม่สรุปspeed superiorityจากผลนี้.
8. ขอบเขตเพียงหนึ่งบั๊ก/หนึ่งclassและbounded inputs; ไม่อ้างครบ17projects/854bugs.

Provider label checkเดิมไม่รับชื่อMessages provider. ResponseSonnetเดิมถูกreconcileในconditionที่ประกาศไว้
โดยไม่ส่งคำขอซ้ำ; receiptอยู่ใน retained-attempts. ความผิดพลาดตรวจชื่อproviderไม่ใช่test failure.
ผลbaselineCsvเก่า truncated/fixed-invalid และ strict source mismatchเก็บอยู่ในrepoต้นฉบับ
ภายใต้ aom-ready-csv-d4j-v1/aom-ready-messages-d4j-v1 ไม่โอนมาเป็นผลผ่านของตารางนี้.
