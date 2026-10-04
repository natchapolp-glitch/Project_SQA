# Full-scope experiment status

Offline pilot only; no new KKU requests.

```json
{
  "planned_bugs": 854,
  "planned_jobs": 3416,
  "attempted_jobs": 6,
  "provider_requested_jobs": 0,
  "evaluated_jobs": 6,
  "states": {
    "PENDING": 3410,
    "DONE": 6
  },
  "new_api_requests": 0,
  "condition": "solo-offline-pilot.v1",
  "phase": "offline algorithms/context; AI workflow pending",
  "per_method": {
    "cmaes": {
      "PENDING": 851,
      "DONE": 3
    },
    "fscs-art": {
      "PENDING": 851,
      "DONE": 3
    },
    "kku-claude": {
      "PENDING": 854
    },
    "kku-gemini": {
      "PENDING": 854
    }
  }
}
```

Missing metrics are empty/null, not zero. Historical suites are not pooled. Fixed observations are used by algorithms; AI context is buggy-only. Generated method count is not a verified executed-test count.
