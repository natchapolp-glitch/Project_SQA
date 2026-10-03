# บีม: ผล Defects4J Csv และ Jsoup ตามแผนเก็บผลจริงก่อนขยาย

วันที่ 4 ตุลาคม 2026 — เครื่องเดียว `beam-pc1`, CPU slot 1; พัก candidate ใหม่ตามแผนล่าสุด

Csv ชุด Messages จาก champ `d98fccee` รันครบทั้ง 4 วิธีแล้วและส่งที่ beam `4940dd8e` ดู [บันทึก Csv และ Cli](BEAM_READY_RESULTS_RETURN_TH.md) รอบนี้รับ packet Jsoup จาก champ `a4c723ad6cd58b3e837af28036ebefcfdba025d1` แล้วรันต่อเฉพาะ CMA-ES และ FSCS-ART ที่ผ่าน native fixed ตามคำสั่งใน `CHAMP_JSOUP_READY_RESULTS_TH.md` เก็บ AI ที่ invalid ทั้งชุดตามเดิม

## ผล Jsoup-1 บนเครื่องบีม

ใช้ frozen v12 จาก aom `63ad195623c2ed3f67f3ae232c00c54d3160ce72`, Java 11, Defects4J 3.0.1 และ timezone America/Los_Angeles; แยกจากผล native Java 17/UTC ของแชมป์

| วิธี | จำนวน tests | fixed สองรอบ | buggy fault | Document lines | Document branches | เวลาบีมรวม setup (วินาที) |
| --- | ---: | --- | --- | --- | --- | ---: |
| CMA-ES | 30 | ผ่านทั้งสองรอบ | false | 36/46 | 10/18 | 33.552953199 |
| FSCS-ART | 30 | ผ่านทั้งสองรอบ | false | 36/46 | 10/18 | 34.088153428 |
| Sonnet 5 | 27 | native ล้มเหลว 1 test ทั้งสองรอบ | ไม่วัด | ไม่วัด | ไม่วัด | ไม่ได้รัน Defects4J |
| Gemini 3.5 Flash Lite | 9 | native ล้มเหลว 2 tests ทั้งสองรอบ | ไม่วัด | ไม่วัด | ไม่วัด | ไม่ได้รัน Defects4J |

สองวิธี algorithm มี actual JUnit XML ครบ fixed-1/fixed-2/buggy/coverage: executed 30, skipped 0, errors 0 และ target checks 30 จาก counter ใน suite เดิมทุก stage เวลารวม run ตาม receipt คือ 71.941838049 วินาที ตารางเวลารวม setup เป็นเวลาทำงานบีม ไม่รวมเวลาสร้าง suite ของแชมป์ Coverage เป็นระดับ class `Document` และไม่ใช่หลักฐานความเท่าเทียมของ input domain ระหว่างวิธี

AI สองชุดไม่ผ่าน native fixed จึงเก็บเป็น `invalid_native_fixed_entire_suite_rejected` พร้อม raw response และ archives เดิม ไม่แก้ assertion ไม่ retry AI และไม่รัน buggy/coverage บน Defects4J ของบีม ช่อง fault/coverage คงไม่มีค่า ไม่แปลงเป็นศูนย์ ข้อผิดพลาด Sonnet เกี่ยวกับ formatting ของ `<head></head>`; Gemini เกี่ยวกับ body/head ที่ยังไม่มีใน fresh Document

## ขอบเขตและความถูกต้องของหลักฐาน

- `suite.tar.bz2`, Java และ assertions ของ packet เดิมคง bytes และ hashes เดิมทั้งหมด
- actual Defects4J buggy source ต่างจาก parent commit ของ native Jsoup เช่นเดียวกับ Csv จึงตรวจ fixed Git archive ร่วมกับ official `Jsoup/1.src.patch` ที่ hash ตรง installed framework แล้ว apply forward ด้วย fuzz 0 บน Linux scratch เปรียบเทียบ Java source ทั้งหมดกับ actual checkout ไม่เขียนทับ production source
- ใช้ canonical frozen evaluator และ fresh worktrees แยกแต่ละวิธี ภายใต้ CPU lock เดียว ตัวเก็บ counts เพิ่มเฉพาะ XML formatter/report location ใน framework แล้วคืน framework เป็น bytes เดิม มีหลักฐาน restore SHA-256 `327ee3448d9ba3faf6947987df2c3950a1abdfb26ca2d2c99727b4c3f8cb3331`
- focused guards ผ่าน 6 tests, skip 0; export ตรวจ checksum entries 411 และ original peer Git blobs 243
- KKU requests จากบีม = 0, queue mutations = 0, primary results added = 0 ยังไม่อ้างครบ 854 หรือผ่าน Gate A

