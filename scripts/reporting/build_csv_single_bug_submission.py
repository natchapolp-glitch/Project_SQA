"""Assemble a self-contained, single-bug submission from unchanged Csv evidence."""
from __future__ import annotations
import argparse
import csv
from datetime import datetime
import hashlib
import json
from pathlib import Path
import re
import shutil
import tarfile
import zipfile

ROOT = Path(__file__).resolve().parents[2]
OUT = ROOT / 'output/round2-single-bug-20261004'
DAY = ROOT / 'output/api854-20261004'
BASE = DAY / 'aom-ready-csv-intake-v1/baseline'
PEER = DAY / 'aom-ready-messages-intake-v1/peer/output/api854-20261004'
GEN = PEER / 'champ-csv-messages-generation-v2'
NATIVE = PEER / 'champ-csv-messages-native-measurement-v1'
BENCH = DAY / 'aom-ready-peer-intake-v1/beam/output/api854-20261004/beam-champ-csv-messages-d4j-v3'
METHODS = {'cmaes':'Algorithm1_CMAES', 'fscs-art':'Algorithm2_FSCSART',
           'kku-claude':'AI1_KKU_Claude', 'kku-gemini':'AI2_KKU_Gemini'}
LABELS = {'cmaes':'CMA-ES','fscs-art':'FSCS-ART','kku-claude':'Sonnet 5','kku-gemini':'Gemini 3.5 Flash Lite'}
MEMBERS = [('นายธนินธร อันทรบุตร','673380043-6'),('นายศุภกร กรมรินทร์','673380061-4'),
           ('นายณัชพล เพ็งพล','673380267-4'),('นายณัฐกรณ์ อินธิสาร','673380268-2')]

def sha(path): return hashlib.sha256(Path(path).read_bytes()).hexdigest()
def load(path): return json.loads(Path(path).read_text(encoding='utf-8-sig'))
def write(path, text):
    path=Path(path); path.parent.mkdir(parents=True,exist_ok=True)
    path.write_text(text,encoding='utf-8',newline='\n')
def save(path,obj): write(path,json.dumps(obj,ensure_ascii=False,indent=2,allow_nan=False)+'\n')

