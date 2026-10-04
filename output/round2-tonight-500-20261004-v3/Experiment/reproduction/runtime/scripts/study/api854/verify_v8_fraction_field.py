"""Offline execution regression for composed v8 Math inputs, never a primary result."""
import argparse
import json
from pathlib import Path
import subprocess
import sys
import xml.etree.ElementTree as ET

from .common import ROOT, read_json, sha256, write_json, cpu_slot, implementation_hashes, contained
from .preparation import POLICY_V8, clean_targets
from .fixture_policy import POLICY_V6
from .build_prepare_v8_development import receive


def verify(preparation, protocol_path, output, trees, d4j):
    sys.path.insert(0,str(ROOT/'scripts/study'))
    import generate
    from run import stage
    from evaluate import EvaluationConfig, evaluate_run
    from .pack_suite import pack_suite
    preparation,protocol_path,output,trees = map(Path,(preparation,protocol_path,output,trees))
    receive()
    protocol,index = read_json(protocol_path),read_json(preparation/'index.json')
    if (protocol['source_sha256'] != implementation_hashes() or index['runtime_source_sha256'] != implementation_hashes()
            or protocol['generation']['prepare_contract'] != POLICY_V8['contract']
            or protocol['enabled_stages'] != [] or protocol['generation']['prompt_token_reserve'] is not None):
        raise ValueError('Require current closed v8 development composition')
    folder = preparation/'Math-1'
    targets = [t for t in read_json(folder/'targets.json')['targets'] if t['method']=='getField']
    if clean_targets(targets) != index['accepted_additions']:
        raise ValueError('Regression must exercise exactly the accepted two signatures')
    with cpu_slot(trees.parent):
        output.mkdir(parents=True,exist_ok=False)
        trees.mkdir(parents=True,exist_ok=False)
        for revision in ('f','b'):
            stage([d4j,'checkout','-p','Math','-v','1'+revision,'-w',trees/revision],ROOT,output/('checkout-'+revision),900)
        proof = read_json(folder/'revision-proof.json')
        head = subprocess.check_output(['git','-c','safe.directory=*','-C',str(trees/'f'),'rev-parse','HEAD'],text=True).strip()
        # Defects4J creates local timestamped commits during each checkout.
        # Verify that checkout's fixed tag, then compare every source with the
        # immutable preparation and its fresh Git HEAD, rather than equating
        # independently synthesized commit IDs across checkout directories.
        fixed_tag = subprocess.check_output(['git','-c','safe.directory=*','-C',str(trees/'f'),
            'rev-parse','D4J_Math_1_FIXED_VERSION^{commit}'],text=True).strip()
        if head != fixed_tag or proof.get('verified') is not True or proof['head'] != proof['fixed_tag_commit']:
            raise ValueError('Fresh or composed Math fixed revision proof differs')
        config = (trees/'f'/'.defects4j.config').read_text()
        if 'pid=Math' not in config or 'vid=1f' not in config:
            raise ValueError('Fresh checkout identity differs from Math-1f')
        for entry in read_json(folder/'context-manifest.json')['source_files']:
            path = contained(trees/'f',entry['path'])
            data = path.read_bytes()
            retained = subprocess.check_output(['git','-c','safe.directory=*','-C',str(trees/'f'),'show','HEAD:'+entry['path']])
            if sha256(path) != entry['sha256'] or data != retained:
                raise ValueError('Composed source differs from fresh fixed HEAD: '+entry['path'])
        stage([d4j,'compile','-w',trees/'f'],ROOT,output/'compile-fixed',900)
        cp = subprocess.check_output([d4j,'export','-p','cp.test','-w',str(trees/'f')],text=True).strip()
        classes = output/'probe-classes'
        classes.mkdir()
        stage(['javac','-source','7','-target','7','-d',classes,ROOT/'algorithms/java/SqaProbe.java'],ROOT,output/'compile-probe',120)
        rows = []
        for target in targets:
            for value,rational in ((0.5,'7/4'),(-0.5,'-3/4')):
                vector = [value]*3
                first = generate.observe(cp+':'+str(classes),target,vector,20,POLICY_V6)
                second = generate.observe(cp+':'+str(classes),target,vector,20,POLICY_V6)
                expected = 'value:fraction-field:runtime=type:'+target['class']+':zero=fraction:0/1:one=fraction:1/1|state=fraction:'+rational
                if first != second or first.get('status') != 'ok' or first.get('target_invoked') is not True or first.get('outcome') != expected:
                    raise ValueError('Composed oracle regression failed: '+json.dumps(first))
                rows.append({'case_id':len(rows),'retained':True,'target':target,'vector':vector,
                             'fixed_first':first,'fixed_second':second})
        write_json(output/'observations.json',rows)
        sources = output/'sources'
        sources.mkdir()
        (sources/'GeneratedStudyTest.java').write_bytes(generate.suite_source(rows,POLICY_V6).encode())
        package = pack_suite(sources,output/'package',4,30)
        instrument = output/'target-classes.txt'
        instrument.write_text('\n'.join(sorted(t['class'] for t in targets))+'\n',encoding='utf-8')
        result = evaluate_run(EvaluationConfig(project='Math',bug_id=1,generator='aom-fraction-field-v8-development',seed=101,budget=4,
            suite=output/'package/suite.tar.bz2',buggy_worktree=trees/'b',fixed_worktree=trees/'f',output=output/'evaluation',
            classes_file=instrument,d4j=d4j,test_count=4,timeout_seconds=300))
        counts = {name:read_json(output/'evaluation'/name/'sqa-stage-counts.json') for name in ('fixed-1','fixed-2','buggy','coverage')}
        if result['status'] != 'complete' or any(c['executed']!=4 or c['skipped']!=0 or c['target_checks']!=4 for c in counts.values()):
            raise ValueError('Development JUnit evaluation did not execute all four checks')
        xml = ET.parse(output/'evaluation/coverage/coverage.xml').getroot()
        hits = {c.get('name'):sum(int(line.get('hits','0')) for method in c.iter('method') if method.get('name')=='getField'
                              for line in method.iter('line')) for c in xml.iter('class') if c.get('name') in {t['class'] for t in targets}}
        if set(hits) != {t['class'] for t in targets} or any(value<=0 for value in hits.values()):
            raise ValueError('Coverage must reach both actual getField bodies')
        receipt = {'scope':'Composed v8 runtime regression; not CMA-ES/FSCS-ART or API primary results',
            'protocol_sha256':sha256(protocol_path),'preparation_index_sha256':sha256(preparation/'index.json'),
            'runner_sha256':sha256(__file__),'runtime_source_sha256':implementation_hashes(),
            'fresh_fixed_head':head,'fresh_fixed_tag_commit':fixed_tag,'composed_revision_proof':proof,
            'fixture_policy_id':POLICY_V6,'accepted_targets':targets,
            'suite_sha256':package['suite_sha256'],'evaluation_sha256':sha256(output/'evaluation/record.json'),
            'status':result['status'],'fixed_validation':result['fixed_validation'],'stage_counts':counts,
            'getField_coverage_hits':hits,'fault_detected':result['fault_detected'],
            'primary':False,'primary_results_added':0,'gate_a_passed':False,'live_requests':0,'queue_mutations':0}
        write_json(output/'receipt.json',receipt)
        write_json(output/'checksums.json',{p.relative_to(output).as_posix():sha256(p) for p in output.rglob('*')
            if p.is_file() and p.suffix!='.class' and '__pycache__' not in p.parts})
        return receipt


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--preparation',type=Path,required=True)
    parser.add_argument('--protocol',type=Path,required=True)
    parser.add_argument('--output',type=Path,required=True)
    parser.add_argument('--worktrees',type=Path,required=True)
    parser.add_argument('--d4j',required=True)
    args = parser.parse_args()
    receipt = verify(args.preparation,args.protocol,args.output,args.worktrees,args.d4j)
    print(json.dumps({k:receipt[k] for k in ('status','fixed_validation','getField_coverage_hits','fault_detected')}))
