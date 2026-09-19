#!/usr/bin/env python3
"""Generate reproducible NumberUtils hexadecimal inputs from an ATCG algorithm."""

from __future__ import annotations

import argparse
import json
from pathlib import Path

from atcg import CMAES, CMAESConfig, FSCSART, FSCSARTConfig


def to_case(vector: tuple[float, float] | list[float]) -> dict[str, object]:
    leading_zeros = int(round(vector[0]))
    suffix_digits = int(round(vector[1]))
    value = "0x" + ("0" * leading_zeros) + "1" + ("0" * suffix_digits)
    return {"leading_zeros": leading_zeros, "suffix_digits": suffix_digits, "input": value}


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("--algorithm", choices=("fscs-art", "cmaes"), required=True)
    parser.add_argument("--budget", type=int, default=30)
    parser.add_argument("--seed", type=int, default=2026)
    parser.add_argument("--output", type=Path, required=True)
    args = parser.parse_args()
    if args.budget < 1:
        parser.error("--budget must be positive")
    if args.algorithm == "fscs-art":
        generator = FSCSART(FSCSARTConfig((0.0, 0.0), (32.0, 20.0), candidate_set_size=10, seed=args.seed))
        vectors = generator.generate(args.budget)
    else:
        strategy = CMAES(CMAESConfig((0.0, 0.0), (32.0, 20.0), seed=args.seed))
        candidates: list[list[float]] = []
        while len(candidates) < args.budget:
            population = strategy.ask()
            # The objective focuses the search on the Integer/Long/BigInteger
            # classification boundaries; the oracle below remains the source of truth.
            scored = []
            for point, z in population:
                total_digits = round(point[0]) + 1 + round(point[1])
                score = min((total_digits - 9) ** 2, (total_digits - 17) ** 2)
                scored.append((point, z, float(score)))
                candidates.append(point)
            strategy.tell(scored)
        vectors = [tuple(point) for point in candidates[: args.budget]]
    payload = {"algorithm": args.algorithm, "seed": args.seed, "budget": args.budget, "cases": [to_case(vector) for vector in vectors]}
    args.output.parent.mkdir(parents=True, exist_ok=True)
    args.output.write_text(json.dumps(payload, indent=2) + "\n", encoding="utf-8")


if __name__ == "__main__":
    main()
