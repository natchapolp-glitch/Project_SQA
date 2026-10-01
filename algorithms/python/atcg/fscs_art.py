"""Fixed-size candidate set adaptive random testing (FSCS-ART)."""

from __future__ import annotations

from dataclasses import dataclass
from math import sqrt
from random import Random
from typing import Iterable, Sequence


@dataclass(frozen=True)
class FSCSARTConfig:
    lower_bounds: tuple[float, ...]
    upper_bounds: tuple[float, ...]
    candidate_set_size: int = 10
    seed: int = 2026

    def __post_init__(self) -> None:
        if not self.lower_bounds or len(self.lower_bounds) != len(self.upper_bounds):
            raise ValueError("bounds must be non-empty and have the same dimension")
        if any(lo > hi for lo, hi in zip(self.lower_bounds, self.upper_bounds)):
            raise ValueError("each lower bound must not exceed its upper bound")
        if self.candidate_set_size < 1:
            raise ValueError("candidate_set_size must be positive")


class FSCSART:
    """Generate evenly spread test vectors over a bounded numeric domain.

    Each new point is selected from a random candidate set as the candidate
    whose nearest previously selected point is farthest away.  Coordinates are
    normalised by their domain width before computing Euclidean distance.
    """

    def __init__(self, config: FSCSARTConfig) -> None:
        self.config = config
        self._rng = Random(config.seed)
        self.selected: list[tuple[float, ...]] = []

    def _sample(self) -> tuple[float, ...]:
        return tuple(
            self._rng.uniform(lo, hi)
            for lo, hi in zip(self.config.lower_bounds, self.config.upper_bounds)
        )

    def _distance(self, left: Sequence[float], right: Sequence[float]) -> float:
        total = 0.0
        for value_l, value_r, lo, hi in zip(
            left, right, self.config.lower_bounds, self.config.upper_bounds
        ):
            width = hi - lo
            delta = 0.0 if width == 0 else (value_l - value_r) / width
            total += delta * delta
        return sqrt(total)

    def next_case(self) -> tuple[float, ...]:
        if not self.selected:
            point = self._sample()
        else:
            candidates = [self._sample() for _ in range(self.config.candidate_set_size)]
            point = max(
                candidates,
                key=lambda candidate: min(
                    self._distance(candidate, previous) for previous in self.selected
                ),
            )
        self.selected.append(point)
        return point

    def generate(self, budget: int) -> list[tuple[float, ...]]:
        if budget < 0:
            raise ValueError("budget must not be negative")
        return [self.next_case() for _ in range(budget)]
