from pathlib import Path
s = Path('scripts/study/evaluate_provider_normalized_v54.py').read_text().replace('v54','v58')
s = s.replace('from provider_compatibility_v39 import repair','from provider_compatibility_v58 import repair')
s = s.replace("'scripts/study/provider_compatibility_v39.py',", "'scripts/study/provider_compatibility_v39.py',\n    'scripts/study/provider_compatibility_v58.py',")
s = s.replace('Local processing v58 (inherited compatibility chain v39):','Local processing v58: hash-identified Jsoup method using fixed-compiler unsupported Document.getChildNodes excluded; surviving assertions unchanged. Inherited compatibility chain v39:')
Path('scripts/study/evaluate_provider_normalized_v58.py').write_text(s)
