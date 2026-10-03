"""Exact per-declaration remaining-work inventory; selected is not semantic approval."""
from collections import Counter
import argparse, hashlib, json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[3]
FIELDS = ('class', 'constructor_types', 'method', 'parameter_types')
def read(path): return json.loads(Path(path).read_bytes())
def identity(target): return tuple(target[f] for f in FIELDS)
def sha(path): return hashlib.sha256(Path(path).read_bytes()).hexdigest()
def write(path, value):
    with path.open('x', encoding='utf-8', newline='\n') as stream:
        json.dump(value, stream, ensure_ascii=False, indent=2); stream.write('\n')


def build(output):
    output.mkdir(parents=True, exist_ok=False)
    received = ROOT / 'output/api854-20261004/beam-v12-received-review-v2/received-aom'
    prepare = received / 'output/api854-20261003/prepare-v12-graphics-development-v1'
    index = read(prepare / 'index.json')
    codec_path = ROOT / 'output/api854-20261004/beam-codec-candidate-v1/policy.json'
    graphics_path = ROOT / 'output/api854-20261003/beam-graphics-review-v1/received-champ/scripts/study/api854/development/graphics/policy.json'
    collections_path = ROOT / 'output/api854-20261004/beam-collections-candidate-v1/policy.json'
    csv_path = ROOT / 'output/api854-20261004/beam-csv-constructor-candidate-v1/policy.json'
    csv_receipt_path = csv_path.parent / 'receipt.json'
    csv_receipt = read(csv_receipt_path)
    if csv_receipt['status'] != 'pass' or csv_receipt['shared_selected'] != 403 or csv_receipt['declarations'] != 1:
        raise ValueError('Fresh Csv constructor candidate receipt required')
    csv_constructor = identity(read(csv_path)['target'])
    codec = {identity(t['target']) for t in read(codec_path)['targets']}
    graphics = {identity(t) for t in read(graphics_path)['worklist_identities']}
    collections = {identity(t['target']) for t in read(collections_path)['targets']}
    chronology = {identity(t) for t in read(ROOT / 'output/api854-20261004/beam-v11-received-review-v1/received-aom/output/api854-20261003/prepare-v11-chronology-development-v3/index.json')['accepted_additions']}
    proof = read(ROOT / 'output/api854-20261004/beam-v12-received-review-v2/receipt.json')
    if not proof['graphics_integrated'] or proof['selected'] != 403 or proof['exclusions'] != 288:
        raise ValueError('Fresh Beam v12 review required')
    rows, summaries, bindings = [], [], {}
    for record in index['records']:
        name = record['project'] + '-' + str(record['bug_id'])
        path = prepare / name / 'capability-exclusions.json'
        capabilities = read(path)
        original_path = ROOT / 'docs/api854/evidence/beam-aom-review-20261003/declarations' / name / 'targets.json'
        original = read(original_path)['targets']
        selected = {identity(t) for t in capabilities['selected']}
        excluded = {identity(r['target']): r['reason'] for r in capabilities['excluded']}
        common = {identity(t) for t in original}
        if len(common) != len(original) or selected & set(excluded) or selected | set(excluded) != common:
            raise ValueError('Changed/duplicate common declaration denominator ' + name)
        if (len(selected), len(excluded)) != (record['target_count'], record['capability_exclusion_count']):
            raise ValueError('Index/partition mismatch ' + name)
        counts = Counter()
        for target in original:
            key = identity(target)
            if key in selected:
                status = 'selected_scoped_chronology_graphics_review' if (key in chronology and name == 'Time-1' or key in graphics and name == 'Chart-1') else 'selected_requires_declaration_level_semantic_completion'
            elif name == 'Codec-1' and key in codec:
                status = 'joint_bounded_candidate_awaiting_shared_integration'
            elif name == 'Collections-1' and key in collections:
                status = 'beam_bounded_candidate_awaiting_joint_review_and_shared_integration'
            elif name == 'Csv-1' and key == csv_constructor:
                status = 'beam_bounded_candidate_awaiting_joint_review_and_shared_integration'
            elif name == 'JacksonXml-1' and target['method'] in {'configure','disable','enable','isEnabled'} and target['parameter_types'].startswith('com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser$Feature'):
                status = 'empty_enum_domain_pending_team_decision'
            else: status = 'fixture_oracle_recipe_and_evidence_required'
            counts[status] += 1
            rows.append({'bug': name, 'owner': record['owner'], 'target': {f: target[f] for f in FIELDS},
                         'shared_selected': key in selected, 'exclusion_reason': excluded.get(key),
                         'remaining_work_status': status, 'full_legal_domain_approved': False,
                         'primary_approval': False})
        summaries.append({'bug': name, 'owner': record['owner'], 'declarations': len(common),
                          'shared_selected': len(selected), 'shared_excluded': len(excluded), 'work_status_counts': dict(counts)})
        bindings[name] = {'partition_sha256': sha(path), 'common_inventory_sha256': sha(original_path)}
    counts = Counter(r['remaining_work_status'] for r in rows)
    if len(rows) != 691 or len(summaries) != 20 or sum(r['shared_selected'] for r in rows) != 403:
        raise ValueError('Expected exact 20/691/403 shared inventory')
    expected = {'selected_scoped_chronology_graphics_review': 13,
                'selected_requires_declaration_level_semantic_completion': 390,
                'joint_bounded_candidate_awaiting_shared_integration': 5,
                'beam_bounded_candidate_awaiting_joint_review_and_shared_integration': 11,
                'empty_enum_domain_pending_team_decision': 4,
                'fixture_oracle_recipe_and_evidence_required': 268}
    if dict(counts) != expected: raise ValueError('Unexpected work classification ' + str(counts))
    write(output / 'declarations.json', {'source_commit': '63ad195623c2ed3f67f3ae232c00c54d3160ce72',
          'denominator': 691, 'shared_selected': 403, 'shared_excluded': 288, 'work_status_counts': dict(counts),
          'classification_scope': 'Shared technical selection and prospective scoped evidence only. Historical reviews exist for subsets of the 390; this is not a claim that none were reviewed. Full declaration/domain completion and joint protocol binding still require verification.',
          'empty_enum_decision': 'User confirms still pending joint team decision; retain all four in denominator.',
          'candidate_policy_sha256': {'codec': sha(codec_path), 'graphics': sha(graphics_path), 'collections': sha(collections_path), 'csv_constructor': sha(csv_path)},
          'csv_candidate_receipt_sha256': sha(csv_receipt_path),
          'index_sha256': sha(prepare / 'index.json'), 'source_bindings': bindings, 'bugs': summaries, 'declarations': rows,
          'all_691_complete': False, 'gate_a_approved': False, 'kku_requests': 0, 'queue_mutations': 0})
    lines = ['# งานบีมราย bug บน shared v12', '',
             'Selected คือ capability selection ไม่ใช่ semantic approval ครบทุกรายการ. คง denominator 691.', '',
             '| Bug | Common declarations | Shared selected | Shared excluded |', '|---|---:|---:|---:|']
    lines.extend(f"| {r['bug']} | {r['declarations']} | {r['shared_selected']} | {r['shared_excluded']} |" for r in summaries)
    lines += ['', 'Actual: 403 selected / 288 excluded. Exclusions มี Codec joint candidate 5,',
              'Collections 10 และ Csv constructor 1 bounded candidates รอตรวจร่วม,',
              'enum รอตัดสิน 4 และ recipe/evidence ที่ต้องทำต่อ 268.',
              'Selected อีก 390 มี historical scoped evidence บางส่วน ต้องปิด semantic ราย declaration/domain',
              'ให้ครบก่อน primary approval; Chronology 6 และ Graphics 7 มี scoped v12 proof แล้ว.', '',
              'ดู exact identities และ bindings ใน declarations.json; ไม่มีการแก้ targets/protocol/queue.']
    (output / 'WORKLIST_TH.md').write_text('\n'.join(lines) + '\n', encoding='utf-8', newline='\n')
    write(output / 'checksums.json', {p.name: sha(p) for p in sorted(output.iterdir()) if p.is_file()})
    print(json.dumps({'declarations': len(rows), 'bugs': 20, 'work_status_counts': dict(counts)}))


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__); parser.add_argument('--output', type=Path, required=True)
    args = parser.parse_args(); build(args.output.resolve())
