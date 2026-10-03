"""Actual worker process crash/reopen rehearsal in a private isolated store."""
import argparse
import hashlib
import json
import os
from pathlib import Path
import subprocess
import sys
import time

from .common import ROOT, sha256
from .queue_server import Store, APIError

CREDENTIALS={'aom':'offline-rehearsal-only-not-a-provider-key-20261003'}


def child(root):
    store=Store(root,CREDENTIALS)
    claim=store.claim({'worker_id':'aom-rehearsal-crash','stage':'prepare','owner':'aom','approaches':['cmaes']},'aom')
    job=claim['job']['job_id']
    artifact=store.upload(job,claim,'rehearsal.txt',b'Offline crash rehearsal only; not an experiment.\n')
    (root/'crashed-claim.private.json').write_text(json.dumps({'claim':claim,'artifact':artifact}))
    os._exit(17)


def run(root, receipt):
    root=root.resolve()
    if not root.is_relative_to(ROOT/'.local/api854'):raise ValueError('Use isolated private state only')
    if root.exists():raise ValueError('Do not reuse a rehearsal store')
    protocol={'state':'offline_rehearsal_not_primary','enabled_stages':['prepare']}
    protocol_hash=hashlib.sha256(json.dumps(protocol,sort_keys=True).encode()).hexdigest()
    store=Store(root,CREDENTIALS)
    store.seed({'bugs':[{'project':'Chart','bug_id':3,'owner':'aom'}]},'offline-owner-recovery',protocol_hash,False,protocol=protocol)
    store.db.close()
    process=subprocess.run([sys.executable,'-m','scripts.study.api854.rehearse_owner_recovery','--child',str(root)],cwd=ROOT,timeout=30,capture_output=True,text=True)
    assert process.returncode==17,(process.returncode,process.stderr)
    stored=json.loads((root/'crashed-claim.private.json').read_text())
    old=stored['claim']
    store=Store(root,CREDENTIALS)
    before=store.status()
    assert len(before['attempts'])==1
    artifact=stored['artifact']
    info=store.db.execute('SELECT * FROM artifacts WHERE artifact_id=?',(artifact['artifact_id'],)).fetchone()
    saved=root/info['path']
    assert sha256(saved)==artifact['sha256']
    # Fault injection: shorten only this isolated preparation lease, without sleeping.
    with store.db:
        store.db.execute('UPDATE jobs SET lease_until=? WHERE job_id=?',(time.time()-1,old['job']['job_id']))
    after=store.status()
    assert next(j for j in after['jobs'] if j['job_id']==old['job']['job_id'])['state']=='queued'
    try:
        store.upload(old['job']['job_id'],old,'late.txt',b'Late stale worker must be fenced')
    except APIError as error:
        assert error.status==409 and error.code=='stale_or_invalid_lease'
    else:raise AssertionError('Stale worker was not fenced')
    fresh=store.claim({'worker_id':'aom-rehearsal-resumed','stage':'prepare','owner':'aom','approaches':['cmaes']},'aom')
    assert fresh['job']['job_id']==old['job']['job_id']
    assert fresh['lease_version']>old['lease_version'] and fresh['attempt_id']!=old['attempt_id']
    uploaded=store.upload(fresh['job']['job_id'],fresh,'rehearsal-resume.txt',b'Offline resumed preparation proof only.\n')
    completed=store.complete(fresh['job']['job_id'],dict(fresh,outcome='prepared',artifact_ids=[uploaded['artifact_id']],metadata={'scope':'offline rehearsal, not experiment'}))
    assert completed['stage']=='generate'
    assert store.claim({'worker_id':'aom-rehearsal','stage':'generate','owner':'aom'},'aom')['job'] is None
    store.db.close()
    restarted=Store(root,CREDENTIALS)
    final=restarted.status()
    assert len(final['jobs'])==4 and len(final['attempts'])==2
    assert sha256(saved)==artifact['sha256']
    assert not any(a['stage']=='generate' for a in final['attempts'])
    restarted.db.close()
    result={'worker_crash_exit':17,'actual_process_crash':True,'controller_reopened_twice':True,
        'lease_expiry_fault_injection':True,'old_worker_fenced':True,'same_job_reclaimed_with_new_attempt':True,
        'original_artifact_preserved':True,'resumed_prepare_completed':True,'disabled_generation_not_claimed':True,
        'unique_jobs':4,'attempts':2,'primary_completed':0,'real_kku_requests':0,'live_queue_mutations':0,
        'cross_machine_tested':False,'scope':'Actual same-host crash/persistence/fencing rehearsal in an isolated private store.',
        'implementation_sha256':sha256(ROOT/'scripts/study/api854/queue_server.py')}
    receipt.parent.mkdir(parents=True,exist_ok=True)
    with receipt.open('x') as stream:stream.write(json.dumps(result,indent=2)+'\n')
    print(json.dumps(result))


if __name__=='__main__':
    parser=argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--child',type=Path)
    parser.add_argument('--root',type=Path)
    parser.add_argument('--receipt',type=Path)
    args=parser.parse_args()
    if args.child:child(args.child)
    else:run(args.root,args.receipt)
