#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/../lib/common.sh"

if [[ $# -ne 5 ]]; then
  echo "Usage: $0 <fscs-art|cmaes> <buggy-worktree> <fixed-worktree> <result-directory> <budget>" >&2
  exit 64
fi

algorithm="$1"
buggy_worktree="$(absolute_path "$2")"
fixed_worktree="$(absolute_path "$3")"
result_directory="$(absolute_path "$4")"
budget="$5"
python_root="$PROJECT_ROOT/algorithms/python"
oracle_source="$PROJECT_ROOT/experiments/targets/lang1_numberutils/NumberUtilsOracle.java"

mkdir -p "$result_directory/harness/buggy" "$result_directory/harness/fixed"
[[ -f "$buggy_worktree/.defects4j.config" && -f "$fixed_worktree/.defects4j.config" ]] || { echo "Both worktrees must be Defects4J checkouts." >&2; exit 65; }
[[ "$budget" =~ ^[1-9][0-9]*$ ]] || { echo "budget must be a positive integer" >&2; exit 64; }

buggy_cp="$(run_defects4j export -w "$buggy_worktree" -p cp.compile)"
fixed_cp="$(run_defects4j export -w "$fixed_worktree" -p cp.compile)"
javac -cp "$buggy_cp" -d "$result_directory/harness/buggy" "$oracle_source"
javac -cp "$fixed_cp" -d "$result_directory/harness/fixed" "$oracle_source"
python3 "$python_root/generate_lang1_inputs.py" --algorithm "$algorithm" --budget "$budget" --output "$result_directory/inputs.json"
python3 "$python_root/differential_lang1.py" \
  --inputs "$result_directory/inputs.json" \
  --buggy-classpath "$buggy_cp" --fixed-classpath "$fixed_cp" \
  --buggy-harness "$result_directory/harness/buggy" --fixed-harness "$result_directory/harness/fixed" \
  --output "$result_directory"
