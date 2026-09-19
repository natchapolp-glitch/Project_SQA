# Reproducible numeric generators

This package contains the two requested non-AI generators, implemented without
third-party runtime dependencies:

- `atcg.fscs_art.FSCSART` implements fixed-size-candidate-set adaptive random
  testing.  It chooses the random candidate furthest from its nearest selected
  predecessor in normalised Euclidean space.
- `atcg.cmaes.CMAES` implements full-covariance CMA-ES with rank-one and
  rank-mu covariance updates and deterministic seeds.

Run their unit tests from WSL:

```bash
PYTHONPATH=algorithms/python python3 -m unittest discover -s algorithms/python/tests -v
```

The `Lang-1` pilot maps a two-dimensional numeric vector to a hexadecimal
string for `NumberUtils.createNumber`.  `scripts/run/lang1-numberutils-differential.sh`
compiles a tiny oracle for both Defects4J revisions, runs the selected generator,
and stores inputs plus b/f comparisons. `scripts/run/run-lang1-smoke.py` consumes
the smoke YAML configuration and passes its seeds and algorithm parameters to the
runner. `scripts/run/validate-lang1-regression-test.sh` then turns one observed
fixed-value difference into a standalone JUnit test and requires it to fail on
the buggy revision and pass on the fixed revision.
