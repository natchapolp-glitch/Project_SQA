# งานบีมราย bug บน shared v12

Selected คือ capability selection ไม่ใช่ semantic approval ครบทุกรายการ. คง denominator 691.

| Bug | Common declarations | Shared selected | Shared excluded |
|---|---:|---:|---:|
| Chart-1 | 56 | 15 | 41 |
| Cli-1 | 16 | 16 | 0 |
| Closure-1 | 14 | 3 | 11 |
| Closure-176 | 50 | 28 | 22 |
| Codec-1 | 18 | 13 | 5 |
| Collections-1 | 22 | 12 | 10 |
| Compress-1 | 19 | 8 | 11 |
| Csv-1 | 7 | 6 | 1 |
| Gson-1 | 5 | 5 | 0 |
| JacksonCore-1 | 44 | 25 | 19 |
| JacksonDatabind-1 | 37 | 14 | 23 |
| JacksonDatabind-112 | 12 | 4 | 8 |
| JacksonXml-1 | 47 | 14 | 33 |
| Jsoup-1 | 12 | 10 | 2 |
| JxPath-1 | 84 | 67 | 17 |
| JxPath-22 | 44 | 36 | 8 |
| Lang-1 | 47 | 47 | 0 |
| Math-1 | 85 | 53 | 32 |
| Mockito-1 | 15 | 6 | 9 |
| Time-1 | 57 | 21 | 36 |

Actual: 403 selected / 288 excluded. Exclusions มี Codec/Graphics joint candidate 5,
Collections bounded candidate รอตรวจร่วม 10, enum รอตัดสิน 4 และ recipe/evidence ที่ต้องทำต่อ 269.
Selected อีก 390 มี historical scoped evidence บางส่วน ต้องปิด semantic ราย declaration/domain
ให้ครบก่อน primary approval; Chronology 6 และ Graphics 7 มี scoped v12 proof แล้ว.

ดู exact identities และ bindings ใน declarations.json; ไม่มีการแก้ targets/protocol/queue.
