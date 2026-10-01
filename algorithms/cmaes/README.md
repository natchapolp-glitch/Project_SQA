# CMA-ES implementation

The standard-library implementation is `algorithms/python/atcg/cmaes.py`.
It maintains a full covariance matrix, rank-one/rank-mu updates, evolution paths,
and adaptive step size. Sampling uses a Cholesky factor with diagonal jitter;
points are clamped to the configured box bounds.

The Round 2 adapter in `scripts/study/generate.py` uses normalized bounds [-1, 1],
sigma configuration 0.30 (initial absolute sigma 0.60), and population size
4 + floor(3 ln(n)), where n is the maximum target dimension plus one API-selector
coordinate. It minimizes the frequency of each observed fixed behavior. This
is a diversity proxy, not a branch-distance or coverage fitness. The adapter
never evaluates a population beyond the 30 proposed-input budget.

Seeds, source hashes, target declarations, proposed/retained inputs, fixed
observations, JUnit archives and evaluation logs are preserved in the v4 batch.
See `docs/study-protocol.md` for validation, sampling and oracle limitations.
