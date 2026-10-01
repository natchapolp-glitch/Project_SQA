from pathlib import Path
s = Path('scripts/reporting/audit_kku_only.py').read_text()
old = "for code in re.findall(r'```java[^\\n]*\\n(.*?)\\n```', response_file.read_text(encoding='utf-8'), re.S | re.I):"
new = """# EOF extraction is permitted only for the disclosed, versioned prefix recovery.
        pattern = r'```java[^\\n]*\\n(.*?)\\n```'
        recovery = any(e.get('local_syntax_recovery') for e in record.get('ai_local_processing_edits', []))
        if recovery and record.get('ai_source_processing_directory') in ('source-processing-v52', 'source-processing-v53'):
            pattern = r'```java[^\\n]*\\n(.*?)(?:\\n```|\\Z)'
        for code in re.findall(pattern, response_file.read_text(encoding='utf-8'), re.S | re.I):"""
assert old in s
s = s.replace(old, new)
Path('scripts/reporting/audit_kku_only_v3.py').write_text(s)
