from pathlib import Path
root=Path(__file__).resolve().parents[2]
target=root/'scripts/reporting/package_kku_submission_v2.py'
if target.exists(): raise ValueError('Package builder v2 already exists')
text=(root/'scripts/reporting/package_kku_submission.py').read_text(encoding='utf-8')
text=text.replace('output/SQA_Round2_KKU_Only_Evidence.zip','output/SQA_Round2_KKU_Only_20261002_Evidence.zip')
text=text.replace('package-verification.json','package-verification-20261002.json')
text=text.replace('This local package retains original private evidence.','This package retains only original evidence available on this machine. Some screenshots from teammates were omitted from public Git and have not been received; see provenance-audit-current.json. No missing images were recreated.')
target.write_text(text,encoding='utf-8')
print(target)
