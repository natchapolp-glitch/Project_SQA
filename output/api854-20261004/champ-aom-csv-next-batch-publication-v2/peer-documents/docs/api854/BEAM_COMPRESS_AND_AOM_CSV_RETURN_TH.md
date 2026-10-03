# บีม: ส่ง Compress-1 Defects4J และตรวจรับ Csv ของออม

วันที่ 4 ตุลาคม 2026 — รับ champ `e9c6371892b3521d363b9c9b548ed348a1c15b5a` และ aom `4334c2ab908f517f99c7046c08f45a25c0b6a64d` แบบ pinned Git bytes ไม่ merge runtime ปัจจุบันเข้าชุดทดลอง ฐานยังเป็น immutable aom `63ad195623c2ed3f67f3ae232c00c54d3160ce72` v12 ไม่ปะปนผล v13

## Compress-1 ผลจริงบนเครื่องบีม

รัน archives ของ CMA-ES/FSCS-ART เดิมบน `beam-pc1`, CPU1 ภายใต้ lock เดียว ใช้ Java11/Defects4J3.0.1/America/Los_Angeles และ fresh checkout ต่อวิธี เก็บผล AI สองชุดเป็น invalid ตาม packet เดิม ไม่ retry/repair และไม่เรียก provider เพิ่ม

| วิธี | Tests | Fixed สองรอบ | Buggy failures | Fault | Class lines | Class branches | เวลารวม setup (วินาที) |
| --- | ---: | --- | ---: | --- | --- | --- | ---: |
| CMA-ES | 30 | ผ่านทั้งสอง | 1 | true | 98/165 | 21/59 | 34.265035759 |
| FSCS-ART | 30 | ผ่านทั้งสอง | 5 | true | 98/165 | 21/59 | 33.695250643 |
| Sonnet 5 | ไม่ได้เป็น suite สมบูรณ์ | ไม่รัน Defects4J | ไม่วัด | ไม่วัด | ไม่วัด | ไม่วัด | ไม่วัด |
| Gemini 3.5 Flash Lite | ประกาศ 8 | compile failed ที่ native | ไม่วัด | ไม่วัด | ไม่วัด | ไม่วัด | ไม่วัด |

Algorithms มี actual JUnit XML ทุก stage fixed-1/fixed-2/buggy/coverage: executed30/skipped0/errors0/target_checks30; fixed และ coverage failures0 เวลารวม replay ตาม receipt 76.049043145 วินาที ไม่รวมเวลา generation ของแชมป์ Coverage จำกัด `CpioArchiveOutputStream` ไม่ใช้สรุป input-domain equivalence หรือ superiority ของวิธี

Failures ของ CMA/FSCS เป็น assertions ของ `close()` และ archive state ที่ขาด trailer ตรงกับ official patch ที่ตัด `this.finish()` จาก close; matched จำนวน failures native 1/5 มี embedded target counter และ raw assertion/JUnit evidence ไม่ใช่ compile หรือ environment failure ส่วน Sonnet ยังคง `invalid_generation_truncated` และ Gemini `compile_failed` เพราะ `CpioArchiveEntry.setFileSize` ไม่มีใน version นี้ ไม่แปลง invalid เป็น fault=false หรือ coverage0

## Strict attempt และ benchmark binding

รอบ strict `beam-champ-compress-d4j-v1` หยุดก่อน checkout/test เพราะ native seal กับ Linux official-patch reference ต่างกันเพียง `CpioArchiveOutputStream.java` ไม่ลบ guard เดิมและไม่แก้ Java/archives/assertions หรือ production source

- Native buggy SHA-256 `4e86807db8b6befda348a24b316662400db1d87f5472059b8f2005c7adf92fa1`
- Linux benchmark buggy SHA-256 `1c622f7ad95926f98b3c6063c15a172dc8e30b42b08edc4dd165ac28dd0df72b`
- Official Compress/1.src.patch SHA-256 `f1f3442aaa3fdb93cb26f36dbc0334399848d77f7456d710199f49d10a8b0bbe` ตรง installed framework Git blob; fixed revision และ archive ตรง native seal
- พิสูจน์ representation: Linux GNU patch และ Linux Git apply ได้ benchmark bytes เดียวกัน; เปลี่ยน LF เป็น CRLF เฉพาะใน scratch reference แล้ว SHA ตรง native ที่ seal ไว้ มี native CRLF413/benchmark CRLF0 ไม่ใช้ normalized hash เป็น checkout guard

เก็บ binding diagnostic v1 ที่ไม่ผ่านการ reproduce native ด้วย Linux Git apply ไว้ด้วย และสร้าง binding v2 ก่อน replay สำเร็จ เก็บ bytes สอง representation เป็นหลักฐาน จากนั้นประกาศ execution condition ใหม่ **`beam-compress-d4j-gnu-patch-benchmark-counted-development-v2`** และตรวจ exact source hashes ของ fixed/benchmark buggy ทุกไฟล์กับ actual Defects4J checkout ไม่อ้าง native source byte-equivalence แบบเดิม

Generation condition เดิมคือ `api854-20261004-compress-eight-target-messages-disabled-thinking-isolated-bug-native-development-v1` รักษา exact8 targets, frozen inputs/runtime41 pins และ seed101/cap30; ไม่สร้าง generation ใหม่จาก host replay ต้องให้ออม/แชมป์รับ binding ใหม่นี้อย่างชัดเจนก่อนนำผลไปใช้ร่วม

## ตรวจรับ Csv ของออมโดยไม่รันซ้ำ

