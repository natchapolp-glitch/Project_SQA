from pathlib import Path
import hashlib,json
root=Path(__file__).resolve().parents[2]
target=root/'scripts/study/evaluate_provider_normalized_v87.py'
if target.exists(): raise ValueError('Version exists')
text=(root/'scripts/study/evaluate_provider_normalized_v86.py').read_text().replace('v86','v87')
anchor="    source = path.read_text()\n"
addition='''    source = source.replace('new AnnotatedField(null, testField, null)', 'new AnnotatedField(testField, null)')
    source = source.replace('public boolean has(Class<?> cls)', 'public boolean has(Class<?> cls)')
    source = re.sub(r'@Override\\s+(public boolean has\\(Class<\\?> cls\\))', r'\\1', source)
    source = re.sub(r'@Override\\s+(public Annotation\\[\\] getAllAnnotations\\(\\))', r'\\1', source)
    helper = 'SimpleAnnotations(java.util.Map<Class<?>, Annotation> annotations) {'
    if source.count(helper) != 1: raise ValueError('Expected helper')
    source = source.replace(helper, 'public int size() { return annotations.size(); }\\n\\n        '+helper)
'''
text=text.replace(anchor,anchor+addition,1).replace('Assertions unchanged.', 'Adapt AnnotatedField fixture to the declared two-argument constructor; implement annotation count and remove overrides absent from this API. Retained assertions unchanged.')
target.write_text(text)
(root/'results/study/kku-only-20261001/processing-policy-v87.json').write_text(json.dumps({'basis':'Fixed constructor diagnostics and inspected fixed Annotations interface; fixture compatibility only; no assertion changes or buggy feedback','driver_sha256':hashlib.sha256(target.read_bytes()).hexdigest()},indent=2)+'\n')
