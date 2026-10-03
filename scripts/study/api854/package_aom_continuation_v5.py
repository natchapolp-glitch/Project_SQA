"""Finalize receipts and package the offline continuation without credentials."""
import hashlib
import json
import zipfile
from pathlib import Path
from .common import ROOT, read_json, sha256, implementation_hashes
from .inventory import build_jobs
from .preparation import encoded


def main():
    out=ROOT/'output/api854-20261003/aom-continuation-v5'
    new=ROOT/'output/api854-20261003/prepare-v5-five-bug-development'
    rebuilt=ROOT/'tmp/aom-prepare-v5-wsl'
    paths=lambda p:{f.relative_to(p).as_posix() for f in p.rglob('*') if f.is_file()}
    assert paths(new)==paths(rebuilt)
    for name in paths(new):
        assert (new/name).read_bytes()==(rebuilt/name).read_bytes(),name
    validation={'windows_api854':{'tests':250,'passed':249,'skipped':1,'failures':0,'errors':0,
        'command':'python -X utf8 -m unittest discover -s scripts/study/api854/tests -t .',
        'skip_reason':'Windows source symlink privilege'},
        'wsl_focused':{'tests':35,'passed':35,'skipped':0,'log_sha256':sha256(out/'wsl-validation.txt'),
            'command':'PYTHONPATH=algorithms/python python3 -m unittest scripts.study.tests.test_generate scripts.study.tests.test_evaluate scripts.study.api854.tests.test_preparation_v5 scripts.study.api854.tests.test_preparation_v4'},
        'cross_platform_prepare_files_identical':len(paths(new)),
        'scope':'Queue/provider mocks and local regression checks; not live KKU or primary experiments.'}
    (out/'validation.json').write_bytes(encoded(validation))
    protocol=read_json(out/'protocol.proposal.json')
    original=read_json(ROOT/'experiments/configs/api854-20261003/protocol.fixture-development-v4.json')
    protocol['beam_fixture_review']=original['beam_fixture_review']
    packet=ROOT/'docs/api854/evidence/beam-scalar-fixture-review-20261003'
    protocol['received_fixture_evidence_scope']={'bugs':5,'suites':10,'primary_approval':False,
        'scalar_packet_checksums_path':(packet/'checksums.json').relative_to(ROOT).as_posix(),
        'scalar_packet_checksums_sha256':sha256(packet/'checksums.json'),
        'scope':'Received original development evidence only; not execution with newly composed runtime.'}
    assert protocol['source_sha256']==implementation_hashes()
    (out/'protocol.proposal.json').write_bytes(encoded(protocol))
    held=read_json(out/'full-cohort-held-jobs.json')
    held['jobs']=build_jobs(read_json(ROOT/'experiments/configs/api854-20261003/ownership.json')['bugs'],sha256(out/'protocol.proposal.json'))
    assert len(held['jobs'])==len({j['job_id'] for j in held['jobs']})==3416
    (out/'full-cohort-held-jobs.json').write_bytes(encoded(held))
    (out/'checksums.json').write_bytes(encoded({p.relative_to(out).as_posix():sha256(p) for p in sorted(out.rglob('*')) if p.is_file() and p.name!='checksums.json'}))
    directories=['docs/api854','experiments/configs/api854-20261003','scripts/study','algorithms',
        'output/api854-20261003/aom-continuation-v5','output/api854-20261003/aom-current-host-continuation',
        'output/api854-20261003/prepare-v3','output/api854-20261003/prepare-v5-five-bug-development']
    files={p for d in directories for p in (ROOT/d).rglob('*') if p.is_file()
        and '__pycache__' not in p.parts and p.suffix not in {'.zip','.pyc'} and not p.name.endswith('.private.json')}
    files.update(ROOT/n for n in ['README.md','.gitignore','.gitattributes','output/api854-20261003/aom-installed-current.json'])
    manifest={p.relative_to(ROOT).as_posix():sha256(p) for p in sorted(files)}
    target=ROOT/'output/api854-20261003/AOM_Continuation_v5_20261003.zip'
    if target.exists():raise ValueError('Do not overwrite an existing delivered package')
    with zipfile.ZipFile(target,'w',compression=zipfile.ZIP_DEFLATED,compresslevel=6) as z:
        for p in sorted(files):z.write(p,'Project_SQA/'+p.relative_to(ROOT).as_posix())
        z.writestr('Project_SQA/CONTINUATION_MANIFEST.json',encoded(manifest))
        z.writestr('Project_SQA/CONTINUATION_SCOPE.txt','Offline preparation and received development evidence only. Primary jobs held. No real KKU requests or live dispatch in this checkpoint. No credentials/private runtime state included.\n')
    with zipfile.ZipFile(target) as z:
        assert z.testzip() is None
        for name, expected in manifest.items():
            assert hashlib.sha256(z.read('Project_SQA/'+name)).hexdigest()==expected
    receipt={'package':target.relative_to(ROOT).as_posix(),'files':len(files),'sha256':sha256(target),
        'bytes':target.stat().st_size,'crc_and_all_hashes_verified':True,'primary_completed':0,'live_requests':0}
    target.with_suffix('.zip.sha256').write_text(receipt['sha256']+'  '+target.name+'\n')
    (target.parent/'AOM_Continuation_v5_20261003.package.json').write_bytes(encoded(receipt))
    print(json.dumps(receipt))


if __name__=='__main__':main()
