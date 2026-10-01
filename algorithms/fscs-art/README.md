# FSCS-ART implementation

The implementation is `algorithms/python/atcg/fscs_art.py`. The first vector is
uniformly sampled. Each later vector is selected from 10 candidates as the one
with the largest distance to its nearest previously selected vector. Distance
is Euclidean after scaling each coordinate by its domain width.

The Round 2 adapter in `scripts/study/generate.py` uses bounds [-1, 1], a target
selector plus bounded fixture coordinates, seeds 101/102/103 and 30 proposed
inputs. It does not use fixed/buggy differences to select inputs. Two fixed JVM
observations supply stable expected outcomes before JUnit validation.

Vectors may decode to duplicate Java arguments, and the reflection fixture
adapter limits useful API coverage. Raw observations, retained tests, archives,
logs and measured coverage are in `results/study/round2-v4-20260929/`.
