"""Retain previous regression plus V13 adversarial scope and four-consumer tests."""
import json
import subprocess
import sys
from .common import ROOT
from .codec_v13 import READY
from .preparation import encoded


if __name__=='__main__':
    previous=json.loads((ROOT/'output/api854-20261003/aom-v12-readiness-v1/regression-command.json').read_bytes())
    command=[sys.executable,*previous[1:],'scripts.study.api854.tests.test_codec_v13','scripts.study.api854.tests.test_preparation_v13_development']
    (READY/'regression-command.json').write_bytes(encoded(command))
    with (READY/'regression-tests.log').open('xb') as stream:
        run=subprocess.run(command,cwd=ROOT,stdout=stream,stderr=subprocess.STDOUT,timeout=1200)
    if run.returncode:raise SystemExit(run.returncode)
    print({'regression_exit_code':0,'log':(READY/'regression-tests.log').relative_to(ROOT).as_posix()})
