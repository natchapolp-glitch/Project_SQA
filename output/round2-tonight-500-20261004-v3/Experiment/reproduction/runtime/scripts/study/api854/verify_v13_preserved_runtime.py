"""Fresh reference regression under V13; historical sealed outputs stay unchanged."""
import argparse
from pathlib import Path
from .common import ROOT, sha256, implementation_hashes
from .preparation import encoded
from .fixture_policy import POLICY_V13
from . import verify_v10_recipe_runtime as components
from . import verify_chronology_v11 as chronology
from . import verify_graphics_v12 as graphics
from . import verify_graphics_environment_failure as environment


def verify(output,defects4j):
    output=Path(output);output.mkdir(parents=True,exist_ok=False)
    components.V6=POLICY_V13
    result=components.verify(defects4j)
    result.update(fixture_policy_id=POLICY_V13,adapter_sha256=sha256(__file__))
    (output/'component-receipt.json').write_bytes(encoded(result))
    chronology.POLICY_V11=POLICY_V13
    chronology.verify(defects4j,output/'chronology')
    graphics.POLICY_V12=POLICY_V13
    graphics.OUTPUT=output/'graphics'
    graphics.verify(graphics.OUTPUT,defects4j)
    # The checker and environment controls now bind the fresh V13 Graphics proof.
    environment.POLICY_V12=POLICY_V13
    environment.OUTPUT=graphics.OUTPUT
    environment.verify(output/'graphics-environment')
    r={'status':'pass','fixture_policy_id':POLICY_V13,'runtime_source_sha256':implementation_hashes(),
       'component_cases':64,'chronology_cases':13,'graphics_cases':24,'chart_old_new_pairs':48,
       'component_receipt_sha256':sha256(output/'component-receipt.json'),
       'chronology_receipt_sha256':sha256(output/'chronology/receipt.json'),
       'graphics_receipt_sha256':sha256(output/'graphics/receipt.json'),
       'graphics_environment_receipt_sha256':sha256(output/'graphics-environment/receipt.json'),
       'chronology_buggy_signature':['arrays_bad_order'],'owner_host_acceptance':False,
       'gate_a_passed':False,'primary_results_added':0,'live_requests':0,'queue_mutations':0}
    (output/'receipt.json').write_bytes(encoded(r))
    (output/'checksums.json').write_bytes(encoded({p.relative_to(output).as_posix():sha256(p) for p in sorted(output.rglob('*')) if p.is_file()}))
    return r


if __name__=='__main__':
    p=argparse.ArgumentParser(description=__doc__);p.add_argument('--output',type=Path,required=True);p.add_argument('--defects4j',type=Path,required=True);a=p.parse_args()
    r=verify(a.output,a.defects4j);print({k:r[k] for k in ('status','component_cases','chronology_cases','graphics_cases','chart_old_new_pairs')})
