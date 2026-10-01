from pathlib import Path
s = Path('scripts/study/evaluate_provider_normalized_v52.py').read_text().replace('evaluate_provider_normalized_v52.py', 'evaluate_provider_normalized_v53.py').replace('ai-source-processing-v52', 'ai-source-processing-v53').replace('source-processing-v52', 'source-processing-v53').replace('from provider_prefix_v52 import repair', 'from provider_prefix_v53 import repair')
s = s.replace('PROCESSING_SOURCES = (', "PROCESSING_SOURCES = ('scripts/study/provider_prefix_v53.py', ")
Path('scripts/study/evaluate_provider_normalized_v53.py').write_text(s)
