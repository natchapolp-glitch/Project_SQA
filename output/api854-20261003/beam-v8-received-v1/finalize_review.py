"""Seal real Beam evidence and template returns, without editing old results."""
from pathlib import Path
from datetime import datetime,timezone
import shutil
import subprocess
import sys

ROOT=Path.cwd();sys.path[:0]=[str(ROOT),str(ROOT/'scripts/study')]
from scripts.study.api854.common import read_json,write_json,sha256,implementation_hashes
BASE=ROOT/'output/api854-20261003/beam-v8-received-v1'


def binding(p):return {'path':Path(p).relative_to(ROOT).as_posix(),'sha256':sha256(p)}


def main():
    dev=ROOT/'.local/api854/beam-v8-math-development-v1'
    # The existing reviewer intentionally excludes the Math field policy.
    # Keep that scope intact and use the explicit, independently sealed reviewer.
    from math_suite_review import review
    resume='--resume-seal' in sys.argv
    reviews=read_json(dev/'semantic-review-index.json')['reviews'] if resume else review(dev)
    assert len(reviews)==2 and all(r['local_development_usable'] for r in reviews),reviews
    index=read_json(dev/'index.json')
    assert implementation_hashes()==read_json(BASE/'composition/protocol.proposal.json')['source_sha256']
    metadata=read_json(BASE/'preparation/Math-1/prepare-metadata.json')
    fields=('class','constructor_types','method','parameter_types')
    wanted={tuple(t[k] for k in fields) for t in read_json(BASE/'preparation/Math-1/targets.json')['targets']}
    packet=ROOT/'docs/api854/evidence/beam-v8-math-development-20261003-v1'
    if resume:
        for rel,h in read_json(packet/'checksums.json').items():assert sha256(packet/rel)==h,rel
    else:packet.mkdir(exist_ok=False)
    def copy_tree(src,dest):
        for p in sorted(Path(src).rglob('*')):
            if not p.is_file() or '__pycache__' in p.parts or p.suffix in ('.class','.pyc') or p.name.endswith('.lock'):continue
            if p.suffix not in {'.json','.log','.xml','.txt','.java','.py','.md','.bz2'}:continue
            target=dest/p.relative_to(src);target.parent.mkdir(parents=True,exist_ok=True)
            target.write_bytes(p.read_bytes())
    if not resume:copy_tree(dev,packet/'run')
    evidence=[]
    for row in index['records']:
        assert row['evaluation_outcome']=='complete'
        for c in row['stage_counts'].values(): assert c['executed']==c['target_checks']==30 and c['skipped']==0
        generation=Path(row['generation_result']).parent
        setup=read_json(generation/'setup/adapter.json')
        actual={tuple(t[k] for k in fields) for t in read_json(generation/'setup/targets.fixture-policy.json')['targets']}
        assert actual==wanted
        for rel,h in {**metadata['fixed_source_sha256'],**metadata['additional_fixture_source_sha256']}.items():
            assert sha256(Path(setup['fixed_worktree'])/rel)==h
        dest=packet/row['approach']
        if not resume:copy_tree(generation,dest/'generation')
        evaluation=Path(row['evaluation_result']).parent
        if not resume:
            copy_tree(evaluation,dest/'evaluation')
            copy_tree(evaluation.parent/'semantic-review',dest/'semantic-review')
        evidence.append({'approach':row['approach'],'primary':False,'tests':30,
            'same_v8_selected_signatures_and_fixed_context_verified':True,
            'live_shared_queue_execution':False,'fault_detected':row['fault_detected'],
            'generation':binding(dest/'generation/result.json'),'evaluation':binding(dest/'evaluation/result.json'),
            'semantic_review':binding(dest/'semantic-review/review.json'),'stage_counts':row['stage_counts']})
    if not resume:
        (packet/'run_v8_math.py').write_bytes((ROOT/'.local/api854/run_v8_math.py').read_bytes())
        write_json(packet/'checksums.json',{p.relative_to(packet).as_posix():sha256(p) for p in sorted(packet.rglob('*')) if p.is_file()})
    reference=read_json(BASE/'math-reference/receipt.json');assert reference['local_development_valid']
    host=read_json(BASE/'host-receipt.json');assert host['environment_ready']
    host['stage_readiness']={'prepare':'Current fixed checkout/discovery/source/target context verified during both local runs; four-consumer preparation validation passed',
        'fscs-art_generate':'30 unchanged tests generated and evaluated; local development only',
        'cmaes_generate':'30 unchanged tests generated and evaluated; local development only',
        'evaluate':'Both 30-test suites and independent four-test reference measured fixed twice/buggy/coverage',
        'kku_generate':'Assigned to champ-pc1 for all owners; not authorized or attempted here',
        'live_queue_transport':'Not tested or mutated in this review'}
    if resume:assert read_json(BASE/'host-readiness.json')==host
    else:write_json(BASE/'host-readiness.json',host)
    template=ROOT/'output/api854-20261003/aom-v8-waiting-work-v1/return-templates/beam-v8-review.template.json'
    report=read_json(template)
    report.update(example_only=False,review_status='scoped_beam_review_complete_final_condition_pending',
        checked_at_utc=datetime.now(timezone.utc).isoformat(),
        # Windows-managed worktree .git pointers are not Linux Git paths.
        # This is the already verified source/runtime commit, not a guessed SHA.
        reviewer_commit=read_json(BASE/'runtime-diff.json')['reviewed_runtime_commit'],
        actual_condition_binding={'protocol':binding(BASE/'composition/protocol.proposal.json'),
            'preparation_index':binding(BASE/'preparation/index.json'),'runner_plan':binding(BASE/'composition/runner-plan.json'),
            'prompt_worksheet':binding(BASE/'composition-audit/prompt-reserve-worksheet.json'),
            'runtime_source_sha256':implementation_hashes()},
        math_field_composition_verdict='accepted_for_scoped_v8_development: exactly two double/getField() signatures, factories separated from modified-source coverage; four real reference examples pass',
        shared_context_recipe_equality_verdict='passed: all 20 bugs x four consumers, including rejection of omitted factory knowledge; no API request',
        final_condition_semantic_review_verdict=None,
        fixed_buggy_coverage_evidence=evidence+[binding(BASE/'math-reference/receipt.json')],
        candidate_packets=[binding(BASE/'joint-candidate-review.json')],
        selected_targets_with_known_fixture_failures=[{'project':'JxPath','bug_id':1,
            'class':'org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer','constructor_types':'java.lang.Object,java.util.Locale',
            'method':'attributeIterator','parameter_types':'org.apache.commons.jxpath.ri.QName',
            'reason':'V8 Math-only profile preserves historical no-Attribute projection; candidate repair received but not adopted into this condition',
            'evidence':binding(BASE/'peer-evidence/champ-beam532-intake-v1.json')}],
        host={'worker_id':'beam-pc1','cpu_slots':1,'environment_evidence':binding(BASE/'environment/environment.json'),
            'shared_worktrees_root':host['shared_worktrees_root'],'cpu_lock_evidence':binding(BASE/'host-receipt.json'),
            'runner_plan_sha256':host['runner_plan_sha256'],'host_verdict':'passed_for_scoped_local_development; live/team freeze approval pending'},
        enum_joint_decision=None,
        evidence=[binding(BASE/p) for p in ('runtime-diff.json','four-consumer-validation.json','joint-candidate-review.json','enum-review.json','host-readiness.json')]+[binding(packet/'checksums.json')],
        unsupported_review_worklist='312 exclusions remain pending; accepting a candidate recipe does not approve all selected targets or all declarations',
        prompt_reserve={'max_prompt_utf8_bytes':264899,'output_cap':4096,'conservative_request_floor_excluding_framing':268995,
            'framing_overhead':None,'final_reserve':None,'quota_limits_reset_expiry_acceptance':'pending for this exact condition'},
        limitations=['Math reference examples and two bounded algorithm suites are local development evidence; no exhaustive 691-declaration semantic approval.',
            'Original v8 runtime bindings differ in five files; actual return binds new artifacts without modifying originals.',
            'Newer Champ/Aom v9 has 380 targets; Beam cumulative Buffer/Lang profile has 389. Neither count substitutes for this Math-only 379-target condition.',
            'Setter/JDOM need exact Champ scoped verdict and final combined preparation review; empty-enum three-owner decision pending.'],
        real_kku_requests=0,queue_mutations=0,primary_results_added=0)
    write_json(BASE/'beam-v8-review.json',report)
    write_json(BASE/'checksums.json',{p.relative_to(BASE).as_posix():sha256(p) for p in sorted(BASE.rglob('*'))
        if p.is_file() and '__pycache__' not in p.parts and p.suffix not in ('.class','.pyc') and p!=BASE/'checksums.json'})
    print('SEALED Beam v8 review; 379/312; development only; KKU=0; queue mutations=0',flush=True)


if __name__=='__main__':main()
