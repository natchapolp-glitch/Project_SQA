"""Publish a local checkpoint from matching audited records; no remote publication."""
import json
from pathlib import Path
root = Path(__file__).resolve().parents[2]
out = root / 'output/kku-only-20261001'
summary = json.loads((out/'summary.json').read_text(encoding='utf-8'))
audit = json.loads((out/'claude-evidence-audit.json').read_text(encoding='utf-8'))
assert summary['completed_runs'] == 167 and not audit['issues']
identities = [('Cli',103),('Codec',103),('Jsoup',102),('Csv',102)]
records = [json.loads((root/f'results/study/kku-only-20261001/claude/{p}/intellisphere-s{i}-b30/evaluation/record.json').read_text()) for p,i in identities]
assert all(r['status']=='complete' for r in records)
assert sum(r['test_count'] for r in records)==112
quota_capture = 'ai-tests/provider-captures/kku-only-20261001/claude/Csv-1/s102-i1-supakron-haiku-20261002'
quota = json.loads((root/quota_capture/'quota-limit-observation.json').read_text())
assert quota['used_percent']==100
lines = [
 '# Claude Haiku daily quota checkpoint — 2 October 2026', '',
 'Requested scope: continue Claude Haiku in KKU IntelSphere until its daily limit.',
 'Account: `supakron.k@kkumail.com`; selected model: `claude-haiku-latest`.',
 'Original prepared prompts only. No test logs, buggy outcomes or coverage feedback sent to the AI.', '',
 '## Result', '',
 '- KKU visibly reports **Claude reached daily usage limit**, 100%. No further prompt submitted.',
 '- Overall audited primary completion: **167/204**. Claude **14/51**; Gemini **51/51**; both algorithms **51/51**.',
 '- Four newly completed identities in this continuation; **112 retained methods**. These are team protocol counts, not instructor minimums.', '',
 '## Completed evaluations', '']
for r in records:
 lines += [f"- {r['project']}/{r['seed']}: {r['test_count']} retained methods; fault detected {r['fault_detected']}; lines {r['line_covered']}/{r['line_total']}; branches {r['branch_covered']}/{r['branch_total']}."]
lines += ['',
 'Each completed suite passed twice on fixed, ran on buggy and produced coverage. Audit v4 checks logs, counters, archive bytes, processing lineage and source freeze.',
 f"Fresh Claude audit: {audit['passed_records']}/{audit['completed_records_audited']} completed records pass; zero issues.", '',
 '## Processing and unsuccessful attempts', '',
 '- Cli/103: original 48 methods. v54 fails compilation on unavailable Option.setValue. v57 excludes 15 whole methods containing that API, caps remaining 33 at 30 and removes three fixed assertion failures. Final 27. Failed attempts preserved; surviving assertions unchanged.',
 '- Codec/103: 97 methods in closed Java fences. Cap 30, remove one fixed failure; final 29. Incomplete trailing SoundexUtils code block omitted rather than fabricated.',
 '- Jsoup/102: original 31 methods. v54 fails on unavailable getChildNodes; v58 fails on private normalise(Element). v59 excludes the two entire methods, then removes three fixed failures; final 26. Failed attempts preserved.',
 '- Csv/102: 31 methods, source-order cap 30. Detects selected bug. No local assertion edits.',
 '- Closure/101, Chart/102, Collections/102, Gson/102, Math/103 and JacksonCore/102: native responses captured, but no runnable Java suite. Reported as generation failures; missing coverage stays null.',
 '- Closure prompt was prepared before a long idle interval. Its reported generation duration is a UI observation upper bound containing that delay, not model latency.',
 '- Model quota changed from previous 85.1% to 14.6% after Closure, then reached 100% on Csv. Exact reset time is unknown.',
 '- Academic context clarification remains unsubmitted; original-prompt constraint retained.', '',
 '## Evidence and remaining work', '',
 f'- Quota evidence: `{quota_capture}/provider-screen.png` and `quota-limit-observation.json`.',
 '- Per-run raw exports, model/account metadata, source-processing snapshots, failed attempts, fixed/buggy logs and coverage retained locally.',
 '- **37 Claude planned identities incomplete**. Historical provenance gaps remain; the provenance audit is not fully passing.',
 '- Report PDF remains checkpoint 140 and slides checkpoint 136. Final report/slides/ZIP and Classroom submission remain unfinished.',
 '- Latest continuation is local and unpushed. Last published commit: 846fb43c4a3daa8f48febef0da7b559186086acb.', '']
doc = root/'docs/CLAUDE_HAIKU_QUOTA_CHECKPOINT_20261002.md'
doc.write_text('\n'.join(lines),encoding='utf-8')
p = out/'delivery-status.json'
s = json.loads(p.read_text(encoding='utf-8'))
s['experiment']='incomplete: 167/204; KKU Claude 14/51, KKU Gemini 51/51'
s['remaining_work']='37 Claude planned identities; historical provenance gaps; update final report/slides/package and submit Classroom.'
s['latest_local_test_update'].update(completed=167,pending=37,claude_completed_runs=14,new_claude_runs=4,new_claude_methods=112,claude_receipt=doc.relative_to(root).as_posix())
s['claude_quota']={'status':'daily_limit_reached','account':quota['account'],'model':quota['model'],'used_percent':100,'observed_at':quota['observed_at'],'reset_time':None,'evidence':quota_capture+'/provider-screen.png'}
s['local_continuation_published']=False
p.write_text(json.dumps(s,ensure_ascii=False,indent=2)+'\n',encoding='utf-8')
p=root/'README.md'
s=p.read_text(encoding='utf-8')
head='''## Latest test update: 167/204 — Claude Haiku daily limit reached

KKU Claude is 14/51; KKU Gemini and both algorithms are 51/51 each. This continuation adds four completed identities and 112 retained methods: Cli/103 27, Codec/103 29, Jsoup/102 26, Csv/102 30. Csv detects the selected bug. Original prompts, failed attempts and disclosed fixed-only processing are preserved. KKU explicitly reports Claude reached daily usage limit, 100%. See `docs/CLAUDE_HAIKU_QUOTA_CHECKPOINT_20261002.md`. 37 Claude identities remain incomplete. Report PDF/slides are earlier checkpoints; latest changes are local and unpushed.

'''
p.write_text(head+s[s.index('# SQA Project 2.2'):],encoding='utf-8')
print(json.dumps({'completed':167,'new_runs':4,'new_methods':112,'quota':'daily_limit_reached'}))
