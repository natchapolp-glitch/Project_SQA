"""Record audited independent prerequisites, without advancing primary work."""
import argparse
import json
import re
from pathlib import Path

from .common import ROOT, contained, read_json, sha256, write_json
from .prepare_owner_sources import now


def checkpoint(base, receipt):
    index_path=base/'aom-owner-sources-final/index.json'
    index=read_json(index_path)
    assert index['owner']=='aom' and index['expected_bugs']==len(index['records'])==284
    assert len({(r['project'],r['bug_id']) for r in index['records']})==284
    assert index['source_prepared']==284 and index['compile_statuses']=={'passed':284}
    assert index['source_preparation_attempts']==287
    assert index['source_recovery_count']==2 and index['interrupted_source_attempts']==1
    for field in ('primary_completed','test_methods_generated','real_kku_requests','live_queue_mutations'):
        assert index[field]==0
    audit=read_json(base/'aom-owner-sources-final/audit.json')
    assert audit['index_sha256']==sha256(index_path)
    assert audit['original']['all_owner_bugs_attempted']
    for linked in index['source_indices']:
        assert sha256(contained(base,linked['path']))==linked['sha256']
    for history in index['retained_failure_history']:
        for key in ('original_record','recovery_record'):
            assert sha256(contained(base,history[key]))==history[key+'_sha256']
    for history in index['retained_interruption_history']:
        path=contained(base,history['path'])
        assert sha256(path)==history['sha256']
        interruption=read_json(path)
        assert interruption['status']=='interrupted_exit_unknown'
        for relative,expected in read_json(path.parent/'checksums.json').items():
            assert sha256(contained(path.parent,relative))==expected
        for relative,expected in interruption['original_evidence_sha256'].items():
            assert sha256(contained(path.parent,relative))==expected
    git_bytes=read_json(base/'aom-fixed-git-bytes-audit.json')
    assert git_bytes['source_index_sha256']==sha256(index_path)
    assert git_bytes['bugs_checked']==284 and not git_bytes['problems']
    assert git_bytes['all_java_fixed_tag_bytes_verified']
    assert git_bytes['implementation_sha256']==sha256(ROOT/'scripts/study/api854/audit_fixed_git_bytes.py')
    recovery=read_json(base/'aom-recovery-preparation-v3.json')
    assert recovery['unique_jobs']==4 and recovery['attempts']==2
    for key in ('actual_process_crash','controller_reopened_twice','old_worker_fenced',
                'same_job_reclaimed_with_new_attempt','original_artifact_preserved',
                'resumed_prepare_completed','disabled_generation_not_claimed'):
        assert recovery[key] is True,key
    public=base/'aom-recovery-public-evidence-v3'
    for relative,expected in read_json(public/'checksums.json').items():
        assert sha256(contained(public,relative))==expected
    evidence=read_json(public/'evidence.json')
    assert len(evidence['jobs'])==4 and len(evidence['attempts'])==2
    assert evidence['original_receipt_sha256']==sha256(base/'aom-recovery-preparation-v3.json')
    for artifact in evidence['artifacts']:
        path=contained(public,artifact['public_path'])
        assert sha256(path)==artifact['sha256'] and path.stat().st_size==artifact['size']
    units=read_json(base/'aom-independent-unit-validation-v2.json')
    assert units['exit_code']==0
    count=int(re.search(r'Ran (\d+) tests',units['stderr']).group(1))
    assert count==15
    for module,expected in units['test_file_sha256'].items():
        assert sha256(ROOT/(module.replace('.','/')+'.py'))==expected
    ui=read_json(base/'aom-progress-final-ui-review.json')
    assert ui['source_index_sha256']==sha256(index_path)
    assert ui['source_prepared']==ui['compile_passed']==284 and ui['primary_completed']==0
    assert ui['failed_filter_count']==0 and ui['prepared_filter_count']==284
    assert ui['gson18_record_href']=='aom-owner-sources-v2/Gson-18/record.json'
    assert ui['time25_record_href']=='aom-owner-sources-v3/Time-25/record.json'
    assert ui['console_errors']==[]
    assert ui['screenshot_sha256']==sha256(base/'aom-owner-progress-final.png')
    assert sha256(index_path) in (base/'aom-owner-progress.html').read_text(encoding='utf-8')
    files=['aom-owner-sources-final/index.json','aom-owner-sources-final/audit.json',
        'aom-fixed-git-bytes-audit.json','gson18-selection-recovery-proof.json','time25-selection-recovery-proof.json',
        'aom-independent-unit-validation-v2.json','aom-recovery-preparation-v3.json',
        'aom-recovery-public-evidence-v3/evidence.json','aom-progress-final-ui-review.json',
        'aom-owner-progress.html','aom-owner-progress-final.png',
        'AOM_SQA_854_Preparation_DRAFT_20261003.pptx']
    result={'updated_at_utc':now(),'scope':'Completed independent source/build prerequisites and offline preparation only.',
        'owner':'aom','unique_owned_bugs':284,'source_prepared':284,'fixed_compile_passed':284,
        'source_preparation_attempts':287,'preserved_original_source_failure':2,'preserved_interrupted_attempt':1,
        'fixed_git_bytes_verified_bugs':git_bytes['bugs_checked'],
        'selected_files_checked':git_bytes['selected_files_checked'],
        'untracked_build_snapshots':git_bytes['untracked_build_snapshots'],
        'offline_unit_tests_passed':count,'offline_recovery_unique_jobs':4,'offline_recovery_attempts':2,
        'cross_machine_recovery_tested':False,'primary_completed':0,'test_methods_generated':0,
        'real_kku_requests':0,'live_queue_mutations':0,'shared_generation_packet_accepted':False,
        'gate_a_approved':False,'report_status':'draft_without_primary_measurements',
        'slides_status':'preparation_draft','demo_status':'runbook_only_no_video',
        'remaining_team_dependencies':['15 pilot fixture reviews','observed API settings/limits/quota/framing/reserve','joint final protocol/runner/Gate A acceptance'],
        'public_file_sha256':{name:sha256(contained(base,name)) for name in files},
        'checkpoint_implementation_sha256':sha256(__file__)}
    write_json(receipt,result)
    return result


if __name__=='__main__':
    parser=argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--base',type=Path,default=ROOT/'output/api854-20261003')
    parser.add_argument('--receipt',type=Path,required=True)
    args=parser.parse_args()
    print(json.dumps(checkpoint(args.base.resolve(),args.receipt.resolve())))
