from pathlib import Path
import json, hashlib
from datetime import datetime, timezone

root=Path(__file__).resolve().parents[2]
out=root/'output/kku-only-20261001'
receipt_path=root/'results/validation/new-account-haiku-20261002/continuation-receipt.json'
if receipt_path.exists(): raise ValueError('Receipt already exists')
requests=[]
for cap in sorted((root/'ai-tests/provider-captures/kku-only-20261001/claude').glob('*/s*-i1-newaccount-*-20261002')):
    operator=json.loads((cap/'operator-metadata.json').read_text())
    request=json.loads((cap/'request-start.json').read_text())
    requests.append({'capture':cap.relative_to(root).as_posix(),'project':request['project'],
        'run_index':request['run_index'],'model':operator['model'],
        'capture_status':operator['capture_status'],'submitted_at':request['submitted_at'],
        'observed_usage_percent':operator['observed_claude_usage_percent'],
        'response_sha256':hashlib.sha256((cap/'response.md').read_bytes()).hexdigest(),
        'operator_sha256':hashlib.sha256((cap/'operator-metadata.json').read_bytes()).hexdigest(),
        'screenshot_sha256':hashlib.sha256((cap/'provider-screen.png').read_bytes()).hexdigest()})
requests.sort(key=lambda r:r['submitted_at'])
summary=json.loads((out/'summary.json').read_text())
receipt={'created_at_utc':datetime.now(timezone.utc).isoformat(),
    'authorization':'Owner requested new-account continuation, then explicitly selected Claude Haiku and finally requested preparation from current results without another account.',
    'previous_completed':178,'completed':summary['completed_runs'],'pending':204-summary['completed_runs'],
    'initial_quota_percent':0.0,'final_quota_percent':93.5,'requests':requests,
    'successful_new_identities':[{'project':p,'index':i,'methods':n,'fault_detected':f} for p,i,n,f in [('Time',102,30,False),('Mockito',103,21,False),('Compress',101,29,True)]],
    'processing_disclosure':'Time terminal incomplete Partial class preserved in raw DOM/code and an unclosed Markdown fence; only the complete UnsupportedDurationField class ingested, then capped to 30. Mockito fixed-only pruning removed 7 of 28 methods. Compress raw v73 had 44 methods; v75 excluded one whole incompatible-constructor method before source-order cap, leaving 43 before cap and 29 after one fixed-only exclusion. All retained assertions unchanged.',
    'failed_requests':'Lang Sonnet server busy; Lang Haiku and JacksonCore Haiku refusal; Gson insufficient-context response; JacksonDatabind compile failure. No metrics inferred for failed stages.',
    'account_scope':'User-authorized new KKU account observed in Profile. Public records use an alias and omit its identifier.',
    'current_summary_sha256':hashlib.sha256((out/'summary.json').read_bytes()).hexdigest(),
    'proof':{'path':'output/kku-only-20261001/claude-quota-proof-20261002_HAIKU.png','sha256':hashlib.sha256((out/'claude-quota-proof-20261002_HAIKU.png').read_bytes()).hexdigest()},
    'scope':'File hashes, observed UI labels and locally executed evaluation. Not proof of Classroom submission.'}
receipt_path.write_text(json.dumps(receipt,ensure_ascii=False,indent=2)+'\n',encoding='utf-8')
target=root/'scripts/reporting/build_kku_analysis_v5.py'
if target.exists(): raise ValueError('Builder already exists')
text=(root/'scripts/reporting/build_kku_analysis_v4.py').read_text(encoding='utf-8').replace('report-20261002_COMPLETE.md','report-20261002_HAIKU.md')
anchor="              '## โมเดลและที่มาของแต่ละกลุ่ม',"
addition="""              '## การทำต่อด้วยบัญชีใหม่และ Claude Haiku',
              'เจ้าของงานให้ทำต่อด้วยบัญชี KKU ใหม่ และเปลี่ยนให้ใช้ claude-haiku-latest ก่อนส่ง Time/102 ใช้ exact original prompts และ Normal เช่นเดิม เปิดแชทใหม่ทุก attempt ไม่ส่ง evaluation feedback ให้ AI หน้าเว็บแสดงใช้โควตาเริ่มต้น 0.0% และสุดท้าย 93.5% เจ้าของเลือกเตรียมชุดส่งจากผลปัจจุบัน ไม่มีบัญชีเพิ่ม จึงหยุดเก็บคำตอบโดยยังไม่อ้างว่าโควตาเต็ม',
              'เก็บ 8 requests: Sonnet 1 ครั้ง (Lang/103 server busy), Haiku 7 ครั้ง สำเร็จเพิ่ม 3 identities คือ Time/102 30 methods, Mockito/103 21 methods และ Compress/101 29 methods รวม 80 methods เพิ่มจาก 178 เป็น 181/204 รอบ Claude เพิ่มจาก 25 เป็น 28/51 รอบ Compress/101 ผ่าน fixed ซ้ำและมี assertion failure บน buggy ส่วน Time/102 และ Mockito/103 ไม่ตรวจพบ sampled bug',
              'Time/102 ถูกตัดกลางไฟล์ Partial แต่ไฟล์ UnsupportedDurationField จบครบ เก็บ code/DOM ดิบทั้งหมดและ serialize ส่วนท้ายเป็น fence ที่ไม่ปิด ไม่สร้าง source ที่ขาด จึงประเมินเฉพาะ complete class ตามกติกาเดิม Mockito/103 มี 28 methods ก่อน fixed-only pruning และตัด 7 methods ที่ไม่ผ่าน fixed ส่วน Compress/101 เดิมมี 44 methods; v75 ตัด testFormatPropagatedToEntry ทั้งเมธอดจาก fixed compiler ที่ไม่รองรับ CpioArchiveEntry() ก่อน cap เหลือ 43 methods ก่อน cap และ 29 หลัง fixed-only pruning ไม่เปลี่ยน retained assertions',
              'v73 และ v74 ใช้ generic policy เดิมและเก็บ failed attempts คนละ history; v75 เพิ่ม hash-guarded whole-method exclusion ของ Compress เท่านั้น Policies เขียนก่อน invocation แต่ละเวอร์ชัน Lang Haiku และ JacksonCore Haiku ปฏิเสธสร้างโค้ด Gson ระบุ context/dependencies ไม่พอ JacksonDatabind ได้ source แต่ fixture/import ไม่ตรง API และคอมไพล์ไม่ผ่าน เก็บ failures ตามจริง ดู results/validation/new-account-haiku-20261002/continuation-receipt.json และ processing-policy-v73/v74/v75.json',
"""
if anchor not in text: raise ValueError('Report insertion anchor missing')
target.write_text(text.replace(anchor,addition+anchor),encoding='utf-8')
print(json.dumps({'receipt':str(receipt_path),'requests':len(requests),'report_builder':str(target)}))
