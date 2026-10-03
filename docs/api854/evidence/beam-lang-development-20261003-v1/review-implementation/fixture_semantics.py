"""Review retained development cases without changing suites or measured results."""
import argparse
import base64
from pathlib import Path
import xml.etree.ElementTree as ET

from .common import read_json, write_json, sha256, identifier, contained
from .evaluate_worker import review_validity
from .fixture_policy import POLICY_V5, POLICY_V6_BUFFER, POLICY_V9_LANG

PRECONDITIONS = {
    'Codec': 'Production Metaphone receiver with bounded strings and actual encoded outputs; encode(Object) receives an explicit non-String boundary and documents EncoderException.',
    'Collections': 'Production Flat3Map with deterministic entries; mutation and views compare actual entries; serialization hooks are outside this v5 sampled recipe.',
    'Csv': 'Real StringReader over deterministic line-separated text; character/line/EOF outputs and reader position are compared.',
    'Lang': 'Non-null deterministic String boundaries; nonempty primitive arrays. NumberUtils create* documents NumberFormatException for invalid numeric text.',
    'JacksonCore': 'NumberInput receives decimal integer strings. TextBuffer receives a non-null recycler and resetWithCopy allocates initialized private storage before append.',
    'Math': 'Finite nonzero quarter-valued fraction receivers and positive bounded scalar operands; projections compare numerator and denominator.',
    'Jsoup': 'Production Document constructor followed by html/head/body shell and content; tag creation uses valid names; results and mutations compare actual HTML.',
    'Cli': 'Production CommandLine with known Option x/String value and positional argument; actual lookup/mutation results and option/value/argument state are compared.',
    'Compress': 'Production CPIO stream over ByteArrayOutputStream with explicit time=0/mode/name/size. Current entry size is 1 for write(int), 0 otherwise; byte output is compared.',
    'Time': 'Non-null production duration field types; Partial starts with hourOfDay=10 and uses bounded hours; type names and concrete partial values are projected.',
    'JxPath': 'Attached namespace-aware DOM tree built by JDK JAXP provider; coherent parent/child pointers and non-mutating namespace setup; node and iterator contents are compared.',
    'Gson': 'Declared generic schema fields with coherent String/Integer parameterized parents; array factories receive actual array types; resolved Type structure is compared.',
    'Closure': 'Production parser/compiler/Normalize pass produces normalized script and empty externs; call-site modification disabled; AST structure after target execution is compared.',
    'Chart': 'AreaRenderer initialized with real CategoryPlot, axes and nonempty deterministic dataset; range, legend labels and actual dimensions are projected; drawing methods excluded.',
    'JacksonDatabind': 'Production ObjectMapper factories create property writers or collection deserializer dependencies. Writer renaming/settings compare property type/name/state; collection parser/context and string deserializer form one coherent graph.',
    'JacksonXml': 'Real StAX reader over deterministic XML, non-null IOContext/XmlMapper and named leaf token before target; parser token/text/closed state is compared.',
    'Mockito': 'Real Mockito factory creates one mock and two recorded invocations; InvocationMatcher receives the actual production invocation, comparing method/arguments and match decisions.',
}


def descriptor(names):
    primitives = {'boolean':'Z', 'byte':'B', 'short':'S', 'int':'I', 'long':'J', 'float':'F', 'double':'D', 'char':'C'}
    return '(' + ''.join(primitives.get(n) or (n.replace('.', '/') if n.startswith('[') else 'L' + n.replace('.', '/') + ';')
        for n in names.split(',') if n) + ')'


def expected_exception(target, outcome, fixture_policy=POLICY_V5):
    if (fixture_policy == POLICY_V9_LANG and target['class'] == 'org.apache.commons.lang3.math.NumberUtils'
            and target['method'] == 'validateArray' and target['parameter_types'] == 'java.lang.Object'):
        boundaries = [('The Array must not be null', 'null'), ('Array cannot be empty.', '[I[]')]
        return outcome in {'exception:java.lang.IllegalArgumentException|message=java.lang.String:'
            + base64.b64encode(message.encode()).decode() + '|state=validation-input:' + state
            for message, state in boundaries}
    return ((target['class'] == 'org.apache.commons.lang3.math.NumberUtils'
        and target['method'] in {'createDouble','createFloat','createInteger','createLong','createNumber','createBigDecimal','createBigInteger'}
        and target['parameter_types'] == 'java.lang.String' and outcome == 'exception:java.lang.NumberFormatException')
        or (target['class'] == 'org.apache.commons.codec.language.Metaphone' and target['method'] == 'encode'
            and target['parameter_types'] == 'java.lang.Object' and outcome == 'exception:org.apache.commons.codec.EncoderException'))


def declaring_class(target):
    return ('org.jfree.chart.renderer.category.AbstractCategoryItemRenderer'
        if target['class'] == 'org.jfree.chart.renderer.category.AreaRenderer' and target['method'] != 'getLegendItem'
        else target['class'])


