"""Derive exact benchmark sources from fixed Git bytes and the official D4J patch."""
import csv,hashlib,io,json,shutil,subprocess,tarfile,tempfile
from pathlib import Path
def sha(path):return hashlib.sha256(Path(path).read_bytes()).hexdigest()
def require(ok,message):
    if not ok:raise ValueError(message)
def write(path,value):
    with Path(path).open('x',encoding='utf-8',newline='\n') as stream:json.dump(value,stream,ensure_ascii=False,indent=2);stream.write('\n')
def derive(project,bug_id,repository,source_root,native_seal,d4j,output,scratch_root):
    d4j=Path(d4j).resolve();output=Path(output).resolve();scratch_root=Path(scratch_root).resolve()
    require(project=='Jsoup' and bug_id==1 and repository=='jsoup.git' and source_root=='src/main/java','Only sealed Jsoup-1 mapping is supported by this producer')
    patch=d4j/f'framework/projects/{project}/patches/{bug_id}.src.patch'
    metadata=d4j/f'framework/projects/{project}/active-bugs.csv';raw_patch=patch.read_bytes()
    git=['git','-C',str(d4j)];framework_commit=subprocess.check_output(git+['rev-parse','HEAD']).decode().strip()
    require(raw_patch==subprocess.check_output(git+['show','HEAD:'+patch.relative_to(d4j).as_posix()]),'Official patch changed from framework Git blob')
    fixed=native_seal['production_source_sha256']['fixed']['sources']
    text=raw_patch.decode()
    patch_headers=[line for line in text.splitlines() if line.startswith('diff --git ')]
    require(patch_headers and all(len(line.split())==4 and line.split()[2][2:] in fixed and line.split()[3][2:]==line.split()[2][2:] for line in patch_headers),'Official patch outside fixed source inventory')
    with metadata.open() as stream:record=next(r for r in csv.DictReader(stream) if r['bug.id']==str(bug_id))
    require(record['revision.id.fixed']==native_seal['exact_production_revisions']['fixed'] and record['revision.id.buggy']==native_seal['exact_production_revisions']['buggy'],'Active bug metadata differs')
    output.mkdir(exist_ok=False);(output/'official.src.patch').write_bytes(raw_patch);(output/'active-bugs.csv').write_bytes(metadata.read_bytes())
    repo=d4j/'project_repos'/repository
    archive=subprocess.check_output(['git','-C',str(repo),'archive',record['revision.id.fixed'],source_root])
    require(hashlib.sha256(archive).hexdigest()==native_seal['production_source_sha256']['fixed']['archive_sha256'],'Fixed production archive differs')
    reference=output/'expected-buggy';reference.mkdir()
    with tarfile.open(fileobj=io.BytesIO(archive)) as tar:
        for member in tar:
            if not member.isfile():continue
            path=(reference/member.name).resolve();require(path.is_relative_to(reference),'Unsafe source archive path');path.parent.mkdir(parents=True,exist_ok=True);path.write_bytes(tar.extractfile(member).read())
    for name,digest in fixed.items():require(sha(reference/name)==digest,'Fixed reference source differs')
    command=['patch','--batch','--fuzz=0','-p1','-i',str(output/'official.src.patch')]
    with tempfile.TemporaryDirectory(dir=scratch_root,prefix='.beam-jsoup-reference-') as temporary:
        scratch=Path(temporary).resolve();require(scratch.is_relative_to(scratch_root),'Unsafe Linux scratch')
        for name in fixed:
            dst=scratch/name;dst.parent.mkdir(parents=True,exist_ok=True);shutil.copyfile(reference/name,dst)
        result=subprocess.run(command,cwd=scratch,capture_output=True,timeout=60)
        (output/'patch.stdout.log').write_bytes(result.stdout);(output/'patch.stderr.log').write_bytes(result.stderr)
        require(result.returncode==0,'Official isolated-bug patch failed')
        for name in fixed:shutil.copyfile(scratch/name,reference/name)
    expected={p.relative_to(reference).as_posix():sha(p) for p in sorted(reference.rglob('*.java'))}
    require(set(expected)==set(fixed),'Patched production source inventory changed')
    write(output/'derivation.json',{'project':project,'bug_id':bug_id,'framework_commit':framework_commit,'official_patch_git_blob_verified':True,'official_patch_sha256':sha(output/'official.src.patch'),'active_bug_metadata_sha256':sha(output/'active-bugs.csv'),'active_bug_record':record,'fixed_archive_sha256':hashlib.sha256(archive).hexdigest(),'patch_command':command,'patch_exit_code':result.returncode,'expected_isolated_buggy_source_sha256':expected,'native_and_isolated_buggy_sources_identical':expected==native_seal['production_source_sha256']['buggy']['sources'],'actual_production_source_replacement':False,'producer_sha256':sha(__file__)})
    return expected
