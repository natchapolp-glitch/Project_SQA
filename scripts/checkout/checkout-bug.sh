#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
# shellcheck source=../lib/common.sh
source "$SCRIPT_DIR/../lib/common.sh"

if [[ $# -ne 5 ]]; then
  echo "Usage: $0 <defects4j-home> <project-id> <bug-id> <b|f> <work-dir>" >&2
  exit 64
fi

d4j_home="$(absolute_path "$1")"
project="$2"
bug_id="$3"
revision="$4"
work_dir="$(absolute_path "$5")"
d4j_bin="$d4j_home/framework/bin/defects4j"

[[ -x "$d4j_bin" ]] || { echo "Defects4J executable not found: $d4j_bin" >&2; exit 1; }
[[ "$revision" == "b" || "$revision" == "f" ]] || { echo "Revision must be b or f" >&2; exit 64; }
[[ ! -e "$work_dir" ]] || { echo "Refusing to overwrite existing directory: $work_dir" >&2; exit 1; }

export TZ=America/Los_Angeles
"$d4j_bin" checkout -p "$project" -v "${bug_id}${revision}" -w "$work_dir"
