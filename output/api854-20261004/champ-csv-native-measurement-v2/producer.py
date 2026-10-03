"""Measure all four Csv development outcomes on exact production revisions.

Native Java 17/JUnit/Cobertura, not a full Defects4J CLI run or primary result.
AI Java is extracted unchanged; fixed failures reject the whole suite.
"""
import argparse
import csv
from datetime import datetime, timezone
import io
import json
import os
from pathlib import Path
import re
import subprocess
import sys
import tarfile
import tempfile
import time
import xml.etree.ElementTree as ET

from .common import ROOT, read_json, write_json, sha256
from .review_joint_recipe_intake import digest, require, extract_archive
from .review_v10_readiness import BatchedObjects
from .start_csv_development import AOM, PREP, CONDITION
from .pack_suite import package_name, pack_suite

RUNNER=r'''import org.junit.runner.*;
public final class CsvMeasurementRunner {
 public static void main(String[] args)throws Exception {
  Class<?>[] classes=new Class<?>[args.length];
  for(int i=0;i<args.length;i++)classes[i]=Class.forName(args[i]);
  Result r=new JUnitCore().run(classes);
  for(org.junit.runner.notification.Failure f:r.getFailures())System.out.println(f.getTrace());
  System.out.println("CSV_COUNTS:{\"executed\":"+r.getRunCount()+",\"skipped\":"+r.getIgnoreCount()
    +",\"failed\":"+r.getFailureCount()+"}");
  if(!r.wasSuccessful())System.exit(1);
 }
}
'''


def command(argv,cwd,out,name,timeout=180):
    argv=list(map(str,argv));start=time.monotonic()
    try:
        r=subprocess.run(argv,cwd=cwd,capture_output=True,timeout=timeout)
        code=r.returncode;stdout=r.stdout;stderr=r.stderr;timed_out=False
    except subprocess.TimeoutExpired as e:
        code=None;stdout=e.stdout or b'';stderr=e.stderr or b'';timed_out=True
    (out/(name+'.stdout.log')).write_bytes(stdout);(out/(name+'.stderr.log')).write_bytes(stderr)
    record={'argv':argv,'exit_code':code,'timed_out':timed_out,'duration_seconds':time.monotonic()-start,
            'stdout_sha256':digest(stdout),'stderr_sha256':digest(stderr)}
    write_json(out/(name+'.command.json'),record)
    return stdout,record


def sources(repository,revision,destination):
    raw=subprocess.check_output(['git','-c','core.autocrlf=false','--git-dir='+str(repository),'archive',revision,'src/main/java'])
    with tarfile.open(fileobj=io.BytesIO(raw)) as archive:
        for member in archive:
            if not member.isfile() or not member.name.endswith('.java'):continue
            path=(destination/member.name).resolve();require(path.is_relative_to(destination),'Unsafe source archive')
            path.parent.mkdir(parents=True,exist_ok=True);path.write_bytes(archive.extractfile(member).read())
    return digest(raw)


def java_sources(raw,destination):
    text=raw.decode('utf-8');blocks=re.findall(r'^```(?:java)?\s*\n(.*?)^```\s*$',text,re.MULTILINE|re.DOTALL)
    require(blocks and len(re.findall(r'^```',text,re.MULTILINE))==2*len(blocks),'Incomplete Java fences')
    names=[];total=0;manifest={}
    for block in blocks:
        public=re.findall(r'\bpublic\s+(?:final\s+)?class\s+(\w+)',block)
        require(len(public)==1,'Exactly one public test class per unchanged Java block required')
        name=public[0];package=package_name(block)
        require(name not in {'ExtendedBufferedReader','SqaProbe'},'Production/helper shadowing rejected')
        require(not any(s in block for s in ('ProcessBuilder','Runtime.getRuntime','java.net.','System.exit','java.nio.file.','FileOutputStream')),'Unexpected external side effects')
        count=len(re.findall(r'@(?:org\.junit\.)?Test\b',block));require(count>0,'No JUnit tests')
        total+=count;require(total<=30,'Entire suite over cap')
        path=destination/package.replace('.','/')/(name+'.java');require(not path.exists(),'Duplicate Java class')
        path.parent.mkdir(parents=True,exist_ok=True);path.write_bytes(block.encode('utf-8'))
        manifest[path.relative_to(destination).as_posix()]=sha256(path)
        names.append((package+'.' if package else '')+name)
    return names,total,manifest


