#!/usr/bin/env bash
# Run inside initialized Ubuntu; keep checkouts on the Linux filesystem.
set -euo pipefail
if [[ "$(uname -s)" != Linux ]]; then echo 'Linux/WSL required' >&2; exit 1; fi
repo_dir="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")/../.." && pwd)"
beam_d4j_root="${1:-$HOME/sqa-beam/defects4j}"
export D4J_HOME="$beam_d4j_root"
if [[ "${BEAM_SKIP_SYSTEM_INSTALL:-0}" != 1 ]]; then
  sudo apt-get update
  sudo apt-get install -y openjdk-11-jdk git subversion perl cpanminus python3 ant unzip curl build-essential
fi
beam_java_home="$(dirname -- "$(dirname -- "$(dpkg -L openjdk-11-jdk-headless | sed -n '/\/bin\/javac$/p' | head -n 1)")")"
[[ -x "$beam_java_home/bin/java" ]] || { echo 'Java11 path unavailable' >&2; exit 1; }
export JAVA_HOME="$beam_java_home"
export PATH="$JAVA_HOME/bin:$PATH"
if [[ ! -d "$beam_d4j_root" ]]; then
  mkdir -p -- "$(dirname -- "$beam_d4j_root")"
  git clone --branch v3.0.1 --depth 1 https://github.com/rjust/defects4j.git "$beam_d4j_root"
fi
[[ "$(git -C "$beam_d4j_root" describe --tags --exact-match)" == v3.0.1 ]] || { echo 'Require Defects4J v3.0.1; will not replace another installation' >&2; exit 1; }
cd -- "$beam_d4j_root"
cpanm --local-lib-contained "$beam_d4j_root/.perl5" --installdeps .
export PERL5LIB="$beam_d4j_root/.perl5/lib/perl5${PERL5LIB:+:$PERL5LIB}"
bash ./init.sh
mkdir -p -- "$HOME/sqa-beam"
{
  printf 'export JAVA_HOME=%q\n' "$JAVA_HOME"
  printf 'export D4J_HOME=%q\n' "$beam_d4j_root"
  printf 'export PERL5LIB=%q\n' "$PERL5LIB"
  printf 'export PATH="$JAVA_HOME/bin:$D4J_HOME/framework/bin:$PATH"\n'
  printf 'export TZ=America/Los_Angeles\n'
} > "$HOME/sqa-beam/environment.sh"
cd -- "$repo_dir"
python3 -m scripts.study.api854.environment --d4j "$D4J_HOME/framework/bin/defects4j" \
  --output "output/api854-beam/environment-$(date -u +%Y%m%dT%H%M%SZ)"
