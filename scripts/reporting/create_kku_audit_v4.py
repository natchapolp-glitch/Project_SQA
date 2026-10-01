"""Retain both v2's hash-identified v40 audit and v3's v52/v53 audit."""
from pathlib import Path
old = Path('scripts/reporting/audit_kku_only_v2.py').read_text()
new = Path('scripts/reporting/audit_kku_only_v3.py').read_text()
start = old.index("        if record.get('ai_source_processing_directory') == 'source-processing-v40':")
end = old.index("        for code in re.findall", start)
block = old[start:end]
anchor = "        for code in re.findall(pattern, response_file.read_text(encoding='utf-8'), re.S | re.I):"
assert new.count(anchor) == 1
new = new.replace(anchor, "        response_text = response_file.read_text(encoding='utf-8')\n" + block + "        for code in re.findall(pattern, response_text, re.S | re.I):")
anchor = "        for edit in record.get('ai_local_processing_edits', []):\n"
guard = "            if record.get('ai_source_processing_directory') == 'source-processing-v40' and edit == record['ai_local_processing_edits'][0]:\n                continue\n"
assert new.count(anchor) == 1
new = new.replace(anchor, anchor + guard)
Path('scripts/reporting/audit_kku_only_v4.py').write_text(new)
