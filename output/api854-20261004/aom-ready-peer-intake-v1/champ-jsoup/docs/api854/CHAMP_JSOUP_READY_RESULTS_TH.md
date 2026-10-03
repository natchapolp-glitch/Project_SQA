# ผลจริง Jsoup-1 ระหว่างรอ peer hosts

ทำต่อจากแผนผู้ใช้: เก็บผลจริงทั้ง 4 วิธีบน bugs ที่พร้อมก่อน แล้วขยายตามเวลา 24 ชม.
ชุดนี้ใช้ **Aom `63ad1956` / v12 เดิม** ไม่สลับไป v13 ระหว่างทดลอง.

## ผลที่เก็บได้

Condition: `api854-20261004-jsoup-ten-target-messages-disabled-thinking-native-development-v1`.
Exact 10 signatures ของ `org.jsoup.nodes.Document`; seed101 / cap30 / temperature0 / output4096.

| วิธี | Tests | Fixed สองรอบ | Buggy | Document lines / branches |
|---|---:|---|---|---|
| CMA-ES | 30 | ผ่านทั้งสอง | ผ่าน, fault=false | 36/46 =78.26% /55.56% |
| FSCS-ART | 30 | ผ่านทั้งสอง | ผ่าน, fault=false | 36/46 =78.26% /55.56% |
| Claude Sonnet | 27 | fail1 ทั้งสอง | ไม่รัน เพราะ suite ไม่ผ่าน fixed | null |
| Gemini Flash Lite | 9 | fail2 ทั้งสอง | ไม่รัน เพราะ suite ไม่ผ่าน fixed | null |

ทั้งสอง algorithms มี executed30 / skipped0 / target_checks30 ทุก stage รวม coverage;
instrumented fixed ผ่าน. AI ทั้งสอง compile ผ่านและไม่มี skip แต่ปฏิเสธ **ทั้ง suite**
โดยเก็บคำตอบ/Java/archives/logs เดิม ไม่แก้ assertions ไม่ตัด tests และไม่ส่ง AI ซ้ำ.

- Sonnet: `outerHtmlContainsHtmlHeadBodyTags` คาด `<head></head>` แบบติดกัน;
  fixed ไม่ผ่าน assertion นี้. เก็บ failure เป็น invalid oracle ตาม observed fixed behavior.
- Gemini: `testOuterHtml` เรียก `body().text()` บน `new Document(...)` ที่ยังไม่มี body;
  `testTitleGetAndSet` เรียก title setter ทั้งที่ยังไม่มี head จึงเกิด NPE บน fixed.
  Exact constructor สร้าง empty document; `createShell` หรือ `normalise` จึงสร้าง head/body.

ไม่ใช้ AI failures บน fixed เป็นหลักฐานพบ bug และไม่แทน missing coverage ด้วย 0.
Actual Jsoup-1 patch แก้ลำดับการย้าย text nodes ใน `normalise`; ชุด algorithms นี้ยังไม่พบ fault.

## Bindings และ provider

- Fixed `27a52f90a25699bebe23ff1ff94d6db361fdb11d`;
  buggy `77add7946ea5bca622b1f4f654f97e62f6db1e95`.
- Prepared fixed `Document.java` SHA-256
  `80ad1c53572ff2d2b6435129db05bb21bf708d4c435260a1513aa15d3536c417`.
- Preflight compile fixed/buggy และ discover 10 declarations/dimensions ตรงกันก่อน API.
  ใช้ Commons Lang2.4 ตาม exact fixed `pom.xml`; dependency hash อยู่ใน plan/seal.
- บัญชี a03 ประกาศสำหรับ bug ใหม่ก่อน request: calibration2 + generation2 =4 calls;
  ไม่หมุนบัญชีเพื่อ retry Csv/Cli. ไม่รวม quota สิบบัญชีเป็น pool.
- Sonnet `/messages`, requested thinking disabled, actual
  `anthropic/claude-sonnet-5` / `Claude Platform on AWS`, thinking_tokens0.
  Actual input64391 / output2618; provider ไม่รายงาน total_tokens จึงคง null.
- Gemini `/chat/completions`, actual `gemini-3.5-flash-lite`;
  input41896 / output695 / total42591.
- หลัง responses a03 เหลือ Sonnet132971 / Gemini307404 tokens ณ เวลาที่รายงาน.
  ไม่ใช่หลักฐาน reset/expiry. Prompt156251 UTF-8 bytes ไม่ใช่ token count;
  admission guard164443 ใช้ byte length +operator buffer4096 +output4096.
  Buffer ไม่ใช่ measured framing หรือ proven maximum; final40-pair reserve ยังไม่ครบ.
