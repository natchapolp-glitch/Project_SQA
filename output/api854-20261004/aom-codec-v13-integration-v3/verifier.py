"""Fresh shared Codec5 development proof, preserving Codec13 on exact production revisions."""
import argparse
import gzip
import io
import json
import os
import tarfile
import tempfile
from pathlib import Path
from datetime import datetime, timezone
from .common import ROOT, sha256, implementation_hashes, contained
from .preparation import encoded, digest
from .fixture_policy import POLICY_V12, POLICY_V13
from .codec_v13 import BASE, PREVIOUS, INTAKE, PROOF, load_intake, validators
from .joint_recipe_v10 import git_bytes
from .verify_chronology_v11 import execute, parse

JVM=['-Duser.timezone=UTC','-Duser.language=en','-Duser.country=US']
OWNERS=['org.apache.commons.codec.language.Metaphone','org.apache.commons.codec.language.SoundexUtils']
PREFIX='src/java/org/apache/commons/codec/language/'


def vectors(policy):
    result={}
    for c in policy['cases']:
        group=[x for x in policy['cases'] if x['method']==c['method']]
        result[c['case']]=-1+2*(group.index(c)+0.5)/len(group)
    return result


def driver(policy):
    targets={r['target']['method']:r['target'] for r in policy['targets']};coords=vectors(policy)
    calls=[]
    for c in policy['cases']:
        t=targets[c['method']]
        calls.append('        check('+','.join(json.dumps(x) for x in [c['case'],t['class'],t['method'],t['parameter_types']])+','+repr(coords[c['case']])+');')
    return '''public final class SqaCodecSuite {
    static int executed=0,passed=0;
    static void check(String name,String owner,String method,String params,double vector){
        String actual=SqaProbe.observeWithPolicy(owner,"",method,params,new double[]{vector,0,0},"'''+POLICY_V13+'''");
        java.util.Map<String,Object> row=SqaProbe.CodecRecipe.lastEvidence;
        if(row==null || !name.equals(row.get("case")) || !SqaProbe.targetInvoked()
            || !actual.equals(SqaProbe.CodecRecipe.json(row.get("observation"))))throw new IllegalStateException("Shared dispatch differs");
        executed++; if(Boolean.TRUE.equals(row.get("target_check_passed")))passed++;
        System.out.println(SqaProbe.CodecRecipe.json(row));
    }
    public static void main(String[] args){
        try {
'''+ '\n'.join(calls)+'''
        System.out.println("{\\"summary\\":true,\\"executed\\":"+executed+",\\"target_checks\\":"+executed+
            ",\\"passed\\":"+passed+",\\"failed\\":"+(executed-passed)+",\\"skipped\\":0,\\"fixture_errors\\":0}");
        if(passed!=43)System.exit(1);
        }catch(Throwable failure){failure.printStackTrace();System.exit(2);}
    }
}
'''


