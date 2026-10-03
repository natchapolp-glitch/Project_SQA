"""Real Defects4J stages for a sealed bounded manual Csv constructor suite."""
import json,sys
from pathlib import Path
BASE=Path(__file__).resolve().parent;ROOT=BASE.parents[2]
sys.path[:0]=[str(BASE),str(ROOT),str(ROOT/'scripts/study')]
import verify_native as native
from scripts.study.api854.common import cpu_slot,implementation_hashes
from scripts.study.api854.worker import prepare_evaluation
from scripts.study.api854.pack_suite import pack_suite
from scripts.study.evaluate import EvaluationConfig,evaluate_run
OUT=BASE/'d4j-v1'
def main():
    OUT.mkdir(exist_ok=False)
    source=OUT/'suite-source/org/apache/commons/csv';source.mkdir(parents=True)
    helper=(BASE/'CsvConstructorProbe.java').read_text()
    nested=helper[helper.index('public final class CsvConstructorProbe {'):].replace('public final class CsvConstructorProbe {','public static final class CsvConstructorProbe {',1)
    tests=[]
    for case in native.POLICY['cases']:
        expected=json.dumps(case['expected'],separators=(',',':'))
        tests.append('@org.junit.Test public void constructor_'+case['case']+'() throws Exception { check('+json.dumps(case['case'])+','+json.dumps(expected)+'); }')
    wrapper='package org.apache.commons.csv;\nimport java.io.*;\nimport java.lang.reflect.*;\npublic class CsvConstructorTest {\n'+'''
    static int executed,checks;
    static void check(String name,String expected) throws Exception {
        executed++; String actual;
        try { actual=CsvConstructorProbe.observation(name); }
        catch (Exception failure) { throw new AssertionError("SQA_HARNESS unexpected setup/projection failure: "+name,failure); }
        checks++; org.junit.Assert.assertEquals("Independent constructor/binding observations differ: "+name,expected,actual);
    }
    @org.junit.AfterClass public static void counts() throws Exception {
        String result="{\\"schema_version\\":1,\\"executed\\":"+executed+",\\"skipped\\":0,\\"target_checks\\":"+checks+"}";
        java.nio.file.Files.write(java.nio.file.Paths.get("sqa-stage-counts.json"),result.getBytes(java.nio.charset.StandardCharsets.UTF_8));
    }
'''+nested+'\n'+'\n'.join(tests)+'\n}\n'
    (source/'CsvConstructorTest.java').write_text(wrapper,encoding='utf-8',newline='\n')
    manifest=pack_suite(OUT/'suite-source',OUT/'packaged',test_count=5,test_method_cap=30)
    runtime=implementation_hashes()
    native.write(OUT/'preexecution-seal.json',{'policy_sha256':native.sha(BASE/'policy.json'),'suite_sha256':manifest['suite_sha256'],'source_sha256':manifest['source_sha256'],'native_preexecution_seal_sha256':native.sha(BASE/'native-v2/preexecution-seal.json'),'producer_sha256':native.sha(__file__),'runtime_source_sha256':runtime,'worker_id':'beam-pc1','cpu_slots':1,'test_method_count':5,'primary':False,'selection_used_buggy_outcomes':False,'scope':'Bounded manual constructor fixture and follow-up argument-binding component evaluation; no primary algorithm/model result.','kku_requests':0,'queue_mutations':0})
    worktrees=Path('/home/beam/sqa-beam/worktrees');trees=worktrees/'beam-csv-constructor-evaluator-v1';trees.mkdir(exist_ok=False)
    d4j='/home/beam/sqa-beam/defects4j/framework/bin/defects4j'
    with cpu_slot(worktrees):
        paths,classes,sources=prepare_evaluation(d4j,{'project':'Csv','bug_id':1},trees,OUT/'setup',900)
        for name,digest in sources.items():native.require(native.sha(BASE/'received-aom/fixed-source'/name)==digest,'Actual D4J fixed source differs')
        record=evaluate_run(EvaluationConfig(project='Csv',bug_id=1,generator='BeamConstructorDevelopment',seed=20261004,budget=30,suite=OUT/'packaged/suite.tar.bz2',buggy_worktree=paths['b'],fixed_worktree=paths['f'],output=OUT/'measurement',d4j=d4j,classes_file=classes,test_count=5,timeout_seconds=900))
        native.require(runtime==implementation_hashes(),'Shared runtime changed')
        native.write(OUT/'receipt.json',{'status':record['status'],'suite_sha256':manifest['suite_sha256'],'record_sha256':native.sha(OUT/'measurement/record.json'),'fixed_source_sha256':sources,'manual_component_fault_detected':record.get('fault_detected'),'fault_attribution':'CRLF follow-up read/line count integration assertion; not a constructor defect claim','semantic_validity':'pending_joint_review','full_legal_domain_approved':False,'shared_integration_approved':False,'primary_added':0,'generation_algorithm_result':False,'gate_a_approved':False,'kku_requests':0,'queue_mutations':0})
        native.require(record['status']=='complete','Evaluator did not complete')
    print(json.dumps({'status':record['status'],'manual_component_fault_detected':record['fault_detected'],'coverage':[record['line_covered'],record['line_total'],record['branch_covered'],record['branch_total']]}))
if __name__=='__main__':
    try:main()
    except BaseException as error:
        if OUT.exists():native.write(OUT/'failure.json',{'status':'failed_attempt_retained','reason':type(error).__name__+': '+str(error)})
        raise
    finally:
        if OUT.exists():native.write(OUT/'checksums.json',{p.relative_to(OUT).as_posix():native.sha(p) for p in sorted(OUT.rglob('*')) if p.is_file()})
