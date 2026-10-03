"""Embed observed records; optionally refresh only a local index on localhost."""
import argparse
import json
from pathlib import Path
from .common import read_json, sha256

PAGE='''<!doctype html><html lang="th"><head><meta charset="utf-8"><meta name="viewport" content="width=device-width,initial-scale=1"><title>Aom · SQA preparation</title>
<style>body{font:16px Tahoma,Arial,sans-serif;background:#f4f6fa;color:#172638;margin:0}main{max-width:1150px;margin:auto;padding:32px 24px}h1{margin:6px 0 12px;font-size:30px}.sub{color:#546477;line-height:1.65}.cards{display:grid;grid-template-columns:repeat(4,1fr);gap:14px;margin:24px 0}.card{background:white;border:1px solid #d9e2ec;border-radius:12px;padding:18px}.card b{display:block;font-size:30px;margin-top:12px}.notice{background:#fff5d9;border:1px solid #e3c66e;padding:16px;border-radius:10px;line-height:1.6}.tools{display:flex;gap:12px;flex-wrap:wrap;margin:24px 0}input,select,button{font:inherit;padding:10px;border:1px solid #b5c4d4;border-radius:8px;background:white}input[type=search]{flex:1;min-width:220px}button{cursor:pointer;color:#163957}.table{background:white;overflow:auto;border:1px solid #d9e2ec;border-radius:10px}table{border-collapse:collapse;width:100%}th,td{text-align:left;padding:13px;border-bottom:1px solid #e6edf4;white-space:nowrap}th{background:#edf2f8}a{color:#155da3}.tag{padding:5px 8px;border-radius:5px;background:#edf2f8}.pass{color:#16613a;background:#e4f5eb}.fail{color:#8e2f24;background:#ffede8}footer{font-size:13px;margin-top:24px;line-height:1.65;color:#546477;overflow-wrap:anywhere}@media(max-width:700px){.cards{grid-template-columns:repeat(2,1fr)}main{padding:20px 12px}h1{font-size:25px}}</style></head><body><main>
<div class="sub">Project SQA · Aom · 854-bug cohort</div><h1>เตรียมข้อมูลบั๊กของออม</h1>
<p class="sub">Fixed source และ build context สำหรับงานที่ออมรับผิดชอบ ใช้ผลสังเกตจริงจากไฟล์ index เท่านั้น</p>
<div class="notice"><strong>ยังเป็นการเตรียมข้อมูล</strong> การ checkout หรือ compile ผ่านยังไม่ใช่ผลทดสอบจากทั้ง 4 วิธี ชุด primary ใหม่ยังไม่ได้เปิดรัน</div>
<div class="cards"><div class="card">บั๊กที่รับผิดชอบ<b id="total"></b></div><div class="card">เก็บ source แล้ว<b id="source"></b></div><div class="card">Compile ผ่าน<b id="compiled"></b></div><div class="card">Primary สำเร็จ<b>0</b></div></div>
<p id="stamp" class="sub"></p><p id="mode" class="sub"></p><div class="tools"><input id="search" type="search" placeholder="ค้นหา project หรือ bug ID" aria-label="ค้นหา"><select id="state" aria-label="สถานะ"><option value="all">ทุกสถานะ</option><option value="prepared">Source พร้อม</option><option value="failed">พบปัญหา</option><option value="pending">ยังไม่เริ่ม / กำลังเตรียม</option></select><button id="load">โหลด index ล่าสุดจากเครื่อง</button><input id="file" type="file" accept=".json" hidden></div>
<p id="count" class="sub"></p><div class="table"><table><thead><tr><th>Project</th><th>Bug</th><th>Source</th><th>Compile</th><th>หลักฐาน</th></tr></thead><tbody id="rows"></tbody></table></div>
<dialog id="detail" aria-labelledby="detail-title" style="border:1px solid #b5c4d4;border-radius:12px;max-width:85vw"><h2 id="detail-title">สถานะที่บันทึก</h2><pre id="detail-record" style="font-size:13px;max-height:55vh;overflow:auto;white-space:pre-wrap;overflow-wrap:anywhere"></pre><p>Source และ stage logs อยู่ในแฟ้มหลักฐานของบั๊กนี้</p><button id="close-detail">ปิด</button></dialog>
<footer>ภาพรวมทั้งทีม: เป้าหมาย 854 bugs × 4 วิธี × 1 รอบ = 3,416 งานทดลอง รายการหน้านี้เป็นบั๊กของออมเท่านั้น<br>เปิดผ่าน localhost จะอ่านไฟล์ index ในเครื่องทุก 10 วินาที; เปิดเป็นไฟล์จะใช้ snapshot หรือเลือกโหลด index.json เอง ไม่มีการเชื่อมต่อคิวจริงหรือ KKU<br>SHA256 ของ index ตอนสร้างหน้า: __HASH__</footer></main><script>
let data=__DATA__;
const $=id=>document.getElementById(id);
function group(r){return r.status.includes('failed')||r.status.includes('changed')?'failed':r.source_prepared?'prepared':'pending'}
function recordHref(r){return r.evidence_folder&&new RegExp('^aom-owner-sources-v[123]/[A-Za-z][A-Za-z0-9]*-[1-9][0-9]*$').test(r.evidence_folder)?r.evidence_folder+'/record.json':`aom-owner-sources-v1/${encodeURIComponent(r.project)}-${Number(r.bug_id)}/record.json`}
function render(){const records=data.records;$('total').textContent=data.expected_bugs;$('source').textContent=records.filter(r=>r.source_prepared).length;$('compiled').textContent=records.filter(r=>r.compile_status==='passed').length;
$('stamp').textContent='ข้อมูล ณ '+new Date(data.updated_at_utc).toLocaleString('th-TH',{timeZone:'Asia/Bangkok'})+' · พบปัญหา '+records.filter(r=>group(r)==='failed').length+' · ยังไม่เริ่ม/กำลังเตรียม '+records.filter(r=>group(r)==='pending').length+' · แก้ไข source แล้ว '+(data.source_recovery_count||0);
const q=$('search').value.toLowerCase(),s=$('state').value;const shown=records.filter(r=>(s==='all'||group(r)===s)&&`${r.project} ${r.bug_id} ${r.project}-${r.bug_id}`.toLowerCase().includes(q));$('count').textContent=`แสดง ${shown.length} จาก ${records.length} บั๊ก`;$('rows').replaceChildren();
for(const r of shown){let tr=document.createElement('tr');for(const value of [r.project,r.bug_id,r.source_prepared?'เก็บแล้ว':r.status==='not_attempted'?'ยังไม่เริ่ม':r.status]){let td=document.createElement('td');td.textContent=value;tr.append(td)}let td=document.createElement('td'),tag=document.createElement('span');tag.className='tag '+(r.compile_status==='passed'?'pass':['failed','timeout'].includes(r.compile_status)?'fail':'');tag.textContent=r.compile_status;td.append(tag);tr.append(td);td=document.createElement('td');if(r.status!=='not_attempted'){let a=document.createElement('a');a.textContent='record.json';a.href=recordHref(r);let button=document.createElement('button');button.textContent='ดู record';button.onclick=()=>{$('detail-title').textContent=r.project+'-'+r.bug_id;$('detail-record').textContent=JSON.stringify(r,null,2);$('detail').showModal()};td.append(button,document.createTextNode(' '),a)}else td.textContent='—';tr.append(td);$('rows').append(tr)}}
function checked(next){if(next.owner!=='aom'||next.expected_bugs!==284||next.primary_completed!==0||!Array.isArray(next.records)||next.records.length!==284)throw Error('ต้องใช้ index ของ source preparation ออม 284 บั๊ก');return next}
let automatic=location.protocol==='http:'&&['127.0.0.1','localhost','[::1]'].includes(location.hostname),refreshing=false;
$('mode').textContent=automatic?'ติดตามไฟล์ index ในเครื่องทุก 10 วินาที':'เปิดจากไฟล์: ใช้ snapshot หรือเลือก index ล่าสุดจากเครื่อง';
async function refreshLocal(){if(!automatic||refreshing)return;refreshing=true;try{const response=await fetch(__INDEX_PATH__,{cache:'no-store'});if(!response.ok)throw Error('อ่าน index ไม่สำเร็จ');const next=checked(await response.json());if(automatic){data=next;render();$('mode').textContent='ติดตามไฟล์ index ในเครื่องทุก 10 วินาที'}}catch(e){if(automatic)$('mode').textContent='ใช้ snapshot ล่าสุด: ยังอ่านไฟล์ index ในเครื่องไม่ได้'}finally{refreshing=false}}
$('close-detail').onclick=()=>$('detail').close();$('search').addEventListener('input',render);$('state').addEventListener('change',render);$('load').onclick=()=>$('file').click();$('file').onchange=async e=>{try{const next=checked(JSON.parse(await e.target.files[0].text()));automatic=false;data=next;render();$('mode').textContent='ใช้ index ที่เลือกจากเครื่อง; หยุดการอ่านอัตโนมัติ'}catch(e){alert(e.message)}};render();if(automatic){refreshLocal();setInterval(refreshLocal,10000)}
</script></body></html>'''


def build(index, output):
    data=read_json(index)
    assert data['owner']=='aom' and data['expected_bugs']==len(data['records'])==284
    assert data['primary_completed']==0
    encoded=json.dumps(data,ensure_ascii=False).replace('<','\\u003c')
    local_index=json.dumps(index.resolve().relative_to(output.parent.resolve()).as_posix()).replace('<','\\u003c')
    output.write_text(PAGE.replace('__DATA__',encoded).replace('__HASH__',sha256(index)).replace('__INDEX_PATH__',local_index),encoding='utf-8')


if __name__=='__main__':
    cli=argparse.ArgumentParser(description=__doc__)
    cli.add_argument('--index',required=True,type=Path)
    cli.add_argument('--output',required=True,type=Path)
    args=cli.parse_args()
    build(args.index,args.output)
