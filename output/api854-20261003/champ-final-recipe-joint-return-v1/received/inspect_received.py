"""Offline Aom receipt for scoped Beam Buffer/Csv verdict; no adoption or API.

Read checksum manifests from immutable Git objects, validate a source snapshot,
and independently reconstruct all 42 declared reference expectations.
"""
import argparse
import base64
from datetime import datetime
import hashlib
import json
from pathlib import Path
import subprocess
import xml.etree.ElementTree as ET

BEAM='2e11c7d92e8a16e01dfb2b7ec9e432765faaee7d'
BASE='output/api854-20261003/beam-buffer-joint-review-v1'
FIELDS=('class','constructor_types','method','parameter_types')


def sha(raw):return hashlib.sha256(raw).hexdigest()


def require(condition,message):
    if not condition:raise ValueError(message)


def bucket(a,n):return min(n-1,int((max(-1,min(1,a))+1)*0.5*n))


def scalar(kind,value):return kind+':'+base64.b64encode(str(value).encode()).decode()


def expected(case):
    target=case['target'];method=target['method'];a,b=case['vector'][:2]
    if target['class'].endswith('NumberInput'):
        if method=='inLongRange':
            text=['0','9223372036854775807','9223372036854775808','9223372036854775809'][bucket(a,4)]
            answer=int(text)<=(9223372036854775808 if b<0 else 9223372036854775807)
            kind,value='java.lang.Boolean',str(answer).lower()
        else:
            choices,kind={'parseInt':(['0','7','12345','999999999'],'java.lang.Integer'),
                'parseLong':(['1000000000','1234567890123','123456789012345678'],'java.lang.Long'),
                'parseBigDecimal':(['0','12.50','-0.125'],'java.math.BigDecimal')}[method]
            value=choices[bucket(a,len(choices))]
        return 'value:'+scalar(kind,value)+'|state=stateless-scalars'
    if target['class'].endswith('TextBuffer'):
        text='xABCDy' if a<0 else 'p12345q';offset=1 if a<0 else 2
        length=1+bucket(b,len(text)-offset-1)
        result=('123' if a<0 else '45.5')+text[offset:offset+length]
        return f'void|state=text:{result}:size={len(result)}'
    require(target['class']=='org.apache.commons.csv.ExtendedBufferedReader','Unexpected reference class')
    stream='A\nBC\nDE' if a<0 else '12\n345\n'
    if target['parameter_types']=='[C,int,int':
        offset=1 if a<0 else 2;length=1+bucket(b,8-offset-1)
        written=stream[:length];buffer=list('~'*8);buffer[offset:offset+len(written)]=written
        rendered='[C['+''.join(scalar('java.lang.Character',c)+';' for c in buffer)+']'
        return ('value:'+scalar('java.lang.Integer',len(written))
                +f"|state=reader:line={written.count(chr(10))}:last={ord(written[-1])}:buffer={rendered}")
    first=ord(stream[0]);line=0;last=-2
    if method=='readLine':value=stream.split('\n')[0];kind='java.lang.String';line=1;last=ord(value[-1])
    else:
        value={'getLineNumber':0,'lookAhead':first,'read':first,'readAgain':-2}[method];kind='java.lang.Integer'
        if method=='read':last=first
    require(case['declared_stream']==stream,'Stream declaration')
    return 'value:'+scalar(kind,value)+f'|state=reader:line={line}:last={last}'


