from pathlib import Path
root=Path(__file__).resolve().parents[2]
target=root/'scripts/study/record_kku_capture_failure_v4.py'
if target.exists(): raise ValueError('Failure recorder v4 already exists')
text=(root/'scripts/study/record_kku_capture_failure_v3.py').read_text(encoding='utf-8')
text=text.replace("'truncated_first_test_body'))", "'truncated_first_test_body', 'scaffold_no_target_tests'))")
text=text.replace('    args = parser.parse_args()', "    parser.add_argument('--capture', type=Path, required=True)\n    args = parser.parse_args()")
start=text.index('    capture = ROOT / f\'ai-tests/')
end=text.index("\n    operator_path",start)
text=text[:start]+"    capture = args.capture.resolve()\n    if not capture.is_relative_to(ROOT/'ai-tests/provider-captures'):\n        raise ValueError('Capture path outside evidence tree')"+text[end:]
start=text.index("    if reason == 'truncated_no_complete_tests':")
text=text[:start]+'''    if reason == 'scaffold_no_target_tests':
        blocks=json.loads((capture/'rendered-code.json').read_text(encoding='utf-8'))
        source=blocks[0]['text'] if len(blocks)==1 else ''
        expected='44653e13ad4ea493f8d609a53f38e8188c2b5db114f8b80d312e095820de3c6f'
        if (args.family,args.project,args.index)!=('claude','JacksonDatabind',103) or hashlib.sha256(source.encode()).hexdigest()!=expected:
            raise ValueError('Scaffold differs from the reviewed capture')
        executable=re.sub(r'/\\*.*?\\*/|//[^\\n]*','',source,flags=re.S)
        if len(re.findall(r'@Test\\b',executable))!=24 or re.search(r'new\\s+BeanPropertyWriter\\b|\\bwriter\\s*\\.',executable):
            raise ValueError('Scaffold target-use review no longer applies')
        review={'reviewed_before_execution':True,'source_sha256':expected,'raw_declared_tests':24,
                'comment_only_test_methods':23,'remaining_test':'testGetSerializedName checks only SerializedString; target BeanPropertyWriter is never initialized or called.',
                'reason':'Illustrative scaffold lacks executable tests of the requested target. No compile, fixed, buggy, or coverage measurements inferred.'}
        (capture/'quality-review.json').write_text(json.dumps(review,indent=2)+'\\n',encoding='utf-8')
    elif reason == 'truncated_no_complete_tests':''' +text[start+len("    if reason == 'truncated_no_complete_tests':"):]
text=text.replace("'note':'No Java or evaluation outcome inferred; visible thinking is excluded.'", "'note':'Raw response retained. No target test execution or measured outcome inferred; visible thinking excluded. See quality-review.json for scaffold failures.'")
target.write_text(text,encoding='utf-8')
print(target)
