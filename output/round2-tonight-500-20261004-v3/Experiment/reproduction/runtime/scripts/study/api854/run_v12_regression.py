"""Retain the complete existing regression plus v12 contract and consumer checks."""
import json
import subprocess
import sys
from .common import ROOT
from .preparation import encoded

if __name__=='__main__':
    out=ROOT/'output/api854-20261003/aom-v12-readiness-v1'
    previous=json.loads((ROOT/'output/api854-20261003/aom-v11-readiness-v1/regression-command.json').read_bytes())
    command=[sys.executable,*previous[1:],'scripts.study.api854.tests.test_graphics_v12','scripts.study.api854.tests.test_preparation_v12_development']
    (out/'regression-command.json').write_bytes(encoded(command))
    with (out/'regression-tests.log').open('xb') as stream:
        result=subprocess.run(command,cwd=ROOT,stdout=stream,stderr=subprocess.STDOUT,timeout=900)
    if result.returncode:raise SystemExit(result.returncode)
    print({'regression_exit_code':0,'log':(out/'regression-tests.log').relative_to(ROOT).as_posix()})
