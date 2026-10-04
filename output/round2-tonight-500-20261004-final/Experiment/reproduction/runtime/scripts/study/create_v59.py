from pathlib import Path
p = Path('scripts/study/provider_compatibility_v58.py').read_text()
p = p.replace("if '.getChildNodes(' in text[utf16_index(text,r['start_utf16']):utf16_index(text,r['end_utf16'])]", "if any(call in text[utf16_index(text,r['start_utf16']):utf16_index(text,r['end_utf16'])] for call in ('.getChildNodes(', 'doc.normalise(element)'))")
p = p.replace('Document.getChildNodes unavailable.', 'Document.getChildNodes unavailable and Document.normalise(Element) private.')
p = p.replace('v54 failed attempt preserved.', 'v54 and v58 failed attempts preserved.')
Path('scripts/study/provider_compatibility_v59.py').write_text(p)
s = Path('scripts/study/evaluate_provider_normalized_v58.py').read_text().replace('v58','v59')
s = s.replace('unsupported Document.getChildNodes excluded', 'unsupported Document.getChildNodes and private normalise(Element) excluded')
Path('scripts/study/evaluate_provider_normalized_v59.py').write_text(s)
