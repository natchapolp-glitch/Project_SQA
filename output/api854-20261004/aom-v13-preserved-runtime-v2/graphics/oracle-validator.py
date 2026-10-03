import hashlib, struct
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
    boundary = name in {'domain_line_null_paint', 'domain_line_null_stroke'}
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