## Packet ส่งกลับและ hashes

- [รับ packet แบบ pinned Git bytes](../../output/api854-20261004/beam-champ-jsoup-intake-v1/receipt.json)
- [ผลจริงและ counts ราย stage](../../output/api854-20261004/beam-champ-jsoup-d4j-v1/results.json)
- [ตารางผลรวมแบบแยก condition](../../output/api854-20261004/beam-jsoup-results-return-v1/condition-separated-results.csv)
- [งานที่เหลือตาม cohort](../../output/api854-20261004/beam-jsoup-results-return-v1/next-cohort-work.json)
- [receipt ตรวจรับ](../../output/api854-20261004/beam-jsoup-results-return-v1/receipt.json), SHA-256 `e7bdf2318e090ef4f65dd63ac716e61fb8e53027d96ae04af7ee27a651f8f55b`
- [checksums](../../output/api854-20261004/beam-jsoup-results-return-v1/checksums.json), SHA-256 `0d5122f854870d53dea9d91f5bc5e6c4e1a312e1d000bbfd41b38d8638d49fc9`

| วิธี | SHA-256 ของ suite.tar.bz2 |
| --- | --- |
| CMA-ES | `9ce61996251b86582ce98ad1780937a5a099fbe6acbe0fdd361ba20ec96062e8` |
| FSCS-ART | `abf17821c0595b1120c6ebe453f63dc875ea7ed18bcf9d0b88946982fd3cff2a` |
| Sonnet 5 | `c5843c3212336cf3daddeaec7f86a02c982193e398b21a46bfd2c3246d624bd1` |
| Gemini 3.5 Flash Lite | `e3930c2fc91f0a27f9f4d2be4f6ddfa89852b93a29b0e4ca008dee14da900317` |

ผลรวม Csv/Jsoup เป็น 2 unique bugs, 12 condition × approach outcomes และ 8 completed full Defects4J evaluations เมื่อนับ Csv baseline เดิมด้วย มี **Csv เพียง 1 bug ที่ได้ valid full measurements ครบ 4 วิธี** Jsoup ได้ 2 valid algorithms และ 2 invalid AI outcomes ที่เก็บจาก native โดยแยก execution condition ชัดเจน ไม่เพิ่มจำนวน bugs จากการรันซ้ำ Cli option-order ยังคง quarantine ไม่นับเป็น confirmed semantic fault

งานต่อไปคือรับ archives ของ bugs ที่พร้อมจาก condition/preparation เดียวกันและเก็บผลจริงตามเวลาที่เหลือ ไม่สร้าง candidate ใหม่ ไม่แก้ Cli condition แทนฝั่งออม และไม่เปิด live pilot เอง

## ข้อความส่งแชมป์

> บีมทำ Csv ทั้ง 4 archives ผ่าน Defects4J แล้วที่ beam 4940dd8e: fixed สองรอบผ่านครบ, CMA/FSCS fault=false และ AI สองตัว fault=true พร้อม counts/coverage/เวลา/hashes ครับ รับ Jsoup จาก champ a4c723ad ต่อแล้ว CMA/FSCS ผ่าน fixed สองรอบและ buggy/coverage ครบ ได้ 36/46 lines, 10/18 branches, fault=false ทั้งคู่ ส่วน AI สองชุดคง native fixed-invalid และไม่รัน buggy/coverage ตาม packet เดิม หลักฐานใหม่อยู่ใน BEAM_JSOUP_READY_RESULTS_RETURN_TH.md พร้อม export แยก conditions ครับ ยังพัก candidate ใหม่, Cli คง quarantine, ไม่เรียก KKU จากบีมและไม่อ้าง Gate A
