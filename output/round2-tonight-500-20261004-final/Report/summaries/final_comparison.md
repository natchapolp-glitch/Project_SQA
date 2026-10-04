# Measured experiment summary

Snapshot: 2026-10-04T14:44:13.524138+00:00

Planned: 500 bugs / 2000 jobs. Recorded: 130 bugs / 17 projects. Installed inventory: 854 bugs; this report's chosen scope is separate. Not all planned cases were attempted. DONE means complete evaluation, not fault detection.

| name | attempted_cases | successfully_evaluated | fault_detection_denominator | fault_detecting_cases | fault_detection_percent | line_coverage_cases | mean_fixed_line_coverage_percent | condition_coverage_cases | mean_fixed_condition_coverage_percent |
|---|---|---|---|---|---|---|---|---|---|
| CMA-ES | 130 | 75 | 75 | 10 | 13.33 | 75 | 35.74 | 74 | 23.63 |
| FSCS-ART | 130 | 75 | 75 | 9 | 12.00 | 75 | 35.62 | 74 | 22.64 |
| KKU Sonnet 5 | 68 | 27 | 27 | 3 | 11.11 | 27 | 52.94 | 27 | 44.49 |
| KKU Gemini 3.5 Flash Lite | 68 | 26 | 26 | 3 | 11.54 | 26 | 60.05 | 26 | 49.84 |

CMA-ES states: {'DONE': 75, 'UNSUPPORTED': 42, 'PENDING': 370, 'INFRA_ERROR': 10, 'INVALID_GENERATED_SUITE': 3}

FSCS-ART states: {'DONE': 75, 'UNSUPPORTED': 42, 'PENDING': 370, 'INFRA_ERROR': 10, 'INVALID_GENERATED_SUITE': 3}

KKU Sonnet 5 states: {'DONE': 27, 'PENDING': 432, 'INFRA_ERROR': 4, 'INVALID_AFTER_REPAIR': 26, 'OUTPUT_INCOMPLETE': 2, 'QUOTA_PAUSED': 9}

KKU Gemini 3.5 Flash Lite states: {'INVALID_AFTER_REPAIR': 35, 'DONE': 26, 'PENDING': 432, 'INFRA_ERROR': 7}

- Snapshot includes sealed outcomes only; dispatched jobs still running are not yet terminal results.
- One generation per method/bug; fixed repeats are validation, not independent repeats.
- Pilot and compact AI protocols are labelled separately; aggregate means are descriptive only.
- Algorithms use fixed-reference oracles; AI uses buggy context and fixed feedback.
- Missing measurements remain null; infrastructure/quota errors are not detected faults.
