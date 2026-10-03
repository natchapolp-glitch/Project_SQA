"""Supplemental target-class coverage; never replaces modified-class metrics."""
from pathlib import Path
import xml.etree.ElementTree as ET

from .common import read_json, write_json, sha256, identifier
from evaluate import (EvidenceError, clear_generated_evidence, copy_evidence,
                      run_command, parse_coverage_csv)


def validate_stage(stage, directory, test_count, classes):
    directory = Path(directory)
    if stage['timed_out'] or stage['exit_code'] != 0:
        raise EvidenceError('Supplemental coverage command failed or timed out')
    failures = directory / 'failing_tests'
    if not failures.is_file() or failures.read_text(encoding='utf-8').strip():
        raise EvidenceError('Supplemental coverage did not establish passing fixed tests')
    counts = read_json(directory / 'sqa-stage-counts.json')
    if counts['executed'] != test_count or counts['target_checks'] != test_count or counts['skipped']:
        raise EvidenceError('Supplemental coverage must execute every unchanged test')
    xml = directory / 'coverage.xml'
    reported = {c.get('name') for c in ET.parse(xml).getroot().iter('class')}
    if not set(classes) <= reported:
        raise EvidenceError('Supplemental coverage XML lacks requested classes')
    parse_coverage_csv((directory / 'summary.csv').read_text(encoding='utf-8'))
    return counts, sha256(xml)


def measure(evaluation, targets, d4j, test_count, timeout, output_name='target-coverage'):
    evaluation = Path(evaluation)
    record = read_json(evaluation / 'measurement/record.json')
    if record['status'] != 'complete':
        raise ValueError('Supplemental coverage requires complete original measurement')
    before = sha256(evaluation / 'measurement/record.json')
    identifier(output_name, 'supplemental coverage attempt')
    if not output_name.startswith('target-coverage'):
        raise ValueError('Require a supplemental target-coverage directory')
    output = evaluation / output_name
    output.mkdir(exist_ok=False)
    classes = sorted(set(record['instrument_classes']) | {t['class'] for t in targets})
    class_file = output / 'instrument-classes.txt'
    class_file.write_text('\n'.join(classes) + '\n', encoding='utf-8')
    fixed, suite = Path(record['fixed_worktree']), Path(record['suite_path'])
    if sha256(suite) != record['suite_sha256']:
        raise ValueError('Evaluated suite changed; cannot measure supplemental coverage')
    names = ('failing_tests', 'all_tests', 'summary.csv', 'coverage.xml', 'sqa-stage-counts.json')
    clear_generated_evidence(fixed, names)
    stage = run_command([d4j, 'coverage', '-w', str(fixed), '-s', str(suite), '-i', str(class_file)],
                        output, output / 'command', timeout)
    copy_evidence(fixed, output / 'command', names)
    result = {'scope': 'Supplemental target execution only; primary coverage metrics unchanged',
              'primary': False, 'suite_sha256': record['suite_sha256'],
              'original_measurement_sha256': before, 'classes': classes, 'stage': stage,
              'status': 'failed', 'producer_sha256': sha256(__file__)}
    try:
        counts, xml_hash = validate_stage(stage, output / 'command', test_count, classes)
        result.update(status='complete', coverage_sha256=xml_hash, stage_counts=counts)
    except Exception as error:
        result['error'] = f'{type(error).__name__}: {error}'
    if sha256(evaluation / 'measurement/record.json') != before or sha256(suite) != record['suite_sha256']:
        raise ValueError('Original measurement or suite changed during supplemental coverage')
    write_json(output / 'result.json', result)
    return result
