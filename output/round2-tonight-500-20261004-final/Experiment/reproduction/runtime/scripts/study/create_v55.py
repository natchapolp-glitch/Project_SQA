from pathlib import Path
s = Path('scripts/study/evaluate_provider_normalized_v54.py').read_text()
s = s.replace('v54', 'v55')
s = s.replace('from provider_compatibility_v39 import repair', 'from provider_compatibility_v55 import repair')
s = s.replace("'scripts/study/provider_compatibility_v39.py',", "'scripts/study/provider_compatibility_v39.py',\n    'scripts/study/provider_compatibility_v55.py',")
s = s.replace('Local processing v55 (inherited compatibility chain v39):', 'Local processing v55: hash-identified JacksonCore non-Java filename header removed; Java statements and assertions unchanged. Inherited compatibility chain v39:')
Path('scripts/study/evaluate_provider_normalized_v55.py').write_text(s)
