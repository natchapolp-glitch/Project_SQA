# แผนใหม่และชุดผลจริงพร้อมส่งออม/บีม

ล่าสุด [รับ Csv ของ Aom และส่ง JacksonDatabind-112](CHAMP_AOM_CSV_AND_JACKSON_NEXT_RETURN_TH.md):
รับAom4334c2abแล้ว; native6bugs/28outcomesรวมinvalid,latest reviewed Beam/Aom10suite-host rows/2bugs.
JacksonDatabind112มี2validalgorithm archives+prospectiveGNUsourcebytebindingส่งAom;
Sonnettruncated/Geminifixedfailเก็บinvalidครบ. CompressยังBeam,เพียงCsvMessagesที่4validfullD4Jmethods.
อ่าน return-index-v6 / champ-aom-next-batch-audit-v1 ก่อนประวัติด้านล่าง.
รับรู้pushใหม่Beamf3484746Compress2fullD4J/Aom7f0c6d31Clioracle+2fullD4Jแล้ว;
คำรับChamprawreceiptsสองชุดยังpending แยกจากตาราง10reviewedhostrows ไม่ให้รันซ้ำ.

ล่าสุด [รับผล Beam และส่ง Compress](CHAMP_BEAM_RESULTS_AND_COMPRESS_RETURN_TH.md):
ตรวจรับCsv4/Jsoup2fullD4Jแล้ว; nativeรวม5bugs/24outcomesรวมinvalid.
Compress2validarchivesพร้อมส่งBeam; Gsonต้องprospectiveTypeoracleก่อนทดลองใหม่.

อัปเดตถัดมา: [Jsoup-1 ready results](CHAMP_JSOUP_READY_RESULTS_TH.md) เก็บครบ4outcomesเพิ่มแล้ว.
รวม3unique bugs/16outcomes; algorithmsผ่าน/AIทั้งสองไม่ผ่าน fixed และเก็บ invalid ไว้ครบ.
ตารางรวมล่าสุดคือ `champ-ready-results-audit-v2`; ข้อมูลด้านล่างเป็น milestone ก่อนขยาย Jsoup.

ผู้ใช้เลือกแล้ว: **เก็บผลจริงครบ4วิธีจากชุดที่พร้อมก่อน แล้วขยายbugsตามเวลา24ชม.**
ให้ใช้เวลากับการทดลองและรายงาน พัก candidate ใหม่. Full854 ยังเป็นเป้าหมายที่ต้อง
รายงานยอดจริง; deadline ไม่เปลี่ยนผล subset ให้กลายเป็นครบ854.

## ชุดที่ทั้งสี่วิธีผ่าน fixed แล้ว

Csv-1 condition `api854-20261004-csv-messages-disabled-thinking-native-development-v2`.
Exact Aom63ad1956 prompt/targets/recipes/runtime เดิม; seed101/cap30/temperature0/output4096.
Sonnet ใช้ `/messages` + requested `thinking:{type:disabled}`; Gemini ใช้ `/chat/completions`.
เป็น conditionใหม่ที่ประกาศก่อน request; ไม่ใช่ retry/repair ของ baseline เดิม.

| วิธี | Tests | Fixed-1 / Fixed-2 | Buggy failures | Class lines | Class branches |
|---|---:|---|---:|---:|---:|
| CMA-ES | 30 | ผ่าน / ผ่าน | 0 | 31/37 =83.78% | 50.00% |
| FSCS-ART | 30 | ผ่าน / ผ่าน | 0 | 31/37 =83.78% | 50.00% |
| Claude Sonnet | 21 | ผ่าน / ผ่าน | 1 | 36/37 =97.30% | 84.62% |
| Gemini Flash Lite | 18 | ผ่าน / ผ่าน | 1 | 37/37 =100% | 88.46% |

Skip0 ทุก stage; instrumented fixed ผ่านทั้งสี่. AIทั้งสองจับการนับบรรทัด CR:
Sonnet `lineNumberDoesNotDoubleCountCRLF` expected1/actual0;
Gemini `testCarriageReturnLineNumber` expected2/actual1. Actual Csv production patch
แก้ `read()` จากนับเฉพาะ LF ให้รับ CR และไม่ double-count CRLF. เป็น native bug reproduction
บน exact fixed/buggy revisions; **ยังไม่ใช่ full Defects4J/primary result**.

