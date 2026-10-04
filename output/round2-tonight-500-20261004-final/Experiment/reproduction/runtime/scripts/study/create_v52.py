from pathlib import Path
s = Path('scripts/study/evaluate_provider_normalized_v51.py').read_text()
s = s.replace('evaluate_provider_normalized_v51.py', 'evaluate_provider_normalized_v52.py').replace('ai-source-processing-v51', 'ai-source-processing-v52').replace('source-processing-v51', 'source-processing-v52')
s = s.replace('from provider_compatibility_v39 import repair', 'from provider_prefix_v52 import repair')
s = s.replace('PROCESSING_SOURCES = (', "PROCESSING_SOURCES = ('scripts/study/provider_prefix_v52.py', ")
assert r'(.*?)\n```' in s
s = s.replace(r'(.*?)\n```', r'(.*?)(?:\n```|\Z)')
Path('scripts/study/evaluate_provider_normalized_v52.py').write_text(s)
