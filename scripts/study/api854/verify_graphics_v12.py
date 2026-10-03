"""Fresh shared Graphics lifecycle/oracle, inherited entry, old Chart and JUnit proof."""
import argparse
import base64
import gzip
import io
import json
import os
from pathlib import Path
import subprocess
import tarfile
import tempfile
from datetime import datetime, timezone
from .common import ROOT, sha256, implementation_hashes, contained
from .preparation import encoded, digest
from .graphics_v12 import contract, load_intake, INTAKE, BASE, V11, CHAMP, CANDIDATE, SIGNATURES, RECEIVER, OWNER
from .joint_recipe_v10 import git_bytes
from .fixture_policy import POLICY_V11, POLICY_V12
from .verify_chronology_v11 import execute, parse

OUTPUT = ROOT/'output/api854-20261003/aom-graphics-v12-integration-v5'
JVM = ['-Djava.awt.headless=true','-Duser.timezone=UTC','-Duser.language=en','-Duser.country=US']
SOURCE = 'source/org/jfree/chart/renderer/category/AbstractCategoryItemRenderer.java'
RECEIVER_SOURCE = 'source/org/jfree/chart/renderer/category/AreaRenderer.java'


def oracle_module():
    load_intake()
    source = (INTAKE/'candidate-verifier.py').read_text()
    policy = json.loads(git_bytes(CHAMP,CANDIDATE+'/policy.json'))
    # Preserve accepted analytic checker functions verbatim; exclude checkpoint/host execution code.
    code = 'import hashlib, struct\n' + source[source.index('def expected_state'):source.index('def linux_path')]
    def require(ok, reason):
        if not ok: raise ValueError(reason)
    space = {'CASES':policy['cases'],'TARGETS':policy['exact_targets'],'OWNER':OWNER,'RECEIVER':RECEIVER,'require':require}
    exec(code,space)
    return policy, code, space


def cases_vectors(policy):
    result = {}
    for case,method in policy['cases'].items():
        cases = [n for n,m in policy['cases'].items() if m==method]
        result[case] = -1+(cases.index(case)+0.5)*2/len(cases)
    return result


def driver(policy):
    vectors = cases_vectors(policy)
    calls = []
    for case,method in policy['cases'].items():
        calls.append('        check('+', '.join(json.dumps(s) for s in [case,method,SIGNATURES[method]])+', '+repr(vectors[case])+');')
    return '''import java.util.Map;
public final class SqaGraphicsSuite {
    private static int passed=0, executed=0;
    private static void check(String name, String method, String params, double vector) {
        try {
            String actual=SqaProbe.observeWithPolicy("'''+RECEIVER+'''","",method,params,new double[]{vector,0,0},"'''+POLICY_V12+'''");
            Map<String,Object> row=SqaProbe.GraphicsRecipe.lastEvidence;
            if (row==null || !name.equals(row.get("case")) || !SqaProbe.targetInvoked()
                || !actual.equals(SqaProbe.GraphicsRecipe.json(row.get("observation"))))
                throw new IllegalStateException("Shared dispatch/evidence differs");
            executed++; if (Boolean.TRUE.equals(row.get("target_check_passed"))) passed++;
            System.out.println(SqaProbe.GraphicsRecipe.json(row));
        } catch(Throwable failure) {
            failure.printStackTrace(); System.exit(2);
        }
    }
    public static void main(String[] args) {
'''+ '\n'.join(calls)+'''
        System.out.println("{\\"summary\\":true,\\"executed\\":"+executed+",\\"target_checks\\":"+executed
            +",\\"passed\\":"+passed+",\\"failed\\":"+(executed-passed)+",\\"skipped\\":0,\\"fixture_errors\\":0}");
        if(passed!=24) System.exit(1);
    }
}
'''


def validate_stage(raw, output, stage, validators, allow_failure=False):
    records = parse(raw); images = output/(stage+'-pixels'); images.mkdir()
    for row in records:
        if 'actual_argb_b64' not in row: continue
        for kind in ('actual','reference'):
            (images/(row['case']+'.'+kind+'.argb')).write_bytes(base64.b64decode(row[kind+'_argb_b64'],validate=True))
    return validators['validate_records'](records,images,allow_assertion_failures=allow_failure)


