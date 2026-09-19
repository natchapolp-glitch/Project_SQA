#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/../lib/common.sh"

if [[ $# -ne 4 ]]; then
  echo "Usage: $0 <comparison.json> <buggy-worktree> <fixed-worktree> <result-directory>" >&2
  exit 64
fi

comparison="$(absolute_path "$1")"
buggy_worktree="$(absolute_path "$2")"
fixed_worktree="$(absolute_path "$3")"
result_directory="$(absolute_path "$4")"
python_root="$PROJECT_ROOT/algorithms/python"
test_class="GeneratedLang1RegressionTest"
source_file="$result_directory/$test_class.java"

mkdir -p "$result_directory/classes/buggy" "$result_directory/classes/fixed"
python3 "$python_root/generate_lang1_junit.py" --comparison "$comparison" --output "$source_file" --class-name "$test_class"
buggy_cp="$(run_defects4j export -w "$buggy_worktree" -p cp.test)"
fixed_cp="$(run_defects4j export -w "$fixed_worktree" -p cp.test)"
javac -cp "$buggy_cp" -d "$result_directory/classes/buggy" "$source_file"
javac -cp "$fixed_cp" -d "$result_directory/classes/fixed" "$source_file"

set +e
java -cp "$buggy_cp:$result_directory/classes/buggy" org.junit.runner.JUnitCore "$test_class" > "$result_directory/buggy-junit.log" 2>&1
buggy_exit=$?
java -cp "$fixed_cp:$result_directory/classes/fixed" org.junit.runner.JUnitCore "$test_class" > "$result_directory/fixed-junit.log" 2>&1
fixed_exit=$?
set -e
printf 'buggy_exit=%s\nfixed_exit=%s\n' "$buggy_exit" "$fixed_exit" > "$result_directory/validation.env"
[[ "$buggy_exit" -ne 0 && "$fixed_exit" -eq 0 ]] || {
  echo "Generated regression test did not fail on buggy and pass on fixed." >&2
  exit 1
}
