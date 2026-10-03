"""Retain a post-evaluation semantic review as a separate immutable artifact."""
import argparse
from pathlib import Path

from .common import read_json, sha256, write_json
from .evaluate_worker import review_validity


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--evaluation", required=True, type=Path)
    parser.add_argument("--review", required=True, type=Path)
    args = parser.parse_args()
    result_path = args.evaluation / "result.json"
    result = read_json(result_path)
    if not result.get("evaluation_attempted") or not result.get("measurement"):
        parser.error("No evaluation measurements to review")
    review = review_validity(args.review, result["suite_sha256"], result["fixed_source_sha256"],
                            result["measurement"], args.evaluation)
    output = args.evaluation.parent / "semantic-review"
    output.mkdir(exist_ok=False)
    write_json(output / "review.json", {"job": result["job"], "evaluation_result_sha256": sha256(result_path), **review})
    print(f"usable={review['usable']}; retained={output / 'review.json'} (original evaluation unchanged)")


if __name__ == "__main__":
    main()
