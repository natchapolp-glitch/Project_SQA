# บีมส่งผลจริง Csv ครบ 4 วิธี และรับ scoped Cli quarantine — 4 ตุลาคม 2026

เปลี่ยนลำดับงานตามแผนใหม่ของทีม: **เก็บผลจริงจาก bugs ที่พร้อมก่อน แล้วขยายตามเวลา24ชม.**
พัก candidate/recipe ใหม่. รอบนี้ใช้ immutable Aom `63ad1956` v12และรับ Champ
`d98fcceee344edb9015854914c659722578d0ffe`; ไม่ผสม Aom v13หรือmanual reference candidates
ลงในผล algorithm/model. ทั้ง691/854ยังอยู่ใน backlog; ไม่อ้างว่าชุดนี้ครบทุกbugหรือผ่านprimary Gate A.

## ผลสดบนเครื่องบีม

`Csv-1` exact6declarationsของ `ExtendedBufferedReader`, seed101/cap30.
Generation condition: `api854-20261004-csv-messages-disabled-thinking-native-development-v2`.
Sonnetใช้Messages/requested thinking disabled; Geminiใช้ChatCompletionsตามpacketแชมป์.
บีมใช้ Java11/Defects4J/Cobertura/`America/Los_Angeles` บน `beam-pc1` CPU1,
fresh fixed/buggy checkoutsแยกทุกapproach. **Java/assertions/archive bytesตรงpacketเดิมทุกวิธี**.

| วิธี | Testsจริงทุกstage | Fixed1 / Fixed2 | Buggy failures / fault | Class lines | Class branches | Worker secondsรวมcheckout/compile |
|---|---:|---|---|---:|---:|---:|
| CMA-ES |30|ผ่าน / ผ่าน|0 / false|31/37|13/26|32.38|
| FSCS-ART |30|ผ่าน / ผ่าน|0 / false|31/37|13/26|30.99|
| Claude Sonnet |21|ผ่าน / ผ่าน|1 / true|36/37|22/26|23.62|
| Gemini Flash Lite |18|ผ่าน / ผ่าน|1 / true|37/37|23/26|23.29|

ทุกstage fixed1/fixed2/buggy/coverageมีactual JUnit XML counts, skipped0/errors0.
Algorithmsมีunchanged embedded target_checks30ทุกstage; AIไม่มีaggregate target counter
จึงเก็บnullแทนการอนุมานจากJUnitcount. AIมีactual target-class coverageและraw failing assertion.
XML formatterเพิ่มเฉพาะการเก็บcountsภายใต้CPUlock; classpath/discovery/Java/assertionsเดิม
และrestore original Defects4J build XML byte-for-byteเมื่อจบ รวมกรณีเกิดexception.
นับ reportproperties26ชุดแล้วไม่พบcredentialpropertiesที่จะส่งออก.

Sonnet fail `lineNumberDoesNotDoubleCountCRLF` expected1/actual0;
Gemini fail `testCarriageReturnLineNumber` expected2/actual1.
ตรงofficial isolated Csv-1 `read()` patchเรื่องCR/CRLF. เป็น **1unique bug /4full development outcomes**,
ไม่ใช่2bugsเพราะAIทั้งสองพบfaultเดียวกัน. Coverageเป็นคลาสนี้ ไม่ใช่ทั้งCsv/20bugs/854.
AIใช้legal Reader inputsกว้างกว่าbounded streamsของalgorithms; domain equivalenceยังไม่รับรอง
จึงยังไม่ใช้ตัวเลขนี้สรุปว่าวิธีใดเหนือกว่า.

Actual upstream generation times: CMA6.77s/FSCS7.37sจากcommand durations,
Sonnet26.25s/Gemini6.45sจากrequest start/endที่แชมป์เก็บ.
Worker timesในตารางรวมsetup+evaluation; canonical evaluatorอย่างเดียวเป็นอีกfieldในresults.json.
รวมwall timeของsuccessful new-condition replay113.36s, ยังใช้ประมาณเวลาทั้ง854ไม่ได้.
แชมป์เป็นผู้เรียกprovider; **บีมไม่เรียกKKUเพิ่ม**.

## Native buggy revision กับ Defects4J isolated bug

แชมป์nativeใช้Gitbuggy `0833f45b...`; actualD4Jbuggyไม่ใช่sourcebytesเดียวกัน.
D4Jเริ่มจากfixed `de1838ea...` แล้วapply official `framework/projects/Csv/patches/1.src.patch`:
isolatesเฉพาะ `read()` CRlinecounter. Native parent revisionมีความต่างในbuffer-readด้วย.

ตรวจofficial patchตรงDefects4J Gitblob, active-bugs.csvและfixedsourcesตรงกัน;
applypatchในreference scratchแบบfuzz0แล้วเทียบactualD4J buggyทุกproduction Javafile.
**ไม่เขียนทับactualproduction sourceเพื่อให้ตรงnativeและไม่ปลดhashguardเฉยๆ**.
ใช้ execution conditionใหม่ `beam-csv-messages-d4j-isolated-bug-counted-development-v3`.
NativeJava17/UTCกับD4JJava11/LosAngelesแยกผลและenvironmentตามจริง.

เก็บattemptv1ที่full-parent-source guardหยุดก่อนรันtests และv2ที่GNUpatch
ติดchmodบนWindows-mounted reference; v3ใช้nativeLinux scratchแล้วผ่าน.
ทุกattemptมีproducer/rawlogs/checksums. Frameworkrestoreผ่าน; frozenAom runtime41pins
และcurrentBeam41pinsตรงก่อน/หลัง. Wrapperแชมป์ `replay_csv_development_d4j.py`
ต้องปรับbindingของisolatedbugก่อนใช้ เพราะguardเดิมคาดparentbuggysources.

