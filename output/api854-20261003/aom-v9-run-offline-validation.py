"""Record complete offline integration results and their raw log, without replacement."""
import contextlib
import argparse
from datetime import datetime, timezone
from pathlib import Path
import sys
import tempfile
import time
import unittest

ROOT = Path(__file__).resolve().parents[2]
sys.path.insert(0,str(ROOT))
from scripts.study.api854.common import implementation_hashes, sha256, write_json


class Tee:
    def __init__(self, *streams):
        self.streams = streams
    def write(self, text):
        for stream in self.streams:
            stream.write(text)
            stream.flush()
        return len(text)
    def flush(self):
        for stream in self.streams:
            stream.flush()


if __name__=='__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--attempt', default='v2', choices=('v2','v3'))
    args = parser.parse_args()
    base = Path(__file__).parent
    log = base/f'aom-v9-offline-integration-validation-{args.attempt}.log'
    receipt = base/f'aom-v9-offline-integration-validation-{args.attempt}.json'
    if receipt.exists():
        raise FileExistsError(receipt)
    before = implementation_hashes()
    temporary = ROOT/'output/v9-test-temp'
    temporary.mkdir(exist_ok=True)
    tempfile.tempdir = str(temporary.resolve())
    reports = []
    started = datetime.now(timezone.utc).isoformat()
    with log.open('x',encoding='utf-8',newline='\n') as stream:
        tee = Tee(stream,sys.stderr)
        for name, directory, top in (
            ('api854','scripts/study/api854/tests','.'),
            ('study','scripts/study/tests',None)):
            begin = time.monotonic()
            suite = unittest.TestLoader().discover(str(ROOT/directory),top_level_dir=str(ROOT/top) if top else None)
            tee.write('Suite: '+name+'\n')
            with contextlib.redirect_stdout(tee):
                result = unittest.TextTestRunner(stream=tee,verbosity=1).run(suite)
            reports.append({'suite':name,'tests_run':result.testsRun,
                'passed':result.testsRun-len(result.failures)-len(result.errors)-len(result.skipped),
                'failures':[str(test) for test,error in result.failures],
                'errors':[str(test) for test,error in result.errors],
                'skipped':[{'test':str(test),'reason':reason} for test,reason in result.skipped],
                'duration_seconds':time.monotonic()-begin,'successful':result.wasSuccessful()})
    unchanged = before == implementation_hashes()
    success = unchanged and all(r['successful'] for r in reports)
    write_json(receipt,{'status':'pass' if success else 'fail','started_at_utc':started,
        'ended_at_utc':datetime.now(timezone.utc).isoformat(),'suites':reports,
        'runtime_unchanged_during_test_run':unchanged,'runtime_source_sha256':before,
        'validation_runner_sha256':sha256(__file__),'raw_log_sha256':sha256(log),
        'selected_protocol_sha256':sha256(base/'aom-continuation-v9-integrated/protocol.proposal.json'),
        'selected_preparation_index_sha256':sha256(base/'prepare-v9-twenty-bug-development/index.json'),
        'scope':'Offline current v9 integration; fake/local transports, no primary result or human approval'})
    print({'status':'pass' if success else 'fail','tests_run':sum(r['tests_run'] for r in reports)})
    raise SystemExit(0 if success else 1)
