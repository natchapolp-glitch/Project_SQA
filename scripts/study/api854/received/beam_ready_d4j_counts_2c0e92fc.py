"""Observe Ant JUnit XML without changing generated Java, assertions or discovery.

The caller must hold the host CPU lock. The temporary formatter-only change is
sealed as a distinct execution profile and restored even when a stage fails.
"""
from contextlib import contextmanager
import hashlib,json,shutil
from pathlib import Path
import xml.etree.ElementTree as ET

REPORT_DIR='.beam-ready-junit-reports'
def digest(data):return hashlib.sha256(data).hexdigest()
def require(ok,message):
    if not ok:raise ValueError(message)
def write(path,value):
    with Path(path).open('x',encoding='utf-8',newline='\n') as stream:json.dump(value,stream,ensure_ascii=False,indent=2);stream.write('\n')
def augment_formatter(raw):
    text=raw.decode('utf-8')
    marker='<target name="run.gen.tests"'
    require(text.count(marker)==1,'Missing/duplicate generated-suite Ant target')
    start=text.index(marker);end=text.index('</target>',start)+len('</target>')
    block=text[start:end]
    require(REPORT_DIR not in text,'Count observer already installed')
    anchor='<junit printsummary="no"'
    require(block.count(anchor)==1,'Ambiguous Ant JUnit invocation')
    block=block.replace(anchor,'<mkdir dir="${basedir}/'+REPORT_DIR+'" />\n        '+anchor,1)
    anchor='<formatter classname="edu.washington.cs.mut.testrunner.Formatter" usefile="false" />'
    require(block.count(anchor)==1,'Missing canonical formatter')
    block=block.replace(anchor,anchor+'\n            <formatter type="xml" />',1)
    anchor='<batchtest unless="test.entry.class">'
    require(block.count(anchor)==1,'Unexpected generated-suite discovery')
    block=block.replace(anchor,'<batchtest unless="test.entry.class" todir="${basedir}/'+REPORT_DIR+'">',1)
    anchor='<test name="${test.entry.class}" methods="${test.entry.method}" if="test.entry.class" />'
    require(block.count(anchor)==1,'Unexpected single-test invocation')
    block=block.replace(anchor,'<test name="${test.entry.class}" methods="${test.entry.method}" if="test.entry.class" todir="${basedir}/'+REPORT_DIR+'" />',1)
    result=(text[:start]+block+text[end:]).encode('utf-8')
    ET.fromstring(result)
    return result
@contextmanager
def observe_framework(framework,output):
    path=Path(framework)/'framework/projects/defects4j.build.xml';output=Path(output)
    output.mkdir(exist_ok=False);original=path.read_bytes();modified=augment_formatter(original)
    (output/'defects4j.build.original.xml').write_bytes(original)
    (output/'defects4j.build.observed.xml').write_bytes(modified)
    write(output/'preexecution.json',{'path':str(path),'original_sha256':digest(original),'observed_sha256':digest(modified),'observer_sha256':digest(Path(__file__).read_bytes()),'changes':'Add XML formatter and report directory only; unchanged test sources, filters, classpath, JVM/fork settings, assertions and canonical formatter','primary':False})
    path.write_bytes(modified)
    try:yield
    finally:
        seen=path.read_bytes();path.write_bytes(original)
        write(output/'restoration.json',{'observed_bytes_unchanged':seen==modified,'restored_exact_bytes':path.read_bytes()==original,'restored_sha256':digest(path.read_bytes())})
        require(seen==modified,'Framework changed concurrently while CPU lock was held')
        require(path.read_bytes()==original,'Framework restoration failed')
def parse_reports(paths):
    suites=[];identities=set();tests=skipped=failures=errors=0
    for path in sorted(paths):
        root=ET.parse(path).getroot();require(root.tag=='testsuite','Unexpected JUnit XML root')
        cases=root.findall('testcase');require(int(root.attrib['tests'])==len(cases),'XML test inventory mismatch')
        local={'tests':len(cases),'skipped':sum(c.find('skipped') is not None for c in cases),'failures':sum(c.find('failure') is not None for c in cases),'errors':sum(c.find('error') is not None for c in cases)}
        for name in ['failures','errors']:require(local[name]==int(root.attrib.get(name,0)),'XML '+name+' mismatch')
        require(local['skipped']==int(root.attrib.get('skipped',0)),'XML skipped mismatch')
        for case in cases:
            identity=(case.attrib.get('classname',root.attrib['name']),case.attrib['name'])
            require(identity not in identities,'Duplicate JUnit test identity');identities.add(identity)
        suites.append({'class':root.attrib['name'],**local,'duration_seconds':float(root.attrib.get('time','0')),'report_sha256':digest(Path(path).read_bytes())})
        tests+=local['tests'];skipped+=local['skipped'];failures+=local['failures'];errors+=local['errors']
    require(suites,'No actual JUnit XML reports')
    return {'source':'Actual Ant JUnit XML formatter in the same Defects4J stage','discovered':tests,'executed':tests-skipped,'skipped':skipped,'failed':failures,'errors':errors,'target_checks':None,'target_checks_source':None,'suites':suites,'test_identities':[list(k) for k in sorted(identities)]}
def install_collector(evaluator,worktrees_root):
    original=evaluator.run_command;worktrees_root=Path(worktrees_root).resolve()
    def wrapped(argv,cwd,directory,timeout):
        args=[str(a) for a in argv];tree=None
        if len(args)>1 and args[1] in {'test','coverage'} and '-w' in args and '-s' in args:
            tree=Path(args[args.index('-w')+1]).resolve()
            require(tree.is_relative_to(worktrees_root),'Counts tree outside declared host worktrees')
            folder=tree/REPORT_DIR
            if folder.exists():
                require(not folder.is_symlink(),'Unexpected reports link')
                for path in folder.glob('TEST-*.xml'):
                    require(path.is_file() and not path.is_symlink(),'Unexpected report entry');path.unlink()
        result=original(argv,cwd,directory,timeout)
        if tree is not None:
            paths=sorted((tree/REPORT_DIR).glob('TEST-*.xml'));directory=Path(directory)
            if paths:
                archive=directory/'junit-reports';archive.mkdir(exist_ok=False)
                for path in paths:shutil.copyfile(path,archive/path.name)
                counts=parse_reports(archive.glob('TEST-*.xml'))
                embedded=directory/'sqa-stage-counts.json'
                # evaluate_run copies embedded counts after run_command returns.
                if (tree/'sqa-stage-counts.json').is_file():
                    embedded_data=json.loads((tree/'sqa-stage-counts.json').read_bytes())
                    require(embedded_data['executed']==counts['executed'] and embedded_data['skipped']==counts['skipped'],'Embedded counters disagree with actual JUnit XML')
                    counts['target_checks']=embedded_data['target_checks'];counts['target_checks_source']='Unchanged suite embedded counter; not inferred from JUnit count'
                write(directory/'actual-junit-counts.json',counts);result['actual_junit_counts']=counts
            else:
                write(directory/'actual-junit-counts.json',{'status':'unavailable','reason':'No JUnit XML; compilation/launch may have failed. No execution count inferred.'})
        return result
    evaluator.run_command=wrapped
    return original
