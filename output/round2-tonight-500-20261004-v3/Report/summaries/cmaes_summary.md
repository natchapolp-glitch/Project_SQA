# Measured experiment summary

Snapshot: 2026-10-04T04:41:03.810754+00:00

Planned: 500 bugs / 2000 jobs. Recorded: 17 bugs / 17 projects. Installed inventory: 854 bugs; this report's chosen scope is separate. Not all planned cases were attempted. DONE means complete evaluation, not fault detection.

| name | attempted_cases | successfully_evaluated | fault_detection_denominator | fault_detecting_cases | fault_detection_percent | line_coverage_cases | mean_fixed_line_coverage_percent | condition_coverage_cases | mean_fixed_condition_coverage_percent |
|---|---|---|---|---|---|---|---|---|---|
| CMA-ES | 17 | 15 | 15 | 2 | 13.33 | 15 | 45.62 | 15 | 28.59 |

CMA-ES states: {'DONE': 15, 'PENDING': 483, 'INFRA_ERROR': 1, 'INVALID_GENERATED_SUITE': 1}

- Snapshot includes sealed outcomes only; dispatched jobs still running are not yet terminal results.
- One generation per method/bug; fixed repeats are validation, not independent repeats.
- Pilot and compact AI protocols are labelled separately; aggregate means are descriptive only.
- Algorithms use fixed-reference oracles; AI uses buggy context and fixed feedback.
- Missing measurements remain null; infrastructure/quota errors are not detected faults.
