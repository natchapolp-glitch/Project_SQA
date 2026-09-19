#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
# shellcheck source=../lib/common.sh
source "$SCRIPT_DIR/../lib/common.sh"

if [[ $# -ne 2 ]]; then
  echo "Usage: $0 <working-copy> <output-directory>" >&2
  exit 64
fi

working_copy="$(absolute_path "$1")"
output_directory="$(absolute_path "$2")"
mkdir -p "$output_directory"
cd "$working_copy"

for property in classes.modified classes.relevant cp.compile cp.test dir.src.classes dir.src.tests tests.all tests.relevant tests.trigger; do
  run_defects4j export -p "$property" -o "$output_directory/$property.txt"
done
