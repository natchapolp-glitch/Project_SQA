from pathlib import Path
s = Path('scripts/study/evaluate_provider_normalized_v51.py').read_text()
s = s.replace('evaluate_provider_normalized_v51.py', 'evaluate_provider_normalized_v54.py').replace('ai-source-processing-v51', 'ai-source-processing-v54').replace('source-processing-v51', 'source-processing-v54')
s = s.replace('provider-source-processing-v39', 'provider-source-processing-v54')
s = s.replace('Local processing v39:', 'Local processing v54 (inherited compatibility chain v39):')
Path('scripts/study/evaluate_provider_normalized_v54.py').write_text(s)
