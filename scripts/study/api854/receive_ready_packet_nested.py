"""Read-only Git intake of one pinned ready-results packet including nested checksum files, with complete SHA checks."""
import argparse,hashlib,io,json,subprocess,tarfile
from pathlib import Path

ROOT=Path(__file__).resolve().parents[3]
DEFAULT_COMMIT='a4a38a5e'
PREFIX='output/api854-20261004/'
DEFAULT_PATHS=[PREFIX+'champ-csv-development-generation-v1',PREFIX+'champ-csv-native-measurement-v4',PREFIX+'champ-csv-four-approach-summary-v1','docs/api854/CHAMP_24H_DELIVERY_PLAN_TH.md','docs/api854/CHAMP_READY_RESULTS_FIRST_TH.md']
def sha(path):return hashlib.sha256(Path(path).read_bytes()).hexdigest()
def write(path,value):
    with Path(path).open('x',encoding='utf-8',newline='\n') as stream:json.dump(value,stream,ensure_ascii=False,indent=2);stream.write('\n')
def require(ok,message):
    if not ok:raise ValueError(message)
def git(*args):return subprocess.check_output(['git','-c','safe.directory='+str(ROOT),'-c','core.autocrlf=false','-C',str(ROOT),*args],timeout=180)
def run(commit,output,paths):
    commit=git('rev-parse',commit+'^{commit}').decode().strip();output=output.resolve()
    require(output.is_relative_to(ROOT/'output') and not output.exists(),'New contained intake output required')
    raw=git('archive','--format=tar',commit,*paths);output.mkdir(parents=True,exist_ok=False)
    received=output/'received-champ';received.mkdir()
    inventories={}
    with tarfile.open(fileobj=io.BytesIO(raw)) as archive:
        for member in archive:
            if member.isdir():continue
            require(member.isfile() and not member.issym() and not member.islnk(),'Unexpected archive entry')
            dst=(received/member.name).resolve();require(dst.is_relative_to(received),'Unsafe archive path')
            require('.private.' not in member.name and not member.name.startswith('.local/'),'Private file rejected')
            dst.parent.mkdir(parents=True,exist_ok=True);dst.write_bytes(archive.extractfile(member).read());inventories[member.name]=sha(dst)
    manifests={};entries=0
    for rel in paths:
        folder=received/rel
        if not folder.is_dir():continue
        manifest=json.loads((folder/'checksums.json').read_bytes())
        actual={p.relative_to(folder).as_posix() for p in folder.rglob('*') if p.is_file() and p!=folder/'checksums.json'}
        require(set(manifest)==actual,'Incomplete manifest: '+rel)
        for name,digest in manifest.items():require(sha(folder/name)==digest,'Changed received file: '+rel+'/'+name)
        manifests[rel]={'sha256':sha(folder/'checksums.json'),'entries':len(manifest)};entries+=len(manifest)
    write(output/'receipt.json',{'status':'received_and_sha_verified','source_commit':commit,'paths':paths,'source_sha256':inventories,'git_archive_sha256':hashlib.sha256(raw).hexdigest(),'packet_manifests':manifests,'verified_checksum_entries':entries,'producer_sha256':sha(__file__),'generation_requests':0,'queue_mutations':0,'primary_results_added':0,'all_four_fixed_passed_claimed':False})
    write(output/'checksums.json',{p.relative_to(output).as_posix():sha(p) for p in sorted(output.rglob('*')) if p.is_file()})
    print(json.dumps({'status':'received_and_sha_verified','source_commit':commit,'files':len(inventories),'verified_checksum_entries':entries}))
if __name__=='__main__':
    parser=argparse.ArgumentParser(description=__doc__);parser.add_argument('--commit',default=DEFAULT_COMMIT);parser.add_argument('--output',type=Path,required=True);parser.add_argument('--path',action='append')
    args=parser.parse_args();run(args.commit,args.output,args.path or DEFAULT_PATHS)
