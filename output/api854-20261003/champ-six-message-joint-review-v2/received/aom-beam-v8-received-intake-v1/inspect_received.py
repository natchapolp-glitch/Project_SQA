"""Receive Beam's recomposed Math-only v8 evidence, without changing runtime.

--snapshot must contain a git archive of Beam 24a38184. Checksum manifests are
read independently from that immutable commit; output must be a new directory.
"""
import argparse
import hashlib
import json
from pathlib import Path
import subprocess
import xml.etree.ElementTree as ET

BEAM = '24a38184'
BASE = 'output/api854-20261003/beam-v8-received-v1'
MATH = 'docs/api854/evidence/beam-v8-math-development-20261003-v1'


def sha(data):
    return hashlib.sha256(data).hexdigest()


def require(value, message):
    if not value:
        raise ValueError(message)


def run(snapshot, output):
    snapshot, output = Path(snapshot), Path(output)
    require(not output.exists(), 'Refuse to overwrite sealed output')
    commit = subprocess.check_output(['git','rev-parse',BEAM],text=True).strip()
    bindings = {}
    def raw(path):
        data = (snapshot/path).read_bytes()
        bindings[path] = sha(data)
        return data
    def document(path):
        return json.loads(raw(path))
    total = 0
    for base in (BASE,MATH):
        original = subprocess.check_output(['git','show',commit+':'+base+'/checksums.json'])
        require(raw(base+'/checksums.json') == original, 'Manifest differs from immutable commit')
        for name,digest in json.loads(original).items():
            require(not Path(name).is_absolute() and '..' not in Path(name).parts, 'Unsafe member')
            require(sha(raw(base+'/'+name)) == digest, 'Checksum mismatch: '+name)
            total += 1
    require(total == 809, 'Expected 809 checksum entries')
    review = document(BASE+'/beam-v8-review.json')
    for item in review['input_binding'].values():
        if isinstance(item,dict) and 'path' in item:
            require(sha(Path(item['path']).read_bytes()) == item['sha256'], 'Historical Aom input changed')
    protocol = document(BASE+'/composition/protocol.proposal.json')
    index = document(BASE+'/preparation/index.json')
    require((len(index['records']),index['target_count'],index['capability_exclusion_count']) == (20,379,312), 'Scope')
    require(not index['generation_ready'] and protocol['enabled_stages']==[]
            and protocol['generation']['prompt_token_reserve'] is None, 'Development gates')
    pins = protocol['source_sha256']
    require(len(pins)==41 and index['runtime_source_sha256']==pins, 'Runtime inventory')
    for path,digest in pins.items():
        require(sha(raw(path))==digest, 'Snapshot runtime differs from preparation')
    prompts = []
    for row in index['records']:
        bug = f"{row['project']}-{row['bug_id']}"
        folder = BASE+'/preparation/'+bug
        targets = document(folder+'/targets.json')
        old = json.loads(Path('output/api854-20261003/prepare-v8-fraction-field-development',bug,'targets.json').read_bytes())
        require(targets['targets']==old['targets'], 'Math-only selection changed: '+bug)
        metadata = document(folder+'/prepare-metadata.json')
        prompt = raw(folder+'/prompt.md')
        require(len(prompt)==metadata['prompt_utf8_bytes'] and sha(prompt)==metadata['prompt_sha256'], 'Prompt binding')
        prompts.append({'bug':bug,'utf8_bytes':len(prompt),'sha256':sha(prompt)})
    require(max(r['utf8_bytes'] for r in prompts)==index['max_prompt_utf8_bytes']==264899, 'Measured prompt maximum')
    def stages(directory,count):
        for stage in ('fixed-1','fixed-2','buggy','coverage'):
            counts = document(directory+'/'+stage+'/sqa-stage-counts.json')
            require((counts['executed'],counts['skipped'],counts['target_checks'])==(count,0,count), 'Stage counters')
            command = document(directory+'/'+stage+'/command.json')
            require(command['exit_code']==0 and not command['timed_out'], 'Failed stage')
        xml = ET.fromstring(raw(directory+'/coverage/coverage.xml'))
        result = {key:int(xml.attrib[attr]) for key,attr in
                  [('line_covered','lines-covered'),('line_total','lines-valid'),
                   ('branch_covered','branches-covered'),('branch_total','branches-valid')]}
        entries=[]
        for cls_name in ('org.apache.commons.math3.fraction.BigFraction','org.apache.commons.math3.fraction.Fraction'):
            methods=[m for c in xml.findall('.//class') if c.attrib['name']==cls_name
                     for m in c.findall('./methods/method') if m.attrib['name']=='getField' and m.attrib['signature'].startswith('()')]
            # Java may emit a bridge method; record actual entry evidence without counting it as another declaration.
            hits=[{'descriptor':m.attrib['signature'],'entry_line':int(l.attrib['number']),'hits':int(l.attrib['hits'])}
                  for m in methods for l in m.findall('./lines/line') if int(l.attrib['hits'])>0]
            require(hits, 'No exact zero-argument getField coverage: '+cls_name)
            entries.append({'class':cls_name,'covered_lines':hits})
        return result,entries
    measured=[]
    for approach in ('fscs-art','cmaes'):
        folder=MATH+'/'+approach
        result=document(folder+'/evaluation/result.json')
        measurement=result['measurement']
        require(measurement['status']=='complete' and measurement['fixed_validation']=='passed_twice'
                and measurement['fault_detected'] is False, 'Math experiment outcome')
        coverage,entries=stages(folder+'/evaluation/measurement',30)
        require(all(measurement[k]==v for k,v in coverage.items()), 'Coverage XML/result disagreement')
        measured.append({'approach':approach,'executed_per_stage':30,'skipped':0,'target_checks':30,
                         'fault_detected':False,'getField_entry_evidence':entries,**coverage})
    ref=document(BASE+'/math-reference/receipt.json')
    require(ref['runtime_source_sha256']==pins
            and ref['shared_protocol_sha256']==sha(raw(BASE+'/composition/protocol.proposal.json'))
            and ref['shared_preparation_index_sha256']==sha(raw(BASE+'/preparation/index.json')),
            'Reference condition binding')
    cases=document(BASE+'/math-reference/observations.json')
    require(len(cases)==4 and ref['fixed_example_count']==4 and ref['fixed_observations']==8, 'Reference examples')
    for case in cases:
        target=case['target'];fraction='7/4' if case['vector'][0]==0.5 else '-3/4'
        expected='value:fraction-field:runtime=type:'+target['class']+':zero=fraction:0/1:one=fraction:1/1|state=fraction:'+fraction
        require(target['class'] in {'org.apache.commons.math3.fraction.BigFraction','org.apache.commons.math3.fraction.Fraction'}
                and target['constructor_types']=='double' and target['method']=='getField' and not target['parameter_types'], 'Reference signature')
        require(case['fixed_first']==case['fixed_second'] and case['fixed_first']['status']=='ok'
                and case['fixed_first']['target_invoked'] is True and case['fixed_first']['outcome']==case['expected']==expected,
                'Independent fraction reference')
    coverage,entries=stages(BASE+'/math-reference/evaluation',4)
    require(ref['fault_detected'] is False and ref['local_development_valid'] is True, 'Reference result')
    host=document(BASE+'/host-readiness.json');env=document(BASE+'/environment/environment.json')
    require(host['environment_sha256']==sha(raw(BASE+'/environment/environment.json'))
            and host['runtime_source_sha256']==pins and env['ready'] and env['defects4j_version']=='3.0.1', 'Host binding')
    require(host['cross_process_cpu_lock']=={'held_slot_rejected_exit':9,'released_slot_reusable_exit':0}
            and host['cpu_slots']==1, 'CPU lock receipt')
    for name in ('java','javac'):
        require(env['checks'][name]['exit_code']==0 and b'11.' in raw(BASE+'/environment/'+name+'/command.log'), 'JDK evidence')
    consumers=document(BASE+'/four-consumer-validation.json')
    require(consumers['tests_run']==4 and consumers['passed'] and consumers['skipped']==0
            and consumers['consumer_combinations']==80 and consumers['no_provider_or_queue_calls'], 'Received consumer result')
    for name in ('test_source','log','preparation_index','protocol'):
        item=consumers[name];require(sha(raw(item['path']))==item['sha256'], 'Consumer evidence binding')
    joint=document(BASE+'/joint-candidate-review.json')
    require(joint['reviewers']['champ'] is None and joint['accepted_candidates']==[], 'Pending joint verdict')
    receipt={'beam_commit':commit,'status':'received_math_v8_preparation_and_beam_host_evidence_verified',
        'checksum_entries_verified':total,'runtime_pins_verified':41,'bugs':20,'selected':379,'unsupported':312,
        'math_algorithm_suites':measured,'reference_cases':4,'reference_fixed_observations':8,
        'reference_getField_entry_evidence':entries,'beam_host_evidence_received':True,'beam_host_checked_at':env['checked_at_utc'],
        'beam_cpu_slots':1,'beam_cpu_lock_receipt':host['cross_process_cpu_lock'],
        'max_prompt_utf8_bytes':264899,'numerical_request_floor_before_unknown_framing':268995,
        'prompt_measurements':prompts,'received_consumer_tests':4,'received_consumer_combinations':80,
        'final_condition_adopted':False,'joint_verdict_pending':True,'known_selected_fixture_failure':'JDOM attributeIterator in this Math-only profile',
        'historical_aom_inputs_preserved':True,'primary_results_added':0,'real_kku_requests':0,'live_queue_mutations':0,
        'gate_a_approved':False,'final_prompt_reserve':None,'aom_defects4j_reruns':0,
        'inspector_sha256':sha(Path(__file__).read_bytes()),
        'limitations':['Host evidence received from Beam; not a current remote host or live transport probe.',
          'This is 379/312 Math-only development, not integrated v9, Buffer/Lang condition or final semantic approval.',
          'Prompt bytes and numerical guard floor are not provider token counts or quota authorization.']}
    output.mkdir(parents=True,exist_ok=False)
    for name,data in [('receipt.json',receipt),('checked-file-sha256.json',bindings)]:
        (output/name).write_bytes((json.dumps(data,indent=2)+'\n').encode())
    print(json.dumps({k:receipt[k] for k in ('status','checksum_entries_verified','selected','unsupported','max_prompt_utf8_bytes')},indent=2))


if __name__=='__main__':
    parser=argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--snapshot',required=True)
    parser.add_argument('--output',required=True)
    args=parser.parse_args();run(args.snapshot,args.output)
