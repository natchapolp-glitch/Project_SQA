# Measured experiment summary

Snapshot: 2026-10-04T14:44:13.524138+00:00

Planned: 500 bugs / 2000 jobs. Recorded: 130 bugs / 17 projects. Installed inventory: 854 bugs; this report's chosen scope is separate. Not all planned cases were attempted. DONE means complete evaluation, not fault detection.

| name | attempted_cases | successfully_evaluated | fault_detection_denominator | fault_detecting_cases | fault_detection_percent | line_coverage_cases | mean_fixed_line_coverage_percent | condition_coverage_cases | mean_fixed_condition_coverage_percent |
|---|---|---|---|---|---|---|---|---|---|
| FSCS-ART | 130 | 75 | 75 | 9 | 12.00 | 75 | 35.62 | 74 | 22.64 |

FSCS-ART states: {'DONE': 75, 'UNSUPPORTED': 42, 'PENDING': 370, 'INFRA_ERROR': 10, 'INVALID_GENERATED_SUITE': 3}

- Snapshot includes sealed outcomes only; dispatched jobs still running are not yet terminal results.
- One generation per method/bug; fixed repeats are validation, not independent repeats.
- Pilot and compact AI protocols are labelled separately; aggregate means are descriptive only.
- Algorithms use fixed-reference oracles; AI uses buggy context and fixed feedback.
- Missing measurements remain null; infrastructure/quota errors are not detected faults.
