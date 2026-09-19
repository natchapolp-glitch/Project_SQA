#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/../lib/common.sh"
export TZ=America/Los_Angeles

if [[ $# -ne 4 ]]; then
  echo "Usage: $0 <comparison.json> <buggy-worktree> <fixed-worktree> <result-directory>" >&2
  exit 64
fi

comparison="$(absolute_path "$1")"
buggy_worktree="$(absolute_path "$2")"
fixed_worktree="$(absolute_path "$3")"
result_directory="$(absolute_path "$4")"
python_root="$PROJECT_ROOT/algorithms/python"
suite_generator="$python_root/generate_lang1_junit_suite.py"
test_class="GeneratedLang1RegressionTest"
source_file="$result_directory/$test_class.java"

mkdir -p "$result_directory/classes/buggy" "$result_directory/classes/fixed"
for expected in "$buggy_worktree:1b" "$fixed_worktree:1f"; do
  worktree="${expected%:*}"
  revision="${expected##*:}"
  [[ -f "$worktree/.defects4j.config" ]] && grep -Fxq 'pid=Lang' "$worktree/.defects4j.config" && grep -Fxq "vid=$revision" "$worktree/.defects4j.config" || { echo "Expected Lang-$revision checkout: $worktree" >&2; exit 65; }
done
python3 "$suite_generator" --comparison "$comparison" --output "$source_file" --class-name "$test_class" > "$result_directory/generation.json"
test_count="$(grep -c '^[[:space:]]*@Test$' "$source_file")"
[[ "$test_count" -gt 0 ]] || { echo "No generated JUnit tests" >&2; exit 1; }
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
printf 'test_count=%s\nbuggy_exit=%s\nfixed_exit=%s\n' "$test_count" "$buggy_exit" "$fixed_exit" > "$result_directory/validation.env"
[[ "$buggy_exit" -ne 0 && "$fixed_exit" -eq 0 ]] || {
  echo "Generated regression test did not fail on buggy and pass on fixed." >&2
  exit 1
}
grep -Fq "Tests run: $test_count,  Failures: $test_count" "$result_directory/buggy-junit.log" || { echo "Buggy run did not report assertion failures for every generated test." >&2; exit 1; }
[[ "$(grep -c 'ComparisonFailure' "$result_directory/buggy-junit.log")" -eq "$test_count" ]] || { echo "Buggy failures were not all JUnit comparison failures." >&2; exit 1; }
grep -Fq "OK ($test_count tests)" "$result_directory/fixed-junit.log" || { echo "Fixed run did not pass every generated test." >&2; exit 1; }
