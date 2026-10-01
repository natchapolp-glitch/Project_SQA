"""Write the account-specific local continuation receipt from audited evidence."""
import json
from pathlib import Path

root = Path(__file__).resolve().parents[2]
out = root / 'output/kku-only-20261001'
summary = json.loads((out/'summary.json').read_text(encoding='utf-8'))
audit = json.loads((out/'claude-evidence-audit.json').read_text(encoding='utf-8'))
assert summary['completed_runs'] == 169 and not audit['issues']
identities = [('JacksonCore',103),('Csv',103)]
records = [json.loads((root/f'results/study/kku-only-20261001/claude/{p}/intellisphere-s{i}-b30/evaluation/record.json').read_text()) for p,i in identities]
assert all(r['status']=='complete' for r in records)
assert sum(r['test_count'] for r in records)==58
capture = 'ai-tests/provider-captures/kku-only-20261001/claude/Math-1/s101-i1-thaninton-haiku-20261002'
quota = json.loads((root/capture/'quota-limit-observation.json').read_text())
assert quota['used_percent']==100
doc = root/'docs/CLAUDE_HAIKU_THANINTON_CHECKPOINT_20261002.md'
lines = ['# Claude Haiku — thaninton.u checkpoint, 2 October 2026', '',
 'Account: `thaninton.u@kkumail.com`; KKU IntelSphere, selected `claude-haiku-latest`.',
 'Original prepared prompts only; no execution logs or extra academic clarification sent.', '',
 '## Result', '',
 '- Daily limit visibly reached at 100% on Math/101. No subsequent prompt submitted.',
 '- Audited overall completion: **169/204**; Claude **16/51**, Gemini and both algorithms **51/51** each. **35 planned Claude identities remain incomplete**.',
 '- Nine captured requests on this account including Lang/103 from the prior interrupted turn; eight requests in the retry continuation. Two completed evaluations add **58 retained methods**.', '']
for r in records:
 lines.append(f"- {r['project']}/{r['seed']}: {r['test_count']} methods; fault detected {r['fault_detected']}; lines {r['line_covered']}/{r['line_total']}; branches {r['branch_covered']}/{r['branch_total']}.")
lines += ['', '## Processing and unsuccessful requests', '',
 '- JacksonCore/103: 74 methods in closed Java fences; incomplete trailing TextBuffer block omitted. First 30 source-order methods selected, two fixed assertion failures removed. Final 28; surviving assertions unchanged.',
 '- Csv/103: 37 original methods; source-order cap 30; no fixed failures pruned. Detects the selected bug.',
 '- Lang/103, Compress/102, Time/102, Chart/103, Collections/103, Gson/103, Math/101: captured refusal or missing dependency response; no Java suite. Evaluator records generation_failed, with no fabricated coverage.',
 '- Successful suites passed twice on fixed, ran on buggy, and produced coverage. Fresh Claude evidence audit: 13/13 complete records pass, zero issues; three reused baseline Claude records bring primary completion to 16.',
 '- Generation duration includes UI preparation and waiting, so it is an observation upper bound rather than pure model latency.', '',
 '## Evidence and remaining work', '',
 f'- Quota evidence: `{capture}/provider-screen.png` and `quota-limit-observation.json`.',
 '- Native exports, provider/model/account metadata, processing snapshots, failed fixed attempts, fixed/buggy logs and coverage are preserved locally.',
 '- Historical provenance gaps remain. The entire provenance audit does not pass.',
 '- Report PDF remains checkpoint 140 and slides checkpoint 136; final report/slides/package and Classroom submission remain unfinished.',
 '- Current continuation is local and unpushed; last published commit 846fb43c4a3daa8f48febef0da7b559186086acb.', '']
doc.write_text('\n'.join(lines),encoding='utf-8')
p=out/'delivery-status.json'
s=json.loads(p.read_text(encoding='utf-8'))
if s.get('claude_quota',{}).get('account')!='thaninton.u@kkumail.com':
 s['historical_claude_quota']=s.get('claude_quota')
s['claude_quota']={'status':'daily_limit_reached','account':quota['account'],'model':quota['model'],'used_percent':100,'observed_at':quota['observed_at'],'reset_time':None,'evidence':capture+'/provider-screen.png'}
s['experiment']='incomplete: 169/204; KKU Claude 16/51, KKU Gemini 51/51'
s['remaining_work']='35 Claude planned identities; historical provenance gaps; update final report/slides/package and submit Classroom.'
s['latest_local_test_update'].update(completed=169,pending=35,claude_completed_runs=16,new_claude_runs=2,new_claude_methods=58,claude_receipt=doc.relative_to(root).as_posix())
s['local_continuation_published']=False
p.write_text(json.dumps(s,ensure_ascii=False,indent=2)+'\n',encoding='utf-8')
p=root/'README.md'
s=p.read_text(encoding='utf-8')
head='''## Latest test update: 169/204 — Claude Haiku daily limit reached

KKU Claude is 16/51; KKU Gemini and both algorithms are 51/51 each. The thaninton.u continuation adds JacksonCore/103 (28 methods) and Csv/103 (30 methods); Csv detects the selected bug. Original exports and fixed-only processing are preserved. Claude reached its daily limit at 100%. See `docs/CLAUDE_HAIKU_THANINTON_CHECKPOINT_20261002.md`. 35 Claude identities remain incomplete. Report/slides are earlier checkpoints; latest changes are local and unpushed.

'''
p.write_text(head+s[s.index('# SQA Project 2.2'):],encoding='utf-8')
print(json.dumps({'completed':169,'new_runs':2,'new_methods':58,'quota':'daily_limit_reached'}))