- Request intents ผูก prompt/settings/hash; serialized_request_sha256 เป็น hash ของ
  intent object ตาม producer ไม่ใช่หลักฐาน capture wire bytes จาก network transport.

Native measurement-v1 หยุดใน Cobertura เพราะ instrumentation JVM หา `DataNode` ไม่พบ.
v2 เพิ่ม exact fixed production classes ใน tool classpath ให้ ASM resolve hierarchy;
คง failed-v1 และรันคำตอบ AI เดิมโดยไม่เรียก provider เพิ่ม. CMA source bytes ตรงกันทั้งสอง attempts.

## ยอดรวมและหลักฐาน

รวม **3 unique bugs /16 condition×bug×approach outcomes** รวม invalids;
Csv Messages condition เดิมยังเป็นชุดเดียวที่ทั้ง 4 วิธีมี valid native measurements.
Csv condition ใหม่คือ bug เดิม ไม่เพิ่มยอด bugs; Cli raw order false positive ยัง quarantine.
Native Java17 / production release7 / UTC; full Defects4J Java11 host replay ยังรอ.
Full Defects4J evaluations0 / primary results0 / GateA=false.

- [Generation](../../output/api854-20261004/champ-jsoup-development-generation-v1/receipt.json)
- [Native outcomes](../../output/api854-20261004/champ-jsoup-native-measurement-v2/receipt.json)
- [Audit รวม 16 outcomes](../../output/api854-20261004/champ-ready-results-audit-v2/receipt.json)
- [ตาราง CSV](../../output/api854-20261004/champ-ready-results-audit-v2/results.csv)
- [Return index พร้อม hashes](../../output/api854-20261004/champ-ready-results-return-index-v3.json)

Audit ตรวจ238 entries รวม prerequisite audit เดิม, raw counts, archive bytes,
provider text/usage/quota และ exact Aom inputs; shared checkpoint100 pins คงเดิม.
Class coverage ไม่ใช่ whole-project coverage; domain equivalence/semanticครบ403 ยังไม่รับรอง.
รับรู้ Aom v13 `ff319ecd` และ Beam candidate `a15ffa4f` แล้ว แต่ไม่รวมใน condition นี้.

## ข้อความพร้อมส่งให้เพื่อน

**ถึงบีม:** แชมป์เก็บ Jsoup-1/v12 ครบ4outcomesแล้วครับ CMA/FSCS30tests fixedสองรอบ/
buggy/coverageผ่าน, 36/46lines, fault=false. Sonnet27/fixedfail1 และ Gemini9/fixedfail2
เก็บเป็น invalid ทั้งsuite ไม่ซ่อมหรือเรียกใหม่. ขอใช้ accepted Linux/Java11 CPU1
รัน Csv ทั้ง4archivesก่อน แล้ว replay Jsoup เฉพาะ2valid algorithm archives ตามคำสั่งด้านล่าง;
คง AI invalid ในตาราง. ส่ง actualcounts/skip/coverage/timings/host hashes กลับครับ.

**ถึงออม:** แชมป์ขยายผลจริงเป็น3bugs/16outcomesแล้วครับ ใช้ frozenv12 เดิม
และเก็บinvalidAIครบ. ขอทำรายงานจาก results.csv รุ่นใหม่ร่วมผล peerhosts,
พักcandidateเพิ่มระหว่าง24ชม. และปิด prospective Cli unordered-option oracle ต่อได้.
v13 ที่ ff319ecd รับรู้แล้ว แต่ต้องมี condition/prompt/bindings/reserve แยกก่อนใช้;
ยังไม่ใช้แทนผล v12 หรือเปิด GateA ครับ.

```bash
python3 -B -m scripts.study.api854.replay_csv_development_d4j \
  --packet output/api854-20261004/champ-jsoup-native-measurement-v2 \
  --approaches cmaes fscs-art \
  --d4j /home/beam/sqa-beam/defects4j/framework/bin/defects4j \
  --worktrees /home/beam/sqa-beam/worktrees --worker-id beam-pc1 \
  --output output/api854-20261004/beam-jsoup-valid-algorithm-d4j-replay-v1
```

Wrapper ระบุ unreplayed outcomes ตามจริง; ไม่ทำให้ Jsoup กลายเป็น4valid measurements.
ใช้ fresh output และ CPU lock root เดิม; owner ของ host ตรวจ environment/semantic ก่อนรับผล.
คำสั่ง Csv ทั้ง4อยู่ใน [update ก่อนหน้า](CHAMP_READY_RESULTS_UPDATE_TH.md).
