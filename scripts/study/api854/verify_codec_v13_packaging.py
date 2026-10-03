"""Pack and execute bounded reference suites within the unchanged 30-method cap."""
import argparse
import gzip
import io
import json
import os
import tarfile
import tempfile
from pathlib import Path
from .common import ROOT, sha256, implementation_hashes, contained
from .preparation import encoded
from .codec_v13 import PROOF, INTAKE, validators
from .fixture_policy import POLICY_V13
from .verify_codec_v13 import checked, vectors, JVM
from .verify_chronology_v11 import execute
from scripts.study.generate import suite_source
from .pack_suite import pack_suite


def verify(out,defects4j):
    checked();out=Path(out);out.mkdir(parents=True,exist_ok=False)
    ns=validators();policy=ns['POLICY'];coords=vectors(policy)
    target={r['target']['method']:r['target'] for r in policy['targets']}
    cases=ns['validate']([json.loads(l) for l in (PROOF/'fixed_first.stdout.log').read_text().splitlines()])
    rows=[{'case_id':str(i),'retained':True,'target':target[c['method']],'vector':[coords[c['case']],0,0],
           'fixed_first':{'outcome':json.dumps(r['observation'],separators=(',',':'))}} for i,(c,r) in enumerate(zip(policy['cases'],cases))]
    junit=Path(defects4j)/'framework/projects/lib/junit-4.12-hamcrest-1.3.jar'
    deps=out/'dependencies';deps.mkdir();(deps/junit.name).write_bytes(junit.read_bytes())
    for start,end in [(0,30),(30,43)]:
        sources=out/f'suite-{start}-{end}';sources.mkdir()
        (sources/'GeneratedStudyTest.java').write_text(suite_source(rows[start:end],POLICY_V13),encoding='utf-8')
        pack_suite(sources,out/f'pack-{start}-{end}',end-start,30)
    (out/'preexecution-seal.json').write_bytes(encoded({'runtime_source_sha256':implementation_hashes(),
        'integration_sha256':sha256(PROOF/'receipt.json'),
        'input_sha256':{p.relative_to(out).as_posix():sha256(p) for p in out.rglob('*') if p.is_file()},
        'reference_only':True,'primary_suite_cap':30}))
    stages={}
    with tempfile.TemporaryDirectory(prefix='aom-codec-packaging-') as temp:
        for version in ('fixed','buggy'):
            folder=Path(temp)/version;folder.mkdir()
            with tarfile.open(fileobj=io.BytesIO(gzip.decompress((INTAKE/(version+'-production-source.tar.gz')).read_bytes()))) as tar:
                for m in tar.getmembers():
                    if not m.isfile():continue
                    p=contained(folder,m.name);p.parent.mkdir(parents=True,exist_ok=True);p.write_bytes(tar.extractfile(m).read())
            for start,end in [(0,30),(30,43)]:
                classes=folder/f'classes-{start}';classes.mkdir();cp=os.pathsep.join(map(str,[classes,deps/junit.name]))
                run,_=execute(['javac','--release','8','-encoding','UTF-8','-cp',cp,'-d',classes,
                              *sorted((folder/'src/java').rglob('*.java')),out/f'suite-{start}-{end}/GeneratedStudyTest.java'],out,f'{version}-{start}-compile')
                if run.returncode:raise ValueError(run.stderr.decode())
                # execute() does not accept cwd; Java writes counters in its launch directory.
                import subprocess
                for repeat in ('first','second'):
                    name=f'{version}-{start}-{repeat}';argv=['java',*JVM,'-cp',cp,'org.junit.runner.JUnitCore','GeneratedStudyTest']
                    run=subprocess.run(list(map(str,argv)),cwd=folder,capture_output=True,timeout=120)
                    (out/(name+'.stdout.log')).write_bytes(run.stdout);(out/(name+'.stderr.log')).write_bytes(run.stderr)
                    raw=(folder/'sqa-stage-counts.json').read_bytes();(out/(name+'.counts.json')).write_bytes(raw)
                    counts=json.loads(raw.decode().replace('\\"','"').replace('\\n','\n'))
                    if run.returncode or counts!={'schema_version':1,'executed':end-start,'skipped':0,'target_checks':end-start}:raise ValueError('Nested Codec suite failed/counters differ')
                    stages[name]={'argv':list(map(str,argv)),'exit_code':run.returncode,'counts':counts,
                                  'stdout_sha256':sha256(out/(name+'.stdout.log')),'counter_sha256':sha256(out/(name+'.counts.json'))}
    result={'status':'pass','runtime_source_sha256':implementation_hashes(),'integration_sha256':sha256(PROOF/'receipt.json'),
            'reference_cases':43,'suite_sizes':[30,13],'stages':stages,'full_defects4j_evaluation':False,
            'primary_results_added':0,'live_requests':0,'queue_mutations':0,'gate_a_passed':False}
    (out/'receipt.json').write_bytes(encoded(result));(out/'checksums.json').write_bytes(encoded({p.relative_to(out).as_posix():sha256(p) for p in sorted(out.rglob('*')) if p.is_file()}))
    return result


if __name__=='__main__':
    p=argparse.ArgumentParser(description=__doc__);p.add_argument('--output',type=Path,required=True);p.add_argument('--defects4j',type=Path,required=True);a=p.parse_args()
    print(verify(a.output,a.defects4j)['status'])
