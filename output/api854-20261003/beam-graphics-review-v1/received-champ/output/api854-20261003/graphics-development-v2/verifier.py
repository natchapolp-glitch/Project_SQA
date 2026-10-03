"""Seal and execute a bounded Graphics2D candidate from local Chart-1 SVN bytes.

Standalone production compilation, not a full Defects4J evaluation or shared
recipe integration. Real headless AWT pixels and exact inherited JDI entries.
"""
import argparse
import csv
from datetime import datetime, timezone
import hashlib
import io
import json
import os
from pathlib import Path
import platform
import shutil
import struct
import subprocess
import tarfile
import tempfile
import time

from .common import ROOT, implementation_hashes, read_json, sha256, write_json
from .verify_chronology_development import checkpoint as shared_checkpoint, parse_log, require

TOOLS = ROOT / 'scripts/study/api854/development/graphics'
PREP = ROOT / 'output/api854-20261003/prepare-v9-twenty-bug-development/Chart-1'
OWNER = 'org.jfree.chart.renderer.category.AbstractCategoryItemRenderer'
RECEIVER = 'org.jfree.chart.renderer.category.AreaRenderer'
SOURCE = 'source/org/jfree/chart/renderer/category/AbstractCategoryItemRenderer.java'
RECEIVER_SOURCE = 'source/org/jfree/chart/renderer/category/AreaRenderer.java'
JVM = ['-Djava.awt.headless=true', '-Duser.timezone=UTC', '-Duser.language=en', '-Duser.country=US']
POLICY = read_json(TOOLS / 'policy.json')
CASES = POLICY['cases']
TARGETS = POLICY['exact_targets']


def expected_state(name):
    color, width = -16777216, 1.0
    if name.startswith('annotations_'):
        color = -65536 if '_fg_' in name else -16711936
    elif name.startswith('background_'):
        color = -256
    elif name.startswith('domain_line_') and 'null_' not in name:
        color, width = -65536, 2.0
    elif name.startswith('domain_marker_') and not name.endswith('missing'):
        color = -65536
        if '_line_' in name:
            width = 2.0
    elif name == 'outline_enabled':
        color, width = -16776961, 2.0
    elif name.startswith('range_') and name != 'range_outside':
        color = -65536
        if '_value_' in name:
            width = 2.0
    with_dataset = name == 'initialise_dataset'
    result = None
    if name.startswith('initialise_'):
        result = {'class': 'org.jfree.chart.renderer.category.CategoryItemRendererState', 'info_null': True,
                  'bar_width': 0.0, 'selection_matches_dataset': with_dataset, 'selection_null': not with_dataset}
    boundary = 'null_' in name
    return {'graphics': {'paint_rgb': color, 'stroke_width': width, 'composite_rule': 3,
                         'composite_alpha': 0.5, 'identity_transform': True, 'clip_null': True},
            'rows': 2 if with_dataset else 0, 'columns': 2 if with_dataset else 0, 'plot_bound': True,
            'dataset': [2.0, 8.0, 4.0, 6.0], 'return_state': result,
            'exception': 'java.lang.IllegalArgumentException' if boundary else None,
            'message': "Null 'paint' argument." if name.endswith('null_paint') else
                       "Null 'stroke' argument." if name.endswith('null_stroke') else None}


