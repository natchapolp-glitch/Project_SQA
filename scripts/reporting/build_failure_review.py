"""Summarize the eighteen KKU runs reviewed on 1 October (Bangkok)."""
from pathlib import Path
import json
import re
from datetime import datetime, timezone

ROOT = Path(__file__).resolve().parents[2]
CASES = [('Chart',102),('Chart',103),('Cli',103),('Closure',101),
         ('Closure',102),('Closure',103),('Compress',101),('Gson',102),
         ('Gson',103),('JacksonDatabind',101),('JacksonDatabind',102),
         ('JacksonDatabind',103),('JacksonXml',102),('JacksonXml',103),
         ('JxPath',103),('Math',103),('Mockito',101),('Mockito',103)]

def main():
    items = []
    for project, seed in CASES:
        run = ROOT / f'results/study/round2-v4-20260929/{project}/intellisphere-s{seed}-b30'
        record = json.loads((run/'evaluation/record.json').read_text(encoding='utf-8'))
        log = run/'evaluation/fixed-1/command.log'
        text = log.read_text(encoding='utf-8',errors='replace') if log.exists() else ''
        errors = re.findall(r'[^\n]*(?:error:|symbol:)[^\n]*',text)
        items.append({'project':project,'run_index':seed,'status':record['status'],
            'retained_methods':record.get('test_count'),'fault_detected':record.get('fault_detected'),
            'failed_stage':record.get('failed_stage'),'error':record.get('error'),
            'fixed_compilation_diagnostics':errors,
            'record':(run/'evaluation/record.json').relative_to(ROOT).as_posix(),
            'processing_driver':record.get('ai_execution_driver'),
            'follow_up':'Unsupported APIs or incomplete helper/fixture semantics require a separately preserved cohort; do not invent provider responses or alter assertions using buggy outcomes.' if record['status']!='complete' else 'Recovered suite passed fixed twice and was evaluated on buggy and for coverage.'})
    result={'reviewed_at_utc':datetime.now(timezone.utc).isoformat(),
            'initial_incomplete_runs_reviewed':18,'recovered_runs':sum(x['status']=='complete' for x in items),
            'scope':'All eighteen initial incomplete KKU primary runs inspected. Recovery is post-hoc and AI-assisted; original responses and prior attempts retained.',
            'runs':items}
    (ROOT/'output/submission/kku-failure-review.json').write_text(json.dumps(result,ensure_ascii=False,indent=2),encoding='utf-8')
    print(json.dumps({'reviewed':18,'recovered':result['recovered_runs']}))

if __name__ == '__main__':
    main()
