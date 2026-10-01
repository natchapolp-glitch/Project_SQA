"""Record the audited natchapol continuation without replacing historical receipts."""
import json
from pathlib import Path
from PIL import Image

root = Path(__file__).resolve().parents[2]
out = root / 'output/kku-only-20261001'
audit = json.loads((out/'claude-evidence-audit.json').read_text())
summary = json.loads((out/'summary.json').read_text())
assert not audit['issues'] and summary['completed_runs'] == 174
identities = [('Compress',103),('Jsoup',103),('Time',103),('Codec',101),('Cli',102)]
records = [json.loads((root/f'results/study/kku-only-20261001/claude/{p}/intellisphere-s{i}-b30/evaluation/record.json').read_text()) for p,i in identities]
assert all(r['status']=='complete' for r in records)
assert sum(r['test_count'] for r in records) == 127
capture = 'ai-tests/provider-captures/kku-only-20261001/claude/Closure-1/s102-i1-natchapol-haiku-20261002'
Image.open(root/capture/'provider-screen.jpg').save(root/capture/'provider-screen.png')
quota = json.loads((root/capture/'quota-limit-observation.json').read_text())
assert quota['quota_percent'] == 100
doc = root/'docs/CLAUDE_HAIKU_NATCHAPOL_CHECKPOINT_20261002.md'
lines = ['# Claude Haiku natchapol checkpoint — 2 October 2026', '',
 'Account: natchapol.p@kkumail.com. KKU IntelSphere, claude-haiku-latest. Original prepared prompts only; no evaluation logs or clarification sent.', '',
 '- Overall audited completion: **174/204**. Claude **21/51**; Gemini, CMA-ES and FSCS-ART **51/51** each. **30 Claude identities remain incomplete**.',
 '- Five new completed evaluations add **127 retained test methods**.',
 '- Claude reached 100% daily usage on Closure/102. No further prompt submitted.',
 '- Fresh Claude evidence audit: 18/18 complete records passed, zero issues. Three reused baseline records bring the Claude total to 21.', '']
for r in records:
 lines.append(f"- {r['project']}/{r['seed']}: {r['test_count']} methods; fault detected {r['fault_detected']}; lines {r['line_covered']}/{r['line_total']}; branches {r['branch_covered']}/{r['branch_total']}.")
lines += ['', '## Processing and limitations', '',
 '- Compress: unsupported constructor methods excluded using fixed compile diagnostics; surviving assertions unchanged. Version 63 driver with version 61 compatibility processing.',
 '- Jsoup: filename header removed; unsupported selectFirst/hasParent methods excluded using fixed compile diagnostics. Version 65.',
 '- Cli: unsupported setValue methods excluded using fixed compile diagnostics. Version 66. Detects the selected bug.',
 '- Time and Codec: closed Java fences only, source-order cap 30. Codec removes one fixed assertion failure; no assertion expectations changed.',
 '- Operator repeats: Lang/102 and Math/102 were already complete. Native exports retained as additional captures; evaluator refused overwrite. Neither counted as a new primary run.',
 '- Closure/102 asked for additional context and produced no Java tests. Preserved as generation_failed; missing measurements remain null.',
 '- Response Style was Explanatory on this account, whereas earlier sessions used Normal. This UI setting difference is disclosed and cannot be treated as an identical configuration.',
 '- Generation timing includes UI preparation and waiting, an observed upper bound rather than pure model latency.',
 '- Historical provenance gaps remain; the entire provenance audit does not pass.',
 '- Report PDF remains checkpoint 140; presentation checkpoint 136. Final report, slides, package and Classroom submission remain unfinished.',
 '- Continuation is local and unpushed. Last published commit: 846fb43c4a3daa8f48febef0da7b559186086acb.',
 f'- Quota evidence: {capture}/provider-screen.png and quota-limit-observation.json.', '']
doc.write_text('\n'.join(lines),encoding='utf-8')
p=out/'delivery-status.json'
s=json.loads(p.read_text(encoding='utf-8'))
s['historical_thaninton_claude_quota']=s['claude_quota']
s['claude_quota']={'status':'daily_limit_reached','account':quota['account'],'model':quota['model'],'used_percent':100,'observed_at':quota['observed_at'],'reset_time':None,'evidence':capture+'/provider-screen.png'}
s['experiment']='incomplete: 174/204; KKU Claude 21/51, KKU Gemini 51/51'
s['remaining_work']='30 Claude planned identities; historical provenance gaps; update final report/slides/package and submit Classroom.'
s['latest_local_test_update'].update(completed=174,pending=30,claude_completed_runs=21,new_claude_runs=5,new_claude_methods=127,claude_receipt=doc.relative_to(root).as_posix())
s['local_continuation_published']=False
p.write_text(json.dumps(s,ensure_ascii=False,indent=2)+'\n',encoding='utf-8')
p=root/'README.md'
body=p.read_text(encoding='utf-8')
head='## Latest test update: 174/204 — Claude Haiku daily limit reached\n\nKKU Claude is 21/51; Gemini and both algorithms are 51/51 each. The natchapol continuation adds five completed runs and 127 retained methods. Cli/102 detects the selected bug. Claude reached 100% daily usage. See `docs/CLAUDE_HAIKU_NATCHAPOL_CHECKPOINT_20261002.md`. 30 Claude identities remain incomplete. Report/slides are earlier checkpoints; latest changes are local and unpushed.\n\n'
p.write_text(head+body[body.index('# SQA Project 2.2'):],encoding='utf-8')
print(json.dumps({'completed':174,'new_runs':5,'new_methods':127,'quota_percent':100}))
