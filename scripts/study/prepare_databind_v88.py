from pathlib import Path
import hashlib,json
root=Path(__file__).resolve().parents[2]
target=root/'scripts/study/evaluate_provider_normalized_v88.py'
if target.exists(): raise ValueError('Version exists')
text=(root/'scripts/study/evaluate_provider_normalized_v87.py').read_text().replace('v87','v88')
methods='''public BeanPropertyDefinition withName(String newName) { return new SimpleBeanPropertyDefinition(newName, required, wrapperName); }
        public String getInternalName() { return name; }
        public boolean isExplicitlyIncluded() { return false; }
        public boolean hasGetter() { return false; }
        public boolean hasSetter() { return false; }
        public boolean hasField() { return false; }
        public boolean hasConstructorParameter() { return false; }
        public AnnotatedMethod getGetter() { return null; }
        public AnnotatedMethod getSetter() { return null; }
        public AnnotatedField getField() { return null; }
        public AnnotatedParameter getConstructorParameter() { return null; }
        public AnnotatedMember getAccessor() { return null; }
        public AnnotatedMember getMutator() { return null; }
'''
anchor="    source = path.read_text()\n"
addition="    fixture = 'SimpleBeanPropertyDefinition(String name, boolean required, PropertyName wrapperName) {'\n    if source.count(fixture) != 1: raise ValueError('Expected property fixture')\n    source = source.replace(fixture, "+repr(methods)+"+'\\n        '+fixture)\n"
text=text.replace(anchor,anchor+addition,1).replace('Retained assertions unchanged.', 'Complete missing abstract API members of the provider property-definition stub using its stored name and absent-member semantics. Retained assertions unchanged.')
target.write_text(text)
(root/'results/study/kku-only-20261001/processing-policy-v88.json').write_text(json.dumps({'basis':'Fixed compiler missing abstract members; checked fixed BeanPropertyDefinition declarations. Complete provider stub with stored name and null/false absent members; disclose manual fixture completion, never call this raw AI output; retain assertions unchanged','driver_sha256':hashlib.sha256(target.read_bytes()).hexdigest(),'added_fixture_methods':methods},indent=2)+'\n')
