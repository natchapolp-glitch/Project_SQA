#!/usr/bin/env python3
"""Re-run a preserved Jsoup Claude suite in separate fixed/buggy demo checkouts."""
from pathlib import Path
import subprocess
import json
import hashlib
import re
from datetime import datetime, timezone

ROOT=Path(__file__).resolve().parents[2]
D4J='/home/team/sqa-round2/defects4j/framework/bin/defects4j'
WORK=Path('/home/team/sqa-round2/demo-20261001')
OUT=ROOT/'output/kku-only-20261001/demo'

def main():
    archive=ROOT/'results/study/kku-only-20261001/claude/Jsoup/intellisphere-s101-b30/evaluation/Jsoup-1f-intellisphere.101.tar.bz2'
    if WORK.exists() or OUT.exists():
        raise ValueError('Existing demo evidence must be preserved; do not rerun into this path')
    OUT.mkdir(parents=True); stages=[]
    for revision in ['1f','1b']:
        work=WORK/('Jsoup-'+revision)
        for label,command in [('checkout',[D4J,'checkout','-p','Jsoup','-v',revision,'-w',str(work)]),
                              ('test',[D4J,'test','-w',str(work),'-s',str(archive)])]:
            started=datetime.now(timezone.utc).isoformat()
            run=subprocess.run(command,text=True,stdout=subprocess.PIPE,stderr=subprocess.STDOUT,timeout=300)
            log=OUT/f'{revision}-{label}.log'; log.write_text(run.stdout)
            stage={'revision':revision,'label':label,'command':command,'started_at_utc':started,
                   'ended_at_utc':datetime.now(timezone.utc).isoformat(),'exit_code':run.returncode,
                   'log':log.relative_to(ROOT).as_posix(),'log_sha256':hashlib.sha256(log.read_bytes()).hexdigest()}
            if label=='test':
                match=re.search(r'Failing tests:\s*(\d+)',run.stdout)
                stage['failing_tests']=int(match.group(1)) if match else None
                failure=work/'failing_tests'
                if failure.exists():(OUT/f'{revision}-failing_tests').write_bytes(failure.read_bytes())
            stages.append(stage)
            if run.returncode:break
    tests={s['revision']:s for s in stages if s['label']=='test'}
    passed=all(s['exit_code']==0 for s in stages) and tests.get('1f',{}).get('failing_tests')==0 and (tests.get('1b',{}).get('failing_tests') or 0)>0
    result={'passed':passed,'archive':archive.relative_to(ROOT).as_posix(),'archive_sha256':hashlib.sha256(archive.read_bytes()).hexdigest(),
            'driver_sha256':hashlib.sha256(Path(__file__).read_bytes()).hexdigest(),'stages':stages,
            'scope':'Separate demo re-execution only. Does not replace or add primary experimental runs.'}
    (OUT/'rehearsal.json').write_text(json.dumps(result,indent=2)+'\n')
    print(json.dumps({'passed':passed,'failing_tests':{k:v['failing_tests'] for k,v in tests.items()}}))
    return not passed

if __name__=='__main__':raise SystemExit(main())
