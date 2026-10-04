#!/usr/bin/env python3
"""Native read-only collector avoids slowing WSL's Java experiment with reporting."""
import argparse
from datetime import datetime, timezone
from pathlib import Path
import time
import nightly_report as report


def main():
    cli = argparse.ArgumentParser(description=__doc__)
    cli.add_argument('--offline', type=Path, required=True)
    cli.add_argument('--ai', type=Path, nargs='+', required=True)
    cli.add_argument('--scope', type=Path, required=True)
    cli.add_argument('--control', type=Path, required=True)
    cli.add_argument('--cutoff', default='2026-10-04T15:30:00+00:00')
    args = cli.parse_args()
    previous = None
    cutoff = datetime.fromisoformat(args.cutoff)
    while True:
        _, stats = report.write_report(args.offline.resolve(), [p.resolve() for p in args.ai],
                                      args.control.resolve() / 'Report', args.scope)
        state = stats['attempted_jobs'], stats['states']
        if state != previous:
            print({'recorded_bugs': stats['recorded_bugs'], 'recorded_projects': stats['recorded_projects'],
                   'recorded_jobs': stats['attempted_jobs'], 'states': stats['states']}, flush=True)
            previous = state
        if (args.control / 'dispatch-ended.json').exists() or datetime.now(timezone.utc) >= cutoff:
            break
        time.sleep(45)


if __name__ == '__main__':
    main()
