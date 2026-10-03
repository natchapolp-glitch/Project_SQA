"""Reconcile v9 capability accounting with immutable, explicitly historical diagnostics."""
import argparse
from collections import Counter, defaultdict
from datetime import datetime, timezone
import json
from pathlib import Path

from .common import ROOT, implementation_hashes, read_json, sha256
from .audit_v9_shared_limits import inspect as inspect_shared
from .review_beam_readiness import inspect as inspect_beam, identity, category

PREP = 'output/api854-20261003/prepare-v9-twenty-bug-development'
SWEEP = 'docs/api854/evidence/beam-v7-fixed-sweep-20261003'
ENUM_TYPE = 'com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser$Feature'


def reconcile(selected, excluded, diagnostics):
    """Reject gaps, overlaps, duplicates and identity drift before assigning work."""
    selected_keys = [identity(t) for t in selected]
    excluded_keys = [identity(r['target']) for r in excluded]
    case_keys = [identity(r['target']) for r in diagnostics]
    for name, keys in [('selected', selected_keys), ('excluded', excluded_keys), ('diagnostic', case_keys)]:
        if len(keys) != len(set(keys)):
            raise ValueError('Duplicate '+name+' identity')
    if set(selected_keys) & set(excluded_keys):
        raise ValueError('Selected/excluded overlap')
    if set(selected_keys) | set(excluded_keys) != set(case_keys):
        raise ValueError('Declaration partition differs from diagnostic inventory')
    return {identity(case['target']): (index, case) for index, case in enumerate(diagnostics)}


def next_action(case, empty_enum):
    if empty_enum:
        return 'Joint decision on empty legal non-null domain; keep unsupported and review separate null-boundary development evidence.'
    kind = category(case)
    if kind == 'fixture_error':
        return 'Develop the missing argument/receiver/projection recipe; then obtain repeated execution, state oracle and target coverage for this exact signature.'
    if kind == 'stable_normal_observation_oracle_review_needed':
        return 'Review fixed-source semantics and meaningful assertions, vary inputs/state, and obtain condition-bound repeated execution and target coverage.'
    if kind == 'target_exception_review_needed':
        return 'Review receiver/argument preconditions and the target exception; distinguish valid-domain behavior from invalid-input boundary coverage.'
    return 'Resolve unstable or unverified invocation before oracle acceptance.'