def checked(output=PROOF):
    load_intake(); ns=validators();out=Path(output)
    hashes=json.loads((out/'checksums.json').read_bytes())
    if set(hashes)!={p.relative_to(out).as_posix() for p in out.rglob('*') if p.is_file() and p.name!='checksums.json'}:raise ValueError('Complete Codec proof inventory required')
    for n,h in hashes.items():
        if sha256(contained(out,n))!=h:raise ValueError('Shared Codec proof checksum differs')
    r=json.loads((out/'receipt.json').read_bytes())
    if r['status']!='pass' or r['runtime_source_sha256']!=implementation_hashes() or r['intake_sha256']!=sha256(INTAKE/'receipt.json'):raise ValueError('Shared Codec proof binding differs')
    seal=json.loads((out/'preexecution-seal.json').read_bytes())
    if seal['runtime_source_sha256']!=r['runtime_source_sha256']:raise ValueError('Preseal runtime differs')
    for n,h in seal['suite_sha256'].items():
        if sha256(contained(out,n))!=h:raise ValueError('Presealed source/suite changed')
    for name in ('fixed_first','fixed_second','buggy_first','buggy_second','fixed_entry','buggy_entry'):
        rows=ns['validate'](parse((out/(name+'.stdout.log')).read_bytes()))
        if rows!=r['stages'][name]['cases'] or r['stages'][name]['exit_code']!=0:raise ValueError('Raw shared cases differ')
        if name.endswith('entry'):ns['validate_trace'](parse((out/(name+'.stdout.log')).read_bytes()),0)
    for label,count in [('next_char',3),('difference_score',9)]:
        rows=ns['validate'](parse((out/(label+'.stdout.log')).read_bytes()),allow_failures=True)
        if sum(not x['target_check_passed'] for x in rows)!=count or r['mutation'][label]['exit_code']!=1:raise ValueError('Sensitivity control differs')
    if len(r['codec13_old_new_pairs'])!=78 or any(not x['equal'] for x in r['codec13_old_new_pairs']):raise ValueError('Codec13 preservation incomplete')
    old=json.loads(git_bytes(BASE,PREVIOUS+'/Codec-1/targets.json'))['targets']
    for version in ('fixed','buggy'):
        for i,t in enumerate(old):
            for v in (-0.75,0,0.75):
                a=(out/f'codec13-{version}-{i}-{v}-old.stdout.log').read_bytes()
                b=(out/f'codec13-{version}-{i}-{v}-new.stdout.log').read_bytes()
                row=next(x for x in r['codec13_old_new_pairs'] if x['version']==version and x['target']==t and x['vector']==[v,0,0])
                if a!=b or digest(a)!=row['observation_sha256'] or b'SQA_FIXTURE_FAILURE:' in a or b'"target_invoked":true' not in a:
                    raise ValueError('Raw Codec13 preservation differs')
    if any(r[k] for k in ('gate_a_passed','shared_team_approval','live_requests','queue_mutations','primary_results_added','candidate_fault_detected')):raise ValueError('Development proof cannot authorize primary/live')
    return {'targets':load_intake()['targets'],'receipt':r}


