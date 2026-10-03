"""Join Champ's exact owner worklist to retained Beam fixed-only diagnostics."""
import argparse
from collections import Counter
import json
from pathlib import Path
import re
import shutil
import subprocess
import sys

ROOT = Path(__file__).resolve().parents[4]
sys.path.insert(0,str(ROOT))
from scripts.study.api854.common import sha256, write_json

FIELDS = ('class','constructor_types','method','parameter_types')
key = lambda target: tuple(target[field] for field in FIELDS)
load = lambda path: json.loads(path.read_text(encoding='utf-8-sig'))


def next_review(target, category):
    name, method, parameters = target['class'], target['method'], target['parameter_types']
    if method == 'getField' and name.endswith(('BigFraction','Fraction')):
        return 'fraction_field_structural_oracle', 'Real field singleton: compare runtime type, zero and one fractions; never compare object identity text.'
    if '[' in parameters and name.endswith(('NumberInput','TextBuffer','CpioArchiveOutputStream')):
        return 'coherent_buffer_slice', 'Construct the buffer first; derive offset/length within its bounds, numeric digits/text or matching archive entry size; compare returned content/post-state.'
    if name.startswith('org.joda.time.'):
        return 'coherent_time_graph_and_index', 'UTC production chronology and explicit instant/partial/period fixtures; derive indices from actual field inventory; compare field/value/chronology semantics.'
    if name.startswith('org.jfree.'):
        if 'Graphics2D' in parameters:
            return 'chart_graphics_plot_recipe', 'Real BufferedImage/Graphics2D with coherent dataset/plot/axes/info/markers; define geometry or rendered-state invariants prospectively.'
        return 'chart_receiver_state_oracle', 'Nonempty concrete dataset and configured generator/annotation fixtures; getters must exercise values and mutators need annotation/generator state snapshots.'
    if name.endswith('StringCollectionDeserializer'):
        return 'deserializer_context_graph', 'One production ObjectMapper parser/context/property graph; project actual deserializer/value-instantiator semantics, not object-type alone.'
    if name.endswith('TextBuffer'):
        return 'initialized_text_buffer_state', 'Allocate/reset valid storage before length/segment operations; inspect contents and size, distinguish invalid bounds from normal execution.'
    if name.endswith('CpioArchiveOutputStream'):
        return 'archive_format_and_entry_graph', 'Use documented format constant with matching entry/header/size; evaluate real written bytes and boundary exceptions separately.'
    if method in ('<init>','hashCode','equals','clone'):
        return 'construction_equality_identity_review', 'Fresh production receiver; verify structural state and equality/hash/clone invariants instead of one stable identity or constructed marker.'
    return 'method_preconditions_and_state_review', 'Review fixed declaration preconditions and returned/mutated state; repeated midpoint observation alone does not certify this target.'


