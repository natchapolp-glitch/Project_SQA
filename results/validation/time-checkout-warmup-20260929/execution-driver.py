from pathlib import Path
import sys
sys.path.insert(0, 'scripts/study')
from evaluate import run_command, validate_worktree

d4j = '/home/aomsin/sqa-round2/defects4j/framework/bin/defects4j'
output = Path('results/validation/time-checkout-warmup-20260929').resolve()
output.mkdir(exist_ok=False)
for revision in ('f', 'b'):
    checkout = Path('/home/aomsin/sqa-round2/worktrees/Time/1') / revision
    if checkout.exists():
        validate_worktree(checkout, 'Time', '1' + revision)
    else:
        stage = run_command([d4j, 'checkout', '-p', 'Time', '-v', '1' + revision, '-w', str(checkout)], output, output / ('checkout-' + revision), 900)
        if stage['exit_code'] != 0 or stage['timed_out']:
            raise RuntimeError('Time checkout failed; see saved log')
    stage = run_command([d4j, 'compile', '-w', str(checkout)], output, output / ('compile-' + revision), 900)
    if stage['exit_code'] != 0 or stage['timed_out']:
        raise RuntimeError('Time compile failed; see saved log')
    print('Time-1' + revision + ' compiled', flush=True)
