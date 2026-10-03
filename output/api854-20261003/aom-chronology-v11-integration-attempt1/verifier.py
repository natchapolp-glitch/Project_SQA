"""Seal and exercise shared Chronology helper against real Time-1 revisions offline."""
import argparse
import base64
import csv
import gzip
import io
import json
import os
from pathlib import Path
import subprocess
import tarfile
import tempfile
from datetime import datetime, timezone

from .common import ROOT, sha256, implementation_hashes
from .preparation import encoded, digest
from .fixture_policy import POLICY_V11
from .chronology_v11 import load_contract, INTAKE, V10, V10_COMMIT
from .joint_recipe_v10 import git_bytes

CASES = {
    'empty_iso_offset':('empty',0),'empty_null':('empty',1),
    'single_hour_iso':('single',0),'single_invalid_hour':('single',1),
    'arrays_leap_iso':('arrays',0),'arrays_invalid_date':('arrays',1),'arrays_bad_order':('arrays',2),
    'internal_iso':('internal',0),'getfield_buddhist':('field',0),'getfield_bad_index':('field',1),
    'withchrono_buddhist':('retain',0),'withchrono_same':('retain',1),'withchrono_null':('retain',2)}
SOURCE='src/main/java/org/joda/time/Partial.java'
JVM=['-Duser.timezone=UTC','-Dorg.joda.time.DateTimeZone.Provider=org.joda.time.tz.UTCProvider']


def partial(row):
    return ('partial:'+row['chronology']+':'+row['zone']+':types=['+', '.join(row['types'])
        +']:values=['+', '.join(map(str,row['values']))+']:named=true')


def expected_outcomes(contract):
    expected={}
    for case,row in contract['independent_expected_observations'].items():
        if row.get('rejected'): expected[case]='exception:'+row['exception']
        elif case=='getfield_buddhist':
            expected[case]='field:year:epoch=2513:supplied-identity=true:type-year=true|receiver='+partial(row['receiver'])+':unchanged=true'
        else: expected[case]=partial(row)
    year=partial(contract['independent_expected_observations']['getfield_buddhist']['receiver'])
    hour=partial(contract['independent_expected_observations']['withchrono_same'])
    bud=partial(contract['independent_expected_observations']['withchrono_buddhist'])
    expected['getfield_bad_index']+='|receiver='+year+':unchanged=true'
    expected['arrays_leap_iso']+=':input-copy=true:output-copy=true'
    for case,same,receiver in [('withchrono_buddhist',False,hour),('withchrono_same',True,hour),('withchrono_null',False,bud)]:
        expected[case]+=':same='+str(same).lower()+'|receiver='+receiver+':unchanged=true'
    if set(expected)!=set(CASES): raise ValueError('Require all thirteen independent cases')
    return expected


def target_for(contract,group):
    descriptor=contract['descriptors'][group]
    constructors={
        'empty':'org.joda.time.Chronology',
        'single':'org.joda.time.DateTimeFieldType,int,org.joda.time.Chronology',
        'arrays':'[Lorg.joda.time.DateTimeFieldType;,[I,org.joda.time.Chronology',
        'internal':'org.joda.time.Chronology,[Lorg.joda.time.DateTimeFieldType;,[I', 'field':'','retain':''}
    params={'field':'int,org.joda.time.Chronology','retain':'org.joda.time.Chronology'}
    target={'class':'org.joda.time.Partial','constructor_types':constructors[group],
            'method':descriptor[0],'parameter_types':params.get(group,'')}
    if target not in contract['targets']: raise ValueError('Driver target lacks joint identity')
    return target


