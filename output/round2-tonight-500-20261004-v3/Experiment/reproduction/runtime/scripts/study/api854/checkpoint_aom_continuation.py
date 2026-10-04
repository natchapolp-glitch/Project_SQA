"""Audit received evidence and emit a new proposal; no provider or queue writes."""
import datetime
import json
import tarfile
from pathlib import Path

from .common import ROOT, read_json, sha256, implementation_hashes, contained
from .inventory import validate_inventory, compare_installed, build_jobs
from .preparation import POLICY_V5, encoded, validate, digest
from .gate_a import route_coverage
from .review_beam_fixture import inspect, packet_checksums


def create():
    out = ROOT/'output/api854-20261003/aom-continuation-v5'
    out.mkdir(parents=True, exist_ok=False)
    def write(name, value):
        (out/name).write_bytes(encoded(value))
    previous = inspect()
    packet = ROOT/'docs/api854/evidence/beam-scalar-fixture-review-20261003'
    file_count = packet_checksums(packet)
    index = read_json(packet/'index.json')
    assert sha256(packet/'protocol-proposal.json') == index['protocol_sha256']
    for name, expected in read_json(packet/'protocol-proposal.json')['source_sha256'].items():
        assert sha256(contained(packet/'runtime-implementation',name)) == expected
    rows=[]
    assert {(r['project'],r['bug_id'],r['approach']) for r in index['records']} == {
        (p,1,a) for p in ('Codec','Collections','Csv') for a in ('cmaes','fscs-art')}
    for row in index['records']:
        folder=packet/f"{row['project']}-{row['bug_id']}-{row['approach']}"
        result=read_json(folder/'original-evaluation-result.json')
        review=read_json(folder/'semantic-review-result.json')
        measure=result['measurement']
        assert result['usable'] is False and result['observed_outcome']=='complete'
        assert review['status']=='valid' and review['evaluation_result_sha256']==sha256(folder/'original-evaluation-result.json')
        assert sha256(folder/'suite.tar.bz2')==row['suite_sha256']==result['suite_sha256']
        with tarfile.open(folder/'suite.tar.bz2','r:bz2') as archive:
            sources=[archive.extractfile(m).read() for m in archive if m.isfile() and m.name.endswith('.java')]
        assert sources==[(folder/'generation/GeneratedStudyTest.java').read_bytes()]
        for stage in ('fixed-1','fixed-2','buggy','coverage'):
            counts=read_json(folder/stage/'sqa-stage-counts.json')
            assert all(counts[k]==v for k,v in {'executed':30,'skipped':0,'target_checks':30}.items())
        assert measure['fixed_validation']=='passed_twice'
        for key in ('fault_detected','line_covered','line_total','branch_covered','branch_total'):
            assert measure[key]==row[key]
        rows.append({k:row[k] for k in ('project','bug_id','approach','suite_sha256','fault_detected','line_covered','line_total','branch_covered','branch_total')})
    ownership=validate_inventory(read_json(ROOT/'experiments/configs/api854-20261003/ownership.json')['bugs'])
    compare_installed(ownership, read_json(ROOT/'output/api854-20261003/aom-installed-current.json')['projects'])
    prep=ROOT/'output/api854-20261003/prepare-v5-five-bug-development'
    prepared=read_json(prep/'index.json')
    assert len(prepared['records'])==5
    verified=0
    for row in prepared['records']:
        folder=prep/f"{row['project']}-{row['bug_id']}"
        for name, expected in read_json(folder/'checksums.json').items():
            assert sha256(contained(folder,name))==expected
            verified+=1
        validate(read_json(folder/'context-manifest.json'),read_json(folder/'prepare-metadata.json'),
            (folder/'prompt.md').read_bytes(),(folder/'targets.json').read_bytes(),(folder/'prepare-policy.json').read_bytes(),
            require_eligible=True,fixture_recipe=read_json(folder/'fixture-recipes.json'))
    env=read_json(ROOT/'output/api854-20261003/aom-current-host-continuation/environment.json')
    assert env['ready']
    beam_host=ROOT/'docs/api854/evidence/beam-one-host-readiness-20261003'
    packet_checksums(beam_host)
    assert read_json(beam_host/'host-receipt.json')['environment_ready']
    plan=read_json(ROOT/'experiments/configs/api854-20261003/runner-plan.beam-one-host.v2.json')
    plan['runners']=[r for r in plan['runners'] if r['worker_id']!='aom-pc2']
    plan.update(state='proposal_pending_team_review',live_workers_started=False,
        scope='One verified current Aom CPU host, one received Beam host, Champ API coordinator; no capacity attributed to unverified Aom second host.')
    routes=route_coverage(plan,{'bugs':ownership})
    assert not routes['gaps']
    write('runner-plan.json',plan)
    protocol=read_json(ROOT/'experiments/configs/api854-20261003/protocol.fixture-development-v4.json')
    protocol.update(condition='api854-20261003-five-bug-development-v5',
        preparation_artifacts=prep.relative_to(ROOT).as_posix(),prepare_policy_sha256=digest(encoded(POLICY_V5)),
        fixture_policy_id=POLICY_V5['fixture_policy'],source_sha256=implementation_hashes(),
        runner_plan_sha256=sha256(out/'runner-plan.json'),enabled_stages=[],
        approval_state='development_proposal_not_frozen',status='development_proposal_pending_team_review',
        state='development_proposal_pending_team_review',
        fixture_development_scope={'bugs':[{'project':r['project'],'bug_id':r['bug_id']} for r in prepared['records']],
            'primary':False,'team_approved':False,'remaining_pilot_bugs_without_recipe_review':15},
        preparation_import_evidence={'path':(prep/'index.json').relative_to(ROOT).as_posix(),'sha256':sha256(prep/'index.json'),'generation_ready':False},
        settings_status='Owner selected Sonnet 5 / Gemini 3.5 Flash Lite; provider settings, limits, framing and observed quota still unverified.')
    protocol['generation'].update(prepare_contract=POLICY_V5['contract'],fixture_policy_id=POLICY_V5['fixture_policy'],prompt_policy_id=POLICY_V5['prompt_policy_id'])
    protocol['gate_a'].update(reviewed_by={'aom':False,'beam':False,'champ':False},
        pending=['15 remaining pilot fixture/semantic reviews','Provider settings/limits/framing and observed quota','Review of final shared condition by team'])
    protocol['aom_host_evidence']={'aom-pc1':'output/api854-20261003/aom-current-host-continuation/environment.json'}
    protocol['fixture_development_proposal']=(out/'protocol.proposal.json').relative_to(ROOT).as_posix()
    protocol['prepare_status']='Five source-bound development inputs; 124 capability-selected targets; no primary adoption.'
    protocol['received_fixture_evidence_scope']={'bugs':5,'suites':10,'primary_approval':False,
        'scalar_packet_checksums_path':(packet/'checksums.json').relative_to(ROOT).as_posix(),
        'scalar_packet_checksums_sha256':sha256(packet/'checksums.json'),
        'scope':'Received original development evidence only; not execution with newly composed runtime.'}
    write('protocol.proposal.json',protocol)
    write('prepare-policy.json',POLICY_V5)
    jobs=build_jobs(ownership,sha256(out/'protocol.proposal.json'))
    write('full-cohort-held-jobs.json',{'jobs':jobs,'dispatched':False,'scope':'854 active bugs × 4 approaches × repeat 1; proposal-bound planning only'})
    write('checkpoint.json',{'checked_at_utc':datetime.datetime.now(datetime.timezone.utc).isoformat(),
        'current_branch':'aom','owner_instructions':{'repeat_index':1,'models':['claude-sonnet-5','gemini-3.5-flash-lite'],'push_branch':'aom'},
        'active_bugs_verified':854,'expected_jobs':len(jobs),'primary_completed':0,
        'historical_results_separate':True,'five_bug_prepare_files_verified':verified,
        'prepared_target_count':prepared['target_count'],'max_prompt_utf8_bytes':prepared['max_prompt_utf8_bytes'],
        'prompt_bytes_are_not_tokens':True,'received_development_suites':previous['development_suites']+rows,
        'received_scalar_packet_files_verified':file_count,'recipe_reviewed_bugs':5,'pilot_recipe_reviews_remaining':15,
        'runner_coverage':routes,'verified_local_environment':env['ready'],
        'gate_a_passed':False,'generation_authorized':False,'real_kku_requests':0,'live_queue_mutations':0,
        'pending':protocol['gate_a']['pending'],
        'validation_scope':'Hash/source/counter/input binding audit of received evidence; no rerun of the ten experiments.'})
    write('checksums.json',{p.relative_to(out).as_posix():sha256(p) for p in sorted(out.rglob('*')) if p.is_file()})
    print(json.dumps({'active_bugs':854,'jobs':len(jobs),'prepared_bugs':5,'targets':prepared['target_count'],'gate_a_passed':False}))


if __name__=='__main__':
    create()
