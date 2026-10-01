"""Disclosed local compiler compatibility repairs; never uses buggy outcomes."""
import re
from normalize_provider_source import sha


def repair(tests, project, tool, seed):
    edits = []
    for path in sorted(tests.rglob('*.java')):
        text = path.read_text(encoding='utf-8')
        before = sha(path)
        changes = []
        substitutions = []
        if project == 'Cli' and tool == 'claude' and seed == 101:
            substitutions.append(('o.addValueForProcessing(value);', 'o.addValue(value);',
                'Fixed Option.java declares package-visible addValue(String).'))
        if project == 'Closure' and tool == 'intellisphere' and seed == 101:
            substitutions.append(('options.setLanguage(LanguageMode.ECMASCRIPT5);',
                'options.setLanguageIn(LanguageMode.ECMASCRIPT5);',
                'Fixed CompilerOptions.java exposes setLanguageIn(LanguageMode).'))
        if project == 'Time' and tool == 'intellisphere' and seed == 102:
            substitutions.append(('p.toString(null)', 'p.toString((String) null)',
                'Resolve String versus DateTimeFormatter overload without changing the argument value.'))
        if project == 'JacksonXml' and tool == 'intellisphere' and seed in (101, 103):
            substitutions.append(('.currentToken()', '.getCurrentToken()',
                'Use the older JsonParser token accessor for the fixed dependency version.'))
        for old, new, reason in substitutions:
            if old in text:
                n = text.count(old)
                text = text.replace(old, new)
                changes.append({'old':old,'new':new,'occurrences':n,'reason':reason})
        if project == 'JacksonCore' and tool == 'intellisphere' and seed == 103:
            pattern = r'assertThrows\((\w+)\.class, \(\) -> ([^;\n]+)\);'
            def replace(match):
                exception, expression = match.groups()
                replacement = ('try { ' + expression + '; fail("Expected ' + exception + '"); } '
                    'catch (' + exception + ' expected) { }')
                changes.append({'old':match.group(0),'new':replacement,
                    'reason':'Equivalent exception assertion for JUnit 4.10 and Java 6; no expected value changed.'})
                return replacement
            text = re.sub(pattern, replace, text)
        if changes:
            path.write_text(text, encoding='utf-8')
            edits.append({'path':path.relative_to(tests).as_posix(),
                'before_sha256':before,'after_sha256':sha(path),
                'local_compatibility_repairs':changes,
                'selection':'Selected from fixed compilation diagnostics and fixed API source only; no buggy feedback.'})
    return edits