def run(snapshot,output):
    snapshot,output=Path(snapshot),Path(output);require(not output.exists(),'Refuse to overwrite receipt')
    bindings={};manifest_files=set();entries=0
    def raw(path):
        data=(snapshot/path).read_bytes();bindings[path]=sha(data);return data
    def doc(path):return json.loads(raw(path))
    for rel in ('checksums.json','focused-checksums.json','reference/checksums.json'):
        path=BASE+'/'+rel;original=subprocess.check_output(['git','show',BEAM+':'+path])
        require(raw(path)==original,'Immutable manifest differs')
        parent=str(Path(path).parent).replace('\\','/')
        for name,digest in json.loads(original).items():
            require(not Path(name).is_absolute()and'..'not in Path(name).parts,'Unsafe member')
            member=parent+'/'+name;require(sha(raw(member))==digest,'Checksum: '+member)
            entries+=1;manifest_files.add(member)
    verdict=doc(BASE+'/beam-buffer-verdict.json');summary=doc(BASE+'/reference/receipt.json');seal=doc(BASE+'/reference/preexecution-seal.json')
    pins=summary['runtime_source_sha256'];require(len(pins)==41,'Runtime inventory')
    for path,digest in pins.items():
        require(sha(raw(path))==digest==sha(raw(BASE+'/reference/implementation/'+path)),'Runtime pin')
    require(seal['runtime_source_sha256']==pins and seal['declared_before_observation']
            and not seal['use_buggy_outcomes_for_selection']and not seal['alter_historical_suites'],'Seal scope')
    require(seal['producer_sha256']==summary['producer_sha256']==sha(raw(BASE+'/run_reference.py')),'Producer binding')
    require(verdict['csv_stream_condition_change']['beam_verdict']=='accepted_as_explicit_prospective_condition_change'
            and verdict['csv_stream_condition_change']['champ_verdict']is None
            and verdict['csv_stream_condition_change']['agreed_condition']is None,'Csv pending Champ verdict')
    old_intake=json.loads(Path('output/api854-20261003/aom-beam-c125-intake-v1/audit/receipt.json').read_bytes())
    key=lambda t:tuple(t[f]for f in FIELDS)
    additions={key(t)for t in old_intake['added_targets']}
    require(len(verdict['candidates'])==8 and {key(r['target'])for r in verdict['candidates']}==additions,'Exact candidate signatures')
    for candidate in verdict['candidates']:
        require(candidate['beam_verdict']=='accepted_for_prospective_bounded_shared_recipe_composition'
                and candidate['champ_verdict']is None and not candidate['accepted_into_shared_inputs'],'Candidate status')
        require(candidate['fixture_helper_sha256']==pins['algorithms/java/SqaProbe.java'],'Candidate runtime binding')
        for item in candidate['evidence']:
            require(sha(raw(item['path']))==item['sha256'],'Candidate evidence')
    require(verdict['received_intake_sha256']==sha(Path('output/api854-20261003/aom-beam-c125-intake-v1/audit/receipt.json').read_bytes()),'Received Aom intake')
    records=[];covered_additions=set();existing_csv=set()
    for project,count in [('JacksonCore',28),('Csv',14)]:
        folder=BASE+'/reference/'+project;declared=doc(folder+'/declared-cases.json');observed=doc(folder+'/observations.json')
        require(len(declared)==len(observed)==count,'Case counts')
        require(seal['cases'][project]['sha256']==sha(raw(folder+'/declared-cases.json'))
                and seal['suites'][project]['sha256']==sha(raw(folder+'/package/suite.tar.bz2')),'Preexecution cases/suite hash')
        for plan,case in zip(declared,observed):
            require(all(case[k]==v for k,v in plan.items()),'Declared case changed')
            reference=expected(case)
            require(case['expected']==reference and case['independent_reference_passed']
                    and case['fixed_first']==case['fixed_second']and case['fixed_first']['status']=='ok'
                    and case['fixed_first']['target_invoked']is True and case['fixed_first']['outcome']==reference,'Reference oracle')
            if case['case_kind']=='existing_selected_csv_condition_reference':
                existing_csv.add((case['target']['method'],case['declared_stream']))
                require('historical_v5_stream_observation'in case,'Historical comparison retained')
        record=doc(folder+'/receipt.json');measured=doc(folder+'/evaluation/record.json')
        require(record['suite_sha256']==seal['suites'][project]['sha256']==measured['suite_sha256'],'Unchanged measured suite')
        require(measured['status']=='complete'and measured['fixed_validation']=='passed_twice'
                and measured['fault_detected']is False,'Measured result')
        for stage in ('fixed-1','fixed-2','buggy','coverage'):
            counters=doc(folder+'/evaluation/'+stage+'/sqa-stage-counts.json');command=doc(folder+'/evaluation/'+stage+'/command.json')
            require((counters['executed'],counters['skipped'],counters['target_checks'])==(count,0,count),'Counters')
            require(command['exit_code']==0 and not command['timed_out'],'Command failed')
            require(datetime.fromisoformat(command['started_at_utc'])>datetime.fromisoformat(seal['created_at_utc']),'Stage predates seal')
            require(not raw(folder+'/evaluation/'+stage+'/failing_tests').strip()
                    and len(raw(folder+'/evaluation/'+stage+'/all_tests').splitlines())==count,'Raw test results')
        xml=ET.fromstring(raw(folder+'/evaluation/coverage/coverage.xml'))
        coverage={k:int(xml.attrib[a])for k,a in [('line_covered','lines-covered'),('line_total','lines-valid'),
                      ('branch_covered','branches-covered'),('branch_total','branches-valid')]}
        require(all(measured[k]==v for k,v in coverage.items()),'Coverage result/XML')
        entry_evidence=[]
        for item in record['exact_target_coverage']:
            methods=[m for cls in xml.findall('.//class')if cls.attrib['name']==item['class']
                     for m in cls.findall('./methods/method')if m.attrib['name']==item['method']
                     and m.attrib['signature'].split(')')[0]+')'==item['parameter_descriptor']]
            require(len(methods)==1,'Exact overload coverage')
            entry=min(methods[0].findall('./lines/line'),key=lambda l:int(l.attrib['number']))
            require(int(entry.attrib['hits'])>0,'No entry hits')
            entry_evidence.append({**item,'entry_line':int(entry.attrib['number']),'entry_hits':int(entry.attrib['hits'])})
        for case in observed:
            if key(case['target'])in additions:covered_additions.add(key(case['target']))
        records.append({'project':project,'reference_tests':count,'fixed_observations':2*count,
            'executed_each_stage':count,'skipped':0,'target_checks':count,'fault_detected':False,
            'entry_evidence':entry_evidence,**coverage})
    require(covered_additions==additions and len(existing_csv)==10,'Reference scope')
    historical=0
    for base in ('docs/api854/evidence/beam-buffer-development-20261003-v1','docs/api854/evidence/beam-buffer-reference-20261003-v1'):
        for p,digest in json.loads(Path(base,'checksums.json').read_bytes()).items():
            require(sha(Path(base,p).read_bytes())==digest,'Historical packet changed');historical+=1
    source_files=subprocess.check_output(['git','ls-tree','-r','--name-only','60cc1a6e','output/api854-20261003/prepare-v8-fraction-field-development'],text=True).splitlines()
    for p in source_files:require(Path(p).read_bytes()==subprocess.check_output(['git','show','60cc1a6e:'+p]),'Historical v8 changed')
    focused=doc(BASE+'/focused-tests.json')
    receipt={'beam_commit':BEAM,'status':'aom_scoped_buffer_and_csv_review_complete_champ_pending',
        'checksum_entries_verified':entries,'unique_received_files_verified':len(manifest_files),
        'historical_buffer_entries_unchanged':historical,'historical_v8_files_unchanged':len(source_files),
        'runtime_pins_verified':41,'reference_records':records,'independently_checked_cases':42,'fixed_observations':84,
        'buffer_signatures_checked':8,'existing_csv_method_stream_pairs':10,
        'aom_scoped_verdict':'suitable_for_bounded_prospective_composition_subject_to_champ_verdict',
        'csv_condition_scope':'Five existing selected reader methods plus new read(char[],int,int), fresh stream per case',
        'beam_verdict_sha256':sha(raw(BASE+'/beam-buffer-verdict.json')),
        'preexecution_seal_sha256':sha(raw(BASE+'/reference/preexecution-seal.json')),
        'reference_receipt_sha256':sha(raw(BASE+'/reference/receipt.json')),
        'proposed_union':{'selected':388,'unsupported':303,'denominator':691,'includes_lang_helpers':False,'implemented':False},
        'historical_cmaes_string_append_entry_hits':0,'historical_fscs_string_append_entry_hits':2,
        'joint_acceptance':False,'new_shared_preparation':False,'final_prompt_reserve':None,'gate_a_approved':False,
        'primary_added':0,'real_kku_requests':0,'live_queue_mutations':0,'aom_defects4j_reruns':0,
        'inspector_sha256':sha(Path(__file__).read_bytes()),
        'limitations':['Eight signatures and Csv conditions have bounded legal inputs only, not complete domain/691 semantic coverage.',
          'Independent reference coverage does not change the measured historical CMA-ES String append gap.',
          'Combined setter/JDOM/Math/Buffer runtime and prompts require explicit Champ verdict and a fresh shared condition.']}
    output.mkdir(parents=True,exist_ok=False)
    for name,data in [('receipt.json',receipt),('checked-file-sha256.json',bindings)]:
        (output/name).write_bytes((json.dumps(data,indent=2)+'\n').encode())
    print(json.dumps({k:receipt[k]for k in ['status','checksum_entries_verified','unique_received_files_verified','independently_checked_cases','historical_v8_files_unchanged']},indent=2))


if __name__=='__main__':
    parser=argparse.ArgumentParser(description=__doc__);parser.add_argument('--snapshot',required=True);parser.add_argument('--output',required=True)
    args=parser.parse_args();run(args.snapshot,args.output)