def review(root, review_tag=None):
    root = Path(root)
    suffix = '-' + identifier(review_tag, 'review tag') if review_tag else ''
    index = read_json(root / 'index.json')
    fixture_policy = index['fixture_policy_id']
    if fixture_policy not in {POLICY_V5, POLICY_V6_BUFFER, POLICY_V9_LANG}:
        raise ValueError('This reviewer implements the predeclared v5/buffer/Lang recipe scope only')
    reviews = []
    for row in index['records']:
        name = f"{row['project']}-{row['bug_id']}-{row['approach']}"
        if not row.get('evaluation_result'):
            reviews.append({'project':row['project'],'bug_id':row['bug_id'],'approach':row['approach'],
                            'verdict':'not_evaluated','local_development_usable':False})
            continue
        generation, evaluation = Path(row['generation_result']).parent, Path(row['evaluation_result']).parent
        generated, evaluated = read_json(generation / 'result.json'), read_json(evaluation / 'result.json')
        original_hash = sha256(evaluation / 'result.json')
        coverage = evaluation / 'measurement/coverage/coverage.xml'
        if not coverage.is_file():
            reviews.append({'project':row['project'],'bug_id':row['bug_id'],'approach':row['approach'],
                            'verdict':'incomplete_evaluation','local_development_usable':False})
            continue
        selected = read_json(generation / 'setup/targets.fixture-policy.json')['targets']
        coverage_files = [coverage]
        binding = evaluation / 'target-coverage-binding.json'
        supplemental_directory = evaluation / 'target-coverage'
        if binding.is_file():
            bound = read_json(binding)
            bound_directory = identifier(bound['directory'], 'supplemental coverage attempt')
            if not bound_directory.startswith('target-coverage'):
                raise ValueError('Invalid supplemental coverage binding')
            supplemental_directory = contained(evaluation, bound_directory)
            if sha256(supplemental_directory / 'result.json') != bound['result_sha256']:
                raise ValueError('Supplemental coverage binding differs')
        supplemental = supplemental_directory / 'result.json'
        if supplemental.is_file():
            extra = read_json(supplemental)
            target_xml = supplemental_directory / 'command/coverage.xml'
            if (extra['status'] != 'complete' or extra['suite_sha256'] != generated['suite_sha256']
                    or sha256(target_xml) != extra['coverage_sha256']
                    or extra['original_measurement_sha256'] != sha256(evaluation / 'measurement/record.json')):
                raise ValueError('Supplemental target coverage differs from immutable suite/measurement')
            coverage_files.append(target_xml)
        covered = {(cls.get('name'), m.get('name'), m.get('signature', '').split(')')[0] + ')')
            for coverage_file in coverage_files for cls in ET.parse(coverage_file).getroot().iter('class') for m in cls.findall('./methods/method')
            if any(int(line.get('hits','0')) > 0 for line in m.findall('./lines/line'))}
        cases = []
        for case in read_json(generation / 'suite/observations.json'):
            if not case['retained']:
                continue
            target, outcome = case['target'], case['fixed_first']['outcome']
            declaring = declaring_class(target)
            allowed = expected_exception(target, outcome, fixture_policy)
            cases.append({'case_id':case['case_id'],'target':target,'outcome':outcome,
                'two_fixed_observations_agree':case['fixed_first'] == case['fixed_second'],
                'target_invoked':case['fixed_first'].get('target_invoked') is True,
                'declared_recipe_selected':target in selected,
                'coverage_declaring_class':declaring,
                'target_method_covered':(declaring,target['method'],descriptor(target['parameter_types'])) in covered,
                'no_type_only_or_constructor_only_oracle':'object-type:' not in outcome and not outcome.startswith('constructed:'),
                'no_stateless_null_or_void_oracle':outcome not in {'value:null|state=stateless-scalars','void|state=stateless-scalars'},
                'expected_boundary_exception':allowed,'unexpected_exception':outcome.startswith('exception:') and not allowed})
        checks = ['two_fixed_observations_agree','target_invoked','declared_recipe_selected',
                  'target_method_covered','no_type_only_or_constructor_only_oracle','no_stateless_null_or_void_oracle']
        valid = (evaluated.get('evaluator_status') == 'complete' and len(cases) == generated['test_count']
                 and all(all(c[k] for k in checks) and not c['unexpected_exception'] for c in cases))
        evidence = evaluation / ('semantic-evidence' + suffix)
        evidence.mkdir(exist_ok=False)
        case_file = evidence / 'fixture-oracle-review.json'
        preconditions = ('Real fixed Closure compiler/type registry and parsed method-specific AST; declared x has a deterministic flow type, production control-flow and reverse interpreter; AST/type/flow outputs are projected.'
            if row['project'] == 'Closure' and row['bug_id'] == 176 else PRECONDITIONS[row['project']])
        if fixture_policy in {POLICY_V6_BUFFER, POLICY_V9_LANG} and row['project'] in {'JacksonCore', 'Csv'}:
            preconditions += ' v6-buffer: numeric digits and legal offset/length are coupled; TextBuffer slices use initialized storage; reader output includes written buffer and sentinel preservation.'
        if fixture_policy == POLICY_V9_LANG and row['project'] == 'Lang':
            preconditions = ('Existing numeric methods keep their bounded v5 inputs. isAllZeros explicitly covers null, empty, zero-only and nonzero text; validateArray receives null, empty int[], and nonempty int[] only. Successful validation compares actual array contents; documented null/empty rejection compares exception class, message and input state. No non-array object is selected as a legal fixture.')
        write_json(case_file, {'reviewer':'Codex local development review','fixture_policy_id':fixture_policy,
            'scope':'Retained development suites only; not all declarations or shared-contract/team approval',
            'method_preconditions':preconditions, 'cases':cases,
            'fixed_source_sha256':generated['fixed_source_sha256'],'suite_sha256':generated['suite_sha256'],
            'observations_sha256':sha256(generation / 'suite/observations.json'),
            'limitations':['Sampled suite validity does not establish complete target coverage.',
                'Partial projections do not prove full object equivalence; bounded deterministic domains are development recipes.',
                'Vectors can decode to repeated inputs; exception class assertions do not compare messages.',
                'Coverage includes fixture setup and state queries; target invocation is independently checked per test.',
                'No assertion repair or pruning based on buggy/fixed failures; all original observations/results retained.']})
        counts = {}
        for stage in ['fixed-1','fixed-2','buggy','coverage']:
            path = evaluation / 'measurement' / stage / 'sqa-stage-counts.json'
            measured = read_json(path)
            valid = valid and measured['executed'] == measured['target_checks'] == generated['test_count'] and measured['skipped'] == 0
            counts[stage] = {**{k:measured[k] for k in ['executed','skipped','target_checks']},
                'evidence':{'path':path.relative_to(evaluation).as_posix(),'sha256':sha256(path)}}
        document = {'reviewer':'Codex local development review','suite_sha256':generated['suite_sha256'],
            'fixed_source_sha256':generated['fixed_source_sha256'],'verdict':'valid' if valid else 'invalid',
            'target_execution_evidence':[{'path':coverage.relative_to(evaluation).as_posix(),'sha256':sha256(coverage)},
                {'path':case_file.relative_to(evaluation).as_posix(),'sha256':sha256(case_file)}],
            'fixture_oracle_review':{'scope':'local development only','reviewed_case_count':len(cases),
                'recipe_proof':preconditions, 'team_or_primary_approval':False,
                'helper_sha256':sha256(evaluation / 'implementation/algorithms/java/SqaProbe.java')},
            'weak_oracles':{'invalid_case_ids':[c['case_id'] for c in cases if not all(c[k] for k in checks) or c['unexpected_exception']],
                'expected_boundary_exception_case_ids':[c['case_id'] for c in cases if c['expected_boundary_exception']],
                'partial_projection_limitations_disclosed':True},'stage_counts':counts}
        if supplemental.is_file():
            document['target_execution_evidence'] += [{'path':p.relative_to(evaluation).as_posix(),'sha256':sha256(p)}
                for p in [supplemental, coverage_files[-1], supplemental_directory / 'command/sqa-stage-counts.json']
                + ([binding] if binding.is_file() else [])]
        path = root / f'{name}-semantic-review{suffix}.json'
        write_json(path, document)
        result = review_validity(path, generated['suite_sha256'], generated['fixed_source_sha256'], evaluated['measurement'], evaluation)
        destination = evaluation.parent / ('semantic-review' + suffix)
        destination.mkdir(exist_ok=False)
        write_json(destination / 'review.json', {'job':evaluated['job'],'evaluation_result_sha256':original_hash,**result})
        if sha256(evaluation / 'result.json') != original_hash:
            raise ValueError('Original measured result changed during review')
        reviews.append({'project':row['project'],'bug_id':row['bug_id'],'approach':row['approach'],
            'verdict':document['verdict'],'local_development_usable':result['usable'],
            'review_sha256':sha256(path),'original_result_unchanged':True,'team_or_primary_approval':False})
        print(name,document['verdict'],document['weak_oracles']['invalid_case_ids'],flush=True)
    write_json(root / ('semantic-review-index' + suffix + '.json'), {'primary':False,'gate_a_approved':False,
        'reviewer_sha256':sha256(__file__),'review_tag':review_tag,'reviews':reviews})
    return reviews


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--run',required=True,type=Path)
    parser.add_argument('--review-tag')
    args = parser.parse_args()
    review(args.run,args.review_tag)
