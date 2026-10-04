from pathlib import Path
s = Path('scripts/study/evaluate_provider_normalized_v54.py').read_text().replace('v54', 'v56')
s = s.replace('from provider_compatibility_v39 import repair', 'from provider_compatibility_v56 import repair')
s = s.replace("'scripts/study/provider_compatibility_v39.py',", "'scripts/study/provider_compatibility_v39.py',\n    'scripts/study/provider_compatibility_v56.py',")
s = s.replace('Local processing v56 (inherited compatibility chain v39):', 'Local processing v56: Historical XML fixture repair is dispatched only for the exact historical hash. New Haiku XML source initially evaluated unchanged. Inherited compatibility chain v39:')
Path('scripts/study/evaluate_provider_normalized_v56.py').write_text(s)
