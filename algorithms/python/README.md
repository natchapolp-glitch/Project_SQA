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
and stores inputs plus b/f comparisons.  A difference is an observed behavioural
difference; it is not by itself a claim that every generated input is a valid
JUnit regression test.
