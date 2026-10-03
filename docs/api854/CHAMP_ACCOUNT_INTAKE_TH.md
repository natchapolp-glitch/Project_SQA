# รับข้อมูล 10 บัญชีก่อนเปิดรันจริง

ขอ keys ของบัญชีที่จะจัดให้ API worker ครบ 10 ไว้ก่อนได้ เพื่อลดเวลารอเมื่อจำเป็นต้องใช้
ส่งผ่านช่องทางส่วนตัวที่ทีมใช้ ไม่ส่ง keys ในแชทหรือ Git
Worker ปัจจุบันใช้บัญชีที่เลือกทีละ alias; การมี keys ครบไม่เปิด pilot หรือสลับบัญชีอัตโนมัติ

## รูปแบบไฟล์รับ

[Blank template](../../experiments/configs/api854-20261003/accounts.template.json) มี aliases `a01`–`a10` และช่อง key ว่าง
ให้ผู้รวบรวมเติมในสำเนาส่วนตัว `.local/api854/accounts-intake.private.json` หรือส่ง private file แล้วแจ้ง path บนเครื่อง
ไฟล์ default `.local/api854/accounts.json` มี a01 ที่ใช้ preflight เดิมอยู่แล้ว ต้องเก็บไว้ก่อนตรวจไฟล์ใหม่
ห้ามเขียนทับ key เดิมโดยไม่ตรวจว่าบัญชี/alias ยังตรง; ถ้าเปลี่ยน key ต้องตรวจ provider/quota evidence ของบัญชีนั้นใหม่

ข้อมูลประกอบแยกตาม alias: เจ้าของบัญชี/การจัดให้ worker, models ที่ใช้ได้, quota remaining/unit,
bucket sharing, window/reset/timezone, observed time/expiry และ evidence ที่ไม่มี key
ค่าไหนยังไม่รู้ให้คง unknown ไม่รวมโควตาจาก aliases เป็น 10 budgets โดยอัตโนมัติ
แม้ keys ต่างกันก็ยังต้องตรวจว่าใช้ quota bucket เดียวกันหรือไม่

## ตรวจแบบ offline ก่อน

```powershell
python -m scripts.study.api854.check_accounts --secrets .local/api854/accounts-intake.private.json
```

ตรวจชนิดข้อมูล, missing keys, duplicate aliases, key ซ้ำ และ effective environment overrides
ผลออกเฉพาะ aliases/counts/status ไม่ออก key, email, credential fingerprint หรือ private file contents
Exit 0 หมายถึง local configuration ครบและพร้อมทำ authenticated preflight เท่านั้น
Exit 2 หมายถึงยังขาดข้อมูลหรือมีข้อมูลผิด ต้องตรวจผลที่ระบุ
คำสั่งนี้ไม่เรียก KKU/คิว ไม่ import quota ledger และไม่เขียนไฟล์บัญชี

Environment `KKU_API_KEY_A01` เป็นต้นมี precedence เหนือ key ในไฟล์ตาม worker เดิม
เมื่อ configuration ครบ ให้ตรวจ authenticated metadata/quota ตามขั้น preflight ที่ได้รับมอบหมาย
ยังไม่สร้างเทสด้วย KKU หรือเปิด pilotจากการได้รับ keys เพียงอย่างเดียว
ยังต้อง final shared inputs, provider limits/reserve และ Gate A ร่วมทีม

## ข้อความขอข้อมูลจากทีม

> ขอ keys ของบัญชีที่จัดให้ worker รวม 10 บัญชีเป็นไฟล์ผ่านช่องทางส่วนตัวครับ
> ใช้ aliases a01–a10 พร้อมระบุเจ้าของ/assignment และข้อมูล quota/models ที่มีหลักฐาน
> ค่า bucket/reset/expiry หรือ limits ที่ยังไม่รู้ให้ระบุ unknown
> ส่ง path ของไฟล์บนเครื่องให้แชมป์ตรวจได้ ไม่ส่ง keys ลงแชทหรือ Git
> รอบนี้ตรวจ configuration offline ก่อน ยังไม่เรียก KKU เพิ่มหรือเปิด pilotครับ
