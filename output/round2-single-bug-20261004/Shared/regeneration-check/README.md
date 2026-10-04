# Algorithm regeneration rehearsal

คำสั่ง `Shared/regenerate-algorithms.py` รันบน Linux/Java 11 ได้ครบทั้งสอง algorithms
สร้าง suite วิธีละ 30 tests จาก fixed Csv-1, frozen runtime, seed 101 และ budget 30
ผลนี้เป็นการตรวจคำสั่งทำซ้ำ ไม่ใช่ผลทดลองเพิ่มในตารางรายงาน

FSCS-ART: Java source ตรงกับ suite ที่รายงานทุก byte
CMA-ES: assertions, targets และ fixture policy ตรงเดิม แต่ตัวเลข floating point
หลักท้ายของ vectors บางตำแหน่งต่างระหว่าง Python/host เดิมกับการรันนี้
จึงไม่อ้างว่า source ตรงทุก byte หรือแทนผล canonical ด้วย suite ใหม่

การซ้อม Defects4J ใน `Presentation/rehearsal` ใช้ archives เดิมที่ hashes ตรง canonical
และผ่าน fixed สองรอบ, buggy และ coverage ครบทั้งสี่วิธี
Suites ที่สร้างใหม่ในโฟลเดอร์นี้ยังไม่ได้รัน buggy/coverage เป็นผลทดลองชุดใหม่