def validate_records(records, images, allow_assertion_failures=False):
    rows = [r for r in records if 'case' in r and not r.get('method_entry')]
    require([r['case'] for r in rows] == list(CASES), 'Graphics case inventory/order differs')
    for row in rows:
        name = row['case']
        require(row['setup_succeeded'] is True and row['target_invoked'] is True,
                'Graphics fixture/invocation failure cannot count as target evidence')
        require(row['method'] == CASES[name] and row['receiver_class'] == RECEIVER
                and row['declaring_class'] == OWNER, 'Wrong inherited declaration or receiver identity')
        expected = row['expected_observation']
        require({k: v for k, v in expected.items() if k != 'pixel_sha256'} == expected_state(name),
                'Independent graphics/state/exception oracle differs: ' + name)
        actual_pixels = (images / (name + '.actual.argb')).read_bytes()
        reference_pixels = (images / (name + '.reference.argb')).read_bytes()
        require(len(actual_pixels) == len(reference_pixels) == 64*64*4, 'Wrong canvas dimensions/format')
        for pixels, metadata in [(actual_pixels, row['observation']), (reference_pixels, expected)]:
            require(hashlib.sha256(pixels).hexdigest() == metadata['pixel_sha256'], 'Pixel evidence hash differs')
        noop = name in {'domain_marker_missing', 'outline_disabled', 'range_outside',
                        'initialise_dataset', 'initialise_null_dataset',
                        'domain_line_null_paint', 'domain_line_null_stroke'}
        reference = struct.unpack('>4096i', reference_pixels)
        require(all(v == -1 for v in reference) if noop else any(v != -1 for v in reference),
                'Expected drawing/no-op footprint differs')
        # Filled solid regions have an independent full-image integer oracle.
        box = None
        if name.startswith('background_'):
            box, color = (10, 10, 40, 40), -256
        elif name.startswith('domain_marker_band_'):
            box, color = (10, 10, 40, 20) if name.endswith('_h') else (10, 10, 20, 40), -65536
        elif name.startswith('range_interval_'):
            box, color = (18, 10, 24, 40) if name.endswith('_h') else (10, 18, 40, 24), -65536
        if box:
            x0, y0, width, height = box
            derived = tuple(color if x0 <= x < x0+width and y0 <= y < y0+height else -1
                            for y in range(64) for x in range(64))
            require(reference == derived, 'Analytic filled-region pixel oracle differs')
        passed = row['observation'] == expected and actual_pixels == reference_pixels
        require(type(row['target_check_passed']) is bool and row['target_check_passed'] is passed,
                'Graphics assertion/result counters differ')
        if passed:
            require(row['failure_class'] is None and row['failure_reason'] is None, 'Passed drawing has failure')
        else:
            require(row['failure_class'] == 'java.lang.AssertionError', 'Unexpected fixture/runtime failure class')
            require(allow_assertion_failures, 'Graphics fixed assertion failed: ' + name)
    passed = sum(r['target_check_passed'] for r in rows)
    require([r for r in records if r.get('summary')] == [{'summary': True, 'executed': 24,
            'target_checks': 24, 'passed': passed, 'failed': 24-passed, 'skipped': 0, 'fixture_errors': 0}],
            'Graphics counters/skips/fixture errors differ')
    return rows


def validate_trace(records, exit_code):
    entries = [r for r in records if r.get('method_entry')]
    first = {}
    for row in entries:
        require(row['case'] in CASES and row['class'] == OWNER and row['source_line'] > 0,
                'Wrong graphics entry class/case/source line')
        first.setdefault(row['case'], row)
    require(set(first) == set(CASES), 'Missing exact graphics target entry')
    for name, entry in first.items():
        require([entry['method'], entry['descriptor']] == TARGETS[CASES[name]], 'Wrong inherited JVM descriptor')
    require(len({(r['method'], r['descriptor']) for r in first.values()}) == 7, 'Seven declaring methods required')
    require([r for r in records if r.get('trace_summary')] == [{'trace_summary': True,
            'method_entries': len(entries), 'debuggee_exit_code': exit_code}], 'Graphics trace counters differ')
    return list(first.values())


def linux_path(path):
    path = Path(path).resolve()
    require(len(path.drive) == 2 and path.drive[1] == ':', 'WSL path requires an absolute Windows drive')
    return '/mnt/' + path.drive[0].lower() + '/' + '/'.join(path.parts[1:])


def execute(command, packet, name, check=True):
    begin = time.monotonic()
    result = subprocess.run(list(map(str, command)), capture_output=True, timeout=240)
    for suffix, raw in [('stdout.log', result.stdout), ('stderr.log', result.stderr)]:
        with (packet / (name + '.' + suffix)).open('xb') as stream:
            stream.write(raw)
    record = {'command': list(map(str, command)), 'exit_code': result.returncode,
              'duration_seconds': time.monotonic()-begin,
              'stdout_sha256': sha256(packet / (name + '.stdout.log')),
              'stderr_sha256': sha256(packet / (name + '.stderr.log'))}
    write_json(packet / (name + '.command.json'), record)
    if check:
        require(result.returncode == 0, name + ' failed: ' + result.stderr.decode(errors='replace')[-1800:])
    return result, record


