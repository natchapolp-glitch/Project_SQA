from pathlib import Path
s = Path('scripts/study/evaluate_provider_normalized_v54.py').read_text().replace('v54','v57')
s = s.replace('from provider_compatibility_v39 import repair','from provider_compatibility_v57 import repair')
s = s.replace("'scripts/study/provider_compatibility_v39.py',", "'scripts/study/provider_compatibility_v39.py',\n    'scripts/study/provider_compatibility_v57.py',")
s = s.replace('Local processing v57 (inherited compatibility chain v39):','Local processing v57: hash-identified Cli methods using fixed-compiler unsupported Option.setValue excluded; surviving assertions unchanged. Inherited compatibility chain v39:')
Path('scripts/study/evaluate_provider_normalized_v57.py').write_text(s)