## BaselineและCli

รับและreplay Csvbaseline `a4a38a5e` แยกด้วย: CMA/FSCSผ่านfullD4J, Sonnettruncated
ไม่มีsuite, Geminifixedfail1ทั้งสองรอบและปฏิเสธทั้งsuite. ไม่รันbuggy/coverageให้invalidsuite.
เก็บ2completed+2invalid outcomesเดิม; ไม่แทนที่ด้วยMessagesconditionที่valid.
รวมCsvสองconditionsเป็น8outcomesของbugเดิม ไม่ใช่8bugsหรือrepeatของconditionเดียว.

บีมรันreceived Cli exactvectorบนJava11/UTCจริง fixed/buggyอย่างละสองรอบแล้ว.
Optionsมี `extra=left`, `x=alpha`; orderedpositionalarguments `positional` เท่ากันทั้งหมด.
ต่างเพียงoptionsiterationorder. **รับ scoped false-positive finding และคงquarantine**:
rawFSCSfaultflag=trueยังอยู่, confirmedsemanticfault=false/scientificfaultcountเพิ่ม0.
ไม่แก้sharedoracle/testsเดิม. ออม/แชมป์ต้องทำprospective options-only unorderedoracle
และregressCli16ก่อนออกconditionใหม่; arguments/buffers/orderedresultsยังต้องรักษาลำดับ.
คำรับนี้ครอบคลุมdiagnosticดังกล่าว ไม่ใช่fullCli domainหรือคำตัดสินว่าCliไม่มีbug.

## หลักฐานส่งมอบ

- [ผลทั้งสองconditions CSV](../../output/api854-20261004/beam-ready-results-delivery-v1/condition-separated-results.csv)
- [New Csv resultsละเอียด](../../output/api854-20261004/beam-champ-csv-messages-d4j-v3/results.json)
- [New Csv receipt](../../output/api854-20261004/beam-champ-csv-messages-d4j-v3/receipt.json)
- [Isolated source derivation](../../output/api854-20261004/beam-champ-csv-messages-d4j-v3/isolated-bug-reference/derivation.json)
- [Cli scoped receipt](../../output/api854-20261004/beam-champ-cli-order-review-v1/receipt.json)
- [Delivery audit](../../output/api854-20261004/beam-ready-results-delivery-v1/receipt.json)
- [Next cohort bindings/work](../../output/api854-20261004/beam-ready-results-delivery-v1/next-cohort-work.json)

Deliveryreceipt SHA256 `b9e8858b1bfc09b90d36111e878cbe7edc6532fa73f0dfaf9ceda74b02e3b95d`.
Deliverychecksums SHA256 `3325262efb02c1bf56d6bfca8fdc9ae67efaa91c245561562765333648dd36d9`.
Audit968packetentries/492originalpeerGitblobsผ่าน; replay/count/quarantine guards9ผ่าน/skip0.
Source/suite/command/dependency/runtime/host/coverage/report hashesอยู่ในsealedpackets.

## งานต่อ

Csv1พร้อมส่งให้ทีมตรวจผลdevelopmentครบ4แล้ว. อีก19bugsของcohort20ยังไม่มีfour-full-D4J
outcomesที่บีมรับในdeliveryนี้; Cliรอprospectiveoracle, อีก18มีpreparedinputsแต่ต้องรับsuitepacket/
scopeที่พร้อมก่อนนับผล. ใช้next-cohort-work.jsonเลือกnextreadybatchตามownerและexactv12pins,
ให้แชมป์ประสานmodel generationแล้วส่งarchives; บีมรันCPU1และส่งผล/เวลา/hashesต่อ.
Providerbudget/newtransport bindingsสำหรับbatchใหม่เป็นงานประสานทีม ไม่เปิดคิวเอง.
พักCodec/Collections/Csvconstructor candidate integration; enum4ยังpendingร่วมทีมและคงdenominator691.

รอบนี้ KKUfromBeam0 /queue mutations0 /primaryresults0 /GateAfalse.
ไม่มีbackgroundgeneration/evaluationค้างจากบีมหลังexportนี้.

## ข้อความส่งแชมป์และออม

> บีมพักcandidateและรันCsvpacket champd98fcceeบนbeam-pc1 CPU1ครบ4วิธีแล้วครับ.
> Fixedสองรอบผ่านทุกวิธี; CMA/FSCS30tests fault=false,Sonnet21/Gemini18 fault=trueจากCRlinecount.
> Coverage31/37,31/37,36/37,37/37linesตามลำดับ พร้อมJUnitจริง/skip0/เวลาจริง/hashes.
> ActualD4Jbuggysourceเป็นisolatedbugต่างจากnativeparent จึงverifyfixed+officialpatchและแยกexecutioncondition;
> อ่านBEAM_READY_RESULTS_RETURN_TH.md/packetv3ก่อนใช้wrapperเดิม. Baselineinvalidยังอยู่แยกครบ.
> Cliexactvectorรันซ้ำแล้ว mappings/argsตรง ต่างเฉพาะoptionorder; รับscopedquarantine ไม่เพิ่มscientificfault.
> ขอnextreadybatchพร้อมsame-condition4archives/pins/domainsให้บีมรันต่อ; ไม่เรียกKKUเพิ่มเองหรือเปิดprimaryGateAครับ.
