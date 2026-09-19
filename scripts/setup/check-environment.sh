#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
# shellcheck source=../lib/common.sh
source "$SCRIPT_DIR/../lib/common.sh"

required=(git svn perl cpanm)
missing=0
for command in "${required[@]}"; do
  if ! command -v "$command" >/dev/null 2>&1; then
    printf 'MISSING: %s\n' "$command" >&2
    missing=1
    continue
  fi
  printf 'FOUND: %s -> %s\n' "$command" "$(command -v "$command")"
done

if ! command -v java >/dev/null 2>&1; then
  printf 'MISSING: java (Java 11 is required)\n' >&2
  exit 1
fi

java_version="$(java -version 2>&1 | awk -F '[\".]' '/version/ {print $2; exit}')"
if [[ "$java_version" != "11" ]]; then
  printf 'Java 11 is required; detected Java %s.\n' "${java_version:-unknown}" >&2
  missing=1
fi

printf 'Java: %s\n' "$(java -version 2>&1 | head -n 1)"
if [[ ! -x "$D4J_BIN" ]]; then
  printf 'MISSING: defects4j executable at %s\n' "$D4J_BIN" >&2
  missing=1
else
  printf 'FOUND: defects4j -> %s\n' "$D4J_BIN"
  run_defects4j pids >/dev/null
  printf 'Defects4J: command interface verified\n'
fi
printf 'Timezone required for runs: America/Los_Angeles\n'
