"""Explain and prospectively bind Compress-1 benchmark bytes without editing sources."""
import argparse,hashlib,json,shutil,subprocess,tempfile
from datetime import datetime,timezone
from pathlib import Path
import ready_compress_d4j_reference as reference
ROOT=Path(__file__).resolve().parents[3]
PATH='src/main/java/org/apache/commons/compress/archivers/cpio/CpioArchiveOutputStream.java'
read=lambda p:json.loads(Path(p).read_bytes())
def run(intake,output):
    intake=intake.resolve();output=output.resolve();reference.require(intake.is_relative_to(ROOT/'output') and output.is_relative_to(ROOT/'output') and not output.exists(),'Fresh contained output required')
    for name,digest in read(intake/'checksums.json').items():reference.require(reference.sha(intake/name)==digest,'Received packet changed')
    native=intake/'received-champ/output/api854-20261004/champ-compress-native-measurement-v2';seal=read(native/'preexecution-seal.json')
    output.mkdir();(output/'producer.py').write_bytes(Path(__file__).read_bytes());(output/'reference-producer.py').write_bytes(Path(reference.__file__).read_bytes())
    d4j=Path('/home/beam/sqa-beam/defects4j');worktrees=Path('/home/beam/sqa-beam/worktrees')
    benchmark=reference.derive('Compress',1,'commons-compress.git','src/main/java',seal,d4j,output/'official-reference',worktrees)
    native_sources=seal['production_source_sha256']['buggy']['sources']
    changed=[k for k in sorted(benchmark) if benchmark[k]!=native_sources[k]]
    reference.require(changed==[PATH],'Unexpected native/benchmark difference')
    fixed_bytes=subprocess.check_output(['git','-C',str(d4j/'project_repos/commons-compress.git'),'show',seal['exact_production_revisions']['fixed']+':'+PATH])
    with tempfile.TemporaryDirectory(dir=worktrees,prefix='.beam-compress-gitapply-') as temporary:
        target=Path(temporary)/PATH;target.parent.mkdir(parents=True);target.write_bytes(fixed_bytes)
        argv=['git','apply','--whitespace=nowarn',str(output/'official-reference/official.src.patch')]
        result=subprocess.run(argv,cwd=temporary,capture_output=True,timeout=60)
        (output/'git-apply.stdout.log').write_bytes(result.stdout);(output/'git-apply.stderr.log').write_bytes(result.stderr)
        reference.require(result.returncode==0,'Native derivation reproduction failed');native_bytes=target.read_bytes()
    benchmark_bytes=(output/'official-reference/expected-buggy'/PATH).read_bytes()
    reference.require(hashlib.sha256(native_bytes).hexdigest()==native_sources[PATH],'Git apply does not reproduce sealed native bytes')
    reference.require(native_bytes!=benchmark_bytes and native_bytes.replace(b'\r\n',b'\n')==benchmark_bytes,'Difference is not exclusively CRLF versus LF')
    (output/'native-git-apply.java').write_bytes(native_bytes);(output/'benchmark-gnu-patch.java').write_bytes(benchmark_bytes)
    data={'status':'prospective_exact_benchmark_binding','sealed_at_utc':datetime.now(timezone.utc).isoformat(),'generation_condition':seal['condition'],'execution_condition':'beam-compress-d4j-gnu-patch-benchmark-counted-development-v2','aom_commit':seal['aom_commit'],'native_sources_sha256':native_sources,'expected_benchmark_sources_sha256':benchmark,'different_paths':changed,'difference':'CRLF versus LF only, independently reproduced Git apply and GNU patch; equality after newline normalization is diagnostic only, actual checkout guard still requires exact SHA-256','native_buggy_sha256':native_sources[PATH],'benchmark_buggy_sha256':benchmark[PATH],'native_crlf_count':native_bytes.count(b'\r\n'),'benchmark_crlf_count':benchmark_bytes.count(b'\r\n'),'derivation_sha256':reference.sha(output/'official-reference/derivation.json'),'strict_attempt_retained':'output/api854-20261004/beam-champ-compress-d4j-v1','native_manifest_sha256':reference.sha(native/'checksums.json'),'actual_production_replacement':False,'suite_or_assertion_changes':False,'kku_requests':0,'queue_mutations':0,'primary':False}
    reference.write(output/'binding.json',data)
    reference.write(output/'checksums.json',{p.relative_to(output).as_posix():reference.sha(p) for p in sorted(output.rglob('*')) if p.is_file()})
    print(json.dumps({'status':data['status'],'native_buggy_sha256':data['native_buggy_sha256'],'benchmark_buggy_sha256':data['benchmark_buggy_sha256'],'native_crlf_count':data['native_crlf_count'],'benchmark_crlf_count':data['benchmark_crlf_count']}))
if __name__=='__main__':
    parser=argparse.ArgumentParser();parser.add_argument('--intake',type=Path,required=True);parser.add_argument('--output',type=Path,required=True);args=parser.parse_args();run(args.intake,args.output)
