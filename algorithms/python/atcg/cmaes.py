"""A small, dependency-free full-covariance CMA-ES minimiser."""

from __future__ import annotations

from dataclasses import dataclass
from math import exp, floor, log, sqrt
from random import Random
from typing import Callable, Sequence


@dataclass(frozen=True)
class CMAESConfig:
    lower_bounds: tuple[float, ...]
    upper_bounds: tuple[float, ...]
    sigma: float = 0.30
    population_size: int | None = None
    seed: int = 2026

    def __post_init__(self) -> None:
        if not self.lower_bounds or len(self.lower_bounds) != len(self.upper_bounds):
            raise ValueError("bounds must be non-empty and have the same dimension")
        if any(lo >= hi for lo, hi in zip(self.lower_bounds, self.upper_bounds)):
            raise ValueError("each lower bound must be strictly below its upper bound")
        if self.sigma <= 0:
            raise ValueError("sigma must be positive")
        if self.population_size is not None and self.population_size < 2:
            raise ValueError("population_size must be at least 2")


class CMAES:
    """Covariance Matrix Adaptation Evolution Strategy with box clamping.

    The implementation follows the rank-one/rank-mu covariance updates.  It is
    deliberately standard-library-only so experiment replays do not depend on
    a package manager or a particular NumPy build.
    """

    def __init__(self, config: CMAESConfig) -> None:
        self.config = config
        self.n = len(config.lower_bounds)
        self.rng = Random(config.seed)
        self.lambda_ = config.population_size or (4 + floor(3 * log(self.n)))
        self.mu = self.lambda_ // 2
        raw_weights = [log(self.mu + 0.5) - log(index + 1) for index in range(self.mu)]
        weight_sum = sum(raw_weights)
        self.weights = [weight / weight_sum for weight in raw_weights]
        self.mu_eff = 1.0 / sum(weight * weight for weight in self.weights)
        self.cc = (4 + self.mu_eff / self.n) / (self.n + 4 + 2 * self.mu_eff / self.n)
        self.cs = (self.mu_eff + 2) / (self.n + self.mu_eff + 5)
        self.c1 = 2 / ((self.n + 1.3) ** 2 + self.mu_eff)
        self.cmu = min(1 - self.c1, 2 * (self.mu_eff - 2 + 1 / self.mu_eff) / ((self.n + 2) ** 2 + self.mu_eff))
        self.damps = 1 + 2 * max(0.0, sqrt((self.mu_eff - 1) / (self.n + 1)) - 1) + self.cs
        self.chi_n = sqrt(self.n) * (1 - 1 / (4 * self.n) + 1 / (21 * self.n * self.n))
        self.mean = [(lo + hi) / 2 for lo, hi in zip(config.lower_bounds, config.upper_bounds)]
        self.sigma = config.sigma * sum(hi - lo for lo, hi in zip(config.lower_bounds, config.upper_bounds)) / self.n
        self.covariance = [[1.0 if row == col else 0.0 for col in range(self.n)] for row in range(self.n)]
        self.path_sigma = [0.0] * self.n
        self.path_c = [0.0] * self.n
        self.generation = 0

    def _cholesky(self) -> list[list[float]]:
        """Return a robust lower Cholesky factor, adding tiny diagonal jitter."""
        jitter = 1e-12
        for _ in range(8):
            lower = [[0.0] * self.n for _ in range(self.n)]
            valid = True
            for row in range(self.n):
                for col in range(row + 1):
                    value = self.covariance[row][col] + (jitter if row == col else 0.0)
                    value -= sum(lower[row][k] * lower[col][k] for k in range(col))
                    if row == col:
                        if value <= 0:
                            valid = False
                            break
                        lower[row][col] = sqrt(value)
                    else:
                        lower[row][col] = value / lower[col][col]
                if not valid:
                    break
            if valid:
                return lower
            jitter *= 10
        raise RuntimeError("covariance matrix is not positive definite")

    @staticmethod
    def _mat_vec(matrix: Sequence[Sequence[float]], vector: Sequence[float]) -> list[float]:
        return [sum(value * vector[col] for col, value in enumerate(row)) for row in matrix]

    @staticmethod
    def _forward_solve(lower: Sequence[Sequence[float]], vector: Sequence[float]) -> list[float]:
        result: list[float] = []
        for row, values in enumerate(lower):
            result.append((vector[row] - sum(values[col] * result[col] for col in range(row))) / values[row])
        return result

    def ask(self) -> list[tuple[list[float], list[float]]]:
        lower = self._cholesky()
        candidates = []
        for _ in range(self.lambda_):
            z = [self.rng.gauss(0.0, 1.0) for _ in range(self.n)]
            offset = self._mat_vec(lower, z)
            point = [
                min(hi, max(lo, mean + self.sigma * delta))
                for mean, delta, lo, hi in zip(self.mean, offset, self.config.lower_bounds, self.config.upper_bounds)
            ]
            candidates.append((point, z))
        return candidates

    def tell(self, evaluated: Sequence[tuple[Sequence[float], Sequence[float], float]]) -> None:
        if len(evaluated) != self.lambda_:
            raise ValueError("tell requires exactly one evaluated population")
        ranked = sorted(evaluated, key=lambda item: item[2])
        old_mean = self.mean[:]
        self.mean = [
            sum(self.weights[index] * ranked[index][0][dimension] for index in range(self.mu))
            for dimension in range(self.n)
        ]
        y_w = [(self.mean[index] - old_mean[index]) / self.sigma for index in range(self.n)]
        lower = self._cholesky()
        whitened = self._forward_solve(lower, y_w)
        scale_sigma = sqrt(self.cs * (2 - self.cs) * self.mu_eff)
        self.path_sigma = [(1 - self.cs) * path + scale_sigma * value for path, value in zip(self.path_sigma, whitened)]
        norm_path_sigma = sqrt(sum(value * value for value in self.path_sigma))
        threshold = 1.4 + 2 / (self.n + 1)
        h_sigma = norm_path_sigma / sqrt(1 - (1 - self.cs) ** (2 * (self.generation + 1))) / self.chi_n < threshold
        scale_c = sqrt(self.cc * (2 - self.cc) * self.mu_eff)
        self.path_c = [
            (1 - self.cc) * path + (scale_c * value if h_sigma else 0.0)
            for path, value in zip(self.path_c, y_w)
        ]
        previous = [row[:] for row in self.covariance]
        for row in range(self.n):
            for col in range(self.n):
                rank_mu = sum(
                    self.weights[index]
                    * ((ranked[index][0][row] - old_mean[row]) / self.sigma)
                    * ((ranked[index][0][col] - old_mean[col]) / self.sigma)
                    for index in range(self.mu)
                )
                self.covariance[row][col] = (
                    (1 - self.c1 - self.cmu) * previous[row][col]
                    + self.c1 * (self.path_c[row] * self.path_c[col] + (0 if h_sigma else self.cc * (2 - self.cc) * previous[row][col]))
                    + self.cmu * rank_mu
                )
        self.sigma *= exp((self.cs / self.damps) * (norm_path_sigma / self.chi_n - 1))
        self.generation += 1

    def minimize(self, objective: Callable[[Sequence[float]], float], generations: int) -> tuple[list[float], float]:
        if generations < 1:
            raise ValueError("generations must be positive")
        best_point: list[float] | None = None
        best_score = float("inf")
        for _ in range(generations):
            population = self.ask()
            evaluated = []
            for point, z in population:
                score = float(objective(point))
                if score < best_score:
                    best_point, best_score = point[:], score
                evaluated.append((point, z, score))
            self.tell(evaluated)
        assert best_point is not None
        return best_point, best_score
