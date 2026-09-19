#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/../lib/common.sh"

if [[ $# -lt 5 ]]; then
  echo "Usage: $0 <fscs-art|cmaes> <buggy-worktree> <fixed-worktree> <result-directory> <budget> [--seed N] [--candidate-set-size N] [--sigma N]" >&2
  exit 64
fi

original_args=("$@")
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

require_lang1_checkout() {
  local worktree="$1" revision="$2"
  [[ -f "$worktree/.defects4j.config" ]] && grep -Fxq 'pid=Lang' "$worktree/.defects4j.config" && grep -Fxq "vid=$revision" "$worktree/.defects4j.config" || { echo "Expected Lang-$revision checkout: $worktree" >&2; exit 65; }
}

oracle_source="$PROJECT_ROOT/experiments/targets/lang1_numberutils/NumberUtilsOracle.java"

if [[ -e "$result_directory" ]] && [[ -n "$(find "$result_directory" -mindepth 1 -maxdepth 1 -print -quit)" ]]; then
  echo "Refusing to overwrite existing run directory: $result_directory" >&2
  exit 73
fi
require_lang1_checkout "$buggy_worktree" 1b
require_lang1_checkout "$fixed_worktree" 1f
mkdir -p "$result_directory/harness/buggy" "$result_directory/harness/fixed"
[[ "$budget" =~ ^[1-9][0-9]*$ ]] || { echo "budget must be a positive integer" >&2; exit 64; }
[[ "$seed" =~ ^[0-9]+$ ]] || { echo "seed must be a non-negative integer" >&2; exit 64; }
[[ "$candidate_set_size" =~ ^[1-9][0-9]*$ ]] || { echo "candidate-set-size must be a positive integer" >&2; exit 64; }
case "$algorithm" in fscs-art|cmaes) ;; *) echo "algorithm must be fscs-art or cmaes" >&2; exit 64;; esac

buggy_cp="$(run_defects4j export -w "$buggy_worktree" -p cp.compile)"
fixed_cp="$(run_defects4j export -w "$fixed_worktree" -p cp.compile)"
javac -cp "$buggy_cp" -d "$result_directory/harness/buggy" "$oracle_source"
javac -cp "$fixed_cp" -d "$result_directory/harness/fixed" "$oracle_source"
command_line="$(printf '%q ' "$0" "${original_args[@]}")"
repository_revision="$(git -C "$PROJECT_ROOT" rev-parse HEAD 2>/dev/null || printf unknown)"
d4j_version="$(git -C "$D4J_HOME" rev-parse HEAD 2>/dev/null || printf unknown)"
java_version="$(java -version 2>&1 | head -n 1)"
printf 'project=Lang\nbug_id=1\nbuggy_revision=1b\nfixed_revision=1f\nrepository_revision=%s\nbuggy_git_head=%s\nfixed_git_head=%s\ndefects4j_revision=%s\njava_version=%s\nalgorithm=%s\nseed=%s\nbudget=%s\ncandidate_set_size=%s\nsigma=%s\nstarted_at=%s\ncommand=%s\n' "$repository_revision" "$(git -C "$buggy_worktree" rev-parse HEAD)" "$(git -C "$fixed_worktree" rev-parse HEAD)" "$d4j_version" "$java_version" "$algorithm" "$seed" "$budget" "$candidate_set_size" "$sigma" "$(date --iso-8601=seconds)" "$command_line" > "$result_directory/run.env"
python3 "$python_root/generate_lang1_inputs.py" --algorithm "$algorithm" --budget "$budget" \
  --seed "$seed" --candidate-set-size "$candidate_set_size" --sigma "$sigma" \
  --output "$result_directory/inputs.json"
python3 "$python_root/differential_lang1.py" \
  --inputs "$result_directory/inputs.json" \
  --buggy-classpath "$buggy_cp" --fixed-classpath "$fixed_cp" \
  --buggy-harness "$result_directory/harness/buggy" --fixed-harness "$result_directory/harness/fixed" \
  --output "$result_directory"
printf 'ended_at=%s\n' "$(date --iso-8601=seconds)" >> "$result_directory/run.env"
