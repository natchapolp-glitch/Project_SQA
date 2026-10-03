"""Receive exact peer Git blobs and review immutable candidate/input evidence.

Run once from the repository root. No peer scripts, queue or provider are run.
"""
from pathlib import Path
from datetime import datetime, timezone
import json
import subprocess
import sys
import unittest
import xml.etree.ElementTree as ET

ROOT = Path.cwd()
sys.path.insert(0,str(ROOT))
from scripts.study.api854.common import read_json,write_json,sha256,implementation_hashes

BASE = ROOT/'output/api854-20261003/beam-v8-received-v1'
CHAMP = 'e742095dcddf795b4d575a611b5f844da9977388'


def binding(path):
    path=Path(path)
    return {'path':path.relative_to(ROOT).as_posix(),'sha256':sha256(path)}


def main():
    template=ROOT/'output/api854-20261003/aom-v8-waiting-work-v1/return-templates/beam-v8-review.template.json'
    original=read_json(template)['input_binding']
    for key in ('protocol','preparation_index','runner_plan','prompt_worksheet'):
        assert sha256(ROOT/original[key]['path'])==original[key]['sha256'],key
    current=implementation_hashes()
    changed=[{'path':p,'requested_sha256':h,'current_sha256':current.get(p)}
        for p,h in original['runtime_source_sha256'].items() if current.get(p)!=h]
    write_json(BASE/'runtime-diff.json',{'requested_input_binding':original,
        'reviewed_runtime_commit':subprocess.check_output(['git','rev-parse','HEAD'],text=True).strip(),
        'runtime_source_files':len(current),'changed':changed,
        'old_runtime_binding_verdict':'stale_on_beam' if changed else 'match',
        'resolution':'New preparation and protocol in beam-v8-received-v1; historical files unchanged',
        'current_runtime_source_sha256':current,'producer_sha256':sha256(__file__)})
    peer=BASE/'peer-evidence';peer.mkdir(exist_ok=False)
    paths=[
        'output/api854-provider-preflight-20261003/champ-beam532-intake-v1.json',
        'output/api854-20261003/aom-beam532baa31-readiness-audit-v2.json',
        'output/api854-20261003/aom-v9-recipe-runtime-verification-v1.json',
        'docs/api854/AOM_CHAMP_V9_INTEGRATION_TH.md',
        'output/api854-20261003/enum-boundary-development-v3/receipt.json']
    provenance=[]
    for path in paths:
        raw=subprocess.check_output(['git','show',CHAMP+':'+path])
        dest=peer/Path(path).name
        dest.write_bytes(raw)
        provenance.append({'source_commit':CHAMP,'source_path':path,**binding(dest)})
    write_json(peer/'provenance.json',provenance)
    peer_audit=read_json(peer/'aom-beam532baa31-readiness-audit-v2.json')
    assert peer_audit['status']=='pass'
    candidate_rows=[]
    specs=[('setter','Codec','beam-v7-setter-recipe-20261003',4,
        {'class':'org.apache.commons.codec.language.Metaphone','constructor_types':'','method':'setMaxCodeLen','parameter_types':'int'},
        'add_unsupported_signature',
        'Fresh production Metaphone(); nonnegative integer max code length. Bounded proof uses 0,1,4,8; deterministic architecture input. Negative limits require separate boundary review.',
        'Call setter, assert getMaxCodeLen()==limit; encode architecture, assert output length<=limit and getter state unchanged. Shared recipe additionally compares actual encoded output.'),
        ('jdom','JxPath','beam-v7-jdom-oracle-20261003',2,
        {'class':'org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer','constructor_types':'java.lang.Object,java.util.Locale',
        'method':'attributeIterator','parameter_types':'org.apache.commons.jxpath.ri.QName'},
        'repair_existing_selected_recipe',
        'Real production JDOM pointer over attached root/item tree, Locale and coherent QName; item has id=left/right and alpha/beta text. Valid attribute iterator; no fabricated Attribute stub.',
        'Iterate returned pointers; project production Attribute getName/getNamespaceURI/getValue, iterator order and attached XML state. Observe id, empty namespace and left/right values; setup/projection failure is fixture_error.')]
    integrity=[]
    for key,project,name,count,target,change,preconditions,oracle in specs:
        packet=ROOT/'docs/api854/evidence'/name
        checks=read_json(packet/'checksums.json')
        for rel,h in checks.items(): assert sha256(packet/rel)==h,(name,rel)
        receipt=read_json(packet/'receipt.json')
        assert receipt['status']=='complete' and receipt['fault_detected'] is False
        assert sha256(packet/'package/suite.tar.bz2')==receipt['suite_sha256']
        assert sha256(packet/'evaluation/record.json')==receipt['result_sha256']
        counters={};commands={}
        for stage in ('fixed-1','fixed-2','buggy','coverage'):
            c=read_json(packet/'evaluation'/stage/'sqa-stage-counts.json')
            assert c['executed']==c['target_checks']==count and c['skipped']==0
            cmd=read_json(packet/'evaluation'/stage/'command.json')
            assert cmd['exit_code']==0 and not cmd['timed_out']
            counters[stage]=c;commands[stage]=binding(packet/'evaluation'/stage/'command.json')
        xml=ET.parse(packet/'evaluation/coverage/coverage.xml').getroot()
        covered=any(int(l.get('hits','0'))>0 for c in xml.iter('class') if c.get('name')==target['class']
            for m in c.findall('./methods/method') if m.get('name')==target['method']
            and m.get('signature')==peer_audit[key]['coverage_target']['signature'] for l in m.findall('./lines/line'))
        assert covered
        source=next(e for e in read_json(BASE/f'preparation/{project}-1/context-manifest.json')['source_files']
            if e['path'].endswith('/'+target['class'].rsplit('.',1)[1]+'.java'))
        if key=='setter': assert source['sha256']==receipt['fixed_source_sha256']
        recipe=packet/('sources/org/apache/commons/codec/language/MetaphoneSetterRecipeTest.java' if key=='setter' else 'helper-src/algorithms/java/SqaProbe.java')
        observations=(read_json(packet/'observations.json') if key=='jdom' else None)
        if observations:
            assert all(c['fixed_first']==c['fixed_second'] and c['fixed_first']['status']=='ok' and c['fixed_first']['target_invoked'] for c in observations)
        candidate_rows.append({'project':project,'bug_id':1,**target,'change_kind':change,
            'normal_domain_preconditions':preconditions,'fixture_recipe_path':binding(recipe)['path'],
            'fixture_recipe_sha256':sha256(recipe),'oracle_rule':oracle,'fixed_source_sha256':source['sha256'],
            'supplemental_source_sha256':{},'setup_failure_separation':'Constructor/dependency/projection failure is fixture_error and is never counted as target exception or skipped-success; target must actually be invoked.',
            'fixed_repeated_observations':observations if observations else {'scope':'Historical unchanged JUnit getter/state suite repeated twice; newer peer shared-runtime limit 0/1/4/8 observations are retained separately',
                'peer_evidence':binding(peer/'aom-v9-recipe-runtime-verification-v1.json')},
            'fixed_twice_suite_result_hashes':[commands[s] for s in ('fixed-1','fixed-2')],
            'buggy_result_hash':commands['buggy'],'target_coverage_result_hash':binding(packet/'evaluation/coverage/coverage.xml'),
            'executed_skipped_target_checks':counters,'suite_sha256':receipt['suite_sha256'],
            'beam_verdict':'accepted_for_bounded_development_recipe_review',
            'champ_verdict':None,'champ_evidence_verdict':'Historical proof integrity accepted; final shared approval explicitly not granted by Champ intake receipt',
            'aom_newer_v9_composition_evidence':binding(peer/'AOM_CHAMP_V9_INTEGRATION_TH.md'),
            'no_new_candidate_defects4j_execution':True,'fault_detected':False})
        integrity.append({'packet':name,'files_checked':len(checks),'checksums':binding(packet/'checksums.json'),'target_method_covered':covered})
    joint=read_json(template.parent/'joint-candidate-acceptance.template.json')
    joint.update(example_only=False,checked_at_utc=datetime.now(timezone.utc).isoformat(),
        reviewer_commit=subprocess.check_output(['git','rev-parse','HEAD'],text=True).strip(),
        review_status='beam_review_complete_champ_final_verdict_pending',accepted_candidates=[],reviewed_candidates=candidate_rows,
        reviewers={'beam':'bounded_candidate_recipe_review_completed','champ':None},
        evidence=[binding(peer/'provenance.json')],packet_integrity=integrity,
        joint_semantic_approval=False,prospective_selection_required=True)
    write_json(BASE/'joint-candidate-review.json',joint)
    # Run the existing equality/rejection tests against the NEW composition.
    from scripts.study.api854.tests import test_preparation_v8_development as tests
    tests.PREP=BASE/'preparation'; tests.PROTOCOL=BASE/'composition/protocol.proposal.json'
    suite=unittest.defaultTestLoader.loadTestsFromTestCase(tests.DevelopmentV8Tests)
    with (BASE/'four-consumer-validation.log').open('w',encoding='utf-8') as stream:
        result=unittest.TextTestRunner(stream=stream,verbosity=2).run(suite)
    assert result.wasSuccessful() and not result.skipped
    write_json(BASE/'four-consumer-validation.json',{'tests_run':result.testsRun,'failures':len(result.failures),
        'errors':len(result.errors),'skipped':len(result.skipped),'passed':result.wasSuccessful(),
        'consumer_combinations':80,'preparation_index':binding(BASE/'preparation/index.json'),
        'protocol':binding(BASE/'composition/protocol.proposal.json'),'test_source':binding(Path(tests.__file__)),
        'log':binding(BASE/'four-consumer-validation.log'),'producer_sha256':sha256(__file__),
        'no_provider_or_queue_calls':True})
    print(json.dumps({'runtime_changes':len(changed),'candidate_integrity':integrity,'fresh_consumer_tests':result.testsRun}))


if __name__=='__main__':
    main()