def driver(contract):
    expected=expected_outcomes(contract)
    calls=[]
    for case,(group,bucket) in CASES.items():
        target=target_for(contract,group)
        args=[case,*[target[k] for k in ('class','constructor_types','method','parameter_types')],expected[case]]
        calls.append('        check('+', '.join(json.dumps(s) for s in args)+', '+str(bucket)+');')
    return '''import java.util.Base64;
import java.nio.charset.StandardCharsets;
public final class SqaChronologySuite {
    private static int failed=0;
    private static String quote(String s) { return "\\\""+s.replace("\\\\","\\\\\\\\").replace("\\\"","\\\\\\\"")+"\\\""; }
    private static void check(String name,String cls,String ctor,String method,String params,String expected,double bucket) {
        String outcome="",error=null; boolean passed=false,invoked=false;
        try {
            outcome=SqaProbe.observeWithPolicy(cls,ctor,method,params,new double[]{bucket,0,0},"'''+POLICY_V11+'''");
            invoked=SqaProbe.targetInvoked();
            if (!invoked || !outcome.equals(expected)) throw new AssertionError("Independent shared oracle differs");
            passed=true;
        } catch (AssertionError assertion) { failed++; }
          catch (Throwable fixture) { error=fixture.getClass().getName()+":"+fixture.getMessage(); failed++; }
        System.out.println("{\\\"case\\\":"+quote(name)+",\\\"target_invoked\\\":"+invoked+",\\\"passed\\\":"+passed
            +",\\\"fixture_error\\\":"+(error==null?"null":quote(error))+",\\\"outcome_b64\\\":"
            +quote(Base64.getEncoder().encodeToString(outcome.getBytes(StandardCharsets.UTF_8)))+"}");
    }
    public static void main(String[] args) {
'''+ '\n'.join(calls)+'''
        if (failed>0) System.exit(1);
    }
}
'''


def parse(raw):
    return [json.loads(s) for s in raw.decode().splitlines() if s.strip()]


def validate_rows(records,contract,fixed=True):
    rows=[r for r in records if 'outcome_b64' in r]
    expected=expected_outcomes(contract)
    if [r['case'] for r in rows]!=list(CASES): raise ValueError('Shared case inventory/order differs')
    for row in rows:
        actual=base64.b64decode(row['outcome_b64'],validate=True).decode()
        if (row['target_invoked'] is not True or row['fixture_error'] is not None
                or type(row['passed']) is not bool or row['passed']!=(actual==expected[row['case']])):
            raise ValueError('Setup/skipped/weakened assertion cannot be target evidence: '+row['case'])
    if fixed and any(not r['passed'] for r in rows):
        raise ValueError('Shared fixed oracle failed: '+json.dumps([r for r in rows if not r['passed']]))
    return rows


def validate_trace(records,contract,exit_code):
    entries=[r for r in records if r.get('method_entry')]
    exact=[]
    for case,(group,bucket) in CASES.items():
        found=[r for r in entries if r['case']==case]
        if (not found or found[0]['class']!='org.joda.time.Partial' or found[0]['source_line']<=0
                or [found[0]['method'],found[0]['descriptor']]!=contract['descriptors'][group]):
            raise ValueError('Exact first target entry missing: '+case)
        exact.append(found[0])
    if ([r for r in records if r.get('trace_summary')]!=[{'trace_summary':True,'method_entries':len(entries),'debuggee_exit_code':exit_code}]
            or len({(r['method'],r['descriptor']) for r in exact})!=6):
        raise ValueError('JDI exit/entry counters differ')
    return exact


def execute(command,packet,name):
    result=subprocess.run(list(map(str,command)),capture_output=True,timeout=120)
    for suffix,raw in [('stdout.log',result.stdout),('stderr.log',result.stderr)]:
        (packet/(name+'.'+suffix)).write_bytes(raw)
    record={'argv':list(map(str,command)),'exit_code':result.returncode,
        'stdout_sha256':digest(result.stdout),'stderr_sha256':digest(result.stderr)}
    (packet/(name+'.command.json')).write_bytes(encoded(record))
    return result,record


