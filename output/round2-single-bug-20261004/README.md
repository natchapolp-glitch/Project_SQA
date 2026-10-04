# SQA รอบ2 - Csv-1 บั๊กเดียว / 4 วิธี

ชุดส่งนี้ใช้ CMA-ES, FSCS-ART, KKU Claude Sonnet5 และ Gemini3.5FlashLite.
ขอบเขต: **หนึ่งบั๊ก Csv-1** และtarget class ExtendedBufferedReader.
ผลจริง: tests30/30/21/18, fixedผ่านทุกวิธี; AIสองวิธีพบCR fault, algorithmsไม่พบในชุดนี้.
ข้อจำกัดของfixedcontext/inputdomain/coverage/timingอยู่ในรายงานและprotocol.

## เริ่มอ่าน

- [รายงานPDF](Report/SQA_Round2_Report.pdf)
- [สไลด์](Presentation/SQA_Round2.pptx)
- [คู่มือและคำสั่งdemo](Presentation/demo-guide.md)
- [ตารางผล](Experiment/summary.csv) / [ข้อมูลผลและhashes](Experiment/results.json)
- [วิธีทดลอง](Experiment/protocol.md) / [สภาพแวดล้อม](Experiment/environment.md)
- [โค้ดและผลCMA-ES](Algorithm1_CMAES/README.md)
- [โค้ดและผลFSCS-ART](Algorithm2_FSCSART/README.md)
- [prompt/output/testsSonnet](AI1_KKU_Claude/README.md)
- [prompt/output/testsGemini](AI2_KKU_Gemini/README.md)
- [frozenruntimeและinputs](Shared/README.md)

## สมาชิก

| ชื่อ-นามสกุล | รหัสนักศึกษา |
|---|---|
| นายธนินธร อันทรบุตร | 673380043-6 |
| นายศุภกร กรมรินทร์ | 673380061-4 |
| นายณัชพล เพ็งพล | 673380267-4 |
| นายณัฐกรณ์ อินธิสาร | 673380268-2 |

หลักฐานเดิมและผลที่ไม่ผ่านยังอยู่ในrepositoryต้นฉบับ ไม่เขียนทับหรือรวมยอดเข้าชุดนี้.
ผลซ้อมdemoเป็นhost replayของsuiteเดิม ไม่ใช่เพิ่มbugหรือเพิ่มgenerationrepeat.
ไม่อ้างว่าชุดนำร่องบั๊กเดียวครอบคลุมทุกJavaprojectตามโจทย์.