Coverageเป็น `ExtendedBufferedReader` เท่านั้น. AIมี legal Reader inputs กว้างกว่า
สอง bounded streams ของ algorithms; input-domain equivalenceยังไม่รับรอง.
ไม่ใช้ตารางนี้สรุป superiority/semantic completeness ของทั้ง20หรือ854bugs.
Counters target invocation ของ algorithms30ทุกstage; AIไม่มี aggregate target counter,
แต่ JUnit executed counts และ actual target-class coverage ตรวจได้.

## ผลเก่าและ Cli ต้องคงอยู่

Baseline Csvเดิมครบ4outcomes: algorithmsผ่าน/AI Sonnettruncated/Geminifixedfail1.
Cli-1 baselineครบ4outcomes: algorithms30testsผ่านfixed; Sonnettruncated;
Gemini12tests/fixedfail4ทั้งสองรอบ. Retain raw responses/suites/commandsทุกcondition.
รวม **2 unique bugs /12 condition×bug×approach outcomes**;
Csvconditionใหม่คือ bugเดิม ไม่ใช่bugที่สาม และห้าม pool เป็นrepeatของbaseline.

Cli FSCS-ART raw receiptมี `fault_detected:true` จาก `generated16/addOption`;
**คำตัดสิน semantic auditไม่รับเป็นfault**. Reproduceexactvectorบนfixed/buggyอย่างละสองรอบ:
optionsมีคู่ `x=alpha`, `extra=left` และargs `positional` เท่ากันทั้งหมด;
ต่างเพียงลำดับ HashSet/HashMap. Projectionของv12serialize `getOptions()` ตามiterationorder.
เก็บrawflagเดิมแล้ว quarantineจาก scientific fault counts;
ขอออมทำ prospective oracle ที่รับunorderedoptions และ regress Cli16ก่อนconditionใหม่.
ไม่ sortทุกarrayรวมกัน เพราะarguments/buffers/orderedresultsยังต้องตรวจลำดับตามcontract.

## Provider / harness ที่ตรวจได้

- A01 latest observed quota: Sonnet64818/Gemini264424 tokens; A02หลังCli:
  Sonnet127539/Gemini303675. เป็นค่าที่responseรายงาน ณ เวลา ไม่ใช่expiry/resetproof
  และไม่รวมtenkeysเป็นpoolโดยไม่มีbucketหลักฐาน.
- SonnetMessages actual ID `anthropic/claude-sonnet-5`, provider `Claude Platform on AWS`;
  short calibrationและfullCsvรายงาน `thinking_tokens:0`. Chatendpointใช้vendorlabel `Claude`.
- FullCsvMessages input63672/output3636; total_tokensไม่รายงานจึงคงnull.
  Geminifreshcontrol input41314/output1655/total42969. ไม่สร้างprovider totalแทนfieldที่ขาด.
- Admissionของnewconditionใช้ prioractualsamepromptinput +8192operatorbuffer +4096output;
  **เป็นdevelopmentbudget ไม่ใช่ proveninputmaximum/measuredframing/finalreserve**.
- Messagesgeneration-v1หยุดเพราะguardคาดvendorlabelผิด. Providerส่งresponseสำเร็จแล้ว;
  generation-v2reconcilebytesเดิมตามobservedcalibrationmapping **ไม่ได้ส่งSonnetซ้ำ**,
  ส่งเฉพาะGeminiที่ยังไม่attempt. ไม่มีfeedback/assertionrepair/suitepruning.
- Cli generation-v1 compileขาดCommonsLang2.1 ก่อนprovidercalls; v2เพิ่มdependency
  ตามexactproductionproject.xmlแล้วจึงcompile/discover/API. Diagnosticorder-v1มีtuple/list
  comparisonerror; successfulv2คงrawfixture/observations/expectedsemanticsเดิม.

## หลักฐาน

- [Audit receipt](../../output/api854-20261004/champ-ready-results-audit-v1/receipt.json)
- [ทุกconditionใน CSV](../../output/api854-20261004/champ-ready-results-audit-v1/results.csv)
- [Csv fresh four-method receipt](../../output/api854-20261004/champ-csv-messages-native-measurement-v1/receipt.json)
- [Generation และ reconciliation](../../output/api854-20261004/champ-csv-messages-generation-v2/receipt.json)
- [Cli semantic decision](../../output/api854-20261004/champ-cli-order-oracle-audit-v2/receipt.json)
- [Received Beam v12 host](../../output/api854-20261004/champ-ready-results-audit-v1/received-beam-host-receipt.json)

