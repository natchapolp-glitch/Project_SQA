"""Reject real pre-target binding assertions and target LinkageError as fixture faults."""
import argparse
import base64
import os
from pathlib import Path
import re
import tarfile
import tempfile
from .common import ROOT,sha256
from .preparation import encoded
from .fixture_policy import POLICY_V12
from .graphics_v12 import RECEIVER, SIGNATURES
from .verify_graphics_v12 import OUTPUT,checked,JVM,SOURCE,RECEIVER_SOURCE
from .verify_chronology_v11 import execute


def verify(output):
    checked();output=Path(output).resolve();output.mkdir(parents=True,exist_ok=False)
    helper=OUTPUT/'SqaProbe.java';jars=sorted((OUTPUT/'dependencies').glob('*.jar'));controls={}
    driver=output/'EnvironmentFailureProbe.java'
    driver.write_text('''public class EnvironmentFailureProbe {
 public static void main(String[] args) {
  try {
   SqaProbe.observeWithPolicy("'''+RECEIVER+'''","","drawBackground","'''+SIGNATURES['drawBackground']+'''",new double[]{0,0,0},"'''+POLICY_V12+'''");
   throw new AssertionError("Control escaped fixture guard");
  } catch(Throwable failure) {
   failure.printStackTrace(System.out);
   StringBuilder chain=new StringBuilder();
   for(Throwable t=failure;t!=null;t=t.getCause()) chain.append(t.getClass().getName()).append(":").append(t.getMessage()).append("\\n");
   boolean binding=args[0].equals("binding_assertion");
   boolean valid=failure.getClass().getName().equals("SqaProbe$FixtureFailure")
    && failure.getMessage().contains("SQA_HARNESS Graphics")
    && SqaProbe.targetInvoked()==!binding
    && (binding ? chain.toString().contains("java.lang.AssertionError:Production receiver binding")
                : chain.toString().contains("java.lang.LinkageError:controlled environment failure"));
   System.out.println("CONTROL_VALID="+valid+";TARGET_INVOKED="+SqaProbe.targetInvoked());
   if(!valid) System.exit(1);
  }
 }
}
''',encoding='utf-8')
    with tempfile.TemporaryDirectory(prefix='aom-graphics-environment-') as temp:
        for name in ('binding_assertion','target_linkage'):
            work=Path(temp)/name;work.mkdir();classes=work/'classes';classes.mkdir()
            with tarfile.open(OUTPUT/'fixed-production-source.tar.gz') as tar:
                for member in tar.getmembers():
                    if not member.isfile():continue
                    path=(work/member.name).resolve()
                    if not path.is_relative_to(work):raise ValueError('Archive escape')
                    path.parent.mkdir(parents=True,exist_ok=True);path.write_bytes(tar.extractfile(member).read())
            if name=='binding_assertion':
                path=work/'source/org/jfree/chart/plot/CategoryPlot.java';source=path.read_text()
                pattern=r'(public CategoryItemRenderer getRenderer\(\)\s*\{)\s*return getRenderer\(0\);'
                source,count=re.subn(pattern,r'\1 return null;',source)
                if count!=1:raise ValueError('Exact plot getter mutation differs')
            else:
                path=work/SOURCE;source=path.read_text()
                pattern=r'(public void drawBackground\(Graphics2D g2,\s*CategoryPlot plot,\s*Rectangle2D dataArea\)\s*\{)'
                source,count=re.subn(pattern,r'\1 if (g2 != null) throw new LinkageError("controlled environment failure");',source)
                if count!=1:raise ValueError('Exact target mutation differs')
            path.write_text(source);(output/(name+'.java')).write_bytes(path.read_bytes())
            cp=os.pathsep.join(map(str,[classes,*jars]))
            (output/(name+'-preexecution-seal.json')).write_bytes(encoded({'helper_sha256':sha256(helper),
                'original_production_archive_sha256':sha256(OUTPUT/'fixed-production-source.tar.gz'),
                'mutant_source_sha256':sha256(path),'mutation_is_not_benchmark_source':True,
                'driver_sha256':sha256(driver),
                'dependency_sha256':{p.name:sha256(p) for p in jars}}))
            result,_=execute(['javac','--release','8','-g','-encoding','UTF-8','-cp',cp,'-sourcepath',work/'source','-d',classes,
                work/SOURCE,work/RECEIVER_SOURCE,work/'source/org/jfree/chart/annotations/CategoryLineAnnotation.java',
                work/'source/org/jfree/chart/axis/NumberAxis.java',work/'source/org/jfree/data/category/DefaultCategoryDataset.java',helper,driver],output,name+'-compile')
            if result.returncode:raise ValueError(result.stderr.decode())
            for path in (work/'source').rglob('*.properties'):
                destination=classes/path.relative_to(work/'source');destination.parent.mkdir(parents=True,exist_ok=True);destination.write_bytes(path.read_bytes())
            result,record=execute(['java',*JVM,'-cp',cp,'SqaProbe','observe',RECEIVER,'','drawBackground',SIGNATURES['drawBackground'],'0,0,0',POLICY_V12],output,name)
            lines=[line.split(b':',1)[1] for line in result.stdout.splitlines() if line.startswith(b'SQA_FIXTURE_FAILURE:')]
            if result.returncode or len(lines)!=1 or b'SQA_RESULT:' in result.stdout:
                raise ValueError('Environment failure became target observation')
            reason=base64.b64decode(lines[0],validate=True).decode()
            if 'SQA_HARNESS Graphics setup/projection failed' not in reason:
                raise ValueError('Fixture marker missing')
            direct,detail=execute(['java',*JVM,'-cp',cp,'EnvironmentFailureProbe',name],output,name+'-cause-chain')
            if direct.returncode or b'CONTROL_VALID=true' not in direct.stdout:
                raise ValueError('Actual binding/fatal cause chain or invocation boundary differs')
            record['cause_chain_proof']=detail
            record.update(status='fixture_error',reason=reason,ordinary_target_observation=False)
            controls[name]=record
    receipt={'status':'pass','fixture_policy_id':POLICY_V12,'shared_integration_sha256':sha256(OUTPUT/'receipt.json'),
        'controls':controls,'scope':'Temporary production mutation controls; no benchmark bytes or primary fault outcomes changed',
        'gate_a_passed':False,'primary_results_added':0,'live_requests':0,'queue_mutations':0}
    (output/'receipt.json').write_bytes(encoded(receipt));(output/'checksums.json').write_bytes(encoded({p.name:sha256(p) for p in output.iterdir() if p.is_file()}))
    return receipt


if __name__=='__main__':
    p=argparse.ArgumentParser();p.add_argument('--output',type=Path,required=True);a=p.parse_args()
    print({'status':verify(a.output)['status'],'controls':2})