def assemble():
    OUT.mkdir(parents=True,exist_ok=False)
    provenance=[]
    def copy(source,dest):
        source=Path(source); dest=OUT/dest
        if source.is_dir():
            for f in sorted(source.rglob('*')):
                if f.is_file() and f.suffix not in {'.class','.pyc','.ser'}:
                    copy(f,dest.relative_to(OUT)/f.relative_to(source))
            return
        dest.parent.mkdir(parents=True,exist_ok=True); shutil.copyfile(source,dest)
        assert sha(source)==sha(dest)
        provenance.append({'source':source.relative_to(ROOT).as_posix(),
                           'destination':dest.relative_to(OUT).as_posix(),'sha256':sha(dest)})
    native_seal=load(NATIVE/'preexecution-seal.json')
    bench_seal=load(BENCH/'preexecution-seal.json')
    pins=native_seal['runtime_source_sha256']
    for path,digest in pins.items():
        assert sha(BASE/path)==digest, path
        copy(BASE/path,'Shared/frozen-runtime/'+path)
    copy(GEN/'received','Shared/inputs')
    copy(NATIVE/'algorithm-targets.json','Shared/inputs/algorithm-targets.json')
    copy(NATIVE/'preexecution-seal.json','Experiment/source-bindings/native-seal.json')
    copy(BENCH/'preexecution-seal.json','Experiment/source-bindings/benchmark-seal.json')
    copy(BENCH/'isolated-bug-reference','Experiment/source-bindings/isolated-bug-reference')
    copy(BENCH/'receipt.json','Experiment/source-bindings/benchmark-receipt.json')
    copy(GEN/'receipt.json','Experiment/source-bindings/generation-receipt.json')
    copy(PEER/'champ-csv-messages-generation-v1/failed-attempt.json','Experiment/retained-attempts/provider-label-check.json')
    copy(GEN/'kku-claude/request-not-resent.json','Experiment/retained-attempts/claude-response-reused.json')
    copy(ROOT/'scripts/reporting/csv_single_bug_replay.py','Presentation/replay-demo.py')
    copy(ROOT/'scripts/reporting/csv_single_bug_regenerate.py','Shared/regenerate-algorithms.py')
    rows=[]
    summary=list(csv.DictReader((DAY/'aom-ready-results-report-v6/latest-four-methods.csv').open(encoding='utf-8-sig',newline='')))
    for method,folder in METHODS.items():
        row=next(r for r in summary if r['project']=='Csv' and r['approach']==method)
        assert row['status']=='complete' and row['full_defects4j_completed']=='True'
        assert sha(ROOT/row['record_path'])==row['record_sha256']
        copy(BENCH/method/'evaluation',folder+'/Result/Defects4J')
        copy(NATIVE/method/'packaged-suite',folder+'/Test' if method in ('cmaes','fscs-art') else folder+'/TestCode')
        copy(NATIVE/method/'sources',folder+'/Test/sources' if method in ('cmaes','fscs-art') else folder+'/TestCode/sources')
        copy(NATIVE/method/'receipt.json',folder+'/Result/native-receipt.json')
        receipt=load(NATIVE/method/'receipt.json')
        if method in ('cmaes','fscs-art'):
            generation_seconds=receipt['generation_seconds']
            timing_kind='Native Windows generation; fixed observations and suite construction'
            copy(NATIVE/method/'algorithm-generation',folder+'/Result/Generation')
            copy(BASE/'algorithms/python/atcg'/('cmaes.py' if method=='cmaes' else 'fscs_art.py'),folder+'/Code/'+('cmaes.py' if method=='cmaes' else 'fscs_art.py'))
            copy(NATIVE/'algorithm-adapter.py',folder+'/Code/original-adapter.py')
            config={'project':'Csv','bug_id':1,'algorithm':method,'seed':101,'proposed_input_budget':30,
                    'bounds':[-1,1],'candidate_set_size':10 if method=='fscs-art' else None,
                    'sigma_fraction':0.30 if method=='cmaes' else None,
                    'fixture_policy':'aom-beam-champ-graphics-fixtures-v12-development',
                    'objective':'fixed behavior frequency minimization' if method=='cmaes' else 'normalized max-min input distance',
                    'target_list':'../../Shared/inputs/algorithm-targets.json',
                    'runtime':'../../Shared/frozen-runtime','oracle':'Two stable fixed-revision observations; not buggy failure feedback'}
            save(OUT/folder/'Configuration/config.json',config)
        else:
            raw=load(GEN/method/'response.json')['evidence']
            generation_seconds=(datetime.fromisoformat(raw['ended_at_utc'])-datetime.fromisoformat(raw['started_at_utc'])).total_seconds()
            timing_kind='Observed KKU HTTP submit-to-response wall time; not model compute time'
            for fn in ['raw-response.txt','generation-receipt.json','request-intent.json']:
                copy(GEN/method/fn,folder+'/Result/'+fn)
            copy(GEN/'received/prompt.md',folder+'/Prompt/prompt.md')
            intent=load(GEN/method/'request-intent.json')
            gr=load(GEN/method/'generation-receipt.json')
            config={'origin':'https://gen.ai.kku.ac.th','requested_model_id':gr['requested_model_id'],
                    'actual_model':gr['actual_model'],'endpoint_path':intent['endpoint_path'],
                    'temperature':intent['temperature'],'max_output_tokens':intent['max_tokens'],
                    'thinking':intent['thinking'],'prompt_sha256':intent['prompt_sha256'],
                    'usage':gr['usage'],'context':'Fixed source plus embedded frozen helper/fixture contract',
                    'processing':'Extract Java from fenced response; no assertion edits or fixed-only pruning for the selected suites'}
            save(OUT/folder/'Prompt/settings.json',config)
            save(OUT/folder/'Result/transport-timing.json',{'http_status':raw['http_status'],
                 'started_at_utc':raw['started_at_utc'],'ended_at_utc':raw['ended_at_utc'],
                 'observed_http_seconds':generation_seconds,'original_response_json_sha256':sha(GEN/method/'response.json')})
        testcase_dir='Test' if method in ('cmaes','fscs-art') else 'TestCode'
        suite=OUT/folder/testcase_dir/'suite.tar.bz2'
        assert sha(suite)==row['suite_sha256']
        r={k:row[k] for k in ['approach','declared_tests','fault_detected','line_covered','line_total','branch_covered','branch_total','duration_seconds','suite_sha256','record_sha256']}
        for field in ['declared_tests','line_covered','line_total','branch_covered','branch_total']:r[field]=int(r[field])
        r.update(project='Csv',bug_id=1,method=LABELS[method],fixed_failures=0,
                 buggy_failures=int(row['buggy_failed']),generation_seconds=generation_seconds,
                 generation_timing_scope=timing_kind,evaluation_seconds=float(row['duration_seconds']),
                 line_pct=100*int(row['line_covered'])/int(row['line_total']),
                 branch_pct=100*int(row['branch_covered'])/int(row['branch_total']),
                 suite_path=folder+'/'+testcase_dir+'/suite.tar.bz2',
                 result_path=folder+'/Result/Defects4J/record.json',generation_condition=native_seal['condition'],
                 execution_condition=bench_seal['execution_condition'])
        rows.append(r)
        write(OUT/folder/'README.md',f'# {LABELS[method]} - Csv-1\n\nชุดทดสอบและผลจริงของบั๊กเดียว Csv-1.\n\n'
              f'- Tests: {r["declared_tests"]}; fixed failures0; buggy failures{r["buggy_failures"]}.\n'
              f'- Target class lines {r["line_covered"]}/{r["line_total"]}; branches {r["branch_covered"]}/{r["branch_total"]}.\n'
              '- ใช้คำสั่งซ้อมรันใน [คู่มือ demo](../Presentation/demo-guide.md).\n'
              '- Runtime และ inputs ที่ตรงรุ่นอยู่ใน [Shared](../Shared/README.md).\n')
    data={'scope':'One bug, Csv-1; four methods; one generated suite per method',
          'project':'Csv','bug_id':1,'target_class':'org.apache.commons.csv.ExtendedBufferedReader',
          'generation_baseline':'63ad195623c2ed3f67f3ae232c00c54d3160ce72','generation_condition':native_seal['condition'],
          'execution_condition':bench_seal['execution_condition'],'canonical_evaluation_host':'beam-pc1',
          'framework_commit':'6d54320e0db5a357f9ab38a8e4d2e5aead7e1c09','java_major':11,'timezone':'America/Los_Angeles',
          'fixed_source_hashes':native_seal['production_source_sha256']['fixed']['sources'],
          'buggy_source_hashes':bench_seal['expected_isolated_buggy_source_sha256'],
          'runtime_source_hashes':pins,'members':[{'name':n,'id':i} for n,i in MEMBERS],
          'methods':rows,'gate_a_approved':False,'cohort_primary_result':False,'new_api_requests':0}
    save(OUT/'Experiment/results.json',data)
    with (OUT/'Experiment/summary.csv').open('w',encoding='utf-8-sig',newline='') as f:
        writer=csv.DictWriter(f,fieldnames=list(rows[0]));writer.writeheader();writer.writerows(rows)
    write(OUT/'Experiment/cases.csv','project_id,bug_id,target_class,scope\nCsv,1,org.apache.commons.csv.ExtendedBufferedReader,one_bug_four_methods\n')
    save(OUT/'Experiment/evidence-index.json',{'source_repository':'https://github.com/natchapolp-glitch/Project_SQA',
         'aom_report_commit':'e2ce1e2701e5d08a01cef0481e53ea621bc9956e','copied_original_files':provenance})
    write(OUT/'Shared/README.md','# Shared runtime and inputs\n\nFrozen 41-file runtime from generation v12; inputs and benchmark source bindings are unchanged.\n'
          'The embedded helper is needed to generate reflection-based tests. Archives already embed their required helper.\n'
          'Regenerate algorithms: `python3 Shared/regenerate-algorithms.py --d4j /path/to/defects4j --worktrees /path/to/worktrees --output /fresh/output`.\n'
          'New generated suites are separate replays and are never silently substituted into the reported table.\n')
    write(OUT/'Experiment/protocol.md',PROTOCOL)
    write(OUT/'Experiment/environment.md',ENVIRONMENT)
    write(OUT/'Presentation/demo-guide.md',DEMO)
    write(OUT/'Presentation/replay-demo.sh','#!/usr/bin/env bash\nset -euo pipefail\ncd "$(dirname "$0")/.."\nexec python3 Presentation/replay-demo.py "$@"\n')
    write(OUT/'README.md',README)
    print(json.dumps({'bundle':str(OUT),'copied_original_files':len(provenance),'methods':len(rows),'unique_bugs':1,'total_method_entries_per_stage':sum(r['declared_tests'] for r in rows)},ensure_ascii=False))

