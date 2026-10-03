"""Run the unchanged CPU JUnit packaging path on the thirteen bounded shared cases."""
import argparse
import gzip
import io
import json
import os
from pathlib import Path
import re
import tarfile
import tempfile

from scripts.study.generate import suite_source
from .common import ROOT,sha256
from .preparation import encoded
from .fixture_policy import POLICY_V11
from .chronology_v11 import load_contract
from .build_prepare_v11_development import INTEGRATION,checked_integration
from .verify_chronology_v11 import CASES,expected_outcomes,target_for,execute,JVM,SOURCE


def verify(d4j,output):
    contract=checked_integration();expected=expected_outcomes(contract)
    output=Path(output).resolve();d4j=Path(d4j).resolve();output.mkdir(parents=True,exist_ok=False)
    rows=[{'retained':True,'case_id':case,'target':target_for(contract,group),
           'vector':[(bucket-1)*0.75,0,0],'fixed_first':{'outcome':expected[case]}}
          for case,(group,bucket) in CASES.items()]
    (output/'GeneratedStudyTest.java').write_bytes(suite_source(rows,POLICY_V11).encode())
    negative='''import org.junit.Test;
public class FixtureFailureStudyTest {
    @Test public void setupFailureCannotBeFault() {
        GeneratedStudyTest.SqaProbe.observeWithPolicy("org.joda.time.Partial","org.joda.time.Chronology","<init>","",
            new double[]{Double.NaN,0,0},"'''+POLICY_V11+'''");
    }
}
'''
    (output/'FixtureFailureStudyTest.java').write_text(negative,encoding='utf-8')
    (output/'verifier.py').write_bytes(Path(__file__).read_bytes())
    jar=d4j/'framework/projects/Time/lib/joda-convert-1.2.jar'
    junit=d4j/'framework/projects/lib/junit-4.12-hamcrest-1.3.jar'
    seal={'suite_sha256':sha256(output/'GeneratedStudyTest.java'),'negative_suite_sha256':sha256(output/'FixtureFailureStudyTest.java'),'generator_sha256':sha256(ROOT/'scripts/study/generate.py'),
        'helper_sha256':sha256(ROOT/'algorithms/java/SqaProbe.java'),'dependency_sha256':{jar.name:sha256(jar),junit.name:sha256(junit)},
        'shared_integration_sha256':sha256(INTEGRATION/'receipt.json'),'presealed_independent_oracles':True,'primary':False}
    (output/'preexecution-seal.json').write_bytes(encoded(seal))
    stages={}
    with tempfile.TemporaryDirectory(prefix='aom-chronology-packaging-') as temp:
        for version in ('fixed','buggy'):
            work=Path(temp)/version;work.mkdir();classes=work/'classes';classes.mkdir()
            archive=gzip.decompress((INTEGRATION/(version+'-production-source.tar.gz')).read_bytes())
            with tarfile.open(fileobj=io.BytesIO(archive)) as tar:
                for member in tar.getmembers():
                    if not member.isfile() or not member.name.endswith('.java'): continue
                    path=(work/member.name).resolve()
                    if not path.is_relative_to(work): raise ValueError('Unsafe production archive')
                    path.parent.mkdir(parents=True,exist_ok=True);path.write_bytes(tar.extractfile(member).read())
            if version=='fixed':
                retained=ROOT/'output/api854-20261003/prepare-v11-chronology-development-v3/Time-1/fixed-source'/SOURCE
                if (work/SOURCE).read_bytes().replace(b'\r\n',b'\n')!=retained.read_bytes().replace(b'\r\n',b'\n'):
                    raise ValueError('Fixed retained source differs')
                (work/SOURCE).write_bytes(retained.read_bytes())
            cp=os.pathsep.join(map(str,[classes,jar,junit]))
            result,_=execute(['javac','--release','8','-g','-cp',cp,'-sourcepath',str(work/'src/main/java'),
                '-d',str(classes),str(work/SOURCE),str(output/'GeneratedStudyTest.java'),str(output/'FixtureFailureStudyTest.java')],output,version+'-compile')
            if result.returncode: raise ValueError(result.stderr.decode())
            for repeat in ('first','second'):
                stage=version+'_'+repeat
                # JUnit counter files are written in a dedicated temporary cwd,
                # never into received/sealed evidence or the workspace root.
                import subprocess
                result=subprocess.run(['java',*JVM,'-cp',cp,'org.junit.runner.JUnitCore','GeneratedStudyTest'],
                    cwd=work,capture_output=True,timeout=120)
                (output/(stage+'.stdout.log')).write_bytes(result.stdout)
                (output/(stage+'.stderr.log')).write_bytes(result.stderr)
                raw=(work/'sqa-stage-counts.json').read_bytes();(output/(stage+'.stage-counts.json')).write_bytes(raw)
                # Historical packager emits escaped JSON; retain its exact bytes.
                text=raw.decode().replace('\\"','"').replace('\\n','\n')
                counts=json.loads(text)
                if counts!={'schema_version':1,'executed':13,'skipped':0,'target_checks':13}: raise ValueError('Packaged target counters differ')
                out=result.stdout.decode()
                if version=='fixed':
                    if result.returncode!=0 or 'OK (13 tests)' not in out: raise ValueError('Packaged fixed suite failed: '+out)
                elif (result.returncode!=1 or 'Tests run: 13,  Failures: 1' not in out
                      or 'generatedarrays_bad_order(GeneratedStudyTest)' not in out):
                    raise ValueError('Packaged buggy negative control differs: '+out)
                stages[stage]={'exit_code':result.returncode,'counters':counts,'stdout_sha256':sha256(output/(stage+'.stdout.log')),
                              'stage_counts_raw_sha256':sha256(output/(stage+'.stage-counts.json'))}
            if version=='fixed':
                negative,_=execute(['java',*JVM,'-cp',cp,'org.junit.runner.JUnitCore','FixtureFailureStudyTest'],output,'controlled-fixture-failure')
                from scripts.study.evaluate import parse_test_evidence,EvidenceError
                if negative.returncode!=1 or b'SQA_HARNESS Chronology setup/projection failed' not in negative.stdout:
                    raise ValueError('Controlled packaged fixture failure did not retain harness marker')
                try:
                    parse_test_evidence('Failing tests: 1\n','--- FixtureFailureStudyTest::setupFailureCannotBeFault\n'+negative.stdout.decode())
                except EvidenceError:
                    stages['controlled_fixture_failure']={'evaluator_rejected_as_fault':True,'raw_stdout_sha256':sha256(output/'controlled-fixture-failure.stdout.log')}
                else: raise ValueError('Evaluator accepted packaged setup failure as fault')
    if sha256(output/'GeneratedStudyTest.java')!=seal['suite_sha256']: raise ValueError('Presealed suite changed')
    receipt={'status':'pass','scope':'CPU JUnit packaging integration only, independently specified development suite; not a CMA-ES/FSCS-ART generated algorithm result',
        'fixture_policy_id':POLICY_V11,'cases':13,'stages':stages,'preexecution_seal_sha256':sha256(output/'preexecution-seal.json'),
        'suite_sha256':seal['suite_sha256'],'verifier_sha256':sha256(__file__),'gate_a_passed':False,'live_requests':0,'queue_mutations':0,'primary_results_added':0}
    (output/'receipt.json').write_bytes(encoded(receipt))
    (output/'checksums.json').write_bytes(encoded({p.name:sha256(p) for p in output.iterdir() if p.is_file()}))
    return receipt


if __name__=='__main__':
    p=argparse.ArgumentParser(description=__doc__);p.add_argument('--defects4j',type=Path,required=True);p.add_argument('--output',type=Path,required=True)
    a=p.parse_args();result=verify(a.defects4j,a.output);print({'status':result['status'],'packaged_cases':13,'gate_a_passed':False})
