#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/../lib/common.sh"

if [[ $# -lt 5 ]]; then
  echo "Usage: $0 <fscs-art|cmaes> <buggy-worktree> <fixed-worktree> <result-directory> <budget> [--seed N] [--candidate-set-size N] [--sigma N]" >&2
  exit 64
fi

algorithm="$1"
buggy_worktree="$(absolute_path "$2")"
fixed_worktree="$(absolute_path "$3")"
result_directory="$(absolute_path "$4")"
budget="$5"
shift 5
seed=2026
candidate_set_size=10
sigma=0.30
while [[ $# -gt 0 ]]; do
  case "$1" in
    --seed)
      seed="${2:?missing value for --seed}"
      shift 2
      ;;
    --candidate-set-size)
      candidate_set_size="${2:?missing value for --candidate-set-size}"
      shift 2
      ;;
    --sigma)
      sigma="${2:?missing value for --sigma}"
      shift 2
      ;;
    *)
      echo "Unknown option: $1" >&2
      exit 64
      ;;
  esac
done
python_root="$PROJECT_ROOT/algorithms/python"
oracle_source="$PROJECT_ROOT/experiments/targets/lang1_numberutils/NumberUtilsOracle.java"

mkdir -p "$result_directory/harness/buggy" "$result_directory/harness/fixed"
[[ -f "$buggy_worktree/.defects4j.config" && -f "$fixed_worktree/.defects4j.config" ]] || { echo "Both worktrees must be Defects4J checkouts." >&2; exit 65; }
[[ "$budget" =~ ^[1-9][0-9]*$ ]] || { echo "budget must be a positive integer" >&2; exit 64; }
[[ "$seed" =~ ^[0-9]+$ ]] || { echo "seed must be a non-negative integer" >&2; exit 64; }
[[ "$candidate_set_size" =~ ^[1-9][0-9]*$ ]] || { echo "candidate-set-size must be a positive integer" >&2; exit 64; }
case "$algorithm" in fscs-art|cmaes) ;; *) echo "algorithm must be fscs-art or cmaes" >&2; exit 64;; esac

buggy_cp="$(run_defects4j export -w "$buggy_worktree" -p cp.compile)"
fixed_cp="$(run_defects4j export -w "$fixed_worktree" -p cp.compile)"
javac -cp "$buggy_cp" -d "$result_directory/harness/buggy" "$oracle_source"
javac -cp "$fixed_cp" -d "$result_directory/harness/fixed" "$oracle_source"
printf 'algorithm=%s\nseed=%s\nbudget=%s\ncandidate_set_size=%s\nsigma=%s\nstarted_at=%s\n' \
  "$algorithm" "$seed" "$budget" "$candidate_set_size" "$sigma" "$(date --iso-8601=seconds)" > "$result_directory/run.env"
python3 "$python_root/generate_lang1_inputs.py" --algorithm "$algorithm" --budget "$budget" \
  --seed "$seed" --candidate-set-size "$candidate_set_size" --sigma "$sigma" \
  --output "$result_directory/inputs.json"
python3 "$python_root/differential_lang1.py" \
  --inputs "$result_directory/inputs.json" \
  --buggy-classpath "$buggy_cp" --fixed-classpath "$fixed_cp" \
  --buggy-harness "$result_directory/harness/buggy" --fixed-harness "$result_directory/harness/fixed" \
  --output "$result_directory"
