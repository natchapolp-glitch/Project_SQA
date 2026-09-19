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
  bash "$metadata_script" "$work_dir" "$result_root/metadata/$label"
  bash "$baseline_script" "$work_dir" "$label" "$result_root"
done
