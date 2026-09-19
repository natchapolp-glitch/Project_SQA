#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
# shellcheck source=../lib/common.sh
source "$SCRIPT_DIR/../lib/common.sh"

[[ -x "$D4J_HOME/init.sh" ]] || {
  echo "Defects4J init script not found: $D4J_HOME/init.sh" >&2
  exit 1
}

cd "$D4J_HOME"
exec /bin/bash ./init.sh