รับ v1 strict failure และ v2 benchmark-bound receipts ครบ ไม่ลบ attempts เก่า ตรวจ archive hashes ตรง Csv ที่บีมเคยรัน, record hashes/counters/coverage summary/raw test-start events/failing tests/environment ผ่านทั้งสี่วิธี ผลตรงบีม: CMA/FSCS fault=false และ AIสองตัว fault=true จาก CR failures เดิม

Runtime41 pins ตรง frozen v12; host plan เป็น `aom-pc1` CPU1, raw commands/Java11/Defects4J3.0.1/framework commit ตรง host ที่ประกาศ เก็บ host แยกจาก `beam-pc1` ไม่ย้ายชื่อ host หรืออ้างการเข้าถึงเครื่องออมจริง Source override ที่ออมประกาศตรง bytes ของ official-patch reference ที่บีม derive ไว้แล้ว: benchmark `4ddf7df1c7c8b9e3d45dc6a3410f802a2e5b641f37089655c3eb2f52cdfd5185` ต่างจาก native upstream `d725202c7a4989c6ec37289578d7f8497d1f0a8c623c64fe3e1aabb9f00fa4a4`

Algorithm counters executed30/skipped0/target_checks30 ครบทุก stage ส่วน AI มี actual Defects4J test-start events21/18 ทุก stage แต่ aggregate skip/target counters ไม่มี จึงคง null ตาม receipt ออม ไม่ยืม XML counts ของบีมไปเติม Review เป็นการตรวจหลักฐานข้าม host ไม่ใช่ rerun หรือ attestation ของ live files ทุกไฟล์บนเครื่องออม

Intake reader รอบแรกไม่รับ nested checksums inventory ของ packet ออม จึงเก็บ attempt ไว้และรับด้วย reader ใหม่ที่รวม nested checksum files ถูกต้อง ไม่ใช่ peer test failure หรือเหตุให้แก้ peer hashes

## ส่งหลักฐานและงานต่อ

- [Compress results/counts](../../output/api854-20261004/beam-champ-compress-d4j-v2/results.json)
- [Benchmark binding](../../output/api854-20261004/beam-compress-benchmark-binding-v2/binding.json)
- [Strict Compress attempt](../../output/api854-20261004/beam-champ-compress-d4j-v1/failed-attempt.json)
- [Aom Csv review](../../output/api854-20261004/beam-aom-csv-receipts-review-v1/receipt.json) และ [stage counters](../../output/api854-20261004/beam-aom-csv-receipts-review-v1/observations.json)
- [ตารางผลรวมแยก condition](../../output/api854-20261004/beam-compress-results-return-v1/condition-separated-results.csv)
- [Export receipt](../../output/api854-20261004/beam-compress-results-return-v1/receipt.json) และ [checksums](../../output/api854-20261004/beam-compress-results-return-v1/checksums.json)

Guard tests ผ่าน9/skip0 ตรวจ raw XML counts, exact archives/producers/source binding, close-state assertions และการคง AI counters null ของออม Export ตรวจ checksum entries1793 และ original Git blobs840 (Champ295/Aom545) Receipt SHA-256 `4d96ba5d98b2a77c408639ea5734c972098e6038097f53c41a69668c0c8d7208`; checksums SHA-256 `55062b275db653037f1e18311bfee440e9fef194a91f97f8d36556480d5bd3c3` Framework XML formatter observer คืน exact original bytes แล้วหลังรัน

รวมผลใน delivery ของบีมเป็น 3 unique bugs (Csv/Jsoup/Compress), 16 condition × approach outcomes และ 10 completed full Defects4J evaluations เมื่อนับ Csv baseline ด้วย มีเพียง Csv1 ที่ valid full measurements ครบ4วิธี ไม่บวก Csv host replay ของออมเป็น bug ใหม่ ผล native ของแชมป์/Gson invalids ไม่ใช่ Defects4J measurements ของบีม

พัก candidate ใหม่ต่อ เก็บผล v12/v13 แยกกัน Cli option-order คง quarantine รอ oracle ใหม่ของออม เก็บ enum4 pending joint decision ตามเดิม รอ packet bugs ที่พร้อมชุดถัดไปจากแชมป์ KKU calls จากบีม0/queue mutations0/primary0 ยังไม่ครบ854หรือผ่าน Gate A

## ข้อความส่งแชมป์

> บีมรัน Compress-1 CMA/FSCS ผ่าน Defects4J ครบแล้วครับ fixed สองรอบผ่าน, buggy failures1/5, counts30/skipped0/errors0/target_checks30 ทุก stage และ coverage98/165 lines,21/59 branches ทั้งคู่ พร้อมเวลา/hashes พบ native buggy เป็น CRLF แต่ Linux benchmark เป็น LF จึงเก็บ strict attempt และประกาศ exact benchmark binding/condition ใหม่ก่อนรัน ไม่ปลด guard หรือแก้ suites ครับ ขอรับตรวจ binding และหลักฐานใน BEAM_COMPRESS_AND_AOM_CSV_RETURN_TH.md ส่วน AI truncated/compile-invalid คงเดิม ไม่เรียก KKU เพิ่ม พร้อมรับ bugs ชุดถัดไปครับ

## ข้อความส่งออม

> บีมรับตรวจ aom4334c2ab Csv receipts/counters/host/runtime/source hashes แล้วครับ ผล4วิธีและ suite bytes ตรงกับบีม benchmark source ตรง official-patch reference เก็บ strict attempt ครบและคง AI aggregate skip/target counters null ไม่รัน Csv ซ้ำครับ Compress2algorithms รัน Defects4J ต่อครบแล้ว มี benchmark binding ใหม่เรื่อง CRLF/LF ให้ทีมรับตรวจ แยก v12 จาก v13 และ Cli คง quarantine รอ oracle ใหม่ฝั่งออมครับ
