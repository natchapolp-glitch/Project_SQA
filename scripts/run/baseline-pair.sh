#!/usr/bin/env bash
set -euo pipefail

if [[ $# -ne 2 ]]; then
  echo "Usage: $0 <project-id> <bug-id>" >&2
  exit 64
fi

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
# shellcheck source=../lib/common.sh
source "$SCRIPT_DIR/../lib/common.sh"

project="$1"
bug_id="$2"
work_root="$PROJECT_ROOT/worktrees/$project/$bug_id"
result_root="$PROJECT_ROOT/results/raw/$project/$bug_id"
checkout_script="$PROJECT_ROOT/scripts/checkout/checkout-bug.sh"
metadata_script="$PROJECT_ROOT/scripts/checkout/export-metadata.sh"
baseline_script="$PROJECT_ROOT/scripts/run/baseline.sh"

mkdir -p "$result_root"
printf 'project=%s\nbug_id=%s\ntimezone=%s\ndefects4j_bin=%s\n' \
  "$project" "$bug_id" "America/Los_Angeles" "$D4J_BIN" > "$result_root/run-manifest.env"

for revision in b f; do
  work_dir="$work_root/$revision"
  label="${bug_id}${revision}"
  if [[ ! -d "$work_dir" ]]; then
    bash "$checkout_script" "$D4J_HOME" "$project" "$bug_id" "$revision" "$work_dir"
  fi
  [[ -f "$work_dir/.defects4j.config" ]] || { echo "Missing Defects4J configuration: $work_dir" >&2; exit 65; }
  grep -Fxq "pid=$project" "$work_dir/.defects4j.config" || { echo "Checkout project does not match: $work_dir" >&2; exit 65; }
  grep -Fxq "vid=${bug_id}${revision}" "$work_dir/.defects4j.config" || { echo "Checkout revision does not match: $work_dir" >&2; exit 65; }
  printf 'revision=%s\ngit_head=%s\nstarted_at=%s\n' "$label" "$(git -C "$work_dir" rev-parse HEAD)" "$(date --iso-8601=seconds)" > "$result_root/run-${label}.env"
  bash "$metadata_script" "$work_dir" "$result_root/metadata/$label"
  bash "$baseline_script" "$work_dir" "$label" "$result_root"
done
