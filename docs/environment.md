# Environment baseline

## Target runtime

Use Ubuntu on WSL2 for all Defects4J execution. The project target is Defects4J
3.0.1, which documents Java 11 as its supported Java version. Set
`TZ=America/Los_Angeles` whenever tests are generated or run.

## Observed on 2026-09-19

| Environment | Available | Missing for Defects4J |
|---|---|---|
| Windows host | Git; Java 17 | Java 11, Subversion, Perl, Defects4J |
| Ubuntu WSL | Git; Perl; Java 11; Subversion; cpanminus; Defects4J 3.0.1 | None |

Java 17 must not be silently substituted for Java 11 because it can change test
reproducibility. The setup command should make Java 11 the active `java` before
the experiment begins.

## Required verification

Run the following in WSL after installation:

```bash
bash scripts/setup/check-environment.sh
```

It must report Java major version 11, Git, Subversion, Perl, `cpanm`, and an
executable `defects4j` command. Save its output with each experiment setup.

## Installation boundary

Installation downloads project repositories and external libraries and can occupy
substantial disk space. The current workspace has been initialized; for a new
workspace, run `bash scripts/setup/start-defects4j-init.sh` after confirming the
target Defects4J release and available storage.
