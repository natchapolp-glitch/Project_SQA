# สภาพแวดล้อมและการทำซ้ำ

- Measured benchmark: Linux/Java11, Defects4J3.0.1 commit6d54320e0db5a357f9ab38a8e4d2e5aead7e1c09.
- TZ America/Los_Angeles. Coverage Cobertura, target ExtendedBufferedReader.
- Generation original: native Windows/Java17; algorithm/API timeเก็บจากproducer/HTTPreceipts.
- Python3 standard libraryสำหรับalgorithm/replay; Defects4J dependenciesติดตั้งตามofficial README.
- Rehearsalใช้hostจริงของออมและCPU1lock; ผลrehearsalไม่บวกเข้าการทดลองใหม่.
- Java source/suite/runtime SHA-256และsource derivationอยู่ในresults.json/source-bindings/evidence-index.

ติดตั้ง Defects4Jจาก https://github.com/rjust/defects4j/tree/v3.0.1 และรัน init.sh ตามคู่มือ.
Dependencies/downloadsของDefects4Jเป็น prerequisite; ไม่รวมJava/Defects4J runtime binariesในZIP.
เมื่อใช้รุ่น/commitอื่นต้องตรวจsource guardsก่อน และไม่ใช้ผลที่sourceไม่ตรงแทนตารางนี้.