def checked():
    c = contract()
    hashes=json.loads((OUTPUT/'checksums.json').read_bytes())
    actual={p.relative_to(OUTPUT).as_posix() for p in OUTPUT.rglob('*') if p.is_file() and p!=OUTPUT/'checksums.json'}
    if set(hashes)!=actual: raise ValueError('Complete Graphics evidence inventory required')
    for path,h in hashes.items():
        if sha256(contained(OUTPUT,path))!=h: raise ValueError('Graphics evidence changed')
    r = json.loads((OUTPUT/'receipt.json').read_bytes())
    if (r['status']!='pass' or r['runtime_source_sha256']!=implementation_hashes()
        or r['intake_sha256']!=sha256(INTAKE/'receipt.json') or not r['local_shared_integration_verified']
        or r['cases']!=24 or r['exact_declarations']!=7):
        raise ValueError('Require current fresh shared integration before selection')
    seal=json.loads((OUTPUT/'preexecution-seal.json').read_bytes())
    if seal['runtime_source_sha256']!=implementation_hashes() or seal['suite_sha256']!=r['suite_sha256']:
        raise ValueError('Pre-execution runtime/suite binding differs')
    for n,h in seal['suite_sha256'].items():
        if sha256(contained(OUTPUT,n))!=h:raise ValueError('Presealed suite changed')
    if any(r[k] for k in ('shared_team_approval','gate_a_passed','primary_results_added','live_requests','queue_mutations','candidate_fault_detected')):
        raise ValueError('Development proof cannot transfer primary/team/live approval')
    _,_,validator=oracle_module()
    for stage in ('fixed_first','fixed_second','buggy_first','buggy_second'):
        rows=validator['validate_records'](parse((OUTPUT/(stage+'.stdout.log')).read_bytes()),OUTPUT/(stage+'-pixels'))
        if rows!=r['stages'][stage]['cases'] or r['stages'][stage]['exit_code']!=0:
            raise ValueError('Raw shared evidence differs')
    for version in ('fixed','buggy'):
        rows=parse((OUTPUT/(version+'-entry-trace.stdout.log')).read_bytes())
        if validator['validate_trace'](rows,0)!=r['stages'][version+'_entry_trace']['exact_target_entries']:
            raise ValueError('Raw inherited entry differs')
        if len([row for row in rows if row.get('method_entry')])!=24:
            raise ValueError('Exactly one inherited invocation per case required')
        for repeat in ('first','second'):
            name=version+'-junit-'+repeat
            raw=(OUTPUT/(name+'.counts.json')).read_bytes()
            counts=json.loads(raw.decode().replace('\\"','"').replace('\\n','\n'))
            proof=r['junit_stages'][name]
            if (counts!={'schema_version':1,'executed':24,'skipped':0,'target_checks':24}
                or proof['counts']!=counts or proof['counter_sha256']!=digest(raw) or proof['exit_code']!=0
                or proof['stdout_sha256']!=sha256(OUTPUT/(name+'.stdout.log'))
                or b'OK (24 tests)' not in (OUTPUT/(name+'.stdout.log')).read_bytes()):
                raise ValueError('Raw packaged JUnit execution/counters differ')
    mutation_rows=validator['validate_records'](parse((OUTPUT/'shifted-domain-line.stdout.log').read_bytes()),OUTPUT/'shifted-domain-line-pixels',allow_assertion_failures=True)
    if ([row['case'] for row in mutation_rows if not row['target_check_passed']]!=['domain_line_v']
        or r['mutation']['failed_cases']!=['domain_line_v'] or r['mutation']['exit_code']!=1):
        raise ValueError('Shared oracle sensitivity control differs')
    from scripts.study.evaluate import parse_test_evidence,EvidenceError
    negative=(OUTPUT/'controlled-fixture-failure.stdout.log').read_bytes()
    if r['fixture_negative_control']['exit_code']!=1 or b'SQA_HARNESS Graphics setup/projection failed' not in negative:
        raise ValueError('Packaged setup failure proof missing')
    try: parse_test_evidence('Failing tests: 1\n','--- FixtureFailureStudyTest::setupFailureCannotBeFault\n'+negative.decode())
    except EvidenceError: pass
    else: raise ValueError('Evaluator counted fixture failure as fault')
    chart=r['preserved_chart_eight_regression']
    old_targets=json.loads(git_bytes(BASE,V11+'/Chart-1/targets.json'))['targets']
    if len(chart)!=48 or r['old_chart_observation_pairs']!=48:raise ValueError('All eight Chart regressions required')
    for version in ('fixed','buggy'):
        for i,t in enumerate(old_targets):
            for v in (-0.75,0,0.75):
                row=next(x for x in chart if x['version']==version and x['target']==t and x['vector']==[v,0,0])
                a=(OUTPUT/f'chart-{version}-{i}-{v}-old.stdout.log').read_bytes()
                b=(OUTPUT/f'chart-{version}-{i}-{v}-new.stdout.log').read_bytes()
                if a!=b or digest(a)!=row['observation_sha256'] or not row['equal'] or b'"target_invoked":true' not in a:
                    raise ValueError('Raw old Chart runtime changed')
    return c


