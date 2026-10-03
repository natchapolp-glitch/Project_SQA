"""Return Beam's scoped Buffer verdict; Champ approval is never inferred."""
from pathlib import Path
from datetime import datetime,timezone
import json
import sys
import xml.etree.ElementTree as ET

ROOT=Path.cwd();sys.path.insert(0,str(ROOT))
from scripts.study.api854.common import read_json,write_json,sha256,implementation_hashes
BASE=ROOT/'output/api854-20261003/beam-buffer-joint-review-v1'


def binding(path):return {'path':Path(path).relative_to(ROOT).as_posix(),'sha256':sha256(path)}


def main():
    historical=ROOT/'docs/api854/evidence/beam-buffer-development-20261003-v1'
    fixed=ROOT/'docs/api854/evidence/beam-buffer-reference-20261003-v1'
    integrity=[]
    for packet in (historical,fixed):
        checks=read_json(packet/'checksums.json')
        for rel,h in checks.items():assert sha256(packet/rel)==h,(packet,rel)
        integrity.append({'checksums':binding(packet/'checksums.json'),'files_checked':len(checks)})
    original=read_json(historical/'index.json')
    assert len(original['records'])==4
    for r in original['records']:
        assert r['fault_detected'] is False
        for c in r['stage_counts'].values():assert c['executed']==c['target_checks']==30 and c['skipped']==0
    reference=read_json(BASE/'reference/receipt.json')
    assert reference['passed'] and len(reference['records'])==2
    seal=read_json(BASE/'reference/preexecution-seal.json')
    assert implementation_hashes()==seal['runtime_source_sha256']
    for project in ('JacksonCore','Csv'):
        assert sha256(ROOT/seal['cases'][project]['path'])==seal['cases'][project]['sha256']
        assert sha256(ROOT/seal['suites'][project]['path'])==seal['suites'][project]['sha256']
    intake=read_json(BASE/'received-aom/receipt.json')
    template=BASE/'received-aom/joint-buffer-acceptance.template.json'
    report=read_json(template)
    assert sha256(BASE/'received-aom/receipt.json')==report['received_intake_sha256']
    cov=read_json(BASE/'received-aom/target-method-coverage.json')
    assert len(cov['rows'])==8
    # Recheck exact JVM descriptors in the retained XML; no result is changed.
    for row in cov['rows']:
        for e in row['evidence']:
            xml=ROOT/e['xml'];assert sha256(xml)==e['xml_sha256']
            methods=[m for c in ET.parse(xml).getroot().iter('class') if c.get('name')==row['class']
                for m in c.findall('./methods/method') if m.get('name')==row['method'] and m.get('signature')==row['descriptor']]
            assert len(methods)==1
            lines=[l for l in methods[0].findall('./lines/line') if int(l.get('number'))==e['entry_line']]
            assert len(lines)==1 and int(lines[0].get('hits'))==e['hits']
    rules={
        'inLongRange':('Digit-only numeric slice inside char[] padded with ## and ?; offset 2 and exact digit length. Sign flag chooses positive or negative long limit; sampled strings include 0, signed max and adjacent magnitudes.',
            'Exact Boolean result from decimal magnitude comparison against 9223372036854775807 or 9223372036854775808 for negative sign; independent expected values, not type-only observation.'),
        'parseInt':('Only ASCII decimal digits, legal offset 2 and 1-9 digits; no sign, whitespace, empty slice or overflow.',
            'Exact Integer value from the declared decimal digits; surrounding characters are excluded from the slice.'),
        'parseLong':('Only ASCII decimal digits, legal offset 2 and 10-18 digits fitting signed long; no sign, whitespace, empty slice or overflow.',
            'Exact Long value from the declared decimal digits; coupled offset/length preserve fixed implementation preconditions.'),
        'parseBigDecimal':('Valid decimal char[] or bounded slice at offset 2; four examples draw 0, 12.50 and -0.125; length equals complete decimal text.',
            'Exact BigDecimal string including sign and scale (12.50 remains 12.50); full-array and sliced overloads reviewed separately.'),
        'append':('Production TextBuffer(non-null BufferRecycler), resetWithCopy initializes contents 123 or 45.5. Source xABCDy or p12345q; coherent nonempty offset/length remain in bounds.',
            'Exact concatenation of initial contents and selected char[]/String slice, plus resulting size. Compare both contents and state, not void/type alone.'),
        'read':('Production ExtendedBufferedReader(StringReader) initially unread. Stream is A\\nBC\\nDE when vector[0]<0, otherwise 12\\n345\\n. char[8] is filled with ~; offset 1 or 2 and positive length up to 6 or 5 remain in bounds.',
            'Exact return count, written prefix of stream, sentinel preservation outside write range, newline-derived line counter and actual last character. Fresh receiver per case; no closed reader or fabricated stub.')}
    observations={p:read_json(BASE/f'reference/{p}/observations.json') for p in ('JacksonCore','Csv')}
    for c in report['candidates']:
        target=c['target'];project=target['project'];preconditions,oracle=rules[target['method']]
        samples=[r for r in observations[project] if all(r['target'][k]==target[k] for k in ('class','constructor_types','method','parameter_types'))]
        assert len(samples)==4 and all(r['independent_reference_passed'] for r in samples)
        record=next(r for r in reference['records'] if r['project']==project)
        row=next(r for r in cov['rows'] if r['class']==target['class'] and r['method']==target['method']
            and r['descriptor'].split(')')[0]+')'==__import__('scripts.study.api854.fixture_semantics',fromlist=['descriptor']).descriptor(target['parameter_types']))
        c.update(beam_verdict='accepted_for_prospective_bounded_shared_recipe_composition',champ_verdict=None,
            preconditions=preconditions,meaningful_oracle=oracle,
            fixed_source_sha256=seal['fixed_source_sha256'][project],
            fixture_helper_sha256=seal['runtime_source_sha256']['algorithms/java/SqaProbe.java'],
            historical_fixture_helper_sha256=report['beam_runtime_source_sha256']['algorithms/java/SqaProbe.java'],
            sampled_approach_target_coverage=row['evidence'],
            fixed_repeated_observations=samples,
            setup_failure_separation='Construction/argument/projection failure is fixture_error; target must be invoked. No catch-and-skip success, assertions repaired or targets selected using buggy outcomes.',
            evidence=[binding(BASE/f'reference/{project}/observations.json'),binding(BASE/f'reference/{project}/receipt.json'),
                binding(BASE/f'reference/{project}/evaluation/coverage/coverage.xml'),binding(BASE/'reference/preexecution-seal.json')],
            stage_counts=record['stage_counts'],reference_suite_sha256=record['suite_sha256'],
            accepted_into_shared_inputs=False)
    csv_affected=[r for r in observations['Csv'] if r['case_kind']=='existing_selected_csv_condition_reference']
    assert len(csv_affected)==10
    report['csv_stream_condition_change'].update(beam_verdict='accepted_as_explicit_prospective_condition_change',
        champ_verdict=None,agreed_condition=None,
        beam_proposed_condition={'fixture_policy_id':report.get('fixture_policy_id','beam-explicit-fixtures-v6-buffer-proposal'),
            'streams':{'negative_vector_0':'A\nBC\nDE','nonnegative_vector_0':'12\n345\n'},
            'receiver':'Fresh production StringReader and ExtendedBufferedReader for each target/case',
            'existing_selected_methods':['getLineNumber()','lookAhead()','read()','readAgain()','readLine()'],
            'new_overload':'read(char[],int,int)',
            'old_v5_behavior':'Must remain byte-exact in historical policy; no relabeling old stream results',
            'shared_knowledge_requirement':'All four approaches receive identical new stream recipes, source/context and prompts in a new combined condition'},
        evidence=[binding(BASE/'reference/Csv/observations.json'),binding(BASE/'reference/Csv/receipt.json')],
        existing_method_examples=10,fixed_observations=20)
    report.update(example_only=False,review_status='beam_scoped_acceptance_complete_champ_pending',
        checked_at_utc=datetime.now(timezone.utc).isoformat(),reviewer='beam',
        reviewer_runtime_commit='24a38184a8ffbe2e08d50fbab7020559746fe432',
        aom_review_commit='ec26350fc919d1be2b4a559c3f91b0072ba04f75',
        current_runtime_source_sha256=implementation_hashes(),
        producer_sha256=sha256(__file__),historical_packet_integrity=integrity,
        proposed_union={'selected':388,'unsupported':303,'denominator':691,'implemented':False,
            'condition':'Exactly Champ/Aom v9 380 + eight buffer additions; excludes Beam-only Lang helpers'},
        current_shared_preparation_changed=False,joint_acceptance_complete=False,
        semantic_scope='Bounded eight-signature recipe and explicit Csv stream condition; not full domain/declaration/team approval',
        primary_results_added=0,real_kku_requests=0,queue_mutations=0,
        limitations=['CMA-ES historical String append coverage is zero; independent reference coverage does not change that result.',
            'No final merged setter/JDOM/Math/Buffer runtime, preparation or reserve is created here.',
            'Four empty-enum exclusions and denominator 691 remain unchanged; joint requirement decision pending.'])
    write_json(BASE/'beam-buffer-verdict.json',report)
    write_json(BASE/'checksums.json',{p.relative_to(BASE).as_posix():sha256(p) for p in sorted(BASE.rglob('*'))
        if p.is_file() and p!=BASE/'checksums.json' and '__pycache__' not in p.parts and p.suffix not in ('.class','.pyc')})
    print(json.dumps({'beam_accepted_signatures':8,'champ_verdict':None,'csv_condition_beam_accepted':True,
        'proposed_union':388,'actual_shared_preparation_changed':False,'gate_a':False}))


if __name__=='__main__':main()
