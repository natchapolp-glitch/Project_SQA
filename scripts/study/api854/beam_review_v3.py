"""Read-only Beam review of shared-v3 discovery, context and one-host routing."""
import argparse
from pathlib import Path

from .common import ROOT, read_json, sha256, write_json
from .fixture_policy import select, POLICY as EXPLICIT_POLICY
from .preparation import clean_targets, clean_fixture_classes, validate, digest, encoded
from .gate_a import route_coverage


def checked_files(folder):
    checksums = read_json(folder / 'checksums.json')
    for name, expected in checksums.items():
        path = (folder / name).resolve(strict=True)
        if not path.is_relative_to(folder.resolve()) or sha256(path) != expected:
            raise ValueError('Artifact hash or containment differs')
    return len(checksums)


def inspect(preparation, review, plan_path, ownership_path):
    preparation, review = Path(preparation), Path(review)
    checked_files(review)
    identities = {(r['project'], r['bug_id']): r['owner'] for r in read_json(review / 'index.json')['bugs']}
    index = read_json(preparation / 'index.json')
    rows, checked = [], 0
    fields = ('class', 'constructor_types', 'method', 'parameter_types')
    def identity(target):
        return tuple(target[k] for k in fields)
    for row in index['records']:
        key = (row['project'], row['bug_id'])
        if identities.get(key) != row['owner']:
            raise ValueError('Pilot identity/owner differs')
        name = f'{key[0]}-{key[1]}'
        folder = preparation / name
        checked += checked_files(folder)
        manifest = read_json(folder / 'context-manifest.json')
        metadata = read_json(folder / 'prepare-metadata.json')
        document = read_json(folder / 'targets.json')
        eligibility = read_json(folder / 'eligibility.json')
        beam_eligibility = read_json(review.parent / 'beam-aom-eligibility-import-20261003' / name / 'eligibility.json')
        validate(manifest, metadata, (folder / 'prompt.md').read_bytes(),
                 (folder / 'targets.json').read_bytes(), (folder / 'prepare-policy.json').read_bytes(), require_eligible=True)
        fixed = read_json(review / 'declarations' / name / 'targets.fixed.json')['targets']
        buggy = read_json(review / 'declarations' / name / 'targets.buggy.json')['targets']
        buggy_ids = {identity(t) for t in buggy}
        common = clean_targets([t for t in fixed if identity(t) in buggy_ids])
        excluded = clean_targets([t for t in fixed if identity(t) not in buggy_ids])
        fixtures = clean_fixture_classes((review / 'declarations' / name / 'fixture-classes.txt').read_text().splitlines())
        if (document['targets'] != common or eligibility['targets'] != common
                or clean_targets(eligibility['excluded_fixed_only']) != excluded
                or document['fixture_classes'] != fixtures or eligibility['fixture_classes'] != fixtures
                or eligibility['fixed_source_sha256'] != metadata['fixed_source_sha256']
                or beam_eligibility['fixed_source_sha256'] != metadata['fixed_source_sha256']):
            raise ValueError('Shared-v3 discovery/exclusions/fixture binding differs')
        # Verify fixed production/build bytes directly, not only metadata labels.
        for source in manifest['source_files']:
            path = (folder / 'fixed-source' / source['path']).resolve(strict=True)
            if (not path.is_relative_to((folder / 'fixed-source').resolve())
                    or sha256(path) != source['sha256'] or path.stat().st_size != source['bytes']):
                raise ValueError('Fixed context source bytes differ')
        proof = read_json(folder / 'revision-proof.json')
        if not proof['verified'] or proof['head'] != proof['fixed_tag_commit']:
            raise ValueError('Fixed revision proof differs')
        if name == 'Chart-1':
            receiver = 'source/org/jfree/chart/renderer/category/AreaRenderer.java'
            if metadata['additional_receiver_source_sha256'] != {receiver: 'fb540d6b7c8faf9f9b83e26d51d9756ed2978b02e29f8f8100ce84661cd227c6'}:
                raise ValueError('Chart concrete receiver mapping differs')
        elif metadata['additional_receiver_source_sha256']:
            raise ValueError('Unexpected additional receiver')
        supported, unsupported = select(common, EXPLICIT_POLICY)
        rows.append({'project': key[0], 'bug_id': key[1], 'owner': row['owner'],
            'declaration_review': 'pass', 'target_count': len(common), 'excluded_fixed_only': excluded,
            'fixture_class_count': len(fixtures), 'fixed_source_sha256': metadata['fixed_source_sha256'],
            'targets_sha256': sha256(folder / 'targets.json'), 'prompt_sha256': sha256(folder / 'prompt.md'),
            'fixture_classes_sha256': digest(encoded(fixtures)),
            'explicit_v3_recipe_target_count': len(supported), 'explicit_v3_unsupported': unsupported,
            'semantic_review': 'new_development_evidence_available_not_shared_approval' if key in {('Closure', 176), ('JxPath', 1)} else 'construction_oracle_review_required',
            'shared_v3_semantic_approved': False})
    if len(rows) != 20 or {(r['project'], r['bug_id']) for r in rows} != set(identities):
        raise ValueError('Require exactly the original 20 pilot bugs')
    plan = read_json(plan_path)
    beam_hosts = [r for r in plan['runners'] if r['role'] == 'beam']
    if len(beam_hosts) != 1 or beam_hosts[0]['worker_id'] != 'beam-pc1' or beam_hosts[0]['max_cpu_slots'] != 1:
        raise ValueError('Beam confirmed one host and one CPU slot')
    routing = route_coverage(plan, read_json(ownership_path))
    if routing['gaps']:
        raise ValueError('One-host plan has uncovered stage routes')
    return {'schema_version': 1, 'scope': 'Beam exact shared-v3 discovery/context/routing review; no semantic approval from inventory alone',
        'primary': False, 'team_or_primary_approval': False, 'gate_a_approved': False,
        'preparation_index_sha256': sha256(preparation / 'index.json'),
        'runner_plan_sha256': sha256(plan_path), 'review_module_sha256': sha256(__file__),
        'review_dependency_sha256': {name: sha256(ROOT / f'scripts/study/api854/{name}.py')
                                    for name in ['fixture_policy', 'preparation', 'gate_a']},
        'bugs_reviewed': len(rows), 'target_count': sum(r['target_count'] for r in rows),
        'fixed_only_exclusion_count': sum(len(r['excluded_fixed_only']) for r in rows),
        'checked_preparation_files': checked, 'beam_host_count': 1, 'routing': routing,
        'records': rows, 'real_kku_requests': 0, 'live_queue_mutations': 0}


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--output', required=True, type=Path)
    args = parser.parse_args()
    result = inspect(ROOT / 'output/api854-20261003/prepare-v3',
        ROOT / 'docs/api854/evidence/beam-aom-review-20261003',
        ROOT / 'experiments/configs/api854-20261003/runner-plan.beam-one-host.v2.json',
        ROOT / 'experiments/configs/api854-20261003/ownership.json')
    args.output.parent.mkdir(parents=True, exist_ok=True)
    write_json(args.output, result)
    print(f"Reviewed {result['bugs_reviewed']} bugs / {result['target_count']} declarations / one Beam host; semantic gate remains pending")


if __name__ == '__main__':
    main()