def archive_sources(folder, destination):
    with tarfile.open(destination, 'w:gz', compresslevel=9) as archive:
        for path in sorted(folder.rglob('*')):
            if path.is_file():
                data = path.read_bytes()
                info = tarfile.TarInfo(path.relative_to(folder.parent).as_posix())
                info.size = len(data); info.mtime = 0
                archive.addfile(info, io.BytesIO(data))


def verify(defects4j, output):
    defects4j, output = Path(defects4j).resolve(), Path(output).resolve()
    require(output.is_relative_to(ROOT / 'output') and not output.exists(), 'Require new repository evidence path')
    pins, _ = shared_checkpoint()
    work = read_json(ROOT / 'output/api854-20261003/aom-champ-v9-readiness-worklist-v1.json')
    group = next(g for g in work['fixture_development_groups'] if g['owner'] == 'champ' and 'Graphics2D' in g['reason'])
    identities = [r['target'] for r in group['targets']]
    require(group['affected_targets'] == 7 and identities == POLICY['worklist_identities'], 'Graphics worklist differs')
    for row in work['unsupported_targets']:
        if row['project'] == 'Chart' and row['bug_id'] == 1 and row['target'] in identities:
            pins[row['raw_case']] = row['raw_case_sha256']
    require(all(sha256(ROOT / path) == value for path, value in pins.items()), 'Graphics raw-case/checkpoint hash differs')
    output.mkdir(parents=True, exist_ok=False)
    runtime = implementation_hashes()
    suite = {}
    for name in ['policy.json', 'Graphics2DProbe.java', 'GraphicsEntryTrace.java']:
        (output / name).write_bytes((TOOLS / name).read_bytes()); suite[name] = sha256(output / name)
    (output / 'verifier.py').write_bytes(Path(__file__).read_bytes()); suite['verifier.py'] = sha256(output / 'verifier.py')
    with (defects4j / 'framework/projects/Chart/commit-db').open() as stream:
        revision = next(row for row in csv.reader(stream) if row[0] == '1')
    revisions = {'fixed': revision[2], 'buggy': revision[1]}
    url = 'file://' + linux_path(defects4j / 'project_repos/jfreechart') + '/trunk'
    archives, compiled_sources, dependencies, bytecodes, stages = {}, {}, {}, {}, {}
    with tempfile.TemporaryDirectory(dir=ROOT / 'output', prefix='.champ-graphics-build-') as temporary:
        base = Path(temporary).resolve()
        require(base.is_relative_to(ROOT / 'output'), 'Temporary build escaped intended workspace')
        classpaths = {}
        for version, revision in revisions.items():
            working = base / version; working.mkdir()
            execute(['wsl', '--exec', 'svn', 'export', '-r', revision, url + '/source@' + revision,
                     linux_path(working / 'source')], output, version + '-source-export')
            execute(['wsl', '--exec', 'svn', 'export', '-r', revision, url + '/lib@' + revision,
                     linux_path(working / 'lib')], output, version + '-dependencies-export')
            archive = output / (version + '-production-source.tar.gz')
            archive_sources(working / 'source', archive)
            archives[version] = {'svn_revision': revision, 'url': url + '/source', 'archive_sha256': sha256(archive)}
            dependencies[version] = {p.name: sha256(p) for p in sorted((working / 'lib').glob('*.jar'))}
            dependency_folder = output / 'dependencies'; dependency_folder.mkdir(exist_ok=True)
            for path in (working / 'lib').glob('*.jar'):
                copied = dependency_folder / path.name
                if copied.exists():
                    require(sha256(copied) == sha256(path), 'Dependency differs between revisions')
                else:
                    copied.write_bytes(path.read_bytes())
            if version == 'fixed':
                for name in [SOURCE, RECEIVER_SOURCE]:
                    retained = PREP / 'fixed-source' / name
                    require((working / name).read_bytes().replace(b'\r\n', b'\n') == retained.read_bytes().replace(b'\r\n', b'\n'),
                            'SVN source differs from retained shared source: ' + name)
                    (working / name).write_bytes(retained.read_bytes())
            compiled_sources[version] = {p.relative_to(working).as_posix(): sha256(p)
                                         for p in sorted((working / 'source').rglob('*.java'))}
            classes = working / 'classes'; classes.mkdir()
            classpaths[version] = os.pathsep.join(map(str, [classes, *sorted((working / 'lib').glob('*.jar'))]))
        write_json(output / 'preexecution-seal.json', {
            'sealed_at_utc': datetime.now(timezone.utc).isoformat(), 'policy_id': POLICY['policy_id'],
            'suite_sha256': suite, 'shared_input_sha256': pins, 'runtime_source_sha256': runtime,
            'svn_revisions': revisions, 'source_archives': archives, 'compiled_source_sha256': compiled_sources,
            'dependencies_sha256': dependencies, 'worklist_identities': identities,
            'fixed_target_source_sha256': {name: sha256(PREP / 'fixed-source' / name) for name in [SOURCE, RECEIVER_SOURCE]},
            'primary': False, 'owner_approval': False, 'selection_used_buggy_outcomes': False})
        for version in revisions:
            working, cp = base / version, classpaths[version]
            execute(['javac', '--release', '8', '-g', '-encoding', 'UTF-8', '-cp', cp,
                     '-sourcepath', working / 'source', '-d', working / 'classes',
                     working / SOURCE, working / RECEIVER_SOURCE, output / 'Graphics2DProbe.java'], output, version + '-compile')
            for path in (working / 'source').rglob('*.properties'):
                destination = working / 'classes' / path.relative_to(working / 'source')
                destination.parent.mkdir(parents=True, exist_ok=True); destination.write_bytes(path.read_bytes())
            bytecodes[version] = {name: sha256(working / 'classes' / (name.replace('.', '/') + '.class')) for name in [OWNER, RECEIVER]}
            if version == 'fixed':
                execute(['javac', '--add-modules', 'jdk.jdi', '-d', working / 'classes', output / 'GraphicsEntryTrace.java'],
                        output, 'compile-jdi-trace')
            for repeat in ['first', 'second']:
                stage = version + '_' + repeat
                images = output / (stage + '-pixels'); images.mkdir()
                result, record = execute(['java', *JVM, '-cp', cp, 'sqa.development.Graphics2DProbe', images], output, stage, check=False)
                record['cases'] = validate_records(parse_log(result.stdout), images, allow_assertion_failures=version == 'buggy')
                require(result.returncode == int(any(not r['target_check_passed'] for r in record['cases'])), 'Graphics process/assertion status differs')
                stages[stage] = record
            require(stages[version + '_first']['cases'] == stages[version + '_second']['cases'], 'Graphics results did not repeat')
        for version in revisions:
            stage = version + '_method_entry_trace'
            images = output / (stage + '-pixels'); images.mkdir()
            result, record = execute(['java', '--add-modules', 'jdk.jdi', '-cp', classpaths['fixed'],
                                     'GraphicsEntryTrace', classpaths[version], images], output, stage, check=False)
            records = parse_log(result.stdout)
            record['cases'] = validate_records(records, images, allow_assertion_failures=version == 'buggy')
            require(record['cases'] == stages[version + '_first']['cases'] and result.returncode == stages[version + '_first']['exit_code'],
                    'Graphics tracing changed observations/process status')
            record['exact_target_entries'] = validate_trace(records, result.returncode)
            stages[stage] = record
            require(bytecodes[version] == {name: sha256(base / version / 'classes' / (name.replace('.', '/') + '.class'))
                                           for name in [OWNER, RECEIVER]}, 'Graphics tracing changed production classes')
        source = base / 'fixed' / SOURCE
        text = source.read_text(encoding='utf-8')
        needle = 'line = new Line2D.Double(value, dataArea.getMinY(), value,'
        require(text.count(needle) == 1, 'Unexpected drawDomainLine source for sensitivity check')
        source.write_text(text.replace(needle, 'line = new Line2D.Double(value + 4, dataArea.getMinY(), value + 4,'), encoding='utf-8')
        (output / 'temporary-shifted-domain-line.java').write_bytes(source.read_bytes())
        execute(['javac', '--release', '8', '-g', '-encoding', 'UTF-8', '-cp', classpaths['fixed'],
                 '-d', base / 'fixed/classes', source], output, 'compile-temporary-shifted-line')
        images = output / 'temporary-shifted-line-pixels'; images.mkdir()
        result, mutation = execute(['java', *JVM, '-cp', classpaths['fixed'], 'sqa.development.Graphics2DProbe', images],
                                   output, 'temporary-shifted-line', check=False)
        mutation_rows = validate_records(parse_log(result.stdout), images, allow_assertion_failures=True)
        mutation['failed_cases'] = [r['case'] for r in mutation_rows if not r['target_check_passed']]
        require(result.returncode == 1 and mutation['failed_cases'] == ['domain_line_v'], 'Oracle missed shifted-line mutation')
    require(runtime == implementation_hashes() and all(sha256(ROOT / p) == h for p, h in pins.items()), 'Shared inputs/runtime changed during graphics development')
    require(all(sha256(output / p) == h for p, h in suite.items()), 'Sealed graphics suite changed')
    failed = [r['case'] for r in stages['buggy_first']['cases'] if not r['target_check_passed']]
    receipt = {'schema_version': 1, 'status': 'pass', 'completed_at_utc': datetime.now(timezone.utc).isoformat(),
               'scope': 'Standalone bounded Graphics2D oracle development; compiled original Chart-1 SVN revisions, not full Defects4J',
               'policy_id': POLICY['policy_id'], 'suite_sha256': suite, 'preexecution_seal_sha256': sha256(output / 'preexecution-seal.json'),
               'shared_input_sha256': pins, 'runtime_source_sha256': runtime, 'source_archives': archives,
               'compiled_source_sha256': compiled_sources, 'production_class_sha256': bytecodes, 'dependencies_sha256': dependencies,
               'stages': stages, 'cases_per_revision': 24, 'exact_declarations_entered_per_revision': 7,
               'worklist_identities': identities, 'declaring_class': OWNER, 'receiver_class': RECEIVER,
               'repeated_fixed_equal': True, 'repeated_buggy_equal': True, 'buggy_failed_cases': failed,
               'candidate_fault_detected': bool(failed), 'temporary_shifted_line_mutation': mutation,
               'oracle_sensitivity_verified': True, 'full_defects4j_evaluation': False,
               'coverage_kind': 'JDI exact inherited method entry; no line/branch percentage', 'line_or_branch_coverage_percentage': None,
               'oracle_approved': False, 'shared_integration_approved': False, 'runtime_modified': False, 'shared_preparation_modified': False,
               'selected': 380, 'unsupported': 311, 'denominator': 691, 'enum_targets_still_unsupported': 4,
               'gate_a_passed': False, 'final_prompt_reserve': None, 'primary_results_added': 0, 'live_requests': 0, 'queue_mutations': 0,
               'java_version': subprocess.run(['java', '-version'], capture_output=True, check=True).stderr.decode().strip(),
               'python': platform.python_version(), 'platform': platform.platform()}
    write_json(output / 'receipt.json', receipt)
    write_json(output / 'checksums.json', {p.relative_to(output).as_posix(): sha256(p)
               for p in sorted(output.rglob('*')) if p.is_file()})
    return receipt


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--defects4j', type=Path, required=True)
    parser.add_argument('--output', type=Path, required=True)
    args = parser.parse_args()
    if args.output.exists():
        parser.error('Evidence output already exists; choose a new packet path')
    try:
        result = verify(args.defects4j, args.output)
    except (ValueError, OSError, subprocess.SubprocessError) as error:
        output = args.output.resolve()
        if output.is_relative_to(ROOT / 'output') and output.is_dir() and not (output / 'checksums.json').exists():
            write_json(output / 'failure.json', {'status': 'fail', 'reason': str(error), 'primary': False, 'approval': False})
            write_json(output / 'checksums.json', {p.relative_to(output).as_posix(): sha256(p)
                       for p in sorted(output.rglob('*')) if p.is_file()})
        raise
    print(json.dumps({k: result[k] for k in ['status', 'cases_per_revision', 'exact_declarations_entered_per_revision',
                                           'candidate_fault_detected', 'buggy_failed_cases', 'oracle_sensitivity_verified']}))


if __name__ == '__main__':
    main()