def verify(output=OUTPUT, defects4j=Path('/home/aomsin/sqa-round2/defects4j')):
    c=contract();policy,code,validators=oracle_module()
    output=Path(output).resolve();output.mkdir(parents=True,exist_ok=False)
    runtime=implementation_hashes(); candidate=json.loads((INTAKE/'candidate-receipt.json').read_bytes())
    hashes=json.loads((INTAKE/'candidate-checksums.json').read_bytes())
    for name in ['fixed-production-source.tar.gz','buggy-production-source.tar.gz',
                 *['dependencies/'+n for n in candidate['dependencies_sha256']['fixed']]]:
        raw=git_bytes(CHAMP,CANDIDATE+'/'+name)
        if digest(raw)!=hashes[name]: raise ValueError('Original candidate source/dependency hash differs')
        path=output/name;path.parent.mkdir(exist_ok=True);path.write_bytes(raw)
    junit=Path(defects4j)/'framework/projects/lib/junit-4.12-hamcrest-1.3.jar'
    (output/'dependencies'/junit.name).write_bytes(junit.read_bytes())
    (output/'SqaProbe.java').write_bytes((ROOT/'algorithms/java/SqaProbe.java').read_bytes())
    (output/'old-SqaProbe.java').write_bytes(git_bytes(BASE,'algorithms/java/SqaProbe.java'))
    (output/'SqaGraphicsSuite.java').write_text(driver(policy),encoding='utf-8')
    tracer=(INTAKE/'candidate-trace.java').read_text()
    tracer=tracer.replace('sqa.development.Graphics2DProbe','SqaGraphicsSuite',1)
    tracer=tracer.replace('sqa.development.Graphics2DProbe','SqaProbe$GraphicsRecipe')
    (output/'GraphicsEntryTrace.java').write_text(tracer,encoding='utf-8')
    (output/'oracle-validator.py').write_text(code,encoding='utf-8')
    (output/'verifier.py').write_bytes(Path(__file__).read_bytes())
    (output/'policy.json').write_bytes(encoded({**policy,'fixture_policy_id':POLICY_V12,'vectors':cases_vectors(policy),
        'intake_sha256':sha256(INTAKE/'receipt.json'),'old_chart_policy':POLICY_V11,'preserve_existing_chart_eight':True}))
    suite={p.relative_to(output).as_posix():sha256(p) for p in output.rglob('*') if p.is_file()}
    (output/'preexecution-seal.json').write_bytes(encoded({'sealed_at_utc':datetime.now(timezone.utc).isoformat(),
        'suite_sha256':suite,'runtime_source_sha256':runtime,'source_archives':candidate['source_archives'],
        'dependencies':candidate['dependencies_sha256'],'primary':False,'gate_a_passed':False}))
    stages={}; charts=[]; class_hashes={}; junit_stages={}
    with tempfile.TemporaryDirectory(prefix='aom-shared-graphics-') as temp:
        base=Path(temp);cps={}
        for version in ('fixed','buggy'):
            work=base/version;work.mkdir();classes=work/'classes';classes.mkdir()
            with tarfile.open(output/(version+'-production-source.tar.gz')) as tar:
                for member in tar.getmembers():
                    if not member.isfile():continue
                    path=(work/member.name).resolve()
                    if not path.is_relative_to(work):raise ValueError('Unsafe production archive')
                    path.parent.mkdir(parents=True,exist_ok=True);path.write_bytes(tar.extractfile(member).read())
            if version=='fixed':
                for source in (SOURCE,RECEIVER_SOURCE):
                    retained=git_bytes(BASE,V11+'/Chart-1/fixed-source/'+source)
                    if (work/source).read_bytes().replace(b'\r\n',b'\n')!=retained.replace(b'\r\n',b'\n'):
                        raise ValueError('Fixed Chart source differs from immutable v11')
                    (work/source).write_bytes(retained)
            jars=sorted((output/'dependencies').glob('*.jar'));cp=os.pathsep.join(map(str,[classes,*jars]));cps[version]=cp
            result,_=execute(['javac','--release','8','-g','-encoding','UTF-8','-cp',cp,'-sourcepath',work/'source',
                '-d',classes,work/SOURCE,work/RECEIVER_SOURCE,
                work/'source/org/jfree/chart/annotations/CategoryLineAnnotation.java',
                work/'source/org/jfree/chart/axis/NumberAxis.java',
                work/'source/org/jfree/data/category/DefaultCategoryDataset.java',
                output/'SqaProbe.java',output/'SqaGraphicsSuite.java'],output,version+'-compile')
            if result.returncode:raise ValueError(result.stderr.decode())
            for path in (work/'source').rglob('*.properties'):
                destination=classes/path.relative_to(work/'source');destination.parent.mkdir(parents=True,exist_ok=True);destination.write_bytes(path.read_bytes())
            class_hashes[version]={n:sha256(classes/(n.replace('.','/')+'.class')) for n in (OWNER,RECEIVER)}
            for repeat in ('first','second'):
                stage=version+'_'+repeat;result,record=execute(['java',*JVM,'-cp',cp,'SqaGraphicsSuite'],output,stage)
                record['cases']=validate_stage(result.stdout,output,stage,validators)
                if result.returncode:raise ValueError('Fixed/buggy shared graphics assertions failed')
                stages[stage]=record
            if stages[version+'_first']['cases']!=stages[version+'_second']['cases']:raise ValueError('Repeated graphics differ')
        result,_=execute(['javac','--add-modules','jdk.jdi','-d',base/'fixed/classes',output/'GraphicsEntryTrace.java'],output,'compile-jdi')
        if result.returncode:raise ValueError(result.stderr.decode())
        for version,cp in cps.items():
            stage=version+'-entry-trace';result,record=execute(['java','--add-modules','jdk.jdi','-cp',cps['fixed'],'GraphicsEntryTrace',cp,str(output)],output,stage)
            record['cases']=validate_stage(result.stdout,output,stage,validators)
            record['exact_target_entries']=validators['validate_trace'](parse(result.stdout),result.returncode)
            if result.returncode or record['cases']!=stages[version+'_first']['cases']:raise ValueError('Tracing changed graphics outcomes')
            if class_hashes[version]!={n:sha256(base/version/'classes'/(n.replace('.','/')+'.class')) for n in (OWNER,RECEIVER)}:
                raise ValueError('Tracing changed production bytecode')
            stages[version+'_entry_trace']=record
        # Original eight Chart lifecycle must remain byte-for-byte observable under current v12.
        old_targets=json.loads(git_bytes(BASE,V11+'/Chart-1/targets.json'))['targets']
        if len(old_targets)!=8:raise ValueError('Expected preserved eight Chart targets')
        old=base/'old-helper';old.mkdir();(old/'SqaProbe.java').write_bytes((output/'old-SqaProbe.java').read_bytes())
        result,_=execute(['javac','--release','8','-d',old,old/'SqaProbe.java'],output,'compile-old-helper')
        if result.returncode:raise ValueError(result.stderr.decode())
        for version,cp in cps.items():
            for i,target in enumerate(old_targets):
                for coordinate in (-0.75,0,0.75):
                    args=[target[k] for k in ('class','constructor_types','method','parameter_types')]
                    values=[]
                    for label,classpath,fixture in [('old',str(old)+os.pathsep+cp,POLICY_V11),('new',cp,POLICY_V12)]:
                        result,record=execute(['java',*JVM,'-cp',classpath,'SqaProbe','observe',*args,str(coordinate)+',0,0',fixture],output,f'chart-{version}-{i}-{coordinate}-{label}')
                        if result.returncode or b'SQA_FIXTURE_FAILURE:' in result.stdout or b'"target_invoked":true' not in result.stdout:
                            raise ValueError('Preserved Chart target fixture/invocation failure')
                        values.append(result.stdout)
                    if values[0]!=values[1]:raise ValueError('Old Chart lifecycle changed')
                    charts.append({'version':version,'target':target,'vector':[coordinate,0,0],'old_policy':POLICY_V11,'new_policy':POLICY_V12,'observation_sha256':digest(values[0]),'equal':True})
        # Real nested CPU JUnit packaging, independent expected maps from the analytic reference.
        from scripts.study.generate import suite_source
        rows=[]
        for row in stages['fixed_first']['cases']:
            method=row['method'];rows.append({'retained':True,'case_id':row['case'],'target':{'class':RECEIVER,'constructor_types':'','method':method,'parameter_types':SIGNATURES[method]},
                'vector':[cases_vectors(policy)[row['case']],0,0],'fixed_first':{'outcome':json.dumps(row['expected_observation'],separators=(',',':'))}})
        (output/'GeneratedStudyTest.java').write_bytes(suite_source(rows,POLICY_V12).encode())
        (output/'FixtureFailureStudyTest.java').write_text('''import org.junit.Test;
public class FixtureFailureStudyTest {
 @Test public void setupFailureCannotBeFault() {
  GeneratedStudyTest.SqaProbe.observeWithPolicy("'''+RECEIVER+'''","","drawBackground","'''+SIGNATURES['drawBackground']+'''",
   new double[]{Double.NaN,0,0},"'''+POLICY_V12+'''");
 }
}
''',encoding='utf-8')
        (output/'junit-preexecution-seal.json').write_bytes(encoded({'suite_sha256':sha256(output/'GeneratedStudyTest.java'),'oracle_basis':'Independent pre-target reference maps; never buggy outcomes','generator_sha256':sha256(ROOT/'scripts/study/generate.py')}))
        for version,cp in cps.items():
            result,_=execute(['javac','--release','8','-cp',cp,'-d',base/version/'classes',output/'GeneratedStudyTest.java',output/'FixtureFailureStudyTest.java'],output,version+'-junit-compile')
            if result.returncode:raise ValueError(result.stderr.decode())
            for repeat in ('first','second'):
                stage=version+'-junit-'+repeat
                result=subprocess.run(['java',*JVM,'-cp',cp,'org.junit.runner.JUnitCore','GeneratedStudyTest'],cwd=base/version,capture_output=True,timeout=120)
                (output/(stage+'.stdout.log')).write_bytes(result.stdout);(output/(stage+'.stderr.log')).write_bytes(result.stderr)
                raw=(base/version/'sqa-stage-counts.json').read_bytes();(output/(stage+'.counts.json')).write_bytes(raw)
                counts=json.loads(raw.decode().replace('\\"','"').replace('\\n','\n'))
                if result.returncode or b'OK (24 tests)' not in result.stdout or counts!={'schema_version':1,'executed':24,'skipped':0,'target_checks':24}:
                    raise ValueError('Shared JUnit Graphics packaging failed: '+result.stdout.decode())
                junit_stages[stage]={'exit_code':result.returncode,'counts':counts,'stdout_sha256':digest(result.stdout),'counter_sha256':digest(raw)}
        result,negative=execute(['java',*JVM,'-cp',cps['fixed'],'org.junit.runner.JUnitCore','FixtureFailureStudyTest'],output,'controlled-fixture-failure')
        from scripts.study.evaluate import parse_test_evidence, EvidenceError
        if result.returncode!=1 or b'SQA_HARNESS Graphics setup/projection failed' not in result.stdout:
            raise ValueError('Packaged fixture failure marker missing')
        try:
            parse_test_evidence('Failing tests: 1\n','--- FixtureFailureStudyTest::setupFailureCannotBeFault\n'+result.stdout.decode())
        except EvidenceError:
            negative['evaluator_rejected_as_fault']=True
        else: raise ValueError('Evaluator falsely counted Graphics setup failure as fault')
        # Positive sensitivity control on a temporary copy, not benchmark bytes retained in preparation.
        path=base/'fixed'/SOURCE;text=path.read_text();needle='line = new Line2D.Double(value, dataArea.getMinY(), value,'
        if text.count(needle)!=1:raise ValueError('Mutation source differs')
        path.write_text(text.replace(needle,'line = new Line2D.Double(value + 4, dataArea.getMinY(), value + 4,'))
        result,_=execute(['javac','--release','8','-g','-cp',cps['fixed'],'-d',base/'fixed/classes',path],output,'compile-sensitivity')
        if result.returncode:raise ValueError(result.stderr.decode())
        result,mutation=execute(['java',*JVM,'-cp',cps['fixed'],'SqaGraphicsSuite'],output,'shifted-domain-line')
        mutation['failed_cases']=[r['case'] for r in validate_stage(result.stdout,output,'shifted-domain-line',validators,True) if not r['target_check_passed']]
        if result.returncode!=1 or mutation['failed_cases']!=['domain_line_v']:raise ValueError('Shared oracle missed shifted line')
        result,invalid_cli=execute(['java',*JVM,'-cp',cps['buggy'],'SqaProbe','observe',RECEIVER,'','drawBackground',SIGNATURES['drawBackground'],'NaN,0,0',POLICY_V12],output,'invalid-cli-selector')
        if result.returncode==0 or b'SQA_RESULT:' in result.stdout:
            raise ValueError('Malformed CLI input falsely counted as target outcome')
    if runtime!=implementation_hashes() or any(sha256(output/n)!=h for n,h in suite.items()):raise ValueError('Sealed runtime/source changed')
    receipt={'status':'pass','cases':24,'exact_declarations':7,'runtime_source_sha256':runtime,
        'intake_sha256':sha256(INTAKE/'receipt.json'),'suite_sha256':suite,'stages':stages,'junit_stages':junit_stages,
        'preserved_chart_eight_regression':charts,'old_chart_observation_pairs':48,'mutation':mutation,
        'fixture_negative_control':negative,'invalid_cli_protocol_error':invalid_cli,'production_class_sha256':class_hashes,'local_shared_integration_verified':True,
        'coverage_kind':'Exact inherited JDI method entry, not line/branch percentage','candidate_fault_detected':False,
        'shared_team_approval':False,'gate_a_passed':False,'primary_results_added':0,'live_requests':0,'queue_mutations':0,
        'scope':'Shared helper + original SVN2266/2264 production sources, bounded development oracles; not full Defects4J or algorithm fault result',
        'java_version':subprocess.run(['java','-version'],capture_output=True).stderr.decode(),'platform':__import__('platform').platform()}
    (output/'receipt.json').write_bytes(encoded(receipt))
    (output/'checksums.json').write_bytes(encoded({p.relative_to(output).as_posix():sha256(p) for p in sorted(output.rglob('*')) if p.is_file()}))
    return receipt


if __name__=='__main__':
    p=argparse.ArgumentParser();p.add_argument('--output',type=Path,default=OUTPUT)
    p.add_argument('--defects4j',type=Path,default=Path('/home/aomsin/sqa-round2/defects4j'));a=p.parse_args()
    r=verify(a.output,a.defects4j);print({'status':r['status'],'cases':24,'exact_declarations':7,'old_chart_pairs':48,'junit_cases':24,'gate_a_passed':False})
