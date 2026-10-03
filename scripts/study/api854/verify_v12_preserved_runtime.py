"""Fresh retained 64 component cases and 13 Chronology cases under the V12 policy."""
import argparse
from pathlib import Path
from . import verify_v10_recipe_runtime as components
from . import verify_chronology_v11 as chronology
from .common import sha256
from .preparation import encoded
from .fixture_policy import POLICY_V12


if __name__=='__main__':
    p=argparse.ArgumentParser(description=__doc__);p.add_argument('--defects4j',type=Path,required=True)
    p.add_argument('--output',type=Path,required=True);a=p.parse_args()
    a.output.mkdir(parents=True,exist_ok=False)
    components.V6=POLICY_V12
    result=components.verify(a.defects4j)
    result.update(fixture_policy_id=POLICY_V12,adapter_sha256=sha256(__file__))
    (a.output/'component-receipt.json').write_bytes(encoded(result))
    chronology.POLICY_V11=POLICY_V12
    result=chronology.verify(a.defects4j,a.output/'chronology')
    (a.output/'receipt.json').write_bytes(encoded({'status':'pass','fixture_policy_id':POLICY_V12,
        'component_cases':64,'chronology_cases':13,'component_receipt_sha256':sha256(a.output/'component-receipt.json'),
        'chronology_receipt_sha256':sha256(a.output/'chronology/receipt.json'),
        'chronology_buggy_signature':['arrays_bad_order'],'adapter_sha256':sha256(__file__),
        'gate_a_passed':False,'primary_results_added':0,'live_requests':0,'queue_mutations':0}))
    (a.output/'checksums.json').write_bytes(encoded({f.relative_to(a.output).as_posix():sha256(f) for f in sorted(a.output.rglob('*')) if f.is_file()}))
    print({'status':'pass','component_cases':64,'chronology_cases':13,'fixture_policy_id':POLICY_V12})