def run(champ, document):
    base = Path(__file__).parent
    received = base / 'received'
    received.mkdir(exist_ok=False)
    provider = champ / 'output/api854-provider-preflight-20261003'
    prep = champ / 'output/api854-20261003/champ-prepare-v7-current-v1'
    beam_prep = ROOT / 'output/api854-20261003/prepare-v7-twenty-bug-development'
    files = {'worklist.json':provider / 'champ-v7-owner-worklist-v1.json',
             'input-audit.json':provider / 'champ-v7-input-audit-v1.json',
             'preparation-index.json':prep / 'index.json',
             'protocol.json':provider / 'champ-v7-current-proposal-v1/protocol.proposal.json',
             'runner-plan.json':provider / 'champ-v7-current-proposal-v1/runner-plan.json',
             'CHAMP_V7_INTAKE_TH.md':document}
    for name, source in files.items():
        shutil.copyfile(source,received / name)
        assert sha256(source) == sha256(received / name)
    worklist, audit, index = (load(received / name) for name in ('worklist.json','input-audit.json','preparation-index.json'))
    assert worklist['source_audit_sha256'] == sha256(received / 'input-audit.json')
    assert audit['preparation_index_sha256'] == sha256(received / 'preparation-index.json')
    assert audit['protocol_sha256'] == sha256(received / 'protocol.json')
    assert worklist['owner'] == 'champ' and worklist['unsupported_count'] == 169
    assert index['target_count'] == 377 and index['capability_exclusion_count'] == 314
    assert index['required_common_declarations'] == 691 and not index['generation_ready']
    assert not audit['generation_authorized']
    checksums, matched_payloads = 0, []
    beam_index = load(beam_prep / 'index.json')
    beam_rows = {(r['project'],r['bug_id']):r for r in beam_index['records']}
    runtime_differences = {name:{'beam':beam_index['runtime_source_sha256'].get(name),'champ':digest}
        for name,digest in index['runtime_source_sha256'].items() if beam_index['runtime_source_sha256'].get(name) != digest}
    for row in index['records']:
        identity = (row['project'],row['bug_id'])
        previous = beam_rows[identity]
        assert row['owner'] == previous['owner']
        name = f'{identity[0]}-{identity[1]}'
        folder = prep / name
        for relative,digest in load(folder / 'checksums.json').items():
            path = (folder / relative).resolve(strict=True)
            assert path.is_relative_to(folder.resolve()) and sha256(path) == digest
            checksums += 1
        payloads = ('prompt.md','context.md','fixture-recipes.json','targets.json','capability-exclusions.json')
        hashes = {}
        for payload in payloads:
            assert (folder / payload).read_bytes() == (beam_prep / name / payload).read_bytes(), (name,payload)
            hashes[payload] = sha256(folder / payload)
        for source in load(folder / 'context-manifest.json')['source_files']:
            p = folder / 'fixed-source' / source['path']
            assert sha256(p) == source['sha256']
            assert p.read_bytes() == (beam_prep / name / 'fixed-source' / source['path']).read_bytes()
        matched_payloads.append({'project':identity[0],'bug_id':identity[1],'payload_sha256':hashes,
            'source_payloads_match':True,'runtime_approval_inherited':False})
    assert len(matched_payloads) == 20 and checksums == audit['checksum_entries_verified']
    review, bug_counts, seen, categories, families = [], [], set(), Counter(), Counter()
    sweep = ROOT / 'docs/api854/evidence/beam-v7-fixed-sweep-20261003'
    for bug in worklist['bugs']:
        name = f"{bug['project']}-{bug['bug_id']}"
        partition = load(prep / name / 'capability-exclusions.json')
        exclusions = {key(r['target']):r['reason'] for r in partition['excluded']}
        assert bug['owner'] == 'champ'
        assert {key(r['target']):r['reason'] for r in bug['unsupported_targets']} == exclusions
        original = load(sweep / name / 'record.json')
        cases = {key(c['target']):(n,c) for n,c in enumerate(original['cases'])}
        source_paths = list((prep / name / 'fixed-source').rglob('*.java'))
        counts = Counter()
        for requested in bug['unsupported_targets']:
            target = requested['target']
            identity = (bug['project'],bug['bug_id'],*key(target))
            assert identity not in seen
            seen.add(identity)
            n, case = cases[key(target)]
            assert case['v7_capability'] == 'unsupported' and case['exclusion_reason'] == requested['reason']
            first = case['first']
            category = first['status'] if first['status'] != 'ok' else 'repeat_unstable' if not case['repeat_equal'] else 'target_exception' if first['outcome'].startswith('exception:') else 'normal_observation_oracle_pending'
            family, action = next_review(target,category)
            anchors = []
            method = target['class'].rsplit('.',1)[-1].split('$')[-1] if target['method'] == '<init>' else target['method']
            pattern = re.compile(r'\b(?:public|protected|private)\b[^;{}]*\b'+re.escape(method)+r'\s*\(')
            for source in source_paths:
                text = source.read_text(encoding='utf-8')
                for match in pattern.finditer(text):
                    anchors.append({'path':source.relative_to(prep / name).as_posix(),'line':text[:match.start()].count('\n')+1,
                        'source_sha256':sha256(source),'scope':'method-name declaration candidate; overload/body review still required'})
            path = sweep / name / f'case-{n:03d}.json'
            result = {'project':bug['project'],'bug_id':bug['bug_id'],'owner':'champ','target':target,
                'existing_exclusion':requested['reason'],'diagnostic_category':category,
                'diagnostic_sha256':sha256(path),'diagnostic_path':path.relative_to(ROOT).as_posix(),
                'historical_runtime_condition_preserved':True,'source_declaration_candidates':anchors,
                'next_review_family':family,'prospective_review_action':action,'oracle_approved':False,
                'target_invocation_certified':first.get('target_invoked') is True and first['status']=='ok',
                'midpoint_outcome':first.get('outcome'),'fixture_failure':first.get('reason')}
            review.append(result)
            counts[category] += 1
            categories[category] += 1
            families[family] += 1
        assert sum(counts.values()) == bug['unsupported']
        bug_counts.append({'project':bug['project'],'bug_id':bug['bug_id'],'unsupported':bug['unsupported'],
            'diagnostic_counts':dict(counts),'joint_semantic_approval':False})
    assert len(review) == 169 and len(seen) == 169
    assert dict(categories) == {'fixture_error':62,'normal_observation_oracle_pending':76,'target_exception':31}
    receipt = {'scope':'Beam receipt and source/payload consistency review of Champ owner worklist; no approval.',
        'champ_local_commit':subprocess.check_output(['git','-c','safe.directory=*','-C',str(champ),'rev-parse','HEAD'],text=True).strip(),
        'source_kind':'Existing local Champ worktree checkpoint; not asserted to be origin/champ',
        'received_sha256':{name:sha256(received / name) for name in files},
        'reviewer_sha256':sha256(__file__),'preparation_files_checked':checksums,
        'payload_comparison':matched_payloads,'runtime_source_differences':runtime_differences,
        'reviewed_worklist_entries':169,'diagnostic_counts':dict(categories),'review_family_counts':dict(families),
        'bugs':bug_counts,'declarations':review,'required_common_declarations':691,
        'shared_supported':377,'shared_unsupported':314,'capability_changes':0,
        'primary_completed':0,'gate_a_passed':False,'joint_semantic_approval':False,'real_kku_requests':0,'queue_mutations':0}
    write_json(base / 'review.json',receipt)
    write_json(base / 'checksums.json',{p.relative_to(base).as_posix():sha256(p) for p in base.rglob('*') if p.is_file() and '__pycache__' not in p.parts})
    print(json.dumps({'reviewed':169,'checksums':checksums,'categories':dict(categories),'runtime_differences':list(runtime_differences)}))


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--champ-root',type=Path,required=True)
    parser.add_argument('--document',type=Path,required=True)
    args = parser.parse_args()
    run(args.champ_root.resolve(),args.document.resolve())
