"""Actual pre-target default-state and fatal target controls remain fixture failures."""
import argparse
import base64
import gzip
import io
import re
import tarfile
import tempfile
from pathlib import Path
from .common import ROOT, contained, sha256, implementation_hashes
from .preparation import encoded
from .codec_v13 import INTAKE, load_intake
from .fixture_policy import POLICY_V13
from .verify_chronology_v11 import execute
from .verify_codec_v13 import JVM, PREFIX


def verify(output):
    load_intake();output=Path(output).resolve();output.mkdir(parents=True,exist_ok=False)
    helper=output/'SqaProbe.java';helper.write_bytes((ROOT/'algorithms/java/SqaProbe.java').read_bytes())
    driver=output/'CodecEnvironmentControl.java'
    driver.write_text('''public class CodecEnvironmentControl {
 public static void main(String[] args) {
  try {
   SqaProbe.observeWithPolicy("org.apache.commons.codec.language.Metaphone","","isNextChar",
       "java.lang.StringBuffer,int,char",new double[]{-0.875,0,0},"'''+POLICY_V13+'''");
   throw new AssertionError("Fixture guard escaped");
  } catch(Throwable failure) {
   failure.printStackTrace(System.out);
   StringBuilder chain=new StringBuilder();
   for(Throwable t=failure;t!=null;t=t.getCause())chain.append(t.getClass().getName()).append(":").append(t.getMessage()).append("\\n");
   boolean setup=args[0].equals("default_state");
   boolean valid=failure.getClass().getName().equals("SqaProbe$FixtureFailure")
    && failure.getMessage().contains("SQA_HARNESS Codec") && SqaProbe.targetInvoked()==!setup
    && SqaProbe.CodecRecipe.lastEvidence==null
    && chain.toString().contains(setup?"java.lang.AssertionError:Exact receiver initial state":"java.lang.LinkageError:controlled Codec environment failure");
   System.out.println("CONTROL_VALID="+valid+";TARGET_INVOKED="+SqaProbe.targetInvoked());
   if(!valid)System.exit(1);
  }
 }
}
''',encoding='utf-8')
    controls={}
    with tempfile.TemporaryDirectory(prefix='aom-codec-environment-') as temp:
        for name in ('default_state','target_linkage'):
            folder=Path(temp)/name;folder.mkdir();classes=folder/'classes';classes.mkdir()
            with tarfile.open(fileobj=io.BytesIO(gzip.decompress((INTAKE/'fixed-production-source.tar.gz').read_bytes()))) as tar:
                for m in tar.getmembers():
                    if not m.isfile():continue
                    p=contained(folder,m.name);p.parent.mkdir(parents=True,exist_ok=True);p.write_bytes(tar.extractfile(m).read())
            src=folder/(PREFIX+'Metaphone.java');s=src.read_text()
            if name=='default_state':
                s,count=re.subn(r'private int maxCodeLen = 4\s*;', 'private int maxCodeLen = 5;',s)
            else:
                s,count=re.subn(r'(private boolean isNextChar\(StringBuffer string, int index, char c\)\s*\{)',r'\1 if(string!=null)throw new LinkageError("controlled Codec environment failure");',s)
            if count!=1:raise ValueError('Exact control mutation anchor differs')
            src.write_text(s);(output/(name+'-mutant.java')).write_bytes(src.read_bytes())
            (output/(name+'-preseal.json')).write_bytes(encoded({'runtime_source_sha256':implementation_hashes(),
                'helper_sha256':sha256(helper),'driver_sha256':sha256(driver),'source_archive_sha256':sha256(INTAKE/'fixed-production-source.tar.gz'),
                'controlled_mutation_sha256':sha256(src),'primary':False}))
            run,_=execute(['javac','--release','8','-encoding','UTF-8','-d',classes,*sorted((folder/'src/java').rglob('*.java')),helper,driver],output,name+'-compile')
            if run.returncode:raise ValueError(run.stderr.decode())
            run,r=execute(['java',*JVM,'-cp',classes,'SqaProbe','observe','org.apache.commons.codec.language.Metaphone','','isNextChar','java.lang.StringBuffer,int,char','-0.875,0,0',POLICY_V13],output,name)
            markers=[line.split(b':',1)[1] for line in run.stdout.splitlines() if line.startswith(b'SQA_FIXTURE_FAILURE:')]
            if run.returncode or len(markers)!=1 or b'SQA_RESULT:' in run.stdout:raise ValueError('Fixture failure became target result')
            reason=base64.b64decode(markers[0],validate=True).decode()
            if 'SQA_HARNESS Codec' not in reason:raise ValueError('Missing harness marker')
            direct,detail=execute(['java',*JVM,'-cp',classes,'CodecEnvironmentControl',name],output,name+'-cause')
            if direct.returncode or b'CONTROL_VALID=true' not in direct.stdout:raise ValueError('Actual default/fatal cause boundary differs')
            from scripts.study.evaluate import parse_test_evidence,EvidenceError
            try:parse_test_evidence('Failing tests: 1\n','--- GeneratedStudyTest::codecFixture\n'+direct.stdout.decode())
            except EvidenceError:pass
            else:raise ValueError('Evaluator counted fixture failure as fault')
            r.update(status='fixture_error',ordinary_target_observation=False,reason=reason,cause_exit_code=direct.returncode,
                     cause_proof=detail,evaluator_rejected_as_fault=True)
            controls[name]=r
    result={'status':'pass','runtime_source_sha256':implementation_hashes(),'controls':controls,
        'scope':'Temporary original-production controls; no benchmark fault result',
        'gate_a_passed':False,'primary_results_added':0,'live_requests':0,'queue_mutations':0}
    (output/'receipt.json').write_bytes(encoded(result));(output/'checksums.json').write_bytes(encoded({p.name:sha256(p) for p in output.iterdir() if p.is_file()}))
    return result


if __name__=='__main__':
    p=argparse.ArgumentParser(description=__doc__);p.add_argument('--output',type=Path,required=True);a=p.parse_args()
    print(verify(a.output)['status'])
