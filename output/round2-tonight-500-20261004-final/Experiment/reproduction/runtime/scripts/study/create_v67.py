"""Create a frozen generic processor for new 2 October captures only."""
from pathlib import Path
root=Path(__file__).resolve().parents[2]
target=root/'scripts/study/evaluate_provider_normalized_v67.py'
if target.exists(): raise ValueError('Version 67 already exists')
text=(root/'scripts/study/evaluate_provider_normalized_v60.py').read_text(encoding='utf-8')
text=text.replace('from provider_compatibility_v39 import repair','def repair(tests, project, tool, seed):\n    return []  # No historical, hash-specific compatibility repairs applied to fresh captures.')
text=text.replace('v60','v67')
start=text.index("            'Local processing v67")
end=text.index("\n            'Raw provider",start)
text=text[:start]+"            'Local processing v67: closed Java fences only, source-order cap of 30 methods, mechanical renderer suffix cleanup, and up to two fixed-only pruning passes. No inherited hash-specific compatibility repairs. Raw provider output remains unchanged.',"+text[end:]
target.write_text(text,encoding='utf-8')
print(target)
