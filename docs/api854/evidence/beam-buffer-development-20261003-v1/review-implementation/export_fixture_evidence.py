"""Export immutable public development proofs, including unsuccessful reviews."""
import argparse
from pathlib import Path
from .common import ROOT, read_json, write_json, sha256, contained
from .beam_queue import assert_public


def export(root, packet, review_tag=None):
    root, packet = Path(root), Path(packet)
    from .common import identifier
    suffix = '-' + identifier(review_tag, 'review tag') if review_tag else ''
    index = read_json(root / 'index.json')
    semantic = read_json(root / ('semantic-review-index' + suffix + '.json'))
    protocol = read_json(root / 'protocol-proposal.json')
    secrets = []
    access = ROOT / '.local/api854/beam-access.private.json'
    if access.is_file():
        secrets.append(read_json(access)['worker_token'])
    packet.mkdir(parents=True,exist_ok=False)
    def copy(source, relative):
        data = Path(source).read_bytes()
        assert_public(data, tuple(secrets))
        target = contained(packet,relative)
        target.parent.mkdir(parents=True,exist_ok=True)
        with target.open('xb') as stream:
            stream.write(data)
    copy(root / 'protocol-proposal.json','protocol-proposal.json')
    copy(root / ('semantic-review-index' + suffix + '.json'),'semantic-review-index.json')
    results = ROOT / 'results/validation/api854-beam'
    def result_folder(path):
        resolved = Path(path).resolve(strict=True)
        if not resolved.is_relative_to(results.resolve()) or resolved.name != 'result.json':
            raise ValueError('Proof results must be retained under the Beam validation root')
        return resolved.parent
    snapshot = result_folder(index['records'][0]['generation_result']) / 'implementation'
    for name, expected in protocol['source_sha256'].items():
        source = contained(snapshot,name)
        if sha256(source) != expected:
            raise ValueError('Runtime snapshot differs from actual proof protocol')
        copy(source, Path('runtime-implementation') / name)
    for name, expected in [('fixture_development.py',index['development_runner_sha256']),
                           ('fixture_semantics.py',semantic['reviewer_sha256'])]:
        path = ROOT / 'scripts/study/api854' / name
        if sha256(path) != expected:
            raise ValueError('Development producer/reviewer changed after run')
        copy(path,Path('review-implementation') / name)
    records = []
    for row in index['records']:
        name = f"{row['project']}-{row['bug_id']}-{row['approach']}"
        generation = result_folder(row['generation_result'])
        generated = read_json(generation / 'result.json')
        copy(generation / 'result.json',Path(name) / 'original-generation-result.json')
        for filename in ['targets.fixture-policy.json','adapter.json','classes.modified.txt','fixture-classes.txt']:
            path = generation / 'setup' / filename
            if path.is_file(): copy(path,Path(name) / 'setup' / filename)
        if generated.get('observed_outcome') == 'generated':
            suite = generated['generation']['suite']
            if not Path(suite).resolve(strict=True).is_relative_to(generation) or sha256(suite) != generated['suite_sha256']:
                raise ValueError('Suite changed after generation')
            copy(suite,Path(name) / 'suite.tar.bz2')
            for filename in ['observations.json','generation.json','GeneratedStudyTest.java']:
                copy(generation / 'suite' / filename,Path(name) / 'generation' / filename)
        if row.get('evaluation_result'):
            evaluation = result_folder(row['evaluation_result'])
            evaluated = read_json(evaluation / 'result.json')
            copy(evaluation / 'result.json',Path(name) / 'original-evaluation-result.json')
            for stage in ['fixed-1','fixed-2','buggy','coverage']:
                folder = evaluation / 'measurement' / stage
                if folder.is_dir():
                    for source in sorted(folder.iterdir()):
                        if source.is_file(): copy(source,Path(name) / stage / ('command.txt' if source.name == 'command.log' else source.name))
            for target_coverage in sorted(evaluation.glob('target-coverage*')):
                if target_coverage.is_file():
                    copy(target_coverage,Path(name) / target_coverage.name)
                    continue
                for source in sorted(target_coverage.rglob('*')):
                    if source.is_file():
                        relative = source.relative_to(evaluation)
                        if relative.name == 'command.log': relative = relative.with_name('command.txt')
                        copy(source,Path(name) / relative)
            review_path = root / f'{name}-semantic-review{suffix}.json'
            if review_path.is_file():
                review = read_json(review_path)
                copy(review_path,Path(name) / 'semantic-review-input.json')
                copy(evaluation.parent / ('semantic-review' + suffix) / 'review.json',Path(name) / 'semantic-review-result.json')
                copy(evaluation / ('semantic-evidence' + suffix) / 'fixture-oracle-review.json',Path(name) / 'fixture-oracle-review.json')
                mapped = {'measurement/coverage/coverage.xml':'coverage/coverage.xml',
                    'semantic-evidence'+suffix+'/fixture-oracle-review.json':'fixture-oracle-review.json'}
                mapped.update({f'measurement/{s}/sqa-stage-counts.json':f'{s}/sqa-stage-counts.json' for s in ['fixed-1','fixed-2','buggy','coverage']})
                mapped.update({ref['path']:ref['path'] for ref in review['target_execution_evidence']
                               if ref['path'].startswith('target-coverage')})
                write_json(packet / name / 'evidence-path-map.json',{'paths':mapped,'scope':'Immutable review paths mapped to public packet'})
                for ref in review['target_execution_evidence'] + [r['evidence'] for r in review['stage_counts'].values()]:
                    if sha256(packet / name / mapped[ref['path']]) != ref['sha256']:
                        raise ValueError('Public review evidence differs')
                verdict = read_json(evaluation.parent / ('semantic-review' + suffix) / 'review.json')
                if (verdict['evaluation_result_sha256'] != sha256(packet / name / 'original-evaluation-result.json')
                        or verdict['review_sha256'] != sha256(packet / name / 'semantic-review-input.json')):
                    raise ValueError('Public immutable result/review binding differs')
            row = {**row,'fixed_validation':evaluated.get('measurement',{}).get('fixed_validation'),
                'buggy_failure_count':evaluated.get('measurement',{}).get('stages',{}).get('buggy',{}).get('failure_count')}
        records.append({**row,'semantic_review':next(r for r in semantic['reviews']
            if (r['project'],r['bug_id'],r['approach']) == (row['project'],row['bug_id'],row['approach']))})
    copy(__file__,Path('review-implementation') / 'export_fixture_evidence.py')
    write_json(packet / 'index.json',{**index,'records':records,'source_sha256':protocol['source_sha256'],
        'exporter_sha256':sha256(__file__),'gate_a_approved':False,'team_or_primary_approval':False,
        'semantic_validity':'Separate local development review only; every failure and exclusion retained'})
    write_json(packet / 'checksums.json',{p.relative_to(packet).as_posix():sha256(p) for p in sorted(packet.rglob('*')) if p.is_file()})
    print('EXPORTED',len(records),'suites;',len(read_json(packet / 'checksums.json')),'hash-bound files')


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--run',required=True,type=Path)
    parser.add_argument('--packet',required=True,type=Path)
    parser.add_argument('--review-tag')
    args = parser.parse_args()
    export(args.run,args.packet,args.review_tag)