def build():
    beam = inspect_beam()
    if beam['status'] != 'pass':
        raise ValueError('Received Beam evidence failed integrity verification')
    shared = inspect_shared()
    index = read_json(ROOT/PREP/'index.json')
    sweep = read_json(ROOT/SWEEP/'index.json')
    diagnostic_bugs = {(r['project'], r['bug_id']): r for r in sweep['records']}
    if len(diagnostic_bugs) != len(sweep['records']) or len(diagnostic_bugs) != 20:
        raise ValueError('Require the exact twenty-bug diagnostic inventory')
    rows, bug_summary, additions = [], [], []
    inputs = {PREP+'/index.json': sha256(ROOT/PREP/'index.json'),
              SWEEP+'/index.json': sha256(ROOT/SWEEP/'index.json'),
              SWEEP+'/checksums.json': sha256(ROOT/SWEEP/'checksums.json')}
    groups = defaultdict(list)
    for record in index['records']:
        key = (record['project'], record['bug_id'])
        diagnostic = diagnostic_bugs.pop(key)
        if diagnostic['owner'] != record['owner']:
            raise ValueError('Owner differs from sealed diagnostic inventory')
        label = f'{key[0]}-{key[1]}'
        folder = ROOT/PREP/label
        selected = read_json(folder/'targets.json')['targets']
        excluded = read_json(folder/'capability-exclusions.json')['excluded']
        cases = reconcile(selected, excluded, diagnostic['cases'])
        for name in ['targets.json', 'capability-exclusions.json']:
            inputs[f'{PREP}/{label}/{name}'] = sha256(folder/name)
        summary = Counter()
        for exclusion in excluded:
            n, case = cases[identity(exclusion['target'])]
            raw = f'{SWEEP}/{label}/case-{n:03d}.json'
            if read_json(ROOT/raw) != case:
                raise ValueError('Raw case differs from diagnostic index')
            target = exclusion['target']
            enum = target['class'] == ENUM_TYPE.rsplit('$', 1)[0] and ENUM_TYPE in target['parameter_types'].split(',')
            kind = category(case)
            row = {'project':key[0], 'bug_id':key[1], 'owner':record['owner'],
                   'target':target, 'current_exclusion_reason':exclusion['reason'],
                   'historical_diagnostic_category':kind, 'historical_observation':case['first'],
                   'historical_repeat_equal':case['repeat_equal'], 'historical_fixture_policy':sweep['fixture_policy'],
                   'current_runtime_reexecution':False, 'oracle_approved':False,
                   'domain_constraint':'empty_non_null_parameter_domain' if enum else None,
                   'raw_case':raw, 'raw_case_sha256':sha256(ROOT/raw),
                   'next_action':next_action(case, enum)}
            rows.append(row)
            summary[kind] += 1
            if kind == 'fixture_error':
                groups[(record['owner'], case['first']['reason'])].append({'project':key[0], 'bug_id':key[1], 'target':target})
        for target in selected:
            _, case = cases[identity(target)]
            if case['v7_capability'] == 'unsupported':
                additions.append({'project':key[0], 'bug_id':key[1], 'owner':record['owner'],
                                  'target':target, 'historical_diagnostic_category':category(case),
                                  'prospective_capability_only':True, 'historical_unsupported_closed':False})
        bug_summary.append({'project':key[0], 'bug_id':key[1], 'owner':record['owner'],
                            'selected':len(selected), 'unsupported':len(excluded), 'diagnostic_counts':dict(summary)})
    if diagnostic_bugs or len(rows) != 311 or len(additions) != 3:
        raise ValueError('Unexpected v9 reconciliation totals')
    if sum(b['selected'] for b in bug_summary) != 380:
        raise ValueError('Selected count differs')
    enum_rows = [r for r in rows if r['domain_constraint']]
    if len(enum_rows) != 4 or {r['target']['method'] for r in enum_rows} != {'configure','disable','enable','isEnabled'}:
        raise ValueError('Empty-enum inventory differs')
    owners = {}
    for owner in ('aom','beam','champ'):
        owned = [r for r in rows if r['owner'] == owner]
        owners[owner] = {'unsupported':len(owned),
            'historical_diagnostic_counts':dict(Counter(r['historical_diagnostic_category'] for r in owned)),
            'pending_empty_enum_targets':sum(bool(r['domain_constraint']) for r in owned)}
    return {'schema_version':1, 'checked_at_utc':datetime.now(timezone.utc).isoformat(),
        'scope':'Current v9 work allocation using historical v7 diagnostic observations; no semantic or execution approval',
        'builder_sha256':sha256(__file__), 'input_sha256':inputs,
        'runtime_source_sha256':implementation_hashes(), 'beam_evidence_integrity':'pass',
        'shared_inputs_verified':shared['shared_prepared_bugs'], 'denominator':691,
        'selected':380, 'unsupported':311, 'owners':owners, 'bugs':bug_summary,
        'historical_diagnostic_counts':dict(Counter(r['historical_diagnostic_category'] for r in rows)),
        'structural_capability_additions_since_v7':additions,
        'fixture_development_groups':[{'owner':owner,'reason':reason,'affected_targets':len(targets),'targets':targets}
            for (owner,reason),targets in sorted(groups.items(),key=lambda x:(-len(x[1]),x[0]))],
        'unsupported_targets':rows, 'pending_empty_enum_targets':len(enum_rows),
        'enum_joint_decision_approved':False, 'original_314_closed':False,
        'gate_a_passed':False, 'primary_results_added':0, 'live_requests':0, 'queue_mutations':0,
        'request_floor':shared['request_reservation_formula'], 'final_prompt_reserve':None}


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--output', type=Path, required=True)
    args = parser.parse_args()
    report = build()
    with args.output.open('x',encoding='utf-8',newline='\n') as stream:
        json.dump(report,stream,ensure_ascii=False,indent=2)
        stream.write('\n')
    print(json.dumps({k:report[k] for k in ('selected','unsupported','owners','historical_diagnostic_counts')}))