def verify(output,defects4j):
    load_intake();ns=validators();policy=ns['POLICY'];runtime=implementation_hashes()
    out=Path(output).resolve();out.mkdir(parents=True,exist_ok=False)
    for n in ('fixed-production-source.tar.gz','buggy-production-source.tar.gz','policy.json'):(out/n).write_bytes((INTAKE/n).read_bytes())
    (out/'SqaProbe.java').write_bytes((ROOT/'algorithms/java/SqaProbe.java').read_bytes())
    (out/'old-SqaProbe.java').write_bytes(git_bytes(BASE,'algorithms/java/SqaProbe.java'))
    (out/'SqaCodecSuite.java').write_text(driver(policy),encoding='utf-8')
    trace=(INTAKE/'peer-trace.java').read_text().replace('sqa.development.CodecCandidateProbe','SqaCodecSuite',1).replace('sqa.development.CodecCandidateProbe','SqaProbe$CodecRecipe')
    (out/'CodecEntryTrace.java').write_text(trace,encoding='utf-8')
    (out/'verifier.py').write_bytes(Path(__file__).read_bytes())
    (out/'vectors.json').write_bytes(encoded(vectors(policy)))
    revisions={'fixed':'52d82d1dfff8c2b2ded9d843e0b03017af6d747c','buggy':'9c0cabead7cf075308b11362172ae1a48d41321c'}
    import csv
    with (Path(defects4j)/'framework/projects/Codec/commit-db').open() as f:
        row=next(r for r in csv.reader(f) if r[0]=='1')
    if [row[2],row[1]]!=[revisions['fixed'],revisions['buggy']]:raise ValueError('Actual Defects4J original revisions differ')
    (out/'preexecution-seal.json').write_bytes(encoded({'sealed_at_utc':datetime.now(timezone.utc).isoformat(),
        'runtime_source_sha256':runtime,'suite_sha256':{p.name:sha256(p) for p in out.iterdir() if p.is_file()},
        'revisions':revisions,'intake_sha256':sha256(INTAKE/'receipt.json'),'selection_used_buggy_outcomes':False,'primary':False}))
    stages={};mutations={};preserved=[];source_hashes={};class_hashes={}
    with tempfile.TemporaryDirectory(prefix='aom-codec-shared-') as temp:
        base=Path(temp);work={};cps={};original={}
        for version,revision in revisions.items():
            folder=base/version;folder.mkdir();work[version]=folder
            raw=gzip.decompress((out/(version+'-production-source.tar.gz')).read_bytes())
            exported,_=execute(['git','-c','core.autocrlf=false','-C',Path(defects4j)/'project_repos/commons-codec.git','archive',revision,'src/java'],out,version+'-original-archive')
            if exported.returncode or exported.stdout!=raw:raise ValueError('Original production archive differs')
            with tarfile.open(fileobj=io.BytesIO(raw)) as tar:
                for m in tar.getmembers():
                    if not m.isfile():continue
                    p=contained(folder,m.name);p.parent.mkdir(parents=True,exist_ok=True);p.write_bytes(tar.extractfile(m).read())
            if version=='fixed':
                for n in ('Metaphone.java','SoundexUtils.java'):
                    retained=git_bytes(BASE,PREVIOUS+'/Codec-1/fixed-source/'+PREFIX+n)
                    if (folder/(PREFIX+n)).read_bytes().replace(b'\r\n',b'\n')!=retained.replace(b'\r\n',b'\n'):raise ValueError('Retained fixed source differs')
                    (folder/(PREFIX+n)).write_bytes(retained)
            source_hashes[version]={p.relative_to(folder).as_posix():sha256(p) for p in sorted((folder/'src/java').rglob('*.java'))}
            classes=folder/'classes';classes.mkdir();cps[version]=classes
            for n in ('Metaphone.java','SoundexUtils.java'):original[(version,n)]=(folder/(PREFIX+n)).read_bytes()
            result,_=execute(['javac','--release','8','-g','-encoding','UTF-8','-d',classes,*sorted((folder/'src/java').rglob('*.java')),out/'SqaProbe.java',out/'SqaCodecSuite.java'],out,version+'-compile')
            if result.returncode:raise ValueError(result.stderr.decode())
            class_hashes[version]={n:sha256(classes/(n.replace('.','/')+'.class')) for n in OWNERS}
            for repeat in ('first','second'):
                name=version+'_'+repeat;run,record=execute(['java',*JVM,'-cp',classes,'SqaCodecSuite'],out,name)
                record['cases']=ns['validate'](parse(run.stdout));stages[name]=record
                if run.returncode:raise ValueError('Shared production oracle failed')
            if stages[version+'_first']['cases']!=stages[version+'_second']['cases']:raise ValueError('Shared repeats differ')
        run,_=execute(['javac','--add-modules','jdk.jdi','-d',cps['fixed'],out/'CodecEntryTrace.java'],out,'compile-jdi')
        if run.returncode:raise ValueError(run.stderr.decode())
        for version in revisions:
            name=version+'_entry';run,record=execute(['java','--add-modules','jdk.jdi','-cp',cps['fixed'],'CodecEntryTrace',cps[version],out/'vectors.json'],out,name)
            record['cases']=ns['validate'](parse(run.stdout));record['exact_entries']=ns['validate_trace'](parse(run.stdout),run.returncode);stages[name]=record
            if record['cases']!=stages[version+'_first']['cases']:raise ValueError('Trace changes observations')
            if class_hashes[version]!={n:sha256(cps[version]/(n.replace('.','/')+'.class')) for n in OWNERS}:raise ValueError('Tracing changed bytecode')
        old=base/'old';old.mkdir();(old/'SqaProbe.java').write_bytes((out/'old-SqaProbe.java').read_bytes())
        run,_=execute(['javac','--release','8','-d',old,old/'SqaProbe.java'],out,'compile-old-helper')
        if run.returncode:raise ValueError(run.stderr.decode())
        targets=json.loads(git_bytes(BASE,PREVIOUS+'/Codec-1/targets.json'))['targets']
        for version in revisions:
            for i,t in enumerate(targets):
                for v in (-0.75,0,0.75):
                    values=[]
                    for label,cp,fixture in [('old',str(old)+os.pathsep+str(cps[version]),POLICY_V12),('new',str(cps[version]),POLICY_V13)]:
                        run,rec=execute(['java',*JVM,'-cp',cp,'SqaProbe','observe',*[t[k] for k in ('class','constructor_types','method','parameter_types')],str(v)+',0,0',fixture],out,f'codec13-{version}-{i}-{v}-{label}')
                        if run.returncode or b'SQA_FIXTURE_FAILURE:' in run.stdout or b'"target_invoked":true' not in run.stdout:raise ValueError('Codec13 fixture/target failed')
                        values.append(run.stdout)
                    if values[0]!=values[1]:raise ValueError('Codec13 prior observations changed')
                    preserved.append({'version':version,'target':t,'vector':[v,0,0],'equal':True,'observation_sha256':digest(values[0])})
        for label,n,old,new in [('next_char','Metaphone.java','matches = string.charAt(index + 1) == c;','matches = string.charAt(index + 1) != c;'),
             ('difference_score','SoundexUtils.java','return differenceEncoded(encoder.encode(s1), encoder.encode(s2));','return differenceEncoded(encoder.encode(s1), encoder.encode(s2)) + 1;')]:
            src=work['fixed']/(PREFIX+n);raw=original[('fixed',n)]
            if raw.count(old.encode())!=1:raise ValueError('Mutation anchor differs')
            src.write_bytes(raw.replace(old.encode(),new.encode()));(out/(label+'-mutated.java')).write_bytes(src.read_bytes())
            run,_=execute(['javac','--release','8','-g','-encoding','UTF-8','-cp',cps['fixed'],'-sourcepath',work['fixed']/'src/java','-d',cps['fixed'],src],out,label+'-compile')
            if run.returncode:raise ValueError(run.stderr.decode())
            run,record=execute(['java',*JVM,'-cp',cps['fixed'],'SqaCodecSuite'],out,label)
            rows=ns['validate'](parse(run.stdout),allow_failures=True);failed=[r['case'] for r in rows if not r['target_check_passed']]
            if run.returncode!=1 or len(failed)!=(3 if label=='next_char' else 9):raise ValueError('Mutation sensitivity missing')
            record['failed_cases']=failed;mutations[label]=record
            src.write_bytes(raw)
            run,_=execute(['javac','--release','8','-g','-encoding','UTF-8','-cp',cps['fixed'],'-sourcepath',work['fixed']/'src/java','-d',cps['fixed'],src],out,label+'-restore')
            if run.returncode or class_hashes['fixed']!={n:sha256(cps['fixed']/(n.replace('.','/')+'.class')) for n in OWNERS}:raise ValueError('Original bytecode restore failed')
    result={'status':'pass','fixture_policy_id':POLICY_V13,'runtime_source_sha256':runtime,'intake_sha256':sha256(INTAKE/'receipt.json'),
        'cases':43,'exact_declarations':5,'stages':stages,'mutation':mutations,'source_revisions':revisions,'source_sha256':source_hashes,
        'compiled_class_sha256':class_hashes,'codec13_old_new_pairs':preserved,'codec13_old_new_observation_pairs':78,
        'candidate_fault_detected':False,'coverage_kind':'Exact JDI first method entry; no line/branch percentage',
        'reference_only':True,'primary_suite_cap':30,'reference_cases_are_not_a_primary_suite':True,
        'shared_team_approval':False,'gate_a_passed':False,'live_requests':0,'queue_mutations':0,'primary_results_added':0}
    (out/'receipt.json').write_bytes(encoded(result))
    (out/'checksums.json').write_bytes(encoded({p.relative_to(out).as_posix():sha256(p) for p in sorted(out.rglob('*')) if p.is_file()}))
    return result


if __name__=='__main__':
    p=argparse.ArgumentParser(description=__doc__);p.add_argument('--output',type=Path,required=True);p.add_argument('--defects4j',type=Path,required=True);a=p.parse_args()
    r=verify(a.output,a.defects4j);print({k:r[k] for k in ('status','cases','exact_declarations','codec13_old_new_observation_pairs','candidate_fault_detected')})
