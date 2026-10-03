from pathlib import Path
import sys
sys.path.insert(0,str(Path.cwd()))
sys.path.insert(0,str(Path.cwd()/'scripts/study'))
from scripts.study.api854.fixture_development import run
from scripts.study.api854.fixture_policy import POLICY_V6
run([('Math',1)],Path('.local/api854/beam-v8-math-development-v1'),Path('/home/team/sqa-round2/beam-buffer-worktrees'),'/home/team/sqa-round2/defects4j/framework/bin/defects4j',POLICY_V6)

