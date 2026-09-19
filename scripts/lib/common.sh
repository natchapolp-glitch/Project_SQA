#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd -- "$SCRIPT_DIR/../.." && pwd)"
D4J_HOME="${D4J_HOME:-$PROJECT_ROOT/defects4j}"
D4J_BIN="${D4J_BIN:-$D4J_HOME/framework/bin/defects4j}"

absolute_path() {
  realpath -m -- "$1"
}

require_defects4j() {
  [[ -x "$D4J_BIN" ]] || {
    echo "Defects4J executable not found: $D4J_BIN" >&2
    exit 1
  }
}

run_defects4j() {
  require_defects4j
  "$D4J_BIN" "$@"
}