def verify(defects4j,output):
    contract=load_contract(); expected=expected_outcomes(contract)
    output=Path(output).resolve(); d4j=Path(defects4j).resolve()
    output.mkdir(parents=True,exist_ok=False)
    runtime=implementation_hashes()
    helper=ROOT/'algorithms/java/SqaProbe.java'
    (output/'SqaProbe.java').write_bytes(helper.read_bytes())
    (output/'fixture_policy.py').write_bytes((ROOT/'scripts/study/api854/fixture_policy.py').read_bytes())
    (output/'SqaChronologySuite.java').write_text(driver(contract),encoding='utf-8')
    candidate='1a28deea84ffd346a86e79d5a5c1122266fb2237'
    tracer=git_bytes(candidate,'output/api854-20261003/chronology-development-v2/ChronologyEntryTrace.java')
    (output/'received-ChronologyEntryTrace.java').write_bytes(tracer)
    tracer=tracer.decode().replace('org.joda.time.ChronologyProbe','SqaChronologySuite',1)
    tracer=tracer.replace('org.joda.time.ChronologyProbe','SqaProbe').replace('"activeCase"','"chronologyActiveCase"')
    (output/'ChronologyEntryTrace.java').write_text(tracer,encoding='utf-8')
    (output/'verifier.py').write_bytes(Path(__file__).read_bytes())
    (output/'policy.json').write_bytes(encoded({'fixture_policy_id':POLICY_V11,'targets':contract['targets'],
        'descriptors':contract['descriptors'],'cases':CASES,'independent_expectations':expected,
        'scope':'Shared development integration; UTC/fixed +07 only; not full legal domain or primary approval'}))
    suite={p.name:sha256(p) for p in output.iterdir() if p.is_file()}
    jar=d4j/'framework/projects/Time/lib/joda-convert-1.2.jar'
    with (d4j/'framework/projects/Time/commit-db').open() as stream:
        row=next(r for r in csv.reader(stream) if r[0]=='1')
    revisions={'fixed':row[2],'buggy':row[1]}
    (output/'preexecution-seal.json').write_bytes(encoded({'sealed_at_utc':datetime.now(timezone.utc).isoformat(),
        'suite_sha256':suite,'runtime_source_sha256':runtime,'joint_receipt_sha256':sha256(INTAKE/'receipt.json'),
        'revisions':revisions,'dependency_sha256':sha256(jar),'primary':False,'gate_a_passed':False}))
    stages={}; archives={}; source_hashes={}; class_hashes={}
    with tempfile.TemporaryDirectory(prefix='aom-shared-chronology-') as tmp:
        base=Path(tmp); cps={}
        for version,revision in revisions.items():
            archive=subprocess.run(['git','--git-dir='+str(d4j/'project_repos/joda-time.git'),'archive',revision,'src/main/java'],
                capture_output=True,check=True,timeout=90).stdout
            name=version+'-production-source.tar.gz'
            (output/name).write_bytes(gzip.compress(archive,mtime=0)); archives[version]=sha256(output/name)
            work=base/version;work.mkdir()
            with tarfile.open(fileobj=io.BytesIO(archive)) as tar:
                for member in tar.getmembers():
                    if not member.isfile() or not member.name.endswith('.java'): continue
                    path=(work/member.name).resolve()
                    if not path.is_relative_to(work): raise ValueError('Unsafe archive path')
                    path.parent.mkdir(parents=True,exist_ok=True);path.write_bytes(tar.extractfile(member).read())
            if version=='fixed':
                retained=git_bytes(V10_COMMIT,V10+'/Time-1/fixed-source/'+SOURCE)
                if (work/SOURCE).read_bytes().replace(b'\r\n',b'\n')!=retained.replace(b'\r\n',b'\n'):
                    raise ValueError('Fixed source differs from preserved v10')
                (work/SOURCE).write_bytes(retained)
            source_hashes[version]={p.relative_to(work).as_posix():sha256(p) for p in sorted(work.rglob('*.java'))}
            classes=work/'classes';classes.mkdir();cp=os.pathsep.join(map(str,[classes,jar]));cps[version]=cp
            result,_=execute(['javac','--release','8','-g','-cp',str(jar),'-sourcepath',str(work/'src/main/java'),
                '-d',str(classes),str(work/SOURCE),str(output/'SqaProbe.java'),str(output/'SqaChronologySuite.java')],output,version+'-compile')
            if result.returncode: raise ValueError(result.stderr.decode())
            class_hashes[version]=sha256(classes/'org/joda/time/Partial.class')
            if version=='fixed':
                result,_=execute(['javac','--add-modules','jdk.jdi','-d',str(classes),str(output/'ChronologyEntryTrace.java')],output,'compile-jdi')
                if result.returncode: raise ValueError(result.stderr.decode())
            for repeat in ('first','second'):
                stage=version+'_'+repeat
                result,record=execute(['java',*JVM,'-cp',cp,'SqaChronologySuite'],output,stage)
                record['cases']=validate_rows(parse(result.stdout),contract,fixed=(version=='fixed'))
                if result.returncode!=(0 if all(r['passed'] for r in record['cases']) else 1): raise ValueError('Exit/assertion differs')
                stages[stage]=record
            if stages[version+'_first']['cases']!=stages[version+'_second']['cases']: raise ValueError('Repeatability failed')
        for version,cp in cps.items():
            result,record=execute(['java','--add-modules','jdk.jdi','-cp',cps['fixed'],'ChronologyEntryTrace',cp],output,version+'-entry-trace')
            rows=parse(result.stdout);record['cases']=validate_rows(rows,contract,fixed=(version=='fixed'))
            record['exact_target_entries']=validate_trace(rows,contract,result.returncode)
            if (record['cases']!=stages[version+'_first']['cases'] or result.returncode!=stages[version+'_first']['exit_code']
                    or sha256(base/version/'classes/org/joda/time/Partial.class')!=class_hashes[version]):
                raise ValueError('Tracing changed source/observations/exit')
            stages[version+'_entry_trace']=record
        source=base/'fixed'/SOURCE
        text=source.read_text();needle='return iTypes[index].getField(chrono);'
        if text.count(needle)!=1: raise ValueError('Sensitivity source differs')
        source.write_text(text.replace(needle,'return iTypes[index].getField(iChronology);'))
        result,_=execute(['javac','--release','8','-g','-cp',cps['fixed'],'-d',str(base/'fixed/classes'),str(source)],output,'compile-sensitivity')
        if result.returncode: raise ValueError(result.stderr.decode())
        result,mutation=execute(['java',*JVM,'-cp',cps['fixed'],'SqaChronologySuite'],output,'ignored-supplied-chronology')
        rows=validate_rows(parse(result.stdout),contract,fixed=False)
        mutation['failed_cases']=[r['case'] for r in rows if not r['passed']]
        if result.returncode!=1 or mutation['failed_cases']!=['getfield_buddhist']: raise ValueError('Supplied chronology mutation escaped oracle')
    if (implementation_hashes()!=runtime or any(sha256(output/n)!=h for n,h in suite.items())
            or sha256(jar)!=json.loads((output/'preexecution-seal.json').read_bytes())['dependency_sha256']):
        raise ValueError('Sealed sources changed during execution')
    result={'schema_version':1,'status':'pass','fixture_policy_id':POLICY_V11,
        'scope':'Fresh shared helper integration, underlying Time-1 production revisions; no generated algorithm/KKU suite or full Defects4J evaluation',
        'cases':13,'exact_declarations':6,'fixed_observations':26,'buggy_observations':26,
        'runtime_source_sha256':runtime,'suite_sha256':suite,'preexecution_seal_sha256':sha256(output/'preexecution-seal.json'),
        'joint_receipt_sha256':sha256(INTAKE/'receipt.json'),'source_archives':archives,'compiled_source_sha256':source_hashes,
        'production_class_sha256':class_hashes,'stages':stages,'sensitivity':mutation,
        'buggy_failed_cases':[r['case'] for r in stages['buggy_first']['cases'] if not r['passed']],
        'coverage_kind':'JDI exact first method entry, unchanged production bytecode','line_branch_percent':None,
        'local_shared_integration_verified':True,'beam_champ_final_condition_approval':False,
        'gate_a_passed':False,'final_reserve':None,'live_requests':0,'queue_mutations':0,'primary_results_added':0}
    (output/'receipt.json').write_bytes(encoded(result))
    (output/'checksums.json').write_bytes(encoded({p.name:sha256(p) for p in sorted(output.iterdir()) if p.is_file()}))
    return result


if __name__=='__main__':
    p=argparse.ArgumentParser(description=__doc__);p.add_argument('--defects4j',type=Path,required=True);p.add_argument('--output',type=Path,required=True)
    a=p.parse_args()
    try: result=verify(a.defects4j,a.output)
    except Exception as error:
        if a.output.exists() and not (a.output/'checksums.json').exists():
            (a.output/'failure.json').write_bytes(encoded({'status':'fail','reason':str(error),'gate_a_passed':False}))
            (a.output/'checksums.json').write_bytes(encoded({f.name:sha256(f) for f in a.output.iterdir() if f.is_file()}))
        raise
    print({k:result[k] for k in ('status','cases','exact_declarations','buggy_failed_cases','gate_a_passed')})