PROTOCOL='''# วิธีทดลอง Csv-1 บั๊กเดียว

เลือกบั๊ก Csv-1 และ target class `org.apache.commons.csv.ExtendedBufferedReader`.
เปรียบเทียบ CMA-ES, FSCS-ART, KKU Sonnet5 และ Gemini3.5FlashLite ด้วยหนึ่งชุดที่สร้างจริงต่อวิธี.
ตัวเลข tests คือ JUnit method entries ในแต่ละ suite ไม่ใช่จำนวนบั๊กหรือ unique input scenarios.

1. Algorithms ใช้ seed101/budget30 proposed vectors และ frozen fixture policy v12.
   เลือก6 exact method declarations, ใช้ fixed observationsสองครั้งสร้าง expected oracle.
   CMA-ES objective เป็นความถี่ fixed behavior; coverageไม่ได้ใช้เป็น fitness.
   FSCS-ART เลือก candidate ที่มีระยะห่างจากจุดเดิมใกล้ที่สุดมากสุด, candidate set10.
2. AIผ่าน KKU IntelSphere เท่านั้น. ส่ง prompt/contextเดียวกัน, temperature0/output4096;
   Sonnet Messages thinkingdisabled, Gemini Chat Completions. เก็บ raw responses/settings/model IDs.
   Contextของชุดนี้ใช้ fixed source/embedded helper; จึงเป็น fixed-assisted regression study.
3. Extract Javaจากresponseโดยไม่แก้ assertions ของชุดที่เลือก. เก็บ original files/hashes.
4. รันบน Defects4J3.0.1/Java11/TZ America/Los_Angeles: fixed1, fixed2, buggy และ coverage.
   Suiteเดิมทุกstage; fixed failures0เป็นเงื่อนไขของการนับfault.
   Buggy assertion failuresต้องสัมพันธ์กับofficialpatch; compile/environment failuresไม่ใช่fault.
5. Coverageวัด target classด้วย Cobertura. ตารางใช้ canonical Beam counted records,
   XML actual starts30/30/21/18 และ skipped0. Host replayอีกเครื่องไม่ใช่independentgenerationrepeat.
6. Native buggy sourceต่างจากactual reconstructedCsv-1b. ใช้ exact benchmarkhash/sourcebinding
   ที่ประกาศไว้ใน Experiment/source-bindings, ไม่แก้productionหรือassertionsให้ผ่าน.
7. แสดงgeneration/API wall timeแยกจากD4Jevaluation time เพราะใช้คนละhost/ขั้นตอน.
   Missing metricเป็นnull. ไม่สรุปspeed superiorityจากผลนี้.
8. ขอบเขตเพียงหนึ่งบั๊ก/หนึ่งclassและbounded inputs; ไม่อ้างครบ17projects/854bugs.

Provider label checkเดิมไม่รับชื่อMessages provider. ResponseSonnetเดิมถูกreconcileในconditionที่ประกาศไว้
โดยไม่ส่งคำขอซ้ำ; receiptอยู่ใน retained-attempts. ความผิดพลาดตรวจชื่อproviderไม่ใช่test failure.
ผลbaselineCsvเก่า truncated/fixed-invalid และ strict source mismatchเก็บอยู่ในrepoต้นฉบับ
ภายใต้ aom-ready-csv-d4j-v1/aom-ready-messages-d4j-v1 ไม่โอนมาเป็นผลผ่านของตารางนี้.
'''
ENVIRONMENT='''# สภาพแวดล้อมและการทำซ้ำ

- Measured benchmark: Linux/Java11, Defects4J3.0.1 commit6d54320e0db5a357f9ab38a8e4d2e5aead7e1c09.
- TZ America/Los_Angeles. Coverage Cobertura, target ExtendedBufferedReader.
- Generation original: native Windows/Java17; algorithm/API timeเก็บจากproducer/HTTPreceipts.
- Python3 standard libraryสำหรับalgorithm/replay; Defects4J dependenciesติดตั้งตามofficial README.
- Rehearsalใช้hostจริงของออมและCPU1lock; ผลrehearsalไม่บวกเข้าการทดลองใหม่.
- Java source/suite/runtime SHA-256และsource derivationอยู่ในresults.json/source-bindings/evidence-index.

ติดตั้ง Defects4Jจาก https://github.com/rjust/defects4j/tree/v3.0.1 และรัน init.sh ตามคู่มือ.
Dependencies/downloadsของDefects4Jเป็น prerequisite; ไม่รวมJava/Defects4J runtime binariesในZIP.
เมื่อใช้รุ่น/commitอื่นต้องตรวจsource guardsก่อน และไม่ใช้ผลที่sourceไม่ตรงแทนตารางนี้.
'''
DEMO='''# Demo: Csv-1 ครบสี่วิธี

## ก่อนนำเสนอ

เปิด Report/SQA_Round2_Report.pdf และ Presentation/SQA_Round2.pptx.
ใช้Linux/WSL+Java11+Defects4J3.0.1. ไม่ต้องlogin KKUหรือใช้โควตาสดเพื่อreplaytests.
ตัวอย่างบนเครื่องออม จากrootของชุดส่ง:

```bash
python3 Presentation/replay-demo.py \\
  --d4j /home/team/sqa-round2/defects4j/framework/bin/defects4j \\
  --worktrees /home/team/sqa-round2/worktrees \\
  --output /fresh/path/csv-demo
```

Outputต้องยังไม่มี; ถ้ามีattemptแล้วใช้dirnameใหม่. ตัว script ตรวจsuite/runtime/source hashesก่อนรัน
และใช้ CPU1lockเดียวกับworkerอื่น. ไม่เขียนทับผลทดลองหรือสั่งAPI.
ผลแต่ละstageอยู่ในapproach/evaluation; summary/receiptของrehearsalแยกจากcanonicalrecords.

## ลำดับการพูด

1. บอกขอบเขต: Csv-1บั๊กเดียว/หนึ่งtarget class/4methods. Methods count30/30/21/18.
2. เปิดโค้ด CMAES/FSCSART และalgorithm-targets.json: strategyเลือกinput, helperสร้างfixture,
   fixedobservationsสร้างoracleแล้วemitJUnit. ไม่เรียกCMAfitnessว่าcoverage.
3. เปิด AI1/AI2 Prompt/prompt.md, settings.json และraw-response.txt แสดงว่าใช้KKUจริง
   ไม่เปิดAPIkeysหรือไฟล์accounts. ชุดนี้ใช้fixedcontext ต้องอธิบายตรงไปตรงมา.
4. เปิด AI1 TestCode/sources/.../ExtendedBufferedReaderTest.java method lineNumberDoesNotDoubleCountCRLF:
   input A\\r\\nB; fixedผ่านแต่buggyคาด1ได้0.
   Gemini method testCarriageReturnLineNumber: input a\\rb\\nc; fixedผ่านแต่buggyคาด2ได้1.
5. แสดงfixedสองรอบและbuggylogs. AIพบfaultแต่algorithmsไม่พบในbounded domainนี้.
6. เปิดcoverage/summary.csv และExperiment/summary.csv. Lines31/37,31/37,36/37,37/37;
   branches13/26,13/26,22/26,23/26. แสดงinput-domain/timing limitationsก่อนสรุป.

ถ้าlive replayใช้เวลานานให้ใช้ผลซ้อมที่เก็บไว้ประกอบ แต่บอกว่าเป็นผลซ้อม/ผลบันทึก
ไม่เรียกภาพlogว่าการรันสด. ไม่มีการรับรองผลทั้งdatasetหรือการทำครบทุกproject.
'''
README='''# SQA รอบ2 - Csv-1 บั๊กเดียว / 4 วิธี

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
'''

