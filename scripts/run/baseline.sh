#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
# shellcheck source=../lib/common.sh
source "$SCRIPT_DIR/../lib/common.sh"

if [[ $# -ne 3 ]]; then
  echo "Usage: $0 <working-copy> <buggy|fixed> <result-directory>" >&2
  exit 64
fi

working_copy="$(absolute_path "$1")"
revision_label="$2"
result_directory="$(absolute_path "$3")"
mkdir -p "$result_directory"

export TZ=America/Los_Angeles
cd "$working_copy"
run_defects4j compile 2>&1 | tee "$result_directory/${revision_label}-compile.log"
run_defects4j test 2>&1 | tee "$result_directory/${revision_label}-test.log"