Audit420newpacketentries, rawJUnitcounts/coverageXML/byte-identicalJavaarchives ผ่าน;
sharedV9checkpoint100pinsคงเดิม. รับตรวจ exactBeam92a3ee1b v12packet1557entriesแล้ว:
consumers80/tests15/skip0/boundedcomponent/beam-pc1CPU1ตามoriginalscope.
ไม่transferเป็นall403semantics/primaryfreeze/GateA. FullDefects4Jevaluation0/primaryresults0.

## คำสั่งสำหรับเครื่องบีม

มี wrapper [replay_csv_development_d4j.py](../../scripts/study/api854/replay_csv_development_d4j.py)
สำหรับfull development replayครบ4archivesโดยไม่เรียกAPI. **ยังไม่ได้executeบนChampWindows**;
ต้องใช้Java11/LinuxhostและsameCPUlockrootที่Beamรับแล้ว.
Wrapperใช้immutableAom63ad1956 evaluator/runtime, freshcheckoutต่อapproach,
ตรวจactualproductionSourcesและretaincheckouts/logs/receipts. NativeUTCกับD4JLosAngeles
เป็นคนละenvironmentcondition; ไม่transferผลอัตโนมัติ. เจ้าของhostตรวจcounts/skip/coverage/rawfaultต่อ.

```bash
python3 -B -m scripts.study.api854.replay_csv_development_d4j \
  --d4j /home/beam/sqa-beam/defects4j/framework/bin/defects4j \
  --worktrees /home/beam/sqa-beam/worktrees --worker-id beam-pc1 \
  --output output/api854-20261004/beam-csv-four-approach-d4j-replay-v1
```

ใช้outputชื่อใหม่หากมีattemptค้าง; เก็บfailureเดิม. รันบนbranchที่มีChamppacketนี้
และตรวจJava11PATHก่อน. ถ้าCPUbusyไม่เปิดอีกslot. ออมใช้actualhost/rootของออม
ที่รับร่วมแล้ว; อย่าใช้receiptเครื่องบีมแทนเครื่องออม.

## ข้อความเปลี่ยนแผนพร้อมส่ง

**ถึงออม:** ผู้ใช้เลือกแผนใหม่แล้วครับ เหลือ24ชม.ให้เก็บผลจริงครบ4วิธีจากbugsพร้อมก่อน
แล้วขยายตามเวลา พักCodec/Collections/candidateใหม่และใช้v12เป็นฐาน.
ChampมีCsvconditionใหม่ครบ4validnative suites: CMA/FSCS30,Sonnet21,Gemini18;
AIทั้งสองพบCRlinecounterfailureบนbuggy แต่ยังไม่ใช่fullDefects4J/primary.
ขอร่วมกับBeamรันarchivesตามCHAMP_READY_RESULTS_UPDATE_TH.mdและทำตารางรายงานไปพร้อมกัน.
Cliพบunorderedoptionoraclefalsepositive ขอซ่อมprospectively/regressCli16ออกconditionใหม่
โดยไม่แก้rawtestsเดิม. ส่งnextreadybatchพร้อมpins/domains/hostและtimingsกลับ;
คงinvalidAI/limitations/จำนวน854ที่ยังไม่ได้รันตามจริงครับ.

**ถึงบีม:** ผู้ใช้เลือกผลจริงก่อนขยายแล้วครับ เหลือ24ชม. ขอพักcandidateใหม่.
รับv12scopedhost/consumersที่92a3ee1bแล้ว; ขอใช้beam-pc1CPU1รันfullDefects4J
developmentของCsv4archivesจากChamp โดยใช้wrapperคำสั่งข้างบน ไม่เรียกAIใหม่.
ส่งfixedสองรอบ/buggy/coverage/actualcounts/skip/timings/hosthashesกลับแล้วขยายreadybatch
ของBeamร่วมออม. CliFSCSrawflagเป็นoption-orderfalsepositiveให้quarantineไว้;
ไม่รวมmanualCollectionsreferenceเป็นผลalgorithm/model และไม่อ้างครบ854/GateAครับ.

แชมป์จัดAPI/bytebindingsและauditผลต่อ. ผลใหม่ใช้Messagestransport/thinkingcontract;
ก่อนขยายfrozen20ต้องให้ออมbindprotocol/worksheetใหม่และระบุexactdomains.
Final40pairreserve/limits/reset/expiryยังไม่ครบ. ห้ามใช้ผลvalidconditionใหม่ลบbaselineinvalid.