def package():
    required=['Report/SQA_Round2_Report.pdf','Report/report-source.md','Presentation/SQA_Round2.pptx',
              'Presentation/rehearsal/receipt.json','Presentation/demo-guide.md','Experiment/summary.csv']
    for f in required: assert (OUT/f).is_file(), f
    assert load(OUT/'Presentation/rehearsal/receipt.json')['all_four_methods_verified']
    entries={p.relative_to(OUT).as_posix():sha(p) for p in sorted(OUT.rglob('*'))
             if p.is_file() and p.name!='checksums.json' and p.suffix not in {'.pyc','.class','.ser'}}
    save(OUT/'checksums.json',entries)
    z=OUT.parent/'SQA_Round2_Csv1_4Methods_20261004.zip'
    if z.exists(): raise FileExistsError(z)
    with zipfile.ZipFile(z,'w',zipfile.ZIP_DEFLATED) as out:
        for f in sorted(OUT.rglob('*')):
            if f.is_file() and f.suffix not in {'.pyc','.class','.ser'}:out.write(f,OUT.name+'/'+f.relative_to(OUT).as_posix())
    with zipfile.ZipFile(z) as inp:
        assert inp.testzip() is None
        for name,digest in entries.items():assert hashlib.sha256(inp.read(OUT.name+'/'+name)).hexdigest()==digest
    write(z.with_suffix(z.suffix+'.sha256'),sha(z)+'  '+z.name+'\n')
    print(json.dumps({'zip':str(z),'bytes':z.stat().st_size,'sha256':sha(z),'files':len(entries)+1,'unique_bugs':1}))

if __name__=='__main__':
    cli=argparse.ArgumentParser();cli.add_argument('stage',choices=['assemble','package'])
    stage=cli.parse_args().stage
    assemble() if stage=='assemble' else package()
