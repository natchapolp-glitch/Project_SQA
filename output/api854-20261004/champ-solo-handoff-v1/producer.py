"""Capture pinned handoff evidence without running tests, providers, or workers."""
import argparse
from datetime import datetime, timezone
import json
from pathlib import Path
import subprocess

from scripts.study.api854.common import ROOT, read_json, sha256, write_json
from scripts.study.api854.review_v10_readiness import BatchedObjects
from scripts.study.api854.review_joint_recipe_intake import digest, require
from scripts.study.api854.verify_chronology_development import checkpoint
from scripts.study.api854.audit_ready_development_results import audit_packet

CHAMP = '8389043b687b0712119c6540fb06bbce387e76e7'
AOM = 'e2ce1e2701e5d08a01cef0481e53ea621bc9956e'
BEAM = 'f3484746fc19b5aa642e51fb08de5471a0c17509'
BASE = 'output/api854-20261004/'


def run(output):
    output = Path(output).resolve()
    require(output.is_relative_to(ROOT / 'output') and not output.exists(), 'Fresh contained output required')
    pins, _ = checkpoint()
    require(len(pins) == 100, 'Shared checkpoint differs')
    origin = ROOT / BASE / 'champ-aom-next-batch-audit-v1'
    verified = audit_packet(origin)
    hosts = read_json(origin / 'reviewed-host-results.json')
    native = read_json(origin / 'native-results.json')
    require(len(native['records']) == 28 and len({(r['project'], r['bug_id']) for r in native['records']}) == 6,
            'Native outcome accounting differs')
    aom, beam = BatchedObjects(AOM), BatchedObjects(BEAM)
    aom_paths = ['docs/api854/AOM_CLI_UNORDERED_RESULTS_TH.md', 'docs/api854/AOM_READY_PEER_RESULTS_TH.md',
                 BASE + 'aom-ready-results-report-v3/full-d4j-results.json',
                 BASE + 'aom-ready-results-report-v6/receipt.json']
    beam_paths = ['docs/api854/BEAM_COMPRESS_AND_AOM_CSV_RETURN_TH.md',
                  BASE + 'beam-champ-compress-d4j-v2/results.json']
    aom.preload(aom_paths)
    beam.preload(beam_paths)
    cli = [r for r in aom.document(aom_paths[2])['records'] if r['project'] == 'Cli' and r['status'] == 'complete']
    compress = [r for r in beam.document(beam_paths[1]) if r['status'] == 'complete']
    require(len(cli) == len(compress) == 2, 'New owner-completed record scope differs')
    aom.preload([r['record_path'] for r in cli])
    compress_paths = [BASE + 'beam-champ-compress-d4j-v2/' + r['approach'] + '/evaluation/record.json' for r in compress]
    beam.preload(compress_paths)
    new_rows, copies = [], []
    for peer_name, peer, rows, paths in [('aom', aom, cli, [r['record_path'] for r in cli]),
                                         ('beam', beam, compress, compress_paths)]:
        for row, path in zip(rows, paths):
            raw = peer.blob(path)
            require(digest(raw) == row['record_sha256'], 'Canonical completed record hash differs')
            record = json.loads(raw)
            require(record['status'] == 'complete' and record['fixed_validation'] == 'passed_twice' and
                    record['compile_status'] == 'passed', 'Incomplete evaluation record')
            for stage in ('fixed-1', 'fixed-2', 'buggy', 'coverage'):
                command = record['stages'][stage]
                require(command['exit_code'] == 0 and not command.get('timed_out'), 'Unsuccessful stage')
            for stage in ('fixed-1', 'fixed-2'):
                require(record['stages'][stage]['failure_count'] == 0, 'Invalid fixed suite')
            require(record['fault_detected'] == (record['stages']['buggy']['failure_count'] > 0),
                    'Raw buggy failure flag differs')
            row_copy = dict(row, peer=peer_name, peer_commit=peer.commit, canonical_record_path=path,
                            canonical_record_sha256=digest(raw),
                            review_status='owner_completed_canonical_hash_and_stage_status_verified_scoped_semantic_acceptance_pending')
            new_rows.append(row_copy)
            copies.append(('peer-records/' + peer_name + '-' + row['project'] + '-1-' + row['approach'] + '.json', raw))
    counted = list(hosts['records']) + new_rows
    progress = {}
    for approach in ('cmaes', 'fscs-art', 'kku-claude', 'kku-gemini'):
        bugs = sorted({(r['project'], r['bug_id']) for r in counted if r['approach'] == approach})
        progress[approach] = {'owner_completed_unique_bugs': len(bugs), 'bugs': bugs, 'team_target_bugs': 854,
                              'percent_of_team_target': round(100 * len(bugs) / 854, 4)}
    require([progress[a]['owner_completed_unique_bugs'] for a in progress] == [4, 4, 1, 1], 'Progress differs')
    output.mkdir(parents=True)
    (output / 'producer.py').write_bytes(Path(__file__).read_bytes())
    for name in ('native-results.json', 'reviewed-host-results.json'):
        (output / name).write_bytes((origin / name).read_bytes())
    for peer_name, peer, paths in [('aom', aom, aom_paths), ('beam', beam, beam_paths)]:
        for path in paths:
            target = output / 'peer-documents' / peer_name / path
            target.parent.mkdir(parents=True, exist_ok=True)
            target.write_bytes(peer.blob(path))
    for relative, raw in copies:
        target = output / relative
        target.parent.mkdir(parents=True, exist_ok=True)
        target.write_bytes(raw)
    previous = subprocess.check_output(['git', 'show', CHAMP + ':docs/api854/CHAMP_CODEX_HANDOFF_TH.md'], cwd=ROOT)
    saved = output / 'previous-documents/docs/api854/CHAMP_CODEX_HANDOFF_TH.md'
    saved.parent.mkdir(parents=True)
    saved.write_bytes(previous)
    historical_paths = ['output/kku-only-20261001/summary.json', 'results/study/kku-only-20261001/protocol.json']
    historical_bindings = {}
    for path in historical_paths:
        target = output / 'historical' / Path(path).name
        target.parent.mkdir(exist_ok=True)
        target.write_bytes((ROOT / path).read_bytes())
        historical_bindings[path] = sha256(ROOT / path)
    historical = read_json(ROOT / historical_paths[0])
    require(historical['completed_runs'] == 185 and historical['expected_runs'] == 204,
            'Historical summary differs; inspect before using')
    write_json(output / 'checkpoint-pins.json', pins)
    write_json(output / 'owner-completed-new-records.json', {'records': new_rows,
               'raw_semantic_source_host_review_not_completed_by_this_handoff': True})
    status = {'created_at_utc': datetime.now(timezone.utc).isoformat(),
              'mode': 'solo_user_and_codex_no_more_beam_aom_work',
              'user_reported_time_remaining_hours': 20, 'absolute_deadline_unconfirmed': True,
              'time_budget_must_not_restart_on_handoff': True,
              'champ_before_handoff_commit': CHAMP, 'aom_commit': AOM, 'beam_commit': BEAM,
              'frozen_generation_v12': '63ad195623c2ed3f67f3ae232c00c54d3160ce72',
              'checkpoint_pins': len(pins), 'verified_local_audit_packet': verified,
              'native_unique_bugs': 6, 'native_condition_method_outcomes': 28,
              'progress': progress, 'unique_bug_with_four_valid_full_d4j_methods': ['Csv-1'],
              'progress_counts_owner_complete_with_review_status_not_all_scoped_acceptance': True,
              'historical_reported_completed_runs': 185, 'historical_expected_runs': 204,
              'historical_unique_projects_in_protocol': 17, 'historical_reaudited_this_turn': False,
              'historical_is_a_separate_model_processing_repeat_condition': True,
              'historical_source_sha256': historical_bindings,
              'previous_main_handoff_sha256': digest(previous),
              'generation_requests_existing': 14, 'billable_requests_existing': 27,
              'new_api_requests': 0, 'new_evaluations': 0, 'queue_mutations': 0,
              'primary_results_added': 0, 'gate_a_approved': False, 'final_reserve': None,
              'submission_report_ready': False,
              'pending': ['write_report_tables_slides_demo_from_current_evidence',
                          'scoped_raw_review_cli_preparation_and_compress_benchmark_binding',
                          'new_cli_ai_generation_condition_two_models',
                          'solo_linux_host_not_proven_no_peer_machine_access_assumed',
                          'jacksondatabind112_two_native_archives_no_full_d4j_receipt',
                          'assignment_bug_sample_scope_unconfirmed']}
    write_json(output / 'status.json', status)
    now, _ = checkpoint()
    require(now == pins, 'Shared checkpoint changed during handoff')
    write_json(output / 'checksums.json', {p.relative_to(output).as_posix(): sha256(p)
               for p in sorted(output.rglob('*')) if p.is_file()})
    audit = audit_packet(output)
    print(json.dumps({'status': status['mode'], 'progress': progress, 'packet': audit,
                      'checkpoint_pins': len(pins)}, ensure_ascii=False))


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--output', type=Path, required=True)
    run(parser.parse_args().output)