def counts(raw):
    lines=[x for x in raw.splitlines() if x.startswith(b'CSV_COUNTS:')]
    require(len(lines)==1,'Missing actual JUnit counter record')
    return json.loads(lines[0].split(b':',1)[1])


def coverage_xml(path):
    tree=ET.parse(path);classes=tree.findall('.//class');target=[c for c in classes if c.get('name')=='org.apache.commons.csv.ExtendedBufferedReader']
    require(len(target)==1,'Exact target coverage class missing')
    c=target[0];lines=c.findall('./lines/line')
    result={'kind':'Cobertura line/branch coverage of the supplied target class, not whole-project percentage',
            'line_rate':float(c.get('line-rate')),'branch_rate':float(c.get('branch-rate')),
            'instrumented_lines':len(lines),'covered_lines':sum(int(l.get('hits'))>0 for l in lines),
            'methods':[{'name':m.get('name'),'signature':m.get('signature'),'line_rate':float(m.get('line-rate')),
                        'branch_rate':float(m.get('branch-rate'))} for m in c.findall('./methods/method')]}
    return result


def run(generation,output,defects4j):
    generation=Path(generation).resolve();output=Path(output).resolve();defects4j=Path(defects4j).resolve()
    require(output.is_relative_to(ROOT/'output') and not output.exists(),'New contained output required')
    received=read_json(generation/'preexecution-plan.json');require(received['condition']==CONDITION and received['aom_commit']==AOM,'Generation condition differs')
    for n,h in read_json(generation/'checksums.json').items():require(sha256(generation/n)==h,'Received generation bytes changed')
    output.mkdir(parents=True,exist_ok=False)
    aom=BatchedObjects(AOM);runtime=received['source_v12_runtime_sha256']
    with (defects4j/'framework/projects/Csv/commit-db').open(encoding='utf-8') as stream:
        row=next(r for r in csv.reader(stream) if r[0]=='1')
    revisions={'buggy':row[1],'fixed':row[2]}
    require(revisions['fixed']=='de1838ea067f3fbc4c7c21b9eeae077c739ecb73' and revisions['buggy']=='0833f45bffd40f44ba6f294d84e9bac8a9ba0a37','Pinned revisions differ')
    libs=defects4j/'framework/projects/lib';junit=libs/'junit-4.12-hamcrest-1.3.jar';cobertura=libs/'cobertura-2.0.3.jar'
    jars=[junit,cobertura,*sorted((libs/'cobertura-2.0.3-lib').glob('*.jar'))]
    (output/'producer.py').write_bytes(Path(__file__).read_bytes());(output/'CsvMeasurementRunner.java').write_text(RUNNER,encoding='utf-8',newline='\n')
    rows=[]
    with tempfile.TemporaryDirectory(prefix='.champ-csv-development-',dir=ROOT/'output') as folder:
        temp=Path(folder).resolve();require(temp.is_relative_to(ROOT/'output'),'Unsafe temporary cleanup path')
        snapshot=temp/'snapshot';snapshot.mkdir()
        archive=extract_archive(AOM,['scripts/study','algorithms','experiments/configs/api854-20261003',PREP],snapshot)
        for p,h in runtime.items():require(sha256(snapshot/p)==h,'Extracted runtime differs')
        source_hashes={};classes={}
        for version,revision in revisions.items():
            base=temp/version;base.mkdir();source_hashes[version]={'archive_sha256':sources(defects4j/'project_repos/commons-csv.git',revision,base)}
            java=sorted(base.rglob('*.java'));source_hashes[version]['sources']={p.relative_to(base).as_posix():sha256(p) for p in java}
            if version=='fixed':require((base/'src/main/java/org/apache/commons/csv/ExtendedBufferedReader.java').read_bytes()==aom.blob(PREP+'/fixed-source/src/main/java/org/apache/commons/csv/ExtendedBufferedReader.java'),'Fixed target source differs')
            classes[version]=base/'classes';classes[version].mkdir()
        write_json(output/'preexecution-seal.json',{'condition':CONDITION,'aom_commit':AOM,'project':'Csv','bug_id':1,
            'exact_production_revisions':revisions,'production_source_sha256':source_hashes,'runtime_source_sha256':runtime,
            'snapshot_archive_sha256':archive,'dependencies_sha256':{str(p.relative_to(defects4j)):sha256(p) for p in jars},
            'generation_manifest_sha256':sha256(generation/'checksums.json'),'producer_sha256':sha256(output/'producer.py'),
            'counter_runner_sha256':sha256(output/'CsvMeasurementRunner.java'),'seed':101,'budget':30,'test_method_cap':30,
            'java_execution':'Native Java17; production release7 for bundled ASM4 instrumentation, suites/helper release8',
            'primary_results_allowed':False,'full_defects4j_cli_run':False,'ai_domain_equivalence_verified':False})
        for version in revisions:
            base=temp/version;java=sorted((base/'src/main/java').rglob('*.java'))
            _,r=command(['javac','--release','7','-g','-d',classes[version],*java],temp,output,'compile-production-'+version)
            require(r['exit_code']==0,'Production compile failed: '+version)
        helper=snapshot/'algorithms/java/SqaProbe.java';helperclasses=temp/'helper';helperclasses.mkdir()
        _,r=command(['javac','--release','8','-g','-d',helperclasses,helper, '-cp',str(junit),output/'CsvMeasurementRunner.java'],temp,output,'compile-helper-counter')
        require(r['exit_code']==0,'Helper/counter compile failed')
        for approach in ('cmaes','fscs-art','kku-claude','kku-gemini'):
            out=output/approach;out.mkdir();sources_dir=out/'sources';sources_dir.mkdir()
            summary={'condition':CONDITION,'approach':approach,'project':'Csv','bug_id':1,'scope':'Native development measurement; full Defects4J evaluation pending','primary_result':False}
            if approach.startswith('kku-'):
                gen=read_json(generation/approach/'generation-receipt.json');summary['generation']=gen
                if gen['outcome']!='response_received':
                    summary['status']='invalid_generation_'+gen['outcome'];write_json(out/'receipt.json',summary);rows.append(summary);continue
                names,declared,source_manifest=java_sources((generation/approach/'raw-response.txt').read_bytes(),sources_dir)
            else:
                algout=out/'algorithm-generation'
                _,r=command([sys.executable,'-B','-X','utf8','-m','scripts.study.generate','--project','Csv','--bug-id','1',
                    '--generator',approach,'--budget','30','--seed','101','--targets',snapshot/PREP/'targets.json',
                    '--classpath',os.pathsep.join(map(str,[classes['fixed'],helperclasses])),
                    '--output',algout,'--fixture-policy','aom-beam-champ-graphics-fixtures-v12-development'],snapshot,out,'generate-algorithm',900)
                if r['exit_code']!=0:
                    summary['status']='algorithm_generation_failed';summary['command']=r;write_json(out/'receipt.json',summary);rows.append(summary);continue
                generated=algout/'GeneratedStudyTest.java';require(generated.exists(),'Algorithm Java source missing')
                (sources_dir/'GeneratedStudyTest.java').write_bytes(generated.read_bytes())
                names=['GeneratedStudyTest'];declared=len(re.findall(r'@Test\b',generated.read_text(encoding='utf-8')))
                source_manifest={'GeneratedStudyTest.java':sha256(generated)};summary['generation_seconds']=r['duration_seconds']
            require(0<declared<=30,'Entire generated suite invalid count')
            summary['declared_test_count']=declared;summary['source_sha256']=source_manifest
            pack_suite(sources_dir,out/'packaged-suite',declared)
            testclasses=temp/(approach+'-testclasses');testclasses.mkdir()
            cp=[classes['fixed'],helperclasses,*jars]
            _,r=command(['javac','--release','8','-g','-cp',os.pathsep.join(map(str,cp)),'-d',testclasses,*sorted(sources_dir.rglob('*.java'))],temp,out,'compile-tests')
            if r['exit_code']!=0:
                summary['status']='compile_failed';summary['compile_command']=r;write_json(out/'receipt.json',summary);rows.append(summary);continue
            stages={}
            for stage in ('fixed_first','fixed_second'):
                cp=[classes['fixed'],testclasses,helperclasses,*jars]
                raw,r=command(['java','-Djava.awt.headless=true','-Duser.timezone=UTC','-cp',os.pathsep.join(map(str,cp)),'CsvMeasurementRunner',*names],temp,out,stage)
                stages[stage]={'command':r,'counts':counts(raw)}
            summary['stages']=stages
            if any(s['counts']['failed'] or s['counts']['skipped'] or s['counts']['executed']!=declared or s['command']['exit_code'] for s in stages.values()):
                summary['status']='fixed_validation_failed_entire_suite_rejected';write_json(out/'receipt.json',summary);rows.append(summary);continue
            cp=[classes['buggy'],testclasses,helperclasses,*jars]
            raw,r=command(['java','-Djava.awt.headless=true','-Duser.timezone=UTC','-cp',os.pathsep.join(map(str,cp)),'CsvMeasurementRunner',*names],temp,out,'buggy')
            stages['buggy']={'command':r,'counts':counts(raw)}
            require(stages['buggy']['counts']['executed']==declared and stages['buggy']['counts']['skipped']==0,'Buggy counts differ')
            instrumented=temp/(approach+'-instrumented');instrumented.mkdir();data=out/'coverage.ser'
            toolcp=os.pathsep.join(map(str,jars[1:]))
            _,r=command(['java','-Dnet.sourceforge.cobertura.datafile='+str(data),'-cp',toolcp,'net.sourceforge.cobertura.instrument.Main',
                '--datafile',data,'--destination',instrumented,'--basedir',classes['fixed'],classes['fixed']],temp,out,'instrument')
            require(r['exit_code']==0,'Instrumentation failed')
            cp=[instrumented,testclasses,helperclasses,*jars]
            raw,r=command(['java','-Djava.awt.headless=true','-Duser.timezone=UTC','-Dnet.sourceforge.cobertura.datafile='+str(data),
                '-cp',os.pathsep.join(map(str,cp)),'CsvMeasurementRunner',*names],temp,out,'coverage')
            stages['coverage']={'command':r,'counts':counts(raw)}
            require(r['exit_code']==0 and stages['coverage']['counts']==stages['fixed_first']['counts'],'Instrumented fixed results differ')
            report=out/'coverage-report';report.mkdir()
            _,r=command(['java','-Dnet.sourceforge.cobertura.datafile='+str(data),'-cp',toolcp,'net.sourceforge.cobertura.reporting.Main',
                '--format','xml','--datafile',data,'--destination',report,temp/'fixed/src/main/java'],temp,out,'coverage-report')
            require(r['exit_code']==0 and (report/'coverage.xml').exists(),'Coverage report failed')
            summary['coverage']=coverage_xml(report/'coverage.xml');summary['status']='native_fixed_twice_buggy_coverage_measured'
            summary['fault_detected']=stages['buggy']['counts']['failed']>0
            summary['target_checks_per_stage']=None
            summary['limitations']=['Native exact production sources; no full Defects4J CLI/environment acceptance','Target invocation per test not independently counted; JUnit executed/skipped and actual target coverage supplied','No full input-domain equivalence or full semantic declaration approval']
            write_json(out/'receipt.json',summary);rows.append(summary)
    result={'condition':CONDITION,'status':'four_approach_native_development_outcomes_recorded','records':rows,'approaches':len(rows),
            'full_defects4j_evaluations':0,'primary_results_added':0,'primary_gate_a_passed':False,'queue_mutations':0,
            'completed_at_utc':datetime.now(timezone.utc).isoformat()}
    write_json(output/'receipt.json',result)
    write_json(output/'checksums.json',{p.relative_to(output).as_posix():sha256(p) for p in sorted(output.rglob('*')) if p.is_file()})
    return result


if __name__=='__main__':
    p=argparse.ArgumentParser(description=__doc__);p.add_argument('--generation',type=Path,required=True);p.add_argument('--output',type=Path,required=True);p.add_argument('--defects4j',type=Path,required=True)
    a=p.parse_args();r=run(a.generation,a.output,a.defects4j)
    print(json.dumps({'status':r['status'],'records':[{'approach':x['approach'],'status':x['status']} for x in r['records']]}))
